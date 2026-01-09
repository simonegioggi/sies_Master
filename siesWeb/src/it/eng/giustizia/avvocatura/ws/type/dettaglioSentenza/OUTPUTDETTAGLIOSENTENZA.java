//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 10:47:00 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}DATI_RIEPILOGO_PROCEDIMENTO"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}DATI_SENTENZA"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza}ERRORE"/>
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
    "datisentenza",
    "errore"
})
@XmlRootElement(name = "OUTPUT_DETTAGLIO_SENTENZA")
public class OUTPUTDETTAGLIOSENTENZA {

    @XmlElement(name = "DATI_RIEPILOGO_PROCEDIMENTO", required = true)
    protected DATIRIEPILOGOPROCEDIMENTO datiriepilogoprocedimento;
    @XmlElement(name = "DATI_SENTENZA", required = true)
    protected DATISENTENZA datisentenza;
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
     * Recupera il valore della proprietà datisentenza.
     * 
     * @return
     *     possible object is
     *     {@link DATISENTENZA }
     *     
     */
    public DATISENTENZA getDATISENTENZA() {
        return datisentenza;
    }

    /**
     * Imposta il valore della proprietà datisentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATISENTENZA }
     *     
     */
    public void setDATISENTENZA(DATISENTENZA value) {
        this.datisentenza = value;
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
