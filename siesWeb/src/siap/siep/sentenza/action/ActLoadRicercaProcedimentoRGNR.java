package siap.siep.sentenza.action;


import siap.sico.decodifiche.controller.DecodificheManager;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaProcedimentoRGNR extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIEPTrattino());
    setRequestAttribute("autoritaEsterna", "" + lOption );

    return PG_LOAD_RICERCAPROCEDIMENTORGNR; //restituisce la jsp di VIEW
  }
}
