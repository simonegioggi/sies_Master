
package it.mig.sies.type.foglicomplementari;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ChiaviProvvedimentoGiudiziario"/>
 *         &lt;element name="numeroSentenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagCumulante" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="annoSentenza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dataProvvedimento" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="codiceAutorita">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceSedeAutoritaPrincipaleDistaccata">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codiceSedeAutoritaPrincipale">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="6"/>
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
    "chiaviProvvedimentoGiudiziario",
    "numeroSentenza",
    "flagCumulante",
    "annoSentenza",
    "dataProvvedimento",
    "codiceAutorita",
    "codiceSedeAutoritaPrincipaleDistaccata",
    "codiceSedeAutoritaPrincipale"
})
public class ProvvedimentoGiudiziario {

    @XmlElement(name = "ChiaviProvvedimentoGiudiziario", required = true, nillable = true)
    protected ChiaviProvvedimentoGiudiziario chiaviProvvedimentoGiudiziario;
    @XmlElement(required = true, nillable = true)
    protected String numeroSentenza;
    protected boolean flagCumulante;
    @XmlElement(required = true, nillable = true)
    protected String annoSentenza;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataProvvedimento;
    @XmlElement(required = true)
    protected String codiceAutorita;
    @XmlElement(required = true, nillable = true)
    protected String codiceSedeAutoritaPrincipaleDistaccata;
    @XmlElement(required = true, nillable = true)
    protected String codiceSedeAutoritaPrincipale;

    /**
     * Recupera il valore della proprieta chiaviProvvedimentoGiudiziario.
     * 
     * @return
     *     possible object is
     *     {@link ChiaviProvvedimentoGiudiziario }
     *     
     */
    public ChiaviProvvedimentoGiudiziario getChiaviProvvedimentoGiudiziario() {
        return chiaviProvvedimentoGiudiziario;
    }

    /**
     * Imposta il valore della proprieta chiaviProvvedimentoGiudiziario.
     * 
     * @param value
     *     allowed object is
     *     {@link ChiaviProvvedimentoGiudiziario }
     *     
     */
    public void setChiaviProvvedimentoGiudiziario(ChiaviProvvedimentoGiudiziario value) {
        this.chiaviProvvedimentoGiudiziario = value;
    }

    /**
     * Recupera il valore della proprieta numeroSentenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSentenza() {
        return numeroSentenza;
    }

    /**
     * Imposta il valore della proprieta numeroSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSentenza(String value) {
        this.numeroSentenza = value;
    }

    /**
     * Recupera il valore della proprieta flagCumulante.
     * 
     */
    public boolean isFlagCumulante() {
        return flagCumulante;
    }

    /**
     * Imposta il valore della proprieta flagCumulante.
     * 
     */
    public void setFlagCumulante(boolean value) {
        this.flagCumulante = value;
    }

    /**
     * Recupera il valore della proprieta annoSentenza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnnoSentenza() {
        return annoSentenza;
    }

    /**
     * Imposta il valore della proprieta annoSentenza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnnoSentenza(String value) {
        this.annoSentenza = value;
    }

    /**
     * Recupera il valore della proprieta dataProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataProvvedimento() {
        return dataProvvedimento;
    }

    /**
     * Imposta il valore della proprieta dataProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataProvvedimento(XMLGregorianCalendar value) {
        this.dataProvvedimento = value;
    }

    /**
     * Recupera il valore della proprieta codiceAutorita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAutorita() {
        return codiceAutorita;
    }

    /**
     * Imposta il valore della proprieta codiceAutorita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAutorita(String value) {
        this.codiceAutorita = value;
    }

    /**
     * Recupera il valore della proprieta codiceSedeAutoritaPrincipaleDistaccata.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceSedeAutoritaPrincipaleDistaccata() {
        return codiceSedeAutoritaPrincipaleDistaccata;
    }

    /**
     * Imposta il valore della proprieta codiceSedeAutoritaPrincipaleDistaccata.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceSedeAutoritaPrincipaleDistaccata(String value) {
        this.codiceSedeAutoritaPrincipaleDistaccata = value;
    }

    /**
     * Recupera il valore della proprieta codiceSedeAutoritaPrincipale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceSedeAutoritaPrincipale() {
        return codiceSedeAutoritaPrincipale;
    }

    /**
     * Imposta il valore della proprieta codiceSedeAutoritaPrincipale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceSedeAutoritaPrincipale(String value) {
        this.codiceSedeAutoritaPrincipale = value;
    }

}
