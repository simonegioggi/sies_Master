package siap.siep.modulocumulo.util;

import f3b.model.GenericModel;
import siap.sico.ufficio.model.UfficioModel;

/**
 * Model per passare al template di stampa l'elenco dei destinatari delle note di trasmissione
 *
 * @author d.fiorletta
 *
 */
public class NotaDiTrasmissioneModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -3593689373964388083L;
	private String mFlagTipoUfficio = "";
	private String mFlagVisualizzaUfficio = "";
	private UfficioModel mUfficioNotaTrasmissione = null;

	public String getFlagTipoUfficio() {
		return mFlagTipoUfficio;
	}

	public String getFlagVisualizzaUfficio() {
		return mFlagVisualizzaUfficio;
	}

	public UfficioModel getUfficioNotaTrasmissione() {
		return mUfficioNotaTrasmissione;
	}

	public void setFlagTipoUfficio(String aValore) {
		this.mFlagTipoUfficio = aValore;
	}

	public void setFlagVisualizzaUfficio(String aValore) {
		this.mFlagVisualizzaUfficio = aValore;
	}

	public void setUfficioNotaTrasmissione(UfficioModel aValore) {
		this.mUfficioNotaTrasmissione = aValore;
	}

}
