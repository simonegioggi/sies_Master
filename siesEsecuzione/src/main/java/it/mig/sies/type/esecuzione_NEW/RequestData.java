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
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Utente&quot;/&gt;
 *         &lt;element name=&quot;azione&quot; type=&quot;{http://it/mig/sies/type/esecuzione_NEW}Azione&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Anagrafica&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Provvedimento&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "utente", "azione", "anagrafica",
		"provvedimento" })
@XmlRootElement(name = "RequestData")
public class RequestData {

	@XmlElement(name = "Utente", required = true)
	protected Utente utente;
	@XmlElement(required = true)
	protected Azione azione;
	@XmlElement(name = "Anagrafica", required = true)
	protected Anagrafica anagrafica;
	@XmlElement(name = "Provvedimento", required = true)
	protected Provvedimento provvedimento;

	/**
	 * Gets the value of the utente property.
	 * 
	 * @return possible object is {@link Utente }
	 * 
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * Sets the value of the utente property.
	 * 
	 * @param value
	 *            allowed object is {@link Utente }
	 * 
	 */
	public void setUtente(Utente value) {
		this.utente = value;
	}

	/**
	 * Gets the value of the azione property.
	 * 
	 * @return possible object is {@link Azione }
	 * 
	 */
	public Azione getAzione() {
		return azione;
	}

	/**
	 * Sets the value of the azione property.
	 * 
	 * @param value
	 *            allowed object is {@link Azione }
	 * 
	 */
	public void setAzione(Azione value) {
		this.azione = value;
	}

	/**
	 * Gets the value of the anagrafica property.
	 * 
	 * @return possible object is {@link Anagrafica }
	 * 
	 */
	public Anagrafica getAnagrafica() {
		return anagrafica;
	}

	/**
	 * Sets the value of the anagrafica property.
	 * 
	 * @param value
	 *            allowed object is {@link Anagrafica }
	 * 
	 */
	public void setAnagrafica(Anagrafica value) {
		this.anagrafica = value;
	}

	/**
	 * Gets the value of the provvedimento property.
	 * 
	 * @return possible object is {@link Provvedimento }
	 * 
	 */
	public Provvedimento getProvvedimento() {
		return provvedimento;
	}

	/**
	 * Sets the value of the provvedimento property.
	 * 
	 * @param value
	 *            allowed object is {@link Provvedimento }
	 * 
	 */
	public void setProvvedimento(Provvedimento value) {
		this.provvedimento = value;
	}

}
