//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.29 alle 10:18:36 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto;

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
 *         &lt;element name="idEvento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codiTipoProvvedimento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoUfficio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://it/eng/giustizia/avvocatura/ws/type/dettaglioDecreto}DATI_AVVISO"/>
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
    "idEvento",
    "codiTipoProvvedimento",
    "tipoUfficio",
    "datiavviso"
})
@XmlRootElement(name = "DATI_INPUT_DETTAGLIO")
public class DATIINPUTDETTAGLIO {

    @XmlElement(required = true)
    protected String idEvento;
    @XmlElement(required = true)
    protected String codiTipoProvvedimento;
    @XmlElement(required = true)
    protected String tipoUfficio;
    @XmlElement(name = "DATI_AVVISO", required = true, nillable = true)
    protected DATIAVVISO datiavviso;

    /**
     * Recupera il valore della proprietà idEvento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEvento() {
        return idEvento;
    }

    /**
     * Imposta il valore della proprietà idEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEvento(String value) {
        this.idEvento = value;
    }

    /**
     * Recupera il valore della proprietà codiTipoProvvedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiTipoProvvedimento() {
        return codiTipoProvvedimento;
    }

    /**
     * Imposta il valore della proprietà codiTipoProvvedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiTipoProvvedimento(String value) {
        this.codiTipoProvvedimento = value;
    }

    /**
     * Recupera il valore della proprietà tipoUfficio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoUfficio() {
        return tipoUfficio;
    }

    /**
     * Imposta il valore della proprietà tipoUfficio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoUfficio(String value) {
        this.tipoUfficio = value;
    }

    /**
     * elemento da valorizzare per richiesta di
     * 							dettaglio da elenco Avvisi. Serve per l'aggiornamento del
     * 							flagVisualizzazione
     * 						
     * 
     * @return
     *     possible object is
     *     {@link DATIAVVISO }
     *     
     */
    public DATIAVVISO getDATIAVVISO() {
        return datiavviso;
    }

    /**
     * Imposta il valore della proprietà datiavviso.
     * 
     * @param value
     *     allowed object is
     *     {@link DATIAVVISO }
     *     
     */
    public void setDATIAVVISO(DATIAVVISO value) {
        this.datiavviso = value;
    }

}
