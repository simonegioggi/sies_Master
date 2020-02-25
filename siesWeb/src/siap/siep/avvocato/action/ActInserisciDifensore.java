package siap.siep.avvocato.action;

/**
* <p>Title: ActInserisciAvvocato</p>
* <p>Description: Classe Action per l'inserimento di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

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
import f3b.log.LogF3B;
import f3b.security.model.ProfileModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActInserisciDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		UtenteModel lUte = (UtenteModel) getSessionAttribute("UtenteConnesso");
		ProfileModel lProfilo = (ProfileModel) lUte.getUserProfile();
		boolean ricercaFascicoloSige = false;

		// 11/09/2008 Prevista associazione a Fascicoli SIGE.
		if (!this.isRequestParameterNullObj("associa")
				&& this.getRequestStringParameter("associa").equals("Associa")) {
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
						// throw new F3BException( F3BException.USER_MESSAGE, "Fascicolo SIGE non presente in
						// sessione " );
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
		// lAvvMod.setDataSospensione(getRequestDateParameter(CAMPO_ANNO_DATA_SOSPENSIONE,CAMPO_MESE_DATA_SOSPENSIONE,CAMPO_GIORNO_DATA_SOSPENSIONE)
		// );
		// lAvvMod.setDataRadiazione(getRequestDateParameter(CAMPO_ANNO_DATA_RADIAZIONE,CAMPO_MESE_DATA_RADIAZIONE,CAMPO_GIORNO_DATA_RADIAZIONE)
		// );
		// lAvvMod.setCodNonAttivita(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA ));
		lAvvMod.setCodNonAttivita("-");
		lAvvMod.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
		lAvvMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAvvMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvMod.setDataInserimento(DateUtils.getSysDate());
		lAvvMod.setFlagCancellato("N");
		// AvvocatoController lCtrl = new AvvocatoController();

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
		// throw new F3BException(F3BException.USER_MESSAGE,"L'avvocato risulta già registrato");

		lAvvMod = lCtrl.ExInserisciAvvocato(lAvvMod); // setta la risposta nella request
														// setRequestAttribute("avvocato",lVect);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(this.getRequestStringParameter("associa"));
		if (!this.isRequestParameterNullObj("associa")
				&& this.getRequestStringParameter("associa").equals("Associa")) {
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