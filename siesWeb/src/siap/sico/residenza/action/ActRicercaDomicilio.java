package siap.sico.residenza.action;

/**
* <p>Title: ActRicercaDomicilio</p>
* <p>Description: Classe Action per la ricerca del Domicilio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Vector;

import siap.sico.residenza.controller.ResidenzaController;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActRicercaDomicilio extends ActionSiap implements ICostantiResidenza {

	public String processRequest() throws F3BException {

		ResidenzaModel lResMod = new ResidenzaModel();

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))
			lResMod.setSogIdSoggetto(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));

		ResidenzaController lCtrl = new ResidenzaController();

		// 'Residenza' (R), 'Domicilio' (D)
		lResMod.setCodTipoResidenza("D");

		Vector lVect = null;

		String formname = "";
		if (!isRequestParameterNullObj("formname")) {
			formname = getRequestStringParameter("formname");
		}

		// MEV 15 Revisione Sige Parte 2
		// In fase di iscrizione, nel caso in cui, il procedimento è stato
		// iscritto partendo da un procedimento SIEP, nella lista dei domicili
		// devono apparire anche i dati inerenti il soggetto SIEP.
		if (formname != "" && formname.equals("LoadInserisciDomicilioFascicoloSige")) {
			// Recupero il domicilio del Soggetto Siep
			FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
					"FascicoloSigeEsteso");
			if (lFasSigeEsteso.getFascicoloSiep() != null
					&& lFasSigeEsteso.getFascicoloSiep().getSogIdSoggetto() != null) {
				lVect = lCtrl.ExRicercaResidenzaSige(lResMod,
						lFasSigeEsteso.getFascicoloSiep().getSogIdSoggetto());
			} else {
				// caso in cui non è presente il Fascicolo Siep
				// 20171117 [EC]: anomalia: mancata assegnazione del risultato al vettore
				lVect = lCtrl.ExRicercaResidenza(lResMod);
			}
		} else {
			// 20171117 [EC]: anomalia: mancata assegnazione del risultato al vettore
			lVect = lCtrl.ExRicercaResidenza(lResMod);
		}

		setRequestAttribute("residenze", lVect);

		return PG_RICERCADOMICILIO;
	}

}