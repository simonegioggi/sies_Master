package siap.siep.refertoscarcerazione.model;

/**
* <p>Title: RefertoScarcerazioneModel</p>
* <p>Description: Classe Model che rappresenta il RefertoScarcerazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

public class RefertoScarcerazioneModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -4980343991607214804L;
	private BigDecimal mIdRefertoScarcerazione;
	private BigDecimal mAnnoNota;
	private String mNumNota;
	private Date mDataNota;
	private Date mDataScarcerazione;
	private String mNote;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private String mCodUfficioAggiornamento;
	private String mDescrUfficioAggiornamento;
	private BigDecimal mEveIdEvento;
	private String mIstDetIdIstitutoDetenzione;

	private String mDescrTipoIstituto;
	private String mDescrLuogo;
	private String mIndirizzo;

	private IstitutoDetenzioneModel mIstitutoDetenzione;

	// COSTRUTTORE DI DEFAULT
	public RefertoScarcerazioneModel() {
		this.mIdRefertoScarcerazione = null;
		this.mAnnoNota = null;
		this.mNumNota = "";
		this.mDataNota = null;
		this.mDataScarcerazione = null;
		this.mNote = "";
		this.mCodOperatoreInserimento = "";
		this.mDataInserimento = null;
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
		this.mCodOperatoreAggiornamento = "";
		this.mDataAggiornamento = null;
		this.mCodUfficioAggiornamento = "";
		this.mDescrUfficioAggiornamento = "";
		this.mEveIdEvento = null;
		this.mIstDetIdIstitutoDetenzione = "";
		this.mIstitutoDetenzione = null;
		this.mDescrTipoIstituto = "";
		this.mDescrLuogo = "";
		this.mIndirizzo = "";

	}

	// COSTRUTTORE DI COPIA
	public RefertoScarcerazioneModel(RefertoScarcerazioneModel aModel) {
		this.mIdRefertoScarcerazione = aModel.mIdRefertoScarcerazione;
		this.mAnnoNota = aModel.mAnnoNota;
		this.mNumNota = aModel.mNumNota;
		this.mDataNota = aModel.mDataNota;
		this.mDataScarcerazione = aModel.mDataScarcerazione;
		this.mNote = aModel.mNote;
		this.mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		this.mDataInserimento = aModel.mDataInserimento;
		this.mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		this.mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		this.mDataAggiornamento = aModel.mDataAggiornamento;
		this.mCodUfficioAggiornamento = aModel.mCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aModel.mDescrUfficioAggiornamento;
		this.mEveIdEvento = aModel.mEveIdEvento;
		this.mIstDetIdIstitutoDetenzione = aModel.mIstDetIdIstitutoDetenzione;
		this.mIstitutoDetenzione = aModel.mIstitutoDetenzione;
	}

	// COSTRUTTORE MODEL
	public RefertoScarcerazioneModel(BigDecimal aIdRefertoScarcerazione, BigDecimal aAnnoNota,
			String aNumNota, Date aDataNota, Date aDataScarcerazione, String aNote,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodUfficioInserimento,
			String aDescrUfficioInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento,
			String aCodUfficioAggiornamento, String aDescrUfficioAggiornamento, BigDecimal aEveIdEvento,
			String aIstDetIdIstitutoDetenzione, IstitutoDetenzioneModel aIstitutoDetenzione) {
		this.mIdRefertoScarcerazione = aIdRefertoScarcerazione;
		this.mAnnoNota = aAnnoNota;
		this.mNumNota = aNumNota;
		this.mDataNota = aDataNota;
		this.mDataScarcerazione = aDataScarcerazione;
		this.mNote = aNote;
		this.mCodOperatoreInserimento = aCodOperatoreInserimento;
		this.mDataInserimento = aDataInserimento;
		this.mCodUfficioInserimento = aCodUfficioInserimento;
		this.mDescrUfficioInserimento = aDescrUfficioInserimento;
		this.mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		this.mDataAggiornamento = aDataAggiornamento;
		this.mCodUfficioAggiornamento = aCodUfficioAggiornamento;
		this.mDescrUfficioAggiornamento = aDescrUfficioAggiornamento;
		this.mEveIdEvento = aEveIdEvento;
		this.mIstDetIdIstitutoDetenzione = aIstDetIdIstitutoDetenzione;
		this.mIstitutoDetenzione = aIstitutoDetenzione;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdRefertoScarcerazione() {
		return mIdRefertoScarcerazione;
	}

	public BigDecimal getAnnoNota() {
		return mAnnoNota;
	}

	public String getNumNota() {
		return mNumNota;
	}

	public Date getDataNota() {
		return mDataNota;
	}

	public Date getDataScarcerazione() {
		return mDataScarcerazione;
	}

	public String getNote() {
		return mNote;
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

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	public String getIstDetIdIstitutoDetenzione() {
		return mIstDetIdIstitutoDetenzione;
	}

	public IstitutoDetenzioneModel getIstitutoDetenzione() {
		return mIstitutoDetenzione;
	}

	public String getDescrTipoIstituto() {
		return mDescrTipoIstituto;
	}

	public String getDescrLuogo() {
		return mDescrLuogo;
	}

	public String getIndirizzo() {
		return mIndirizzo;
	}

	//
	// METODI SET()
	//

	public void setIdRefertoScarcerazione(BigDecimal aValore) {
		mIdRefertoScarcerazione = aValore;
	}

	public void setAnnoNota(BigDecimal aValore) {
		mAnnoNota = aValore;
	}

	public void setNumNota(String aValore) {
		mNumNota = aValore;
	}

	public void setDataNota(Date aValore) {
		mDataNota = aValore;
	}

	public void setDataScarcerazione(Date aValore) {
		mDataScarcerazione = aValore;
	}

	public void setNote(String aValore) {
		mNote = aValore;
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

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		mIstDetIdIstitutoDetenzione = aValore;
	}

	public void setIstitutoDetenzione(IstitutoDetenzioneModel aValore) {
		mIstitutoDetenzione = aValore;
	}

	public void setDescrTipoIstituto(String aValore) {
		mDescrTipoIstituto = aValore;
	}

	public void setDescrLuogo(String aValore) {
		mDescrLuogo = aValore;
	}

	public void setIndirizzo(String aValore) {
		mIndirizzo = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mIdRefertoScarcerazione + " - " + mAnnoNota + " - " + mNumNota + " - " + mDataNota + " - "
				+ mDataScarcerazione + " - " + mNote + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodUfficioInserimento + " - " + mDescrUfficioInserimento + " - "
				+ mCodOperatoreAggiornamento + " - " + mDataAggiornamento + " - " + mCodUfficioAggiornamento
				+ " - " + mDescrUfficioAggiornamento + " - " + mEveIdEvento + " - "
				+ mIstDetIdIstitutoDetenzione;

		if (mIstitutoDetenzione != null)
			lStr += "" + mIstitutoDetenzione;

		return lStr;
	}
}
