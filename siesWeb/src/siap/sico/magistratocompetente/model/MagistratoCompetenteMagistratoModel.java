package siap.sico.magistratocompetente.model;

import siap.sico.magistrato.model.MagistratoModel;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: MagistratoCompetenteMagistratoModel
 * </p>
 * <p>
 * Description: ClasseModel che contiene il Magistrato ed il Magistrato competente
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
public class MagistratoCompetenteMagistratoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5660170599536792730L;

	private MagistratoCompetenteModel mCompetente;
	private MagistratoModel mMagistrato;

	public MagistratoCompetenteMagistratoModel() {
		mCompetente = new MagistratoCompetenteModel();
		mMagistrato = new MagistratoModel();
	}

	public MagistratoCompetenteMagistratoModel(MagistratoCompetenteMagistratoModel aModel) {
		this.mCompetente = aModel.getMagistratoCompetente();
		this.mMagistrato = aModel.getMagistrato();
	}

	public MagistratoCompetenteMagistratoModel(MagistratoCompetenteModel aCompetente,
			MagistratoModel aMagistrato) {
		this.mCompetente = aCompetente;
		this.mMagistrato = aMagistrato;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	public MagistratoCompetenteModel getMagistratoCompetente() {
		return mCompetente;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

	public void setMagistratoCompetente(MagistratoCompetenteModel aValore) {
		mCompetente = aValore;
	}

}