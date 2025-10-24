package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInsComNuovoResPenaRidetPenaAltro
 * </p>
 * <p>
 * Description: Classe Action per la Load Inserimento Comunicazione Nuovo residuo pena nel caso di
 * Rideterminazione Pena Altro Questa Action viene invocata dalla form della griglia dei provvedimenti
 * (stampe) della rideterminazione pena.
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

public class ActLoadInsComNuovoResPenaRidetPenaAltro extends ActionSiap implements ICostantiCalcoloPena {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Sezione con i controlli preliminari
		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// Controllo Fascicolo di Competenza
		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

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
		// Recupero l'evento di computo a cui collegare l'evento di comunicazione
		// e lo passo alla form di visualizzazione
		// ==========================================================================
		BigDecimal lIdEveComputo = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(lIdEveComputo);

		this.setRequestAttribute("aEventoComputo", lEveComputo);

		// ===============================================
		// Ricerco eventuale provvedimeto altra autorità
		// ===============================================
		if (lEveComputo.getEveIdEvento() != null) {
			EventoModel lEveAltraAut = lCtrlEvento.ExRicercaEventoByKey(lEveComputo.getEveIdEvento());
			this.setRequestAttribute("aEventoAltraAut", lEveAltraAut);
		}

		// ============================================
		// Recupero il campo nota
		// ============================================
		ICampoNota lCtrlCampoNota = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCampoNotaModel = lCtrlCampoNota
				.ExRicercaCampoNotaByIdEvento(lEveComputo.getIdEvento());

		this.setRequestAttribute("aCampoNota", lCampoNotaModel);

		// Verifico se esiste evento non validato
		// ==========================================================================
		// Verifico se presente evento non validato ad eccezione ovviamente del
		// computo corrente che potrebbe essere non validato
		// Sperimentale: se l'evento è un OE ne carico il dettaglio
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Verifico la presenza di eventi non validati");
		EventoModel lUltimoEve = this.isEventoNonValidatoRidetPenaAltro(lEveComputo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lUltimoEve = " + lUltimoEve);
		if (lUltimoEve != null && lUltimoEve.getIdEvento().compareTo(lEveComputo.getIdEvento()) != 0 // escludo
																										// l'evento
																										// di
																										// computo
				&& !lUltimoEve.getCodTipoEvento().equals("05") // Richiesta Istruttoria
				&& lUltimoEve.getCodMotivo() != null && !lUltimoEve.getCodMotivo().equals("0076") // 0076 =
																									// Concessione
																									// Liberazione
																									// Anticipata
				&& !lUltimoEve.getCodMotivo().equals("2130") // 2130 = Concessione Liberazione Anticipata
				&& (lUltimoEve.getFlagDocumentoRegistrato() == null
						|| "N".equals(lUltimoEve.getFlagDocumentoRegistrato()))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Esiste evento non validato: " + lUltimoEve);
			// verificare i codici
			if (lUltimoEve.getCodTipoEvento().equals("01")
					&& lUltimoEve.getCodTipoProvvedimento().equals("12")
					&& (lUltimoEve.getCodMotivo().equals("0975") || lUltimoEve.getCodMotivo().equals("0976")
							|| lUltimoEve.getCodMotivo().equals("0977")
							|| lUltimoEve.getCodMotivo().equals("0978")
							|| lUltimoEve.getCodMotivo().equals("0979")
							|| lUltimoEve.getCodMotivo().equals("0980")
							|| lUltimoEve.getCodMotivo().equals("0981")
							|| lUltimoEve.getCodMotivo().equals("0982")
							|| lUltimoEve.getCodMotivo().equals("0983")
							|| lUltimoEve.getCodMotivo().equals("0984")
							|| lUltimoEve.getCodMotivo().equals("0985")
							|| lUltimoEve.getCodMotivo().equals("0986")
							|| lUltimoEve.getCodMotivo().equals("0991")
							|| lUltimoEve.getCodMotivo().equals("0992"))) {
				// Evento corrente di Comunicazione - Ne carico il dettaglio
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trattasi di Comunicazione, carico il dettaglio");
				String lPage = "";
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.calcolopena.action.ActLoadDettComNuovoResPenaRidetPenaAltro&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lUltimoEve.getIdEvento();
				return lPage;
			} else {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento '" + lUltimoEve.getDescrTipoProvvedimento() + " - "
								+ lUltimoEve.getDescrMotivo()
								+ "' NON validato. Validarlo o cancellarlo e rieseguire la funzione.");
			}
		}

		// ======================================================
		// Controllo esistenza almeno un avvocato per fascicolo.
		// ======================================================
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;

		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					e.getMessage() + " Impossibile eseguire il Provvedimento.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		// Passo gli avvocati alla form
		setRequestAttribute("avvocati", lAvvocati);

		// ==========================================================================
		// Recupero la Posizione Giuridica
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

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Ricerca l'ultima pena residua per quel fascicolo
		// n.b. Da verificare. Questo OE viene emesso a seguito di un provvedimento
		// di rideterminazione pena che ha ricalcolato la pena e al quale deve
		// essere collegato. Per cui in teoria la PR da recuperare ed eseguire
		// dovrebbe essere quella rideterminata con il computo.
		// Tuttavia questo OE può essere emesso anche in un secondo momento
		// passando per il dettaglio del provvedimento di computo. In questo
		// caso la pena potrebbe essere stata modificata da provvedimenti
		// intermedi per cui andrebbe eseguita quest'ultima pena. Anche se in
		// questo caso che senso ha emettere questo OE? In teoria quindi l'unico
		// evento che può aver modificato al pena dovrebbe essere una correzione
		// (0219).
		// ==========================================================================
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null // && 1==2
				|| (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
						&& !lPos.getPosizioneGiuridica().isLibero()
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-")
						&& lPenaResMod != null && lPenaResMod.getDataInizio() == null)) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena.
			// Impossibile eseguire l'ordine di esecuzione.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			if (lPenaResMod == null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			} else {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare incoerente con Posizione Giuridica. Eseguire Calcolo della pena?");
			}

			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		/*
		 * 07/06/2010 Su indicazione di Michele Testa, La Pos. Giur. Libero va gestita in ogni caso. else if (
		 * lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") && (
		 * CalendarUtil.getTotGiorni(lPenaResMod.getQuantumReclusione())>0 ||
		 * CalendarUtil.getTotGiorni(lPenaResMod.getQuantumArresto())>0 ) ) { RedirectTo lRedirigi = new
		 * RedirectTo(); lRedirigi.setPage(IWebConstants.PG_MAIN);
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		 * "Posizione Giuridica Libero, ma pena rideterminata non nulla! Impossibile emettere comunicazione."
		 * );
		 *
		 * return IWebConstants.PG_MESSAGE; }
		 */

		// ==========================================================================
		// !!!! POSIZIONI GIURIDICHE ATTUALMENTE GESTITE IN QUESTA FUNZIONE !!!!
		// 16 Libero in Differimento Pena
		// 17 Libero in Differimento Pena (Provvisorio)
		// 46 Libero in Sospensione
		// 47 Libero in Sospensione DPR 309/90
		// 49 Sospensione provvisoria Arresti Domiciliari (??)
		// 10 - Libero (new! 4.0upd02) solo se pena residua = 0
		// ==========================================================================
		boolean isPenaGiaEspiata = false;
		if (lPos != null && lPos.getPosizioneGiuridica() != null
				&& (!lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("16") // Libero in
																							// Differimento
																							// Pena
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("17") // Libero in
																									// Differimento
																									// Pena
																									// (Provvisorio)
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("46") // Libero in
																									// Sospensione
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("47") // Libero in
																									// Sospensione
																									// DPR
																									// 309/90
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("49") // Sospensione
																									// provvisoria
																									// Arresti
																									// Domiciliari
																									// (??)
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // Libero
																									// (Commento
																									// 07/06/2010)
				)) {
			if (!lPos.getPosizioneGiuridica().isLibero() && lPenaResMod.getDataFine() != null
					&& DateUtils.isGreater(DateUtils.getSysDate(), lPenaResMod.getDataFine())) {
				// new d.f. 16/09/2014 su segnalazione di Nunzia
				// Soggetto con pena in esecuzione ma già terminata al momento del
				// provvedimento (di fatto è libero). Devo consentire comunque di emettere
				// una Comunicazione in quanto l'ordine di scarcerazione non è corretto,
				// il soggetto è già scarcerato
				isPenaGiaEspiata = true;
			} else {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Posizione Giuridica non gestita, impossibile procedere!");

				return IWebConstants.PG_MESSAGE;
			}
		}

		// ==========================================================================
		// Recupero il Magistrato a cui è assegnato il fascicolo
		// ==========================================================================
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// ==========================================================================
		// Caricamento combo
		// ==========================================================================
		// Combo Autorità (dominio TIPO_AUTORITA senza filtro)
		Option lOptionAutorita = null;
		lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("tipoAutorita", "" + lOptionAutorita);

		// ==========================================================================
		// Caricamento delle Combo da Visualizzare sulla maschera
		// ==========================================================================
		// Destinatari per la Notifica (22 = UNEP)
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// Destinatari per l'Esecuzione
		lOption = null;
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunto controllo preventivo
			if (lPos.getAltraCausa() != null) {
				if (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
				} else {
					if (lPos.getAltraCausa().getIstitutoDetenzione() != null)
						lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
								lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
				}
			}
		} else {
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")
					|| isPenaGiaEspiata) {
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}

		if (lOption == null)
			lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());

		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// ======================
		//
		// ======================
		setRequestAttribute("penaresidua", lPenaResMod);

		return PG_LOAD_INS_COM_NUOVO_RES_PENA_RIDET_PENA_ALTRO;
	}
}