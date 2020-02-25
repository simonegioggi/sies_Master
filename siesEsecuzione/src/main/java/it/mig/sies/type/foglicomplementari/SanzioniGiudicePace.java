//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="permanenzaDomiciliare" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="lavoroPubblicaUtilita" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="lavoroSostitutivo" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="espulsioneStato" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="tipoEspulsioneStato">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
    "permanenzaDomiciliare",
    "lavoroPubblicaUtilita",
    "lavoroSostitutivo",
    "espulsioneStato",
    "tipoEspulsioneStato"
})
public class SanzioniGiudicePace {

    @XmlElement(required = true, nillable = true)
    protected Durata permanenzaDomiciliare;
    @XmlElement(required = true, nillable = true)
    protected Durata lavoroPubblicaUtilita;
    @XmlElement(required = true, nillable = true)
    protected Durata lavoroSostitutivo;
    @XmlElement(required = true, nillable = true)
    protected Durata espulsioneStato;
    @XmlElement(required = true, nillable = true)
    protected String tipoEspulsioneStato;

    /**
     * Recupera il valore della proprietà permanenzaDomiciliare.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getPermanenzaDomiciliare() {
        return permanenzaDomiciliare;
    }

    /**
     * Imposta il valore della proprietà permanenzaDomiciliare.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setPermanenzaDomiciliare(Durata value) {
        this.permanenzaDomiciliare = value;
    }

    /**
     * Recupera il valore della proprietà lavoroPubblicaUtilita.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getLavoroPubblicaUtilita() {
        return lavoroPubblicaUtilita;
    }

    /**
     * Imposta il valore della proprietà lavoroPubblicaUtilita.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setLavoroPubblicaUtilita(Durata value) {
        this.lavoroPubblicaUtilita = value;
    }

    /**
     * Recupera il valore della proprietà lavoroSostitutivo.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getLavoroSostitutivo() {
        return lavoroSostitutivo;
    }

    /**
     * Imposta il valore della proprietà lavoroSostitutivo.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setLavoroSostitutivo(Durata value) {
        this.lavoroSostitutivo = value;
    }

    /**
     * Recupera il valore della proprietà espulsioneStato.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getEspulsioneStato() {
        return espulsioneStato;
    }

    /**
     * Imposta il valore della proprietà espulsioneStato.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setEspulsioneStato(Durata value) {
        this.espulsioneStato = value;
    }

    /**
     * Recupera il valore della proprietà tipoEspulsioneStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEspulsioneStato() {
        return tipoEspulsioneStato;
    }

    /**
     * Imposta il valore della proprietà tipoEspulsioneStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEspulsioneStato(String value) {
        this.tipoEspulsioneStato = value;
    }

}
