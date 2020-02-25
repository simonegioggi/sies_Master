package siap.siep.modulocumulo.model;

/**
 * <p>Title: ReatoCumuloModel</p>
 * <p>Description: Classe Model che rappresenta il Reato nell'ambito CUMULO</p>
 * @version 4.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.siep.reato.model.ReatoModel;

public class ReatoCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -3356616479734899823L;
	private BigDecimal mIdReatoCum;
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
	private String mDescrPeriodoConsumazioneCum;
	private String mDescLuogo;

	private String mCodFonte;
	private String mDescrFonte;
	private BigDecimal mAnnoFonte;
	private String mNumeroFonte;
	private String mCodSottonumerazione;
	private String mDescrSottonumerazione;
	private String mComma;
	private String mCommaQualificante;
	private String mDescrCommaQualificante;
	private String mLettera;
	private String mNumero;
	private String mArticolo;

	private String mCodTipoPenaDetentiva;
	private String mDescrTipoPenaDetentiva;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	private BigDecimal mSanzionePecuniaria;
	private String mCodTipoSanzione;
	private String mDescrTipoSanzione;
	private String mFlagErgastolo;
	private BigDecimal mNumAnniIsolamentoDiurno;
	private BigDecimal mNumMesiIsolamentoDiurno;
	private BigDecimal mNumGiorniIsolamentoDiurno;

	private BigDecimal mIdContinuazioneReatoCum;
	private String mTipoContinuazioneReato;
	// per interoperabilita tra SIES e NSC (WS)
	private BigDecimal mKeyReatoNsc;
	private String mNote;

	// campi Cumulo
	private String mFlagStato;
	private String mMotivoModificaNote;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdReatoOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	// Altri campi 1
	private String mFlagVisto;

	// Altri campi 2
	private String mStringaConsumazione;
	private String mStringaPenaReatoCum;
	private String mStringaPenaPecuniaria;

	// COSTRUTTORE DI DEFAULT
	public ReatoCumuloModel() {
		this.mIdReatoCum = null;
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
		this.mDescrPeriodoConsumazioneCum = "";
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
		this.mCodTipoPenaDetentiva = "";
		this.mDescrTipoPenaDetentiva = "";
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;
		this.mSanzionePecuniaria = null;
		this.mFlagErgastolo = "";
		this.mCodTipoSanzione = "";
		this.mDescrTipoSanzione = "";
		this.mNumAnniIsolamentoDiurno = null;
		this.mNumMesiIsolamentoDiurno = null;
		this.mNumGiorniIsolamentoDiurno = null;
		this.mIdContinuazioneReatoCum = null;
		this.mTipoContinuazioneReato = "";
		this.mNote = "";

		this.mKeyReatoNsc = null;

		// campi Cumulo
		this.mFlagStato = "";
		this.mMotivoModificaNote = "";
		this.mTitIdTitoloCumulato = null;
		this.mIdReatoOrigine = null;

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		// --
		this.mStringaPenaReatoCum = "";
		this.mStringaPenaPecuniaria = "";
		// campi a9-rr-078
		this.mCommaQualificante = "";
		this.mDescrCommaQualificante = "";

	}

	// COSTRUTTORE DI COPIA
	public ReatoCumuloModel(ReatoCumuloModel aModel) {
		this.mIdReatoCum = aModel.mIdReatoCum;
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
		this.mDescrPeriodoConsumazioneCum = aModel.mDescrPeriodoConsumazioneCum;
		this.mDescLuogo = aModel.mDescLuogo;

		this.mCodFonte = aModel.mCodFonte;
		this.mDescrFonte = aModel.mDescrFonte;
		this.mAnnoFonte = aModel.mAnnoFonte;
		this.mNumeroFonte = aModel.mNumeroFonte;
		this.mCodSottonumerazione = aModel.mCodSottonumerazione;
		this.mDescrSottonumerazione = aModel.mDescrSottonumerazione;
		this.mComma = aModel.mComma;
		this.mCommaQualificante = aModel.mCommaQualificante;
		this.mDescrCommaQualificante = aModel.mDescrCommaQualificante;
		this.mLettera = aModel.mLettera;
		this.mNumero = aModel.mNumero;
		this.mArticolo = aModel.mArticolo;

		this.mCodTipoPenaDetentiva = aModel.mCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aModel.mDescrTipoPenaDetentiva;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;
		this.mSanzionePecuniaria = aModel.mSanzionePecuniaria;
		this.mFlagErgastolo = aModel.mFlagErgastolo;
		this.mCodTipoSanzione = aModel.mCodTipoSanzione;
		this.mDescrTipoSanzione = aModel.mDescrTipoSanzione;
		this.mNumAnniIsolamentoDiurno = aModel.mNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aModel.mNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aModel.mNumGiorniIsolamentoDiurno;

		this.mIdContinuazioneReatoCum = aModel.mIdContinuazioneReatoCum;
		this.mTipoContinuazioneReato = aModel.mTipoContinuazioneReato;

		this.mKeyReatoNsc = aModel.mKeyReatoNsc;

		this.mNote = aModel.mNote;

		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModificaNote = aModel.mMotivoModificaNote;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIdReatoOrigine = aModel.mIdReatoOrigine;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mStringaPenaReatoCum = aModel.mStringaPenaReatoCum;
		this.mStringaPenaPecuniaria = aModel.mStringaPenaPecuniaria;

	}

	/**
	 * 
	 * @param aModel
	 */
	public ReatoCumuloModel(ReatoModel aModel) {

		this.mCodTipoReato = aModel.getCodTipoReato();
		this.mDataReato = aModel.getDataReato();
		this.mProgrNumeroManuale = aModel.getProgrNumeroManuale();
		this.mProgrReato = aModel.getProgrReato();
		this.mProgrCircostanza = aModel.getProgrCircostanza();

		this.mDataInizio = aModel.getDataInizio();
		this.mAnnoInizio = aModel.getAnnoInizio();
		this.mMeseInizio = aModel.getMeseInizio();
		this.mGiornoInizio = aModel.getGiornoInizio();
		this.mDataFine = aModel.getDataFine();
		this.mAnnoFine = aModel.getAnnoFine();
		this.mMeseFine = aModel.getMeseFine();
		this.mGiornoFine = aModel.getGiornoFine();
		this.mCodPeriodoConsumazione = aModel.getCodPeriodoConsumazione();
		this.mDescLuogo = aModel.getDescLuogo();

		this.mCodFonte = aModel.getCodFonte();
		this.mAnnoFonte = aModel.getAnnoFonte();
		this.mNumeroFonte = aModel.getNumeroFonte();
		this.mCodSottonumerazione = aModel.getCodSottonumerazione();
		this.mComma = aModel.getComma();
		this.mCommaQualificante = aModel.getCommaQualificante();
		this.mLettera = aModel.getLettera();
		this.mNumero = aModel.getNumero();
		this.mArticolo = aModel.getArticolo();

		this.mCodTipoPenaDetentiva = aModel.getCodTipoPenaDetentiva();
		this.mNumAnni = aModel.getNumAnni();
		this.mNumMesi = aModel.getNumMesi();
		this.mNumGiorni = aModel.getNumGiorni();
		this.mCodTipoSanzione = aModel.getCodTipoSanzione();
		this.mSanzionePecuniaria = aModel.getSanzionePecuniaria();
		this.mFlagErgastolo = aModel.getFlagErgastolo();
		this.mNumAnniIsolamentoDiurno = aModel.getNumAnniIsolamentoDiurno();
		this.mNumMesiIsolamentoDiurno = aModel.getNumMesiIsolamentoDiurno();
		this.mNumGiorniIsolamentoDiurno = aModel.getNumGiorniIsolamentoDiurno();

		this.mIdContinuazioneReatoCum = aModel.getIdContinuazioneReato();
		this.mTipoContinuazioneReato = aModel.getTipoContinuazioneReato();
		this.mKeyReatoNsc = aModel.getKeyReatoNsc();
		this.mNote = aModel.getNote();

		this.mIdReatoOrigine = aModel.getIdReato();
	}

	// COSTRUTTORE MODEL
	public ReatoCumuloModel(BigDecimal aIdReatoCum, String aCodTipoReato, String aDescrTipoReato,
			Date aDataReato, String aProgrNumeroManuale, BigDecimal aProgrReato, BigDecimal aProgrCircostanza,
			Date aDataInizio, BigDecimal aAnnoInizio, BigDecimal aMeseInizio, BigDecimal aGiornoInizio,
			Date aDataFine, BigDecimal aAnnoFine, BigDecimal aMeseFine, BigDecimal aGiornoFine,
			String aCodPeriodoConsumazione, String aDescrPeriodoConsumazioneCum, String aDescLuogo,

			String aCodFonte, String aDescrFonte, BigDecimal aAnnoFonte, String aNumeroFonte,
			String aCodSottonumerazione, String aDescrSottonumerazione, String aComma,
			String aCommaQualificante, String aDescrCommaQualificante, String aLettera, String aNumero,
			String aArticolo,

			String aCodTipoPenaDetentiva, String aDescrTipoPenaDetentiva, BigDecimal aNumAnni,
			BigDecimal aNumMesi, BigDecimal aNumGiorni, String aCodTipoSanzione, String aDescrTipoSanzione,
			BigDecimal aSanzionePecuniaria, String aFlagErgastolo, BigDecimal aNumAnniIsolamentoDiurno,
			BigDecimal aNumMesiIsolamentoDiurno, BigDecimal aNumGiorniIsolamentoDiurno,

			String aNote, BigDecimal aKeyReatoNsc, BigDecimal aIdContinuazioneReatoCum,
			String aTipoContinuazioneReato,

			String aFlagStato, String aMotivoModificaNote, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdReatoOrigine,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento) {
		this.mIdReatoCum = aIdReatoCum;
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
		this.mDescrPeriodoConsumazioneCum = aDescrPeriodoConsumazioneCum;
		this.mDescLuogo = aDescLuogo;

		this.mCodFonte = aCodFonte;
		this.mDescrFonte = aDescrFonte;
		this.mAnnoFonte = aAnnoFonte;
		this.mNumeroFonte = aNumeroFonte;
		this.mCodSottonumerazione = aCodSottonumerazione;
		this.mDescrSottonumerazione = aDescrSottonumerazione;
		this.mComma = aComma;
		this.mCommaQualificante = aCommaQualificante;
		this.mDescrCommaQualificante = aDescrCommaQualificante;
		this.mLettera = aLettera;
		this.mNumero = aNumero;
		this.mArticolo = aArticolo;

		this.mCodTipoPenaDetentiva = aCodTipoPenaDetentiva;
		this.mDescrTipoPenaDetentiva = aDescrTipoPenaDetentiva;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;
		this.mCodTipoSanzione = aCodTipoSanzione;
		this.mDescrTipoSanzione = aDescrTipoSanzione;
		this.mSanzionePecuniaria = aSanzionePecuniaria;
		this.mFlagErgastolo = aFlagErgastolo;
		this.mNumAnniIsolamentoDiurno = aNumAnniIsolamentoDiurno;
		this.mNumMesiIsolamentoDiurno = aNumMesiIsolamentoDiurno;
		this.mNumGiorniIsolamentoDiurno = aNumGiorniIsolamentoDiurno;

		this.mKeyReatoNsc = aKeyReatoNsc;
		this.mNote = aNote;
		this.mIdContinuazioneReatoCum = aIdContinuazioneReatoCum;
		this.mTipoContinuazioneReato = aTipoContinuazioneReato;

		this.mFlagStato = aFlagStato;
		this.mMotivoModificaNote = aMotivoModificaNote;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdReatoOrigine = aIdReatoOrigine;

		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdReatoCum() {
		return mIdReatoCum;
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

	public String getDescrPeriodoConsumazioneCum() {
		return mDescrPeriodoConsumazioneCum;
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

	public String getCommaQualificante() {
		return mCommaQualificante;
	}

	public String getDescrCommaQualificante() {
		return mDescrCommaQualificante;
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

	public String getCodTipoSanzione() {
		return mCodTipoSanzione;
	}

	public String getDescrTipoSanzione() {
		return mDescrTipoSanzione;
	}

	public BigDecimal getSanzionePecuniaria() {
		return mSanzionePecuniaria;
	}

	public String getFlagErgastolo() {
		return mFlagErgastolo;
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

	public BigDecimal getIdContinuazioneReatoCum() {
		return mIdContinuazioneReatoCum;
	}

	public String getTipoContinuazioneReato() {
		return mTipoContinuazioneReato;
	}

	public BigDecimal getKeyReatoNsc() {
		return mKeyReatoNsc;
	}

	public String getNote() {
		return mNote;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public String getMotivoModificaNote() {
		return mMotivoModificaNote;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIdReatoOrigine() {
		return mIdReatoOrigine;
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

	// Altri metodi GET
	public String getFlagVisto() {
		return mFlagVisto;
	}

	public String getStringaConsumazione() {
		return mStringaConsumazione;
	}

	public boolean isReato() {
		return (mProgrCircostanza.intValue() == 1);
	}

	public boolean isPenaReatoInserita() {
		return ((mCodTipoPenaDetentiva != null && !mCodTipoPenaDetentiva.equals("-")
				&& !mCodTipoPenaDetentiva.equals(""))
				|| (mNumAnni != null || mNumMesi != null || mNumGiorni != null)
				|| (mSanzionePecuniaria != null && mSanzionePecuniaria.intValue() != 0)
				|| (mCodTipoSanzione != null && !mCodTipoSanzione.equals("-")
						&& !mCodTipoSanzione.equals("")));
	}

	//
	// METODI SET()
	//
	public void setIdReatoCum(BigDecimal aValore) {
		mIdReatoCum = aValore;
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

	public void setDescrPeriodoConsumazioneCum(String aValore) {
		mDescrPeriodoConsumazioneCum = aValore;
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

	public void setCommaQualificante(String aValore) {
		mCommaQualificante = aValore;
	}

	public void setDescrCommaQualificante(String aValore) {
		mDescrCommaQualificante = aValore;
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

	public void setCodTipoSanzione(String aValore) {
		mCodTipoSanzione = aValore;
	}

	public void setDescrTipoSanzione(String aValore) {
		mDescrTipoSanzione = aValore;
	}

	public void setSanzionePecuniaria(BigDecimal aValore) {
		mSanzionePecuniaria = aValore;
	}

	public void setFlagErgastolo(String aValore) {
		mFlagErgastolo = aValore;
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

	public void setIdContinuazioneReatoCum(BigDecimal aValore) {
		mIdContinuazioneReatoCum = aValore;
	}

	public void setTipoContinuazioneReato(String aValore) {
		mTipoContinuazioneReato = aValore;
	}

	public void setKeyReatoNsc(BigDecimal aValore) {
		mKeyReatoNsc = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setMotivoModificaNote(String aValore) {
		mMotivoModificaNote = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIdReatoOrigine(BigDecimal aValore) {
		mIdReatoOrigine = aValore;
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

	public void setFlagVisto(String aValore) {
		mFlagVisto = aValore;
	}

	public void setStringaConsumazione(String aValore) {
		mStringaConsumazione = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "ReatoCumuloModel:\n" + "[ mIdReatoCum                = " + mIdReatoCum + " ]\n"
				+ "[ mCodTipoReato              = " + mCodTipoReato + " ]\n"
				+ "[ mDataReato                 = " + mDataReato + " ]\n" + "[ mProgrNumeroManuale        = "
				+ mProgrNumeroManuale + " ]\n" + "[ mProgrReato                = " + mProgrReato + " ]\n"
				+ "[ mProgrCircostanza          = " + mProgrCircostanza + " ]\n"
				+ "[ mDataInizio                = " + mDataInizio + " ]\n" + "[ mAnnoInizio                = "
				+ mAnnoInizio + " ]\n" + "[ mMeseInizio                = " + mMeseInizio + " ]\n"
				+ "[ mGiornoInizio              = " + mGiornoInizio + " ]\n"
				+ "[ mDataFine                  = " + mDataFine + " ]\n" + "[ mAnnoFine                  = "
				+ mAnnoFine + " ]\n" + "[ mMeseFine                  = " + mMeseFine + " ]\n"
				+ "[ mGiornoFine                = " + mGiornoFine + " ]\n" + "[ mCodPeriodoConsumazione    = "
				+ mCodPeriodoConsumazione + " ]\n" + "[ mDescLuogo                 = " + mDescLuogo + " ]\n"
				+ "[ mCodFonte                  = " + mCodFonte + " ]\n" + "[ mAnnoFonte                 = "
				+ mAnnoFonte + " ]\n" + "[ mNumeroFonte               = " + mNumeroFonte + " ]\n"
				+ "[ mCodSottonumerazione       = " + mCodSottonumerazione + " ]\n"
				+ "[ mComma                     = " + mComma + " ]\n" + "[ mCommaQualificante         = "
				+ mCommaQualificante + " ]\n" + "[ mLettera                   = " + mLettera + " ]\n"
				+ "[ mNumero                    = " + mNumero + " ]\n" + "[ mArticolo                  = "
				+ mArticolo + " ]\n" + "[ mCodTipoPenaDetentiva      = " + mCodTipoPenaDetentiva + " ]\n"
				+ "[ mNumAnni                   = " + mNumAnni + " ]\n" + "[ mNumMesi                   = "
				+ mNumMesi + " ]\n" + "[ mNumGiorni                 = " + mNumGiorni + " ]\n"
				+ "[ mCodTipoSanzione           = " + mCodTipoSanzione + " ]\n"
				+ "[ mSanzionePecuniaria        = " + mSanzionePecuniaria + " ]\n"
				+ "[ mFlagErgastolo             = " + mFlagErgastolo + " ]\n"
				+ "[ mNumGiorniIsolamentoDiurno = " + mNumGiorniIsolamentoDiurno + " ]\n"
				+ "[ mNumMesiIsolamentoDiurno   = " + mNumMesiIsolamentoDiurno + " ]\n"
				+ "[ mNumAnniIsolamentoDiurno   = " + mNumAnniIsolamentoDiurno + " ]\n"
				+ "[ mNote                      = " + mNote + " ]\n" + "[ mIdContinuazioneReatoCum   = "
				+ mIdContinuazioneReatoCum + " ]\n" + "[ mTipoContinuazioneReato    = "
				+ mTipoContinuazioneReato + " ]\n" + "[ mKeyReatoNsc               = " + mKeyReatoNsc + " ]\n"
				+ "[ mFlagStato                 = " + mFlagStato + " ]\n" + "[ mMotivoModifica            = "
				+ mMotivoModificaNote + " ]\n" + "[ mTitIdTitoloCumulato       = " + mTitIdTitoloCumulato
				+ " ]\n" + "[ mIdReatoOrigine            = " + mIdReatoOrigine + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

	/**
	 * 
	 */
	public void calcolaStringaConsumazioneCum() {
		String lString = mDescrPeriodoConsumazioneCum;
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
	public String toStringReatoCum() {
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

		// campi a9-rr-078
		if (this.getComma() != null && !this.getComma().equals(""))
			lReato += " c." + this.getComma();
		if (this.getDescrCommaQualificante() != null && !this.getDescrCommaQualificante().equals("")
				&& !this.getDescrCommaQualificante().equals("-"))
			lReato += " " + this.getDescrCommaQualificante();

		if (!lFlagAnnoNumero) {
			if (this.getDescrFonte() != null && !this.getDescrFonte().equals("")
					&& !this.getDescrFonte().equals("-"))
				lReato += " " + this.getDescrFonte();
		}

		// if (this.getComma() != null && !this.getComma().equals(""))
		// lReato += " c. " + this.getComma();
		if (this.getLettera() != null && !this.getLettera().equals(""))
			lReato += " l. " + this.getLettera();
		if (this.getNumero() != null && !this.getNumero().equals(""))
			lReato += " n. " + this.getNumero();

		lReato += "</font>";

		return lReato;
	}

	public String getStringaPenaReatoCum() {
		return mStringaPenaReatoCum;
	}

	public void setMstringaPenaReato(String stringaPenaReato) {
		mStringaPenaReatoCum = stringaPenaReato;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaPenaReatoCum() {
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
				this.mStringaPenaReatoCum = lStringPenaReato;
			}
		}

	}

	public String getStringaPenaPecuniariaCum() {
		return mStringaPenaPecuniaria;
	}

	public void setStringaPenaPecuniariaCum(String stringaPenaPecuniaria) {
		mStringaPenaPecuniaria = stringaPenaPecuniaria;
	}

	/**
	 * calcolaStringaArresto per la Stampa in cui serve la stringa composta di anni mesi giorni
	 * 
	 * @return
	 */
	public void calcolaStringaPenaPecuniariaCum() {
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

}
