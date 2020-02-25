//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per PERMESSO_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PERMESSO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrTipoPermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrStatoPermesso" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="durataPermesso" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DURATA_TYPE"/>
 *         &lt;element name="luogoSvolgimentoProva" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagScorta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PERMESSO_TYPE", propOrder = {
    "descrTipoPermesso",
    "descrStatoPermesso",
    "durataPermesso",
    "luogoSvolgimentoProva",
    "flagScorta",
    "note"
})
public class PERMESSOTYPE {

    @XmlElement(required = true)
    protected String descrTipoPermesso;
    @XmlElement(required = true)
    protected String descrStatoPermesso;
    @XmlElement(required = true)
    protected DURATATYPE durataPermesso;
    @XmlElement(required = true)
    protected String luogoSvolgimentoProva;
    @XmlElement(required = true)
    protected String flagScorta;
    @XmlElement(required = true)
    protected String note;

    /**
     * Recupera il valore della proprietà descrTipoPermesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoPermesso() {
        return descrTipoPermesso;
    }

    /**
     * Imposta il valore della proprietà descrTipoPermesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoPermesso(String value) {
        this.descrTipoPermesso = value;
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

    /**
     * Recupera il valore della proprietà durataPermesso.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataPermesso() {
        return durataPermesso;
    }

    /**
     * Imposta il valore della proprietà durataPermesso.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataPermesso(DURATATYPE value) {
        this.durataPermesso = value;
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
     * Recupera il valore della proprietà flagScorta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagScorta() {
        return flagScorta;
    }

    /**
     * Imposta il valore della proprietà flagScorta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagScorta(String value) {
        this.flagScorta = value;
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

}
