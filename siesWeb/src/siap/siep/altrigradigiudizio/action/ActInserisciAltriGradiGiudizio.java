package siap.siep.altrigradigiudizio.action;

/**
* <p>Title: ActInserisciAltriGradiGiudizio</p>
* <p>Description: Classe Action per l'inserimento di AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del AltriGradiGiudizio
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		AltriGradiGiudizioModel lAltMod = new AltriGradiGiudizioModel();

		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

		lAltMod.setSenIdSentenza(lSen.getIdSentenza());
		lAltMod.setDataSentenzaIGrado(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA_I_GRADO,
				CAMPO_MESE_DATA_SENTENZA_I_GRADO, CAMPO_GIORNO_DATA_SENTENZA_I_GRADO));
		lAltMod.setAnnoSentenzaIGrado(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_I_GRADO));
		lAltMod.setNumeroSentenzaIGrado(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_I_GRADO));
		lAltMod.setCodAutEmittSentIGrado(getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_I_GRADO));
		if (!isRequestParameterNullObj(CAMPO_COD_LUO_EMITT_SENT_I_GRADO)) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUO_EMITT_SENT_I_GRADO)));
			lAltMod.setCodLuoEmittSentIGrado(lComMod.getCodComune());
		} else
			lAltMod.setCodLuoEmittSentIGrado("-");

		lAltMod.setNumSezEmittSentIGrado(getRequestStringParameter(CAMPO_NUM_SEZ_EMITT_SENT_I_GRADO));
		lAltMod.setCodTipoSentenzaIiGrado(getRequestStringParameter(CAMPO_COD_TIPO_SENTENZA_II_GRADO));
		// Hanno inserito l'autorità di riferimento
		if (getRequestStringParameter(CAMPO_COD_TIPO_SENTENZA_II_GRADO).compareTo("-") != 0) {
			String lCodTipo = getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_I_GRADO);

			// MODIFICA 01-03-2006 -- DARIO -- VIVIANA
			// AGGIUNGO ALTRI UFFICI A QUELLI ESISTENTI E SOSTITUISCO CAS CON CASAP
			// if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CAS") ||
			// lCodTipo.equals("PGCAP"))
			if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CASAP")
					|| lCodTipo.equals("PGCAP") || lCodTipo.equals("PGMI") || lCodTipo.equals("PGMID")) {
				if (getRequestStringParameter(CAMPO_COD_TIPO_SENTENZA_II_GRADO).equals("01"))
					lAltMod.setCodTipoSentenzaIiGrado("03");
				else
					lAltMod.setCodTipoSentenzaIiGrado("04");
			}

		}

		lAltMod.setDataSentenzaIiGrado(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA_II_GRADO,
				CAMPO_MESE_DATA_SENTENZA_II_GRADO, CAMPO_GIORNO_DATA_SENTENZA_II_GRADO));
		lAltMod.setAnnoSentenzaIiGrado(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_II_GRADO));
		lAltMod.setNumeroSentenzaIiGrado(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_II_GRADO));
		lAltMod.setCodAutEmittSentIiGrado(getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_II_GRADO));

		lAltMod.setCodLuoEmittSentIiGrado(getRequestStringParameter(CAMPO_COD_LUO_EMITT_SENT_II_GRADO));
		if (!isRequestParameterNullObj(CAMPO_COD_LUO_EMITT_SENT_II_GRADO)) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUO_EMITT_SENT_II_GRADO)));
			lAltMod.setCodLuoEmittSentIiGrado(lComMod.getCodComune());
		} else
			lAltMod.setCodLuoEmittSentIiGrado("-");

		lAltMod.setNumSezEmittSentIiGrado(getRequestStringParameter(CAMPO_NUM_SEZ_EMITT_SENT_II_GRADO));

		lAltMod.setAnnoRegGenCassaz(getRequestBigDecimalParameter(CAMPO_ANNO_REG_GEN_CASSAZ));
		lAltMod.setNumeroRegGenCassaz(getRequestStringParameter(CAMPO_NUMERO_REG_GEN_CASSAZ));
		lAltMod.setAnnoSentenzaCassaz(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_CASSAZ));
		lAltMod.setNumeroSentenzaCassaz(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZ));
		lAltMod.setAnnoRaccGenealeIiGrado(getRequestBigDecimalParameter(CAMPO_ANNO_RACC_GENEALE_II_GRADO));
		lAltMod.setNumeroRaccGenealeIiGrado(getRequestStringParameter(CAMPO_NUMERO_RACC_GENEALE_II_GRADO));
		lAltMod.setCodTipoDecisioneCassazione(getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// COD_TIPO_RITO
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO)) {

			lAltMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO));

			if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO_RIF)) {

				if (!getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF).equals("-"))
					lAltMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF));

			}
		} else {

			lAltMod.setCodTipoRito("-");
		}

		// 15/07/2010 Controllo presenza ufficio
		if (getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_I_GRADO).compareTo("-") != 0) {
			String lCodTipo = getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_I_GRADO);
			/* String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(lCodTipo,
					getRequestStringParameter(CAMPO_COD_LUO_EMITT_SENT_I_GRADO));
		}
		if (getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_II_GRADO).compareTo("-") != 0) {
			String lCodTipo = getRequestStringParameter(CAMPO_COD_AUT_EMITT_SENT_II_GRADO);
			/* String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(lCodTipo,
					getRequestStringParameter(CAMPO_COD_LUO_EMITT_SENT_II_GRADO));
		}

		lAltMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		lAltMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lAltMod.setDataInserimento(DateUtils.getSysDate());

		AgdgFascicoloSiepModel lAgdgFascMod = new AgdgFascicoloSiepModel();

		IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();
		AltriGradiGiudizioModel lAltModRet; // setta la risposta nella request

		if (!isSessionAttributeNullObj("fascicolo")) {
			FascicoloSiepModel lFasc = new FascicoloSiepModel(
					(FascicoloSiepModel) getSessionAttribute("fascicolo"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("lFasc.getSenIdSentenza()= " + lFasc.getSenIdSentenza());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("lSen.getIdSentenza()= " + lSen.getIdSentenza());

			if (lFasc.getSenIdSentenza().compareTo(lSen.getIdSentenza()) == 0) {
				lAgdgFascMod.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());
				lAltModRet = lCtrl.ExInserisciAltriGradiGiudizioFascicoloSiep(lAltMod, lAgdgFascMod); // setta
																										// la
																										// risposta
																										// nella
																										// request
			} else {
				lAltModRet = lCtrl.ExInserisciAltriGradiGiudizio(lAltMod); // setta la risposta nella request
			}
		} else {
			lAltModRet = lCtrl.ExInserisciAltriGradiGiudizio(lAltMod); // setta la risposta nella request
		}

		setRequestAttribute("altrogradogiudizio", lAltModRet);
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("llAltModRet-------------- inizio-----------------" + llAltModRet);
		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.altrigradigiudizio.action.ActLoadDettaglioAltriGradiGiudizio&"
				+ CAMPO_ID_ALTRIGRADIGIUDIZIO + "=" + lAltModRet.getIdAltrigradigiudizio().toString();
		return lPage;
	}

}