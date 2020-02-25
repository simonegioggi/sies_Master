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
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
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
 * Title: ActLoadInserisciMADetDomSpecAmmiPeriodo
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraAlternativa Detenzione Domiciliare Speciale
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
public class ActLoadInserisciMADetDomSpecAmmiPeriodo extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controllo Validazione Fascicolo
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

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Esistenza PENA RESIDUA
		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		if (llPenMod == null || llPenMod.getIdPenaResidua() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Eseguire prima il calcolo della pena. Impossibile eseguire la misura alternativa.");

		setRequestAttribute("penaresidua", llPenMod);

		if (llPenMod != null && llPenMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		} else {
		}

		// Ricerca Ultima Ordinanza di tipo Concessione legata al Fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = null;

		if (this.isRequestParameterNullObj("warning")) {
			lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaConcessaCorrenteByIdFascicolo(lFascMod
					.getIdFascicoloSiep());

			if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
				setRequestAttribute("misuraalternativa", lMisAlModConcessa);

				UfficioModel lUffEmiMod = getUfficioByCodUfficio(lMisAlModConcessa
						.getChiaveUfficioFascicoloSius());

				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

				// Controllo Coerenza Richiesta con Ordinanza TDS
				if (lMisAlModConcessa != null) {
					if (lMisAlModConcessa.getCodTipoDecisione() == null
							|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
							|| lMisAlModConcessa.getCodNaturaDecisione() == null
							|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
							|| lMisAlModConcessa.getCodTipoMisura() == null
							|| !lMisAlModConcessa.getCodTipoMisura().equals("0012")) {
						// set goto page set flag misura
						setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
						setRequestAttribute(
								IWebConstants.MESSAGE_TEXT,
								"Ordinanza assente o dati incoerenti, si vuole procedere all'inserimento dell' Ordinanza e alla contestuale emissione del Provvedimento?");

						return "/jsp/files/warning.jsp";
					}
				}
			} else {
				setRequestAttribute("flagmisura", "N");
			}
		} else {
			setRequestAttribute("flagmisura", "N");
		}

		// Controllo Esistenza POSIZIONE GIURIDICA
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAlt = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosLuoAlt = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
						.getIdFascicoloSiep());

		if (lPosLuoAlt == null || lPosLuoAlt.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Al Procedimento N."
					+ lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
					+ " non à stata associata una Posizione Giuridica.");

		setRequestAttribute("posizioneluogoaltra", lPosLuoAlt);

		// Ricerca Magistrato Competente
		IMagistratoCompetente lCtrlMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagiComp = lCtrlMagComp
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("magistratocompetente", lMagiComp);

		// Avvocati Associati al Fascicolo
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("avvocati", lAvvocati);

		// Ricerca evento notifica ordinanza
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		Hashtable lTable = new Hashtable();
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
			// Ricerca evento notifica riferito all'ordinanza
			lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lMisAlModConcessa.getEveIdEvento());

			// ricerca evento non riferito all'ordinanza
			EventoModel lEveModelNonOrd = lCtrlEvento.ExRicercaEventoMANonRegistratoByFascicoloSiep(
					lFascMod.getIdFascicoloSiep(), lMisAlModConcessa.getEveIdEvento());
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

		// magistrato con TIPO NOTIFICA = C
		/*
		 * if (lMisAlModConcessa != null) { String lCodMag = lMisAlModConcessa.getCodMagistrato();
		 * 
		 * MagistratoModel lMagSorvMod = new MagistratoModel(); IMagistrato lCtrlMagSorv =
		 * SICOLookupRemote.getMagistratoRemote(); lMagSorvMod =
		 * lCtrlMagSorv.ExRicercaMagistratoByCod(lCodMag);
		 * 
		 * setRequestAttribute("magistratosorveglianza", lMagSorvMod); }
		 */

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
		if (lTable.get("NotCssa") != null) {
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
		}

		// Ufficio TDS
		String UffTDS = null;
		if (lTable.get("UffTDS") != null) {
			UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
		}

		setRequestAttribute("UffTDS", UffTDS);

		// Ufficio UDS
		String UffUDS = null;
		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			setRequestAttribute("UffUDS", UffUDS);
		}

		// ISTITUTO DI DETENZIONE
		/*
		 * LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel(); ILuogoDetenzione lLuoDetCtrl =
		 * SIEPLookupRemote.getLuogoDetenzioneRemote(); lLuoMod =
		 * lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("luogodetenzione", lLuoMod);
		 */

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance()
				.getMotivoProvvedimentoDetDomSpecAmmiPeriodo());
		setRequestAttribute("motivoProvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "TDS");
		setRequestAttribute("tipoUfficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		return PG_LOAD_INSERISCI_MA_DETDOMSPECAMMIPERIODO;
	}

}