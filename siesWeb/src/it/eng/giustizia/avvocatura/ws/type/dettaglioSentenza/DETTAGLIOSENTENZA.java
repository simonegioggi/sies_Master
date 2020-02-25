//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 10:47:00 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}DATI_INPUT_DETTAGLIO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}OUTPUT_DETTAGLIO_SENTENZA"/>
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
    "datiinputdettaglio",
    "outputdettagliosentenza"
})
@XmlRootElement(name = "DETTAGLIO_SENTENZA")
public class DETTAGLIOSENTENZA {

    @XmlElement(name = "DATI_INPUT_DETTAGLIO", required = true)
    protected DATIINPUTDETTAGLIO datiinputdettaglio;
    @XmlElement(name = "OUTPUT_DETTAGLIO_SENTENZA", required = true)
    protected OUTPUTDETTAGLIOSENTENZA outputdettagliosentenza;

    /**
     * Recupera il valore della proprietà datiinputdettaglio.
     * 
     * @return
     *     possible object is
     *     {@link DATIINPUTDETTAGLIO }
     *     
     */
    public DATIINPUTDETTAGLIO getDATIINPUTDETTAGLIO() {
        return datiinputdettaglio;
    }

    /**
     * Imposta il valore della proprietà datiinputdettaglio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIINPUTDETTAGLIO }
     *     
     */
    public void setDATIINPUTDETTAGLIO(DATIINPUTDETTAGLIO value) {
        this.datiinputdettaglio = value;
    }

    /**
     * Recupera il valore della proprietà outputdettagliosentenza.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOSENTENZA }
     *     
     */
    public OUTPUTDETTAGLIOSENTENZA getOUTPUTDETTAGLIOSENTENZA() {
        return outputdettagliosentenza;
    }

    /**
     * Imposta il valore della proprietà outputdettagliosentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOSENTENZA }
     *     
     */
    public void setOUTPUTDETTAGLIOSENTENZA(OUTPUTDETTAGLIOSENTENZA value) {
        this.outputdettagliosentenza = value;
    }

}
