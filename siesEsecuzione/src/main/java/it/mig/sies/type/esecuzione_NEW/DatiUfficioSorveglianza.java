package it.mig.sies.type.esecuzione_NEW;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name=&quot;dataProvvedimento&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}annoOrdinanza&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}numeroOrdinanza&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}codiceSedePM&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}annoSIUS&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}numeroSIUS&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}annoSentenza&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}numeroSentenza&quot;/&gt;
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
 *         &lt;element name=&quot;codiceUnivocoProvvedimento&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;12&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;tipoProvvedimento&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;2&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;giorniLibertaAnticipata&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;4&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;giorniLibertaAnticipataLs&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;4&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;giorniLibertaAnticipataLi&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;4&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}PeriodoLibertaAnticipata&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;importoAmmenda&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}decimal&quot;&gt;
 *               &lt;totalDigits value=&quot;14&quot;/&gt;
 *               &lt;fractionDigits value=&quot;2&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;importoMulta&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}decimal&quot;&gt;
 *               &lt;totalDigits value=&quot;14&quot;/&gt;
 *               &lt;fractionDigits value=&quot;2&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}durataLibertaControllata&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}durataLavoroSostitutivo&quot;/&gt;
 *         &lt;element name=&quot;testoLibero&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *               &lt;maxLength value=&quot;2000&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;numeroRate&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;3&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;importoRata&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}decimal&quot;&gt;
 *               &lt;totalDigits value=&quot;14&quot;/&gt;
 *               &lt;fractionDigits value=&quot;2&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;importoUltimaRata&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}decimal&quot;&gt;
 *               &lt;totalDigits value=&quot;14&quot;/&gt;
 *               &lt;fractionDigits value=&quot;2&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;dataDecorrenzaPrimaRata&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element name=&quot;giorniDallaNotifica&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;4&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name=&quot;DurataDifferimento&quot; type=&quot;{http://it/mig/sies/type/esecuzione_NEW}Durata&quot;/&gt;
 *         &lt;element name=&quot;dataDecorrenzaSospensione&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}durataSospensione&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataFineSospensione&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;idProvvedimentoRevocato&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;&gt;
 *               &lt;totalDigits value=&quot;15&quot;/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}MisuraSicurezza&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "dataProvvedimento", "annoOrdinanza",
		"numeroOrdinanza", "codiceSedePM", "annoSIUS", "numeroSIUS",
		"annoSentenza", "numeroSentenza", "codiceAutorita",
		"codiceSedeAutoritaPrincipaleDistaccata",
		"codiceSedeAutoritaPrincipale", "codiceUnivocoProvvedimento",
		"tipoProvvedimento", "giorniLibertaAnticipata",
		"giorniLibertaAnticipataLs", "giorniLibertaAnticipataLi",
		"periodoLibertaAnticipata", "importoAmmenda", "importoMulta",
		"durataLibertaControllata", "durataLavoroSostitutivo", "testoLibero",
		"numeroRate", "importoRata", "importoUltimaRata",
		"dataDecorrenzaPrimaRata", "giorniDallaNotifica", "durataDifferimento",
		"dataDecorrenzaSospensione", "durataSospensione",
		"dataFineSospensione", "idProvvedimentoRevocato", "misuraSicurezza" })
@XmlRootElement(name = "DatiUfficioSorveglianza")
public class DatiUfficioSorveglianza {

	@XmlElement(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataProvvedimento;
	@XmlElement(required = true, nillable = true)
	protected BigInteger annoOrdinanza;
	@XmlElement(required = true, nillable = true)
	protected BigInteger numeroOrdinanza;
	@XmlElement(required = true, nillable = true)
	protected String codiceSedePM;
	@XmlElement(required = true, nillable = true)
	protected BigInteger annoSIUS;
	@XmlElement(required = true, nillable = true)
	protected BigInteger numeroSIUS;
	@XmlElement(required = true, nillable = true)
	protected BigInteger annoSentenza;
	@XmlElement(required = true, nillable = true)
	protected BigInteger numeroSentenza;
	@XmlElement(required = true)
	protected String codiceAutorita;
	@XmlElement(required = true, nillable = true)
	protected String codiceSedeAutoritaPrincipaleDistaccata;
	@XmlElement(required = true, nillable = true)
	protected String codiceSedeAutoritaPrincipale;
	@XmlElement(required = true)
	protected String codiceUnivocoProvvedimento;
	@XmlElement(required = true)
	protected String tipoProvvedimento;
	@XmlElement(required = true, nillable = true)
	protected BigInteger giorniLibertaAnticipata;
	@XmlElement(required = true, nillable = true)
	protected BigInteger giorniLibertaAnticipataLs;
	@XmlElement(required = true, nillable = true)
	protected BigInteger giorniLibertaAnticipataLi;
	@XmlElement(name = "PeriodoLibertaAnticipata", nillable = true)
	protected List<PeriodoLibertaAnticipata> periodoLibertaAnticipata;
	@XmlElement(required = true, nillable = true)
	protected BigDecimal importoAmmenda;
	@XmlElement(required = true, nillable = true)
	protected BigDecimal importoMulta;
	@XmlElement(required = true, nillable = true)
	protected Durata durataLibertaControllata;
	@XmlElement(required = true, nillable = true)
	protected Durata durataLavoroSostitutivo;
	@XmlElement(required = true, nillable = true)
	protected String testoLibero;
	@XmlElement(required = true, nillable = true)
	protected BigInteger numeroRate;
	@XmlElement(required = true, nillable = true)
	protected BigDecimal importoRata;
	@XmlElement(required = true, nillable = true)
	protected BigDecimal importoUltimaRata;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataDecorrenzaPrimaRata;
	@XmlElement(required = true, nillable = true)
	protected BigInteger giorniDallaNotifica;
	@XmlElement(name = "DurataDifferimento", required = true, nillable = true)
	protected Durata durataDifferimento;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataDecorrenzaSospensione;
	protected Durata durataSospensione;
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataFineSospensione;
	@XmlElement(required = true, nillable = true)
	protected BigInteger idProvvedimentoRevocato;
	@XmlElement(name = "MisuraSicurezza", nillable = true)
	protected List<MisuraSicurezza> misuraSicurezza;

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
	 * Gets the value of the annoOrdinanza property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getAnnoOrdinanza() {
		return annoOrdinanza;
	}

	/**
	 * Sets the value of the annoOrdinanza property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setAnnoOrdinanza(BigInteger value) {
		this.annoOrdinanza = value;
	}

	/**
	 * Gets the value of the numeroOrdinanza property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNumeroOrdinanza() {
		return numeroOrdinanza;
	}

	/**
	 * Sets the value of the numeroOrdinanza property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNumeroOrdinanza(BigInteger value) {
		this.numeroOrdinanza = value;
	}

	/**
	 * Gets the value of the codiceSedePM property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceSedePM() {
		return codiceSedePM;
	}

	/**
	 * Sets the value of the codiceSedePM property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceSedePM(String value) {
		this.codiceSedePM = value;
	}

	/**
	 * Gets the value of the annoSIUS property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getAnnoSIUS() {
		return annoSIUS;
	}

	/**
	 * Sets the value of the annoSIUS property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setAnnoSIUS(BigInteger value) {
		this.annoSIUS = value;
	}

	/**
	 * Gets the value of the numeroSIUS property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNumeroSIUS() {
		return numeroSIUS;
	}

	/**
	 * Sets the value of the numeroSIUS property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNumeroSIUS(BigInteger value) {
		this.numeroSIUS = value;
	}

	/**
	 * Gets the value of the annoSentenza property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getAnnoSentenza() {
		return annoSentenza;
	}

	/**
	 * Sets the value of the annoSentenza property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setAnnoSentenza(BigInteger value) {
		this.annoSentenza = value;
	}

	/**
	 * Gets the value of the numeroSentenza property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNumeroSentenza() {
		return numeroSentenza;
	}

	/**
	 * Sets the value of the numeroSentenza property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNumeroSentenza(BigInteger value) {
		this.numeroSentenza = value;
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
	 * Gets the value of the codiceUnivocoProvvedimento property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceUnivocoProvvedimento() {
		return codiceUnivocoProvvedimento;
	}

	/**
	 * Sets the value of the codiceUnivocoProvvedimento property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodiceUnivocoProvvedimento(String value) {
		this.codiceUnivocoProvvedimento = value;
	}

	/**
	 * Gets the value of the tipoProvvedimento property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTipoProvvedimento() {
		return tipoProvvedimento;
	}

	/**
	 * Sets the value of the tipoProvvedimento property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTipoProvvedimento(String value) {
		this.tipoProvvedimento = value;
	}

	/**
	 * Gets the value of the giorniLibertaAnticipata property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getGiorniLibertaAnticipata() {
		return giorniLibertaAnticipata;
	}

	/**
	 * Sets the value of the giorniLibertaAnticipata property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setGiorniLibertaAnticipata(BigInteger value) {
		this.giorniLibertaAnticipata = value;
	}

	/**
	 * Gets the value of the giorniLibertaAnticipataLs property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getGiorniLibertaAnticipataLs() {
		return giorniLibertaAnticipataLs;
	}

	/**
	 * Sets the value of the giorniLibertaAnticipataLs property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setGiorniLibertaAnticipataLs(BigInteger value) {
		this.giorniLibertaAnticipataLs = value;
	}

	/**
	 * Gets the value of the giorniLibertaAnticipataLi property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getGiorniLibertaAnticipataLi() {
		return giorniLibertaAnticipataLi;
	}

	/**
	 * Sets the value of the giorniLibertaAnticipataLi property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setGiorniLibertaAnticipataLi(BigInteger value) {
		this.giorniLibertaAnticipataLi = value;
	}

	/**
	 * Gets the value of the periodoLibertaAnticipata property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the periodoLibertaAnticipata property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPeriodoLibertaAnticipata().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link PeriodoLibertaAnticipata }
	 * 
	 * 
	 */
	public List<PeriodoLibertaAnticipata> getPeriodoLibertaAnticipata() {
		if (periodoLibertaAnticipata == null) {
			periodoLibertaAnticipata = new ArrayList<PeriodoLibertaAnticipata>();
		}
		return this.periodoLibertaAnticipata;
	}

	/**
	 * Gets the value of the importoAmmenda property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getImportoAmmenda() {
		return importoAmmenda;
	}

	/**
	 * Sets the value of the importoAmmenda property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setImportoAmmenda(BigDecimal value) {
		this.importoAmmenda = value;
	}

	/**
	 * Gets the value of the importoMulta property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getImportoMulta() {
		return importoMulta;
	}

	/**
	 * Sets the value of the importoMulta property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setImportoMulta(BigDecimal value) {
		this.importoMulta = value;
	}

	/**
	 * Gets the value of the durataLibertaControllata property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataLibertaControllata() {
		return durataLibertaControllata;
	}

	/**
	 * Sets the value of the durataLibertaControllata property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataLibertaControllata(Durata value) {
		this.durataLibertaControllata = value;
	}

	/**
	 * Gets the value of the durataLavoroSostitutivo property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataLavoroSostitutivo() {
		return durataLavoroSostitutivo;
	}

	/**
	 * Sets the value of the durataLavoroSostitutivo property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataLavoroSostitutivo(Durata value) {
		this.durataLavoroSostitutivo = value;
	}

	/**
	 * Gets the value of the testoLibero property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTestoLibero() {
		return testoLibero;
	}

	/**
	 * Sets the value of the testoLibero property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTestoLibero(String value) {
		this.testoLibero = value;
	}

	/**
	 * Gets the value of the numeroRate property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getNumeroRate() {
		return numeroRate;
	}

	/**
	 * Sets the value of the numeroRate property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNumeroRate(BigInteger value) {
		this.numeroRate = value;
	}

	/**
	 * Gets the value of the importoRata property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getImportoRata() {
		return importoRata;
	}

	/**
	 * Sets the value of the importoRata property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setImportoRata(BigDecimal value) {
		this.importoRata = value;
	}

	/**
	 * Gets the value of the importoUltimaRata property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getImportoUltimaRata() {
		return importoUltimaRata;
	}

	/**
	 * Sets the value of the importoUltimaRata property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setImportoUltimaRata(BigDecimal value) {
		this.importoUltimaRata = value;
	}

	/**
	 * Gets the value of the dataDecorrenzaPrimaRata property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataDecorrenzaPrimaRata() {
		return dataDecorrenzaPrimaRata;
	}

	/**
	 * Sets the value of the dataDecorrenzaPrimaRata property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataDecorrenzaPrimaRata(XMLGregorianCalendar value) {
		this.dataDecorrenzaPrimaRata = value;
	}

	/**
	 * Gets the value of the giorniDallaNotifica property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getGiorniDallaNotifica() {
		return giorniDallaNotifica;
	}

	/**
	 * Sets the value of the giorniDallaNotifica property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setGiorniDallaNotifica(BigInteger value) {
		this.giorniDallaNotifica = value;
	}

	/**
	 * Gets the value of the durataDifferimento property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataDifferimento() {
		return durataDifferimento;
	}

	/**
	 * Sets the value of the durataDifferimento property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataDifferimento(Durata value) {
		this.durataDifferimento = value;
	}

	/**
	 * Gets the value of the dataDecorrenzaSospensione property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataDecorrenzaSospensione() {
		return dataDecorrenzaSospensione;
	}

	/**
	 * Sets the value of the dataDecorrenzaSospensione property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataDecorrenzaSospensione(XMLGregorianCalendar value) {
		this.dataDecorrenzaSospensione = value;
	}

	/**
	 * Gets the value of the durataSospensione property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataSospensione() {
		return durataSospensione;
	}

	/**
	 * Sets the value of the durataSospensione property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataSospensione(Durata value) {
		this.durataSospensione = value;
	}

	/**
	 * Gets the value of the dataFineSospensione property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataFineSospensione() {
		return dataFineSospensione;
	}

	/**
	 * Sets the value of the dataFineSospensione property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataFineSospensione(XMLGregorianCalendar value) {
		this.dataFineSospensione = value;
	}

	/**
	 * Gets the value of the idProvvedimentoRevocato property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getIdProvvedimentoRevocato() {
		return idProvvedimentoRevocato;
	}

	/**
	 * Sets the value of the idProvvedimentoRevocato property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setIdProvvedimentoRevocato(BigInteger value) {
		this.idProvvedimentoRevocato = value;
	}

	/**
	 * Gets the value of the misuraSicurezza property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the misuraSicurezza property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMisuraSicurezza().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link MisuraSicurezza }
	 * 
	 * 
	 */
	public List<MisuraSicurezza> getMisuraSicurezza() {
		if (misuraSicurezza == null) {
			misuraSicurezza = new ArrayList<MisuraSicurezza>();
		}
		return this.misuraSicurezza;
	}

}
