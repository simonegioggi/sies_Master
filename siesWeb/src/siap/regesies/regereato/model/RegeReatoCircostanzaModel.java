package siap.regesies.regereato.model;

import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: RegeReatoCircostanzaModel
 * </p>
 * <p>
 * Description: Realizza il model teorico dei Reati con un retao principale e un array di Circostanze ad esso
 * legate
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
public class RegeReatoCircostanzaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2307868555130705475L;

	private RegeReatoModel mReato;
	private RegeReatoModel[] mCircostanze;

	public RegeReatoCircostanzaModel() {
		mReato = new RegeReatoModel();
	}

	public RegeReatoCircostanzaModel(RegeReatoModel aReato) {
		mReato = aReato;
	}

	public RegeReatoCircostanzaModel(RegeReatoCircostanzaModel aReato) {
		mReato = aReato.getReato();
		mCircostanze = aReato.getCircostanze();
	}

	public RegeReatoModel getReato() {
		return mReato;
	}

	public RegeReatoModel[] getCircostanze() {
		return mCircostanze;
	}

	public void setReato(RegeReatoModel aValore) {
		mReato = aValore;
	}

	public void setCircostanze(RegeReatoModel[] aValore) {
		mCircostanze = aValore;
	}

	/**
	 * Conversione in Reato Circostanza
	 * 
	 * @return
	 */
	public ReatoCircostanzaModel toReatoCircostanze() {
		ReatoCircostanzaModel lReatoCirc = new ReatoCircostanzaModel();

		if (this.getReato() != null)
			lReatoCirc.setReato(this.getReato().toReato());
		if (this.getCircostanze() != null)
			lReatoCirc.setCircostanze(this.toReatoArray());
		return lReatoCirc;
	}

	private ReatoModel[] toReatoArray() {
		if (this.getCircostanze() != null && getCircostanze().length > 0) {
			ReatoModel[] lReati = new ReatoModel[getCircostanze().length];

			int i = 0;
			int y = 0;
			for (i = 0; i < getCircostanze().length; i++) {

				if (getCircostanze()[i] != null) {
					ReatoModel lReato = getCircostanze()[i].toReato();
					lReati[y] = lReato;
					y++;
				}
			}
			return lReati;
		}
		return null;
	}

}