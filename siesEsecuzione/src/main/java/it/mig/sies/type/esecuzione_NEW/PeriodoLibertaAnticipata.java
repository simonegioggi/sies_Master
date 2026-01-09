package it.mig.sies.type.esecuzione_NEW;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name=&quot;dataLibertaAnticipataInizio&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element name=&quot;dataLibertaAnticipataFine&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element name=&quot;statoPermesso&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;2&quot;/&gt;
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
@XmlType(name = "", propOrder = { "dataLibertaAnticipataInizio",
		"dataLibertaAnticipataFine", "statoPermesso" })
public class PeriodoLibertaAnticipata {

	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataLibertaAnticipataInizio;
	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataLibertaAnticipataFine;
	@XmlElement(required = true, nillable = true)
	protected String statoPermesso;

	/**
	 * Gets the value of the dataLibertaAnticipataInizio property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataLibertaAnticipataInizio() {
		return dataLibertaAnticipataInizio;
	}

	/**
	 * Sets the value of the dataLibertaAnticipataInizio property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataLibertaAnticipataInizio(XMLGregorianCalendar value) {
		this.dataLibertaAnticipataInizio = value;
	}

	/**
	 * Gets the value of the dataLibertaAnticipataFine property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataLibertaAnticipataFine() {
		return dataLibertaAnticipataFine;
	}

	/**
	 * Sets the value of the dataLibertaAnticipataFine property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataLibertaAnticipataFine(XMLGregorianCalendar value) {
		this.dataLibertaAnticipataFine = value;
	}

	/**
	 * Gets the value of the statoPermesso property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStatoPermesso() {
		return statoPermesso;
	}

	/**
	 * Sets the value of the statoPermesso property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStatoPermesso(String value) {
		this.statoPermesso = value;
	}

}
