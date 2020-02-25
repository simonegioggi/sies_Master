package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActValidaInizioSanzioneSostitutivaUDS extends ActSanzioneSostitutiva 
implements  ICostantiSanzioneSostitutiva
	{
	public String processRequest() throws F3BException {
	    PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
	    
	    //	  recupero l'id del period
	    BigDecimal lIdPeriodoAltraSanzione     = getRequestBigDecimalParameter ( ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE) ;
	    lPerMod.setIdPeriodoAltraSanzione(lIdPeriodoAltraSanzione);
	 
	    // Recupera il controller ed effettua la ricerca 
	    IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote(); 
	    lPerMod = lCtrl.ExRicercaPeriodoAltraSanzioneById( lIdPeriodoAltraSanzione);
		
	    // recupero l'id del periodo e la data scadenza
	    lPerMod.setDataScadenza( getRequestDateParameter       ( CAMPO_ANNO_DATA_SCADENZA,CAMPO_MESE_DATA_SCADENZA,CAMPO_GIORNO_DATA_SCADENZA) );
	  
	    //recupero dell' ID del fascicolo
	    BigDecimal lIdFasSius =  lPerMod.getFasSiuIdFascicoloSius();

	    // Modifica del record esecuzione_sanzione_sost
	    // inserimento DATA_TERMINE_ATTUALE  
	    IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
	    EsecuzioneSanzioneSostitutivaModel lEssMod = lESSCtrl.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);
	    if (lEssMod == null || lEssMod.getIdEsecuzioneSanzioneSost().equals(null))
	    	throw new SIUSException( SIUSException.USER_MESSAGE, "Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!" );
	     
	    if (lEssMod.getDataInizioSanzione() == null)
	    	lEssMod.setDataInizioSanzione (lPerMod.getDataInizioEsecuzione());    
	    if (lEssMod.getDataTermineIniziale() == null)
	    	lEssMod.setDataTermineIniziale( lPerMod.getDataScadenza());
	    if (lEssMod.getDataTermineAttuale() == null 
	    		|| !lEssMod.getDataTermineAttuale().equals(lPerMod.getDataScadenza())){
		    lEssMod.setDataTermineAttuale(  lPerMod.getDataScadenza());
		    lEssMod.setCodOperatoreAggiornamento (getCodUtenteConnesso()); 
		    lEssMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); 
		    lEssMod.setDataAggiornamento( DateUtils.getSysDate());
	    }
		
      
      
//	  calcolo dei quantum sanzione sostitutiva espiata
		CalendarModel lCalMode = new CalendarModel();
		lCalMode = quantumEspiata(lPerMod,lEssMod);
	    
    
		// calcolo dei quantum sanzione sostitutiva totale 
		CalendarModel lCalModr = new CalendarModel();
		lCalModr = quantumTotale(lEssMod);
    
    
		// per completare il calcolo della sanzione residua da espiare
		// sottrarre quella gia espiata
		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalModre = new CalendarModel();
       
    
		lCalModre = lCalUtil.sottraiGiorni(lCalModr, lCalMode);
	      
		lPerMod = riempioquantumEspiataeResidua(lPerMod,lCalMode,lCalModre);
		ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();

 	    //   01 Flag Motivo Inizio
 	    //   02 Flag Motivo Ripresa
 	    //   03 Flag Motivo Sospensione
		if (lPerMod.getFlagMotivo().equals("01"))  {
			EventoModel lEveMod = preparoEvento(lPerMod);
			lScaMod = riempioScadenzario(lPerMod,lScaMod,"80");

		  /*****************************************************************************
		   * 3 modifica Periodo Altra Sanzione + Ess 
		   * Inserimento Evento + scadenzario
		   ****************************************************************************/		
			lCtrl.ExModificaPeriodoAltraSanzione(lEveMod,lScaMod,lPerMod ,lEssMod);
		}else {
			IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
			lScaMod = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),"80");
			
			//devo mettere a null la data iniziale per non ricoprirla nello scadenzario
			PeriodoAltraSanzioneModel lPerModSca = new PeriodoAltraSanzioneModel(lPerMod);
			lPerModSca.setDataInizioEsecuzione(null);
			lScaMod = riempioScadenzario(lPerModSca,lScaMod,"80");
		  /*****************************************************************************
		   * 6 modifica Periodo Altra Sanzione + Ess 
		   * modifica scadenzario
		   * eventuale modifica scadenzario tipo 80 (sanzione) aggiungo gg da recuperare
		   ****************************************************************************/
			// non è necessario modificare entrambi gli scadenzari
			// non siamo nel caso della sospensione
			lCtrl.ExModificaPeriodoAltraSanzione(lScaMod,null,lPerMod ,lEssMod);
		}
	    //====================================================================== 
	    // Prepara la pagina di destinazione
	    // Viene restituita la pagina di dettaglio con i dati appena inseriti
	    //======================================================================   
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.sanzionesostitutiva.action.ActLoadDettaglioInizioSanzioneSostitutivaUDS";
	    return lPage;
	  }
}