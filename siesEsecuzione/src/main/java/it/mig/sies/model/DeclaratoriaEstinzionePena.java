package it.mig.sies.model;

public class DeclaratoriaEstinzionePena extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3851529585949879185L;

	private int totale;
	private int tipo_a;
	private int tipo_b;

	public int getTotale() {
		return totale;
	}

	public void setTotale(int totale) {
		this.totale = totale;
	}

	public int getTipo_a() {
		return tipo_a;
	}

	public void setTipo_a(int tipo_a) {
		this.tipo_a = tipo_a;
	}

	public int getTipo_b() {
		return tipo_b;
	}

	public void setTipo_b(int tipo_b) {
		this.tipo_b = tipo_b;
	}

}