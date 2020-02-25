package siap.sige.magistratoassegnatario.action;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioMagistratoAssegnatario</p>
* <p>Description: Classe Action per la load dettaglio di Magistrato Assegnatario</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadDettaglioMagistratoAssegnatario extends ActionSiap implements ICostantiMagistratoAssegnatario
{
public String processRequest() throws F3BException {


     IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
     MagistratoModel lMagis = new MagistratoModel();

     if(!this.isRequestParameterNullObj("magistratoprecedente") &&
        (!getRequestAttribute("magistratoprecedente").toString().equals("")))
      {
        String lMagistratoPre = getRequestAttribute("magistratoprecedente").toString();
        lMagis = lCtrl.ExRicercaMagistratoByCod(lMagistratoPre);
      }


       MagistratoAssegnatarioModel lMagAss = (MagistratoAssegnatarioModel)this.getRequestAttribute("magistratoassegnatario");
       MagistratoModel lMagistrato = lCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato());

       setRequestAttribute("magistratoassegnatario", lMagistrato);
       setRequestAttribute("magistratoprecedente", lMagis);
       setRequestAttribute("magistrato", lMagAss);



		 return PG_LOAD_DETTAGLIOMAGISTRATOASSEGNATARIO;
	 }

}