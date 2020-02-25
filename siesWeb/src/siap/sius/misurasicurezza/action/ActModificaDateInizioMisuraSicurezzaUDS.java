package siap.sius.misurasicurezza.action;
/**
* <p>Title: ActModificaDateInizioMisuraSicurezzaUDS</p>
* <p>Description: Classe Action per la modifica della Data Inizio Esecuzione e Data Fine Pena</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Engineering</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

//import siap.sico.evento.model.EventoModel;
//import siap.sius.SIUSException;
//import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
//import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;
//import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaDateInizioMisuraSicurezzaUDS extends ActMisuraSicurezza 
implements  ICostantiSiusMisuraSicurezza
{

	 /*****************************************************************************
	  * Azione di Modifica delle Date Inizio Esecuzione e Fine Pena
	  * @return Nome della pagina JSP da visualizzare
	  * al termine dell'elaborazione
	  * @throws F3BException
	  *****************************************************************************/
	  public String processRequest() throws F3BException {
		  
	    // Recupera la key del record da Modifica 
	    BigDecimal lIdPeriodoAltraMisura     = getRequestBigDecimalParameter ( ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA) ;

//TODO modifica del 11/11/2013 verificare
	    // ricerca del fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

	    // ricerca record esecuzione_misura_sicurezza
	    IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
	    EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);
	    if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza().equals(null))
	      throw new SIUSException( SIUSException.USER_MESSAGE, "Attenzione! ESECUZIONE MISURA SICUREZZA Assente!" );
	    
	    // Recupera i dati del record da modificare  
	    IPeriodoAltraMisura lCtrlPAM = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
	    PeriodoAltraMisuraModel lPerMod = lCtrlPAM.ExRicercaPeriodoAltraMisuraById( lIdPeriodoAltraMisura);
	    
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
	    
	    // Modifica Tabella PERIODO_ALTRA_MISURA
	    // recupero la data inizio esecuzione
	    Date dataInizioEsecuzione = getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO_ESECUZIONE,CAMPO_MESE_DATA_INIZIO_ESECUZIONE,CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE);
	    lPerMod.setDataInizioEsecuzione ( dataInizioEsecuzione );

	    // recupero la data fine pena
	    Date dataFineEsecuzione = getRequestDateParameter( CAMPO_ANNO_DATA_SCADENZA,CAMPO_MESE_DATA_SCADENZA,CAMPO_GIORNO_DATA_SCADENZA);
	    lPerMod.setDataScadenza( dataFineEsecuzione );
	    
		// setto i dati dell'utente che opera la modifica
	    lPerMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	    lPerMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	    lPerMod.setDataAggiornamento(DateUtils.getSysDate());

	    // Recupera il controller ed effettua la modifica periodo
	    lPerMod = lCtrlPAM.ExModificaDateInizioMisuraSicurezza(lPerMod);

//TODO modifica del 11/11/2013 verificare
	    // Modifica Tabella ESECUZIONE_MISURA_SICUREZZA
	    // Si settano la Data Inizio Esecuzione, e la Data Termine Iniziale e Attuale 
	    // date dalla somma della data inizio esecuzione + durata della misura
	    lEMSModel.setDataInizioMisura( dataInizioEsecuzione );
	    
	    CalendarModel lCal = new CalendarModel();
	    lCal.setDataInizio( dataInizioEsecuzione );
	    lCal.setDataFine( dataFineEsecuzione );
	    CalendarUtil lCalUtil = new CalendarUtil();
	    lCal = lCalUtil.CalcolaNumGiorniMesiAnni(lCal, false);

	    ICalcoloPena lCalcolaPena = SIEPLookupRemote.getCalcoloPenaRemote();
	    lEMSModel.setDataTermineIniziale( lCalcolaPena.exCalcolaNuovaDataFine( dataInizioEsecuzione, lCal, false) );
	    lEMSModel.setDataTermineAttuale( lCalcolaPena.exCalcolaNuovaDataFine( dataInizioEsecuzione, lCal, false));
	    
	    lEMSModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	    lEMSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	    lEMSModel.setDataAggiornamento(DateUtils.getSysDate());
	    
	    // Recupera il controller ed effettua la modifica esecuzione Misura Sicurezza
	    lEMSModel = lEMSCtrl.ExModificaEsecuzioneMisuraSicurezza(lEMSModel);
	    
	    //====================================================================== 
	    // Prepara la pagina di destinazione
	    // Viene restituita la pagina di dettaglio con i dati appena inseriti
	    //====================================================================== 
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misurasicurezza.action.ActLoadDettaglioInizioMisuraSicurezzaUDS";
	    return lPage;
	  }
}
	
