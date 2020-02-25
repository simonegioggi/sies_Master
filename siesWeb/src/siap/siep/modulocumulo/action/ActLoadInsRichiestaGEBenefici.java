package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load inserisci/Modifica della Richieste al GE di Applicazione Beneficio dell'Indulto/Amnistia
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActLoadInsRichiestaGEBenefici extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
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
			this.getElencoTitoliReato(lIstrCumulo);
			lPage = PG_INS_RICH_GE_BENEFICI;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			// RichiestaAlGE
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			lRichiestaModel = lCtrlRich.ExRicercaRichiestePmInCumuloById(lId);
			setRequestAttribute("RichiestaAlGE", lRichiestaModel);

			// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
			Vector<RichPMTitoloCumModel> VecRicPM = new Vector<>();
			VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(lId);

			// Ricerca completa dei dati Aggregati ai Titoli coinvolti nella Richiesta (Reati_Cum,
			// Mis_Sic_Cum, Pena_Acc_Cum)
			Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
			VecTitoli = lCtrlRich.ExRicercaAltriDatiRichiestaGE(VecRicPM);

			siesLogger.debug("--XX-- i Titoli collegati alla Richiesta sono " + VecTitoli.size());
			setRequestAttribute("TitoliRichiesta", VecTitoli);

			lPage = PG_MOD_RICH_GE_BENEFICI;

		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ================================
		// Recupero i dati delle combo
		// ================================
		Option lOptionTipoAnn = new Option(
				DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici(), "002");
		if (lRichiestaModel != null)
			lOptionTipoAnn.setSelected(lRichiestaModel.getCodTipoAnnotazione());
		setRequestAttribute("TipoAnnotazioneManuale", "" + lOptionTipoAnn);

		// Se Inserimento posizionamento combo DPR all'ultimo elemento.
		// Se Modifica posizionamento su Elemento Trovato
		Vector lVect = (Vector) DecodificheManager.getInstance().getDPR();
		DecodificheModel lDecMod = (DecodificheModel) lVect.lastElement();

		Option lOptionDPR = new Option(lVect);
		if (lRichiestaModel != null)
			lOptionDPR.setSelected(lRichiestaModel.getCodDpr());
		else
			lOptionDPR.setSelected(lDecMod.getCode());
		setRequestAttribute("listaDPR", "" + lOptionDPR);

		setRequestAttribute("modalita", lModalita);

		return lPage;
	}

	/**
	 * Recupera l'elenco dei Titoli con relativi reati da caricare nella form in hidden
	 * 
	 * @param aIstruttoriaModel
	 * @throws F3BException
	 */
	private void getElencoTitoliReato(IstruttoriaCumuloModel aIstruttoriaModel) throws F3BException {

		// ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		Vector<TitoloCumulatoModel> lVecTit = null;

		String lOrdinamento = aIstruttoriaModel.getOrdinamentoTitoli();
		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

		if (getRequestStringParameter(CAMPO_CHECK_SCELTA_TITOLI).equals("R")) {
			Date lDataRea = getRequestDateParameter(CAMPO_ANNO_DATA_COMMESSO_REATO,
					CAMPO_MESE_DATA_COMMESSO_REATO, CAMPO_GIORNO_DATA_COMMESSO_REATO);
			// La Query trova solo i Titolo_Cumulato_Model (che hanno i Reati con DataReato come Richiesto), e
			// i suoi aggregati: Procedimento_Cumulato e Soggetto_Cumulato
			lVecTit = new Vector<>(
					lIstrCtrl.ExRicercaTitoliByIstruttoriaDataReatoCumOrderBy(
							aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento, lDataRea));
		} else {
			// La Query trova Titolo_Cumulato_Model e i suoi aggregati: Procedimento_Cumulato e
			// Soggetto_Cumulato
			lVecTit = new Vector<>(lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(
					aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento));
		}

		/// --------------------------------------------------------------------------------------
		// Con una seconda query (per non appesantire la precedente query)
		// Aggiungo : Reati_Cumulo, Pene_Accessorie_Cumulo e Misure_Sicutrzza_Cumulo
		// -------------------------------------------------------------------------------------
		IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		Vector<TitoloCumulatoModel> lVecTitoli = lCtrlRic.ExRicercaAggregatiAlTitolo(lVecTit);
		//
		// Lista degli ID_Titoli Selezionati dall'Utente
		String[] lIdTitoliSelezionati = null;

		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_SELEZIONATO))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

		// ============================================================================
		// Confronto la Lista di TUTTI i Titoli legati all'Istruttoria Corrente (lVecTitoli),
		// con la lista degli ID_Titoli selezionati dall'Utente (lIdTitoliSelezionati);
		//
		// Dal confronto viene prodotta la Lista dei Titoli validi che andrà nella
		// successiva Form (lVecTitoliValidi)
		// ============================================================================
		Vector<TitoloCumulatoModel> lVecTitoliValidi = new Vector<>();

		if (lIdTitoliSelezionati != null && lIdTitoliSelezionati.length > 0) {
			for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
				for (int j = 0; j < lVecTitoli.size(); j++) {
					TitoloCumulatoModel Titolo = lVecTitoli.elementAt(j);
					if (Titolo.getIdTitoloCumulato().toString().equals(lIdTitoliSelezionati[i])) {
						// siesLogger.debug("--XX-- Append Titolo = "+Titolo.getIdTitoloCumulato());
						lVecTitoliValidi.add(Titolo);
					}
				}
			}
		} else {
			lVecTitoliValidi = lVecTitoli;
		}

		siesLogger.debug("--XX-- Size DOPO = " + lVecTitoliValidi.size());

		setRequestAttribute("VectorTitoli", lVecTitoliValidi);
	} // Chiude getElencoTitoliReato()

} // Chiude Classe