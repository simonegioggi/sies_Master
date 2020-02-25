package siap.sius.avvocato.model;

import f3b.model.GenericModel;

/**
 * Model che aggraga l'AvvocatoModel ed il AvvocatoFascicoloSiusModel
 * <p>
 * Title: AvvocatoSiusModel
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
public class AvvocatoSiusModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -6953439397519187846L;
	private AvvocatoModel mAvvocato;
	private AvvocatoFascicoloSiusModel mAvvFascSius;

	public AvvocatoSiusModel() {
		mAvvocato = new AvvocatoModel();
		mAvvFascSius = new AvvocatoFascicoloSiusModel();
	}

	public AvvocatoSiusModel(AvvocatoSiusModel lAvv) {
		mAvvocato = lAvv.getAvvocato();
		mAvvFascSius = lAvv.getAvvocatoFascicoloSiusModel();
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public AvvocatoFascicoloSiusModel getAvvocatoFascicoloSiusModel() {
		return mAvvFascSius;
	}

	public void setAvvocato(AvvocatoModel aValore) {
		mAvvocato = aValore;
	}

	public void setAvvocatoFascicoloSiusModel(AvvocatoFascicoloSiusModel aValore) {
		mAvvFascSius = aValore;
	}

}