//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.09.05 alle 02:30:32 PM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.richiestaStampa;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.eng.giustizia.avvocatura.ws.type.richiestaStampa package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.eng.giustizia.avvocatura.ws.type.richiestaStampa
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
     * Create an instance of {@link RICHIESTASTAMPA }
     * 
     */
    public RICHIESTASTAMPA createRICHIESTASTAMPA() {
        return new RICHIESTASTAMPA();
    }

    /**
     * Create an instance of {@link DATISTAMPAINPUT }
     * 
     */
    public DATISTAMPAINPUT createDATISTAMPAINPUT() {
        return new DATISTAMPAINPUT();
    }

    /**
     * Create an instance of {@link DATISTAMPAOUTPUT }
     * 
     */
    public DATISTAMPAOUTPUT createDATISTAMPAOUTPUT() {
        return new DATISTAMPAOUTPUT();
    }

}
