package siap.siep.tipologiaorario.model;

/**
* <p>Title: TipologiaOrarioModel</p>
* <p>Description: Classe Model che rappresenta il TipologiaOrario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class TipologiaOrarioModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3118510351860464004L;

	private BigDecimal mIdTipologiaOrario;
	private String mCodNumGiorno;
	private String mDescrNumGiorno;
	private String mDalleOre;
	private String mAlleOre;
	private String mEnteIncaricato;
	private BigDecimal mBenIdBeneficio;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	// MEV26 - CUMULO
	private BigDecimal mDatFinCumUltSanzioni;
	private BigDecimal mBenIdBeneficioCumulo;
	private String mStringaDurata;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public TipologiaOrarioModel() {
		this.mIdTipologiaOrario = null;
		this.mCodNumGiorno = "";
		this.mDescrNumGiorno = "";
		this.mDalleOre = "";
		this.mAlleOre = "";
		this.mEnteIncaricato = "";
		this.mBenIdBeneficio = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mBenIdBeneficioCumulo = null;
		this.mDatFinCumUltSanzioni = null;
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public TipologiaOrarioModel(TipologiaOrarioModel aModel) {
		this.mIdTipologiaOrario = aModel.mIdTipologiaOrario;
		this.mCodNumGiorno = aModel.mCodNumGiorno;
		this.mDescrNumGiorno = aModel.mDescrNumGiorno;
		this.mDalleOre = aModel.mDalleOre;
		this.mAlleOre = aModel.mAlleOre;
		this.mEnteIncaricato = aModel.mEnteIncaricato;
		this.mBenIdBeneficio = aModel.mBenIdBeneficio;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mDatFinCumUltSanzioni = aModel.mDatFinCumUltSanzioni;
		this.mBenIdBeneficioCumulo = aModel.mBenIdBeneficioCumulo;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public TipologiaOrarioModel(BigDecimal aIdTipologiaOrario, String aCodNumGiorno, String aDescrNumGiorno,
			String aDalleOre, String aAlleOre, String aEnteIncaricato, BigDecimal aBenIdBeneficio,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento,
			BigDecimal aDatFinCumUltSanzioni, BigDecimal aBenIdBeneficioCumulo) {
		this.mIdTipologiaOrario = aIdTipologiaOrario;
		this.mCodNumGiorno = aCodNumGiorno;
		this.mDescrNumGiorno = aDescrNumGiorno;
		this.mDalleOre = aDalleOre;
		this.mAlleOre = aAlleOre;
		this.mEnteIncaricato = aEnteIncaricato;
		this.mBenIdBeneficio = aBenIdBeneficio;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mDatFinCumUltSanzioni = aDatFinCumUltSanzioni;
		this.mBenIdBeneficioCumulo = aBenIdBeneficioCumulo;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdTipologiaOrario() {
		return mIdTipologiaOrario;
	}

	public String getCodNumGiorno() {
		return mCodNumGiorno;
	}

	public String getDescrNumGiorno() {
		return mDescrNumGiorno;
	}

	public String getDalleOre() {
		return mDalleOre;
	}

	public String getAlleOre() {
		return mAlleOre;
	}

	public String getEnteIncaricato() {
		return mEnteIncaricato;
	}

	public BigDecimal getBenIdBeneficio() {
		return mBenIdBeneficio;
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

	public BigDecimal getDatFinCumUltSanzioni() {
		return mDatFinCumUltSanzioni;
	}

	public BigDecimal getBenIdBeneficioCumulo() {
		return mBenIdBeneficioCumulo;
	}

	public String getStringaDurata() {
		return mStringaDurata;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdTipologiaOrario(BigDecimal aValore) {
		mIdTipologiaOrario = aValore;
	}

	public void setCodNumGiorno(String aValore) {
		mCodNumGiorno = aValore;
	}

	public void setDescrNumGiorno(String aValore) {
		mDescrNumGiorno = aValore;
	}

	public void setDalleOre(String aValore) {
		mDalleOre = aValore;
	}

	public void setAlleOre(String aValore) {
		mAlleOre = aValore;
	}

	public void setEnteIncaricato(String aValore) {
		mEnteIncaricato = aValore;
	}

	public void setBenIdBeneficio(BigDecimal aValore) {
		mBenIdBeneficio = aValore;
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

	public void setDatFinCumUltSanzioni(BigDecimal aValore) {
		mDatFinCumUltSanzioni = aValore;
	}

	public void setBenIdBeneficioCumulo(BigDecimal aValore) {
		mBenIdBeneficioCumulo = aValore;
	}

	public void setStringaDurata(String aValore) {
		mStringaDurata = aValore;
	}

	public void calcolaStringaDurata() {
		String lStringDurata = "";

		if (mDalleOre != null && mDalleOre.length() > 0)
			lStringDurata = "dalle ore " + mDalleOre;

		if (mAlleOre != null && mAlleOre.length() != 0)
			lStringDurata += " alle ore " + mAlleOre;

		if (lStringDurata.length() > 1) {
			this.mStringaDurata = lStringDurata;
		} else {
			this.mStringaDurata = null;
		}
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "TipologiaOrarioModel:\n" + "[ mIdTipologiaOrario         = " + mIdTipologiaOrario + " ]\n"
				+ "[ mCodNumGiorno              = " + mCodNumGiorno + " ]\n"
				+ "[ mDalleOre                  = " + mDalleOre + " ]\n" + "[ mAlleOre                   = "
				+ mAlleOre + " ]\n" + "[ mEnteIncaricato            = " + mEnteIncaricato + " ]\n"
				+ "[ mBenIdBeneficio            = " + mBenIdBeneficio + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]\n"
				+ "[ mBenIdBeneficioCumulo      = " + mBenIdBeneficioCumulo + " ]\n"
				+ "[ mDatFinCumUltSanzioni      = " + mDatFinCumUltSanzioni + " ]";
		return lStr;
	}

}