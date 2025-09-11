package siap.siep.verbale.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadInserisciVerbaleSottoscrizione - Classe Action per la load inserisci di Verbale Sottoscrizione
 *
 * @version 1.0
 */
public class ActLoadInserisciVerbaleSottoscrizione extends ActionSiap
		implements ICostantiVerbale, ICostantiPenaResidua {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.info(getClass().getName() + "processRequest: inizio");

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		isEventoNonValidato();

		/************************************ MODIFICA *****************************************/
		PenaResiduaModel lPenMod = new PenaResiduaModel();
		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (notEsistePenaResiduaCorrenteByFascicoloSiep(lPenMod))
			return IWebConstants.PG_MESSAGE;
		else {
			setRequestAttribute("penaRes", lPenMod);
		}

		/************************************ MODIFICA ******************************************/
		/*
		 * modifica 13-02-06 - effettuata da Dario -- Richiesta da Viviana si cerca l'ultima concessione per
		 * usarla nella registrazione data inizio misura!!!
		 */
		MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
		IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		String[] Natura = { "CO", "DD" };
		String[] TipoMisura = null;
		String[] Decisione = null;
		lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), Decisione, Natura, TipoMisura);
		EventoModel lEveMod = null;
		EventoModel lEveProvvMod = null;

		// Recupero L'Ordinanza/Decreto
		if (lMisAlMod != null && lMisAlMod.getEveIdEvento() != null) {
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			lEveMod = lCtrlEve.ExRicercaEventoByKey(lMisAlMod.getEveIdEvento());
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				IEventoSimeone lCtrlEvento = SICOLookupRemote.getEventoSimeoneRemote();
				lEveProvvMod = lCtrlEvento.ExRicercaEventoByEveIdEvento(lEveMod.getIdEvento());
			}
		}

		// -- AMBROSINO - 03-02-2011 - Da segnalazioni di Testa/Alfieri, si blocca la registrazione data per
		// quei Procedimenti dove è già presente Data Inizio Misura
		if (lMisAlMod != null && lMisAlMod.getDataInizioMisura() != null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Data Inizio Misura già presente nel procedimento: Impossibile una ulteriore registrazione.");
		// -- END AMBROSINO

		if (lEveMod == null || lEveMod.getIdEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non esiste l'Ordinanza/Decreto del TDS/MDS.");

		// MEV-9 si aggiungono gli ulteriori codici motivo (sorveglianza)
		Set<String> codiciAffidamentoSorvNew = new HashSet<>(
				Arrays.asList(new String[] { "0680", "0681", "0690", "0691", "0692" }));
		Set<String> codiciDetenzioneSorvNew = new HashSet<>(
				Arrays.asList(new String[] { "0682", "0693" }));

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null && lEveMod.getCodTipoProvvedimento() != null
				&& lEveMod.getCodTipoEvento().equals("01") // Provvedimento
				&& (lEveMod.getCodTipoProvvedimento().equals("03") // Ordinanza
						|| lEveMod.getCodTipoProvvedimento().equals("02")) // Decreto
				&& (lEveMod.getCodMotivo().equals("0001") // Affidamento In prova Concessione
						|| lEveMod.getCodMotivo().equals("0002") // Affidamento
						|| lEveMod.getCodMotivo().equals("0003") // Affidamento
						|| lEveMod.getCodMotivo().equals("0005") // Detenzione Domiciliare Concessione
						|| lEveMod.getCodMotivo().equals("0010") // Detenzione
						|| lEveMod.getCodMotivo().equals("0013") // Detenzione
						|| lEveMod.getCodMotivo().equals("0004") // Semilibertà
						|| lEveMod.getCodMotivo().equals("2245") // Indultino
						|| lEveMod.getCodMotivo().equals("2630") // Esecuzione presso domicilio della pena
																	// detentiva ( UdS )
						|| lEveMod.getCodMotivo().equals("0011") // Detenzione a termine
						|| lEveMod.getCodMotivo().equals("2005") // Ammissione provvisoria a detenzione
																	// domiciliare
						|| lEveMod.getCodMotivo().equals("2006") // Ammissione provvisoria ad Affidamento in
																	// Prova - Affidamento Terapeutico
						|| lEveMod.getCodMotivo().equals("2008") // Ammissione provvisoria ad Affidamento in
																	// Prova - new DL146 2013
						// MEV_9 - SIEP - Si gestiscono anche i codici della semiliberta
						|| lEveMod.getCodMotivo().equals("2007") // Ammissione provvisoria a Semiliberta'
						|| lEveMod.getCodMotivo().equals("0683") // Semiliberta' (Art. 50 comma 1 O.P. - Art.
																	// 678 comma 1-ter c.p.p.)
						|| lEveMod.getCodMotivo().equals("0694") // Semiliberta' (art. 7 d.lgs. 121/2018, art.
																	// 678 comma 1 ter cp.p.)
						// MEV_9 - SIEP - Fine Semiliberta
						// MEV_9 - SIEP Si gestiscono gli ulteriori codici AFFIDAMENTO
						|| codiciAffidamentoSorvNew.contains(lEveMod.getCodMotivo())
						// MEV_9 - SIEP Si gestiscono gli ulteriori codici DETENZIONE DOMICILIARE
						|| codiciDetenzioneSorvNew.contains(lEveMod.getCodMotivo())
						// 20191120 [SG]: aggiunto codice per gestione ticket
						// Ticket#20191114019 — SIES - mancata registrazione data inizio misura
						// Esecuzione presso domicilio della pena detentiva ( TdS )
						|| lEveMod.getCodMotivo().equals("0610"))
				&& lEveProvvMod != null && "S".equals(lEveProvvMod.getFlagDocumentoRegistrato())) {
			setRequestAttribute("evento", lEveMod);
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

		// info per il log
		siesLogger.info(getClass().getName() + "processRequest: fine");

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCI_VERBALE_SOTTOSCRIZIONE;
	}

}