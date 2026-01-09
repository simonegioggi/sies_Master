
package it.mig.sies.type.foglicomplementari;

import java.math.BigDecimal;
import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
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
    "dataFinePenaAl"
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

    /**
     * Recupera il valore della proprieta chiaveSies.
     * 
     */
    public long getChiaveSies() {
        return chiaveSies;
    }

    /**
     * Imposta il valore della proprieta chiaveSies.
     * 
     */
    public void setChiaveSies(long value) {
        this.chiaveSies = value;
    }

    /**
     * Recupera il valore della proprieta chiaveNsc.
     * 
     */
    public long getChiaveNsc() {
        return chiaveNsc;
    }

    /**
     * Imposta il valore della proprieta chiaveNsc.
     * 
     */
    public void setChiaveNsc(long value) {
        this.chiaveNsc = value;
    }

    /**
     * Recupera il valore della proprieta dataProvvedimento.
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
     * Imposta il valore della proprieta dataProvvedimento.
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
     * Recupera il valore della proprieta annoOrdinanza.
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
     * Imposta il valore della proprieta annoOrdinanza.
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
     * Recupera il valore della proprieta numeroOrdinanza.
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
     * Imposta il valore della proprieta numeroOrdinanza.
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
     * Recupera il valore della proprieta codiceSedePM.
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
     * Imposta il valore della proprieta codiceSedePM.
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
     * Recupera il valore della proprieta annoSIEP.
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
     * Imposta il valore della proprieta annoSIEP.
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
     * Recupera il valore della proprieta numeroSIEP.
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
     * Imposta il valore della proprieta numeroSIEP.
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
     * Recupera il valore della proprieta annoSentenza.
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
     * Imposta il valore della proprieta annoSentenza.
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
     * Recupera il valore della proprieta numeroSentenza.
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
     * Imposta il valore della proprieta numeroSentenza.
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
     * Recupera il valore della proprieta codiceAutorita.
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
     * Imposta il valore della proprieta codiceAutorita.
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
     * Recupera il valore della proprieta sedeAutoritaPrinDist.
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
     * Imposta il valore della proprieta sedeAutoritaPrinDist.
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
     * Recupera il valore della proprieta sedeAutoritaPrinc.
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
     * Imposta il valore della proprieta sedeAutoritaPrinc.
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
     * Recupera il valore della proprieta codiceUnivocoProvvedimento.
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
     * Imposta il valore della proprieta codiceUnivocoProvvedimento.
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
     * Recupera il valore della proprieta tipoProvvedimento.
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
     * Imposta il valore della proprieta tipoProvvedimento.
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
     * Recupera il valore della proprieta importoAmmenda.
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
     * Imposta il valore della proprieta importoAmmenda.
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
     * Recupera il valore della proprieta importoMulta.
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
     * Imposta il valore della proprieta importoMulta.
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
     * Recupera il valore della proprieta arresto.
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
     * Imposta il valore della proprieta arresto.
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
     * Recupera il valore della proprieta reclusione.
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
     * Imposta il valore della proprieta reclusione.
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
     * Recupera il valore della proprieta ergastolo.
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
     * Imposta il valore della proprieta ergastolo.
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
     * Recupera il valore della proprieta isolamentoDiurno.
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
     * Imposta il valore della proprieta isolamentoDiurno.
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
     * Recupera il valore della proprieta dataFinePena.
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
     * Imposta il valore della proprieta dataFinePena.
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
     * Recupera il valore della proprieta dataFinePenaDal.
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
     * Imposta il valore della proprieta dataFinePenaDal.
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
     * Recupera il valore della proprieta dataFinePenaAl.
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
     * Imposta il valore della proprieta dataFinePenaAl.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFinePenaAl(XMLGregorianCalendar value) {
        this.dataFinePenaAl = value;
    }

}
