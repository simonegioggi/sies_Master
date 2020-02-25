//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.27 alle 02:09:58 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Modella l'oggetto NotificaModel di SIES
 * 			
 * 
 * <p>Classe Java per NOTIFICA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NOTIFICA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mDescrizione" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mDestinatario" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="mDataInvio" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *         &lt;element name="mDataAvvenutaNotifica" type="{http://it/eng/giustizia/avvocatura/ws/type/ricercaSoggettiConProcedimenti}DATA_TYPE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NOTIFICA_TYPE", propOrder = {
    "mDescrizione",
    "mDestinatario",
    "mDataInvio",
    "mDataAvvenutaNotifica"
})
public class NOTIFICATYPE {

    @XmlElement(required = true)
    protected String mDescrizione;
    @XmlElement(required = true)
    protected Object mDestinatario;
    @XmlElement(required = true)
    protected DATATYPE mDataInvio;
    @XmlElement(required = true)
    protected DATATYPE mDataAvvenutaNotifica;

    /**
     * Recupera il valore della proprietà mDescrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDescrizione() {
        return mDescrizione;
    }

    /**
     * Imposta il valore della proprietà mDescrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDescrizione(String value) {
        this.mDescrizione = value;
    }

    /**
     * Recupera il valore della proprietà mDestinatario.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMDestinatario() {
        return mDestinatario;
    }

    /**
     * Imposta il valore della proprietà mDestinatario.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMDestinatario(Object value) {
        this.mDestinatario = value;
    }

    /**
     * Recupera il valore della proprietà mDataInvio.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getMDataInvio() {
        return mDataInvio;
    }

    /**
     * Imposta il valore della proprietà mDataInvio.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setMDataInvio(DATATYPE value) {
        this.mDataInvio = value;
    }

    /**
     * Recupera il valore della proprietà mDataAvvenutaNotifica.
     * 
     * @return
     *     possible object is
     *     {@link DATATYPE }
     *     
     */
    public DATATYPE getMDataAvvenutaNotifica() {
        return mDataAvvenutaNotifica;
    }

    /**
     * Imposta il valore della proprietà mDataAvvenutaNotifica.
     * 
     * @param value
     *     allowed object is
     *     {@link DATATYPE }
     *     
     */
    public void setMDataAvvenutaNotifica(DATATYPE value) {
        this.mDataAvvenutaNotifica = value;
    }

}
