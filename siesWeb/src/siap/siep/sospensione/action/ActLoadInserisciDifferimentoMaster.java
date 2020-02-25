package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciDifferimentoMaster</p>
 * <p>Description: Classe Action Estesa dalla classi di Load inserimento del
 *    differimento (provvisorio, definitivo, revoca, rigetto) </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciDifferimentoMaster extends ActSIESDettaglioProvvedimento
		implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo che effettua i controlli preliminari all'accesso alla funzionalità richiesta, e il caricamento
	 * dei dati da visualizzare nella finestra di inserimento (combo, ......) - Posizione Giuridica - flag
	 * ergastolo - Pena Residua I dati da caricare in maschera sono: - Posizione Giuridica - Atra causa
	 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String tipoProvvedimento = (String) getRequestAttribute(TIPO_DIFFERIMENTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("");
		// ==========================================================================
		// Controllo Presenza del Fascicolo in Sessione
		// ==========================================================================
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Verifico se fascicolo di competenza
		// ==========================================================================
		this.isFascicoloSiepDiCompetenza();

		// ==========================================================================
		// Controllo Validazione Fascicolo
		// ==========================================================================
		if (this.isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Controllo Fascicolo definito
		// ==========================================================================
		if (this.isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Verifico se esistono eventi non validati
		// ==========================================================================
		this.isEventoNonValidato();

		// ==========================================================================
		// Verifico l'esistenza della Posizione Giuridica da caricare in maschera
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		// ==========================================================================
		// Verifico se posizione giuridica gestita
		// ==========================================================================
		if (tipoProvvedimento.equals(DIFFERIMENTO_PROV)) {
			if (lPosizione.getCodPosizioneGiuridica().equals("17")) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Attenzione, la Posizione Giuridica di partenza (libero in differimento provvisorio) non è corretta");
			}
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_RIGETTO)) {
			if (lPosizione.getCodPosizioneGiuridica().equals("16")) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Attenzione, soggetto già in differimento definitivo; occorre prima revocarlo");
			}
		}

		// ==========================================================================
		// Se sto inserendo un differimento definitivo, verifico se presente un
		// differimento provvisorio per precaricare la data del differimento in
		// maschera
		// ==========================================================================
		if (tipoProvvedimento.equals(DIFFERIMENTO_DEF)) {
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
			lMisAlMod = lMisAltCtrl
					.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

			if (lMisAlMod != null && "02".equals(lMisAlMod.getCodTipoDecisione()) // 02 Decreto
					&& "CO".equals(lMisAlMod.getCodNaturaDecisione()) // Concessione
					&& ("2010".equals(lMisAlMod.getCodTipoMisura()) // Differimento Pena facoltativo art.146
																	// C.P. (provvisorio)
							|| "2011".equals(lMisAlMod.getCodTipoMisura()) // Differimento Pena obbligatorio
																			// art.147 C.P. (provvisorio)
					)) {
				// Carico il differimento Provvisoria da passare alla form
				setRequestAttribute("differimentoprovvisorio", lMisAlMod);
			}
		}

		// ==========================================================================
		// Verifico se Ergastolo recuperando il dato della Pena Complessiva
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

		// ==========================================================================
		// Caricamento combo:
		// - Tipo Provvedimento (bean: tipoprovvedimento)
		// - Autorità Emittente (bean: autoritaemittente) non serve, campo fisso
		// - Contenuto Decisione (bean: oggettodecisione)
		// - Oggetto Decisione (bean: tipologiadecisione)
		// ==========================================================================
		Option lOption = null;

		// ===========================================
		// TIPO PROVVEDIMENTO
		// ===========================================
		if (tipoProvvedimento.equals(DIFFERIMENTO_PROV)) {
			setRequestAttribute("CodTipoProvvedimento", "02"); // Decreto
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_DEF)) {
			setRequestAttribute("CodTipoProvvedimento", "03"); // Ordinanza
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_RIGETTO)) {
			// Il tipo provvedimento dipende dalla posizione Giuridica di partenza:
			// se 17 (Libero in differimento provvisorio) allora il rigetto Tds 03 Ordinanza
			// In tutti gli altri casi semplice annotazione (03 = Ordinanza)
			// Quindi sempre ordinanza!!!
			setRequestAttribute("CodTipoProvvedimento", "03"); // Ordinanza
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_REVOCA)) {
			/*
			 * lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimenti());
			 * lOption.setFilter( new String[] {"02", "03"} ); //solo DECRETO o ORDINANZA
			 * 
			 * setRequestAttribute("listaTipoProvvedimento", "" + lOption );
			 */
			setRequestAttribute("CodTipoProvvedimento", "03"); // Ordinanza
		}

		// ===========================================
		// AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		// ===========================================
		lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
		setRequestAttribute("autoritaemittente", "" + lOption);

		// ===============================
		// Combo CONTENUTO DECISIONE
		// ===============================
		// if ( tipoProvvedimento.equals("DifferimentoProvv") ){
		// Option loggettoDecisione = new
		// Option(DecodificheManager.getInstance().getOggettoSospensioneDiffProvv());
		// setRequestAttribute("oggettodecisione", "" + loggettoDecisione);
		// }
		// else if ( tipoProvvedimento.equals("DifferimentoDef") ){
		// Option loggettoDecisione = new
		// Option(DecodificheManager.getInstance().getOggettoSospensioneDiffDef());
		// setRequestAttribute("oggettodecisione", "" + loggettoDecisione);
		// }
		// else if ( tipoProvvedimento.equals("DifferimentoRigetto") ){
		// }
		// else if ( tipoProvvedimento.equals("DifferimentoRevoca") ){
		// Option loggettoDecisione = new
		// Option(DecodificheManager.getInstance().getOggettoRevocaDifferimento());
		// setRequestAttribute("oggettodecisione", "" + loggettoDecisione);
		// }

		// =================================================
		// Combo OGGETTO DECISIONE (MOTIVO_PROVVEDIMENTO)
		// =================================================
		if (tipoProvvedimento.equals(DIFFERIMENTO_PROV)) {
			Option lTipologiaDecisione = new Option(
					DecodificheManager.getInstance().getTipologiaDecisioneSospensioneDiffProvv());
			setRequestAttribute("tipologiadecisione", "" + lTipologiaDecisione);
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_DEF)) {
			Option lTipologiaDecisione = new Option(
					DecodificheManager.getInstance().getTipologiaDecisioneSospensioneDiffDef());
			setRequestAttribute("tipologiadecisione", "" + lTipologiaDecisione);
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_RIGETTO)) {
			//
			Option lTipologiaDecisione = new Option(
					DecodificheManager.getInstance().getTipologiaDecisioneRigettoDifferimento());
			setRequestAttribute("tipologiadecisione", "" + lTipologiaDecisione);

			// Combo Tipoligia Rigetto
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			Collection lColl = lDecodifiche.ExRicercaTipologiaRigettoDiff();
			Option lTipologiaRigetto = new Option(lColl);
			setRequestAttribute("tipologiarigetto", "" + lTipologiaRigetto);
		} else if (tipoProvvedimento.equals(DIFFERIMENTO_REVOCA)) {
			Option lTipologiaDecisione = new Option(
					DecodificheManager.getInstance().getTipologiaDecisioneRevocaDifferimento());
			setRequestAttribute("tipologiadecisione", "" + lTipologiaDecisione);
		}

		// ==========================================================================
		// Imposto la pagina di risposta
		// ==========================================================================
		setRequestAttribute(TIPO_DIFFERIMENTO, tipoProvvedimento);

		String lPage = IWebConstants.ROOT_DIR
				+ "/files/siap/siep/sospensione/LoadInserisciDifferimentoSORV.jsp";
		return lPage;
		// return PG_LOAD_INSERISCI_SOSPENSIONE_DIFFERIMENTO; //restituisce la jsp di VIEW
	}

	/**
	 * Restituisce true se il soggetto è in una posizione giuridica di misura alternativa gestita dal
	 * differimento
	 * 
	 * @param aPosGui
	 * @return
	 */
	protected boolean isMisAlt(PosizioneGiuridicaModel aPosGui) {
		boolean inMisuraAlt = false;

		if (aPosGui.getCodPosizioneGiuridica().equals("12") || aPosGui.getCodPosizioneGiuridica().equals("13")
				|| aPosGui.getCodPosizioneGiuridica().equals("14")
				|| aPosGui.getCodPosizioneGiuridica().equals("27")
				// MEV29 Aggiunte la altre PG in Misura RV_ABBREVIATION = MIS_ALT
				|| aPosGui.isMisAlt()) {
			inMisuraAlt = true;
		}
		return inMisuraAlt;
	}

	/**
	 * Restituisce il tipo provvedimento in funzione del codice natira decisione della sorveglianza e del cod
	 * tipo decisione (ordinanza/decreto)
	 * 
	 * @param aMisAltDiff
	 * @return
	 */
	protected String getTipoProvvedimento(MisuraAlternativaModel aMisAltDiff) {

		String tipoProvvedimento = "";

		if (aMisAltDiff.getCodNaturaDecisione().equals("CO")
				&& aMisAltDiff.getCodTipoDecisione().equals("02")) {
			tipoProvvedimento = DIFFERIMENTO_PROV;
		} else if (aMisAltDiff.getCodNaturaDecisione().equals("CO")
				&& aMisAltDiff.getCodTipoDecisione().equals("03")) {
			tipoProvvedimento = DIFFERIMENTO_DEF;
		} else if (aMisAltDiff.getCodNaturaDecisione().equals("RG")) {
			tipoProvvedimento = DIFFERIMENTO_RIGETTO;
		} else if (aMisAltDiff.getCodNaturaDecisione().equals("RE")) {
			tipoProvvedimento = DIFFERIMENTO_REVOCA;
		}

		return tipoProvvedimento;
	}

}