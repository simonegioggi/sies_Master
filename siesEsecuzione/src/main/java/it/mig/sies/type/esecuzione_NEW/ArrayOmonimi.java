package it.mig.sies.type.esecuzione_NEW;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name=&quot;Omonimo&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Anagrafica&quot;/&gt;
 *                   &lt;element name=&quot;CertificatoControllo&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}base64Binary&quot;/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
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
@XmlType(name = "", propOrder = { "omonimo" })
@XmlRootElement(name = "ArrayOmonimi")
public class ArrayOmonimi {

	@XmlElement(name = "Omonimo")
	protected List<ArrayOmonimi.Omonimo> omonimo;

	/**
	 * Gets the value of the omonimo property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the omonimo property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOmonimo().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ArrayOmonimi.Omonimo }
	 * 
	 * 
	 */
	public List<ArrayOmonimi.Omonimo> getOmonimo() {
		if (omonimo == null) {
			omonimo = new ArrayList<ArrayOmonimi.Omonimo>();
		}
		return this.omonimo;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType&gt;
	 *   &lt;complexContent&gt;
	 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
	 *       &lt;sequence&gt;
	 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Anagrafica&quot;/&gt;
	 *         &lt;element name=&quot;CertificatoControllo&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}base64Binary&quot;/&gt;
	 *       &lt;/sequence&gt;
	 *     &lt;/restriction&gt;
	 *   &lt;/complexContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "anagrafica", "certificatoControllo" })
	public static class Omonimo {

		@XmlElement(name = "Anagrafica", required = true)
		protected Anagrafica anagrafica;
		@XmlElement(name = "CertificatoControllo", required = true)
		protected byte[] certificatoControllo;

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
		 * Gets the value of the certificatoControllo property.
		 * 
		 * @return possible object is byte[]
		 */
		public byte[] getCertificatoControllo() {
			return certificatoControllo;
		}

		/**
		 * Sets the value of the certificatoControllo property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 */
		public void setCertificatoControllo(byte[] value) {
			this.certificatoControllo = ((byte[]) value);
		}

	}

}
