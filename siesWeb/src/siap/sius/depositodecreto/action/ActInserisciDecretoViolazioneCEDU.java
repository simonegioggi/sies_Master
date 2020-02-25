package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciDecretoViolazioneCEDU extends ActionSius implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private class PeriodoClass {

		Date mDataIni = null;
		Date mDataFine = null;
	}

	// Data odierna
	protected Date mOggi = null;

	private PeriodoClass[] mPeriodi = null;

	// private int mInd = 0;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Inizializzazione data
		mOggi = DateUtils.getSysDate();

		// Si prelevano dati di sessione.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodiceComune = getCodComuneUtenteConnesso();

		// Si preleva dall sessione il fascicolo GPModel.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva id generale procedimento.
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

		// Data EMISSIONE
		Date lDataEmissione = getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE,
				"dd/MM/yyyy");

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
		lGenProcModel.setIdGeneraleProcedimento(lIdGenProc);
		lGenProcModel.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGenProcModel.setDataAggiornamento(mOggi);
		lGenProcModel.setCodUfficioAggiornamento(lCodiceUfficio);
		lGenProcModel.setCodOperatoreAggiornamento(lCodiceOperatore);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ActInserisciDecretoViolazioneCEDU - GeneraleProcedimento = " + lGenProcModel);
		// ===

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		if (lMagRel == null || lMagRel.getMagistrato() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Magistrato relatore non definito !");
		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		else {
			IMagistrato im = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel mm = new MagistratoModel();
			mm.setCodMagistrato(lMagRel.getMagistrato().getCodMagistrato());
			mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
			mm.setCognome(lMagRel.getMagistrato().getCognome());
			mm.setNome(lMagRel.getMagistrato().getNome());
			Vector v = im.ExRicercaMagistrato(mm);
			if (!v.isEmpty()) {
				MagistratoModel mag = (MagistratoModel) v.get(0);
				if (mag.getDataFineValidita() != null
						&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
								|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! Impossibile emettere il provvedimento. Assegnatario del procedimento è un magistrato non più in servizio!");
			}
		}

		// Prelevare il codice Magistrato_Relatore
		String lCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

		// GESTIONE TENORI DECRETO
		TenoreModel[] lTenori = null;

		// Lettura campi Tenore.
		String[] lCodOggetti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);
		String[] lDescOggetti = getRequestStringParameters(ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE);
		String[] lCodDettaglioOggetti = getRequestStringParameters(
				ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO);
		String[] lCodEsiti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);

		if (!((lCodOggetti.length == lDescOggetti.length)
				&& (lDescOggetti.length == lCodDettaglioOggetti.length)
				&& (lCodDettaglioOggetti.length == lCodEsiti.length)))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nella lettura dei Tenori");

		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();

		int lSizeArray = lCodOggetti.length;
		if (lSizeArray > 0) {
			lTenori = new TenoreModel[lSizeArray];
			for (int i = 0; i < lSizeArray; i++) {
				TenoreModel lTenModel = new TenoreModel();

				lTenModel.setProgrTenore(new BigDecimal((double) (i + 1)));
				lTenModel.setCodOggettoTenore(lCodOggetti[i]);
				lTenModel.setDescrOggettoTenore(lDescOggetti[i]);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioOggetti[i]);
				lTenModel.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(lCodEsiti[i]));

				// lTenModel.setCodEsitoTenore(lCodEsiti[i]);
				lTenModel.setCodUfficioInserimento(lCodiceUfficio); // Codice dell'ufficio dell'operatore che
																	// inserisce
				lTenModel.setCodOperatoreInserimento(lCodiceOperatore); // Codice dell'operatore che inserisce
				lTenModel.setDataInserimento(mOggi);
				lTenModel.setGenPridGeneraleProcedimento(lIdGenProc);
				lTenModel.setCodMagistrato(lCodMagistrato);

				// Inserimenti i-esimo Tenore
				lTenori[i] = lTenModel;

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" ActInserisciDecretoViolazioneCEDU - Tenore n."+i+" = " + lTenori[i]);

			}
		}

		// // Inserisce nel model aggregante GPTenoreModel l'Array di model dei Tenori e Il model
		// GeneraleProcedimento.
		GPTenoreModel lGPTenoreModel = new GPTenoreModel();

		lGPTenoreModel.setTenori(lTenori);
		lGPTenoreModel.setGeneraleProcedimentoModel(lGenProcModel);

		// Prepara il model DEPOSITODECRETO. -- Creare un Metodo private ? --
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel();
		lDepDecrModel.setDataEmissione(lDataEmissione);
		lDepDecrModel.setCodTipoDecreto(
				getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA));

		lDepDecrModel.setCodMagistrato(lCodMagistrato); // da Inserire qui ? Oppure nel controller?.
		lDepDecrModel.setGenPridGeneraleProcedimento(lIdGenProc);
		lDepDecrModel.setCodOperatoreInserimento(lCodiceOperatore);
		lDepDecrModel.setCodUfficioInserimento(lCodiceUfficio);
		lDepDecrModel.setDataInserimento(mOggi);
		lDepDecrModel.setCodTipoControlloEsecuzione("-");

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)) {
			if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)
					.compareTo("") != 0)
				lDepDecrModel.setCodUfficioCompetente(
						getCodUfficioByCodTipoUfficioDescrComune("UDS", getRequestStringParameter(
								ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP)));
			else
				lDepDecrModel.setCodUfficioCompetente("-");
		}

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)
						.equals("")) {
			// lDepDecrModel.setSentenzeRiferimento(getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
			lDepDecrModel.setNote(
					getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ActInserisciDecretoViolazioneCEDU - DepositoDecreto = " + lDepDecrModel);

		// EVENTO
		EventoModel lEventoModel = new EventoModel();
		lEventoModel.setCodTipoEvento("01"); // 01 = Provvedimento.
		lEventoModel.setCodTipoProvvedimento("02"); // 02 = Decreto.
		lEventoModel.setCodLuogoEmittente(lCodiceComune);
		lEventoModel.setCodUfficioEmittente(lCodiceUfficio);
		// lEvento.setCodEsito("-");
		lEventoModel.setDataEmissione(lDataEmissione);
		lEventoModel.setCodMagistrato(lCodMagistrato);
		lEventoModel.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEventoModel.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEventoModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEventoModel.setCodUfficioInserimento(lCodiceUfficio);
		lEventoModel.setDataInserimento(DateUtils.getSysDate());
		lEventoModel.setCodLuogoDestinatario("-");
		lEventoModel.setCodTipoUfficioDestinatario("-");
		lEventoModel.setCodUfficioDestinatario("-");

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ActInserisciDecretoViolazioneCEDU - Evento = " + lEventoModel);

		// DEPOSITO DECRETO + TENORE
		DepositoDecretoEventoModel lDepDecrEveModel = new DepositoDecretoEventoModel();
		lDepDecrEveModel.setDepositoDecreto(lDepDecrModel);
		lDepDecrEveModel.setEvento(lEventoModel);

		lDepDecrEveModel = inserimento(lGPTenoreModel, lDepDecrEveModel, lFasGPMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ActInserisciDecretoViolazioneCEDU - Id Evento INSERITO  = "
				+ lDepDecrEveModel.getEvento().getIdEvento());

		// Prepara la pagina di destinazione, in questo caso è il dettaglio del decreto d'inammissibilità.
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
				lDepDecrEveModel.getEvento().getIdEvento().toString());

		return lRedirectTo.toString();
	}

	public DepositoDecretoEventoModel inserimento(GPTenoreModel lGPTenoreModel,
			DepositoDecretoEventoModel lDepDecrEveModel, FascicoloGPModel mFasGPMod) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("----->  INSERIMENTO DECRETO VIOLAZIONE CEDU: PERIODO / SOMMA RISARCIMENTO  ");

		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
		// LicenzaLibAnticipataModel lLicenzaC = null;

		// String[] lChecks = null;
		// String[] lDate = null;
		// PeriodoClass[] periodi = null;

		int numCheck = 0;
		boolean lSomma = false;
		boolean lPeriodo = false;
		boolean lRigettati = false;
		boolean lInammissibili = false;
		boolean lNLP = false;
		//
		// eventuali giorni di di Riduzione pena concessi

		int GiorniRiduzione = 0;

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU)
						.equals("")) {
			GiorniRiduzione = getRequestBigDecimalParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU).intValue();
		}

		// eventuale somma da liquidare per risarcimento

		String SommaRisarcimento = "";

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU)
						.equals("")) {
			SommaRisarcimento += getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU);
		}

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU)
						.equals("")) {
			SommaRisarcimento += "."
					+ getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU);
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ActInserisciDecretoViolazioneCEDU - XXXXX Somma Risarcimento = "+
		// SommaRisarcimento);
		//

		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_PERIODI_CONCESSI_CEDU)) {
			numCheck++;
			lPeriodo = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_EURO_CONCESSI_CEDU)) {
			numCheck++;
			lSomma = true;
		}

		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_RIGETTATI_CEDU)) {
			numCheck++;
			lRigettati = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_INAMMISSIBILI_CEDU)) {
			numCheck++;
			lInammissibili = true;
		}
		if (isRequestChecked(ICostantiDepositoOrdinanzaPc.CAMPO_CHECK_NLP_CEDU)) {
			numCheck++;
			lNLP = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciDecretoViolazioneCEDU - Numero chek " + numCheck);

		String flagConcesso = "";
		String TipoLic = "";

		if (numCheck > 0) {
			lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
			int i = 0;

			mPeriodi = leggiDate();

			if (lPeriodo) {
				flagConcesso = "C";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod, lDepDecrEveModel));

				if (GiorniRiduzione > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(GiorniRiduzione));

				setPeriodiInLicenzeCEDU(lLicenze[i], 0);
				i++;
			}
			if (lSomma) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" --> Riarcimento in denaro Concesso");
				flagConcesso = "C";
				TipoLic = "SL";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod, lDepDecrEveModel));

				if (!SommaRisarcimento.equals(""))
					lLicenze[i].getLicenza().setSommaRisarcDanni(new BigDecimal(SommaRisarcimento));

				lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(0));
				setPeriodiInLicenzeCEDU(lLicenze[i], 10);
				i++;
			}

			if (lRigettati) {
				flagConcesso = "R";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod, lDepDecrEveModel));
				setPeriodiInLicenzeCEDU(lLicenze[i], 20);
				i++;
			}
			if (lInammissibili) {
				flagConcesso = "I";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod, lDepDecrEveModel));
				setPeriodiInLicenzeCEDU(lLicenze[i], 30);
				i++;
			}
			if (lNLP) {
				flagConcesso = "N";
				TipoLic = "RD";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenzaCEDU(flagConcesso, TipoLic, mFasGPMod, lDepDecrEveModel));
				setPeriodiInLicenzeCEDU(lLicenze[i], 40);
				i++;
			}

		} // Chiude if numCheck > 0

		// Somma totale dei giorni concessi e della somma da liquidare in Eu. in DEPOSITO_ORDINANZA_PC

		lDepDecrEveModel.getDepositoDecreto().setNumeroGiorniRiduzionePena(new BigDecimal(GiorniRiduzione));
		if (!SommaRisarcimento.equals(""))
			lDepDecrEveModel.getDepositoDecreto()
					.setSommaRisarcimentoDanni(new BigDecimal(SommaRisarcimento));
		//
		// Deceto
		DepositoDecretoEventoModel lModRet = new DepositoDecretoEventoModel();

		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		try {

			lModRet = (lDepDecrCtrl.ExInserisciDecretoRevocaLiberazAnticipata(lGPTenoreModel,
					lDepDecrEveModel, lLicenze, null, null, null, null, null, null));

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO !");
		return lModRet;

	} // Chiude metodo. public OrdinanzaEventoTenoriGProcModel inserimento

	//

	private PeriodoClass[] leggiDate() throws F3BException {

		PeriodoClass[] lPeriodi = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO);
		lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
				CAMPO_GIORNO_DATA_FINE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		lPeriodi = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi[i] = new PeriodoClass();
			lPeriodi[i].mDataIni = lDateInizio[i];
			lPeriodi[i].mDataFine = lDateFine[i];
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: Elemento Iesimo = >"+i+"<" );
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataIni = " + lPeriodi[i].mDataIni);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataFine = " + lPeriodi[i].mDataFine);
		}

		return lPeriodi;
	}

	private void setPeriodiInLicenzeCEDU(LicenzaPeriodiLibAnticipataModel aLicenza, int aInd)
			throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da aInd
		// int lInd = aInd; // Salvataggio del valore corrente di aInd

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" setPeriodiInLicenze - inizio lInd = "+lInd);

		for (int j = 0, k = aInd; j < 10; j++, k++) {
			if ((mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null)) {
				num++;
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" setPeriodiInLicenze - data_ini presa = "+mPeriodi[k].mDataIni);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" setPeriodiInLicenze -------------> num = "+num +" - J = "+j
				// +" - k = "+k);
			}
		}

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd

		for (int j = 0; j < num; aInd++) {
			if ((mPeriodi[aInd].mDataIni != null) && (mPeriodi[aInd].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa(aLicenza.getLicenza().getFlagConcesso(),
						aInd);
			}
		}

		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		// aInd = lInd + 10;

	} // chiude setPeriodiInLicenze

	private LicenzaLibAnticipataModel generaLicenzaCEDU(String aFlagConcesso, String aTipoLicenza,
			FascicoloGPModel mFasGPMod, DepositoDecretoEventoModel lDepDecrEveModel) throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza ini ");
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza(aTipoLicenza);
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante ");

		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());

		lLicenza.setCodLuogoEmittente(lDepDecrEveModel.getEvento().getCodLuogoEmittente());
		lLicenza.setCodUfficioEmittente(lDepDecrEveModel.getEvento().getCodUfficioEmittente());

		if (lDepDecrEveModel.getDepositoDecreto() != null
				&& lDepDecrEveModel.getDepositoDecreto().getAnnoS72() != null)
			lLicenza.setAnnoOrdinanza(lDepDecrEveModel.getDepositoDecreto().getAnnoS72());

		if (lDepDecrEveModel.getDepositoDecreto() != null
				&& lDepDecrEveModel.getDepositoDecreto().getNumS72() != null)
			lLicenza.setNumeroOrdinanza(lDepDecrEveModel.getDepositoDecreto().getNumS72());

		lLicenza.setDataEmissioneOrdinanza(lDepDecrEveModel.getDepositoDecreto().getDataEmissione());

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza fine ");

		return lLicenza;

	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa(String aFlagConcesso, int aInd)
			throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi[aInd].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi[aInd].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;

	}

	// private void controllo(LicenzaPeriodiLibAnticipataModel[] aLicenze) {
	//
	// if (aLicenze == null)
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("ATTENZIONE ---------------> Licenze assenti");
	// else
	// for (int i = 0; i < aLicenze.length; i++) {
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("*** Licenza n. " + i + " ***");
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug(aLicenze[i].toString());
	// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	// // LogF3B.getLogger()
	// siesLogger.debug("********");
	// }
	// }

} // Chiude Classe Action