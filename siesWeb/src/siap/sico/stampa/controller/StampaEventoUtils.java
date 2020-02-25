package siap.sico.stampa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.calendar.model.TotalePeriodoModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoPerStampaSqlDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoLicenzePeriodiModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.libertaanticipata.model.TotalePeriodoLicenzaAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaPerStatoEsecuzioneSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.archiviazione.dao.ArchiviazioneSqlDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.calcolopena.controller.CalcoloPenaControllerF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.dao.PenaResiduaPerStatoEsecuzioneSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: StampaEventoUtils
 * </p>
 * <p>
 * Description: Classe di utilità dello SiapStampaCOntroller per lo stato esecuzione
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaEventoUtils extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StampaEventoUtils() {
	}

	/**
	 * appendStatoEsecuzione - Crea il TreeMOdel dello stato di esecuzione da appendere al fascicolo corrente
	 * 
	 * @param aModalita
	 * @param aTreeRoot
	 * @param lConn
	 *            - Connessione
	 * @param lKeyFascicolo
	 *            - Chiave Fascicolo Siep
	 * @throws F3BException
	 */
	public void appendStatoEsecuzione(String aModalita, TreeModel aTreeRoot, Connection lConn,
			BigDecimal lKeyFascicolo) throws F3BException {

		EventoPerStampaSqlDAO lEveDaoStampa = null;
		PenaResiduaPerStatoEsecuzioneSqlDAO lPenaResStatDao = null;
		MisuraAlternativaPerStatoEsecuzioneSqlDAO lMisAltStatDAO = null;
		LicenzaLibanticipataSqlDAO lLibDAO = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;
		AnnotazioneManualeSqlDAO lAnnoSqlDao = null;
		TenoreSqlDAO lTenSql = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSql = null;
		DepositoDecretoSqlDAO lDepDecSql = null;
		DepositoOrdinanzaPcModel lDepOrdPCMod;
		DepositoDecretoModel lDepDecMod;
		ScambioSanzioneSqlDAO lScaSanSql = null;
		NotificaEventoSqlDAO lNotEveSqlDao = null;

		Vector lTenori = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("---------------------------------------- ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("appendStatoEsecuzioneXX ");

			// Instanzazione dei DAO
			lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
			lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
			lAnnoSqlDao = new AnnotazioneManualeSqlDAO(lConn);
			lEveDaoStampa = new EventoPerStampaSqlDAO(lConn);
			lDepOrdSql = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDecSql = new DepositoDecretoSqlDAO(lConn);
			lTenSql = new TenoreSqlDAO(lConn);
			lScaSanSql = new ScambioSanzioneSqlDAO(lConn);

			// Ricerca Eventi per lo stato esecuzione
			lEveDaoStampa.ricercaEventoByFascicoloSiepXStampa(lKeyFascicolo);
			Vector lVectEve = new Vector(lEveDaoStampa.getModels());

			// Se esistono degli eventi che devono essere visualizzati in Stampa
			if (lVectEve != null) {

				// ---------------------------------------------------------------
				// Sezione per il caricamento dei dati nelle HASH TABLE
				// in modo da evitare di fare piu' volte accesso alla base dati
				// Carico:
				// Pena Residua
				// Misura Alternativa
				// Annotazione Manuale
				// ---------------------------------------------------------------

				// Ricerco la pena residua legata a tutti gli eventi
				// Che devono essere visualizzati all'interno dello stato di
				// esecuzione
				lPenaResStatDao = new PenaResiduaPerStatoEsecuzioneSqlDAO(lConn);
				lPenaResStatDao.ricercaPenaResiduaPerStatoEsecuzione(lKeyFascicolo);
				Vector lPenResi = new Vector(lPenaResStatDao.getModels());
				// Hash table di Pena Residua

				Hashtable lHashPenRes = new Hashtable();
				lPenaResStatDao.stop();

				if (lPenResi != null) {
					Iterator lItxPenRes = lPenResi.iterator();

					while (lItxPenRes.hasNext()) {
						PenaResiduaModel lPenRes = (PenaResiduaModel) lItxPenRes.next();
						lPenRes.calcolaStringaReclusione();
						lPenRes.calcolaStringaArresto();
						lPenRes.calcolaStringaIsolamento();
						// Carico tutte le pene residue all'interno di una hash table
						lHashPenRes.put(lPenRes.getEveIdEvento(), lPenRes);
					}
				}
				// Fine Hash table di Pena Residua

				// Ricerco tutte le Misure alternative che fanno riferimento
				// al fascicolo corrente
				lMisAltStatDAO = new MisuraAlternativaPerStatoEsecuzioneSqlDAO(lConn);
				lMisAltStatDAO.ricercaMisuraAlternativaPerStatoEsecuzione(lKeyFascicolo);
				Vector lMisureAlti = new Vector(lMisAltStatDAO.getModels());
				Hashtable lHashMisAlt = new Hashtable();
				if (lMisureAlti != null) {
					Iterator lItxMisAlt = lMisureAlti.iterator();

					while (lItxMisAlt.hasNext()) {
						// Carico tutte el MA all'interno
						// di una hash table
						MisuraAlternativaModel lMisuraAlternativa = (MisuraAlternativaModel) lItxMisAlt
								.next();
						lMisuraAlternativa.calcolaStringaRevocaArresto();
						lMisuraAlternativa.calcolaStringaRevocaReclusione();
						lMisuraAlternativa.calcolaStringaMisura();
						lHashMisAlt.put(lMisuraAlternativa.getEveIdEvento(), lMisuraAlternativa);
					}
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("icerco tutte lo scambio sanzione ");

				// Ricerco tutte lo scambio sanzione
				// al fascicolo corrente
				lScaSanSql.ricercaScambioSanzionePerStatoEsecuzione(lKeyFascicolo);
				Vector lScaSons = new Vector(lScaSanSql.getModels());
				Hashtable lHashScaSons = new Hashtable();
				if (lScaSons != null) {
					Iterator lItxScaSon = lScaSons.iterator();

					while (lItxScaSon.hasNext()) {
						// di una hash table
						ScambioSanzioneModel lScambioModel = (ScambioSanzioneModel) lItxScaSon.next();
						lHashScaSons.put(lScambioModel.getEveIdEvento(), lScambioModel);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
						siesLogger.debug("lScambioModel = " + lScambioModel);
					}
				}

				// Ricerco tutte le Annmotazioni MAnuali che fanno riferimento
				// al fascicolo corrente
				lAnnoSqlDao = new AnnotazioneManualeSqlDAO(lConn);
				lAnnoSqlDao.ricercaAnnotazioneManualeByIdFascicoloPerStatoEsecuzione(lKeyFascicolo);
				Vector lAnnotazioni = new Vector(lAnnoSqlDao.getModels());
				// Hast table annotazioni manuali
				Hashtable lHashAnnotazioni = new Hashtable();
				if (lAnnotazioni != null && lAnnotazioni.size() > 0) {
					Iterator lItxAnno = lAnnotazioni.iterator();
					Vector lAnnotazioniTemp = null;
					BigDecimal lIdEvento = new BigDecimal(0);
					AnnotazioneManualeModel lAnnoModel = null;
					while (lItxAnno.hasNext()) {
						lAnnoModel = (AnnotazioneManualeModel) lItxAnno.next();
						// Sono nel caso del primo giro
						if (lIdEvento.equals(new BigDecimal(0))) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.debug("New Vector for " + lAnnoModel.getEveIdEvento());
							lAnnotazioniTemp = new Vector();
							lIdEvento = lAnnoModel.getEveIdEvento();
						}
						// Carico tutte le Annotazioni all'interno
						// di una hash table
						lAnnoModel.calcolaStringaArresto();
						lAnnoModel.calcolaStringaReclusione();

						if (lAnnoModel != null && lIdEvento != null && lAnnoModel.getEveIdEvento() != null
								&& lIdEvento.compareTo(lAnnoModel.getEveIdEvento()) != 0) {
							lHashAnnotazioni.put(lIdEvento, lAnnotazioniTemp);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.debug("Put in HASH ---> ID EVENTO = " + lIdEvento);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.debug("New Vector for " + lAnnoModel.getEveIdEvento());
							lAnnotazioniTemp = new Vector();
							lAnnotazioniTemp.add(lAnnoModel);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.debug("Put in Vector --->" + lAnnoModel.getIdAnnotazioneManuale()
									+ " - ID EVENTO = " + lAnnoModel.getEveIdEvento());
						} else {
							lAnnotazioniTemp.add(lAnnoModel);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.debug("Put in Vector --->" + lAnnoModel.getIdAnnotazioneManuale()
									+ " - ID EVENTO = " + lAnnoModel.getEveIdEvento());
						}
						lIdEvento = lAnnoModel.getEveIdEvento();
					}
					if (lAnnotazioniTemp != null) {
						lHashAnnotazioni.put(lIdEvento, lAnnotazioniTemp);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
						siesLogger.debug("Put Last HASH --->" + lAnnoModel.getIdAnnotazioneManuale()
								+ " - ID EVENTO = " + lAnnoModel.getEveIdEvento());
					}
				}
				// --------------------------------------------------------------------
				// FINE SEZIONE caricamento hash table
				// --------------------------------------------------------------------

				/*
				 * Ciclo su tutti gli eventi associati al fascicolo e visualizzabili in stampa
				 */
				Iterator lItx = lVectEve.iterator();
				while (lItx.hasNext()) {
					EventoModel lEveNot = (EventoModel) lItx.next();
					lEveNot.setEventoCorrente("N");
					lEveNot = this.setEventoDiStampa(lEveNot, lConn);

					boolean lCercaAnnotazione = true;

					// Per questo eveto ho gia' cercato l'annotazione manuale
					// Prima di inserire l'Evento sul TreeModel
					// verifico per TOPPA INDULTO
					if (lEveNot.getCodTipoProvvedimento().equals("04")
							&& lEveNot.getCodMotivo().equals("0284")) { // Per questo eveto ho gia' cercato
																		// l'annotazione manuale
																		// Non devo farlo in seguito
						lCercaAnnotazione = false;
						// mi serve la pena residua
						PenaResiduaModel lPenResStatoMod = (PenaResiduaModel) lHashPenRes.get(lEveNot
								.getIdEvento());

						/*
						 * if(lPenResStatoMod.getDataFine().compareTo(lEveNot.getDataEmissione())<0)
						 * //Soggetto scarcerato { this.insReworkIndulto(lAnnoSqlDao, lEveNot);
						 * lEveNot.setDescrTipoProvvedimento(""); lEveNot.setDescrMotivo("");
						 * lEveNot.setData(""); } else //soggetto in espiazione
						 */
						this.insReworkIndulto(lAnnoSqlDao, lEveNot, lPenResStatoMod);
					}

					TreeModel lStatEve = new TreeModel(lEveNot);

					// se il tipo provvedimento è 02 cerco il decreto altrimenti è 03 e cerco il
					// l'ordinanza e il relativo tenore!!
					if (lEveNot != null && lEveNot.getCodTipoProvvedimento() != null) { // Evento tipo decreto
						if (lEveNot.getCodTipoProvvedimento().equals("02")) {
							lDepDecSql.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(lEveNot
									.getIdEvento());
							lDepDecSql.start();
							lDepDecMod = null;
							if (lDepDecSql.next()) {
								lDepDecMod = (DepositoDecretoModel) lDepDecSql.getModelNoDescTipoDecreto();
							}
							lDepDecSql.stop();
							if (lDepDecMod != null) {
								// tenore
								lTenSql.ricercaTenoriByDecretoOrderByPesoNoGenProc(lDepDecMod
										.getIdDepositoDecreto());
								lTenori = new Vector(lTenSql.getModels());
								Iterator iter = lTenori.iterator();
								while (iter.hasNext()) {
									TenoreModel lTenMod = (TenoreModel) iter.next();
									TreeModel lTreeTenori = new TreeModel(lTenMod);
									lStatEve.add(lTreeTenori);
								}
							}
						} else if (lEveNot.getCodTipoProvvedimento().equals("03")) { // Evento ordinanza
							lDepOrdSql.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveNot.getIdEvento());
							lDepOrdPCMod = (DepositoOrdinanzaPcModel) lDepOrdSql.getModelByKey();
							if (lDepOrdPCMod != null) {
								// tenore
								lTenSql.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(lDepOrdPCMod
										.getIdDepositoOrdinanzaPc());
								lTenori = new Vector(lTenSql.getModels());
								Iterator iter = lTenori.iterator();
								while (iter.hasNext()) {
									TenoreModel lTenMod = (TenoreModel) iter.next();
									TreeModel lTreeTenori = new TreeModel(lTenMod);
									lStatEve.add(lTreeTenori);
								}
							}
						}
					}

					// ====================================================================
					// Nel caso di trasmissione atti ho bisogno anche delle notifiche in
					// quanto su alcuni template (SIEP_SS_COM_NRESI_PENA.rtf) va stampata
					// la data di trasmissione (27/06/2008)
					// ====================================================================
					if (lEveNot.getCodTipoEvento() != null
							&& lEveNot.getCodTipoProvvedimento() != null
							&& lEveNot.getCodMotivo() != null
							&& lEveNot.getCodTipoEvento().equals("02")
							&& lEveNot.getCodTipoProvvedimento().equals("31")
							&& (lEveNot.getCodMotivo().equals("0396") || lEveNot.getCodMotivo()
									.equals("0941"))) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						// siesLogger.debug("Ev Trasmissione atti. Cerco Notifiche");
						lNotEveSqlDao = new NotificaEventoSqlDAO(lConn);
						lNotEveSqlDao.ricercaNotificaByEvento(lEveNot.getIdEvento());
						Vector lNotifiche = new Vector(lNotEveSqlDao.getModels());

						Iterator lItxNot = lNotifiche.iterator();
						while (lItxNot.hasNext()) {
							NotificaModel lNotMod = (NotificaModel) lItxNot.next();

							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							// siesLogger.debug("Trovata Notifica: "+lNotMod);

							TreeModel lTreeNotifica = new TreeModel(lNotMod);
							lStatEve.add(lTreeNotifica);

						}
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.debug("");
					}

					aTreeRoot.add(lStatEve);
					/*
					 * // Ufficio proprietario dell'evento UfficioModel lUfficio = new UfficioModel();
					 * lUfficio.setDescrTipoUfficio(lEveNot.getDescrUfficioEmittente());
					 * lUfficio.setDescrComune(lEveNot.getDescrLuogoEmittente()); lStatEve.add(new
					 * TreeModel(lUfficio));
					 */
					// Cerco la Pena Residua sull'hash table
					PenaResiduaModel lPenResStatoMod = (PenaResiduaModel) lHashPenRes.get(lEveNot
							.getIdEvento());

					// Caso Ordinanza Indulto
					if (lEveNot.getCodTipoProvvedimento().equals("03")
							&& lEveNot.getCodMotivo().equals("0284") && lEveNot.getEveIdEvento() != null) { // Caso
																											// soggetto
																											// scarcerato
																											// Per
																											// questo
																											// eveto
																											// ho
																											// gia'
																											// cercato
																											// l'annotazione
																											// manuale
																											// Non
																											// devo
																											// farlo
																											// in
																											// seguito
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
						siesLogger.info(" >>> Ordinanza Concessione INDULTO scarcerato = "
								+ lEveNot.getDescrTipoProvvedimento() + " " + lEveNot.getDescrMotivo());

						lPenResStatoMod = (PenaResiduaModel) lHashPenRes.get(lEveNot.getEveIdEvento());

						if (lPenResStatoMod != null && lPenResStatoMod.getDataFine() != null) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.info(" >>> Data Fine <<<< = " + lPenResStatoMod.getDataFine() + " * * * "
									+ lEveNot.getDataEmissione());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.info("COMPARE = = = "
									+ lPenResStatoMod.getDataFine().compareTo(lEveNot.getDataEmissione()));
							if (lPenResStatoMod.getDataFine().compareTo(lEveNot.getDataEmissione()) < 0) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
								siesLogger.info(" >>> Ordinanza SCARCERATO O O O O O<<<< = "
										+ lEveNot.getDescrTipoProvvedimento() + " "
										+ lEveNot.getDescrMotivo());
								lCercaAnnotazione = false;
								this.insReworkIndultoScarcerato(lAnnoSqlDao, lEveNot, lPenResStatoMod);
							}
							lPenResStatoMod.setDiesAQuo(null);
						}
					}

					// Se la Pena è significativa ( nel senso
					// che i quantum e gli importi sono != 0 )
					// setta a null il campo dies a quo
					// per problemi di compatibilità sui template
					// con i dati migrati da RES
					if (lPenResStatoMod != null && !lPenResStatoMod.isSignificativa())
						lPenResStatoMod.setDiesAQuo(null);

					if (isOrdineScarcerzioneIndulto(lEveNot)) { // Nel caso di OS per Indulto la pena residua
																// è legata al provv Indulto e non
																// all'OS
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
						siesLogger.info(" >>> Ordine Scar INDULTO = " + lEveNot.getDescrTipoProvvedimento() + " "
								+ lEveNot.getDescrMotivo());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
						siesLogger.info(" >>> EveIdEvento = " + lEveNot.getEveIdEvento());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
						siesLogger.info(" >>> IdEvento = " + lEveNot.getIdEvento());
						if (lEveNot.getEveIdEvento() != null)
							lPenResStatoMod = (PenaResiduaModel) lHashPenRes.get(lEveNot.getEveIdEvento());

						String lDescrMotivo = " a seguito di concessione indulto emesso in data "
								+ DateUtils.getDateToString(lEveNot.getDataEmissione(), "dd-MM-yyyy");

						if (lPenResStatoMod != null) {

							lPenResStatoMod.setDiesAQuo("S");
							// Soggetto Scarcerato
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.info(" >>> Ordine Scar Data Fine = "
									+ DateUtils.getDateToString(lPenResStatoMod.getDataFine(), "dd/MM/yyyy"));
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.info(" >>> Ordine Scar Data Emissione = "
									+ DateUtils.getDateToString(lEveNot.getDataEmissione(), "dd/MM/yyyy"));

							if (lPenResStatoMod.getDataFine() != null)
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
								siesLogger.info(" >>> Ordine Scar Compare = "
										+ lPenResStatoMod.getDataFine().compareTo(lEveNot.getDataEmissione()));

							if (lPenResStatoMod.getDataFine() != null
									&& lPenResStatoMod.getDataFine().compareTo(lEveNot.getDataEmissione()) < 0) { // Cambia
																													// la
																													// dicitura
																													// per
																													// l'OS
																													// di
																													// soggetto
																													// scarcerato
																													// data
																													// fine
																													// pena
																													// <
																													// data
																													// provvedimento
								lDescrMotivo += ". Scarcerazione avvenuta in data "
										+ DateUtils.getDateToString(lPenResStatoMod.getDataFine(),
												"dd-MM-yyyy");
							}
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
							siesLogger.info(" >>> >>> DESCR OS INDULTO = " + lEveNot.getDescrTipoProvvedimento()
									+ " " + lEveNot.getDescrMotivo());
							lEveNot.setDescrMotivo(lDescrMotivo);
						}
					}

					// Ufficio proprietario dell'evento
					UfficioModel lUfficio = new UfficioModel();
					lUfficio.setDescrTipoUfficio(lEveNot.getDescrUfficioEmittente());
					lUfficio.setDescrComune(lEveNot.getDescrLuogoEmittente());
					lStatEve.add(new TreeModel(lUfficio));

					// Cerco la Misura Alternativa sull'hash table
					// modifica dario 05-05-2007
					// MisuraAlternativaModel lMisuraAlternativa = (MisuraAlternativaModel)
					// lHashMisAlt.get(lEveNot.getIdEvento());
					MisuraAlternativaModel lMisuraAlternativa = new MisuraAlternativaModel();
					if (lEveNot.getEveIdEvento() != null)
						lMisuraAlternativa = (MisuraAlternativaModel) lHashMisAlt.get(lEveNot
								.getEveIdEvento());
					else
						lMisuraAlternativa = (MisuraAlternativaModel) lHashMisAlt.get(lEveNot.getIdEvento());

					// scambio sanzione

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
					siesLogger.debug("lScambioModel =");

					ScambioSanzioneModel lScaSanMod = new ScambioSanzioneModel();
					if (lEveNot != null && lEveNot.getEveIdEvento() != null)
						lScaSanMod = (ScambioSanzioneModel) lHashScaSons.get(lEveNot.getEveIdEvento());
					else if (lEveNot != null && lEveNot.getIdEvento() != null)
						lScaSanMod = (ScambioSanzioneModel) lHashScaSons.get(lEveNot.getIdEvento());

					if (lScaSanMod != null && lScaSanMod.getIdScambioSanzione() != null)
						lStatEve.add(new TreeModel(lScaSanMod));

					// GDV--- Toppa per l'INDULTO
					// Per eventi di tipo ---> 09-0367; 26-0290; 04-0284
					// La pena residua non deve essere visualizzata
					if (isEventiIndulto(lEveNot) && (lPenResStatoMod != null)) {
						// Me lo copio in un nuovo oggetto per non cambiare quello nell'hash table
						PenaResiduaModel lPenRes = new PenaResiduaModel(lPenResStatoMod);
						lPenRes.setDiesAQuo(null);
						lPenRes.setStringaReclusione(null);
						lPenRes.setStringaArresto(null);
						lPenRes.setImportoMulta(null);
						lPenRes.setImportoAmmenda(null);
						lPenRes.setDataFine(null);
						lPenRes.setDataInizio(null);
						lStatEve.add(new TreeModel(lPenRes));
					} else
						lStatEve.add(new TreeModel(lPenResStatoMod));

					lStatEve.add(new TreeModel(lMisuraAlternativa));

					// Liberazione Anticipata + Periodi Liberazione
					if (this.isEventoConLiberazioneAnticipata(lEveNot))
						this.appendLiberazioneAnticipata(lConn, lEveNot.getIdEvento(), lStatEve);

					// Annotazioni Manuali
					if (lCercaAnnotazione) {
						// Cerco l'annotazione manuale nell'hash table
						// -------------------------------------------------------------------------
						// lAnnoSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveNot.getIdEvento());
						// List lListAnnMod = (ArrayList) lAnnoSqlDao.getModels();
						// -------------------------------------------------------------------------
						Vector lListAnnMod = (Vector) lHashAnnotazioni.get(lEveNot.getIdEvento());

						if (lListAnnMod != null) {
							for (Iterator i = lListAnnMod.iterator(); i.hasNext();) {
								AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) i.next();
								if (lEveNot.getCodTipoProvvedimento() != null
										&& !lEveNot.getCodTipoProvvedimento().equals("03")) {
									if (lAnnMod != null && lAnnMod.getDataReclusioneDa() != null
											&& lAnnMod.getDataReclusioneA() != null) {
										lAnnMod.setBeneficio("N");
									} else {
										lAnnMod.setBeneficio("S");
									}
								}
								lAnnMod.calcolaStringaArresto();
								lAnnMod.calcolaStringaReclusione();

								lStatEve.add(new TreeModel(lAnnMod));
							}
						}
					}
				}
			}

			lEveDaoStampa.stop();
			lPenaResStatDao.stop();
			lMisAltStatDAO.stop();
			lLibDAO.stop();
			lPerDao.stop();
			lAnnoSqlDao.stop();
			lDepOrdSql.stop();
			lDepDecSql.stop();
			lTenSql.stop();
			lScaSanSql.stop();

			if (lNotEveSqlDao != null) {
				lNotEveSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.appendStatoEsecuzione: ", daoEx);
			throw new F3BException("StampaEventoUtils.appendStatoEsecuzione: " + daoEx);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.appendStatoEsecuzione: ", sqe);
			throw new F3BException("StampaEventoUtils.appendStatoEsecuzione: " + sqe);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.appendStatoEsecuzione: ", sqe);
			throw new F3BException("StampaEventoUtils.appendStatoEsecuzione: " + sqe);
		}

		finally {
			cleanup(lEveDaoStampa);
			cleanup(lPenaResStatDao);
			cleanup(lMisAltStatDAO);
			cleanup(lLibDAO);
			cleanup(lPerDao);
			cleanup(lAnnoSqlDao);
			cleanup(lDepOrdSql);
			cleanup(lDepDecSql);
			cleanup(lTenSql);
			cleanup(lScaSanSql);
			cleanup(lNotEveSqlDao);
		}
	}

	/**
	 * Aggiunge le NOtifiche per l'evento passato al TreeModel
	 * 
	 * @param aEveTree
	 *            Nodo su cui aggiungere le notifiche
	 * @param aEveModel
	 *            l'Evento da cui prendere i dati
	 * @throws F3BException
	 */
	public void appendNotifiche(TreeModel aEveTree, EventoNotificaModel aEveModel, Vector lAvvocati,
			UtenteModel aUtenteModel) throws F3BException {
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		Connection lConn = null;

		try {
			int count = 0;
			lConn = getDBConnection();

			if (lAvvocati == null) {
				lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
				lAvvDao.ricercaAvvocatiByFascicolo(aEveModel.getEvento().getFasSieIdFascicoloSiep());
				lAvvocati = new Vector(lAvvDao.getModels());
			}

			while (count < aEveModel.getNotifiche().length) {
				TreeModel lTreeNot = new TreeModel(aEveModel.getNotifiche()[count]);
				aEveTree.add(lTreeNot);

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
				} else
					lTreeNot.add(new TreeModel(aEveModel.getNotifiche()[count].getAutoritaEsterna()));

				BigDecimal lIdAvv = aEveModel.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep();

				if ((lAvvocati != null) && (lIdAvv != null)) {
					Iterator lItx = lAvvocati.iterator();
					while (lItx.hasNext()) {
						AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItx.next();
						if (lIdAvv.compareTo(lAvv.getAvvocatoFascicoloSiepModel()
								.getIdAvvocatoFascicoloSiep()) == 0)
							lTreeNot.add(new TreeModel(lAvv.getAvvocato()));
					}
				}

				if (aEveModel.getNotifiche()[count].getUffCodUfficio() != null) {
					UfficioModel lUfficio = UfficioUtils
							.getUfficioByCodUfficio((aEveModel.getNotifiche()[count].getUffCodUfficio()));

					// C.S. 08/10/2015 richiesta di Michele
					// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso
					// il Tribunale per minorenni" in "Magistrato di Sorveglianza per i minorenni"
					if (aUtenteModel != null
							&& aUtenteModel.getUfficioUtente() != null
							&& aUtenteModel.getUfficioUtente().getCodTipoUfficio() != null
							&& !aUtenteModel.getUfficioUtente().getCodTipoUfficio().equals("")
							&& (aUtenteModel.getUfficioUtente().getCodTipoUfficio().equals("PM")
									|| aUtenteModel.getUfficioUtente().getCodTipoUfficio().equals("PMM") || aUtenteModel
									.getUfficioUtente().getCodTipoUfficio().equals("PGCAP"))) {
						if (lUfficio.getCodTipoUfficio() != null && !lUfficio.getCodTipoUfficio().equals("")
								&& lUfficio.getCodTipoUfficio().equals("UDSM")) {
							lUfficio.setDescrTipoUfficio("Magistrato di Sorveglianza per i Minorenni");
						}
					}

					lTreeNot.add(new TreeModel(lUfficio));
				}

				if (aEveModel.getNotifiche()[count].getCssIdCssa() != null)
					lTreeNot.add(new TreeModel(aEveModel.getNotifiche()[count].getCSSA()));

				// Fine Aggiunta
				count++;
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.appendNotifiche: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
	}

	/**
	 * Set dicitura per lo stato di esecuzione
	 * 
	 * @param aEvento
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public EventoModel setEventoDiStampa(EventoModel aEvento, Connection lConn) throws F3BException {
		EventoModel mEvento = aEvento;
		SospensioneSqlDAO lSospSql = null;
		ArchiviazioneSqlDAO lArchSql = null;
		DecretoOrdinanzaSiepSqlDAO lDeOrSql = null;
		try {
			// STUB 28/02/2006 In alcuni casi ( Stampa modelli Ordinanze SIUS)il Tipo Provvedimento non è
			// valorizzato.
			if (mEvento.getCodTipoProvvedimento() != null) {

				// Interruzione - Avvemuto decesso oppure avvenuta evasione
				if (mEvento.getCodTipoProvvedimento().compareTo("12") == 0
						&& ((mEvento.getCodMotivo().compareTo("0266") == 0) || (mEvento.getCodMotivo()
								.compareTo("0267") == 0))) {
					mEvento.setDescrizioneData("in data");
					mEvento.setData(mEvento.getDataEmissione());
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
					// Lettura della Sospensione.
					lSospSql = new SospensioneSqlDAO(lConn);
					lSospSql.ricercaSospensioneByFascicolo(mEvento.getFasSieIdFascicoloSiep());
					SospensioneModel lSospMod = (SospensioneModel) lSospSql.getModelByKey();
					if (lSospMod != null)
						mEvento.setData(lSospMod.getDataInizio());
				} else if (mEvento.getCodTipoProvvedimento().compareTo("28") == 0) { // Comunicazione
																						// trasmissione
																						// istanza al TdS
					mEvento.setDescrizioneData("in data");
					mEvento.setData(mEvento.getDataEmissione());
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
				} else if (mEvento.getCodTipoProvvedimento().compareTo("02") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("03") == 0) { // Decreto Ordinanza
					if (mEvento.getCodTipoProvvedimento().compareTo("03") == 0)
						mEvento.setDescrizioneData("emessa in data");
					else
						mEvento.setDescrizioneData("emesso in data");

					mEvento.setData(mEvento.getDataEmissione());
					mEvento.setCodUfficioEmittente(aEvento.getCodUfficioEmittente());
					mEvento.setDescrUfficioEmittente(aEvento.getDescrUfficioEmittente());
				} else if (mEvento.getCodTipoProvvedimento().compareTo("16") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("17") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("18") == 0) { // Verbale
					mEvento.setDescrizioneData("redatto in data");
					// Lettura del Verbale.
					mEvento.setData(mEvento.getDataEmissione());
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
				} else if ((mEvento.getCodTipoProvvedimento().compareTo("25") == 0 || (mEvento
						.getCodTipoProvvedimento().compareTo("04") == 0))
						&& (mEvento.getCodMotivo().compareTo("0270") == 0)) { // Provvedimento Annotazione
																				// interruzione della
																				// esecuzione della pena
					mEvento.setDescrizioneData("in data");
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
					// Lettura della Sospensione.
					lSospSql = new SospensioneSqlDAO(lConn);
					lSospSql.ricercaSospensioneByFascicolo(mEvento.getFasSieIdFascicoloSiep());
					SospensioneModel lSospMod = (SospensioneModel) lSospSql.getModelByKey();
					if (lSospMod != null)
						mEvento.setData(lSospMod.getDataInizio());
				} else if ((mEvento.getCodTipoProvvedimento().compareTo("25") == 0 || (mEvento
						.getCodTipoProvvedimento().compareTo("04") == 0))
						&& (mEvento.getCodMotivo().compareTo("0268") == 0 || mEvento.getCodMotivo()
								.compareTo("0269") == 0)) {
					// Interruzione - consegna temporanea art. 709 comma 1c.p.p.
					// Interruzione - esecuzione penale all'estero della condanna ex art. 742 c.p.p.
					mEvento.setDescrizioneData("in data");
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
					// Lettura del Decreto_Ordinanza_Siep.
					lDeOrSql = new DecretoOrdinanzaSiepSqlDAO(lConn);
					lDeOrSql.ricercaDecretoOrdinanzaSiepByIdEvento(mEvento.getIdEvento());
					DecretoOrdinanzaSiepModel lDeOrMod = (DecretoOrdinanzaSiepModel) lDeOrSql.getModelByKey();
					if (lDeOrMod != null)
						mEvento.setData(lDeOrMod.getDataInterruzionePena());
				} else if (mEvento.getCodTipoProvvedimento().compareTo("25") == 0) { // Annotazione
					mEvento.setDescrizioneData("in data");
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
					// Lettura della Archiviazione.
					lArchSql = new ArchiviazioneSqlDAO(lConn);
					lArchSql.ricercaArchiviazioneByIdEvento(mEvento.getIdEvento());
					ArchiviazioneModel lArchMod = (ArchiviazioneModel) lArchSql.getModelByKey();
					if (lArchMod != null)
						mEvento.setData(lArchMod.getDataDefinizione());
					else {
						// Lettura della Sospensione.
						lSospSql = new SospensioneSqlDAO(lConn);
						lSospSql.ricercaSospensioneByFascicolo(mEvento.getFasSieIdFascicoloSiep());
						SospensioneModel lSospMod = (SospensioneModel) lSospSql.getModelByKey();
						if (lSospMod != null)
							mEvento.setData(lSospMod.getDataInizio());
					}
				} else if (mEvento.getCodTipoProvvedimento().compareTo("27") == 0) {
					if (mEvento.getCodMotivo().compareTo("0276") == 0) { // Verbale di Espulsione
						mEvento.setDescrizioneData("redatto in data");
						mEvento.setCodUfficioEmittente(null);
						mEvento.setDescrUfficioEmittente(null);
						// Lettura della Sospensione.
						lSospSql = new SospensioneSqlDAO(lConn);
						lSospSql.ricercaSospensioneByFascicolo(mEvento.getFasSieIdFascicoloSiep());
						SospensioneModel lSospMod = (SospensioneModel) lSospSql.getModelByKey();
						if (lSospMod != null)
							mEvento.setData(lSospMod.getDataInizio());
					}
				} else if (mEvento.getCodTipoProvvedimento().compareTo("06") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("04") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("09") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("26") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("12") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("11") == 0
						|| mEvento.getCodTipoProvvedimento().compareTo("24") == 0) {
					mEvento.setDescrizioneData("emesso in data");
					mEvento.setData(mEvento.getDataEmissione());
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
				}

				// Toppa INDULTO
				if (mEvento.getCodTipoProvvedimento().equals("04") && mEvento.getCodMotivo().equals("0284")) {
					mEvento.setDescrizioneData("");
					mEvento.setData(null);
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
				}

				// Toppa per l'Ordine si Scarcerazione INDULTO
				if (this.isOrdineScarcerzioneIndulto(mEvento)) {
					mEvento.setDescrizioneData("");
					mEvento.setData(null);
					mEvento.setCodUfficioEmittente(null);
					mEvento.setDescrUfficioEmittente(null);
				}

			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Attenzione!! Metodo setEventoDiStampa Non Eseguito su IdEvento : "
						+ mEvento.getIdEvento() + "   TipoProvv. = " + mEvento.getCodTipoProvvedimento());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.setEventoDiStampa: " + daoEx, daoEx);
			throw new F3BException("StampaEventoUtils.setEventoDiStampa: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.setEventoDiStampa: " + sqe, sqe);
			throw new F3BException("StampaEventoUtils.setEventoDiStampa: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lSospSql);
			cleanup(lArchSql);
			cleanup(lDeOrSql);
		}
		return mEvento;
	}

	/**
	 * append Liberazione Anticipata all'evento di uno stato esecuzione
	 * 
	 * @param lConn
	 * @param IdEvento
	 * @param lStatEve
	 * @throws F3BException
	 */
	private void appendLiberazioneAnticipata(Connection lConn, BigDecimal IdEvento, TreeModel lStatEve)
			throws F3BException {
		LicenzaLibanticipataSqlDAO lLibDAO = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;

		try {
			lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
			lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
			// Liberazione Anticipata + Periodi Liberazione
			lLibDAO.ricercaLicenzaLibanticipataByEve(IdEvento);

			List lLibVect = new Vector(lLibDAO.getModels());

			int lTotGiorni = 0;
			for (int i = 0; i < lLibVect.size(); i++) {
				LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibVect.get(i);

				if (lLibAnt != null && lLibAnt.getFlagConcesso() != null
						&& lLibAnt.getFlagConcesso().equals("C"))
				// && (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N") ||
				// lLibAnt.getFlagElaborato().equals("E")))
				{
					if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
						lTotGiorni += lLibAnt.getNumeroGiorni().intValue();

						TreeModel lTreeLibAnt = new TreeModel(lLibAnt);
						lStatEve.add(lTreeLibAnt);

						// Ricerca dei Periodi relativi alla licenza
						lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
						List lPeriodi = new ArrayList(lPerDao.getModels());

						Iterator lIterPeriodi = lPeriodi.iterator();
						while (lIterPeriodi.hasNext()) {
							PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi
									.next();
							lTreeLibAnt.add(new TreeModel(lPeriodoModel));
						}
					}
				}
			}

			// Calcola il totale dei giorni di Licenza Anticipata
			if (lTotGiorni != 0) {
				TotalePeriodoModel lTotLibAnt = new TotalePeriodoModel(0, 0, lTotGiorni);
				TreeModel lTreeTotLibAnt = new TreeModel(lTotLibAnt);
				lStatEve.add(lTreeTotLibAnt);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.appendLiberazioneAnticipata: " + daoEx, daoEx);
			throw new F3BException("StampaEventoUtils.appendLiberazioneAnticipata: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StampaEventoUtils.appendLiberazioneAnticipata: " + sqe, sqe);
			throw new F3BException("StampaEventoUtils.appendLiberazioneAnticipata: Eccezione Generica: "
					+ sqe);
		} finally {
			cleanup(lLibDAO);
			cleanup(lPerDao);
		}
	}

	/**
	 * Questo metodo controlla se è l'evento corrente puo' avere la Liberazione Anticipata
	 * 
	 * @param lEve
	 * @return
	 */
	private boolean isEventoConLiberazioneAnticipata(EventoModel lEve) {
		if (lEve != null && lEve.getCodTipoEvento() != null && lEve.getCodTipoProvvedimento() != null
				&& lEve.getCodMotivo() != null) {
			boolean aRet1 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("03") && lEve.getCodMotivo().equals("2130");
			boolean aRet4 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("03") && lEve.getCodMotivo().equals("0076");
			boolean aRet2 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("09") && lEve.getCodMotivo().equals("0081");
			boolean aRet3 = lEve.getCodTipoEvento().equals("01")
					&& lEve.getCodTipoProvvedimento().equals("09") && lEve.getCodMotivo().equals("0083");

			return aRet1 || aRet2 || aRet3 || aRet4;
		} else
			return false;
	}

	/**
	 * Questo metodo è di utilità per l'evento corente e non per lo stato di esecuzione aggancia la
	 * liberazione anticipata con tutti i parametri che servono all'evento corrente
	 * 
	 * @param lConn
	 * @param lTreeFasMod
	 * @param lKeyFascicolo
	 * @param aEveModel
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void appendLiberazioneAnticipata(Connection lConn, TreeModel lTreeFasMod,
			BigDecimal lKeyFascicolo, EventoNotificaModel aEveModel) throws DAOException, SQLException {
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("+++ appendLiberazioneAnticipata");
		CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5();
		CalcoloPenaModel lCalcoloModel = null;
		try {
			lCalcoloModel = lCtrlF5.exGetPenaIniziale(lKeyFascicolo, null);
		} catch (F3BException fex) {
			throw new SQLException("");
		}

		LicenzaLibanticipataSqlDAO lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
		lLibDAO.ricercaLicenzaLibanticipataByIDFascicoloSIEPNew(lKeyFascicolo, lCalcoloModel.getDataDal(),
				null);

		// Liberazione Anticipata + Periodi Liberazione
		// LicenzaLibanticipataSqlDAO lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
		// lLibDAO.ricercaLicenzaLibanticipataByIDFascicoloSIEP(lKeyFascicolo, null);
		// lLibDAO.ricercaLicenzaLibanticipataByEve(aEveModel.getEvento().getEveIdEvento());
		PeriodoLibanticipataSqlDAO lPerDao = new PeriodoLibanticipataSqlDAO(lConn);

		List lLibVect = new Vector(lLibDAO.getModels());
		int lTotGiorniConcessiEvento = 0;
		int lTotaleGiorniDaConcedere = 0;
		int lTotaleGiorniConcessi = 0;
		for (int i = 0; i < lLibVect.size(); i++) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibVect.get(i);

			// AGGIUNGE AL TREE MODEL TUTTI I TIPI DI LA LEGATE ALL'EVENTO
			if (lLibAnt != null
					&& lLibAnt.getFlagConcesso() != null
					&& (lLibAnt.getEveIdEvento() != null && lLibAnt.getEveIdEvento().equals(
							aEveModel.getEvento().getEveIdEvento()))) {
				TreeModel lTreeLibAnt = new TreeModel(lLibAnt);
				lTreeFasMod.add(lTreeLibAnt);

				// Ricerca dei Periodi relativi alla licenza
				lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
				List lPeriodi = new ArrayList(lPerDao.getModels());

				Iterator lIterPeriodi = lPeriodi.iterator();
				while (lIterPeriodi.hasNext()) {
					PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi.next();
					lTreeLibAnt.add(new TreeModel(lPeriodoModel));
				}
			}

			// CONTA I GIORNI CONCESSI LEGATI ALL'EVENTO
			if (lLibAnt != null
					&& lLibAnt.getFlagConcesso() != null
					&& lLibAnt.getFlagConcesso().equals("C")
					&& (lLibAnt.getEveIdEvento() != null && lLibAnt.getEveIdEvento().equals(
							aEveModel.getEvento().getEveIdEvento()))) {
				if (lLibAnt.getNumeroGiorni() != null
						&& lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
					if (aEveModel.getEvento() != null
							&& aEveModel.getEvento().getCodMotivo() != null
							&& (aEveModel.getEvento().getCodMotivo().equals("0922") || aEveModel.getEvento()
									.getCodMotivo().equals("0923"))) {
						lTotGiorniConcessiEvento += lLibAnt.getNumeroGiorni().intValue();
					} else if (lLibAnt.getFlagElaborato() != null && lLibAnt.getFlagElaborato().equals("E")) {
						lTotGiorniConcessiEvento += lLibAnt.getNumeroGiorni().intValue();
					}
				}
			} // CONTA I GIORNI DA CONCERE IN ASSOLUTO LEGATI AL FASCICOLO
			else if (lLibAnt != null && lLibAnt.getFlagConcesso() != null
					&& lLibAnt.getFlagConcesso().equals("C")
					&& (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N"))) {
				if (lLibAnt.getNumeroGiorni() != null
						&& lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
					lTotaleGiorniDaConcedere += lLibAnt.getNumeroGiorni().intValue();
				}
			} // CONTA I GIORNI CONCESSI IN ASSOLUTO LEGATI AL FASCICOLO
			else if (lLibAnt != null && lLibAnt.getFlagConcesso() != null
					&& lLibAnt.getFlagConcesso().equals("C")
					&& (lLibAnt.getFlagElaborato() != null && lLibAnt.getFlagElaborato().equals("S"))) {
				if (lLibAnt.getNumeroGiorni() != null
						&& lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
					lTotaleGiorniConcessi += lLibAnt.getNumeroGiorni().intValue();
				}
			}
		}

		// Calcola il totale dei giorni di Licenza Anticipata concessi associati all'Evento
		TotalePeriodoModel lTotLibAnt = new TotalePeriodoModel(0, 0, lTotGiorniConcessiEvento);

		// Calcola il totale dei giorni di Licenza Anticipata concessi associati all'Evento
		TotalePeriodoLicenzaAnticipataModel lTotLibAntConc = new TotalePeriodoLicenzaAnticipataModel();
		lTotLibAntConc.setTotaleGiorniDaConcedere(lTotaleGiorniDaConcedere);
		lTotLibAntConc.setTotaleGiorniConcessi(lTotaleGiorniConcessi);

		TreeModel lTreeTotLibAnt = new TreeModel(lTotLibAnt);

		if (lTotLibAnt.isSignificativa()) {
			lTreeFasMod.add(lTreeTotLibAnt);
		}

		if (lTotLibAntConc.getTotaleGiorniDaConcedere() != 0 || lTotLibAntConc.getTotaleGiorniConcessi() != 0) {
			lTreeFasMod.add(new TreeModel(lTotLibAntConc));
		}
	}

	/**
	 * 10-09-2014 Siep_MA_Affi_DS - 'COMUNICAZIONE CONCESSIONE AFFIDAMENTO IN PROVA' (01 12 0226) // NON
	 * AUTORIZZATO - non deve andare in produzione Questo metodo è di utilità nella stampa di determinati
	 * provvedimenti, nel caso in cui deve essere visualizzato il totale giorni di L.A. concessi (nell'XML è
	 * inserito il totale nel ramo FASCICOLO, non legati ad un evento)
	 * 
	 * @param lConn
	 * @param lTreeFasMod
	 * @param lKeyFascicolo
	 * @param aEveModel
	 * @throws DAOException
	 * @throws SQLException
	 */

	public void appendLiberazioneAnticipataPerFascicolo(Connection lConn, TreeModel lTreeFasMod,
			BigDecimal lKeyFascicolo, EventoNotificaModel aEveModel) throws DAOException, SQLException {
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("+++ appendLiberazioneAnticipataPerFASCICOLO");

		CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5();
		// CalcoloPenaModel lCalcoloModel = null;
		try {
			/* lCalcoloModel = */lCtrlF5.exGetPenaIniziale(lKeyFascicolo, null);
		} catch (F3BException fex) {
			throw new SQLException("");
		}

		LicenzaLibanticipataSqlDAO lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
		lLibDAO.ricercaLicenzaLibanticipataByIdFascicoloSiepTutti(lKeyFascicolo);

		PeriodoLibanticipataSqlDAO lPerDao = new PeriodoLibanticipataSqlDAO(lConn);

		List lLibVect = new Vector(lLibDAO.getModels());
		int lTotaleGiorniDaConcedere = 0;
		int lTotaleGiorniConcessi = 0;
		int lTotaleGiorniScomputo = 0;
		int lTotaleDifferenza = 0;

		for (int i = 0; i < lLibVect.size(); i++) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibVect.get(i);

			// AGGIUNGE AL TREE MODEL TUTTI I TIPI DI LA LEGATE ALL'EVENTO
			//
			if (lLibAnt != null && lLibAnt.getIdLicenzaLibanticipata() != null) {
				TreeModel lTreeLibAnt = new TreeModel(lLibAnt);
				lTreeFasMod.add(lTreeLibAnt);

				// Ricerca dei Periodi relativi alla licenza
				lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
				List lPeriodi = new ArrayList(lPerDao.getModels());

				Iterator lIterPeriodi = lPeriodi.iterator();
				while (lIterPeriodi.hasNext()) {
					PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi.next();
					lTreeLibAnt.add(new TreeModel(lPeriodoModel));
				}

			}

			// 10-09-2014 - CONTA I GIORNI CONCESSI (LEGATI AL FASCICOLO A PRESCINDERE DA FLAG_ELABORATO)
			if (lLibAnt != null && lLibAnt.getFlagConcesso() != null) {
				if (lLibAnt.getFlagConcesso().equals("C")) {
					if (lLibAnt.getNumeroGiorni() != null
							&& lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
						lTotaleGiorniConcessi += lLibAnt.getNumeroGiorni().intValue();
					}
				} else if (lLibAnt.getFlagConcesso().equals("S")) {
					if (lLibAnt.getNumeroGiorni() != null
							&& lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
						lTotaleGiorniScomputo += lLibAnt.getNumeroGiorni().intValue();
					}
				}
			}
		} // Chiude ciclo for

		// --
		lTotaleDifferenza = lTotaleGiorniConcessi - lTotaleGiorniScomputo;
		// --

		// Calcola il totale dei giorni di Licenza Anticipata concessi associati all'Evento
		TotalePeriodoModel lTotLibAnt = new TotalePeriodoModel(0, 0, lTotaleDifferenza);

		// Calcola il totale dei giorni di Licenza Anticipata concessi associati all'Evento
		TotalePeriodoLicenzaAnticipataModel lTotLibAntConc = new TotalePeriodoLicenzaAnticipataModel();
		lTotLibAntConc.setTotaleGiorniDaConcedere(lTotaleGiorniDaConcedere);
		lTotLibAntConc.setTotaleGiorniConcessi(lTotaleDifferenza);

		TreeModel lTreeTotLibAnt = new TreeModel(lTotLibAnt);

		if (lTotLibAnt.isSignificativa()) {
			lTreeFasMod.add(lTreeTotLibAnt);
		}

		if (lTotLibAntConc.getTotaleGiorniDaConcedere() != 0 || lTotLibAntConc.getTotaleGiorniConcessi() != 0) {
			lTreeFasMod.add(new TreeModel(lTotLibAntConc));
		}
	}

	/**
	 * Aggiunge al treeModel dell'evento corrente i dati relativi ai rimedi risarcitori memorizzato sulla
	 * tabell LICENZA_LIBANTICIPATA e PERIODI_LIBANTICIPATA
	 * 
	 * @param lConn
	 * @param lTreeFasMod
	 * @param lKeyFascicolo
	 * @param aEveModel
	 * @throws DAOException
	 * @throws SQLException
	 */
	public void appendRimediRisarcitori(Connection lConn, BigDecimal lKeyFascicolo,
			EventoNotificaModel aEveModel, TreeModel aTreeEveMod) throws DAOException, SQLException,
			Exception {
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("appendRimediRisarcitori");

		// MERGE v10: aggiunto codice per recupero informazioni del 
		// Rimedio Risarcitorio al quale è legato un Reclamo Rimedio Risarcitorio

		// Recupero l'EVENTO per il rimedio risarcitorio
		String codTipoUfficioEmittenteRR = null;
		String descrUfficioEmittenteRR = null;
		String descrLuogoEmittenteRR = null;
		BigDecimal annoProvvRR = null;
		BigDecimal numeroProvvRR = null;
		Date dataEmissioneRR = null;		
		
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector<EventoLicenzePeriodiModel> eventiLicenzePeriodiModel = lCtrlLib
				.ExRicercaRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(lKeyFascicolo);
		if (!eventiLicenzePeriodiModel.isEmpty()) {
			EventoLicenzePeriodiModel eventoLicenzePeriodiModel = eventiLicenzePeriodiModel.firstElement();
			BigDecimal idEventoRR = eventoLicenzePeriodiModel.getEvento().getIdEvento();
			// EVENTO SIUS
			IEvento iEvento = SICOLookupRemote.getEventoRemote();
			EventoModel eventoModel = iEvento.ExRicercaEventoByKey(idEventoRR);
			
			codTipoUfficioEmittenteRR = eventoModel.getCodTipoUfficioEmittente();
			descrUfficioEmittenteRR = eventoModel.getDescrUfficioEmittente();
			descrLuogoEmittenteRR = eventoModel.getDescrLuogoEmittente();

			// RICERCA DEPOSITO_DECRETO o DEPOSITO_ORDINANZA_PC dei Rimedi Risarcotori
			if ("02".equals(eventoModel.getCodTipoProvvedimento())) {
				IDepositoDecreto iDepositoDecreto = SIUSLookupRemote.getDepositoDecretoRemote();
				DepositoDecretoModel depositoDecretoModel = iDepositoDecreto
						.ExRicercaDepositoDecretoByEvento(idEventoRR);

				annoProvvRR = depositoDecretoModel.getAnnoS72();
				numeroProvvRR = depositoDecretoModel.getNumS72();
				dataEmissioneRR = depositoDecretoModel.getDataEmissione();
				
			} else if ("03".equals(eventoModel.getCodTipoProvvedimento())) {
				IDepositoOrdinanzaPc iDepositoOrdinanzaPc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel depositoOrdinanzaPcModel = iDepositoOrdinanzaPc
						.ExRicercaDepositoOrdinanzaPcByEvento(idEventoRR);

				annoProvvRR = depositoOrdinanzaPcModel.getAnnoS3();
				numeroProvvRR = depositoOrdinanzaPcModel.getNumS3();
				dataEmissioneRR = depositoOrdinanzaPcModel.getDataCameraConsiglio();
			}
			
		}
		// FINE MERGE v10
		
		// Recupero il provvedimento SIUS (decreto/ordinanza)
		EventoSqlDAO lEveSqlDao = new EventoSqlDAO(lConn);
		lEveSqlDao.ricercaEventoByKey(aEveModel.getEvento().getEveIdEvento());
		EventoModel lEveSorvMod = (EventoModel) lEveSqlDao.getModelByKey();

		TreeModel lTreeModelEveSorv = new TreeModel(lEveSorvMod);
		aTreeEveMod.add(lTreeModelEveSorv);

		//
		// RICERCA DEPOSITO_DECRETO o DEPOSITO_ORDINANZA_PC
		if ("02".equals(lEveSorvMod.getCodTipoProvvedimento())) {
			IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDepDecMod = lCtrlDepDec.ExRicercaDepositoDecretoByEvento(lEveSorvMod
					.getIdEvento());
			TreeModel lTreeModelDepDec = new TreeModel(lDepDecMod);
			lTreeModelEveSorv.add(lTreeModelDepDec);
		} else if ("03".equals(lEveSorvMod.getCodTipoProvvedimento())) {
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveSorvMod
					.getIdEvento());
			TreeModel lTreeModelDepOrdPc = new TreeModel(lDepOrdMod);
			lTreeModelEveSorv.add(lTreeModelDepOrdPc);
		}

		//
		LicenzaLibanticipataSqlDAO lLibSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);
		lLibSqlDAO.ricercaLicenzaLibanticipataByEve(aEveModel.getEvento().getEveIdEvento());

		Vector<LicenzaLibAnticipataModel> lLicenzeVect = new Vector<LicenzaLibAnticipataModel>(
				lLibSqlDAO.getModels());

		PeriodoLibanticipataSqlDAO lPerSqlDao = new PeriodoLibanticipataSqlDAO(lConn);

		for (int i = 0; i < lLicenzeVect.size(); i++) {
			LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLicenzeVect.get(i);

			// MERGE v10: settati parametri relativi al Rimedio Risarcitorio 
			// al quale è legato un Reclamo Rimedio Risarcitorio
			lLibAnt.setAnnoProvvRR(annoProvvRR);
			lLibAnt.setNumeroProvvRR(numeroProvvRR);
			lLibAnt.setCodTipoUfficioEmittenteRR(codTipoUfficioEmittenteRR);
			lLibAnt.setDescrUfficioEmittenteRR(descrUfficioEmittenteRR);
			lLibAnt.setDescrLuogoEmittenteRR(descrLuogoEmittenteRR);
			lLibAnt.setDataEmissioneRR(dataEmissioneRR);
			
			// AGGIUNGE AL TREE MODEL TUTTI I TIPI DI LA LEGATE ALL'EVENTO
			TreeModel lTreeLibAnt = new TreeModel(lLibAnt);
			lTreeModelEveSorv.add(lTreeLibAnt);

			// Ricerca dei Periodi relativi alla licenza
			lPerSqlDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
			List lPeriodi = new ArrayList(lPerSqlDao.getModels());

			Iterator lIterPeriodi = lPeriodi.iterator();
			while (lIterPeriodi.hasNext()) {
				PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi.next();
				lTreeLibAnt.add(new TreeModel(lPeriodoModel));
			}
		}
		

	}

	/**
	 * Metodo che aggiunge all'evento corrente (trasmissione atti x competenza ai fine dell'esecuzione MS),
	 * ulteriori dati da visualizzare sul template di stampa ovvero: -- Dati dell'ordinanza con cui la Sorv
	 * dispone la misura o riforma -- Il tipo di misura disposta dalla SORV e la durata -- L'istituto
	 * designato dal DAP per l'esecuzione della misura
	 * 
	 * @param lConn
	 * @param lKeyFascicolo
	 * @param aEveModel
	 * @param aTreeEveMod
	 * @throws DAOException
	 * @throws SQLException
	 * @throws Exception
	 */
	public void appendDatiTrasmissioneMS(Connection lConn, BigDecimal lKeyFascicolo, TreeModel aTreeEveMod)
			throws DAOException, SQLException, Exception {
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("appendDatiTrasmissioneMS");

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoRicerca = new EventoModel();
		lEventoRicerca.setFasSieIdFascicoloSiep(lKeyFascicolo);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		// siesLogger.debug("Ricerco l'annotazione decisione dellea sorveglianza 01-03-2110");
		EventoModel lEveDecisioneSorv = lCtrl.ExRicercaEventoUnicoTipoProvTipoMot(lEventoRicerca,
				new String[] { "03" }, new String[] { "2110" });

		// Aggiungo i dati all'evento corrente
		// <Evento> Corrente
		// ....
		// <Evento> Ordinanza
		// <MisuraSicurezza>
		// </Evento>
		// </Evento>
		if (lEveDecisioneSorv != null && lEveDecisioneSorv.getIdEvento() != null) {
			TreeModel lTreeModelEveSorv = new TreeModel(lEveDecisioneSorv);
			aTreeEveMod.add(lTreeModelEveSorv);

			// lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			// lMisSqlDao.ricercaMisuraSicurezzaByIdFascicoloOrd(lKeyFascicolo);
			// List lMisure = = new Vector(lMisSqlDao.getModels());

			try {
				// n.b. per ora si suppone che la MS sul fascicolo sia solo UNA o comunque
				// che quella da eseguire sia quella inserita più di recente nel caso
				// in cui la Sorveglianza abbia convertito la misura
				IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				List lListMis = new ArrayList();
				lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lKeyFascicolo);
				if (lListMis != null && lListMis.size() > 0) {
					TreeModel lTreeMisura = new TreeModel((MisuraSicurezzaModel) lListMis.get(0));
					lTreeModelEveSorv.add(lTreeMisura);
				}
			} catch (Exception ex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.warn("Errore in fase di recupero della MS da eseguire", ex);
			}
		}

		// Recupero l'ultimo evento 01-25-1130 Annotazione designazione Istituto
		// Il verbale collegato su cui è presenti l'istituto
		// Aggiungo i dati all'evento corrente
		// <Evento> Corrente
		// ....
		// <Verbale>
		// <IstitutoDetenzione>
		// </Verbale>
		// </Evento>
		EventoVerbaleModel lEveVerMod = new EventoVerbaleModel();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("Ricerca ultimo evento di Annotazione designazione Istituto");
		lEventoRicerca.setFasSieIdFascicoloSiep(lKeyFascicolo);
		lEventoRicerca = lCtrl.ExRicercaEventoUnicoTipoProvTipoMot(lEventoRicerca, new String[] { "25" },
				new String[] { "1130" });

		if (lEventoRicerca != null && lEventoRicerca.getIdEvento() != null) {
			lEveVerMod = lCtrl.ExRicercaEventoVerbaleByIdEve(lEventoRicerca.getIdEvento());

			if (lEveVerMod.getVerbale() != null && lEveVerMod.getVerbale().getIdVerbale() != null) {
				TreeModel lTreeModelVerbale = new TreeModel(lEveVerMod.getVerbale());
				aTreeEveMod.add(lTreeModelVerbale);

				if (lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione() != null) {
					IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
					IstitutoDetenzioneModel lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lEveVerMod
							.getVerbale().getIstDetIdIstitutoDetenzione());

					TreeModel lIstitutoModel = new TreeModel(lIstMod);
					lTreeModelVerbale.add(lIstitutoModel);
				}
			}
		}
	}

	/**
	 * isEventiIndulto restituisce true per eventi di tipo indulto Metodo utilizzato per non far comparire la
	 * pena residua nelle stampe
	 * 
	 * @param aEve
	 *            - evento da testare
	 * @return true se l'evento è un indulto
	 */
	private boolean isEventiIndulto(EventoModel aEve) {
		boolean lRisultato = false;
		if ((aEve.getCodTipoProvvedimento().equals("09") && aEve.getCodMotivo().equals("0367"))
				|| (aEve.getCodTipoProvvedimento().equals("26") && aEve.getCodMotivo().equals("0290"))
				|| (aEve.getCodTipoProvvedimento().equals("04") && aEve.getCodMotivo().equals("0284"))
				|| (aEve.getCodTipoProvvedimento().equals("03") && aEve.getCodMotivo().equals("0284"))

				|| (aEve.getCodTipoProvvedimento().equals("26") && aEve.getCodMotivo().equals("0296"))) {
			lRisultato = true;
		}

		return lRisultato;

	}

	/**
	 * Metodo per comporre la frase dell'ordinanza di tipo indulto nel caso di soggetto scarcerato
	 * 
	 * @param lAnnoSqlDao
	 * @param lEveNot
	 * @param lPenRes
	 * @throws Exception
	 */
	private void insReworkIndultoScarcerato(AnnotazioneManualeSqlDAO lAnnoSqlDao, EventoModel lEveNot,
			PenaResiduaModel lPenRes) throws Exception {
		lAnnoSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveNot.getIdEvento());
		AnnotazioneManualeModel lAnnModIndulto = (AnnotazioneManualeModel) lAnnoSqlDao.getModelByKey();
		lAnnoSqlDao.stop();
		if (lAnnModIndulto != null) {
			String lDescrMotivo = lEveNot.getDescrTipoProvvedimento() + " emessa in data "
					+ DateUtils.getDateToString(lEveNot.getDataEmissione(), "dd-MM-yyyy") + " da "
					+ lEveNot.getDescrUfficioEmittente() + " di " + lEveNot.getDescrLuogoEmittente()
					+ " di Applicazione Indulto " + lAnnModIndulto.getDescrDpr() + " emesso in data "
					+ DateUtils.getDateToString(lEveNot.getDataEmissione(), "dd-MM-yyyy");

			lEveNot.setDescrTipoProvvedimento(lDescrMotivo);

			// Pulisco i dati che non devono apparire in stampa.
			lEveNot.setDescrizioneData("");
			lEveNot.setData(null);
			lEveNot.setCodUfficioEmittente(null);
			lEveNot.setDescrUfficioEmittente(null);
			// lEveNot.getDescrLuogoEmittente(null);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(" >>>>>> DESCR ORDINANZA SCARCERATO = " + lEveNot.getDescrTipoProvvedimento());
		}

	}

	/**
	 * Modifica le diciture per la visualizzazione dei provvedimenti di Amnistia/Indulto inserendo delle
	 * diciture particolari per l'evento
	 * 
	 * @param lAnnoSqlDao
	 * @param lEveNot
	 * @throws Exception
	 */
	private void insReworkIndulto(AnnotazioneManualeSqlDAO lAnnoSqlDao, EventoModel lEveNot,
			PenaResiduaModel lPenRes) throws Exception {

		lAnnoSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveNot.getIdEvento());
		AnnotazioneManualeModel lAnnModIndulto = (AnnotazioneManualeModel) lAnnoSqlDao.getModelByKey();
		lAnnoSqlDao.stop();

		if (lAnnModIndulto == null)
			return;

		String lScrittaScarc = "";

		if (lAnnModIndulto != null
				&& (lPenRes != null && (lPenRes.getDataFine() == null || (lPenRes.getDataFine().compareTo(
						lEveNot.getDataEmissione()) >= 0)))) {
			lAnnModIndulto.calcolaStringaArresto();
			lAnnModIndulto.calcolaStringaReclusione();

			String lScritta = "";
			// soggetto in espiazione
			if (lAnnModIndulto.getFlagPiuMeno() != null) {
				if (lAnnModIndulto.getFlagPiuMeno().equals("-"))
					lScritta = "Concessione ";
				else
					lScritta = "Revoca ";
			} else if (lAnnModIndulto.getFlagConforme() != null) {
				if (lAnnModIndulto.getFlagConforme().equals("R"))
					lScritta = "Rigetto ";
				else if (lAnnModIndulto.getFlagConforme().equals("I"))
					lScritta = "Dichiara Inammissibile ";
			}

			String lDescrProvv = lScritta + lAnnModIndulto.getDescrTipoAnnotazione() + " "
					+ lAnnModIndulto.getDescrDpr();

			if (lScritta.equals("Revoca ")) {
				lDescrProvv += " applicato con Ordinanza nr." + lAnnModIndulto.getAnnoGe() + "/"
						+ lAnnModIndulto.getNumeroGe() + " in data "
						+ DateUtils.getDateToString(lAnnModIndulto.getDataGE(), "dd/MM/yyyy");
			}

			lEveNot.setDescrTipoProvvedimento(lDescrProvv);

			// Tutte e due valorizzate arresto e reclusione

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(" >>> >>> DESCR = " + lEveNot.getDescrTipoProvvedimento() + " "
					+ lEveNot.getDescrMotivo());
		} else { // Soggetto scarcerato
			if (lAnnModIndulto != null && lAnnModIndulto.getFlagPiuMeno() != null) {
				if (lAnnModIndulto.getFlagPiuMeno().equals("-"))
					lScrittaScarc = "Concede ";
				else
					lScrittaScarc = "Revoca ";
			} else if (lAnnModIndulto.getFlagConforme() != null) {
				if (lAnnModIndulto.getFlagConforme().equals("R"))
					lScrittaScarc = "Rigetta ";
				else if (lAnnModIndulto.getFlagConforme().equals("I"))
					lScrittaScarc = "Dichiara Inammissibile ";
			}

			lEveNot.setDescrTipoProvvedimento("");
		}

		// String lMotivo = lScrittaScarc;

		if (lAnnModIndulto != null && lAnnModIndulto.getStringaArresto() != null
				&& lAnnModIndulto.getStringaArresto().length() > 1
				&& lAnnModIndulto.getStringaReclusione() != null
				&& lAnnModIndulto.getStringaReclusione().length() > 1)
			lScrittaScarc = " Per " + lAnnModIndulto.getStringaReclusione() + " e "
					+ lAnnModIndulto.getStringaArresto();

		else {
			if (lAnnModIndulto != null && lAnnModIndulto.getStringaArresto() != null
					&& lAnnModIndulto.getStringaArresto().length() > 1)
				lScrittaScarc = " Per " + lAnnModIndulto.getStringaArresto();
			if (lAnnModIndulto != null && lAnnModIndulto.getStringaReclusione() != null
					&& lAnnModIndulto.getStringaReclusione().length() > 1)
				lScrittaScarc = " Per " + lAnnModIndulto.getStringaReclusione();
		}

		lEveNot.setDescrMotivo(lScrittaScarc);
	}

	/**
	 * metodo per testare se un metodo è un ordine di scarcerazione per Indulto da Decisioni del GE
	 * 
	 * @param aEvento
	 * @return
	 */
	private boolean isOrdineScarcerzioneIndulto(EventoModel aEvento) {
		boolean lReturn = false;

		if (aEvento.getCodTipoProvvedimento().equals("09")
				&& (aEvento.getCodMotivo().equals("0160") || aEvento.getCodMotivo().equals("0158")
						|| aEvento.getCodMotivo().equals("0159") || aEvento.getCodMotivo().equals("0174")
						|| aEvento.getCodMotivo().equals("0175")
				// Ordine di Scarcerazione Provvisorio
				|| aEvento.getCodMotivo().equals("0367")))
			lReturn = true;

		return lReturn;

	}

	/**
	 * Viene passato il verbale trovato per l'evento corrente
	 * 
	 * @param aEveModel
	 * @param lConn
	 * @return TreeModel
	 */
	public TreeModel getVerbaleTree(EventoNotificaModel aEveModel, Connection lConn, String aTipoVerbale)
			throws F3BException {
		VerbaleSqlDAO lVerDAO = null;
		IstitutoDetenzioneSqlDAO lIstDAO = null;

		TreeModel lTreeVerbale = null;
		TreeModel lTreeIstituto = null;
		try {
			lVerDAO = new VerbaleSqlDAO(lConn);
			lIstDAO = new IstitutoDetenzioneSqlDAO(lConn);

			lVerDAO.ricercaVerbaleByCodTipoIdEvento(aEveModel.getEvento().getEveIdEvento(), aTipoVerbale);
			VerbaleModel lVerMod = (VerbaleModel) lVerDAO.getModelByKey();

			if (lVerMod != null) {
				lVerMod.calcolaStringaEspulsione();
				lTreeVerbale = new TreeModel(lVerMod);
			}

			if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null
					&& !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
				lIstDAO.ricercaIstitutoDetenzioneByKey(lVerMod.getIstDetIdIstitutoDetenzione());
				IstitutoDetenzioneModel lIstMod = (IstitutoDetenzioneModel) lIstDAO.getModelByKey();
				lTreeIstituto = new TreeModel(lIstMod);
				lTreeVerbale.add(lTreeIstituto);
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore durante getVerbaleTree" + ex, ex);
			throw new F3BException("Errore durante getVerbaleTree" + ex);
		} finally {
			cleanup(lVerDAO);
			cleanup(lIstDAO);
		}

		return lTreeVerbale;
	}

	// *****************************************************

	Date elaboraDataReato(ReatoModel lReato) {
		Date ret = null;
		if (lReato.getDataInizio() != null) {
			ret = lReato.getDataInizio();
		} else if (lReato.getMeseInizio() != null && lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), lReato.getMeseInizio().intValue(), 1);
		} else if (lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), 1, 1);
		}
		return ret;
	}

	int deltaAnni(Date dataStart, Date dataEnd) {
		int anni = 0;
		if (dataStart != null && dataEnd != null) {
			long lStart = dataStart.getTime();
			long lEnd = dataEnd.getTime();
			long delta = lEnd - lStart;
			long days = Math.round((delta / (1000 * 60 * 60 * 24)));
			anni = Math.round(days / 365);
		}
		return anni;
	}

	Date elaboraDataUltimoReato(Vector reatiVect) {
		// data del primo reato
		Date dataUltimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataUltimoReato == null) {
					dataUltimoReato = dataReato;
				} else if (dataUltimoReato.compareTo(dataReato) < 0) {
					dataUltimoReato = dataReato;
				}
			}
		}

		return dataUltimoReato;
	}

	Date elaboraDataPrimoReato(Vector reatiVect) {
		// data del primo reato
		Date dataPrimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataPrimoReato == null) {
					dataPrimoReato = dataReato;
				} else if (dataPrimoReato.compareTo(dataReato) > 0) {
					dataPrimoReato = dataReato;
				}
			}
		}

		return dataPrimoReato;
	}

	Date elaboraDataNascitaSoggetto(SoggettoModel soggMod) {
		Date ret = null;
		if (soggMod.getDataNascita() != null) {
			ret = soggMod.getDataNascita();
		} else if (soggMod.getMeseNascita() != null && soggMod.getAnnoNascita() != null) {
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), soggMod.getMeseNascita().intValue(),
					1);
		} else if (soggMod.getAnnoNascita() != null) {
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), 1, 1);
		}
		return ret;
	}

	public String checkMinorMagg(Vector reatiVect, SoggettoModel soggMod, FascicoloSiepModel lFascicoloMod,
			SentenzaModel lSentenzaMod) {

		// int anni = 0;
		String statoMinorMagg = "";

		// data di sistema
		Date dataSistema = DateUtils.getSysDate();

		// data di nascita
		Date dataNascitaSoggetto = elaboraDataNascitaSoggetto(soggMod);

		// data del primo reato e ultimo
		Date dataPrimoReato = elaboraDataPrimoReato(reatiVect);
		Date dataUltimoReato = elaboraDataUltimoReato(reatiVect);
		// uguale a false se il soggetto ha 18 anni ed è quindi minorenne
		// uguale a true se il soggetto ha 18 anni ed 1 giorno ed è quindi maggiorenne
		boolean anni_18_Maggiorenne = false;

		if (dataNascitaSoggetto != null) {

			// calcolo gli anni del soggetto
			int anniSoggetto = deltaAnni(dataNascitaSoggetto, dataSistema);
			int anniUltimoReato = deltaAnni(dataNascitaSoggetto, dataUltimoReato);
			statoMinorMagg = impostaEtichetta(anniSoggetto, anniUltimoReato, lFascicoloMod, lSentenzaMod,
					anni_18_Maggiorenne);
			// anni = anniSoggetto;

		} else if (soggMod.getEtaPresuntaAnni() != null && dataPrimoReato != null) {

			// calcolo gli anni presunti del soggetto
			Date dataNascitaPresunta = DateUtils.moveDateTo(dataPrimoReato, Calendar.YEAR, -soggMod
					.getEtaPresuntaAnni().intValue());
			if (soggMod.getEtaPresuntaMesi() != null) {
				dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH, -soggMod
						.getEtaPresuntaMesi().intValue());
			}
			int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);
			int anniPresuntiUltimoReato = deltaAnni(dataNascitaPresunta, dataUltimoReato);

			// quando il calcolo degli anni presunti restituisce 18, bisogna
			// verificare se il giorno della data di sistema è maggiore
			// della data ultimo reato, in questo saco il soggetto è maggiorenne
			if (anniPresunti == 18) {
				int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
				int lGiornoDataUltimoReato = Integer.parseInt(DateUtils
						.getDateToString(dataUltimoReato, "dd"));
				if (lGiornoDataSistema > lGiornoDataUltimoReato) {
					anni_18_Maggiorenne = true;
				} else {
					anni_18_Maggiorenne = false;
				}
			}

			statoMinorMagg = impostaEtichetta(anniPresunti, anniPresuntiUltimoReato, lFascicoloMod,
					lSentenzaMod, anni_18_Maggiorenne);
			// anni = anniPresunti;

		}

		return statoMinorMagg;
	}

	public String impostaEtichetta(int anni, int anniUltimoReato, FascicoloSiepModel lFascicoloMod,
			SentenzaModel lSentenzaMod, boolean anni_18_Maggiorenne) {

		String etichettaEta = "";

		// a) il soggetto iscritto dalla Procura della Repubblica presso il
		// Tribunale Ordinario (PM) è sempre 'MAGGIORENNE';
		if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PM")) {
			etichettaEta = "MAGGIORENNE";
		}
		// b) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PMM") && anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "MINORENNE";
		}
		// c) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la
		// 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 25 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PMM") && anni <= 24
				&& lFascicoloMod.getVisibilitaMinorenne() != null
				&& lFascicoloMod.getVisibilitaMinorenne().equals("N")) {
			etichettaEta = "MAGGIORENNE";
		}
		// d) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla data di
		// sistema ha più di 18 anni ma meno di 25 anni ed avente campo VISIBILITA_EX_MINORENNE = '';
		else if (lFascicoloMod.getCodTipoUfficio() != null
				&& !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PMM")
				&& anni <= 24
				&& (lFascicoloMod.getVisibilitaMinorenne() == null || lFascicoloMod.getVisibilitaMinorenne()
						.equals(""))) {
			etichettaEta = "MINORENNE";
		}
		// e) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 25 anni;
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PMM") && anni > 24) {
			etichettaEta = "MAGGIORENNE";
		}
		// f) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MAGGIORENNE' se l'ufficio giudicante è diverso da 'CAPSM','DIBM','GIPM';
		else if (lFascicoloMod.getCodTipoUfficio() != null
				&& !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& !lSentenzaMod.getCodTipoAutoritaEmittente().equals("CAPSM")
						&& !lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM") && !lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("GIPM"))) {
			etichettaEta = "MAGGIORENNE";
		}
		// g) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (lFascicoloMod.getCodTipoUfficio() != null
				&& !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null && (lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("CAPSM")
						|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM") || lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("GIPM"))) && anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "MINORENNE";
		}
		// h) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 25 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		else if (lFascicoloMod.getCodTipoUfficio() != null
				&& !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null && (lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("CAPSM")
						|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM") || lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("GIPM"))) && anni <= 24
				&& lFascicoloMod.getVisibilitaMinorenne() != null
				&& lFascicoloMod.getVisibilitaMinorenne().equals("N")) {
			etichettaEta = "MAGGIORENNE";
		}
		// i) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta'
		// alla data di sistema ha più di 18 anni ma meno di 25 anni ed avente
		// campo VISIBILITA_EX_MINORENNE = '';
		else if (lFascicoloMod.getCodTipoUfficio() != null
				&& !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null && (lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("CAPSM")
						|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM") || lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("GIPM")))
				&& anni <= 24
				&& (lFascicoloMod.getVisibilitaMinorenne() == null || lFascicoloMod.getVisibilitaMinorenne()
						.equals(""))) {
			etichettaEta = "MINORENNE";
		}
		// j) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure
		// secondo la 'Età presunta' alla data di sistema ha più di 25 anni.
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (lFascicoloMod.getCodTipoUfficio() != null
				&& !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null && (lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("CAPSM")
						|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM") || lSentenzaMod
						.getCodTipoAutoritaEmittente().equals("GIPM"))) && anni > 24) {
			etichettaEta = "MAGGIORENNE";
		}

		return etichettaEta;
	}

}