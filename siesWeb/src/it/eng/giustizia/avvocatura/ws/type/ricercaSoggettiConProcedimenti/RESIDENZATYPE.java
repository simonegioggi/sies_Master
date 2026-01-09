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
 * <p>Classe Java per RESIDENZA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="RESIDENZA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codTipoResidenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codStato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComune" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="indirizzo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descComuneEstero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RESIDENZA_TYPE", propOrder = {
    "codTipoResidenza",
    "codStato",
    "descrStato",
    "codProvincia",
    "descrComune",
    "indirizzo",
    "descComuneEstero"
})
public class RESIDENZATYPE {

    @XmlElement(required = true)
    protected String codTipoResidenza;
    @XmlElement(required = true)
    protected String codStato;
    @XmlElement(required = true)
    protected String descrStato;
    @XmlElement(required = true)
    protected String codProvincia;
    @XmlElement(required = true)
    protected String descrComune;
    @XmlElement(required = true)
    protected String indirizzo;
    @XmlElement(required = true)
    protected String descComuneEstero;

    /**
     * Recupera il valore della proprietà codTipoResidenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoResidenza() {
        return codTipoResidenza;
    }

    /**
     * Imposta il valore della proprietà codTipoResidenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoResidenza(String value) {
        this.codTipoResidenza = value;
    }

    /**
     * Recupera il valore della proprietà codStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodStato() {
        return codStato;
    }

    /**
     * Imposta il valore della proprietà codStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodStato(String value) {
        this.codStato = value;
    }

    /**
     * Recupera il valore della proprietà descrStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrStato() {
        return descrStato;
    }

    /**
     * Imposta il valore della proprietà descrStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrStato(String value) {
        this.descrStato = value;
    }

    /**
     * Recupera il valore della proprietà codProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodProvincia() {
        return codProvincia;
    }

    /**
     * Imposta il valore della proprietà codProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodProvincia(String value) {
        this.codProvincia = value;
    }

    /**
     * Recupera il valore della proprietà descrComune.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComune() {
        return descrComune;
    }

    /**
     * Imposta il valore della proprietà descrComune.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComune(String value) {
        this.descrComune = value;
    }

    /**
     * Recupera il valore della proprietà indirizzo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * Imposta il valore della proprietà indirizzo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndirizzo(String value) {
        this.indirizzo = value;
    }

    /**
     * Recupera il valore della proprietà descComuneEstero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescComuneEstero() {
        return descComuneEstero;
    }

    /**
     * Imposta il valore della proprietà descComuneEstero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescComuneEstero(String value) {
        this.descComuneEstero = value;
    }

}
