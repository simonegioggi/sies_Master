package f3b.model;

/**
 * <p>
 * Title: DecodeModel
 * </p>
 * <p>
 * Description: Model per la gestione delle combobox di decodifica o per decodifiche di uso generico.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class DecodeModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -391113472091151634L;

	protected String mCode;
	protected String mDescription;

	/**
	 * Costruttore di classe.
	 */
	public DecodeModel() {
	}

	/**
	 * Costruttore di model con parametri.
	 * <p>
	 * 
	 * @param aCode
	 *            codice.
	 * @param aDescription
	 *            descrizione.
	 */
	public DecodeModel(String aCode, String aDescription) {
		this.mCode = aCode;
		this.mDescription = aDescription;
	}

	/**
	 * Ritorna il codice.
	 * <p>
	 * 
	 * @return il codice
	 */
	public String getCode() {
		return this.mCode;
	}

	/**
	 * Ritorna la descrizione.
	 * <p>
	 * 
	 * @return la descrizione.
	 */
	public String getDescription() {
		return this.mDescription;
	}

	/**
	 * Imposta il codice al relativo membro di classe.
	 * <p>
	 * 
	 * @param aValue
	 *            valore.
	 */
	public void setCode(String aValue) {
		this.mCode = aValue;
	}

	/**
	 * Imposta la descrizione al relativo membro di classe.
	 * <p>
	 * 
	 * @param aValue
	 *            valore.
	 */
	public void setDescription(String aValue) {
		this.mDescription = aValue;
	}

}