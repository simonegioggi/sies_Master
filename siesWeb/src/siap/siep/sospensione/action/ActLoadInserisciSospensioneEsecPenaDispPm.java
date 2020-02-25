package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciSospensioneEsecPenaDispPm
 * </p>
 * <p>
 * Description: Classe Action per la load inserimento di Sospensione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadInserisciSospensioneEsecPenaDispPm extends ActionSiap implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// ==========================================================================
		// ATTENZIONE!! Questa action viene invocata due volte:
		// - una prima volta per l'inserimento dei dati della sospensione
		// - una seconda volta per l'inserimento dei dati del provvedimento (destinatari...)
		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
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

		isEventoNonValidato();

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

		// ==========================================================================
		// Recupero la Posizione Giuridica e il luogo di detenzione
		// ==========================================================================
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

		// PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel(lPos.getPosizioneGiuridica());
		setRequestAttribute("posizioneluogoaltra", lPos);

		/*
		 * //========================================================================== // Recupero l'attuale
		 * pena residua: // se libero: ultima pena, validata o meno // altrimenti: ultima pena validata
		 * //========================================================================== IPenaResidua
		 * lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel lPenaResidua = null; if (
		 * lPosizione.getCodPosizioneGiuridica() != null && (
		 * !lPosizione.getCodPosizioneGiuridica().equals("07") &&
		 * !lPosizione.getCodPosizioneGiuridica().equals("16") &&
		 * !lPosizione.getCodPosizioneGiuridica().equals("17") &&
		 * !lPosizione.getCodPosizioneGiuridica().equals("46") &&
		 * !lPosizione.getCodPosizioneGiuridica().equals("47") ) ) { // non libero lPenaResidua =
		 * lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo); } else { lPenaResidua =
		 * lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo); }
		 * 
		 * if(lPenaResidua == null) { RedirectTo lRedirigi = new RedirectTo();
		 * lRedirigi.setPage(IWebConstants.PG_MAIN); setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?");
		 * lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena"+"&" +
		 * ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		 * 
		 * return IWebConstants.PG_MESSAGE; }
		 */
		PenaResiduaModel lPenaResidua = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResidua = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResidua == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("penaresidua", lPenaResidua);

		//
		if (lPenaResidua != null && lPenaResidua.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		// ==========================================================================
		// Controlla se per il fascicolo selezionato esiste un ordine di esecuzione
		// (o legge simeone) o un Provvedimento di Esecuzione di Pene Concorrenti
		// codice motivo 0222, 0223, 0224, 0277 (Cumulo)
		// Infatti la form può essere utilizzata anche per la richiesta restituzione
		// OE
		// ==========================================================================
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		boolean lFlagOrdineEsecuzione = lCtrl.ExEsisteOrdineEsecuzioneByFascicoloSiep(lIdFascicolo);
		setRequestAttribute("esisteOE", "" + lFlagOrdineEsecuzione);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Combo motivo sospensione
		Option lOption = new Option(DecodificheManager.getInstance().getMotivoSospensionePm());
		setRequestAttribute("contenuto", "" + lOption);

		// Combo Autorità esterne
		Option lOptionAutoritaE = null;
		lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// MEV_66: aggiunte 5 uffici GE Competente (lato DB) e lista uepe
		// Combo AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		IDecodifiche lCtrlDec = SICOLookupRemote.getDecodificheRemote();
		Collection lCol = lCtrlDec.ExRicercaDecodifiche(
				new DecodificheModel("", "", "TIPO_UFFICIO_SOSP", "0002", "", "", "", "", ""));
		setRequestAttribute("lColAutEmi", lCol);

		lOption = new Option(DecodificheManager.getInstance().getTipoUffEsePenEstSerSocMin(), "-");
		String[] lFiltro = new String[3];
		lFiltro[0] = "-";
		lFiltro[1] = "UEPE";
		lFiltro[2] = "USSM";
		lOption.setFilter(lFiltro);
		setRequestAttribute("uepe", "" + lOption);
		// FINE MEV_66

		// Lista Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Combo autorità avvocati
		// lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),"22");
		// a7-rr-030
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();

		// MEV_66: aggiunta combo di scelta tds + tdsm
		Option lOptionUffSorv = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM());
		lOptionUffSorv.setFilter(new String[] { "-", "TDS", "TDSM" });
		setRequestAttribute("ufficioTdS", "" + lOptionUffSorv);

		// MEV_66: aggiunta combo di scelta uds + udsm
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM());
		lOptionUffSIUS.setFilter(new String[] { "-", "UDS", "UDSM" });
		setRequestAttribute("ufficioMdS", "" + lOptionUffSIUS);

		// ==========================================================================
		// Ricerco se Esiste un Ordine di Esecuzione(06) inserito e validato
		// per indicare alla form che esiste un ordine di esecuzione
		// ==========================================================================
		EventoModel lEveModRic = new EventoModel();
		lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveModRic.setCodTipoEvento("01");
		lEveModRic.setCodTipoProvvedimento("06");

		try {
			Vector lEveVect = lCtrlEve.ExRicercaEventoTipoEveTipoProvMot(lEveModRic, "S");
			if (lEveVect.size() > 0)
				setRequestAttribute("eventoOE", "S");
			else
				setRequestAttribute("eventoOE", "N");
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Evento OE non trovato");
		}

		// Ricerca evento già inserito
		// La ricerca comprende ultimo Evento sia con CodTipoProv = 04 che 09.
		// Luigi 10-10-2005

		// ==========================================================================
		// Ricerco l'ultimo (data inserimento) evento legato a una sospensione del
		// PM (0900, 0901, 0902, 0903)
		// Attenzione la select recupera anche gli eventi annullati ma poi viene
		// testato se l'evento è non volidato
		// ==========================================================================
		EventoModel lEveRic = new EventoModel();
		String[] lMotivo = { "0900", "0900", "0901", "0901", "0902", "0902", "0903", "0903" };
		String[] lTipoProv = { "04", "09", "04", "09", "04", "09", "04", "09" };
		lEveRic.setCodTipoEvento("01");
		lEveRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		EventoModel lEve = new EventoModel();
		lEve = lCtrlEve.ExRicercaEventoUnicoTipoProvTipoMot(lEveRic, lTipoProv, lMotivo);

		// ==========================================================================
		// Se esiste già una sospensione del PM non validata recupera l'ultima
		// ordinanza a sistema
		// ==========================================================================
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		DecretoOrdinanzaSiepModel lDecOrd = null;
		if (lEve != null && (lEve.getFlagDocumentoRegistrato() == null
				|| lEve.getFlagDocumentoRegistrato().equals("N"))) {
			// Ricerca l'ultimo decreto non elaborato su DECRETO_ORDINANZA_SIEP **
			IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
			lDecOrd = lCtrlDecOrd.ExRicercaUltimaDecretoOrdinanzaSiepByIdFascicolo(lIdFascicolo);

			// Recupero l'evento associato al dectreto
			EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey(lDecOrd.getIdEventoGenerato());
			setRequestAttribute("evento", lEvento);

			// Recupero le notifiche associate alla sospensione se presenti
			lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(lEve.getIdEvento());
			if (lEveNot != null) {
				setRequestAttribute("eventonotifica", lEveNot);

				IUfficio lUff = SICOLookupRemote.getUfficioRemote();

				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();

				NotificaModel[] lNotifiche = lEveNot.getNotifiche();
				for (int i = 0; i < lNotifiche.length; i++) {
					// TDS
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("NC")) {
						UfficioModel lUffModTDS = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffTDS", lUffModTDS);
					}

					// ISTITUTO
					if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
						IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
								lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
						setRequestAttribute("Istituto", lModIst);
					}

					// UFFICIALI GIUDIZIARI PER CONDANNATO
					if (lNotifiche[i].getAutoritaEsterna() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
						setRequestAttribute("autoritaUGC", lNotifiche[i].getAutoritaEsterna());
						setRequestAttribute("NoteUGC", lNotifiche[i].getNote());
						setRequestAttribute("SedeUGC", lNotifiche[i].getAutoritaEsterna().getDescrSede());
					}

					// AUTORITA DI POLIZIA
					if (lNotifiche[i].getAutoritaEsterna() != null
							& lNotifiche[i].getCodTipoNotifica().equals("ND")) {
						setRequestAttribute("autorita", lNotifiche[i].getAutoritaEsterna());
						setRequestAttribute("Noteautorita", lNotifiche[i].getNote());
						setRequestAttribute("Sedeautorita",
								lNotifiche[i].getAutoritaEsterna().getDescrSede());
					}

					// GE
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("NG")) {
						UfficioModel lUffModTDS = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffGE", lUffModTDS);
						setRequestAttribute("SedeGE", lUffModTDS.getDescrComune());
					}

					// cssa
					if (lNotifiche[i].getCSSA() != null && lNotifiche[i].getCodTipoNotifica().equals("C")) {
						CSSAModel lModCssa = lNotifiche[i].getCSSA();
						setRequestAttribute("Cssa", lModCssa);
					}
				}
			}
		}

		String lFlagDec = (lDecOrd == null ? "N" : "S");
		setRequestAttribute("flagdecretoordinanza", lFlagDec);

		setRequestAttribute("decretoordinanza", lDecOrd);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_INSERISCI_SOSPENSIONE_ESEC_PM; // restituisce la jsp di VIEW
	}

}