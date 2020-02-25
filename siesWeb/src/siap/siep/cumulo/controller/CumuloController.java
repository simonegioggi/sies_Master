package siap.siep.cumulo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.cumulo.dao.CumuloDAO;
import siap.siep.cumulo.dao.CumuloSqlDAO;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.FascicoloSiepController;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penacumulo.dao.PenaCumuloDAO;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sentenza.controller.SentenzaController;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.ulterioresanzionecumulo.dao.UlterioreSanzioneCumuloDAO;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: CumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Cumulo
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
public class CumuloController extends SiapController implements ICumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Funzione di inserimento dei dati dei fascicoli cumulati: cumulo, fascicolo, sentenza
	 *
	 * @param aCumulo
	 *            - Model contenete i riferimenti al titolo esecutivo da cumulate (Fascicolo - Sentenza)
	 * @param aFasMod
	 *            - Solo se CUMULATO di altra BDI contiene i dati del fascicolo da iscrivere
	 * @param aSenMod
	 *            - Solo se CUMULATO di altra BDI contiene i dati della Sentenza/Decreto/Cumulo
	 * @return il model del CUMULO inserito
	 * @throws F3BException
	 */
	public CumuloModel ExInserisciCumulo(CumuloModel aCumulo, FascicoloSiepModel aFasMod,
			SentenzaModel aSenMod, BigDecimal IdFas0) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;
		SentenzaDAO lSenDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasSqlDao = null;
		SentenzaSqlDAO lSenSQL = null;

		CumuloModel lCumMod = null;
		lCumMod = new CumuloModel(aCumulo);
		BigDecimal lKeyFascicolo = null;
		BigDecimal lKeySentenza = null;

		try {
			lConn = getDBTransaction();
			lCumDao = new CumuloDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lSenDao = new SentenzaDAO(lConn);
			lSenSQL = new SentenzaSqlDAO(lConn);
			lFasSqlDao = new FascicoloSiepSqlDAO(lConn);

			// Inserimento SENTENZA (solo se fascicolo di altra BDI) e FASCICOLO
			if (aSenMod != null && aSenMod.getCodTipoProvvedimento() != null
					&& !aSenMod.getCodTipoProvvedimento().equals("")) {

				SentenzaModel lSenMod = null;
				lSenSQL.ricercaSentenza(aSenMod.getNumeroSentenza(), aSenMod.getAnnoSentenza(),
						aSenMod.getCodTipoAutoritaEmittente(), aSenMod.getCodLuogoEmittente());
				lSenMod = (SentenzaModel) lSenSQL.getModelByKey();
				if (lSenMod != null) {
					throw new SIEPException(SIEPException.USER_MESSAGE, "La sentenza "
							+ lSenMod.getAnnoSentenza() + "/" + lSenMod.getNumeroSentenza() + " - "
							+ lSenMod.getDescrTipoAutoritaEmittente() + " <br>di "
							+ lSenMod.getDescrLuogoEmittente() + " è già presente in archivio.");
				}

				lSenDao.setDAOFromModel(aSenMod);
				lKeySentenza = lSenDao.insert();

				// fascicolo inserito solo se Decreto o Sentenza
				if (aFasMod != null && aFasMod.getChiaveAnno() != null) {
					lFasSqlDao.ricercaFascicoloByProgrAnnoCodUfficio(aFasMod);
					FascicoloSiepModel lFascMod = (FascicoloSiepModel) lFasSqlDao.getModelByKey();
					if (lFascMod != null && lFascMod.getIdFascicoloSiep() != null) {
						throw new F3BException(F3BException.USER_MESSAGE,
								"Il fascicolo risulta presente.Impossibile inserirlo");
					}

					aFasMod.setSenIdSentenza(lKeySentenza);
					lFasDao.setDAOFromModel(aFasMod);
					lKeyFascicolo = lFasDao.insert();
				}
			}

			// cumulo
			if (aCumulo.getIdFascicoloSiepCumulato() == null && lKeyFascicolo != null)
				aCumulo.setIdFascicoloSiepCumulato(lKeyFascicolo);

			if (lKeySentenza != null)
				aCumulo.setSenIdSentenza(lKeySentenza);

			lCumDao.setDAOFromModel(aCumulo);
			BigDecimal lKey = null;
			lKey = lCumDao.insert();
			lCumMod.setIdCumulo(lKey);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CumuloController.ExInserisci: Non posso inserire: " + ex);
		} catch (F3BException fex) {
			rollback(lConn);
			throw fex;
		} finally {
			cleanup(lCumDao);
			cleanup(lFasDao);
			cleanup(lSenDao);
			cleanup(lSenSQL);
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}
		return lCumMod;
	}

	/**
	 * Inserisce/Aggiorna i dati del Cumulo, Pena_cumulo e LA in cumulo
	 * 
	 * @param aPenaCumulo
	 * @param aCumulo
	 * @param alibAntMod
	 * @param aEsisteGiorniLib
	 * @return
	 * @throws F3BException
	 */
	public PenaCumuloModel ExInserisciCumuloPenaCumuloLibAnt(PenaCumuloModel aPenaCumulo,
			CumuloModel aCumulo, LicenzaLibAnticipataModel alibAntMod, boolean aEsisteGiorniLib)
			throws F3BException {
		Connection lConn = null;
		PenaCumuloDAO lPenDao = null;
		PenaCumuloSqlDAO lPenSqlDao = null;
		PenaCumuloModel lPenMod = null;
		CumuloDAO lCumDao = null;
		CumuloSqlDAO lCumSqlDao = null;
		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		lPenMod = new PenaCumuloModel(aPenaCumulo);

		try {
			lConn = getDBTransaction();

			lPenDao = new PenaCumuloDAO(lConn);
			lCumDao = new CumuloDAO(lConn);
			lCumSqlDao = new CumuloSqlDAO(lConn);
			lPenSqlDao = new PenaCumuloSqlDAO(lConn);
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			/*
			 * modifica 08-06-2006 -- Dario -- Viviana -- viene spostata la scrittura del FlagCumulante
			 * dall'inserimento dei fascicoli coinvolti alla validazione del provvedimento pene concorrenti
			 * ossia la funzione Stampa di gestione cumulo!!
			 * 
			 * //FASCICOLO SIEP FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			 * lFascSqlDao.ricercaFascicoloByKey(aCumulo.getFasSieIdFascicoloSiep()); lFascMod =
			 * (FascicoloSiepModel)lFascSqlDao.getModelByKey();
			 * 
			 * if(lFascMod != null && lFascMod.getIdFascicoloSiep() != null) {
			 * lFascDao.setDAOFromModelForUpdate(lFascMod); lFascDao.setFlagCumulante("S");
			 * lFascDao.selCondizioneUpdate(lFascMod.getIdFascicoloSiep());
			 * 
			 * lFascDao.update(); lFascDao.stop(); }
			 */
			// =======================================================================
			// Recupero il primo (per data inserimento) record CUMULO non validato
			// presente sul fascicolo e lo aggiorno
			// =======================================================================
			CumuloModel lCumMod = new CumuloModel();
			lCumSqlDao
					.ricercaFascicoliCumulobyIdFascicoloSiepFlagValidato(aCumulo.getFasSieIdFascicoloSiep());
			lCumMod = (CumuloModel) lCumSqlDao.getModelByKey();

			BigDecimal lKeyCumulo = null;
			if (lCumMod != null && lCumMod.getIdCumulo() != null) {
				// se presente AGGIORNA con i dati inseriti nella maschera 'Annotazione dati finali'
				// n.b. il record potrebbe essere quello contenente i dati di un cumulato
				lKeyCumulo = lCumMod.getIdCumulo();

				lCumDao.setDataCumulo(aCumulo.getDataCumulo());
				lCumDao.setFasSieIdFascicoloSiep(aCumulo.getFasSieIdFascicoloSiep());
				// 17-05-2006 -- Dario -- Umberto -- vine commentato perchè se il giro classico del cumulo
				// ossia
				// inserimento fascicoli coinvolti poi annotazioni data finali e stampa viene modificato con
				// inserimento fascicoli coinvolti poi stampa(non validata) e annotazioni data finali il
				// contenuto di
				// FlagTipoStampa viene alterato, quando invece era già stato inserito dalla stampa.
				// lCumDao.setFlagTipoStampa(aCumulo.getFlagTipoStampa());

				lCumDao.setFlagValidato("N");

				lCumDao.setCodUfficioAggiornamento(aCumulo.getCodUfficioAggiornamento());
				lCumDao.setCodOperatoreAggiornamento(aCumulo.getCodOperatoreAggiornamento());
				lCumDao.setDataAggiornamento(DateUtils.getSysDate());

				lCumDao.setCondizioneUpdate(lKeyCumulo);
				lCumDao.update();
				lCumDao.stop();
			} else // INSERISCE
			{ // Non è presente alcun record CUMULO il che vuol dire che non sono stati
				// iscritti o selezionati ancora fascicoli da cumulare (?)
				lCumDao.setDataCumulo(aCumulo.getDataCumulo());
				lCumDao.setFasSieIdFascicoloSiep(aCumulo.getFasSieIdFascicoloSiep());
				lCumDao.setFlagTipoStampa(aCumulo.getFlagTipoStampa());
				lCumDao.setFlagValidato("N");
				// ???? campo obbligatorio dovrebbe essere l'id della sentenza di un cumulato
				// ma in assenza di tale evento utilizzo l'id della sentenza del cumulante
				lCumDao.setSenIdSentenza(aCumulo.getSenIdSentenza());

				lCumDao.setCodUfficioInserimento(aCumulo.getCodUfficioAggiornamento());
				lCumDao.setCodOperatoreInserimento(aCumulo.getCodOperatoreAggiornamento());
				lCumDao.setDataInserimento(DateUtils.getSysDate());

				lKeyCumulo = lCumDao.insert();
				lCumDao.stop();
			}

			// =======================================================================
			// PENA CUMULO
			// Il record pena_cumulo viene agganciato al primo record cumulo iscritto
			// per cui lo cerco su tale record. Se presente lo aggiorno altrimenti lo
			// inserisco.
			// =======================================================================
			PenaCumuloModel lPenaCumulo = new PenaCumuloModel();
			lPenSqlDao.ricercaPenaCumuloByIdCumulo(lKeyCumulo);
			lPenaCumulo = (PenaCumuloModel) lPenSqlDao.getModelByKey();
			BigDecimal lKeyPena = null;

			if (lPenaCumulo != null && lPenaCumulo.getIdPenaCumulo() != null) {// aggiorna

				lKeyPena = lPenaCumulo.getIdPenaCumulo();

				aPenaCumulo.setIdPenaCumulo(lKeyPena);
				lPenDao.setDAOFromModelForUpdate(aPenaCumulo);
				lPenDao.setCumIdCumulo(lKeyCumulo);

				lPenDao.setCodUfficioAggiornamento(aCumulo.getCodUfficioAggiornamento());
				lPenDao.setCodOperatoreAggiornamento(aCumulo.getCodOperatoreAggiornamento());
				lPenDao.setDataAggiornamento(DateUtils.getSysDate());

				lPenDao.update();
				lPenDao.stop();
			} else {// inserisce

				aPenaCumulo.setCumIdCumulo(lKeyCumulo);
				lPenDao.setDAOFromModel(aPenaCumulo);

				lPenDao.setCodUfficioInserimento(aCumulo.getCodUfficioAggiornamento());
				lPenDao.setCodOperatoreInserimento(aCumulo.getCodOperatoreAggiornamento());
				lPenDao.setDataInserimento(DateUtils.getSysDate());

				lKeyPena = lPenDao.insert();
				lPenDao.stop();
			}
			lPenMod.setIdPenaCumulo(lKeyPena);

			// =======================================================================
			// LIBERAZIONE ANTICIPATA (?)
			// =======================================================================
			/*
			 * if(aEsisteGiorniLib && alibAntMod != null && alibAntMod.getNumeroGiorni() != null &&
			 * alibAntMod.getNumeroGiorni().compareTo(new BigDecimal(0))!= 0 ) { BigDecimal lLiberazioneGiorni
			 * = alibAntMod.getNumeroGiorni();
			 * 
			 * LicenzaLibAnticipataModel lLicMod = new LicenzaLibAnticipataModel();
			 * lLicSqlDao.ricercaLicenzaLibanticipataUltimaByIDFascicoloSIEP
			 * (aCumulo.getFasSieIdFascicoloSiep()); lLicMod = (LicenzaLibAnticipataModel)
			 * lLicSqlDao.getModelByKey();
			 * 
			 * if ( lLicMod != null && lLicMod.getFlagElaborato() != null &&
			 * lLicMod.getFlagElaborato().equals("N") ) { //aggiorna
			 * lLicMod.setFasSieIdFascicoloSiep(alibAntMod.getFasSieIdFascicoloSiep());
			 * lLicMod.setFlagConcesso(alibAntMod.getFlagConcesso());
			 * lLicMod.setFlagElaborato(alibAntMod.getFlagElaborato());
			 * lLicMod.setCodTipoLicenza(alibAntMod.getCodTipoLicenza());
			 * lLicMod.setNumeroGiorni(lLiberazioneGiorni);
			 * 
			 * //???????????????????????
			 * lLicMod.setCodUfficioAggiornamento(alibAntMod.getCodUfficioInserimento());
			 * lLicMod.setCodOperatoreAggiornamento(alibAntMod.getCodOperatoreInserimento());
			 * lLicMod.setDataAggiornamento(DateUtils.getSysDate());
			 * 
			 * lLicDao.setDAOFromModelForUpdate(lLicMod); lLicDao.update(); lLicDao.stop(); } else {
			 * //inserisce alibAntMod.setNumeroGiorni(lLiberazioneGiorni);
			 * lLicDao.setDAOFromModel(alibAntMod); lLicDao.insert(); lLicDao.stop(); } }
			 */

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("CumuloController.ExInserisciCumuloPenaCumuloLibAnt: " + ex);
		} catch (Exception exp) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + exp);
			throw new F3BException("CumuloController.ExInserisciCumuloPenaCumuloLibAnt: " + exp);
		} finally {
			cleanup(lPenDao);
			cleanup(lPenSqlDao);
			cleanup(lCumDao);
			cleanup(lCumSqlDao);
			cleanup(lLicDao);
			cleanup(lLicSqlDao);

			cleanup(lConn);
		}

		return lPenMod;
	}

	public Vector ricercaCumuloSentenzaFascicoloSige(CumuloModel aCumulo) throws F3BException {
		Connection lConn = null;
		Vector lCumuli = new Vector();
		CumuloSqlDAO lCumDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaCumuloSentenzaFascicoloSige(aCumulo);
			lCumuli = new Vector(lCumDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		return lCumuli;
	}

	public Vector ExRicercaCumulo(CumuloModel aCumulo) throws F3BException {
		Connection lConn = null;
		Vector lCumuli = new Vector();
		CumuloSqlDAO lCumDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaCumulo(aCumulo);
			lCumuli = new Vector(lCumDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		return lCumuli;
	}

	/**
	 * Ricerca i record CUMULO non validati collegati al fascicolo specificato
	 *
	 * @param aKey
	 *            - Id del fascicolo cumulante
	 * @return Vector di CumuloModel con i CUMULI non validati
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		Vector lCumuli = new Vector();
		CumuloSqlDAO lCumDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaFascicoliCumulobyIdFascicoloSiepFlagValidato(aKey);
			lCumuli = new Vector(lCumDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"CumuloController.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}

		return lCumuli;
	}

	public Vector ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		Vector lCumuli = new Vector();
		CumuloSqlDAO lCumDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(aKey);
			lCumuli = new Vector(lCumDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"CumuloController.ExRicercaFascicoliCumulobyIdFascicoloSiepValidato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}

		return lCumuli;
	}

	/**
	 * Recupera i Fascicoli cumulati o in istruttoria cumulo legati al cumulante specificato in input.
	 * Recupera anche le sentenze associate.
	 *
	 * @param lFascID
	 *            - id Fascicoli cumulante
	 * @return Vector di FascicoloSiepModel con i fascicoli cumuloati.
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliCumulatiByIDFascicoloSiep(BigDecimal lFascID) throws F3BException {
		Connection lConn = null;
		Vector lCumuli = new Vector();
		CumuloSqlDAO lCumDao = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepController lFasCtr = new FascicoloSiepController();
		SentenzaController lSenCtr = new SentenzaController();
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaFascicoliCumulobyIdFascicoloSiep(lFascID);
			lCumuli = new Vector(lCumDao.getModels());
			if (lCumuli.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("------>> Trovati " + lCumuli.size() + " fascicoli Cumulati");

			for (int i = 0; i < lCumuli.size(); i++) {
				// MEV 16: La ricerca avviene nella tabella CUMULO per il procedimento cumulante.
				// SE LA SENTENZA COINVOLTA PROVIENE DA NSC
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("------>> Fascicolo " + i + " ID CUMULANTE: "
						+ ((CumuloModel) lCumuli.get(i)).getFasSieIdFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("------>> Fascicolo " + i + " ID CUMULATO: "
						+ ((CumuloModel) lCumuli.get(i)).getIdFascicoloSiepCumulato());
				CumuloModel cumulo = ((CumuloModel) lCumuli.get(i));
				if (cumulo.getIdFascicoloSiepCumulato() != null) {
					lFasMod = lFasCtr.ExRicercaFascicoloByKey(cumulo.getIdFascicoloSiepCumulato());
					lFasMod.setSentenza(lSenCtr.ExRicercaSentenzaByKey(lFasMod.getSenIdSentenza()));
					lFascicoli.add(lFasMod);
					// Indica i Titoli Esecutivi appartenenti ad un Fascicolo di Cumulo
					// e non collegati a Procedimenti SIEP.
				} else if ((cumulo.getSentenza() != null && cumulo.getSentenza().getFlagVisibilita() != null && cumulo
						.getSentenza().getFlagVisibilita().equals("S"))) {
					lFasMod.setSentenza(lSenCtr.ExRicercaSentenzaByKey(cumulo.getSenIdSentenza()));
					lFascicoli.add(lFasMod);
				}
				// MEV 16: nuova ricerca per il cumulante SE LA SENTENZA COINVOLTA PROVIENE DA NSC
				else if (cumulo.getFasSieIdFascicoloSiep() != null) {
					lFasMod = lFasCtr.ExRicercaFascicoloByKey(cumulo.getFasSieIdFascicoloSiep());
					lFasMod.setSentenza(lSenCtr.ExRicercaSentenzaByKey(cumulo.getSenIdSentenza()));
					lFascicoli.add(lFasMod);
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lFascicoli;
	}

	public CumuloModel ExRicercaCumuloByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		CumuloSqlDAO lCumDao = null;
		CumuloModel lCumMod;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaCumuloByKey(aKey);
			lCumMod = (CumuloModel) lCumDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		return lCumMod;
	}

	public CumuloModel ExRicercaCumuloByEveIdEvento(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		CumuloSqlDAO lCumDao = null;
		CumuloModel lCumMod;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaCumuloByEveIdEvento(aKey);
			lCumMod = (CumuloModel) lCumDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumuloByEveIdEvento: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		return lCumMod;
	}

	public CumuloModel ExModificaCumulo(CumuloModel aCumulo) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;
		CumuloModel lCumMod = new CumuloModel(aCumulo);

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloDAO(lConn);
			lCumDao.setDAOFromModelForUpdate(aCumulo);
			lCumDao.update();
			lCumDao.stop();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CumuloController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		return lCumMod;
	}

	public void ExCancellaCumulo(CumuloModel aCumulo) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloDAO(lConn);
			lCumDao.setCondizioneUpdate(aCumulo.getIdCumulo());
			lCumDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExCancellaCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
	}

	public void ExAggiornaCumuloFasc(CumuloModel aCumulo) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;
		try {
			lConn = getDBConnection();
			lCumDao = new CumuloDAO(lConn);

			lCumDao.setCondizioneUpdateByFasc(aCumulo.getFasSieIdFascicoloSiep());
			lCumDao.setDAOFromModelForUpdate(aCumulo);
			lCumDao.update();
			lCumDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExAggiornaCumulo: Non posso aggiornare : " + daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la validazione di un Provvedimento di Cumulo:<br>
	 * - aggiorna il flag cumulante sul fascicolo corrente<br>
	 * - aggiorna il flag cumulato sui fascicoli coinvolti nel cumulo<br>
	 * - valida il record CUMULO<br>
	 * - valida il record pena residua<br>
	 * - aggiorna le LA a S<br>
	 *
	 * @param aEvento
	 *            - Provvedimento di Cumulo da Validare
	 * @param aFascicolo
	 *            - fascicolo SIEP Cumulante
	 * @return EventoModel -
	 */
	public EventoModel ExUpdateValidaCumulo(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {
		Connection lConn = null;
		// Connection lConnBlob = null;

		EventoSqlDAO lEveSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;

		EventoDAO lEveDaoBlob = null;

		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;

		StatoProcedimentoDAO lStatoDao = null;

		CumuloSqlDAO lCumSqlDao = null;
		CumuloDAO lCumDao = null;

		FascicoloSiepDAO lFascDao = null;
		LicenzaLibanticipataDAO lLibDAO = null;
		LicenzaLibanticipataSqlDAO lLibSqlDAO = null;

		NotificaEventoSqlDAO lNotEveDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScadeDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		ScadenzarioModel lScaMod = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			lFascDao = new FascicoloSiepDAO(lConn);
			lCumSqlDao = new CumuloSqlDAO(lConn);
			lCumDao = new CumuloDAO(lConn);
			lLibDAO = new LicenzaLibanticipataDAO(lConn);
			lLibSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);

			// ========================================================================
			// Recupera l'evento da validare (Provvedimento di Cumulo)
			// ========================================================================
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Ricerca il CUMULO collegato all'evento. n.b sebbene esistano più record
			// CUMULO solo quello inserito per primo viene agganciato all'evento (?)
			CumuloModel lCumuloMod = null;
			lCumSqlDao.ricercaCumuloByEveIdEvento(aEvento.getIdEvento());
			lCumuloMod = (CumuloModel) lCumSqlDao.getModelByKey();
			// tipo di provvedimento (dominio STAMPE_CUMULO)
			String atipologia = lCumuloMod.getFlagTipoStampa();

			// ========================================================================
			// Aggiorna flag cumulante e flag cumulato sul Fascicolo_model
			// ========================================================================
			lCumSqlDao.ricercaFascicoliCumulobyIdFascicoloSiepFlagValidato(aFascicolo.getIdFascicoloSiep());
			Vector lVectCum = new Vector(lCumSqlDao.getModels());

			CumuloModel lCumMod = null;

			// Aggiorno il Flag cumulante sul fascicolo corrente
			if (lVectCum != null && lVectCum.size() > 0) {
				lCumMod = (CumuloModel) lVectCum.get(0);

				lFascDao.selCondizioneUpdate(lCumMod.getFasSieIdFascicoloSiep());
				lFascDao.setFlagCumulante("S");

				lFascDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lFascDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lFascDao.setDataAggiornamento(DateUtils.getSysDate());

				lFascDao.update();
				lFascDao.stop();
			}

			// ========================================================================
			// Aggiorna il flag cumulato sui fascicoli coinvolti nel cumulo (CUMULATI)
			// Aggiorna i campi:
			// - NUM_FASCICOLO_UNIONE
			// - ANNO_FASCICOLO_UNIONE
			// - DATA_UNIONE
			// - COD_UFFICIO_UNIONE
			// ========================================================================
			Iterator iter = lVectCum.iterator();
			while (iter.hasNext()) {
				lCumMod = new CumuloModel();
				lCumMod = (CumuloModel) iter.next();

				// cumulato
				if (lCumMod != null && lCumMod.getIdFascicoloSiepCumulato() != null) {
					lFascDao.selCondizioneUpdate(lCumMod.getIdFascicoloSiepCumulato());
					lFascDao.setFlagCumulato("S");

					/*
					 * modifica 28-04-05 -- luciana -- Dario-- dovuta alla funzione di archiviazione per
					 * perdita di competenza
					 */
					lFascDao.setNumFascicoloUnione(aFascicolo.getChiaveProgr().toString());
					lFascDao.setAnnoFascicoloUnione(aFascicolo.getChiaveAnno().toString());
					lFascDao.setDataUnione(((CumuloModel) lVectCum.get(0)).getDataCumulo());
					lFascDao.setCodUfficioUnione(lCumMod.getCodUfficioInserimento());

					lFascDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
					lFascDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
					lFascDao.setDataAggiornamento(DateUtils.getSysDate());

					lFascDao.update();
					lFascDao.stop();
				}

				// aggiunto dopo -- Dario -- Luciana -- 21-07-05
				lCumDao.setFlagValidato("S");
				lCumDao.setCondizioneUpdate(lCumMod.getIdCumulo());

				lCumDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
				lCumDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
				lCumDao.setDataAggiornamento(DateUtils.getSysDate());

				lCumDao.update();
				lCumDao.stop();
			}

			// ===================================
			// Aggiorna Inserisci PENA_RESIDUA
			// ===================================
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
			lPenResDao.setFlagValidato("S");
			lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
			lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
			lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
			lPenResDao.selByKey();
			lPenResDao.update();
			lPenResDao.stop();

			// ========================================================================
			// Cancellazione Stato Procedimento (?)
			// ========================================================================
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();
			lStatoDao.stop();

			// ========================================================================
			// inserimento Stato Procedimento
			// ========================================================================
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lEveModel.getDataEmissione());

			if (atipologia.equals("0")) // Condannato in stato di liberta'
				lStatoDao.setCodStatoProcedimento("0003"); // Eseguito Ordine di Esecuzione con Arresto il
			else if (atipologia.equals("1")) // Condannato gia' detenuto
				lStatoDao.setCodStatoProcedimento("0015"); // Eseguito Ordine di Esecuzione con Notifica il
			else if (atipologia.equals("2")) // Condannato in misura alternativa
				lStatoDao.setCodStatoProcedimento("0117"); // Emesso Provvedimento di Esecuzione Pene
															// Concorrenti in
															// Misura Alternativa
			else
				// stampa generica // 3 Generico
				lStatoDao.setCodStatoProcedimento("0136"); // Emesso provvedimento di pene concorrenti in data

			// !!!!!!!!!!! perchè il codice 0003? l'ordine è stato emesso, non eseguito

			lStatoDao.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveModel.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
			lStatoDao.insert();

			// ========================================================================
			// Aggiorna tabella nome_provvedimento
			// ========================================================================
			lNomProvvDAO.setCodNomeProvvedimento("NP203");
			lNomProvvDAO.setEveIdEvento(lEveModel.getIdEvento());
			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// RICERCA POSIZIONE GIURIDICA
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aFascicolo.getIdFascicoloSiep());

			// PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// ========================================================================
			// Aggiorna liberazione anticipata legate all'evento ponendo il flag
			// elaborate a S se computate sulla pena
			// ========================================================================
			if (lPenResMod != null
					&& lPenResMod.getDataInizio() != null
					&& lPenResMod.getDataFine() != null
					&& (lPenResMod.getFlagErgastolo() == null || (lPenResMod.getFlagErgastolo() != null && lPenResMod
							.getFlagErgastolo().equals("N")))) {
				lLibSqlDAO.ricercaLicenzaLibanticipataByEve(lEveMod.getIdEvento());

				Vector lListaLicenze = new Vector(lLibSqlDAO.getModels());

				lLibDAO = new LicenzaLibanticipataDAO(lConn);

				for (int i = 0; i < lListaLicenze.size(); i++) {
					LicenzaLibAnticipataModel lLicModel = (LicenzaLibAnticipataModel) lListaLicenze
							.elementAt(i);

					lLicModel.setFlagElaborato("S");

					lLibDAO.setDAOFromModelForUpdate(lLicModel);
					lLibDAO.update();
					lLibDAO.stop();
				}
			}

			/*
			 * lLibSqlDAO.ricercaLicenzaLibanticipataByEve(lEveMod.getIdEvento()); LicenzaLibAnticipataModel
			 * lLicMod = null; lLicMod = (LicenzaLibAnticipataModel) lLibSqlDAO.getModelByKey(); if (lLicMod
			 * != null) { // modifica 24/01/2007: se le LA non sono state computate sul fine pena // non
			 * aggiorno il flag_elaborato a S if ( lPenResMod != null && lPenResMod.getDataInizio() != null &&
			 * lPenResMod.getDataFine() != null && ( lPenResMod.getFlagErgastolo()==null ||
			 * (lPenResMod.getFlagErgastolo()!=null && lPenResMod.getFlagErgastolo().equals("N")) ) ) {
			 * lLibDAO = new LicenzaLibanticipataDAO(lConn); lLicMod.setFlagElaborato("S");
			 * lLibDAO.setDAOFromModelForUpdate(lLicMod); lLibDAO.update(); lLibDAO.stop(); } }
			 */
			// AMBROSINO 09-02-2011 - NOTIFICHE - le prendo per la data inizioscadenzario

			// * Cerca le NOTIFICHE *
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			Vector lNotifiche = new Vector(lNotEveDao.getModels());
			NotificaModel lNotifica = (NotificaModel) lNotifiche.get(0);

			// SCADENZARIO
			lScaDao = new ScadenzarioDAO(lConn);
			lScaMod = new ScadenzarioModel();

			Date lFineScadenza = null;

			// se libero da combo
			if (atipologia.equals("0")) {
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("VANE RICERCHE");
				lParMod.setCodUfficioValidita(aEvento.getCodUfficioAggiornamento());

				Vector lVectPar = null;
				IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();
				lVectPar = lCtrlPar.ExRicercaParametroScadenzario(lParMod);

				// lScaMod.setDataInizioScadenza(lEveModel.getDataTrasmissioneAtti());
				lScaMod.setDataInizioScadenza(lNotifica.getDataInvio());

				Iterator lIter = lVectPar.iterator();
				Date lSommaAnni = null;
				Date lSommaMesi = null;

				if (lIter.hasNext()) {
					ParametroModel lParModel = (ParametroModel) lIter.next();
					lSommaAnni = DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(),
							java.util.Calendar.YEAR, lParModel.getAnni().intValue());
					lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH, lParModel
							.getMesi().intValue());
					lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH,
							lParModel.getGiorni().intValue());
				}

				lScaMod.setCodTipoScadenzario("03");
				lScaMod.setDataFineScadenza(lFineScadenza);
			}
			// se Detenuto o Misura Alternativa
			else if (atipologia.equals("1") || atipologia.equals("2")) {
				lScaMod.setCodTipoScadenzario("02");

				if (lPenResMod != null && lPenResMod.getDataInizio() != null
						&& lPenResMod.getDataFine() != null) {
					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
				}
			}

			if (!atipologia.equals("3") && lScaMod.getDataInizioScadenza() != null
					&& lScaMod.getDataFineScadenza() != null) {
				// Vector lScadenzarii = null;
				lScadeDao = new ScadenzarioSqlDAO(lConn);
				// cerca un scadenzario per id fascicolo e per tipo scadenzario
				lScadeDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo(lScaMod.getCodTipoScadenzario(),
						aFascicolo.getIdFascicoloSiep());
				ScadenzarioModel lScaModel = (ScadenzarioModel) lScadeDao.getModelByKey();

				if (lScaModel == null) {
					lScaMod.setFlagVisto("N");
					lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataInserimento(aEvento.getDataAggiornamento());
					lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					// a6-rr-238 20-11-2010
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
					if (lScaMod.getCodTipoScadenzario().equals("03"))
						lScaMod.setCodStatoNotifica("NP");

					lScaDao.setDAOFromModel(lScaMod);
					// BigDecimal lKeyScad = null;
					/* lKeyScad = */lScaDao.insert();
				} else {
					lScaMod.setIdScadenzario(lScaModel.getIdScadenzario());
					lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
					// a6-rr-238 20-11-2010
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
					if (lScaMod.getCodTipoScadenzario().equals("03"))
						lScaMod.setCodStatoNotifica("NP");

					lScaDao.setDAOFromModelForUpdate(lScaMod);
					lScaDao.update();
				}
			}

			// ===================================================================
			// Cancella tutti gli scadenzari di tipo LEGGE SIMEONE ( Tipo = 01 )
			// ===================================================================
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aFascicolo.getIdFascicoloSiep(), "01");
			lScaDao.delete();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
			// commit(lConnBlob);
		} catch (DAOException daoEx) {

			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("CumuloController.ExUpdateValidaCumulo : " + daoEx);
		} catch (F3BException f3bEx) {

			rollback(lConn);
			// rollback(lConnBlob);

			throw f3bEx;
		} catch (Exception ex) {

			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("CumuloController.ExUpdateValidaCumulo : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);
			cleanup(lStatoDao);
			cleanup(lFascDao);
			cleanup(lCumSqlDao);
			cleanup(lCumDao);
			cleanup(lLibDAO);
			cleanup(lLibSqlDAO);
			cleanup(lScadeDao);
			cleanup(lScaDao);
			cleanup(lPosSqlDao);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}

		return lEveMod;
	}

	public ByteArrayOutputStream ExStampaDocumentoXCumulo(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(aEvento.getEvento()
					.getIdEvento());

			lEveMod.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			IStampa lStampa = SICOLookupRemote.getStampaRemote();

			TreeModel lTree = lStampa.prelevaDatiEventoSiepXCumulo(lEveMod, aUtente);

			ReportGenerator lReport = new ReportGenerator();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Chiave = " + aEvento.getNomeTemplate());

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("EVENTO >>> " + aEvento.getEvento().toString());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("CumuloController.ExStampaDocumentoXCumulo: " + daoEx);
		} finally {
			cleanup(lEveDao);

			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Effettua l'inserimento di un Elenco Cumuli riferiti a un Fascicolo <br>
	 *
	 * @param aCumuli
	 *            - Elenco Cumuli
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciCumuloWithoutSequence(ArrayList aCumuli, Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		CumuloDAO lCumuloDao = null;
		CumuloModel lCumuloMod = null;
		try {

			lCumuloDao = new CumuloDAO(lConn);

			if (aCumuli != null && aCumuli.size() > 0) {
				for (int i = 0; i < aCumuli.size(); i++) {
					lCumuloMod = (CumuloModel) aCumuli.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Cumulo da inserire = " + lCumuloMod);
					if (lCumuloMod != null && lCumuloMod.getIdCumulo() != null) {
						lCumuloDao.setDAOFromModel(lCumuloMod);
						lCumuloDao.setWithoutSequence(true);
						lCumuloDao.insert();
						lCumuloDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Cumulo! ");
			}
		} finally {
			cleanup(lCumuloDao);
		}
		return lCodEsito;
	}

	/**
	 * Effettua la ricerca di tutti i record CUMULO associati ad una certa istruttoria
	 * 
	 * @param aIdIstruttoria
	 *            - Id Dell'istruttoria
	 * @return vettore di CumuloModel
	 * @throws F3BException
	 */
	public Vector ExRicercaCumuloByIstruttoria(BigDecimal aIdIstruttoria) throws F3BException {
		Connection lConn = null;

		CumuloSqlDAO lCumSqlDao = null;
		FascicoloSiepSqlDAO lFasSiepSqlDAO = null;
		SentenzaSqlDAO lSentSqlDAO = null;

		Vector lListaCumuli = new Vector();

		CumuloModel lCumuloModel = null;

		try {
			lConn = getDBConnection();

			lCumSqlDao = new CumuloSqlDAO(lConn);
			lCumSqlDao.ricercaCumuloByIdIstruttoria(aIdIstruttoria);
			lListaCumuli = new Vector(lCumSqlDao.getModels());

			// ========================================================================
			// Recupero i dati del fascicolo e della sentenza
			// ========================================================================
			for (int i = 0; i < lListaCumuli.size(); i++) {
				lCumuloModel = (CumuloModel) lListaCumuli.elementAt(i);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cumulo = " + lCumuloModel);

				// Recupero i dati dle fascicolo
				if (lCumuloModel.getIdFascicoloSiepCumulato() != null) {
					lFasSiepSqlDAO = new FascicoloSiepSqlDAO(lConn);
					lFasSiepSqlDAO.ricercaFascicoloByKey(lCumuloModel.getIdFascicoloSiepCumulato());
					FascicoloSiepModel lFascModel = (FascicoloSiepModel) lFasSiepSqlDAO.getModelByKey();
					lCumuloModel.setFascicoloSiep(lFascModel);
					lFasSiepSqlDAO.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Fascicolo = " + lFascModel);
				}

				// Recupero i dati della sentenza
				if (lCumuloModel.getSenIdSentenza() != null) {
					lSentSqlDAO = new SentenzaSqlDAO(lConn);
					lSentSqlDAO.ricercaSentenzaBykey(lCumuloModel.getSenIdSentenza());
					SentenzaModel lSentModel = (SentenzaModel) lSentSqlDAO.getModelByKey();
					lCumuloModel.setSentenza(lSentModel);
					lSentSqlDAO.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Sentenza = " + lSentModel);
				}
			}

		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumuloByIstruttoria: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCumSqlDao);
			cleanup(lFasSiepSqlDAO);
			cleanup(lSentSqlDAO);

			cleanup(lConn);
		}

		return lListaCumuli;
	}

	/**
	 * Effettua la cancellazione del cumulato che non risulta essere 'Primo Cumulo' e della Sentenza ad esso
	 * associata, se a quest'ultima non è associato nessun Fascicolo Siep.
	 * 
	 * @param aCumulo
	 *            - cumulo da eliminare
	 * @throws F3BException
	 */
	public void ExCancellaFascCumulato(CumuloModel aCumulo) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;
		SentenzaDAO lSentenzaDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloDAO(lConn);
			lSentenzaDao = new SentenzaDAO(lConn);

			lCumDao.setCondizioneUpdateByIdSentenza(aCumulo.getSenIdSentenza());
			lCumDao.delete();
			lCumDao.stop();

			lSentenzaDao.selCondizioneUpdate(aCumulo.getSenIdSentenza());
			lSentenzaDao.delete();
			lSentenzaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibbile cancellare il Provvedimento, Sentenza collegata a Fascicolo Siep!");
			else
				throw new F3BException("CumuloController.ExCancellaFascCumulato: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("CumuloController.ExCancellaFascCumulato: " + e);
		}

		finally {
			cleanup(lCumDao);
			cleanup(lSentenzaDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la cancellazione del cumulato che risulta essere 'Primo Cumulo'. Prima di eliminare il record
	 * si imposta il cumulo successivo come 'Primo Cumulo' e si associa a quest'ultimo la Pena Cumulo (se
	 * esiste); si elimina la sentanza ad esso associata, se a quest'ultima non è associato nessun Fascicolo
	 * Siep.
	 * 
	 * @param aCumulo
	 *            - cumulo da eliminare
	 * @param aNextPrimoCumulo
	 *            - cumulo che diventa primo Cumulo
	 * @param aPenaCumulo
	 *            - Pena Cumulo
	 * @param aPenaCumulo
	 *            - vettore contenente le Ulteriori Sanzioni associate al Cumulo da eliminare
	 * @throws F3BException
	 */
	public void ExCancellaFascCumulatoPrimoCumulo(CumuloModel aCumulo, CumuloModel aNextPrimoCumulo,
			PenaCumuloModel aPenaCumulo, Vector aUltSanzCumulo) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;
		SentenzaDAO lSentenzaDao = null;
		PenaCumuloDAO lPenaCumuloDao = null;
		UlterioreSanzioneCumuloDAO lUltSanzDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloDAO(lConn);
			lSentenzaDao = new SentenzaDAO(lConn);
			lPenaCumuloDao = new PenaCumuloDAO(lConn);
			lUltSanzDao = new UlterioreSanzioneCumuloDAO(lConn);

			// aggiorno il flag del prossimo 'Primo Cumulo'
			lCumDao.setCondizioneUpdate(aNextPrimoCumulo.getIdCumulo());
			lCumDao.setPrimoCumulo("P");
			lCumDao.setCodUfficioAggiornamento(aNextPrimoCumulo.getCodUfficioAggiornamento());
			lCumDao.setCodOperatoreAggiornamento(aNextPrimoCumulo.getCodOperatoreAggiornamento());
			lCumDao.setDataAggiornamento(aNextPrimoCumulo.getDataAggiornamento());
			lCumDao.update();
			lCumDao.stop();

			// aggiorno la Pena Cumulo
			if (aPenaCumulo.getIdPenaCumulo() != null) {
				lPenaCumuloDao.setCondizioneUpdate(aPenaCumulo.getIdPenaCumulo());
				lPenaCumuloDao.setCumIdCumulo(aNextPrimoCumulo.getIdCumulo());
				lPenaCumuloDao.setCodOperatoreAggiornamento(aNextPrimoCumulo.getCodOperatoreAggiornamento());
				lPenaCumuloDao.setCodUfficioAggiornamento(aNextPrimoCumulo.getCodUfficioAggiornamento());
				lPenaCumuloDao.setDataAggiornamento(aNextPrimoCumulo.getDataAggiornamento());
				lPenaCumuloDao.update();
				lPenaCumuloDao.stop();
			}

			// aggiorno le Ulteriori Sanzioni
			if (aUltSanzCumulo.size() > 0) {
				for (int i = 0; i < aUltSanzCumulo.size(); i++) {
					UlterioreSanzioneCumuloModel lUltCumMod = (UlterioreSanzioneCumuloModel) aUltSanzCumulo
							.get(i);
					lUltSanzDao.setCondizioneUpdate(lUltCumMod.getIdUlterioreSanzioneCumulo());
					lUltSanzDao.setCumIdCumulo(aNextPrimoCumulo.getIdCumulo());
					lUltSanzDao.setCodOperatoreAggiornamento(aNextPrimoCumulo.getCodOperatoreAggiornamento());
					lUltSanzDao.setCodUfficioAggiornamento(aNextPrimoCumulo.getCodUfficioAggiornamento());
					lUltSanzDao.setDataAggiornamento(aNextPrimoCumulo.getDataAggiornamento());
					lUltSanzDao.update();

				}
				lPenaCumuloDao.stop();
			}

			// elimino il cumulato
			lCumDao.setCondizioneUpdateByIdSentenza(aCumulo.getSenIdSentenza());
			lCumDao.delete();
			lCumDao.stop();

			// elimino la sentenza
			lSentenzaDao.selCondizioneUpdate(aCumulo.getSenIdSentenza());
			lSentenzaDao.delete();
			lSentenzaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibbile cancellare il Provvedimento, Sentenza collegata a Fascicolo Siep!");
			else
				throw new F3BException("CumuloController.ExCancellaFascCumulatoPrimoCumulo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("CumuloController.ExCancellaFascCumulatoPrimoCumulo: " + e);
		}

		finally {
			cleanup(lCumDao);
			cleanup(lSentenzaDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la cancellazione del cumulato che risulta essere 'Primo Cumulo' ed 'Unico' fascicolo cumulato.
	 * Prima di eliminare il record si cancella la Pena Cumulo (se esiste). Si cancellano le Ulteriori
	 * Sanzioni (se esistono) legate al Cumulo. Si imposta a 'N' il Flag FLAG_CUMULANTE sulla tabella
	 * FASCICOLO_SIEP. Si cancella l'evento associato. Si elimina la sentanza ad esso associata, se a
	 * quest'ultima non è associato nessun Fascicolo Siep.
	 * 
	 * @param aCumulo
	 *            - cumulo da eliminare
	 * @param aNextPrimoCumulo
	 *            - cumulo che diventa primo Cumulo
	 * @param aPenaCumulo
	 *            - Pena Cumulo
	 * @throws F3BException
	 */
	public void ExCancellaFascCumulatoUnico(CumuloModel aCumulo, String aCodUtenteConnesso,
			String aCodUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		CumuloDAO lCumDao = null;
		SentenzaDAO lSentenzaDao = null;
		PenaCumuloDAO lPenaCumuloDao = null;
		FascicoloSiepDAO lFascDao = null;
		EventoDAO lEventoDao = null;
		UlterioreSanzioneCumuloDAO lUltSanzDao = null;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloDAO(lConn);
			lSentenzaDao = new SentenzaDAO(lConn);
			lPenaCumuloDao = new PenaCumuloDAO(lConn);
			lFascDao = new FascicoloSiepDAO(lConn);
			lEventoDao = new EventoDAO(lConn);
			lUltSanzDao = new UlterioreSanzioneCumuloDAO(lConn);

			// elimino la Pena Cumulo
			lPenaCumuloDao.setCumIdCumulo(aCumulo.getIdCumulo());
			lPenaCumuloDao.delete();
			lPenaCumuloDao.stop();

			// elimino le Ulteriori Sanzioni
			lUltSanzDao.setCondizioneIdCumulo(aCumulo.getIdCumulo());
			lUltSanzDao.delete();
			lUltSanzDao.stop();

			// aggiorno il FLAG_CUMULANTE = 'N' sulla tabella FASCICOLO_SIEP
			lFascDao.selCondizioneUpdate(aCumulo.getFasSieIdFascicoloSiep());
			lFascDao.setFlagCumulante("N");
			lFascDao.setCodUfficioAggiornamento(aCodUfficioUtenteConnesso);
			lFascDao.setCodOperatoreAggiornamento(aCodUtenteConnesso);
			lFascDao.setDataAggiornamento(DateUtils.getSysDate());
			lFascDao.update();
			lFascDao.stop();

			// elimino l'evento legato al cumulo
			lEventoDao.selCondizioneUpdate(aCumulo.getEveIdEvento());
			lEventoDao.delete();
			lEventoDao.stop();

			// elimino il cumulo
			lCumDao.setCondizioneUpdateByIdSentenza(aCumulo.getSenIdSentenza());
			lCumDao.delete();
			lCumDao.stop();

			// elimino la sentenza
			lSentenzaDao.selCondizioneUpdate(aCumulo.getSenIdSentenza());
			lSentenzaDao.delete();
			lSentenzaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibbile cancellare il Provvedimento, Sentenza collegata a Fascicolo Siep!");
			else
				throw new F3BException("CumuloController.ExCancellaFascCumulatoUnico: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("CumuloController.ExCancellaFascCumulatoUnico: " + e);
		}

		finally {
			cleanup(lCumDao);
			cleanup(lSentenzaDao);
			cleanup(lUltSanzDao);
			cleanup(lEventoDao);
			cleanup(lFascDao);
			cleanup(lConn);
		}
	}

	public CumuloModel ExRicercaCumuloByIdSentenza(BigDecimal aKey, String flagValidato) throws F3BException {
		Connection lConn = null;
		CumuloSqlDAO lCumDao = null;
		CumuloModel lCumMod;

		try {
			lConn = getDBConnection();
			lCumDao = new CumuloSqlDAO(lConn);
			lCumDao.ricercaCumuloByIdSentenza(aKey, flagValidato);
			lCumMod = (CumuloModel) lCumDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumuloByIdSentenza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lCumDao);
			cleanup(lConn);
		}
		return lCumMod;
	}

	// Modifica del 28/11/2016 MEV_15_S4
	// La modifica si è resa necessaria per integrare la funzionalità
	// alla "Nuova Gestione del Cumulo" introdotta con la MEV_26
	// Vengono estratti dalla tabella Evento, tutti gli eventi legati al Fascicolo Siep,
	// che hanno COD_MOTIVO legati al cumulo
	public Vector ExRicercaEventoCumulo(BigDecimal aIdFascicoloSiep) throws F3BException {
		Connection lConn = null;
		Vector lCumuli = new Vector();
		// CumuloSqlDAO lCumDao = null;
		EventoSqlDAO lEventoDao = null;

		try {
			lConn = getDBConnection();
			// lCumDao = new CumuloSqlDAO(lConn);
			lEventoDao = new EventoSqlDAO(lConn);
			// lCumDao.ricercaCumulo(aCumulo);
			lEventoDao.ricercaEventoCumulo(aIdFascicoloSiep);
			lCumuli = new Vector(lEventoDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException("CumuloController.ExRicercaCumulo: Non posso leggere : " + daoEx);
		} finally {
			// cleanup(lCumDao);
			cleanup(lEventoDao);
			cleanup(lConn);
		}
		return lCumuli;
	}

}