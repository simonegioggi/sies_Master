package it.mig.sies.type.esecuzione_NEW;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ResponseCode.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;ResponseCode&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;201&quot;/&gt;
 *     &lt;enumeration value=&quot;202&quot;/&gt;
 *     &lt;enumeration value=&quot;203&quot;/&gt;
 *     &lt;enumeration value=&quot;204&quot;/&gt;
 *     &lt;enumeration value=&quot;205&quot;/&gt;
 *     &lt;enumeration value=&quot;206&quot;/&gt;
 *     &lt;enumeration value=&quot;207&quot;/&gt;
 *     &lt;enumeration value=&quot;400&quot;/&gt;
 *     &lt;enumeration value=&quot;401&quot;/&gt;
 *     &lt;enumeration value=&quot;402&quot;/&gt;
 *     &lt;enumeration value=&quot;403&quot;/&gt;
 *     &lt;enumeration value=&quot;404&quot;/&gt;
 *     &lt;enumeration value=&quot;405&quot;/&gt;
 *     &lt;enumeration value=&quot;406&quot;/&gt;
 *     &lt;enumeration value=&quot;407&quot;/&gt;
 *     &lt;enumeration value=&quot;408&quot;/&gt;
 *     &lt;enumeration value=&quot;409&quot;/&gt;
 *     &lt;enumeration value=&quot;410&quot;/&gt;
 *     &lt;enumeration value=&quot;411&quot;/&gt;
 *     &lt;enumeration value=&quot;412&quot;/&gt;
 *     &lt;enumeration value=&quot;413&quot;/&gt;
 *     &lt;enumeration value=&quot;500&quot;/&gt;
 *     &lt;enumeration value=&quot;501&quot;/&gt;
 *     &lt;enumeration value=&quot;502&quot;/&gt;
 *     &lt;enumeration value=&quot;503&quot;/&gt;
 *     &lt;enumeration value=&quot;504&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ResponseCode")
@XmlEnum
public enum ResponseCode {

	@XmlEnumValue("201")
	INSERT_EFFETTUATA("201"), @XmlEnumValue("202")
	UPDATE_EFFETTUATA("202"), @XmlEnumValue("203")
	DELETE_EFFETTUATA("203"), @XmlEnumValue("204")
	WARNING_IN_FASE_DI_INSERIMENTO("204"), @XmlEnumValue("205")
	PROBLEMI_CON_LA_GENERAZIONE_ESTRATTO("205"), @XmlEnumValue("206")
	ERRORI_IN_CALCOLO_MENZIONABILITA("206"), @XmlEnumValue("207")
	ERRORI_IN_VERIFICA_PROVVEDIMENTO("207"), @XmlEnumValue("400")
	ERRORE_VALIDAZIONE("400"), @XmlEnumValue("401")
	SOGGETTO_NON_TROVATO("401"), @XmlEnumValue("402")
	TROVATI_SOGGETTI_MULTIPLI("402"), @XmlEnumValue("403")
	INCONGRUENZA_SOGGETTO("403"), @XmlEnumValue("404")
	INCONGRUENZA_SOGGETTO_PROVVEDIMENTI_ASSOCIATI("404"), @XmlEnumValue("405")
	PROVVEDIMENTO_ESECUTIVO_PRESENTE_NEL_DATABASE("405"), @XmlEnumValue("406")
	ANAGRAFICA_SIES_NON_COERENTE_CON_ANAGRAFICA_NSC("406"), @XmlEnumValue("407")
	UFFICIO_NON_ABILITATO_ALLA_MODIFICA("407"), @XmlEnumValue("408")
	PROVVEDIMENTO_ESECUTIVO_COLLEGATO_AD_ALTRO_PROVVEDIMENTO("408"), @XmlEnumValue("409")
	INCONGRUENZA_CHIAVI_PROVVEDIMENTO_NSC_SIES("409"), @XmlEnumValue("410")
	CUMULO_NON_TRASFERIBILE("410"), @XmlEnumValue("411")
	PROVVEDIMENTO_ESECUTIVO_NON_ASSOCIABILE("411"), @XmlEnumValue("412")
	PROVVEDIMENTO_NON_ASSOCIABILE_AL_SOGGETTO("412"), @XmlEnumValue("413")
	TROVATI_SOGGETTI_SINONIMI("413"), @XmlEnumValue("500")
	ERRORE_INASPETTATO("500"), @XmlEnumValue("501")
	ERRORE_IN_DECODIFICA_CODICI_UNIVOCI("501"), @XmlEnumValue("502")
	ERRORE_DURANTE_INSERT("502"), @XmlEnumValue("503")
	ERRORE_DURANTE_UPDATE("503"), @XmlEnumValue("504")
	ERRORE_DURANTE_DELETE("504");
	private final String value;

	ResponseCode(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static ResponseCode fromValue(String v) {
		for (ResponseCode c : ResponseCode.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
