package siap.regesies.regescarti.action;


/**
* <p>Title: ActDettaglioRegeFile</p>
* <p>Description: Classe Action per la cancellazione del file scartato REGE</p>
 <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
*/

import siap.regesies.regescarti.controller.RegeFileController;
import siap.regesies.regescarti.model.RegeFileModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;



public class ActCancellaRegeFile extends ActionSiap implements ICostantiRegeFile
{
public String processRequest() throws F3BException {


    // istanzia il Model
    RegeFileModel llRegMod = new RegeFileModel();

    // riempie il model
    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    llRegMod.setIdFile(lId);

    // chiama il controller
    //IRegeFile lCtrl = SIEPLookupRemote.getRegeFileRemote();
    RegeFileController lCtrl = new RegeFileController();

    lCtrl.ExCancellaRegeFile(llRegMod);

    String lPage = "";
    // Riapre l'elenco dei file scartati RIGE
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.regesies.regescarti.action.ActRicercaRegeFile";
    return lPage;

  }

}