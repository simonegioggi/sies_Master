package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.SICOException;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.misurasicurezza.util.InserisciPeriodoAltraMisuraModificaEMS;
import siap.sius.prescrizione.action.ICostantiPrescrizione;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.sanzionesostitutiva.util.InserisciPeriodoAltraSanzioneModificaESS;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciEmissioneDecretoDeposito extends ActionSius implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Variabili di sessione.
	private FascicoloGPModel mFasGPMod = null;
	private String mCodiceOperatore = null;
	private String mCodiceUfficio = null;
	private String mCodiceComune = null;

	private BigDecimal mIdGenProc = null; // Id Generale Procedimento
	private String mCodTipoRegistro = null;
	private Date mOggi = null; // Data bodierna
	private String mCodMagistrato = null; // Cod. Magistrato Relatore
	private String mDescUffMagComp = null; // Descrizione Ufficio Magistrato Competente
	private String mLuogo = null; // Descrizione Luogo della Prova
	private String mDescComuneCSSA = null; // Descrizione Comune CSSA competente
	private TenoreModel[] mTenori = null;

	private EsecuzioneSanzioneSostitutivaModel mEssM = null; // Model Esecuzione Sanzione Sostitutiva
	private EsecuzioneMisuraSicurezzaModel mEmsM = null; // Model Esecuzione Misura Sicurezza

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): inizio");

		String lRetPage = IWebConstants.PG_MESSAGE;
		String lCodUffMagistrato = null; // Codice Ufficio Magistrato Competente
		String lCodUffTDS = null; // Codice Ufficio Tribunale di Sorvegliaqnza Competente
		String lCodUffProcura = null; // Codice Ufficio Procura
		String lNote = null; // Note
		String mOreRagg = null; // Totale ore raggiungimento
		String mIstDet = null; // Istituto Detenzione

		// Inizializzazione data
		mOggi = DateUtils.getSysDate();

		// Si prelevano dati di sessione.
		mCodiceOperatore = getCodUtenteConnesso();
		mCodiceUfficio = getCodUfficioUtenteConnesso();
		mCodiceComune = getCodComuneUtenteConnesso();

		// Si preleva dalla sessione il fascicolo GPModel.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		mFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// Preleva id generale procedimento.
		mIdGenProc = mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (mIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "ID Generale Procedimento non in sessione");

		mCodTipoRegistro = mFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro();

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
		// Il controllo viene già effettuato nell?ActLoad.. ma viene qui ripetuto per sicurezza
		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(mIdGenProc);
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();

		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

		// Si richiama il lock per evitare inserimenti multipli
		lockApplicativo("EmissioneProvvedimento");

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// Prelevare il codice Magistrato_Relatore
		if (lMagRel != null && lMagRel.getMagistrato() != null)
			mCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

		Date lDataEmissione = getRequestDateParameter(CAMPO_DATA_EMISSIONE, "dd/MM/yyyy");
		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();

		try {
			// DepositoDecreto + Evento
			DepositoDecretoEventoModel lDepDecrEveModel = new DepositoDecretoEventoModel();
			lDepDecrEveModel.setDepositoDecreto(generaDecreto(lDataEmissione));
			lDepDecrEveModel.setEvento(generaEvento(lDataEmissione));
			//
			// - Lettura dei campi opzionali -
			//
			if (!isRequestParameterNullObj(CAMPO_NOTE)) {
				// Lettura Note Rilevato
				lNote = getRequestStringParameter(CAMPO_NOTE);
				lDepDecrEveModel.getDepositoDecreto().setNote(lNote);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Lettura Rilevato : " + lNote);
			}

			if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
				// Lettura ufficio del Magistrato Competente
				mDescUffMagComp = getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP);
				if (mDescUffMagComp.trim().length() > 1) {
					// MEV_66: aggiunta gestione UDS/UDSM
					String codTipoUfficio = "UDS";
					if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO) && ("U121"
							.equals(getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO))
							|| "U122".equals(
									getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO))
							|| "U123".equals(
									getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)))) {
						if (!isRequestParameterNullObj(CAMPO_COD_MAGISTRATO))
							codTipoUfficio = getRequestStringParameter(CAMPO_COD_MAGISTRATO);
					}
					lCodUffMagistrato = getCodUfficioByCodTipoUfficioDescrComune(codTipoUfficio,
							mDescUffMagComp);
					lDepDecrEveModel.getDepositoDecreto().setCodUfficioCompetente(lCodUffMagistrato);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cod Ufficio Magistrato Competente: " + lCodUffMagistrato);
				}
			}

			if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_TDS_COMP)) {
				// Lettura Tribunale di Sorveglianza Competente.
				String lDescUff = getRequestStringParameter(CAMPO_COD_UFFICIO_TDS_COMP);
				if (lDescUff.trim().length() > 1) {
					// MEV_66: aggiunta gestione UDS/UDSM
					String codTipoUfficio = "TDS";
					if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO) && ("U121"
							.equals(getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO))
							|| "U122".equals(
									getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO))
							|| "U123".equals(
									getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO))))
						codTipoUfficio = "TDSM";
					lCodUffTDS = getCodUfficioByCodTipoUfficioDescrComune(codTipoUfficio, lDescUff);
					lDepDecrEveModel.getDepositoDecreto().setCodTdsComp(lCodUffTDS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cod Ufficio TDS Competente: " + lCodUffTDS);
				}
			}

			if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_PROCURA_COMP)) {
				// Lettura Procura Competente.
				String lDescUff = getRequestStringParameter(CAMPO_COD_UFFICIO_PROCURA_COMP);
				if (lDescUff.trim().length() > 1) {
					lCodUffProcura = getCodUfficioByCodTipoUfficioDescrComune("PM", lDescUff);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cod Procura Competente: " + lCodUffProcura);
					lDepDecrEveModel.getDepositoDecreto().setCodProcuraEsecuzione(lCodUffProcura);
				}
			}
			if (!isRequestParameterNullObj(CAMPO_LUOGO_SVOLGIMENTO_PROVA)) {
				// Lettura Luogo Svolgimento della Prova.
				mLuogo = getRequestStringParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA);
				lDepDecrEveModel.getDepositoDecreto().setLuogoSvolgimentoProva(mLuogo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cod Procura Competente: " + lCodUffProcura);
			}
			if (!isRequestParameterNullObj(CAMPO_STATUS_PERSONA)) {
				// Lettura Status Persona.
				lDepDecrEveModel.getDepositoDecreto()
						.setStatusPersona(getRequestStringParameter(CAMPO_STATUS_PERSONA));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Lettura Status Persona");
			}
			if (!(isRequestParameterNullObj(CAMPO_ANNO_PROC_REVOCATO)
					|| isRequestParameterNullObj(CAMPO_PROGR_PROC_REVOCATO))) {
				// Lettura Anno e Progressivo Provvedimento Revocato
				String lAnno = getRequestStringParameter(CAMPO_ANNO_PROC_REVOCATO);
				String lProgr = getRequestStringParameter(CAMPO_PROGR_PROC_REVOCATO);

				lDepDecrEveModel.getDepositoDecreto().setAnnoProcRevocato(lAnno);
				lDepDecrEveModel.getDepositoDecreto().setProgrProcRevocato(lProgr);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Lettura Anno e Progressivo Fascicolo Revocato:  " + lAnno + " " + lProgr);
			}
			if (!isRequestParameterNullObj(CAMPO_UFFICIO_PROC_REVOCATO)) {
				// Lettura Sede Provvedimento Revocato
				String lTipoSede = getRequestStringParameter(CAMPO_TIPO_SEDE_REVOCA);
				String lDescUffRevoca = getRequestStringParameter(CAMPO_UFFICIO_PROC_REVOCATO);
				if (lDescUffRevoca.trim().length() > 1) {
					String lCodUffRevoca = getCodUfficioByCodTipoUfficioDescrComune(lTipoSede,
							lDescUffRevoca);
					lDepDecrEveModel.getDepositoDecreto().setUfficioProcRevocato(lCodUffRevoca);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cod Sede Provv. Revocato [" + lTipoSede + "] " + lCodUffRevoca);
				}
			}
			if (!isRequestParameterNullObj(CAMPO_TOT_ORE_RAGGIUNGIMENTO)) {
				// Lettura Ore raggiungimento.
				mOreRagg = getRequestStringParameter(CAMPO_TOT_ORE_RAGGIUNGIMENTO);
				if (mOreRagg.trim().length() > 1) {
					lDepDecrEveModel.getDepositoDecreto().setTotOreRaggiungimento(mOreRagg);
				}
			}
			if (!isRequestParameterNullObj(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
				// Lettura Istituto Detenzione.
				mIstDet = getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
				if (mIstDet.trim().length() > 1) {
					lDepDecrEveModel.getDepositoDecreto().setIstDetIdIstitutoDetenzione(mIstDet);
				}
			}
			// Questura di esecuzione Luigi 31-1-2005
			if (!isRequestParameterNullObj(CAMPO_ALTRI_DESTINATARI)) {
				// Lettura Campo.
				String lComuneQuestura = getRequestStringParameter(CAMPO_ALTRI_DESTINATARI);
				if (lComuneQuestura.trim().length() > 1) {
					// Chiamata solo per controllare l'esistenza del comune
					getCodComuneByDescr(lComuneQuestura);
					lDepDecrEveModel.getDepositoDecreto().setAltriDestinatari(lComuneQuestura.toUpperCase());
				}
			}
			// Ordinanza / Decreto Ricovero OPG
			if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO)) {
				// Lettura Diagnosi
				lNote = getRequestStringParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO);
				lDepDecrEveModel.getDepositoDecreto().setNote(lNote);
			}
			// Decreto /ordinanza Revoca provvedimento
			if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)) {
				// Lettura Ulteriore Descrizione e memorizzazione in Note
				lNote = getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE);
				lDepDecrEveModel.getDepositoDecreto().setNote(lNote);
			}

			// Data Decorrenza Sospensione
			if (!isRequestParameterNullObj(CAMPO_GIORNO_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(CAMPO_MESE_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(CAMPO_ANNO_SOSPENSIONE_SS)) {
				Date lDataDecorrenzaSS = getRequestDateParameter(CAMPO_ANNO_SOSPENSIONE_SS,
						CAMPO_MESE_SOSPENSIONE_SS, CAMPO_GIORNO_SOSPENSIONE_SS);
				lDepDecrEveModel.getDepositoDecreto().setDataSospensioneSS(lDataDecorrenzaSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Decorrenza Sospensione:" + lDataDecorrenzaSS);
			}

			// #### Periodo Sospensione #####
			// ------ Anni Periodo Sospensione ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_AA_SS)) {
				BigDecimal lAnniSospensioneSS = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_AA_SS);
				lDepDecrEveModel.getDepositoDecreto().setSospensioneAASS(lAnniSospensioneSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Anni Periodo Sospensione" + lAnniSospensioneSS);
			}
			// ------ Mesi Periodo Sospensione ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_MM_SS)) {
				BigDecimal lMesiSospensioneSS = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_MM_SS);
				lDepDecrEveModel.getDepositoDecreto().setSospensioneMMSS(lMesiSospensioneSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Mesi Periodo Sospensione" + lMesiSospensioneSS);
			}
			// ------ Giorni Periodo Sospensione ----
			if (!isRequestParameterNullObj(CAMPO_SOSPENSIONE_GG_SS)) {
				BigDecimal lGiorniSospensioneSS = getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_GG_SS);
				lDepDecrEveModel.getDepositoDecreto().setSospensioneGGSS(lGiorniSospensioneSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Giorni Periodo Sospensione" + lGiorniSospensioneSS);
			}

			// Data Fino al
			if (!isRequestParameterNullObj(CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(CAMPO_MESE_SCADENZA_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS)) {
				Date lDataFinoAlSS = getRequestDateParameter(CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS,
						CAMPO_MESE_SCADENZA_SOSPENSIONE_SS, CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS);
				lDepDecrEveModel.getDepositoDecreto().setDataScadenzaSospensioneSS(lDataFinoAlSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fino al:" + lDataFinoAlSS);
			}

			// Flag Recupero SS
			if (!isRequestParameterNullObj(CAMPO_FLAG_RECUPERO_SS)) {
				String lFlagRecupero = getRequestStringParameter(CAMPO_FLAG_RECUPERO_SS);
				lDepDecrEveModel.getDepositoDecreto().setFlagRecuperoSS(lFlagRecupero);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Flag Recupero Sospensione:" + lFlagRecupero);
				// Se il flag recupero è impostato su "da recuperare"(valore= "S") carico anche il campo
				// "Numero Giorni"
				if (lFlagRecupero.equals("S")) {
					// Numero Giorni Recupero
					if (!isRequestParameterNullObj(CAMPO_GIORNI_RECUPERO_SS)) {
						BigDecimal lGiorniRecupero = getRequestBigDecimalParameter(CAMPO_GIORNI_RECUPERO_SS);
						lDepDecrEveModel.getDepositoDecreto().setGiorniRecuperoSS(lGiorniRecupero);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Giorni Recupero Sospensione:" + lGiorniRecupero);
					}
				}
			}

			// Decreto limitazioni e controlli della corrispondenza. //
			// I dati che seguono vengono inseriri rispettivamente nel seguente modo :
			// -> CAMPO_DURATA_PROROGA in SentenzeRiferimento
			// -> CAMPO_NUOVA_SCADENZA_LIM_CTRL in LuogoSvolgimentoProva

			// Lettura Durata della proroga.
			if (!isRequestParameterNullObj(CAMPO_DURATA_PROROGA)) {
				// Lettura Durata della Proroga
				String lDurataProroga = getRequestStringParameter(CAMPO_DURATA_PROROGA);
				lDepDecrEveModel.getDepositoDecreto().setSentenzeRiferimento(lDurataProroga);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Durata della proroga : " + lDurataProroga);
			}
			// Lettura Nuova Scadenza Limitazione Controlli.
			if (!isRequestParameterNullObj(CAMPO_NUOVA_SCADENZA_LIM_CTRL)) {
				// Lettura Nuova scadenza limitazione e controlli.
				String lNuovaScadenza = getRequestStringParameter(CAMPO_NUOVA_SCADENZA_LIM_CTRL);
				lDepDecrEveModel.getDepositoDecreto().setLuogoSvolgimentoProva(lNuovaScadenza);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nuova scadenza limitazioni/controllo : " + lNuovaScadenza);
			}

			// 20140605 - Tipo Controllo Esecuzione
			if (!isRequestParameterNullObj(CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE))
				lDepDecrEveModel.getDepositoDecreto().setCodTipoControlloEsecuzione(
						getRequestStringParameters(CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE)[0]);

			// 05/06/2014 d.f. aggiunti i campi della Richiesta Ottemperanza
			if (!isRequestParameterNullObj(CAMPO_FLAG_NOMINA_COMM_ACTA)) {
				lDepDecrEveModel.getDepositoDecreto()
						.setFlagNominaComActa(getRequestStringParameter(CAMPO_FLAG_NOMINA_COMM_ACTA));
			}

			if (!isRequestParameterNullObj(CAMPO_DESCR_COMM_ACTA)) {
				lDepDecrEveModel.getDepositoDecreto()
						.setDescrCommActa(getRequestStringParameter(CAMPO_DESCR_COMM_ACTA));
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Deposito Decreto = "+lDepDecrEveModel.getDepositoDecreto());

			// Inserisce nel model aggregante l'Array di model dei Tenori e Il model
			// GeneraleProcedimento.
			GPTenoreModel lGPTenoreModel = new GPTenoreModel();
			lGPTenoreModel.setTenori(generaTenori());
			lGPTenoreModel.setGeneraleProcedimentoModel(generaProcedimento());

			// Inserimento Decreto

			/*
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("--------- [ dati dell'evento ] ---------"); // [FT] -
			 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("Evento : " + lDepDecrEveModel.getEvento()); // [FT] -
			 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("----------------------------------------");
			 *
			 *
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("##### generaProcedimento() : " + generaProcedimento());
			 */

			if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto().compareTo(REVOCA_PERMESSO) == 0)
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoRevocaPermesso(lGPTenoreModel,
						lDepDecrEveModel,
						generaRevocaPermesso(ICostantiLicenzaLibanticipata.REVOCA_PERMESSO));
			else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto()
					.compareTo(ESCLUSIONE_COMPUTO) == 0)
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoRevocaPermesso(lGPTenoreModel,
						lDepDecrEveModel,
						generaRevocaPermesso(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_PERMESSO));
			else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto()
					.compareTo(ESCLUSIONE_COMPUTO_LICENZA) == 0)
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoRevocaPermesso(lGPTenoreModel,
						lDepDecrEveModel,
						generaRevocaPermesso(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_LICENZA));
			else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto().compareTo(REVOCA_LICENZA) == 0)
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoRevocaPermesso(lGPTenoreModel,
						lDepDecrEveModel, generaRevocaPermesso(ICostantiLicenzaLibanticipata.REVOCA_LICENZA));
			// 30/08/2007 Inserimento Decreto di Licenza o Permesso reso Transazionale.
			else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto().compareTo(LICENZA) == 0)
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoLicenzaoPermesso(lGPTenoreModel,
						lDepDecrEveModel, caricaLicenza(lDepDecrEveModel.getEvento().getIdEvento()));
			else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto().compareTo(PERMESSO) == 0)
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoLicenzaoPermesso(lGPTenoreModel,
						lDepDecrEveModel, caricaPermesso(lDepDecrEveModel.getEvento().getIdEvento()));
			else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto()
					.compareTo(SOSPENSIONE_ESECUZIONE_SS) == 0) {
				// Richiama la funzione di "util" per Inserire Periodo Altra Sanzione e Modifica Esecuzione
				// Sanzione Sostitutiva
				InserisciPeriodoAltraSanzioneModificaESS lPASmESS = new InserisciPeriodoAltraSanzioneModificaESS();
				// passa alla funzione di "util" i dati della Request e della Session
				lPASmESS.setReqSes(getRequest(), getSession());
				// esegue la funzione per popolare i model del Periodo Altra Sanzione e Esecuzione Sanzione
				// Sostitutiva
				PeriodoAltraSanzioneModel lPASMod = lPASmESS.caricaPeriodoAltraSanzioneModESS(mFasGPMod);
				// Carica model dell'Esecuzione Sanzione Sostitutiva
				mEssM = lPASmESS.getESS();

				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoPeriodoAltraSanzione(lGPTenoreModel,
						lDepDecrEveModel, lPASMod, mEssM);
			} else if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto()
					.compareTo(SOSPENSIONE_ESECUZIONE_MS) == 0) {
				// Richiama la funzione di "util" per Inserire Periodo Altra Misura e Modifica Esecuzione
				// Misura Sicurezza
				InserisciPeriodoAltraMisuraModificaEMS lPAMmEMS = new InserisciPeriodoAltraMisuraModificaEMS();
				// passa alla funzione di "util" i dati della Request e della Session
				lPAMmEMS.setReqSes(getRequest(), getSession());
				// esegue la funzione per popolare i model del Periodo Altra Misura e Esecuzione Misura
				// Sicurezza
				PeriodoAltraMisuraModel lPAMMod = lPAMmEMS.caricaPeriodoAltraMisuraModEMS(mFasGPMod);
				// Carica model dell'Esecuzione Misura Sicurezza
				mEmsM = lPAMmEMS.getEMS();

				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecretoPeriodoAltraMisura(lGPTenoreModel,
						lDepDecrEveModel, lPAMMod, mEmsM);
			} else {
				lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecreto(lGPTenoreModel, lDepDecrEveModel, null);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto -> " + lDepDecrEveModel.getDepositoDecreto());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento -> " + lDepDecrEveModel.getEvento());

			if (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto()
					.compareTo(MODIFICA_PRESCRIZIONI) == 0
					|| lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto()
							.compareTo(MODIFICA_PRESCRIZIONI_MS) == 0) {
				// In questo caso viene inserita una Prescrizione Libera
				inserisciPrescrizione(lDepDecrEveModel.getEvento().getIdEvento());
			}

			// MEV63: aggiunto codice
			String codOggettoProcedimento = mFasGPMod.getGeneraleProcedimentoModel()
					.getCodOggettoProcedimento();
			if ((codOggettoProcedimento
					.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA))
					&& super.isUserUDSM()) {
				elaboraMisuraAlternativa(lGPTenoreModel, lDepDecrEveModel);
			}

			/*
			 * 30/08/2007 Abolito per impostare la transazionalità dell'azione. // In caso di Decreto
			 * Permesso, occorre inserire un'occorrenza in LICENZA_LIBANTICIPATA. if
			 * ((lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto().compareTo(PERMESSO) == 0))
			 * inserisciPermesso(lDepDecrEveModel.getEvento().getIdEvento());
			 *
			 * // In caso di Decreto Licenza, occorre inserire un'occorrenza in LICENZA_LIBANTICIPATA. else if
			 * (lDepDecrEveModel.getDepositoDecreto().getCodTipoDecreto().compareTo(LICENZA) == 0)
			 * inserisciLicenza(lDepDecrEveModel.getEvento().getIdEvento());
			 */

			// Aggiornamento dei dati in sessione.
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			mFasGPMod = lFasCtrl
					.ExRicercaFascicoloByKey(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			setSessionAttribute("fascicoloSiusGP", mFasGPMod);

			// Preparazione della pagina di destinazione
			// Apre la pagina delle Prescrizioni.
			if (!isRequestParameterNullObj(CAMPO_CK_PRESCRIZIONI)
					&& isRequestChecked(CAMPO_CK_PRESCRIZIONI)) {
				// Per Sanzioni Sostitutive
				if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
						&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
								.equals("U059")
						|| mCodTipoRegistro.equals("S09")) {
					RedirectTo lRedirectTo = new RedirectTo();
					lRedirectTo.setPage(IWebConstants.PG_MAIN);
					lRedirectTo.setAction("siap.sius.prescrizione.action.ActLoadInserisciPrescrizioneNew");
					lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
							lDepDecrEveModel.getEvento().getIdEvento().toString());
					lRedirectTo.setParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO,
							getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
					lRetPage = lRedirectTo.toString();
				} else {
					lRetPage = ICostantiPrescrizione.PG_LOAD_INSERISCIPRESCRIZIONE;
				}

				// Passaggio di dati alla jsp
				setRequestAttribute("UffMagComp", mDescUffMagComp);
				setRequestAttribute("LuogoProva", mLuogo);
				setRequestAttribute("ComuneCSSA", mDescComuneCSSA);
				setRequestAttribute("IDEvento", lDepDecrEveModel.getEvento().getIdEvento().toString());
				setRequestAttribute("modalita", "I");
				setRequestAttribute("nextaction",
						"siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");
			} else {
				// dettaglio del decreto
				RedirectTo lRedirectTo = new RedirectTo();
				lRedirectTo.setPage(IWebConstants.PG_MAIN);
				lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");
				lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
						lDepDecrEveModel.getEvento().getIdEvento().toString());
				lRetPage = lRedirectTo.toString();
			}
		} catch (SICOException daoex) {
			if (daoex.getErrorCode() == F3BException.USER_MESSAGE) {
				lRetPage = IWebConstants.PG_MESSAGE;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, daoex.getMessage());
			} else
				throw daoex;
		} catch (Exception e) {
			throw e;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): fine");

		return lRetPage;
	}

	private GeneraleProcedimentoModel generaProcedimento() throws F3BException {

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
		lGenProcModel.setIdGeneraleProcedimento(mIdGenProc);
		lGenProcModel.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGenProcModel.setDataAggiornamento(mOggi);
		lGenProcModel.setCodUfficioAggiornamento(mCodiceUfficio);
		lGenProcModel.setCodOperatoreAggiornamento(mCodiceOperatore);

		return lGenProcModel;
	}

	private TenoreModel[] generaTenori() throws F3BException {

		// Lettura campi Tenore.
		String[] lCodOggetti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);
		String[] lDescOggetti = getRequestStringParameters(ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE);
		String[] lCodDettaglioOggetti = getRequestStringParameters(
				ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO);
		String[] lCodEsiti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);

		if (!((lCodOggetti.length == lDescOggetti.length)
				&& (lDescOggetti.length == lCodDettaglioOggetti.length)
				&& (lCodDettaglioOggetti.length == lCodEsiti.length)))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nella lettura dei Tenori");

		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();

		int lSizeArray = lCodOggetti.length;
		if (lSizeArray > 0) {
			mTenori = new TenoreModel[lSizeArray];
			for (int i = 0; i < lSizeArray; i++) {
				TenoreModel lTenModel = new TenoreModel();

				lTenModel.setProgrTenore(new BigDecimal((double) (i + 1)));
				lTenModel.setCodOggettoTenore(lCodOggetti[i]);
				lTenModel.setDescrOggettoTenore(lDescOggetti[i]);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioOggetti[i]);
				lTenModel.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(lCodEsiti[i]));

				// lTenModel.setCodEsitoTenore(lCodEsiti[i]);
				lTenModel.setCodUfficioInserimento(mCodiceUfficio); // Codice dell'ufficio dell'operatore che
																	// inserisce
				lTenModel.setCodOperatoreInserimento(mCodiceOperatore); // Codice dell'operatore che inserisce
				lTenModel.setDataInserimento(mOggi);
				lTenModel.setGenPridGeneraleProcedimento(mIdGenProc);
				lTenModel.setCodMagistrato(mCodMagistrato);

				// Inserimenti i-esimo Tenore
				mTenori[i] = lTenModel;
			}
		}
		return mTenori;
	}

	private DepositoDecretoModel generaDecreto(Date aDataEmissione) throws F3BException {

		// Prepara il model DepositoDecreto.
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel();
		lDepDecrModel.setDataEmissione(aDataEmissione);
		lDepDecrModel.setCodTipoDecreto(getRequestStringParameter(CAMPO_COD_TIPO_DECRETO));
		// lDepDecrModel.setSentenzeRiferimento( getRequestStringParameter(
		// ICostantiDepositoDecreto.CAMPO_SENTENZA_RIFERIMENTO ) );
		// lDepDecrModel.setAltriDestinatari( getRequestStringParameter(
		// ICostantiDepositoDecreto.CAMPO_ALTRI_DESTINATARI ) );
		lDepDecrModel.setCodMagistrato(mCodMagistrato);
		lDepDecrModel.setGenPridGeneraleProcedimento(mIdGenProc);
		lDepDecrModel.setCodOperatoreInserimento(mCodiceOperatore);
		lDepDecrModel.setCodUfficioInserimento(mCodiceUfficio);
		lDepDecrModel.setDataInserimento(mOggi);

		if (lDepDecrModel.getCodTipoDecreto().compareTo(ESPULSIONE) == 0) {
			// Nel caso decreto di espulsione viene valorizzato il TdS
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUff = lUffCtrl.getUfficioUDSTDS(getCodDistrettoUtenteConnesso(), "TDS",
					getCodComuneUtenteConnesso());
			lDepDecrModel.setCodTdsComp(lUff.getCodUfficio());
		}
		return lDepDecrModel;
	}

	private EventoModel generaEvento(Date aDataEmissione) throws F3BException {

		// Prepara Model Evento.
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento("01"); // 01 = Provvedimento.
		lEvento.setCodTipoProvvedimento("02"); // 02 = Decreto.
		lEvento.setCodLuogoEmittente(mCodiceComune);
		lEvento.setCodUfficioEmittente(mCodiceUfficio);
		lEvento.setCodEsito("-");
		lEvento.setDataEmissione(aDataEmissione);

		lEvento.setFasSieIdFascicoloSiep(mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEvento.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEvento.setCodOperatoreInserimento(mCodiceOperatore);
		lEvento.setCodUfficioInserimento(mCodiceUfficio);
		lEvento.setDataInserimento(mOggi);
		lEvento.setCodLuogoDestinatario("-");
		lEvento.setCodTipoUfficioDestinatario("-");
		lEvento.setCodUfficioDestinatario("-");
		lEvento.setCodMagistrato(mCodMagistrato);
		// Inserimento dell'Evento collegato (Decreto Revocato)
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_EVE_ID_EVENTO))
			lEvento.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));

		return lEvento;
	}

	// 30/08/2007 Abolito per impostare la transazionalità dell'azione.
	private void inserisciPrescrizione(BigDecimal aIdEvento) throws F3BException {

		// Inizializzazione Prescrizione.
		PrescrizioneModel lPreMod = new PrescrizioneModel();
		lPreMod.setCodOperatoreInserimento(mCodiceOperatore);
		lPreMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPreMod.setDataInserimento(mOggi);
		lPreMod.setEveIdEve(aIdEvento);
		lPreMod.setCodTipoPrescrizione("90"); // Prescrizione di tipo GENERICO
		lPreMod.setDescrAltraPrescrizione(
				getRequestStringParameter(ICostantiPrescrizione.CAMPO_DESCR_ALTRA_PRESCRIZIONE));
		lPreMod.setCodLuogoAffidamento("-");
		lPreMod.setIdCssaCompetente(new BigDecimal("9999"));
		lPreMod.setCodLuogoAutorizzato("-");
		lPreMod.setCodProvinciaAutorizzata("-");
		lPreMod.setCodUffMagistratoCompetente("-");
		// Inserimento.
		IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		lCtrl.ExInserisciPrescrizione(lPreMod);
	}

	// 30/08/2007 Realizzato per impostare la transazionalità dell'azione.
	private LicenzaLibAnticipataModel caricaPermesso(BigDecimal aIdEvento) throws F3BException {

		// Inizializzazione LicenzaLibAnticipata.
		LicenzaLibAnticipataModel lLicMod = generaLicenza(aIdEvento);

		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug("--------- [ dati Generale Procedimento ] ---------"); // [FT]
		 * - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("Generale Procedimento : " + mFasGPMod.getGeneraleProcedimentoModel()); // [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("--------------------------------------------------");
		 */

		// 20110523 - PM :
		// Condizione per individuazione il tipo di permesso (Permesso Premio, Permesso Internato).
		//
		if (!mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U090"))
			lLicMod.setCodTipoLicenza("PP"); // Permesso Premio
		else
			lLicMod.setCodTipoLicenza("PI"); // Permesso Internato

		// TODO carmela verificare
		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI).trim()
						.length() > 0)
			lLicMod.setNumeroMesi(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI)));

		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI).trim()
						.length() > 0)
			lLicMod.setNumeroGiorni(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI)));

		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE).trim()
						.length() > 0)
			lLicMod.setNumeroOre(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)));

		lLicMod.setFlagScorta(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_FLAG_SCORTA));
		lLicMod.setLuogoSvolgimentoProva(
				getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_LUOGO_SVOLGIMENTO_PROVA));
		lLicMod.setCodStatoPermesso(
				getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_COD_STATO_PERMESSO));
		lLicMod.setDescrStatoPermesso(
				getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_DESCR_STATO_PERMESSO));

		// Restituisce il Risultato.
		return lLicMod;
	}

	/*
	 * 30/08/2007 Abolito per impostare la transazionalità dell'azione. private void
	 * inserisciPermesso(BigDecimal aIdEvento) throws F3BException { LicenzaLibAnticipataModel lLicMod =
	 * generaLicenza(aIdEvento);
	 *
	 * lLicMod.setCodTipoLicenza("PP");
	 *
	 * if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI) &&
	 * getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI).trim().length()>0 )
	 * lLicMod.setNumeroGiorni(new
	 * BigDecimal(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI)) );
	 *
	 * if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE) &&
	 * getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE).trim().length()>0 )
	 * lLicMod.setNumeroOre(new
	 * BigDecimal(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)) );
	 *
	 * lLicMod.setFlagScorta(getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_FLAG_SCORTA) );
	 * lLicMod.setLuogoSvolgimentoProva(getRequestStringParameter(ICostantiLicenzaLibanticipata.
	 * CAMPO_LUOGO_SVOLGIMENTO_PROVA) );
	 * lLicMod.setCodStatoPermesso(getRequestStringParameter(ICostantiLicenzaLibanticipata
	 * .CAMPO_COD_STATO_PERMESSO) );
	 * lLicMod.setDescrStatoPermesso(getRequestStringParameter(ICostantiLicenzaLibanticipata
	 * .CAMPO_DESCR_STATO_PERMESSO) );
	 *
	 * // Inserimento. ILicenzaPeriodiLibAnticipata lCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
	 * lLicMod = lCtrl.ExInserisciLicenzaLibanticipata(lLicMod); }
	 */

	private LicenzaLibAnticipataModel generaLicenza(BigDecimal aIdEvento) throws F3BException {

		// Inizializzazione LicenzaLibAnticipata con i valori comuni
		LicenzaLibAnticipataModel lLicMod = new LicenzaLibAnticipataModel();
		lLicMod.setCodOperatoreInserimento(mCodiceOperatore);
		lLicMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicMod.setDataInserimento(mOggi);
		lLicMod.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lLicMod.setFasSieIdFascicoloSiep(mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lLicMod.setEveIdEvento(aIdEvento);
		return lLicMod;
	}

	// 30/08/2007 Realizzato per impostare la transazionalità dell'azione.
	private LicenzaLibAnticipataModel caricaLicenza(BigDecimal aIdEvento) throws F3BException {

		String lOraInizio = null;
		String lOraFine = null;

		// Inizializzazione LicenzaLibAnticipata.
		LicenzaLibAnticipataModel lLicenza = generaLicenza(aIdEvento);

		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug("CodOggettoProcedimento : " +
		 * mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento() ); // [FT] - 03/08/2016 -
		 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("COD_CONTENUTO : " + getRequestStringParameter(
		 * ICostantiFascicoloSius.CAMPO_COD_CONTENUTO ) );
		 */
		// 20110520
		// Esegue controllo per discriminare il tipo di licenza.
		// Questo controllo verrà eseguito utilizzando il CodiceOggettoProcedimento pari a U068
		if (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U068"))
			lLicenza.setCodTipoLicenza("LI"); // Licenza Internati
		else
			lLicenza.setCodTipoLicenza("LC"); // Licenza

		lLicenza.setFlagConcesso("C");
		// TODO carmela verificare
		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI).trim()
						.length() > 0)
			lLicenza.setNumeroMesi(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_MESI)));
		lLicenza.setNumeroGiorni(
				getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI));
		// 30/08/2007 Numero Ore non sempre valorizzato.
		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)
				&& getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE).trim()
						.length() > 0)
			lLicenza.setNumeroOre(new BigDecimal(
					getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE)));
		lLicenza.setLuogoSvolgimentoProva(getRequestStringParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));

		// Preleva data di inizio licenza
		Date lDataInizio = getRequestDateParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_INIZIO,
				ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_INIZIO,
				ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_INIZIO);

		// Preleva data di fine licenza
		Date lDataFine = getRequestDateParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_FINE,
				ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_FINE,
				ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_FINE);

		lLicenza.setDataInizio(lDataInizio);
		lLicenza.setDataFine(lDataFine);

		lOraInizio = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ORA_INIZIO);
		lOraFine = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ORA_FINE);

		if (lOraInizio.trim().length() > 0) {
			lOraInizio += "," + getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO);
			lLicenza.setOraInizio(lOraInizio);
		}
		if (lOraFine.trim().length() > 0) {
			lOraFine += "," + getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE);
			lLicenza.setOraFine(lOraFine);
		}
		// Restituisce il Risultato.
		return lLicenza;
	}

	/*
	 * 30/08/2007 Abolito per impostare la transazionalità dell'azione. private void
	 * inserisciLicenza(BigDecimal aIdEvento) throws F3BException { String lOraInizio = null; String lOraFine
	 * = null;
	 *
	 * // Inizializzazione LicenzaLibAnticipata. LicenzaLibAnticipataModel lLicenza =
	 * generaLicenza(aIdEvento);
	 *
	 * lLicenza.setCodTipoLicenza("LC");
	 *
	 * lLicenza.setFlagConcesso("C");
	 * lLicenza.setNumeroGiorni(getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata
	 * .CAMPO_NUMERO_GIORNI));
	 * lLicenza.setNumeroOre(getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE));
	 * lLicenza.setLuogoSvolgimentoProva(getRequestStringParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));
	 *
	 * // Preleva data di inizio licenza Date lDataInizio = getRequestDateParameter(
	 * ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_INIZIO,
	 * ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_INIZIO,
	 * ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_INIZIO ); //Date lDataInizio =
	 * getRequestDateTimeParameter( CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO ,
	 * CAMPO_ORA_INIZIO,CAMPO_MINUTI_INIZIO);
	 *
	 * // Preleva data di fine licenza Date lDataFine = getRequestDateParameter(
	 * ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_FINE, ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_FINE,
	 * ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_FINE ); //Date lDataFine = getRequestDateTimeParameter(
	 * CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE , CAMPO_ORA_FINE,
	 * CAMPO_MINUTI_FINE);
	 *
	 * lLicenza.setDataInizio(lDataInizio); lLicenza.setDataFine(lDataFine);
	 *
	 * lOraInizio = getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ORA_INIZIO); lOraFine =
	 * getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ORA_FINE);
	 *
	 * if (lOraInizio.trim().length() > 0) { lOraInizio += "," +
	 * getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_MINUTI_INIZIO);
	 * lLicenza.setOraInizio(lOraInizio); } if (lOraFine.trim().length() > 0) { lOraFine += "," +
	 * getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_MINUTI_FINE);
	 * lLicenza.setOraFine(lOraFine); }
	 *
	 * // Inserimento. ILicenzaPeriodiLibAnticipata lCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
	 * lLicenza = lCtrl.ExInserisciLicenzaLibanticipata(lLicenza); }
	 */

	private LicenzaLibAnticipataModel generaRevocaPermesso(String acodLicenza) throws F3BException {

		// Cod Esito da testare nel caso di Revoca Permesso / Esclusione Computo Permesso
		String lcodEsito = "0006";
		if ((acodLicenza.compareTo(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_LICENZA) == 0)
				|| (acodLicenza.compareTo(ICostantiLicenzaLibanticipata.ESCLUSIONE_COMPUTO_PERMESSO) == 0))
			lcodEsito = "0013";
		// Inizializzazione LicenzaLibAnticipata con i valori comuni
		LicenzaLibAnticipataModel lLicMod = new LicenzaLibAnticipataModel();
		lLicMod.setCodOperatoreInserimento(mCodiceOperatore);
		lLicMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicMod.setDataInserimento(mOggi);
		lLicMod.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lLicMod.setFasSieIdFascicoloSiep(mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lLicMod.setCodTipoLicenza(acodLicenza);
		lLicMod.setNumeroGiorni(
				getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_GIORNI_SCOMPUTO));
		// STUB 19/07/2007 Controllo per Ins. ore scomputo.
		if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_SCOMPUTO))
			lLicMod.setNumeroOre(
					getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORE_SCOMPUTO));

		if (isEsitoTenoriRevoca(lcodEsito))
			lLicMod.setFlagScomputo("S");
		else
			lLicMod.setFlagScomputo("N");
		// lLicMod.setLuogoSvolgimentoProva("-");
		// lLicMod.setCodStatoPermesso("99");
		// lLicMod.setEveIdEvento(aIdEvento);
		return lLicMod;
	}

	// La f.ne controlla che tutti gli esiti siano "REVOCA"
	private boolean isEsitoTenoriRevoca(String acodEsito) {

		boolean lRetValue = true;
		for (int i = 0; i < mTenori.length; i++) {
			if (mTenori[0].getCodEsitoTenore().compareTo(acodEsito) != 0) {
				lRetValue = false;
				break;
			}
		}
		return lRetValue;
	}

	private void elaboraMisuraAlternativa(GPTenoreModel lOrdEveTenGP,
			DepositoDecretoEventoModel lDepDecrEveModel) throws F3BException {

		TenoreModel tenori[] = lOrdEveTenGP.getTenori();

		IMisuraAlternativa lCtrl = SIEPLookupRemote.getMisuraAlternativaRemote();
		for (TenoreModel tenore : tenori) {
			String codEsitoTenore = tenore.getCodEsitoTenore();
			if (ICostantiMisuraAlternativa.COD_ESITO_CONCEDE.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraAlternativa.COD_ESITO_CONCEDE_SOSPENSIONE_PENA_DETENZIONE_CASA
							.equalsIgnoreCase(codEsitoTenore)) {
				MisuraAlternativaModel misuraAlternativaModel = new MisuraAlternativaModel();

				BigDecimal idEvento = lDepDecrEveModel.getEvento().getIdEvento();
				misuraAlternativaModel.setEveIdEvento(idEvento);
				misuraAlternativaModel.setDescrizioneComunita(
						getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
				misuraAlternativaModel.setCodTipoDecisione("03");
				misuraAlternativaModel.setCodNaturaDecisione("CO");
				String flagFormaMisura = getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA);

				if (flagFormaMisura != null)
					misuraAlternativaModel.setFlFormaMisura(new BigDecimal(flagFormaMisura));

				misuraAlternativaModel.setCodTipoMisura(tenore.getCodOggettoTenore());
				misuraAlternativaModel.setNumAnniMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM));
				misuraAlternativaModel.setNumMesiMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM));
				misuraAlternativaModel.setNumGiorniMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM));
				misuraAlternativaModel
						.setChiaveAnnoFascicoloSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
				misuraAlternativaModel
						.setChiaveProgrFascicoloSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr());
				misuraAlternativaModel.setCodUfficioInserimento(
						mFasGPMod.getFascicoloSiusModel().getCodUfficioInserimento());
				misuraAlternativaModel.setCodOperatoreInserimento(
						mFasGPMod.getFascicoloSiusModel().getCodOperatoreInserimento());
				misuraAlternativaModel
						.setCodMagistrato(lDepDecrEveModel.getDepositoDecreto().getCodMagistrato());
				misuraAlternativaModel.setFasSieIdFascicoloSiep(
						mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				misuraAlternativaModel.setDescrLuogoProva(getParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));
				misuraAlternativaModel.setCodTipoUfficioScarcerazione("-");
				misuraAlternativaModel.setCodUfficioSorveglianza("-");
				misuraAlternativaModel.setCodAutoritaAltroTitolo("-");
				// MEV_62 [EC] 15/05/2018 - INIZIO
				misuraAlternativaModel.setChiaveUfficioFascicoloSius(
						mFasGPMod.getFascicoloSiusModel().getCodUfficioInserimento());

				if (lDepDecrEveModel.getDepositoDecreto().getDataEmissione() != null) {
					misuraAlternativaModel
							.setDataDecisione(lDepDecrEveModel.getDepositoDecreto().getDataEmissione());
				}
				if (lDepDecrEveModel.getDepositoDecreto().getAnnoS72() != null)
					misuraAlternativaModel
							.setAnnoRegistro(lDepDecrEveModel.getDepositoDecreto().getAnnoS72());
				if (lDepDecrEveModel.getDepositoDecreto().getNumS72() != null)
					misuraAlternativaModel
							.setNumeroRegistro(lDepDecrEveModel.getDepositoDecreto().getNumS72());
				// MEV 62 [EC] 15/05/2018 - FINE
				lCtrl.ExInserisciMisuraAlternativa(misuraAlternativaModel);
			}
		}
	}

	private String getStringParameter(String paramName) throws F3BException {

		if (isRequestParameterNullObj(paramName))
			return null;
		String val = super.getRequestStringParameter(paramName);
		return val;
	}

	private BigDecimal getBigDecimalParameter(String paramName) throws F3BException {

		if (isRequestParameterNullObj(paramName))
			return null;
		BigDecimal val = super.getRequestBigDecimalParameter(paramName);
		return val;
	}

}