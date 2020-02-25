package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSospProvv51BisAffProva</p>
 * <p>Description: Classe Action per la load inserisci di Sospensione Provvisoria 51 bis Affidamento In prova</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciSospProvv51BisAffProva extends ActSospensioneProvvisoria
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
    String lRitorno = this.getSospensioni();
    if(!lRitorno.equals(""))
      return lRitorno;

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoProsecProvvMAAffPro());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoSospensione", "AFFIDAMENTO51BIS");

    return PG_LOAD_INSERISCI_MA_DECRETO_SOSP;
  }
}