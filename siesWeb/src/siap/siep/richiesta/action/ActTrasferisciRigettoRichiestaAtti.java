package siap.siep.richiesta.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActTrasferisciRigettoRichiestaAtti
 * </p>
 * <p>
 * Description: Si occupa di inviare la comunicazione di: RIGETTO Richiesta
 * </p>
 * <p>
 * di Trasmissione Atti per Competenza
 * </p>
 */

public class ActTrasferisciRigettoRichiestaAtti extends ActionSiap
		implements ICostantiRichiesta, ICostantiJMS {

	public String processRequest() throws Exception {
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ricerca evento competenza
		ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
		CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);
		setRequestAttribute("competenza", mComp);

		// destinatari
		String lCodiceUfficioDest = mComp.getCodUfficioAutoritaComp();
		UfficioModel lUfficioDestModel = getUfficioByCodUfficio(lCodiceUfficioDest);
		UfficioModel lBDIDest = getUfficioByCodUfficio(lUfficioDestModel.getCodDistretto());

		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		// ---------------------------------------------------------
		// MESSAGGIO di RIGETTO Richiesta
		// ---------------------------------------------------------
		MessaggioModel lMessage = null;

		// ==========================================================================
		// Ottimizzazione BLOB: su trasmissione RIGETTO non valorizzo MAI il blob per evitare
		// di sovraccaricare la tabella MESSAGGIO. Il destinatario prenderà
		// i dati direttamente dal DB.

		lMessage = new MessaggioModel();
		lMessage.setTreeModel(new TreeModel());

		// ==========================================================================

		// STATO PROCEDIMENTO ????
		// Prendo dal TreeModel il Dettaglio del Fasciocolo per modificare lo stato procedimento
		/*
		 * TreeModel ltree = lMessage.getTreeModel(); StatoProcedimentoModel lStat = new
		 * StatoProcedimentoModel(); ltree.findTreeModel(ltree, lStat); lStat.setCodStatoProcedimento("0347");
		 */

		lMessage.setDescrBdiDestinataria(lBDIDest.getDescrComune());
		lMessage.setCodBdiDestinataria(lBDIDest.getCodUfficio());
		lMessage.setCodUfficioDestinatario(lCodiceUfficioDest);

		lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
		lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());

		lMessage.setCodTipoMessaggio(ESITO);
		lMessage.setCodTipoOperazione(RIGETTO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);
		lMessage.setDataInvio(DateUtils.getSysDate());

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals("")) {
			lMessage.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
		}

		// Riferimento al Messaggio di Richiesta Trasmissione
		if (mComp != null && mComp.getIdCompetenza() != null && mComp.getIdMessaggioRichiesta() != null)
			lMessage.setIdRichiesta(mComp.getIdMessaggioRichiesta());

		// dati soggetto
		if (mComp.getNome_Soggetto_Rich() != null)
			lMessage.setNomeSoggetto(mComp.getNome_Soggetto_Rich());

		if (mComp.getCognome_Soggetto_Rich() != null)
			lMessage.setCognomeSoggetto(mComp.getCognome_Soggetto_Rich());

		if (mComp.getDataNascita_Soggetto_Rich() != null)
			lMessage.setDataNascita(mComp.getDataNascita_Soggetto_Rich());

		if (mComp.getCodComuneNascita_Soggetto_Rich() != null)
			lMessage.setCodComuneNascita(mComp.getCodComuneNascita_Soggetto_Rich());

		if (mComp.getCodStatoNascita_Soggetto_Rich() != null)
			lMessage.setCodStatoNascita(mComp.getCodStatoNascita_Soggetto_Rich());

		// SETTA RIFERIMENTI FASCICOLO SIEP
		lMessage.setChiaveAnnoSiep(lFascicoloModel.getChiaveAnno());
		lMessage.setChiaveProgrSiep(lFascicoloModel.getChiaveProgr());
		lMessage.setChiaveUfficioSiep(lFascicoloModel.getChiaveUfficio());

		lMessage.setChiaveAnnoFasCumulante(mComp.getChiaveAnno());
		lMessage.setChiaveProgrFasCumulante(mComp.getChiaveProgr());
		lMessage.setChiaveUfficioFasCumulante(mComp.getChiaveUfficio());

		// Invio il messaggio
		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		// AGGIORNO LA DATA DI TRASMISSIONE DELL'EVENTO
		IEvento lCrtlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lModel = lCrtlEve.ExRicercaEventoByKey(lIdEvento);

		// EventoModel lModel = new EventoModel();
		// lModel.setIdEvento(lIdEvento);
		lModel.setFlagDocumentoRegistrato("S");
		lModel.setDataTrasmissioneAtti(DateUtils.getSysDate());

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lCtrl.ExModificaEvento(lModel);

		// ====================
		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Provvedimento sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.richiesta.action.ActDettaglioRigettoRichiestaAtti");
		lRedirigi.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString());

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
		// ================================================================================

	}
}
