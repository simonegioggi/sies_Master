package siap.siep.misurasicurezza.model;

import java.util.List;

import f3b.model.GenericModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: MisuraSicurezzaFascicoloModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta le Misure di Sicurezza legate ad un Fascicolo Siep
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
public class MisuraSicurezzaFascicoloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4669128496525600057L;
	private FascicoloSiepModel mFascicolo;
	private List mMisureSicurezza;

	// COSTRUTTORE DI DEFAULT
	public MisuraSicurezzaFascicoloModel() {
		mFascicolo = null;
		mMisureSicurezza = null;
	}

	public MisuraSicurezzaFascicoloModel(FascicoloSiepModel aFascicolo, List aMisureSicurezza) {
		mFascicolo = null;
		mMisureSicurezza = aMisureSicurezza;
	}

	//
	// METODI GET()
	//
	public FascicoloSiepModel getFascicoloSiep() {
		return mFascicolo;
	}

	public List getMisureSicurezza() {
		return mMisureSicurezza;
	}

	//
	// METODI SET()
	//
	public void setFascicoloSiep(FascicoloSiepModel aValore) {
		mFascicolo = aValore;
	}

	public void setMisureSicurezza(List aValore) {
		mMisureSicurezza = aValore;
	}
}