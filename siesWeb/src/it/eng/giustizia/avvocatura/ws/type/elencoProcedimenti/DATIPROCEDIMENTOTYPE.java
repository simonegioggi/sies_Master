//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.02 alle 03:59:57 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DATI_PROCEDIMENTO_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DATI_PROCEDIMENTO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idFascicoloSius" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="chiaveAnno" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="chiaveProgr" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="codPosGiuridica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrPosGiuridica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataCameraConsiglio" type="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}DATA_TYPE"/>
 *         &lt;element name="codStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codOggettoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrOggettoProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataRichiesta" type="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}DATA_TYPE"/>
 *         &lt;element name="dataAggiornamento" type="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrDefinizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoAtto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoAtto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATI_PROCEDIMENTO_TYPE", propOrder = {
    "idFascicoloSius",
    "chiaveAnno",
    "chiaveProgr",
    "codPosGiuridica",
    "descrPosGiuridica",
    "dataCameraConsiglio",
    "codStatoFascicolo",
    "descrStatoFascicolo",
    "codOggettoProcedimento",
    "descrOggettoProcedimento",
    "dataRichiesta",
    "dataAggiornamento",
    "descrDefinizione",
    "codTipoAtto",
    "descrTipoAtto",
    "codTipoUfficio"
})
public class DATIPROCEDIMENTOTYPE {

    @XmlElement(required = true)
    protected BigInteger idFascicoloSius;
    @XmlElement(required = true)
    protected BigInteger chiaveAnno;
    @XmlElement(required = true)
    protected BigInteger chiaveProgr;
    @XmlElement(required = true)
    protected String codPosGiuridica;
    @XmlElement(required = true)
    protected String descrPosGiuridica;
    @XmlElement(required = true, nillable = true)
    protected DATATYPE dataCameraConsiglio;
    @XmlElement(required = true, nillable = true)
    protected String codStatoFascicolo;
    @XmlElement(required = true, nillable = true)
    protected String descrStatoFascicolo;
    @XmlElement(required = true, nillable = true)
    protected String codOggettoProcedimento;
    @XmlElement(required = true, nillable = true)
    protected String descrOggettoProcedimento;
    @XmlElement(required = true, nillable = true)
    protected DATATYPE dataRichiesta;
    @XmlElement(required = true, nillable = true)
    protected DATATYPE dataAggiornamento;
    @XmlElement(required = true, nillable = true)
    protected String descrDefinizione;
    @XmlElement(required = true)
    protected String codTipoAtto;
    @XmlElement(required = true, nillable = true)
    protected String descrTipoAtto;
    @XmlElement(required = true)
    protected String codTipoUfficio;

    /**
     * Recupera il valore della proprietà idFascicoloSius.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdFascicoloSius() {
        return idFascicoloSius;
    }

    /**
     * Imposta il valore della proprietà idFascicoloSius.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdFascicoloSius(BigInteger value) {
        this.idFascicoloSius = value;
    }

    /**
     * Recupera il valore della proprietà chiaveAnno.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChiaveAnno() {
        return chiaveAnno;
    }

    /**
     * Imposta il valore della proprietà chiaveAnno.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChiaveAnno(BigInteger value) {
        this.chiaveAnno = value;
    }

    /**
     * Recupera il valore della proprietà chiaveProgr.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChiaveProgr() {
        return chiaveProgr;
    }

    /**
     * Imposta il valore della proprietà chiaveProgr.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChiaveProgr(BigInteger value) {
        this.chiaveProgr = value;
    }

    /**
     * Recupera il valore della proprietà codPosGiuridica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPosGiuridica() {
        return codPosGiuridica;
    }

    /**
     * Imposta il valore della proprietà codPosGiuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPosGiuridica(String value) {
        this.codPosGiuridica = value;
    }

    /**
     * Recupera il valore della proprietà descrPosGiuridica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrPosGiuridica() {
        return descrPosGiuridica;
    }

    /**
     * Imposta il valore della proprietà descrPosGiuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrPosGiuridica(String value) {
        this.descrPosGiuridica = value;
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
     * Recupera il valore della proprietà codStatoFascicolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodStatoFascicolo() {
        return codStatoFascicolo;
    }

    /**
     * Imposta il valore della proprietà codStatoFascicolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodStatoFascicolo(String value) {
        this.codStatoFascicolo = value;
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
     * Recupera il valore della proprietà codOggettoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOggettoProcedimento() {
        return codOggettoProcedimento;
    }

    /**
     * Imposta il valore della proprietà codOggettoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOggettoProcedimento(String value) {
        this.codOggettoProcedimento = value;
    }

    /**
     * Recupera il valore della proprietà descrOggettoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrOggettoProcedimento() {
        return descrOggettoProcedimento;
    }

    /**
     * Imposta il valore della proprietà descrOggettoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrOggettoProcedimento(String value) {
        this.descrOggettoProcedimento = value;
    }

    /**
     * Recupera il valore della proprietà dataRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataRichiesta() {
        return dataRichiesta;
    }

    /**
     * Imposta il valore della proprietà dataRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataRichiesta(DATATYPE value) {
        this.dataRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà dataAggiornamento.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataAggiornamento() {
        return dataAggiornamento;
    }

    /**
     * Imposta il valore della proprietà dataAggiornamento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataAggiornamento(DATATYPE value) {
        this.dataAggiornamento = value;
    }

    /**
     * Recupera il valore della proprietà descrDefinizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrDefinizione() {
        return descrDefinizione;
    }

    /**
     * Imposta il valore della proprietà descrDefinizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrDefinizione(String value) {
        this.descrDefinizione = value;
    }

    /**
     * Recupera il valore della proprietà codTipoAtto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoAtto() {
        return codTipoAtto;
    }

    /**
     * Imposta il valore della proprietà codTipoAtto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoAtto(String value) {
        this.codTipoAtto = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoAtto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoAtto() {
        return descrTipoAtto;
    }

    /**
     * Imposta il valore della proprietà descrTipoAtto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoAtto(String value) {
        this.descrTipoAtto = value;
    }

    /**
     * Recupera il valore della proprietà codTipoUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoUfficio() {
        return codTipoUfficio;
    }

    /**
     * Imposta il valore della proprietà codTipoUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoUfficio(String value) {
        this.codTipoUfficio = value;
    }

}
