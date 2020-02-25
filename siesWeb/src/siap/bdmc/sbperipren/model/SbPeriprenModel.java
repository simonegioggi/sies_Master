package siap.bdmc.sbperipren.model;

/**
* <p>Title: SbPeriprenModel</p>
* <p>Description: Classe Model che rappresenta il SbPeripren</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SbPeriprenModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7592999933634539294L;

	private Date mDataInizPeri;
	private Date mDataFinePeri;
	private BigDecimal mProgPeriPres;
	private BigDecimal mIdPren;
	// sostituzione cod con codi
	private String mCodiUffiSies;
	private String mDescrUffiSies;
	private BigDecimal mAnnoFascSiep;
	private BigDecimal mNumeFascSiep;
	private String mCodiSedeInst;
	private String mDescriSedeInst;
	private BigDecimal mAnnoFascBdmc;
	private BigDecimal mNumeFascBdmc;
	private String mCodStatPrenPeri;
	private String mDescrStatPrenPeri;
	// campo non piu' in uso
	// private String mCodTipoPeri;
	private String mDescrTipoPeri;
	private Date mDataPrenPeri;
	private Date mDataDaPeri;
	private Date mDataAPeri;
	private String mDescPeri;
	private Date mDataModiPrenPeri;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SbPeriprenModel() {
		this.mDataInizPeri = null;
		this.mDataFinePeri = null;
		this.mProgPeriPres = null;
		this.mIdPren = null;
		this.mCodiUffiSies = "";
		this.mDescrUffiSies = "";
		this.mAnnoFascSiep = null;
		this.mNumeFascSiep = null;
		this.mCodiSedeInst = "";
		this.mDescriSedeInst = "";
		this.mAnnoFascBdmc = null;
		this.mNumeFascBdmc = null;
		this.mCodStatPrenPeri = "";
		this.mDescrStatPrenPeri = "";
		// this.mCodTipoPeri = "";
		this.mDescrTipoPeri = "";
		this.mDataPrenPeri = null;
		this.mDataDaPeri = null;
		this.mDataAPeri = null;
		this.mDescPeri = "";
		this.mDataModiPrenPeri = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SbPeriprenModel(SbPeriprenModel aModel) {
		this.mDataInizPeri = aModel.mDataInizPeri;
		this.mDataFinePeri = aModel.mDataFinePeri;
		this.mProgPeriPres = aModel.mProgPeriPres;
		this.mIdPren = aModel.mIdPren;
		this.mCodiUffiSies = aModel.mCodiUffiSies;
		this.mDescrUffiSies = aModel.mDescrUffiSies;
		this.mAnnoFascSiep = aModel.mAnnoFascSiep;
		this.mNumeFascSiep = aModel.mNumeFascSiep;
		this.mCodiSedeInst = aModel.mCodiSedeInst;
		this.mDescriSedeInst = aModel.mDescriSedeInst;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mCodStatPrenPeri = aModel.mCodStatPrenPeri;
		this.mDescrStatPrenPeri = aModel.mDescrStatPrenPeri;
		// this.mCodTipoPeri = aModel.mCodTipoPeri;
		this.mDescrTipoPeri = aModel.mDescrTipoPeri;
		this.mDataPrenPeri = aModel.mDataPrenPeri;
		this.mDataDaPeri = aModel.mDataDaPeri;
		this.mDataAPeri = aModel.mDataAPeri;
		this.mDescPeri = aModel.mDescPeri;
		this.mDataModiPrenPeri = aModel.mDataModiPrenPeri;

	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SbPeriprenModel(Date aDataInizPeri, Date aDataFinePeri, BigDecimal aProgPeriPres,
			BigDecimal aIdPren, String aCodiUffiSies, String aDescrUffiSies, BigDecimal aAnnoFascSiep,
			BigDecimal aNumeFascSiep, String aCodiSedeInst, String aDescriSedeInst, BigDecimal aAnnoFascBdmc,
			BigDecimal aNumeFascBdmc, String aCodStatPrenPeri, String aDescrStatPrenPeri,
			// String aCodTipoPeri,
			String aDescrTipoPeri, Date aDataPrenPeri, String aDescPeri, Date aDataModiPrenPeri) {
		this.mDataInizPeri = aDataInizPeri;
		this.mDataFinePeri = aDataFinePeri;
		this.mProgPeriPres = aProgPeriPres;
		this.mIdPren = aIdPren;
		this.mCodiUffiSies = aCodiUffiSies;
		this.mDescrUffiSies = aDescrUffiSies;
		this.mAnnoFascSiep = aAnnoFascSiep;
		this.mNumeFascSiep = aNumeFascSiep;
		this.mCodiSedeInst = aCodiSedeInst;
		this.mDescriSedeInst = aDescriSedeInst;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mCodStatPrenPeri = aCodStatPrenPeri;
		this.mDescrStatPrenPeri = aDescrStatPrenPeri;
		// this.mCodTipoPeri = aCodTipoPeri;
		this.mDescrTipoPeri = aDescrTipoPeri;
		this.mDataPrenPeri = aDataPrenPeri;
		this.mDataDaPeri = null;
		this.mDataAPeri = null;
		this.mDescPeri = aDescPeri;
		this.mDataModiPrenPeri = aDataModiPrenPeri;

	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public Date getDataInizPeri() {
		return mDataInizPeri;
	}

	public Date getDataFinePeri() {
		return mDataFinePeri;
	}

	public BigDecimal getProgPeriPres() {
		return mProgPeriPres;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public String getCodiUffiSies() {
		return mCodiUffiSies;
	}

	public String getDescrUffiSies() {
		return mDescrUffiSies;
	}

	public BigDecimal getAnnoFascSiep() {
		return mAnnoFascSiep;
	}

	public BigDecimal getNumeFascSiep() {
		return mNumeFascSiep;
	}

	public String getCodiSedeInst() {
		return mCodiSedeInst;
	}

	public String getDescriSedeInst() {
		return mDescriSedeInst;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public BigDecimal getNumeFascBdmc() {
		return mNumeFascBdmc;
	}

	public String getCodStatPrenPeri() {
		return mCodStatPrenPeri;
	}

	public String getDescrStatPrenPeri() {
		return mDescrStatPrenPeri;
	}

	// public String getCodTipoPeri() { return mCodTipoPeri; }
	public String getDescrTipoPeri() {
		return mDescrTipoPeri;
	}

	public Date getDataPrenPeri() {
		return mDataPrenPeri;
	}

	public Date getDataDaPeri() {
		return mDataDaPeri;
	}

	public Date getDataAPeri() {
		return mDataAPeri;
	}

	public String getDescPeri() {
		return mDescPeri;
	}

	public Date getDataModiPrenPeri() {
		return mDataModiPrenPeri;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setDataInizPeri(Date aValore) {
		mDataInizPeri = aValore;
	}

	public void setDataFinePeri(Date aValore) {
		mDataFinePeri = aValore;
	}

	public void setProgPeriPres(BigDecimal aValore) {
		mProgPeriPres = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setCodiUffiSies(String aValore) {
		mCodiUffiSies = aValore;
	}

	public void setDescrUffiSies(String aValore) {
		mDescrUffiSies = aValore;
	}

	public void setAnnoFascSiep(BigDecimal aValore) {
		mAnnoFascSiep = aValore;
	}

	public void setNumeFascSiep(BigDecimal aValore) {
		mNumeFascSiep = aValore;
	}

	public void setCodiSedeInst(String aValore) {
		mCodiSedeInst = aValore;
	}

	public void setDescriSedeInst(String aValore) {
		mDescriSedeInst = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		mNumeFascBdmc = aValore;
	}

	public void setCodStatPrenPeri(String aValore) {
		mCodStatPrenPeri = aValore;
	}

	public void setDescrStatPrenPeri(String aValore) {
		mDescrStatPrenPeri = aValore;
	}

	// public void setCodTipoPeri (String aValore ) { mCodTipoPeri = aValore; }
	public void setDescrTipoPeri(String aValore) {
		mDescrTipoPeri = aValore;
	}

	public void setDataPrenPeri(Date aValore) {
		mDataPrenPeri = aValore;
	}

	public void setDataDaPeri(Date aValore) {
		mDataDaPeri = aValore;
	}

	public void setDataAPeri(Date aValore) {
		mDataAPeri = aValore;
	}

	public void setDescPeri(String aValore) {
		mDescPeri = aValore;
	}

	public void setDataModiPrenPeri(Date aValore) {
		mDataModiPrenPeri = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SbPeriprenModel:\n" + "[ mDataInizPeri    = " + mDataInizPeri + " ]\n"
				+ "[ mDataFinePeri    = " + mDataFinePeri + " ]\n" + "[ mProgPeriPres    = " + mProgPeriPres
				+ " ]\n" + "[ mIdPren          = " + mIdPren + " ]\n" + "[ mCodUffiSies     = "
				+ mCodiUffiSies + " ]\n" + "[ mAnnoFascSiep    = " + mAnnoFascSiep + " ]\n"
				+ "[ mNumeFascSiep    = " + mNumeFascSiep + " ]\n" + "[ mCodiSedeInst    = " + mCodiSedeInst
				+ " ]\n" + "[ mAnnoFascBdmc    = " + mAnnoFascBdmc + " ]\n" + "[ mNumeFascBdmc    = "
				+ mNumeFascBdmc + " ]\n" + "[ mCodStatPrenPeri = " + mCodStatPrenPeri + " ]\n" +
				// "[ mCodTipoPeri = "+mCodTipoPeri+" ]\n"+
				"[ mDataPrenPeri    = " + mDataPrenPeri + " ]\n" + "[ mDataDaPeri      = " + mDataDaPeri
				+ " ]\n" + "[ mDataAPeri       = " + mDataAPeri + " ]\n" + "[ mDescPeri     = " + mDescPeri
				+ " ]\n" + "[ mDataModiPrenPeri    = " + mDataModiPrenPeri + " ]";
		return lStr;
	}

}