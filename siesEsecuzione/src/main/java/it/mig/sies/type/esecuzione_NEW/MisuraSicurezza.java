package it.mig.sies.type.esecuzione_NEW;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 *         &lt;element name=&quot;codiceMisuraSicurezzaProvvedimentoEsecuzione&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;codiceMisuraSicurezzaTitoloEsecutivo&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;5&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;DurataMisura&quot; type=&quot;{http://it/mig/sies/type/esecuzione_NEW}Durata&quot;/&gt;
 *         &lt;element name=&quot;DurataMisuraOld&quot; type=&quot;{http://it/mig/sies/type/esecuzione_NEW}Durata&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"codiceMisuraSicurezzaProvvedimentoEsecuzione",
		"codiceMisuraSicurezzaTitoloEsecutivo", "durataMisura",
		"durataMisuraOld" })
public class MisuraSicurezza {

	@XmlElement(required = true, nillable = true)
	protected String codiceMisuraSicurezzaProvvedimentoEsecuzione;
	@XmlElement(required = true, nillable = true)
	protected String codiceMisuraSicurezzaTitoloEsecutivo;
	@XmlElement(name = "DurataMisura", required = true, nillable = true)
	protected Durata durataMisura;
	@XmlElement(name = "DurataMisuraOld", required = true, nillable = true)
	protected Durata durataMisuraOld;

	/**
	 * Gets the value of the codiceMisuraSicurezzaProvvedimentoEsecuzione
	 * property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceMisuraSicurezzaProvvedimentoEsecuzione() {
		return codiceMisuraSicurezzaProvvedimentoEsecuzione;
	}

	/**
	 * Sets the value of the codiceMisuraSicurezzaProvvedimentoEsecuzione
	 * property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceMisuraSicurezzaProvvedimentoEsecuzione(String value) {
		this.codiceMisuraSicurezzaProvvedimentoEsecuzione = value;
	}

	/**
	 * Gets the value of the codiceMisuraSicurezzaTitoloEsecutivo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceMisuraSicurezzaTitoloEsecutivo() {
		return codiceMisuraSicurezzaTitoloEsecutivo;
	}

	/**
	 * Sets the value of the codiceMisuraSicurezzaTitoloEsecutivo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceMisuraSicurezzaTitoloEsecutivo(String value) {
		this.codiceMisuraSicurezzaTitoloEsecutivo = value;
	}

	/**
	 * Gets the value of the durataMisura property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataMisura() {
		return durataMisura;
	}

	/**
	 * Sets the value of the durataMisura property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataMisura(Durata value) {
		this.durataMisura = value;
	}

	/**
	 * Gets the value of the durataMisuraOld property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataMisuraOld() {
		return durataMisuraOld;
	}

	/**
	 * Sets the value of the durataMisuraOld property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataMisuraOld(Durata value) {
		this.durataMisuraOld = value;
	}

}
