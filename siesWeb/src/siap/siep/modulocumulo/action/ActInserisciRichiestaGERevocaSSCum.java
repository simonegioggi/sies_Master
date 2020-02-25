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
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta al GE di Revoca Sanzione Sostitutiva
 * Cumulo
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaGERevocaSSCum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGERevocaSSCum....");

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

		// Stringhe[] con gli 'Id' dei Titoli coinvolti nella Richiesta
		String[] lIdTitoliSelezionati = null;
		String[] lIdSanzioniSelezionate = null;

		if ("I".equals(lModalita)) {
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);
			lIdSanzioniSelezionate = getRequestStringParameters(CAMPO_ID_SANZIONE_SOST_SELEZIONATA);
		}

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablle di Collegamento tra RICHIESTE_PM_IN_CUMULO e le altre Entità
		// Correlate:
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_RevocaSS(lRichModel, lIdTitoliSelezionati,
					lIdSanzioniSelezionate);
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
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaSSCum";
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
																								// 023 =
																								// Revoca
																								// Sanzioni
																								// Sostitutive

		// Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		// ==========================================================================================================
		// Gestione Quantum
		// ==========================================================================================================
		lRicMod.setFlagPiuMenoR(getRequestStringParameter(CAMPO_FLAG_PIU_MENO_R));

		// Reclusione e Multa
		lRicMod.setNumAnniReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE_R));
		lRicMod.setNumMesiReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE_R));
		lRicMod.setNumGiorniReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE_R));

		if ((getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC")).equals(""))) {
			lRicMod.setImportoMultaR(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT")
					+ "." + getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC")));
		}

		// Arresto e Ammenda
		lRicMod.setNumAnniArrestoR(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO_R));
		lRicMod.setNumMesiArrestoR(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO_R));
		lRicMod.setNumGiorniArrestoR(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO_R));

		if ((getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "DEC")).equals(""))) {
			lRicMod.setImportoAmmendaR(
					new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "INT") + "."
							+ getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "DEC")));
		}

		if (isRequestChecked(CAMPO_FLAG_APP_PROVVISORIA))
			lRicMod.setFlagAppProvvisoria("A"); // Con Anticipazione
		else
			lRicMod.setFlagAppProvvisoria("R"); // semplice richiesta

		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		// =======================================================================================================

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe