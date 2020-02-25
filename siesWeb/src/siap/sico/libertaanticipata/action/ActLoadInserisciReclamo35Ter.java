package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoLicenzePeriodiModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInserisciReclamo35Ter extends ActionSiap implements ICostantiLibertaAnticipata {

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// =================================================
		// Controllo Presenza del Fascicolo in Sessione
		// =================================================
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// =================================================
		// Controllo Validazione Fascicolo
		// =================================================
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==============================
		// Controllo Fascicolo definito
		// ==============================
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

		// =================================================
		// esistenza evento non validato
		// =================================================
		this.isEventoNonValidato();

		// =================================================
		// Posizione Giuridica
		// =================================================
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		setRequestAttribute("posizione", lPosizione);

		// =================================================
		// Pena Complessiva
		// =================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		// =================================================
		// Pena Residua
		// =================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getCodPosizioneGiuridica() != null
				&& !lPosizione.getCodPosizioneGiuridica().equals("07")) // LIBERO PRIMA (07)
		{
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else { // Validata o meno
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		if (lPenaResidua == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Reidua non presente. Impossibile eseguire la richiesta.");

		setRequestAttribute("penaresidua", lPenaResidua);

		// ==========================================================================
		// Si cerca la presenza di Ordinanze DL92 non elaborate per presentare
		// un messaggio di avviso all'utente in modo che le selezioni dalla lista
		// ==========================================================================
		// FIXME DL92 controllo presenza Ordinanze non computate. Da implementare
		// ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// LicenzaLibAnticipataModel lModelLib = new LicenzaLibAnticipataModel();
		// List lLicenze =
		// lCtrlLib.ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP(lIdFascicolo, "NE");
		//
		// if( !lLicenze.isEmpty() )
		// {
		// // IL messaggio di Alert:
		// "Attenzione vi sono ordinanze di Liberazione Anticipata già caricate e non elaborate"
		// // non deve comparire se è stata emessa e validata una comunicazione per quell'ordinanza di LA
		// Iterator iter = lLicenze.iterator();
		// while (iter.hasNext())
		// {
		// LicenzaLibAnticipataModel item = (LicenzaLibAnticipataModel) iter.next();
		// if( !item.isConProvvedimentoValidato() )
		// {
		// setRequestAttribute("FlagLicenzeNonElaborate", "S");
		// }
		// }
		// }

		// ==========================================================================
		// Caricamento delle combo
		// ==========================================================================
		// Oggetti per il caricamento delle combo Tipo Provvedimento
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		// Oggetti per il caricamento della combo Autorità Emittente
		Option lAutoritaSORV = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		lAutoritaSORV.setFilter(new String[] { "TDS", "UDS", "-" });
		setRequestAttribute("autorita", "" + lAutoritaSORV);

		// // AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		// Option lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
		// setRequestAttribute("autoritaemittente", "" + lOption);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		// ==========================================================================
		// Recupera i dati del provvedimento della sorveglianza da visualizzare
		// - EVENTO
		// - DEPOSITO_ORDINANZA_PC o DEPOSITO_DECRETO
		// - TENORE (NO non contiene dati utili da visualizzare)
		// - LICENZA_LIBANTICIPATA
		// - PERIODO_LIBANTICIPATA
		// ==========================================================================
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector<LicenzaPeriodiLibAnticipataModel> lLicenzePeriodi = null;

		// Recupero l'EVENTO della Sorveglianza, se ho selezionato un provvedimento
		// dalla lista
		BigDecimal lIdEvento = null;
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		}

		if (lIdEvento != null) {
			// EVENTO SIUS
			IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
			EventoModel lEventoModel = lEventoCtrl.ExRicercaEventoByKey(lIdEvento);
			setRequestAttribute("EventoSIUS", lEventoModel);

			// RICERCA DEPOSITO_DECRETO o DEPOSITO_ORDINANZA_PC
			if ("02".equals(lEventoModel.getCodTipoProvvedimento())) {
				IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
				DepositoDecretoModel lDepDecMod = lCtrlDepDec.ExRicercaDepositoDecretoByEvento(lIdEvento);
				setRequestAttribute("DepositoDecreto", lDepDecMod);
			} else if ("03".equals(lEventoModel.getCodTipoProvvedimento())) {
				IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep
						.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
				setRequestAttribute("DepositoOrdinanzaPc", lDepOrdMod);
			}

			// RICERCA LICENZA e PERIODI
			lLicenzePeriodi = lCtrlLib.ExRicercaLicenzeLibanticipataByEve(lIdEvento);

			if (lEventoModel != null && !"".equals(lEventoModel.toString())) {
				lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(),
						lEventoModel.getCodTipoProvvedimento());
				setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

				lAutoritaSORV = new Option(DecodificheManager.getInstance().getTipoUfficio(),
						lEventoModel.getCodTipoUfficioEmittente());
				// MERGE v10: modificato codice
				UtenteModel lUtenteMod = new UtenteModel(
						(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
				String codiceTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
				if ("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio)
						|| "PGCAP".equals(codiceTipoUfficio))
					lAutoritaSORV.setFilter(new String[] { "TDS", "TDSM", "UDS", "UDSM", "-" });
				else
					lAutoritaSORV.setFilter(new String[] { "TDS", "UDS", "-" });
				setRequestAttribute("autorita", "" + lAutoritaSORV);
			}
		}

		setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

		// MERGE v10: aggiunto codice per recupero rimedio risarcitorio ed impostati valori nella request
		// Recupero l'EVENTO per il rimedio risarcitorio
		Vector<EventoLicenzePeriodiModel> eventiLicenzePeriodiModel = lCtrlLib
				.ExRicercaRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(lIdFascicolo);
		if (!eventiLicenzePeriodiModel.isEmpty()) {
			EventoLicenzePeriodiModel eventoLicenzePeriodiModel = eventiLicenzePeriodiModel.firstElement();
			BigDecimal idEventoRR = eventoLicenzePeriodiModel.getEvento().getIdEvento();
			// EVENTO SIUS
			IEvento iEvento = SICOLookupRemote.getEventoRemote();
			EventoModel eventoModel = iEvento.ExRicercaEventoByKey(idEventoRR);
			setRequestAttribute("EventoSIUSRR", eventoModel);
			// RICERCA DEPOSITO_DECRETO o DEPOSITO_ORDINANZA_PC
			if ("02".equals(eventoModel.getCodTipoProvvedimento())) {
				IDepositoDecreto iDepositoDecreto = SIUSLookupRemote.getDepositoDecretoRemote();
				DepositoDecretoModel depositoDecretoModel = iDepositoDecreto
						.ExRicercaDepositoDecretoByEvento(idEventoRR);
				setRequestAttribute("DepositoDecretoRR", depositoDecretoModel);
			} else if ("03".equals(eventoModel.getCodTipoProvvedimento())) {
				IDepositoOrdinanzaPc iDepositoOrdinanzaPc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel depositoOrdinanzaPcModel = iDepositoOrdinanzaPc
						.ExRicercaDepositoOrdinanzaPcByEvento(idEventoRR);
				setRequestAttribute("DepositoOrdinanzaPcRR", depositoOrdinanzaPcModel);
			}
			// Ricerca licenza periodi liberazione anticipata
			Vector<LicenzaPeriodiLibAnticipataModel> lplam = lCtrlLib
					.ExRicercaLicenzeLibanticipataByEve(idEventoRR);
			setRequestAttribute("LicenzePeriodiRR", lplam);
		}
		// FINE MERGE v10

		// valore di ritorno
		return PG_LOAD_INSERISCI_RECLAMO_35TER;
	}

}