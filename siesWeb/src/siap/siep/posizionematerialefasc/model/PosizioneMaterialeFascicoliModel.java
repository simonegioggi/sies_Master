package siap.siep.posizionematerialefasc.model;

import java.util.List;

import f3b.model.GenericModel;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;

/**
 * <p>
 * Title: PosizioneMaterialeFascicoliModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta i Fascicoli associati attualmente a una Posizione Materiale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class PosizioneMaterialeFascicoliModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 3015891882468636700L;
	private PosizioneMaterialeModel mPosizioneMateriale;
	private List mFascicoliSiep;
	private List mFascicoliSius;

	// COSTRUTTORE DI DEFAULT
	public PosizioneMaterialeFascicoliModel() {
		mPosizioneMateriale = null;
		mFascicoliSiep = null;
		mFascicoliSius = null;
	}

	public PosizioneMaterialeFascicoliModel(PosizioneMaterialeModel aPosizioneMateriale,
			List aFascicoliSiep) {
		mPosizioneMateriale = aPosizioneMateriale;
		mFascicoliSiep = aFascicoliSiep;
		mFascicoliSius = null;
	}

	public PosizioneMaterialeFascicoliModel(PosizioneMaterialeModel aPosizioneMateriale, List aFascicoliSiep,
			List aFascicoliSius) {
		mPosizioneMateriale = aPosizioneMateriale;
		mFascicoliSiep = aFascicoliSiep;
		mFascicoliSius = aFascicoliSius;
	}

	//
	// METODI GET()
	//
	public PosizioneMaterialeModel getPosizioneMateriale() {
		return mPosizioneMateriale;
	}

	public List getFascicoliSiep() {
		return mFascicoliSiep;
	}

	public List getFascicoliSius() {
		return mFascicoliSius;
	}

	//
	// METODI SET()
	//
	public void setPosizioneMateriale(PosizioneMaterialeModel aValore) {
		mPosizioneMateriale = aValore;
	}

	public void setFascicoliSiep(List aValore) {
		mFascicoliSiep = aValore;
	}

	public void setFascicoliSius(List aValore) {
		mFascicoliSius = aValore;
	}
}