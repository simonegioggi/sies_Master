//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per EVENTO_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="EVENTO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idEvento" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrTipoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrMotivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrEsito" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataDeposito" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="altreInformazioni" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventoIdEventoRevoca" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoImpugnazioniOpposizioni" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}IMPUGNAZIONE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="flagDocumentoRegistrato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagDepositoValidato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EVENTO_TYPE", propOrder = {
    "idEvento",
    "dataEmissione",
    "descrTipoProvvedimento",
    "descrMotivo",
    "descrEsito",
    "dataDeposito",
    "altreInformazioni",
    "codTipoProvvedimento",
    "eventoIdEventoRevoca",
    "elencoImpugnazioniOpposizioni",
    "flagDocumentoRegistrato",
    "flagDepositoValidato"
})
public class EVENTOTYPE {

    @XmlElement(required = true)
    protected BigInteger idEvento;
    @XmlElement(required = true)
    protected DATATYPE dataEmissione;
    @XmlElement(required = true)
    protected String descrTipoProvvedimento;
    @XmlElement(required = true)
    protected String descrMotivo;
    @XmlElement(required = true)
    protected String descrEsito;
    @XmlElement(required = true)
    protected DATATYPE dataDeposito;
    @XmlElement(required = true)
    protected String altreInformazioni;
    @XmlElement(required = true)
    protected String codTipoProvvedimento;
    @XmlElement(required = true)
    protected String eventoIdEventoRevoca;
    @XmlElement(required = true)
    protected List<IMPUGNAZIONETYPE> elencoImpugnazioniOpposizioni;
    @XmlElement(required = true)
    protected String flagDocumentoRegistrato;
    @XmlElement(required = true)
    protected String flagDepositoValidato;

    /**
     * Recupera il valore della proprietà idEvento.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdEvento() {
        return idEvento;
    }

    /**
     * Imposta il valore della proprietà idEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdEvento(BigInteger value) {
        this.idEvento = value;
    }

    /**
     * Recupera il valore della proprietà dataEmissione.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataEmissione() {
        return dataEmissione;
    }

    /**
     * Imposta il valore della proprietà dataEmissione.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataEmissione(DATATYPE value) {
        this.dataEmissione = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoProvvedimento() {
        return descrTipoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà descrTipoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoProvvedimento(String value) {
        this.descrTipoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietà descrMotivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrMotivo() {
        return descrMotivo;
    }

    /**
     * Imposta il valore della proprietà descrMotivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrMotivo(String value) {
        this.descrMotivo = value;
    }

    /**
     * Recupera il valore della proprietà descrEsito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrEsito() {
        return descrEsito;
    }

    /**
     * Imposta il valore della proprietà descrEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrEsito(String value) {
        this.descrEsito = value;
    }

    /**
     * Recupera il valore della proprietà dataDeposito.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataDeposito() {
        return dataDeposito;
    }

    /**
     * Imposta il valore della proprietà dataDeposito.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataDeposito(DATATYPE value) {
        this.dataDeposito = value;
    }

    /**
     * Recupera il valore della proprietà altreInformazioni.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltreInformazioni() {
        return altreInformazioni;
    }

    /**
     * Imposta il valore della proprietà altreInformazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltreInformazioni(String value) {
        this.altreInformazioni = value;
    }

    /**
     * Recupera il valore della proprietà codTipoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoProvvedimento() {
        return codTipoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà codTipoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoProvvedimento(String value) {
        this.codTipoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietà eventoIdEventoRevoca.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventoIdEventoRevoca() {
        return eventoIdEventoRevoca;
    }

    /**
     * Imposta il valore della proprietà eventoIdEventoRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventoIdEventoRevoca(String value) {
        this.eventoIdEventoRevoca = value;
    }

    /**
     * Gets the value of the elencoImpugnazioniOpposizioni property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoImpugnazioniOpposizioni property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoImpugnazioniOpposizioni().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IMPUGNAZIONETYPE }
     * 
     * 
     */
    public List<IMPUGNAZIONETYPE> getElencoImpugnazioniOpposizioni() {
        if (elencoImpugnazioniOpposizioni == null) {
            elencoImpugnazioniOpposizioni = new ArrayList<IMPUGNAZIONETYPE>();
        }
        return this.elencoImpugnazioniOpposizioni;
    }

    /**
     * Recupera il valore della proprietà flagDocumentoRegistrato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDocumentoRegistrato() {
        return flagDocumentoRegistrato;
    }

    /**
     * Imposta il valore della proprietà flagDocumentoRegistrato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDocumentoRegistrato(String value) {
        this.flagDocumentoRegistrato = value;
    }

    /**
     * Recupera il valore della proprietà flagDepositoValidato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagDepositoValidato() {
        return flagDepositoValidato;
    }

    /**
     * Imposta il valore della proprietà flagDepositoValidato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagDepositoValidato(String value) {
        this.flagDepositoValidato = value;
    }

}
