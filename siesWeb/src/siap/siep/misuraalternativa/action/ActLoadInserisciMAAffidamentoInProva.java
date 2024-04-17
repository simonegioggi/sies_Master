package siap.siep.misuraalternativa.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * ActLoadInserisciMAAffidamentoInProva - Classe Action per la load inserisci di Concessione Affidamento
 *
 * @version 1.0
 */
public class ActLoadInserisciMAAffidamentoInProva extends ActConcessione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		// passo la posizione attuale per vedere se esiste il verbale o no
		String lRitorno = getConcessione("13");
		if (!lRitorno.equals(""))
			return lRitorno;

		// setto il campo codice motivo
		Option lOption = null;
		// MEV_9-SIEP: si differenzia per PM e PMM
		if (isUfficioMinorenni())
			lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoMAffPMinor());
		else
			lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoMAffP());
		// FINE MEV_9-SIEP
		setRequestAttribute("motivoProvv", "" + lOption);

		setRequestAttribute("tipoMisura", "AFFIDAMENTO");

		// Instanzia il model dell'evento
		EventoModel lEvent = new EventoModel();

		String lCodPosizione = (String) getRequestAttribute("lcodicePosizione");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lCodPosizione = " + lCodPosizione);

		// ==========================================================================
		// Ricerca se presente concessione provvisoria della misura.
		// Cerco sia Affidamento Provvisorio che Detenzione Provvisoria infatti va
		// gestito anche il passaggio da Detenzione Domiciliare Provvisoria ad Affidamento In Prova
		// (definitivo)
		// since marzo 2015 MEV29. Prima cercava solo l'affidamento provvisorio
		// ==========================================================================

		// MEV_9-SIEP: aggiunta nuova gestione per modifica
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = null;
		String tipoOperazione = "";
		if (!isRequestParameterNullObj("tipoOperazione"))
			tipoOperazione = getRequestStringParameter("tipoOperazione");
		if ("MODIFICA".equals(tipoOperazione)) {
			lEve = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		} else {
			// prende dalla Session l'ID del procedimento e lo carica nel model
			lEvent.setFasSieIdFascicoloSiep(
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			// carica nel model il Tipo evento
			lEvent.setCodTipoEvento("01");
			// carica nel model il flag documento registrato
			lEvent.setFlagDocumentoRegistrato("S");
			// carica nel model il codice dei provvedimenti
			String[] lProvv = { "04", "09", "12" };
			// carica nel model il tipo motivo
			String[] lMotivo = null;
			if ("29".equals(lCodPosizione)) {// Pos Giu 29 Detenzione Domiciliare Provvisoria
				// carica nel model il tipo motivo
				lMotivo = new String[] { "2005" };
			} else {
				// carica nel model il tipo motivo
				lMotivo = new String[] { "2006", "2008" };
			}
			// Ricerca eventuale Provvedimeto di Concessione Misura Provvisoria (affidamento/Detenzione)
			lEve = ie.ExRicercaEventoPerMotivoPerProvv(lMotivo, lProvv, lEvent);
		}

		// setta la risposta della ricerca nella request
		setRequestAttribute("eventoammissioneprovvisoriaaffidamento", lEve);

		// solo se provengo da annotazione affidamento in prova delle sanzioni sostitutive
		String lFlagSanzione = "N";
		if (!isRequestParameterNullObj("lFlagSanzione")
				&& "S".equals(getRequestStringParameter("lFlagSanzione"))) {
			lFlagSanzione = "S";
		}
		setRequestAttribute("lFlagSanzione", lFlagSanzione);

		if (lEve != null) {
			// Ricerca nella tabella MISURA ALTERNATIVA
			IMisuraAlternativa ima = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel mam = ima.ExRicercaMisuraAlternativaByIdEvento(lEve.getEveIdEvento());

			// setta la risposta della ricerca nella request
			setRequestAttribute("maammissioneprovvisoria", mam);

			// MEV_9-SIEP: aggiunta impostazione per il campo codice motivo
			if (!Utils.isNullObj(mam)) {
				lOption.setSelected(mam.getCodTipoMisura());
				setRequestAttribute("motivoProvv", "" + lOption);
			}
		}

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", getFiltroMinorenni());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_INSERISCI_MA_CONCESSIONE;
	}

}