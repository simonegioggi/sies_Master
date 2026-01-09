//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.26 alle 04:01:02 PM CEST 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ChiaviAnagrafica"/>
 *         &lt;element name="cognome">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nome">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceLuogoNascita">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceStatoEsteroNascita">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="descrizioneComuneEstero">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="60"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="sesso">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceFiscale">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceImprontaDigitale">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="7"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nomePadre">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="flagAliasRichiamo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "chiaviAnagrafica",
    "cognome",
    "nome",
    "codiceLuogoNascita",
    "codiceStatoEsteroNascita",
    "descrizioneComuneEstero",
    "dataNascita",
    "sesso",
    "codiceFiscale",
    "codiceImprontaDigitale",
    "nomePadre",
    "flagAliasRichiamo"
})
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
     * Recupera il valore della proprietï¿½ chiaviAnagrafica.
     * 
     * @return
     *     possible object is
     *     {@link ChiaviAnagrafica }
     *     
     */
    public ChiaviAnagrafica getChiaviAnagrafica() {
        return chiaviAnagrafica;
    }

    /**
     * Imposta il valore della proprietï¿½ chiaviAnagrafica.
     * 
     * @param value
     *     allowed object is
     *     {@link ChiaviAnagrafica }
     *     
     */
    public void setChiaviAnagrafica(ChiaviAnagrafica value) {
        this.chiaviAnagrafica = value;
    }

    /**
     * Recupera il valore della proprietï¿½ cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietï¿½ cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietï¿½ nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietï¿½ nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceLuogoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceLuogoNascita() {
        return codiceLuogoNascita;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceLuogoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceLuogoNascita(String value) {
        this.codiceLuogoNascita = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceStatoEsteroNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceStatoEsteroNascita() {
        return codiceStatoEsteroNascita;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceStatoEsteroNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceStatoEsteroNascita(String value) {
        this.codiceStatoEsteroNascita = value;
    }

    /**
     * Recupera il valore della proprietï¿½ descrizioneComuneEstero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneComuneEstero() {
        return descrizioneComuneEstero;
    }

    /**
     * Imposta il valore della proprietï¿½ descrizioneComuneEstero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneComuneEstero(String value) {
        this.descrizioneComuneEstero = value;
    }

    /**
     * Recupera il valore della proprietï¿½ dataNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta il valore della proprietï¿½ dataNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataNascita(XMLGregorianCalendar value) {
        this.dataNascita = value;
    }

    /**
     * Recupera il valore della proprietï¿½ sesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietï¿½ sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceImprontaDigitale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceImprontaDigitale() {
        return codiceImprontaDigitale;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceImprontaDigitale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceImprontaDigitale(String value) {
        this.codiceImprontaDigitale = value;
    }

    /**
     * Recupera il valore della proprietï¿½ nomePadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomePadre() {
        return nomePadre;
    }

    /**
     * Imposta il valore della proprietï¿½ nomePadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomePadre(String value) {
        this.nomePadre = value;
    }

    /**
     * Recupera il valore della proprietï¿½ flagAliasRichiamo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagAliasRichiamo() {
        return flagAliasRichiamo;
    }

    /**
     * Imposta il valore della proprietï¿½ flagAliasRichiamo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagAliasRichiamo(String value) {
        this.flagAliasRichiamo = value;
    }

}
