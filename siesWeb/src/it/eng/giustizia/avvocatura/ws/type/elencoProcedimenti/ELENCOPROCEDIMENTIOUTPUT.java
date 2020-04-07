//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:41:34 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="elencoProcedimenti" type="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}DATI_PROCEDIMENTO_TYPE" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/elencoProcedimenti}ERRORE"/>
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
    "elencoProcedimenti",
    "errore"
})
@XmlRootElement(name = "ELENCO_PROCEDIMENTI_OUTPUT")
public class ELENCOPROCEDIMENTIOUTPUT {

    @XmlElement(required = true)
    protected List<DATIPROCEDIMENTOTYPE> elencoProcedimenti;
    @XmlElement(name = "ERRORE", required = true)
    protected ERRORE errore;

    /**
     * Gets the value of the elencoProcedimenti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoProcedimenti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoProcedimenti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DATIPROCEDIMENTOTYPE }
     * 
     * 
     */
    public List<DATIPROCEDIMENTOTYPE> getElencoProcedimenti() {
        if (elencoProcedimenti == null) {
            elencoProcedimenti = new ArrayList<DATIPROCEDIMENTOTYPE>();
        }
        return this.elencoProcedimenti;
    }

    /**
     * contenitore di errori
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
