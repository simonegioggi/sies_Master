package siap.sige.attiinarchivio.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaAttiInArchivio extends ActionSige implements ICostantiUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		// Preleva l'Evento
		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		EventoModel lEvento = lFasCtrl.ExRicercaDataInvioAtti(lFasEsteso.getFascicoloSige()
				.getIdFascicoloSige());
		IEvento evCtrl = SICOLookupRemote.getEventoRemote();
		ByteArrayOutputStream lReport = null;
		try {
			lReport = evCtrl.ExGetDocumento(lEvento);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (lReport != null && lReport.size() > 0) {
			setRequestAttribute("report", lReport);
			lReport.flush();
			lReport.close();
			return IWebConstants.PG_DOWNLOAD_NEW;
		}

		IStampaSige ctrl = SIGELookupRemote.getStampaRemote();
		ByteArrayOutputStream lReportNew = ctrl.ExStampaAttiInArchivio(lEvento, lFasEsteso, super.getUtenteConnesso());

		// Prepara la pagina di destinazione
		if (lReportNew != null && lReportNew.size() > 0)
			setRequestAttribute("report", lReportNew);
		else
			throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

		lEvento.setDataAggiornamento(DateUtils.getSysDate());
		lEvento.setCodUfficioAggiornamento(lCodiceUfficio);
		lEvento.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEvento.setFlagDocumentoRegistrato("N");
		evCtrl.ExModificaEvento(lEvento);
		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lReportNew.toByteArray());
		lEvento.setDocBlobIn(lByteArrayInput);
		evCtrl.ExUpdateDocument(lEvento);
		lReportNew.flush();
		lReportNew.close();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}