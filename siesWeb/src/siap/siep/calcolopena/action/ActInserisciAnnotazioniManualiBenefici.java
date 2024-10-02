package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciAnnotazioniManualiBenefici
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento delle annotazioni manuali Decisioni del GE - Applicazione
 * Benefici - Amnistia/Indulto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciAnnotazioniManualiBenefici extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Action che effettua l'inserimento delle Decisione del GE di Applicazione Benefici Amnistia e Indulto
	 * Affettua l'iserimento dell'Ordinanza (una sola volta) e dei provvedimenti
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();
		IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();

		// Se esiste la Decisione del GE si duplica l'Annotazione e
		// la si collega al Provvedimento del PM.
		if (!isRequestParameterNullObj("IdAnnGE")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decisione del GE già presente");
			AnnotazioneManualeModel lAnnotazione = creaAnnotazionePMdaGE(
					getRequestBigDecimalParameter("IdAnnGE"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnnotazione = " + lAnnotazione);

			EventoModel lProvvedimentoPM = creaEventoProvvedimentoPM(lIdFascicolo,
					lAnnotazione.getFlagConforme());

			// MEV 29 - Rideterminazione Pena - Annotazione Ordinanza Indulto
			// anche se l'ordinanza è stata inserita SIGE, la data emissione del
			// provvedimento (04) deve essere la stessa dell'ordinanza. Vedi controller
			// nel caso di inserimento SIEP.
			if (lAnnotazione != null && lAnnotazione.getDataGE() != null)
				lProvvedimentoPM.setDataEmissione(lAnnotazione.getDataGE());

			// MEV_2019-09 Cerco idAnnotazione Richiesta
			AnnotazioneManualeModel lAnnRich = lAnnManCtrl.ExRicercaAnnotazioneManualeByKey(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE));

			AnnotazioneManualeModel lAnnIns = new AnnotazioneManualeModel();
			if (lAnnRich != null) {
				lAnnIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEventoUpd(lAnnotazione, lProvvedimentoPM,
						lAnnRich.getIdAnnotazioneManuale(), getRequestBigDecimalParameter("IdAnnGE"));
			} else {
				// caso in cui viene scaricato esito senza Richiesta Rideterminazione pena
				lAnnIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEventoUpd(lAnnotazione, lProvvedimentoPM,
						null, getRequestBigDecimalParameter("IdAnnGE"));
			}

			setRequestAttribute("lFlagPage", "AMNI");
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniManuali&"
					+ ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE + "="
					+ lAnnIns.getIdAnnotazioneManuale();

			return lPage;
		}

		// ==========================================================================
		// Recupero i dati dell'Annotazione Manuale
		// ==========================================================================
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lAnnMod.setCodTipoAnnotazione(getRequestStringParameter("tipoannotazione"));
		lAnnMod.setFlagAppProvvisoria("-");

		lAnnMod.setFlagValidato("N");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");

		lAnnMod.setCodDpr(getRequestStringParameter("dpr"));
		if (!isRequestParameterNullObj("TipoOrd")) {
			if (getRequestStringParameter("TipoOrd").equals("Conforme"))
				lAnnMod.setFlagConforme("C");
			else if (getRequestStringParameter("TipoOrd").equals("Difforme"))
				lAnnMod.setFlagConforme("D");
			else if (getRequestStringParameter("TipoOrd").equals("Rigetta"))
				lAnnMod.setFlagConforme("R");
			else if (getRequestStringParameter("TipoOrd").equals("Inammissibile"))
				lAnnMod.setFlagConforme("I");
			else if (getRequestStringParameter("TipoOrd").equals("Riunisce"))
				lAnnMod.setFlagConforme("U");
			else
				lAnnMod.setFlagConforme("-");
		}

		String noteRec = getRequestStringParameter("noteRec");
		lAnnMod.setNoteReclusione(noteRec);

		if (!isRequestParameterNullObj("IdReato")) {
			lAnnMod.setReaIdReato(getRequestBigDecimalParameter("IdReato"));
		}

		// =========================================================
		// Recupero la Reclusione e la Multa
		// =========================================================
		String GRec = getRequestStringParameter("GRec");
		String MRec = getRequestStringParameter("MRec");
		String ARec = getRequestStringParameter("ARec");
		String Multa = getRequestStringParameter("Multa");
		String Multa_dec = getRequestStringParameter("Mul_dec");

		if (!ARec.equals(""))
			lAnnMod.setNumAnniReclusione(new BigDecimal(ARec));
		if (!MRec.equals(""))
			lAnnMod.setNumMesiReclusione(new BigDecimal(MRec));
		if (!GRec.equals(""))
			lAnnMod.setNumGiorniReclusione(new BigDecimal(GRec));

		if (!Multa.equals("")) {
			if (!Multa_dec.equals("")) {
				lAnnMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
			} else
				lAnnMod.setImportoMulta(new BigDecimal(Multa));
		} else if (!Multa_dec.equals(""))
			lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));

		// =========================================================
		// Recupero la Arresti e Ammenda
		// =========================================================
		String GArr = getRequestStringParameter("GArr");
		String MArr = getRequestStringParameter("MArr");
		String AArr = getRequestStringParameter("AArr");
		String Ammenda = getRequestStringParameter("Ammenda");
		String Ammenda_dec = getRequestStringParameter("Amm_dec");

		if (!AArr.equals(""))
			lAnnMod.setNumAnniArresto(new BigDecimal(AArr));
		if (!MArr.equals(""))
			lAnnMod.setNumMesiArresto(new BigDecimal(MArr));
		if (!GArr.equals(""))
			lAnnMod.setNumGiorniArresto(new BigDecimal(GArr));

		if (!Ammenda.equals("")) {
			if (!Ammenda_dec.equals(""))
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
			else
				lAnnMod.setImportoAmmenda(new BigDecimal(Ammenda));
		} else if (!Ammenda_dec.equals(""))
			lAnnMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));

		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());

		// ===============================================================
		// ===== Aggiunta Toppa INDULTO ===== (anno e numero Ordinanza SIGE)
		// ===============================================================
		if (getRequestStringParameter("annoGe").equals("")) {
			lAnnMod.setAnnoGe(new BigDecimal(0));
		} else
			lAnnMod.setAnnoGe(getRequestBigDecimalParameter("annoGe"));

		lAnnMod.setNumeroGe(getRequestStringParameter("numeroGe"));
		if (!getRequestStringParameter("DaAnArr").equals(""))
			lAnnMod.setDataGE(DateUtils.getDate(getRequestStringParameter("DaAnArr"),
					getRequestStringParameter("DaMeArr"), getRequestStringParameter("DaGiArr")));

		// 07-2015 - MEV29 punto 11 - Anno e Numero Procedimento SIGE
		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE)
				&& !getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE).equals("")
				&& getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE) != null) {
			lAnnMod.setChiaveAnnoSige(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE));
			lAnnMod.setChiaveNumeroSige(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE));
		}

		// AMBROS 05/2014 - in alcuni casi (se tipo richiesta è con anticipazione o meno),
		// non metto il segno per avere un corretto Calcolodella pena

		String TipodiRichie = getRequestStringParameter("tRich");

		lAnnMod.setMotivazioni(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI));
		if (lAnnMod.getFlagConforme().equals("C") || lAnnMod.getFlagConforme().equals("D")
				|| lAnnMod.getFlagConforme().equals("-")) {
			lAnnMod.setFlagPiuMeno(getRequestStringParameter("PM"));
		} else if ((lAnnMod.getFlagConforme().equals("R") || lAnnMod.getFlagConforme().equals("I")
				|| lAnnMod.getFlagConforme().equals("U")) && TipodiRichie.equals("A")) {
			lAnnMod.setFlagPiuMeno(getRequestStringParameter("PM"));
		}
		// END AMBROS

		// ===============================================================

		// ==========================================================================
		// Recupero le richieste da legare alla decisione
		// ==========================================================================

		Vector lListaRichieste = new Vector();
		int lNumTotRichieste = getRequestIntParameter("NumTotRichieste");

		for (int i = 0; i < lNumTotRichieste; i++) {
			if (!isRequestParameterNullObj("cb_record_" + (i + 1))) {
				AnnotazioneManualeModel lAnnRichiesta = new AnnotazioneManualeModel();
				lAnnRichiesta.setIdAnnotazioneManuale(getRequestBigDecimalParameter("cb_record_" + (i + 1)));
				lListaRichieste.add(lAnnRichiesta);

			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("cb_record_" + (i + 1) + " is null");
		}
		// if (true)
		// return "xxx";

		// ***********************************
		String lDescrLuogoEmittente = "-";
		String lCodUffEmi = "-";

		// ==========================================================================
		// Se non è stata ancora inserita l'Ordinanza del GE, creo l'annotazione
		// da associargli. n.b. è una annotazione fittizia, non contiene dati
		// significativi, contiene solo anno numero e data della declaratoria.
		// ==========================================================================
		AnnotazioneManualeModel lAnnOrdMod = new AnnotazioneManualeModel();

		boolean lOrdinanzaPresente = false; // sempre false!!!!! vedi JSP loadAnnotazioniManualiBenefici
		if (getRequestStringParameter("flagOrdinanza").equals("true")) {
			lOrdinanzaPresente = true;
		}

		if (!lOrdinanzaPresente) { // sempre vera questa condizione

			String lCodTipoUfficioEmittente = "-";
			if (getRequestStringParameter("annoGe").equals("")) {
				lAnnOrdMod.setAnnoGe(new BigDecimal(0));
			} else
				lAnnOrdMod.setAnnoGe(getRequestBigDecimalParameter("annoGe")); // Anno Ordinanza SIGE

			lAnnOrdMod.setNumeroGe(getRequestStringParameter("numeroGe")); // Numero Ordinanza SIGE
			if (!getRequestStringParameter("DaAnArr").equals(""))
				lAnnOrdMod.setDataGE(DateUtils.getDate(getRequestStringParameter("DaAnArr"),
						getRequestStringParameter("DaMeArr"), getRequestStringParameter("DaGiArr")));

			// 07-2015 - MEV29 punto 11 - Anno e Numero Procedimento SIGE
			if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE)
					&& !getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE)
							.equals("")
					&& getRequestStringParameter(
							ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE) != null) {
				lAnnOrdMod.setChiaveAnnoSige(
						getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE));
				lAnnOrdMod.setChiaveNumeroSige(
						getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE));
			}

			lAnnOrdMod.setIdAnnotazioneManuale(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE));

			lAnnOrdMod.setFasSieIdFascicoloSiep(lIdFascicolo);
			lAnnOrdMod
					.setMotivazioni(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI));
			lAnnOrdMod.setFlagValidato("S");
			lAnnOrdMod.setFlagConforme("-");
			lAnnOrdMod.setCodTipoAnnotazione("-");
			lAnnOrdMod.setCodDpr("-");
			lAnnOrdMod.setCodFonte("-");
			lAnnOrdMod.setCodSottonumerazione("-");
			lAnnOrdMod.setCodCausaleComputo("-");
			lAnnOrdMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAnnOrdMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAnnOrdMod.setDataInserimento(DateUtils.getSysDate());

			lCodTipoUfficioEmittente = getRequestStringParameter("CodTipoUffEmi");
			lDescrLuogoEmittente = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE);

			if (!lCodTipoUfficioEmittente.equals("-") && !lCodTipoUfficioEmittente.equals("")
					&& !lDescrLuogoEmittente.equals("") && !lDescrLuogoEmittente.equals("-")) {
				lCodUffEmi = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioEmittente,
						lDescrLuogoEmittente);
			}
		}

		boolean lInserireOrdinanza = false;
		if (lAnnOrdMod.getDataGE() != null
				// && lAnnOrdMod.getAnnoGe() != null
				// && lAnnOrdMod.getNumeroGe() != null
				// && lAnnOrdMod.getNumeroGe() != ""
				&& lAnnOrdMod.getChiaveAnnoSige() != null && lAnnOrdMod.getChiaveNumeroSige() != null) {
			lInserireOrdinanza = true;
		}

		// ==========================================================================
		// Evento Ordinanza che simula l'ordinanza del GE
		// ==========================================================================
		EventoModel lEveOrdinanzaMod = new EventoModel();

		lEveOrdinanzaMod.setCodTipoEvento("01");
		lEveOrdinanzaMod.setCodTipoProvvedimento("03"); // ORDINANZA
		lEveOrdinanzaMod.setCodMotivo("0284"); // 0284-Applicazione Amnistia / Indulto
		// --- Come devono essere gestiti i documenti di altri uffici simulati?
		// lEveOrdinanzaMod.setFlagDocumentoRegistrato("S");
		// --- il flag come deve essere gestito?
		lEveOrdinanzaMod.setFlagStampaSiep("S");
		lEveOrdinanzaMod.setFlagVideoSiep("S");
		lEveOrdinanzaMod.setCodUfficioEmittente(lCodUffEmi);
		lEveOrdinanzaMod.setCodLuogoEmittente(getCodComuneByDescr(lDescrLuogoEmittente).getCodComune());
		lEveOrdinanzaMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveOrdinanzaMod.setDataEmissione(lAnnOrdMod.getDataGE());
		// AMBROS a8-rr-222
		if (!isRequestParameterNullObj("TipoOrd")) {
			if (getRequestStringParameter("TipoOrd").equals("Conforme"))
				lEveOrdinanzaMod.setCodEsito("C");
			else if (getRequestStringParameter("TipoOrd").equals("Difforme"))
				lEveOrdinanzaMod.setCodEsito("D");
			else if (getRequestStringParameter("TipoOrd").equals("Rigetta"))
				lEveOrdinanzaMod.setCodEsito("R");
			else if (getRequestStringParameter("TipoOrd").equals("Inammissibile"))
				lEveOrdinanzaMod.setCodEsito("I");
			else if (getRequestStringParameter("TipoOrd").equals("Riunisce"))
				lEveOrdinanzaMod.setCodEsito("U");
			else
				lEveOrdinanzaMod.setCodEsito("-");
		}
		// END AMBROS

		// ==========================================================================
		// Evento Provvedmento della Esecuzione
		// ATTENZIONE non aggancia il provvedimento all'ordinanza
		// ==========================================================================

		EventoModel lEveProvvedimentoMod = creaEventoProvvedimentoPM(lIdFascicolo, lAnnMod.getFlagConforme());

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug(lAnnMod.toString());

		// ==========================================================================
		// Effettuo l'inserimento dell'Ordinanza, del Provvedimento e dell'Annotazione
		// ==========================================================================
		String lPage = "";

		if (getRequestStringParameter("operazione").equals("Torna")) { // non più utilizzato

			lAnnManCtrl.ExInserisciAnnotazioneManuale(lAnnMod);

			lPage = IWebConstants.ROOT_DIR
					+ "Main.jsp?Action=siap.siep.calcolopena.action.ActLoadNuovoCalcoloPenaBenefici";
			return lPage;
		}

		if (getRequestStringParameter("operazione").equals("Quantum")) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("operazione = quantum");
			AnnotazioneManualeModel lAnnManIns = null;

			if (!lOrdinanzaPresente && lInserireOrdinanza) { // sempre vero
																// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
																// variabile di istanza siesLogger al posto di
																// LogF3B.getLogger()
				siesLogger.debug("Ordinanza assente");
				lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeProvvedimentoRichiesta(lAnnMod,
						lEveOrdinanzaMod, lEveProvvedimentoMod, lAnnOrdMod, lListaRichieste);
			} else { // sempre falso dato che l'ordinanza per ora non viene da SIGE
						// MEV 29 - Rideterminazione Pena - Annotazione Ordinanza Indulto
						// anche se l'ordinanza è stata inserita SIGE, la data emissione del
						// provvedimento (04) deve essere la stessa dell'ordinanza. Vedi controller
						// nel caso di inserimento SIEP.
						//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("Ordinanza già presente = "+lEveOrdinanzaMod);
				if (lEveOrdinanzaMod != null && lEveOrdinanzaMod.getDataEmissione() != null)
					lEveProvvedimentoMod.setDataEmissione(lEveOrdinanzaMod.getDataEmissione());
				lAnnManIns = lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnMod, lEveProvvedimentoMod);
			}

			setRequestAttribute("lFlagPage", "AMNI");

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniManuali&"
					+ ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE + "="
					+ lAnnManIns.getIdAnnotazioneManuale();

			return lPage;
		}

		return null;
	}

	/**
	 * Crea l'Evento- Provvedimento AMNISTIA/INDULTO del PM
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws Exception
	 */
	private EventoModel creaEventoProvvedimentoPM(BigDecimal aIdFascicolo, String esito) throws Exception {
		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setFasSieIdFascicoloSiep(aIdFascicolo);
		lEveProvvedimentoMod.setCodTipoEvento("01");
		lEveProvvedimentoMod.setCodTipoProvvedimento("04"); // PROVVEDIMENTO
		lEveProvvedimentoMod.setCodMotivo("0284"); // 0284-Applicazione Amnistia / Indulto
		lEveProvvedimentoMod.setFlagDocumentoRegistrato(null);
		lEveProvvedimentoMod.setFlagStampaSiep("S");
		lEveProvvedimentoMod.setFlagVideoSiep("S");
		// AMBROS a8-rr-222 _ metto Esito
		if (!isRequestParameterNullObj("TipoOrd")) {
			if (getRequestStringParameter("TipoOrd").equals("Conforme"))
				lEveProvvedimentoMod.setCodEsito("C");
			else if (getRequestStringParameter("TipoOrd").equals("Difforme"))
				lEveProvvedimentoMod.setCodEsito("D");
			else if (getRequestStringParameter("TipoOrd").equals("Rigetta"))
				lEveProvvedimentoMod.setCodEsito("R");
			else if (getRequestStringParameter("TipoOrd").equals("Inammissibile"))
				lEveProvvedimentoMod.setCodEsito("I");
			else if (getRequestStringParameter("TipoOrd").equals("Riunisce"))
				lEveProvvedimentoMod.setCodEsito("U");
			else
				lEveProvvedimentoMod.setCodEsito("-");
		} else {
			lEveProvvedimentoMod.setCodEsito(esito);
		}

		// END AMBROS

		lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveProvvedimentoMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("yyyy"),
				DateUtils.getSysDate("MM"), DateUtils.getSysDate("dd")));
		return lEveProvvedimentoMod;
	}

	/**
	 * Crea l'Annotazione Manuale da associare al Provvedimento di AMNISTIA/INDULTO del PM partendo
	 * dall'Annotazione del GE.
	 * 
	 * @param aIdAnnotazioneMan
	 * @return
	 * @throws Exception
	 */

	private AnnotazioneManualeModel creaAnnotazionePMdaGE(BigDecimal aIdAnnotazioneMan) throws Exception {

		// Ricerca Annotazione Manuale da copiare
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnManCorrente = lCtrlAnnMan
				.ExRicercaAnnotazioneManualeByKey(aIdAnnotazioneMan);

		// La nuova Annotazione viene creata come copia della iniziale
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel(lAnnManCorrente);

		// Valorizzazione campi specifici
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setFlagValidato("N");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");
		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());

		// I dati relativi all'Ordinanza GE vengono letti dalla request
		lAnnMod.setAnnoGe(getRequestBigDecimalParameter("annoGE"));
		lAnnMod.setNumeroGe(getRequestStringParameter("numGE"));
		lAnnMod.setDataGE(DateUtils.getDate(getRequestStringParameter("DaAnArr"),
				getRequestStringParameter("DaMeArr"), getRequestStringParameter("DaGiArr")));

		// 07-2015 - MEV29 punto 11 - Anno e Numero Procedimento SIGE
		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE)
				&& !getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE).equals("")
				&& getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE) != null) {
			lAnnMod.setChiaveAnnoSige(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_ANNO_SIGE));
			lAnnMod.setChiaveNumeroSige(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_CHIAVE_PROGR_SIGE));
		}

		// Si lega l'Annotazione del Provvedimento del PM all'Annotazione della decisione del GE (Ordinanza)
		lAnnMod.setAnnoIdAnnotazioneManuale(aIdAnnotazioneMan);

		return lAnnMod;
	}

}