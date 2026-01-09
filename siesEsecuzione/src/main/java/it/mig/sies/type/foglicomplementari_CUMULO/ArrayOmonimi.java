//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.26 alle 04:01:02 PM CEST 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

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
 *         &lt;element name="Omonimo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Anagrafica"/>
 *                   &lt;element name="CertificatoControllo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "omonimo"
})
@XmlRootElement(name = "ArrayOmonimi")
public class ArrayOmonimi {

    @XmlElement(name = "Omonimo")
    protected List<ArrayOmonimi.Omonimo> omonimo;

    /**
     * Gets the value of the omonimo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the omonimo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOmonimo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOmonimi.Omonimo }
     * 
     * 
     */
    public List<ArrayOmonimi.Omonimo> getOmonimo() {
        if (omonimo == null) {
            omonimo = new ArrayList<ArrayOmonimi.Omonimo>();
        }
        return this.omonimo;
    }


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
     *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Anagrafica"/>
     *         &lt;element name="CertificatoControllo" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
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
        "anagrafica",
        "certificatoControllo"
    })
    public static class Omonimo {

        @XmlElement(name = "Anagrafica", required = true)
        protected Anagrafica anagrafica;
        @XmlElement(name = "CertificatoControllo", required = true)
        protected byte[] certificatoControllo;

        /**
         * Recupera il valore della proprietï¿½ anagrafica.
         * 
         * @return
         *     possible object is
         *     {@link Anagrafica }
         *     
         */
        public Anagrafica getAnagrafica() {
            return anagrafica;
        }

        /**
         * Imposta il valore della proprietï¿½ anagrafica.
         * 
         * @param value
         *     allowed object is
         *     {@link Anagrafica }
         *     
         */
        public void setAnagrafica(Anagrafica value) {
            this.anagrafica = value;
        }

        /**
         * Recupera il valore della proprietï¿½ certificatoControllo.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getCertificatoControllo() {
            return certificatoControllo;
        }

        /**
         * Imposta il valore della proprietï¿½ certificatoControllo.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setCertificatoControllo(byte[] value) {
            this.certificatoControllo = value;
        }

    }

}
