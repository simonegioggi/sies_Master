package siap.sius.ulterioreistanza.action;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActInserisciUlterioreIstanza - Classe Action per l'inserimento di UlterioreIstanza
 *
 * @version 1.0
 */
public class ActInserisciUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private FascicoloGPModel mFascicoloGP = null;

	/**
	 * Metodod azione di Inserimento del UlterioreIstanza.
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 *             propaga errore di eccezione.
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		// Preleva il fascicolo GPModel dalla sessione.
		mFascicoloGP = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
		FascicoloSiusModel lFascicolo = new FascicoloSiusModel(mFascicoloGP.getFascicoloSiusModel());

		// **
		// Inizio : Lettura dalla request e valorizzazione attributi del model con i dati sottomessi.
		// dall'operatore.
		// **

		// Inizializza Model e lo si popola con i dati prelevati dalla request.
		UlterioreIstanzaModel lUltMod = new UlterioreIstanzaModel();
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

		// Dati di operazione.
		lUltMod.setCodOperatoreInserimento(super.getCodUtenteConnesso());
		lUltMod.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
		lUltMod.setDataInserimento(DateUtils.getSysDate());

		lUltMod.setFasSiuIdFascicoloSius(lFascicolo.getIdFascicoloSius());

		//
		// Gestione lettura e set dei tenori, by referece.
		//
		this.letturaTenori(lUltMod);

		// ***
		// Fine: Lettura dati dalla Request.
		// ***

		// Preparazione dati dei Tenori, per la lettura e scrittura sulla tabella
		// tenori.
		TenoreModel[] lTenori = this.toArrayTenori(lUltMod);

		// Chaimata al controller per lesecuzione dell'inserimento dei dati in tabella.
		IUlterioreIstanza lCtrl = SIUSLookupRemote.getUlterioreIstanzaRemote();
		// setta la risposta nella request
		UlterioreIstanzaModel lUltModRet = lCtrl.ExInserisciUlterioreIstanza(lUltMod, lTenori);

		// Invia il model popolato nella request.
		setRequestAttribute("ulterioreistanza", lUltModRet);

		// Prepara la pagina di destinazione.
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
	 * PIPE, ed inserire l'elenco dei tenori, opportumante raccolti in forma di Array, all'interno
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

		// String lCodOgg = getRequestStringParameter(
		// ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO);
		StringTokenizer lCodOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiUlterioreIstanza.CAMPO_COD_OGGETTO_PROCEDIMENTO), "|");
		StringTokenizer lDescrOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiUlterioreIstanza.CAMPO_DESCR_OGGETTO_PROCEDIMENTO), "\n");
		// STUB 12/11/2003 Aggiunti i Codici Dettaglio Oggetti.
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));

		// Preleva il numero dei Tokens
		int lSizeVector = lCodOggetto.countTokens();
		UlterioreIstanzaTenoreModel lTenori[] = new UlterioreIstanzaTenoreModel[lSizeVector];

		int lIndex = 0;

		// Cicla per gli elementi del cod oggetto, ed esegue parsing.
		while (lCodOggetto.hasMoreTokens()) {
			UlterioreIstanzaTenoreModel lUltIstTenModel = new UlterioreIstanzaTenoreModel();

			lUltIstTenModel.setCodOggettoTenore(lCodOggetto.nextToken());
			lUltIstTenModel.setDescrOggettoTenore(lDescrOggetto.nextToken());
			// Codice dell'ufficio dell'operatore che inserisce
			lUltIstTenModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			// Codice dell'operatore che inserisce
			lUltIstTenModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lUltIstTenModel.setDataInserimento(DateUtils.getSysDate());
			lUltIstTenModel
					.setCodMagistrato(mFascicoloGP.getGeneraleProcedimentoModel().getCodAutoritaDelegata());

			// lUltIstTenModel.setCodMagistrato( "-" );

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
			// Setto l'Array su GPtenoreModel
			lTenori[lIndex] = lUltIstTenModel;
			lIndex++;
		}

		aUltIstMod.setUltIstTenori(lTenori);
	}

	/**
	 * Metodo che preleva dalla lista dei tenori di Ulteriore istanza l'array dei tenori, e contestualmente
	 * alimenta un secondo array in uscita dei Tenori generici.
	 *
	 * @param aUltIst
	 *            model popolato con i dati inviati dall chiamante.
	 * @return ritrona array di TenoriModel.
	 */
	private TenoreModel[] toArrayTenori(UlterioreIstanzaModel aUltIst) {

		int lSize = aUltIst.getUltIstTenori().length;
		TenoreModel[] lTenori = new TenoreModel[lSize];

		// Cicla per gli elementi dell'array di tenori, appartenenti al
		// dominio di Ulteriore Istanza Tenori e inserisce gli stessi
		// nell'array di elementi apparteneti al dominio Tenori.
		for (int i = 0; i < lSize; i++) {
			lTenori[i] = new TenoreModel();
			lTenori[i].setCodOggettoTenore(aUltIst.getUltIstTenori()[i].getCodOggettoTenore());
			lTenori[i].setGenPridGeneraleProcedimento(
					this.mFascicoloGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenori[i].setDataInserimento(aUltIst.getUltIstTenori()[i].getDataInserimento());
			lTenori[i].setCodOperatoreInserimento(aUltIst.getUltIstTenori()[i].getCodOperatoreInserimento());
			lTenori[i].setCodUfficioInserimento(aUltIst.getUltIstTenori()[i].getCodUfficioInserimento());
			lTenori[i].setCodMagistrato(aUltIst.getUltIstTenori()[i].getCodMagistrato());
			lTenori[i].setCodEsitoTenore(aUltIst.getUltIstTenori()[i].getCodEsitoTenore());
		}

		return lTenori;
	}

}