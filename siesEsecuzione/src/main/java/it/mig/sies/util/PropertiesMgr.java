package it.mig.sies.util;

import it.mig.sies.exception.PropertiesException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: PropertiesMgr
 * </p>
 * <p>
 * Description: Classe di gestione della lettura dei files di properties. Infatti questa classe espone i metodi di
 * lettura dei parametri e la fase di load del file stesso.
 * </p>
 * <p>
 * Copyright: BULL Italia S.p.A. Copyright (c) 2003
 * </p>
 * <p>
 * Company: BULL Italia S.p.A.
 * </p>
 */
public class PropertiesMgr {

	protected Properties mProps;
	protected String mFileProps = new String();

	private static final Logger logger = Logger.getLogger(PropertiesMgr.class);

	protected PropertiesMgr() {
	}

	/**
	 * Imposta il nome del file da caricare.
	 * <p>
	 * 
	 * @param aValue
	 *            nome del file di properties.
	 */
	protected void setFileProps(String aValue) {

		mFileProps = (aValue == null ? "" : aValue);
	}

	/**
	 * Ritorna il nome dei file di properties.
	 * <p>
	 * 
	 * @return nome del file di properties
	 */
	protected String getFileProps() {

		return mFileProps;
	}

	/**
	 * Metodo che legge un valore attraverso la propria chiave.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave per valore.
	 * @return il valore corrispondente alla chiave.
	 */
	protected String readProperty(String aKey) {

		return mProps.getProperty(aKey);
	}

	/**
	 * Metodo che ritorna il valore corrispondente alla chiave passata come attributo, inoltre nel caso in cui non
	 * esistesse, il valore corrispondente ritorna quello di default, anch'esso passato come attributo.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave per valore.
	 * @param aDefaultValue
	 *            valore di default.
	 * @return il valore corrispondente alla chiave.
	 */
	protected String readProperty(String aKey, String aDefaultValue) {

		return mProps.getProperty(aKey, aDefaultValue);
	}

	/**
	 * Carica il file di properties desiderato ed inizializza l'oggetto properties.
	 * <p>
	 * 
	 * @throws F3BException
	 *             rilancia l'errore di eccezione.
	 */
	protected void init() throws PropertiesException {

		logger.info("PropertiesMgr.init(): mFileProps " + mFileProps);
		FileInputStream lFis = null;
		try {
			mProps = new Properties();
			lFis = new FileInputStream(mFileProps.equals("") ? "./f3b.properties" : mFileProps);
			this.mProps.load(lFis);
		} catch (IOException ioex) {
			throw new PropertiesException("Errore nella fase di load del file di properties");
		} finally {
			try {
				if (lFis != null)
					lFis.close();
			} catch (IOException ioex) {
				throw new PropertiesException("Errore nella fase di chiusura del file");
			}
		}
	}

	/**
	 * Metodo che legge un valore attraverso la propria chiave.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave per valore.
	 * @return il valore corrispondente alla chiave.
	 */
	protected synchronized int readIntProperty(String aKey) throws PropertiesException {

		int lIntValue = 0;

		try {
			lIntValue = Integer.parseInt(mProps.getProperty(aKey));
		} catch (NumberFormatException nfex) {
			throw new PropertiesException("Non ï¿½ possibile convertire la chiave " + aKey + " con valore = "
					+ mProps.getProperty(aKey) + " in intero!");
		}

		return lIntValue;
	}

	/**
	 * Metodo che ritorna il valore corrispondente alla chiave come intero passata come attributo, inoltre nel caso in
	 * cui non esistesse, il valore corrispondente ritorna quello di default, anch'esso passato come attributo.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave per valore.
	 * @param aDefaultValue
	 *            valore di default.
	 * @return il valore corrispondente alla chiave.
	 */
	protected int readIntProperty(String aKey, int aDefaultValue) throws PropertiesException {

		int lIntValue = 0;

		try {
			String lStrValue = mProps.getProperty(aKey);

			if (lStrValue == null)
				lIntValue = aDefaultValue;
			else
				lIntValue = Integer.parseInt(lStrValue);
		} catch (NumberFormatException nfex) {
			throw new PropertiesException("Non ï¿½ possibile convertire la stringa in intero!");
		}
		return lIntValue;
	}

	/**
	 * Permette di impostare un parametro
	 * 
	 * @param aKey
	 * @param aValue
	 */
	protected void writeProperty(String aKey, String aValue) {

		mProps.setProperty(aKey, aValue);
	}
}