package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta alla SORV di Unificazione Misure di
 * Sicurezza.
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaSORVUnificaMS extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaSORVUnificaMS....");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("lModalita = " + lModalita);

		RichiestePmInCumuloModel lRichModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("Sono in INSERT");
			lRichModel = this.getDatiForm();

			lRichModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRichModel.setDataInserimento(DateUtils.getSysDate());
			lRichModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			siesLogger.debug("INSERT - ho riempito il model lRichModel = " + lRichModel);
			// insert
		} else if ("M".equals(lModalita)) {
			// Modifica
			siesLogger.debug("Sono in MODIFICA");

			lRichModel = this.getDatiForm();

			lRichModel
					.setIdRichiestePmInCumulo(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			lRichModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRichModel.setDataAggiornamento(DateUtils.getSysDate());
			lRichModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			// siesLogger.debug("MODIFICA lRichModel = "+lRichModel);
		}

		siesLogger.debug(
				"--XX-- >>>> Inizio getRequest delle String con gli ID delle entita oggetto della richiesta ");
		// -----------------------------------------------------------------------------------
		// Stringhe[] con gli 'Id' delle Entità selezionate che riguardano la Richiesta
		// -----------------------------------------------------------------------------------
		// TITOLO_CUMULATO_MODEL:
		String[] lIdTitoliSelezionati = null;
		String[] lIdMisureSelezionate = null;

		if ("I".equals(lModalita)) {
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);
			lIdMisureSelezionate = getRequestStringParameters(CAMPO_ID_MIS_SIC_CUM_SEL);
		}

		// ====================================================================================================================
		// Inserimento RICHIESTA e di RICHPM_STATO_ESEC_CUM (Relazione tra RICHIESTE_PM_IN_CUMULO e
		// STATO_ESEC_TITOLO_CUMULATO
		// ====================================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_SORV_UnificaMS(lRichModel,
					lIdTitoliSelezionati, lIdMisureSelezionate);
			siesLogger.debug("--XX-- >>>> Sono tornato da ExInserisciRichiestePmInCumulo_SORV_UnificaMS ");
		} else if ("M".equals(lModalita)) {
			ICtrlRic.ExModificaRichiestePmInCumulo(lRichModel);
			siesLogger.debug("--XX-- >>>> Sono tornato da Inserimento in RichiestePmInCumuloController ");
			lRicRetMod.setIdRichiestePmInCumulo(lRichModel.getIdRichiestePmInCumulo());
		} else if ("C".equals(lModalita)) {
			// Cancellazione
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			// delete
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(lId);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		siesLogger.debug("--XX-- >>>> IdRichiestePmInCumulo = " + lRicRetMod.getIdRichiestePmInCumulo());
		if (!"C".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVUnificaMS";
			lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "="
					+ lRicRetMod.getIdRichiestePmInCumulo().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
		}

		return lPage;
	}

	/**
	 * Metodo che recupera i dati dalla form
	 */
	private RichiestePmInCumuloModel getDatiForm() throws F3BException {
		RichiestePmInCumuloModel lRicMod = new RichiestePmInCumuloModel();

		// lRicMod.setCodTipoRichiesta ( getRequestStringParameter ( CAMPO_COD_TIPO_RICHIESTA) ); // dominio
		// TIPO_RICHIESTA_CUMULO: 02 = Richiesta alla SORV.
		// lRicMod.setCodTipoAnnotazione ( getRequestStringParameter ( CAMPO_COD_TIPO_ANNOTAZIONE) );
		lRicMod.setCodTipoRichiesta("02");
		lRicMod.setCodTipoAnnotazione("022");

		lRicMod.setFlagPiuMenoR("-");

		// La Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		if (!isRequestParameterNullObj(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe
