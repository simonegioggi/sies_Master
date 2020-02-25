package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action preposta al caricamento o cancellazione dei provvedimenti dello stato di esecuzione.
 *
 *
 * @author d.fiorletta
 *
 */
public class ActAggiornaStatoEsecuzioneTitolo extends ActionModuloCumulo
		implements ICostantiStatoEsecuzioneCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstruttoriaModel = */super.getDatiIstruttoria();
		TitoloCumulatoModel lTitoloModel = super.getDatiTitoloCumulato();

		// ==========================================================================
		// - Recupero gli idEvento selezionati nella form
		// - Recupero lo stato esecuzione del titolo (S.E.T.)

		// - Scorro idEvento selezionati:
		// se presenti in S.E.T. li elimino da entrambe le liste
		// Al termine ho:
		// - la lista idEvento che contiene solo quelli da inserire
		// - la lista S.E.T. che contiene solo quelli da eliminare
		// Passo le 2 liste al ctrl per aggiornate il S.E.T.
		// Carico lista S.E.T. con flagOperazione C = Cancella (default)
		// Scorro idEvento selezionati:
		// Se già presente in S.E.T. aggiorno flagOperazione N = Nulla da fare.
		// Se assente in S.E.T. lo lascio nella lista di quelli da inserire
		// ==========================================================================

		// Recupero la lista degli idEvento selezionati

		String[] lListaIdEventiSelezionati = new String[0];
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			lListaIdEventiSelezionati = getRequestStringParameters(ICostantiEvento.CAMPO_ID_EVENTO);
		}
		siesLogger.debug("Eventi selezionati = " + lListaIdEventiSelezionati.length);

		// Recupero lo stato esecuzione attualmente a sistema
		IStatoEsecTitoloCumulato lCtrlSET = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		Vector<StatoEsecTitoloCumulatoModel> lListaEventiSET = lCtrlSET
				.ExRicercaStatoEsecTitoloCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());
		siesLogger.debug("Eventi SET prima = " + lListaEventiSET.size());

		// Elimino dalla lista quelli inseriti a mano (ID_EVENTO_ORIGINE = null)
		for (int i = 0; i < lListaEventiSET.size(); i++) {
			StatoEsecTitoloCumulatoModel lEveSET = lListaEventiSET.elementAt(i);
			if (lEveSET.getIdEventoOrigine() == null) {
				siesLogger.debug("eventSET inserito a mano, lo escludo dalla lista");
				lListaEventiSET.remove(i);
				i--;
			}
		}
		siesLogger.debug("Eventi SET prima (no manuali) = " + lListaEventiSET.size());

		// ==========================================================================
		// Carico la lista degli eventi da inserire in lIdEventiDaInserire e
		Vector<BigDecimal> lIdEventiDaInserire = new Vector<>();
		for (int i = 0; i < lListaIdEventiSelezionati.length; i++) {
			BigDecimal lIdEvento = new BigDecimal(lListaIdEventiSelezionati[i]);

			boolean lGiaPresente = false;
			for (int j = 0; j < lListaEventiSET.size(); j++) {
				StatoEsecTitoloCumulatoModel lEveSET = lListaEventiSET.elementAt(j);
				if (lIdEvento.compareTo(lEveSET.getIdEventoOrigine()) == 0) {
					siesLogger.debug("id " + lIdEvento + " già presente lo escludo");
					lGiaPresente = true;
					lListaEventiSET.remove(j);
					break;
				}
			}

			if (!lGiaPresente) {
				siesLogger.debug("id " + lIdEvento + " non presente lo aggiungo");
				lIdEventiDaInserire.add(lIdEvento);
			}
		}

		siesLogger.debug("Eventi SET da eliminare = " + lListaEventiSET.size());

		// ==========================================================================
		// Effettuo l'aggiornamento
		// ==========================================================================
		DatiOperazioneModel lDatiOper = new DatiOperazioneModel();

		lDatiOper.setCodOperatore(getCodUtenteConnesso());
		lDatiOper.setData(DateUtils.getSysDate());
		lDatiOper.setCodUfficio(getCodUfficioUtenteConnesso());

		lCtrlSET.ExAggiornaStatoEsecTitoloCumulatoByIdTitolo(lIdEventiDaInserire, lListaEventiSET,
				lTitoloModel.getIdTitoloCumulato(), lTitoloModel.getIstrIdIstruttoriaCumulo(), null,
				lDatiOper, false);

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Stato Esecuzione Effettuato");

		return PG_POPUP_RET_STATO_ESEC_FASCICOLO;
	}

}