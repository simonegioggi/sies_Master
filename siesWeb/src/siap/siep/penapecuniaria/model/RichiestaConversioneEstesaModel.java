package siap.siep.penapecuniaria.model;

import f3b.model.GenericModel;

/**
* <p>Title: RichiestaConversioneEstesaModel</p>
* <p>Description: Questa Classe Model associa l RICHIESTA_CONVERSIONE a FASCICOLO_SIEP e FASCICOLO_SIUS</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;

public class RichiestaConversioneEstesaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5796510332695320667L;
	private RichiestaConversioneModel mRichiestaCPP;
	private FascicoloSiepModel mFasSiep;
	private FascicoloSiusModel mFasSius;

	// COSTRUTTORE DI DEFAULT
	public RichiestaConversioneEstesaModel() {
		mRichiestaCPP = null;
		mFasSiep = null;
		mFasSius = null;
	}

	// COSTRUTTORE DI COPIA
	public RichiestaConversioneEstesaModel(RichiestaConversioneEstesaModel aModel) {
		mRichiestaCPP = new RichiestaConversioneModel(aModel.getRichiestaConversione());
		mFasSiep = new FascicoloSiepModel(aModel.getFasSiep());
		mFasSius = new FascicoloSiusModel(aModel.getFasSius());
	}

	// COSTRUTTORE MODEL
	public RichiestaConversioneEstesaModel(RichiestaConversioneModel aRichiestaCPP) {
		this();
		aRichiestaCPP = new RichiestaConversioneModel(aRichiestaCPP);
	}

	public RichiestaConversioneEstesaModel(RichiestaConversioneModel aRichiestaCPP,
			FascicoloSiepModel aFasSiep, FascicoloSiusModel aFasSius) {
		mRichiestaCPP = new RichiestaConversioneModel(aRichiestaCPP);
		mFasSiep = new FascicoloSiepModel(aFasSiep);
		mFasSius = new FascicoloSiusModel(aFasSius);
	}

	// METODI GET()
	//
	public RichiestaConversioneModel getRichiestaConversione() {
		return mRichiestaCPP;
	}

	public FascicoloSiepModel getFasSiep() {
		return mFasSiep;
	}

	public FascicoloSiusModel getFasSius() {
		return mFasSius;
	}

	// METODI SET()
	//
	public void setRichiestaConversione(RichiestaConversioneModel aValore) {
		mRichiestaCPP = aValore;
	}

	public void setFasSiep(FascicoloSiepModel aValore) {
		mFasSiep = aValore;
	}

	public void setFasSius(FascicoloSiusModel aValore) {
		mFasSius = aValore;
	}

	@Override
	public String toString() {
		String lRet = getClass().getName() + "\n";
		if (mRichiestaCPP != null)
			lRet += mRichiestaCPP.toString() + "\n";
		if (mFasSiep != null)
			lRet += mFasSiep.toString() + "\n";
		if (mFasSius != null)
			lRet += mFasSius.toString() + "\n";

		return lRet;
	}
}