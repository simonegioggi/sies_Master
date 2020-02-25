package siap.siep.jms.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;

public class ActRicercaEstesaSentenzaPerTrasferimento extends ActionSiap implements ICostantiSiepJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		SentenzaModel lSmod = new SentenzaModel();

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_NUMERO_SENTENZA))
			lSmod.setNumeroSentenza(getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_SENTENZA));

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ANNO_SENTENZA))
			lSmod.setAnnoSentenza(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_SENTENZA));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("valore--->" + getRequestStringParameter("valore"));
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE)) {
			if (getRequestStringParameter("valore").equals("gip")) {
				lSmod.setNumeroRegeGip(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("valore--->" + lSmod.getNumeroRegeGip());
			}
			if (getRequestStringParameter("valore").equals("dib")) {
				lSmod.setNumeroRegeDib(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("cas")) {
				lSmod.setNumeroRegeCas(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("cap")) {
				lSmod.setNumeroRegeCap(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("casap")) {
				lSmod.setNumeroRegeCasap(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("valore").equals("gup")) {
				lSmod.setNumeroRegeGup(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("capsm")) {
				lSmod.setNumeroRegeCapsm(
						getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE));
			}
		}

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE)) {
			if (getRequestStringParameter("valore").equals("gip")) {
				lSmod.setAnnoRegeGip(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("dib")) {
				lSmod.setAnnoRegeDib(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("cas")) {
				lSmod.setAnnoRegeCas(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("cap")) {
				lSmod.setAnnoRegeCap(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("casap")) {
				lSmod.setAnnoRegeCasap(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
			// MEV_66: aggiunte quattro nuove proprietà
			if (getRequestStringParameter("valore").equals("gup")) {
				lSmod.setAnnoRegeGup(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
			if (getRequestStringParameter("valore").equals("capsm")) {
				lSmod.setAnnoRegeCapsm(
						getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE));
			}
		}
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_NUMERO_REGE_PM))
			lSmod.setNumeroRegePm(getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_REGE_PM));

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ANNO_REGE_PM))
			lSmod.setAnnoRegePm(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_REGE_PM));

		String lTipoUff = getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO);
		String lSedeUff = getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO);

		String lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);
		// lFasMod.setChiaveUfficio(lCodiceUfficio);

		UfficioModel lUfficioDestinatario = getUfficioByCodUfficio(lCodiceUfficio);
		UfficioModel lBDIDestinataria = getUfficioByCodUfficio(lUfficioDestinatario.getCodDistretto());

		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + lBDIMittente);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI Destinatario = " + lBDIDestinataria);

		// ******** Esegue tutta una serie di operazioni sul DB locale **********************

		// ----//GDV temporaneo levato
		// IRicercaJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
		// ----//GDV temporaneo levato

		/*
		 * RicercaJMSController lCtrlMess = new RicercaJMSController(); MessaggioModel lMessage =
		 * lCtrlMess.ExSpedisciRichiestaRicerca(lFasMod);
		 *
		 * lMessage.setDescrBdiDestinataria(lBDIDestinataria.getDescrComune());
		 * lMessage.setCodBdiDestinataria(lBDIDestinataria.getCodUfficio());
		 * lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
		 * lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
		 * lMessage.setCodUfficioDestinatario(lCodiceUfficio);
		 * lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		 * lMessage.setCodTipoMessaggio(RICHIESTA_RICERCA);
		 * lMessage.setCodTipoOperazione(RICERCA_FASCICOLO_PER_TRASFERIMENTO);
		 * lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
		 * lMessage.setDataInvio(DateUtils.getSysDate()); lMessage.setChiaveAnnoSiep(lFasMod.getChiaveAnno());
		 * lMessage.setChiaveProgrSiep(lFasMod.getChiaveProgr());
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Richiesta di ricerca Procedimento sottomessa al Sistema!");

		// SIAPSender lSender = new SIAPSender();
		// lSender.send(lMessage);

		// setRequestAttribute("IDEvento", lEveId.toString());
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Richiesta di ricerca Procedimento sottomessa al
		// Sistema!");
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Funzionalità in fase di implementazione");
		return IWebConstants.PG_MESSAGE;
	}

}