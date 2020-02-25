package it.eng.giustizia.avvocatura.util;

import f3b.util.F3BException;
import f3b.util.PropertiesMgr;

/**
 * MEV AVVOCATURA: aggiunta classe per gestire gli errori
 * 
 * @author sgioggi
 */
public class AvvocaturaProperties extends PropertiesMgr {

	private static AvvocaturaProperties avvocaturaProperties = null;
	public final static String PREFISSO_ERRORI = "errore.";
	public final static String PREFISSO_PATH = "path.";

	protected AvvocaturaProperties() {
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>AvvocaturaProperties</code>.
	 * 
	 * @return l'istanza di <code>AvvocaturaProperties</code>.
	 * @throws F3BException propaga l'errore di eccezione.
	 */
	public static AvvocaturaProperties getInstance() throws F3BException {

		if (avvocaturaProperties == null) {
			String lPathProp = System.getProperty("path.properties");
			String lNameFile = lPathProp + System.getProperty("file.separator") + "avvocatura.properties";
			avvocaturaProperties = new AvvocaturaProperties();
			avvocaturaProperties.setFileProps(lNameFile);
			avvocaturaProperties.init();
		}
		// valore di ritorno
		return avvocaturaProperties;
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
	public static String getProperty(String aName, String aDefault) throws F3BException {

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
	public static int getIntProperty(String aName, int aDefaultValue) throws F3BException {

		// valore di ritorno
		return getInstance().readIntProperty(aName, aDefaultValue);
	}

}