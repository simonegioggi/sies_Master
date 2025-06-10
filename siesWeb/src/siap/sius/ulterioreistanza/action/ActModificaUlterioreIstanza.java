package siap.sius.ulterioreistanza.action;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActModificaUlterioreIstanza - Classe Action per la modifica di UlterioreIstanza
 *
 * @version 1.0
 */
public class ActModificaUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del UlterioreIstanza.
	 * <p>
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Inizializza il model per popolarlo con i dati in request
		UlterioreIstanzaModel lUltMod = new UlterioreIstanzaModel();

		lUltMod.setIdUlterioreIstanza(getRequestBigDecimalParameter(CAMPO_ID_ULTERIORE_ISTANZA));
		lUltMod.setCodOggettoProcedimento(getRequestStringParameter(CAMPO_COD_CONTENUTO));

		lUltMod.setDataRichiesta(getRequestDateParameter(CAMPO_ANNO_DATA_RICHIESTA, CAMPO_MESE_DATA_RICHIESTA,
				CAMPO_GIORNO_DATA_RICHIESTA));

		lUltMod.setDataArrivoCancelleria(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,
				CAMPO_MESE_DATA_ARRIVO_CANCELLERIA, CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA));

		lUltMod.setCodTipoAtto(getRequestStringParameter(CAMPO_COD_TIPO_ATTO));
		lUltMod.setCodTipoMittenteAtto(getRequestStringParameter(CAMPO_COD_TIPO_MITTENTE_ATTO));

		// Imposta il campo sedeMittente, prelevando dalla superclass il Codice Comune
		// attraverso la propria descrizione.
		if ((getRequestStringParameter(CAMPO_SEDE_MITTENTE)).equalsIgnoreCase(""))
			lUltMod.setSedeMittente(super.getCodComuneByDescr("-").getCodComune());
		else
			lUltMod.setSedeMittente(
					super.getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_MITTENTE)).getCodComune());

		lUltMod.setDescrMittente(getRequestStringParameter(CAMPO_DESCR_MITTENTE));
		lUltMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lUltMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lUltMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lUltMod.setDataAggiornamento(DateUtils.getSysDate());

		// Imposta l'id del fasciclo sius, prelevando lo stesso dal fascicolo in sessione.
		FascicoloSiusModel lFascicolo = new FascicoloSiusModel(
				((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel());
		lUltMod.setFasSiuIdFascicoloSius(lFascicolo.getIdFascicoloSius());
		//
		// Gestione lettura e set dei tenori, by referece.
		//
		this.letturaTenori(lUltMod);

		// Chiama il controller per eseguire la modifica del record interessato
		IUlterioreIstanza lCtrl = SIUSLookupRemote.getUlterioreIstanzaRemote();
		UlterioreIstanzaModel lUltModRet = lCtrl.ExModificaUlterioreIstanza(lUltMod);

		// Imposta la modalità di modifica
		setRequestAttribute("modalita", "M");

		setRequestAttribute("ulterioreistanza", lUltModRet);

		// Va alla pagina di dettaglio invocando la corrispondete Action
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.ulterioreistanza.action.ActLoadDettaglioUlterioreIstanza&"
				+ CAMPO_ID_ULTERIORE_ISTANZA + "=" + lUltModRet.getIdUlterioreIstanza().toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return lPage;
	}

	/**
	 * Metodo che si occupa di leggere i tenori dalla Request, eseguire la scomposizione secondo il Pattern
	 * PIPE, e inserire l'elenco dei tenori, opportumante raccolti in forma di Array, all'interno
	 * dell'UlterioreIstanzaModel
	 *
	 * @param aUltIstMod
	 *            Istnaza del model by reference.
	 */
	private void letturaTenori(UlterioreIstanzaModel aUltIstMod) throws F3BException {

		// Caricamento Tenore...
		// Preleva dalla request i codici e descrizioni dei tenori,
		// impipati rispettivamente con separatore "|" e "\n".

		// Stabilisce la size dell'Array di Tenori da caricare in UlterioreIstanzaModel.

		StringTokenizer lCodOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO), "|");
		StringTokenizer lDescrOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiUlterioreIstanza.CAMPO_DESCR_OGGETTO_PROCEDIMENTO), "\n");
		// STUB 12/11/2003 Aggiunti i Codici Dettaglio Oggetti.
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));
		// Conta i tokens
		int lSizeVector = lCodOggetto.countTokens();
		UlterioreIstanzaTenoreModel lTenori[] = new UlterioreIstanzaTenoreModel[lSizeVector];

		int lIndex = 0;
		// Lettura dei tokens
		while (lCodOggetto.hasMoreTokens()) {
			UlterioreIstanzaTenoreModel lUltIstTenModel = new UlterioreIstanzaTenoreModel();

			lUltIstTenModel.setCodOggettoTenore(lCodOggetto.nextToken());
			lUltIstTenModel.setDescrOggettoTenore(lDescrOggetto.nextToken());
			// Codice dell'ufficio dell'operatore che inserisce
			lUltIstTenModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			// Codice dell'operatore che inserisce
			lUltIstTenModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lUltIstTenModel.setDataInserimento(DateUtils.getSysDate());
			lUltIstTenModel.setCodMagistrato("-");
			lUltIstTenModel.setProgrTenore(new Integer(lIndex + 1));
			lUltIstTenModel.setCodEsitoTenore("-");
			// Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller

			// 12/11/2003 Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
			if ((lStCodiceDet).indexOf(lUltIstTenModel.getCodOggettoTenore() + "0") < 0)
				lUltIstTenModel.setCodDettaglioOggetto("-");
			else {
				String lCodDettaglioCorrente = lStCodiceDet.substring(
						lStCodiceDet.indexOf(lUltIstTenModel.getCodOggettoTenore() + "0") + 4,
						lStCodiceDet.indexOf(lUltIstTenModel.getCodOggettoTenore() + "0") + 8);
				lUltIstTenModel.setCodDettaglioOggetto(lCodDettaglioCorrente);
			}
			lTenori[lIndex] = lUltIstTenModel;
			lIndex++;
		}
		aUltIstMod.setUltIstTenori(lTenori);
	}

}