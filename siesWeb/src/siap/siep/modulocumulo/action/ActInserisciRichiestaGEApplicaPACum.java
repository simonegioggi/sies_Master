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
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta al GE di Applicazione Pena Accessoria
 * Cumulo
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaGEApplicaPACum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGESApplicaPenaAcc....");

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

			siesLogger.debug("MODIFICA lRichModel per modifica = " + lRichModel);
		}

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablla di Collegamento tra RICHIESTE_PM_IN_CUMULO e TITOLO_CUMULATO
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_ApplicazionePenaAccCum(lRichModel);
		} else if ("M".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExModificaRichiestePmInCumuloERichPMTitoloCum(lRichModel);
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
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGEApplicaPenaAccCum";
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
																								// 029=Applicazione
																								// P.A. /
																								// 030=Applicazione
																								// e Condono
																								// P.A.

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
		// Solo per APPLICAZIONE e CONDONO, riporto gli estremi Beneficio : Indulto/Amnistia + Dpr
		if (getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE).equals("030")) {
			lRicMod.setCodDpr(getRequestStringParameter(CAMPO_ESTREMI_CONDONO_DPR)); // Estr. Beneficio:
																						// Codice DPR
			lRicMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_ESTREMI_CONDONO_INDULTO)); // Estr.
																									// Beneficio:
																									// Codice
																									// Indulto/Amnistia
		}
		//

		lRicMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		// lRicMod.setTitIdTitoloCumulatoRef(getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe
