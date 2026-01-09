//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per PERIODI_LIBERTA_ANTICIPATA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PERIODI_LIBERTA_ANTICIPATA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flagConcesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagScorta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoPermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiTipoLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroGiorniRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sommaRisarcimentoDanni" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="elencoPeriodi" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}PERIODI_TYPE" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PERIODI_LIBERTA_ANTICIPATA_TYPE", propOrder = {
    "flagConcesso",
    "flagScorta",
    "descrStatoPermesso",
    "codiTipoLicenza",
    "numeroGiorniRiduzione",
    "sommaRisarcimentoDanni",
    "elencoPeriodi"
})
public class PERIODILIBERTAANTICIPATATYPE {

    @XmlElement(required = true)
    protected String flagConcesso;
    @XmlElement(required = true)
    protected String flagScorta;
    @XmlElement(required = true)
    protected String descrStatoPermesso;
    @XmlElement(required = true)
    protected String codiTipoLicenza;
    @XmlElement(required = true)
    protected String numeroGiorniRiduzione;
    @XmlElement(required = true)
    protected BigDecimal sommaRisarcimentoDanni;
    @XmlElement(required = true)
    protected List<PERIODITYPE> elencoPeriodi;

    /**
     * Recupera il valore della proprietà flagConcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagConcesso() {
        return flagConcesso;
    }

    /**
     * Imposta il valore della proprietà flagConcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagConcesso(String value) {
        this.flagConcesso = value;
    }

    /**
     * Recupera il valore della proprietà flagScorta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagScorta() {
        return flagScorta;
    }

    /**
     * Imposta il valore della proprietà flagScorta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagScorta(String value) {
        this.flagScorta = value;
    }

    /**
     * Recupera il valore della proprietà descrStatoPermesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrStatoPermesso() {
        return descrStatoPermesso;
    }

    /**
     * Imposta il valore della proprietà descrStatoPermesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrStatoPermesso(String value) {
        this.descrStatoPermesso = value;
    }

    /**
     * Recupera il valore della proprietà codiTipoLicenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiTipoLicenza() {
        return codiTipoLicenza;
    }

    /**
     * Imposta il valore della proprietà codiTipoLicenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiTipoLicenza(String value) {
        this.codiTipoLicenza = value;
    }

    /**
     * Recupera il valore della proprietà numeroGiorniRiduzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroGiorniRiduzione() {
        return numeroGiorniRiduzione;
    }

    /**
     * Imposta il valore della proprietà numeroGiorniRiduzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroGiorniRiduzione(String value) {
        this.numeroGiorniRiduzione = value;
    }

    /**
     * Recupera il valore della proprietà sommaRisarcimentoDanni.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSommaRisarcimentoDanni() {
        return sommaRisarcimentoDanni;
    }

    /**
     * Imposta il valore della proprietà sommaRisarcimentoDanni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSommaRisarcimentoDanni(BigDecimal value) {
        this.sommaRisarcimentoDanni = value;
    }

    /**
     * Gets the value of the elencoPeriodi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoPeriodi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoPeriodi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PERIODITYPE }
     * 
     * 
     */
    public List<PERIODITYPE> getElencoPeriodi() {
        if (elencoPeriodi == null) {
            elencoPeriodi = new ArrayList<PERIODITYPE>();
        }
        return this.elencoPeriodi;
    }

}
