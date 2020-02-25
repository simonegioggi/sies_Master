package siap.siep.istruttoriacumulo.action;

import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaIstruttoriaCumulo</p>
* <p>Description: Classe Action per la modifica di IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia S.p.A</p>
* @deprecated Action generata del framework, Non Utilizzata al momento, codice
* non verificato
*/
public class ActModificaIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo
{
 /*****************************************************************************
  * Azione di Modifica del IstruttoriaCumulo
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
    IstruttoriaCumuloModel lIstMod = new IstruttoriaCumuloModel ();

    lIstMod.setIdIstruttoriaCumulo       ( getRequestBigDecimalParameter ( CAMPO_ID_ISTRUTTORIA_CUMULO) );
    lIstMod.setFasSieIdFascicoloSiep     ( getRequestBigDecimalParameter ( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
    lIstMod.setEveIdEventoIstr           ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO_ISTR) );
    lIstMod.setEveIdEventoProv           ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO_PROV) );
    lIstMod.setDataApertura              ( getRequestDateParameter       ( CAMPO_ANNO_DATA_APERTURA,CAMPO_MESE_DATA_APERTURA, CAMPO_GIORNO_DATA_APERTURA) );
    lIstMod.setDataChiusura              ( getRequestDateParameter       ( CAMPO_ANNO_DATA_CHIUSURA,CAMPO_MESE_DATA_CHIUSURA, CAMPO_GIORNO_DATA_CHIUSURA) );
    lIstMod.setNote                      ( getRequestStringParameter     ( CAMPO_NOTE) );
    lIstMod.setFlagStato                 ( getRequestStringParameter     ( CAMPO_FLAG_STATO) );
    lIstMod.setCodOperatoreInserimento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lIstMod.setDataInserimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO) );
    lIstMod.setCodUfficioInserimento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lIstMod.setCodOperatoreAggiornamento ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lIstMod.setDataAggiornamento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lIstMod.setCodUfficioAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua la modifica 
    //=================================================== 
    IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
    lCtrl.ExModificaIstruttoriaCumulo(lIstMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.istruttoriacumulo.action.ActLoadDettaglioIstruttoriaCumulo";
    lPage += "&" + CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + lIstMod.getIdIstruttoriaCumulo().toString();

    return lPage;
  }
}