//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.10 alle 12:41:34 PM CET 
//


package it.eng.giustizia.avvocatura.ws.type.elencoProcedimenti;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DATA_TYPE complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DATA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="giorno" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="mese" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="anno" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATA_TYPE", propOrder = {
    "giorno",
    "mese",
    "anno"
})
public class DATATYPE {

    protected int giorno;
    protected int mese;
    protected int anno;

    /**
     * Recupera il valore della proprietà giorno.
     * 
     */
    public int getGiorno() {
        return giorno;
    }

    /**
     * Imposta il valore della proprietà giorno.
     * 
     */
    public void setGiorno(int value) {
        this.giorno = value;
    }

    /**
     * Recupera il valore della proprietà mese.
     * 
     */
    public int getMese() {
        return mese;
    }

    /**
     * Imposta il valore della proprietà mese.
     * 
     */
    public void setMese(int value) {
        this.mese = value;
    }

    /**
     * Recupera il valore della proprietà anno.
     * 
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Imposta il valore della proprietà anno.
     * 
     */
    public void setAnno(int value) {
        this.anno = value;
    }

}
