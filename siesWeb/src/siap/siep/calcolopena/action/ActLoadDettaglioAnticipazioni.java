package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 *
 * <p>
 * Title: ActLoadDettaglioAnticipazioni
 * </p>
 * <p>
 * Description: Dettaglio Anticipazioni classe padre di varie richieste
 * ActLoadDettaglioAnnotazioniAnticipazioniIncost ActLoadDettaglioAnnotazioniAnticipazioniDepen
 * ActLoadDettaglioAnnotazioniAnticipazioniAmnistia
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ActLoadDettaglioAnticipazioni extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Load del Dettaglio delle richieste di anticipazione incosittuzionalità
	 * 
	 * @param lMotivoProvvedimento
	 * @param lTipoAnnotazione
	 * @param lFlagPage
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String loadDettaglio(String lMotivoProvvedimento, String lTipoAnnotazione, String lFlagPage)
			throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Posizione Giuridica
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);
		setRequestAttribute("MotivoProvvedimento", lMotivoProvvedimento);

		// Annotazione Manuale
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
		AnnotazioneManualeModel lAnnManIns = null;
		Vector lListAnnMan = new Vector();

		// Nel caso in cui provengo dal dettaglio e ho l'id Annotazione Manuale
		if (!isRequestParameterNullObj(CAMPO_ID_ANNOTAZIONE_MANUALE)) { // Cerco per Id Annotazione Manuale
			BigDecimal lIdAnn = getRequestBigDecimalParameter(CAMPO_ID_ANNOTAZIONE_MANUALE);
			lAnnManIns = lCtrlAnnMan.ExRicercaAnnotazioneManualeByKey(lIdAnn);
			lListAnnMan.add(lAnnManIns);
		} else {
			// Cerco le Annotazioni MAnuali
			lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);
			lAnnMod.setCodTipoAnnotazione(lTipoAnnotazione);
			lAnnMod.setFlagAppProvvisoria("RICHIESTE");
			// Cerco le annotazioni manuali non validate
			lAnnMod.setFlagValidato("N");
			try {
				lListAnnMan = lCtrlAnnMan.ExRicercaAnnotazioneManualeGenerico(lAnnMod);
			} catch (F3BException ex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(getClass().getName() + " Nessun elemento Trovato");
			}

			if (lListAnnMan != null && lListAnnMan.size() > 0) { // Setto l'ultima annotazione manuale trovata
				lAnnManIns = (AnnotazioneManualeModel) lListAnnMan.get((lListAnnMan.size() - 1));
			} else
				// Non esistono ANNotazioni devo inserire un nuovo Provv
				throw new SIEPException(SIEPException.EX_NOT_FOUND, "Nessun elememento trovato.");
		}

		setRequestAttribute("ListaAnnotazioni", lListAnnMan);
		setRequestAttribute("AnnotazioneManualeInserita", lAnnManIns);

		// Passo alla maschera di dettaglio la pena residua in modo che possa decidere
		// se visualizzare o meno i campi com la data di scarcerazione.
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenRes = new PenaResiduaModel();
		lUltimaPenRes = IPenRes.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);

		setRequestAttribute("UltimaPenRes", lUltimaPenRes);
		setRequestAttribute("lFlagPage", "GE");
		setRequestAttribute("lFlagRich", lFlagPage);

		// Nel caso ritorna null, tutti i controlli sono passati
		return null;
	}

	/**
	 * checkCalcoloPena
	 * 
	 * @return
	 * @throws F3BException
	 */
	protected String checkCalcoloPena() throws F3BException {

		String lMessage = "";

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// verifico se calcolo abInizio
		ICalcoloPena lCalPen = SIEPLookupRemote.getCalcoloPenaRemote();
		boolean lIsCalcoloPenaAbInitio = lCalPen.ExIsCalcoloPenaAbInizio(lIdFascicolo);

		// Recupero la posizione giuridica
		IPosizioneGiuridica lPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel PGMod = new PosizioneGiuridicaModel();
		PGMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		PosizioneGiuridicaModel lPosizione = lPG.ExRicercaPosizioneGiuridicaCorrente(PGMod);

		// Verifico se esiste una pena residua validata
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenRes = new PenaResiduaModel();
		lUltimaPenRes = IPenRes.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);

		// Verifico se Migrato Res
		if (!lIsCalcoloPenaAbInitio && lPosizione.isLibero() && lUltimaPenRes == null) {
			lMessage = "Attenzione! Eventuali calcoli della pena partiranno dai dell'ultima pena calcolata. Si consiglia di aggiornare il calcolo prima di procedere (Assegnazione/Calcolo Pena).";
		}

		return lMessage;
	}

}