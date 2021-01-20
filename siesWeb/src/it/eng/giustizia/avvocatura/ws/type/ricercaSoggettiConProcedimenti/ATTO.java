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
 *         &lt;element name="dataRichiesta" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrTipoAtto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataArrivoCancelleria" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="descrTipoMittenteAtto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrSedeMittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrMittente" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "dataRichiesta",
    "descrTipoAtto",
    "dataArrivoCancelleria",
    "descrTipoMittenteAtto",
    "descrSedeMittente",
    "descrMittente"
})
@XmlRootElement(name = "ATTO")
public class ATTO {

    @XmlElement(required = true)
    protected DATATYPE dataRichiesta;
    @XmlElement(required = true)
    protected String descrTipoAtto;
    @XmlElement(required = true)
    protected DATATYPE dataArrivoCancelleria;
    @XmlElement(required = true)
    protected String descrTipoMittenteAtto;
    @XmlElement(required = true)
    protected String descrSedeMittente;
    @XmlElement(required = true)
    protected String descrMittente;

    /**
     * Recupera il valore della proprietà dataRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataRichiesta() {
        return dataRichiesta;
    }

    /**
     * Imposta il valore della proprietà dataRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataRichiesta(DATATYPE value) {
        this.dataRichiesta = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoAtto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoAtto() {
        return descrTipoAtto;
    }

    /**
     * Imposta il valore della proprietà descrTipoAtto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoAtto(String value) {
        this.descrTipoAtto = value;
    }

    /**
     * Recupera il valore della proprietà dataArrivoCancelleria.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataArrivoCancelleria() {
        return dataArrivoCancelleria;
    }

    /**
     * Imposta il valore della proprietà dataArrivoCancelleria.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataArrivoCancelleria(DATATYPE value) {
        this.dataArrivoCancelleria = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoMittenteAtto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoMittenteAtto() {
        return descrTipoMittenteAtto;
    }

    /**
     * Imposta il valore della proprietà descrTipoMittenteAtto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoMittenteAtto(String value) {
        this.descrTipoMittenteAtto = value;
    }

    /**
     * Recupera il valore della proprietà descrSedeMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrSedeMittente() {
        return descrSedeMittente;
    }

    /**
     * Imposta il valore della proprietà descrSedeMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrSedeMittente(String value) {
        this.descrSedeMittente = value;
    }

    /**
     * Recupera il valore della proprietà descrMittente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrMittente() {
        return descrMittente;
    }

    /**
     * Imposta il valore della proprietà descrMittente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrMittente(String value) {
        this.descrMittente = value;
    }

}
