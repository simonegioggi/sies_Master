//
// Questo file ï¿½ stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrï¿½ persa durante la ricompilazione dello schema di origine. 
// Generato il: 2017.02.06 alle 11:58:26 AM CET 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import java.math.BigInteger;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.mig.sies.type.foglicomplementari package. 
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

    private final static QName _AnnoOrdinanza_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "annoOrdinanza");
    private final static QName _MisuraSicurezza_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "MisuraSicurezza");
    private final static QName _AnnoSIEP_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "annoSIEP");
    private final static QName _AnnoSentenza_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "annoSentenza");
    private final static QName _IsolamentoDiurno_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "isolamentoDiurno");
    private final static QName _ProvvedimentoGiudiziario_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "ProvvedimentoGiudiziario");
    private final static QName _ChiaveSies_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "chiaveSies");
    private final static QName _PenaAccessoria_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "PenaAccessoria");
    private final static QName _NumeroSIEP_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "numeroSIEP");
    private final static QName _Reclusione_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "reclusione");
    private final static QName _ChiaviProvvedimentoGiudiziario_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "ChiaviProvvedimentoGiudiziario");
    private final static QName _PenaConversionePenaPecuniaria_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "PenaConversionePenaPecuniaria");
    private final static QName _ChiaviAnagrafica_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "ChiaviAnagrafica");
    private final static QName _DataFinePenaDal_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "dataFinePenaDal");
    private final static QName _SanzioniSostitutive_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "SanzioniSostitutive");
    private final static QName _Arresto_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "arresto");
    private final static QName _Ergastolo_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "ergastolo");
    private final static QName _RichiesteGEAnticipazioneEffetti_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "RichiesteGEAnticipazioneEffetti");
    private final static QName _NumeroSentenza_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "numeroSentenza");
    private final static QName _NumeroOrdinanza_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "numeroOrdinanza");
    private final static QName _CodiceSedePM_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "codiceSedePM");
    private final static QName _DataFinePenaAl_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "dataFinePenaAl");
    private final static QName _LiberazioneAnticipataConcessaDetrarreCumulo_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "LiberazioneAnticipataConcessaDetrarreCumulo");
    private final static QName _SanzioniGiudicePace_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "SanzioniGiudicePace");
    private final static QName _ChiaveNsc_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "chiaveNsc");
    private final static QName _DataFinePena_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "dataFinePena");
    private final static QName _ChiaviProvvedimentoEsecutivo_QNAME = new QName("http://it/mig/sies/type/fogliComplementari", "ChiaviProvvedimentoEsecutivo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.mig.sies.type.foglicomplementari
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOmonimi }
     * 
     */
    public ArrayOmonimi createArrayOmonimi() {
        return new ArrayOmonimi();
    }

    /**
     * Create an instance of {@link Durata }
     * 
     */
    public Durata createDurata() {
        return new Durata();
    }

    /**
     * Create an instance of {@link ChiaviProvvedimentoGiudiziario }
     * 
     */
    public ChiaviProvvedimentoGiudiziario createChiaviProvvedimentoGiudiziario() {
        return new ChiaviProvvedimentoGiudiziario();
    }

    /**
     * Create an instance of {@link PenaConversionePenaPecuniaria }
     * 
     */
    public PenaConversionePenaPecuniaria createPenaConversionePenaPecuniaria() {
        return new PenaConversionePenaPecuniaria();
    }

    /**
     * Create an instance of {@link Utente }
     * 
     */
    public Utente createUtente() {
        return new Utente();
    }

    /**
     * Create an instance of {@link Ufficio }
     * 
     */
    public Ufficio createUfficio() {
        return new Ufficio();
    }

    /**
     * Create an instance of {@link FoglioComplementare }
     * 
     */
    public FoglioComplementare createFoglioComplementare() {
        return new FoglioComplementare();
    }

    /**
     * Create an instance of {@link ProvvedimentoGiudiziario }
     * 
     */
    public ProvvedimentoGiudiziario createProvvedimentoGiudiziario() {
        return new ProvvedimentoGiudiziario();
    }

    /**
     * Create an instance of {@link ChiaviProvvedimentoEsecutivo }
     * 
     */
    public ChiaviProvvedimentoEsecutivo createChiaviProvvedimentoEsecutivo() {
        return new ChiaviProvvedimentoEsecutivo();
    }

    /**
     * Create an instance of {@link DatiPubblicoMinistero }
     * 
     */
    public DatiPubblicoMinistero createDatiPubblicoMinistero() {
        return new DatiPubblicoMinistero();
    }

    /**
     * Create an instance of {@link MisuraSicurezza }
     * 
     */
    public MisuraSicurezza createMisuraSicurezza() {
        return new MisuraSicurezza();
    }

    /**
     * Create an instance of {@link PenaAccessoria }
     * 
     */
    public PenaAccessoria createPenaAccessoria() {
        return new PenaAccessoria();
    }

    /**
     * Create an instance of {@link SanzioniSostitutive }
     * 
     */
    public SanzioniSostitutive createSanzioniSostitutive() {
        return new SanzioniSostitutive();
    }

    /**
     * Create an instance of {@link LiberazioneAnticipataConcessaDetrarreCumulo }
     * 
     */
    public LiberazioneAnticipataConcessaDetrarreCumulo createLiberazioneAnticipataConcessaDetrarreCumulo() {
        return new LiberazioneAnticipataConcessaDetrarreCumulo();
    }

    /**
     * Create an instance of {@link SanzioniGiudicePace }
     * 
     */
    public SanzioniGiudicePace createSanzioniGiudicePace() {
        return new SanzioniGiudicePace();
    }

    /**
     * Create an instance of {@link RichiesteGEAnticipazioneEffetti }
     * 
     */
    public RichiesteGEAnticipazioneEffetti createRichiesteGEAnticipazioneEffetti() {
        return new RichiesteGEAnticipazioneEffetti();
    }

    /**
     * Create an instance of {@link Anagrafica }
     * 
     */
    public Anagrafica createAnagrafica() {
        return new Anagrafica();
    }

    /**
     * Create an instance of {@link ChiaviAnagrafica }
     * 
     */
    public ChiaviAnagrafica createChiaviAnagrafica() {
        return new ChiaviAnagrafica();
    }

    /**
     * Create an instance of {@link ProvvedimentoNSC }
     * 
     */
    public ProvvedimentoNSC createProvvedimentoNSC() {
        return new ProvvedimentoNSC();
    }

    /**
     * Create an instance of {@link Esito }
     * 
     */
    public Esito createEsito() {
        return new Esito();
    }

    /**
     * Create an instance of {@link RequestData }
     * 
     */
    public RequestData createRequestData() {
        return new RequestData();
    }

    /**
     * Create an instance of {@link ResponseData }
     * 
     */
    public ResponseData createResponseData() {
        return new ResponseData();
    }

    /**
     * Create an instance of {@link ArrayOmonimi.Omonimo }
     * 
     */
    public ArrayOmonimi.Omonimo createArrayOmonimiOmonimo() {
        return new ArrayOmonimi.Omonimo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "annoOrdinanza")
    public JAXBElement<BigInteger> createAnnoOrdinanza(BigInteger value) {
        return new JAXBElement<BigInteger>(_AnnoOrdinanza_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MisuraSicurezza }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "MisuraSicurezza")
    public JAXBElement<MisuraSicurezza> createMisuraSicurezza(MisuraSicurezza value) {
        return new JAXBElement<MisuraSicurezza>(_MisuraSicurezza_QNAME, MisuraSicurezza.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "annoSIEP")
    public JAXBElement<BigInteger> createAnnoSIEP(BigInteger value) {
        return new JAXBElement<BigInteger>(_AnnoSIEP_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "annoSentenza")
    public JAXBElement<BigInteger> createAnnoSentenza(BigInteger value) {
        return new JAXBElement<BigInteger>(_AnnoSentenza_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Durata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "isolamentoDiurno")
    public JAXBElement<Durata> createIsolamentoDiurno(Durata value) {
        return new JAXBElement<Durata>(_IsolamentoDiurno_QNAME, Durata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvvedimentoGiudiziario }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "ProvvedimentoGiudiziario")
    public JAXBElement<ProvvedimentoGiudiziario> createProvvedimentoGiudiziario(ProvvedimentoGiudiziario value) {
        return new JAXBElement<ProvvedimentoGiudiziario>(_ProvvedimentoGiudiziario_QNAME, ProvvedimentoGiudiziario.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "chiaveSies")
    public JAXBElement<Long> createChiaveSies(Long value) {
        return new JAXBElement<Long>(_ChiaveSies_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PenaAccessoria }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "PenaAccessoria")
    public JAXBElement<PenaAccessoria> createPenaAccessoria(PenaAccessoria value) {
        return new JAXBElement<PenaAccessoria>(_PenaAccessoria_QNAME, PenaAccessoria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "numeroSIEP")
    public JAXBElement<BigInteger> createNumeroSIEP(BigInteger value) {
        return new JAXBElement<BigInteger>(_NumeroSIEP_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Durata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "reclusione")
    public JAXBElement<Durata> createReclusione(Durata value) {
        return new JAXBElement<Durata>(_Reclusione_QNAME, Durata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChiaviProvvedimentoGiudiziario }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "ChiaviProvvedimentoGiudiziario")
    public JAXBElement<ChiaviProvvedimentoGiudiziario> createChiaviProvvedimentoGiudiziario(ChiaviProvvedimentoGiudiziario value) {
        return new JAXBElement<ChiaviProvvedimentoGiudiziario>(_ChiaviProvvedimentoGiudiziario_QNAME, ChiaviProvvedimentoGiudiziario.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PenaConversionePenaPecuniaria }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "PenaConversionePenaPecuniaria")
    public JAXBElement<PenaConversionePenaPecuniaria> createPenaConversionePenaPecuniaria(PenaConversionePenaPecuniaria value) {
        return new JAXBElement<PenaConversionePenaPecuniaria>(_PenaConversionePenaPecuniaria_QNAME, PenaConversionePenaPecuniaria.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChiaviAnagrafica }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "ChiaviAnagrafica")
    public JAXBElement<ChiaviAnagrafica> createChiaviAnagrafica(ChiaviAnagrafica value) {
        return new JAXBElement<ChiaviAnagrafica>(_ChiaviAnagrafica_QNAME, ChiaviAnagrafica.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "dataFinePenaDal")
    public JAXBElement<XMLGregorianCalendar> createDataFinePenaDal(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DataFinePenaDal_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SanzioniSostitutive }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "SanzioniSostitutive")
    public JAXBElement<SanzioniSostitutive> createSanzioniSostitutive(SanzioniSostitutive value) {
        return new JAXBElement<SanzioniSostitutive>(_SanzioniSostitutive_QNAME, SanzioniSostitutive.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Durata }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "arresto")
    public JAXBElement<Durata> createArresto(Durata value) {
        return new JAXBElement<Durata>(_Arresto_QNAME, Durata.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "ergastolo")
    public JAXBElement<String> createErgastolo(String value) {
        return new JAXBElement<String>(_Ergastolo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RichiesteGEAnticipazioneEffetti }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "RichiesteGEAnticipazioneEffetti")
    public JAXBElement<RichiesteGEAnticipazioneEffetti> createRichiesteGEAnticipazioneEffetti(RichiesteGEAnticipazioneEffetti value) {
        return new JAXBElement<RichiesteGEAnticipazioneEffetti>(_RichiesteGEAnticipazioneEffetti_QNAME, RichiesteGEAnticipazioneEffetti.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "numeroSentenza")
    public JAXBElement<BigInteger> createNumeroSentenza(BigInteger value) {
        return new JAXBElement<BigInteger>(_NumeroSentenza_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "numeroOrdinanza")
    public JAXBElement<BigInteger> createNumeroOrdinanza(BigInteger value) {
        return new JAXBElement<BigInteger>(_NumeroOrdinanza_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "codiceSedePM")
    public JAXBElement<String> createCodiceSedePM(String value) {
        return new JAXBElement<String>(_CodiceSedePM_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "dataFinePenaAl")
    public JAXBElement<XMLGregorianCalendar> createDataFinePenaAl(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DataFinePenaAl_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LiberazioneAnticipataConcessaDetrarreCumulo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "LiberazioneAnticipataConcessaDetrarreCumulo")
    public JAXBElement<LiberazioneAnticipataConcessaDetrarreCumulo> createLiberazioneAnticipataConcessaDetrarreCumulo(LiberazioneAnticipataConcessaDetrarreCumulo value) {
        return new JAXBElement<LiberazioneAnticipataConcessaDetrarreCumulo>(_LiberazioneAnticipataConcessaDetrarreCumulo_QNAME, LiberazioneAnticipataConcessaDetrarreCumulo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SanzioniGiudicePace }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "SanzioniGiudicePace")
    public JAXBElement<SanzioniGiudicePace> createSanzioniGiudicePace(SanzioniGiudicePace value) {
        return new JAXBElement<SanzioniGiudicePace>(_SanzioniGiudicePace_QNAME, SanzioniGiudicePace.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "chiaveNsc")
    public JAXBElement<Long> createChiaveNsc(Long value) {
        return new JAXBElement<Long>(_ChiaveNsc_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "dataFinePena")
    public JAXBElement<XMLGregorianCalendar> createDataFinePena(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DataFinePena_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChiaviProvvedimentoEsecutivo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it/mig/sies/type/fogliComplementari", name = "ChiaviProvvedimentoEsecutivo")
    public JAXBElement<ChiaviProvvedimentoEsecutivo> createChiaviProvvedimentoEsecutivo(ChiaviProvvedimentoEsecutivo value) {
        return new JAXBElement<ChiaviProvvedimentoEsecutivo>(_ChiaviProvvedimentoEsecutivo_QNAME, ChiaviProvvedimentoEsecutivo.class, null, value);
    }

}
