package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaCompFoglioComp
 * </p>
 * <p>
 * Description: Azione specializzazione per la modifica di un foglio complementare legato al fascicolo SIUS.
 * <p>
 * Copyright: Eutelia S.p.A.(c) 2007
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadModificaCompFoglioComp extends ActionSius implements ICostantiProvvedimento {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	DocumentoAllegatoModel mDocAll = null;
	IDocumentoAllegato mDocAllCtrl = null;

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		lockApplicativo("FoglioComplementare"); // abilitazione lock applicativo su entità

		String lRetPage = PG_LOADINSERISCICOMPFOGLIOCOMP;

		// Gestione del punto di ritorno
		setLinkRitorno();

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// UC passo1
		mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");

		ricercaAnnullate(lIdEvento);

		// Evento. Legge l'evento collegato al decreto.
		EventoModel lEveMod = new EventoModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// 25/09/2007 Anomalia SIUS - Compilazione Foglio Complementare inibito per provvedimento annullato.
		if (lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0)
			throw new SIUSException(F3BException.USER_MESSAGE,
					"Non è possibile modificare il Foglio Complementare per un provvedimento annullato.");

		// MEV10-s3: per i minori è possibile gestire la sentenza come FC
		String codTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio();
		DepositoSentenzaModel depSenMod = null;
		if ("TDSM".equalsIgnoreCase(codTipoUff) || "UDSM".equalsIgnoreCase(codTipoUff)) {
			if (lEveMod.getCodTipoProvvedimento().equals("01")) {
				IDepositoSentenza lCtrlDS = SIUSLookupRemote.getDepositoSentenzaRemote();
				depSenMod = lCtrlDS.ExRicercaDepositoSentenzaByEvento(lIdEvento);
			}
		}

		// Si Preleva dati del Decreto
		DepositoDecretoModel llDecMod = null;
		if (lEveMod.getCodTipoProvvedimento().equals("02")) {
			IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
			llDecMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
		}

		// Si Preleva dati di Ordinanza
		DepositoOrdinanzaPcModel llDepMod = null;
		if (lEveMod.getCodTipoProvvedimento().equals("03")) {
			IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			llDepMod = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
		}

		// Imposta la Combo contenente le Motivazioni non Inviato FC.
		Option lOption = null;
		if (mDocAll != null && mDocAll.getCodMotivazioneNonInvio() != null) {
			lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio(),
					mDocAll.getCodMotivazioneNonInvio());
		} else {
			lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
		}

		setRequestAttribute("motivoNonInvio", "" + lOption);

		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("documentoAllegato", mDocAll);
		setRequestAttribute("depositoDecreto", llDecMod);
		setRequestAttribute("depositoordinanzapc", llDepMod);
		// MEV10-s3: aggiunta impostazione di attributo nella richiesta
		setRequestAttribute("depositoSentenza", depSenMod);
		setRequestAttribute("modalita", "M"); // Modalità di modifica

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage;
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
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ricercaAnnullate: inizio");
		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata

		if (mDocAll == null || mDocAll.getDataAnnullamento() == null) {
			Vector lCFCAnnullati = mDocAllCtrl.ExRicercaDocAllAnnulatiByIdEventoCodTipo(aIdEvento, "06");
			setRequestAttribute("DocAllNulli", lCFCAnnullati);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ricercaAnnullate: fine");
	}
}