package siap.sico.inviosegnalazione.config;

import f3b.util.F3BException;
import f3b.util.PropertiesMgr;

/**
 * MEV10-s3: aggiunta classe per gestire l'invio di una segnalazione
 * 
 * @author sgioggi
 * @version
 */
public class ISProperties extends PropertiesMgr {

	private static ISProperties mISProperties = null;

	protected ISProperties() {}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe
	 * <code>PropertiesMgr</code>.
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public static ISProperties getInstance() throws F3BException {

		if (mISProperties == null) {
			String lPathProp = System.getProperty("path.properties");
			String lNameFile = lPathProp + System.getProperty("file.separator") + "inviosegnalazione.properties";
			mISProperties = new ISProperties();
			mISProperties.setFileProps(lNameFile);
			mISProperties.init();
		}
		// valore di ritorno
		return mISProperties;
	}

	/**
	 * Ritorna il valore corrispondente alla chiave.
	 * 
	 * @param aName nome chiave del valore desiderato.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public static String getProperty(String aName) throws F3BException {

		// valore di ritorno
		return getInstance().readProperty(aName);
	}

	/**
	 * Ritorna il valore corrispondente alla chiave.
	 * 
	 * @param aName nome chiave del valore desiderato.
	 * @param aDefault valore di default nel caso in cui il valore è un <code>null</code>.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public static String getProperty(String aName, String aDefault)
			throws F3BException {

		// valore di ritorno
		return getInstance().readProperty(aName, aDefault);
	}

	/**
	 * Ritorna
	 * 
	 * @param aName
	 * @return int
	 * @throws F3BException
	 */
	public static int getIntProperty(String aName) throws F3BException {

		// valore di ritorno
		return getInstance().readIntProperty(aName);
	}

	/**
	 * Ritorna
	 * 
	 * @param aName
	 * @param aDefaultValue
	 * @return int
	 * @throws F3BException
	 */
	public static int getIntProperty(String aName, int aDefaultValue)
			throws F3BException {

		// valore di ritorno
		return getInstance().readIntProperty(aName, aDefaultValue);
	}

}