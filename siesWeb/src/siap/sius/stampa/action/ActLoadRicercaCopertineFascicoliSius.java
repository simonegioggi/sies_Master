package siap.sius.stampa.action;

import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.util.SIUSLookupRemote;

import java.util.Vector;

import f3b.util.F3BException;

/**
 * MEV_65: aggiunta action per gestire nuova funzionalita'
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActLoadRicercaCopertineFascicoliSius extends ActionSiap implements ICostantiStampaSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Prepara il model di ricerca delle Cancellerie Assegnatarie previste per l'Ufficio dell'utente
		CancelleriaAssegnatariaModel cam = new CancelleriaAssegnatariaModel();
		cam.setCodUfficio(getCodUfficioUtenteConnesso());

		// Ricerca
		ICancelleriaAssegnataria ica = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
		Vector cancellerie = ica.ExRicercaCancelleriaAssegnataria(cam);
		if (cancellerie != null && cancellerie.size() > 0)
			setRequestAttribute("cancellerie", cancellerie);
		// valore di ritorno
		return PG_LOAD_RICERCA_COPERTINE_FASCICOLI_SIUS;
	}

}