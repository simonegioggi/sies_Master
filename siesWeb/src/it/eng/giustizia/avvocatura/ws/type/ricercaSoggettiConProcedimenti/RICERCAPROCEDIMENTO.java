//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATI_PROCEDIMENTO_INPUT"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATI_PROCEDIMENTO_OUTPUT"/>
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
    "datiprocedimentoinput",
    "datiprocedimentooutput"
})
@XmlRootElement(name = "RICERCA_PROCEDIMENTO")
public class RICERCAPROCEDIMENTO {

    @XmlElement(name = "DATI_PROCEDIMENTO_INPUT", required = true)
    protected DATIPROCEDIMENTOINPUT datiprocedimentoinput;
    @XmlElement(name = "DATI_PROCEDIMENTO_OUTPUT", required = true)
    protected DATIPROCEDIMENTOOUTPUT datiprocedimentooutput;

    /**
     * Recupera il valore della proprietà datiprocedimentoinput.
     * 
     * @return
     *     possible object is
     *     {@link DATIPROCEDIMENTOINPUT }
     *     
     */
    public DATIPROCEDIMENTOINPUT getDATIPROCEDIMENTOINPUT() {
        return datiprocedimentoinput;
    }

    /**
     * Imposta il valore della proprietà datiprocedimentoinput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIPROCEDIMENTOINPUT }
     *     
     */
    public void setDATIPROCEDIMENTOINPUT(DATIPROCEDIMENTOINPUT value) {
        this.datiprocedimentoinput = value;
    }

    /**
     * Recupera il valore della proprietà datiprocedimentooutput.
     * 
     * @return
     *     possible object is
     *     {@link DATIPROCEDIMENTOOUTPUT }
     *     
     */
    public DATIPROCEDIMENTOOUTPUT getDATIPROCEDIMENTOOUTPUT() {
        return datiprocedimentooutput;
    }

    /**
     * Imposta il valore della proprietà datiprocedimentooutput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIPROCEDIMENTOOUTPUT }
     *     
     */
    public void setDATIPROCEDIMENTOOUTPUT(DATIPROCEDIMENTOOUTPUT value) {
        this.datiprocedimentooutput = value;
    }

}
