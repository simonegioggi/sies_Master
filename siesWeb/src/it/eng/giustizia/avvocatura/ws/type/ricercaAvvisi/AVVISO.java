//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 03:51:09 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="idAvviso" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="codiceFiscaleAvvocato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="contenuto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ufficioEmittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataInserimento" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaAvvisi}DATA_TYPE"/>
 *         &lt;element name="annoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaAvvisi}DATA_TYPE"/>
 *         &lt;element name="codStatoAvviso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idEvento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceEsito" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "idAvviso",
    "codiceFiscaleAvvocato",
    "descrTipoProvvedimento",
    "contenuto",
    "ufficioEmittente",
    "dataInserimento",
    "annoProcedimentoSIUS",
    "numeroProcedimentoSIUS",
    "nomeSoggetto",
    "cognomeSoggetto",
    "dataUdienza",
    "codStatoAvviso",
    "idEvento",
    "codTipoProvvedimento",
    "codiceEsito"
})
@XmlRootElement(name = "AVVISO")
public class AVVISO {

    @XmlElement(required = true)
    protected BigInteger idAvviso;
    @XmlElement(required = true)
    protected String codiceFiscaleAvvocato;
    @XmlElement(required = true)
    protected String descrTipoProvvedimento;
    @XmlElement(required = true)
    protected String contenuto;
    @XmlElement(required = true)
    protected String ufficioEmittente;
    @XmlElement(required = true)
    protected DATATYPE dataInserimento;
    @XmlElement(required = true)
    protected BigInteger annoProcedimentoSIUS;
    @XmlElement(required = true)
    protected BigInteger numeroProcedimentoSIUS;
    @XmlElement(required = true)
    protected String nomeSoggetto;
    @XmlElement(required = true)
    protected String cognomeSoggetto;
    @XmlElement(required = true)
    protected DATATYPE dataUdienza;
    @XmlElement(required = true)
    protected String codStatoAvviso;
    @XmlElement(required = true)
    protected String idEvento;
    @XmlElement(required = true)
    protected String codTipoProvvedimento;
    @XmlElement(required = true)
    protected String codiceEsito;

    /**
     * Recupera il valore della proprietà idAvviso.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdAvviso() {
        return idAvviso;
    }

    /**
     * Imposta il valore della proprietà idAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdAvviso(BigInteger value) {
        this.idAvviso = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscaleAvvocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleAvvocato() {
        return codiceFiscaleAvvocato;
    }

    /**
     * Imposta il valore della proprietà codiceFiscaleAvvocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleAvvocato(String value) {
        this.codiceFiscaleAvvocato = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoProvvedimento() {
        return descrTipoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà descrTipoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoProvvedimento(String value) {
        this.descrTipoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietà contenuto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContenuto() {
        return contenuto;
    }

    /**
     * Imposta il valore della proprietà contenuto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContenuto(String value) {
        this.contenuto = value;
    }

    /**
     * Recupera il valore della proprietà ufficioEmittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUfficioEmittente() {
        return ufficioEmittente;
    }

    /**
     * Imposta il valore della proprietà ufficioEmittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUfficioEmittente(String value) {
        this.ufficioEmittente = value;
    }

    /**
     * Recupera il valore della proprietà dataInserimento.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataInserimento() {
        return dataInserimento;
    }

    /**
     * Imposta il valore della proprietà dataInserimento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataInserimento(DATATYPE value) {
        this.dataInserimento = value;
    }

    /**
     * Recupera il valore della proprietà annoProcedimentoSIUS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoProcedimentoSIUS() {
        return annoProcedimentoSIUS;
    }

    /**
     * Imposta il valore della proprietà annoProcedimentoSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoProcedimentoSIUS(BigInteger value) {
        this.annoProcedimentoSIUS = value;
    }

    /**
     * Recupera il valore della proprietà numeroProcedimentoSIUS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroProcedimentoSIUS() {
        return numeroProcedimentoSIUS;
    }

    /**
     * Imposta il valore della proprietà numeroProcedimentoSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroProcedimentoSIUS(BigInteger value) {
        this.numeroProcedimentoSIUS = value;
    }

    /**
     * Recupera il valore della proprietà nomeSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeSoggetto() {
        return nomeSoggetto;
    }

    /**
     * Imposta il valore della proprietà nomeSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeSoggetto(String value) {
        this.nomeSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà cognomeSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognomeSoggetto() {
        return cognomeSoggetto;
    }

    /**
     * Imposta il valore della proprietà cognomeSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognomeSoggetto(String value) {
        this.cognomeSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà dataUdienza.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataUdienza() {
        return dataUdienza;
    }

    /**
     * Imposta il valore della proprietà dataUdienza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataUdienza(DATATYPE value) {
        this.dataUdienza = value;
    }

    /**
     * Recupera il valore della proprietà codStatoAvviso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodStatoAvviso() {
        return codStatoAvviso;
    }

    /**
     * Imposta il valore della proprietà codStatoAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodStatoAvviso(String value) {
        this.codStatoAvviso = value;
    }

    /**
     * Recupera il valore della proprietà idEvento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEvento() {
        return idEvento;
    }

    /**
     * Imposta il valore della proprietà idEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEvento(String value) {
        this.idEvento = value;
    }

    /**
     * Recupera il valore della proprietà codTipoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoProvvedimento() {
        return codTipoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà codTipoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoProvvedimento(String value) {
        this.codTipoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietà codiceEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEsito() {
        return codiceEsito;
    }

    /**
     * Imposta il valore della proprietà codiceEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEsito(String value) {
        this.codiceEsito = value;
    }

}
