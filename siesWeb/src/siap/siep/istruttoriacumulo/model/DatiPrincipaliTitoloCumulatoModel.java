package siap.siep.istruttoriacumulo.model;

import java.math.BigDecimal;
import java.util.Date;

import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: DatiPrincipaliTitoloCumulatoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il TitoloCumulato
 * </p>
 * 
 * @version 1.0
 */
public class DatiPrincipaliTitoloCumulatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4435731679407120041L;

	private BigDecimal mIdTitoloCumulato;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;

	private Date mDataProvvedimento;
	private BigDecimal mAnnoSentenza;
	private String mNumeroSentenza;
	private String mCodTipoAutoritaEmittente;
	private String mDescrTipoAutoritaEmittente;
	private String mCodLuogoEmittente;
	private String mDescrLuogoEmittente;
	private String mNumSezioneAutoritaEmittente;

	private String mNote;
	private String mEstremiProvvedimento;
	
	private String mEstremiOrdinanza;
	
	// dati sulla pena complessiva del titolo
	private String mArrestoPenaComp;
	private String mReclusionePenaComp;
	private String mIsolamentoPenaComp;
	private BigDecimal mMultaPenaComp;
	private BigDecimal mAmmendaPenaComp;
	// Beneficio Concesso Sul Titolo
	private BigDecimal mIdBeneficioCumulo;
	private String mCodNaturaBeneficio;
	private String mDescrNaturaBeneficio;
	private String mCodTipoBeneficio;
	private String mDescrTipoBeneficio;
	private String mNoteBeneficio;

	private String mFlagTutto;
	
	// MEV70 Stringa Sanzione Sostitutiva (legaa alla Pena Complessiva)
	private String mStringaSanzioneSostitutiva;
	private String mPeriodoSanzioneSostitutiva;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public DatiPrincipaliTitoloCumulatoModel() {
		this.mIdTitoloCumulato = null;
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";

		this.mDataProvvedimento = null;
		this.mAnnoSentenza = null;
		this.mNumeroSentenza = "";
		this.mCodTipoAutoritaEmittente = "";
		this.mDescrTipoAutoritaEmittente = "";
		this.mCodLuogoEmittente = "";
		this.mDescrLuogoEmittente = "";
		this.mNumSezioneAutoritaEmittente = "";

		this.mNote = "";
		this.mFlagTutto = null;

		this.mStringaSanzioneSostitutiva = null;
		this.mPeriodoSanzioneSostitutiva = null;

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public DatiPrincipaliTitoloCumulatoModel(DatiPrincipaliTitoloCumulatoModel aModel) {
		this.mIdTitoloCumulato = aModel.mIdTitoloCumulato;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;

		this.mDataProvvedimento = aModel.mDataProvvedimento;
		this.mAnnoSentenza = aModel.mAnnoSentenza;
		this.mNumeroSentenza = aModel.mNumeroSentenza;
		this.mCodTipoAutoritaEmittente = aModel.mCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aModel.mDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aModel.mCodLuogoEmittente;
		this.mDescrLuogoEmittente = aModel.mDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aModel.mNumSezioneAutoritaEmittente;

		this.mNote = aModel.mNote;

		this.mStringaSanzioneSostitutiva = aModel.mStringaSanzioneSostitutiva;
		this.mPeriodoSanzioneSostitutiva = aModel.mPeriodoSanzioneSostitutiva;

	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public DatiPrincipaliTitoloCumulatoModel(BigDecimal aIdTitoloCumulato, String aCodTipoProvvedimento,
			String aDescrTipoProvvedimento, Date aDataProvvedimento, BigDecimal aAnnoSentenza,
			String aNumeroSentenza, String aCodTipoAutoritaEmittente, String aDescrTipoAutoritaEmittente,
			String aCodLuogoEmittente, String aDescrLuogoEmittente, String aNumSezioneAutoritaEmittente,
			String aNote)

	{
		this.mIdTitoloCumulato = aIdTitoloCumulato;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;

		this.mDataProvvedimento = aDataProvvedimento;
		this.mAnnoSentenza = aAnnoSentenza;
		this.mNumeroSentenza = aNumeroSentenza;
		this.mCodTipoAutoritaEmittente = aCodTipoAutoritaEmittente;
		this.mDescrTipoAutoritaEmittente = aDescrTipoAutoritaEmittente;
		this.mCodLuogoEmittente = aCodLuogoEmittente;
		this.mDescrLuogoEmittente = aDescrLuogoEmittente;
		this.mNumSezioneAutoritaEmittente = aNumSezioneAutoritaEmittente;

		this.mNote = aNote;
	}

	/**
	 * Costruttore che inizializza il model estraendo i dati da un record TitoloCumulato
	 * 
	 * @param aModel
	 */
	public DatiPrincipaliTitoloCumulatoModel(TitoloCumulatoModel aTitoloCumModel) {
		this.mIdTitoloCumulato = aTitoloCumModel.getIdTitoloCumulato();
		this.mCodTipoProvvedimento = aTitoloCumModel.getCodTipoProvvedimento();
		this.mDescrTipoProvvedimento = aTitoloCumModel.getDescrTipoProvvedimento();
		this.mDataProvvedimento = aTitoloCumModel.getDataProvvedimento();
		this.mAnnoSentenza = aTitoloCumModel.getAnnoSentenza();
		this.mNumeroSentenza = aTitoloCumModel.getNumeroSentenza();

		this.mCodTipoAutoritaEmittente = aTitoloCumModel.getCodTipoAutoritaEmittente();
		this.mDescrTipoAutoritaEmittente = aTitoloCumModel.getDescrTipoAutoritaEmittente();
		this.mCodLuogoEmittente = aTitoloCumModel.getCodLuogoEmittente();
		this.mDescrLuogoEmittente = aTitoloCumModel.getDescrLuogoEmittente();
		this.mNumSezioneAutoritaEmittente = aTitoloCumModel.getNumSezioneAutoritaEmittente();
		this.mNote = aTitoloCumModel.getNote();

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

	public String getNote() {
		return mNote;
	}

	public String getEstremiProvvedimento() {
		return mEstremiProvvedimento;
	}
	
	public String getEstremiOrdinanza() {
		return mEstremiOrdinanza;
	}

	public String getArrestoPenaComp() {
		return mArrestoPenaComp;
	}

	public BigDecimal getAmmendaPenaComp() {
		return mAmmendaPenaComp;
	}

	public String getIsolamentoPenaComp() {
		return mIsolamentoPenaComp;
	}

	public String getReclusionePenaComp() {
		return mReclusionePenaComp;
	}

	public BigDecimal getMultaPenaComp() {
		return mMultaPenaComp;
	}

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

	public String getNoteBeneficio() {
		return mNoteBeneficio;
	}

	public String getFlagTutto() {
		return mFlagTutto;
	}
	
	public String getStringaSanzioneSostitutiva() {
		return mStringaSanzioneSostitutiva;
	}

	public String getPeriodoSanzioneSostitutiva() {
		return mPeriodoSanzioneSostitutiva;
	}

	// --
	public String FormaEstremiProvvedimentoperProspetto() {
		String lProvv = "";
		if (this.getDescrTipoProvvedimento() != null)
			lProvv += this.getDescrTipoProvvedimento();

		if (this.getNumeroSentenza() != null && this.getAnnoSentenza() != null)
			lProvv += " N. " + this.getAnnoSentenza().toString() + "/" + this.getNumeroSentenza();

		if (this.getDataProvvedimento() != null)
			lProvv += " del " + DateUtils.getDateToString(this.getDataProvvedimento(), "dd-MM-yyyy");

		if (this.getCodTipoAutoritaEmittente() != null && this.getCodLuogoEmittente() != null) {
			if (this.getCodTipoProvvedimento().compareTo("02") == 0
					|| this.getCodTipoProvvedimento().compareTo("04") == 0) {
				lProvv += ", emesso da " + this.getDescrTipoAutoritaEmittente() + " di "
						+ this.getDescrLuogoEmittente();
			} else {
				lProvv += ", emessa da " + this.getDescrTipoAutoritaEmittente() + " di "
						+ this.getDescrLuogoEmittente();
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

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setEstremiProvvedimento(String aValore) {
		mEstremiProvvedimento = aValore;
	}
	
	public void setEstremiOrdinanza(String aValore) {
		mEstremiOrdinanza = aValore;
	}

	public void setArrestoPenaComp(String aValore) {
		mArrestoPenaComp = aValore;
	}

	public void setAmmendaPenaComp(BigDecimal aValore) {
		mAmmendaPenaComp = aValore;
	}

	public void setIsolamentoPenaComp(String aValore) {
		mIsolamentoPenaComp = aValore;
	}

	public void setReclusionePenaComp(String aValore) {
		mReclusionePenaComp = aValore;
	}

	public void setMultaPenaComp(BigDecimal aValore) {
		mMultaPenaComp = aValore;
	}

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

	public void setNoteBeneficio(String aValore) {
		mNoteBeneficio = aValore;
	}

	public void setFlagTutto(String aValore) {
		mFlagTutto = aValore;
	}

	
	public void setStringaSanzioneSostitutiva(String aValore) {
		mStringaSanzioneSostitutiva = aValore;
	}

	public void setPeriodoSanzioneSostitutiva(String aValore) {
		mPeriodoSanzioneSostitutiva = aValore;
	}
	
	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "TitoloCumulatoModel:\n" + "[ mIdTitoloCumulato            = " + mIdTitoloCumulato + " ]\n"
				+ "[ mCodTipoProvvedimento        = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDataProvvedimento           = " + mDataProvvedimento + " ]\n"
				+ "[ mAnnoSentenza                = " + mAnnoSentenza + " ]\n"
				+ "[ mNumeroSentenza              = " + mNumeroSentenza + " ]\n"
				+ "[ mCodTipoAutoritaEmittente    = " + mCodTipoAutoritaEmittente + " ]\n"
				+ "[ mCodLuogoEmittente           = " + mCodLuogoEmittente + " ]\n"
				+ "[ mNumSezioneAutoritaEmittente = " + mNumSezioneAutoritaEmittente + " ]\n"
				+ "[ mNote                        = " + mNote + " ]";

		return lStr;
	}

}