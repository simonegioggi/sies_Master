package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load Inserimento e Modifica della Richieste del PM al GE di revoca di pena principale
 * (depenalizzazione/Incostituzionalità)
 *
 * @author
 *
 */
public class ActLoadInsRichiestaGERevocaPenaPrinc extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		// Recupera dati legati all'ISTRUTTORIA_CUMULO
		IstruttoriaCumuloModel lIstrCumulo = super.getDatiIstruttoria();

		String lPage = "";
		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		RichiestePmInCumuloModel lRichiestaModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento : Precarico l'elenco dei titoli
			this.getElencoTitoliReato(lIstrCumulo);

			lPage = PG_INS_RICH_GE_REV_PENA_PRINC;
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);

			// Recupero i dati della Richiesta e li passo alla form
			IRichiestePmInCumulo lCtrlRich = SIEPLookupRemote.getRichiestePmInCumuloRemote();
			lRichiestaModel = lCtrlRich.ExRicercaRichiestePmInCumuloById(lId);
			setRequestAttribute("RichiestaAlGE", lRichiestaModel);

			// Ricerca dei Titoli collegati alla Richiesta (tramite tabella di Relazione RICHPM_TITOLO_CUM)
			Vector<RichPMTitoloCumModel> VecRicPM = new Vector<>();
			VecRicPM = lCtrlRich.ExRicercaRichPMTitoliCum(lId);

			// Ricerca completa dei dati Aggregati ai Titoli coinvolti nella Richiesta (Reati_Cum)
			Vector<TitoloCumulatoModel> VecTitoli = new Vector<>();
			VecTitoli = lCtrlRich.ExRicercaAltriDatiRichiestaGE(VecRicPM);

			setRequestAttribute("TitoliRichiesta", VecTitoli);

			lPage = PG_MOD_RICH_GE_REVOCA_PENA_PRINC;
		} else {
			// Rilanciare Eccezione - Operazione non supportata
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Modalità operazione sconosciuta. Impossibile eseguire la richiesta.");
		}

		// ================================
		// Recupero i dati delle combo
		// ================================
		Option lOptionF = new Option(DecodificheManager.getInstance().getTipoFonteReato());
		if (lRichiestaModel != null && lRichiestaModel.getIdRichiestePmInCumulo() != null) {
			if (lRichiestaModel.getCodFonte() != null) {
				lOptionF.setSelected(lRichiestaModel.getCodFonte());
			}
		}
		setRequestAttribute("TipiFontiReato", "" + lOptionF);

		Option lOptionS = new Option(DecodificheManager.getInstance().getSottonumerazione());
		if (lRichiestaModel != null && lRichiestaModel.getIdRichiestePmInCumulo() != null) {
			if (lRichiestaModel.getCodSottonumerazione() != null) {
				lOptionS.setSelected(lRichiestaModel.getCodSottonumerazione());
			}
		}
		setRequestAttribute("TipiSottonumerazione", "" + lOptionS);

		// Option lOptionA = new Option( DecodificheManager.getInstance().getTipoAnnotazioneManualeMC() );
		Option lOptionA = new Option(DecodificheManager.getInstance().getTipoAnnotazioneManualeTutte());
		lOptionA.setFilter(new String[] { "004", "013", "017", "-" });
		if (lRichiestaModel != null && lRichiestaModel.getIdRichiestePmInCumulo() != null) {
			if (lRichiestaModel.getCodTipoAnnotazione() != null) {
				lOptionA.setSelected(lRichiestaModel.getCodTipoAnnotazione());
			}
		}
		setRequestAttribute("TipoAnnotazione", "" + lOptionA);

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
		// Aggiungo : Reati_Cumulo
		// -------------------------------------------------------------------------------------
		IRichiestePmInCumulo lCtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		Vector<TitoloCumulatoModel> lVecTitoli = lCtrlRic.ExRicercaAggregatiAlTitolo(lVecTit);
		//
		siesLogger.debug("--XX-- Size Prima = " + lVecTitoli.size());

		// Lista degli ID_Titoli Selezionati dall'Utente
		String[] lIdTitoliSelezionati = null;

		if (!isRequestParameterNullObj(CAMPO_ID_TITOLO_SELEZIONATO))
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

		// ============================================================================
		// Confronto la Lista di TUTTI i Titoli legati all'Istruttoria Corrente (lVecTitoli),
		// con la lista degli ID_Titoli selezionati dall'Utente (lIdTitoliSelezionati);
		//
		// Dal confronto viene prodotta la Lista dei Titoli validi che andrà nella request
		// per la successiva Form (lVecTitoliValidi)
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

} // Chiude Classe()
