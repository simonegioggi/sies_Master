//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:41:34 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti;

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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}DATI_SOGGETTO_INPUT"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}ELENCO_PROCEDIMENTI_OUTPUT"/>
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
    "datisoggettoinput",
    "elencoprocedimentioutput"
})
@XmlRootElement(name = "ELENCO_PROCEDIMENTI")
public class ELENCOPROCEDIMENTI {

    @XmlElement(name = "DATI_SOGGETTO_INPUT", required = true)
    protected DATISOGGETTOINPUT datisoggettoinput;
    @XmlElement(name = "ELENCO_PROCEDIMENTI_OUTPUT", required = true)
    protected ELENCOPROCEDIMENTIOUTPUT elencoprocedimentioutput;

    /**
     * elemento contentitore dei dati di input per la ricerca dell'elenco dei procedimenti
     * 
     * @return
     *     possible object is
     *     {@link DATISOGGETTOINPUT }
     *     
     */
    public DATISOGGETTOINPUT getDATISOGGETTOINPUT() {
        return datisoggettoinput;
    }

    /**
     * Imposta il valore della proprietà datisoggettoinput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATISOGGETTOINPUT }
     *     
     */
    public void setDATISOGGETTOINPUT(DATISOGGETTOINPUT value) {
        this.datisoggettoinput = value;
    }

    /**
     * elemento contentitore dei dati di outup della ricerca dell'elenco dei procedimenti
     * 
     * @return
     *     possible object is
     *     {@link ELENCOPROCEDIMENTIOUTPUT }
     *     
     */
    public ELENCOPROCEDIMENTIOUTPUT getELENCOPROCEDIMENTIOUTPUT() {
        return elencoprocedimentioutput;
    }

    /**
     * Imposta il valore della proprietà elencoprocedimentioutput.
     * 
     * @param value
     *     allowed object is
     *     {@link ELENCOPROCEDIMENTIOUTPUT }
     *     
     */
    public void setELENCOPROCEDIMENTIOUTPUT(ELENCOPROCEDIMENTIOUTPUT value) {
        this.elencoprocedimentioutput = value;
    }

}
