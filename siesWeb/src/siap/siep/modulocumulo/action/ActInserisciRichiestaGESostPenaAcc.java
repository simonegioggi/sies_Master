package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta al GE di Sostituzione Pena Accessoria
 * Cumulo
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaGESostPenaAcc extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGESostPenaAcc....");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		RichiestePmInCumuloModel lRichModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento
			lRichModel = this.getDatiForm();

			lRichModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRichModel.setDataInserimento(DateUtils.getSysDate());
			lRichModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			siesLogger.debug("INSERT - ho riempito il model lRichModel = " + lRichModel);
		} else if ("M".equals(lModalita)) {
			// Modifica
			lRichModel = this.getDatiForm();

			lRichModel
					.setIdRichiestePmInCumulo(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			lRichModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRichModel.setDataAggiornamento(DateUtils.getSysDate());
			lRichModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			siesLogger.debug("MODIFICA lRichModel = " + lRichModel);
		}

		// Stringhe[] con gli 'Id' delle Pene Accessorie da Sostituire
		String[] lIdPASelezionate = null;

		if ("I".equals(lModalita)) {
			lIdPASelezionate = getRequestStringParameters(
					ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO);
		}

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablle di Collegamento tra RICHIESTE_PM_IN_CUMULO e le altre Entità
		// Correlate:
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			// Titolo di Riferimento
			BigDecimal lIdTitolo = getRequestBigDecimalParameter(CAMPO_ID_TITOLO_SELEZIONATO);
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_SostituzionePenaAccCum(lRichModel,
					lIdTitolo, lIdPASelezionate);
		} else if ("M".equals(lModalita)) {
			ICtrlRic.ExModificaRichiestePmInCumulo(lRichModel);
			lRicRetMod.setIdRichiestePmInCumulo(lRichModel.getIdRichiestePmInCumulo());
		} else if ("C".equals(lModalita)) {
			// Cancellazione
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(lId);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		if (!"C".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGESostPenaAcc";
			lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "="
					+ lRicRetMod.getIdRichiestePmInCumulo().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
		}

		return lPage;
	}

	/**
	 * Metodo che recupera i dati dalla form
	 */
	private RichiestePmInCumuloModel getDatiForm() throws F3BException {
		// siesLogger.debug("--XX-- >>>> Sono nel metodo getDatiForm() ");
		RichiestePmInCumuloModel lRicMod = new RichiestePmInCumuloModel();

		lRicMod.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA)); // dominio
																							// TIPO_RICHIESTA_CUMULO:
																							// 01 = Richiesta
																							// al G.E.
		lRicMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE)); // dominio
																								// TIPO_ANNOTAZIONE:
																								// 024 =
																								// Sostituzione
																								// Pena
																								// Accessoria
																								// / 025 =
																								// Sostituzione
																								// e Condono
																								// Pena
																								// Accessoria

		// Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		// ==========================================================================================================
		// Gestione Quantum ( solo durata P.A. in Anni, Mesi, Giorni)
		// ==========================================================================================================
		lRicMod.setCodTipoPenaAccessoria(getRequestStringParameter(CAMPO_COD_TIPO_PENA_ACCESSORIA));
		lRicMod.setCodTipoDurataPa(getRequestStringParameter(CAMPO_COD_TIPO_DURATA_PA));

		lRicMod.setNumAnniPa(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_PA));
		lRicMod.setNumMesiPa(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PA));
		lRicMod.setNumGiorniPa(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PA));

		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		// =======================================================================================================
		// Solo per CONDONO, riporto gli estremi Beneficio : Indulto/Amnistia + Dpr
		if (getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE).equals("025")) {
			lRicMod.setCodDpr(getRequestStringParameter(CAMPO_ESTREMI_CONDONO_DPR)); // Estr. Beneficio:
																						// Codice DPR
			lRicMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_ESTREMI_CONDONO_INDULTO)); // Estr.
																									// Beneficio:
																									// Codice
																									// Indulto/Amnistia
		}
		//

		lRicMod.setTitIdTitoloCumulato(getRequestBigDecimalParameter(CAMPO_ID_TITOLO_SELEZIONATO));
		// lRicMod.setTitIdTitoloCumulatoRef(getRequestBigDecimalParameter(CAMPO_ID_TITOLO_SELEZIONATO));

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe
