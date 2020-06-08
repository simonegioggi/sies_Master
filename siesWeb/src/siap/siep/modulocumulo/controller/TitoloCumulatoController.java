package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSimeoneSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneDAO;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneSqlDAO;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.modulocumulo.dao.BeneficioCumuloDAO;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloDAO;
import siap.siep.modulocumulo.dao.NotificaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: TitoloCumulatoController
 * </p>
 * <p>
 * Description: Classe Controller per TitoloCumulato
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TitoloCumulatoController extends SiapController implements ITitoloCumulato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un TitoloCumulato a partire dai dati contenuti nel Model
	 *
	 * @param aTitoloCumulato
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public TitoloCumulatoModel ExInserisciTitoloCumulato(TitoloCumulatoModel aTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		TitoloCumulatoDAO lTitDao = null;
		TitoloCumulatoModel lTitMod = null;

		try {
			lConn = getDBConnection();
			lTitDao = new TitoloCumulatoDAO(lConn);
			lTitDao.setDAOFromModel(aTitoloCumulato);
			BigDecimal lSequence = lTitDao.insert();
			commit(lConn);
			lTitMod = new TitoloCumulatoModel(aTitoloCumulato);
			lTitMod.setMessage("Inserimento avvenuto correttamente!");
			lTitMod.setIdTitoloCumulato(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex.getMessage(), ex);
			throw new F3BException("TitoloCumulatoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lTitDao);
			cleanup(lConn);
		}

		return lTitMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati TitoloCumulato
	 *
	 * @param aTitoloCumulato
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException {

		Connection lConn = null;
		Vector lTitoloCumulati = new Vector();
		TitoloCumulatoDAO lTitDao = null;

		try {
			lConn = getDBConnection();
			lTitDao = new TitoloCumulatoDAO(lConn);
			lTitDao.setCondizioni(aTitoloCumulato);
			lTitDao.setOrderBy();
			lTitDao.start();
			while (lTitDao.next()) {
				lTitoloCumulati.add(lTitDao.getModel());
			}
			lTitDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTitDao);
			cleanup(lConn);
		}

		return lTitoloCumulati;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @para m aIdTitoloCumulato valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public TitoloCumulatoModel ExRicercaTitoloCumulatoById(BigDecimal aIdTitoloCumulato) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoModel lTitoloCumulatoMod = new TitoloCumulatoModel();
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloCumulatoSqlDao.ricercaTitoloCumulatoByKey(aIdTitoloCumulato);
			lTitoloCumulatoMod = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulatoById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);

			cleanup(lConn);
		}

		return lTitoloCumulatoMod;
	}

	/**
	 * Ricerca il TITOLO_CUMULATO in istruttoria by idSentenzaOrigine
	 *
	 * @param aIdIstruttoria
	 * @param aIdTitoloOrigine
	 *            *
	 * @return
	 * @throws F3BException
	 */
	public TitoloCumulatoModel ExRicercaTitoloCumulatoByIstrIdOrig(BigDecimal aIdIstruttoria,
			BigDecimal aIdTitoloOrigine) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoModel lTitoloCumulatoMod = new TitoloCumulatoModel();
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloCumulatoSqlDao.ricercaTitoloCumulatoByIstrIdOrig(aIdIstruttoria, aIdTitoloOrigine);
			lTitoloCumulatoMod = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulatoById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);

			cleanup(lConn);
		}

		return lTitoloCumulatoMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei TitoliCumulati che abbiano dei Benefici; La Ricerca serve nella funzione
	 * 'Revoca Benefici Cumulo'
	 *
	 * @param aIdTitoloCumulato
	 *            , aIdIstru
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<TitoloCumulatoModel> ExRicercaTitoloCumulatoPerRevocaBeneficio(BigDecimal aIdTitoloCumulato,
			BigDecimal aIdIstru, String aTipoBen) throws F3BException {

		Connection lConn = null;

		Vector<TitoloCumulatoModel> lTitoloCumulati = new Vector<>();
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;

		TitoloCumulatoModel lTitoloRicerca = new TitoloCumulatoModel();

		lTitoloRicerca.setIstrIdIstruttoriaCumulo(aIdIstru);

		try {
			lConn = getDBConnection();

			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloCumulatoSqlDao.RicercaTitoloCumulatoPerRevocaBeneficio(aIdTitoloCumulato, aIdIstru,
					aTipoBen);

			lTitoloCumulatoSqlDao.start();
			while (lTitoloCumulatoSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModelXRevoche();
				lTitoloCumulati.add(lTitolo);

			}
			lTitoloCumulatoSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulatoPerRevocaBeneficio: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);
			cleanup(lConn);
		}

		return lTitoloCumulati;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'TitoloCumulato Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aTitoloCumulato
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoDAO lTitDao = null;

		try {
			lConn = getDBConnection();
			lTitDao = new TitoloCumulatoDAO(lConn);
			lTitDao.setDAOFromModelForUpdate(aTitoloCumulato);
			lTitDao.selCondizioneUpdate(aTitoloCumulato.getIdTitoloCumulato());
			lTitDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex.getMessage(), ex);
			throw new F3BException("TitoloCumulatoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lTitDao);
			cleanup(lConn);
		}
	}

	public void ExIncludiEscludiTitoloDaIstruttoria(TitoloCumulatoModel aTitoloCumulato) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoDAO lTitDao = null;

		try {
			lConn = getDBConnection();
			lTitDao = new TitoloCumulatoDAO(lConn);

			lTitDao.setFlagEscluso(aTitoloCumulato.getFlagEscluso());
			lTitDao.setCodOperatoreAggiornamento(aTitoloCumulato.getCodOperatoreAggiornamento());
			lTitDao.setDataAggiornamento(aTitoloCumulato.getDataAggiornamento());
			lTitDao.setCodUfficioAggiornamento(aTitoloCumulato.getCodUfficioAggiornamento());

			lTitDao.selCondizioneUpdate(aTitoloCumulato.getIdTitoloCumulato());
			lTitDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex.getMessage(), ex);
			throw new F3BException(
					"TitoloCumulatoController.ExEscludiTitoloDaIstruttoria: Non posso inserire: " + ex);
		} finally {
			cleanup(lTitDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aTitoloCumulato
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoDAO lTitDao = null;

		try {
			lConn = getDBConnection();
			lTitDao = new TitoloCumulatoDAO(lConn);
			lTitDao.selCondizioneUpdate(aTitoloCumulato.getIdTitoloCumulato());
			lTitDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx, daoEx);
			rollback(lConn);
			throw new F3BException(
					"TitoloCumulatoController.ExCancellaTitoloCumulato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTitDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la cancellazione FISICA dei dati del Titolo dall'istruttoria ovvero cancella tutti i dati
	 * analitici e quanti referenzia il titolo
	 */
	public void ExCancellaTitoloDaIstruttoriaById(BigDecimal aIdTitoloCumulato, String aTipoIscrizione)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio Cancellazione Titolo aIdTitoloCumulato = " + aIdTitoloCumulato);

		Connection lConn = null;

		TitoloCumulatoDAO lTitDao = null;
		ProcedimentoCumulatoDAO lProcCumDao = null;
		SoggettoCumulatoDAO lSoggDao = null;
		MisuraSicurezzaCumuloDAO lMisSicDao = null;
		BeneficioCumuloDAO lBeneficioDAO = null;
		BeneficioCumuloSqlDAO lBeneficioSqlDAO = null;
		TipologiaOrarioDAO lTipologiaOrarioDAO = null;
		ReatoCumuloSqlDAO lReaCumSqlDao = null;
		ReatoCumuloDAO lReaCumDao = null;
		CircostanzaCumuloDAO lCirCumDao = null;
		CircostanzaCumuloSqlDAO lCirCumSqlDao = null;
		PenaAccessoriaCumuloDAO lPenAccDao = null;
		PenaComplessivaCumuloDAO lPenCompCumDao = null;
		PenaComplessivaCumuloSqlDAO lPenCompCumSqlDao = null;
		SanzioneSostitutivaCumuloDAO lSanSostDao = null;
		ContinuazioneCumuloDAO lContCumDao = null;
		ContinuazioneCumuloSqlDAO lContCumSqlDao = null;
		MisuraCautelareCumuloDAO lMisuraCautelareDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecSqlDao = null;
		StatoEsecTitoloCumulatoDAO lStatoEsecDao = null;
		ComputiCumuloDAO lComputiDao = null;
		NotificaCumuloDAO lNotificaDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		EventoDAO lEveDao = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnEsiDao = null;
		AnnotazioneEsitoTrasmissioneSqlDAO lAnnEsiSqlDao = null;
		LibAnticipataCumuloDAO lLibAntDao = null;
		LibAnticipataCumuloSqlDAO lLibAntSqlDao = null;
		PeriodoLibAntCumuloDAO lPeriodoLibAntDao = null;
		PosizioneGiuridicaCumuloDAO lPosizGiurDao = null;

		try {
			lConn = getDBConnection();

			// Cancello i Reati
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello i Reati");
			ReatoCumuloModel lReatoRicerca = new ReatoCumuloModel();
			lReatoRicerca.setTitIdTitoloCumulato(aIdTitoloCumulato);

			lReaCumSqlDao = new ReatoCumuloSqlDAO(lConn);
			lReaCumSqlDao.ricercaReatoCumulo(lReatoRicerca);
			Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(lReaCumSqlDao.getModels());

			if (lReati != null) {
				lReaCumDao = new ReatoCumuloDAO(lConn);

				for (int i = 0; i < lReati.size(); i++) {
					ReatoCumuloModel lReato = lReati.elementAt(i);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cancello Reato con id=" + lReato.getIdReatoCum());

					lReaCumDao.setCondizioneUpdate(lReato.getIdReatoCum());

					lReaCumDao.delete();

					lReaCumDao.stop();
				}
			}

			// Cancello Eventuali Circostanze Aggravanti/Attenuanti
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello le Circostanze...");

			lCirCumSqlDao = new CircostanzaCumuloSqlDAO(lConn);
			lCirCumSqlDao.ricercaCircostanzeCumuloByTitolo(aIdTitoloCumulato);
			Vector<CircostanzaCumuloModel> lCircostanze = new Vector<CircostanzaCumuloModel>(
					lCirCumSqlDao.getModels());

			if (lCircostanze != null && lCircostanze.size() > 0) {
				lCirCumDao = new CircostanzaCumuloDAO(lConn);

				for (int i = 0; i < lCircostanze.size(); i++) {
					CircostanzaCumuloModel lCircostanzaMod = lCircostanze.elementAt(i);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("Cancello Circostanza con id=" + lCircostanzaMod.getIdCircostanzaCumulo());

					lCirCumDao.setCondizioneUpdate(lCircostanzaMod.getIdCircostanzaCumulo());
					lCirCumDao.delete();
					lCirCumDao.stop();
				}
			}

			// Cancello La Pena Principale
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello La Pena Principale");
			// PenaComplessivaCumuloDAO lPenCompCumDao = null;
			lPenCompCumSqlDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lPenCompCumSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(aIdTitoloCumulato);
			PenaComplessivaCumuloModel lPenCompMod = (PenaComplessivaCumuloModel) lPenCompCumSqlDao
					.getModelByKey();

			if (lPenCompMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena Principale trovata id " + lPenCompMod.getIdPenaComplessivaCum());

				//
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello le Sanzioni Sostitutive legate alla PC");
				lSanSostDao = new SanzioneSostitutivaCumuloDAO(lConn);
				lSanSostDao.setCondizioneByIdPenaComplessivaCum(lPenCompMod.getIdPenaComplessivaCum());
				lSanSostDao.delete();
				lSanSostDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello le Continuazione legate alla PC");
				lContCumDao = new ContinuazioneCumuloDAO(lConn);
				lContCumDao.setCondizioneByIdPenaComplessiva(lPenCompMod.getIdPenaComplessivaCum());
				lContCumDao.delete();
				lContCumDao.stop();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello la Pena Complessiva");
				lPenCompCumDao = new PenaComplessivaCumuloDAO(lConn);
				lPenCompCumDao.setCondizioneUpdate(lPenCompMod.getIdPenaComplessivaCum());
				lPenCompCumDao.delete();
				lPenCompCumDao.stop();
			}

			// Cancello Le Pene Accessorie
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello Le Pene Accessorie");
			lPenAccDao = new PenaAccessoriaCumuloDAO(lConn);
			lPenAccDao.selCondizioneUpdateByIdTitolo(aIdTitoloCumulato);
			lPenAccDao.delete();
			lPenAccDao.stop();

			// Cancello Le Misure Sicurezza
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello Le Misure Sicurezza");
			lMisSicDao = new MisuraSicurezzaCumuloDAO(lConn);
			lMisSicDao.selCondizioneUpdateByIdTitolo(aIdTitoloCumulato);
			lMisSicDao.delete();
			lMisSicDao.stop();

			// Cancello le Misure Cautelari
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello Le Misure Cautelari");
			lMisuraCautelareDao = new MisuraCautelareCumuloDAO(lConn);
			lMisuraCautelareDao.selCondizioneUpdateByIdTitolo(aIdTitoloCumulato);
			lMisuraCautelareDao.delete();
			lMisuraCautelareDao.stop();

			// Cancello i Benefici/Revoche (tipologia Orario se presente)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello i Benefici/Revoche");
			lBeneficioSqlDAO = new BeneficioCumuloSqlDAO(lConn);
			BeneficioCumuloModel lBeneficioModelRicerca = new BeneficioCumuloModel();
			lBeneficioModelRicerca.setTitIdTitoloCumulato(aIdTitoloCumulato);

			lBeneficioSqlDAO.ricercaBeneficioCumulo(lBeneficioModelRicerca);
			Vector<BeneficioCumuloModel> lListaBenefici = new Vector<BeneficioCumuloModel>(
					lBeneficioSqlDAO.getModels());

			lTipologiaOrarioDAO = new TipologiaOrarioDAO(lConn);
			lBeneficioDAO = new BeneficioCumuloDAO(lConn);

			Iterator<BeneficioCumuloModel> iterBenefici = lListaBenefici.iterator();
			while (iterBenefici.hasNext()) {
				BeneficioCumuloModel lBeneficioModel = iterBenefici.next();

				// Cancello eventuale Tipologia Orario collegate al beneficio
				lTipologiaOrarioDAO
						.selCondizioneDeleteByIdBeneficioCumulo(lBeneficioModel.getIdBeneficioCumulo());
				lTipologiaOrarioDAO.delete();
				lTipologiaOrarioDAO.stop();

				// Cancello Beneficio
				lBeneficioDAO.setCondizioneUpdate(lBeneficioModel.getIdBeneficioCumulo());
				lBeneficioDAO.delete();
				lBeneficioDAO.stop();
			}

			// lBeneficioDAO = new BeneficioCumuloDAO (lConn);
			// lBeneficioDAO.selCondizioneUpdateByIdTitolo (aIdTitoloCumulato);
			// lBeneficioDAO.sel
			// lBeneficioDAO.delete();
			// lBeneficioDAO.stop();

			// Cancellazione dati dello Stato di Esecuzione (attività PM - GE - SORV)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero i Provvedimenti dello Stato Esecuzione...");
			lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lStatoEsecSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(aIdTitoloCumulato, null);
			Vector<StatoEsecTitoloCumulatoModel> lListaProvv = new Vector<StatoEsecTitoloCumulatoModel>(
					lStatoEsecSqlDao.getModels());
			lStatoEsecSqlDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Provvedimenti trovati: " + lListaProvv.size());

			lComputiDao = new ComputiCumuloDAO(lConn);
			lNotificaDao = new NotificaCumuloDAO(lConn);
			lStatoEsecDao = new StatoEsecTitoloCumulatoDAO(lConn);

			lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lLibAntDao = new LibAnticipataCumuloDAO(lConn);
			lPeriodoLibAntDao = new PeriodoLibAntCumuloDAO(lConn);

			Iterator<StatoEsecTitoloCumulatoModel> iterProvv = lListaProvv.iterator();
			while (iterProvv.hasNext()) {
				StatoEsecTitoloCumulatoModel lStatoModel = iterProvv.next();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("idStatoEsec = " + lStatoModel.getIdStatoEsecTitoloCumulato());

				// Cancello i COMPUTI_CUMULO se presenti
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello i COMPUTI_CUMULO se presenti");
				lComputiDao.selCondizioneUpdateByIdStatEsec(lStatoModel.getIdStatoEsecTitoloCumulato());
				lComputiDao.delete();
				lComputiDao.stop();

				// Cancello le NOTIFICHE_CUMULO se presenti
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello le NOTIFICHE_CUMULO se presenti");
				lNotificaDao = new NotificaCumuloDAO(lConn);
				lNotificaDao.selCondizioneUpdateByIdStatEsec(lStatoModel.getIdStatoEsecTitoloCumulato());
				lNotificaDao.delete();
				lNotificaDao.stop();

				// Aggiungere cancellazione eventuali altre tabelle
				// Liberazione Anticipata cumulo
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello le LIB_ANTICIPATA_CUMULO se presenti");
				lLibAntSqlDao
						.ricercaLibAnticipataCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());
				Vector<LibAnticipataCumuloModel> lListaLA = new Vector<LibAnticipataCumuloModel>(
						lLibAntSqlDao.getModels());

				for (LibAnticipataCumuloModel lLA : lListaLA) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cancellazione Periodi by idLA = " + lLA.getIdLibAnticipataCumulo());
					lPeriodoLibAntDao.selCondizioneLib_Id_LibAnt(lLA.getIdLibAnticipataCumulo());
					lPeriodoLibAntDao.delete();
					lPeriodoLibAntDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cancellazione LA = " + lLA.getIdLibAnticipataCumulo());
					// lLibAntDao.selCondizioneUpdateByIdStatEsec
					// (lStatoModel.getIdStatoEsecTitoloCumulato());
					lLibAntDao.selCondizioneUpdate(lLA.getIdLibAnticipataCumulo());
					lLibAntDao.delete();
					lLibAntDao.stop();
				}

				// Posizione Giuridica x Espiazione Attuale
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello PG Espiazione Attuale collegate al titolo");
				lPosizGiurDao = new PosizioneGiuridicaCumuloDAO(lConn);
				lPosizGiurDao.selCondizioneByIdTitolo(aIdTitoloCumulato);
				lPosizGiurDao.delete();
				lPosizGiurDao.stop();

				// Cancello infine lo stato esecuzione
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello infine lo stato esecuzione");
				lStatoEsecDao.selCondizioneUpdate(lStatoModel.getIdStatoEsecTitoloCumulato());
				lStatoEsecDao.delete();
				lStatoEsecDao.stop();

			}

			// Cancello il Soggetto
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello il Soggetto Cumulato");
			lSoggDao = new SoggettoCumulatoDAO(lConn);
			lSoggDao.selCondizioneUpdateByIdTitolo(aIdTitoloCumulato);
			lSoggDao.delete();
			lSoggDao.stop();

			if ("04".equals(aTipoIscrizione)) // 04 = Iscrizione in Istruttoeia Fascicolo proprio Ufficio
			{
				// Ricerco il Procedimento Cumulato per avere Eve_Id_Evento
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"--XX-- >>>>>>>>>>>>>>>  Ricerco il Procedimento Cumulato per avere Eve_id_Evento");
				lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(aIdTitoloCumulato);
				ProcedimentoCumulatoModel lProcMod = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();

				if (lProcMod != null && lProcMod.getEveIdEvento() != null) {
					// Ricerco e Cancello Annotazione_Esito_Trasmissione
					lAnnEsiDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);
					lAnnEsiSqlDao = new AnnotazioneEsitoTrasmissioneSqlDAO(lConn);

					lAnnEsiSqlDao.ricercaAnnotazioneEsitoTrasmissioneByIdEvento(lProcMod.getEveIdEvento());
					AnnotazioneEsitoTrasmissioneModel lAnnMod = (AnnotazioneEsitoTrasmissioneModel) lAnnEsiSqlDao
							.getModelByKey();

					lAnnEsiDao.selCondizioneUpdate(lAnnMod.getIdEsitoTrasmissione());
					lAnnEsiDao.delete();
					lAnnEsiDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("--XX-- >>>>>>>>>>>>>>>  Cancellato AnnotazioneEsitoTrasmissioneModel");

					// Cancello Evento
					lEveDao = new EventoDAO(lConn);
					lEveDao.selCondizioneUpdate(lProcMod.getEveIdEvento());
					lEveDao.delete();
					lEveDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("--XX-- >>>>>>>>>>>>>>>  Cancellato Evento");
				}
			}

			// Cancello il Procedimento Cumulato
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello il Procedimento Cumulato");
			lProcCumDao = new ProcedimentoCumulatoDAO(lConn);
			lProcCumDao.selCondizioneUpdateByIdTitolo(aIdTitoloCumulato);
			lProcCumDao.delete();
			lProcCumDao.stop();

			// ========================================================================
			// Cancello eventuali REF anche privi di FK al titolo es:
			// CONTINUAZIONE_CUMULO.TIT_ID_TITOLO_CUMULATO_CONT
			// ========================================================================
			lContCumSqlDao = new ContinuazioneCumuloSqlDAO(lConn);
			lContCumSqlDao.ricercaContinuazioneByIdTitoloCont(aIdTitoloCumulato);
			Vector<ContinuazioneCumuloModel> lListaContinuazioni = new Vector<ContinuazioneCumuloModel>(
					lContCumSqlDao.getModels());
			if (lContCumDao == null)
				lContCumDao = new ContinuazioneCumuloDAO(lConn);

			for (ContinuazioneCumuloModel lContinua : lListaContinuazioni) {
				siesLogger.debug("Aggiorno CONTINUAZIONE_CUMULO che punta il titolo da rimuovere: "
						+ lContinua.getIdContinuazioneCum());
				lContCumDao.setTitIdTitoloCumulatoCont(null);
				lContCumDao.setCondizioneUpdate(lContinua.getIdContinuazioneCum());
				lContCumDao.update();
			}

			// ========================================================================
			// Verifico se presenti riferimenti con benefici di altri titoli in istruttoria
			// in questo caso devo resettare i riferimenti
			// ========================================================================
			siesLogger.debug("Ricerco benefici altri titoli che puntano il titolo corrente");
			BeneficioCumuloModel lBenRich = new BeneficioCumuloModel();
			lBenRich.setTitIdTitoloCumulatoCollegato(aIdTitoloCumulato); // benefici che puntano il titolo
																			// corrente

			lBeneficioSqlDAO.ricercaBeneficioCumulo(lBenRich);

			Vector<BeneficioCumuloModel> lListaBeneficiIstr = new Vector<BeneficioCumuloModel>(
					lBeneficioSqlDAO.getModels());
			lBeneficioSqlDAO.stop();

			for (BeneficioCumuloModel lBenIstrRev : lListaBeneficiIstr) {
				siesLogger.debug("Resetto beneficio cumulo con id: " + lBenIstrRev.getIdBeneficioCumulo());
				// resetto il collegamento
				lBeneficioDAO.setTitIdTitoloCumulatoCollegato(null);
				lBeneficioDAO.setCondizioneUpdate(lBenIstrRev.getIdBeneficioCumulo());
				lBeneficioDAO.update();
				lBeneficioDAO.stop();
			}
			// ========================================================================

			// Cancello il Titolo
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello il Titolo");
			lTitDao = new TitoloCumulatoDAO(lConn);
			lTitDao.selCondizioneUpdate(aIdTitoloCumulato);
			lTitDao.delete();
			lTitDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx, daoEx);
			rollback(lConn);
			throw new F3BException(
					"TitoloCumulatoController.ExCancellaTitoloDaIstruttoriaById: Non posso Cancellare : "
							+ daoEx);
		} finally {
			cleanup(lTitDao);
			cleanup(lSoggDao);
			cleanup(lProcCumDao);
			cleanup(lMisSicDao);
			cleanup(lMisuraCautelareDao);
			cleanup(lBeneficioDAO);
			cleanup(lBeneficioSqlDAO);
			cleanup(lTipologiaOrarioDAO);
			cleanup(lCirCumDao);
			cleanup(lCirCumSqlDao);
			cleanup(lReaCumDao);
			cleanup(lReaCumSqlDao);
			cleanup(lPenAccDao);
			cleanup(lPenCompCumSqlDao);
			cleanup(lPenCompCumDao);
			cleanup(lSanSostDao);
			cleanup(lContCumDao);
			cleanup(lContCumSqlDao);
			cleanup(lComputiDao);
			cleanup(lNotificaDao);
			cleanup(lStatoEsecDao);
			cleanup(lProcCumSqlDao);
			cleanup(lEveDao);
			cleanup(lAnnEsiSqlDao);
			cleanup(lAnnEsiDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lStatoEsecSqlDao);
			cleanup(lLibAntDao);
			cleanup(lLibAntSqlDao);
			cleanup(lPeriodoLibAntDao);
			cleanup(lPosizGiurDao);

			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aTitoloCumulato
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountTitoloCumulato(TitoloCumulatoModel aTitoloCumulato) throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloCumulatoSqlDao.getCountTitoloCumulato(aTitoloCumulato);
			lTitoloCumulatoSqlDao.start();
			lTitoloCumulatoSqlDao.next();
			lCount = lTitoloCumulatoSqlDao.getBigDecimal("HowManyRecords");
			lTitoloCumulatoSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExGetCountTitoloCumulato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aTitoloCumulato
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaTitoloCumulatoPaged(TitoloCumulatoModel aTitoloCumulato, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lTitoloCumulati = new Vector();
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloCumulatoSqlDao.ricercaTitoloCumulatoPaged(aTitoloCumulato, aPage);
			lTitoloCumulati = new Vector(lTitoloCumulatoSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulatoPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);
			cleanup(lConn);
		}
		return lTitoloCumulati;
	}

	public ProcedimentoCumulatoModel ExRicercaProcedimentoCumulatoByIdTitolo(BigDecimal aIdTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		ProcedimentoCumulatoModel lProcedimentoCumulatoMod = new ProcedimentoCumulatoModel();
		ProcedimentoCumulatoSqlDAO lProcedimentoCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lProcedimentoCumulatoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lProcedimentoCumulatoSqlDao.ricercaProcedimentoCumulatoByIdTitolo(aIdTitoloCumulato);
			lProcedimentoCumulatoMod = (ProcedimentoCumulatoModel) lProcedimentoCumulatoSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaProcedimentoCumulatoByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lProcedimentoCumulatoSqlDao);
			cleanup(lConn);
		}

		return lProcedimentoCumulatoMod;
	}

	/**
	 * @param aIdTitoloCumulato
	 * @return
	 * @throws F3BException
	 */
	public SoggettoCumulatoModel ExRicercaSoggettoCumulatoByIdTitolo(BigDecimal aIdTitoloCumulato)
			throws F3BException {

		Connection lConn = null;

		SoggettoCumulatoModel lSoggettoCumulatoMod = new SoggettoCumulatoModel();
		SoggettoCumulatoSqlDAO lSoggettoCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lSoggettoCumulatoSqlDao = new SoggettoCumulatoSqlDAO(lConn);
			lSoggettoCumulatoSqlDao.ricercaSoggettoCumulatoByIdTitolo(aIdTitoloCumulato);
			lSoggettoCumulatoMod = (SoggettoCumulatoModel) lSoggettoCumulatoSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaSoggettoCumulatoByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lSoggettoCumulatoSqlDao);
			cleanup(lConn);
		}

		return lSoggettoCumulatoMod;
	}

	public ProcedimentoCumulatoModel ExInserisciProcedimentoCumulato(
			ProcedimentoCumulatoModel aProcedimentoCumulato) throws F3BException {

		Connection lConn = null;
		ProcedimentoCumulatoDAO lProDao = null;
		ProcedimentoCumulatoModel lProMod = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProcedimentoCumulatoDAO(lConn);
			lProDao.setDAOFromModel(aProcedimentoCumulato);
			BigDecimal lSequence = lProDao.insert();
			commit(lConn);
			lProMod = new ProcedimentoCumulatoModel(aProcedimentoCumulato);
			lProMod.setMessage("Inserimento avvenuto correttamente!");
			lProMod.setIdProcedimentoCumulato(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex.getMessage(), ex);
			throw new F3BException("TitoloCumulatoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}

		return lProMod;
	}

	/**
	 * Ricerca in chiave del ProcedimentoCumulato
	 *
	 * @param aIdProcedimentoCumulato
	 * @return
	 * @throws F3BException
	 */
	public ProcedimentoCumulatoModel ExRicercaProcedimentoCumulatoById(BigDecimal aIdProcedimentoCumulato)
			throws F3BException {

		Connection lConn = null;
		ProcedimentoCumulatoModel lProcedimentoCumulatoMod = new ProcedimentoCumulatoModel();
		ProcedimentoCumulatoSqlDAO lProcedimentoCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lProcedimentoCumulatoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lProcedimentoCumulatoSqlDao.ricercaProcedimentoCumulatoByKey(aIdProcedimentoCumulato);
			lProcedimentoCumulatoMod = (ProcedimentoCumulatoModel) lProcedimentoCumulatoSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaProcedimentoCumulatoById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lProcedimentoCumulatoSqlDao);
			cleanup(lConn);
		}

		return lProcedimentoCumulatoMod;
	}

	/**
	 * Ricerca del ProcedimentoCumulato per Fascicolo siep di origine
	 *
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public ProcedimentoCumulatoModel ExRicercaProcedimentoCumulatoByDatiFascicoloSiep(
			ProcedimentoCumulatoModel aProcedimentoCumulato, BigDecimal aIdIstruttoria) throws F3BException {

		Connection lConn = null;
		ProcedimentoCumulatoModel lProcedimentoCumulatoMod = new ProcedimentoCumulatoModel();
		ProcedimentoCumulatoSqlDAO lProcedimentoCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lProcedimentoCumulatoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lProcedimentoCumulatoSqlDao.ricercaProcedimentoCumulato(aProcedimentoCumulato, aIdIstruttoria);
			lProcedimentoCumulatoMod = (ProcedimentoCumulatoModel) lProcedimentoCumulatoSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaProcedimentoCumulatoByDatiFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lProcedimentoCumulatoSqlDao);
			cleanup(lConn);
		}

		return lProcedimentoCumulatoMod;
	}

	/**
	 * @param aProcedimentoCumulato
	 * @throws F3BException
	 */
	public void ExModificaProcedimentoCumulato(ProcedimentoCumulatoModel aProcedimentoCumulato)
			throws F3BException {

		Connection lConn = null;
		ProcedimentoCumulatoDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProcedimentoCumulatoDAO(lConn);
			lProDao.setDAOFromModelForUpdate(aProcedimentoCumulato);
			lProDao.selCondizioneUpdate(aProcedimentoCumulato.getIdProcedimentoCumulato());
			lProDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExModificaProcedimentoCumulato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaProcedimentoCumulato(ProcedimentoCumulatoModel aProcedimentoCumulato)
			throws F3BException {

		Connection lConn = null;
		ProcedimentoCumulatoDAO lProDao = null;

		try {
			lConn = getDBConnection();
			lProDao = new ProcedimentoCumulatoDAO(lConn);
			lProDao.selCondizioneUpdate(aProcedimentoCumulato.getIdProcedimentoCumulato());
			lProDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			rollback(lConn);
			throw new F3BException(
					"TitoloCumulatoController.ExCancellaProcedimentoCumulato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lProDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo che recupera lo stato di esecuzione di un titolo in istruttoria dal fascicolo Originario
	 *
	 * @param aIdFascicolo
	 * @param aPage
	 *            se 0 la ricerca va fatto non paginata
	 * @return
	 * @throws F3BException
	 */
	public Vector<MisuraAlternativaAggregatoModel> ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged(
			BigDecimal aIdFascicolo, int aPage) throws F3BException {

		Connection lConn = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero Stato esecuzione per fascicolo con id = " + aIdFascicolo);

		EventoSimeoneSqlDAO lEveSqlDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		MisuraAlternativaSqlDAO lMisAltSqlDao = null;

		MisuraAlternativaModel lMisMod;
		MisuraAlternativaAggregatoModel lMisAggregato = null;

		Vector<MisuraAlternativaAggregatoModel> lAggregato = new Vector<>();
		Vector<TenoreModel> lTenori = null;

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSimeoneSqlDAO(lConn);
			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
			lTenSqlDao = new TenoreSqlDAO(lConn);
			lMisAltSqlDao = new MisuraAlternativaSqlDAO(lConn);

			// Recupera tutti gli eventi con FLAG_VIDEO_SIEP = 'S' legati al fascicolo
			lEveSqlDao.ricercaEventiPerStatoEsecuzioneCumuloByIdFascicoloPaged(aIdFascicolo, aPage, 10);

			lEveSqlDao.start();
			while (lEveSqlDao.next()) {
				lMisAggregato = new MisuraAlternativaAggregatoModel();

				EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelEvento();

				// **** get Blob **** ??? serve il blob in questa fase???
				lEveMod.setDocBlobOut(lEveSqlDao.getBlob());

				lMisAggregato.getEventoNotifica().setEvento(lEveMod);

				// se il tipo provvedimento è 02 cerco il decreto altrimenti è 03 e cerco il
				// l'ordinanza e il relativo tenore!!
				if (lEveMod != null && lEveMod.getCodTipoProvvedimento() != null) {
					if (lEveMod.getCodTipoProvvedimento().equals("02")) {
						DepositoDecretoModel lDepDecMod = null;
						lDepDecSqlDao.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(
								lEveMod.getIdEvento());
						lDepDecSqlDao.start();
						if (lDepDecSqlDao.next()) {
							lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelNoDescTipoDecreto();
						}
						lDepDecSqlDao.stop();

						if (lDepDecMod != null) {
							lMisAggregato.setDepositoDecreto(lDepDecMod);
							// tenore
							lTenSqlDao.ricercaTenoriByDecretoOrderByPesoNoGenProc(
									lDepDecMod.getIdDepositoDecreto());
							lTenori = new Vector<TenoreModel>(lTenSqlDao.getModels());
							lMisAggregato.setTenori(lTenori.toArray(new TenoreModel[0]));
						}
					} else if (lEveMod.getCodTipoProvvedimento().equals("03")) {
						DepositoOrdinanzaPcModel lDepOrdPCMod = null;
						lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveMod.getIdEvento());
						lDepOrdPCMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();
						if (lDepOrdPCMod != null) {
							lMisAggregato.setDepositoOrdinanzaPc(lDepOrdPCMod);
							// tenore
							lTenSqlDao.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(
									lDepOrdPCMod.getIdDepositoOrdinanzaPc());
							lTenori = new Vector<TenoreModel>(lTenSqlDao.getModels());
							lMisAggregato.setTenori(lTenori.toArray(new TenoreModel[0]));
						}
					}
				}

				// ==============================
				// ricerca misura alternativa
				// ==============================
				lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(lEveMod.getIdEvento());
				lMisMod = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();
				lMisAggregato.setMisuraAlternativa(lMisMod);

				lAggregato.add(lMisAggregato);
			}

			lEveSqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx, daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged: "
							+ daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe, sqe);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex, ex);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged: " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lMisAltSqlDao);

			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Num eventi recuperati = " + lAggregato.size());
		return lAggregato;
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequence. Il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 *
	 * @param aTitCumModel
	 * @param lConn
	 * @return
	 * @since - MEV 42 Cumulo Step2
	 */
	public String ExInserisciTitoliCumulatiWithoutSequence(TitoloCumulatoModel aTitCumModel, Connection aConn)
			throws F3BException {

		String EsitodiRitorno = "00000";
		TitoloCumulatoDAO lTitoCumDao = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		Connection lConn = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBTransaction();
			lTitoCumDao = new TitoloCumulatoDAO(lConn);

			if (aTitCumModel != null && aTitCumModel.getIdTitoloCumulato() != null) {
				lTitoCumDao.setDAOFromModel(aTitCumModel);
				lTitoCumDao.setWithoutSequence(true);
				lTitoCumDao.insert();
				lTitoCumDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("Titolo Cumulo gia' presente...>" + aTitCumModel.getIdTitoloCumulato() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Titolo Cumulo! ");
			}
		} finally {
			cleanup(lTitoCumDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aConn == null)
				cleanup(lConn);
		}

		return EsitodiRitorno;
	} // CHIUDE ExInserisciTitoliCumulatiWithoutSequence()

	public String ExInserisciProcedimentoCumulatoWithoutSequence(
			ProcedimentoCumulatoModel aProcedimentoCumulato, Connection aConn) throws F3BException {

		String EsitodiRitorno = "00000";
		ProcedimentoCumulatoDAO lProcCumDao = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		Connection lConn = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBTransaction();
			lProcCumDao = new ProcedimentoCumulatoDAO(lConn);

			if (aProcedimentoCumulato != null && aProcedimentoCumulato.getIdProcedimentoCumulato() != null) {
				lProcCumDao.setDAOFromModel(aProcedimentoCumulato);
				lProcCumDao.setWithoutSequence(true);
				lProcCumDao.insert();
				lProcCumDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Procedimento Cumulato gia' presente...>"
						+ aProcedimentoCumulato.getIdProcedimentoCumulato() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Procedimento Cumulato! ");
			}
		} finally {
			cleanup(lProcCumDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aConn == null)
				cleanup(lConn);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciProcedimentoCumulatoWithoutSequence()

	public TitoloCumulatoModel ExRicercaTitoloCumulatoPeneAccessorieCum(BigDecimal aIdTitoloCumulato,
			Vector<String> listaIdPenaAcc) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoModel lTitoloCumulatoMod = new TitoloCumulatoModel();
		Vector<PenaAccessoriaCumuloModel> VecPA = new Vector<>();

		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);

			lTitoloCumulatoSqlDao.ricercaTitoloCumulatoByKey(aIdTitoloCumulato);
			lTitoloCumulatoMod = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModelByKey();

			if (lTitoloCumulatoMod != null && lTitoloCumulatoMod.getIdTitoloCumulato() != null) {
				for (int k = 0; k < listaIdPenaAcc.size(); k++) {
					lPACumSqlDao.ricercaPenaAccessoriaCumuloByKey(new BigDecimal(listaIdPenaAcc.get(k)));
					PenaAccessoriaCumuloModel lPAMod = (PenaAccessoriaCumuloModel) lPACumSqlDao
							.getModelByKey();

					if (lPAMod != null && lPAMod.getIdPenaAccessoriaCumulo() != null) {
						VecPA.add(lPAMod);
					}
				}

				if (VecPA != null && VecPA.size() > 0) {
					lTitoloCumulatoMod.setPeneAccessorieCumulo(VecPA);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulatoPeneAccessorieCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lPACumSqlDao);

			cleanup(lConn);
		}

		return lTitoloCumulatoMod;
	} // chiude ExRicercaTitoloCumulatoPeneAccessorieCum()

	public Vector<TitoloCumulatoModel> ExRicercaTitoliCumulatiPeneAccessorieCum(String[] lIdTitoliSelezionati)
			throws F3BException {

		Connection lConn = null;
		Vector<TitoloCumulatoModel> lTitoloCumulati = new Vector<>();
		Vector<String> listaIdPA = new Vector<>();

		TitoloCumulatoModel lTitoloCumulatoMod = new TitoloCumulatoModel();
		String lIdTitolo = "";
		String salvaTitolo = "";

		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);

			// Preparazione Lista dei Titoli selezionati
			String Ele = "";
			for (int i = 0; i < lIdTitoliSelezionati.length; i++) {
				Ele = lIdTitoliSelezionati[i];
				String[] EleSpl = Ele.split(";");

				lIdTitolo = EleSpl[0];
				listaIdPA.add(EleSpl[1]);

				if (!lIdTitolo.equals(salvaTitolo)) {
					salvaTitolo = lIdTitolo;

					lTitoloCumulatoSqlDao.ricercaTitoloCumulatoByKey(new BigDecimal(lIdTitolo));
					lTitoloCumulatoMod = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModelByKey();

					// aggiungo il Titolo alla Lista Titoli da restituire
					if (lTitoloCumulatoMod != null && lTitoloCumulatoMod.getIdTitoloCumulato() != null)
						lTitoloCumulati.add(lTitoloCumulatoMod);
				}
			}

			// preparazione delle Liste di Pene_Accessorie selezionate
			lTitoloCumulatoMod = null;
			Iterator itx = lTitoloCumulati.iterator();
			while (itx.hasNext()) {
				lTitoloCumulatoMod = (TitoloCumulatoModel) itx.next();
				Vector<PenaAccessoriaCumuloModel> lVecPA = new Vector<>();

				lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCum(lTitoloCumulatoMod.getIdTitoloCumulato());
				lPACumSqlDao.start();
				while (lPACumSqlDao.next()) {
					PenaAccessoriaCumuloModel lPAMod = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();

					Iterator itxP = listaIdPA.iterator();
					while (itxP.hasNext()) {
						// String lId = (String)itxP.next();
						if (lPAMod.getIdPenaAccessoriaCumulo()
								.compareTo(new BigDecimal((String) itxP.next())) == 0)
							lVecPA.add(lPAMod);
					}
				}
				lPACumSqlDao.stop();

				// aggregazione delle P.A. al Titolo_Cumulato_Model
				if (lVecPA != null && lVecPA.size() > 0)
					lTitoloCumulatoMod.setPeneAccessorieCumulo(lVecPA);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoliCumulatiPeneAccessorieCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lConn);
		}

		return lTitoloCumulati;
	} // Chiude ExRicercaTitoliCumulatiPeneAccessorieCum()

	public TitoloCumulatoModel ExRicercaTitoloCumulatoStatoEsecTitoloCum(BigDecimal aIdTitoloCumulato,
			BigDecimal aIdStatoEsecTCum) throws F3BException {

		Connection lConn = null;
		TitoloCumulatoModel lTitoloCumulatoMod = new TitoloCumulatoModel();
		TitoloCumulatoSqlDAO lTitoloCumulatoSqlDao = null;

		StatoEsecTitoloCumulatoSqlDAO lStatoEseSqlDao = null;
		StatoEsecTitoloCumulatoModel lStatoEseModel = null;

		try {
			lConn = getDBConnection();
			lTitoloCumulatoSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lStatoEseSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			lTitoloCumulatoSqlDao.ricercaTitoloCumulatoByKey(aIdTitoloCumulato);
			lTitoloCumulatoMod = (TitoloCumulatoModel) lTitoloCumulatoSqlDao.getModelByKey();

			if (lTitoloCumulatoMod != null && lTitoloCumulatoMod.getIdTitoloCumulato() != null) {
				lStatoEseSqlDao.ricercaStatoEsecTitoloCumulatoByKey(aIdStatoEsecTCum);
				lStatoEseModel = (StatoEsecTitoloCumulatoModel) lStatoEseSqlDao.getModelByKey();

				if (lStatoEseModel != null && lStatoEseModel.getIdStatoEsecTitoloCumulato() != null) {
					lTitoloCumulatoMod.setStatoEsecTitoloCumulato(lStatoEseModel);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"TitoloCumulatoController.ExRicercaTitoloCumulatoStatoEsecTitoloCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lTitoloCumulatoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lStatoEseSqlDao);

			cleanup(lConn);
		}

		return lTitoloCumulatoMod;
	} // Chiude ExRicercaTitoloCumulatoStatoEsecTitoloCum()

} // CHIUDE TitoloCumulatoController()