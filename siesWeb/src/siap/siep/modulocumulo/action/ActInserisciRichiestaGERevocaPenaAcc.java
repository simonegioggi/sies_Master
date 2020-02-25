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
 * Action per l'Inserimento, Modifica eCancellazione di una richiesta al GE di Revoca Pena Accessoria Cumulo
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaGERevocaPenaAcc extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGERevocaPenaAcc....");

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

		// Lista degli ID dei Titoli e relative P.A.
		String[] lIdTitoli_PA_Selezionati = null;

		if ("I".equals(lModalita)) {
			if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_PA_SELEZIONATI))
				lIdTitoli_PA_Selezionati = getRequestStringParameters(CAMPO_ID_TITOLO_PA_SELEZIONATI);
		}

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablle di Collegamento tra RICHIESTE_PM_IN_CUMULO e le altre Entità
		// Correlate:
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_RevocaPenaAccCum(lRichModel,
					lIdTitoli_PA_Selezionati);
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
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaAcc";
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
																								// 026 =
																								// Revoca PA -
																								// 027 =
																								// Depen. PA -
																								// 028 =
																								// Condono PA

		// Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		// ==========================================================================================================
		// solo per DEPENALIZZAZIONE, riporto la Norma Depenalizzante
		if (getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE).equals("027")) {
			lRicMod.setCodFonte(getRequestStringParameter(CAMPO_COD_FONTE));
			// siesLogger.debug("--XX-- Depe/ Illec fonte = "+getRequestStringParameter(CAMPO_COD_FONTE));
			lRicMod.setAnnoFonte(getRequestBigDecimalParameter(CAMPO_ANNO_FONTE));
			lRicMod.setNumeroFonte(getRequestStringParameter(CAMPO_NUMERO_FONTE));
			lRicMod.setArticolo(getRequestStringParameter(CAMPO_ARTICOLO));
			lRicMod.setCodSottonumerazione(getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
			// siesLogger.debug("--XX-- Depe/ Illec Sottonume BisTer =
			// "+getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
			lRicMod.setComma(getRequestStringParameter(CAMPO_COMMA));
			lRicMod.setLettera(getRequestStringParameter(CAMPO_LETTERA));
			lRicMod.setNumero(getRequestStringParameter(CAMPO_NUMERO));
		}

		// =======================================================================================================
		// Solo per CONDONO, riporto gli estremi Beneficio : Indulto/Amnistia + Dpr
		if (getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE).equals("028")) {
			lRicMod.setCodDpr(getRequestStringParameter(CAMPO_ESTREMI_CONDONO_DPR)); // Estr. Beneficio:
																						// Codice DPR
			lRicMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_ESTREMI_CONDONO_INDULTO)); // Estr.
																									// Beneficio:
																									// Codice
																									// Indulto/Amnistia
		}
		//

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe