package siap.sico.trasmissione.action;


/**
* <p>Title: ActModificaTrasmissioni</p>
* <p>Description: Classe Action per la modifica di Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;

public class ActModificaTrasmissioni extends ActionSiap implements ICostantiTrasmissioni
{
  Logger logger = Logger.getLogger("actionLogger");
 /*****************************************************************************
  * Azione di Modifica del Trasmissioni
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
    TrasmissioniModel lTraMod = new TrasmissioniModel ();

    lTraMod.setIdTrasmissione   ( getRequestBigDecimalParameter ( CAMPO_ID_TRASMISSIONE) );
    lTraMod.setTipoTrasmissione ( getRequestStringParameter     ( CAMPO_TIPO_TRASMISSIONE) );
    lTraMod.setDataTrasmissione ( getRequestDateParameter       ( CAMPO_ANNO_DATA_TRASMISSIONE,CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE) );
    lTraMod.setEsitoTrasmissione ( getRequestStringParameter     ( CAMPO_ESITO_TRASMISSIONE) );
    lTraMod.setCodErrore        ( getRequestStringParameter     ( CAMPO_COD_ERRORE) );
    lTraMod.setTipoOperazione   ( getRequestStringParameter     ( CAMPO_TIPO_OPERAZIONE) );
    lTraMod.setDestinazione     ( getRequestStringParameter     ( CAMPO_DESTINAZIONE) );
    lTraMod.setChiaveSiesSogg   ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_SIES_SOGG) );
    lTraMod.setChiaveSiesFasc   ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_SIES_FASC) );
    lTraMod.setChiaveNscSogg    ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_NSC_SOGG) );
    lTraMod.setChiaveNscProv    ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_NSC_PROV) );
    lTraMod.setChiaveAnno       ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO) );
    lTraMod.setChiaveProgr      ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR) );
    lTraMod.setCodOperatoreInserimento( getRequestStringParameter (CAMPO_COD_OPERATORE_INSERIMENTO) );
    lTraMod.setDataInserimento  ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
    lTraMod.setCodUfficioInserimento( getRequestStringParameter (CAMPO_COD_UFFICIO_INSERIMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua la modifica 
    //=================================================== 
    ITrasmissioni lCtrl = SICOLookupRemote.getTrasmissioniRemote();
    lCtrl.ExModificaTrasmissioni(lTraMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.trasmissione.action.ActLoadDettaglioTrasmissioni";
    lPage += "&" + CAMPO_ID_TRASMISSIONE + "=" + lTraMod.getIdTrasmissione().toString();

    return lPage;
  }
}