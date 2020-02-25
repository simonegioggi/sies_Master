package siap.bdmc.sbviewnotifiche.model;

/**
* <p>Title: SbViewNotificheModel</p>
* <p>Description: Classe Model che rappresenta il SbViewNotifiche</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SbViewNotificheModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1217501312831509165L;

	private BigDecimal mProgNoti;
	private BigDecimal mIdProvSies;
	private String mCodiNoti;
	private String mDescriNoti;
	private String mDescrizione;
	private Date mDataInviNoti;
	private Date mDataRegiNoti;
	private Date mDataValiNoti;
	private String mNote;
	private String mCodiUffiSies;
	private String mDescriUffiSies;
	private String mCodiUffi;
	private String mDescriUffi;
	private String mFlagStatNoti;
	private Date mDataChiuNoti;
	private String mFlagTras;
	private BigDecimal mStopAnnoFascBdmc;
	private BigDecimal mStopNumeFascBdmc;
	private String mUtenSies;
	private BigDecimal mIdPren;
	private BigDecimal mProgPeri;
	private BigDecimal mModiAnnoFascBdmc;
	private BigDecimal mModiNumeFascBdmc;
	private String mFlagModi;
	private Date mDataIniz;
	private Date mDataFine;
	private Date mDataInizPrec;
	private Date mDataFinePrec;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SbViewNotificheModel() {
		this.mProgNoti = null;
		this.mIdProvSies = null;
		this.mCodiNoti = "";
		this.mDescriNoti = "";
		this.mDescrizione = "";
		this.mDataInviNoti = null;
		this.mDataRegiNoti = null;
		this.mDataValiNoti = null;
		this.mNote = "";
		this.mCodiUffiSies = "";
		this.mDescriUffiSies = "";
		this.mCodiUffi = "";
		this.mDescriUffi = "";
		this.mFlagStatNoti = "";
		this.mDataChiuNoti = null;
		this.mFlagTras = null;
		this.mStopAnnoFascBdmc = null;
		this.mStopNumeFascBdmc = null;
		this.mUtenSies = "";
		this.mIdPren = null;
		this.mProgPeri = null;
		this.mModiAnnoFascBdmc = null;
		this.mModiNumeFascBdmc = null;
		this.mFlagModi = "";
		this.mDataIniz = null;
		this.mDataFine = null;
		this.mDataInizPrec = null;
		this.mDataFinePrec = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SbViewNotificheModel(SbViewNotificheModel aModel) {
		this.mProgNoti = aModel.mProgNoti;
		this.mIdProvSies = aModel.mIdProvSies;
		this.mCodiNoti = aModel.mCodiNoti;
		this.mDescriNoti = aModel.mDescriNoti;
		this.mDescrizione = aModel.mDescrizione;
		this.mDataInviNoti = aModel.mDataInviNoti;
		this.mDataRegiNoti = aModel.mDataRegiNoti;
		this.mDataValiNoti = aModel.mDataValiNoti;
		this.mNote = aModel.mNote;
		this.mCodiUffiSies = aModel.mCodiUffiSies;
		this.mDescriUffiSies = aModel.mDescriUffiSies;
		this.mCodiUffi = aModel.mCodiUffi;
		this.mDescriUffi = aModel.mDescriUffi;
		this.mFlagStatNoti = aModel.mFlagStatNoti;
		this.mDataChiuNoti = aModel.mDataChiuNoti;
		this.mFlagTras = aModel.mFlagTras;
		this.mStopAnnoFascBdmc = aModel.mStopAnnoFascBdmc;
		this.mStopNumeFascBdmc = aModel.mStopNumeFascBdmc;
		this.mUtenSies = aModel.mUtenSies;
		this.mIdPren = aModel.mIdPren;
		this.mProgPeri = aModel.mProgPeri;
		this.mModiAnnoFascBdmc = aModel.mModiAnnoFascBdmc;
		this.mModiNumeFascBdmc = aModel.mModiNumeFascBdmc;
		this.mFlagModi = aModel.mFlagModi;
		this.mDataIniz = aModel.mDataIniz;
		this.mDataFine = aModel.mDataFine;
		this.mDataInizPrec = aModel.mDataInizPrec;
		this.mDataFinePrec = aModel.mDataFinePrec;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SbViewNotificheModel(BigDecimal aProgNoti, String aCodiNoti, String aDescriNoti,
			String aDescrizione, Date aDataInviNoti, Date aDataRegiNoti, Date aDataValiNoti, String aNote,
			String aCodiUffiSies, String aDescriUffiSies, String aCodiUffi, String aDescriUffi,
			String aFlagStatNoti, Date aDataChiuNoti, String aFlagTras, BigDecimal aStopAnnoFascBdmc,
			BigDecimal aStopNumeFascBdmc, String aUtenSies, BigDecimal aIdPren, BigDecimal aProgPeri,
			BigDecimal aModiAnnoFascBdmc, BigDecimal aModiNumeFascBdmc, String aFlagModi, Date aDataIniz,
			Date aDataFine, Date aDataInizPrec, Date aDataFinePrec, BigDecimal aIdProvSies) {
		this.mProgNoti = aProgNoti;
		this.mIdProvSies = aIdProvSies;
		this.mCodiNoti = aCodiNoti;
		this.mDescriNoti = aDescriNoti;
		this.mDescrizione = aDescrizione;
		this.mDataInviNoti = aDataInviNoti;
		this.mDataRegiNoti = aDataRegiNoti;
		this.mDataValiNoti = aDataValiNoti;
		this.mNote = aNote;
		this.mCodiUffiSies = aCodiUffiSies;
		this.mDescriUffiSies = aDescriUffiSies;
		this.mCodiUffi = aCodiUffi;
		this.mDescriUffi = aDescriUffi;
		this.mFlagStatNoti = aFlagStatNoti;
		this.mDataChiuNoti = aDataChiuNoti;
		this.mFlagTras = aFlagTras;
		this.mStopAnnoFascBdmc = aStopAnnoFascBdmc;
		this.mStopNumeFascBdmc = aStopNumeFascBdmc;
		this.mUtenSies = aUtenSies;
		this.mIdPren = aIdPren;
		this.mProgPeri = aProgPeri;
		this.mModiAnnoFascBdmc = aModiAnnoFascBdmc;
		this.mModiNumeFascBdmc = aModiNumeFascBdmc;
		this.mFlagModi = aFlagModi;
		this.mDataIniz = aDataIniz;
		this.mDataFine = aDataFine;
		this.mDataInizPrec = aDataInizPrec;
		this.mDataFinePrec = aDataFinePrec;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getProgNoti() {
		return mProgNoti;
	}

	public BigDecimal getIdProvSies() {
		return mIdProvSies;
	}

	public String getCodiNoti() {
		return mCodiNoti;
	}

	public String getDescriNoti() {
		return mDescriNoti;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public Date getDataInviNoti() {
		return mDataInviNoti;
	}

	public Date getDataRegiNoti() {
		return mDataRegiNoti;
	}

	public Date getDataValiNoti() {
		return mDataValiNoti;
	}

	public String getNote() {
		return mNote;
	}

	public String getCodiUffiSies() {
		return mCodiUffiSies;
	}

	public String getDescriUffiSies() {
		return mDescriUffiSies;
	}

	public String getCodiUffi() {
		return mCodiUffi;
	}

	public String getDescriUffi() {
		return mDescriUffi;
	}

	public String getFlagStatNoti() {
		return mFlagStatNoti;
	}

	public Date getDataChiuNoti() {
		return mDataChiuNoti;
	}

	public String getFlagTras() {
		return mFlagTras;
	}

	public BigDecimal getStopAnnoFascBdmc() {
		return mStopAnnoFascBdmc;
	}

	public BigDecimal getStopNumeFascBdmc() {
		return mStopNumeFascBdmc;
	}

	public String getUtenSies() {
		return mUtenSies;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public BigDecimal getProgPeri() {
		return mProgPeri;
	}

	public BigDecimal getModiAnnoFascBdmc() {
		return mModiAnnoFascBdmc;
	}

	public BigDecimal getModiNumeFascBdmc() {
		return mModiNumeFascBdmc;
	}

	public String getFlagModi() {
		return mFlagModi;
	}

	public Date getDataIniz() {
		return mDataIniz;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public Date getDataInizPrec() {
		return mDataInizPrec;
	}

	public Date getDataFinePrec() {
		return mDataFinePrec;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setProgNoti(BigDecimal aValore) {
		mProgNoti = aValore;
	}

	public void setIdProvSies(BigDecimal aValore) {
		mIdProvSies = aValore;
	}

	public void setCodiNoti(String aValore) {
		mCodiNoti = aValore;
	}

	public void setDescriNoti(String aValore) {
		mDescriNoti = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setDataInviNoti(Date aValore) {
		mDataInviNoti = aValore;
	}

	public void setDataRegiNoti(Date aValore) {
		mDataRegiNoti = aValore;
	}

	public void setDataValiNoti(Date aValore) {
		mDataValiNoti = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	public void setCodiUffiSies(String aValore) {
		mCodiUffiSies = aValore;
	}

	public void setDescriUffiSies(String aValore) {
		mDescriUffiSies = aValore;
	}

	public void setCodiUffi(String aValore) {
		mCodiUffi = aValore;
	}

	public void setDescriUffi(String aValore) {
		mDescriUffi = aValore;
	}

	public void setFlagStatNoti(String aValore) {
		mFlagStatNoti = aValore;
	}

	public void setDataChiuNoti(Date aValore) {
		mDataChiuNoti = aValore;
	}

	public void setFlagTras(String aValore) {
		mFlagTras = aValore;
	}

	public void setStopAnnoFascBdmc(BigDecimal aValore) {
		mStopAnnoFascBdmc = aValore;
	}

	public void setStopNumeFascBdmc(BigDecimal aValore) {
		mStopNumeFascBdmc = aValore;
	}

	public void setUtenSies(String aValore) {
		mUtenSies = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setProgPeri(BigDecimal aValore) {
		mProgPeri = aValore;
	}

	public void setModiAnnoFascBdmc(BigDecimal aValore) {
		mModiAnnoFascBdmc = aValore;
	}

	public void setModiNumeFascBdmc(BigDecimal aValore) {
		mModiNumeFascBdmc = aValore;
	}

	public void setFlagModi(String aValore) {
		mFlagModi = aValore;
	}

	public void setDataIniz(Date aValore) {
		mDataIniz = aValore;
	}

	public void setDataFine(Date aValore) {
		mDataFine = aValore;
	}

	public void setDataInizPrec(Date aValore) {
		mDataInizPrec = aValore;
	}

	public void setDataFinePrec(Date aValore) {
		mDataFinePrec = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SbViewNotificheModel:\n" + "[ mProgNoti         = " + mProgNoti + " ]\n"
				+ "[ mCodiNoti         = " + mCodiNoti + " ]\n" + "[ mDescrizione      = " + mDescrizione
				+ " ]\n" + "[ mDataInviNoti     = " + mDataInviNoti + " ]\n" + "[ mDataRegiNoti     = "
				+ mDataRegiNoti + " ]\n" + "[ mDataValiNoti     = " + mDataValiNoti + " ]\n"
				+ "[ mNote             = " + mNote + " ]\n" + "[ mCodiUffiSies     = " + mCodiUffiSies
				+ " ]\n" + "[ mCodiUffi         = " + mCodiUffi + " ]\n" + "[ mFlagStatNoti     = "
				+ mFlagStatNoti + " ]\n" + "[ mDataChiuNoti     = " + mDataChiuNoti + " ]\n"
				+ "[ mFlagTras         = " + mFlagTras + " ]\n" + "[ mStopAnnoFascBdmc = " + mStopAnnoFascBdmc
				+ " ]\n" + "[ mStopNumeFascBdmc = " + mStopNumeFascBdmc + " ]\n" + "[ mUtenSies         = "
				+ mUtenSies + " ]\n" + "[ mIdPren           = " + mIdPren + " ]\n" + "[ mProgPeri         = "
				+ mProgPeri + " ]\n" + "[ mModiAnnoFascBdmc = " + mModiAnnoFascBdmc + " ]\n"
				+ "[ mModiNumeFascBdmc = " + mModiNumeFascBdmc + " ]\n" + "[ mFlagModi         = " + mFlagModi
				+ " ]\n" + "[ mDataIniz         = " + mDataIniz + " ]\n" + "[ mDataFine         = "
				+ mDataFine + " ]\n" + "[ mDataInizPrec     = " + mDataInizPrec + " ]\n"
				+ "[ mDataFinePrec     = " + mDataFinePrec + " ]";
		return lStr;
	}

}