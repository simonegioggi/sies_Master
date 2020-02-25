package siap.sico.soggettocertificato.action;


/**
* <p>Title: ActInserisciSoggettoCertificato</p>
* <p>Description: Classe Action per l'inserimento di SoggettoCertificato</p>
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

public class ActInserisciSoggettoCertificato extends ActionSiap implements ICostantiSoggettoCertificato {

 /*****************************************************************************
  * Azione di Inserimento del SoggettoCertificato
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException {
    SoggettoCertificatoModel lSogMod = new SoggettoCertificatoModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    // n.b. eliminare o commentare i campi non presenti in maschera 
    //      es: chiave della tabella, date_ins, foreignkey... 
    //========================================================================== 
    lSogMod.setIdSoggettoCertificato     ( getRequestBigDecimalParameter ( CAMPO_ID_SOGGETTO_CERTIFICATO) );
    lSogMod.setSogIdSoggetto             ( getRequestBigDecimalParameter ( CAMPO_SOG_ID_SOGGETTO) );
    lSogMod.setDataInserimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
    lSogMod.setCodOperatoreInserimento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lSogMod.setCodUfficioInserimento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lSogMod.setDataAggiornamento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO,CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lSogMod.setCodOperatoreAggiornamento ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lSogMod.setCodUfficioAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );

    //=================================================== 
    // Recupera il controller ed effettua l'inserimento 
    //=================================================== 
    ISoggettoCertificato lCtrl = SICOLookupRemote.getSoggettoCertificatoRemote();
    SoggettoCertificatoModel lSogRetMod = new SoggettoCertificatoModel();
    lSogRetMod=lCtrl.ExInserisciSoggettoCertificato(lSogMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.soggettocertificato.action.ActLoadDettaglioSoggettoCertificato";
    lPage += "&" + CAMPO_ID_SOGGETTO_CERTIFICATO + "=" + lSogRetMod.getIdSoggettoCertificato().toString();

    return lPage;
  }
}