package siap.sige.fascicolo.action;

import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadRicercaFascicoloSIEP Description: Classe Action per la ricerca del Procedimento SIEP. Viene
 * richiamata la maschera che attiva la funzione di Ricerca Procedimento SIEP. Company: Engineering
 * 
 * @version 1.0
 */
/*
 * public class ActLoadRicercaFascicoloSIEP extends ActLoadRicercaFascicolo { public String processRequest()
 * throws Exception { setRequestAttribute("next_action",
 * "siap.sige.fascicolo.action.ActRicercaFascicoloSIEP");
 * 
 * setRequestAttribute("titolo", "Ricerca Procedimento SIEP");
 * 
 * String strCodUfficio = getUfficioUtenteConnesso().getCodUfficio();
 * setRequestAttribute("ufficioUtenteConnesso", strCodUfficio);
 * 
 * // esegue la query per recuperare l'elenco degli uffici accorpati IUfficio lUACon =
 * SICOLookupRemote.getUfficioRemote(); Vector <UfficioModel>lUffAccTotali = lUACon.ListaUfficiAccorpati(null,
 * null); String codUfficioUtenteConnesso=super.getCodUfficioUtenteConnesso();
 * 
 * UfficioModel ufficio = lUACon.getUfficioByKey(codUfficioUtenteConnesso);
 * 
 * 
 * 
 * // imposta sulla request la lista degli uffici accorpati setRequestAttribute("ufficiAccorpati",
 * lUffAccTotali);
 * 
 * return super.processRequest(); }
 * 
 * }
 */
public class ActLoadRicercaFascicoloSIEP extends ActionSiap implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setRequestAttribute("next_action", "siap.sige.fascicolo.action.ActRicercaFascicoloSIEP");

		setRequestAttribute("titolo", "Ricerca Procedimento SIEP");

		// Ufficio Utente connesso
		String strCodUfficio = getUfficioUtenteConnesso().getCodUfficio();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String strDescrComune = getUfficioUtenteConnesso().getDescrComune();

		// Imposta Tipo Ufficio SIEP.
		String tipoUfficioSiep = "";
		if (strCodTipoUfficio.equals("DIB") || strCodTipoUfficio.equals("GIP")
				|| strCodTipoUfficio.equals("CAS")) {
			tipoUfficioSiep = "PM";
		} else if (strCodTipoUfficio.equals("DIBM") || strCodTipoUfficio.equals("GIPM")
				|| strCodTipoUfficio.equals("GUPM")) {
			tipoUfficioSiep = "PMM";
		} else if (strCodTipoUfficio.equals("CAP") || strCodTipoUfficio.equals("CASAP")
				|| strCodTipoUfficio.equals("CAPSM")) {
			tipoUfficioSiep = "PGCAP";
		}

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIEPSIGE(),
				tipoUfficioSiep);
		setRequestAttribute("ufficioSIEP", "" + lOption);

		setRequestAttribute("UfficioConnesso", strCodUfficio);
		setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio);
		setRequestAttribute("ComuneUfficioConnesso", strDescrComune);

		// esegue la query per recuperare l'elenco degli uffici accorpati
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(tipoUfficioSiep, null);
		Vector lUffAccUtente = lUACon.ListaUfficiAccorpati(tipoUfficioSiep, strCodUfficio);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);
		setRequestAttribute("elencoUfficiAccorpati", lUffAccUtente);

		return PG_LOAD_RICERCAFASCICOLO_SIEP_SIGE; // restituisce la jsp di VIEW
	}

}