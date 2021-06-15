package siap.sico.saml.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.saml2.encryption.Encrypter;
import org.opensaml.saml2.encryption.Encrypter.KeyPlacement;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.encryption.EncryptionConstants;
import org.opensaml.xml.encryption.EncryptionException;
import org.opensaml.xml.encryption.EncryptionParameters;
import org.opensaml.xml.encryption.KeyEncryptionParameters;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.SecurityTestHelper;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.saml.model.SamlModel;
import siap.sico.webservice.config.NscProperties;
import siap.util.SIAPPathProperties;
import sun.misc.BASE64Encoder;

/**
 * Classe che crea l'assertion Saml con i dati impostati nel SAML Model. * L'assertion viene firmata e
 * criptata. *
 *
 * @author Giselda De Vita
 */
public class SamlMaker {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	SamlUtil mSamlUtil; // Classe di utilità per la creazione dell'assertion
	// SAML
	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// Logger mLog = LogF3B.getLogger();// Log
	private static NscProperties mProperties = NscProperties.getInstance(); // File di properties

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	/**
	 * SamlMaker - costruttore
	 */
	public SamlMaker() {
		try {
			bootstrap();
			mSamlUtil = new SamlUtil();
		} catch (ConfigurationException ex) {
		}

	}

	/**
	 * bootstrap
	 *
	 * @throws org.opensaml.xml.ConfigurationException
	 */
	public static void bootstrap() throws ConfigurationException {

		DefaultBootstrap.bootstrap();
	}

	/**
	 * createSamlAssertion - Crea l'assertion SAML con i dati passati nel SAML Model
	 *
	 * @param lSamlData
	 * @return String Assertion criptata
	 */
	public String createSamlAssertion(SamlModel lSamlData) {

		String cryptedAssertion = "";
		String lReturncryptedAssertion = "";
		try {
			DateTime expectedIssueInstant = new DateTime();
			// expectedIssueInstant.
			String expectedID = mProperties.getProperty("ASSERTION_ID");
			String issuerName = mProperties.getProperty("ISSUER_NAME");

			QName qname = new QName(SAMLConstants.SAML20_NS, Assertion.DEFAULT_ELEMENT_LOCAL_NAME,
					SAMLConstants.SAML20_PREFIX);
			Assertion assertion = (Assertion) mSamlUtil.buildXMLObject(qname);
			assertion.setIssueInstant(expectedIssueInstant);
			assertion.setID(expectedID);
			QName issuerQName = new QName(SAMLConstants.SAML20_NS, Issuer.DEFAULT_ELEMENT_LOCAL_NAME,
					SAMLConstants.SAML20_PREFIX);
			Issuer issuer = (Issuer) mSamlUtil.buildXMLObject(issuerQName);
			issuer.setValue(issuerName);

			assertion.setIssuer(issuer);
			QName nameQName = new QName(SAMLConstants.SAML20_NS, NameID.DEFAULT_ELEMENT_LOCAL_NAME,
					SAMLConstants.SAML20_PREFIX);
			NameID nameID = (NameID) mSamlUtil.buildXMLObject(nameQName);

			/****************************************************/
			/******* Settaggio Dati all'interno dell Assertion ****/
			/****************************************************/
			String expectedName = lSamlData.getIdUtente();
			String expectedNameQualifier = lSamlData.getCognomeUtente();
			String expectedSPNameQualifier = lSamlData.getNomeUtente();
			String expectedUfficio = lSamlData.getCodTipoUfficio() + "#" + lSamlData.getCodSedeUfficio();
			String expectedAddress = lSamlData.getHostAddress();
			// MEV INTEGRAZIONE SIES ADN: aggiunta sezione per variabile UserAdn
			String expectedUserAdn = lSamlData.getUserAdn();
			nameID.setSPProvidedID(expectedUserAdn);
			// FINE MEV INTEGRAZIONE SIES ADN

			nameID.setValue(expectedName);
			nameID.setNameQualifier(expectedNameQualifier);
			nameID.setSPNameQualifier(expectedSPNameQualifier);
			// nameID.setFormat(expectedFormat);
			// nameID.setSPProvidedID(expectedSPID);
			/****************************************************/
			/******* Fine Settaggio Dati all'interno dell'Assertion ****/
			/****************************************************/
			QName subjectQName = new QName(SAMLConstants.SAML20_NS, Subject.DEFAULT_ELEMENT_LOCAL_NAME,
					SAMLConstants.SAML20_PREFIX);
			Subject subject = (Subject) mSamlUtil.buildXMLObject(subjectQName);
			subject.setNameID(nameID);

			QName qnameLocality = new QName(SAMLConstants.SAML20_NS,
					SubjectLocality.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
			SubjectLocality subjectLocality = (SubjectLocality) mSamlUtil.buildXMLObject(qnameLocality);
			subjectLocality.setAddress(expectedAddress);
			subjectLocality.setParent(subject);

			QName qnameConfirmation = new QName(SAMLConstants.SAML20_NS,
					SubjectConfirmationData.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
			SubjectConfirmationData subjectCD = (SubjectConfirmationData) mSamlUtil
					.buildXMLObject(qnameConfirmation);

			subjectCD.setAddress(expectedUfficio);
			subjectCD.setParent(subject);
			// subject.getSubjectConfirmations().add(subjectCD);

			AttributeStatement attributeStatement = setAttribute(lSamlData);
			assertion.getAttributeStatements().add(attributeStatement);
			// signAssertion(assertion);
			storeAssertionToFile(assertion);
			cryptedAssertion = encrypt(assertion);
			// storeAssertionToFile(cryptedAssertion);

			BASE64Encoder encoder = new BASE64Encoder();
			lReturncryptedAssertion = encoder.encode(cryptedAssertion.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return lReturncryptedAssertion;
	}

	/**
	 * Setta gli attributi particolari del sistema SIES
	 *
	 * @param lSamlData
	 * @return AttributeStatement
	 */
	private AttributeStatement setAttribute(SamlModel lSamlData) {

		QName attributeStatementQName = new QName(SAMLConstants.SAML20_NS,
				AttributeStatement.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
		AttributeStatement attributeStatement = (AttributeStatement) mSamlUtil
				.buildXMLObject(attributeStatementQName);

		QName attributeQName = new QName(SAMLConstants.SAML20_NS, Attribute.DEFAULT_ELEMENT_LOCAL_NAME,
				SAMLConstants.SAML20_PREFIX);

		Attribute attribute = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attribute.setName(SamlModel.COD_SEDE_UFFICIO);
		XSStringBuilder stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory()
				.getBuilder(XSString.TYPE_NAME);
		XSString stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,
				XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getCodSedeUfficio());
		attribute.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attribute);

		Attribute attributeTipoUff = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeTipoUff.setName(SamlModel.COD_TIPO_UFFICIO);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getCodTipoUfficio());
		attributeTipoUff.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeTipoUff);

		Attribute attributeCognUtente = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeCognUtente.setName(SamlModel.COGNOME_UTENTE);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getCognomeUtente());
		attributeCognUtente.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeCognUtente);

		Attribute attributeNomeUtente = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeNomeUtente.setName(SamlModel.NOME_UTENTE);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getNomeUtente());
		attributeNomeUtente.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeNomeUtente);

		Attribute attributeDistretto = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeDistretto.setName(SamlModel.DISTRETTO);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getDistretto());
		attributeDistretto.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeDistretto);

		Attribute attributeHostAddress = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeHostAddress.setName(SamlModel.HOST_ADDRESS);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getHostAddress());
		attributeHostAddress.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeHostAddress);

		Attribute attributeIdUtente = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeIdUtente.setName(SamlModel.ID_UTENTE);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getIdUtente());
		attributeIdUtente.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeIdUtente);

		Attribute attributeSistema = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeSistema.setName(SamlModel.SISTEMA);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getSistema());
		attributeSistema.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeSistema);

		// MEV INTEGRAZIONE SIES ADN: aggiunta sezione per variabile UserAdn
		Attribute attributeUserAdn = (Attribute) mSamlUtil.buildXMLObject(attributeQName);
		attributeUserAdn.setName(SamlModel.USER_ADN);
		stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		stringValue.setValue(lSamlData.getUserAdn());
		attributeUserAdn.getAttributeValues().add(stringValue);
		attributeStatement.getAttributes().add(attributeUserAdn);

		return attributeStatement;

	}

	/**
	 * Firma l'Assertion aggiungedo la Signature
	 *
	 * @param assertion
	 * @throws java.security.cert.CertificateException
	 * @throws java.security.KeyStoreException
	 * @throws java.io.IOException
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws org.opensaml.xml.parse.XMLParserException
	 * @throws org.opensaml.xml.io.MarshallingException
	 * @throws org.opensaml.xml.signature.SignatureException
	 */
	// private void signAssertion(Assertion assertion)
	// throws CertificateException, KeyStoreException, IOException,
	// NoSuchAlgorithmException, XMLParserException, MarshallingException,
	// SignatureException, F3BException {
	//
	// SignatureBuilder signatureBuilder = (SignatureBuilder)
	// mSamlUtil.getBuilderFactory().getBuilder(Signature.DEFAULT_ELEMENT_NAME);
	// Signature signature = signatureBuilder.buildObject();
	//
	// /** Credential used for signing.
	// BasicCredential goodCredential = null;
	// String lPathProp = System.getProperty("path.properties");
	// String nomeFileCertificatoX509 = lPathProp
	// + System.getProperty("file.separator")
	// + mProperties.getProperty("CERTIFICATO_X509");
	//
	// java.security.cert.X509Certificate lCert = SecurityTestHelper.buildJavaX509Cert(mSamlUtil
	// .convertFileToString(nomeFileCertificatoX509));
	// X509Credential cred = SecurityHelper.getSimpleCredential(lCert, null);
	// KeyStore keystore = KeyStore.getInstance("JKS");
	//
	// String nomeKeystore = lPathProp + System.getProperty("file.separator")
	// + mProperties.getProperty("KEYSTORE");
	// String aliasKeystore = mProperties.getProperty("KEYSTORE_ALIAS");
	// String pwdCriptata = mProperties.getProperty("PWD");
	// String pwdDecriptata = CryptPassword.decryptPwd(pwdCriptata);
	//
	// keystore.load(new FileInputStream(nomeKeystore), pwdDecriptata.toCharArray());
	// KeyPair keyPair = mSamlUtil.getPrivateKey(keystore, aliasKeystore,
	// pwdDecriptata.toCharArray());
	// goodCredential = SecurityHelper.getSimpleCredential(lCert, keyPair.getPrivate());
	// */
	// String lPathProp = System.getProperty("path.properties");
	// String nomeFileCertificatoX509 = lPathProp + System.getProperty("file.separator") +
	// mProperties.getProperty("CERTIFICATO_X509");
	//
	// java.security.cert.X509Certificate lCert =
	// SecurityTestHelper.buildJavaX509Cert(mSamlUtil.convertFileToString(nomeFileCertificatoX509));
	// Credential credentialNSC = SecurityHelper.getSimpleCredential(lCert, null);
	//
	//
	//
	// signature.setSigningCredential(credentialNSC);
	// signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	// signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA);
	// assertion.setSignature(signature);
	// Marshaller marshaller = mSamlUtil.getMarshallerFactory().getMarshaller(assertion);
	// marshaller.marshall(assertion);
	// Signer.signObject(signature);
	//
	// }

	/**
	 * Memorizza l'Assertion formata su un file
	 *
	 * @param assertion
	 * @throws org.opensaml.xml.io.MarshallingException
	 * @throws org.opensaml.xml.parse.XMLParserException
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException
	 */
	private void storeAssertionToFile(Assertion assertion)
			throws MarshallingException, XMLParserException, FileNotFoundException, IOException {

		// NUOVA INFRASTRUTTURA: aggiunta info per il log
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("<<< ASSERTION >>> : " + assertion);

		Marshaller marshaller = mSamlUtil.getMarshallerFactory().getMarshaller(assertion);
		marshaller.marshall(assertion);

		Element generatedDOM = marshaller.marshall(assertion, SamlUtil.getParser().newDocument());
		String lString = XMLHelper.nodeToString(generatedDOM);

		try {
			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("ASSERTION");
		} catch (F3BException e) {
			e.printStackTrace();
		}
		FileOutputStream lFile = new FileOutputStream(mPath);
		lFile.write(lString.getBytes());
		lFile.flush();
		lFile.close();
	}

	/**
	 * cryptAssertion(Assertion assertion)
	 *
	 * @param assertion
	 */
	// private String cryptAssertion(Assertion assertion)
	// throws MarshallingException, EncryptionException {
	// try {
	// Encrypter encrypter;
	// EncryptionParameters encParams;
	// KeyEncryptionParameters kekParamsRSA;
	//
	// encParams = new EncryptionParameters();
	// String algoURI = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128;
	// algoURI = XMLCipher.AES_256;
	// // String kekExpectedKeyNameRSA = "RSAKeyEncryptionKey";
	// String kekURIRSA = EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15;
	// kekURIRSA = XMLCipher.RSA_OAEP;
	//
	// /* NEW IMPLEMENTATION */
	//
	// // Credenziali NSC da certificato X509
	// String lPathProp = System.getProperty("path.properties");
	// String nomeFileCertificatoX509 = lPathProp + System.getProperty("file.separator") +
	// mProperties.getProperty("CERTIFICATO_X509");
	//
	// java.security.cert.X509Certificate lCert =
	// SecurityTestHelper.buildJavaX509Cert(mSamlUtil.convertFileToString(nomeFileCertificatoX509));
	// Credential credentialNSC = SecurityHelper.getSimpleCredential(lCert, null);
	//
	// Credential symmetricCredential =
	// SecurityHelper.getSimpleCredential(SecurityHelper.generateSymmetricKey(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128));
	//
	// encParams = new EncryptionParameters();
	// //encParams.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
	// encParams.setAlgorithm(XMLCipher.AES_256);
	// encParams.setEncryptionCredential(symmetricCredential);
	//
	// KeyEncryptionParameters kek = new KeyEncryptionParameters();
	// //kek.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
	// kek.setAlgorithm(XMLCipher.RSA_OAEP);
	// kek.setEncryptionCredential(credentialNSC);
	//
	// KeyInfoGenerator lKInfo =
	// SecurityHelper.getKeyInfoGenerator(credentialNSC, null, null);
	// encParams.setKeyInfoGenerator(lKInfo);
	// kekParamsRSA = new KeyEncryptionParameters();
	// kekParamsRSA.setAlgorithm(kekURIRSA);
	// kekParamsRSA.setEncryptionCredential(credentialNSC);
	//
	// List kekParamsList = new ArrayList<KeyEncryptionParameters>();
	// kekParamsList.add(kekParamsRSA);
	//
	// Key encryptionKey =
	// SecurityHelper.extractEncryptionKey(credentialNSC);
	//
	//
	// // encParams = SecurityHelper.buildDataEncryptionParams(null, null, null);
	//
	// //BasicCredential encryptCredential = new BasicCredential();
	// // encryptCredential.setPublicKey(keyPair.getPublic()); // Partner
	// KeyEncryptionParameters kekParams = new KeyEncryptionParameters();
	// kekParams.setEncryptionCredential(credentialNSC);
	// kekParams.setAlgorithm(credentialNSC.getPublicKey().getAlgorithm());
	// kekParams.setAlgorithm(XMLCipher.RSA_v1dot5);
	//
	//
	//
	// // ENCRYPT --- Criptaggio dell'assertion
	// EncryptedAssertion encTarget = null;
	// XMLObject encObject = null;
	//
	// Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	// encrypter = new Encrypter(encParams, kekParams);
	// encrypter.setKeyPlacement(KeyPlacement.INLINE);
	//
	// encObject = encrypter.encrypt(assertion);
	//
	// encTarget = (EncryptedAssertion) encObject;
	//
	// Marshaller marshaller = mSamlUtil.getMarshallerFactory().getMarshaller(encTarget.getEncryptedData());
	// Element generatedDOM = marshaller.marshall(encTarget.getEncryptedData(),
	// mSamlUtil.getParser().newDocument());
	//
	// OutputStream out = null;
	// XMLUtils.serialize(generatedDOM.getOwnerDocument(), out);
	//
	//
	// String lStringCriptata = XMLHelper.nodeToString(generatedDOM);
	//
	//
	// // per test per verificare
	// // decryptAssertion(lStringCriptata, keyPair);
	//
	// return lStringCriptata;
	//
	// } catch (Exception e) {
	// return null;
	// }
	// }

	/**
	 * decryptAssertion - Metodo di prova e test per decriptare un assertion prendendola da un file
	 *
	 * @param lStringCriptata
	 * @param keyPair
	 */
	// private void decryptAssertion(String lStringCriptata, KeyPair keyPair) {
	//
	// try {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	// siesLogger.debug("**************************************************** -->");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	// siesLogger.debug("******DECRIPTOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO******** -->");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	// siesLogger.debug("**************************************************** -->");
	//
	// FileOutputStream lFile = new FileOutputStream("C://temp//prova.xml");
	// FileOutputStream lFile = new FileOutputStream("/var/SIES/temp/prova.xml");
	// lFile.write(lStringCriptata.getBytes());
	// lFile.flush();
	// lFile.close();
	//
	// EncryptedData encryptedTarget = (EncryptedData) mSamlUtil.unmarshallElement("c://temp//prova.xml");
	// EncryptedData encryptedTarget = (EncryptedData)
	// mSamlUtil.unmarshallElement("/var/SIES/temp/prova.xml");
	// EncryptedAssertion lAss = (EncryptedAssertion)
	// mSamlUtil.buildXMLObject(EncryptedAssertion.DEFAULT_ELEMENT_NAME);
	// lAss.setEncryptedData(encryptedTarget);
	// SAMLObject decryptedTarget = null;
	// /*
	// java.security.cert.X509Certificate certificate = SecurityTestHelper
	// .buildJavaX509Cert(mSamlUtil
	// .convertFileToString("c:\\temp\\sies.cer"));
	// X509Credential lcerdential = SecurityHelper.getSimpleCredential(
	// certificate, keyPair.getPrivate());
	// StaticKeyInfoCredentialResolver skicr = new StaticKeyInfoCredentialResolver(
	// lcerdential);
	// Decrypter decrypter = new Decrypter(null, skicr,
	// new InlineEncryptedKeyResolver());
	//
	// decrypter.setJCAProviderName("BC");
	//
	// try {
	// decryptedTarget = decrypter.decrypt(lAss);
	// } catch (Exception e) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	// siesLogger
	// .info("Error on decryption of encrypted SAML 2 type to element: "
	// + e);
	// }*/
	//
	//
	// /////////////////////////////////////////////////////////////////////////////
	// // Credenziali NSC da certificato X509
	// String lPathProp = System.getProperty("path.properties");
	// String nomeFileCertificatoX509 = lPathProp + System.getProperty("file.separator") +
	// mProperties.getProperty("CERTIFICATO_X509");
	//
	// java.security.cert.X509Certificate lCert =
	// SecurityTestHelper.buildJavaX509Cert(mSamlUtil.convertFileToString(nomeFileCertificatoX509));
	// Credential credentialNSC = SecurityHelper.getSimpleCredential(lCert, null);
	//
	//
	//
	// KeyInfoCredentialResolver keyResolver = new StaticKeyInfoCredentialResolver(credentialNSC);
	// EncryptedKey key = lAss.getEncryptedData().getKeyInfo().getEncryptedKeys().get(0);
	//
	// Decrypter decrypter = new Decrypter(null, keyResolver, null);
	// SecretKey dkey = (SecretKey) decrypter.decryptKey(
	// key, lAss.getEncryptedData().getEncryptionMethod().getAlgorithm());
	//
	// Credential shared = SecurityHelper.getSimpleCredential(dkey);
	// decrypter = new Decrypter(new StaticKeyInfoCredentialResolver(credentialNSC), null, null);
	// //return decrypter.decrypt(enc);
	//
	// try {
	// decryptedTarget = decrypter.decrypt(lAss);
	// } catch (Exception e) {
	// }
	// /////////////////////////////////////////////////////////////////////////////
	//
	// Assertion decassertion = (Assertion) decryptedTarget;
	//
	// List lList = decassertion.getAttributeStatements();
	// Iterator itx = lList.iterator();
	// while (itx.hasNext()) {
	// AttributeStatement lStat = (AttributeStatement) itx.next();
	//
	// Iterator Aitx = lStat.getAttributes().iterator();
	// while (Aitx.hasNext()) {
	// Attribute lAttr = (Attribute) Aitx.next();
	// String lStr = lAttr.getAttributeValues().get(0).getDOM().getTagName();
	// }
	// }
	//
	//
	// String lStringDecriptata = XMLHelper.nodeToString(decryptedTarget.getDOM());
	//
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	// siesLogger.info("=================================================================");
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	//
	// }

	public String encrypt(Assertion ass) throws NoSuchAlgorithmException, F3BException, CertificateException,
			KeyException, EncryptionException, MarshallingException, XMLParserException {

		Credential symmetricCredential = SecurityHelper.getSimpleCredential(
				SecurityHelper.generateSymmetricKey(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128));

		EncryptionParameters encParams = new EncryptionParameters();
		encParams.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
		encParams.setEncryptionCredential(symmetricCredential);

		KeyEncryptionParameters kek = new KeyEncryptionParameters();
		String lPathProp = System.getProperty("path.properties");
		String nomeFileCertificatoX509 = lPathProp + System.getProperty("file.separator")
				+ mProperties.getProperty("CERTIFICATO_X509");

		java.security.cert.X509Certificate lCert = SecurityTestHelper
				.buildJavaX509Cert(mSamlUtil.convertFileToString(nomeFileCertificatoX509));
		Credential credentialNSC = SecurityHelper.getSimpleCredential(lCert, null);

		kek.setEncryptionCredential(credentialNSC);
		kek.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);

		/************************************************************/
		/* Key encryptionKey = */SecurityHelper.extractEncryptionKey(encParams.getEncryptionCredential());

		BasicCredential encryptCredential = new BasicCredential();
		encryptCredential.setPublicKey(credentialNSC.getPublicKey()); // Partner

		KeyEncryptionParameters kekParams = new KeyEncryptionParameters();
		kekParams.setEncryptionCredential(encryptCredential);
		kekParams.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);

		/*****************************************************/
		Encrypter encrypter = new Encrypter(encParams, kekParams);
		encrypter.setKeyPlacement(KeyPlacement.INLINE);

		EncryptedAssertion encObject = encrypter.encrypt(ass);
		EncryptedAssertion encTarget = encObject;

		Marshaller marshaller = mSamlUtil.getMarshallerFactory().getMarshaller(encTarget);
		Element generatedDOM = marshaller.marshall(encTarget, SamlUtil.getParser().newDocument());

		String lStringCriptata = XMLHelper.nodeToString(generatedDOM);

		return lStringCriptata;
	}

}