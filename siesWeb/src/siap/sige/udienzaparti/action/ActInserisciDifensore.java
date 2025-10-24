package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.SIAPException;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * ActInserisciDifensore - Classe Action per l'inserimento di un Difensore
 * 
 * @version 1.0
 */
public class ActInserisciDifensore extends ActionSige implements ICostantiAvvocato {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Difensore
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException, Exception {
		String idSoggetto = "";

		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}
		setRequestAttribute("idSoggetto", "" + idSoggetto);

		if (!this.isRequestParameterNullObj("associa")
				&& this.getRequestStringParameter("associa").equals("Associa")) {
			Vector lVect = null;
			try {
				siap.sige.avvocato.controller.IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
				lVect = lCtrl.ExRicercaAvvocatiByParteUdienza(new BigDecimal(idSoggetto));
			} catch (SIAPException e) {
				if (e.getErrorCode() != SIAPException.USER_MESSAGE) {
					throw e;
				}
			}
			if (lVect != null && lVect.size() > 1) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: Sono già assegnati due difensori!");
			}
		}

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = null;

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		// STUB 09/10/2008 Nella riverca Avvocato si discrimina anche per Ufficio di appartenenza.
		lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lAvvMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		lAvvMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
		lAvvMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lAvvMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lAvvMod.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
		lAvvMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());
		// da qui sul secondo model
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

		if (this.isRequestParameterNullObj("warning")) {
			try {
				lVect = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModRic);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" nessun avvocato trovato");
			}

			if (lVect != null && lVect.size() > 0) {
				setRequestAttribute("avvocato", lAvvMod);
				setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"L'avvocato risulta già registrato.Continuare con l'inserimento?");

				return "/jsp/files/siap/siep/avvocato/warningAvvocato.jsp";

			}
		}

		lAvvMod = lCtrl.ExInserisciAvvocato(lAvvMod);

		if (!this.isRequestParameterNullObj("associa")
				&& this.getRequestStringParameter("associa").equals("Associa")) {
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.udienzaparti.action.ActLoadInserisciAssegnaAvvocato&" + CAMPO_ID_AVVOCATO
					+ "=" + lAvvMod.getIdAvvocato().toString();
		} else {
			return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.udienzaparti.action.ActDettaglioAvvocato&" + CAMPO_ID_AVVOCATO + "="
					+ lAvvMod.getIdAvvocato().toString();
		}
	}

}