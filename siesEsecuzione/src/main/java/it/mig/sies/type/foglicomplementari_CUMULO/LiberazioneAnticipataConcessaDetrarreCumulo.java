//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

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
 *         &lt;element name="giorniLAOrdinaria">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="giorniLASpeciale">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="giorniLAIntegrazione">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="giorniLARisarcimento">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="4"/>
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
    "giorniLAOrdinaria",
    "giorniLASpeciale",
    "giorniLAIntegrazione",
    "giorniLARisarcimento"
})
public class LiberazioneAnticipataConcessaDetrarreCumulo {

    @XmlElement(required = true, nillable = true)
    protected BigInteger giorniLAOrdinaria;
    @XmlElement(required = true, nillable = true)
    protected BigInteger giorniLASpeciale;
    @XmlElement(required = true, nillable = true)
    protected BigInteger giorniLAIntegrazione;
    @XmlElement(required = true, nillable = true)
    protected BigInteger giorniLARisarcimento;

    /**
     * Recupera il valore della proprietï¿½ giorniLAOrdinaria.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGiorniLAOrdinaria() {
        return giorniLAOrdinaria;
    }

    /**
     * Imposta il valore della proprietï¿½ giorniLAOrdinaria.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGiorniLAOrdinaria(BigInteger value) {
        this.giorniLAOrdinaria = value;
    }

    /**
     * Recupera il valore della proprietï¿½ giorniLASpeciale.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGiorniLASpeciale() {
        return giorniLASpeciale;
    }

    /**
     * Imposta il valore della proprietï¿½ giorniLASpeciale.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGiorniLASpeciale(BigInteger value) {
        this.giorniLASpeciale = value;
    }

    /**
     * Recupera il valore della proprietï¿½ giorniLAIntegrazione.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGiorniLAIntegrazione() {
        return giorniLAIntegrazione;
    }

    /**
     * Imposta il valore della proprietï¿½ giorniLAIntegrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGiorniLAIntegrazione(BigInteger value) {
        this.giorniLAIntegrazione = value;
    }

    /**
     * Recupera il valore della proprietï¿½ giorniLARisarcimento.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGiorniLARisarcimento() {
        return giorniLARisarcimento;
    }

    /**
     * Imposta il valore della proprietï¿½ giorniLARisarcimento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGiorniLARisarcimento(BigInteger value) {
        this.giorniLARisarcimento = value;
    }

}
