//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 03:51:09 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi;

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
 *         &lt;element name="dataEmissioneInizio" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaAvvisi}DATA_TYPE"/>
 *         &lt;element name="dataEmissioneFine" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaAvvisi}DATA_TYPE"/>
 *         &lt;element name="codStatoAvviso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codDistretto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceFiscaleAvvocato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "dataEmissioneInizio",
    "dataEmissioneFine",
    "codStatoAvviso",
    "codDistretto",
    "codiceFiscaleAvvocato",
    "codTipoUfficio"
})
@XmlRootElement(name = "DATI_AVVISO_INPUT")
public class DATIAVVISOINPUT {

    @XmlElement(required = true, nillable = true)
    protected DATATYPE dataEmissioneInizio;
    @XmlElement(required = true, nillable = true)
    protected DATATYPE dataEmissioneFine;
    @XmlElement(required = true)
    protected String codStatoAvviso;
    @XmlElement(required = true)
    protected String codDistretto;
    @XmlElement(required = true)
    protected String codiceFiscaleAvvocato;
    @XmlElement(required = true)
    protected String codTipoUfficio;

    /**
     * Recupera il valore della proprietà dataEmissioneInizio.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataEmissioneInizio() {
        return dataEmissioneInizio;
    }

    /**
     * Imposta il valore della proprietà dataEmissioneInizio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataEmissioneInizio(DATATYPE value) {
        this.dataEmissioneInizio = value;
    }

    /**
     * Recupera il valore della proprietà dataEmissioneFine.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataEmissioneFine() {
        return dataEmissioneFine;
    }

    /**
     * Imposta il valore della proprietà dataEmissioneFine.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataEmissioneFine(DATATYPE value) {
        this.dataEmissioneFine = value;
    }

    /**
     * Recupera il valore della proprietà codStatoAvviso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodStatoAvviso() {
        return codStatoAvviso;
    }

    /**
     * Imposta il valore della proprietà codStatoAvviso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodStatoAvviso(String value) {
        this.codStatoAvviso = value;
    }

    /**
     * Recupera il valore della proprietà codDistretto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDistretto() {
        return codDistretto;
    }

    /**
     * Imposta il valore della proprietà codDistretto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDistretto(String value) {
        this.codDistretto = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscaleAvvocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleAvvocato() {
        return codiceFiscaleAvvocato;
    }

    /**
     * Imposta il valore della proprietà codiceFiscaleAvvocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleAvvocato(String value) {
        this.codiceFiscaleAvvocato = value;
    }

    /**
     * Recupera il valore della proprietà codTipoUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoUfficio() {
        return codTipoUfficio;
    }

    /**
     * Imposta il valore della proprietà codTipoUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoUfficio(String value) {
        this.codTipoUfficio = value;
    }

}
