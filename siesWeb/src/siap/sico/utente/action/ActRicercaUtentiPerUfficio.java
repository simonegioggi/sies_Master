package siap.sico.utente.action;

import java.util.Vector;

import siap.sico.SICOException;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.controller.IUtente;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaUtentePerUfficio
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Utente per Ufficio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaUtentiPerUfficio extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		String sede = getRequestStringParameter(CAMPO_SEDE_UFFICIO);
		String tipo = getRequestStringParameter(CAMPO_TIPO_UFFICIO);
		String cognome = getRequestStringParameter(ICostantiUtente.CAMPO_COGNOME);
		String nome = getRequestStringParameter(ICostantiUtente.CAMPO_NOME);
		String codUt = getRequestStringParameter(ICostantiUtente.CAMPO_COD_UTENTE);
		String codUfficio = "";

		if (!sede.equals("") && this.getCodComuneByDescr(sede) != null) {
			codUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(tipo, sede);
		}

		// ==========================================================================
		// Verifica che l'ufficio selezionato sia di competenza dell'Utente connesso
		// Se l'ufficio non è stato selezionato e l'utente ha profilo 99, la ricerca
		// viene ristretta ai soli utenti del distretto ell'utente connesso
		// ==========================================================================
		String lCodDistretto = getCodDistrettoUtenteConnesso();
		if (getUtenteConnesso().isSysAdmin()) {
			if (!codUfficio.equals("")) {
				UfficioModel lUffMod = getUfficioByCodUfficio(codUfficio);
				if (!lUffMod.getCodDistretto().equals(lCodDistretto)) {
					throw new SICOException(SICOException.USER_MESSAGE,
							"L'Ufficio selezionato non appartiene al Distretto");
				}
			}
		}

		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		Vector lVect = lCtrl.ExRicercaUtentiPerUfficio(codUfficio, lCodDistretto, cognome, nome, codUt);

		setRequestAttribute("utenti", lVect);
		setRequestAttribute("ufficio", tipo);
		setRequestAttribute("sede", sede);

		return ROOT_DIR + "files/siap/sico/utente/RicercaUtentiPerUfficio.jsp";
	}

}