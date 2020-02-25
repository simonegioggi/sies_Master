package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.sanzionesostitutiva.model.PeriodoEsecuzioneSanzioneModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaSospensioneSanzioneSostitutivaUDS extends ActSanzioneSostitutiva 
	implements  ICostantiSanzioneSostitutiva
	{
	 	public String processRequest() throws F3BException 
	 	{
			  
		    // 	 80 tipo scadenzario inizio o ripresa
		    // 	 81 tipo scadenzario sospensione
		    //   01 Flag Motivo Inizio
		    //   02 Flag Motivo Ripresa
		    //   03 Flag Motivo Sospensione
	 		
		    // Recupera la key del record da Modifica 
		    BigDecimal lIdPeriodoAltraSanzione     = getRequestBigDecimalParameter
		    	( ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE) ;
		 
		    // Recupera i dati del record da modificare  
		    IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		    PeriodoAltraSanzioneModel lPerMod = lCtrl.ExRicercaPeriodoAltraSanzioneById( lIdPeriodoAltraSanzione);
		    

			//===========================================================
		    // Restituisce la msg se i dati non sono stati recuperati. 
		    //===========================================================
		    if (lPerMod==null ){ 
			      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			      // Specificare eventualmente la jump page dove verrà ridirezionata la 
			      // PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il 
			      // parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1) 
			      // n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo 
			      //      della root_dir es /siap/frame.htm  
			      setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR); 
			      return IWebConstants.PG_MESSAGE;
		    }	  
		    
		    //recupero dell' ID del fascicolo
		    BigDecimal lIdFasSius =  lPerMod.getFasSiuIdFascicoloSius();
		    
		    //recupero i dati in maschera
			lPerMod = recuperoDatiMaschera(lPerMod);
		   
			// setto i dati dell'utente che opera la modifica
		    lPerMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		    lPerMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		    lPerMod.setDataAggiornamento(DateUtils.getSysDate());
		  

	        //ricerca record sanzione sostitutiva per controllo data termine attuale
	        // ed eventualmente aggiunta dei giorni da recuperare
		    IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		    EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);
			
			// calcola da data scadenza del periodo qualora siano stati digitati i quantum
			// calcola la nuova data termine attuale qualora ci siano de gg da recuperare
			// salva il tutto nel modello comune lPerEse
			PeriodoEsecuzioneSanzioneModel lPerEse = calcoloDataScadenza(lPerMod,lESSModel);
			lPerMod = lPerEse.getPeriodoAltraSanzione();
		
			// calcolo dei quantum sanzione sostitutiva espiata 
			CalendarModel lCalMode = new CalendarModel();
			lCalMode = quantumEspiata(lPerMod,lESSModel);
		    
			// calcolo dei quantum sanzione sostitutiva totale
			CalendarModel lCalModr = new CalendarModel();
			lCalModr = quantumTotale(lESSModel);
			
			lESSModel =lPerEse.getEsecuzioneSanzioneSostitutiva();
			
			// per completare il calcolo della sanzione residua da espiare
			// sottrarre quella gia espiata
			CalendarUtil lCalUtil = new CalendarUtil();
			CalendarModel lCalModre = new CalendarModel();
			lCalModre = lCalUtil.sottraiGiorni(lCalModr, lCalMode);
			
			lPerMod = riempioquantumEspiataeResidua(lPerMod,lCalMode,lCalModre);
			
		
		    if ( lPerMod.getDaRecuperare().equals("1") ){				
		        lESSModel.setCodOperatoreAggiornamento (getCodUtenteConnesso()); 
			    lESSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			    lESSModel.setDataAggiornamento( DateUtils.getSysDate());
		    }
		    
		    /*****************************************************************************
			   * 6 modifica Periodo Altra Sanzione + Ess 
			   * modifica scadenzario 
			   * eventuale modifica altro scadenzario
			   ****************************************************************************/ 
		    
			// recupero lo scadenzario 81 
			IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
			ScadenzarioSiusModel lScaMod81 = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),"81");

			// riempio i campi dello scadenzario 81 con l'id evento e i valori del periodo
			lScaMod81 = riempioScadenzario(lPerMod, lScaMod81,"81");
			
			if (lScaMod81.getDataFineScadenza()== null) 
				lScaMod81.setDataFineScadenza(lESSModel.getDataTermineAttuale());
		
//			 se non esistono gg da recuperare lo scadenzario 80 non va modificato quindi lo metto a null
			ScadenzarioSiusModel  lScaMod80= null;
			if (lPerMod.getDaRecuperare().equals("1")){
				// ricerco lo scadenzario 80 
				// avvaloro la Data Termine Attuale ci sono da aggiungere i gg da recuperare
				lScaMod80 = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),"80");
				lScaMod80.setDataFineScadenza(lESSModel.getDataTermineAttuale());
			}
			lPerMod = lCtrl.ExModificaPeriodoAltraSanzione(lScaMod81,lScaMod80,lPerMod, lESSModel);
			
		    setRequestAttribute("lCalMode",  lCalMode);
		    setRequestAttribute("lCalModre",  lCalModre);
		    setRequestAttribute("lESSModel",  lESSModel);
		    //====================================================================== 
		    // Prepara la pagina di destinazione
		    // Viene restituita la pagina di dettaglio con i dati appena inseriti
		    //====================================================================== 
		    String lPage="";
		    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.sanzionesostitutiva.action.ActLoadDettaglioSospensioneSanzioneSostitutivaUDS";
		    lPage += "&" + CAMPO_ID_PERIODO_ALTRA_SANZIONE + "=" + lPerMod.getIdPeriodoAltraSanzione().toString();
		    return lPage;
		  }
	}
		

	
	

