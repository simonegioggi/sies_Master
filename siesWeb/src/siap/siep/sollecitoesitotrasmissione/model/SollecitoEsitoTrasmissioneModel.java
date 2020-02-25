package siap.siep.sollecitoesitotrasmissione.model;

/**
* <p>Title: SollecitoEsitoTrasmissioneModel</p>
* <p>Description: Classe Model che rappresenta il SollecitoEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SollecitoEsitoTrasmissioneModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5821842912737383447L;

	private BigDecimal mIdSollecito;
	private String mCodUffSollecitato;
	private String mDescrUffSollecitato;
	private String mOggettoMsSollecito;
	private String mOggettoMsSollecitato;
	private Date mDataInvioMsSollecitato;
	private BigDecimal mMesIdMessaggioSollecitato;
	private String mCodUffInoltrante;
	private String mDescrUffInoltrante;
	private Date mDataInoltro;
	private BigDecimal mMesIdMessaggioInoltro;
	private BigDecimal mEveIdEvento;
	private BigDecimal mFasSieIdFascicoloSiep;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SollecitoEsitoTrasmissioneModel() {
		this.mIdSollecito = null;
		this.mCodUffSollecitato = "";
		this.mDescrUffSollecitato = "";
		this.mOggettoMsSollecito = "";
		this.mOggettoMsSollecitato = "";
		this.mDataInvioMsSollecitato = null;
		this.mMesIdMessaggioSollecitato = null;
		this.mCodUffInoltrante = "";
		this.mDescrUffInoltrante = "";
		this.mDataInoltro = null;
		this.mMesIdMessaggioInoltro = null;
		this.mEveIdEvento = null;
		this.mFasSieIdFascicoloSiep = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SollecitoEsitoTrasmissioneModel(SollecitoEsitoTrasmissioneModel aModel) {
		this.mIdSollecito = aModel.mIdSollecito;
		this.mCodUffSollecitato = aModel.mCodUffSollecitato;
		this.mDescrUffSollecitato = aModel.mDescrUffSollecitato;
		this.mOggettoMsSollecito = aModel.mOggettoMsSollecito;
		this.mOggettoMsSollecitato = aModel.mOggettoMsSollecitato;
		this.mDataInvioMsSollecitato = aModel.mDataInvioMsSollecitato;
		this.mMesIdMessaggioSollecitato = aModel.mMesIdMessaggioSollecitato;
		this.mCodUffInoltrante = aModel.mCodUffInoltrante;
		this.mDescrUffInoltrante = aModel.mDescrUffInoltrante;
		this.mDataInoltro = aModel.mDataInoltro;
		this.mMesIdMessaggioInoltro = aModel.mMesIdMessaggioInoltro;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mFasSieIdFascicoloSiep = aModel.mFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SollecitoEsitoTrasmissioneModel(BigDecimal aIdSollecito, String aCodUffSollecitato,
			String aDescrUffSollecitato, String aOggettoMsSollecito, String aOggettoMsSollecitato,
			Date aDataInvioMsSollecitato, BigDecimal aMesIdMessaggioSollecitato, String aCodUffInoltrante,
			String aDescrUffInoltrante, Date aDataInoltro, BigDecimal aMesIdMessaggioInoltro,
			BigDecimal aEveIdEvento, BigDecimal aFasSieIdFascicoloSiep, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento,
			String aDescrUfficioAggiornamento) {
		this.mIdSollecito = aIdSollecito;
		this.mCodUffSollecitato = aCodUffSollecitato;
		this.mDescrUffSollecitato = aDescrUffSollecitato;
		this.mOggettoMsSollecito = aOggettoMsSollecito;
		this.mOggettoMsSollecitato = aOggettoMsSollecitato;
		this.mDataInvioMsSollecitato = aDataInvioMsSollecitato;
		this.mMesIdMessaggioSollecitato = aMesIdMessaggioSollecitato;
		this.mCodUffInoltrante = aCodUffInoltrante;
		this.mDescrUffInoltrante = aDescrUffInoltrante;
		this.mDataInoltro = aDataInoltro;
		this.mMesIdMessaggioInoltro = aMesIdMessaggioInoltro;
		this.mEveIdEvento = aEveIdEvento;
		this.mFasSieIdFascicoloSiep = aFasSieIdFascicoloSiep;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdSollecito() {
		return mIdSollecito;
	}

	public String getCodUffSollecitato() {
		return mCodUffSollecitato;
	}

	public String getDescrUffSollecitato() {
		return mDescrUffSollecitato;
	}

	public String getOggettoMsSollecito() {
		return mOggettoMsSollecito;
	}

	public String getOggettoMsSollecitato() {
		return mOggettoMsSollecitato;
	}

	public Date getDataInvioMsSollecitato() {
		return mDataInvioMsSollecitato;
	}

	public BigDecimal getMesIdMessaggioSollecitato() {
		return mMesIdMessaggioSollecitato;
	}

	public String getCodUffInoltrante() {
		return mCodUffInoltrante;
	}

	public String getDescrUffInoltrante() {
		return mDescrUffInoltrante;
	}

	public Date getDataInoltro() {
		return mDataInoltro;
	}

	public BigDecimal getMesIdMessaggioInoltro() {
		return mMesIdMessaggioInoltro;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public BigDecimal getFasSieIdFascicoloSiep() {
		return mFasSieIdFascicoloSiep;
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

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdSollecito(BigDecimal aValore) {
		mIdSollecito = aValore;
	}

	public void setCodUffSollecitato(String aValore) {
		mCodUffSollecitato = aValore;
	}

	public void setDescrUffSollecitato(String aValore) {
		mDescrUffSollecitato = aValore;
	}

	public void setOggettoMsSollecito(String aValore) {
		mOggettoMsSollecito = aValore;
	}

	public void setOggettoMsSollecitato(String aValore) {
		mOggettoMsSollecitato = aValore;
	}

	public void setDataInvioMsSollecitato(Date aValore) {
		mDataInvioMsSollecitato = aValore;
	}

	public void setMesIdMessaggioSollecitato(BigDecimal aValore) {
		mMesIdMessaggioSollecitato = aValore;
	}

	public void setCodUffInoltrante(String aValore) {
		mCodUffInoltrante = aValore;
	}

	public void setDescrUffInoltrante(String aValore) {
		mDescrUffInoltrante = aValore;
	}

	public void setDataInoltro(Date aValore) {
		mDataInoltro = aValore;
	}

	public void setMesIdMessaggioInoltro(BigDecimal aValore) {
		mMesIdMessaggioInoltro = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		mFasSieIdFascicoloSiep = aValore;
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

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SollecitoEsitoTrasmissioneModel:\n" + "[ mIdSollecito               = " + mIdSollecito
				+ " ]\n" + "[ mCodUffSollecitato         = " + mCodUffSollecitato + " ]\n"
				+ "[ mOggettoMsSollecito        = " + mOggettoMsSollecito + " ]\n"
				+ "[ mOggettoMsSollecitato      = " + mOggettoMsSollecitato + " ]\n"
				+ "[ mDataInvioMsSollecitato    = " + mDataInvioMsSollecitato + " ]\n"
				+ "[ mMesIdMessaggioSollecitato = " + mMesIdMessaggioSollecitato + " ]\n"
				+ "[ mCodUffInoltrante          = " + mCodUffInoltrante + " ]\n"
				+ "[ mDataInoltro               = " + mDataInoltro + " ]\n"
				+ "[ mMesIdMessaggioInoltro     = " + mMesIdMessaggioInoltro + " ]\n"
				+ "[ mEveIdEvento               = " + mEveIdEvento + " ]\n"
				+ "[ mFasSieIdFascicoloSiep     = " + mFasSieIdFascicoloSiep + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}