package f3b.util.xml;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: ParserModelTree
 * </p>
 * <p>
 * Description: Il ParserModelTree ha il compito di creare un docuemnto DOM o XML da una struttura fissata
 * come Tree di oggetti Model.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ParserModelTree {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
	 * Documento DOM generato dal Parser
	 */
	protected Document mDocument;

	/**
	 * Costruttore di default che istanzia l' attributo mDocument
	 * 
	 * @throws Exception
	 */
	public ParserModelTree() throws Exception {
		DocumentBuilder lDocumentBuilder = null;

		try {
			// Step 1: create a DocumentBuilderFactory and configure it
			DocumentBuilderFactory lDocumentBuilderFactory = DocumentBuilderFactory.newInstance();

			// Step 2: create a DocumentBuilder that satisfies the constraints
			// specified by the DocumentBuilderFactory
			lDocumentBuilder = lDocumentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce);
		}

		mDocument = lDocumentBuilder.newDocument();
	}

	/**
	 * Metodo che esamina l'inputSource in entrata e restituisce un Document
	 * <p>
	 * 
	 * @param mIs
	 * @return XmlDocument
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document parse(ModelTreeInputSource mIs) throws SAXException, IOException {
		try {
			TreeModel lTreeRoot = (TreeModel) mIs.getTreeNode();

			mDocument.appendChild(iterateObjectElement((TreeModel) lTreeRoot.getRoot()));

			return mDocument;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Itera sul TreeModel e crea gli Elementi di un documento con gerarchia a seconda della struttura del
	 * Tree.
	 * <p>
	 * 
	 * @param TreeModel
	 * @return Element
	 */
	private Element iterateObjectElement(TreeModel aTreeModel) {
		try {
			int nChildCount = aTreeModel.getChildCount();
			Element lElement = null;

			for (int i = 0; i <= nChildCount; i++) {
				if (!aTreeModel.getMark()) {
					Object lObj = aTreeModel.getModel();

					String lClassMarker = getMarkerFromClass(lObj.getClass().getName());
					lElement = mDocument.createElement(lClassMarker);

					Method[] lFieldMetho = lObj.getClass().getDeclaredMethods();

					int lCounthMethod = lFieldMetho.length;

					for (int y = 0; y < lCounthMethod; y++) {
						String lAttributeName = lFieldMetho[y].getName();

						// Controllo che sia un metodo senza argomenti. Luigi 27-03-2006
						Class[] lParameter = lFieldMetho[y].getParameterTypes();
						if (lParameter.length == 0)
							if (lAttributeName.startsWith("get", 0)) {
								String lAttributeMarker = getAttributeMarker(lAttributeName);
								String lAttributeValueChars = "";
								String[] lArg = {};

								Object lRet = lFieldMetho[y].invoke(lObj, (Object[]) lArg);

								if ((lRet != null) && (!(lRet instanceof GenericModel))
										&& (!(lRet instanceof Vector))) {
									lAttributeValueChars = convertObjectToString(lRet, lAttributeMarker);
									if (lAttributeValueChars != null) {
										Element lElementChild = mDocument.createElement(lAttributeMarker);
										lElementChild.appendChild(mDocument
												.createTextNode(lAttributeValueChars));
										lElement.appendChild(lElementChild);
									}
								}
							}

					} // fine for sugli attributi

					aTreeModel.setMark();
				}// fine if sul mark dell'elemento

				if (!aTreeModel.isLeaf() && aTreeModel.getChildCount() > i) {// Chiamata Ricorsiva
					TreeModel lTreeChild = (TreeModel) aTreeModel.getChildAt(i);

					lElement.appendChild(iterateObjectElement(lTreeChild));
				}

			}// fine for sui figli

			return lElement;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName(), ex);
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Itera sul TreeModel e crea gli Elementi con gli attributi con gerarchia a seconda della struttura del
	 * Tree. Per ora non usata ma da tener presente per sviluppi futuri.
	 * 
	 * @param aTreeModel
	 * @return Element
	 */
	// private Element iterateObjectAttribute(TreeModel aTreeModel) {
	// try {
	// int nChildCount = aTreeModel.getChildCount();
	// Element lElement = null;
	//
	// for (int i = 0; i <= nChildCount; i++) {
	// if (!aTreeModel.getMark()) {
	// Object lObj = aTreeModel.getModel();
	// String lClassMarker = getMarkerFromClass(lObj.getClass().getName());
	// lElement = mDocument.createElement(lClassMarker);
	// Method[] lFieldMetho = lObj.getClass().getDeclaredMethods();
	//
	// int lCounthMethod = lFieldMetho.length;
	//
	// for (int y = 0; y < lCounthMethod; y++) {
	// String lAttributeName = lFieldMetho[y].getName();
	//
	// if (lAttributeName.startsWith("get", 0)) {
	// String lAttributeMarker = getAttributeMarker(lAttributeName);
	// String lAttributeValueChars = "";
	// String[] lArg = {};
	//
	// Object lRet = lFieldMetho[y].invoke(lObj, lArg);
	//
	// if (lRet != null) {
	// lAttributeValueChars = convertObjectToString(lRet, lAttributeMarker);
	// if (lAttributeValueChars != null) {
	// lElement.setAttribute(lAttributeMarker, lAttributeValueChars);
	// }
	// }
	// }
	//
	// } // fine for sugli attributi
	//
	// aTreeModel.setMark();
	//
	// }// fine if sul mark dell'elemento
	//
	// if (!aTreeModel.isLeaf() && aTreeModel.getChildCount() > i) {// Chiamata Ricorsiva
	// TreeModel lTreeChild = (TreeModel) aTreeModel.getChildAt(i);
	// lElement.appendChild(iterateObjectElement(lTreeChild));
	// }
	//
	// }// fine for sui figli
	//
	// return lElement;
	// } catch (Exception ex) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.error(this.getClass().getName(), ex);
	// ex.printStackTrace();
	// }
	// return null;
	// }

	/**
	 * Restituisce il tag da usare per gli attributi
	 * <p>
	 * 
	 * @param attributeName
	 *            a value of type 'String'
	 * @return a value of type 'String'
	 */
	protected String getAttributeMarker(String attributeName) {
		if (attributeName.length() > 2)
			return attributeName.substring(3);
		else
			return "";
	}

	/**
	 * Restituisce il tag costituito dal nome della classe da analizzare
	 * 
	 * @param String
	 * @return a value of type 'String'
	 */
	private String getMarkerFromClass(String aClassName) {
		String nameMark = "";

		if (aClassName.length() > 1) {
			int indexPackage = aClassName.lastIndexOf(".");
			nameMark = aClassName.substring(indexPackage + 1);

			int indexModel = nameMark.indexOf("Model");

			if (indexModel > 0)
				nameMark = nameMark.substring(0, indexModel);
		}

		return nameMark;
	}

	/**
	 * Converte un oggetto in una stringa da inserire all'interno del documento XML come testo fra i tag
	 * 
	 * @param lRet
	 * @return String
	 */
	private String convertObjectToString(Object lRet, String lMarker) {
		String lClassName = lRet.getClass().getName();
		String lReturnString = lRet.toString();

		if (lClassName.compareTo("java.util.Date") == 0)
			lReturnString = DateUtils.getDateToString((Date) lRet, "dd-MM-yyyy");

		if (lClassName.compareTo("java.lang.String") == 0) {
			lReturnString = lRet.toString();
			if (lReturnString.equals(""))
				lReturnString = null;
		}

		if (lClassName.compareTo("java.math.BigInteger") == 0)
			lReturnString = lRet.toString();

		if (lClassName.compareTo("java.math.BigDecimal") == 0) { // Nel caso si tratti di valuta
			BigDecimal lDec = (BigDecimal) lRet;
			if ((lMarker.indexOf("Importo") >= 0) && (lDec.intValue() != 0))
				lReturnString = StringUtils.toEuroFormat((BigDecimal) lRet);
			else
				lReturnString = lRet.toString();
		}

		if (lClassName.compareTo("java.sql.Timestamp") == 0)
			lReturnString = DateUtils.getDateToString((Date) lRet, "dd-MM-yyyy");

		return lReturnString;
	}

}