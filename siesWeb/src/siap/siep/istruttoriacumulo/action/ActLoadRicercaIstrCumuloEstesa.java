package siap.siep.istruttoriacumulo.action;

import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * Action per il caricamento delle form di Ricerca Istruttorie Cumulo Estesa,
 *
 * @author
 */
public class ActLoadRicercaIstrCumuloEstesa extends ActionSiap implements ICostantiIstruttoriaCumulo {
	/**
	 * Viene invocata direttamente dal menù rapido orizzontale.
	 */

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceDistrettoUtente = lUtenteConnesso.getUfficioUtente().getCodDistretto();

		IUfficio iUff = SICOLookupRemote.getUfficioRemote();

		Vector uffici = iUff.ListaUfficiCompletaDistrettoAbilitatiLogin(StrCodiceDistrettoUtente);
		setRequestAttribute("distretto", StrCodiceDistrettoUtente);
		setRequestAttribute("uffici", uffici);
		setRequestAttribute("descrComune", lUtenteConnesso.getUfficioUtente().getDescrComune());

		String tipoUfficioUtenteConnesso = lUtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("tipoUfficioUtenteConnesso", tipoUfficioUtenteConnesso);
		String ufficioUtenteConnesso = lUtenteConnesso.getUfficioUtente().getCodUfficio();
		setRequestAttribute("ufficioUtenteConnesso", ufficioUtenteConnesso);

		Option lOption = new Option(DecodificheManager.getInstance().getStatoIstruttoriaCumulo());
		setRequestAttribute("statoIstruttoriaCumulo", "" + lOption);

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

		return PG_LOAD_RICERCA_ISTR_CUMULO_ESTESA;
	}

}