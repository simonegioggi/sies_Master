package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento, la modifica e la cancellazione dei dati di un provedimento di Fungibilità per
 * computo presofferto disposta su uno dei titoli cumulati 0212 - Fungibilità per computo Misura Cautelare
 * Altro Reato art. 657 c.p.p 0213 - Fungibilità per computo Pena Detentiva Espiata per Altro Reato art. 657
 * c.p.p
 *
 * @author
 *
 */
public class ActInserisciFungibilitaCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		//
		//
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella, NP=nuovo
															// Periodo

		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		StatoEsecTitoloCumulatoModel lStatoEsecMod = null;

		if ("I".equals(lModalita)) {
			siesLogger.debug("Sono in INSERIMENTO");

			lStatoEsecMod = this.getDatiProvvedimento("I");

			ComputiCumuloModel lComputiModel = this.getDatiComputo("I");

			// In caso di inserimento va caricato in StatoEsecuzione l'ufficio emittente
			if (super.getDatiTitoloCumulato().getProcedimentoCumulato() != null) {
				lStatoEsecMod.setCodUfficioEmittente(
						super.getDatiTitoloCumulato().getProcedimentoCumulato().getCodUfficioFasCumulato());
				lStatoEsecMod.setCodLuogoEmittente(super.getDatiTitoloCumulato().getProcedimentoCumulato()
						.getCodLuogoUfficioFasCumulato());
			}
			/* BigDecimal IdStatoEsecTitoCum = */lCtrlStatoEsec.ExInserisciFungibilitaCumulo(lStatoEsecMod,
					lComputiModel);
		} else if ("M".equals(lModalita)) {
			siesLogger.debug("Sono in MODIFICA");

			lStatoEsecMod = this.getDatiProvvedimento("M");

			ComputiCumuloModel lComputiModel = this.getDatiComputo("M");

			if (lStatoEsecMod.getFlagStato().equals("I"))
				lStatoEsecMod.setFlagStato("I"); // Resta I
			else
				lStatoEsecMod.setFlagStato("M"); // E' un dato estratto

			lComputiModel.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());

			lCtrlStatoEsec.ExModificaFungibilitaCumulo(lStatoEsecMod, lComputiModel);
		} else if ("NP".equals(lModalita)) {
			siesLogger.debug("Sono in Aggiunta NUOVO PERIODO");

			lStatoEsecMod = lCtrlStatoEsec.ExRicercaStatoEsecTitoloCumulatoById(
					getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));

			lStatoEsecMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lStatoEsecMod.setDataAggiornamento(DateUtils.getSysDate());
			lStatoEsecMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			if (lStatoEsecMod.getFlagStato().equals("I")) {
				lStatoEsecMod.setFlagStato("I"); // Resta I
			} else {
				lStatoEsecMod.setFlagStato("M"); // E' un dato estratto
				lStatoEsecMod.setMotivoModifica("Aggiunto un periodo di fungiilita");
			}

			ComputiCumuloModel lComputiModel = this.getDatiComputo("I");

			lComputiModel.setStatIdStatoEsecTitCum(lStatoEsecMod.getIdStatoEsecTitoloCumulato());

			lCtrlStatoEsec.ExInserisciPeriodoFungibilitaCumulo(lStatoEsecMod, lComputiModel);
		} else if ("C".equals(lModalita)) {
			siesLogger.debug("Sono in CANCELLAZIONE");

			BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter(
					CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			BigDecimal lIdComputo = getRequestBigDecimalParameter(CAMPO_ID_COMPUTI_CUMULO);

			// Devo verificare se cancellare un solo computo o l'intero provvedimento
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			StatoEsecTitoloCumulatoModel StatoEsec = lCtrlStato
					.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStatoEsecuzione);

			if (StatoEsec.getListaComputi().size() == 1) {
				// Un solo computro, cancello tutto
				siesLogger.debug("Elimino l'intero provvedimento di computo: " + lIdStatoEsecuzione);
				lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(lIdStatoEsecuzione, null);

			} else {
				// Ho più computi cancello solo quello indicato sulla request
				siesLogger.debug("Elimino il solo conmputo:" + lIdComputo);
				IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
				lCtrlComputi.ExCancellaComputiCumuloBykey(lIdComputo);
			}
		}

		String lPage = "";
		if ("I".equals(lModalita) || "M".equals(lModalita) || "NP".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadDettaglioFungibilitaCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
					+ "&" + ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ lStatoEsecMod.getIdStatoEsecTitoloCumulato();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActRicercaFungibilitaCumulo" + "&"
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

		siesLogger.debug("Inizio getDatiProvvedimento  " + aTipoOper);
		StatoEsecTitoloCumulatoModel lStaMod = new StatoEsecTitoloCumulatoModel();

		// lStaMod.setIdStatoEsecTitoloCumulato ( getRequestBigDecimalParameter (
		// CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO) );
		lStaMod.setCodTipoEvento("01");
		lStaMod.setCodTipoProvvedimento("04");
		lStaMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
		lStaMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		if ("M".equals(aTipoOper)) {
			lStaMod.setCodUfficioEmittente(getRequestStringParameter(CAMPO_COD_UFFICIO_EMITTENTE + "_PROV"));
			lStaMod.setCodLuogoEmittente(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE + "_PROV"));
		}

		lStaMod.setCodEsito("-");
		lStaMod.setCodEsitoTenore("-");
		// lStaMod.setAnnoProcedimento (null);
		// lStaMod.setProgrProcedimento (null);
		lStaMod.setAnnoProvvedimento(null);
		lStaMod.setProgrProvvedimento(null);

		// Provvedimento su richiesta di
		String codTipoIstante = null;
		if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE))
			codTipoIstante = getRequestStringParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_ISTANTE);

		lStaMod.setCodTipoIstante(codTipoIstante);
		if ("G".equals(codTipoIstante)) {
			// Se Richiesta del Giudeice Esecuzione
			String TipoUfficio = getRequestStringParameter(
					ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF);
			String DescrComune = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF);

			lStaMod.setCodTipoUfficioAltro(TipoUfficio);
			lStaMod.setCodUfficioAltro(getCodUfficioByCodTipoUfficioDescrComune(TipoUfficio, DescrComune));

			lStaMod.setCodLuogoAltro(getCodComuneByDescr(
					getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_LUOGO_PROVV_RIF))
							.getCodComune());

			lStaMod.setSezioneAltro(
					getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF));
			lStaMod.setDataEmissioneAltro(getRequestDateParameter(CAMPO_ANNO_DATA_RICHIESTA,
					CAMPO_MESE_DATA_RICHIESTA, CAMPO_GIORNO_DATA_RICHIESTA));
			lStaMod.setAnnoProcedimento(
					getRequestBigDecimalParameter(ICostantiPresoffertoCumulo.CAMPO_ANNO_PROVV));
			lStaMod.setProgrProcedimento(
					getRequestBigDecimalParameter(ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROVV));
		} else if ("D".equals(codTipoIstante)) {
			// Se Richiesta dell'avvocato difensore
			siesLogger.debug("Su Richiesta Difensore");
		} else {
			siesLogger.debug("Su Richiesta ALTRI");
		}

		if (!isRequestParameterNullObj(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE))
			lStaMod.setNote(getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE));

		siesLogger.debug("Dopo campponote ");

		lStaMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lStaMod.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
		lStaMod.setFlagStato("I");
		lStaMod.setMotivoModifica(null);

		if (!isRequestParameterNullObj(CAMPO_ID_EVENTO_ORIGINE)
				&& !"".equals(getRequestStringParameter(CAMPO_ID_EVENTO_ORIGINE))) {
			lStaMod.setIdEventoOrigine(getRequestBigDecimalParameter(CAMPO_ID_EVENTO_ORIGINE));
		}

		if (!isRequestParameterNullObj(CAMPO_EVE_ID_EVENTO_ORIGINE)
				&& !"".equals(getRequestStringParameter(CAMPO_EVE_ID_EVENTO_ORIGINE))) {
			lStaMod.setEveIdEventoOrigine(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO_ORIGINE));
		}

		if ("I".equals(aTipoOper)) {
			lStaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lStaMod.setDataInserimento(DateUtils.getSysDate());
			lStaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			// La Modifica cambia solo lo stato di "Estratto / Modificato".
			String flagStato = getRequestStringParameter(ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO);
			if ("E".equals(flagStato) || "M".equals(flagStato))
				lStaMod.setFlagStato("M");
			lStaMod.setIdStatoEsecTitoloCumulato(
					getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));
			lStaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lStaMod.setDataAggiornamento(DateUtils.getSysDate());
			lStaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		return lStaMod;
	}

	/**
	 * Recupera i dati del computo cumulo
	 * 
	 * @return
	 * @throws F3BException
	 */
	private ComputiCumuloModel getDatiComputo(String aTipoOper) throws F3BException {

		ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

		// lComputiModel.setIdComputiCumulo (getRequestBigDecimalParameter ( CAMPO_ID_COMPUTI_CUMULO) );

		String lCodMotivo = getRequestStringParameter(CAMPO_COD_MOTIVO);
		if ("0212".equals(lCodMotivo)) // 0212-computo Misura Cautelare Altro Reato art. 657 c.p.p.
			lComputiModel.setCodTipoAnnotazione("006"); // 006-Pena Espiata per Altro Titolo
		else if ("0213".equals(lCodMotivo)) // 0213-computo Pena Detentiva Espiata per Altro Reato
											// (fungibilità) art. 657 c.p.p.
			lComputiModel.setCodTipoAnnotazione("007"); // 007-Pena Espiata Senza Titolo

		// lComputiModel.setCodCausaleComputo ("-"); // Fisso a trittino (res migrava 08)
		lComputiModel.setCodCausaleComputo(getRequestStringParameter(CAMPO_COD_CAUSALE_COMPUTO));

		// Se Provvedimento 0212
		if (getRequestStringParameter(CAMPO_COD_MOTIVO).equals("0212")) {
			// Dati procedimento R.G.P.M.
			if (!isRequestParameterNullObj(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM)
					&& !getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM)
							.equals("")
					&& getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM) != null) {
				lComputiModel.setAnnoRegePM(
						getRequestBigDecimalParameter(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REGE_PM));
				lComputiModel.setNumeroRegePM(
						getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REGE_PM));
			}
			// Autorita e sede
			if (!getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE)
					.equals("-")) {
				lComputiModel.setCodTipoUfficioPM(
						getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_PM_SEDE));
				lComputiModel.setCodSedeUfficioPM(getCodComuneByDescr(
						getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_DESCR_COMUNE_PM_SEDE))
								.getCodComune());
			}
			// -----------------------------------

			// Dati Procedimento B.D.M.C.
			if (!isRequestParameterNullObj(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC)
					&& !getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC)
							.equals("")
					&& getRequestStringParameter(
							ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC) != null) {
				lComputiModel.setAnnoBDMC(
						getRequestBigDecimalParameter(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_FASC_BDMC));
				lComputiModel.setNumeroBDMC(
						getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_NUME_FASC_BDMC));
			}

			// Dati Procedimento Reg. gen
			if (!getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_REG_GEN)
					.equals("-")) {
				lComputiModel.setAnnoRege(
						getRequestBigDecimalParameter(ICostantiMisuraCautelareCumulo.CAMPO_ANNO_REG_GEN));
				lComputiModel.setNumeroRege(
						getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_NUMERO_REG_GEN));
				lComputiModel.setTipoRege(
						getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_TIPO_UFFICIO_REG_GEN));
			}

			// Autorita e sede Reg. gen.
			if (!getRequestStringParameter(ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE)
					.equals("-")) {
				String CodTipoAutoRegGen = getRequestStringParameter(
						ICostantiMisuraCautelareCumulo.CAMPO_AUTORITA_EMITTENTE);
				String DescSedeAutoRegGen = getRequestStringParameter(
						ICostantiMisuraCautelareCumulo.CAMPO_DESCR_AUTORITA_EMITTENTE_LUOGO);

				lComputiModel.setCodTipoAutoritaRege(CodTipoAutoRegGen);
				lComputiModel.setCodLuogoAutoritaRege(getCodComuneByDescr(DescSedeAutoRegGen).getCodComune());
			}

			// Data Ordinanza Misura Cau
			if (!isRequestParameterNullObj(
					ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA)
					&& !getRequestStringParameter(
							ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA).equals("")
					&& getRequestStringParameter(
							ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA) != null) {
				lComputiModel.setDataEmissioneOrdRege(getRequestDateParameter(
						ICostantiMisuraCautelareCumulo.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA,
						ICostantiMisuraCautelareCumulo.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA,
						ICostantiMisuraCautelareCumulo.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA));
			}

		} else if (getRequestStringParameter(CAMPO_COD_MOTIVO).equals("0213")) {
			// Anno e Numero Sentenza di riferimento SIEP
			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA)
					&& !getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA).equals("")
					&& getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA) != null) {
				lComputiModel.setAnnoSentenza(
						getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA));
				lComputiModel.setNumeroSentenza(
						getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA));
			}

			// Data Emissione sentenza
			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO)
					&& !getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO)
							.equals("")
					&& getRequestStringParameter(
							ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO) != null) {
				lComputiModel.setDataSentenza(
						getRequestDateParameter(ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO,
								ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO,
								ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO));
			}

			// Autorita e sede Emittente Sentenza
			if (!getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
					.equals("-")) {
				String CodTipoAutoEmi = getRequestStringParameter(
						ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
				String DescLuogoAutoEmi = getRequestStringParameter(
						ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE);

				lComputiModel.setCodTipoAutoritaEmittente(CodTipoAutoEmi);
				lComputiModel.setCodLuogoEmittente(getCodComuneByDescr(DescLuogoAutoEmi).getCodComune());
			}

			// Anno e Numero riferimento SIEP
			if (!isRequestParameterNullObj(ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP)
					&& !getRequestStringParameter(ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP).equals("")
					&& getRequestStringParameter(ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP) != null) {
				lComputiModel.setChiaveAnnoSIEP(
						getRequestBigDecimalParameter(ICostantiPresoffertoCumulo.CAMPO_ANNO_PROC_SIEP));
				lComputiModel.setChiaveNumeroSIEP(
						getRequestBigDecimalParameter(ICostantiPresoffertoCumulo.CAMPO_NUMERO_PROC_SIEP));
			}
		}

		lComputiModel.setFlagPiuMeno("-"); // Sempre meno

		lComputiModel.setDataReclusioneDa(getRequestDateParameter(CAMPO_ANNO_DATA_RECLUSIONE_DA,
				CAMPO_MESE_DATA_RECLUSIONE_DA, CAMPO_GIORNO_DATA_RECLUSIONE_DA));
		lComputiModel.setDataReclusioneA(getRequestDateParameter(CAMPO_ANNO_DATA_RECLUSIONE_A,
				CAMPO_MESE_DATA_RECLUSIONE_A, CAMPO_GIORNO_DATA_RECLUSIONE_A));

		// Calcolo i Quantum
		CalendarModel lCalMod = new CalendarModel();
		lCalMod.setDataInizio(lComputiModel.getDataReclusioneDa());
		lCalMod.setDataFine(lComputiModel.getDataReclusioneA());

		CalendarUtil lCalUtil = new CalendarUtil();
		lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
		lCalMod = lCalUtil.ricalcolaGAM(lCalMod);

		lComputiModel.setNumAnniReclusione(new BigDecimal(lCalMod.getNumAnni()));
		lComputiModel.setNumMesiReclusione(new BigDecimal(lCalMod.getNumMesi()));
		lComputiModel.setNumGiorniReclusione(new BigDecimal(lCalMod.getNumGiorni()));

		siesLogger.debug("Quantum Calcolati = " + lCalMod.getStringPerStampa());
		if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_RECLUSIONE)) {
			siesLogger.debug("quantum in form...");
			lComputiModel.setNumAnniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
			lComputiModel.setNumMesiReclusione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
			lComputiModel.setNumGiorniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));
		}

		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_MAP)) { // MESSA ALLA PROVA
			lComputiModel.setNumGiorniMap(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_MAP));
		}

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_MISURA)) {
			lComputiModel.setCodTipoMisura(getRequestStringParameter(CAMPO_COD_TIPO_MISURA));
		}

		if (!isRequestParameterNullObj(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lComputiModel.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		}

		if (!isRequestParameterNullObj(CAMPO_ALTRO_LUOGO_DETENZIONE)) {
			lComputiModel.setAltroLuogoDetenzione(getRequestStringParameter(CAMPO_ALTRO_LUOGO_DETENZIONE));
		}

		lComputiModel.setFlagStato("I");
		lComputiModel.setMotivoModifica(null);

		// lComputiModel.setFlagStato ( getRequestStringParameter ( ICostantiComputiCumulo.CAMPO_FLAG_STATO)
		// );
		// lComputiModel.setMotivoModifica ( getRequestStringParameter (
		// ICostantiComputiCumulo.CAMPO_MOTIVO_MODIFICA) );

		lComputiModel.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		lComputiModel.setIstrIdIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		if ("I".equals(aTipoOper)) {
			lComputiModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lComputiModel.setDataInserimento(DateUtils.getSysDate());
			lComputiModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		} else if ("M".equals(aTipoOper)) {
			lComputiModel.setIdComputiCumulo(getRequestBigDecimalParameter(CAMPO_ID_COMPUTI_CUMULO));
			lComputiModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lComputiModel.setDataAggiornamento(DateUtils.getSysDate());
			lComputiModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			if (lComputiModel.getFlagStato().equals("I"))
				lComputiModel.setFlagStato("I"); // Resta I
			else
				lComputiModel.setFlagStato("M"); // E' un dato estratto
		}

		return lComputiModel;
	}

}