package siap.sius.scadenzario.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaScadenzario
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaScadenzarioSius extends ActionSiap implements ICostantiScadenzarioSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
		String titolo = new String();

		// STUB 25/03/2005
		// this.gestioneRitorno();

		// STUB 25/03/2005
		String aTipoScadenzario = getRequestStringParameter(CAMPO_COD_TIPO_SCADENZARIO);

		titolo = "Tutti";
		String tipo = null; // tipo di ricerca selezionato

		// intervallo date di ricerca
		Date data_ini = null;
		Date data_fine = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("-------- ActRicercaScadenzario: inizio");
		tipo = getRequestStringParameter("tipo");
		if (tipo.equals("tutti")) { // Ricerca di tutti: scaduti e non
			titolo = "Tutti";
			data_ini = null;
			data_fine = null;
		} else if (tipo.equals("scaduti")) { // Ricerca delle sole scadenze già scadute
			titolo = "Scaduti";
			data_ini = null;
			data_fine = DateUtils.getSysDate();

		} else if (tipo.equals("oggi")) { // Ricerca delle scadenze che scadono oggi
			titolo = "In Scadenza Oggi";
			data_ini = DateUtils.getSysDate();
			data_fine = data_ini;
		} else if (tipo.equals("intervallo")) { // Ricerca delle scadenze non ancora scadute ma che scadranno
												// in un intervallo
			titolo = "In Scadenza";
			data_ini = DateUtils.getSysDate();
			BigDecimal lAnni = NullToZero(getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA));
			BigDecimal lMesi = NullToZero(getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA));
			BigDecimal lGiorni = NullToZero(getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA));
			// calcolo della data di fine
			data_fine = DateUtils.moveDateTo(data_ini, Calendar.YEAR, lAnni.intValue());
			data_fine = DateUtils.moveDateTo(data_fine, Calendar.MONTH, lMesi.intValue());
			data_fine = DateUtils.moveDateTo(data_fine, Calendar.DAY_OF_YEAR, lGiorni.intValue());
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("-------- ActRicercaScadenzario: data_ini-> " + data_ini);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("-------- ActRicercaScadenzario: data_fine-> " + data_fine);
		IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote();
		Vector lVect = lCtrl.ExRicercaScadenzarioSius(aTipoScadenzario, data_ini, data_fine);

		setRequestAttribute("scadenzarii", lVect);

		setRequestAttribute("tipoScadenzario", getRequestStringParameter(CAMPO_COD_TIPO_SCADENZARIO));

		String lTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		IScadenzarioSius lScaCtrl = SIUSLookupRemote.getScadenzarioRemote();
		String aDescTipoScadenzario = DecodificheUtils
				.getDescbyCode(lScaCtrl.ExElencoTipiScadenzarioByTipoUfficio(lTipoUfficio), aTipoScadenzario);

		setRequestAttribute("descTipoScadenzario", aDescTipoScadenzario);
		setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		setRequestAttribute("titolo", titolo);
		setRequestAttribute("anni", getRequestStringParameter(CAMPO_ANNI_SCADENZA));
		setRequestAttribute("mesi", getRequestStringParameter(CAMPO_MESI_SCADENZA));
		setRequestAttribute("giorni", getRequestStringParameter(CAMPO_GIORNI_SCADENZA));
		return PG_RICERCASCADENZARIO;
	}

	private BigDecimal NullToZero(BigDecimal aValue) {

		if (aValue == null)
			aValue = new BigDecimal(0);
		return aValue;
	}

}