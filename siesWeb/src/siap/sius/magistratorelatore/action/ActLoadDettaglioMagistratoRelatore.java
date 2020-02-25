package siap.sius.magistratorelatore.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioMagistratoRelatore
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di MagistratoRelatore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioMagistratoRelatore extends ActionSiap implements ICostantiMagistratoRelatore {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.setLinkRitorno();
		// Passa la action di destinazione
		if (!isRequestParameterNullObj("acdest")) {
			this.setRequestAttribute("acdest", this.getRequestStringParameter("acdest"));
		}

		// Generale.
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Magistrato relatore
		// IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		// lMagRel = lMagRelCtrl.ExRicercaMagRelByFascicolo(lIdFasSius);

		IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		Vector lMagistrati = lMagRelCtrl.ExRicercaMagRelCorrentePrecedenteByFascicolo(lIdFasSius);

		if (lMagistrati == null || lMagistrati.size() == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Non esistono magistrati relatori legati al fascicolo corrente.");

		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
		MagistratoModel lMagistratoNew = null;
		MagistratoModel lMagistratoOld = null;
		EspertoModel lEspertoNew = null;
		EspertoModel lEspertoOld = null;

		Iterator lItx = lMagistrati.iterator();
		while (lItx.hasNext()) {
			MagistratoRelatoreModel lMagRel = (MagistratoRelatoreModel) lItx.next();

			if (lMagRel.getDataFine() != null) {
				if (lMagRel.getMagCodMagistrato() != null) // Magistrato
				{
					lMagistratoOld = lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato());
				}

				if (lMagRel.getEspIdEsperto() != null) // Esperto
				{
					lEspertoOld = lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto());
				}
			} else {
				if (lMagRel.getMagCodMagistrato() != null) // Magistrato
				{
					lMagistratoNew = lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato());
				}

				if (lMagRel.getEspIdEsperto() != null) // Esperto
				{
					lEspertoNew = lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto());
				}
			}
		}

		setRequestAttribute("magistratoNew", lMagistratoNew);
		setRequestAttribute("magistratoOld", lMagistratoOld);
		setRequestAttribute("espertoNew", lEspertoNew);
		setRequestAttribute("espertoOld", lEspertoOld);

		return PG_LOAD_DETTAGLIOMAGISTRATORELATORE;
	}

}