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
 * <p>Classe Java per SENTENZA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SENTENZA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idSentenza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroSentenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoSentenza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataProvvedimento" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrTipoAutoritaEmittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SENTENZA_TYPE", propOrder = {
    "idSentenza",
    "numeroSentenza",
    "annoSentenza",
    "dataProvvedimento",
    "descrTipoAutoritaEmittente"
})
public class SENTENZATYPE {

    @XmlElement(required = true)
    protected BigInteger idSentenza;
    @XmlElement(required = true)
    protected String numeroSentenza;
    @XmlElement(required = true)
    protected BigInteger annoSentenza;
    @XmlElement(required = true)
    protected DATATYPE dataProvvedimento;
    @XmlElement(required = true)
    protected String descrTipoAutoritaEmittente;

    /**
     * Recupera il valore della proprietà idSentenza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdSentenza() {
        return idSentenza;
    }

    /**
     * Imposta il valore della proprietà idSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdSentenza(BigInteger value) {
        this.idSentenza = value;
    }

    /**
     * Recupera il valore della proprietà numeroSentenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSentenza() {
        return numeroSentenza;
    }

    /**
     * Imposta il valore della proprietà numeroSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSentenza(String value) {
        this.numeroSentenza = value;
    }

    /**
     * Recupera il valore della proprietà annoSentenza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoSentenza() {
        return annoSentenza;
    }

    /**
     * Imposta il valore della proprietà annoSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoSentenza(BigInteger value) {
        this.annoSentenza = value;
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

}
