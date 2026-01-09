//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DATI_INPUT_DETTAGLIO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}OUTPUT_DETTAGLIO_ORDINANZA"/>
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
    "outputdettaglioordinanza"
})
@XmlRootElement(name = "DETTAGLIO_ORDINANZA")
public class DETTAGLIOORDINANZA {

    @XmlElement(name = "DATI_INPUT_DETTAGLIO", required = true)
    protected DATIINPUTDETTAGLIO datiinputdettaglio;
    @XmlElement(name = "OUTPUT_DETTAGLIO_ORDINANZA", required = true)
    protected OUTPUTDETTAGLIOORDINANZA outputdettaglioordinanza;

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
     * Recupera il valore della proprietà outputdettaglioordinanza.
     * 
     * @return
     *     possible object is
     *     {@link OUTPUTDETTAGLIOORDINANZA }
     *     
     */
    public OUTPUTDETTAGLIOORDINANZA getOUTPUTDETTAGLIOORDINANZA() {
        return outputdettaglioordinanza;
    }

    /**
     * Imposta il valore della proprietà outputdettaglioordinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link OUTPUTDETTAGLIOORDINANZA }
     *     
     */
    public void setOUTPUTDETTAGLIOORDINANZA(OUTPUTDETTAGLIOORDINANZA value) {
        this.outputdettaglioordinanza = value;
    }

}
