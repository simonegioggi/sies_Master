package siap.sico.log.action;

import java.util.Date;
import java.util.Vector;

import siap.sico.log.controller.ILogAttivita;
import siap.sico.log.model.LogAttivitaModel;
import siap.sico.utente.action.ICostantiUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaLogAttivita
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di LogAttivita
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
public class ActRicercaLogAttivita extends ActionSiap implements ICostantiLogAttivita {

	public String processRequest() throws F3BException {

		Date DataInizio = null;
		Date DataFine = null;
		LogAttivitaModel lLogMod = new LogAttivitaModel();
		if (!isRequestParameterNullObj(CAMPO_RECORD))
			lLogMod.setRecord(getRequestStringParameter(CAMPO_RECORD));
		if (!isRequestParameterNullObj(CAMPO_COD_OPERATORE))
			lLogMod.setCodOperatore(getRequestStringParameter(CAMPO_COD_OPERATORE));
		if (!isRequestParameterNullObj(ICostantiUtente.CAMPO_COGNOME))
			lLogMod.setCognome(getRequestStringParameter(ICostantiUtente.CAMPO_COGNOME));
		if (!isRequestParameterNullObj(ICostantiUtente.CAMPO_NOME))
			lLogMod.setNome(getRequestStringParameter(ICostantiUtente.CAMPO_NOME));
		if (!isRequestParameterNullObj(CAMPO_IP_UTENTE))
			lLogMod.setIpUtente(getRequestStringParameter(CAMPO_IP_UTENTE));

		if (!isRequestParameterNullObj("ggIn"))
			DataInizio = getRequestDateParameter("aaIn", "mmIn", "ggIn");
		if (!isRequestParameterNullObj("ggFi"))
			DataFine = getRequestDateParameter("aaFi", "mmFi", "ggFi");

		ILogAttivita lCtrl = SICOLookupRemote.getLogAttivitaRemote();
		String FlagAttivita = "";
		if (!isRequestParameterNullObj("FlagAttivita"))
			FlagAttivita = ((UtenteModel) getSessionAttribute("UtenteConnesso")).getUserId();
		Vector lVect = lCtrl.ExRicercaLogAttivita(lLogMod, DataInizio, DataFine, FlagAttivita);
		setRequestAttribute("logattivita", lVect);
		setRequestAttribute("FreeText", lLogMod.getRecord());

		return PG_RICERCALOGATTIVITA;
	}

}