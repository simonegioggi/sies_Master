package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciMAProsecProvvAffProvaCumulo</p>
 * <p>Description: Classe Action per la load inserisci di Prosecuzione provvisoria Affidamento in Prova con cumulo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciMAProsecProvvAffProvaCumulo extends ActProsecuzioneProvvisoria
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
    String lRitorno = getProsecuzioneProvvisoria();
    if(!lRitorno.equals(""))
      return lRitorno;

    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAAffPro());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoMisura", "AFFIDAMENTOCUMULO");

    return PG_LOAD_INSERISCI_MA_PROSECUZIONE;
  }
}