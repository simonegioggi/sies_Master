package it.mig.sies.util;

import it.mig.sies.exception.PropertiesException;

import org.apache.log4j.Logger;

/**
 * Classe che estende il properties manager per la gestione del file nsc.properties dove sono contenuti i parametri per
 * la connessio al casellario (NSC)
 * 
 * @author Paolo Cherubini
 *
 */
public class NscProperties extends PropertiesMgr {

	private static NscProperties mNscProperties = null;

	private static final Logger logger = Logger.getLogger(NscProperties.class);

	protected NscProperties() {
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>PropertiesMgr</code>.
	 * <p>
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static NscProperties getInstance() {

		try {
			if (mNscProperties == null) {
				mNscProperties = new NscProperties();
				mNscProperties.initialize();
			}
		} catch (Exception ex) {
			logger.info("Errore in NscProperties");
			ex.printStackTrace();
		}

		return mNscProperties;
	}

	/**
	 * inizializza gli attributi privati del Singleton
	 */
	private void initialize() throws PropertiesException {

		String lPathProp = System.getProperty("path.properties");
		String lNameFile = lPathProp + System.getProperty("file.separator") + "nsc.properties";
		mNscProperties.setFileProps(lNameFile);
		mNscProperties.init();
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
	public String getProperty(String aName) throws PropertiesException {

		String lProperty = getInstance().readProperty(aName);

		if (lProperty == null || (lProperty != null && lProperty.length() == 0))
			logger.info("\n Proprietï¿½ <" + aName + "> non trovata nel file Nsc.properties!");

		return lProperty;
	}

	/**
	 * <p>
	 * * Ritorna il valore corrispondente alla chiave.
	 * 
	 * @param aName
	 *            nome chiave del valore desiderato.
	 * @param aDefault
	 *            valore di default nel caso in cui il valore ï¿½ un <code>null</code>.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public String getProperty(String aName, String aDefault) throws PropertiesException {

		return getInstance().readProperty(aName, aDefault);
	}

	public int getIntProperty(String aName) throws PropertiesException {

		return getInstance().readIntProperty(aName);
	}

	public int getIntProperty(String aName, int aDefaultValue) throws PropertiesException {

		return getInstance().readIntProperty(aName, aDefaultValue);
	}

}