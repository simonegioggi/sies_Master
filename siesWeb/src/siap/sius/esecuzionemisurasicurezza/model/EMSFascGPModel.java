package siap.sius.esecuzionemisurasicurezza.model;

/**
* <p>Title: EMSFascGPModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di EsecuzioneMS, FascicoloSius, Generale Procedimento </p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

public class EMSFascGPModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7189871279049969861L;

	private EsecuzioneMisuraSicurezzaModel mEsecuzioneMSModel;
	private FascicoloSiusModel mFascicoloSiusModel;
	private GeneraleProcedimentoModel mGeneraleProcedimentoModel;
	private TenoreModel[] mTenori;

	// COSTRUTTORE DI DEFAULT
	public EMSFascGPModel() {
		this.mEsecuzioneMSModel = new EsecuzioneMisuraSicurezzaModel();
		this.mFascicoloSiusModel = new FascicoloSiusModel();
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel();
		this.mTenori = null;
	}

	// COSTRUTTORE DI COPIA
	public EMSFascGPModel(EMSFascGPModel aModel) {
		this.mEsecuzioneMSModel = new EsecuzioneMisuraSicurezzaModel(aModel.mEsecuzioneMSModel);
		this.mFascicoloSiusModel = new FascicoloSiusModel(aModel.mFascicoloSiusModel);
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel(aModel.mGeneraleProcedimentoModel);
		this.mTenori = new TenoreModel[aModel.mTenori.length];
		this.mTenori = aModel.mTenori;
	}

	// COSTRUTTORE MODEL
	public EMSFascGPModel(EsecuzioneMisuraSicurezzaModel aEsecuzioneMSModel,
			FascicoloSiusModel aFascicoloSiusModel, GeneraleProcedimentoModel aGeneraleProcedimentoModel,
			TenoreModel[] aTenori)

	{
		this.mEsecuzioneMSModel = aEsecuzioneMSModel;
		this.mFascicoloSiusModel = aFascicoloSiusModel;
		this.mGeneraleProcedimentoModel = aGeneraleProcedimentoModel;
		this.mTenori = aTenori;
	}

	//
	// METODI GET()
	//
	public EsecuzioneMisuraSicurezzaModel getEsecuzioneMSModel() {
		return mEsecuzioneMSModel;
	}

	public FascicoloSiusModel getFascicoloSiusModel() {
		return mFascicoloSiusModel;
	}

	public GeneraleProcedimentoModel getGeneraleProcedimentoModel() {
		return mGeneraleProcedimentoModel;
	}

	public TenoreModel[] getTenori() {
		return mTenori;
	}

	//
	// METODI SET()
	//
	public void setEsecuzioneMSModel(EsecuzioneMisuraSicurezzaModel aValore) {
		mEsecuzioneMSModel = aValore;
	}

	public void setFascicoloSiusModel(FascicoloSiusModel aValore) {
		mFascicoloSiusModel = aValore;
	}

	public void setGeneraleProcedimentoModel(GeneraleProcedimentoModel aValore) {
		mGeneraleProcedimentoModel = aValore;
	}

	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

}