package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciStampaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load della form di stampa di Cumulo
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
public class ActLoadInserisciStampaCumulo extends ActCumulo implements ICostantiCumulo {

	/**
	 * Effettua il caricamento dei dati della form di inserimento del Provvedimento di cumulo
	 * 
	 * @return jsp di visualizzazione
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			String lVariabili = ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR + "=" + lFascMod.getChiaveProgr()
					+ "&" + ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO + "=" + lFascMod.getChiaveAnno();

			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActValidazioneFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName() + "&"
					+ lVariabili);
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

		// 07-06-2006 -- Dario -- Viviana
		// this.isEventoNonValidatoPerCumulo();
		// this.isEventoNonValidato();
		this.isEventoNonValidatoPerPenaCumulo();

		// ==========================================================================
		// Recupera tutti i cumuli legati al fascicolo (FasSieIdFascicoloSiep),
		// validati e non, ordinati per data inserimento asc (quindi dal meno recente)
		// Serve solo per verificare se esiste un cumulo su cui emettere una stampa
		// attenzione la select fa una serie di join
		// ==========================================================================
		CumuloModel lCumMod = new CumuloModel();
		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		lCumMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		Vector lCum = iCum.ExRicercaCumulo(lCumMod);

		if (lCum.size() == 0) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());

			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione : Il fascicolo non risulta soggetto a cumulo! Selezionare un altro fascicolo.");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Ricerca l'evento di Cumulo non validato (inserito durante la fase di
		// Calcolo Pena) associato al Fascicolo Cumulante.
		// L'evento viene aggiornato, e successivamente validato, con le informazioni
		// inserite nella funzionalità di "Stampa Cumulo"
		// ==========================================================================
		EventoModel lEventoMod = new EventoModel();
		lEventoMod.setCodTipoEvento("01");
		lEventoMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEventoMod.setFlagDocumentoRegistrato("N");

		String lMotivo[] = { "0222", "0223", "0224", "0277" };
		String lTipoProvv[] = { "04" };

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lEventoMod = lCtrlEvento.ExRicercaEventoPerMotivoPerProvv(lMotivo, lTipoProvv, lEventoMod);
		this.setRequestAttribute("eventoCumulo", lEventoMod);

		if (lEventoMod == null || lEventoMod.getIdEvento() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.cumulo.action.ActLoadInserisciPenaComplessivaCumulo");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Non esiste l'Evento di Cumulo associato al Fascicolo. Eseguire prima il Calcolo della Pena");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Ricerca i cumuli NON VALIDATI data ins ASC (dal meno recente)
		// n.b. L'evento di stampa viene associato al primo record cumulo che opera
		// da record di appoggio anche per la pena_cumulo e le ulteriori sanzioni
		// Gli altri record Cumulo (contenenti altri link con i fascicoli cumulati)
		// restano sganciati dall'evento.
		// ==========================================================================
		Vector cumuli = iCum.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascMod
				.getIdFascicoloSiep());

		if (cumuli.size() == 0) {
			// MEV 16 - Interoperabilità SIEP-NSC
			// Il Provvedimento di esecuzione pene concorrenti risulta già validato
			// visualizzo la pagina di "Dettaglio Stampa Cumulo" con l'icona
			// per la gestione del Foglio Complementare
			ICumulo lCtrlCumulo = SIEPLookupRemote.getCumuloRemote();
			Vector lVectMod = new Vector();
			lVectMod = lCtrlCumulo
					.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(lFascMod
							.getIdFascicoloSiep());
			BigDecimal idEvento = null;
			if (lVectMod.size() > 0) {
				lCumMod = (CumuloModel) lVectMod.get(0);
				idEvento = lCumMod.getEveIdEvento();
			}

			return ICostantiCumulo.REDIRECT_DETTAGLIO_CUMULO + idEvento;

		}

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

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

		setRequestAttribute("PosizioneGiuridicaLuogoAltra", lPos);

		// ==========================================================================
		// new! 13/05/2008
		// Verifico che sia stata effettuata l'annotazione dati finali. In questo
		// caso deve essere presente un record pena_cumulo collegato al CUMULO non
		// validato meno recente (il record di appoggio)
		// Il controllo serve per evitare che venga emesso il provvedimento di Cumulo
		// senza aver annotato al pena da eseguire (vedi Ancona).
		// ==========================================================================
		PenaCumuloModel lPenaCumModel = null;
		if (cumuli != null && cumuli.size() > 0) {
			CumuloModel lCumModAppo = (CumuloModel) cumuli.elementAt(0);
			IPenaCumulo lCtrlPenaCumulo = SIEPLookupRemote.getPenaCumuloRemote();

			lPenaCumModel = lCtrlPenaCumulo.ExRicercaPenaCumuloByIdCumulo(lCumModAppo.getIdCumulo());

			if (lPenaCumModel == null || lPenaCumModel.getIdPenaCumulo() == null) {
				RedirectTo lRedirigi = new RedirectTo();

				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.cumulo.action.ActLoadInserisciPenaComplessivaCumulo");

				// lRedirigi.setAction("siap.siep.cumulo.action.ActLoadInserisciPenaComplessivaCumulo&" +
				// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());

				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Non sono stati specificati i dati Finali Cumulo. Eseguire 'annotazione dati finali'");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
		}

		// ==========================================================================
		// Carica la Pena Residua NON validata inserita più di recente, dovrebbe
		// essere quella calcolata in fase di inserimento dei dati del cumulo.
		// Attenzione!! ciò non è necessariamente vero, se è stato fatto solo il primo
		// calcolo della pena (sul cumulante)
		// MAC - Va verificato che sia presente una pena cumulo associata al record
		// cumulo non validato (quelli di appoggio, il primo inserito)
		// ==========================================================================
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ==========================================================================
		// Ricerca l'ultimo provvedimento di cumulo VALIDATO. Se esiste già un
		// provvedimento di cumulo vengono recuperati i destinatari delle notifiche
		// e precaricati nella form di inserimento.
		// ==========================================================================
		EventoModel lEveModelRic = new EventoModel();
		lEveModelRic.setCodTipoEvento("01");
		lEveModelRic.setCodTipoProvvedimento("04");
		// lEveModelRic.setCodMotivo("0277");
		lEveModelRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveModelRic.setFlagDocumentoRegistrato("S");

		String[] lCodMotivoProvvedimento = { "0222", "0223", "0224", "0277" };
		// ORDER BY DATA_EMISSIONE DESC, DATA_INSERIMENTO DESC, ID_EVENTO DESC
		EventoModel lEveModelNonOrd = lCtrlEvento.ExRicercaEventoPerMotivoOrderDesc(lCodMotivoProvvedimento,
				lEveModelRic);

		Hashtable lTable = new Hashtable();

		if (lEveModelNonOrd != null) {
			// ricerca evento notifica non riferito all'ordinanza
			EventoNotificaModel lEveModSucc = lCtrlEvento.ExRicercaEventoNotificaByKey(lEveModelNonOrd
					.getIdEvento());
			this.setRequestAttribute("eventonotifica", lEveModSucc);
			lTable = this.ricercaNotifiche(lEveModSucc.getNotifiche());
		}

		// ==========================================================================
		// Caricamento dei potenziali destinatari
		// ==========================================================================
		// Autorità esterna N
		Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

		AutoritaEsternaModel lAutN = null;
		if (lTable.get("AutN") != null) {
			lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			setRequestAttribute("autoritaEsternaN", lAutN);
			if (lAutN != null) {
				lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lAutN.getCodTipoAutorita());
				setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);
			}
		}

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

		// Istituto
		if (lTable.get("Ist") != null) {
			setRequestAttribute("Ist", ((NotificaModel) lTable.get("Ist")).getIstitutoDetenzione());
		}

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// Collection lTipologia = DecodificheManager.getInstance().getStampeCumulo();
		// setRequestAttribute("tipologia", lTipologia);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsterna", "" + lOption);

		// ========================================================================
		// Vengono recuperati i GG di LA concessi (presi in carico e associati a
		// un evento SIES VALIDATI) già detratti o da detrarre
		// Nuova gestione LA (12/2006)
		// ========================================================================
		// CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5();
		// CalcoloPenaModel lCalcPenaModel = lCtrlF5.exGetPenaIniziale(lFascMod.getIdFascicoloSiep(), null);
		// Vector lListaLA =
		// lCtrlF5.exGetLiberazioneAnticipata(lFascMod.getIdFascicoloSiep(),lCalcPenaModel.getDataDal(),null);
		// lCalcPenaModel.setLibAnticipate(lListaLA);
		//
		// int totGiorniLAConcessi = lCalcPenaModel.getLiberazioneAnticipataGiaConcesse();
		// this.setRequestAttribute("giorniLA",""+ totGiorniLAConcessi);

		// if (lPenaCumModel!=null && lPenaCumModel.getIdPenaCumulo()!=null)
		// {
		// this.setRequestAttribute("giorniLA",""+ lPenaCumModel.getNumGiorniLibAnticipata());
		// }

		// 20/05/2014 - Nuova L.A. - decreto 2013/146
		this.setRequestAttribute("penaCum", lPenaCumModel);

		return PG_LOAD_INSERIMENTO_STAMPA_CUMULO;
	}

}