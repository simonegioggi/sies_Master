package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per l' Inserimento/Modifica della decisione del G.E. a fronte di una Richiesta del P.M. alla
 * SORV.
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciDecisioneDellaSORVCumulo extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		siesLogger.debug("--XX-- >>>> Start  ActInserisciDecisioneDellaSORVCumulo....");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("lModalita = " + lModalita);

		BigDecimal aIdRich = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);

		RichiestePmInCumuloModel lRicMod = null;
		ProvvedimentoGeSorvCumModel lProvvMod = new ProvvedimentoGeSorvCumModel();
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();

		if (!"C".equals(lModalita)) {
			// Inserimento/Modifica
			siesLogger.debug("Sono in Modalita diversa da C, = " + lModalita);

			lRicMod = ICtrlRic.ExRicercaRichiestePmInCumuloById(aIdRich);

			// Flag_Conforme : Conformità, Difformotà, Rigetto, Inammissibilità
			lProvvMod.setFlagConforme(getRequestStringParameter(CAMPO_FLAG_CONFORME));

			// Dati Provvedimento della Sorveglianza
			lProvvMod.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO));
			lProvvMod.setDataD(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_D,
					CAMPO_MESE_DATA_EMISSIONE_D, CAMPO_GIORNO_DATA_EMISSIONE_D));

			lProvvMod.setAnnoProvv(
					getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV));
			lProvvMod.setNumeroProvv(
					getRequestStringParameter(ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV));
			lProvvMod.setAnnoSIUS(
					getRequestBigDecimalParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO));
			lProvvMod.setNumeroSIUS(
					getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO));

			String lCodTipoUfficio = getRequestStringParameter(
					ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE);
			String lComune = getRequestStringParameter(
					ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE);

			String lCodUfficio = getUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lComune)
					.getCodUfficio();
			ComuneModel lComuneMod = getCodComuneByDescr(lComune);

			lProvvMod.setCodUfficioEmittente(lCodUfficio);
			lProvvMod.setCodLuogoEmittente(lComuneMod.getCodComune());

			if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_LA_REV_D)) {
				lProvvMod.setNumGiorniRevocaLaD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV_D));
				lProvvMod.setNumGiorniRevocaLsD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV_D));
				lProvvMod.setNumGiorniRevocaLiD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV_D));
			}
			if (!isRequestParameterNullObj(ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO))
				lProvvMod.setCodTipoMsD(
						getRequestStringParameter(ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO));

			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_MS))
				lProvvMod.setNumAnniMsD(
						getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_MS));
			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_MS))
				lProvvMod.setNumMesiMsD(
						getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_MS));
			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_MS))
				lProvvMod.setNumGiorniMsD(
						getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_MS));

			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D))
				lProvvMod.setNumAnniReclusioneD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_D));
			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D))
				lProvvMod.setNumMesiReclusioneD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_D));
			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D))
				lProvvMod.setNumGiorniReclusioneD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_D));

			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D))
				lProvvMod.setNumAnniArrestoD(
						getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_D));
			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D))
				lProvvMod.setNumMesiArrestoD(
						getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_D));
			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D))
				lProvvMod.setNumGiorniArrestoD(getRequestBigDecimalParameter(
						ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_D));

			if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_REVOCA))
				lProvvMod.setDataRevoca(getRequestDateParameter(CAMPO_ANNO_DATA_REVOCA,
						CAMPO_MESE_DATA_REVOCA, CAMPO_GIORNO_DATA_REVOCA));

			lProvvMod.setMotivazioniD(
					getRequestStringParameter(ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D));

			lProvvMod.setRicIdRichiestePmInCumulo(lRicMod.getIdRichiestePmInCumulo());

			if (!isRequestParameterNullObj(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO))
				lRicMod.setIstrIdIstruttoriaCumulo(
						getRequestBigDecimalParameter(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO));

			lRicMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRicMod.setDataAggiornamento(DateUtils.getSysDate());
			lRicMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			lRicMod.setDecisioneGeSorvCum(lProvvMod);

			siesLogger.debug("INSERIMENTO/MODIFICA lRichModel = " + lRicMod);
		}

		if ("C".equals(lModalita)) {
			// Cancellazione
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(aIdRich);
		} else {
			// Inserimento / Modifica
			ICtrlRic.ExModificaRichiestaEProvvPmInCumulo(lRicMod, null, null);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		if (!"C".equals(lModalita)) {
			if (lRicMod.getCodTipoAnnotazione().trim().compareTo("020") == 0)
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaLA";
			if (lRicMod.getCodTipoAnnotazione().trim().compareTo("022") == 0)
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVUnificaMS";
			if (lRicMod.getCodTipoAnnotazione().trim().compareTo("014") == 0)
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVAltro";
			if (lRicMod.getCodTipoAnnotazione().trim().compareTo("031") == 0)
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaMA";
			lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "=" + aIdRich.toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
		}

		return lPage;
	}

} // Chiude classe
