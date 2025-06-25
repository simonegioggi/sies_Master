package siap.siepe.attivita.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.util.UtilTemplate;
import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.espertoattivita.controller.IEspertoAttivita;
import siap.siepe.relazione.controller.IRelazione;
import siap.siepe.util.SIEPELookupRemote;

/**
 * ActLoadDettaglioAttivita - Classe Action per la load dettaglio di Attivita
 *
 * @version 1.0
 */
public class ActLoadDettaglioAttivita extends ActionSiap implements ICostantiAttivita {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	AttivitaModel mAttMod = null;
	AssistenteSocialeModel mAssSocMod = null;
	String lMessaggioRiapertura = "Confermi la riapertura della Attività ? ";

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdAttivita = this.getRequestBigDecimalParameter(CAMPO_ID_ATTIVITA);
		// Chiama il controller per il recupero dell'attività
		IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
		mAttMod = lCtrl.ExRicercaAttivitaByKey(lIdAttivita);
		// Chiama il controller per il recupero dei dati dell'Assistente Sociale.
		IAssistenteSociale lCtrlAssSoc = SIEPELookupRemote.getAssistenteSocialeRemote();
		mAssSocMod = lCtrlAssSoc.ExRicercaAssistenteSocialeByKey(mAttMod.getAssSocIdAssSociale());

		// Chiama il Controller per il recupero delle Relazioni
		IRelazione lCtrlRel = SIEPELookupRemote.getRelazioneRemote();
		Vector lRelazioni = lCtrlRel.ExRicercaRelazioniByAttivita(lIdAttivita);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Numero di Relazioni collegate ->" + lRelazioni.size());

		// Chiama il Controller per il recupero dell'elenco degli Esperti
		IEspertoAttivita lCtrlEsp = SIEPELookupRemote.getEspertoAttivitaRemote();
		Vector lEsperti = lCtrlEsp.ExRicercaEspertiAttiviXAttivita(lIdAttivita);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Numero di Esperti attivi ->" + lEsperti.size());

		setLinkRitorno();

		// Gestione stampabilità, modificabilità, trasferibilità
		String lStampabile = "SI";
		String lModificabile = "SI";
		String lTrasferibile = "NO";
		String lDaValidare = "SI";
		String lProprietario = "SI";

		// l'Attività è Modificabile fino a che non è chiusa purchè l'utente sia proprietario
		if (mAttMod.getDataChiusura() != null || !isProprietario()) {
			lModificabile = "NO";
			lDaValidare = "NO";
		}

		// Controllo sulla validazione della Stampa
		if ((mAttMod.getFlagDocumentoRegistrato() != null
				&& mAttMod.getFlagDocumentoRegistrato().compareToIgnoreCase("S") == 0) || !isProprietario()) {
			// l'Attività è Trasferibile se è anche non modificabile
			if (mAttMod.getDataChiusura() != null && isProprietario()) {

				lTrasferibile = "SI";
				// messaggio di conferma Riapertura

			}
			// L'attività non è modificabile se la stampa è validata
			lModificabile = "NO";

			setRequestAttribute("Upload", "NO");
		} else
			// Preparazione Combo Template
			gestioneTemplate();

		// Stampabile se utente proprietario o se stampa validata
		if (isProprietario() || (mAttMod.getFlagDocumentoRegistrato() != null
				&& mAttMod.getFlagDocumentoRegistrato().compareToIgnoreCase("S") == 0))
			lStampabile = "SI";
		else
			lStampabile = "NO";

		if (isProprietario())
			lProprietario = "SI";
		else
			lProprietario = "NO";

		setRequestAttribute("UtenteProprietario", lProprietario);
		setRequestAttribute("FlagFasSiepe", "SI");
		setRequestAttribute("attivita", mAttMod);
		setRequestAttribute("assistentesociale", mAssSocMod);
		setRequestAttribute("relazioni", lRelazioni);
		setRequestAttribute("espertiAttivi", lEsperti);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Trasferibile", lTrasferibile);
		setRequestAttribute("DaValidare", lDaValidare);
		setRequestAttribute("MessaggioRiapertura", lMessaggioRiapertura);

		return PG_LOAD_DETTAGLIOATTIVITA;
	}

	// Funzione per la costruzione della combo con i template di stampa previsti per l'attività
	private void gestioneTemplate() throws Exception {

		Option lOptTemplate = null;
		// La lista dei Template nella combo viene scelta in base al codice Attività.
		lOptTemplate = UtilTemplate.listaTemplateByIncaricoAttivita("", mAttMod.getCodTipoAttivita());
		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate -> " + lOptTemplate);
		/*
		 * // template di default String[] lSelected = lOptTemplate.getSelecteds(); if (lSelected != null &&
		 * lSelected.length > 0) { setRequestAttribute(ICostantiTemplate.CAMPO_DEFAULT_TEMPLATE,
		 * lSelected[0]); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
		 * posto di LogF3B.getLogger() siesLogger.debug("TemplateDiDefault -> " + lSelected[0]); }
		 */
		return;
	}

	private boolean isProprietario() throws Exception {

		boolean retValue = false;
		if (getCodUfficioUtenteConnesso().equalsIgnoreCase(mAttMod.getCodUfficioInserimento()))
			retValue = true;
		return retValue;
	}

}