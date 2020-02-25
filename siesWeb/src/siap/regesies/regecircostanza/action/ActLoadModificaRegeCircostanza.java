package siap.regesies.regecircostanza.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regecircostanza.controller.IRegeCircostanza;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciRegeCircostanza</p>
 * <p>Description: Classe Action per la load inserisci di RegeCircostanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadModificaRegeCircostanza extends ActionRegeSiap implements ICostantiRegeCircostanza
{
  public String processRequest() throws F3BException
  {

    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    int lProgrCirc = getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA);

    IRegeCircostanza lCtrl = RegeSiesLookupRemote.getRegeCircostanzaRemote();
    RegeCircostanzaModel lCircMod = lCtrl.ExRicercaRegeCircostanzaByKey(lId, lProgrCirc);

    setRequestAttribute("regeCircostanza", lCircMod);

    Option lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lCircMod.getCodFonte());
    setRequestAttribute("TipiFontiCircostanza", "" + lOption);

    lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(), lCircMod.getCodSottonumerazione());
    setRequestAttribute("TipiSottonumerazione", "" + lOption);


    return PG_MODIFICAREGECIRCOSTANZA; //restituisce la jsp di VIEW
  }
}