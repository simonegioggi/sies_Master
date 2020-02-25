package siap.siep.modulocumulo.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class PenaAccessoriaCumuloModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2985336367793537679L;
	private BigDecimal mIdPenaAccessoriaCumulo;
	private String mCodTipoPenaAccessoria;
	private String mDescrTipoPenaAccessoria;
	private String mDescrAltrePA;
	private String mDurata;
	private String mDescrDurata;
	private BigDecimal mNumAnni;
	private BigDecimal mNumMesi;
	private BigDecimal mNumGiorni;
	/*
	 * private BigDecimal mAnnoOrdinanzaGE; private BigDecimal mNumeroOrdinanzaGE; private Date
	 * mDataOrdinanzaGE; private String mCodTipoUfficioOrdinanzaGE; private String
	 * mDescrTipoUfficioOrdinanzaGE; private String mCodLuogoUfficioOrdinanzaGE; private String
	 * mDescrLuogoUfficioOrdinanzaGE;
	 */
	private String mNote;
	private BigDecimal mBenIdBeneficioOrig;
	private BigDecimal mBenIdBeneficioCumulo;

	private String mFlagStato;
	private String mMotivoModifica;
	private BigDecimal mTitIdTitoloCumulato;
	private BigDecimal mIdPenaAccessoriaOrigine;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;

	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private String mFlagDatiFinali;

	private TitoloCumulatoModel mTitoloCumulato;
	private String mStringaDurata;

	public PenaAccessoriaCumuloModel() {
		this.mIdPenaAccessoriaCumulo = null;
		this.mCodTipoPenaAccessoria = "-";
		this.mDescrTipoPenaAccessoria = null;
		this.mDescrAltrePA = null;
		this.mDurata = "-";
		this.mDescrDurata = null;
		this.mNumAnni = null;
		this.mNumMesi = null;
		this.mNumGiorni = null;

		this.mNote = null;
		this.mBenIdBeneficioOrig = null;
		this.mBenIdBeneficioCumulo = null;

		this.mFlagStato = null;
		this.mMotivoModifica = null;
		this.mTitIdTitoloCumulato = null;
		this.mIdPenaAccessoriaOrigine = null;

		/*
		 * this.mAnnoOrdinanzaGE = null; this.mNumeroOrdinanzaGE = null; this.mDataOrdinanzaGE = null;
		 * this.mCodTipoUfficioOrdinanzaGE = "-"; this.mDescrTipoUfficioOrdinanzaGE = "";
		 * this.mCodLuogoUfficioOrdinanzaGE = "-"; this.mDescrLuogoUfficioOrdinanzaGE = "";
		 */
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "-";
		this.mCodOperatoreAggiornamento = null;
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "-";

		this.mFlagDatiFinali = "";
	}

	public PenaAccessoriaCumuloModel(PenaAccessoriaCumuloModel aModel) {
		this.mIdPenaAccessoriaCumulo = aModel.mIdPenaAccessoriaCumulo;
		this.mCodTipoPenaAccessoria = aModel.mCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aModel.mDescrTipoPenaAccessoria;
		this.mDescrAltrePA = aModel.mDescrAltrePA;

		this.mDurata = aModel.mDurata;
		this.mDescrDurata = aModel.mDescrDurata;
		this.mNumAnni = aModel.mNumAnni;
		this.mNumMesi = aModel.mNumMesi;
		this.mNumGiorni = aModel.mNumGiorni;

		this.mNote = aModel.mNote;
		this.mBenIdBeneficioOrig = aModel.mBenIdBeneficioOrig;
		this.mBenIdBeneficioCumulo = aModel.mBenIdBeneficioCumulo;

		this.mFlagStato = aModel.mFlagStato;
		this.mMotivoModifica = aModel.mMotivoModifica;
		this.mTitIdTitoloCumulato = aModel.mTitIdTitoloCumulato;
		this.mIdPenaAccessoriaOrigine = aModel.mIdPenaAccessoriaOrigine;

		/*
		 * this.mAnnoOrdinanzaGE = aModel.mAnnoOrdinanzaGE; this.mNumeroOrdinanzaGE =
		 * aModel.mNumeroOrdinanzaGE; this.mDataOrdinanzaGE = aModel.mDataOrdinanzaGE;
		 * this.mCodTipoUfficioOrdinanzaGE = aModel.mCodTipoUfficioOrdinanzaGE;
		 * this.mDescrTipoUfficioOrdinanzaGE = aModel.mDescrTipoUfficioOrdinanzaGE;
		 * this.mCodLuogoUfficioOrdinanzaGE = aModel.mCodLuogoUfficioOrdinanzaGE;
		 * this.mDescrLuogoUfficioOrdinanzaGE = aModel.mDescrLuogoUfficioOrdinanzaGE;
		 */
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;

		this.mFlagDatiFinali = aModel.mFlagDatiFinali;

	}

	// COSTRUTTORE MODEL
	public PenaAccessoriaCumuloModel(BigDecimal aIdPenaAccessoriaCumulo, String aCodTipoPenaAccessoria,
			String aDescrTipoPenaAccessoria, String aDescrAltrePA,

			String aDurata, String aDescrDurata, BigDecimal aNumAnni, BigDecimal aNumMesi,
			BigDecimal aNumGiorni,
			/*
			 * BigDecimal aAnnoOrdinanzaGE, BigDecimal aNumeroOrdinanzaGE, Date aDataOrdinanzaGE, String
			 * aCodTipoUfficioOrdinanzaGE, String aDescrTipoUfficioOrdinanzaGE, String
			 * aCodLuogoUfficioOrdinanzaGE, String aDescrLuogoUfficioOrdinanzaGE,
			 */
			String aNote, BigDecimal aBenIdBeneficioOrig, BigDecimal aBenIdBeneficioCumulo,

			String aFlagStato, String aMotivoModifica, BigDecimal aTitIdTitoloCumulato,
			BigDecimal aIdPenaAccessoriaOrigine, String aFlagDatiFinali,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,

			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento)

	{
		this.mIdPenaAccessoriaCumulo = aIdPenaAccessoriaCumulo;
		this.mCodTipoPenaAccessoria = aCodTipoPenaAccessoria;
		this.mDescrTipoPenaAccessoria = aDescrTipoPenaAccessoria;
		this.mDescrAltrePA = aDescrAltrePA;

		this.mDurata = aDurata;
		this.mDescrDurata = aDescrDurata;
		this.mNumAnni = aNumAnni;
		this.mNumMesi = aNumMesi;
		this.mNumGiorni = aNumGiorni;

		/*
		 * this.mAnnoOrdinanzaGE = aAnnoOrdinanzaGE; this.mNumeroOrdinanzaGE = aNumeroOrdinanzaGE;
		 * this.mDataOrdinanzaGE = aDataOrdinanzaGE; this.mCodTipoUfficioOrdinanzaGE =
		 * aCodTipoUfficioOrdinanzaGE; this.mDescrTipoUfficioOrdinanzaGE = aDescrTipoUfficioOrdinanzaGE;
		 * this.mCodLuogoUfficioOrdinanzaGE = aCodLuogoUfficioOrdinanzaGE; this.mDescrLuogoUfficioOrdinanzaGE
		 * = aDescrLuogoUfficioOrdinanzaGE;
		 */
		this.mNote = aNote;
		this.mBenIdBeneficioOrig = aBenIdBeneficioOrig;
		this.mBenIdBeneficioCumulo = aBenIdBeneficioCumulo;

		this.mFlagStato = aFlagStato;
		this.mMotivoModifica = aMotivoModifica;
		this.mTitIdTitoloCumulato = aTitIdTitoloCumulato;
		this.mIdPenaAccessoriaOrigine = aIdPenaAccessoriaOrigine;
		this.mFlagDatiFinali = aFlagDatiFinali;

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
	public BigDecimal getIdPenaAccessoriaCumulo() {
		return mIdPenaAccessoriaCumulo;
	}

	public String getCodTipoPenaAccessoria() {
		return mCodTipoPenaAccessoria;
	}

	public String getDescrTipoPenaAccessoria() {
		return mDescrTipoPenaAccessoria;
	}

	public String getDescrAltrePA() {
		return mDescrAltrePA;
	}

	public String getDurata() {
		return mDurata;
	}

	public String getDescrDurata() {
		return mDescrDurata;
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

	/*
	 * public BigDecimal getAnnoOrdinanzaGE() { return mAnnoOrdinanzaGE; } public BigDecimal
	 * getNumeroOrdinanzaGE() { return mNumeroOrdinanzaGE; } public Date getDataOrdinanzaGE() { return
	 * mDataOrdinanzaGE; } public String getCodTipoUfficioOrdinanzaGE() { return mCodTipoUfficioOrdinanzaGE; }
	 * public String getDescrTipoUfficioOrdinanzaGE() { return mDescrTipoUfficioOrdinanzaGE; } public String
	 * getCodLuogoUfficioOrdinanzaGE() { return mCodLuogoUfficioOrdinanzaGE; } public String
	 * getDescrLuogoUfficioOrdinanzaGE() { return mDescrLuogoUfficioOrdinanzaGE; }
	 */
	public String getNote() {
		return mNote;
	}

	public BigDecimal getBenIdBeneficioOrig() {
		return mBenIdBeneficioOrig;
	}

	public BigDecimal getBenIdBeneficioCumulo() {
		return mBenIdBeneficioCumulo;
	}

	public String getMotivoModifica() {
		return mMotivoModifica;
	}

	public String getFlagStato() {
		return mFlagStato;
	}

	public BigDecimal getTitIdTitoloCumulato() {
		return mTitIdTitoloCumulato;
	}

	public BigDecimal getIdPenaAccessoriaOrigine() {
		return mIdPenaAccessoriaOrigine;
	}

	public String getFlagDatiFinali() {
		return mFlagDatiFinali;
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

	public TitoloCumulatoModel getTitoloCumulato() {
		return mTitoloCumulato;
	}

	public String getStringaDurata() {
		return mStringaDurata;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdPenaAccessoriaCumulo(BigDecimal aValore) {
		mIdPenaAccessoriaCumulo = aValore;
	}

	public void setCodTipoPenaAccessoria(String aValore) {
		mCodTipoPenaAccessoria = aValore;
	}

	public void setDescrTipoPenaAccessoria(String aValore) {
		mDescrTipoPenaAccessoria = aValore;
	}

	public void setDescrAltrePA(String aValore) {
		mDescrAltrePA = aValore;
	}

	public void setDurata(String aValore) {
		mDurata = aValore;
	}

	public void setDescrDurata(String aValore) {
		mDescrDurata = aValore;
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

	/*
	 * public void setAnnoOrdinanzaGE(BigDecimal aValore ) { mAnnoOrdinanzaGE = aValore; } public void
	 * setNumeroOrdinanzaGE(BigDecimal aValore ) { mNumeroOrdinanzaGE = aValore; } public void
	 * setDataOrdinanzaGE(Date aValore ) { mDataOrdinanzaGE = aValore; } public void
	 * setCodTipoUfficioOrdinanzaGE(String aValore ) { mCodTipoUfficioOrdinanzaGE = aValore; } public void
	 * setDescrTipoUfficioOrdinanzaGE(String aValore ) { mDescrTipoUfficioOrdinanzaGE = aValore; } public void
	 * setCodLuogoUfficioOrdinanzaGE(String aValore ) { mCodLuogoUfficioOrdinanzaGE = aValore; } public void
	 * setDescrLuogoUfficioOrdinanzaGE(String aValore ) { mDescrLuogoUfficioOrdinanzaGE = aValore; }
	 */
	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setBenIdBeneficioOrig(BigDecimal aValore) {
		mBenIdBeneficioOrig = aValore;
	}

	public void setBenIdBeneficioCumulo(BigDecimal aValore) {
		mBenIdBeneficioCumulo = aValore;
	}

	public void setMotivoModifica(String aValore) {
		mMotivoModifica = aValore;
	}

	public void setFlagStato(String aValore) {
		mFlagStato = aValore;
	}

	public void setTitIdTitoloCumulato(BigDecimal aValore) {
		mTitIdTitoloCumulato = aValore;
	}

	public void setIdPenaAccessoriaOrigine(BigDecimal aValore) {
		mIdPenaAccessoriaOrigine = aValore;
	}

	public void setFlagDatiFinali(String aValore) {
		mFlagDatiFinali = aValore;
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

	public void setTitoloCumulato(TitoloCumulatoModel aValore) {
		mTitoloCumulato = aValore;
	}

	public void setStringaDurata(String aValore) {
		mStringaDurata = aValore;
	}

	public void calcolaStringaDurataPACum() {
		String lStringaDur = "";

		if (mNumAnni != null && mNumAnni.intValue() != 0)
			lStringaDur = "Anni " + mNumAnni;

		if (mNumMesi != null && mNumMesi.intValue() != 0)
			lStringaDur += " Mesi " + mNumMesi;

		if (mNumGiorni != null && mNumGiorni.intValue() != 0)
			lStringaDur += " Giorni " + mNumGiorni;

		if (lStringaDur.length() > 1) {
			this.mStringaDurata = lStringaDur.trim();
		} else {
			this.mStringaDurata = null;
		}
	}

}