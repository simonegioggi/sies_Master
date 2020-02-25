package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRideterminazionePenaRevocaSS
 * </p>
 * <p>
 * Description: Classe Action per la Load Inserimento della Rideterminazione pena a seguito di Decreto di
 * Revoca/Conversione di una Sanzione Sostitutiva *
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

public class ActLoadInserisciRideterminazionePenaRevocaSS extends ActionSiap
		implements ICostantiSanzioneSostitutiva {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * n.b. Classe invocata DOPO l'annotazione di revoca/conversione SS Serve per poter emettere un Ordine di
	 * Esecuzione (?) per rideterminazione pena a seguito revoca/conversione SS dopo un cumulo.
	 * 
	 * n.b. il calcolo delle pena deve essere effettuato qui
	 * 
	 * La form con: - pena rideterminata - pena convertita (SS aggiunta alla pena in cumulo) n.b. possono
	 * essere presenti più quantitativi - Oggetto: codice motivo legato al tipo di conversione. --
	 * rideterminazione pena a seguito conversione libertà controllata -- rideterminazione pena a seguito
	 * conversione Semidetenzione - magistrato firmatario e destinatari
	 * 
	 * 
	 * Recupero l'annotazione quindi il decreto puntato dall'annotazione a cui sono i quantum di SS revocati
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Sezione con i controlli preliminari
		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();
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
		// Verifico esistenza evento non validato
		// ==========================================================================
		this.isEventoNonValidato();

		// ==========================================================================
		// Recupero Posizione Giuridica
		// ==========================================================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		if (lPosLuoAltr == null || lPosLuoAltr.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		// ==========================================================================
		// Verifico l'esistenza del decreto di Revoca/Conversione e Recupero i
		// dati relativi alla Sanzione convertita da passare alla form
		// Quale decreto. Passare id decreto?
		// ==========================================================================
		BigDecimal lIdEveAnnotazione = null;
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
			lIdEveAnnotazione = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		}

		if (lIdEveAnnotazione == null) {
			// Non esiste l'ordinanza di revoca/conversione Sanzione Sostitutiva
			// RedirectTo lRedirigi = new RedirectTo();
			// lRedirigi.setPage(IWebConstants.PG_MAIN);
			// lRedirigi.setAction("siap.siep.sanzionesostitutiva.action.ActGestioneEspulsione");

			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Per il Procedimento N."
					+ lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
					+ " non risulta presente un'ordinanza di Revoca/Conversione Sanzione Sostitutiva.");
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			// n.b. non imposto parametri in modo che faccia history.go(-1)
			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Sezione per il recupero dei dati da visualizzare nella form.
		// - Posizione giuridica
		// - Pena Residua Rideterminata a Seguito Conversione
		// - Sanzioni Convertite - Pena Convertita
		// ==========================================================================

		// ==========================================================================
		// Calcolo la pena residua da espiare (n.b. non la inserisco, ma la passo
		// solo alla form per la visualizzazione)
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenaValidata = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Calcolo la nuova pena residua");

		ActCalcoloPenaMain lCalcPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcPenaModel = lCalcPenaMain.calcoloPena(lIdFascicolo, null);
		PenaResiduaModel lNuovaPenaResidua = null;
		try {
			lNuovaPenaResidua = lCalcPenaModel.getPenaDaEspiare(lUltimaPenaValidata.getDataInizio(), null,
					"all", null);
		} catch (Exception e) {
			throw new F3BException(e);
		}

		// ==========================================================================
		// Recupero la pena convertita
		// ==========================================================================
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = lCtrlAnnMan
				.ExRicercaAnnotazioniManualiByIdEvento(lIdEveAnnotazione);

		// ==========================================================================
		// Gestite solo le seguenti posizioni giuridiche
		// - 07 = Libero (PRIMA) (o detenuto altra causa)
		// - 01 = Custodia Cautelare per Questa Causa in Regime di Detenzione (???????)
		// - isMisuraAlternativa() (11,12,13,14,15,25,29,41,42,43,44)
		// - 27 = Sospensione Pena ex L. 207/03 (mis_alt)
		// - 45 = Sospensione Pena Ex L. 207/03 in Estensione Provvisoria 51 Bis (mis_alt)
		// ==========================================================================
		PosizioneGiuridicaModel lPosGiu = lPosLuoAltr.getPosizioneGiuridica();
		if (lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // Libero
																								// prima
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03") // ????
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("27")
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("45")
				&& !lPosLuoAltr.getPosizioneGiuridica().isMisuraAlternativa()) {
			// RedirectTo lRedirigi = new RedirectTo();
			// lRedirigi.setPage(IWebConstants.PG_MAIN);
			// lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Posizione giuridica " + lPosGiu.getDescrPosizioneGiuridica() + " non gestita!");
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Ricerca l'ultima pena residua per quel fascicolo (????)
		// Rcerca la pena residua associata all'annotazione (ricalcolo)
		// ==========================================================================
		// IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel lPenaResMod =
		// lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// if( lPenaResMod == null
		// || ( lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica()!= null
		// && !lPosLuoAltr.getPosizioneGiuridica().isLibero()
		// && !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-")
		// && lPenaResMod != null && lPenaResMod.getDataInizio()== null
		// )
		// )
		// {
		// RedirectTo lRedirigi = new RedirectTo();
		// lRedirigi.setPage(IWebConstants.PG_MAIN);
		// if (lPenaResMod==null) {
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire
		// Calcolo della pena?");
		// }
		// else {
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare incoerente con Posizione
		// Giuridica. Eseguire Calcolo della pena?");
		// }
		//
		// lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
		// ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		//
		// return IWebConstants.PG_MESSAGE;
		// }

		// =====================================
		// Ricerca Magistrato Competente
		// =====================================
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ==========================================================================
		// Caricamento combo per i destinatari che sono:
		// - autorità di destinazione
		// oppure
		// - autorità competente per il territorio
		// - UEPE
		// - MdS
		// - TdS
		// sempre
		// - avvocati
		// ==========================================================================
		// Combo Autorità (dominio TIPO_AUTORITA senza filtro)
		Option lOptionAutorita = null;
		lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());

		setRequestAttribute("tipoAutorita", "" + lOptionAutorita);

		// ==========================================================================
		// Recupero i dati per i destinatari
		// ==========================================================================

		// Autorità esterna E
		Option lOptionAutoritaE = null;
		// lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),"22");
		lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaE);

		// Autorità esterna altra
		Option lOptionAutoritaAltra = null;
		lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaAltra", "" + lOptionAutoritaAltra);

		// =============
		// Avvocato/i
		// =============
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		Option lOption = null;
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// ======================
		//
		// ======================
		// setRequestAttribute("penaresidua", lPenaResMod);

		setRequestAttribute("aIdEveAnnotazione", lIdEveAnnotazione.toString());

		setRequestAttribute("aPenaConvertita", lAnnMod);
		setRequestAttribute("aPenaRideterminata", lNuovaPenaResidua);
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

		return PG_LOAD_INSERISCI_RIDETPENA_REVOCA_SS;
	}

}