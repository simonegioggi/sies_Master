package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActInserisciAnnEstinzioneReato</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Agile</p>
 * <p> @author Luigi</p>
 * @version 1.0
 */
public class ActInserisciAnnEstinzioneReato extends ActInserisciRicEstinzioneReato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  public String processRequest() throws Exception
  {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("ActInserisciAnnEstinzioneReato: inizio");

	  // Inizializzazioni
	   EventoNotificaModel lEve = new EventoNotificaModel();
	   
	  // VALORIZZAZIONE EVENTO 
	  EventoModel lEveModel = leggiEvento();
	  lEve.setEvento(lEveModel);
	    
	  // VALORIZZAZIONE ANNOTAZIONE MANUALE 
	  AnnotazioneManualeModel lAnnMod = letturaAnnotazione();
	  
	  // VALORIZZAZIONE NOTIFICA
      NotificaModel lNotifiche[] = new NotificaModel[0];
      lEve.setNotifiche(lNotifiche);

	 // Viene utilizzata una funzione di inserimento che prevede anche le Notifiche 
	 // che non vengono in questo caso utilizzate.
	 IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
	 lAnnMod = lAnnManCtrl.ExInserisciAnnotazioneManualeEventoNotifica(lAnnMod, lEve);

	 // Costruzione della pagina di redirect
	    RedirectTo lRedirectTo = new RedirectTo();
	    lRedirectTo.setPage(IWebConstants.PG_MAIN);
	    lRedirectTo.setAction("siap.siep.penasospesa.action.ActDettaglioAnnEstinzioneReato");
	    lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lAnnMod.getEveIdEvento().toString() );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ActInserisciAnnEstinzioneReato: fine");

    return lRedirectTo.toString()  ;
  }
  
  /**
   * La funzione utilizza la funzione letturaEvento() 
   * dell'Ancestor per leggere i dati dalla form e ne 
   * modifica i campi specifici.
   * @return
   * @throws Exception
   */
  protected EventoModel leggiEvento() throws Exception
  {
	    EventoModel lEveModel = letturaEvento();
	    lEveModel.setCodTipoEvento("01");
	    lEveModel.setCodTipoProvvedimento("25");  //Annotazione 
	    lEveModel.setCodTipoProvvedimento("57");  //Annotazione GE

	    return lEveModel;    
  }
   
  private AnnotazioneManualeModel letturaAnnotazione() throws Exception 
  {	  
	AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
	
    // ID Fascicolo SIEP dalla sessione
    BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

	// Valorizzazione campi specifici
	lAnnMod.setCodTipoAnnotazione("016"); //Gestione Pene Sospese
    lAnnMod.setFlagAppProvvisoria("-");
	lAnnMod.setFlagValidato("S");
    lAnnMod.setCodFonte("-");
    lAnnMod.setCodSottonumerazione("-");
    lAnnMod.setCodCausaleComputo("-");
    lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lAnnMod.setDataInserimento(DateUtils.getSysDate());
    lAnnMod.setCodDpr("-");
    lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
     
    // I dati relativi alla Ordinanza del GE vengono letti dalla request
    lAnnMod.setAnnoGe(getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ANNO_GE));
    lAnnMod.setNumeroGe(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_NUMERO_GE));
    
    lAnnMod.setDataGE(getRequestDateParameter(
    		ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE,
    		ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE,
    		ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE));    
    
	// Lettura  dati dell'Autorità Emittente
	String lDescrComune        = getRequestStringParameter(CAMPO_SEDE_UFFICIO_GE);
	String lCodTipoUfficioEmi = getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_GE );
	lAnnMod.setCodTipoUfficioSiep( lCodTipoUfficioEmi);
	lAnnMod.setCodLuogoUfficioSiep(((ComuneModel )getCodComuneByDescr(lDescrComune)).getCodComune());
	
	// Questo solo per controllo validità dati
	getCodUfficioByCodTipoUfficioDescrComune( lCodTipoUfficioEmi, lDescrComune );

	// Lettura Note
    lAnnMod.setMotivazioni( getRequestStringParameter(  CAMPO_NOTE));

	
    return lAnnMod;
  }

}