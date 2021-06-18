
package it.giustizia.serviziTelematici.reginde.interrogazioniExt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per soggetto complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="soggetto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruoliente" type="{http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt}ruoloente" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="indirizzi" type="{http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt}indirizzo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="soggetto" type="{http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt}soggetti" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soggetto", propOrder = {
    "ruoliente",
    "indirizzi",
    "soggetto"
})
public class Soggetto {

    protected List<Ruoloente> ruoliente;
    protected List<Indirizzo> indirizzi;
    protected Soggetti soggetto;

    /**
     * Gets the value of the ruoliente property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ruoliente property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRuoliente().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ruoloente }
     * 
     * 
     */
    public List<Ruoloente> getRuoliente() {
        if (ruoliente == null) {
            ruoliente = new ArrayList<Ruoloente>();
        }
        return this.ruoliente;
    }

    /**
     * Gets the value of the indirizzi property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indirizzi property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndirizzi().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Indirizzo }
     * 
     * 
     */
    public List<Indirizzo> getIndirizzi() {
        if (indirizzi == null) {
            indirizzi = new ArrayList<Indirizzo>();
        }
        return this.indirizzi;
    }

    /**
     * Recupera il valore della proprietà soggetto.
     * 
     * @return
     *     possible object is
     *     {@link Soggetti }
     *     
     */
    public Soggetti getSoggetto() {
        return soggetto;
    }

    /**
     * Imposta il valore della proprietà soggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link Soggetti }
     *     
     */
    public void setSoggetto(Soggetti value) {
        this.soggetto = value;
    }

}
