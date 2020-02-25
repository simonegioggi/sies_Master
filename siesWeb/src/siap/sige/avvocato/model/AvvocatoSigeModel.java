package siap.sige.avvocato.model;

import f3b.model.GenericModel;
import siap.sico.avvocato.model.AvvocatoModel;

/**
 * Model che aggrega l'AvvocatoModel e l' AvvocatoFascicoloSigeModel
 * <p>
 * Title: AvvocatoSigeModel
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class AvvocatoSigeModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 5798942240177335298L;
	private AvvocatoModel mAvvocato;
	private AvvocatoFascicoloSigeModel mAvvFascSige;

	public AvvocatoSigeModel() {
		mAvvocato = new AvvocatoModel();
		mAvvFascSige = new AvvocatoFascicoloSigeModel();
	}

	public AvvocatoSigeModel(AvvocatoSigeModel lAvv) {
		mAvvocato = lAvv.getAvvocato();
		mAvvFascSige = lAvv.getAvvocatoFascicoloSigeModel();
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public AvvocatoFascicoloSigeModel getAvvocatoFascicoloSigeModel() {
		return mAvvFascSige;
	}

	public void setAvvocato(AvvocatoModel aValore) {
		mAvvocato = aValore;
	}

	public void setAvvocatoFascicoloSigeModel(AvvocatoFascicoloSigeModel aValore) {
		mAvvFascSige = aValore;
	}

}