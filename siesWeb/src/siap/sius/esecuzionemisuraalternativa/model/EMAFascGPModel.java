package siap.sius.esecuzionemisuraalternativa.model;

/**
* <p>Title: EMAFascGPModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di EsecuzioneMA, FascicoloSius, Generale Procedimento </p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

public class EMAFascGPModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8291358005978295706L;

	private EsecuzioneMisuraAlternativaModel mEsecuzioneMAModel;
	private FascicoloSiusModel mFascicoloSiusModel;
	private GeneraleProcedimentoModel mGeneraleProcedimentoModel;
	private TenoreModel[] mTenori;

	// COSTRUTTORE DI DEFAULT
	public EMAFascGPModel() {
		this.mEsecuzioneMAModel = new EsecuzioneMisuraAlternativaModel();
		this.mFascicoloSiusModel = new FascicoloSiusModel();
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel();
		this.mTenori = null;
	}

	// COSTRUTTORE DI COPIA
	public EMAFascGPModel(EMAFascGPModel aModel) {
		this.mEsecuzioneMAModel = new EsecuzioneMisuraAlternativaModel(aModel.mEsecuzioneMAModel);
		this.mFascicoloSiusModel = new FascicoloSiusModel(aModel.mFascicoloSiusModel);
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel(aModel.mGeneraleProcedimentoModel);
		this.mTenori = new TenoreModel[aModel.mTenori.length];
		this.mTenori = aModel.mTenori;
	}

	// COSTRUTTORE MODEL
	public EMAFascGPModel(EsecuzioneMisuraAlternativaModel aEsecuzioneMAModel,
			FascicoloSiusModel aFascicoloSiusModel, GeneraleProcedimentoModel aGeneraleProcedimentoModel,
			TenoreModel[] aTenori)

	{
		this.mEsecuzioneMAModel = aEsecuzioneMAModel;
		this.mFascicoloSiusModel = aFascicoloSiusModel;
		this.mGeneraleProcedimentoModel = aGeneraleProcedimentoModel;
		this.mTenori = aTenori;
	}

	//
	// METODI GET()
	//
	public EsecuzioneMisuraAlternativaModel getEsecuzioneMAModel() {
		return mEsecuzioneMAModel;
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
	public void setEsecuzioneMAModel(EsecuzioneMisuraAlternativaModel aValore) {
		mEsecuzioneMAModel = aValore;
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