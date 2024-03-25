package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.model.DecodeModel;
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
 * MEV_9-SIEP Si agggiunge la gestione dell'ammissione provvisoria anche per la semilibertà.
 *
 * Da verificare:questa action viene richiamata -- dalla griglia della semilibertà -- dalla funzione di
 * modifica -- dal dettaglio del verbale di sottomissione (verificare se previsto)
 *
 */
public class ActLoadInserisciMAAmmProvSemiliberta extends ActAmmissioneProvvisoria {

	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getAmmissioneProvvisoria();
		if (!lRitorno.equals(""))
			return lRitorno;

		// Se provengo dal dettaglio del verbale mi viene passato l'id_della misura legata al verbale.
		// Carico i dati e li passo alla form (non modificabili)
		MisuraAlternativaModel lMisAlModAMM = new MisuraAlternativaModel();
		if (!isRequestParameterNullEmptyObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);

			// recupero la misura
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
			setRequestAttribute("misuraalternativa", lMisAlModAMM);

			// Recupero il verbale - legato alla misura
			EventoModel lEveVer = new EventoModel();
			IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
			lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(lMisAlModAMM.getEveIdEvento(),
					"07", "18", "0314");

			IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
			setRequestAttribute("verbale", lVerbMod);

			// Recupero il CSSA che ha redatto il verbale se presente (???)
			if (lVerbMod.getCssIdCssa() != null) {
				ICSSA lCSSACtl = SICOLookupRemote.getCSSARemote();
				CSSAModel lCSSAModel = lCSSACtl.getCSSAByKey(lVerbMod.getCssIdCssa());
				setRequestAttribute("daticssa", lCSSAModel);
			}

			// ???????? SERVE??
			UfficioModel lUffEmiMod = new UfficioModel();
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModAMM.getChiaveUfficioFascicoloSius());
			setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
		}

		String tipoOperazione = "INSERIMENTO";
		if (!isRequestParameterNullObj("tipoOperazione"))
			tipoOperazione = getRequestStringParameter("tipoOperazione");
		setRequestAttribute("tipoOperazione", tipoOperazione);

		// =======================================================================================
		// In caso di modofica recuperai dati per poi preselezionere i valori nelle combo
		//
		MisuraAlternativaModel lMisAlModToChange = null;
		EventoModel lEveSorv = null;
		UfficioModel lUffEmittente = null;
		AutoritaEsternaModel lAutEsternaE = null;
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
					lMisAlModToChange.getEveIdEvento(), "07", "18", "0314", "S"); // MEV_9 mi interessano i
																					// validati!!!

			IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
			setRequestAttribute("verbale", lVerbMod);

			if (lVerbMod != null && lVerbMod.getIdVerbale() != null) {
				setRequestAttribute("misuraalternativa", lMisAlModToChange);
				setRequestAttribute("misuraalternativaToChange", null);
			}
		}

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
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage()
					+ " Impossibile eseguire la richiesta. Nessun avvocato associato al fascicolo.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// =================================
		// Caricamento delle combo
		// =================================
		// Tipo Ufficio SIUS
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		if (lUffEmittente != null) // se da verbale si precarica come dest l'ufficio emittente il provv sorv
			lOptionUffSIUS.setSelected(lUffEmittente.getCodTipoUfficio());

		lOptionUffSIUS.setValueBlankItem("-");
		lOptionUffSIUS.setAddBlankItem(Option.BLANK_ITEM);
		setRequestAttribute("comboTipoUfficioSIUS", "" + lOptionUffSIUS);

		// Tipo Provvedimento
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

		// Setto il campo codice motivo provv SIUS
		// FIXME recuperare i codici corretti. Da capire se cambia tra PM e PMM
		Option lOptionMotivo = new Option(
				DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
		Collection lMotiviColl = null;
		if (isUfficioMinorenni())
			lMotiviColl = DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvSemilibPmm();
		else
			lMotiviColl = DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvSemilibPm();

		// Scorro la collection e forzo la descrizione per alcuni codici perchè lato SIEP NON sono parlanti
		Iterator<DecodeModel> iterMot = lMotiviColl.iterator();
		Collection lMotiviCollSIEP = new Vector();
		while (iterMot.hasNext()) {
			DecodeModel lDecode = iterMot.next();
			DecodeModel lDecodeNew = new DecodeModel();
			lDecodeNew.setCode(lDecode.getCode());

			// AFFIDAMENTO
			if ("0683".equals(lDecode.getCode()))
				lDecodeNew.setDescription("Applicazione Provvisoria " + lDecode.getDescription());
			else if ("0694".equals(lDecode.getCode()))
				lDecodeNew.setDescription("Applicazione Provvisoria " + lDecode.getDescription());
			else
				lDecodeNew.setDescription(lDecode.getDescription());

			lMotiviCollSIEP.add(lDecodeNew);
		}
		lOptionMotivo = new Option(lMotiviCollSIEP);

		// Preselezione in caso di modifica
		if (lEveSorv != null)
			lOptionMotivo.setSelected(lEveSorv.getCodMotivo());

		setRequestAttribute("motivoProvv", "" + lOptionMotivo);

		// Destinatari - Se da verbale o modifica precarico il CSS che ha redatto il verbale
		if (lCssa != null)
			setRequestAttribute("daticssa", lCssa);

		// Autorita' esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		if (lAutEsternaE != null) {
			lOptionAutoritaE.setSelected(lAutEsternaE.getCodTipoAutorita());
			setRequestAttribute("autoritaEsternaE", lAutEsternaE);
		}
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		//
		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);

		// SERVE???
		setRequestAttribute("filtroMinorenni", getFiltroMinorenni());

		setRequestAttribute("tipomisura", "SEMILIBERTA");

		return PG_LOAD_INSERISCI_MA_AMM_PROVV_SEMILIBERTA;
	}

}