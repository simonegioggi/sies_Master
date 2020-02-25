package siap.siepe.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.TemplateManager;
import siap.sico.util.report.ReportGenerator;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.jms.controller.ITrasmissioneJMS;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActStampaFascicoloSiepe
 * </p>
 * <p>
 * Description: Classe Azione demandata all'attuazione della stampa del Fascicolo.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ActStampaFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// ID TEMPLATE fissato per testare prima di implementare la COMBO
	public final String TEMPLATE_ATTIVITA_01 = "SIEPE_ATT_001";

	public String processRequest() throws Exception {
		// Model FascicoloSiepe
		// FascicoloSiepeModel lFascicolo = null;
		// Interfaccia a AttivitaController
		IFascicoloSiepe lFasCtrl = null;

		// Documento di stampa
		ByteArrayOutputStream lReport = null;

		// Preleva dalla request la chiave del Fascicolo come parametro
		BigDecimal lKeyFascicolo = getRequestBigDecimalParameter(
				ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE);

		// Ricerca Fascicolo
		lFasCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		/* lFascicolo = */lFasCtrl.ExRicercaFascicoloSiepeByKey(lKeyFascicolo);

		// Si ricava il template da utilizzare
		// Si fissa il valore di default ad un modello di prova per la stampa Fascicolo
		String lIdTemplate = TEMPLATE_ATTIVITA_01;
		if (!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE)) {
			lIdTemplate = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
		}

		lReport = generaStampa(lKeyFascicolo, lIdTemplate);

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIEPEException(SIEPEException.USER_MESSAGE,
					"Nessun documento è stato generato. Funzione non ancora disponibile!");

		return IWebConstants.PG_DOWNLOAD_NEW;
	}

	/**
	 * Funzione per la creazione della stampa Fascicolo. La funzione utilizza il template, il cui riferimento
	 * è passato come argomento. Crea il documento XML e poi da questo quello RTF che viene restituito.
	 * 
	 * @param aIdAttivita
	 * @param lIdTemplate
	 * @return ByteArrayOutputStream
	 * @throws Exception
	 */
	public ByteArrayOutputStream generaStampa(BigDecimal aIdFascicolo, String lIdTemplate) throws Exception {
		// Documento di stampa generato
		ByteArrayOutputStream lReport = null;
		// Documento XML contenente i dati oggetto della stampa
		TreeModel lXMLDocument = null;
		// Report generator per la costruzione del report
		ReportGenerator lReportCtrl = null;

		// Preleva dalla sessione i dati dell'utente connesso.
		// String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Lettura dei dati del Fascicolo in sessione
		FascicoloSiepeEstesoModel lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute(
				"FascicoloSiepeEsteso");

		// Generazione del documento XML
		ITrasmissioneJMS lCtrlMess = SIEPELookupRemote.getTrasmissioneJMSRemote();
		lXMLDocument = lCtrlMess.getTreeModelForFascicolo(lFascicoloEsteso, getUfficioUtenteConnesso(),
				getUtenteConnesso());

		// Si ricava il nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

		lReportCtrl = new ReportGenerator(lCodiceUfficio);
		lReport = (ByteArrayOutputStream) lReportCtrl.generateDocument(lXMLDocument, lNomeTemplate);

		return lReport;
	}

}