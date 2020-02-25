package siap.siep.avvocato.model;

import f3b.model.GenericModel;

/**
 * Model che aggrega l'AvvocatoModel ed il AvvocatoFascicoloSiepModel
 * <p>
 * Title: AvvocatoSiepModel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class AvvocatoSiepModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3342193367949224651L;

	private AvvocatoModel mAvvocato;
	private AvvocatoFascicoloSiepModel mAvvFascSiep;

	public AvvocatoSiepModel() {
		mAvvocato = new AvvocatoModel();
		mAvvFascSiep = new AvvocatoFascicoloSiepModel();
	}

	public AvvocatoSiepModel(AvvocatoSiepModel lAvv) {
		mAvvocato = lAvv.getAvvocato();
		mAvvFascSiep = lAvv.getAvvocatoFascicoloSiepModel();
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public AvvocatoFascicoloSiepModel getAvvocatoFascicoloSiepModel() {
		return mAvvFascSiep;
	}

	public void setAvvocato(AvvocatoModel aValore) {
		mAvvocato = aValore;
	}

	public void setAvvocatoFascicoloSiepModel(AvvocatoFascicoloSiepModel aValore) {
		mAvvFascSiep = aValore;
	}

	/*****************************************************************************
	 * Metodo toString() che restituisce il contenuto del Model opportunamente formattato. Utile per il debug.
	 ****************************************************************************/
	public String toString() {
		String lStr = new String();

		lStr = "AvvocatoModel:\n" + mAvvocato.toString();
		lStr = "\n";
		lStr = "AvvocatoFascicoloSiepModel:\n" + mAvvFascSiep.toString2();
		return lStr;
	}

}