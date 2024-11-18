package siap.siep.penaresidua.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.SIEPException;
import siap.siep.penaresidua.model.PenaResiduaModel;

public class PenaResiduaUtil {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Calcola il residuo pena a partire da una nuova data inizio. Utilizzata nel caso di interruzione
	 * specificando la data interruzione e fornendo in input il model della pena residua che si va ad
	 * interrompere. Il metodo calcola il residuo a partire dalla aNuovaDataInizio+1 alla data fine pena
	 * prevista. ????
	 *
	 * @param aNuovaDataInizio
	 *            - Data nuovo inizio pena
	 * @param aPenaResidua
	 *            - model con quantum e
	 * @param aEscludiDiesequo
	 *            - Indica se conteggiare o meno il giorno di inizio nel calcolo dei nuovi quantum
	 * @return PenaResiduaModel - con valorizzati i nuovi quantum e decorrenza e scadenza
	 * @throws Exception
	 */
	public static PenaResiduaModel calcolaPenaNuovaDataInizio(Date aNuovaDataInizio,
			PenaResiduaModel aPenaResidua, boolean aEscludiDiesequo) throws Exception {

		// Controlli sull'esistenza delle date
		if (aNuovaDataInizio == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La nuova data inizio pena non può essere vuota. Impossibile procedere.");

		if (aPenaResidua.getDataInizio() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La pena residua non ha la data inizio pena. Impossibile procedere.");

		if (aPenaResidua.getDataFine() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La pena residua non ha la data fine pena. Impossibile procedere.");

		PenaResiduaModel lPenaResidua = new PenaResiduaModel(aPenaResidua);

		CalendarUtil lCalUtil = new CalendarUtil();

		// RECLUSIONE
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumAnni(aPenaResidua.getNumAnniReclusione());
		lCalReclusione.setNumMesi(aPenaResidua.getNumMesiReclusione());
		lCalReclusione.setNumGiorni(aPenaResidua.getNumGiorniReclusione());

		if (!lCalUtil.isZero(lCalReclusione)) {
			lCalReclusione.setDataInizio(aPenaResidua.getDataInizio());

			if (aPenaResidua.getDataFineReclusione() != null)
				lCalReclusione.setDataFine(aPenaResidua.getDataFineReclusione());
			else
				lCalReclusione.setDataFine(aPenaResidua.getDataFine());
		}

		// ARRESTO
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumAnni(aPenaResidua.getNumAnniArresto());
		lCalArresto.setNumMesi(aPenaResidua.getNumMesiArresto());
		lCalArresto.setNumGiorni(aPenaResidua.getNumGiorniArresto());

		if (!lCalUtil.isZero(lCalArresto)) {
			Date lDataInizioArresto = null;
			if (aPenaResidua.getDataInizioArresto() == null)
				lDataInizioArresto = aPenaResidua.getDataInizio();
			else
				lDataInizioArresto = aPenaResidua.getDataInizioArresto();

			if (lDataInizioArresto == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"La pena residua non ha la data inizio arresto. Impossibile procedere.");

			lCalArresto.setDataInizio(lDataInizioArresto);
			lCalArresto.setDataFine(aPenaResidua.getDataFine());
		}

		CalendarModel lCalReclusioneNew = new CalendarModel();
		CalendarModel lCalArrestoNew = new CalendarModel();

		if (!lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) {
			if (aNuovaDataInizio.before(lCalReclusione.getDataFine())) {
				lCalReclusioneNew.setDataInizio(DateUtils.getDayAfter(aNuovaDataInizio));
				lCalReclusioneNew.setDataFine(lCalReclusione.getDataFine());

				lPenaResidua.setDataInizio(lCalReclusioneNew.getDataInizio());

				lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, aEscludiDiesequo);
				lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

				lPenaResidua.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
				lPenaResidua.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
				lPenaResidua.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

				// ARRESTO RIMANE INVARIATO
			} else if (aNuovaDataInizio.equals(lCalArresto.getDataInizio())) {
				// RECLUSIONE = 0
				lPenaResidua.setNumAnniReclusione(new BigDecimal(0));
				lPenaResidua.setNumMesiReclusione(new BigDecimal(0));
				lPenaResidua.setNumGiorniReclusione(new BigDecimal(0));

				lCalArrestoNew.setDataInizio(DateUtils.getDayAfter(aNuovaDataInizio));
				lCalArrestoNew.setDataFine(lCalArresto.getDataFine());

				lPenaResidua.setDataInizioArresto(lCalArrestoNew.getDataInizio());

				lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, aEscludiDiesequo);
				lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

				lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
				lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
				lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
			} else if (aNuovaDataInizio.after(lCalReclusione.getDataFine())
					|| aNuovaDataInizio.equals(lCalReclusione.getDataFine())) {
				// RECLUSIONE = 0
				lPenaResidua.setNumAnniReclusione(new BigDecimal(0));
				lPenaResidua.setNumMesiReclusione(new BigDecimal(0));
				lPenaResidua.setNumGiorniReclusione(new BigDecimal(0));

				lCalArrestoNew.setDataInizio(DateUtils.getDayAfter(aNuovaDataInizio));
				lCalArrestoNew.setDataFine(lCalArresto.getDataFine());

				lPenaResidua.setDataInizioArresto(lCalArrestoNew.getDataInizio());

				lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, aEscludiDiesequo);
				lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

				lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
				lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
				lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
			}
		} else if (!lCalUtil.isZero(lCalReclusione) && lCalUtil.isZero(lCalArresto)) {
			lCalReclusioneNew.setDataInizio(DateUtils.getDayAfter(aNuovaDataInizio));
			lCalReclusioneNew.setDataFine(lCalReclusione.getDataFine());

			lPenaResidua.setDataInizio(lCalReclusioneNew.getDataInizio());

			lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, aEscludiDiesequo);
			lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

			lPenaResidua.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
			lPenaResidua.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
			lPenaResidua.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

			// ARRESTO = 0
			lPenaResidua.setNumAnniArresto(new BigDecimal(0));
			lPenaResidua.setNumMesiArresto(new BigDecimal(0));
			lPenaResidua.setNumGiorniArresto(new BigDecimal(0));
		} else if (lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) {
			// RECLUSIONE = 0
			lPenaResidua.setNumAnniReclusione(new BigDecimal(0));
			lPenaResidua.setNumMesiReclusione(new BigDecimal(0));
			lPenaResidua.setNumGiorniReclusione(new BigDecimal(0));

			lCalArrestoNew.setDataInizio(DateUtils.getDayAfter(aNuovaDataInizio));
			lCalArrestoNew.setDataFine(lCalArresto.getDataFine());

			lPenaResidua.setDataInizioArresto(lCalArrestoNew.getDataInizio());

			lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, aEscludiDiesequo);
			lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

			lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
			lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
			lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));
		}

		lPenaResidua.setDataAggiornamento(null);
		lPenaResidua.setCodOperatoreAggiornamento(null);
		lPenaResidua.setDataAggiornamento(null);
		lPenaResidua.setDataFineIsolamentoDiurno(null);
		lPenaResidua.setDataInizioIsolamentoDiurno(null);
		lPenaResidua.setEveIdEvento(null);
		lPenaResidua.setFlagPenaSospesa(null);
		lPenaResidua.setFlagValidato("N");
		lPenaResidua.setMisAltIdMisuraAlternativa(null);

		return lPenaResidua;
	}

	/***************************************************************************
	 * Questo metodo ricalcola il periodo di PENA_RESIDUA utilizzando la nuova data fine. Vengono ricalcolati:
	 * gg, mm, aa, DataFineReclusione, DataInizioArresto, DataFine aggiungendo o sottraendo opportunamente i
	 * gg da Reclusione e Arresto<br>
	 *
	 * n.b. Vengono aggiornati preferibilmente se possibile i quantum dell'arresto e solo se non possibile
	 * vengono aggiornati i dati della reclusione
	 *
	 * n.b. devono essere not null NuovaDataFine e data_inizio e data_fine del model penaResidua
	 *
	 * @param Date
	 *            - nuova data fine
	 * @param PenaResiduaModel
	 *            - model con i dati di partenza
	 * @param boolean
	 *            - diesaquo
	 * @return PenaResiduaModel - model di partenza con i dati aggiornati
	 */
	public static PenaResiduaModel calcolaPenaNuovaDataFine(Date aNuovaDataFine,
			PenaResiduaModel aPenaResidua, boolean aEscludiDiesequo) throws Exception {

		// Controlli sull'esistenza delle date
		if (aNuovaDataFine == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La nuova data fine pena non può essere vuota. Impossibile procedere.");

		if (aPenaResidua.getDataInizio() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La pena residua non ha la data inizio pena. Impossibile procedere.");

		if (aPenaResidua.getDataFine() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La pena residua non ha la data fine pena. Impossibile procedere.");

		PenaResiduaModel lPenaResidua = new PenaResiduaModel(aPenaResidua);

		CalendarUtil lCalUtil = new CalendarUtil();

		// Prelevo i dati di Reclusione e Arresto dal PenaResiduaModel per
		// e li metto nei CalendarModel per poter essere manipolati opportunamente
		// =============
		// RECLUSIONE
		// =============
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumAnni(aPenaResidua.getNumAnniReclusione());
		lCalReclusione.setNumMesi(aPenaResidua.getNumMesiReclusione());
		lCalReclusione.setNumGiorni(aPenaResidua.getNumGiorniReclusione());

		if (!lCalUtil.isZero(lCalReclusione)) {
			lCalReclusione.setDataInizio(aPenaResidua.getDataInizio());

			if (aPenaResidua.getDataFineReclusione() != null)
				lCalReclusione.setDataFine(aPenaResidua.getDataFineReclusione());
			else
				lCalReclusione.setDataFine(aPenaResidua.getDataFine());
		}

		// ==========
		// ARRESTO
		// ==========
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumAnni(aPenaResidua.getNumAnniArresto());
		lCalArresto.setNumMesi(aPenaResidua.getNumMesiArresto());
		lCalArresto.setNumGiorni(aPenaResidua.getNumGiorniArresto());

		if (!lCalUtil.isZero(lCalArresto)) {
			Date lDataInizioArresto = null;
			if (aPenaResidua.getDataInizioArresto() == null)
				lDataInizioArresto = aPenaResidua.getDataInizio();
			else
				lDataInizioArresto = aPenaResidua.getDataInizioArresto();

			if (lDataInizioArresto == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"La pena residua non ha la data inizio arresto. Impossibile procedere.");

			lCalArresto.setDataInizio(lDataInizioArresto);
			lCalArresto.setDataFine(aPenaResidua.getDataFine());
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCalReclusione : " + lCalReclusione);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCalArresto : " + lCalArresto);

		// ========================================================================
		// Ricalcolo Reclusione e Arresto
		// ========================================================================
		CalendarModel lCalReclusioneNew = new CalendarModel();
		CalendarModel lCalArrestoNew = new CalendarModel();

		if (!lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lReclusione Arresto");

			if (aNuovaDataFine.after(lCalArresto.getDataInizio())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fine > Inizio Arresto");
				// RECLUSIONE RIMANE INVARIATO
				lCalReclusioneNew = new CalendarModel(lCalReclusione);

				// ARRESTO
				lCalArrestoNew.setDataInizio(lCalArresto.getDataInizio());
				lCalArrestoNew.setDataFine(aNuovaDataFine);

				lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, aEscludiDiesequo);
				lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCalArrestoNew : " + lCalArrestoNew);
			} else if (aNuovaDataFine.equals(lCalArresto.getDataInizio())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fine = Inizio Arresto");
				// RECLUSIONE RIMANE INVARIATO
				lCalReclusioneNew = new CalendarModel(lCalReclusione);

				// ARRESTO = 0
				lCalArrestoNew = new CalendarModel();
				// dovrebbe azzerare la data inizioArresto!!!!!
			} else if (aNuovaDataFine.before(lCalReclusione.getDataFine())
					|| aNuovaDataFine.equals(lCalReclusione.getDataFine())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fine <= Inizio Arresto");

				// ARRESTO = 0
				lCalArrestoNew = new CalendarModel();

				// RECLUSIONE
				lCalReclusioneNew.setDataInizio(aPenaResidua.getDataInizio());
				lCalReclusioneNew.setDataFine(aNuovaDataFine);

				lPenaResidua.setDataInizioArresto(null);
				lPenaResidua.setDataFineReclusione(aNuovaDataFine);

				lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, aEscludiDiesequo);
				lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);
			}
		} else if (!lCalUtil.isZero(lCalReclusione) && lCalUtil.isZero(lCalArresto)) {
			// Solo reclusione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Reclusione != 0");

			lCalReclusioneNew.setDataInizio(aPenaResidua.getDataInizio());
			lCalReclusioneNew.setDataFine(aNuovaDataFine);

			lPenaResidua.setDataFineReclusione(aNuovaDataFine);

			lCalReclusioneNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalReclusioneNew, aEscludiDiesequo);
			lCalReclusioneNew = lCalUtil.ricalcolaGAM(lCalReclusioneNew);

			// ARRESTO = 0
			lCalArrestoNew = new CalendarModel();
		} else if (lCalUtil.isZero(lCalReclusione) && !lCalUtil.isZero(lCalArresto)) {
			// Solo arresti
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Arresto != 0");

			// RECLUSIONE = 0
			lCalReclusioneNew = new CalendarModel();

			// ARRESTO
			lCalArrestoNew.setDataInizio(aPenaResidua.getDataInizio());
			lCalArrestoNew.setDataFine(aNuovaDataFine);

			lPenaResidua.setDataInizioArresto(aPenaResidua.getDataInizio());

			lCalArrestoNew = lCalUtil.CalcolaNumGiorniMesiAnni(lCalArrestoNew, aEscludiDiesequo);
			lCalArrestoNew = lCalUtil.ricalcolaGAM(lCalArrestoNew);
		}

		// Ricarico nella pena residua i quantum ricalcolati in base alla nuova
		// data fine pena
		// n.b. le nuove date (data fine reclusione, data inizio arresto) sono già
		// state caricate
		/**
		 * TODO Attenzione al caso di reclusione+arresto e nuovaDataFine=dataInizioArresto in quasto caso
		 * viene azzerato il quantum di Arresti, ma non viene cancellata la data inizio arresto che resta
		 * valorizzata
		 */
		lPenaResidua.setNumAnniReclusione(new BigDecimal(lCalReclusioneNew.getNumAnni()));
		lPenaResidua.setNumMesiReclusione(new BigDecimal(lCalReclusioneNew.getNumMesi()));
		lPenaResidua.setNumGiorniReclusione(new BigDecimal(lCalReclusioneNew.getNumGiorni()));

		lPenaResidua.setNumAnniArresto(new BigDecimal(lCalArrestoNew.getNumAnni()));
		lPenaResidua.setNumMesiArresto(new BigDecimal(lCalArrestoNew.getNumMesi()));
		lPenaResidua.setNumGiorniArresto(new BigDecimal(lCalArrestoNew.getNumGiorni()));

		lPenaResidua.setDataFine(aNuovaDataFine);

		lPenaResidua.setDataAggiornamento(null);
		lPenaResidua.setCodOperatoreAggiornamento(null);
		lPenaResidua.setDataAggiornamento(null);
		lPenaResidua.setDataFineIsolamentoDiurno(null);
		lPenaResidua.setDataInizioIsolamentoDiurno(null);
		lPenaResidua.setEveIdEvento(null);
		lPenaResidua.setFlagPenaSospesa(null);
		lPenaResidua.setFlagValidato("N");
		lPenaResidua.setMisAltIdMisuraAlternativa(null);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenaResidua : " + lPenaResidua);

		return lPenaResidua;
	}

}