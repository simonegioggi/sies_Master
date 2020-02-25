package siap.sius.fascicolo.model;

/**
* <p>Title: FascicoloGPModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di FascicoloSius, Generale Procedimento </p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import f3b.model.GenericModel;

public class FascicoloGPModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -601445074628330050L;

	private FascicoloSiusModel mFascicoloSiusModel;
	private GeneraleProcedimentoModel mGeneraleProcedimentoModel;
	// 05/11/2003 REWORK per portare FascicoloGPModel a contenere un array di Tenori.
	// private TenoreModel mTenoreModel;
	private TenoreModel[] mTenori;
	// Anche la relazione Udienza-Procedimento Luigi 9-5-2005
	private UdienzaProcedimentoModel mUdiPro = null;

	private MagistratoRelatoreModel mMagistratoRelatore = null;

	// 05/11/2003 REWORK per i COSTRUTTORI.
	/*
	 * //COSTRUTTORE DI DEFAULT public FascicoloGPModel () { this.mFascicoloSiusModel = new
	 * FascicoloSiusModel(); this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel(); this.mTenori =
	 * new TenoreModel(); }
	 * 
	 * //COSTRUTTORE DI COPIA public FascicoloGPModel ( FascicoloGPModel aModel ) { this.mFascicoloSiusModel =
	 * new FascicoloSiusModel(aModel.mFascicoloSiusModel); this.mGeneraleProcedimentoModel = new
	 * GeneraleProcedimentoModel(aModel.mGeneraleProcedimentoModel); this.mTenoreModel = new
	 * TenoreModel(aModel.mTenoreModel); }
	 */

	// COSTRUTTORE DI DEFAULT
	public FascicoloGPModel() {
		this.mFascicoloSiusModel = new FascicoloSiusModel();
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel();
		this.mTenori = null;
		mUdiPro = null;
	}

	// COSTRUTTORE DI COPIA
	public FascicoloGPModel(FascicoloGPModel aModel) {
		this.mFascicoloSiusModel = new FascicoloSiusModel(aModel.mFascicoloSiusModel);
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel(aModel.mGeneraleProcedimentoModel);
		this.mTenori = new TenoreModel[aModel.mTenori.length];
		this.mTenori = aModel.mTenori;
		mUdiPro = aModel.mUdiPro;
	}

	// COSTRUTTORE MODEL
	public FascicoloGPModel(FascicoloSiusModel aFascicoloSiusModel,
			GeneraleProcedimentoModel aGeneraleProcedimentoModel, TenoreModel[] aTenori,
			UdienzaProcedimentoModel aUdiPro)

	{
		this.mFascicoloSiusModel = aFascicoloSiusModel;
		this.mGeneraleProcedimentoModel = aGeneraleProcedimentoModel;
		this.mTenori = aTenori;
		mUdiPro = aUdiPro;
	}

	//
	// METODI GET()
	//
	public FascicoloSiusModel getFascicoloSiusModel() {
		return mFascicoloSiusModel;
	}

	public GeneraleProcedimentoModel getGeneraleProcedimentoModel() {
		return mGeneraleProcedimentoModel;
	}

	// 05/11/2003 REWORK - TenoreModel ---> TenoreModel[]
	public TenoreModel[] getTenori() {
		return mTenori;
	}

	public UdienzaProcedimentoModel getUdiPro() {
		return mUdiPro;
	}

	public MagistratoRelatoreModel getMagistratoRelatore() {
		return mMagistratoRelatore;
	}

	//
	// METODI SET()
	//
	public void setFascicoloSiusModel(FascicoloSiusModel aValore) {
		mFascicoloSiusModel = aValore;
	}

	public void setGeneraleProcedimentoModel(GeneraleProcedimentoModel aValore) {
		mGeneraleProcedimentoModel = aValore;
	}

	// 05/11/2003 REWORK - TenoreModel ---> TenoreModel[]
	public void setTenori(TenoreModel[] aValore) {
		mTenori = aValore;
	}

	public void setUdiPro(UdienzaProcedimentoModel aValore) {
		mUdiPro = aValore;
	}

	public void setMagistratoRelatore(MagistratoRelatoreModel aValore) {
		mMagistratoRelatore = aValore;
	}

}