package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per la stampa massiva dei bollettini
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActStampaMassivaBollettini extends ActionSiap {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException, IOException, DocumentException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		// recupero il fascicolo dalla sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		// [SG]: MEV_2023-33 recupero idEvento aggiunto nella chiamata dalla JSP
		BigDecimal idEvento = null;
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
			idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		// [SG]: MEV_2023-33 chiamo query per idFascicolo + idEvento
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = null;
		if (Utils.isNullObj(idEvento))
			elencoStatoPagamenti = ibp.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		else
			elencoStatoPagamenti = ibp.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiepIdEvento(idFascicolo,
					idEvento);
		Iterator<BollettinoPagopaModel> iterBPM = elencoStatoPagamenti.iterator();
		ByteArrayOutputStream baosSingolo = null;
		List<byte[]> listaByteArray = new ArrayList<>(elencoStatoPagamenti.size());
		siesLogger.debug("Stampa Massiva di: " + elencoStatoPagamenti.size() + " Bollettini!");
		while (iterBPM.hasNext()) {
			BollettinoPagopaModel bpm = iterBPM.next();
			baosSingolo = ibp.ExGetBollettino(bpm.getIdBollettinoPagopa());
			if (baosSingolo != null && baosSingolo.size() > 0)
				listaByteArray.add(baosSingolo.toByteArray());
		}
		// [SG]: MEV_2023-33 aggiunto controllo consistenza blob (sopra) ed array per stampa massiva
		if ((listaByteArray == null) || (listaByteArray.size() == 0))
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Bollettino Associato");
		siesLogger.debug("Inizio la concatenzaione dei PDFs!");
		ByteArrayOutputStream baosMassivo = concatPDF(listaByteArray);

		baosMassivo.flush();
		baosMassivo.close();

		// ==============================================
		// Setta il documento di stampa sulla response
		// ==============================================
		setRequestAttribute("report", baosMassivo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD_PDF;
	}

	private static PdfReader getReader(byte[] b) {

		try {
			return new PdfReader(b);
		} catch (Exception e) {
			return null;
		}
	}

	public static ByteArrayOutputStream/* byte[] */ concatPDF(List<byte[]> bytes)
			throws IOException, DocumentException {

		List<PdfReader> readers = bytes.stream().map(i -> getReader(i)).collect(Collectors.toList());
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		document.open();
		PdfContentByte pageContentByte = writer.getDirectContent();
		PdfImportedPage pdfImportedPage;
		int currentPdfReaderPage = 1;
		Iterator<PdfReader> iteratorPDFReader = readers.iterator();
		while (iteratorPDFReader.hasNext()) {
			PdfReader pdfReader = iteratorPDFReader.next();
			while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
				document.newPage();
				pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
				pageContentByte.addTemplate(pdfImportedPage, 0, 0);
				currentPdfReaderPage++;
			}
			currentPdfReaderPage = 1;
		}
		baos.flush();
		document.close();
		baos.close();
		return baos/* .toByteArray() */;
	}

}