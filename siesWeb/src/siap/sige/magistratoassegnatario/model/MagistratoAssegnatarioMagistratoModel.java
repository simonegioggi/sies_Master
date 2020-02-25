package siap.sige.magistratoassegnatario.model;

import f3b.model.GenericModel;
import siap.sico.magistrato.model.MagistratoModel;

/**
 * <p>
 * Title: MagistratoAssegnatarioMagistratoModel
 * </p>
 * <p>
 * Description: ClasseModel che contiene il Magistrato ed il Magistrato Assegnatario
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

public class MagistratoAssegnatarioMagistratoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1230696399468022784L;
	private MagistratoAssegnatarioModel mAssegnatario;
	private MagistratoModel mMagistrato;

	public MagistratoAssegnatarioMagistratoModel() {
		mAssegnatario = new MagistratoAssegnatarioModel();
		mMagistrato = new MagistratoModel();
	}

	public MagistratoAssegnatarioMagistratoModel(MagistratoAssegnatarioMagistratoModel aModel) {
		this.mAssegnatario = aModel.getMagistratoAssegnatario();
		this.mMagistrato = aModel.getMagistrato();
	}

	public MagistratoAssegnatarioMagistratoModel(MagistratoAssegnatarioModel aAssegnatario,
			MagistratoModel aMagistrato) {
		this.mAssegnatario = aAssegnatario;
		this.mMagistrato = aMagistrato;
	}

	public MagistratoModel getMagistrato() {
		return mMagistrato;
	}

	public MagistratoAssegnatarioModel getMagistratoAssegnatario() {
		return mAssegnatario;
	}

	public void setMagistrato(MagistratoModel aValore) {
		mMagistrato = aValore;
	}

	public void setMagistratoAssegnatario(MagistratoAssegnatarioModel aValore) {
		mAssegnatario = aValore;
	}

}