//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.22 alle 01:43:35 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioRinvioUdienza;

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
 *         &lt;element name="elencoOggetti" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}TENORE_TYPE" maxOccurs="unbounded"/>
 *         &lt;element name="dataEmissione" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}DATA_TYPE"/>
 *         &lt;element name="luogoSvolgimentoUdienza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioRinvioUdienza}DATA_TYPE"/>
 *         &lt;element name="dateUdienzePrecedenti" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "elencoOggetti",
    "dataEmissione",
    "luogoSvolgimentoUdienza",
    "dataUdienza",
    "dateUdienzePrecedenti"
})
@XmlRootElement(name = "DATI_RINVIO_UDIENZA")
public class DATIRINVIOUDIENZA {

    @XmlElement(required = true, nillable = true)
    protected List<TENORETYPE> elencoOggetti;
    @XmlElement(required = true)
    protected DATATYPE dataEmissione;
    @XmlElement(required = true, nillable = true)
    protected String luogoSvolgimentoUdienza;
    @XmlElement(required = true)
    protected DATATYPE dataUdienza;
    @XmlElement(required = true, nillable = true)
    protected String dateUdienzePrecedenti;

    /**
     * Gets the value of the elencoOggetti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoOggetti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoOggetti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TENORETYPE }
     * 
     * 
     */
    public List<TENORETYPE> getElencoOggetti() {
        if (elencoOggetti == null) {
            elencoOggetti = new ArrayList<TENORETYPE>();
        }
        return this.elencoOggetti;
    }

    /**
     * Recupera il valore della proprietà dataEmissione.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataEmissione() {
        return dataEmissione;
    }

    /**
     * Imposta il valore della proprietà dataEmissione.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataEmissione(DATATYPE value) {
        this.dataEmissione = value;
    }

    /**
     * Recupera il valore della proprietà luogoSvolgimentoUdienza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLuogoSvolgimentoUdienza() {
        return luogoSvolgimentoUdienza;
    }

    /**
     * Imposta il valore della proprietà luogoSvolgimentoUdienza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLuogoSvolgimentoUdienza(String value) {
        this.luogoSvolgimentoUdienza = value;
    }

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
     * Recupera il valore della proprietà dateUdienzePrecedenti.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateUdienzePrecedenti() {
        return dateUdienzePrecedenti;
    }

    /**
     * Imposta il valore della proprietà dateUdienzePrecedenti.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateUdienzePrecedenti(String value) {
        this.dateUdienzePrecedenti = value;
    }

}
