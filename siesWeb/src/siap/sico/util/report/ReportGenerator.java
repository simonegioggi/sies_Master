package siap.sico.util.report;

import java.io.File;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ReportGenerator extends f3b.util.report.ReportGenerator {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private String mCodUfficio;

	// Lock del costruttore base.
	public ReportGenerator() {}

	/**
	 * Costruttore personalizzato per gestione codUfficio
	 * <p>
	 * 
	 * @param aCodUfficio
	 */
	public ReportGenerator(String aCodUfficio) {
		this.mCodUfficio = aCodUfficio;
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

		String lFileTemplateRTF = null;

		// Chiamata al controllo del file custom... se l'argomento passato è != null
		if (aFileTemplateRTF != null)
			lFileTemplateRTF = this.checkAndGetCustomTemplate(aFileTemplateRTF);

		// Chiamata al metodo generateDocument della superclasse
		return super.generateDocument(aTreeModel, lFileTemplateRTF);
	}

	/**
	 * Metodo che esegue il controllo se esiste un template personalizzato. In caso di esito positivo, ne
	 * ritorna il path modificato nel seguente modo : es.: im_<codUfficio>
	 * <p>
	 * 
	 * @return path e nome file di template corrente.
	 */
	private String checkAndGetCustomTemplate(String aFileTemplateRTF) throws F3BException {

		String lTemplateName = null;

		if (mCodUfficio != null) {
			// Inserire qui il replace per normalizzazione path ( Windows / Unix )
			aFileTemplateRTF = aFileTemplateRTF.replaceAll("\\\\", "/");
			// Preleva il valore di posizione dell'ultimo "/";
			int lPos = aFileTemplateRTF.lastIndexOf("/");
			// Esegue concatenazione del path con il codice ufficio per template custom.
			String lCustomPathRicerca = aFileTemplateRTF.substring(0, lPos) + "_" + mCodUfficio;
			String lFileTemplateRTF = aFileTemplateRTF.substring(lPos);
			String lCustomTemplateName = lCustomPathRicerca + lFileTemplateRTF;
			// Verifica se il file e path sono sul file system.
			File lFile = new File(lCustomTemplateName);
			// Verifica l'esistenza del file custom.
			if (lFile.exists())
				lTemplateName = lCustomTemplateName;
			else
				lTemplateName = aFileTemplateRTF;
		} else {
			// Rilancia errore di eccezione !
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore ! Il codice ufficio non è valorizzato !");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>> (checkAndGetCustomTemplate) >>>> Nome del Template di ritorno : "
				+ lTemplateName + "<<<");

		return lTemplateName;
	}

}