package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggetto</p>
 * <p>Description: Azione di caricamento della form di ricerca del Soggetto.</p>
 * <p>Copyright: Bull Italia Copyright (c) 2002</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaSoggettoFascicoloSius extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws F3BException
  {

    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
    setRequestAttribute("nazioni", "" + lOption );


    return PG_LOAD_RICERCASOGGETTOFASCICOLOSIUS; //restituisce la jsp di VIEW
  }
}
