//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 *         &lt;element name="semiDetenzione" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="libertaControllata" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
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
 *         &lt;element name="espulsioneStato" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="tipoEspulsioneStato">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="lavoroPubblicaUtilita" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="numeroOreLPU">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tipoLPU">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
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
    "semiDetenzione",
    "libertaControllata",
    "importoAmmenda",
    "importoMulta",
    "espulsioneStato",
    "tipoEspulsioneStato",
    "lavoroPubblicaUtilita",
    "numeroOreLPU",
    "tipoLPU"
})
public class SanzioniSostitutive {

    @XmlElement(required = true, nillable = true)
    protected Durata semiDetenzione;
    @XmlElement(required = true, nillable = true)
    protected Durata libertaControllata;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal importoAmmenda;
    @XmlElement(required = true, nillable = true)
    protected BigDecimal importoMulta;
    @XmlElement(required = true, nillable = true)
    protected Durata espulsioneStato;
    @XmlElement(required = true, nillable = true)
    protected String tipoEspulsioneStato;
    @XmlElement(required = true, nillable = true)
    protected Durata lavoroPubblicaUtilita;
    @XmlElement(required = true, nillable = true)
    protected BigInteger numeroOreLPU;
    @XmlElement(required = true, nillable = true)
    protected String tipoLPU;

    /**
     * Recupera il valore della proprietï¿½ semiDetenzione.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getSemiDetenzione() {
        return semiDetenzione;
    }

    /**
     * Imposta il valore della proprietï¿½ semiDetenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setSemiDetenzione(Durata value) {
        this.semiDetenzione = value;
    }

    /**
     * Recupera il valore della proprietï¿½ libertaControllata.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getLibertaControllata() {
        return libertaControllata;
    }

    /**
     * Imposta il valore della proprietï¿½ libertaControllata.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setLibertaControllata(Durata value) {
        this.libertaControllata = value;
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
     * Recupera il valore della proprietï¿½ espulsioneStato.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getEspulsioneStato() {
        return espulsioneStato;
    }

    /**
     * Imposta il valore della proprietï¿½ espulsioneStato.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setEspulsioneStato(Durata value) {
        this.espulsioneStato = value;
    }

    /**
     * Recupera il valore della proprietï¿½ tipoEspulsioneStato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEspulsioneStato() {
        return tipoEspulsioneStato;
    }

    /**
     * Imposta il valore della proprietï¿½ tipoEspulsioneStato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEspulsioneStato(String value) {
        this.tipoEspulsioneStato = value;
    }

    /**
     * Recupera il valore della proprietï¿½ lavoroPubblicaUtilita.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getLavoroPubblicaUtilita() {
        return lavoroPubblicaUtilita;
    }

    /**
     * Imposta il valore della proprietï¿½ lavoroPubblicaUtilita.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setLavoroPubblicaUtilita(Durata value) {
        this.lavoroPubblicaUtilita = value;
    }

    /**
     * Recupera il valore della proprietï¿½ numeroOreLPU.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumeroOreLPU() {
        return numeroOreLPU;
    }

    /**
     * Imposta il valore della proprietï¿½ numeroOreLPU.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumeroOreLPU(BigInteger value) {
        this.numeroOreLPU = value;
    }

    /**
     * Recupera il valore della proprietï¿½ tipoLPU.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoLPU() {
        return tipoLPU;
    }

    /**
     * Imposta il valore della proprietï¿½ tipoLPU.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoLPU(String value) {
        this.tipoLPU = value;
    }

}
