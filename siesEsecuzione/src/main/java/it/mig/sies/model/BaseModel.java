package it.mig.sies.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * SIES FASE 2 - Classe base per i model
 * 
 * @author Federico Paparoni
 */
public class BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7926446345272299718L;

	/**
	 * Implementazione del metodo toString che viene ereditata da tutte le classi figlie
	 */
	public String toString() {

		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}