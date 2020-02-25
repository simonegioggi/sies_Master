//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per LICENZA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="LICENZA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiTipoLicenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeGiorni" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeMesi" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeOre" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataInizio" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="dataFine" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATA_TYPE"/>
 *         &lt;element name="oraInizio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="oraFine" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sommaRisarcDanni" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="descrStatoPermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LICENZA_TYPE", propOrder = {
    "codiTipoLicenza",
    "numeGiorni",
    "numeMesi",
    "numeOre",
    "dataInizio",
    "dataFine",
    "oraInizio",
    "oraFine",
    "luogoSvolgimentoProva",
    "sommaRisarcDanni",
    "descrStatoPermesso"
})
public class LICENZATYPE {

    @XmlElement(required = true)
    protected String codiTipoLicenza;
    @XmlElement(required = true)
    protected BigInteger numeGiorni;
    @XmlElement(required = true)
    protected BigInteger numeMesi;
    @XmlElement(required = true)
    protected BigInteger numeOre;
    @XmlElement(required = true)
    protected DATATYPE dataInizio;
    @XmlElement(required = true)
    protected DATATYPE dataFine;
    @XmlElement(required = true)
    protected String oraInizio;
    @XmlElement(required = true)
    protected String oraFine;
    @XmlElement(required = true)
    protected String luogoSvolgimentoProva;
    @XmlElement(required = true)
    protected BigDecimal sommaRisarcDanni;
    @XmlElement(required = true, nillable = true)
    protected String descrStatoPermesso;

    /**
     * Recupera il valore della proprietà codiTipoLicenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiTipoLicenza() {
        return codiTipoLicenza;
    }

    /**
     * Imposta il valore della proprietà codiTipoLicenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiTipoLicenza(String value) {
        this.codiTipoLicenza = value;
    }

    /**
     * Recupera il valore della proprietà numeGiorni.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeGiorni() {
        return numeGiorni;
    }

    /**
     * Imposta il valore della proprietà numeGiorni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeGiorni(BigInteger value) {
        this.numeGiorni = value;
    }

    /**
     * Recupera il valore della proprietà numeMesi.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeMesi() {
        return numeMesi;
    }

    /**
     * Imposta il valore della proprietà numeMesi.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeMesi(BigInteger value) {
        this.numeMesi = value;
    }

    /**
     * Recupera il valore della proprietà numeOre.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeOre() {
        return numeOre;
    }

    /**
     * Imposta il valore della proprietà numeOre.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeOre(BigInteger value) {
        this.numeOre = value;
    }

    /**
     * Recupera il valore della proprietà dataInizio.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataInizio() {
        return dataInizio;
    }

    /**
     * Imposta il valore della proprietà dataInizio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataInizio(DATATYPE value) {
        this.dataInizio = value;
    }

    /**
     * Recupera il valore della proprietà dataFine.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataFine() {
        return dataFine;
    }

    /**
     * Imposta il valore della proprietà dataFine.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataFine(DATATYPE value) {
        this.dataFine = value;
    }

    /**
     * Recupera il valore della proprietà oraInizio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOraInizio() {
        return oraInizio;
    }

    /**
     * Imposta il valore della proprietà oraInizio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOraInizio(String value) {
        this.oraInizio = value;
    }

    /**
     * Recupera il valore della proprietà oraFine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOraFine() {
        return oraFine;
    }

    /**
     * Imposta il valore della proprietà oraFine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOraFine(String value) {
        this.oraFine = value;
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
     * Recupera il valore della proprietà sommaRisarcDanni.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSommaRisarcDanni() {
        return sommaRisarcDanni;
    }

    /**
     * Imposta il valore della proprietà sommaRisarcDanni.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSommaRisarcDanni(BigDecimal value) {
        this.sommaRisarcDanni = value;
    }

    /**
     * Recupera il valore della proprietà descrStatoPermesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrStatoPermesso() {
        return descrStatoPermesso;
    }

    /**
     * Imposta il valore della proprietà descrStatoPermesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrStatoPermesso(String value) {
        this.descrStatoPermesso = value;
    }

}
