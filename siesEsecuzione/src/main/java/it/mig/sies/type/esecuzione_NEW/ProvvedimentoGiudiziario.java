package it.mig.sies.type.esecuzione_NEW;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ChiaviProvvedimentoGiudiziario&quot;/&gt;
 *         &lt;element name=&quot;numeroSentenza&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;flagCumulante&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot;/&gt;
 *         &lt;element name=&quot;annoSentenza&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;dataProvvedimento&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element name=&quot;dataImpugnazione&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element name=&quot;codiceAutorita&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;3&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceSedeAutoritaPrincipaleDistaccata&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;6&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceSedeAutoritaPrincipale&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;6&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;tipoAtto&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;1&quot;/&gt;
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
@XmlType(name = "", propOrder = { "chiaviProvvedimentoGiudiziario",
		"numeroSentenza", "flagCumulante", "annoSentenza", "dataProvvedimento",
		"dataImpugnazione", "codiceAutorita",
		"codiceSedeAutoritaPrincipaleDistaccata",
		"codiceSedeAutoritaPrincipale", "tipoAtto" })
public class ProvvedimentoGiudiziario {

	@XmlElement(name = "ChiaviProvvedimentoGiudiziario", required = true, nillable = true)
	protected ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario;
	@XmlElement(required = true, nillable = true)
	protected String numeroSentenza;
	protected boolean flagCumulante;
	@XmlElement(required = true, nillable = true)
	protected String annoSentenza;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataProvvedimento;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataImpugnazione;
	@XmlElement(required = true)
	protected String codiceAutorita;
	@XmlElement(required = true, nillable = true)
	protected String codiceSedeAutoritaPrincipaleDistaccata;
	@XmlElement(required = true, nillable = true)
	protected String codiceSedeAutoritaPrincipale;
	@XmlElement(required = true, nillable = true)
	protected String tipoAtto;

	/**
	 * Gets the value of the chiaviProvvedimentoGiudiziario property.
	 * 
	 * @return possible object is {@link ChiaviProvvedimentoGiudiziario }
	 * 
	 */
	public ChiaviProvvedimentoGiudiziario getChiaviProvvedimentoGiudiziario() {
		return chiaviProvvedimentoGiudiziario;
	}

	/**
	 * Sets the value of the chiaviProvvedimentoGiudiziario property.
	 * 
	 * @param value
	 *            allowed object is {@link ChiaviProvvedimentoGiudiziario }
	 * 
	 */
	public void setChiaviProvvedimentoGiudiziario(
			ChiaviProvvedimentoGiudiziario value) {
		this.chiaviProvvedimentoGiudiziario = value;
	}

	/**
	 * Gets the value of the numeroSentenza property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumeroSentenza() {
		return numeroSentenza;
	}

	/**
	 * Sets the value of the numeroSentenza property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNumeroSentenza(String value) {
		this.numeroSentenza = value;
	}

	/**
	 * Gets the value of the flagCumulante property.
	 * 
	 */
	public boolean isFlagCumulante() {
		return flagCumulante;
	}

	/**
	 * Sets the value of the flagCumulante property.
	 * 
	 */
	public void setFlagCumulante(boolean value) {
		this.flagCumulante = value;
	}

	/**
	 * Gets the value of the annoSentenza property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAnnoSentenza() {
		return annoSentenza;
	}

	/**
	 * Sets the value of the annoSentenza property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAnnoSentenza(String value) {
		this.annoSentenza = value;
	}

	/**
	 * Gets the value of the dataProvvedimento property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataProvvedimento() {
		return dataProvvedimento;
	}

	/**
	 * Sets the value of the dataProvvedimento property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataProvvedimento(XMLGregorianCalendar value) {
		this.dataProvvedimento = value;
	}

	/**
	 * Gets the value of the dataImpugnazione property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataImpugnazione() {
		return dataImpugnazione;
	}

	/**
	 * Sets the value of the dataImpugnazione property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataImpugnazione(XMLGregorianCalendar value) {
		this.dataImpugnazione = value;
	}

	/**
	 * Gets the value of the codiceAutorita property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceAutorita() {
		return codiceAutorita;
	}

	/**
	 * Sets the value of the codiceAutorita property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceAutorita(String value) {
		this.codiceAutorita = value;
	}

	/**
	 * Gets the value of the codiceSedeAutoritaPrincipaleDistaccata property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceSedeAutoritaPrincipaleDistaccata() {
		return codiceSedeAutoritaPrincipaleDistaccata;
	}

	/**
	 * Sets the value of the codiceSedeAutoritaPrincipaleDistaccata property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceSedeAutoritaPrincipaleDistaccata(String value) {
		this.codiceSedeAutoritaPrincipaleDistaccata = value;
	}

	/**
	 * Gets the value of the codiceSedeAutoritaPrincipale property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceSedeAutoritaPrincipale() {
		return codiceSedeAutoritaPrincipale;
	}

	/**
	 * Sets the value of the codiceSedeAutoritaPrincipale property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceSedeAutoritaPrincipale(String value) {
		this.codiceSedeAutoritaPrincipale = value;
	}

	/**
	 * Gets the value of the tipoAtto property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTipoAtto() {
		return tipoAtto;
	}

	/**
	 * Sets the value of the tipoAtto property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTipoAtto(String value) {
		this.tipoAtto = value;
	}

}
