//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari;

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
 *         &lt;element name="codiceTipoMS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceTipoDurataMS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroAnniMS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroMesiMS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroGiorniMS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="3"/>
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
    "codiceTipoMS",
    "codiceTipoDurataMS",
    "numeroAnniMS",
    "numeroMesiMS",
    "numeroGiorniMS"
})
public class MisuraSicurezza {

    @XmlElement(required = true, nillable = true)
    protected String codiceTipoMS;
    @XmlElement(required = true, nillable = true)
    protected String codiceTipoDurataMS;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroAnniMS;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroMesiMS;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroGiorniMS;

    /**
     * Recupera il valore della proprietà codiceTipoMS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoMS() {
        return codiceTipoMS;
    }

    /**
     * Imposta il valore della proprietà codiceTipoMS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoMS(String value) {
        this.codiceTipoMS = value;
    }

    /**
     * Recupera il valore della proprietà codiceTipoDurataMS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoDurataMS() {
        return codiceTipoDurataMS;
    }

    /**
     * Imposta il valore della proprietà codiceTipoDurataMS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoDurataMS(String value) {
        this.codiceTipoDurataMS = value;
    }

    /**
     * Recupera il valore della proprietà numeroAnniMS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroAnniMS() {
        return numeroAnniMS;
    }

    /**
     * Imposta il valore della proprietà numeroAnniMS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroAnniMS(BigInteger value) {
        this.numeroAnniMS = value;
    }

    /**
     * Recupera il valore della proprietà numeroMesiMS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroMesiMS() {
        return numeroMesiMS;
    }

    /**
     * Imposta il valore della proprietà numeroMesiMS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroMesiMS(BigInteger value) {
        this.numeroMesiMS = value;
    }

    /**
     * Recupera il valore della proprietà numeroGiorniMS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroGiorniMS() {
        return numeroGiorniMS;
    }

    /**
     * Imposta il valore della proprietà numeroGiorniMS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroGiorniMS(BigInteger value) {
        this.numeroGiorniMS = value;
    }

}
