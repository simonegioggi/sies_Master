package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
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
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciCompFoglioComp
 * </p>
 * <p>
 * Description: Azione specializzazione per l'insermento dei Provvedimenti legati al fascicolo SIUS.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActLoadInserisciCompFoglioComp extends ActionSius implements ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	DocumentoAllegatoModel mDocAll = null;
	IDocumentoAllegato mDocAllCtrl = null;

	public String processRequest() throws Exception {

		String lRetPage = PG_LOADDETTAGLIOCOMPFOGLIOCOMP;

		// Gestione del punto di ritorno
		setLinkRitorno();

		// Parametro per individuare se provengo dall'icona "Dettaglio Foglio Complementare".
		String provenienza = getRequestStringParameter("Provenienza");
		setRequestAttribute("provenienza", provenienza);

		// BigDecimal lIdFascicolo = null;
		// if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
		// lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		// } else {
		// FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		// }

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// UC passo1
		mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();

		mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");

		ricercaAnnullate(lIdEvento);

		// Evento. Leggo l'evento collegato al decreto.
		EventoModel lEveMod = new EventoModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// 25/09/2007 Anomalia SIUS - Compilazione Foglio Complementare inibito per provvedimento annullato.
		if (lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0)
			throw new SIUSException(F3BException.USER_MESSAGE,
					"Non è possibile emettere il Foglio Complementare per un provvedimento annullato");

		// Prelevo Decreto
		DepositoDecretoModel llDecMod = null;
		if (lEveMod.getCodTipoProvvedimento().equals("02")) {
			IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
			llDecMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
		}

		// Prelevo Ordinanza
		DepositoOrdinanzaPcModel llDepMod = null;
		if (lEveMod.getCodTipoProvvedimento().equals("03")) {
			IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			llDepMod = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
		}

		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("documentoAllegato", mDocAll);
		setRequestAttribute("depositoDecreto", llDecMod);
		setRequestAttribute("depositoordinanzapc", llDepMod);

		if (mDocAll == null) {
			// Inserimento Foglio Complementare
			lRetPage = PG_LOADINSERISCICOMPFOGLIOCOMP;
			lockApplicativo("FoglioComplementare");
		}

		return lRetPage;
	}

	@SuppressWarnings("rawtypes")
	private void ricercaAnnullate(BigDecimal aIdEvento) throws Exception {
		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata
		if (mDocAll == null || mDocAll.getDataAnnullamento() == null) {
			Vector lCFCAnnullati = mDocAllCtrl.ExRicercaDocAllAnnulatiByIdEventoCodTipo(aIdEvento, "06");
			setRequestAttribute("DocAllNulli", lCFCAnnullati);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".ricercaAnnullate: fine");
		}
	}

}