package siap.sige.magistratoassegnatario.action;

/**
* <p>Title: ActRicercaMagistratoAssegnatario</p>
* <p>Description: Classe Action per la ricerca di Magistrato Assegnatario Fascicolo SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.controller.MagistratoAssegnatarioController;

public class ActRicercaMagistratoAssegnatario extends ActionSiap implements ICostantiMagistratoAssegnatario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		BigDecimal lIdFascicoloSige = lFasEsteso.getFascicoloSige().getIdFascicoloSige();

		MagistratoAssegnatarioController lCtrl = new MagistratoAssegnatarioController();
		Vector lVect = lCtrl.ExRicercaMagistratoByProcedimentoSige(lIdFascicoloSige,
				this.getCodUfficioUtenteConnesso());

		// String lModificabile = "NO";

		// if (this.IsFascicoloSiusModificabile()==true) lModificabile="SI"; else lModificabile="NO";
		// this.setRequestAttribute("isModificabile", lModificabile);

		setRequestAttribute("magistrati", lVect);

		return PG_RICERCAMAGISTRATOASSEGNATARIO;
	}

}