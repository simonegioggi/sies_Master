package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.util.CalendarUtil;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un provedimento di Revoca Misura
 * Alternativa disposto su uno dei titoli cumulati
 *
 * @author Intersistemi SpA
 *
 */
public class ActInserisciRevocaMisuraAlternativaCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lPage = "";
		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		if ("I".equals(lModalita)) {
			siesLogger.debug("Sono in INSERIMENTO");

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

			lStatoEsecMod = lCtrlStatoEsec.ExInserisciStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRevocaMisuraAlternativaCumulo" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);

		} else if ("M".equals(lModalita)) {
			siesLogger.debug("Sono in MODIFICA");

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

			// NB Per la modifica di StatoEsec. e ComputoCumulo può essere nomenclato ExModificaPresofferto
			// --> ExModificaStatoEsecComputoCumulo.
			lCtrlStatoEsec.ExModificaStatoEsecComputoCumulo(lStatoEsecMod, lComputo);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRevocaMisuraAlternativaCumulo" + "&"
					+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato() + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		} else if ("C".equals(lModalita)) {
			siesLogger.debug("Sono in CANCELLAZIONE");

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
					+ "=siap.siep.modulocumulo.action.ActRicercaRevocheMisureAlternativeCumulo" + "&"
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
		lStaMod.setCodEsito("0188");
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

		// ==============================================================================
		// Recupero i dati della Revoca Misura Alternativa.
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

		String lGGPassatiinIst = "";
		if (!isRequestParameterNullObj(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP)
				&& !getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP).equals("")
				&& getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP) != null) {
			lComputo.setNumGiorniMap(
					getRequestBigDecimalParameter(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP));
			lGGPassatiinIst = getRequestStringParameter(ICostantiComputiCumulo.CAMPO_NUM_GIORNI_MAP);
		}

		// ==================================
		// Recupero dei Campi opzionali
		// ==================================
		CalendarUtil lCalUtil = new CalendarUtil();

		// ===== Data Inizio Misura
		Date lDateIniMisura = null;
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)
						.equals("")
				&& getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA) != null) {
			lDateIniMisura = (DateUtils.getDate(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)));
			lComputo.setDataInizioMisura(DateUtils.getDate(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA)));
			// siesLogger.debug("--XX-- lDateIniMisura = "+DateUtils.getDateToString(lDateIniMisura,
			// "yyyyMMdd"));
		}

		// ===== data Fine Misura
		Date lDateFineMisura = null;
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)
						.equals("")
				&& getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA) != null) {
			lDateFineMisura = (DateUtils.getDate(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)));
			lComputo.setDataFineMisura(DateUtils.getDate(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA)));

			// siesLogger.debug("--XX-- lDateFineMisura = "+DateUtils.getDateToString(lDateFineMisura,
			// "yyyyMMdd"));
		}

		// ==== DURATA_MISURA = Data_Fine_Misura - Data_inizio_Misura
		CalendarModel lDateModel = new CalendarModel();
		lDateModel.setDataInizio(lDateIniMisura);
		lDateModel.setDataFine(lDateFineMisura);

		CalendarModel lDurataMisura = new CalendarModel();
		lDurataMisura = lCalUtil.CalcolaNumGiorniMesiAnni(lDateModel);

		lComputo.setNumAnniMisura(new BigDecimal(lDurataMisura.getNumAnni()));
		lComputo.setNumMesiMisura(new BigDecimal(lDurataMisura.getNumMesi()));
		lComputo.setNumGiorniMisura(new BigDecimal(lDurataMisura.getNumGiorni()));

		// ===================================================================================
		// ===== Totale Pena Residua da Espiare ==============================================
		CalendarModel lResiduoDaEspiare = new CalendarModel();
		// Reclusione
		CalendarModel lResiduaReclusione = new CalendarModel();
		// Arresto
		CalendarModel lResiduaArresto = new CalendarModel();

		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE)) {

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE)
					.equals("")) {
				lComputo.setNumAnniRevocaReclusione(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE));
				lResiduaReclusione.setNumAnni(lComputo.getNumAnniRevocaReclusione());
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE)
					.equals("")) {
				lComputo.setNumMesiRevocaReclusione(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE));
				lResiduaReclusione.setNumMesi(lComputo.getNumMesiRevocaReclusione());
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE)
					.equals("")) {
				lComputo.setNumGiorniRevocaReclusione(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE));
				lResiduaReclusione.setNumGiorni(lComputo.getNumGiorniRevocaReclusione());
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO)
					.equals("")) {
				lComputo.setNumAnniRevocaArresto(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO));
				lResiduaArresto.setNumAnni(lComputo.getNumAnniRevocaArresto());
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO)
					.equals("")) {
				lComputo.setNumMesiRevocaArresto(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO));
				lResiduaArresto.setNumMesi(lComputo.getNumMesiRevocaArresto());
			}

			if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO)
					.equals("")) {
				lComputo.setNumGiorniRevocaArresto(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO));
				lResiduaArresto.setNumGiorni(lComputo.getNumGiorniRevocaArresto());
			}
		}

		// Totale Pena Residua da espiare = Reclusione + Arresto
		lResiduoDaEspiare = lCalUtil.sommaGiorni(lResiduaReclusione, lResiduaArresto);

		// ==============================================================================
		// CALCOLO DI: PENA_ESPIATA
		// ==============================================================================

		CalendarModel lPenaEspiataModel = new CalendarModel();

		if (!getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA)
				.equals("")) {
			lComputo.setDataInizioRevoca(DateUtils.getDate(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA)));

			// ======= >>> ( se Data_Revoca è presente, PENA-ESPIATA = (DATA_REVOCA - DATA_INIZIO_MISURA) + GG
			// passati in Istituto
			lDateModel.setDataInizio(lDateIniMisura); // data_Inizio_Misura
			
			// ticket#202012020116 [D.F.] collateralmente alla segnalazione ci si è accorti che il
			// modulo cumulo non applica la regola di NON considerare validamente espiato il 
			// giorno dell'interruzione. Si corregge anticipando per i calcolo dell'espiato il giorno di 
			// revoca.
			lDateModel.setDataFine(DateUtils.getDayBefore(lComputo.getDataInizioRevoca()));
			//lDateModel.setDataFine(lComputo.getDataInizioRevoca()); // data_Revoca
			// FINE ticket#202012020116 [D.F.]
			lPenaEspiataModel = lCalUtil.CalcolaNumGiorniMesiAnni(lDateModel);
		} else {
			// ======== >>> PENA-ESPIATA = (DURATA_MISURA - RESIDUO_PEma_da_Espiare ) + GG passati in Istituto
			lPenaEspiataModel = lCalUtil.sottraiGiorniNew(lDurataMisura, lResiduoDaEspiare);
		}

		// ci somma i gg passati in istituto
		if (!lGGPassatiinIst.equals("")) {
			lPenaEspiataModel = lCalUtil.sommaGiorni(lPenaEspiataModel, Integer.parseInt(lGGPassatiinIst), 0,
					0);
			// siesLogger.debug("--XX-- pena espiata totale = "+lPenaEspiataModel);
		}

		// ===================================================================================
		// =======>>>>>>>>>>> Riempio il Moel con i valori della PENA_ESPIATA calcolati
		lComputo.setNumAnniReclusione(new BigDecimal(lPenaEspiataModel.getNumAnni()));
		lComputo.setNumMesiReclusione(new BigDecimal(lPenaEspiataModel.getNumMesi()));
		lComputo.setNumGiorniReclusione(new BigDecimal(lPenaEspiataModel.getNumGiorni()));

		// siesLogger.debug("--XX-- Pena Espiata = AA = "+lComputo.getNumAnniReclusione()+" -MM =
		// "+lComputo.getNumMesiReclusione()+" -GG = "+lComputo.getNumGiorniReclusione());

		// ==============================================================================
		// CALCOLO DI: PENA_RESIDUA
		// ==============================================================================
		if (lComputo.getNumAnniRevocaReclusione() != null || lComputo.getNumMesiRevocaReclusione() != null
				|| lComputo.getNumGiorniRevocaReclusione() != null
				|| lComputo.getNumAnniRevocaArresto() != null || lComputo.getNumMesiRevocaArresto() != null
				|| lComputo.getNumGiorniRevocaArresto() != null) {
			// Pena Residua = residuo PEma da Espiare
		} else {

			// ====== >>> PENA_RESIDUA = Data Fine Misura – Data Revoca
			if (lDateFineMisura != null && lComputo.getDataInizioRevoca() != null) {
				lDateModel = new CalendarModel();

				lDateModel.setDataInizio(lComputo.getDataInizioRevoca());
				lDateModel.setDataFine(lDateFineMisura);

				CalendarModel lPenaRes = new CalendarModel();
				lPenaRes = lCalUtil.CalcolaNumGiorniMesiAnni(lDateModel);

				// ci Tolgo i gg passati in istituto
				if (!lGGPassatiinIst.equals("")) {
					CalendarModel lMapIst = new CalendarModel();
					lMapIst.setNumGiorni(Integer.parseInt(lGGPassatiinIst));
					lMapIst.setNumMesi(0);
					lMapIst.setNumAnni(0);

					lPenaRes = lCalUtil.sottraiGiorniNew(lPenaRes, lMapIst);
					// siesLogger.debug("--XX-->>>>>>>>>>>>>>>>>>>>>>>>> pena residua totale = "+lPenaRes);
				}

				// La Pena residua Calcolata, NON è divisa tra reclusione e Arresto:
				// viene inserita come unica entità nei quantum RECLUSIONE
				lComputo.setNumAnniRevocaReclusione(new BigDecimal(lPenaRes.getNumAnni()));
				lComputo.setNumMesiRevocaReclusione(new BigDecimal(lPenaRes.getNumMesi()));
				lComputo.setNumGiorniRevocaReclusione(new BigDecimal(lPenaRes.getNumGiorni()));

			}

		}

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