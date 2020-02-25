package siap.sico.soggettocertificato.model;

/**
* <p>Title: SoggettoCertificatoModel</p>
* <p>Description: Classe Model che rappresenta il SoggettoCertificato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class SoggettoCertificatoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4951395779464546848L;

	private BigDecimal mIdSoggettoCertificato;
	private BigDecimal mSogIdSoggetto;
	private Date mDataInserimento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private Date mDataAggiornamento;
	private String mCodOperatoreAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private ByteArrayInputStream mDocBlobCertificato;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public SoggettoCertificatoModel() {
		this.mIdSoggettoCertificato = null;
		this.mSogIdSoggetto = null;
		this.mDataInserimento = null;
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mDataAggiornamento = null;
		this.mCodOperatoreAggiornamento = "";
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public SoggettoCertificatoModel(SoggettoCertificatoModel aModel) {
		this.mIdSoggettoCertificato = aModel.mIdSoggettoCertificato;
		this.mSogIdSoggetto = aModel.mSogIdSoggetto;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
	}

	/*****************************************************************************
	 * Costruttore che istanzia un nuovo Model caricandolo con i dati passati in input. Utilizzato dai DAO.
	 ****************************************************************************/
	public SoggettoCertificatoModel(BigDecimal aIdSoggettoCertificato, BigDecimal aSogIdSoggetto,
			Date aDataInserimento, String aCodOperatoreInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, Date aDataAggiornamento, String aCodOperatoreAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento) {
		this.mIdSoggettoCertificato = aIdSoggettoCertificato;
		this.mSogIdSoggetto = aSogIdSoggetto;
		this.mDataInserimento = aDataInserimento;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
	}

	// ============================================================================
	// METODI GET()
	// ============================================================================
	public BigDecimal getIdSoggettoCertificato() {
		return mIdSoggettoCertificato;
	}

	public BigDecimal getSogIdSoggetto() {
		return mSogIdSoggetto;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public String getCodUfficioInserimento() {
		return mCodUfficioInserimento;
	}

	public String getDescrUfficioInserimento() {
		return mDescrUfficioInserimento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public String getCodUfficioAggiornamento() {
		return mCodUfficioAggiornamento;
	}

	public String getDescrUfficioAggiornamento() {
		return mDescrUfficioAggiornamento;
	}

	public ByteArrayInputStream getDocBlobCertificato() {
		return mDocBlobCertificato;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdSoggettoCertificato(BigDecimal aValore) {
		mIdSoggettoCertificato = aValore;
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		mSogIdSoggetto = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setCodUfficioInserimento(String aValore) {
		mCodUfficioInserimento = aValore;
	}

	public void setDescrUfficioInserimento(String aValore) {
		mDescrUfficioInserimento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setCodUfficioAggiornamento(String aValore) {
		mCodUfficioAggiornamento = aValore;
	}

	public void setDescrUfficioAggiornamento(String aValore) {
		mDescrUfficioAggiornamento = aValore;
	}

	public void setDocBlobCertificato(ByteArrayInputStream aValore) {
		mDocBlobCertificato = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "SoggettoCertificatoModel:\n" + "[ mIdSoggettoCertificato     = " + mIdSoggettoCertificato
				+ " ]\n" + "[ mSogIdSoggetto             = " + mSogIdSoggetto + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}

}