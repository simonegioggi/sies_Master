package siap.siep.misurasicurezza.action;

/**
* <p>Title: ActInserisciAnnotazioneDecisioneGiudiceCassazione </p>
* <p>Description: Classe Action per la load iscrizione Annotazione dei provvedimenti	  </p>
* <p>		del Giudice su faascicoli di Misure di Sicurezza Provvisorie o Fuori sentenza </p>
* @version 8.2
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciAnnotazioneDecisioneGiudiceCassazione extends ActionSiap
		implements ICostantiMisuraSicurezza, ICostantiDecretoOrdinanzaSiep {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		// Date dataOd = DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy");

		// String lTipoProvvedimento = getRequestStringParameter(
		// ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO);
		// Controllo sull'esistenza dell'ufficio per quel comune
		String lCodiceUffEmittente = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE));
		ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE)));
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- ActInserisciAnnotazioneDecisioneGiudiceCassazione - ComuneModel =
		// "+lComMod);

		// ---------------------------------------------------
		// EVENTO 1 - PROVVEDIMENTO di ANNOTAZIONE DECISIONE SIEP
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");

		// lEveMod.setCodMotivo(getRequestStringParameter(ICostantiProvvedimentoGenerico.CAMPO_OGGETTO));
		lEveMod.setCodMotivo(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE));

		lEveMod.setCodEsito(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO)); // ESITO
																										// TENORE
																										// ???
																										// (controllare)

		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setDataRicezioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI));
		// Ufficio Emittente Provvedimento di Annotazione = ufficio inserimento dell'evento
		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");

		// lEveMod.setCodMagistrato(calcolaMagistrato() );
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		if (!isRequestParameterNullObj(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO)
				&& getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO) != null
				&& !getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO)
						.equals("")) {
			lEveMod.setAnnoProtocollo(
					getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO));
			lEveMod.setProgrProtocollo(
					getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO));
		} else
			lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// -------------------------------------------
		// EVENTO 2 - ORDINANZA O DECRETO DEL RIESAME O CASSAZIONE (faccio la set solo per quei valori che
		// devono cambiare rispetto alìEVENTO 1)
		EventoModel lOrdMod = new EventoModel(lEveMod);

		lOrdMod.setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO));
		lOrdMod.setDataEmissione(
				getRequestDateParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO,
						ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO,
						ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO));
		lOrdMod.setDataRicezioneAtti(null);

		// Ufficio Emittente Ordinanza/ecreto
		lOrdMod.setCodUfficioEmittente(lCodiceUffEmittente);
		lOrdMod.setCodLuogoEmittente(lComMod.getCodComune());

		// ------------------------------------------------------------------
		// DECRETO_ORDINANZA_SIEP
		DecretoOrdinanzaSiepModel lDecOrdMod = new DecretoOrdinanzaSiepModel();

		if (!isRequestParameterNullObj(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO)
				&& getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO) != null
				&& !getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO)
						.equals("")) {
			lDecOrdMod.setAnnoProvvedimento(
					getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO));
			lDecOrdMod.setNumProvvedimento(
					getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO));
		}

		// ANNO, NUMERO E TIPO REG. GEN.
		if (!isRequestParameterNullObj(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN)
				&& getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN) != null
				&& !getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN).toString()
						.equals("")) {
			lDecOrdMod.setAnnoRegGen(
					getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN));
			lDecOrdMod.setNumeroRegGen(
					getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN));
			lDecOrdMod.setTipoRegGen(
					getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_TIPO_REG_GEN));
		}

		// ANNO E NUMERO R.G.N.R. - 11/2015 ---> Eliminati su segnalazione di M.T.
		// if(!isRequestParameterNullObj(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO) &&
		// getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO) != null &&
		// !getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO).equals("") )
		// {
		// lDecOrdMod.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO));
		// lDecOrdMod.setNumRegistro(getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO));
		// }

		lDecOrdMod.setCodTipoRegistroOrdinanza("-");
		lDecOrdMod.setCodContenutoDecreto("-");

		lDecOrdMod.setCodLuogoEmittente(lComMod.getCodComune());
		// lDecOrdMod.setCodTipoAutoritaEmittente(lCodiceUffEmittente);
		lDecOrdMod.setCodTipoAutoritaEmittente(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		// Higk_Value di Motivo_Provvedimento / Oggetto_Procedimento (MS01/MS02/MS03/MS04)
		lDecOrdMod.setCodOggettoDecisione(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO));

		// Motivo_Provvedimento / Oggetto_Decisione
		lDecOrdMod.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE));
		// Tipo_Provvedimento (Ordinnza/De creto/Sentenza)
		lDecOrdMod.setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO));
		// Esito_Tenore / Esito_Provvedimento
		lDecOrdMod.setCodEsito(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO));

		lDecOrdMod.setDataEmissioneProvvedimento(
				getRequestDateParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO,
						ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO,
						ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO));
		lDecOrdMod.setDataRicezioneProvvedimento(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI,
						ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI,
						ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI));
		lDecOrdMod.setDataInserimento(DateUtils.getSysDate());
		lDecOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
		lDecOrdMod.setCodUfficioInserimento(lCodiceUfficio);

		lDecOrdMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		if (getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NOTE).equals("")) {
			lDecOrdMod.setNote(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_NOTE));
		}

		// MISURE SICUREZZA
		MisuraSicurezzaModel lMisModNuova = null;
		lMisModNuova = getMisura(lIdFascicolo);

		// ------------------------------------------------------------------

		// INSERIMENTO
		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		EventoModel lEventoMod = lCtrl.ExInserisciProvvedimentoDecisioneCassazioneRiesame(lEveMod, lOrdMod,
				lDecOrdMod, lMisModNuova);

		String lPage = null;
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioAnnotazioneDecisioneGiudiceCassazione";
		lPage += "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEventoMod.getIdEvento();

		return lPage;

	} // Chiude process()

	protected MisuraSicurezzaModel getMisura(BigDecimal lIdFascicolo) throws Exception {
		MisuraSicurezzaModel lMisModNuova = new MisuraSicurezzaModel();

		if (!isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_COD_NATURA)
				&& getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_NATURA) != null
				&& !getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_COD_NATURA).equals("-")) {
			lMisModNuova.setCodNatura(getRequestStringParameter(CAMPO_COD_NATURA));
			lMisModNuova.setCodTipo(getRequestStringParameter(CAMPO_COD_TIPO));
			lMisModNuova.setNumAnni(getRequestBigDecimalParameter(CAMPO_NUM_ANNI));
			lMisModNuova.setNumMesi(getRequestBigDecimalParameter(CAMPO_NUM_MESI));
			lMisModNuova.setNumGiorni(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI));
		}

		if (!isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA)
				&& getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA) != null
				&& !getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA)
						.equals("")) {
			lMisModNuova.setLuogoEsecuzioneMisura(
					getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA));
		}

		lMisModNuova.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMisModNuova.setDataInserimento(DateUtils.getSysDate());
		lMisModNuova.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lMisModNuova.setFasSieIdFascicoloSiep(lIdFascicolo);

		return lMisModNuova;
	}

} // CHIUDE CLASSE ActIscrizioneProcApplicazioneMisuraProvvisoria()