package siap.sico.magistratocompetente.action;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioMagistratoCompetente</p>
* <p>Description: Classe Action per la load dettaglio di MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioMagistratoCompetente extends ActionSiap implements ICostantiMagistratoCompetente
{
public String processRequest() throws F3BException {


     IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
     MagistratoModel lMagis = new MagistratoModel();

    // if(!this.isRequestParameterNullObj("magistratoprecedente"))
      if(!getRequestAttribute("magistratoprecedente").toString().equals(""))
      {
        String lMagistratoPre = getRequestAttribute("magistratoprecedente").toString();
        lMagis = lCtrl.ExRicercaMagistratoByCod(lMagistratoPre);
      }


       MagistratoCompetenteModel lMagCom =  (MagistratoCompetenteModel)this.getRequestAttribute("magistratocompetente");
       MagistratoModel lMagistrato = lCtrl.ExRicercaMagistratoByCod(lMagCom.getMagCodMagistrato());

       setRequestAttribute("magistratocompetente", lMagistrato);
       setRequestAttribute("magistratoprecedente", lMagis);
       setRequestAttribute("magistrato", lMagCom);



		 return PG_LOAD_DETTAGLIO_MAGISTRATO_COMPETENTE;
	 }

}