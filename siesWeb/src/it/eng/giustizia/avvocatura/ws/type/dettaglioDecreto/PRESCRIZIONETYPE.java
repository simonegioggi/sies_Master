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
 * <p>Classe Java per PRESCRIZIONE_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PRESCRIZIONE_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrTipoPrescrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrAltraPrescrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRESCRIZIONE_TYPE", propOrder = {
    "descrTipoPrescrizione",
    "descrAltraPrescrizione"
})
public class PRESCRIZIONETYPE {

    @XmlElement(required = true)
    protected String descrTipoPrescrizione;
    @XmlElement(required = true)
    protected String descrAltraPrescrizione;

    /**
     * Recupera il valore della proprietà descrTipoPrescrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoPrescrizione() {
        return descrTipoPrescrizione;
    }

    /**
     * Imposta il valore della proprietà descrTipoPrescrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoPrescrizione(String value) {
        this.descrTipoPrescrizione = value;
    }

    /**
     * Recupera il valore della proprietà descrAltraPrescrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrAltraPrescrizione() {
        return descrAltraPrescrizione;
    }

    /**
     * Imposta il valore della proprietà descrAltraPrescrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrAltraPrescrizione(String value) {
        this.descrAltraPrescrizione = value;
    }

}
