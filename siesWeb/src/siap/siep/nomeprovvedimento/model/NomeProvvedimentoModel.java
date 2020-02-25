package siap.siep.nomeprovvedimento.model;

/**
* <p>Title: NomeProvvedimentoModel</p>
* <p>Description: Classe Model che rappresenta il NomeProvvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class NomeProvvedimentoModel extends GenericModel {
	/**
	* 
	*/
	private static final long serialVersionUID = -9185253507761137721L;
	private String mCodNomeProvvedimento;
	private String mDescrNomeProvvedimento;
	private BigDecimal mEveIdEvento;

	// COSTRUTTORE DI DEFAULT
	public NomeProvvedimentoModel() {
		this.mCodNomeProvvedimento = "";
		this.mDescrNomeProvvedimento = "";
		this.mEveIdEvento = null;
	}

	// COSTRUTTORE DI COPIA
	public NomeProvvedimentoModel(NomeProvvedimentoModel aModel) {
		this.mCodNomeProvvedimento = aModel.mCodNomeProvvedimento;
		this.mDescrNomeProvvedimento = aModel.mDescrNomeProvvedimento;
		this.mEveIdEvento = aModel.mEveIdEvento;
	}

	// COSTRUTTORE MODEL
	public NomeProvvedimentoModel(String aCodNomeProvvedimento, String aDescrNomeProvvedimento,
			BigDecimal aEveIdEvento) {
		this.mCodNomeProvvedimento = aCodNomeProvvedimento;
		this.mDescrNomeProvvedimento = aDescrNomeProvvedimento;
		this.mEveIdEvento = aEveIdEvento;
	}

	//
	// METODI GET()
	//

	public String getCodNomeProvvedimento() {
		return mCodNomeProvvedimento;
	}

	public String getDescrNomeProvvedimento() {
		return mDescrNomeProvvedimento;
	}

	public BigDecimal getEveIdEvento() {
		return mEveIdEvento;
	}

	//
	// METODI SET()
	//

	public void setCodNomeProvvedimento(String aValore) {
		mCodNomeProvvedimento = aValore;
	}

	public void setDescrNomeProvvedimento(String aValore) {
		mDescrNomeProvvedimento = aValore;
	}

	public void setEveIdEvento(BigDecimal aValore) {
		mEveIdEvento = aValore;
	}

	@Override
	public String toString() {
		String lStr = new String();

		lStr = "" + mCodNomeProvvedimento + " - " + mDescrNomeProvvedimento + " - " + mEveIdEvento;

		return lStr;
	}
}
