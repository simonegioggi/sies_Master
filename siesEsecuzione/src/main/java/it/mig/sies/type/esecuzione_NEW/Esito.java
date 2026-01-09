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
 *         &lt;element name=&quot;codice&quot; type=&quot;{http://it/mig/sies/type/esecuzione_NEW}ResponseCode&quot;/&gt;
 *         &lt;element name=&quot;descrizione&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;60&quot;/&gt;
 *               &lt;enumeration value=&quot;&quot;/&gt;
 *               &lt;enumeration value=&quot;&quot;/&gt;
 *               &lt;enumeration value=&quot;&quot;/&gt;
 *               &lt;enumeration value=&quot;&quot;/&gt;
 *               &lt;enumeration value=&quot;&quot;/&gt;
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
@XmlType(name = "", propOrder = { "codice", "descrizione" })
@XmlRootElement(name = "Esito")
public class Esito {

	@XmlElement(required = true)
	protected ResponseCode codice;
	@XmlElement(required = true)
	protected String descrizione;

	/**
	 * Gets the value of the codice property.
	 * 
	 * @return possible object is {@link ResponseCode }
	 * 
	 */
	public ResponseCode getCodice() {
		return codice;
	}

	/**
	 * Sets the value of the codice property.
	 * 
	 * @param value
	 *            allowed object is {@link ResponseCode }
	 * 
	 */
	public void setCodice(ResponseCode value) {
		this.codice = value;
	}

	/**
	 * Gets the value of the descrizione property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Sets the value of the descrizione property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescrizione(String value) {
		this.descrizione = value;
	}

}
