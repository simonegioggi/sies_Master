package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;

/**
 * <p>
 * Title: BeneficioCumuloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il BeneficioCumulo
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
@SuppressWarnings("rawtypes")
public class BeneficioCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751091863226808751L;

	private BigDecimal mIdBeneficioCumulo;
	private String mCodNaturaBeneficio;
	private String mDescrNaturaBeneficio;
	private String mCodTipoBeneficio;
	private String mDescrTipoBeneficio;
	private String mCodTipoSospSubordinata;
	private String mDescrTipoSospSubordinata;
	private String mCodSottotipoBeneficio;
	private String mDescrSottotipoBeneficio;
	private BigDecimal mNumAnniReclusione;
	private BigDecimal mNumMesiReclusione;
	private BigDecimal mNumGiorniReclusione;
	private BigDecimal mImportoMulta;
	private BigDecimal mNumAnniArresto;
	private BigDecimal mNumMesiArresto;
	private BigDecimal mNumGiorniArresto;
	private BigDecimal mImportoAmmenda;
	private String mCodDpr;
	private String mDescrDpr;
	private String mNote;

	private BigDecimal mNumAnniSospensione;

	private BigDecimal mNumMesiPrestazione;
	private BigDecimal mNumGiorniPrestazione;
	private BigDecimal mNumOreSettimanali;
	private BigDecimal mNumAnniAdempimento;
	private BigDecimal mNumMesiAdempimento;
	private BigDecimal mNumGiorniAdempimento;
	private String mEnteIncaricato;
	private String mFlagFrequenzaSettimanale;

	private BigDecimal mRifIdProvvedimento;
	private String mRifCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private Date mRifDataProvvedimento;
	private Date mRifDataIrrevocabilita;
	private String mRifCodTipoAutoEmittente;
	private String mRifCodLuogoEmittente;
	private String mDescrTipoAutoEmittente;
	private String mDescrLuogoEmittente;
	private String mRifNumSezioneAutoEmittente;
	private BigDecimal mRifAnnoProvvedimento;
	private String mRifNumeroProvvedimento;
	private BigDecimal mTitIdTitoloCumulato;
	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mIdBeneficioOrigine;
	private BigDecimal mBenIdBeneficioOrig;
	private BigDecimal mBenIdBeneficioCumulo;

	private BigDecimal mTitIdTitoloCumulatoCollegato;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mStringaReclusione;
	private String mStringaArresto;

	// solo per benefici Concessi; I dati dell'eventuale Revoca vengono scritti qui
	private String mStringaRevoca;
	private Vector<TipologiaOrarioModel> mListaOrari;
	private Vector mTotBeneficiCumulo; // Benefici concessi/revocati
	
	private boolean mIsRevocato;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public BeneficioCumuloModel() {
		this.mIdBeneficioCumulo = null;
		this.mCodNaturaBeneficio = "";
		this.mDescrNaturaBeneficio = "";
		this.mCodTipoBeneficio = "";
		this.mDescrTipoBeneficio = "";
		this.mCodTipoSospSubordinata = "";
		this.mDescrTipoSospSubordinata = "";
		this.mCodSottotipoBeneficio = "";
		this.mDescrSottotipoBeneficio = "";
		this.mNumAnniReclusione = null;
		this.mNumMesiReclusione = null;
		this.mNumGiorniReclusione = null;
		this.mImportoMulta = null;
		this.mNumAnniArresto = null;
		this.mNumMesiArresto = null;
		this.mNumGiorniArresto = null;
		this.mImportoAmmenda = null;
		this.mCodDpr = "";
		this.mDescrDpr = "";
		this.mNote = "";
		this.mNumAnniSospensione = null;
		this.mNumMesiPrestazione = null;
		this.mNumGiorniPrestazione = null;
		this.mNumOreSettimanali = null;
		this.mNumAnniAdempimento = null;
		this.mNumMesiAdempimento = null;
		this.mNumGiorniAdempimento = null;
		this.mEnteIncaricato = "";
		this.mFlagFrequenzaSettimanale = "";
		this.mRifIdProvvedimento = null;
		this.mRifCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mRifDataProvvedimento = null;
		this.mRifDataIrrevocabilita = null;
		this.mRifCodTipoAutoEmittente = "";
		this.mRifCodLuogoEmittente = "";
		this.mDescrTipoAutoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mRifNumSezioneAutoEmittente = "";
		this.mRifAnnoProvvedimento = null;
		this.mRifNumeroProvvedimento = "";
		this.mTitIdTitoloCumulato = null;
		this.mFlagStato = "";
		this.mMotivoModifica = "";
		this.mIdBeneficioOrigine = null;
		this.mBenIdBeneficioOrig = null;
		this.mBenIdBeneficioCumulo = null;
		this.mTitIdTitoloCumulatoCollegato = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		this.mTotBeneficiCumulo = new Vector();
		
		this.mIsRevocato = false;

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public BeneficioCumuloModel(BeneficioCumuloModel aModel) {
		this.mIdBeneficioCumulo = aModel.mIdBeneficioCumulo;
		this.mCodNaturaBeneficio = aModel.mCodNaturaBeneficio;
		this.mDescrNaturaBeneficio = aModel.mDescrNaturaBeneficio;
		this.mCodTipoBeneficio = aModel.mCodTipoBeneficio;
		this.mDescrTipoBeneficio = aModel.mDescrTipoBeneficio;
		this.mCodTipoSospSubordinata = aModel.mCodTipoSospSubordinata;
		this.mDescrTipoSospSubordinata = aModel.mDescrTipoSospSubordinata;
		this.mCodSottotipoBeneficio = aModel.mCodSottotipoBeneficio;
		this.mDescrSottotipoBeneficio = aModel.mDescrSottotipoBeneficio;
		this.mNumAnniReclusione = aModel.mNumAnniReclusione;
		this.mNumMesiReclusione = aModel.mNumMesiReclusione;
		this.mNumGiorniReclusione = aModel.mNumGiorniReclusione;
		this.mImportoMulta = aModel.mImportoMulta;
		this.mNumAnniArresto = aModel.mNumAnniArresto;
		this.mNumMesiArresto = aModel.mNumMesiArresto;
		this.mNumGiorniArresto = aModel.mNumGiorniArresto;
		this.mImportoAmmenda = aModel.mImportoAmmenda;
		this.mCodDpr = aModel.mCodDpr;
		this.mDescrDpr = aModel.mDescrDpr;
		this.mNote = aModel.mNote;
		this.mNumAnniSospensione = aModel.mNumAnniSospensione;
		this.mNumMesiPrestazione = aModel.mNumMesiPrestazione;
		this.mNumGiorniPrestazione = aModel.mNumGiorniPrestazione;
		this.mNumOreSettimanali = aModel.mNumOreSettimanali;
		this.mNumAnniAdempimento = aModel.mNumAnniAdempimento;
		this.mNumMesiAdempimento = aModel.mNumMesiAdempimento;
		this.mNumGiorniAdempimento = aModel.mNumGiorniAdempimento;
		this.mEnteIncaricato = aModel.mEnteIncaricato;
		this.mFlagFrequenzaSettimanale = aModel.mFlagFrequenzaSettimanale;
		this.mRifIdProvvedimento = aModel.mRifIdProvvedimento;
		this.mRifCodTipoProvvedimento = aModel.mRifCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mRifDataProvvedimento = aModel.mRifDataProvvedimento;
		this.mRifDataIrrevocabilita = aModel.mRifDataIrrevocabilita;
		this.mRifCodTipoAutoEmittente = aModel.mRifCodTipoAutoEmittente;
		this.mRifCodLuogoEmittente = aModel.mRifCodLuogoEmittente;
		this.mDescrTipoAutoEmittente = aModel.mDescrTipoAutoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mRifNumSezioneAutoEmittente = aModel.mRifNumSezioneAutoEmittente;
		this.mRifAnnoProvvedimento = aModel.mRifAnnoProvvedimento;
		this.mRifNumeroProvvedimento = aModel.mRifNumeroProvvedimento;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mIdBeneficioOrigine = aModel.mIdBeneficioOrigine;
		this.mBenIdBeneficioOrig = aModel.mBenIdBeneficioOrig;
		this.mBenIdBeneficioCumulo = aModel.mBenIdBeneficioCumulo;
		this.mTitIdTitoloCumulatoCollegato = aModel.mTitIdTitoloCumulatoCollegato;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		
		this.mIsRevocato = aModel.mIsRevocato;
	}

	/*****************************************************************************
	 * / * USATO IN STAMPA PROSPETTO CUMULO PROPOSTA PER RAGGRUPPARE I BENEFICI
	 * /****************************************************************************** COSTRUTTORE DI COPIA
	 * che istanzia un nuovo model caricandolo con il contenuto del model passato in input
	 * (COMPUTICUMULOMODEL)
	 * 
	 * @param aModel
	 ****************************************************************************/
	public BeneficioCumuloModel(ComputiCumuloModel aModel) {
		this.mNumAnniReclusione = aModel.getNumAnniReclusione();
		this.mNumMesiReclusione = aModel.getNumMesiReclusione();
		this.mNumGiorniReclusione = aModel.getNumGiorniReclusione();
		this.mImportoMulta = aModel.getImportoMulta();
		this.mNumAnniArresto = aModel.getNumAnniArresto();
		this.mNumMesiArresto = aModel.getNumMesiArresto();
		this.mNumGiorniArresto = aModel.getNumGiorniArresto();
		this.mImportoAmmenda = aModel.getImportoAmmenda();
		this.mCodDpr = aModel.getCodDpr();
		this.mDescrDpr = aModel.getDescDpr();
		this.mNote = aModel.getNote();

		this.mStringaArresto = aModel.getStringaArresto();
		this.mStringaReclusione = aModel.getStringaReclusione();

	}

	/*****************************************************************************
	 * / * USATO IN STAMPA PROSPETTO CUMULO PROPOSTA PER RAGGRUPPARE I BENEFICI
	 * /****************************************************************************** COSTRUTTORE DI COPIA
	 * che istanzia un nuovo model caricandolo con il contenuto del model passato in input
	 * (RICHIESTEPMINCUMULOOMODEL)
	 * 
	 * @param aModel
	 ****************************************************************************/
	public BeneficioCumuloModel(RichiestePmInCumuloModel aModel) {
		this.mNumAnniReclusione = aModel.getNumAnniReclusioneR();
		this.mNumMesiReclusione = aModel.getNumMesiReclusioneR();
		this.mNumGiorniReclusione = aModel.getNumGiorniReclusioneR();
		this.mImportoMulta = aModel.getImportoMultaR();
		this.mNumAnniArresto = aModel.getNumAnniArrestoR();
		this.mNumMesiArresto = aModel.getNumMesiArrestoR();
		this.mNumGiorniArresto = aModel.getNumGiorniArrestoR();
		this.mImportoAmmenda = aModel.getImportoAmmendaR();
		this.mCodDpr = aModel.getCodDpr();
		this.mDescrDpr = aModel.getDescrDpr();
		this.mNote = aModel.getNoteReclusione();

		this.mStringaArresto = aModel.getStringaArrestoR();
		this.mStringaReclusione = aModel.getStringaReclusioneR();

	}

	/*****************************************************************************
	 * / * USATO IN STAMPA PROSPETTO CUMULO PROPOSTA PER RAGGRUPPARE I BENEFICI
	 * /****************************************************************************** COSTRUTTORE DI COPIA
	 * che istanzia un nuovo model caricandolo con il contenuto del model passato in input
	 * (RICHIESTEPMINCUMULOOMODEL)
	 * 
	 * @param aModel
	 ****************************************************************************/
	public BeneficioCumuloModel(ProvvedimentoGeSorvCumModel aModel) {
		this.mNumAnniReclusione = aModel.getNumAnniReclusioneD();
		this.mNumMesiReclusione = aModel.getNumMesiReclusioneD();
		this.mNumGiorniReclusione = aModel.getNumGiorniReclusioneD();
		this.mImportoMulta = aModel.getImportoMultaD();
		this.mNumAnniArresto = aModel.getNumAnniArrestoD();
		this.mNumMesiArresto = aModel.getNumMesiArrestoD();
		this.mNumGiorniArresto = aModel.getNumGiorniArrestoD();

	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public BeneficioCumuloModel(BigDecimal aIdBeneficioCumulo, String aCodNaturaBeneficio,
			String aDescrNaturaBeneficio, String aCodTipoBeneficio, String aDescrTipoBeneficio,
			String aCodTipoSospSubordinata, String aDescrTipoSospSubordinata, String aCodSottotipoBeneficio,
			String aDescrSottotipoBeneficio, BigDecimal aNumAnniReclusione, BigDecimal aNumMesiReclusione,
			BigDecimal aNumGiorniReclusione, BigDecimal aImportoMulta, BigDecimal aNumAnniArresto,
			BigDecimal aNumMesiArresto, BigDecimal aNumGiorniArresto, BigDecimal aImportoAmmenda,
			String aCodDpr, String aDescrDpr, String aNote, BigDecimal aNumAnniSospensione,
			BigDecimal aNumMesiPrestazione, BigDecimal aNumGiorniPrestazione, BigDecimal aNumOreSettimanali,
			BigDecimal aNumAnniAdempimento, BigDecimal aNumMesiAdempimento, BigDecimal aNumGiorniAdempimento,
			String aEnteIncaricato, String aFlagFrequenzaSettimanale, BigDecimal aRifIdProvvedimento,
			String aRifCodTipoProvvedimento, String aDescrTipoProvvedimento, Date aRifDataProvvedimento,
			Date aRifDataIrrevocabilita, String aRifCodTipoAutoEmittente, String aRifCodLuogoEmittente,
			String aDescrTipoAutoEmittente, String aDescrLuogoEmittente, String aRifNumSezioneAutoEmittente,
			BigDecimal aRifAnnoProvvedimento, String aRifNumeroProvvedimento, BigDecimal aTitIdTitoloCumulato,
			String aFlagStato, String aMotivoModifica, BigDecimal aIdBeneficioOrigine,
			BigDecimal aBenIdBeneficioOrig, BigDecimal aBenIdBeneficioCumulo,
			BigDecimal aTitIdTitoloCumulatoCollegato, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdBeneficioCumulo = aIdBeneficioCumulo;
		this.mCodNaturaBeneficio = aCodNaturaBeneficio;
		this.mDescrNaturaBeneficio = aDescrNaturaBeneficio;
		this.mCodTipoBeneficio = aCodTipoBeneficio;
		this.mDescrTipoBeneficio = aDescrTipoBeneficio;
		this.mCodTipoSospSubordinata = aCodTipoSospSubordinata;
		this.mDescrTipoSospSubordinata = aDescrTipoSospSubordinata;
		this.mCodSottotipoBeneficio = aCodSottotipoBeneficio;
		this.mDescrSottotipoBeneficio = aDescrSottotipoBeneficio;
		this.mNumAnniReclusione = aNumAnniReclusione;
		this.mNumMesiReclusione = aNumMesiReclusione;
		this.mNumGiorniReclusione = aNumGiorniReclusione;
		this.mImportoMulta = aImportoMulta;
		this.mNumAnniArresto = aNumAnniArresto;
		this.mNumMesiArresto = aNumMesiArresto;
		this.mNumGiorniArresto = aNumGiorniArresto;
		this.mImportoAmmenda = aImportoAmmenda;
		this.mCodDpr = aCodDpr;
		this.mDescrDpr = aDescrDpr;
		this.mNote = aNote;
		this.mNumAnniSospensione = aNumAnniSospensione;
		this.mNumMesiPrestazione = aNumMesiPrestazione;
		this.mNumGiorniPrestazione = aNumGiorniPrestazione;
		this.mNumOreSettimanali = aNumOreSettimanali;
		this.mNumAnniAdempimento = aNumAnniAdempimento;
		this.mNumMesiAdempimento = aNumMesiAdempimento;
		this.mNumGiorniAdempimento = aNumGiorniAdempimento;
		this.mEnteIncaricato = aEnteIncaricato;
		this.mFlagFrequenzaSettimanale = aFlagFrequenzaSettimanale;
		this.mRifIdProvvedimento = aRifIdProvvedimento;
		this.mRifCodTipoProvvedimento = aRifCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mRifDataProvvedimento = aRifDataProvvedimento;
		this.mRifDataIrrevocabilita = aRifDataIrrevocabilita;
		this.mRifCodTipoAutoEmittente = aRifCodTipoAutoEmittente;
		this.mRifCodLuogoEmittente = aRifCodLuogoEmittente;
		this.mDescrTipoAutoEmittente = aDescrTipoAutoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mRifNumSezioneAutoEmittente = aRifNumSezioneAutoEmittente;
		this.mRifAnnoProvvedimento = aRifAnnoProvvedimento;
		this.mRifNumeroProvvedimento = aRifNumeroProvvedimento;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mIdBeneficioOrigine = aIdBeneficioOrigine;
		this.mBenIdBeneficioOrig = aBenIdBeneficioOrig;
		this.mBenIdBeneficioCumulo = aBenIdBeneficioCumulo;
		this.mTitIdTitoloCumulatoCollegato = aTitIdTitoloCumulatoCollegato;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdBeneficioCumulo() {
		return mIdBeneficioCumulo;
	}

	public String getCodNaturaBeneficio() {
		return mCodNaturaBeneficio;
	}

	public String getDescrNaturaBeneficio() {
		return mDescrNaturaBeneficio;
	}

	public String getCodTipoBeneficio() {
		return mCodTipoBeneficio;
	}

	public String getDescrTipoBeneficio() {
		return mDescrTipoBeneficio;
	}

	public String getCodTipoSospSubordinata() {
		return mCodTipoSospSubordinata;
	}

	public String getDescrTipoSospSubordinata() {
		return mDescrTipoSospSubordinata;
	}

	public String getCodSottotipoBeneficio() {
		return mCodSottotipoBeneficio;
	}

	public String getDescrSottotipoBeneficio() {
		return mDescrSottotipoBeneficio;
	}

	public BigDecimal getNumAnniReclusione() {
		return mNumAnniReclusione;
	}

	public BigDecimal getNumMesiReclusione() {
		return mNumMesiReclusione;
	}

	public BigDecimal getNumGiorniReclusione() {
		return mNumGiorniReclusione;
	}

	public BigDecimal getImportoMulta() {
		return mImportoMulta;
	}

	public BigDecimal getNumAnniArresto() {
		return mNumAnniArresto;
	}

	public BigDecimal getNumMesiArresto() {
		return mNumMesiArresto;
	}

	public BigDecimal getNumGiorniArresto() {
		return mNumGiorniArresto;
	}

	public BigDecimal getImportoAmmenda() {
		return mImportoAmmenda;
	}

	public String getCodDpr() {
		return mCodDpr;
	}

	public String getDescrDpr() {
		return mDescrDpr;
	}

	public String getNote() {
		return mNote;
	}

	public BigDecimal getNumAnniSospensione() {
		return mNumAnniSospensione;
	}

	public BigDecimal getNumMesiPrestazione() {
		return mNumMesiPrestazione;
	}

	public BigDecimal getNumGiorniPrestazione() {
		return mNumGiorniPrestazione;
	}

	public BigDecimal getNumOreSettimanali() {
		return mNumOreSettimanali;
	}

	public BigDecimal getNumAnniAdempimento() {
		return mNumAnniAdempimento;
	}

	public BigDecimal getNumMesiAdempimento() {
		return mNumMesiAdempimento;
	}

	public BigDecimal getNumGiorniAdempimento() {
		return mNumGiorniAdempimento;
	}

	public String getEnteIncaricato() {
		return mEnteIncaricato;
	}

	public String getFlagFrequenzaSettimanale() {
		return mFlagFrequenzaSettimanale;
	}

	public BigDecimal getRifIdProvvedimento() {
		return mRifIdProvvedimento;
	}

	public String getRifCodTipoProvvedimento() {
		return mRifCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public Date getRifDataProvvedimento() {
		return mRifDataProvvedimento;
	}

	public Date getRifDataIrrevocabilita() {
		return mRifDataIrrevocabilita;
	}

	public String getRifCodTipoAutoEmittente() {
		return mRifCodTipoAutoEmittente;
	}

	public String getRifCodLuogoEmittente() {
		return mRifCodLuogoEmittente;
	}

	public String getDescrTipoAutoEmittente() {
		return mDescrTipoAutoEmittente;
	}

	public String getDescrLuogoEmittente() {
		return mDescrLuogoEmittente;
	}

	public String getRifNumSezioneAutoEmittente() {
		return mRifNumSezioneAutoEmittente;
	}

	public BigDecimal getRifAnnoProvvedimento() {
		return mRifAnnoProvvedimento;
	}

	public String getRifNumeroProvvedimento() {
		return mRifNumeroProvvedimento;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public BigDecimal getIdBeneficioOrigine() {
		return mIdBeneficioOrigine;
	}

	public BigDecimal getBenIdBeneficioOrig() {
		return mBenIdBeneficioOrig;
	}

	public BigDecimal getBenIdBeneficioCumulo() {
		return mBenIdBeneficioCumulo;
	}

	public BigDecimal getTitIdTitoloCumulatoCollegato() {
		return mTitIdTitoloCumulatoCollegato;
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

	public String getStringaReclusione() {
		return mStringaReclusione;
	}

	public String getStringaArresto() {
		return mStringaArresto;
	}

	public String getStringaRevoca() {
		return mStringaRevoca;
	}

	public Vector<TipologiaOrarioModel> getListaOrari() {
		return mListaOrari;
	}

	public Vector getTotBeneficiCumulo() {
		return mTotBeneficiCumulo;
	}

	public boolean getIsRevocato() {
	  return mIsRevocato;
	}

	/**
	 * Metodo che verifica se il giorno in input è presente nella lista di giorni di prestazione.
	 * 
	 * @param aProgressivoGiorno
	 *            . Progressivo giorno settimana 01= Lunedi... vedi dominio NUM_GIORNO
	 * @return true se presente, false altrimenti
	 */
	public boolean isGiornoTipologia(String aProgressivoGiorno) {
		boolean isPresente = false;

		if (mListaOrari != null) {
			for (int i = 0; i < mListaOrari.size(); i++) {
				TipologiaOrarioModel lTipologia = mListaOrari.elementAt(i);
				if (aProgressivoGiorno.equals(lTipologia.getCodNumGiorno())) {
					isPresente = true;
					break;
				}
			}
		}

		return isPresente;
	}

	public String getDalleOre(String aProgressivoGiorno) {
		String lDalleOre = "";

		if (mListaOrari != null) {
			for (int i = 0; i < mListaOrari.size(); i++) {
				TipologiaOrarioModel lTipologia = mListaOrari.elementAt(i);
				if (aProgressivoGiorno.equals(lTipologia.getCodNumGiorno())) {
					lDalleOre = lTipologia.getDalleOre();
					break;
				}
			}
		}

		return lDalleOre;
	}

	public String getAlleOre(String aProgressivoGiorno) {
		String lAlleOre = "";

		if (mListaOrari != null) {
			for (int i = 0; i < mListaOrari.size(); i++) {
				TipologiaOrarioModel lTipologia = mListaOrari.elementAt(i);
				if (aProgressivoGiorno.equals(lTipologia.getCodNumGiorno())) {
					lAlleOre = lTipologia.getAlleOre();
					break;
				}
			}
		}

		return lAlleOre;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdBeneficioCumulo(BigDecimal aValore) {
		mIdBeneficioCumulo = aValore;
	}

	public void setCodNaturaBeneficio(String aValore) {
		mCodNaturaBeneficio = aValore;
	}

	public void setDescrNaturaBeneficio(String aValore) {
		mDescrNaturaBeneficio = aValore;
	}

	public void setCodTipoBeneficio(String aValore) {
		mCodTipoBeneficio = aValore;
	}

	public void setDescrTipoBeneficio(String aValore) {
		mDescrTipoBeneficio = aValore;
	}

	public void setCodTipoSospSubordinata(String aValore) {
		mCodTipoSospSubordinata = aValore;
	}

	public void setDescrTipoSospSubordinata(String aValore) {
		mDescrTipoSospSubordinata = aValore;
	}

	public void setCodSottotipoBeneficio(String aValore) {
		mCodSottotipoBeneficio = aValore;
	}

	public void setDescrSottotipoBeneficio(String aValore) {
		mDescrSottotipoBeneficio = aValore;
	}

	public void setNumAnniReclusione(BigDecimal aValore) {
		mNumAnniReclusione = aValore;
	}

	public void setNumMesiReclusione(BigDecimal aValore) {
		mNumMesiReclusione = aValore;
	}

	public void setNumGiorniReclusione(BigDecimal aValore) {
		mNumGiorniReclusione = aValore;
	}

	public void setImportoMulta(BigDecimal aValore) {
		mImportoMulta = aValore;
	}

	public void setNumAnniArresto(BigDecimal aValore) {
		mNumAnniArresto = aValore;
	}

	public void setNumMesiArresto(BigDecimal aValore) {
		mNumMesiArresto = aValore;
	}

	public void setNumGiorniArresto(BigDecimal aValore) {
		mNumGiorniArresto = aValore;
	}

	public void setImportoAmmenda(BigDecimal aValore) {
		mImportoAmmenda = aValore;
	}

	public void setCodDpr(String aValore) {
		mCodDpr = aValore;
	}

	public void setDescrDpr(String aValore) {
		mDescrDpr = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setNumAnniSospensione(BigDecimal aValore) {
		mNumAnniSospensione = aValore;
	}

	public void setNumMesiPrestazione(BigDecimal aValore) {
		mNumMesiPrestazione = aValore;
	}

	public void setNumGiorniPrestazione(BigDecimal aValore) {
		mNumGiorniPrestazione = aValore;
	}

	public void setNumOreSettimanali(BigDecimal aValore) {
		mNumOreSettimanali = aValore;
	}

	public void setNumAnniAdempimento(BigDecimal aValore) {
		mNumAnniAdempimento = aValore;
	}

	public void setNumMesiAdempimento(BigDecimal aValore) {
		mNumMesiAdempimento = aValore;
	}

	public void setNumGiorniAdempimento(BigDecimal aValore) {
		mNumGiorniAdempimento = aValore;
	}

	public void setEnteIncaricato(String aValore) {
		mEnteIncaricato = aValore;
	}

	public void setFlagFrequenzaSettimanale(String aValore) {
		mFlagFrequenzaSettimanale = aValore;
	}

	public void setRifIdProvvedimento(BigDecimal aValore) {
		mRifIdProvvedimento = aValore;
	}

	public void setRifCodTipoProvvedimento(String aValore) {
		mRifCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setRifDataProvvedimento(Date aValore) {
		mRifDataProvvedimento = aValore;
	}

	public void setRifDataIrrevocabilita(Date aValore) {
		mRifDataIrrevocabilita = aValore;
	}

	public void setRifCodTipoAutoEmittente(String aValore) {
		mRifCodTipoAutoEmittente = aValore;
	}

	public void setRifCodLuogoEmittente(String aValore) {
		mRifCodLuogoEmittente = aValore;
	}

	public void setDescrTipoAutoEmittente(String aValore) {
		mDescrTipoAutoEmittente = aValore;
	}

	public void setDescrLuogoEmittente(String aValore) {
		mDescrLuogoEmittente = aValore;
	}

	public void setRifNumSezioneAutoEmittente(String aValore) {
		mRifNumSezioneAutoEmittente = aValore;
	}

	public void setRifAnnoProvvedimento(BigDecimal aValore) {
		mRifAnnoProvvedimento = aValore;
	}

	public void setRifNumeroProvvedimento(String aValore) {
		mRifNumeroProvvedimento = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setIdBeneficioOrigine(BigDecimal aValore) {
		mIdBeneficioOrigine = aValore;
	}

	public void setBenIdBeneficioOrig(BigDecimal aValore) {
		mBenIdBeneficioOrig = aValore;
	}

	public void setBenIdBeneficioCumulo(BigDecimal aValore) {
		mBenIdBeneficioCumulo = aValore;
	}

	public void setTitIdTitoloCumulatoCollegato(BigDecimal aValore) {
		mTitIdTitoloCumulatoCollegato = aValore;
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

	public void setStringaArresto(String aValore) {
		mStringaArresto = aValore;
	}

	public void setStringaReclusione(String aValore) {
		mStringaReclusione = aValore;
	}

	public void setStringaRevoca(String aValore) {
		mStringaRevoca = aValore;
	}

	public void setListaOrari(Vector<TipologiaOrarioModel> aValore) {
		mListaOrari = aValore;
	}

	public void setTotBeneficiCumulo(Vector aValore) {
		mTotBeneficiCumulo = aValore;
	}

	 public void setIsRevocato (boolean aValore) {
     mIsRevocato = aValore;
  }
	
	public CalendarModel getQuantumReclusione() {
		CalendarModel lCalReclusione = new CalendarModel();

		lCalReclusione.setNumGiorni(this.mNumGiorniReclusione);
		lCalReclusione.setNumMesi(this.mNumMesiReclusione);
		lCalReclusione.setNumAnni(this.mNumAnniReclusione);

		if (mImportoMulta != null)
			lCalReclusione.setImportoMulta(this.mImportoMulta.doubleValue());

		return lCalReclusione;
	}

	/**
	* 
	*/
	public CalendarModel getQuantumArresto() {
		CalendarModel lCalArresto = new CalendarModel();

		lCalArresto.setNumGiorni(this.mNumGiorniArresto);
		lCalArresto.setNumMesi(this.mNumMesiArresto);
		lCalArresto.setNumAnni(this.mNumAnniArresto);

		if (mImportoAmmenda != null)
			lCalArresto.setImportoMulta(this.mImportoAmmenda.doubleValue());

		return lCalArresto;
	}

	public void setQuantumReclusione(CalendarModel aCalendar) {
		if (aCalendar.getNumGiorni() > 0)
			this.mNumGiorniReclusione = new BigDecimal(aCalendar.getNumGiorni());
		else
			this.mNumGiorniReclusione = null;

		if (aCalendar.getNumMesi() > 0)
			this.mNumMesiReclusione = new BigDecimal(aCalendar.getNumMesi());
		else
			this.mNumMesiReclusione = null;

		if (aCalendar.getNumAnni() > 0)
			this.mNumAnniReclusione = new BigDecimal(aCalendar.getNumAnni());
		else
			this.mNumAnniReclusione = null;

		if (aCalendar.getImportoMulta() > 0)
			this.mImportoMulta = new BigDecimal(aCalendar.getImportoMulta());
		else
			this.mImportoMulta = null;
	}

	/**
	* 
	*/
	public void setQuantumArresto(CalendarModel aCalendar) {
		if (aCalendar.getNumGiorni() > 0)
			this.mNumGiorniArresto = new BigDecimal(aCalendar.getNumGiorni());
		else
			this.mNumGiorniArresto = null;

		if (aCalendar.getNumMesi() > 0)
			this.mNumMesiArresto = new BigDecimal(aCalendar.getNumMesi());
		else
			this.mNumMesiArresto = null;

		if (aCalendar.getNumAnni() > 0)
			this.mNumAnniArresto = new BigDecimal(aCalendar.getNumAnni());
		else
			this.mNumAnniArresto = null;

		if (aCalendar.getImportoAmmenda() > 0)
			this.mImportoAmmenda = new BigDecimal(aCalendar.getImportoAmmenda());
		else
			this.mImportoAmmenda = null;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "BeneficioCumuloModel:\n" + "[ mIdBeneficioCumulo          = " + mIdBeneficioCumulo + " ]\n"
				+ "[ mCodNaturaBeneficio         = " + mCodNaturaBeneficio + " ]\n"
				+ "[ mCodTipoBeneficio           = " + mCodTipoBeneficio + " ]\n"
				+ "[ mCodTipoSospSubordinata     = " + mCodTipoSospSubordinata + " ]\n"
				+ "[ mCodSottotipoBeneficio      = " + mCodSottotipoBeneficio + " ]\n"
				+ "[ mNumAnniReclusione          = " + mNumAnniReclusione + " ]\n"
				+ "[ mNumMesiReclusione          = " + mNumMesiReclusione + " ]\n"
				+ "[ mNumGiorniReclusione        = " + mNumGiorniReclusione + " ]\n"
				+ "[ mImportoMulta               = " + mImportoMulta + " ]\n"
				+ "[ mNumAnniArresto             = " + mNumAnniArresto + " ]\n"
				+ "[ mNumMesiArresto             = " + mNumMesiArresto + " ]\n"
				+ "[ mNumGiorniArresto           = " + mNumGiorniArresto + " ]\n"
				+ "[ mImportoAmmenda             = " + mImportoAmmenda + " ]\n"
				+ "[ mCodDpr                     = " + mCodDpr + " ]\n" + "[ mNote                       = "
				+ mNote + " ]\n" + "[ mNumAnniSospensione         = " + mNumAnniSospensione + " ]\n"
				+ "[ mNumMesiPrestazione         = " + mNumMesiPrestazione + " ]\n"
				+ "[ mNumGiorniPrestazione       = " + mNumGiorniPrestazione + " ]\n"
				+ "[ mNumOreSettimanali          = " + mNumOreSettimanali + " ]\n"
				+ "[ mNumAnniAdempimento         = " + mNumAnniAdempimento + " ]\n"
				+ "[ mNumMesiAdempimento         = " + mNumMesiAdempimento + " ]\n"
				+ "[ mNumGiorniAdempimento       = " + mNumGiorniAdempimento + " ]\n"
				+ "[ mEnteIncaricato             = " + mEnteIncaricato + " ]\n"
				+ "[ mFlagFrequenzaSettimanale   = " + mFlagFrequenzaSettimanale + " ]\n"
				+ "[ mRifIdProvvedimento         = " + mRifIdProvvedimento + " ]\n"
				+ "[ mRifCodTipoProvvedimento    = " + mRifCodTipoProvvedimento + " ]\n"
				+ "[ mDescrTipoProvvedimento     = " + mDescrTipoProvvedimento + " ]\n"
				+ "[ mRifDataProvvedimento       = " + mRifDataProvvedimento + " ]\n"
				+ "[ mRifDataIrrevocabilita      = " + mRifDataIrrevocabilita + " ]\n"
				+ "[ mRifCodTipoAutoEmittente    = " + mRifCodTipoAutoEmittente + " ]\n"
				+ "[ mRifCodLuogoEmittente       = " + mRifCodLuogoEmittente + " ]\n"
				+ "[ mDescrTipoAutoEmittente     = " + mDescrTipoAutoEmittente + " ]\n"
				+ "[ mDescrLuogoEmittente        = " + mDescrLuogoEmittente + " ]\n"
				+ "[ mRifNumSezioneAutoEmittente = " + mRifNumSezioneAutoEmittente + " ]\n"
				+ "[ mRifAnnoProvvedimento       = " + mRifAnnoProvvedimento + " ]\n"
				+ "[ mRifNumeroProvvedimento     = " + mRifNumeroProvvedimento + " ]\n"
				+ "[ mTitIdTitoloCumulato        = " + mTitIdTitoloCumulato + " ]\n"
				+ "[ mFlagStato                  = " + mFlagStato + " ]\n"
				+ "[ mMotivoModifica             = " + mMotivoModifica + " ]\n"
				+ "[ mIdBeneficioOrigine         = " + mIdBeneficioOrigine + " ]\n"
				+ "[ mBenIdBeneficioOrig         = " + mBenIdBeneficioOrig + " ]\n"
				+ "[ mBenIdBeneficioCumulo       = " + mBenIdBeneficioCumulo + " ]\n"
				+ "[ mTitIdTitoloCumulatoCollegato  = " + mTitIdTitoloCumulatoCollegato + " ]\n"
				+ "[ mCodOperatoreInserimento    = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento            = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento      = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento  = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento          = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento    = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

	/**
	 * calcolaStringaArresto e calcolaStringaReclusione: metodi richiamati dalle stampe per avere direttamete
	 * la stringa con i valori
	 * 
	 * @return
	 */
	public void calcolaStringaArrestoCumulo() {
		String lStringArresto = "";
		if (this.getNumAnniArresto() != null) {
			if (this.getNumAnniArresto().intValue() != 0)
				lStringArresto = "Anni " + this.getNumAnniArresto();
		}

		if (this.getNumMesiArresto() != null) {
			if (this.getNumMesiArresto().intValue() != 0)
				lStringArresto += " Mesi " + this.getNumMesiArresto();
		}

		if (this.getNumGiorniArresto() != null) {
			if (this.getNumGiorniArresto().intValue() != 0)
				lStringArresto += " Giorni " + this.getNumGiorniArresto();
		}

		if (lStringArresto.length() > 1)
			this.mStringaArresto = lStringArresto.trim();
		else
			this.mStringaArresto = null;
	}

	public void calcolaStringaReclusioneCumulo() {
		String lStringReclusione = "";
		if (this.getNumAnniReclusione() != null) {
			if (this.getNumAnniReclusione().intValue() != 0)
				lStringReclusione = "Anni " + this.getNumAnniReclusione();
		}

		if (this.getNumMesiReclusione() != null) {
			if (this.getNumMesiReclusione().intValue() != 0)
				lStringReclusione += " Mesi " + this.getNumMesiReclusione();
		}

		if (this.getNumGiorniReclusione() != null) {
			if (this.getNumGiorniReclusione().intValue() != 0)
				lStringReclusione += " Giorni " + this.getNumGiorniReclusione();
		}

		if (lStringReclusione.length() > 1)
			this.mStringaReclusione = lStringReclusione.trim();
		else
			this.mStringaReclusione = null;
	}

	/**
	 * i 2 metodi seguenti Restituiscono la somma di (Arresti e Ammenda , Reclusione e multa) di tutti i
	 * benefici_Cumulo di tutti i Titoli di una istruttoria_Cumulo Sono chiamati nella stampa prospetto Titoli
	 * in istruttoria
	 */
	public CalendarModel getBeneficiReclusioneCumulo(String aFlagConcessiRevocati) {
		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mTotBeneficiCumulo.size(); i++) {
			BeneficioCumuloModel lBenMod = (BeneficioCumuloModel) mTotBeneficiCumulo.elementAt(i);

			if (aFlagConcessiRevocati != null && !aFlagConcessiRevocati.equals("")
					&& lBenMod.getCodNaturaBeneficio().equals(aFlagConcessiRevocati)) {
				CalendarModel lCalMod = new CalendarModel();

				if (lBenMod.getNumAnniReclusione() != null)
					lCalMod.setNumAnni(lBenMod.getNumAnniReclusione());
				else
					lCalMod.setNumAnni(new BigDecimal(0));

				if (lBenMod.getNumMesiReclusione() != null)
					lCalMod.setNumMesi(lBenMod.getNumMesiReclusione());
				else
					lCalMod.setNumMesi(new BigDecimal(0));

				if (lBenMod.getNumGiorniReclusione() != null)
					lCalMod.setNumGiorni(lBenMod.getNumGiorniReclusione());
				else
					lCalMod.setNumGiorni(new BigDecimal(0));

				if (lBenMod.getImportoMulta() != null)
					lCalMod.setImportoMulta(lBenMod.getImportoMulta().doubleValue());
				else
					lCalMod.setImportoMulta(0);

				lBenConc = lCalUtil.sommaGiornieValute(lBenConc, lCalMod);
			}
		}

		return lBenConc;
	}

	public CalendarModel getBeneficiArrestiCumulo(String aFlagConcessiRevocati) {
		CalendarModel lBenConc = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		for (int i = 0; i < mTotBeneficiCumulo.size(); i++) {
			BeneficioCumuloModel lBenMod = (BeneficioCumuloModel) mTotBeneficiCumulo.elementAt(i);

			if (aFlagConcessiRevocati != null && !aFlagConcessiRevocati.equals("")
					&& lBenMod.getCodNaturaBeneficio().equals(aFlagConcessiRevocati)) {
				CalendarModel lCalMod = new CalendarModel();

				if (lBenMod.getNumAnniArresto() != null)
					lCalMod.setNumAnni(lBenMod.getNumAnniArresto());
				else
					lCalMod.setNumAnni(new BigDecimal(0));

				if (lBenMod.getNumMesiArresto() != null)
					lCalMod.setNumMesi(lBenMod.getNumMesiArresto());
				else
					lCalMod.setNumMesi(new BigDecimal(0));

				if (lBenMod.getNumGiorniArresto() != null)
					lCalMod.setNumGiorni(lBenMod.getNumGiorniArresto());
				else
					lCalMod.setNumGiorni(new BigDecimal(0));

				if (lBenMod.getImportoAmmenda() != null)
					lCalMod.setImportoAmmenda(lBenMod.getImportoAmmenda().doubleValue());
				else
					lCalMod.setImportoAmmenda(0);

				lBenConc = lCalUtil.sommaGiornieValute(lBenConc, lCalMod);
			}
		}

		return lBenConc;
	}

	public boolean isQuantumReclusioneZero() {
		return ((mNumAnniReclusione == null || mNumAnniReclusione.intValue() == 0)
				&& (mNumMesiReclusione == null || mNumMesiReclusione.intValue() == 0)
				&& (mNumGiorniReclusione == null || mNumGiorniReclusione.intValue() == 0));
	}

	public boolean isQuantumArrestoZero() {
		return ((mNumAnniArresto == null || mNumAnniArresto.intValue() == 0)
				&& (mNumMesiArresto == null || mNumMesiArresto.intValue() == 0)
				&& (mNumGiorniArresto == null || mNumGiorniArresto.intValue() == 0));
	}
	
  /**
   * Confronta i dati del benerifico continuazione con quelli del Titolo in input e restituisce
   * true se i titoli 'coincidono'
   * n.b. il confronto viene fatto solo se i dati delle due entità sono 'completi'
   * @param aTitoloModel
   * @return
   */
  public boolean isStessoTitolo ( TitoloCumulatoModel aTitoloModel )
  {
    boolean isStessoTitolo = true;

    if (   this.mRifCodTipoAutoEmittente==null  || this.mRifCodTipoAutoEmittente.equals("")
        || this.mRifCodLuogoEmittente==null || this.mRifCodLuogoEmittente.equals("")
        || this.mRifDataProvvedimento==null 
        || this.mRifAnnoProvvedimento==null
        || this.mRifNumeroProvvedimento==null || this.mRifNumeroProvvedimento.equals("")
       )
    return false;
      
    
    if (   aTitoloModel.getCodTipoAutoritaEmittente()==null  || aTitoloModel.getCodTipoAutoritaEmittente().equals("")
        || aTitoloModel.getCodLuogoEmittente()==null || aTitoloModel.getCodLuogoEmittente().equals("")
        || aTitoloModel.getDataProvvedimento()==null 
        || aTitoloModel.getAnnoSentenza()==null
        || aTitoloModel.getNumeroSentenza()==null || aTitoloModel.getNumeroSentenza().equals("")
       )
    return false;
      
    if (!this.mRifCodTipoAutoEmittente.equals(aTitoloModel.getCodTipoAutoritaEmittente()))
      return false;
      
    if (!this.mRifCodLuogoEmittente.equals(aTitoloModel.getCodLuogoEmittente()))
      return false;

    //FIXME CUMULO verificare di che date si tratta e quindi se confrontabili
//    if (!this.mDataSentenza.equals(aTitoloModel.getDataProvvedimento()))
//      return false;
    
    if (this.mRifAnnoProvvedimento.compareTo(aTitoloModel.getAnnoSentenza())!=0)
      return false;
    
    if (!this.mRifNumeroProvvedimento.equals(aTitoloModel.getNumeroSentenza()))
      return false;
    
    return isStessoTitolo;
  }  

}