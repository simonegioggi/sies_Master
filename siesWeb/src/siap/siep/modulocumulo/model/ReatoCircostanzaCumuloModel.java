package siap.siep.modulocumulo.model;

import f3b.model.GenericModel;

/**
 * <p>
 * Title:
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

public class ReatoCircostanzaCumuloModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8574579833691863367L;
	private ReatoCumuloModel mReato;
	private ReatoCumuloModel[] mCircostanze;

	public ReatoCircostanzaCumuloModel() {
		mReato = new ReatoCumuloModel();
	}

	public ReatoCircostanzaCumuloModel(ReatoCumuloModel aReato) {
		mReato = aReato;
	}

	public ReatoCircostanzaCumuloModel(ReatoCircostanzaCumuloModel aReato) {
		mReato = aReato.getReatoCum();
		mCircostanze = aReato.getCircostanzeCum();
	}

	public ReatoCumuloModel getReatoCum() {
		return mReato;
	}

	public ReatoCumuloModel[] getCircostanzeCum() {
		return mCircostanze;
	}

	public void setReatoCum(ReatoCumuloModel aValore) {
		mReato = aValore;
	}

	public void setCircostanzeCum(ReatoCumuloModel[] aValore) {
		mCircostanze = aValore;
	}
}