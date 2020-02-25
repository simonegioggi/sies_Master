package siap.sico.residenza.model;

import f3b.model.GenericModel;

public class ResidenzaAssociataModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6363374340403143375L;

	private ResidenzaModel mResidenza;
	private ResidenzaFascicoloSiepModel mResidenzaFascicoloSiep;
	private ResidenzaFascicoloSiusModel mResidenzaFascicoloSius;
	private ResidenzaFascicoloSigeModel mResidenzaFascicoloSige;

	public ResidenzaAssociataModel() {
		mResidenza = null;
		mResidenzaFascicoloSiep = null;
		mResidenzaFascicoloSius = null;
		mResidenzaFascicoloSige = null;
	}

	public ResidenzaAssociataModel(ResidenzaModel aResidenza,
			ResidenzaFascicoloSiepModel aResidenzaFascicoloSiep,
			ResidenzaFascicoloSiusModel aResidenzaFascicoloSius,
			ResidenzaFascicoloSigeModel aResidenzaFascicoloSige) {
		mResidenza = aResidenza;
		mResidenzaFascicoloSiep = aResidenzaFascicoloSiep;
		mResidenzaFascicoloSius = aResidenzaFascicoloSius;
		mResidenzaFascicoloSige = aResidenzaFascicoloSige;
	}

	//
	// METODI GET()
	//

	public ResidenzaModel getResidenza() {
		return mResidenza;
	}

	public ResidenzaFascicoloSiepModel getResidenzaFascicoloSiep() {
		return mResidenzaFascicoloSiep;
	}

	public ResidenzaFascicoloSiusModel getResidenzaFascicoloSius() {
		return mResidenzaFascicoloSius;
	}

	public ResidenzaFascicoloSigeModel getResidenzaFascicoloSige() {
		return mResidenzaFascicoloSige;
	}

	//
	// METODI SET()
	//

	public void setResidenza(ResidenzaModel aValore) {
		mResidenza = aValore;
	}

	public void setResidenzaFascicoloSiep(ResidenzaFascicoloSiepModel aValore) {
		mResidenzaFascicoloSiep = aValore;
	}

	public void setResidenzaFascicoloSius(ResidenzaFascicoloSiusModel aValore) {
		mResidenzaFascicoloSius = aValore;
	}

	public void setResidenzaFascicoloSige(ResidenzaFascicoloSigeModel aValore) {
		mResidenzaFascicoloSige = aValore;
	}

}