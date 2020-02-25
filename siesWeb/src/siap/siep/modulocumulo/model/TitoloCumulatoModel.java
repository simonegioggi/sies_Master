package siap.siep.modulocumulo.model;

/**
* <p>Title: TitoloCumulatoModel</p>
* <p>Description: Classe Model che rappresenta il TitoloCumulato</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.siep.modulocumulo.util.NotaDiTrasmissioneModel;
import siap.siep.sentenza.model.SentenzaModel;

public class TitoloCumulatoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -966367635508125977L;

	private BigDecimal mIdTitoloCumulato;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mDataIrrevocabilita;
	private BigDecimal mAnnoRegePm;
	private String mNumeroRegePm;
	private String mCodSedeNotiziaReato;
	private String mDescrSedeNotiziaReato;
	private BigDecimal mAnnoRegGen;
	private String mNumeroRegGen;
	private String mTipoRegGen;
	private Date mDataProvvedimento;
	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mNumSezioneAutoritaEmittente;
	private String mCodTipoRito;
	private String mDescrTipoRito;
	private String mCodTipoProvvedimentoRif;
	private String mDescrTipoProvvedimentoRif;
	private String mCodTipoProvvRif;
	private String mDescrTipoProvvRif;
	private Date mDataProvvRif;
	private BigDecimal mAnnoProvvRif;
	private String mNumeroProvvRif;
	private String mCodTipoAutoritaProvvRif;
	private String mDescrTipoAutoritaProvvRif;
	private String mCodLuogoProvvRif;
	private String mDescrLuogoProvvRif;
	private String mNumSezioneAutoritaProvvRif;
	private String mCodTipoRitoRif;
	private String mDescrTipoRitoRif;
	private String mCodTipoProvvedimentoAltro;
	private String mDescrTipoProvvedimentoAltro;
	private String mNote1DecisioneCassazione;
	private String mNote2DecisioneCassazione;
	private BigDecimal mAnnoSentenzaCassazione;
	private String mNumeroSentenzaCassazione;
	private BigDecimal mAnnoRaccoltaGenerale;
	private String mNumeroRaccoltaGenerale;
	private String mCodTipoDecisioneCassazione;
	private String mDescrTipoDecisioneCassazione;
	private String mNote;

	private BigDecimal mIstrIdIstruttoriaCumulo;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdSentenzaOrigine;

	private String mFlagEscluso;
	private BigDecimal mMessIdMessaggio;
	private String mTipoIscrizione;

	private Date mDataPresaInCarico;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private ProcedimentoCumulatoModel mProcedimentoCumulato;
	private SoggettoCumulatoModel mSoggettoCumulato;

	private PenaComplessivaCumuloModel mPenaComplessivaCumulo;
	// private SanzioneSostitutivaCumuloModel mSanzioneSostitutivaCumulo;
	// private List<ContinuazioneCumuloModel> mListaContinuazioniCumulo;
	private List<ReatoCircostanzaCumuloModel> mListaReatoCircostanzaCumulo; // Relativo ai Reati, distingue
																			// Prima Ipotesi e Successive
																			// (TAB. REATO_CUMULO)
	private List<ReatoCumuloModel> mListaReatiCumulo; // Relativo ai Reati (TAB. REATO_CUMULO)
	private List<CircostanzaCumuloModel> mListaCircostanzeCumulo; // Relativo alle Circostanze
																	// Aggravanti/Attenuanti
																	// (TAB.CIRCOSTANZE_CUMULO)
	private List<MisuraSicurezzaCumuloModel> mListaMisureSicurezzaCumulo;
	private List<PenaAccessoriaCumuloModel> mListaPeneAccessorieCumulo;
	private List<MisuraCautelareCumuloModel> mListaMisureCautelariCumulo;
	private List<BeneficioCumuloModel> mListaBeneficiCumulo;
	private List<StatoEsecTitoloCumulatoModel> mListaStatoEsecuzioneTitoloCum;

	private BeneficioCumuloModel mBeneficioCumulo;
	private SanzioneSostitutivaCumuloModel mSanzioneSostitutivaCumulo;
	private StatoEsecTitoloCumulatoModel mStatoEsecTitoloCumulato;

	// private NotaDiTrasmissioneModel mNotaTrasmissione;
	private Vector<NotaDiTrasmissioneModel> mNotaTrasmissione;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public TitoloCumulatoModel() {
		mIdTitoloCumulato = null;
		mCodTipoProvvedimento = "";
		mDescrTipoProvvedimento = "";
		mDataIrrevocabilita = null;
		mAnnoRegePm = null;
		mNumeroRegePm = "";
		mCodSedeNotiziaReato = "";
		mDescrSedeNotiziaReato = "";
		mAnnoRegGen = null;
		mNumeroRegGen = "";
		mTipoRegGen = "";
		mDataProvvedimento = null;
		mAnnoSentenza = null;
		mNumeroSentenza = "";
		mCodTipoAutoritaEmittente = "";
		mDescrTipoAutoritaEmittente = "";
		mCodLuogoEmittente = "";
		mDescrLuogoEmittente = "";
		mNumSezioneAutoritaEmittente = "";
		mCodTipoRito = "";
		mDescrTipoRito = "";
		mCodTipoProvvedimentoRif = "";
		mDescrTipoProvvedimentoRif = "";
		mCodTipoProvvRif = "";
		mDescrTipoProvvRif = "";
		mDataProvvRif = null;
		mAnnoProvvRif = null;
		mNumeroProvvRif = "";
		mCodTipoAutoritaProvvRif = "";
		mDescrTipoAutoritaProvvRif = "";
		mCodLuogoProvvRif = "";
		mDescrLuogoProvvRif = "";
		mNumSezioneAutoritaProvvRif = "";
		mCodTipoRitoRif = "";
		mDescrTipoRitoRif = "";
		mCodTipoProvvedimentoAltro = "";
		mDescrTipoProvvedimentoAltro = "";
		mNote1DecisioneCassazione = "";
		mNote2DecisioneCassazione = "";
		mAnnoSentenzaCassazione = null;
		mNumeroSentenzaCassazione = "";
		mAnnoRaccoltaGenerale = null;
		mNumeroRaccoltaGenerale = "";
		mCodTipoDecisioneCassazione = "";
		mDescrTipoDecisioneCassazione = "";
		mNote = "";

		mIstrIdIstruttoriaCumulo = null;
		mFlagStato = "";
		mMotivoModifica = "";
		mIdSentenzaOrigine = null;
		mFlagEscluso = "";
		mMessIdMessaggio = null;
		mTipoIscrizione = "";
		mDataPresaInCarico = null;

		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodUfficioInserimento = "";
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mCodUfficioAggiornamento = "";

		mProcedimentoCumulato = null;
		mSoggettoCumulato = null;

		mPenaComplessivaCumulo = null;
		// mSanzioneSostitutivaCumulo = null;
		// mListaContinuazioniCumulo = null;
		mListaReatoCircostanzaCumulo = null;
		mListaReatiCumulo = null;
		mListaCircostanzeCumulo = null;
		mListaMisureSicurezzaCumulo = null;
		mListaPeneAccessorieCumulo = null;
		mListaMisureCautelariCumulo = null;
		mListaBeneficiCumulo = null;
		mListaStatoEsecuzioneTitoloCum = null;

		mBeneficioCumulo = null;
		mSanzioneSostitutivaCumulo = null;
		mStatoEsecTitoloCumulato = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public TitoloCumulatoModel(TitoloCumulatoModel aModel) {
		mIdTitoloCumulato = aModel.mIdTitoloCumulato;
		mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		mDataIrrevocabilita = aModel.mDataIrrevocabilita;
		mAnnoRegePm = aModel.mAnnoRegePm;
		mNumeroRegePm = aModel.mNumeroRegePm;
		mCodSedeNotiziaReato = aModel.mCodSedeNotiziaReato;
		mDescrSedeNotiziaReato = aModel.mDescrSedeNotiziaReato;
		mAnnoRegGen = aModel.mAnnoRegGen;
		mNumeroRegGen = aModel.mNumeroRegGen;
		mTipoRegGen = aModel.mTipoRegGen;
		mDataProvvedimento = aModel.mDataProvvedimento;
		mAnnoSentenza = aModel.mAnnoSentenza;
		mNumeroSentenza = aModel.mNumeroSentenza;
		mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		mNumSezioneAutoritaEmittente = aModel.mNumSezioneAutoritaEmittente;
		mCodTipoRito = aModel.mCodTipoRito;
		mDescrTipoRito = aModel.mDescrTipoRito;
		mCodTipoProvvedimentoRif = aModel.mCodTipoProvvedimentoRif;
		mDescrTipoProvvedimentoRif = aModel.mDescrTipoProvvedimentoRif;
		mCodTipoProvvRif = aModel.mCodTipoProvvRif;
		mDescrTipoProvvRif = aModel.mDescrTipoProvvRif;
		mDataProvvRif = aModel.mDataProvvRif;
		mAnnoProvvRif = aModel.mAnnoProvvRif;
		mNumeroProvvRif = aModel.mNumeroProvvRif;
		mCodTipoAutoritaProvvRif = aModel.mCodTipoAutoritaProvvRif;
		mDescrTipoAutoritaProvvRif = aModel.mDescrTipoAutoritaProvvRif;
		mCodLuogoProvvRif = aModel.mCodLuogoProvvRif;
		mDescrLuogoProvvRif = aModel.mDescrLuogoProvvRif;
		mNumSezioneAutoritaProvvRif = aModel.mNumSezioneAutoritaProvvRif;
		mCodTipoRitoRif = aModel.mCodTipoRitoRif;
		mDescrTipoRitoRif = aModel.mDescrTipoRitoRif;
		mCodTipoProvvedimentoAltro = aModel.mCodTipoProvvedimentoAltro;
		mDescrTipoProvvedimentoAltro = aModel.mDescrTipoProvvedimentoAltro;
		mNote1DecisioneCassazione = aModel.mNote1DecisioneCassazione;
		mNote2DecisioneCassazione = aModel.mNote2DecisioneCassazione;
		mAnnoSentenzaCassazione = aModel.mAnnoSentenzaCassazione;
		mNumeroSentenzaCassazione = aModel.mNumeroSentenzaCassazione;
		mAnnoRaccoltaGenerale = aModel.mAnnoRaccoltaGenerale;
		mNumeroRaccoltaGenerale = aModel.mNumeroRaccoltaGenerale;
		mCodTipoDecisioneCassazione = aModel.mCodTipoDecisioneCassazione;
		mDescrTipoDecisioneCassazione = aModel.mDescrTipoDecisioneCassazione;
		mNote = aModel.mNote;

		mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;
		mFlagStato = aModel.mFlagStato;
		mMotivoModifica = aModel.mMotivoModifica;
		mIdSentenzaOrigine = aModel.mIdSentenzaOrigine;
		mFlagEscluso = aModel.mFlagEscluso;
		mMessIdMessaggio = aModel.mMessIdMessaggio;
		mTipoIscrizione = aModel.mTipoIscrizione;
		mDataPresaInCarico = aModel.mDataPresaInCarico;

		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public TitoloCumulatoModel(BigDecimal aIdTitoloCumulato, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, Date aDataIrrevocabilita, BigDecimal aAnnoRegePm,
			String aNumeroRegePm, String aCodSedeNotiziaReato, String aDescrSedeNotiziaReato,
			BigDecimal aAnnoRegGen, String aNumeroRegGen, String aTipoRegGen,

			Date aDataProvvedimento, BigDecimal aAnnoSentenza, String aNumeroSentenza,
			String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente, String aCodLuogoEmittente,
			String aDescrLuogoEmittente, String aNumSezioneAutoritaEmittente, String aCodTipoRito,
			String aDescrTipoRito,

			String aCodTipoProvvedimentoRif, String aDescrTipoProvvedimentoRif, String aCodTipoProvvRif,
			String aDescrTipoProvvRif, Date aDataProvvRif, BigDecimal aAnnoProvvRif, String aNumeroProvvRif,
			String aCodTipoAutoritaProvvRif, String aDescrTipoAutoritaProvvRif, String aCodLuogoProvvRif,
			String aDescrLuogoProvvRif, String aNumSezioneAutoritaProvvRif, String aCodTipoRitoRif,
			String aDescrTipoRitoRif,

			String aCodTipoProvvedimentoAltro, String aDescrTipoProvvedimentoAltro,
			String aNote1DecisioneCassazione, String aNote2DecisioneCassazione,
			BigDecimal aAnnoSentenzaCassazione, String aNumeroSentenzaCassazione,
			BigDecimal aAnnoRaccoltaGenerale, String aNumeroRaccoltaGenerale,
			String aCodTipoDecisioneCassazione, String aDescrTipoDecisioneCassazione, String aNote,

			BigDecimal aIstrIdIstruttoriaCumulo, String aFlagStato, String aMotivoModifica,
			BigDecimal aIdSentenzaOrigine, String aFlagEscluso, BigDecimal aMessIdMessaggio,
			String aTipoIscrizione, Date aDataPresaInCarico,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		mIdTitoloCumulato = aIdTitoloCumulato;
		mCodTipoProvvedimento = aCodTipoProvvedimento;
		mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		mDataIrrevocabilita = aDataIrrevocabilita;
		mAnnoRegePm = aAnnoRegePm;
		mNumeroRegePm = aNumeroRegePm;
		mCodSedeNotiziaReato = aCodSedeNotiziaReato;
		mDescrSedeNotiziaReato = aDescrSedeNotiziaReato;
		mAnnoRegGen = aAnnoRegGen;
		mNumeroRegGen = aNumeroRegGen;
		mTipoRegGen = aTipoRegGen;
		mDataProvvedimento = aDataProvvedimento;
		mAnnoSentenza = aAnnoSentenza;
		mNumeroSentenza = aNumeroSentenza;
		mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		mCodLuogoEmittente = aCodLuogoEmittente;
		mDescrLuogoEmittente = aDescrLuogoEmittente;
		mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;
		mCodTipoRito = aCodTipoRito;
		mDescrTipoRito = aDescrTipoRito;
		mCodTipoProvvedimentoRif = aCodTipoProvvedimentoRif;
		mDescrTipoProvvedimentoRif = aDescrTipoProvvedimentoRif;
		mCodTipoProvvRif = aCodTipoProvvRif;
		mDescrTipoProvvRif = aDescrTipoProvvRif;
		mDataProvvRif = aDataProvvRif;
		mAnnoProvvRif = aAnnoProvvRif;
		mNumeroProvvRif = aNumeroProvvRif;
		mCodTipoAutoritaProvvRif = aCodTipoAutoritaProvvRif;
		mDescrTipoAutoritaProvvRif = aDescrTipoAutoritaProvvRif;
		mCodLuogoProvvRif = aCodLuogoProvvRif;
		mDescrLuogoProvvRif = aDescrLuogoProvvRif;
		mNumSezioneAutoritaProvvRif = aNumSezioneAutoritaProvvRif;
		mCodTipoRitoRif = aCodTipoRitoRif;
		mDescrTipoRitoRif = aDescrTipoRitoRif;
		mCodTipoProvvedimentoAltro = aCodTipoProvvedimentoAltro;
		mDescrTipoProvvedimentoAltro = aDescrTipoProvvedimentoAltro;
		mNote1DecisioneCassazione = aNote1DecisioneCassazione;
		mNote2DecisioneCassazione = aNote2DecisioneCassazione;
		mAnnoSentenzaCassazione = aAnnoSentenzaCassazione;
		mNumeroSentenzaCassazione = aNumeroSentenzaCassazione;
		mAnnoRaccoltaGenerale = aAnnoRaccoltaGenerale;
		mNumeroRaccoltaGenerale = aNumeroRaccoltaGenerale;
		mCodTipoDecisioneCassazione = aCodTipoDecisioneCassazione;
		mDescrTipoDecisioneCassazione = aDescrTipoDecisioneCassazione;
		mNote = aNote;

		mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;
		mFlagStato = aFlagStato;
		mMotivoModifica = aMotivoModifica;
		mIdSentenzaOrigine = aIdSentenzaOrigine;
		mFlagEscluso = aFlagEscluso;
		mMessIdMessaggio = aMessIdMessaggio;
		mTipoIscrizione = aTipoIscrizione;
		mDataPresaInCarico = aDataPresaInCarico;

		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	/**
	 * Costruttore che inizializza il model estraendo i dati da un record Sentenza
	 * 
	 * @param aModel
	 */
	public TitoloCumulatoModel(SentenzaModel aSentenzaModel) {
		mCodTipoProvvedimento = aSentenzaModel.getCodTipoProvvedimento();
		// mDataIrrevocabilita = aSentenzaModel.); // Da recuperare sul fascicolo_siep
		mAnnoRegePm = aSentenzaModel.getAnnoRegePm();
		mNumeroRegePm = aSentenzaModel.getNumeroRegePm();
		mCodSedeNotiziaReato = aSentenzaModel.getCodSedeNotiziaReato();

		mTipoRegGen = "-";

		// Anno/Numero e tipo Registro Generale
		if (aSentenzaModel.getAnnoRegeCap() != null) {
			mAnnoRegGen = aSentenzaModel.getAnnoRegeCap();
			mNumeroRegGen = aSentenzaModel.getNumeroRegeCap();
			mTipoRegGen = "CAP";
		} else if (aSentenzaModel.getAnnoRegeCas() != null) {
			mAnnoRegGen = aSentenzaModel.getAnnoRegeCas();
			mNumeroRegGen = aSentenzaModel.getNumeroRegeCas();
			mTipoRegGen = "CAS";
		} else if (aSentenzaModel.getAnnoRegeDib() != null) {
			mAnnoRegGen = aSentenzaModel.getAnnoRegeDib();
			mNumeroRegGen = aSentenzaModel.getNumeroRegeDib();
			mTipoRegGen = "DIB";
		} else if (aSentenzaModel.getAnnoRegeCasap() != null) {
			mAnnoRegGen = aSentenzaModel.getAnnoRegeCasap();
			mNumeroRegGen = aSentenzaModel.getNumeroRegeCasap();
			mTipoRegGen = "CASAP";
		} else if (aSentenzaModel.getAnnoRegeGip() != null) {
			mAnnoRegGen = aSentenzaModel.getAnnoRegeGip();
			mNumeroRegGen = aSentenzaModel.getNumeroRegeGip();
			mTipoRegGen = "GIP";
		}

		// Sentenza da eseguire
		mDataProvvedimento = aSentenzaModel.getDataProvvedimento();
		mAnnoSentenza = aSentenzaModel.getAnnoSentenza();
		mNumeroSentenza = aSentenzaModel.getNumeroSentenza();

		mCodTipoAutoritaEmittente = aSentenzaModel.getCodTipoAutoritaEmittente();
		mCodLuogoEmittente = aSentenzaModel.getCodLuogoEmittente();
		mNumSezioneAutoritaEmittente = aSentenzaModel.getNumSezioneAutoritaEmittente();

		// Attenzione! il campo SENTENZA.COD_TIPO_RITO può riferirsi alla 'Sentenza da eseguire'
		// o ad 'altro grado di giudizio'. Sulla tabella è previsto un solo campo
		// in quanto può essere valorizzato solo per uno dei due.
		// Per riuscire a capire a quale dei due si riferisce (se presente)
		// va testata la tipologia di autorità emittente.
		// Tipo rito è previsto solo per: DIB/TRIBSD
		if (aSentenzaModel.getCodTipoRito() != null && !"".equals(aSentenzaModel.getCodTipoRito())) { // Tipo
																										// rito
																										// è
																										// valorizzato
			if ("DIB".equals(aSentenzaModel.getCodTipoAutoritaEmittente())
					|| "TRIBSD".equals(aSentenzaModel.getCodTipoAutoritaEmittente())) { //
				mCodTipoRito = aSentenzaModel.getCodTipoRito();
			} else {
				mCodTipoRitoRif = aSentenzaModel.getCodTipoRito();
			}
		}

		// Altro grado di giudizio
		mCodTipoProvvedimentoRif = aSentenzaModel.getCodTipoProvvedimentoRif();
		mCodTipoProvvRif = aSentenzaModel.getCodTipoProvvRif();
		mDataProvvRif = aSentenzaModel.getDataProvvRif();
		mAnnoProvvRif = aSentenzaModel.getAnnoProvvRif();
		mNumeroProvvRif = aSentenzaModel.getNumeroProvvRif();
		mCodTipoAutoritaProvvRif = aSentenzaModel.getCodTipoAutoritaProvvRif();
		mCodLuogoProvvRif = aSentenzaModel.getCodLuogoProvvRif();
		mNumSezioneAutoritaProvvRif = aSentenzaModel.getNumSezioneAutoritaProvvRif();
		// mCodTipoRitoRif = Vedi SU

		mCodTipoProvvedimentoAltro = aSentenzaModel.getCodTipoProvvedimentoAltro();

		mNote1DecisioneCassazione = aSentenzaModel.getNote1DecisioneCassazione();
		mNote2DecisioneCassazione = aSentenzaModel.getNote2DecisioneCassazione();
		mAnnoSentenzaCassazione = aSentenzaModel.getAnnoSentenzaCassazione();
		mNumeroSentenzaCassazione = aSentenzaModel.getNumeroSentenzaCassazione();
		mAnnoRaccoltaGenerale = aSentenzaModel.getAnnoRaccoltaGenerale();
		mNumeroRaccoltaGenerale = aSentenzaModel.getNumeroRaccoltaGenerale();
		mCodTipoDecisioneCassazione = aSentenzaModel.getCodTipoDecisioneCassazione();

		mNote = aSentenzaModel.getNote();

		mIdSentenzaOrigine = aSentenzaModel.getIdSentenza();
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdTitoloCumulato() {
		return mIdTitoloCumulato;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getDataIrrevocabilita() {
		return mDataIrrevocabilita;
	}

	public BigDecimal getAnnoRegePm() {
		return mAnnoRegePm;
	}

	public String getNumeroRegePm() {
		return mNumeroRegePm;
	}

	public String getCodSedeNotiziaReato() {
		return mCodSedeNotiziaReato;
	}

	public String getDescrSedeNotiziaReato() {
		return mDescrSedeNotiziaReato;
	}

	public BigDecimal getAnnoRegGen() {
		return mAnnoRegGen;
	}

	public String getNumeroRegGen() {
		return mNumeroRegGen;
	}

	public String getTipoRegGen() {
		return mTipoRegGen;
	}

	// Provvedimento
	public Date getDataProvvedimento() {
		return mDataProvvedimento;
	}

	public BigDecimal getAnnoSentenza() {
		return mAnnoSentenza;
	}

	public String getNumeroSentenza() {
		return mNumeroSentenza;
	}

	public String getCodTipoAutoritaEmittente() {
		return mCodTipoAutoritaEmittente;
	}

	public String getDescrTipoAutoritaEmittente() {
		return mDescrTipoAutoritaEmittente;
	}

	public String getCodLuogoEmittente() {
		return mCodLuogoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getNumSezioneAutoritaEmittente() {
		return mNumSezioneAutoritaEmittente;
	}

	public String getCodTipoRito() {
		return mCodTipoRito;
	}

	public String getDescrTipoRito() {
		return mDescrTipoRito;
	}

	// Altro grado di giudizio
	public String getCodTipoProvvedimentoRif() {
		return mCodTipoProvvedimentoRif;
	}

	public String getDescrTipoProvvedimentoRif() {
		return mDescrTipoProvvedimentoRif;
	}

	public String getCodTipoProvvRif() {
		return mCodTipoProvvRif;
	}

	public String getDescrTipoProvvRif() {
		return mDescrTipoProvvRif;
	}

	public Date getDataProvvRif() {
		return mDataProvvRif;
	}

	public BigDecimal getAnnoProvvRif() {
		return mAnnoProvvRif;
	}

	public String getNumeroProvvRif() {
		return mNumeroProvvRif;
	}

	public String getCodTipoAutoritaProvvRif() {
		return mCodTipoAutoritaProvvRif;
	}

	public String getDescrTipoAutoritaProvvRif() {
		return mDescrTipoAutoritaProvvRif;
	}

	public String getCodLuogoProvvRif() {
		return mCodLuogoProvvRif;
	}

	public String getDescrLuogoProvvRif() {
		return mDescrLuogoProvvRif;
	}

	public String getNumSezioneAutoritaProvvRif() {
		return mNumSezioneAutoritaProvvRif;
	}

	public String getCodTipoRitoRif() {
		return mCodTipoRitoRif;
	}

	public String getDescrTipoRitoRif() {
		return mDescrTipoRitoRif;
	}

	// Casssazione
	public String getCodTipoProvvedimentoAltro() {
		return mCodTipoProvvedimentoAltro;
	}

	public String getDescrTipoProvvedimentoAltro() {
		return mDescrTipoProvvedimentoAltro;
	}

	public String getNote1DecisioneCassazione() {
		return mNote1DecisioneCassazione;
	}

	public String getNote2DecisioneCassazione() {
		return mNote2DecisioneCassazione;
	}

	public BigDecimal getAnnoSentenzaCassazione() {
		return mAnnoSentenzaCassazione;
	}

	public String getNumeroSentenzaCassazione() {
		return mNumeroSentenzaCassazione;
	}

	public BigDecimal getAnnoRaccoltaGenerale() {
		return mAnnoRaccoltaGenerale;
	}

	public String getNumeroRaccoltaGenerale() {
		return mNumeroRaccoltaGenerale;
	}

	public String getCodTipoDecisioneCassazione() {
		return mCodTipoDecisioneCassazione;
	}

	public String getDescrTipoDecisioneCassazione() {
		return mDescrTipoDecisioneCassazione;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getIdSentenzaOrigine() {
		return mIdSentenzaOrigine;
	}

	public String getFlagEscluso() {
		return mFlagEscluso;
	}

	public BigDecimal getMessIdMessaggio() {
		return mMessIdMessaggio;
	}

	public String getTipoIscrizione() {
		return mTipoIscrizione;
	}

	public Date getDataPresaInCarico() {
		return mDataPresaInCarico;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public ProcedimentoCumulatoModel getProcedimentoCumulato() {
		return mProcedimentoCumulato;
	}

	public SoggettoCumulatoModel getSoggettoCumulato() {
		return mSoggettoCumulato;
	}

	public PenaComplessivaCumuloModel getPenaComplessivaCumulo() {
		return mPenaComplessivaCumulo;
	}

	// public SanzioneSostitutivaCumuloModel getSanzioneSostitutivaCumulo() { return
	// mSanzioneSostitutivaCumulo; }
	// public List<ContinuazioneCumuloModel> getContinuazioniCumulo() { return mListaContinuazioniCumulo; }
	public List<ReatoCircostanzaCumuloModel> getReatoCircostanzaCumulo() {
		return mListaReatoCircostanzaCumulo;
	}

	public List<ReatoCumuloModel> getReatiCumulo() {
		return mListaReatiCumulo;
	}

	public List<CircostanzaCumuloModel> getCircostanzeCumulo() {
		return mListaCircostanzeCumulo;
	}

	public List<MisuraSicurezzaCumuloModel> getMisureSicurezzaCumulo() {
		return mListaMisureSicurezzaCumulo;
	}

	public List<PenaAccessoriaCumuloModel> getPeneAccessorieCumulo() {
		return mListaPeneAccessorieCumulo;
	}

	public List<MisuraCautelareCumuloModel> getMisureCautelariCumulo() {
		return mListaMisureCautelariCumulo;
	}

	public List<BeneficioCumuloModel> getBeneficiCumulo() {
		return mListaBeneficiCumulo;
	}

	public List<StatoEsecTitoloCumulatoModel> getStatoEsecuzioneTitoloCumulato() {
		return mListaStatoEsecuzioneTitoloCum;
	}

	public BeneficioCumuloModel getBeneficioCumulato() {
		return mBeneficioCumulo;
	}

	public SanzioneSostitutivaCumuloModel getSanzioneSostitutivaCumulo() {
		return mSanzioneSostitutivaCumulo;
	}

	public StatoEsecTitoloCumulatoModel getStatoEsecTitoloCumulato() {
		return mStatoEsecTitoloCumulato;
	}

	// public NotaDiTrasmissioneModel getNotaTrasmissione() {return mNotaTrasmissione;}
	public Vector<NotaDiTrasmissioneModel> getNotaTrasmissione() {
		return mNotaTrasmissione;
	}

	// --
	public String getEstremiProvvedimento() {
		String lProvv = "";
		if (getDescrTipoProvvedimento() != null)
			lProvv += " con " + getDescrTipoProvvedimento();

		if (getNumeroSentenza() != null && getAnnoSentenza() != null)
			lProvv += " N. " + getAnnoSentenza().toString() + "/" + getNumeroSentenza();

		if (getDataProvvedimento() != null)
			lProvv += " del " + DateUtils.getDateToString(getDataProvvedimento(), "dd-MM-yyyy");

		if (getDataIrrevocabilita() != null) {
			if (getCodTipoProvvedimento().compareTo("02") == 0
					|| getCodTipoProvvedimento().compareTo("04") == 0) {
				lProvv += ", definitivo in data "
						+ DateUtils.getDateToString(getDataIrrevocabilita(), "dd-MM-yyyy");
			} else {
				lProvv += ", definitiva in data "
						+ DateUtils.getDateToString(getDataIrrevocabilita(), "dd-MM-yyyy");
			}
		}

		if (getCodTipoAutoritaEmittente() != null && getCodLuogoEmittente() != null) {
			if (getCodTipoProvvedimento().compareTo("02") == 0
					|| getCodTipoProvvedimento().compareTo("04") == 0) {
				lProvv += ", emesso da " + getDescrTipoAutoritaEmittente() + " di "
						+ getDescrLuogoEmittente();
			} else {
				lProvv += ", emessa da " + getDescrTipoAutoritaEmittente() + " di "
						+ getDescrLuogoEmittente();
			}
		}

		return lProvv;
	}

	// --

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdTitoloCumulato(BigDecimal aValore) {
		mIdTitoloCumulato = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setDataIrrevocabilita(Date aValore) {
		mDataIrrevocabilita = aValore;
	}

	public void setAnnoRegePm(BigDecimal aValore) {
		mAnnoRegePm = aValore;
	}

	public void setNumeroRegePm(String aValore) {
		mNumeroRegePm = aValore;
	}

	public void setCodSedeNotiziaReato(String aValore) {
		mCodSedeNotiziaReato = aValore;
	}

	public void setDescrSedeNotiziaReato(String aValore) {
		mDescrSedeNotiziaReato = aValore;
	}

	public void setAnnoRegGen(BigDecimal aValore) {
		mAnnoRegGen = aValore;
	}

	public void setNumeroRegGen(String aValore) {
		mNumeroRegGen = aValore;
	}

	public void setTipoRegGen(String aValore) {
		mTipoRegGen = aValore;
	}

	public void setDataProvvedimento(Date aValore) {
		mDataProvvedimento = aValore;
	}

	public void setAnnoSentenza(BigDecimal aValore) {
		mAnnoSentenza = aValore;
	}

	public void setNumeroSentenza(String aValore) {
		mNumeroSentenza = aValore;
	}

	public void setCodTipoAutoritaEmittente(String aValore) {
		mCodTipoAutoritaEmittente = aValore;
	}

	public void setDescrTipoAutoritaEmittente(String aValore) {
		mDescrTipoAutoritaEmittente = aValore;
	}

	public void setCodLuogoEmittente(String aValore) {
		mCodLuogoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setNumSezioneAutoritaEmittente(String aValore) {
		mNumSezioneAutoritaEmittente = aValore;
	}

	public void setCodTipoRito(String aValore) {
		mCodTipoRito = aValore;
	}

	public void setDescrTipoRito(String aValore) {
		mDescrTipoRito = aValore;
	}

	public void setCodTipoProvvedimentoRif(String aValore) {
		mCodTipoProvvedimentoRif = aValore;
	}

	public void setDescrTipoProvvedimentoRif(String aValore) {
		mDescrTipoProvvedimentoRif = aValore;
	}

	public void setCodTipoProvvRif(String aValore) {
		mCodTipoProvvRif = aValore;
	}

	public void setDescrTipoProvvRif(String aValore) {
		mDescrTipoProvvRif = aValore;
	}

	public void setDataProvvRif(Date aValore) {
		mDataProvvRif = aValore;
	}

	public void setAnnoProvvRif(BigDecimal aValore) {
		mAnnoProvvRif = aValore;
	}

	public void setNumeroProvvRif(String aValore) {
		mNumeroProvvRif = aValore;
	}

	public void setCodTipoAutoritaProvvRif(String aValore) {
		mCodTipoAutoritaProvvRif = aValore;
	}

	public void setDescrTipoAutoritaProvvRif(String aValore) {
		mDescrTipoAutoritaProvvRif = aValore;
	}

	public void setCodLuogoProvvRif(String aValore) {
		mCodLuogoProvvRif = aValore;
	}

	public void setDescrLuogoProvvRif(String aValore) {
		mDescrLuogoProvvRif = aValore;
	}

	public void setNumSezioneAutoritaProvvRif(String aValore) {
		mNumSezioneAutoritaProvvRif = aValore;
	}

	public void setCodTipoRitoRif(String aValore) {
		mCodTipoRitoRif = aValore;
	}

	public void setDescrTipoRitoRif(String aValore) {
		mDescrTipoRitoRif = aValore;
	}

	public void setCodTipoProvvedimentoAltro(String aValore) {
		mCodTipoProvvedimentoAltro = aValore;
	}

	public void setDescrTipoProvvedimentoAltro(String aValore) {
		mDescrTipoProvvedimentoAltro = aValore;
	}

	public void setNote1DecisioneCassazione(String aValore) {
		mNote1DecisioneCassazione = aValore;
	}

	public void setNote2DecisioneCassazione(String aValore) {
		mNote2DecisioneCassazione = aValore;
	}

	public void setAnnoSentenzaCassazione(BigDecimal aValore) {
		mAnnoSentenzaCassazione = aValore;
	}

	public void setNumeroSentenzaCassazione(String aValore) {
		mNumeroSentenzaCassazione = aValore;
	}

	public void setAnnoRaccoltaGenerale(BigDecimal aValore) {
		mAnnoRaccoltaGenerale = aValore;
	}

	public void setNumeroRaccoltaGenerale(String aValore) {
		mNumeroRaccoltaGenerale = aValore;
	}

	public void setCodTipoDecisioneCassazione(String aValore) {
		mCodTipoDecisioneCassazione = aValore;
	}

	public void setDescrTipoDecisioneCassazione(String aValore) {
		mDescrTipoDecisioneCassazione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setIdSentenzaOrigine(BigDecimal aValore) {
		mIdSentenzaOrigine = aValore;
	}

	public void setFlagEscluso(String aValore) {
		mFlagEscluso = aValore;
	}

	public void setMessIdMessaggio(BigDecimal aValore) {
		mMessIdMessaggio = aValore;
	}

	public void setTipoIscrizione(String aValore) {
		mTipoIscrizione = aValore;
	}

	public void setDataPresaInCarico(Date aValore) {
		mDataPresaInCarico = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setProcedimentoCumulato(ProcedimentoCumulatoModel aValore) {
		mProcedimentoCumulato = aValore;
	}

	public void setSoggettoCumulato(SoggettoCumulatoModel aValore) {
		mSoggettoCumulato = aValore;
	}

	public void setPenaComplessivaCumulo(PenaComplessivaCumuloModel aValore) {
		mPenaComplessivaCumulo = aValore;
	}

	// public void setSanzioneSostitutivaCumulo (SanzioneSostitutivaCumuloModel aValore) {
	// mSanzioneSostitutivaCumulo = aValore; }
	// public void setContinuazioniCumulo (List<ContinuazioneCumuloModel> aValore) { mListaContinuazioniCumulo
	// = aValore; }
	public void setReatoCircostanzaCumulo(List<ReatoCircostanzaCumuloModel> aValore) {
		mListaReatoCircostanzaCumulo = aValore;
	}

	public void setReatiCumulo(List<ReatoCumuloModel> aValore) {
		mListaReatiCumulo = aValore;
	}

	public void setCircostanzeCumulo(List<CircostanzaCumuloModel> aValore) {
		mListaCircostanzeCumulo = aValore;
	}

	public void setMisureSicurezzaCumulo(List<MisuraSicurezzaCumuloModel> aValore) {
		mListaMisureSicurezzaCumulo = aValore;
	}

	public void setPeneAccessorieCumulo(List<PenaAccessoriaCumuloModel> aValore) {
		mListaPeneAccessorieCumulo = aValore;
	}

	public void setMisureCautelariCumulo(List<MisuraCautelareCumuloModel> aValore) {
		mListaMisureCautelariCumulo = aValore;
	}

	public void setBeneficiCumulo(List<BeneficioCumuloModel> aValore) {
		mListaBeneficiCumulo = aValore;
	}

	public void setStatoEsecuzioneTitoloCumulato(List<StatoEsecTitoloCumulatoModel> aValore) {
		mListaStatoEsecuzioneTitoloCum = aValore;
	}

	public void setBeneficioCumulato(BeneficioCumuloModel aValore) {
		mBeneficioCumulo = aValore;
	}

	public void setSanzioneSostitutivaCumulo(SanzioneSostitutivaCumuloModel aValore) {
		mSanzioneSostitutivaCumulo = aValore;
	}

	public void setStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aValore) {
		mStatoEsecTitoloCumulato = aValore;
	}

	// public void setNotaTrasmissione (NotaDiTrasmissioneModel aValore) { mNotaTrasmissione = aValore; } ;
	public void setNotaTrasmissione(Vector<NotaDiTrasmissioneModel> aValore) {
		mNotaTrasmissione = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "TitoloCumulatoModel:\n" + "[ mIdTitoloCumulato            = " + mIdTitoloCumulato + " ]\n"
				+ "[ mCodTipoProvvedimento        = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDataIrrevocabilita          = " + mDataIrrevocabilita + " ]\n"
				+ "[ mAnnoRegePm                  = " + mAnnoRegePm + " ]\n"
				+ "[ mNumeroRegePm                = " + mNumeroRegePm + " ]\n"
				+ "[ mCodSedeNotiziaReato         = " + mCodSedeNotiziaReato + " ]\n"
				+ "[ mAnnoRegGen                  = " + mAnnoRegGen + " ]\n"
				+ "[ mNumeroRegGen                = " + mNumeroRegGen + " ]\n"
				+ "[ mTipoRegGen                  = " + mTipoRegGen + " ]\n"
				+ "[ mDataProvvedimento           = " + mDataProvvedimento + " ]\n"
				+ "[ mAnnoSentenza                = " + mAnnoSentenza + " ]\n"
				+ "[ mNumeroSentenza              = " + mNumeroSentenza + " ]\n"
				+ "[ mCodTipoAutoritaEmittente    = " + mCodTipoAutoritaEmittente + " ]\n"
				+ "[ mCodLuogoEmittente           = " + mCodLuogoEmittente + " ]\n"
				+ "[ mNumSezioneAutoritaEmittente = " + mNumSezioneAutoritaEmittente + " ]\n"
				+ "[ mCodTipoRito                 = " + mCodTipoRito + " ]\n"
				+ "[ mCodTipoProvvedimentoRif     = " + mCodTipoProvvedimentoRif + " ]\n"
				+ "[ mCodTipoProvvRif             = " + mCodTipoProvvRif + " ]\n"
				+ "[ mDataProvvRif                = " + mDataProvvRif + " ]\n"
				+ "[ mAnnoProvvRif                = " + mAnnoProvvRif + " ]\n"
				+ "[ mNumeroProvvRif              = " + mNumeroProvvRif + " ]\n"
				+ "[ mCodTipoAutoritaProvvRif     = " + mCodTipoAutoritaProvvRif + " ]\n"
				+ "[ mCodLuogoProvvRif            = " + mCodLuogoProvvRif + " ]\n"
				+ "[ mNumSezioneAutoritaProvvRif  = " + mNumSezioneAutoritaProvvRif + " ]\n"
				+ "[ mCodTipoRitoRif              = " + mCodTipoRitoRif + " ]\n"
				+ "[ mCodTipoProvvedimentoAltro   = " + mCodTipoProvvedimentoAltro + " ]\n"
				+ "[ mNote1DecisioneCassazione    = " + mNote1DecisioneCassazione + " ]\n"
				+ "[ mNote2DecisioneCassazione    = " + mNote2DecisioneCassazione + " ]\n"
				+ "[ mAnnoSentenzaCassazione      = " + mAnnoSentenzaCassazione + " ]\n"
				+ "[ mNumeroSentenzaCassazione    = " + mNumeroSentenzaCassazione + " ]\n"
				+ "[ mAnnoRaccoltaGenerale        = " + mAnnoRaccoltaGenerale + " ]\n"
				+ "[ mNumeroRaccoltaGenerale      = " + mNumeroRaccoltaGenerale + " ]\n"
				+ "[ mCodTipoDecisioneCassazione  = " + mCodTipoDecisioneCassazione + " ]\n"
				+ "[ mNote                        = " + mNote + " ]\n" +

				"[ mIstrIdIstruttoriaCumulo     = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mFlagStato                   = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica              = " + mMotivoModifica + " ]\n"
				+ "[ mIdSentenzaOrigine           = " + mIdSentenzaOrigine + " ]\n"
				+ "[ mFlagEscluso                 = " + mFlagEscluso + " ]\n"
				+ "[ mMessIdMessaggio             = " + mMessIdMessaggio + " ]\n"
				+ "[ mTipoIscrizione              = " + mTipoIscrizione + " ]\n"
				+ "[ mDataPresaInCarico           = " + mDataPresaInCarico + " ]\n" +

				"[ mCodOperatoreInserimento     = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento             = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento   = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento           = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento     = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

	/**
	 * 24/04/2019 MEV70. Confronta i dati del TitoloCumulato con quelli della Sentenza in input e restituisce
	 * true se i titoli 'coincidono' n.b. il confronto viene fatto solo se i dati delle due entità sono
	 * 'completi'
	 * 
	 * @param aTitoloModel
	 * @return
	 */
	public boolean isStessoTitolo(SentenzaModel aSentenza) {
		boolean isStessoTitolo = true;

		if (mCodTipoAutoritaEmittente == null || mCodTipoAutoritaEmittente.equals("")
				|| mCodLuogoEmittente == null || mCodLuogoEmittente.equals("") || mDataProvvedimento == null
				|| mAnnoSentenza == null || mNumeroSentenza == null || mNumeroSentenza.equals(""))
			return false;

		if (aSentenza.getCodTipoAutoritaEmittente() == null
				|| aSentenza.getCodTipoAutoritaEmittente().equals("")
				|| aSentenza.getCodLuogoEmittente() == null || aSentenza.getCodLuogoEmittente().equals("")
				|| aSentenza.getDataProvvedimento() == null || aSentenza.getAnnoSentenza() == null
				|| aSentenza.getNumeroSentenza() == null || aSentenza.getNumeroSentenza().equals(""))
			return false;

		if (!mCodTipoAutoritaEmittente.equals(aSentenza.getCodTipoAutoritaEmittente()))
			return false;

		if (!mCodLuogoEmittente.equals(aSentenza.getCodLuogoEmittente()))
			return false;

		// FIXME CUMULO verificare di che date si tratta e quindi se confrontabili
		if (!mDataProvvedimento.equals(aSentenza.getDataProvvedimento()))
			return false;

		if (mAnnoSentenza.compareTo(aSentenza.getAnnoSentenza()) != 0)
			return false;

		if (!mNumeroSentenza.equals(aSentenza.getNumeroSentenza()))
			return false;

		return isStessoTitolo;
	}

}