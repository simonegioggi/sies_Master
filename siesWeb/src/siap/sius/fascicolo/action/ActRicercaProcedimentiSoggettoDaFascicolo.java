package siap.sius.fascicolo.action;

import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti del Soggetto a Partire dal Dettaglio Fascicolo (in Sessione)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ActRicercaProcedimentiSoggettoDaFascicolo extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Soggetto Model preso dalla sessione.
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Lettura soggetto.
		SoggettoModel lSogMod = new SoggettoModel();
		lSogMod.setIdSoggetto(lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto());

		// Parametri di ricerca.
		String ufUtConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String ufOTribunale = "";
		String codDistretto = "3";
		String lIncludeArchiviati = "S";
		String codContenuto = "-";
		String descrContenuto = "-";
		// String tipoUfficio = "";
		String dataDalInCancelleria = "";
		String dataAlInCancelleria = "";

		// Si chiama il FascicoloSiusController.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliDelSoggetto(lSogMod, ufUtConnesso,
				ufOTribunale, codDistretto, lIncludeArchiviati, codContenuto,
				DateUtils.getDate(dataAlInCancelleria, "ddMMyyyy"),
				DateUtils.getDate(dataAlInCancelleria, "ddMMyyyy"));

		String lReturnPage = "";

		// Estrazione Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloGPModel) lFascicoliSoggetti.get(0)).getFascicoloSiusModel()
					.getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto);
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("IdSoggetto", lSogMod.getIdSoggetto());
		setRequestAttribute("ufUtConnesso", "");
		setRequestAttribute("ufOTribunale", ufOTribunale);
		setRequestAttribute("codDistretto", codDistretto);
		setRequestAttribute("lIncludeArchiviati", "S");
		setRequestAttribute("codContenuto", codContenuto);
		setRequestAttribute("descrContenuto", descrContenuto);
		setRequestAttribute("dataDalInCancelleria", dataDalInCancelleria);
		setRequestAttribute("dataAlInCancelleria", dataAlInCancelleria);
		setRequestAttribute("tipoUfficio", "");

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_PROCEDIMENTIDELSOGGETTO;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}