package siap.sige.udienzacollegiale.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioUdienzaCollegiale
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di
 * <p>
 * UdienzaCollegialeSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioUdienzaCollegiale extends ActUdienzaCollegiale
		implements ICostantiUdienzaSige, ICostantiUdienzaCollegiale, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String fixUdienza(String uff) throws Exception {

		String lPage = PG_LOAD_DETTAGLIOUDIENZACOLLEGIALE_FIX;
		ArrayList<String> magArr = null;
		String procDesc = null;
		String giudPop = null;

		// *** tipo 1
		if (uff.equalsIgnoreCase("CAP")) {
			// CAP = Corte appello
			magArr = getMagistratiArray(1);
			procDesc = "Procuratore Generale";
		} else if (uff.equalsIgnoreCase("DIB")) {
			// DIB = Tribunale
			magArr = getMagistratiArray(1);
			procDesc = "Procuratore della Repubblica";

			// *** tipo 2
		} else if (uff.equalsIgnoreCase("GUPM")) {// GUPM = GUP Tribunale per i Minorenni
			magArr = getMagistratiArray(2);
			// procDesc = "Procuratore Generale";
			// Modifica del 29/11/2016 MEV_15_S4 (richiesto da Michele)
			procDesc = "Procuratore della Repubblica";

			// *** tipo 3
		} else if (uff.equalsIgnoreCase("DIBM")) {
			// DIBM = Tribunale per i Minorenni
			magArr = getMagistratiArray(3);
			procDesc = "Procuratore della Repubblica";

			// *** tipo 4
		} else if (uff.equalsIgnoreCase("CAS")) {
			// CAS = Corte Assise
			magArr = getMagistratiArray(4);
			giudPop = "yes";
		} else if (uff.equalsIgnoreCase("CASAP")) {
			// CASAP = Corte Assise Appello
			magArr = getMagistratiArray(4);
			giudPop = "yes";

			// *** tipo 5
		} else if (uff.equalsIgnoreCase("CAPSM")) {
			// CAPSM = Sezione per i Minorenni Corte di appello
			magArr = getMagistratiArray(5);
			procDesc = "Procuratore Generale";

		} else {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Funzione non gestita dal tipo ufficio di competenza.");
		}

		setRequestAttribute("magistratiArray", magArr);
		setRequestAttribute("procuraDesc", procDesc);
		setRequestAttribute("giudiciPopolari", giudPop);
		return lPage;
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

		//
		// Decisione del ruolo magistrato in virtù del tipo ufficio.
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Decisione Ruolo Magistrato : INIT ");

		String lRuoloMagistrato = "Giudice";
		String lTipoProcuratore = "Procuratore Generale";
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAPSM")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIBM")) {
			lRuoloMagistrato = "Consigliere";
			lTipoProcuratore = "Procuratore Repubblica";
		}

		setRequestAttribute("ruoloMagistrato", lRuoloMagistrato);
		setRequestAttribute("tipoProcuratore", lTipoProcuratore);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Decisione Ruolo Magistrato : OK ");

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
			// setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);
			return IWebConstants.PG_MESSAGE;
		}

		// ====================================================
		// Gestisce lo stack di ritorno, aggiungendo l'id
		// dell'udienza inserita, utile perla fissazione
		// udienza.
		// ====================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Il valore lIdUdienzaSige è : " + lIdUdienzaSige);

		String ret = PG_LOAD_DETTAGLIOUDIENZACOLLEGIALE;
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			ret = fixUdienza(getUfficioUtenteConnesso().getCodTipoUfficio());
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

		// if(!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO) ) {
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Il flag di ritorno è impostato."); if(lUdiMod != null){
		// siesLogger.debug("Imposta l'id UdienzaSige. a : " + lUdiMod.getIdUdienzaSige() );
		// setRequestAttribute("idudienzasige",lUdiMod.getIdUdienzaSige());
		// }

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("udienzasige", lUdiMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");
		// **********************
		String returnModifica = "N";
		if (!isRequestParameterNullObj("ReturnModifica")) {
			returnModifica = getRequestStringParameter("ReturnModifica");
		}
		setRequestAttribute("returnModifica", returnModifica);
		// Modifica del 08/03/2017
		String popUp = "";
		if (!isRequestParameterNullObj("PopUp")) {
			popUp = getRequestStringParameter("PopUp");
		}
		setRequestAttribute("PopUp", popUp);

		// *******************
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return ret;
	}

}