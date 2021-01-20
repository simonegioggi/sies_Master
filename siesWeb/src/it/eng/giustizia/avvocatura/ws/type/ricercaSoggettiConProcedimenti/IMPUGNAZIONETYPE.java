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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per IMPUGNAZIONE_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="IMPUGNAZIONE_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descrTipoImpugnazione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataRicorso" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="flagTipo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IMPUGNAZIONE_TYPE", propOrder = {
    "descrTipoImpugnazione",
    "dataRicorso",
    "flagTipo"
})
public class IMPUGNAZIONETYPE {

    @XmlElement(required = true)
    protected String descrTipoImpugnazione;
    @XmlElement(required = true)
    protected DATATYPE dataRicorso;
    @XmlElement(required = true)
    protected String flagTipo;

    /**
     * Recupera il valore della proprietà descrTipoImpugnazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoImpugnazione() {
        return descrTipoImpugnazione;
    }

    /**
     * Imposta il valore della proprietà descrTipoImpugnazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoImpugnazione(String value) {
        this.descrTipoImpugnazione = value;
    }

    /**
     * Recupera il valore della proprietà dataRicorso.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getDataRicorso() {
        return dataRicorso;
    }

    /**
     * Imposta il valore della proprietà dataRicorso.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setDataRicorso(DATATYPE value) {
        this.dataRicorso = value;
    }

    /**
     * Recupera il valore della proprietà flagTipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagTipo() {
        return flagTipo;
    }

    /**
     * Imposta il valore della proprietà flagTipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagTipo(String value) {
        this.flagTipo = value;
    }

}
