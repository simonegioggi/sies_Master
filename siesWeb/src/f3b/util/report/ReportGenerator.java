package f3b.util.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.windward.xmlreport.ProcessHtml;
import net.windward.xmlreport.ProcessPdf;
import net.windward.xmlreport.ProcessReport;
import net.windward.xmlreport.ProcessRtf;
import net.windward.xmlreport.ProcessTxt;

import org.w3c.dom.Document;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.ModelTreeInputSource;
import f3b.util.xml.ParserModelTree;
import f3b.util.xml.TreeModel;
import f3b.util.xml.XMLUtils;
//import siap.sico.stampa.controller.StampaEventoUtils;
// PATCH di Paolo  Luigi 15-4-2005
//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
import org.apache.log4j.Logger;

import siap.util.SIAPPathProperties;

/**
 * <p>
 * Title: ReportGenerator
 * </p>
 * <p>
 * Description: Classe gestione elaborazione stampe, con la libreria
 * WindwardReport
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A. Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia
 * </p>
 */
@SuppressWarnings("static-access")
public class ReportGenerator {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per StampaLog
	private static Logger printLogger = Logger.getLogger(LogF3B.STAMPA_LOG);

	public static final int RTF = 1;
	public static final int PDF = 2;
	public static final int TXT = 3;
	public static final int HTML = 4;

	private InputStream mXmlDataIS;
	private InputStream mTemplateIS;
	private OutputStream mGeneratedOS;

	private int mReport = this.RTF;

	// Aggiunta del log
	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
//	public static Logger mLog = LogF3B.getLogger();

	// NUOVA INFRASTRUTTURA: aggiunte variabili di classe
	private SIAPPathProperties mPathProperties = SIAPPathProperties.getInstance();
	private String mPath = null;

	/**
	 * Costruttore di classe.
	 */
	public ReportGenerator() {
	}

	/**
	 * Costruttore di classe, con parametri.
	 * <p>
	 * 
	 * @param aXmlDataIS
	 *            Dati in formato XML.
	 * @param aTemplateIS
	 *            Template rtf.
	 * @param aGeneratedOS
	 *            stream di uscita del documento generato.
	 */
	public ReportGenerator(InputStream aXmlDataIS, InputStream aTemplateIS, OutputStream aGeneratedOS) {
		this.mXmlDataIS = aXmlDataIS;
		this.mTemplateIS = aTemplateIS;
		this.mGeneratedOS = aGeneratedOS;
	}

	/**
	 * Costruttore di classe, con parametri.
	 * <p>
	 * 
	 * @param aXmlDataIS
	 *            Dati in formato XML.
	 * @param aTemplateIS
	 *            Template rtf.
	 * @param aGeneratedOS
	 *            stream di uscita del documento generato.
	 * @param aReport
	 *            tipo di documento da generare.
	 */
	public ReportGenerator(InputStream aXmlDataIS, InputStream aTemplateIS, OutputStream aGeneratedOS, int aReport) {
		this.mXmlDataIS = aXmlDataIS;
		this.mTemplateIS = aTemplateIS;
		this.mGeneratedOS = aGeneratedOS;
		this.mReport = aReport;
	}

	/**
	 * Imposta il flusso di dati in formato XML.
	 * <p>
	 * 
	 * @param aValue
	 *            flusso di dati in formato XML.
	 */
	public void setXmlData(InputStream aValue) {
		this.mXmlDataIS = aValue;
	}

	/**
	 * Imposta il template RTF
	 * <p>
	 * 
	 * @param aValue
	 *            il template rtf.
	 */
	public void setRtfTemplate(InputStream aValue) {
		this.mTemplateIS = aValue;
	}

	/**
	 * Imposta lo stream di output del documento generato.
	 * <p>
	 * 
	 * @param aOut
	 *            stream di output del documento generato.
	 */
	public void setOutputStream(OutputStream aOut) {
		this.mGeneratedOS = aOut;
	}

	/**
	 * Imposta il tipo di report.
	 * <p>
	 * 
	 * @param aValue
	 *            tipo di report.
	 */
	public void setReport(int aValue) {
		this.mReport = aValue;
	}

	/**
	 * Ritorn ail tipo di report.
	 * <p>
	 * 
	 * @return il tipo di report come <code>int</code>.
	 */
	public int getReport() {
		return this.mReport;
	}

	/**
	 * Creazione di un documento RTF da un tree model e dal nome del template
	 * <p>
	 * 
	 * @param aTreeModel
	 *            albero di model da prserizzare in XML.
	 * @param aFileTemplateRTF
	 *            tempalte RTF.
	 * @return il documento generato.
	 * @throws F3BException
	 *             progazione errori di eccezione.
	 */
	public OutputStream generateDocument(TreeModel aTreeModel, String aFileTemplateRTF) throws F3BException {
		// Inizializzazione dello Stream di Output
		this.mGeneratedOS = new ByteArrayOutputStream();

		try {
			this.mXmlDataIS = parseTreeXML(aTreeModel);
			this.mTemplateIS = new FileInputStream(aFileTemplateRTF);

			// Esegue l'elaborazione del documento.
			this.process();

			// Chiusura degli Stream
			this.mTemplateIS.close();
			this.mXmlDataIS.close();
		} catch (Throwable t) {
			t.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nel generateDocument", t);
			try {
				// Chiusura degli Stream
				this.mTemplateIS.close();
				this.mXmlDataIS.close();
			} catch (IOException ioex) {
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report !");
			}
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report !");

		}

		return mGeneratedOS;
	}

	/**
	 * Creazione di un documento RTF da un tree model e dal nome del template
	 * <p>
	 * 
	 * @param aTreeModel
	 *            albero di model da prserizzare in XML.
	 * @param aFileTemplateRTF
	 *            tempalte RTF.
	 * @return il documento generato.
	 * @throws F3BException
	 *             progazione errori di eccezione.
	 */
	public OutputStream generateDocumentFromFile(String aFileXml, String aFileTemplateRTF) throws F3BException {
		// Inizializzazione dello Stream di Output
		this.mGeneratedOS = new ByteArrayOutputStream();

		try {
			// NUOVA INFRASTRUTTURA: cambiato il path per macchina UNIX
			mPath = mPathProperties.getProperty("TEMP");
			this.mXmlDataIS = new FileInputStream(mPath + aFileXml);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("File XML dimensione da passare agli XML = " + mXmlDataIS.available());

			// this.mXmlDataIS = lIInputXml;

			this.mTemplateIS = new FileInputStream(aFileTemplateRTF);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Nome Template = " + aFileTemplateRTF);

			// Esegue l'elaborazione del documento.
			this.process();

			// Chiusura degli Stream
			this.mTemplateIS.close();
			this.mXmlDataIS.close();
		} catch (Throwable t) {
			t.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nel generateDocument", t);
			try {
				// Chiusura degli Stream
				this.mTemplateIS.close();
				this.mXmlDataIS.close();
			} catch (IOException ioex) {
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report !");
			}
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report !");

		}

		return mGeneratedOS;
	}

	/**
	 * Creazione di un documento RTF da un tree model e dal nome del template e
	 * poi memorizza il file xml prodotto su un file di sistema di cui viene
	 * passata la path in input
	 * <p>
	 * 
	 * @param aTreeModel
	 *            albero di model da prserizzare in XML.
	 * @param aFileTemplateRTF
	 *            tempalte RTF.
	 * @param aFileTemplateRTF
	 *            tempalte RTF.
	 * @return il documento generato.
	 * @throws F3BException
	 *             progazione errori di eccezione.
	 */
	public OutputStream generateDocumentAndStoreXml(TreeModel aTreeModel, String aFileTemplateRTF, String aFileXml)
			throws F3BException {
		// Inizializzazione dello Stream di Output
		this.mGeneratedOS = new ByteArrayOutputStream();

		try {
			this.mXmlDataIS = parseTreeXMLWithStore(aTreeModel, aFileXml);
			this.mTemplateIS = new FileInputStream(aFileTemplateRTF);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("File Xml memorizzato!");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Nome Template = " + aFileTemplateRTF);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Nome File Xml da Memorizzare = " + aFileXml);

			// Esegue l'elaborazione del documento.
			this.process();

			// Chiusura degli Stream
			this.mTemplateIS.close();
			this.mXmlDataIS.close();
		} catch (Throwable t) {
			t.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nel generateDocument", t);
			try {
				// Chiusura degli Stream
				this.mTemplateIS.close();
				this.mXmlDataIS.close();
			} catch (IOException ioex) {
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report !");
			}
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report !");

		}

		return mGeneratedOS;
	}

	/**
	 * parseTreeXMLWithStore
	 * 
	 * @param aTreeModel
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayInputStream parseTreeXMLWithStore(TreeModel aTreeModel, String aNomeFileXML) throws F3BException {
		ByteArrayInputStream lXML;

		try {
			ModelTreeInputSource lIs = new ModelTreeInputSource(aTreeModel);
			ParserModelTree lParser = new ParserModelTree();
			/* SC XmlDocument lDocXML = lParser.parse(lIs); */
			Document lDocXML = lParser.parse(lIs);
			/* SC sostituito XMLDOcument con Document Interface */

			/*
			 * OutputFormat lOF = new OutputFormat(); StringWriter lSW = new
			 * StringWriter(); XMLSerializer lXMLSer = new XMLSerializer();
			 * lOF.setEncoding("ISO-8859-1"); lOF.setIndenting(true); //
			 * Settando questo parametro a 0 le righe non // subiscono eventuale
			 * Wrapping lOF.setLineWidth(0);
			 * 
			 * lXMLSer.setOutputFormat(lOF); lXMLSer.setOutputCharStream(lSW);
			 * lXMLSer.serialize(lDocXML);
			 */

			ByteArrayOutputStream outXml = new ByteArrayOutputStream();

			XMLUtils.serialize(lDocXML, outXml);

			// NUOVA INFRASTRUTTURA: cambiato il path per differenziare macchina windows da macchina UNIX
			mPath = mPathProperties.getProperty("TEMP");
			File outFile = new File(mPath + aNomeFileXML);

			String lDaMemorizzare = new String(outXml.toString());

			FileWriter out = new FileWriter(outFile, true);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza printLogger al posto di LogF3B.getStampaLogger()
			printLogger.info(lDaMemorizzare);

			if (lDaMemorizzare != null && lDaMemorizzare.length() > 43) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Scritto!");
				// tasglio la prima parte dell'xml
				out.write(lDaMemorizzare.substring(43));
			}
			out.flush();
			out.close();

			lXML = new ByteArrayInputStream(outXml.toString().getBytes());
		} catch (Throwable t) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nel parseTreeXML", t);
			throw new F3BException("Errore di generazione report" + t);
		}

		return lXML;
	}

	/**
	 * Creazione di un InputStream da un TreeModel.
	 * <p>
	 * 
	 * @param aTreeModel
	 *            Gerarchia di models.
	 * @return ByteArrayInputStream rappresentaizone in Byte dell'XML
	 * @throws F3BException
	 *             propaga l'errore di ecczione.
	 */
	/*
	 * Luigi 15-4-2005 Viene reintrodotta una patch di Paolo che setta la
	 * codifica dell'XML generato "ISO-8859-1"; in questa maniera vengono
	 * gestiti i caratteri estesi che nel formato di default non sarebbero
	 * gestiti.
	 */
	public ByteArrayInputStream parseTreeXML(TreeModel aTreeModel) throws F3BException {
		ByteArrayInputStream lXML;

		try {
			ModelTreeInputSource lIs = new ModelTreeInputSource(aTreeModel);
			ParserModelTree lParser = new ParserModelTree();
			Document lDocXML = lParser.parse(lIs);

			/*
			 * 
			 * OutputFormat lOF = new OutputFormat(); StringWriter lSW = new
			 * StringWriter(); XMLSerializer lXMLSer = new XMLSerializer();
			 * lOF.setEncoding("ISO-8859-1"); lOF.setIndenting(true); //
			 * Settando questo parametro a 0 le righe non // subiscono eventuale
			 * Wrapping lOF.setLineWidth(0);
			 * 
			 * lXMLSer.setOutputFormat(lOF); lXMLSer.setOutputCharStream(lSW);
			 * lXMLSer.serialize(lDocXML);
			 */

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Prima di chiamare  XMLUtils.serialize");
			XMLUtils.serialize(lDocXML, out);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza printLogger al posto di LogF3B.getStampaLogger()
			printLogger.info(out.toString());

			lXML = new ByteArrayInputStream(out.toString().getBytes());
		} catch (Throwable t) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nel parseTreeXML", t);
			throw new F3BException("Errore di generazione report" + t);
		}

		return lXML;
	}

	/**
	 * Processa la generazione del documento.
	 * 
	 * @throws Throwable
	 *             propagazione errori di eccezione.
	 */
	public void process() throws Throwable {
		try {
			ProcessReport lReport = null;

			switch (this.mReport) {
			case RTF:
				lReport = new ProcessRtf(this.mXmlDataIS, this.mTemplateIS, this.mGeneratedOS);
				break;

			case PDF:
				lReport = new ProcessPdf(this.mXmlDataIS, this.mTemplateIS, this.mGeneratedOS);
				break;

			case TXT:
				lReport = new ProcessTxt(this.mXmlDataIS, this.mTemplateIS, this.mGeneratedOS);
				break;

			case HTML:
				lReport = new ProcessHtml(this.mXmlDataIS, this.mTemplateIS, this.mGeneratedOS);
				break;
			}

			lReport.process();

		} catch (Throwable t) {
			t.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore nel process", t);
			throw new F3BException("Errore nella generazione del report " + t);
		}

	}

	/**
	 * Creazione della descrizione di un TreeModel.
	 * <p>
	 * 
	 * @param aTreeModel
	 *            Gerarchia di models.
	 * @return String rappresentaizone dell'XML
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public String debugTreeXML(TreeModel aTreeModel) throws F3BException {
		String lRet = null;
		TreeModel lTree = new TreeModel(aTreeModel.getModel());
		try {
			ModelTreeInputSource lIs = new ModelTreeInputSource(lTree);
			ParserModelTree lParser = new ParserModelTree();
			Document lDocXML = lParser.parse(lIs);
			CharArrayWriter lWri = new CharArrayWriter();

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			XMLUtils.serialize(lDocXML, out);
			// lDocXML.write(lWri);
			lRet = out.toString();
			lWri.flush();
			lWri.close();
		} catch (Throwable t) {
			throw new F3BException("Errore: " + t);
		}

		return lRet;
	}

	/**
	 * Settaggio per la generazione di un file PDF
	 */
	public void setPdf() {
		mReport = this.PDF;
	}

}