package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.util.UtilTemplate;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioOrdinanza_AmmProvv_AffiInProva_ServSoc
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di DepositoOrdinanzaPc
 * </p>
 * <p>
 * in caso di Ordinanza di Affidamento in aprova al sevizio sociale (ammissione provvisoria)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActDettaglioOrdinanza_AmmProvv_AffiInProva_ServSoc extends ActionSius implements
		ICostantiDepositoOrdinanzaPc, ICostantiTemplate {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	FascicoloGPModel mFasGPMod = null;
	BigDecimal mIdEvento = null;
	protected OrdinanzaEventoTenoriPrescrizioniModel mOrdEveTenPreMod = null;
	// Interfaccia al Controller di DepositoOrdinanzaPc
	IDepositoOrdinanzaPc mDepOrdCtrl = null;

	public String processRequest() throws Exception {

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
			OrdinanzaEventoTenoriPrescrizioniModel lOrdinanzaDiRevoca = ricercaOrdinanza(mOrdEveTenPreMod
					.getEvento().getEveIdEventoRevoca());
			if (lOrdinanzaDiRevoca != null) {
				// L'Ordinanza di Revoca viene passata nella request
				setRequestAttribute("OrdinanzaDiRevoca", lOrdinanzaDiRevoca);
				// Ricerca del Fascicolo relativo all'Ordinanza di revoca
				if (lOrdinanzaDiRevoca.getEvento() != null
						&& lOrdinanzaDiRevoca.getEvento().getFasSiuIdFascicoloSius() != null) {
					FascicoloGPModel lFascicoloSius = ricercaFascicoloSIUS(lOrdinanzaDiRevoca.getEvento()
							.getFasSiuIdFascicoloSius());
					// Fascicolo SIUS dell'Ordinanza di Revoca viene passata nella request
					setRequestAttribute("FascicoloDiRevoca", lFascicoloSius);
				}
			}
		}

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(mFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);

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
			// 01/2014 in caso di Ammissione provvisoria di misura alt. è valorizzato il CodiceTipoUfficio
			// (TdS).
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficioTDS = lUffCtrl.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza()
					.getCodUffTdsConcessoRiduzione());

			if (lUfficioTDS != null)
				setRequestAttribute("ufficioTDS", lUfficioTDS);
		}

		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante() != null
				&& mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante().trim().length() > 0) {
			// 01/2014 in caso di Ammissione provvisoria di misura alt. è valorizzato il CodiceTipoUfficio
			// (PM).
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficioP = lUffCtrl.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza()
					.getAutoritaVigilante());

			if (lUfficioP != null)
				setRequestAttribute("ufficioProcura", lUfficioP);
		}

		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 0) {
			// 01/2014 in caso di Ammissione provvisoria di misura alt. è valorizzato il CodiceTipoUfficio
			// (PM).
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficioUDS = lUffCtrl.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza()
					.getCodUfficioMagistratoComp());

			if (lUfficioUDS != null)
				setRequestAttribute("ufficioUDS", lUfficioUDS);
		}

		// ricercaFascicoloOrigine();

		return PG_DETT_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA;
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
//	private void ricercaPeriodoAltraMisura() throws Exception {
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ricercaPeriodoAltraMisura: inizio");
//
//		// Ricerca il Periodo Altra Misura tramite l'ID dell'evento
//		IPeriodoAltraMisura lCtrlPAM = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
//		PeriodoAltraMisuraModel mPerAlMisu = lCtrlPAM.ExRicercaMisuraSicurezzaByIdEvento(mOrdEveTenPreMod
//				.getEvento().getIdEvento());
//
//		// Passa i dati trovati del Periodo Altra Misura alla JSP
//		setRequestAttribute("PeriodoAltraMisura", mPerAlMisu);
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ricercaPeriodoAltraMisura: fine");
//	}

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

//	private void ricercaFascicoloOrigine() throws Exception {
//		if (mFasGPMod != null && mFasGPMod.getFascicoloSiusModel() != null
//				&& mFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
//			BigDecimal lIdFascicoloOrigine = mFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
//
//			FascicoloGPModel lFasGPModOrigine = new FascicoloGPModel();
//
//			lFasGPModOrigine = ricercaFascicoloSIUS(lIdFascicoloOrigine);
//			setRequestAttribute("fascicolo_origine", lFasGPModOrigine);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//			// LogF3B.getLogger()
//			siesLogger.debug("Effettuata ricerca Fascicolo Origine");
//		}
//	}

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void passaPrescrizioniSeparatamente() {
		Vector lPrescrizioni = null;

		if (mOrdEveTenPreMod.getPrescrizioni().length > 0) {
			lPrescrizioni = new Vector();
			for (int i = 0; i < mOrdEveTenPreMod.getPrescrizioni().length; i++) {
				lPrescrizioni.add((PrescrizioneModel) mOrdEveTenPreMod.getPrescrizioni()[i]);
			}
			setRequestAttribute("prescrizioni", lPrescrizioni);
		}
	}

}