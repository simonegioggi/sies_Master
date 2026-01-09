//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FASCICOLO_SIUS_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FASCICOLO_SIUS_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idFascicoloSius" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="chiaveAnno" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="chiaveProg" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="dataIscrizione" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrPosizioneMateriale" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoFascicolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fascicoloUnificante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataDefinizione" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrDefinizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoDefinizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annoS1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="progrS1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoFascicoliUnificati" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="elencoFascicoliCollegati" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fascicoloPadre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}UDIENZA"/>
 *         &lt;element name="contenuto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}MAGISTRATO"/>
 *         &lt;element name="descrCancelleriaAssegnataria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="annotazioniProcedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="listaNote" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}NOTE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="ulterioriIstanze" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FASCICOLO_SIUS_TYPE", propOrder = {
    "idFascicoloSius",
    "chiaveAnno",
    "chiaveProg",
    "dataIscrizione",
    "descrPosizioneMateriale",
    "descrStatoFascicolo",
    "fascicoloUnificante",
    "dataDefinizione",
    "descrDefinizione",
    "tipoDefinizione",
    "codTipoRegistro",
    "descrTipoRegistro",
    "annoS1",
    "progrS1",
    "elencoFascicoliUnificati",
    "elencoFascicoliCollegati",
    "fascicoloPadre",
    "udienza",
    "contenuto",
    "magistrato",
    "descrCancelleriaAssegnataria",
    "annotazioniProcedimento",
    "listaNote",
    "ulterioriIstanze"
})
@XmlSeeAlso({
    FASCICOLOSIUS.class
})
public class FASCICOLOSIUSTYPE {

    @XmlElement(required = true)
    protected BigInteger idFascicoloSius;
    @XmlElement(required = true)
    protected BigInteger chiaveAnno;
    @XmlElement(required = true)
    protected BigInteger chiaveProg;
    @XmlElement(required = true)
    protected DATATYPE dataIscrizione;
    @XmlElement(required = true)
    protected String descrPosizioneMateriale;
    @XmlElement(required = true)
    protected String descrStatoFascicolo;
    @XmlElement(required = true)
    protected String fascicoloUnificante;
    @XmlElement(required = true)
    protected DATATYPE dataDefinizione;
    @XmlElement(required = true)
    protected String descrDefinizione;
    @XmlElement(required = true)
    protected String tipoDefinizione;
    @XmlElement(required = true)
    protected String codTipoRegistro;
    @XmlElement(required = true)
    protected String descrTipoRegistro;
    @XmlElement(required = true)
    protected String annoS1;
    @XmlElement(required = true)
    protected String progrS1;
    @XmlElement(required = true)
    protected String elencoFascicoliUnificati;
    @XmlElement(required = true)
    protected String elencoFascicoliCollegati;
    @XmlElement(required = true)
    protected String fascicoloPadre;
    @XmlElement(name = "UDIENZA", required = true)
    protected UDIENZA udienza;
    @XmlElement(required = true)
    protected String contenuto;
    @XmlElement(name = "MAGISTRATO", required = true)
    protected MAGISTRATO magistrato;
    @XmlElement(required = true)
    protected String descrCancelleriaAssegnataria;
    @XmlElement(required = true)
    protected String annotazioniProcedimento;
    @XmlElement(required = true)
    protected List<NOTETYPE> listaNote;
    protected boolean ulterioriIstanze;

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
     * Recupera il valore della proprietà chiaveProg.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChiaveProg() {
        return chiaveProg;
    }

    /**
     * Imposta il valore della proprietà chiaveProg.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChiaveProg(BigInteger value) {
        this.chiaveProg = value;
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
     * Recupera il valore della proprietà descrPosizioneMateriale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrPosizioneMateriale() {
        return descrPosizioneMateriale;
    }

    /**
     * Imposta il valore della proprietà descrPosizioneMateriale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrPosizioneMateriale(String value) {
        this.descrPosizioneMateriale = value;
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
     * Recupera il valore della proprietà fascicoloUnificante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFascicoloUnificante() {
        return fascicoloUnificante;
    }

    /**
     * Imposta il valore della proprietà fascicoloUnificante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFascicoloUnificante(String value) {
        this.fascicoloUnificante = value;
    }

    /**
     * Recupera il valore della proprietà dataDefinizione.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataDefinizione() {
        return dataDefinizione;
    }

    /**
     * Imposta il valore della proprietà dataDefinizione.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataDefinizione(DATATYPE value) {
        this.dataDefinizione = value;
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
     * Recupera il valore della proprietà tipoDefinizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDefinizione() {
        return tipoDefinizione;
    }

    /**
     * Imposta il valore della proprietà tipoDefinizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDefinizione(String value) {
        this.tipoDefinizione = value;
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
     * Recupera il valore della proprietà descrTipoRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoRegistro() {
        return descrTipoRegistro;
    }

    /**
     * Imposta il valore della proprietà descrTipoRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoRegistro(String value) {
        this.descrTipoRegistro = value;
    }

    /**
     * Recupera il valore della proprietà annoS1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnoS1() {
        return annoS1;
    }

    /**
     * Imposta il valore della proprietà annoS1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoS1(String value) {
        this.annoS1 = value;
    }

    /**
     * Recupera il valore della proprietà progrS1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProgrS1() {
        return progrS1;
    }

    /**
     * Imposta il valore della proprietà progrS1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProgrS1(String value) {
        this.progrS1 = value;
    }

    /**
     * Recupera il valore della proprietà elencoFascicoliUnificati.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElencoFascicoliUnificati() {
        return elencoFascicoliUnificati;
    }

    /**
     * Imposta il valore della proprietà elencoFascicoliUnificati.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElencoFascicoliUnificati(String value) {
        this.elencoFascicoliUnificati = value;
    }

    /**
     * Recupera il valore della proprietà elencoFascicoliCollegati.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElencoFascicoliCollegati() {
        return elencoFascicoliCollegati;
    }

    /**
     * Imposta il valore della proprietà elencoFascicoliCollegati.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElencoFascicoliCollegati(String value) {
        this.elencoFascicoliCollegati = value;
    }

    /**
     * Recupera il valore della proprietà fascicoloPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFascicoloPadre() {
        return fascicoloPadre;
    }

    /**
     * Imposta il valore della proprietà fascicoloPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFascicoloPadre(String value) {
        this.fascicoloPadre = value;
    }

    /**
     * dati dell'udienza
     * 
     * @return
     *     possible object is
     *     {@link UDIENZA }
     *     
     */
    public UDIENZA getUDIENZA() {
        return udienza;
    }

    /**
     * Imposta il valore della proprietà udienza.
     * 
     * @param value
     *     allowed object is
     *     {@link UDIENZA }
     *     
     */
    public void setUDIENZA(UDIENZA value) {
        this.udienza = value;
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
     * dati del magistrato
     * 
     * @return
     *     possible object is
     *     {@link MAGISTRATO }
     *     
     */
    public MAGISTRATO getMAGISTRATO() {
        return magistrato;
    }

    /**
     * Imposta il valore della proprietà magistrato.
     * 
     * @param value
     *     allowed object is
     *     {@link MAGISTRATO }
     *     
     */
    public void setMAGISTRATO(MAGISTRATO value) {
        this.magistrato = value;
    }

    /**
     * Recupera il valore della proprietà descrCancelleriaAssegnataria.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrCancelleriaAssegnataria() {
        return descrCancelleriaAssegnataria;
    }

    /**
     * Imposta il valore della proprietà descrCancelleriaAssegnataria.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrCancelleriaAssegnataria(String value) {
        this.descrCancelleriaAssegnataria = value;
    }

    /**
     * Recupera il valore della proprietà annotazioniProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnotazioniProcedimento() {
        return annotazioniProcedimento;
    }

    /**
     * Imposta il valore della proprietà annotazioniProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnotazioniProcedimento(String value) {
        this.annotazioniProcedimento = value;
    }

    /**
     * Gets the value of the listaNote property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaNote property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NOTETYPE }
     * 
     * 
     */
    public List<NOTETYPE> getListaNote() {
        if (listaNote == null) {
            listaNote = new ArrayList<NOTETYPE>();
        }
        return this.listaNote;
    }

    /**
     * Recupera il valore della proprietà ulterioriIstanze.
     * 
     */
    public boolean isUlterioriIstanze() {
        return ulterioriIstanze;
    }

    /**
     * Imposta il valore della proprietà ulterioriIstanze.
     * 
     */
    public void setUlterioriIstanze(boolean value) {
        this.ulterioriIstanze = value;
    }

}
