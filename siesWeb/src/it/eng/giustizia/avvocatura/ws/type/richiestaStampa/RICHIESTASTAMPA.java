//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.05 alle 02:30:32 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.richiestaStampa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/richiestaStampa}DATI_STAMPA_INPUT"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/richiestaStampa}DATI_STAMPA_OUTPUT"/>
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
    "datistampainput",
    "datistampaoutput"
})
@XmlRootElement(name = "RICHIESTA_STAMPA")
public class RICHIESTASTAMPA {

    @XmlElement(name = "DATI_STAMPA_INPUT", required = true)
    protected DATISTAMPAINPUT datistampainput;
    @XmlElement(name = "DATI_STAMPA_OUTPUT", required = true)
    protected DATISTAMPAOUTPUT datistampaoutput;

    /**
     * Recupera il valore della proprietà datistampainput.
     * 
     * @return
     *     possible object is
     *     {@link DATISTAMPAINPUT }
     *     
     */
    public DATISTAMPAINPUT getDATISTAMPAINPUT() {
        return datistampainput;
    }

    /**
     * Imposta il valore della proprietà datistampainput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATISTAMPAINPUT }
     *     
     */
    public void setDATISTAMPAINPUT(DATISTAMPAINPUT value) {
        this.datistampainput = value;
    }

    /**
     * Recupera il valore della proprietà datistampaoutput.
     * 
     * @return
     *     possible object is
     *     {@link DATISTAMPAOUTPUT }
     *     
     */
    public DATISTAMPAOUTPUT getDATISTAMPAOUTPUT() {
        return datistampaoutput;
    }

    /**
     * Imposta il valore della proprietà datistampaoutput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATISTAMPAOUTPUT }
     *     
     */
    public void setDATISTAMPAOUTPUT(DATISTAMPAOUTPUT value) {
        this.datistampaoutput = value;
    }

}
