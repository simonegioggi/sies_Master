package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la loadInserisci della Richieste del PM alla Sorveglianza di Revoca L.A.
 *
 * @author Intersistemi Italia S.p.A.
 *
 */

public class ActLoadInsRichiestaSORVRevocaLA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();

		String lPage = "";
		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("--XX-- Inizio - Modalita = " + lModalita);

		RichiestePmInCumuloModel lRichiestaModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento
			// Precarico l'elenco dei titoli
			this.getElencoTitoliConLibAnt(lIstrCumulo);
			lPage = PG_INS_RICH_SORV_REV_LA;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			// RichiestaAllaSORV
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			lRichiestaModel = lCtrlRich.ExRicercaRichiestePmInCumuloById(lId);
			setRequestAttribute("RichiestaAllaSORV", lRichiestaModel);

			// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
			Vector<RichPMTitoloCumModel> VecRicPM = new Vector<>();
			VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(lId);

			// Ricerca dei dati di Liberazione Anticipata aggregati ai Titoli coinvolti nella Richiesta.
			Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
			VecTitoli = lCtrlRich.ExRicercaTitoliDiLibAntPerRichiesta(VecRicPM);

			siesLogger.debug("--XX-- i Titoli collegati alla Richiesta sono " + VecTitoli.size());
			setRequestAttribute("VectorTitoli", VecTitoli);

			lPage = PG_MOD_RICH_SORV_REV_LA;

		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		setRequestAttribute("modalita", lModalita);

		return lPage;
	}

	/**
	 * Recupera l'elenco dei Titoli con relativi provvedimenti di L.A. da caricare nella form in hidden
	 * 
	 * @param aIstruttoriaModel
	 * @throws F3BException
	 */
	private void getElencoTitoliConLibAnt(IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException {
		String lOrdinamento = aIstruttoriaModel.getOrdinamentoTitoli();
		// ------------------------------------------------------------------------------------------------------
		// La Query trova Titolo_Cumulato_Model e i suoi aggregati: Procedimento_Cumulato e Soggetto_Cumulato
		// ------------------------------------------------------------------------------------------------------
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector<TitoloCumulatoModel> lVecTit = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(
				aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento);

		/// --------------------------------------------------------------------------------------
		// Con una seconda query si aggiunge Stato_Esec_Titolo_Cumulato
		// -------------------------------------------------------------------------------------
		IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		Vector<TitoloCumulatoModel> lVecTitoli = lCtrlRic.ExCaricaLibAntDelTitolo(lVecTit);

		siesLogger.debug("--XX-- Size Prima = " + lVecTitoli.size());

		// Lista degli ID_Titoli Selezionati dall'Utente
		String[] lIdTitoliSelezionati = null;

		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_SELEZIONATO))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

		// ==================================================================================
		// Confronto la Lista di TUTTI i Titoli legati all'Istruttoria Corrente (lVecTitoli),
		// con la lista degli ID_Titoli selezionati dall'Utente (lIdTitoliSelezionati);
		//
		// Dal confronto viene prodotta la Lista dei Titoli validi che andrà nella
		// successiva Form (lVecTitoliValidi)
		// ==================================================================================
		Vector<TitoloCumulatoModel> lVecTitoliValidi = new Vector<>();

		for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
			for (int j = 0; j < lVecTitoli.size(); j++) {
				TitoloCumulatoModel Titolo = lVecTitoli.elementAt(j);
				if (Titolo.getIdTitoloCumulato().toString().equals(lIdTitoliSelezionati[i])) {
					// siesLogger.debug("--XX-- Append Titolo = "+Titolo.getIdTitoloCumulato());
					lVecTitoliValidi.add(Titolo);
				}
			}
		}

		siesLogger.debug("--XX-- Size DOPO = " + lVecTitoliValidi.size());

		setRequestAttribute("VectorTitoli", lVecTitoliValidi);

	} // Chiude getElencoTitoliConLibAnt()

} // Chiude Classe
