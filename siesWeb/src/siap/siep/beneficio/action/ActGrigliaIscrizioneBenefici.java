package siap.siep.beneficio.action;


import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;


public class ActGrigliaIscrizioneBenefici extends ActionSiap implements ICostantiBeneficio
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    setRequestAttribute("strFunzione", "Iscrizione concessione benefici");

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }    
    
    return PG_LOAD_GRIGLIA_ISCRIZIONE_BENEFICIO;
  }
}
