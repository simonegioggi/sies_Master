package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaSigeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/*******************************************************************************
 * Classe action per la Load del dettaglio dei Provvedimenti dei Applicazione delle Decisioni del GE -
 * Amnistia/Indulto (04-0284 - Applicazione Amnistia / Indulto ) - Depenalizzazione (04-0285 - Applicazione
 * depenalizzazione) - Incostituzionalità (04-2086 - Applicazione incostituzionalita)
 *
 * Tali provvedimenti non prevedono allo stato attuale (31/07/2007) un dettaglio, per cui quando il
 * provvedimento (non ancora validato) compare nell'elenco dei provvedimenti del PM o nel dettaglio del
 * procedimento viene agganciato il provvedimento generico generando un errore in fase di validazione.
 *
 * Questa action viene invocata <b>SOLO</b> dall'elenco dei Provvedimento del PM con evento validato o meno, o
 * dal dettaglio del procedimento con evento non validato. Se l'evento è validato viene richiamata una pagina
 * di visualizzazione del dettaglio. Se l'evento non è validato vengono richiamate le action load per i
 * singoli tipi di provvedimento, le stesse invocate dalla prima maschera di inserimento: menu: Decisioni del
 * GE - Applicazione Benefici
 * 
 */
public class ActLoadDettaglioProvvedimentiDecGE extends ActSIESDettaglioProvvedimento
		implements ICostantiAnnotazioneManuale {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ==========================================================================
		// Recupero il prvvedimento per verificare se è validato validate
		// ==========================================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveProvvMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

		if (lEveProvvMod == null) {
			// rilanciare eccezione
		}

		if (lEveProvvMod.getFlagDocumentoRegistrato() != null
				&& lEveProvvMod.getFlagDocumentoRegistrato().equalsIgnoreCase("S")) {
			// Provvedimento VALIDATO devo visualizzare la pagina di dettaglio

			// Devo recuperare gli estremi dell'ordinanza, ma non esiste un legame tra
			// il provvedimento e l'ordinanza. Manca l'eve_id_evento sul provvedimento
			// Posso recuperare solo le annotazioni che sono legate al provvedimento.
			// E' l'ordinanza che punta il provvedimento
			IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
			Vector lListAnnMan = lCtrlAnnMan
					.ExRicercaAnnotazioneManualeByIdEvento(lEveProvvMod.getIdEvento());
			setRequestAttribute("ListaAnnotazioni", lListAnnMan);
			setRequestAttribute("ProvvedimentoMod", lEveProvvMod);

			AnnotazioneOrdinanzaModel lAnnOrdMod = new AnnotazioneOrdinanzaModel();

			// Recupero l'ordinanza che punta il provvedimento
			EventoModel lEveRicerca = new EventoModel();
			lEveRicerca.setFasSieIdFascicoloSiep(lIdFascicolo);
			lEveRicerca.setCodTipoEvento("01");
			lEveRicerca.setCodTipoProvvedimento("03");
			lEveRicerca.setCodMotivo(lEveProvvMod.getCodMotivo());

			try {
				Vector lListaOrd = lCtrlEvento.ExRicercaEvento(lEveRicerca);
				for (int i = 0; i < lListaOrd.size(); i++) {
					EventoModel lEveOrd = (EventoModel) lListaOrd.elementAt(i);
					if (lEveOrd.getEveIdEvento() != null
							&& lEveOrd.getEveIdEvento().compareTo(lEveProvvMod.getIdEvento()) == 0) {
						lAnnOrdMod.setEvento(lEveOrd);
						break;
					}
				}
			} catch (Exception e) {
				// se non trova l'ordinanza
			}

			if (lAnnOrdMod.getEvento() != null) {
				try {
					Vector lListaAnnGE = lCtrlAnnMan
							.ExRicercaAnnotazioneManualeByIdEvento(lAnnOrdMod.getEvento().getIdEvento());
					if (lListaAnnGE != null && lListaAnnGE.size() > 0) {
						lAnnOrdMod.setAnnotazioneManuale((AnnotazioneManualeModel) lListaAnnGE.elementAt(0));
					}
				} catch (Exception e) {
					// se non trova le annotazioni
				}
				setRequestAttribute("AnnotazioneOrdinanza", lAnnOrdMod);
			} else if (lListAnnMan != null && lListAnnMan.size() > 0) {
				// Si cerca Ordinanza del GE legata al Provvedimento attraverso l'Annotazione
				AnnotazioneOrdinanzaSigeModel lAnnOrdSigeMod = lCtrlAnnMan
						.ExRicercannotazioneManualeOrdinanzaSigeByIdAnnMan(
								((AnnotazioneManualeModel) lListAnnMan.get(0)).getAnnoIdAnnotazioneManuale());
				setRequestAttribute("AnnotazioneOrdinanza", lAnnOrdSigeMod);
			}

			String lPage = "";
			// AMBROS a8-rr-222 05/2013 Nela caso vengo dall'elenco provvedimenti e devo andare sul
			// Dettaglio Provvedimento Amnistia/Indulto
			if (lEveProvvMod.getCodMotivo().equals("0284")) {
				String lFlagPage = "AMNI";
				setRequestAttribute("lFlagPage", lFlagPage);
				setRequestAttribute("lPageGE", lFlagPage);
				// lPage = IWebConstants.ROOT_DIR
				// +"/files/siap/siep/calcolopena/DettaglioAnnotazioniManuali.jsp";
				lPage = IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/DettaglioProvvApplicazioneDecGE.jsp";
			} else
				// END AMBROS
				// Provare a recupeare anche i dati dell'ordinanza (stesso codice)
				lPage = IWebConstants.ROOT_DIR
						+ "/files/siap/siep/calcolopena/DettaglioProvvApplicazioneDecGE.jsp";
			// Questa istruzione rilancia sul dettaglio generico
			// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			// "=siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lIdEvento;

			return lPage;
		} else if (lEveProvvMod.getFlagDocumentoRegistrato() != null
				&& lEveProvvMod.getFlagDocumentoRegistrato().equalsIgnoreCase("A")) { // Anche gli annullati
																						// hanno il dettaglio
																						// ma in in questo
																						// caso le annotazioni
																						// sono state
																						// fisicamente
																						// cancellate per cui
																						// aggancio il
																						// dettaglio generico
																						// non avendo altri
																						// dati da
																						// visualizzare

			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEvento;
			return lPage;
		} else {

			String lPage = "";
			// Attivare questo codice per andare direttamente sulla pagina delle stampe
			// delle decisioni del GE
			// ==========================================================================
			// Pena Complessiva (utilizzata dalla IntestazionePenaValidataAnnotazioni.jsp)
			// importata dalle pagine di stampa
			// ==========================================================================
			// IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
			// PenaComplessivaModel lPenComMod =
			// ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
			// setRequestAttribute("PenaComplessivaSentenza", lPenComMod);
			//
			// // Ultima pena residua (validata o meno)
			// IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
			// PenaResiduaModel lPenRes =
			// lCtrlPenaRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
			// setRequestAttribute("PenaResidua", lPenRes);
			// lPage = IWebConstants.ROOT_DIR
			// +"/files/siap/siep/calcolopena/VediCalcoloPenaValidataAnnotazioni.jsp";

			// Attivare questo codice per reindirizzare sulla pagina di dettaglio delle
			// annotazioni come se la chiamata fosse stata effettuata direttamente dalla
			// pagina - Decisioni Del GE
			if (lEveProvvMod.getCodMotivo().equalsIgnoreCase("0284")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniAmnistiaIndulto";
			} else if (lEveProvvMod.getCodMotivo().equalsIgnoreCase("0285")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniDepenalizzazione";
			} else if (lEveProvvMod.getCodMotivo().equalsIgnoreCase("0286")) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniIncostituzionalita";
			}

			return lPage;
		}
	}
}