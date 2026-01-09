package it.mig.sies.type.esecuzione_NEW;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name=&quot;codiceTipo&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;10&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceSede&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;6&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceDistretto&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;6&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceSistema&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;4&quot;/&gt;
 *               &lt;enumeration value=&quot;SIGE&quot;/&gt;
 *               &lt;enumeration value=&quot;SIUS&quot;/&gt;
 *               &lt;enumeration value=&quot;SIEP&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "codiceTipo", "codiceSede",
		"codiceDistretto", "codiceSistema" })
@XmlRootElement(name = "Ufficio")
public class Ufficio {

	@XmlElement(required = true)
	protected String codiceTipo;
	@XmlElement(required = true)
	protected String codiceSede;
	@XmlElement(required = true)
	protected String codiceDistretto;
	@XmlElement(required = true)
	protected String codiceSistema;

	/**
	 * Gets the value of the codiceTipo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceTipo() {
		return codiceTipo;
	}

	/**
	 * Sets the value of the codiceTipo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceTipo(String value) {
		this.codiceTipo = value;
	}

	/**
	 * Gets the value of the codiceSede property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceSede() {
		return codiceSede;
	}

	/**
	 * Sets the value of the codiceSede property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceSede(String value) {
		this.codiceSede = value;
	}

	/**
	 * Gets the value of the codiceDistretto property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceDistretto() {
		return codiceDistretto;
	}

	/**
	 * Sets the value of the codiceDistretto property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceDistretto(String value) {
		this.codiceDistretto = value;
	}

	/**
	 * Gets the value of the codiceSistema property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceSistema() {
		return codiceSistema;
	}

	/**
	 * Sets the value of the codiceSistema property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceSistema(String value) {
		this.codiceSistema = value;
	}

}
