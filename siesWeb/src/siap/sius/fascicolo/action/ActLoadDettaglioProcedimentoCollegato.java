package siap.sius.fascicolo.action;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
* <p>Title: ActLoadDettaglioProcedimentoCollegato</p>
* <p>Description: Classe Action per la load dettaglio di ProcedimentoCollegato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioProcedimentoCollegato extends ActionSius implements ICostantiFascicoloSius
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

      // Fascicolo Collegato (Padre)
      IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

      FascicoloGPModel lFasPadre = null;
      if(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine()!=null )
        lFasPadre = lCtrl.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine());

      setRequestAttribute("fascicoloPadre", lFasPadre);
      return PG_LOAD_DETTAGLIOPROCEDIMENTOCOLLEGATO;
    }
}
