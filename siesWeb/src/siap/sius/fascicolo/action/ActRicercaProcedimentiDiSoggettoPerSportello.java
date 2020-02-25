package siap.sius.fascicolo.action;

import java.util.Vector;

import f3b.util.DateUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Attività di Sportello - Ricerca dei Procedimenti (rispondenti ai parametri selezionati) del
 * soggetto individuato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ActRicercaProcedimentiDiSoggettoPerSportello extends ActionSiap
		implements ICostantiFascicoloSius {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		SoggettoModel lSogMod = new SoggettoModel();

		// Recupero dei parametri di ricerca.
		// Predisposizione alla chiamata diretta: preimpostazione dei parametri di filtro.
		String ufUtConnesso = "";
		String ufOTribunale = "";
		String codDistretto = getUfficioUtenteConnesso().getCodDistretto();
		String lIncludeArchiviati = "S";
		String codContenuto = "-";
		String descrContenuto = "-";
		String tipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String dataDalInCancelleria = "";
		String dataAlInCancelleria = "";

		if (!isRequestParameterNullObj("hufUtConnesso"))
			ufUtConnesso = getRequestStringParameter("hufUtConnesso");
		if (!isRequestParameterNullObj("hufOTribunale"))
			ufOTribunale = getRequestStringParameter("hufOTribunale");
		if (!isRequestParameterNullObj("hcodDistretto"))
			codDistretto = getRequestStringParameter("hcodDistretto");
		if (!isRequestParameterNullObj("hlIncludeArchiviati"))
			lIncludeArchiviati = getRequestStringParameter("hlIncludeArchiviati");
		if (!isRequestParameterNullObj("hcodContenuto"))
			codContenuto = getRequestStringParameter("hcodContenuto");
		if (!isRequestParameterNullObj("hdescrContenuto"))
			descrContenuto = getRequestStringParameter("hdescrContenuto");
		if (!isRequestParameterNullObj("htipoUfficio"))
			tipoUfficio = getRequestStringParameter("htipoUfficio");
		if (!isRequestParameterNullObj("hdataDalInCancelleria"))
			dataDalInCancelleria = getRequestStringParameter("hdataDalInCancelleria");
		if (!isRequestParameterNullObj("hdataAlInCancelleria"))
			dataAlInCancelleria = getRequestStringParameter("hdataAlInCancelleria");

		// riempie il model
		lSogMod.setIdSoggetto(getRequestBigDecimalParameter("IdSoggetto"));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// Si chiama il FascicoloSiusController.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliDelSoggetto(lSogMod, ufUtConnesso,
				ufOTribunale, codDistretto, lIncludeArchiviati, codContenuto,
				DateUtils.getDate(dataDalInCancelleria, "dd/MM/yyyy"),
				DateUtils.getDate(dataAlInCancelleria, "dd/MM/yyyy"));

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloGPModel) lFascicoliSoggetti.get(0)).getFascicoloSiusModel()
					.getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto); // Da rivedere
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", ufUtConnesso);
		setRequestAttribute("ufOTribunale", ufOTribunale);
		setRequestAttribute("codDistretto", codDistretto);
		setRequestAttribute("lIncludeArchiviati", lIncludeArchiviati);
		setRequestAttribute("codContenuto", codContenuto);
		setRequestAttribute("descrContenuto", descrContenuto);
		setRequestAttribute("dataDalInCancelleria", dataDalInCancelleria);
		setRequestAttribute("dataAlInCancelleria", dataAlInCancelleria);
		setRequestAttribute("tipoUfficio", tipoUfficio);

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_PROC_SOGGETTO_SPORTELLO;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}
}
