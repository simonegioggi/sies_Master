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
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.action.ICostantiCompetenza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.action.ICostantiSiepJMS;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciTrasmissioneCompetenza Description: Classe Action per la load inserisci della
 * Trasmissione per Competenza
 * </p>
 *
 * @version 1.0
 */

public class ActLoadInserisciTrasmissioneSeguitoAtti extends ActionSiap implements ICostantiRichiesta {

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
		// siesLogger.debug(" --XX-- >>>>>>>>>>>>>>>>>>>>>>>>> START ");

		if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			siesLogger.debug(" --XX-- Messaggio id = "
					+ getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO));
			// Provengo dalla lista delle Richieste caricherò dopo il procedimento
			// corretto in funzione del fascicoli richiesto dopo le opportune verifiche
		} else if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// Verifico (?) se si sta lavorando in architettura distribuita
		// leggendo l'apposito flag su f3b.properties
		boolean isArcDistribuita = false;

		// ---------------------------------
		// flag per l'inserimento manuale
		// String insertManuale = "1";
		String insertManuale = "0";
		// ----------------------------------

		String lCodSeguito = "";

		if (isArcDistribuita) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Funzionalità in fase di Implementazione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		} else {
			if (!this.isRequestParameterNullObj(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO)) {
				// Provengo dalla form con i dati della ricerca procedimento (nel distretto)
				siesLogger.debug("--XX-- Provengo dalla form con i dati della ricerca procedimento");
				//
				insertManuale = "0";

				// ricavo i criteri di ricerca del fascicolo da trasmettere
				// dai dati inputati in form
				IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = null;

				lUfficio = lCtrlUfficio.getUfficioByCodTipoUffDescrComune(
						this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO),
						this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).toUpperCase());

				FascicoloSiepModel aFascMod = new FascicoloSiepModel();
				aFascMod.setChiaveUfficio(lUfficio.getCodUfficio());
				aFascMod.setChiaveAnno(
						getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
				aFascMod.setChiaveProgr(
						getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));

				IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
				FascicoloSiepModel findedFasc = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFascMod);

				// Se ho trovato il fascicolo leggo i dati che saranno precompilati in maschera
				if (findedFasc != null) {
					ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
					SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(findedFasc.getSenIdSentenza());

					setRequestAttribute("senFascicoloTrovato", aSent);
					setRequestAttribute("fasFascicoloTrovato", findedFasc);
				} else {
					RedirectTo lRedirigi = new RedirectTo();
					lRedirigi.setPage(IWebConstants.PG_MAIN);
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Fascicolo " + aFascMod.getChiaveAnno()
							+ "/" + aFascMod.getChiaveProgr() + " non trovato!");
					lRedirigi.setAction(
							"siap.siep.richiesta.action.ActLoadRicercaDistrettoTrasmissioneCompetenza&"
									+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
									+ getClass().getName());
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
					return IWebConstants.PG_MESSAGE;
				}

			} // fascicolo inputato dall'utente
			else if ((!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) || (!this
					.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_ID_COMPETENZA)
					&& !this.getRequestStringParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA).equals("")
					&& this.getRequestStringParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA) != null))) {

				BigDecimal lIdMessaggio = null;

				if (!this.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_ID_COMPETENZA)
						&& !this.getRequestStringParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA).equals("")
						&& this.getRequestStringParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA) != null) {
					// Provengo dal Dettaglio Trasmissione e devo fare Seguito Atti
					siesLogger.debug("--XX-- Provego dal Dettaglio Trasmissione e devo fare Seguito Atti");
					lCodSeguito = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);

					String IdComp = getRequestStringParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA);
					ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
					CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaById(new BigDecimal(IdComp));

					setRequestAttribute("Competenza", mComp);

					lIdMessaggio = mComp.getIdMessaggioRichiesta();
					MessaggioModel lMessModel = null;

					try {
						// Cerco Messaggio by Id_Messaggio
						IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
						lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);
					} catch (Exception e) {
						// Errore nel parser
						siesLogger.error("Errore Ricerca Messaggio by KEY - Id_Messaggio", e);
					}

					if (lMessModel != null && lMessModel.getIdMessaggio() != null) {
						lIdMessaggio = lMessModel.getIdMessaggio();
						// siesLogger.error("--XX-- Messaggio trovato By Id_Messaggio = "+lIdMessaggio);
					} else {
						try {
							// cerco Messaggio con ricerca by Correlation_Id
							IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
							lMessModel = lCrtl.ExRicercaMessaggioByCorrelationIdOnly(lIdMessaggio.toString());
						} catch (Exception e) {
							// Errore nel parser
							siesLogger.error(
									"Errore Ricerca Messaggio by CORR_KEY - Correlation_Id_Messaggio", e);
						}

						if (lMessModel != null && lMessModel.getIdMessaggio() != null) {
							lIdMessaggio = lMessModel.getIdMessaggio();
							// siesLogger.error("--XX-- Messaggio trovato by Correlation_ID - Id_Messaggio
							// trovato = "+lIdMessaggio);
						} else {
							// Seguito Atti Senza Messaggio di Richiesta ????? NON Dovrebbe mai entrare qui
							siesLogger.debug("--XX-- NON HO TROVATO la RICHIESTA - HO solo il Model Comp"
									+ mComp.getIdCompetenza());

							FascicoloSiepModel aFascMod = new FascicoloSiepModel();
							aFascMod.setChiaveUfficio(mComp.getChiaveUfficio());
							aFascMod.setChiaveAnno(mComp.getChiaveAnno());
							aFascMod.setChiaveProgr(mComp.getChiaveProgr());

							IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
							FascicoloSiepModel findedFasc = lCtrl
									.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aFascMod);

							// Se ho trovato il fascicolo leggo i dati che saranno precompilati in maschera
							if (findedFasc != null) {
								ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
								SentenzaModel aSent = lCrtlSentenza
										.ExRicercaSentenzaByKey(findedFasc.getSenIdSentenza());

								setRequestAttribute("senFascicoloTrovato", aSent);
								setRequestAttribute("fasFascicoloTrovato", findedFasc);
							}
							// else
							// { // Se non ho trovato il Fascicolo, prenderò i dati da Competenza
							// setRequestAttribute("Competenza", mComp);
							// }

						}
					}
				} else {
					// Provengo dal Richieste Atti Ricevuti - Dettaglio Richiesta - Trasmissione; NON DOVREBBE
					// Mai arrivare qui
					lIdMessaggio = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
					siesLogger
							.debug("--XX-- 11 Provego dal Messaggio di richiesta e devo fare Trasf per comp "
									+ getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO));
				}

				try {

					// Potrei non avere il procedimento da trasmettere in sessione o averne uno differente
					// Devo caricare il procedimento richiesto in sessione prima di procedere
					// siesLogger.debug("--XX-- Continuo da qui - Id = "+lIdMessaggio);
					IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
					MessaggioModel lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);

					// ========================================================================================================
					// se la Trsmissione è sulla stessa BDI, il BLOB è senza dati - prendo i Dati sul
					// Cumulante da Request
					// ============================================================================================================
					if (lMessModel.getCodBdiMittente().equals(lMessModel.getCodBdiDestinataria())) {
						// siesLogger.debug("--XX-- Stessa BDI - Id = "+lIdMessaggio);
					} else {
						// siesLogger.debug("--XX-- Diverse BDI - Id = "+lIdMessaggio);
					}
					setRequestAttribute("MessaggioRichiesta", lMessModel);

					if ("0740".equals(lCodSeguito)) {
						ParserMessage lParser = new ParserMessage(lMessModel.getTreeModel());
						DettaglioFascicoloModel lDettaglioFasModel = lParser.getDettaglioFascicoloSiep();

						DatiSiepPerTrasferimentoModel lDSPT = lDettaglioFasModel
								.getDatiSiepPerTrasferimento();
						Vector lListaCompetenze = (Vector) lDSPT.getListCompetenze();

						CompetenzaModel lUltimaCompetenza = null;
						lUltimaCompetenza = (CompetenzaModel) lListaCompetenze.lastElement();

						// Verifico il fascicolo richiesto e lo carico in sessione se non già
						// caricato
						boolean caricaFascicolo = false;

						// verifico se l'utente ha indicato i dati del fascicolo manualmente in
						// quanto non presenti sulla richiesta
						if (lUltimaCompetenza.getChiaveAnno() == null
								|| lUltimaCompetenza.getChiaveProgr() == null) {
							BigDecimal lChiaveAnno = getRequestBigDecimalParameter(
									ICostantiJMS.CHIAVE_ANNO_SIEP);
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

							if (lFascInSessMod.getChiaveAnno()
									.compareTo(lUltimaCompetenza.getChiaveAnno()) != 0
									|| lFascInSessMod.getChiaveProgr()
											.compareTo(lUltimaCompetenza.getChiaveProgr()) != 0
									|| !lFascInSessMod.getChiaveUfficio()
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

						// Passo il cumulante
						FascicoloSiepModel fascicoloSIEP = lDettaglioFasModel.getFascicoloSiep();
						SentenzaModel sentenzaRicevuta = fascicoloSIEP.getSentenza();

						this.setRequestAttribute("fasFascicoloTrovato", fascicoloSIEP);
						this.setRequestAttribute("senFascicoloTrovato", sentenzaRicevuta);

						//
						// Verifico se il facicolo da trasmettere è già associato a NSC
						FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

						String lAlertNSC = "Attenzione! Il Procedimento corrente (" + lFascMod.getChiaveAnno()
								+ "/" + lFascMod.getChiaveProgr() + ") non risulta ancora trasmesso a NSC.";
						if (lFascMod.getKeyProvvNsc() == null && this.isRequestParameterNullObj("warning")) { // Non
																												// bloccante
							lAlertNSC += " Per poter procedere alla trasmissione per competenza è consigliabile prima affettuare lo scarico su NSC. Si vuole procedere comunque?";
							setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
							setRequestAttribute(IWebConstants.MESSAGE_TEXT, lAlertNSC);

							setRequestAttribute("postaParametri", "S");

							return IWebConstants.PG_WARNING;
						}

						insertManuale = "0"; // 0 = i dati del titolo cumulante non sono imputabili
												// dall'utente. Vengono recuperati dalla
												// Richiesta
					} // Chiude lCodseguito

				} catch (Exception e) {
					// Errore nel parser
					siesLogger.error("Errore", e);

				}

			} // CHIUDE if( (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) ||
				// (!this.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_ID_COMPETENZA) &&

		} // CHIUDE la else di if(isArcDistribuita){

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Aggiunto controllo presenza evento non validato
		// siesLogger.debug("--XX-- Verifico presenza evento non validato");
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

		// Combo tipo richiesta
		Option lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaT());
		setRequestAttribute("richiesta", "" + lOption);

		// richiesta oggetto
		Option lOggetto = null;
		if (!lCodSeguito.equals("")) {
			lOggetto = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen());
			lOggetto.setFilter(new String[] { "-", "0740" });
		} else {
			lOggetto = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen());
			lOggetto.setFilter(new String[] { "-", "0340", "5403", "0740" });
		}

		setRequestAttribute("oggetto", "" + lOggetto);
		setRequestAttribute("seguito", lCodSeguito);

		Vector<DecodificheModel> lProvvedimenti = new Vector<>();
		lProvvedimenti.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lProvvedimenti.add(new DecodificheModel("01", "Sentenza", "", "", "", "", "", "", ""));
		lProvvedimenti.add(new DecodificheModel("02", "Decreto Penale", "", "", "", "", "", "", ""));
		Option lOptionProvvedimenti = new Option(lProvvedimenti);
		if (!this.isRequestAttributeNullObj("senFascicoloTrovato")
				&& this.getRequestAttribute("senFascicoloTrovato") != null) {
			lOptionProvvedimenti.setSelected(((SentenzaModel) this.getRequestAttribute("senFascicoloTrovato"))
					.getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoprovvedimento", "" + lOptionProvvedimenti);

		// UFFICIO EMITTENTE
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		if (!this.isRequestAttributeNullObj("senFascicoloTrovato")
				&& this.getRequestAttribute("senFascicoloTrovato") != null) {
			lOption.setSelected(((SentenzaModel) this.getRequestAttribute("senFascicoloTrovato"))
					.getCodTipoAutoritaEmittente());
		}
		setRequestAttribute("autoritaEmi", "" + lOption);

		// UFFICIO PM
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
		lOption.setFilter(new String[] { "-", "PM", "PGCAP", "PMM" });
		if (!this.isRequestAttributeNullObj("fasFascicoloTrovato")
				&& this.getRequestAttribute("fasFascicoloTrovato") != null) {
			lOption.setSelected(((FascicoloSiepModel) this.getRequestAttribute("fasFascicoloTrovato"))
					.getCodTipoUfficio());
		}
		setRequestAttribute("ufficioPM", "" + lOption);

		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ALTRO DESTINATARIO
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		setRequestAttribute("insertManuale", insertManuale);
		if (!lCodSeguito.equals(""))
			setRequestAttribute("titolo", "TRASMISSIONE PER COMPETENZA: SEGUITO ATTI");
		else
			setRequestAttribute("titolo", "TRASMISSIONE PER COMPETENZA");

		return PG_LOAD_TRASMISSIONE_COMP;

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
	// Iterator itx = lKeys.iterator();
	// while (itx.hasNext()) {
	// String key = (String) itx.next();
	// // Copia di tutti gli attributi tranne LINK_RITORNO e FLAG_RITORNO
	// if (!key.equals(IWebConstants.LINK_RITORNO) && !key.equals(IWebConstants.FLAG_RITORNO)
	// && !key.equals(IWebConstants.ACTION_FIELD)) {
	// lRequest += key + "=" + getRequestStringParameter(key) + "&";
	// }
	// }
	// if (lRequest.length() > 0) {
	// return lRequest.substring(0, lRequest.length() - 1);
	// } else {
	// return lRequest;
	// }
	// }

}