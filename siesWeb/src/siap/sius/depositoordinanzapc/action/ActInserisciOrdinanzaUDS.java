package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.SICOException;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.controller.MisuraSicurezzaController;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penaaccessoria.action.ICostantiPenaAccessoria;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.depositoordinanzapc.util.GestioneFlussoOrdinanza;
import siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza;
import siap.sius.prescrizione.action.ICostantiPrescrizione;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

@SuppressWarnings("rawtypes")
public class ActInserisciOrdinanzaUDS extends ActionSius implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Variabili di sessione.
	protected FascicoloGPModel mFasGPMod = null;
	private String mCodiceOperatore = null;
	private String mCodiceUfficio = null;
	private String mCodiceComune = null;
	// Id Generale Procedimento
	private BigDecimal mIdGenProc = null;
	private String mCodTipoRegistro = null;
	// Data odierna
	protected Date mOggi = null;
	// Cod. Magistrato Relatore
	private String mCodMagistrato = null;
	// Descrizione Luogo della Prova
	private String mLuogo = null;
	// Descrizione Comune CSSA competente
	private String mDescComuneCSSA = null;
	// Descrizione Comune USSM competente
	private String mDescComuneUSSM = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciOrdinanzaUDS");
		String lRetPage = IWebConstants.PG_MESSAGE;

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

		// recupero l'identificativo del fascicolo SIUS per il quale si sta emettendo l'ordinanza
		BigDecimal idFascicoloSius = mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Preleva id generale procedimento.
		mIdGenProc = mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (mIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "ID Generale Procedimento non in sessione");

		mCodTipoRegistro = mFasGPMod.getGeneraleProcedimentoModel().getCodTipoRegistro();

		// MEV_32 - Modifica del 14/11/2016
		// Deve essere reso obbligatorio l'indicazione della Misura di Sicurezza per
		// i Procedimenti con contenuto "Applicazione Misura di Sicurezza" (U023)
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
				&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U023")) {
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
			aMisuraSicurezza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
			Vector lVect = lCtrl.ExRicercaMisuraSicurezzaAndRifTitoloEsec(aMisuraSicurezza);
			// @emma 16072018 intervento post COLLAUDO 11.2 (a misura di sicurezza è obbligatoria solo per
			// alcuni codici esito)
			if (lVect.size() == 0) {
				// recupero gli esiti/esito inserito
				String[] esiti = new String[] { "1190", "1191", "1198", "1990", "1991", "1992", "1993",
						"1206", "2720" };
				if (!isRequestParameterNullObj(ICostantiTenore.CAMPO_COD_ESITO_TENORE)) {
					String[] lCodEsiti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);
					if (lCodEsiti.length > 0) {
						for (int i = 0; i < lCodEsiti.length; i++) {
							if (Arrays.binarySearch(esiti, lCodEsiti[i]) >= 0) {
								throw new SIUSException(SIUSException.USER_MESSAGE,
										"E' obbligatorio inserire la Misura di Sicurezza attraverso il link presente nella funzionalità.");
							}
						}
					}
				}
				// throw new SIUSException(SIUSException.USER_MESSAGE,
				// "E' obbligatorio inserire la Misura di Sicurezza attraverso il link presente nella
				// funzionalità.");
			}
		}

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
		// Il controllo viene già effettuato nell'ActLoad.. ma viene qui ripetuto per sicurezza
		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(mIdGenProc);
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

		// Si richiama il lock per evitare inserimenti multipli per evitare inserimenti multipli
		lockApplicativo("EmissioneProvvedimento");

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// Prelevare il codice Magistrato_Relatore
		if (lMagRel != null && lMagRel.getMagistrato() != null)
			mCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

		Date lDataEmissione = getRequestDateParameter(CAMPO_DATA_EMISSIONE, "dd/MM/yyyy");

		try {
			OrdinanzaEventoTenoriGProcModel lOrdEveTenGP = new OrdinanzaEventoTenoriGProcModel();
			lOrdEveTenGP.setEvento(generaEvento(lDataEmissione));
			lOrdEveTenGP.setOrdinanza(generaOrdinanza(lDataEmissione));
			lOrdEveTenGP.setTenori(generaTenori());
			lOrdEveTenGP.setGeneraleProcedimento(generaProcedimento());
			// Lettura di ulteriori dati

			lOrdEveTenGP = generaDati(lOrdEveTenGP);

			lOrdEveTenGP = inserimento(lOrdEveTenGP);

			// l'aggiornamento dei dati in sessione
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			mFasGPMod = lFasCtrl
					.ExRicercaFascicoloByKey(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			setSessionAttribute("fascicoloSiusGP", mFasGPMod);

			String codOggettoProcedimento = mFasGPMod.getGeneraleProcedimentoModel()
					.getCodOggettoProcedimento();
			if ((codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_PROC_CONCESSIONE_MISURE_ALTERNATIVA)
					|| codOggettoProcedimento
							.equalsIgnoreCase(COD_OGGETTO_PROC_CONCESSIONE_LIBERAZIONE_CONDIZIONALE))
					&& super.isUserTDSM()) {
				elaboraMisuraAlternativa(lOrdEveTenGP);
			}

			// MEV63: aggiunto codice in or condition
			if ((codOggettoProcedimento
					.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA)
					|| codOggettoProcedimento
							.equalsIgnoreCase(COD_OGGETTO_ESECUZIONE_PRESSO_DOMICILIO_PENA_DETENTIVA))
					&& super.isUserUDSM()) {
				elaboraMisuraAlternativa(lOrdEveTenGP);
			}

			if ((codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_MISURA_SICUREZZA)
					|| codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_RIESAME_PERICOLOSITA_SOCIALE)
					|| codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_LIBERAZIONE_CONDIZIONALE))
					&& super.isUserUDSM()) {
				elaboraMisuraSicurezza(lOrdEveTenGP);
			}

			// String
			// nuovaMisura=getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA);
			// String nuovaMisuraTwo=getRequestStringParameter(
			// ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO);
			// String nuovaMisuraEsec=getRequestStringParameter(
			// ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE);
			// String nuovaMisuraRide=getRequestStringParameter(
			// ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA);
			// String nuovaMisuraRideTwo=getRequestStringParameter(
			// ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO);

			// Per le applicazioni di misure di sicurezza, nel caso di trasformazione della misura, va
			// inserita la nuova misura
			// collegata all' evento generato.
			if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA)
					&& getRequestStringParameter(
							ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA) != null
					&& getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA)
							.compareTo("-") != 0) {

				MisuraSicurezzaModel lNuovaMisura = new MisuraSicurezzaModel();
				lNuovaMisura.setCodNatura(getRequestStringParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA));
				lNuovaMisura.setCodTipo(
						getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA));
				lNuovaMisura.setNumAnni(getRequestBigDecimalParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA));
				lNuovaMisura.setNumMesi(getRequestBigDecimalParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA));
				lNuovaMisura.setNumGiorni(getRequestBigDecimalParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA));
				lNuovaMisura.setAnnoReg38(new BigDecimal(DateUtils.getSysDate("yyyy")));

				lNuovaMisura.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lNuovaMisura.setEveIdEvento(lOrdEveTenGP.getEvento().getIdEvento());
				lNuovaMisura.setFlFormaMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
				lNuovaMisura.setDescrizioneComunita(
						getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
				// Effettuo l'inserimento della Misura di Sicurezza
				lNuovaMisura = lCtrl.ExInserisciMisuraSicurezza(lNuovaMisura);
			}

			// Per le applicazioni di misure di sicurezza, nel caso di esito uguale a "Accertata Pericolosita'
			// Sociale",
			// unifica le m.s. e ne ordina l'esecuzione" (RV_LOW_VALUE di CG_REF_CODES = 1990),
			// va inserita l'eventuale seconda nuova misura.
			if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO)
					&& getRequestStringParameter(
							ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO) != null
					&& getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO)
							.compareTo("-") != 0) {
				MisuraSicurezzaModel lNuovaMisura = new MisuraSicurezzaModel();
				lNuovaMisura.setCodNatura(getRequestStringParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA_TWO));
				lNuovaMisura.setCodTipo(getRequestStringParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO));
				lNuovaMisura.setNumAnni(getRequestBigDecimalParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA_TWO));
				lNuovaMisura.setNumMesi(getRequestBigDecimalParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA_TWO));
				lNuovaMisura.setNumGiorni(getRequestBigDecimalParameter(
						ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA_TWO));
				lNuovaMisura.setAnnoReg38(new BigDecimal(DateUtils.getSysDate("yyyy")));
				lNuovaMisura.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lNuovaMisura.setEveIdEvento(lOrdEveTenGP.getEvento().getIdEvento());
				lNuovaMisura.setFlFormaMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
				lNuovaMisura.setDescrizioneComunita(
						getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
				// Effettuo l'inserimento della Misura di Sicurezza
				lNuovaMisura = lCtrl.ExInserisciMisuraSicurezza(lNuovaMisura);
			}

			// Per le esecuzioni di misure di sicurezza, nel caso di trasformazione della misura, va inserita
			// la nuova misura
			// in esecuzione
			if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE)
					&& getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE) != null
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE)
							.length() > 0
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE)
							.compareTo("-") != 0) {
				IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel lNuovaEMS = new EsecuzioneMisuraSicurezzaModel();
				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS)
						&& getRequestBigDecimalParameter(
								ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS) != null) {
					// Eredito alcune info dalla vecchia esecuzione
					EsecuzioneMisuraSicurezzaModel lVecchiaEMS = new EsecuzioneMisuraSicurezzaModel();
					lVecchiaEMS = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByKey(
							getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS));
					if (lVecchiaEMS != null) {
						lNuovaEMS.setDataDeclaratoriaEMS(lVecchiaEMS.getDataDeclaratoriaEMS());
						lNuovaEMS.setDataInizioMisura(lVecchiaEMS.getDataInizioMisura());
						lNuovaEMS.setDataTermineAttuale(lVecchiaEMS.getDataTermineAttuale());
						lNuovaEMS.setDataTermineIniziale(lVecchiaEMS.getDataTermineIniziale());
					}
				}

				// Gli altri dati vanno letti nella form
				// Per il Tipo Misura occore la decodifica
				if (getRequestStringParameter(
						ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE) != null) {
					String lCodTipoMisuraSicurezza = getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE);
					Collection lColTipoMisureSicurezza = null;
					DecodificheModel lModel = new DecodificheModel();
					IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
					lModel.setContesto("TIPO_MISURA_SICUREZZA");
					lColTipoMisureSicurezza = lDecodifiche.ExRicercaDecodifiche(lModel);

					if (DecodificheUtils.getCodAltebyCode(lColTipoMisureSicurezza,
							lCodTipoMisuraSicurezza) != null) {
						String codTipoNuovaMisura = DecodificheUtils.getCodAltebyCode(lColTipoMisureSicurezza,
								lCodTipoMisuraSicurezza);
						lNuovaEMS.setCodTipoMisura(codTipoNuovaMisura);
					}
				}
				// MERGE v10: prevenzione nullpointer
				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_NUOVA_MISURA_ESECUZIONE))
					lNuovaEMS.setNumAnniMisura(getRequestBigDecimalParameter(
							ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_NUOVA_MISURA_ESECUZIONE));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA))
					lNuovaEMS.setNumAnniMisura(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA));

				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_MESI_NUOVA_MISURA_ESECUZIONE))
					lNuovaEMS.setNumMesiMisura(getRequestBigDecimalParameter(
							ICostantiEsecuzioneMS.CAMPO_NUM_MESI_NUOVA_MISURA_ESECUZIONE));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA))
					lNuovaEMS.setNumMesiMisura(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA));

				if (!isRequestParameterNullObj(
						ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_NUOVA_MISURA_ESECUZIONE))
					lNuovaEMS.setNumGiorniMisura(getRequestBigDecimalParameter(
							ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_NUOVA_MISURA_ESECUZIONE));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA))
					lNuovaEMS.setNumGiorniMisura(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA));

				lNuovaEMS.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNuovaEMS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNuovaEMS.setCodAutoritaEmittOrd(getCodUfficioUtenteConnesso()); // verificare !
				lNuovaEMS.setDataInserimento(DateUtils.getSysDate());
				lNuovaEMS.setGenPridGeneraleProcedimento(
						mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lNuovaEMS.setDepOpidDepositoOrdinanzaPc(
						lOrdEveTenGP.getOrdinanza().getIdDepositoOrdinanzaPc());
				lNuovaEMS.setDataOrdinanza(lOrdEveTenGP.getEvento().getDataEmissione());
				lNuovaEMS.setAnnoS07(mFasGPMod.getFascicoloSiusModel().getChiaveAnno()); // verificare !
				lNuovaEMS.setProgrS07(mFasGPMod.getFascicoloSiusModel().getChiaveProgr()); // verificare !
				lNuovaEMS = lCtrlEMS.ExInserisciEsecuzioneMisuraSicurezza(lNuovaEMS);

				// 13/02/2015 Per le esecuzioni di misure di sicurezza, nel caso di trasformazione della
				// misura, va inserita anche la nuova MisuraSicurezza.
				MisuraSicurezzaModel lNuovaMisura = new MisuraSicurezzaModel();
				lNuovaMisura.setCodTipo(getRequestStringParameter(
						ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE));

				// recupero il codice NATURA_MISURA_SICUREZZA.
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setContesto("TIPO_MISURA_SICUREZZA");
				lDecMod.setCode(getRequestStringParameter(
						ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE));
				IDecodifiche lCtrlDecodifiche = SICOLookupRemote.getDecodificheRemote();
				lDecMod = lCtrlDecodifiche.ExRicercaDecodificheByAbbByHigh(lDecMod);
				String lCodNatura = "-";
				if (lDecMod != null)
					lCodNatura = lDecMod.getFiltro();
				lNuovaMisura.setCodNatura(lCodNatura);

				// MERGE v10: prevenzione nullpointer
				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_NUOVA_MISURA_ESECUZIONE))
					lNuovaMisura.setNumAnni(getRequestBigDecimalParameter(
							ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_NUOVA_MISURA_ESECUZIONE));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA))
					lNuovaMisura.setNumAnni(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA));

				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_MESI_NUOVA_MISURA_ESECUZIONE))
					lNuovaMisura.setNumMesi(getRequestBigDecimalParameter(
							ICostantiEsecuzioneMS.CAMPO_NUM_MESI_NUOVA_MISURA_ESECUZIONE));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA))
					lNuovaMisura.setNumMesi(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA));

				if (!isRequestParameterNullObj(
						ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_NUOVA_MISURA_ESECUZIONE))
					lNuovaMisura.setNumGiorni(getRequestBigDecimalParameter(
							ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_NUOVA_MISURA_ESECUZIONE));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA))
					lNuovaMisura.setNumGiorni(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA));

				lNuovaMisura.setAnnoReg38(new BigDecimal(DateUtils.getSysDate("yyyy")));
				lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
				lNuovaMisura.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lNuovaMisura.setEveIdEvento(lOrdEveTenGP.getEvento().getIdEvento());

				// MERGE v10: aggiunti 2 campi da inserire
				lNuovaMisura.setFlFormaMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
				lNuovaMisura.setDescrizioneComunita(
						getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				// 13/02/2015 fine intervento.

				// MERGE v10: controllo preventivo
				List lMisureSicurezza = lCtrl.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(idFascicoloSius);
				// non esiste la misura di sicurezza associata al fascicolo Sius corrente
				// pertanto devo inserirla
				if (lMisureSicurezza.size() == 0) {
					lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
					lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
					// Effettuo l'inserimento della Misura di Sicurezza
					lNuovaMisura = lCtrl.ExInserisciMisuraSicurezza(lNuovaMisura);
				} else {
					lNuovaMisura.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lNuovaMisura.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lNuovaMisura.setDataAggiornamento(DateUtils.getSysDate());
					// Effettuo la Modifica della Misura di Sicurezza
					lCtrl.ExModificaMisuraSicurezza(lNuovaMisura);
				}
			}

			if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_ESITO_TENORE)
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_ESITO_TENORE) != null
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_ESITO_TENORE)
							.compareTo("2754") == 0) {
				MisuraSicurezzaModel lNuovaMisura = new MisuraSicurezzaModel();
				lNuovaMisura.setCodNatura("01");
				lNuovaMisura.setCodTipo("-");
				lNuovaMisura.setNumAnni(
						getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA));
				lNuovaMisura.setNumMesi(
						getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA));
				lNuovaMisura.setNumGiorni(getRequestBigDecimalParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA));
				if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA))
					lNuovaMisura.setDataDecorrenza(
							getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA,
									ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA,
									ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA));

				lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
				lNuovaMisura.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lNuovaMisura.setEveIdEvento(lOrdEveTenGP.getEvento().getIdEvento());
				lNuovaMisura.setFlFormaMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
				lNuovaMisura.setDescrizioneComunita(
						getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();

				// MERGE v10: controllo preventivo
				List lMisureSicurezza = lCtrl.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(idFascicoloSius);
				// non esiste la misura di sicurezza associata al fascicolo Sius corrente
				// pertanto devo inserirla
				if (lMisureSicurezza.size() == 0) {
					lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
					lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
					// Effettuo l'inserimento della Misura di Sicurezza
					lNuovaMisura = lCtrl.ExInserisciMisuraSicurezza(lNuovaMisura);
				} else {
					lNuovaMisura.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					lNuovaMisura.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lNuovaMisura.setDataAggiornamento(DateUtils.getSysDate());
					// Effettuo la Modifica della Misura di Sicurezza
					lCtrl.ExModificaMisuraSicurezza(lNuovaMisura);
				}

				setRequestAttribute("misuraSicurezza", lNuovaMisura);
			}

			// Per le esecuzioni di misure di sicurezza, nel caso di
			// "Misura di Sicurezza Rideterminata A Seguito Unificazione",
			// vanno inserite le nuove misure in esecuzione (al massimo 2)
			if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA)
					&& getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA) != null
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA)
							.length() > 0
					&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA)
							.compareTo("-") != 0) {
				// Inizio ********************* non so se questo è necessario e se deve settare queste info
				IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel lRidetrminataEMS = new EsecuzioneMisuraSicurezzaModel();

				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS)
						&& getRequestBigDecimalParameter(
								ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS) != null) {
					// Eredito alcune info dalla vecchia esecuzione
					EsecuzioneMisuraSicurezzaModel lVecchiaEMS = new EsecuzioneMisuraSicurezzaModel();
					lVecchiaEMS = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByKey(
							getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS));
					if (lVecchiaEMS != null) {
						lRidetrminataEMS.setDataDeclaratoriaEMS(lVecchiaEMS.getDataDeclaratoriaEMS());
						lRidetrminataEMS.setDataInizioMisura(lVecchiaEMS.getDataInizioMisura());
						lRidetrminataEMS.setDataTermineAttuale(lVecchiaEMS.getDataTermineAttuale());
						lRidetrminataEMS.setDataTermineIniziale(lVecchiaEMS.getDataTermineIniziale());
					}
				}
				// Fine ********************* non so se questo è necessario e se deve settare queste info

				// Gli altri dati vanno letti nella form
				// Per il Tipo Misura occore la decodifica
				if (getRequestStringParameter(
						ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA) != null) {
					String lCodTipoMisuraSicurezza = getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA);
					Collection lColTipoMisureSicurezza = null;
					DecodificheModel lModel = new DecodificheModel();
					IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
					lModel.setContesto("TIPO_MISURA_SICUREZZA");
					lColTipoMisureSicurezza = lDecodifiche.ExRicercaDecodifiche(lModel);

					if (DecodificheUtils.getCodAltebyCode(lColTipoMisureSicurezza,
							lCodTipoMisuraSicurezza) != null) {
						String codTipoMisuraRid = DecodificheUtils.getCodAltebyCode(lColTipoMisureSicurezza,
								lCodTipoMisuraSicurezza);
						lRidetrminataEMS.setCodTipoMisura(codTipoMisuraRid);
					}
				}

				// ***************** nel caso precedente di Nuova Misura non
				// viene recuperato il campo "Natura" perchè?
				lRidetrminataEMS.setNumAnniMisura(getRequestBigDecimalParameter(
						ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA));
				lRidetrminataEMS.setNumMesiMisura(getRequestBigDecimalParameter(
						ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA));
				lRidetrminataEMS.setNumGiorniMisura(getRequestBigDecimalParameter(
						ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA));

				lRidetrminataEMS.setCodOperatoreInserimento(getCodUtenteConnesso());
				lRidetrminataEMS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lRidetrminataEMS.setCodAutoritaEmittOrd(getCodUfficioUtenteConnesso()); // verificare!
				lRidetrminataEMS.setDataInserimento(DateUtils.getSysDate());
				lRidetrminataEMS.setGenPridGeneraleProcedimento(
						mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lRidetrminataEMS.setDepOpidDepositoOrdinanzaPc(
						lOrdEveTenGP.getOrdinanza().getIdDepositoOrdinanzaPc());
				lRidetrminataEMS.setDataOrdinanza(lOrdEveTenGP.getEvento().getDataEmissione());
				lRidetrminataEMS.setAnnoS07(mFasGPMod.getFascicoloSiusModel().getChiaveAnno()); // verificare!
				lRidetrminataEMS.setProgrS07(mFasGPMod.getFascicoloSiusModel().getChiaveProgr()); // verificare!

				lRidetrminataEMS = lCtrlEMS.ExInserisciEsecuzioneMisuraSicurezza(lRidetrminataEMS);
			}

			// recupero eventuale seconda Misura di Sicurezza Rideterminata A Seguito Unificazione
			if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO)
					&& getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO) != null
					&& getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO).length() > 0
					&& getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO)
									.compareTo("-") != 0) {
				// Inizio ********************* non so se questo è necessario e se deve settare queste info
				IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel lRidetrminataEMSTwo = new EsecuzioneMisuraSicurezzaModel();

				if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS)
						&& getRequestBigDecimalParameter(
								ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS) != null) {
					// Eredito alcune info dalla vecchia esecuzione
					EsecuzioneMisuraSicurezzaModel lVecchiaEMS = new EsecuzioneMisuraSicurezzaModel();
					lVecchiaEMS = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByKey(
							getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS));
					if (lVecchiaEMS != null) {
						lRidetrminataEMSTwo.setDataDeclaratoriaEMS(lVecchiaEMS.getDataDeclaratoriaEMS());
						lRidetrminataEMSTwo.setDataInizioMisura(lVecchiaEMS.getDataInizioMisura());
						lRidetrminataEMSTwo.setDataTermineAttuale(lVecchiaEMS.getDataTermineAttuale());
						lRidetrminataEMSTwo.setDataTermineIniziale(lVecchiaEMS.getDataTermineIniziale());
					}
				}
				// Fine ********************* non so se questo è necessario e se deve settare queste info

				// Gli altri dati vanno letti nella form
				// Per il Tipo Misura occore la decodifica
				if (getRequestStringParameter(
						ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO) != null) {
					String lCodTipoMisuraSicurezza = getRequestStringParameter(
							ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO);
					Collection lColTipoMisureSicurezza = null;
					DecodificheModel lModel = new DecodificheModel();
					IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
					lModel.setContesto("TIPO_MISURA_SICUREZZA");
					lColTipoMisureSicurezza = lDecodifiche.ExRicercaDecodifiche(lModel);

					if (DecodificheUtils.getCodAltebyCode(lColTipoMisureSicurezza,
							lCodTipoMisuraSicurezza) != null) {
						String codTipoMisuraRidTwo = DecodificheUtils
								.getCodAltebyCode(lColTipoMisureSicurezza, lCodTipoMisuraSicurezza);
						lRidetrminataEMSTwo.setCodTipoMisura(codTipoMisuraRidTwo);
					}
				}

				// ***************** nel caso precedente di Nuova Misura
				// non viene recuperato il campo "Natura" perchè?
				lRidetrminataEMSTwo.setNumAnniMisura(getRequestBigDecimalParameter(
						ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA_TWO));
				lRidetrminataEMSTwo.setNumMesiMisura(getRequestBigDecimalParameter(
						ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA_TWO));
				lRidetrminataEMSTwo.setNumGiorniMisura(getRequestBigDecimalParameter(
						ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA_TWO));
				lRidetrminataEMSTwo.setCodOperatoreInserimento(getCodUtenteConnesso());
				lRidetrminataEMSTwo.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lRidetrminataEMSTwo.setCodAutoritaEmittOrd(getCodUfficioUtenteConnesso()); // verificare
				lRidetrminataEMSTwo.setDataInserimento(DateUtils.getSysDate());
				lRidetrminataEMSTwo.setGenPridGeneraleProcedimento(
						mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lRidetrminataEMSTwo.setDepOpidDepositoOrdinanzaPc(
						lOrdEveTenGP.getOrdinanza().getIdDepositoOrdinanzaPc());
				lRidetrminataEMSTwo.setDataOrdinanza(lOrdEveTenGP.getEvento().getDataEmissione());
				lRidetrminataEMSTwo.setAnnoS07(mFasGPMod.getFascicoloSiusModel().getChiaveAnno()); // verificare
				lRidetrminataEMSTwo.setProgrS07(mFasGPMod.getFascicoloSiusModel().getChiaveProgr()); // verificare

				lRidetrminataEMSTwo = lCtrlEMS.ExInserisciEsecuzioneMisuraSicurezza(lRidetrminataEMSTwo);
			}

			// MERGE v10: spostata qui questa porzione di codice
			// Per le applicazioni di misure di sicurezza, con oggetto "Inosservanza delle Misure di Sicurezza
			// Detentive (art. 214 c.p.)" ed esito "Dispone che ricominci a decorrere il periodo minimi
			// della misura" bisogna recuperare il campo "Data Decorrenza per la Misura di Sicurezza"
			// oppure in corrispondenza di
			// inserimento Emissione "Proposta di aggravamento della libertà vigilata per
			// persone in stato di infermità psichica (art.232 c.p.)" e l’ oggetto
			// "Proposta di aggravamento della libertà vigilata per persone in stato di
			// infermità psichica (art.232 c.p.)" ed esito "Sostituisce la libertà vigilata
			// con la casa di cura e custodia"

			// Ticket#20220415019 — cancellazione/pagina errore
			// In caso di Revoca misura alternativa (C002) veniva inserito un record MISURA_SICUREZZA senza
			// alcun
			// motivo a causa della presenza dei campi AnnoDataDecorrenza ecc. In fase di cancellazione si
			// verificava
			// una violazione di integrità. Si escludono le revoche MA
			if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
					&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("C002")) {
				// non faccio nulla
				siesLogger.debug("Revoca MA non gestisco l'inserimento MS ");
				// MEV_2023-35 aggiungo anche la revoca PS che scrivono sul campo deposito.data_decorrenza
			} else if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
					&& (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U131")
							|| getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
									.equals("U132"))) {
				// non faccio nulla
				siesLogger.debug("Revoca Pene sostitutive non gestisco l'inserimento MS ");
			} else {
				Date dataDecorrenzaMS = null;
				if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA))
					dataDecorrenzaMS = getRequestDateParameter(
							ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA,
							ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA,
							ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA);
				// MERGE v10: aggiunta porzione di codice per gestire la data
				if (!Utils.isPresent(dataDecorrenzaMS) && !isRequestParameterNullObj(
						ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA))
					dataDecorrenzaMS = getRequestDateParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA,
							ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA,
							ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA);
				if (!Utils.isPresent(dataDecorrenzaMS)
						&& !isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA))
					dataDecorrenzaMS = getRequestDateParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA,
							ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PROROGA,
							ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PROROGA);
				if (dataDecorrenzaMS != null) {
					// MERGE v10: spostata sopra questa impostazione
					// recupero l'identificativo del fascicolo SIUS per il quale si sta emettendo l'ordinanza
					// BigDecimal idFascicoloSius = mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
					if (idFascicoloSius != null) {
						IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
						List lMisureSicurezza = lCtrl
								.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(idFascicoloSius);
						// non esiste la misura di sicurezza associata al fascicolo Sius corrente
						// pertanto devo inserirla
						if (lMisureSicurezza.size() == 0) {
							MisuraSicurezzaModel lNuovaMisura = new MisuraSicurezzaModel();

							lNuovaMisura.setCodOperatoreInserimento(getCodUtenteConnesso());
							lNuovaMisura.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lNuovaMisura.setDataInserimento(DateUtils.getSysDate());
							lNuovaMisura.setFasSiuIdFascicoloSius(
									mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
							lNuovaMisura.setCodNatura("-");
							lNuovaMisura.setCodTipo("-");
							lNuovaMisura.setEveIdEvento(lOrdEveTenGP.getEvento().getIdEvento());
							lNuovaMisura.setDataDecorrenza(dataDecorrenzaMS);
							lNuovaMisura.setFlFormaMisura(
									getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
							lNuovaMisura.setDescrizioneComunita(
									getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
							// MERGE v10: aggiunto codice
							lNuovaMisura.setNumAnni(getAnniMisuraParameter());
							lNuovaMisura.setNumMesi(getMesiMisuraParameter());
							lNuovaMisura.setNumGiorni(getGiorniMisuraParameter());
							// Effettuo l'Inserimento della Misura di Sicurezza
							lNuovaMisura = lCtrl.ExInserisciMisuraSicurezza(lNuovaMisura);
						} else {
							// aggiorno le misure di sicurezza trovate impostando la Data Decorrenza
							Iterator itxMis = lMisureSicurezza.iterator();
							while (itxMis.hasNext()) {
								MisuraSicurezzaModel lMisSicuSius = (MisuraSicurezzaModel) itxMis.next();
								lMisSicuSius.setCodOperatoreAggiornamento(getCodUtenteConnesso());
								lMisSicuSius.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
								lMisSicuSius.setDataAggiornamento(DateUtils.getSysDate());
								lMisSicuSius.setEveIdEvento(lOrdEveTenGP.getEvento().getIdEvento());
								lMisSicuSius.setDataDecorrenza(dataDecorrenzaMS);
								// Effettuo la Modifica della Misura di Sicurezza
								lCtrl.ExModificaMisuraSicurezza(lMisSicuSius);
							}
						}
					} // if(idFascicoloSius != null){
				} // if(dataDecorrenzaMS != null){
			} // Ticket#20220415019 - FINE

			/*
			 * ISSUE MEV : aggiunto codice per gestione oggetto C029 
			 * Numero MEV : 39 
			 * Autore : Gioggi 
			 * Data : 19/giu/2017 
			 * Branch : MEV_39
			 */
			if (codOggettoProcedimento.equalsIgnoreCase(OGG_ORD_APPELLO_CONTRO_PROVV_MS)) {
				TenoreModel tenori[] = lOrdEveTenGP.getTenori();
				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				for (TenoreModel tenore : tenori) {
					String codEsitoTenore = tenore.getCodEsitoTenore();
					if (ICostantiMisuraSicurezza.COD_ACCOGLIE_APPELLO_E_MODIFICA_MDS
							.equalsIgnoreCase(codEsitoTenore)) {
						MisuraSicurezzaModel misuraSicurezzaModel = new MisuraSicurezzaModel();
						BigDecimal idEvento = lOrdEveTenGP.getEvento().getIdEvento();
						misuraSicurezzaModel.setEveIdEvento(idEvento);
						misuraSicurezzaModel.setCodTipo(getRequestStringParameter("codiTipoNuovaMisura"));
						Collection c = DecodificheManager.getInstance().getTipoMisuraSicurezza();
						String natura = DecodificheUtils.getFiltrobyCode(c,
								getRequestStringParameter("codiTipoNuovaMisura"));
						misuraSicurezzaModel.setCodNatura(natura);
						misuraSicurezzaModel.setFlFormaMisura(
								getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
						misuraSicurezzaModel.setDescrizioneComunita(
								getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
						misuraSicurezzaModel.setCodUfficioInserimento(mCodiceUfficio);
						misuraSicurezzaModel.setCodOperatoreInserimento(mCodiceOperatore);
						misuraSicurezzaModel.setFasSiuIdFascicoloSius(
								mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
						misuraSicurezzaModel.setNumAnni(getRequestBigDecimalParameter("anniDurataNuovaMS"));
						misuraSicurezzaModel.setNumMesi(getRequestBigDecimalParameter("mesiDurataNuovaMS"));
						misuraSicurezzaModel
								.setNumGiorni(getRequestBigDecimalParameter("giorniDurataNuovaMS"));
						Date dataDecorrenzaNuovaMS = null;
						if (!isRequestParameterNullObj("annoDataDecorrenzaNuovaMS")) {
							dataDecorrenzaNuovaMS = getRequestDateParameter("annoDataDecorrenzaNuovaMS",
									"meseDataDecorrenzaNuovaMS", "giornoDataDecorrenzaNuovaMS");
						}
						misuraSicurezzaModel.setDataDecorrenza(dataDecorrenzaNuovaMS);
						misuraSicurezzaModel.setDataInserimento(DateUtils.getSysDate());
						lCtrl.ExInserisciMisuraSicurezza(misuraSicurezzaModel);
					}
				}
			}
			// ***** FINE INTERVENTO MEV_39 *****//

			// Preparazione della pagina di destinazione per l'inserimento delle Prescrizioni
			if (!isRequestParameterNullObj(CAMPO_CK_PRESCRIZIONI)
					&& isRequestChecked(CAMPO_CK_PRESCRIZIONI)) {
				// Per Sanzioni Sostitutive
				if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
						&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
								.equals("U017")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Prescrizioni SS: ");

					RedirectTo lRedirectTo = new RedirectTo();
					lRedirectTo.setPage(IWebConstants.PG_MAIN);
					lRedirectTo.setAction("siap.sius.prescrizione.action.ActLoadInserisciPrescrizioneNew");
					lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
							lOrdEveTenGP.getEvento().getIdEvento().toString());
					// Viene passato il filtro usato per il decreto ....
					lRedirectTo.setParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO, "U059");
					// Action successiva
					lRedirectTo.setParameter("nextaction",
							"siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");

					lRetPage = lRedirectTo.toString();
				}
				// Per Conversione Pene Pecuniarie.
				else if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
						&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
								.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE)) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Conversione PP: ");

					RedirectTo lRedirectTo = new RedirectTo();
					lRedirectTo.setPage(IWebConstants.PG_MAIN);
					lRedirectTo.setAction("siap.sius.prescrizione.action.ActLoadInserisciPrescrizioneNew");
					lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
							lOrdEveTenGP.getEvento().getIdEvento().toString());
					// Viene passato il filtro usato per il decreto ....
					// lRedirectTo.setParameter( ICostantiFascicoloSius.CAMPO_COD_CONTENUTO, "U059" );
					// Action successiva
					lRedirectTo.setParameter("nextaction",
							"siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");

					lRetPage = lRedirectTo.toString();
				}
				// Per Misure Sicurezza
				else if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
						&& (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
								.equals("U023")
								|| getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
										.equals("U086")
								|| mCodTipoRegistro.equals("S09"))) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Misure Sicurezza: ");

					RedirectTo lRedirectTo = new RedirectTo();
					lRedirectTo.setPage(IWebConstants.PG_MAIN);
					lRedirectTo.setAction("siap.sius.prescrizione.action.ActLoadInserisciPrescrizioneNew");
					lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
							lOrdEveTenGP.getEvento().getIdEvento().toString());

					// Action successiva
					lRedirectTo.setParameter("nextaction",
							"siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");

					lRetPage = lRedirectTo.toString();
				} else if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
						&& getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO)
								.equals("U125")) {
					siesLogger.debug("Prescrizioni SP: ");

					RedirectTo lRedirectTo = new RedirectTo();
					lRedirectTo.setPage(IWebConstants.PG_MAIN);
					lRedirectTo.setAction("siap.sius.prescrizione.action.ActLoadInserisciPrescrizioneNew");
					lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
							lOrdEveTenGP.getEvento().getIdEvento().toString());
					// Action successiva
					lRedirectTo.setParameter("nextaction",
							"siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");

					lRetPage = lRedirectTo.toString();
				} else {
					lRetPage = ICostantiPrescrizione.PG_LOAD_INSERISCIPRESCRIZIONE;
				}

				// Passaggio di dati alla jsp
				// setRequestAttribute("UffMagComp", mDescUffMagComp);
				setRequestAttribute("LuogoProva", mLuogo);
				setRequestAttribute("ComuneCSSA", mDescComuneCSSA);
				setRequestAttribute("IDEvento", lOrdEveTenGP.getEvento().getIdEvento().toString());
				setRequestAttribute("modalita", "I");
				setRequestAttribute("nextaction",
						"siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
			} else {
				// dettaglio dell'ordinanza
				RedirectTo lRedirectTo = new RedirectTo();
				lRedirectTo.setPage(IWebConstants.PG_MAIN);
				lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
				lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
						lOrdEveTenGP.getEvento().getIdEvento().toString());
				lRetPage = lRedirectTo.toString();
			}
		} catch (SICOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + daoex);
			if (daoex.getErrorCode() == F3BException.USER_MESSAGE) {
				lRetPage = IWebConstants.PG_MESSAGE;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, daoex.getMessage());
			} else
				throw daoex;
		} catch (Exception e) {
			throw e;
		}
		return lRetPage;
	}

	/**
	 *
	 * @return
	 * @throws F3BException
	 */
	GeneraleProcedimentoModel generaProcedimento() throws F3BException {

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
		lGenProcModel.setIdGeneraleProcedimento(mIdGenProc);
		lGenProcModel.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGenProcModel.setDataAggiornamento(mOggi);
		lGenProcModel.setCodUfficioAggiornamento(mCodiceUfficio);
		lGenProcModel.setCodOperatoreAggiornamento(mCodiceOperatore);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Generale Procedimento = " + lGenProcModel);

		return lGenProcModel;
	}

	private TenoreModel[] generaTenori() throws F3BException {

		TenoreModel[] lTenori = null;

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
			lTenori = new TenoreModel[lSizeArray];
			for (int i = 0; i < lSizeArray; i++) {
				TenoreModel lTenModel = new TenoreModel();

				lTenModel.setProgrTenore(new BigDecimal((double) (i + 1)));
				lTenModel.setCodOggettoTenore(lCodOggetti[i]);
				lTenModel.setDescrOggettoTenore(lDescOggetti[i]);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioOggetti[i]);
				lTenModel.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(lCodEsiti[i]));
				// lTenModel.setCodEsitoTenore(lCodEsiti[i]);
				// Codice dell'ufficio dell'operatore che inserisce
				lTenModel.setCodUfficioInserimento(mCodiceUfficio);
				// Codice dell'operatore che inserisce
				lTenModel.setCodOperatoreInserimento(mCodiceOperatore);
				lTenModel.setDataInserimento(mOggi);
				lTenModel.setGenPridGeneraleProcedimento(mIdGenProc);
				lTenModel.setCodMagistrato(mCodMagistrato);

				// Inserimenti i-esimo Tenore
				lTenori[i] = lTenModel;

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Tenore n." + i + " = " + lTenori[i]);
			}
		}
		return lTenori;
	}

	/**
	 * Prepara con i dati il Model per il Deposito Ordinanza.
	 *
	 * @param aDataEmissione
	 *            Date data di emissione
	 * @throws F3BException
	 *             propaga errori di eccezione.
	 * @return DepositoOrdinanzaPcModel riotrna istanza del model opportunemnte popolato.
	 */
	private DepositoOrdinanzaPcModel generaOrdinanza(Date aDataEmissione) throws F3BException {

		// Prepara il model DepositoOrdinanza.
		DepositoOrdinanzaPcModel lDepOrdModel = new DepositoOrdinanzaPcModel();

		lDepOrdModel.setCodTipoOrdinanza(getRequestStringParameter(CAMPO_COD_TIPO_ORDINANZA));

		// Per i procedimenti di esecuzione MS il tipo ordinanza viene impostato ad "MS" (per le prescrizioni)
		if (mCodTipoRegistro != null && mCodTipoRegistro.equals("S09")
				&& (!lDepOrdModel.getCodTipoOrdinanza().equals(ORD_SOSPENSIONE_ESECUZIONE_MS)))
			lDepOrdModel.setCodTipoOrdinanza("MS");

		// Per i procedimenti di riesame, trasformazione e inosservanza esecuzione MS il tipo ordinanza viene
		// impostato a "TM"
		if (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U067")
				|| getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U088")
				|| getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U066")) {
			lDepOrdModel.setCodTipoOrdinanza("TM");
		}
		lDepOrdModel.setCodMagistrato(mCodMagistrato);
		lDepOrdModel.setGenPridGeneraleProcedimento(mIdGenProc);
		lDepOrdModel.setCodOperatoreInserimento(mCodiceOperatore);
		lDepOrdModel.setCodUfficioInserimento(mCodiceUfficio);
		lDepOrdModel.setDataInserimento(mOggi);
		lDepOrdModel.setDataCameraConsiglio(aDataEmissione);

		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		// 20140605 - Tipo Controllo Esecuzione
		if (!isRequestParameterNullObj(CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE))
			lDepOrdModel.setCodTipoControlloEsecuzione(
					getRequestStringParameters(CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE)[0]);

		if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA)
				&& getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA) != null
				&& !getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA).equals("")) {
			// In questo campo c'è direttamente la descrizione dell'Ufficio Magistrato inserimento
			// dell'Ordinanza L.A.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" XXXXXXXXXXXXXXXXXXXXXXX Mag = " +
			// getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA));
			lDepOrdModel.setDescrUfficioMagistratoComp(
					getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA));
		}

		// MEV_2023-35: aggiunto recupero dell'importo della pena pecuniaria convertita
		if (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("C065")) {
			if (!isRequestParameterNullObj(CAMPO_INTERO_PENA_PECUNIARIA_CONVERTITA)
					&& ((getRequestStringParameter(CAMPO_INTERO_PENA_PECUNIARIA_CONVERTITA) != null
							&& !(getRequestStringParameter(CAMPO_INTERO_PENA_PECUNIARIA_CONVERTITA))
									.equals(""))
							|| (getRequestStringParameter(CAMPO_DECIMALE_PENA_PECUNIARIA_CONVERTITA) != null
									&& !(getRequestStringParameter(CAMPO_DECIMALE_PENA_PECUNIARIA_CONVERTITA))
											.equals("")))) {
				lDepOrdModel.setSommaRisarcimento((new BigDecimal(
						getRequestStringParameter(CAMPO_INTERO_PENA_PECUNIARIA_CONVERTITA) + "."
								+ getRequestStringParameter(CAMPO_DECIMALE_PENA_PECUNIARIA_CONVERTITA))));
			}
		} else if (getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO).equals("U142")) {
			lDepOrdModel.setCodTipoOrdinanza("SR");
		}
		// FINE MEV_2023-35

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("DepositoOrdinanza = " + lDepOrdModel);
		return lDepOrdModel;
	}

	/**
	 * Prepara con i relativi dati, il Model per l'evento.
	 *
	 * @param aDataEmissione
	 *            Date data di emissione.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 * @return EventoModel ritorna l'evento model.
	 */
	private EventoModel generaEvento(Date aDataEmissione) throws F3BException {

		// Prepara Model Evento.
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento("01"); // 01 = Provvedimento.
		lEvento.setCodTipoProvvedimento("03"); // 03 = Ordinanza.
		lEvento.setCodLuogoEmittente(mCodiceComune);
		lEvento.setCodUfficioEmittente(mCodiceUfficio);
		// lEvento.setCodEsito("-");
		lEvento.setDataEmissione(aDataEmissione);
		lEvento.setFasSieIdFascicoloSiep(mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEvento.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEvento.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEvento.setCodUfficioInserimento(mCodiceUfficio);
		lEvento.setDataInserimento(mOggi);
		lEvento.setCodLuogoDestinatario("-");
		lEvento.setCodTipoUfficioDestinatario("-");
		lEvento.setCodUfficioDestinatario("-");
		lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvento.setCodMagistrato(mCodMagistrato);
		// Inserimento dell'Evento collegato (Revoca Provvedimento)
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_EVE_ID_EVENTO))
			lEvento.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento = " + lEvento);
		return lEvento;
	}

	/**
	 * Funzione di lettura dei dati opzionali
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	private OrdinanzaEventoTenoriGProcModel generaDati(OrdinanzaEventoTenoriGProcModel aModel)
			throws F3BException {

		// /////////////////////////////////////////////////
		// Aggiornamento dei dati in DepositoOrdinanza. //
		// /////////////////////////////////////////////////
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("generaDati...");
		DepositoOrdinanzaPcModel lDepOrdModel = aModel.getOrdinanza();

		lDepOrdModel.setCodNaturaProvvedimento("-");

		// Lettura del CSSA
		if (!isRequestParameterNullObj(CAMPO_ID_CSSA_COMP)) {
			lDepOrdModel.setIdCssaComp(getRequestBigDecimalParameter(CAMPO_ID_CSSA_COMP));
			if (!isRequestParameterNullObj(CAMPO_COMUNE_CSSA_COMP))
				mDescComuneCSSA = getRequestStringParameter(CAMPO_COMUNE_CSSA_COMP).toUpperCase();

			// 30/10/2003 Controllo Esistenza CSSA.
			if (mDescComuneCSSA != null && mDescComuneCSSA.length() > 1) {
				lDepOrdModel.setDescrComuneCssaComp(
						getRequestStringParameter(CAMPO_COMUNE_CSSA_COMP).toUpperCase());
				ICSSA lCSSACtrl = SICOLookupRemote.getCSSARemote();
				lDepOrdModel.setIdCssaComp(lCSSACtrl
						.getCSSAByDescrComune(getRequestStringParameter(CAMPO_COMUNE_CSSA_COMP).toUpperCase())
						.getIdCSSA());
			} else
				lDepOrdModel.setIdCssaComp(new BigDecimal("9999"));
		}

		// Lettura del USSM
		if (!isRequestParameterNullObj(CAMPO_UFFICIO_USSM)) {
			mDescComuneUSSM = getRequestStringParameter(CAMPO_UFFICIO_USSM).toUpperCase();
			// 10/03/2015 Controllo Esistenza CSSA.
			if (mDescComuneUSSM != null && mDescComuneUSSM.length() > 1) {
				ICSSA lCSSACtrl = SICOLookupRemote.getCSSARemote();
				lDepOrdModel.setCodUssm(lCSSACtrl
						.getUSSMByDescrComune(getRequestStringParameter(CAMPO_UFFICIO_USSM).toUpperCase())
						.getIdCSSA());
				lDepOrdModel.setDescrComuneUssmComp(mDescComuneUSSM);
			} else
				lDepOrdModel.setCodUssm(null);
		}

		// Lettura COD Ufficio Magistrato
		if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA)) {
			// Controlla e imposta il codice ufficio magistrato competente.
			if (getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA).compareTo("") != 0)
				lDepOrdModel.setCodUfficioMagistratoComp(getCodUfficioByCodTipoUfficioDescrComune("UDS",
						getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA)));
			else
				lDepOrdModel.setCodUfficioMagistratoComp("-");
		} else
			lDepOrdModel.setCodUfficioMagistratoComp("-");

		// Lettura COD Ufficio TDS
		// 28/03/2007 L'Ufficio (Prima TDS statico) può anche essere un UDS.
		// 04/01/2008 Nota aggiuntiva : Come richiesto dall'amministrazione, tale campo
		// viene utlizzato in caso di concessione dell'indultino l'Ufficio
		// di Sorveglianza emette un'ordinanza; quindi per UDS. Pertanto le modifiche
		// richieste sono state implementate nella InserisciOrdinanzaRevocaMA.jsp.
		// Al fine di ridurre l'impatto in caso di UDS emittente cmq si usa il campo
		// CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE.
		if (!isRequestParameterNullObj(CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE)) {
			// 23/05/2007 e 21/06/2007 MAC x segnalazione da Torino.
			String lCodTipoUfficio = "TDS";
			if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_TIPO_UFFICIO))
				lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
			// Controlla e imposta il codice ufficio TDS.
			if (getRequestStringParameter(CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE).compareTo("") != 0)
				// lDepOrdModel.setCodUffTdsConcessoRiduzione(getCodUfficioByCodTipoUfficioDescrComune("TDS",
				// getRequestStringParameter(CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE)));
				lDepOrdModel.setCodUffTdsConcessoRiduzione(getCodUfficioByCodTipoUfficioDescrComune(
						lCodTipoUfficio, getRequestStringParameter(CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE)));
			else
				lDepOrdModel.setCodUffTdsConcessoRiduzione("-");
		}

		if (!isRequestParameterNullObj(CAMPO_LUOGO_SVOLGIMENTO_PROVA)) {
			// 07/06/2010 mLuogo = StringUtils.convertSqlString(
			// getRequestStringParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));
			mLuogo = StringUtils.toStringJSP(getRequestStringParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));
			lDepOrdModel.setLuogoSvolgimentoProva(mLuogo);
		}
		if (!isRequestParameterNullObj(CAMPO_SERVIZIO_TERAPEUTICO_COMP))
			lDepOrdModel
					.setServizioTerapeuticoComp(getRequestStringParameter(CAMPO_SERVIZIO_TERAPEUTICO_COMP));
		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_DETENZIONE_DOM))
			lDepOrdModel.setNumGiorniDetenzioneDom(
					getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_DETENZIONE_DOM));
		if (!isRequestParameterNullObj(CAMPO_NUM_MESI_DETENZIONE_DOM))
			lDepOrdModel
					.setNumMesiDetenzioneDom(getRequestBigDecimalParameter(CAMPO_NUM_MESI_DETENZIONE_DOM));
		if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_DETENZIONE_DOM))
			lDepOrdModel
					.setNumAnniDetenzioneDom(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_DETENZIONE_DOM));
		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI))
			lDepOrdModel.setNumGiorniPermessoAccordati(
					getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI));

		if (!isRequestParameterNullObj(CAMPO_AUTORITA_VIGILANTE)
				&& getRequestStringParameter(CAMPO_AUTORITA_VIGILANTE).trim().length() > 0) {
			if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)
					&& ICostantiDepositoOrdinanzaPc.AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP
							.equals(getRequestStringParameter(
									ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA)))
				lDepOrdModel.setAutoritaVigilante(getCodUfficioByCodTipoUfficioDescrComune("PM",
						getRequestStringParameter(CAMPO_AUTORITA_VIGILANTE)));
			else
				lDepOrdModel.setAutoritaVigilante(
						StringUtils.convertSqlString(getRequestStringParameter(CAMPO_AUTORITA_VIGILANTE)));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE_MISURA))
			lDepOrdModel.setDataFineMisura(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_MISURA,
					CAMPO_MESE_DATA_FINE_MISURA, CAMPO_GIORNO_DATA_FINE_MISURA));
		if (!isRequestParameterNullObj(CAMPO_FLAG_ESISTENZA_REATOOSTATIVO))
			lDepOrdModel.setFlagEsistenzaReatoostativo(
					getRequestStringParameter(CAMPO_FLAG_ESISTENZA_REATOOSTATIVO));
		if (!isRequestParameterNullObj(CAMPO_FLAG_ESPIAZIONE_REATOOSTATIVO))
			lDepOrdModel.setFlagEspiazioneReatoostativo(
					getRequestStringParameter(CAMPO_FLAG_ESPIAZIONE_REATOOSTATIVO));
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_TRASMISSIONE))
			lDepOrdModel.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
					CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_DECORRENZA))
			lDepOrdModel.setDataDecorrenza(getRequestDateParameter(CAMPO_ANNO_DATA_DECORRENZA,
					CAMPO_MESE_DATA_DECORRENZA, CAMPO_GIORNO_DATA_DECORRENZA));
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO_PERIODO))
			lDepOrdModel.setDataInizioPeriodo(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_PERIODO,
					CAMPO_MESE_DATA_INIZIO_PERIODO, CAMPO_GIORNO_DATA_INIZIO_PERIODO));
		if (!isRequestParameterNullObj(CAMPO_COD_NATURA_PROVVEDIMENTO))
			lDepOrdModel.setCodNaturaProvvedimento(getRequestStringParameter(CAMPO_COD_NATURA_PROVVEDIMENTO));
		// Nuovi campi 9-5-2006
		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_ARRESTO_REV))
			lDepOrdModel.setNumGiorniArrestoRev(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO_REV));
		if (!isRequestParameterNullObj(CAMPO_NUM_MESI_ARRESTO_REV))
			lDepOrdModel.setNumMesiArrestoRev(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO_REV));
		if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_ARRESTO_REV))
			lDepOrdModel.setNumAnniArrestoRev(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO_REV));
		// Campi proroga misura sicurezza
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROROGA) && lDepOrdModel.getDataDecorrenza() == null)
			lDepOrdModel.setDataDecorrenza(getRequestDateParameter(CAMPO_ANNO_DATA_PROROGA,
					CAMPO_MESE_DATA_PROROGA, CAMPO_GIORNO_DATA_PROROGA));
		if (!isRequestParameterNullObj(CAMPO_NUM_GIORNI_PROROGA))
			lDepOrdModel.setSospensioneGGSS(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PROROGA));
		if (!isRequestParameterNullObj(CAMPO_NUM_MESI_PROROGA))
			lDepOrdModel.setSospensioneMMSS(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PROROGA));
		if (!isRequestParameterNullObj(CAMPO_NUM_ANNI_PROROGA))
			lDepOrdModel.setSospensioneAASS(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_PROROGA));

		// ===============================
		// Lettura COD Ufficio Magistrato add d.f. 13/06/2014 su segnalazione Umb
		// per ordinanza concessione esecuzione presso domicilio

		// modifica del 16/06/2015 per presenza anomalia
		// il CodUfficioMagistratoComp settato in precedenza con il valore
		// del campo "CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA" presente nella
		// maschera "Emissione Ordinanza di Reclamo Liberazione Anticipata"
		// veniva sovrascritto con il valore del campo "CAMPO_COD_UFFICIO_MAGISTRATO_COMP"
		if (lDepOrdModel.getCodUfficioMagistratoComp() != null
				&& lDepOrdModel.getCodUfficioMagistratoComp().equals("-")) {
			if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
				// Controlla e imposta il codice ufficio magistrato competente.
				if (getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP).compareTo("") != 0)
					lDepOrdModel.setCodUfficioMagistratoComp(getCodUfficioByCodTipoUfficioDescrComune("UDS",
							getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP)));
				else
					lDepOrdModel.setCodUfficioMagistratoComp("-");
			}
		}

		// Data Decorrenza Sospensione
		if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS)
				&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS)
				&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS)) {
			Date lDataDecorrenzaSS = getRequestDateParameter(
					ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS,
					ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS,
					ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS);
			lDepOrdModel.setDataSospensioneSS(lDataDecorrenzaSS);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Data Decorrenza Sospensione:" + lDataDecorrenzaSS);
			// Lettura COD Ufficio Magistrato Competente per Ordinanza di Sospensione
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
				// Controlla e imposta il codice ufficio magistrato competente.
				if (getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)
						.compareTo("") != 0)
					lDepOrdModel.setCodUfficioMagistratoComp(
							getCodUfficioByCodTipoUfficioDescrComune("UDS", getRequestStringParameter(
									ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)));
				else
					lDepOrdModel.setCodUfficioMagistratoComp("-");
			}
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP)) {
				// Lettura Tribunale di SorveglianzaCompetente.
				String lDescUff = getRequestStringParameter(
						ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP);
				if (lDescUff.trim().length() > 1) {
					String lCodTipoUfficio = "TDS";
					if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_TIPO_UFFICIO)) {
						lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
					}
					String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescUff);
					lDepOrdModel.setCodUffTdsConcessoRiduzione(lCodUfficio);
				}
			}
			// Le NOTE nel campo COD_NATURA_PROVVEDIMENTO
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_NOTE))
				lDepOrdModel.setCodNaturaProvvedimento(
						getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_NOTE));
		}

		if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS))
			lDepOrdModel.setSospensioneAASS(
					getRequestBigDecimalParameter(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS));
		if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS))
			lDepOrdModel.setSospensioneMMSS(
					getRequestBigDecimalParameter(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS));
		if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS))
			lDepOrdModel.setSospensioneGGSS(
					getRequestBigDecimalParameter(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS));

		// MEV_2023-35: aggiungo controllo per Data "Fino al"
		if ((!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS)
				&& getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS)
						.length() > 0)
				&& (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS)
						&& getRequestStringParameter(
								ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS).length() > 0)
				&& (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS)
						&& getRequestStringParameter(
								ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS).length() > 0))
			lDepOrdModel.setDataScadenzaSospensioneSS(
					getRequestDateParameter(ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS,
							ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS,
							ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS));

		if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO))
			lDepOrdModel = leggiDatiDecreto(lDepOrdModel);

		// Inserisce, ove sia definito, il valore del campo "Ulteriori Descrizioni".
		if (!isRequestParameterNullObj(CAMPO_ULTERIORE_DESCRIZIONE))
			lDepOrdModel.setUlterioreDescrizione(getRequestStringParameter(CAMPO_ULTERIORE_DESCRIZIONE));

		// Ordinanza di Applicazione Sanzioni Sostitutiva (SS)
		// prevede il campo Cod Ufficio Competente.
		// Se presente viene memorizzato in Cod Ufficio Magistrato Competente
		// STUB: Attenzione l'utilizzo di questo campo in maniera improprio è sbagliato
		// perchè nella maschera di trasferimento Ordinanza tale codice viene decodificato
		// come Ufficio del Magistrato di Sorveglianza.
		// Bisogna utilizzare un altro campo per memorizzare tale campo.
		// Luigi 9-8-2007
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO)) {
			if (!getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO).equalsIgnoreCase("-"))
				lDepOrdModel.setCodUfficioMagistratoComp(getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO),
						getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO)));
			else
				lDepOrdModel.setCodUfficioMagistratoComp("-");
		}

		// MEV_2023-35: gestione Sospensione esecuzione pene accessorie
		String codOggettoProcedimento = mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		if (COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_TDS.equals(codOggettoProcedimento)
				|| COD_OGGETTO_SOSPENSIONE_ESECUZIONE_PENE_ACCESSORIE_UDS.equals(codOggettoProcedimento)) {
			lDepOrdModel.setCodTipoPenaAccessoria(
					getRequestStringParameter(ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA));
			if (!isRequestParameterNullEmptyObj(ICostantiPenaAccessoria.CAMPO_DURATA))
				lDepOrdModel.setDurata(getRequestStringParameter(ICostantiPenaAccessoria.CAMPO_DURATA));
			if (!isRequestParameterNullEmptyObj(ICostantiPenaAccessoria.CAMPO_NUM_ANNI))
				lDepOrdModel
						.setNumAnni(getRequestBigDecimalParameter(ICostantiPenaAccessoria.CAMPO_NUM_ANNI));
			if (!isRequestParameterNullEmptyObj(ICostantiPenaAccessoria.CAMPO_NUM_MESI))
				lDepOrdModel
						.setNumMesi(getRequestBigDecimalParameter(ICostantiPenaAccessoria.CAMPO_NUM_MESI));
			if (!isRequestParameterNullEmptyObj(ICostantiPenaAccessoria.CAMPO_NUM_GIORNI))
				lDepOrdModel.setNumGiorni(
						getRequestBigDecimalParameter(ICostantiPenaAccessoria.CAMPO_NUM_GIORNI));
		}

		// Aggiornamento Ordinanza effettuato
		aModel.setOrdinanza(lDepOrdModel);

		// /////////////////////////////////////////////////
		// Aggiornamento del template in Evento. //
		// ///////////////////////////////////////////////
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Aggiornamento del template in Evento.");
		if (!isRequestParameterNullObj(CAMPO_TIPO_ORDINANZA_DA_PRODURRE)) {
			EventoModel lEvento = aModel.getEvento();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CAMPO_TIPO_ORDINANZA_DA_PRODURRE = "
					+ getRequestStringParameter(CAMPO_TIPO_ORDINANZA_DA_PRODURRE));
			// Tipo di template da assegnare all'evento
			if (getRequestStringParameter(CAMPO_TIPO_ORDINANZA_DA_PRODURRE).equals("02"))
				lEvento.setTemIdTemplate(TEMPLATE_ORDINANZA_GENERICO);
			else if (getRequestStringParameter(CAMPO_TIPO_ORDINANZA_DA_PRODURRE).equals("03"))
				lEvento.setTemIdTemplate(TEMPLATE_ORDINANZA_RIGETTO_GENERICO);
			else if (getRequestStringParameter(CAMPO_TIPO_ORDINANZA_DA_PRODURRE).equals("04"))
				lEvento.setTemIdTemplate(TEMPLATE_ORDINANZA_NLP_GENERICO);
			else if (getRequestStringParameter(CAMPO_TIPO_ORDINANZA_DA_PRODURRE).equals("01")) {
			// generazione automantica
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Chiama la logica Decisionale per il template  ...");
				// Chiama la logica Decisionale per il template da associare.
				GestioneFlussoOrdinanza lGFO = new GestioneFlussoOrdinanza(aModel.getTenori());
				lGFO.start();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Template Associato : " + lGFO.getTemplate());
				// Inserisce qui nell'evento l'id del template, prima dell'inserimento.
				lEvento.setTemIdTemplate(lGFO.getTemplate());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Evento  >>> " + lEvento);

			// Aggiornamento Evento effettuato
			aModel.setEvento(lEvento);
		}

		// 05/06/2014 d.f. aggiunti i campi della Richiesta Ottemperanza
		if (!isRequestParameterNullObj(CAMPO_FLAG_NOMINA_COMM_ACTA)) {
			lDepOrdModel.setFlagNominaComActa(getRequestStringParameter(CAMPO_FLAG_NOMINA_COMM_ACTA));
		}

		if (!isRequestParameterNullObj(CAMPO_DESCR_COMM_ACTA)) {
			lDepOrdModel.setDescrCommActa(getRequestStringParameter(CAMPO_DESCR_COMM_ACTA));
		}

		return aModel;
	}

	/**
	 * Funzione di lettura dei dati del Decreto
	 *
	 * @param aDepOrdModel
	 * @return
	 * @throws F3BException
	 */
	private DepositoOrdinanzaPcModel leggiDatiDecreto(DepositoOrdinanzaPcModel aDepOrdModel)
			throws F3BException {

		String lCodTipoDecreto = getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO);
		if (lCodTipoDecreto.equalsIgnoreCase(ICostantiDepositoDecreto.SOPRAVVENIENZA_NT)
				|| lCodTipoDecreto.equalsIgnoreCase(ICostantiDepositoDecreto.RICOVERI)
				|| lCodTipoDecreto.equalsIgnoreCase(ICostantiDepositoDecreto.RICOVERO_OPG_OSS_PSICHE)
				|| lCodTipoDecreto.equalsIgnoreCase(ICostantiDepositoDecreto.DECLARATORIA_ESTINZIONE_SS)
				|| lCodTipoDecreto.equalsIgnoreCase(ICostantiDepositoDecreto.MODIFICA_PERMANENTE_SS)
				|| lCodTipoDecreto.equalsIgnoreCase(ICostantiDepositoDecreto.SOSPENSIONE_ESECUZIONE_SS)) {
			// Le NOTE nel campo COD_NATURA_PROVVEDIMENTO
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_NOTE))
				aDepOrdModel.setCodNaturaProvvedimento(
						getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_NOTE));

			// LUOGO SVOLGIMENTO DELLA PROVA
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA))
				aDepOrdModel.setLuogoSvolgimentoProva(
						getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA));

			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP)) {
				// Lettura Tribunale di SorveglianzaCompetente.
				String lDescUff = getRequestStringParameter(
						ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_TDS_COMP);
				if (lDescUff.trim().length() > 1) {
					// 28/03/2007 L'Ufficio (Prima TDS statico) può anche essere un UDS.
					// Codice Tribunale di sorveglianza competente in COD_UFFICIO_TDS_COMPETENTE
					// String lCodUffTDS = getCodUfficioByCodTipoUfficioDescrComune("TDS", lDescUff);
					// aDepOrdModel.setCodUffTdsConcessoRiduzione(lCodUffTDS);

					String lCodTipoUfficio = "TDS";
					if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_TIPO_UFFICIO)) {
						lCodTipoUfficio = getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO);
					}
					String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescUff);
					aDepOrdModel.setCodUffTdsConcessoRiduzione(lCodUfficio);
				}
			}

			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_PROCURA_COMP)) {
				// Lettura Procura Competente.
				String lDescUff = getRequestStringParameter(
						ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_PROCURA_COMP);
				if (lDescUff.trim().length() > 1) {
					// chiamato solo per controllo
					getCodUfficioByCodTipoUfficioDescrComune("PM", lDescUff);
					// Comune della Procura nel LUOGO_SVOLGIMENTO_PROVA
					aDepOrdModel.setLuogoSvolgimentoProva(lDescUff);
				}
			}
			// Istituto Detenzione in SERVIZIO TERAPEUTICO
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
				// Lettura Descrizione Istituto Detenzione.
				String lDescrIstDeten = getRequestStringParameter("Comune");
				if (lDescrIstDeten.trim().length() > 1) {
					// Descrizione Istituto di detenzione nel campo SERVIZIO_TERAPEUTICO
					aDepOrdModel.setServizioTerapeuticoComp(lDescrIstDeten);
				}
			}

			// Lettura COD Ufficio Magistrato
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
				// Controlla e imposta il codice ufficio magistrato competente.
				if (getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)
						.compareTo("") != 0)
					aDepOrdModel.setCodUfficioMagistratoComp(
							getCodUfficioByCodTipoUfficioDescrComune("UDS", getRequestStringParameter(
									ICostantiDepositoDecreto.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)));
				else
					aDepOrdModel.setCodUfficioMagistratoComp("-");
			}

			// Data Decorrenza Sospensione
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS)) {
				Date lDataDecorrenzaSS = getRequestDateParameter(
						ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS,
						ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS,
						ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS);
				aDepOrdModel.setDataSospensioneSS(lDataDecorrenzaSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Decorrenza Sospensione:" + lDataDecorrenzaSS);
			}

			// #### Periodo Sospensione #####
			// ------ Anni Periodo Sospensione ----
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS)) {
				BigDecimal lGiorniSospensioneSS = getRequestBigDecimalParameter(
						ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS);
				aDepOrdModel.setSospensioneAASS(lGiorniSospensioneSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Anni Periodo Sospensione" + lGiorniSospensioneSS);
			}
			// ------ Mesi Periodo Sospensione ----
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS)) {
				BigDecimal lMesiSospensioneSS = getRequestBigDecimalParameter(
						ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS);
				aDepOrdModel.setSospensioneMMSS(lMesiSospensioneSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Mesi Periodo Sospensione" + lMesiSospensioneSS);
			}
			// ------ Giorni Periodo Sospensione ----
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS)) {
				BigDecimal lGiorniSospensioneSS = getRequestBigDecimalParameter(
						ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS);
				aDepOrdModel.setSospensioneGGSS(lGiorniSospensioneSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Giorni Periodo Sospensione" + lGiorniSospensioneSS);
			}

			// Data Fino al
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS)
					&& !isRequestParameterNullObj(
							ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS)) {
				Date lDataFinoAlSS = getRequestDateParameter(
						ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS,
						ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS,
						ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS);
				aDepOrdModel.setDataScadenzaSospensioneSS(lDataFinoAlSS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data Fino al:" + lDataFinoAlSS);
			}

			// Flag Recupero SS
			if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_FLAG_RECUPERO_SS)) {
				String lFlagRecupero = getRequestStringParameter(
						ICostantiDepositoDecreto.CAMPO_FLAG_RECUPERO_SS);
				aDepOrdModel.setFlagRecuperoSS(lFlagRecupero);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Flag Recupero Sospensione:" + lFlagRecupero);
				// Se il flag recupero è impostato su "da recuperare"(valore= "S") carico anche il campo
				// "Numero Giorni"
				if (lFlagRecupero.equals("S")) {
					// Numero Giorni Recupero
					if (!isRequestParameterNullObj(ICostantiDepositoDecreto.CAMPO_GIORNI_RECUPERO_SS)) {
						BigDecimal lGiorniRecupero = getRequestBigDecimalParameter(
								ICostantiDepositoDecreto.CAMPO_GIORNI_RECUPERO_SS);
						aDepOrdModel.setGiorniRecuperoSS(lGiorniRecupero);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Giorni Recupero Sospensione:" + lGiorniRecupero);
					}
				}
			}
			// 03/03/2009 Dati relativi alle Richieste Conversioni Pene Pecuniarie.
			String lTipoRichiestaCPP = "";
			if (!isRequestParameterNullObj("numeroRichiesteCPP")) {
				// Si distingue tra i casi di Conversione e Rateizzazione.
				if (!isRequestParameterNullObj(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE))
					lTipoRichiestaCPP = getRequestStringParameter(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);

				// Come CodTipoOrdinanza si imposta il COD_OGGETTO (2470 o 2471).
				aDepOrdModel.setCodTipoOrdinanza(lTipoRichiestaCPP);
			}
		}
		return aDepOrdModel;
	}

	// Ticket#20200625014 - sius-rito2: questo metodo deve essere PUBLIC poichè
	// in conflitto con ActInserisciOrdinanzaLiberAnt.inserimento(...)
	public OrdinanzaEventoTenoriGProcModel inserimento(OrdinanzaEventoTenoriGProcModel aOrdEveTenGP)
			throws F3BException {

		OrdinanzaEventoTenoriGProcModel lObjRet = null;
		IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();

		// Variazione Fascicolo SIUS Origine
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE)) {
			BigDecimal lIdFascOrigineNew = getRequestBigDecimalParameter(
					ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE);
			BigDecimal lIdFascOrigineOld = mFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
			// Aggiornamento ID Fascicolo Origine solo se variato
			if (lIdFascOrigineOld == null || lIdFascOrigineNew.compareTo(lIdFascOrigineOld) != 0) {
				lObjRet = IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP, lIdFascOrigineNew);
			} else
				lObjRet = IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP);
		} else
			lObjRet = IDepOrdCtrl.ExInserisciOrdinanza(aOrdEveTenGP);

		return lObjRet;
	}

	private void elaboraMisuraAlternativa(OrdinanzaEventoTenoriGProcModel lOrdEveTenGP) throws F3BException {

		TenoreModel tenori[] = lOrdEveTenGP.getTenori();

		IMisuraAlternativa lCtrl = SIEPLookupRemote.getMisuraAlternativaRemote();
		for (TenoreModel tenore : tenori) {
			String codEsitoTenore = tenore.getCodEsitoTenore();
			if (ICostantiMisuraAlternativa.COD_ESITO_CONCEDE.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraAlternativa.COD_ESITO_CONCEDE_SOSPENSIONE_PENA_DETENZIONE_CASA
							.equalsIgnoreCase(codEsitoTenore)) {
				MisuraAlternativaModel misuraAlternativaModel = new MisuraAlternativaModel();

				BigDecimal idEvento = lOrdEveTenGP.getEvento().getIdEvento();
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
				misuraAlternativaModel.setCodMagistrato(lOrdEveTenGP.getOrdinanza().getCodMagistrato());
				misuraAlternativaModel.setFasSieIdFascicoloSiep(
						mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				misuraAlternativaModel.setDescrLuogoProva(getParameter(CAMPO_LUOGO_SVOLGIMENTO_PROVA));
				misuraAlternativaModel.setCodTipoUfficioScarcerazione("-");
				misuraAlternativaModel.setCodUfficioSorveglianza("-");
				misuraAlternativaModel.setCodAutoritaAltroTitolo("-");
				// MEV_62 [EC] 15/05/2018 - INIZIO
				misuraAlternativaModel.setChiaveUfficioFascicoloSius(
						mFasGPMod.getFascicoloSiusModel().getCodUfficioInserimento());

				if (lOrdEveTenGP.getOrdinanza().getDataCameraConsiglio() != null) {
					misuraAlternativaModel
							.setDataDecisione(lOrdEveTenGP.getOrdinanza().getDataCameraConsiglio());
				}
				if (lOrdEveTenGP.getOrdinanza().getAnnoS3() != null)
					misuraAlternativaModel.setAnnoRegistro(lOrdEveTenGP.getOrdinanza().getAnnoS3());
				if (lOrdEveTenGP.getOrdinanza().getNumS3() != null)
					misuraAlternativaModel.setNumeroRegistro(lOrdEveTenGP.getOrdinanza().getNumS3());
				// MEV 62 [EC] 15/05/2018 - FINE
				lCtrl.ExInserisciMisuraAlternativa(misuraAlternativaModel);
			}
		}
	}

	private void elaboraMisuraSicurezza(OrdinanzaEventoTenoriGProcModel lOrdEveTenGP) throws F3BException {

		TenoreModel tenori[] = lOrdEveTenGP.getTenori();

		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();

		for (TenoreModel tenore : tenori) {
			String codEsitoTenore = tenore.getCodEsitoTenore();
			if (ICostantiMisuraSicurezza.COD_DICHIARA_SCEMATA_PERICOLOSITA.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_TRASFORMA_MISURA.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_ACCERTA_PERICOLOSITA_SOCIALE
							.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_CONFERMA_PERICOLOSITA_PROROGA
							.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_SOSTITUISCE_MISURA.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_DETERMINA_PRESCRIZIONI.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_DETERMINA_PRESCRIZIONI.equalsIgnoreCase(codEsitoTenore)
					|| ICostantiMisuraSicurezza.COD_MODIFICA_LIBERAZIONE_CONDIZIONALE
							.equalsIgnoreCase(codEsitoTenore)) {

				MisuraSicurezzaModel misuraSicurezzaModel = new MisuraSicurezzaModel();

				BigDecimal idEvento = lOrdEveTenGP.getEvento().getIdEvento();
				misuraSicurezzaModel.setEveIdEvento(idEvento);

				misuraSicurezzaModel.setCodTipo(getTipoMisuraParameter());
				misuraSicurezzaModel.setCodNatura(getNaturaMisuraParameter());

				misuraSicurezzaModel.setFlFormaMisura(
						getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA));
				misuraSicurezzaModel.setDescrizioneComunita(
						getStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA));
				misuraSicurezzaModel.setCodUfficioInserimento(
						mFasGPMod.getFascicoloSiusModel().getCodUfficioInserimento());
				misuraSicurezzaModel.setCodOperatoreInserimento(
						mFasGPMod.getFascicoloSiusModel().getCodOperatoreInserimento());
				misuraSicurezzaModel
						.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				misuraSicurezzaModel.setNumAnni(getAnniMisuraParameter());
				misuraSicurezzaModel.setNumMesi(getMesiMisuraParameter());
				misuraSicurezzaModel.setNumGiorni(getGiorniMisuraParameter());
				misuraSicurezzaModel.setDataDecorrenza(getDataDecorrenzaMisuraParameter());
				misuraSicurezzaModel.setDataInserimento(DateUtils.getSysDate());
				lCtrl.ExInserisciMisuraSicurezza(misuraSicurezzaModel);
			}
		}
	}

	private BigDecimal getAnniMisuraParameter() throws F3BException {

		// MERGE v10: cambiata gestione metodo
		BigDecimal anni = null;
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI))
			anni = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA))
			anni = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA_TWO))
			anni = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA_TWO);

		else if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_PROROGA))
			anni = getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_PROROGA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA))
			anni = getBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA_TWO))
			anni = getBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA_TWO);

		return anni;
	}

	private BigDecimal getMesiMisuraParameter() throws F3BException {

		// MERGE v10: cambiata gestione metodo
		BigDecimal mesi = null;
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI))
			mesi = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA))
			mesi = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA_TWO))
			mesi = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA_TWO);

		else if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_PROROGA))
			mesi = getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_PROROGA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA))
			mesi = getBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA_TWO))
			mesi = getBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA_TWO);

		return mesi;
	}

	private BigDecimal getGiorniMisuraParameter() throws F3BException {

		// MERGE v10: cambiata gestione metodo
		BigDecimal giorni = null;
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI))
			giorni = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA))
			giorni = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA_TWO))
			giorni = getBigDecimalParameter(ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA_TWO);

		else if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_PROROGA))
			giorni = getBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_PROROGA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA))
			giorni = getBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA_TWO))
			giorni = getBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA_TWO);

		return giorni;
	}

	private Date getDataDecorrenzaMisuraParameter() throws F3BException {

		Date dataDecorrenzaMS = null;
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_DECORRENZA)) {
			dataDecorrenzaMS = getRequestDateParameter(CAMPO_ANNO_DATA_DECORRENZA, CAMPO_MESE_DATA_DECORRENZA,
					CAMPO_GIORNO_DATA_DECORRENZA);
		}

		// MERGE v10: modificata l'interfaccia di riferimento
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA)
				&& dataDecorrenzaMS == null) {
			dataDecorrenzaMS = getRequestDateParameter(
					ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA,
					ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA,
					ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA);
		}

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA)
				&& dataDecorrenzaMS == null) {
			dataDecorrenzaMS = getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA,
					ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PROROGA,
					ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PROROGA);
		}
		return dataDecorrenzaMS;
	}

	private String getNaturaMisuraParameter() throws F3BException {

		// MERGE v10: cambiata gestione metodo
		String natura = null;
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA))
			natura = getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_NATURA_MISURA_RIDETERMINATA))
			natura = getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_NATURA_MISURA_RIDETERMINATA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO))
			natura = getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA))
			natura = getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA_TWO))
			natura = getRequestStringParameter(
					ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA_TWO);

		if (natura == null || natura.equals("-"))
			natura = "02";

		return natura;
	}

	private String getTipoMisuraParameter() throws F3BException {

		// MERGE v10: cambiata gestione metodo
		String tipoMisura = null;
		if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO))
			tipoMisura = getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA))
			tipoMisura = getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA);

		else if (!isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO))
			tipoMisura = getRequestStringParameter(
					ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO);

		// else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_ESECUZIONE))
		// tipoMisura = getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_ESECUZIONE);

		// else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE))
		// tipoMisura =
		// getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA))
			tipoMisura = getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA);

		else if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO))
			tipoMisura = getRequestStringParameter(
					ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO);

		if (tipoMisura == null)
			tipoMisura = "-";

		return tipoMisura;
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