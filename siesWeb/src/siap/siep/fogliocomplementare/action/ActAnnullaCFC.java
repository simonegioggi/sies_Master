package siap.siep.fogliocomplementare.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.impugnazione.action.ICostantiImpugnazione;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Title: ActAnnullaImpugnazione
 * Description: Classe Action per annullare un Foglio Complementare
 * Created: A.S.
 * 
 * @version 1.0
 */

public class ActAnnullaCFC extends ActionSius implements ICostantiImpugnazione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		String lRectPage = null;
		if (isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE)
				|| isRequestParameterNullObj(CAMPO_MOTIVO_ANNULLAMENTO))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati");

		BigDecimal idEvento = null;
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
			idEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// questo parametro indica se bisogna procedere solo all'annullamento del
		// Foglio Complementare oppure annullare lo stesso anche su NSC
		String annullaNSC = "";
		if (!isRequestParameterNullObj("annullaNSC"))
			if (this.getRequestStringParameter("annullaNSC").equals("Si"))
				annullaNSC = "Si";
			else
				annullaNSC = "No";

		gestioneRitorno();

		// Verifico se è presente il Foglio Complementare ed è stato trasemsso a NSC
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		boolean presenzaFC = lCtrlEve.ExRicercaFoglioComplementareTrasmesso(idEvento);

		annulla(presenzaFC);

		if (annullaNSC.equals("Si") && presenzaFC) {
			setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, idEvento);
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Foglio Complementare annullato!");
			lRectPage = IWebConstants.PG_MESSAGE_ANNULLA_CFC;
		} else {
			lRectPage = ritornoDopoCancellazione("Foglio Complementare annullato!", lRectPage);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");
		}

		// MEV 16: aggiunti controlli e set di proprietà
		if (!isRequestParameterNullObj("tipoWS"))
			setRequestAttribute("tipoWS", this.getRequestStringParameter("tipoWS"));
		if (!isRequestParameterNullObj("idSoggetto"))
			setRequestAttribute("idSoggetto", this.getRequestStringParameter("idSoggetto"));
		if (!isRequestParameterNullObj("idSentenza"))
			setRequestAttribute("idSentenza", this.getRequestStringParameter("idSentenza"));
		if (!isRequestParameterNullObj("idFascicoloSiep"))
			setRequestAttribute("idFascicoloSiep", this.getRequestStringParameter("idFascicoloSiep"));

		// valore di ritorno
		return lRectPage;
	}

	private void annulla(boolean presenzaFC) throws Exception {

		// Istanzio il Model e lo carico con quello posto nella request.
		DocumentoAllegatoModel lDocAll = new DocumentoAllegatoModel();

		// Dati da aggiornare
		lDocAll.setIdDocumentoAllegato(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		lDocAll.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																		// dell'operatore
																		// che
																		// annulla
		lDocAll.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																			// dell'operatore
																			// che
																			// annulla
		lDocAll.setDataAggiornamento(DateUtils.getSysDate());
		// se il FC non è stato trasmesso setto la Data Annullamento
		// in caso di trasmissione la Data Annullamento viene settata
		// in SiesEsecuzione
		if (!presenzaFC)
			lDocAll.setDataAnnullamento(lDocAll.getDataAggiornamento());
		lDocAll.setMotivoAnnullamento(getRequestStringParameter(CAMPO_MOTIVO_ANNULLAMENTO));

		// Viene richiamato il Controller per eseguire l'Update
		IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		lDocAllCtrl.ExAnnullaDocumentoAllegato(lDocAll);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("ID Impugnazione Aggiornato: " + lDocAll.getIdDocumentoAllegato());
	}

}