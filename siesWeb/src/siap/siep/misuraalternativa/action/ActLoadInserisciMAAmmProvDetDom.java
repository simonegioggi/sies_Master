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
 * ActLoadInserisciMAAmmProvDetDom - Classe Action per la load inserisci di MisuraAlternativa
 *
 * @version 1.0
 */
public class ActLoadInserisciMAAmmProvDetDom extends ActAmmissioneProvvisoria {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getAmmissioneProvvisoria();
		if (!lRitorno.equals(""))
			return lRitorno;

		MisuraAlternativaModel lMisAlModAMM = new MisuraAlternativaModel();

		if (!this.isRequestParameterNullObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = this.getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);
			if (lIdMisuraAlternativa != null) {
				IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
				setRequestAttribute("misuraalternativa", lMisAlModAMM);

				// Data Sottoscrizione Verbale Obblighi

				EventoModel lEveVer = new EventoModel();
				IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
				lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
						lMisAlModAMM.getEveIdEvento(), "07", "18", "0314");

				IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
				VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
				setRequestAttribute("verbale", lVerbMod);

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

		MisuraAlternativaModel lMisAlModToChange = null;
		EventoModel lEveSorv = null;
		UfficioModel lUffEmittente = null;
		AutoritaEsternaModel lAutEsternaE = null;
		CSSAModel lCssa = null;
		UfficioModel lUffSorv = null;
		UfficioModel lTribSorv = null;
		IstitutoDetenzioneModel lIstituto = null;
		if ("MODIFICA".equals(tipoOperazione)) {
			// Blocco l'editabilita' in modifica
			setRequestAttribute("dataeditabile", "N");

			BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

			// ricerca evento notifica SIEP
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(idEvento);
			setRequestAttribute("eventonotifica", lEveNotMod);

			// ricerca evento notifica della SORV (dec/ord)
			lEveSorv = lCtrlEvento.ExRicercaEventoByKey(lEveNotMod.getEvento().getEveIdEvento());

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
		// =======================================================================================
		// MEV_9 - FINE

		// Controllo esistenza almeno un avvocato per fascicolo.
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

		// Autorità esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// MEV_9 in caso di modifica preselezione l'autorità esterrna
		if (lAutEsternaE != null) {
			lOptionAutoritaE.setSelected(lAutEsternaE.getCodTipoAutorita());
			setRequestAttribute("autoritaEsternaE", lAutEsternaE);
		}
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// MEV_9 x modifica
		if (lCssa != null)
			setRequestAttribute("daticssa", lCssa);

		// MEV_9 si precarca eventualmente in caso di modifica il tipo uff SORV nella combo
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		if (lUffEmittente != null)
			lOptionUffSIUS.setSelected(lUffEmittente.getCodTipoUfficio());

		lOptionUffSIUS.setValueBlankItem("-");
		lOptionUffSIUS.setAddBlankItem(Option.BLANK_ITEM);
		setRequestAttribute("comboTipoUfficioSIUS", "" + lOptionUffSIUS);

		// MEV_9 - Si caricano i dati per la combo decreto/ordinaza
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
		// MEV_9 - FINE

		// setto il campo codice motivo
		// MEV_9
		// Option lOption = new
		// Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDom());
		/*
		 * Option lOption = null; if (isUfficioMinorenni()) lOption = new
		 * Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDomPmm()); else lOption =
		 * new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDom());
		 */
		// MEV_9 - FINE

		Collection lMotiviColl = null;
		if (isUfficioMinorenni())
			lMotiviColl = DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDomPmm();
		else
			lMotiviColl = DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDom();
		// Scorro la collection e forzo la descrizione per alcuni codici perchè lato SIEP NON sono parlanti
		Iterator<DecodeModel> itMot = lMotiviColl.iterator();
		Collection nuovaColl = new Vector();
		while (itMot.hasNext()) {
			DecodeModel lDecode = itMot.next();
			DecodeModel lDecodeNew = new DecodeModel();
			lDecodeNew.setCode(lDecode.getCode());

			// DETENZIONE
			if ("0682".equals(lDecode.getCode()))
				lDecodeNew.setDescription("Applicazione Provvisoria " + lDecode.getDescription());
			else if ("0693".equals(lDecode.getCode()))
				lDecodeNew.setDescription("Applicazione Provvisoria " + lDecode.getDescription());
			else
				lDecodeNew.setDescription(lDecode.getDescription());

			nuovaColl.add(lDecodeNew);
		}
		Option lOptionMotivo = new Option(nuovaColl);

		if (lEveSorv != null)
			lOptionMotivo.setSelected(lEveSorv.getCodMotivo());

		setRequestAttribute("motivoProvv", "" + lOptionMotivo);

		// setRequestAttribute("motivoProvv", "" + lOption);

		// Riempimento ComboBoX
		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);

		setRequestAttribute("tipomisura", "DETENZIONE");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_AMM_PROVVISORIA;
	}

}