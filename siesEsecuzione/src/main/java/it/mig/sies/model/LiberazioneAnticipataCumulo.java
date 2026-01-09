package it.mig.sies.model;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le richieste GE associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class LiberazioneAnticipataCumulo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5750692086979148883L;

	private int giorniLAOrdinaria;
	private int giorniLASpeciale;
	private int giorniLAIntegrazione;
	private int giorniLARisarcimento;

	/**
	 * @return the giorniLAOrdinaria
	 */
	public int getGiorniLAOrdinaria() {
		return giorniLAOrdinaria;
	}

	/**
	 * @param giorniLAOrdinaria
	 *            the giorniLAOrdinaria to set
	 */
	public void setGiorniLAOrdinaria(int giorniLAOrdinaria) {
		this.giorniLAOrdinaria = giorniLAOrdinaria;
	}

	/**
	 * @return the giorniLASpeciale
	 */
	public int getGiorniLASpeciale() {
		return giorniLASpeciale;
	}

	/**
	 * @param giorniLASpeciale
	 *            the giorniLASpeciale to set
	 */
	public void setGiorniLASpeciale(int giorniLASpeciale) {
		this.giorniLASpeciale = giorniLASpeciale;
	}

	/**
	 * @return the giorniLAIntegrazione
	 */
	public int getGiorniLAIntegrazione() {
		return giorniLAIntegrazione;
	}

	/**
	 * @param giorniLAIntegrazione
	 *            the giorniLAIntegrazione to set
	 */
	public void setGiorniLAIntegrazione(int giorniLAIntegrazione) {
		this.giorniLAIntegrazione = giorniLAIntegrazione;
	}

	/**
	 * @return the giorniLARisarcimento
	 */
	public int getGiorniLARisarcimento() {
		return giorniLARisarcimento;
	}

	/**
	 * @param giorniLARisarcimento
	 *            the giorniLARisarcimento to set
	 */
	public void setGiorniLARisarcimento(int giorniLARisarcimento) {
		this.giorniLARisarcimento = giorniLARisarcimento;
	}

}