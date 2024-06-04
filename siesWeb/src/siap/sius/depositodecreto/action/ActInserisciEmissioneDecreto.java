package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.util.RicercaProvvCollegati;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciEmissioneDecreto
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento dell'Emissione di un decreto generico.
 * </p>
 * L'azione legge contenuto ed oggetti relativi al procedimento scelto.
 * </p>
 * Legge il tipo di decreto da emettere.
 * </p>
 * Non effettua nesun inserimento nel DB ma passa i dati letti (tenori) alla form successiva.
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
public class ActInserisciEmissioneDecreto extends ActionSius implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String mRetPage = IWebConstants.PG_MESSAGE; // pagina di view
	FascicoloGPModel lFasGPMod = null;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdGenProc = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciEmissioneDecreto: inizio");

		// setLinkRitorno(); Sostituzione perchè problemi con bottone di ritorno
		this.gestioneRitorno();

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Tipo decreto non disponibile ...");

		// Si preleva dalla sessione il fascicolo GPModel.
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");
		lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));

		// Preleva id generale procedimento.
		lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (lIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "ID Generale Procedimento non in sessione");

		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		if (lFasGPMod.getFascicoloSiusModel() != null) {
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			MagistratoRelatoreModel mrm = imr
					.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			if (mrm != null && mrm.getMagistrato() != null) {
				IMagistrato im = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel mm = new MagistratoModel();
				mm.setCodMagistrato(mrm.getMagistrato().getCodMagistrato());
				mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
				mm.setCognome(mrm.getMagistrato().getCognome());
				mm.setNome(mrm.getMagistrato().getNome());
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
		}

		// Switch tipo decreto. Anticipato per effettuare il controllo
		String lCodTipoDec = selezione();

		// Preleva data di emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data Emissione :" + lDataEmissione);

		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);

		// Lettura Tenori
		// String codiciTenori = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
		// String descrTenori = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO);

		StringTokenizer lStCodice = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO), "|");
		StringTokenizer lStDescr = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO), "\n");
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("### DEBUG DESCRIZIONE OGGETTI ###");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro Cod oggetti ->" + lStCodice.countTokens());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N.ro Descr. oggetti ->" + lStDescr.countTokens());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Descr. oggetti ->" +

				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO));

		int lSizeVector = lStCodice.countTokens();

		TenoreModel lTenori[] = new TenoreModel[lSizeVector];
		String[] lEsiti = new String[lSizeVector];

		int lIndex = 0;
		super.setRequestAttribute("inFormaDiPanelTDSM", "hidden");

		String codOggettoProcedimento = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		if ((codOggettoProcedimento.equalsIgnoreCase(
				ICostantiDepositoOrdinanzaPc.COD_OGGETTO_PROC_CONCESSIONE_MISURE_ALTERNATIVA)
				|| codOggettoProcedimento.equalsIgnoreCase(
						ICostantiDepositoOrdinanzaPc.COD_OGGETTO_PROC_CONCESSIONE_LIBERAZIONE_CONDIZIONALE))
				&& super.isUserTDSM()) {
			super.setRequestAttribute("inFormaDiPanelTDSM", "visible");
		}

		if ((codOggettoProcedimento
				.equalsIgnoreCase(ICostantiDepositoOrdinanzaPc.COD_OGGETTO_APPLICAZIONE_MISURA_SICUREZZA)
				|| codOggettoProcedimento.equalsIgnoreCase(
						ICostantiDepositoOrdinanzaPc.COD_OGGETTO_RIESAME_PERICOLOSITA_SOCIALE)
				|| codOggettoProcedimento
						.equalsIgnoreCase(ICostantiDepositoOrdinanzaPc.COD_OGGETTO_LIBERAZIONE_CONDIZIONALE)
				|| codOggettoProcedimento.equalsIgnoreCase(
						ICostantiDepositoOrdinanzaPc.COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA))
				&& super.isUserUDSM()) {

			super.setRequestAttribute("inFormaDiPanelUDSM", "visible");
		}

		while (lStCodice.hasMoreTokens() && lStDescr.hasMoreTokens()) {
			TenoreModel lTenModel = new TenoreModel();
			String codiceTenore = lStCodice.nextToken();
			String descrizioneTenore = lStDescr.nextToken();

			lTenModel.setCodOggettoTenore(codiceTenore);
			lTenModel.setDescrOggettoTenore(descrizioneTenore);
			lTenModel.setCodEsitoTenore("-");

			// 12/11/2003 Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
			if ((lStCodiceDet).indexOf(lTenModel.getCodOggettoTenore() + "0") < 0) {
				lTenModel.setCodDettaglioOggetto("-");
			} else {
				String lCodDettaglioCorrente = lStCodiceDet.substring(
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 4,
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 8);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioCorrente);
			}

			// Setto l'Array
			lTenori[lIndex] = lTenModel;

			lEsiti[lIndex] = getEsito(lTenModel.getCodOggettoTenore());

			lIndex++;
		}

		// lettura contenuto e passaggio oltre
		setRequestAttribute("contenuto", lCodContenuto);
		// trasferimento tenori
		setRequestAttribute("tenori", lTenori);
		// trasferimento lista esiti
		setRequestAttribute("esiti", lEsiti);
		setRequestAttribute("tipo_decreto", lCodTipoDec);
		setRequestAttribute("data_emissione", lDataEmissione);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciEmissioneDecreto: -> page: " + mRetPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciEmissioneDecreto: fine");
		return mRetPage; // restituisce la jsp di VIEW
	}

	// Preleva gli esiti dalla CG_REF_CODES.
	private String getEsito(String codiceOggetti) throws Exception {

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		Collection<?> lColl = lDecodifiche.ExRicercaEsitiByOggetto(codiceOggetti);
		Option lOption = new Option(lColl, true);
		// STUB 21/07/2004 La Combo degli Esiti va impostata con il "-".
		lOption.setValueBlankItem("-");
		return lOption.toString();
	}

	/**
	 * Nel richiamo della pagina di Warning occorre riciclare i parametri nella request
	 *
	 * @throws Exception
	 */
	public void passaggioParametri() throws Exception {

		setRequestAttribute(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE,
				getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE));
		setRequestAttribute(ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE,
				getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE));
		setRequestAttribute(ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE,
				getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE));
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO,
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_COD_OGGETTO,
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO));
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO,
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO));
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO,
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));
		setRequestAttribute(IWebConstants.ACTION_FIELD,
				getRequestStringParameter(IWebConstants.ACTION_FIELD));
		if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			setRequestAttribute(IWebConstants.LINK_RITORNO,
					getRequestStringParameter(IWebConstants.LINK_RITORNO));
	}

	/**
	 * La funzione controlla l'esistenza di un decreto o di un'ordinanza con la stessa data di emissione. Nel
	 * caso esiste passa alla pagina di Warning.
	 *
	 * @param aIdGenProc
	 * @param aDataEmissione
	 * @return
	 * @throws Exception
	 */
	public boolean verificaEsistenzaDoc(BigDecimal aIdGenProc, Date aDataEmissione) throws Exception {

		boolean retValue = false;
		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		if (lDepDecrCtrl.ExEsisteDepositoDecretoByGenProcDataEmissione(aIdGenProc, aDataEmissione)) {
			// Controllo Decreto
			passaggioParametri();
			// Viene richiamata la pagina di Warning
			mRetPage = PG_WARNING;
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione non consentita. Per il procedimento indicato è già stato emesso un decreto nella stessa data.");
			retValue = true;
		} else {
			// Controllo Ordinanza
			IDepositoOrdinanzaPc lDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			if (lDepOrdCtrl.ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(aIdGenProc, aDataEmissione)) {
				passaggioParametri();
				// Viene richiamata la pagina di Warning
				mRetPage = PG_WARNING;
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Operazione non consentita. Per il procedimento indicato è già stata emessa una ordinanza nella stessa data.");
				retValue = true;
			}
		}
		return retValue;
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public String selezione() throws Exception {

		// Lettura contenuto
		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);

		// lettura tipo di decreto. Sempre automatico 13-9-04
		String lCodTipoDec = null;

		// switch tipo di decreto
		// Generazione automatica in base al contenuto
		Collection<?> lOggetti = DecodificheManager.getInstance().getOggettoProcedimento();
		lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo Decreto = " + lCodTipoDec);

		if (lCodTipoDec == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Tipo decreto automatico non definito");

		// MEV_2023-35 si aggiunge un nuovo codice per il decreto generico (GENERICO2=GE)
		//if (lCodTipoDec.compareTo(GENERICO) == 0 ) {
		if (lCodTipoDec.compareTo(GENERICO) == 0 || lCodTipoDec.compareTo(GENERICO2) == 0) {
			// Decreto Generico
			mRetPage = PG_INSERISCI_GENERICO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Generico " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(APPLICAZIONE_PROVVISORIA_MA) == 0) {
			// Sospensione rinvio pena diventa Applicazione Provvisoria di Misura Alternativa
			mRetPage = PG_INSERISCI_APPLICAZIONE_PROVVISORIA_MA;

			// creazione della Option StatoLibertatis
			Option lOption = new Option(DecodificheManager.getInstance().getStatoLibertatis(), "-", 75);
			setRequestAttribute("statolibertatis", "" + lOption);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sospensione rinvio pena " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(SOPRAVVENIENZA_NT) == 0) {
			// Sopravvenienza Nuovo Titolo
			mRetPage = PG_INSERISCI_SOPRAVVENIENZA_NT;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sopravvenienza Nuovo Titolo " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ESPULSIONE) == 0) {
			// Esecuzione espulsioni
			mRetPage = PG_INSERISCI_DECRETO_ESPULSIONE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Espulsione " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MODIFICA_ATT_LAVORATIVA) == 0) {
			// Esecuzione modifica attività lavorativa
			mRetPage = PG_INSERISCI_DECRETO_MODIFICA_ATT_LAVORATIVA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Modifica Attività Lavorativa " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MODIFICA_PRESCRIZIONI) == 0) {
			// Esecuzione modifica prescrizioni
			mRetPage = PG_INSERISCI_DECRETO_MODIFICA_PRESCRIZIONI;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Modifica Prescrizioni " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RICOVERI) == 0) {
			// Esecuzione decreto ricoveri
			mRetPage = PG_INSERISCI_DECRETO_RICOVERI;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricoveri " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(AUTORIZZAZIONE_CORRISPONDENZA_TELEFONICA) == 0) {
			// Esecuzione decreto Autorizzazione Corrispondenza Telefonica
			mRetPage = PG_INSERISCI_AUTORIZ_CORRIS_TELEFONICA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Autorizzazione Corrispondenza Telefonica " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RINVIO_ESECUZIONE_PENA) == 0) {
			// Esecuzione decreto Rinvio Esecuzione Pena
			mRetPage = PG_INSERISCI_RINVIO_ESECUZIONE_PENA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Rinvio Esecuzione Pena " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(SOSPENSIONE_ESECUZIONE_PENA) == 0) {
			// Esecuzione decreto Sospensione Esecuzione Pena
			mRetPage = PG_INSERISCI_SOSPENSIONE_ESECUZIONE_PENA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sospensione Esecuzione Pena " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_SOSPENSIONE_ESECUZIONE_PENA) == 0) {
			// Esecuzione decreto Revoca Sospensione Esecuzione Pena
			mRetPage = PG_INSERISCI_REVOCA_SOSPENSIONE_ESECUZIONE_PENA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Revoca Sospensione Esecuzione Pena " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_APPLICAZIONE_PROVVISORIA_MA) == 0) {
			// Esecuzione decreto Revoca Applicazione Provvisoria Misura Alternativa
			mRetPage = PG_INSERISCI_REVOCA_APPLICAZIONE_PROVVISORIA_MA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Revoca Applicazione Provvisoria Misura Alternativa " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(PERMESSO) == 0) {
			// creazione della Option StatoPermesso
			Option lOption = new Option(DecodificheManager.getInstance().getStatoPermesso(), "-", 75);
			setRequestAttribute("statoPermesso", "" + lOption);

			// Esecuzione decreto Permesso
			mRetPage = PG_INSERISCI_PERMESSO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Permesso " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RICOVERO_OPG_OSS_PSICHE) == 0) {
			// Esecuzione decreto Ricovero OPG per osservazione
			mRetPage = PG_INSERISCI_RICOVERO_OPG_OSS_PSICHE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricovero OPG per osservazione " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ICostantiDepositoOrdinanzaPc.RICOVERO_OPG) == 0) {
			// Decreto Ricovero OPG - Esiste anche l'Ordinanza
			mRetPage = ICostantiDepositoOrdinanzaPc.PG_LOAD_INSERISCI_ORDINANZA_RICOVERO_OPG;
			setRequestAttribute("Action",
					"siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Ricovero OPG " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MODIFICA_ATTIVITA_LUOGO_DET) == 0) {
			// Modifica Attivita' / Luogo Detenzione
			mRetPage = PG_INSERISCI_MODIFICAATTLUOGODET;
			// creazione della Option StatoLibertatis
			Option lOption = new Option(DecodificheManager.getInstance().getStatoLibertatis(), "-", 75);
			setRequestAttribute("statolibertatis", "" + lOption);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Modifica Attività / Luogo Detenzione " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(INOSSERVANZA_OBBLIGHI) == 0) {
			// Proposta Declaratoria esito pena
			mRetPage = PG_INSERISCI_INOSSERVANZAOBBLIGHI;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inosservanza obblighi/prescrizioni " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(PROPOSTA_DECLAR_ESITO_PROVA) == 0) {
			// Inosservanza obblighi/prescrizioni
			mRetPage = PG_INSERISCI_PROPOSTA_DECLAR_ESITO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Proposta Declaratoria esito pena " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(LICENZA) == 0) {
			// Licenza
			mRetPage = PG_INSERISCI_LICENZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Licenza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_PERMESSO) == 0) {
			// Revoca Permesso
			// Viene effettuata la Ricerca del decreto Permesso da revocare
			ricercaDecretiDaRevocare(PERMESSO, "PP");
			mRetPage = PG_INSERISCI_REVOCA_PERMESSO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Revoca Permesso " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ESCLUSIONE_COMPUTO) == 0) {
			// Escusione Scomputo
			// Viene effettuata la Ricerca del decreto Permesso da revocare
			ricercaDecretiDaRevocare(PERMESSO, "PP");
			mRetPage = PG_INSERISCI_ESCLUSIONE_COMPUTO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Escusione Scomputo " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(AUTORIZZAZIONE_MA) == 0) {
			// Autorizzazione su Misura Alternativa
			mRetPage = PG_INSERISCI_AUTORIZZAZIONE_MA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Autorizzazione su MA " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ESCLUSIONE_COMPUTO_LICENZA) == 0) {
			// Escusione Scomputo Licenza
			// Viene effettuata la Ricerca del decreto Licenza dal quale escludere computo
			// 20110524 - PM Commentato per sostituzione.
			// ricercaDecretiDaRevocare(LICENZA, "LC");
			// 20110524 - PM si considera anche LI ( Licenza per Internati )

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cod Oggetto Procedimento :"
					+ lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento());

			if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U069"))
				ricercaDecretiDaRevocare(LICENZA, "LI"); // licenza per internati
			else
				ricercaDecretiDaRevocare(LICENZA, "LC"); // licenza per internati

			mRetPage = PG_INSERISCI_ESCLUSIONE_COMPUTO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Escusione Scomputo Licenza" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_LICENZA) == 0) {
			// Revoca licenza
			// Viene effettuata la Ricerca del decreto Licenza da revocare

			if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U069"))
				ricercaDecretiDaRevocare(LICENZA, "LI"); // licenza per internati
			else
				ricercaDecretiDaRevocare(LICENZA, "LC"); // licenza per internati

			mRetPage = PG_INSERISCI_REVOCA_PERMESSO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Revoca Licenza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(LIMITAZIONI_CONTROLLI_CORRISPONDENZA) == 0) {
			// Decreto Limitazione e Controlli della corrispondenza
			mRetPage = PG_INSERISCI_LIMITAZIONE_CONTROLLI_CORRISPONDENZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Limitazione e Controlli della corrispondenza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_DECRETO) == 0) {
			// throw new SIUSException(SIUSException.USER_MESSAGE,
			// "Decreto di Revoca non ancora prevista ma in fase di rilascio.");

			// Esiste sia il Decreto che l'ordinanza. Luigi 8-11-2006
			mRetPage = ICostantiDepositoOrdinanzaPc.PG_LOAD_INSERISCI_ORDINANZA_REVOCA;
			ricercaFascicoloOrigine();
			setRequestAttribute("Action",
					"siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto di Revoca Decreto" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(AUTORIZZAZIONE_SS) == 0) {
			// Autorizzazione su Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_AUTORIZZAZIONE_SANZIONI_SOSTITUTIVE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Autorizzazione su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(DECLARATORIA_ESTINZIONE_SS) == 0) {
			// Declaratoria Estinzione Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_DECLARATORIA_ESTINZIONE_SANZIONI_SOSTITUTIVE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Declaratoria Estinzione su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(CONVOCAZIONE_DIFFIDA_SS) == 0) {
			// Convocazione/Diffida Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_CONVOCAZIONE_DIFFIDA_SANZIONI_SOSTITUTIVE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Convocazione/Diffida su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MODIFICA_PERMANENTE_SS) == 0) {
			// Modifica Permanente Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_MODIFICA_PERMANENTE_SANZIONI_SOSTITUTIVE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Modifica Permanente su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(SOSPENSIONE_ESECUZIONE_SS) == 0) {
			// Sospensione Esecuzione Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Sospensione Esecuzione su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_AUTORIZZAZIONE_SS) == 0) {
			// Revoca Autorizzazione Sanzione Sostitutiva
			mRetPage = PG_INSERISCI_REVOCA_AUTORIZZAZIONE_SANZIONE_SOSTITUTIVA;
			ricercaFascicoloOrigine();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Revoca Autorizzazione Sanzione Sostitutiva " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(AUTORIZZAZIONE_MS) == 0) {
			// Autorizzazione su Misure Sicurezza
			mRetPage = PG_INSERISCI_AUTORIZZAZIONE_MISURE_SICUREZZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Autorizzazione su Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(SOSPENSIONE_ESECUZIONE_MS) == 0) {
			// Sospensione Esecuzione Misure Sicurezza
			mRetPage = PG_INSERISCI_SOSPENSIONE_ESECUZIONE_MISURE_SICUREZZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Sospensione Esecuzione su Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MODIFICA_PRESCRIZIONI_MS) == 0) {
			// Esecuzione modifica prescrizioni Misure Sicurezza
			mRetPage = PG_INSERISCI_DECRETO_MODIFICA_PRESCRIZIONI_MS;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Modifica Prescrizioni Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(DEC_INOSSERVANZA_OBBLIGHI_MS) == 0) {
			// Diffida Misure Sicurezza
			mRetPage = PG_INSERISCI_DIFFIDA_MISURE_SICUREZZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto Diffida su Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ICostantiDepositoOrdinanzaPc.RICHIESTA_OTTEMPERANZA) == 0) {
			// Richiesta Ottemperanza
			mRetPage = PG_LOAD_INSERISCI_DECRETO_RICHIESTA_OTTEMPERANZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Richiesta Ottemperanza " + lCodTipoDec);
		}
		// AMBROS - Luglio 2014 -->
		else if (lCodTipoDec.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
			// Decreto di Revoca Liberazione Anticipata
			// Occorre attivare una Action intermedia per la ricerca del provvedimento di L.A.
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			// lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaReclamoPermesso");
			lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadInserisciDecretoRevocaLA");
			lRedirectTo.setParameter(ICostantiDepositoDecreto.CAMPO_TIPO_DECRETO_DA_PRODURRE, lCodTipoDec);
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DECRETO di Revoca Liberazione Anticipata " + lCodTipoDec);
		}
		// 10102014 - DL 92/2014 Violazione CEDU
		else if (lCodTipoDec.compareTo(ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU) == 0) {
			// Decreto Rimedi Risarcitori per Violazione ART. 3 CEDU
			mRetPage = ICostantiDepositoOrdinanzaPc.PG_LOAD_INS_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU;
			setRequestAttribute("decreto", "SI");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Decreto di Risarcimrnto Art. 3 CEDU " + lCodTipoDec);
		}
		/*
		 * ISSUE MEV : aggiunta casistica per contenuto U082 Numero MEV : 39 Autore : Gioggi Data :
		 * 08/giu/2017 Branch : MEV_39
		 */
		else if (lCodTipoDec.compareTo(RINVIO_ESECUZIONE_MS) == 0) {
			mRetPage = PG_RINVIO_ESECUZIONE_MS;
			siesLogger.debug("RINVIO ESECUZIONE MISURA SICUREZZA EX ART. 684 CPP C. 2 " + lCodTipoDec);
		}
		// ***** FINE INTERVENTO MEV_39 *****//
		// MEV_2023-35 - Revoca Autorizzazioni Pena Sostitutiva
        else if (lCodTipoDec.compareTo(REVOCA_AUTORIZZAZIONE_PS) == 0) {
          // Revoca Autorizzazione Pena Sostitutiva
          mRetPage = PG_INSERISCI_REVOCA_AUTORIZZAZIONE_PENA_SOSTITUTIVA; 
          ricercaFascicoloOrigine();
          siesLogger.debug("Decreto Revoca Autorizzazione Pena Sostitutiva " + lCodTipoDec);
        }
		// MEV_2023-35 - FINE
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Decreto non previsto per il contenuto indicato");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("cod Tipo decreto: " + lCodTipoDec);

		return lCodTipoDec;
	}

	/**
	 *
	 * @param aTipoDecreto
	 * @param aTipoLicenza
	 * @throws Exception
	 */
	private void ricercaDecretiDaRevocare(String aTipoDecreto, String aTipoLicenza) throws Exception {

		if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null
				&& lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto() != null) {
			RicercaProvvCollegati lRicerca = new RicercaProvvCollegati(getCodUfficioUtenteConnesso());
			Vector<?> lDecreti = lRicerca.ricercaDecretiLicenzeDaSog(
					lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(), aTipoDecreto, aTipoLicenza);
			if (lDecreti != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("N.ro dei decreti da revocare trovati: " + lDecreti.size());

				if (lDecreti.size() > 0)
					setRequestAttribute("decreti", lDecreti);
			}
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Manca l'ID del Soggetto");
		return;
	}

	/**
	 *
	 * @throws Exception
	 */
	public void ricercaFascicoloOrigine() throws Exception {

		// Si preleva dalla sessione il fascicolo GPModel.
		if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			if (lFasGPMod.getFascicoloSiusModel() != null
					&& lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
				BigDecimal lIdFascicoloOrigine = lFasGPMod.getFascicoloSiusModel()
						.getIdFascicoloSiusOrigine();
				FascicoloGPModel lFasGPModOrigine = new FascicoloGPModel();
				lFasGPModOrigine.getFascicoloSiusModel().setIdFascicoloSius(lIdFascicoloOrigine);
				IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPModOrigine = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloOrigine);
				setRequestAttribute("fascicolo_origine", lFasGPModOrigine);
			}
		}
	}

}