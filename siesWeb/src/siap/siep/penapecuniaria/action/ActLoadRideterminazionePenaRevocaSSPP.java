package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
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
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRideterminazionePenaRevocaSSPP
 * </p>
 * <p>
 * Description: Classe Action per la Load Inserimento della Rideterminazione pena a seguito di Decreto di
 * Revoca/Conversione di una Sanzione Sostitutiva per Pena Pecuniaria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: Intersistemi S.p.A.
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadRideterminazionePenaRevocaSSPP extends ActionSiap implements ICostantiPenaPecuniaria // ICostantiSanzioneSostitutiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * n.b. Classe invocata DOPO l'annotazione di revoca/conversione SS per Pena Pecuniaria Serve per poter
	 * emettere un Ordine di Esecuzione previa rideterminazione pena a seguito revoca/conversione SS in pena
	 * detentiva.
	 * 
	 * n.b. il calcolo delle pena deve essere effettuato qui
	 * 
	 * La form con: - pena rideterminata - pena convertita (SS aggiunta alla pena in cumulo) n.b. possono
	 * essere presenti più quantitativi - Oggetto: codice motivo legato al tipo di conversione. --
	 * rideterminazione pena a seguito conversione libertà controllata -- rideterminazione pena a seguito
	 * conversione Semidetenzione - magistrato firmatario e destinatari
	 * 
	 * 
	 * Recupero l'annotazione quindi il decreto puntato dall'annotazione a cui sono collegati i quantum di SS
	 * revocati.
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Sezione con i controlli preliminari
		// ==========================================================================

		BigDecimal aIdFascicolo = null;
		BigDecimal aIdFascClasseVII = null;
		DettaglioFascicoloModel lDettaglio = null;
		FascicoloSiepModel lFasMod = null;
		if (this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento di classe I oggetto di Rideterminazione Pena non è stato individuato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;

		} else {
			aIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

			lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aIdFascicolo);

			if (lDettaglio == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

			lFasMod = lDettaglio.getFascicoloSiep();
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
		// Recupero la Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

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
		// PenaResiduaModel lNuovaPenaResidua =null;
		try {
			/* lNuovaPenaResidua = */lCalcPenaModel.getPenaDaEspiare(lUltimaPenaValidata.getDataInizio(),
					null, "all", null);
		} catch (Exception e) {
			throw new F3BException(e);
		}

		// ==========================================================================
		// Recupero la pena convertita
		// ==========================================================================
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = lCtrlAnnMan
				.ExRicercaAnnotazioniManualiByIdEvento(lIdEveAnnotazione);
		setRequestAttribute("annotazione", lAnnMod);

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
				&& !lPosLuoAltr.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // Libero
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

		// =====================================
		// Ricerca Magistrato Competente
		// =====================================
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ==========================================================================
		// Recupero il Motivo Provvedimento
		// ==========================================================================
		BigDecimal lCodMotivo = this.getRequestBigDecimalParameter("MotivoRidetPena");
		String descMotivo = this.getRequestStringParameter("DescMotivoRidetPena");
		setRequestAttribute("descMotivoRidetPena", descMotivo);
		setRequestAttribute("motivoRidetPena", lCodMotivo.toString());

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

		// ====================================================================================================
		// Prima di passare alla Rideterminazione Pena si archivia il classe VII e si validano i provvedimenti
		// N.B. Occorre leggere e mettere in sessione il fascicolo di classe I
		// ====================================================================================================
		// Valorizzazione Fascicolo
		lFasMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lFasMod.setDataAggiornamento(DateUtils.getSysDate());
		lFasMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		if (this.isRequestParameterNullObj("IdFascClasseVII")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento di classe VII oggetto di Rideterminazione Pena non è stato individuato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;

		} else {

			aIdFascClasseVII = getRequestBigDecimalParameter("IdFascClasseVII");
			lFasMod.setIdFascicoloSiep(aIdFascClasseVII);
			// Si imposta il collegamento al fascicolo di classe I.
			lFasMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			// N.B. Si fa viaggiare l'IdEvento dell'Annotazione di Revoca/Conversione del fascicolo di classe
			// VII per validare il provvedimento.
			BigDecimal aIdEvento = getRequestBigDecimalParameter("IdEvento");
			String lFlagPenaScaduta = "";
			if (!this.isRequestParameterNullObj("flagPenaScaduta")) {
				lFlagPenaScaduta = this.getRequestStringParameter("flagPenaScaduta");
			}

			// Aggiornamento dati
			IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
			lFasMod = lCtrl.ExArchiviazioneClasseVII(lFasMod, aIdEvento, lFlagPenaScaduta);
		}

		// ==========================================================================
		// Passo i dati alla form, Ponendo il fascicolo di classe I in sessione
		// ==========================================================================

		aIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(aIdFascicolo);

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

		lFasMod = lDettaglio.getFascicoloSiep();
		setSessionAttribute("fascicolo", lFasMod);
		setSessionAttribute("soggetto", lFasMod.getSoggetto());
		setSessionAttribute("sentenza", lFasMod.getSentenza());
		setSessionAttribute("penaresidua", lDettaglio.getPenaResidua());
		setRequestAttribute("aPenaResidua", lDettaglio.getPenaResidua());

		setRequestAttribute("PenaComplessiva", lPenComMod);
		setRequestAttribute("aPenaConvertita", lAnnMod);
		setRequestAttribute("posizioneluogoaltra", lPosLuoAltr);

		return PG_LOAD_RIDETPENA_REVOCA_SSPP;
	}
}