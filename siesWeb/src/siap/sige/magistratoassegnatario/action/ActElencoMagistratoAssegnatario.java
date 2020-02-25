package siap.sige.magistratoassegnatario.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.controller.MagistratoAssegnatarioController;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActElencoMagistratoAssegnatario
 * </p>
 * <p>
 * Description: Classe Action che permette di visualizzare l'Elenco dei Magistrati assegnati al Procedimento
 * SIGE, sia quello attivo sia le assegnazioni precedenti.
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActElencoMagistratoAssegnatario extends ActionSige implements ICostantiMagistratoAssegnatario {

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

		setRequestAttribute("magistrati", lVect);

		return PG_ELENCOMAGISTRATOASSEGNATARIO;
	}

}