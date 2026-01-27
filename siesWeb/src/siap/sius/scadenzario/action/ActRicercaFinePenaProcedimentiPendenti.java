package siap.sius.scadenzario.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActRicercaFinePenaProcedimentiPendenti - Classe Action per la Ricerca Fine Pena Procedimenti Pendenti di
 * Scadenzario SIUS
 *
 * @author sgioggi
 * @since MEV_2026-1
 * @version 1.0
 */
public class ActRicercaFinePenaProcedimentiPendenti extends ActionSiap implements ICostantiScadenzarioSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// navigazione
		setLinkRitorno();

		// paginazione
		String pagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			pagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Criterio di ricerca
		String cdr = new String();
		String cdrRet = new String();
		// tipo di ricerca selezionato
		String tipo = null;
		// riferimento selezionato
		String riferimento = null;
		// intervallo date iscrizione procedimenti
		Date dii = null;
		Date dif = null;
		dii = getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE_INIZIALE,
				CAMPO_MESE_DATA_ISCRIZIONE_INIZIALE, CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE);
		dif = getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE_FINALE, CAMPO_MESE_DATA_ISCRIZIONE_FINALE,
				CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE);
		// intervallo estremi procedimenti
		BigDecimal ai = null;
		BigDecimal ni = null;
		BigDecimal af = null;
		BigDecimal nf = null;
		ai = getRequestBigDecimalParameter(CAMPO_ANNO_INIZIALE);
		ni = getRequestBigDecimalParameter(CAMPO_NUM_INIZIALE);
		af = getRequestBigDecimalParameter(CAMPO_ANNO_FINALE);
		nf = getRequestBigDecimalParameter(CAMPO_NUM_FINALE);
		tipo = getRequestStringParameter("tipo");
		riferimento = getRequestStringParameter("rife");
		// data scadenza iniziale e finale
		Date dsi = null, dsf = null;
		if (tipo.equals("tutti")) { // Ricerca di tutti: scaduti e non
			cdr = "Tutti";
		} else if (tipo.equals("scaduti")) { // Ricerca delle sole scadenze già scadute
			cdr = "Scaduti";
			dsi = null;
			dsf = DateUtils.getSysDate();
		} else if (tipo.equals("oggi")) { // Ricerca delle scadenze che scadono oggi
			cdr = "In Scadenza Oggi";
			dsi = DateUtils.getSysDate();
			dsf = dsi;
		} else if (tipo.equals("intervallo")) {
			// Ricerca delle scadenze non ancora scadute ma che scadranno in un intervallo
			dsi = DateUtils.getSysDate();
			BigDecimal anni = NullToZero(getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA));
			BigDecimal mesi = NullToZero(getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA));
			BigDecimal giorni = NullToZero(getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA));
			// calcolo della data di fine
			dsf = DateUtils.moveDateTo(dsi, Calendar.YEAR, anni.intValue());
			dsf = DateUtils.moveDateTo(dsf, Calendar.MONTH, mesi.intValue());
			dsf = DateUtils.moveDateTo(dsf, Calendar.DAY_OF_YEAR, giorni.intValue());
			cdr = "In Scadenza entro: Anni " + anni + " Mesi " + mesi + " Giorni " + giorni;
		}
		IScadenzarioSius iss = SIUSLookupRemote.getScadenzarioRemote();
		String codUfficio = getUfficioUtenteConnesso().getCodUfficio();
		Vector v = iss.ExRicercaFinePenaProcedimentiPendentiPaginata(riferimento, ai, ni, af, nf, dii, dif,
				dsi, dsf, codUfficio, Integer.parseInt(pagina));
		setRequestAttribute("scadenzari", v);

		// Paginazione
		BigDecimal records = null;
		if (isRequestParameterNullObj("CountRisultati"))
			records = iss.ExGetNumRicercaFinePenaProcedimentiPendenti(riferimento, ai, ni, af, nf, dii, dif,
					dsi, dsf, codUfficio);
		else
			records = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", records);
		setRequestAttribute(IWebConstants.NUM_PAGE, pagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		final String cdr1 = "Intervallo Estremi Procedimenti: ";
		final String cdr2 = "Intervallo Date Iscrizione: ";
		final String cdr3 = "Criterio di Ricerca: ";
		final String cdr4 = "Con Riferimento alla: ";
		if (!Utils.isNullObj(ai))
			cdrRet += cdr1 + "da " + ai + "/" + ni + " a " + af + "/" + nf;
		if (!Utils.isNullObj(dii)) {
			if (!cdrRet.isEmpty())
				cdrRet += "; ";
			cdrRet += cdr2 + "da " + DateUtils.getDateToString(dii, "dd/MM/yyyy") + " a "
					+ DateUtils.getDateToString(dif, "dd/MM/yyyy");
		}
		cdrRet += "; " + cdr3 + cdr;
		String rife = ("reale".equals(riferimento)) ? "Data Fine Pena Reale" : "Data Fine Pena Virtuale";
		cdrRet += "; " + cdr4 + rife;
		setRequestAttribute("intestazione", cdrRet);

		// gestione dati in sessione
		List<Object> l = new ArrayList<Object>();
		l.add(riferimento);
		l.add(ai);
		l.add(ni);
		l.add(af);
		l.add(nf);
		l.add(dii);
		l.add(dif);
		l.add(dsi);
		l.add(dsf);
		l.add(codUfficio);
		l.add(cdrRet);
		setSessionAttribute("ricercaFinePenaProcedimentiPendenti", l);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		// pagina di ritorno
		return PG_RICERCAFINEPENAPROCEDIMENTIPENDENTI;
	}

	private BigDecimal NullToZero(BigDecimal aValue) {

		if (aValue == null)
			aValue = new BigDecimal(0);
		return aValue;
	}

}