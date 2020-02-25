//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DATI_DECRETO_REVOCATO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DATI_DECRETO_REVOCATO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrTipoDecreto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoDecreto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroDecreto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataDepositoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="statoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoPermessi" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}PERMESSO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="numeroGiorniRevocaLA" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="annoProcRevocato" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroProcRevocato" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="descrOggettoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATI_DECRETO_REVOCATO", propOrder = {
    "descrTipoDecreto",
    "annoDecreto",
    "numeroDecreto",
    "dataDepositoCancelleria",
    "statoProvvedimento",
    "elencoPermessi",
    "dataEmissione",
    "numeroGiorniRevocaLA",
    "annoProcRevocato",
    "numeroProcRevocato",
    "descrOggettoProcedimento",
    "descrTipoUfficio",
    "descrComuneUfficio"
})
public class DATIDECRETOREVOCATO {

    @XmlElement(required = true)
    protected String descrTipoDecreto;
    @XmlElement(required = true)
    protected BigInteger annoDecreto;
    @XmlElement(required = true)
    protected BigInteger numeroDecreto;
    @XmlElement(required = true)
    protected DATATYPE dataDepositoCancelleria;
    @XmlElement(required = true)
    protected String statoProvvedimento;
    @XmlElement(required = true)
    protected List<PERMESSOTYPE> elencoPermessi;
    @XmlElement(required = true)
    protected DATATYPE dataEmissione;
    @XmlElement(required = true)
    protected BigInteger numeroGiorniRevocaLA;
    @XmlElement(required = true, nillable = true)
    protected BigInteger annoProcRevocato;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroProcRevocato;
    @XmlElement(required = true, nillable = true)
    protected String descrOggettoProcedimento;
    @XmlElement(required = true, nillable = true)
    protected String descrTipoUfficio;
    @XmlElement(required = true, nillable = true)
    protected String descrComuneUfficio;

    /**
     * Recupera il valore della proprietà descrTipoDecreto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoDecreto() {
        return descrTipoDecreto;
    }

    /**
     * Imposta il valore della proprietà descrTipoDecreto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoDecreto(String value) {
        this.descrTipoDecreto = value;
    }

    /**
     * Recupera il valore della proprietà annoDecreto.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoDecreto() {
        return annoDecreto;
    }

    /**
     * Imposta il valore della proprietà annoDecreto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoDecreto(BigInteger value) {
        this.annoDecreto = value;
    }

    /**
     * Recupera il valore della proprietà numeroDecreto.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroDecreto() {
        return numeroDecreto;
    }

    /**
     * Imposta il valore della proprietà numeroDecreto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroDecreto(BigInteger value) {
        this.numeroDecreto = value;
    }

    /**
     * Recupera il valore della proprietà dataDepositoCancelleria.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataDepositoCancelleria() {
        return dataDepositoCancelleria;
    }

    /**
     * Imposta il valore della proprietà dataDepositoCancelleria.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataDepositoCancelleria(DATATYPE value) {
        this.dataDepositoCancelleria = value;
    }

    /**
     * Recupera il valore della proprietà statoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatoProvvedimento() {
        return statoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà statoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatoProvvedimento(String value) {
        this.statoProvvedimento = value;
    }

    /**
     * Gets the value of the elencoPermessi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoPermessi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoPermessi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PERMESSOTYPE }
     * 
     * 
     */
    public List<PERMESSOTYPE> getElencoPermessi() {
        if (elencoPermessi == null) {
            elencoPermessi = new ArrayList<PERMESSOTYPE>();
        }
        return this.elencoPermessi;
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
     * Recupera il valore della proprietà numeroGiorniRevocaLA.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroGiorniRevocaLA() {
        return numeroGiorniRevocaLA;
    }

    /**
     * Imposta il valore della proprietà numeroGiorniRevocaLA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroGiorniRevocaLA(BigInteger value) {
        this.numeroGiorniRevocaLA = value;
    }

    /**
     * Recupera il valore della proprietà annoProcRevocato.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoProcRevocato() {
        return annoProcRevocato;
    }

    /**
     * Imposta il valore della proprietà annoProcRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoProcRevocato(BigInteger value) {
        this.annoProcRevocato = value;
    }

    /**
     * Recupera il valore della proprietà numeroProcRevocato.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroProcRevocato() {
        return numeroProcRevocato;
    }

    /**
     * Imposta il valore della proprietà numeroProcRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroProcRevocato(BigInteger value) {
        this.numeroProcRevocato = value;
    }

    /**
     * Recupera il valore della proprietà descrOggettoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrOggettoProcedimento() {
        return descrOggettoProcedimento;
    }

    /**
     * Imposta il valore della proprietà descrOggettoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrOggettoProcedimento(String value) {
        this.descrOggettoProcedimento = value;
    }

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

}
