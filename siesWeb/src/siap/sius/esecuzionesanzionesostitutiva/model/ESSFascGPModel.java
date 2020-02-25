package siap.sius.esecuzionesanzionesostitutiva.model;

/**
* <p>Title: ESSFascGPModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di EsecuzioneSS, FascicoloSius, Generale Procedimento </p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.model.GenericModel;

public class ESSFascGPModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5858420228568679652L;

	private EsecuzioneSanzioneSostitutivaModel mEsecuzioneSSModel;
	private FascicoloSiusModel mFascicoloSiusModel;
	private GeneraleProcedimentoModel mGeneraleProcedimentoModel;
	private TenoreModel[] mTenori;

	// COSTRUTTORE DI DEFAULT
	public ESSFascGPModel() {
		this.mEsecuzioneSSModel = new EsecuzioneSanzioneSostitutivaModel();
		this.mFascicoloSiusModel = new FascicoloSiusModel();
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel();
		this.mTenori = null;
	}

	// COSTRUTTORE DI COPIA
	public ESSFascGPModel(ESSFascGPModel aModel) {
		this.mEsecuzioneSSModel = new EsecuzioneSanzioneSostitutivaModel(aModel.mEsecuzioneSSModel);
		this.mFascicoloSiusModel = new FascicoloSiusModel(aModel.mFascicoloSiusModel);
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel(aModel.mGeneraleProcedimentoModel);
		this.mTenori = new TenoreModel[aModel.mTenori.length];
		this.mTenori = aModel.mTenori;
	}

	// COSTRUTTORE MODEL
	public ESSFascGPModel(EsecuzioneSanzioneSostitutivaModel aEsecuzioneSSModel,
			FascicoloSiusModel aFascicoloSiusModel, GeneraleProcedimentoModel aGeneraleProcedimentoModel,
			TenoreModel[] aTenori)

	{
		this.mEsecuzioneSSModel = aEsecuzioneSSModel;
		this.mFascicoloSiusModel = aFascicoloSiusModel;
		this.mGeneraleProcedimentoModel = aGeneraleProcedimentoModel;
		this.mTenori = aTenori;
	}

	//
	// METODI GET()
	//
	public EsecuzioneSanzioneSostitutivaModel getEsecuzioneSSModel() {
		return mEsecuzioneSSModel;
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
	public void setEsecuzioneSSModel(EsecuzioneSanzioneSostitutivaModel aValore) {
		mEsecuzioneSSModel = aValore;
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