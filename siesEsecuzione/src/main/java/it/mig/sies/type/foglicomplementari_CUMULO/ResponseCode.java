//
// Questo file � stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andr� persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.10.26 alle 04:01:02 PM CEST 
//


package it.mig.sies.type.foglicomplementari_CUMULO;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ResponseCode.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="ResponseCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="201"/>
 *     &lt;enumeration value="202"/>
 *     &lt;enumeration value="203"/>
 *     &lt;enumeration value="204"/>
 *     &lt;enumeration value="205"/>
 *     &lt;enumeration value="206"/>
 *     &lt;enumeration value="207"/>
 *     &lt;enumeration value="400"/>
 *     &lt;enumeration value="401"/>
 *     &lt;enumeration value="402"/>
 *     &lt;enumeration value="403"/>
 *     &lt;enumeration value="404"/>
 *     &lt;enumeration value="405"/>
 *     &lt;enumeration value="406"/>
 *     &lt;enumeration value="407"/>
 *     &lt;enumeration value="408"/>
 *     &lt;enumeration value="409"/>
 *     &lt;enumeration value="410"/>
 *     &lt;enumeration value="411"/>
 *     &lt;enumeration value="412"/>
 *     &lt;enumeration value="413"/>
 *     &lt;enumeration value="414"/>
 *     &lt;enumeration value="500"/>
 *     &lt;enumeration value="501"/>
 *     &lt;enumeration value="502"/>
 *     &lt;enumeration value="503"/>
 *     &lt;enumeration value="504"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResponseCode")
@XmlEnum
public enum ResponseCode {

    @XmlEnumValue("201")
    INSERT_EFFETTUATA("201"),
    @XmlEnumValue("202")
    UPDATE_EFFETTUATA("202"),
    @XmlEnumValue("203")
    DELETE_EFFETTUATA("203"),
    @XmlEnumValue("204")
    WARNING_IN_FASE_DI_INSERIMENTO("204"),
    @XmlEnumValue("205")
    PROBLEMI_CON_LA_GENERAZIONE_ESTRATTO("205"),
    @XmlEnumValue("206")
    ERRORI_IN_CALCOLO_MENZIONABILITA("206"),
    @XmlEnumValue("207")
    ERRORI_IN_VERIFICA_PROVVEDIMENTO("207"),
    @XmlEnumValue("400")
    ERRORE_VALIDAZIONE("400"),
    @XmlEnumValue("401")
    SOGGETTO_NON_TROVATO("401"),
    @XmlEnumValue("402")
    TROVATI_SOGGETTI_MULTIPLI("402"),
    @XmlEnumValue("403")
    INCONGRUENZA_SOGGETTO("403"),
    @XmlEnumValue("404")
    INCONGRUENZA_SOGGETTO_PROVVEDIMENTI_ASSOCIATI("404"),
    @XmlEnumValue("405")
    PROVVEDIMENTO_ESECUTIVO_PRESENTE_NEL_DATABASE("405"),
    @XmlEnumValue("406")
    ANAGRAFICA_SIES_NON_COERENTE_CON_ANAGRAFICA_NSC("406"),
    @XmlEnumValue("407")
    UFFICIO_NON_ABILITATO_ALLA_MODIFICA("407"),
    @XmlEnumValue("408")
    PROVVEDIMENTO_ESECUTIVO_COLLEGATO_AD_ALTRO_PROVVEDIMENTO("408"),
    @XmlEnumValue("409")
    INCONGRUENZA_CHIAVI_PROVVEDIMENTO_NSC_SIES("409"),
    @XmlEnumValue("410")
    CUMULO_NON_TRASFERIBILE("410"),
    @XmlEnumValue("411")
    PROVVEDIMENTO_ESECUTIVO_NON_ASSOCIABILE("411"),
    @XmlEnumValue("412")
    PROVVEDIMENTO_NON_ASSOCIABILE_AL_SOGGETTO("412"),
    @XmlEnumValue("413")
    TROVATI_SOGGETTI_SINONIMI("413"),
    @XmlEnumValue("414")
    ELENCO_PROVVEDIMENTI_CUMULATI_NSC("414"),
    @XmlEnumValue("500")
    ERRORE_INASPETTATO("500"),
    @XmlEnumValue("501")
    ERRORE_IN_DECODIFICA_CODICI_UNIVOCI("501"),
    @XmlEnumValue("502")
    ERRORE_DURANTE_INSERT("502"),
    @XmlEnumValue("503")
    ERRORE_DURANTE_UPDATE("503"),
    @XmlEnumValue("504")
    ERRORE_DURANTE_DELETE("504");
    private final String value;

    ResponseCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResponseCode fromValue(String v) {
        for (ResponseCode c: ResponseCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
