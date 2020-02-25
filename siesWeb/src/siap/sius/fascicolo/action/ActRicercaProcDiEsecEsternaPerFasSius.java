package siap.sius.fascicolo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.util.SIEPELookupRemote;
//import siap.siepe.SIEPEException;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti di Esecuzione Esterna Per Fascicolo SIUS
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
public class ActRicercaProcDiEsecEsternaPerFasSius extends ActionSiap implements ICostantiFascicoloSius {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Preleva dalla sessione il FascicoloGPModel.
		FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Si chiama il FascicoloSiusController.
		IFascicoloSiepe lFasCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		Vector lFascicoliSIEPE = lFasCtrl.ExRicercaFascicoliSiepePerIdFasSius(
				lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());

		// Se non vi sono elementi, lancia un errore di eccezione.
		if (lFascicoliSIEPE.size() == 0)
			throw new SIUSException(F3BException.USER_MESSAGE, "Non esistono procedimenti SIEPE.");

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSIEPE != null) {
			SoggettoModel lSoggetto = ((FascicoloSiepeEstesoModel) lFascicoliSIEPE.get(0)).getFascicoloSius()
					.getFascicoloSiusModel().getSoggetto();
			if (lSoggetto != null) {
				setRequestAttribute("soggetto", lSoggetto);
				setSessionAttribute("soggetto", lSoggetto); // Da rivedere
				setSessionAttribute("fascicoloSiusGP", lFascicoloGPModel); // Da rivedere
			}
		}

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSIEPE);

		// Bottone di ritorno
		setLinkRitorno();

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_PROCDIESECESTERNAPERFASSIUS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}