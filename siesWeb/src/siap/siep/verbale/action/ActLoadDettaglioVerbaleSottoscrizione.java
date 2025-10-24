package siap.siep.verbale.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * ActLoadDettaglioVerbaleSottoscrizione - Classe Action per la load dettaglio di Verbale di Sottoscrizione
 *
 * @version 1.0
 */
public class ActLoadDettaglioVerbaleSottoscrizione extends ActSIESDettaglioProvvedimento
		implements ICostantiVerbale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdVerbale = null;
		BigDecimal lIdEvento = null; // Id dell'evento collegato al verbale
		VerbaleModel lVerMod = new VerbaleModel();
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		String lDettaglioProvvedimento = "SI";

		// NOn esiste Verbale - Siamo nel caso del
		// dettalgio dell'evento da elenco PM. -
		// In generale il processo termina con la Form
		// del Dettaglio - Dettaglio = SI
		// Esiste solo l'id dell'evento, ricerco il
		// verbale da quello
		if (isRequestParameterNullObj(CAMPO_ID_VERBALE)) {
			lIdEvento = getRequestBigDecimalParameter(
					siap.sico.evento.action.ICostantiEvento.CAMPO_ID_EVENTO);
			lVerMod = lCtrl.ExRicercaVerbaleObblighiByIdEvento(lIdEvento);
			setRequestAttribute("dettaglioProvvedimento", "SI");
			lDettaglioProvvedimento = "SI";
		} else {
			// Siamo nel caso del verbale appena inserito - Processo Registra data Inizio Misura
			// il processo NON termina con la Form del Dettaglio - Dettaglio = NO
			// Provengo dalla ActInserisciVerbaleSottoscrizione
			lIdVerbale = getRequestBigDecimalParameter(CAMPO_ID_VERBALE);
			lVerMod = lCtrl.ExRicercaVerbaleByKey(lIdVerbale);
			lIdEvento = lVerMod.getEveIdEvento();
			setRequestAttribute("dettaglioProvvedimento", "NO");
			lDettaglioProvvedimento = "NO";
		}

		BigDecimal lIdFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		// model cssa
		CSSAModel lCssaMod = new CSSAModel();
		if (lVerMod != null && lVerMod.getCssIdCssa() != null
				&& lVerMod.getCssIdCssa().compareTo(new BigDecimal(0)) != 0) {
			ICSSA lCtrlCssa = SICOLookupRemote.getCSSARemote();
			lCssaMod = lCtrlCssa.getCSSAByKey(lVerMod.getCssIdCssa());
		}
		setRequestAttribute("cssa", lCssaMod);

		// model Istituto Detenzione
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
		if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null
				&& !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lVerMod.getIstDetIdIstitutoDetenzione());
		}
		setRequestAttribute("istitutodetenzione", lIstMod);

		// Ricerca l'ordinanza/decreto di concessione puntato (eve_id_evento) dal verbale
		EventoModel lEveMod = new EventoModel();
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrlEve.ExRicercaEventoByKey(lIdEvento);

		// Recupera la Misura Alternativa di concessione. n.b. l'evento verbale
		// punta (eve_id_evento) l'ordinanza di concessione
		MisuraAlternativaModel lMisAltMod = new MisuraAlternativaModel();
		IMisuraAlternativa lCtrlMisAlt = SICOLookupRemote.getMisuraAlternativaRemote();
		lMisAltMod = lCtrlMisAlt.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEveIdEvento());

		// MEV_2019-09-SIEP mi serve anche l'evento decreto/ord puntato dalla MA per testare l'esito
		EventoModel provvSorv = lCtrlEve.ExRicercaEventoByKey(lMisAltMod.getEveIdEvento());
		setRequestAttribute("provvSorv", provvSorv);
		// MEV_2019-09-SIEP - FINE

		// posizione giuridica corrente ???
		/* PosizioneGiuridicaModel lPosMododel = */getPosizioneGiuridica(lIdEvento, lIdFasc);

		// posizione giuridica precedente ???
		// PosizioneGiuridicaModel lPosPre = null;
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* lPosPre = */lCtrlPos
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("verbale", lVerMod);
		setRequestAttribute("misuraalternativa", lMisAltMod);

		// Se la pena associata al Verbale risulta non validata o assente e l'ultima
		// Pena sul fascicolo risulta Non Validata, devo andare al calcolo pena,
		// quindi metto lDettaglioProvvedimento = "NO";
		PenaResiduaModel lPenMod = getPenaResidua(lIdEvento, lIdFasc);
		if (lPenMod != null && lPenMod.getFlagValidato() != null && !lPenMod.getFlagValidato().equals("S")) {
			setRequestAttribute("dettaglioProvvedimento", "NO");
			lDettaglioProvvedimento = "NO";
		}

		String lPage = "";
		if (lDettaglioProvvedimento.equals("NO")) { // Se dattaglio = NO vado al calcolo pena
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.verbale.action.ActCalcoloPenaVerbaleSottoscrizione";

		} else {
			setRequestAttribute("penaresidua", lPenMod);

			if (!"A".equals(lEveMod.getFlagDocumentoRegistrato())) {
				// Sto visualizzando il dettaglio del verbale con data fine pena validata
				// Provengo dall'elenco provvedimenti del PM con pena validata.
				// Controllo se presente il provvedimento di decorrenza scadenza, in caso
				// negativo consento all'utente di inserirlo

				// n.b. all'ordinanza/decreto di concessione sono legati puù eventi:
				// 1) il provvedimento SIEP di
				// 2) il verbale (evento) di sottomissione
				// 3) il provvedment decorrenza scadenza
				// Se manca quest'ultimo provvedimento devo consentire all'utente di
				// inserirlo a partire dal dettaglio del verbale
				EventoModel lEveModelRicerca = new EventoModel();
				lEveModelRicerca.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
				lEveModelRicerca.setFlagDocumentoRegistrato("S");

				IEvento lCtrlEven = SICOLookupRemote.getEventoRemote();
				Vector lListaEventiValidati = lCtrlEven.ExRicercaEvento(lEveModelRicerca);

				BigDecimal idEventoOrdinanzaDecreto = lEveMod.getEveIdEvento();
				int lConta = 0;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lListaEventiValidati.size() = " + lListaEventiValidati.size());
				for (int i = 0; i < lListaEventiValidati.size(); i++) {
					EventoModel lEveModFasc = (EventoModel) lListaEventiValidati.elementAt(i);
					if (lEveModFasc.getEveIdEvento() != null
							&& idEventoOrdinanzaDecreto.compareTo(lEveModFasc.getEveIdEvento()) == 0) {
						// Ho trovato un evento che punta l'ordinanza e non è l'envento Verbale
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lEveModFasc = " + lEveModFasc);
						lConta = lConta + 1;
					}
				}

				if (lConta == 2) // Presente sol il provv di concessione e il verbale
					setRequestAttribute("caricaDecorrenzaScadenza", "SI");
			}

			lPage = PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_SOTTOSCRIZIONE;
		}

		/*
		 *
		 * if (lPosMododel != null && lPosPre != null && lPosMododel.getCodPosizioneGiuridica() != null &&
		 * lPosPre.getCodPosizioneGiuridica() != null && (lPosMododel.getCodPosizioneGiuridica().equals("12")
		 * || lPosMododel.getCodPosizioneGiuridica().equals("13") ||
		 * lPosMododel.getCodPosizioneGiuridica().equals("14") ||
		 * lPosMododel.getCodPosizioneGiuridica().equals("27") ||
		 * lPosMododel.getCodPosizioneGiuridica().equals("29") ||
		 * lPosMododel.getCodPosizioneGiuridica().equals("51")) && (lPosPre.isLibero() ||
		 * lPosPre.getCodPosizioneGiuridica().equals("03"))) {
		 *
		 * // setRequestAttribute("dettaglioProvvedimento", "NO");
		 *
		 *
		 * lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		 * "=siap.siep.verbale.action.ActCalcoloPenaVerbaleSottoscrizione"; } else {
		 *
		 * // setRequestAttribute("dettaglioProvvedimento", "SI");
		 *
		 *
		 *
		 *
		 * PenaResiduaModel lPenMod = getPenaResidua(lIdEvento, lIdFasc);
		 *
		 * if (lPenMod != null && lPenMod.getFlagValidato() != null && !lPenMod.getFlagValidato().equals("S"))
		 * setRequestAttribute("dettaglioProvvedimento", "NO");
		 *
		 * setRequestAttribute("penaresidua", lPenMod);
		 *
		 * if (lPenMod != null && lPenMod.getDataFinePresunta() != null && lPenMod.getDataFineReclusione() !=
		 * null) { vedoDataIntermedia = "S"; } if (!isRequestAttributeNullObj("vedoDataIntermedia")) {
		 * setRequestAttribute("vedoDataIntermedia", getRequestAttribute("vedoDataIntermedia")); } else {
		 * setRequestAttribute("DettaglioDaElenco", "SI"); }
		 *
		 * lPage = PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_SOTTOSCRIZIONE; }
		 */
		return lPage;
	}

}