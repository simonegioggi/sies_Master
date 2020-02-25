package siap.siep.ordineesecuzione.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaScadenzarioEvento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario Evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActRicercaScadenzarioEvento extends ActionSiap implements ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire un ordine d'esecuzione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		IOrdineEsecuzione lOECtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		Vector lRicercaVect = lOECtrl.ExRicercaOEScadenzarioEventoByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("vettore", lRicercaVect);

		return PG_RICERCASCADENZARIOEVENTO; // restituisce la jsp di VIEW

	}

}