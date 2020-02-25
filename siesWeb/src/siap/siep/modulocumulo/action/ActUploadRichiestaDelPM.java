package siap.siep.modulocumulo.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action di validazione della Richiesta al G.E., Emessa del PM (Gestione Cumulo - Richieste del PM)
 * 
 * @author Intersistemi Italia S.p.A.
 */
public class ActUploadRichiestaDelPM extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		siesLogger.debug("--XX-- START ------>>>>>>>>>  ActUpload ");
		// FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal aIdIstru = getRequestBigDecimalParameter(
				ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		// siesLogger.debug("--XX-- aId = "+aId);

		// ==========================================================================
		// Recupero la Richiesta da Validare
		// ==========================================================================
		RichiesteInviateCumModel lRichMod = new RichiesteInviateCumModel();
		lRichMod.setIdRichiesteInviateCum(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_INVIATA_CUM));

		// ==========================================================================
		// Scarico l'eventuale report
		// ==========================================================================
		InputStream lInput = getFile(CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			siesLogger.debug("--XX-- Bene, il CAMPO_BLOB è pieno ed è in lRichMod ");

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);

			lRichMod.setDocBlobIn(lSt);
		}

		// ==========================================================================
		// Imposto i dati dell'aggiornamento
		// ==========================================================================
		lRichMod.setDataAggiornamento(DateUtils.getSysDate());
		lRichMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lRichMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		// ==========================================================================
		// Validazione
		// ==========================================================================
		IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		if (isRequestChecked(CAMPO_VALIDA)) {
			lRichMod.setFlagDocValidato("S");
		} else {
			lRichMod.setFlagDocValidato("N");
		}

		lCtrlRic.ExUpdateValidaRichiesteInviateCumulo(lRichMod);

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTA_INVIATA_CUM + "="
					+ getRequestStringParameter(CAMPO_ID_RICHIESTA_INVIATA_CUM) + "&"
					+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + aIdIstru);

			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}