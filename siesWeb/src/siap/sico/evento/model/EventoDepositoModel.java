package siap.sico.evento.model;

/**
* <p>Title: EventoModel</p>
* <p>Description: Classe Model che rappresenta il Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.util.Date;

public class EventoDepositoModel extends EventoModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3606901787976206437L;

	private Date mDataDeposito = null;
	private String mLegge = null;

	// COSTRUTTORE DI DEFAULT
	public EventoDepositoModel() {
		super();
		mDataDeposito = null;
		mLegge = null;
	}

	// COSTRUTTORI DI COPIA
	public EventoDepositoModel(EventoModel aModel) {
		super(aModel);
		mDataDeposito = null;
		mLegge = aModel.getLegge();
	}

	public EventoDepositoModel(EventoDepositoModel aModel) {
		super(aModel);
		mDataDeposito = aModel.mDataDeposito;
		mLegge = aModel.mLegge;
	}

	public EventoDepositoModel(EventoModel aModel, Date aData) {
		super(aModel);
		mDataDeposito = aData;
	}

	//
	// METODI GET() e SET
	//
	public Date getDataDeposito() {
		return mDataDeposito;
	}

	public void setDataDeposito(Date aValore) {
		mDataDeposito = aValore;
	}

	public String getLegge() {
		return mLegge;
	}

	public void setLegge(String aValore) {
		mLegge = aValore;
	}

	public String toString() {
		String lStr = new String();

		lStr = "EventoDepositoModel:\n"
				+ "[ EventoModel	= " + super.toString() + " ]\n"
				+ "[ mDataDeposito	= " + mDataDeposito + " ]\n"
				+ "[ mLegge			= " + mLegge + " ]";

		return lStr;
	}

}