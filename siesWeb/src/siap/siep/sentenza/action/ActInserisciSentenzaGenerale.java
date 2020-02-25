package siap.siep.sentenza.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * ActInserisciSentenzaGenerale
 * </p>
 * <p>
 * Description:
 * </p>
 * La classe implementa la parte comune all'inserimento o alla modifica di una Sentenza che può essere di 3
 * tipi: Sentenza, Decreto, Sentenza Straniera. Per ognuna delle sentenze specifiche verra ereditata da questa
 * classe una classe specifica che richiamerà la funzione comune ai 3 tipi ed implementerà la funzione
 * abstract preparazioneDatiSpecifici() che invece implementa la parte specifica di quel tipo di sentenza.
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
abstract class ActInserisciSentenzaGenerale extends ActionSiap implements ICostantiSentenza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected SentenzaModel mSenMod = null;

	// Flag che segnala funzione di Modifica e non Inserimento
	protected boolean mModifica = false;

	public String processRequest() throws Exception {

		String lPage = "";

		// Viene istanziato il model
		mSenMod = new SentenzaModel();

		// Vengono valorizzati i dati comuni ai tre tipi di sentenza

		if (!isRequestParameterNullObj("TipoProvvedimento")) {
			String CodTipoProvvedimento = getRequestStringParameter("TipoProvvedimento");
			mSenMod.setCodTipoProvvedimento(CodTipoProvvedimento);
		}

		mSenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		mSenMod.setDataInserimento(DateUtils.getSysDate());
		mSenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// mSenMod.setDataIscrizione(getRequestDateParameter(
		// CAMPO_ANNO_DATA_ISCRIZIONE,CAMPO_MESE_DATA_ISCRIZIONE,CAMPO_GIORNO_DATA_ISCRIZIONE));
		mSenMod.setDescrUfficioInserimento(getUfficioUtenteConnesso().getDescrTipoUfficio());

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM)) {
			mSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
			mSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));
		}

		// segnalazioni 4: aggiunta gestione campi
		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_GIP)) {
			mSenMod.setAnnoRegeGip(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_GIP));
			mSenMod.setNumeroRegeGip(getRequestStringParameter(CAMPO_NUMERO_REGE_GIP));
		}

		// mSenMod.setDataArrivoAtto( getRequestDateParameter(
		// CAMPO_ANNO_DATA_ARRIVO_ATTO,CAMPO_MESE_DATA_ARRIVO_ATTO,CAMPO_GIORNO_DATA_ARRIVO_ATTO) );
		mSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
				CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));
		mSenMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		mSenMod.setCodLuogoEmittente(lComMod.getCodComune());

		if (!isRequestParameterNullObj(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE)) {
			mSenMod.setNumSezioneAutoritaEmittente(getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));
		}
		
		if (!isRequestParameterNullObj(CAMPO_ANNO_SENTENZA)) {
			mSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		}
		if (!isRequestParameterNullObj(CAMPO_NUMERO_SENTENZA)) {
			mSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_PROVVEDIMENTO)) {
			mSenMod.setAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
		}
		if (!isRequestParameterNullObj(CAMPO_NUMERO_PROVVEDIMENTO)) {
			mSenMod.setNumeroProvvedimento(getRequestStringParameter(CAMPO_NUMERO_PROVVEDIMENTO));
		}

		// if(! isRequestParameterNullObj(CAMPO_ANNO_DATA_IRREVOCABILITA))
		// mSenMod.setDataIrrevocabilita( getRequestDateParameter(
		// CAMPO_ANNO_DATA_IRREVOCABILITA,CAMPO_MESE_DATA_IRREVOCABILITA,CAMPO_GIORNO_DATA_IRREVOCABILITA) );

		if (isRequestParameterNullObj(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE))
			mSenMod.setCodTipoDecisioneCassazione("-");
		else {
			mSenMod.setCodTipoDecisioneCassazione(getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE));
			mSenMod.setAnnoSentenzaCassazione(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_CASSAZIONE));
			mSenMod.setNumeroSentenzaCassazione(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZIONE));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_RACCOLTA_GENERALE)) {
			mSenMod.setAnnoRaccoltaGenerale(getRequestBigDecimalParameter(CAMPO_ANNO_RACCOLTA_GENERALE));
			mSenMod.setNumeroRaccoltaGenerale(getRequestStringParameter(CAMPO_NUMERO_RACCOLTA_GENERALE));
		}

		// NUOVI CAMPI REVISIONE SENTENZA
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF)) {
			mSenMod.setCodTipoProvvedimentoRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF));
		} else
			mSenMod.setCodTipoProvvedimentoRif("-");

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO)) {
			mSenMod.setCodTipoProvvedimentoAltro(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO));
		} else
			mSenMod.setCodTipoProvvedimentoAltro("-");

		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			mSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else
			mSenMod.setCodSedeNotiziaReato("-");

		mSenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		// per le join
		mSenMod.setCodLuogoProvvRif("-");

		// 13/07/2010 Controllo presenza ufficio
		if (getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE).compareTo("-") != 0) {
			String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);
			/*String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(lCodTipo,
					getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
		}
		mSenMod.setCodTipoAutoritaProvvRif("-");
		mSenMod.setCodTipoProvvRif("-");
		mSenMod.setCodBilanciamentoCircostanze("-"); // Per le join

		// Nel caso di funzione di Modifica
		if (mModifica) {
			mSenMod.setIdSentenza(getRequestBigDecimalParameter(CAMPO_ID_SENTENZA));
			mSenMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			mSenMod.setDataAggiornamento(DateUtils.getSysDate());
			mSenMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		}

		// Valorizzazione dei dati specifici del tipo di sentenza
		lPage = preparazioneDatiSpecifici();

		return lPage;
	}

	abstract String preparazioneDatiSpecifici() throws Exception;

	protected String inserimento() throws Exception {

		// chiama il contreller
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Sentenza da inserire -> " + mSenMod);
		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
		SentenzaModel lSen = lSCtrl.ExInserisciSentenza(mSenMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Sentenza inserita -> " + lSen);

		// setta la risposta nella request
		setRequestAttribute("sentenza", lSen);

		// Prepara la "pagina" di destinAction
		String lPage = "";
		// setta la risposta nella request
		// Si passa solo il Model
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&" + CAMPO_ID_SENTENZA + "="
				+ lSen.getIdSentenza().toString();

		return lPage;
	}

	protected String modifica() throws Exception {

		SentenzaModel lSenRet = null;

		ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Sentenza con modifiche  -> " + mSenMod);

		// Controllo se in presenza di Fascicoli SIGE collegati alla sentenza
		if (!isRequestParameterNullObj(ICostantiFasSigeSentenza.NUM_FASCICOLI_SIGE)) {
			Vector lKeyFascicoliSelezionati = letturaIdFascicoliSige();
			// Modifica per segnalazione di M. Testa: impossibile modificare senza modificare Anno/Num
			// Sentenza)
			// Nel caso siano stati selezionati tutti i Fascicoli per la modifica si effettua una modifica e
			// non una duplicazione del record
			if (lKeyFascicoliSelezionati != null
					&& lKeyFascicoliSelezionati.size() == getRequestIntParameter(ICostantiFasSigeSentenza.NUM_FASCICOLI_SIGE))
				lSenRet = lCtrl.ExModificaSentenza(mSenMod);
			else
				lSenRet = lCtrl.ExModificaSentenzaSige(mSenMod, lKeyFascicoliSelezionati);
		} else {
			lSenRet = lCtrl.ExModificaSentenza(mSenMod);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Sentenza modificata  -> " + lSenRet);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("sentenza", lSenRet);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sentenza.action.ActLoadDettaglioSentenza&" + CAMPO_ID_SENTENZA + "="
				+ lSenRet.getIdSentenza().toString();

		return lPage;
	}

	/**
	 * Costruisce l'elenco con gli Id dei Fascicoli SIGE selezionati per la modifica alla Sentenza
	 * 
	 * @return
	 * @throws Exception
	 */
	private Vector letturaIdFascicoliSige() throws Exception {

		int lNumFascicoli = getRequestIntParameter(ICostantiFasSigeSentenza.NUM_FASCICOLI_SIGE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Num Fascicoli Sige  -> " + lNumFascicoli);

		Vector lKeyFascicoliSelezionati = new Vector();
		for (int y = 0; y < lNumFascicoli; y++) {
			if (!isRequestParameterNullObj("fascicolo" + y)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug(
						"id Fascicolo selezionato " + getRequestStringParameter("fascicolo" + y));
				lKeyFascicoliSelezionati.add(getRequestBigDecimalParameter("fascicolo" + y));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug(
						"id Fascicolo selezionato " + getRequestBigDecimalParameter("fascicolo" + y));
			}
		}
		return lKeyFascicoliSelezionati;
	}

}