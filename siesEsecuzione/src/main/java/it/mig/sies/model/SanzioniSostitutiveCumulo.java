package it.mig.sies.model;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le richieste GE associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class SanzioniSostitutiveCumulo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1214029466827442933L;

	private int giorniSemidetenzione;
	private int mesiSemidetenzione;
	private int anniSemidetenzione;
	private int giorniLibertaControllata;
	private int mesiLibertaControllata;
	private int anniLibertaControllata;
	private double importoAmmenda;
	private double importoMulta;
	private int giorniEspulsioneStato;
	private int mesiEspulsioneStato;
	private int anniEspulsioneStato;
	private String codiceTipoEspulsioneStato;
	private int giorniLavoroPubblicaUtilita;
	private int mesiLavoroPubblicaUtilita;
	private int anniLavoroPubblicaUtilita;
	private int oreLavoroPubblicaUtilita;
	private String codTipoLavoroPubblicaUtilita;

	/**
	 * @return the giorniSemidetenzione
	 */
	public int getGiorniSemidetenzione() {
		return giorniSemidetenzione;
	}

	/**
	 * @param giorniSemidetenzione
	 *            the giorniSemidetenzione to set
	 */
	public void setGiorniSemidetenzione(int giorniSemidetenzione) {
		this.giorniSemidetenzione = giorniSemidetenzione;
	}

	/**
	 * @return the mesiSemidetenzione
	 */
	public int getMesiSemidetenzione() {
		return mesiSemidetenzione;
	}

	/**
	 * @param mesiSemidetenzione
	 *            the mesiSemidetenzione to set
	 */
	public void setMesiSemidetenzione(int mesiSemidetenzione) {
		this.mesiSemidetenzione = mesiSemidetenzione;
	}

	/**
	 * @return the anniSemidetenzione
	 */
	public int getAnniSemidetenzione() {
		return anniSemidetenzione;
	}

	/**
	 * @param anniSemidetenzione
	 *            the anniSemidetenzione to set
	 */
	public void setAnniSemidetenzione(int anniSemidetenzione) {
		this.anniSemidetenzione = anniSemidetenzione;
	}

	/**
	 * @return the giorniLibertaControllata
	 */
	public int getGiorniLibertaControllata() {
		return giorniLibertaControllata;
	}

	/**
	 * @param giorniLibertaControllata
	 *            the giorniLibertaControllata to set
	 */
	public void setGiorniLibertaControllata(int giorniLibertaControllata) {
		this.giorniLibertaControllata = giorniLibertaControllata;
	}

	/**
	 * @return the mesiLibertaControllata
	 */
	public int getMesiLibertaControllata() {
		return mesiLibertaControllata;
	}

	/**
	 * @param mesiLibertaControllata
	 *            the mesiLibertaControllata to set
	 */
	public void setMesiLibertaControllata(int mesiLibertaControllata) {
		this.mesiLibertaControllata = mesiLibertaControllata;
	}

	/**
	 * @return the anniLibertaControllata
	 */
	public int getAnniLibertaControllata() {
		return anniLibertaControllata;
	}

	/**
	 * @param anniLibertaControllata
	 *            the anniLibertaControllata to set
	 */
	public void setAnniLibertaControllata(int anniLibertaControllata) {
		this.anniLibertaControllata = anniLibertaControllata;
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

	/**
	 * @return the giorniEspulsioneStato
	 */
	public int getGiorniEspulsioneStato() {
		return giorniEspulsioneStato;
	}

	/**
	 * @param giorniEspulsioneStato
	 *            the giorniEspulsioneStato to set
	 */
	public void setGiorniEspulsioneStato(int giorniEspulsioneStato) {
		this.giorniEspulsioneStato = giorniEspulsioneStato;
	}

	/**
	 * @return the mesiEspulsioneStato
	 */
	public int getMesiEspulsioneStato() {
		return mesiEspulsioneStato;
	}

	/**
	 * @param mesiEspulsioneStato
	 *            the mesiEspulsioneStato to set
	 */
	public void setMesiEspulsioneStato(int mesiEspulsioneStato) {
		this.mesiEspulsioneStato = mesiEspulsioneStato;
	}

	/**
	 * @return the anniEspulsioneStato
	 */
	public int getAnniEspulsioneStato() {
		return anniEspulsioneStato;
	}

	/**
	 * @param anniEspulsioneStato
	 *            the anniEspulsioneStato to set
	 */
	public void setAnniEspulsioneStato(int anniEspulsioneStato) {
		this.anniEspulsioneStato = anniEspulsioneStato;
	}

	/**
	 * @return the codiceTipoEspulsioneStato
	 */
	public String getCodiceTipoEspulsioneStato() {
		return codiceTipoEspulsioneStato;
	}

	/**
	 * @param codiceTipoEspulsioneStato
	 *            the codiceTipoEspulsioneStato to set
	 */
	public void setCodiceTipoEspulsioneStato(String codiceTipoEspulsioneStato) {
		this.codiceTipoEspulsioneStato = codiceTipoEspulsioneStato;
	}

	/**
	 * @return the giorniLavoroPubblicaUtilita
	 */
	public int getGiorniLavoroPubblicaUtilita() {
		return giorniLavoroPubblicaUtilita;
	}

	/**
	 * @param giorniLavoroPubblicaUtilita
	 *            the giorniLavoroPubblicaUtilita to set
	 */
	public void setGiorniLavoroPubblicaUtilita(int giorniLavoroPubblicaUtilita) {
		this.giorniLavoroPubblicaUtilita = giorniLavoroPubblicaUtilita;
	}

	/**
	 * @return the mesiLavoroPubblicaUtilita
	 */
	public int getMesiLavoroPubblicaUtilita() {
		return mesiLavoroPubblicaUtilita;
	}

	/**
	 * @param mesiLavoroPubblicaUtilita
	 *            the mesiLavoroPubblicaUtilita to set
	 */
	public void setMesiLavoroPubblicaUtilita(int mesiLavoroPubblicaUtilita) {
		this.mesiLavoroPubblicaUtilita = mesiLavoroPubblicaUtilita;
	}

	/**
	 * @return the anniLavoroPubblicaUtilita
	 */
	public int getAnniLavoroPubblicaUtilita() {
		return anniLavoroPubblicaUtilita;
	}

	/**
	 * @param anniLavoroPubblicaUtilita
	 *            the anniLavoroPubblicaUtilita to set
	 */
	public void setAnniLavoroPubblicaUtilita(int anniLavoroPubblicaUtilita) {
		this.anniLavoroPubblicaUtilita = anniLavoroPubblicaUtilita;
	}

	/**
	 * @return the oreLavoroPubblicaUtilita
	 */
	public int getOreLavoroPubblicaUtilita() {
		return oreLavoroPubblicaUtilita;
	}

	/**
	 * @param oreLavoroPubblicaUtilita
	 *            the oreLavoroPubblicaUtilita to set
	 */
	public void setOreLavoroPubblicaUtilita(int oreLavoroPubblicaUtilita) {
		this.oreLavoroPubblicaUtilita = oreLavoroPubblicaUtilita;
	}

	/**
	 * @return the codTipoLavoroPubblicaUtilita
	 */
	public String getCodTipoLavoroPubblicaUtilita() {
		return codTipoLavoroPubblicaUtilita;
	}

	/**
	 * @param codTipoLavoroPubblicaUtilita
	 *            the codTipoLavoroPubblicaUtilita to set
	 */
	public void setCodTipoLavoroPubblicaUtilita(String codTipoLavoroPubblicaUtilita) {
		this.codTipoLavoroPubblicaUtilita = codTipoLavoroPubblicaUtilita;
	}

}