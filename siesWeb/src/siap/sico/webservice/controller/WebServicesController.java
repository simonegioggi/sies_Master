package siap.sico.webservice.controller;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import it.mig.sies.type.CHIAVIDocument;
import it.mig.sies.type.DATIOPERAZIONEDocument;
import it.mig.sies.type.ESITODocument;
import it.mig.sies.type.esecuzione.DATIRISPOSTAESECUZIONEDocument;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.soggettocertificato.controller.SoggettoCertificatoController;
import siap.sico.soggettocertificato.dao.SoggettoCertificatoDAO;
import siap.sico.soggettocertificato.model.SoggettoCertificatoModel;
import siap.sico.trasmissione.dao.TrasmissioniDAO;
import siap.sico.trasmissione.model.TrasmissioniModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.webservice.dao.WSFascicoloSoggettoSentenzaSqlDAO;
import siap.sico.webservice.dao.WebserviceSqlDAO;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.beneficio.dao.BeneficioDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.continuazione.dao.ContinuazioneDAO;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: WebServicesController
 * </p>
 * <p>
 * Description: Classe controller per l'inserimento Sentenza+Soggetto+Fascicolo+Reati+ecc.
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WebServicesController extends SiapController implements IWebServices {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * MEV 16: aggiunto parametro di passaggio per inserire solo sentenza e cumulo, NO sogg e fasc per
	 * gestione fascicolo coinvolto nel cumulo. Modificato anche il codice. MEV 16 CUMULO: aggiunto parametro
	 * di passaggio per gestione dei titoli da associare al cumulo
	 *
	 * @param aDatiNscToSiesModel
	 * @param aWriteSentenza
	 * @param isForCumulo
	 * @param idFascicoloSiep
	 * @param lCodUfficioIns
	 * @param lCodOperatoreIns
	 * @param idIstruttoriaCumulo
	 * @throws F3BException
	 */
	public void ExInserisciFascicoloDaNsc(DatiNscToSiesModel aDatiNscToSiesModel, String aWriteSentenza,
			boolean isForCumulo, String idFascicoloSiep, String lCodOperatoreIns, String lCodUfficioIns,
			String idIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		SentenzaDAO lSenDao = null;
		SoggettoDAO lSogDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		ReatoDAO lReatoDao = null;
		PenaComplessivaDAO lPenaComplDao = null;
		CircostanzaDAO lCircostanzaDao = null;
		MisuraSicurezzaDAO lMisuraSicDao = null;
		PenaAccessoriaDAO lPenaAccessoriaDao = null;
		SanzioneSostitutivaDAO lSanzioneSostitutivaDao = null;
		BeneficioDAO lBeneficioDao = null;
		ContinuazioneDAO lContinuazioneDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		SoggettoCertificatoDAO lSogCerDao = null;

		try {
			lConn = getDBTransaction();

			lSenDao = new SentenzaDAO(lConn);
			lSogDao = new SoggettoDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			lReatoDao = new ReatoDAO(lConn);
			lPenaComplDao = new PenaComplessivaDAO(lConn);
			lCircostanzaDao = new CircostanzaDAO(lConn);
			lMisuraSicDao = new MisuraSicurezzaDAO(lConn);
			lPenaAccessoriaDao = new PenaAccessoriaDAO(lConn);
			lSanzioneSostitutivaDao = new SanzioneSostitutivaDAO(lConn);
			lBeneficioDao = new BeneficioDAO(lConn);
			lContinuazioneDao = new ContinuazioneDAO(lConn);
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lSogCerDao = new SoggettoCertificatoDAO(lConn);

			// MEV 16 CUMULO: cambiata gestione --> non iscrivo più un nuovo soggetto e fascicolo di
			// classe VII, bensì un titolo cumulato come per l'Iscrizione Manuale Titolo!
			if (!isForCumulo) {
				BigDecimal lIDSentenza;
				if (aWriteSentenza.equals("S")) {
					// Insert Sentenza (Dati Titolo Esecutivo - PRIMO/SECONDO GRADO - CASSAZIONE)
					lSenDao.setDAOFromModel(aDatiNscToSiesModel.getSentenzaModel());
					lIDSentenza = lSenDao.insert();
					lSenDao.stop();
				} else
					lIDSentenza = aDatiNscToSiesModel.getSentenzaModel().getIdSentenza();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ExInserisciFascicoloDaNsc --> ID_SENTENZA: " + lIDSentenza);

				// Insert Soggetto
				lSogDao.setDAOFromModel(aDatiNscToSiesModel.getSoggettoModel());
				BigDecimal lIDSoggetto = lSogDao.insert();
				lSogDao.stop();
				aDatiNscToSiesModel.getSoggettoModel().setIdSoggetto(lIDSoggetto);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ExInserisciFascicoloDaNsc --> ID_SOGGETTO: " + lIDSoggetto);
				// Scrivo il record SoggettoCertificato per ospitare il Certificato
				// Penale del Soggetto
				if (aDatiNscToSiesModel.getSoggettoModel().getIdSoggetto() != null) {
					aDatiNscToSiesModel.getSoggettoCertificatoModel()
							.setSogIdSoggetto(aDatiNscToSiesModel.getSoggettoModel().getIdSoggetto());
					lSogCerDao.setDAOFromModel(aDatiNscToSiesModel.getSoggettoCertificatoModel());
					BigDecimal lIDSoggCert = lSogCerDao.insert();
					lSogCerDao.stop();
					aDatiNscToSiesModel.getSoggettoCertificatoModel().setIdSoggettoCertificato(lIDSoggCert);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_SOGGETTO_CERTIFICATO: " + lIDSoggCert);
					// Scrittura Campo BLOB Certificato
					if (aDatiNscToSiesModel.getSoggettoCertificatoModel().getDocBlobCertificato() != null) {
						lSogCerDao = new SoggettoCertificatoDAO(lConn);
						lSogCerDao.setDAOFromModelForUpdateBlob(
								aDatiNscToSiesModel.getSoggettoCertificatoModel());
						lSogCerDao.selCondizioneUpdate(lIDSoggCert);
						lSogCerDao.update();
						lSogCerDao.stop();
					}
				}

				// -----> Determino CHIAVE_PROGR --------
				lFasDaoSql.getProgressivoFascicoloSiep(aDatiNscToSiesModel.getFascicoloSiepModel());
				lFasDaoSql.start();
				int lMaxProgr = 0;
				if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
					lMaxProgr = lFasDaoSql.getInt("aMAX");
				lFasDaoSql.stop();
				int lTipoProgr = aDatiNscToSiesModel.getFascicoloSiepModel().getTipoProgressivo();
				if (lMaxProgr == 0) {
					if (lTipoProgr == 1) {
						aDatiNscToSiesModel.getFascicoloSiepModel().setChiaveProgr(new BigDecimal(1));
					} else {
						aDatiNscToSiesModel.getFascicoloSiepModel()
								.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
					}
				} else {
					aDatiNscToSiesModel.getFascicoloSiepModel().setChiaveProgr(new BigDecimal(lMaxProgr + 1));
				}

				// Insert Fascicolo
				// Paolo Cherubini 17/01/2012 controllo se il flag altra causa è null ci metto 'N'
				if (aDatiNscToSiesModel.getFascicoloSiepModel().getFlagAltraCausa() == null)
					aDatiNscToSiesModel.getFascicoloSiepModel().setFlagAltraCausa("N");
				// fine Paolo Cherubini 17/01/2012
				aDatiNscToSiesModel.getFascicoloSiepModel().setSenIdSentenza(lIDSentenza);
				aDatiNscToSiesModel.getFascicoloSiepModel().setSogIdSoggetto(lIDSoggetto);
				lFasDao.setDAOFromModel(aDatiNscToSiesModel.getFascicoloSiepModel());
				BigDecimal lIDFascicolo = lFasDao.insert();
				lFasDao.stop();
				aDatiNscToSiesModel.getFascicoloSiepModel().setIdFascicoloSiep(lIDFascicolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ExInserisciFascicoloDaNsc --> ID_FASCICOLO: " + lIDFascicolo);

				// Insert Reato del Titolo Esecutivo
				Vector lListaReati = aDatiNscToSiesModel.getReatoModel();
				for (int i = 0; i <= lListaReati.size() - 1; i++) {
					ReatoModel lReatoModel = (ReatoModel) lListaReati.elementAt(i);
					lReatoModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lReatoDao.setDAOFromModel(lReatoModel);
					BigDecimal lIDReato = lReatoDao.insert();
					lReatoDao.stop();
					lReatoModel.setIdReato(lIDReato);
					// Scrivi L'ID del REATO per passarlo nell'XML di Risposta
					((ReatoModel) aDatiNscToSiesModel.getReatoModel().elementAt(i)).setIdReato(lIDReato);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_REATO: " + lIDReato);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.info("ExInserisciFascicoloDaNsc --> Prog Reato: " + lReatoModel.getProgrReato());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> Prog Circostanza: "
							+ lReatoModel.getProgrCircostanza());
				}

				// Insert Pena Complessiva del Titolo Esecutivo
				Vector lListaPenaCompl = aDatiNscToSiesModel.getPenaComplessivaModel();
				BigDecimal lIDPenaComplessiva = new BigDecimal(0);
				for (int i = 0; i <= lListaPenaCompl.size() - 1; i++) {
					PenaComplessivaModel lPenaComplModel = (PenaComplessivaModel) lListaPenaCompl
							.elementAt(i);
					lPenaComplModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lPenaComplDao.setDAOFromModel(lPenaComplModel);
					lIDPenaComplessiva = lPenaComplDao.insert();
					lPenaComplDao.stop();
					lPenaComplModel.setIdPenaComplessiva(lIDPenaComplessiva);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.info("ExInserisciFascicoloDaNsc --> ID_PENACOMPLESSIVA: " + lIDPenaComplessiva);
				}

				// Insert Circostanza del Titolo Esecutivo
				Vector lListaCircostanza = aDatiNscToSiesModel.getCircostanzeModel();
				for (int i = 0; i <= lListaCircostanza.size() - 1; i++) {
					CircostanzaModel lCircostanzaModel = (CircostanzaModel) lListaCircostanza.elementAt(i);
					lCircostanzaModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lCircostanzaDao.setDAOFromModel(lCircostanzaModel);
					BigDecimal lIDCircostanza = lCircostanzaDao.insert();
					lCircostanzaDao.stop();
					lCircostanzaModel.setIdCircostanza(lIDCircostanza);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_CIRCOSTANZA: " + lIDCircostanza);
				}

				// Insert Misura Sicurezza del Titolo Esecutivo
				Vector lListaMisuraSicurezza = aDatiNscToSiesModel.getMisuraSicurezzaModel();
				for (int i = 0; i <= lListaMisuraSicurezza.size() - 1; i++) {
					MisuraSicurezzaModel lMisuraSicurezzaModel = (MisuraSicurezzaModel) lListaMisuraSicurezza
							.elementAt(i);
					lMisuraSicurezzaModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lMisuraSicDao.setDAOFromModel(lMisuraSicurezzaModel);
					BigDecimal lIDMisuraSicurezza = lMisuraSicDao.insert();
					lMisuraSicDao.stop();
					lMisuraSicDao.setIdMisuraSicurezza(lIDMisuraSicurezza);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.info("ExInserisciFascicoloDaNsc --> ID_MISURA_SICUREZZA: " + lIDMisuraSicurezza);
				}

				// Insert Pena Accessoria del Titolo Esecutivo
				Vector lListaPenaAccessoria = aDatiNscToSiesModel.getPeneAccessorieModel();
				for (int i = 0; i <= lListaPenaAccessoria.size() - 1; i++) {
					PenaAccessoriaModel lPenaAccessoriaModel = (PenaAccessoriaModel) lListaPenaAccessoria
							.elementAt(i);
					lPenaAccessoriaModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lPenaAccessoriaDao.setDAOFromModel(lPenaAccessoriaModel);
					BigDecimal lIDPenaAccessoria = lPenaAccessoriaDao.insert();
					lPenaAccessoriaDao.stop();
					lPenaAccessoriaDao.setIdPenaAccessoria(lIDPenaAccessoria);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_PENA_ACCESSORIA: " + lIDPenaAccessoria);
				}

				// Insert SANZIONE SOSTITUTIVA del Titolo Esecutivo (1 solo record)
				Vector lSanzioneSostitutiva = aDatiNscToSiesModel.getSanzioneSostitutivaModel();
				for (int i = 0; i <= lSanzioneSostitutiva.size() - 1; i++) {
					SanzioneSostitutivaModel lSanzioneSostitutivaModel = (SanzioneSostitutivaModel) lSanzioneSostitutiva
							.elementAt(i);
					// Paolo Cherubini 07/03/2011 se c'è il - significa che non
					// esiste la sanzione non la carico
					if (!lSanzioneSostitutivaModel.getCodTipoSanzione().equals("-")) {
						lSanzioneSostitutivaModel.setPenComIdPenaComplessiva(lIDPenaComplessiva);
						lSanzioneSostitutivaDao.setDAOFromModel(lSanzioneSostitutivaModel);
						BigDecimal lIDSanzioneSostitutiva = lSanzioneSostitutivaDao.insert();
						lSanzioneSostitutivaDao.stop();
						lSanzioneSostitutivaDao.setIdSanzioneSostitutiva(lIDSanzioneSostitutiva);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("ExInserisciFascicoloDaNsc --> ID_SANZIONE_SOSTITUTIVA: "
								+ lIDSanzioneSostitutiva);
					}
				}

				// Insert Beneficio
				Vector lListaBenefici = aDatiNscToSiesModel.getBeneficioModel();
				for (int i = 0; i <= lListaBenefici.size() - 1; i++) {
					BeneficioModel lBeneficioModel = (BeneficioModel) lListaBenefici.elementAt(i);
					lBeneficioModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lBeneficioDao.setDAOFromModel(lBeneficioModel);
					BigDecimal lIDBeneficio = lBeneficioDao.insert();
					lBeneficioDao.stop();
					lBeneficioDao.setIdBeneficio(lIDBeneficio);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_BENEFICIO: " + lIDBeneficio);
				}

				// Insert Revoche
				Vector lListaRevoche = aDatiNscToSiesModel.getRevocaModel();
				for (int i = 0; i <= lListaRevoche.size() - 1; i++) {
					BeneficioModel lBeneficioModel = (BeneficioModel) lListaRevoche.elementAt(i);
					lBeneficioModel.setFasSieIdFascicoloSiep(lIDFascicolo);
					lBeneficioDao.setDAOFromModel(lBeneficioModel);
					BigDecimal lIDBeneficio = lBeneficioDao.insert();
					lBeneficioDao.stop();
					lBeneficioDao.setIdBeneficio(lIDBeneficio);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_REVOCA: " + lIDBeneficio);
				}

				// Insert CONTINUAZIONE (PENE AGGIUNTIVE) del Titolo Esecutivo
				Vector lContinuazione = aDatiNscToSiesModel.getContinuazioniModel();
				for (int i = 0; i <= lContinuazione.size() - 1; i++) {
					ContinuazioneModel lContinuazioneModel = (ContinuazioneModel) lContinuazione.elementAt(i);
					lContinuazioneModel.setPenComIdPenaComplessiva(lIDPenaComplessiva);
					lContinuazioneDao.setDAOFromModel(lContinuazioneModel);
					BigDecimal lIDContinuazione = lContinuazioneDao.insert();
					lContinuazioneDao.stop();
					lContinuazioneDao.setIdContinuazione(lIDContinuazione);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ExInserisciFascicoloDaNsc --> ID_CONTINUAZIONE: " + lIDContinuazione);
				}

				// Insert Stato Procedimento
				aDatiNscToSiesModel.getStatoProcedimentoModel().setFasSieIdFascicoloSiep(lIDFascicolo);
				lStatoProcDao.setDAOFromModel(aDatiNscToSiesModel.getStatoProcedimentoModel());
				lStatoProcDao.insert();
				lStatoProcDao.stop();
				// aDatiNscToSiesModel.getStatoProcedimentoModel().setProgressivo(lSequence);
			} else {
				// MEV 16 CUMULO: operazione da fare solo per il titolo esecutivo da associare al cumulo
				// gestione del cumulo --> da "ActInserisciFascicoloInIstruttoria"
				IModuloCumulo iModuloCumulo = SIEPLookupRemote.getModuloCumuloRemote();
				DatiOperazioneModel dom = new DatiOperazioneModel();
				dom.setCodOperatore(lCodOperatoreIns);
				dom.setCodUfficio(lCodUfficioIns);
				dom.setData(DateUtils.getSysDate());
				dom.setDescrUfficio("");
				// MEV 16: aggiunto set di proprietà
				aDatiNscToSiesModel.getSentenzaModel().setFlagVisibilita("N");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ExInserisciFascicoloDaNsc --> ExInserisciTitoloInIstruttoria x CUMULO");
				// 01=Presa in carico, 02=Manuale, 03=NSC
				iModuloCumulo.ExInserisciTitoloInIstruttoria(new BigDecimal(idIstruttoriaCumulo),
						aDatiNscToSiesModel, dom, "03", lConn);
			}
			commit(lConn);
		} catch (DAOException e) {
			rollback(lConn);
			throw new SICOException("WebServcesController.ExInserisciFascicoloDaNsc : " + e);
		} catch (Exception e) {
			rollback(lConn);
			throw new SICOException("WebServcesController.ExInserisciFascicoloDaNsc : " + e);
		} finally {
			cleanup(lSenDao);
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lReatoDao);
			cleanup(lPenaComplDao);
			cleanup(lCircostanzaDao);
			cleanup(lMisuraSicDao);
			cleanup(lPenaAccessoriaDao);
			cleanup(lSanzioneSostitutivaDao);
			cleanup(lBeneficioDao);
			cleanup(lStatoProcDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lContinuazioneDao);
			cleanup(lSogCerDao);
			cleanup(lConn);
		}
	}

	/**
	 * MEV 16: aggiunto parametri di passaggio per differenziare collegato al cumulo
	 */
	public Vector ExRicercaSoggettoWebServices(DatiNscToSiesModel aDatiNscToSiesModel, boolean isForCumulo,
			String idIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;
		Vector lSoggettoFascicolo = new Vector();
		WebserviceSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new WebserviceSqlDAO(lConn);
			// MEV 16 CUMULO: aggiunto parametri di passaggio x gestione cumulo
			lSqlDao.ricercaSoggettoWebServices(aDatiNscToSiesModel, isForCumulo, idIstruttoriaCumulo);
			lSqlDao.start(); // Esegue la Query
			// Ciclo su Recordset
			while (lSqlDao.next()) {
				// MEV 16 CUMULO: aggiunto parametro di passaggio x gestione cumulo
				DatiNscToSiesModel vDatiNscToSiesModel = (DatiNscToSiesModel) lSqlDao
						.getRisultatoRicercaSoggettoWebServices(isForCumulo);
				lSoggettoFascicolo.add(vDatiNscToSiesModel);
			}
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SICOException(
					"WebServicesController.ExRicercaSoggettoWebServices: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SICOException(
					"WebServicesController.ExRicercaSoggettoWebServices: Non posso leggere  : " + e);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lSoggettoFascicolo;
	}

	public ComuneModel ExRicercaProvinciaSedeGiudiziaria(String aCodIstatComuneNascita) throws F3BException {

		Connection lConn = null;
		ComuneModel lComuneModel = new ComuneModel();
		WebserviceSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new WebserviceSqlDAO(lConn);
			lSqlDao.ricercaProvinciaSedeGiudiziaria(aCodIstatComuneNascita); // Imposto Istruzione Sql
			lSqlDao.start(); // Esegue la Query
			if (lSqlDao.next()) // Controllo su Recordset
				lComuneModel = lSqlDao.getRisultatoRicercaProvinciaSedeGiudiziaria();
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SICOException(
					"WebServicesController.ExRicercaProvinciaSedeGiudiziaria: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SICOException(
					"WebServicesController.ExRicercaProvinciaSedeGiudiziaria: Non posso leggere  : " + e);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lComuneModel;
	}

	public UfficioModel ExRicercaCodiceUfficio(String aCodTipoUfficio, String aCodComune)
			throws F3BException {

		Connection lConn = null;
		UfficioModel lUfficioModel = new UfficioModel();
		WebserviceSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new WebserviceSqlDAO(lConn);
			// Imposto Istruzione Sql
			lSqlDao.ricercaCodUfficio(aCodTipoUfficio, aCodComune);
			lSqlDao.start(); // Esegue la Query

			if (lSqlDao.next()) // Controllo su Recordset
				lUfficioModel = lSqlDao.getRisultatoRicercaCodUfficio();
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SICOException(
					"WebServicesController.ExRicercaCodiceUfficio: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SICOException(
					"WebServicesController.ExRicercaCodiceUfficio: Non posso leggere  : " + e);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lUfficioModel;
	}

	public Vector ExRicercaReatiInContinuazione(long aFascicoloSIEP) throws F3BException {

		Connection lConn = null;
		Vector lReatoVector = new Vector();
		WebserviceSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new WebserviceSqlDAO(lConn);
			lSqlDao.RicercaReatiInContinuazione(aFascicoloSIEP); // Imposto Istruzione Sql
			lSqlDao.start(); // Esegue la Query
			// Ciclo su Recordset
			while (lSqlDao.next()) {
				ReatoModel lReatoModel = new ReatoModel();
				lReatoModel = lSqlDao.getRisultatoRicercaReatiInContinuazione();
				lReatoVector.add(lReatoModel);
			}
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SICOException(
					"WebServicesController.ExRicercaReatiInContinuazione: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SICOException(
					"WebServicesController.ExRicercaReatiInContinuazione: Non posso leggere  : " + e);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lReatoVector;
	}

	public Vector ExRicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
			String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lFascSoggSent = new Vector();
		WSFascicoloSoggettoSentenzaSqlDAO lWSFasSoggSenSqlDao = null;

		try {
			lConn = getDBConnection();
			lWSFasSoggSenSqlDao = new WSFascicoloSoggettoSentenzaSqlDAO(lConn);
			lWSFasSoggSenSqlDao.RicercaTitoloEsecutivoTrasferito(dataRicercaInizio, dataRicercaFine,
					lDestinazione, lStatoFascicolo, lCodUfficioUtenteConnesso, aPage);
			lWSFasSoggSenSqlDao.start();
			while (lWSFasSoggSenSqlDao.next()) {
				lFascSoggSent.add(lWSFasSoggSenSqlDao.getModel());
			}
			lWSFasSoggSenSqlDao.stop();
			if (lFascSoggSent.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WebServicesController.ExRicercaTitoloEsecutivoTrasferito: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWSFasSoggSenSqlDao);
			cleanup(lConn);
		}
		return lFascSoggSent;
	}

	public BigDecimal ExGetCountRicercaTitoloEsecutivoTrasferito(Date dataRicercaInizio, Date dataRicercaFine,
			String lDestinazione, String lStatoFascicolo, String lCodUfficioUtenteConnesso)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		WSFascicoloSoggettoSentenzaSqlDAO lWSFasSoggSenSqlDao = null;

		try {
			lConn = getDBConnection();
			lWSFasSoggSenSqlDao = new WSFascicoloSoggettoSentenzaSqlDAO(lConn);
			lWSFasSoggSenSqlDao.getCountRicercaTitoloEsecutivoTrasferito(dataRicercaInizio, dataRicercaFine,
					lDestinazione, lStatoFascicolo, lCodUfficioUtenteConnesso);
			lWSFasSoggSenSqlDao.start();
			lWSFasSoggSenSqlDao.next();
			lCount = lWSFasSoggSenSqlDao.getBigDecimal("HowManyRecords");
			lWSFasSoggSenSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WebServicesController.ExGetCountRicercaTitoloEsecutivoTrasferito: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lWSFasSoggSenSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lCount;
	}

	public void ExScritturaChiaviNsc(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;
		FascicoloSiepDAO lFasDao = null;
		ReatoDAO lReaDao = null;
		TrasmissioniDAO lTraDao = null;

		try {
			conn = getDBConnection();

			// ----> Scrittura Key Soggetto NSC su Tabella Soggetto SIES
			SoggettoModel lSoggettoModel = new SoggettoModel();
			lSoggettoModel.setIdSoggetto(new BigDecimal(adatiChiavi.getKASIES()));
			lSoggettoModel.setKeySoggNsc(new BigDecimal(adatiChiavi.getKANSC()));

			lSogDao = new SoggettoDAO(conn);
			lSogDao.setDAOFromModelForUpdateKeyNsc(lSoggettoModel);
			lSogDao.selCondizioneUpdate(lSoggettoModel.getIdSoggetto());
			lSogDao.update();

			// ----> Scrittura Key Provvedimento NSC su Tabella Fascicolo SIES
			FascicoloSiepModel lFascicoloSiepModel = new FascicoloSiepModel();
			lFascicoloSiepModel.setIdFascicoloSiep(new BigDecimal(adatiChiavi.getKPSIES()));
			lFascicoloSiepModel.setKeyProvvNsc(new BigDecimal(adatiChiavi.getKPNSC()));

			lFasDao = new FascicoloSiepDAO(conn);
			lFasDao.setDAOFromModelForUpdateKeyNsc(lFascicoloSiepModel);
			lFasDao.selCondizioneUpdate(lFascicoloSiepModel.getIdFascicoloSiep());
			lFasDao.update();

			// ----> Scrittura Key Reato NSC su Tabella REATO SIES
			if (adatiChiavi.getArrayChiaviReati() != null) {
				for (int i = 0; i <= adatiChiavi.getArrayChiaviReati().getCHIAVIREATOArray().length
						- 1; i++) {
					ReatoModel lReatoModel = new ReatoModel();
					lReatoModel.setIdReato(new BigDecimal(
							adatiChiavi.getArrayChiaviReati().getCHIAVIREATOArray(i).getKRSIES()));
					lReatoModel.setKeyReatoNsc(new BigDecimal(
							adatiChiavi.getArrayChiaviReati().getCHIAVIREATOArray(i).getKRNSC()));

					lReaDao = new ReatoDAO(conn);
					lReaDao.setDAOFromModelForUpdateKeyReatoNsc(lReatoModel);
					lReaDao.setCondizioneUpdate(lReatoModel.getIdReato());
					lReaDao.update();
				}
			}

			// ---> Scrittura Record Trasmissione
			TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();

			lTrasmissioniModel.setTipoTrasmissione("01"); // Provvedimenti
															// Principali
			lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
			lTrasmissioniModel.setEsitoTrasmissione(adatiEsito.getCODICE().toString());
			lTrasmissioniModel.setCodErrore("");
			lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString());
			lTrasmissioniModel.setDestinazione("NSC");

			if (adatiEsito.getCODICE().toString().equals("0")) {
				lTrasmissioniModel.setChiaveNscProv(new BigDecimal(adatiChiavi.getKPNSC()));
				lTrasmissioniModel.setChiaveNscSogg(new BigDecimal(adatiChiavi.getKANSC()));
				lTrasmissioniModel.setChiaveSiesFasc(new BigDecimal(adatiChiavi.getKPSIES()));
				lTrasmissioniModel.setChiaveSiesSogg(new BigDecimal(adatiChiavi.getKASIES()));
			}

			lTrasmissioniModel.setChiaveAnno(lAnnoFascicolo);
			lTrasmissioniModel.setChiaveProgr(lNumeroFascicolo);

			lTrasmissioniModel.setCodOperatoreInserimento(lUteMod.getUserId());
			lTrasmissioniModel.setDataInserimento(DateUtils.getSysDate());
			lTrasmissioniModel.setCodUfficioInserimento(lUteMod.getUfficioUtente().getCodUfficio());

			// Scrivo TRASMISSIONE
			lTraDao = new TrasmissioniDAO(conn);
			lTraDao.setDAOFromModel(lTrasmissioniModel);
			/* BigDecimal lSequence = */lTraDao.insert();
			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("WebServicesController.ExScritturaChiaviNsc: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lReaDao);
			cleanup(lTraDao);
			cleanup(conn);
		}
	}

	// MEV 16 CUMULO: per il cumulo eseguo un'altra count
	public BigDecimal ExGetCountCercaFascSogg(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc,
			boolean isForCumulo) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		WSFascicoloSoggettoSentenzaSqlDAO lWSFasSoggSenSqlDao = null;

		try {
			lConn = getDBConnection();
			lWSFasSoggSenSqlDao = new WSFascicoloSoggettoSentenzaSqlDAO(lConn);
			lWSFasSoggSenSqlDao.getCountCercaFascSogg(lKeyProvvNsc, lKeySoggNsc, isForCumulo);
			lWSFasSoggSenSqlDao.start();
			lWSFasSoggSenSqlDao.next();
			lCount = lWSFasSoggSenSqlDao.getBigDecimal("HowManyRecords");
			lWSFasSoggSenSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WebServicesController.ExGetCountCercaFascSogg: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWSFasSoggSenSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lCount;
	}

	public FascicoloSiepModel ExPrelevaAnnoNumeroFas(BigDecimal lKeyProvvNsc, BigDecimal lKeySoggNsc)
			throws F3BException {

		Connection lConn = null;
		// BigDecimal lCount=new BigDecimal(0);
		WSFascicoloSoggettoSentenzaSqlDAO lWSFasSoggSenSqlDao = null;
		FascicoloSiepModel lFascicoloSiepModel = new FascicoloSiepModel();

		try {
			lConn = getDBConnection();

			lWSFasSoggSenSqlDao = new WSFascicoloSoggettoSentenzaSqlDAO(lConn);
			lWSFasSoggSenSqlDao.RicercaAnnoNumeroFas(lKeyProvvNsc, lKeySoggNsc);
			lWSFasSoggSenSqlDao.start(); // Esegue la Query

			if (lWSFasSoggSenSqlDao.next()) // Controllo su Recordset
				lFascicoloSiepModel = lWSFasSoggSenSqlDao.getRicercaAnnoNumeroFas();
			lWSFasSoggSenSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"WebServicesController.ExPrelevaAnnoNumeroFas: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lWSFasSoggSenSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lFascicoloSiepModel;
	}

	public void ExCancellazioneChiaviNsc(CHIAVIDocument.CHIAVI adatiChiavi,
			DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione, ESITODocument.ESITO adatiEsito,
			BigDecimal lAnnoFascicolo, BigDecimal lNumeroFascicolo, UtenteModel lUteMod) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;
		FascicoloSiepDAO lFasDao = null;
		ReatoDAO lReaDao = null;
		TrasmissioniDAO lTraDao = null;

		try {
			conn = getDBConnection();
			// ----> Cancellazione Key Soggetto NSC su Tabella Soggetto SIES
			SoggettoModel lSoggettoModel = new SoggettoModel();
			lSoggettoModel.setIdSoggetto(new BigDecimal(adatiChiavi.getKASIES()));
			lSoggettoModel.setKeySoggNsc(null);
			lSogDao = new SoggettoDAO(conn);
			lSogDao.setDAOFromModelForUpdateKeyNsc(lSoggettoModel);
			lSogDao.selCondizioneUpdate(lSoggettoModel.getIdSoggetto());
			lSogDao.update();
			// ----> Cancellazione Key Provvedimento NSC su Tabella Fascicolo
			// SIES
			FascicoloSiepModel lFascicoloSiepModel = new FascicoloSiepModel();
			lFascicoloSiepModel.setIdFascicoloSiep(new BigDecimal(adatiChiavi.getKPSIES()));
			lFascicoloSiepModel.setKeyProvvNsc(null);
			lFasDao = new FascicoloSiepDAO(conn);
			lFasDao.setDAOFromModelForUpdateKeyNsc(lFascicoloSiepModel);
			lFasDao.selCondizioneUpdate(lFascicoloSiepModel.getIdFascicoloSiep());
			lFasDao.update();
			// ----> Cancellazione Key Reato NSC su Tabella REATO SIES
			if (adatiChiavi.getArrayChiaviReati() != null) {
				ReatoModel lReatoModel = new ReatoModel();
				lReatoModel.setKeyReatoNsc(null);
				lReaDao = new ReatoDAO(conn);
				lReaDao.setDAOFromModelForUpdateKeyReatoNsc(lReatoModel);
				lReaDao.setCondizioneIdSiep(lFascicoloSiepModel.getIdFascicoloSiep());
				lReaDao.update();
			}

			/*
			 * if (adatiChiavi.getArrayChiaviReati() != null) { for (int i=0; i<=
			 * adatiChiavi.getArrayChiaviReati().getCHIAVIREATOArray().length-1 ;i++) { ReatoModel lReatoModel
			 * = new ReatoModel(); lReatoModel.setIdReato(new BigDecimal (adatiChiavi.getArrayChiaviReati
			 * ().getCHIAVIREATOArray(i).getKRSIES())); lReatoModel.setKeyReatoNsc(null);
			 *
			 * lReaDao = new ReatoDAO(conn); lReaDao.setDAOFromModelForUpdateKeyReatoNsc(lReatoModel);
			 * lReaDao.setCondizioneUpdate(lReatoModel.getIdReato()); lReaDao.update(); } }
			 */

			// ---> Scrittura Record Trasmissione
			TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();
			lTrasmissioniModel.setTipoTrasmissione("01"); // Provvedimenti
			lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
			lTrasmissioniModel.setEsitoTrasmissione(adatiEsito.getCODICE().toString());
			lTrasmissioniModel.setCodErrore("");
			lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString());
			lTrasmissioniModel.setDestinazione("NSC");
			if (adatiEsito.getCODICE().toString().equals("0")) {
				lTrasmissioniModel.setChiaveNscProv(null);
				lTrasmissioniModel.setChiaveNscSogg(null);
				lTrasmissioniModel.setChiaveSiesFasc(null);
				lTrasmissioniModel.setChiaveSiesSogg(null);
			}
			lTrasmissioniModel.setChiaveAnno(lAnnoFascicolo);
			lTrasmissioniModel.setChiaveProgr(lNumeroFascicolo);
			lTrasmissioniModel.setCodOperatoreInserimento(lUteMod.getUserId());
			lTrasmissioniModel.setDataInserimento(DateUtils.getSysDate());
			lTrasmissioniModel.setCodUfficioInserimento(lUteMod.getUfficioUtente().getCodUfficio());
			// Scrivo TRASMISSIONE
			lTraDao = new TrasmissioniDAO(conn);
			lTraDao.setDAOFromModel(lTrasmissioniModel);
			/* BigDecimal lSequence = */lTraDao.insert();
			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("WebServicesController.ExCancellazioneChiaviNsc: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lReaDao);
			cleanup(lTraDao);
			cleanup(conn);
		}
	}

	public void ExScritturaChiaviNscUDS(it.mig.sies.type.esecuzione.CHIAVIDocument.CHIAVI adatiChiavi,
			it.mig.sies.type.esecuzione.DATIOPERAZIONEDocument.DATIOPERAZIONE adatiOperazione,
			it.mig.sies.type.esecuzione.ESITODocument.ESITO adatiEsito, BigDecimal lAnnoFascicolo,
			BigDecimal lNumeroFascicolo, UtenteModel lUteMod,
			DATIRISPOSTAESECUZIONEDocument.DATIRISPOSTAESECUZIONE aDatiRispostaEsecuzione)
			throws F3BException {

		Connection conn = null;
		EventoDAO lEveDao = null;
		SoggettoCertificatoDAO lSogCerDao = null;
		TrasmissioniDAO lTraDao = null;

		try {
			conn = getDBConnection();
			// ----> Scrittura Key Provvedimento Esecuzione UDS NSC su Tabella
			// Evento SIES
			EventoModel lEventoModel = new EventoModel();
			lEventoModel.setIdEvento(new BigDecimal(adatiChiavi.getKPESIES()));
			lEventoModel.setKeyEsecNsc(new BigDecimal(adatiChiavi.getKPENSC()));
			lEveDao = new EventoDAO(conn);
			lEveDao.setDAOFromModelForUpdateKeyNsc(lEventoModel);
			lEveDao.selCondizioneUpdate(lEventoModel.getIdEvento());
			lEveDao.update();
			// Scrittura Certificato
			if (aDatiRispostaEsecuzione.getCERTIFICATO() != null
					&& !aDatiRispostaEsecuzione.getCERTIFICATO().toString().equals("")) {
				SoggettoCertificatoController lCtrlSoggCert = new SoggettoCertificatoController();
				SoggettoCertificatoModel lSoggettoCertModel = new SoggettoCertificatoModel();
				lSoggettoCertModel.setSogIdSoggetto(new BigDecimal(adatiChiavi.getKASIES()));
				// BigDecimal lConta =
				// lCtrlSoggCert.ExGetCountSoggettoCertificato(lSoggettoCertModel);
				Vector lListaCert = lCtrlSoggCert.ExRicercaSoggettoCertificato(lSoggettoCertModel);
				if (lListaCert.size() == 0) {
					SoggettoCertificatoModel lSoggCertModel = new SoggettoCertificatoModel();
					lSoggCertModel.setCodOperatoreInserimento("nsc-" + lUteMod.getCodOperatoreInserimento());
					lSoggCertModel.setDataInserimento(DateUtils.getSysDate());
					lSoggCertModel.setCodUfficioInserimento(lUteMod.getUfficioUtente().getCodUfficio());
					lSoggCertModel
							.setCodOperatoreAggiornamento("nsc-" + lUteMod.getCodOperatoreInserimento());
					lSoggCertModel.setDataAggiornamento(DateUtils.getSysDate());
					lSoggCertModel.setCodUfficioAggiornamento(lUteMod.getUfficioUtente().getCodUfficio());
					// Caricamento CERTIFICATO (Campo BLOB) nel Model
					ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(
							aDatiRispostaEsecuzione.getCERTIFICATO());
					lSoggCertModel.setDocBlobCertificato(lByteArrayInput);
					// Prima Facciamo la Insert del Record
					// SoggettoCertificatoModel e poi Update
					lSogCerDao = new SoggettoCertificatoDAO(conn);
					lSogCerDao.setDAOFromModel(lSoggCertModel);
					BigDecimal lIDSoggCert = lSogCerDao.insert();
					lSogCerDao.stop();
					lSoggCertModel.setIdSoggettoCertificato(lIDSoggCert);
					// lSogCerDao = new SoggettoCertificatoDAO(conn);
					lSogCerDao.setDAOFromModelForUpdateBlob(lSoggCertModel);
					lSogCerDao.selCondizioneUpdate(lIDSoggCert);
					lSogCerDao.update();
					lSogCerDao.stop();
				} else {
					SoggettoCertificatoModel lSoggCertModel = (SoggettoCertificatoModel) lListaCert
							.elementAt(0);
					lSoggCertModel
							.setCodOperatoreAggiornamento("nsc-" + lUteMod.getCodOperatoreInserimento());
					lSoggCertModel.setDataAggiornamento(DateUtils.getSysDate());
					lSoggCertModel.setCodUfficioAggiornamento(lUteMod.getUfficioUtente().getCodUfficio());

					ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(
							aDatiRispostaEsecuzione.getCERTIFICATO());
					lSoggCertModel.setDocBlobCertificato(lByteArrayInput);

					lSogCerDao = new SoggettoCertificatoDAO(conn);
					lSogCerDao.setDAOFromModelForUpdateBlob(lSoggCertModel);
					lSogCerDao.selCondizioneUpdate(lSoggCertModel.getIdSoggettoCertificato());
					lSogCerDao.update();
					lSogCerDao.stop();
				}
			}
			// ---> Scrittura Record Trasmissione
			TrasmissioniModel lTrasmissioniModel = new TrasmissioniModel();
			lTrasmissioniModel.setTipoTrasmissione("02"); // Provvedimenti
			lTrasmissioniModel.setDataTrasmissione(DateUtils.getSysDate());
			lTrasmissioniModel.setEsitoTrasmissione(adatiEsito.getCODICE().toString());
			lTrasmissioniModel.setCodErrore("");
			lTrasmissioniModel.setTipoOperazione(adatiOperazione.getOPERAZIONE().toString());
			lTrasmissioniModel.setDestinazione("NSC");
			if (adatiEsito.getCODICE().toString().equals("0")) {
				lTrasmissioniModel.setChiaveNscProv(new BigDecimal(adatiChiavi.getKPENSC()));
				lTrasmissioniModel.setChiaveNscSogg(new BigDecimal(adatiChiavi.getKANSC()));
				lTrasmissioniModel.setChiaveSiesFasc(new BigDecimal(adatiChiavi.getKPESIES()));
				lTrasmissioniModel.setChiaveSiesSogg(new BigDecimal(adatiChiavi.getKASIES()));
			}
			lTrasmissioniModel.setChiaveAnno(lAnnoFascicolo);
			lTrasmissioniModel.setChiaveProgr(lNumeroFascicolo);
			lTrasmissioniModel.setCodOperatoreInserimento(lUteMod.getUserId());
			lTrasmissioniModel.setDataInserimento(DateUtils.getSysDate());
			lTrasmissioniModel.setCodUfficioInserimento(lUteMod.getUfficioUtente().getCodUfficio());
			// Scrivo TRASMISSIONE
			lTraDao = new TrasmissioniDAO(conn);
			lTraDao.setDAOFromModel(lTrasmissioniModel);
			/* BigDecimal lSequence = */lTraDao.insert();
			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("WebServicesController.ExScritturaChiaviNsc: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lTraDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSogCerDao);
			cleanup(conn);
		}
	}

}