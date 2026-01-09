package it.mig.sies.model;

import java.io.Serial;

/**
 * MEV 23010 - Mappa le chiavi relative ad un provvedimento collegato
 * 
 * @author Federico Paparoni
 */
public class ProvvedimentoCollegato extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -8441215486109822041L;

	private String chiaveNSC;
	private String chiaveSIES;

	/**
	 * @return the chiaveNSC
	 */
	public String getChiaveNSC() {
		return chiaveNSC;
	}

	/**
	 * @param chiaveNSC
	 *            the chiaveNSC to set
	 */
	public void setChiaveNSC(String chiaveNSC) {
		this.chiaveNSC = chiaveNSC;
	}

	/**
	 * @return the chiaveSIES
	 */
	public String getChiaveSIES() {
		return chiaveSIES;
	}

	/**
	 * @param chiaveSIES
	 *            the chiaveSIES to set
	 */
	public void setChiaveSIES(String chiaveSIES) {
		this.chiaveSIES = chiaveSIES;
	}

}