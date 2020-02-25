package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaProvvedimentiIscrizioneMisuraFuoriSentenza
 * </p>
 * <p>
 * Description: Ricerca Provvedimenti per l'iscrizione del procedimento
 * </p>
 * </p> di Misura Sicurezza Emessa Fuori Sentenza </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia s.p.a.
 * </p>
 * 
 * @version 8.2
 */
public class ActRicercaProvvedimentiIscrizioneMisuraFuoriSentenza extends ActionSiap implements
		ICostantiDepositoOrdinanzaPc, ICostantiSentenza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();
		CalendarModel lCalMod = new CalendarModel();
		lCalMod.getDataInizio();

		Date Data_od = DateUtils.getSysDate();

		Date Data_Ini = null;
		Date Data_Fine = null;
		Boolean giaelaborati = false;

		if (getRequestStringParameter(ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO) != null
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO).equals("")) {
			Data_Ini = getRequestDateParameter(ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO,
					ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO,
					ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO);
		} else {
			String AAAA = DateUtils.getYearToString(Data_od);
			int LastAAAA = (Integer.parseInt(AAAA) - 1);
			String GG = DateUtils.getDayToString(Data_od);
			String MM = DateUtils.getMonthToString(Data_od);

			Data_Ini = DateUtils.getDate(LastAAAA, Integer.parseInt(MM), Integer.parseInt(GG));
		}

		if (getRequestStringParameter(ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO) != null
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO).equals("")) {
			Data_Fine = getRequestDateParameter(ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO,
					ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO,
					ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO);
		} else {
			Data_Fine = Data_od;
		}

		// procedimenti: anche gia elaborati
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ELABORATO)) {
			if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ELABORATO).toString()
					.equals("ELABORATI")) {
				giaelaborati = true;
			}
		}

		setRequestAttribute("NumerazioneManualeMisureProvvFS",
				getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));

		String codUfficio = getCodUfficioUtenteConnesso();
		// String distretto = getCodDistrettoUtenteConnesso();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		BigDecimal CountRisultati;
		IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		if (isRequestParameterNullObj("CountRisultati"))
			// MEV_39: aggiunto parametro di passaggio
			CountRisultati = lDepoCtrl.ExCountProvvedimentiSoggettoPerMisuraFuoriSentenza(Data_Ini,
					Data_Fine, giaelaborati, codUfficio);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);

		Vector DepoOrdinanze = null;
		try {
			// MEV_39: aggiunto parametro di passaggio
			DepoOrdinanze = lDepoCtrl.ExRicercaProvvedimentiSoggettoPerMisuraFuoriSentenza(Data_Ini,
					Data_Fine, giaelaborati, codUfficio, Integer.parseInt(lPagina));
		} catch (Exception Ex) {

			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, Ex.getMessage());
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi
					.setAction("siap.siep.misurasicurezza.action.ActLoadIscrizioneProcedimentoMisuraFuoriSentenza");
			lRedirigi
					.setParameter(
							ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS,
							getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("FascicoliOrdinaze", DepoOrdinanze);

		return ICostantiMisuraSicurezza.PG_RICERCA_PROVV_MIS_SIC_FUORI_SENTENZA; // restituisce la jsp di VIEW
	}

}