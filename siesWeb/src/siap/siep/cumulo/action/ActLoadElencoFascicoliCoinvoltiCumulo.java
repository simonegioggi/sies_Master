package siap.siep.cumulo.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActLoadRicercaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Cumulo
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
@SuppressWarnings("rawtypes")
public class ActLoadElencoFascicoliCoinvoltiCumulo extends ActionSiap implements ICostantiCumulo {
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFasc = new FascicoloSiepModel();
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Ricerca cumulo per FasSieIdFascicoloSiep
		CumuloModel lCumMod = new CumuloModel();
		lCumMod.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());

		ICumulo lCtrlCumulo = SIEPLookupRemote.getCumuloRemote();
		Vector lCum = lCtrlCumulo.ExRicercaCumulo(lCumMod);
		if (lCum.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione : Il fascicolo non risulta soggetto a cumulo! Selezionare un altro fascicolo.");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		/*
		 * Vector lCumulo = new Vector(); lCumulo =
		 * lCtrlCumulo.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFasc.getIdFascicoloSiep());
		 * 
		 * if (lCumulo.size()>0) {
		 */
		Vector lCumulati = lCtrlCumulo.ExRicercaFascicoliCumulatiByIDFascicoloSiep(lFasc.getIdFascicoloSiep());

		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFasc.getIdFascicoloSiep());

		IPenaResidua lPen = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = null;
		lPenRes = lPen.ExRicercaPenaResiduaUltimaValidata(lFasc.getIdFascicoloSiep());

		setRequestAttribute("PosizioneGiuridica", lPG);
		setRequestAttribute("PenaResidua", lPenRes);

		setRequestAttribute("cumulati", lCumulati);
		// setRequestAttribute("cumulo",lCumulo);
		setRequestAttribute("cumulo", lCum);

		return PG_LISTAFASCICOLICUMULATI;
		/*
		 * } else { //throw new F3BException(F3BException.USER_MESSAGE,
		 * "Attenzione : Il fascicolo non risulta soggetto a cumulo! Selezionare un altro fascicolo."); RedirectTo
		 * lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Attenzione : Il Provvedimento di esecuzione pene concorrenti risulta già validato.");
		 * lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
		 * ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 * 
		 * return IWebConstants.PG_MESSAGE; }
		 */
	}
}