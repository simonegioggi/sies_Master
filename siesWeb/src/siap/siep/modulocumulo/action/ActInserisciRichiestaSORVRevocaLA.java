package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta alla Sorvegnlianza di Revoca LA
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaSORVRevocaLA extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaSORVRevocaLA....");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("lModalita = " + lModalita);

		RichiestePmInCumuloModel lRichModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("Sono in INSERT");
			lRichModel = this.getDatiForm();

			lRichModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRichModel.setDataInserimento(DateUtils.getSysDate());
			lRichModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			siesLogger.debug("INSERT - ho riempito il model lRichModel = " + lRichModel);
			// insert
		} else if ("M".equals(lModalita)) {
			// Modifica
			siesLogger.debug("Sono in MODIFICA");

			lRichModel = this.getDatiForm();

			lRichModel
					.setIdRichiestePmInCumulo(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			lRichModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRichModel.setDataAggiornamento(DateUtils.getSysDate());
			lRichModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			// siesLogger.debug("MODIFICA lRichModel = "+lRichModel);
		}

		siesLogger.debug(
				"--XX-- >>>> Inizio getRequest delle String con gli ID delle entita oggetto della richiesta ");
		// -----------------------------------------------------------------------------------
		// Stringhe[] con gli 'Id' delle Entità selezionate che riguardano la Richiesta
		// -----------------------------------------------------------------------------------
		// TITOLO_CUMULATO_MODEL:
		String[] lIdTitoliSelezionati = null; // ID dei titoli della prima form
		String[] lIdTitoliCompleti = null; // ID titoli selezionati check

		// STATO_ESEC_TITOLO_CUMULATO_MODEL
		String[] lIdStatiSelezionati = null; // ID provvedimenti selezionati (check)

		if ("I".equals(lModalita)) {
			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

			if (!isRequestParameterNullObj("idTitoloCompleto"))
				lIdTitoliCompleti = getRequestStringParameters("idTitoloCompleto");

			if (!isRequestParameterNullObj("idStatoEsecSelezionato")) {
				lIdStatiSelezionati = getRequestStringParameters("idStatoEsecSelezionato");

				siesLogger.debug("--XX-- >>>> lIdStatiSelezionati.length = " + lIdStatiSelezionati.length);
				siesLogger.debug("--XX-- >>>> lIdStatiSelezionati.element[0] = " + lIdStatiSelezionati[0]);
			}
		}

		// ====================================================================================
		// La parte seguente di Codice serve per implementare un Vector con tutti i dati
		// aggregati dei Titoli selezionati dall'Utente che riguardano la Richiesta;
		// ====================================================================================
		siesLogger.debug("--XX-- >>>> Inizio Gestione campi della form per Tabelle Relazione ");
		Vector<TitoloCumulatoModel> lListaTitoli = new Vector<TitoloCumulatoModel>();

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();

		if ("I".equals(lModalita)) {
			// Se selezionato il Titolo, vanno considerati selezionati tutti i provvedimenti
			for (int cct = 0; cct < lIdTitoliSelezionati.length; cct++) {
				TitoloCumulatoModel lTitoMod = null;
				lTitoMod = (TitoloCumulatoModel) lCtrlT
						.ExRicercaTitoloCumulatoById(new BigDecimal(lIdTitoliSelezionati[cct]));

				Boolean lcompl = false;
				// cerco se è tra i titoli con check
				if (lIdTitoliCompleti != null && lIdTitoliCompleti.length > 0) {
					for (int kkt = 0; kkt < lIdTitoliCompleti.length; kkt++) {
						if (lIdTitoliCompleti[kkt].compareTo(lIdTitoliSelezionati[cct]) == 0) {
							lcompl = true;
						}
					}
				}

				// Lettura StatiEsecuzione con L.A. per marcare quelli selezionati
				Vector<String> aVectCodiciLA = new Vector<String>();
				aVectCodiciLA.addAll(StatoEsecuzioneCumuloUtils.aCodLibAnticipata);
				Vector<String> aVectProvvLA = new Vector<String>(Arrays.asList("02", "03"));

				Vector<StatoEsecTitoloCumulatoModel> lVectStati = new Vector<StatoEsecTitoloCumulatoModel>();
				IStatoEsecTitoloCumulato lCtrlSETC = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
				lVectStati = lCtrlSETC.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
						new BigDecimal(lIdTitoliSelezionati[cct]), "01", aVectProvvLA, aVectCodiciLA);

				Vector<StatoEsecTitoloCumulatoModel> lVecStatiSel = new Vector<StatoEsecTitoloCumulatoModel>();

				if (lVectStati != null && lVectStati.size() > 0) {
					if (!lcompl) {
						Iterator itS = lVectStati.iterator();
						while (itS.hasNext()) {
							StatoEsecTitoloCumulatoModel lStato = (StatoEsecTitoloCumulatoModel) itS.next();
							if (lIdStatiSelezionati != null && lIdStatiSelezionati.length > 0) {
								for (int ccse = 0; ccse < lIdStatiSelezionati.length; ccse++) {
									if (lStato.getIdStatoEsecTitoloCumulato()
											.equals(new BigDecimal(lIdStatiSelezionati[ccse]))) {
										lVecStatiSel.add(lStato);
										// siesLogger.debug("--XX-- >>>> lVecStatiSel.size() =
										// "+lVecStatiSel.size());
									}
								}
							}
						}

						if (lVecStatiSel != null && lVecStatiSel.size() > 0)
							lTitoMod.setStatoEsecuzioneTitoloCumulato(lVecStatiSel);
					} else {
						lTitoMod.setStatoEsecuzioneTitoloCumulato(lVectStati);
					}
				}

				// Solo per i Titoli con selezione si alimenta la lista
				if (lcompl || lVecStatiSel.size() > 0)
					lListaTitoli.add(lTitoMod);

			} // Chiude ciclo for

		} // Chiude if(modalita="I")

		siesLogger.debug("--XX-- >>>> Fine Gestione campi form per Tabelle Relazione ");

		// ====================================================================================================================
		// Inserimento RICHIESTA e di RICHPM_STATO_ESEC_CUM (Relazione tra RICHIESTE_PM_IN_CUMULO e
		// STATO_ESEC_TITOLO_CUMULATO
		// ====================================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_SORV_RevocaLA(lRichModel, lListaTitoli);
			siesLogger.debug("--XX-- >>>> Sono tornato da ExInserisciRichiestePmInCumulo_SORV_RevocaLA ");
		} else if ("M".equals(lModalita)) {
			ICtrlRic.ExModificaRichiestePmInCumulo(lRichModel);
			siesLogger.debug("--XX-- >>>> Sono tornato da Inserimento in RichiestePmInCumuloController ");
			lRicRetMod.setIdRichiestePmInCumulo(lRichModel.getIdRichiestePmInCumulo());
		} else if ("C".equals(lModalita)) {
			// Cancellazione
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			// delete
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(lId);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		siesLogger.debug("--XX-- >>>> IdRichiestePmInCumulo = " + lRicRetMod.getIdRichiestePmInCumulo());
		if (!"C".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaSORVRevocaLA";
			lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "="
					+ lRicRetMod.getIdRichiestePmInCumulo().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
		}

		return lPage;
	}

	/**
	 * Metodo che recupera i dati dalla form
	 */
	private RichiestePmInCumuloModel getDatiForm() throws F3BException {
		RichiestePmInCumuloModel lRicMod = new RichiestePmInCumuloModel();

		lRicMod.setCodTipoRichiesta("02"); // dominio TIPO_RICHIESTA_CUMULO: 02 = Richiesta alla SORV.
		lRicMod.setCodTipoAnnotazione("020"); // dominio TIPO_ANNOTAZIONE: 020 Revoca Liberazione Anticipata

		lRicMod.setFlagPiuMenoR("-"); // fisso a - = revoca

		// La Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		lRicMod.setNumGiorniRevocaLA(
				getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV));
		lRicMod.setNumGiorniRevocaLS(
				getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV));
		lRicMod.setNumGiorniRevocaLI(
				getRequestBigDecimalParameter(ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV));

		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		if (!isRequestParameterNullObj(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe