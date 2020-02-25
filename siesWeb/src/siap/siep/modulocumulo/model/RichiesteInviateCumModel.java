package siap.siep.modulocumulo.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

/**
* <p>Title: RichiesteInviateCumModel</p>
* <p>Description: Classe Model che rappresenta il Richieste_Inviate_Cum</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: InterSistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.util.Date;
import java.util.List;

import f3b.model.GenericModel;

public class RichiesteInviateCumModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -8222767961817245373L;
	private BigDecimal mIdRichiesteInviateCum;
	private Date mDataEmissione;
	private Date mDataTrasmissione;
	private String mCodMagistrato;
	private String mContenuto;
	private String mCodUfficioDest;
	private String mDescrUfficioDest;
	private String mCodLuogoDest;
	private String mDescrLuogoDest;
	private BigDecimal mIstrIdIstruttoriaCumulo;

	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private String mFlagDocValidato;

	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;

	private List<RichiestePmInCumuloModel> mListaRichiestePMinCumulo;

	/*****************************************************************************
	 * Costruttore di default che inizializza i campi del model I campi String vengono inizializzati a "",
	 * tutti gli altri campi a null
	 ****************************************************************************/
	public RichiesteInviateCumModel() {
		this.mIdRichiesteInviateCum = null;
		this.mDataEmissione = null;
		this.mDataTrasmissione = null;
		this.mCodMagistrato = "";
		this.mContenuto = "";
		this.mCodUfficioDest = "";
		this.mDescrUfficioDest = "";
		this.mCodLuogoDest = "";
		this.mDescrLuogoDest = "";
		this.mIstrIdIstruttoriaCumulo = null;

		this.mFlagDocValidato = "";

		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";

		this.mListaRichiestePMinCumulo = null;

	}

	/*****************************************************************************
	 * COSTRUTTORE DI COPIA che istanzia un nuovo model caricandolo con il contenuto del model passato in
	 * input
	 * 
	 * @param aModel
	 ****************************************************************************/
	public RichiesteInviateCumModel(RichiesteInviateCumModel aModel) {
		this.mIdRichiesteInviateCum = aModel.mIdRichiesteInviateCum;
		this.mDataEmissione = aModel.mDataEmissione;
		this.mDataTrasmissione = aModel.mDataTrasmissione;
		this.mCodMagistrato = aModel.mCodMagistrato;
		this.mContenuto = aModel.mContenuto;
		this.mCodUfficioDest = aModel.mCodUfficioDest;
		this.mDescrUfficioDest = aModel.mDescrUfficioDest;
		this.mCodLuogoDest = aModel.mCodLuogoDest;
		this.mDescrLuogoDest = aModel.mDescrLuogoDest;
		this.mIstrIdIstruttoriaCumulo = aModel.mIstrIdIstruttoriaCumulo;

		this.mFlagDocValidato = aModel.mFlagDocValidato;

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
	public RichiesteInviateCumModel(BigDecimal aIdRichiesteInviateCum, Date aDataEmissione,
			Date aDataTrasmissione, String aCodMagistrato, String aContenuto, String aCodUfficioDest,
			String aDescrUfficioDest, String aCodLuogoDest, String aDescrLuogoDest,
			BigDecimal aIstrIdIstruttoriaCumulo,

			String aFlagDocValidato,

			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento

	) {
		this.mIdRichiesteInviateCum = aIdRichiesteInviateCum;
		this.mDataEmissione = aDataEmissione;
		this.mDataTrasmissione = aDataTrasmissione;
		this.mCodMagistrato = aCodMagistrato;
		this.mContenuto = aContenuto;
		this.mCodUfficioDest = aCodUfficioDest;
		this.mDescrUfficioDest = aDescrUfficioDest;
		this.mCodLuogoDest = aCodLuogoDest;
		this.mDescrLuogoDest = aDescrLuogoDest;
		this.mIstrIdIstruttoriaCumulo = aIstrIdIstruttoriaCumulo;

		this.mFlagDocValidato = aFlagDocValidato;

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
	public BigDecimal getIdRichiesteInviateCum() {
		return mIdRichiesteInviateCum;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public Date getDataTrasmissione() {
		return mDataTrasmissione;
	}

	public String getCodMagistrato() {
		return mCodMagistrato;
	}

	public String getContenuto() {
		return mContenuto;
	}

	public String getCodUfficioDest() {
		return mCodUfficioDest;
	}

	public String getDescrUfficioDest() {
		return mDescrUfficioDest;
	}

	public String getCodLuogoDest() {
		return mCodLuogoDest;
	}

	public String getDescrLuogoDest() {
		return mDescrLuogoDest;
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() {
		return mIstrIdIstruttoriaCumulo;
	}

	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
	}

	public String getFlagDocValidato() {
		return mFlagDocValidato;
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

	public List<RichiestePmInCumuloModel> getListaRichiestePMinCumulo() {
		return mListaRichiestePMinCumulo;
	}

	// ============================================================================
	// METODI SET()
	// ============================================================================
	public void setIdRichiesteInviateCum(BigDecimal aValore) {
		mIdRichiesteInviateCum = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

	public void setDataTrasmissione(Date aValore) {
		mDataTrasmissione = aValore;
	}

	public void setCodMagistrato(String aValore) {
		mCodMagistrato = aValore;
	}

	public void setContenuto(String aValore) {
		mContenuto = aValore;
	}

	public void setCodUfficioDest(String aValore) {
		mCodUfficioDest = aValore;
	}

	public void setDescrUfficioDest(String aValore) {
		mDescrUfficioDest = aValore;
	}

	public void setCodLuogoDest(String aValore) {
		mCodLuogoDest = aValore;
	}

	public void setDescrLuogoDest(String aValore) {
		mDescrLuogoDest = aValore;
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		mIstrIdIstruttoriaCumulo = aValore;
	}

	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
	}

	public void setFlagDocValidato(String aValore) {
		mFlagDocValidato = aValore;
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

	public void setListaRichiestePMinCumulo(List<RichiestePmInCumuloModel> aValore) {
		mListaRichiestePMinCumulo = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	@Override
	public String toString() {
		String lStr = new String();

		lStr = "RichiesteInviateCumModel:\n" + "[ mIdRichiesteInviateCum     = " + mIdRichiesteInviateCum
				+ " ]\n" + "[ mDataEmissione             = " + mDataEmissione + " ]\n"
				+ "[ mDataTrasmissione          = " + mDataTrasmissione + " ]\n"
				+ "[ mCodMagistrato         	 = " + mCodMagistrato + " ]\n"
				+ "[ mContenuto        		 = " + mContenuto + " ]\n" + "[ mCodUfficioDest		     = "
				+ mCodUfficioDest + " ]\n" + "[ mCodLuogoDest              = " + mCodLuogoDest + " ]\n"
				+ "[ mIstrIdIstruttoriaCumulo   = " + mIstrIdIstruttoriaCumulo + " ]\n" +

				"[ mFlagDocValidato   = " + mFlagDocValidato + " ]\n" +

				"[ mCodOperatoreInserimento   = " + mCodOperatoreInserimento + " ]\n"
				+ "[ mDataInserimento           = " + mDataInserimento + " ]\n"
				+ "[ mCodUfficioInserimento     = " + mCodUfficioInserimento + " ]\n"
				+ "[ mCodOperatoreAggiornamento = " + mCodOperatoreAggiornamento + " ]\n"
				+ "[ mDataAggiornamento         = " + mDataAggiornamento + " ]\n"
				+ "[ mCodUfficioAggiornamento   = " + mCodUfficioAggiornamento + " ]";
		return lStr;
	}
}
