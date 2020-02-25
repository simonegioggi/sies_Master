package siap.sius.statistiche.model;

import java.util.Date;

import f3b.model.GenericModel;
import siap.sico.utente.model.UtenteModel;

public class RicercaAggregatiCognomeModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2685372890662600417L;
	private Date mDataInizio = null;
	private Date mDataFine = null;
	private UtenteModel mUtenteConnesso = null;

	public RicercaAggregatiCognomeModel() {

	}

	public RicercaAggregatiCognomeModel(RicercaAggregatiCognomeModel aModel) {
		this.mDataInizio = aModel.getDataInizio();
		this.mDataFine = aModel.getDataFine();
		this.mUtenteConnesso = aModel.getUtenteConnesso();
	}

	public Date getDataInizio() {
		return mDataInizio;
	}

	public void setDataInizio(Date aDataInizio) {
		this.mDataInizio = aDataInizio;
	}

	public Date getDataFine() {
		return mDataFine;
	}

	public void setDataFine(Date aDataFine) {
		this.mDataFine = aDataFine;
	}

	public UtenteModel getUtenteConnesso() {
		return mUtenteConnesso;
	}

	public void setUtenteConnesso(UtenteModel aUtenteConnesso) {
		this.mUtenteConnesso = aUtenteConnesso;
	}

}
