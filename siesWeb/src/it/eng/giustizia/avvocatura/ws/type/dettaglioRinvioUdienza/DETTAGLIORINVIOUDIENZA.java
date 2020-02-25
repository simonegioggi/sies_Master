//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.22 alle 01:43:35 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}DATI_INPUT_DETTAGLIO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}OUTPUT_RINVIO_UDIENZA"/>
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
    "outputrinvioudienza"
})
@XmlRootElement(name = "DETTAGLIO_RINVIO_UDIENZA")
public class DETTAGLIORINVIOUDIENZA {

    @XmlElement(name = "DATI_INPUT_DETTAGLIO", required = true)
    protected DATIINPUTDETTAGLIO datiinputdettaglio;
    @XmlElement(name = "OUTPUT_RINVIO_UDIENZA", required = true)
    protected OUTPUTRINVIOUDIENZA outputrinvioudienza;

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
     * Recupera il valore della proprietà outputrinvioudienza.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTRINVIOUDIENZA }
     *     
     */
    public OUTPUTRINVIOUDIENZA getOUTPUTRINVIOUDIENZA() {
        return outputrinvioudienza;
    }

    /**
     * Imposta il valore della proprietà outputrinvioudienza.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTRINVIOUDIENZA }
     *     
     */
    public void setOUTPUTRINVIOUDIENZA(OUTPUTRINVIOUDIENZA value) {
        this.outputrinvioudienza = value;
    }

}
