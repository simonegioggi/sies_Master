package siap.sige.fascicolo.action;

import java.util.Vector;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti (rispondenti ai parametri selezionati) del soggetto individuato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company :
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaProcSigeDelSoggetto extends ActionSiap implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		SoggettoModel lSogMod = new SoggettoModel();

		// Recupero dei parametri di ricerca con predisposizione alla chiamata diretta: preimpostazione dei
		// parametri di filtro.
		String ufUtConnesso = "";
		String codDistretto = getUfficioUtenteConnesso().getCodDistretto();
		String tipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		if (!isRequestParameterNullObj("hufUtConnesso"))
			ufUtConnesso = getRequestStringParameter("hufUtConnesso");
		if (!isRequestParameterNullObj("hcodDistretto"))
			codDistretto = getRequestStringParameter("hcodDistretto");
		if (!isRequestParameterNullObj("htipoUfficio"))
			tipoUfficio = getRequestStringParameter("htipoUfficio");

		// riempie il model
		lSogMod.setIdSoggetto(getRequestBigDecimalParameter("IdSoggetto"));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// Si chiama il FascicoloSigeController.
		IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		Vector lFascicoliSoggetti = lFasSigeCtrl.ExRicercaFascSigeDelSoggetto(lSogMod, ufUtConnesso,
				codDistretto);

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {

			// Chiama il controller.
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			SoggettoModel lSoggetto = lSogCtrl
					.ExRicercaSoggettoByKey(getRequestBigDecimalParameter("IdSoggetto"));

			// SoggettoModel lSoggetto = ((FascicoloSigeEstesoModel)lFascicoliSoggetti.get(0)).getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			// setSessionAttribute("soggetto", lSoggetto ); // Da rivedere
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", ufUtConnesso);
		setRequestAttribute("codDistretto", codDistretto);
		setRequestAttribute("tipoUfficio", tipoUfficio);

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSige.PG_RICERCA_FASCICOLIDELSOGGETTO;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}

}