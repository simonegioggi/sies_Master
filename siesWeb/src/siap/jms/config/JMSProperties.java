package siap.jms.config;

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import siap.jms.jmscode.controller.JmsCodeController;
import siap.jms.jmscode.model.JmsCodeModel;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import f3b.util.PropertiesMgr;

/**
 * <p>
 * Title: JMSProperties
 * </p>
 * <p>
 * Description: Classe singleton che ricerca, memorizza e le proprietà statiche utili al funzionamento di
 * OpenJMS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JMSProperties extends PropertiesMgr {

	private static JMSProperties mJMSProperties = null;
	private Vector mAllBDI;
	private Vector mProgrBDI;
	private String mNomeBDI;
	private Properties mConnectionString;

	private static boolean MCheckProgressivo;
	private String errore = "";

	protected JMSProperties() {
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>PropertiesMgr</code>.
	 * <p>
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static JMSProperties getInstance() throws F3BException {
		if (mJMSProperties == null) {
			mJMSProperties = new JMSProperties();
			mJMSProperties.initialize();
		}

		return mJMSProperties;
	}

	/**
	 * inizializza gli attributi privati del Singleton
	 */
	private void initialize() throws F3BException {
		String lPathProp = System.getProperty("path.properties");
		String lNameFile = lPathProp + System.getProperty("file.separator") + "siapjms.properties";
		mJMSProperties.setFileProps(lNameFile);
		mJMSProperties.init();

		// Riempio il vector AllBDI
		JmsCodeController lCrtl = new JmsCodeController();
		mAllBDI = new Vector(lCrtl.ExRicercaAllBDI());

		// Riempio il vector delle stringhe di connessione
		mConnectionString = new Properties();
		Vector lConnStrings = lCrtl.ExRicercaPerDominio("CONN_JMS_STRING");
		if (lConnStrings != null) {
			Iterator lItx = lConnStrings.iterator();
			while (lItx.hasNext()) {
				JmsCodeModel lJmsCode = (JmsCodeModel) lItx.next();
				if (lJmsCode != null)
					mConnectionString.setProperty(lJmsCode.getCodice(), lJmsCode.getDescrizione());
			}

		}

		mProgrBDI = new Vector(lCrtl.ExRicercaProgressiviBDI());
		ControlloProgressivo();
		MCheckProgressivo = true;
	}

	/**
	 * Controlla che la descrizione inserita nel file f3b.properties sia uguale a quella inserita nella
	 * tabella JMS_CODE.
	 * <p>
	 * 
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private void ControlloProgressivo() throws F3BException {
		String lStrSeq = F3BProperties.getProperty("sequence.command");
		String lCodiceBDI = F3BProperties.getProperty("Bdi.DistrictCode");
		String lBDINome = this.getProperty("JMS_LOCAL_MITTENTE");

		Iterator lItx = mAllBDI.iterator();
		while (lItx.hasNext()) {
			JmsCodeModel lJmsCode = (JmsCodeModel) lItx.next();
			if (lJmsCode.getCodice() == lCodiceBDI)
				lBDINome = lJmsCode.getDescrizione();

		}
		mNomeBDI = lBDINome;
		if (lStrSeq != null) {
			int lIntProgressivo = lStrSeq.indexOf("'");
			String lProgressivo = "";
			if (lIntProgressivo > 0) {
				lProgressivo = lStrSeq.substring(lIntProgressivo + 1, lIntProgressivo + 3);

			}

			Iterator lIProgDBI = mProgrBDI.iterator();
			while (lIProgDBI.hasNext()) {
				JmsCodeModel lCode = (JmsCodeModel) lIProgDBI.next();
				// Confronta il Codice
				if (lBDINome.compareTo(lCode.getCodice()) == 0) {
					// Confronta la Descrizione
					if (lProgressivo.compareTo(lCode.getDescrizione()) != 0) {
						MCheckProgressivo = false;

						errore = errore + "<p class=\"CRosso\">Attenzione!!!</p>";
						errore = errore
								+ "<p class=\"input\">Progressivo ERRATO.<BR><BR>Nel file di configurazione è presente <span class=\"CRosso\">"
								+ lProgressivo + "</span>";
						errore = errore + "<BR>ma dovrebbe essere <span class=\"CRosso\">"
								+ lCode.getDescrizione() + "</span>.<BR><BR>";
						errore = errore
								+ "Provvedere alla sostituzione del paramentro<BR>nel file <span class=\"CRosso\">f3b.properties</span> nella directory<BR> ";
						errore = errore + "<span class=\"CRosso\">" + System.getProperty("path.properties")
								+ "</span></p>";
						throw new F3BException(F3BException.USER_MESSAGE, errore);
					}
				}

			}
		}

	}

	public String getChekProgressivo() throws F3BException {

		if (MCheckProgressivo == false) {
			throw new F3BException(F3BException.USER_MESSAGE, errore);
		} else {
			return null;
		}

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
		return getInstance().readProperty(aName);
	}

	/**
	 * Restituisce la stringa di connessione associata ad un determinato destinatario.
	 * <p>
	 * 
	 * @param aName
	 *            nome chiave del valore desiderato.
	 * @return valore corrispondente alla chiave.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public String getConnectionString(String aDestinatario) throws F3BException {
		String lConnection = "";

		if (mConnectionString != null)
			lConnection = getInstance().mConnectionString.getProperty(aDestinatario);

		return lConnection;
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

	public Vector getAllBDI() throws F3BException {
		return mAllBDI;
	}

	public Vector getProgressiviBDI() throws F3BException {
		return mProgrBDI;
	}

	public String getNomeBDI() throws F3BException {
		return mNomeBDI;
	}

}