package siap.regesies.regescarti.action;

import siap.regesies.regescarti.controller.RegeFileController;
import siap.regesies.regescarti.model.RegeFileModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActDettaglioRegeFile</p>
* <p>Description: Classe Action per il dettaglio del File scartato REGE</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioRegeFile extends ActionSiap implements ICostantiRegeFile
{
public String processRequest() throws F3BException {

    // prende dalla request il campo ID_FILE per individuare il record
    String lId = getRequestStringParameter(CAMPO_ID_FILE);

    // chiama il controller
    //IRegeFile lCtrl = SIEPLookupRemote.getRegeFileRemote();
    RegeFileController lCtrl = new RegeFileController();

    // Instanzia e riempie il model
    RegeFileModel llRegMod = lCtrl.ExRicercaRegeFileByKey(new String(lId));
    setRequestAttribute("regefile", llRegMod);

    // Apre la pagina di dettaglio del File Scartato
    return PG_LOAD_DETTAGLIOREGEFILE;
  }

}