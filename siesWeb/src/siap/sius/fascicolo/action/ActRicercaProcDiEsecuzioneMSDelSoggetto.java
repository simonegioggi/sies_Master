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
 * Description: Ricerca dei Procedimenti di Esecuzione M.S.(rispondenti ai parametri selezionati) del soggetto
 * individuato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EUNICS S.p.A.
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaProcDiEsecuzioneMSDelSoggetto extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		SoggettoModel lSogMod = new SoggettoModel();

		// Recupero dei parametri di ricerca.
		String ufUtConnesso = getRequestStringParameter("hufUtConnesso");
		String ufOTribunale = getRequestStringParameter("hufOTribunale");
		String codDistretto = getRequestStringParameter("hcodDistretto");
		String lIncludeArchiviati = getRequestStringParameter("hlIncludeArchiviati");
		String codContenuto = getRequestStringParameter("hcodContenuto");
		String descrContenuto = getRequestStringParameter("hdescrContenuto");
		String tipoUfficio = getRequestStringParameter("htipoUfficio");
		String dataDalInCancelleria = getRequestStringParameter("hdataDalInCancelleria");
		String dataAlInCancelleria = getRequestStringParameter("hdataAlInCancelleria");

		// riempie il model
		lSogMod.setIdSoggetto(getRequestBigDecimalParameter("IdSoggetto"));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// Si chiama il FascicoloSiusController.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliDelSoggetto(lSogMod, ufUtConnesso,
				ufOTribunale, codDistretto, lIncludeArchiviati, codContenuto,
				DateUtils.getDate(dataAlInCancelleria, "ddMMyyyy"),
				DateUtils.getDate(dataAlInCancelleria, "ddMMyyyy"));

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

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_PROCDIESECUZIONEMSDELSOGGETTO;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}

}