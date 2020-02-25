package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.CalcoloPenaController;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaScadenzarioFinePenaMisureSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario per
 * </p>
 * <p>
 * Fine Pena di procedimenti di Misure Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: IntersistemiItalia
 * </p>
 *
 * @version 8.3
 */
public class ActRicercaScadenzarioFinePenaMisureSicurezza extends ActionSiap implements ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		ScadenzarioModel lScaMod = new ScadenzarioModel();
		String titolo = new String();
		titolo = "Tutti";

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// MEV_39: è stato introdotto un nuovo codice_tipo_scadenzario=20
		// (scadenzario di inizio misura)
		// rimuovo quindi il set a scadenzario = 02
		// lScaMod.setCodTipoScadenzario("02");
		lScaMod.setCodTipoScadenzario("20");
		lScaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Consultazione Scadenzario Inizio Misura Sicurezza con criterio di
		// ricerca: SCADENZA OGGI
		if (getRequestStringParameter("tipo").equals("oggi")) {
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataFineScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza Oggi";
			lScaMod.setTipoRic("oggi");
		}

		// Consultazione Scadenzario Inizio Misura Sicurezza con criterio di
		// ricerca: IN SCADENZA (anche con gg, mm e yyyy)
		// MEV_39: estratti queste variabili per settarle come attributi della richiesta alla fine
		BigDecimal lAnni = null, lMesi = null, lGiorni = null;
		if (getRequestStringParameter("tipo").equals("sette")) {
			if (!isRequestParameterNullObj(CAMPO_ANNI_SCADENZA)
					&& getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA) != null)
				lAnni = getRequestBigDecimalParameter(CAMPO_ANNI_SCADENZA);
			if (!isRequestParameterNullObj(CAMPO_MESI_SCADENZA)
					&& getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA) != null)
				lMesi = getRequestBigDecimalParameter(CAMPO_MESI_SCADENZA);
			if (!isRequestParameterNullObj(CAMPO_GIORNI_SCADENZA)
					&& getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA) != null)
				lGiorni = getRequestBigDecimalParameter(CAMPO_GIORNI_SCADENZA);
			CalendarModel lCalMod = new CalendarModel();
			CalendarModel lDataInizio = new CalendarModel();
			lDataInizio.setNumAnni(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
			lDataInizio.setNumMesi(new BigDecimal(DateUtils.getMonthToString(DateUtils.getSysDate())));
			lDataInizio.setNumGiorni(new BigDecimal(DateUtils.getDayToString(DateUtils.getSysDate())));
			if (lAnni != null && lAnni.intValue() != 0) {
				lScaMod.setNumAnni(lAnni);
				lCalMod.setNumAnni(lScaMod.getNumAnni());
			}
			if (lGiorni != null && lGiorni.intValue() != 0) {
				lScaMod.setNumGiorni(lGiorni);
				lCalMod.setNumGiorni(lScaMod.getNumGiorni());
			}
			if (lMesi != null && lMesi.intValue() != 0) {
				lScaMod.setNumMesi(lMesi);
				lCalMod.setNumMesi(lScaMod.getNumMesi());
			}

			lScaMod.setTipoRic("sette");
			CalcoloPenaController lCalPen = new CalcoloPenaController();
			lScaMod.setDataFineScadenza(lCalPen.exCalcolaNuovaDataFine(lDataInizio, lCalMod, false));
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "In Scadenza";
		}

		// Consultazione Scadenzario Inizio Misura Sicurezza con criterio di
		// ricerca: SCADUTI
		if (getRequestStringParameter("tipo").equals("scaduto")) {
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));
			titolo = "Scaduti";
			lScaMod.setTipoRic("scaduto");
		}

		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			// 20191121 [SG]: aggiunto metodo
			// CountRisultati = lCtrl.ExGetCountScadenzariMisSic(lScaMod);
			CountRisultati = lCtrl.ExGetCountRicercaScadenzarioPagedMisSic(lScaMod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// ricerca scadenzari
		Vector lVect = lCtrl.ExRicercaScadenzarioPagedMisSic(lScaMod, Integer.parseInt(lPagina));
		// ricerca del parametro
		ParametroModel pm = new ParametroModel();
		// Ricerca se esiste Periodo per quell'Ufficio
		pm.setCodUfficioValidita(getCodUfficioUtenteConnesso());
		pm.setNomeParametro("INIZIO MISURA");
		IParametro ip = SIEPLookupRemote.getParametroRemote();
		Vector parametri = ip.ExRicercaParametroUfficioConnesso(pm);
		BigDecimal anni = null, mesi = null, giorni = null;
		if (parametri != null && parametri.size() != 0) {
			ParametroModel pam = (ParametroModel) parametri.firstElement();
			anni = pam.getAnni();
			mesi = pam.getMesi();
			giorni = pam.getGiorni();
		}
		// se esiste un parametro ricalcolo la data scadenza comunicazione
		if (anni != null || mesi != null || giorni != null) {
			// ciclo su tutti gli elementi trovati
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				ScadenzarioModel sm = (ScadenzarioModel) itx.next();
				if (sm != null && sm.getDataFineScadenza() != null) {
					// ricalcolo la data scadenza comunicazione
					Calendar dataScadenzaComunicazione = Calendar.getInstance();
					dataScadenzaComunicazione.setTime(sm.getDataFineScadenza());
					int a = (anni != null) ? anni.intValue() : 0;
					int m = (mesi != null) ? mesi.intValue() : 0;
					int g = (giorni != null) ? giorni.intValue() : 0;
					dataScadenzaComunicazione.add(Calendar.YEAR, -a);
					dataScadenzaComunicazione.add(Calendar.MONTH, -m);
					dataScadenzaComunicazione.add(Calendar.DATE, -g);
					sm.setDataScadenzaComunicazione(dataScadenzaComunicazione.getTime());
				}
			}
		} else {
			// default
			anni = new BigDecimal(0);
			mesi = new BigDecimal(6);
			giorni = new BigDecimal(0);
		}

		setRequestAttribute("anni", anni.toString());
		setRequestAttribute("mesi", mesi.toString());
		setRequestAttribute("giorni", giorni.toString());
		setRequestAttribute("scadenzario", lVect);
		setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		setRequestAttribute("titolo", titolo);
		// MEV_39: aggiunte impostazioni
		setRequestAttribute("giorniScadenza", lGiorni != null ? lGiorni.toString() : "");
		setRequestAttribute("mesiScadenza", lMesi != null ? lMesi.toString() : "");
		setRequestAttribute("anniScadenza", lAnni != null ? lAnni.toString() : "");

		return PG_RICERCASCADENZARIOFINEPENA_MS;
	}

}