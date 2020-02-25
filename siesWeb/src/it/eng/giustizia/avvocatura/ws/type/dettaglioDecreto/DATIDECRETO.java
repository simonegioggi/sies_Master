//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

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
 *         &lt;element name="descrTipoDecreto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoDecreto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroDecreto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="dataDepositoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="statoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eventualeMotivazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tribSorvCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ufficioSorvCompetente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoControlloEsecuzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrCommActa" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sentenzaRiferimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="contenuto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_DECRETO_UNIFICANTE"/>
 *         &lt;element name="descrProcuraEsecuzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="statusPersona" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoProcRevocato" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroProcRevocato" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="questuraCompEsecuzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrProcuraRevocato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="totOreRaggiungimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoIstitutoDeten" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrComuneIstitutoDeten" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="licenza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}LICENZA_TYPE"/>
 *         &lt;element name="numeGiorniRevoca" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroOreRevoca" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="DECRETO_REVOCATO" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_DECRETO_REVOCATO"/>
 *         &lt;element name="elencoLicenzePeriodiLA" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}LICENZA_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="numeGiorniRiduzionePena" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sommaRisarcimentoDanni" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="dataSospensioneSanzSost" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="durataSospensioneSanzSost" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DURATA_TYPE"/>
 *         &lt;element name="dataScadenzaSospSanzSost" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="flagRecuperoSanzSost" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="giorniRecuperoSanzSost" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="durataSanzSostEspiata" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DURATA_TYPE"/>
 *         &lt;element name="durataSanzSostResidua" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DURATA_TYPE"/>
 *         &lt;element name="codTipoRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeGiorniRevocaLA" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="datiFascicoloOrigine">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="annoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="numeroProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="oggettoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *                   &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiceProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="codiceStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="dataCameraConsiglio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *                   &lt;element name="flagRinviata" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "descrTipoDecreto",
    "annoDecreto",
    "numeroDecreto",
    "dataEmissione",
    "dataDepositoCancelleria",
    "statoProvvedimento",
    "eventualeMotivazione",
    "tribSorvCompetente",
    "ufficioSorvCompetente",
    "descrTipoControlloEsecuzione",
    "descrCommActa",
    "sentenzaRiferimento",
    "luogoSvolgimentoProva",
    "dataUdienza",
    "contenuto",
    "note",
    "datidecretounificante",
    "descrProcuraEsecuzione",
    "statusPersona",
    "annoProcRevocato",
    "numeroProcRevocato",
    "questuraCompEsecuzione",
    "descrProcuraRevocato",
    "totOreRaggiungimento",
    "descrTipoIstitutoDeten",
    "descrComuneIstitutoDeten",
    "licenza",
    "numeGiorniRevoca",
    "numeroOreRevoca",
    "decretorevocato",
    "elencoLicenzePeriodiLA",
    "numeGiorniRiduzionePena",
    "sommaRisarcimentoDanni",
    "dataSospensioneSanzSost",
    "durataSospensioneSanzSost",
    "dataScadenzaSospSanzSost",
    "flagRecuperoSanzSost",
    "giorniRecuperoSanzSost",
    "durataSanzSostEspiata",
    "durataSanzSostResidua",
    "codTipoRegistro",
    "numeGiorniRevocaLA",
    "datiFascicoloOrigine"
})
@XmlRootElement(name = "DATI_DECRETO")
public class DATIDECRETO {

    @XmlElement(required = true)
    protected String descrTipoDecreto;
    @XmlElement(required = true)
    protected BigInteger annoDecreto;
    @XmlElement(required = true)
    protected BigInteger numeroDecreto;
    @XmlElement(required = true)
    protected DATATYPE dataEmissione;
    @XmlElement(required = true)
    protected DATATYPE dataDepositoCancelleria;
    @XmlElement(required = true)
    protected String statoProvvedimento;
    @XmlElement(required = true)
    protected String eventualeMotivazione;
    @XmlElement(required = true)
    protected String tribSorvCompetente;
    @XmlElement(required = true)
    protected String ufficioSorvCompetente;
    @XmlElement(required = true)
    protected String descrTipoControlloEsecuzione;
    @XmlElement(required = true)
    protected String descrCommActa;
    @XmlElement(required = true)
    protected String sentenzaRiferimento;
    @XmlElement(required = true)
    protected String luogoSvolgimentoProva;
    @XmlElement(required = true)
    protected DATATYPE dataUdienza;
    @XmlElement(required = true)
    protected String contenuto;
    @XmlElement(required = true)
    protected String note;
    @XmlElement(name = "DATI_DECRETO_UNIFICANTE", required = true)
    protected DATIDECRETOUNIFICANTE datidecretounificante;
    @XmlElement(required = true)
    protected String descrProcuraEsecuzione;
    @XmlElement(required = true)
    protected String statusPersona;
    @XmlElement(required = true)
    protected BigInteger annoProcRevocato;
    @XmlElement(required = true)
    protected BigInteger numeroProcRevocato;
    @XmlElement(required = true)
    protected String questuraCompEsecuzione;
    @XmlElement(required = true)
    protected String descrProcuraRevocato;
    @XmlElement(required = true)
    protected String totOreRaggiungimento;
    @XmlElement(required = true)
    protected String descrTipoIstitutoDeten;
    @XmlElement(required = true)
    protected String descrComuneIstitutoDeten;
    @XmlElement(required = true)
    protected LICENZATYPE licenza;
    @XmlElement(required = true)
    protected BigInteger numeGiorniRevoca;
    @XmlElement(required = true)
    protected BigInteger numeroOreRevoca;
    @XmlElement(name = "DECRETO_REVOCATO", required = true)
    protected DATIDECRETOREVOCATO decretorevocato;
    @XmlElement(required = true)
    protected List<LICENZATYPE> elencoLicenzePeriodiLA;
    @XmlElement(required = true)
    protected String numeGiorniRiduzionePena;
    @XmlElement(required = true)
    protected BigDecimal sommaRisarcimentoDanni;
    @XmlElement(required = true)
    protected DATATYPE dataSospensioneSanzSost;
    @XmlElement(required = true)
    protected DURATATYPE durataSospensioneSanzSost;
    @XmlElement(required = true)
    protected DATATYPE dataScadenzaSospSanzSost;
    @XmlElement(required = true)
    protected String flagRecuperoSanzSost;
    @XmlElement(required = true)
    protected BigInteger giorniRecuperoSanzSost;
    @XmlElement(required = true)
    protected DURATATYPE durataSanzSostEspiata;
    @XmlElement(required = true)
    protected DURATATYPE durataSanzSostResidua;
    @XmlElement(required = true, nillable = true)
    protected String codTipoRegistro;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeGiorniRevocaLA;
    @XmlElement(required = true)
    protected DATIDECRETO.DatiFascicoloOrigine datiFascicoloOrigine;

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
     * Recupera il valore della proprietà eventualeMotivazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventualeMotivazione() {
        return eventualeMotivazione;
    }

    /**
     * Imposta il valore della proprietà eventualeMotivazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventualeMotivazione(String value) {
        this.eventualeMotivazione = value;
    }

    /**
     * Recupera il valore della proprietà tribSorvCompetente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTribSorvCompetente() {
        return tribSorvCompetente;
    }

    /**
     * Imposta il valore della proprietà tribSorvCompetente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTribSorvCompetente(String value) {
        this.tribSorvCompetente = value;
    }

    /**
     * Recupera il valore della proprietà ufficioSorvCompetente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUfficioSorvCompetente() {
        return ufficioSorvCompetente;
    }

    /**
     * Imposta il valore della proprietà ufficioSorvCompetente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUfficioSorvCompetente(String value) {
        this.ufficioSorvCompetente = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoControlloEsecuzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoControlloEsecuzione() {
        return descrTipoControlloEsecuzione;
    }

    /**
     * Imposta il valore della proprietà descrTipoControlloEsecuzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoControlloEsecuzione(String value) {
        this.descrTipoControlloEsecuzione = value;
    }

    /**
     * Recupera il valore della proprietà descrCommActa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrCommActa() {
        return descrCommActa;
    }

    /**
     * Imposta il valore della proprietà descrCommActa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrCommActa(String value) {
        this.descrCommActa = value;
    }

    /**
     * Recupera il valore della proprietà sentenzaRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSentenzaRiferimento() {
        return sentenzaRiferimento;
    }

    /**
     * Imposta il valore della proprietà sentenzaRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSentenzaRiferimento(String value) {
        this.sentenzaRiferimento = value;
    }

    /**
     * Recupera il valore della proprietà luogoSvolgimentoProva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLuogoSvolgimentoProva() {
        return luogoSvolgimentoProva;
    }

    /**
     * Imposta il valore della proprietà luogoSvolgimentoProva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLuogoSvolgimentoProva(String value) {
        this.luogoSvolgimentoProva = value;
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
     * Recupera il valore della proprietà contenuto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContenuto() {
        return contenuto;
    }

    /**
     * Imposta il valore della proprietà contenuto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContenuto(String value) {
        this.contenuto = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Recupera il valore della proprietà datidecretounificante.
     * 
     * @return
     *     possible object is
     *     {@link DATIDECRETOUNIFICANTE }
     *     
     */
    public DATIDECRETOUNIFICANTE getDATIDECRETOUNIFICANTE() {
        return datidecretounificante;
    }

    /**
     * Imposta il valore della proprietà datidecretounificante.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIDECRETOUNIFICANTE }
     *     
     */
    public void setDATIDECRETOUNIFICANTE(DATIDECRETOUNIFICANTE value) {
        this.datidecretounificante = value;
    }

    /**
     * Recupera il valore della proprietà descrProcuraEsecuzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrProcuraEsecuzione() {
        return descrProcuraEsecuzione;
    }

    /**
     * Imposta il valore della proprietà descrProcuraEsecuzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrProcuraEsecuzione(String value) {
        this.descrProcuraEsecuzione = value;
    }

    /**
     * Recupera il valore della proprietà statusPersona.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusPersona() {
        return statusPersona;
    }

    /**
     * Imposta il valore della proprietà statusPersona.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusPersona(String value) {
        this.statusPersona = value;
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
     * Recupera il valore della proprietà questuraCompEsecuzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuesturaCompEsecuzione() {
        return questuraCompEsecuzione;
    }

    /**
     * Imposta il valore della proprietà questuraCompEsecuzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuesturaCompEsecuzione(String value) {
        this.questuraCompEsecuzione = value;
    }

    /**
     * Recupera il valore della proprietà descrProcuraRevocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrProcuraRevocato() {
        return descrProcuraRevocato;
    }

    /**
     * Imposta il valore della proprietà descrProcuraRevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrProcuraRevocato(String value) {
        this.descrProcuraRevocato = value;
    }

    /**
     * Recupera il valore della proprietà totOreRaggiungimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotOreRaggiungimento() {
        return totOreRaggiungimento;
    }

    /**
     * Imposta il valore della proprietà totOreRaggiungimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotOreRaggiungimento(String value) {
        this.totOreRaggiungimento = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoIstitutoDeten.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoIstitutoDeten() {
        return descrTipoIstitutoDeten;
    }

    /**
     * Imposta il valore della proprietà descrTipoIstitutoDeten.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoIstitutoDeten(String value) {
        this.descrTipoIstitutoDeten = value;
    }

    /**
     * Recupera il valore della proprietà descrComuneIstitutoDeten.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrComuneIstitutoDeten() {
        return descrComuneIstitutoDeten;
    }

    /**
     * Imposta il valore della proprietà descrComuneIstitutoDeten.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrComuneIstitutoDeten(String value) {
        this.descrComuneIstitutoDeten = value;
    }

    /**
     * Recupera il valore della proprietà licenza.
     * 
     * @return
     *     possible object is
     *     {@link LICENZATYPE }
     *     
     */
    public LICENZATYPE getLicenza() {
        return licenza;
    }

    /**
     * Imposta il valore della proprietà licenza.
     * 
     * @param value
     *     allowed object is
     *     {@link LICENZATYPE }
     *     
     */
    public void setLicenza(LICENZATYPE value) {
        this.licenza = value;
    }

    /**
     * Recupera il valore della proprietà numeGiorniRevoca.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeGiorniRevoca() {
        return numeGiorniRevoca;
    }

    /**
     * Imposta il valore della proprietà numeGiorniRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeGiorniRevoca(BigInteger value) {
        this.numeGiorniRevoca = value;
    }

    /**
     * Recupera il valore della proprietà numeroOreRevoca.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroOreRevoca() {
        return numeroOreRevoca;
    }

    /**
     * Imposta il valore della proprietà numeroOreRevoca.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroOreRevoca(BigInteger value) {
        this.numeroOreRevoca = value;
    }

    /**
     * Recupera il valore della proprietà decretorevocato.
     * 
     * @return
     *     possible object is
     *     {@link DATIDECRETOREVOCATO }
     *     
     */
    public DATIDECRETOREVOCATO getDECRETOREVOCATO() {
        return decretorevocato;
    }

    /**
     * Imposta il valore della proprietà decretorevocato.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIDECRETOREVOCATO }
     *     
     */
    public void setDECRETOREVOCATO(DATIDECRETOREVOCATO value) {
        this.decretorevocato = value;
    }

    /**
     * Gets the value of the elencoLicenzePeriodiLA property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoLicenzePeriodiLA property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoLicenzePeriodiLA().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LICENZATYPE }
     * 
     * 
     */
    public List<LICENZATYPE> getElencoLicenzePeriodiLA() {
        if (elencoLicenzePeriodiLA == null) {
            elencoLicenzePeriodiLA = new ArrayList<LICENZATYPE>();
        }
        return this.elencoLicenzePeriodiLA;
    }

    /**
     * Recupera il valore della proprietà numeGiorniRiduzionePena.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeGiorniRiduzionePena() {
        return numeGiorniRiduzionePena;
    }

    /**
     * Imposta il valore della proprietà numeGiorniRiduzionePena.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeGiorniRiduzionePena(String value) {
        this.numeGiorniRiduzionePena = value;
    }

    /**
     * Recupera il valore della proprietà sommaRisarcimentoDanni.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSommaRisarcimentoDanni() {
        return sommaRisarcimentoDanni;
    }

    /**
     * Imposta il valore della proprietà sommaRisarcimentoDanni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSommaRisarcimentoDanni(BigDecimal value) {
        this.sommaRisarcimentoDanni = value;
    }

    /**
     * Recupera il valore della proprietà dataSospensioneSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataSospensioneSanzSost() {
        return dataSospensioneSanzSost;
    }

    /**
     * Imposta il valore della proprietà dataSospensioneSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataSospensioneSanzSost(DATATYPE value) {
        this.dataSospensioneSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà durataSospensioneSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataSospensioneSanzSost() {
        return durataSospensioneSanzSost;
    }

    /**
     * Imposta il valore della proprietà durataSospensioneSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataSospensioneSanzSost(DURATATYPE value) {
        this.durataSospensioneSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà dataScadenzaSospSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataScadenzaSospSanzSost() {
        return dataScadenzaSospSanzSost;
    }

    /**
     * Imposta il valore della proprietà dataScadenzaSospSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataScadenzaSospSanzSost(DATATYPE value) {
        this.dataScadenzaSospSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà flagRecuperoSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagRecuperoSanzSost() {
        return flagRecuperoSanzSost;
    }

    /**
     * Imposta il valore della proprietà flagRecuperoSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagRecuperoSanzSost(String value) {
        this.flagRecuperoSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà giorniRecuperoSanzSost.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGiorniRecuperoSanzSost() {
        return giorniRecuperoSanzSost;
    }

    /**
     * Imposta il valore della proprietà giorniRecuperoSanzSost.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGiorniRecuperoSanzSost(BigInteger value) {
        this.giorniRecuperoSanzSost = value;
    }

    /**
     * Recupera il valore della proprietà durataSanzSostEspiata.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataSanzSostEspiata() {
        return durataSanzSostEspiata;
    }

    /**
     * Imposta il valore della proprietà durataSanzSostEspiata.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataSanzSostEspiata(DURATATYPE value) {
        this.durataSanzSostEspiata = value;
    }

    /**
     * Recupera il valore della proprietà durataSanzSostResidua.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataSanzSostResidua() {
        return durataSanzSostResidua;
    }

    /**
     * Imposta il valore della proprietà durataSanzSostResidua.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataSanzSostResidua(DURATATYPE value) {
        this.durataSanzSostResidua = value;
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

    /**
     * Recupera il valore della proprietà numeGiorniRevocaLA.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeGiorniRevocaLA() {
        return numeGiorniRevocaLA;
    }

    /**
     * Imposta il valore della proprietà numeGiorniRevocaLA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeGiorniRevocaLA(BigInteger value) {
        this.numeGiorniRevocaLA = value;
    }

    /**
     * Recupera il valore della proprietà datiFascicoloOrigine.
     * 
     * @return
     *     possible object is
     *     {@link DATIDECRETO.DatiFascicoloOrigine }
     *     
     */
    public DATIDECRETO.DatiFascicoloOrigine getDatiFascicoloOrigine() {
        return datiFascicoloOrigine;
    }

    /**
     * Imposta il valore della proprietà datiFascicoloOrigine.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIDECRETO.DatiFascicoloOrigine }
     *     
     */
    public void setDatiFascicoloOrigine(DATIDECRETO.DatiFascicoloOrigine value) {
        this.datiFascicoloOrigine = value;
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
     *         &lt;element name="annoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="numeroProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="descrTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrComuneUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="oggettoProcedimentoSIUS" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="nomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="cognomeSoggetto" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
     *         &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiceProvincia" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="codiceStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="dataCameraConsiglio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
     *         &lt;element name="flagRinviata" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "descrTipoUfficio",
        "descrComuneUfficio",
        "oggettoProcedimentoSIUS",
        "nomeSoggetto",
        "cognomeSoggetto",
        "sesso",
        "dataNascita",
        "descrComuneNascita",
        "codiceProvincia",
        "descrStatoNascita",
        "codiceStatoFascicolo",
        "descrStatoFascicolo",
        "dataCameraConsiglio",
        "flagRinviata"
    })
    public static class DatiFascicoloOrigine {

        @XmlElement(required = true)
        protected BigInteger annoProcedimentoSIUS;
        @XmlElement(required = true)
        protected BigInteger numeroProcedimentoSIUS;
        @XmlElement(required = true)
        protected String descrTipoUfficio;
        @XmlElement(required = true)
        protected String descrComuneUfficio;
        @XmlElement(required = true)
        protected String oggettoProcedimentoSIUS;
        @XmlElement(required = true)
        protected String nomeSoggetto;
        @XmlElement(required = true)
        protected String cognomeSoggetto;
        @XmlElement(required = true)
        protected String sesso;
        @XmlElement(required = true)
        protected DATATYPE dataNascita;
        @XmlElement(required = true)
        protected String descrComuneNascita;
        @XmlElement(required = true)
        protected String codiceProvincia;
        @XmlElement(required = true)
        protected String descrStatoNascita;
        @XmlElement(required = true)
        protected String codiceStatoFascicolo;
        @XmlElement(required = true)
        protected String descrStatoFascicolo;
        @XmlElement(required = true)
        protected DATATYPE dataCameraConsiglio;
        @XmlElement(required = true)
        protected String flagRinviata;

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
         * Recupera il valore della proprietà descrComuneNascita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrComuneNascita() {
            return descrComuneNascita;
        }

        /**
         * Imposta il valore della proprietà descrComuneNascita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrComuneNascita(String value) {
            this.descrComuneNascita = value;
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
         * Recupera il valore della proprietà descrStatoNascita.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescrStatoNascita() {
            return descrStatoNascita;
        }

        /**
         * Imposta il valore della proprietà descrStatoNascita.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescrStatoNascita(String value) {
            this.descrStatoNascita = value;
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
         * Recupera il valore della proprietà dataCameraConsiglio.
         * 
         * @return
         *     possible object is
         *     {@link DATATYPE }
         *     
         */
        public DATATYPE getDataCameraConsiglio() {
            return dataCameraConsiglio;
        }

        /**
         * Imposta il valore della proprietà dataCameraConsiglio.
         * 
         * @param value
         *     allowed object is
         *     {@link DATATYPE }
         *     
         */
        public void setDataCameraConsiglio(DATATYPE value) {
            this.dataCameraConsiglio = value;
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

    }

}
