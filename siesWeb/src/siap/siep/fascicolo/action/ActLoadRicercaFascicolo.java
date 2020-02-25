package siap.siep.fascicolo.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaFascicolo extends ActionSiap implements ICostantiFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		this.setLinkRitorno(); // STUB 04/03/2005

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIEP(), "PGCAP");
		setRequestAttribute("ufficioSIEP", "" + lOption);
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getCodDistretto();

		IUfficio iUff = SICOLookupRemote.getUfficioRemote();

		Vector uffici = iUff.ListaUfficiCompletaDistrettoAbilitatiLogin(StrCodiceDistrettoUtente);
		setRequestAttribute("distretto", StrCodiceDistrettoUtente);
		setRequestAttribute("uffici", uffici);
		setRequestAttribute("descrComune", lUtenteConnesso.getUfficioUtente().getDescrComune());

		// String comuneUtenteConnesso = lUtenteConnesso.getUfficioUtente().getDescrComune();
		String tipoUfficioUtenteConnesso = lUtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("tipoUfficioUtenteConnesso", tipoUfficioUtenteConnesso);
		String ufficioUtenteConnesso = lUtenteConnesso.getUfficioUtente().getCodUfficio();
		setRequestAttribute("ufficioUtenteConnesso", ufficioUtenteConnesso);

		// Luigi 7-10-2004 Viene testato il tipo di ufficio per limitare le funzioni nella jsp
		if (tipoUfficioUtenteConnesso.compareToIgnoreCase("TDS") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("UDS") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("TDSM") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("UDSM") == 0
				||
				// Uffici SIGE
				tipoUfficioUtenteConnesso.compareToIgnoreCase("CAP") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("CAS") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("CASAP") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("GIP") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("GIPM") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("GUP") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("GUPM") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("DIB") == 0
				|| tipoUfficioUtenteConnesso.compareToIgnoreCase("DIBM") == 0)
			setRequestAttribute("ricercaEstesa", "NO");

		// Vector lCom = iUff.ExGetListaComuniUfficiPerDistretto(StrCodiceDistrettoUtente,
		// comuneUtenteConnesso, UfficioUtenteConnesso);
		// UfficioModel uffcioModel = (UfficioModel) lCom.get(0);
		// String descrizioneComune = uffcioModel.getCodComune();
		// setRequestAttribute("comuneUtenteConnesso", comuneUtenteConnesso);
		// Vector ufficiEsecuzione = iUff.ListaUfficiEsecuzione();

		// lOption = new Option(DecodificheManager.getInstance().getUfficioLogin());
		// setRequestAttribute("ufficio", "" + lOption );

		// model degli elementi del vettore
		// UfficioAccorpatoModel lUAMod = new UfficioAccorpatoModel();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati("PM", null);
		Vector lUffAccUtente = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);
		setRequestAttribute("elencoUfficiAccorpati", lUffAccUtente);

		// Modifica del 27/03/2017
		// Aggiunto parametro per identificare la funzione che richiama la maschera
		// di Ricerca Procedimento SIEP.
		// Quando la maschera viene richiamata dalla funzione "Ricerca Altre BDI"
		// (cod. 90111824)- SIGE i campi relativi all'ufficio non devono essere
		// prevalorizzati con i dati dell'utente connesso
		String codFunzione = getCodFunMenuVerticale();
		if (codFunzione != null && codFunzione.equals("90111824")) { // Menu Ricerche Altre BDI
			setRequestAttribute("descrComune", "");
		}
		setRequestAttribute("codFunzione", codFunzione);

		return PG_LOAD_RICERCAFASCICOLO_SIEP; // restituisce la jsp di VIEW
	}

}