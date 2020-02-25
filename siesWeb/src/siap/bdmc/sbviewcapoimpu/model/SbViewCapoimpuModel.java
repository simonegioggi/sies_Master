package siap.bdmc.sbviewcapoimpu.model;

/**
* <p>Title: SbViewCapoimpuModel</p>
* <p>Description: Classe Model che rappresenta il SbViewCapoimpu</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.model.GenericModel;

@SuppressWarnings("rawtypes")
public class SbViewCapoimpuModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2119086994174288780L;

	private String mFlagArti0056;
	private String mFlagArti0061;
	private String mArti0061Comm;
	private String mFlagArti0081;
	private String mArti0081Comm;
	private String mFlagArt0110;
	private String mFlagArti0112;
	private String mArti0112Commi;
	private String mFlagArti0113;
	private String mFlagArti0114;
	private String mFlagArti0116;
	private String mFlagArti0117;
	private String mLuogReat;
	private String mFlagPeriTemp;
	private Date mDataReat0101;
	private Date mDataReat0202;
	private String mDescPeriTemp;
	private BigDecimal mNumeProgCapoImpu;
	private BigDecimal mIdPren;
	private BigDecimal mAnnoFascBdmc;
	private BigDecimal mNumeFascBdmc;
	private String mCodiSedeInst;
	private String mDescriSedeInst;
	private Vector mSbViewReat;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SbViewCapoimpuModel() {
		this.mFlagArti0056 = "";
		this.mFlagArti0061 = "";
		this.mArti0061Comm = "";
		this.mFlagArti0081 = "";
		this.mArti0081Comm = "";
		this.mFlagArt0110 = "";
		this.mFlagArti0112 = "";
		this.mArti0112Commi = "";
		this.mFlagArti0113 = "";
		this.mFlagArti0114 = "";
		this.mFlagArti0116 = "";
		this.mFlagArti0117 = "";
		this.mLuogReat = "";
		this.mFlagPeriTemp = "";
		this.mDataReat0101 = null;
		this.mDataReat0202 = null;
		this.mDescPeriTemp = "";
		this.mNumeProgCapoImpu = null;
		this.mIdPren = null;
		this.mAnnoFascBdmc = null;
		this.mNumeFascBdmc = null;
		this.mCodiSedeInst = "";
		this.mDescriSedeInst = "";
		this.mSbViewReat = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SbViewCapoimpuModel(SbViewCapoimpuModel aModel) {
		this.mFlagArti0056 = aModel.mFlagArti0056;
		this.mFlagArti0061 = aModel.mFlagArti0061;
		this.mArti0061Comm = aModel.mArti0061Comm;
		this.mFlagArti0081 = aModel.mFlagArti0081;
		this.mArti0081Comm = aModel.mArti0081Comm;
		this.mFlagArt0110 = aModel.mFlagArt0110;
		this.mFlagArti0112 = aModel.mFlagArti0112;
		this.mArti0112Commi = aModel.mArti0112Commi;
		this.mFlagArti0113 = aModel.mFlagArti0113;
		this.mFlagArti0114 = aModel.mFlagArti0114;
		this.mFlagArti0116 = aModel.mFlagArti0116;
		this.mFlagArti0117 = aModel.mFlagArti0117;
		this.mLuogReat = aModel.mLuogReat;
		this.mFlagPeriTemp = aModel.mFlagPeriTemp;
		this.mDataReat0101 = aModel.mDataReat0101;
		this.mDataReat0202 = aModel.mDataReat0202;
		this.mDescPeriTemp = aModel.mDescPeriTemp;
		this.mNumeProgCapoImpu = aModel.mNumeProgCapoImpu;
		this.mIdPren = aModel.mIdPren;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mNumeFascBdmc = aModel.mNumeFascBdmc;
		this.mCodiSedeInst = aModel.mCodiSedeInst;
		this.mDescriSedeInst = aModel.mDescriSedeInst;
		this.mSbViewReat = aModel.mSbViewReat;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SbViewCapoimpuModel(String aFlagArti0056, String aFlagArti0061, String aArti0061Comm,
			String aFlagArti0081, String aArti0081Comm, String aFlagArt0110, String aFlagArti0112,
			String aArti0112Commi, String aFlagArti0113, String aFlagArti0114, String aFlagArti0116,
			String aFlagArti0117, String aLuogReat, String aFlagPeriTemp, Date aDataReat0101,
			Date aDataReat0202, String aDescPeriTemp, BigDecimal aNumeProgCapoImpu, BigDecimal aIdPren,
			BigDecimal aAnnoFascBdmc, BigDecimal aNumeFascBdmc, String aCodiSedeInst,
			String aDescriSedeInst) {
		this.mFlagArti0056 = aFlagArti0056;
		this.mFlagArti0061 = aFlagArti0061;
		this.mArti0061Comm = aArti0061Comm;
		this.mFlagArti0081 = aFlagArti0081;
		this.mArti0081Comm = aArti0081Comm;
		this.mFlagArt0110 = aFlagArt0110;
		this.mFlagArti0112 = aFlagArti0112;
		this.mArti0112Commi = aArti0112Commi;
		this.mFlagArti0113 = aFlagArti0113;
		this.mFlagArti0114 = aFlagArti0114;
		this.mFlagArti0116 = aFlagArti0116;
		this.mFlagArti0117 = aFlagArti0117;
		this.mLuogReat = aLuogReat;
		this.mFlagPeriTemp = aFlagPeriTemp;
		this.mDataReat0101 = aDataReat0101;
		this.mDataReat0202 = aDataReat0202;
		this.mDescPeriTemp = aDescPeriTemp;
		this.mNumeProgCapoImpu = aNumeProgCapoImpu;
		this.mIdPren = aIdPren;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mNumeFascBdmc = aNumeFascBdmc;
		this.mCodiSedeInst = aCodiSedeInst;
		this.mDescriSedeInst = aDescriSedeInst;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public String getFlagArti0056() {
		return mFlagArti0056;
	}

	public String getFlagArti0061() {
		return mFlagArti0061;
	}

	public String getArti0061Comm() {
		return mArti0061Comm;
	}

	public String getFlagArti0081() {
		return mFlagArti0081;
	}

	public String getArti0081Comm() {
		return mArti0081Comm;
	}

	public String getFlagArt0110() {
		return mFlagArt0110;
	}

	public String getFlagArti0112() {
		return mFlagArti0112;
	}

	public String getArti0112Commi() {
		return mArti0112Commi;
	}

	public String getFlagArti0113() {
		return mFlagArti0113;
	}

	public String getFlagArti0114() {
		return mFlagArti0114;
	}

	public String getFlagArti0116() {
		return mFlagArti0116;
	}

	public String getFlagArti0117() {
		return mFlagArti0117;
	}

	public String getLuogReat() {
		return mLuogReat;
	}

	public String getFlagPeriTemp() {
		return mFlagPeriTemp;
	}

	public Date getDataReat0101() {
		return mDataReat0101;
	}

	public Date getDataReat0202() {
		return mDataReat0202;
	}

	public String getDescPeriTemp() {
		return mDescPeriTemp;
	}

	public BigDecimal getNumeProgCapoImpu() {
		return mNumeProgCapoImpu;
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

	public Vector getSbViewReat() {
		return mSbViewReat;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setFlagArti0056(String aValore) {
		mFlagArti0056 = aValore;
	}

	public void setFlagArti0061(String aValore) {
		mFlagArti0061 = aValore;
	}

	public void setArti0061Comm(String aValore) {
		mArti0061Comm = aValore;
	}

	public void setFlagArti0081(String aValore) {
		mFlagArti0081 = aValore;
	}

	public void setArti0081Comm(String aValore) {
		mArti0081Comm = aValore;
	}

	public void setFlagArt0110(String aValore) {
		mFlagArt0110 = aValore;
	}

	public void setFlagArti0112(String aValore) {
		mFlagArti0112 = aValore;
	}

	public void setArti0112Commi(String aValore) {
		mArti0112Commi = aValore;
	}

	public void setFlagArti0113(String aValore) {
		mFlagArti0113 = aValore;
	}

	public void setFlagArti0114(String aValore) {
		mFlagArti0114 = aValore;
	}

	public void setFlagArti0116(String aValore) {
		mFlagArti0116 = aValore;
	}

	public void setFlagArti0117(String aValore) {
		mFlagArti0117 = aValore;
	}

	public void setLuogReat(String aValore) {
		mLuogReat = aValore;
	}

	public void setFlagPeriTemp(String aValore) {
		mFlagPeriTemp = aValore;
	}

	public void setDataReat0101(Date aValore) {
		mDataReat0101 = aValore;
	}

	public void setDataReat0202(Date aValore) {
		mDataReat0202 = aValore;
	}

	public void setDescPeriTemp(String aValore) {
		mDescPeriTemp = aValore;
	}

	public void setNumeProgCapoImpu(BigDecimal aValore) {
		mNumeProgCapoImpu = aValore;
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

	public void setSbViewReat(Vector aValore) {
		mSbViewReat = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SbViewCapoimpuModel:\n" + "[ mFlagArti0056     = " + mFlagArti0056 + " ]\n"
				+ "[ mFlagArti0061     = " + mFlagArti0061 + " ]\n" + "[ mArti0061Comm     = " + mArti0061Comm
				+ " ]\n" + "[ mFlagArti0081     = " + mFlagArti0081 + " ]\n" + "[ mArti0081Comm     = "
				+ mArti0081Comm + " ]\n" + "[ mFlagArt0110      = " + mFlagArt0110 + " ]\n"
				+ "[ mFlagArti0112     = " + mFlagArti0112 + " ]\n" + "[ mArti0112Commi    = "
				+ mArti0112Commi + " ]\n" + "[ mFlagArti0113     = " + mFlagArti0113 + " ]\n"
				+ "[ mFlagArti0114     = " + mFlagArti0114 + " ]\n" + "[ mFlagArti0116     = " + mFlagArti0116
				+ " ]\n" + "[ mFlagArti0117     = " + mFlagArti0117 + " ]\n" + "[ mLuogReat         = "
				+ mLuogReat + " ]\n" + "[ mFlagPeriTemp     = " + mFlagPeriTemp + " ]\n"
				+ "[ mDataReat0101     = " + mDataReat0101 + " ]\n" + "[ mDataReat0202     = " + mDataReat0202
				+ " ]\n" + "[ mDescPeriTemp     = " + mDescPeriTemp + " ]\n" + "[ mNumeProgCapoImpu = "
				+ mNumeProgCapoImpu + " ]\n" + "[ mIdPren           = " + mIdPren + " ]\n"
				+ "[ mAnnoFascBdmc     = " + mAnnoFascBdmc + " ]\n" + "[ mNumeFascBdmc     = " + mNumeFascBdmc
				+ " ]\n" + "[ mCodiSedeInst     = " + mCodiSedeInst + " ]";
		return lStr;
	}

}