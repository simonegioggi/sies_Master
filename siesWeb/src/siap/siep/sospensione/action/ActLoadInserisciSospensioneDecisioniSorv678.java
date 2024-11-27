package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.model.DecodeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * Nuova funzione per registrare la sospensione della sorveglianza disposta secondo l'art. 678
 *
 * La Action viene richiamata sia dalla griglia delle sospensioni che dalla ActInserimento dopo la
 * registrazione/selezione del provvedimento della SORVEGLIANZA
 *
 * @since MEV_2019-09 - SIEP 03.2024
 */
public class ActLoadInserisciSospensioneDecisioniSorv678 extends ActionSiap implements ICostantiSospensione {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		String tipoOperazione = "INSERIMENTO";
		if (!isRequestParameterNullObj("tipoOperazione"))
			tipoOperazione = getRequestStringParameter("tipoOperazione");
		setRequestAttribute("tipoOperazione", tipoOperazione);

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		this.isFascicoloSiepDiCompetenza();

		if (isFascicoloNonValidato() || isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		/******************************* Posizione Giuridica **********************************/
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		/******************************* Pena Complessiva *****************************/
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03"))
				lFlagErgastolo = "S";
			else if (lPenComMod.getCodTipoPenaDetentiva().equals("04"))
				lFlagErgastolo = "D";
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);
		/******************************* Fine Pena Complessiva ************************/
		/*********************************** Pena Residua ***************************/
		// Controllo Esistenza pena residua per quel fascicolo
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();

		// provvedimento della sorveglianza
		if ("INSERIMENTO".equals(tipoOperazione))
			this.isEventoNonValidato();

		if (!lPosizione.getCodPosizioneGiuridica().equals("07")
				&& !lPosizione.getCodPosizioneGiuridica().equals("16")
				&& !lPosizione.getCodPosizioneGiuridica().equals("17")
				&& !lPosizione.getCodPosizioneGiuridica().equals("46")
				&& !lPosizione.getCodPosizioneGiuridica().equals("47")) // NOT LIBERO
		{
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else {
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
		String lErrore = null;
		String lAzioneChiamante = null;

		if (lPenaResMod == null) {
			// lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
			lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
			lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
		} else if ((!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("16")
				&& !lPosizione.getCodPosizioneGiuridica().equals("17")
				&& !lPosizione.getCodPosizioneGiuridica().equals("46")
				&& !lPosizione.getCodPosizioneGiuridica().equals("47")) // non è libero
				&& (lPenaResMod.getDataInizio() == null // non ha le date
						|| lPenaResMod.getDataFine() == null)
				&& lFlagErgastolo.equals("N")) {
			lErrore = "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica";
			lAzioneChiamante = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
		}

		if (lErrore != null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
			lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
					+ getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("penaresidua", lPenaResMod);

		// Magistrato assegnatario eventualòmenet sovrescritto se stiamo sul fasicolo
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistratocompetente", lMagMod);

		// =======================================================================================
		// In caso di modifica recupero i dati per poi preselezionare i valori nelle combo
		//
		MisuraAlternativaModel lMisAlModToChange = null;
		EventoModel lEveSorv = null;
		UfficioModel lUffEmittente = null;
		// AutoritaEsternaModel lAutEsternaE = null;
		IstitutoDetenzioneModel lIstituto = null;
		CSSAModel lCssa = null;
		UfficioModel lUffSorv = null;
		UfficioModel lTribSorv = null;
		if ("MODIFICA".equals(tipoOperazione)) {
			// Blocco l'editabilita' in modifica
			setRequestAttribute("dataeditabile", "N");

			BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

			// ricerca evento notifica
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(idEvento);
			setRequestAttribute("eventonotifica", lEveNotMod);

			// ricerca evento notifica della SORV (dec/ord)
			lEveSorv = lCtrlEvento.ExRicercaEventoByKey(lEveNotMod.getEvento().getEveIdEvento());
			// setRequestAttribute("eventonotifica", lEveMod);

			// ricerca misura per il fascicolo
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			lMisAlModToChange = lMisAltCtrl
					.ExRicercaMisuraAlternativaByIdEvento(lEveNotMod.getEvento().getEveIdEvento());
			setRequestAttribute("misuraalternativaToChange", lMisAlModToChange);

			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			lUffEmittente = lCtrlUffEmi.getUfficioByKey(lMisAlModToChange.getChiaveUfficioFascicoloSius());
			setRequestAttribute("UfficioEmittente", lUffEmittente);

			// Magistrato Firmatario - Sovrascrivo il MAG competente caricato del super()
			IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel lMagi = lCtrlM
					.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());

			MagistratoCompetenteMagistratoModel lMagModCompEve = new MagistratoCompetenteMagistratoModel();
			lMagModCompEve.setMagistrato(lMagi);
			setRequestAttribute("magistratocompetente", lMagModCompEve);

			// Ciclo sui destinatari
			NotificaModel[] listaNotifiche = lEveNotMod.getNotifiche();
			for (int i = 0; i < listaNotifiche.length; i++) {
				NotificaModel lNotifica = listaNotifiche[i];
				if ("E".equals(lNotifica.getCodTipoNotifica()) && lNotifica.getAutoritaEsterna() != null) {
					// lAutEsternaE = lNotifica.getAutoritaEsterna();
					setRequestAttribute("NotificaAutoritaEsternaE", lNotifica);
				} else if ("C".equals(lNotifica.getCodTipoNotifica())
						&& lNotifica.getAutoritaEsterna() != null) {
					// lAutEsternaC = lNotifica.getAutoritaEsterna();
					setRequestAttribute("NotificaAutoritaEsternaC", lNotifica);
				} else if (lNotifica.getIstitutoDetenzione() != null) {
					lIstituto = lNotifica.getIstitutoDetenzione();
					setRequestAttribute("NotificaIstDetenzione", lIstituto);
				} else if (lNotifica.getCSSA() != null)
					lCssa = lNotifica.getCSSA();
				else if (lNotifica.getUfficio() != null
						&& ("TDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
								|| "TDSM".equals(lNotifica.getUfficio().getCodTipoUfficio()))) {
					lTribSorv = lNotifica.getUfficio();
					setRequestAttribute("DestTribunaleSorv", lTribSorv);
				} else if (lNotifica.getUfficio() != null
						&& ("UDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
								|| "UDSM".equals(lNotifica.getUfficio().getCodTipoUfficio()))) {
					lUffSorv = lNotifica.getUfficio();
					setRequestAttribute("DestUfficioSorv", lUffSorv);
				} else if (lNotifica.getAvvSiep() != null) {
					setRequestAttribute("NotificaAvv", lNotifica);
				} else if (lNotifica.getAvvSiep() != null) {
					setRequestAttribute("NotificaAvv", lNotifica);
				}
			}
		}

		// Tipo provvedimento
		Option lOptionTipoProvvSorv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOptionTipoProvvSorv.setFilter(new String[] { "-", "02", "03" }); // solo DECRETO o ORDINANZA
		if (lEveSorv != null)
			lOptionTipoProvvSorv.setSelected(lEveSorv.getCodTipoProvvedimento());
		else
			lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionTipoProvvSorv);

		// OGGETTO DECISIONE
		// Option lOptionOggetto = new Option(DecodificheManager.getInstance().getOggettiDecisioneSosp678());
		// setRequestAttribute("motivoProvv", "" + lOptionOggetto);
		Collection lMotiviColl = DecodificheManager.getInstance().getOggettiDecisioneSosp678();
		// Scorro la collection e forzo la descrizione per alcuni codici perchè lato SIEP NON sono parlanti
		Iterator<DecodeModel> iterMot = lMotiviColl.iterator();
		Collection lMotiviCollSIEP = new Vector();
		while (iterMot.hasNext()) {
			DecodeModel lDecode = iterMot.next();
			DecodeModel lDecodeNew = new DecodeModel();
			lDecodeNew.setCode(lDecode.getCode());
			//lDecodeNew.setDescription("Applicazione Provvisoria " + lDecode.getDescription());
			lDecodeNew.setDescription("Applicazione " + lDecode.getDescription());
			lMotiviCollSIEP.add(lDecodeNew);
		}
		Option lOptionOggetto = new Option(lMotiviCollSIEP);
		if (isUfficioMinorenni()) { // TDSM
			// lOptionOggetto.setFilter(new String[] {"-","0695"});
			lOptionOggetto.setFilter("0695");
		} else {
			// lOptionOggetto.setFilter(new String[] {"-","0684"});
			lOptionOggetto.setFilter("0684");
		}

		if (lEveSorv != null)
			lOptionOggetto.setSelected(lEveSorv.getCodMotivo());
		setRequestAttribute("motivoProvv", "" + lOptionOggetto);

		// Tipo Ufficio SIUS Emittente.
		Option lOptionSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		lOptionSIUS.setValueBlankItem("-");
		lOptionSIUS.setAddBlankItem(Option.BLANK_ITEM);
		if (lUffEmittente != null) // se da verbale si precarica come dest l'ufficio emittente il provv sorv
			lOptionSIUS.setSelected(lUffEmittente.getCodTipoUfficio());
		setRequestAttribute("tipoUfficioSIUS", "" + lOptionSIUS);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		// Autorità esterna
		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + lOptionAutorita);

		if (lCssa != null)
			setRequestAttribute("daticssa", lCssa);

		// pagina di ritorno
		return PG_LOAD_INSERISCI_SOSP_DECISIONI_SORVEGLIANZA_678;
	}

}