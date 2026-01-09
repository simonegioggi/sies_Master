package it.mig.sies.type.esecuzione_NEW;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Ufficio&quot;/&gt;
 *         &lt;element name=&quot;Username&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;128&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;Cognome&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;35&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;Nome&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;35&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;ipServer&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "ufficio", "username", "cognome", "nome",
		"ipServer" })
@XmlRootElement(name = "Utente")
public class Utente {

	@XmlElement(name = "Ufficio", required = true)
	protected Ufficio ufficio;
	@XmlElement(name = "Username", required = true)
	protected String username;
	@XmlElement(name = "Cognome", required = true)
	protected String cognome;
	@XmlElement(name = "Nome", required = true)
	protected String nome;
	@XmlElement(required = true, nillable = true)
	protected String ipServer;

	/**
	 * Gets the value of the ufficio property.
	 * 
	 * @return possible object is {@link Ufficio }
	 * 
	 */
	public Ufficio getUfficio() {
		return ufficio;
	}

	/**
	 * Sets the value of the ufficio property.
	 * 
	 * @param value
	 *            allowed object is {@link Ufficio }
	 * 
	 */
	public void setUfficio(Ufficio value) {
		this.ufficio = value;
	}

	/**
	 * Gets the value of the username property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the value of the username property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUsername(String value) {
		this.username = value;
	}

	/**
	 * Gets the value of the cognome property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Sets the value of the cognome property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCognome(String value) {
		this.cognome = value;
	}

	/**
	 * Gets the value of the nome property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the value of the nome property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNome(String value) {
		this.nome = value;
	}

	/**
	 * Gets the value of the ipServer property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIpServer() {
		return ipServer;
	}

	/**
	 * Sets the value of the ipServer property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIpServer(String value) {
		this.ipServer = value;
	}

}
