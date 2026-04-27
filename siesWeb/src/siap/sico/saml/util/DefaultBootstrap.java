package siap.sico.saml.util;

import java.io.File;

import org.apache.log4j.Logger;
import org.opensaml.Configuration;
import org.opensaml.saml1.binding.artifact.SAML1ArtifactBuilderFactory;
import org.opensaml.saml2.binding.artifact.SAML2ArtifactBuilderFactory;
import org.opensaml.xml.ConfigurationException;
//import org.opensaml.xml.XMLConfigurator;
import org.opensaml.xml.security.DefaultSecurityConfigurationBootstrap;

import siap.sico.webservice.config.NscProperties;

import org.apache.xml.security.Init;

import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * This class can be used to bootstrap the OpenSAML library with the default configurations that ship with the
 * library.
 */
public class DefaultBootstrap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// public static Logger mLog = LogF3B.getLogger();
	private static NscProperties mProperties = NscProperties.getInstance();
//	private static SAMLVersion expectedVersion;

	/** List of default XMLTooling configuration files. */
	// private static String[] xmlToolingConfigs;
	/** Constructor. */
	public DefaultBootstrap() {
	}

	/**
	 * Initializes the OpenSAML library, loading default configurations.
	 * 
	 * @throws ConfigurationException
	 *             thrown if there is a problem initializing the OpenSAML library
	 */
	public static synchronized void bootstrap() throws ConfigurationException {
		try {

//			expectedVersion = SAMLVersion.VERSION_20;
			String lPathProp = System.getProperty("path.properties");
			String lNameFile = lPathProp + System.getProperty("file.separator");

			String defaultConfig = lNameFile + mProperties.getProperty("DEFAULT_CONFIG");
			String schemaConfig = lNameFile + mProperties.getProperty("SCHEMA_CONFIG");
			String signatureConfig = lNameFile + mProperties.getProperty("SIGNATURE_CONFIG");
			String signatureValidationConfig = lNameFile
					+ mProperties.getProperty("SIGNATURE_VALIDATION_CONFIG");
			String encryptionConfig = lNameFile + mProperties.getProperty("ENCRYPTION_CONFIG");
			String encryptionValidationConfig = lNameFile
					+ mProperties.getProperty("ENCRYPTION_VALIDATION_CONFIG");
			String soap11Config = lNameFile + mProperties.getProperty("SOAP11_CONFIG");
			String assertionConfig = lNameFile + mProperties.getProperty("ASSERTION_CONFIG");

			String[] xmlToolingConfigs = { defaultConfig, schemaConfig, signatureConfig,
					signatureValidationConfig, encryptionConfig, encryptionValidationConfig, soap11Config,
					assertionConfig };

			initializeXMLSecurity();
			initializeXMLTooling(xmlToolingConfigs);
			initializeArtifactBuilderFactories();
			initializeGlobalSecurityConfiguration();

		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nell'Inizializzazione del  SAML", ex);
		}
	}

	/**
	 * Initializes the default global security configuration.
	 */
	protected static void initializeGlobalSecurityConfiguration() {
		Configuration.setGlobalSecurityConfiguration(DefaultSecurityConfigurationBootstrap
				.buildDefaultConfig());
	}

	/**
	 * Initializes the Apache XMLSecurity libary.
	 * 
	 * @throws ConfigurationException
	 *             thrown is there is a problem initializing the library
	 */
	protected static void initializeXMLSecurity() throws ConfigurationException {
		if (!Init.isInitialized()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Initializing Apache XMLSecurity library");
			Init.init();
		}
	}

	/**
	 * Initializes the XMLTooling library with a default set of object providers.
	 * 
	 * @param providerConfigs
	 *            list of provider configuration files located on the classpath
	 * 
	 * @throws ConfigurationException
	 *             thrown if there is a problem loading the configuration files
	 */
	protected static void initializeXMLTooling(String[] providerConfigs) throws ConfigurationException {
//		Class clazz = Configuration.class;

		XMLConfigurator configurator = new XMLConfigurator();

		for (String config : providerConfigs) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Loading XMLTooling configuration {}" + config);

			File ltest2 = new File(config);

			if (ltest2.exists()) {
				configurator.load(ltest2);
			}
		}

	}

	/**
	 * Initializes the artifact factories for SAML 1 and SAML 2 artifacts.
	 * 
	 * @throws ConfigurationException
	 *             thrown if there is a problem initializing the artifact factory
	 */
	protected static void initializeArtifactBuilderFactories() throws ConfigurationException {
		Configuration.setSAML1ArtifactBuilderFactory(new SAML1ArtifactBuilderFactory());
		Configuration.setSAML2ArtifactBuilderFactory(new SAML2ArtifactBuilderFactory());
	}

}