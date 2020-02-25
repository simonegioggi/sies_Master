//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.27 alle 02:09:58 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}FASCICOLO_SIUS"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}SOGGETTO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}ATTO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}FASCICOLO_SIEP"/>
 *         &lt;element name="oggetto" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}TENORE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="oggettiStralciati" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}TENORE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoRiferimentiFascicoliSIEP" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}RIFERIMENTO_FASC_SIEP_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="listaAvvocati" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}AVVOCATO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoMovimentiUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}MOVIMENTI_UDIENZA_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoProvvedimenti" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}EVENTO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoAtti" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}EVENTO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="elencoRichiesteIstruttorie" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}NOTIFICA_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="ALLEGATO_RTF" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}ERRORE"/>
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
    "fascicolosius",
    "soggetto",
    "atto",
    "fascicolosiep",
    "oggetto",
    "oggettiStralciati",
    "elencoRiferimentiFascicoliSIEP",
    "listaAvvocati",
    "elencoMovimentiUdienza",
    "elencoProvvedimenti",
    "elencoAtti",
    "elencoRichiesteIstruttorie",
    "allegatortf",
    "errore"
})
@XmlRootElement(name = "DATI_PROCEDIMENTO_OUTPUT")
public class DATIPROCEDIMENTOOUTPUT {

    @XmlElement(name = "FASCICOLO_SIUS", required = true)
    protected FASCICOLOSIUS fascicolosius;
    @XmlElement(name = "SOGGETTO", required = true)
    protected SOGGETTOTYPE soggetto;
    @XmlElement(name = "ATTO", required = true)
    protected ATTO atto;
    @XmlElement(name = "FASCICOLO_SIEP", required = true)
    protected FASCICOLOSIEP fascicolosiep;
    @XmlElement(required = true)
    protected List<TENORETYPE> oggetto;
    @XmlElement(required = true)
    protected List<TENORETYPE> oggettiStralciati;
    @XmlElement(required = true)
    protected List<RIFERIMENTOFASCSIEPTYPE> elencoRiferimentiFascicoliSIEP;
    @XmlElement(required = true)
    protected List<AVVOCATOTYPE> listaAvvocati;
    @XmlElement(required = true)
    protected List<MOVIMENTIUDIENZATYPE> elencoMovimentiUdienza;
    @XmlElement(required = true)
    protected List<EVENTOTYPE> elencoProvvedimenti;
    @XmlElement(required = true)
    protected List<EVENTOTYPE> elencoAtti;
    @XmlElement(required = true)
    protected List<NOTIFICATYPE> elencoRichiesteIstruttorie;
    @XmlElement(name = "ALLEGATO_RTF", required = true, nillable = true)
    protected byte[] allegatortf;
    @XmlElement(name = "ERRORE", required = true)
    protected ERRORE errore;

    /**
     * Recupera il valore della proprietà fascicolosius.
     * 
     * @return
     *     possible object is
     *     {@link FASCICOLOSIUS }
     *     
     */
    public FASCICOLOSIUS getFASCICOLOSIUS() {
        return fascicolosius;
    }

    /**
     * Imposta il valore della proprietà fascicolosius.
     * 
     * @param value
     *     allowed object is
     *     {@link FASCICOLOSIUS }
     *     
     */
    public void setFASCICOLOSIUS(FASCICOLOSIUS value) {
        this.fascicolosius = value;
    }

    /**
     * Recupera il valore della proprietà soggetto.
     * 
     * @return
     *     possible object is
     *     {@link SOGGETTOTYPE }
     *     
     */
    public SOGGETTOTYPE getSOGGETTO() {
        return soggetto;
    }

    /**
     * Imposta il valore della proprietà soggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link SOGGETTOTYPE }
     *     
     */
    public void setSOGGETTO(SOGGETTOTYPE value) {
        this.soggetto = value;
    }

    /**
     * Recupera il valore della proprietà atto.
     * 
     * @return
     *     possible object is
     *     {@link ATTO }
     *     
     */
    public ATTO getATTO() {
        return atto;
    }

    /**
     * Imposta il valore della proprietà atto.
     * 
     * @param value
     *     allowed object is
     *     {@link ATTO }
     *     
     */
    public void setATTO(ATTO value) {
        this.atto = value;
    }

    /**
     * Recupera il valore della proprietà fascicolosiep.
     * 
     * @return
     *     possible object is
     *     {@link FASCICOLOSIEP }
     *     
     */
    public FASCICOLOSIEP getFASCICOLOSIEP() {
        return fascicolosiep;
    }

    /**
     * Imposta il valore della proprietà fascicolosiep.
     * 
     * @param value
     *     allowed object is
     *     {@link FASCICOLOSIEP }
     *     
     */
    public void setFASCICOLOSIEP(FASCICOLOSIEP value) {
        this.fascicolosiep = value;
    }

    /**
     * Gets the value of the oggetto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oggetto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOggetto().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TENORETYPE }
     * 
     * 
     */
    public List<TENORETYPE> getOggetto() {
        if (oggetto == null) {
            oggetto = new ArrayList<TENORETYPE>();
        }
        return this.oggetto;
    }

    /**
     * Gets the value of the oggettiStralciati property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the oggettiStralciati property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOggettiStralciati().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TENORETYPE }
     * 
     * 
     */
    public List<TENORETYPE> getOggettiStralciati() {
        if (oggettiStralciati == null) {
            oggettiStralciati = new ArrayList<TENORETYPE>();
        }
        return this.oggettiStralciati;
    }

    /**
     * Gets the value of the elencoRiferimentiFascicoliSIEP property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoRiferimentiFascicoliSIEP property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoRiferimentiFascicoliSIEP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RIFERIMENTOFASCSIEPTYPE }
     * 
     * 
     */
    public List<RIFERIMENTOFASCSIEPTYPE> getElencoRiferimentiFascicoliSIEP() {
        if (elencoRiferimentiFascicoliSIEP == null) {
            elencoRiferimentiFascicoliSIEP = new ArrayList<RIFERIMENTOFASCSIEPTYPE>();
        }
        return this.elencoRiferimentiFascicoliSIEP;
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
     * Gets the value of the elencoMovimentiUdienza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoMovimentiUdienza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoMovimentiUdienza().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MOVIMENTIUDIENZATYPE }
     * 
     * 
     */
    public List<MOVIMENTIUDIENZATYPE> getElencoMovimentiUdienza() {
        if (elencoMovimentiUdienza == null) {
            elencoMovimentiUdienza = new ArrayList<MOVIMENTIUDIENZATYPE>();
        }
        return this.elencoMovimentiUdienza;
    }

    /**
     * Gets the value of the elencoProvvedimenti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoProvvedimenti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoProvvedimenti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EVENTOTYPE }
     * 
     * 
     */
    public List<EVENTOTYPE> getElencoProvvedimenti() {
        if (elencoProvvedimenti == null) {
            elencoProvvedimenti = new ArrayList<EVENTOTYPE>();
        }
        return this.elencoProvvedimenti;
    }

    /**
     * Gets the value of the elencoAtti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoAtti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoAtti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EVENTOTYPE }
     * 
     * 
     */
    public List<EVENTOTYPE> getElencoAtti() {
        if (elencoAtti == null) {
            elencoAtti = new ArrayList<EVENTOTYPE>();
        }
        return this.elencoAtti;
    }

    /**
     * Gets the value of the elencoRichiesteIstruttorie property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoRichiesteIstruttorie property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoRichiesteIstruttorie().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NOTIFICATYPE }
     * 
     * 
     */
    public List<NOTIFICATYPE> getElencoRichiesteIstruttorie() {
        if (elencoRichiesteIstruttorie == null) {
            elencoRichiesteIstruttorie = new ArrayList<NOTIFICATYPE>();
        }
        return this.elencoRichiesteIstruttorie;
    }

    /**
     * Recupera il valore della proprietà allegatortf.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getALLEGATORTF() {
        return allegatortf;
    }

    /**
     * Imposta il valore della proprietà allegatortf.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setALLEGATORTF(byte[] value) {
        this.allegatortf = value;
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

}
