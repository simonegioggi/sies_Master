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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}DATI_RIEPILOGO_PROCEDIMENTO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}DATI_RINVIO_UDIENZA"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}ERRORE"/>
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
    "datiriepilogoprocedimento",
    "datirinvioudienza",
    "errore"
})
@XmlRootElement(name = "OUTPUT_RINVIO_UDIENZA")
public class OUTPUTRINVIOUDIENZA {

    @XmlElement(name = "DATI_RIEPILOGO_PROCEDIMENTO", required = true)
    protected DATIRIEPILOGOPROCEDIMENTO datiriepilogoprocedimento;
    @XmlElement(name = "DATI_RINVIO_UDIENZA", required = true)
    protected DATIRINVIOUDIENZA datirinvioudienza;
    @XmlElement(name = "ERRORE", required = true)
    protected ERRORE errore;

    /**
     * Recupera il valore della proprietà datiriepilogoprocedimento.
     * 
     * @return
     *     possible object is
     *     {@link DATIRIEPILOGOPROCEDIMENTO }
     *     
     */
    public DATIRIEPILOGOPROCEDIMENTO getDATIRIEPILOGOPROCEDIMENTO() {
        return datiriepilogoprocedimento;
    }

    /**
     * Imposta il valore della proprietà datiriepilogoprocedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIRIEPILOGOPROCEDIMENTO }
     *     
     */
    public void setDATIRIEPILOGOPROCEDIMENTO(DATIRIEPILOGOPROCEDIMENTO value) {
        this.datiriepilogoprocedimento = value;
    }

    /**
     * Recupera il valore della proprietà datirinvioudienza.
     * 
     * @return
     *     possible object is
     *     {@link DATIRINVIOUDIENZA }
     *     
     */
    public DATIRINVIOUDIENZA getDATIRINVIOUDIENZA() {
        return datirinvioudienza;
    }

    /**
     * Imposta il valore della proprietà datirinvioudienza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIRINVIOUDIENZA }
     *     
     */
    public void setDATIRINVIOUDIENZA(DATIRINVIOUDIENZA value) {
        this.datirinvioudienza = value;
    }

    /**
     * Recupera il valore della proprietà errore.
     * 
     * @return
     *     possible object is
     *     {@link ERRORE }
     *     
     */
    public ERRORE getERRORE() {
        return errore;
    }

    /**
     * Imposta il valore della proprietà errore.
     * 
     * @param value
     *     allowed object is
     *     {@link ERRORE }
     *     
     */
    public void setERRORE(ERRORE value) {
        this.errore = value;
    }

}
