package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciEstensioneDefDetDomCumulo</p>
 * <p>Description: Classe Action per la load inserisci di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciEstensioneDefDetDomCumulo extends ActEstensioneDefinitiva
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
    String lRitorno = getEstensioneDefinitiva();
    if(!lRitorno.equals(""))
      return lRitorno;

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoEstDefMADetDom());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoMisura", "DETENZIONECUMULO");

    return PG_LOAD_INSERISCI_MA_EST_DEF;
  }
}