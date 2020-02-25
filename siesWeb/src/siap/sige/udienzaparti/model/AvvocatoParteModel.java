package siap.sige.udienzaparti.model;

import f3b.model.GenericModel;
import siap.sico.avvocato.model.AvvocatoModel;

/**
 * Model che aggrega l'AvvocatoModel e PartiUdienzaDifensoreModel
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class AvvocatoParteModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -7167565153778516641L;
	private AvvocatoModel mAvvocato;
	private PartiUdienzaDifensoreModel mAvvParteUdienza;

	public AvvocatoParteModel() {
		mAvvocato = new AvvocatoModel();
		mAvvParteUdienza = new PartiUdienzaDifensoreModel();
	}

	public AvvocatoParteModel(AvvocatoParteModel lAvv) {
		mAvvocato = lAvv.getAvvocato();
		mAvvParteUdienza = lAvv.getAvvocatoParteUdienzaModel();
	}

	public AvvocatoModel getAvvocato() {
		return mAvvocato;
	}

	public PartiUdienzaDifensoreModel getAvvocatoParteUdienzaModel() {
		return mAvvParteUdienza;
	}

	public void setAvvocato(AvvocatoModel aValore) {
		mAvvocato = aValore;
	}

	public void setAvvocatoParteUdienzaModel(PartiUdienzaDifensoreModel aValore) {
		mAvvParteUdienza = aValore;
	}

}