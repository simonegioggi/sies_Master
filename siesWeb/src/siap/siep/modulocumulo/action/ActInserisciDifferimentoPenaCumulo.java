package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.util.CalendarUtil;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un provedimento di Differimento Pena
 * disposto su uno dei titoli cumulati
 * 
 * @author Intersistemi SpA
 *
 */
public class ActInserisciDifferimentoPenaCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String Error = "NO";

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lPage = "";
		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		if ("I".equals(lModalita)) {
			siesLogger.debug("--XX-- >>>>>>>>>>>>>>  Sono in INSERIMENTO");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("I");

			// Si carica in StatoEsecuzione l'ufficio emittente
			lStatoEsecMod.setCodUfficioEmittente(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			lStatoEsecMod.setCodLuogoEmittente(
					getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE))
							.getCodComune());

			// siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);

			ComputiCumuloModel lComputo = this.getDatiComputo("I");
			// siesLogger.debug("lComputo = "+lComputo);

			if (Error.equals("SI")) {
				// MANDO un ERRORE/WARNING a VIDEO
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"La data Fine Differimento non coincide con i quantum relativi al Differimento");
				return IWebConstants.PG_MESSAGE;
			}

			lStatoEsecMod = lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioDifferimentoPenaCumulo" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		} else if ("M".equals(lModalita)) {
			siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>>> Sono in MODIFICA");

			StatoEsecTitoloCumulatoModel lStatoEsecMod = this.getDatiProvvedimento("M");
			// siesLogger.debug("lStatoEsecMod = "+lStatoEsecMod);

			// Si carica in StatoEsecuzione l'ufficio emittente reimpostato.

			lStatoEsecMod.setCodUfficioEmittente(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE)));
			lStatoEsecMod.setCodLuogoEmittente(
					getCodComuneByDescr(getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE))
							.getCodComune());

			ComputiCumuloModel lComputo = this.getDatiComputo("M");
			// siesLogger.debug("lComputo = "+lComputo);

			lComputo.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());

			if (Error.equals("SI")) {
				// MANDO un ERRORE/WARNING a VIDEO
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"La data Fine Differimento non coincide con i quantum relativi al Differimento");
				return IWebConstants.PG_MESSAGE;
			}

			// NB Per la modifica di StatoEsec. e ComputoCumulo può essere nomenclato ExModificaPresofferto
			// --> ExModificaStatoEsecComputoCumulo.
			lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioDifferimentoPenaCumulo" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		} else if ("C".equals(lModalita)) {
			siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>>> Sono in CANCELLAZIONE");

			BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter(
					CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			BigDecimal lIdComputo = getRequestBigDecimalParameter(CAMPO_ID_COMPUTI_CUMULO);

			// Devo verificare se cancellare un solo computo o l'intero provvedimento
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			StatoEsecTitoloCumulatoModel lStato = lCtrlStato
					.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStatoEsecuzione);

			if (lStato.getListaComputi().size() == 1) {
				// Un solo computo, cancello tutto
				siesLogger.debug("Elimino l'intero provvedimento di computo: " + lIdStatoEsecuzione);
				lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(lIdStatoEsecuzione, null);
			} else {
				// Ho più computi cancello solo quello indicato sulla request
				siesLogger.debug("Elimino il solo conmputo:" + lIdComputo);
				IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
				lCtrlComputi.ExCancellaComputiCumuloBykey(lIdComputo);
			}
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActRicercaDifferimentoPenaCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		}

		return lPage;
	}

	/**
	 * 
	 * @throws F3BException
	 */
	private StatoEsecTitoloCumulatoModel getDatiProvvedimento(String aTipoOper) throws F3BException {

		StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();

		lStaMod.setIdStatoEsecTitoloCumulato(
				getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));
		lStaMod.setCodTipoEvento("01");
		lStaMod.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO));
		lStaMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));

		if (super.getDatiTitoloCumulato().getProcedimentoCumulato() != null) {
			lStaMod.setCodUfficioEmittente(
					super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato());
			lStaMod.setCodLuogoEmittente(
					super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodLuogoUfficioFasCumulato());
		}

		lStaMod.setDataEmissione(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_MESE_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_GIORNO_DATA_EMISSIONE)));
		lStaMod.setCodEsito("0001");
		lStaMod.setCodEsitoTenore("-");
		lStaMod.setAnnoProcedimento(null);
		lStaMod.setProgrProcedimento(null);
		lStaMod.setAnnoProvvedimento(null);
		lStaMod.setProgrProvvedimento(null);

		lStaMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lStaMod.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		lStaMod.setFlagStato("I");
		lStaMod.setMotivoModifica(null);

		if ("I".equals(aTipoOper)) {
			lStaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lStaMod.setDataInserimento(DateUtils.getSysDate());
			lStaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			// La Modifica cambia solo lo stato di "Estratto / Modificato".
			String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO);
			if ("E".equals(flagStato) || "M".equals(flagStato))
				lStaMod.setFlagStato("M");
			lStaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lStaMod.setDataAggiornamento(DateUtils.getSysDate());
			lStaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lStaMod;
	}

	/**
	 * Recupera i dati dei computi cumulo
	 * 
	 * @return
	 * @throws F3BException
	 */
	private ComputiCumuloModel getDatiComputo(String aTipoOper) throws F3BException {

		// siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>> Sono in getDatiComputo ");
		// ==============================================================================
		// Recupero i dati del Differimento Pena.
		// ==============================================================================
		ComputiCumuloModel lComputo = new ComputiCumuloModel();

		lComputo.setCodTipoAnnotazione("-");
		lComputo.setCodCausaleComputo("-");
		lComputo.setCodDpr("-");

		lComputo.setDataEmissioneProvv(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_MESE_DATA_EMISSIONE),
				getRequestStringParameter(CAMPO_GIORNO_DATA_EMISSIONE)));
		lComputo.setAnnoProvv(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
		lComputo.setProgrProvv(getRequestBigDecimalParameter(CAMPO_PROGR_PROVVEDIMENTO));
		lComputo.setAnnoProc(getRequestBigDecimalParameter(CAMPO_ANNO_PROCEDIMENTO));
		lComputo.setProgrProc(getRequestBigDecimalParameter(CAMPO_PROGR_PROCEDIMENTO));

		lComputo.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lComputo.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		lComputo.setCodUfficioEmittenteProvv(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE)));
		lComputo.setCodLuogoUfficioProvv(getCodComuneByDescr(
				getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE)).getCodComune());

		// ==============================================================
		// Recupero dei Campi opzionali in base all'oggetto procedimento
		// ==============================================================
		String[] arrayUDS = new String[] { "2010", "2011" };
		String[] arrayTDS = new String[] { "0030", "0031", "0032", "0033", "0031", "0201", "0202" };

		Date lDataDiff = null;
		Date lDataFineDiff = null;
		BigDecimal aaDiff = null;
		BigDecimal mmDiff = null;
		BigDecimal ggDiff = null;

		if (Arrays.asList(arrayUDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO))) {
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)
					.equals("")) {
				lComputo.setDataInizioMisura(DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA),
						getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)));
				lDataDiff = DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA),
						getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA));
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
					.equals("")) {
				lComputo.setDataFineMisura(DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)));
				lDataFineDiff = DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA));
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE).equals("")) {
				if (this.isRequestChecked(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE)) {
					lComputo.setCodTDSCompetente(getCodUfficioByCodTipoUfficioDescrComune("TDS",
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE)));
				} else if (this
						.isRequestChecked(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE_MINORI)) {
					lComputo.setCodTDSCompetente(getCodUfficioByCodTipoUfficioDescrComune("TDSM",
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE)));
				}
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals("")) {
				lComputo.setNumAnniMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA));
				aaDiff = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA);
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals("")) {
				lComputo.setNumMesiMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA));
				mmDiff = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA);
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA).equals("")) {
				lComputo.setNumGiorniMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));
				ggDiff = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA);
			}

			if (this.isRequestChecked(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE))
				lComputo.setFlagDecisioneTribunale("S");
			else if (this.isRequestChecked(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE_MINORI))
				lComputo.setFlagDecisioneTribunale("M");
			else
				lComputo.setFlagDecisioneTribunale(null);

		}

		if (Arrays.asList(arrayTDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO))) {
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)
					.equals("")) {
				lComputo.setDataInizioMisura(DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA),
						getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)));
				lDataDiff = DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA),
						getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA));
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
					.equals("")) {
				lComputo.setDataFineMisura(DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)));
				lDataFineDiff = DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA));
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals("")) {
				lComputo.setNumAnniMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA));
				aaDiff = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA);
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals("")) {
				lComputo.setNumMesiMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA));
				mmDiff = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA);
			}
			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA).equals("")) {
				lComputo.setNumGiorniMisura(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));
				ggDiff = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA);
			}

		}

		// =================== MEB_70 =========================
		// CONTROLLO SULLA ESATTEZA DEL PERIODO DI DIFFERIMENTO
		// ====================================================
		CalendarModel lCalDifferimento = new CalendarModel();
		CalendarUtil lCaleCalc = new CalendarUtil();
		PenaResiduaModel FineDifferimentoModel = null;

		if (lDataFineDiff != null) {

			// ====== Calcolo dei quantum (Tramite Data Inizio e Data Fine DIFFERIMENTO) ======
			// ================================================================================================

			lCalDifferimento.setDataInizio(lDataDiff);
			lCalDifferimento.setDataFine(lDataFineDiff);

			String diesAquo = "S"; // forzato a S per conteggiare anche la data inizio
			if (diesAquo.equals("S")) {
				lCalDifferimento = lCaleCalc.CalcolaNumGiorniMesiAnni(lCalDifferimento, false);
			} else {
				lCalDifferimento = lCaleCalc.CalcolaNumGiorniMesiAnni(lCalDifferimento, true);
			}

			// Normalizzo i quantum
			lCalDifferimento = lCaleCalc.ricalcolaGAM(lCalDifferimento);

			Error = "NO";

			if (getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals("")
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals("")
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
							.equals("")) {
				// Se nella form NON sono indicati i quantum, valgono quelli appena CALCOLATI
			} else {
				// CONFRONTO: tra i quantum indicati nella Form e quelli appena Calcolati :
				// Se NON coincidono, mando um MESS di WARNING
				int QQAnni = 0;
				int QQMesi = 0;
				int QQGiorni = 0;

				if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals("")) {
					QQAnni = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
							.intValue();
				}
				if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals("")) {
					QQMesi = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
							.intValue();
				}
				if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
						.equals("")) {
					QQGiorni = getRequestBigDecimalParameter(
							ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA).intValue();
				}

				if (lCalDifferimento.getNumAnni() != QQAnni) {
					Error = "SI";
				}

				if (lCalDifferimento.getNumMesi() != QQMesi) {
					Error = "SI";
				}

				if (lCalDifferimento.getNumGiorni() != QQGiorni) {
					Error = "SI";
				}
			}

		} else {

			// ====== Calcolo della Data Fine DIFFERIMENTO (Tramite Data Inizio e Periodo Differimento
			// (quantum) ) ======
			// =====================================================================================================================
			CalcoloPenaModel lCalcDiffModel = new CalcoloPenaModel();

			PenaResiduaModel lquantumModel = new PenaResiduaModel();
			if (aaDiff != null) {
				lquantumModel.setNumAnniReclusione(aaDiff);
			} else {
				lquantumModel.setNumAnniReclusione(new BigDecimal(0));
			}
			if (mmDiff != null) {
				lquantumModel.setNumMesiReclusione(mmDiff);
			} else {
				lquantumModel.setNumMesiReclusione(new BigDecimal(0));
			}
			if (ggDiff != null) {
				lquantumModel.setNumGiorniReclusione(ggDiff);
			} else {
				lquantumModel.setNumGiorniReclusione(new BigDecimal(0));
			}

			lCalcDiffModel.setPenaResiduaManuale(lquantumModel);
			lCalcDiffModel.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);

			FineDifferimentoModel = null;
			try {
				FineDifferimentoModel = lCalcDiffModel.getPenaDaEspiare(lDataDiff, null, "all", null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// String SggF = DateUtils.getDayToString(FineDifferimentoModel.getDataFine());
			// String SmmF = DateUtils.getMonthToString(FineDifferimentoModel.getDataFine());
			// String SaaF = DateUtils.getYearToString(FineDifferimentoModel.getDataFine());

			Error = "NO";

			if (getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA).equals("")
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA)
							.equals("")
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
							.equals("")) {
				// Se nella form NON è indicata la DATA_FINE_DIFFERIMENTO, vale quella appena CALCOLATA

			} else {
				// CONFRONTO: tra la DATA_FINE_DIFFERIMENTO indicata nella Form e quella appena CALCOLATA :
				// Se NON coincidono, mando um MESS di WARNING
				Date lDataFine = null;

				lDataFine = DateUtils.getDate(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA));

				if (lDataFine.compareTo(FineDifferimentoModel.getDataFine()) != 0) {
					Error = "SI";
				}
			}

		}

		// se i quantum NON sono stati digitati a mano nella FORM, vengono archiviati nel DB quelli calcolati
		if (getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA).equals("")
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA).equals("")
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA).equals("")) {

			lComputo.setNumAnniMisura(new BigDecimal(lCalDifferimento.getNumAnni()));
			lComputo.setNumMesiMisura(new BigDecimal(lCalDifferimento.getNumMesi()));
			lComputo.setNumGiorniMisura(new BigDecimal(lCalDifferimento.getNumGiorni()));
		}

		// Se la DATA_FINE_DIFFERIMENTO NON è stata digitata a mano nella FORM, viene archiviata nel DB quella
		// calcolata
		if (getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA).equals("")
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA)
						.equals("")
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
						.equals("")) {

			lComputo.setDataFineMisura(FineDifferimentoModel.getDataFine());

		}

		// FINE CONTROLLO SULLA ESATTEZA DEL PERIODO DI DIFFERIMENTO
		// ==========================================================
		// ========= >>>> DATA_INIZIO_PENA
		Date lDataDecorrenzaPena = null;
		// ======== >>>>> DATA_FINE_PENA
		Date lDataScadenzaPena = null;
		if (Arrays.asList(arrayTDS).contains(getRequestStringParameter(CAMPO_COD_MOTIVO))) {
			if (!getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_DA)
					.equals("")) {
				lComputo.setDataReclusioneDa(DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_DA),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_DA),
						getRequestStringParameter(
								ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_DA)));
				lDataDecorrenzaPena = DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_DA),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_DA),
						getRequestStringParameter(
								ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_DA));
			}

			if (!getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_A)
					.equals("")) {
				lComputo.setDataReclusioneA(DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_A),
						getRequestStringParameter(
								ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_A)));
				lDataScadenzaPena = DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_A));
			}
		} else {
			if (!getRequestStringParameter(ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA)
					.equals("")) {
				lComputo.setDataReclusioneDa(DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA)));
				lDataDecorrenzaPena = DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA));
			}

			if (!getRequestStringParameter(ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A)
					.equals("")) {
				lComputo.setDataReclusioneA(DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A)));
				lDataScadenzaPena = DateUtils.getDate(
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A),
						getRequestStringParameter(ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A));
			}
		}

		// ====================================================================================
		// Calcolo PERIODO ESPIATO = DATA_DIFFERIMENTO - DATA_DECORRENZA_PENA
		// ====================================================================================
		CalendarUtil lCalUtil = new CalendarUtil();

		CalendarModel lDateModel = new CalendarModel();
		lDateModel.setDataInizio(lDataDecorrenzaPena);
		lDateModel.setDataFine(lDataDiff);

		CalendarModel lPenaEspiata = new CalendarModel();
		lPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lDateModel);

		// La Pena Espiata Calcolata, viene inserita come unica entità nei quantum RECLUSIONE
		lComputo.setNumAnniReclusione(new BigDecimal(lPenaEspiata.getNumAnni()));
		lComputo.setNumMesiReclusione(new BigDecimal(lPenaEspiata.getNumMesi()));
		lComputo.setNumGiorniReclusione(new BigDecimal(lPenaEspiata.getNumGiorni()));

		// ====================================================================================
		// Calcolo RESIDUO PENA = DATA_SCADENZA_PENA - DATA_DIFFERIMENTO
		// ====================================================================================

		lDateModel = new CalendarModel();
		lDateModel.setDataInizio(lDataDiff);
		lDateModel.setDataFine(lDataScadenzaPena);

		CalendarModel lPenaRes = new CalendarModel();
		lPenaRes = lCalUtil.CalcolaNumGiorniMesiAnni(lDateModel);

		// La Pena residua Calcolata, NON è divisa tra reclusione e Arresto:
		// viene inserita come unica entità nei quantum RECLUSIONE
		lComputo.setNumAnniRevocaReclusione(new BigDecimal(lPenaRes.getNumAnni()));
		lComputo.setNumMesiRevocaReclusione(new BigDecimal(lPenaRes.getNumMesi()));
		lComputo.setNumGiorniRevocaReclusione(new BigDecimal(lPenaRes.getNumGiorni()));

		// =====================================================================================

		if ("I".equals(aTipoOper)) {
			lComputo.setCodOperatoreInserimento(getCodUtenteConnesso());
			lComputo.setDataInserimento(DateUtils.getSysDate());
			lComputo.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			lComputo.setIdComputiCumulo(
					getRequestBigDecimalParameter(ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO));

			lComputo.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lComputo.setDataAggiornamento(DateUtils.getSysDate());
			lComputo.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lComputo;
	}

}