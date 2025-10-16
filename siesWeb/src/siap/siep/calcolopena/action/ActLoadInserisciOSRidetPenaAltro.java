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
 * ActLoadInserisciOSRidetPenaAltro - Classe Action per la Load Inserimento Odine di Scarcerazione per Nuovo
 * residuo pena nel caso di Rideterminazione Pena Altro Questa Action viene invocata dalla form della griglia
 * dei provvedimenti (stampe) della rideterminazione pena.
 *
 * @version 1.0
 */
public class ActLoadInserisciOSRidetPenaAltro extends ActionSiap implements ICostantiCalcoloPena {

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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
					+ " non è stato Validato. Impossibile procedere!");
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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
					+ " risulta Definito. Impossibile procedere!");
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

		// ==========================================================================
		// Recupero l'eventuale evento Altra Autorità a cui è collegato l'evento di
		// computo e lo passo alla form di visualizzazione
		// ==========================================================================
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
		// escludo l'evento di computo
		if (lUltimoEve != null && lUltimoEve.getIdEvento().compareTo(lEveComputo.getIdEvento()) != 0
		// Richiesta Istruttoria
				&& !lUltimoEve.getCodTipoEvento().equals("05")
				// 0076 = Concessione Liberazione Anticipata
				&& lUltimoEve.getCodMotivo() != null && !lUltimoEve.getCodMotivo().equals("0076")
				&& !lUltimoEve.getCodMotivo().equals("2130") // 2130 = Concessione Liberazione Anticipata
				&& (lUltimoEve.getFlagDocumentoRegistrato() == null
						|| "N".equals(lUltimoEve.getFlagDocumentoRegistrato()))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Esiste evento non validato: " + lUltimoEve);
			// verificare i codici
			if (lUltimoEve.getCodTipoEvento().equals("01") && ((lUltimoEve.getCodTipoProvvedimento()
					.equals("12")
					&& (lUltimoEve.getCodMotivo().equals("0965") || lUltimoEve.getCodMotivo().equals("0966")
							|| lUltimoEve.getCodMotivo().equals("0967")
							|| lUltimoEve.getCodMotivo().equals("0969")
							|| lUltimoEve.getCodMotivo().equals("0970")
							|| lUltimoEve.getCodMotivo().equals("0971")
							|| lUltimoEve.getCodMotivo().equals("0973")
							|| lUltimoEve.getCodMotivo().equals("0974")
							|| lUltimoEve.getCodMotivo().equals("0989")
							|| lUltimoEve.getCodMotivo().equals("0990")))
					|| (lUltimoEve.getCodTipoProvvedimento().equals("04")
							&& (lUltimoEve.getCodMotivo().equals("0963")
									|| lUltimoEve.getCodMotivo().equals("0964")
									|| lUltimoEve.getCodMotivo().equals("0968")
									|| lUltimoEve.getCodMotivo().equals("0972"))))) {
				// Evento corrente di - Ne carico il dettaglio
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Trattasi di Evento Corrente, carico il dettaglio");
				String lPage = "";
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.calcolopena.action.ActLoadDettaglioOSRidetPenaAltro&"
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
					e.getMessage() + " Impossibile eseguire l'Ordine di Esecuzione.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Impossibile eseguire l'Ordine di Esecuzione.");
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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Al Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
					+ " non è stata associata una Posizione Giuridica.");
			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// !!!! POSIZIONI GIURIDICHE ATTUALMENTE GESTITE IN QUESTA FUNZIONE !!!!
		// Detenuto (01,02,03,04,23)
		// In Misura Alternativa (11,12,13,14,15,25,27,29,41,42,43,44) isMisuraAlternativa
		// In Sospensione Misura (27,31,32,33,34,35,36,37,38,39,40,45) isMisSosp
		// ==========================================================================
		if (lPos != null && lPos.getPosizioneGiuridica() != null
		// Custodia Cautelare per Questa Causa in Regime di Detenzione
				&& (!lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("01")
						// Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
						// Espiazione Pena in Regime Carcerario
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03")
						// Arresti Domiciliari ex art. 656/10
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")
						// Custodia Cautelare in Regime di Arresti Domiciliari
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("23")
						&& !lPos.getPosizioneGiuridica().isMisSosp()
						&& !lPos.getPosizioneGiuridica().isMisuraAlternativa())) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Posizione Giuridica non gestita, impossibile procedere!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Posizione Giuridica non gestita, impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Ricerca l'ultima pena residua per quel fascicolo
		// n.b. Da verificare. Questo evento viene emesso a seguito di un provvedimento
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

		if (lPenaResMod == null || (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
				&& !lPos.getPosizioneGiuridica().isLibero()
				&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-") && lPenaResMod != null
				&& lPenaResMod.getDataInizio() == null)) {
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

		// ==========================================================================
		// 09/12/2014 aggiunto controllo sulla pena in decorrenza.
		// Se il fine pena è trascorso, deve essere emessa una comunicazione e non
		// un ordine di scarcerazione
		// ==========================================================================
		if (!lPos.getPosizioneGiuridica().isLibero() && lPenaResMod.getDataFine() != null
				&& DateUtils.isLower(lPenaResMod.getDataFine(), lEveComputo.getDataEmissione())) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La pena risulta già interamente espiata (il "
					+ DateUtils.getDateToString(lPenaResMod.getDataFine(), "dd/MM/yyyy")
					+ "), non è possibile emettere un Ordine di Scarcerazione. E' possibile emettere solo "
					+ "una Comunicazione o procedere alla Validazione del Provvedimento di rideterminazione.");

			return IWebConstants.PG_MESSAGE;

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

		// Autorità esterna altra
		Option lOptionAutoritaAltra = null;
		lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaAltra", "" + lOptionAutoritaAltra);

		// Destinatari per l'Esecuzione
		Option lAutoritaEsternaE = null;
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {

			if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
				lAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIstitutoDetenzione() != null) {
					lAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
				} else {
					lAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
				}
			}
		} else {
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				lAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null) {
					lAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
				} else {
					lAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
				}
			}
		}

		setRequestAttribute("autoritaEsternaE", "" + lAutoritaEsternaE);
		setRequestAttribute("penaresidua", lPenaResMod);

		/*
		 * ISSUE MAC : filtro sui minorenni 
		 * Numero MAC : 20191129017 
		 * Autore : monica 
		 * Data : 05/dic/2019 
		 * Branch : 11.2.4
		 */
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());
		// ***** FINE INTERVENTO MAC_numero_MAC *****//

		return PG_LOAD_INS_OS_NUOVO_RES_PENA_RIDET_PENA_ALTRO;
	}

}