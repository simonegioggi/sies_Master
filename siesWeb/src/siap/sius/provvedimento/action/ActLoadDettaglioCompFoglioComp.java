package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.html.Option;
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
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActLoadDettaglioCompFoglioComp - Azione specializzazione per la visualizzazione del Dettaglio di un Foglio
 * Complementare
 *
 * @version 1.0
 */
public class ActLoadDettaglioCompFoglioComp extends ActionSiap implements ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	DocumentoAllegatoModel dam = null;
	IDocumentoAllegato ida = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Parametro per individuare se provengo dall'icona "Compilazione Foglio Complementare".
		String provenienza = null;
		if (!isRequestParameterNullObj("Provenienza")) {
			provenienza = getRequestStringParameter("Provenienza");
			setRequestAttribute("provenienza", provenienza);
		}

		// Inserimento Foglio Complementare sulla tabella DOCUMENTO_ALLEGATO
		if (provenienza != null && provenienza.equals("InsertFC")) {
			BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

			// Controller del DocumentoAllegato
			ida = SIUSLookupRemote.getDocumentoAllegatoRemote();

			dam = ida.ExRicercaDocumentoAllegatoByIdEventoCodTipo(idEvento, "06");

			ricercaAnnullate(idEvento);

			// Evento. Leggo l'evento collegato al decreto.
			EventoModel em = new EventoModel();
			IEvento ie = SICOLookupRemote.getEventoRemote();
			em = ie.ExRicercaEventoByKey(idEvento);

			// 25/09/2007 Anomalia SIUS - Compilazione Foglio Complementare inibito per provvedimento
			// annullato.
			if (em.getFlagDocumentoRegistrato().compareTo("A") == 0)
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Non è possibile emettere il Foglio Complementare per un provvedimento annullato");

			// MEV_2024-092: per i procedimenti con contenuto 'Concessione Misure Alternative alla Detenzione
			// (art. 678 comma 1 ter c.p.p.)' (C050 e C051), la possibilità di inserire il Foglio
			// complementare solo se sull'ordinanza di definizione del procedimento e' presente la data di
			// esecutivita'
			FascicoloGPModel fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			if (!Utils.isNullObj(fgpm) && !Utils.isNullObj(fgpm.getGeneraleProcedimentoModel())
					&& !Utils.isNullObj(fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento())
					&& (fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("C050") == 0
							|| fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
									.compareTo("C051") == 0)
					&& ("0270".equals(em.getCodEsito()) && "03".equals(em.getCodTipoProvvedimento()))) {
				IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel dopcm = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
						fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "AM");
				if (!Utils.isNullObj(dopcm) && Utils.isNullObj(dopcm.getDataEsecutivita())) {
					siesLogger.debug("Non è possibile emettere il Foglio Complementare per un provvedimento di "
							+ "Applicazione Misure Alternative DL 123/2018 senza Data di Esecutivit&agrave; "
							+ "valorizzata!");
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Non è possibile emettere il Foglio Complementare per un provvedimento di "
							+ "Applicazione Misure Alternative DL 123/2018 senza Data di Esecutivit&agrave; "
							+ "valorizzata!");
				}
			}

			// Valorizzazione del Documento Allegato
			DocumentoAllegatoModel damNew = new DocumentoAllegatoModel();
			damNew.setDataEmissione(DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")), "dd/MM/yyyy"));
			damNew.setCodTipoDocumento("06");
			damNew.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));
			damNew.setDataTrasmissione(null);
			damNew.setEveIdEvento(idEvento);
			damNew.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			damNew.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			damNew.setDataInserimento(DateUtils.getSysDate());
			damNew.setComuneSedeGiudiziaria(null);

			// MEV10-s3: per i minori è possibile gestire la sentenza come FC
			// Prelevo Sentenza
			String codTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio();
			if ("TDSM".equalsIgnoreCase(codTipoUff) || "UDSM".equalsIgnoreCase(codTipoUff)) {
				DepositoSentenzaModel dsm = null;
				if (em.getCodTipoProvvedimento().equals("01")) {
					IDepositoSentenza ids = SIUSLookupRemote.getDepositoSentenzaRemote();
					dsm = ids.ExRicercaDepositoSentenzaByEvento(idEvento);
					damNew = ida.ExInserisciFoglioComplementare(damNew, em.getCodTipoProvvedimento(),
							dsm.getIdDepositoSentenza());
					setRequestAttribute("depositoSentenza", dsm);
					setRequestAttribute("depositoDecreto", null);
					setRequestAttribute("depositoordinanzapc", null);
				}
			}

			// Prelevo Decreto
			DepositoDecretoModel ddm = null;
			if (em.getCodTipoProvvedimento().equals("02")) {
				IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
				ddm = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(idEvento);
				damNew = ida.ExInserisciFoglioComplementare(damNew, em.getCodTipoProvvedimento(),
						ddm.getIdDepositoDecreto());
				setRequestAttribute("depositoDecreto", ddm);
				setRequestAttribute("depositoordinanzapc", null);
				// MEV10-s3: aggiunta impostazione di attributo nella richiesta
				setRequestAttribute("depositoSentenza", null);
			}

			// Prelevo Ordinanza
			DepositoOrdinanzaPcModel dopcm = null;
			if (em.getCodTipoProvvedimento().equals("03")) {
				IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				dopcm = idop.ExRicercaDepositoOrdinanzaPcByEvento(idEvento);
				damNew = ida.ExInserisciFoglioComplementare(damNew, em.getCodTipoProvvedimento(),
						dopcm.getIdDepositoOrdinanzaPc());
				setRequestAttribute("depositoordinanzapc", ddm);
				setRequestAttribute("depositoDecreto", null);
				// MEV10-s3: aggiunta impostazione di attributo nella richiesta
				setRequestAttribute("depositoSentenza", null);
			}

			// Imposta la Combo contenente le Motivazioni non Inviato FC.
			Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
			setRequestAttribute("motivoNonInvio", "" + lOption);
			setRequestAttribute("documentoAllegato", damNew);
			setRequestAttribute("evento", em);

			// return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			// "=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&"+
			// ICostantiEvento.CAMPO_ID_EVENTO+"="+em.getIdEvento().toString();
			return PG_LOADDETTAGLIOCOMPFOGLIOCOMP;
		} else {
			// ********************************* DETTAGLIO
			// Gestione del punto di ritorno
			setLinkRitorno();
			// UC passo1
			IDocumentoAllegato idaNew = SIUSLookupRemote.getDocumentoAllegatoRemote();
			DocumentoAllegatoModel damNew = null;
			BigDecimal idEvento = null;
			if (!isRequestParameterNullObj(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO)) {
				// Ricerca del Foglio Complementare
				BigDecimal idDocAll = this.getRequestBigDecimalParameter(
						ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);
				damNew = idaNew.ExRicercaDocumentoAllegatoByKey(idDocAll);
				idEvento = damNew.getEveIdEvento();
			} else {
				// Ricerca del Foglio Complementare
				idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
				damNew = idaNew.ExRicercaDocumentoAllegatoByIdEventoCodTipo(idEvento, "06");
			}

			// Evento. Leggo l'evento collegato al decreto/ordinanza.
			EventoModel em = null;
			IEvento ie = SICOLookupRemote.getEventoRemote();
			em = ie.ExRicercaEventoByKey(idEvento);
			if (em == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Documento Allegato assente");

			// Prelevo Decreto
			DepositoDecretoModel ddm = null;
			if (em.getCodTipoProvvedimento().equals("02")) {
				IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
				ddm = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(em.getIdEvento());
			}

			// Prelevo Ordinanza
			DepositoOrdinanzaPcModel dopm = null;
			if (em.getCodTipoProvvedimento().equals("03")) {
				IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				dopm = idop.ExRicercaDepositoOrdinanzaPcByEvento(em.getIdEvento());
			}

			setRequestAttribute("evento", em);
			setRequestAttribute("documentoAllegato", damNew);
			setRequestAttribute("depositoDecreto", ddm);
			setRequestAttribute("depositoordinanzapc", dopm);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			return PG_LOADDETTAGLIOCOMPFOGLIOCOMP;
		}
	}

	@SuppressWarnings("rawtypes")
	private void ricercaAnnullate(BigDecimal aIdEvento) throws Exception {

		// Ricerca eventuali impugnazioni annullate solo se l'impugnazione stessa non annullata
		if (dam == null || dam.getDataAnnullamento() == null) {
			Vector lCFCAnnullati = ida.ExRicercaDocAllAnnulatiByIdEventoCodTipo(aIdEvento, "06");
			setRequestAttribute("DocAllNulli", lCFCAnnullati);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".ricercaAnnullate: fine");
		}
	}

}