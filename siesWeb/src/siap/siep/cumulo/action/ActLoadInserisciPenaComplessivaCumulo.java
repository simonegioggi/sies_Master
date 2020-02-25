package siap.siep.cumulo.action;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.RedirectTo;
import java.util.Vector;
import siap.siep.cumulo.controller.ICumulo;
import siap.web.ISIAPCostantiWeb;
import siap.siep.cumulo.model.CumuloModel;

/**
 * <p>
 * Title: ActLoadInserisciPenaComplessivaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento dei dati del Cumulo
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
public class ActLoadInserisciPenaComplessivaCumulo extends ActionSiap implements ICostantiCumulo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_CUMULANTE + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// 07-06-2006 -- Dario -- Viviana
		// this.isEventoNonValidatoPerCumulo();
		// this.isEventoNonValidato();
		this.isEventoNonValidatoPerPenaCumulo();

		// Ricerca cumulo per FasSieIdFascicoloSiep
		CumuloModel lCumMod = new CumuloModel();
		lCumMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		Vector lCum = iCum.ExRicercaCumulo(lCumMod);
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

		// MEV 16: tolto msg bloccante
		// Vector cumuli =
		// iCum.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascMod.getIdFascicoloSiep());
		// if (cumuli.size() == 0)
		// {
		// RedirectTo lRedirigi = new RedirectTo();
		// lRedirigi.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		// "Attenzione : Il Provvedimento di esecuzione pene concorrenti risulta già validato.");
		// lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
		// +
		// ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" +
		// getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		// return IWebConstants.PG_MESSAGE;
		// }

		// ==========================================================================
		// Recupera la posizione giuridica corrente
		// ==========================================================================
		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		if (lPG == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");

		setRequestAttribute("PosizioneGiuridica", lPG);

		Option lOption = null;
		lOption = new Option(DecodificheManager.getInstance().getFlagErgastolo());
		setRequestAttribute("FlagErgastolo", "" + lOption);

		// ==========================================================================
		// Recupera il magistrato competente
		// ==========================================================================
		IMagistratoCompetente IMag = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagCo = IMag.ExRicercaMagistratoCompetenteByFascicolo(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("MagistratoCompetente", lMagCo);
		setSessionAttribute("cumulowiz", "");

		return PG_LOAD_INSERIMENTO_PENA_COMPLESSIVA_CUMULO;
	}

}