//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.05 alle 02:30:32 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.richiestaStampa;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element name="idFascicoloSius" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ALLEGATO_RTF" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/richiestaStampa}ERRORE"/>
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
    "idFascicoloSius",
    "allegatortf",
    "errore"
})
@XmlRootElement(name = "DATI_STAMPA_OUTPUT")
public class DATISTAMPAOUTPUT {

    @XmlElement(required = true, nillable = true)
    protected BigInteger idFascicoloSius;
    @XmlElement(name = "ALLEGATO_RTF", required = true, nillable = true)
    protected byte[] allegatortf;
    @XmlElement(name = "ERRORE", required = true)
    protected ERRORE errore;

    /**
     * Recupera il valore della proprietà idFascicoloSius.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIdFascicoloSius() {
        return idFascicoloSius;
    }

    /**
     * Imposta il valore della proprietà idFascicoloSius.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIdFascicoloSius(BigInteger value) {
        this.idFascicoloSius = value;
    }

    /**
     * Recupera il valore della proprietà allegatortf.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getALLEGATORTF() {
        return allegatortf;
    }

    /**
     * Imposta il valore della proprietà allegatortf.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setALLEGATORTF(byte[] value) {
        this.allegatortf = value;
    }

    /**
     * Recupera il valore della proprietà errore.
     * 
     * @return
     *     possible object is
     *     {@link ERRORE }
     *     
     */
    public ERRORE getERRORE() {
        return errore;
    }

    /**
     * Imposta il valore della proprietà errore.
     * 
     * @param value
     *     allowed object is
     *     {@link ERRORE }
     *     
     */
    public void setERRORE(ERRORE value) {
        this.errore = value;
    }

}
