package siap.siep.verbale.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
//import f3b.log.LogF3B;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleDataInizioModel;

/**
 * <p>
 * Title: ActLoadInserisciVariazioneVerbaleSottoscrizione
 * </p>
 * <p>
 * Description: Classe Action per la load dell'inserimento della VARIAZIONE di Verbale Sottoscrizione
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
public class ActLoadInserisciVariazioneVerbaleSottoscrizione extends ActionSiap
		implements ICostantiVerbale, ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		this.isEventoNonValidato();

		/*
		 * modifica 13-02-06 - effettuata da Dario -- Richiesta da Viviana si cerca l'ultima concessione per
		 * usarla nella registrazione data inizio misura!!!
		 */

		// ===========================================================================
		// Recupera l'ultima (DATA_EMISSIONE) MA di concessione
		// ===========================================================================
		MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
		IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		String[] Natura = { "CO", "DD" }; // CO = Concede, DD = Differimento pena nelle forme della Detenzione
											// Domiciliare
		String[] TipoMisura = null;
		String[] Decisione = null;
		lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), Decisione, Natura, TipoMisura);
		EventoModel lEveMod = null; // Ordinanza/decreto
		EventoModel lEveProvvMod = null; // Provvedimento SIEP

		if (lMisAlMod != null && lMisAlMod.getEveIdEvento() != null) {
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrlEve.ExRicercaEventoByKey(lMisAlMod.getEveIdEvento());
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				IEventoSimeone lCtrlEvento = SICOLookupRemote.getEventoSimeoneRemote();
				lEveProvvMod = lCtrlEvento.ExRicercaEventoByEveIdEvento(lEveMod.getIdEvento());
			}
		}

		if (lEveMod == null || lEveMod.getIdEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non esiste l'Ordinanza/Decreto del TDS/MDS.");

		setRequestAttribute("eventoProv", lEveProvvMod);

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null && lEveMod.getCodTipoProvvedimento() != null
				&& lEveMod.getCodTipoEvento().equals("01") // Provvedimento
				&& (lEveMod.getCodTipoProvvedimento().equals("03") // Ordinanza
						|| lEveMod.getCodTipoProvvedimento().equals("02")) // Decreto
				&& (lEveMod.getCodMotivo().equals("0001") // Affidamento
						|| lEveMod.getCodMotivo().equals("0002") // Affidamento
						|| lEveMod.getCodMotivo().equals("0003") // Affidamento
						|| lEveMod.getCodMotivo().equals("0005") // Detenzione
						|| lEveMod.getCodMotivo().equals("0010") // Detenzione
						|| lEveMod.getCodMotivo().equals("0013") // Detenzione
						|| lEveMod.getCodMotivo().equals("0004") // Semilibertà
						|| lEveMod.getCodMotivo().equals("2245") // Indultino
						|| lEveMod.getCodMotivo().equals("0011") // Detenzione a termine
						|| lEveMod.getCodMotivo().equals("2005") // Ammissione provvisoria a detenzione
																	// domiciliare
						|| lEveMod.getCodMotivo().equals("2006")// Ammissione provvisoria ad Affidamento in
																// prova
						|| lEveMod.getCodMotivo().equals("2008")// Ammissione provvisoria ad Affidamento in
																// prova DL 146/2013
						// Esecuzione presso domicilio della pena detentiva ( UdS )
						|| lEveMod.getCodMotivo().equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM_MOTIVO)
						// 20191120 [SG]: aggiunto codice per gestione ticket
						// Ticket#20191114019 — SIES - mancata registrazione data inizio misura
						// Esecuzione presso domicilio della pena detentiva ( TdS )
						|| lEveMod.getCodMotivo().equals("0610"))
				&& lEveProvvMod != null && "S".equals(lEveProvvMod.getFlagDocumentoRegistrato())) {
			setRequestAttribute("evento", lEveMod);
			setRequestAttribute("misalt", lMisAlMod);
		} else {
			if (lEveProvvMod != null && (lEveProvvMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveProvvMod.getFlagDocumentoRegistrato())
					|| "".equals(lEveProvvMod.getFlagDocumentoRegistrato()))) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Il provvedimento non è validato. Non si può dare corso alla misura alternativa.");
			} else if (lEveProvvMod == null) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Il provvedimento non è presente o è stato annullato. Non si può dare corso alla misura alternativa.");
			} else {
				throw new SIEPException(SIEPException.USER_MESSAGE, "Il provvedimento "
						+ lEveMod.getDescrMotivo() + ", non permette di dare corso alla misura alternativa.");
			}
		}

		Option lOptionAutorita = null;
		lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutoritaArresto());
		setRequestAttribute("tipoAutorita", "" + lOptionAutorita);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// - - - - - - > Ricerca del Verbale per la Variazione della data
		// FIXME ricerca un verbale qualsiasi
		VerbaleDataInizioModel lVerMod = new VerbaleDataInizioModel();
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
		lVerMod = lCtrl.ExRicercaVerbaleByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("verbaleData", lVerMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Verbale = " + lVerMod);

		// - - - - - - - - model cssa
		CSSAModel lCssaMod = new CSSAModel();
		if (lVerMod != null && lVerMod.getCssIdCssa() != null
				&& lVerMod.getCssIdCssa().compareTo(new BigDecimal(0)) != 0) {
			ICSSA lCtrlCssa = SICOLookupRemote.getCSSARemote();
			lCssaMod = lCtrlCssa.getCSSAByKey(lVerMod.getCssIdCssa());
		}
		setRequestAttribute("cssa", lCssaMod);

		// - - - - - - - - - - - model Istituto Detenzione
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
		if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null
				&& !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lVerMod.getIstDetIdIstitutoDetenzione());
		}
		setRequestAttribute("istitutodetenzione", lIstMod);

		// - - - - - - - - - > Ricerca pena da espiare
		// Recupero l'ultima pena residua a sistema (validata o meno)
		PenaResiduaModel lPenMod = new PenaResiduaModel();
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// AMBROSINO 01-02-2011 - Segnalazione di Nunzia Alfieri :
		// La Data Inizio Misura si può variare solo se questa è uguale alla Data Inizio Pena
		if (lPenMod.getDataInizio() != null
				&& !lPenMod.getDataInizio().equals(lMisAlMod.getDataInizioMisura()))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"NON E' POSSIBILE VARIARE LA DATA INIZIO MISURA SE IL SOGGETTO PASSA DA UNA MISURA AD UN'ALTRA");

		// ========================================================================
		// Recupero i quantum di pena Validati che concorrono alla calcolo della
		// pena
		// ========================================================================
		String vedoDataIntermedia = "N";
		Date lDataFinePenaPres = null;
		Date lDataInizio = lVerMod.getDataEmissione();
		PenaResiduaModel lPenaModel = new PenaResiduaModel();
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(),
				null);

		// ***********************************NO*ERGASTOLO********************************************/
		// ===========================================================================
		// Il ricalcolo viene effettuato solo se non Ergastolo, altrimenti viene
		// semplicemente impostato il fine pena a 31/12/9999
		// ===========================================================================
		if (lPenMod.getFlagErgastolo().equals("N")) {
			lPenaModel = lCalcoloPenaModel.getPenaDaEspiare(lDataInizio, null, "all");
			lDataFinePenaPres = lPenaModel.getDataFine();
			if (lPenaModel != null && (lPenaModel.getDataFineReclusione() != null
					|| lPenaModel.getDataInizioArresto() != null)) {
				vedoDataIntermedia = "S";
			}
		} else { // Ergastolo
			lPenaModel = new PenaResiduaModel(lPenMod);
			lDataFinePenaPres = DateUtils.getDate(9999, 12, 31);
			lPenaModel.setDataFine(lDataFinePenaPres);
		} // Fine Ergastolo

		lPenaModel.setDataInizio(lDataInizio);
		lPenaModel.setDataFinePresunta(lDataFinePenaPres);

		int lTotGiorni = lCalcoloPenaModel.getLiberazioneAnticipata();
		int lTotGiorniRD = lCalcoloPenaModel.getRimediRisarcitori();

		lPenaModel.setEveIdEvento(lVerMod.getEveIdEvento());
		lPenaModel.setFlagValidato(lPenMod.getFlagValidato());
		lPenaModel.setIdPenaResidua(lPenMod.getIdPenaResidua()); // A8RR004
		// ------
		lPenaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lPenaModel.setFlagErgastolo(lPenMod.getFlagErgastolo());
		lPenaModel.setDiesAQuo("S");

		// Se l'ultima pena residua era Validata
		if (lPenMod.getFlagValidato().equals("S")) {
			lPenaModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lPenaModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lPenaModel.setDataInserimento(DateUtils.getSysDate());
			lPenaModel.setCodUfficioAggiornamento(null);
			lPenaModel.setCodOperatoreAggiornamento(null);
			lPenaModel.setDataAggiornamento(null);
		}

		if (lPenMod.getFlagValidato().equals("N")) {
			lPenaModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lPenaModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lPenaModel.setDataAggiornamento(DateUtils.getSysDate());
		}

		// ==========================================================================
		// Aggiorna la pena_residua e le LA (da N a E)
		// ==========================================================================
		// PenaResiduaModel lPenModFin = new PenaResiduaModel();
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Aggiorno/Inserisco Pena residua: "+lPenaModel);
		// IVerbale IVerCtrl = SIEPLookupRemote.getVerbaleRemote();
		// lPenModFin = IVerCtrl.ExAggiornaPenaVerbale(lPenaModel);

		setRequestAttribute("penaresidua", lPenaModel);
		// setRequestAttribute("penaresidua", lPenModFin);
		setRequestAttribute("vedoDataIntermedia", vedoDataIntermedia);
		setRequestAttribute("lTotGiorniConcessi", "" + lTotGiorni);

		setRequestAttribute("lTotGiorniRDConcessi", "" + lTotGiorniRD);

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCI_VARIAZIONE_VERBALE_SOTTOSCRIZIONE;
	}

}