package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'Emissione (invio logico) di una richiesta già effettuata del PM alla SORV.
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActEmettiRichiesteDelPMallaSORV extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActEmettiRichiesteDelPMallaSORV");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("lModalita = " + lModalita);

		/*
		 * String lStato="I"; if(lModalita.equals("M")) { // Cambia stato solo se il dato NON è Iscritto
		 * manualmente if(getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("E")
		 * || getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("M") ) { lStato
		 * = "M"; } }
		 */
		RichiesteInviateCumModel lRicInviate = new RichiesteInviateCumModel();

		if (!"C".equals(lModalita)) {
			lRicInviate.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
					CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
			lRicInviate.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
					CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));

			lRicInviate.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lRicInviate.setContenuto(getRequestStringParameter(CAMPO_CONTENUTO));

			String lCodTipoUfficio = getRequestStringParameter(CAMPO_UFFICIO_DEST);
			String lDescrComune = getRequestStringParameter(CAMPO_SEDE_UFFICIO_DEST);

			String lCodUfficio = getUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComune)
					.getCodUfficio();
			ComuneModel lComune = getCodComuneByDescr(lDescrComune);

			lRicInviate.setCodUfficioDest(lCodUfficio);
			lRicInviate.setCodLuogoDest(lComune.getCodComune());

			lRicInviate.setIstrIdIstruttoriaCumulo(getIdIstruttoria());
		}

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("Sono in INSERT");

			lRicInviate.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRicInviate.setDataInserimento(DateUtils.getSysDate());
			lRicInviate.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			siesLogger.debug("INSERT - ho riempito il model lRichModel = " + lRicInviate);
			// insert
		} else if ("M".equals(lModalita)) {
			// Modifica
			siesLogger.debug("Sono in MODIFICA");

			lRicInviate.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRicInviate.setDataAggiornamento(DateUtils.getSysDate());
			lRicInviate.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			siesLogger.debug("MODIFICA lRichModel = " + lRicInviate);

		}

		// =================================================================================================
		// Inserimento RICHIESTA_INVIATA e Collegamento con le varie RICHIESTE_PM_IN_CUMULO selezionate
		// ==================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiesteInviateCumModel lRicRetMod = new RichiesteInviateCumModel();

		String[] lIdRichiesteSelezionate = null;
		if ("I".equals(lModalita)) {
			// Stringhe[] con gli 'Id' delle RICHIESTE_PM_IN_CUMULO selezionate
			lIdRichiesteSelezionate = getRequestStringParameters(CAMPO_ID_RICHIESTE_SELEZIONATE);

			// Inserisce un Record RICHIESTE_INVIATE_CUM e procede ad una UPDATE per Ogni
			// RICHIESTA_PM_IN_CUMULO collegata
			lRicRetMod = ICtrlRic.ExInserisciRichiesteInviateCumulo(lRicInviate, lIdRichiesteSelezionate);
		}
		if ("C".equals(lModalita)) {
			siesLogger.debug(
					">>>>>> SE MODALITA = C devo vedere questo messaggio !!! >>>>>>>>>>>>  lModalita = "
							+ lModalita);
			// Cancellazione
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_INVIATA_CUM);
			// delete
			ICtrlRic.ExCancellaRichiesteInviateCumuloFull(lId, getCodUfficioUtenteConnesso(),
					getCodUtenteConnesso());
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		if ("I".equals(lModalita) || "M".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaDelPMallaSORVEmessa";
			lPage += "&" + CAMPO_ID_RICHIESTA_INVIATA_CUM + "="
					+ lRicRetMod.getIdRichiesteInviateCum().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
		}

		return lPage;
	}

}