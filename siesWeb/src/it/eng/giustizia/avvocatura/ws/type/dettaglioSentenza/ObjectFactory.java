//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.08.24 alle 10:47:00 AM CEST 
//


package it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza package. 
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

    private final static QName _DATIAVVISO_QNAME = new QName("http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza", "DATI_AVVISO");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.eng.giustizia.avvocatura.ws.type.dettaglioSentenza
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DETTAGLIOSENTENZA }
     * 
     */
    public DETTAGLIOSENTENZA createDETTAGLIOSENTENZA() {
        return new DETTAGLIOSENTENZA();
    }

    /**
     * Create an instance of {@link DATIINPUTDETTAGLIO }
     * 
     */
    public DATIINPUTDETTAGLIO createDATIINPUTDETTAGLIO() {
        return new DATIINPUTDETTAGLIO();
    }

    /**
     * Create an instance of {@link DATIAVVISO }
     * 
     */
    public DATIAVVISO createDATIAVVISO() {
        return new DATIAVVISO();
    }

    /**
     * Create an instance of {@link OUTPUTDETTAGLIOSENTENZA }
     * 
     */
    public OUTPUTDETTAGLIOSENTENZA createOUTPUTDETTAGLIOSENTENZA() {
        return new OUTPUTDETTAGLIOSENTENZA();
    }

    /**
     * Create an instance of {@link DATIRIEPILOGOPROCEDIMENTO }
     * 
     */
    public DATIRIEPILOGOPROCEDIMENTO createDATIRIEPILOGOPROCEDIMENTO() {
        return new DATIRIEPILOGOPROCEDIMENTO();
    }

    /**
     * Create an instance of {@link DATATYPE }
     * 
     */
    public DATATYPE createDATATYPE() {
        return new DATATYPE();
    }

    /**
     * Create an instance of {@link DATISENTENZA }
     * 
     */
    public DATISENTENZA createDATISENTENZA() {
        return new DATISENTENZA();
    }

    /**
     * Create an instance of {@link ESITITYPE }
     * 
     */
    public ESITITYPE createESITITYPE() {
        return new ESITITYPE();
    }

    /**
     * Create an instance of {@link DESTINATARITYPE }
     * 
     */
    public DESTINATARITYPE createDESTINATARITYPE() {
        return new DESTINATARITYPE();
    }

    /**
     * Create an instance of {@link ERRORE }
     * 
     */
    public ERRORE createERRORE() {
        return new ERRORE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DATIAVVISO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/eng/giustizia/avvocatura/ws/type/dettaglioSentenza", name = "DATI_AVVISO")
    public JAXBElement<DATIAVVISO> createDATIAVVISO(DATIAVVISO value) {
        return new JAXBElement<DATIAVVISO>(_DATIAVVISO_QNAME, DATIAVVISO.class, null, value);
    }

}
