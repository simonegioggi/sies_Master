
package it.giustizia.serviziTelematici.reginde.interrogazioniInt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import it.giustizia.serviziTelematici.reginde.interrogazioniExt.Ruoloente;


/**
 * <p>Classe Java per ricercaEnteCompleteResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ricercaEnteCompleteResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt}ruoloente" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ricercaEnteCompleteResponse", propOrder = {
    "_return"
})
public class RicercaEnteCompleteResponse {

    @XmlElement(name = "return")
    protected List<Ruoloente> _return;

    /**
     * Gets the value of the return property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the return property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReturn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ruoloente }
     * 
     * 
     */
    public List<Ruoloente> getReturn() {
        if (_return == null) {
            _return = new ArrayList<Ruoloente>();
        }
        return this._return;
    }

}
