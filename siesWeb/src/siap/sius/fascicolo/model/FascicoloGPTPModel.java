package siap.sius.fascicolo.model;

/**
* <p>Title: FascicoloGPTPModel</p>
* <p>Description: Classe Model che rappresenta tutti i dati di FascicoloSius, Generale Procedimento, Tenori, ed Eventuali provvedimenti associati </p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.ArrayList; //  10/01/2008
import java.util.List;

import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.tenore.model.TenoreProvvedimentoModel;
import f3b.model.GenericModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloGPTPModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 49291813172817672L;

	private FascicoloSiusModel mFascicoloSiusModel;
	private GeneraleProcedimentoModel mGeneraleProcedimentoModel;
	private TenoreProvvedimentoModel[] mTenori;
	private List mAllegati; // 10/01/2008
	private DatiSiusPerTrasferimentoModel mDatiSiusPerTrasferimento; // 14/01/2008

	// COSTRUTTORE DI DEFAULT
	public FascicoloGPTPModel() {
		this.mFascicoloSiusModel = new FascicoloSiusModel();
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel();
		this.mTenori = null;
		this.mAllegati = null; // 10/01/2008
		this.mDatiSiusPerTrasferimento = new DatiSiusPerTrasferimentoModel(); // 14/01/2008
	}

	// COSTRUTTORE DI COPIA
	public FascicoloGPTPModel(FascicoloGPTPModel aModel) {
		this.mFascicoloSiusModel = new FascicoloSiusModel(aModel.mFascicoloSiusModel);
		this.mGeneraleProcedimentoModel = new GeneraleProcedimentoModel(aModel.mGeneraleProcedimentoModel);
		this.mTenori = new TenoreProvvedimentoModel[aModel.mTenori.length];
		this.mTenori = aModel.mTenori;
		this.mAllegati = new ArrayList(aModel.mAllegati); // 10/01/2008
		this.mDatiSiusPerTrasferimento = new DatiSiusPerTrasferimentoModel(); // 14/01/2008
	}

	// COSTRUTTORE MODEL
	public FascicoloGPTPModel(FascicoloSiusModel aFascicoloSiusModel,
			GeneraleProcedimentoModel aGeneraleProcedimentoModel, TenoreProvvedimentoModel[] aTenori,
			List aAllegati, DatiSiusPerTrasferimentoModel aDatiSiusPerTrasferimento)

	{
		this.mFascicoloSiusModel = aFascicoloSiusModel;
		this.mGeneraleProcedimentoModel = aGeneraleProcedimentoModel;
		this.mTenori = aTenori;
		this.mAllegati = aAllegati; // 10/01/2008
		this.mDatiSiusPerTrasferimento = aDatiSiusPerTrasferimento; // 14/01/2008
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

	public TenoreProvvedimentoModel[] getTenori() {
		return mTenori;
	}

	public List getAllegati() {
		return mAllegati;
	} // 10/01/2008

	public DatiSiusPerTrasferimentoModel getDatiSiusPerTrasferimento() {
		return mDatiSiusPerTrasferimento;
	} // 14/01/2008

	//
	// METODI SET()
	//
	public void setFascicoloSiusModel(FascicoloSiusModel aValore) {
		mFascicoloSiusModel = aValore;
	}

	public void setGeneraleProcedimentoModel(GeneraleProcedimentoModel aValore) {
		mGeneraleProcedimentoModel = aValore;
	}

	public void setTenori(TenoreProvvedimentoModel[] aValore) {
		mTenori = aValore;
	}

	public void setAllegati(List aValore) {
		mAllegati = aValore;
	} // 10/01/2008

	public void setDatiSiusPerTrasferimento(DatiSiusPerTrasferimentoModel aValore) {
		mDatiSiusPerTrasferimento = aValore;
	} // 14/01/2008

}