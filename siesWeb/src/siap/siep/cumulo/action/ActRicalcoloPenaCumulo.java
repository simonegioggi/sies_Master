package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
//import siap.sico.calendar.model.CalendarModel;
//import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
//import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
//import siap.sico.util.CalendarUtil;
//import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActInserisciCumulo
 * </p>
 * <p>
 * Description: Classe Action per il Calcolo della Pena Residua da associare al provvedimento di Cumulo
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
public class ActRicalcoloPenaCumulo extends ActionSiap implements ICostantiCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Calcolo e Inserimento della Pena residua in Cumulo
	 * 
	 * @return jsp di visualizzazione del risultato
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		BigDecimal lFascID = ((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep();

		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaByKey(getRequestBigDecimalParameter("IdPosizioneGiuridica"));
		setRequestAttribute("PosizioneGiuridica", lPG);

		// ============================================================================
		// Recupero il cumulo non validato (meno recente??)
		// ============================================================================
		CumuloModel lCumMod = new CumuloModel();
		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		// lCumMod.setFasSieIdFascicoloSiep(lFascID);
		// Vector cumuli=iCum.ExRicercaCumulo(lCumMod);
		Vector cumuli = iCum.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascID);
		if (cumuli.size() > 0)
			lCumMod = ((CumuloModel) (cumuli).get(0));

		// ============================================================================
		// Recupero la PENA_CUMULO associata al cumulo
		// contiene i quantum e le LA
		// ============================================================================
		PenaCumuloModel lPenCumMod = new PenaCumuloModel();
		IPenaCumulo lCtrlPena = SIEPLookupRemote.getPenaCumuloRemote();
		lPenCumMod = lCtrlPena.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());

		// ============================================================================
		// Verifico la congruenza tra la posizione giuridica e la decorrenza della
		// pena
		// Se diverso da Libero (07-10), in Sospensione (46-47), in differimento (16-17)
		// deve essere valorixzzata la data inizio pena
		// ============================================================================
		if (lPG != null && lPG.getCodPosizioneGiuridica() != null
				&& !lPG.getCodPosizioneGiuridica().equals("10")
				&& !lPG.getCodPosizioneGiuridica().equals("07")
				&& !lPG.getCodPosizioneGiuridica().equals("46")
				&& !lPG.getCodPosizioneGiuridica().equals("47")
				&& !lPG.getCodPosizioneGiuridica().equals("16")
				&& !lPG.getCodPosizioneGiuridica().equals("17") && lPenCumMod != null
				&& lPenCumMod.getDataDecorrenzaPena() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Data Decorrenza Pena obbligatoria");
			lRedirigi.setAction("siap.siep.cumulo.action.ActLoadInserisciPenaComplessivaCumulo&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (lPG != null && lPG.getCodPosizioneGiuridica() != null
				&& (lPG.getCodPosizioneGiuridica().equals("10") || lPG.getCodPosizioneGiuridica().equals("07")
						|| lPG.getCodPosizioneGiuridica().equals("46")
						|| lPG.getCodPosizioneGiuridica().equals("47")
						|| lPG.getCodPosizioneGiuridica().equals("16"))
				&& lPenCumMod != null && lPenCumMod.getDataDecorrenzaPena() != null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Posizione Giuridica Incoerente con la data Decorrenza Pena");
			lRedirigi.setAction("siap.siep.cumulo.action.ActLoadInserisciPenaComplessivaCumulo&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Viene creato L'Evento
		EventoModel lEventoMod = new EventoModel();
		String lTipologia = this.getRequestStringParameter(ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA);

		lEventoMod.setCodTipoEvento("01");
		lEventoMod.setCodTipoProvvedimento("04");

		if (lTipologia != null && lTipologia.equals("0")) {
			lEventoMod.setCodMotivo("0222");
		} else if (lTipologia != null && lTipologia.equals("1")) {
			lEventoMod.setCodMotivo("0223");
		} else if (lTipologia != null && lTipologia.equals("2")) {
			lEventoMod.setCodMotivo("0224");
		} else {
			lEventoMod.setCodMotivo("0277");
		}

		lEventoMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEventoMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEventoMod.setCodEsito("-");
		lEventoMod.setCodUfficioDestinatario("-");
		lEventoMod.setCodLuogoDestinatario("-");
		lEventoMod.setCodTipoUfficioDestinatario("-");
		lEventoMod.setFasSieIdFascicoloSiep(lFascID);
		lEventoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEventoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEventoMod.setDataInserimento(DateUtils.getSysDate());
		lEventoMod.setFlagDocumentoRegistrato("N");

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lCtrlEvento.ExInserisciEvento(lEventoMod);

		// ============================================================================
		// Verifico se esiste una Pena Residua con flag validato a 'N' (????), in
		// questo caso la aggiorno altrimenti ne inserisco una nuova
		// ============================================================================
		PenaResiduaModel lPenResidua = new PenaResiduaModel();
		IPenaResidua lCtrlPen = SIEPLookupRemote.getPenaResiduaRemote();
		lPenResidua = lCtrlPen.ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato(lFascID);

		PenaResiduaModel lPenRes = new PenaResiduaModel();

		if (lPenResidua != null && lPenResidua.getIdPenaResidua() != null
				&& lPenResidua.getEveIdEvento() == null // mod. 17/09/2007 la pena residua non deve essere
														// associata ad alcun evento
		) { // Trovata pena non validata la utilizzo
			// lPenRes = lPenResidua;
			lPenRes.setIdPenaResidua(lPenResidua.getIdPenaResidua()); // mod. 17/09/2007 copio solo l'id per
																		// andare in aggiornamento
		}

		lPenRes.setDataInizio(null);
		lPenRes.setDataFine(null);

		lPenRes.setDataInizioArresto(null);
		lPenRes.setDataFineReclusione(null);
		lPenRes.setDataFinePresunta(null);

		lPenRes.setDataInizioIsolamentoDiurno(null);
		lPenRes.setDataFineIsolamentoDiurno(null);

		if (lPenCumMod != null && lPenCumMod.getDataDecorrenzaPena() != null) {
			lPenRes.setDataInizio(lPenCumMod.getDataDecorrenzaPena());
		}

		// ============================================================================
		// Ricalcolo la pena residua. Copio i quantum specificati nel Cumulo se è stata
		// specificata una data di decorrenza calcolo anche le date e anticipo il fine
		// pena se presenti LA da computare.
		// ============================================================================
		if (lPenCumMod.getFlagErgastolo() != null
				&& (lPenCumMod.getFlagErgastolo().equals("S") || lPenCumMod.getFlagErgastolo().equals("D"))) { // Se
																												// Ergastolo
			lPenRes.setFlagErgastolo(lPenCumMod.getFlagErgastolo());
			lPenRes.setDataFinePresunta(DateUtils.getDate("9999", "12", "31"));
			lPenRes.setDataFine(DateUtils.getDate("9999", "12", "31"));

			if (lPenCumMod.getNumAnniIsolamentoDiurno() != null)
				lPenRes.setNumAnniIsolamentoDiurno(lPenCumMod.getNumAnniIsolamentoDiurno());

			if (lPenCumMod.getNumMesiIsolamentoDiurno() != null)
				lPenRes.setNumMesiIsolamentoDiurno(lPenCumMod.getNumMesiIsolamentoDiurno());

			if (lPenCumMod.getNumGiorniIsolamentoDiurno() != null)
				lPenRes.setNumGiorniIsolamentoDiurno(lPenCumMod.getNumGiorniIsolamentoDiurno());
		} else {
			/***********************
			 * SOTTRAZIONE QUANTUM IN CASO DI QUANTUM IN SOSPENSIONE
			 *************************/
			// commentato 17-01-05 Luciana --Dario
			/*
			 * CalendarUtil lCal = new CalendarUtil(); CalendarModel lCalModArrSo = new CalendarModel();
			 * CalendarModel lCalModRecSo = new CalendarModel();
			 * 
			 * CalendarModel lCalModArr = new CalendarModel(); CalendarModel lCalModRec = new CalendarModel();
			 * CalendarModel lCalReclusione = null; CalendarModel lCalArresto = null;
			 * 
			 * if (lPenCumMod != null && (lPenCumMod.getNumAnniArrestoSosp() != null ||
			 * lPenCumMod.getNumGiorniArrestoSosp() != null || lPenCumMod.getNumMesiArrestoSosp() != null)) {
			 * //arresto sospeso lCalModArrSo.setNumAnni(lPenCumMod.getNumAnniArrestoSosp());
			 * lCalModArrSo.setNumGiorni(lPenCumMod.getNumGiorniArrestoSosp());
			 * lCalModArrSo.setNumMesi(lPenCumMod.getNumMesiArrestoSosp());
			 * 
			 * //arresto lCalModArr.setNumAnni(lPenCumMod.getNumAnniArresto());
			 * lCalModArr.setNumGiorni(lPenCumMod.getNumGiorniArresto());
			 * lCalModArr.setNumMesi(lPenCumMod.getNumMesiArresto());
			 * 
			 * //arresto lCalArresto = lCal.sottraiGiorni(lCalModArr, lCalModArrSo); }
			 * 
			 * if (lPenCumMod != null && (lPenCumMod.getNumAnniReclusioneSosp() != null ||
			 * lPenCumMod.getNumGiorniReclusioneSosp() != null || lPenCumMod.getNumMesiReclusioneSosp() !=
			 * null)) {
			 * 
			 * //reclusione sospeso lCalModRecSo.setNumAnni(lPenCumMod.getNumAnniReclusioneSosp());
			 * lCalModRecSo.setNumGiorni(lPenCumMod.getNumGiorniReclusioneSosp());
			 * lCalModRecSo.setNumMesi(lPenCumMod.getNumMesiReclusioneSosp());
			 * 
			 * //reclusione lCalModRec.setNumAnni(lPenCumMod.getNumAnniReclusione());
			 * lCalModRec.setNumGiorni(lPenCumMod.getNumGiorniReclusione());
			 * lCalModRec.setNumMesi(lPenCumMod.getNumMesiReclusione());
			 * 
			 * //reclusione lCalReclusione = lCal.sottraiGiorni(lCalModRec, lCalModRecSo); }
			 * 
			 * if (lCalReclusione != null) { lPenRes.setNumAnniReclusione(new
			 * BigDecimal(lCalReclusione.getNumAnni())); lPenRes.setNumMesiReclusione(new
			 * BigDecimal(lCalReclusione.getNumMesi())); lPenRes.setNumGiorniReclusione(new
			 * BigDecimal(lCalReclusione.getNumGiorni())); }
			 * 
			 * if (lCalArresto != null) { lPenRes.setNumAnniArresto(new BigDecimal(lCalArresto.getNumAnni()));
			 * lPenRes.setNumMesiArresto(new BigDecimal(lCalArresto.getNumMesi()));
			 * lPenRes.setNumGiorniArresto(new BigDecimal(lCalArresto.getNumGiorni())); } fine commento
			 */

			// ==========================================================================
			// I quantum di pena sono gli stessi di quelli imputati nel cumulo
			// ==========================================================================
			lPenRes.setFlagErgastolo(lPenCumMod.getFlagErgastolo());

			lPenRes.setNumAnniReclusione(lPenCumMod.getNumAnniReclusione());
			lPenRes.setNumMesiReclusione(lPenCumMod.getNumMesiReclusione());
			lPenRes.setNumGiorniReclusione(lPenCumMod.getNumGiorniReclusione());

			lPenRes.setNumAnniArresto(lPenCumMod.getNumAnniArresto());
			lPenRes.setNumMesiArresto(lPenCumMod.getNumMesiArresto());
			lPenRes.setNumGiorniArresto(lPenCumMod.getNumGiorniArresto());

			// ==========================================================================
			// Ricalcolo le date di decorrenza in funzione dei quantum specificati nel
			// cumulo, della data di decorrenza
			// ==========================================================================
			Date lDataFinePena = null;
			Date lDataFineReclusione = null;
			Date lDataInizioArresto = null;

			if (lPenCumMod != null && lPenCumMod.getDataDecorrenzaPena() != null) {
				ICalcoloPena iCal = SIEPLookupRemote.getCalcoloPenaRemote();
				Vector lDateFine = iCal.exCalcolaDataFinePena(lPenCumMod.getDataDecorrenzaPena(), lPenRes,
						true); // il flag true indica CON DIES_A_QUO

				if (lDateFine.size() == 2) {
					lDataFineReclusione = (Date) lDateFine.get(0);
					lDataInizioArresto = DateUtils.getDayAfter(lDataFineReclusione);
					lDataFinePena = (Date) lDateFine.get(1);
				}

				// String lFlagDateIntermedie = "si";
				if (lDateFine.size() == 1) {
					lDataFinePena = (Date) lDateFine.get(0);
					// lFlagDateIntermedie = "no";
				}

				lPenRes.setDataFineReclusione(lDataFineReclusione);
				lPenRes.setDataInizioArresto(lDataInizioArresto);
			}

			/*
			 * ** I giorni di liberazione anticipata vengono presi non più dalla tabella LICENZA_LIBANTICIPATA
			 * (che contiene, per quanto riguarda il cumulo, i giorni totali già inseriti meno i giorni
			 * indicati dall'utente nel cumulo) ma dalla tabella PENA_CUMULO. DL 01/08/2005 **
			 * 
			 * LicenzaLibAnticipataModel libAntMod; ILicenzaPeriodiLibAnticipata Ilib =
			 * SICOLookupRemote.getLicenzaPeriodiLibAntRemote(); libAntMod =
			 * Ilib.ExRicercaLicenzaLibanticipataUltimaByIDFascicoloSIEP(lFascID);
			 * 
			 * if(libAntMod != null && libAntMod.getNumeroGiorni()!= null && lDataFinePena != null) {
			 * lDataFinePena = DateUtils.moveDateTo(lDataFinePena, java.util.Calendar.DAY_OF_MONTH,
			 * -libAntMod.getNumeroGiorni().intValue()); }
			 */

			// ==========================================================================
			// Anticipo il fine pena se presenti LA
			// n.b. vengono anticipati sia il fine reclusione che inizio arresto che
			// fine pena. Se fine reclusione<data inizio viene eliminato
			// ==========================================================================
			if (lDataFinePena != null && (lPenCumMod.getNumGiorniLibAnticipata() != null
					|| lPenCumMod.getNumGiorniRiduzionePena() != null)) {
				int ggDaDetrarre = 0;
				if (lPenCumMod.getNumGiorniLibAnticipata() != null)
					ggDaDetrarre += lPenCumMod.getNumGiorniLibAnticipata().intValue();

				if (lPenCumMod.getNumGiorniRiduzionePena() != null)
					ggDaDetrarre += lPenCumMod.getNumGiorniRiduzionePena().intValue();

				lDataFinePena = DateUtils.moveDateTo(lDataFinePena, java.util.Calendar.DAY_OF_MONTH,
						-ggDaDetrarre);

				// Arretro le data fine reclusione, inizio arresto
				if (lPenRes.getDataFineReclusione() != null) {
					lPenRes.setDataFineReclusione(DateUtils.moveDateTo(lPenRes.getDataFineReclusione(),
							Calendar.DAY_OF_MONTH, -ggDaDetrarre));
				}
				if (lPenRes.getDataInizioArresto() != null) {
					lPenRes.setDataInizioArresto(DateUtils.moveDateTo(lPenRes.getDataInizioArresto(),
							Calendar.DAY_OF_MONTH, -ggDaDetrarre));
				}

				// Azzero le date se per effetto delle LA sono arretrate oltre la data inizio pena
				if (lPenRes.getDataFineReclusione() != null && DateUtils
						.isGreater(lPenCumMod.getDataDecorrenzaPena(), lPenRes.getDataFineReclusione())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data fine reclusione < data inizio pena per effetto arretramento LA");
					lPenRes.setDataFineReclusione(null);
				}

				if (lPenRes.getDataInizioArresto() != null && DateUtils
						.isGreater(lPenCumMod.getDataDecorrenzaPena(), lPenRes.getDataInizioArresto())) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Data inizio arresto < data inizio pena per effetto arretramento LA");
					lPenRes.setDataInizioArresto(null);
				}
			}

			lPenRes.setDataFinePresunta(lDataFinePena);

			if (lPenCumMod != null && lPenCumMod.getImportoAmmenda() != null) {
				lPenRes.setImportoAmmenda(lPenCumMod.getImportoAmmenda());
			}

			if (lPenCumMod != null && lPenCumMod.getImportoMulta() != null) {
				lPenRes.setImportoMulta(lPenCumMod.getImportoMulta());
			}

			lPenRes.setDiesAQuo("S");
		}

		PenaResiduaModel lPena = new PenaResiduaModel();

		// ============================================================================
		// Se esiste una pena non validata (N) vado in aggiornamento, la inserisco
		// ============================================================================
		// if(lPenResidua != null)
		if (lPenRes.getIdPenaResidua() != null) // mod. 17/09/2007
		{ // Pena residua già a sistema
			lPenRes.setFlagValidato("N");
			lPenRes.setFasSieIdFascicoloSiep(lFascID);
			lPenRes.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lPenRes.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lPenRes.setDataInserimento(DateUtils.getSysDate());

			// if (lPenResidua != null && lPenResidua.getFlagValidato().equals("N"))
			// { // sempre vero
			// lPenRes.setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
			// lPenRes.setCodUfficioAggiornamento (this.getCodUfficioUtenteConnesso());
			// lPenRes.setDataAggiornamento (DateUtils.getSysDate());
			// }
			// else if (lPenResidua != null && lPenResidua.getFlagValidato().equals("S")) //fine aggiornamento
			// inizio inserimento
			// { // mai vero
			// lPenRes.setCodOperatoreInserimento (this.getCodUtenteConnesso());
			// lPenRes.setCodUfficioInserimento (this.getCodUfficioUtenteConnesso());
			// lPenRes.setDataInserimento (DateUtils.getSysDate());
			// }

			lPena = lCtrlPen.ExInserisciAggiornaPenaResidua(lPenRes);
		} else {
			lPenRes.setFlagValidato("N");
			lPenRes.setFasSieIdFascicoloSiep(lFascID);
			lPenRes.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lPenRes.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lPenRes.setDataInserimento(DateUtils.getSysDate());

			lPena = lCtrlPen.ExInserisciPenaResidua(lPenRes);
		}

		setRequestAttribute("PenaResidua", lPena);
		setSessionAttribute("cumulowiz", "");
		setRequestAttribute("PenaCumuloMod", lPenCumMod);

		return PG_DETTAGLIOPENACUMULO;
	}
}