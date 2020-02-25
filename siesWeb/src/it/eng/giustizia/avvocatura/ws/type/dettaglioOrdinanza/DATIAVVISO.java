//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
    "codiceFiscaleAvvocato"
})
public class DATIAVVISO {

    @XmlElement(required = true)
    protected BigInteger idAvviso;
    @XmlElement(required = true)
    protected String codiceFiscaleAvvocato;

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

}
