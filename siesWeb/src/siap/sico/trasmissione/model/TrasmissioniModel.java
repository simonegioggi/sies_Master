package siap.sico.trasmissione.model;

/**
* <p>Title: TrasmissioniModel</p>
* <p>Description: Classe Model che rappresenta il Trasmissioni</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class TrasmissioniModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6666236615049397548L;

	private BigDecimal mIdTrasmissione;
	private String mTipoTrasmissione;
	private Date mDataTrasmissione;
	private String mEsitoTrasmissione;
	private String mCodErrore;
	private String mDescrErrore;
	private String mTipoOperazione;
	private String mDestinazione;
	private BigDecimal mChiaveSiesSogg;
	private BigDecimal mChiaveSiesFasc;
	private BigDecimal mChiaveNscSogg;
	private BigDecimal mChiaveNscProv;
	private BigDecimal mChiaveAnno;
	private BigDecimal mChiaveProgr;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public TrasmissioniModel() {
		this.mIdTrasmissione = null;
		this.mTipoTrasmissione = "";
		this.mDataTrasmissione = null;
		this.mEsitoTrasmissione = "";
		this.mCodErrore = "";
		this.mDescrErrore = "";
		this.mTipoOperazione = "";
		this.mDestinazione = "";
		this.mChiaveSiesSogg = null;
		this.mChiaveSiesFasc = null;
		this.mChiaveNscSogg = null;
		this.mChiaveNscProv = null;
		this.mChiaveAnno = null;
		this.mChiaveProgr = null;
		this.mCodOperatoreInserimento = null;
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = null;

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public TrasmissioniModel(TrasmissioniModel aModel) {
		this.mIdTrasmissione = aModel.mIdTrasmissione;
		this.mTipoTrasmissione = aModel.mTipoTrasmissione;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mEsitoTrasmissione = aModel.mEsitoTrasmissione;
		this.mCodErrore = aModel.mCodErrore;
		this.mDescrErrore = aModel.mDescrErrore;
		this.mTipoOperazione = aModel.mTipoOperazione;
		this.mDestinazione = aModel.mDestinazione;
		this.mChiaveSiesSogg = aModel.mChiaveSiesSogg;
		this.mChiaveSiesFasc = aModel.mChiaveSiesFasc;
		this.mChiaveNscSogg = aModel.mChiaveNscSogg;
		this.mChiaveNscProv = aModel.mChiaveNscProv;
		this.mChiaveAnno = aModel.mChiaveAnno;
		this.mChiaveProgr = aModel.mChiaveProgr;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public TrasmissioniModel(BigDecimal aIdTrasmissione, String aTipoTrasmissione, Date aDataTrasmissione,
			String aEsitoTrasmissione, String aCodErrore, String aDescrErrore, String aTipoOperazione,
			String aDestinazione, BigDecimal aChiaveSiesSogg, BigDecimal aChiaveSiesFasc,
			BigDecimal aChiaveNscSogg, BigDecimal aChiaveNscProv, BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String aCodOperatoreInserimento, Date aDataInserimento,
			String aCodUfficioInserimento) {
		this.mIdTrasmissione = aIdTrasmissione;
		this.mTipoTrasmissione = aTipoTrasmissione;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mEsitoTrasmissione = aEsitoTrasmissione;
		this.mCodErrore = aCodErrore;
		this.mDescrErrore = aDescrErrore;
		this.mTipoOperazione = aTipoOperazione;
		this.mDestinazione = aDestinazione;
		this.mChiaveSiesSogg = aChiaveSiesSogg;
		this.mChiaveSiesFasc = aChiaveSiesFasc;
		this.mChiaveNscSogg = aChiaveNscSogg;
		this.mChiaveNscProv = aChiaveNscProv;
		this.mChiaveAnno = aChiaveAnno;
		this.mChiaveProgr = aChiaveProgr;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdTrasmissione() {
		return mIdTrasmissione;
	}

	public String getTipoTrasmissione() {
		return mTipoTrasmissione;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public String getEsitoTrasmissione() {
		return mEsitoTrasmissione;
	}

	public String getCodErrore() {
		return mCodErrore;
	}

	public String getDescrErrore() {
		return mDescrErrore;
	}

	public String getTipoOperazione() {
		return mTipoOperazione;
	}

	public String getDestinazione() {
		return mDestinazione;
	}

	public BigDecimal getChiaveSiesSogg() {
		return mChiaveSiesSogg;
	}

	public BigDecimal getChiaveSiesFasc() {
		return mChiaveSiesFasc;
	}

	public BigDecimal getChiaveNscSogg() {
		return mChiaveNscSogg;
	}

	public BigDecimal getChiaveNscProv() {
		return mChiaveNscProv;
	}

	public BigDecimal getChiaveAnno() {
		return mChiaveAnno;
	}

	public BigDecimal getChiaveProgr() {
		return mChiaveProgr;
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

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdTrasmissione(BigDecimal aValore) {
		mIdTrasmissione = aValore;
	}

	public void setTipoTrasmissione(String aValore) {
		mTipoTrasmissione = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setEsitoTrasmissione(String aValore) {
		mEsitoTrasmissione = aValore;
	}

	public void setCodErrore(String aValore) {
		mCodErrore = aValore;
	}

	public void setDescrErrore(String aValore) {
		mDescrErrore = aValore;
	}

	public void setTipoOperazione(String aValore) {
		mTipoOperazione = aValore;
	}

	public void setDestinazione(String aValore) {
		mDestinazione = aValore;
	}

	public void setChiaveSiesSogg(BigDecimal aValore) {
		mChiaveSiesSogg = aValore;
	}

	public void setChiaveSiesFasc(BigDecimal aValore) {
		mChiaveSiesFasc = aValore;
	}

	public void setChiaveNscSogg(BigDecimal aValore) {
		mChiaveNscSogg = aValore;
	}

	public void setChiaveNscProv(BigDecimal aValore) {
		mChiaveNscProv = aValore;
	}

	public void setChiaveAnno(BigDecimal aValore) {
		mChiaveAnno = aValore;
	}

	public void setChiaveProgr(BigDecimal aValore) {
		mChiaveProgr = aValore;
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

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "TrasmissioniModel:\n" + "[ mIdTrasmissione   = " + mIdTrasmissione + " ]\n"
				+ "[ mTipoTrasmissione = " + mTipoTrasmissione + " ]\n" + "[ mDataTrasmissione = "
				+ mDataTrasmissione + " ]\n" + "[ mEsitoTrasmissione = " + mEsitoTrasmissione + " ]\n"
				+ "[ mCodErrore        = " + mCodErrore + " ]\n" + "[ mTipoOperazione   = " + mTipoOperazione
				+ " ]\n" + "[ mDestinazione     = " + mDestinazione + " ]\n" + "[ mChiaveSiesSogg   = "
				+ mChiaveSiesSogg + " ]\n" + "[ mChiaveSiesFasc   = " + mChiaveSiesFasc + " ]\n"
				+ "[ mChiaveNscSogg    = " + mChiaveNscSogg + " ]\n" + "[ mChiaveNscProv    = "
				+ mChiaveNscProv + " ]\n" + "[ mChiaveAnno       = " + mChiaveAnno + " ]\n"
				+ "[ mChiaveProgr       = " + mChiaveProgr + " ]\n" + "[ mCodOperatoreInserimento       = "
				+ mCodOperatoreInserimento + " ]\n" + "[ mDataInserimento       = " + mDataInserimento
				+ " ]\n" + "[ mCodUfficioInserimento       = " + mCodUfficioInserimento + " ]";

		return lStr;
	}

}