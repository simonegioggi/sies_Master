package siap.sige.fascicolo.action;

import siap.sico.ufficio.controller.UfficioUtils;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
* <p>Title: ActLoadDettaglioProcedimentoCollegato</p>
* <p>Description: Classe Action per la load dettaglio di ProcedimentoCollegato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/

public class ActLoadDettaglioProcedimentoCollegato extends ActionSige implements ICostantiFascicoloSige
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

      // Fascicolo Collegato (Padre)
      IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

      FascicoloSigeModel lFasSigePadre = null;
      if(lFasSigeEsteso.getFascicoloSige().getIdFascicoloSigeOrigine()!=null )
    	  lFasSigePadre = lCtrl.ExRicercaFascicoloSigeByKey(lFasSigeEsteso.getFascicoloSige().getIdFascicoloSigeOrigine());

      if(lFasSigePadre != null && lFasSigePadre.getDescrUfficio() != null && lFasSigePadre.getDescrUfficio().equals("")){
    	  lFasSigePadre.setDescrUfficio( (UfficioUtils.getUfficioByCodUfficio(lFasSigePadre.getChiaveUfficio())).getDescrComune() );
      }

      setRequestAttribute("fascicoloSigeEsteso", lFasSigeEsteso);
      setRequestAttribute("fascicoloPadre", lFasSigePadre);
      return PG_LOAD_DETTAGLIOPROCEDIMENTOCOLLEGATO;
    }
}
