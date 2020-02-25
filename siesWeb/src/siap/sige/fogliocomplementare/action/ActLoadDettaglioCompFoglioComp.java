package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDettaglioCompFoglioComp
 * </p>
 * <p>
 * Description: Azione per la visualizzazione del Inserimento/Dettaglio di un Foglio Complementare.
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioCompFoglioComp extends ActionSige implements ICostantiFoglioComp {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	DocumentoAllegatoModel mDocAll = null;
	IDocumentoAllegato mDocAllCtrl = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Parametro per individuare se provengo dall'icona "Compilazione Foglio Complementare".
		String provenienza = null;
		if (!isRequestParameterNullObj("Provenienza")) {
			provenienza = getRequestStringParameter("Provenienza");
			setRequestAttribute("provenienza", provenienza);
		}

		// Inserimento Foglio Complementare sulla tabella DOCUMENTO_ALLEGATO
		if (provenienza != null && provenienza.equals("InsertFC")) {

			BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

			// Controller del DocumentoAllegato
			mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();

			mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");

			ricercaAnnullate(lIdEvento);

			// Evento. Leggo l'evento collegato al Provvedimento.
			EventoModel lEveMod = new EventoModel();
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

			// Compilazione Foglio Complementare inibito per Provvedimento annullato.
			if (lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0) {
				throw new SIGEException(F3BException.USER_MESSAGE,
						"Non è possibile emettere il Foglio Complementare per un provvedimento annullato");
			}

			// Valorizzazione del Documento Allegato
			DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
			aDocAllegato.setDataEmissione(DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")),
					"dd/MM/yyyy"));
			aDocAllegato.setCodTipoDocumento("06");
			aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));
			aDocAllegato.setDataTrasmissione(null);
			aDocAllegato.setEveIdEvento(lIdEvento);
			aDocAllegato.setComuneSedeGiudiziaria(null);

			// MERGE v10: modificata gestione documento allegato
			DocumentoAllegatoModel docAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByKeyEvento(lIdEvento);
			if (!Utils.isPresent(docAll)) {
				aDocAllegato.setCodOperatoreInserimento(getCodUtenteConnesso());
				aDocAllegato.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				aDocAllegato.setDataInserimento(DateUtils.getSysDate());
				// inserisco Foglio Complementare Nsc
				aDocAllegato = mDocAllCtrl.ExInserisciFoglioComplementareNsc(aDocAllegato,
						lEveMod.getCodTipoProvvedimento());
			} else {
				aDocAllegato.setIdDocumentoAllegato(docAll.getIdDocumentoAllegato());
				aDocAllegato.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				aDocAllegato.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				aDocAllegato.setDataAggiornamento(DateUtils.getSysDate());
				// modifico Foglio Complementare Nsc
				mDocAllCtrl.ExModificaFoglioComplementareNsc(aDocAllegato, lEveMod.getCodTipoProvvedimento());
			}

			// Imposta la Combo contenente le Motivazioni non Inviato FC.
			Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
			setRequestAttribute("motivoNonInvio", "" + lOption);
			setRequestAttribute("documentoAllegato", aDocAllegato);
			setRequestAttribute("evento", lEveMod);

			return PG_LOADDETTAGLIOCOMPFOGLIOCOMP;

		} else {

			// ********************************* DETTAGLIO FOGLIO COMPLEMENTARE

			// Gestione del punto di ritorno
			setLinkRitorno();

			IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
			DocumentoAllegatoModel lDocAll = null;
			BigDecimal lIdEvento = null;

			if (!isRequestParameterNullObj(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO)) {
				// Ricerca del Foglio Complementare
				BigDecimal idDocAll = this
						.getRequestBigDecimalParameter(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);
				lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByKey(idDocAll);
				lIdEvento = lDocAll.getEveIdEvento();
			} else {
				// Ricerca del Foglio Complementare
				lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
				lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");
			}

			// Evento. Leggo l'evento collegato al Provvedimento.
			EventoModel lEveMod = null;
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
			if (lEveMod == null)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Documento Allegato assente");

			// Prelevo il Provvedimento
			ProvvedimentoSigeEventoModel provvSigeModel = null;
			IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
			provvSigeModel = mCtrl.ExRicercaProvvedimentoByIdEvento(lIdEvento);

			setRequestAttribute("provvSige", provvSigeModel);
			setRequestAttribute("evento", lEveMod);
			setRequestAttribute("documentoAllegato", lDocAll);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			return PG_LOADDETTAGLIOCOMPFOGLIOCOMP;

		}

	}

	private void ricercaAnnullate(BigDecimal aIdEvento) throws Exception {

		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata
		if (mDocAll == null || mDocAll.getDataAnnullamento() == null) {
			Vector lCFCAnnullati = mDocAllCtrl.ExRicercaDocAllAnnulatiByIdEventoCodTipo(aIdEvento, "06");
			setRequestAttribute("DocAllNulli", lCFCAnnullati);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".ricercaAnnullate: fine");

		}

	}

}