package siap.siepe.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaAttiVistiPerSoggetto</p>
 * <p>Description: Azione di caricamento della form di ricerca degli atti ricevuti e Presi in visione per Soggetto</p>
 * <p>Copyright: Bull Italia Copyright (c) 2006</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaAttiVistiPerSoggetto extends ActionSiap implements ICostantiFascicoloSiepe
{
  public String processRequest() throws Exception
  {
    this.setLinkRitorno();

    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
    setRequestAttribute("nazioni", "" + lOption );

    return PG_LOAD_RICERCAATTIVISTIPERSOGGETTO; //restituisce la jsp di VIEW
  }
}
