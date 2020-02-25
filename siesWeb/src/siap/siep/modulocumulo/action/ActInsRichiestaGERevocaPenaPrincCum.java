package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta al GE di Revoca Pena Principale Cumulo
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInsRichiestaGERevocaPenaPrincCum extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInsRichiestaGERevocaPenaPrincCum....");

		super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		RichiestePmInCumuloModel lRichModel = null;

		if ("I".equals(lModalita)) {
			// Inserimento
			lRichModel = this.getDatiForm();

			lRichModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lRichModel.setDataInserimento(DateUtils.getSysDate());
			lRichModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			siesLogger.debug("INSERT - ho riempito il model lRichModel = " + lRichModel);
		} else if ("M".equals(lModalita)) {
			// Modifica
			lRichModel = this.getDatiForm();

			lRichModel
					.setIdRichiestePmInCumulo(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO));
			lRichModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lRichModel.setDataAggiornamento(DateUtils.getSysDate());
			lRichModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			siesLogger.debug("MODIFICA lRichModel = " + lRichModel);
		}

		// -----------------------------------------------------------------------------------
		// Stringhe[] con gli 'Id' delle Entità selezionate che riguardano la Rihiesta
		// -----------------------------------------------------------------------------------
		// TITOLO_CUMULATO_MODEL:
		String[] lIdTitoliSelezionati = null;
		String[] lIdTitoliCompleti = null;

		// REATO_CUMULO_MODEL
		String[] lIdReatiSelezionati = null;

		if ("I".equals(lModalita)) {

			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

			if (!isRequestParameterNullObj("idTitoloCompleto"))
				lIdTitoliCompleti = getRequestStringParameters("idTitoloCompleto");

			if (!isRequestParameterNullObj("idReatoSelezionato"))
				lIdReatiSelezionati = getRequestStringParameters("idReatoSelezionato");
		}

		// ====================================================================================
		// La parte seguente di Codice serve per implementare un Vector con tutti i dati
		// aggragati dei Titoli selezionati dall'Utente che riguardano la Richiesta;
		// ====================================================================================
		Vector<TitoloCumulatoModel> lListaTitoli = new Vector<>();

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		IReatoCumulo ReaCtrl = SIEPLookupRemote.getReatoCumuloRemote();

		if ("I".equals(lModalita)) {
			for (int cct = 0; cct < lIdTitoliSelezionati.length; cct++) {
				TitoloCumulatoModel lTitoMod = null;
				lTitoMod = lCtrlT.ExRicercaTitoloCumulatoById(new BigDecimal(lIdTitoliSelezionati[cct]));

				Boolean lcompl = false;
				Boolean lparz = false;
				// cerco se è tra i titoli completi
				if (lIdTitoliCompleti != null && lIdTitoliCompleti.length > 0) {
					for (int kkt = 0; kkt < lIdTitoliCompleti.length; kkt++) {
						if (lIdTitoliCompleti[kkt].compareTo(lIdTitoliSelezionati[cct]) == 0) {
							lcompl = true;
						}
					}
				}

				// Tratto i Reati
				Vector<ReatoCircostanzaCumuloModel> VecReati = new Vector<>();
				VecReati = ReaCtrl
						.ExRicercaReatoCircostanzaCumByTitoloCum(new BigDecimal(lIdTitoliSelezionati[cct]));
				Vector<ReatoCircostanzaCumuloModel> VecRea = new Vector<>();

				if (VecReati != null && VecReati.size() > 0) {
					if (!lcompl) {
						Iterator itR = VecReati.iterator();
						while (itR.hasNext()) {
							ReatoCircostanzaCumuloModel lRea = (ReatoCircostanzaCumuloModel) itR.next();
							if (lIdReatiSelezionati != null && lIdReatiSelezionati.length > 0) {
								for (int ccr = 0; ccr < lIdReatiSelezionati.length; ccr++) {
									if (lRea.getReatoCum().getIdReatoCum()
											.equals(new BigDecimal(lIdReatiSelezionati[ccr]))) {
										VecRea.add(lRea);
										lparz = true;
									}
								}
							}
						}

						if (VecRea != null && VecRea.size() > 0)
							lTitoMod.setReatoCircostanzaCumulo(VecRea);
					} else {
						lTitoMod.setReatoCircostanzaCumulo(VecReati);
					}
				}

				// nella lista va o il titolo completo
				// o il titolo che abbia almeno un elemento selezionato (un Reato )
				if (lparz) {
					lListaTitoli.add(lTitoMod);
				} else {
					if (lcompl) {
						lListaTitoli.add(lTitoMod);
					}
				}

			} // Chiude ciclo for

		} // Chiude if(modalita="I")

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablla di Collegamento tra RICHIESTE_PM_IN_CUMULO e le altre Entità
		// Correlate:
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici(lRichModel,
					lListaTitoli);
		} else if ("M".equals(lModalita)) {
			ICtrlRic.ExModificaRichiestePmInCumulo(lRichModel);
			lRicRetMod.setIdRichiestePmInCumulo(lRichModel.getIdRichiestePmInCumulo());
		} else if ("C".equals(lModalita)) {
			// Cancellazione
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
			ICtrlRic.ExCancellaRichiestePmInCumuloFull(lId);
		}

		// ==========================================================================
		//
		// ==========================================================================
		String lPage = "";
		if (!"C".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGERevocaPenaPrincCum";
			lPage += "&" + CAMPO_ID_RICHIESTE_PM_IN_CUMULO + "="
					+ lRicRetMod.getIdRichiestePmInCumulo().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE";
		}

		return lPage;
	}

	/**
	 * Metodo che recupera i dati dalla form
	 */
	private RichiestePmInCumuloModel getDatiForm() throws F3BException {

		// siesLogger.debug("--XX-- >>>> Sono nel metodo getDatiForm() ");
		RichiestePmInCumuloModel lRicMod = new RichiestePmInCumuloModel();

		lRicMod.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA)); // dominio
																							// TIPO_RICHIESTA_CUMULO:
																							// 01 = Richiesta
																							// al G.E.
		lRicMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE));

		// Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		// ==========================================================================================================
		// Gestione Quantum
		// ==========================================================================================================
		lRicMod.setFlagPiuMenoR(getRequestStringParameter(CAMPO_FLAG_PIU_MENO_R));

		// Reclusione e Multa
		lRicMod.setNumAnniReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE_R));
		lRicMod.setNumMesiReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE_R));
		lRicMod.setNumGiorniReclusioneR(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE_R));

		if ((getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC")).equals(""))) {
			lRicMod.setImportoMultaR(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "INT")
					+ "." + getRequestStringParameter(CAMPO_IMPORTO_MULTA_R + "DEC")));
		}

		// Arresto e Ammenda
		lRicMod.setNumAnniArrestoR(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO_R));
		lRicMod.setNumMesiArrestoR(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO_R));
		lRicMod.setNumGiorniArrestoR(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO_R));

		if ((getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "DEC")).equals(""))) {
			lRicMod.setImportoAmmendaR(
					new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "INT") + "."
							+ getRequestStringParameter(CAMPO_IMPORTO_AMMENDA_R + "DEC")));
		}

		if (isRequestChecked(CAMPO_FLAG_APP_PROVVISORIA))
			lRicMod.setFlagAppProvvisoria("A"); // Con Anticipazione
		else
			lRicMod.setFlagAppProvvisoria("R"); // semplice richiesta

		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));

		// =======================================================================================================
		// Campi Relativi a Depenalizzazione o Illecito Amm.
		if ("004".equals(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE))
				|| "017".equals(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE))) {
			lRicMod.setCodFonte(getRequestStringParameter(CAMPO_COD_FONTE));
			// siesLogger.debug("--XX-- Depe/ Illec fonte = "+getRequestStringParameter(CAMPO_COD_FONTE));
			lRicMod.setAnnoFonte(getRequestBigDecimalParameter(CAMPO_ANNO_FONTE));
			lRicMod.setNumeroFonte(getRequestStringParameter(CAMPO_NUMERO_FONTE));
			lRicMod.setArticolo(getRequestStringParameter(CAMPO_ARTICOLO));
			lRicMod.setCodSottonumerazione(getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
			// siesLogger.debug("--XX-- Depe/ Illec Sottonume BisTer =
			// "+getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
			lRicMod.setComma(getRequestStringParameter(CAMPO_COMMA));
			lRicMod.setLettera(getRequestStringParameter(CAMPO_LETTERA));
			lRicMod.setNumero(getRequestStringParameter(CAMPO_NUMERO));
		}

		// Campi Relativi a Incostituzionalità
		if ("013".equals(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE))) {
			lRicMod.setAnnoCc(getRequestBigDecimalParameter(CAMPO_ANNO_CC));
			lRicMod.setNumeroCc(getRequestStringParameter(CAMPO_NUMERO_CC));
			lRicMod.setDataCc(
					getRequestDateParameter(CAMPO_ANNO_DATA_CC, CAMPO_MESE_DATA_CC, CAMPO_GIORNO_DATA_CC));
		}

		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe