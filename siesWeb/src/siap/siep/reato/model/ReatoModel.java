package siap.siep.reato.model;

/**
 * <p>Title: ReatoModel</p>
 * <p>Description: Classe Model che rappresenta il Reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class ReatoModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4391814362718113096L;
	private BigDecimal mIdReato;
	private String mCodTipoReato;
	private String mDescrTipoReato;
	private Date mDataReato;
	private String mProgrNumeroManuale;
	private BigDecimal mProgrReato;
	private BigDecimal mProgrCircostanza;
	private Date mDataInizio;
	private BigDecimal mAnnoInizio;
	private BigDecimal mMeseInizio;
	private BigDecimal mGiornoInizio;
	private Date mDataFine;
	private BigDecimal mAnnoFine;
	private BigDecimal mMeseFine;
	private BigDecimal mGiornoFine;
	private String mCodPeriodoConsumazione;
	private String mDescrPeriodoConsumazione;
	private String mDescLuogo;
	private String mCodFonte;
	private String mDescrFonte;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mLettera;
	private String mNumero;
	private String mArticolo;
	private String mNote;
	private String mCodTipoPenaDetentiva;
	private String mDescrTipoPenaDetentiva;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzionePecuniaria;
	private String mFlagErgastolo;
	private Date mDataInizioIsolamentoDiurno;
	private Date mDataFineIsolamentoDiurno;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;
	private BigDecimal mIdContinuazioneReato;
	private String mTipoContinuazioneReato;

	private boolean mFlagReato;
	private String mFlagVisto;

	private String mStringaConsumazione;
	private String mStringaPenaReato;
	private String mStringaPenaPecuniaria;

	// per interoperabilita tra SIES e NSC (WS)
	private BigDecimal mKeyReatoNsc;

	// ***************************************
	// Federica - a9-rr-078
	// aggiunto campo Comma-Qualificante
	private String mCommaQualificante;
	private String mDescrCommaQualificante;
	// ***************************************
	private String mNazionalita;
	private String mCodStatoFascicolo;

	// COSTRUTTORE DI DEFAULT
	public ReatoModel() {
		this.mIdReato = null;
		this.mCodTipoReato = "";
		this.mDescrTipoReato = "";
		this.mDataReato = null;
		this.mProgrNumeroManuale = "";
		this.mProgrReato = null;
		this.mProgrCircostanza = null;
		this.mDataInizio = null;
		this.mAnnoInizio = null;
		this.mMeseInizio = null;
		this.mGiornoInizio = null;
		this.mDataFine = null;
		this.mAnnoFine = null;
		this.mMeseFine = null;
		this.mGiornoFine = null;
		this.mCodPeriodoConsumazione = "";
		this.mDescrPeriodoConsumazione = "";
		this.mDescLuogo = "";
		this.mCodFonte = "";
		this.mDescrFonte = "";
		this.mAnnoFonte = null;
		this.mNumeroFonte = "";
		this.mCodSottonumerazione = "";
		this.mDescrSottonumerazione = "";
		this.mComma = "";
		this.mLettera = "";
		this.mNumero = "";
		this.mArticolo = "";
		this.mNote = "";
		this.mCodTipoPenaDetentiva = "";
		this.mDescrTipoPenaDetentiva = "";
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzionePecuniaria = null;
		this.mFlagErgastolo = "";
		this.mDataInizioIsolamentoDiurno = null;
		this.mDataFineIsolamentoDiurno = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mFasSieIdFascicoloSiep = null;
		this.mCodTipoSanzione = "";
		this.mDescrTipoSanzione = "";
		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;
		this.mIdContinuazioneReato = null;
		this.mTipoContinuazioneReato = "";
		this.mStringaPenaReato = "";
		this.mStringaPenaPecuniaria = "";
		this.mKeyReatoNsc = null;
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = "";
		this.mDescrCommaQualificante = "";
		// ***************************************

	}

	// COSTRUTTORE DI COPIA
	public ReatoModel(ReatoModel aModel) {
		this.mIdReato = aModel.mIdReato;
		this.mCodTipoReato = aModel.mCodTipoReato;
		this.mDescrTipoReato = aModel.mDescrTipoReato;
		this.mDataReato = aModel.mDataReato;
		this.mProgrNumeroManuale = aModel.mProgrNumeroManuale;
		this.mProgrReato = aModel.mProgrReato;
		this.mProgrCircostanza = aModel.mProgrCircostanza;
		this.mDataInizio = aModel.mDataInizio;
		this.mAnnoInizio = aModel.mAnnoInizio;
		this.mMeseInizio = aModel.mMeseInizio;
		this.mGiornoInizio = aModel.mGiornoInizio;
		this.mDataFine = aModel.mDataFine;
		this.mAnnoFine = aModel.mAnnoFine;
		this.mMeseFine = aModel.mMeseFine;
		this.mGiornoFine = aModel.mGiornoFine;
		this.mCodPeriodoConsumazione = aModel.mCodPeriodoConsumazione;
		this.mDescrPeriodoConsumazione = aModel.mDescrPeriodoConsumazione;
		this.mDescLuogo = aModel.mDescLuogo;
		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mArticolo = aModel.mArticolo;
		this.mNote = aModel.mNote;
		this.mCodTipoPenaDetentiva = aModel.mCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aModel.mDescrTipoPenaDetentiva;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mSanzionePecuniaria = aModel.mSanzionePecuniaria;
		this.mFlagErgastolo = aModel.mFlagErgastolo;
		this.mDataInizioIsolamentoDiurno = aModel.mDataInizioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aModel.mDataFineIsolamentoDiurno;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;
		this.mIdContinuazioneReato = aModel.mIdContinuazioneReato;
		this.mTipoContinuazioneReato = aModel.mTipoContinuazioneReato;
		this.mStringaPenaReato = aModel.mStringaPenaReato;
		this.mStringaPenaPecuniaria = aModel.mStringaPenaPecuniaria;
		this.mKeyReatoNsc = aModel.mKeyReatoNsc;
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = aModel.mCommaQualificante;
		this.mDescrCommaQualificante = aModel.mDescrCommaQualificante;
		// ***************************************
	}

	// COSTRUTTORE MODEL 1
	public ReatoModel(BigDecimal aIdReato, String aCodTipoReato, String aDescrTipoReato, Date aDataReato,
			String aProgrNumeroManuale, BigDecimal aProgrReato, BigDecimal aProgrCircostanza,
			Date aDataInizio, BigDecimal aAnnoInizio, BigDecimal aMeseInizio, BigDecimal aGiornoInizio,
			Date aDataFine, BigDecimal aAnnoFine, BigDecimal aMeseFine, BigDecimal aGiornoFine,
			String aCodPeriodoConsumazione, String aDescrPeriodoConsumazione, String aDescLuogo,
			String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte, String aNumeroFonte,
			String aCodSottonumerazione, String aDescrSottonumerazione, String aComma, String aLettera,
			String aNumero, String aArticolo, String aNote, String aCodTipoPenaDetentiva,
			String aDescrTipoPenaDetentiva, BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			BigDecimal aSanzionePecuniaria, String aFlagErgastolo, Date aDataInizioIsolamentoDiurno,
			Date aDataFineIsolamentoDiurno, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, String aCodTipoSanzione, String aDescrTipoSanzione,
			BigDecimal aNumAnniIsolamentoDiurno, BigDecimal aNumMesiIsolamentoDiurno,
			BigDecimal aNumGiorniIsolamentoDiurno, BigDecimal aKeyReatoNsc,
			// ***************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			String aCommaQualificante, String aDescrCommaQualificante
	// ***************************************
	) {
		this.mIdReato = aIdReato;
		this.mCodTipoReato = aCodTipoReato;
		this.mDescrTipoReato = aDescrTipoReato;
		this.mDataReato = aDataReato;
		this.mProgrNumeroManuale = aProgrNumeroManuale;
		this.mProgrReato = aProgrReato;
		this.mProgrCircostanza = aProgrCircostanza;
		this.mDataInizio = aDataInizio;
		this.mAnnoInizio = aAnnoInizio;
		this.mMeseInizio = aMeseInizio;
		this.mGiornoInizio = aGiornoInizio;
		this.mDataFine = aDataFine;
		this.mAnnoFine = aAnnoFine;
		this.mMeseFine = aMeseFine;
		this.mGiornoFine = aGiornoFine;
		this.mCodPeriodoConsumazione = aCodPeriodoConsumazione;
		this.mDescrPeriodoConsumazione = aDescrPeriodoConsumazione;
		this.mDescLuogo = aDescLuogo;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;
		this.mNote = aNote;
		this.mCodTipoPenaDetentiva = aCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aDescrTipoPenaDetentiva;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mSanzionePecuniaria = aSanzionePecuniaria;
		this.mFlagErgastolo = aFlagErgastolo;
		this.mDataInizioIsolamentoDiurno = aDataInizioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aDataFineIsolamentoDiurno;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;
		this.mKeyReatoNsc = aKeyReatoNsc;
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = aCommaQualificante;
		this.mDescrCommaQualificante = aDescrCommaQualificante;
		// ***************************************

	}

	// COSTRUTTORE MODEL 2
	public ReatoModel(BigDecimal aIdReato, String aCodTipoReato, String aDescrTipoReato, Date aDataReato,
			String aProgrNumeroManuale, BigDecimal aProgrReato, BigDecimal aProgrCircostanza,
			Date aDataInizio, BigDecimal aAnnoInizio, BigDecimal aMeseInizio, BigDecimal aGiornoInizio,
			Date aDataFine, BigDecimal aAnnoFine, BigDecimal aMeseFine, BigDecimal aGiornoFine,
			String aCodPeriodoConsumazione, String aDescrPeriodoConsumazione, String aDescLuogo,
			String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte, String aNumeroFonte,
			String aCodSottonumerazione, String aDescrSottonumerazione, String aComma, String aLettera,
			String aNumero, String aArticolo, String aNote, String aCodTipoPenaDetentiva,
			String aDescrTipoPenaDetentiva, BigDecimal aNumAnni, BigDecimal aNumMesi, BigDecimal aNumGiorni,
			BigDecimal aSanzionePecuniaria, String aFlagErgastolo, Date aDataInizioIsolamentoDiurno,
			Date aDataFineIsolamentoDiurno, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aFasSieIdFascicoloSiep, String aCodTipoSanzione, String aDescrTipoSanzione,
			BigDecimal aNumAnniIsolamentoDiurno, BigDecimal aNumMesiIsolamentoDiurno,
			BigDecimal aNumGiorniIsolamentoDiurno, BigDecimal aIdContinuazioneReato,
			String aTipoContinuazioneReato, BigDecimal aKeyReatoNsc,
			// ***************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			String aCommaQualificante, String aDescrCommaQualificante
	// ***************************************
	) {
		this.mIdReato = aIdReato;
		this.mCodTipoReato = aCodTipoReato;
		this.mDescrTipoReato = aDescrTipoReato;
		this.mDataReato = aDataReato;
		this.mProgrNumeroManuale = aProgrNumeroManuale;
		this.mProgrReato = aProgrReato;
		this.mProgrCircostanza = aProgrCircostanza;
		this.mDataInizio = aDataInizio;
		this.mAnnoInizio = aAnnoInizio;
		this.mMeseInizio = aMeseInizio;
		this.mGiornoInizio = aGiornoInizio;
		this.mDataFine = aDataFine;
		this.mAnnoFine = aAnnoFine;
		this.mMeseFine = aMeseFine;
		this.mGiornoFine = aGiornoFine;
		this.mCodPeriodoConsumazione = aCodPeriodoConsumazione;
		this.mDescrPeriodoConsumazione = aDescrPeriodoConsumazione;
		this.mDescLuogo = aDescLuogo;
		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;
		this.mNote = aNote;
		this.mCodTipoPenaDetentiva = aCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aDescrTipoPenaDetentiva;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mSanzionePecuniaria = aSanzionePecuniaria;
		this.mFlagErgastolo = aFlagErgastolo;
		this.mDataInizioIsolamentoDiurno = aDataInizioIsolamentoDiurno;
		this.mDataFineIsolamentoDiurno = aDataFineIsolamentoDiurno;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;
		this.mIdContinuazioneReato = aIdContinuazioneReato;
		this.mTipoContinuazioneReato = aTipoContinuazioneReato;
		this.mKeyReatoNsc = aKeyReatoNsc;
		// ***************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		this.mCommaQualificante = aCommaQualificante;
		this.mDescrCommaQualificante = aDescrCommaQualificante;
		// ***************************************

	}

	//
	// METODI GET()
	//
	public BigDecimal getIdReato() {
		return mIdReato;
	}

	public String getCodTipoReato() {
		return mCodTipoReato;
	}

	public String getDescrTipoReato() {
		return mDescrTipoReato;
	}

	public Date getDataReato() {
		return mDataReato;
	}

	public String getProgrNumeroManuale() {
		return mProgrNumeroManuale;
	}

	public BigDecimal getProgrReato() {
		return mProgrReato;
	}

	public BigDecimal getProgrCircostanza() {
		return mProgrCircostanza;
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public BigDecimal getAnnoInizio() {
		return mAnnoInizio;
	}

	public BigDecimal getMeseInizio() {
		return mMeseInizio;
	}

	public BigDecimal getGiornoInizio() {
		return mGiornoInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public BigDecimal getAnnoFine() {
		return mAnnoFine;
	}

	public BigDecimal getMeseFine() {
		return mMeseFine;
	}

	public BigDecimal getGiornoFine() {
		return mGiornoFine;
	}

	public String getCodPeriodoConsumazione() {
		return mCodPeriodoConsumazione;
	}

	public String getDescrPeriodoConsumazione() {
		return mDescrPeriodoConsumazione;
	}

	public String getDescLuogo() {
		return mDescLuogo;
	}

	public String getCodFonte() {
		return mCodFonte;
	}

	public String getDescrFonte() {
		return mDescrFonte;
	}

	public BigDecimal getAnnoFonte() {
		return mAnnoFonte;
	}

	public String getNumeroFonte() {
		return mNumeroFonte;
	}

	public String getCodSottonumerazione() {
		return mCodSottonumerazione;
	}

	public String getDescrSottonumerazione() {
		return mDescrSottonumerazione;
	}

	public String getComma() {
		return mComma;
	}

	public String getLettera() {
		return mLettera;
	}

	public String getNumero() {
		return mNumero;
	}

	public String getArticolo() {
		return mArticolo;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodTipoPenaDetentiva() {
		return mCodTipoPenaDetentiva;
	}

	public String getDescrTipoPenaDetentiva() {
		return mDescrTipoPenaDetentiva;
	}

	public BigDecimal getNumAnni() {
		return mNumAnni;
	}

	public BigDecimal getNumMesi() {
		return mNumMesi;
	}

	public BigDecimal getNumGiorni() {
		return mNumGiorni;
	}

	public BigDecimal getSanzionePecuniaria() {
		return mSanzionePecuniaria;
	}

	public String getFlagErgastolo() {
		return mFlagErgastolo;
	}

	public Date getDataInizioIsolamentoDiurno() {
		return mDataInizioIsolamentoDiurno;
	}

	public Date getDataFineIsolamentoDiurno() {
		return mDataFineIsolamentoDiurno;
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

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
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

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
	}

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public String getFlagVisto() {
		return mFlagVisto;
	}

	public BigDecimal getNumAnniIsolamentoDiurno() {
		return mNumAnniIsolamentoDiurno;
	}

	public BigDecimal getNumMesiIsolamentoDiurno() {
		return mNumMesiIsolamentoDiurno;
	}

	public BigDecimal getNumGiorniIsolamentoDiurno() {
		return mNumGiorniIsolamentoDiurno;
	}

	public String getStringaConsumazione() {
		return mStringaConsumazione;
	}

	public BigDecimal getIdContinuazioneReato() {
		return mIdContinuazioneReato;
	}

	public String getTipoContinuazioneReato() {
		return mTipoContinuazioneReato;
	}

	public BigDecimal getKeyReatoNsc() {
		return mKeyReatoNsc;
	}

	// ***************************************
	// Federica - a9-rr-078
	// aggiunto campo Comma-Qualificante
	public String getCommaQualificante() {
		return mCommaQualificante;
	}

	public String getDescrCommaQualificante() {
		return mDescrCommaQualificante;
	}
	// ***************************************

	public boolean isReato() {
		return (mProgrCircostanza.intValue() == 1);
	}

	public boolean isPenaReatoInserita() {
		return ((mCodTipoPenaDetentiva != null && !mCodTipoPenaDetentiva.equals("-")
				&& !mCodTipoPenaDetentiva.equals(""))
				|| (mNumAnni != null || mNumMesi != null || mNumGiorni != null)
				|| (mSanzionePecuniaria != null && mSanzionePecuniaria.intValue() != 0)
				|| (mDataInizioIsolamentoDiurno != null) || (mDataFineIsolamentoDiurno != null)
				|| (mCodTipoSanzione != null && !mCodTipoSanzione.equals("-")
						&& !mCodTipoSanzione.equals("")));
	}

	//
	// METODI SET()
	//
	public void setIdReato(BigDecimal aValore) {
		mIdReato = aValore;
	}

	public void setCodTipoReato(String aValore) {
		mCodTipoReato = aValore;
	}

	public void setDescrTipoReato(String aValore) {
		mDescrTipoReato = aValore;
	}

	public void setDataReato(Date aValore) {
		mDataReato = aValore;
	}

	public void setProgrNumeroManuale(String aValore) {
		mProgrNumeroManuale = aValore;
	}

	public void setProgrReato(BigDecimal aValore) {
		mProgrReato = aValore;
	}

	public void setProgrCircostanza(BigDecimal aValore) {
		mProgrCircostanza = aValore;
	}

	public void setDataInizio(Date aValore) {
		mDataInizio = aValore;
	}

	public void setAnnoInizio(BigDecimal aValore) {
		mAnnoInizio = aValore;
	}

	public void setMeseInizio(BigDecimal aValore) {
		mMeseInizio = aValore;
	}

	public void setGiornoInizio(BigDecimal aValore) {
		mGiornoInizio = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setAnnoFine(BigDecimal aValore) {
		mAnnoFine = aValore;
	}

	public void setMeseFine(BigDecimal aValore) {
		mMeseFine = aValore;
	}

	public void setGiornoFine(BigDecimal aValore) {
		mGiornoFine = aValore;
	}

	public void setCodPeriodoConsumazione(String aValore) {
		mCodPeriodoConsumazione = aValore;
	}

	public void setDescrPeriodoConsumazione(String aValore) {
		mDescrPeriodoConsumazione = aValore;
	}

	public void setDescLuogo(String aValore) {
		mDescLuogo = aValore;
	}

	public void setCodFonte(String aValore) {
		mCodFonte = aValore;
	}

	public void setDescrFonte(String aValore) {
		mDescrFonte = aValore;
	}

	public void setAnnoFonte(BigDecimal aValore) {
		mAnnoFonte = aValore;
	}

	public void setNumeroFonte(String aValore) {
		mNumeroFonte = aValore;
	}

	public void setCodSottonumerazione(String aValore) {
		mCodSottonumerazione = aValore;
	}

	public void setDescrSottonumerazione(String aValore) {
		mDescrSottonumerazione = aValore;
	}

	public void setComma(String aValore) {
		mComma = aValore;
	}

	public void setLettera(String aValore) {
		mLettera = aValore;
	}

	public void setNumero(String aValore) {
		mNumero = aValore;
	}

	public void setArticolo(String aValore) {
		mArticolo = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodTipoPenaDetentiva(String aValore) {
		mCodTipoPenaDetentiva = aValore;
	}

	public void setDescrTipoPenaDetentiva(String aValore) {
		mDescrTipoPenaDetentiva = aValore;
	}

	public void setNumAnni(BigDecimal aValore) {
		mNumAnni = aValore;
	}

	public void setNumMesi(BigDecimal aValore) {
		mNumMesi = aValore;
	}

	public void setNumGiorni(BigDecimal aValore) {
		mNumGiorni = aValore;
	}

	public void setSanzionePecuniaria(BigDecimal aValore) {
		mSanzionePecuniaria = aValore;
	}

	public void setFlagErgastolo(String aValore) {
		mFlagErgastolo = aValore;
	}

	public void setDataInizioIsolamentoDiurno(Date aValore) {
		mDataInizioIsolamentoDiurno = aValore;
	}

	public void setDataFineIsolamentoDiurno(Date aValore) {
		mDataFineIsolamentoDiurno = aValore;
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

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
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

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
	}

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setFlagVisto(String aValore) {
		mFlagVisto = aValore;
	}

	public void setNumAnniIsolamentoDiurno(BigDecimal aValore) {
		mNumAnniIsolamentoDiurno = aValore;
	}

	public void setNumMesiIsolamentoDiurno(BigDecimal aValore) {
		mNumMesiIsolamentoDiurno = aValore;
	}

	public void setNumGiorniIsolamentoDiurno(BigDecimal aValore) {
		mNumGiorniIsolamentoDiurno = aValore;
	}

	public void setStringaConsumazione(String aValore) {
		mStringaConsumazione = aValore;
	}

	public void setIdContinuazioneReato(BigDecimal aValore) {
		mIdContinuazioneReato = aValore;
	}

	public void setTipoContinuazioneReato(String aValore) {
		mTipoContinuazioneReato = aValore;
	}

	public void setKeyReatoNsc(BigDecimal aValore) {
		mKeyReatoNsc = aValore;
	}

	// ***************************************
	// Federica - a9-rr-078
	// aggiunto campo Comma-Qualificante
	public void setCommaQualificante(String aValore) {
		mCommaQualificante = aValore;
	}

	public void setDescrCommaQualificante(String aValore) {
		mDescrCommaQualificante = aValore;
	}
	// ***************************************

	public String getNazionalita() {
		return mNazionalita;
	}

	public void setNazionalita(String nazionalita) {
		mNazionalita = nazionalita;
	}

	public String getCodStatoFascicolo() {
		return mCodStatoFascicolo;
	}

	public void setCodStatoFascicolo(String codStatoFascicolo) {
		mCodStatoFascicolo = codStatoFascicolo;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdReato + " - " + mCodTipoReato + " - " + mDescrTipoReato + " - " + mDataReato + " - "
				+ mProgrNumeroManuale + " - " + mProgrReato + " - " + mProgrCircostanza + " - " + mDataInizio
				+ " - " + mAnnoInizio + " - " + mMeseInizio + " - " + mGiornoInizio + " - " + mDataFine
				+ " - " + mAnnoFine + " - " + mMeseFine + " - " + mGiornoFine + " - "
				+ mCodPeriodoConsumazione + " - " + mDescrPeriodoConsumazione + " - " + mDescLuogo + " - "
				+ mCodFonte + " - " + mDescrFonte + " - " + mAnnoFonte + " - " + mNumeroFonte + " - "
				+ mCodSottonumerazione + " - " + mDescrSottonumerazione + " - " + mComma + " - " + mLettera
				+ " - " + mNumero + " - " + mArticolo + " - " + mNote + " - " + mCodTipoPenaDetentiva + " - "
				+ mDescrTipoPenaDetentiva + " - " + mNumAnni + " - " + mNumMesi + " - " + mNumGiorni + " - "
				+ mSanzionePecuniaria + " - " + mFlagErgastolo + " - " + mDataInizioIsolamentoDiurno + " - "
				+ mDataFineIsolamentoDiurno + " - " + mCodOperatoreInserimento + " - " + mDataInserimento
				+ " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mFasSieIdFascicoloSiep + " - "
				+ mCodTipoSanzione + " - " + mDescrTipoSanzione + " - " + mNumAnniIsolamentoDiurno + " - "
				+ mNumMesiIsolamentoDiurno + " - " + mNumGiorniIsolamentoDiurno + " - "
				+ mIdContinuazioneReato + " - " + mTipoContinuazioneReato + " - " + mKeyReatoNsc + " - " +
				// ***************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				mCommaQualificante + " - " + mDescrCommaQualificante;
		// ***************************************

		return lStr;
	}

	public void calcolaStringaConsumazione() {
		String lString = mDescrPeriodoConsumazione;
		// String ltemp = null;
		String lParziale = "";

		lString = lString.replace('[', 'X');
		lString = lString.replace(']', 'X');

		// Esistono le date di consumazione
		if (((mDataFine != null) || (mDataInizio != null)) && (lString != null && lString.length() > 1)) {
			if (mDataInizio != null)
				lString = lString.replaceFirst("Xdata1X",
						DateUtils.getDateToString(mDataInizio, "dd/MM/yyyy"));

			if (mDataFine != null)
				lString = lString.replaceFirst("Xdata2X", DateUtils.getDateToString(mDataFine, "dd/MM/yyyy"));
		}

		// Esistono le date di consumazione parziali
		boolean isGiorno = true;
		boolean isMese = true;
		boolean isAnno = true;
		if ((((mAnnoInizio != null) || (mMeseInizio != null) || (mGiornoInizio != null))
				&& (mDataInizio == null)) && (lString != null && lString.length() > 1)) {
			if (mGiornoInizio == null) {
				lParziale = "";
				isGiorno = false;
			} else {
				lParziale = mGiornoInizio + "/";
			}

			if (mMeseInizio == null) {
				lParziale += "";
				isMese = false;
			} else {
				lParziale += mMeseInizio + "/";
				if (lParziale.length() == 2)
					lParziale = "0" + lParziale;
			}

			if (mAnnoInizio == null) {
				lParziale += "";
				isAnno = false;
			} else {
				lParziale += mAnnoInizio;
			}

			// rielaboro la data senza gli asterischi al posto del giorno
			if (isGiorno == false && isMese == true && isAnno == true) {
				String dataCorretta = DateUtils
						.getMonth((Integer.parseInt(lParziale.substring(0, 2)) - 1) + "") + " "
						+ lParziale.substring(3, lParziale.length());
				lParziale = dataCorretta;
			}

			lString = lString.replaceFirst("Xdata1X", lParziale);

		}

		isGiorno = true;
		isMese = true;
		isAnno = true;
		if ((((mAnnoFine != null) || (mMeseFine != null) || (mGiornoFine != null)) && (mDataFine == null))
				&& (lString != null && lString.length() > 1)) {
			lParziale = "";
			if (mGiornoFine == null) {
				lParziale = "";
				isGiorno = false;
			} else {
				lParziale = mGiornoFine + "/";
			}

			if (mMeseFine == null) {
				lParziale += "";
				isMese = false;
			} else {
				lParziale += mMeseFine + "/";
				if (lParziale.length() == 2)
					lParziale = "0" + lParziale;
			}

			if (mAnnoFine == null) {
				lParziale += "";
				isAnno = false;
			} else {
				lParziale += mAnnoFine;
			}

			// rielaboro la data senza gli asterischi al posto del giorno
			if (isGiorno == false && isMese == true && isAnno == true) {
				String dataCorretta = DateUtils
						.getMonth((Integer.parseInt(lParziale.substring(0, 2)) - 1) + "") + " "
						+ lParziale.substring(3, lParziale.length());
				lParziale = dataCorretta;
			}

			lString = lString.replaceFirst("Xdata2X", lParziale);
		}

		this.mStringaConsumazione = lString;
	}

	/**
	 * Trasforma il reato in una stringa parlante costituita da tutti i valori del model
	 * 
	 * @return Stringa reato
	 */
	public String toStringReato() {
		String lReato = "";

		if (this.getProgrNumeroManuale() != null && !this.getProgrNumeroManuale().equals("")) {
			lReato = "Reato " + this.getProgrNumeroManuale() + ": ";
		} else {
			lReato = "Reato " + this.getProgrReato() + ": ";
		}

		lReato += "<font class=\"campo\">";

		boolean lFlagAnnoNumero = false;
		if (this.getAnnoFonte() != null && this.getNumeroFonte() != null
				&& !this.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}
		if (lFlagAnnoNumero) {
			if (this.getDescrFonte() != null && !this.getDescrFonte().equals("")
					&& !this.getDescrFonte().equals("-"))
				lReato += " " + this.getDescrFonte() + " ";
			if (this.getAnnoFonte() != null)
				lReato += this.getAnnoFonte().toString();
			if (this.getNumeroFonte() != null && !this.getNumeroFonte().equals(""))
				lReato += "/" + this.getNumeroFonte();
		}

		if (this.getArticolo() != null && !this.getArticolo().equals(""))
			lReato += " art." + this.getArticolo();
		if (this.getDescrSottonumerazione() != null && !this.getDescrSottonumerazione().equals("")
				&& !this.getDescrSottonumerazione().equals("-"))
			lReato += " " + this.getDescrSottonumerazione();

		// ***********************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		if (this.getComma() != null && !this.getComma().equals(""))
			lReato += " c." + this.getComma();
		if (this.getDescrCommaQualificante() != null && !this.getDescrCommaQualificante().equals("")
				&& !this.getDescrCommaQualificante().equals("-"))
			lReato += " " + this.getDescrCommaQualificante();
		// ***********************************************************************************

		if (!lFlagAnnoNumero) {
			if (this.getDescrFonte() != null && !this.getDescrFonte().equals("")
					&& !this.getDescrFonte().equals("-"))
				lReato += " " + this.getDescrFonte();
		}

		// Federica - a9-rr-078
		// commento perché fatto sopra
		// if (this.getComma() != null && !this.getComma().equals(""))
		// lReato += " c. " + this.getComma();
		if (this.getLettera() != null && !this.getLettera().equals(""))
			lReato += " l. " + this.getLettera();
		if (this.getNumero() != null && !this.getNumero().equals(""))
			lReato += " n. " + this.getNumero();

		lReato += "</font>";

		return lReato;
	}

	public String getStringaPenaReato() {
		return mStringaPenaReato;
	}

	public void setMtringaPenaReato(String stringaPenaReato) {
		mStringaPenaReato = stringaPenaReato;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaPenaReato() {
		String lStringPenaReato = "";

		if (this.getDescrTipoPenaDetentiva() != null && this.getDescrTipoPenaDetentiva().length() > 0) {
			lStringPenaReato = getDescrTipoPenaDetentiva();

			if (this.getNumAnni() != null) {
				if (this.getNumAnni().intValue() != 0)
					lStringPenaReato += " Anni " + this.getNumAnni();
			}
			if (this.getNumMesi() != null) {
				if (this.getNumMesi().intValue() != 0)
					lStringPenaReato += " Mesi " + this.getNumMesi();
			}
			if (this.getNumGiorni() != null) {
				if (this.getNumGiorni().intValue() != 0)
					lStringPenaReato += " Giorni " + this.getNumGiorni();
			}

			if (lStringPenaReato.length() > 1) {
				this.mStringaPenaReato = lStringPenaReato;
			}
		}

	}

	public String getStringaPenaPecuniaria() {
		return mStringaPenaPecuniaria;
	}

	public void setStringaPenaPecuniaria(String stringaPenaPecuniaria) {
		mStringaPenaPecuniaria = stringaPenaPecuniaria;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaPenaPecuniaria() {
		String lStringPenaPecuniaria = "";

		if (this.getDescrTipoSanzione() != null && this.getDescrTipoSanzione().length() > 0) {
			lStringPenaPecuniaria = getDescrTipoSanzione();

			if (lStringPenaPecuniaria.length() > 1) {
				this.mStringaPenaPecuniaria += lStringPenaPecuniaria;
				this.mStringaPenaPecuniaria += " Euro ";
				this.mStringaPenaPecuniaria += getSanzionePecuniaria();
			}
		}

	}

	public boolean ismFlagReato() {
		return mFlagReato;
	}

	public void setmFlagReato(boolean mFlagReato) {
		this.mFlagReato = mFlagReato;
	}

}