package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
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
@SuppressWarnings("rawtypes")
public class ActLoadInserisciTrasmissioneSeguitoAttidaRiscontro extends ActionSiap
		implements ICostantiRichiesta {

	/**
	 * Action richiamata in fase di ricerca del procedimento cumulante, oppure per apertura diretta senza
	 * richiesta
	 * 
	 * 
	 */
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
			// siesLogger.debug(" --XX-- Inizio con Messaggio id =
			// "+getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO));
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
			else if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)
					&& !this.getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO).equals("")
					&& this.getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) != null) {

				BigDecimal lIdMessaggio = null;
				Boolean isLeggibile = true;

				// Provengo dal Riscontro Trasmissione e devo fare Seguito Atti
				String lIdMess = getRequestStringParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
				siesLogger.debug("--XX-- Riscontro Trasmissione e Id Mess = " + lIdMess);

				lCodSeguito = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
				lIdMessaggio = new BigDecimal(lIdMess);
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
					// siesLogger.debug("--XX-- Messaggio Trovato by IdMess- Id = "+lIdMessaggio);
					lIdMessaggio = lMessModel.getIdMessaggio();
					if (lMessModel.getIsErroreParser()) {
						// siesLogger.debug("--XX-- Messaggio Trovato by KEY Ma BLOB NON Leggibile ... >>>>
						// ");
						isLeggibile = false;
					}

				} else {
					try {
						// provo con ricerca by Correlation_Id
						IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
						lMessModel = lCrtl.ExRicercaMessaggioByCorrelationIdOnly(lIdMessaggio.toString());
					} catch (Exception e) {
						// Errore nel parser
						siesLogger.error("Errore Ricerca Messaggio by CORR_KEY - Correlation_Id_Messaggio",
								e);
					}

					if (lMessModel != null && lMessModel.getIdMessaggio() != null) {
						// siesLogger.debug("--XX-- Messaggio Trovato by Correlation_IdMess - Id_Mess trovato
						// = "+lIdMessaggio);
						lIdMessaggio = lMessModel.getIdMessaggio();
						if (lMessModel.getIsErroreParser()) {
							// siesLogger.debug("--XX-- Messaggio Trovato by Correlation_IdMess Ma BLOB NON
							// Leggibile ... >>>> ");
							isLeggibile = false;
						}
					} else {
						// Seguito Atti Senza Messaggio di Richiesta ??????? NON DOVREBBE MAI ENTRARE QUI
						// !!!!!!
						siesLogger.debug("--XX-- NON HO TROVATO la RICHIESTA - ");

						FascicoloSiepModel aFascMod = new FascicoloSiepModel();
						aFascMod.setChiaveUfficio(
								getRequestStringParameter(ICostantiMessaggio.CAMPO_COD_UFFICIO_DESTINATARIO));
						aFascMod.setChiaveAnno(getRequestBigDecimalParameter(
								ICostantiMessaggio.CAMPO_CHIAVE_ANNO_CUMULANTE));
						aFascMod.setChiaveProgr(getRequestBigDecimalParameter(
								ICostantiMessaggio.CAMPO_CHIAVE_PROGR_CUMULANTE));

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
						} else {
							RedirectTo lRedirigi = new RedirectTo();
							lRedirigi.setPage(IWebConstants.PG_MAIN);
							setRequestAttribute(IWebConstants.MESSAGE_TEXT,
									"Fascicolo " + aFascMod.getChiaveAnno() + "/" + aFascMod.getChiaveProgr()
											+ " non trovato!");
							lRedirigi.setAction(
									"siap.siep.richiesta.action.ActLoadRicercaDistrettoTrasmissioneCompetenza&"
											+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
											+ getClass().getName());
							setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
							return IWebConstants.PG_MESSAGE;
						}

					}

				} // chiude la prima Else di if(lMessModel!=null && lMessModel.getIdMessaggio()!=null)

				try {

					IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
					MessaggioModel MessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);
					Boolean lSenzaRichiesta = false;

					if ("00066".equals(MessModel.getCodTipoOperazione())) {
						if (MessModel.getIdRichiesta() == null) {
							lSenzaRichiesta = true;
						}
					}

					setRequestAttribute("MessaggioRichiesta", MessModel);
					// ========================================================================================================
					// se la Trsmissione è sulla stessa BDI, il BLOB è senza dati - prendo i Dati sul
					// Cumulante da Request
					// ============================================================================================================
					if (MessModel.getCodBdiMittente().equals(MessModel.getCodBdiDestinataria())) {
						siesLogger.debug("--XX-- Messaggio con Blob NON valorizzato - Stessa BDI");

						FascicoloSiepModel aFascMod = new FascicoloSiepModel();
						aFascMod.setChiaveUfficio(
								getRequestStringParameter(ICostantiMessaggio.CAMPO_COD_UFFICIO_DESTINATARIO));
						aFascMod.setChiaveAnno(getRequestBigDecimalParameter(
								ICostantiMessaggio.CAMPO_CHIAVE_ANNO_CUMULANTE));
						aFascMod.setChiaveProgr(getRequestBigDecimalParameter(
								ICostantiMessaggio.CAMPO_CHIAVE_PROGR_CUMULANTE));

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
						} else {
							RedirectTo lRedirigi = new RedirectTo();
							lRedirigi.setPage(IWebConstants.PG_MAIN);
							setRequestAttribute(IWebConstants.MESSAGE_TEXT,
									"-Fascicolo " + aFascMod.getChiaveAnno() + "/" + aFascMod.getChiaveProgr()
											+ " non trovato!");
							lRedirigi.setAction(
									"siap.siep.richiesta.action.ActLoadRicercaDistrettoTrasmissioneCompetenza&"
											+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "="
											+ getClass().getName());
							setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
							return IWebConstants.PG_MESSAGE;
						}
					}
					// ======================================================================================
					// se la Trasmissione è tra Diverse BDI, occorre cercare il rec. COMPETENZA;
					//
					// se esiste una Richiesta di Trasmissione atti, allora nel Messaggio di Trasmissione Atti
					// (00066)
					// MESSAGGIO.Id_Richiesta è uguale a COMPETENZA.Id_Messaggio_Richiesta
					//
					// se NON esiste una Richiesta di Trasmissione atti, devo cercare il Rec. COMPETENZA
					// attraverso
					// il parsemessage del BLOB. Se il BLOB è leggibile OK; altrimenti.....bisogna andare su
					// EVENTO
					// ======================================================================================
					else {
						siesLogger.debug("--XX-- Seguito atti tra BDI Diverse  - ");
						CompetenzaModel lUltimaCompetenza = null;
						setRequestAttribute("MessaggioRichiesta", MessModel);

						if (lSenzaRichiesta) {
							siesLogger.debug(
									"--XX-- Seguito atti tra BDI Diverse e Senza Richiesta iniziale - ");

							// cerco comunque di recuperare Anno e Numero Fas. Cumulante, attraverso il
							// messaggio 00067
							MessaggioModel MessaggioEsito = null;
							MessaggioEsito = lCrtl.ExRicercaMessaggioByCorrelationIdOnly(
									MessModel.getIdMessaggio().toString());
							if (MessaggioEsito != null && MessaggioEsito.getIdMessaggio() != null) {
								if (MessaggioEsito.getChiaveAnnoSiep() != null
										&& MessaggioEsito.getChiaveProgrSiep() != null) {
									setRequestAttribute("AnnoFasCumulante",
											" " + MessaggioEsito.getChiaveAnnoFasCumulante());
									setRequestAttribute("ProgFasCumulante",
											" " + MessaggioEsito.getChiaveProgrFasCumulante());
								}
							}

							if (isLeggibile) {
								// siesLogger.debug("--XX-- Con Blob_Message Leggibile - ");
								ParserMessage lParser = new ParserMessage(MessModel.getTreeModel());
								DettaglioFascicoloModel lDettaglioFasModel = lParser
										.getDettaglioFascicoloSiep();

								DatiSiepPerTrasferimentoModel lDSPT = lDettaglioFasModel
										.getDatiSiepPerTrasferimento();
								Vector lListaCompetenze = (Vector) lDSPT.getListCompetenze();

								lUltimaCompetenza = (CompetenzaModel) lListaCompetenze.lastElement();
							} else {
								// siesLogger.debug("--XX-- Con Blob_Message NON Leggibile - ");
								lUltimaCompetenza = CercaCompetenzaByEvento(MessModel);
							}
						} else {
							siesLogger.debug(
									"--XX-- Seguito atti tra BDI Diverse e CON Id_Richiesta iniziale = "
											+ MessModel.getIdRichiesta());
							ICompetenza lCtrlC = SIEPLookupRemote.getCompetenzaRemote();
							lUltimaCompetenza = lCtrlC
									.ExRicercaCompetenzaByIdMessaggioRichiesta(MessModel.getIdRichiesta());
						}

						// siesLogger.debug("--XX-- Cpmpetenza = "+lUltimaCompetenza);
						setRequestAttribute("Competenza", lUltimaCompetenza);

						// ===========================================================================
						// Verifico il fascicolo richiesto e lo carico in sessione se non già caricato
						boolean caricaFascicolo = false;
						if (!this.isSessionAttributeNullObj("fascicolo")) {
							// Verifico se il fascicolo in sessione è quello richiesto
							FascicoloSiepModel lFascInSessMod = (FascicoloSiepModel) getSessionAttribute(
									"fascicolo");

							if (lFascInSessMod.getChiaveAnno().compareTo(MessModel.getChiaveAnnoSiep()) != 0
									|| lFascInSessMod.getChiaveProgr()
											.compareTo(MessModel.getChiaveProgrSiep()) != 0
									|| !lFascInSessMod.getChiaveUfficio()
											.equals(MessModel.getChiaveUfficioSiep())) {
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

							lFasDaTrasmetterMod.setChiaveAnno(MessModel.getChiaveAnnoSiep());
							lFasDaTrasmetterMod.setChiaveProgr(MessModel.getChiaveProgrSiep());
							lFasDaTrasmetterMod.setChiaveUfficio(MessModel.getChiaveUfficioSiep());

							IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
							FascicoloSiepModel lFasRet = lCtrl
									.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasDaTrasmetterMod);

							// Il fascicolo in sessione non è quello corrente
							setSessionAttribute("fascicolo", null);
							setSessionAttribute("soggetto", null);
							setSessionAttribute("sentenza", null);

							setSessionAttribute("fascicolo", lFasRet);
							setSessionAttribute("soggetto", lFasRet.getSoggetto());
							setSessionAttribute("sentenza", lFasRet.getSentenza());
						}

						// =========================================================================================
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
												// dall'utente.
												// Vengono recuperati dalla Richiesta

					} // CHIUDE la Else di
						// if(MessModel.getCodBdiMittente().equals(MessModel.getCodBdiDestinataria()) )

				} catch (Exception e) {
					siesLogger.error("Errore", e);
				}

			} // CHIUDE if( (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) ||
				// (!this.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_ID_COMPETENZA) &&

		} // CHIUDE la else di if(isArcDistribuita){

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Aggiunto controllo presenza evento non validato
		// siesLogger.debug("--XX-- Verifico presenza evento non validato - fascicolo in sessione =
		// "+lFascMod.getIdFascicoloSiep());
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

		// TIPO PROVVEDIMENTO

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

	} // CHIUDE processRequest

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

	private CompetenzaModel CercaCompetenzaByEvento(MessaggioModel aMessMod) throws F3BException {
		siesLogger.debug(
				"--XX-- CercaCompetenzaByEvento perchè NON esiste richiesta; Messaggio da cui prendo i dati = "
						+ aMessMod.getIdMessaggio());

		// Cerco i dati del fascicolo da Inviare
		FascicoloSiepModel lFasDaTrasmetterMod = new FascicoloSiepModel();
		lFasDaTrasmetterMod.setChiaveAnno(aMessMod.getChiaveAnnoSiep());
		lFasDaTrasmetterMod.setChiaveProgr(aMessMod.getChiaveProgrSiep());
		lFasDaTrasmetterMod.setChiaveUfficio(aMessMod.getChiaveUfficioSiep());

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasDaTrasmetterMod);

		// Cerco Provvedimento di Trasferimento per Competenza: Prima su EVENTO e poi sul relativo rec.
		// COMPETENZA
		EventoModel lEveMod = null;
		CompetenzaModel lComp = null;
		ICompetenza lCtrlC = SIEPLookupRemote.getCompetenzaRemote();
		IEvento lCtrlE = SICOLookupRemote.getEventoRemote();

		String[] aMotivo = { "0340" };
		String aTipoProvv = "31";
		String aTipoEve = "01";

		List lListEventi = lCtrlE.ExRicercaEventiNOTAnnullati(lFasRet.getIdFascicoloSiep(), aMotivo,
				aTipoProvv, aTipoEve);

		if (!lListEventi.isEmpty()) {
			for (int i = 0; i < lListEventi.size(); i++) {
				lEveMod = (EventoModel) lListEventi.get(i);
				if (lEveMod != null && lEveMod.getIdEvento() != null && lEveMod.getDataEmissione() != null) {
					if (lEveMod.getDataEmissione().before(aMessMod.getDataInvio())
							|| lEveMod.getDataEmissione().equals(aMessMod.getDataInvio())) {
						// siesLogger.debug("--XX-- CercaCompetenzaByEvento Data_Emissione =
						// "+lEveMod.getDataEmissione());
						lComp = lCtrlC.ExRicercaCompetenzaByEveIdEvento(lEveMod.getIdEvento());
						break;
					}
				}
			}
		}

		return lComp;

	}

}
