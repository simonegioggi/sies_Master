//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="lavoroSostitutivo" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
 *         &lt;element name="libertaControllata" type="{http://it/mig/sies/type/fogliComplementari}Durata"/>
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
    "lavoroSostitutivo",
    "libertaControllata"
})
public class PenaConversionePenaPecuniaria {

    @XmlElement(required = true, nillable = true)
    protected Durata lavoroSostitutivo;
    @XmlElement(required = true, nillable = true)
    protected Durata libertaControllata;

    /**
     * Recupera il valore della proprietï¿½ lavoroSostitutivo.
     * 
     * @return
     *     possible object is
     *     {@link Durata }
     *     
     */
    public Durata getLavoroSostitutivo() {
        return lavoroSostitutivo;
    }

    /**
     * Imposta il valore della proprietï¿½ lavoroSostitutivo.
     * 
     * @param value
     *     allowed object is
     *     {@link Durata }
     *     
     */
    public void setLavoroSostitutivo(Durata value) {
        this.lavoroSostitutivo = value;
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

}
