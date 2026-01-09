
package it.mig.sies.type.foglicomplementari;

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
 *         &lt;element name="annoSiep" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="numeroSiep" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="flagPrincipale" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ProvvedimentoGiudiziario" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ChiaviProvvedimentoEsecutivo"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}DatiPubblicoMinistero"/>
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
    "annoSiep",
    "numeroSiep",
    "flagPrincipale",
    "provvedimentoGiudiziario",
    "chiaviProvvedimentoEsecutivo",
    "datiPubblicoMinistero"
})
@XmlRootElement(name = "FoglioComplementare")
public class FoglioComplementare {

    protected Integer annoSiep;
    protected Long numeroSiep;
    protected boolean flagPrincipale;
    @XmlElement(name = "ProvvedimentoGiudiziario", required = true, nillable = true)
    protected List<ProvvedimentoGiudiziario> provvedimentoGiudiziario;
    @XmlElement(name = "ChiaviProvvedimentoEsecutivo", required = true, nillable = true)
    protected ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo;
    @XmlElement(name = "DatiPubblicoMinistero", required = true)
    protected DatiPubblicoMinistero datiPubblicoMinistero;

    /**
     * Recupera il valore della proprieta annoSiep.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnnoSiep() {
        return annoSiep;
    }

    /**
     * Imposta il valore della proprieta annoSiep.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnnoSiep(Integer value) {
        this.annoSiep = value;
    }

    /**
     * Recupera il valore della proprieta numeroSiep.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumeroSiep() {
        return numeroSiep;
    }

    /**
     * Imposta il valore della proprieta numeroSiep.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumeroSiep(Long value) {
        this.numeroSiep = value;
    }

    /**
     * Recupera il valore della proprieta flagPrincipale.
     * 
     */
    public boolean isFlagPrincipale() {
        return flagPrincipale;
    }

    /**
     * Imposta il valore della proprieta flagPrincipale.
     * 
     */
    public void setFlagPrincipale(boolean value) {
        this.flagPrincipale = value;
    }

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
     * Recupera il valore della proprieta chiaviProvvedimentoEsecutivo.
     * 
     * @return
     *     possible object is
     *     {@link ChiaviProvvedimentoEsecutivo }
     *     
     */
    public ChiaviProvvedimentoEsecutivo getChiaviProvvedimentoEsecutivo() {
        return chiaviProvvedimentoEsecutivo;
    }

    /**
     * Imposta il valore della proprieta chiaviProvvedimentoEsecutivo.
     * 
     * @param value
     *     allowed object is
     *     {@link ChiaviProvvedimentoEsecutivo }
     *     
     */
    public void setChiaviProvvedimentoEsecutivo(ChiaviProvvedimentoEsecutivo value) {
        this.chiaviProvvedimentoEsecutivo = value;
    }

    /**
     * Recupera il valore della proprieta datiPubblicoMinistero.
     * 
     * @return
     *     possible object is
     *     {@link DatiPubblicoMinistero }
     *     
     */
    public DatiPubblicoMinistero getDatiPubblicoMinistero() {
        return datiPubblicoMinistero;
    }

    /**
     * Imposta il valore della proprieta datiPubblicoMinistero.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiPubblicoMinistero }
     *     
     */
    public void setDatiPubblicoMinistero(DatiPubblicoMinistero value) {
        this.datiPubblicoMinistero = value;
    }

}
