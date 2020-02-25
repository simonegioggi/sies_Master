package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.util.UfficioAccorpatoUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRigettoRichiestaAtti Description: Classe Action per la LOAD INSERISCI RIGETTO
 * RICHIESTA Trasmissione Atti per Competenza
 * </p>
 */

public class ActLoadInserisciRigettoRichiestaAtti extends ActionSiap implements ICostantiRichiesta {

	/**
	 * Action richiamata in fase di ricerca del procedimento cumulante, oppure per apertura diretta senza
	 * richiesta
	 * 
	 * 
	 */
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			// Provengo dalla lista delle Richieste caricherò dopo il procedimento
			// corretto in funzione del fascicoli richiesto dopo le opportune verifiche
		} else if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// Verifico (?) se si sta lavorando in architettura distribuita
		// leggendo l'apposito flag su f3b.properties
		boolean isArcDistribuita = false;
		// flag per l'inserimento manuale
		String insertManuale = "1";

		if (isArcDistribuita) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Funzionalità in fase di Implementazione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		} else {

			if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
				// Provengo dal Richieste Atti Ricevuti - Dettaglio Richiesta - Trasmissione
				siesLogger.debug("Provego dal Messaggio di richiesta");

				// Potrei non avere il procedimento da trasmettere in sessione o averne uno differente
				// Devo caricare il procedimento richiesto in sessione prima di procedere

				try {
					// Recupero il CompetenzaModel da Passare alla Maschera
					BigDecimal lIdMessaggio = this
							.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

					IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
					MessaggioModel lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);

					setRequestAttribute("MessaggioRichiesta", lMessModel);

					ParserMessage lParser = new ParserMessage(lMessModel.getTreeModel());
					DettaglioFascicoloModel lDettaglioFasModel = lParser.getDettaglioFascicoloSiep();

					/// ----->> lDettaglioFasModel.getFascicoloSiep().getSoggetto() = SOGGETTO DEL CUMULANTE
					/// che è nella Richiesta
					if (lDettaglioFasModel != null && lDettaglioFasModel.getFascicoloSiep() != null
							&& lDettaglioFasModel.getFascicoloSiep().getSoggetto() != null) {
						setRequestAttribute("soggetto", lDettaglioFasModel.getFascicoloSiep().getSoggetto());
					}

					DatiSiepPerTrasferimentoModel lDSPT = lDettaglioFasModel.getDatiSiepPerTrasferimento();
					Vector lListaCompetenze = (Vector) lDSPT.getListCompetenze();

					CompetenzaModel lUltimaCompetenza = null;
					lUltimaCompetenza = (CompetenzaModel) lListaCompetenze.lastElement();

					siesLogger.debug(" --XX-- ActLodInserisciRigetto -- Ultima Comp = " + lUltimaCompetenza);

					// Verifico il fascicolo richiesto e lo carico in sessione se non già
					// caricato
					boolean caricaFascicolo = false;

					// verifico se l'utente ha indicato i dati del fascicolo manualmente in
					// quanto non presenti sulla richiesta
					if (lUltimaCompetenza.getChiaveAnno() == null
							|| lUltimaCompetenza.getChiaveProgr() == null) {
						BigDecimal lChiaveAnno = getRequestBigDecimalParameter(ICostantiJMS.CHIAVE_ANNO_SIEP);
						BigDecimal lChiaveProgr = getRequestBigDecimalParameter(
								ICostantiJMS.CHIAVE_PROGR_SIEP);

						// verifico la presenza effettiva del procedimento
						// Ricerco il fascicolo
						FascicoloSiepModel lFascRich = new FascicoloSiepModel();
						lFascRich.setChiaveAnno(lChiaveAnno);
						lFascRich.setChiaveProgr(lChiaveProgr);
						lFascRich.setChiaveUfficio(lUltimaCompetenza.getChiaveUfficio());

						IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
						FascicoloSiepModel lFasRet = lCtrl
								.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascRich);

						if (lFasRet == null || lFasRet.getIdFascicoloSiep() == null) {
							// errore procedimento non trovato
							RedirectTo lRedirigi = new RedirectTo();
							lRedirigi.setPage(IWebConstants.PG_MAIN);
							setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N. "
									+ lFascRich.getChiaveAnno() + "/" + lFascRich.getChiaveProgr()
									+ " non è stato trovato. Verificare di aver indicato correttamente i dati del procedimento.");
							lRedirigi.setAction(
									"siap.siep.modulocumulo.action.ActLoadDettaglioRichiestaAttiRicevuta"
											+ "&IdMessaggio=" + lIdMessaggio);
							setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
							return IWebConstants.PG_MESSAGE;
						} else {
							lUltimaCompetenza.setChiaveAnno(lChiaveAnno);
							lUltimaCompetenza.setChiaveProgr(lChiaveProgr);
						}
					}

					if (!this.isSessionAttributeNullObj("fascicolo")) {
						// Verifico se il fascicolo in sessione è quello richiesto
						FascicoloSiepModel lFascInSessMod = (FascicoloSiepModel) getSessionAttribute(
								"fascicolo");

						if (lFascInSessMod.getChiaveAnno().compareTo(lUltimaCompetenza.getChiaveAnno()) != 0
								|| lFascInSessMod.getChiaveProgr()
										.compareTo(lUltimaCompetenza.getChiaveProgr()) != 0
								|| lFascInSessMod.getChiaveUfficio()
										.equals(lUltimaCompetenza.getChiaveUfficio())) {
							// Ricerco il fasciolo e lo metto in sessione
							siesLogger.debug(
									"Il fascicolo attualmente in sessione non è quello richiesto. Carico il fascicolo corretto.");
							caricaFascicolo = true;
						}
					} else {
						// Carico il fasciolo richiesto in sessione
						siesLogger.debug("Nessun fascicolo in sessione. Carico il fascicolo richiesto.");
						caricaFascicolo = true;
					}

					if (caricaFascicolo) {
						FascicoloSiepModel lFasDaTrasmetterMod = new FascicoloSiepModel();
						lFasDaTrasmetterMod.setChiaveAnno(lUltimaCompetenza.getChiaveAnno());
						lFasDaTrasmetterMod.setChiaveProgr(lUltimaCompetenza.getChiaveProgr());
						lFasDaTrasmetterMod.setChiaveUfficio(lUltimaCompetenza.getChiaveUfficio());

						IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
						FascicoloSiepModel lFasRet = lCtrl
								.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasDaTrasmetterMod);

						// Il fascicolo in sessione non è quello corrente
						setSessionAttribute("fascicolo", lFasRet);
						setSessionAttribute("soggetto", lFasRet.getSoggetto());
						setSessionAttribute("sentenza", lFasRet.getSentenza());
					}

					// I dati sul Titolo RICHIESTO li prendo da quì
					this.setRequestAttribute("competenza", lUltimaCompetenza);

					// Cerco eventuale Ufficio di origine se trattasi di Ufficio accorpato
					if (lUltimaCompetenza.getChiaveAnno() != null
							&& lUltimaCompetenza.getChiaveProgr() != null) {
						UfficioAccorpatoModel lUfficioAccorpato = null;

						UfficioAccorpatoUtils lUffAccorpUtils = new UfficioAccorpatoUtils();
						lUfficioAccorpato = lUffAccorpUtils.getUfficioAccorpatoByCodAccorpanteProgr(
								lUltimaCompetenza.getChiaveUfficio(), lUltimaCompetenza.getChiaveProgr());

						if (lUfficioAccorpato != null) {
							this.setRequestAttribute("UfficioOld",
									getUfficioByCodUfficio(lUfficioAccorpato.getCodUfficio()));

							this.setRequestAttribute("UfficioAccorpatoOrigine", lUfficioAccorpato);
						}
					}

					// I Dati sul Titolo CUMULANTE li prendo da quì sotto
					FascicoloSiepModel fascicoloSIEP = lDettaglioFasModel.getFascicoloSiep();
					SentenzaModel sentenzaRicevuta = fascicoloSIEP.getSentenza();

					this.setRequestAttribute("fasFascicoloTrovato", fascicoloSIEP);
					this.setRequestAttribute("senFascicoloTrovato", sentenzaRicevuta);

					insertManuale = "0"; // 0 = i dati del titolo cumulante non sono imputabili dall'utente.
											// Vengono recuperati dalla
											// Richiesta
				} catch (Exception e) {
					// Errore nel parser
					siesLogger.error("Errore", e);

				}

			} // END if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) )

		} // END else di if(isArcDistribuita)

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Aggiunto controllo presenza evento non validato
		siesLogger.debug("Verifico presenza evento non validato");
		isEventoNonValidato();

		// ==========================================================================
		//
		// ==========================================================================
		// Posizione giuridica
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
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPos);
		setRequestAttribute("lcodicePosizione", lPos.getPosizioneGiuridica().getCodPosizioneGiuridica());
		// fine posizione giuridica

		// Pena Residua
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		// Combo tipo richiesta (TIPOLOGIA_ATTO nella form)
		Option lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaRC()); // Comunicazione/Richiesta
																							// con filtro su
																							// Comunicazione
		lOption.setFilter("12");
		setRequestAttribute("richiesta", "" + lOption);

		// richiesta oggetto (OGGETTO_ATTO nella form)
		Option lOggetto = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		lOggetto.setFilter("0599");
		setRequestAttribute("oggetto", "" + lOggetto);

		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ALTRO DESTINATARIO
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		setRequestAttribute("insertManuale", insertManuale);
		setRequestAttribute("titolo", "RIGETTO RICHIESTA TRASMISSIONE ATTI PER COMPETENZA");

		// return PG_LOAD_TRASMISSIONE_COMP;
		return PG_LOAD_RIGETTO_RICHIESTA_ATTI_TRASM_COMP;
	}

	/**
	 * Recupera solo i parametri di ricerca da passare alla JSP per la costruzione del link "torna indietro"
	 * 
	 * @return
	 * @throws F3BException
	 */
	// private String getQueryRequestUrl() throws F3BException {
	// String lRequest = ""; // this.getRequest().getRequestURL() + "?";
	// Set lKeys = getRequest().getParameterMap().keySet();
	//
	// Iterator itx = lKeys.iterator();
	// while (itx.hasNext()) {
	// String key = (String) itx.next();
	// // Copia di tutti gli attributi tranne LINK_RITORNO e FLAG_RITORNO
	// if (!key.equals(IWebConstants.LINK_RITORNO) && !key.equals(IWebConstants.FLAG_RITORNO)
	// && !key.equals(IWebConstants.ACTION_FIELD)) {
	// lRequest += key + "=" + getRequestStringParameter(key) + "&";
	// }
	// }
	//
	// if (lRequest.length() > 0) {
	// return lRequest.substring(0, lRequest.length() - 1);
	// } else {
	// return lRequest;
	// }
	//
	// }

}