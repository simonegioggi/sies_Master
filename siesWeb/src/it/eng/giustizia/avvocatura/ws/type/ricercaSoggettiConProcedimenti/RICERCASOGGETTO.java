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
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATI_SOGGETTO_INPUT"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATI_SOGGETTO_OUTPUT"/>
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
    "datisoggettooutput"
})
@XmlRootElement(name = "RICERCA_SOGGETTO")
public class RICERCASOGGETTO {

    @XmlElement(name = "DATI_SOGGETTO_INPUT", required = true)
    protected DATISOGGETTOINPUT datisoggettoinput;
    @XmlElement(name = "DATI_SOGGETTO_OUTPUT", required = true)
    protected DATISOGGETTOOUTPUT datisoggettooutput;

    /**
     * elemento contenitore dei dati di input alla ricerca soggetto
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
     * elemento contenitore dei dati di output alla ricerca soggetto
     * 
     * @return
     *     possible object is
     *     {@link DATISOGGETTOOUTPUT }
     *     
     */
    public DATISOGGETTOOUTPUT getDATISOGGETTOOUTPUT() {
        return datisoggettooutput;
    }

    /**
     * Imposta il valore della proprietà datisoggettooutput.
     * 
     * @param value
     *     allowed object is
     *     {@link DATISOGGETTOOUTPUT }
     *     
     */
    public void setDATISOGGETTOOUTPUT(DATISOGGETTOOUTPUT value) {
        this.datisoggettooutput = value;
    }

}
