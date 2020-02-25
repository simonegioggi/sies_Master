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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_INPUT_DETTAGLIO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}OUTPUT_DETTAGLIO_DECRETO"/>
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
    "outputdettagliodecreto"
})
@XmlRootElement(name = "DETTAGLIO_DECRETO")
public class DETTAGLIODECRETO {

    @XmlElement(name = "DATI_INPUT_DETTAGLIO", required = true)
    protected DATIINPUTDETTAGLIO datiinputdettaglio;
    @XmlElement(name = "OUTPUT_DETTAGLIO_DECRETO", required = true)
    protected OUTPUTDETTAGLIODECRETO outputdettagliodecreto;

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
     * Recupera il valore della proprietà outputdettagliodecreto.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIODECRETO }
     *     
     */
    public OUTPUTDETTAGLIODECRETO getOUTPUTDETTAGLIODECRETO() {
        return outputdettagliodecreto;
    }

    /**
     * Imposta il valore della proprietà outputdettagliodecreto.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIODECRETO }
     *     
     */
    public void setOUTPUTDETTAGLIODECRETO(OUTPUTDETTAGLIODECRETO value) {
        this.outputdettagliodecreto = value;
    }

}
