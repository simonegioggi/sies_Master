package siap.siep.sollecitoesitotrasmissione.action;


/**
* <p>Title: ActInserisciSollecitoEsitoTrasmissione</p>
* <p>Description: Classe Action per l'inserimento di SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.sollecitoesitotrasmissione.controller.ISollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;

public class ActInserisciSollecitoEsitoTrasmissione extends ActionSiap implements ICostantiSollecitoEsitoTrasmissione {
  Logger logger = Logger.getLogger("actionLogger");

 /*****************************************************************************
  * Azione di Inserimento del SollecitoEsitoTrasmissione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException {
    SollecitoEsitoTrasmissioneModel lSolMod = new SollecitoEsitoTrasmissioneModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
    lSolMod.setIdSollecito               ( getRequestBigDecimalParameter ( CAMPO_ID_SOLLECITO) );
    lSolMod.setCodUffSollecitato         ( getRequestStringParameter     ( CAMPO_COD_UFF_SOLLECITATO) );
    lSolMod.setOggettoMsSollecito        ( getRequestStringParameter     ( CAMPO_OGGETTO_MS_SOLLECITO) );
    lSolMod.setOggettoMsSollecitato      ( getRequestStringParameter     ( CAMPO_OGGETTO_MS_SOLLECITATO) );
    lSolMod.setDataInvioMsSollecitato    ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INVIO_MS_SOLLECITATO,CAMPO_MESE_DATA_INVIO_MS_SOLLECITATO,CAMPO_GIORNO_DATA_INVIO_MS_SOLLECITATO) );
    lSolMod.setMesIdMessaggioSollecitato ( getRequestBigDecimalParameter ( CAMPO_MES_ID_MESSAGGIO_SOLLECITATO) );
    lSolMod.setCodUffInoltrante          ( getRequestStringParameter     ( CAMPO_COD_UFF_INOLTRANTE) );
    lSolMod.setDataInoltro               ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INOLTRO,CAMPO_MESE_DATA_INOLTRO,CAMPO_GIORNO_DATA_INOLTRO) );
    lSolMod.setMesIdMessaggioInoltro     ( getRequestBigDecimalParameter ( CAMPO_MES_ID_MESSAGGIO_INOLTRO) );
    lSolMod.setEveIdEvento               ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO) );
    lSolMod.setFasSieIdFascicoloSiep     ( getRequestBigDecimalParameter ( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
    lSolMod.setCodOperatoreInserimento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lSolMod.setDataInserimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
    lSolMod.setCodUfficioInserimento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lSolMod.setCodOperatoreAggiornamento ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lSolMod.setDataAggiornamento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO,CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lSolMod.setCodUfficioAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua l'inserimento 
    //=================================================== 
    ISollecitoEsitoTrasmissione lCtrl = SIEPLookupRemote.getSollecitoEsitoTrasmissioneRemote();
    SollecitoEsitoTrasmissioneModel lSolRetMod = new SollecitoEsitoTrasmissioneModel();
    lSolRetMod=lCtrl.ExInserisciSollecitoEsitoTrasmissione(lSolMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.jms.sollecitoesitotrasmissione.action.ActLoadDettaglioSollecitoEsitoTrasmissione";
    lPage += "&" + CAMPO_ID_SOLLECITO + "=" + lSolRetMod.getIdSollecito().toString();

    return lPage;
  }
}