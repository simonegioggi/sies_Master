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
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per AUTORITA_ESTERNA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="AUTORITA_ESTERNA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mDescrTipoAutorita" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mDescrSede" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AUTORITA_ESTERNA_TYPE", propOrder = {
    "mDescrTipoAutorita",
    "mDescrSede"
})
public class AUTORITAESTERNATYPE {

    @XmlElement(required = true)
    protected String mDescrTipoAutorita;
    @XmlElement(required = true)
    protected String mDescrSede;

    /**
     * Recupera il valore della proprietà mDescrTipoAutorita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDescrTipoAutorita() {
        return mDescrTipoAutorita;
    }

    /**
     * Imposta il valore della proprietà mDescrTipoAutorita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDescrTipoAutorita(String value) {
        this.mDescrTipoAutorita = value;
    }

    /**
     * Recupera il valore della proprietà mDescrSede.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDescrSede() {
        return mDescrSede;
    }

    /**
     * Imposta il valore della proprietà mDescrSede.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDescrSede(String value) {
        this.mDescrSede = value;
    }

}
