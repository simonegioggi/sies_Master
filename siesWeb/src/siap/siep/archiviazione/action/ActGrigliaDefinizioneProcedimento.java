package siap.siep.archiviazione.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActGrigliaDefinizioneProcedimento
    extends ActionSiap
    implements ICostantiArchiviazione
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    setRequestAttribute("strFunzione", "Definizione Procedimento");

    return PG_GRIGLIA_DEFINIZIONE_PROCEDIMENTO;

  }
}