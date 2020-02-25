package siap.sius.depositosentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDettaglioEmissioneSentenza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di DepositoSentenza
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioEmissioneSentenza extends ActionSius
		implements ICostantiDepositoSentenza, ICostantiTemplate {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	FascicoloGPModel mFasGPMod = null;
	BigDecimal mIdEvento = null;
	protected SentenzaEventoTenoriPrescrizioniModel mSenEveTenPreMod = null;

	// Interfaccia al Controller di DepositoSentenza
	IDepositoSentenza mDepSenCtrl = null;

	public String processRequest() throws Exception {

		if (!(this instanceof siap.sius.depositosentenza.action.ActLoadModificaSentenza))
			this.setLinkRitorno();

		// Preleva il fascicolo dalla sessione.
		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Manca Fascicolo in sessione");

		mFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati: manca CAMPO_ID_EVENTO");

		mIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Ricerca i dati relativi alla Sentenza
		mSenEveTenPreMod = ricercaSentenza(mIdEvento);

		// Se la Sentenza è revocata si cercano i dati relativi alla Sentenza di Revoca
		if (mSenEveTenPreMod != null && mSenEveTenPreMod.getEvento() != null
				&& mSenEveTenPreMod.getEvento().getEveIdEventoRevoca() != null) {
			SentenzaEventoTenoriPrescrizioniModel lSentenzaDiRevoca = ricercaSentenza(
					mSenEveTenPreMod.getEvento().getEveIdEventoRevoca());
			if (lSentenzaDiRevoca != null) {
				// La Sentenza di Revoca viene passata nella request
				setRequestAttribute("SentenzaDiRevoca", lSentenzaDiRevoca);
				// Ricerca del Fascicolo relativo alla Sentenza di revoca
				if (lSentenzaDiRevoca.getEvento() != null
						&& lSentenzaDiRevoca.getEvento().getFasSiuIdFascicoloSius() != null) {
					FascicoloGPModel lFascicoloSius = ricercaFascicoloSIUS(
							lSentenzaDiRevoca.getEvento().getFasSiuIdFascicoloSius());
					// Fascicolo SIUS della Sentenza di Revoca viene passata nella request
					setRequestAttribute("FascicoloDiRevoca", lFascicoloSius);
				}
			}
		}

		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagModel = lMagCtrl.ExRicercaMagistratoByEvento(mIdEvento);
		// 20131130 - Per "uniformità" si istanzia e si popola un magistratoRelatoreModel
		MagistratoRelatoreModel lMagRel = new MagistratoRelatoreModel();
		lMagRel.setMagistrato(lMagModel);
		setRequestAttribute("magistratorelatore", lMagRel);

		String lCodTipoProvvedimento = mSenEveTenPreMod.getEvento().getCodTipoProvvedimento();

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
		setRequestAttribute("datiSentenza", mSenEveTenPreMod);

		// Nel caso di Sentenza di Rimessione Atti occorre caricare il vettore delle notifiche
		if (mSenEveTenPreMod != null && mSenEveTenPreMod.getSentenza() != null) {// &&
			// mSenEveTenPreMod.getSentenza().getCodTipoSentenza().compareTo(RIMESSIONE_ATTI) ==0 ) {
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(mIdEvento);
			setRequestAttribute("notifiche", lVect);
		}

		ricercaFascicoloOrigine();

		return PG_LOAD_DETTAGLIO_EMISSIONE_SENTENZA;
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
	/*
	 * private void ricercaPeriodoAltraMisura() throws Exception { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo
	 * la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug("ricercaPeriodoAltraMisura: inizio");
	 * 
	 * // Ricerca il Periodo Altra Misura tramite l'ID dell'evento IPeriodoAltraMisura lCtrlPAM =
	 * SIUSLookupRemote.getPeriodoAltraMisuraRemote(); PeriodoAltraMisuraModel mPerAlMisu =
	 * lCtrlPAM.ExRicercaMisuraSicurezzaByIdEvento(mSenEveTenPreMod.getEvento().getIdEvento());
	 * 
	 * // Passa i dati trovati del Periodo Altra Misura alla JSP setRequestAttribute("PeriodoAltraMisura",
	 * mPerAlMisu);
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug("ricercaPeriodoAltraMisura: fine"); }
	 */

	private void ModificabileStampabile() throws Exception {
		String lStampabile = "NO";
		String lModificabile = "NO";
		String lProvModifica = "NO";

		if (IsFascicoloSiusModificabile()) {
			if (mSenEveTenPreMod.getEvento().getFlagDocumentoRegistrato() == null
					|| mSenEveTenPreMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
				lStampabile = "SI";

				// Se depositato non può essere cancellato
				if (mSenEveTenPreMod.getSentenza().getAnnoSentenza() == null
						&& mSenEveTenPreMod.getSentenza().getNumSentenza() == null)
					lModificabile = "SI";
				else
					lModificabile = "NO";
			}
			// TODO completare
			// Valutazione abilitazione Modififica Provvedimento
			/*
			 * Le condizioni per abilitare la modifica dell'ordinanza sono: il Procedimento SIUS è
			 * modificabile; (modificabile) provv. validato; (lProv.getFlagDocumentoRegistrato()!=null &&
			 * lProv.getFlagDocumentoRegistrato().compareTo("S")==0) provv. depositato ma con deposito non
			 * validato; (isDepositato.equalsIgnoreCase("NO") && lProv.getDataDeposito() != null) provv. non
			 * appartiene ad uno dei seguenti tipi: Unificazione (cod. Esito = 0600), Fissazione Udienza (cod.
			 * Esito = 0601), Irreperibilità (cod. Esito = 0602), Rinvio Udienza (cod. Esito = 0603);
			 */
			if (mSenEveTenPreMod.getEvento().getFlagDocumentoRegistrato() != null
					&& mSenEveTenPreMod.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
				// depositato ma non validato
				if (mSenEveTenPreMod.getEvento().getNumAllegati() == 1
						&& mSenEveTenPreMod.getSentenza().getDataDeposito() != null) {
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
	 * Funzione di ricerca dei dati relativi alla Sentenza.
	 * 
	 * @param lIdEvento
	 * @throws Exception
	 */
	public SentenzaEventoTenoriPrescrizioniModel ricercaSentenza(BigDecimal aIdEvento) throws Exception {
		// Lettura della Sentenza.
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca Sentenza");
		SentenzaEventoTenoriPrescrizioniModel lSenEveTenPreMod = null;

		if (mDepSenCtrl == null)
			mDepSenCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();

		if (aIdEvento != null) {
			lSenEveTenPreMod = mDepSenCtrl.ExRicercaSentenzaEventoTenoriPrescrizioniByIdEvento(aIdEvento);

			if (lSenEveTenPreMod == null || lSenEveTenPreMod.getSentenza().getIdDepositoSentenza() == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Sentenza non trovata !");
		}
		return lSenEveTenPreMod;
	}

}