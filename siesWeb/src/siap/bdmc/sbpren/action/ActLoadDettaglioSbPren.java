package siap.bdmc.sbpren.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbperipren.controller.ISbPeripren;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.bdmc.sbpren.controller.ISbPren;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbpren.model.SbPrenModel;
import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.bdmc.sbviewreat.controller.ISbViewReat;
import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDettaglioSbPren
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di SbPren
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
public class ActLoadDettaglioSbPren extends ActionSiap implements ICostantiSbPren {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String lPage = "";
		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================
		BigDecimal lIdPren = getRequestBigDecimalParameter(CAMPO_ID_PREN);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		ISbPren lCtrl = BDMCLookupRemote.getSbPrenRemote();
		SbPrenModel lSbPMod = lCtrl.ExRicercaSbPrenById(lIdPren);
		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lSbPMod == null) {
			setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		// setRequestAttribute("sbpren", lSbPMod);
		// setSessionAttribute("sbprenInclude", lSbPMod);
		// Ricerca del procedimento penale legato alla prenotazione
		ISbViewProcpena lCtrlProcPena = BDMCLookupRemote.getSbViewProcpenaRemote();
		SbViewProcpenaModel aSbViewProcpena = new SbViewProcpenaModel();
		aSbViewProcpena.setIdPren(lIdPren);
		Vector lVectProcPena = lCtrlProcPena.ExRicercaSbViewProcpena(aSbViewProcpena);
		// setRequestAttribute("lVectProcPena", lVectProcPena);
		// Ricerca dei periodi prenotati
		ISbPeripren lCtrlPeripren = BDMCLookupRemote.getSbPeriprenRemote();
		SbPeriprenModel aSbPeripren = new SbPeriprenModel();
		aSbPeripren.setIdPren(lIdPren);
		Vector lVectPeripren = lCtrlPeripren.ExRicercaSbPeripren(aSbPeripren);
		// ========================================================================
		// = Routine di bonifica dei periodi già iscritti ma non ancora
		// = segnalati a Bdmc
		// = Powere by Festa Carlo
		// =========================================================================
		Vector lVectPeriprenScope = new Vector();
		MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
		lModBdmc.setIdPren(lIdPren);
		lModBdmc.setFlagComputabile("0");
		// lModBdmc.setFlagStato("I");
		lModBdmc.setStatoTrasmissioneIsc("N");
		IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
		Vector misCautBdmc = lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
		if (misCautBdmc != null && misCautBdmc.size() != 0 && lVectPeripren != null
				&& lVectPeripren.size() != 0) {
			for (int ii = 0; ii < lVectPeripren.size(); ii++) {
				SbPeriprenModel aSbPeriprenIsc = (SbPeriprenModel) lVectPeripren.get(ii);
				for (int kk = 0; kk < misCautBdmc.size(); kk++) {
					MisuraCautelareBdmcModel lModBdmcIsc = (MisuraCautelareBdmcModel) misCautBdmc.get(kk);
					if (aSbPeriprenIsc.getProgPeriPres().equals(lModBdmcIsc.getProgPeriPres())) {
						aSbPeriprenIsc.setCodStatPrenPeri("9");
						break;
					}
				} // fine for misCautBdmc
				lVectPeriprenScope.add(aSbPeriprenIsc);
			} // fine for lVectPeriPren

		} // fine "if (misCautBdmc != null && misCautBdmc.size() != 0)"
		else {
			lVectPeriprenScope = lVectPeripren;
		}
		// = fine Routine di bonifica dei periodi già iscritti ma non ancora
		// ==============================================================================
		// setRequestAttribute("lVectPeripren", lVectPeripren);
		// Ricerca dei capi imputazioni
		ISbViewCapoimpu lCtrlCapoimpu = BDMCLookupRemote.getSbViewCapoimpuRemote();
		SbViewCapoimpuModel aSbCapoimpu = new SbViewCapoimpuModel();
		aSbCapoimpu.setIdPren(lIdPren);
		Vector lVectCapoimpu = lCtrlCapoimpu.ExRicercaSbViewCapoimpu(aSbCapoimpu);
		Vector lSbViewCapoimpi = new Vector();
		Iterator lIterCapoImpu = lVectCapoimpu.iterator();
		while (lIterCapoImpu.hasNext()) {
			SbViewCapoimpuModel lCapoimpu = (SbViewCapoimpuModel) lIterCapoImpu.next();
			// Ricerca dei reati
			ISbViewReat lCtrlReat = BDMCLookupRemote.getSbViewReatRemote();
			SbViewReatModel aSbViewReat = new SbViewReatModel();
			aSbViewReat.setIdPren(lIdPren);
			aSbViewReat.setNumeProgCapoImpu(lCapoimpu.getNumeProgCapoImpu());
			aSbViewReat.setAnnoFascBdmc(lCapoimpu.getAnnoFascBdmc());
			aSbViewReat.setNumeFascBdmc(lCapoimpu.getNumeFascBdmc());
			Vector lVectReat = lCtrlReat.ExRicercaSbViewReat(aSbViewReat);
			lCapoimpu.setSbViewReat(lVectReat);
			lSbViewCapoimpi.add(lCapoimpu);
		}

		// setRequestAttribute("lVectCapoimpu", lSbViewCapoimpi);
		// Ricerca dei reati
		// ISbViewReat lCtrlReat = BDMCLookupRemote.getSbViewReatRemote();
		// SbViewReatModel aSbViewReat = new SbViewReatModel();
		// aSbViewReat.setIdPren(lIdPren);
		// Vector lVectReat = lCtrlReat.ExRicercaSbViewReat(aSbViewReat);
		// setRequestAttribute("lVectReat", lVectReat);

		// ====================================================
		// Inizio creazione Modellone
		// ====================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("*** ini creazione modellone BDMC ");

		ProvvedimentoModelBDMC lProvv = new ProvvedimentoModelBDMC();
		lProvv.setSbPren(lSbPMod);

		lProvv.setSbPeriPren(lVectPeriprenScope);
		lProvv.setSbViewCapoImpu(lVectCapoimpu);
		// lProvv.setSbViewReat(lVectReat);
		// Ricerca Sentenza
		try {
			SbViewProcpenaModel lProcPenaMod = new SbViewProcpenaModel();
			if (lVectProcPena.size() > 0) {
				lProcPenaMod = (SbViewProcpenaModel) lVectProcPena.get(0);

				// Decodifico il luogo di detenzione
				if (lProcPenaMod.getCodiIstiPena() != null && lProcPenaMod.getCodiIstiPena().length() != 0) {
					IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
					IIstitutoDetenzione lCtrlIstDet = SIEPLookupRemote.getIstitutoDetenzioneRemote();
					lIstMod = lCtrlIstDet.ExRicercaIstitutoDetenzioneByKey(lProcPenaMod.getCodiIstiPena());
					lProcPenaMod.setDescriIstiPena(lIstMod.getDescrTipoIstituto() + " di "
							+ lIstMod.getDescrComune());
				}
				SentenzaModel lSentMod = new SentenzaModel();
				lSentMod = lProcPenaMod.toSentenza();
				lSentMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
				Vector lSentDuplicate = lSCtrl.ExRicercaSentenzaDuplicata(lSentMod);
				if (lSentDuplicate.size() > 0) {
					lProvv.setSentenza(((SentenzaModel) lSentDuplicate.get(0)));

				}
			}
		} catch (SIEPException siepEx) {
		} catch (Exception ex) {
		}
		lProvv.setSbViewProcpena(lVectProcPena);
		// Ricerca Soggetto Sies/omonimi
		lProvv = lCtrl.ExRicercaSogg(lSbPMod, lProvv);
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaEmi", "" + lOption);

		String codUfficio = this.getUfficioUtenteConnesso().getCodUfficio(); // per il codice ufficio
																				// dell'operatore connesso
		String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la descrizione del
																						// tipo ufficio
																						// dell'utente
																						// connesso
		String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune dell'ufficio
																				// connesso
		String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della provincia

		setRequestAttribute("codUfficio", codUfficio);
		setRequestAttribute("descrUfficio", descrUfficio);
		setRequestAttribute("descrComune", descrComune);
		setRequestAttribute("codProv", codProv);

		setRequestAttribute("descrLuogoEmittente", "");
		setRequestAttribute("codTipoAutoritaEmittente", "");

		setRequestAttribute("provvedimento", lProvv);
		setSessionAttribute("provvedimentoBDMC", lProvv);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("*** end creazione modellone BDMC ");

		lPage = PG_LOAD_DETTAGLIOSBPREN;

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");
		return lPage;
	}

}