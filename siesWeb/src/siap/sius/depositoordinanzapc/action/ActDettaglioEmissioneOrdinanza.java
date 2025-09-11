package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActLoadDettaglioEmissioneOrdinanza - Classe Action per la load dettaglio di DepositoOrdinanzaPc
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioEmissioneOrdinanza extends ActionSius
		implements ICostantiDepositoOrdinanzaPc, ICostantiTemplate {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	FascicoloGPModel mFasGPMod = null;
	BigDecimal mIdEvento = null;
	protected OrdinanzaEventoTenoriPrescrizioniModel mOrdEveTenPreMod = null;
	// Interfaccia al Controller di DepositoOrdinanzaPc
	IDepositoOrdinanzaPc mDepOrdCtrl = null;

	public String processRequest() throws Exception {

		if (!(this instanceof siap.sius.depositoordinanzapc.action.ActLoadModificaOrdinanza))
			this.setLinkRitorno();

		// Preleva il fascicolo dalla sessione.
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Manca Fascicolo in sessione");

		// FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		mFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati: manca CAMPO_ID_EVENTO");

		mIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Ricerca i dati relativi all'Ordinanza
		mOrdEveTenPreMod = ricercaOrdinanza(mIdEvento);

		// Se l'Ordinanza è revocata si cercano i dati relativi all?Ordinanza di Revoca
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getEvento() != null
				&& mOrdEveTenPreMod.getEvento().getEveIdEventoRevoca() != null) {
			OrdinanzaEventoTenoriPrescrizioniModel lOrdinanzaDiRevoca = ricercaOrdinanza(
					mOrdEveTenPreMod.getEvento().getEveIdEventoRevoca());
			if (lOrdinanzaDiRevoca != null) {
				// L'Ordinanza di Revoca viene passata nella request
				setRequestAttribute("OrdinanzaDiRevoca", lOrdinanzaDiRevoca);
				// Ricerca del Fascicolo relativo all'Ordinanza di revoca
				if (lOrdinanzaDiRevoca.getEvento() != null
						&& lOrdinanzaDiRevoca.getEvento().getFasSiuIdFascicoloSius() != null) {
					FascicoloGPModel lFascicoloSius = ricercaFascicoloSIUS(
							lOrdinanzaDiRevoca.getEvento().getFasSiuIdFascicoloSius());
					// Fascicolo SIUS dell'Ordinanza di Revoca viene passata nella request
					setRequestAttribute("FascicoloDiRevoca", lFascicoloSius);
				}
			}
		}

		// Ricerca del Magistrato Relatore
		// 20131130 - si leggerà il magistrato dall'evento
		// IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		/*
		 * MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(
		 * mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		 */

		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagModel = lMagCtrl.ExRicercaMagistratoByEvento(mIdEvento);
		// 20131130 - Per "uniformità" si istanzia e si popola un magistratoRelatoreModel
		MagistratoRelatoreModel lMagRel = new MagistratoRelatoreModel();
		lMagRel.setMagistrato(lMagModel);
		setRequestAttribute("magistratorelatore", lMagRel);

		// 20131130 -
		String lCodTipoProvvedimento = mOrdEveTenPreMod.getEvento().getCodTipoProvvedimento();

		setRequestAttribute("CodTipoProvvedimento", lCodTipoProvvedimento);

		// Imposta gli oggetti nella request.
		setRequestAttribute("IdEvento", mIdEvento);
		gestioneTemplate(mIdEvento);

		// Valorizzazione eventuale bottone di ritorno
		if (!this.isRequestParameterNullObj("acdest")) {
			setRequestAttribute("acdest", getRequestStringParameter("acdest"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getRequestStringParameter("acdest"));
		}

		ModificabileStampabile();
		// Imposta l'oggetto nella request.
		setRequestAttribute("datiOrdinanza", mOrdEveTenPreMod);

		// Vengono passate le prescrizioni nella request separatamente solo se presenti.
		if (mOrdEveTenPreMod.getPrescrizioni().length > 0)
			passaPrescrizioniSeparatamente();

		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione().trim().length() > 0) {
			// 29/03/2007 Nel caso di Sospensione Esecutiva Ordinanza occorre valorizzare il CodiceTipoUfficio
			// (TdS o UdS).
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficio = lUffCtrl
					.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione());

			if (lUfficio != null)
				setRequestAttribute("ufficioConcessoRiduzione", lUfficio);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Effettuata ricerca Ufficio Concesso Riduzione");
		}

		// Nel caso di Ordinanza di Rimessione Atti occorre caricare il vettore delle notifiche
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RIMESSIONE_ATTI) == 0) {
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(mIdEvento);
			setRequestAttribute("notifiche", lVect);
		}
		// Nel caso di Ordinanza di Applicazione Misure Sicurezza occorre caricare il vettore delle misure
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(MISURA_SICUREZZA) == 0) {
			// INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			// Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento (mIdEvento);
			// setRequestAttribute("notifiche", lVect );
			IMisuraSicurezza lCtrlMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
			aMisuraSicurezza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			Vector lVectMisure = lCtrlMis.ExRicercaMisuraSicurezza(aMisuraSicurezza);
			setRequestAttribute("misuresicurezza", lVectMisure);

			if (lVectMisure.size() > 0) {
				setRequestAttribute("misuraSicurezza", (lVectMisure.get(0)));
			} else {
				setRequestAttribute("misuraSicurezza", null);
			}
		}

		int lSize = mOrdEveTenPreMod.getTenori().length;
		String unificazione = "";
		for (int x = 0; x < lSize; x++) {
			// verifico se si tratta di un oggetto
			// "Unificazione delle misure di sicurezza (art. 209 C.P.)" (2442),
			if (mOrdEveTenPreMod.getTenori()[x].getCodOggettoTenore().equals("2442")) {
				unificazione = "SI";
			}
		}
		// **************************** FINE

		// Nel caso di Ordinanza di Esecuzione Misure Sicurezza con Trasformazione occorre caricare le misure
		// prima e dopo la trasformazione
		if (mOrdEveTenPreMod != null
				&& mOrdEveTenPreMod.getOrdinanza() != null && mOrdEveTenPreMod.getOrdinanza()
						.getCodTipoOrdinanza().compareTo(TRASFORMA_MISURA_SICUREZZA) == 0
				&& unificazione.equals("")) {
			IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
			EsecuzioneMisuraSicurezzaModel aEMSOldMod = new EsecuzioneMisuraSicurezzaModel();
			EsecuzioneMisuraSicurezzaModel aEMSNewMod = new EsecuzioneMisuraSicurezzaModel();
			FascicoloGPModel lFascicoloPadreEMS = null;

			if (mFasSiusCtrl == null)
				mFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFascicoloPadreEMS = mFasSiusCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(
					mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1(),
					mFasGPMod.getGeneraleProcedimentoModel().getProgrS1(),
					mFasGPMod.getGeneraleProcedimentoModel().getCodUfficioInserimento());

			// aEMSOldMod =
			// lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			if (lFascicoloPadreEMS != null && lFascicoloPadreEMS.getFascicoloSiusModel() != null
					&& lFascicoloPadreEMS.getFascicoloSiusModel().getIdFascicoloSius() != null)
				aEMSOldMod = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
						lFascicoloPadreEMS.getFascicoloSiusModel().getIdFascicoloSius());
			aEMSNewMod = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdOrdinanza(
					mOrdEveTenPreMod.getOrdinanza().getIdDepositoOrdinanzaPc());

			setRequestAttribute("precedenteMisuraSicurezza", aEMSOldMod);
			setRequestAttribute("attualeMisuraSicurezza", aEMSNewMod);
		}

		// Nel caso di Ordinanza di Esecuzione Misure Sicurezza con Trasformazione occorre caricare
		// le misure rideterminate a seguito unificazione
		if (mOrdEveTenPreMod != null
				&& mOrdEveTenPreMod.getOrdinanza() != null && mOrdEveTenPreMod.getOrdinanza()
						.getCodTipoOrdinanza().compareTo(TRASFORMA_MISURA_SICUREZZA) == 0
				&& unificazione.equals("SI")) {

			// Misure di Sicurezza Rideterminate a seguito Unificazione
			IEsecuzioneMS lCtrlEMS = SIUSLookupRemote.getEsecuzioneMSRemote();
			// EsecuzioneMisuraSicurezzaModel aEMSRideterminate = new EsecuzioneMisuraSicurezzaModel();

			Vector lVectMisureSicRid = lCtrlEMS.ExRicercaEsecuzioneMisureSicRidByIdOrdinanza(
					mOrdEveTenPreMod.getOrdinanza().getIdDepositoOrdinanzaPc());

			if (lVectMisureSicRid.size() > 0) {
				setRequestAttribute("misureSicurezzaRideterminate", lVectMisureSicRid);
			} else {
				setRequestAttribute("misureSicurezzaRideterminate", null);
			}

			// Misure di Sicurezza Inserite dall'UDS
			IMisuraSicurezza lCtrlMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
			aMisuraSicurezza.setFasSiuIdFascicoloSius(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			Vector lVectMisure = lCtrlMis.ExRicercaMisuraSicurezza(aMisuraSicurezza);
			setRequestAttribute("misuresicurezza", lVectMisure);

			// Misura di Sicurezza in Esecuzione
			EsecuzioneMisuraSicurezzaModel aEMSOldMod = new EsecuzioneMisuraSicurezzaModel();
			// EsecuzioneMisuraSicurezzaModel aEMSNewMod = new EsecuzioneMisuraSicurezzaModel();
			FascicoloGPModel lFascicoloPadreEMS = null;

			if (mFasSiusCtrl == null)
				mFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFascicoloPadreEMS = mFasSiusCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(
					mFasGPMod.getGeneraleProcedimentoModel().getAnnoS1(),
					mFasGPMod.getGeneraleProcedimentoModel().getProgrS1(),
					mFasGPMod.getGeneraleProcedimentoModel().getCodUfficioInserimento());

			if (lFascicoloPadreEMS != null && lFascicoloPadreEMS.getFascicoloSiusModel() != null
					&& lFascicoloPadreEMS.getFascicoloSiusModel().getIdFascicoloSius() != null)
				aEMSOldMod = lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
						lFascicoloPadreEMS.getFascicoloSiusModel().getIdFascicoloSius());
			// aEMSNewMod =
			// lCtrlEMS.ExRicercaEsecuzioneMisuraSicurezzaByIdOrdinanza(mOrdEveTenPreMod.getOrdinanza().getIdDepositoOrdinanzaPc());

			setRequestAttribute("precedenteMisuraSicurezza", aEMSOldMod);
			setRequestAttribute("attualeMisuraSicurezza", null);
		}

		if (mOrdEveTenPreMod.getEvento() != null && mOrdEveTenPreMod.getEvento().getCodMotivo() != null
				&& (mOrdEveTenPreMod.getEvento().getCodMotivo().equals("2410")
						|| mOrdEveTenPreMod.getEvento().getCodMotivo().equals("2411")
						|| mOrdEveTenPreMod.getEvento().getCodMotivo().equals("2412"))) {
			// Sospensione Esecuzione Misura Sicurezza
			gestioneTemplate(mOrdEveTenPreMod.getEvento().getIdEvento());
			ricercaPeriodoAltraMisura();
			// return PG_DETTAGLIO_ORD_SOSPENSIONE_EMS;
		}

		// INIZIO: MEV_9 (D.lgs. 123/2018)
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& MISURA_ALTERNATIVA_AMMISSIONE_PROVVISORIA
						.equals(mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza())) {
			// Nuovo caricamento combo Template
			siesLogger.debug("Ordinanza di ammissione provvisoria filtro i template");

			TemplateModel tempaletRicerca = new TemplateModel();
			tempaletRicerca.setCodTipoEvento(mOrdEveTenPreMod.getEvento().getCodTipoEvento());
			tempaletRicerca.setCodTipoProvvedimento(mOrdEveTenPreMod.getEvento().getCodTipoProvvedimento());
			tempaletRicerca.setCodMotivo(mOrdEveTenPreMod.getEvento().getCodMotivo());
			tempaletRicerca.setCodOggettoProcedimento(
					mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
			tempaletRicerca.setFlagTemplate("1"); // presente solo per le provvisorie che hanno gli stessi
													// codice dalle ordinarie

			Option lOptTemplate = null;
			lOptTemplate = UtilTemplate.listaCbxTemplate(tempaletRicerca);
			setRequestAttribute(CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
		}
		// FINE: MEV_9

		ricercaFascicoloOrigine();

		return PG_LOAD_DETTAGLIO_EMISSIONE_ORDINANZA;
	}

	// Funzione per la costruzione della combo con i template di stampa previsti
	private void gestioneTemplate(BigDecimal alIdEvento) throws Exception {

		Option lOptTemplate = null;
		lOptTemplate = UtilTemplate.listaCbxTemplate(alIdEvento);
		setRequestAttribute(CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate -> " + lOptTemplate);

		// template di default
		String[] lSelected = lOptTemplate.getSelecteds();
		if (lSelected != null && lSelected.length > 0) {
			setRequestAttribute(CAMPO_DEFAULT_TEMPLATE, lSelected[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("TemplateDiDefault -> " + lSelected[0]);
		}
		return;
	}

	// Ricerca Periodo Altra Misura
	private void ricercaPeriodoAltraMisura() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaPeriodoAltraMisura: inizio");

		// Ricerca il Periodo Altra Misura tramite l'ID dell'evento
		IPeriodoAltraMisura lCtrlPAM = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		PeriodoAltraMisuraModel mPerAlMisu = lCtrlPAM
				.ExRicercaMisuraSicurezzaByIdEvento(mOrdEveTenPreMod.getEvento().getIdEvento());

		// Passa i dati trovati del Periodo Altra Misura alla JSP
		setRequestAttribute("PeriodoAltraMisura", mPerAlMisu);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaPeriodoAltraMisura: fine");
	}

	private void ModificabileStampabile() throws Exception {

		String lStampabile = "NO";
		String lModificabile = "NO";
		String lProvModifica = "NO";

		if (IsFascicoloSiusModificabile()) {
			if (mOrdEveTenPreMod.getEvento().getFlagDocumentoRegistrato() == null
					|| mOrdEveTenPreMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
				lStampabile = "SI";
				// Se depositato non può essere cancellato
				if (mOrdEveTenPreMod.getOrdinanza().getAnnoS3() == null
						&& mOrdEveTenPreMod.getOrdinanza().getNumS3() == null)
					lModificabile = "SI";
				else
					lModificabile = "NO";
			}
			// Valutazione abilitazione Modififica Provvedimento
			/*
			 * Le condizioni per abilitare la modifica dell'ordinanza sono: il Procedimento SIUS è
			 * modificabile; (modificabile) provv. validato; (lProv.getFlagDocumentoRegistrato()!=null &&
			 * lProv.getFlagDocumentoRegistrato().compareTo("S")==0) provv. depositato ma con deposito non
			 * validato; (isDepositato.equalsIgnoreCase("NO") && lProv.getDataDeposito() != null) provv. non
			 * appartiene ad uno dei seguenti tipi: Unificazione (cod. Esito = 0600), Fissazione Udienza (cod.
			 * Esito = 0601), Irreperibilità (cod. Esito = 0602), Rinvio Udienza (cod. Esito = 0603);
			 */
			if (mOrdEveTenPreMod.getEvento().getFlagDocumentoRegistrato() != null
					&& mOrdEveTenPreMod.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
				// depositato ma non validato
				if (mOrdEveTenPreMod.getEvento().getNumAllegati() == 1
						&& mOrdEveTenPreMod.getOrdinanza().getDataDeposito() != null) {
					lProvModifica = "NO";

					// Inserire i controlli sul Tipo

				}

			}

		}
		// Modificabile coincide con stampabile !!
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("AbilitaModifica", lProvModifica);

	}

	// Interfaccia al Controller del Fascicolo SIUS
	IFascicoloSius mFasSiusCtrl = null;

	private void ricercaFascicoloOrigine() throws Exception {

		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null
				&& mFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
			BigDecimal lIdFascicoloOrigine = mFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();

			FascicoloGPModel lFasGPModOrigine = new FascicoloGPModel();

			lFasGPModOrigine = ricercaFascicoloSIUS(lIdFascicoloOrigine);
			setRequestAttribute("fascicolo_origine", lFasGPModOrigine);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Effettuata ricerca Fascicolo Origine");
		}
	}

	private FascicoloGPModel ricercaFascicoloSIUS(BigDecimal aIdFascicoloSius) throws Exception {

		FascicoloGPModel lFascicolo = null;

		if (mFasSiusCtrl == null)
			mFasSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lFascicolo = mFasSiusCtrl.ExRicercaFascicoloByKey(aIdFascicoloSius);
		return lFascicolo;
	}

	/**
	 * Funzione di ricerca dei dati relativi all'Ordinanza.
	 *
	 * @param lIdEvento
	 * @throws Exception
	 */
	public OrdinanzaEventoTenoriPrescrizioniModel ricercaOrdinanza(BigDecimal aIdEvento) throws Exception {

		// Lettura dell'Ordinanza di Revoca.
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca Ordinanza");
		OrdinanzaEventoTenoriPrescrizioniModel lOrdEveTenPreMod = null;

		if (mDepOrdCtrl == null)
			mDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();

		if (aIdEvento != null) {
			lOrdEveTenPreMod = mDepOrdCtrl.ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(aIdEvento);

			if (lOrdEveTenPreMod == null
					|| lOrdEveTenPreMod.getOrdinanza().getIdDepositoOrdinanzaPc() == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Ordinanza non trovata !");
		}
		return lOrdEveTenPreMod;
	}

	private void passaPrescrizioniSeparatamente() {

		Vector lPrescrizioni = null;

		if (mOrdEveTenPreMod.getPrescrizioni().length > 0) {
			lPrescrizioni = new Vector();
			for (int i = 0; i < mOrdEveTenPreMod.getPrescrizioni().length; i++) {
				lPrescrizioni.add(mOrdEveTenPreMod.getPrescrizioni()[i]);
			}
			setRequestAttribute("prescrizioni", lPrescrizioni);
		}
	}

}