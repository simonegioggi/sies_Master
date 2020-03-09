//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:42:15 PM CET 
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
 *         &lt;element name="mCognome" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="mNome" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
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
    "mCognome",
    "mNome"
})
@XmlRootElement(name = "MAGISTRATO")
public class MAGISTRATO {

    @XmlElement(required = true)
    protected Object mCognome;
    @XmlElement(required = true)
    protected Object mNome;

    /**
     * Recupera il valore della proprietà mCognome.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMCognome() {
        return mCognome;
    }

    /**
     * Imposta il valore della proprietà mCognome.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMCognome(Object value) {
        this.mCognome = value;
    }

    /**
     * Recupera il valore della proprietà mNome.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMNome() {
        return mNome;
    }

    /**
     * Imposta il valore della proprietà mNome.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMNome(Object value) {
        this.mNome = value;
    }

}
