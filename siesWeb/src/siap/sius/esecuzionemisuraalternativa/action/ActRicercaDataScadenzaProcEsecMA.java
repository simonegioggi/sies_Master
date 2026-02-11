package siap.sius.esecuzionemisuraalternativa.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.scadenzario.action.ICostantiScadenzarioSius;
import siap.sius.statistiche.action.ICostantiStatistiche;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActRicercaDataScadenzaProcEsecMA - Classe Action per la ricerca Scadenzario monitoraggio misure alternative
 * espiate
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActRicercaDataScadenzaProcEsecMA extends ActionSius
		implements ICostantiStatistiche, ICostantiScadenzarioSius, ICostantiEsecuzioneMA {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: inizio");

		setLinkRitorno();
		String pagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		BigDecimal annoIniziale = null;
		BigDecimal numeroIniziale = null;
		BigDecimal annoFinale = null;
		BigDecimal numeroFinale = null;
		Date dataIscrizioneIniziale = null;
		Date dataIscrizioneFinale = null;
		RicercaProcedimentoModel rpm = null;
		IEsecuzioneMA iema = null;
		Collection<EMAFascGPModel> elenco;
		BigDecimal numeroElementiElenco = null;

		annoIniziale = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_INI);
		numeroIniziale = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_INI);
		annoFinale = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_ANNO_FINE);
		numeroFinale = getRequestBigDecimalParameter(ICostantiStatistiche.CAMPO_NUM_FINE);

		dataIscrizioneIniziale = getRequestDateParameter(
				ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO,
				ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO,
				ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO);

		dataIscrizioneFinale = getRequestDateParameter(ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE,
				ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE,
				ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE);

		rpm = new RicercaProcedimentoModel();

		rpm.setAnnoInizio(annoIniziale);
		rpm.setNumeroInizio(numeroIniziale);
		rpm.setAnnoFine(annoFinale);
		rpm.setNumeroFine(numeroFinale);
		rpm.setDataIscrizioneInizio(dataIscrizioneIniziale);
		rpm.setDataIscrizioneFine(dataIscrizioneFinale);
		rpm.setUtenteConnesso(getUtenteConnesso());

		String titolo = "Tutti";
		// tipo di ricerca selezionato
		String tipo = null;
		// intervallo date di ricerca
		Date data_ini = null;
		Date data_fine = null;

		tipo = getRequestStringParameter("tipo");
		if (tipo.equals("tutti")) {
			// Ricerca di tutti: scaduti e non
			titolo = "Tutti";
			data_ini = null;
			data_fine = null;
		} else if (tipo.equals("scaduti")) {
			// Ricerca delle sole scadenze già scadute
			titolo = "Scaduti";
			data_ini = null;
			data_fine = DateUtils.getSysDate();
		} else if (tipo.equals("oggi")) {
			// Ricerca delle scadenze che scadono oggi
			titolo = "In Scadenza Oggi";
			data_ini = DateUtils.getSysDate();
			data_fine = data_ini;
		} else if (tipo.equals("intervallo")) {
			// Ricerca delle scadenze non ancora scadute ma che scadranno in un intervallo
			titolo = "In Scadenza";
			data_ini = DateUtils.getSysDate();
			BigDecimal anniScadenza = Utils.NullToZero(getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA));
			BigDecimal mesiScadenza = Utils.NullToZero(getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA));
			BigDecimal giorniScadenza = Utils
					.NullToZero(getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA));
			// calcolo della data di fine
			data_fine = DateUtils.moveDateTo(data_ini, Calendar.YEAR, anniScadenza.intValue());
			data_fine = DateUtils.moveDateTo(data_fine, Calendar.MONTH, mesiScadenza.intValue());
			data_fine = DateUtils.moveDateTo(data_fine, Calendar.DAY_OF_YEAR, giorniScadenza.intValue());
		}
		rpm.setDataEmissioneInizio(data_ini); // data iscrizione iniziale
		rpm.setDataEmissioneFine(data_fine); // data iscrizione finale

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("ActRicercaDataScadenzaProcEsecMA: data_ini -> " + data_ini);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("ActRicercaDataScadenzaProcEsecMA: data_fine -> " + data_fine);
		iema = SIUSLookupRemote.getEsecuzioneMARemote();
		elenco = iema.ExRicercaDataScadenzaProcEsecMAPaginata(rpm, Integer.parseInt(pagina));

		// Paginazione
		if (isRequestParameterNullObj("CountRisultati"))
			numeroElementiElenco = iema.ExGetNumRicercaDataScadenzaProcEsecMA(rpm);
		else
			numeroElementiElenco = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", numeroElementiElenco);
		setRequestAttribute(IWebConstants.NUM_PAGE, pagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setSessionAttribute("rpm", rpm);
		setRequestAttribute("elencoProcedimenti", elenco);
		setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		setRequestAttribute("titolo", titolo);
		setRequestAttribute("anni", getRequestStringParameter(CAMPO_ANNI_SCADENZA));
		setRequestAttribute("mesi", getRequestStringParameter(CAMPO_MESI_SCADENZA));
		setRequestAttribute("giorni", getRequestStringParameter(CAMPO_GIORNI_SCADENZA));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: inizio");

		// pagina di ritorno
		return PG_RICERCA_DATA_SCADENZA_PROC_ESEC_MA;
	}

}