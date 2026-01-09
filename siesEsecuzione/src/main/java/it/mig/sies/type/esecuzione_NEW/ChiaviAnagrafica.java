package it.mig.sies.type.esecuzione_NEW;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name=&quot;nsc&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;15&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;sies&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;15&quot;/&gt;
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
@XmlType(name = "", propOrder = { "nsc", "sies" })
public class ChiaviAnagrafica {

	@XmlElement(required = true, nillable = true)
	protected BigInteger nsc;
	@XmlElement(required = true)
	protected BigInteger sies;

	/**
	 * Gets the value of the nsc property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNsc() {
		return nsc;
	}

	/**
	 * Sets the value of the nsc property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNsc(BigInteger value) {
		this.nsc = value;
	}

	/**
	 * Gets the value of the sies property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getSies() {
		return sies;
	}

	/**
	 * Sets the value of the sies property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setSies(BigInteger value) {
		this.sies = value;
	}

}
