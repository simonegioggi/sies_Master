package siap.sius.avvocato.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
//import siap.sius.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaAvvocato
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Avvocato
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
public class ActModificaAvvocato extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Modifica del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

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
		lAvvMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvMod.setDataAggiornamento(DateUtils.getSysDate());

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		lAvvMod = lCtrl.ExModificaAvvocato(lAvvMod);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("avvocato", lAvvMod);

//		String lPage = "";
		return /*lPage = */IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getIdAvvocato().toString();
	}

}