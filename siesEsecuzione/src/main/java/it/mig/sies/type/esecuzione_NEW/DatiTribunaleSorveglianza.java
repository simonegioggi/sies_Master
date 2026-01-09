package it.mig.sies.type.esecuzione_NEW;

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
 *         &lt;element name=&quot;note&quot;&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
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
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}durataMisura&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataTermineMisura&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataDecorrenzaRevoca&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}penaRideterminataArresto&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}penaRideterminataReclusione&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataFineBeneficio&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}durataBeneficio&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataInizioNonEspiata&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataFineNonEspiata&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}penaRideterminata&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataInizioDifferimentoPena&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataFineDifferimentoPena&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}dataInizioRevoca&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}penaDetentivaDaEspiareArresto&quot;/&gt;
 *         &lt;element ref=&quot;{http://it/mig/sies/type/esecuzione_NEW}penaDetentivaDaEspiareReclusione&quot;/&gt;
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
		"annoSentenza", "numeroSentenza", "codiceAutorita", "note",
		"codiceSedeAutoritaPrincipaleDistaccata",
		"codiceSedeAutoritaPrincipale", "codiceUnivocoProvvedimento",
		"tipoProvvedimento", "giorniLibertaAnticipata",
		"giorniLibertaAnticipataLs", "giorniLibertaAnticipataLi",
		"periodoLibertaAnticipata", "durataMisura", "dataTermineMisura",
		"dataDecorrenzaRevoca", "penaRideterminataArresto",
		"penaRideterminataReclusione", "dataFineBeneficio", "durataBeneficio",
		"dataInizioNonEspiata", "dataFineNonEspiata", "penaRideterminata",
		"dataInizioDifferimentoPena", "dataFineDifferimentoPena",
		"dataInizioRevoca", "penaDetentivaDaEspiareArresto",
		"penaDetentivaDaEspiareReclusione", "idProvvedimentoRevocato",
		"misuraSicurezza" })
@XmlRootElement(name = "DatiTribunaleSorveglianza")
public class DatiTribunaleSorveglianza {

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
	protected String note;
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
	protected Durata durataMisura;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataTermineMisura;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataDecorrenzaRevoca;
	@XmlElement(required = true, nillable = true)
	protected Durata penaRideterminataArresto;
	@XmlElement(required = true, nillable = true)
	protected Durata penaRideterminataReclusione;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataFineBeneficio;
	@XmlElement(required = true, nillable = true)
	protected Durata durataBeneficio;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataInizioNonEspiata;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataFineNonEspiata;
	@XmlElement(required = true, nillable = true)
	protected Durata penaRideterminata;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataInizioDifferimentoPena;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataFineDifferimentoPena;
	@XmlElement(required = true, nillable = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar dataInizioRevoca;
	@XmlElement(required = true, nillable = true)
	protected Durata penaDetentivaDaEspiareArresto;
	@XmlElement(required = true, nillable = true)
	protected Durata penaDetentivaDaEspiareReclusione;
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
	 * Gets the value of the note property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Sets the value of the note property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNote(String value) {
		this.note = value;
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
	 * Gets the value of the dataTermineMisura property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataTermineMisura() {
		return dataTermineMisura;
	}

	/**
	 * Sets the value of the dataTermineMisura property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataTermineMisura(XMLGregorianCalendar value) {
		this.dataTermineMisura = value;
	}

	/**
	 * Gets the value of the dataDecorrenzaRevoca property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataDecorrenzaRevoca() {
		return dataDecorrenzaRevoca;
	}

	/**
	 * Sets the value of the dataDecorrenzaRevoca property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataDecorrenzaRevoca(XMLGregorianCalendar value) {
		this.dataDecorrenzaRevoca = value;
	}

	/**
	 * Gets the value of the penaRideterminataArresto property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getPenaRideterminataArresto() {
		return penaRideterminataArresto;
	}

	/**
	 * Sets the value of the penaRideterminataArresto property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setPenaRideterminataArresto(Durata value) {
		this.penaRideterminataArresto = value;
	}

	/**
	 * Gets the value of the penaRideterminataReclusione property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getPenaRideterminataReclusione() {
		return penaRideterminataReclusione;
	}

	/**
	 * Sets the value of the penaRideterminataReclusione property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setPenaRideterminataReclusione(Durata value) {
		this.penaRideterminataReclusione = value;
	}

	/**
	 * Gets the value of the dataFineBeneficio property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataFineBeneficio() {
		return dataFineBeneficio;
	}

	/**
	 * Sets the value of the dataFineBeneficio property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataFineBeneficio(XMLGregorianCalendar value) {
		this.dataFineBeneficio = value;
	}

	/**
	 * Gets the value of the durataBeneficio property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getDurataBeneficio() {
		return durataBeneficio;
	}

	/**
	 * Sets the value of the durataBeneficio property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setDurataBeneficio(Durata value) {
		this.durataBeneficio = value;
	}

	/**
	 * Gets the value of the dataInizioNonEspiata property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataInizioNonEspiata() {
		return dataInizioNonEspiata;
	}

	/**
	 * Sets the value of the dataInizioNonEspiata property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataInizioNonEspiata(XMLGregorianCalendar value) {
		this.dataInizioNonEspiata = value;
	}

	/**
	 * Gets the value of the dataFineNonEspiata property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataFineNonEspiata() {
		return dataFineNonEspiata;
	}

	/**
	 * Sets the value of the dataFineNonEspiata property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataFineNonEspiata(XMLGregorianCalendar value) {
		this.dataFineNonEspiata = value;
	}

	/**
	 * Gets the value of the penaRideterminata property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getPenaRideterminata() {
		return penaRideterminata;
	}

	/**
	 * Sets the value of the penaRideterminata property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setPenaRideterminata(Durata value) {
		this.penaRideterminata = value;
	}

	/**
	 * Gets the value of the dataInizioDifferimentoPena property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataInizioDifferimentoPena() {
		return dataInizioDifferimentoPena;
	}

	/**
	 * Sets the value of the dataInizioDifferimentoPena property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataInizioDifferimentoPena(XMLGregorianCalendar value) {
		this.dataInizioDifferimentoPena = value;
	}

	/**
	 * Gets the value of the dataFineDifferimentoPena property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataFineDifferimentoPena() {
		return dataFineDifferimentoPena;
	}

	/**
	 * Sets the value of the dataFineDifferimentoPena property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataFineDifferimentoPena(XMLGregorianCalendar value) {
		this.dataFineDifferimentoPena = value;
	}

	/**
	 * Gets the value of the dataInizioRevoca property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDataInizioRevoca() {
		return dataInizioRevoca;
	}

	/**
	 * Sets the value of the dataInizioRevoca property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDataInizioRevoca(XMLGregorianCalendar value) {
		this.dataInizioRevoca = value;
	}

	/**
	 * Gets the value of the penaDetentivaDaEspiareArresto property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getPenaDetentivaDaEspiareArresto() {
		return penaDetentivaDaEspiareArresto;
	}

	/**
	 * Sets the value of the penaDetentivaDaEspiareArresto property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setPenaDetentivaDaEspiareArresto(Durata value) {
		this.penaDetentivaDaEspiareArresto = value;
	}

	/**
	 * Gets the value of the penaDetentivaDaEspiareReclusione property.
	 * 
	 * @return possible object is {@link Durata }
	 * 
	 */
	public Durata getPenaDetentivaDaEspiareReclusione() {
		return penaDetentivaDaEspiareReclusione;
	}

	/**
	 * Sets the value of the penaDetentivaDaEspiareReclusione property.
	 * 
	 * @param value
	 *            allowed object is {@link Durata }
	 * 
	 */
	public void setPenaDetentivaDaEspiareReclusione(Durata value) {
		this.penaDetentivaDaEspiareReclusione = value;
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
