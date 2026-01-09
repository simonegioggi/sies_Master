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
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ProvvedimentoGiudiziario" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Esito"/>
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
    "provvedimentoGiudiziario",
    "esito"
})
@XmlRootElement(name = "ProvvedimentoNSC")
public class ProvvedimentoNSC {

    @XmlElement(name = "ProvvedimentoGiudiziario", required = true, nillable = true)
    protected List<ProvvedimentoGiudiziario> provvedimentoGiudiziario;
    @XmlElement(name = "Esito", required = true)
    protected Esito esito;

    /**
     * Gets the value of the provvedimentoGiudiziario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the provvedimentoGiudiziario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProvvedimentoGiudiziario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProvvedimentoGiudiziario }
     * 
     * 
     */
    public List<ProvvedimentoGiudiziario> getProvvedimentoGiudiziario() {
        if (provvedimentoGiudiziario == null) {
            provvedimentoGiudiziario = new ArrayList<ProvvedimentoGiudiziario>();
        }
        return this.provvedimentoGiudiziario;
    }

    /**
     * Recupera il valore della proprietï¿½ esito.
     * 
     * @return
     *     possible object is
     *     {@link Esito }
     *     
     */
    public Esito getEsito() {
        return esito;
    }

    /**
     * Imposta il valore della proprietï¿½ esito.
     * 
     * @param value
     *     allowed object is
     *     {@link Esito }
     *     
     */
    public void setEsito(Esito value) {
        this.esito = value;
    }

}
