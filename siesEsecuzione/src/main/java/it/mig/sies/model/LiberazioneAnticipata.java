package it.mig.sies.model;

import java.io.Serial;

public class LiberazioneAnticipata extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 3779243118930410207L;

	private int totale;
	private int anticipata;
	private int speciale;
	private int integrazione;

	public int getTotale() {
		return totale;
	}

	public void setTotale(int totale) {
		this.totale = totale;
	}

	public int getAnticipata() {
		return anticipata;
	}

	public void setAnticipata(int anticipata) {
		this.anticipata = anticipata;
	}

	public int getSpeciale() {
		return speciale;
	}

	public void setSpeciale(int speciale) {
		this.speciale = speciale;
	}

	public int getIntegrazione() {
		return integrazione;
	}

	public void setIntegrazione(int integrazione) {
		this.integrazione = integrazione;
	}

}