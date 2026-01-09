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
 * <p>Classe Java per MOVIMENTI_UDIENZA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MOVIMENTI_UDIENZA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataUdienza" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="tipoOperazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataInserimento" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="dataModifica" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MOVIMENTI_UDIENZA_TYPE", propOrder = {
    "dataUdienza",
    "tipoOperazione",
    "dataInserimento",
    "dataModifica"
})
public class MOVIMENTIUDIENZATYPE {

    @XmlElement(required = true)
    protected DATATYPE dataUdienza;
    @XmlElement(required = true)
    protected String tipoOperazione;
    @XmlElement(required = true)
    protected DATATYPE dataInserimento;
    @XmlElement(required = true)
    protected DATATYPE dataModifica;

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
     * Recupera il valore della proprietà tipoOperazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOperazione() {
        return tipoOperazione;
    }

    /**
     * Imposta il valore della proprietà tipoOperazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOperazione(String value) {
        this.tipoOperazione = value;
    }

    /**
     * Recupera il valore della proprietà dataInserimento.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataInserimento() {
        return dataInserimento;
    }

    /**
     * Imposta il valore della proprietà dataInserimento.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataInserimento(DATATYPE value) {
        this.dataInserimento = value;
    }

    /**
     * Recupera il valore della proprietà dataModifica.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataModifica() {
        return dataModifica;
    }

    /**
     * Imposta il valore della proprietà dataModifica.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataModifica(DATATYPE value) {
        this.dataModifica = value;
    }

}
