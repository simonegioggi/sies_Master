package siap.siep.statistiche.action;

import java.util.Vector;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;

/**
 * MEV_39: creata nuova classe per gestione Statistiche - Estrazione Dati - Ricerca Proc Classe IV e VII
 * 
 * @author Gioggi
 */
public class ActLoadRicercaProcedimentiClasseIVeVII extends ActionSiap implements ICostantiStatistiche {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Recupero Ufficio Utente
		UtenteModel um = new UtenteModel((UtenteModel) getSession().getAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String codUfficio = um.getUfficioUtente().getCodUfficio();
		String codTipoUfficio = um.getUfficioUtente().getCodTipoUfficio();

		IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
		Vector listaUfficiAccorpati = iUfficio.ListaUfficiAccorpati(codTipoUfficio, codUfficio);
		setRequestAttribute("listaUfficiAccorpati", listaUfficiAccorpati);

		// RECUPERO SYSDATE
		String sysDate = DateUtils.getSysDate("dd/MM/yyyy");
		setRequestAttribute("sysdate", sysDate);

		// recupero parametro in query string
		if (!isRequestParameterNullObj("classe"))
			setRequestAttribute("classe", getRequestStringParameter("classe"));
		if (!isRequestParameterNullObj("tipologia"))
			setRequestAttribute("tipologia", getRequestStringParameter("tipologia"));
		// valore di ritorno
		return PG_LOAD_RICERCA_PROCEDIMENTI_CLASSE_IV_VII;
	}

}