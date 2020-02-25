package siap.sige.udienza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.SIGEException;
import siap.sige.aula.dao.AulaSqlDAO;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.collegio.dao.CollegioSqlDAO;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegioesperto.dao.CollegioEspertoSqlDAO;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiogiudicepopolare.dao.CollegioGiudicePopolareSqlDAO;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import siap.sige.collegiomagistrato.dao.CollegioMagistratoSqlDAO;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.magistrato.dao.MagistratoSqlDAO;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.dao.MagistratoAssegnatarioDAO;
import siap.sige.magistratoassegnatario.dao.MagistratoAssegnatarioSqlDAO;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.dao.SezioneSqlDAO;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.udienza.dao.UdienzaSigeDAO;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeSqlDAO;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;

/**
 * <p>
 * Title: UdienzaSigeController
 * </p>
 * <p>
 * Description: Classe Controller per UdienzaSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UdienzaSigeController extends GenericController implements IUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un UdienzaSige a partire dai dati contenuti nel Model
	 * 
	 * @param aUdienzaSige
	 *            Model con i dati da inserire
	 * @param aIdFascicoloSige
	 *            id del Fascicolo Sige in sessione
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public UdienzaSigeModel ExInserisciUdienzaSige(UdienzaSigeModel aUdienzaSige, BigDecimal aIdFascicoloSige)
			throws F3BException {
		Connection lConn = null;
		UdienzaSigeDAO lUdiDao = null;
		UdienzaSigeModel lUdiMod = null;
		MagistratoAssegnatarioSqlDAO lMagSqlDAO = null;
		MagistratoAssegnatarioModel lMagMod = null;
		MagistratoAssegnatarioDAO lMagAssDao = null;	

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSigeDAO(lConn);
			lUdiDao.setDAOFromModel(aUdienzaSige);
			BigDecimal lIdUdienzaSige = lUdiDao.insert();

			// Quando si inserisce/modifica il Giudice viene chiesto conferma all'utente
			// se bisogna procede anche alla modifica del Magistrato Assegnatario, impostando
			// quest'ultimo uguale al codice del Giudice scelto.
			
			// QUESTO DOVREBBE ESSERE VERO SOLO PER LE MONOCRATICHE
			if (aIdFascicoloSige != null) {
				if (aUdienzaSige.getCodMagistratoAss() != null
						&& !"".equals(aUdienzaSige.getCodMagistratoAss())) {
					lMagSqlDAO = new MagistratoAssegnatarioSqlDAO(lConn);
					lMagSqlDAO.ricercaMagistratoAssegnatarioByFascicolo(aIdFascicoloSige);
					lMagMod = (MagistratoAssegnatarioModel) lMagSqlDAO.getModelByKey();
					lMagAssDao = new MagistratoAssegnatarioDAO(lConn);
					if (lMagMod != null) {
						lMagAssDao.setMagCodMagistrato(aUdienzaSige.getCodMagistratoAss());
						lMagAssDao.setCodOperatoreAggiornamento(aUdienzaSige.getCodOperatoreAggiornamento());
						lMagAssDao.setCodUfficioAggiornamento(aUdienzaSige.getCodUfficioAggiornamento());
						lMagAssDao.setDataAggiornamento(DateUtils.getSysDate());
						lMagAssDao.setCondizioneUpdate(lMagMod.getFasSigeIdFascicoloSige(),
								lMagMod.getMagCodMagistrato());
						lMagAssDao.update();
						lMagAssDao.stop();
					}
				}
			}

			commit(lConn);
			lUdiMod = new UdienzaSigeModel(aUdienzaSige);
			lUdiMod.setIdUdienzaSige(lIdUdienzaSige);
		} catch (DAOException ex) {
			rollback(lConn);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Inserimento impossibile: Udienza già presente per giudice e data udienza! ");
			throw new F3BException("UdienzaSigeController.ExInserisci: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("UdienzaSigeController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lMagSqlDAO);
			cleanup(lMagAssDao);			
			cleanup(lConn);
		}

		return lUdiMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati UdienzaSige
	 * <p>
	 * 
	 * @param aUdienzaSige
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException {
		Connection lConn = null;
		Vector lUdienzeSige = new Vector();
		UdienzaSigeSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSigeSqlDAO(lConn);
			lUdiDao.ricercaUdienzaSige(aUdienzaSige);
			// lUdiDao.setOrderBy();
			lUdiDao.start();
			while (lUdiDao.next()) {
				lUdienzeSige.add((UdienzaSigeModel) lUdiDao.getModel());
			}
			lUdiDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSige: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lUdienzeSige;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public UdienzaSigeModel ExRicercaUdienzaSigeById(BigDecimal aIdUdienzaSige) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		Connection lConn = null;
		UdienzaSigeModel lUdienzaSigeMod = new UdienzaSigeModel();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		CollegioModel lColMod = new CollegioModel();
		CollegioSqlDAO lColDao = null;
		CollegioMagistratoSqlDAO lColMagDao = null;
		CollegioGiudicePopolareSqlDAO lColGiuPopDao = null;
		CollegioEspertoSqlDAO lColEspDao = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdienzaSigeSqlDao.ricercaUdienzaSigeByKey(aIdUdienzaSige);
			lUdienzaSigeMod = (UdienzaSigeModel) lUdienzaSigeSqlDao.getModelByKey();

			// Ticket#20190712011 — SIGE [SG]: aggiunto controllo preventivo
			if (lUdienzaSigeMod != null && Utils.isPresent(lUdienzaSigeMod.getColIdCollegio())) {
				lColDao = new CollegioSqlDAO(lConn);
				lColDao.ricercaCollegioByKey(lUdienzaSigeMod.getColIdCollegio());
				lColMod = (CollegioModel) lColDao.getModelByKey();

				// Recupero dei dati afferenti al magistrato.
				lColMagDao = new CollegioMagistratoSqlDAO(lConn);
				// lColMagDao.ricercaMagistratoByIdCollegioCodUff(lUdienzaSigeMod.getColIdCollegio(),
				// lColMod.getCodUfficioAppartenenza());
				// 20171012: [SG] nuova query di ricerca con join sulla tabella udienza_sige
				lColMagDao.ricercaMagistratoByIdCollegioCodUffIdUdienza(lUdienzaSigeMod.getColIdCollegio(),
						lColMod.getCodUfficioAppartenenza(), lUdienzaSigeMod.getIdUdienzaSige());
				Collection lColl = new ArrayList();
				lColl = lColMagDao.getModels();

				lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lColl
						.toArray(new CollegioMagistratoModel[0]));

				// Recupero dei dati afferenti al Giudice Popolare.
				lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
				lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(lUdienzaSigeMod.getColIdCollegio());
				lColl = new ArrayList();
				lColl = lColGiuPopDao.getModels();

				lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lColl
						.toArray(new CollegioGiudicePopolareModel[0]));

				// Recupero dei dati afferenti al Giudice Popolare.
				lColEspDao = new CollegioEspertoSqlDAO(lConn);
				lColEspDao.ricercaEspertoByIdCollegio(lUdienzaSigeMod.getColIdCollegio());
				lColl = new ArrayList();
				lColl = lColEspDao.getModels();

				lColMod.setCollegioEsperti((CollegioEspertoModel[]) lColl
						.toArray(new CollegioEspertoModel[0]));

				lUdienzaSigeMod.setCollegio(lColMod);
			}
			if (lUdienzaSigeMod != null && lUdienzaSigeMod.getCodIdAulaUdienza() != null) {
				AulaSqlDAO aulaDao = new AulaSqlDAO(lConn);
				aulaDao.ricercaAulaByKey(lUdienzaSigeMod.getCodIdAulaUdienza(),
						lUdienzaSigeMod.getCodIdSezioneUdienza());
				AulaUdienzaModel aulaModel = (AulaUdienzaModel) aulaDao.getModelByKey();
				lUdienzaSigeMod.setAulaUdienzaModel(aulaModel);
			}

			if (lUdienzaSigeMod != null && lUdienzaSigeMod.getCodIdSezioneUdienza() != null) {
				SezioneSqlDAO sezioneDao = new SezioneSqlDAO(lConn);
				sezioneDao.ricercaSezioneByKey(lUdienzaSigeMod.getCodIdSezioneUdienza());
				SezioneModel sezioneModel = (SezioneModel) sezioneDao.getModelByKey();
				lUdienzaSigeMod.setSezioneModel(sezioneModel);
			}

		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSigeById: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lColDao);
			cleanup(lColMagDao);
			cleanup(lColGiuPopDao);
			cleanup(lColEspDao);

			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lUdienzaSigeMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'UdienzaSige Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aUdienzaSige
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		Connection lConn = null;
		UdienzaSigeDAO lUdiDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdienzaProcSigeSqlDao = null;
		UdienzaProcedimentoSigeModel lUdiMod = null;
		MagistratoAssegnatarioSqlDAO lMagSqlDAO = null;
		MagistratoAssegnatarioModel lMagMod = null;
		MagistratoAssegnatarioDAO lMagAssDao = null;
		
		// intervento post collaudo
		CollegioModel lColMod = new CollegioModel();
		CollegioSqlDAO lColDao = null;
		
		try {
			lConn = getDBConnection();
			
			// intervento post collaudo
			if (aUdienzaSige != null && aUdienzaSige.getColIdCollegio() != null) {
				lColDao = new CollegioSqlDAO(lConn);
				lColDao.ricercaCollegioByKey(aUdienzaSige.getColIdCollegio());
				lColMod = (CollegioModel) lColDao.getModelByKey();
				
				if(lColMod!= null && lColMod.getSezIdSezione() != null){
					aUdienzaSige.setCodIdSezioneUdienza(lColMod.getSezIdSezione());
				}				
					
			}
			
			lUdiDao = new UdienzaSigeDAO(lConn);
			lUdiDao.setDAOFromModelForUpdate(aUdienzaSige);
			// lUdiDao.selCondizioneUpdate( aUdienzaSige.getIdUdienzaSige());
			lUdiDao.update();

			// Quando si inserisce/modifica il Giudice viene chiesto conferma all'utente
			// se bisogna procede anche alla modifica del Magistrato Assegnatario, impostando
			// quest'ultimo uguale al codice del Giudice scelto.
			if (aUdienzaSige.getCodMagistratoAss() != null && !aUdienzaSige.getCodMagistratoAss().equals("")) {
				lUdienzaProcSigeSqlDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdienzaProcSigeSqlDao.ricercaUdienzaProcedimentoByIdUdienzaSige(aUdienzaSige
						.getIdUdienzaSige());
				lUdiMod = (UdienzaProcedimentoSigeModel) lUdienzaProcSigeSqlDao.getModelByKey();

				if (lUdiMod != null) {
					BigDecimal idFascicoloSige = lUdiMod.getFasIdFascicoloSige();

					lMagSqlDAO = new MagistratoAssegnatarioSqlDAO(lConn);
					lMagSqlDAO.ricercaMagistratoAssegnatarioByFascicolo(idFascicoloSige);
					lMagMod = (MagistratoAssegnatarioModel) lMagSqlDAO.getModelByKey();
					lMagAssDao = new MagistratoAssegnatarioDAO(lConn);
					if (lMagMod != null) {
						lMagAssDao.setMagCodMagistrato(aUdienzaSige.getCodMagistratoAss());
						lMagAssDao.setCodOperatoreAggiornamento(aUdienzaSige.getCodOperatoreAggiornamento());
						lMagAssDao.setCodUfficioAggiornamento(aUdienzaSige.getCodUfficioAggiornamento());
						lMagAssDao.setDataAggiornamento(DateUtils.getSysDate());
						// inizio intervento per 11.2.1 
						if(aUdienzaSige.getCodProcuratore() != null)
							lMagAssDao.setCodProcuratore(aUdienzaSige.getCodProcuratore());
						if(aUdienzaSige.getCodIdAssistente()!= null)
							lMagAssDao.setCodIdAssistente(aUdienzaSige.getCodIdAssistente());						
						// fine intervento per 11.2.1
						lMagAssDao.setCondizioneUpdate(lMagMod.getFasSigeIdFascicoloSige(),
								lMagMod.getMagCodMagistrato());
						lMagAssDao.update();
						lMagAssDao.stop();
					}

				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			if (ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Inserimento impossibile: Udienza già presente per giudice e data udienza! ");
			throw new F3BException("UdienzaSigeController.ExModifica: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("UdienzaSigeController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lUdienzaProcSigeSqlDao);
			cleanup(lMagSqlDAO);
			cleanup(lMagAssDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * <p>
	 * 
	 * @param aUdienzaSige
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		Connection lConn = null;
		UdienzaSigeDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSigeDAO(lConn);
			lUdiDao.selCondizioneUpdate(aUdienzaSige.getIdUdienzaSige());
			lUdiDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Cancellazione non effettuabile, per l'udienza indicata risultano procedimenti fissati! ");
			throw new F3BException("UdienzaSigeController.ExCancellaUdienzaSige: Non posso cancellare : "
					+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("UdienzaSigeController.ExCancellaUdienzaSige: Non posso leggere : " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * <p>
	 * 
	 * @param aUdienzaSige
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdienzaSigeSqlDao.getCountUdienzaSige(aUdienzaSige);
			lUdienzaSigeSqlDao.start();
			lUdienzaSigeSqlDao.next();
			lCount = lUdienzaSigeSqlDao.getBigDecimal("HowManyRecords");
			lUdienzaSigeSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExGetCountUdienzaSige: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * <p>
	 * 
	 * @param aUdienzaSige
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaUdienzaSigePaged(UdienzaSigeModel aUdienzaSige, int aPage) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		Connection lConn = null;
		Vector lUdienzaSigi = new Vector();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdienzaSigeSqlDao.ricercaUdienzaSigePaged(aUdienzaSige, aPage);
			lUdienzaSigi = new Vector(lUdienzaSigeSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSigePaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lUdienzaSigi;
	}

	/*****************************************************************************
	 * Funzione di ricerca Udienza in base al magistrato e alla data
	 * 
	 * @param codMag
	 * @param dataUdienza
	 * @return
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaUdienzaCollegialeSige(String codMag, String dataUdienza, String codUfficioAppartenenza) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		Connection lConn = null;
		Vector lUdienzaSigi = new Vector();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdienzaSigeSqlDao.getIdUdienzaCollegiale(codMag, dataUdienza, codUfficioAppartenenza);
			lUdienzaSigeSqlDao.start();
			while (lUdienzaSigeSqlDao.next()) {
				lUdienzaSigi.add(lUdienzaSigeSqlDao.getBigDecimal("id_udienza_sige"));
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSigePaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lUdienzaSigi;
	}

	/*****************************************************************************
	 * Funzione di ricerca Udienza in base al magistrato e alla data
	 * 
	 * @param codMag
	 * @param dataUdienza
	 * @return
	 * @throws F3BException
	 ****************************************************************************/
	// [EC] - 20171019 aggiungo parametro in input
	public Vector ExRicercaUdienzaMonocraticaSige(String codMag, String dataUdienza, BigDecimal sez, String ufficioAppartenenza) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		Connection lConn = null;
		Vector lUdienzaSigi = new Vector();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			// [EC] - 20171019 aggiungo parametro in input
			// [EC] 20190325:   AGGIUNGO IL PARAMETR COD_UFFICIO IN INPUT (L'UDIENZA DEVE ESSERE UNIVOCA PER UFFICIO)
			lUdienzaSigeSqlDao.getIdUdienzaMonocratica(codMag, dataUdienza, sez, ufficioAppartenenza);
			lUdienzaSigeSqlDao.start();
			while (lUdienzaSigeSqlDao.next()) {
				lUdienzaSigi.add(lUdienzaSigeSqlDao.getBigDecimal("id_udienza_sige"));
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSigePaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lUdienzaSigi;
	}

	/**
	 * 20170914: [SG] aggiunto metodo di ricerca
	 */
	public Vector ExRicercaUdienzaCollegialeSige(String codMagis, String dataUdienza, BigDecimal idSezione,
			BigDecimal idAssistente, String idProcuratore, String modalita, String codUfficioAppartenenza) throws F3BException {

		Connection lConn = null;
		Vector lUdienzaSigi = new Vector();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdienzaSigeSqlDao.getIdUdienzaCollegiale(codMagis, dataUdienza, idSezione, idAssistente,
					idProcuratore, modalita, codUfficioAppartenenza);
			lUdienzaSigeSqlDao.start();
			while (lUdienzaSigeSqlDao.next())
				lUdienzaSigi.add(lUdienzaSigeSqlDao.getBigDecimal("id_udienza_sige"));
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaCollegialeSige(3 params): " + daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lConn);
		}
		return lUdienzaSigi;
	}

	/* (non-Javadoc)
	 * @see siap.sige.udienza.controller.IUdienzaSige#ExRicercaUdienzaSigePerFunzioniSupporto(siap.sige.udienza.model.UdienzaSigeModel)
	 */
	public Vector ExRicercaUdienzaSigePerFunzioniSupporto(UdienzaSigeModel aUdienzaSige) throws F3BException {
		Connection lConn = null;
		Vector lUdienzeSige = new Vector();
		UdienzaSigeSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaSigeSqlDAO(lConn);
			lUdiDao.ricercaUdienzaSigePerFunzioniSupporto(aUdienzaSige);			
			lUdiDao.start();
			while (lUdiDao.next()) {
				lUdienzeSige.add((UdienzaSigeModel) lUdiDao.getExtendModel());
			}
			lUdiDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSige: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lUdienzeSige;
	}
	
	
	/*****************************************************************************
	 * Funzione di ricerca Udienza in base al magistrato e alla data
	 * 
	 * @param codMag
	 * @param dataUdienza
	 * @return
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaUdienzaCollegialeSige(UdienzaSigeModel aUdienzaSige, String codUfficioAppartenenza, String proven) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		Connection lConn = null;
		Vector lUdienzaSigi = new Vector();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;
		CollegioMagistratoSqlDAO lColMagDao = null;
		CollegioGiudicePopolareSqlDAO lColGiuPopDao = null;
		CollegioEspertoSqlDAO lColEspDao = null;
		MagistratoSqlDAO lMagDAO = null;

		try {
			lConn = getDBConnection();
			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdienzaSigeSqlDao.ricercaUdienzaCollegiale(aUdienzaSige, codUfficioAppartenenza, proven);
			lUdienzaSigeSqlDao.start();
			String codPresidenteCollegio ="";			
			ArrayList<MagistratoModel> lMagistrati = new ArrayList<MagistratoModel>();
			while (lUdienzaSigeSqlDao.next()) {
				UdienzaSigeModel udienzaCollegiale = (UdienzaSigeModel) lUdienzaSigeSqlDao.getModelUdienzaCollegiale();
				
				if(udienzaCollegiale.getColIdCollegio() != null){
					CollegioModel lColMod = new CollegioModel();
					lColMod.setIdCollegio(udienzaCollegiale.getColIdCollegio());
					lColMod.setCodCollegio(udienzaCollegiale.getCollegio().getCodCollegio());
					codPresidenteCollegio = udienzaCollegiale.getCollegio().getMagCodMagistrato();
					if(!"".equals(codPresidenteCollegio)){
						// recupero i dati i dati anagrafici del magistrato firmatario dell'udienza collegial
						 lMagDAO = new MagistratoSqlDAO(lConn);
						 lMagDAO.ricercaMagistratoByCodUfficioCodMagistrato(codUfficioAppartenenza, codPresidenteCollegio);
						 lMagistrati = new ArrayList(lMagDAO.getModels());
						 if (lMagistrati.size() > 0) {
							 MagistratoModel magPRed = lMagistrati.get(0);
							 lColMod.setDescrMagistratoPresidente(magPRed.getCognome() + " " + magPRed.getNome());
						 }
					}
					if(udienzaCollegiale.getCollegio()!= null && udienzaCollegiale.getCollegio().getSezione()!= null){
						if(udienzaCollegiale.getCollegio().getSezione().getIdSezione()!=null)lColMod.setSezIdSezione(udienzaCollegiale.getCollegio().getSezione().getIdSezione());
						SezioneModel sez = udienzaCollegiale.getCollegio().getSezione();
						if(udienzaCollegiale.getCollegio().getSezione().getDescrizione()!=null){
							sez.setDescrizione(udienzaCollegiale.getCollegio().getSezione().getDescrizione());
							lColMod.setSezione(sez);
						}
					}
					// Recupero dei dati afferenti al magistrato.
					lColMagDao = new CollegioMagistratoSqlDAO(lConn);
					lColMagDao.ricercaMagistratoByIdCollegioCodUff(udienzaCollegiale.getColIdCollegio(), codUfficioAppartenenza);
					Collection lColl = new ArrayList();
					lColl = lColMagDao.getModels();
					lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lColl
							.toArray(new CollegioMagistratoModel[0]));
					// Recupero dei dati afferenti al Giudice Popolare.
					lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
					lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(udienzaCollegiale.getColIdCollegio());
					lColl = new ArrayList();
					lColl = lColGiuPopDao.getModels();
					lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lColl
							.toArray(new CollegioGiudicePopolareModel[0]));
					// Recupero dei dati afferenti al Giudice Popolare.
					lColEspDao = new CollegioEspertoSqlDAO(lConn);
					lColEspDao.ricercaEspertoByIdCollegio(udienzaCollegiale.getColIdCollegio());
					lColl = new ArrayList();
					lColl = lColEspDao.getModels();
					lColMod.setCollegioEsperti((CollegioEspertoModel[]) lColl
							.toArray(new CollegioEspertoModel[0]));
					
					udienzaCollegiale.setCollegio(lColMod);
				}
				lUdienzaSigi.add(udienzaCollegiale);
			}
			
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeController.ExRicercaUdienzaSigePaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
		return lUdienzaSigi;
	}

}