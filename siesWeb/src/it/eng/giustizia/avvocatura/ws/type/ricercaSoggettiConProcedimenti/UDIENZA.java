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
 *         &lt;element name="dataUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="dataCameraConsiglio" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="flagRinviata" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrPresidente" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "dataUdienza",
    "dataCameraConsiglio",
    "flagRinviata",
    "descrPresidente"
})
@XmlRootElement(name = "UDIENZA")
public class UDIENZA {

    @XmlElement(required = true)
    protected DATATYPE dataUdienza;
    @XmlElement(required = true)
    protected DATATYPE dataCameraConsiglio;
    @XmlElement(required = true)
    protected String flagRinviata;
    @XmlElement(required = true)
    protected String descrPresidente;

    /**
     * Recupera il valore della proprietà dataUdienza.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataUdienza() {
        return dataUdienza;
    }

    /**
     * Imposta il valore della proprietà dataUdienza.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataUdienza(DATATYPE value) {
        this.dataUdienza = value;
    }

    /**
     * Recupera il valore della proprietà dataCameraConsiglio.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataCameraConsiglio() {
        return dataCameraConsiglio;
    }

    /**
     * Imposta il valore della proprietà dataCameraConsiglio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataCameraConsiglio(DATATYPE value) {
        this.dataCameraConsiglio = value;
    }

    /**
     * Recupera il valore della proprietà flagRinviata.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagRinviata() {
        return flagRinviata;
    }

    /**
     * Imposta il valore della proprietà flagRinviata.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagRinviata(String value) {
        this.flagRinviata = value;
    }

    /**
     * Recupera il valore della proprietà descrPresidente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrPresidente() {
        return descrPresidente;
    }

    /**
     * Imposta il valore della proprietà descrPresidente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrPresidente(String value) {
        this.descrPresidente = value;
    }

}
