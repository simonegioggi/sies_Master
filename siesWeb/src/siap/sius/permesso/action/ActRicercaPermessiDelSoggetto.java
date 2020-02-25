package siap.sius.permesso.action;

import java.util.Vector;

import f3b.util.DateUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.PermessoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti relativi a permesso (rispondenti ai parametri selezionati) del
 * soggetto individuato
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
public class ActRicercaPermessiDelSoggetto extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		SoggettoModel lSogMod = new SoggettoModel();

		// Recupero dei parametri di ricerca.
		String ufUtConnesso = getRequestStringParameter("hufUtConnesso");
		String ufOTribunale = getRequestStringParameter("hufOTribunale");
		String codDistretto = getRequestStringParameter("hcodDistretto");
		String lIncludeRigettati = getRequestStringParameter("hlIncludeRigettati");
		String codPermesso = getRequestStringParameter("hcodPermesso");
		String descrPermesso = getRequestStringParameter("hdescrPermesso");
		String tipoUfficio = getRequestStringParameter("htipoUfficio");
		String dataDalInCancelleria = getRequestStringParameter("hdataDalInCancelleria");
		String dataAlInCancelleria = getRequestStringParameter("hdataAlInCancelleria");

		// riempie il model
		lSogMod.setIdSoggetto(getRequestBigDecimalParameter("IdSoggetto"));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// Si chiama il PermessoController.
		IPermesso lPermCtrl = SIUSLookupRemote.getPermessoRemote();
		Vector lPermessiSoggetto = lPermCtrl.ExRicercaPermessiDelSoggetto(lSogMod, ufUtConnesso, ufOTribunale,
				codDistretto, lIncludeRigettati, codPermesso,
				DateUtils.getDate(dataDalInCancelleria, "dd/MM/yyyy"),
				DateUtils.getDate(dataAlInCancelleria, "dd/MM/yyyy"));

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lPermessiSoggetto != null) {
			SoggettoModel lSoggetto = ((PermessoModel) lPermessiSoggetto.get(0)).getSoggetto();
			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto); // Da rivedere
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", ufUtConnesso);
		setRequestAttribute("ufOTribunale", ufOTribunale);
		setRequestAttribute("codDistretto", codDistretto);
		setRequestAttribute("lIncludeRigettati", lIncludeRigettati);
		setRequestAttribute("codPermesso", codPermesso);
		setRequestAttribute("descrPermesso", descrPermesso);
		setRequestAttribute("dataDalInCancelleria", dataDalInCancelleria);
		setRequestAttribute("dataAlInCancelleria", dataAlInCancelleria);
		setRequestAttribute("tipoUfficio", tipoUfficio);

		// Setta la risposta nella request
		setRequestAttribute("permessi", lPermessiSoggetto);

		lReturnPage = ICostantiPermesso.PG_RICERCA_PERMESSIDELSOGGETTO;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}

}