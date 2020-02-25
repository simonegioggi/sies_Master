package siap.sico.soggettocertificato.action;


/**
* <p>Title: ActModificaSoggettoCertificato</p>
* <p>Description: Classe Action per la modifica di SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.soggettocertificato.controller.ISoggettoCertificato;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;

public class ActModificaSoggettoCertificato extends ActionSiap implements ICostantiSoggettoCertificato
{
 /*****************************************************************************
  * Azione di Modifica del SoggettoCertificato
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
    SoggettoCertificatoModel lSogMod = new SoggettoCertificatoModel ();

    lSogMod.setIdSoggettoCertificato     ( getRequestBigDecimalParameter ( CAMPO_ID_SOGGETTO_CERTIFICATO) );
    lSogMod.setSogIdSoggetto             ( getRequestBigDecimalParameter ( CAMPO_SOG_ID_SOGGETTO) );
    lSogMod.setDataInserimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO) );
    lSogMod.setCodOperatoreInserimento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lSogMod.setCodUfficioInserimento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lSogMod.setDataAggiornamento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lSogMod.setCodOperatoreAggiornamento ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lSogMod.setCodUfficioAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua la modifica 
    //=================================================== 
    ISoggettoCertificato lCtrl = SICOLookupRemote.getSoggettoCertificatoRemote();
    lCtrl.ExModificaSoggettoCertificato(lSogMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.soggettocertificato.action.ActLoadDettaglioSoggettoCertificato";
    lPage += "&" + CAMPO_ID_SOGGETTO_CERTIFICATO + "=" + lSogMod.getIdSoggettoCertificato().toString();

    return lPage;
  }
}