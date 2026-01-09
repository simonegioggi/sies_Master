//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.26 alle 04:01:02 PM CEST 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Utente"/>
 *         &lt;element name="azione" type="{http://it/mig/sies/type/fogliComplementari}Azione"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Anagrafica"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}FoglioComplementare"/>
 *         &lt;element name="azioneCumulo" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "utente",
    "azione",
    "anagrafica",
    "foglioComplementare",
    "azioneCumulo"
})
@XmlRootElement(name = "RequestData")
public class RequestData {

    @XmlElement(name = "Utente", required = true)
    protected Utente utente;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Azione azione;
    @XmlElement(name = "Anagrafica", required = true)
    protected Anagrafica anagrafica;
    @XmlElement(name = "FoglioComplementare", required = true)
    protected FoglioComplementare foglioComplementare;
    @XmlElement(required = true, nillable = true)
    protected String azioneCumulo;

    /**
     * Recupera il valore della proprietï¿½ utente.
     * 
     * @return
     *     possible object is
     *     {@link Utente }
     *     
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Imposta il valore della proprietï¿½ utente.
     * 
     * @param value
     *     allowed object is
     *     {@link Utente }
     *     
     */
    public void setUtente(Utente value) {
        this.utente = value;
    }

    /**
     * Recupera il valore della proprietï¿½ azione.
     * 
     * @return
     *     possible object is
     *     {@link Azione }
     *     
     */
    public Azione getAzione() {
        return azione;
    }

    /**
     * Imposta il valore della proprietï¿½ azione.
     * 
     * @param value
     *     allowed object is
     *     {@link Azione }
     *     
     */
    public void setAzione(Azione value) {
        this.azione = value;
    }

    /**
     * Recupera il valore della proprietï¿½ anagrafica.
     * 
     * @return
     *     possible object is
     *     {@link Anagrafica }
     *     
     */
    public Anagrafica getAnagrafica() {
        return anagrafica;
    }

    /**
     * Imposta il valore della proprietï¿½ anagrafica.
     * 
     * @param value
     *     allowed object is
     *     {@link Anagrafica }
     *     
     */
    public void setAnagrafica(Anagrafica value) {
        this.anagrafica = value;
    }

    /**
     * Recupera il valore della proprietï¿½ foglioComplementare.
     * 
     * @return
     *     possible object is
     *     {@link FoglioComplementare }
     *     
     */
    public FoglioComplementare getFoglioComplementare() {
        return foglioComplementare;
    }

    /**
     * Imposta il valore della proprietï¿½ foglioComplementare.
     * 
     * @param value
     *     allowed object is
     *     {@link FoglioComplementare }
     *     
     */
    public void setFoglioComplementare(FoglioComplementare value) {
        this.foglioComplementare = value;
    }

    /**
     * Recupera il valore della proprietï¿½ azioneCumulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAzioneCumulo() {
        return azioneCumulo;
    }

    /**
     * Imposta il valore della proprietï¿½ azioneCumulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAzioneCumulo(String value) {
        this.azioneCumulo = value;
    }

}
