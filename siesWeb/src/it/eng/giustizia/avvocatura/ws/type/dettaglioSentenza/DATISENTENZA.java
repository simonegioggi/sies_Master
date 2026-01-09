//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 10:47:00 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza;

import java.math.BigInteger;
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
 *         &lt;element name="tipoSentenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataEmissioneSentenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}DATA_TYPE"/>
 *         &lt;element name="annoSentenza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroSentenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataDepositoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}DATA_TYPE"/>
 *         &lt;element name="statoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrizioneDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dispositivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="motivazioni" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoEsiti" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}ESITI_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoDestinatari" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}DESTINATARI_TYPE" maxOccurs="unbounded"/>
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
    "tipoSentenza",
    "dataEmissioneSentenza",
    "annoSentenza",
    "numeroSentenza",
    "dataDepositoCancelleria",
    "statoProvvedimento",
    "descrizioneDecisione",
    "dispositivo",
    "motivazioni",
    "elencoEsiti",
    "elencoDestinatari"
})
@XmlRootElement(name = "DATI_SENTENZA")
public class DATISENTENZA {

    @XmlElement(required = true)
    protected String tipoSentenza;
    @XmlElement(required = true)
    protected DATATYPE dataEmissioneSentenza;
    @XmlElement(required = true)
    protected BigInteger annoSentenza;
    @XmlElement(required = true)
    protected String numeroSentenza;
    @XmlElement(required = true)
    protected DATATYPE dataDepositoCancelleria;
    @XmlElement(required = true)
    protected String statoProvvedimento;
    @XmlElement(required = true)
    protected String descrizioneDecisione;
    @XmlElement(required = true)
    protected String dispositivo;
    @XmlElement(required = true)
    protected String motivazioni;
    @XmlElement(required = true)
    protected List<ESITITYPE> elencoEsiti;
    @XmlElement(required = true)
    protected List<DESTINATARITYPE> elencoDestinatari;

    /**
     * Recupera il valore della proprietà tipoSentenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoSentenza() {
        return tipoSentenza;
    }

    /**
     * Imposta il valore della proprietà tipoSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoSentenza(String value) {
        this.tipoSentenza = value;
    }

    /**
     * Recupera il valore della proprietà dataEmissioneSentenza.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataEmissioneSentenza() {
        return dataEmissioneSentenza;
    }

    /**
     * Imposta il valore della proprietà dataEmissioneSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataEmissioneSentenza(DATATYPE value) {
        this.dataEmissioneSentenza = value;
    }

    /**
     * Recupera il valore della proprietà annoSentenza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoSentenza() {
        return annoSentenza;
    }

    /**
     * Imposta il valore della proprietà annoSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoSentenza(BigInteger value) {
        this.annoSentenza = value;
    }

    /**
     * Recupera il valore della proprietà numeroSentenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSentenza() {
        return numeroSentenza;
    }

    /**
     * Imposta il valore della proprietà numeroSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSentenza(String value) {
        this.numeroSentenza = value;
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
     * Recupera il valore della proprietà descrizioneDecisione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneDecisione() {
        return descrizioneDecisione;
    }

    /**
     * Imposta il valore della proprietà descrizioneDecisione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneDecisione(String value) {
        this.descrizioneDecisione = value;
    }

    /**
     * Recupera il valore della proprietà dispositivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispositivo() {
        return dispositivo;
    }

    /**
     * Imposta il valore della proprietà dispositivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispositivo(String value) {
        this.dispositivo = value;
    }

    /**
     * Recupera il valore della proprietà motivazioni.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivazioni() {
        return motivazioni;
    }

    /**
     * Imposta il valore della proprietà motivazioni.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivazioni(String value) {
        this.motivazioni = value;
    }

    /**
     * Gets the value of the elencoEsiti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoEsiti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoEsiti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ESITITYPE }
     * 
     * 
     */
    public List<ESITITYPE> getElencoEsiti() {
        if (elencoEsiti == null) {
            elencoEsiti = new ArrayList<ESITITYPE>();
        }
        return this.elencoEsiti;
    }

    /**
     * Gets the value of the elencoDestinatari property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoDestinatari property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoDestinatari().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DESTINATARITYPE }
     * 
     * 
     */
    public List<DESTINATARITYPE> getElencoDestinatari() {
        if (elencoDestinatari == null) {
            elencoDestinatari = new ArrayList<DESTINATARITYPE>();
        }
        return this.elencoDestinatari;
    }

}
