package siap.sico.stampa.controller;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteMagistratoSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.SiapStringUtil;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.archiviazione.dao.ArchiviazioneSqlDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSqlDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.dao.CircostanzaSqlDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.circostanza.util.CircostanzaUtil;
import siap.siep.competenza.dao.CompetenzaSqlDAO;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.cumulo.dao.CumuloSqlDAO;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.dao.FascicoloSiepPadreSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepPadreModel;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.motivoevento.dao.MotivoEventoSqlDAO;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.notiziareato.dao.NotiziaReatoSqlDAO;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.siep.nuovaistanza.dao.NuovaIstanzaSqlDAO;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.dao.PenaPrecedenteSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaPrecedenteModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.penaresidua.util.PenaResiduaUtil;
import siap.siep.penasospesa.controller.IPenaSospesa;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.refertoscarcerazione.dao.RefertoScarcerazioneSqlDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaSqlDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sollecitoesitotrasmissione.dao.SollecitoEsitoTrasmissioneSqlDAO;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.sospensione.controller.IInterruzione;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.PeriodoInterruzioneModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoesecuzione.controller.StatoEsecuzioneController;
import siap.siep.ulterioresanzionecumulo.dao.UlterioreSanzioneCumuloSqlDAO;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.util.SIESSwitch;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: Stampa Controller
 * </p>
 * <p>
 * Description: Classe Controller per le selezioni della Stampa
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaController extends SIAPStampaController implements IStampa {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * prelevaDatiEventoSiepXAnnotazioni
	 * 
	 * @param aEveModel
	 * @return il TreeModel che contiene anche tutte le annotazioni manuali
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiEventoSiepXAnnotazioni(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDAO = null;
		AnnotazioneManualeSqlDAO lAnnoSqlDao = null;

		TreeModel lTree = prelevaDatiEventoSiep(aEveModel, aUtenteModel);

		try {
			lConn = getDBConnection();

			lEveDAO = new EventoSqlDAO(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("NOME TEMPLATE (ID_TEMPLATE) > > > " + aEveModel.getNomeTemplate());

			// -----------------------------------------------------------------------------------------------------
			// -----------------------------------------------------------------------------------------------------
			// Gestione della stampa Ordine di scarcerazione provvisorio SIEP_OS_SCARPROVINDULTO.RTF
			//
			// Occorre gestire il seguente caso:
			// Se viene richiamata la stampa dell'Ordine di Scarcerazione Provvisiorio bisogna associare,
			// se presente, i dati dell'Annotazione Manuale relativa alla richiesta di indulto.
			// Per richiesta di indulto si intende un evento validato
			// (quindi e' sicuramente non annullato) di tipo 26-0290 oppure 26-0287
			// Se l'annotazione manuale corrisponde a quel tipo di evento si aggiunge come figlia
			// dell'evento corrente
			// -----------------------------------------------------------------------------------------------------
			// -----------------------------------------------------------------------------------------------------
			// 1- Se ANN_ID_ANNOTAZIONE_MANUALE su EVENTO e' pieno, ne ricerca l'annotazione manuale
			// corrispondente
			if (aEveModel != null && aEveModel.getEvento() != null
					&& aEveModel.getEvento().getAnnIdAnnotazioneManuale() != null) {
				BigDecimal lAnnIdAnnotazioneManuale = aEveModel.getEvento().getAnnIdAnnotazioneManuale();

				lAnnoSqlDao = new AnnotazioneManualeSqlDAO(lConn);

				lAnnoSqlDao.ricercaAnnotazioneManualeByKey(lAnnIdAnnotazioneManuale);
				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lAnnoSqlDao.getModelByKey();

				// 2- Se trova l'annotazione manuale ricerca tutti gli eventi che la referenziano
				// attraverso il campo ANN_ID_ANNOTAZIONE_MANUALE.
				// Gli eventi sono selezionati se validati e ordinati in ordine "decrescente"
				if (lAnnMod != null) {
					lEveDAO.ricercaEventoValidatoByAnnIdAnnotazioneManuale(lAnnIdAnnotazioneManuale);
					List lListaEventi = (List) lEveDAO.getModels();

					if (lListaEventi != null) {
						// Flag che indica l'esistenza dell'evento di tipo richiesta
						boolean lEventoTrovato = false;
						EventoModel lEveAnnMan = null;

						for (Iterator iter = lListaEventi.iterator(); iter.hasNext();) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.debug("Ciclo For");

							lEveAnnMan = (EventoModel) iter.next();

							// 3- Controlla che l'evento legato all'annotazione sia validato e sia del tipo
							// 0287 (Determinazione Pena - ex art. 671 c.p.p. e 174 c.p.)
							// 0290 (Applicazione Benefici - ex art. 174 c.p. e 672 c.p.p. in maschera
							// 'Applicazione benefici : Indulto')
							if (lEveAnnMan != null && "S".equals(lEveAnnMan.getFlagDocumentoRegistrato())
									&& lEveAnnMan.getCodTipoProvvedimento() != null
									&& lEveAnnMan.getCodMotivo() != null) {
								String lCodMotivo = lEveAnnMan.getCodMotivo();
								if (("26".equals(lEveAnnMan.getCodTipoProvvedimento()) && "0290"
										.equals(lCodMotivo))
										|| ("26".equals(lEveAnnMan.getCodTipoProvvedimento()) && "0287"
												.equals(lCodMotivo))) {
									// Formatta per il template la stringa reclusione e arresto
									lAnnMod.calcolaStringaReclusione();
									lAnnMod.calcolaStringaArresto();

									// Setta il Flag di Immediata Scarcerazione su Pena Residua
									// se data fine pena <= della data di sistema

									// Cerco il nodo PenaResidua
									PenaResiduaModel lPenRes = new PenaResiduaModel();
									TreeModel lTreePenRes = lTree.findTreeModel(lTree, lPenRes);

									if (lTreePenRes != null && lTreePenRes.getModel() != null) {
										lPenRes = (PenaResiduaModel) lTreePenRes.getModel();

										if (lPenRes.getDataFine() != null) {
											if (!DateUtils.isGreater(new Date(), lPenRes.getDataFine())) {
												lPenRes.setImmediataScarcerazione("S");
											}
										}
									}

									// Indica l'evento come trovato e esce dal ciclo
									lEventoTrovato = true;
									break;
								}
							}
						}

						// se l'evento richiesta e' stato trovato
						if (lEventoTrovato) {
							// 4- Cerca il TreeModel che contiene l'Evento corrente
							TreeModel lTreeEventoCorrente = null;
							Enumeration lEnumTree = lTree.breadthFirstEnumeration();

							while (lEnumTree.hasMoreElements()) {
								TreeModel lTreeElement = (TreeModel) lEnumTree.nextElement();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.debug(
										"Tree Element : " + lTreeElement.getModel().getClass().getName());
								String lNomeClasse = lTreeElement.getModel().getClass().getName();

								if ("siap.sico.evento.model.EventoModel".equals(lNomeClasse)) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
									siesLogger.debug("Evento Tree Element : " + lNomeClasse);

									EventoModel lEveMod = (EventoModel) lTreeElement.getModel();
									if ("S".equals(lEveMod.getEventoCorrente())) {
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
										siesLogger.debug("Evento Corrente : " + lNomeClasse);

										lTreeEventoCorrente = lTreeElement;
										// Quando trova il nodo dell'evento corrente esce dal ciclo
										break;
									}
								}
							}

							// 5- Aggiunge all'Evento corrente il nodo contenente l'Annotazione Manuale
							TreeModel lTreeAnnManEventoOS = new TreeModel(lAnnMod);
							TreeModel lTreeEvento = new TreeModel(lEveAnnMan);
							lTreeAnnManEventoOS.add(lTreeEvento);
							lTreeEventoCorrente.add(lTreeAnnManEventoOS);
						}
					}
				}
			}
			// -----------------------------------------------------------------------------------------------------
			// -----------------------------------------------------------------------------------------------------
			// Fine Gestione della stampa Ordine di scarcerazione provvisorio SIEP_OS_SCARPROVINDULTO.RTF
			// -----------------------------------------------------------------------------------------------------
			// -----------------------------------------------------------------------------------------------------

			// Cerco il nodo FascicoloSiep su cui attaccare il nodo di Annotazioni manuali
			FascicoloSiepModel lUltAnn = new FascicoloSiepModel();
			TreeModel lTreeUlt = lTree.findTreeModel(lTree, lUltAnn);
			if (lTreeUlt != null) {
				this.getTreeUltimeAnnotazioniManuali(aEveModel.getEvento().getFasSieIdFascicoloSiep(),
						lTreeUlt, aEveModel.getEvento());
			}

			// Aggiunge nodi di evento precedente
			TreeModel lTreeEventoPrec = null;
			String codiceMotivo = null;

			if (aEveModel.getEvento().getEveIdEvento() != null) {
				lEveDAO.ricercaEventoByKey(aEveModel.getEvento().getEveIdEvento());
				EventoModel lEveModRidet = (EventoModel) lEveDAO.getModelByKey();

				EventoModel lEveModRic = new EventoModel();
				lEveModRic.setFasSieIdFascicoloSiep(aEveModel.getEvento().getFasSieIdFascicoloSiep());
				lEveModRic.setCodTipoProvvedimento("04");
				lEveModRic.setCodTipoEvento("01");
				lEveModRic.setFlagDocumentoRegistrato("S");
				if (lEveModRidet.getCodMotivo().equals("0284")) // Amnistia/indulto
				{
					codiceMotivo = "0122";
				} else if (lEveModRidet.getCodMotivo().equals("0285")) // Depenalizzazione
				{
					codiceMotivo = "0210";
				} else if (lEveModRidet.getCodMotivo().equals("0286")) // Incostituzionalita'
				{
					codiceMotivo = "0211";
				}

				lEveModRic.setCodMotivo(codiceMotivo);

				// STUB 24/10/2005 REWORK STATO ESECUZIONE
				String[] lMotivi = { codiceMotivo };
				String[] lProvv = { "04", "26" };
				lEveDAO.ricercaEventoPerMotivoPerProvv(lMotivi, lProvv, lEveModRic);

				EventoModel lEvePrec = (EventoModel) lEveDAO.getModelByKey();
				if (lEvePrec != null) {
					if (lEveModRidet != null
							&& (lEveModRidet.getCodMotivo().equals("0284")
									|| lEveModRidet.getCodMotivo().equals("0285") || lEveModRidet
									.getCodMotivo().equals("0286"))) {
						lTreeEventoPrec = getTreeEventoPrecedenteAnnotazioniManuali(
								lEvePrec.getFasSieIdFascicoloSiep(), lEvePrec.getCodMotivo());
						if (lTreeEventoPrec != null)
							lTree.add(lTreeEventoPrec);
					}
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiepXAnnotazioni: " + daoEx, daoEx);
			throw new F3BException("StampaController.prelevaDatiEventoSiepXAnnotazioni: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiepXAnnotazioni: " + sqe, sqe);
			throw new F3BException("StampaController.prelevaDatiEventoSiepXAnnotazioni: Eccezione Generica: "
					+ sqe);
		} finally {
			cleanup(lEveDAO);
			cleanup(lAnnoSqlDao);

			cleanup(lConn);
		}

		return lTree;
	}

	/**
	 * Preleva i dati per le stampe del cumulo
	 * 
	 * @param aEveModel
	 * @param aUtenteModel
	 * @return
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiEventoSiepXCumulo(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		SoggettoSqlDAO lSogDao = null;
		SentenzaSqlDAO lSenDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		ResidenzaSqlDAO lResDao = null;
		FascicoloSiepPadreSqlDAO lFasPaDao = null;
		CumuloSqlDAO lCumDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		PenaCumuloSqlDAO lPenCumDao = null;
		UlterioreSanzioneCumuloSqlDAO lUltSanCumDao = null;
		PenaResiduaSqlDAO lPenDao = null;
		LicenzaLibanticipataSqlDAO lLibDAO = null;
		UfficioSqlDAO lUffSqlDAO = null;
		PenaResiduaSqlDAO lPenResDao = null;

		BigDecimal lKeyFascicolo = aEveModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lConn = getDBConnection();

			lPenCumDao = new PenaCumuloSqlDAO(lConn);
			// Aggiunge Nodo STRINGA ufficio
			lUffSqlDAO = new UfficioSqlDAO(lConn);
			lUffSqlDAO.selUfficioByCod(aUtenteModel.getUfficioUtente().getCodUfficio());
			UfficioModel lModel = (UfficioModel) lUffSqlDAO.getModelByKey();
			if (lModel != null) {
				lModel.settaStringaUfficio();
			}

			UfficioModel lModelVuoto = new UfficioModel();
			lModelVuoto.setStringaUfficio(lModel.getStringaUfficio());
			TreeModel lTreeStringaUfficio = new TreeModel(lModelVuoto);

			// Aggiunge nodi di evento e notifica
			lTreeRoot = new TreeModel(createRoot(aEveModel, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			// cumulo
			TreeModel lPenCumModTree = getPenaCumulo(lKeyFascicolo, lConn);
			lTreeRoot.add(lPenCumModTree);
			lTreeRoot.add(lTreeStringaUfficio);

			// Fascicolo Padre
			lFasPaDao = new FascicoloSiepPadreSqlDAO(lConn);
			lFasPaDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepPadreModel lFasModel = (FascicoloSiepPadreModel) lFasPaDao.getModelByKey();

			TreeModel lTreeFasPadreMod = new TreeModel(lFasModel);

			// Soggetto Residenza Alias
			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn, null);

			// Ulteriore Sanzione Cumulo
			lUltSanCumDao = new UlterioreSanzioneCumuloSqlDAO(lConn);
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();
			lFasMod.setIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
			lUltSanCumDao.ricercaUlterioreSanzioneCumuloByFascicoloCumulante(lFasMod);
			Vector lUltSanzioniCum = new Vector(lUltSanCumDao.getModels());
			UlterioreSanzioneCumuloModel lUltMod = new UlterioreSanzioneCumuloModel();

			if (lUltSanzioniCum != null && lUltSanzioniCum.size() > 0) {
				Iterator lUlteriori = lUltSanzioniCum.iterator();
				while (lUlteriori.hasNext()) {
					lUltMod = (UlterioreSanzioneCumuloModel) lUlteriori.next();
					lTreeFasPadreMod.add(new TreeModel(lUltMod));
				}
			}

			// Cumulo
			lCumDao = new CumuloSqlDAO(lConn);
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lSenDao = new SentenzaSqlDAO(lConn);

			// Modifica per far uscire le sentenze di cumulo in modo corretto -- 28-07-2005 --Dario --Luciana
			lCumDao.ricercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lKeyFascicolo);
			Vector lCumuli = new Vector(lCumDao.getModels());
			CumuloModel lCumMod = new CumuloModel();

			if (lCumuli != null && lCumuli.size() > 0) {
				Iterator lCumulo = lCumuli.iterator();
				while (lCumulo.hasNext()) {
					lCumMod = (CumuloModel) lCumulo.next();
					TreeModel lTreeCumuloMod = new TreeModel(lCumMod);

					if (lCumMod != null && lCumMod.getIdCumulo() != null) {
						// Pena Cumulo
						lPenCumDao.ricercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
						PenaCumuloModel lPenCum = (PenaCumuloModel) lPenCumDao.getModelByKey();
						if (lPenCum != null) {
							lPenCum.calcolaStringaReclusione();
							lPenCum.calcolaStringaArresto();
							lPenCum.calcolaStringaReclusioneSosp();
							lPenCum.calcolaStringaArrestoSosp();
							lPenCum.calcolaStringaIsolamento();

							lTreeCumuloMod.add(new TreeModel(lPenCum));
						}

						// fascicoli Cumulati
						if (lCumMod.getIdFascicoloSiepCumulato() != null) {
							lFasDao.ricercaFascicoloByKey(lCumMod.getIdFascicoloSiepCumulato());
							FascicoloSiepModel lFasCumalto = (FascicoloSiepModel) lFasDao.getModelByKey();
							TreeModel lTreeFasCumulatoMod = new TreeModel(lFasCumalto);

							// sentenze cumalante
							if (lFasCumalto != null && lFasCumalto.getSenIdSentenza() != null) {
								lSenDao.ricercaSentenzaBykey(lFasCumalto.getSenIdSentenza());
								SentenzaModel lSenteModel = (SentenzaModel) lSenDao.getModelByKey();
								// Nel caso il numero sentenza cominci per NC non viene considerato
								// il resto del dato
								if (lSenteModel != null && lSenteModel.getNumeroSentenza() != null
										&& lSenteModel.getNumeroSentenza().toUpperCase().startsWith("NC")) {
									lSenteModel.setNumeroSentenza("NC");
								}
								lTreeFasCumulatoMod.add(new TreeModel(lSenteModel));
							}

							lTreeCumuloMod.add(lTreeFasCumulatoMod);
						} else if (lCumMod.getSenIdSentenza() != null) // per sentenza di tipo 13 che nn sono
																		// associate a fascicoli
						{
							lSenDao.ricercaSentenzaBykey(lCumMod.getSenIdSentenza());
							SentenzaModel lSeModel = (SentenzaModel) lSenDao.getModelByKey();
							lTreeCumuloMod.add(new TreeModel(lSeModel));
						}

						lTreeFasPadreMod.add(lTreeCumuloMod);
					}
				}
			}

			// Liberazione Anticipata
			lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
			lLibDAO.ricercaLicenzaLibanticipataUltimaByIDFascicoloSIEP(lKeyFascicolo);
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibDAO.getModelByKey();
			if (lLibAnt != null && lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("N")) {
				lTreeFasPadreMod.add(new TreeModel(lLibAnt));
			}

			// Pena Residua
			lPenDao = new PenaResiduaSqlDAO(lConn);
			lPenDao.ricercaPenaResiduaFlagNonValidatoDesc(lKeyFascicolo);
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenDao.getModelByKey();

			// Stringa Arresto - Reclusione
			if (lPenResMod != null) {
				lPenResMod.calcolaStringaReclusione();
				lPenResMod.calcolaStringaArresto();
				lPenResMod.calcolaStringaIsolamento();

				if (lPenResMod.getDataFine() != null
						&& lPenResMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
						// GDV 23/10/2006 a6-rr-328
						&& lPenResMod.getFlagErgastolo() != null
						&& !lPenResMod.getFlagErgastolo().equals("S")
						&& !lPenResMod.getFlagErgastolo().equals("D")) {
					lPenResMod.setImmediataScarcerazione("S");
				} else {
					if (lPenResMod != null)
						lPenResMod.setImmediataScarcerazione("N");
				}
			}

			lTreeFasPadreMod.add(new TreeModel(lPenResMod));

			// Avvocati
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
			Vector lAvvocati = new Vector(lAvvDao.getModels());

			aEveModel.getEvento().setEventoCorrente("S");

			// STATO ESECUZIONE
			aEveModel.setEvento(this.mEventoUtils.setEventoDiStampa(aEveModel.getEvento(), lConn));

			TreeModel lTreeEveMod = new TreeModel(aEveModel.getEvento());

			// Magistrato
			if (aEveModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEveModel.getMagistrato()));

			this.mEventoUtils.appendNotifiche(lTreeEveMod, aEveModel, lAvvocati, aUtenteModel);
			lTreeRoot.add(lTreeEveMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Prima di appendere lo Stato di Esecuzione = = = "
					+ SIESSwitch.isReworkStatoEsecuzioneOn());

			if (SIESSwitch.isReworkStatoEsecuzioneOn()) {
				// Se sono nel rework dello stato esecuzione chiamo il nuovo
				// controller che gestisce lo stato esec.

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Prima di appendere lo Stato di Esecuzione");
				StatoEsecuzioneController lStat = new StatoEsecuzioneController();
				lStat.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Ho appeso lo Stato di Esecuzione");

			} else
				this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);

			// Nodi livello 1 Fascicolo - Soggetto - Sentenza

			this.appendTableToFascicoloSiep(lConn, lKeyFascicolo, lTreeFasPadreMod,
					lFasModel.getFlagAltraCausa());

			Iterator lItx = null;

			// Add Avvocati per Fascicolo
			if (lAvvocati != null) {
				lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
					TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
					lTreeFasPadreMod.add(lTreeAvvMod);
					lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
				}
			}
			lTreeRoot.add(lTreeFasPadreMod);

			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(getTreeSentenza(lFasModel, lConn));

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiepXCumulo: " + daoEx, daoEx);
			throw new F3BException("StampaController.prelevaDatiEventoSiepXCumulo: " + daoEx);
		}

		catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiepXCumulo: " + sqe, sqe);
			throw new F3BException("StampaController.prelevaDatiEventoSiepXCumulo: Eccezione Generica: "
					+ sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lSogDao);
			cleanup(lAvvDao);
			cleanup(lResDao);
			cleanup(lPenDao);
			cleanup(lFasPaDao);
			cleanup(lPenCumDao);
			cleanup(lCumDao);
			cleanup(lLibDAO);
			cleanup(lUltSanCumDao);
			cleanup(lUffSqlDAO);
			cleanup(lPenResDao);

			cleanup(lConn);

		}

		return lTreeRoot;
	}

	/**
	 * preleva Dati Evento Siep
	 * 
	 * @param aEveModel
	 * @return TreeModel
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiEventoSiep(EventoNotificaModel aEveModel) throws F3BException {
		return prelevaDatiEventoSiep(aEveModel, null);
	}

	/**
	 * Crea l'albero di entita' ricavandole da opportune selezioni da DB
	 * 
	 * @param aEveModel
	 * @return TreeModel
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiEventoSiep(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {

		TreeModel lTreeRoot = null;

		Connection lConn = null;

		ReatoSqlDAO lReaDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		FungibilitaSqlDAO lFungDAO = null;
		PenaResiduaSqlDAO lPenResDao = null;
		PenaPrecedenteSqlDAO lPenPrecDao = null;
		RefertoScarcerazioneSqlDAO lRefScarcDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		LicenzaLibanticipataSqlDAO lLibDAO = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		ArchiviazioneSqlDAO lArcSqlDao = null;
		MotivoEventoSqlDAO lMotEveSqlDao = null;
		PenaAccessoriaSqlDAO lPenAccDAO = null;
		SospensioneSqlDAO lSospSql = null;
		SanzioneSostResiduaSqlDAO lSSSqlDAO = null;
		NuovaIstanzaSqlDAO lIstSqlDao = null;
		SollecitoEsitoTrasmissioneSqlDAO lSollecitoEsitoSqlDao = null;

		SospensioneModel lSospMod = null;
		TreeModel lSospTree = null;
		StampaMAUtils lStampa = new StampaMAUtils();
		StampaEventoUtils lStampaEvento = new StampaEventoUtils();

		BigDecimal lKeyFascicolo = aEveModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lConn = getDBConnection();

			// Fascicolo

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr(): " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			// Posizione Giuridica
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(lKeyFascicolo);
			PosizioneGiuridicaModel lPos = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();

			// Aggiunge nodi di evento e notifica
			lTreeRoot = new TreeModel(createRoot(aEveModel, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			// cumulo
			if (lFasModel.getFlagCumulante() != null && lFasModel.getFlagCumulante().equals("S")) {
				TreeModel lPenCumModTree = getPenaCumulo(lKeyFascicolo, lConn);
				lTreeRoot.add(lPenCumModTree);
			}

			// setto Stringa AssenzaDa e AssenzaA Del Ripristino interruzione

			if (aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null
					&& aEveModel.getEvento().getCodMotivo().equals("0272")) {
				IInterruzione lCtrlInterr = SIEPLookupRemote.getInterruzioneRemote();
				List lListaInterruzioni = lCtrlInterr.ExRicercaPeriodiInterruzione(lKeyFascicolo);

				if (lListaInterruzioni != null && !lListaInterruzioni.isEmpty()) {
					// QUANTUM INTERRUZIONE
					PeriodoInterruzioneModel lPeriodo = (PeriodoInterruzioneModel) lListaInterruzioni
							.get(lListaInterruzioni.size() - 1);
					CalendarModel Quantum = lPeriodo.getQuantum();

					lSospMod = new SospensioneModel();

					lSospMod.setAssenzaDa(Quantum.getDataInizio());
					lSospMod.setAssenzaA(Quantum.getDataFine());

					lSospTree = new TreeModel(lSospMod);
				}
			}

			// Ricerca Pena Residua e Pena Precedente
			// Prendo entrambe utilizzando il PenaPrecedenteSqlDAO
			lPenPrecDao = new PenaPrecedenteSqlDAO(lConn);
			lPenPrecDao.ricercaPenaPrecedenteByFascicolo(lKeyFascicolo);
			Vector lPrec = new Vector(lPenPrecDao.getModels());

			PenaResiduaModel lPenResMod = null;

			if (lPrec != null && lPrec.size() > 0) {
				PenaPrecedenteModel lPenPrecedente = new PenaPrecedenteModel();
				// Prendo il primo elemento del vettore... La pena residua!
				lPenPrecedente = (PenaPrecedenteModel) lPrec.firstElement();
				// Presa la Pena Residua
				lPenResMod = lPenPrecedente.getPenaResiduaModel();
				// Stringa Arresto - Reclusione
				if (lPenResMod != null) {
					lPenResMod.setPenaResiduaPerStampa(lPos);
				}
				lPenPrecDao.stop();
			}

			// calcola stinghe residue SE e' SIMEONE
			if (aEveModel.getEvento() != null
					&& aEveModel.getEvento().getCodMotivo() != null
					&& (aEveModel.getEvento().getCodMotivo().equals("0061")
							|| aEveModel.getEvento().getCodMotivo().equals("0062") || aEveModel.getEvento()
							.getCodMotivo().equals("0063"))) {
				if (!lPos.isLibero()) {
					PenaResiduaModel lPenaModel = null;

					if (lPenResMod.getDataFine() != null || lPenResMod.getDataFinePresunta() != null) {
						if (lPenResMod.getDataFine() == null) {
							lPenResMod.setDataFine(lPenResMod.getDataFinePresunta());
						}

						lPenaModel = PenaResiduaUtil.calcolaPenaNuovaDataInizio(DateUtils.getSysDate(),
								lPenResMod, true);
						lPenaModel.calcolaStringaReclusione();
						lPenaModel.calcolaStringaArresto();
						lPenaModel.calcolaStringaIsolamento();

						lPenResMod.setStringaIsolamentoDiurno(lPenaModel.getStringaIsolamentoDiurno());
						lPenResMod.setStringaArrestoResidua(lPenaModel.getStringaArrestoResidua());
						lPenResMod.setStringaReclusioneResidua(lPenaModel.getStringaReclusioneResidua());
						if (lPenaModel.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)
							lPenResMod.setImportoAmmendaResidua(lPenaModel.getImportoAmmenda());
						if (lPenaModel.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
							lPenResMod.setImportoMultaResidua(lPenaModel.getImportoMulta());
					}
				} else {
					if (lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)
						lPenResMod.setImportoAmmendaResidua(lPenResMod.getImportoAmmenda());
					if (lPenResMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
						lPenResMod.setImportoMultaResidua(lPenResMod.getImportoMulta());
				}
			}

			if (lPenResMod != null && lPenResMod.getDataFine() != null
					&& lPenResMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
					// GDV 23/10/2006 a6-rr-328
					&& lPenResMod.getFlagErgastolo() != null && !lPenResMod.getFlagErgastolo().equals("S")
					&& !lPenResMod.getFlagErgastolo().equals("D")) {
				lPenResMod.setImmediataScarcerazione("S");
			} else {
				if (lPenResMod != null)
					lPenResMod.setImmediataScarcerazione("N");
			}

			PenaPrecedenteModel lPrecModel = null;
			// Prese tutte le pene residue ordinate per Data, prendo la penultima...
			// Per avere la Pena Rasidua Precedente
			if (lPrec != null) {
				Iterator lPenPrecItx = lPrec.iterator();
				while (lPenPrecItx.hasNext()) {
					lPrecModel = (PenaPrecedenteModel) lPenPrecItx.next();
					if (lPrecModel.getFlagValidato() != null && lPrecModel.getFlagValidato().equals("S")) {
						// Stringa Arresto - Reclusione
						if (lPrecModel != null) {
							lPrecModel.setPenaPrecedentePerStampa(lPos);
						}
						break; // Trovata la prima pena precedente validata esco dal while
					}
				}
			}

			TreeModel lTreeFasMod = new TreeModel(lFasModel);

			// Decreto Ordinanza Siep
			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecSqlDao.ricercaDecretoOrdinanzaSiepByFascicoloSiepDataInsDesc(lKeyFascicolo);
			DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDecSqlDao.getModelByKey();
			if (lDecMod != null) {
				lDecMod.calcolaStringaRinvio();
				TreeModel lTreeDecOrd = new TreeModel(lDecMod);
				lTreeFasMod.add(lTreeDecOrd);
			}

			// Avvocati
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
			Vector lAvvocati = new Vector(lAvvDao.getModels());

			aEveModel.getEvento().setEventoCorrente("S");
			aEveModel.setEvento(this.mEventoUtils.setEventoDiStampa(aEveModel.getEvento(), lConn));

			// ========================================================================
			// Creo il TreeModel per l'evento CORRENTE e aggiungo i TreeModel figli di
			// interesse per l'evento.
			// - FungibilitaModel (eventuale)
			// - Notifiche
			// - AutoritaEsterna
			// - ArchiviazioneModel (eventuale)
			// - MotivoEventoModel (eventuale)
			// - MagistratoModel (firmatario)
			// - Pena Accessorie
			// - CampoNota
			// - CompetenzaModel
			// ========================================================================
			TreeModel lTreeEveMod = new TreeModel(aEveModel.getEvento());

			// Fungibilita
			lFungDAO = new FungibilitaSqlDAO(lConn);
			BigDecimal lIdEventoFung = null;

			if ("0998".equals(aEveModel.getEvento().getCodMotivo()) || // MEV 29 - Punto 17 nel caso di
																		// ridimensionamento LA,
					"0369".equals(aEveModel.getEvento().getCodMotivo()) || // MEV 37 - Inizio
					"0158".equals(aEveModel.getEvento().getCodMotivo())) { // MEV 37 - Fine
				// la Fungibilita' e' legata all'annotazione collegata al provvedimento, e non al
				// provvedimento stesso
				lIdEventoFung = aEveModel.getEvento().getEveIdEvento();
			} else {
				lIdEventoFung = aEveModel.getEvento().getIdEvento();
			}

			lFungDAO.ricercaFungibilitaByKeyEvento(lIdEventoFung);
			FungibilitaModel lFung = (FungibilitaModel) lFungDAO.getModelByKey();

			if (lFung != null) {
				if (lPenResMod != null && lPenResMod.getDataFine() != null
						&& DateUtils.isEquals(lPenResMod.getDataFine(), DateUtils.getSysDate())) {
					lFung.setDataOdierna("S");
				} else {
					lFung.setDataOdierna("N");
				}

				lFung.calcolaStringaFungibilita();
				lTreeEveMod.add(new TreeModel(lFung));
			}

			// Aggiungo i nodi relativi alle Notifiche (NotificheModel/AutoritaEsterna)
			// all'evento corrente
			this.mEventoUtils.appendNotifiche(lTreeEveMod, aEveModel, lAvvocati, aUtenteModel);

			// In caso di Archiviazione aggiungo il nodo ArchiviazioneModel
			lArcSqlDao = new ArchiviazioneSqlDAO(lConn);
			lArcSqlDao.ricercaArchiviazioneByIdEvento(aEveModel.getEvento().getIdEvento());
			ArchiviazioneModel lArchMod = (ArchiviazioneModel) lArcSqlDao.getModelByKey();
			if (lArchMod != null) {
				lTreeEveMod.add(new TreeModel(lArchMod));
			}

			// Motivo Evento
			lMotEveSqlDao = new MotivoEventoSqlDAO(lConn);
			lMotEveSqlDao.ricercaMotivoEventoByEveIdEvento(aEveModel.getEvento().getIdEvento());
			MotivoEventoModel lMotEve = (MotivoEventoModel) lMotEveSqlDao.getModelByKey();

			if (lMotEve != null) {
				lTreeEveMod.add(new TreeModel(lMotEve));
			}

			// Magistrato
			if (aEveModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEveModel.getMagistrato()));

			// STUB 09-02-2006 Pena Accessoria
			if (aEveModel.getEvento().getPenAccIdPenaAccessoria() != null) {
				PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
				lPenMod.setIdPenaAccessoria(aEveModel.getEvento().getPenAccIdPenaAccessoria());

				lPenAccDAO = new PenaAccessoriaSqlDAO(lConn);
				lPenAccDAO.ricercaPenaAccessoria(lPenMod);
				lPenMod = (PenaAccessoriaModel) lPenAccDAO.getModelByKey();
				if (lPenMod != null) {
					lTreeEveMod.add(new TreeModel(lPenMod));
				}
			}

			// Campo Nota
			// AMBROSINO 29/09/2010 : Nel caso di Evento con Cod.Motivo = 5415 (Comunicazione Variazione
			// Decorrenza e
			// scadenza Misura), il CampoNota da prelevare e' legato all'Evento padre, e cioe'
			// all'Evento con Cod.Motivo = 5414 (Annotazione Pervenimento richiesta variazione
			// data Inizio Misura;
			// 20/12/2010 Prima di effettuare un controllo puntuale occorre controllare la validita' del dato.
			if (aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null
					&& aEveModel.getEvento().getCodMotivo().equals("5415")) {
				this.prelevaDatiCampoNota(lTreeEveMod, aEveModel.getEvento().getEveIdEvento(), lConn);
			} else {
				this.prelevaDatiCampoNota(lTreeEveMod, aEveModel.getEvento().getIdEvento(), lConn);
			}

			lTreeRoot.add(lTreeEveMod);

			// Competenza
			this.prelevaDatiCompetenza(lTreeEveMod, aEveModel.getEvento().getIdEvento(), lConn);
			lTreeRoot.add(lTreeEveMod);

			// ========================================================================
			// Nel caso degli eventi di rideterminazione Pena Altro (versione 4.0) devo
			// aggiungere all'evento corrente anche l'evento di annotazione collegato.
			// Tale evento potrebbe essere non validato in quanto in alcuni casi si
			// procede alla validazione contestuale, quindi non essedo validato non
			// compare negli eventi dello stato di esecuzione.
			if (isRidetPenaAltro(aEveModel.getEvento())) {
				this.prelevaDatiAnnRidetPenaAltro(lTreeEveMod, aEveModel.getEvento().getEveIdEvento(), lConn);
			} else if (aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null
					&& aEveModel.getEvento().getCodMotivo().equals("1007")) {
				this.prelevaDatiAnnRidetPenaAltro(lTreeEveMod, aEveModel.getEvento().getIdEvento(), lConn);
			} // AMBROSINO 06/2011 - per template Annota pagamento PenaPecuniaria

			// Aggiunto la pena Precedente al tree del Fascicolo
			if (lPrecModel != null)
				lTreeFasMod.add(new TreeModel(lPrecModel));

			// ---- this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);

			if (SIESSwitch.isReworkStatoEsecuzioneOn()) {
				// Se sono nel rework dello stato esecuzione chiamo il nuovo
				// controller che gestisce lo stato esec.

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Prima di appendere lo Stato di Esecuzione");
				StatoEsecuzioneController lStat = new StatoEsecuzioneController();
				lStat.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Ho appeso lo Stato di Esecuzione");

			} else
				this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);

			// Nodi livello 1 Fascicolo - Soggetto - Sentenza
			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn,
					lFasModel);

			this.appendTableToFascicoloSiep(lConn, lKeyFascicolo, lTreeFasMod, lFasModel.getFlagAltraCausa());
			lSospSql = new SospensioneSqlDAO(lConn);
			if (lPenResMod != null) // Pena Residua
			{
				// sanzione sostitutiva residua -- dario -- 26-03-2008
				lSSSqlDAO = new SanzioneSostResiduaSqlDAO(lConn);
				lSSSqlDAO.ricercaUltimaSanzioneSostResiduaByIdFasc(lKeyFascicolo, "N");
				SanzioneSostResiduaModel lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO
						.getModelByKey();

				if (lSSResiduaModel != null) {
					TreeModel lTreeSSResiduaMod = new TreeModel(lSSResiduaModel);
					lTreeFasMod.add(lTreeSSResiduaMod);
				}

				TreeModel lTreePenResMod = new TreeModel(lPenResMod);

				if (aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null
						&& aEveModel.getEvento().getCodMotivo().equals("0272")) {
					if (lSospMod != null)
						lTreePenResMod.add(lSospTree);
				} else if (aEveModel.getEvento() != null
						&& "2141".equals(aEveModel.getEvento().getCodMotivo())) {
					lSospSql.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
					lSospMod = (SospensioneModel) lSospSql.getModelByKey();
					if (lSospMod != null) {
						lSospTree = new TreeModel(lSospMod);
						lTreePenResMod.add(lSospTree);
					}
				}

				lTreeFasMod.add(lTreePenResMod);

				// Misura Alternativa
				TreeModel lMATree = lStampa.getMATree(lPenResMod, aEveModel, lConn, lKeyFascicolo,
						aUtenteModel);
				if (lMATree != null)
					lTreePenResMod.add(lMATree);
				// Misura Alternativa precedente
				TreeModel lMAPrecTree = lStampa.setMAPrecedente(lTreePenResMod, lKeyFascicolo, aEveModel
						.getEvento().getTemIdTemplate(), lConn);
				if (lMAPrecTree != null)
					lTreePenResMod.add(lMAPrecTree);

			}

			// Referto Scarcerazione
			TreeModel lRefScarc = lStampa.getRefertoScarcerazioneTree(lPenResMod, aEveModel, lConn);
			if (lRefScarc != null)
				lTreeEveMod.add(lRefScarc);
			
			// mev39: recupero dati del differimento
			if(aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null
					&& aEveModel.getEvento().getCodMotivo().equals("1132")){
				
				aEveModel.getEvento().setEveIdEvento(aEveModel.getEvento().getIdEvento());
				
				TreeModel lMATree = lStampa.getMATree(lPenResMod, aEveModel, lConn, lKeyFascicolo,
						aUtenteModel);
				
				if (lMATree != null)
					lTreeEveMod.add(lMATree);
			}
			// intervento per mev 39 (casistica di restituzione ordine di consenga)
			if(aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null
					&& aEveModel.getEvento().getCodMotivo().equals("1149")){
				
				BigDecimal eventoCorrelato = aEveModel.getEvento().getEveIdEvento();
				if(eventoCorrelato!=null){
					
					IEvento iEvento = SICOLookupRemote.getEventoRemote();
					EventoNotificaModel enm = iEvento.ExRicercaEventoNotificaByKey(eventoCorrelato);
					if(enm!=null){
						CampoNotaModel lCampoNota =new CampoNotaModel();
						lCampoNota.setDescr(enm.getEvento().getCodMotivo());
						TreeModel mytree = new TreeModel((CampoNotaModel) lCampoNota);
						if (mytree != null)
							lTreeEveMod.add(mytree);
					}
				}				
				
			}
			//LTreeNota = new TreeModel((CampoNotaModel) lCampoNota);

			// Verbale solo per avvenuta espulsione --> 2141
			// e per rinuncia opposizione espulsione --> 2140
			if (aEveModel != null
					&& aEveModel.getEvento() != null
					&& (("2140".equals(aEveModel.getEvento().getCodMotivo())) || "2141".equals(aEveModel
							.getEvento().getCodMotivo()))) {
				String lTipoVerbale = "05"; // N.B. CodMotivo = 2141 --> TipoVerbale = 05
				if ("2140".equals(aEveModel.getEvento().getCodMotivo())) {
					lTipoVerbale = "07"; // N.B. CodMotivo = 2140 --> TipoVerbale = 07
				}

				TreeModel lVerbale = lStampaEvento.getVerbaleTree(aEveModel, lConn, lTipoVerbale);

				if (lVerbale != null)
					lTreeEveMod.add(lVerbale);
			}

			Iterator lItx = null;
			// Add Avvocati per Fascicolo
			if (lAvvocati != null) {
				lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
					TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
					lTreeFasMod.add(lTreeAvvMod);
					lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
				}
			}

			// -----------------------------------------------------------------
			if ("0226".equals(aEveModel.getEvento().getCodMotivo())) {
				// 10-09-2014 Siep_MA_Affi_DS - 'COMUNICAZIONE CONCESSIONE AFFIDAMENTO IN PROVA' (01 12 0226)
				// con una concessione di L.A. PRECEDENTE al provvedimento corrente
				// NON AUTORIZZATO - non deve andare in produzione

				lStampaEvento.appendLiberazioneAnticipataPerFascicolo(lConn, lTreeFasMod, lKeyFascicolo,
						aEveModel);
			} else {
				lStampaEvento.appendLiberazioneAnticipata(lConn, lTreeFasMod, lKeyFascicolo, aEveModel);
			}

			// DL 92/2014 Rimedi Risarcitori/ Reclamo Rimedi Risarcitori
			if ("5491".equals(aEveModel.getEvento().getCodMotivo())
					|| "9154".equals(aEveModel.getEvento().getCodMotivo()) // EC: gestione Ticket#20200508014 (aggiungo il codice mancante 9154) 
					|| "5492".equals(aEveModel.getEvento().getCodMotivo())
					|| "5493".equals(aEveModel.getEvento().getCodMotivo())
					|| "5494".equals(aEveModel.getEvento().getCodMotivo())
					|| "5495".equals(aEveModel.getEvento().getCodMotivo())
					// Reclamo Rimedi Risarcitori
					|| "9032".equals(aEveModel.getEvento().getCodMotivo())
					|| "9033".equals(aEveModel.getEvento().getCodMotivo())
					|| "9034".equals(aEveModel.getEvento().getCodMotivo())
					// inizio ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori 
					|| "9254".equals(aEveModel.getEvento().getCodMotivo())
					|| "9354".equals(aEveModel.getEvento().getCodMotivo())
					// fine ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori
				) {
				lStampaEvento.appendRimediRisarcitori(lConn, lKeyFascicolo, aEveModel, lTreeEveMod);
			}

			// Misure Sicurezza - Trasmissione atti x esecuzione
			if ("5416".equals(aEveModel.getEvento().getCodMotivo())
					|| "1130".equals(aEveModel.getEvento().getCodMotivo())) {
				// Devo recuperare in questo caso ulteriori deti da visualizzare nel
				// template ovvero:
				// - Decisione delle Sorv con relativa Misura da eseguire
				// - Luogo di espiazione deciso dal DAP
				//
				lStampaEvento.appendDatiTrasmissioneMS(lConn, lKeyFascicolo, lTreeEveMod);
			}

			// Misure Sicurezza - Sollecito trasmissione x competeza
			if ("5201".equals(aEveModel.getEvento().getCodMotivo())) {
				lSollecitoEsitoSqlDao = new SollecitoEsitoTrasmissioneSqlDAO(lConn);
				SollecitoEsitoTrasmissioneModel lSollecitoModel = null;
				lSollecitoEsitoSqlDao.ricercaSollecitoEsitoTrasmissioneByIdEvento(aEveModel.getEvento()
						.getIdEvento());
				lSollecitoModel = (SollecitoEsitoTrasmissioneModel) lSollecitoEsitoSqlDao.getModelByKey();
				if (lSollecitoModel != null) {
					TreeModel lTreeModelSollecito = new TreeModel(lSollecitoModel);
					lTreeEveMod.add(lTreeModelSollecito);
				}
			}

			// lStampaEvento.appendLiberazioneAnticipata(lConn, lTreeFasMod, lKeyFascicolo, aEveModel);

			/*
			 * //Liberazione Anticipata + Periodi Liberazione lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
			 * lLibDAO.ricercaLicenzaLibanticipataByIDFascicoloSIEP(lKeyFascicolo, null);
			 * //lLibDAO.ricercaLicenzaLibanticipataByEve(aEveModel.getEvento().getEveIdEvento()); lPerDao =
			 * new PeriodoLibanticipataSqlDAO(lConn); List lLibVect = new Vector(lLibDAO.getModels()); int
			 * lTotGiorniConcessiEvento = 0; int lTotaleGiorniDaConcedere = 0; int lTotaleGiorniConcessi = 0;
			 * for (int i = 0; i < lLibVect.size(); i++) { LicenzaLibAnticipataModel lLibAnt =
			 * (LicenzaLibAnticipataModel) lLibVect.get(i); // AGGIUNGE AL TREE MODEL TUTTI I TIPI DI LA
			 * LEGATE ALL'EVENTO if( lLibAnt != null && lLibAnt.getFlagConcesso() != null &&
			 * (lLibAnt.getEveIdEvento() != null &&
			 * lLibAnt.getEveIdEvento().equals(aEveModel.getEvento().getEveIdEvento())) ) { TreeModel
			 * lTreeLibAnt = new TreeModel(lLibAnt); lTreeFasMod.add(lTreeLibAnt); // Ricerca dei Periodi
			 * relativi alla licenza
			 * lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata()); List lPeriodi =
			 * new ArrayList(lPerDao.getModels()); Iterator lIterPeriodi = lPeriodi.iterator(); while
			 * (lIterPeriodi.hasNext()) { PeriodoLibAnticipataModel lPeriodoModel =
			 * (PeriodoLibAnticipataModel) lIterPeriodi.next(); lTreeLibAnt.add(new TreeModel(lPeriodoModel));
			 * } } // CONTA I GIORNI CONCESSI LEGATI ALL'EVENTO if( lLibAnt != null &&
			 * lLibAnt.getFlagConcesso() != null && lLibAnt.getFlagConcesso().equals("C") &&
			 * (lLibAnt.getEveIdEvento() != null &&
			 * lLibAnt.getEveIdEvento().equals(aEveModel.getEvento().getEveIdEvento())) ) { if (
			 * lLibAnt.getNumeroGiorni() != null && lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) !=
			 * 0) { if ( aEveModel.getEvento() != null && aEveModel.getEvento().getCodMotivo() != null &&
			 * (aEveModel.getEvento().getCodMotivo().equals("0922") ||
			 * aEveModel.getEvento().getCodMotivo().equals("0923") ) ) { lTotGiorniConcessiEvento +=
			 * lLibAnt.getNumeroGiorni().intValue(); } else if ( lLibAnt.getFlagElaborato() != null &&
			 * lLibAnt.getFlagElaborato().equals("E") ) { lTotGiorniConcessiEvento +=
			 * lLibAnt.getNumeroGiorni().intValue(); } } }// CONTA I GIORNI DA CONCERE IN ASSOLUTO LEGATI AL
			 * FASCICOLO else if( lLibAnt != null && lLibAnt.getFlagConcesso() != null &&
			 * lLibAnt.getFlagConcesso().equals("C") && (lLibAnt.getFlagElaborato() == null ||
			 * lLibAnt.getFlagElaborato().equals("N")) ) { if ( lLibAnt.getNumeroGiorni() != null &&
			 * lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) { lTotaleGiorniDaConcedere +=
			 * lLibAnt.getNumeroGiorni().intValue(); } } // CONTA I GIORNI CONCESSI IN ASSOLUTO LEGATI AL
			 * FASCICOLO else if( lLibAnt != null && lLibAnt.getFlagConcesso() != null &&
			 * lLibAnt.getFlagConcesso().equals("C") && (lLibAnt.getFlagElaborato() != null &&
			 * lLibAnt.getFlagElaborato().equals("S") ) ) { if ( lLibAnt.getNumeroGiorni() != null &&
			 * lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) { lTotaleGiorniConcessi +=
			 * lLibAnt.getNumeroGiorni().intValue(); } } } // Calcola il totale dei giorni di Licenza
			 * Anticipata concessi associati all'Evento TotalePeriodoModel lTotLibAnt = new
			 * TotalePeriodoModel(0, 0, lTotGiorniConcessiEvento); // Calcola il totale dei giorni di Licenza
			 * Anticipata concessi associati all'Evento TotalePeriodoLicenzaAnticipataModel lTotLibAntConc =
			 * new TotalePeriodoLicenzaAnticipataModel();
			 * lTotLibAntConc.setTotaleGiorniDaConcedere(lTotaleGiorniDaConcedere);
			 * lTotLibAntConc.setTotaleGiorniConcessi(lTotaleGiorniConcessi); TreeModel lTreeTotLibAnt = new
			 * TreeModel(lTotLibAnt); if(lTotLibAnt.isSignificativa()) { lTreeFasMod.add(lTreeTotLibAnt); }
			 * if( lTotLibAntConc.getTotaleGiorniDaConcedere() != 0 ||
			 * lTotLibAntConc.getTotaleGiorniConcessi() != 0) { lTreeFasMod.add( new TreeModel(lTotLibAntConc)
			 * ); } //-----------------------------------------------------------------
			 */

			// Aggiunta della Nuova Istanza al fascicolo 10/12/2010
			// if(aEveModel.getEvento().getCodTipoProvvedimento().equals("08") )
			// { // nessuna condizione
			lIstSqlDao = new NuovaIstanzaSqlDAO(lConn);
			// lIstSqlDao.ricercaNuovaIstanzaByEveIdEvento(aEveModel.getEvento().getIdEvento());
			lIstSqlDao.ricercaUltimaNuovaIstanzaByIdFascicolo(lKeyFascicolo);
			lIstSqlDao.start();
			if (lIstSqlDao.next()) {
				NuovaIstanzaModel lNuoIstMod = new NuovaIstanzaModel(
						(NuovaIstanzaModel) lIstSqlDao.getModel());
				if (lNuoIstMod != null) {
					TreeModel lTreeIstMod = new TreeModel(lNuoIstMod);
					lTreeFasMod.add(lTreeIstMod);
				}
			}
			// Fine Aggiunta della Nuova Istanza

			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));
			// lTreeRoot.add(this.getTreeSentenza(lFasModel.getSenIdSentenza(), lConn));

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiep: " + daoEx, daoEx);
			throw new F3BException("StampaController.prelevaDatiEventoSiep: " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiep: " + sqe, sqe);
			throw new F3BException("StampaController.prelevaDatiEventoSiep: " + sqe);
		} catch (F3BException f3bEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiep: " + f3bEx);
			throw f3bEx;
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiep: " + sqe, sqe);
			throw new F3BException("StampaController.prelevaDatiEventoSiep: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lFasDao);
			cleanup(lAvvDao);
			cleanup(lFungDAO);
			cleanup(lPenResDao);
			cleanup(lPenPrecDao);
			cleanup(lRefScarcDao);
			cleanup(lPosSqlDAO);
			cleanup(lLibDAO);
			cleanup(lPerDao);
			cleanup(lDecSqlDao);
			cleanup(lArcSqlDao);
			cleanup(lPenAccDAO); // 09/02/2006
			cleanup(lMotEveSqlDao);
			cleanup(lSospSql);
			cleanup(lSSSqlDAO);
			cleanup(lIstSqlDao);

			cleanup(lConn);
		}

		return lTreeRoot;
	}

	/*********************************************************************************/
	/*
	 * Preleva i dati dal DB per le stampe di Sospensioni
	 * 
	 * @param aEveModel
	 * 
	 * @return
	 * 
	 * @throws F3BException
	 */
	/*********************************************************************************/
	public TreeModel prelevaDatiSospensioni(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasDao = null;
		EventoSqlDAO lEventoSqlDAO = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		PenaPrecedenteSqlDAO lPenPrecDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		PenaResiduaSqlDAO lPenaResDao = null;
		SospensioneSqlDAO lSospSql = null;
		SospensioneModel lSosp = null;

		BigDecimal lKeyFascicolo = aEveModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lConn = getDBConnection();

			// Fascicolo
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

			// MEV a7-rr-311 riportare anche il vecchio codice RES
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr(): " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			// Pena Residua Precedente
			lPenPrecDao = new PenaPrecedenteSqlDAO(lConn);
			lPenPrecDao.ricercaPenaPrecedenteSospensioniByFascicolo(lKeyFascicolo);
			Vector lPrec = new Vector(lPenPrecDao.getModels());

			PenaPrecedenteModel lPenPrecedente = null;

			if (lPrec != null && lPrec.size() > 0) {
				lPenPrecedente = new PenaPrecedenteModel();
				lPenPrecedente = (PenaPrecedenteModel) lPrec.firstElement();
				// Stringa Arresto - Reclusione
				if (lPenPrecedente != null) {
					lPenPrecedente.calcolaStringaReclusione();
					lPenPrecedente.calcolaStringaArresto();
					lPenPrecedente.calcolaStringaIsolamento();
					if (lPenPrecedente.getDataFine() != null
							&& lPenPrecedente.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
							// GDV 23/10/2006 a6-rr-328
							&& lPenPrecedente.getFlagErgastolo() != null
							&& !lPenPrecedente.getFlagErgastolo().equals("S")
							&& !lPenPrecedente.getFlagErgastolo().equals("D"))
						lPenPrecedente.setImmediataScarcerazione("S");
					else {
						if (lPenPrecedente != null)
							lPenPrecedente.setImmediataScarcerazione("N");
					}
				}
				lPenPrecDao.stop();
			}

			// Pena Residua
			lPenaResDao = new PenaResiduaSqlDAO(lConn);
			lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lKeyFascicolo);
			PenaResiduaModel lPenresMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
			if (lPenresMod != null) {
				lPenresMod.calcolaStringaReclusione();
				lPenresMod.calcolaStringaArresto();
				lPenresMod.calcolaStringaIsolamento();
				if (lPenresMod.getDataFine() != null
						&& lPenresMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
						// GDV 23/10/2006 a6-rr-328
						&& lPenresMod.getFlagErgastolo() != null
						&& !lPenresMod.getFlagErgastolo().equals("S")
						&& !lPenresMod.getFlagErgastolo().equals("D")) {
					lPenresMod.setImmediataScarcerazione("S");
				} else {
					if (lPenresMod != null)
						lPenresMod.setImmediataScarcerazione("N");
				}
			}
			lPenaResDao.stop();

			// Sospensione
			lSospSql = new SospensioneSqlDAO(lConn);
			if (lPenresMod != null) {
				lSospSql.ricercaSospensioneByIdPenaResidua(lPenresMod.getIdPenaResidua());
				lSosp = (SospensioneModel) lSospSql.getModelByKey();
			}

			TreeModel lTreeFasMod = new TreeModel(lFasModel);

			// Decreto Ordinanza Siep

			lDecSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecSqlDao.ricercaDecretoOrdinanzaSiepByFascicoloSiepDataInsDesc(lKeyFascicolo);
			DecretoOrdinanzaSiepModel lDecMod = (DecretoOrdinanzaSiepModel) lDecSqlDao.getModelByKey();
			if (lDecMod != null) {
				lDecMod.calcolaStringaRinvio();
				TreeModel lTreeDecOrd = new TreeModel(lDecMod);
				lTreeFasMod.add(lTreeDecOrd);
			}

			this.appendTableToFascicoloSiep(lConn, lKeyFascicolo, lTreeFasMod, lFasModel.getFlagAltraCausa());

			// Avvocati
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
			Vector lAvvocati = new Vector(lAvvDao.getModels());

			// Aggiunge nodi di evento e notifica
			lTreeRoot = new TreeModel(createRoot(aEveModel, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			aEveModel.getEvento().setEventoCorrente("S");
			aEveModel.setEvento(this.mEventoUtils.setEventoDiStampa(aEveModel.getEvento(), lConn));

			TreeModel lTreeEveMod = new TreeModel(aEveModel.getEvento());

			this.mEventoUtils.appendNotifiche(lTreeEveMod, aEveModel, lAvvocati, aUtenteModel);

			lTreeRoot.add(lTreeEveMod);

			// Magistrato
			if (aEveModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEveModel.getMagistrato()));

			// this.mEventoUtils.appendStatoEsecuzione("MEDIUM", lTreeRoot, lConn, lKeyFascicolo);
			if (SIESSwitch.isReworkStatoEsecuzioneOn()) {
				// Se sono nel rework dello stato esecuzione chiamo il nuovo
				// controller che gestisce lo stato esec.

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Prima di appendere lo Stato di Esecuzione");
				StatoEsecuzioneController lStat = new StatoEsecuzioneController();
				lStat.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Ho appeso lo Stato di Esecuzione");

			} else
				this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);

			// Nodi livello 1 Fascicolo - Soggetto - Sentenza
			// [EC] - 20171030 : intervento per risoluzione anomalia segnalata dalla procura di bologna in relazione alla stampa dell'OE a seguito di un 
			// provvedimento di INTERRUZIONE PER EVASIONE (email Maffucci del 25102017).
			// Intervento effettuato : il metodogetTreeSoggetto non prendeva in input l'oggetto lFasModel valorizzato, ma veniva passato sempre a NULL! 
			// Per la distinzione sulla stampa tra  minorenni e maggiorenni serve che l'oggetto lFasModel sia valorizzato!			
			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn, lFasModel);

			if (lPenPrecedente != null) {
				TreeModel lTreePenPrec = new TreeModel(lPenPrecedente);
				lTreeFasMod.add(lTreePenPrec);
			}
			if (lPenresMod != null) // Pena Residua
			{
				TreeModel lTreelPenresMod = new TreeModel(lPenresMod);
				if (lSosp != null) {
					TreeModel lTreeSosp = new TreeModel(lSosp);
					lTreelPenresMod.add(lTreeSosp);

				}
				lTreeFasMod.add(lTreelPenresMod);
			}

			Iterator lItx = null;

			// Add Avvocati per Fascicolo
			if (lAvvocati != null) {
				lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
					TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
					lTreeFasMod.add(lTreeAvvMod);
					lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
				}
			}

			lTreeRoot.add(lTreeFasMod);

			// cumulo
			if (lFasModel.getFlagCumulante() != null && lFasModel.getFlagCumulante().equals("S")) {
				TreeModel lPenCumModTree = getPenaCumulo(lKeyFascicolo, lConn);
				lTreeRoot.add(lPenCumModTree);
			}

			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));
			// lTreeRoot.add(this.getTreeSentenza(lFasModel.getSenIdSentenza(), lConn));

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiSospensioni: " + daoEx);
		}

		catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("StampaController.prelevaDatiSospensioni: " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lAvvDao);
			cleanup(lEventoSqlDAO);
			cleanup(lSanDao);
			cleanup(lPenPrecDao);
			cleanup(lDecSqlDao);
			cleanup(lPenaResDao);
			cleanup(lSospSql);
			cleanup(lConn);
		}

		return lTreeRoot;
	}

	/**
	 * Preleva i dati dal DB per le stampe di IStruttoria e Nuova Istanza
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiIstruttoria(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		ReatoSqlDAO lReaDao = null;
		NotificaSqlDAO lNotDao = null;
		NotiziaReatoSqlDAO lNotRDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		// IstanzaSqlDAO lIstSqlDao = null;
		NuovaIstanzaSqlDAO lIstSqlDao = null;
		TreeModel lTreeEventoPrec = null;
		SanzioneSostResiduaSqlDAO lSSSqlDAO = null;
		PenaComplessivaSqlDAO lPenaComplDAO = null;
		SanzioneSostitutivaSqlDAO lSSostsqlDAO = null;
		CircostanzaSqlDAO lCircDao = null;

		PenaPrecedenteSqlDAO lPenPrecDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;

		BigDecimal lKeyFascicolo = aEveModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lTreeRoot = new TreeModel(createRoot(aEveModel, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			// evento OE Per istanza

			if (aEveModel.getEvento().getCodTipoProvvedimento().equals("08")) {
				lTreeEventoPrec = this.getTreeEventoPrecedenteOEPerIstanza(aEveModel.getEvento()
						.getFasSieIdFascicoloSiep());
				if (lTreeEventoPrec != null)
					lTreeRoot.add(lTreeEventoPrec);
			}

			lConn = getDBConnection();

			// Fascicolo
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("prima dell'if del codice RES = "
					+ lFasModel.getCodOperatoreInserimento().startsWith("RES"));
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("SONo entrata nell'if del codice RES");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr(): " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			// AMBROSINO COLL
			// Posizione Giuridica
			lPosSqlDAO = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDAO.ricercaPosGiuCorrenteByIdFascicolo(lKeyFascicolo);
			PosizioneGiuridicaModel lPos = (PosizioneGiuridicaModel) lPosSqlDAO.getModelByKey();
			// END- AMBROSONO COLL

			// Vector lNotiziaReati = null;
			Vector lReati = null;
			NotiziaReatoModel mNotR = null;

			// ricerche specifiche per richiesta accertamenti anagrafici
			if (aEveModel.getEvento().getCodMotivo().equals("0557")
					|| aEveModel.getEvento().getCodMotivo().equals("0578")) {
				// notizie
				lNotRDao = new NotiziaReatoSqlDAO(lConn);
				// lNotRDao.ricercaNotiziaReatoByIdFascicolo(lKeyFascicolo);
				// lNotiziaReati = new Vector(lNotRDao.getModels());
				lNotRDao.ricercaNotiziaReatoByIdFascicoloOrder(lKeyFascicolo);
				mNotR = (NotiziaReatoModel) lNotRDao.getModelByKey();
				// reati (FABIO - MAC A7-RR-307)
				IReato lReaCtr = SIEPLookupRemote.getReatoRemote();
				lReati = lReaCtr.ExRicercaReatoCircostanzaByFascicolo(lKeyFascicolo);
				// lReaDao = new ReatoSqlDAO(lConn);
				// lReaDao.ricercaReatiByFascicoloSiep(lKeyFascicolo);

				// lReati = new Vector(lReaDao.getModels());
			}

			TreeModel lTreeEveMod = new TreeModel(aEveModel.getEvento());

			// 02/03/2011 Aggiunto il Magistrato.
			if (aEveModel.getMagistrato() != null
					&& aEveModel.getMagistrato().getCodMagistrato().compareTo("-") != 0)
				lTreeEveMod.add(new TreeModel(aEveModel.getMagistrato()));

			int count = 0;
			while (count < aEveModel.getNotifiche().length) { // Notifiche con Autorita' Esterna
				NotificaModel lNot = new NotificaModel(aEveModel.getNotifiche()[count]);
				TreeModel lTreeNot = new TreeModel(lNot);
				lTreeEveMod.add(lTreeNot);

				if (aEveModel.getNotifiche()[count].getAutoritaEsterna() != null) {
					lTreeNot.add(new TreeModel(aEveModel.getNotifiche()[count].getAutoritaEsterna()));
				}
				if (aEveModel.getNotifiche()[count].getUfficio() != null) {
					lTreeNot.add(new TreeModel(aEveModel.getNotifiche()[count].getUfficio()));
				}

				// paolo cherubini 11/01/2011 per far uscire l'istituto
				if ((aEveModel.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null)
						&& (aEveModel.getNotifiche()[count].getIstitutoDetenzione() != null)) {
					AutoritaEsternaModel lAutFinto = new AutoritaEsternaModel();

					lAutFinto.setDescrTipoAutorita(aEveModel.getNotifiche()[count].getIstitutoDetenzione()
							.getDescrTipoIstituto());
					// lAutFinto.setDescrSede(aEveModel.getNotifiche()[count].getIstitutoDetenzione().getDescrComune()
					// + ", " + aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo());
					/*
					 * DEREMMARE PER ELIMINARE IL PROBLEMA DEL NULL DOVE MANCA L'INDIRIZZO DELL'ISTITUTO DI
					 * DETENZIONE -----> NON TESTATA
					 */
					if (aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo() != null
							&& !aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo()
									.equalsIgnoreCase("null")) {
						lAutFinto.setDescrSede(aEveModel.getNotifiche()[count].getIstitutoDetenzione()
								.getDescrComune()
								+ ", "
								+ aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo());
					} else {
						lAutFinto.setDescrSede(aEveModel.getNotifiche()[count].getIstitutoDetenzione()
								.getDescrComune());
					}
					lTreeNot.add(new TreeModel(lAutFinto));
				}

				count++;
			}

			// 18/06/2010 Commentata Vecchia Istanza
			// lIstSqlDao = new IstanzaSqlDAO(lConn);
			// lIstSqlDao.ricercaByKeyEvento(aEveModel.getEvento().getIdEvento());
			// IstanzaModel lIstModel = (IstanzaModel) lIstSqlDao.getModelByKey();
			// TreeModel lTreeIstMod = new TreeModel(lIstModel);
			// lTreeEveMod.add(lTreeIstMod);

			// 18/06/2010 Nuova Istanza
			if (aEveModel.getEvento().getCodTipoProvvedimento().equals("08")) {
				lIstSqlDao = new NuovaIstanzaSqlDAO(lConn);
				// paolo cherubini 08/02/2011
				// lIstSqlDao.ricercaNuovaIstanzaByEveIdEvento(aEveModel.getEvento().getIdEvento());
				// lIstSqlDao.ricercaNuovaIstanzaByIdFascicolo(aEveModel.getEvento().getFasSieIdFascicoloSiep());
				// NuovaIstanzaModel lNuoIstMod = new NuovaIstanzaModel((NuovaIstanzaModel)
				// lIstSqlDao.getModelByKey());
				if (aEveModel.getEvento().getCodMotivo() != null
						&& aEveModel.getEvento().getCodMotivo().equals("0993")) {
					lIstSqlDao.ricercaNuovaIstanzaByEveIdEvento(aEveModel.getEvento().getIdEvento());
				} else if (aEveModel.getEvento().getCodMotivo() != null
						&& (aEveModel.getEvento().getCodMotivo().equals("1001") || aEveModel.getEvento()
								.getCodMotivo().equals("1002"))) {
					lIstSqlDao.ricercaNuovaIstanzaByEveIdEvento(aEveModel.getEvento().getEveIdEvento());
				} else {
					lIstSqlDao.ricercaNuovaIstanzaByIdFascicolo(aEveModel.getEvento()
							.getFasSieIdFascicoloSiep());
				}
				NuovaIstanzaModel lNuoIstMod = new NuovaIstanzaModel(
						(NuovaIstanzaModel) lIstSqlDao.getModelByKey());

				// 07/03/2011 Lettura Avvocato Presentante.
				if (lNuoIstMod.getAvvIdAvvocatoPresentante() != null) {
					AvvocatoSqlDAO lAvvDao = new AvvocatoSqlDAO(lConn);
					lAvvDao.ricercaAvvocatobyKey(lNuoIstMod.getAvvIdAvvocatoPresentante());

					AvvocatoModel lAvvocato = (AvvocatoModel) lAvvDao.getModelByKey();
					if (lAvvocato != null) {
						lNuoIstMod.setDescrAvvocatoPresentante(lAvvocato.getCognome() + " "
								+ lAvvocato.getNome());
						lNuoIstMod.setAvvocatoPresentante(lAvvocato);
					}
				}
				TreeModel lTreeIstMod = new TreeModel(lNuoIstMod);

				// 07/03/2011 Lettura Avvocato.
				if (lNuoIstMod.getAvvIdAvvocato() != null) {
					AvvocatoSqlDAO lAvvDao = new AvvocatoSqlDAO(lConn);
					lAvvDao.ricercaAvvocatobyKey(lNuoIstMod.getAvvIdAvvocato());

					AvvocatoModel lAvvocato = (AvvocatoModel) lAvvDao.getModelByKey();
					if (lAvvocato != null) {
						TreeModel lTreeAvvMod = new TreeModel(lAvvocato);
						lTreeIstMod.add(lTreeAvvMod);
						lNuoIstMod.setAvvocato(lAvvocato);
					}
				}

				lTreeEveMod.add(lTreeIstMod);
			}

			// Notizie di reato
			TreeModel lTreeFasMod = new TreeModel(lFasModel);
			if (mNotR != null) {
				lTreeFasMod.add(new TreeModel(mNotR));
			}
			/*
			 * if(lNotiziaReati!=null){ Iterator lNotR = lNotiziaReati.iterator(); while (lNotR.hasNext()) {
			 * NotiziaReatoModel mNotR = (NotiziaReatoModel) lNotR.next(); lTreeFasMod.add(new
			 * TreeModel(mNotR)); } }
			 */
			// Reati
			// (FABIO - MAC A7-RR-308)
			Iterator lItx = null;
			if (lReati != null) {
				Vector lReatiPerCont = new Vector();
				lItx = lReati.iterator();
				while (lItx.hasNext()) {
					// Add gerarchia dei Reati - Circostanza
					ReatoCircostanzaModel lReatoModel = new ReatoCircostanzaModel(
							(ReatoCircostanzaModel) lItx.next());
					TreeModel lTreeReatoMod = new TreeModel(lReatoModel.getReato());

					// Creo Vettore dei Reati per le Continuazioni
					ReatoModel lReato = lReatoModel.getReato();
					if (lReato != null) {
						lReato.calcolaPenaReato();
						lReato.calcolaStringaPenaPecuniaria();// <==== modifica template import
					}

					lReatiPerCont.add(lReato);

					int countR = 0; // Circostanze
					while (countR < lReatoModel.getCircostanze().length) {
						TreeModel lTreeCirc = new TreeModel(lReatoModel.getCircostanze()[countR]);
						lTreeReatoMod.add(lTreeCirc);
						countR++;
					}
					lTreeFasMod.add(lTreeReatoMod);
				}
			}

			// Circostanze - fabio - 16-09-2008
			lCircDao = new CircostanzaSqlDAO(lConn);
			// lCircDao.ricercaCircostanzeByIdFascicolo(lFasModel.getFasSieIdFascicoloSiep());
			CircostanzaModel lCirMod = new CircostanzaModel();
			lCirMod.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
			// ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();

			Vector lVectC = null;
			ICircostanza lCtrlC = SIEPLookupRemote.getCircostanzaRemote();
			try {
				lVectC = lCtrlC.ExRicercaCircostanza(lCirMod);
			} catch (Exception e) {
			}

			if (lVectC != null && lVectC.size() > 0) {
				// for (int y = 0; y < lVectC.size(); y++) {
				// CircostanzaModel CiReatoS = (CircostanzaModel) lVectC.get(y);
				// }
				Vector lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lVectC);

				for (int i = 0; i < lNewVectCirc.size(); i++) {
					CircostanzaModel CiReato = (CircostanzaModel) lNewVectCirc.get(i);
					TreeModel lTreeCirc = new TreeModel(CiReato);
					lTreeFasMod.add(lTreeCirc);
				}
			}

			// END Circostanze - fabio - 16-09-2008

			// Annotazioni
			this.prelevaDatiCampoNota(lTreeEveMod, aEveModel.getEvento().getIdEvento(), lConn);

			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn,
					lFasModel);

			// PENA COMPLESSIVA - fabio - 10-06-2008
			lPenaComplDAO = new PenaComplessivaSqlDAO(lConn);
			lPenaComplDAO.ricercaPenaComplessivaByIdFascicolo(lKeyFascicolo);

			PenaComplessivaModel lPenaComplMod = (PenaComplessivaModel) lPenaComplDAO.getModelByKey();

			if (lPenaComplMod != null) {
				lPenaComplMod.calcolaStringaArresto();
				lPenaComplMod.calcolaStringaIsolamento();
				lPenaComplMod.calcolaStringaReclusione();
				TreeModel lTreePenaComplMod = new TreeModel(lPenaComplMod);
				lTreeFasMod.add(lTreePenaComplMod);
			}
			// END PENA COMPLESSIVA - fabio - 10-06-2008

			// AMBROSINO COLL

			// Ricerca Pena Residua e Pena Precedente
			// Prendo entrambe utilizzando il PenaPrecedenteSqlDAO
			lPenPrecDao = new PenaPrecedenteSqlDAO(lConn);
			lPenPrecDao.ricercaPenaPrecedenteByFascicolo(lKeyFascicolo);
			Vector lPrec = new Vector(lPenPrecDao.getModels());

			PenaResiduaModel lPenResMod = null;

			if (lPrec != null && lPrec.size() > 0) {
				PenaPrecedenteModel lPenPrecedente = new PenaPrecedenteModel();
				// Prendo il primo elemento del vettore... La pena residua!
				lPenPrecedente = (PenaPrecedenteModel) lPrec.firstElement();
				// Presa la Pena Residua
				lPenResMod = lPenPrecedente.getPenaResiduaModel();
				// Stringa Arresto - Reclusione
				if (lPenResMod != null) {
					lPenResMod.setPenaResiduaPerStampa(lPos);
				}
				lPenPrecDao.stop();
			}

			// calcola stinghe residue SE e' SIMEONE
			if (aEveModel.getEvento() != null
					&& aEveModel.getEvento().getCodMotivo() != null
					&& (aEveModel.getEvento().getCodMotivo().equals("0061")
							|| aEveModel.getEvento().getCodMotivo().equals("0062") || aEveModel.getEvento()
							.getCodMotivo().equals("0063"))) {
				if (!lPos.isLibero()) {
					PenaResiduaModel lPenaModel = null;

					if (lPenResMod.getDataFine() != null || lPenResMod.getDataFinePresunta() != null) {
						if (lPenResMod.getDataFine() == null) {
							lPenResMod.setDataFine(lPenResMod.getDataFinePresunta());
						}

						lPenaModel = PenaResiduaUtil.calcolaPenaNuovaDataInizio(DateUtils.getSysDate(),
								lPenResMod, true);
						lPenaModel.calcolaStringaReclusione();
						lPenaModel.calcolaStringaArresto();
						lPenaModel.calcolaStringaIsolamento();

						lPenResMod.setStringaIsolamentoDiurno(lPenaModel.getStringaIsolamentoDiurno());
						lPenResMod.setStringaArrestoResidua(lPenaModel.getStringaArrestoResidua());
						lPenResMod.setStringaReclusioneResidua(lPenaModel.getStringaReclusioneResidua());
						if (lPenaModel.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)
							lPenResMod.setImportoAmmendaResidua(lPenaModel.getImportoAmmenda());
						if (lPenaModel.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
							lPenResMod.setImportoMultaResidua(lPenaModel.getImportoMulta());
					}
				} else {
					if (lPenResMod.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0)
						lPenResMod.setImportoAmmendaResidua(lPenResMod.getImportoAmmenda());
					if (lPenResMod.getImportoMulta().compareTo(new BigDecimal(0)) != 0)
						lPenResMod.setImportoMultaResidua(lPenResMod.getImportoMulta());
				}
			}

			if (lPenResMod != null && lPenResMod.getDataFine() != null
					&& lPenResMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
					// GDV 23/10/2006 a6-rr-328
					&& lPenResMod.getFlagErgastolo() != null && !lPenResMod.getFlagErgastolo().equals("S")
					&& !lPenResMod.getFlagErgastolo().equals("D")) {
				lPenResMod.setImmediataScarcerazione("S");
			} else {
				if (lPenResMod != null)
					lPenResMod.setImmediataScarcerazione("N");
			}

			PenaPrecedenteModel lPrecModel = null;
			// Prese tutte le pene residue ordinate per Data, prendo la penultima...
			// Per avere la Pena Rasidua Precedente
			if (lPrec != null) {
				Iterator lPenPrecItx = lPrec.iterator();
				while (lPenPrecItx.hasNext()) {
					lPrecModel = (PenaPrecedenteModel) lPenPrecItx.next();
					if (lPrecModel.getFlagValidato().equals("S")) {
						// Stringa Arresto - Reclusione
						if (lPrecModel != null) {
							lPrecModel.setPenaPrecedentePerStampa(lPos);
						}
						break; // Trovata la prima pena precedente validata esco dal while
					}
				}
			}

			// TreeModel lTreeFasMod = new TreeModel(lFasModel);

			TreeModel lTreePenResMod = new TreeModel(lPenResMod);
			lTreeFasMod.add(lTreePenResMod);

			// END-AMBROSINO COLL

			// SANZIONE SOSTITUTIVA - fabio - 10-06-2008
			lSSostsqlDAO = new SanzioneSostitutivaSqlDAO(lConn);
			SanzioneSostitutivaModel lSanzSostMod = null;

			if (lPenaComplMod != null && lPenaComplMod.getIdPenaComplessiva() != null) {
				lSSostsqlDAO.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenaComplMod
						.getIdPenaComplessiva());
				lSanzSostMod = (SanzioneSostitutivaModel) lSSostsqlDAO.getModelByKey();
			}

			if (lSanzSostMod != null) {
				lSanzSostMod.calcolaStringaSanzione();
				TreeModel lTreeSanzSost = new TreeModel(lSanzSostMod);
				lTreeFasMod.add(lTreeSanzSost);
			}
			// END SANZIONE SOSTITUTIVA - fabio - 10-06-2008

			// sanzione sostitutiva residua -- dario -- 26-03-2008
			lSSSqlDAO = new SanzioneSostResiduaSqlDAO(lConn);
			lSSSqlDAO.ricercaUltimaSanzioneSostResiduaByIdFasc(lKeyFascicolo, "S");
			SanzioneSostResiduaModel lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO.getModelByKey();

			if (lSSResiduaModel == null) {
				lSSSqlDAO.ricercaUltimaSanzioneSostResiduaByIdFasc(lKeyFascicolo, "N");
				lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO.getModelByKey();
			}

			if (lSSResiduaModel != null) {
				TreeModel lTreeSSResiduaMod = new TreeModel(lSSResiduaModel);
				lTreeFasMod.add(lTreeSSResiduaMod);
			}

			// Aggiungo la posizione Giuridica
			this.addPosizioneGiuridica(lConn, lKeyFascicolo, lTreeFasMod, lFasModel.getFlagAltraCausa());

			lTreeRoot.add(lTreeEveMod);
			lTreeRoot.add(lTreeFasMod);

			// Cumulo
			if (lFasModel.getFlagCumulante() != null && lFasModel.getFlagCumulante().equals("S")) {
				TreeModel lPenCumModTree = getPenaCumulo(lKeyFascicolo, lConn);
				lTreeRoot.add(lPenCumModTree);
			}

			lTreeRoot.add(lTreeSogMod);

			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiIstruttoria: Non posso leggere : " + daoEx);
		} catch (F3BException f3bEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiep: " + f3bEx);
			throw f3bEx;
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiEventoSiep: " + sqe, sqe);
			throw new F3BException("StampaController.prelevaDatiEventoSiep: Eccezione Generica: " + sqe);
		}

		finally {
			cleanup(lReaDao);
			cleanup(lNotRDao);
			cleanup(lFasDao);

			cleanup(lNotDao);
			cleanup(lIstSqlDao);
			cleanup(lSSSqlDAO);
			cleanup(lPenaComplDAO);
			cleanup(lCircDao);
			cleanup(lPenPrecDao); // 28/07/2011
			cleanup(lPosSqlDAO); // 28/07/2011

			cleanup(lConn);
		}
		return lTreeRoot;
	}

	/**
	 * Crea una Root per l'Avvocato
	 * 
	 * @param aAvvSieModel
	 * @param aUtenteConnesso
	 * @param aSedeUtenteConnesso
	 * @return
	 */
	private XModel createRootAvvocato(AvvocatoSiepModel aAvvSieModel, UtenteModel aUtenteModel) {
		XModel lStampa = new XModel();

		String descrTipoUff = "";

		if (aUtenteModel != null && aUtenteModel.getUfficioUtente() != null) {
			descrTipoUff = aUtenteModel.getUfficioUtente().getDescrTipoUfficio().toUpperCase();

			UfficioModel lUffMod = aUtenteModel.getUfficioUtente();

			lStampa.setUfficio(aUtenteModel.getUfficioUtente().getDescrComune().toUpperCase());
			lStampa.setTipoUfficio(descrTipoUff);
			lStampa.setDataElaborazione(DateUtils.getSysDate());
			lStampa.setCap(lUffMod.getCap());
			lStampa.setFax(lUffMod.getFax());
			lStampa.setIndirizzo(lUffMod.getIndirizzo());
			lStampa.setTelefono(lUffMod.getTelefono());
		}
		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
		}
		// modifica per templates
		if (descrTipoUff.indexOf("GENERALE") > 0) {
			lStampa.setFirmatario("Il Procuratore Generale");
		} else {
			lStampa.setFirmatario("Il Pubblico Ministero");
		}

		return lStampa;
	}

	/**
	 * Preleva i dati dal DB per le stampe di IStruttoria
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiAvvocato(AvvocatoFascicoloSiepModel aAvvocatoSiep, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = null;

		AvvocatoFascicoloSiepSqlDAO lAvvSiepSqlDAO = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		MagistratoCompetenteMagistratoSqlDAO lMagSql = null;
		IstitutoDetenzioneSqlDAO lIstSql = null;
		MagistratoModel lMag = null;
		ResidenzaSqlDAO lResDao = null;
		Connection lConn = null;

		try {
			lConn = getDBConnection();

			// Avvocato
			lAvvSiepSqlDAO = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvSiepSqlDAO.ricercaAvvocatoFascicoloSiepByIdAvvocatoIdAvvFascicolo(
					aAvvocatoSiep.getAvvIdAvvocato(), aAvvocatoSiep.getIdAvvocatoFascicoloSiep());
			AvvocatoSiepModel lAvvSiep = (AvvocatoSiepModel) lAvvSiepSqlDAO.getModelByKey();
			AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel(
					lAvvSiep.getAvvocatoFascicoloSiepModel());
			AvvocatoModel lAvv = new AvvocatoModel(lAvvSiep.getAvvocato());

			// Fascicolo
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lAvvFascMod.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr(): " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			// Soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lFasModel.getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogDao.getModelByKey();

			// Magistrato competente
			lMagSql = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			lMagSql.ricercaMagistratoCompetenteByFascicolo(lAvvFascMod.getFasSieIdFascicoloSiep());
			MagistratoCompetenteMagistratoModel lMagModel = (MagistratoCompetenteMagistratoModel) lMagSql
					.getModelByKey();

			// istituto detenzione
			lIstSql = new IstitutoDetenzioneSqlDAO(lConn);
			lIstSql.ricercaIstitutoDetenzioneByKey(lAvvFascMod.getIstDetIdIstitutoDetenzione());
			IstitutoDetenzioneModel lIstMod = (IstitutoDetenzioneModel) lIstSql.getModelByKey();
			IstitutoDetenzioneModel lIstModNuovo = new IstitutoDetenzioneModel();

			if (lIstMod != null) {
				lIstModNuovo.setDescrTipoIstituto(lIstMod.getDescrTipoIstituto() + " di "
						+ lIstMod.getDescrComune() + " " + lIstMod.getIndirizzo());
				// lIstModNuovo.setDescrComune(lIstMod.getDescrComune());
			}
			if (lMagModel != null) {
				lMag = new MagistratoModel(lMagModel.getMagistrato());
			}

			lTreeRoot = new TreeModel(createRootAvvocato(lAvvSiep, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			// cumulo
			if (lFasModel.getFlagCumulante() != null && lFasModel.getFlagCumulante().equals("S")) {
				TreeModel lPenCumModTree = getPenaCumulo(lAvvFascMod.getFasSieIdFascicoloSiep(), lConn);
				lTreeRoot.add(lPenCumModTree);
			}

			TreeModel lTreeFasc = new TreeModel(lFasModel);
			TreeModel lTreeAvvFasc = new TreeModel(lAvvFascMod);
			if (lIstMod != null) {
				TreeModel lTreeIst = new TreeModel(lIstModNuovo);
				lTreeAvvFasc.add(lTreeIst);

			}
			TreeModel lTreeAvv = new TreeModel(lAvv);
			TreeModel lTreeSogMod = new TreeModel(lSogModel);

			lTreeAvv.add(lTreeAvvFasc);
			lTreeFasc.add(lTreeAvv);
			if (lMag != null) {
				TreeModel lTreeMag = new TreeModel(lMag);
				lTreeFasc.add(lTreeMag);
			}

			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));

			lTreeRoot.add(lTreeFasc);
			lTreeRoot.add(lTreeSogMod);

			// Residenza

			lResDao = new ResidenzaSqlDAO(lConn);
			lResDao.ricercaResidenzaByFascicoloXStampa(lFasModel.getIdFascicoloSiep());
			Vector lResidenze = new Vector(lResDao.getModels());

			Iterator lItx = null;

			// Aggiungere Residenze e Domicili
			if (lResidenze != null) {
				lItx = lResidenze.iterator();
				while (lItx.hasNext()) {
					ResidenzaModel lResModel = new ResidenzaModel((ResidenzaModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lResModel));
				}
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiAvvocato: Non posso leggere : " + daoEx);
		}

		finally {
			cleanup(lAvvSiepSqlDAO);
			cleanup(lMagSql);
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lResDao);

			cleanup(lIstSql);

			cleanup(lConn);
		}
		return lTreeRoot;
	}

	/**
	 * La funzione estrae dalla tabella CAMPO_NOTA i record collegati all'Evento e li aggiunge al TreeModel
	 * passato.
	 */
	private TreeModel prelevaDatiCampoNota(TreeModel aTree, BigDecimal aEventoKey, Connection aConn)
			throws Exception {
		// CampoNote
		CampoNotaSqlDAO lCampoNotaSqlDao = null;
		Vector lCampiNote = null;
		CampoNotaModel lCampoNota = null;
		TreeModel LTreeNota = null;

		try {
			// Ricerca Campi note collegate all'evento
			lCampoNotaSqlDao = new CampoNotaSqlDAO(aConn);
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aEventoKey);
			lCampiNote = new Vector(lCampoNotaSqlDao.getModels());
			Iterator lItx = lCampiNote.iterator();
			while (lItx.hasNext()) {
				lCampoNota = (CampoNotaModel) lItx.next();
				LTreeNota = new TreeModel((CampoNotaModel) lCampoNota);
				aTree.add(LTreeNota);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lCampoNotaSqlDao);
		}
		return aTree;
	}

	/**
	 * La funzione estrae dalla tabella COMPETENZA i record collegati all'Evento e li aggiunge al TreeModel
	 * passato.
	 */
	private TreeModel prelevaDatiCompetenza(TreeModel aTree, BigDecimal aEventoKey, Connection aConn)
			throws Exception {
		CompetenzaSqlDAO lCompSqlDAO = null;
		Vector lCompetenze = null;
		CompetenzaModel lCompetenza = null;
		TreeModel lTreeCompetenza = null;
    
    UfficioSqlDAO lUffSqlDao = null;
    
		try {
			// Ricerca Campi note collegate all'evento
			lCompSqlDAO = new CompetenzaSqlDAO(aConn);
			lCompSqlDAO.ricercaCompetenzaByEveIdEvento(aEventoKey);
			lCompetenze = new Vector(lCompSqlDAO.getModels());
			Iterator lItx = lCompetenze.iterator();
			while (lItx.hasNext()) {
				lCompetenza = (CompetenzaModel) lItx.next();
				lTreeCompetenza = new TreeModel((CompetenzaModel) lCompetenza);
				aTree.add(lTreeCompetenza);
        
        if (   "S".equals(lCompetenza.getFlagAccorpato())
            && lCompetenza.getChiaveUfficioOrigine()!=null
           )
        {
          lUffSqlDao = new UfficioSqlDAO (aConn);
         
          lUffSqlDao.selUfficioByCod (lCompetenza.getChiaveUfficioOrigine());
          UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
         
          lTreeCompetenza.add (new TreeModel(lUffMod));
        }
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lCompSqlDAO);
      cleanup(lUffSqlDao);
		}
		return aTree;
	}

	/**
	 * Recupera i dati dell'annotazione di rideterminazione Pena Altro che viene puntata dall'evento corrente
	 * e la carica nel tree model. Servono per la stampa degli eventi di OE, OECS, OS COMUNICAZIONE, per far
	 * riferimento al provvedimento di computo.
	 * 
	 * @param aTree
	 *            - TreeModel dell'evento corrente a cui agganciare gli eventi collegati
	 * @param aEventoKey
	 *            - id dell'evento correlato (annotazione)
	 * @param aConn
	 * @return
	 * @throws Exception
	 */
	private TreeModel prelevaDatiAnnRidetPenaAltro(TreeModel aTree, BigDecimal aEventoKey, Connection aConn)
			throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Prelevo i dati dell'evento di annotazione ");
		EventoSqlDAO lEveDao = null;
		EventoModel lEveAnnMod;

		AnnotazioneManualeSqlDAO lAnnManSqlDAO = null;
		Vector lListaAnnoManuali = null;

		CampoNotaSqlDAO lCamSqlDao = null;
		CampoNotaModel lCamMod = null;

		FungibilitaSqlDAO lFungSqlDao = null;
		FungibilitaModel lFunModel = null;

		TreeModel lEveAnnTree = null;
		try {
			// n.b. se di ufficio recupero l'evento di procura (Annotazione) altrimenti i
			// dati dell'evento di Altro ufficio
			lEveDao = new EventoSqlDAO(aConn);
			lEveDao.ricercaEventoByKey(aEventoKey);
			lEveAnnMod = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();
			BigDecimal idAnnotazione = lEveAnnMod.getIdEvento();

			// ========================================================================
			// Prelevo i dati dei quantum Computati (Annotazioni Manuali)
			// n.b. per la stampa mi servono solo le stringhe dei totali per cui li
			// calcolo qui e passo solo le stringhe
			// ========================================================================
			lAnnManSqlDAO = new AnnotazioneManualeSqlDAO(aConn);
			lAnnManSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEventoKey);
			lListaAnnoManuali = new Vector(lAnnManSqlDAO.getModels());

			if (lEveAnnMod != null) {
				// ==============================================
				// Vedo se esiste provvedimento altra autorita' puntato dall'annotazione
				// ==============================================
				EventoModel lEveAltraAut = null;
				if (lEveAnnMod.getEveIdEvento() != null) {
					lEveDao.ricercaEventoByKey(lEveAnnMod.getEveIdEvento());
					lEveAltraAut = (EventoModel) lEveDao.getModelByKey();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("evento altra autorita' = " + lEveAltraAut);

					if (lEveAltraAut != null) {
						// TreeModel lEveAATree = null;
						lEveAnnMod = lEveAltraAut;

						lEveAnnTree = new TreeModel((EventoModel) lEveAnnMod);
						aTree.add(lEveAnnTree);
						// lEveAATree = new TreeModel((EventoModel) lEveAltraAut);
						// aTree.add(lEveAATree);
					}
				} else {
					lEveAnnTree = new TreeModel((EventoModel) lEveAnnMod);
					aTree.add(lEveAnnTree);
				}
			}

			// Vedo se esiste provvedimento altra autorita'
			EventoModel lEveAltraAut = null;
			if (lEveAnnMod.getEveIdEvento() != null) {
				lEveDao.ricercaEventoByKey(lEveAnnMod.getEveIdEvento());
				lEveAltraAut = (EventoModel) lEveDao.getModelByKey();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("evento altra autorita' = " + lEveAltraAut);

				if (lEveAltraAut != null) {
					TreeModel lEveAATree = null;
					lEveAATree = new TreeModel((EventoModel) lEveAltraAut);
					aTree.add(lEveAATree);
				}
			}

			// ========================================================================
			// Scorro la lista delle Annotazione e calcolo i quantun aggregati per
			// Reclusiione e arresto
			// ========================================================================
			CalendarModel lCalTotaleAggregatoRec = new CalendarModel();
			CalendarModel lCalTotaleAggregatoArr = new CalendarModel();
			CalendarUtil lCalUtil = new CalendarUtil();

			for (int i = 0; i < lListaAnnoManuali.size(); i++) {
				AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) lListaAnnoManuali.elementAt(i);

				// Reclusione/Multa
				CalendarModel lCalReclusione = new CalendarModel();
				lCalReclusione = lAnnMod.getQuantumReclusione();
				if (lAnnMod.getImportoMulta() != null)
					lCalReclusione.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

				if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals("+")) {
					lCalTotaleAggregatoRec = lCalUtil.sommaGiornieValute(lCalTotaleAggregatoRec,
							lCalReclusione);
				} else {
					lCalTotaleAggregatoRec = lCalUtil.sottraiGiorniValuteNew(lCalTotaleAggregatoRec,
							lCalReclusione);
				}

				// Arresti/Ammenda
				CalendarModel lCalArresti = new CalendarModel();
				lCalArresti = lAnnMod.getQuantumArresto();
				if (lAnnMod.getImportoAmmenda() != null)
					lCalArresti.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());

				if (lAnnMod.getFlagPiuMeno() != null && lAnnMod.getFlagPiuMeno().equals("+")) {
					lCalTotaleAggregatoArr = lCalUtil.sommaGiornieValute(lCalTotaleAggregatoArr, lCalArresti);
				} else {
					lCalTotaleAggregatoArr = lCalUtil.sottraiGiorniValuteNew(lCalTotaleAggregatoArr,
							lCalArresti);
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Totale lCalTotaleAggregatoRec = " + lCalTotaleAggregatoRec);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Totale lCalTotaleAggregatoArr = " + lCalTotaleAggregatoArr);

			// ======================
			// Reclusione e Multa
			// ======================
			String lStringaReclusione = "";
			if (CalendarUtil.getTotGiorni(lCalTotaleAggregatoRec) > 0) {
				lStringaReclusione = "aumenta la pena di "
						+ SiapStringUtil.formattaQuantum(lCalTotaleAggregatoRec.getNumAnni(),
								lCalTotaleAggregatoRec.getNumMesi(), lCalTotaleAggregatoRec.getNumGiorni())
						+ " di Reclusione";
			} else if (CalendarUtil.getTotGiorni(lCalTotaleAggregatoRec) < 0) {
				CalendarModel lCalModelApp = new CalendarModel(lCalUtil.abs(lCalTotaleAggregatoRec));
				lStringaReclusione = "diminuisce la pena di "
						+ SiapStringUtil.formattaQuantum(lCalModelApp.getNumAnni(),
								lCalModelApp.getNumMesi(), lCalModelApp.getNumGiorni()) + " di Reclusione";
			}

			// Aggiungo Eventuale Multa
			if (lCalTotaleAggregatoRec.getImportoMulta() != 0) {
				if (!lStringaReclusione.equals("")) {
					lStringaReclusione = lStringaReclusione
							+ " Multa Euro "
							+ StringUtils.toEuroFormat(new BigDecimal(Math.abs(lCalTotaleAggregatoRec
									.getImportoMulta())));
				} else if (lCalTotaleAggregatoRec.getImportoMulta() > 0) {
					lStringaReclusione = "aumenta la pena di Multa Euro "
							+ StringUtils.toEuroFormat(new BigDecimal(Math.abs(lCalTotaleAggregatoRec
									.getImportoMulta())));
				} else if (lCalTotaleAggregatoRec.getImportoMulta() < 0) {
					lStringaReclusione = "diminuisce la pena di Multa Euro "
							+ StringUtils.toEuroFormat(new BigDecimal(Math.abs(lCalTotaleAggregatoRec
									.getImportoMulta())));
				}
			}

			// =====================
			// Arresti e Ammenda
			// =====================
			String lStringaArresto = "";
			if (CalendarUtil.getTotGiorni(lCalTotaleAggregatoArr) > 0) {
				lStringaArresto = "aumenta la pena di "
						+ SiapStringUtil.formattaQuantum(lCalTotaleAggregatoArr.getNumAnni(),
								lCalTotaleAggregatoArr.getNumMesi(), lCalTotaleAggregatoArr.getNumGiorni())
						+ " di Arresto";
			} else if (CalendarUtil.getTotGiorni(lCalTotaleAggregatoArr) < 0) {
				CalendarModel lCalModelApp = new CalendarModel(lCalUtil.abs(lCalTotaleAggregatoArr));
				lStringaArresto = "diminuisce la pena di "
						+ SiapStringUtil.formattaQuantum(lCalModelApp.getNumAnni(),
								lCalModelApp.getNumMesi(), lCalModelApp.getNumGiorni()) + " di Arresto";
			}

			// Aggiungo Eventuale Ammenda
			if (lCalTotaleAggregatoArr.getImportoAmmenda() != 0) {
				if (!lStringaArresto.equals("")) {
					lStringaArresto = lStringaArresto
							+ " Ammenda Euro "
							+ StringUtils.toEuroFormat(new BigDecimal(Math.abs(lCalTotaleAggregatoArr
									.getImportoAmmenda())));
				} else if (lCalTotaleAggregatoArr.getImportoAmmenda() > 0) {
					lStringaArresto = "aumenta la pena di Ammenda Euro "
							+ StringUtils.toEuroFormat(new BigDecimal(Math.abs(lCalTotaleAggregatoArr
									.getImportoAmmenda())));
				} else if (lCalTotaleAggregatoArr.getImportoAmmenda() < 0) {
					lStringaArresto = "diminuisce la pena di Ammenda Euro "
							+ StringUtils.toEuroFormat(new BigDecimal(Math.abs(lCalTotaleAggregatoArr
									.getImportoAmmenda())));
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("lStringaReclusione=" + lStringaReclusione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("lStringaArresto=" + lStringaArresto);

			// SiapStringUtil.formattaQuantum(lCalReclusione.getNumAnni(), lCalReclusione.getNumMesi(),
			// lCalReclusione.getNumGiorni());
			// SiapStringUtil.formattaQuantum(lCalArresto.getNumAnni(), lCalArresto.getNumMesi(),
			// lCalArresto.getNumGiorni());

			lEveAnnMod.setTotalePeriodiReclusione(lStringaReclusione);
			lEveAnnMod.setTotalePeriodiArresto(lStringaArresto);

			// ========================================================================
			// Aggiungo le annotazioni manuali al TreeModel
			for (int i = 0; i < lListaAnnoManuali.size(); i++) {
				AnnotazioneManualeModel lAnnModel = (AnnotazioneManualeModel) lListaAnnoManuali.elementAt(i);
				lAnnModel.calcolaStringaReclusione();
				lAnnModel.calcolaStringaArresto();
				TreeModel lAnnModTree = new TreeModel(lAnnModel);
				lEveAnnTree.add(lAnnModTree);
			}

			// ================================================
			// Recupero le note
			// ================================================
			lCamSqlDao = new CampoNotaSqlDAO(aConn);

			lCamSqlDao.ricercaCampoNotaByKeyEvento(lEveAnnMod.getIdEvento());
			lCamMod = (CampoNotaModel) lCamSqlDao.getModelByKey();
			TreeModel lTreeCampoNota = new TreeModel((CampoNotaModel) lCamMod);
			lEveAnnTree.add(lTreeCampoNota);

			// =========================================================
			// Recupero l'eventuale fungibilita'
			// =========================================================
			lFungSqlDao = new FungibilitaSqlDAO(aConn);
			lFungSqlDao.ricercaFungibilitaByKeyEvento(idAnnotazione);
			lFunModel = (FungibilitaModel) lFungSqlDao.getModelByKey();
			if (lFunModel != null) {
				lFunModel.calcolaStringaFungibilita();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Aggiungo fungibilita' = " + lFunModel);
				TreeModel lTreeFung = new TreeModel(lFunModel);
				lEveAnnTree.add(lTreeFung);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lEveDao);
			cleanup(lCamSqlDao);
			cleanup(lAnnManSqlDAO);
		}
		return aTree;
	}

	/**
	 * ************************************************************************** Preleva i dati dal DB per le
	 * stampe del Differimento
	 * 
	 * @param aEveNotModel
	 *            -
	 * @param aUtenteModel
	 *            - Contiene le informazioni sull'utente da inserire nel treeModel
	 * @return
	 * @throws F3BException
	 * @deprecated Non piu' utilizzato (01/09/2006)
	 ************************************************************************** */
	public TreeModel prelevaDatiDifferimento(EventoNotificaModel aEveNotModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasDao = null;
		EventoSqlDAO lEventoSqlDAO = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		PenaPrecedenteSqlDAO lPenPrecDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;
		PenaResiduaSqlDAO lPenaResDao = null;
		SospensioneSqlDAO lSospSql = null;
		SospensioneModel lSosp = null;

		BigDecimal lKeyFascicolo = aEveNotModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lConn = getDBConnection();

			// ========================================================================
			// Fascicolo
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Recupero dati fascicolo: start...");
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr(): " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Recupero dati fascicolo: ...stop");

			// ========================================================================
			// Pena Residua Precedente (che ci fasccio?)
			// order by data inserimento desc
			// validata
			// (FLAG_PENA_SOSPESA = 'N' OR FLAG_PENA_SOSPESA IS NULL )
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Recupero dati PenaPrecedente: start...");
			lPenPrecDao = new PenaPrecedenteSqlDAO(lConn);
			lPenPrecDao.ricercaPenaPrecedenteSospensioniByFascicolo(lKeyFascicolo);
			Vector lPrec = new Vector(lPenPrecDao.getModels());

			PenaPrecedenteModel lPenPrecedente = null;

			if (lPrec != null && lPrec.size() > 0) {
				lPenPrecedente = new PenaPrecedenteModel();
				lPenPrecedente = (PenaPrecedenteModel) lPrec.firstElement();
				// Stringa Arresto - Reclusione
				if (lPenPrecedente != null) {
					lPenPrecedente.calcolaStringaReclusione();
					lPenPrecedente.calcolaStringaArresto();
					lPenPrecedente.calcolaStringaIsolamento();
					if (lPenPrecedente.getDataFine() != null
							&& lPenPrecedente.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
							// GDV 23/10/2006 a6-rr-328
							&& lPenPrecedente.getFlagErgastolo() != null
							&& !lPenPrecedente.getFlagErgastolo().equals("S")
							&& !lPenPrecedente.getFlagErgastolo().equals("D"))
						lPenPrecedente.setImmediataScarcerazione("S");
					else {
						if (lPenPrecedente != null)
							lPenPrecedente.setImmediataScarcerazione("N");
					}
				}
				lPenPrecDao.stop();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Recupero dati PenaPrecedente: ...stop");

			// ========================================================================
			// Pena Residua
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Recupero dati PenaResidua corrente: start...");
			lPenaResDao = new PenaResiduaSqlDAO(lConn);
			lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lKeyFascicolo);
			PenaResiduaModel lPenresMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
			if (lPenresMod != null) {
				lPenresMod.calcolaStringaReclusione();
				lPenresMod.calcolaStringaArresto();
				lPenresMod.calcolaStringaIsolamento();
				if (lPenresMod.getDataFine() != null
						&& lPenresMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
						// GDV 23/10/2006 a6-rr-328
						&& lPenresMod.getFlagErgastolo() != null
						&& !lPenresMod.getFlagErgastolo().equals("S")
						&& !lPenresMod.getFlagErgastolo().equals("D")) {
					lPenresMod.setImmediataScarcerazione("S");
				} else {
					if (lPenresMod != null)
						lPenPrecedente.setImmediataScarcerazione("N");

				}
			}
			lPenaResDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Recupero dati PenaResidua corrente: ...stop");

			// ========================================================================
			// Sospensione
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Recupero dati della Sospensione: start...");
			lSospSql = new SospensioneSqlDAO(lConn);
			if (lPenresMod != null) {
				lSospSql.ricercaSospensioneByIdPenaResidua(lPenresMod.getIdPenaResidua());
				lSosp = (SospensioneModel) lSospSql.getModelByKey();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Recupero dati della Sospensione: ...stop");

			// ========================================================================
			// Avvocati
			// ========================================================================
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
			Vector lAvvocati = new Vector(lAvvDao.getModels());

			// ========================================================================
			// Costruzione del TreeModel
			// ========================================================================
			// ==========================
			// Creazione del root (X)
			// <X>
			// ...
			// </X>
			// ==========================
			lTreeRoot = new TreeModel(createRoot(aEveNotModel, aUtenteModel));
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("createRoot: "+ReportGenerator.debugTreeXML(lTreeRoot));

			// =================================
			// Aggiunta nodo utente X/Utente
			// <X>
			// ...
			// <Utente>
			// ...
			// </Utente>
			// </X>
			// =================================
			lTreeRoot.add(new TreeModel(aUtenteModel));
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("lTreeRoot add utente: "+ReportGenerator.debugTreeXML(lTreeRoot));

			// Creazione nodo FascicoloSiep
			// <FascicoloSiep>
			// ...
			// </FascicoloSiep>
			TreeModel lTreeFasMod = new TreeModel(lFasModel);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("lTreeFasMod: "+ReportGenerator.debugTreeXML(lTreeFasMod));

			// Aggiungo alla sezione FascicoloSiep i sottoelementi
			// Creazione nodo FascicoloSiep
			// <FascicoloSiep>
			// ...
			// <LuogoDetenzione>...</LuogoDetenzione>
			// <PosizioneGiuridica>...</PosizioneGiuridica>
			// <Reato>...</Reato>
			// <Circostanza>...</Circostanza>
			// <Circostanza>...</Circostanza>
			// <TotaleMisure>...</TotaleMisure>
			// <PenaComplessiva>...</PenaComplessiva>
			// <PosizioneGiuridica>...</PosizioneGiuridica>
			// </FascicoloSiep>
			this.appendTableToFascicoloSiep(lConn, lKeyFascicolo, lTreeFasMod, lFasModel.getFlagAltraCausa());

			// ========================================================================
			// Crea il nodo Evento opportunamente modificato per la stampa
			// ========================================================================
			aEveNotModel.getEvento().setEventoCorrente("S");
			aEveNotModel.setEvento(this.mEventoUtils.setEventoDiStampa(aEveNotModel.getEvento(), lConn));

			TreeModel lTreeEveMod = new TreeModel(aEveNotModel.getEvento());

			// ========================================================================
			// Aggiunge al nodo Evento i destinatari per le notifiche e gli avvocati
			// <Evento>
			// ......
			// <Notifica>.Tds.</Notifica>
			// <Notifica>.Avvocato 1.</Notifica>
			// <Notifica>.Avvocato 2.</Notifica>
			// </Evento>
			// ========================================================================
			this.mEventoUtils.appendNotifiche(lTreeEveMod, aEveNotModel, lAvvocati, aUtenteModel);

			// ===============================================
			// Aggiungo il nodo Evento (+Notifiche) al root
			// <X>
			// ...
			// <Utente>...</Utente>
			// <Evento>
			// ......
			// <Notifica>.Tds.</Notifica>
			// <Notifica>.Avvocato 1.</Notifica>
			// <Notifica>.Avvocato 2.</Notifica>
			// </Evento>
			// </X>
			// ===============================================
			lTreeRoot.add(lTreeEveMod);

			// ========================================================================
			// Crea e Aggiunge il nodo Magistrato al nodo Evento
			// n.b. in questo modo viene aggiunto anche al TreeRoot
			// ========================================================================
			if (aEveNotModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEveNotModel.getMagistrato()));

			// ========================================================================
			// Aggiungo lo Stato di Esecuzione
			// ========================================================================
			if (SIESSwitch.isReworkStatoEsecuzioneOn()) {
				// Se sono nel rework dello stato esecuzione chiamo il nuovo
				// controller che gestisce lo stato esec.

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Prima di appendere lo Stato di Esecuzione");
				StatoEsecuzioneController lStat = new StatoEsecuzioneController();
				lStat.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Ho appeso lo Stato di Esecuzione");

			} else
				this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lKeyFascicolo);

			// ========================================================================
			// Nodi livello 1 Fascicolo - Soggetto - Sentenza
			// ========================================================================
			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn, null);

			if (lPenPrecedente != null) {
				TreeModel lTreePenPrec = new TreeModel(lPenPrecedente);
				lTreeFasMod.add(lTreePenPrec);
			}

			// =============================
			// Pena residua + Sospensione (Aggiunti al fascicolo. Pareche'????)
			// <PenaResidua>
			// <Sospensione>
			// </Sospensione>
			// </PenaResidua>
			// =============================
			if (lPenresMod != null) // Pena Residua
			{
				TreeModel lTreelPenresMod = new TreeModel(lPenresMod);
				if (lSosp != null) {
					TreeModel lTreeSosp = new TreeModel(lSosp);
					lTreelPenresMod.add(lTreeSosp);
				}
				lTreeFasMod.add(lTreelPenresMod);
			}

			// =================================
			// Add Avvocati per Fascicolo
			// =================================
			Iterator lItx = null;

			if (lAvvocati != null) {
				lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
					TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
					lTreeFasMod.add(lTreeAvvMod);
					lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
				}
			}

			lTreeRoot.add(lTreeFasMod);

			// cumulo
			if (lFasModel.getFlagCumulante() != null && lFasModel.getFlagCumulante().equals("S")) {
				TreeModel lPenCumModTree = getPenaCumulo(lKeyFascicolo, lConn);
				lTreeRoot.add(lPenCumModTree);
			}

			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiSospensioni: " + daoEx);
		}

		catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("StampaController.prelevaDatiSospensioni: " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lAvvDao);
			cleanup(lEventoSqlDAO);
			cleanup(lSanDao);
			cleanup(lPenPrecDao);
			cleanup(lPenaResDao);
			cleanup(lSospSql);

			cleanup(lConn);
		}

		return lTreeRoot;
	}

	/**
	 * Controlla se il motivo e' una rideterminazione pena Altro
	 * 
	 * @param aEveModel
	 * @return
	 */
	private boolean isRidetPenaAltro(EventoModel aEveModel) {
		boolean lIsRidetPenaAltro = false;
		if (aEveModel != null && aEveModel.getEveIdEvento() != null && aEveModel.getCodMotivo() != null) {
			int intCodiceMotivo = Integer.parseInt(aEveModel.getCodMotivo());

			// 0963-->0974 [0989,0990,1003,1004] Ordini scarcerazione
			// 0975-->0986 [0991,0992] Comunicazione
			// [0999,1006,0994,1003,1004,1005,1006]
			if ((intCodiceMotivo >= 960 && intCodiceMotivo <= 986)
					|| (intCodiceMotivo >= 989 && intCodiceMotivo <= 992)
					// add d.f. 09/2014 mancava la gestione di alcuni codici
					|| (intCodiceMotivo >= 1003 && intCodiceMotivo <= 1004) || (intCodiceMotivo == 999)
					|| (intCodiceMotivo == 994) || (intCodiceMotivo == 1005) || (intCodiceMotivo == 1006)
					|| (intCodiceMotivo == 1010)) {
				lIsRidetPenaAltro = true;
			}
		}

		/*
		 * if ( aEveModel != null && aEveModel.getEveIdEvento()!=null && aEveModel.getCodMotivo() != null && (
		 * aEveModel.getCodMotivo().equals("0960") || aEveModel.getCodMotivo().equals("0961") ||
		 * aEveModel.getCodMotivo().equals("0962") // Rideterminazione ||
		 * aEveModel.getCodMotivo().equals("0963") || aEveModel.getCodMotivo().equals("0964") ||
		 * aEveModel.getCodMotivo().equals("0965") || aEveModel.getCodMotivo().equals("0966") ||
		 * aEveModel.getCodMotivo().equals("0967") || aEveModel.getCodMotivo().equals("0968") ||
		 * aEveModel.getCodMotivo().equals("0969") || aEveModel.getCodMotivo().equals("0970") ||
		 * aEveModel.getCodMotivo().equals("0971") || aEveModel.getCodMotivo().equals("0972") ||
		 * aEveModel.getCodMotivo().equals("0973") || aEveModel.getCodMotivo().equals("0974") ||
		 * aEveModel.getCodMotivo().equals("0989") || aEveModel.getCodMotivo().equals("0990") // Comunicazione
		 * || aEveModel.getCodMotivo().equals("0975") || aEveModel.getCodMotivo().equals("0976") ||
		 * aEveModel.getCodMotivo().equals("0977") || aEveModel.getCodMotivo().equals("0978") ||
		 * aEveModel.getCodMotivo().equals("0979") || aEveModel.getCodMotivo().equals("0980") ||
		 * aEveModel.getCodMotivo().equals("0981") || aEveModel.getCodMotivo().equals("0982") ||
		 * aEveModel.getCodMotivo().equals("0983") || aEveModel.getCodMotivo().equals("0984") ||
		 * aEveModel.getCodMotivo().equals("0985") || aEveModel.getCodMotivo().equals("0986") ||
		 * aEveModel.getCodMotivo().equals("0991") || aEveModel.getCodMotivo().equals("0992") ) ) {
		 * lIsRidetPenaAltro = true; }
		 */

		return lIsRidetPenaAltro;
	}

	/**
	 * Preleva i dati dal DB per le stampe di IStruttoria e Nuova Istanza
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	public TreeModel prelevaDatiPenaSospesa(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		ReatoSqlDAO lReaDao = null;
		NotificaSqlDAO lNotDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		PenaComplessivaSqlDAO lPenaComplDAO = null;
		SanzioneSostitutivaSqlDAO lSSostsqlDAO = null;
		AnnotazioneManualeSqlDAO lAnnoSqlDao = null;
		// IstanzaSqlDAO lIstSqlDao = null;
		// NuovaIstanzaSqlDAO lIstSqlDao = null;
		// NotiziaReatoSqlDAO lNotRDao = null;
		// SanzioneSostResiduaSqlDAO lSSSqlDAO = null;
		// CircostanzaSqlDAO lCircDao = null;
		// TreeModel lTreeEventoPrec = null;

		BigDecimal lKeyFascicolo = aEveModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lTreeRoot = new TreeModel(createRoot(aEveModel, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			lConn = getDBConnection();

			// Fascicolo
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("prima dell'if del codice RES = "
					+ lFasModel.getCodOperatoreInserimento().startsWith("RES"));
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("SONo entrata nell'if del codice RES");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr(): " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			TreeModel lTreeEveMod = new TreeModel(aEveModel.getEvento());

			// 02/03/2011 Aggiunto il Magistrato.
			if (aEveModel.getMagistrato() != null
					&& aEveModel.getMagistrato().getCodMagistrato().compareTo("-") != 0)
				lTreeEveMod.add(new TreeModel(aEveModel.getMagistrato()));

			int count = 0;
			while (count < aEveModel.getNotifiche().length) { // Notifiche con Autorita' Esterna
				NotificaModel lNot = new NotificaModel(aEveModel.getNotifiche()[count]);
				TreeModel lTreeNot = new TreeModel(lNot);
				lTreeEveMod.add(lTreeNot);

				if (aEveModel.getNotifiche()[count].getAutoritaEsterna() != null) {
					lTreeNot.add(new TreeModel(aEveModel.getNotifiche()[count].getAutoritaEsterna()));
				}
				if (aEveModel.getNotifiche()[count].getUfficio() != null) {
					lTreeNot.add(new TreeModel(aEveModel.getNotifiche()[count].getUfficio()));
				}

				// paolo cherubini 11/01/2011 per far uscire l'istituto
				if ((aEveModel.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null)
						&& (aEveModel.getNotifiche()[count].getIstitutoDetenzione() != null)) {
					AutoritaEsternaModel lAutFinto = new AutoritaEsternaModel();

					lAutFinto.setDescrTipoAutorita(aEveModel.getNotifiche()[count].getIstitutoDetenzione()
							.getDescrTipoIstituto());
					// lAutFinto.setDescrSede(aEveModel.getNotifiche()[count].getIstitutoDetenzione().getDescrComune()
					// + ", " + aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo());
					/*
					 * DEREMMARE PER ELIMINARE IL PROBLEMA DEL NULL DOVE MANCA L'INDIRIZZO DELL'ISTITUTO DI
					 * DETENZIONE -----> NON TESTATA
					 */
					if (aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo() != null
							&& !aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo()
									.equalsIgnoreCase("null")) {
						lAutFinto.setDescrSede(aEveModel.getNotifiche()[count].getIstitutoDetenzione()
								.getDescrComune()
								+ ", "
								+ aEveModel.getNotifiche()[count].getIstitutoDetenzione().getIndirizzo());
					} else {
						lAutFinto.setDescrSede(aEveModel.getNotifiche()[count].getIstitutoDetenzione()
								.getDescrComune());
					}
					lTreeNot.add(new TreeModel(lAutFinto));
				}

				count++;
			}

			// Reati
			// ***
			AnnotazioneManualeModel lAnnMod = null;
			if (aEveModel != null && aEveModel.getEvento() != null
					&& aEveModel.getEvento().getAnnIdAnnotazioneManuale() != null) {
				BigDecimal lAnnIdAnnotazioneManuale = aEveModel.getEvento().getAnnIdAnnotazioneManuale();

				lAnnoSqlDao = new AnnotazioneManualeSqlDAO(lConn);

				lAnnoSqlDao.ricercaAnnotazioneManualeByKey(lAnnIdAnnotazioneManuale);
				lAnnMod = (AnnotazioneManualeModel) lAnnoSqlDao.getModelByKey();

				// cerco le descr di ufficio e comune che sono solo codificate!!!
				DecodificheModel lModel = new DecodificheModel();
				lModel.setContesto("TIPO_UFFICIO");	// 23/04/2019  MEV70
				IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
				Collection Uffi_Emi = lDecodifiche.ExRicercaDecodifiche(lModel);
				String strDescrUfficio = "";
				java.util.Iterator itxOggetto = Uffi_Emi.iterator();
				while (itxOggetto.hasNext()) {
					DecodificheModel lDecMod = (DecodificheModel) itxOggetto.next();
					if (lDecMod.getCode().equals(lAnnMod.getCodTipoUfficioSiep())) {
						strDescrUfficio += lDecMod.getDescription();
						break;
					}
				}
				IComune comctrl = SICOLookupRemote.getComuneRemote();
				ComuneModel comunemod = comctrl.ExRicercaComuneByKey(lAnnMod.getCodLuogoUfficioSiep());
				lAnnMod.setDescrTipoUfficioSiep(strDescrUfficio);
				lAnnMod.setDescrLuogoUfficioSiep(comunemod.getDescrizione());
			}
			// ***
			TreeModel lTreeFasMod = new TreeModel(lFasModel);
			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));

			Vector lReati = null;

			// 04/07/2011 Lettura Reati afferenti al Fascicolo SIEP.
			IReato lReaCtr = SIEPLookupRemote.getReatoRemote();
			lReati = lReaCtr.ExRicercaReatoCircostanzaByFascicolo(lKeyFascicolo);
			Iterator lItx = null;
			if (lReati != null) {
				Vector lReatiPerCont = new Vector();
				lItx = lReati.iterator();
				while (lItx.hasNext()) {
					// Add gerarchia dei Reati - Circostanza
					ReatoCircostanzaModel lReatoModel = new ReatoCircostanzaModel(
							(ReatoCircostanzaModel) lItx.next());
					TreeModel lTreeReatoMod = new TreeModel(lReatoModel.getReato());

					// Creo Vettore dei Reati per le Continuazioni
					ReatoModel lReato = lReatoModel.getReato();
					if (lReato != null) {
						lReato.calcolaPenaReato();
						lReato.calcolaStringaPenaPecuniaria();// <==== modifica template import
					}

					lReatiPerCont.add(lReato);

					int countR = 0; // Circostanze
					while (countR < lReatoModel.getCircostanze().length) {
						TreeModel lTreeCirc = new TreeModel(lReatoModel.getCircostanze()[countR]);
						lTreeReatoMod.add(lTreeCirc);
						countR++;
					}
					lTreeFasMod.add(lTreeReatoMod);
				}
			}

			// 04/07/2011 Lettura Reati afferenti all'Annotazione Manuale.
			TreeModel lTreeAnnManMod = null;
			if (lAnnMod != null) {
				lTreeAnnManMod = new TreeModel(lAnnMod);
				IPenaSospesa lCtrlPSosp = SIEPLookupRemote.getPenaSospesaRemote();
				lReati = lCtrlPSosp.ExRicercaReatiByAnnotazioneMan(lAnnMod.getIdAnnotazioneManuale());
				Iterator lItx1 = null;
				if (lReati != null) {
					Vector lReatiPerCont = new Vector();
					lItx1 = lReati.iterator();
					while (lItx1.hasNext()) {
						// Add gerarchia dei Reati - Circostanza
						ReatoModel lReato = (ReatoModel) lItx1.next();

						TreeModel lTreeReatoMod = new TreeModel(lReato);

						// Creo Vettore dei Reati per le Continuazioni
						if (lReato != null) {
							lReato.calcolaPenaReato();
							lReato.calcolaStringaPenaPecuniaria();// <==== modifica template import
						}

						lReatiPerCont.add(lReato);

						// int countR = 0; // Circostanze
						lTreeAnnManMod.add(lTreeReatoMod);
					}
				}
			}

			// Note
			this.prelevaDatiCampoNota(lTreeEveMod, aEveModel.getEvento().getIdEvento(), lConn);

			// [EC] - 20171030 : intervento per risoluzione anomalia segnalata dalla procura di bologna in relazione alla stampa dell'OE a seguito di un 
			// provvedimento di INTERRUZIONE PER EVASIONE (email Maffucci del 25102017).
			// Intervento effettuato : il metodogetTreeSoggetto non prendeva in input l'oggetto lFasModel valorizzato, ma veniva passato sempre a NULL! 
			// Per la distinzione sulla stampa tra  minorenni e maggiorenni serve che l'oggetto lFasModel sia valorizzato!			
			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn, lFasModel);

			// 04/07/2011 PENA COMPLESSIVA afferente al Fascicolo SIEP
			PenaComplessivaModel lPenaComplMod = null;
			lPenaComplDAO = new PenaComplessivaSqlDAO(lConn);
			lPenaComplDAO.ricercaPenaComplessivaByIdFascicolo(lKeyFascicolo);
			lPenaComplMod = (PenaComplessivaModel) lPenaComplDAO.getModelByKey();

			if (lPenaComplMod != null) {
				lPenaComplMod.calcolaStringaArresto();
				lPenaComplMod.calcolaStringaIsolamento();
				lPenaComplMod.calcolaStringaReclusione();
				TreeModel lTreePenaComplMod = new TreeModel(lPenaComplMod);
				lTreeFasMod.add(lTreePenaComplMod);
			}

			// 04/07/2011 PENA COMPLESSIVA afferente all' Annotazione Manuale
			if (lAnnMod != null) {
				lPenaComplMod = null;
				IPenaSospesa lCtrlPSosp = SIEPLookupRemote.getPenaSospesaRemote();
				PenaComplessivaSanzioneSostitutivaModel dettaglioPenaComplessiva = lCtrlPSosp
						.ExRicercaPenaComplessivaByAnnotazioneMan(lAnnMod.getIdAnnotazioneManuale());
				if (dettaglioPenaComplessiva != null)
					lPenaComplMod = dettaglioPenaComplessiva.getPenaComplessiva();

				if (lPenaComplMod != null) {
					lPenaComplMod.calcolaStringaArresto();
					lPenaComplMod.calcolaStringaIsolamento();
					lPenaComplMod.calcolaStringaReclusione();
					TreeModel lTreePenaComplMod = new TreeModel(lPenaComplMod);
					lTreeAnnManMod.add(lTreePenaComplMod);
				}
			}
			// END PENA COMPLESSIVA

			// SANZIONE SOSTITUTIVA
			lSSostsqlDAO = new SanzioneSostitutivaSqlDAO(lConn);
			SanzioneSostitutivaModel lSanzSostMod = null;

			if (lPenaComplMod != null && lPenaComplMod.getIdPenaComplessiva() != null) {
				lSSostsqlDAO.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenaComplMod
						.getIdPenaComplessiva());
				lSanzSostMod = (SanzioneSostitutivaModel) lSSostsqlDAO.getModelByKey();
			}

			if (lSanzSostMod != null) {
				lSanzSostMod.calcolaStringaSanzione();
				TreeModel lTreeSanzSost = new TreeModel(lSanzSostMod);
				lTreeFasMod.add(lTreeSanzSost);
			}
			// END SANZIONE SOSTITUTIVA

			// Aggiungo la posizione Giuridica
			this.addPosizioneGiuridica(lConn, lKeyFascicolo, lTreeFasMod, lFasModel.getFlagAltraCausa());

			lTreeRoot.add(lTreeEveMod);
			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSogMod);

			// I dati della sentenza sono nell'annotazione manuale!!!
			// lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));

			// 04/07/2011 Annotazione Manuale e reati annessi caricati in lTreeA
			// TreeModel lTreeSenMod = new TreeModel(lAnnMod);
			// lTreeRoot.add(lTreeSenMod);
			lTreeRoot.add(lTreeAnnManMod);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiPenaSospesa: Non posso leggere : " + daoEx);
		} catch (F3BException f3bEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiPenaSospesa: " + f3bEx);
			throw f3bEx;
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("StampaController.prelevaDatiPenaSospesa: " + sqe, sqe);
			throw new F3BException("StampaController.prelevaDatiPenaSospesa: Eccezione Generica: " + sqe);
		}

		finally {
			cleanup(lReaDao);
			cleanup(lFasDao);

			cleanup(lNotDao);
			cleanup(lPenaComplDAO);
			cleanup(lConn);
		}

		Enumeration e = lTreeRoot.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("elemento =" + e.nextElement().toString());
		}
		Enumeration lChildRoot = lTreeRoot.children();

		int lDepth = lTreeRoot.getDepth();
		int nChildCount = lTreeRoot.getChildCount();
		// Ciclo di primo livello sui figli della root
		while (lChildRoot.hasMoreElements()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info(
					"Depth = " + lDepth + " Level=" + lTreeRoot.getLevel() + "  figli " + nChildCount);
			TreeModel lTNod = (TreeModel) lChildRoot.nextElement();

			if (!lTNod.isLeaf() && lTNod.getChildCount() > 0) {
				Enumeration lEnumCH2 = lTNod.children();

				// Ciclo a livello 2
				while (lEnumCH2.hasMoreElements()) {
					TreeModel lTNod2 = (TreeModel) lEnumCH2.nextElement();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info("LIVELLO2----->" + lTNod2.getModel().getClass().getName());
				}// fine while 2 livello
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("LIVELLO1-->" + lTNod.getModel().getClass().getName());
		}// fine for sui figli della root

		return lTreeRoot;
	}

}