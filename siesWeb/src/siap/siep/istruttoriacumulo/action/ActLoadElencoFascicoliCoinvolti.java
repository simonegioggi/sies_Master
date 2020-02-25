package siap.siep.istruttoriacumulo.action;

/**
* <p>Title: ActLoadElencoFascicoliCoinvolti</p>
* <p>Description: Classe Action per la load dei fascicoli coinvolti in Istruttoria
* Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadElencoFascicoliCoinvolti extends ActionModuloCumulo
		implements ICostantiIstruttoriaCumulo {
	/*****************************************************************************
	 * Azione di caricamento della pagina di con l'elenco dei Titoli Coinvolti in cumulo.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter(CAMPO_ID_ISTRUTTORIA_CUMULO);

		// ==========================================================================
		// Recupero l'istruttoria da passare alla form
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();

		// ==========================================================================
		// Ricerco i record TITOLO_CUMULATO legati all'istruttoria recuperando anche i dati
		// di PROCEDIMENTO_CUMULATO e SOGGETTO_CUMULATO
		// ==========================================================================

		String lOrdinamento = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;

		if (!isRequestParameterNullObj("Ordinamento")) {
			// Selezionato dalla lista nuovo ordinamento
			lOrdinamento = getRequestStringParameter("Ordinamento");
			siesLogger.debug("ParOrdinamento = " + lOrdinamento);

			lIstruttoriaModel.setOrdinamentoTitoli(lOrdinamento);

			// Aggiorno l'ordinamento
			IIstruttoriaCumulo lCtrlIstruttoria = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			lCtrlIstruttoria.ExUpdateOrdinamentoTitoli(lIstruttoriaModel);
		} else if (lIstruttoriaModel.getOrdinamentoTitoli() != null
				&& !lIstruttoriaModel.getOrdinamentoTitoli().equals("")) {
			// Nessun ordinamento selezionato, verificaìo se presente sul record ISTRUTTORIA_CUMULO
			siesLogger.debug("Nessun ordinamento selezionato");

			lOrdinamento = lIstruttoriaModel.getOrdinamentoTitoli();
		}
		setRequestAttribute("Ordinamento", lOrdinamento);

		// Ricerco i titoli
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(lIdIstruttoriaCumulo,
				lOrdinamento);
		setRequestAttribute("ListaTitoli", lListaTitoli);

		// Verifico se presente provvedimento di Cumulo sull'istruttoria (Dati Finali)
		IDatiFinaliCumulo lCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
		DatiFinaliCumuloModel lDatiFinali = lCtrl.ExRicercaDatiFinaliCumuloByIdIstrutt(lIdIstruttoriaCumulo);

		if (lDatiFinali != null && lDatiFinali.getIdDatiFinaliCumulo() != null
				&& lDatiFinali.getEveIdEvento() != null) {
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEveProvvCum = lCtrlEvento.ExRicercaEventoByKey(lDatiFinali.getEveIdEvento());
			setRequestAttribute("ProvvedimentoCumulo", lEveProvvCum);
		}

		// =================================================================================
		// Controllo i Dati in Sessione:
		// Se Arrivo dalla Ricerca Istruttoria, i Dati in Sessione potrebbero NON esserci
		// o potrebbero essere di altri oggetti
		// ================================================================================
		Boolean RicercaFas = false;
		FascicoloSiepModel lFascicolo = null;

		if (!this.isSessionAttributeNullObj("fascicolo") && getSessionAttribute("fascicolo") != null) {
			lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			if (!lFascicolo.getIdFascicoloSiep().equals(lIstruttoriaModel.getFasSieIdFascicoloSiep())) {
				RicercaFas = true;
			}
		} else {
			RicercaFas = true;
		}

		if (RicercaFas) {
			IFascicoloSiep lCtrlF = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFascDaInserireInSessio = null;

			lFascDaInserireInSessio = (FascicoloSiepModel) lCtrlF
					.ExRicercaFascicoloByKey(lIstruttoriaModel.getFasSieIdFascicoloSiep());
			if (lFascDaInserireInSessio != null && lFascDaInserireInSessio.getIdFascicoloSiep() != null) {
				setSessionAttribute("fascicolo", null);
				setSessionAttribute("soggetto", null);
				setSessionAttribute("sentenza", null);

				setSessionAttribute("cumulowiz", null);
				setSessionAttribute("penaresidua", null);
				setSessionAttribute("reato", null);

				setSessionAttribute("fascicolo", lFascDaInserireInSessio);
				setSessionAttribute("soggetto", lFascDaInserireInSessio.getSoggetto());
				setSessionAttribute("sentenza", lFascDaInserireInSessio.getSentenza());
			}
		}

		// ===================================================================================
		// Per la gestione del Bottone <Torna Indietro> su Elenco Istruttorie
		// (Solo se provengo da <Ricerca Istruttoria Estesa>
		// ===================================================================================
		if (!this.isRequestParameterNullObj("ParentFormName") && this
				.getRequestStringParameter("ParentFormName").trim().equals("ElencoEstesoIstruttorieCumulo")) {
			siesLogger.debug("Provengo dalla ricerca istruttorie");

			// per gestire il 'tornaIndietro' all'Elenco Istruttorie Estese
			setRequestAttribute("ParentFormName", getRequestStringParameter("ParentFormName").trim());
		}

		return PG_LOAD_ELENCO_FASCICOLI_COINVOLTI;
	}
}