package siap.sico.ufficio.action;

import siap.sico.web.ActionSiap;
import siap.siep.parametro.action.ICostantiParametro;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
/**
* <p>Title: ActDettaglioPeriodoFeriale</p>
* <p>Description: Classe Action per il dettaglio peridodo feriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioPeriodoFeriale extends ActionSiap 
                                        implements ICostantiUfficio, ICostantiParametro
{
  public String processRequest() throws F3BException
  {
    IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();

    ParametroModel lParMod = lCtrlPar.ExRicercaParametroUfficioConnesso(PERIODO_FERIALE, this.getCodUfficioUtenteConnesso());

    if(lParMod == null)
    {
      throw new F3BException(F3BException.USER_MESSAGE, "Per l'ufficio non esiste un periodo feriale personalizzato, utilizzare la funzione di inserimento.");
    }
    
    setRequestAttribute("Parametro", lParMod);
    
    return PG_LOAD_DETTALGIO_PERIODO_FERIALE;
  }
}
