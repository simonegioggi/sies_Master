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
 * <p>Classe Java per MOTIVAZIONI_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MOTIVAZIONI_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrMotivazioniDecreto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrAltreMotivazioni" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MOTIVAZIONI_TYPE", propOrder = {
    "descrMotivazioniDecreto",
    "descrAltreMotivazioni"
})
public class MOTIVAZIONITYPE {

    @XmlElement(required = true)
    protected String descrMotivazioniDecreto;
    @XmlElement(required = true)
    protected String descrAltreMotivazioni;

    /**
     * Recupera il valore della proprietà descrMotivazioniDecreto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrMotivazioniDecreto() {
        return descrMotivazioniDecreto;
    }

    /**
     * Imposta il valore della proprietà descrMotivazioniDecreto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrMotivazioniDecreto(String value) {
        this.descrMotivazioniDecreto = value;
    }

    /**
     * Recupera il valore della proprietà descrAltreMotivazioni.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrAltreMotivazioni() {
        return descrAltreMotivazioni;
    }

    /**
     * Imposta il valore della proprietà descrAltreMotivazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrAltreMotivazioni(String value) {
        this.descrAltreMotivazioni = value;
    }

}
