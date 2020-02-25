package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * MEV_39
 * <p>
 * Title: ActLoadInserisciRestituzioneOrdineConsegna
 * </p>
 * <p>
 * Description: Caricamento per l'Inserimento della Restituzione Ordine di consegna
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciRestituzioneOrdineConsegna extends ActionSiap implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

		// se il fascicolo non esiste in sessione
		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("idfascicolo", "" + fsm.getIdFascicoloSiep());
		// Funzione di esclusiva competenza dei proc. di classe IV
		if (fsm.getChiaveProgr().intValue() < 40000 || fsm.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimento di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		// CONTROLLI SUGLI ELEMENTO DEL FASCICOLO PER POTER ESEGUIRE PROVVEDIMENTI
		if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire una Misura di Sicurezza!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		// if (fsm.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
		// RedirectTo rt = new RedirectTo();
		// rt.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
		// + fsm.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
		// rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
		// + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		// return IWebConstants.PG_MESSAGE;
		// }

		isEventoNonValidato();

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel prm = new PenaResiduaModel();
		IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(fsm.getIdFascicoloSiep());

		if (prm != null && prm.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		Date dataInizioPena = null;
		if (prm != null)
			dataInizioPena = prm.getDataInizio();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(dataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", prm);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(fsm
				.getIdFascicoloSiep());

		if (pgldacm == null || pgldacm.getPosizioneGiuridica() == null) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			rt.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Ricerca Misure sicurezza già presenti nel fascicolo: Sono ORDINATE per DATA_INSERIMENTO
		List listaMisure = new ArrayList();
		IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
		listaMisure = ims.ExRicercaMisuraSicurezzaByIdFascicoloOrd(fsm.getIdFascicoloSiep());
		if (listaMisure.isEmpty()) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Procedimento privo di Misura di Sicurezza, impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}
		setRequestAttribute("listaMisure", listaMisure);

		// Ricerca Misure notificate
		List elencoMisureNotificate = new ArrayList();
		elencoMisureNotificate = ims.ExRicercaMSNotificateByIdFasc(fsm.getIdFascicoloSiep(), "I", null); // paretro inserimento
		setRequestAttribute("elencoMisureNotificate", elencoMisureNotificate);

		// Magistrato
		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc.ExRicercaMagistratoCompetenteByFascicoloDataFine(fsm
				.getIdFascicoloSiep());
		setRequestAttribute("magistrato", mcmm);

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCI_RESTITUZIONE_ORDINE_CONSEGNA;
	}

}