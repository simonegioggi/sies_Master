package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciMAProsecProvvAffProva</p>
 * <p>Description: Classe Action per la load inserisci di Prosecuzione provvisoria Affidamento in Prova</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class ActLoadInserisciMAProsecProvvAffProva extends ActProsecuzioneProvvisoria
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
    String lRitorno = getProsecuzioneProvvisoria();
    if(!lRitorno.equals(""))
      return lRitorno;

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAAffPro());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoMisura", "AFFIDAMENTO");

    return PG_LOAD_INSERISCI_MA_PROSECUZIONE;
  }
}