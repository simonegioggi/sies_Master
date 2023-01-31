package siap.siep.penapecuniaria.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Title: ActUploadTrasmissioneConversione 
 * Description: Validazione
 *
 * @version 1.0
 */
public class ActUploadTrasmAnnotazioneProvvedimentoSor extends ActionSiap implements ICostantiEvento {

	public String processRequest() throws Exception {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);
		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lModel = new EventoModel();
		lModel = lCtrl.ExRicercaEventoByKey(lIdEvento);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];
			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lModel.setFlagDocumentoRegistrato("S");

		// New
		String codStatoProc = "";
		if (lModel.getCodMotivo().equals("2470"))
			codStatoProc = "0341";
		else if (lModel.getCodMotivo().equals("2471"))
			codStatoProc = "0342";
		else
			// Ticket#20230125017-SIEP-Impossibile visualizzare alcuni fascicolo mediante ricerca per numero.
			// "0000" NON ESISTE NELLA TABELLA STATO_PROCEDIMENTO: mettiamo validato '0109'
			// codStatoProc = "0000";
			codStatoProc = "0109";

		IEvento lCtrlUpd = SICOLookupRemote.getEventoRemote();
		lCtrlUpd.ExUpdateValidaProvvedimento(lModel, codStatoProc);

		// 18/11/2015 Oltre alla validazione, occorre anche aggiornare la RichiestaConversione se la
		// DataDeposito non è valorizzata.
		// Inizio 01/02/2016
		// 29/01/2015 Lettura ultima Richiesta Conversione del Fascicolo SIEP prima dell'aggiornamento della
		// relativa DATA_DEPOSITO.
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lModelRC = new RichiestaConversioneModel();
		lModelRC = lCtrlRC.ExRicercaRichiestaConversioneByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		if (lModelRC.getIdRichiestaConversione() != null)
			lCtrlRC.ExModificaDataDepositoRichiestaConversione(lModelRC.getIdRichiestaConversione(),
					lModel.getDataEmissione());
		// Fine 01/02/2016

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.penapecuniaria.action.ActDettaglioAnnotazioneProvvedimento");
		lRedirigi.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lModel.getIdEvento().toString());
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}