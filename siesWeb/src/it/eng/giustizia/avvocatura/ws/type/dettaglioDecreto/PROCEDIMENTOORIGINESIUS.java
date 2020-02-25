//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.28 alle 01:14:48 PM CEST 
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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_RIEPILOGO_PROCEDIMENTO"/>
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
    "datiriepilogoprocedimento"
})
@XmlRootElement(name = "PROCEDIMENTO_ORIGINE_SIUS")
public class PROCEDIMENTOORIGINESIUS {

    @XmlElement(name = "DATI_RIEPILOGO_PROCEDIMENTO", required = true)
    protected DATIRIEPILOGOPROCEDIMENTO datiriepilogoprocedimento;

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

}
