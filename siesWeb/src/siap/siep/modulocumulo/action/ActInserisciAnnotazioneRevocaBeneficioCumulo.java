package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un di 'Annotazione revoca Beneficio'
 * disposto su uno dei titoli cumulati
 * 
 * @author Intersistemi SpA
 *
 */
public class ActInserisciAnnotazioneRevocaBeneficioCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiDecretoOrdinanzaSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		// ==========================================================================
		//
		//
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaAnnotazioneRevocaBeneficioCumulo" + "&"
				+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
				+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO) + "&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		if ("I".equals(lModalita)) {
			siesLogger.debug("Sono in INSERIMENTO");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");
			// siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);

			lStatoEsecMod = lCtrlStatoEsec.ExInserisciStatoEsecTitoloCumulato(lStatoEsecMod);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioAnnotazioneRevocaBeneficioCumulo" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		} else if ("M".equals(lModalita)) {
			siesLogger.debug("Sono in MODIFICA");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
			// siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);

			lCtrlStatoEsec.ExModificaStatoEsecTitoloCumulato(lStatoEsecMod);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioAnnotazioneRevocaBeneficioCumulo" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		} else if ("C".equals(lModalita)) {
			siesLogger.debug("Sono in CANCELLAZIONE");

			BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter(
					CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(lIdStatoEsecuzione, null);

		}

		return lPage;
	}

	/**
	 * 
	 * @throws F3BException
	 */
	private StatoEsecTitoloCumulatoModel getDatiProvvedimento(String aTipoOper) throws F3BException {
		StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();

		lStaMod.setCodTipoEvento("01");
		lStaMod.setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO));

		if (this.isRequestChecked(CAMPO_CHECK_SOSPENSIONE)
				&& this.isRequestChecked(CAMPO_CHECK_NON_MENZIONE)) {
			lStaMod.setCodMotivo("0818");
			lStaMod.setFlagTipoSosp("SM");
		} else if (this.isRequestChecked(CAMPO_CHECK_SOSPENSIONE)) {
			lStaMod.setCodMotivo("0818");
			lStaMod.setFlagTipoSosp("S");
		} else if (this.isRequestChecked(CAMPO_CHECK_NON_MENZIONE)) {
			lStaMod.setCodMotivo("0822");
			lStaMod.setFlagTipoSosp("M");
		}

		lStaMod.setCodEsitoTenore("0006"); // REVOCA
		lStaMod.setCodEsito("-");

		ComuneModel lComu = getCodComuneByDescr(
				getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE));
		lStaMod.setCodLuogoEmittente(lComu.getCodComune());
		lStaMod.setCodUfficioEmittente(this.getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_TIPO_UFFICIO_EMITTENTE),
				getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE)));

		if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_SEZIONE_ALTRO)
				&& getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_SEZIONE_ALTRO) != null
				&& !getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_SEZIONE_ALTRO)
						.equals("")) {
			lStaMod.setSezioneAltro(
					getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_SEZIONE_ALTRO));
		}

		lStaMod.setDataEmissione(DateUtils.getDate(
				getRequestStringParameter(
						ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO),
				getRequestStringParameter(
						ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO),
				getRequestStringParameter(
						ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO)));

		lStaMod.setAnnoProcedimento(null);
		lStaMod.setProgrProcedimento(null);

		lStaMod.setAnnoProvvedimento(
				getRequestBigDecimalParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO));
		lStaMod.setProgrProvvedimento(
				getRequestBigDecimalParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO));

		if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_TEXT_AREA_MOTIVO)
				&& getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_TEXT_AREA_MOTIVO) != null
				&& !getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_TEXT_AREA_MOTIVO)
						.equals("")) {
			lStaMod.setNote(
					getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_TEXT_AREA_MOTIVO));
		}

		lStaMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lStaMod.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		lStaMod.setFlagStato("I");
		lStaMod.setMotivoModifica(null);

		if ("I".equals(aTipoOper)) {
			lStaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lStaMod.setDataInserimento(DateUtils.getSysDate());
			lStaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			// La Modifica cambia solo lo stato di "Estratto / Modificato".
			String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO);
			if ("E".equals(flagStato) || "M".equals(flagStato)) {
				lStaMod.setFlagStato("M");
			}

			lStaMod.setIdStatoEsecTitoloCumulato(getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));

			lStaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lStaMod.setDataAggiornamento(DateUtils.getSysDate());
			lStaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lStaMod;
	}

}