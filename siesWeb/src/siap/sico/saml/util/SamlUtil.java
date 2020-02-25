/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package siap.sico.saml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.opensaml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.misc.BASE64Encoder;
import f3b.log.LogF3B;

/**
 * Intermediate class that serves to initialize the configuration environment for other base test classes.
 */
@SuppressWarnings("rawtypes")
public class SamlUtil { // extends XMLTestCase

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/** Parser manager used to parse XML. */
	protected static BasicParserPool parser;
	/** XMLObject builder factory. */
	protected static XMLObjectBuilderFactory builderFactory;
	/** XMLObject marshaller factory. */
	protected static MarshallerFactory marshallerFactory;
	/** XMLObject unmarshaller factory. */
	protected static UnmarshallerFactory unmarshallerFactory;

	public MarshallerFactory getMarshallerFactory() {
		return marshallerFactory;
	}

	public static void setMarshallerFactory(MarshallerFactory marshallerFactory) {
		SamlUtil.marshallerFactory = marshallerFactory;
	}

	public static BasicParserPool getParser() {
		return parser;
	}

	public static void setParser(BasicParserPool parser) {
		SamlUtil.parser = parser;
	}

	public static UnmarshallerFactory getUnmarshallerFactory() {
		return unmarshallerFactory;
	}

	public static void setUnmarshallerFactory(UnmarshallerFactory unmarshallerFactory) {
		SamlUtil.unmarshallerFactory = unmarshallerFactory;
	}

	/** Class logger. */
	// private static Logger log = LoggerFactory.getLogger(SamlUtil.class);
	/** Constructor. */
	public SamlUtil() {
		// super();

		parser = new BasicParserPool();
		parser.setNamespaceAware(true);
		builderFactory = Configuration.getBuilderFactory();
		marshallerFactory = Configuration.getMarshallerFactory();
		unmarshallerFactory = Configuration.getUnmarshallerFactory();

		/** {@inheritDoc} */
	}

	/**
	 * Builds the requested XMLObject.
	 * 
	 * @param objectQName
	 *            name of the XMLObject
	 * 
	 * @return the build XMLObject
	 */
	public XMLObject buildXMLObject(QName objectQName) {

		XMLObjectBuilderFactory lFact = Configuration.getBuilderFactory();
		if (lFact == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Unable to retrieve XMLObjectBuilderFactory for object QName " + objectQName);
		}

//		Map lMap = lFact.getBuilders();

//		Set lSet = lMap.keySet();
//		Object lQ[] = lSet.toArray();

		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		 * siesLogger.info("Array key length "+ lQ.length);
		 * 
		 * for(int i=0;i<lQ.length;i++)
		 * 
		 * { Object lQnam = lQ[i]; if (lQnam instanceof QName) { QName lQu= (QName)lQnam; //// [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		 * //siesLogger.info("Giro "+i+" --- "+ lQu.getNamespaceURI() + lQu.toString()); } }
		 */

		XMLObjectBuilder builder = lFact.getBuilder(objectQName);
		if (builder == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Unable to retrieve builder for object QName " + objectQName);
		}
		return builder.buildObject(objectQName.getNamespaceURI(), objectQName.getLocalPart(),
				objectQName.getPrefix());
	}

	/**
	 * Unmarshalls an element file into its SAMLObject.
	 * 
	 * @param elementFile
	 *            the classpath path to an XML document to unmarshall
	 * 
	 * @return the SAMLObject from the file
	 */
	public XMLObject unmarshallElement(String elementFile) {
		try {

			InputStream in = new FileInputStream(elementFile);

			Document doc = parser.parse(in);
			Element samlElement = doc.getDocumentElement();

			Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(samlElement);
			if (unmarshaller == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Unable to retrieve unmarshaller by DOM Element");
			}

			return unmarshaller.unmarshall(samlElement);
		} catch (XMLParserException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Unable to parse element file " + elementFile);
		} catch (UnmarshallingException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Unmarshalling siesLogger.infoed when parsing element file " + elementFile + ": "
					+ e);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Unmarshalling siesLogger.infoed when parsing element file " + elementFile + ": "
					+ e);
		}

		return null;
	}

	public XMLObjectBuilderFactory getBuilderFactory() {
		return builderFactory;
	}

//	private Document getDOM(String aStringCrypt) throws XMLParserException {
//		Document targetDOM = parser.parse(new StringBufferInputStream(aStringCrypt));
//		return targetDOM;
//	}

	public KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
		try {
			Key key = keystore.getKey(alias, password);
			if (key instanceof PrivateKey) {
				Certificate cert = keystore.getCertificate(alias);
				PublicKey publicKey = cert.getPublicKey();
				return new KeyPair(publicKey, (PrivateKey) key);
			}
		} catch (UnrecoverableKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (KeyStoreException e) {
		}
		return null;
	}

	public String convertFileToString(String nomeFile) {

		String lReturnString = "";
		try {

			File fileIn = new File(nomeFile);

			FileInputStream streamIn = new FileInputStream(fileIn);
			byte[] lBuffer = new byte[streamIn.available()];
			streamIn.read(lBuffer, 0, streamIn.available());
			streamIn.close();

			BASE64Encoder encoder = new BASE64Encoder();
			lReturnString = encoder.encode(lBuffer);
		} catch (FileNotFoundException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("FileCopy: " + e);
		} catch (IOException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("FileCopy: " + e);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("FileCopy: " + e);
		}
		return lReturnString;
	}

	/**
	 * serialize doc
	 * 
	 * @param doc
	 * @param out
	 * @throws Exception
	 */
	public static void serialize(Document doc, OutputStream out) throws Exception {

		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			// Setup indenting to "pretty print"
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			// serializer.setOutputProperty(OutputKeys.

			serializer.transform(new DOMSource(doc), new StreamResult(out));
		} catch (TransformerException e) {
			// this is fatal, just dump the stack and throw a runtime exception
			e.printStackTrace();

			throw new RuntimeException(e);
		}
	}

}