package siap.sige.magistrato.model;

import siap.sige.magistratosezione.model.MagistratoSezioneModel;

/**
 * <p>
 * Title: MagistratoModel
 * </p>
 * <p>
 * Description: Classe Model che rappresenta il Magistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
public class MagistratoModel extends siap.sico.magistrato.model.MagistratoModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 5330005730751680695L;
	private MagistratoSezioneModel[] mMagistratoSezioni = null;

	// COSTRUTTORE DI DEFAULT
	public MagistratoModel() {
		super();
	}

	// COSTRUTTORE DI COPIA
	public MagistratoModel(MagistratoModel aModel) {
		super(aModel);
	}

	public MagistratoSezioneModel[] getMagistratoSezioni() {
		return mMagistratoSezioni;
	}

	public void setMagistratoSezioni(MagistratoSezioneModel[] aValori) {
		mMagistratoSezioni = aValori;
	}

	public String getDescrFlagStato() {
		String descDisponibilita = "";
		if (getFlagStato().equals("A")) {
			descDisponibilita = "Assente";
		} else if (getFlagStato().equals("P")) {
			descDisponibilita = "Presente";
		} else if (getFlagStato().equals("T")) {
			descDisponibilita = "Trasferito";
		} else {
			descDisponibilita = "-";
		}

		return descDisponibilita;
	}

}
