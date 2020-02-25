package siap.siep.fogliocomplementare.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.provvedimentopm.action.ICostantiProvvedimento;
import siap.sius.SIUSException;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioCompFoglioComp
 * </p>
 * <p>
 * Description: Azione specializzazione per la visualizzazione del Dettaglio di un Foglio Complementare NSC
 * <p>
 * Created: A.S.
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioCompFoglioComp extends ActionSiap implements ICostantiProvvedimento {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	DocumentoAllegatoModel mDocAll = null;
	IDocumentoAllegato mDocAllCtrl = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "processRequest: inizio");

		// Parametro IdEvento se provengo dall'icona "FC".
		String IdEvento = null;
		if (!isRequestParameterNullObj("IdEvento")) {
			IdEvento = getRequestStringParameter("IdEvento");
			setRequestAttribute("IdEvento", IdEvento);
		}

		// Parametro per individuare la Action chiamante
		String azioneChiamante = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE)) {
			azioneChiamante = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE);
			setRequestAttribute("azioneChiamante", azioneChiamante);
		}

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

		if (lDocAll != null && ("01".equals(lDocAll.getCodMotivazioneNonInvio())
				|| "02".equals(lDocAll.getCodMotivazioneNonInvio()))) {
			// DETTAGLIO
			// Gestione del punto di ritorno
			setLinkRitorno();
			// UC passo1
			// Evento. Leggo l'evento collegato al decreto/ordinanza.
			EventoModel lEveMod = null;
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
			if (lEveMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Documento Allegato assente");
			setRequestAttribute("evento", lEveMod);
			setRequestAttribute("documentoAllegato", lDocAll);
			// sono in modifica imposta la Combo contenente le Motivazioni non Inviato FC.
			Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
			setRequestAttribute("motivoNonInvio", "" + lOption);
			setRequestAttribute("evento", lEveMod);
			setRequestAttribute("provenienza", null);
		} else {
			// Parametro per individuare se provengo dall'icona "Compilazione Foglio Complementare".
			String provenienza = null;
			if (!isRequestParameterNullObj("Provenienza")) {
				provenienza = getRequestStringParameter("Provenienza");
				setRequestAttribute("provenienza", provenienza);
			}
			// Inserimento Foglio Complementare sulla tabella DOCUMENTO_ALLEGATO
			if (provenienza != null && (provenienza.equals("InsertFC"))) {
				lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
				// Controller del DocumentoAllegato
				mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
				mDocAll = mDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");
				ricercaAnnullate(lIdEvento);
				// Leggo l'evento
				EventoModel lEveMod = new EventoModel();
				IEvento lCtrl = SICOLookupRemote.getEventoRemote();
				lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
				// 25/09/2007 Anomalia SIUS - Compilazione Foglio Complementare inibito per
				// provvedimento annullato.
				if (lEveMod.getFlagDocumentoRegistrato() != null
						&& lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0)
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Non è possibile emettere il Foglio Complementare per un provvedimento annullato");
				// Valorizzazione del Documento Allegato
				DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
				// MERGE v10: modificata gestione documento allegato, no insert, si update
				DocumentoAllegatoModel docAll = mDocAllCtrl.ricDocAllByIdEvento(lIdEvento);
				if (!Utils.isPresent(docAll)) {
					aDocAllegato.setCodTipoDocumento("06");
					aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));
					aDocAllegato.setEveIdEvento(lIdEvento);
					aDocAllegato.setDataEmissione(
							DateUtils.getDate((DateUtils.getSysDate("dd/MM/yyyy")), "dd/MM/yyyy"));
					aDocAllegato.setDataTrasmissione(null);
					aDocAllegato.setComuneSedeGiudiziaria(null);
					aDocAllegato.setCodOperatoreInserimento(getCodUtenteConnesso());
					aDocAllegato.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					aDocAllegato.setDataInserimento(DateUtils.getSysDate());
					// inserisco Foglio Complementare Nsc
					aDocAllegato = mDocAllCtrl.ExInserisciFoglioComplementareNsc(aDocAllegato,
							lEveMod.getCodTipoProvvedimento());
				} else {
					aDocAllegato.setCodTipoDocumento(docAll.getCodTipoDocumento());
					aDocAllegato.setAnnoFoglioComplementare(docAll.getAnnoFoglioComplementare());
					aDocAllegato.setProgrFoglioComplementare(docAll.getProgrFoglioComplementare());
					aDocAllegato.setEveIdEvento(docAll.getEveIdEvento());
					aDocAllegato.setDataEmissione(docAll.getDataEmissione());
					aDocAllegato.setIdDocumentoAllegato(docAll.getIdDocumentoAllegato());
					aDocAllegato.setCodOperatoreAggiornamento(getCodUtenteConnesso());
					aDocAllegato.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					aDocAllegato.setDataAggiornamento(DateUtils.getSysDate());
					aDocAllegato.setDataAnnullamento(null);
					aDocAllegato.setMotivoAnnullamento(null);
					aDocAllegato.setDataTrasmissione(docAll.getDataTrasmissione());
					aDocAllegato.setComuneSedeGiudiziaria(docAll.getComuneSedeGiudiziaria());
					aDocAllegato.setCodMotivazioneNonInvio(docAll.getCodMotivazioneNonInvio());
					aDocAllegato.setDescrizioneNonInvio(docAll.getDescrizioneNonInvio());
					aDocAllegato.setDataUltInvio(docAll.getDataUltInvio());
					aDocAllegato.setDataInsMan(docAll.getDataInsMan());
					// modifico Foglio Complementare Nsc
					mDocAllCtrl.ExModificaFoglioComplementareNsc(aDocAllegato,
							lEveMod.getCodTipoProvvedimento());
				}
				setRequestAttribute("depositoordinanzapc", null);
				// Imposta la Combo contenente le Motivazioni non Inviato FC.
				Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
				setRequestAttribute("motivoNonInvio", "" + lOption);
				setRequestAttribute("documentoAllegato", aDocAllegato);
				setRequestAttribute("evento", lEveMod);
			}
		}
		// return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString();
		return PG_LOADDETTAGLIOCOMPFOGLIOCOMPNSC;
	}

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

	public boolean isFCIconVisible(String motivoEvento, BigDecimal idEvento) {

		if (motivoEvento == null)
			return false;

		try {
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(idEvento);
			if (lEveNotMod.getEvento().getFlagDocumentoRegistrato() == null
					|| lEveNotMod.getEvento().getFlagDocumentoRegistrato().compareTo("S") != 0)
				return false;
			// MEV 16: cambiata gestione visibilità icona FC
			String lCodMotivo = lEveNotMod.getEvento().getCodMotivo();
			String[] lListaCodSospPena = ICostantiEvento.CODICI_SOSPENSIONE_DELLA_PENA;
			List<String> lCodSospPena = Arrays.asList(lListaCodSospPena);
			String[] lListaCodAvvEsecPena = ICostantiEvento.CODICI_AVVENUTA_ESECUZIONE_PENA;
			List<String> lCodAvvEsecPena = Arrays.asList(lListaCodAvvEsecPena);
			// MEV 16 CUMULO: modificata gestione icone per cumulo;
			// il FC viene inviato una volta chiusa l'istruttoria
			// variabile per far vedere l'icona del FC (per spegnere impostare a false)
			boolean onOffIcon = true; // false
			if (Utils.isPresent(lCodMotivo)) {
				boolean isCumulo = lCtrl.isCumulo(lCodMotivo, idEvento);
				// valore di ritorno
				return lCodSospPena.contains(lCodMotivo) || lCodAvvEsecPena.contains(lCodMotivo)
						|| (isCumulo && onOffIcon);
			}
		} catch (F3BException f3b) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in: " + getClass().getName() + ". Eccezione: " + f3b.toString());
			f3b.printStackTrace();
		}
		// valore di ritorno
		return false;
	}

	public boolean existsFC(BigDecimal idEvento) {

		boolean exists = false;
		try {
			IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
			DocumentoAllegatoModel lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(idEvento,
					"06");
			exists = lDocAll != null;
		} catch (F3BException f3b) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in: " + getClass().getName() + ". Eccezione: " + f3b.toString());
			f3b.printStackTrace();
		}
		// valore di ritorno
		return exists;
	}

	/**
	 * MEV 16: aggiunto metodo di controllo se esiste il FC dalla tabella "EVENTO"
	 *
	 * @param idEvento
	 * @return boolean
	 */
	public boolean existsReallyFC(BigDecimal idEvento) {

		boolean exists = false;
		try {
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			exists = lCtrl.existsReallyFC(idEvento);
		} catch (F3BException f3b) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in: " + getClass().getName() + ". Eccezione: " + f3b.toString());
			f3b.printStackTrace();
		}
		// valore di ritorno
		return exists;
	}

}