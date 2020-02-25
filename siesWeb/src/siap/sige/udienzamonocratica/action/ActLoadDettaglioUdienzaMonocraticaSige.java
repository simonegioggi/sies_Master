package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioUdienzaMonocraticaSige
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di UdienzaMonocraticaSige
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
public class ActLoadDettaglioUdienzaMonocraticaSige extends ActUdienzaMonocraticaSige
		implements ICostantiUdienzaSige, ICostantiUdienzaMonocraticaSige, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
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

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getTipoUfficio();

		// 20190516 [SG]: cambiata gestione ritorno
		// super.gestioneRitorno();

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
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /null/frame.htm
			setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);
			return IWebConstants.PG_MESSAGE;
		}

		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		// devo individuare tutti i fascicoli SIGE che puntanto all'udienza che sto modificando
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();
		lVect = lCtrlUdPr.ExRicercaProcedimentiPerUdienza(lIdUdienzaSige, STATO_FASCICOLO, null);
		siesLogger.debug("NUMERO DI PROCEDIMENTI COLLEGATI ALL'UDIENZA:" + lVect.size());
		setRequestAttribute("numProcePerUdienza", Integer.toString(lVect.size()));

		// ====================================================
		// Gestisce lo stack di ritorno, aggiungendo l'id
		// dell'udienza inserita, utile perla fissazione
		// udienza.
		// ====================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Il valore lIdUdienzaSige è : " + lIdUdienzaSige);
		MagistratoAssegnatarioModel magAssCorrente = null;
		IMagistrato lCtrlM = SIGELookupRemote.getMagistratoRemote();
		IMagistratoAssegnatario lCtrlA = SIGELookupRemote.getMagistratoAssegnatarioRemote();
		String ret = PG_LOAD_DETTAGLIOUDIENZAMONOCRATICASIGE;

		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			ret = PG_LOAD_DETTAGLIOUDIENZAMONOCRATICASIGE_FIX;
			setRequestAttribute("descSezione", descSezione(lUdiMod));
			// quando sono in questa IF sto lavorando per uno specifico FASCICOLO SIGE
			// devo recuperare il Magistrato Assegnatario del Fascicolo in Lavorazione per
			// visualizzare i dati sulla maschera di dettaglio (magistrato assegnatario, procuratore e
			// cancelliere)
			if (!isSessionAttributeNullObj("FascicoloSigeEsteso")
					&& getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige() != null
					&& lVect.size() > 0) {
				magAssCorrente = lCtrlA.ExRicercaMagAssCorrenteXFascicolo(
						getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
				if (magAssCorrente != null && magAssCorrente.getMagCodMagistrato() != null) {
					MagistratoModel lMagistrato = lCtrlM
							.ExRicercaMagistratoByCod(magAssCorrente.getMagCodMagistrato(), lCodUfficio);
					if (lMagistrato != null) {
						setRequestAttribute("descrMagAssegnatario",
								lMagistrato.getCognome() + " " + lMagistrato.getNome());
					}
				}
				// recupero il procuratore
				if (magAssCorrente.getCodProcuratore() != null) {
					MagistratoModel lMagMod = lCtrlM.ExRicercaMagistratoByCodETipoUfficio(
							magAssCorrente.getCodProcuratore(), lCodTipoUfficio, lCodComune);
					if (lMagMod != null)
						setRequestAttribute("descrProcuratore",
								lMagMod.getCognome() + " " + lMagMod.getNome());
				} else {
					setRequestAttribute("descrProcuratore", lUdiMod.getDescrProcuratore());
				}
				// recupero l'assistente cancelliere
				if (magAssCorrente.getIdAssistente() != null) {
					// chiama il controller
					IAssistenteGiudiziario lCtrlAs = SICOLookupRemote.getAssistenteGiudiziarioRemote();
					AssistenteGiudiziarioModel llAssMod = lCtrlAs
							.ExRicercaAssistenteGiudiziarioByKey(magAssCorrente.getIdAssistente());
					if (llAssMod != null)
						setRequestAttribute("descrAssistente",
								llAssMod.getCognome() + " " + llAssMod.getNome());
				} else {
					setRequestAttribute("descrAssistente", lUdiMod.getDescrIdAssistente());
				}
			}

			// QUANDO LA SIZE è ZERO , IL CODICE PROCURATORE, il cancelliere e il giudice LO DEVO PRENDERE
			// DALL'OGGETTO UDIENZA
			if (lVect.size() == 0) {
				setRequestAttribute("descrMagAssegnatario", "");
				setRequestAttribute("descrProcuratore", lUdiMod.getDescrProcuratore());
				setRequestAttribute("descrAssistente", lUdiMod.getDescrIdAssistente());
			}

			// intervento 11.2.1
			if (this.getParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null) {
				setRequestAttribute(ICostantiMagistrato.CAMPO_COD_MAGISTRATO,
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			}
		} else {
			if (!isSessionAttributeNullObj("StackDiRitorno")) {
				Stack lRetStack = (Stack) getSessionAttribute("StackDiRitorno");
				// 20190516 [SG]: cambiata gestione ritorno
				if (!lRetStack.isEmpty()) {
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

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return ret;
	}

}