package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.modulocumulo.dao.BeneficioCumuloDAO;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;

/*
 * MEV 16 CUMULO: aggiunta classe
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TitoloEsecutivoController extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * MEV 16 CUMULO: aggiunto metodo
	 */
	public void ExEstraiDatiAnalitici(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati della Pena Complessiva,
			// Sanzione Sostitutiva
			// e Sentenze Continuazione del fasciolo
			// ========================================================================
			ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo(dntsm, tcm, lConn);

			// ========================================================================
			// Estraggo i dati dei Reati del fasciolo
			// ========================================================================
			ExEstraiReatiPerCumulo(dntsm, tcm, lConn);

			// ========================================================================
			// Estraggo i dati dei Reati del fasciolo
			// ========================================================================
			ExEstraiCircostanzePerCumulo(dntsm, tcm, lConn);

			// ========================================================================
			// Estraggo i dati delle misure sicurezza del fasciolo
			// ========================================================================
			ExEstraiMisuraSicurezzaPerCumulo(dntsm, tcm, lConn);

			// ========================================================================
			// Estraggo i dati della Pena Accessoria del fasciolo
			// ========================================================================
			ExEstraiPenaAccessoriaPerCumulo(dntsm, tcm, lConn);

			// ========================================================================
			// Estraggo i dati dei Benefici Concessi del fasciolo
			// ========================================================================
			ExEstraiBeneficiConcessiPerCumulo(dntsm, tcm, lConn);

			// ===================================================================================
			// Estraggo i dati dei Benefici del fasciolo Revocati in altro Procedimento (REVOCHE)
			// ===================================================================================
			ExEstraiRevochePerCumulo(dntsm, tcm, lConn);

			// ========================================================================
			// Estraggo i dati delle Misure Cautelari del fasciolo
			// ========================================================================
			ExEstraiMisureCautelariPerCumulo(dntsm, tcm, lConn);

			if (aDBConnection == null)
				commit(lConn);
		} catch (F3BException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in fase di estrazioned dei dati analitici", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiDatiAnalitici! Non posso inserire: " + ex);
		} finally {
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	// ??? MISURE CAUTELARI ???
	private void ExEstraiMisureCautelariPerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		MisuraCautelareCumuloDAO lMisCautelareCumuloDAO = null;

		try {
			lMisCautelareCumuloDAO = new MisuraCautelareCumuloDAO(aDBConnection);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiMisureCautelariCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisCautelareCumuloDAO);
		}
	}

	private void ExEstraiRevochePerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		BeneficioCumuloDAO lBenCumDao = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;
		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		BeneficioCumuloSqlDAO lConCumSqlDao = null;
		BeneficioCumuloDAO lConcessioneCumDao = null;
		BeneficioModel lBenMod = null;
		BeneficioCumuloModel bcm = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			if (dntsm.getRevocaModel() != null && dntsm.getRevocaModel().size() > 0) {
				Iterator itxBn = dntsm.getRevocaModel().iterator();
				while (itxBn.hasNext()) {
					lBenMod = (BeneficioModel) itxBn.next();
					// Prendo solo le REVOCHE (TipoBeneficio = "R")
					if (lBenMod != null && lBenMod.getIdBeneficio() != null
							&& lBenMod.getCodNaturaBeneficio().compareTo("R") == 0) {
						bcm = new BeneficioCumuloModel();
						bcm = PreparaBeneficio(lBenMod);
						bcm.setIdBeneficioOrigine(lBenMod.getIdBeneficio());
						// Se la Revoca_Origine è collegata ad un altro Beneficio tramite 'Ben_Id_Beneficio',
						// anche la Revoca_Cumulo avrà un collegamento ad un Beneficio tramite
						// 'Ben_Id_Beneficio_Cumulo'
						if (lBenMod.getBenIdBeneficio() != null) {
							bcm.setBenIdBeneficioOrig(lBenMod.getBenIdBeneficio());
							lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
							lBenCumSqlDao
									.ricercaBeneficioCumuloByKeyBeneficioOrig(lBenMod.getBenIdBeneficio());
							BeneficioCumuloModel lBeneficioCum = (BeneficioCumuloModel) lBenCumSqlDao
									.getModelByKey();
							if (lBeneficioCum != null && lBeneficioCum.getIdBeneficioCumulo() != null) {
								bcm.setBenIdBeneficioCumulo(lBeneficioCum.getIdBeneficioCumulo());
							}
							cleanup(lBenCumSqlDao);
						}

						// Se la Revoca_Origine riporta il riferimento alla Sentenza di Concessione
						// Beneficio_Origine tramite 'Rif_Id_Provvedimento',
						// anche la Revoca_Cumulo avrà il riferimento al TITOLO_CUMULATO di concessione
						// Beneficio_Cumulo tramite 'Tit_Id_Titolo_Cumulo_Collegato'
						if (lBenMod.getRifIdProvvedimento() != null) {
							// Cerco titolo_Cumulato By Id_Sentenza_origine e Istruttiria;
							lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
							TitoloCumulatoModel lTitoloMod = new TitoloCumulatoModel();
							lTitoloMod.setIdSentenzaOrigine(lBenMod.getRifIdProvvedimento());
							lTitoloMod.setIstrIdIstruttoriaCumulo(tcm.getIstrIdIstruttoriaCumulo());
							lTitSqlDao.ricercaTitoloCumulato(lTitoloMod);
							lTitoloMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();
							if (lTitoloMod != null && lTitoloMod.getIdTitoloCumulato() != null) {
								// nella REVOCA_CUMULO riporto il dato relativo AL TITOLO_CUMULATO che aveva
								// CONCESSIO il BENEFICIO
								bcm.setTitIdTitoloCumulatoCollegato(lTitoloMod.getIdTitoloCumulato());
							}
							// cerco Il beneficio_Cumulo_Concesso con Tit_id_Titolo_Cumulato appena trovato
							if (lTitoloMod != null && lTitoloMod.getIdTitoloCumulato() != null) {
								lConCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
								Vector lConcessi = new Vector();
								BeneficioCumuloModel lModel = new BeneficioCumuloModel();
								lModel.setCodTipoBeneficio(lBenMod.getCodTipoBeneficio());
								lModel.setCodNaturaBeneficio("C");
								lModel.setTitIdTitoloCumulato(lTitoloMod.getIdTitoloCumulato());
								if (lBenMod.getCodTipoBeneficio().compareTo("03") == 0)
									lModel.setCodDpr(lBenMod.getCodDpr());
								lConCumSqlDao.ricercaBeneficioCumulo(lModel);
								lConcessi = new Vector(lConCumSqlDao.getModels());
								// In realtà dovrei trovarne solo uno
								if (lConcessi != null && lConcessi.size() > 0) {
									// Nel Beneficio_Concesso riporto il dato relativo AL TITOLO_CUMULATO che
									// lo sta REVOCANDO
									lModel = (BeneficioCumuloModel) lConcessi.get(0);
									lModel.setTitIdTitoloCumulatoCollegato(tcm.getIdTitoloCumulato());
									lConcessioneCumDao = new BeneficioCumuloDAO(lConn);
									lConcessioneCumDao.setDAOFromModelForUpdate(lModel);
									lConcessioneCumDao.update();
									lConcessioneCumDao.stop();
								}
							}
						}

						// Finisco di inserire la Revoca_Cumulo
						bcm.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
						bcm.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
						bcm.setDataInserimento(tcm.getDataInserimento());
						bcm.setFlagStato("E");
						bcm.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());

						// Insert;
						lBenCumDao = new BeneficioCumuloDAO(lConn);
						lBenCumDao.setDAOFromModel(bcm);
						BigDecimal lKey_01 = lBenCumDao.insert();
						bcm.setIdBeneficioCumulo(lKey_01);
						lBenCumDao.stop();

						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("--XX-- Insert REVOCA bcm = "+bcm);
					}

				} // Chiude Iterator
			}
			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiRevochePerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiRevochePerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lBenCumDao);
			cleanup(lTitSqlDao);
			cleanup(lConCumSqlDao);
			cleanup(lConcessioneCumDao);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private void ExEstraiBeneficiConcessiPerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		BeneficioSqlDAO lBenSqlDao = null;
		BeneficioCumuloDAO lBenCumDao = null;
		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		PenaAccessoriaSqlDAO lpenAccSDao = null;
		PenaAccessoriaCumuloDAO lpenAccCumDao = null;
		PenaAccessoriaCumuloSqlDAO lPAcCumSqlDao = null;
		TipologiaOrarioDAO lTipOrDao = null;
		BeneficioModel lBenMod = null;
		BeneficioCumuloModel lBenCumMod = null;
		PenaAccessoriaModel lPenMod = null;
		PenaAccessoriaCumuloModel lPenCumMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			if (dntsm.getBeneficioModel() != null && dntsm.getBeneficioModel().size() > 0) {
				Iterator itxBn = dntsm.getBeneficioModel().iterator();
				while (itxBn.hasNext()) {
					lBenMod = (BeneficioModel) itxBn.next();
					// Prendo solo i CONCESSI (TipoBeneficio = "C")
					if (lBenMod != null /* && lBenMod.getIdBeneficio() != null è sempre null */
							&& lBenMod.getCodNaturaBeneficio().compareTo("C") == 0) {
						if (lBenMod.getCodTipoBeneficio().compareTo("02") == 0
								&& lBenMod.getBenIdBeneficio() != null) {
							// NON lo copio perchè trattasi di NON MENZIONE Collegata a SOSPENSIONE, e viene
							// copiato dopo
						} else {
							lBenCumMod = new BeneficioCumuloModel();
							lBenCumMod = PreparaBeneficio(lBenMod);
							lBenCumMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
							lBenCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
							lBenCumMod.setDataInserimento(tcm.getDataInserimento());
							lBenCumMod.setFlagStato("E");
							lBenCumMod.setIdBeneficioOrigine(lBenMod.getIdBeneficio());
							lBenCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
							// Insert;
							lBenCumDao = new BeneficioCumuloDAO(lConn);
							lBenCumDao.setDAOFromModel(lBenCumMod);
							BigDecimal lKey_01 = lBenCumDao.insert();
							lBenCumDao.stop();
							if (lBenMod.getCodTipoSospSubordinata().compareTo("08") == 0) {
								// Cerco Tipologia Orario
								ITipologiaOrario CtrlTip = SIEPLookupRemote.getTipologiaOrarioRemote();
								Vector lTipologia = CtrlTip
										.ExRicercaTipologiaOrarioByIdBeneficio(lBenMod.getIdBeneficio());
								if (lTipologia != null && lTipologia.size() > 0) {
									Iterator itx = lTipologia.iterator();
									while (itx.hasNext()) {
										TipologiaOrarioModel lTipolo = (TipologiaOrarioModel) itx.next();
										TipologiaOrarioModel NewTipolo = new TipologiaOrarioModel();
										NewTipolo.setAlleOre(lTipolo.getAlleOre());
										NewTipolo.setDalleOre(lTipolo.getDalleOre());
										NewTipolo.setCodNumGiorno(lTipolo.getCodNumGiorno());
										NewTipolo.setDatFinCumUltSanzioni(lTipolo.getDatFinCumUltSanzioni());
										NewTipolo.setDescrNumGiorno(lTipolo.getDescrNumGiorno());
										NewTipolo.setEnteIncaricato(lTipolo.getEnteIncaricato());
										NewTipolo.setCodOperatoreInserimento(
												lTipolo.getCodOperatoreInserimento());
										NewTipolo
												.setCodUfficioInserimento(lTipolo.getCodUfficioInserimento());
										NewTipolo.setDataInserimento(lTipolo.getDataInserimento());
										NewTipolo.setBenIdBeneficio(null);
										NewTipolo.setBenIdBeneficioCumulo(lKey_01);
										// Insert
										lTipOrDao = new TipologiaOrarioDAO(lConn);
										lTipOrDao.setDAOFromModel(NewTipolo);
										lTipOrDao.insert();
										lTipOrDao.stop();
									}
								}
							}

							// Amnistia/Indulto
							if (lBenMod.getCodTipoBeneficio().compareTo("03") == 0
									|| lBenMod.getCodTipoBeneficio().compareTo("04") == 0) {
								// Cerco eventuale pena Accessoria
								lpenAccSDao = new PenaAccessoriaSqlDAO(lConn);
								lpenAccSDao.ricercaPenaAccessoriaByBenIdBeneficio(lBenMod.getIdBeneficio());
								lPenMod = (PenaAccessoriaModel) lpenAccSDao.getModelByKey();
								if (lPenMod != null && lPenMod.getIdPenaAccessoria() != null) {
									// Cerco Pena_Acceessoria_Cumulo nata da Pena_Accessoria
									lPAcCumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
									lPAcCumSqlDao.ricercaPenaAccessoriaCumuloByKeyPAOrigine(
											lPenMod.getIdPenaAccessoria(), tcm.getIdTitoloCumulato());
									lPenCumMod = (PenaAccessoriaCumuloModel) lPAcCumSqlDao.getModelByKey();
									if (lPenCumMod != null
											&& lPenCumMod.getIdPenaAccessoriaCumulo() != null) {
										lPenCumMod.setBenIdBeneficioCumulo(lKey_01);
										lpenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);
										lpenAccCumDao.setDAOFromModelForUpdate(lPenCumMod);
										lpenAccCumDao
												.selCondizioneUpdate(lPenCumMod.getIdPenaAccessoriaCumulo());
										lpenAccCumDao.update();
										lpenAccCumDao.stop();
									}
								}
							}

							// Sospensione Condizionale
							if (lBenMod.getCodTipoBeneficio().compareTo("01") == 0) {
								// cerco un eventuale Beneficio Non_Menzione collegato
								lBenSqlDao = new BeneficioSqlDAO(lConn);
								lBenSqlDao.ricercaBeneficioByBenIdBeneficio(lBenMod.getIdBeneficio());
								BeneficioModel lBen = (BeneficioModel) lBenSqlDao.getModelByKey();
								if (lBen != null && lBen.getIdBeneficio() != null) {
									if (lBen.getCodTipoBeneficio().compareTo("02") == 0) {
										lBenCumMod = new BeneficioCumuloModel();
										lBenCumMod = PreparaBeneficio(lBen);
										lBenCumMod
												.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
										lBenCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
										lBenCumMod.setDataInserimento(tcm.getDataInserimento());
										lBenCumMod.setFlagStato("E");
										lBenCumMod.setIdBeneficioOrigine(lBen.getIdBeneficio());
										lBenCumMod.setBenIdBeneficioOrig(lBen.getBenIdBeneficio());
										lBenCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
										lBenCumMod.setBenIdBeneficioCumulo(lKey_01);
										// Insert;
										lBenCumDao = new BeneficioCumuloDAO(lConn);
										lBenCumDao.setDAOFromModel(lBenCumMod);
										lBenCumDao.insert();
										lBenCumDao.stop();
									}
								}
							}

							// Si cerca di collegare la concessione SOSPENSIONE CONDIZIONALE
							// ad una eventuale revoca su uno dei titoli già a sistema
							if (lBenMod.getCodTipoBeneficio().compareTo("01") == 0) {
								// Cerca sui titoli in istruttoria se presente un record che revoca il
								// beneficio
								siesLogger.debug("Ricerco le revoche delle sospensive già in istruttoria");
								lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
								lBenCumSqlDao.ricercaBeneficioCumuloByIdIstruttoria(
										tcm.getIstrIdIstruttoriaCumulo());
								Vector<BeneficioCumuloModel> lListaBeneficiIstr = new Vector<BeneficioCumuloModel>(
										lBenCumSqlDao.getModels());
								lBenCumSqlDao.stop();
								for (BeneficioCumuloModel lBenIstrRev : lListaBeneficiIstr) {
									if ("R".equals(lBenIstrRev.getCodNaturaBeneficio()) // R = Revoca
											// "01" = Sospensione condizionale
											&& "01".equals(lBenIstrRev.getCodTipoBeneficio())
											// C = Cancellato
											&& !"C".equals(lBenIstrRev.getFlagStato())) {
										siesLogger.debug("Trovata revoca " + lBenIstrRev);
										if (lBenIstrRev.isStessoTitolo(tcm)) {
											siesLogger.debug(
													"La revoca si riferisce proprio al beneficio che sto inserendo. Aggiorno i collegamenti.");
											// Aggiorno il record della revoca con l'id del titolo di
											// concessione che sto caricando
											lBenCumDao.setTitIdTitoloCumulatoCollegato(
													tcm.getIdTitoloCumulato());
											lBenCumDao
													.setCondizioneUpdate(lBenIstrRev.getIdBeneficioCumulo());
											lBenCumDao.update();
											lBenCumDao.stop();
											// Aggiorno il record corrente della concessione con l'id del
											// titolo di revoca
											lBenCumDao.setTitIdTitoloCumulatoCollegato(
													lBenIstrRev.getTitIdTitoloCumulato());
											lBenCumDao.setCondizioneUpdate(lKey_01);
											lBenCumDao.update();
											lBenCumDao.stop();
										}
									}
								}
							}
						}
					}
				}
			}
			if (aDBConnection == null)
				commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiBeneficiConcessiPerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiBeneficiConcessiPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lpenAccSDao);
			cleanup(lpenAccCumDao);
			cleanup(lBenSqlDao);
			cleanup(lBenCumDao);
			cleanup(lBenCumSqlDao);
			cleanup(lPAcCumSqlDao);
			cleanup(lTipOrDao);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private void ExEstraiPenaAccessoriaPerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		PenaAccessoriaCumuloDAO lpenAccCumDao = null;
		PenaAccessoriaModel lPenMod = null;
		PenaAccessoriaCumuloModel lPenCumMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			Iterator i = dntsm.getPeneAccessorieModel().iterator();
			while (i.hasNext()) {
				lPenMod = (PenaAccessoriaModel) i.next();
				lPenCumMod = new PenaAccessoriaCumuloModel();
				lPenCumMod.setCodTipoPenaAccessoria(lPenMod.getCodTipoPenaAccessoria());
				lPenCumMod.setDescrAltrePA(lPenMod.getDescrAltrePA());
				lPenCumMod.setDurata(lPenMod.getDurata());
				lPenCumMod.setNumAnni(lPenMod.getNumAnni());
				lPenCumMod.setNumMesi(lPenMod.getNumMesi());
				lPenCumMod.setNumGiorni(lPenMod.getNumGiorni());
				lPenCumMod.setBenIdBeneficioOrig(lPenMod.getBenIdBeneficio());
				lPenCumMod.setNote(lPenMod.getNote());
				lPenCumMod.setFlagStato("E"); // E = Estratto
				lPenCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
				lPenCumMod.setIdPenaAccessoriaOrigine(lPenMod.getIdPenaAccessoria());
				lPenCumMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
				lPenCumMod.setDataInserimento(tcm.getDataInserimento());
				lPenCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
				lPenCumMod.setFlagDatiFinali("S"); // per default le PA vengono iscritte in Dati Finali Cumulo
				// Inserisco
				lpenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);
				lpenAccCumDao.setDAOFromModel(lPenCumMod);
				lpenAccCumDao.insert();
				lpenAccCumDao.stop();
			}
			if (aDBConnection == null)
				commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiPenaAccessoriaPerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiPenaAccessoriaPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lpenAccCumDao);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private void ExEstraiMisuraSicurezzaPerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaCumuloDAO lMisCumDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisCumSqlDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisCumSqlDaoXX = null;
		FascMsToFascSiepSqlDAO lFascMsSqlDao = null;
		FascMsToFascSiepModel lFascMsModel = null;
		UfficioSqlDAO lUffSqldao = null;
		UfficioModel lUffMod = null;
		MisuraSicurezzaCumuloModel lMisSicCumMod = null;
		MisuraSicurezzaModel lMisSicMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			Iterator iter = dntsm.getMisuraSicurezzaModel().iterator();
			while (iter.hasNext()) {
				lMisSicMod = (MisuraSicurezzaModel) iter.next();
				lMisSicCumMod = new MisuraSicurezzaCumuloModel();
				String lNatura = lMisSicMod.getCodNatura();
				if (lMisSicMod.getCodNatura().equals("-")) {
					if (lMisSicMod.getCodTipo() != null && lMisSicMod.getCodTipo().compareTo("-") != 0) {
						lNatura = trattaDato(lMisSicMod.getCodTipo());
					}
				}
				lMisSicCumMod.setCodNatura(lNatura);
				lMisSicCumMod.setCodTipo(lMisSicMod.getCodTipo());
				lMisSicCumMod.setNumAnni(lMisSicMod.getNumAnni());
				lMisSicCumMod.setNumMesi(lMisSicMod.getNumMesi());
				lMisSicCumMod.setNumGiorni(lMisSicMod.getNumGiorni());
				lMisSicCumMod.setAnnoReg38(lMisSicMod.getAnnoReg38());
				lMisSicCumMod.setNumReg38(lMisSicMod.getNumReg38());
				lMisSicCumMod.setFlagStato("E"); // E = Estratto
				lMisSicCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
				lMisSicCumMod.setIdMisuraSicurezzaOrigine(lMisSicMod.getIdMisuraSicurezza());
				lMisSicCumMod.setFlagAnnullaMisura(lMisSicMod.getFlagAnnullaMisura());
				lMisSicCumMod.setDataFineValidita(lMisSicMod.getDataFineValidita());
				lMisSicCumMod.setIstDetIdIstitutoDetenzione(lMisSicMod.getIstDetIdIstitutoDetenzione());
				lMisSicCumMod.setLuogoEsecuzioneMisura(lMisSicMod.getLuogoEsecuzioneMisura());
				if (lMisSicMod.getMisIdMisuraSicurezza() != null)
					lMisSicCumMod.setMisIdMisuraSicurezzaOrigine(lMisSicMod.getMisIdMisuraSicurezza());
				lMisSicCumMod.setMisIdMisuraSicurezzaCumulo(null); // viene aggiornato più avanti
				lMisSicCumMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
				lMisSicCumMod.setDataInserimento(tcm.getDataInserimento());
				lMisSicCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());

				// Se la Misura di Classe I è Iscritta ad un Classe IV vanno riempiti Ulteriori campi:
				// ANNO e NUMERO Fascicolo di classe IV; COD AUTORITA' e LUOGO AUTORITA' EMITTENTI del
				// Fascicolo di classe IV;
				lFascMsSqlDao = new FascMsToFascSiepSqlDAO(lConn);
				lFascMsSqlDao.ricercaByKeyFascEsecuzione(null);
				lFascMsModel = (FascMsToFascSiepModel) lFascMsSqlDao.getModelByKey();
				if (lFascMsModel != null && lFascMsModel.getIdFascMsToFascSiep() != null) {
					lMisSicCumMod.setAnnoFascicoloSiepIV(lFascMsModel.getChiaveAnnoSiepCollegato());
					lMisSicCumMod.setNumeroFascicoloSiepIV(lFascMsModel.getChiaveProgrSiepCollegato());
					if (lFascMsModel.getChiaveUfficioSiepCollegato() != null) {
						lMisSicCumMod.setCodAutoritaEmittenteIV(lFascMsModel.getChiaveUfficioSiepCollegato());
						lUffSqldao = new UfficioSqlDAO(lConn);
						lUffSqldao.ricercaUfficioByCod(lFascMsModel.getChiaveUfficioSiepCollegato());
						lUffMod = (UfficioModel) lUffSqldao.getModelByKey();
						if (lUffMod != null && lUffMod.getCodUfficio() != null) {
							lMisSicCumMod.setCodLuogoEmittenteIV(lUffMod.getCodComune());
							lMisSicCumMod.setDescrAutoritaEmittenteIV(lUffMod.getDescrTipoUfficio());
							lMisSicCumMod.setDescrluogoEmittenteIV(lUffMod.getDescrComune());
						}
					}
				}
				if (lMisSicCumMod.getDataFineValidita() == null) {
					lMisSicCumMod.setFlagStatoMisura("V"); // per default è valida
				} else {
					// dovrebbe essere solo Classe IV per ora porto il trattino l'utente
					// dovrà indicare il vero stato (revocata/trasformata)
					lMisSicCumMod.setFlagStatoMisura("-");
				}
				// per defult tutte le Misure fanno parte del PROVV --> Dati_Finali_Cumulo
				lMisSicCumMod.setFlagDatiFinali("S");
				// Inserisco
				lMisCumDao = new MisuraSicurezzaCumuloDAO(lConn);
				lMisCumDao.setDAOFromModel(lMisSicCumMod);
				lMisCumDao.insert();
				lMisCumDao.stop();
			}

			// Serve un secondo passaggio per valorizzare, se possibile e se esiste, l'attributo
			// 'MisIdMisuraSicurezzaCumulo'
			// con il corrispondente valore di 'IdMisuraSicurezzaCumulo'
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Secondo giro con le Misure trovate");
			while (iter.hasNext()) {
				lMisSicMod = (MisuraSicurezzaModel) iter.next();
				if (lMisSicMod.getMisIdMisuraSicurezza() != null) {
					BigDecimal KeyMisCumulo = null;
					lMisCumSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
					lMisCumSqlDao.ricercaMisuraSicurezzaCumuloByIdMisuraOrigine(
							lMisSicMod.getMisIdMisuraSicurezza(), tcm.getIdTitoloCumulato());
					MisuraSicurezzaCumuloModel lMisCum = (MisuraSicurezzaCumuloModel) lMisCumSqlDao
							.getModelByKey();
					if (lMisCum != null && lMisCum.getIdMisuraSicurezzaCumulo() != null)
						KeyMisCumulo = lMisCum.getIdMisuraSicurezzaCumulo();
					lMisCumSqlDaoXX = new MisuraSicurezzaCumuloSqlDAO(lConn);
					lMisCumSqlDaoXX.ricercaMisuraSicurezzaCumuloByMisIdMisuraOrigine(
							lMisSicMod.getMisIdMisuraSicurezza(), tcm.getIdTitoloCumulato());
					MisuraSicurezzaCumuloModel lMisCumXX = (MisuraSicurezzaCumuloModel) lMisCumSqlDaoXX
							.getModelByKey();
					if (lMisCumXX != null && lMisCumXX.getIdMisuraSicurezzaCumulo() != null) {
						lMisCumXX.setMisIdMisuraSicurezzaCumulo(KeyMisCumulo);
						lMisCumDao = new MisuraSicurezzaCumuloDAO(lConn);
						lMisCumDao.setDAOFromModelForUpdate(lMisCumXX);
						lMisCumDao.selCondizioneUpdate(lMisCumXX.getIdMisuraSicurezzaCumulo());
						lMisCumDao.update();
						lMisCumDao.stop();
					}
				}
			}
			if (aDBConnection == null)
				commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiMisuraSicurezzaPerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiMisuraSicurezzaPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisCumDao);
			cleanup(lMisCumSqlDao);
			cleanup(lMisCumSqlDaoXX);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private void ExEstraiCircostanzePerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;
		CircostanzaCumuloDAO lCirCumDao = null;
		CircostanzaCumuloModel lCirCumuloMod = null;

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input (Metodo ExEstraiCircostanzeCumulo) ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione (Metodo ExEstraiCircostanzeCumulo)");
				lConn = getDBConnection();
			}

			Iterator iter = dntsm.getCircostanzeModel().iterator();
			while (iter.hasNext()) {
				CircostanzaModel lCircostanza = (CircostanzaModel) iter.next();
				lCirCumuloMod = new CircostanzaCumuloModel(lCircostanza);
				lCirCumuloMod.setFlagStato("E"); // E = Estratto
				lCirCumuloMod.setMotivoModifica("");
				lCirCumuloMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
				lCirCumuloMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
				lCirCumuloMod.setDataInserimento(tcm.getDataInserimento());
				lCirCumuloMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
				// Inserisco
				siesLogger.debug("Inserisco Circostanza cumulo: " + lCirCumuloMod);
				lCirCumDao = new CircostanzaCumuloDAO(lConn);
				lCirCumDao.setDAOFromModel(lCirCumuloMod);
				lCirCumDao.insert();
				lCirCumDao.stop();
			}
			if (aDBConnection == null)
				commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiCircostanzePerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lCirCumDao);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private void ExEstraiReatiPerCumulo(DatiNscToSiesModel dntsm, TitoloCumulatoModel tcm,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		ReatoCumuloDAO lReaCumDao = null;
		ReatoCumuloModel lReaCumPrincipale = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input (Metodo ExEstraiReatiCumulo) ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione (Metodo ExEstraiReatiCumulo)");
				lConn = getDBConnection();
			}

			if (dntsm.getReatoModel() != null && dntsm.getReatoModel().size() > 0) {
				Iterator irm = dntsm.getReatoModel().iterator();
				while (irm.hasNext()) {
					ReatoModel lReaPrincipale = (ReatoModel) irm.next();
					lReaCumPrincipale = new ReatoCumuloModel(lReaPrincipale);
					lReaCumPrincipale.setFlagStato("E"); // E = Estratto
					lReaCumPrincipale.setMotivoModificaNote("");
					lReaCumPrincipale.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
					lReaCumPrincipale.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
					lReaCumPrincipale.setDataInserimento(tcm.getDataInserimento());
					lReaCumPrincipale.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
					// Inserisco
					lReaCumDao = new ReatoCumuloDAO(lConn);
					lReaCumDao.setDAOFromModel(lReaCumPrincipale);
					lReaCumDao.insert();
					lReaCumDao.stop();
				}
			}
			if (aDBConnection == null)
				commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiReatiPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lReaCumDao);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private void ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo(DatiNscToSiesModel dntsm,
			TitoloCumulatoModel tcm, Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		PenaComplessivaCumuloDAO lPenCumDao = null;
		SanzioneSostitutivaCumuloDAO lSanzCumDao = null;
		ContinuazioneCumuloDAO lContCumDao = null;
		ContinuazioneCumuloSqlDAO lContCumSqlDAo = null;
		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;
		PenaComplessivaCumuloModel lPenCumMod = null;
		SanzioneSostitutivaCumuloModel lSanCumMod = null;
		ContinuazioneCumuloModel lContCumMod = null;
		TitoloCumulatoSqlDAO lTitCumSqlDao = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Utilizzo connessione in input (Metodo ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo)");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Apro nuova connessione (Metodo ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo)");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati della Pena Complessiva del fasciolo Cumulato
			// - - -> MODULO PENA_COMPLESSIVA / PENA_COMPLESSIVA_CUMULO
			// ========================================================================
			if (dntsm.getPenaComplessivaModel() != null && dntsm.getPenaComplessivaModel().size() > 0) {
				Iterator ipcm = dntsm.getPenaComplessivaModel().iterator();
				while (ipcm.hasNext()) {
					lPenMod = (PenaComplessivaModel) ipcm.next();
					if (lPenMod != null) {
						lPenCumMod = new PenaComplessivaCumuloModel(lPenMod);
						lPenCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
						lPenCumMod.setFlagStato("E"); // E = Estratto
						lPenCumMod.setMotivoModifica(null);
						lPenCumMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
						lPenCumMod.setDataInserimento(tcm.getDataInserimento());
						lPenCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
						// ======================================================================
						// Estraggo i dati della Sanzione Sostitutiva del fasciolo Cumulato
						// - - -> MODULO SANZIONE_SOSTITUTIVA / SANZIONE_SOST_CUM
						// ======================================================================
						if (dntsm.getSanzioneSostitutivaModel() != null
								&& dntsm.getSanzioneSostitutivaModel().size() > 0) {
							Iterator issm = dntsm.getSanzioneSostitutivaModel().iterator();
							while (issm.hasNext()) {
								lSanMod = (SanzioneSostitutivaModel) issm.next();
								if (lSanMod != null) {
									lSanCumMod = new SanzioneSostitutivaCumuloModel(lSanMod);
									lSanCumMod.setFlagStato("E"); // E = Estratto
									lSanCumMod.setMotivoModifica(null);
									lSanCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
									lSanCumMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
									lSanCumMod.setDataInserimento(tcm.getDataInserimento());
									lSanCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
								}
							}
						}

						// ========================================================================
						// Estraggo i dati della Sentenze in Continuazione del fasciolo Cumulato
						// - - -> MODULO CONTINUAZIONE / CONTINUAZIONE_CUMULO
						// ========================================================================
						Vector VContCum = new Vector();
						if (dntsm.getContinuazioniModel() != null
								&& dntsm.getContinuazioniModel().size() > 0) {
							Iterator icm = dntsm.getContinuazioniModel().iterator();
							// Recupero i titoli in istruttoria per verificare se uno di essi coincide
							// con la continuazione
							lTitCumSqlDao = new TitoloCumulatoSqlDAO(lConn);
							Vector<TitoloCumulatoModel> lListaTitoli = null;
							if (icm.hasNext()) {
								// Recupero i titoli solo se esiste almeno una continuazione
								lTitCumSqlDao
										.ricercaTitoloCumulatoByIstruttoria(tcm.getIstrIdIstruttoriaCumulo());
								lTitCumSqlDao.start();
								lListaTitoli = new Vector(lTitCumSqlDao.getModels());
								lTitCumSqlDao.stop();
							}
							while (icm.hasNext()) {
								ContinuazioneModel lContMod = (ContinuazioneModel) icm.next();
								lContCumMod = new ContinuazioneCumuloModel(lContMod);
								lContCumMod.setFlagStato("E"); // E = Estratto
								lContCumMod.setMotivoModifica(null);
								lContCumMod.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
								lContCumMod.setCodOperatoreInserimento(tcm.getCodOperatoreInserimento());
								lContCumMod.setDataInserimento(tcm.getDataInserimento());
								lContCumMod.setCodUfficioInserimento(tcm.getCodUfficioInserimento());
								// Verifico se la Continuazione Coincide con uno dei titoli già in istruttoria
								Iterator<TitoloCumulatoModel> lTitoliIter = lListaTitoli.iterator();
								while (lTitoliIter.hasNext()) {
									TitoloCumulatoModel lTitolo = lTitoliIter.next();
									if (lContCumMod.isStessoTitolo(lTitolo)) {
										lContCumMod.setTitIdTitoloCumulatoCont(lTitolo.getIdTitoloCumulato());
										break;
									}
								}
								VContCum.add(lContCumMod);
							}
						}

						// ======================================================================
						// Inserimento Pena Complessiva/Sanzione Sostitutiva/Sentenze in Continuazione
						// ======================================================================
						lPenCumDao = new PenaComplessivaCumuloDAO(lConn);
						lPenCumDao.setDAOFromModel(lPenCumMod);
						BigDecimal lKeyPenaComplessiva = null;
						lKeyPenaComplessiva = lPenCumDao.insert();
						lPenCumMod.setIdPenaComplessivaCum(lKeyPenaComplessiva);
						// Inserimento Sanzione Sostitutiva
						if (lSanCumMod != null) {
							lSanzCumDao = new SanzioneSostitutivaCumuloDAO(lConn);
							lSanCumMod.setPcIdPenaComplessivaCum(lKeyPenaComplessiva);
							lSanzCumDao.setDAOFromModel(lSanCumMod);
							BigDecimal lKeySanzioneSostitutiva = null;
							lKeySanzioneSostitutiva = lSanzCumDao.insert();
							lSanCumMod.setIdSanzioneSostitutivaCum(lKeySanzioneSostitutiva);
						}

						// Inserimento Continuazioni
						if (VContCum.size() > 0) {
							lContCumDao = new ContinuazioneCumuloDAO(lConn);
							lContCumSqlDAo = new ContinuazioneCumuloSqlDAO(lConn);
							Iterator lIter = VContCum.iterator();
							while (lIter.hasNext()) {
								ContinuazioneCumuloModel lItemConCum = (ContinuazioneCumuloModel) lIter
										.next();
								// Gestione Progressivo
								BigDecimal lProgr = lContCumSqlDAo
										.getProgressivoContinuazione(lKeyPenaComplessiva);
								lItemConCum.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));
								lItemConCum.setPcIdPenaComplessivaCum(lKeyPenaComplessiva);
								lContCumDao.setDAOFromModel(lItemConCum);
								lContCumDao.insert();
								lContCumDao.stop();
							}
						}
						if (aDBConnection == null)
							commit(lConn);
					}
				}
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo. Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"TitoloEsecutivoController.ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo. Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lPenCumDao);
			cleanup(lSanzCumDao);
			cleanup(lContCumDao);
			cleanup(lContCumSqlDAo);
			cleanup(lTitCumSqlDao);
			if (aDBConnection == null)
				cleanup(lConn);
		}
	}

	private BeneficioCumuloModel PreparaBeneficio(BeneficioModel lBenMod) throws F3BException {

		BeneficioCumuloModel bcm = new BeneficioCumuloModel();
		bcm.setCodTipoBeneficio(lBenMod.getCodTipoBeneficio());
		bcm.setCodSottotipoBeneficio(lBenMod.getCodSottotipoBeneficio());
		bcm.setCodNaturaBeneficio(lBenMod.getCodNaturaBeneficio());
		bcm.setCodDpr(lBenMod.getCodDpr());
		bcm.setCodTipoSospSubordinata(lBenMod.getCodTipoSospSubordinata());
		bcm.setDescrDpr(lBenMod.getDescrDpr());
		bcm.setDescrLuogoEmittente(lBenMod.getDescrLuogoAutoritaEmittente());
		bcm.setDescrNaturaBeneficio(lBenMod.getDescrNaturaBeneficio());
		bcm.setDescrSottotipoBeneficio(lBenMod.getDescrSottotipoBeneficio());
		bcm.setDescrTipoAutoEmittente(lBenMod.getDescrTipoAutoritaEmittente());
		bcm.setDescrTipoBeneficio(lBenMod.getDescrTipoBeneficio());
		bcm.setDescrTipoProvvedimento(lBenMod.getDescrTipoProvvedimento());
		bcm.setDescrTipoSospSubordinata(lBenMod.getDescrTipoSospSubordinata());
		bcm.setFlagFrequenzaSettimanale(lBenMod.getFlagFrequenzaSettimanale());
		bcm.setImportoAmmenda(lBenMod.getImportoAmmenda());
		bcm.setImportoMulta(lBenMod.getImportoMulta());
		bcm.setNote(lBenMod.getNote());
		bcm.setNumAnniAdempimento(lBenMod.getNumAnniAdempimento());
		bcm.setNumAnniArresto(lBenMod.getNumAnniArresto());
		bcm.setNumAnniReclusione(lBenMod.getNumAnniReclusione());
		bcm.setNumAnniSospensione(lBenMod.getNumAnniSospensione());
		bcm.setNumGiorniAdempimento(lBenMod.getNumGiorniAdempimento());
		bcm.setNumGiorniArresto(lBenMod.getNumGiorniArresto());
		bcm.setNumGiorniPrestazione(lBenMod.getNumGiorniPrestazione());
		bcm.setNumGiorniReclusione(lBenMod.getNumGiorniReclusione());
		bcm.setNumMesiAdempimento(lBenMod.getNumMesiAdempimento());
		bcm.setNumMesiArresto(lBenMod.getNumMesiArresto());
		bcm.setNumMesiPrestazione(lBenMod.getNumMesiPrestazione());
		bcm.setNumMesiReclusione(lBenMod.getNumMesiReclusione());
		bcm.setNumOreSettimanali(lBenMod.getNumOreSettimanali());
		bcm.setRifAnnoProvvedimento(lBenMod.getRifAnnoProvvedimento());
		bcm.setRifCodLuogoEmittente(lBenMod.getRifCodLuogoAutoEmittente());
		bcm.setRifCodTipoAutoEmittente(lBenMod.getRifCodTipoAutoEmittente());
		bcm.setRifCodTipoProvvedimento(lBenMod.getRifCodTipoProvvedimento());
		bcm.setRifDataIrrevocabilita(lBenMod.getRifDataIrrevocabilita());
		bcm.setRifDataProvvedimento(lBenMod.getRifDataProvvedimento());
		bcm.setRifIdProvvedimento(lBenMod.getRifIdProvvedimento());
		bcm.setRifNumeroProvvedimento(lBenMod.getRifNumeroProvvedimento());
		bcm.setRifNumSezioneAutoEmittente(lBenMod.getRifNumSezioneAutoEmittente());

		return bcm;
	}

	private String trattaDato(String lTipo) throws F3BException {

		String lNat = "";

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lModel = new DecodificheModel();
		lModel.setContesto("TIPO_MISURA_SICUREZZA");
		Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));
		DecodificheModel lModelVec = null;
		Iterator Ite1 = lVec.iterator();
		while (Ite1.hasNext()) {
			lModelVec = (DecodificheModel) Ite1.next();
			if (lModelVec.getCode().compareTo(lTipo) == 0)
				lNat = lModelVec.getFiltro();
		}
		return lNat;
	}

}