package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

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
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta al GE di Revoca Applicazione Beneficio
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaGERevocaBenefici extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGERevocaBenefici....");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("lModalita = " + lModalita);

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

		Vector<String> listaIdBen = new Vector<String>();
		BigDecimal lIdProvv = null;
		if (!"C".equals(lModalita)) {
			// Preparo la lista dei Benefici per cui è richiesta la Revoca (Max. 2 IdBenefici)
			if (!isRequestParameterNullObj(CAMPO_ID_STATOESEC_CUM_SEL)) {
				lIdProvv = getRequestBigDecimalParameter(CAMPO_ID_STATOESEC_CUM_SEL);
			} else {
				listaIdBen.add(getRequestStringParameter(CAMPO_ID_BENEFICIO_CUM_SEL_1));

				if (!isRequestParameterNullObj(CAMPO_ID_BENEFICIO_CUM_SEL_2)
						&& !getRequestStringParameter(CAMPO_ID_BENEFICIO_CUM_SEL_2).equals("")) {
					listaIdBen.add(getRequestStringParameter(CAMPO_ID_BENEFICIO_CUM_SEL_2));
				}
			}
		}

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablla di Collegamento tra RICHIESTE_PM_IN_CUMULO e le altre Entità
		// Correlate:
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_RevocaBenefici(lRichModel, listaIdBen,
					lIdProvv);
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
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaBenefici";
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
		siesLogger.debug("--XX-- >>>> Sono nel metodo getDatiForm() ");
		RichiestePmInCumuloModel lRicMod = new RichiestePmInCumuloModel();

		lRicMod.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA)); // dominio
																							// TIPO_RICHIESTA_CUMULO:
																							// 01 = Richiesta
																							// al G.E.
		lRicMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE));

		if (!isRequestParameterNullObj(ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO)
				&& !getRequestStringParameter(ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO).equals("")) {
			lRicMod.setCodMotivo(getRequestStringParameter(ICostantiRichiestePmInCumulo.CAMPO_COD_MOTIVO));
		}

		// 20/05/2019 MEV70 Gestione del check Anticipazione degli Effetti in caso di Revoca beneficio
		// Sospensione Condizionale/Non Menzione
		if (!isRequestParameterNullObj(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO)
				&& (getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO).equals("01")
						|| // Sospensione condizionale
						getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO)
								.equals("02")) // Non Menzione
		) {
    	if (isRequestChecked("FlagAnticipazione")) 
	      lRicMod.setFlagAppProvvisoria("A");	// Con Anticipazione
	    else 
	      lRicMod.setFlagAppProvvisoria("R"); // semplice richiesta
   }
	
		if (!isRequestParameterNullObj(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO)
				&& (getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO).equals("03")
						|| // Indulto (in sentenza)
						getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO)
								.equals("04")
						|| // Amnistai (in sentenza)
						getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO)
								.equals("002")
						|| // Indulto (con provvedimento)
						getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_COD_TIPO_BENEFICIO)
								.equals("003")) // Amnistia (con provvedimento)
		) {
			lRicMod.setFlagPiuMenoR(getRequestStringParameter(CAMPO_FLAG_PIU_MENO_R));

			// Reclusione e Multa
			lRicMod.setNumAnniReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE_R));
			lRicMod.setNumMesiReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE_R));
			lRicMod.setNumGiorniReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE_R));

			if ((getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT") != null
					&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT")).equals(""))
					|| (getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC") != null
							&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC")).equals(""))) {
				lRicMod.setImportoMultaR(
						new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT") + "."
								+ getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC")));
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
		}

		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		// Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		lRicMod.setTitIdTitoloCumulato(getRequestBigDecimalParameter(CAMPO_TIT_ID_TITOLO_CUMULATO)); // Id_Titolo
																										// associato
																										// al
																										// Beneficio
																										// da
																										// Revocare
		lRicMod.setTitIdTitoloCumulatoRef(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)); // Id_Titolo
																									// a cui è
																									// riferita
																									// la
																									// Revoca

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe