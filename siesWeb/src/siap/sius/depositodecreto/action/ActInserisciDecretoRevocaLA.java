package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
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
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
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

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciDecretoRevocaLA extends ActionSius implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private class PeriodoClass {
		Date mDataIni = null;
		Date mDataFine = null;
	}

	// Data odierna
	protected Date mOggi = null;

	private PeriodoClass[] mPeriodi = null;
	private int mInd = 0;
	private String mTipoConcessione; /* modalità di scelta dei periodi concessi. (S/C) L.A NORMALE */

	private PeriodoClass[] mPeriodi_spe = null;
	private int mInd_spe = 0;
	private String mTipoConcessione_spe; /* modalità di scelta dei periodi concessi. (S/C) L.A. SPECIALE */

	private PeriodoClass[] mPeriodi_int = null;
	private int mInd_int = 0;
	private String mTipoConcessione_int; /*
											 * modalità di scelta dei periodi concessi. (S/C) L.A.
											 * INTEGRAZIONE
											 */

	public String processRequest() throws Exception {

		// String lRetPage = "";
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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ActInserisciDecretoRevocaLA - GeneraleProcedimento = " + lGenProcModel);
		// ===

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		if (lMagRel == null || lMagRel.getMagistrato() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Magistrato relatore non definito!");
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

		// GESTIONE TENORI ORDINANZA
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

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ActInserisciDecretoRevocaLA - Tenore n." + i + " = " + lTenori[i]);

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

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA)
				&& getRequestStringParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA) != null
				&& !getRequestStringParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA).equals("")) {
			// In questo campo c'è direttamente la descrizione dell'Ufficio Magistrato inserimento
			// dell'Ordinanza L.A.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" XXXXXXXXXXXXXXXXXXXXXXX Mag = " +
			// getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA));
			lDepDecrModel.setDescrMagistrato(getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP_RECLA));
		}

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)
				&& getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE) != null
				&& !getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE)
						.equals("")) {
			// lDepDecrModel.setSentenzeRiferimento(getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
			lDepDecrModel.setNote(
					getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ActInserisciDecretoRevocaLA - DepositoDecreto = " + lDepDecrModel);

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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ActInserisciDecretoRevocaLA - Evento = " + lEventoModel);

		// DEPOSITO DECRETO + TENORE
		DepositoDecretoEventoModel lDepDecrEveModel = new DepositoDecretoEventoModel();
		lDepDecrEveModel.setDepositoDecreto(lDepDecrModel);
		lDepDecrEveModel.setEvento(lEventoModel);

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
		// Ufficio Emittente
		String ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di "
				+ user.getUfficioUtente().getDescrComune();

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector<AvvocatoSiusModel> avvocati = null;
		avvocati = lAvvCtrl
				.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		// Recupero i dati del soggetto dalla sessione @emma 25/08/2016 - avvocatura
		String cognomeSoggetto = "";
		String nomeSoggetto = "";
		if (!isSessionAttributeNullObj("soggetto")) {
			SoggettoModel datiSoggetto = (SoggettoModel) getSessionAttribute("soggetto");
			cognomeSoggetto = datiSoggetto.getCognome();
			nomeSoggetto = datiSoggetto.getNome();
		} else if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null) {
			// provo a verificare se è presente nell'oggetto FascicoloGPModel
			cognomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null
					? lFasGPMod.getFascicoloSiusModel().getSoggetto().getCognome()
					: "";
			// EC@19/05/2017: correzione su nomeSoggetto. Inserivamo il nome sul cognome!
			nomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null
					? lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome()
					: "";
		} else {
			// devo procedere con una ricerca del soggetto per chiave soggetto
			BigDecimal idSoggetto = lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto();
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			SoggettoModel s = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);
			cognomeSoggetto = s.getCognome();
			nomeSoggetto = s.getNome();
		}

		Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = new Vector<>();

		AvvisiAvvocatoModel lAvvisiAvvModel = null;

		Iterator itxAvv = avvocati.iterator();
		while (itxAvv.hasNext()) {
			lAvvisiAvvModel = new AvvisiAvvocatoModel();

			AvvocatoSiusModel lAvv = (AvvocatoSiusModel) itxAvv.next();

			lAvvisiAvvModel.setIdAvvocato(lAvv.getAvvocato().getIdAvvocato());
			lAvvisiAvvModel.setCognomeSoggeto(cognomeSoggetto);
			lAvvisiAvvModel.setNomeSoggetto(nomeSoggetto);
			// lAvvisiAvvModel.setIdProvvedimento(); // viene settato nel controller
			lAvvisiAvvModel.setDescProvvedimento("Decreto");
			lAvvisiAvvModel.setUfficioEmittente(ufficio);
			lAvvisiAvvModel.setTestoAvviso(ICostantiAvvisiAvvocato.CONTENUTO_EMISSIONE_DECRETO);
			lAvvisiAvvModel.setFlagVisualizzazione("N");
			lAvvisiAvvModel.setCodOperatoreInserimento(user.getUserId());
			lAvvisiAvvModel.setCodUfficioInserimento(user.getUfficioUtente().getCodUfficio());
			lAvvisiAvvModel.setDataInserimento(DateUtils.getSysDate());

			lAvvvisiAvvocato.add(lAvvisiAvvModel);

		}

		// ***********************************************************************
		// MEV_AVVOCATURA aggiunto parametro lAvvvisiAvvocato
		// ***********************************************************************
		lDepDecrEveModel = inserimento(lGPTenoreModel, lDepDecrEveModel, lFasGPMod, lAvvvisiAvvocato);
		// lDepDecrEveModel = inserimento(lGPTenoreModel, lDepDecrEveModel, lFasGPMod);

		// *********************************************
		// MEV_AVVOCATURA - FINE
		// *********************************************

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ActInserisciDecretoRevocaLA - Id Evento INSERITO  = "
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
			DepositoDecretoEventoModel lDepDecrEveModel, FascicoloGPModel mFasGPMod,
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("-------------> - INSERIMENTO REVOCA L.A. NORMALE ");

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> L.A. NORMALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//

		LicenzaPeriodiLibAnticipataModel[] lLicenze = null;
		LicenzaLibAnticipataModel lLicenzaC = null;

		String[] lChecks = null;
		// String[] lDate = null;
		// PeriodoClass[] periodi = null;

		int numCheck = 0;
		int numCheckConcessi = 0;

		boolean lConcessi = false;
		boolean lPeriodo = false;
		boolean lRigettati = false;
		boolean lInammissibili = false;
		boolean lNLP = false;
		//
		// Prendo subito gli eventuali giorni di L.A.

		int SommatotLA = 0;

		if (getRequestBigDecimalParameter(
				ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA) != null) {
			if (getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA)
					.intValue() > 0) {
				SommatotLA = getRequestBigDecimalParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA).intValue();
			}
		}
		if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA) != null) {
			if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA)
					.intValue() > 0) {
				SommatotLA = getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA)
						.intValue();
			}
		}

		mTipoConcessione = this.getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE); // S semestri -
																							// C periodo
																							// Unico

		if (isRequestChecked(CAMPO_CHECK_CONCESSI)) {
			// Numero chek periodi da 45 gg ( se CAMPO_RADIO_TIPO_CONCESSIONE = S)
			lChecks = getRequestStringParameters(CAMPO_CHECK_CONCESSI);
			numCheck += lChecks.length;
			lConcessi = true;
			numCheckConcessi = numCheck;
		}
		if (isRequestChecked(CAMPO_CHECK_PERIODO)) {
			// check per inserimento periodo in periodo Unico ( se CAMPO_RADIO_TIPO_CONCESSIONE = C)
			numCheck++;
			lPeriodo = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI)) {
			numCheck++;
			lRigettati = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI)) {
			numCheck++;
			lInammissibili = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP)) {
			numCheck++;
			lNLP = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek " + numCheck);

		// Per Le Revoche in materia di LA se l'esito è 'Revoca'
		// il FLAG_CONCESSO va impostato ad 'S' (scomputa) -- Michele

		String flagConcesso = "";

		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)
					.compareTo("S") == 0)
				flagConcesso = "S";
		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A. e Periodi concessi.
		if (numCheck > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("LicLib");
			lLicenze = new LicenzaPeriodiLibAnticipataModel[numCheck];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi = leggiDate();

			if (lConcessi) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" --> Semestri Concessi");

				while (i < numCheckConcessi) {
					lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
					lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);
					setPeriodiInLicenze(lLicenze[i]);
					i++;
				}
			}
			if (lPeriodo) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("--> PeriodoUnico con date dal al");

				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));

				if (SommatotLA > 0)
					lLicenze[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLA));

				lLicenze[i].getLicenza().setFlagScorta(mTipoConcessione);
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}

			if (lRigettati) {
				flagConcesso = "R";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
			if (lInammissibili) {
				flagConcesso = "I";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}
			if (lNLP) {
				flagConcesso = "N";
				lLicenze[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze[i].setLicenza(generaLicenza(flagConcesso, mFasGPMod));
				setPeriodiInLicenze(lLicenze[i]);
				i++;
			}

		} // Chiude if numCheck > 0

		// devo ricontrollarlo perchè 'flagConcesso' potrebbe essere diventato R, oppure I, o N
		if (!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)) {
			if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_PM_ACCOLTO)
					.compareTo("S") == 0)
				flagConcesso = "S";
		}

		// if (numCheck == 0 && mTipoConcessione.compareTo("C") == 0)
		if (mTipoConcessione.compareTo("C") == 0 && lPeriodo == false && SommatotLA > 0) {
			// Periodo Unico senza check periodo, cioè con solo numero gg senza date dal .. al ..

			lLicenzaC = new LicenzaLibAnticipataModel();
			lLicenzaC.setCodTipoLicenza("LA");
			lLicenzaC.setCodOperatoreInserimento(getCodUtenteConnesso());
			lLicenzaC.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lLicenzaC.setDataInserimento(mOggi);
			lLicenzaC.setFlagConcesso(flagConcesso);

			if (SommatotLA > 0)
				lLicenzaC.setNumeroGiorni(new BigDecimal(SommatotLA));

			if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
				lLicenzaC.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lLicenzaC.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
			lLicenzaC.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			// L.A. NORMALE
			lLicenzaC.setDescrStatoPermesso("LAU");
			lLicenzaC.setFlagScorta(mTipoConcessione);
		}

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> L.A. SPECIALE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--------------> - INSERIMENTO REVOCA L.A. SPECIALE ");

		LicenzaPeriodiLibAnticipataModel[] lLicenze_spe = null;
		LicenzaLibAnticipataModel lLicenzaC_SPE = null;

		String[] lChecks_spe = null;
		// String[] lDate_spe = null;
		// PeriodoClass[] periodi_spe = null;

		int numCheck_spe = 0;
		int numCheckConcessi_spe = 0;

		boolean lConcessi_spe = false;
		boolean lPeriodo_spe = false;
		boolean lRigettati_spe = false;
		boolean lInammissibili_spe = false;
		boolean lNLP_spe = false;
		//
		// Prendo subito gli eventuali giorni di L.A.SPECIALE

		int SommatotLS = 0;

		if (getRequestBigDecimalParameter(
				ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE) != null) {
			if (getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE)
					.intValue() > 0) {
				SommatotLS = getRequestBigDecimalParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE).intValue();
			}
		}
		if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE) != null) {
			if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE)
					.intValue() > 0) {
				SommatotLS = getRequestBigDecimalParameter(
						ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE).intValue();
			}
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> Tot gg concessi SPE - SommatotLS = "+SommatotLS);
		mTipoConcessione_spe = this.getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE_SPE); // S
																									// semestri
																									// - C
																									// periodo
																									// Unico

		if (isRequestChecked(CAMPO_CHECK_CONCESSI_SPE)) { // Numero chek periodi da 75 gg ( se
															// CAMPO_RADIO_TIPO_CONCESSIONE = S)
			lChecks_spe = getRequestStringParameters(CAMPO_CHECK_CONCESSI_SPE);
			numCheck_spe += lChecks_spe.length;
			lConcessi_spe = true;
			numCheckConcessi_spe = numCheck_spe;
		}
		if (isRequestChecked(CAMPO_CHECK_PERIODO_SPE)) { // check per inserimento periodo in periodo Unico (
															// se CAMPO_RADIO_TIPO_CONCESSIONE = C)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("--> periodo Unico SPE ");
			numCheck_spe++;
			lPeriodo_spe = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI_SPE)) {
			numCheck_spe++;
			lRigettati_spe = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_SPE)) {
			numCheck_spe++;
			lInammissibili_spe = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP_SPE)) {
			numCheck_spe++;
			lNLP_spe = true;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek L.A. Speciale " + numCheck_spe);

		// Per i Reclami in materia di 'L.A. SPECIALE' se l'esito è 'Accoglie reclamo del PM'
		// il 'flagConcesso_spe' va impostato ad 'S' -- Michele

		String flagConcesso_spe = "";

		if (!isRequestParameterNullObj(
				ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)
							.compareTo("S") == 0)
				flagConcesso_spe = "S";
		}

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.SPECIALE e Periodi concessi.
		if (numCheck_spe > 0) {
			lLicenze_spe = new LicenzaPeriodiLibAnticipataModel[numCheck_spe];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_spe = leggiDate_spe();

			if (lConcessi_spe) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" --> Semestri SPE Concessi");
				while (i < numCheckConcessi_spe) {
					lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
					lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
					setPeriodiInLicenze_spe(lLicenze_spe[i]);
					i++;
				}
			}
			if (lPeriodo_spe) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("--> PeriodoUnico SPE con date dal al");

				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));

				if (SommatotLS > 0)
					lLicenze_spe[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLS));

				lLicenze_spe[i].getLicenza().setFlagScorta(mTipoConcessione_spe);
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lRigettati_spe) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" -------> Rigettati SPE");
				flagConcesso_spe = "R";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lInammissibili_spe) {
				flagConcesso_spe = "I";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

			if (lNLP_spe) {
				flagConcesso_spe = "N";
				lLicenze_spe[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_spe[i].setLicenza(generaLicenza_spe(flagConcesso_spe, mFasGPMod));
				setPeriodiInLicenze_spe(lLicenze_spe[i]);
				i++;
			}

		} // chiude if numcheck_spe > 0

		// devo ricontrollarlo perchè 'flagConcesso_spe' potrebbe essere diventato R, oppure I, o N
		if (!isRequestParameterNullObj(
				ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)) {
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_SPE_PM_ACCOLTO)
							.compareTo("S") == 0)
				flagConcesso_spe = "S";
		}

		// if (numCheck_spe == 0 && mTipoConcessione_spe.compareTo("C") == 0)
		if (mTipoConcessione_spe.compareTo("C") == 0 && lPeriodo_spe == false && SommatotLS > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> Periodo Unico senza check periodo, cioè con solo numero gg senza date dal
			// .. al .. ");
			lLicenzaC_SPE = new LicenzaLibAnticipataModel();

			lLicenzaC_SPE.setCodTipoLicenza("LA");
			lLicenzaC_SPE.setCodOperatoreInserimento(getCodUtenteConnesso());
			lLicenzaC_SPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lLicenzaC_SPE.setDataInserimento(mOggi);
			lLicenzaC_SPE.setFlagConcesso(flagConcesso_spe);

			if (SommatotLS > 0)
				lLicenzaC_SPE.setNumeroGiorni(new BigDecimal(SommatotLS));

			if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
				lLicenzaC_SPE
						.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lLicenzaC_SPE.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
			lLicenzaC_SPE.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			// L.A. SPECIALE
			lLicenzaC_SPE.setDescrStatoPermesso("LSU");
			lLicenzaC_SPE.setFlagScorta(mTipoConcessione_spe);
		}

		// -------------------------------------------------------------
		// >>>>>>>>>>>>>>>>> L.A. INTEGRAZIONE <<<<<<<<<<<<<<<<<<<<<<<<<<<<
		//

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("------------->  INSERIMENTO REVOCA L.A. INTEGRAZIONE");
		//
		LicenzaPeriodiLibAnticipataModel[] lLicenze_int = null;
		LicenzaLibAnticipataModel lLicenzaC_INT = null;

		String[] lChecks_int = null;
		// String[] lDate_int = null;
		// PeriodoClass[] periodi_int = null;

		int numCheck_int = 0;
		int numCheckConcessi_int = 0;

		boolean lConcessi_int = false;
		boolean lPeriodo_int = false;
		boolean lRigettati_int = false;
		boolean lInammissibili_int = false;
		boolean lNLP_int = false;
		//
		// prendo subito i giorni di L.A. INTEGRAZIONE

		int SommatotLI = 0;

		if (getRequestBigDecimalParameter(
				ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT) != null) {
			if (getRequestBigDecimalParameter(ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT)
					.intValue() > 0) {
				SommatotLI = getRequestBigDecimalParameter(
						ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT).intValue();
			}
		}
		if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT) != null) {
			if (getRequestBigDecimalParameter(ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT)
					.intValue() > 0) {
				SommatotLI = getRequestBigDecimalParameter(
						ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT).intValue();
			}
		}

		mTipoConcessione_int = this.getRequestStringParameter(CAMPO_RADIO_TIPO_CONCESSIONE_INT);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> tipo concessione _int (radio Button semestri/periodo unico) = " +
		// mTipoConcessione_int);

		if (isRequestChecked(CAMPO_CHECK_CONCESSI_INT)) {
			lChecks_int = getRequestStringParameters(CAMPO_CHECK_CONCESSI_INT);
			numCheck_int += lChecks_int.length;
			lConcessi_int = true;
			numCheckConcessi_int = numCheck_int;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> LA I - CAMPO_CHECK_CONCESSI_INT - numCheckConcessi_int = " +
			// numCheckConcessi_int);
		}
		if (isRequestChecked(CAMPO_CHECK_PERIODO_INT)) {
			numCheck_int++;
			lPeriodo_int = true;
		}
		if (isRequestChecked(CAMPO_CHECK_RIGETTATI_INT)) {
			numCheck_int++;
			lRigettati_int = true;
		}
		if (isRequestChecked(CAMPO_CHECK_INAMMISSIBILI_INT)) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> LA I CHECK_INAMMISSIBILI_INT ");
			numCheck_int++;
			lInammissibili_int = true;
		}
		if (isRequestChecked(CAMPO_CHECK_NLP_INT)) {
			numCheck_int++;
			lNLP_int = true;
		}

		// Per i Reclami in materia di 'L.A. IINTEGRAZIONE' se l'esito è 'Accoglie reclamo del PM'
		// il 'flagConcesso_int' va impostato ad 'S' -- Michele

		String flagConcesso_int = "";

		if (!isRequestParameterNullObj(
				ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)) {
			// Controlla e imposta il flag
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)
							.compareTo("S") == 0)
				flagConcesso_int = "S";
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero chek L.A. Integrazione " + numCheck_int);

		// 17/07/2008 Da fare Controllo incrociato tra Esito L.A.INTEGRAZIONE e Periodi concessi.
		if (numCheck_int > 0) {
			lLicenze_int = new LicenzaPeriodiLibAnticipataModel[numCheck_int];
			int i = 0;

			// Inizializzazione dei periodi valorizzati nella form di input
			mPeriodi_int = leggiDate_int();

			if (lConcessi_int) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("--> LA I - lConcessi_int - numCheckConcessi_int = "+numCheckConcessi_int
				// );

				while (i < numCheckConcessi_int) {
					lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
					lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
					lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
					setPeriodiInLicenze_int(lLicenze_int[i]);
					i++;
				}
			}
			if (lPeriodo_int) {
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));

				if (SommatotLI > 0)
					lLicenze_int[i].getLicenza().setNumeroGiorni(new BigDecimal(SommatotLI));

				lLicenze_int[i].getLicenza().setFlagScorta(mTipoConcessione_int);
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}

			if (lRigettati_int) {
				flagConcesso_int = "R";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
			if (lInammissibili_int) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("--> LA I - lInammissibili_int - " );
				flagConcesso_int = "I";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}
			if (lNLP_int) {
				flagConcesso_int = "N";
				lLicenze_int[i] = new LicenzaPeriodiLibAnticipataModel();
				lLicenze_int[i].setLicenza(generaLicenza_int(flagConcesso_int, mFasGPMod));
				setPeriodiInLicenze_int(lLicenze_int[i]);
				i++;
			}

		} // chiude if numcheck_int > 0

		// devo ricontrollarlo perchè 'flagConcesso_int' potrebbe essere diventato R, oppure I, o N
		if (!isRequestParameterNullObj(
				ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)) {
			if (getRequestStringParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESITO_REVOCA_LA_INT_PM_ACCOLTO)
							.compareTo("S") == 0)
				flagConcesso_int = "S";
		}

		// if (numCheck_int == 0 && mTipoConcessione_int.compareTo("C") == 0)
		if (mTipoConcessione_int.compareTo("C") == 0 && lPeriodo_int == false && SommatotLI > 0) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> Periodo Unico senza check periodo, cioè con solo numero gg senza date dal
			// .. al .. ");
			lLicenzaC_INT = new LicenzaLibAnticipataModel();

			lLicenzaC_INT.setCodTipoLicenza("LA");
			lLicenzaC_INT.setCodOperatoreInserimento(getCodUtenteConnesso());
			lLicenzaC_INT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lLicenzaC_INT.setDataInserimento(mOggi);
			lLicenzaC_INT.setFlagConcesso(flagConcesso_int);

			if (SommatotLI > 0)
				lLicenzaC_INT.setNumeroGiorni(new BigDecimal(SommatotLI));

			if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
				lLicenzaC_INT
						.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			lLicenzaC_INT.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
			lLicenzaC_INT.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			// L.A. INTEGRAZIONE
			lLicenzaC_INT.setDescrStatoPermesso("LIU");
			lLicenzaC_INT.setFlagScorta(mTipoConcessione_int);
		}

		// ---------------------------------------------------------------------------------------------------
		// SOMMA TOTALE GIORNI REVOCATI
		//
		// 10-03-2014 ---> Nuova ordinanza L.A. dopo DECRETO 146/2013 //
		// Da oggi in poi in "aOrdEveTenGP" va messo il numero di giorni totali di LA ( LA + LS + LI)
		//
		// SommatotLA = eventuale Num. gg. Totali di L.A. NORMALE
		// SommatotLS = eventuale Num. gg. Totali di L.A. SPECIALE
		// SommatotLI = eventuale Num. gg. Totali di L.A. INTEGRAZIONE

		int Sommatot = 0;
		Sommatot = SommatotLA + SommatotLS + SommatotLI;

		// Somma totale dei giorni Revocati in DEPOSITO_ORDINANZA_PC
		lDepDecrEveModel.getDepositoDecreto().setNumeroGiorniRevocaLA(new BigDecimal(Sommatot));

		// Somma totale dei giorni Revocati in DEPOSITO_ORDINANZA_PC
		// aOrdEveTenGP.getOrdinanza().setNumGiorniLibanticipata(new BigDecimal(Sommatot));
		//
		// --> Ordinanza
		// OrdinanzaEventoTenoriGProcModel lModRet = null;
		// IDepositoOrdinanzaPc IDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		// lModRet = (IDepOrdCtrl.ExInserisciOrdinanzaLibAnt(aOrdEveTenGP, lLicenze, lLicenze_spe,
		// lLicenze_int, lLicenzaC, lLicenzaC_SPE, lLicenzaC_INT) );
		// End ordinanza <--
		//
		// Deceto
		DepositoDecretoEventoModel lModRet = new DepositoDecretoEventoModel();

		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		try {
			lModRet = (lDepDecrCtrl.ExInserisciDecretoRevocaLiberazAnticipata(lGPTenoreModel,
					lDepDecrEveModel, lLicenze, lLicenze_spe, lLicenze_int, lLicenzaC, lLicenzaC_SPE,
					lLicenzaC_INT, lAvvvisiAvvocato));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (lModRet == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "NESSUN INSERIMENTO EFFETTUATO !");
		return lModRet;
	} // Chiude metodo. public OrdinanzaEventoTenoriGProcModel inserimento

	// >>>>>>>> L.A NORMALE Lettura di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate() throws F3BException {

		PeriodoClass[] lPeriodi = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("-------> leggiDate");

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
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataIni = " + lPeriodi[i].mDataIni);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("----> C leggiDate: lPeriodi[i].mDataFine = " + lPeriodi[i].mDataFine);
		}

		return lPeriodi;
	}

	private void setPeriodiInLicenze(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd; // Salvataggio del valore corrente di mInd

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" setPeriodiInLicenze - inizio lInd = "+lInd);

		for (int j = 0, k = mInd; j < 10; j++, k++) {
			if ((mPeriodi[k].mDataIni != null) && (mPeriodi[k].mDataFine != null))
				num++;
		}

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd

		for (int j = 0; j < num; mInd++) {
			if ((mPeriodi[mInd].mDataIni != null) && (mPeriodi[mInd].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa(
						aLicenza.getLicenza().getFlagConcesso());
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.

		mInd = lInd + 10;
	} // chiude setPeriodiInLicenze

	private LicenzaLibAnticipataModel generaLicenza(String aFlagConcesso, FascicoloGPModel mFasGPMod)
			throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza ini ");
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza("LA");
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(45));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante ");

		// Si inseriscono i dati che servono alla Procura !?! Luigi 30-6-2005
		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		// L.A. NORMALE
		lLicenza.setDescrStatoPermesso("LA");

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(getClass().getName() + ".generaLicenza fine ");

		return lLicenza;
	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa(String aFlagConcesso) throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi[mInd].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi[mInd].mDataFine);
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

	/*
	 * >>>>>>>>>>>>>>>>>>>>>>>>>> L.A. SPECIALE
	 */
	// L.A SPECIALE Lettura di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate_spe() throws F3BException {

		PeriodoClass[] lPeriodi_spe = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("leggiDate_spe");

		lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO_SPE, CAMPO_MESE_DATA_INIZIO_SPE,
				CAMPO_GIORNO_DATA_INIZIO_SPE);
		lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE_SPE, CAMPO_MESE_DATA_FINE_SPE,
				CAMPO_GIORNO_DATA_FINE_SPE);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> CLOD - Numero date spe " + lNumDate);

		lPeriodi_spe = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi_spe[i] = new PeriodoClass();
			lPeriodi_spe[i].mDataIni = lDateInizio[i];
			lPeriodi_spe[i].mDataFine = lDateFine[i];
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> i = "+i+" - lPeriodi_spe[i].mDataIni = " + lPeriodi_spe[i].mDataIni);
		}
		return lPeriodi_spe;
	}

	private void setPeriodiInLicenze_spe(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd_spe; // Salvataggio del valore corrente di mInd

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" setPeriodiInLicenze_spe - inizio lInd = "+lInd+" - mInd_spe = "+mInd_spe );

		for (int j = 0, k = mInd_spe; j < 10; j++, k++)
			if ((mPeriodi_spe[k].mDataIni != null) && (mPeriodi_spe[k].mDataFine != null))
				num++;

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd_spe++) {
			if ((mPeriodi_spe[mInd_spe].mDataIni != null) && (mPeriodi_spe[mInd_spe].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_spe(
						aLicenza.getLicenza().getFlagConcesso());
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		mInd_spe = lInd + 10;
	} // chiude setPeriodiInLicenze_spe

	private LicenzaLibAnticipataModel generaLicenza_spe(String aFlagConcesso, FascicoloGPModel mFasGPMod)
			throws F3BException {

		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza("LA");
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(75));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante -_spe-");

		// Si inseriscono i dati che servono alla Procura !?! Luigi 30-6-2005
		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		// L.A. SPECIALE
		lLicenza.setDescrStatoPermesso("LS");

		return lLicenza;

	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa_spe(String aFlagConcesso) throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("generaPeriodoLibAnticipa_spe ini ");

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi_spe[mInd_spe].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi_spe[mInd_spe].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;
	}

	/*
	 * >>>>>>>>>>>>>>>>>>>>>>>>>> L.A. INTEGRAZIONE
	 */
	// L.A INTEGRAZIONE Lettura di tutti i periodi di date valorizzati nella form di input.
	private PeriodoClass[] leggiDate_int() throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--> CLeggi date int - inizio");
		PeriodoClass[] lPeriodi_int = null;
		Date[] lDateInizio = null;
		Date[] lDateFine = null;

		lDateInizio = getRequestDateParameters(CAMPO_ANNO_DATA_INIZIO_INT, CAMPO_MESE_DATA_INIZIO_INT,
				CAMPO_GIORNO_DATA_INIZIO_INT);
		lDateFine = getRequestDateParameters(CAMPO_ANNO_DATA_FINE_INT, CAMPO_MESE_DATA_FINE_INT,
				CAMPO_GIORNO_DATA_FINE_INT);

		int lNumDate = (lDateInizio.length < lDateFine.length) ? lDateInizio.length : lDateFine.length;

		lPeriodi_int = new PeriodoClass[lNumDate];

		for (int i = 0; i < lDateFine.length; i++) {
			lPeriodi_int[i] = new PeriodoClass();
			lPeriodi_int[i].mDataIni = lDateInizio[i];
			lPeriodi_int[i].mDataFine = lDateFine[i];
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--> i = "+i+ " - lPeriodi_int[i].mDataIni = " + lPeriodi_int[i].mDataIni);

		}

		return lPeriodi_int;
	}

	private void setPeriodiInLicenze_int(LicenzaPeriodiLibAnticipataModel aLicenza) throws F3BException {

		// Conteggio dei periodi valorizzati
		int num = 0; // numero di periodi validi letti [0 : 10 ] a partire da mInd
		int lInd = mInd_int; // Salvataggio del valore corrente di mInd

		for (int j = 0, k = mInd_int; j < 10; j++, k++)
			if ((mPeriodi_int[k].mDataIni != null) && (mPeriodi_int[k].mDataFine != null))
				num++;

		// I Periodi per la Licenza sono in numero di num
		if (num > 0)
			aLicenza.setPeriodi(new PeriodoLibAnticipataModel[num]);

		// Generazione dei Periodi di Liertà Anticipa
		// I Periodi validi vengono estratti dall'array generale mPeriodi
		// scandito attraverso l'indice mInd
		for (int j = 0; j < num; mInd_int++) {
			if ((mPeriodi_int[mInd_int].mDataIni != null) && (mPeriodi_int[mInd_int].mDataFine != null)) {
				aLicenza.getPeriodi()[j++] = generaPeriodoLibAnticipa_int(
						aLicenza.getLicenza().getFlagConcesso());
			}
		}
		// Aggiornamento dell'indice generale, assunto che il numero di date per gruppo sia di 10.
		mInd_int = lInd + 10;

	} // chiude setPeriodiInLicenze_int

	private LicenzaLibAnticipataModel generaLicenza_int(String aFlagConcesso, FascicoloGPModel mFasGPMod)
			throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" generaLicenza_int - inizio - ");
		LicenzaLibAnticipataModel lLicenza;
		lLicenza = new LicenzaLibAnticipataModel();

		lLicenza.setCodTipoLicenza("LA");
		lLicenza.setCodOperatoreInserimento(getCodUtenteConnesso());
		lLicenza.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lLicenza.setDataInserimento(mOggi);
		lLicenza.setFlagConcesso(aFlagConcesso);

		if (aFlagConcesso.compareTo("S") == 0)
			lLicenza.setNumeroGiorni(new BigDecimal(30));

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null)
			lLicenza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" Fascicolo SIUS mancante -_int-");

		// Si inseriscono i dati che servono alla Procura !?! Luigi 30-6-2005
		lLicenza.setNumeroSius(mFasGPMod.getFascicoloSiusModel().getChiaveProgr().toString());
		lLicenza.setAnnoSius(mFasGPMod.getFascicoloSiusModel().getChiaveAnno());
		// L.A. INTEGRAZIONE
		lLicenza.setDescrStatoPermesso("LI");

		return lLicenza;

	}

	private PeriodoLibAnticipataModel generaPeriodoLibAnticipa_int(String aFlagConcesso) throws F3BException {

		// Valorizza un Periodo leggendo le date all'indice mInd nell'Array mPeriodi
		PeriodoLibAnticipataModel lPeriodoLib = null;

		lPeriodoLib = new PeriodoLibAnticipataModel();
		lPeriodoLib.setDataInizio(mPeriodi_int[mInd_int].mDataIni);
		lPeriodoLib.setDataFine(mPeriodi_int[mInd_int].mDataFine);
		lPeriodoLib.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPeriodoLib.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPeriodoLib.setDataInserimento(mOggi);
		lPeriodoLib.setFlagConcesso(aFlagConcesso);

		return lPeriodoLib;
	}

} // Chiude Classe Action