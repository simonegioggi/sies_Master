package siap.siep.competenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.competenza.dao.CompetenzaDAO;
import siap.siep.competenza.dao.CompetenzaSqlDAO;
import siap.siep.competenza.model.CompetenzaModel;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: CompetenzaController
 * </p>
 * <p>
 * Description: Classe Controller per Competenza
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
public class CompetenzaController extends SiapController implements ICompetenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  /*****************************************************************************
	 * Effettua l'inserimento di un Competenza a partire dai dati contenuti nel Model
	 * 
	 * @param aCompetenza
	 *            Model con i dati da inserire
   * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
   * @throws F3BException
   ****************************************************************************/
  public CompetenzaModel ExInserisciCompetenza (CompetenzaModel aCompetenza ) throws F3BException {
    Connection lConn = null;
    CompetenzaDAO lComDao = null;
    CompetenzaModel lComMod = null;

    try {
      lConn = getDBConnection();
      lComDao = new CompetenzaDAO(lConn);
      lComDao.setDAOFromModel(aCompetenza );
      BigDecimal lSequence =lComDao.insert();
      commit(lConn);
      lComMod = new CompetenzaModel(aCompetenza);
      lComMod.setMessage("Inserimento avvenuto correttamente!");
      lComMod.setIdCompetenza(lSequence);
    } catch (DAOException daoEx) { 
      rollback(lConn);
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExInserisci: Non posso inserire: " + daoEx);
		} finally {
      cleanup(lComDao);  
      cleanup(lConn);  
    }

    return lComMod;
  }

  /**
   * 
   */
	public EventoNotificaModel ExInserisciRichiestaTrasmissioneAtti(EventoNotificaModel aEveNotModel,
			CompetenzaModel aCompetenzaModel) throws F3BException {
    Connection lConn = null;
    
    CompetenzaDAO lCompDao = null;
    
    EventoNotificaModel lEveNot = null;
    
    try {
      lConn = getDBConnection();
      
      // Inserimento Evento e Notifiche e CampoNote
      IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
      lEveNot = lEveCtrl.ExInserisciEventoNotifica(aEveNotModel, lConn);
      
      // Inserimento competenza
      aCompetenzaModel.setCodOperatoreInserimento (lEveNot.getEvento().getCodOperatoreInserimento());
      aCompetenzaModel.setDataInserimento         (lEveNot.getEvento().getDataInserimento());
      aCompetenzaModel.setCodUfficioInserimento   (lEveNot.getEvento().getCodUfficioInserimento());
      
      aCompetenzaModel.setEveIdEvento (lEveNot.getEvento().getIdEvento());

      
      lCompDao = new CompetenzaDAO(lConn);
      lCompDao.setDAOFromModel(aCompetenzaModel );
      BigDecimal lSequence =lCompDao.insert();
      aCompetenzaModel.setIdCompetenza(lSequence);      
      
      commit(lConn);
		} catch (DAOException ex) {
      rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
      siesLogger.error("Errore in fase di inserimento della richiesta atti",ex);
			throw new F3BException(
					"CompetenzaController.ExInserisciRichiestaTrasmissioneAtti: Non posso inserire: " + ex);
		} finally {
      cleanup(lCompDao);  
      cleanup(lConn);  
    }

    return lEveNot;
  }


  /*****************************************************************************
   * Effettua la ricerca dei dati Competenza
	 * 
	 * @param aCompetenza
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
   * @return un vettore di model con il risultato della ricerca
   * @throws F3BException
   ****************************************************************************/
  public Vector ExRicercaCompetenza (CompetenzaModel aCompetenza )  throws F3BException {
    Connection lConn = null;
    Vector lCompetenzi = new Vector();
    CompetenzaDAO lComDao = null;

    try {
      lConn = getDBConnection();
      lComDao = new CompetenzaDAO(lConn);
      lComDao.setCondizioni(aCompetenza);
      lComDao.setOrderBy();
      lComDao.start();
      while ( lComDao.next() ) { 
        lCompetenzi.add( (CompetenzaModel)lComDao.getModel() );
      }
      lComDao.stop();
    }
    catch (DAOException daoEx) {
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExRicercaCompetenza: Non posso leggere : " + daoEx);
		} finally {
      cleanup(lComDao);
      cleanup(lConn);
    }

    return lCompetenzi;
  }



  /*****************************************************************************
   * Effettua la ricerca per chiave  
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
   * @return il model con i dati trovati 
   * @throws F3BException 
   ****************************************************************************/
  public CompetenzaModel ExRicercaCompetenzaById ( BigDecimal aIdCompetenza  )  throws F3BException {
    Connection lConn = null;
    CompetenzaModel lCompetenzaMod = new CompetenzaModel();
    CompetenzaSqlDAO lCompetenzaSqlDao = null;

    try {
      lConn = getDBConnection();
      lCompetenzaSqlDao = new CompetenzaSqlDAO(lConn);
      lCompetenzaSqlDao.ricercaCompetenzaByKey( aIdCompetenza);
      lCompetenzaMod = (CompetenzaModel) lCompetenzaSqlDao.getModelByKey();
    }
    catch (DAOException daoEx) {
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExRicercaCompetenzaById: Non posso leggere : " + daoEx);
    }
    finally {
      cleanup(lCompetenzaSqlDao);
      cleanup(lConn);
    }

    return lCompetenzaMod;
  }

  
  /*****************************************************************************
   * Effettua la ricerca per ID EVENTO 
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
   * @return il model con i dati trovati 
   * @throws F3BException 
   ****************************************************************************/
  public CompetenzaModel ExRicercaCompetenzaByEveIdEvento ( BigDecimal aIdEvento  )  throws F3BException {
    Connection lConn = null;
    CompetenzaModel lCompetenzaMod = new CompetenzaModel();
    CompetenzaSqlDAO lCompetenzaSqlDao = null;

    try {
      lConn = getDBConnection();
      lCompetenzaSqlDao = new CompetenzaSqlDAO(lConn);
      lCompetenzaSqlDao.ricercaCompetenzaByEveIdEvento( aIdEvento);
      lCompetenzaMod = (CompetenzaModel) lCompetenzaSqlDao.getModelByKey();
    }
    catch (DAOException daoEx) {
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExRicercaCompetenzaById: Non posso leggere : " + daoEx);
    }
    finally {
      cleanup(lCompetenzaSqlDao);
      cleanup(lConn);
    }

    return lCompetenzaMod;
  }
  


  /*****************************************************************************
	 * Metodo che modifica i dati dell'Competenza Viene fatto l'update di tutti i campi del record recuperando
	 * i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno impostati a
	 * null
	 * 
	 * @param aCompetenza
	 *            Model con i nuovi valori
   * @throws F3BException 
   ****************************************************************************/ 
  public void ExModificaCompetenza (CompetenzaModel aCompetenza )  throws F3BException {
    Connection lConn = null;
    CompetenzaDAO lComDao = null;

    try {
      lConn = getDBConnection();
      lComDao = new CompetenzaDAO(lConn);
      lComDao.setDAOFromModel(aCompetenza );
      lComDao.selCondizioneUpdate( aCompetenza.getIdCompetenza());
      lComDao.update();
      commit(lConn);
    }
    catch (DAOException daoEx) { 
      rollback(lConn);
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExModifica: Non posso inserire: " + daoEx);
		} finally {
      cleanup(lComDao);
      cleanup(lConn);
    }
  }



  /***************************************************************************** 
   * Effettua la cancellazione del record 
	 * 
   * @param aCompetenza 
   * @throws F3BException 
   ****************************************************************************/ 
  public void ExCancellaCompetenza (CompetenzaModel aCompetenza ) throws F3BException {
    Connection lConn = null;
    CompetenzaDAO lComDao = null;

    try {
      lConn = getDBConnection();
      lComDao = new CompetenzaDAO(lConn);
      lComDao.selCondizioneUpdate( aCompetenza.getIdCompetenza());
      lComDao.delete();
      commit(lConn);
    }
    catch (DAOException daoEx) {
      siesLogger.error("DAOException",daoEx);
      rollback(lConn);
      throw new F3BException("CompetenzaController.ExCancellaCompetenza: Non posso leggere : " + daoEx);
		} finally {
      cleanup(lComDao);
      cleanup(lConn);
    }
  }



  /***************************************************************************** 
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
   * @param aCompetenza 
   * @return numero di record trovati dalla funzione dei ricerca 
   * @throws F3BException 
   ****************************************************************************/ 
  public BigDecimal ExGetCountCompetenza (CompetenzaModel aCompetenza )  throws F3BException {
    Connection lConn = null;
    BigDecimal lCount=new BigDecimal(0);
    CompetenzaSqlDAO  lCompetenzaSqlDao = null;

    try {
      lConn = getDBConnection();
      lCompetenzaSqlDao = new CompetenzaSqlDAO(lConn);
      lCompetenzaSqlDao.getCountCompetenza(aCompetenza);
      lCompetenzaSqlDao.start();
      lCompetenzaSqlDao.next();
      lCount=lCompetenzaSqlDao.getBigDecimal("HowManyRecords");
      lCompetenzaSqlDao.stop();
    }
    catch (DAOException daoEx) {
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExGetCountCompetenza: Non posso leggere : " + daoEx);
		} finally {
      cleanup(lCompetenzaSqlDao);
      cleanup(lConn);
    }

    return lCount;
  }



  /***************************************************************************** 
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aCompetenza
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
   * @return Vettore di model con i record da visualizzare nella pagina specificata in input
   * @throws F3BException 
   ****************************************************************************/ 
  public Vector <CompetenzaModel> ExRicercaCompetenzaPaged (CompetenzaModel aCompetenza,int aPage ) throws F3BException { 
    Connection lConn = null; 
    Vector <CompetenzaModel> lCompetenzi = new Vector <CompetenzaModel>(); 
    CompetenzaSqlDAO lCompetenzaSqlDao = null; 

    try {
      lConn = getDBConnection(); 
      lCompetenzaSqlDao = new CompetenzaSqlDAO(lConn); 
      lCompetenzaSqlDao.ricercaCompetenzaPaged(aCompetenza, aPage ); 
      lCompetenzi = new Vector <CompetenzaModel>(lCompetenzaSqlDao.getModels()); 
    } 
    catch (DAOException daoEx) { 
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExRicercaCompetenzaPaged: Non posso leggere : " + daoEx); 
    } 
    finally { 
      cleanup(lCompetenzaSqlDao); 
      cleanup(lConn); 
    } 
    return lCompetenzi; 
  }
  
  
  
  /*****************************************************************************
   * Effettua l'inserimento dell'EventoNotificaModel e della Competenza collegata 
   * 
	 * @param aEneNotMod
	 *            EventoNotificaModel
	 * @param aCompetenza
	 *            Model con i dati da inserire
   *  
   * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
   * @throws F3BException
   ****************************************************************************/
	public CompetenzaModel ExInserisciEventoECompetenza(EventoNotificaModel aEveNotMod,
			CompetenzaModel aCompetenza) throws F3BException {
	  
	    Connection lConn = null;
	    CompetenzaDAO lComDao = null;
	    CompetenzaModel lComMod = null;
//		EventoNotificaModel lEveRet = null;

	    try {
	    	
			lConn = getDBTransaction();
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			/*lEveRet = */lCtrl.ExInserisciEventoNotifica(aEveNotMod);
			   
			lComDao = new CompetenzaDAO(lConn);
			lComDao.setDAOFromModel(aCompetenza );
			BigDecimal lSequence =lComDao.insert();
			commit(lConn);
			
			lComMod = new CompetenzaModel(aCompetenza);
			lComMod.setMessage("Inserimento avvenuto correttamente!");
			lComMod.setIdCompetenza(lSequence);
	    }
	    catch (DAOException daoEx) { 
	      rollback(lConn);
	      siesLogger.error("DAOException",daoEx);
	      throw new F3BException("CompetenzaController.ExInserisci: Non posso inserire: " + daoEx);
		} finally {
	      cleanup(lComDao);  
	      cleanup(lConn);  
	    }

	    return lComMod;
	  }

  /**
	 * Ricerca tutti i record COMPETENZA associati ad eventi legati al fascicolo passato in input
   */
	public Vector ExRicercaCompetenzaByIdFascicoloSiep(BigDecimal aIdFascicoloSiep) throws F3BException {
    Connection lConn = null;
    Vector lCompetenze = new Vector();
    CompetenzaSqlDAO lComSqlDao = null;

    try {
      lConn = getDBConnection();
      lComSqlDao = new CompetenzaSqlDAO(lConn);
      
      lComSqlDao.ricercaCompetenzaByKeyFasc(aIdFascicoloSiep);

      lCompetenze = new Vector(lComSqlDao.getModels());
      
      lComSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
      siesLogger.error("Errore in fase di ricerca delle COMPETENZE legato al fascicolo",daoEx);

			throw new F3BException(
					"CompetenzaController.ExRicercaCompetenzaByIdFascicoloSiep: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
      siesLogger.error("Errore in fase di ricerca delle COMPETENZE legato al fascicolo",ex);

			throw new F3BException(
					"CompetenzaController.ExRicercaCompetenzaByIdFascicoloSiep: Non posso leggere : " + ex);
		} finally {
      cleanup(lComSqlDao);
      
      cleanup(lConn);
    }

    return lCompetenze;
  }

  
  
  /**
   * Inserisci i records COMPETENZA per JMS senza assegnare la sequence
	 * 
   * @param aListaCompetenze
   * @param lConn
   * @return lCodEsito
   * @throws F3BException
   */
  public String ExInserisciCompetenzeWithoutSequence (ArrayList aListaCompetenze, Connection lConn)  
			throws F3BException {
    String lCodEsito = "00000";    

    CompetenzaDAO   lCompetenzaDAO= null;    
    CompetenzaModel lCompetenzaModel = null;

		try {
      lCompetenzaDAO = new CompetenzaDAO (lConn);

			if (aListaCompetenze != null && aListaCompetenze.size() > 0) {
				for (int i = 0; i < aListaCompetenze.size(); i++) {
          lCompetenzaModel = (CompetenzaModel) aListaCompetenze.get(i);
          
          try {
            // Inserisco COMPETENZA
            lCompetenzaDAO.setDAOFromModel (lCompetenzaModel);
            lCompetenzaDAO.setWithoutSequence(true);
            lCompetenzaDAO.insert();
            lCompetenzaDAO.stop();             
          }
          catch (DAOException daoEx) {
            if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
              siesLogger.warn("Competenza gia' presente id = "+lCompetenzaModel.getIdCompetenza());
              lCodEsito = "00001";
            }
            else {
              throw daoEx;
            }            
          }
        }
      }
    }
    catch (DAOException ex)
    {
      lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
      siesLogger.error("Errore in fase di inserimento Competenza:",ex);
      throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Record Competenza! ");
    }
    finally
    {
      cleanup(lCompetenzaDAO);
    }
    return lCodEsito;
  }
  
  
  
  /****************************************************************************************
	 * Metodo che modifica il record competenza relativo alla Richiesta Atti x Competenza con l'Id del
	 * Messaggio di Trasmissione Richiesta; Viene Modificato anche l'evento con la data Trasmissione. I Model
	 * sono riepiti nella classe che richiama il metodo.
   ******************************************************************************************/ 
	public void ExModificaEvento_e_Competenza(EventoModel aEvento, CompetenzaModel aCompetenza)
			throws F3BException {
    Connection lConn = null;
    
    CompetenzaDAO lComDao = null;
//		CompetenzaSqlDAO lCompetenzaSqlDao = null;
    EventoDAO lEveDao = null;

		try {
      lConn = getDBConnection();
      
      // Evento
      lEveDao = new EventoDAO(lConn);

      lEveDao.setDAOFromModelForUpdate(aEvento);
      lEveDao.update();
      
      // Competenza
      lComDao = new CompetenzaDAO(lConn);
      
      lComDao.setDAOFromModel(aCompetenza );
      lComDao.selCondizioneUpdate( aCompetenza.getIdCompetenza());
      lComDao.update();
      
      commit(lConn);
    }
    catch (DAOException daoEx) { 
      rollback(lConn);
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExModificaEvento_e_Competenza: Non posso inserire: " + daoEx);
     }
    finally {
    	cleanup(lEveDao);
    	cleanup(lComDao);
    	cleanup(lConn);
    }
  }
  
  /*****************************************************************************
   * Effettua la ricerca per ID MESSAGGIO_RICHIESTA 
	 * 
	 * @param akey
	 *            valore ID MESSAGGIO_RICHIESTA
   * @return il model con i dati trovati 
   * @throws F3BException 
   ****************************************************************************/
	public CompetenzaModel ExRicercaCompetenzaByIdMessaggioRichiesta(BigDecimal aIdMessaggio)
			throws F3BException {
    Connection lConn = null;
    CompetenzaModel lCompetenzaMod = new CompetenzaModel();
    CompetenzaSqlDAO lCompetenzaSqlDao = null;

    try {
      lConn = getDBConnection();
      lCompetenzaSqlDao = new CompetenzaSqlDAO(lConn);
      lCompetenzaSqlDao.ricercaCompetenzaByIdMessaggioRichiesta(aIdMessaggio);
      lCompetenzaMod = (CompetenzaModel) lCompetenzaSqlDao.getModelByKey();
    }
    catch (DAOException daoEx) {
      siesLogger.error("DAOException",daoEx);
      throw new F3BException("CompetenzaController.ExRicercaCompetenzaByIdMessaggioRichiesta: Non posso leggere : " + daoEx);
    }
    finally {
      cleanup(lCompetenzaSqlDao);
      cleanup(lConn);
    }

    return lCompetenzaMod;
  }
  
  /*****************************************************************************
   * Effettua l'inserimento di Competenza e EventoNotifica a partire dai dati contenuti 
   * nel Model; 
   * Usato per Inserimeto dei provvedimenti di:
   * - atti per competenza (per emissione provvedimento cumulo)					(0340)
   * - atti per competenza (per emissione provvedimento cumulo) - Seguito Atti  (0740)
   * - Rigetto Richiesta atti per competenza (emissione Cumulo)					(0599)
   * @param aCompetenza Model, aEventoNotifica Model con i dati da inserire 
   * @return il model Competenza con i dati inseriti e l'aggiunta dell'id del record inserito
   * @throws F3BException
   ****************************************************************************/
  	public CompetenzaModel ExInserisciCompetenzaEventoNotifica (CompetenzaModel aCompetenza, EventoNotificaModel aEveNotMod ) throws F3BException
  	{
	    Connection lConn = null;
	    CompetenzaDAO lComDao = null;
	    CompetenzaModel lComMod = null;
	    
	    EventoNotificaModel lEveRet = null;

	    try 
	    {
	    	// EventoNotificaCampoMote
			lConn = getDBTransaction();
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveRet = (EventoNotificaModel)lCtrl.ExInserisciEventoNotifica(aEveNotMod, lConn);

			// Competenza
			
			lComDao = new CompetenzaDAO(lConn);
			
			aCompetenza.setCodOperatoreInserimento 	(lEveRet.getEvento().getCodOperatoreInserimento());
			aCompetenza.setDataInserimento         	(lEveRet.getEvento().getDataInserimento());
			aCompetenza.setCodUfficioInserimento   	(lEveRet.getEvento().getCodUfficioInserimento());
			aCompetenza.setEveIdEvento				(lEveRet.getEvento().getIdEvento());
			
			lComDao.setDAOFromModel(aCompetenza );
			
			BigDecimal lSequence =lComDao.insert();
			
			commit(lConn);
			lComMod = new CompetenzaModel(aCompetenza);
			lComMod.setMessage("Inserimento avvenuto correttamente!");
			lComMod.setIdCompetenza(lSequence);
	    }
	    catch (DAOException ex) 
	    { 
	    	rollback(lConn);
        siesLogger.error("DAOException: ", ex);
	    	throw new F3BException("CompetenzaController.ExInserisciCompetenzaEventoNotifica: Non posso inserire: " + ex);
	    }
	    finally
	    {
	    	cleanup(lComDao);  
	    	cleanup(lConn);  
	    }
	
	    return lComMod;
  	}
  	
  	
    /**
     * MEV_39
  	 *  Ricerca tutti i record COMPETENZA associati ad eventi di tipo Trasmessione Atti per competenza per emissione provvedimento di cumulo
  	 *  legati al fascicolo passato in input
     */
  	public Vector ExRicercaCompetenzePerCumulo(BigDecimal aIdFascicoloSiep, boolean stessoDistr) throws F3BException {
      Connection lConn = null;
      Vector lCompetenze = new Vector();
      CompetenzaSqlDAO lComSqlDao = null;

      try {
        lConn = getDBConnection();
        lComSqlDao = new CompetenzaSqlDAO(lConn);        
        lComSqlDao.ExRicercaCompetenzePerCumulo(aIdFascicoloSiep, stessoDistr);

        lComSqlDao.start();
        while ( lComSqlDao.next() ) { 
        	lCompetenze.add( (CompetenzaModel)lComSqlDao.getModelCompCumulo() );
        }       
        
        lComSqlDao.stop();
  		} catch (DAOException daoEx) {
  			siesLogger.error("Errore in fase di ricerca delle COMPETENZE legato al fascicolo",daoEx);
  			throw new F3BException(
  					"CompetenzaController.ExRicercaCompetenzePerCumulo: Non posso leggere : " + daoEx);
  		} catch (Exception ex) {
  			siesLogger.error("Errore in fase di ricerca delle COMPETENZE legato al fascicolo",ex);

  			throw new F3BException(
  					"CompetenzaController.ExRicercaCompetenzePerCumulo: Non posso leggere : " + ex);
  		} finally {
        cleanup(lComSqlDao);
        
        cleanup(lConn);
      }

      return lCompetenze;
    }
  
}
