//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigInteger;
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
 *         &lt;element name="chiaveProgrSIEP" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="chiaveAnnoSIEP" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="flagCumulante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataIscrizione" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}SENTENZA"/>
 *         &lt;element name="descrPosizioneGiuridica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PENA_RESIDUA" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}PENA_TYPE"/>
 *         &lt;element name="PENA_COMPLESSIVA" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}PENA_TYPE"/>
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
    "chiaveProgrSIEP",
    "chiaveAnnoSIEP",
    "flagCumulante",
    "descrTipoUfficio",
    "descrComuneUfficio",
    "dataIscrizione",
    "sentenza",
    "descrPosizioneGiuridica",
    "penaresidua",
    "penacomplessiva"
})
@XmlRootElement(name = "FASCICOLO_SIEP")
public class FASCICOLOSIEP {

    @XmlElement(required = true)
    protected BigInteger chiaveProgrSIEP;
    @XmlElement(required = true)
    protected BigInteger chiaveAnnoSIEP;
    @XmlElement(required = true)
    protected String flagCumulante;
    @XmlElement(required = true)
    protected Object descrTipoUfficio;
    @XmlElement(required = true)
    protected String descrComuneUfficio;
    @XmlElement(required = true)
    protected DATATYPE dataIscrizione;
    @XmlElement(name = "SENTENZA", required = true)
    protected SENTENZATYPE sentenza;
    @XmlElement(required = true)
    protected String descrPosizioneGiuridica;
    @XmlElement(name = "PENA_RESIDUA", required = true)
    protected PENATYPE penaresidua;
    @XmlElement(name = "PENA_COMPLESSIVA", required = true)
    protected PENATYPE penacomplessiva;

    /**
     * Recupera il valore della proprietà chiaveProgrSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChiaveProgrSIEP() {
        return chiaveProgrSIEP;
    }

    /**
     * Imposta il valore della proprietà chiaveProgrSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChiaveProgrSIEP(BigInteger value) {
        this.chiaveProgrSIEP = value;
    }

    /**
     * Recupera il valore della proprietà chiaveAnnoSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChiaveAnnoSIEP() {
        return chiaveAnnoSIEP;
    }

    /**
     * Imposta il valore della proprietà chiaveAnnoSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChiaveAnnoSIEP(BigInteger value) {
        this.chiaveAnnoSIEP = value;
    }

    /**
     * Recupera il valore della proprietà flagCumulante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagCumulante() {
        return flagCumulante;
    }

    /**
     * Imposta il valore della proprietà flagCumulante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagCumulante(String value) {
        this.flagCumulante = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoUfficio.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDescrTipoUfficio() {
        return descrTipoUfficio;
    }

    /**
     * Imposta il valore della proprietà descrTipoUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDescrTipoUfficio(Object value) {
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
     * Recupera il valore della proprietà dataIscrizione.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataIscrizione() {
        return dataIscrizione;
    }

    /**
     * Imposta il valore della proprietà dataIscrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataIscrizione(DATATYPE value) {
        this.dataIscrizione = value;
    }

    /**
     * dati della sentenza
     * 
     * @return
     *     possible object is
     *     {@link SENTENZATYPE }
     *     
     */
    public SENTENZATYPE getSENTENZA() {
        return sentenza;
    }

    /**
     * Imposta il valore della proprietà sentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link SENTENZATYPE }
     *     
     */
    public void setSENTENZA(SENTENZATYPE value) {
        this.sentenza = value;
    }

    /**
     * Recupera il valore della proprietà descrPosizioneGiuridica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrPosizioneGiuridica() {
        return descrPosizioneGiuridica;
    }

    /**
     * Imposta il valore della proprietà descrPosizioneGiuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrPosizioneGiuridica(String value) {
        this.descrPosizioneGiuridica = value;
    }

    /**
     * Recupera il valore della proprietà penaresidua.
     * 
     * @return
     *     possible object is
     *     {@link PENATYPE }
     *     
     */
    public PENATYPE getPENARESIDUA() {
        return penaresidua;
    }

    /**
     * Imposta il valore della proprietà penaresidua.
     * 
     * @param value
     *     allowed object is
     *     {@link PENATYPE }
     *     
     */
    public void setPENARESIDUA(PENATYPE value) {
        this.penaresidua = value;
    }

    /**
     * Recupera il valore della proprietà penacomplessiva.
     * 
     * @return
     *     possible object is
     *     {@link PENATYPE }
     *     
     */
    public PENATYPE getPENACOMPLESSIVA() {
        return penacomplessiva;
    }

    /**
     * Imposta il valore della proprietà penacomplessiva.
     * 
     * @param value
     *     allowed object is
     *     {@link PENATYPE }
     *     
     */
    public void setPENACOMPLESSIVA(PENATYPE value) {
        this.penacomplessiva = value;
    }

}
