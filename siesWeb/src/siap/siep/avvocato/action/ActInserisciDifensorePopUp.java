package siap.siep.avvocato.action;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActInserisciAvvocato - Classe Action per l'inserimento di Avvocato
 *
 * @version 1.0
 */
public class ActInserisciDifensorePopUp extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Inserimento del Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoModel lAvvModRic = new AvvocatoModel();
		lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lAvvMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		lAvvMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
		lAvvMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lAvvMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lAvvMod.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
		lAvvMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());

		if (this.isRequestParameterNullObj("warning")) {
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA)));
			lAvvMod.setCodLuogoNascita(lComMod.getCodComune());
			ComuneModel lComModRes = new ComuneModel(this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvMod.setCodComuneResidenza(lComModRes.getCodComune());

		} else {
			lAvvMod.setCodLuogoNascita(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA));
			lAvvMod.setCodComuneResidenza(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA));
		}

		lAvvMod.setDataNascita(getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
				ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA, ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA));
		lAvvMod.setCodNonAttivita("-");
		lAvvMod.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
		lAvvMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAvvMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvMod.setDataInserimento(DateUtils.getSysDate());
		lAvvMod.setFlagCancellato("N");

		setRequestAttribute("formname", getRequestStringParameter("formname"));

		lAvvMod = lCtrl.ExInserisciAvvocato(lAvvMod); // setta la risposta nella request
														// setRequestAttribute("avvocato",lVect);
		setRequestAttribute("formname", getRequestStringParameter("formname"));
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActDettaglioAvvocatoPopUp&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getIdAvvocato().toString();
	}

}