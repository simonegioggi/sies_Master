package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'Inserimento, Modifica, Cancellazione di una richiesta al GE di Applicazione Beneficio
 * dell'Indulto/Amnistia
 *
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActInserisciRichiestaGEBenefici extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- >>>> Start  ActInserisciRichiestaGEBenefici....");

		/* IstruttoriaCumuloModel lIstrCumulo = */super.getDatiIstruttoria();

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("lModalita = " + lModalita);

		/*
		 * String lStato="I"; if(lModalita.equals("M")) { // Cambia stato solo se il dato NON è Iscritto
		 * manualmente if(getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("E")
		 * || getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("M") ) { lStato
		 * = "M"; } }
		 */
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

			siesLogger.debug("MODIFICA lRichModel = " + lRichModel);

			// BigDecimal lId = getRequestBigDecimalParameter("idDecreto");
			// update
		}

		siesLogger.debug(
				"--XX-- >>>> Inizio getRequest delle String con gli ID delle entita oggetto della richiesta ");
		// -----------------------------------------------------------------------------------
		// Stringhe[] con gli 'Id' delle Entità selezionate che riguardano la Rihiesta
		// -----------------------------------------------------------------------------------
		// TITOLO_CUMULATO_MODEL:
		String[] lIdTitoliSelezionati = null;
		String[] lIdTitoliCompleti = null;

		// REATO_CUMULO_MODEL
		String[] lIdReatiSelezionati = null;

		// MISURA_SICUREZZA_CUMULO_MODEL
		String[] lIdMisureSelezionate = null;

		// PENA_ACCESSORIA_CUMULO_MODEL
		String[] lIdPenaAccSelezionate = null;

		if ("I".equals(lModalita)) {

			lIdTitoliSelezionati = getRequestStringParameters(CAMPO_ID_TITOLO_SELEZIONATO);

			if (!isRequestParameterNullObj("idTitoloCompleto"))
				lIdTitoliCompleti = getRequestStringParameters("idTitoloCompleto");

			if (!isRequestParameterNullObj("idReatoSelezionato"))
				lIdReatiSelezionati = getRequestStringParameters("idReatoSelezionato");

			if (!isRequestParameterNullObj("idMisuraSel"))
				lIdMisureSelezionate = getRequestStringParameters("idMisuraSel");

			if (!isRequestParameterNullObj("idPenaAccSel"))
				lIdPenaAccSelezionate = getRequestStringParameters("idPenaAccSel");
		}

		// ====================================================================================
		// La parte seguente di Codice serve per implementare un Vector con tutti i dati
		// aggragati dei Titoli selezionati dall'Utente che riguardano la Richiesta;
		// ====================================================================================
		siesLogger.debug("--XX-- >>>> Inizio Gestione campi della form per Tabelle Relazione ");
		Vector<TitoloCumulatoModel> lListaTitoli = new Vector<>();

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		IReatoCumulo ReaCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		IPenaAccessoriaCumulo PenaCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
		IMisuraSicurezzaCumulo MisuCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();

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

				// Tratto le Misure di Sicurezza
				Vector<MisuraSicurezzaCumuloModel> VecMisure = new Vector<>();
				VecMisure = MisuCtrl.ExRicercaMisureSicurezzaCumuloByIdTitoloCum(
						new BigDecimal(lIdTitoliSelezionati[cct]));
				Vector<MisuraSicurezzaCumuloModel> VecMisu = new Vector<>();

				if (VecMisure != null && VecMisure.size() > 0) {
					if (!lcompl) {
						Iterator itM = VecMisure.iterator();
						while (itM.hasNext()) {
							MisuraSicurezzaCumuloModel lMisu = (MisuraSicurezzaCumuloModel) itM.next();
							if (lIdMisureSelezionate != null && lIdMisureSelezionate.length > 0) {
								for (int ccm = 0; ccm < lIdMisureSelezionate.length; ccm++) {
									if (lMisu.getIdMisuraSicurezzaCumulo()
											.equals(new BigDecimal(lIdMisureSelezionate[ccm]))) {
										VecMisu.add(lMisu);
										lparz = true;
									}
								}
							}
						}

						if (VecMisu != null && VecMisu.size() > 0)
							lTitoMod.setMisureSicurezzaCumulo(VecMisu);
					} else {
						lTitoMod.setMisureSicurezzaCumulo(VecMisure);
					}
				}

				// Tratto le Pene Accessorie
				Vector<PenaAccessoriaCumuloModel> VecPeneAcc = new Vector<>();
				VecPeneAcc = PenaCtrl.ExRicercaPenaAccessoriaCumuloByIdTitoloCum(
						new BigDecimal(lIdTitoliSelezionati[cct]));
				Vector<PenaAccessoriaCumuloModel> VecPen = new Vector<>();

				if (VecPeneAcc != null && VecPeneAcc.size() > 0) {
					if (!lcompl) {
						Iterator itP = VecPeneAcc.iterator();
						while (itP.hasNext()) {
							PenaAccessoriaCumuloModel lPen = (PenaAccessoriaCumuloModel) itP.next();
							if (lIdPenaAccSelezionate != null && lIdPenaAccSelezionate.length > 0) {
								for (int ccp = 0; ccp < lIdPenaAccSelezionate.length; ccp++) {
									if (lPen.getIdPenaAccessoriaCumulo()
											.equals(new BigDecimal(lIdPenaAccSelezionate[ccp]))) {
										VecPen.add(lPen);
										lparz = true;
									}
								}
							}
						}

						if (VecPen != null && VecPen.size() > 0)
							lTitoMod.setPeneAccessorieCumulo(VecPen);
					} else {
						lTitoMod.setPeneAccessorieCumulo(VecPeneAcc);
					}
				}

				// nella lista va o il titolo completo
				// o il titolo che abbia almeno un elemento selezionato (o un Reato, o una misura o una Pena
				// Acc)
				if (lparz) {
					lListaTitoli.add(lTitoMod);
				} else {
					if (lcompl) {
						lListaTitoli.add(lTitoMod);
					}
				}

			} // Chiude ciclo for

		} // Chiude if(modalita="I")

		siesLogger.debug("--XX-- >>>> Fine Gestione campi form per Tabelle Relazione ");

		// ============================================================================================================
		// Inserimento RICHIESTA e Tablle di Collegamento tra RICHIESTE_PM_IN_CUMULO e le altre Entità
		// Correlate:
		// ============================================================================================================
		IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
		RichiestePmInCumuloModel lRicRetMod = new RichiestePmInCumuloModel();
		if ("I".equals(lModalita)) {
			lRicRetMod = ICtrlRic.ExInserisciRichiestePmInCumulo_GE_ApplicazioneBenefici(lRichModel,
					lListaTitoli);
			// siesLogger.debug("--XX-- >>>> Sono tornato da Inserimento in RichiestePmInCumuloController ");
		} else if ("M".equals(lModalita)) {
			ICtrlRic.ExModificaRichiestePmInCumulo(lRichModel);
			// siesLogger.debug("--XX-- >>>> Sono tornato da Modifica in RichiestePmInCumuloController ");
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
					+ "=siap.siep.modulocumulo.action.ActDettaglioRichiestaGEBenefici";
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
		siesLogger.debug("--XX-- >>>> Sono nel metodo getDatiForm() ");
		RichiestePmInCumuloModel lRicMod = new RichiestePmInCumuloModel();

		lRicMod.setCodTipoRichiesta(getRequestStringParameter(CAMPO_COD_TIPO_RICHIESTA)); // dominio
																							// TIPO_RICHIESTA_CUMULO:
																							// 01 = Richiesta
																							// al G.E.
		lRicMod.setCodTipoAnnotazione(getRequestStringParameter(CAMPO_COD_TIPO_ANNOTAZIONE));

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

    // 09/05/2018 MEV70 - Valorizzazione nuovo Flag TitoloCompleto
    if (isRequestChecked("idTitoloCompleto")) 
        lRicMod.setFlagInteroCumulo("S");	// Intero Cumulo
      else 
        lRicMod.setFlagInteroCumulo("N"); //
    
		lRicMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));
		lRicMod.setMotivazioni(getRequestStringParameter(CAMPO_MOTIVAZIONI));
		// lRicMod.setNoteReclusione ( getRequestStringParameter ( CAMPO_NOTE_RECLUSIONE) );

		// Nella Form è indicata come Data_Richiesta
		lRicMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		if (!isRequestParameterNullObj(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO))
			lRicMod.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO));

		return lRicMod;
	} // Chiude Metodo getDatiForm()

} // Chiude classe
