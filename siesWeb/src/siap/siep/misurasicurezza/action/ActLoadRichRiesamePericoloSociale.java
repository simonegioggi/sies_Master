package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 *
 * <p>
 * Title: ActLoadRichRiesamePericoloSociale
 * </p>
 * <p>
 * Description: Classe la richiesta al MDS di accertamento pericolosità sociale -
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 *
 */
@SuppressWarnings("rawtypes")
public class ActLoadRichRiesamePericoloSociale extends ActionSiap implements ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		// 05/11/2014 Funzione di esclusiva competenza dei proc. di classe IV.
		if (lFascMod.getChiaveProgr().intValue() < 40000 || lFascMod.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimento di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire una Misura di Sicurezza!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// Controllo Esistenza pena residua non validata per quel fascicolo

		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		/*
		 * if (lPenaResMod == null) { RedirectTo lRedirigi = new RedirectTo();
		 * lRedirigi.setPage(IWebConstants.PG_MAIN); setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
		 * lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
		 * ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 * 
		 * return IWebConstants.PG_MESSAGE; }
		 */

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
//		Date lDataFinePenaM = null;
		if (lPenaResMod != null) {
			lDataInizioPena = lPenaResMod.getDataInizio();
//			lDataFinePenaM = lPenaResMod.getDataFine();
			lDataFinePenaA = lPenaResMod.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		// Controllo esistenza almeno un avvocato per fascicolo.
		// Commentato su richiesta di M.T. (misure di sicurezza test 03-03-2015.doc)
		/*
		 * IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote(); try { Vector lAvvVect =
		 * lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep()); } catch (SIEPException e) {
		 * RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage() +
		 * " Impossibile eseguire Richiesta di Accertamento.");
		 * lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&" +
		 * ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 * 
		 * return IWebConstants.PG_MESSAGE; }
		 */
		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
//		String lCodPosGiu = "";

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

//		lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().trim();
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Riempimento ComboBox Autorità
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// MERGE v10: cancello codice come in Mev2-s2
		// Avvocato
//		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
//		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
//		setRequestAttribute("avvocati", lAvvocati);

		// Misure Sicurezza
		Option lOptionT = new Option(DecodificheManager.getInstance().getRichiesteMisSic());
		lOptionT.setFilter(new String[] { "2110", "2114" });

		setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);

		List lListMis = new ArrayList();
		MisuraSicurezzaModel MisMod = new MisuraSicurezzaModel();
		MisMod = null;

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		try {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
			if (lListMis != null)
				if (lListMis.size() == 1)
					MisMod = (MisuraSicurezzaModel) lListMis.get(0);
		} catch (F3BException e) {

		}

		setRequestAttribute("listaMisure", lListMis);
		setRequestAttribute("MisuraModel", MisMod);

		// 24/10/2014
		// Tipo UDS per la comunicazione
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		lOption.setFilter(new String[] { "-", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + lOption);

		// Altri destinatari (procure)
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM(), "-");
		String[] lFiltroAut = { "-", "PM", "PGCAP", "PMM" };
		lOption.setFilter(lFiltroAut);
		setRequestAttribute("autoritaProc", "" + lOption);

		return PG_LOAD_RICH_RIESAME_PERICOLO_SOC; // restituisce la jsp di VIEW
	}

}