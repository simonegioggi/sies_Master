package f3b.util;

/**
 * <p>
 * Title: F3BProperties
 * </p>
 * <p>
 * Description: Classe di gestione del file di properties di F3B
 * </p>
 * <p>
 * Copyright: Eunics Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eunics
 * </p>
 */
public class F3BProperties extends PropertiesMgr {

	private static F3BProperties mF3BProperties = null;

	protected F3BProperties() {
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>PropertiesMgr</code>.
	 * <p>
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static F3BProperties getInstance() throws F3BException {
		if (mF3BProperties == null) {
			String lPathProp = System.getProperty("path.properties");
			String lNameFile = lPathProp + System.getProperty("file.separator") + "f3b.properties";
			mF3BProperties = new F3BProperties();
			mF3BProperties.setFileProps(lNameFile);
			mF3BProperties.init();
		}

		return mF3BProperties;
	}

	public static F3BProperties getInstance(String aPropFile) throws F3BException {
		if (mF3BProperties == null) {
			mF3BProperties = new F3BProperties();
			mF3BProperties.setFileProps(aPropFile);
			mF3BProperties.init();
		}
		return mF3BProperties;
	}

	/**
	 * Ritorna il valore corrispondente alla chiave.
	 * <p>
	 * 
	 * @param aName
	 *            nome chiave del valore desiderato.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static String getProperty(String aName) throws F3BException {
		return getInstance().readProperty(aName);
	}

	/**
	 * Ritorna il valore corrispondente alla chiave.
	 * <p>
	 * 
	 * @param aName
	 *            nome chiave del valore desiderato.
	 * @param aDefault
	 *            valore di default nel caso in cui il valore è un <code>null</code>.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static String getProperty(String aName, String aDefault) throws F3BException {
		return getInstance().readProperty(aName, aDefault);
	}

	/**
	 * Ritorna un valore del properties convertito come intero.
	 * <p>
	 * 
	 * @param aName
	 *            nome chiave del parametro.
	 * @return valore del parametro convertito in <code>int</code>.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public static int getIntProperty(String aName) throws F3BException {
		return getInstance().readIntProperty(aName);
	}

	/**
	 * Ritorna un valore del properties convertito come intero. Nel caso in cui il parametro non sia stato
	 * definito ritorna il valore di default, passato come argomento.
	 * <p>
	 * 
	 * @param aName
	 *            nome chiave parametro.
	 * @param aDefaultValue
	 *            valore di default
	 * @return valore del parametro convertito in <code>int</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static int getIntProperty(String aName, int aDefaultValue) throws F3BException {
		return getInstance().readIntProperty(aName, aDefaultValue);
	}

	/**
	 * 
	 * @param aName
	 * @param aValue
	 * @throws F3BException
	 */
	public static synchronized void setProperty(String aName, String aValue) throws F3BException {
		getInstance().writeProperty(aName, aValue);
	}

}