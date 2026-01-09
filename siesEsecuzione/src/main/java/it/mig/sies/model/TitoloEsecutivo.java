package it.mig.sies.model;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SIES FASE 2 - Classe model relativa al titolo esecutivo
 * 
 * @author Federico Paparoni
 */
public class TitoloEsecutivo extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 6554647573890038333L;

	private long idEvento;
	private int annoFascicolo;
	private long numeroFascicolo;
	private String tipologiaUfficio;
	private Date dataEmissione;
	private String comuneUfficio;
	private String output;

	/**
	 * @return the idEvento
	 */
	public long getIdEvento() {
		return idEvento;
	}

	/**
	 * @param idEvento
	 *            the idEvento to set
	 */
	public void setIdEvento(long idEvento) {
		this.idEvento = idEvento;
	}

	/**
	 * @return the annoFascicolo
	 */
	public int getAnnoFascicolo() {
		return annoFascicolo;
	}

	/**
	 * @param annoFascicolo
	 *            the annoFascicolo to set
	 */
	public void setAnnoFascicolo(int annoFascicolo) {
		this.annoFascicolo = annoFascicolo;
	}

	/**
	 * @return the numeroFascicolo
	 */
	public long getNumeroFascicolo() {
		return numeroFascicolo;
	}

	/**
	 * @param numeroFascicolo
	 *            the numeroFascicolo to set
	 */
	public void setNumeroFascicolo(long numeroFascicolo) {
		this.numeroFascicolo = numeroFascicolo;
	}

	/**
	 * @return the tipologiaUfficio
	 */
	public String getTipologiaUfficio() {
		return tipologiaUfficio;
	}

	/**
	 * @param tipologiaUfficio
	 *            the tipologiaUfficio to set
	 */
	public void setTipologiaUfficio(String tipologiaUfficio) {
		this.tipologiaUfficio = tipologiaUfficio;
	}

	/**
	 * @return the dataEmissione
	 */
	public Date getDataEmissione() {
		return dataEmissione;
	}

	/**
	 * @param dataEmissione
	 *            the dataEmissione to set
	 */
	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	/**
	 * @return the comuneUfficio
	 */
	public String getComuneUfficio() {
		return comuneUfficio;
	}

	/**
	 * @param comuneUfficio
	 *            the comuneUfficio to set
	 */
	public void setComuneUfficio(String comuneUfficio) {
		this.comuneUfficio = comuneUfficio;
	}

	/**
	 * Richiamato in fase di visualizzazione
	 * 
	 * @return the output
	 */
	public String getOutput() {
		String tipologia;
		if (tipologiaUfficio.equals("UDS"))
			tipologia = "MAGISTRATO DI SORVEGLIANZA";
		else if (tipologiaUfficio.equals("TDS"))
			tipologia = "TRIBUNALE DI SORVEGLIANZA";
		// MEV 16: aggiunta nuova casistica per "PM"
		else
			tipologia = "PUBBLICO MINISTERO";

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		output = format.format(dataEmissione) + " " + tipologia + " " + comuneUfficio;
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * MEV 16 CUMULO
	 * 
	 * @return Ritorna la data di emissione nel formato gg/mm/aaaa
	 */
	public String getDataEmissioneFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// valore di ritorno
		return dateFormat.format(dataEmissione);
	}

}