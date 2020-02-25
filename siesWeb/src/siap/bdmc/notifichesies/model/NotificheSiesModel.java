package siap.bdmc.notifichesies.model;

/**
* <p>Title: NotificheSiesModel</p>
* <p>Description: Classe Model che rappresenta il NotificheSies</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class NotificheSiesModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7725989264493411072L;

	private BigDecimal mIdNotificheSies;
	private BigDecimal mAnnoSiep;
	private BigDecimal mProgSiep;
	private String mUfficioSiep;
	private BigDecimal mAnnoFascBdmc;
	private String mUfficioFascBdmc;
	private BigDecimal mNumeroFascBdmc;
	private String mTipoNotifica;
	private Date mDataNotifica;
	private String mStatoTrasmissione;
	private Date mDataTrasmissione;
	private BigDecimal mIdPren;
	private BigDecimal mProgPeriPres;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private BigDecimal mIdEvento;
	private BigDecimal mIdFascicoloBdmc;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public NotificheSiesModel() {
		this.mIdNotificheSies = null;
		this.mAnnoSiep = null;
		this.mProgSiep = null;
		this.mUfficioSiep = "";
		this.mAnnoFascBdmc = null;
		this.mUfficioFascBdmc = "";
		this.mNumeroFascBdmc = null;
		this.mTipoNotifica = "";
		this.mDataNotifica = null;
		this.mStatoTrasmissione = "";
		this.mDataTrasmissione = null;
		this.mIdPren = null;
		this.mProgPeriPres = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mIdEvento = null;
		this.mIdFascicoloBdmc = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public NotificheSiesModel(NotificheSiesModel aModel) {
		this.mIdNotificheSies = aModel.mIdNotificheSies;
		this.mAnnoSiep = aModel.mAnnoSiep;
		this.mProgSiep = aModel.mProgSiep;
		this.mUfficioSiep = aModel.mUfficioSiep;
		this.mAnnoFascBdmc = aModel.mAnnoFascBdmc;
		this.mUfficioFascBdmc = aModel.mUfficioFascBdmc;
		this.mNumeroFascBdmc = aModel.mNumeroFascBdmc;
		this.mTipoNotifica = aModel.mTipoNotifica;
		this.mDataNotifica = aModel.mDataNotifica;
		this.mStatoTrasmissione = aModel.mStatoTrasmissione;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mIdPren = aModel.mIdPren;
		this.mProgPeriPres = aModel.mProgPeriPres;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mIdEvento = aModel.mIdEvento;
		this.mIdFascicoloBdmc = aModel.mIdFascicoloBdmc;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public NotificheSiesModel(BigDecimal aIdNotificheSies, BigDecimal aAnnoSiep, BigDecimal aProgSiep,
			String aUfficioSiep, BigDecimal aAnnoFascBdmc, String aUfficioFascBdmc,
			BigDecimal aNumeroFascBdmc, String aTipoNotifica, Date aDataNotifica, String aStatoTrasmissione,
			Date aDataTrasmissione, BigDecimal aIdPren, BigDecimal aProgPeriPres,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			BigDecimal aIdEvento, BigDecimal aIdFascicoloBdmc) {
		this.mIdNotificheSies = aIdNotificheSies;
		this.mAnnoSiep = aAnnoSiep;
		this.mProgSiep = aProgSiep;
		this.mUfficioSiep = aUfficioSiep;
		this.mAnnoFascBdmc = aAnnoFascBdmc;
		this.mUfficioFascBdmc = aUfficioFascBdmc;
		this.mNumeroFascBdmc = aNumeroFascBdmc;
		this.mTipoNotifica = aTipoNotifica;
		this.mDataNotifica = aDataNotifica;
		this.mStatoTrasmissione = aStatoTrasmissione;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mIdPren = aIdPren;
		this.mProgPeriPres = aProgPeriPres;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mIdEvento = aIdEvento;
		this.mIdFascicoloBdmc = aIdFascicoloBdmc;
		// this.mDescrUfficioInserimento = aDescrUfficioInserimento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdNotificheSies() {
		return mIdNotificheSies;
	}

	public BigDecimal getAnnoSiep() {
		return mAnnoSiep;
	}

	public BigDecimal getProgSiep() {
		return mProgSiep;
	}

	public String getUfficioSiep() {
		return mUfficioSiep;
	}

	public BigDecimal getAnnoFascBdmc() {
		return mAnnoFascBdmc;
	}

	public String getUfficioFascBdmc() {
		return mUfficioFascBdmc;
	}

	public BigDecimal getNumeroFascBdmc() {
		return mNumeroFascBdmc;
	}

	public String getTipoNotifica() {
		return mTipoNotifica;
	}

	public Date getDataNotifica() {
		return mDataNotifica;
	}

	public String getStatoTrasmissione() {
		return mStatoTrasmissione;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public BigDecimal getIdPren() {
		return mIdPren;
	}

	public BigDecimal getProgPeriPres() {
		return mProgPeriPres;
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

	public BigDecimal getIdEvento() {
		return mIdEvento;
	}

	public BigDecimal getIdFascicoloBdmc() {
		return mIdFascicoloBdmc;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdNotificheSies(BigDecimal aValore) {
		mIdNotificheSies = aValore;
	}

	public void setAnnoSiep(BigDecimal aValore) {
		mAnnoSiep = aValore;
	}

	public void setProgSiep(BigDecimal aValore) {
		mProgSiep = aValore;
	}

	public void setUfficioSiep(String aValore) {
		mUfficioSiep = aValore;
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		mAnnoFascBdmc = aValore;
	}

	public void setUfficioFascBdmc(String aValore) {
		mUfficioFascBdmc = aValore;
	}

	public void setNumeroFascBdmc(BigDecimal aValore) {
		mNumeroFascBdmc = aValore;
	}

	public void setTipoNotifica(String aValore) {
		mTipoNotifica = aValore;
	}

	public void setDataNotifica(Date aValore) {
		mDataNotifica = aValore;
	}

	public void setStatoTrasmissione(String aValore) {
		mStatoTrasmissione = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setIdPren(BigDecimal aValore) {
		mIdPren = aValore;
	}

	public void setProgPeriPres(BigDecimal aValore) {
		mProgPeriPres = aValore;
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

	public void setIdEvento(BigDecimal aValore) {
		mIdEvento = aValore;
	}

	public void setIdFascicoloBdmc(BigDecimal aValore) {
		mIdFascicoloBdmc = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "NotificheSiesModel:\n" + "[ mIdNotificheSies         = " + mIdNotificheSies + " ]\n"
				+ "[ mAnnoSiep                = " + mAnnoSiep + " ]\n" + "[ mProgSiep                = "
				+ mProgSiep + " ]\n" + "[ mUfficioSiep             = " + mUfficioSiep + " ]\n"
				+ "[ mAnnoFascBdmc            = " + mAnnoFascBdmc + " ]\n" + "[ mUfficioFascBdmc         = "
				+ mUfficioFascBdmc + " ]\n" + "[ mNumeroFascBdmc          = " + mNumeroFascBdmc + " ]\n"
				+ "[ mTipoNotifica            = " + mTipoNotifica + " ]\n" + "[ mDataNotifica            = "
				+ mDataNotifica + " ]\n" + "[ mStatoTrasmissione       = " + mStatoTrasmissione + " ]\n"
				+ "[ mDataTrasmissione        = " + mDataTrasmissione + " ]\n"
				+ "[ mIdPren                  = " + mIdPren + " ]\n" + "[ mProgPeriPres            = "
				+ mProgPeriPres + " ]\n" + "[ mCodOperatoreInserimento = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento         = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento   = " + mCodUfficioInserimento + " ]\n" + "[ mIdEvento         = "
				+ mIdEvento + " ]\n" + "[ mIdFascicoloBdmc         = " + mIdFascicoloBdmc + " ]";

		return lStr;
	}

}