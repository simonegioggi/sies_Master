//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="reclusione" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="arresto" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="importoAmmenda">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="importoMulta">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="14"/>
 *               &lt;fractionDigits value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tipoRichiesta">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
    "reclusione",
    "arresto",
    "importoAmmenda",
    "importoMulta",
    "tipoRichiesta"
})
public class RichiesteGEAnticipazioneEffetti {

    @XmlElement(required = true, nillable = true)
    protected Durata reclusione;
    @XmlElement(required = true, nillable = true)
    protected Durata arresto;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal importoAmmenda;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal importoMulta;
    @XmlElement(required = true)
    protected String tipoRichiesta;

    /**
     * Recupera il valore della proprietï¿½ reclusione.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getReclusione() {
        return reclusione;
    }

    /**
     * Imposta il valore della proprietï¿½ reclusione.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setReclusione(Durata value) {
        this.reclusione = value;
    }

    /**
     * Recupera il valore della proprietï¿½ arresto.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getArresto() {
        return arresto;
    }

    /**
     * Imposta il valore della proprietï¿½ arresto.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setArresto(Durata value) {
        this.arresto = value;
    }

    /**
     * Recupera il valore della proprietï¿½ importoAmmenda.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoAmmenda() {
        return importoAmmenda;
    }

    /**
     * Imposta il valore della proprietï¿½ importoAmmenda.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoAmmenda(BigDecimal value) {
        this.importoAmmenda = value;
    }

    /**
     * Recupera il valore della proprietï¿½ importoMulta.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoMulta() {
        return importoMulta;
    }

    /**
     * Imposta il valore della proprietï¿½ importoMulta.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoMulta(BigDecimal value) {
        this.importoMulta = value;
    }

    /**
     * Recupera il valore della proprietï¿½ tipoRichiesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRichiesta() {
        return tipoRichiesta;
    }

    /**
     * Imposta il valore della proprietï¿½ tipoRichiesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRichiesta(String value) {
        this.tipoRichiesta = value;
    }

}
