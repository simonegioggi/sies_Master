package it.mig.sies.type.esecuzione_NEW;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Durata complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;Durata&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;anni&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;2&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;mesi&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;3&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;giorni&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;3&quot;/&gt;
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
@XmlType(name = "Durata", propOrder = { "anni", "mesi", "giorni" })
public class Durata {

	@XmlElement(required = true, nillable = true)
	protected BigInteger anni;
	@XmlElement(required = true)
	protected BigInteger mesi;
	@XmlElement(required = true)
	protected BigInteger giorni;

	/**
	 * Gets the value of the anni property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getAnni() {
		return anni;
	}

	/**
	 * Sets the value of the anni property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setAnni(BigInteger value) {
		this.anni = value;
	}

	/**
	 * Gets the value of the mesi property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getMesi() {
		return mesi;
	}

	/**
	 * Sets the value of the mesi property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setMesi(BigInteger value) {
		this.mesi = value;
	}

	/**
	 * Gets the value of the giorni property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getGiorni() {
		return giorni;
	}

	/**
	 * Sets the value of the giorni property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setGiorni(BigInteger value) {
		this.giorni = value;
	}

}
