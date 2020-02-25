package siap.bdmc.sbpren.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.bdmc.sbpren.dao.SbPrenDAO;
import siap.bdmc.sbpren.dao.SbPrenSqlDAO;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbpren.model.SbPrenModel;
import siap.controller.SiapController;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbPrenController
 * </p>
 * <p>
 * Description: Classe Controller per SbPren
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
public class SbPrenController extends SiapController implements ISbPren {

	/*****************************************************************************
	 * Effettua l'inserimento di un SbPren a partire dai dati contenuti nel Model
	 * 
	 * @param aSbPren
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public SbPrenModel ExInserisciSbPren(SbPrenModel aSbPren) throws F3BException {
		Connection lConn = null;
		SbPrenDAO lSbPDao = null;
		SbPrenModel lSbPMod = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPrenDAO(lConn);
			lSbPDao.setDAOFromModel(aSbPren);
			BigDecimal lSequence = lSbPDao.insert();
			commit(lConn);
			lSbPMod = new SbPrenModel(aSbPren);
			lSbPMod.setMessage("Inserimento avvenuto correttamente!");
			lSbPMod.setIdPren(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbPrenController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}

		return lSbPMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati SbPren
	 * 
	 * @param aSbPren
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbPren(SbPrenModel aSbPren) throws F3BException {
		Connection lConn = null;
		Vector lSbPrei = new Vector();
		SbPrenDAO lSbPDao = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPrenDAO(lConn);
			lSbPDao.setCondizioni(aSbPren);
			lSbPDao.setOrderBy();
			lSbPDao.start();
			while (lSbPDao.next()) {
				lSbPrei.add((SbPrenModel) lSbPDao.getModel());
			}
			lSbPDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("Problemi durante la connessione alla Banca Dati Misure Cautelari: "
					+ daoEx);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}

		return lSbPrei;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei soggetti omonimi in SIES
	 * 
	 * @param SbPren
	 *            Model
	 * @return il modellone con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ProvvedimentoModelBDMC ExRicercaSogg(SbPrenModel aSbPren, ProvvedimentoModelBDMC aProvvedimento)
			throws F3BException {

		// leggo il Soggetto dal modello SbPren e lo setto in quello SIES
		SoggettoModel lSogg = new SoggettoModel();
		lSogg.setCodAfis(aSbPren.getCodiIdenAfis());
		lSogg.setCognome(aSbPren.getCognSogg());
		lSogg.setNome(aSbPren.getNomeSogg());
		lSogg.setDataNascitaPresunta("N");
		// lSogg.setAnnoNascita(new BigDecimal(this.getAnnoNascita()));
		lSogg.setDataNascita(aSbPren.getDataNasc());
		// ??lSogg.setCodComuneNascita(aSbPren.getLuogNasc());
		lSogg.setDescrComuneNascita(aSbPren.getLuogNasc());
		// lSogg.setCodProvinciaNascita(??);
		// lSogg.setDescrProvinciaNascita(??);
		lSogg.setCodStatoNascita(aSbPren.getCodiStat());
		lSogg.setDescrStatoNascita(aSbPren.getDescriStat());
		// lSogg.setDescComuneNascitaEstero(??);
		// lSogg.setNazionalita(??);
		// lSogg.setPaternita(this.getPaternita());
		// lSogg.setCognomeMadre(this.getCognomeMadre());
		// lSogg.setNomeMadre(this.getNomeMadre());
		lSogg.setSesso(aSbPren.getFlagSess());
		// lSogg.setAttoNascita(this.getAttoNascita());
		// lSogg.setNote(this.getNote());
		// lSogg.setCodOperatoreInserimento(this.getCodOperatoreInserimento());
		// lSogg.setDataInserimento(this.getDataInserimento());
		// lSogg.setCodUfficioInserimento(this.getCodUfficioInserimento());
		// lSogg.setDescrUfficioInserimento(this.getDescrUfficioInserimento());
		// lSogg.setCodOperatoreAggiornamento(this.getCodOperatoreAggiornamento());
		// lSogg.setDataAggiornamento(this.getDataAggiornamento());
		// lSogg.setCodUfficioAggiornamento(this.getCodUfficioAggiornamento());
		// lSogg.setDescrUfficioAggiornamento(this.getDescrUfficioAggiornamento());
		// lSogg.setCodComuneCasellario(this.getCodComuneCasellario());

		// Setto il Soggetto
		if (aSbPren.getCognSogg() != null) {

			// Cerco l'omonimo in SIEP
			ISoggetto lCSiepSoggetto = SICOLookupRemote.getSoggettoRemote();

			// Qui resta il dubbio di dover cambiare la query eliminando il like!!!!
			Vector lSoggettiOmonimi = lCSiepSoggetto.ExRicercaSoggettiOmonimi(lSogg);
			if (lSoggettiOmonimi.size() > 0) {
				// Carico i soggetti omonimi
				aProvvedimento.setSoggettiOmonimi(lSoggettiOmonimi);
			}
		} else
			// Soggetto non Trovato
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile visualizzare il dettaglio del Provvedimento "
							+ "</font>.<br>Nessun soggetto associato al Provvedimento selezionato.");

		return aProvvedimento;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public SbPrenModel ExRicercaSbPrenById(BigDecimal aIdPren) throws F3BException {
		Connection lConn = null;
		SbPrenModel lSbPrenMod = new SbPrenModel();
		SbPrenSqlDAO lSbPrenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbPrenSqlDao = new SbPrenSqlDAO(lConn);
			lSbPrenSqlDao.ricercaSbPrenByKey(aIdPren);
			lSbPrenMod = (SbPrenModel) lSbPrenSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("SbPrenController.ExRicercaSbPrenById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPrenSqlDao);
			cleanup(lConn);
		}

		return lSbPrenMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'SbPren Viene fatto l'update di tutti i campi del record recuperando i
	 * valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno impostati a
	 * null
	 * 
	 * @param aSbPren
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaSbPren(SbPrenModel aSbPren) throws F3BException {
		Connection lConn = null;
		SbPrenDAO lSbPDao = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPrenDAO(lConn);
			lSbPDao.setDAOFromModel(aSbPren);
			lSbPDao.selCondizioneUpdate(aSbPren.getIdPren());
			lSbPDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("SbPrenController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aSbPren
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaSbPren(SbPrenModel aSbPren) throws F3BException {
		Connection lConn = null;
		SbPrenDAO lSbPDao = null;

		try {
			lConn = getDBConnection();
			lSbPDao = new SbPrenDAO(lConn);
			lSbPDao.selCondizioneUpdate(aSbPren.getIdPren());
			lSbPDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("SbPrenController.ExCancellaSbPren: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aSbPren
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountSbPren(SbPrenModel aSbPren) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		SbPrenSqlDAO lSbPrenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbPrenSqlDao = new SbPrenSqlDAO(lConn);
			lSbPrenSqlDao.getCountSbPren(aSbPren);
			lSbPrenSqlDao.start();
			lSbPrenSqlDao.next();
			lCount = lSbPrenSqlDao.getBigDecimal("HowManyRecords");
			lSbPrenSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("SbPrenController.ExGetCountSbPren: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPrenSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aSbPren
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaSbPrenPaged(SbPrenModel aSbPren, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lSbPrei = new Vector();
		SbPrenSqlDAO lSbPrenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSbPrenSqlDao = new SbPrenSqlDAO(lConn);
			lSbPrenSqlDao.ricercaSbPrenPaged(aSbPren, aPage);
			lSbPrei = new Vector(lSbPrenSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("SbPrenController.ExRicercaSbPrenPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lSbPrenSqlDao);
			cleanup(lConn);
		}
		return lSbPrei;
	}

}