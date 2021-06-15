package it.mig.sies.model;

/**
 * SIES FASE 2 - Classe model relativa all'utente che sta effettuando il trasferimento
 *
 * @author Federico Paparoni
 */
public class Utente extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1479133706548534297L;

	private String codiceTipoUfficio;
	private String sedeUfficio;
	private String sedeDistretto;
	private String codiceSistema;
	private String username;
	private String cognome;
	private String nome;
	private String ipServer;
	// MEV INTEGRAZIONE SIES ADN: aggiunta variabile
	private String userAdn;

	/**
	 * @return the codiceTipoUfficio
	 */
	public String getCodiceTipoUfficio() {
		return codiceTipoUfficio;
	}

	/**
	 * @param codiceTipoUfficio
	 *            the codiceTipoUfficio to set
	 */
	public void setCodiceTipoUfficio(String codiceTipoUfficio) {
		this.codiceTipoUfficio = codiceTipoUfficio;
	}

	/**
	 * @return the sedeUfficio
	 */
	public String getSedeUfficio() {
		return sedeUfficio;
	}

	/**
	 * @param sedeUfficio
	 *            the sedeUfficio to set
	 */
	public void setSedeUfficio(String sedeUfficio) {
		this.sedeUfficio = sedeUfficio;
	}

	/**
	 * @return the sedeDistretto
	 */
	public String getSedeDistretto() {
		return sedeDistretto;
	}

	/**
	 * @param sedeDistretto
	 *            the sedeDistretto to set
	 */
	public void setSedeDistretto(String sedeDistretto) {
		this.sedeDistretto = sedeDistretto;
	}

	/**
	 * @return the codiceSistema
	 */
	public String getCodiceSistema() {
		return codiceSistema;
	}

	/**
	 * @param codiceSistema
	 *            the codiceSistema to set
	 */
	public void setCodiceSistema(String codiceSistema) {
		this.codiceSistema = codiceSistema;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @param cognome
	 *            the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the ipServer
	 */
	public String getIpServer() {
		return ipServer;
	}

	/**
	 * @param ipServer
	 *            the ipServer to set
	 */
	public void setIpServer(String ipServer) {
		this.ipServer = ipServer;
	}

	public String getUserAdn() {
		return userAdn;
	}

	public void setUserAdn(String userAdn) {
		this.userAdn = userAdn;
	}

}