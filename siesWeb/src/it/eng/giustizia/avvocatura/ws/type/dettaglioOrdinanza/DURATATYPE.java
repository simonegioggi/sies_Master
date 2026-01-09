//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DURATA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DURATA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anni" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="mesi" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="giorni" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DURATA_TYPE", propOrder = {
    "anni",
    "mesi",
    "giorni"
})
public class DURATATYPE {

    @XmlElement(required = true)
    protected BigInteger anni;
    @XmlElement(required = true)
    protected BigInteger mesi;
    @XmlElement(required = true)
    protected BigInteger giorni;

    /**
     * Recupera il valore della proprietà anni.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnni() {
        return anni;
    }

    /**
     * Imposta il valore della proprietà anni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnni(BigInteger value) {
        this.anni = value;
    }

    /**
     * Recupera il valore della proprietà mesi.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMesi() {
        return mesi;
    }

    /**
     * Imposta il valore della proprietà mesi.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMesi(BigInteger value) {
        this.mesi = value;
    }

    /**
     * Recupera il valore della proprietà giorni.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGiorni() {
        return giorni;
    }

    /**
     * Imposta il valore della proprietà giorni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGiorni(BigInteger value) {
        this.giorni = value;
    }

}
