package siap.siep.presaincarico.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: ActDettaglioPresaincaricoCompetenza
 * </p>
 * <p>
 * Action per il caricamento dei dati del messaggio di trasmissione per competenza e visualizzazione del
 * dettaglio
 * </p>
 */
public class ActDettaglioPresaincaricoCompetenza extends ActionSiap implements ICostantiPresaincarico {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lIdMessage = null;

		if (!isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			lIdMessage = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		} else if (!isRequestAttributeNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			lIdMessage = (BigDecimal) getRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		}

		MessaggioModel lMess = new MessaggioModel();
		if (lIdMessage != null) {
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
		}

		BigDecimal lIdIstruttoria = null;
		IstruttoriaCumuloModel lIstruttoriaCumuloModel = null;
		IIstruttoriaCumulo lIstrCumCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)) {
			lIdIstruttoria = getRequestBigDecimalParameter(
					ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
			lIstruttoriaCumuloModel = lIstrCumCtrl.ExRicercaIstruttoriaCumuloById(lIdIstruttoria);
			setRequestAttribute("IstruttoriaCumulo", lIstruttoriaCumuloModel);
		}

		setRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lIdMessage);
		setRequestAttribute("Messaggio", lMess);

		// ======================================================
		// ricerco il fascicolo cumulante se esiste
		// ======================================================
		BigDecimal lAnn = lMess.getChiaveAnnoFasCumulante();
		BigDecimal lProg = lMess.getChiaveProgrFasCumulante();

		String lChiaveUffCumulante = lMess.getChiaveUfficioFasCumulante();

		String Messa = "";

		if (lAnn != null && lProg != null && lChiaveUffCumulante != null) {
			FascicoloSiepModel aFas = new FascicoloSiepModel();
			aFas.setChiaveAnno(lMess.getChiaveAnnoFasCumulante());
			aFas.setChiaveProgr(lMess.getChiaveProgrFasCumulante());
			aFas.setChiaveUfficio(lMess.getChiaveUfficioFasCumulante());

			IFascicoloSiep lCrtlFas = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel mFas = lCrtlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFas);

			// se non trovo il fascicolo restituisco il messaggio all'utente
			if (mFas == null) {
				Messa = "Il Procedimento " + lMess.getChiaveAnnoFasCumulante() + "/"
						+ lMess.getChiaveProgrFasCumulante() + " indicato da "
						+ lMess.getDescrUfficioMittente() + " di " + lMess.getDescrSedeUfficioMittente()
						+ " come competente all'esecuzione, non è presente nel sistema!";
			} else {
				IPenaResidua lCrtlP = SIEPLookupRemote.getPenaResiduaRemote();
				PenaResiduaModel mPena = lCrtlP
						.ExRicercaPenaResiduaCorrenteByFascicoloSiep(mFas.getFasSieIdFascicoloSiep());
				setRequestAttribute("penaresiduaCumulante", mPena);
			}

			// -------> Proviamo a fare presa in carico
			IstruttoriaCumuloModel IstruttoriaCumuloReq = null;
			if (lIstruttoriaCumuloModel != null && lIstruttoriaCumuloModel.getIdIstruttoriaCumulo() != null) {
				// l'Istruttoria è già stata trovata e messa in request (provengo da Atti Ricevuti in
				// Istruttoria)
			} else {
				// In qusto caso, provengo da Elenco Atti Ricevuti o dal CRUSCOTTO ELENCO ATTI
				// quindi mi devo cercare ISTRUTTORIA e passarla alla request
				if (mFas != null && mFas.getIdFascicoloSiep() != null) {
					IstruttoriaCumuloReq = lIstrCumCtrl
							.ExRicercaIstruttoriaCumuloApertaByIdFasSiep(mFas.getIdFascicoloSiep());
					setRequestAttribute("IstruttoriaCumulo", IstruttoriaCumuloReq);
				}
			}

			// Ripulisco la sessione da eventuali dati che potrebbero fuorviare
			setSessionAttribute("fascicolo", null);
			setSessionAttribute("soggetto", null);
			setSessionAttribute("sentenza", null);
			// metto in Sessione il Fascicolo Cumulante
			// Ticket#20200730015 - Se il cumulante on viene trovato a sistema andava in nullPointer
			// mFas.getSoggetto()
			if (mFas != null) {
				setSessionAttribute("fascicolo", mFas);
				setSessionAttribute("soggetto", mFas.getSoggetto());
				setSessionAttribute("sentenza", mFas.getSentenza());
			}

			setRequestAttribute("fascicoloCumulante", mFas);
		}

		FascicoloSiepModel lFasModel = new FascicoloSiepModel();
		CompetenzaModel lUltimaCompetenza = null;

		// ==========================================================================
		// TEST Campo BLOB
		// Se il fascicolo trasmesso è della stessa BDI, non è stato inserito nel
		// Messaggio (Campo blob) per contenere lo spazio. Recupero i dati direttamente
		// dalla BDI
		UfficioModel lUfficioFascicoloRicevuto = getUfficioByCodUfficio(lMess.getChiaveUfficioSiep());
		if (lUfficioFascicoloRicevuto.getCodDistretto().equals(getCodDistrettoUtenteConnesso())) {
			siesLogger.debug(
					"Fascicolo da Cumulare della stessa BDI, recupero i dati direttamete dalla Base DATI");

			IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasModel.setChiaveAnno(lMess.getChiaveAnnoSiep());
			lFasModel.setChiaveProgr(lMess.getChiaveProgrSiep());
			lFasModel.setChiaveUfficio(lMess.getChiaveUfficioSiep());

			lFasModel = lCtrlFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasModel);

			// Recupera anche il record Competenza
			// FIXME verificare bene cosa recupera il metodo
			ICompetenza lCtrlCompetenza = SIEPLookupRemote.getCompetenzaRemote();
			Vector<CompetenzaModel> lListaCompetenze = lCtrlCompetenza
					.ExRicercaCompetenzaByIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
			lUltimaCompetenza = lListaCompetenze.lastElement();
		} else {
			siesLogger.debug(
					" --XX-- Fascicolo da Cumulare proveniente da fuori Distretto, leggo il BLOB del MESSAGGIO");

			ParserMessage lParser = null;
			if (lMess != null) {
				lParser = new ParserMessage(lMess.getTreeModel());
			}

			if (lParser != null && lParser.getDettaglioFascicoloSiep() != null
					&& lParser.getDettaglioFascicoloSiep().getFascicoloSiep() != null)
				lFasModel = lParser.getDettaglioFascicoloSiep().getFascicoloSiep();
			else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Errore nella Ricezione del Procedimento. <BR>Rivolgersi all'amministratore di sistema! ");

			if (lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto() != null)
				lFasModel.setSoggetto(lParser.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto());
			else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Errore nella Ricezione del Soggetto. <BR>Rivolgersi all'amministratore di sistema!");

			// Provo a recuperare il record Competenza
			DatiSiepPerTrasferimentoModel lDSPT = lParser.getDettaglioFascicoloSiep()
					.getDatiSiepPerTrasferimento();
			Vector lListaCompetenze = (Vector) lDSPT.getListCompetenze();

			if (lListaCompetenze != null && !lListaCompetenze.isEmpty()) {
				lUltimaCompetenza = (CompetenzaModel) lListaCompetenze.lastElement();
				siesLogger.debug(" --XX-- Dal BLOB del MESSAGGIO UltimaCompetenza = " + lUltimaCompetenza);
			}
		}

		setRequestAttribute("Competenza", lUltimaCompetenza);
		setRequestAttribute("fascicoloSIEP", lFasModel);
		setRequestAttribute("NoProcedimento", Messa);

		return PG_DETTAGLIO_TRASMISSIONE_COMPETENZA_RICEVUTA;
	}

}