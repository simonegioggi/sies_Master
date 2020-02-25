//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 10:47:00 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DESTINATARI_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DESTINATARI_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrUffiicio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrAutorita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrSedeAutorita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoIstitutoDetenzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneIstitutoDetenzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avvocato_fasc_SIEP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avvocato_fasc_SIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="indirizzo_UEPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comune_UEPE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DESTINATARI_TYPE", propOrder = {
    "descrUffiicio",
    "descrAutorita",
    "descrSedeAutorita",
    "descrTipoIstitutoDetenzione",
    "descrComuneIstitutoDetenzione",
    "avvocatoFascSIEP",
    "avvocatoFascSIUS",
    "indirizzoUEPE",
    "comuneUEPE"
})
public class DESTINATARITYPE {

    @XmlElement(required = true)
    protected String descrUffiicio;
    @XmlElement(required = true)
    protected String descrAutorita;
    @XmlElement(required = true)
    protected String descrSedeAutorita;
    @XmlElement(required = true)
    protected String descrTipoIstitutoDetenzione;
    @XmlElement(required = true)
    protected String descrComuneIstitutoDetenzione;
    @XmlElement(name = "avvocato_fasc_SIEP", required = true)
    protected String avvocatoFascSIEP;
    @XmlElement(name = "avvocato_fasc_SIUS", required = true)
    protected String avvocatoFascSIUS;
    @XmlElement(name = "indirizzo_UEPE", required = true)
    protected String indirizzoUEPE;
    @XmlElement(name = "comune_UEPE", required = true)
    protected String comuneUEPE;

    /**
     * Recupera il valore della proprietà descrUffiicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrUffiicio() {
        return descrUffiicio;
    }

    /**
     * Imposta il valore della proprietà descrUffiicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrUffiicio(String value) {
        this.descrUffiicio = value;
    }

    /**
     * Recupera il valore della proprietà descrAutorita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrAutorita() {
        return descrAutorita;
    }

    /**
     * Imposta il valore della proprietà descrAutorita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrAutorita(String value) {
        this.descrAutorita = value;
    }

    /**
     * Recupera il valore della proprietà descrSedeAutorita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrSedeAutorita() {
        return descrSedeAutorita;
    }

    /**
     * Imposta il valore della proprietà descrSedeAutorita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrSedeAutorita(String value) {
        this.descrSedeAutorita = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoIstitutoDetenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoIstitutoDetenzione() {
        return descrTipoIstitutoDetenzione;
    }

    /**
     * Imposta il valore della proprietà descrTipoIstitutoDetenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoIstitutoDetenzione(String value) {
        this.descrTipoIstitutoDetenzione = value;
    }

    /**
     * Recupera il valore della proprietà descrComuneIstitutoDetenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComuneIstitutoDetenzione() {
        return descrComuneIstitutoDetenzione;
    }

    /**
     * Imposta il valore della proprietà descrComuneIstitutoDetenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComuneIstitutoDetenzione(String value) {
        this.descrComuneIstitutoDetenzione = value;
    }

    /**
     * Recupera il valore della proprietà avvocatoFascSIEP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvvocatoFascSIEP() {
        return avvocatoFascSIEP;
    }

    /**
     * Imposta il valore della proprietà avvocatoFascSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvvocatoFascSIEP(String value) {
        this.avvocatoFascSIEP = value;
    }

    /**
     * Recupera il valore della proprietà avvocatoFascSIUS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvvocatoFascSIUS() {
        return avvocatoFascSIUS;
    }

    /**
     * Imposta il valore della proprietà avvocatoFascSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvvocatoFascSIUS(String value) {
        this.avvocatoFascSIUS = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoUEPE.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoUEPE() {
        return indirizzoUEPE;
    }

    /**
     * Imposta il valore della proprietà indirizzoUEPE.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoUEPE(String value) {
        this.indirizzoUEPE = value;
    }

    /**
     * Recupera il valore della proprietà comuneUEPE.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneUEPE() {
        return comuneUEPE;
    }

    /**
     * Imposta il valore della proprietà comuneUEPE.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneUEPE(String value) {
        this.comuneUEPE = value;
    }

}
