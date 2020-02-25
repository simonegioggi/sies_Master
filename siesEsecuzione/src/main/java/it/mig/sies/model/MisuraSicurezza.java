package it.mig.sies.model;

public class MisuraSicurezza extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7292154860308819671L;

	private int giorni;
	private int mesi;
	private int anni;
	private int giorniOld;
	private int mesiOld;
	private int anniOld;
	private String codice;
	private String codiceOld;

	/**
	 * @return the codiceOld
	 */
	public String getCodiceOld() {
		return codiceOld;
	}

	/**
	 * @param codiceOld
	 *            the codiceOld to set
	 */
	public void setCodiceOld(String codiceOld) {
		this.codiceOld = codiceOld;
	}

	/**
	 * @return the giorni
	 */
	public int getGiorni() {
		return giorni;
	}

	/**
	 * @param giorni
	 *            the giorni to set
	 */
	public void setGiorni(int giorni) {
		this.giorni = giorni;
	}

	/**
	 * @return the mesi
	 */
	public int getMesi() {
		return mesi;
	}

	/**
	 * @param mesi
	 *            the mesi to set
	 */
	public void setMesi(int mesi) {
		this.mesi = mesi;
	}

	/**
	 * @return the anni
	 */
	public int getAnni() {
		return anni;
	}

	/**
	 * @param anni
	 *            the anni to set
	 */
	public void setAnni(int anni) {
		this.anni = anni;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return the giorniOld
	 */
	public int getGiorniOld() {
		return giorniOld;
	}

	/**
	 * @param giorniOld
	 *            the giorniOld to set
	 */
	public void setGiorniOld(int giorniOld) {
		this.giorniOld = giorniOld;
	}

	/**
	 * @return the mesiOld
	 */
	public int getMesiOld() {
		return mesiOld;
	}

	/**
	 * @param mesiOld
	 *            the mesiOld to set
	 */
	public void setMesiOld(int mesiOld) {
		this.mesiOld = mesiOld;
	}

	/**
	 * @return the anniOld
	 */
	public int getAnniOld() {
		return anniOld;
	}

	/**
	 * @param anniOld
	 *            the anniOld to set
	 */
	public void setAnniOld(int anniOld) {
		this.anniOld = anniOld;
	}

}