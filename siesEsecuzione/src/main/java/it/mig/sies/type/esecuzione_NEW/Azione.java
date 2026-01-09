package it.mig.sies.type.esecuzione_NEW;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Azione.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;Azione&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;INSERT&quot;/&gt;
 *     &lt;enumeration value=&quot;UPDATE&quot;/&gt;
 *     &lt;enumeration value=&quot;DELETE&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Azione")
@XmlEnum
public enum Azione {

	INSERT, UPDATE, DELETE;

	public String value() {
		return name();
	}

	public static Azione fromValue(String v) {
		return valueOf(v);
	}

}
