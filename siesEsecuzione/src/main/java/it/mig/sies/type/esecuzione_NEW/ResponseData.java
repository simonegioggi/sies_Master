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
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}Esito&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ChiaviAnagrafica&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ChiaviProvvedimentoEsecutivo&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ChiaviProvvedimentoGiudiziario&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ArrayOmonimi&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;Estratto&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}base64Binary&quot;/&gt;
 *         &lt;element name=&quot;AnagraficaCancellata&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "esito", "chiaviAnagrafica",
		"chiaviProvvedimentoEsecutivo", "chiaviProvvedimentoGiudiziario",
		"arrayOmonimi", "estratto", "anagraficaCancellata" })
@XmlRootElement(name = "ResponseData")
public class ResponseData {

	@XmlElement(name = "Esito", required = true)
	protected Esito esito;
	@XmlElement(name = "ChiaviAnagrafica", required = true, nillable = true)
	protected ChiaviAnagrafica chiaviAnagrafica;
	@XmlElement(name = "ChiaviProvvedimentoEsecutivo", required = true, nillable = true)
	protected ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo;
	@XmlElement(name = "ChiaviProvvedimentoGiudiziario", required = true, nillable = true)
	protected List<ChiaviProvvedimentoGiudiziario> chiaviProvvedimentoGiudiziario;
	@XmlElement(name = "ArrayOmonimi")
	protected ArrayOmonimi arrayOmonimi;
	@XmlElement(name = "Estratto", required = true, nillable = true)
	protected byte[] estratto;
	@XmlElement(name = "AnagraficaCancellata")
	protected boolean anagraficaCancellata;

	/**
	 * Gets the value of the esito property.
	 * 
	 * @return possible object is {@link Esito }
	 * 
	 */
	public Esito getEsito() {
		return esito;
	}

	/**
	 * Sets the value of the esito property.
	 * 
	 * @param value
	 *            allowed object is {@link Esito }
	 * 
	 */
	public void setEsito(Esito value) {
		this.esito = value;
	}

	/**
	 * Gets the value of the chiaviAnagrafica property.
	 * 
	 * @return possible object is {@link ChiaviAnagrafica }
	 * 
	 */
	public ChiaviAnagrafica getChiaviAnagrafica() {
		return chiaviAnagrafica;
	}

	/**
	 * Sets the value of the chiaviAnagrafica property.
	 * 
	 * @param value
	 *            allowed object is {@link ChiaviAnagrafica }
	 * 
	 */
	public void setChiaviAnagrafica(ChiaviAnagrafica value) {
		this.chiaviAnagrafica = value;
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
	 * Gets the value of the chiaviProvvedimentoGiudiziario property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the chiaviProvvedimentoGiudiziario
	 * property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getChiaviProvvedimentoGiudiziario().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ChiaviProvvedimentoGiudiziario }
	 * 
	 * 
	 */
	public List<ChiaviProvvedimentoGiudiziario> getChiaviProvvedimentoGiudiziario() {
		if (chiaviProvvedimentoGiudiziario == null) {
			chiaviProvvedimentoGiudiziario = new ArrayList<ChiaviProvvedimentoGiudiziario>();
		}
		return this.chiaviProvvedimentoGiudiziario;
	}

	/**
	 * Gets the value of the arrayOmonimi property.
	 * 
	 * @return possible object is {@link ArrayOmonimi }
	 * 
	 */
	public ArrayOmonimi getArrayOmonimi() {
		return arrayOmonimi;
	}

	/**
	 * Sets the value of the arrayOmonimi property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOmonimi }
	 * 
	 */
	public void setArrayOmonimi(ArrayOmonimi value) {
		this.arrayOmonimi = value;
	}

	/**
	 * Gets the value of the estratto property.
	 * 
	 * @return possible object is byte[]
	 */
	public byte[] getEstratto() {
		return estratto;
	}

	/**
	 * Sets the value of the estratto property.
	 * 
	 * @param value
	 *            allowed object is byte[]
	 */
	public void setEstratto(byte[] value) {
		this.estratto = ((byte[]) value);
	}

	/**
	 * Gets the value of the anagraficaCancellata property.
	 * 
	 */
	public boolean isAnagraficaCancellata() {
		return anagraficaCancellata;
	}

	/**
	 * Sets the value of the anagraficaCancellata property.
	 * 
	 */
	public void setAnagraficaCancellata(boolean value) {
		this.anagraficaCancellata = value;
	}

}
