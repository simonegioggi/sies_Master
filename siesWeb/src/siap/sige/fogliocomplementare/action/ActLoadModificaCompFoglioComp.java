package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
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

/**
 * <p>
 * Title: ActLoadModificaCompFoglioComp
 * </p>
 * <p>
 * Description: Azione per la modifica di un foglio complementare legato al fascicolo SIGE.
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadModificaCompFoglioComp extends ActionSige implements ICostantiFoglioComp {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	DocumentoAllegatoModel mDocAll = null;
	IDocumentoAllegato mDocAllCtrl = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		lockApplicativo("FoglioComplementare"); // abilitazione lock applicativo su entità

		// Gestione del punto di ritorno
		setLinkRitorno();

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");

		ricercaAnnullate(lIdEvento);

		// Evento. Legge l'evento collegato al Provvedimento.
		EventoModel lEveMod = new EventoModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// Compilazione Foglio Complementare inibito per provvedimento annullato.
		if (lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0) {
			throw new SIGEException(F3BException.USER_MESSAGE,
					"Non è possibile modificare il Foglio Complementare per un provvedimento annullato.");
		}

		// Prelevo il Provvedimento
		ProvvedimentoSigeEventoModel provvSigeModel = null;
		IProvvedimentoSige mCtrl = SIGELookupRemote.getProvvedimentoRemote();
		provvSigeModel = mCtrl.ExRicercaProvvedimentoByIdEvento(lIdEvento);

		// Imposta la Combo contenente le Motivazioni non Inviato FC.
		Option lOption = null;
		if (mDocAll != null && mDocAll.getCodMotivazioneNonInvio() != null) {
			lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio(),
					mDocAll.getCodMotivazioneNonInvio());
		} else {
			lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
		}

		setRequestAttribute("motivoNonInvio", "" + lOption);
		setRequestAttribute("provvSige", provvSigeModel);
		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("documentoAllegato", mDocAll);
		setRequestAttribute("modalita", "M"); // Modalità di modifica

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		if (!super.isRequestParameterNullEmptyObj("Provenienza")) {
			String provenienza = super.getRequestStringParameter("Provenienza");
			if (provenienza.equalsIgnoreCase("ModificaFCTastoFunzione")) {
				siap.sige.documentoallegato.model.DocumentoAllegatoModel foglioComplementare = this
						.getFoglioComplementare();
				super.setRequestAttribute("documentoAllegato", foglioComplementare);
				return PG_LOADINSERISCICOMPFOGLIOCOMP_TASTO_FUNZIONE;
			}
		}

		return PG_LOADINSERISCICOMPFOGLIOCOMP;
	}

	/**
	 * Metodo che si occupa di eseguire la ricerca di documenti allegati annullati. L'insieme, opportunamente
	 * filtrato viene inviato in request.
	 * <p>
	 * 
	 * @param aIdEvento
	 *            id evento.
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	@SuppressWarnings("rawtypes")
	private void ricercaAnnullate(BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ricercaAnnullate: inizio");
		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata

		if (mDocAll == null || mDocAll.getDataAnnullamento() == null) {
			Vector lCFCAnnullati = mDocAllCtrl.ExRicercaDocAllAnnulatiByIdEventoCodTipo(aIdEvento, "06");
			setRequestAttribute("DocAllNulli", lCFCAnnullati);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ricercaAnnullate: fine");
	}

	private siap.sige.documentoallegato.model.DocumentoAllegatoModel getFoglioComplementare()
			throws F3BException {

		BigDecimal idDocAll = this
				.getRequestBigDecimalParameter(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);
		siap.sige.documentoallegato.controller.IDocumentoAllegato ctrl = SIGELookupRemote
				.getDocumentoAllegatoController();
		return ctrl.ExRicercaFCById(idDocAll);
	}

}