package siap.siep.statis.model;

/**
* <p>Title: StatoFascicoloResModel</p>
* <p>Description: Classe Model che rappresenta il StatoFascicoloRes</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.model.GenericModel;

public class StatoFascicoloResModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 534978397861747640L;

	private Integer mCodStatoFascicolo;
	private String mDescrizione;

	private Integer mOrdinamento;
	private String mTipoCampo;

	private Integer mT1;
	private Integer mT2;

	// COSTRUTTORE DI DEFAULT
	public StatoFascicoloResModel() {
		this.mCodStatoFascicolo = null;
		this.mDescrizione = "";

		this.mOrdinamento = null;
		this.mTipoCampo = "";
		this.mT1 = null;
		this.mT2 = null;

	}

	// COSTRUTTORE DI COPIA
	public StatoFascicoloResModel(StatoFascicoloResModel aModel) {
		this.mCodStatoFascicolo = aModel.mCodStatoFascicolo;
		this.mDescrizione = aModel.mDescrizione;

		this.mOrdinamento = aModel.mOrdinamento;
		this.mTipoCampo = aModel.mTipoCampo;
		this.mT1 = aModel.mT1;
		this.mT2 = aModel.mT2;
	}

	// COSTRUTTORE MODEL
	public StatoFascicoloResModel(Integer aCodStatoFascicolo, String aDescrizione, Integer aOrdinamento,
			String aTipoCampo, Integer aT1, Integer aT2) {
		this.mCodStatoFascicolo = aCodStatoFascicolo;
		this.mDescrizione = aDescrizione;

		this.mOrdinamento = aOrdinamento;
		this.mTipoCampo = aTipoCampo;
		this.mT1 = aT1;
		this.mT2 = aT2;
	}

	//
	// METODI GET()
	//
	public Integer getCodStatoFascicolo() {
		return mCodStatoFascicolo;
	}

	public String getDescrizione() {
		return mDescrizione;
	}

	public Integer getOrdinamento() {
		return mOrdinamento;
	}

	public String getTipoCampo() {
		return mTipoCampo;
	}

	public Integer getT1() {
		return mT1;
	}

	public Integer getT2() {
		return mT2;
	}

	//
	// METODI SET()
	//
	public void setCodStatoFascicolo(Integer aValore) {
		mCodStatoFascicolo = aValore;
	}

	public void setDescrizione(String aValore) {
		mDescrizione = aValore;
	}

	public void setOrdinamento(Integer aValore) {
		mOrdinamento = aValore;
	}

	public void setTipoCampo(String aValore) {
		mTipoCampo = aValore;
	}

	public void setT1(Integer aValore) {
		mT1 = aValore;
	}

	public void setT2(Integer aValore) {
		mT2 = aValore;
	}

}