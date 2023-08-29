package siap.siep.rinnovo.model;

/**
* <p>Title: RinnovoModel</p>
* <p>Description: Classe Model che rappresenta il Rinnovo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class RinnovoModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = 8942635284321106640L;
	private BigDecimal mIdRinnovo;
	private String mCodTipoRinnovo;
	private String mDescrTipoRinnovo;
	private Date mDataRinnovo;
	private String mCodTipoAutoritaRinnovo;
	private String mDescrTipoAutoritaRinnovo;
	private String mCodLuogoRinnovo;
	private String mDescrLuogoRinnovo;
	private String mNote;
	// private Blob mDocBlob;
	private ByteArrayInputStream mDocBlobIn;
	private ByteArrayOutputStream mDocBlobOut;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mNotIdNotifica;
	private BigDecimal mVerIdVerbale;
	private String mFlagDocumentoRegistrato;
	private String mTemIdTemplate;
	private String mNuovoLuogoNotifica;
	// MEV_2023-33
	private String mEsito; 

	// COSTRUTTORE DI DEFAULT
	public RinnovoModel() {
		this.mIdRinnovo = null;
		this.mCodTipoRinnovo = "";
		this.mDescrTipoRinnovo = "";
		this.mDataRinnovo = null;
		this.mCodTipoAutoritaRinnovo = "";
		this.mDescrTipoAutoritaRinnovo = "";
		this.mCodLuogoRinnovo = "";
		this.mDescrLuogoRinnovo = "";
		this.mNote = "";
		// this.mDocBlob = null;
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mNotIdNotifica = null;
		this.mVerIdVerbale = null;
		this.mTemIdTemplate = "";
		this.mFlagDocumentoRegistrato = "";
		this.mNuovoLuogoNotifica = "";
		// MEV_2023-33
		this.mEsito = "";
	}

	// COSTRUTTORE DI COPIA
	public RinnovoModel(RinnovoModel aModel) {
		this.mIdRinnovo = aModel.mIdRinnovo;
		this.mCodTipoRinnovo = aModel.mCodTipoRinnovo;
		this.mDescrTipoRinnovo = aModel.mDescrTipoRinnovo;
		this.mDataRinnovo = aModel.mDataRinnovo;
		this.mCodTipoAutoritaRinnovo = aModel.mCodTipoAutoritaRinnovo;
		this.mDescrTipoAutoritaRinnovo = aModel.mDescrTipoAutoritaRinnovo;
		this.mCodLuogoRinnovo = aModel.mCodLuogoRinnovo;
		this.mDescrLuogoRinnovo = aModel.mDescrLuogoRinnovo;
		this.mNote = aModel.mNote;
		// this.mDocBlob = aModel.mDocBlob;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mNotIdNotifica = aModel.mNotIdNotifica;
		this.mVerIdVerbale = aModel.mVerIdVerbale;
		this.mTemIdTemplate = aModel.mTemIdTemplate;
		this.mFlagDocumentoRegistrato = aModel.mTemIdTemplate;
		this.mNuovoLuogoNotifica = aModel.mNuovoLuogoNotifica;
		// MEV_2023-33
		this.mEsito = aModel.mEsito;
	}

	// COSTRUTTORE MODEL
	public RinnovoModel(BigDecimal aIdRinnovo, String aCodTipoRinnovo, String aDescrTipoRinnovo,
			Date aDataRinnovo, String aCodTipoAutoritaRinnovo, String aDescrTipoAutoritaRinnovo,
			String aCodLuogoRinnovo, String aDescrLuogoRinnovo, String aNote,
			// Blob aDocBlob,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aNotIdNotifica,
			BigDecimal aVerIdVerbale, String aFlagDocumentoRegistrato, String aTemIdTemplate,
			String aNuovoLuogoNotifica
			// MEV_2023-33
			, String aEsito
			) {
		this.mIdRinnovo = aIdRinnovo;
		this.mCodTipoRinnovo = aCodTipoRinnovo;
		this.mDescrTipoRinnovo = aDescrTipoRinnovo;
		this.mDataRinnovo = aDataRinnovo;
		this.mCodTipoAutoritaRinnovo = aCodTipoAutoritaRinnovo;
		this.mDescrTipoAutoritaRinnovo = aDescrTipoAutoritaRinnovo;
		this.mCodLuogoRinnovo = aCodLuogoRinnovo;
		this.mDescrLuogoRinnovo = aDescrLuogoRinnovo;
		this.mNote = aNote;
		// this.mDocBlob = aDocBlob;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mNotIdNotifica = aNotIdNotifica;
		this.mVerIdVerbale = aVerIdVerbale;
		this.mTemIdTemplate = aTemIdTemplate;
		this.mFlagDocumentoRegistrato = aFlagDocumentoRegistrato;
		this.mNuovoLuogoNotifica = aNuovoLuogoNotifica;
		// MEV_2023-33
		this.mEsito = aEsito;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdRinnovo() {
		return mIdRinnovo;
	}

	public String getCodTipoRinnovo() {
		return mCodTipoRinnovo;
	}

	public String getDescrTipoRinnovo() {
		return mDescrTipoRinnovo;
	}

	public Date getDataRinnovo() {
		return mDataRinnovo;
	}

	public String getCodTipoAutoritaRinnovo() {
		return mCodTipoAutoritaRinnovo;
	}

	public String getDescrTipoAutoritaRinnovo() {
		return mDescrTipoAutoritaRinnovo;
	}

	public String getCodLuogoRinnovo() {
		return mCodLuogoRinnovo;
	}

	public String getDescrLuogoRinnovo() {
		return mDescrLuogoRinnovo;
	}

	public String getNote() {
		return mNote;
	}

	// public Blob getDocBlob() { return mDocBlob; }
	public ByteArrayInputStream getDocBlobIn() {
		return mDocBlobIn;
	}

	public ByteArrayOutputStream getDocBlobOut() {
		return mDocBlobOut;
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

	public BigDecimal getNotIdNotifica() {
		return mNotIdNotifica;
	}

	public BigDecimal getVerIdVerbale() {
		return mVerIdVerbale;
	}

	public String getFlagDocumentoRegistrato() {
		return mFlagDocumentoRegistrato;
	}

	public String getTemIdTemplate() {
		return mTemIdTemplate;
	}

	public String getNuovoLuogoNotifica() {
		return mNuovoLuogoNotifica;
	}

	// MEV_2023-33
	public String getEsito() {
		return mEsito;
	}	
	
	//
	// METODI SET()
	//

	public void setIdRinnovo(BigDecimal aValore) {
		mIdRinnovo = aValore;
	}

	public void setCodTipoRinnovo(String aValore) {
		mCodTipoRinnovo = aValore;
	}

	public void setDescrTipoRinnovo(String aValore) {
		mDescrTipoRinnovo = aValore;
	}

	public void setDataRinnovo(Date aValore) {
		mDataRinnovo = aValore;
	}

	public void setCodTipoAutoritaRinnovo(String aValore) {
		mCodTipoAutoritaRinnovo = aValore;
	}

	public void setDescrTipoAutoritaRinnovo(String aValore) {
		mDescrTipoAutoritaRinnovo = aValore;
	}

	public void setCodLuogoRinnovo(String aValore) {
		mCodLuogoRinnovo = aValore;
	}

	public void setDescrLuogoRinnovo(String aValore) {
		mDescrLuogoRinnovo = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
	}

	// public void setDocBlob(Blob aValore ) { mDocBlob = aValore; }
	public void setDocBlobIn(ByteArrayInputStream aValore) {
		mDocBlobIn = aValore;
	}

	public void setDocBlobOut(ByteArrayOutputStream aValore) {
		mDocBlobOut = aValore;
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

	public void setNotIdNotifica(BigDecimal aValore) {
		mNotIdNotifica = aValore;
	}

	public void setVerIdVerbale(BigDecimal aValore) {
		mVerIdVerbale = aValore;
	}

	public void setFlagDocumentoRegistrato(String aValore) {
		mFlagDocumentoRegistrato = aValore;
	}

	public void setTemIdTemplate(String aValore) {
		mTemIdTemplate = aValore;
	}

	public void setNuovoLuogoNotifica(String aValore) {
		mNuovoLuogoNotifica = aValore;
	}
	
	// MEV_2023-33
	public void setEsito (String aValore) {
		mEsito = aValore;
	}
	

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdRinnovo + " - " + mCodTipoRinnovo + " - " + mDescrTipoRinnovo + " - " + mDataRinnovo
				+ " - " + mCodTipoAutoritaRinnovo + " - " + mDescrTipoAutoritaRinnovo + " - "
				+ mCodLuogoRinnovo + " - " + mDescrLuogoRinnovo + " - " + mNote + " - " +
				// mDocBlob +" - " +
				mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodUfficioInserimento + " - "
				+ mDescrUfficioInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento
				+ " - " + mCodUfficioAggiornamento + " - " + mDescrUfficioAggiornamento + " - "
				+ mNotIdNotifica + " - " + mVerIdVerbale + " - " + mFlagDocumentoRegistrato + " - "
				+ mTemIdTemplate + " - " + mNuovoLuogoNotifica + " - "+ mEsito ;

		return lStr;
	}
}
