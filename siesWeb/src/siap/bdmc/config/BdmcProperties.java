package siap.bdmc.config;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.PropertiesMgr;

/**
 * Classe che estende il properties manager per la gestione del file stampa.properties dove sono contenuti
 * tutte le frasi fisse statiche che servono all'interno della stampa dei template.
 * 
 * @author Giselda De Vita
 *
 */
public class BdmcProperties extends PropertiesMgr {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private static BdmcProperties mBdmcProperties = null;
//	private Properties mConnectionString;

	protected BdmcProperties() {
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>PropertiesMgr</code>.
	 * <p>
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static BdmcProperties getInstance() {
		try {
			if (mBdmcProperties == null) {
				mBdmcProperties = new BdmcProperties();
				mBdmcProperties.initialize();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mBdmcProperties;
	}

	/**
	 * inizializza gli attributi privati del Singleton
	 */
	private void initialize() throws F3BException {
		String lPathProp = System.getProperty("path.properties");
		String lNameFile = lPathProp + System.getProperty("file.separator") + "bdmc.properties";
		mBdmcProperties.setFileProps(lNameFile);
		mBdmcProperties.init();
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
	public String getProperty(String aName) throws F3BException {

		String lProperty = getInstance().readProperty(aName);

		if (lProperty == null || (lProperty != null && lProperty.length() == 0))
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("\n Proprietà <" + aName + "> non trovata nel file bdmc.properties!");

		return lProperty;
	}

	/**
	 * <p>
	 * * Ritorna il valore corrispondente alla chiave.
	 * 
	 * @param aName
	 *            nome chiave del valore desiderato.
	 * @param aDefault
	 *            valore di default nel caso in cui il valore è un <code>null</code>.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public String getProperty(String aName, String aDefault) throws F3BException {
		return getInstance().readProperty(aName, aDefault);
	}

	public int getIntProperty(String aName) throws F3BException {
		return getInstance().readIntProperty(aName);
	}

	public int getIntProperty(String aName, int aDefaultValue) throws F3BException {
		return getInstance().readIntProperty(aName, aDefaultValue);
	}

}