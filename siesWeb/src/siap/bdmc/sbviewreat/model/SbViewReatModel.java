package siap.bdmc.sbviewreat.model;

/**
* <p>Title: SbViewReatModel</p>
* <p>Description: Classe Model che rappresenta il SbViewReat</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class SbViewReatModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2539838787847412226L;

	private BigDecimal mNumeProgCapoImpu;
	private BigDecimal mNumeProgReat;
	private String mCodiFontGiur;
	private String mDescriFontGiur;
	private BigDecimal mAnnoFontGiur;
	private BigDecimal mNumeFontGiur;
	private BigDecimal mArtiFontGiur;
	private String mCommiArtiFont;
	private String mLettArtiFont;
	private String mNumeArtiFont;
	private String mArtiQualFont;
	private BigDecimal mIdPren;
	private BigDecimal mAnnoFascBdmc;
	private BigDecimal mNumeFascBdmc;
	private String mCodiSedeInst;
	private String mDescriSedeInst;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SbViewReatModel() {
		this.mNumeProgCapoImpu = null;
		this.mNumeProgReat = null;
		this.mCodiFontGiur = "";
		this.mDescriFontGiur = "";
		this.mAnnoFontGiur = null;
		this.mNumeFontGiur = null;
		this.mArtiFontGiur = null;
		this.mCommiArtiFont = "";
		this.mLettArtiFont = "";
		this.mNumeArtiFont = "";
		this.mArtiQualFont = "";
		this.mIdPren = null;
		this.mAnnoFascBdmc = null;
		this.mNumeFascBdmc = null;
		this.mCodiSedeInst = "";
		this.mDescriSedeInst = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SbViewReatModel(SbViewReatModel aModel) {
		this.mNumeProgCapoImpu = aModel.mNumeProgCapoImpu;
		this.mNumeProgReat = aModel.mNumeProgReat;
		this.mCodiFontGiur = aModel.mCodiFontGiur;
		this.mDescriFontGiur = aModel.mDescriFontGiur;
		this.mAnnoFontGiur = aModel.mAnnoFontGiur;
		this.mNumeFontGiur = aModel.mNumeFontGiur;
		this.mArtiFontGiur = aModel.mArtiFontGiur;
		this.mCommiArtiFont = aModel.mCommiArtiFont;
		this.mLettArtiFont = aModel.mLettArtiFont;
		this.mNumeArtiFont = aModel.mNumeArtiFont;
		this.mArtiQualFont = aModel.mArtiQualFont;
		this.mIdPren = aModel.mIdPren;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mCodiSedeInst = aModel.mCodiSedeInst;
		this.mDescriSedeInst = aModel.mDescriSedeInst;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SbViewReatModel(BigDecimal aNumeProgCapoImpu, BigDecimal aNumeProgReat, String aCodiFontGiur,
			String aDescriFontGiur, BigDecimal aAnnoFontGiur, BigDecimal aNumeFontGiur,
			BigDecimal aArtiFontGiur, String aCommiArtiFont, String aLettArtiFont, String aNumeArtiFont,
			String aArtiQualFont, BigDecimal aIdPren, BigDecimal aAnnoFascBdmc, BigDecimal aNumeFascBdmc,
			String aCodiSedeInst) {
		this.mNumeProgCapoImpu = aNumeProgCapoImpu;
		this.mNumeProgReat = aNumeProgReat;
		this.mCodiFontGiur = aCodiFontGiur;
		this.mDescriFontGiur = aDescriFontGiur;
		this.mAnnoFontGiur = aAnnoFontGiur;
		this.mNumeFontGiur = aNumeFontGiur;
		this.mArtiFontGiur = aArtiFontGiur;
		this.mCommiArtiFont = aCommiArtiFont;
		this.mLettArtiFont = aLettArtiFont;
		this.mNumeArtiFont = aNumeArtiFont;
		this.mArtiQualFont = aArtiQualFont;
		this.mIdPren = aIdPren;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mCodiSedeInst = aCodiSedeInst;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getNumeProgCapoImpu() {
		return mNumeProgCapoImpu;
	}

	public BigDecimal getNumeProgReat() {
		return mNumeProgReat;
	}

	public String getCodiFontGiur() {
		return mCodiFontGiur;
	}

	public String getDescriFontGiur() {
		return mDescriFontGiur;
	}

	public BigDecimal getAnnoFontGiur() {
		return mAnnoFontGiur;
	}

	public BigDecimal getNumeFontGiur() {
		return mNumeFontGiur;
	}

	public BigDecimal getArtiFontGiur() {
		return mArtiFontGiur;
	}

	public String getCommiArtiFont() {
		return mCommiArtiFont;
	}

	public String getLettArtiFont() {
		return mLettArtiFont;
	}

	public String getNumeArtiFont() {
		return mNumeArtiFont;
	}

	public String getArtiQualFont() {
		return mArtiQualFont;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public BigDecimal getNumeFascBdmc() {
		return mNumeFascBdmc;
	}

	public String getCodiSedeInst() {
		return mCodiSedeInst;
	}

	public String getDescriSedeInst() {
		return mDescriSedeInst;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setNumeProgCapoImpu(BigDecimal aValore) {
		mNumeProgCapoImpu = aValore;
	}

	public void setNumeProgReat(BigDecimal aValore) {
		mNumeProgReat = aValore;
	}

	public void setCodiFontGiur(String aValore) {
		mCodiFontGiur = aValore;
	}

	public void setDescriFontGiur(String aValore) {
		mDescriFontGiur = aValore;
	}

	public void setAnnoFontGiur(BigDecimal aValore) {
		mAnnoFontGiur = aValore;
	}

	public void setNumeFontGiur(BigDecimal aValore) {
		mNumeFontGiur = aValore;
	}

	public void setArtiFontGiur(BigDecimal aValore) {
		mArtiFontGiur = aValore;
	}

	public void setCommiArtiFont(String aValore) {
		mCommiArtiFont = aValore;
	}

	public void setLettArtiFont(String aValore) {
		mLettArtiFont = aValore;
	}

	public void setNumeArtiFont(String aValore) {
		mNumeArtiFont = aValore;
	}

	public void setArtiQualFont(String aValore) {
		mArtiQualFont = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		mNumeFascBdmc = aValore;
	}

	public void setCodiSedeInst(String aValore) {
		mCodiSedeInst = aValore;
	}

	public void setDescriSedeInst(String aValore) {
		mDescriSedeInst = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SbViewReatModel:\n" + "[ mNumeProgCapoImpu = " + mNumeProgCapoImpu + " ]\n"
				+ "[ mNumeProgReat     = " + mNumeProgReat + " ]\n" + "[ mCodiFontGiur     = " + mCodiFontGiur
				+ " ]\n" + "[ mAnnoFontGiur     = " + mAnnoFontGiur + " ]\n" + "[ mNumeFontGiur     = "
				+ mNumeFontGiur + " ]\n" + "[ mArtiFontGiur     = " + mArtiFontGiur + " ]\n"
				+ "[ mCommiArtiFont    = " + mCommiArtiFont + " ]\n" + "[ mLettArtiFont     = "
				+ mLettArtiFont + " ]\n" + "[ mNumeArtiFont     = " + mNumeArtiFont + " ]\n"
				+ "[ mArtiQualFont     = " + mArtiQualFont + " ]\n" + "[ mIdPren           = " + mIdPren
				+ " ]\n" + "[ mAnnoFascBdmc     = " + mAnnoFascBdmc + " ]\n" + "[ mNumeFascBdmc     = "
				+ mNumeFascBdmc + " ]\n" + "[ mCodiSedeInst     = " + mCodiSedeInst + " ]";
		return lStr;
	}

}