package siap.sico.utenzaAdn.model;

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class UtenzaAdnModel extends GenericModel {

	private static final long serialVersionUID = -3464730149978600606L;

	private BigDecimal mId;
	private String mSamAccountName;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public UtenzaAdnModel() {

		mId = null;
		mSamAccountName = "";
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
	}

	// COSTRUTTORE DI COPIA
	public UtenzaAdnModel(UtenzaAdnModel aModel) {

		mId = aModel.mId;
		mSamAccountName = aModel.mSamAccountName;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
	}

	// COSTRUTTORE MODEL
	public UtenzaAdnModel(BigDecimal aId, String aSamAccountName, String aCodOperatoreInserimento,
			Date aDataInserimento, String aCodOperatoreAggiornamento, Date aDataAggiornamento) {

		mId = aId;
		mSamAccountName = aSamAccountName;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getId() {
		return mId;
	}

	public String getSamAccountName() {
		return mSamAccountName;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	//
	// METODI SET()
	//

	public void setId(BigDecimal aValore) {
		mId = aValore;
	}

	public void setSamAccountName(String aValore) {
		mSamAccountName = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	@Override
	public String toString() {

		String ts = mId + " - " + mSamAccountName + " - " + mCodOperatoreInserimento + " - "
				+ mDataInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento;

		return ts;
	}

}