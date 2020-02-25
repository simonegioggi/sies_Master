package siap.sico.evento.model;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import f3b.model.GenericModel;

public class StampaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 995371702377776117L;

	private String mUfficio;
	private String mTipoUfficio;

	public StampaModel() {
	}

	public StampaModel(String aUff, String aTipoUff) {
		mUfficio = aUff;
		mTipoUfficio = aTipoUff;
	}

	public String getUfficio() {
		return mUfficio;
	}

	public String getTipoUfficio() {
		return mTipoUfficio;
	}

	public void setUfficio(String aValore) {
		mUfficio = aValore;
	}

	public void setTipoUfficio(String aValore) {
		mTipoUfficio = aValore;
	}

}