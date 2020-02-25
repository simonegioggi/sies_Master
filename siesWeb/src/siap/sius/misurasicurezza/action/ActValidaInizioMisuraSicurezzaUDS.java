package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActValidaInizioMisuraSicurezzaUDS extends ActMisuraSicurezza 
implements  ICostantiSiusMisuraSicurezza
	{
	public String processRequest() throws F3BException {
	    PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
	    
	    //	  recupero l'id del period
	    BigDecimal lIdPeriodoAltraMisura     = getRequestBigDecimalParameter ( ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA) ;
	    lPerMod.setIdPeriodoAltraMisura(lIdPeriodoAltraMisura);
	 
	    // Recupera il controller ed effettua la ricerca 
	    IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote(); 
	    lPerMod = lCtrl.ExRicercaPeriodoAltraMisuraById( lIdPeriodoAltraMisura);
		
	    // recupero l'id del periodo e la data scadenza
	    lPerMod.setDataScadenza( getRequestDateParameter       ( CAMPO_ANNO_DATA_SCADENZA,CAMPO_MESE_DATA_SCADENZA,CAMPO_GIORNO_DATA_SCADENZA) );
	  
	    //recupero dell' ID del fascicolo
	    BigDecimal lIdFasSius =  lPerMod.getFasSiuIdFascicoloSius();

	    // Modifica del record esecuzione_misura_sicurezza
	    // inserimento DATA_TERMINE_ATTUALE  
	    IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
	    EsecuzioneMisuraSicurezzaModel lEmsMod = lEMSCtrl.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);
	    if (lEmsMod == null || lEmsMod.getIdEsecuzioneMisuraSicurezza().equals(null))
	    	throw new SIUSException( SIUSException.USER_MESSAGE, "Attenzione! ESECUZIONE MISURA SICUREZZA Assente!" );
	     
	    if (lEmsMod.getDataInizioMisura() == null)
	    	lEmsMod.setDataInizioMisura (lPerMod.getDataInizioEsecuzione());    
	    if (lEmsMod.getDataTermineIniziale() == null)
	    	lEmsMod.setDataTermineIniziale( lPerMod.getDataScadenza());
	    if (lEmsMod.getDataTermineAttuale() == null 
	    		|| !lEmsMod.getDataTermineAttuale().equals(lPerMod.getDataScadenza())){
	    	lEmsMod.setDataTermineAttuale(  lPerMod.getDataScadenza());
	    	lEmsMod.setCodOperatoreAggiornamento (getCodUtenteConnesso()); 
	    	lEmsMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); 
	    	lEmsMod.setDataAggiornamento( DateUtils.getSysDate());
	    }
		
      
      
//	  calcolo dei quantum misura sicurezza espiata
		CalendarModel lCalMode = new CalendarModel();
		lCalMode = quantumEspiata(lPerMod,lEmsMod);
	    
    
		// calcolo dei quantum misura sicurezza totale 
		CalendarModel lCalModr = new CalendarModel();
		lCalModr = quantumTotale(lEmsMod);
    
    
		// per completare il calcolo della misura residua da espiare
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
			lScaMod = riempioScadenzario(lPerMod,lScaMod,"83");

		  /*****************************************************************************
		   * 3 modifica Periodo Altra Misura + Ems 
		   * Inserimento Evento + scadenzario
		   ****************************************************************************/		
			lCtrl.ExModificaPeriodoAltraMisura(lEveMod,lScaMod,lPerMod ,lEmsMod);
		}else {
			IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
			lScaMod = lCtrlSca.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),"83");
			
			//devo mettere a null la data iniziale per non ricoprirla nello scadenzario
			PeriodoAltraMisuraModel lPerModSca = new PeriodoAltraMisuraModel(lPerMod);
			lPerModSca.setDataInizioEsecuzione(null);
			lScaMod = riempioScadenzario(lPerModSca,lScaMod,"83");
		  /*****************************************************************************
		   * 6 modifica Periodo Altra Misura + Ems 
		   * modifica scadenzario
		   * eventuale modifica scadenzario tipo 83 (misura) aggiungo gg da recuperare
		   ****************************************************************************/
			// non è necessario modificare entrambi gli scadenzari
			// non siamo nel caso della sospensione
			lCtrl.ExModificaPeriodoAltraMisura(lScaMod,null,lPerMod ,lEmsMod);
		}
	    //====================================================================== 
	    // Prepara la pagina di destinazione
	    // Viene restituita la pagina di dettaglio con i dati appena inseriti
	    //======================================================================   
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActLoadDettaglioInizioMisuraSicurezzaUDS";
	    return lPage;
	  }
}