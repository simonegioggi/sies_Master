package siap.siep.notifica.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActUploadRich8Bis
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActUploadRich8Bis extends ActionSiap implements ICostantiNotifica {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		RinnovoModel lModel = new RinnovoModel();
		RinnovoModel lModelDue = new RinnovoModel();

		BigDecimal IdRinnovoUno = getRequestBigDecimalParameter("idPrimoRinnovo");
		BigDecimal IdRinnovoDue = getRequestBigDecimalParameter("idSecondoRinnovo");

		Vector lRinnovi = new Vector();

		// lModel.setIdRinnovo(getRequestBigDecimalParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO));

		InputStream lInput = getFile(ICostantiRinnovo.CAMPO_DOC_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
			lModelDue.setDocBlobIn(lSt);

		}

		lModel.setIdRinnovo(IdRinnovoUno);

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		lRinnovi.add(lModel);

		if (IdRinnovoDue != null) {

			lModelDue.setIdRinnovo(IdRinnovoDue);
			lModelDue.setDataAggiornamento(DateUtils.getSysDate());
			lModelDue.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lModelDue.setCodOperatoreAggiornamento(getCodUtenteConnesso());

			lRinnovi.add(lModelDue);
		}

		IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
		if (isRequestChecked(ICostantiRinnovo.CAMPO_VALIDA)) {
			lModel.setFlagDocumentoRegistrato("S");
			lModelDue.setFlagDocumentoRegistrato("S");

			lCtrl.ExUpdateValidaRich8Bis(lFascMod, lRinnovi);
		} else {
			lModel.setFlagDocumentoRegistrato("N");
			lModelDue.setFlagDocumentoRegistrato("N");

			lCtrl.ExUpdateDocument(lModel);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
			setRequestAttribute("idRinnovi", lRinnovi);

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(CAMPO_AZIONE_DETTAGLIO) + "&idRinnnovoUno="
					+ IdRinnovoUno + "&idRinnnovoDue=" + IdRinnovoDue);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}

}