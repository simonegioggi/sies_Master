package siap.sige.collegio.controller;

import java.sql.Connection;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import siap.controller.SiapController;
import siap.sige.SIGEException;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegio.dao.CollegioDAO;
import siap.sige.collegio.dao.CollegioSqlDAO;
import siap.sige.collegioesperto.dao.CollegioEspertoDAO;
import siap.sige.collegioesperto.dao.CollegioEspertoSqlDAO;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiogiudicepopolare.dao.CollegioGiudicePopolareDAO;
import siap.sige.collegiogiudicepopolare.dao.CollegioGiudicePopolareSqlDAO;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import siap.sige.collegiomagistrato.dao.CollegioMagistratoDAO;
import siap.sige.collegiomagistrato.dao.CollegioMagistratoSqlDAO;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: CollegioController
 * </p>
 * <p>
 * Description: Classe Controller per il Collegio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
* @version 1.0
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollegioController extends SiapController implements ICollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  /**
   * Metodo che esegue l'inserimento di una sezione.
   * <p>
	 * 
	 * @param aCollegio
	 *            CollegioModel Il model con i dati da inserire.
	 * @throws F3BException
	 *             propaga errore di eccezione.
   * @return CuratoreModel ritorna il model.
   */
	public CollegioModel ExInserisciCollegio(CollegioModel aCollegio) throws F3BException {
    Connection lConn = null;
    CollegioDAO lColDao = null;
    CollegioModel lColMod = null;
    CollegioMagistratoDAO lColMagDao = null;
    CollegioGiudicePopolareDAO lColGiuPopDao = null;
    CollegioEspertoDAO lColEspertoDao = null;

		try {			
			lConn = getDBConnection();
			lColDao = new CollegioDAO(lConn);
			lColMod = new CollegioModel(aCollegio);
      
			// 20171005: [SG] inserisco solo se nuovo
//			if ("0".equals(lColMod.getCodCollegio())) {
//				lColMod.setCodCollegio("1");
		
		// 20171122: [EC] inserisco un nuovo collegio solo se IdCollegio = 0	
		// IdCollegio = 0 quando devo inserire un nuovo collegio
		if ("0".equals(lColMod.getIdCollegio().toString())) {
			lColDao.setDAOFromModel(lColMod);
			BigDecimal lKey = null;
			// lKey = lColDao.insert();
			// 20171010: [SG] gestita eccezione in modifica del collegio
			try {
				lKey = lColDao.insert();
			} catch (DAOException daoe) {
				if (daoe.UNIQUE_CONSTRAINT_VIOLATED) {
					siesLogger.info("Inserimento impossibile: Collegio già presente! --> "
							+ daoe.getMessage());
					// ricerco id_collegio
					BigDecimal idCollegioNew = ricercaCollegioBySezCodColl(lColMod.getSezIdSezione(),
							lColMod.getCodCollegio(), lColMod.getCodUfficioAppartenenza());
					lColMod.setIdCollegio(idCollegioNew);
				}
			}		
			if (Utils.isPresent(lKey))
				lColMod.setIdCollegio(lKey);
		}		
      //
      // Esegue inserimento delle relazioni Collegio Magistrati.
      //
      lColMagDao = new CollegioMagistratoDAO(lConn);
				if (lColMod.getCollegioMagistrati() != null) {
        int lSize = lColMod.getCollegioMagistrati().length;
					for (int i = 0; i < lSize; i++) {
          lColMod.getCollegioMagistrati()[i].setColIdCollegio(lColMod.getIdCollegio());
          lColMagDao.setDAOFromModel(lColMod.getCollegioMagistrati()[i]);
          lColMagDao.insert();
          lColMagDao.stop();
        }
      }
      
      //
      // Esegue inserimento delle relazioni Collegio Giudici Popolari.
      //
      lColGiuPopDao = new CollegioGiudicePopolareDAO(lConn);
				if (lColMod.getCollegioGiudiciPopolari() != null) {
        int lSize = lColMod.getCollegioGiudiciPopolari().length;
					for (int i = 0; i < lSize; i++) {
          lColMod.getCollegioGiudiciPopolari()[i].setColIdCollegio(lColMod.getIdCollegio());
          lColGiuPopDao.setDAOFromModel(lColMod.getCollegioGiudiciPopolari()[i]);
          lColGiuPopDao.insert();
          lColGiuPopDao.stop();
        }
      }
      
      //
      // Esegue inserimento delle relazioni Collegio Esperti.
      //
      lColEspertoDao = new CollegioEspertoDAO(lConn);
				if (lColMod.getCollegioEsperti() != null) {
        int lSize = lColMod.getCollegioEsperti().length;
					for (int i = 0; i < lSize; i++) {
          lColMod.getCollegioEsperti()[i].setColIdCollegio(lColMod.getIdCollegio());
          lColEspertoDao.setDAOFromModel(lColMod.getCollegioEsperti()[i]);
          lColEspertoDao.insert();
          lColEspertoDao.stop();
        }
      }

      commit(lConn);
  //  }
		} catch (DAOException daoex) {
      rollback(lConn);
			// if (daoex.UNIQUE_CONSTRAINT_VIOLATED)
			// throw new SIGEException(SIGEException.USER_MESSAGE,
			// "Inserimento impossibile: Collegio già presente! ");
      throw new F3BException("CollegioController.ExInserisciCollegio: Non posso inserire: " + daoex);
		} catch (Exception ex) {
      rollback(lConn);
      throw new F3BException("CollegioController.ExInserisciCollegio: Non posso inserire : " +  ex );
		} finally {
      cleanup(lColDao);
      cleanup(lColMagDao);
      cleanup(lColGiuPopDao);
      cleanup(lConn);
    }
    
    return lColMod;
  }

  /**
   * Metodo che esegue la ricerca di un Collegio.
   * <p>
	 * 
	 * @param aCollegio
	 *            Model popolato con i parametri necessari per la ricerca
   * @return ritorna l'insieme delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
   */
	public Vector ExRicercaCollegio(CollegioModel aCollegio) throws F3BException {
    Connection lConn = null;
    Vector lCollegi = new Vector();
    
    CollegioSqlDAO lColDao = null;
    CollegioMagistratoSqlDAO lColMagDao = null;
    CollegioGiudicePopolareSqlDAO lColGiuPopDao = null;
    CollegioEspertoSqlDAO lColEspDao = null;

		try {
      lConn = getDBConnection();
      lColDao = new CollegioSqlDAO(lConn);
      lColDao.ricercaCollegio(aCollegio);
      lCollegi = new Vector(lColDao.getModels());
      
      Iterator lItx = lCollegi.iterator();
      while( lItx.hasNext() ) {
        CollegioModel lColMod = (CollegioModel)lItx.next();
        BigDecimal lKey = lColMod.getIdCollegio();
        
        // Recupero dei dati afferenti al magistrato.
        lColMagDao = new CollegioMagistratoSqlDAO(lConn);
        lColMagDao.ricercaMagistratoByIdCollegioCodUff(lKey, lColMod.getCodUfficioAppartenenza());
				Collection lColl = new ArrayList();
				lColl = lColMagDao.getModels();
 
				lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lColl
						.toArray(new CollegioMagistratoModel[0]));

        // Recupero dei dati afferenti al Giudice Popolare.
        lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
        lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(lKey);
				lColl = new ArrayList();
				lColl = lColGiuPopDao.getModels();
 
				lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lColl
						.toArray(new CollegioGiudicePopolareModel[0]));
      
        // Recupero dei dati afferenti al Giudice Popolare.
        lColEspDao = new CollegioEspertoSqlDAO(lConn);
        lColEspDao.ricercaEspertoByIdCollegio(lKey);
				lColl = new ArrayList();
				lColl = lColEspDao.getModels();
 
				lColMod.setCollegioEsperti((CollegioEspertoModel[]) lColl
						.toArray(new CollegioEspertoModel[0]));
      }
      
      
      if ( lCollegi.size() == 0 )
        throw new F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
		} catch (DAOException daoEx) {
      throw new F3BException("CollegioController.ExRicercaCollegio: Non posso leggere : " + daoEx);
		} finally {
      cleanup(lColDao);
      cleanup(lConn);
    }

    return lCollegi;
  }

  /**
	 * Metodo che si occupa di recupera l'elenco delle sezioni per popolare elementi Combobox oppurtamente
	 * filtrati per il codice ufficio.
   * <p>
	 * 
	 * @param aCodUfficio
	 *            codice uffcio.
   * @return ritorna l'insieme di CollegioModel delle occorrenze.
	 * @throws F3BException
	 *             propaga errore di eccezione.
   */
	public Collection ExElencoCbxCollegiByCodUfficio(String aCodUfficio) throws F3BException {
    Connection lConn        = null;
    CollegioSqlDAO lColDao  = null;
    Collection lColl        = new ArrayList();

		try {
      lConn = getDBConnection();
      lColDao = new CollegioSqlDAO(lConn);
      lColDao.ricercaCollegioByCodUfficio(aCodUfficio);
      lColDao.start();

			while (lColDao.next()) {
        String lIdCollegio = lColDao.getBigDecimal("ID_SEZIONE").toString();
				String lCodCollegio = lColDao.getBigDecimal("CODICE").toString() + " - "
						+ lColDao.getString("DESCRIZIONE");
         
        lColl.add( new DecodeModel( lIdCollegio, lCodCollegio ) );
      }

      lColDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("CollegioController.ExElencoCbxCuratoriByCodUfficio: Non posso leggere : "
					+ daoEx);
		} finally {
      cleanup(lColDao);
      cleanup(lConn);
    }

    return lColl;
  }


  /**
   * Metodo che esegue la ricerca puntuale per l'id di un Collegio.
   * <p>
	 * 
	 * @param aKey
	 *            id chiave di puntamento al record.
   * @return CollegioModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
   */
	public CollegioModel ExRicercaCollegioByKey(BigDecimal aKey) throws F3BException {
    Connection lConn = null;
    CollegioSqlDAO lColDao = null;
    CollegioMagistratoSqlDAO lColMagDao = null;
    CollegioGiudicePopolareSqlDAO lColGiuPopDao = null;
    CollegioEspertoSqlDAO lColEspDao = null;
    CollegioModel lColMod;

		try {
      lConn = getDBConnection();
      lColDao = new CollegioSqlDAO(lConn);
      lColDao.ricercaCollegioByKey(aKey);
      lColMod = (CollegioModel)lColDao.getModelByKey();
      
      // Recupero dei dati afferenti al magistrato.
      lColMagDao = new CollegioMagistratoSqlDAO(lConn);
      lColMagDao.ricercaMagistratoByIdCollegioCodUff(aKey, lColMod.getCodUfficioAppartenenza());
			Collection lColl = new ArrayList();
			lColl = lColMagDao.getModels();
 
			lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lColl
					.toArray(new CollegioMagistratoModel[0]));

      // Recupero dei dati afferenti al Giudice Popolare.
      lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
      lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(aKey);
			lColl = new ArrayList();
			lColl = lColGiuPopDao.getModels();
 
			lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lColl
					.toArray(new CollegioGiudicePopolareModel[0]));
      
      // Recupero dei dati afferenti al Giudice Popolare.
      lColEspDao = new CollegioEspertoSqlDAO(lConn);
      lColEspDao.ricercaEspertoByIdCollegio(aKey);
			lColl = new ArrayList();
			lColl = lColEspDao.getModels();
 
			lColMod.setCollegioEsperti((CollegioEspertoModel[]) lColl.toArray(new CollegioEspertoModel[0]));
            
		} catch (DAOException daoEx) {
      throw new F3BException("CollegioController.ExRicercaCollegioByKey : Non posso leggere : " + daoEx);
		} finally {
      cleanup(lColDao);
      cleanup(lColMagDao);
      cleanup(lColGiuPopDao);
      cleanup(lColEspDao);
      cleanup(lConn);
    }
    return lColMod;
  }

  /**
   * Metodo che esegue la modifica dei dati di un Collegio.
   * <p>
	 * 
	 * @param aCollegio
	 *            Model Collegio.
   * @return model dell'e Collegio di ritorno.
	 * @throws F3BException
	 *             propga errore di eccezione.
   */
	public CollegioModel ExModificaCollegio(CollegioModel aCollegio) throws F3BException {
    Connection lConn = null;
    
    CollegioDAO lColDao = null;
    CollegioMagistratoDAO lColMagDao = null;
    CollegioGiudicePopolareDAO lColGiuPopDao = null;
    CollegioEspertoDAO lColEspDao = null;
    
		try {
      lConn = getDBConnection();
      lColDao = new CollegioDAO(lConn);
      lColDao.setDAOFromModelForUpdate(aCollegio);
			// 20170927: [SG] gestita eccezione in modifica del collegio
			try {
      lColDao.update();
			} catch (DAOException daoe) {
				if (daoe.getMessage().contains("COL_UFF_SEZ_DATA_UK")) {
					// throw new F3BException(F3BException.USER_MESSAGE,
					// "Impossibile modificare il collegio poichè già esistente");
					siesLogger.info("Impossibile modificare il collegio poichè già esistente: "
							+ daoe.getMessage());
					// ricerco id_collegio
					BigDecimal idCollegioNew = ricercaCollegioBySezCodColl(aCollegio.getSezIdSezione(),
							aCollegio.getCodCollegio(), aCollegio.getCodUfficioAppartenenza());
					aCollegio.setIdCollegio(idCollegioNew);
				}
			}
      
      //
      // Si esegue cancellazione delle relazioni Collegio Magistrato
      // prima di eseguire nuovo inserimento delle sezioni riassociate.
      //
      lColMagDao = new CollegioMagistratoDAO(lConn);
      lColMagDao.setCondizioneByIdCol(aCollegio.getIdCollegio());
      lColMagDao.delete();
      lColMagDao.stop();
      
      //
      // Esegue inserimento delle nuove relazioni collegio magistrati.
      //
      lColMagDao = new CollegioMagistratoDAO(lConn);
	  if (aCollegio.getCollegioMagistrati() != null) {
        int lSize = aCollegio.getCollegioMagistrati().length;
		for (int i = 0; i < lSize; i++) {
					// 2011006: [SG] aggiunto set di proprietà
					aCollegio.getCollegioMagistrati()[i].setColIdCollegio(aCollegio.getIdCollegio());
          lColMagDao.setDAOFromModel(aCollegio.getCollegioMagistrati()[i]);
          lColMagDao.insert();
          lColMagDao.stop();
        }
      }

      //
      // Si esegue cancellazione delle relazioni Collegio Giudice Popolare
      // prima di eseguire nuovo inserimento delle sezioni riassociate.
      //
      lColGiuPopDao = new CollegioGiudicePopolareDAO(lConn);
      lColGiuPopDao.setCondizioneByIdCol(aCollegio.getIdCollegio());
      lColGiuPopDao.delete();
      lColGiuPopDao.stop();
      
      //
      // Esegue inserimento delle nuove relazioni collegio giudice popolare.
      //
      lColGiuPopDao = new CollegioGiudicePopolareDAO(lConn);
			if (aCollegio.getCollegioGiudiciPopolari() != null) {
        int lSize = aCollegio.getCollegioGiudiciPopolari().length;
				for (int i = 0; i < lSize; i++) {
					// 2011006: [SG] aggiunto set di proprietà
					aCollegio.getCollegioGiudiciPopolari()[i].setColIdCollegio(aCollegio.getIdCollegio());
          lColGiuPopDao.setDAOFromModel(aCollegio.getCollegioGiudiciPopolari()[i]);
          lColGiuPopDao.insert();
          lColGiuPopDao.stop();
        }
      }
      
      //
      // Si esegue cancellazione delle relazioni Collegio Esperto
      // prima di eseguire nuovo inserimento delle sezioni riassociate.
      //
      lColEspDao = new CollegioEspertoDAO(lConn);
      lColEspDao.setCondizioneByIdCol(aCollegio.getIdCollegio());
      lColEspDao.delete();
      lColEspDao.stop();
      
      //
      // Esegue inserimento delle nuove relazioni collegio espertii.
      //
      lColEspDao = new CollegioEspertoDAO(lConn);
			if (aCollegio.getCollegioEsperti() != null) {
        int lSize = aCollegio.getCollegioEsperti().length;
				for (int i = 0; i < lSize; i++) {
					// 2011006: [SG] aggiunto set di proprietà
					aCollegio.getCollegioEsperti()[i].setColIdCollegio(aCollegio.getIdCollegio());
          lColEspDao.setDAOFromModel(aCollegio.getCollegioEsperti()[i]);
          lColEspDao.insert();
          lColEspDao.stop();
        }
      }
      
      commit(lConn);
		} catch (DAOException daoex) {
      rollback(lConn);
      throw new F3BException("CollegioController.ExModificaCollegio: Non posso modificare : " + daoex);
		} catch (Exception ex) {
      rollback(lConn);
      throw new F3BException("CollegioController.ExModificaCollegio: Non posso modificare : " + ex);
		} finally {
      cleanup(lColDao);
      cleanup(lColMagDao);
      cleanup(lColGiuPopDao);
      cleanup(lColEspDao);
      cleanup(lConn);
    }
		// valore di ritorno
		return new CollegioModel(aCollegio);
	}

	// 20171006: [SG] aggiunto metodo di ricerca
	private BigDecimal ricercaCollegioBySezCodColl(BigDecimal sezIdSezione, String codCollegio,
			String codUfficioAppartenenza) throws F3BException {

		Connection c = null;
		CollegioSqlDAO csDAO = null;
		BigDecimal idCollegio = new BigDecimal(0);

		try {
			c = getDBConnection();
			csDAO = new CollegioSqlDAO(c);
			csDAO.ricercaCollegioBySezCodColl(sezIdSezione, codCollegio, codUfficioAppartenenza);
			csDAO.start();
			if (csDAO.next())
				idCollegio = csDAO.getBigDecimal("id");
		} catch (DAOException daoEx) {
			throw new F3BException("CollegioController.ricercaCollegioBySezCodColl: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(csDAO);
			cleanup(c);
		}
		return idCollegio;
  }

  /**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un record nella tabella Collegio
	 * 
	 * @param IdCollegio
	 *            : identificatore univoco Collegio
   * @return
   * @throws F3BException
   */
	public void ExCancellaCollegio(BigDecimal IdCollegio) throws F3BException {
    Connection lConn = null;
    CollegioDAO lColDao = null;
    CollegioMagistratoDAO lColMagDao = null;
    CollegioGiudicePopolareDAO lColGiuPopDao = null;
    CollegioEspertoDAO lColEspDao = null;

		try {
      lConn = getDBConnection();
      
      // Rimozione eventuali magistrati presenti nella tabella di relazione.      
      lColMagDao = new CollegioMagistratoDAO(lConn);
      lColMagDao.setCondizioneByIdCol(IdCollegio);
      lColMagDao.delete();
      
      // Rimozione eventuali giudici popolari presenti nella tabella di relazione.      
      lColGiuPopDao = new CollegioGiudicePopolareDAO(lConn);
      lColGiuPopDao.setCondizioneByIdCol(IdCollegio);
      lColGiuPopDao.delete();

      // Rimozione eventuali esperti presenti nella tabella di relazione.
      lColEspDao = new CollegioEspertoDAO(lConn);
      lColEspDao.setCondizioneByIdCol(IdCollegio);
      lColEspDao.delete();

      // Rimozione del collegio.
      lColDao = new CollegioDAO(lConn);
      lColDao.setCondizioneUpdate(IdCollegio);
      lColDao.delete();
      
      commit(lConn);
		} catch (DAOException daoEx) {
      rollback(lConn);
      if(daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Collegio collegato ad altri dati. Impossibile effettuare la Cancellazione!");
      
      throw new F3BException("CollegioController.ExCancellaCollegio: Non posso cancellare : " + daoEx);
		} catch (Exception ex) {
      rollback(lConn);
      throw new F3BException("CollegioController.ExCancellaCollegio: Non posso cancellare : " + ex);
		} finally {
      cleanup(lColDao);
      cleanup(lColMagDao);
      cleanup(lColGiuPopDao);
      cleanup(lColEspDao);
    }
  }

  /**
	 * <p>
	 * Description: : restituisce l'elenco degli Esperti per ufficio
	 * 
	 * @param aCodUfficio
	 *            : COdice ufficio di appartenenza
   * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
   */
	public Vector ExRicercaCollegioByCodUfficio(String aCodUfficio) throws F3BException {
    Connection lConn = null;
    CollegioSqlDAO lCurDao = null;
    Vector lCurMods = null;

		try {
      lConn = getDBConnection();
      lCurDao = new CollegioSqlDAO(lConn);

      lCurDao.ricercaCollegioByCodUfficio( aCodUfficio );
      lCurMods = new Vector( lCurDao.getModels() );
		} catch (DAOException daoEx) {
			throw new F3BException("CollegioController.ExRicercaCollegioByCodUfficio: Non posso leggere : "
					+ daoEx);
		} finally {
      cleanup(lCurDao);
      cleanup(lConn);
    }

    return lCurMods;
  }

  /** 
   * Numero dei record occorsi.
   * <p>
	 * 
   * @param aCollegio
   * @return
   * @throws F3BException
   */
	public int ExGetNumRicercaCollegio(CollegioModel aCollegio) throws F3BException {
    Connection lConn = null;
    CollegioSqlDAO lCurDao = null;
    
    int lNum = 0;
    
		try {
      lConn = getDBConnection();
      lCurDao = new CollegioSqlDAO(lConn);
      lCurDao.getNumRicercaCollegio(aCollegio);
      lNum = lCurDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
      throw new F3BException("CollegioController.ExRicercaCollegio: Non posso leggere : " + daoEx);
		} finally {
      cleanup(lCurDao);
      cleanup(lConn);
    }
    return lNum;
  }

  /**
   * Metodo che esegue la ricerca puntuale di un Collegio riferito da un'udienza SIGE.
   * <p>
	 * 
	 * @param aKey
	 *            id chiave di puntamento al record.
   * @return CollegioModel ritorna il model opportunamente popolato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
   */
	public CollegioModel ExRicercaCollegioByIdUdienzaSige(BigDecimal aIdUdienzaSige) throws F3BException {
    Connection lConn = null;
    UdienzaSigeSqlDAO lUdiSqlDao = null;
    CollegioSqlDAO lColDao = null;
    CollegioMagistratoSqlDAO lColMagDao = null;
    CollegioGiudicePopolareSqlDAO lColGiuPopDao = null;
    CollegioEspertoSqlDAO lColEspDao = null;
    CollegioModel lColMod = null;
    UdienzaSigeModel lUdiSigeMod;

		try {
      lConn = getDBConnection();
      lUdiSqlDao = new UdienzaSigeSqlDAO(lConn);
      lUdiSqlDao.ricercaUdienzaSigeByKey(aIdUdienzaSige);
      lUdiSigeMod = (UdienzaSigeModel)lUdiSqlDao.getModelByKey();

			if (lUdiSigeMod.getColIdCollegio() != null) {
      	lColDao = new CollegioSqlDAO(lConn);
      	lColDao.ricercaCollegioByKey(lUdiSigeMod.getColIdCollegio());
      	lColMod = (CollegioModel)lColDao.getModelByKey();
      
      	// Recupero dei dati afferenti al magistrato.
      	lColMagDao = new CollegioMagistratoSqlDAO(lConn);
				lColMagDao.ricercaMagistratoByIdCollegioCodUff(lUdiSigeMod.getColIdCollegio(),
						lColMod.getCodUfficioAppartenenza());
				Collection lColl = new ArrayList();
				lColl = lColMagDao.getModels();
 
				lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lColl
						.toArray(new CollegioMagistratoModel[0]));

      	// Recupero dei dati afferenti al Giudice Popolare.
      	lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
      	lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(lUdiSigeMod.getColIdCollegio());
				lColl = new ArrayList();
				lColl = lColGiuPopDao.getModels();
 
				lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lColl
						.toArray(new CollegioGiudicePopolareModel[0]));
      
      	// Recupero dei dati afferenti al Giudice Popolare.
      	lColEspDao = new CollegioEspertoSqlDAO(lConn);
      	lColEspDao.ricercaEspertoByIdCollegio(lUdiSigeMod.getColIdCollegio());
				lColl = new ArrayList();
				lColl = lColEspDao.getModels();
 
				lColMod.setCollegioEsperti((CollegioEspertoModel[]) lColl
						.toArray(new CollegioEspertoModel[0]));

      }
		} catch (DAOException daoEx) {
      throw new F3BException("CollegioController.ExRicercaCollegioByIdUdienzaSige : " + daoEx);
		} finally {
      cleanup(lColDao);
      cleanup(lColMagDao);
      cleanup(lColGiuPopDao);
      cleanup(lColEspDao);
      cleanup(lUdiSqlDao);
      cleanup(lConn);
    }
    return lColMod;
  }

  /**
	 * 20170913: [SG] aggiunti parametri di passaggio
	 * 
	 * 20171127: [EC] aggiunto parametro di passaggio tipoGiudizio
	 * 
 * @param aCollegio
	 * @param dataUdienza
	 * @param codMagis
	 * @return int
 * @throws F3BException
 */
	public String ExRicercaMaxCodCollegio(CollegioModel aCollegio, Date dataUdienza, String codMagis, String tipoGiudizio, String prove )
			throws F3BException {

		// 20170913: [SG] aggiunta query di ricerca (vedi sotto), modificato da int a string
		// e ricalcolato nella pagina
		Connection lConn = null;
		CollegioSqlDAO lCurDao = null;
	//	UdienzaSigeSqlDAO ussDAO = null;
		IUdienzaSige lCtrlUdi = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel usm = null;
		String max = "";

		try {
			lConn = getDBConnection();
			lCurDao = new CollegioSqlDAO(lConn);

			// prima della ricerca del max cerco se esiste un udienza
			// con stessa data, stesso presidente e stessa sezione
			usm = new UdienzaSigeModel();
			usm.setCodMagistratoAss(codMagis);
			// 20171127: [EC] il CodGiudice è valorizzato solo per le MONOCRATICHE, pertanto in ricerca delle collegiali non va passato
			// sull'oggetto usato per la query di ricerca
			if("M".equals(tipoGiudizio))
				usm.setCodGiudice(codMagis); 
			
			if(dataUdienza!=null)
				usm.setDataUdienza(dataUdienza);
			CollegioModel cm = new CollegioModel();
			if(!"M".equals(prove))
				cm.setSezIdSezione(aCollegio.getSezIdSezione());
			cm.setIdCollegio(aCollegio.getIdCollegio());
			cm.setMagCodMagistrato(codMagis);
			usm.setCollegio(cm);
			//ussDAO = new UdienzaSigeSqlDAO(lConn);
			usm.setCodUfficioAppartenenza(aCollegio.getCodUfficioAppartenenza());
			//nb: RIVEDERE QUESTA CHIAMATA!!! SERVE??
		    //	ussDAO.ricercaUdienzaSige(usm);		
			// NUOVA RICERCA PER 11.2.1
			Vector udienze = lCtrlUdi.ExRicercaUdienzaCollegialeSige(usm, usm.getCodUfficioAppartenenza(), prove);					
			
			//usm = (UdienzaSigeModel) ussDAO.getModelByKey();
			UdienzaSigeModel udiTrovata = null;
			if (udienze.size() > 0 ){
			 udiTrovata = (UdienzaSigeModel) udienze.get(0);
		    }
			
			if ( !Utils.isNullObj(udiTrovata) && !Utils.isNullObj(udiTrovata.getCollegio())
					&& !Utils.isNullObj(udiTrovata.getCollegio().getIdCollegio())){
				max = udiTrovata.getCollegio().getCodCollegio();
				// 20171122: [EC] mi faccio restituire una stringa composta da codCollegio#IdCollegio
			    max = max +"#"+udiTrovata.getCollegio().getIdCollegio()+"#"+ "P";} // P = COLLEGIO PRESENTE
			else {
				// 20171005: [SG] aggiunto codice magistrato
				lCurDao.ricercaMaxCodCollegio(aCollegio, codMagis, DateUtils.getDateToString(dataUdienza, "dd/MM/yyyy"));
				lCurDao.start();
				if (lCurDao.next()){
					// 20171122: [EC] mi faccio restituire una stringa composta da codCollegio#IdCollegio
					max = lCurDao.getString("max")+"#"+ "P"; // P = COLLEGIO PRESENTE
				}
				else{
					// ripeto la ricerca senza magistrato solo se non ha trovato un collegio con codice magistrato
					lCurDao.ricercaMaxCodCollegio(aCollegio);
					lCurDao.start();
					if (lCurDao.next()){
						max = lCurDao.getString("max");
						int maxx = new Integer(max).intValue();						
						max = "" + (maxx + 1);
						// 20171122: [EC] mi faccio restituire una stringa composta da codCollegio#IdCollegio
						max += "#0#"+ "N"; // P = COLLEGIO NON PRESENTE
					}
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CollegioController.ExRicercaMaxCodCollegio: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lCurDao);
			cleanup(lConn);
      }

		return max;
    }

	/**
	 * 20171012: [SG] ricerca Paginata!!!
	 */
	public Vector ExRicercaCollegioPaged(CollegioModel aCollegio, int lPagina) throws F3BException {

		Connection lConn = null;
		Vector lCollegi = new Vector();

		CollegioSqlDAO lColDao = null;
		CollegioMagistratoSqlDAO lColMagDao = null;
		CollegioGiudicePopolareSqlDAO lColGiuPopDao = null;
		CollegioEspertoSqlDAO lColEspDao = null;

		try {
			lConn = getDBConnection();
			lColDao = new CollegioSqlDAO(lConn);
			lColDao.ricercaCollegioPaged(aCollegio, lPagina);
			lCollegi = new Vector(lColDao.getModels());
			Iterator lItx = lCollegi.iterator();
			while (lItx.hasNext()) {
				CollegioModel lColMod = (CollegioModel) lItx.next();
				BigDecimal lKey = lColMod.getIdCollegio();
				// Recupero dei dati afferenti al magistrato.
				lColMagDao = new CollegioMagistratoSqlDAO(lConn);
				lColMagDao.ricercaMagistratoByIdCollegioCodUff(lKey, lColMod.getCodUfficioAppartenenza());
				Collection lColl = new ArrayList();
				lColl = lColMagDao.getModels();
				lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lColl
						.toArray(new CollegioMagistratoModel[0]));
				// Recupero dei dati afferenti al Giudice Popolare.
				lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
				lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(lKey);
				lColl = new ArrayList();
				lColl = lColGiuPopDao.getModels();
				lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lColl
						.toArray(new CollegioGiudicePopolareModel[0]));
				// Recupero dei dati afferenti al Giudice Popolare.
				lColEspDao = new CollegioEspertoSqlDAO(lConn);
				lColEspDao.ricercaEspertoByIdCollegio(lKey);
				lColl = new ArrayList();
				lColl = lColEspDao.getModels();
				lColMod.setCollegioEsperti((CollegioEspertoModel[]) lColl
						.toArray(new CollegioEspertoModel[0]));
    }
			if (lCollegi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			throw new F3BException("CollegioController.ExRicercaCollegioPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lColDao);
      cleanup(lConn);
    }

		return lCollegi;
	}

	// 20171013: [SG] aggiorno la tabella collegio_magistrato col collegamento all'udienza sige
	public void ExAggiornaCollegioMagistrati(UdienzaSigeModel lUdiMod,
			CollegioMagistratoModel[] collegioMagistrati, String modo) throws F3BException {

		Connection lConn = null;
		CollegioMagistratoDAO lColMagDao = null;

		try {
			lConn = getDBConnection();
			lColMagDao = new CollegioMagistratoDAO(lConn);
			if (collegioMagistrati != null) {
				for (int i = 0; i < collegioMagistrati.length; i++) {
					String idMagistrato = ((CollegioMagistratoModel) collegioMagistrati[i]).getMagCodMagistrato();
					String sql = "update collegio_magistrato m";
					sql += " set m.udi_id_udienza_sige = '" + lUdiMod.getIdUdienzaSige() + "'";
					sql += " where m.col_id_collegio = '" + lUdiMod.getColIdCollegio() + "'";
					sql += " and m.mag_cod_magistrato = '" + idMagistrato +"'";
					if ("I".equals(modo))
						sql += " and m.udi_id_udienza_sige is null";
					lColMagDao.update(sql);
				}
  }

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			throw new F3BException("CollegioController.ExAggiornaCollegioMagistrati: Non posso modificare : "
					+ daoex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("CollegioController.ExAggiornaCollegioMagistrati: Non posso modificare : "
					+ ex);
		} finally {
			cleanup(lColMagDao);
			cleanup(lConn);
		}
	}
}