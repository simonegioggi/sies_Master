package siap.util;

import f3b.util.F3BException;
import f3b.util.PropertiesMgr;

/**
 * NUOVA INFRASTRUTTURA: modificata classe per creazione nuovo file di properties
 * 
 * @author gioggi
 *
 */
public class SIAPPathProperties extends PropertiesMgr {

	private static SIAPPathProperties mSIAPPathProperties = null;

	protected SIAPPathProperties() {
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>PropertiesMgr</code>.
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public static SIAPPathProperties getInstance() {

		try {
			if (mSIAPPathProperties == null) {
				String lPathProp = System.getProperty("path.properties");
				String lNameFile = lPathProp + System.getProperty("file.separator") + "siappath.properties";
				mSIAPPathProperties = new SIAPPathProperties();
				mSIAPPathProperties.setFileProps(lNameFile);
				mSIAPPathProperties.init();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		// valore di ritorno
		return mSIAPPathProperties;
	}

	/**
	 * Ritorna il valore corrispondente alla chiave.
	 * 
	 * @param aName nome chiave del valore desiderato.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public String getProperty(String aName) throws F3BException {

		// valore di ritorno
		return getInstance().readProperty(aName);
	}

	/**
	 * Ritorna il valore corrispondente alla chiave.
	 * <p>
	 * 
	 * @param aName nome chiave del valore desiderato.
	 * @param aDefault valore di default nel caso in cui il valore è un <code>null</code>.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public String getProperty(String aName, String aDefault) throws F3BException {

		// valore di ritorno
		return getInstance().readProperty(aName, aDefault);
	}

	/**
	 * Ritorna
	 * 
	 * @param aName
	 * @return
	 * @throws F3BException
	 */
	public int getIntProperty(String aName) throws F3BException {

		// valore di ritorno
		return getInstance().readIntProperty(aName);
	}

	/**
	 * Ritorna
	 * 
	 * @param aName
	 * @param aDefaultValue
	 * @return
	 * @throws F3BException
	 */
	public int getIntProperty(String aName, int aDefaultValue) throws F3BException {

		// valore di ritorno
		return getInstance().readIntProperty(aName, aDefaultValue);
	}

}