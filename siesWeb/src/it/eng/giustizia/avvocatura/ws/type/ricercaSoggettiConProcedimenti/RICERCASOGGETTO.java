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
     * Recupera il valore della proprietà datisoggettoinput.
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
     * Recupera il valore della proprietà datisoggettooutput.
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
