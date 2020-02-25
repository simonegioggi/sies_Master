package siap.siep.richiesta.model;

/**
 * <p>Title: </p>
 * <p>Description: Model di un documento che dovrà essere stampato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.Date;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class RichiestaModel extends GenericModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5335461138702476615L;
	String SedeSiep = "";
	String AutoritaDestinatario = "";
	String AutoritaSede = "";
	String CampoLibero = "";
	String DataOdierna = "";
	Date mDataEmissione;

	public RichiestaModel() {
		SedeSiep = "";
		AutoritaDestinatario = "";
		AutoritaSede = "";
		CampoLibero = "";
		DataOdierna = DateUtils.getSysDate("dd-MM-yyyy");
		mDataEmissione = null;
	}

	public String getSedeSiep() {
		return SedeSiep;
	}

	public String getAutoritaDestinatario() {
		return AutoritaDestinatario;
	}

	public String AutoritaSede() {
		return AutoritaSede;
	}

	public String getCampoLibero() {
		return CampoLibero;
	}

	public String getDataOdierna() {
		return DataOdierna;
	}

	public Date getDataEmissione() {
		return mDataEmissione;
	}

	public void setSedeSiep(String aValore) {
		SedeSiep = aValore;
	}

	public void setAutoritaDestinatario(String aValore) {
		AutoritaDestinatario = aValore;
	}

	public void setAutoritaSede(String aValore) {
		AutoritaSede = aValore;
	}

	public void setCampoLibero(String aValore) {
		CampoLibero = aValore;
	}

	public void setDataEmissione(Date aValore) {
		mDataEmissione = aValore;
	}

}
