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
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActRicercaSbPren
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di SbPren
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
public class ActRicercaSbPren extends ActionSiap implements ICostantiSbPren {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Ricerca. Recupera i dati dalla form ed effettua la ricerca.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// ==============================================================
		// Recupero dalla form i campi di ricerca
		// n.b. cancellare o commentare i campi non presenti nella form
		// ==============================================================
		SbPrenModel lSbPMod = new SbPrenModel();
		boolean flagElenco = false;
		try {

			lSbPMod.setIdPren(getRequestBigDecimalParameter(CAMPO_ID_PREN));
			lSbPMod.setCognSogg(getRequestStringParameter(CAMPO_COGN_SOGG));
			lSbPMod.setNomeSogg(getRequestStringParameter(CAMPO_NOME_SOGG));
			lSbPMod.setCognSogg(lSbPMod.getCognSogg().toUpperCase());
			lSbPMod.setNomeSogg(lSbPMod.getNomeSogg().toUpperCase());
		} catch (Exception e) {
			flagElenco = true;
		}
		// commenti per lettura prenotazione in fase integrazione BDMC
		// da scommentare
		lSbPMod.setUtenSies(this.getUtenteConnesso().getUserId());
		lSbPMod.setCodiUffiSies(this.getUfficioUtenteConnesso().getCodUfficio());
		// lSbPMod.setUtenSies("UTENTESIES01");
		lSbPMod.setCodiStatPren(new BigDecimal(0));

		// =================================
		// Selezionare il tipo di ricerca
		// =================================
		String tipo_ricerca = "semplice";
		// String tipo_ricerca = "paginata";

		String lReturnPage = null;

		if (tipo_ricerca.equals("semplice")) {
			// =========================================================
			// Istanzio il controller ed effettuo la ricerca semplice
			// =========================================================
			ISbPren lCtrl = BDMCLookupRemote.getSbPrenRemote();
			Vector lVect = lCtrl.ExRicercaSbPren(lSbPMod);

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}
			if ((lVect.size() == 1) && (!flagElenco)) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSbPMod = new SbPrenModel((SbPrenModel) lVect.firstElement());
				BigDecimal lIdPren = lSbPMod.getIdPren();

				// ===========================================================
				// Restituisce la msg se i dati non sono stati recuperati.
				// ===========================================================
//				if (lSbPMod == null) {
//					setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
//							"I dati richiesti non sono stati trovati");
//					// Specificare eventualmente la jump page dove verrà ridirezionata la
//					// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
//					// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
//					// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
//					// della root_dir es /siap/frame.htm
//					setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
//					return ISIAPCostantiWeb.PG_MESSAGE;
//				}

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
				// ====================================================
				// Inizio creazione Modellone
				// ====================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("*** ini creazione modellone BDMC ");

				ProvvedimentoModelBDMC lProvv = new ProvvedimentoModelBDMC();
				lProvv.setSbPren(lSbPMod);

				lProvv.setSbPeriPren(lVectPeripren);
				lProvv.setSbViewCapoImpu(lVectCapoimpu);
				// Ricerca Sentenza
				try {
					SbViewProcpenaModel lProcPenaMod = new SbViewProcpenaModel();
					if (lVectProcPena.size() > 0) {
						lProcPenaMod = (SbViewProcpenaModel) lVectProcPena.get(0);
						// Decodifico il luogo di detenzione
						if (lProcPenaMod.getCodiIstiPena() != null
								&& lProcPenaMod.getCodiIstiPena().length() != 0) {
							IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
							IIstitutoDetenzione lCtrlIstDet = SIEPLookupRemote.getIstitutoDetenzioneRemote();
							lIstMod = lCtrlIstDet.ExRicercaIstitutoDetenzioneByKey(lProcPenaMod
									.getCodiIstiPena());
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
																						// dell'operatore
																						// connesso
				String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la
																								// descrizione
																								// del tipo
																								// ufficio
																								// dell'utente
																								// connesso
				String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune
																						// dell'ufficio
																						// connesso
				String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della
																					// provincia

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

				lReturnPage = PG_LOAD_DETTAGLIOSBPREN;

			} else {

				lReturnPage = PG_RICERCASBPREN;
				setRequestAttribute("sbpren", lVect);

				setRequestAttribute("tipo_ricerca", tipo_ricerca);
			}

		} else {
			String lPagina = "1";
			String CountRisultati;

			// ============================================================================
			// Recupero la pagina da visualizzare se prevengo dalla finestra dei risultati
			// ============================================================================
			if (!isRequestParameterNullObj(ISIAPCostantiWeb.NUM_PAGE))
				lPagina = getRequestStringParameter(ISIAPCostantiWeb.NUM_PAGE);

			// =========================================================
			// Istanzio il controller ed effettuo la ricerca paginata
			// =========================================================
			ISbPren lCtrl = BDMCLookupRemote.getSbPrenRemote();
			Vector lVect = lCtrl.ExRicercaSbPrenPaged(lSbPMod, Integer.parseInt(lPagina));

			if (lVect.size() == 0) {
				this.setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Nessun dato presente");
				return ISIAPCostantiWeb.PG_MESSAGE;
			}

			// ======================================================================
			// Recupero il numero di record totali della ricerca utilizzato per
			// calcolare il numero totale di pagine necessarie a visualizzare i dati
			// ======================================================================
			if (isRequestParameterNullObj("CountRisultati"))
				CountRisultati = lCtrl.ExGetCountSbPren(lSbPMod).toString();
			else
				CountRisultati = getRequestStringParameter("CountRisultati");

			if (Integer.parseInt(CountRisultati) == 1) {
				// Nel caso di un solo record visualizzo direttamente il dettaglio...
				lSbPMod = new SbPrenModel((SbPrenModel) lVect.firstElement());
				BigDecimal lIdPren = lSbPMod.getIdPren();
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
				// ====================================================
				// Inizio creazione Modellone
				// ====================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("*** ini creazione modellone BDMC ");

				ProvvedimentoModelBDMC lProvv = new ProvvedimentoModelBDMC();
				lProvv.setSbPren(lSbPMod);

				lProvv.setSbPeriPren(lVectPeripren);
				lProvv.setSbViewCapoImpu(lVectCapoimpu);
				// Ricerca Sentenza
				try {
					SbViewProcpenaModel lProcPenaMod = new SbViewProcpenaModel();
					if (lVectProcPena.size() > 0) {
						lProcPenaMod = (SbViewProcpenaModel) lVectProcPena.get(0);
						// Decodifico il luogo di detenzione
						// Decodifico il luogo di detenzione
						if (lProcPenaMod.getCodiIstiPena() != null
								&& lProcPenaMod.getCodiIstiPena().length() != 0) {
							IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
							IIstitutoDetenzione lCtrlIstDet = SIEPLookupRemote.getIstitutoDetenzioneRemote();
							lIstMod = lCtrlIstDet.ExRicercaIstitutoDetenzioneByKey(lProcPenaMod
									.getCodiIstiPena());
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
																						// dell'operatore
																						// connesso
				String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la
																								// descrizione
																								// del tipo
																								// ufficio
																								// dell'utente
																								// connesso
				String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune
																						// dell'ufficio
																						// connesso
				String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della
																					// provincia

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

				lReturnPage = PG_LOAD_DETTAGLIOSBPREN;
			} else {
				// ...altrimenti la pagina con i risultati
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(ISIAPCostantiWeb.NUM_PAGE, lPagina);
				setRequestAttribute(ISIAPCostantiWeb.REQUEST_FOR_PAGING, getCompleteRequestURL());
				setRequestAttribute("tipo_ricerca", tipo_ricerca);

				setRequestAttribute("sbpren", lVect);

				lReturnPage = PG_RICERCASBPREN;
			}
		} // fine if tipo_ricerca
		return lReturnPage;
	}

}