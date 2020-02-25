package siap.sius.depositoordinanzapc.model;

import siap.sico.ufficio.model.UfficioModel;

/**
 * 
 * @author user
 *
 */
public class UfficioTDSConcessoRiduzioneModel extends UfficioModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5751309623054942070L;

	public UfficioTDSConcessoRiduzioneModel() {
		super();
	}

	public UfficioTDSConcessoRiduzioneModel(UfficioModel aModel) {
		super(aModel);
	}

	public void setCodUfficio(String aValore) {
		super.setCodUfficio(aValore);
	}

	public void setCodTipoUfficio(String aValore) {
		super.setCodTipoUfficio(aValore);
	}

	public void setDescrTipoUfficio(String aValore) {
		super.setDescrTipoUfficio(aValore);
	}

	public String getCodUfficio() {
		return super.getCodUfficio();
	}

	public String getCodTipoUfficio() {
		return super.getCodTipoUfficio();
	}

	public String getDescrTipoUfficio() {
		return super.getDescrTipoUfficio();
	}

}