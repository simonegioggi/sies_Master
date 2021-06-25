package siap.siep.avvocato.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.SIAPException;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

@SuppressWarnings("rawtypes")
public class ActInserisciDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		UtenteModel lUte = (UtenteModel) getSessionAttribute("UtenteConnesso");
		ProfileModel lProfilo = lUte.getUserProfile();
		boolean ricercaFascicoloSige = false;

		// 11/09/2008 Prevista associazione a Fascicoli SIGE.
		if (!isRequestParameterNullObj("associa") && getRequestStringParameter("associa").equals("Associa")) {
			Vector lVect = null;
			try {
				if (lProfilo.isSiep()) {
					BigDecimal id_fascicolo = ((FascicoloSiepModel) (getSessionAttribute("fascicolo")))
							.getIdFascicoloSiep();
					IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
					lVect = lCtrl.ExRicercaAvvocatiByFascicolo(id_fascicolo);
				} else if (lProfilo.isSius()) {
					BigDecimal id_fascicolo = (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
							.getFascicoloSiusModel().getIdFascicoloSius());
					siap.sius.avvocato.controller.IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
					lVect = lCtrl.ExRicercaAvvocatiByFascicolo(id_fascicolo);
				} else if (lProfilo.isSige()) {
					if (!isSessionAttributeNullObj("FascicoloSigeEsteso")) {
						BigDecimal id_fascicolo = (((FascicoloSigeEstesoModel) getSessionAttribute(
								"FascicoloSigeEsteso")).getFascicoloSige().getIdFascicoloSige());
						siap.sige.avvocato.controller.IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
						lVect = lCtrl.ExRicercaAvvocatiByFascicolo(id_fascicolo);
					} else {
						ricercaFascicoloSige = true;
					}
				} else {
					throw new F3BException(F3BException.USER_MESSAGE,
							"Attenzione: Profilo Utente non abilitato!");
				}
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
		lAvvModRic.setCodUffAppartenenza(getCodUfficioUtenteConnesso());

		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lAvvMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		lAvvMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
		lAvvMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lAvvMod.setFax(getRequestStringParameter(CAMPO_FAX));
		// MEV_21: aggiungo impostazione di variabili
		lAvvMod.setPec(getRequestStringParameter(CAMPO_PEC).toUpperCase());
		lAvvMod.setDescrComuneStudio(getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO).toUpperCase());
		lAvvMod.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
		lAvvMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());
		// da qui sul secondo model
		if (isRequestParameterNullObj("warning")) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)));
			lAvvMod.setCodLuogoNascita(lComMod.getCodComune());
			// MEV_21: aggiungo controllo di consistenza
			if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA)) {
				ComuneModel lComModRes = new ComuneModel(
						getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA)));
				lAvvMod.setCodComuneResidenza(lComModRes.getCodComune());
			} else {
				ComuneModel lComModRes = new ComuneModel(
						getCodComuneByDescr(getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO)));
				lAvvMod.setCodComuneResidenza(lComModRes.getCodComune());
			}
		} else {
			lAvvMod.setCodLuogoNascita(getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA));
			if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA))
				lAvvMod.setCodComuneResidenza(getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA));
		}

		lAvvMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		// lAvvMod.setDataSospensione(getRequestDateParameter(CAMPO_ANNO_DATA_SOSPENSIONE,CAMPO_MESE_DATA_SOSPENSIONE,CAMPO_GIORNO_DATA_SOSPENSIONE)
		// );
		// lAvvMod.setDataRadiazione(getRequestDateParameter(CAMPO_ANNO_DATA_RADIAZIONE,CAMPO_MESE_DATA_RADIAZIONE,CAMPO_GIORNO_DATA_RADIAZIONE)
		// );
		// lAvvMod.setCodNonAttivita(getRequestStringParameter(CAMPO_COD_NON_ATTIVITA));
		lAvvMod.setCodNonAttivita("-");
		lAvvMod.setCodUffAppartenenza(getCodUfficioUtenteConnesso());
		lAvvMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAvvMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvMod.setDataInserimento(DateUtils.getSysDate());
		lAvvMod.setFlagCancellato("N");
		// AvvocatoController lCtrl = new AvvocatoController();

		if (isRequestParameterNullObj("warning")) {
			try {
				lVect = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModRic);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessun avvocato trovato!");
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

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(getRequestStringParameter("associa"));
		if (!isRequestParameterNullObj("associa") && getRequestStringParameter("associa").equals("Associa")) {
			if (lProfilo.isSiep()) {
				return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.avvocato.action.ActLoadInserisciAssegnaAvvocato&" + CAMPO_ID_AVVOCATO
						+ "=" + lAvvMod.getIdAvvocato().toString();

			}
			if (lProfilo.isSius()) {
				return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.sius.avvocato.action.ActLoadInserisciAssegnaAvvocato&" + CAMPO_ID_AVVOCATO
						+ "=" + lAvvMod.getIdAvvocato().toString();
			}

			if (lProfilo.isSige()) {
				if (ricercaFascicoloSige) {
					return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.sige.avvocato.action.ActLoadFSigePerAssociaAvvocato&" + CAMPO_ID_AVVOCATO
							+ "=" + lAvvMod.getIdAvvocato().toString();
				} else {
					return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
							+ "=siap.sige.avvocato.action.ActLoadInserisciAssegnaAvvocato&"
							+ CAMPO_ID_AVVOCATO + "=" + lAvvMod.getIdAvvocato().toString();
				}
			}
		}

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActDettaglioAvvocato&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getIdAvvocato().toString();
	}

}