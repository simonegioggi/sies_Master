package it.mig.sies.type.esecuzione_NEW;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}ChiaviAnagrafica&quot;/&gt;
 *         &lt;element name=&quot;cognome&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;35&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;nome&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;35&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceLuogoNascita&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;6&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceStatoEsteroNascita&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;8&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;descrizioneComuneEstero&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;60&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;dataNascita&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element name=&quot;sesso&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;1&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceFiscale&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;16&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;codiceImprontaDigitale&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;7&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;nomePadre&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;35&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;flagAliasRichiamo&quot;&gt;
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
@XmlType(name = "", propOrder = { "chiaviAnagrafica", "cognome", "nome",
		"codiceLuogoNascita", "codiceStatoEsteroNascita",
		"descrizioneComuneEstero", "dataNascita", "sesso", "codiceFiscale",
		"codiceImprontaDigitale", "nomePadre", "flagAliasRichiamo" })
@XmlRootElement(name = "Anagrafica")
public class Anagrafica {

	@XmlElement(name = "ChiaviAnagrafica", required = true, nillable = true)
	protected ChiaviAnagrafica chiaviAnagrafica;
	@XmlElement(required = true)
	protected String cognome;
	@XmlElement(required = true)
	protected String nome;
	@XmlElement(required = true, nillable = true)
	protected String codiceLuogoNascita;
	@XmlElement(required = true, nillable = true)
	protected String codiceStatoEsteroNascita;
	@XmlElement(required = true, nillable = true)
	protected String descrizioneComuneEstero;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataNascita;
	@XmlElement(required = true)
	protected String sesso;
	@XmlElement(required = true, nillable = true)
	protected String codiceFiscale;
	@XmlElement(required = true, nillable = true)
	protected String codiceImprontaDigitale;
	@XmlElement(required = true, nillable = true)
	protected String nomePadre;
	@XmlElement(required = true, nillable = true)
	protected String flagAliasRichiamo;

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
	 * Gets the value of the cognome property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Sets the value of the cognome property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCognome(String value) {
		this.cognome = value;
	}

	/**
	 * Gets the value of the nome property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Sets the value of the nome property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNome(String value) {
		this.nome = value;
	}

	/**
	 * Gets the value of the codiceLuogoNascita property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceLuogoNascita() {
		return codiceLuogoNascita;
	}

	/**
	 * Sets the value of the codiceLuogoNascita property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceLuogoNascita(String value) {
		this.codiceLuogoNascita = value;
	}

	/**
	 * Gets the value of the codiceStatoEsteroNascita property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceStatoEsteroNascita() {
		return codiceStatoEsteroNascita;
	}

	/**
	 * Sets the value of the codiceStatoEsteroNascita property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceStatoEsteroNascita(String value) {
		this.codiceStatoEsteroNascita = value;
	}

	/**
	 * Gets the value of the descrizioneComuneEstero property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneComuneEstero() {
		return descrizioneComuneEstero;
	}

	/**
	 * Sets the value of the descrizioneComuneEstero property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneComuneEstero(String value) {
		this.descrizioneComuneEstero = value;
	}

	/**
	 * Gets the value of the dataNascita property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataNascita() {
		return dataNascita;
	}

	/**
	 * Sets the value of the dataNascita property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataNascita(XMLGregorianCalendar value) {
		this.dataNascita = value;
	}

	/**
	 * Gets the value of the sesso property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * Sets the value of the sesso property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSesso(String value) {
		this.sesso = value;
	}

	/**
	 * Gets the value of the codiceFiscale property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * Sets the value of the codiceFiscale property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceFiscale(String value) {
		this.codiceFiscale = value;
	}

	/**
	 * Gets the value of the codiceImprontaDigitale property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceImprontaDigitale() {
		return codiceImprontaDigitale;
	}

	/**
	 * Sets the value of the codiceImprontaDigitale property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceImprontaDigitale(String value) {
		this.codiceImprontaDigitale = value;
	}

	/**
	 * Gets the value of the nomePadre property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNomePadre() {
		return nomePadre;
	}

	/**
	 * Sets the value of the nomePadre property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNomePadre(String value) {
		this.nomePadre = value;
	}

	/**
	 * Gets the value of the flagAliasRichiamo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlagAliasRichiamo() {
		return flagAliasRichiamo;
	}

	/**
	 * Sets the value of the flagAliasRichiamo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFlagAliasRichiamo(String value) {
		this.flagAliasRichiamo = value;
	}

}
