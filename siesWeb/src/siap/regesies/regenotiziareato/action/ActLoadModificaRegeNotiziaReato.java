package siap.regesies.regenotiziareato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regenotiziareato.controller.IRegeNotiziaReato;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciRegeCircostanza</p>
 * <p>Description: Classe Action per la load inserisci di RegeCircostanza</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 */

public class ActLoadModificaRegeNotiziaReato extends ActionRegeSiap
implements ICostantiRegeNotiziaReato
{
  public String processRequest() throws F3BException
  {

    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    int lProgrNot = getRequestIntParameter(CAMPO_PROGR_NOTIZIA);

    IRegeNotiziaReato lCtrl = RegeSiesLookupRemote.getRegeNotiziaReatoRemote();
    RegeNotiziaReatoModel lNotMod = lCtrl.ExRicercaRegeNotiziaReatoByKey(lId, lProgrNot);

    setRequestAttribute("regenotiziareato", lNotMod);

    Option lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), lNotMod.getAcquisizioneDiretta());
    setRequestAttribute("Acquisizione", "" + lOption);

    return PG_MODIFICAREGENOTIZIAREATO; //restituisce la jsp di VIEW
  }
}