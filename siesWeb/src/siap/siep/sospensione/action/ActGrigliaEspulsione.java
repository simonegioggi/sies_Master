package siap.siep.sospensione.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;


/**
* <p>Title: ActGrigliaEspulsione</p>
* <p>Description: Classe Action per la griglia dell'espulsione</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActGrigliaEspulsione extends ActionSiap implements ICostantiSospensione
{
  public String processRequest() throws F3BException
  {

    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    setRequestAttribute("strFunzione", "ESPULSIONE DAL TERRITORIO DELLO STATO ART.16 COMMA 5 D.LO 286/1998 E SUCC.MOD.");
    return ISIAPCostantiWeb.PG_GRIGLIA_BOTTONI_SIEP;

  }
}





