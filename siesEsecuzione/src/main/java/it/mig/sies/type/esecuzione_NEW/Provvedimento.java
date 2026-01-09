package it.mig.sies.type.esecuzione_NEW;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name=&quot;annoSius&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;numeroSius&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}long&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;flagPrincipale&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ProvvedimentoGiudiziario&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ChiaviProvvedimentoEsecutivo&quot;/&gt;
 *         &lt;choice&gt;
 *           &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}DatiUfficioSorveglianza&quot;/&gt;
 *           &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}DatiTribunaleSorveglianza&quot;/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "annoSius", "numeroSius", "flagPrincipale",
		"provvedimentoGiudiziario", "chiaviProvvedimentoEsecutivo",
		"datiUfficioSorveglianza", "datiTribunaleSorveglianza" })
@XmlRootElement(name = "Provvedimento")
public class Provvedimento {

	protected Integer annoSius;
	protected Long numeroSius;
	protected boolean flagPrincipale;
	@XmlElement(name = "ProvvedimentoGiudiziario", required = true, nillable = true)
	protected List<ProvvedimentoGiudiziario> provvedimentoGiudiziario;
	@XmlElement(name = "ChiaviProvvedimentoEsecutivo", required = true, nillable = true)
	protected ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo;
	@XmlElement(name = "DatiUfficioSorveglianza")
	protected DatiUfficioSorveglianza datiUfficioSorveglianza;
	@XmlElement(name = "DatiTribunaleSorveglianza")
	protected DatiTribunaleSorveglianza datiTribunaleSorveglianza;

	/**
	 * Gets the value of the annoSius property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getAnnoSius() {
		return annoSius;
	}

	/**
	 * Sets the value of the annoSius property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setAnnoSius(Integer value) {
		this.annoSius = value;
	}

	/**
	 * Gets the value of the numeroSius property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getNumeroSius() {
		return numeroSius;
	}

	/**
	 * Sets the value of the numeroSius property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setNumeroSius(Long value) {
		this.numeroSius = value;
	}

	/**
	 * Gets the value of the flagPrincipale property.
	 * 
	 */
	public boolean isFlagPrincipale() {
		return flagPrincipale;
	}

	/**
	 * Sets the value of the flagPrincipale property.
	 * 
	 */
	public void setFlagPrincipale(boolean value) {
		this.flagPrincipale = value;
	}

	/**
	 * Gets the value of the provvedimentoGiudiziario property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the provvedimentoGiudiziario property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getProvvedimentoGiudiziario().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ProvvedimentoGiudiziario }
	 * 
	 * 
	 */
	public List<ProvvedimentoGiudiziario> getProvvedimentoGiudiziario() {
		if (provvedimentoGiudiziario == null) {
			provvedimentoGiudiziario = new ArrayList<ProvvedimentoGiudiziario>();
		}
		return this.provvedimentoGiudiziario;
	}

	/**
	 * Gets the value of the chiaviProvvedimentoEsecutivo property.
	 * 
	 * @return possible object is {@link ChiaviProvvedimentoEsecutivo }
	 * 
	 */
	public ChiaviProvvedimentoEsecutivo getChiaviProvvedimentoEsecutivo() {
		return chiaviProvvedimentoEsecutivo;
	}

	/**
	 * Sets the value of the chiaviProvvedimentoEsecutivo property.
	 * 
	 * @param value
	 *            allowed object is {@link ChiaviProvvedimentoEsecutivo }
	 * 
	 */
	public void setChiaviProvvedimentoEsecutivo(
			ChiaviProvvedimentoEsecutivo value) {
		this.chiaviProvvedimentoEsecutivo = value;
	}

	/**
	 * Gets the value of the datiUfficioSorveglianza property.
	 * 
	 * @return possible object is {@link DatiUfficioSorveglianza }
	 * 
	 */
	public DatiUfficioSorveglianza getDatiUfficioSorveglianza() {
		return datiUfficioSorveglianza;
	}

	/**
	 * Sets the value of the datiUfficioSorveglianza property.
	 * 
	 * @param value
	 *            allowed object is {@link DatiUfficioSorveglianza }
	 * 
	 */
	public void setDatiUfficioSorveglianza(DatiUfficioSorveglianza value) {
		this.datiUfficioSorveglianza = value;
	}

	/**
	 * Gets the value of the datiTribunaleSorveglianza property.
	 * 
	 * @return possible object is {@link DatiTribunaleSorveglianza }
	 * 
	 */
	public DatiTribunaleSorveglianza getDatiTribunaleSorveglianza() {
		return datiTribunaleSorveglianza;
	}

	/**
	 * Sets the value of the datiTribunaleSorveglianza property.
	 * 
	 * @param value
	 *            allowed object is {@link DatiTribunaleSorveglianza }
	 * 
	 */
	public void setDatiTribunaleSorveglianza(DatiTribunaleSorveglianza value) {
		this.datiTribunaleSorveglianza = value;
	}

}
