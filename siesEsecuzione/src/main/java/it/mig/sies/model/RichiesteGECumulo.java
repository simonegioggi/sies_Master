package it.mig.sies.model;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le richieste GE associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class RichiesteGECumulo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5352424444226618275L;

	private String codiceTipoRichiesta;
	private int giorniArresto;
	private int mesiArresto;
	private int anniArresto;
	private int giorniReclusione;
	private int mesiReclusione;
	private int anniReclusione;
	private double importoAmmenda;
	private double importoMulta;

	/**
	 * @return the codiceTipoRichiesta
	 */
	public String getCodiceTipoRichiesta() {
		return codiceTipoRichiesta;
	}

	/**
	 * @param codiceTipoRichiesta
	 *            the codiceTipoRichiesta to set
	 */
	public void setCodiceTipoRichiesta(String codiceTipoRichiesta) {
		this.codiceTipoRichiesta = codiceTipoRichiesta;
	}

	/**
	 * @return the giorniArresto
	 */
	public int getGiorniArresto() {
		return giorniArresto;
	}

	/**
	 * @param giorniArresto
	 *            the giorniArresto to set
	 */
	public void setGiorniArresto(int giorniArresto) {
		this.giorniArresto = giorniArresto;
	}

	/**
	 * @return the mesiArresto
	 */
	public int getMesiArresto() {
		return mesiArresto;
	}

	/**
	 * @param mesiArresto
	 *            the mesiArresto to set
	 */
	public void setMesiArresto(int mesiArresto) {
		this.mesiArresto = mesiArresto;
	}

	/**
	 * @return the anniArresto
	 */
	public int getAnniArresto() {
		return anniArresto;
	}

	/**
	 * @param anniArresto
	 *            the anniArresto to set
	 */
	public void setAnniArresto(int anniArresto) {
		this.anniArresto = anniArresto;
	}

	/**
	 * @return the giorniReclusione
	 */
	public int getGiorniReclusione() {
		return giorniReclusione;
	}

	/**
	 * @param giorniReclusione
	 *            the giorniReclusione to set
	 */
	public void setGiorniReclusione(int giorniReclusione) {
		this.giorniReclusione = giorniReclusione;
	}

	/**
	 * @return the mesiReclusione
	 */
	public int getMesiReclusione() {
		return mesiReclusione;
	}

	/**
	 * @param mesiReclusione
	 *            the mesiReclusione to set
	 */
	public void setMesiReclusione(int mesiReclusione) {
		this.mesiReclusione = mesiReclusione;
	}

	/**
	 * @return the anniReclusione
	 */
	public int getAnniReclusione() {
		return anniReclusione;
	}

	/**
	 * @param anniReclusione
	 *            the anniReclusione to set
	 */
	public void setAnniReclusione(int anniReclusione) {
		this.anniReclusione = anniReclusione;
	}

	/**
	 * @return the importoAmmenda
	 */
	public double getImportoAmmenda() {
		return importoAmmenda;
	}

	/**
	 * @param importoAmmenda
	 *            the importoAmmenda to set
	 */
	public void setImportoAmmenda(double importoAmmenda) {
		this.importoAmmenda = importoAmmenda;
	}

	/**
	 * @return the importoMulta
	 */
	public double getImportoMulta() {
		return importoMulta;
	}

	/**
	 * @param importoMulta
	 *            the importoMulta to set
	 */
	public void setImportoMulta(double importoMulta) {
		this.importoMulta = importoMulta;
	}

}