package siap.siep.jms.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaEstesaFascicoloSiep</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ActLoadRicercaEstesaFascicolo extends ActionSiap implements ICostantiSiepJMS
{
  public String processRequest() throws Exception
  {
     Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
     setRequestAttribute("autoritaEsterna", "" + lOption );

    return PG_LOAD_RICERCA_ESTESA_FASCICOLO_SIEP;  //restituisce la jsp di VIEW
  }
}
