package it.mig.sies.model;

/**
 * SIES FASE 2 - Classe model relativa a vari dati presenti in un fascicolo
 * 
 * @author Federico Paparoni
 */
public class DettagliFascicolo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8908935180568126456L;

	private long numeroSentenza;
	private int annoSentenza;
	private long numeroOrdinanza;
	private int annoOrdinanza;
	private int annoSiep;
	private long numeroSiep;
	private int annoSius;
	private long numeroSius;
	private String sedePm;
	// MEV 16: aggiunte variabili
	private String flagCumulante;
	private String codMotivo;

	/**
	 * @return the numeroSentenza
	 */
	public long getNumeroSentenza() {
		return numeroSentenza;
	}

	/**
	 * @param numeroSentenza
	 *            the numeroSentenza to set
	 */
	public void setNumeroSentenza(long numeroSentenza) {
		this.numeroSentenza = numeroSentenza;
	}

	/**
	 * @return the annoSentenza
	 */
	public int getAnnoSentenza() {
		return annoSentenza;
	}

	/**
	 * @param annoSentenza
	 *            the annoSentenza to set
	 */
	public void setAnnoSentenza(int annoSentenza) {
		this.annoSentenza = annoSentenza;
	}

	/**
	 * @return the numeroOrdinanza
	 */
	public long getNumeroOrdinanza() {
		return numeroOrdinanza;
	}

	/**
	 * @param numeroOrdinanza
	 *            the numeroOrdinanza to set
	 */
	public void setNumeroOrdinanza(long numeroOrdinanza) {
		this.numeroOrdinanza = numeroOrdinanza;
	}

	/**
	 * @return the annoOrdinanza
	 */
	public int getAnnoOrdinanza() {
		return annoOrdinanza;
	}

	/**
	 * @param annoOrdinanza
	 *            the annoOrdinanza to set
	 */
	public void setAnnoOrdinanza(int annoOrdinanza) {
		this.annoOrdinanza = annoOrdinanza;
	}

	/**
	 * @return the annoSiep
	 */
	public int getAnnoSiep() {
		return annoSiep;
	}

	/**
	 * @param annoSiep
	 *            the annoSiep to set
	 */
	public void setAnnoSiep(int annoSiep) {
		this.annoSiep = annoSiep;
	}

	/**
	 * @return the numeroSiep
	 */
	public long getNumeroSiep() {
		return numeroSiep;
	}

	/**
	 * @param numeroSiep
	 *            the numeroSiep to set
	 */
	public void setNumeroSiep(long numeroSiep) {
		this.numeroSiep = numeroSiep;
	}

	/**
	 * @return the annoSius
	 */
	public int getAnnoSius() {
		return annoSius;
	}

	/**
	 * @param annoSius
	 *            the annoSius to set
	 */
	public void setAnnoSius(int annoSius) {
		this.annoSius = annoSius;
	}

	/**
	 * @return the numeroSius
	 */
	public long getNumeroSius() {
		return numeroSius;
	}

	/**
	 * @param numeroSius
	 *            the numeroSius to set
	 */
	public void setNumeroSius(long numeroSius) {
		this.numeroSius = numeroSius;
	}

	/**
	 * @return the sedePm
	 */
	public String getSedePm() {
		return sedePm;
	}

	/**
	 * @param sedePm
	 *            the sedePm to set
	 */
	public void setSedePm(String sedePm) {
		this.sedePm = sedePm;
	}

	/**
	 * @return the flagCumulante
	 */
	public String getFlagCumulante() {
		return flagCumulante;
	}

	/**
	 * @param flagCumulante
	 *            the flagCumulante to set
	 */
	public void setFlagCumulante(String flagCumulante) {
		this.flagCumulante = flagCumulante;
	}

	/**
	 * @return the codMotivo
	 */
	public String getCodMotivo() {
		return codMotivo;
	}

	/**
	 * @param codMotivo
	 *            the codMotivo to set
	 */
	public void setCodMotivo(String codMotivo) {
		this.codMotivo = codMotivo;
	}

}