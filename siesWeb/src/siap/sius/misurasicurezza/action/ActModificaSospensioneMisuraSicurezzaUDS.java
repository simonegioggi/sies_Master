package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.misurasicurezza.model.PeriodoEsecuzioneMisuraModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaSospensioneMisuraSicurezzaUDS extends ActMisuraSicurezza 
	implements  ICostantiSiusMisuraSicurezza
	{
	 	public String processRequest() throws F3BException 
	 	{
			  
		    // 	 83 tipo scadenzario inizio o ripresa
		    // 	 84 tipo scadenzario sospensione
		    //   01 Flag Motivo Inizio
		    //   02 Flag Motivo Ripresa
		    //   03 Flag Motivo Sospensione
	 		
		    // Recupera la key del record da Modifica 
		    BigDecimal lIdPeriodoAltraMisura     = getRequestBigDecimalParameter
		    	( ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA) ;
		 
		    // Recupera i dati del record da modificare  
		    IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		    PeriodoAltraMisuraModel lPerMod = lCtrl.ExRicercaPeriodoAltraMisuraById( lIdPeriodoAltraMisura);
		    

			//===========================================================
		    // Restituisce la msg se i dati non sono stati recuperati. 
		    //===========================================================
		    if (lPerMod==null ){ 
			      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			      // Specificare eventualmente la jump page dove verrà ridirezionata la 
			      // PG_MEMSAGE quando viene premuto OK. Se non viene attualizzato il 
			      // parametro GOTO_PAGE, la PG_MEMSAGE effettua un history.go(-1) 
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
		  

	        //ricerca record misura sicurezza per controllo data termine attuale
	        // ed eventualmente aggiunta dei giorni da recuperare
		    IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		    EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);
			
			// calcola da data scadenza del periodo qualora siano stati digitati i quantum
			// calcola la nuova data termine attuale qualora ci siano de gg da recuperare
			// salva il tutto nel modello comune lPerEse
			PeriodoEsecuzioneMisuraModel lPerEse = calcoloDataScadenza(lPerMod,lEMSModel);
			lPerMod = lPerEse.getPeriodoAltraMisura();
		
			// calcolo dei quantum misura sicurezza espiata 
			CalendarModel lCalMode = new CalendarModel();
			lCalMode = quantumEspiata(lPerMod,lEMSModel);
		    
			// calcolo dei quantum misura sicurezza totale
			CalendarModel lCalModr = new CalendarModel();
			lCalModr = quantumTotale(lEMSModel);
			
			lEMSModel =lPerEse.getEsecuzioneMisuraSicurezza();
			
			// per completare il calcolo della misura residua da espiare
			// sottrarre quella gia espiata
			CalendarUtil lCalUtil = new CalendarUtil();
			CalendarModel lCalModre = new CalendarModel();
			lCalModre = lCalUtil.sottraiGiorni(lCalModr, lCalMode);
			
			lPerMod = riempioquantumEspiataeResidua(lPerMod,lCalMode,lCalModre);
			
		
		    if ( lPerMod.getDaRecuperare().equals("1") ){				
		        lEMSModel.setCodOperatoreAggiornamento (getCodUtenteConnesso()); 
			    lEMSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			    lEMSModel.setDataAggiornamento( DateUtils.getSysDate());
		    }
		    
		    /*****************************************************************************
			   * 6 modifica Periodo Altra Misura + Ems 
			   * modifica scadenzario 
			   * eventuale modifica altro scadenzario
			   ****************************************************************************/ 
		    
			// recupero lo scadenzario 84 
			IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
			ScadenzarioSiusModel lScaMod84 = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),"84");

			// riempio i campi dello scadenzario 84 con l'id evento e i valori del periodo
			lScaMod84 = riempioScadenzario(lPerMod, lScaMod84,"84");
			
			if (lScaMod84.getDataFineScadenza()== null) 
				lScaMod84.setDataFineScadenza(lEMSModel.getDataTermineAttuale());
		
//			 se non esistono gg da recuperare lo scadenzario 83 non va modificato quindi lo metto a null
			ScadenzarioSiusModel  lScaMod83= null;
			if (lPerMod.getDaRecuperare().equals("1")){
				// ricerco lo scadenzario 83 
				// avvaloro la Data Termine Attuale ci sono da aggiungere i gg da recuperare
				lScaMod83 = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),"83");
				lScaMod83.setDataFineScadenza(lEMSModel.getDataTermineAttuale());
			}
			lPerMod = lCtrl.ExModificaPeriodoAltraMisura(lScaMod84,lScaMod83,lPerMod, lEMSModel);
			
		    setRequestAttribute("lCalMode",  lCalMode);
		    setRequestAttribute("lCalModre",  lCalModre);
		    setRequestAttribute("lEMSModel",  lEMSModel);
		    //====================================================================== 
		    // Prepara la pagina di destinazione
		    // Viene restituita la pagina di dettaglio con i dati appena inseriti
		    //====================================================================== 
		    String lPage="";
		    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActLoadDettaglioSospensioneMisuraSicurezzaUDS";
		    lPage += "&" + CAMPO_ID_PERIODO_ALTRA_MISURA + "=" + lPerMod.getIdPeriodoAltraMisura().toString();
		    return lPage;
		  }
	}
		

	
	

