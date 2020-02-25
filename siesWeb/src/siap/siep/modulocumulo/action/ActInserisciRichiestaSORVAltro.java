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
 * Action per l'Inserimento, Modifica, Cancellazione di una Richiesta al Magistrato di SORVEGLIANZA - Tipo
 * Richiesta = Altro (cod = 014) - (Gestione Cumulo)
 *
 * @author Intersistemi Italia S.p.A.
 */

public class ActInserisciRichiestaSORVAltro extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGEAltro....");

		super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		RichiestePmInCumuloModel lRichModel = new RichiestePmInCumuloModel();

		if (!"C".equals(lModalita)) {
			lRichModel.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA)); // dominio
																									// TIPO_RICHIESTA_CUMULO:
																									// 02 =
																									// Richiesta
																									// alla
																									// SORVEGLIANZA
			lRichModel.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE)); // dominio
																										// TIPO_ANNOTAZIONE:
																										// 014
																										// =
																										// Altre
																										// Richieste

			// Nella Form è indicata come Data_Richiesta
			lRichModel.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
					CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
			lRichModel.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

			lRichModel.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			// lRichModel.setTitIdTitoloCumulatoRef(getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));

			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
				lRichModel.setIstrIdIstruttoriaCumulo(getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		}

		if ("I".equals(lModalita)) {
			// Inserimento
			lRichModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRichModel.setDataInserimento(DateUtils.getSysDate());
			lRichModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			siesLogger.debug("INSERT - ho riempito il model lRichModel = " + lRichModel);
		} else if ("M".equals(lModalita)) {
			// Modifica
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
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_RichPMTitoloCum_SORV(lRichModel);
		} else if ("M".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExModificaRichiestePmInCumuloERichPMTitoloCum(lRichModel);
		} else if ("C".equals(lModalita)) {
			// Cancellazione
			BigDecimal lIdRic = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(lIdRic);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		if (!"C".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVAltro";
			lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "="
					+ lRicRetMod.getIdRichiestePmInCumulo().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
		}

		return lPage;

	} // Chiude Process_Request()

} // Chiude classe