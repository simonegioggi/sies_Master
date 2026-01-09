//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.26 alle 04:01:02 PM CEST 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="codiceTipo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceSede">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceDistretto">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceSistema">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *               &lt;enumeration value="SIGE"/>
 *               &lt;enumeration value="SIUS"/>
 *               &lt;enumeration value="SIEP"/>
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
    "codiceTipo",
    "codiceSede",
    "codiceDistretto",
    "codiceSistema"
})
@XmlRootElement(name = "Ufficio")
public class Ufficio {

    @XmlElement(required = true)
    protected String codiceTipo;
    @XmlElement(required = true)
    protected String codiceSede;
    @XmlElement(required = true)
    protected String codiceDistretto;
    @XmlElement(required = true)
    protected String codiceSistema;

    /**
     * Recupera il valore della proprietï¿½ codiceTipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceTipo() {
        return codiceTipo;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceTipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceTipo(String value) {
        this.codiceTipo = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceSede() {
        return codiceSede;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceSede(String value) {
        this.codiceSede = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceDistretto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceDistretto() {
        return codiceDistretto;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceDistretto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceDistretto(String value) {
        this.codiceDistretto = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceSistema.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceSistema() {
        return codiceSistema;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceSistema.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceSistema(String value) {
        this.codiceSistema = value;
    }

}
