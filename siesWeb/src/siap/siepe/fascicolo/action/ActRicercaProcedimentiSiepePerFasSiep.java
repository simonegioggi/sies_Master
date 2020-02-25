package siap.siepe.fascicolo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti di Esecuzione Esterna Per Fascicolo SIEPE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaProcedimentiSiepePerFasSiep extends ActionSiap implements ICostantiFascicoloSiepe {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Preleva dalla sessione il FascicoloSiep.
		FascicoloSiepModel lFascicoloSiepModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Si chiama il FascicoloSiusController.
		IFascicoloSiepe lFasCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		Vector lFascicoliSIEPE = lFasCtrl
				.ExRicercaFascicoliSiepePerIdFasSiep(lFascicoloSiepModel.getIdFascicoloSiep());

		if (lFascicoliSIEPE.size() == 0)
			throw new SIEPEException(F3BException.USER_MESSAGE, "Non esistono procedimenti SIEPE.");

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSIEPE != null) {
			SoggettoModel lSoggetto = ((FascicoloSiepeEstesoModel) lFascicoliSIEPE.get(0)).getFascicoloSiep()
					.getSoggetto();
			if (lSoggetto != null) {
				setRequestAttribute("soggetto", lSoggetto);
				setSessionAttribute("soggetto", lSoggetto); // Da rivedere

				FascicoloGPModel lFascicoloGPModel = new FascicoloGPModel();
				setSessionAttribute("fascicoloSiusGP", lFascicoloGPModel); // Da rivedere
			}
		}

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSIEPE);

		lReturnPage = ICostantiFascicoloSiepe.PG_RICERCA_PROCEDIMENTI_SIEPE_PER_FAS_SIEP;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}

}