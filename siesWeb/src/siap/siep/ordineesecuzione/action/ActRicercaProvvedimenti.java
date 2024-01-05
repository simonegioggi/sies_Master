package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Description: Classe Action per la ricerca dei Provvedimenti associati al fascicolo corrente
 *
 * @version 1.0
 */
public class ActRicercaProvvedimenti extends ActionSiap implements ICostantiOrdineEsecuzione {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = null;

		// ==========================================================================
		// Verifico che il fascicolo sia in sessione in quanto questa funzione
		// può essere richiamata anche dal menù di scelta rapida (29/05/2006)
		// ==========================================================================
		if (!this.isSessionAttributeNullObj("fascicolo")) {
			lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		} else {
			// Se non ho l'id fascicolo ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Ordinamento di default
		String lOrdinamento = "EM";

		// Lettura ordine data_invio
		if (!isRequestParameterNullObj("lOrdinamento")) {
			lOrdinamento = getRequestStringParameter("lOrdinamento");
		} else if (!isRequestAttributeNullObj("lOrdinamento")) {
			lOrdinamento = (String) getRequestAttribute("lOrdinamento");
		}
		this.setRequestAttribute("ordinamento", lOrdinamento);

		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		// Nell'Elenco Provvedimenti PM anche i Verbali. Luigi 3-2-06
		// Nell'Elenco Provvedimenti PM anche le richieste. Dario 14-3-06
		// Nell'Elenco Provvedimenti PM anche le Pene accessorie. Vincenzo 29-3-06
		String[] lTipoEvento = { "01", "07", "02", "16", "17", "18" };
		// Modifica del 12/04/2016
		// Dall'elenco vengono scartate le Ordinanze (cod 03),
		// i Decreti (cod 02), e le Sentenze (cod 01).
		// Ticket#202101270113 - aggiunto il codice 50 tra i TipoProvv da escludere tra gli eventi del PM.
		// E' un codice prettamente SIUS
		String[] lTipoProv = { "03", "02", "01", "50" };
		// FINE - Ticket#202101270113
		// 26/03/2019 MEV70 - Esclusione degli Eventi con CodMotivo = "0670".
		String[] lCodMotivo = { "0670", "esclude" };
		Vector lVect = lCtrl.ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(
				lFascicoloModel.getIdFascicoloSiep(),
				// getCodUfficioUtenteConnesso(),
				getUfficioUtenteConnesso(), lTipoEvento, lTipoProv, lCodMotivo, Integer.parseInt(lPagina),
				lOrdinamento);
		setRequestAttribute("eventi", lVect);

		EventoModel lEveMod = lCtrl
				.ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
						lFascicoloModel.getIdFascicoloSiep(), lTipoEvento, lTipoProv, lCodMotivo,
						lOrdinamento);
		if (lEveMod == null || lEveMod.getIdEvento() == null) {
			lEveMod = new EventoModel();
		}
		siesLogger.debug("eventocancellareannullare = " + lEveMod);
		setRequestAttribute("eventocancellareannullare", lEveMod);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			// 26/03/2019 MEV70 - Esclusione degli Eventi con CodMotivo = "0670".
			// Ticket#202101270113 - si adeguano le condizione della count alle condizioni della select
			// impostando il filtro sull'ufficio + accorpati
			CountRisultati = lCtrl.ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(
					lFascicoloModel.getIdFascicoloSiep()
					// , getCodUfficioUtenteConnesso()
					, getUfficioUtenteConnesso(), lTipoEvento, lTipoProv, lCodMotivo);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		// ==========================================================================
		// Nel caso di evento che punta una istruttoria di cumulo, recupero il
		// record DATI_FINALI_CUMULO per verificare se emesso dal GE, in questo
		// caso forzo la visualizzazione della colonna autorità in modo che esca
		// Giudice Esecuzione - [tipo ufficio] [LUOGO]
		// ==========================================================================
		// MEV26 - CUMULO
		for (int i = 0; i < lVect.size(); i++) {
			EventoModel lEventoMod = (EventoModel) lVect.elementAt(i);
			if (lEventoMod.getIstruIdIstruttoriaCumulo() != null) {
				IDatiFinaliCumulo lDatiFinCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
				DatiFinaliCumuloModel lDatifinali = lDatiFinCtrl
						.ExRicercaDatiFinaliCumuloByIdIstrutt(lEventoMod.getIstruIdIstruttoriaCumulo());
				if (lDatifinali != null && "03".equals(lDatifinali.getTipoUfficioEmissione())) {
					lEventoMod.setDescrUfficioEmittente(
							"Giudice Esecuzione - " + lDatifinali.getDescrTipoUfficioEmittente());
					lEventoMod.setDescrLuogoEmittente(lDatifinali.getDescrLuogoUfficioEmittente());
				}
			}
		}
		// END MEV26 - CUMULO
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		// ANNA per Pene Sospese
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		// lModel.setCodiceAlternativo("REVOCA");
		// 2024.01.05 - Inverce di ricaricare tutta la tabella (1500 codici) ci si limita a quelli dei 2
		// domini REVOCA e ESTINZIONE_REATO i soli di interesse per la jsp
		// Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		// setRequestAttribute("AllMotivi", lColMotivo);
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivoREVOCA = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("ESTINZIONE_REATO");
		Collection lColMotivoESTINZIONE = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		Collection lColMotivo = new Vector();
		lColMotivo.addAll(lColMotivoREVOCA);
		lColMotivo.addAll(lColMotivoESTINZIONE);
		setRequestAttribute("AllMotivi", lColMotivo);
		// 2024.01.05 - FINE

		// MEV 15 - Revisione SIGE
		// Aggiunto parametro per identificare la funzione che richiama la maschera
		// di Elenco Provvedimenti PM da Iscrizione Manuale.
		// Quando viene richiamata da SIGE sulla maschera viene inserito
		// il pulsante Indietro
		String codFunzione = getCodFunMenuVerticale();
		setRequestAttribute("codFunzione", codFunzione);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		this.setLinkRitorno();
		return PG_RICERCA_PROVVEDIMENTI;
	}

}