
package it.mig.sies.type.foglicomplementari;

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
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Utente"/>
 *         &lt;element name="azione" type="{http://it/mig/sies/type/fogliComplementari}Azione"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Anagrafica"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}FoglioComplementare"/>
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
    "foglioComplementare"
})
@XmlRootElement(name = "RequestData")
public class RequestData {

    @XmlElement(name = "Utente", required = true)
    protected Utente utente;
    @XmlElement(required = true)
    protected Azione azione;
    @XmlElement(name = "Anagrafica", required = true)
    protected Anagrafica anagrafica;
    @XmlElement(name = "FoglioComplementare", required = true)
    protected FoglioComplementare foglioComplementare;

    /**
     * Recupera il valore della proprieta utente.
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
     * Imposta il valore della proprieta utente.
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
     * Recupera il valore della proprieta azione.
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
     * Imposta il valore della proprieta azione.
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
     * Recupera il valore della proprieta anagrafica.
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
     * Imposta il valore della proprieta anagrafica.
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
     * Recupera il valore della proprieta foglioComplementare.
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
     * Imposta il valore della proprieta foglioComplementare.
     * 
     * @param value
     *     allowed object is
     *     {@link FoglioComplementare }
     *     
     */
    public void setFoglioComplementare(FoglioComplementare value) {
        this.foglioComplementare = value;
    }

}
