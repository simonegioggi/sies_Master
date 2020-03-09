//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RIFERIMENTO_FASC_SIEP_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RIFERIMENTO_FASC_SIEP_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idRiferimentoFascicoloSiep" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="flagMS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoFascicoloSiep" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="progrFascicoloSiep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrUffFascicoloSiep" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataProvvedimento" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrTipoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoAutoritaEmittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrLuogoEmittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataIrrevocabilita" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="annoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RIFERIMENTO_FASC_SIEP_TYPE", propOrder = {
    "idRiferimentoFascicoloSiep",
    "flagMS",
    "annoFascicoloSiep",
    "progrFascicoloSiep",
    "descrUffFascicoloSiep",
    "dataProvvedimento",
    "descrTipoProvvedimento",
    "descrTipoAutoritaEmittente",
    "descrLuogoEmittente",
    "dataIrrevocabilita",
    "annoProvvedimento",
    "numeroProvvedimento"
})
public class RIFERIMENTOFASCSIEPTYPE {

    @XmlElement(required = true)
    protected BigInteger idRiferimentoFascicoloSiep;
    @XmlElement(required = true)
    protected String flagMS;
    @XmlElement(required = true)
    protected BigInteger annoFascicoloSiep;
    @XmlElement(required = true)
    protected String progrFascicoloSiep;
    @XmlElement(required = true)
    protected String descrUffFascicoloSiep;
    @XmlElement(required = true)
    protected DATATYPE dataProvvedimento;
    @XmlElement(required = true)
    protected String descrTipoProvvedimento;
    @XmlElement(required = true)
    protected String descrTipoAutoritaEmittente;
    @XmlElement(required = true)
    protected String descrLuogoEmittente;
    @XmlElement(required = true)
    protected DATATYPE dataIrrevocabilita;
    @XmlElement(required = true)
    protected BigInteger annoProvvedimento;
    @XmlElement(required = true)
    protected String numeroProvvedimento;

    /**
     * Recupera il valore della proprietà idRiferimentoFascicoloSiep.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdRiferimentoFascicoloSiep() {
        return idRiferimentoFascicoloSiep;
    }

    /**
     * Imposta il valore della proprietà idRiferimentoFascicoloSiep.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdRiferimentoFascicoloSiep(BigInteger value) {
        this.idRiferimentoFascicoloSiep = value;
    }

    /**
     * Recupera il valore della proprietà flagMS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagMS() {
        return flagMS;
    }

    /**
     * Imposta il valore della proprietà flagMS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagMS(String value) {
        this.flagMS = value;
    }

    /**
     * Recupera il valore della proprietà annoFascicoloSiep.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoFascicoloSiep() {
        return annoFascicoloSiep;
    }

    /**
     * Imposta il valore della proprietà annoFascicoloSiep.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoFascicoloSiep(BigInteger value) {
        this.annoFascicoloSiep = value;
    }

    /**
     * Recupera il valore della proprietà progrFascicoloSiep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgrFascicoloSiep() {
        return progrFascicoloSiep;
    }

    /**
     * Imposta il valore della proprietà progrFascicoloSiep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgrFascicoloSiep(String value) {
        this.progrFascicoloSiep = value;
    }

    /**
     * Recupera il valore della proprietà descrUffFascicoloSiep.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrUffFascicoloSiep() {
        return descrUffFascicoloSiep;
    }

    /**
     * Imposta il valore della proprietà descrUffFascicoloSiep.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrUffFascicoloSiep(String value) {
        this.descrUffFascicoloSiep = value;
    }

    /**
     * Recupera il valore della proprietà dataProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataProvvedimento() {
        return dataProvvedimento;
    }

    /**
     * Imposta il valore della proprietà dataProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataProvvedimento(DATATYPE value) {
        this.dataProvvedimento = value;
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
     * Recupera il valore della proprietà descrTipoAutoritaEmittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoAutoritaEmittente() {
        return descrTipoAutoritaEmittente;
    }

    /**
     * Imposta il valore della proprietà descrTipoAutoritaEmittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoAutoritaEmittente(String value) {
        this.descrTipoAutoritaEmittente = value;
    }

    /**
     * Recupera il valore della proprietà descrLuogoEmittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrLuogoEmittente() {
        return descrLuogoEmittente;
    }

    /**
     * Imposta il valore della proprietà descrLuogoEmittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrLuogoEmittente(String value) {
        this.descrLuogoEmittente = value;
    }

    /**
     * Recupera il valore della proprietà dataIrrevocabilita.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataIrrevocabilita() {
        return dataIrrevocabilita;
    }

    /**
     * Imposta il valore della proprietà dataIrrevocabilita.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataIrrevocabilita(DATATYPE value) {
        this.dataIrrevocabilita = value;
    }

    /**
     * Recupera il valore della proprietà annoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoProvvedimento() {
        return annoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà annoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoProvvedimento(BigInteger value) {
        this.annoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietà numeroProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroProvvedimento() {
        return numeroProvvedimento;
    }

    /**
     * Imposta il valore della proprietà numeroProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroProvvedimento(String value) {
        this.numeroProvvedimento = value;
    }

}
