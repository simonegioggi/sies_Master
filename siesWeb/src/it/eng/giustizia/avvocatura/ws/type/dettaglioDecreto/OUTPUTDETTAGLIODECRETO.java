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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_RIEPILOGO_PROCEDIMENTO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_DECRETO"/>
 *         &lt;element name="elencoEsiti" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}TENORE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="listaAvvocati" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}AVVOCATO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoPrescrizioni" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}PRESCRIZIONE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoMotivazioniDecreto" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}MOTIVAZIONI_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoDestinatari" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DESTINATARIO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoPermessi" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}PERMESSO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="datiRevocaLibertaAnticipata">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}ERRORE"/>
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
    "datiriepilogoprocedimento",
    "datidecreto",
    "elencoEsiti",
    "listaAvvocati",
    "elencoPrescrizioni",
    "elencoMotivazioniDecreto",
    "elencoDestinatari",
    "elencoPermessi",
    "datiRevocaLibertaAnticipata",
    "errore"
})
@XmlRootElement(name = "OUTPUT_DETTAGLIO_DECRETO")
public class OUTPUTDETTAGLIODECRETO {

    @XmlElement(name = "DATI_RIEPILOGO_PROCEDIMENTO", required = true)
    protected DATIRIEPILOGOPROCEDIMENTO datiriepilogoprocedimento;
    @XmlElement(name = "DATI_DECRETO", required = true)
    protected DATIDECRETO datidecreto;
    @XmlElement(required = true)
    protected List<TENORETYPE> elencoEsiti;
    @XmlElement(required = true)
    protected List<AVVOCATOTYPE> listaAvvocati;
    @XmlElement(required = true)
    protected List<PRESCRIZIONETYPE> elencoPrescrizioni;
    @XmlElement(required = true)
    protected List<MOTIVAZIONITYPE> elencoMotivazioniDecreto;
    @XmlElement(required = true)
    protected List<DESTINATARIOTYPE> elencoDestinatari;
    @XmlElement(required = true)
    protected List<PERMESSOTYPE> elencoPermessi;
    @XmlElement(required = true)
    protected OUTPUTDETTAGLIODECRETO.DatiRevocaLibertaAnticipata datiRevocaLibertaAnticipata;
    @XmlElement(name = "ERRORE", required = true)
    protected ERRORE errore;

    /**
     * Recupera il valore della proprietà datiriepilogoprocedimento.
     * 
     * @return
     *     possible object is
     *     {@link DATIRIEPILOGOPROCEDIMENTO }
     *     
     */
    public DATIRIEPILOGOPROCEDIMENTO getDATIRIEPILOGOPROCEDIMENTO() {
        return datiriepilogoprocedimento;
    }

    /**
     * Imposta il valore della proprietà datiriepilogoprocedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIRIEPILOGOPROCEDIMENTO }
     *     
     */
    public void setDATIRIEPILOGOPROCEDIMENTO(DATIRIEPILOGOPROCEDIMENTO value) {
        this.datiriepilogoprocedimento = value;
    }

    /**
     * Recupera il valore della proprietà datidecreto.
     * 
     * @return
     *     possible object is
     *     {@link DATIDECRETO }
     *     
     */
    public DATIDECRETO getDATIDECRETO() {
        return datidecreto;
    }

    /**
     * Imposta il valore della proprietà datidecreto.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIDECRETO }
     *     
     */
    public void setDATIDECRETO(DATIDECRETO value) {
        this.datidecreto = value;
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
     * {@link TENORETYPE }
     * 
     * 
     */
    public List<TENORETYPE> getElencoEsiti() {
        if (elencoEsiti == null) {
            elencoEsiti = new ArrayList<TENORETYPE>();
        }
        return this.elencoEsiti;
    }

    /**
     * Gets the value of the listaAvvocati property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaAvvocati property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaAvvocati().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AVVOCATOTYPE }
     * 
     * 
     */
    public List<AVVOCATOTYPE> getListaAvvocati() {
        if (listaAvvocati == null) {
            listaAvvocati = new ArrayList<AVVOCATOTYPE>();
        }
        return this.listaAvvocati;
    }

    /**
     * Gets the value of the elencoPrescrizioni property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoPrescrizioni property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoPrescrizioni().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRESCRIZIONETYPE }
     * 
     * 
     */
    public List<PRESCRIZIONETYPE> getElencoPrescrizioni() {
        if (elencoPrescrizioni == null) {
            elencoPrescrizioni = new ArrayList<PRESCRIZIONETYPE>();
        }
        return this.elencoPrescrizioni;
    }

    /**
     * Gets the value of the elencoMotivazioniDecreto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoMotivazioniDecreto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoMotivazioniDecreto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MOTIVAZIONITYPE }
     * 
     * 
     */
    public List<MOTIVAZIONITYPE> getElencoMotivazioniDecreto() {
        if (elencoMotivazioniDecreto == null) {
            elencoMotivazioniDecreto = new ArrayList<MOTIVAZIONITYPE>();
        }
        return this.elencoMotivazioniDecreto;
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
     * {@link DESTINATARIOTYPE }
     * 
     * 
     */
    public List<DESTINATARIOTYPE> getElencoDestinatari() {
        if (elencoDestinatari == null) {
            elencoDestinatari = new ArrayList<DESTINATARIOTYPE>();
        }
        return this.elencoDestinatari;
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
     * Recupera il valore della proprietà datiRevocaLibertaAnticipata.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIODECRETO.DatiRevocaLibertaAnticipata }
     *     
     */
    public OUTPUTDETTAGLIODECRETO.DatiRevocaLibertaAnticipata getDatiRevocaLibertaAnticipata() {
        return datiRevocaLibertaAnticipata;
    }

    /**
     * Imposta il valore della proprietà datiRevocaLibertaAnticipata.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIODECRETO.DatiRevocaLibertaAnticipata }
     *     
     */
    public void setDatiRevocaLibertaAnticipata(OUTPUTDETTAGLIODECRETO.DatiRevocaLibertaAnticipata value) {
        this.datiRevocaLibertaAnticipata = value;
    }

    /**
     * Recupera il valore della proprietà errore.
     * 
     * @return
     *     possible object is
     *     {@link ERRORE }
     *     
     */
    public ERRORE getERRORE() {
        return errore;
    }

    /**
     * Imposta il valore della proprietà errore.
     * 
     * @param value
     *     allowed object is
     *     {@link ERRORE }
     *     
     */
    public void setERRORE(ERRORE value) {
        this.errore = value;
    }


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
     *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="licenzaPeriodiLibertaAnticipata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}PERIODI_LIBERTA_ANTICIPATA_TYPE" maxOccurs="unbounded"/>
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
        "descrUfficioMagistratoCompetente",
        "descrDecisione",
        "licenzaPeriodiLibertaAnticipata"
    })
    public static class DatiRevocaLibertaAnticipata {

        @XmlElement(required = true)
        protected String descrUfficioMagistratoCompetente;
        @XmlElement(required = true)
        protected String descrDecisione;
        @XmlElement(required = true)
        protected List<PERIODILIBERTAANTICIPATATYPE> licenzaPeriodiLibertaAnticipata;

        /**
         * Recupera il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrUfficioMagistratoCompetente() {
            return descrUfficioMagistratoCompetente;
        }

        /**
         * Imposta il valore della proprietà descrUfficioMagistratoCompetente.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrUfficioMagistratoCompetente(String value) {
            this.descrUfficioMagistratoCompetente = value;
        }

        /**
         * Recupera il valore della proprietà descrDecisione.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrDecisione() {
            return descrDecisione;
        }

        /**
         * Imposta il valore della proprietà descrDecisione.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrDecisione(String value) {
            this.descrDecisione = value;
        }

        /**
         * Gets the value of the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the licenzaPeriodiLibertaAnticipata property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLicenzaPeriodiLibertaAnticipata().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PERIODILIBERTAANTICIPATATYPE }
         * 
         * 
         */
        public List<PERIODILIBERTAANTICIPATATYPE> getLicenzaPeriodiLibertaAnticipata() {
            if (licenzaPeriodiLibertaAnticipata == null) {
                licenzaPeriodiLibertaAnticipata = new ArrayList<PERIODILIBERTAANTICIPATATYPE>();
            }
            return this.licenzaPeriodiLibertaAnticipata;
        }

    }

}
