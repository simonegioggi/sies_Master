package it.mig.sies.type.foglicomplementari_CUMULO;

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
 *         &lt;element name=&quot;xml&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "xml" })
@XmlRootElement(name = "trasferisciFoglioComplementare")
public class TrasferisciFoglioComplementare_Type {

	@XmlElement(required = true)
	protected String xml;

	/**
	 * Gets the value of the xml property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getXml() {
		return xml;
	}

	/**
	 * Sets the value of the xml property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setXml(String value) {
		this.xml = value;
	}

}
