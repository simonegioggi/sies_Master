//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.05 alle 02:30:32 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.richiestaStampa;

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
 *         &lt;element name="CODICE_ERRORE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DESCR_ERRORE" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "codiceerrore",
    "descrerrore"
})
@XmlRootElement(name = "ERRORE")
public class ERRORE {

    @XmlElement(name = "CODICE_ERRORE", required = true)
    protected String codiceerrore;
    @XmlElement(name = "DESCR_ERRORE", required = true)
    protected String descrerrore;

    /**
     * Recupera il valore della proprietà codiceerrore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODICEERRORE() {
        return codiceerrore;
    }

    /**
     * Imposta il valore della proprietà codiceerrore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODICEERRORE(String value) {
        this.codiceerrore = value;
    }

    /**
     * Recupera il valore della proprietà descrerrore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRERRORE() {
        return descrerrore;
    }

    /**
     * Imposta il valore della proprietà descrerrore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRERRORE(String value) {
        this.descrerrore = value;
    }

}
