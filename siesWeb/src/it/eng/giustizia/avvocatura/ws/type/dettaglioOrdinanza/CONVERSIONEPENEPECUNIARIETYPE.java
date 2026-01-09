//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per CONVERSIONE_PENE_PECUNIARIE_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="CONVERSIONE_PENE_PECUNIARIE_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annoFascSIEP" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroFascSIEP" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="codTipoSanzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="importoMulta" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="importoAmmenda" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="durataEsito" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *         &lt;element name="codOggettoTenore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrOggettoTenore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="valoreRata" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="valoreUltimaRata" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="dataInizioPagamento" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATA_TYPE"/>
 *         &lt;element name="numeroGiorniInizioPagamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroRate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CONVERSIONE_PENE_PECUNIARIE_TYPE", propOrder = {
    "annoFascSIEP",
    "numeroFascSIEP",
    "codTipoSanzione",
    "importoMulta",
    "importoAmmenda",
    "durataEsito",
    "codOggettoTenore",
    "descrOggettoTenore",
    "valoreRata",
    "valoreUltimaRata",
    "dataInizioPagamento",
    "numeroGiorniInizioPagamento",
    "numeroRate"
})
public class CONVERSIONEPENEPECUNIARIETYPE {

    @XmlElement(required = true)
    protected BigInteger annoFascSIEP;
    @XmlElement(required = true)
    protected BigInteger numeroFascSIEP;
    @XmlElement(required = true)
    protected String codTipoSanzione;
    @XmlElement(required = true)
    protected BigDecimal importoMulta;
    @XmlElement(required = true)
    protected BigDecimal importoAmmenda;
    @XmlElement(required = true)
    protected DURATATYPE durataEsito;
    @XmlElement(required = true)
    protected String codOggettoTenore;
    @XmlElement(required = true)
    protected String descrOggettoTenore;
    @XmlElement(required = true)
    protected BigDecimal valoreRata;
    @XmlElement(required = true)
    protected BigDecimal valoreUltimaRata;
    @XmlElement(required = true)
    protected DATATYPE dataInizioPagamento;
    @XmlElement(required = true)
    protected String numeroGiorniInizioPagamento;
    protected int numeroRate;

    /**
     * Recupera il valore della proprietà annoFascSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoFascSIEP() {
        return annoFascSIEP;
    }

    /**
     * Imposta il valore della proprietà annoFascSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoFascSIEP(BigInteger value) {
        this.annoFascSIEP = value;
    }

    /**
     * Recupera il valore della proprietà numeroFascSIEP.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroFascSIEP() {
        return numeroFascSIEP;
    }

    /**
     * Imposta il valore della proprietà numeroFascSIEP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroFascSIEP(BigInteger value) {
        this.numeroFascSIEP = value;
    }

    /**
     * Recupera il valore della proprietà codTipoSanzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoSanzione() {
        return codTipoSanzione;
    }

    /**
     * Imposta il valore della proprietà codTipoSanzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoSanzione(String value) {
        this.codTipoSanzione = value;
    }

    /**
     * Recupera il valore della proprietà importoMulta.
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
     * Imposta il valore della proprietà importoMulta.
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
     * Recupera il valore della proprietà importoAmmenda.
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
     * Imposta il valore della proprietà importoAmmenda.
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
     * Recupera il valore della proprietà durataEsito.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataEsito() {
        return durataEsito;
    }

    /**
     * Imposta il valore della proprietà durataEsito.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataEsito(DURATATYPE value) {
        this.durataEsito = value;
    }

    /**
     * Recupera il valore della proprietà codOggettoTenore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOggettoTenore() {
        return codOggettoTenore;
    }

    /**
     * Imposta il valore della proprietà codOggettoTenore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOggettoTenore(String value) {
        this.codOggettoTenore = value;
    }

    /**
     * Recupera il valore della proprietà descrOggettoTenore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrOggettoTenore() {
        return descrOggettoTenore;
    }

    /**
     * Imposta il valore della proprietà descrOggettoTenore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrOggettoTenore(String value) {
        this.descrOggettoTenore = value;
    }

    /**
     * Recupera il valore della proprietà valoreRata.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValoreRata() {
        return valoreRata;
    }

    /**
     * Imposta il valore della proprietà valoreRata.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValoreRata(BigDecimal value) {
        this.valoreRata = value;
    }

    /**
     * Recupera il valore della proprietà valoreUltimaRata.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValoreUltimaRata() {
        return valoreUltimaRata;
    }

    /**
     * Imposta il valore della proprietà valoreUltimaRata.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValoreUltimaRata(BigDecimal value) {
        this.valoreUltimaRata = value;
    }

    /**
     * Recupera il valore della proprietà dataInizioPagamento.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataInizioPagamento() {
        return dataInizioPagamento;
    }

    /**
     * Imposta il valore della proprietà dataInizioPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataInizioPagamento(DATATYPE value) {
        this.dataInizioPagamento = value;
    }

    /**
     * Recupera il valore della proprietà numeroGiorniInizioPagamento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroGiorniInizioPagamento() {
        return numeroGiorniInizioPagamento;
    }

    /**
     * Imposta il valore della proprietà numeroGiorniInizioPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroGiorniInizioPagamento(String value) {
        this.numeroGiorniInizioPagamento = value;
    }

    /**
     * Recupera il valore della proprietà numeroRate.
     * 
     */
    public int getNumeroRate() {
        return numeroRate;
    }

    /**
     * Imposta il valore della proprietà numeroRate.
     * 
     */
    public void setNumeroRate(int value) {
        this.numeroRate = value;
    }

}
