package siap.siep.reato.action;

import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadModificaContinuazioneReati
 * </p>
 * <p>
 * Description: Classe Action per la load di Modifica Continuazione Reati
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadModificaContinuazioneReati extends ActionSiap implements ICostantiReato {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		boolean proceed = true;
		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		lFascMod.setIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascMod = lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile modificare i Reati");
		}
		if (lFascMod.getFlagValidato().equalsIgnoreCase("S")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è stato validato! Impossibile modificare i Reati");
		}

		// ReatoModel lMod = new ReatoModel();

		if (proceed) {
			// IReato lRCtrl = SIEPLookupRemote.getReatoRemote();
			// Vector lVect = lRCtrl.ExRicercaReatiNoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());
			ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
			Vector lVect = lRCtrl.ExRicercaReatiNoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());

			setRequestAttribute("reati", lVect);
			setRequestAttribute("stringareati", lRCtrl.getStringheReati(lVect));
			setRequestAttribute("table", lRCtrl.getTableContinuazioni(lVect));

			return PG_LOADMODIFICACONTINUAZIONEREATI; // restituisce la jsp di VIEW
		} else {
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
		}
	}

}
