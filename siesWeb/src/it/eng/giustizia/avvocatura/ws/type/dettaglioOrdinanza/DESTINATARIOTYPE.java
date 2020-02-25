//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DESTINATARIO_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DESTINATARIO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoAutoritaEsterna" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrSedeAutoritaEsterna" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoIstitutoDeten" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneIstitutoDeten" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avvocatoSIEP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="avvocatoSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoCuratore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="curatore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="indirizzoCSSA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comuneCSSA" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DESTINATARIO_TYPE", propOrder = {
    "descrTipoUfficio",
    "descrComuneUfficio",
    "descrTipoAutoritaEsterna",
    "note",
    "descrSedeAutoritaEsterna",
    "descrTipoIstitutoDeten",
    "descrComuneIstitutoDeten",
    "idSoggetto",
    "avvocatoSIEP",
    "avvocatoSIUS",
    "tipoCuratore",
    "curatore",
    "indirizzoCSSA",
    "comuneCSSA"
})
public class DESTINATARIOTYPE {

    @XmlElement(required = true)
    protected String descrTipoUfficio;
    @XmlElement(required = true)
    protected String descrComuneUfficio;
    @XmlElement(required = true)
    protected String descrTipoAutoritaEsterna;
    @XmlElement(required = true)
    protected String note;
    @XmlElement(required = true)
    protected String descrSedeAutoritaEsterna;
    @XmlElement(required = true)
    protected String descrTipoIstitutoDeten;
    @XmlElement(required = true)
    protected String descrComuneIstitutoDeten;
    @XmlElement(required = true)
    protected String idSoggetto;
    @XmlElement(required = true)
    protected String avvocatoSIEP;
    @XmlElement(required = true)
    protected String avvocatoSIUS;
    @XmlElement(required = true)
    protected String tipoCuratore;
    @XmlElement(required = true)
    protected String curatore;
    @XmlElement(required = true)
    protected String indirizzoCSSA;
    @XmlElement(required = true)
    protected String comuneCSSA;

    /**
     * Recupera il valore della proprietà descrTipoUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoUfficio() {
        return descrTipoUfficio;
    }

    /**
     * Imposta il valore della proprietà descrTipoUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoUfficio(String value) {
        this.descrTipoUfficio = value;
    }

    /**
     * Recupera il valore della proprietà descrComuneUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComuneUfficio() {
        return descrComuneUfficio;
    }

    /**
     * Imposta il valore della proprietà descrComuneUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComuneUfficio(String value) {
        this.descrComuneUfficio = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoAutoritaEsterna.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoAutoritaEsterna() {
        return descrTipoAutoritaEsterna;
    }

    /**
     * Imposta il valore della proprietà descrTipoAutoritaEsterna.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoAutoritaEsterna(String value) {
        this.descrTipoAutoritaEsterna = value;
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

    /**
     * Recupera il valore della proprietà descrSedeAutoritaEsterna.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrSedeAutoritaEsterna() {
        return descrSedeAutoritaEsterna;
    }

    /**
     * Imposta il valore della proprietà descrSedeAutoritaEsterna.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrSedeAutoritaEsterna(String value) {
        this.descrSedeAutoritaEsterna = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoIstitutoDeten.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoIstitutoDeten() {
        return descrTipoIstitutoDeten;
    }

    /**
     * Imposta il valore della proprietà descrTipoIstitutoDeten.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoIstitutoDeten(String value) {
        this.descrTipoIstitutoDeten = value;
    }

    /**
     * Recupera il valore della proprietà descrComuneIstitutoDeten.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComuneIstitutoDeten() {
        return descrComuneIstitutoDeten;
    }

    /**
     * Imposta il valore della proprietà descrComuneIstitutoDeten.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComuneIstitutoDeten(String value) {
        this.descrComuneIstitutoDeten = value;
    }

    /**
     * Recupera il valore della proprietà idSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdSoggetto() {
        return idSoggetto;
    }

    /**
     * Imposta il valore della proprietà idSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdSoggetto(String value) {
        this.idSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà avvocatoSIEP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvvocatoSIEP() {
        return avvocatoSIEP;
    }

    /**
     * Imposta il valore della proprietà avvocatoSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvvocatoSIEP(String value) {
        this.avvocatoSIEP = value;
    }

    /**
     * Recupera il valore della proprietà avvocatoSIUS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvvocatoSIUS() {
        return avvocatoSIUS;
    }

    /**
     * Imposta il valore della proprietà avvocatoSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvvocatoSIUS(String value) {
        this.avvocatoSIUS = value;
    }

    /**
     * Recupera il valore della proprietà tipoCuratore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCuratore() {
        return tipoCuratore;
    }

    /**
     * Imposta il valore della proprietà tipoCuratore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCuratore(String value) {
        this.tipoCuratore = value;
    }

    /**
     * Recupera il valore della proprietà curatore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuratore() {
        return curatore;
    }

    /**
     * Imposta il valore della proprietà curatore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuratore(String value) {
        this.curatore = value;
    }

    /**
     * Recupera il valore della proprietà indirizzoCSSA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzoCSSA() {
        return indirizzoCSSA;
    }

    /**
     * Imposta il valore della proprietà indirizzoCSSA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzoCSSA(String value) {
        this.indirizzoCSSA = value;
    }

    /**
     * Recupera il valore della proprietà comuneCSSA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneCSSA() {
        return comuneCSSA;
    }

    /**
     * Imposta il valore della proprietà comuneCSSA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneCSSA(String value) {
        this.comuneCSSA = value;
    }

}
