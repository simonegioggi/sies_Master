//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

import java.math.BigInteger;

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
 *         &lt;element name="annoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="oggettoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoFascicoloSIEP" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroFascicoloSIEP" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ufficioFascicoloSIEP" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataFascicoloSIEP" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="luogoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="nomeMagistratoRelatore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cognomeMagistratoRelatore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="etaPresuntaAnni" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="etaPresuntaMesi" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagRinviata" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiceStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "annoProcedimentoSIUS",
    "numeroProcedimentoSIUS",
    "oggettoProcedimentoSIUS",
    "annoFascicoloSIEP",
    "numeroFascicoloSIEP",
    "ufficioFascicoloSIEP",
    "dataFascicoloSIEP",
    "nomeSoggetto",
    "cognomeSoggetto",
    "dataNascita",
    "luogoNascita",
    "codiceProvincia",
    "dataUdienza",
    "nomeMagistratoRelatore",
    "cognomeMagistratoRelatore",
    "etaPresuntaAnni",
    "etaPresuntaMesi",
    "sesso",
    "flagRinviata",
    "descrStatoFascicolo",
    "codiceStatoFascicolo"
})
@XmlRootElement(name = "DATI_RIEPILOGO_PROCEDIMENTO")
public class DATIRIEPILOGOPROCEDIMENTO {

    @XmlElement(required = true)
    protected BigInteger annoProcedimentoSIUS;
    @XmlElement(required = true)
    protected BigInteger numeroProcedimentoSIUS;
    @XmlElement(required = true)
    protected String oggettoProcedimentoSIUS;
    @XmlElement(required = true)
    protected BigInteger annoFascicoloSIEP;
    @XmlElement(required = true)
    protected BigInteger numeroFascicoloSIEP;
    @XmlElement(required = true)
    protected String ufficioFascicoloSIEP;
    @XmlElement(required = true)
    protected DATATYPE dataFascicoloSIEP;
    @XmlElement(required = true)
    protected String nomeSoggetto;
    @XmlElement(required = true)
    protected String cognomeSoggetto;
    @XmlElement(required = true)
    protected DATATYPE dataNascita;
    @XmlElement(required = true)
    protected String luogoNascita;
    @XmlElement(required = true)
    protected String codiceProvincia;
    @XmlElement(required = true)
    protected DATATYPE dataUdienza;
    @XmlElement(required = true)
    protected String nomeMagistratoRelatore;
    @XmlElement(required = true)
    protected String cognomeMagistratoRelatore;
    @XmlElement(required = true, nillable = true)
    protected BigInteger etaPresuntaAnni;
    @XmlElement(required = true, nillable = true)
    protected BigInteger etaPresuntaMesi;
    @XmlElement(required = true)
    protected String sesso;
    @XmlElement(required = true)
    protected String flagRinviata;
    @XmlElement(required = true)
    protected String descrStatoFascicolo;
    @XmlElement(required = true)
    protected String codiceStatoFascicolo;

    /**
     * Recupera il valore della proprietà annoProcedimentoSIUS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoProcedimentoSIUS() {
        return annoProcedimentoSIUS;
    }

    /**
     * Imposta il valore della proprietà annoProcedimentoSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoProcedimentoSIUS(BigInteger value) {
        this.annoProcedimentoSIUS = value;
    }

    /**
     * Recupera il valore della proprietà numeroProcedimentoSIUS.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroProcedimentoSIUS() {
        return numeroProcedimentoSIUS;
    }

    /**
     * Imposta il valore della proprietà numeroProcedimentoSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroProcedimentoSIUS(BigInteger value) {
        this.numeroProcedimentoSIUS = value;
    }

    /**
     * Recupera il valore della proprietà oggettoProcedimentoSIUS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggettoProcedimentoSIUS() {
        return oggettoProcedimentoSIUS;
    }

    /**
     * Imposta il valore della proprietà oggettoProcedimentoSIUS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggettoProcedimentoSIUS(String value) {
        this.oggettoProcedimentoSIUS = value;
    }

    /**
     * Recupera il valore della proprietà annoFascicoloSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoFascicoloSIEP() {
        return annoFascicoloSIEP;
    }

    /**
     * Imposta il valore della proprietà annoFascicoloSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoFascicoloSIEP(BigInteger value) {
        this.annoFascicoloSIEP = value;
    }

    /**
     * Recupera il valore della proprietà numeroFascicoloSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroFascicoloSIEP() {
        return numeroFascicoloSIEP;
    }

    /**
     * Imposta il valore della proprietà numeroFascicoloSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroFascicoloSIEP(BigInteger value) {
        this.numeroFascicoloSIEP = value;
    }

    /**
     * Recupera il valore della proprietà ufficioFascicoloSIEP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUfficioFascicoloSIEP() {
        return ufficioFascicoloSIEP;
    }

    /**
     * Imposta il valore della proprietà ufficioFascicoloSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUfficioFascicoloSIEP(String value) {
        this.ufficioFascicoloSIEP = value;
    }

    /**
     * Recupera il valore della proprietà dataFascicoloSIEP.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataFascicoloSIEP() {
        return dataFascicoloSIEP;
    }

    /**
     * Imposta il valore della proprietà dataFascicoloSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataFascicoloSIEP(DATATYPE value) {
        this.dataFascicoloSIEP = value;
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
     * Recupera il valore della proprietà codiceProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceProvincia() {
        return codiceProvincia;
    }

    /**
     * Imposta il valore della proprietà codiceProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceProvincia(String value) {
        this.codiceProvincia = value;
    }

    /**
     * Recupera il valore della proprietà dataUdienza.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataUdienza() {
        return dataUdienza;
    }

    /**
     * Imposta il valore della proprietà dataUdienza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataUdienza(DATATYPE value) {
        this.dataUdienza = value;
    }

    /**
     * Recupera il valore della proprietà nomeMagistratoRelatore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeMagistratoRelatore() {
        return nomeMagistratoRelatore;
    }

    /**
     * Imposta il valore della proprietà nomeMagistratoRelatore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeMagistratoRelatore(String value) {
        this.nomeMagistratoRelatore = value;
    }

    /**
     * Recupera il valore della proprietà cognomeMagistratoRelatore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognomeMagistratoRelatore() {
        return cognomeMagistratoRelatore;
    }

    /**
     * Imposta il valore della proprietà cognomeMagistratoRelatore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognomeMagistratoRelatore(String value) {
        this.cognomeMagistratoRelatore = value;
    }

    /**
     * Recupera il valore della proprietà etaPresuntaAnni.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEtaPresuntaAnni() {
        return etaPresuntaAnni;
    }

    /**
     * Imposta il valore della proprietà etaPresuntaAnni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEtaPresuntaAnni(BigInteger value) {
        this.etaPresuntaAnni = value;
    }

    /**
     * Recupera il valore della proprietà etaPresuntaMesi.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEtaPresuntaMesi() {
        return etaPresuntaMesi;
    }

    /**
     * Imposta il valore della proprietà etaPresuntaMesi.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEtaPresuntaMesi(BigInteger value) {
        this.etaPresuntaMesi = value;
    }

    /**
     * Recupera il valore della proprietà sesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietà sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietà flagRinviata.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagRinviata() {
        return flagRinviata;
    }

    /**
     * Imposta il valore della proprietà flagRinviata.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagRinviata(String value) {
        this.flagRinviata = value;
    }

    /**
     * Recupera il valore della proprietà descrStatoFascicolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrStatoFascicolo() {
        return descrStatoFascicolo;
    }

    /**
     * Imposta il valore della proprietà descrStatoFascicolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrStatoFascicolo(String value) {
        this.descrStatoFascicolo = value;
    }

    /**
     * Recupera il valore della proprietà codiceStatoFascicolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceStatoFascicolo() {
        return codiceStatoFascicolo;
    }

    /**
     * Imposta il valore della proprietà codiceStatoFascicolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceStatoFascicolo(String value) {
        this.codiceStatoFascicolo = value;
    }

}
