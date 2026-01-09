//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element name="codiceTipoPA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceTipoDurataPA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroAnniPA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroMesiPA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroGiorniPA">
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
    "codiceTipoPA",
    "codiceTipoDurataPA",
    "numeroAnniPA",
    "numeroMesiPA",
    "numeroGiorniPA"
})
public class PenaAccessoria {

    @XmlElement(required = true, nillable = true)
    protected String codiceTipoPA;
    @XmlElement(required = true, nillable = true)
    protected String codiceTipoDurataPA;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroAnniPA;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroMesiPA;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroGiorniPA;

    /**
     * Recupera il valore della proprietï¿½ codiceTipoPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoPA() {
        return codiceTipoPA;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceTipoPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoPA(String value) {
        this.codiceTipoPA = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceTipoDurataPA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipoDurataPA() {
        return codiceTipoDurataPA;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceTipoDurataPA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipoDurataPA(String value) {
        this.codiceTipoDurataPA = value;
    }

    /**
     * Recupera il valore della proprietï¿½ numeroAnniPA.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroAnniPA() {
        return numeroAnniPA;
    }

    /**
     * Imposta il valore della proprietï¿½ numeroAnniPA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroAnniPA(BigInteger value) {
        this.numeroAnniPA = value;
    }

    /**
     * Recupera il valore della proprietï¿½ numeroMesiPA.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroMesiPA() {
        return numeroMesiPA;
    }

    /**
     * Imposta il valore della proprietï¿½ numeroMesiPA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroMesiPA(BigInteger value) {
        this.numeroMesiPA = value;
    }

    /**
     * Recupera il valore della proprietï¿½ numeroGiorniPA.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroGiorniPA() {
        return numeroGiorniPA;
    }

    /**
     * Imposta il valore della proprietï¿½ numeroGiorniPA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroGiorniPA(BigInteger value) {
        this.numeroGiorniPA = value;
    }

}
