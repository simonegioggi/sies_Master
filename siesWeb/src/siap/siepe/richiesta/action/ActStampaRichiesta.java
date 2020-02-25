package siap.siepe.richiesta.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.TemplateManager;
import siap.sico.util.report.ReportGenerator;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.jms.controller.ITrasmissioneJMS;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActStampaRichiesta
 * </p>
 * <p>
 * Description: Classe Azione demandata all'attuazione della stampa della Richiesta.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActStampaRichiesta extends ActionSiap implements ICostantiRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// ID TEMPLATE fissato per testare prima di implementare la COMBO
	public final String TEMPLATE_ATTIVITA_01 = "SIEPE_ATT_001";

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Model della Richiesta trattata
		RichiestaModel lRichiesta = null;
		// Interfaccia al RichiestaController
		IRichiesta lRichCtrl = null;

		// Documento di stampa
		ByteArrayOutputStream lReport = null;

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Preleva dalla request la chiave della richiesta come parametro
		BigDecimal lKeyRichiesta = getRequestBigDecimalParameter(ICostantiRichiesta.CAMPO_ID_RICHIESTA);

		// Ricerca Richiesta
		lRichCtrl = SIEPELookupRemote.getRichiestaRemote();
		lRichiesta = lRichCtrl.ExRicercaRichiestaByKey(lKeyRichiesta);

		// Se il documento è validato si fornisce la stampa già memorizzata
		if (lRichiesta.getFlagDocumentoRegistrato() != null
				&& lRichiesta.getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
			lReport = lRichCtrl.ExGetDocumento(lRichiesta);
		} else {
			// Creazione documento di stampa

			// Si fissa il valore di default ad un modello di prova
			String lIdTemplate = TEMPLATE_ATTIVITA_01;
			if (!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE)) {
				lIdTemplate = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
			}

			lReport = generaStampa(lKeyRichiesta, lIdTemplate);

			// Valorizzazione del model della Richiesta
			lRichiesta.setDataAggiornamento(DateUtils.getSysDate());
			lRichiesta.setCodOperatoreAggiornamento(lCodiceOperatore);
			lRichiesta.setCodUfficioAggiornamento(lCodiceUfficio);
			lRichiesta.setFlagDocumentoRegistrato("N");

			// Inserisce il documento generato nel model di ritorno
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lReport.toByteArray());
			lRichiesta.setDocBlobIn(lByteArrayInput);
			lRichCtrl.ExUpdateDocument(lRichiesta);
		}

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIEPEException(SIEPEException.USER_MESSAGE, "Nessun documento è stato generato.");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return IWebConstants.PG_DOWNLOAD_NEW;
	}

	/**
	 * Funzione per la creazione della stampa Richiesta. La funzione utilizza il template, il cui riferimento
	 * viene passato come argomento (aIdTemplate) . Crea il documento XML e poi da questo quello RTF che
	 * restituisce come ByteArrayOutputStream. Infine restituisce il documento in ByteArrayOutputStream.
	 * 
	 * @param aIdRichiesta
	 * @param aIdTemplate
	 * @return ByteArrayOutputStream
	 * @throws Exception
	 */
	public ByteArrayOutputStream generaStampa(BigDecimal aIdRichiesta, String aIdTemplate) throws Exception {
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
		lXMLDocument = lCtrlMess.getTreeModelForRichiesta(aIdRichiesta, lFascicoloEsteso,
				getUfficioUtenteConnesso(), this.getUtenteConnesso());

		// Si ricava il nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## NOME TEMPLATE >>>" + lNomeTemplate);

		lReportCtrl = new ReportGenerator(lCodiceUfficio);

		lReport = (ByteArrayOutputStream) lReportCtrl.generateDocument(lXMLDocument, lNomeTemplate);

		return lReport;
	}

}