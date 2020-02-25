package siap.sius.provvedimento.action;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoDepositoModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActRicercaProvvedimentiDepositati
 * </p>
 * <p>
 * Description: Classe Action per la Ricerca dei Provvedimenti Depositati. L' Azione effettua una ricerca dei
 * provvedimenti per ID Fascicolo Sius che siano stati validati. Poi dall'elenco ottenuto vengono rimossi i
 * provvedimenti con data di deposito nulla. per un pProcedimento SIUS.
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaProvvedimentiDepositati extends ActionSius implements ICostantiProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "." + ".processRequest(): inizio");

		// jsp di output
		String lRetPage = PG_LISTA_PROVVEDIMENTI_POPUP;
		// Model restituito nella lista dei Provvedimenti
		EventoDepositoModel lEventoDep = null;
		// Lista dei provvedimenti Depositati e restituiti nell'output
		Vector lProvDepositati = new Vector();
		// Lista dei Provvedimenti validati risultato della Ricerca tramite Controller
		Vector lProvValidati = null;

		// Si valorizza EventoModel per filtrare la Ricerca di Provvedimenti
		// validati per Fascicolo SIUS
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento(COD_EVENTO_PROVVEDIMENTO);
		lEvento.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_TIPO_PROVVEDIMENTO));
		lEvento.setFlagDocumentoRegistrato("S");
		lEvento.setFasSiuIdFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
		// Ricerca
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		lProvValidati = mCtrl.ExRicercaProvvedimentiDeposito(lEvento);

		// L'elenco dei Provvedimenti Validati viene analizzato per
		// restituire solo quelli Depositati e non già Revocati
		Iterator lItx = lProvValidati.iterator();
		while (lItx.hasNext()) {
			lEventoDep = (EventoDepositoModel) lItx.next();
			if (lEventoDep.getDataDeposito() != null && lEventoDep.getEveIdEventoRevoca() == null)
				lProvDepositati.add(lEventoDep);
		}
		setRequestAttribute("provvedimenti", lProvDepositati);
		// Passaggio del nome della form chiamante
		setRequestAttribute("formname", getRequestStringParameter("formname"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "." + ".processRequest(): fine");
		return lRetPage;
	}

}