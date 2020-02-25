package siap.siep.sentenza.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.siep.SIEPException;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Inserisce una sentenza sul DB
 * <p>
 * Title:ActInserisciSentenza
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ActInserisciSentenza extends ActInserisciSentenzaGenerale implements ICostantiSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// valore di ritorno
		return super.processRequest();
	}

	/**
	 * Implementazione della funzione abstract richiamata dalla processRequest() del super che valorizza i
	 * dati specifici di questo tipo di sentenza.
	 */

	public String preparazioneDatiSpecifici() throws Exception {

		ComuneModel lComMod;

		mSenMod.setCodTipoProvvedimento("01");

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		mSenMod.setDescrUfficioInserimento(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());

		/* String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));

		// mSenMod.setFlagSentenzaApplicazPena( getRequestStringParameter( CAMPO_FLAG_SENTENZA_APPLICAZ_PENA)
		// );
		mSenMod.setCodTipoProvvRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF));
		mSenMod.setDataProvvRif(getRequestDateParameter(CAMPO_ANNO_DATA_PROVV_RIF, CAMPO_MESE_DATA_PROVV_RIF,
				CAMPO_GIORNO_DATA_PROVV_RIF));
		mSenMod.setCodTipoAutoritaProvvRif(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF));
		mSenMod.setAnnoProvvRif(getRequestBigDecimalParameter(CAMPO_ANNO_PROVV_RIF));
		mSenMod.setNumeroProvvRif(getRequestStringParameter(CAMPO_NUMERO_PROVV_RIF));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF))
			mSenMod.setCodTipoProvvedimentoRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF));
		else
			mSenMod.setCodTipoProvvedimentoRif("-");

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO))
			mSenMod.setCodTipoProvvedimentoAltro(
					getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO));
		else
			mSenMod.setCodTipoProvvedimentoAltro("-");

		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			mSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else
			mSenMod.setCodSedeNotiziaReato("-");

		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_PROVV_RIF)) {
			lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_PROVV_RIF)));
			mSenMod.setCodLuogoProvvRif(lComMod.getCodComune());
		} else
			mSenMod.setCodLuogoProvvRif("-");

		// Hanno inserito l'autorità di riferimento
		if (getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF).compareTo("-") != 0) {
			String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
			/* lCodice = */getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF),
					getRequestStringParameter(CAMPO_COD_LUOGO_PROVV_RIF));

			// MODIFICA 01-03-2006 -- DARIO -- VIVIANA
			// AGGIUNGO ALTRI UFFICI A QUELLI ESISTENTI E SOSTITUISCO CAS CON CASAP
			// if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CAS") ||
			// lCodTipo.equals("PGCAP"))
			if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CASAP")
					|| lCodTipo.equals("PGCAP") || lCodTipo.equals("PGMI") || lCodTipo.equals("PGMID")) {
				if (getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF).equals("01"))
					mSenMod.setCodTipoProvvRif("03");
				else
					mSenMod.setCodTipoProvvRif("04");
			}

		}
		mSenMod.setNumSezioneAutoritaProvvRif(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF));

		// if (!isRequestParameterNullObj(CAMPO_DESCR_NUM_CAMPIONE_PENALE))
		// mSenMod.setDescrNumCampionePenale( getRequestStringParameter( CAMPO_DESCR_NUM_CAMPIONE_PENALE) );

		// COD_TIPO_RITO
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO)) {

			mSenMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO));

			if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO_RIF)) {

				if (!getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF).equals("-"))
					mSenMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF));

			}
		} else {

			mSenMod.setCodTipoRito("-");
		}

		// -----------------------------------------------------------------

		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gip")) {
			mSenMod.setAnnoRegeGip(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeGip(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("dib")) {
			mSenMod.setAnnoRegeDib(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeDib(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cas")) {
			mSenMod.setAnnoRegeCas(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeCas(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cap")) {
			mSenMod.setAnnoRegeCap(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeCap(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("casap")) {
			mSenMod.setAnnoRegeCasap(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeCasap(getRequestStringParameter("NRG"));
		}
		// MEV_66: aggiunte quattro nuove proprietà
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gup")) {
			mSenMod.setAnnoRegeGup(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeGup(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("capsm")) {
			mSenMod.setAnnoRegeCapsm(getRequestBigDecimalParameter("ARG"));
			mSenMod.setNumeroRegeCapsm(getRequestStringParameter("NRG"));
		}

		// ----------------------------------------------------------------------------
		// Pezza d'appoggio....occhio che è palesemente na fregnaccia
		// Gianluca 15.03.2004
		// ----------------------------------------------------------------------------

		mSenMod.setNote1DecisioneCassazione(getRequestStringParameter("ANNOREGECAS"));
		mSenMod.setNote2DecisioneCassazione(getRequestStringParameter("NUMREGECAS"));
		// ---------------------------------------------------------------------------

		String lPage = "";

		if (mModifica)
			lPage = modifica();
		else
			lPage = inserimento();

		return lPage;
	}

	protected String inserimento() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inserimento Sentenza specializzata");

		// Chiama il controller
		SentenzaModel lSen = new SentenzaModel();
		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sentenza da inserire -> " + mSenMod);
			lSen = lSCtrl.ExInserisciSentenza(mSenMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sentenza inserita -> " + lSen);

		} catch (SIEPException siepEx) {
			if (siepEx.getErrorCode() == SIEPException.SENTENZA_PRESENTE_NEL_SISTEMA) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(siepEx.getMessage());

				String lPage2 = new String(IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.sentenza.action.ActRicercaSentenzaDuplicata");
				setRequestAttribute("sentenza_duplicata", mSenMod);
				return lPage2;
			} else {
				throw siepEx;
			}
		}

		// setta la risposta nella request
		setRequestAttribute("sentenza", lSen);

		// Prepara la "pagina" di destinazione
		String lPage = "";
		// Setta la risposta nella request
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&" + CAMPO_ID_SENTENZA + "="
				+ lSen.getIdSentenza().toString();

		return lPage;
	}

}