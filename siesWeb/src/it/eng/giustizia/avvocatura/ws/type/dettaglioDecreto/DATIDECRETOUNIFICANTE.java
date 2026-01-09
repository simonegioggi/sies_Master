//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="luogoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroSIEP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataUnificazione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="fascicoloUnificato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoOggetti" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
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
    "cognomeSoggetto",
    "nomeSoggetto",
    "dataNascita",
    "luogoNascita",
    "numeroSIUS",
    "numeroSIEP",
    "dataUnificazione",
    "fascicoloUnificato",
    "elencoOggetti"
})
@XmlRootElement(name = "DATI_DECRETO_UNIFICANTE")
public class DATIDECRETOUNIFICANTE {

    @XmlElement(required = true)
    protected String cognomeSoggetto;
    @XmlElement(required = true)
    protected String nomeSoggetto;
    @XmlElement(required = true)
    protected DATATYPE dataNascita;
    @XmlElement(required = true)
    protected String luogoNascita;
    @XmlElement(required = true)
    protected String numeroSIUS;
    @XmlElement(required = true)
    protected String numeroSIEP;
    @XmlElement(required = true)
    protected DATATYPE dataUnificazione;
    @XmlElement(required = true)
    protected String fascicoloUnificato;
    @XmlElement(required = true)
    protected List<String> elencoOggetti;

    /**
     * Recupera il valore della proprietà cognomeSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognomeSoggetto() {
        return cognomeSoggetto;
    }

    /**
     * Imposta il valore della proprietà cognomeSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognomeSoggetto(String value) {
        this.cognomeSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà nomeSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeSoggetto() {
        return nomeSoggetto;
    }

    /**
     * Imposta il valore della proprietà nomeSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeSoggetto(String value) {
        this.nomeSoggetto = value;
    }

    /**
     * Recupera il valore della proprietà dataNascita.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta il valore della proprietà dataNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataNascita(DATATYPE value) {
        this.dataNascita = value;
    }

    /**
     * Recupera il valore della proprietà luogoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLuogoNascita() {
        return luogoNascita;
    }

    /**
     * Imposta il valore della proprietà luogoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLuogoNascita(String value) {
        this.luogoNascita = value;
    }

    /**
     * Recupera il valore della proprietà numeroSIUS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSIUS() {
        return numeroSIUS;
    }

    /**
     * Imposta il valore della proprietà numeroSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSIUS(String value) {
        this.numeroSIUS = value;
    }

    /**
     * Recupera il valore della proprietà numeroSIEP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSIEP() {
        return numeroSIEP;
    }

    /**
     * Imposta il valore della proprietà numeroSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSIEP(String value) {
        this.numeroSIEP = value;
    }

    /**
     * Recupera il valore della proprietà dataUnificazione.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataUnificazione() {
        return dataUnificazione;
    }

    /**
     * Imposta il valore della proprietà dataUnificazione.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataUnificazione(DATATYPE value) {
        this.dataUnificazione = value;
    }

    /**
     * Recupera il valore della proprietà fascicoloUnificato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFascicoloUnificato() {
        return fascicoloUnificato;
    }

    /**
     * Imposta il valore della proprietà fascicoloUnificato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFascicoloUnificato(String value) {
        this.fascicoloUnificato = value;
    }

    /**
     * Gets the value of the elencoOggetti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoOggetti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoOggetti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getElencoOggetti() {
        if (elencoOggetti == null) {
            elencoOggetti = new ArrayList<String>();
        }
        return this.elencoOggetti;
    }

}
