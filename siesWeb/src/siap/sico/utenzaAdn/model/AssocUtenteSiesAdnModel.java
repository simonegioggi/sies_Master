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
public class AssocUtenteSiesAdnModel extends GenericModel {

	private static final long serialVersionUID = -1287014387736772061L;

	private BigDecimal mId;
	private String mUteCodUtente;
	private BigDecimal mIdUtenteAdn;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;

	// COSTRUTTORE DI DEFAULT
	public AssocUtenteSiesAdnModel() {

		mId = null;
		mUteCodUtente = "";
		mIdUtenteAdn = null;
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
	}

	// COSTRUTTORE DI COPIA
	public AssocUtenteSiesAdnModel(AssocUtenteSiesAdnModel aModel) {

		mId = aModel.mId;
		mUteCodUtente = aModel.mUteCodUtente;
		mIdUtenteAdn = aModel.mIdUtenteAdn;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
	}

	// COSTRUTTORE MODEL
	public AssocUtenteSiesAdnModel(BigDecimal aId, String aUteCodUtente, BigDecimal aIdUtenteAdn,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento) {

		mId = aId;
		mUteCodUtente = aUteCodUtente;
		mIdUtenteAdn = aIdUtenteAdn;
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

	public String getUteCodUtente() {
		return mUteCodUtente;
	}

	public BigDecimal getIdUtenteAdn() {
		return mIdUtenteAdn;
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

	public void setUteCodUtente(String aValore) {
		mUteCodUtente = aValore;
	}

	public void setIdUtenteAdn(BigDecimal aValore) {
		mIdUtenteAdn = aValore;
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

		String ts = mId + " - " + mUteCodUtente + " - " + mIdUtenteAdn + " - " + mCodOperatoreInserimento
				+ " - " + mDataInserimento + " - " + mCodOperatoreAggiornamento + " - " + mDataAggiornamento;

		return ts;
	}

}