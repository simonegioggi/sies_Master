package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * Title: ActLoadInserisciMAAmmProvAffi
 * Description: Classe Action per la load inserisci di MisuraAlternativa
 *
 * Questa Action viene richiamata in due casi: - in fase di registrazione dell'Ammissione provvisoria alla
 * misura - dopo la registrazione del Verbale di sottoscrizione agli obblighi in fase di emissione dell'OS
 * (09)
 *
 * Nel primo caso la chiamata avviene dalla 'griglia' delle funzioni
 *
 * @version 1.0
 */

public class ActLoadInserisciMAAmmProvAffi extends ActAmmissioneProvvisoria {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getAmmissioneProvvisoria();
		if (!lRitorno.equals(""))
			return lRitorno;

		MisuraAlternativaModel lMisAlModAMM = new MisuraAlternativaModel();
		if (!isRequestParameterNullObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);
			if (lIdMisuraAlternativa != null) {
				IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
				setRequestAttribute("misuraalternativa", lMisAlModAMM);
				EventoModel lEveVer = new EventoModel();
				IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
				lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
						lMisAlModAMM.getEveIdEvento(), "07", "18", "0314");

				IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
				VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
				setRequestAttribute("verbale", lVerbMod);

				// Recupero il CSSA
				if (lVerbMod.getCssIdCssa() != null) {
					ICSSA lCSSACtl = SICOLookupRemote.getCSSARemote();
					CSSAModel lCSSAModel = lCSSACtl.getCSSAByKey(lVerbMod.getCssIdCssa());
					setRequestAttribute("daticssa", lCSSAModel);
				}

				UfficioModel lUffEmiMod = new UfficioModel();
				IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModAMM.getChiaveUfficioFascicoloSius());
				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
			}
		}

		// MEV_9 si aggiunge la possibilita' di modificare i dati
		String tipoOperazione = "INSERIMENTO";
		if (!isRequestParameterNullObj("tipoOperazione"))
			tipoOperazione = getRequestStringParameter("tipoOperazione");
		setRequestAttribute("tipoOperazione", tipoOperazione);

		// =======================================================================================
		MisuraAlternativaModel lMisAlModToChange = null;
		EventoModel lEveSorv = null;
		UfficioModel lUffEmittente = null;
		AutoritaEsternaModel lAutEsternaE = null;
		// AutoritaEsternaModel lAutEsternaC = null;
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

			// ricerca evento notifica
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

			MagistratoCompetenteMagistratoModel lMagMod = new MagistratoCompetenteMagistratoModel();
			lMagMod.setMagistrato(lMagi);
			setRequestAttribute("magistratocompetente", lMagMod);

			// Ciclo sui destinatari
			NotificaModel[] listaNotifiche = lEveNotMod.getNotifiche();
			for (int i = 0; i < listaNotifiche.length; i++) {
				NotificaModel lNotifica = listaNotifiche[i];
				if ("E".equals(lNotifica.getCodTipoNotifica()) && lNotifica.getAutoritaEsterna() != null) {
					lAutEsternaE = lNotifica.getAutoritaEsterna();
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

			// Provo a capire se trattasi del provvedimento che segue il verbale
			EventoModel lEveVer = new EventoModel();
			IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
			lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
					lMisAlModToChange.getEveIdEvento(), "07", "18", "0314");

			IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
			setRequestAttribute("verbale", lVerbMod);

			siesLogger.debug("lVerbMod.getIdVerbale()  = " + lVerbMod.getIdVerbale());
			if (lVerbMod != null && lVerbMod.getIdVerbale() != null) {
				siesLogger.debug("lMisAlModAMM  = " + lMisAlModAMM);
				setRequestAttribute("misuraalternativa", lMisAlModToChange);
				setRequestAttribute("misuraalternativaToChange", null);
			}
		}
		// =======================================================================================
		// MEV_9 - FINE

		// Controllo esistenza almeno un avvocato per fascicolo
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvocati);
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					e.getMessage() + " Impossibile eseguire l'Ordine di Esecuzione.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Autorita'  esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		if (lAutEsternaE != null) {
			lOptionAutoritaE.setSelected(lAutEsternaE.getCodTipoAutorita());
			setRequestAttribute("autoritaEsternaE", lAutEsternaE);
		}
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		if (lCssa != null)
			setRequestAttribute("daticssa", lCssa);

		// Riempimento ComboBoX
		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);
		// END AMBROSINO

		// new d.f. DL 146/2013
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		if (lUffEmittente != null) // se da verbale si precarica come dest l'ufficio emittente il provv sorv
			lOptionUffSIUS.setSelected(lUffEmittente.getCodTipoUfficio());
		setRequestAttribute("comboTipoUfficioSIUS", "" + lOptionUffSIUS);

		// new d.f. DL 146/2013
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
		lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		if (lEveSorv != null)
			lOptionTipoProvvSorv.setSelected(lEveSorv.getCodTipoProvvedimento());
		else
			lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		// setto il campo codice motivo
		// MEV_9
		// Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
		Option lOptionMotivo = null;
		if (isUfficioMinorenni())
			lOptionMotivo = new Option(
					DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffiPmm());
		else
			lOptionMotivo = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
		// MEV_9 - FINE
		if (lEveSorv != null)
			lOptionMotivo.setSelected(lEveSorv.getCodMotivo());

		setRequestAttribute("motivoProvv", "" + lOptionMotivo);

		setRequestAttribute("tipomisura", "AFFIDAMENTO");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_AMM_PROVVISORIA;
	}

}