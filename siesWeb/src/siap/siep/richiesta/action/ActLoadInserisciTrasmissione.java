package siap.siep.richiesta.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciTrasmissione
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci della Trasmissione
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
public class ActLoadInserisciTrasmissione extends ActionSiap implements ICostantiRichiesta {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		this.isEventoNonValidato();

		// vado avanti solo se il fascicolo non è iscritto
		if (isFascicoloIscritto())
			return IWebConstants.PG_MESSAGE;

		this.isFascicoloSiepDiCompetenza();

		/*
		 * if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) { RedirectTo
		 * lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno() +
		 * "/" + lFascMod.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
		 * lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
		 * ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi); return IWebConstants.PG_MESSAGE; }
		 */

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;

		}

		setRequestAttribute("posizioneluogoaltra", lPos);
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (lPenaResMod != null && "N".equals(lPenaResMod.getFlagValidato())) {
			setRequestAttribute("dataeditabile", "S");
		}

		setRequestAttribute("penaresidua", lPenaResMod);

		// tipo richiesta
		Option lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaT());
		setRequestAttribute("richiesta", "" + lOption);

		// richiesta oggetto
		Vector lVect = new Vector();

		DecodificheModel mMod = new DecodificheModel();
		mMod.setContesto("MOTIVO_PROVVEDIMENTO");
		mMod.setFiltro("31");
		mMod.setDescription("atti");
		mMod.setCodiceAlternativo("RICHGEN");
		mMod.setCode("0339");
		lVect.add(mMod);
		//
		DecodificheModel mMod1 = new DecodificheModel();
		mMod1.setContesto("MOTIVO_PROVVEDIMENTO");
		mMod1.setFiltro("31");
		mMod1.setDescription("Richiesta Cessazione Mis. Alt. per sopravvenienza nuovo Titolo ex art. 51 bis");
		mMod1.setCode("5440");
		lVect.add(mMod1);
		//
		DecodificheModel mMod2 = new DecodificheModel();
		mMod2.setContesto("MOTIVO_PROVVEDIMENTO");
		mMod2.setFiltro("31");
		mMod2.setDescription("Richiesta prosecuzione della Misura Alternativa in corso ex art. 51 bis");
		mMod2.setCode("5441");
		lVect.add(mMod2);

		setRequestAttribute("oggetto", lVect);

		// ricerca magistrato firmatario
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Autorità esterna
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autorita", "" + lOption);

		// ufficio giudice dell'esecuzione
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioGE());
		setRequestAttribute("ufficioge", "" + lOption);

		// ufficio pubblico ministero
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		setRequestAttribute("ufficiopm", "" + lOption);

		setRequestAttribute("titolo", "TRASMISSIONE ATTI");

		return PG_LOAD_RICHIESTA_COMUNICAZIONE;
	}

}
