//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.27 alle 02:09:58 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

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
