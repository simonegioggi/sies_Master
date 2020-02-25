//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 03:51:09 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaAvvisi}DATI_AVVISO_INPUT"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaAvvisi}DATI_AVVISO_OUTPUT"/>
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
    "datiavvisoinput",
    "datiavvisooutput"
})
@XmlRootElement(name = "RICERCA_AVVISO")
public class RICERCAAVVISO {

    @XmlElement(name = "DATI_AVVISO_INPUT", required = true)
    protected DATIAVVISOINPUT datiavvisoinput;
    @XmlElement(name = "DATI_AVVISO_OUTPUT", required = true)
    protected DATIAVVISOOUTPUT datiavvisooutput;

    /**
     * Recupera il valore della proprietà datiavvisoinput.
     * 
     * @return
     *     possible object is
     *     {@link DATIAVVISOINPUT }
     *     
     */
    public DATIAVVISOINPUT getDATIAVVISOINPUT() {
        return datiavvisoinput;
    }

    /**
     * Imposta il valore della proprietà datiavvisoinput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIAVVISOINPUT }
     *     
     */
    public void setDATIAVVISOINPUT(DATIAVVISOINPUT value) {
        this.datiavvisoinput = value;
    }

    /**
     * Recupera il valore della proprietà datiavvisooutput.
     * 
     * @return
     *     possible object is
     *     {@link DATIAVVISOOUTPUT }
     *     
     */
    public DATIAVVISOOUTPUT getDATIAVVISOOUTPUT() {
        return datiavvisooutput;
    }

    /**
     * Imposta il valore della proprietà datiavvisooutput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIAVVISOOUTPUT }
     *     
     */
    public void setDATIAVVISOOUTPUT(DATIAVVISOOUTPUT value) {
        this.datiavvisooutput = value;
    }

}
