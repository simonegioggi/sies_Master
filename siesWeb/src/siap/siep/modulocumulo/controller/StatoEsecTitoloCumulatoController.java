package siap.siep.modulocumulo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioDAO;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.dao.ComputiCumuloDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.NotificaCumuloDAO;
import siap.siep.modulocumulo.dao.NotificaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.dao.RichPMStatoEsecCumSqlDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoDAO;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.RichPMStatoEsecCumModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.motivoevento.dao.MotivoEventoSqlDAO;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.nuovaistanza.dao.NuovaIstanzaSqlDAO;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.penaresidua.dao.PenaPrecedenteSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaPrecedenteModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: StatoEsecTitoloCumulatoController
 * </p>
 * <p>
 * Description: Classe Controller per StatoEsecTitoloCumulato
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecTitoloCumulatoController extends SiapController implements IStatoEsecTitoloCumulato {

	/*****************************************************************************
	 * Effettua l'inserimento di un StatoEsecTitoloCumulato a partire dai dati contenuti nel Model
	 *
	 * @param aStatoEsecTitoloCumulato
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecTitoloCumulatoModel ExInserisciStatoEsecTitoloCumulato(
			StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato) throws F3BException {

		Connection lConn = null;
		StatoEsecTitoloCumulatoDAO lStaDao = null;
		StatoEsecTitoloCumulatoModel lStaMod = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStaDao.setDAOFromModel(aStatoEsecTitoloCumulato);
			BigDecimal lSequence = lStaDao.insert();
			commit(lConn);
			lStaMod = new StatoEsecTitoloCumulatoModel(aStatoEsecTitoloCumulato);
			lStaMod.setMessage("Inserimento avvenuto correttamente!");
			lStaMod.setIdStatoEsecTitoloCumulato(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStaMod;
	}

	/***************************************************************************************************
	 * Effettua l'inserimento di StatoEsecTitoloCumulato a partire da un vettore di dati Insert di
	 * STATO_ESEC_TITOLO_CUMULATO e NOTIFICHE_CUMULO. L'Inserimento avviene in modalità NoSequence (il valore
	 * della Primary_Key è già presente nel model/Dao di Input)
	 *
	 * @param Vector
	 *            <StatoEsecTitoloCumulatoModel> con i dati da inserire
	 * @return String lCodEsito, riporta zero se Insert è OK
	 * @throws F3BException
	 * @since MEV 42 Cumulo STEP2
	 ****************************************************************************************************/
	public String ExInserisciStatoEsecTitoloCumulatoFullWithoutSequence(
			Vector<StatoEsecTitoloCumulatoModel> aVecStatoEsec, Connection aConn) throws F3BException {

		String lCodEsito = "00000";
		String QualeOggetto = "";
		BigDecimal QualeId = null;

		StatoEsecTitoloCumulatoDAO lStaDao = null;
		StatoEsecTitoloCumulatoModel lStaMod = null;

		NotificaCumuloDAO lNotificaDao = null;

		try {
			lStaDao = new StatoEsecTitoloCumulatoDAO(aConn);
			if (aVecStatoEsec != null && aVecStatoEsec.size() > 0) {
				for (int i = 0; i < aVecStatoEsec.size(); i++) {
					lStaMod = aVecStatoEsec.get(i);

					QualeOggetto = "Stato_Esec_Titolo_Cumulato";
					QualeId = lStaMod.getIdStatoEsecTitoloCumulato();

					try {
						lStaDao.setDAOFromModel(lStaMod);
						lStaDao.setWithoutSequence(true);
						lStaDao.insert();
						lStaDao.stop();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn(
									QualeOggetto + " gia' presente in archivio ... id= >" + QualeId + "<");
							lCodEsito = "00001";
						} else {
							throw ex;
						}
					}

					// ====================================
					// Inserisco le notifiche se presenti
					// ====================================
					if (lStaMod.getListaNotifiche() != null) {
						Vector<NotificaCumuloModel> lListaNotifiche = lStaMod.getListaNotifiche();
						Iterator<NotificaCumuloModel> lNotIter = lListaNotifiche.iterator();

						QualeOggetto = "Notifica_Cumulo";
						lNotificaDao = new NotificaCumuloDAO(aConn);
						while (lNotIter.hasNext()) {
							try {
								NotificaCumuloModel lNotifica = lNotIter.next();
								QualeId = lNotifica.getIdNotificaCumulo();

								lNotificaDao.setDAOFromModel(lNotifica);
								lNotificaDao.setWithoutSequence(true);
								lNotificaDao.insert();
								lNotificaDao.stop();
							} catch (DAOException ex) {
								if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
									siesLogger.warn(QualeOggetto + " gia' presente in archivio ... id= >"
											+ QualeId + "<");
									lCodEsito = "00001";
								} else {
									throw ex;
								}
							}
						}
					}
				}
			}
		} catch (DAOException ex) {
			lCodEsito = "01400";
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire --->" + QualeOggetto + " - id= >" + QualeId + "<");
		} finally {
			cleanup(lStaDao);
			cleanup(lNotificaDao);
		}

		return lCodEsito;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati StatoEsecTitoloCumulato
	 *
	 * @param aStatoEsecTitoloCumulato
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		Vector lStatoEsecTitoloCumulati = new Vector();
		StatoEsecTitoloCumulatoDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStaDao.setCondizioni(aStatoEsecTitoloCumulato);
			lStaDao.setOrderBy();
			lStaDao.start();
			while (lStaDao.next()) {
				lStatoEsecTitoloCumulati.add(lStaDao.getModel());
			}
			lStaDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaStatoEsecTitoloCumulato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStatoEsecTitoloCumulati;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public StatoEsecTitoloCumulatoModel ExRicercaStatoEsecTitoloCumulatoById(
			BigDecimal aIdStatoEsecTitoloCumulato) throws F3BException {

		Connection lConn = null;
		StatoEsecTitoloCumulatoModel lStatoEsecTitoloCumulatoMod = new StatoEsecTitoloCumulatoModel();
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatoByKey(aIdStatoEsecTitoloCumulato);
			lStatoEsecTitoloCumulatoMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoloCumulatoSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaStatoEsecTitoloCumulatoById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lConn);
		}

		return lStatoEsecTitoloCumulatoMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'StatoEsecTitoloCumulato Viene fatto l'update di tutti i campi del
	 * record recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella
	 * verranno impostati a null
	 *
	 * @param aStatoEsecTitoloCumulato
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		StatoEsecTitoloCumulatoDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStaDao.setDAOFromModel(aStatoEsecTitoloCumulato);
			lStaDao.selCondizioneUpdate(aStatoEsecTitoloCumulato.getIdStatoEsecTitoloCumulato());
			lStaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("StatoEsecTitoloCumulatoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aStatoEsecTitoloCumulato
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		StatoEsecTitoloCumulatoDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStaDao.selCondizioneUpdate(aStatoEsecTitoloCumulato.getIdStatoEsecTitoloCumulato());
			lStaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExCancellaStatoEsecTitoloCumulato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aStatoEsecTitoloCumulato
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountStatoEsecTitoloCumulato(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lStatoEsecTitoloCumulatoSqlDao.getCountStatoEsecTitoloCumulato(aStatoEsecTitoloCumulato);
			lStatoEsecTitoloCumulatoSqlDao.start();
			lStatoEsecTitoloCumulatoSqlDao.next();
			lCount = lStatoEsecTitoloCumulatoSqlDao.getBigDecimal("HowManyRecords");
			lStatoEsecTitoloCumulatoSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExGetCountStatoEsecTitoloCumulato: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aStatoEsecTitoloCumulato
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaStatoEsecTitoloCumulatoPaged(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato,
			int aPage) throws F3BException {

		Connection lConn = null;
		Vector lStatoEsecTitoloCumulati = new Vector();
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();
			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatoPaged(aStatoEsecTitoloCumulato,
					aPage);
			lStatoEsecTitoloCumulati = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaStatoEsecTitoloCumulatoPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lConn);
		}
		return lStatoEsecTitoloCumulati;
	}

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaStatoEsecTitoloCumulatoByIdTitolo(
			BigDecimal aIdTitolo) throws F3BException {

		Connection lConn = null;

		Vector<StatoEsecTitoloCumulatoModel> lStatoEsecTitoloCumulati = new Vector();

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(aIdTitolo, null);

			lStatoEsecTitoloCumulati = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaStatoEsecTitoloCumulatoByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lConn);
		}
		return lStatoEsecTitoloCumulati;
	}

	/**
	 * @param aListaEventiDaInserire
	 * @param aListaEventiDaRimuovere
	 * @param aIdTitolo
	 *            = id del titolo
	 * @param aIdIstruttoria
	 *            = id dell'istruttoria
	 * @param aDatiOper
	 *            = model con i dati dell'opertore e ufficio inserimento
	 */
	public void ExAggiornaStatoEsecTitoloCumulatoByIdTitolo(Vector<BigDecimal> aListaEventiDaInserire,
			Vector<StatoEsecTitoloCumulatoModel> aListaEventiDaRimuovere, BigDecimal aIdTitolo,
			BigDecimal aIdIstruttoria, Connection aDBConnection, DatiOperazioneModel aDatiOper,
			boolean aPresaInCarico) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		EventoDAO lEventoDao = null;
		EventoSqlDAO lEventoSqlDao = null;
		MotivoEventoSqlDAO lMotivoEventoSqlDao = null;
		NotificaSqlDAO lNotificaSqlDao = null;
		NotificaCumuloDAO lNotificaCumuloDao = null;
		VerbaleSqlDAO lVerbaleSqlDao = null;
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;
		TitoloCumulatoDAO lTitoloDao = null;

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			lEventoDao = new EventoDAO(lConn);
			lEventoSqlDao = new EventoSqlDAO(lConn);

			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lNotificaSqlDao = new NotificaSqlDAO(lConn);
			lNotificaCumuloDao = new NotificaCumuloDAO(lConn);
			lVerbaleSqlDao = new VerbaleSqlDAO(lConn);
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lMotivoEventoSqlDao = new MotivoEventoSqlDAO(lConn);

			// ====================================================
			// Recupero l'evento origine e lo inserisco nel SET
			// ====================================================
			EventoModel lEventoModel = null;
			if (aListaEventiDaInserire != null) {
				for (int i = 0; i < aListaEventiDaInserire.size(); i++) {
					try {
						BigDecimal lIdEvento = aListaEventiDaInserire.elementAt(i);

						lEventoDao.setIdEvento(lIdEvento);
						lEventoDao.selByKey();
						lEventoModel = (EventoModel) lEventoDao.getModelByKey();
						lEventoDao.stop();

						StatoEsecTitoloCumulatoModel lStatoEsecModel = new StatoEsecTitoloCumulatoModel(
								lEventoModel);

						lStatoEsecModel.setCodOperatoreInserimento(aDatiOper.getCodOperatore());
						lStatoEsecModel.setCodUfficioInserimento(aDatiOper.getCodUfficio());
						lStatoEsecModel.setDataInserimento(aDatiOper.getData());

						lStatoEsecModel.setTitIdTitoloCumulato(aIdTitolo);
						lStatoEsecModel.setIstrIdIstruttoriaCumulo(aIdIstruttoria);
						lStatoEsecModel.setFlagStato("E");

						lStatoEsecTitoloCumulatoDao.setDAOFromModel(lStatoEsecModel);

						BigDecimal lIdStatoEsec = lStatoEsecTitoloCumulatoDao.insert();
						lStatoEsecModel.setIdStatoEsecTitoloCumulato(lIdStatoEsec);

						lStatoEsecTitoloCumulatoDao.stop();

						// ======================================================================
						// Attività del PM
						// ======================================================================

						// Se sospensione Simeone recupero lo stato delle notifiche
						if (StatoEsecuzioneCumuloUtils.isSospensioneC5Provv(lEventoModel.getCodMotivo())) {
							siesLogger.debug(
									"Sospensione Simeone: Decreto Sospensione. Recupero lo stato delle notifiche");

							lNotificaSqlDao.ricercaNotificaByEvento(lEventoModel.getIdEvento());

							Vector<NotificaModel> lListaNotifiche = new Vector(lNotificaSqlDao.getModels());
							Iterator<NotificaModel> lIterNotifiche = lListaNotifiche.iterator();
							while (lIterNotifiche.hasNext()) {
								NotificaModel lNotifica = lIterNotifiche.next();

								NotificaCumuloModel lNotificaCumulo = new NotificaCumuloModel(lNotifica);

								// lNotificaCumulo.setFlagStato("E");
								// lNotificaCumulo.setMotivoModifica(null);

								lNotificaCumulo.setStatIdStatoEsecTitCum(
										lStatoEsecModel.getIdStatoEsecTitoloCumulato());

								lNotificaCumulo.setCodOperatoreInserimento(
										lStatoEsecModel.getCodOperatoreInserimento());
								lNotificaCumulo.setDataInserimento(lStatoEsecModel.getDataInserimento());
								lNotificaCumulo
										.setCodUfficioInserimento(lStatoEsecModel.getCodUfficioInserimento());

								lNotificaCumuloDao.setDAOFromModel(lNotificaCumulo);
								lNotificaCumuloDao.insert();
								lNotificaCumuloDao.stop();
							}
						}

						// Se Verbale Vane Ricerche
						if (StatoEsecuzioneCumuloUtils.isSospensioneC5VVR(lEventoModel.getCodMotivo())) {
							siesLogger.debug(
									"Sospensione Simeone: VVR. Recupero l'autorità emittente e Data emissione");

							lVerbaleSqlDao.ricercaVerbaleByIdEvento(lEventoModel.getIdEvento());
							VerbaleModel lVerbale = (VerbaleModel) lVerbaleSqlDao.getModelByKey();

							if (lVerbale != null) {
								siesLogger.debug("Aggiorno i dati del Verbale: firmatario e data");
								lStatoEsecTitoloCumulatoDao.setDataEmissione(lVerbale.getDataEmissione());
								lStatoEsecTitoloCumulatoDao
										.setCodAutoritaEmittente(lVerbale.getCodTipoUfficioFirmatario());
								lStatoEsecTitoloCumulatoDao
										.setCodLuogoEmittente(lVerbale.getCodLuogoUfficioFirmatario());

								lStatoEsecTitoloCumulatoDao
										.selCondizioneUpdate(lStatoEsecModel.getIdStatoEsecTitoloCumulato());

								lStatoEsecTitoloCumulatoDao.update();
								lStatoEsecTitoloCumulatoDao.stop();
							}
						}

						// Sospensione Simeone - Istanza
						if (StatoEsecuzioneCumuloUtils.isSospensioneC5Istanza(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Sospensione Simeone: Istanza. Recupero i dati dell'istanza");

							lNuovaIstanzaSqlDao.ricercaNuovaIstanzaByEveIdEvento(lEventoModel.getIdEvento());
							NuovaIstanzaModel lNuovaIstanza = (NuovaIstanzaModel) lNuovaIstanzaSqlDao
									.getModelByKey();

							if (lNuovaIstanza != null && "C001".equals(lNuovaIstanza.getCodContenuto())) {
								siesLogger.debug(
										"Istanza C001. Aggiorno StatoEsecuzione con i dati dell'istanza");

								lStatoEsecTitoloCumulatoDao
										.setCodContenutoIstanza(lNuovaIstanza.getCodContenuto());
								lStatoEsecTitoloCumulatoDao.setDataIstanza(lNuovaIstanza.getDataIstanza());
								lStatoEsecTitoloCumulatoDao
										.setFlagIstanzaPresdep(lNuovaIstanza.getFlagPresdep());
								lStatoEsecTitoloCumulatoDao
										.setCodStatoIstanza(lNuovaIstanza.getCodStatoIstanza());

								// Se trasmessa al TDS
								if ("03".equals(lNuovaIstanza.getCodStatoIstanza())) {
									lStatoEsecTitoloCumulatoDao.setCodTipoUfficioDestinatario(
											lNuovaIstanza.getCodTipoUfficioDestinatario());
									lStatoEsecTitoloCumulatoDao
											.setCodLuogoDestinatario(lNuovaIstanza.getCodLuogoDestinatario());

									lStatoEsecTitoloCumulatoDao.setCodUfficioDestinatario(
											lNuovaIstanza.getCodUfficioDestinatario());

									// La data trasmissione la recupero dal record Notifica che viene aggiuto
									// al
									// momento
									// della trasmissione. ATTENZIONE che va recuperata solo la trasmissione
									// al TDS, TDSM, UDS, UDSM e la notifica più recente
									siesLogger.debug("Cerco le Notifiche...");
									lNotificaSqlDao.ricercaNotificaByEvento(lEventoModel.getIdEvento());

									Vector<NotificaModel> lListaNotifiche = new Vector(
											lNotificaSqlDao.getModels());
									siesLogger.debug("...Notifiche trovate: " + lListaNotifiche.size());

									Iterator<NotificaModel> lIterNotifiche = lListaNotifiche.iterator();
									while (lIterNotifiche.hasNext()) {
										NotificaModel lNotifica = lIterNotifiche.next();
										if (lNotifica.getUffCodUfficio() != null) {
											siesLogger.debug("Recupero i dati dell'ufficio");

											UfficioDAO lUffDao = new UfficioDAO(lConn);
											lUffDao.setCodUfficio(lNotifica.getUffCodUfficio());
											lUffDao.selByKey();

											UfficioModel lUffDest = (UfficioModel) lUffDao.getModelByKey();
											lUffDao.stop();
											siesLogger.debug("lUffDest = " + lUffDest);
											if (lUffDest != null
													&& ("TDS".equals(lUffDest.getCodTipoUfficio())
															|| "TDSM".equals(lUffDest.getCodTipoUfficio())
															|| "UDS".equals(lUffDest.getCodTipoUfficio())
															|| "UDSM".equals(lUffDest.getCodTipoUfficio()))) {
												lStatoEsecTitoloCumulatoDao.setCodTipoUfficioDestinatario(
														lUffDest.getCodTipoUfficio());
												lStatoEsecTitoloCumulatoDao
														.setCodLuogoDestinatario(lUffDest.getCodComune());
												lStatoEsecTitoloCumulatoDao
														.setCodUfficioDestinatario(lUffDest.getCodUfficio());

												lStatoEsecTitoloCumulatoDao
														.setDataTrasmissione(lNotifica.getDataInvio());
											}
										}
									}
								}

								//
								lStatoEsecTitoloCumulatoDao
										.selCondizioneUpdate(lStatoEsecModel.getIdStatoEsecTitoloCumulato());

								lStatoEsecTitoloCumulatoDao.update();
								lStatoEsecTitoloCumulatoDao.stop();
							}
						}

						// Se revoca Simeone
						if (StatoEsecuzioneCumuloUtils.isSospC5Revoca(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Sospensione Simeone: Revoca. Recupero i dati del motivo");

							lMotivoEventoSqlDao.ricercaMotivoEventoByEveIdEvento(lEventoModel.getIdEvento());
							MotivoEventoModel lMotivoEv = (MotivoEventoModel) lMotivoEventoSqlDao
									.getModelByKey();
							if (lMotivoEv != null) {
								lStatoEsecTitoloCumulatoDao
										.setCodMotivoRevoca(lMotivoEv.getCodMotivoRevoca());
								lStatoEsecTitoloCumulatoDao
										.setCodMotivoRevocaPm(lMotivoEv.getCodMotivoRevocaPm());
								lStatoEsecTitoloCumulatoDao.setNote(lMotivoEv.getMotivazioni());

								lStatoEsecTitoloCumulatoDao
										.selCondizioneUpdate(lStatoEsecModel.getIdStatoEsecTitoloCumulato());

								lStatoEsecTitoloCumulatoDao.update();
								lStatoEsecTitoloCumulatoDao.stop();
							}
						}

						// Ordinanza di revoca/rigetto MA (forzo "C5")
						if (StatoEsecuzioneCumuloUtils.isSospC5RevocaSorv(lEventoModel.getCodMotivo(),
								"C5")) {
							// Attenzione: l'ordinanza di revoca è generica. Per capire se trattasi della
							// revoca
							// Simeone
							// va verificato almeno se puntata dal provvedimento SIEP di revoca
							siesLogger.debug("Ordinanza di Revoca/Rigetto (" + lEventoModel.getCodMotivo()
									+ "). TEST C5: ricerco il provv di esecuzione");

							lEventoSqlDao.ricercaEventoByEveIdEvento(lEventoModel.getIdEvento());
							EventoModel lEveEsecuzione = (EventoModel) lEventoSqlDao.getModelByKey();
							lEventoSqlDao.stop();

							if (lEveEsecuzione != null) {
								siesLogger.debug("Trovato Evento di esecuzione: "
										+ lEveEsecuzione.getCodTipoProvvedimento() + " - "
										+ lEveEsecuzione.getCodMotivo());

								if (StatoEsecuzioneCumuloUtils
										.isSospC5Revoca(lEveEsecuzione.getCodMotivo())) {
									siesLogger.debug("E' ordinanza di rigetto Simeone: aggiorno i campi");

									this.caricaDatiSorv(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);

									lStatoEsecTitoloCumulatoDao.setFlagTipoSosp("C5");
									lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStatoEsec);
									lStatoEsecTitoloCumulatoDao.update();
									lStatoEsecTitoloCumulatoDao.stop();
								}
							}
						}

						// Se revoca 199 devo recuperare anche il motivo dalla tabella MOTIVO_EVENTO
						if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Rev(lEventoModel.getCodMotivo())) {

							lMotivoEventoSqlDao.ricercaMotivoEventoByEveIdEvento(lEventoModel.getIdEvento());
							MotivoEventoModel lMotivoEv = (MotivoEventoModel) lMotivoEventoSqlDao
									.getModelByKey();
							if (lMotivoEv != null) {
								lStatoEsecTitoloCumulatoDao
										.setCodMotivoRevoca(lMotivoEv.getCodMotivoRevoca());
								lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStatoEsec);
								lStatoEsecTitoloCumulatoDao.update();
								lStatoEsecTitoloCumulatoDao.stop();
							}
						}

						// Ordinanza di revoca della 199 - Recupero i dati da MA, deposito decreto,
						// deposito ordinanza e esito_tenore
						if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Sorv(lEventoModel.getCodMotivo(),
								"199")) {
							// Attenzione: l'ordinanza di revoca è generica. Per capire se trattasi della
							// revoca
							// Simeone
							// va verificato almeno se puntata dal provvedimento SIEP di revoca
							siesLogger.debug("Ordinanza di Revoca/Rigetto (" + lEventoModel.getCodMotivo()
									+ "). TEST 199: ricerco il provv di esecuzione");
							lEventoSqlDao.ricercaEventoByEveIdEvento(lEventoModel.getIdEvento());
							EventoModel lEveEsecuzione = (EventoModel) lEventoSqlDao.getModelByKey();
							lEventoSqlDao.stop();

							if (StatoEsecuzioneCumuloUtils
									.isSospensionePM199_Rev(lEveEsecuzione.getCodMotivo())) {
								siesLogger.debug("E' ordinanza di rigetto Simeone: aggiorno i campi");

								this.caricaDatiSorv(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);

								lStatoEsecTitoloCumulatoDao.setFlagTipoSosp("199");
								lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStatoEsec);
								lStatoEsecTitoloCumulatoDao.update();
								lStatoEsecTitoloCumulatoDao.stop();
							}
						}

						// 02/04/2019 MEV70 - Recupero il Cod. Esito per Stato Esecuzione decodificando il
						// Codice Natura Decisione della Misura Alternativa.
						if (StatoEsecuzioneCumuloUtils.isConcMisAlternativa(lEventoModel)) {

							MisuraAlternativaDAO lMisuraAltDao = null;
							lMisuraAltDao = new MisuraAlternativaDAO(lConn);

							lMisuraAltDao.setCondizioneByIdEvento(lEventoModel.getIdEvento());
							MisuraAlternativaModel lMisuraModel = null;
							lMisuraModel = (MisuraAlternativaModel) lMisuraAltDao.getModelByKey();
							lMisuraAltDao.stop();

							if ("CO".equals(lMisuraModel.getCodNaturaDecisione())) {
								lStatoEsecTitoloCumulatoDao.setCodEsito("0001");
							} else if ("IP".equals(lMisuraModel.getCodNaturaDecisione())) {
								lStatoEsecTitoloCumulatoDao.setCodEsito("0068");
							} else if ("ED".equals(lMisuraModel.getCodNaturaDecisione())) {
								lStatoEsecTitoloCumulatoDao.setCodEsito("0009");
							} else if ("AC".equals(lMisuraModel.getCodNaturaDecisione())) {
								lStatoEsecTitoloCumulatoDao.setCodEsito("0019");
							}
							lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStatoEsec);
							lStatoEsecTitoloCumulatoDao.update();
							lStatoEsecTitoloCumulatoDao.stop();

						}

						// Presofferto Carico i computi
						if (StatoEsecuzioneCumuloUtils.isPresofferto(lEventoModel)) {
							siesLogger.debug("Presofferto Carico i computi");
							this.caricaComputi(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);
						}

						// Fungibilità - Carico i computi
						if (StatoEsecuzioneCumuloUtils.isFungibilita(lEventoModel)) {
							siesLogger.debug("Fungibilità - Carico i computi");
							this.caricaComputi(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);

							// lStatoEsecTitoloCumulatoDao.setCodTipoIstante("U");
							// lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStatoEsec);
							// lStatoEsecTitoloCumulatoDao.update();
							// lStatoEsecTitoloCumulatoDao.stop();
						}

						// Espiazione
						if (StatoEsecuzioneCumuloUtils.isEspiazionePregressaPM(lEventoModel.getCodMotivo())) {
							// Devo inserire anche l'espiato su COMPUTI_CUMULO
							siesLogger.debug("Espiazione Pregressa PM");
							this.caricaComputiEspiatoPM(lEventoModel.getFasSieIdFascicoloSiep(),
									lEventoModel.getIdEvento(), lStatoEsecModel, lConn);
						}

						// Pagamento Pena Pecuniaria
						if (StatoEsecuzioneCumuloUtils.isPagamentoPP(lEventoModel)) {
							siesLogger.debug("Pagamento Pena Pecuniaria");
							this.caricaComputiPagamentoPP(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);
						}

						// ======================================================================
						// Attività del GE
						// ======================================================================
						// Amnistia Indulto
						if (StatoEsecuzioneCumuloUtils.isAmnistiaIndulto(lEventoModel)) {
							siesLogger.debug("Attività del GE - Amnistia Indulto");
							this.caricaComputiAmnistiaIndulto(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}

						// Depenalizzazione
						if (StatoEsecuzioneCumuloUtils.isDepenalizzazione(lEventoModel)) {
							siesLogger.debug("Attività del GE - Depenalizzazione");
							this.caricaComputiDepenIncost(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);
						}

						// Incostituzionalità
						if (StatoEsecuzioneCumuloUtils.isIncostituzionalita(lEventoModel)) {
							siesLogger.debug("Attività del GE - Incostituzionalità");
							this.caricaComputiDepenIncost(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);
						}

						// Sospensioni del GE
						if (StatoEsecuzioneCumuloUtils.isSospensioneGE(lEventoModel)) {
							siesLogger.debug("Attività del GE - Sospensione");
							this.caricaComputiSospensioneGE(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}

						// Interruzioni del GE
						// Attenzione!!!! Gli eventi 'Interruzione del GE' sono IDENTICI alle interruzioni
						// del PM. In SIEP la action è la stessa agganciata da due menu distinti.
						// Per cui i computi sono stati già caricati come interruzione del PM (Espiazione
						// Pregressa)
						// Non tutti: i codici in conflitto tra esp pregressa e Int del GE sono 0267 e 0270
						// PEZZA A COLORI!! Salto 0267 e 0270 già caricati come espiazione pregressa
						if (!"0267".equals(lEventoModel.getCodMotivo())
								&& !"0270".equals(lEventoModel.getCodMotivo())) {
							if (StatoEsecuzioneCumuloUtils.isInterruzioneGE(lEventoModel)) {
								siesLogger.debug("Attività del GE - Interruzioni");
								this.caricaComputiInterruzioneGE(lEventoModel.getIdEvento(), lStatoEsecModel,
										lConn);
							}
						}

						// Decisione della SORV. Concessione/Revoca/Sospensione Misure Alternative
						if (StatoEsecuzioneCumuloUtils.isConcMisAlternativa(lEventoModel)
								|| StatoEsecuzioneCumuloUtils.isRevocaMisAlternativa(lEventoModel)
								|| StatoEsecuzioneCumuloUtils.isSospMisAlternativa(lEventoModel)) {
							siesLogger.debug(
									"Attività della SORV - Concessione/Revoca/Sospensione Misure Alternative");
							this.caricaComputiMisuraAlternativaSORV(lEventoModel.getIdEvento(),
									lStatoEsecModel, lConn);
						}

						// Decisione della SORV.
						// Liberazioni Anticipate L.A. (Concessione/Revoca/Reclamo)
						// Rimedi Risarcitori e Reclamo Rimedi Risarcitori
						if (StatoEsecuzioneCumuloUtils.isLiberazioneAnticipata(lEventoModel)
								|| StatoEsecuzioneCumuloUtils.isRimediRisarcitori(lEventoModel)) {
							siesLogger.debug(
									"Attività della Sorveglianza - Liberazioni Anticipate / Rimedi Risarcitori ");
							this.caricaLiberazioniAnticipate(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}

						// Decisione della SORV. - Scomputo Permessi
						if (StatoEsecuzioneCumuloUtils.isScomputoPermessi(lEventoModel)) {
							siesLogger.debug("Attività della Sorveglianza - Scomputo Permessi");
							this.caricaScomputiPermessi(lEventoModel.getIdEvento(), lStatoEsecModel, lConn);
						}

						// Decisione della SORV. Sospensione Esecuzione Pena
						if (StatoEsecuzioneCumuloUtils.isSospEsecuzione(lEventoModel)) {
							siesLogger.debug("Attività della SORV - Sospensione Esecuzione Pena");
							this.caricaComputiMisuraAlternativaSORV(lEventoModel.getIdEvento(),
									lStatoEsecModel, lConn);
						}
						// Decisione della SORV. Differimento Pena
						if (StatoEsecuzioneCumuloUtils.isDifferimento(lEventoModel)) {
							siesLogger.debug("Attività della SORV - Differimento Pena");
							this.caricaComputiMisuraAlternativaSORV(lEventoModel.getIdEvento(),
									lStatoEsecModel, lConn);
						}
						// Decisione della SORV. Espulsione
						if (StatoEsecuzioneCumuloUtils.isEspulsione(lEventoModel)) {
							siesLogger.debug("Attività della SORV - Espulsione");
							this.caricaComputiMisuraAlternativaSORV(lEventoModel.getIdEvento(),
									lStatoEsecModel, lConn);
						}
						// 09/05/2019 Revoca Sospensione Condizionale della Pena GE.
						if (StatoEsecuzioneCumuloUtils.isRevocaSospCondPenaGE(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Attività del GE - Revoca Sospensione Condizionale della Pena");
							caricaComputiOrdRevocaSospCondPenaGE(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}
						// 22/02/2019 Ridet. Pena PM Altro - D'ufficio
						if (StatoEsecuzioneCumuloUtils
								.isRidPenaPMAltroDufficio(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Ridet. Pena PM Altro - D'ufficio");
							this.caricaComputiRidetPenaPMAltro(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}
						// 22/02/2019 Ridet. Pena PM Altro - Altra Autorità
						if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroAltAut(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Ridet. Pena PM Altro - Altra Autorità");
							this.caricaComputiRidetPenaPMAltro(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}
						// 22/02/2019 Ridet. Pena PM Altro - Giudice Esecuzione
						if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroGE(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Ridet. Pena PM Altro - Giudice Esecuzione");
							this.caricaComputiRidetPenaPMAltro(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}
						// 22/02/2019 Ridet. Pena PM Altro - Sorveglianza
						if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroSorv(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Ridet. Pena PM Altro - Sorveglianza");
							this.caricaComputiRidetPenaPMAltro(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}
						// 22/05/2019 Revoca Pena Sospesa
						if (StatoEsecuzioneCumuloUtils.isRevocaPenaSospesa(lEventoModel.getCodMotivo())) {
							siesLogger.debug("Attività del PM - Ordinanza/Sentenza Revoca Pena Sospesa");
							this.caricaComputiRevocaPenaSospesa(lEventoModel.getIdEvento(), lStatoEsecModel,
									lConn);
						}

						commit(lConn);
					} catch (Exception e) {
						String lMsgErr = "";
						lMsgErr = "id = " + lEventoModel.getIdEvento() + " ("
								+ lEventoModel.getCodTipoEvento() + "-"
								+ lEventoModel.getCodTipoProvvedimento() + "-" + lEventoModel.getCodMotivo()
								+ "-" + lEventoModel.getCodEsito() + ")";
						siesLogger.error("Impossibile caricare l'evento : " + lMsgErr);
						rollback(lConn);
					}
				} // end FOR
			} // end if

			// Se sto aggiornando lo stato esecuzione da "presain carico/seguito atti" allora aggiorno
			// anche la DATA_PRESA_IN_CARICO sul TITOLO_CUMULATO
			lTitoloDao = new TitoloCumulatoDAO(lConn);
			if (aPresaInCarico) {
				lTitoloDao.setDataPresaInCarico(aDatiOper.getData());
				lTitoloDao.selCondizioneUpdate(aIdTitolo);
				lTitoloDao.update();
				lTitoloDao.stop();
			}

			// ===============================================
			// Elimino i record S.E.T.
			// ===============================================
			if (aListaEventiDaRimuovere != null) {
				for (int i = 0; i < aListaEventiDaRimuovere.size(); i++) {
					StatoEsecTitoloCumulatoModel lStatoEsecModel = null;
					try {
						lStatoEsecModel = aListaEventiDaRimuovere.elementAt(i);
						this.ExCancellaStatoEsecTitoloCumulatoById(
								lStatoEsecModel.getIdStatoEsecTitoloCumulato(), lConn);
						commit(lConn);
					} catch (Exception e) {
						String lMsgErr = "";
						lMsgErr = "id = " + lStatoEsecModel.getIdStatoEsecTitoloCumulato() + " ("
								+ lStatoEsecModel.getCodTipoEvento() + "-"
								+ lStatoEsecModel.getCodTipoProvvedimento() + "-"
								+ lStatoEsecModel.getCodMotivo() + "-" + lStatoEsecModel.getCodEsito() + ")";
						siesLogger.error("Impossibile cancellare  StatoEsec : " + lMsgErr);
						rollback(lConn);
					}
				}
			}

			// commit(lConn);
			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (Exception daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExAggiornaStatoEsecTitoloCumulatoByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lEventoDao);
			cleanup(lEventoSqlDao);
			cleanup(lMotivoEventoSqlDao);
			cleanup(lNotificaSqlDao);
			cleanup(lNotificaCumuloDao);
			cleanup(lVerbaleSqlDao);
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lTitoloDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	/**
	 * Produca la stampa dello stato di esecuzione di uno dei Fascicoli coinvolti in cumulo
	 *
	 */
	public ByteArrayOutputStream ExStampaStatoEsecTitolo(FascicoloSiepModel aFasc, String lIdTemplate,
			UtenteModel aUtente) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		try {

			IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
			TreeModel lTree = lCtrStam.prelevaDatiStampaFascicolo(aFasc, aUtente);

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);

			ReportGenerator lReport = new ReportGenerator();

			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);

			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExStampaStatoEsecTitolo: Non posso inserire: " + ex);
		}

		return lByteArrayOut;
	}

	public StatoEsecTitoloCumulatoModel ExInserisciStatoEsecComputoCumulo(
			StatoEsecTitoloCumulatoModel aStatoEsecuz, ComputiCumuloModel aComputoCumulo)
			throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Inserisco lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);

			lStatoEsecTitoloCumulatoDao.setDAOFromModel(aStatoEsecuz);

			BigDecimal lIdStato = lStatoEsecTitoloCumulatoDao.insert();
			aStatoEsecuz.setIdStatoEsecTitoloCumulato(lIdStato);

			lStatoEsecTitoloCumulatoDao.stop();

			// Inserisco e collego il computo
			// aComputoCumulo
			aComputoCumulo.setStatIdStatoEsecTitCum(lIdStato);
			lComputiDao = new ComputiCumuloDAO(lConn);
			lComputiDao.setDAOFromModel(aComputoCumulo);
			BigDecimal lIdComputo = lComputiDao.insert();
			aComputoCumulo.setIdComputiCumulo(lIdComputo);

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciStatoEsecComputoCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
		return aStatoEsecuz;
	}

	public StatoEsecTitoloCumulatoModel ExInserisciPagamentiPP(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<ComputiCumuloModel> aListaCumuli) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Inserisco lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);

			lStatoEsecTitoloCumulatoDao.setDAOFromModel(aStatoEsecuz);

			BigDecimal lIdStato = lStatoEsecTitoloCumulatoDao.insert();
			aStatoEsecuz.setIdStatoEsecTitoloCumulato(lIdStato);

			lStatoEsecTitoloCumulatoDao.stop();

			// Inserisco e collego i Computi Cumulo
			for (int i = 0; i < aListaCumuli.size(); i++) {
				ComputiCumuloModel aComputoCumulo = aListaCumuli.elementAt(i);
				aComputoCumulo.setStatIdStatoEsecTitCum(lIdStato);

				lComputiDao = new ComputiCumuloDAO(lConn);
				lComputiDao.setDAOFromModel(aComputoCumulo);
				BigDecimal lIdComputo = lComputiDao.insert();
				aComputoCumulo.setIdComputiCumulo(lIdComputo);

				lComputiDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciPagamentiPP: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
		return aStatoEsecuz;
	}

	public void ExModificaStatoEsecComputoCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Aggiorno lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModelForUpdate(aStatoEsecuz);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(aStatoEsecuz.getIdStatoEsecTitoloCumulato());
			lStatoEsecTitoloCumulatoDao.update();

			// Aggiorno il computo o aggiungo
			lComputiDao = new ComputiCumuloDAO(lConn);
			if (aComputoCumulo.getIdComputiCumulo() != null) {
				lComputiDao.setDAOFromModelForUpdate(aComputoCumulo);
				lComputiDao.selCondizioneUpdate(aComputoCumulo.getIdComputiCumulo());
				lComputiDao.update();
			} else {
				lComputiDao.setDAOFromModel(aComputoCumulo);
				lComputiDao.insert();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExModificaStatoEsecComputoCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
	}

	public void ExModificaStatoEsecComputiCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<ComputiCumuloModel> aListaCumuli) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Aggiorno lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModelForUpdate(aStatoEsecuz);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(aStatoEsecuz.getIdStatoEsecTitoloCumulato());
			lStatoEsecTitoloCumulatoDao.update();

			// Aggiorno i computi o aggiungo
			for (int i = 0; i < aListaCumuli.size(); i++) {
				ComputiCumuloModel aComputoCumulo = aListaCumuli.elementAt(i);

				if (aComputoCumulo.getIdComputiCumulo() != null) {
					// siesLogger.debug("upd i getIdComputiCumulo ="+i +
					// " "+aComputoCumulo.getIdComputiCumulo() );
					lComputiDao = new ComputiCumuloDAO(lConn);
					lComputiDao.setDAOFromModelForUpdate(aComputoCumulo);
					lComputiDao.selCondizioneUpdate(aComputoCumulo.getIdComputiCumulo());
					lComputiDao.update();
				} else {
					// siesLogger.debug("ins i getIdComputiCumulo ="+i +
					// " "+aComputoCumulo.getIdComputiCumulo() );
					aComputoCumulo.setStatIdStatoEsecTitCum(aStatoEsecuz.getIdStatoEsecTitoloCumulato());
					lComputiDao = new ComputiCumuloDAO(lConn);
					lComputiDao.setDAOFromModel(aComputoCumulo);
					BigDecimal lIdComputo = lComputiDao.insert();
					aComputoCumulo.setIdComputiCumulo(lIdComputo);
				}

				lComputiDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExModificaStatoEsecComputoCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
	}

	public void ExCancellaStatoEsecTitoloCumulatoById(BigDecimal aStatoEsecTitoloCumulato, Connection aConn)
			throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		NotificaCumuloDAO lNotificaDao = null;
		LibAnticipataCumuloDAO lLibAntDao = null;
		LibAnticipataCumuloSqlDAO lLibAntSqlDao = null;
		PeriodoLibAntCumuloDAO lPerLibDao = null;
		RichPMStatoEsecCumSqlDAO lRichPMStatEsecSqlDao = null;

		try {
			if (aConn != null)
				lConn = aConn;
			else
				lConn = getDBConnection();

			//
			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatoByKey(aStatoEsecTitoloCumulato);
			StatoEsecTitoloCumulatoModel lStatoEsecModel = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoloCumulatoSqlDao
					.getModelByKey();
			lStatoEsecTitoloCumulatoSqlDao.stop();

			// Verifico prima se esistono richieste associate al provvedimento
			lRichPMStatEsecSqlDao = new RichPMStatoEsecCumSqlDAO(lConn);
			lRichPMStatEsecSqlDao.ricercaRichPmStatoEsecCumByIdStatEsec(aStatoEsecTitoloCumulato);
			Vector<RichPMStatoEsecCumModel> lListaRichieste = new Vector<RichPMStatoEsecCumModel>(
					lRichPMStatEsecSqlDao.getModels());
			lRichPMStatEsecSqlDao.stop();
			if (lListaRichieste != null && lListaRichieste.size() > 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibile eliminate il provvedimento "
								+ lStatoEsecModel.getDescrTipoProvvedimento() + " del "
								+ DateUtils.getDateToString(lStatoEsecModel.getDataEmissione(), "dd-MM-yyyy")
								+ "-" + lStatoEsecModel.getDescrMotivo()
								+ " in quanto esiste una richiesta associata");
			}

			// Cancello i COMPUTI_CUMULO se presenti
			lComputiDao = new ComputiCumuloDAO(lConn);
			lComputiDao.selCondizioneUpdateByIdStatEsec(aStatoEsecTitoloCumulato);
			lComputiDao.delete();
			lComputiDao.stop();

			// Cancello le NOTIFICHE_CUMULO se presenti
			lNotificaDao = new NotificaCumuloDAO(lConn);
			lNotificaDao.selCondizioneUpdateByIdStatEsec(aStatoEsecTitoloCumulato);
			lNotificaDao.delete();
			lNotificaDao.stop();

			// Ricerco e Cancello eventuali LIBERAZIONI_ANTICIPATE ed, eventualmente, i relativi
			// PERIODI_LIBERAZIONI_ANTICIPATE
			lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lPerLibDao = new PeriodoLibAntCumuloDAO(lConn);

			lLibAntSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(aStatoEsecTitoloCumulato);
			Vector<LibAnticipataCumuloModel> lListaLiberazioni = new Vector<LibAnticipataCumuloModel>(
					lLibAntSqlDao.getModels());
			if (lListaLiberazioni != null && lListaLiberazioni.size() > 0) {
				for (int j = 0; j < lListaLiberazioni.size(); j++) {
					LibAnticipataCumuloModel lLibAntCumMod = lListaLiberazioni.elementAt(j);
					// Cancello PERIODI_LIBERAZIONI_ANTICIPATE
					lPerLibDao.selCondizioneLib_Id_LibAnt(lLibAntCumMod.getIdLibAnticipataCumulo());
					lPerLibDao.delete();
				}
			}

			lPerLibDao.stop();
			lLibAntSqlDao.stop();

			// Cancello LIBERAZIONI_ANTICIPATE
			lLibAntDao = new LibAnticipataCumuloDAO(lConn);
			lLibAntDao.selCondizioneUpdateByIdStatEsec(aStatoEsecTitoloCumulato);
			lLibAntDao.delete();
			lLibAntDao.stop();

			// Aggiungere cancellazione eventuali altre tabelle

			// Cancello infine lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(aStatoEsecTitoloCumulato);
			lStatoEsecTitoloCumulatoDao.delete();
			lStatoEsecTitoloCumulatoDao.stop();

			if (aConn == null)
				commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			siesLogger.error("DAOException.getErrorCode(): " + daoEx.getErrorCode());
			siesLogger.error("DAOException.getSQLState(): " + daoEx.getSQLState());
			// if (daoEx.getErrorCode() == 2292) {
			// restrizione di integrità violata
			// }
			if (aConn == null)
				rollback(lConn);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExCancellaStatoEsecTitoloCumulatoById: " + daoEx);
		} catch (F3BException f3bEx) {
			if (aConn == null)
				rollback(lConn);
			throw f3bEx;
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lComputiDao);
			cleanup(lNotificaDao);
			cleanup(lPerLibDao);
			cleanup(lLibAntDao);
			cleanup(lLibAntSqlDao);
			cleanup(lRichPMStatEsecSqlDao);

			if (aConn == null)
				cleanup(lConn);
		}
	}

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaPresoffertiByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;

		Vector<StatoEsecTitoloCumulatoModel> lListaPresofferti = new Vector<>();

		StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();

		lStatoRicerca.setCodTipoEvento("01");
		lStatoRicerca.setCodTipoProvvedimento("04");
		lStatoRicerca.setCodMotivo("0121");
		lStatoRicerca.setTitIdTitoloCumulato(aIdTitolo);

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulato(lStatoRicerca);

			lListaPresofferti = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());

			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			for (int i = 0; i < lListaPresofferti.size(); i++) {
				StatoEsecTitoloCumulatoModel lStatoModel = lListaPresofferti.elementAt(i);

				lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());

				Vector<ComputiCumuloModel> lListaPeriodi = new Vector(lComputiSqlDao.getModels());

				lStatoModel.setListaComputi(lListaPeriodi);
				lComputiSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaPresoffertiByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lComputiSqlDao);

			cleanup(lConn);
		}

		return lListaPresofferti;
	}

	/**
	 *
	 * @param aIdStatoEsecTitoloCumulato
	 * @return
	 * @throws F3BException
	 */
	public StatoEsecTitoloCumulatoModel ExRicercaStatoEsecTitoloCumulatoByIdFull(
			BigDecimal aIdStatoEsecTitoloCumulato) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;
		NotificaCumuloSqlDAO lNotificaSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoLibAntSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstitutoSqlDao = null;

		StatoEsecTitoloCumulatoModel lStatoEsecTitoloCumulatoMod = null;
		LibAnticipataCumuloModel lLibAntCumMod = null;

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatoByKey(aIdStatoEsecTitoloCumulato);
			lStatoEsecTitoloCumulatoMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoloCumulatoSqlDao
					.getModelByKey();

			// ===========================================
			// Provo a recuperare i computi se presenti
			// ===========================================
			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(aIdStatoEsecTitoloCumulato);
			Vector<ComputiCumuloModel> lListaPeriodi = new Vector(lComputiSqlDao.getModels());
			lStatoEsecTitoloCumulatoMod.setListaComputi(lListaPeriodi);

			lIstitutoSqlDao = new IstitutoDetenzioneSqlDAO(lConn);
			for (int i = 0; i < lListaPeriodi.size(); i++) {
				ComputiCumuloModel lComputo = lListaPeriodi.elementAt(i);
				siesLogger.debug("<<<<<<<<< lComputo = " + lComputo.toString());
				if (lComputo.getIstDetIdIstitutoDetenzione() != null) {
					lIstitutoSqlDao.ricercaIstitutoDetenzioneByKey(lComputo.getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstMod = (IstitutoDetenzioneModel) lIstitutoSqlDao
							.getModelByKey();
					lComputo.setIstitutoDetenzione(lIstMod);
				}
			}

			// ===========================================
			// Provo a recuperare eventuali Notifiche
			// ===========================================
			lNotificaSqlDao = new NotificaCumuloSqlDAO(lConn);
			lNotificaSqlDao.ricercaNotificheCumuloByIdStatoEsec(aIdStatoEsecTitoloCumulato);
			Vector<NotificaCumuloModel> lListaNotifiche = new Vector(lNotificaSqlDao.getModels());
			lStatoEsecTitoloCumulatoMod.setListaNotifiche(lListaNotifiche);

			// ==============================================================================
			// Provo a recuperare eventuali Liberazioni Anticipate e Relativi Periodi
			// ===========================================================================
			lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lPeriodoLibAntSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			lLibAntSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(aIdStatoEsecTitoloCumulato);
			Vector<LibAnticipataCumuloModel> lListaLiberazioni = new Vector(lLibAntSqlDao.getModels());

			if (lListaLiberazioni != null && lListaLiberazioni.size() > 0) {
				for (int j = 0; j < lListaLiberazioni.size(); j++) {
					lLibAntCumMod = lListaLiberazioni.elementAt(j);
					// Ricerca eventuali Periodi di Liberazione Anticipata
					lPeriodoLibAntSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
							lLibAntCumMod.getIdLibAnticipataCumulo());
					Vector<PeriodoLibAntCumuloModel> lListaPeriodiLibAnt = new Vector(
							lPeriodoLibAntSqlDao.getModels());
					if (lListaPeriodiLibAnt != null && lListaPeriodiLibAnt.size() > 0) {
						lLibAntCumMod.setListaPeriodiLibAnticipate(lListaPeriodiLibAnt);
					}
					lPeriodoLibAntSqlDao.stop();
				}
				lStatoEsecTitoloCumulatoMod.setListaLiberazioniAnticipate(lListaLiberazioni);
			}
			lLibAntSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaStatoEsecTitoloCumulatoByIdFull: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lComputiSqlDao);
			cleanup(lIstitutoSqlDao);
			cleanup(lNotificaSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lLibAntSqlDao);
			cleanup(lPeriodoLibAntSqlDao);

			cleanup(lConn);
		}

		return lStatoEsecTitoloCumulatoMod;
	}

	/**
	 *
	 * @param aEventoModel
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputi(BigDecimal aIdEvento, StatoEsecTitoloCumulatoModel aStatoEsecModel,
			Connection aConn) throws F3BException {

		AnnotazioneManualeSqlDAO lAnnotaSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			// Recupero le annotazioni manuali
			lAnnotaSqlDao = new AnnotazioneManualeSqlDAO(aConn);
			lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(aIdEvento);

			Vector<AnnotazioneManualeModel> lListaAnnotazioni = new Vector(lAnnotaSqlDao.getModels());

			lComputiDao = new ComputiCumuloDAO(aConn);

			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnota = lListaAnnotazioni.elementAt(i);

				ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

				// lComputiModel.setCodTipoAnnotazione ("005" ); //005-Pena Espiata per lo Stesso Titolo
				lComputiModel.setCodTipoAnnotazione(lAnnota.getCodTipoAnnotazione());
				lComputiModel.setCodCausaleComputo("-"); // Fisso a trattino (res migrava 08)

				lComputiModel.setFlagPiuMeno(lAnnota.getFlagPiuMeno());

				lComputiModel.setDataReclusioneDa(lAnnota.getDataReclusioneDa());
				lComputiModel.setDataReclusioneA(lAnnota.getDataReclusioneA());

				lComputiModel.setNumAnniReclusione(lAnnota.getNumAnniReclusione());
				lComputiModel.setNumMesiReclusione(lAnnota.getNumMesiReclusione());
				lComputiModel.setNumGiorniReclusione(lAnnota.getNumGiorniReclusione());

				lComputiModel.setCodTipoMisura("XX");// Non presente su ANN MAN

				// Per Ora non gestiti in SIEP
				// lComputiModel.setNumGiorniMap ( );
				// lComputiModel.setIstDetIdIstitutoDetenzione ( );
				// lComputiModel.setAltroLuogoDetenzione ( );

				if ("0212".equals(aStatoEsecModel.getCodMotivo())) {
					// Computo Custodia Cautelare Altro reato
					lComputiModel.setCodCausaleComputo(lAnnota.getCodCausaleComputo());
					lComputiModel.setAnnoRegePM(lAnnota.getAnnoRege()); // R.G.P.M
					lComputiModel.setNumeroRegePM(lAnnota.getNumeroRege()); // R.G.P.M
					lComputiModel.setAnnoBDMC(lAnnota.getAnnoMc()); // B.D.M.C.
					lComputiModel.setNumeroBDMC(lAnnota.getNumeroMc()); // B.D.M.C.
					// lAnnota.getDataRichiesta(); // Data Istanza

					lComputiModel.setCodTipoMisura(null);

					lComputiModel.setNote(lAnnota.getNoteReclusione());
				} else if ("0213".equals(aStatoEsecModel.getCodMotivo())) {
					// Computo Pena Detentiva Altro reato
					lComputiModel.setCodCausaleComputo(lAnnota.getCodCausaleComputo());

					lComputiModel.setAnnoSentenza(lAnnota.getAnnoSentenzaSiap());
					lComputiModel.setNumeroSentenza(lAnnota.getNumeroSentenzaSiap());
					lComputiModel.setDataSentenza(lAnnota.getDataSentenzaSiap());

					lComputiModel.setCodTipoMisura(null);

					lComputiModel.setNote(lAnnota.getNoteReclusione());
				}

				lComputiModel.setFlagStato("E");
				lComputiModel.setMotivoModifica(null);

				lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
				lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
				lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

				lComputiDao.setDAOFromModel(lComputiModel);
				lComputiDao.insert();

				lComputiDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputi: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAnnotaSqlDao);
			cleanup(lComputiDao);
		}
	}

	/**
	 *
	 * @param aIdEvento
	 * @param aStatoEsecModel
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiEspiatoPM(BigDecimal aKeyFascicolo, BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		PenaResiduaSqlDAO lPenaResSqlDao = null;
		SospensioneSqlDAO lSospSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		PenaPrecedenteSqlDAO lPenaPrecedenteSqlDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecOrdSqlDao = null;

		PenaResiduaModel lPenResMod = null;
		SospensioneModel lSospModel = null;
		ComputiCumuloModel lComputiModel = null;

		try {
			lPenaResSqlDao = new PenaResiduaSqlDAO(aConn);

			siesLogger.debug("Recupero la PR");
			lPenaResSqlDao.ricercaPenaResiduaByKeyEvento(aIdEvento);
			lPenResMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();

			if (lPenResMod != null) {
				siesLogger.debug("PR presente, recupero la SOSPENSIONE");

				lSospSqlDao = new SospensioneSqlDAO(aConn);
				lSospSqlDao.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
				lSospModel = (SospensioneModel) lSospSqlDao.getModelByKey();

				if (lSospModel != null) {
					siesLogger.debug("Trovata sospensione id " + lSospModel.getIdSospensione());
				} else {
					siesLogger.warn("Sospensione NON trovata per PR con id " + lPenResMod.getIdPenaResidua()
							+ " su evento id " + aIdEvento);
				}
			} else
				siesLogger.warn("PR assente per evento interruttivo con id = " + aIdEvento);

			if (lSospModel != null) {
				lComputiModel = new ComputiCumuloModel();

				lComputiModel.setCodTipoAnnotazione("019"); // Espiazione Pregressa
				lComputiModel.setCodCausaleComputo("-");

				lComputiModel.setFlagPiuMeno("-"); // ?????????

				// Provo a recuperare la data inizio periodi di espiazione dalla pena
				// residua precedente a quelle di interruzione
				//
				siesLogger.debug("Provo a recuperare l'ultima pena in esecuzione...");
				lPenaPrecedenteSqlDao = new PenaPrecedenteSqlDAO(aConn);
				lPenaPrecedenteSqlDao.ricercaPenaPrecedenteByFascicoloDataIserimento(aKeyFascicolo,
						lPenResMod.getIdPenaResidua());
				PenaPrecedenteModel lPenaPrecedente = (PenaPrecedenteModel) lPenaPrecedenteSqlDao
						.getModelByKey();
				if (lPenaPrecedente != null) {
					siesLogger.debug("Pena trovata " + lPenaPrecedente.getIdPenaResidua() + ""
							+ lPenaPrecedente.getDataInizio());
					lComputiModel.setDataReclusioneDa(lPenaPrecedente.getDataInizio());
				}

				// La data fine espiazione coincide con la data Inizio Sospensione
				lComputiModel.setDataReclusioneA(lSospModel.getDataInizio());

				lComputiModel.setNumAnniReclusione(lSospModel.getNumAnniPenaEspiata());
				lComputiModel.setNumMesiReclusione(lSospModel.getNumMesiPenaEspiata());
				lComputiModel.setNumGiorniReclusione(lSospModel.getNumGiorniPenaEspiata());

				// ======================================================================
				// Lettura del DecretoOrdinanzaSIEP.
				siesLogger.debug("Cerco Decreto Ordinanza Collegato...");
				lDecOrdSqlDao = new DecretoOrdinanzaSiepSqlDAO(aConn);

				lDecOrdSqlDao.ricercaDecretoOrdinanzaSiepByIdEvento(aIdEvento);
				DecretoOrdinanzaSiepModel lDecOrdModel = (DecretoOrdinanzaSiepModel) lDecOrdSqlDao
						.getModelByKey();
				lDecOrdSqlDao.stop();

				siesLogger.debug("lDecOrdModel = " + lDecOrdModel);

				if (lDecOrdModel != null) {
					lComputiModel.setDataRicezioneProvv(lDecOrdModel.getDataRicezioneProvvedimento());
					lComputiModel.setDataEmissioneProvv(lDecOrdModel.getDataEmissioneProvvedimento());
					lComputiModel.setDataSospensioneInterruzione(lDecOrdModel.getDataInterruzionePena());

					lComputiModel.setProtocollo(lDecOrdModel.getProtocollo());
					lComputiModel.setAltraAutorita(lDecOrdModel.getAltraAutorita());
					lComputiModel.setAltroLuogo(lDecOrdModel.getAltroLuogo());

					lComputiModel.setNote(lDecOrdModel.getMotivazioni());
				}
				// ======================================================================

				lComputiModel.setCodTipoMisura(null);// Non presente

				lComputiModel.setFlagStato("E"); // ESTRATTO
				lComputiModel.setMotivoModifica(null);

				lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
				lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
				lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

				lComputiDao = new ComputiCumuloDAO(aConn);
				lComputiDao.setDAOFromModel(lComputiModel);
				lComputiDao.insert();

				lComputiDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiEspiatoPM: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lPenaResSqlDao);
			cleanup(lSospSqlDao);
			cleanup(lComputiDao);
			cleanup(lPenaPrecedenteSqlDao);
			cleanup(lDecOrdSqlDao);
		}
	}

	/**
	 * Estrae i dati dei decreti/ordinanze
	 *
	 * @param aIdEvento
	 * @param aStatoEsecModel
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaDatiSorv(BigDecimal aIdEvento, StatoEsecTitoloCumulatoModel aStatoEsecModel,
			Connection aConn) throws F3BException {

		// I dati da recuperare sono iscritti su:
		// MISURA_ALTERNATIVA
		// DEPOSITO_DECRETO
		// DEPOSITO_ORDINANZA
		// ESITO_TENORE
		MisuraAlternativaDAO lMisuraAltDao = null;
		DepositoOrdinanzaPcSqlDAO lDepositoOrdinanzaSqlDao = null;
		DepositoDecretoSqlDAO lDepositoDecretoSqlDao = null;
		TenoreSqlDAO lTenoreSqlDal = null;
		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;

		try {
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(aConn);

			lMisuraAltDao = new MisuraAlternativaDAO(aConn);

			lMisuraAltDao.setCondizioneByIdEvento(aIdEvento);
			MisuraAlternativaModel lMisuraModel = null;
			lMisuraModel = (MisuraAlternativaModel) lMisuraAltDao.getModelByKey();
			// siesLogger.debug("lMisuraModel = "+lMisuraModel);

			// Recupero anno e numero Fascicolo SIUS dalla MA
			lStatoEsecTitoloCumulatoDao.setAnnoProcedimento(lMisuraModel.getChiaveAnnoFascicoloSius());
			lStatoEsecTitoloCumulatoDao.setProgrProcedimento(lMisuraModel.getChiaveProgrFascicoloSius());

			lTenoreSqlDal = new TenoreSqlDAO(aConn);

			// Deposito decreto: recupero anno e numero Ordinanza ANNO_S72,NUM_S72
			if (aStatoEsecModel.getCodTipoProvvedimento().equals("02")) {
				// Recupero: anno e numero decreto ANNO_S72,NUM_S72
				lDepositoDecretoSqlDao = new DepositoDecretoSqlDAO(aConn);
				lDepositoDecretoSqlDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
				DepositoDecretoModel lDopDecModel = (DepositoDecretoModel) lDepositoDecretoSqlDao
						.getModelByKey();

				// Recupero anno e numero Decreto
				lStatoEsecTitoloCumulatoDao.setAnnoProvvedimento(lDopDecModel.getAnnoS72());
				lStatoEsecTitoloCumulatoDao.setProgrProvvedimento(lDopDecModel.getNumS72());

				// Recupero il Tenore
				// lTenoreSqlDal.ricercaTenoreByDecreto (lDopDecModel.getIdDepositoDecreto());
				lTenoreSqlDal.ricercaTenoriByDecretoOrderByPesoNoGenProc(lDopDecModel.getIdDepositoDecreto());

				TenoreModel lTenore = (TenoreModel) lTenoreSqlDal.getModelByKey();
				lStatoEsecTitoloCumulatoDao.setCodEsitoTenore(lTenore.getCodEsitoTenore());
				lTenoreSqlDal.stop();
			}
			// Deposito Ordinanza: recupero anno e numero Ordinanza ANNO_S3,NUM_S3
			else if (aStatoEsecModel.getCodTipoProvvedimento().equals("03")) {
				lDepositoOrdinanzaSqlDao = new DepositoOrdinanzaPcSqlDAO(aConn);
				lDepositoOrdinanzaSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aIdEvento);
				DepositoOrdinanzaPcModel lDepOrdModel = (DepositoOrdinanzaPcModel) lDepositoOrdinanzaSqlDao
						.getModelByKey();
				lDepositoOrdinanzaSqlDao.stop();

				siesLogger.debug("lDepOrdModel = " + lDepOrdModel);

				// Recupero anno e numero Ordinanza
				lStatoEsecTitoloCumulatoDao.setAnnoProvvedimento(lDepOrdModel.getAnnoS3());
				lStatoEsecTitoloCumulatoDao.setProgrProvvedimento(lDepOrdModel.getNumS3());

				// Recupero il Tenore
				// lTenoreSqlDal.ricercaTenoreByOrdinanza (lDepOrdModel.getIdDepositoOrdinanzaPc());
				lTenoreSqlDal.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(
						lDepOrdModel.getIdDepositoOrdinanzaPc());

				TenoreModel lTenore = (TenoreModel) lTenoreSqlDal.getModelByKey();
				lStatoEsecTitoloCumulatoDao.setCodEsitoTenore(lTenore.getCodEsitoTenore());
				lTenoreSqlDal.stop();
			}

			// Aggiorno lo stato esecuzione
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(aStatoEsecModel.getIdStatoEsecTitoloCumulato());
			lStatoEsecTitoloCumulatoDao.update();
			lStatoEsecTitoloCumulatoDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaDatiSorv: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMisuraAltDao);
			cleanup(lDepositoDecretoSqlDao);
			cleanup(lDepositoOrdinanzaSqlDao);
			cleanup(lTenoreSqlDal);
			cleanup(lStatoEsecTitoloCumulatoDao);
		}
	}

	/**
	 *
	 * @param aEventoModel
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiPagamentoPP(BigDecimal aIdEvento, StatoEsecTitoloCumulatoModel aStatoEsecModel,
			Connection aConn) throws F3BException {

		AnnotazioneManualeSqlDAO lAnnotaSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		CampoNotaSqlDAO lCaNoSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Lettura dell'Evento e recupero dell'Evento collegato
			EventoModel lEveMod, lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			// Eventuale comunicazione (12). Presente solo se altro ufficio e con
			// FLAG_VIDEO = N. E' un evento nascosto
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(lEvento.getEveIdEvento());
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			// Recupero delle occorrenze di CAMPO_NOTA per individuare i CAMPO_NOTA di interesse
			lCaNoSqlDao = new CampoNotaSqlDAO(aConn);
			lCaNoSqlDao.ricercaCampoNotaByKeyEvento(aIdEvento);

			Vector<CampoNotaModel> lListaCampoNota = new Vector(lCaNoSqlDao.getModels());

			String lDescCampoNotaSezione = "", lDescCampoNotaExCampione = "";

			for (int ii = 0; ii < lListaCampoNota.size(); ii++) {
				CampoNotaModel lCaNo = lListaCampoNota.elementAt(ii);
				if (lCaNo.getOggettoNotaRes().toUpperCase().compareTo("SEZIONE") == 0)
					lDescCampoNotaSezione = lCaNo.getDescr();
				if (lCaNo.getOggettoNotaRes().toUpperCase().compareTo("EXCAMPIONE") == 0)
					lDescCampoNotaExCampione = lCaNo.getDescr();
			}

			// Recupero le annotazioni manuali
			lAnnotaSqlDao = new AnnotazioneManualeSqlDAO(aConn);
			lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(aIdEvento);

			Vector<AnnotazioneManualeModel> lListaAnnotazioni = new Vector(lAnnotaSqlDao.getModels());
			lComputiDao = new ComputiCumuloDAO(aConn);

			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnota = lListaAnnotazioni.elementAt(i);

				ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

				lComputiModel.setCodTipoAnnotazione(lAnnota.getCodTipoAnnotazione());
				lComputiModel.setCodCausaleComputo("-"); // Fisso a trattino (res migrava 08)
				lComputiModel.setFlagPiuMeno(lAnnota.getFlagPiuMeno());

				lComputiModel.setImportoMulta(lAnnota.getImportoMulta());
				lComputiModel.setImportoAmmenda(lAnnota.getImportoAmmenda());

				if (lEveMod != null) {
					// Recupero i dati altro ufficio dalla comunicazione collegata
					lComputiModel.setCodUfficioEmittenteProvv(lEveMod.getCodUfficioEmittente());
					lComputiModel.setCodLuogoUfficioProvv(lEveMod.getCodLuogoEmittente());
					lComputiModel.setDataEmissioneProvv(lEveMod.getDataEmissione());
					lComputiModel.setDataRicezioneProvv(lEveMod.getDataRicezioneAtti());
					lComputiModel.setAnnoProvv(lEveMod.getAnnoProtocollo());
					lComputiModel.setProgrProvv(lEveMod.getProgrProtocollo());
				}

				lComputiModel.setSezioneProvv(lDescCampoNotaSezione);
				lComputiModel.setNote(lDescCampoNotaExCampione);

				lComputiModel.setCodTipoMisura("XX");// Non presente su ANN MAN

				// Per Ora non gestiti in SIEP
				// lComputiModel.setNumGiorniMap ( );
				// lComputiModel.setIstDetIdIstitutoDetenzione ( );
				// lComputiModel.setAltroLuogoDetenzione ( );

				lComputiModel.setFlagStato("E");
				lComputiModel.setMotivoModifica(null);

				lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
				lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
				lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

				lComputiDao.setDAOFromModel(lComputiModel);
				lComputiDao.insert();

				lComputiDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiPagamentoPP: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotaSqlDao);
			cleanup(lComputiDao);
			cleanup(lCaNoSqlDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 *
	 * @param aEventoModel
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiAmnistiaIndulto(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		AnnotazioneManualeSqlDAO lAnnotaSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ReatoCumuloSqlDAO lReatoCumuloSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura delle Annotazioni manuali
			lAnnotaSqlDao = new AnnotazioneManualeSqlDAO(aConn);

			// N.B. Per le ordinanze inserite direttamente da SIGE bisogna recuperare l'annotazione manuale
			// afferente all'evento.
			// Per le ordinanze del GE iscritte da SIEP (CodOperatoreInserimento inizia con A, o B, o C )
			// l'annotazione va individuata tramite EveIdEvento.
			String[] a = { "A", "B", "C" };
			if (Arrays.asList(a).contains(lEvento.getCodOperatoreInserimento().substring(0, 1)))
				lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(lEvento.getEveIdEvento());
			else
				lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(lEvento.getIdEvento());

			Vector<AnnotazioneManualeModel> lListaAnnotazioni = new Vector(lAnnotaSqlDao.getModels());
			lComputiDao = new ComputiCumuloDAO(aConn);

			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnota = lListaAnnotazioni.elementAt(i);

				// siesLogger.debug("IdAnnotazione = "+lAnnota.getIdAnnotazioneManuale());

				ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

				lComputiModel.setCodTipoAnnotazione(lAnnota.getCodTipoAnnotazione());
				lComputiModel.setCodCausaleComputo("-");
				lComputiModel.setFlagPiuMeno(lAnnota.getFlagPiuMeno());

				lComputiModel.setNumAnniReclusione(lAnnota.getNumAnniReclusione());
				lComputiModel.setNumMesiReclusione(lAnnota.getNumMesiReclusione());
				lComputiModel.setNumGiorniReclusione(lAnnota.getNumGiorniReclusione());

				lComputiModel.setNumAnniArresto(lAnnota.getNumAnniArresto());
				lComputiModel.setNumMesiArresto(lAnnota.getNumMesiArresto());
				lComputiModel.setNumGiorniArresto(lAnnota.getNumGiorniArresto());

				lComputiModel.setCodTipoMisura(null);

				lComputiModel.setNote(lAnnota.getMotivazioni());

				lComputiModel.setImportoMulta(lAnnota.getImportoMulta());
				lComputiModel.setImportoAmmenda(lAnnota.getImportoAmmenda());
				lComputiModel.setCodDpr(lAnnota.getCodDpr());

				lComputiModel.setCodUfficioEmittenteProvv(lEvento.getCodUfficioEmittente());
				lComputiModel.setCodLuogoUfficioProvv(lEvento.getCodLuogoEmittente());
				lComputiModel.setDataEmissioneProvv(lAnnota.getDataGE());
				// lComputiModel.setDataRicezioneProvv (lEvento.getDataRicezioneAtti() );
				lComputiModel.setAnnoProvv(lAnnota.getChiaveAnnoSige());
				lComputiModel.setProgrProvv(lAnnota.getChiaveNumeroSige());

				// siesLogger.debug("lAnnota.getReaIdReato() = "+lAnnota.getReaIdReato());
				if (lAnnota.getReaIdReato() != null) {
					// siesLogger.debug("Individuazione del ReatoCumulo: idReato = "+lAnnota.getReaIdReato());

					lReatoCumuloSqlDao = new ReatoCumuloSqlDAO(aConn);
					lReatoCumuloSqlDao.ricercaReatiCumuloByIdTitolo(aStatoEsecModel.getTitIdTitoloCumulato());

					Vector<ReatoCumuloModel> lReaVect = new Vector(lReatoCumuloSqlDao.getModels());
					lReatoCumuloSqlDao.stop();

					for (int ii = 0; ii < lReaVect.size(); ii++) {
						ReatoCumuloModel lReaCum = lReaVect.get(ii);
						if (lReaCum.getIdReatoOrigine().compareTo(lAnnota.getReaIdReato()) == 0
								&& lReaCum.getProgrCircostanza().toString().equals("1")) {
							lComputiModel.setReaIdReatoCum(lReaCum.getIdReatoCum());
							break;
						}
					}
				}

				/*
				 * // Individuazione del ReatoCumulo. ReatoCumuloModel lReaCumMod = new ReatoCumuloModel();
				 * lReaCumMod.setIdReatoOrigine(lAnnota.getReaIdReato()); IReatoCumulo lCtrlR =
				 * SIEPLookupRemote.getReatoCumuloRemote(); Vector<ReatoCumuloModel> lReaVect =
				 * lCtrlR.ExRicercaReatoCumulo(lReaCumMod);
				 *
				 * ss if (lReaVect.size() > 0) for (int ii=0; i<lReaVect.size();ii++) { ReatoCumuloModel
				 * lReaCum = lReaVect.get(ii); if (lReaCum.getProgrCircostanza().toString().equals("1"))
				 * lComputiModel.setReaIdReatoCum (lReaCum.getIdReatoCum() ); break; }
				 */

				lComputiModel.setFlagStato("E");
				lComputiModel.setMotivoModifica(null);

				lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
				lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
				lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

				lComputiDao.setDAOFromModel(lComputiModel);
				lComputiDao.insert();

				lComputiDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiAmnistiaIndulto: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotaSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
			cleanup(lReatoCumuloSqlDao);
		}
	}

	/**
	 *
	 * @param aEventoModel
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiDepenIncost(BigDecimal aIdEvento, StatoEsecTitoloCumulatoModel aStatoEsecModel,
			Connection aConn) throws F3BException {

		AnnotazioneManualeSqlDAO lAnnotaSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ReatoCumuloSqlDAO lReatoCumuloSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura delle Annotazioni manuali
			lAnnotaSqlDao = new AnnotazioneManualeSqlDAO(aConn);

			// Annotazione con i quantum presente sul provvedimento puntato dall'ordinanza
			lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(lEvento.getEveIdEvento());
			AnnotazioneManualeModel lAnnotazione1 = (AnnotazioneManualeModel) lAnnotaSqlDao.getModelByKey();
			lAnnotaSqlDao.stop();

			// siesLogger.debug("lAnnotazione1 = "+lAnnotazione1);

			// Annotazione legata all'ordinanza con i dati del GE
			lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(lEvento.getIdEvento());
			AnnotazioneManualeModel lAnnotazione2 = (AnnotazioneManualeModel) lAnnotaSqlDao.getModelByKey();
			// siesLogger.debug("lAnnotazione2 = "+lAnnotazione2);

			lComputiDao = new ComputiCumuloDAO(aConn);

			ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

			lComputiModel.setCodTipoAnnotazione(lAnnotazione1.getCodTipoAnnotazione());
			lComputiModel.setCodCausaleComputo("-"); // Fisso a trattino (res migrava 08)
			lComputiModel.setFlagPiuMeno(lAnnotazione1.getFlagPiuMeno());

			lComputiModel.setNumAnniReclusione(lAnnotazione1.getNumAnniReclusione());
			lComputiModel.setNumMesiReclusione(lAnnotazione1.getNumMesiReclusione());
			lComputiModel.setNumGiorniReclusione(lAnnotazione1.getNumGiorniReclusione());

			lComputiModel.setNumAnniArresto(lAnnotazione1.getNumAnniArresto());
			lComputiModel.setNumMesiArresto(lAnnotazione1.getNumMesiArresto());
			lComputiModel.setNumGiorniArresto(lAnnotazione1.getNumGiorniArresto());

			lComputiModel.setCodTipoMisura(null);

			lComputiModel.setNote(lAnnotazione2.getMotivazioni());

			lComputiModel.setImportoMulta(lAnnotazione1.getImportoMulta());
			lComputiModel.setImportoAmmenda(lAnnotazione1.getImportoAmmenda());
			lComputiModel.setCodDpr(lAnnotazione1.getCodDpr());

			lComputiModel.setCodUfficioEmittenteProvv(lEvento.getCodUfficioEmittente());
			lComputiModel.setCodLuogoUfficioProvv(lEvento.getCodLuogoEmittente());
			lComputiModel.setDataEmissioneProvv(lAnnotazione2.getDataGE());
			// lComputiModel.setDataRicezioneProvv (lEvento.getDataRicezioneAtti() );
			lComputiModel.setAnnoProvv(lAnnotazione2.getAnnoGe());
			lComputiModel.setProgrProvv(new BigDecimal(lAnnotazione2.getNumeroGe()));

			// Incostituzionalità: dati della sentenza di incostituzionelità
			lComputiModel.setAnnoSentenza(lAnnotazione1.getAnnoCc());
			lComputiModel.setNumeroSentenza(lAnnotazione1.getNumeroCc());
			lComputiModel.setDataSentenza(lAnnotazione1.getDataCC());

			lComputiModel.setCodFonte(lAnnotazione1.getCodFonte());
			lComputiModel.setAnnoFonte(lAnnotazione1.getAnnoFonte());
			lComputiModel.setNumeroFonte(lAnnotazione1.getNumeroFonte());
			lComputiModel.setCodSottonumerazione(lAnnotazione1.getCodSottonumerazione());
			lComputiModel.setComma(lAnnotazione1.getComma());
			lComputiModel.setLettera(lAnnotazione1.getLettera());
			lComputiModel.setNumero(lAnnotazione1.getNumero());
			lComputiModel.setArticolo(lAnnotazione1.getArticolo());

			// Individuazione del ReatoCumulo.

			if (lAnnotazione1.getReaIdReato() != null) {
				// siesLogger.debug("Individuazione del ReatoCumulo: idReato =
				// "+lAnnotazione1.getReaIdReato());

				lReatoCumuloSqlDao = new ReatoCumuloSqlDAO(aConn);
				lReatoCumuloSqlDao.ricercaReatiCumuloByIdTitolo(aStatoEsecModel.getTitIdTitoloCumulato());

				Vector<ReatoCumuloModel> lReaVect = new Vector(lReatoCumuloSqlDao.getModels());
				lReatoCumuloSqlDao.stop();

				// ReatoCumuloModel lReaCumMod = new ReatoCumuloModel();
				// lReaCumMod.setIdReatoOrigine(lAnnotazione1.getReaIdReato());
				// IReatoCumulo lCtrlR = SIEPLookupRemote.getReatoCumuloRemote();
				// Vector<ReatoCumuloModel> lReaVect = lCtrlR.ExRicercaReatoCumulo(lReaCumMod);

				for (int ii = 0; ii < lReaVect.size(); ii++) {
					ReatoCumuloModel lReaCum = lReaVect.get(ii);
					if (lReaCum.getIdReatoOrigine().compareTo(lAnnotazione1.getReaIdReato()) == 0
							&& lReaCum.getProgrCircostanza().toString().equals("1")) {
						lComputiModel.setReaIdReatoCum(lReaCum.getIdReatoCum());
						break;
					}
				}
			}

			lComputiModel.setFlagStato("E");
			lComputiModel.setMotivoModifica(null);

			lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
			lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
			lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

			lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
			lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
			lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

			lComputiDao.setDAOFromModel(lComputiModel);
			lComputiDao.insert();

			lComputiDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiDepenIncost: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotaSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
			cleanup(lReatoCumuloSqlDao);
		}
	}

	/**
	 *
	 * @param aIdEvento
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiSospensioneGE(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		DecretoOrdinanzaSiepSqlDAO lDecOrdSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura del DecretoOrdinanzaSIEP.
			lDecOrdSqlDao = new DecretoOrdinanzaSiepSqlDAO(aConn);

			lDecOrdSqlDao.ricercaDecretoOrdinanzaSiepByIdEvento(lEvento.getIdEvento());
			DecretoOrdinanzaSiepModel lDecOrd = (DecretoOrdinanzaSiepModel) lDecOrdSqlDao.getModelByKey();
			lDecOrdSqlDao.stop();

			lComputiDao = new ComputiCumuloDAO(aConn);

			ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

			lComputiModel.setCodTipoAnnotazione("-");
			lComputiModel.setCodCausaleComputo("-");
			lComputiModel.setCodTipoMisura(null);

			lComputiModel.setDataRicezioneProvv(lDecOrd.getDataRicezioneProvvedimento());
			lComputiModel.setCodTipoRegistroOrdinanza(lDecOrd.getCodTipoRegistroOrdinanza());
			lComputiModel.setAnnoProc(lDecOrd.getAnnoRegistro());
			lComputiModel.setProgrProc(lDecOrd.getNumRegistro());
			lComputiModel.setDataEmissioneProvv(lDecOrd.getDataEmissioneProvvedimento());
			lComputiModel.setAnnoProvv(lDecOrd.getAnnoProvvedimento());
			lComputiModel.setProgrProvv(lDecOrd.getNumProvvedimento());

			// Attenzione: autorità emittenete e ufficio emittente Coincidono
			lComputiModel.setCodTipoAutoritaEmittente(lDecOrd.getCodTipoAutoritaEmittente());
			lComputiModel.setCodLuogoEmittente(lDecOrd.getCodLuogoEmittente());

			lComputiModel.setCodUfficioEmittenteProvv(lEvento.getCodUfficioEmittente());
			lComputiModel.setCodLuogoUfficioProvv(lEvento.getCodLuogoEmittente());
			// =====

			lComputiModel.setDataSospensioneInterruzione(lDecOrd.getDataSospensioneEsecuzione());

			lComputiModel.setCodOggettoDecisione(lDecOrd.getCodOggettoDecisione());
			lComputiModel.setNote(lDecOrd.getMotivazioni());

			lComputiModel.setFlagStato("E");
			lComputiModel.setMotivoModifica(null);

			lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
			lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
			lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

			lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
			lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
			lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

			lComputiDao.setDAOFromModel(lComputiModel);
			lComputiDao.insert();

			lComputiDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiSospensioneGE: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDecOrdSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 *
	 * @param aIdEvento
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiInterruzioneGE(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		DecretoOrdinanzaSiepSqlDAO lDecOrdSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura del DecretoOrdinanzaSIEP.
			lDecOrdSqlDao = new DecretoOrdinanzaSiepSqlDAO(aConn);

			lDecOrdSqlDao.ricercaDecretoOrdinanzaSiepByIdEvento(lEvento.getIdEvento());
			DecretoOrdinanzaSiepModel lDecOrd = (DecretoOrdinanzaSiepModel) lDecOrdSqlDao.getModelByKey();
			lDecOrdSqlDao.stop();

			lComputiDao = new ComputiCumuloDAO(aConn);

			ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

			lComputiModel.setCodTipoAnnotazione("-");
			lComputiModel.setCodCausaleComputo("-");
			lComputiModel.setCodTipoMisura(null);

			lComputiModel.setDataRicezioneProvv(lDecOrd.getDataRicezioneProvvedimento());
			lComputiModel.setCodTipoRegistroOrdinanza(null);
			// lComputiModel.setAnnoProc (lDecOrd.getAnnoRegistro());
			// lComputiModel.setProgrProc (lDecOrd.getNumRegistro());
			lComputiModel.setDataEmissioneProvv(lDecOrd.getDataEmissioneProvvedimento());
			// lComputiModel.setAnnoProvv (lDecOrd.getAnnoProvvedimento());
			// lComputiModel.setProgrProvv (lDecOrd.getNumProvvedimento());

			lComputiModel.setCodTipoAutoritaEmittente(lEvento.getCodTipoUfficioEmittente());
			lComputiModel.setCodUfficioEmittenteProvv(lEvento.getCodUfficioEmittente());
			lComputiModel.setCodLuogoUfficioProvv(lEvento.getCodLuogoEmittente());

			lComputiModel.setProtocollo(lDecOrd.getProtocollo());
			lComputiModel.setAltraAutorita(lDecOrd.getAltraAutorita());
			lComputiModel.setAltroLuogo(lDecOrd.getAltroLuogo());

			lComputiModel.setDataSospensioneInterruzione(lDecOrd.getDataInterruzionePena());

			// Sulla tabella STATO_ESECUZIONE.COD_MOTIVO
			// lComputiModel.setCodOggettoDecisione (lDecOrd.getCodOggettoDecisione() );
			lComputiModel.setNote(lDecOrd.getMotivazioni());

			lComputiModel.setFlagStato("E");
			lComputiModel.setMotivoModifica(null);

			lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
			lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
			lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

			lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
			lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
			lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

			lComputiDao.setDAOFromModel(lComputiModel);
			lComputiDao.insert();

			lComputiDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiInterruzioneGE: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDecOrdSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 *
	 * @param aIdEvento
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiMisuraAlternativaSORV(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		MisuraAlternativaSqlDAO lMisAltSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaSqlDAO lPenaResSqlDao = null;
		SospensioneSqlDAO lSospSqlDao = null;
		PenaPrecedenteSqlDAO lPenaPrecedenteSqlDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecOrdSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura dell'Evento di Revoca
			EventoModel lEveRevoca = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByEveIdEvento(aIdEvento);
			lEveRevoca = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura della Misura Alternativa.
			lMisAltSqlDao = new MisuraAlternativaSqlDAO(aConn);

			lMisAltSqlDao.ricercaMisuraAlternativaByIdEvento(aIdEvento);
			MisuraAlternativaModel lMisAlt = (MisuraAlternativaModel) lMisAltSqlDao.getModelByKey();
			lMisAltSqlDao.stop();

			lComputiDao = new ComputiCumuloDAO(aConn);

			ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

			lComputiModel.setCodTipoAnnotazione("-");
			lComputiModel.setCodCausaleComputo("-");
			lComputiModel.setCodTipoMisura(lMisAlt.getCodNaturaDecisione());

			lComputiModel.setDataEmissioneProvv(lMisAlt.getDataDecisione());
			lComputiModel.setCodUfficioEmittenteProvv(lMisAlt.getChiaveUfficioFascicoloSius());
			lComputiModel.setAnnoProc(lMisAlt.getChiaveAnnoFascicoloSius());
			lComputiModel.setProgrProc(lMisAlt.getChiaveProgrFascicoloSius());
			lComputiModel.setAnnoProvv(lMisAlt.getAnnoRegistro());
			lComputiModel.setProgrProvv(lMisAlt.getNumeroRegistro());
			lComputiModel.setLuogoEsecMisura(lMisAlt.getDescrLuogoProva());
			lComputiModel.setNumAnniMisura(lMisAlt.getNumAnniMisura());
			lComputiModel.setNumMesiMisura(lMisAlt.getNumMesiMisura());
			lComputiModel.setNumGiorniMisura(lMisAlt.getNumGiorniMisura());

			lComputiModel.setDataInizioMisura(lMisAlt.getDataInizioMisura());
			lComputiModel.setDataFineMisura(lMisAlt.getDataFineMisura());
			lComputiModel.setNote(lMisAlt.getNote());
			lComputiModel.setCodOggettoDecisione(lMisAlt.getCodTipoMisura());

			lComputiModel.setDataInizioRevoca(lMisAlt.getDataInizioRevoca());
			lComputiModel.setNumAnniRevocaReclusione(lMisAlt.getNumAnniRevocaReclusione());
			lComputiModel.setNumMesiRevocaReclusione(lMisAlt.getNumMesiRevocaReclusione());
			lComputiModel.setNumGiorniRevocaReclusione(lMisAlt.getNumGiorniRevocaReclusione());
			lComputiModel.setNumAnniRevocaArresto(lMisAlt.getNumAnniRevocaArresto());
			lComputiModel.setNumMesiRevocaArresto(lMisAlt.getNumMesiRevocaArresto());
			lComputiModel.setNumGiorniRevocaArresto(lMisAlt.getNumGiorniRevocaArresto());
			lComputiModel.setDataIngressoIstituto(lMisAlt.getDataIngressoIstituto());

			lComputiModel.setDataScarcerazione(lMisAlt.getDataScarcerazione());

			lComputiModel.setCodTDSCompetente(lMisAlt.getCodTdsCompetente());
			lComputiModel.setFlagDecisioneTribunale(lMisAlt.getFlagDecisioneTribunale());

			// 02/04/2019 MEV70 - Recupero informazioni di quantum da SOSPENSIONE
			PenaResiduaModel lPenResMod = null;
			SospensioneModel lSospModel = null;

			lPenaResSqlDao = new PenaResiduaSqlDAO(aConn);

			siesLogger.debug("Recupero la PR");
			lPenaResSqlDao.ricercaPenaResiduaByKeyEvento(lEveRevoca.getIdEvento());
			lPenResMod = (PenaResiduaModel) lPenaResSqlDao.getModelByKey();

			if (lPenResMod != null) {
				siesLogger.debug("PR presente, recupero la SOSPENSIONE");

				lSospSqlDao = new SospensioneSqlDAO(aConn);
				lSospSqlDao.ricercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
				lSospModel = (SospensioneModel) lSospSqlDao.getModelByKey();

				if (lSospModel != null) {
					siesLogger.debug("Trovata sospensione id " + lSospModel.getIdSospensione());
				} else {
					siesLogger.warn("Sospensione NON trovata per PR con id " + lPenResMod.getIdPenaResidua()
							+ " su evento id " + aIdEvento);
				}
			} else {
				siesLogger.warn("PR assente per evento interruttivo con id = " + aIdEvento);
			}

			if (lSospModel != null) {

				lComputiModel.setCodTipoAnnotazione("019"); // Espiazione Pregressa
				lComputiModel.setFlagPiuMeno("-");

				// Provo a recuperare la data inizio periodi di espiazione dalla pena residua precedente a
				// quelle di interruzione
				siesLogger.debug("Provo a recuperare l'ultima pena in esecuzione...");
				lPenaPrecedenteSqlDao = new PenaPrecedenteSqlDAO(aConn);
				lPenaPrecedenteSqlDao.ricercaPenaPrecedenteByFascicoloDataIserimento(
						lEvento.getFasSieIdFascicoloSiep(), lPenResMod.getIdPenaResidua());
				PenaPrecedenteModel lPenaPrecedente = (PenaPrecedenteModel) lPenaPrecedenteSqlDao
						.getModelByKey();
				if (lPenaPrecedente != null) {
					siesLogger.debug("Pena trovata id=" + lPenaPrecedente.getIdPenaResidua() + ", dataInizio"
							+ lPenaPrecedente.getDataInizio() + ", dataFine" + lPenaPrecedente.getDataFine());

					lComputiModel.setDataReclusioneDa(lPenaPrecedente.getDataInizio());

					// D.F. 07/05/2019 Il COMPUTI_CUMULO.DataReclusioneA dovrebbe essere il fine pena
					// previsto prima dell'interruzione i base al quale viene calcolata
					// la pena residua in form cge tra le altre cose non serve a null
					if (lSospModel.getNumGiorniLibanticipata() != null
							&& lPenaPrecedente.getDataFine() != null) {
						siesLogger.debug("Ho PR.DATA_FINE e GG di LA, ricalcolo la data fine");
						Date lDatFine = DateUtils.moveDateTo(lPenaPrecedente.getDataFine(),
								Calendar.DAY_OF_MONTH, lSospModel.getNumGiorniLibanticipata().intValue());
						lComputiModel.setDataReclusioneA(lDatFine);
					} else {
						lComputiModel.setDataReclusioneA(lPenaPrecedente.getDataFine());
					}
				} else {
					// La data fine espiazione coincide con la data Inizio Sospensione
					lComputiModel.setDataReclusioneA(lSospModel.getDataInizio());
				}

				lComputiModel.setNumAnniReclusione(lSospModel.getNumAnniPenaEspiata());
				lComputiModel.setNumMesiReclusione(lSospModel.getNumMesiPenaEspiata());
				lComputiModel.setNumGiorniReclusione(lSospModel.getNumGiorniPenaEspiata());

				// D.F. 06/05/2019
				if (lSospModel.getNumGiorniLibanticipata() != null
						&& lSospModel.getNumGiorniLibanticipata().intValue() > 0) {
					siesLogger.debug("Trovate LA sulla sospensione. Ricalcolo l'espiato");
					if (lPenaPrecedente != null && lPenaPrecedente.getDataInizio() != null
							&& lSospModel.getDataInizio() != null) {
						siesLogger.debug("dataInizioPena Ultima PR = " + lPenaPrecedente.getDataInizio());
						siesLogger.debug("dataInterruzione = " + lSospModel.getDataInizio());
						CalendarModel lCalPenaEspiata = new CalendarModel();
						lCalPenaEspiata.setDataInizio(lPenaPrecedente.getDataInizio());
						lCalPenaEspiata.setDataFine(lSospModel.getDataInizio());

						CalendarUtil lCalUtil = new CalendarUtil();
						lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, false);

						// Normalizzo i quantum
						lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);

						siesLogger.debug("Quantum = " + lCalPenaEspiata);

						//
						siesLogger.debug("Sovrascrivo l'espiato con i dati ricalcolati");
						lComputiModel.setNumAnniReclusione(new BigDecimal(lCalPenaEspiata.getNumAnni()));
						lComputiModel.setNumMesiReclusione(new BigDecimal(lCalPenaEspiata.getNumMesi()));
						lComputiModel.setNumGiorniReclusione(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
					}
				}

				// 16/04/2019 MEV70 Recupero della Pena Residua dalla Sospensione.
				lComputiModel.setNumGiorniRevocaReclusione(lSospModel.getNumGiorniPenaResiduaReclus());
				lComputiModel.setNumMesiRevocaReclusione(lSospModel.getNumMesiPenaResiduaReclus());
				lComputiModel.setNumAnniRevocaReclusione(lSospModel.getNumAnniPenaResiduaReclus());

				// Lettura del DecretoOrdinanzaSIEP.
				siesLogger.debug("Cerco Decreto Ordinanza Collegato...");
				lDecOrdSqlDao = new DecretoOrdinanzaSiepSqlDAO(aConn);

				lDecOrdSqlDao.ricercaDecretoOrdinanzaSiepByIdEvento(aIdEvento);
				DecretoOrdinanzaSiepModel lDecOrdModel = (DecretoOrdinanzaSiepModel) lDecOrdSqlDao
						.getModelByKey();
				lDecOrdSqlDao.stop();

				siesLogger.debug("lDecOrdModel = " + lDecOrdModel);

				if (lDecOrdModel != null) {
					lComputiModel.setDataRicezioneProvv(lDecOrdModel.getDataRicezioneProvvedimento());
					lComputiModel.setDataEmissioneProvv(lDecOrdModel.getDataEmissioneProvvedimento());
					lComputiModel.setDataSospensioneInterruzione(lDecOrdModel.getDataInterruzionePena());

					lComputiModel.setProtocollo(lDecOrdModel.getProtocollo());
					lComputiModel.setAltraAutorita(lDecOrdModel.getAltraAutorita());
					lComputiModel.setAltroLuogo(lDecOrdModel.getAltroLuogo());

					lComputiModel.setNote(lDecOrdModel.getMotivazioni());
				}
			}

			lComputiModel.setFlagStato("E");
			lComputiModel.setMotivoModifica(null);

			lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
			lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
			lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

			lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
			lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
			lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

			lComputiDao.setDAOFromModel(lComputiModel);
			lComputiDao.insert();

			lComputiDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiMisuraAlternativaSORV: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisAltSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
			cleanup(lPenaResSqlDao);
			cleanup(lSospSqlDao);
			cleanup(lPenaPrecedenteSqlDao);
			cleanup(lDecOrdSqlDao);
		}
	}

	private void caricaComputiOrdRevocaSospCondPenaGE(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdPCSqlDao = null;
		TenoreSqlDAO lTenoreSqlDao = null;
		CampoNotaSqlDAO lCampoNotaSqlDao = null;
		StatoEsecTitoloCumulatoDAO lStatoEsecTitCumDAO = null;

		String codEsito = "";

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();

			// Lettura dell'Ordinanza ( in DepositoOrdinanzaPC).
			lDepOrdPCSqlDao = new DepositoOrdinanzaPcSqlDAO(aConn);
			lDepOrdPCSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aIdEvento);
			DepositoOrdinanzaPcModel lOrdinanza = (DepositoOrdinanzaPcModel) lDepOrdPCSqlDao.getModelByKey();
			lDepOrdPCSqlDao.stop();

			// Lettura del Tenore.
			lTenoreSqlDao = new TenoreSqlDAO(aConn);
			lTenoreSqlDao.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(lOrdinanza.getIdDepositoOrdinanzaPc());
			TenoreModel lTenore = (TenoreModel) lTenoreSqlDao.getModelByKey();
			codEsito = lTenore.getCodEsitoTenore();
			lTenoreSqlDao.stop();

			// Lettura del Campo Nota.
			lCampoNotaSqlDao = new CampoNotaSqlDAO(aConn);
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aIdEvento);
			CampoNotaModel lCampoNota = (CampoNotaModel) lCampoNotaSqlDao.getModelByKey();
			lCampoNotaSqlDao.stop();

			// Valorizzazione Esito Tenore e aggiornamento dello stato esecuzione.
			lStatoEsecTitCumDAO = new StatoEsecTitoloCumulatoDAO(aConn);
			lStatoEsecTitCumDAO.setCodEsitoTenore(codEsito);
			lStatoEsecTitCumDAO.selCondizioneUpdate(aStatoEsecModel.getIdStatoEsecTitoloCumulato());
			lStatoEsecTitCumDAO.update();
			lStatoEsecTitCumDAO.stop();

			// Valorizzazione Computi Cumulo
			lComputiDao = new ComputiCumuloDAO(aConn);

			ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

			lComputiModel.setCodTipoAnnotazione("021");
			lComputiModel.setCodCausaleComputo("-");
			lComputiModel.setCodTipoMisura("-");

			lComputiModel.setDataEmissioneProvv(lEvento.getDataEmissione());
			lComputiModel.setCodUfficioEmittenteProvv(lEvento.getCodUfficioEmittente());
			lComputiModel.setCodLuogoUfficioProvv(lEvento.getCodLuogoEmittente());
			lComputiModel.setAnnoProvv(lOrdinanza.getAnnoS3());
			lComputiModel.setProgrProvv(lOrdinanza.getNumS3());

			lComputiModel.setNote(lCampoNota.getDescr());

			lComputiModel.setFlagStato("E");
			lComputiModel.setMotivoModifica(null);

			lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
			lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
			lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

			lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
			lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
			lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

			lComputiDao.setDAOFromModel(lComputiModel);
			lComputiDao.insert();

			lComputiDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiOrdRevocaSospCondPenaGE: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
			cleanup(lDepOrdPCSqlDao);
			cleanup(lTenoreSqlDao);
			cleanup(lCampoNotaSqlDao);
			cleanup(lStatoEsecTitCumDAO);
		}
	}

	// Oltre ai provvedimenti di Inserimento e Revoca Liberazione Anticipata,
	// carica anche i provvedimenti per i Rimedi Risarcitori (Cod.Motivo = 2790) e Reclamo Rimedi Risarcitori
	// (Cod.Motivo = 9027)
	private void caricaLiberazioniAnticipate(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		siesLogger.debug("--XX-- Start caricaLiberazioniAnticipate / Rimedi Risarcitori... >>> ");

		StatoEsecTitoloCumulatoDAO lStatDao = null;
		LibAnticipataCumuloDAO lLibAntDao = null;
		PeriodoLibAntCumuloDAO lPerLibDao = null;
		EventoSqlDAO lEveSqlDao = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		PeriodoLibanticipataSqlDAO lPerSqlDao = null;

		ArrayList lPeriodi = null;

		try {
			// Lettura dell'Evento
			// EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			/* lEvento = (EventoModel) */lEveSqlDao.getModelByKey();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(aConn);

			lLicSqlDao.ricercaLicenzaLibanticipataByEve(aIdEvento);
			Vector<LicenzaLibAnticipataModel> lLicenze = new Vector(lLicSqlDao.getModels());
			if (lLicenze != null && lLicenze.size() > 0) {
				int conta = 0;
				Iterator lItx = lLicenze.iterator();
				while (lItx.hasNext()) {
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					aModel.setLicenza((LicenzaLibAnticipataModel) lItx.next());

					// ------------------------------------------------------------------------------------------------
					// Altri campi da Mettere in StatoEsecTitoloCumulato
					if (conta == 0) {
						if (aModel.getLicenza().getAnnoSius() != null)
							aStatoEsecModel.setAnnoProcedimento(aModel.getLicenza().getAnnoSius());
						if (aModel.getLicenza().getNumeroSius() != null)
							aStatoEsecModel.setProgrProcedimento(
									new BigDecimal(aModel.getLicenza().getNumeroSius()));

						if (aModel.getLicenza().getAnnoOrdinanza() != null)
							aStatoEsecModel.setAnnoProvvedimento(aModel.getLicenza().getAnnoOrdinanza());
						if (aModel.getLicenza().getNumeroOrdinanza() != null)
							aStatoEsecModel.setProgrProvvedimento(aModel.getLicenza().getNumeroOrdinanza());
					}
					conta++;
					// --------------------------------------------------------------------------------------------
					// Copia di L.A. su L.A. CUMULO
					// ---------------------------------------------------------------------------------------------

					LibAnticipataCumuloModel lLibAntCumMod = new LibAnticipataCumuloModel(
							aModel.getLicenza()); // aModel.getLicenza()
													// =
													// LICENZALIBANTICIPATAMODEL

					lLibAntCumMod.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());
					lLibAntCumMod.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
					lLibAntCumMod.setDataInserimento(aStatoEsecModel.getDataInserimento());

					lLibAntCumMod.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
					lLibAntCumMod.setFlagStato(aStatoEsecModel.getFlagStato());
					lLibAntCumMod.setStatIdStatoEsecTitoloCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

					lLibAntDao = new LibAnticipataCumuloDAO(aConn);

					lLibAntDao.setDAOFromModel(lLibAntCumMod);
					BigDecimal lKeyLA = lLibAntDao.insert();
					lLibAntCumMod.setIdLibAnticipataCumulo(lKeyLA);
					lLibAntDao.stop();
					// ----------------------------------------------------------------------------------------------

					if (("LAU").equals(aModel.getLicenza().getDescrStatoPermesso())
							|| ("LSU").equals(aModel.getLicenza().getDescrStatoPermesso())
							|| ("LIU").equals(aModel.getLicenza().getDescrStatoPermesso())) {
						// Non sono presenti PERIODI
					} else {
						// Ricerca dei Periodi relativi alla licenza
						lPerSqlDao = new PeriodoLibanticipataSqlDAO(aConn);

						lPerSqlDao.ricercaPeriodoLibanticipataByLic(
								aModel.getLicenza().getIdLicenzaLibanticipata());
						lPeriodi = new ArrayList(lPerSqlDao.getModels());

						if (lPeriodi != null && lPeriodi.size() > 0) {
							aModel.setPeriodi((PeriodoLibAnticipataModel[]) lPeriodi
									.toArray(new PeriodoLibAnticipataModel[1]));
							cleanup(lPerSqlDao);

							// --------------------------------------------------------------------------------------------
							// Copia dei Periodi L.A. su PERIODI L.A. CUMULO
							// ---------------------------------------------------------------------------------------------
							lPerLibDao = new PeriodoLibAntCumuloDAO(aConn);

							for (int k = 0; k < lPeriodi.size(); k++) {
								PeriodoLibAnticipataModel lPerMod = (PeriodoLibAnticipataModel) lPeriodi
										.get(k);

								PeriodoLibAntCumuloModel lPerCum = new PeriodoLibAntCumuloModel();
								lPerCum.setDataInizio(lPerMod.getDataInizio());
								lPerCum.setDataFine(lPerMod.getDataFine());
								lPerCum.setIdPeriodoLibantOrigine(lPerMod.getIdPeriodoLibanticipata());
								lPerCum.setLibIdLibAnticipataCumulo(lKeyLA);

								lPerCum.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());
								lPerCum.setCodOperatoreInserimento(
										aStatoEsecModel.getCodOperatoreInserimento());
								lPerCum.setDataInserimento(aStatoEsecModel.getDataInserimento());
								lPerCum.setFlagStato(aStatoEsecModel.getFlagStato());

								lPerLibDao.setDAOFromModel(lPerCum);
								lPerLibDao.insert();
							}

							lPerLibDao.stop();

						} // Chiude if(lPeriodi!=null && lPeriodi.size() > 0 )

					} // Chiude Else

				} // Chiude ciclo while()

			} // Chiude if(lLicenze !=null && lLicenze.size() > 0)

			// Update di StatoEsecTitoloCumulato con aggiunta di altri campi
			lStatDao = new StatoEsecTitoloCumulatoDAO(aConn);
			lStatDao.setDAOFromModelForUpdate(aStatoEsecModel);
			lStatDao.selCondizioneUpdate(aStatoEsecModel.getIdStatoEsecTitoloCumulato());
			lStatDao.update();
			lStatDao.stop();
		} catch (Exception daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaLiberazioniAnticipate: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lLibAntDao);
			cleanup(lPerLibDao);
			cleanup(lLicSqlDao);
			cleanup(lPerSqlDao);
			cleanup(lStatDao);
		}
	} // Chiude caricaLiberazioniAnticipate()

	/**
	 * Carica i provvedimenti di Scomputo Permessi (Cod.Motivo = 2250) e Reclamo Scomputo Permessi (Cod.Motivo
	 * = 0039)
	 *
	 * Attenzione!! Gli scomputi e i Reclami vengono iscritti da SIEP e da SIUS in modo differente: SIEP
	 * inserisce il decreto SIUS e un provvedimento di annotazione (25) che lo punta. Le LA vengono legate
	 * all'annotazione e non al Decreto/ordinanza.
	 *
	 * SIUS invece scrive il decreto/ordinanza e ovviamente vi collega le LA.
	 *
	 * Per cui le LA con i dati dei quantum scomputati/ricomputati sono legati a eventi differenti.
	 *
	 * Anche le LA vengono scritte in modo differente: SIEP : 2250 - LA.COD_TIPO_LICENZA = PP e
	 * LA.FLAG_CONCESSO = C , LA.FLAG_SCOMPUTO = S SIUS (UDS): 2250 - LA.COD_TIPO_LICENZA = EP e
	 * LA.FLAG_CONCESSO = null, LA.FLAG_SCOMPUTO = S/N
	 *
	 * SIEP : 0039 - LA.COD_TIPO_LICENZA = EP e LA.FLAG_CONCESSO = C , LA.FLAG_SCOMPUTO = S SIUS (TDS): 0039 -
	 * LA.COD_TIPO_LICENZA = EP e LA.FLAG_CONCESSO = null, LA.FLAG_SCOMPUTO = sembra null
	 *
	 * SIEP scrive COD_TIPO_LICENZA in modo inverso rispetto a SIUS
	 *
	 * @param aIdEvento
	 * @param aStatoEsecModel
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaScomputiPermessi(BigDecimal aIdEvento, StatoEsecTitoloCumulatoModel aStatoEsecModel,
			Connection aConn) throws F3BException {

		siesLogger.debug("--XX-- Start carica ScomputiPermessi... >>> ");

		StatoEsecTitoloCumulatoDAO lStatDao = null;
		LibAnticipataCumuloDAO lLibAntDao = null;
		EventoSqlDAO lEveSqlDao = null;
		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		FascicoloSiusSqlDAO lFascSiusSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdPCSqlDao = null;
		TenoreSqlDAO lTenoreSqlDao = null;

		// ============================================================================================================
		// Lettura dell'Evento : L'Evento legato alla licenza Scomputo Permesso (dove ci sono i giorni da
		// scomputare),
		// NON è quello corrente, ma quello ad esso collegato tramite EVE_ID_EVENTO;
		// ============================================================================================================
		boolean isSius = false;

		try {
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			EventoModel lEventoDecOrd = (EventoModel) lEveSqlDao.getModelByKey();

			if (lEventoDecOrd.getFasSiuIdFascicoloSius() != null) {
				isSius = true;
				siesLogger.debug(
						"Evento SIUS recupero i dati del fascicolo sius e anno num provv e data emissione");

				lFascSiusSqlDao = new FascicoloSiusSqlDAO(aConn);
				lFascSiusSqlDao.ricercaFascicoloByKey(lEventoDecOrd.getFasSiuIdFascicoloSius());
				FascicoloSiusModel lFasSiusModel = (FascicoloSiusModel) lFascSiusSqlDao.getModelByKey();

				if (lFasSiusModel != null) {
					aStatoEsecModel.setAnnoProcedimento(lFasSiusModel.getChiaveAnno());
					aStatoEsecModel.setProgrProcedimento(lFasSiusModel.getChiaveProgr());
				}
				siesLogger.debug(
						"aStatoEsecModel.getAnnoProcedimento() = " + aStatoEsecModel.getAnnoProcedimento());
				siesLogger.debug(
						"aStatoEsecModel.getProgrProcedimento() = " + aStatoEsecModel.getProgrProcedimento());

				if ("02".equals(lEventoDecOrd.getCodTipoProvvedimento())) {
					// Recupere Deposito Decreto
					siesLogger.debug("Recupere Deposito Decreto");
					lDepDecSqlDao = new DepositoDecretoSqlDAO(aConn);
					lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
					DepositoDecretoModel lDopDecModel = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();
					if (lDopDecModel != null) {
						aStatoEsecModel.setAnnoProvvedimento(lDopDecModel.getAnnoS72());
						aStatoEsecModel.setProgrProvvedimento(lDopDecModel.getNumS72());
						aStatoEsecModel.setDataEmissione(lDopDecModel.getDataEmissione());

						lTenoreSqlDao = new TenoreSqlDAO(aConn);
						lTenoreSqlDao.ricercaTenoreByDecreto(lDopDecModel.getIdDepositoDecreto());
						TenoreModel lTenoreModel = (TenoreModel) lTenoreSqlDao.getModelByKey();
						if (lTenoreModel != null)
							aStatoEsecModel.setCodEsitoTenore(lTenoreModel.getCodEsitoTenore());
					}
				} else if ("03".equals(lEventoDecOrd.getCodTipoProvvedimento())) {
					// Recupere Deposito Ordinanza PC
					siesLogger.debug("Recupere Deposito Ordinanza PC");

					lDepOrdPCSqlDao = new DepositoOrdinanzaPcSqlDAO(aConn);
					lDepOrdPCSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aIdEvento);
					DepositoOrdinanzaPcModel lDepOrdModel = (DepositoOrdinanzaPcModel) lDepOrdPCSqlDao
							.getModelByKey();
					if (lDepOrdModel != null) {
						aStatoEsecModel.setAnnoProvvedimento(lDepOrdModel.getAnnoS3());
						aStatoEsecModel.setProgrProvvedimento(lDepOrdModel.getNumS3());
						// TODO: verificare
						aStatoEsecModel.setDataEmissione(lDepOrdModel.getDataCameraConsiglio());
						lTenoreSqlDao = new TenoreSqlDAO(aConn);
						lTenoreSqlDao.ricercaTenoreByOrdinanza(lDepOrdModel.getIdDepositoOrdinanzaPc());
						TenoreModel lTenoreModel = (TenoreModel) lTenoreSqlDao.getModelByKey();
						if (lTenoreModel != null)
							aStatoEsecModel.setCodEsitoTenore(lTenoreModel.getCodEsitoTenore());
					}
				}
			} else {
				siesLogger.debug("Evento SIEP");
			}

			// ========================================================================
			// Provo a recuperare le LA collegate all'evento in input (decreto/ordinanza)
			// Se iscritto SIUS le LA sono colllegate a tale evento
			// ========================================================================
			lLicSqlDao = new LicenzaLibanticipataSqlDAO(aConn);
			lLicSqlDao.ricercaLicenzaLibanticipataByEve(aIdEvento);
			LicenzaLibAnticipataModel lLicenza = (LicenzaLibAnticipataModel) lLicSqlDao.getModelByKey();
			lLicSqlDao.stop();

			if (lLicenza == null) {
				siesLogger.debug("LA non presenti su ordinanza. Ricerco Annotazione SIEP");
				// Le LA non sono legate all'evento di sorveglianza. Probabilmente evento
				// iscritto SIEP che lega le LA ad evento di annotazione (01-25) che punta
				// l'evento di sorveglianza
				lEveSqlDao = new EventoSqlDAO(aConn);

				// Provo a recuperare l'annotazione
				lEveSqlDao.ricercaEventoByEveIdEvento(aIdEvento);
				EventoModel lEventoAnn = (EventoModel) lEveSqlDao.getModelByKey();

				if (lEventoAnn != null) {
					lLicSqlDao.ricercaLicenzaLibanticipataByEve(lEventoAnn.getIdEvento());
					lLicenza = (LicenzaLibAnticipataModel) lLicSqlDao.getModelByKey();
					lLicSqlDao.stop();
				}
			}

			//
			if (lLicenza != null && lLicenza.getIdLicenzaLibanticipata() != null) {
				// Altri campi da Mettere in StatoEsecTitoloCumulato: riferimenti
				// al procedimento SIUS (anno/numero) e dati Ordinanza/Decreto

				// SIEP scrive i riferimenti al procedimento SIUS sui campi
				// ANNO_SIUS, NUMERO_SIUS
				// ANNO_ORDINANZA, NUMERO_ORDINANZA
				if (!isSius) {
					siesLogger.debug("Recupero i dati dell'provvedimento da Licenza");
					aStatoEsecModel.setAnnoProcedimento(lLicenza.getAnnoSius());
					if (lLicenza.getNumeroSius() != null)
						aStatoEsecModel.setProgrProcedimento(new BigDecimal(lLicenza.getNumeroSius()));

					aStatoEsecModel.setAnnoProvvedimento(lLicenza.getAnnoOrdinanza());
					aStatoEsecModel.setProgrProvvedimento(lLicenza.getNumeroOrdinanza());
				}
				// UDS valorizza solo ANNO_ORDINANZA, NUMERO_ORDINANZA
				// TDS non valorizza alcun dato della tabella LICENZA_LIBANTICIPATA
				// --------------------------------------------------------------------------------------------
				// Copia di L.A. su L.A. CUMULO
				// ---------------------------------------------------------------------------------------------

				LibAnticipataCumuloModel lLibAntCumMod = new LibAnticipataCumuloModel(lLicenza);

				// lLibAntCumMod.setCodTipoLicenza(aValore) EP, PP

				lLibAntCumMod.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());
				lLibAntCumMod.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lLibAntCumMod.setDataInserimento(aStatoEsecModel.getDataInserimento());

				lLibAntCumMod.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lLibAntCumMod.setFlagStato(aStatoEsecModel.getFlagStato());
				lLibAntCumMod.setStatIdStatoEsecTitoloCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lLibAntDao = new LibAnticipataCumuloDAO(aConn);

				lLibAntDao.setDAOFromModel(lLibAntCumMod);
				BigDecimal lKeyLA = lLibAntDao.insert();
				lLibAntCumMod.setIdLibAnticipataCumulo(lKeyLA);
				lLibAntDao.stop();
			} else {
				// ???
				siesLogger.warn("Non è stato possibile recuperare le LA degli Scomputi");
			}

			// Update di StatoEsecTitoloCumulato con aggiunta di altri campi
			lStatDao = new StatoEsecTitoloCumulatoDAO(aConn);
			lStatDao.setDAOFromModelForUpdate(aStatoEsecModel);
			lStatDao.selCondizioneUpdate(aStatoEsecModel.getIdStatoEsecTitoloCumulato());
			lStatDao.update();
			lStatDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaScomputiPermessi: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lLibAntDao);
			cleanup(lLicSqlDao);
			cleanup(lStatDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFascSiusSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lDepOrdPCSqlDao);
			cleanup(lTenoreSqlDao);
		}
	} // Chiude caricaScomputiPermessi()

	/**
	 *
	 * @param aListaProvvedimenti
	 *            elenco dei provvedimenti da inserire
	 */
	public void ExInserisciSospensioniDelPM(Vector<StatoEsecTitoloCumulatoModel> aListaProvvedimenti)
			throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		NotificaCumuloDAO lNotificaDao = null;

		try {
			lConn = getDBConnection();
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);

			// ====================================
			// Inserisco i Provvedimenti
			// ====================================
			for (int i = 0; i < aListaProvvedimenti.size(); i++) {
				StatoEsecTitoloCumulatoModel lStatoModel = aListaProvvedimenti.elementAt(i);

				lStatoEsecTitoloCumulatoDao.setDAOFromModel(lStatoModel);
				BigDecimal idStat = lStatoEsecTitoloCumulatoDao.insert();

				lStatoEsecTitoloCumulatoDao.stop();

				// ====================================
				// Inserisco le notifiche se presenti
				// ====================================
				Vector<NotificaCumuloModel> lListaNotifiche = lStatoModel.getListaNotifiche();
				if (lListaNotifiche != null) {
					Iterator<NotificaCumuloModel> lNotIter = lListaNotifiche.iterator();
					lNotificaDao = new NotificaCumuloDAO(lConn);
					while (lNotIter.hasNext()) {
						NotificaCumuloModel lNotifica = lNotIter.next();
						lNotifica.setStatIdStatoEsecTitCum(idStat);
						lNotificaDao.setDAOFromModel(lNotifica);
						lNotificaDao.insert();
						lNotificaDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciSospensioniDelPM: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lNotificaDao);

			cleanup(lConn);
		}
	}

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaProvvedimentiCumuloByIdTitoloTipoProvv(
			BigDecimal aIdTitolo, String aCodTipoEvento, String aCodTipoProvvedimento, String aCodMotivo)
			throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;

		Vector<StatoEsecTitoloCumulatoModel> lListaProvvedimentiCumulo = new Vector<>();

		StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();

		lStatoRicerca.setCodTipoEvento(aCodTipoEvento);
		lStatoRicerca.setCodTipoProvvedimento(aCodTipoProvvedimento);
		lStatoRicerca.setCodMotivo(aCodMotivo);
		lStatoRicerca.setTitIdTitoloCumulato(aIdTitolo);

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulato(lStatoRicerca);

			lListaProvvedimentiCumulo = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());

			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			for (int i = 0; i < lListaProvvedimentiCumulo.size(); i++) {
				StatoEsecTitoloCumulatoModel lStatoModel = lListaProvvedimentiCumulo.elementAt(i);

				lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());

				Vector<ComputiCumuloModel> lListaPeriodi = new Vector(lComputiSqlDao.getModels());

				lStatoModel.setListaComputi(lListaPeriodi);
				lComputiSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaProvvedimentiCumuloByIdTitoloTipoProvv: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lComputiSqlDao);

			cleanup(lConn);
		}

		return lListaProvvedimentiCumulo;
	}

	/**
	 * Effettua l'aggiornamento dello stato esecuzione. Non solo della tabella principale
	 * STATO_ESEC_TITOLO_CUMULATO ma anche delle tabelle collegate. Il model in input aStatoEsecTitoloCumulato
	 * deve essere completo delle dipendenze.
	 *
	 * Il metodo aggiorna STATO_ESEC_TITOLO_CUMULATO, quindi recupera le dipendenze a sistema e verifica se
	 * sono state cancellate, confrontandole con quelle presenti nel model. Quindi scorre le dipendenze del
	 * model e inserisce le nuove o aggiorna quelle che hanno id valorizzato.
	 *
	 * @param aStatoEsecTitoloCumulato
	 *            - Completo ovvero con tutte le dipendenze
	 */
	public void ExModificaStatoEsecTitoloCumulatoFull(StatoEsecTitoloCumulatoModel aStatoEsecTitoloCumulato)
			throws F3BException {

		Connection lConn = null;
		StatoEsecTitoloCumulatoDAO lStaDao = null;
		NotificaCumuloSqlDAO lNotificaSqlDao = null;
		NotificaCumuloDAO lNotificaDao = null;

		try {
			lConn = getDBConnection();

			lStaDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStaDao.setDAOFromModelForUpdate(aStatoEsecTitoloCumulato);
			lStaDao.selCondizioneUpdate(aStatoEsecTitoloCumulato.getIdStatoEsecTitoloCumulato());
			lStaDao.update();

			// ===============
			// Notifiche
			// ===============
			lNotificaSqlDao = new NotificaCumuloSqlDAO(lConn);
			lNotificaSqlDao.ricercaNotificheCumuloByIdStatoEsec(
					aStatoEsecTitoloCumulato.getIdStatoEsecTitoloCumulato());
			Vector<NotificaCumuloModel> lListaNotificheASistema = new Vector<NotificaCumuloModel>(
					lNotificaSqlDao.getModels());

			Vector<NotificaCumuloModel> lListaNotificheInInput = aStatoEsecTitoloCumulato.getListaNotifiche();
			lNotificaDao = new NotificaCumuloDAO(lConn);

			// Scorro le Notifiche a sistema: se non presenti tra quelle in input le cancello se presenti le
			// aggiorno
			if (lListaNotificheASistema != null && lListaNotificheASistema.size() > 0) {
				Iterator<NotificaCumuloModel> lIterListaNotificheASistema = lListaNotificheASistema
						.iterator();
				while (lIterListaNotificheASistema.hasNext()) {
					NotificaCumuloModel lNotSistema = lIterListaNotificheASistema.next();
					boolean cancella = true;

					if (lListaNotificheInInput == null || lListaNotificheInInput.size() == 0) {
						cancella = true;
					} else {
						Iterator<NotificaCumuloModel> lListaNotificheInputIter = lListaNotificheInInput
								.iterator();
						while (lListaNotificheInputIter.hasNext()) {
							NotificaCumuloModel lNotInput = lListaNotificheInputIter.next();
							if (lNotInput.getIdNotificaCumulo() != null && lNotInput.getIdNotificaCumulo()
									.compareTo(lNotSistema.getIdNotificaCumulo()) == 0) {
								cancella = false;
								break;
							}
						}
					}

					if (cancella) {
						siesLogger.debug("Cancello notifica con ID = " + lNotSistema.getIdNotificaCumulo());
						lNotificaDao.selCondizioneUpdate(lNotSistema.getIdNotificaCumulo());
						lNotificaDao.delete();
						lNotificaDao.stop();
					}
				}
			}

			// Scorro le Notifiche in Input: quelle con id le aggiorno, quelle senza le inserisco
			if (lListaNotificheInInput != null) {
				Iterator<NotificaCumuloModel> lListaNotificheIter = lListaNotificheInInput.iterator();
				while (lListaNotificheIter.hasNext()) {
					NotificaCumuloModel lNotInput = lListaNotificheIter.next();
					if (lNotInput.getIdNotificaCumulo() != null) {
						siesLogger.debug("Aggiorno Notifica con ID = " + lNotInput.getIdNotificaCumulo());
						lNotificaDao.setDAOFromModel(lNotInput);
						lNotificaDao.selCondizioneUpdate(lNotInput.getIdNotificaCumulo());
						lNotificaDao.update();
						lNotificaDao.stop();
					} else {
						siesLogger.debug("Inserisco Notifica ");
						lNotificaDao.setDAOFromModel(lNotInput);
						lNotificaDao.insert();
						lNotificaDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExModificaStatoEsecTitoloCumulatoFull: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lStaDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lNotificaSqlDao);
			cleanup(lNotificaDao);

			cleanup(lConn);
		}
	}

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaSospensioniDelPMByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;

		Vector<StatoEsecTitoloCumulatoModel> lStatoEsecTitoloCumulati = new Vector();

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			List<String> listCodiciSosp = new ArrayList<>();

			// Comma 5. Legge Simeone
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospC5Provv);
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospC5VVR);
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospC5DecIRR);
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospIstanza);
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospC5Revoca);

			// DL78
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospPM78);

			// Legge 199
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospPM199_Conc);
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospPM199_Sorv);
			listCodiciSosp.addAll(StatoEsecuzioneCumuloUtils.aCodSospPM199_Rev);

			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(aIdTitolo,
					listCodiciSosp);

			lStatoEsecTitoloCumulati = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());

			// Istanze comma 5. Cod motivo 0993 e COD_CONTENUTO_ISTANZA = C001. Rimuovo
			// dalla lista le istanze che no sono di accesso alle MS.
			for (int i = 0; i < lStatoEsecTitoloCumulati.size(); i++) {
				StatoEsecTitoloCumulatoModel lStato = lStatoEsecTitoloCumulati.elementAt(i);
				if (lStato.getCodMotivo().equals("0993") && !"C001".equals(lStato.getCodContenutoIstanza())) {
					lStatoEsecTitoloCumulati.remove(i);
					i--;
				}
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaStatoEsecTitoloCumulatoByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lConn);
		}

		return lStatoEsecTitoloCumulati;
	}

	// FUNGIBILITA'
	public BigDecimal ExInserisciFungibilitaCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException {

		Connection lConn = null;

		BigDecimal lIdStato = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Inserisco lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModel(aStatoEsecuz);

			lIdStato = lStatoEsecTitoloCumulatoDao.insert();
			aStatoEsecuz.setIdStatoEsecTitoloCumulato(lIdStato);

			lStatoEsecTitoloCumulatoDao.stop();

			// Inserisco e collego il computo
			// aComputoCumulo
			aComputoCumulo.setStatIdStatoEsecTitCum(lIdStato);
			lComputiDao = new ComputiCumuloDAO(lConn);
			lComputiDao.setDAOFromModel(aComputoCumulo);

			BigDecimal lIdComputo = lComputiDao.insert();
			aComputoCumulo.setIdComputiCumulo(lIdComputo);

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciFungibilita: Non posso Scrivere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}

		return lIdStato;
	} // CHIUDE ExInserisciFungibilitaCumulo()

	public void ExInserisciPeriodoFungibilitaCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Aggiorno lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModelForUpdate(aStatoEsecuz);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(aStatoEsecuz.getIdStatoEsecTitoloCumulato());
			lStatoEsecTitoloCumulatoDao.update();

			// Inserisco il Nuovo computo
			lComputiDao = new ComputiCumuloDAO(lConn);
			lComputiDao.setDAOFromModel(aComputoCumulo);
			lComputiDao.insert();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciPeriodoFungibilitaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
	} // Chiude ExInserisciPeriodoFungibilitaCumulo()

	public void ExModificaFungibilitaCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			ComputiCumuloModel aComputoCumulo) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Aggiorno lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModelForUpdate(aStatoEsecuz);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(aStatoEsecuz.getIdStatoEsecTitoloCumulato());
			lStatoEsecTitoloCumulatoDao.update();

			// Aggiorno il computo
			lComputiDao = new ComputiCumuloDAO(lConn);
			if (aComputoCumulo.getIdComputiCumulo() != null) {
				lComputiDao.setDAOFromModelForUpdate(aComputoCumulo);
				lComputiDao.selCondizioneUpdate(aComputoCumulo.getIdComputiCumulo());
				lComputiDao.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExModificaFungibilitaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
	} // Chiude ExModificaFungibilitaCumulo()

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaProvvedimentiCumuloByIdTitoloListaProvv(
			BigDecimal aIdTitolo, String aCodTipoEvento, String aCodTipoProvvedimento,
			Vector<String> listaProvv) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;

		Vector<StatoEsecTitoloCumulatoModel> lListaProvvedimentiCumulo = new Vector<>();

		StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();

		lStatoRicerca.setCodTipoEvento(aCodTipoEvento);
		lStatoRicerca.setCodTipoProvvedimento(aCodTipoProvvedimento);
		lStatoRicerca.setTitIdTitoloCumulato(aIdTitolo);

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatobylistaProvv(lStatoRicerca,
					listaProvv);

			lListaProvvedimentiCumulo = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());

			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			for (int i = 0; i < lListaProvvedimentiCumulo.size(); i++) {
				StatoEsecTitoloCumulatoModel lStatoModel = lListaProvvedimentiCumulo.elementAt(i);

				lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());

				Vector<ComputiCumuloModel> lListaPeriodi = new Vector(lComputiSqlDao.getModels());

				lStatoModel.setListaComputi(lListaPeriodi);
				lComputiSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaProvvedimentiCumuloByIdTitoloListaProvv: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lComputiSqlDao);

			cleanup(lConn);
		}

		return lListaProvvedimentiCumulo;
	} // CHIUDE ExRicercaProvvedimentiCumuloByIdTitoloListaProvv()

	public Vector<StatoEsecTitoloCumulatoModel> ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv(
			BigDecimal aIdTitolo, String aCodTipoEvento, Vector<String> listaTipoProvv,
			Vector<String> listaProvv) throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoloCumulatoSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoLibAntSqlDao = null;

		Vector<StatoEsecTitoloCumulatoModel> lListaProvvedimentiCumulo = new Vector<>();

		StatoEsecTitoloCumulatoModel lStatoRicerca = new StatoEsecTitoloCumulatoModel();
		LibAnticipataCumuloModel lLibAntCumMod = null;

		lStatoRicerca.setCodTipoEvento(aCodTipoEvento);
		lStatoRicerca.setTitIdTitoloCumulato(aIdTitolo);

		try {
			lConn = getDBConnection();

			lStatoEsecTitoloCumulatoSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);

			lStatoEsecTitoloCumulatoSqlDao.ricercaStatoEsecTitoloCumulatobylisteTipoMotivoProvv(lStatoRicerca,
					listaTipoProvv, listaProvv);

			lListaProvvedimentiCumulo = new Vector(lStatoEsecTitoloCumulatoSqlDao.getModels());

			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			lLibAntSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lPeriodoLibAntSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			for (int i = 0; i < lListaProvvedimentiCumulo.size(); i++) {
				StatoEsecTitoloCumulatoModel lStatoModel = lListaProvvedimentiCumulo.elementAt(i);

				lComputiSqlDao.ricercaComputiCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());

				Vector<ComputiCumuloModel> lListaPeriodi = new Vector(lComputiSqlDao.getModels());
				if (lListaPeriodi != null && lListaPeriodi.size() > 0) {
					lStatoModel.setListaComputi(lListaPeriodi);
				}

				lComputiSqlDao.stop();

				// Ricerca Eventuali Liberazioni Anticipate
				lLibAntSqlDao
						.ricercaLibAnticipataCumuloByIdStatoEsec(lStatoModel.getIdStatoEsecTitoloCumulato());
				Vector<LibAnticipataCumuloModel> lListaLiberazioni = new Vector<LibAnticipataCumuloModel>(
						lLibAntSqlDao.getModels());
				if (lListaLiberazioni != null && lListaLiberazioni.size() > 0) {
					for (int j = 0; j < lListaLiberazioni.size(); j++) {
						lLibAntCumMod = lListaLiberazioni.elementAt(j);

						// Ricerca eventuali Periodi di Liberazione Anticipata
						lPeriodoLibAntSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
								lLibAntCumMod.getIdLibAnticipataCumulo());
						Vector<PeriodoLibAntCumuloModel> lListaPeriodiLibAnt = new Vector<PeriodoLibAntCumuloModel>(
								lPeriodoLibAntSqlDao.getModels());
						if (lListaPeriodiLibAnt != null && lListaPeriodiLibAnt.size() > 0) {
							lLibAntCumMod.setListaPeriodiLibAnticipate(lListaPeriodiLibAnt);
						}

						// lPeriodoLibAntSqlDao.stop();
					}
					lPeriodoLibAntSqlDao.stop();
					lStatoModel.setListaLiberazioniAnticipate(lListaLiberazioni);
				}

				lLibAntSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoSqlDao);
			cleanup(lComputiSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lLibAntSqlDao);
			cleanup(lPeriodoLibAntSqlDao);

			cleanup(lConn);
		}

		return lListaProvvedimentiCumulo;
	} // CHIUDE ExRicercaProvvedimentiCumuloByIdTitoloListeTipoMotivoProvv()

	// =============================================
	// LIBERAZIONE ANTICIPATA CUMULO
	// =============================================
	public BigDecimal ExInserisciLiberazioneAnticipataCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			LibAnticipataCumuloModel aLibAntCumLAModel, LibAnticipataCumuloModel aLibAntCumLASPEModel,
			LibAnticipataCumuloModel aLibAntCumLAINTModel) throws F3BException {

		Connection lConn = null;
		BigDecimal lIdStato = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		LibAnticipataCumuloDAO lLibAntCumDao = null;
		PeriodoLibAntCumuloDAO lPeriodoCumDao = null;

		Vector<PeriodoLibAntCumuloModel> lVecPeriodi = null;

		try {
			lConn = getDBConnection();

			// Inserisco lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModel(aStatoEsecuz);
			lIdStato = lStatoEsecTitoloCumulatoDao.insert();
			aStatoEsecuz.setIdStatoEsecTitoloCumulato(lIdStato);
			lStatoEsecTitoloCumulatoDao.stop();

			// Inserisco e collego la Liberazione Anticipata e i Rispettivi Periodi
			Vector<LibAnticipataCumuloModel> VecLib = new Vector();

			if (aLibAntCumLAModel != null)
				VecLib.addElement(aLibAntCumLAModel);

			if (aLibAntCumLASPEModel != null)
				VecLib.addElement(aLibAntCumLASPEModel);

			if (aLibAntCumLAINTModel != null)
				VecLib.addElement(aLibAntCumLAINTModel);

			if (VecLib != null && VecLib.size() > 0) {
				Iterator ItxLib = VecLib.iterator();
				while (ItxLib.hasNext()) {
					LibAnticipataCumuloModel LibAntCumMod = (LibAnticipataCumuloModel) ItxLib.next();
					if (LibAntCumMod != null) {
						LibAntCumMod.setStatIdStatoEsecTitoloCum(lIdStato);
						lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
						lLibAntCumDao.setDAOFromModel(LibAntCumMod);
						BigDecimal lIdLA = lLibAntCumDao.insert();

						if (LibAntCumMod.getListaPeriodiLibAnticipate() != null
								&& LibAntCumMod.getListaPeriodiLibAnticipate().size() > 0) {
							lVecPeriodi = LibAntCumMod.getListaPeriodiLibAnticipate();

							Iterator Itx = lVecPeriodi.iterator();
							while (Itx.hasNext()) {
								PeriodoLibAntCumuloModel lPeriodoMod = (PeriodoLibAntCumuloModel) Itx.next();
								lPeriodoMod.setLibIdLibAnticipataCumulo(lIdLA);
								lPeriodoCumDao = new PeriodoLibAntCumuloDAO(lConn);
								lPeriodoCumDao.setDAOFromModel(lPeriodoMod);
								/* BigDecimal lIdPerLA = */lPeriodoCumDao.insert();
								lPeriodoCumDao.stop();
							}
						}
					}
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciLiberazioneAnticipataCumulo: Non posso Scrivere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lLibAntCumDao);
			cleanup(lPeriodoCumDao);

			cleanup(lConn);
		}

		return lIdStato;
	} // CHIUDE ExInserisciLiberazioneAnticipataCumulo()

	public BigDecimal ExModificaLiberazioneAnticipataCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			LibAnticipataCumuloModel aLibAntCumLAModel, LibAnticipataCumuloModel aLibAntCumLASPEModel,
			LibAnticipataCumuloModel aLibAntCumLAINTModel) throws F3BException {

		siesLogger.debug("--XX-- ExModificaLiberazioneAnticipataCumulo Inizio... ");

		Connection lConn = null;
		BigDecimal lIdStato = aStatoEsecuz.getIdStatoEsecTitoloCumulato();

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		LibAnticipataCumuloDAO lLibAntCumDao = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		PeriodoLibAntCumuloDAO lPeriodoCumDao = null;

		Vector<PeriodoLibAntCumuloModel> lVecPeriodi = null;

		try {
			lConn = getDBConnection();
			// ---------------------------------
			// MODIFICO LO STATO_ESECUZIONE
			// ---------------------------------
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModelForUpdate(aStatoEsecuz);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStato);
			lStatoEsecTitoloCumulatoDao.update();

			// siesLogger.debug("--XX-- StatoEsecTitoloCumulato Aggiornato ");

			// ---------------------------------------------------------------------------------------------------
			// Le L.A. e i relativi periodi da aggiornare, vengono comunque Cancellati e poi rinseriti
			// -----------------------------------------------------------------------------------------------------
			lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);

			lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(lIdStato);
			Vector<LibAnticipataCumuloModel> VecLiberazioni = new Vector<LibAnticipataCumuloModel>(
					lLibAntCumSqlDao.getModels());

			if (VecLiberazioni != null && VecLiberazioni.size() > 0) {
				Iterator ItxL1 = VecLiberazioni.iterator();
				while (ItxL1.hasNext()) {
					LibAnticipataCumuloModel LibAntMod = (LibAnticipataCumuloModel) ItxL1.next();
					if (LibAntMod != null && LibAntMod.getIdLibAnticipataCumulo() != null) {
						// Eliminazione dei Periodi legati alla L.A. Corrente
						lPeriodoCumDao = new PeriodoLibAntCumuloDAO(lConn);
						lPeriodoCumDao.selCondizioneLib_Id_LibAnt(LibAntMod.getIdLibAnticipataCumulo());
						lPeriodoCumDao.delete();
						lPeriodoCumDao.stop();

					}
				}

				// Eliminazione delle L.A
				lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
				lLibAntCumDao.selCondizioneUpdateByIdStatEsec(lIdStato);
				lLibAntCumDao.delete();
				lLibAntCumDao.stop();

			}

			// Inserimento delle L.A. modificate con i relativi Periodi
			Vector<LibAnticipataCumuloModel> VecLib = new Vector();

			if (aLibAntCumLAModel != null)
				VecLib.addElement(aLibAntCumLAModel); // Liberazioni Anticipate e relativi Periodi

			if (aLibAntCumLASPEModel != null)
				VecLib.addElement(aLibAntCumLASPEModel); // Liberazioni Anticipate Speciali e relativi Periodi

			if (aLibAntCumLAINTModel != null)
				VecLib.addElement(aLibAntCumLAINTModel); // Integrazioni Liberazioni Anticipate e relativi
															// Periodi

			if (VecLib != null && VecLib.size() > 0) {
				Iterator ItxLib = VecLib.iterator();
				while (ItxLib.hasNext()) {
					LibAnticipataCumuloModel LibAntCumMod = (LibAnticipataCumuloModel) ItxLib.next();
					if (LibAntCumMod != null) {
						LibAntCumMod.setStatIdStatoEsecTitoloCum(lIdStato);
						lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
						lLibAntCumDao.setDAOFromModel(LibAntCumMod);
						BigDecimal lIdLA = lLibAntCumDao.insert();

						if (LibAntCumMod.getListaPeriodiLibAnticipate() != null
								&& LibAntCumMod.getListaPeriodiLibAnticipate().size() > 0) {
							lVecPeriodi = LibAntCumMod.getListaPeriodiLibAnticipate();

							Iterator Itx = lVecPeriodi.iterator();
							while (Itx.hasNext()) {
								PeriodoLibAntCumuloModel lPeriodoMod = (PeriodoLibAntCumuloModel) Itx.next();
								lPeriodoMod.setLibIdLibAnticipataCumulo(lIdLA);
								lPeriodoCumDao = new PeriodoLibAntCumuloDAO(lConn);
								lPeriodoCumDao.setDAOFromModel(lPeriodoMod);
								/* BigDecimal lIdPerLA = */lPeriodoCumDao.insert();

								lPeriodoCumDao.stop();
							}
						}

					} // Chiude if(LibAntCumMod!=null )

				} // Chiude Ciclo while

			} // Chiude if(VecLib != null && VecLib.size() >0)

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExModificaLiberazioneAnticipataCumulo: Non posso Scrivere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lLibAntCumDao);
			cleanup(lLibAntCumSqlDao);
			cleanup(lPeriodoCumDao);

			cleanup(lConn);
		}

		return lIdStato;
	} // CHIUDE ExModificaLiberazioneAnticipataCumulo()

	public String ExInserisciLiberazioneAnticipataCumuloFullWithoutSequence(
			Vector<LibAnticipataCumuloModel> aVecLibAntCum, Connection aConn) throws F3BException {

		String lCodEsito = "00000";
		String QualeOggetto = "";
		BigDecimal QualeId = null;

		LibAnticipataCumuloDAO lLibDao = null;
		PeriodoLibAntCumuloDAO lPeriodoDao = null;

		LibAnticipataCumuloModel lLibModel = null;

		try {
			lLibDao = new LibAnticipataCumuloDAO(aConn);
			if (aVecLibAntCum != null && aVecLibAntCum.size() > 0) {
				// ====================================
				// LiberazioneAnticipataCumulo
				// ====================================
				for (int i = 0; i < aVecLibAntCum.size(); i++) {
					lLibModel = aVecLibAntCum.get(i);

					QualeOggetto = "Lib_Anticipata_Cumulo";
					QualeId = lLibModel.getIdLibAnticipataCumulo();

					try {
						lLibDao.setDAOFromModel(lLibModel);
						lLibDao.setWithoutSequence(true);
						lLibDao.insert();
						lLibDao.stop();
					} catch (DAOException ex) {
						if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
							siesLogger.warn(
									QualeOggetto + " gia' presente in archivio ... id= >" + QualeId + "<");
							lCodEsito = "00001";
						} else {
							throw ex;
						}
					}

					// ====================================
					// PeriodoLibAnticipataCumulo
					// ====================================
					if (lLibModel.getListaPeriodiLibAnticipate() != null) {
						Vector<PeriodoLibAntCumuloModel> lListaPeriodo = lLibModel
								.getListaPeriodiLibAnticipate();
						Iterator<PeriodoLibAntCumuloModel> lPeriodi = lListaPeriodo.iterator();

						QualeOggetto = "Periodo_Lib_Ant_Cumulo";
						lPeriodoDao = new PeriodoLibAntCumuloDAO(aConn);
						while (lPeriodi.hasNext()) {
							try {
								PeriodoLibAntCumuloModel lPeriodoMod = lPeriodi.next();
								QualeId = lPeriodoMod.getIdPeriodoLibAntCumulo();

								lPeriodoDao.setDAOFromModel(lPeriodoMod);
								lPeriodoDao.setWithoutSequence(true);
								lPeriodoDao.insert();
								lPeriodoDao.stop();
							} catch (DAOException ex) {
								if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
									siesLogger.warn(QualeOggetto + " gia' presente in archivio ... id= >"
											+ QualeId + "<");
									lCodEsito = "00001";
								} else {
									throw ex;
								}
							}
						}
					}
				}
			}
		} catch (DAOException ex) {
			lCodEsito = "01400";
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire --->" + QualeOggetto + " - id = >" + QualeId + "<");
		} finally {
			cleanup(lLibDao);
			cleanup(lPeriodoDao);
		}

		return lCodEsito;
	} // CHIUDE ExInserisciLiberazioneAnticipataCumuloFullWithoutSequence

	// RIMEDI RISARCITORI
	public BigDecimal ExInserisciRimediRisarcitoriCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<LibAnticipataCumuloModel> VecLib) throws F3BException {

		Connection lConn = null;
		BigDecimal lIdStato = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		LibAnticipataCumuloDAO lLibAntCumDao = null;
		PeriodoLibAntCumuloDAO lPeriodoCumDao = null;

		Vector<PeriodoLibAntCumuloModel> lVecPeriodi = null;

		try {
			lConn = getDBConnection();

			// Inserisco lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModel(aStatoEsecuz);
			lIdStato = lStatoEsecTitoloCumulatoDao.insert();
			aStatoEsecuz.setIdStatoEsecTitoloCumulato(lIdStato);
			lStatoEsecTitoloCumulatoDao.stop();

			// Inserisco e collego la Licenza (RD o SL) e i Rispettivi Periodi
			if (VecLib != null && VecLib.size() > 0) {
				Iterator ItxLib = VecLib.iterator();
				while (ItxLib.hasNext()) {
					LibAnticipataCumuloModel LibAntCumMod = (LibAnticipataCumuloModel) ItxLib.next();
					if (LibAntCumMod != null) {
						LibAntCumMod.setStatIdStatoEsecTitoloCum(lIdStato);
						lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
						lLibAntCumDao.setDAOFromModel(LibAntCumMod);
						BigDecimal lIdLA = lLibAntCumDao.insert();

						if (LibAntCumMod.getListaPeriodiLibAnticipate() != null
								&& LibAntCumMod.getListaPeriodiLibAnticipate().size() > 0) {
							lVecPeriodi = LibAntCumMod.getListaPeriodiLibAnticipate();

							Iterator Itx = lVecPeriodi.iterator();
							while (Itx.hasNext()) {
								PeriodoLibAntCumuloModel lPeriodoMod = (PeriodoLibAntCumuloModel) Itx.next();
								lPeriodoMod.setLibIdLibAnticipataCumulo(lIdLA);
								lPeriodoCumDao = new PeriodoLibAntCumuloDAO(lConn);
								lPeriodoCumDao.setDAOFromModel(lPeriodoMod);
								/* BigDecimal lIdPerLA = */lPeriodoCumDao.insert();

								lPeriodoCumDao.stop();
							}
						}
					}
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);

			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciRimediRisarcitoriCumulo: Non posso Scrivere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lLibAntCumDao);
			cleanup(lPeriodoCumDao);

			cleanup(lConn);
		}

		return lIdStato;
	} // CHIUDE ExInserisciRimediRisarcitoriCumulo()

	public BigDecimal ExModificaRimediRisarcitoriCumulo(StatoEsecTitoloCumulatoModel aStatoEsecuz,
			Vector<LibAnticipataCumuloModel> VecLib) throws F3BException {

		siesLogger.debug("--XX-- ExModificaRimediRisarcitoriCumulo Inizio... ");

		Connection lConn = null;
		BigDecimal lIdStato = aStatoEsecuz.getIdStatoEsecTitoloCumulato();

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		LibAnticipataCumuloDAO lLibAntCumDao = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		PeriodoLibAntCumuloDAO lPeriodoCumDao = null;

		Vector<PeriodoLibAntCumuloModel> lVecPeriodi = null;

		try {
			lConn = getDBConnection();
			// ---------------------------------
			// MODIFICO LO STATO_ESECUZIONE
			// ---------------------------------
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);
			lStatoEsecTitoloCumulatoDao.setDAOFromModelForUpdate(aStatoEsecuz);
			lStatoEsecTitoloCumulatoDao.selCondizioneUpdate(lIdStato);
			lStatoEsecTitoloCumulatoDao.update();

			// ---------------------------------------------------------------------------------------------------
			// Le Licenze Rd e SL e i relativi periodi da aggiornare, vengono comunque CANCELLATI e poi
			// rinseriti
			// -----------------------------------------------------------------------------------------------------
			lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);

			lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(lIdStato);
			Vector<LibAnticipataCumuloModel> VecLiberazioni = new Vector<LibAnticipataCumuloModel>(
					lLibAntCumSqlDao.getModels());

			if (VecLiberazioni != null && VecLiberazioni.size() > 0) {
				Iterator ItxL1 = VecLiberazioni.iterator();
				while (ItxL1.hasNext()) {
					LibAnticipataCumuloModel LibAntMod = (LibAnticipataCumuloModel) ItxL1.next();
					if (LibAntMod != null && LibAntMod.getIdLibAnticipataCumulo() != null) {
						// Eliminazione dei Periodi legati alla L.A. Corrente
						lPeriodoCumDao = new PeriodoLibAntCumuloDAO(lConn);
						lPeriodoCumDao.selCondizioneLib_Id_LibAnt(LibAntMod.getIdLibAnticipataCumulo());
						lPeriodoCumDao.delete();
						lPeriodoCumDao.stop();

					}
				}

				// Eliminazione delle L.A
				lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
				lLibAntCumDao.selCondizioneUpdateByIdStatEsec(lIdStato);
				lLibAntCumDao.delete();
				lLibAntCumDao.stop();

			}

			// Inserimento delle L.A. modificate con i relativi Periodi
			if (VecLib != null && VecLib.size() > 0) {
				Iterator ItxLib = VecLib.iterator();
				while (ItxLib.hasNext()) {
					LibAnticipataCumuloModel LibAntCumMod = (LibAnticipataCumuloModel) ItxLib.next();
					if (LibAntCumMod != null) {
						LibAntCumMod.setStatIdStatoEsecTitoloCum(lIdStato);
						lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
						lLibAntCumDao.setDAOFromModel(LibAntCumMod);
						BigDecimal lIdLA = lLibAntCumDao.insert();

						if (LibAntCumMod.getListaPeriodiLibAnticipate() != null
								&& LibAntCumMod.getListaPeriodiLibAnticipate().size() > 0) {
							lVecPeriodi = LibAntCumMod.getListaPeriodiLibAnticipate();

							Iterator Itx = lVecPeriodi.iterator();
							while (Itx.hasNext()) {
								PeriodoLibAntCumuloModel lPeriodoMod = (PeriodoLibAntCumuloModel) Itx.next();
								lPeriodoMod.setLibIdLibAnticipataCumulo(lIdLA);
								lPeriodoCumDao = new PeriodoLibAntCumuloDAO(lConn);
								lPeriodoCumDao.setDAOFromModel(lPeriodoMod);
								/* BigDecimal lIdPerLA = */lPeriodoCumDao.insert();
								lPeriodoCumDao.stop();
							}
						}
					} // Chiude if(LibAntCumMod!=null )
				} // Chiude Ciclo while
			} // Chiude if(VecLib != null && VecLib.size() >0)

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExModificaRimediRisarcitoriCumulo: Non posso Scrivere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lLibAntCumDao);
			cleanup(lLibAntCumSqlDao);
			cleanup(lPeriodoCumDao);

			cleanup(lConn);
		}

		return lIdStato;
	} // Chiude ExModificaRimediRisarcitoriCumulo()

	/**
	 *
	 * @param aEventoModel
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiRidetPenaPMAltro(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		AnnotazioneManualeSqlDAO lAnnotaSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ReatoCumuloSqlDAO lReatoCumuloSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			// Lettura delle Annotazioni manuali
			lAnnotaSqlDao = new AnnotazioneManualeSqlDAO(aConn);

			// Annotazione legata all'annotazione di rideterminazione pena PM altro.
			lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(lEvento.getIdEvento());

			Vector<AnnotazioneManualeModel> lListaAnnotazioni = new Vector(lAnnotaSqlDao.getModels());
			lComputiDao = new ComputiCumuloDAO(aConn);

			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnota = lListaAnnotazioni.elementAt(i);

				// siesLogger.debug("IdAnnotazione = "+lAnnota.getIdAnnotazioneManuale());

				ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

				lComputiModel.setCodTipoAnnotazione(lAnnota.getCodTipoAnnotazione());
				lComputiModel.setCodCausaleComputo("-");
				lComputiModel.setFlagPiuMeno(lAnnota.getFlagPiuMeno());

				lComputiModel.setNumAnniReclusione(lAnnota.getNumAnniReclusione());
				lComputiModel.setNumMesiReclusione(lAnnota.getNumMesiReclusione());
				lComputiModel.setNumGiorniReclusione(lAnnota.getNumGiorniReclusione());

				lComputiModel.setNumAnniArresto(lAnnota.getNumAnniArresto());
				lComputiModel.setNumMesiArresto(lAnnota.getNumMesiArresto());
				lComputiModel.setNumGiorniArresto(lAnnota.getNumGiorniArresto());

				lComputiModel.setCodTipoMisura(null);

				lComputiModel.setNote(lAnnota.getMotivazioni());

				lComputiModel.setImportoMulta(lAnnota.getImportoMulta());
				lComputiModel.setImportoAmmenda(lAnnota.getImportoAmmenda());
				lComputiModel.setCodDpr(lAnnota.getCodDpr());

				lComputiModel.setCodUfficioEmittenteProvv(lEvento.getCodUfficioEmittente());
				lComputiModel.setCodLuogoUfficioProvv(lEvento.getCodLuogoEmittente());
				lComputiModel.setDataEmissioneProvv(lAnnota.getDataGE());
				// lComputiModel.setDataRicezioneProvv (lEvento.getDataRicezioneAtti() );
				lComputiModel.setAnnoProvv(lAnnota.getChiaveAnnoSige());
				lComputiModel.setProgrProvv(lAnnota.getChiaveNumeroSige());

				// siesLogger.debug("lAnnota.getReaIdReato() = "+lAnnota.getReaIdReato());
				if (lAnnota.getReaIdReato() != null) {
					// siesLogger.debug("Individuazione del ReatoCumulo: idReato = "+lAnnota.getReaIdReato());

					lReatoCumuloSqlDao = new ReatoCumuloSqlDAO(aConn);
					lReatoCumuloSqlDao.ricercaReatiCumuloByIdTitolo(aStatoEsecModel.getTitIdTitoloCumulato());

					Vector<ReatoCumuloModel> lReaVect = new Vector(lReatoCumuloSqlDao.getModels());
					lReatoCumuloSqlDao.stop();

					for (int ii = 0; ii < lReaVect.size(); ii++) {
						ReatoCumuloModel lReaCum = lReaVect.get(ii);
						if (lReaCum.getIdReatoOrigine().compareTo(lAnnota.getReaIdReato()) == 0
								&& lReaCum.getProgrCircostanza().toString().equals("1")) {
							lComputiModel.setReaIdReatoCum(lReaCum.getIdReatoCum());
							break;
						}
					}
				}

				/*
				 * // Individuazione del ReatoCumulo. ReatoCumuloModel lReaCumMod = new ReatoCumuloModel();
				 * lReaCumMod.setIdReatoOrigine(lAnnota.getReaIdReato()); IReatoCumulo lCtrlR =
				 * SIEPLookupRemote.getReatoCumuloRemote(); Vector<ReatoCumuloModel> lReaVect =
				 * lCtrlR.ExRicercaReatoCumulo(lReaCumMod);
				 *
				 * ss if (lReaVect.size() > 0) for (int ii=0; i<lReaVect.size();ii++) { ReatoCumuloModel
				 * lReaCum = lReaVect.get(ii); if (lReaCum.getProgrCircostanza().toString().equals("1"))
				 * lComputiModel.setReaIdReatoCum (lReaCum.getIdReatoCum() ); break; }
				 */

				// Eventuale valorizzazione dei dati del provvedimento Origine.
				if (aStatoEsecModel.getEveIdEventoOrigine() != null) {
					EventoModel lEventoOri = new EventoModel();
					lEveSqlDao = new EventoSqlDAO(aConn);
					lEveSqlDao.ricercaEventoByKey(aStatoEsecModel.getEveIdEventoOrigine());
					lEventoOri = (EventoModel) lEveSqlDao.getModelByKey();

					lComputiModel.setDataRicezioneProvv(lEventoOri.getDataRicezioneAtti());
					lComputiModel.setDataEmissioneProvv(lEventoOri.getDataEmissione());
					lComputiModel.setAnnoProvv(lEventoOri.getAnnoProtocollo());
					lComputiModel.setProgrProvv(lEventoOri.getProgrProtocollo());
					lComputiModel.setCodUfficioEmittenteProvv(lEventoOri.getCodUfficioEmittente());
					lComputiModel.setCodLuogoUfficioProvv(lEventoOri.getCodLuogoEmittente());
					lComputiModel.setCodTipoProvv(lEventoOri.getCodTipoProvvedimento());
				}

				lComputiModel.setFlagStato("E");
				lComputiModel.setMotivoModifica(null);

				lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
				lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
				lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

				lComputiDao.setDAOFromModel(lComputiModel);
				lComputiDao.insert();

				lComputiDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiRidetPenaPMAltro: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotaSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
			cleanup(lReatoCumuloSqlDao);
		}
	}

	/**
	 * 22/05/2019 MEV70
	 *
	 * @param aEventoModel
	 * @param aIdStatoEsec
	 * @param aConn
	 * @throws F3BException
	 */
	private void caricaComputiRevocaPenaSospesa(BigDecimal aIdEvento,
			StatoEsecTitoloCumulatoModel aStatoEsecModel, Connection aConn) throws F3BException {

		AnnotazioneManualeSqlDAO lAnnotaSqlDao = null;
		ComputiCumuloDAO lComputiDao = null;
		EventoSqlDAO lEveSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;

		try {
			// Lettura dell'Evento
			EventoModel lEvento = new EventoModel();
			lEveSqlDao = new EventoSqlDAO(aConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();

			// Lettura delle Annotazioni manuali
			lAnnotaSqlDao = new AnnotazioneManualeSqlDAO(aConn);

			// Annotazione legata alla Sentenza/Ordinanza di Revoca Pena Sospesa.
			lAnnotaSqlDao.ricercaAnnotazioneManualeByIdEvento(lEvento.getIdEvento());

			Vector<AnnotazioneManualeModel> lListaAnnotazioni = new Vector(lAnnotaSqlDao.getModels());
			lComputiDao = new ComputiCumuloDAO(aConn);

			for (int i = 0; i < lListaAnnotazioni.size(); i++) {
				AnnotazioneManualeModel lAnnota = lListaAnnotazioni.elementAt(i);

				// siesLogger.debug("IdAnnotazione = "+lAnnota.getIdAnnotazioneManuale());

				ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

				lComputiModel.setCodTipoAnnotazione("-");
				lComputiModel.setCodCausaleComputo("-");

				lComputiModel.setCodTipoMisura(null);

				lComputiModel.setNote(lAnnota.getMotivazioni());

				if (lAnnota.getDataSentenzaSiap() == null) {
					lComputiModel.setDataEmissioneProvv(lAnnota.getDataGE());
					lComputiModel.setAnnoProvv(lAnnota.getAnnoGe());
					lComputiModel.setProgrProvv(new BigDecimal(lAnnota.getNumeroGe()));
					lComputiModel.setCodTipoProvv("03");
				} else {
					lComputiModel.setAnnoRege(lAnnota.getAnnoRege());
					lComputiModel.setNumeroRege(lAnnota.getNumeroRege());
					lComputiModel.setAnnoProvv(lAnnota.getAnnoSentenzaSiap());
					lComputiModel.setProgrProvv(new BigDecimal(lAnnota.getNumeroSentenzaSiap()));
					lComputiModel.setDataEmissioneProvv(lAnnota.getDataSentenzaSiap());
					lComputiModel.setAnnoRegePM(lAnnota.getAnnoGe());
					lComputiModel.setNumeroRegePM(lAnnota.getNumeroGe());
					lComputiModel.setCodTipoProvv("01");
				}
				lUffSqlDao = new UfficioSqlDAO(aConn);
				lUffSqlDao.selUfficioByCodTipoUffCodComune(lAnnota.getCodTipoUfficioSiep(),
						lAnnota.getCodLuogoUfficioSiep());
				UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
				lUffSqlDao.stop();

				if (lUffMod != null && lUffMod.getCodUfficio() != null) {
					lComputiModel.setCodUfficioEmittenteProvv(lUffMod.getCodUfficio());
					lComputiModel.setCodLuogoUfficioProvv(lAnnota.getCodLuogoUfficioSiep());
				}

				lComputiModel.setFlagStato("E");
				lComputiModel.setMotivoModifica(null);

				lComputiModel.setTitIdTitoloCumulato(aStatoEsecModel.getTitIdTitoloCumulato());
				lComputiModel.setIstrIdIstruttoriaCumulo(aStatoEsecModel.getIstrIdIstruttoriaCumulo());
				lComputiModel.setStatIdStatoEsecTitCum(aStatoEsecModel.getIdStatoEsecTitoloCumulato());

				lComputiModel.setCodOperatoreInserimento(aStatoEsecModel.getCodOperatoreInserimento());
				lComputiModel.setDataInserimento(aStatoEsecModel.getDataInserimento());
				lComputiModel.setCodUfficioInserimento(aStatoEsecModel.getCodUfficioInserimento());

				lComputiDao.setDAOFromModel(lComputiModel);
				lComputiDao.insert();

				lComputiDao.stop();
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.caricaComputiRevocaPenaSospesa: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lAnnotaSqlDao);
			cleanup(lComputiDao);
			cleanup(lEveSqlDao);
			cleanup(lUffSqlDao);
		}
	}

	/**
	 * @param aStatoEsecuzione
	 * @param aListaComputi
	 *            Vector<ComputiCumuloModel>
	 * @throws F3BException
	 */
	public StatoEsecTitoloCumulatoModel ExInserisciStatoEsecComputiCumulo(
			StatoEsecTitoloCumulatoModel aStatoEsecuzione, Vector<ComputiCumuloModel> aListaComputi)
			throws F3BException {

		Connection lConn = null;

		StatoEsecTitoloCumulatoDAO lStatoEsecTitoloCumulatoDao = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lConn = getDBConnection();

			// Inserisco lo stato esecuzione
			lStatoEsecTitoloCumulatoDao = new StatoEsecTitoloCumulatoDAO(lConn);

			lStatoEsecTitoloCumulatoDao.setDAOFromModel(aStatoEsecuzione);

			BigDecimal lIdStato = lStatoEsecTitoloCumulatoDao.insert();
			aStatoEsecuzione.setIdStatoEsecTitoloCumulato(lIdStato);

			lStatoEsecTitoloCumulatoDao.stop();

			// Inserisco e collego i Computi Cumulo
			for (int i = 0; i < aListaComputi.size(); i++) {
				ComputiCumuloModel aComputoCumulo = aListaComputi.elementAt(i);
				aComputoCumulo.setStatIdStatoEsecTitCum(lIdStato);

				lComputiDao = new ComputiCumuloDAO(lConn);
				lComputiDao.setDAOFromModel(aComputoCumulo);
				BigDecimal lIdComputo = lComputiDao.insert();
				aComputoCumulo.setIdComputiCumulo(lIdComputo);

				lComputiDao.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"StatoEsecTitoloCumulatoController.ExInserisciStatoEsecComputiCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lStatoEsecTitoloCumulatoDao);
			cleanup(lComputiDao);

			cleanup(lConn);
		}
		return aStatoEsecuzione;
	}

}