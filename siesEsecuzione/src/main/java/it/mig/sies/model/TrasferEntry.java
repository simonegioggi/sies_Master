package it.mig.sies.model;

import java.util.List;

/**
 * SIES FASE 2 - Classe model relativa alla singola entry trasferita in modalitï¿½ massiva
 * 
 * @author Federico Paparoni
 */
public class TrasferEntry extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 434994928857888686L;

	private long idEvento;
	private long numeroProtocollo;
	private int annoProtocollo;
	private long responseId;
	private boolean completed;
	private boolean estratto;
	private String esito;
	// MEV 31: aggiunta variabile per gestire la sinonimia
	private List<Sinonimo> elencoSinonimi;

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
	 * @return the numeroProtocollo
	 */
	public long getNumeroProtocollo() {
		return numeroProtocollo;
	}

	/**
	 * @param numeroProtocollo
	 *            the numeroProtocollo to set
	 */
	public void setNumeroProtocollo(long numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	/**
	 * @return the annoProtocollo
	 */
	public int getAnnoProtocollo() {
		return annoProtocollo;
	}

	/**
	 * @param annoProtocollo
	 *            the annoProtocollo to set
	 */
	public void setAnnoProtocollo(int annoProtocollo) {
		this.annoProtocollo = annoProtocollo;
	}

	/**
	 * @return the responseId
	 */
	public long getResponseId() {
		return responseId;
	}

	/**
	 * @param responseId
	 *            the responseId to set
	 */
	public void setResponseId(long responseId) {
		this.responseId = responseId;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the estratto
	 */
	public boolean isEstratto() {
		return estratto;
	}

	/**
	 * @param estratto
	 *            the estratto to set
	 */
	public void setEstratto(boolean estratto) {
		this.estratto = estratto;
	}

	/**
	 * @return the esito
	 */
	public String getEsito() {
		return esito;
	}

	/**
	 * @param esito
	 *            the esito to set
	 */
	public void setEsito(String esito) {
		this.esito = esito;
	}

	/**
	 * @return the elencoSinonimi
	 */
	public List<Sinonimo> getElencoSinonimi() {
		return elencoSinonimi;
	}

	/**
	 * @param elencoSinonimi
	 *            the elencoSinonimi to set
	 */
	public void setElencoSinonimi(List<Sinonimo> elencoSinonimi) {
		this.elencoSinonimi = elencoSinonimi;
	}

}