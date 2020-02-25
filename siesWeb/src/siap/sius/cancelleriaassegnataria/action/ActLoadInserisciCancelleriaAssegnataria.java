package siap.sius.cancelleriaassegnataria.action;

import java.util.Vector;

import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciCancelleriaAssegnataria
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di CancelleriaAssegnataria
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
public class ActLoadInserisciCancelleriaAssegnataria extends ActionSiap implements
		ICostantiCancelleriaAssegnataria {

	public String processRequest() throws F3BException {

		trovaUfficio();
		setRequestAttribute("modalita", "I");
		return PG_LOAD_INSERISCICANCELLERIAASSEGNATARIA; // restituisce la jsp di VIEW
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void trovaUfficio() throws F3BException {

		Vector lUffici = new Vector();
		UfficioModel lUfficioUtente = getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioUtente.getCodTipoUfficio();

		// La funzione può essere attivata solo per un Ufficio di Sorveglianza
		if (lCodTipoUfficio.compareToIgnoreCase("TDS") == 0
				|| lCodTipoUfficio.compareToIgnoreCase("UDS") == 0
				// Modifica del 19/04/2016 abilitati anche gli uffici dei minorenni
				|| lCodTipoUfficio.compareToIgnoreCase("TDSM") == 0
				|| lCodTipoUfficio.compareToIgnoreCase("UDSM") == 0)
			lUffici.add(lUfficioUtente);

		if (lUffici.isEmpty())
			throw new F3BException(F3BException.USER_MESSAGE,
					"Ufficio non esistente per la gestione della funzione!");

		setRequestAttribute("uffici", lUffici);
	}

}