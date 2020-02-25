//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.26 alle 11:09:52 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per MISURA_SICUREZZA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="MISURA_SICUREZZA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codiceEsitoEvento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrNatura" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descrTipoMisura" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="durataMisura" type="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioOrdinanza}DURATA_TYPE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MISURA_SICUREZZA_TYPE", propOrder = {
    "codiceEsitoEvento",
    "descrNatura",
    "descrTipoMisura",
    "durataMisura"
})
public class MISURASICUREZZATYPE {

    @XmlElement(required = true)
    protected String codiceEsitoEvento;
    @XmlElement(required = true)
    protected String descrNatura;
    @XmlElement(required = true)
    protected String descrTipoMisura;
    @XmlElement(required = true)
    protected DURATATYPE durataMisura;

    /**
     * Recupera il valore della proprietà codiceEsitoEvento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceEsitoEvento() {
        return codiceEsitoEvento;
    }

    /**
     * Imposta il valore della proprietà codiceEsitoEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceEsitoEvento(String value) {
        this.codiceEsitoEvento = value;
    }

    /**
     * Recupera il valore della proprietà descrNatura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrNatura() {
        return descrNatura;
    }

    /**
     * Imposta il valore della proprietà descrNatura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrNatura(String value) {
        this.descrNatura = value;
    }

    /**
     * Recupera il valore della proprietà descrTipoMisura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrTipoMisura() {
        return descrTipoMisura;
    }

    /**
     * Imposta il valore della proprietà descrTipoMisura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrTipoMisura(String value) {
        this.descrTipoMisura = value;
    }

    /**
     * Recupera il valore della proprietà durataMisura.
     * 
     * @return
     *     possible object is
     *     {@link DURATATYPE }
     *     
     */
    public DURATATYPE getDurataMisura() {
        return durataMisura;
    }

    /**
     * Imposta il valore della proprietà durataMisura.
     * 
     * @param value
     *     allowed object is
     *     {@link DURATATYPE }
     *     
     */
    public void setDurataMisura(DURATATYPE value) {
        this.durataMisura = value;
    }

}
