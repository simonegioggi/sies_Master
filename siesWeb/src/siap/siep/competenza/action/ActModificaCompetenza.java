package siap.siep.competenza.action;


/**
* <p>Title: ActModificaCompetenza</p>
* <p>Description: Classe Action per la modifica di Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.sico.web.ISICOCostantiWeb;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
//import f3b.web.RedirectTo;

public class ActModificaCompetenza extends ActionSiap implements ICostantiCompetenza
{
 /*****************************************************************************
  * Azione di Modifica del Competenza
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
    CompetenzaModel lComMod = new CompetenzaModel ();

    lComMod.setIdCompetenza                ( getRequestBigDecimalParameter ( CAMPO_ID_COMPETENZA) );
    lComMod.setCodTipoProvvedimento        ( getRequestStringParameter     ( CAMPO_COD_TIPO_PROVVEDIMENTO) );
    lComMod.setDataProvvedimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_PROVVEDIMENTO,CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO) );
    lComMod.setCodTipoAutoritaEmittente    ( getRequestStringParameter     ( CAMPO_COD_TIPO_AUTORITA_EMITTENTE) );
    lComMod.setCodLuogoEmittente           ( getRequestStringParameter     ( CAMPO_COD_LUOGO_EMITTENTE) );
    lComMod.setNumSezioneAutoritaEmittente ( getRequestStringParameter     ( CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE) );
    lComMod.setAnnoSentenza                ( getRequestBigDecimalParameter ( CAMPO_ANNO_SENTENZA) );
    lComMod.setNumeroSentenza              ( getRequestStringParameter     ( CAMPO_NUMERO_SENTENZA) );
    lComMod.setDataIrrevocabilita          ( getRequestDateParameter       ( CAMPO_ANNO_DATA_IRREVOCABILITA,CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA) );
    lComMod.setSenIdSentenza               ( getRequestBigDecimalParameter ( CAMPO_SEN_ID_SENTENZA) );
    lComMod.setFasSieIdFascicoloSiep       ( getRequestBigDecimalParameter ( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
    lComMod.setEveIdEvento                 ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO) );
    lComMod.setChiaveAnno                  ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO) );
    lComMod.setChiaveUfficio               ( getRequestStringParameter     ( CAMPO_CHIAVE_UFFICIO) );
    lComMod.setChiaveProgr                 ( getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR) );
    lComMod.setCodOperatoreInserimento     ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lComMod.setDataInserimento             ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO) );
    lComMod.setCodUfficioInserimento       ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lComMod.setCodOperatoreAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lComMod.setDataAggiornamento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lComMod.setCodUfficioAggiornamento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua la modifica 
    //=================================================== 
    ICompetenza lCtrl = SIEPLookupRemote.getCompetenzaRemote();
    lCtrl.ExModificaCompetenza(lComMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = ISICOCostantiWeb.PG_MAIN + "?" + ISICOCostantiWeb.ACTION_FIELD + "=siap.siep.competenza.action.ActLoadDettaglioCompetenza";
    lPage += "&" + CAMPO_ID_COMPETENZA + "=" + lComMod.getIdCompetenza().toString();

    return lPage;
  }
}