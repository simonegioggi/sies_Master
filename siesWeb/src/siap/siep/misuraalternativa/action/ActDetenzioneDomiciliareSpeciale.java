package siap.siep.misuraalternativa.action;

import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
* <p>Title: ActDetenzioneDomiciliareSpeciale</p>
* <p>Description: Classe Action per la Detenzione domiciliare speciale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDetenzioneDomiciliareSpeciale extends ActionSiap
                                              implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
    //avviso di pagina in costruzione
      return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;

 /* scommentare appena si toglie l'avviso

    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    this.isEventoNonValidato();
    setRequestAttribute("strFunzione", "Detenzione Domiciliare Speciale");

    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;*/
  }
}