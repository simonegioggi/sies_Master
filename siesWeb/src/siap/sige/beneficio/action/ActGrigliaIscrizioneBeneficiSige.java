package siap.sige.beneficio.action;


import siap.sico.web.ActionSiap;
import siap.siep.beneficio.action.ICostantiBeneficio;
import f3b.util.F3BException;


public class ActGrigliaIscrizioneBeneficiSige extends ActionSiap  
{
  public String processRequest() throws F3BException
  {

    setRequestAttribute("strFunzione", "Iscrizione concessione benefici");
	setRequestAttribute("modo", "SIGE");

    
    return ICostantiBeneficio.PG_LOAD_GRIGLIA_ISCRIZIONE_BENEFICIO;
  }
}
