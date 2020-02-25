//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.27 alle 02:09:58 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SOGGETTO_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SOGGETTO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataNascita" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codProvinciaNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idSoggetto" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="listaResidenze" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}RESIDENZA_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="paternita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codCs" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cognomeMadre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroFascicoli" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="descrPosGiuridica" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataFinePena" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="luogoDetenzione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SOGGETTO_TYPE", propOrder = {
    "cognome",
    "nome",
    "sesso",
    "dataNascita",
    "descrComuneNascita",
    "descrStatoNascita",
    "codProvinciaNascita",
    "idSoggetto",
    "listaResidenze",
    "paternita",
    "codCs",
    "cognomeMadre",
    "numeroFascicoli",
    "descrPosGiuridica",
    "dataFinePena",
    "luogoDetenzione",
    "codComuneNascita",
    "codStatoNascita"
})
public class SOGGETTOTYPE {

    @XmlElement(required = true)
    protected String cognome;
    @XmlElement(required = true)
    protected String nome;
    @XmlElement(required = true)
    protected String sesso;
    @XmlElement(required = true)
    protected DATATYPE dataNascita;
    @XmlElement(required = true)
    protected String descrComuneNascita;
    @XmlElement(required = true)
    protected String descrStatoNascita;
    @XmlElement(required = true)
    protected String codProvinciaNascita;
    @XmlElement(required = true)
    protected BigInteger idSoggetto;
    @XmlElement(required = true)
    protected List<RESIDENZATYPE> listaResidenze;
    @XmlElement(required = true)
    protected String paternita;
    @XmlElement(required = true)
    protected String codCs;
    @XmlElement(required = true)
    protected String cognomeMadre;
    @XmlElement(required = true)
    protected BigInteger numeroFascicoli;
    @XmlElement(required = true)
    protected String descrPosGiuridica;
    @XmlElement(required = true)
    protected DATATYPE dataFinePena;
    @XmlElement(required = true)
    protected String luogoDetenzione;
    @XmlElement(required = true)
    protected String codComuneNascita;
    @XmlElement(required = true)
    protected String codStatoNascita;

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
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
     * Recupera il valore della proprietà codProvinciaNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodProvinciaNascita() {
        return codProvinciaNascita;
    }

    /**
     * Imposta il valore della proprietà codProvinciaNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodProvinciaNascita(String value) {
        this.codProvinciaNascita = value;
    }

    /**
     * Recupera il valore della proprietà idSoggetto.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdSoggetto() {
        return idSoggetto;
    }

    /**
     * Imposta il valore della proprietà idSoggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdSoggetto(BigInteger value) {
        this.idSoggetto = value;
    }

    /**
     * Gets the value of the listaResidenze property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaResidenze property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaResidenze().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RESIDENZATYPE }
     * 
     * 
     */
    public List<RESIDENZATYPE> getListaResidenze() {
        if (listaResidenze == null) {
            listaResidenze = new ArrayList<RESIDENZATYPE>();
        }
        return this.listaResidenze;
    }

    /**
     * Recupera il valore della proprietà paternita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaternita() {
        return paternita;
    }

    /**
     * Imposta il valore della proprietà paternita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaternita(String value) {
        this.paternita = value;
    }

    /**
     * Recupera il valore della proprietà codCs.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCs() {
        return codCs;
    }

    /**
     * Imposta il valore della proprietà codCs.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCs(String value) {
        this.codCs = value;
    }

    /**
     * Recupera il valore della proprietà cognomeMadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognomeMadre() {
        return cognomeMadre;
    }

    /**
     * Imposta il valore della proprietà cognomeMadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognomeMadre(String value) {
        this.cognomeMadre = value;
    }

    /**
     * Recupera il valore della proprietà numeroFascicoli.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroFascicoli() {
        return numeroFascicoli;
    }

    /**
     * Imposta il valore della proprietà numeroFascicoli.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroFascicoli(BigInteger value) {
        this.numeroFascicoli = value;
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
     * Recupera il valore della proprietà luogoDetenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLuogoDetenzione() {
        return luogoDetenzione;
    }

    /**
     * Imposta il valore della proprietà luogoDetenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLuogoDetenzione(String value) {
        this.luogoDetenzione = value;
    }

    /**
     * Recupera il valore della proprietà codComuneNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodComuneNascita() {
        return codComuneNascita;
    }

    /**
     * Imposta il valore della proprietà codComuneNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodComuneNascita(String value) {
        this.codComuneNascita = value;
    }

    /**
     * Recupera il valore della proprietà codStatoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodStatoNascita() {
        return codStatoNascita;
    }

    /**
     * Imposta il valore della proprietà codStatoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodStatoNascita(String value) {
        this.codStatoNascita = value;
    }

}
