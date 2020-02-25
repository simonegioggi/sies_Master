package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInsDifferimentoOE</p>
 * <p>Description: Classe Action per il caricamento della finestra di
 *    inserimento del Provvedimento dell'esecuzione a seguito di un provvedimento
 *    della sorveglianza riguardante il Differimento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

public class ActLoadInsDifferimentoOE extends ActLoadInserisciDifferimentoMaster implements
		ICostantiSospensione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
   *
   */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// ==========================================================================
		// Questa action deve caricare i dati del provvedimento SIUS, effettuare il
		// calcolo della pena da passare alla finestra, caricare i dati da visualizzare
		// nelle combo.
		// Arriva l'id dell'evento legato alla misura alternativa.
		//
		// - Verifiche preliminari sul fascicolo
		// - Recupero dei dati sulla posizione giuridica e pena (attenzione in questo
		// caso la nuova pena residua va ricalcolata)
		// - recupero dei dati del provvedimento della sorveglianza (dettaglio)
		// - recupero dei dati per la sezione dei destinatari (combo)
		// - IN FUNZIONE DEL TIPO PROVVEDIMENTO SIUS E DELLA POSIZIONE GIURIDICA
		// DETERMINA IL TIPO PROVVEDIMENTO SIES E IL MOTIVO
		// ==========================================================================
		if (isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS)
				|| getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS).equals("")) {
			// Manca il riferimento al provvedimento della Sorveglianza
			throw new F3BException("Manca il riferimento al provvedimento della Sorveglianza");
		}

		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		// ==========================================================================
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		/*
		 * //========================================================================== // Verifico se
		 * fascicolo di competenza
		 * //==========================================================================
		 * this.isFascicoloSiepDiCompetenza();
		 * 
		 * //========================================================================== // Controllo
		 * Validazione Fascicolo //==========================================================================
		 * if ( this.isFascicoloNonValidato() ) return IWebConstants.PG_MESSAGE;
		 * 
		 * //========================================================================== // Controllo Fascicolo
		 * definito //========================================================================== if
		 * (this.isFascicoloArchiviatoDefinito()) return IWebConstants.PG_MESSAGE;
		 * 
		 * //========================================================================== // Verifico se
		 * esistono eventi non validati
		 * //==========================================================================
		 * this.isEventoNonValidato();
		 */
		// ==========================================================================
		// Verifico l'esistenza della Posizione Giuridica da caricare in maschera
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		// ==========================================================================
		// Verifico se Ergastolo recuperando il dato dalla Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// ==========================================================================
		// Recupero e controllo la Pena Residua (da caricare in maschera)
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getCodPosizioneGiuridica() != null && !lPosizione.isLibero()) {
			// Detenuto l'ultima pena Validata
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else { // Libero recupero l'ultima Pena Residua (anche se non validata)
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
		String lErrore = null;
		String lAzioneChiamante = null;

		if (lPenaResidua == null) {
			lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
			lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
		} else if (!lPosizione.isLibero()
				&& (lPenaResidua.getDataInizio() == null || lPenaResidua.getDataFine() == null)
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

		setRequestAttribute("penaresidua", lPenaResidua);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("lPenaResidua = " + lPenaResidua);
		// ==============================================================================
		// ATTENZIONE tutta questa parte di codice è in comune con la ActLoadInserisciDifferimentoMaster

		// ==========================================================================
		// Recupero i dati della Misura Alternativa
		// n.b. non recupero i dati dalle tabelle DEPOSITO_ORDINANZA_PC,
		// DEPOSITO_DECRETO e TENORE in quanto non contengono dati da visualizzare
		// nella maschera di dettaglio o utilizzabili nei calcoli successivi
		// ==========================================================================
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();

		MisuraAlternativaModel lMisAltDiff = null;
		BigDecimal lIdEventoProvvedimento = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		lMisAltDiff = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdEventoProvvedimento);
		setRequestAttribute("misuraalternativa", lMisAltDiff);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("misuraalternativa = " + lMisAltDiff.toString2());

		// ==========================================================================
		// Recupero i dati dell'ufficio che ha emesso il provvedimento (tipo, luogo...)
		// ==========================================================================
		if (lMisAltDiff != null) {
			UfficioModel lUffMod = new UfficioModel();
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lMisAltDiff.getChiaveUfficioFascicoloSius());

			setRequestAttribute("UfficioEmittente", lUffMod);
		}

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// ==========================================================================
		// Effettuo il calcolo della nuova Pena Residua in base alla Pena Residua
		// iniziale e ai dati del differimento (Data differimento)
		// I dati calcolati vengono proposti in maschera, non inseriti in questa fase
		// ==========================================================================
		PenaResiduaModel lNuovaPenaResidua = new PenaResiduaModel(lPenaResidua);
		CalendarModel lPenaEspiataSosp = new CalendarModel();
		BigDecimal lNumGiorniLA = new BigDecimal(0);

		try {
			if (lFlagErgastolo.equals("N")) {
				/*
				 * lNuovaPenaResidua = calcolaPenaResiduaDifferimento(lPosizione,
				 * lMisAltDiff.getDataInizioMisura(), lPenaResidua);
				 */
				// ==========================================================================
				// Ricalcola la Pena Residua da espiare come differenza tra la data del
				// differimento (data di interruzione della pena) a la data fine pena prevista.
				//
				// n.b. se libero la pena residua resta pari a quella a sistema
				// ==========================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
				ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
				CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

				// Pena Ricalcolata sul
				PenaResiduaModel lPenaResiduaIniziale = lCalcoloPenaModel.getPenaDaEspiare(
						lPenaResidua.getDataInizio(), null, "all");
				lCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale,
						lMisAltDiff.getDataInizioMisura());
				lNuovaPenaResidua = lCalcoloPenaModel.getPenaResiduaRicalcolata();
				lPenaEspiataSosp = lCalcoloPenaModel.getPenaEspiata();
				lNumGiorniLA = new BigDecimal(lCalcoloPenaModel.getLiberazioneAnticipata());

				// Vengono settati quei parametri
				// che non vengono gestiti nel CalcoloPenaModel
				lNuovaPenaResidua.setFasSieIdFascicoloSiep(lIdFascicolo);
				lNuovaPenaResidua.setDiesAQuo(lPenaResidua.getDiesAQuo());
				lNuovaPenaResidua.setFlagErgastolo(lPenaResidua.getFlagErgastolo());
				lNuovaPenaResidua.setFlagValidato("N");
			} else {
				// In caso di Ergastolo la nuova pena Residua non viene visualizzata
				// in quanto non significativa
			}
		} catch (Exception e) {
			throw new F3BException(e);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Pena residua ricalcolata = " + lNuovaPenaResidua);
		setRequestAttribute("nuovapenaresidua", lNuovaPenaResidua);

		// =========================================================
		// Calcolo della Sospensione (pena espiata)
		// =========================================================
		if (!lPosizione.isLibero() && lPenaResidua.getDataInizio() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Prepara SopensioneModel");

			SospensioneModel lSospensione = new SospensioneModel();
			lSospensione.setDataInizio(lMisAltDiff.getDataInizioMisura());

			if (lFlagErgastolo.equals("N")) {
				lSospensione.setNumAnniPenaResiduaReclus(lNuovaPenaResidua.getNumAnniReclusione());
				lSospensione.setNumMesiPenaResiduaReclus(lNuovaPenaResidua.getNumMesiReclusione());
				lSospensione.setNumGiorniPenaResiduaReclus(lNuovaPenaResidua.getNumGiorniReclusione());
				lSospensione.setNumAnniPenaResiduaArres(lNuovaPenaResidua.getNumAnniArresto());
				lSospensione.setNumMesiPenaResiduaArres(lNuovaPenaResidua.getNumMesiArresto());
				lSospensione.setNumGiorniPenaResiduaArres(lNuovaPenaResidua.getNumGiorniArresto());

				lSospensione.setAmmendaResidua(lNuovaPenaResidua.getImportoAmmenda());
				lSospensione.setMultaResidua(lNuovaPenaResidua.getImportoMulta());

				lSospensione.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
				lSospensione.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
				lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
			} else // Nel caso di ergastolo
			{
				// Calcolo la pena espiata come intervallo tra la data inizio e la data
				// di sospensione (considerato come giorno espiato)
				CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();

				lCalPenaEspiataCalcoloErg.setDataInizio(lPenaResidua.getDataInizio());
				lCalPenaEspiataCalcoloErg.setDataFine(lMisAltDiff.getDataInizioMisura());

				lCalPenaEspiataCalcoloErg = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
				lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

				lSospensione.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
				lSospensione.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
				lSospensione
						.setNumGiorniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumGiorni()));
			}

			lSospensione.setNumGiorniLibanticipata(lNumGiorniLA);

			setRequestAttribute("sospensione", lSospensione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("sospensione = " + lSospensione);
		}

		// ==========================================================================
		// Imposto il tipo di provvedimento: Differimento Provvisorio, Definitivo
		// Rigetto, Revoca
		// Il CodTipoProvvedimento e il CodMotivo dipendono anche dalla posizione
		// giuridica
		// ==========================================================================
		String tipoProvvedimento = getTipoProvvedimento(lMisAltDiff);
		setRequestAttribute("tipoProvvedimento", tipoProvvedimento);

		setRequestAttribute("CodTipoProvvedimento", getCodTipoProvvedimento(lMisAltDiff, lPosizione));
		setRequestAttribute("CodMotivo", getCodMotivo(lMisAltDiff));

		// ==========================================================================
		// Caricamento combo:
		// ==========================================================================
		// ==========================================================================
		// Recupero il Magistrato Competente
		// ==========================================================================
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lIdFascicolo);
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ========================
		// Autorità Esterna
		// ========================
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// ==========================================================================
		// Recupero gli Avvocati
		// ==========================================================================
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lIdFascicolo);
		setRequestAttribute("avvocati", lAvvocati);

		// ==========================================================================
		// Carico i dati della Combo "Autorità Destinazione" Avvocati UNEP
		// ==========================================================================
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// ==========================================================================
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.siep.sospensione.action.ActLoadInsDifferimentoOE");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("ritorno la pagina di visualizzazione");

		String lPage = IWebConstants.ROOT_DIR
				+ "/files/siap/siep/sospensione/LoadInserisciDifferimentoOE.jsp";

		return lPage;
	}

	/**
	 * Restituisce il codice tipo provvedimento SIES a partire dal tipo provvedimento SIUS, dalla posizione
	 * giuridica e del flag scarcerato/da scarcerare
	 * 
	 * @param aMisAltDiff
	 * @param aPosGui
	 * @return
	 */
	private String getCodTipoProvvedimento(MisuraAlternativaModel aMisAltDiff, PosizioneGiuridicaModel aPosGui) {
		String tipoProvv = getTipoProvvedimento(aMisAltDiff);
		String codTipoProvvedimento = "";

		if (tipoProvv.equals(DIFFERIMENTO_PROV)) {
			// ==========================
			// Differimento provvisorio
			// ==========================
			if (aPosGui.isLibero()) {
				// Libero
				codTipoProvvedimento = "12"; // comunicazione
			} else if (aMisAltDiff.getCodTipoUfficioScarcerazione().equals("PROC")
					&& (aPosGui.getCodPosizioneGiuridica().equals("03") // detenuto
							|| isMisAlt(aPosGui) // Misure alternative gestite
					|| aPosGui.getCodPosizioneGiuridica().equals("04") // arresti domiciliari
					)) {
				// Da scarcerare, detenuto, in misAlt o agli arresti domiciliari
				codTipoProvvedimento = "09"; // ordine di scarcerazione
			} else if (aMisAltDiff.getCodTipoUfficioScarcerazione().equals("SORV")
					&& (aPosGui.getCodPosizioneGiuridica().equals("03") || isMisAlt(aPosGui) || aPosGui
							.getCodPosizioneGiuridica().equals("04"))) {
				// Già scarcerato, detenuto, in misAlt o agli arresti domiciliari
				codTipoProvvedimento = "12"; // comunicazione
			} else {
				// Non rientro in nessuna delle posizioni giuridiche gestite.
				codTipoProvvedimento = "04"; // Provvedimento
			}
		} else if (tipoProvv.equals(DIFFERIMENTO_DEF)) {
			// ==========================
			// Differimento Definitivo
			// ==========================
			if (aPosGui.isLibero()) {
				// Libero
				codTipoProvvedimento = "12"; // comunicazione
			} else if (aMisAltDiff.getCodTipoUfficioScarcerazione().equals("PROC")
					&& (aPosGui.getCodPosizioneGiuridica().equals("03") // detenuto
							|| isMisAlt(aPosGui) // Misure alternative gestite
					|| aPosGui.getCodPosizioneGiuridica().equals("04") // arresti domiciliari
					)) {
				// Da scarcerare, detenuto, in misAlt o agli arresti domiciliari
				codTipoProvvedimento = "09"; // ordine di scarcerazione
			} else if (aMisAltDiff.getCodTipoUfficioScarcerazione().equals("SORV")
					&& (aPosGui.getCodPosizioneGiuridica().equals("03") || isMisAlt(aPosGui) || aPosGui
							.getCodPosizioneGiuridica().equals("04"))) {
				// Già scarcerato, detenuto, in misAlt o agli arresti domiciliari
				codTipoProvvedimento = "12"; // comunicazione
			} else {
				// Non rientro in nessuna delle posizioni giuridiche gestite.
				codTipoProvvedimento = "04"; // Provvedimento
			}
		} else if (tipoProvv.equals(DIFFERIMENTO_RIGETTO)) {
			// L'unico caso in cui si emette un provvedimento per un rigetto è
			// quando il soggetto è in differimento provvisorio (17). In questo caso
			// se emette un Ordine di esecuzione
			codTipoProvvedimento = "06"; // Ordine di esecuzione
		} else if (tipoProvv.equals(DIFFERIMENTO_REVOCA)) {
			codTipoProvvedimento = "06"; // Ordine di esecuzione
		}

		return codTipoProvvedimento;
	}

	/**
	 * Restituisce il codice motivo in funzione del tipo di provvedimento. RV_LOW_VALUE del dominio
	 * MOTIVO_PROVVEDIMENTO
	 * 
	 * @param aMisAltDiff
	 * @return
	 */
	private String getCodMotivo(MisuraAlternativaModel aMisAltDiff) {
		String tipoProvv = getTipoProvvedimento(aMisAltDiff);
		String codMotivo = "";

		if (tipoProvv.equals(DIFFERIMENTO_PROV)) {
			codMotivo = "0274"; // Rinvio provvisorio dell'esecuzione ex art.684 c.2 c.p.p.
		} else if (tipoProvv.equals(DIFFERIMENTO_DEF)) {
			codMotivo = "0221"; // rinvio dell'esecuzione ex art. 684 c.1 c.p.p.
		} else if (tipoProvv.equals(DIFFERIMENTO_RIGETTO)) {
			codMotivo = "0354"; // per la carcerazione (Rigetto Differimento)
		} else if (tipoProvv.equals(DIFFERIMENTO_REVOCA)) {
			codMotivo = "0355"; // per la carcerazione (Revoca Differimento)
		}

		return codMotivo;
	}
}