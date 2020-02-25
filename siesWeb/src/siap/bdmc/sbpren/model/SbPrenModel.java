package siap.bdmc.sbpren.model;

/**
* <p>Title: SbPrenModel</p>
* <p>Description: Classe Model che rappresenta il SbPren</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SbPrenModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4260799334396346249L;

	private Date mDataPren;
	private BigDecimal mIdPren;
	private String mUtenSies;
	private String mNote;
	private Date mDataAnnu;
	private String mMotiAnnu;
	private BigDecimal mCodiStatPren;
	private String mFlagSeleCapoImpu;
	private String mFlagSeleProcPena;
	// int. Bdmc private BigDecimal mFlagSeleSent;
	private String mFlagPrenPres;
	private String mCodiUffiSies;
	private String mDescriUffiSies;
	private String mFlagSelePeriComp;
	private BigDecimal mNumeFascBdmc;
	private BigDecimal mAnnoFascBdmc;
	private String mCodiSedeInst;
	private String mDescriSedeInst;
	private String mCognSogg;
	private String mNomeSogg;
	private String mFlagSess;
	private String mCodiStat;
	private String mDescriStat;
	private String mLuogNasc;
	private String mCodiIdenAfis;
	private String mDescriIdenAfis;
	private Date mDataNasc;
	private String mFlagSeleCircSogg;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SbPrenModel() {
		this.mDataPren = null;
		this.mIdPren = null;
		this.mUtenSies = "";
		this.mNote = "";
		this.mDataAnnu = null;
		this.mMotiAnnu = "";
		this.mCodiStatPren = null;
		this.mFlagSeleCapoImpu = "";
		this.mFlagSeleProcPena = "";
		// this.mFlagSeleSent = null;
		this.mFlagPrenPres = "";
		this.mCodiUffiSies = "";
		this.mDescriUffiSies = "";
		this.mFlagSelePeriComp = "";
		this.mNumeFascBdmc = null;
		this.mAnnoFascBdmc = null;
		this.mCodiSedeInst = "";
		this.mDescriSedeInst = "";
		this.mCognSogg = "";
		this.mNomeSogg = "";
		this.mFlagSess = "";
		this.mCodiStat = "";
		this.mDescriStat = "";
		this.mLuogNasc = "";
		this.mCodiIdenAfis = "";
		this.mDescriIdenAfis = "";
		this.mDataNasc = null;
		this.mFlagSeleCircSogg = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SbPrenModel(SbPrenModel aModel) {
		this.mDataPren = aModel.mDataPren;
		this.mIdPren = aModel.mIdPren;
		this.mUtenSies = aModel.mUtenSies;
		this.mNote = aModel.mNote;
		this.mDataAnnu = aModel.mDataAnnu;
		this.mMotiAnnu = aModel.mMotiAnnu;
		this.mCodiStatPren = aModel.mCodiStatPren;
		this.mFlagSeleCapoImpu = aModel.mFlagSeleCapoImpu;
		this.mFlagSeleProcPena = aModel.mFlagSeleProcPena;
		// this.mFlagSeleSent = aModel.mFlagSeleSent;
		this.mFlagPrenPres = aModel.mFlagPrenPres;
		this.mCodiUffiSies = aModel.mCodiUffiSies;
		this.mDescriUffiSies = aModel.mDescriUffiSies;
		this.mFlagSelePeriComp = aModel.mFlagSelePeriComp;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mCodiSedeInst = aModel.mCodiSedeInst;
		this.mDescriSedeInst = aModel.mDescriSedeInst;
		this.mCognSogg = aModel.mCognSogg;
		this.mNomeSogg = aModel.mNomeSogg;
		this.mFlagSess = aModel.mFlagSess;
		this.mCodiStat = aModel.mCodiStat;
		this.mDescriStat = aModel.mDescriStat;
		this.mLuogNasc = aModel.mLuogNasc;
		this.mCodiIdenAfis = aModel.mCodiIdenAfis;
		this.mDescriIdenAfis = aModel.mDescriIdenAfis;
		this.mDataNasc = aModel.mDataNasc;
		this.mFlagSeleCircSogg = aModel.mFlagSeleCircSogg;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SbPrenModel(Date aDataPren, BigDecimal aIdPren, String aUtenSies, String aNote, Date aDataAnnu,
			String aMotiAnnu, BigDecimal aCodiStatPren, String aFlagSeleCapoImpu, String aFlagSeleProcPena,
			// BigDecimal aFlagSeleSent,
			String aFlagPrenPres, String aCodiUffiSies, String aDescriUffiSies, String aFlagSelePeriComp,
			BigDecimal aNumeFascBdmc, BigDecimal aAnnoFascBdmc, String aCodiSedeInst, String aDescriSedeInst,
			String aCognSogg, String aNomeSogg, String aFlagSess, String aCodiStat, String aDescriStat,
			String aLuogNasc, String aCodiIdenAfis, String aDescriIdenAfis, Date aDataNasc,
			String aFlagSeleCircSogg) {
		this.mDataPren = aDataPren;
		this.mIdPren = aIdPren;
		this.mUtenSies = aUtenSies;
		this.mNote = aNote;
		this.mDataAnnu = aDataAnnu;
		this.mMotiAnnu = aMotiAnnu;
		this.mCodiStatPren = aCodiStatPren;
		this.mFlagSeleCapoImpu = aFlagSeleCapoImpu;
		this.mFlagSeleProcPena = aFlagSeleProcPena;
		// this.mFlagSeleSent = aFlagSeleSent;
		this.mFlagPrenPres = aFlagPrenPres;
		this.mCodiUffiSies = aCodiUffiSies;
		this.mDescriUffiSies = aDescriUffiSies;
		this.mFlagSelePeriComp = aFlagSelePeriComp;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mCodiSedeInst = aCodiSedeInst;
		this.mDescriSedeInst = aDescriSedeInst;
		this.mCognSogg = aCognSogg;
		this.mNomeSogg = aNomeSogg;
		this.mFlagSess = aFlagSess;
		this.mCodiStat = aCodiStat;
		this.mDescriStat = aDescriStat;
		this.mLuogNasc = aLuogNasc;
		this.mCodiIdenAfis = aCodiIdenAfis;
		this.mDescriIdenAfis = aDescriIdenAfis;
		this.mDataNasc = aDataNasc;
		this.mFlagSeleCircSogg = aFlagSeleCircSogg;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public Date getDataPren() {
		return mDataPren;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public String getUtenSies() {
		return mUtenSies;
	}

	public String getNote() {
		return mNote;
	}

	public Date getDataAnnu() {
		return mDataAnnu;
	}

	public String getMotiAnnu() {
		return mMotiAnnu;
	}

	public BigDecimal getCodiStatPren() {
		return mCodiStatPren;
	}

	public String getFlagSeleCapoImpu() {
		return mFlagSeleCapoImpu;
	}

	public String getFlagSeleProcPena() {
		return mFlagSeleProcPena;
	}

	// public BigDecimal getFlagSeleSent() { return mFlagSeleSent; }
	public String getFlagPrenPres() {
		return mFlagPrenPres;
	}

	public String getCodiUffiSies() {
		return mCodiUffiSies;
	}

	public String getDescriUffiSies() {
		System.out.print("CHIAMATA getDescriUffiSies");
		return mDescriUffiSies;
	}

	public String getFlagSelePeriComp() {
		return mFlagSelePeriComp;
	}

	public BigDecimal getNumeFascBdmc() {
		return mNumeFascBdmc;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public String getCodiSedeInst() {
		return mCodiSedeInst;
	}

	public String getDescriSedeInst() {
		System.out.print("CHIAMATA getDescriSedeInst=" + mDescriSedeInst);
		return mDescriSedeInst;
	}

	public String getCognSogg() {
		return mCognSogg;
	}

	public String getNomeSogg() {
		return mNomeSogg;
	}

	public String getFlagSess() {
		return mFlagSess;
	}

	public String getCodiStat() {
		return mCodiStat;
	}

	public String getDescriStat() {
		return mDescriStat;
	}

	public String getLuogNasc() {
		return mLuogNasc;
	}

	public String getCodiIdenAfis() {
		return mCodiIdenAfis;
	}

	public String getDescriIdenAfis() {
		return mDescriIdenAfis;
	}

	public Date getDataNasc() {
		return mDataNasc;
	}

	public String getFlagSeleCircSogg() {
		return mFlagSeleCircSogg;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setDataPren(Date aValore) {
		mDataPren = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setUtenSies(String aValore) {
		mUtenSies = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setDataAnnu(Date aValore) {
		mDataAnnu = aValore;
	}

	public void setMotiAnnu(String aValore) {
		mMotiAnnu = aValore;
	}

	public void setCodiStatPren(BigDecimal aValore) {
		mCodiStatPren = aValore;
	}

	public void setFlagSeleCapoImpu(String aValore) {
		mFlagSeleCapoImpu = aValore;
	}

	public void setFlagSeleProcPena(String aValore) {
		mFlagSeleProcPena = aValore;
	}

	// public void setFlagSeleSent (BigDecimal aValore ) { mFlagSeleSent = aValore; }
	public void setFlagPrenPres(String aValore) {
		mFlagPrenPres = aValore;
	}

	public void setCodiUffiSies(String aValore) {
		mCodiUffiSies = aValore;
	}

	public void setDescriUffiSies(String aValore) {
		mDescriUffiSies = aValore;
	}

	public void setFlagSelePeriComp(String aValore) {
		mFlagSelePeriComp = aValore;
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		mNumeFascBdmc = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setCodiSedeInst(String aValore) {
		mCodiSedeInst = aValore;
	}

	public void setDescriSedeInst(String aValore) {
		mDescriSedeInst = aValore;
	}

	public void setCognSogg(String aValore) {
		mCognSogg = aValore;
	}

	public void setNomeSogg(String aValore) {
		mNomeSogg = aValore;
	}

	public void setFlagSess(String aValore) {
		mFlagSess = aValore;
	}

	public void setCodiStat(String aValore) {
		mCodiStat = aValore;
	}

	public void setDescriStat(String aValore) {
		mDescriStat = aValore;
	}

	public void setLuogNasc(String aValore) {
		mLuogNasc = aValore;
	}

	public void setCodiIdenAfis(String aValore) {
		mCodiIdenAfis = aValore;
	}

	public void setDescriIdenAfis(String aValore) {
		mDescriIdenAfis = aValore;
	}

	public void setDataNasc(Date aValore) {
		mDataNasc = aValore;
	}

	public void setFlagSeleCircSogg(String aValore) {
		mFlagSeleCircSogg = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SbPrenModel:\n" + "[ mDataPren         = " + mDataPren + " ]\n" + "[ mIdPren           = "
				+ mIdPren + " ]\n" + "[ mUtenSies         = " + mUtenSies + " ]\n" + "[ mNote             = "
				+ mNote + " ]\n" + "[ mDataAnnu         = " + mDataAnnu + " ]\n" + "[ mMotiAnnu         = "
				+ mMotiAnnu + " ]\n" + "[ mFlagPren         = " + mCodiStatPren + " ]\n"
				+ "[ mFlagSeleCapoImpu = " + mFlagSeleCapoImpu + " ]\n" + "[ mFlagSeleProcPena = "
				+ mFlagSeleProcPena + " ]\n" +
				// "[ mFlagSeleSent = "+mFlagSeleSent+" ]\n"+
				"[ mFlagPrenPres     = " + mFlagPrenPres + " ]\n" + "[ mCodiUffiSies     = " + mCodiUffiSies
				+ " ]\n" + "[ mFlagSelePeriComp = " + mFlagSelePeriComp + " ]\n" + "[ mNumeFascBdmc     = "
				+ mNumeFascBdmc + " ]\n" + "[ mAnnoFascBdmc     = " + mAnnoFascBdmc + " ]\n"
				+ "[ mCodiSedeInst     = " + mCodiSedeInst + " ]\n" + "[ mCognSogg         = " + mCognSogg
				+ " ]\n" + "[ mNomeSogg         = " + mNomeSogg + " ]\n" + "[ mFlagSess         = "
				+ mFlagSess + " ]\n" + "[ mCodiStat         = " + mCodiStat + " ]\n"
				+ "[ mLuogNasc         = " + mLuogNasc + " ]\n" + "[ mCodiIdenAfis     = " + mCodiIdenAfis
				+ " ]\n" + "[ mDataNasc         = " + mDataNasc + " ]\n" + "[ mFlagSeleCircSogg = "
				+ mFlagSeleCircSogg + " ]";
		return lStr;
	}

}