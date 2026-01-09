package it.mig.sies.model;

/**
 * MEV 23010 - Classe model relativa ai dettagli di una singola tipologia di provvedimento
 * 
 * @author Federico Paparoni
 */
public class DescrizioneProvvedimento extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2154605373686507209L;

	private String codiceUnivoco;
	private String oggetto;
	private String motivo;
	private String esito;

	public DescrizioneProvvedimento() {
	}

	public DescrizioneProvvedimento(String codiceUnivoco) {
		this.codiceUnivoco = codiceUnivoco;
	}

	/**
	 * @return the codiceUnivoco
	 */
	public String getCodiceUnivoco() {
		return codiceUnivoco;
	}

	/**
	 * @param codiceUnivoco
	 *            the codiceUnivoco to set
	 */
	public void setCodiceUnivoco(String codiceUnivoco) {
		this.codiceUnivoco = codiceUnivoco;
	}

	/**
	 * @return the oggetto
	 */
	public String getOggetto() {
		return oggetto;
	}

	/**
	 * @param oggetto
	 *            the oggetto to set
	 */
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * @return the motivo
	 */
	public String getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo
	 *            the motivo to set
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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

}