package siap.sius.statistiche.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: ActRicercaStatisticaMisureAlter678c1tercpp
 * Description: Classe Action per la ricerca statistica per procedimenti MA Art.678c1tercpp
 *
 * @version 1.0
 */
// MEV_9: aggiunta classe per le statistiche
public class ActRicercaStatisticaMisureAlter678c1tercpp extends ActionSius implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		setLinkRitorno();
		String pagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		int statoProcedimento = Integer
				.parseInt(ICostantiStatistiche.VALUE_RICERCA_ORD_NON_EMESSE_ATTI_AL_PRESIDENTE);
		BigDecimal annoIni = null;
		BigDecimal numIni = null;
		BigDecimal annoFine = null;
		BigDecimal numFine = null;
		Date dataDepositoIni = null;
		Date dataDepositoFine = null;
		RicercaProcedimentoModel rpm = null;
		IStatisticheSius iss = null;
		Collection<EveFasGepSogProvModel> elenco;
		BigDecimal records = null;

		statoProcedimento = getRequestIntParameter(ICostantiStatistiche.RADIO_RICERCA_STATISTICA_MA);
		annoIni = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_INI);
		numIni = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_INI);
		annoFine = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_FINE);
		numFine = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_FINE);

		dataDepositoIni = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI,
				ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI,
				ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI);

		dataDepositoFine = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE,
				ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE,
				ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE);

		rpm = new RicercaProcedimentoModel();

		rpm.setStatoProcedimento(statoProcedimento);
		rpm.setAnnoInizio(annoIni);
		rpm.setNumeroInizio(numIni);
		rpm.setAnnoFine(annoFine);
		rpm.setNumeroFine(numFine);
		rpm.setDataDepositoInizio(dataDepositoIni);
		rpm.setDataDepositoFine(dataDepositoFine);
		rpm.setUtenteConnesso(getUtenteConnesso());

		iss = SIUSLookupRemote.getStatisticheSiusRemote();
		elenco = iss.ExRicercaProcPerStatisticaMisureAlternative(rpm,
				Integer.parseInt(pagina));

		// Paginazione
		if (isRequestParameterNullObj("CountRisultati"))
			records = iss.ExGetNumRicercaProcPerStatisticaMisureAlternative(rpm);
		else
			records = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", records);
		setRequestAttribute(IWebConstants.NUM_PAGE, pagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setSessionAttribute("ricercaProcedimenti", rpm);
		setRequestAttribute("elencoProcedimenti", elenco);

		return PG_RICERCA_STATISTICA_MISURE_ALTER_678C1TERCPP;
	}

}