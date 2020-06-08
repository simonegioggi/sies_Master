package siap.siep.misuracautelarebdmc.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misuracautelarebdmc.dao.MisuraCautelareBdmcDAO;
import siap.siep.misuracautelarebdmc.dao.MisuraCautelareBdmcSqlDAO;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: MisuraCautelareBdmcController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraCautelareBdmc
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
public class MisuraCautelareBdmcController extends SiapController implements IMisuraCautelareBdmc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un MisuraCautelareBdmc a partire dai dati contenuti nel Model
	 *
	 * @param aMisuraCautelareBdmc
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public MisuraCautelareBdmcModel ExInserisciMisuraCautelareBdmc(
			MisuraCautelareBdmcModel aMisuraCautelareBdmc) throws F3BException {

		Connection lConn = null;
		MisuraCautelareBdmcDAO lMisDao = null;
		MisuraCautelareBdmcModel lMisMod = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareBdmcDAO(lConn);
			lMisDao.setDAOFromModel(aMisuraCautelareBdmc);
			BigDecimal lSequence = lMisDao.insert();
			commit(lConn);
			lMisMod = new MisuraCautelareBdmcModel(aMisuraCautelareBdmc);
			lMisMod.setMessage("Inserimento avvenuto correttamente!");
			lMisMod.setIdMisuraCautelareBdmc(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("MisuraCautelareBdmcController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati MisuraCautelareBdmc
	 *
	 * @param aMisuraCautelareBdmc
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc)
			throws F3BException {

		Connection lConn = null;
		Vector lMisuraCautelareBdmi = new Vector();
		MisuraCautelareBdmcDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareBdmcDAO(lConn);
			lMisDao.setCondizioni(aMisuraCautelareBdmc);
			lMisDao.setOrderBy();
			lMisDao.start();
			while (lMisDao.next()) {
				lMisuraCautelareBdmi.add(lMisDao.getModel());
			}
			lMisDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareBdmcController.ExRicercaMisuraCautelareBdmc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisuraCautelareBdmi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public MisuraCautelareBdmcModel ExRicercaMisuraCautelareBdmcById(BigDecimal aIdMisuraCautelare)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareBdmcModel lMisuraCautelareBdmcMod = new MisuraCautelareBdmcModel();
		MisuraCautelareBdmcSqlDAO lMisuraCautelareBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraCautelareBdmcSqlDao = new MisuraCautelareBdmcSqlDAO(lConn);
			lMisuraCautelareBdmcSqlDao.ricercaMisuraCautelareBdmcByKey(aIdMisuraCautelare);
			lMisuraCautelareBdmcMod = (MisuraCautelareBdmcModel) lMisuraCautelareBdmcSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareBdmcController.ExRicercaMisuraCautelareBdmcById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraCautelareBdmcSqlDao);
			cleanup(lConn);
		}

		return lMisuraCautelareBdmcMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'MisuraCautelareBdmc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aMisuraCautelareBdmc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc,
			EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		MisuraCautelareBdmcDAO lMisDao = null;
		boolean flagComunica = true;
		if (aEvento != null) {
			IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
			aEvento = lCtrEve.ExRicercaEventoByKey(aEvento.getIdEvento());
			TipoEventiBdmcModel lTipoEven = new TipoEventiBdmcModel();
			ITipoEventiBdmc lCtrlTipoEven = SIEPLookupRemote.getTipoEventiBdmcRemote();
			lTipoEven.setCodTipoEvento(aEvento.getCodTipoEvento());
			lTipoEven.setCodProvvedimento(aEvento.getCodTipoProvvedimento());
			lTipoEven.setCodMotivo(aEvento.getCodMotivo());
			Vector trovaEvento = lCtrlTipoEven.ExRicercaTipoEventiBdmc(lTipoEven);
			if (trovaEvento != null && trovaEvento.size() == 0)
				flagComunica = false;
		}
		if (flagComunica) {
			try {
				lConn = getDBConnection();
				lMisDao = new MisuraCautelareBdmcDAO(lConn);
				lMisDao.setDAOFromModel(aMisuraCautelareBdmc);
				lMisDao.selCondizioneUpdate(aMisuraCautelareBdmc.getIdMisuraCautelareBdmc());
				lMisDao.update();
				commit(lConn);
			} catch (DAOException ex) {
				rollback(lConn);
				throw new F3BException("MisuraCautelareBdmcController.ExModifica: Non posso inserire: " + ex);
			} finally {
				cleanup(lMisDao);
				cleanup(lConn);
			}
		}
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'MisuraCautelareBdmc Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aMisuraCautelareBdmc
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaMisuraCautelareBdmcNoCommit(Connection lConn,
			MisuraCautelareBdmcModel aMisuraCautelareBdmc, EventoModel aEvento) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Sono dentro update misure cautelari bdmc no commit");
		MisuraCautelareBdmcDAO lMisDao = null;

		boolean flagComunica = true;
		if (aEvento != null) {
			IEvento lCtrEve = SICOLookupRemote.getEventoRemote();
			aEvento = lCtrEve.ExRicercaEventoByKey(aEvento.getIdEvento());
			TipoEventiBdmcModel lTipoEven = new TipoEventiBdmcModel();
			ITipoEventiBdmc lCtrlTipoEven = SIEPLookupRemote.getTipoEventiBdmcRemote();
			lTipoEven.setCodTipoEvento(aEvento.getCodTipoEvento());
			lTipoEven.setCodProvvedimento(aEvento.getCodTipoProvvedimento());
			lTipoEven.setCodMotivo(aEvento.getCodMotivo());
			Vector trovaEvento = lCtrlTipoEven.ExRicercaTipoEventiBdmc(lTipoEven);
			if (trovaEvento != null && trovaEvento.size() == 0)
				flagComunica = false;
		}

		if (flagComunica) {
			try {
				// lConn = getDBConnection();
				lMisDao = new MisuraCautelareBdmcDAO(lConn);
				lMisDao.setDAOFromModel(aMisuraCautelareBdmc);
				lMisDao.selCondizioneUpdate(aMisuraCautelareBdmc.getIdMisuraCautelareBdmc());
				lMisDao.update();
				// commit(lConn);
			} catch (DAOException ex) {
				// rollback(lConn);
				throw new F3BException("MisuraCautelareBdmcController.ExModifica: Non posso inserire: " + ex);
			} finally {
				cleanup(lMisDao);
			}
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aMisuraCautelareBdmc
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareBdmcDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareBdmcDAO(lConn);
			lMisDao.selCondizioneUpdate(aMisuraCautelareBdmc.getIdMisuraCautelareBdmc());
			lMisDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareBdmcController.ExCancellaMisuraCautelareBdmc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aMisuraCautelareBdmc
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountMisuraCautelareBdmc(MisuraCautelareBdmcModel aMisuraCautelareBdmc)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MisuraCautelareBdmcSqlDAO lMisuraCautelareBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraCautelareBdmcSqlDao = new MisuraCautelareBdmcSqlDAO(lConn);
			lMisuraCautelareBdmcSqlDao.getCountMisuraCautelareBdmc(aMisuraCautelareBdmc);
			lMisuraCautelareBdmcSqlDao.start();
			lMisuraCautelareBdmcSqlDao.next();
			lCount = lMisuraCautelareBdmcSqlDao.getBigDecimal("HowManyRecords");
			lMisuraCautelareBdmcSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareBdmcController.ExGetCountMisuraCautelareBdmc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraCautelareBdmcSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aMisuraCautelareBdmc
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaMisuraCautelareBdmcPaged(MisuraCautelareBdmcModel aMisuraCautelareBdmc, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lMisuraCautelareBdmi = new Vector();
		MisuraCautelareBdmcSqlDAO lMisuraCautelareBdmcSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraCautelareBdmcSqlDao = new MisuraCautelareBdmcSqlDAO(lConn);
			lMisuraCautelareBdmcSqlDao.ricercaMisuraCautelareBdmcPaged(aMisuraCautelareBdmc, aPage);
			lMisuraCautelareBdmi = new Vector(lMisuraCautelareBdmcSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"MisuraCautelareBdmcController.ExRicercaMisuraCautelareBdmcPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraCautelareBdmcSqlDao);
			cleanup(lConn);
		}
		return lMisuraCautelareBdmi;
	}

}