package siap.siep.misuraalternativa.action;

import java.util.Hashtable;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaBackupSrc;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciMAProrogaUltPeriodo
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraAlternativa
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
public class ActLoadInserisciMAProrogaUltPeriodo extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isFascicoloSiepDiCompetenza();

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

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Eseguire prima il calcolo della pena. Impossibile eseguire la misura alternativa.");

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");

		} else {

		}

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Al Procedimento N."
					+ lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
					+ " non à stata associata una Posizione Giuridica.");

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca misura per il fascicolo
		// ricerca evento inserito dal TDS insieme alla misura alternativa
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		MisuraAlternativaModel lProrogaUtltP = new MisuraAlternativaModel();
		IMisuraAlternativaBackupSrc lMisAltController = SICOLookupRemote
				.getMisuraAlternativaBackupSrcRemote();
		lProrogaUtltP = lMisAltController.ExRicercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
		if (this.isRequestParameterNullObj("warning")) {
			if (lProrogaUtltP != null && lProrogaUtltP.getIdMisuraAlternativa() != null) {
				setRequestAttribute("misuraalternativa", lProrogaUtltP);

				UfficioModel lUffEmiMod = new UfficioModel();

				IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lProrogaUtltP.getChiaveUfficioFascicoloSius());
				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

			} else {
				setRequestAttribute("flagmisura", "N");

				// set goto page set flag misura
				setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
				setRequestAttribute(
						IWebConstants.MESSAGE_TEXT,
						"Ordinanza assente o dati incoerenti, si vuole procedere all'inserimento dell' Ordinanza e alla contestuale emissione del Provvedimento?");

				return "/jsp/files/warning.jsp";

			}
		} else {
			setRequestAttribute("flagmisura", "N");
		}

		// ricerca misura alternativa concessione det domiciliare speciale

		MisuraAlternativaModel lDetSpec = new MisuraAlternativaModel();
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		lDetSpec = lMisAltCtrl.ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
		if (lDetSpec != null && lDetSpec.getCodTipoMisura() != null)
			if (lDetSpec.getCodTipoMisura().equals("0012") || lDetSpec.getCodTipoMisura().equals("0077")) {
				setRequestAttribute("detdomSpecconcessa", lDetSpec);

			} else {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Eseguire prima l'Ammissione ad un periodo della detenzione domiciliare speciale");
			}
		// ricerca evento notifica ordinanza
		Hashtable lTable = new Hashtable();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		if (lProrogaUtltP != null && lProrogaUtltP.getIdMisuraAlternativa() != null) {
			// ricerca evento notifica riferito all'ordinanza
			lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lProrogaUtltP.getEveIdEvento());

			// ricerca evento non riferito all'ordinanza
			EventoModel lEveModelNonOrd = lCtrlEvento.ExRicercaEventoMANonRegistratoByFascicoloSiep(
					lFascMod.getIdFascicoloSiep(), lProrogaUtltP.getEveIdEvento());
			if (lEveModelNonOrd != null) {
				// ricerca evento notifica non riferito all'ordinanza
				EventoNotificaModel lEveModSucc = lCtrlEvento.ExRicercaEventoNotificaByKey(lEveModelNonOrd
						.getIdEvento());
				this.setRequestAttribute("eventonotifica", lEveModSucc);
				lTable = this.ricercaNotifiche(lEveModSucc.getNotifiche());

			} else {
				lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

			}

		}

		// RICERCA NOTIFICHE PER LA VISUALIZZAZIONE

		// ricerca magistrato competente

		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Pena Residua

		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", llPenMod);

		// Autorità esterna E

		AutoritaEsternaModel lAutE = null;

		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			setRequestAttribute("autoritaEsternaE", lAutE);

		}

		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// Autorità esterna N

		AutoritaEsternaModel lAutN = null;
		if (lTable.get("AutN") != null) {
			lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			setRequestAttribute("autoritaEsternaN", lAutN);

		}

		Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

		// Autorità esterna C

		AutoritaEsternaModel lAutC = null;
		if (lTable.get("AutC") != null) {
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
			setRequestAttribute("autoritaEsternaC", lAutC);

		}

		Option lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita(), "35");
		setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

		// Cssa

		String lCssa = null;
		if (lTable.get("NotCssa") != null) {
			lCssa = ((NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " "
					+ ((NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
			setRequestAttribute("Cssa", lCssa);
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());

		}

		// Ufficio TDS

		String UffTDS = null;
		if (lTable.get("UffTDS") != null) {
			UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
			setRequestAttribute("UffTDS", UffTDS);

		}

		// Ufficio UDS

		String UffUDS = null;
		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			setRequestAttribute("UffUDS", UffUDS);

		}

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance()
				.getMotivoProvvedimentoProrogaUlteriorePeriodo());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "TDS");
		setRequestAttribute("tipoUfficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		return PG_LOAD_INSERISCI_MA_PROROGA_ULT_PERIODO;
	}

}