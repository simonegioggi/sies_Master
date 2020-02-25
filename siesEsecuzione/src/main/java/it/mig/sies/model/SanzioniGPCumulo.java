package it.mig.sies.model;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le richieste GE associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class SanzioniGPCumulo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 890753394083108678L;

	private int giorniPermanenzaDomiciliare;
	private int mesiPermanenzaDomiciliare;
	private int anniPermanenzaDomiciliare;
	private int giorniLavoroPubblicaUtilita;
	private int mesiLavoroPubblicaUtilita;
	private int anniLavoroPubblicaUtilita;
	private int giorniLavoroSostitutivo;
	private int mesiLavoroSostitutivo;
	private int anniLavoroSostitutivo;
	private int giorniEspulsioneStato;
	private int mesiEspulsioneStato;
	private int anniEspulsioneStato;
	private String codiceTipoEspulsioneStato;

	/**
	 * @return the giorniPermanenzaDomiciliare
	 */
	public int getGiorniPermanenzaDomiciliare() {
		return giorniPermanenzaDomiciliare;
	}

	/**
	 * @param giorniPermanenzaDomiciliare
	 *            the giorniPermanenzaDomiciliare to set
	 */
	public void setGiorniPermanenzaDomiciliare(int giorniPermanenzaDomiciliare) {
		this.giorniPermanenzaDomiciliare = giorniPermanenzaDomiciliare;
	}

	/**
	 * @return the mesiPermanenzaDomiciliare
	 */
	public int getMesiPermanenzaDomiciliare() {
		return mesiPermanenzaDomiciliare;
	}

	/**
	 * @param mesiPermanenzaDomiciliare
	 *            the mesiPermanenzaDomiciliare to set
	 */
	public void setMesiPermanenzaDomiciliare(int mesiPermanenzaDomiciliare) {
		this.mesiPermanenzaDomiciliare = mesiPermanenzaDomiciliare;
	}

	/**
	 * @return the anniPermanenzaDomiciliare
	 */
	public int getAnniPermanenzaDomiciliare() {
		return anniPermanenzaDomiciliare;
	}

	/**
	 * @param anniPermanenzaDomiciliare
	 *            the anniPermanenzaDomiciliare to set
	 */
	public void setAnniPermanenzaDomiciliare(int anniPermanenzaDomiciliare) {
		this.anniPermanenzaDomiciliare = anniPermanenzaDomiciliare;
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
	 * @return the giorniLavoroSostitutivo
	 */
	public int getGiorniLavoroSostitutivo() {
		return giorniLavoroSostitutivo;
	}

	/**
	 * @param giorniLavoroSostitutivo
	 *            the giorniLavoroSostitutivo to set
	 */
	public void setGiorniLavoroSostitutivo(int giorniLavoroSostitutivo) {
		this.giorniLavoroSostitutivo = giorniLavoroSostitutivo;
	}

	/**
	 * @return the mesiLavoroSostitutivo
	 */
	public int getMesiLavoroSostitutivo() {
		return mesiLavoroSostitutivo;
	}

	/**
	 * @param mesiLavoroSostitutivo
	 *            the mesiLavoroSostitutivo to set
	 */
	public void setMesiLavoroSostitutivo(int mesiLavoroSostitutivo) {
		this.mesiLavoroSostitutivo = mesiLavoroSostitutivo;
	}

	/**
	 * @return the anniLavoroSostitutivo
	 */
	public int getAnniLavoroSostitutivo() {
		return anniLavoroSostitutivo;
	}

	/**
	 * @param anniLavoroSostitutivo
	 *            the anniLavoroSostitutivo to set
	 */
	public void setAnniLavoroSostitutivo(int anniLavoroSostitutivo) {
		this.anniLavoroSostitutivo = anniLavoroSostitutivo;
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

}