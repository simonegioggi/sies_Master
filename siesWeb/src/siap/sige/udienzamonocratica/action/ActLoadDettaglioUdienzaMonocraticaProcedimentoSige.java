package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Stack;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.util.SICOLookupRemote;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioUdienzaMonocraticaProcedimentoSige
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di UdienzaMonocraticaSige accoppiato con il procedimento
 * sige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioUdienzaMonocraticaProcedimentoSige extends ActUdienzaMonocraticaSige
		implements ICostantiUdienzaSige, ICostantiUdienzaMonocraticaSige, ICostantiCollegio {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String descSezione(UdienzaSigeModel lUdiMod) {
		String desc = "";
		try {
			ISezione lCtrlSez = SIGELookupRemote.getSezioneRemote();
			SezioneModel lColl = lCtrlSez.ExRicercaSezioneByKey(lUdiMod.getCodIdSezioneUdienza());
			desc = lColl.getDescrizione();
		} catch (Exception e) {
		}
		return desc;
	}

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 *
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		super.gestioneRitorno();

		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================
		BigDecimal lIdUdienzaSige = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);

		if (!isRequestParameterNullObj(CAMPO_NUMERO_UDIENZE_MAGRISTRATO)) {
			// siamo nel dettaglio di una udienza trovata tramite data e magistrato
			String numUdienze = getRequestStringParameter(CAMPO_NUMERO_UDIENZE_MAGRISTRATO);
			setRequestAttribute(CAMPO_NUMERO_UDIENZE_MAGRISTRATO, numUdienze);
		}

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiMod = lCtrl.ExRicercaUdienzaSigeById(lIdUdienzaSige);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lUdiMod == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);
			return IWebConstants.PG_MESSAGE;
		}

		// ====================================================
		// Gestisce lo stack di ritorno, aggiungendo l'id
		// dell'udienza inserita, utile perla fissazione
		// udienza.
		// ====================================================
		siesLogger.debug(" Il valore lIdUdienzaSige è : " + lIdUdienzaSige);

		String ret = PG_LOAD_DETTAGLIOUDIENZAPROCEDMONOCRATICASIGE;

		// casistica dall'interno delle fissazioni udienze e/o scarico ordinanza
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			ret = PG_LOAD_DETTAGLIOUDIENZAMONOCRATICASIGE_FIX;
			setRequestAttribute("descSezione", descSezione(lUdiMod));
			// casistica da funzioni di supporto
		} else {

			if (!isSessionAttributeNullObj("StackDiRitorno")) {
				Stack lRetStack = (Stack) getSessionAttribute("StackDiRitorno");
				String lUrl = (String) lRetStack.pop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Il valore di lRetStack è : " + lUrl);
				// Si aggiunge l'id dell'udienza.
				lUrl += "&" + "IdUdienzaSige=" + lUdiMod.getIdUdienzaSige();
				// Reinserisce il nuovo url...
				lRetStack.push(lUrl);
				// Si rimette in sessione lo stack di ritorno.
				setSessionAttribute("StackDiRitorno", lRetStack);
			}

		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("udienzamonocraticasige", lUdiMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getTipoUfficio();

		// Se L'utente connesso appartiene ad un ufficio distaccato
		// s'imposta come codice comune i primi 6 caratteri del codice distretto.
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("TRIBSD"))
			lCodComune = getUfficioUtenteConnesso().getCodDistretto().substring(0, 6);

		// Crea Lista Elenco Magistrati per ruolo di giudice.
		IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));

		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		// recupero LA LISTA DEI MAGISTRATI ASSEGNATARI
		String codMagAss = "-";
		Option lOptionMagAss = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOptionMagAss.setAddBlankItem(Option.BLANK_ITEM);
		lOptionMagAss.setValueBlankItem("-");
		// recupero il magistrato assegnatario legato al fascicolo in SESSIONE solo se NON PROVENGO DA
		// FUNZIONI AMMINISTRATIVE
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			if (!isSessionAttributeNullObj("FascicoloSigeEsteso")
					&& getFascicoloSigeEstesoInSessione().getMagAssegnatario() != null) {
				codMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario().getMagCodMagistrato();
				lOptionMagAss.setSelected(codMagAss);
			}
		} else {
			if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO))
				lOptionMagAss.setSelected(this.getParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		}
		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		setRequestAttribute("elencoMagAsseg", "" + lOptionMagAss);

		// Crea lista Elenco procuratori.
		// lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio));
		// 20171003: [EC] aggiunto blank item nella lista dei procuratori
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio),
				Option.BLANK_ITEM);
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE))
			lOption.setSelected(this.getParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco assistenti.
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio),
				Option.BLANK_ITEM);
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lOption.setSelected(this.getParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));

		setRequestAttribute("elencoAssistenti", "" + lOption);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return ret;
	}

}