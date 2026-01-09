//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per TENORE_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="TENORE_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrOggettoTenore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrEsitoTenore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codOggettoTenore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TENORE_TYPE", propOrder = {
    "descrOggettoTenore",
    "descrEsitoTenore",
    "codOggettoTenore"
})
public class TENORETYPE {

    @XmlElement(required = true)
    protected String descrOggettoTenore;
    @XmlElement(required = true)
    protected String descrEsitoTenore;
    @XmlElement(required = true, nillable = true)
    protected String codOggettoTenore;

    /**
     * Recupera il valore della proprietà descrOggettoTenore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrOggettoTenore() {
        return descrOggettoTenore;
    }

    /**
     * Imposta il valore della proprietà descrOggettoTenore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrOggettoTenore(String value) {
        this.descrOggettoTenore = value;
    }

    /**
     * Recupera il valore della proprietà descrEsitoTenore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrEsitoTenore() {
        return descrEsitoTenore;
    }

    /**
     * Imposta il valore della proprietà descrEsitoTenore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrEsitoTenore(String value) {
        this.descrEsitoTenore = value;
    }

    /**
     * Recupera il valore della proprietà codOggettoTenore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOggettoTenore() {
        return codOggettoTenore;
    }

    /**
     * Imposta il valore della proprietà codOggettoTenore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOggettoTenore(String value) {
        this.codOggettoTenore = value;
    }

}
