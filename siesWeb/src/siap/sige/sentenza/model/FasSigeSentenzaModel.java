package siap.sige.sentenza.model;

/**
* <p>Title: FasSigeSentenzaModel</p>
* <p>Description: Classe Model che rappresenta il FasSigeSentenza, 
* ovvero il tracciato record della tabella di relazione FAS_SIGE_SENTENZA, che memorizza l'associazione FascicoloSige - Sentenze.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import f3b.model.GenericModel;

public class FasSigeSentenzaModel extends GenericModel {

	/**
	* 
	*/
	private static final long serialVersionUID = -4431224165185772495L;

	private BigDecimal mIdFasSigeSentenza;
	private BigDecimal mFasIdFascicoloSige;
	private BigDecimal mSenIdSentenza;
	private Date mDataInserimento;
	private String mCodOperatoreInserimento;
	private String mCodUfficioInserimento;
	private String mDescrUfficioInserimento;

	// COSTRUTTORE DI DEFAULT
	public FasSigeSentenzaModel() {
		this.mIdFasSigeSentenza = null;
		this.mFasIdFascicoloSige = null;
		this.mSenIdSentenza = null;
		this.mDataInserimento = null;
		this.mCodOperatoreInserimento = "";
		this.mCodUfficioInserimento = "";
		this.mDescrUfficioInserimento = "";
	}

	// COSTRUTTORE DI COPIA
	public FasSigeSentenzaModel(FasSigeSentenzaModel aModel) {
		mIdFasSigeSentenza = aModel.mIdFasSigeSentenza;
		mFasIdFascicoloSige = aModel.mFasIdFascicoloSige;
		mSenIdSentenza = aModel.mSenIdSentenza;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mCodUfficioInserimento = aModel.mCodUfficioInserimento;
		mDescrUfficioInserimento = aModel.mDescrUfficioInserimento;
	}

	// COSTRUTTORE MODEL
	public FasSigeSentenzaModel(BigDecimal aIdFasSigeSentenza, BigDecimal aFasIdFascicoloSige,
			BigDecimal aSenIdSentenza, Date aDataInserimento, String aCodOperatoreInserimento,
			String aCodUfficioInserimento, String aDescrUfficioInserimento) {
		mIdFasSigeSentenza = aIdFasSigeSentenza;
		mFasIdFascicoloSige = aFasIdFascicoloSige;
		mSenIdSentenza = aSenIdSentenza;
		mDataInserimento = aDataInserimento;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mCodUfficioInserimento = aCodUfficioInserimento;
		mDescrUfficioInserimento = aDescrUfficioInserimento;
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFasSigeSentenza() {
		return mIdFasSigeSentenza;
	}

	public BigDecimal getFasIdFascicoloSige() {
		return mFasIdFascicoloSige;
	}

	public BigDecimal getSenIdSentenza() {
		return mSenIdSentenza;
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

	//
	// METODI SET()
	//

	public void setIdFasSigeSentenza(BigDecimal aValore) {
		mIdFasSigeSentenza = aValore;
	}

	public void setFasIdFascicoloSige(BigDecimal aValore) {
		mFasIdFascicoloSige = aValore;
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		mSenIdSentenza = aValore;
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

}