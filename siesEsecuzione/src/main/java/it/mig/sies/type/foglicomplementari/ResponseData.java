//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.26 alle 04:01:02 PM CEST 
//


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
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}Esito"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ChiaviAnagrafica"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ChiaviProvvedimentoEsecutivo"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ChiaviProvvedimentoGiudiziario" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ArrayOmonimi" minOccurs="0"/>
 *         &lt;element name="Estratto" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="AnagraficaCancellata" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element ref="{http://it/mig/sies/type/fogliComplementari}ProvvedimentoNSC" minOccurs="0"/>
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
    "esito",
    "chiaviAnagrafica",
    "chiaviProvvedimentoEsecutivo",
    "chiaviProvvedimentoGiudiziario",
    "arrayOmonimi",
    "estratto",
    "anagraficaCancellata",
    "provvedimentoNSC"
})
@XmlRootElement(name = "ResponseData")
public class ResponseData {

    @XmlElement(name = "Esito", required = true)
    protected Esito esito;
    @XmlElement(name = "ChiaviAnagrafica", required = true, nillable = true)
    protected ChiaviAnagrafica chiaviAnagrafica;
    @XmlElement(name = "ChiaviProvvedimentoEsecutivo", required = true, nillable = true)
    protected ChiaviProvvedimentoEsecutivo chiaviProvvedimentoEsecutivo;
    @XmlElement(name = "ChiaviProvvedimentoGiudiziario", required = true, nillable = true)
    protected List<ChiaviProvvedimentoGiudiziario> chiaviProvvedimentoGiudiziario;
    @XmlElement(name = "ArrayOmonimi")
    protected ArrayOmonimi arrayOmonimi;
    @XmlElement(name = "Estratto", required = true, nillable = true)
    protected byte[] estratto;
    @XmlElement(name = "AnagraficaCancellata")
    protected boolean anagraficaCancellata;
    @XmlElement(name = "ProvvedimentoNSC")
    protected ProvvedimentoNSC provvedimentoNSC;

    /**
     * Recupera il valore della proprietà esito.
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
     * Imposta il valore della proprietà esito.
     * 
     * @param value
     *     allowed object is
     *     {@link Esito }
     *     
     */
    public void setEsito(Esito value) {
        this.esito = value;
    }

    /**
     * Recupera il valore della proprietà chiaviAnagrafica.
     * 
     * @return
     *     possible object is
     *     {@link ChiaviAnagrafica }
     *     
     */
    public ChiaviAnagrafica getChiaviAnagrafica() {
        return chiaviAnagrafica;
    }

    /**
     * Imposta il valore della proprietà chiaviAnagrafica.
     * 
     * @param value
     *     allowed object is
     *     {@link ChiaviAnagrafica }
     *     
     */
    public void setChiaviAnagrafica(ChiaviAnagrafica value) {
        this.chiaviAnagrafica = value;
    }

    /**
     * Recupera il valore della proprietà chiaviProvvedimentoEsecutivo.
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
     * Imposta il valore della proprietà chiaviProvvedimentoEsecutivo.
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
     * Gets the value of the chiaviProvvedimentoGiudiziario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chiaviProvvedimentoGiudiziario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChiaviProvvedimentoGiudiziario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChiaviProvvedimentoGiudiziario }
     * 
     * 
     */
    public List<ChiaviProvvedimentoGiudiziario> getChiaviProvvedimentoGiudiziario() {
        if (chiaviProvvedimentoGiudiziario == null) {
            chiaviProvvedimentoGiudiziario = new ArrayList<ChiaviProvvedimentoGiudiziario>();
        }
        return this.chiaviProvvedimentoGiudiziario;
    }

    /**
     * Recupera il valore della proprietà arrayOmonimi.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOmonimi }
     *     
     */
    public ArrayOmonimi getArrayOmonimi() {
        return arrayOmonimi;
    }

    /**
     * Imposta il valore della proprietà arrayOmonimi.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOmonimi }
     *     
     */
    public void setArrayOmonimi(ArrayOmonimi value) {
        this.arrayOmonimi = value;
    }

    /**
     * Recupera il valore della proprietà estratto.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getEstratto() {
        return estratto;
    }

    /**
     * Imposta il valore della proprietà estratto.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setEstratto(byte[] value) {
        this.estratto = value;
    }

    /**
     * Recupera il valore della proprietà anagraficaCancellata.
     * 
     */
    public boolean isAnagraficaCancellata() {
        return anagraficaCancellata;
    }

    /**
     * Imposta il valore della proprietà anagraficaCancellata.
     * 
     */
    public void setAnagraficaCancellata(boolean value) {
        this.anagraficaCancellata = value;
    }

    /**
     * Recupera il valore della proprietà provvedimentoNSC.
     * 
     * @return
     *     possible object is
     *     {@link ProvvedimentoNSC }
     *     
     */
    public ProvvedimentoNSC getProvvedimentoNSC() {
        return provvedimentoNSC;
    }

    /**
     * Imposta il valore della proprietà provvedimentoNSC.
     * 
     * @param value
     *     allowed object is
     *     {@link ProvvedimentoNSC }
     *     
     */
    public void setProvvedimentoNSC(ProvvedimentoNSC value) {
        this.provvedimentoNSC = value;
    }

}
