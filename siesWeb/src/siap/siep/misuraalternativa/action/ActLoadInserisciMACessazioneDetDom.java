package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciMARevocaDetDom</p>
 * <p>Description: Classe Action per la load inserisci di Revoca Detenzione Domiciliare</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciMACessazioneDetDom extends ActRevoca
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
   String lRitorno = getRevoca();
   if(!lRitorno.equals(""))
      return lRitorno;

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMADetDom());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoRevoca", "DETENZIONE");

    return PG_LOAD_INSERISCI_MA_REVOCA;
  }
}