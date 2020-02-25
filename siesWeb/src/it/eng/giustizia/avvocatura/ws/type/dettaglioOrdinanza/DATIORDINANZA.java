//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element name="tipoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="annoOrdinanza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroOrdinanza" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataDepositoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="statoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="totaleGiorniLibertaAnticipata" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="totaleGiorniRiduzionePena" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="sommaRisarcimento" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ufficioSorveglianzaCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ufficioConcessioneRiduzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dispositivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ulterioreDescrDecisione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagNominaCommissarioActa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrCommissarioActa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataCessazioneMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="dataInizioRinvio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="dataFineRinvio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="durataSospensione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *         &lt;element name="dataSospensioneOrdinanza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="durataResiduaAltraMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *         &lt;element name="descrUfficioMagistratoCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="motivoRichiesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ulterioreDescrizioneOrdinanza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="motivazioni" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataDecorrenzaMisuraSicurezza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="durataMisuraSicurezza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *         &lt;element name="elencoEsiti" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}ESITI_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="totaleGiorniLibertaAnticipataSpeciale" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="totaleGiorniLibertaAnticipataIntegrazione" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="totaleGiorniLibertaAnticipataNormale" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="oggettoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "tipoOrdinanza",
    "dataEmissione",
    "annoOrdinanza",
    "numeroOrdinanza",
    "dataDepositoCancelleria",
    "statoProvvedimento",
    "descrDecisione",
    "totaleGiorniLibertaAnticipata",
    "totaleGiorniRiduzionePena",
    "sommaRisarcimento",
    "ufficioSorveglianzaCompetente",
    "ufficioConcessioneRiduzione",
    "dispositivo",
    "ulterioreDescrDecisione",
    "flagNominaCommissarioActa",
    "descrCommissarioActa",
    "dataCessazioneMisura",
    "dataInizioRinvio",
    "dataFineRinvio",
    "durataSospensione",
    "dataSospensioneOrdinanza",
    "durataResiduaAltraMisura",
    "descrUfficioMagistratoCompetente",
    "motivoRichiesta",
    "ulterioreDescrizioneOrdinanza",
    "motivazioni",
    "dataDecorrenzaMisuraSicurezza",
    "durataMisuraSicurezza",
    "elencoEsiti",
    "totaleGiorniLibertaAnticipataSpeciale",
    "totaleGiorniLibertaAnticipataIntegrazione",
    "totaleGiorniLibertaAnticipataNormale",
    "oggettoProcedimento",
    "codTipoRegistro"
})
@XmlRootElement(name = "DATI_ORDINANZA")
public class DATIORDINANZA {

    @XmlElement(required = true)
    protected String tipoOrdinanza;
    @XmlElement(required = true)
    protected DATATYPE dataEmissione;
    @XmlElement(required = true)
    protected BigInteger annoOrdinanza;
    @XmlElement(required = true)
    protected BigInteger numeroOrdinanza;
    @XmlElement(required = true)
    protected DATATYPE dataDepositoCancelleria;
    @XmlElement(required = true)
    protected String statoProvvedimento;
    @XmlElement(required = true)
    protected String descrDecisione;
    @XmlElement(required = true)
    protected BigInteger totaleGiorniLibertaAnticipata;
    @XmlElement(required = true)
    protected BigInteger totaleGiorniRiduzionePena;
    @XmlElement(required = true)
    protected BigDecimal sommaRisarcimento;
    @XmlElement(required = true)
    protected String ufficioSorveglianzaCompetente;
    @XmlElement(required = true)
    protected String ufficioConcessioneRiduzione;
    @XmlElement(required = true)
    protected String dispositivo;
    @XmlElement(required = true)
    protected String ulterioreDescrDecisione;
    @XmlElement(required = true)
    protected String flagNominaCommissarioActa;
    @XmlElement(required = true)
    protected String descrCommissarioActa;
    @XmlElement(required = true)
    protected DATATYPE dataCessazioneMisura;
    @XmlElement(required = true)
    protected DATATYPE dataInizioRinvio;
    @XmlElement(required = true)
    protected DATATYPE dataFineRinvio;
    @XmlElement(required = true)
    protected DURATATYPE durataSospensione;
    @XmlElement(required = true)
    protected DATATYPE dataSospensioneOrdinanza;
    @XmlElement(required = true)
    protected DURATATYPE durataResiduaAltraMisura;
    @XmlElement(required = true)
    protected String descrUfficioMagistratoCompetente;
    @XmlElement(required = true)
    protected String motivoRichiesta;
    @XmlElement(required = true)
    protected String ulterioreDescrizioneOrdinanza;
    @XmlElement(required = true)
    protected String motivazioni;
    @XmlElement(required = true)
    protected DATATYPE dataDecorrenzaMisuraSicurezza;
    @XmlElement(required = true)
    protected DURATATYPE durataMisuraSicurezza;
    @XmlElement(required = true)
    protected List<ESITITYPE> elencoEsiti;
    @XmlElement(required = true)
    protected BigInteger totaleGiorniLibertaAnticipataSpeciale;
    @XmlElement(required = true)
    protected BigInteger totaleGiorniLibertaAnticipataIntegrazione;
    @XmlElement(required = true)
    protected BigInteger totaleGiorniLibertaAnticipataNormale;
    @XmlElement(required = true)
    protected String oggettoProcedimento;
    @XmlElement(required = true, nillable = true)
    protected String codTipoRegistro;

    /**
     * Recupera il valore della proprietà tipoOrdinanza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOrdinanza() {
        return tipoOrdinanza;
    }

    /**
     * Imposta il valore della proprietà tipoOrdinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOrdinanza(String value) {
        this.tipoOrdinanza = value;
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
     * Recupera il valore della proprietà annoOrdinanza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoOrdinanza() {
        return annoOrdinanza;
    }

    /**
     * Imposta il valore della proprietà annoOrdinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoOrdinanza(BigInteger value) {
        this.annoOrdinanza = value;
    }

    /**
     * Recupera il valore della proprietà numeroOrdinanza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroOrdinanza() {
        return numeroOrdinanza;
    }

    /**
     * Imposta il valore della proprietà numeroOrdinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroOrdinanza(BigInteger value) {
        this.numeroOrdinanza = value;
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
     * Recupera il valore della proprietà totaleGiorniLibertaAnticipata.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotaleGiorniLibertaAnticipata() {
        return totaleGiorniLibertaAnticipata;
    }

    /**
     * Imposta il valore della proprietà totaleGiorniLibertaAnticipata.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotaleGiorniLibertaAnticipata(BigInteger value) {
        this.totaleGiorniLibertaAnticipata = value;
    }

    /**
     * Recupera il valore della proprietà totaleGiorniRiduzionePena.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotaleGiorniRiduzionePena() {
        return totaleGiorniRiduzionePena;
    }

    /**
     * Imposta il valore della proprietà totaleGiorniRiduzionePena.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotaleGiorniRiduzionePena(BigInteger value) {
        this.totaleGiorniRiduzionePena = value;
    }

    /**
     * Recupera il valore della proprietà sommaRisarcimento.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSommaRisarcimento() {
        return sommaRisarcimento;
    }

    /**
     * Imposta il valore della proprietà sommaRisarcimento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSommaRisarcimento(BigDecimal value) {
        this.sommaRisarcimento = value;
    }

    /**
     * Recupera il valore della proprietà ufficioSorveglianzaCompetente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUfficioSorveglianzaCompetente() {
        return ufficioSorveglianzaCompetente;
    }

    /**
     * Imposta il valore della proprietà ufficioSorveglianzaCompetente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUfficioSorveglianzaCompetente(String value) {
        this.ufficioSorveglianzaCompetente = value;
    }

    /**
     * Recupera il valore della proprietà ufficioConcessioneRiduzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUfficioConcessioneRiduzione() {
        return ufficioConcessioneRiduzione;
    }

    /**
     * Imposta il valore della proprietà ufficioConcessioneRiduzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUfficioConcessioneRiduzione(String value) {
        this.ufficioConcessioneRiduzione = value;
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
     * Recupera il valore della proprietà ulterioreDescrDecisione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUlterioreDescrDecisione() {
        return ulterioreDescrDecisione;
    }

    /**
     * Imposta il valore della proprietà ulterioreDescrDecisione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUlterioreDescrDecisione(String value) {
        this.ulterioreDescrDecisione = value;
    }

    /**
     * Recupera il valore della proprietà flagNominaCommissarioActa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagNominaCommissarioActa() {
        return flagNominaCommissarioActa;
    }

    /**
     * Imposta il valore della proprietà flagNominaCommissarioActa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagNominaCommissarioActa(String value) {
        this.flagNominaCommissarioActa = value;
    }

    /**
     * Recupera il valore della proprietà descrCommissarioActa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrCommissarioActa() {
        return descrCommissarioActa;
    }

    /**
     * Imposta il valore della proprietà descrCommissarioActa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrCommissarioActa(String value) {
        this.descrCommissarioActa = value;
    }

    /**
     * Recupera il valore della proprietà dataCessazioneMisura.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataCessazioneMisura() {
        return dataCessazioneMisura;
    }

    /**
     * Imposta il valore della proprietà dataCessazioneMisura.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataCessazioneMisura(DATATYPE value) {
        this.dataCessazioneMisura = value;
    }

    /**
     * Recupera il valore della proprietà dataInizioRinvio.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataInizioRinvio() {
        return dataInizioRinvio;
    }

    /**
     * Imposta il valore della proprietà dataInizioRinvio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataInizioRinvio(DATATYPE value) {
        this.dataInizioRinvio = value;
    }

    /**
     * Recupera il valore della proprietà dataFineRinvio.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataFineRinvio() {
        return dataFineRinvio;
    }

    /**
     * Imposta il valore della proprietà dataFineRinvio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataFineRinvio(DATATYPE value) {
        this.dataFineRinvio = value;
    }

    /**
     * Recupera il valore della proprietà durataSospensione.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataSospensione() {
        return durataSospensione;
    }

    /**
     * Imposta il valore della proprietà durataSospensione.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataSospensione(DURATATYPE value) {
        this.durataSospensione = value;
    }

    /**
     * Recupera il valore della proprietà dataSospensioneOrdinanza.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataSospensioneOrdinanza() {
        return dataSospensioneOrdinanza;
    }

    /**
     * Imposta il valore della proprietà dataSospensioneOrdinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataSospensioneOrdinanza(DATATYPE value) {
        this.dataSospensioneOrdinanza = value;
    }

    /**
     * Recupera il valore della proprietà durataResiduaAltraMisura.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataResiduaAltraMisura() {
        return durataResiduaAltraMisura;
    }

    /**
     * Imposta il valore della proprietà durataResiduaAltraMisura.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataResiduaAltraMisura(DURATATYPE value) {
        this.durataResiduaAltraMisura = value;
    }

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
     * Recupera il valore della proprietà motivoRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivoRichiesta() {
        return motivoRichiesta;
    }

    /**
     * Imposta il valore della proprietà motivoRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivoRichiesta(String value) {
        this.motivoRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà ulterioreDescrizioneOrdinanza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUlterioreDescrizioneOrdinanza() {
        return ulterioreDescrizioneOrdinanza;
    }

    /**
     * Imposta il valore della proprietà ulterioreDescrizioneOrdinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUlterioreDescrizioneOrdinanza(String value) {
        this.ulterioreDescrizioneOrdinanza = value;
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
     * Recupera il valore della proprietà dataDecorrenzaMisuraSicurezza.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataDecorrenzaMisuraSicurezza() {
        return dataDecorrenzaMisuraSicurezza;
    }

    /**
     * Imposta il valore della proprietà dataDecorrenzaMisuraSicurezza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataDecorrenzaMisuraSicurezza(DATATYPE value) {
        this.dataDecorrenzaMisuraSicurezza = value;
    }

    /**
     * Recupera il valore della proprietà durataMisuraSicurezza.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataMisuraSicurezza() {
        return durataMisuraSicurezza;
    }

    /**
     * Imposta il valore della proprietà durataMisuraSicurezza.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataMisuraSicurezza(DURATATYPE value) {
        this.durataMisuraSicurezza = value;
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
     * Recupera il valore della proprietà totaleGiorniLibertaAnticipataSpeciale.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotaleGiorniLibertaAnticipataSpeciale() {
        return totaleGiorniLibertaAnticipataSpeciale;
    }

    /**
     * Imposta il valore della proprietà totaleGiorniLibertaAnticipataSpeciale.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotaleGiorniLibertaAnticipataSpeciale(BigInteger value) {
        this.totaleGiorniLibertaAnticipataSpeciale = value;
    }

    /**
     * Recupera il valore della proprietà totaleGiorniLibertaAnticipataIntegrazione.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotaleGiorniLibertaAnticipataIntegrazione() {
        return totaleGiorniLibertaAnticipataIntegrazione;
    }

    /**
     * Imposta il valore della proprietà totaleGiorniLibertaAnticipataIntegrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotaleGiorniLibertaAnticipataIntegrazione(BigInteger value) {
        this.totaleGiorniLibertaAnticipataIntegrazione = value;
    }

    /**
     * Recupera il valore della proprietà totaleGiorniLibertaAnticipataNormale.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotaleGiorniLibertaAnticipataNormale() {
        return totaleGiorniLibertaAnticipataNormale;
    }

    /**
     * Imposta il valore della proprietà totaleGiorniLibertaAnticipataNormale.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotaleGiorniLibertaAnticipataNormale(BigInteger value) {
        this.totaleGiorniLibertaAnticipataNormale = value;
    }

    /**
     * Recupera il valore della proprietà oggettoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggettoProcedimento() {
        return oggettoProcedimento;
    }

    /**
     * Imposta il valore della proprietà oggettoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggettoProcedimento(String value) {
        this.oggettoProcedimento = value;
    }

    /**
     * Recupera il valore della proprietà codTipoRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoRegistro() {
        return codTipoRegistro;
    }

    /**
     * Imposta il valore della proprietà codTipoRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoRegistro(String value) {
        this.codTipoRegistro = value;
    }

}
