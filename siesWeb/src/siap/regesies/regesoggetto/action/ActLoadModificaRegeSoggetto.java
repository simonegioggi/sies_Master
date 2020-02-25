package siap.regesies.regesoggetto.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesoggetto.controller.IRegeSoggetto;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciRegeSoggetto</p>
 * <p>Description: Classe Action per la load inserisci di RegeSoggetto</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 */

public class ActLoadModificaRegeSoggetto extends ActionRegeSiap
implements ICostantiRegeSoggetto
{
  public String processRequest() throws F3BException
  {

    String lId = getRequestStringParameter(CAMPO_ID_FILE);

    IRegeSoggetto lCtrl = RegeSiesLookupRemote.getRegeSoggettoRemote();
    RegeSoggettoModel lSoggetto = lCtrl.ExRicercaRegeSoggettoByKey(lId);

    setRequestAttribute("regesoggetto", lSoggetto);

    // Imposta la combo nazioni
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), lSoggetto.getCodStatoNascita());
    setRequestAttribute("nazioni", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getSesso(), lSoggetto.getSesso());
    setRequestAttribute("sesso", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getNazionalita(), lSoggetto.getNazionalita());
    setRequestAttribute("nazionalita", "" + lOption );



    return PG_MODIFICAREGESOGGETTO; //restituisce la jsp di VIEW
  }
}