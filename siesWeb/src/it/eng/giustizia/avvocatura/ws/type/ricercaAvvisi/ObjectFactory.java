//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 03:51:09 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.ricercaAvvisi;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.eng.giustizia.avvocatura.ws.type.ricercaavvisi package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.eng.giustizia.avvocatura.ws.type.ricercaavvisi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ERRORE }
     * 
     */
    public ERRORE createERRORE() {
        return new ERRORE();
    }

    /**
     * Create an instance of {@link RICERCAAVVISO }
     * 
     */
    public RICERCAAVVISO createRICERCAAVVISO() {
        return new RICERCAAVVISO();
    }

    /**
     * Create an instance of {@link DATIAVVISOINPUT }
     * 
     */
    public DATIAVVISOINPUT createDATIAVVISOINPUT() {
        return new DATIAVVISOINPUT();
    }

    /**
     * Create an instance of {@link DATATYPE }
     * 
     */
    public DATATYPE createDATATYPE() {
        return new DATATYPE();
    }

    /**
     * Create an instance of {@link DATIAVVISOOUTPUT }
     * 
     */
    public DATIAVVISOOUTPUT createDATIAVVISOOUTPUT() {
        return new DATIAVVISOOUTPUT();
    }

    /**
     * Create an instance of {@link AVVISO }
     * 
     */
    public AVVISO createAVVISO() {
        return new AVVISO();
    }

}
