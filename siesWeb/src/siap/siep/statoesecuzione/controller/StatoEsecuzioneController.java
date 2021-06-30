package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.calendar.model.TotalePeriodoModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoPerStampaSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaPerStatoEsecuzioneSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.controller.CalcoloPenaControllerF5;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.util.ModuloCumuloUtils;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.dao.PenaResiduaPerStatoEsecuzioneSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoesecuzione.config.StampaProperties;
import siap.siep.statoesecuzione.dao.StatoEsecuzioneSqlDAO;
import siap.siep.statoesecuzione.model.DettaglioLAModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import siap.siep.statoesecuzione.model.LAModel;
import siap.siep.statoesecuzione.model.PeriodoLAModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * StatoEsecuzioneController
 *
 * @author Giselda De Vita
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecuzioneController extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ExCreaStatoEsecuzione
	 */
	public void ExCreaStatoEsecuzione() {
	}

	/**
	 * appendStatoEsecuzione
	 *
	 * @param aModalita
	 * @param aTreeRoot
	 * @param lConn
	 * @param lKeyFascicolo
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
		// Paolo Cherubini 29/04/2011
		RichiestaConversioneSqlDAO lRicConSqlDao = null;
		ScambioSanzioneSqlDAO lScaSanSql = null;

		DepositoOrdinanzaPcModel lDepOrdPCMod = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("\n>>> SONO 222 NEL REWORK DELLO STATO ESECUZIONE!!! <<< ");
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
			lEveDaoStampa.stop();

			// Ricerca Eventi Ordinanza/Decreto
			lEveDaoStampa.ricercaEventoOrdinanzaDecretoByFascicoloSiepXStampa(lKeyFascicolo);
			Vector lVectOrd = new Vector(lEveDaoStampa.getModels());

			// Se esistono degli eventi che devono essere visualizzati in Stampa
			if (lVectEve != null) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("\n>>> SONO 333 NEL REWORK DELLO STATO ESECUZIONE!!! <<< ");

				// ---------------------------------------------------------------
				// Sezione per il caricamento dei dati nelle HASH TABLE
				// in modo da evitare di fare piu' volte accesso alla base dati
				// Carico:
				// Eventi ordinanza/Decreto
				// Pena Residua
				// Misura Alternativa
				// Annotazione Manuale
				// ---------------------------------------------------------------
				Hashtable lHashSorveglianza = new Hashtable();

				if (lVectOrd != null && lVectOrd.size() > 0) {
					Iterator lItxSorv = lVectOrd.iterator();

					while (lItxSorv.hasNext()) {
						// Carico tutte el MA all'interno
						// di una hash table
						EventoModel lEventoSorv = (EventoModel) lItxSorv.next();
						// lMisuraAlternativa.calcolaStringaRevocaArresto();
						// lMisuraAlternativa.calcolaStringaRevocaReclusione();
						// 0289, 0293, 0287, 0288, 0289, 0290, 0292, 0294, 0295, 0296, 0297, 0299, 0298, 0367,
						// 0291, 0295, 0293
						// NEl caso indulto la relazione e' invertita
						if (lEventoSorv != null && lEventoSorv.getCodMotivo() != null
								&& lEventoSorv.getEveIdEvento() != null
								&& (lEventoSorv.getCodMotivo().equals("0284")
										|| lEventoSorv.getCodMotivo().equals("0285")
										|| lEventoSorv.getCodMotivo().equals("0286")))
							lHashSorveglianza.put(lEventoSorv.getEveIdEvento(), lEventoSorv);
						else
							lHashSorveglianza.put(lEventoSorv.getIdEvento(), lEventoSorv);

					}

				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ricerco tutte lo scambio sanzione ");
				// ---------------------------------------------------------------
				// Caricamento Hash Table SCAMBIO SANZIONE
				// Ricerco lo scambio sanzione legato a tutti gli eventi
				// Che devono essere visualizzati all'interno dello stato di
				// esecuzione
				// ---------------------------------------------------------------

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
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("lScambioModel = " + lScambioModel);
					}
				}

				// ---------------------------------------------------------------
				// Caricamento Hash Table PENA RESIDUA
				// Ricerco la pena residua legata a tutti gli eventi
				// Che devono essere visualizzati all'interno dello stato di
				// esecuzione
				// ---------------------------------------------------------------
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

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("Carico Pena Residua = " + lPenRes.getEveIdEvento());
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
						lHashMisAlt.put(lMisuraAlternativa.getEveIdEvento(), lMisuraAlternativa);
					}
				}

				// ---------------------------------------------------------------
				// Caricamento HASH TABLE ANNOTAZIONI MANUALI
				// Ricerco tutte le Annmotazioni MAnuali che fanno riferimento
				// al fascicolo corrente
				// ---------------------------------------------------------------

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
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
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
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("Put in HASH ---> ID EVENTO = " + lIdEvento);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("New Vector for " + lAnnoModel.getEveIdEvento());
							lAnnotazioniTemp = new Vector();
							lAnnotazioniTemp.add(lAnnoModel);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("Put in Vector --->" + lAnnoModel.getIdAnnotazioneManuale()
									+ " - ID EVENTO = " + lAnnoModel.getEveIdEvento());
						} else {
							lAnnotazioniTemp.add(lAnnoModel);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("Put in Vector --->" + lAnnoModel.getIdAnnotazioneManuale()
									+ " - ID EVENTO = " + lAnnoModel.getEveIdEvento());
						}
						lIdEvento = lAnnoModel.getEveIdEvento();
					}
					if (lAnnotazioniTemp != null) {
						lHashAnnotazioni.put(lIdEvento, lAnnotazioniTemp);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Put Last HASH --->" + lAnnoModel.getIdAnnotazioneManuale()
								+ " - ID EVENTO = " + lAnnoModel.getEveIdEvento());
					}
				}
				// --------------------------------------------------------------------
				// FINE SEZIONE caricamento hash table
				// --------------------------------------------------------------------

				lEveDaoStampa.ricercaEveIdEventi(lKeyFascicolo);
				Vector lEveIdEventi = lEveDaoStampa.getModelEveIdEventi();

				// --------------------------------------------------------------------
				// INIZIO SEZIONE caricamento EVENTI
				// --------------------------------------------------------------------

				// Istanzio il disphatcer dei vari componenti dello Stato Esecuzione
				CreatorStatoEsecuzione lCreator = new CreatorStatoEsecuzione();

				StatoEsecuzioneElement lStatEsec = new StatoEsecuzioneElement();
				lStatEsec.setHashPenaResidua(lHashPenRes);
				lStatEsec.setHashAnnotazioni(lHashAnnotazioni);
				lStatEsec.setHashEventiRiferimento(lHashSorveglianza);
				lStatEsec.setHashMisure(lHashMisAlt);
				lStatEsec.setEveIdEvento(lEveIdEventi);
				// FIXME d.f. verificare se e' il caso di recuperare anche le LA.
				// Sebbene siano legate al provvedimento SIUS hanno un ref anche al fascicolo SIEP
				// Si potrebber recuperare le LA legate a Ordinanze/Decreti puntati
				// da provvedimenti SIUS.

				String lStringaLiberazioneAnticipata = null;
				// String lDetrazioneStringaLiberazioneAnticipata = null;

				String lStringaNewLiberazioneAnticipataLA = null;
				String lStringaNewLiberazioneAnticipataLAS = null;
				String lStringaNewLiberazioneAnticipataLAI = null;

				// String lStringaPeriodoLiberazioneAnticipata = null;
				// String lStringaPeriodoLiberazioneAnticipataLA = null;
				// String lStringaPeriodoLiberazioneAnticipataLAS = null;
				// String lStringaPeriodoLiberazioneAnticipataLAI = null;

				String lStringaRimediRisarcitoriDL92 = null;

				/*------------------------------------------------------------------------------
				 * Ciclo su tutti gli eventi associati al fascicolo
				 * e visualizzabili in stampa
				 *------------------------------------------------------------------------------*/
				Iterator lItx = lVectEve.iterator();
				while (lItx.hasNext()) {
					EventoModel lEveNot = (EventoModel) lItx.next();
					lEveNot.setEventoCorrente("N");

					/*
					 * Creazione dell'Elememto Statement Element
					 */
					String lTipoEvento = lCreator.getTipoStatoEsecuzioneDaCreare(lEveNot);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(" XXXZZ  lTipoEvento = " + lTipoEvento);

					// StatoEsecuzioneElement lStatEsecElement = new StatoEsecuzioneElement();
					StatoEsecuzioneElement lStatEsecElement;
					lStatEsecElement = lCreator.create(lTipoEvento, lStatEsec);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(" XXXZZ  Evento: id = " + lEveNot.getIdEvento() + " - CodMotivo = "
							+ lEveNot.getCodMotivo());
					lStatEsecElement.elabora(lEveNot);

					siap.siep.statoesecuzione.model.EventoModel lEve = lStatEsecElement.getEventoModel();

					// ====================================================================
					// ATTENZIONE. Se tutto e' andato correttamente non c'e' altro da fare.
					// Sono i singoli StatoEsecuzioneXX che sul metodo elabora()
					// hanno valorizzato tutte le informazioni e settate sull'oggetto
					// EventoModel (di StatoEsecuzione)
					// Quindi si dovrebbe solo prelevare l'evento [lStatEsecElement.getEventoModel()]
					// e creare gli opportuni TreeModel da aggiungere nell'xml
					// ====================================================================

					if (lEve != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(" XXXZZ Evento lEve id = " + lEve.getIdEvento());
						// Paolo Cherubini 26/05/2011 provo a caricare le LA anche se la famiglia non e' LA
						// La LA e' collegata all'evento PM collegato al Nostro per il caso 01 09 0998
						// ridimensionamento

						// FIXME d.f. 04/08/2015 tutta questa parte di codice che cerca di
						// ricostruire il tot gg concessi e le stringe dei periodi
						// andrebbe spostata in un metodo a parte. Meglio se nel
						// modulo che costruisce la classe LA
						ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote
								.getLicenzaPeriodiLibAntRemote();
						if (("0998").equals(lEve.getCodMotivo()) // RidimensionamentoLA
								|| ("0081").equals(lEve.getCodMotivo()) // O.S. Nuova scadenza pena a seguito
																		// di L.A - condannato detenuto
								|| ("0083").equals(lEve.getCodMotivo()) // O.S. Nuova scadenza pena a seguito
																		// di L.A - condannato in mis alt
								|| ("0922").equals(lEve.getCodMotivo()) // Comunicazione Concessione L.A. -
																		// condannato Ergastolo
								|| ("0923").equals(lEve.getCodMotivo()) // Comunicazione Concessione L.A. -
																		// condannato Libero
								|| ("5493").equals(lEve.getCodMotivo()) // DL92 - Condannato Libero
								|| ("5494").equals(lEve.getCodMotivo()) // DL92 - Condannato in Ergastolo
								|| ("5495").equals(lEve.getCodMotivo()) // DL92 - Condannato gia' Scarcerato
						) {
							// ================================================================
							// Nel caso di provvedimenti SIEP di concessione LA si recuperano
							// i giorni concessi con provvedimento (n.b. solo quelli con
							// provvedimento)
							// ================================================================
							try {
								// String Periodi_Si="";
								// String PeriodiLASPE_Si="";
								// String PeriodiLAINT_Si="";
								// String PeriodiLA_Si="";
								List lListLA = lCtrlLib.ExRicercaLicenzeByEve(lEveNot.getEveIdEvento());

								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("lListLA.size()=" + lListLA.size());

								if (!lListLA.isEmpty()) {
									if (("0998").equals(lEve.getCodMotivo())) { // RidimensionamentoLA
										for (int i = 0; i < lListLA.size(); i++) {
											LicenzaLibAnticipataModel llibAntMod = (LicenzaLibAnticipataModel) lListLA
													.get(i);
											if (llibAntMod.getDescrStatoPermesso() != null) {
												if (llibAntMod.getDescrStatoPermesso().equals("LA")) {
													lEve.setStringaDopoProvvedimento("nella misura di gg "
															+ llibAntMod.getNumeroGiorni() + " per L.A.");
												} else if (llibAntMod.getDescrStatoPermesso().equals("LS")) {
													lEve.setStringaDopoProvvedimentoLASPE(
															"nella misura di gg "
																	+ llibAntMod.getNumeroGiorni()
																	+ " per L.A. Speciale");
												} else if (llibAntMod.getDescrStatoPermesso().equals("LI")) {
													lEve.setStringaDopoProvvedimentoLAINT(
															"nella misura di gg "
																	+ llibAntMod.getNumeroGiorni()
																	+ " per Integrazione L.A.");
												}
											} else {
												lEve.setStringaDopoProvvedimento(
														"nella misura di gg " + llibAntMod.getNumeroGiorni());
											}
										}
									} else {
										/*
										 * MEV29 - Sezione comentata in quanto la gestione di tali provv e'
										 * stata spostata nello StatoEsecuzioneLA che calcola i totali e le
										 * stinghe dei periodi
										 *
										 * //========================================================== // Si
										 * calcolano qui il tot gg concessi CON IL PROVVEDIMENTO (EVENTO) // e
										 * le stringhe dei periodi
										 * //==========================================================
										 * lStringaPeriodoLiberazioneAnticipata ="Relativamente ai periodi";
										 * lStringaPeriodoLiberazioneAnticipataLA ="Relativamente ai periodi";
										 * lStringaPeriodoLiberazioneAnticipataLAS
										 * ="Relativamente ai periodi";
										 * lStringaPeriodoLiberazioneAnticipataLAI
										 * ="Relativamente ai periodi"; int lTotGiorni = 0; int lTotGiorniLA =
										 * 0; int lTotGiorniLS = 0; int lTotGiorniLI = 0;
										 *
										 * //Liberazione Anticipata + Periodi Liberazione lLibDAO = new
										 * LicenzaLibanticipataSqlDAO(lConn); lPerDao = new
										 * PeriodoLibanticipataSqlDAO(lConn);
										 * lLibDAO.ricercaLicenzaLibanticipataByEve(lEveNot.getEveIdEvento());
										 * List lLibVect = new Vector(lLibDAO.getModels());
										 *
										 * for (int i = 0; i < lLibVect.size(); i++) {
										 * LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel)
										 * lLibVect.get(i);
										 *
										 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										 * siesLogger al posto di LogF3B.getLogger()
										 * siesLogger.debug(">>>>>>>>> append NEW LiberazioneAnticipata" ); if
										 * (lLibAnt != null && lLibAnt.getFlagConcesso() != null &&
										 * lLibAnt.getFlagConcesso().equals("C")) //&&
										 * (lLibAnt.getFlagElaborato() == null ||
										 * lLibAnt.getFlagElaborato().equals("N") ||
										 * lLibAnt.getFlagElaborato().equals("E"))) {
										 * if(lLibAnt.getDescrStatoPermesso() != null) {
										 * if(lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LA") )
										 * lTotGiorniLA += lLibAnt.getNumeroGiorni().intValue();
										 * if(lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LS") )
										 * lTotGiorniLS += lLibAnt.getNumeroGiorni().intValue();
										 * if(lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LI") )
										 * lTotGiorniLI += lLibAnt.getNumeroGiorni().intValue(); }
										 *
										 * if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
										 * lTotGiorni += lLibAnt.getNumeroGiorni().intValue();
										 * LogF3B.getLogger
										 * ().debug(">>>>>>>>>>>>>>>>>>>>>>> lTotGiorni = "+lTotGiorni); }
										 *
										 *
										 * //====================================================== // Ricerca
										 * dei Periodi relativi alla licenza per comporre // la stringa.
										 * lPerDao
										 * .ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata
										 * ()); List lPeriodi = new ArrayList(lPerDao.getModels());
										 *
										 * if(lPeriodi.size() !=0) { Iterator lIterPeriodi =
										 * lPeriodi.iterator(); while (lIterPeriodi.hasNext()) {
										 * PeriodoLibAnticipataModel lPeriodoModel =
										 * (PeriodoLibAnticipataModel) lIterPeriodi.next(); String DataIni =
										 * DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
										 * "dd-MM-yyyy"); String DataFIni =
										 * DateUtils.getDateToString(lPeriodoModel.getDataFine(),
										 * "dd-MM-yyyy");
										 *
										 * if(lLibAnt.getDescrStatoPermesso() != null) {
										 * if(lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LA") ) {
										 * lStringaPeriodoLiberazioneAnticipataLA
										 * +=" dal "+DataIni+" al "+DataFIni+";"; PeriodiLA_Si="SI"; }
										 * if(lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LS") ) {
										 * lStringaPeriodoLiberazioneAnticipataLAS
										 * +=" dal "+DataIni+" al "+DataFIni+";"; PeriodiLASPE_Si="SI"; }
										 * if(lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LI") ) {
										 * lStringaPeriodoLiberazioneAnticipataLAI
										 * +=" dal "+DataIni+" al "+DataFIni+";"; PeriodiLAINT_Si="SI"; } }
										 * else { lStringaPeriodoLiberazioneAnticipata
										 * +=" dal "+DataIni+" al "+DataFIni+";"; Periodi_Si="SI"; } } } } }
										 * // End for
										 *
										 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										 * siesLogger al posto di LogF3B.getLogger() siesLogger.debug(
										 * ">>>>>>>>>>>>>>>>>>>>>>> CONTINUA --- > lTotGiorni = "+lTotGiorni);
										 * // Calcola il totale dei giorni di Licenza Anticipata
										 *
										 *
										 * if ( lTotGiorni != 0 && ( "5493".equals(lEve.getCodMotivo()) ||
										 * "5494".equals(lEve.getCodMotivo()) ||
										 * "5495".equals(lEve.getCodMotivo()) ) ) { // DL92
										 * lStringaLiberazioneAnticipata
										 * ="Risarcimento Danni D.L. 92/2014 gg "+lTotGiorni;
										 * lEve.setStringRisarcimentoDL92(lStringaLiberazioneAnticipata); }
										 * else if (lTotGiorni != 0) { lStringaLiberazioneAnticipata
										 * ="Liberazione Anticipata gg "+lTotGiorni;
										 * lEve.setStringLiberazioneAnticipata(lStringaLiberazioneAnticipata);
										 * }
										 *
										 * if (lTotGiorniLA != 0) { lStringaNewLiberazioneAnticipataLA =
										 * "Liberazione Anticipata gg "+lTotGiorniLA;
										 * lEve.setStringLiberazioneAnticipataLA
										 * (lStringaNewLiberazioneAnticipataLA); }
										 *
										 * if (lTotGiorniLS != 0) { lStringaNewLiberazioneAnticipataLAS
										 * ="Liberazione Anticipata Speciale gg "+lTotGiorniLS;
										 * lEve.setStringLiberazioneAnticipataLASpec
										 * (lStringaNewLiberazioneAnticipataLAS); }
										 *
										 * if (lTotGiorniLI != 0) { lStringaNewLiberazioneAnticipataLAI
										 * ="Integrazione Liberazione Anticipata gg "+lTotGiorniLI;
										 * lEve.setStringLiberazioneAnticipataLAInt
										 * (lStringaNewLiberazioneAnticipataLAI); }
										 *
										 * if(!Periodi_Si.equals(""))
										 * lEve.setStringPeriodoLiberazioneAnticipata
										 * (lStringaPeriodoLiberazioneAnticipata);
										 * if(!PeriodiLA_Si.equals(""))
										 * lEve.setStringPeriodoLiberazioneAnticipataLA
										 * (lStringaPeriodoLiberazioneAnticipataLA);
										 * if(!PeriodiLASPE_Si.equals(""))
										 * lEve.setStringPeriodoLiberazioneAnticipataLASPE
										 * (lStringaPeriodoLiberazioneAnticipataLAS);
										 * if(!PeriodiLAINT_Si.equals(""))
										 * lEve.setStringPeriodoLiberazioneAnticipataLAINT
										 * (lStringaPeriodoLiberazioneAnticipataLAI);
										 */
									} // END
								}
							} catch (F3BException e) {
							}
						} // CHIUDE if ( ("0998").equals(lEve.getCodMotivo()) ||
							// ("0081").equals(lEve.getCodMotivo()) || ("0083").equals(lEve.getCodMotivo()) ||
							// ......

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lEve.getStringaPenaResidua() = " + lEve.getStringaPenaResidua());

						// FIXME attenzione la successiva parte di codice sovrascrive i totali
						// delle LA calcolati nella precedente

						// ==================================================================
						// Nel caso di provvedimento che HA ASSOCIATA una pena residua DA VISUALIZZARE
						// o un
						// provvedimento di 'LA', si recuperano i dati delle LA da visualizzare
						// nello stato di esecuzione, ovvero le LA totali computate/da computare
						// sulla pena. Nel caso della famoglia 'LA' quindi non solo quelle
						// concesse con il provvedimento, ma tutte.
						//
						// ==================================================================
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lEve.getFamiglia() = " + lEve.getFamiglia());
						if ((lEve.getStringaPenaResidua() != null
								&& lEve.getStringaPenaResidua().length() > 0)
								|| lEve.getFamiglia().equals("LA")) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Recupero i totali LA e DL92 da visualizzare l'evento con id = "
									+ lEve.getIdEvento());

							// ========================================================================
							// Vengono recuperati i GG di LA concessi (presi in carico e associati a
							// un evento SIES VALIDATI) gia' detratti o da detrarre
							// Nuova gestione LA (12/2006)
							// ========================================================================
							CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5();
							CalcoloPenaModel lCalcPenaModel = lCtrlF5.exGetPenaIniziale(
									lEveNot.getFasSieIdFascicoloSiep(), lEveNot.getIdEvento());
							Vector lListaLA = lCtrlF5.exGetLiberazioneAnticipata(
									lEveNot.getFasSieIdFascicoloSiep(), lCalcPenaModel.getDataDal(),
									lCalcPenaModel.getDataAl());

							lCalcPenaModel.setLibAnticipate(lListaLA);
							int totGiorniLAConcessi = lCalcPenaModel.getLiberazioneAnticipataGiaConcesse();

							int totGiorniConcessiLA = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLA();
							int totGiorniConcessiSPE = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLS();
							int totGiorniConcessiINT = lCalcPenaModel.getLiberazioneAnticipataGiaConcesseLI();

							int totGiorniRisarcimentoConcessi = lCalcPenaModel
									.getRimediRisarcitoriGiaConcessi();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("totGiorniRisarcimentoConcessi = "
									+ totGiorniRisarcimentoConcessi + " - idEvento = " + lEve.getIdEvento()
									+ ". Famiglia: " + lEve.getFamiglia());

							if (totGiorniLAConcessi > 0 || totGiorniRisarcimentoConcessi > 0) {
								lEve.setStringDetrazioneLiberazioneAnticipata("");

								if (totGiorniLAConcessi > 0) {
									lStringaLiberazioneAnticipata = "Totale Liberazione Anticipata gg "
											+ totGiorniLAConcessi;
									lEve.setStringLiberazioneAnticipata(lStringaLiberazioneAnticipata);
								}

								if (totGiorniConcessiLA > 0) {
									lStringaNewLiberazioneAnticipataLA = "Totale Liberazione Anticipata gg "
											+ totGiorniConcessiLA;
									lEve.setStringLiberazioneAnticipataLA(lStringaNewLiberazioneAnticipataLA);
								}

								if (totGiorniConcessiSPE > 0) {
									lStringaNewLiberazioneAnticipataLAS = "Totale Liberazione Anticipata Speciale gg "
											+ totGiorniConcessiSPE;
									lEve.setStringLiberazioneAnticipataLASpec(
											lStringaNewLiberazioneAnticipataLAS);
								}

								if (totGiorniConcessiINT > 0) {
									lStringaNewLiberazioneAnticipataLAI = "Totale Integrazione Liberazione Anticipata gg "
											+ totGiorniConcessiINT;
									lEve.setStringLiberazioneAnticipataLAInt(
											lStringaNewLiberazioneAnticipataLAI);
								}

								// inserire qui i dati dei gg DL92
								if (totGiorniRisarcimentoConcessi > 0) {
									lStringaRimediRisarcitoriDL92 = "Totale Risarcimento Danni D.L. 92/2014 gg "
											+ totGiorniRisarcimentoConcessi;
									lEve.setStringRisarcimentoDL92(lStringaRimediRisarcitoriDL92);
								}

								// Se l'evento fa parte di una delle famiglie che ricalcola la pena allora la
								// LA diventa gia' detratta
								if (lEve.getFamiglia().equals("MAC") || lEve.getFamiglia().equals("DIFF")
										|| lEve.getFamiglia().equals("INTERR")
										|| lEve.getFamiglia().equals("MAR")
										|| lEve.getFamiglia().equals("PROVV")
										|| lEve.getFamiglia().equals("SIMEONE")
										|| lEve.getFamiglia().equals("RIDETERM"))
									lEve.setStringDetrazioneLiberazioneAnticipata(
											StampaProperties.getInstance().getProperty("LA_DETRATTA"));
							}
						}
						// FINE Paolo Cherubini 17/01/2011 modifico gestione stringa liberazione anticipata

						// ==================================================================
						// Si compone il tree che serve per la composizione dell'alberatura
						// ==================================================================

						// Paolo Cherubini 22/06/2011
						// esce un null nella stringa nello stato esecuzione provo a toglierlo
						if (lEve.getStringaPenaResidua() != null)
							lEve.setStringaPenaResidua(lEve.getStringaPenaResidua().replace("null ", ""));

						TreeModel lStatEve = new TreeModel(lEve);

						// inserisco nel TreeModel i rami relativi
						// all'evento sorveglianza dipendente dall'evento
						if (lEve != null && lEve.getEventoSorveglianza() != null) {
							TreeModel lStatEveSorv = new TreeModel(lEve.getEventoSorveglianza());
							lStatEve.add(lStatEveSorv);
						}

						// ====================================
						// Aggiunta Tree TENORI
						if (lEve != null && lEve.getEventoSorveglianza() != null
								&& lEve.getEventoSorveglianza().getTenori() != null
								&& lEve.getEventoSorveglianza().getTenori().size() > 0) {
							Iterator iter = lEve.getEventoSorveglianza().getTenori().iterator();
							while (iter.hasNext()) {
								TenoreModel lTenMod = (TenoreModel) iter.next();
								if ((lTenMod.getCodOggettoTenore().equals("2440")
										|| lTenMod.getCodOggettoTenore().equals("2441"))
										&& lTenMod.getCodEsitoTenore().equals("0053")) {
									IDepositoOrdinanzaPc lctrlD = SIUSLookupRemote
											.getDepositoOrdinanzaPcRemote();
									lDepOrdPCMod = lctrlD
											.ExRicercaDepositoOrdinanzaPcByEvento(lEve.getIdEvento());
								}
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug(
										"--XX-- StatoEsecuzioneController - TreeModel TENORI - TenoreModel = "
												+ lTenMod);
								TreeModel lTreeTenori = new TreeModel(lTenMod);
								lStatEve.add(lTreeTenori);

								if (lDepOrdPCMod != null && lDepOrdPCMod.getIdDepositoOrdinanzaPc() != null) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.debug(
											"--XX-- StatoEsecuzioneController - TreeModel DEPOSITO_ORDINANZA_PC - DepositoPCModel = "
													+ lDepOrdPCMod);
									TreeModel lTreeDepoPc = new TreeModel(lDepOrdPCMod);
									lStatEve.add(lTreeDepoPc);
								}
							}
						}

						// AMBROSINO 04/2013 --> Data commessa EVASIONE in certficato StatoEsecuzione
						// Aggiungo SOSPENSIONE

						if (lEve.getCodMotivo().equals("0267")) {
							SospensioneSqlDAO lSospSDao = null;
							lSospSDao = new SospensioneSqlDAO(lConn);
							if (lPenResi != null) {
								Iterator lItxPenRes = lPenResi.iterator();

								while (lItxPenRes.hasNext()) {
									PenaResiduaModel lPenRes = (PenaResiduaModel) lItxPenRes.next();
									if (lPenRes.getEveIdEvento().equals(lEve.getIdEvento())) {
										lSospSDao.ricercaSospensioneByIdPenaResidua(
												lPenRes.getIdPenaResidua());
										SospensioneModel lSospMod = new SospensioneModel();
										lSospMod = (SospensioneModel) lSospSDao.getModelByKey();

										TreeModel lTreeSosp = new TreeModel(lSospMod);
										lStatEve.add(lTreeSosp);

									}

								}
							}

						}

						// END AMBROSINO 04/2013

						// MEV29
						if (lEve.getDettagliLibAnticipate() != null
								&& lEve.getDettagliLibAnticipate().size() > 0) {
							Iterator<DettaglioLAModel> lLA = lEve.getDettagliLibAnticipate().iterator();
							while (lLA.hasNext()) {
								lStatEve.add(new TreeModel(lLA.next()));
							}
						}

						if (lEve.getFungibilita() != null) {
							TreeModel lTreeFung = new TreeModel(lEve.getFungibilita());
							lStatEve.add(lTreeFung);
						}
						// =================================================
						// Liberazione Anticipata + Periodi Liberazione
						// =================================================
						if (lEve.getLibAnticipate() != null && lEve.getLibAnticipate().size() > 0) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug(
									"Evento con LA: " + lEve.getIdEvento() + "-" + lEve.getCodMotivo());
							// FIXME: SE n.b. getLibAnticipate()!=null solo per la Famiglia 'LA'
							// Questa parte di codice probabilmente non viene mai eseguita in
							// quanto la famiglia LA contiene 0081,0083,2130,0076. I primi due
							// codici sono esclusi dall'if mentre 2130 e 0076 sono le ordinanze
							// di concessione LA per le quali non vengono recuperate le LA
							// In pratica setLibAnticipate() viene invocato solo per 0081 e 0083
							if (!("0081").equals(lEve.getCodMotivo()) && // O.S. Nuova scadenza pena a seguito
																			// di L.A - condannato detenuto
									!("0083").equals(lEve.getCodMotivo()) && // O.S. Nuova scadenza pena a
																				// seguito di L.A - condannato
																				// in mis alt
									!("0922").equals(lEve.getCodMotivo()) && // Comunicazione Concessione L.A.
																				// - condannato Ergastolo
									!("0923").equals(lEve.getCodMotivo())) { // Comunicazione Concessione L.A.
																				// -
																				// condannato Libero
								Iterator lLA = lEve.getLibAnticipate().iterator();
								while (lLA.hasNext()) {
									Object lObj = lLA.next();
									if (lObj instanceof LAModel) {
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										// siesLogger al posto di LogF3B.getLogger()
										siesLogger.debug("instanceof LAModel");
										LAModel lLAMod = (LAModel) lObj;
										TreeModel lTreeLibAnt = new TreeModel(lLAMod.getLAModel());
										lStatEve.add(lTreeLibAnt);

										if (lLAMod.getPeriodiLA() != null
												&& lLAMod.getPeriodiLA().size() > 0) {
											Iterator lIterPeriodi = lLAMod.getPeriodiLA().iterator();
											// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
											// siesLogger al posto di mLog
											siesLogger.info("PERIODI LA = " + lLAMod.getPeriodiLA().size());
											while (lIterPeriodi.hasNext()) {
												PeriodoLAModel lPeriodoModel = (PeriodoLAModel) lIterPeriodi
														.next();
												lTreeLibAnt.add(new TreeModel(lPeriodoModel));
											}
										}
									} else if (lObj instanceof TotalePeriodoModel) {
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										// siesLogger al posto di LogF3B.getLogger()
										siesLogger.debug("instanceof TotalePeriodoModel");

										TotalePeriodoModel lTotPeriodo = (TotalePeriodoModel) lObj;
										TreeModel lTreeTotLibAnt = new TreeModel(lTotPeriodo);
										lStatEve.add(lTreeTotLibAnt);
									} else if (lObj instanceof FungibilitaModel) {
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										// siesLogger al posto di LogF3B.getLogger()
										siesLogger.debug("instanceof FungibilitaModel");
										FungibilitaModel lFungibilita = (FungibilitaModel) lObj;
										TreeModel lTreeFung = new TreeModel(lFungibilita);
										lStatEve.add(lTreeFung);
									}
								}
							}
						}

						// 12/2014 --> MISure SICurezza: Designazione Istituto : e' scritto sul VERBALE
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("----- XXXXX --- Ricerca VERBALE ");
						if (lEve.getCodMotivo().equals("1130")) {
							VerbaleSqlDAO lVerbSDao = null;
							IstitutoDetenzioneSqlDAO lIstDAO = null;

							VerbaleModel lVerMod = null;

							lVerbSDao = new VerbaleSqlDAO(lConn);
							lIstDAO = new IstitutoDetenzioneSqlDAO(lConn);
							lVerbSDao.ricercaVerbaleByIdEvento(lEve.getIdEvento());
							lVerMod = (VerbaleModel) lVerbSDao.getModelByKey();

							if (lVerMod != null && lVerMod.getIdVerbale() != null) {
								TreeModel lTreeVerbale = new TreeModel(lVerMod);
								lStatEve.add(lTreeVerbale);

								if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null
										&& !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.debug("-----  Aggiungo Istituto ");
									lIstDAO.ricercaIstitutoDetenzioneByKey(
											lVerMod.getIstDetIdIstitutoDetenzione());
									IstitutoDetenzioneModel lIstMod = (IstitutoDetenzioneModel) lIstDAO
											.getModelByKey();
									if (lIstMod != null && lIstMod.getIdIstitutoDetenzione() != null) {
										lTreeVerbale.add(new TreeModel(lIstMod));
									}
									// TreeModel lTreeIstituto = new TreeModel(lIstMod);
									// lStatEve.add(lTreeIstituto);
								}
							}
						}

						aTreeRoot.add(lStatEve);

						// ------------UFFICIO -----------------
						// -----------------Ufficio proprietario dell'evento
						UfficioModel lUfficio = new UfficioModel();
						lUfficio.setDescrTipoUfficio(lEve.getDescrUfficioEmittente());
						lUfficio.setDescrComune(lEve.getDescrLuogoEmittente());
						lStatEve.add(new TreeModel(lUfficio));

						// ==================================================================
						// MEV26 - Cumulo - INIZIO
						// se cumulo emesso dal GE recupero i dati dell'ufficio per visualizzarli
						// nello stato esecuzione
						// es "da Giudice Esecuzione - Corte D'Appello ROMA"
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lEveNot = " + lEveNot);
						if (lEveNot.getIstruIdIstruttoriaCumulo() != null
								&& ModuloCumuloUtils.isCumulo(lEve.getCodMotivo())) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("isCumlo = true");
							BigDecimal lIdIstruttoria = lEveNot.getIstruIdIstruttoriaCumulo();
							IDatiFinaliCumulo lDatFinCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
							DatiFinaliCumuloModel lDatFinCumulo = lDatFinCtrl
									.ExRicercaDatiFinaliCumuloByIdIstrutt(lIdIstruttoria);

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("lDatFinCumulo = " + lDatFinCumulo);

							/*
							 * ISSUE MAC : aggiunto il controllo su data finali cumulo != null 
							 * Numero MAC : 20200224011
							 * Autore : monica 
							 * Data : 25/feb/2020 
							 * Branch : 12.1
							 */
							if (lDatFinCumulo != null
									&& "03".equals(lDatFinCumulo.getTipoUfficioEmissione())) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								// ***** FINE INTERVENTO MAC_numero_MAC *****//
								siesLogger.debug("Provo a recuperare i dati dell'ufficio del GE");
								IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();

								UfficioModel lUfficioGE = lUffCtrl.getUfficioByCodTipoUffCodComune(
										lDatFinCumulo.getCodTipoUfficioEmittente(),
										lDatFinCumulo.getCodLuogoUfficioEmittente());

								if (lUfficioGE != null) {
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di LogF3B.getLogger()
									siesLogger.debug("lUfficioGE = " + lUfficioGE);
									// lStatEve.add(new TreeModel(lUfficioGE));

									lEve.setFraseUfficio("da Giudice Esecuzione -");
									lEve.setDescrUfficioEmittente(lUfficioGE.getDescrTipoUfficio() + " "
											+ lUfficioGE.getDescrComune());
								}
							}
						}
						// MEV26 - Cumulo - FINE
						// ==================================================================

						// =============================================
						// Cerco la Misura Alternativa sull'hash table
						MisuraAlternativaModel lMisuraAlternativa = (MisuraAlternativaModel) lHashMisAlt
								.get(lEveNot.getIdEvento());

						lStatEve.add(new TreeModel(lMisuraAlternativa));

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("carico ScambioSanzioneModel ");

						ScambioSanzioneModel lScaSanMod = new ScambioSanzioneModel();
						if (lEveNot != null && lEveNot.getEveIdEvento() != null)
							lScaSanMod = (ScambioSanzioneModel) lHashScaSons.get(lEveNot.getEveIdEvento());
						else if (lEveNot != null && lEveNot.getIdEvento() != null)
							lScaSanMod = (ScambioSanzioneModel) lHashScaSons.get(lEveNot.getIdEvento());
						if (lScaSanMod != null && lScaSanMod.getIdScambioSanzione() != null)
							lStatEve.add(new TreeModel(lScaSanMod));

						// Annotazioni manuali eventuali
						if (lEve.getAnnotazioniManuali() != null) {
							for (Iterator i = lEve.getAnnotazioniManuali().iterator(); i.hasNext();) {
								AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) i.next();
								lStatEve.add(new TreeModel(lAnnMod));
							}
						}

						// Paolo Cherubini 06/02/2012
						// Modifica relativa alla segnalazione bb/rr/004
						// Problema: Quando viene emesso il decreto di irreperibilita' non vengono riportati i
						// dati del verbale di vane ricerche ( data e autorita')
						// Spiegazione: Occorre prendere i dati o dal rinnovo o dal Verbale vane ricerche
						// vince quello piu' recente
						// Modifica: Prendo l'ultimo rinnovo poi prendo l'ultimo VVR e se il rinnovo e' piu'
						// recente lo carico nell'XML
						// altrimenti no. Il template e' fatto in modo che se trova il rinnovo prende sempre
						// il rinnovo altrimenti l'ultimo VVR
						IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
						// Ticket#202106220110 - Si recupera l'ultimo 
						// RinnovoModel lRinMod = lCtrl.ExRicercaRinnovoByKeyEvento(lEveNot.getIdEvento());
						RinnovoModel lRinMod = lCtrl.ExRicercaUltimoRinnovoByKeyEvento(lEveNot.getIdEvento());
						// Ticket#202106220110 - FINE
						if (lRinMod != null) {
							String[] lCodTipoProvv = { "17" };
							String[] lCodMotivi = { "0313" }; // VVR

							IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
							EventoModel lEveModxRicerca = new EventoModel(lEveNot);
							lEveModxRicerca.setCodMotivo(null);
							lEveModxRicerca.setCodTipoProvvedimento(null);
							lEveModxRicerca.setCodTipoEvento(null);
							EventoModel lEveModVVR = lCtrlEvento.ExRicercaEventoUnicoTipoProvTipoMot(
									lEveModxRicerca, lCodTipoProvv, lCodMotivi);
							if (lEveModVVR != null) {
								if (lEveModVVR.getDataEmissione().before(lRinMod.getDataRinnovo()))
									lStatEve.add(new TreeModel(lRinMod));
							} else {
								lStatEve.add(new TreeModel(lRinMod));
							}
						}
						// fine Paolo Cherubini 06/02/2012 b2/rr/004

						// ===================================================================
						// Paolo Cherubini 02/05/2011 aggiungo l'estrazione della richiesta di conversione
						// effettuo l'estrazione solo per codice motivo = 0942 ossia 'Annotazione Conversione
						// Pena Pecuniaria'
						// Se sono su un procedimento di classe VII cerco la richiesta di conversione tramite
						// evento
						// se sono su un procedimenti di classe I cerco il procedimento di classe VII
						// collegato e poi la
						// relativa richiesta di conversione, aggiunto l'indicazione del fascicolo convertito
						// di classe VII
						// usando per gentile concessione la stringa di liberazione anticipata
						// p.s. nel template stato esecuzione ho aggiunto le relative righe per far uscire la
						// richiesta di conversione
						// ===================================================================
						if (lEveNot.getCodMotivo().equals("0942")) {

							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug("====== richiesta conversione =======");

							// Paolo Cherubini 29/04/2011 aggiungo inserimento della richiesta di conversione
							// Cerca il fascicolo by key fascicolo
							FascicoloSiepSqlDAO lFascDao = null;
							lFascDao = new FascicoloSiepSqlDAO(lConn);
							lFascDao.ricercaFascicoloByKey(lEveNot.getFasSieIdFascicoloSiep());
							FascicoloSiepModel lFasMod = null;
							lFasMod = (FascicoloSiepModel) lFascDao.getModelByKey();
							RichiestaConversioneModel lRicConMod = new RichiestaConversioneModel();
							int lFascProg = lFasMod.getChiaveProgr().intValue();
							if (lFascProg > 70000 && lFascProg < 80001) {

								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di mLog
								siesLogger.debug("====== classe VII =======");

								// Se sono su un procedimento di classe VII cerco la richiesta di conversione
								// tramite evento
								lRicConSqlDao = new RichiestaConversioneSqlDAO(lConn);
								lRicConSqlDao.ricercaRichiestaConversioneByEvento(lEveNot.getIdEvento());
								lRicConMod = (RichiestaConversioneModel) lRicConSqlDao.getModelByKey();
								if (lRicConMod != null && lRicConMod.getIdRichiestaConversione() != null)
									lStatEve.add(new TreeModel(lRicConMod));
							} else {
								// lFascDao = new FascicoloSiepSqlDAO(lConn);

								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di mLog
								siesLogger.debug("====== classe I =======");

								// se sono su un procedimenti di classe I cerco il procedimento di classe VII
								// collegato e poi la
								// relativa richiesta di conversione, aggiunto l'indicazione del fascicolo
								// convertito di classe VII
								// usando per gentile concessione la stringa di liberazione anticipata

								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di mLog
								siesLogger.debug("= lavoro evento = " + lEveNot.getIdEvento());

								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di mLog
								siesLogger.debug("= cerco classe VII collegata a questa classe I = "
										+ lEveNot.getFasSieIdFascicoloSiep());

								FascicoloSiepModel lFasModVII = new FascicoloSiepModel();
								lFasModVII.setFasSieIdFascicoloSiep(lEveNot.getFasSieIdFascicoloSiep());
								lFascDao.ricercaFascicolo(lFasModVII);
								Vector lFascicoli = new Vector(lFascDao.getModels());
								if (lFascicoli.size() > 0) {
									lFasMod = (FascicoloSiepModel) lFascicoli.get(0);

									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di mLog
									siesLogger.debug("= trovata classe VII collegata = "
											+ lFasMod.getIdFascicoloSiep());

									lRicConSqlDao = new RichiestaConversioneSqlDAO(lConn);
									lRicConSqlDao.ricercaRichiestaConversioneByIdFascicoloSiep(
											lFasMod.getIdFascicoloSiep());

									lRicConMod = (RichiestaConversioneModel) lRicConSqlDao.getModelByKey();
									if (lRicConMod != null
											&& lRicConMod.getIdRichiestaConversione() != null) {
										lStatEve.add(new TreeModel(lRicConMod));
										lEve.setStringLiberazioneAnticipata("Fascicolo convertito "
												+ lFasMod.getChiaveAnno() + "/" + lFasMod.getChiaveProgr());
									}
								}
							}
						}

						/*
						 * //Annotazioni Manuali if (true) //trovata annotazione) { //Cerco l'annotazione
						 * manuale nell'hash table
						 * //------------------------------------------------------------------------- //
						 * lAnnoSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveNot.getIdEvento()); // List
						 * lListAnnMod = (ArrayList) lAnnoSqlDao.getModels();
						 * //------------------------------------------------------------------------- Vector
						 * lListAnnMod = (Vector) lHashAnnotazioni.get(lEveNot.getIdEvento());
						 *
						 * if (lListAnnMod != null) { for (Iterator i = lListAnnMod.iterator(); i.hasNext(); )
						 * { AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) i.next(); if
						 * (lEveNot.getCodTipoProvvedimento() != null &&
						 * !lEveNot.getCodTipoProvvedimento().equals("03")) { if (lAnnMod != null &&
						 * lAnnMod.getDataReclusioneDa() != null && lAnnMod.getDataReclusioneA() != null) {
						 * lAnnMod.setBeneficio("N"); } else { lAnnMod.setBeneficio("S"); } }
						 * lAnnMod.calcolaStringaArresto(); lAnnMod.calcolaStringaReclusione();
						 *
						 * lStatEve.add(new TreeModel(lAnnMod)); } } }
						 */
					} // if sul null dell'Evento
				} // FINE WHILE SUGLI EVENTI
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

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneController.appendStatoEsecuzione: ", daoEx);
			throw new F3BException("StatoEsecuzioneController.appendStatoEsecuzione: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneController.appendStatoEsecuzione: ", sqe);
			throw new F3BException("StatoEsecuzioneController.appendStatoEsecuzione: " + sqe);
		} finally {
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
			cleanup(lRicConSqlDao);
		}
	}

	/**
	 * getDataSopsensione - Restituisce la data sospensione. Metodo utile per quei provvedimenti interruttivi
	 * che al posto della data di emissione cdevono visualizzare la data di sospensione.
	 *
	 * @return Data inizio esecuzione
	 */
	Date getDataSospensione(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;
		SospensioneSqlDAO lSospSql = null;
		Date lReturnDate = null;
		try {
			lConn = getDBConnection();
			lSospSql = new SospensioneSqlDAO(lConn);

			lSospSql = new SospensioneSqlDAO(lConn);
			lSospSql.ricercaSospensioneByFascicolo(aIdFascicolo);
			SospensioneModel lSospMod = (SospensioneModel) lSospSql.getModelByKey();
			if (lSospMod != null)
				lReturnDate = lSospMod.getDataInizio();

			// fine lettura sospensione
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneController.appendStatoEsecuzione: ", sqe);
			throw new F3BException("StatoEsecuzioneController.appendStatoEsecuzione: " + sqe);
		} finally {
			cleanup(lSospSql);
			cleanup(lConn);
		}

		return lReturnDate;
	}

	/**
	 * get Anno Numero Sius
	 *
	 * @param aEveSorv
	 */
	void getAnnoNumeroSius(EventoSorveglianzaModel aEveSorv) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- getAnnoNumeroSius - di StatoEsecuzioneController");
		Connection lConn = null;
		StatoEsecuzioneSqlDAO lStatEsec = null;
		try {
			lConn = getDBConnection();
			lStatEsec = new StatoEsecuzioneSqlDAO(lConn);
			lStatEsec.ricercaAnnoNumeroSius(aEveSorv.getIdEvento(), aEveSorv.getCodTipoProvvedimento());
			lStatEsec.start();

			if (lStatEsec.next()) {
				aEveSorv.setAnnoRegistro(lStatEsec.getBigDecimal("ANNO"));
				aEveSorv.setNumeroRegistro(lStatEsec.getBigDecimal("NUMERO"));
				aEveSorv.setFlagElaborato(lStatEsec.getString("ELABORATO"));
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in getAnnoNumeroSius");
			ex.printStackTrace();
		} finally {
			try {
				cleanup(lStatEsec);
				cleanup(lConn);
			} catch (Exception eee) {
				siesLogger.error("Errore in getAnnoNumeroSius");
			}
		}
	}

}