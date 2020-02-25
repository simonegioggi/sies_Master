package siap.sius.generaleprocedimento.model;

import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

public class GPTenoreModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8754894282268382730L;

	private GeneraleProcedimentoModel mGPModel;
	private TenoreModel[] mTenori;
	private String[] mEsiti;

	public GPTenoreModel() {
		mGPModel = null;
	}

	// Costruttore di Copia.
	public GPTenoreModel(GPTenoreModel aModel) {
		this.mGPModel = aModel.mGPModel;
		this.mTenori = aModel.mTenori;
		this.mEsiti = aModel.mEsiti;
	}

	public GeneraleProcedimentoModel getGeneraleProcedimentoModel() {
		return mGPModel;
	}

	public TenoreModel[] getTenori() {
		return mTenori;
	}

	public String[] getEsito() {
		return mEsiti;
	}

	public void setGeneraleProcedimentoModel(GeneraleProcedimentoModel aValore) {
		mGPModel = aValore;
	}

	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

	public void setEsiti(String[] aValore) {
		mEsiti = aValore;
	}

}