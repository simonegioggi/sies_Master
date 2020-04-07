//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per PENA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PENA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataInizioPena" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="dataFinePena" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="sanzSostResidua" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagErgastolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numAnniIsolamentoDiurno" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numMesiIsolamentoDiurno" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numGiorniIsolamentoDiurno" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numAnniReclusione" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numMesiReclusione" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numGiorniReclusione" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="importoMulta" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="numAnniArresto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numMesiArresto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numGiorniArresto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="importoAmmenda" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="codTipoPenaDetentiva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoPenaDetentiva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PENA_TYPE", propOrder = {
    "dataInizioPena",
    "dataFinePena",
    "sanzSostResidua",
    "flagErgastolo",
    "numAnniIsolamentoDiurno",
    "numMesiIsolamentoDiurno",
    "numGiorniIsolamentoDiurno",
    "numAnniReclusione",
    "numMesiReclusione",
    "numGiorniReclusione",
    "importoMulta",
    "numAnniArresto",
    "numMesiArresto",
    "numGiorniArresto",
    "importoAmmenda",
    "codTipoPenaDetentiva",
    "descrTipoPenaDetentiva"
})
public class PENATYPE {

    @XmlElement(required = true)
    protected DATATYPE dataInizioPena;
    @XmlElement(required = true)
    protected DATATYPE dataFinePena;
    @XmlElement(required = true)
    protected String sanzSostResidua;
    @XmlElement(required = true)
    protected String flagErgastolo;
    @XmlElement(required = true)
    protected BigInteger numAnniIsolamentoDiurno;
    @XmlElement(required = true)
    protected BigInteger numMesiIsolamentoDiurno;
    @XmlElement(required = true)
    protected BigInteger numGiorniIsolamentoDiurno;
    @XmlElement(required = true)
    protected BigInteger numAnniReclusione;
    @XmlElement(required = true)
    protected BigInteger numMesiReclusione;
    @XmlElement(required = true)
    protected BigInteger numGiorniReclusione;
    @XmlElement(required = true)
    protected BigDecimal importoMulta;
    @XmlElement(required = true)
    protected BigInteger numAnniArresto;
    @XmlElement(required = true)
    protected BigInteger numMesiArresto;
    @XmlElement(required = true)
    protected BigInteger numGiorniArresto;
    @XmlElement(required = true)
    protected BigDecimal importoAmmenda;
    @XmlElement(required = true)
    protected String codTipoPenaDetentiva;
    @XmlElement(required = true)
    protected String descrTipoPenaDetentiva;

    /**
     * Recupera il valore della proprietà dataInizioPena.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataInizioPena() {
        return dataInizioPena;
    }

    /**
     * Imposta il valore della proprietà dataInizioPena.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataInizioPena(DATATYPE value) {
        this.dataInizioPena = value;
    }

    /**
     * Recupera il valore della proprietà dataFinePena.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataFinePena() {
        return dataFinePena;
    }

    /**
     * Imposta il valore della proprietà dataFinePena.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataFinePena(DATATYPE value) {
        this.dataFinePena = value;
    }

    /**
     * Recupera il valore della proprietà sanzSostResidua.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSanzSostResidua() {
        return sanzSostResidua;
    }

    /**
     * Imposta il valore della proprietà sanzSostResidua.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSanzSostResidua(String value) {
        this.sanzSostResidua = value;
    }

    /**
     * Recupera il valore della proprietà flagErgastolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagErgastolo() {
        return flagErgastolo;
    }

    /**
     * Imposta il valore della proprietà flagErgastolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagErgastolo(String value) {
        this.flagErgastolo = value;
    }

    /**
     * Recupera il valore della proprietà numAnniIsolamentoDiurno.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumAnniIsolamentoDiurno() {
        return numAnniIsolamentoDiurno;
    }

    /**
     * Imposta il valore della proprietà numAnniIsolamentoDiurno.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumAnniIsolamentoDiurno(BigInteger value) {
        this.numAnniIsolamentoDiurno = value;
    }

    /**
     * Recupera il valore della proprietà numMesiIsolamentoDiurno.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumMesiIsolamentoDiurno() {
        return numMesiIsolamentoDiurno;
    }

    /**
     * Imposta il valore della proprietà numMesiIsolamentoDiurno.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumMesiIsolamentoDiurno(BigInteger value) {
        this.numMesiIsolamentoDiurno = value;
    }

    /**
     * Recupera il valore della proprietà numGiorniIsolamentoDiurno.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumGiorniIsolamentoDiurno() {
        return numGiorniIsolamentoDiurno;
    }

    /**
     * Imposta il valore della proprietà numGiorniIsolamentoDiurno.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumGiorniIsolamentoDiurno(BigInteger value) {
        this.numGiorniIsolamentoDiurno = value;
    }

    /**
     * Recupera il valore della proprietà numAnniReclusione.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumAnniReclusione() {
        return numAnniReclusione;
    }

    /**
     * Imposta il valore della proprietà numAnniReclusione.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumAnniReclusione(BigInteger value) {
        this.numAnniReclusione = value;
    }

    /**
     * Recupera il valore della proprietà numMesiReclusione.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumMesiReclusione() {
        return numMesiReclusione;
    }

    /**
     * Imposta il valore della proprietà numMesiReclusione.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumMesiReclusione(BigInteger value) {
        this.numMesiReclusione = value;
    }

    /**
     * Recupera il valore della proprietà numGiorniReclusione.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumGiorniReclusione() {
        return numGiorniReclusione;
    }

    /**
     * Imposta il valore della proprietà numGiorniReclusione.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumGiorniReclusione(BigInteger value) {
        this.numGiorniReclusione = value;
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
     * Recupera il valore della proprietà numAnniArresto.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumAnniArresto() {
        return numAnniArresto;
    }

    /**
     * Imposta il valore della proprietà numAnniArresto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumAnniArresto(BigInteger value) {
        this.numAnniArresto = value;
    }

    /**
     * Recupera il valore della proprietà numMesiArresto.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumMesiArresto() {
        return numMesiArresto;
    }

    /**
     * Imposta il valore della proprietà numMesiArresto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumMesiArresto(BigInteger value) {
        this.numMesiArresto = value;
    }

    /**
     * Recupera il valore della proprietà numGiorniArresto.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumGiorniArresto() {
        return numGiorniArresto;
    }

    /**
     * Imposta il valore della proprietà numGiorniArresto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumGiorniArresto(BigInteger value) {
        this.numGiorniArresto = value;
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
     * Recupera il valore della proprietà codTipoPenaDetentiva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoPenaDetentiva() {
        return codTipoPenaDetentiva;
    }

    /**
     * Imposta il valore della proprietà codTipoPenaDetentiva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoPenaDetentiva(String value) {
        this.codTipoPenaDetentiva = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoPenaDetentiva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoPenaDetentiva() {
        return descrTipoPenaDetentiva;
    }

    /**
     * Imposta il valore della proprietà descrTipoPenaDetentiva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoPenaDetentiva(String value) {
        this.descrTipoPenaDetentiva = value;
    }

}
