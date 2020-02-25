package siap.sius.impugnazione.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciOpposizione
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento dell'Opposizione
 * </p>
 * <p>
 * Company: Intersistemi
 * </p>
 * 
 * @since 06/2014
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOpposizione extends ActionSiap implements ICostantiImpugnazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Opposizione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Recupero il Fascicolo SIUS dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dalla request l'IdEvento.
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Recupera i dati dell'Opposizione
		ImpugnazioneModel lImpMod = new ImpugnazioneModel();

		// Il campo Progr_S7 viene impostato nel controller.
		lImpMod.setAnnoS7(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lImpMod.setCodTipoImpugnazione(getRequestStringParameter(CAMPO_COD_TIPO_IMPUGNAZIONE));
		lImpMod.setSoggettoImpugnante(getRequestStringParameter(CAMPO_SOGGETTO_IMPUGNANTE));
		lImpMod.setDataRicorso(getRequestDateParameter(CAMPO_ANNO_DATA_RICORSO, CAMPO_MESE_DATA_RICORSO,
				CAMPO_GIORNO_DATA_RICORSO));
		lImpMod.setDataArrivoCancelleria(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,
				CAMPO_MESE_DATA_ARRIVO_CANCELLERIA, CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA));
		lImpMod.setDataTrasmissioneAtti(null); // non prevista per OPPOSIZIONE
		lImpMod.setCodAutoritaDestinataria("-"); // non prevista per OPPOSIZIONE messo a trattino per le JOIN
		lImpMod.setDataDecisione(getRequestDateParameter(CAMPO_ANNO_DATA_DECISIONE,
				CAMPO_MESE_DATA_DECISIONE, CAMPO_GIORNO_DATA_DECISIONE));
		lImpMod.setCodTenoreDecisione(getRequestStringParameter(CAMPO_COD_TENORE_DECISIONE));
		lImpMod.setDataRestituzioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE_ATTI,
				CAMPO_MESE_DATA_RESTITUZIONE_ATTI, CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI));
		lImpMod.setAnnotazione(getRequestStringParameter(CAMPO_NOTE));
		lImpMod.setFlagSospEsec(null); // non prevista per OPPOSIZIONE

		lImpMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lImpMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lImpMod.setDataInserimento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Opposizione da inserire: lImpMod " + lImpMod);

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
		// Ufficio Emittente
		String ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di "
				+ user.getUfficioUtente().getDescrComune();

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel eveMod = lCtrlEve.ExRicercaEventoByKey(lIdEvento);

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector<AvvocatoSiusModel> avvocati = null;
		avvocati = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius());

		// Recupero i dati del soggetto dalla sessione @emma 25/08/2016 - avvocatura
		String cognomeSoggetto = "";
		String nomeSoggetto = "";
		if (!isSessionAttributeNullObj("soggetto")) {
			SoggettoModel datiSoggetto = (SoggettoModel) getSessionAttribute("soggetto");
			cognomeSoggetto = datiSoggetto.getCognome();
			nomeSoggetto = datiSoggetto.getNome();
		} else if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null) {
			// provo a verificare se è presente nell'oggetto FascicoloGPModel
			cognomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod
					.getFascicoloSiusModel().getSoggetto().getCognome() : "";
			//EC@19/05/2017: correzione su nomeSoggetto. Inserivamo il nome sul cognome!			
			nomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod
					.getFascicoloSiusModel().getSoggetto().getNome() : "";
		} else {
			// devo procedere con una ricerca del soggetto per chiave soggetto
			BigDecimal idSoggetto = lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto();
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			SoggettoModel s = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);
			cognomeSoggetto = s.getCognome();
			nomeSoggetto = s.getNome();
		}

		Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = new Vector<AvvisiAvvocatoModel>();

		AvvisiAvvocatoModel lAvvisiAvvModel = null;

		Iterator itxAvv = avvocati.iterator();
		while (itxAvv.hasNext()) {
			lAvvisiAvvModel = new AvvisiAvvocatoModel();

			AvvocatoSiusModel lAvv = (AvvocatoSiusModel) itxAvv.next();

			lAvvisiAvvModel.setIdAvvocato(lAvv.getAvvocato().getIdAvvocato());
			lAvvisiAvvModel.setCognomeSoggeto(cognomeSoggetto);
			lAvvisiAvvModel.setNomeSoggetto(nomeSoggetto);
			// lAvvisiAvvModel.setIdProvvedimento(); // viene settato nel controller
			lAvvisiAvvModel.setDescProvvedimento(eveMod.getDescrTipoProvvedimento());
			lAvvisiAvvModel.setUfficioEmittente(ufficio);
			lAvvisiAvvModel.setTestoAvviso(ICostantiAvvisiAvvocato.CONTENUTO_INSERIMENTO_OPPOSIZIONE);
			lAvvisiAvvModel.setFlagVisualizzazione("N");
			lAvvisiAvvModel.setCodOperatoreInserimento(user.getUserId());
			lAvvisiAvvModel.setCodUfficioInserimento(user.getUfficioUtente().getCodUfficio());
			lAvvisiAvvModel.setDataInserimento(DateUtils.getSysDate());

			lAvvvisiAvvocato.add(lAvvisiAvvModel);

		}

		// ***********************************************************************
		// MEV_AVVOCATURA aggiunto parametro lAvvvisiAvvocato
		// ***********************************************************************
		// I campi depOpidDepositoOrdinanzaPc e depDecIdDepositoDecreto vengono impostati nel controller.
		IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
		ImpugnazioneModel llImpModRet = lCtrl.ExInserisciOpposizione(lImpMod, lIdEvento, lAvvvisiAvvocato);
		// ImpugnazioneModel llImpModRet = lCtrl.ExInserisciOpposizione (lImpMod, lIdEvento);

		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		// Setta la risposta nella request.
		setRequestAttribute("impugnazione", llImpModRet);

		// Prepara la pagina di destinazione, puntando all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.impugnazione.action.ActLoadDettaglioOpposizione");
		lPage.setParameter(ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE, "" + llImpModRet.getIdImpugnazione());

		return "" + lPage;
	}
}