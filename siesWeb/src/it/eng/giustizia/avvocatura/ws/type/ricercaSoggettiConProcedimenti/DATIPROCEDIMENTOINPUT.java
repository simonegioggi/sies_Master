//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element name="annoProcedimento" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="numeroProcedimento" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="codiceFiscaleAvvocato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codDistretto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "annoProcedimento",
    "numeroProcedimento",
    "codiceFiscaleAvvocato",
    "codTipoUfficio",
    "codDistretto",
    "codUfficio"
})
@XmlRootElement(name = "DATI_PROCEDIMENTO_INPUT")
public class DATIPROCEDIMENTOINPUT {

    @XmlElement(required = true)
    protected BigInteger annoProcedimento;
    @XmlElement(required = true)
    protected BigInteger numeroProcedimento;
    @XmlElement(required = true)
    protected String codiceFiscaleAvvocato;
    @XmlElement(required = true)
    protected String codTipoUfficio;
    @XmlElement(required = true)
    protected String codDistretto;
    @XmlElement(required = true)
    protected String codUfficio;

    /**
     * Recupera il valore della proprietà annoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnnoProcedimento() {
        return annoProcedimento;
    }

    /**
     * Imposta il valore della proprietà annoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnnoProcedimento(BigInteger value) {
        this.annoProcedimento = value;
    }

    /**
     * Recupera il valore della proprietà numeroProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroProcedimento() {
        return numeroProcedimento;
    }

    /**
     * Imposta il valore della proprietà numeroProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroProcedimento(BigInteger value) {
        this.numeroProcedimento = value;
    }

    /**
     * Recupera il valore della proprietà codiceFiscaleAvvocato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscaleAvvocato() {
        return codiceFiscaleAvvocato;
    }

    /**
     * Imposta il valore della proprietà codiceFiscaleAvvocato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscaleAvvocato(String value) {
        this.codiceFiscaleAvvocato = value;
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

    /**
     * Recupera il valore della proprietà codDistretto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDistretto() {
        return codDistretto;
    }

    /**
     * Imposta il valore della proprietà codDistretto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDistretto(String value) {
        this.codDistretto = value;
    }

    /**
     * Recupera il valore della proprietà codUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUfficio() {
        return codUfficio;
    }

    /**
     * Imposta il valore della proprietà codUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUfficio(String value) {
        this.codUfficio = value;
    }

}
