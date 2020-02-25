package f3b.web;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>
 * Title: RedirecTo
 * </p>
 * <p>
 * Description: la classe imposta l'azione da richiamare prima che la classe dell'azione specifica ritorni la
 * pagina di view, infatti attraverso questa classe è possibile impostare l'azione con i relativi parametri da
 * poi richiamare nelle varie JSP.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:Bull Italia S.p.A.
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedirectTo {

	private String mAction = new String();
	private String mPage = new String();
	private Hashtable mParameters = new Hashtable();

	/**
	 * Imposta l'azione da richiamare.
	 * <p>
	 * 
	 * @param aName
	 *            l'azione da richiamare.
	 */
	public void setAction(String aName) {
		this.mAction = aName;
	}

	/**
	 * Imposta la pagina.
	 * <p>
	 * 
	 * @param aName
	 *            nome della pagina.
	 */
	public void setPage(String aName) {
		this.mPage = aName;
	}

	/**
	 * Imposta i parametri da aggiungere all'azione.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave del valore.
	 * @param aValue
	 *            valore da impostare
	 */
	public void setParameter(String aKey, String aValue) {
		this.mParameters.put(aKey, aValue);
	}

	/**
	 * Ritorna l'azione con tutti i parametri desiderati da richiamare nella pagina JSP.
	 * <p>
	 * 
	 * @return azione da richiamare.
	 */
	public String toString() {

		String lString = null;
		Enumeration lEnum = null;
		String lKey = null;

		lString = this.mPage + "?Action=" + this.mAction;
		lEnum = this.mParameters.keys();

		while (lEnum.hasMoreElements()) {
			lKey = (String) lEnum.nextElement();
			lString += "&" + lKey + "=" + mParameters.get(lKey);
		}
		return lString;
	}

}