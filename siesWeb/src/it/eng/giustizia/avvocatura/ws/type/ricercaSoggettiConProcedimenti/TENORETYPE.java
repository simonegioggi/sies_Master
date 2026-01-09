//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

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
 *         &lt;element name="descrOggettoTenore" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "note"
})
public class TENORETYPE {

    @XmlElement(required = true)
    protected Object descrOggettoTenore;
    @XmlElement(required = true)
    protected String note;

    /**
     * Recupera il valore della proprietà descrOggettoTenore.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDescrOggettoTenore() {
        return descrOggettoTenore;
    }

    /**
     * Imposta il valore della proprietà descrOggettoTenore.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDescrOggettoTenore(Object value) {
        this.descrOggettoTenore = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

}
