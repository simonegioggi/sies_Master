package siap.sico.utente.action;

import java.util.Vector;

import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaUtente
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Utente
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaUtente extends ActionSiap implements ICostantiUtente {

	public String processRequest() throws F3BException {

		UtenteModel lUteMod = new UtenteModel();
		lUteMod.setUserId(getRequestStringParameter(CAMPO_COD_UTENTE));
		lUteMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lUteMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lUteMod.setPwd(getRequestStringParameter(CAMPO_PWD));
		lUteMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lUteMod.setDataOraConnessione(getRequestDateParameter(CAMPO_ANNO_DATA_ORA_CONNESSIONE,
				CAMPO_MESE_DATA_ORA_CONNESSIONE, CAMPO_GIORNO_DATA_ORA_CONNESSIONE));
		lUteMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lUteMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lUteMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lUteMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lUteMod.setDataUltimaModifcaPwd(getRequestDateParameter(CAMPO_ANNO_DATA_ULTIMA_MODIFICA_PWD,
				CAMPO_MESE_DATA_ULTIMA_MODIFICA_PWD, CAMPO_GIORNO_DATA_ULTIMA_MODIFICA_PWD));

		IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		Vector lVect = lCtrl.ExRicercaUtente(lUteMod);
		setRequestAttribute("utente", lVect);

		return PG_RICERCAUTENTE;
	}

}