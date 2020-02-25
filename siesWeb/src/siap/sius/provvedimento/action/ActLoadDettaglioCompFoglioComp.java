package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDettaglioCompFoglioComp
 * </p>
 * <p>
 * Description: Azione specializzazione per la visualizzazione del Dettaglio di un Foglio Complementare.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadDettaglioCompFoglioComp extends ActionSiap implements ICostantiProvvedimento {
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

			// Evento. Leggo l'evento collegato al decreto.
			EventoModel lEveMod = new EventoModel();
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

			// 25/09/2007 Anomalia SIUS - Compilazione Foglio Complementare inibito per provvedimento
			// annullato.
			if (lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0)
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Non è possibile emettere il Foglio Complementare per un provvedimento annullato");

			// Valorizzazione del Documento Allegato
			DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
			aDocAllegato.setDataEmissione(DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")),
					"dd/MM/yyyy"));

			aDocAllegato.setCodTipoDocumento("06");
			aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));
			aDocAllegato.setDataTrasmissione(null);
			aDocAllegato.setEveIdEvento(lIdEvento);
			aDocAllegato.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			aDocAllegato.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			aDocAllegato.setDataInserimento(DateUtils.getSysDate());
			aDocAllegato.setComuneSedeGiudiziaria(null);

			// MEV10-s3: per i minori è possibile gestire la sentenza come FC
			// Prelevo Sentenza
			String codTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio();
			if ("TDSM".equalsIgnoreCase(codTipoUff) || "UDSM".equalsIgnoreCase(codTipoUff)) {
				DepositoSentenzaModel depSenMod = null;
				if (lEveMod.getCodTipoProvvedimento().equals("01")) {
					IDepositoSentenza lCtrlDS = SIUSLookupRemote.getDepositoSentenzaRemote();
					depSenMod = lCtrlDS.ExRicercaDepositoSentenzaByEvento(lIdEvento);
					aDocAllegato = mDocAllCtrl.ExInserisciFoglioComplementare(aDocAllegato,
							lEveMod.getCodTipoProvvedimento(), depSenMod.getIdDepositoSentenza());
					setRequestAttribute("depositoSentenza", depSenMod);
					setRequestAttribute("depositoDecreto", null);
					setRequestAttribute("depositoordinanzapc", null);
				}
			}

			// Prelevo Decreto
			DepositoDecretoModel llDecMod = null;
			if (lEveMod.getCodTipoProvvedimento().equals("02")) {
				IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
				llDecMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
				aDocAllegato = mDocAllCtrl.ExInserisciFoglioComplementare(aDocAllegato,
						lEveMod.getCodTipoProvvedimento(), llDecMod.getIdDepositoDecreto());
				setRequestAttribute("depositoDecreto", llDecMod);
				setRequestAttribute("depositoordinanzapc", null);
				// MEV10-s3: aggiunta impostazione di attributo nella richiesta
				setRequestAttribute("depositoSentenza", null);
			}

			// Prelevo Ordinanza
			DepositoOrdinanzaPcModel llDepMod = null;
			if (lEveMod.getCodTipoProvvedimento().equals("03")) {
				IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				llDepMod = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
				aDocAllegato = mDocAllCtrl.ExInserisciFoglioComplementare(aDocAllegato,
						lEveMod.getCodTipoProvvedimento(), llDepMod.getIdDepositoOrdinanzaPc());
				setRequestAttribute("depositoordinanzapc", llDecMod);
				setRequestAttribute("depositoDecreto", null);
				// MEV10-s3: aggiunta impostazione di attributo nella richiesta
				setRequestAttribute("depositoSentenza", null);
			}

			// Imposta la Combo contenente le Motivazioni non Inviato FC.
			Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
			setRequestAttribute("motivoNonInvio", "" + lOption);
			setRequestAttribute("documentoAllegato", aDocAllegato);
			setRequestAttribute("evento", lEveMod);

			// return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			// "=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString();
			return PG_LOADDETTAGLIOCOMPFOGLIOCOMP;

		} else {

			// ********************************* DETTAGLIO

			// Gestione del punto di ritorno
			setLinkRitorno();
			// UC passo1
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

			// Evento. Leggo l'evento collegato al decreto/ordinanza.
			EventoModel lEveMod = null;
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
			if (lEveMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Documento Allegato assente");

			// Prelevo Decreto
			DepositoDecretoModel llDecMod = null;
			if (lEveMod.getCodTipoProvvedimento().equals("02")) {
				IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
				llDecMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lEveMod.getIdEvento());
			}

			// Prelevo Ordinanza
			DepositoOrdinanzaPcModel llDepMod = null;
			if (lEveMod.getCodTipoProvvedimento().equals("03")) {
				IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				llDepMod = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getIdEvento());
			}

			setRequestAttribute("evento", lEveMod);
			setRequestAttribute("documentoAllegato", lDocAll);
			setRequestAttribute("depositoDecreto", llDecMod);
			setRequestAttribute("depositoordinanzapc", llDepMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			return PG_LOADDETTAGLIOCOMPFOGLIOCOMP;

		}

	}

	@SuppressWarnings("rawtypes")
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