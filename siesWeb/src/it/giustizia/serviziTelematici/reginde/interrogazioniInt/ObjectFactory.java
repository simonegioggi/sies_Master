
package it.giustizia.serviziTelematici.reginde.interrogazioniInt;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.giustizia.serviziTelematici.reginde.interrogazioniInt package. 
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

    private final static QName _RicercaEnteComplete_QNAME = new QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaEnteComplete");
    private final static QName _RicercaSoggettoComplete_QNAME = new QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaSoggettoComplete");
    private final static QName _SearchLimitException_QNAME = new QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException");
    private final static QName _RicercaSoggettoCompleteResponse_QNAME = new QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaSoggettoCompleteResponse");
    private final static QName _RicercaEnteCompleteResponse_QNAME = new QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaEnteCompleteResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.giustizia.serviziTelematici.reginde.interrogazioniInt
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchLimitException }
     * 
     */
    public SearchLimitException createSearchLimitException() {
        return new SearchLimitException();
    }

    /**
     * Create an instance of {@link RicercaEnteComplete }
     * 
     */
    public RicercaEnteComplete createRicercaEnteComplete() {
        return new RicercaEnteComplete();
    }

    /**
     * Create an instance of {@link RicercaSoggettoComplete }
     * 
     */
    public RicercaSoggettoComplete createRicercaSoggettoComplete() {
        return new RicercaSoggettoComplete();
    }

    /**
     * Create an instance of {@link RicercaSoggettoCompleteResponse }
     * 
     */
    public RicercaSoggettoCompleteResponse createRicercaSoggettoCompleteResponse() {
        return new RicercaSoggettoCompleteResponse();
    }

    /**
     * Create an instance of {@link RicercaEnteCompleteResponse }
     * 
     */
    public RicercaEnteCompleteResponse createRicercaEnteCompleteResponse() {
        return new RicercaEnteCompleteResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RicercaEnteComplete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", name = "ricercaEnteComplete")
    public JAXBElement<RicercaEnteComplete> createRicercaEnteComplete(RicercaEnteComplete value) {
        return new JAXBElement<RicercaEnteComplete>(_RicercaEnteComplete_QNAME, RicercaEnteComplete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RicercaSoggettoComplete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", name = "ricercaSoggettoComplete")
    public JAXBElement<RicercaSoggettoComplete> createRicercaSoggettoComplete(RicercaSoggettoComplete value) {
        return new JAXBElement<RicercaSoggettoComplete>(_RicercaSoggettoComplete_QNAME, RicercaSoggettoComplete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchLimitException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", name = "SearchLimitException")
    public JAXBElement<SearchLimitException> createSearchLimitException(SearchLimitException value) {
        return new JAXBElement<SearchLimitException>(_SearchLimitException_QNAME, SearchLimitException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RicercaSoggettoCompleteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", name = "ricercaSoggettoCompleteResponse")
    public JAXBElement<RicercaSoggettoCompleteResponse> createRicercaSoggettoCompleteResponse(RicercaSoggettoCompleteResponse value) {
        return new JAXBElement<RicercaSoggettoCompleteResponse>(_RicercaSoggettoCompleteResponse_QNAME, RicercaSoggettoCompleteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RicercaEnteCompleteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", name = "ricercaEnteCompleteResponse")
    public JAXBElement<RicercaEnteCompleteResponse> createRicercaEnteCompleteResponse(RicercaEnteCompleteResponse value) {
        return new JAXBElement<RicercaEnteCompleteResponse>(_RicercaEnteCompleteResponse_QNAME, RicercaEnteCompleteResponse.class, null, value);
    }

}
