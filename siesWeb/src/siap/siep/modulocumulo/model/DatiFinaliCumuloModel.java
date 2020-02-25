package siap.siep.modulocumulo.model;

/**
* <p>Title: DatiFinaliCumuloModel</p>
* <p>Description: Classe Model che rappresenta il DatiFinaliCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class DatiFinaliCumuloModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4561669530047668395L;

	private BigDecimal mIdDatiFinaliCumulo;
	private String mTipoUfficioEmissione;
	private Date mDataProvvedimento;
	private String mCodTipoProvvedimento;
	private String mDescrTipoProvvedimento;
	private BigDecimal mAnnoProvvedimento;
	private BigDecimal mNumeroProvvedimento;
	private String mCodTipoUfficioEmittente;
	private String mDescrTipoUfficioEmittente;
	private String mCodLuogoUfficioEmittente;
	private String mDescrLuogoUfficioEmittente;
	private String mSezioneUfficioEmittente;

	private String mFlagCreaFascicoloMs;
	private BigDecimal mFasSieIdFascicoloSiepMs;

	private BigDecimal mEveIdEvento;
	private BigDecimal mIstrIdIstruttoriaCumulo;

	private String mFlagPrimoCumulo;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public DatiFinaliCumuloModel() {
		this.mIdDatiFinaliCumulo = null;
		this.mTipoUfficioEmissione = "";
		this.mDataProvvedimento = null;
		this.mCodTipoProvvedimento = "";
		this.mDescrTipoProvvedimento = "";
		this.mAnnoProvvedimento = null;
		this.mNumeroProvvedimento = null;
		this.mCodTipoUfficioEmittente = "";
		this.mDescrTipoUfficioEmittente = "";
		this.mCodLuogoUfficioEmittente = "";
		this.mDescrLuogoUfficioEmittente = "";
		this.mSezioneUfficioEmittente = "";
		this.mFlagCreaFascicoloMs = "";
		this.mFasSieIdFascicoloSiepMs = null;
		this.mEveIdEvento = null;
		this.mIstrIdIstruttoriaCumulo = null;

		this.mFlagPrimoCumulo = "";

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public DatiFinaliCumuloModel(DatiFinaliCumuloModel aModel) {
		this.mIdDatiFinaliCumulo = aModel.mIdDatiFinaliCumulo;
		this.mTipoUfficioEmissione = aModel.mTipoUfficioEmissione;
		this.mDataProvvedimento = aModel.mDataProvvedimento;
		this.mCodTipoProvvedimento = aModel.mCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aModel.mDescrTipoProvvedimento;
		this.mAnnoProvvedimento = aModel.mAnnoProvvedimento;
		this.mNumeroProvvedimento = aModel.mNumeroProvvedimento;
		this.mCodTipoUfficioEmittente = aModel.mCodTipoUfficioEmittente;
		this.mDescrTipoUfficioEmittente = aModel.mDescrTipoUfficioEmittente;
		this.mCodLuogoUfficioEmittente = aModel.mCodLuogoUfficioEmittente;
		this.mDescrLuogoUfficioEmittente = aModel.mDescrLuogoUfficioEmittente;
		this.mSezioneUfficioEmittente = aModel.mSezioneUfficioEmittente;
		this.mFlagCreaFascicoloMs = aModel.mFlagCreaFascicoloMs;
		this.mFasSieIdFascicoloSiepMs = aModel.mFasSieIdFascicoloSiepMs;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;

		this.mFlagPrimoCumulo = aModel.mFlagPrimoCumulo;

		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public DatiFinaliCumuloModel(BigDecimal aIdDatiFinaliCumulo, String aTipoUfficioEmissione,
			Date aDataProvvedimento, String aCodTipoProvvedimento, String aDescrTipoProvvedimento,
			BigDecimal aAnnoProvvedimento, BigDecimal aNumeroProvvedimento, String aCodTipoUfficioEmittente,
			String aDescrTipoUfficioEmittente, String aCodLuogoUfficioEmittente,
			String aDescrLuogoUfficioEmittente, String aSezioneUfficioEmittente, String aFlagCreaFascicoloMs,
			BigDecimal aFasSieIdFascicoloSiepMs, BigDecimal aEveIdEvento, BigDecimal aIstrIdIstruttoriaCumulo,
			String aFlagPrimoCumulo, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento) {
		this.mIdDatiFinaliCumulo = aIdDatiFinaliCumulo;
		this.mTipoUfficioEmissione = aTipoUfficioEmissione;
		this.mDataProvvedimento = aDataProvvedimento;
		this.mCodTipoProvvedimento = aCodTipoProvvedimento;
		this.mDescrTipoProvvedimento = aDescrTipoProvvedimento;
		this.mAnnoProvvedimento = aAnnoProvvedimento;
		this.mNumeroProvvedimento = aNumeroProvvedimento;
		this.mCodTipoUfficioEmittente = aCodTipoUfficioEmittente;
		this.mDescrTipoUfficioEmittente = aDescrTipoUfficioEmittente;
		this.mCodLuogoUfficioEmittente = aCodLuogoUfficioEmittente;
		this.mDescrLuogoUfficioEmittente = aDescrLuogoUfficioEmittente;
		this.mSezioneUfficioEmittente = aSezioneUfficioEmittente;
		this.mFlagCreaFascicoloMs = aFlagCreaFascicoloMs;
		this.mFasSieIdFascicoloSiepMs = aFasSieIdFascicoloSiepMs;

		this.mEveIdEvento = aEveIdEvento;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;

		this.mFlagPrimoCumulo = aFlagPrimoCumulo;

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
	public BigDecimal getIdDatiFinaliCumulo() {
		return mIdDatiFinaliCumulo;
	}

	public String getTipoUfficioEmissione() {
		return mTipoUfficioEmissione;
	}

	public Date getDataProvvedimento() {
		return mDataProvvedimento;
	}

	public String getCodTipoProvvedimento() {
		return mCodTipoProvvedimento;
	}

	public String getDescrTipoProvvedimento() {
		return mDescrTipoProvvedimento;
	}

	public BigDecimal getAnnoProvvedimento() {
		return mAnnoProvvedimento;
	}

	public BigDecimal getNumeroProvvedimento() {
		return mNumeroProvvedimento;
	}

	public String getCodTipoUfficioEmittente() {
		return mCodTipoUfficioEmittente;
	}

	public String getDescrTipoUfficioEmittente() {
		return mDescrTipoUfficioEmittente;
	}

	public String getCodLuogoUfficioEmittente() {
		return mCodLuogoUfficioEmittente;
	}

	public String getDescrLuogoUfficioEmittente() {
		return mDescrLuogoUfficioEmittente;
	}

	public String getSezioneUfficioEmittente() {
		return mSezioneUfficioEmittente;
	}

	public String getFlagCreaFascicoloMs() {
		return mFlagCreaFascicoloMs;
	}

	public BigDecimal getFasSieIdFascicoloSiepMs() {
		return mFasSieIdFascicoloSiepMs;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public String getFlagPrimoCumulo() {
		return mFlagPrimoCumulo;
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

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdDatiFinaliCumulo(BigDecimal aValore) {
		mIdDatiFinaliCumulo = aValore;
	}

	public void setTipoUfficioEmissione(String aValore) {
		mTipoUfficioEmissione = aValore;
	}

	public void setDataProvvedimento(Date aValore) {
		mDataProvvedimento = aValore;
	}

	public void setCodTipoProvvedimento(String aValore) {
		mCodTipoProvvedimento = aValore;
	}

	public void setDescrTipoProvvedimento(String aValore) {
		mDescrTipoProvvedimento = aValore;
	}

	public void setAnnoProvvedimento(BigDecimal aValore) {
		mAnnoProvvedimento = aValore;
	}

	public void setNumeroProvvedimento(BigDecimal aValore) {
		mNumeroProvvedimento = aValore;
	}

	public void setCodTipoUfficioEmittente(String aValore) {
		mCodTipoUfficioEmittente = aValore;
	}

	public void setDescrTipoUfficioEmittente(String aValore) {
		mDescrTipoUfficioEmittente = aValore;
	}

	public void setCodLuogoUfficioEmittente(String aValore) {
		mCodLuogoUfficioEmittente = aValore;
	}

	public void setDescrLuogoUfficioEmittente(String aValore) {
		mDescrLuogoUfficioEmittente = aValore;
	}

	public void setSezioneUfficioEmittente(String aValore) {
		mSezioneUfficioEmittente = aValore;
	}

	public void setFlagCreaFascicoloMs(String aValore) {
		mFlagCreaFascicoloMs = aValore;
	}

	public void setFasSieIdFascicoloSiepMs(BigDecimal aValore) {
		mFasSieIdFascicoloSiepMs = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setFlagPrimoCumulo(String aValore) {
		mFlagPrimoCumulo = aValore;
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

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "DatiFinaliCumuloModel:\n" + "[ mIdDatiFinaliCumulo        = " + mIdDatiFinaliCumulo + " ]\n"
				+ "[ mTipoUfficioEmissione      = " + mTipoUfficioEmissione + " ]\n"
				+ "[ mDataProvvedimento         = " + mDataProvvedimento + " ]\n"
				+ "[ mCodTipoProvvedimento      = " + mCodTipoProvvedimento + " ]\n"
				+ "[ mDescrTipoProvvedimento    = " + mDescrTipoProvvedimento + " ]\n"
				+ "[ mAnnoProvvedimento         = " + mAnnoProvvedimento + " ]\n"
				+ "[ mNumeroProvvedimento       = " + mNumeroProvvedimento + " ]\n"
				+ "[ mCodTipoUfficioEmittente   = " + mCodTipoUfficioEmittente + " ]\n"
				+ "[ mDescrTipoUfficioEmittente = " + mDescrTipoUfficioEmittente + " ]\n"
				+ "[ mCodLuogoUfficioEmittente  = " + mCodLuogoUfficioEmittente + " ]\n"
				+ "[ mDescrLuogoUfficioEmittente= " + mDescrLuogoUfficioEmittente + " ]\n"
				+ "[ mFlagCreaFascicoloMs       = " + mFlagCreaFascicoloMs + " ]\n"
				+ "[ mFasSieIdFascicoloSiepMs   = " + mFasSieIdFascicoloSiepMs + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n"
				+ "[ mFlagPrimoCumulo		     = " + mFlagPrimoCumulo + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}