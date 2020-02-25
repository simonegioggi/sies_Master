package siap.siep.statis.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActGrigliaEstrazioneDatiStatisticheUfficio extends ActionSiap implements ICostantiStatis
{
  public String processRequest() throws F3BException
  {
  
/*	if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    setRequestAttribute("strFunzione", "Iscrizione concessione benefici");
*/
    return PG_LOAD_GRIGLIA_ESTRAZIONE_DATI_STATISTICHE_UFFICIO;
  }
}
