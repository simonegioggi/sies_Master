package siap.sius.tenore.model;

/**
* <p>Title: TenoreProvvedimentoModel</p>
* <p>Description: Classe Model che rappresenta il Tenore con l'eventuale provvedimento annesso (Decreto o Ordinanza)</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import f3b.model.GenericModel;

public class TenoreProvvedimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1242049821843319472L;

	private TenoreModel mTenore;
	private DepositoDecretoModel mDecreto;
	private DepositoOrdinanzaPcModel mOrdinanza;
	private DecretoOrdinanzaSiepModel mDecOrdSiep; // 10/01/2008

	// COSTRUTTORE DI DEFAULT
	public TenoreProvvedimentoModel() {
		this.mTenore = new TenoreModel();
		this.mDecreto = new DepositoDecretoModel();
		this.mOrdinanza = new DepositoOrdinanzaPcModel();
		this.mDecOrdSiep = new DecretoOrdinanzaSiepModel(); // 10/01/2008
	}

	// COSTRUTTORE DI COPIA
	public TenoreProvvedimentoModel(TenoreProvvedimentoModel aModel) {
		this.mTenore = new TenoreModel(aModel.mTenore);
		this.mDecreto = new DepositoDecretoModel(aModel.mDecreto);
		this.mOrdinanza = new DepositoOrdinanzaPcModel(aModel.mOrdinanza);
		this.mDecOrdSiep = new DecretoOrdinanzaSiepModel(aModel.mDecOrdSiep); // 10/01/2008
	}

	// COSTRUTTORE MODEL
	public TenoreProvvedimentoModel(TenoreModel aTenore, DepositoDecretoModel aDecreto,
			DepositoOrdinanzaPcModel aOrdinanza, DecretoOrdinanzaSiepModel aDecOrdSiep) {
		this.mTenore = aTenore;
		this.mDecreto = aDecreto;
		this.mOrdinanza = aOrdinanza;
		this.mDecOrdSiep = aDecOrdSiep; // 10/01/2008
	}

	//
	// METODI GET()
	//
	public TenoreModel getTenore() {
		return mTenore;
	}

	public DepositoDecretoModel getDecreto() {
		return mDecreto;
	}

	public DepositoOrdinanzaPcModel getOrdinanza() {
		return mOrdinanza;
	}

	public DecretoOrdinanzaSiepModel getDecOrdSiep() {
		return mDecOrdSiep;
	}

	//
	// METODI SET()
	//
	public void setTenore(TenoreModel aValore) {
		mTenore = aValore;
	}

	public void setDecreto(DepositoDecretoModel aValore) {
		mDecreto = aValore;
	}

	public void setOrdinanza(DepositoOrdinanzaPcModel aValore) {
		mOrdinanza = aValore;
	}

	public void setDecOrdSiep(DecretoOrdinanzaSiepModel aValore) {
		mDecOrdSiep = aValore;
	}

}