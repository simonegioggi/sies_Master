//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}chiaveSies"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}chiaveNsc"/>
 *         &lt;element name="dataProvvedimento" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}annoOrdinanza"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}numeroOrdinanza"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}codiceSedePM"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}annoSIEP"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}numeroSIEP"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}annoSentenza"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}numeroSentenza"/>
 *         &lt;element name="codiceAutorita">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sedeAutoritaPrinDist">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sedeAutoritaPrinc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceUnivocoProvvedimento">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tipoProvvedimento">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="importoAmmenda">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="importoMulta">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}arresto"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}reclusione"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ergastolo"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}isolamentoDiurno"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}dataFinePena"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}dataFinePenaDal"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}dataFinePenaAl"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}MisuraSicurezza" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}PenaAccessoria" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}SanzioniSostitutive"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}LiberazioneAnticipataConcessaDetrarreCumulo"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}PenaConversionePenaPecuniaria"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}SanzioniGiudicePace"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}RichiesteGEAnticipazioneEffetti" maxOccurs="unbounded" minOccurs="0"/>
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
    "chiaveSies",
    "chiaveNsc",
    "dataProvvedimento",
    "annoOrdinanza",
    "numeroOrdinanza",
    "codiceSedePM",
    "annoSIEP",
    "numeroSIEP",
    "annoSentenza",
    "numeroSentenza",
    "codiceAutorita",
    "sedeAutoritaPrinDist",
    "sedeAutoritaPrinc",
    "codiceUnivocoProvvedimento",
    "tipoProvvedimento",
    "importoAmmenda",
    "importoMulta",
    "arresto",
    "reclusione",
    "ergastolo",
    "isolamentoDiurno",
    "dataFinePena",
    "dataFinePenaDal",
    "dataFinePenaAl",
    "misuraSicurezza",
    "penaAccessoria",
    "sanzioniSostitutive",
    "liberazioneAnticipataConcessaDetrarreCumulo",
    "penaConversionePenaPecuniaria",
    "sanzioniGiudicePace",
    "richiesteGEAnticipazioneEffetti"
})
@XmlRootElement(name = "DatiPubblicoMinistero")
public class DatiPubblicoMinistero {

    protected long chiaveSies;
    protected long chiaveNsc;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataProvvedimento;
    @XmlElement(required = true, nillable = true)
    protected BigInteger annoOrdinanza;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroOrdinanza;
    @XmlElement(required = true, nillable = true)
    protected String codiceSedePM;
    @XmlElement(required = true, nillable = true)
    protected BigInteger annoSIEP;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroSIEP;
    @XmlElement(required = true, nillable = true)
    protected BigInteger annoSentenza;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroSentenza;
    @XmlElement(required = true)
    protected String codiceAutorita;
    @XmlElement(required = true, nillable = true)
    protected String sedeAutoritaPrinDist;
    @XmlElement(required = true, nillable = true)
    protected String sedeAutoritaPrinc;
    @XmlElement(required = true)
    protected String codiceUnivocoProvvedimento;
    @XmlElement(required = true)
    protected String tipoProvvedimento;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal importoAmmenda;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal importoMulta;
    @XmlElement(required = true, nillable = true)
    protected Durata arresto;
    @XmlElement(required = true, nillable = true)
    protected Durata reclusione;
    @XmlElement(required = true, nillable = true)
    protected String ergastolo;
    @XmlElement(required = true, nillable = true)
    protected Durata isolamentoDiurno;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFinePena;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFinePenaDal;
    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFinePenaAl;
    @XmlElement(name = "MisuraSicurezza", nillable = true)
    protected List<MisuraSicurezza> misuraSicurezza;
    @XmlElement(name = "PenaAccessoria", nillable = true)
    protected List<PenaAccessoria> penaAccessoria;
    @XmlElement(name = "SanzioniSostitutive", required = true, nillable = true)
    protected SanzioniSostitutive sanzioniSostitutive;
    @XmlElement(name = "LiberazioneAnticipataConcessaDetrarreCumulo", required = true, nillable = true)
    protected LiberazioneAnticipataConcessaDetrarreCumulo liberazioneAnticipataConcessaDetrarreCumulo;
    @XmlElement(name = "PenaConversionePenaPecuniaria", required = true, nillable = true)
    protected PenaConversionePenaPecuniaria penaConversionePenaPecuniaria;
    @XmlElement(name = "SanzioniGiudicePace", required = true, nillable = true)
    protected SanzioniGiudicePace sanzioniGiudicePace;
    @XmlElement(name = "RichiesteGEAnticipazioneEffetti", nillable = true)
    protected List<RichiesteGEAnticipazioneEffetti> richiesteGEAnticipazioneEffetti;

    /**
     * Recupera il valore della proprietï¿½ chiaveSies.
     * 
     */
    public long getChiaveSies() {
        return chiaveSies;
    }

    /**
     * Imposta il valore della proprietï¿½ chiaveSies.
     * 
     */
    public void setChiaveSies(long value) {
        this.chiaveSies = value;
    }

    /**
     * Recupera il valore della proprietï¿½ chiaveNsc.
     * 
     */
    public long getChiaveNsc() {
        return chiaveNsc;
    }

    /**
     * Imposta il valore della proprietï¿½ chiaveNsc.
     * 
     */
    public void setChiaveNsc(long value) {
        this.chiaveNsc = value;
    }

    /**
     * Recupera il valore della proprietï¿½ dataProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataProvvedimento() {
        return dataProvvedimento;
    }

    /**
     * Imposta il valore della proprietï¿½ dataProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataProvvedimento(XMLGregorianCalendar value) {
        this.dataProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietï¿½ annoOrdinanza.
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
     * Imposta il valore della proprietï¿½ annoOrdinanza.
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
     * Recupera il valore della proprietï¿½ numeroOrdinanza.
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
     * Imposta il valore della proprietï¿½ numeroOrdinanza.
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
     * Recupera il valore della proprietï¿½ codiceSedePM.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceSedePM() {
        return codiceSedePM;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceSedePM.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceSedePM(String value) {
        this.codiceSedePM = value;
    }

    /**
     * Recupera il valore della proprietï¿½ annoSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoSIEP() {
        return annoSIEP;
    }

    /**
     * Imposta il valore della proprietï¿½ annoSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoSIEP(BigInteger value) {
        this.annoSIEP = value;
    }

    /**
     * Recupera il valore della proprietï¿½ numeroSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroSIEP() {
        return numeroSIEP;
    }

    /**
     * Imposta il valore della proprietï¿½ numeroSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroSIEP(BigInteger value) {
        this.numeroSIEP = value;
    }

    /**
     * Recupera il valore della proprietï¿½ annoSentenza.
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
     * Imposta il valore della proprietï¿½ annoSentenza.
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
     * Recupera il valore della proprietï¿½ numeroSentenza.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroSentenza() {
        return numeroSentenza;
    }

    /**
     * Imposta il valore della proprietï¿½ numeroSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroSentenza(BigInteger value) {
        this.numeroSentenza = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceAutorita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAutorita() {
        return codiceAutorita;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceAutorita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAutorita(String value) {
        this.codiceAutorita = value;
    }

    /**
     * Recupera il valore della proprietï¿½ sedeAutoritaPrinDist.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSedeAutoritaPrinDist() {
        return sedeAutoritaPrinDist;
    }

    /**
     * Imposta il valore della proprietï¿½ sedeAutoritaPrinDist.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSedeAutoritaPrinDist(String value) {
        this.sedeAutoritaPrinDist = value;
    }

    /**
     * Recupera il valore della proprietï¿½ sedeAutoritaPrinc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSedeAutoritaPrinc() {
        return sedeAutoritaPrinc;
    }

    /**
     * Imposta il valore della proprietï¿½ sedeAutoritaPrinc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSedeAutoritaPrinc(String value) {
        this.sedeAutoritaPrinc = value;
    }

    /**
     * Recupera il valore della proprietï¿½ codiceUnivocoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceUnivocoProvvedimento() {
        return codiceUnivocoProvvedimento;
    }

    /**
     * Imposta il valore della proprietï¿½ codiceUnivocoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceUnivocoProvvedimento(String value) {
        this.codiceUnivocoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietï¿½ tipoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProvvedimento() {
        return tipoProvvedimento;
    }

    /**
     * Imposta il valore della proprietï¿½ tipoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProvvedimento(String value) {
        this.tipoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietï¿½ importoAmmenda.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoAmmenda() {
        return importoAmmenda;
    }

    /**
     * Imposta il valore della proprietï¿½ importoAmmenda.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoAmmenda(BigDecimal value) {
        this.importoAmmenda = value;
    }

    /**
     * Recupera il valore della proprietï¿½ importoMulta.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoMulta() {
        return importoMulta;
    }

    /**
     * Imposta il valore della proprietï¿½ importoMulta.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoMulta(BigDecimal value) {
        this.importoMulta = value;
    }

    /**
     * Recupera il valore della proprietï¿½ arresto.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getArresto() {
        return arresto;
    }

    /**
     * Imposta il valore della proprietï¿½ arresto.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setArresto(Durata value) {
        this.arresto = value;
    }

    /**
     * Recupera il valore della proprietï¿½ reclusione.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getReclusione() {
        return reclusione;
    }

    /**
     * Imposta il valore della proprietï¿½ reclusione.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setReclusione(Durata value) {
        this.reclusione = value;
    }

    /**
     * Recupera il valore della proprietï¿½ ergastolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErgastolo() {
        return ergastolo;
    }

    /**
     * Imposta il valore della proprietï¿½ ergastolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErgastolo(String value) {
        this.ergastolo = value;
    }

    /**
     * Recupera il valore della proprietï¿½ isolamentoDiurno.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getIsolamentoDiurno() {
        return isolamentoDiurno;
    }

    /**
     * Imposta il valore della proprietï¿½ isolamentoDiurno.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setIsolamentoDiurno(Durata value) {
        this.isolamentoDiurno = value;
    }

    /**
     * Recupera il valore della proprietï¿½ dataFinePena.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFinePena() {
        return dataFinePena;
    }

    /**
     * Imposta il valore della proprietï¿½ dataFinePena.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFinePena(XMLGregorianCalendar value) {
        this.dataFinePena = value;
    }

    /**
     * Recupera il valore della proprietï¿½ dataFinePenaDal.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFinePenaDal() {
        return dataFinePenaDal;
    }

    /**
     * Imposta il valore della proprietï¿½ dataFinePenaDal.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFinePenaDal(XMLGregorianCalendar value) {
        this.dataFinePenaDal = value;
    }

    /**
     * Recupera il valore della proprietï¿½ dataFinePenaAl.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFinePenaAl() {
        return dataFinePenaAl;
    }

    /**
     * Imposta il valore della proprietï¿½ dataFinePenaAl.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFinePenaAl(XMLGregorianCalendar value) {
        this.dataFinePenaAl = value;
    }

    /**
     * Gets the value of the misuraSicurezza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the misuraSicurezza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMisuraSicurezza().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MisuraSicurezza }
     * 
     * 
     */
    public List<MisuraSicurezza> getMisuraSicurezza() {
        if (misuraSicurezza == null) {
            misuraSicurezza = new ArrayList<MisuraSicurezza>();
        }
        return this.misuraSicurezza;
    }

    /**
     * Gets the value of the penaAccessoria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the penaAccessoria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPenaAccessoria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PenaAccessoria }
     * 
     * 
     */
    public List<PenaAccessoria> getPenaAccessoria() {
        if (penaAccessoria == null) {
            penaAccessoria = new ArrayList<PenaAccessoria>();
        }
        return this.penaAccessoria;
    }

    /**
     * Recupera il valore della proprietï¿½ sanzioniSostitutive.
     * 
     * @return
     *     possible object is
     *     {@link SanzioniSostitutive }
     *     
     */
    public SanzioniSostitutive getSanzioniSostitutive() {
        return sanzioniSostitutive;
    }

    /**
     * Imposta il valore della proprietï¿½ sanzioniSostitutive.
     * 
     * @param value
     *     allowed object is
     *     {@link SanzioniSostitutive }
     *     
     */
    public void setSanzioniSostitutive(SanzioniSostitutive value) {
        this.sanzioniSostitutive = value;
    }

    /**
     * Recupera il valore della proprietï¿½ liberazioneAnticipataConcessaDetrarreCumulo.
     * 
     * @return
     *     possible object is
     *     {@link LiberazioneAnticipataConcessaDetrarreCumulo }
     *     
     */
    public LiberazioneAnticipataConcessaDetrarreCumulo getLiberazioneAnticipataConcessaDetrarreCumulo() {
        return liberazioneAnticipataConcessaDetrarreCumulo;
    }

    /**
     * Imposta il valore della proprietï¿½ liberazioneAnticipataConcessaDetrarreCumulo.
     * 
     * @param value
     *     allowed object is
     *     {@link LiberazioneAnticipataConcessaDetrarreCumulo }
     *     
     */
    public void setLiberazioneAnticipataConcessaDetrarreCumulo(LiberazioneAnticipataConcessaDetrarreCumulo value) {
        this.liberazioneAnticipataConcessaDetrarreCumulo = value;
    }

    /**
     * Recupera il valore della proprietï¿½ penaConversionePenaPecuniaria.
     * 
     * @return
     *     possible object is
     *     {@link PenaConversionePenaPecuniaria }
     *     
     */
    public PenaConversionePenaPecuniaria getPenaConversionePenaPecuniaria() {
        return penaConversionePenaPecuniaria;
    }

    /**
     * Imposta il valore della proprietï¿½ penaConversionePenaPecuniaria.
     * 
     * @param value
     *     allowed object is
     *     {@link PenaConversionePenaPecuniaria }
     *     
     */
    public void setPenaConversionePenaPecuniaria(PenaConversionePenaPecuniaria value) {
        this.penaConversionePenaPecuniaria = value;
    }

    /**
     * Recupera il valore della proprietï¿½ sanzioniGiudicePace.
     * 
     * @return
     *     possible object is
     *     {@link SanzioniGiudicePace }
     *     
     */
    public SanzioniGiudicePace getSanzioniGiudicePace() {
        return sanzioniGiudicePace;
    }

    /**
     * Imposta il valore della proprietï¿½ sanzioniGiudicePace.
     * 
     * @param value
     *     allowed object is
     *     {@link SanzioniGiudicePace }
     *     
     */
    public void setSanzioniGiudicePace(SanzioniGiudicePace value) {
        this.sanzioniGiudicePace = value;
    }

    /**
     * Gets the value of the richiesteGEAnticipazioneEffetti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the richiesteGEAnticipazioneEffetti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRichiesteGEAnticipazioneEffetti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RichiesteGEAnticipazioneEffetti }
     * 
     * 
     */
    public List<RichiesteGEAnticipazioneEffetti> getRichiesteGEAnticipazioneEffetti() {
        if (richiesteGEAnticipazioneEffetti == null) {
            richiesteGEAnticipazioneEffetti = new ArrayList<RichiesteGEAnticipazioneEffetti>();
        }
        return this.richiesteGEAnticipazioneEffetti;
    }

}
