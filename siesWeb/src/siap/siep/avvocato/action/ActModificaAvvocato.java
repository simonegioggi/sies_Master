package siap.siep.avvocato.action;

/**
* <p>Title: ActModificaAvvocato</p>
* <p>Description: Classe Action per la modifica di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
//import siap.siep.avvocato.controller.AvvocatoController;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaAvvocato extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Modifica del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("avvocato", getRequestStringParameter(CAMPO_ID_AVVOCATO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'" + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		String lId = getRequestStringParameter(CAMPO_ID_AVVOCATO);
		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(new BigDecimal(lId));
		lAvvMod.setIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));
		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lAvvMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO));
		lAvvMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
		lAvvMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lAvvMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lAvvMod.setEMail(getRequestStringParameter(CAMPO_E_MAIL));
		lAvvMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE));
		lAvvMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAvvMod.setDataAggiornamento(DateUtils.getSysDate());

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		lAvvMod = lCtrl.ExModificaAvvocato(lAvvMod);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("avvocato", lAvvMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getIdAvvocato().toString();
	}

}