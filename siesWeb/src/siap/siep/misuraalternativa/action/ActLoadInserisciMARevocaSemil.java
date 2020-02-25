package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciMARevocaSemil</p>
 * <p>Description: Classe Action per la load inserisci di Revoca Semilibertà</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciMARevocaSemil extends ActRevoca
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
    String lRitorno = getRevoca();
    if(!lRitorno.equals(""))
       return lRitorno;

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMASemiL());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoRevoca", "SEMILIBERTA");

	// MEV 10 - filtro sui minorenni
	setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

    return PG_LOAD_INSERISCI_MA_REVOCA;
  }
}