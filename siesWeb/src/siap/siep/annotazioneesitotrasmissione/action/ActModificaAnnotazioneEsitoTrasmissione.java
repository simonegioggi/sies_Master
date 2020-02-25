package siap.siep.annotazioneesitotrasmissione.action;


/**
* <p>Title: ActModificaAnnotazioneEsitoTrasmissione</p>
* <p>Description: Classe Action per la modifica di AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;

public class ActModificaAnnotazioneEsitoTrasmissione extends ActionSiap implements ICostantiAnnotazioneEsitoTrasmissione
{
  Logger logger = Logger.getLogger("actionLogger");
 /*****************************************************************************
  * Azione di Modifica del AnnotazioneEsitoTrasmissione
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {

    //===================================================== 
    // Riempie il model con i campi recuperati dalla form 
    // n.b. per i campi del model non valorizzati, i corrispondenti  
    //      campi della tabella verranno impostati a null  
    // Il campo chiave è obbligatorio perchè utilizzato nelle clausola where 
    // per individuare il record da aggiornare 
    //===================================================== 
    AnnotazioneEsitoTrasmissioneModel lAnnMod = new AnnotazioneEsitoTrasmissioneModel ();

    lAnnMod.setIdEsitoTrasmissione       ( getRequestBigDecimalParameter ( CAMPO_ID_ESITO_TRASMISSIONE) );
    lAnnMod.setDataTrasmissione          ( getRequestDateParameter       ( CAMPO_ANNO_DATA_TRASMISSIONE,CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE) );
    lAnnMod.setOggettoTrasmissione       ( getRequestStringParameter     ( CAMPO_OGGETTO_TRASMISSIONE) );
    lAnnMod.setCodUfficioDestinatario    ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_DESTINATARIO) );
    lAnnMod.setCodUfficioInoltrante      ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INOLTRANTE) );
    lAnnMod.setCodUfficioEsito           ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_ESITO) );
    lAnnMod.setDataEsito                 ( getRequestDateParameter       ( CAMPO_ANNO_DATA_ESITO,CAMPO_MESE_DATA_ESITO, CAMPO_GIORNO_DATA_ESITO) );
    lAnnMod.setCodEsito                  ( getRequestStringParameter     ( CAMPO_COD_ESITO) );
    lAnnMod.setNoteEsito                 ( getRequestStringParameter     ( CAMPO_NOTE_ESITO) );
    lAnnMod.setChiaveAnno                ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO) );
    lAnnMod.setChiaveProgr               ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR) );
    lAnnMod.setChiaveUfficio             ( getRequestStringParameter     ( CAMPO_CHIAVE_UFFICIO) );
    lAnnMod.setEveIdEvento               ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO) );
    lAnnMod.setFasSieIdFascicoloSiep     ( getRequestBigDecimalParameter ( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
    lAnnMod.setMesIdMessaggioRichiesta   ( getRequestBigDecimalParameter ( CAMPO_MES_ID_MESSAGGIO_RICHIESTA) );
    lAnnMod.setMesIdMessaggioEsito       ( getRequestBigDecimalParameter ( CAMPO_MES_ID_MESSAGGIO_ESITO) );
    lAnnMod.setCodOperatoreInserimento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lAnnMod.setDataInserimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO) );
    lAnnMod.setCodUfficioInserimento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lAnnMod.setCodOperatoreAggiornamento ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lAnnMod.setDataAggiornamento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lAnnMod.setCodUfficioAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua la modifica 
    //=================================================== 
    IAnnotazioneEsitoTrasmissione lCtrl = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
    lCtrl.ExModificaAnnotazioneEsitoTrasmissione(lAnnMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.annotazioneesitotrasmissione.action.ActLoadDettaglioAnnotazioneEsitoTrasmissione";
    lPage += "&" + CAMPO_ID_ESITO_TRASMISSIONE + "=" + lAnnMod.getIdEsitoTrasmissione().toString();

    return lPage;
  }
}