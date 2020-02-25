package siap.siep.stampadocumenti.controller;

import java.io.ByteArrayOutputStream;
//import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.SIAPReceiver;
import siap.sico.evento.model.XModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.report.ReportGenerator;
import siap.siep.SIEPException;
import siap.siep.stampadocumenti.dao.StampaDocumentiDAO;
import siap.siep.stampadocumenti.dao.StampaDocumentiSqlDAO;
import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import siap.siep.statistiche.model.RicercaFogliCompModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;


/**
* <p>Title: StampaDocumentiController</p>
* <p>Description: Classe Controller per StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaDocumentiController extends SiapController implements IStampaDocumenti
 {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un StampaDocumenti a partire dai dati contenuti nel Model
	 * 
	 * @param aStampaDocumenti
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public StampaDocumentiModel ExInserisciStampaDocumenti(StampaDocumentiModel aStampaDocumenti)
			throws F3BException {
		Connection lConn = null;
		StampaDocumentiDAO lStaDao = null;
		StampaDocumentiModel lStaMod = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StampaDocumentiDAO(lConn);
			lStaDao.setDAOFromModel(aStampaDocumenti);
			BigDecimal lSequence = lStaDao.insert();
			commit(lConn);
			lStaMod = new StampaDocumentiModel(aStampaDocumenti);
			lStaMod.setMessage("Inserimento avvenuto correttamente!");
			lStaMod.setIdStampa(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StampaDocumentiController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStaMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati StampaDocumenti
	 * 
	 * @param aStampaDocumenti
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException {
		Connection lConn = null;
		Vector lStampaDocumenti = new Vector();
		StampaDocumentiDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StampaDocumentiDAO(lConn);
			lStaDao.setCondizioni(aStampaDocumenti);
			lStaDao.setOrderBy();
			lStaDao.start();
			while (lStaDao.next()) {
				lStampaDocumenti.add((StampaDocumentiModel) lStaDao.getModel());
			}
			lStaDao.stop();

			try {
				// Provo a svegliare il listener sulla coda di stampa
				SIAPReceiver.getInstance().testStampa();
			} catch (Exception e) {
			}

		} catch (DAOException daoEx) {
			throw new F3BException("StampaDocumentiController.ExRicercaStampaDocumenti: Non posso leggere : "
					+ daoEx);
		} catch (Exception daoEx) {
			throw new F3BException("StampaDocumentiController.ExRicercaStampaDocumenti: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lStampaDocumenti;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public StampaDocumentiModel ExRicercaStampaDocumentiById(BigDecimal aIdStampa) throws F3BException {
		Connection lConn = null;
		StampaDocumentiModel lStampaDocumentiMod = new StampaDocumentiModel();
		StampaDocumentiSqlDAO lStampaDocumentiSqlDao = null;

		try {
			lConn = getDBConnection();
			lStampaDocumentiSqlDao = new StampaDocumentiSqlDAO(lConn);
			lStampaDocumentiSqlDao.ricercaStampaDocumentiByKey(aIdStampa);
			lStampaDocumentiMod = (StampaDocumentiModel) lStampaDocumentiSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StampaDocumentiController.ExRicercaStampaDocumentiById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStampaDocumentiSqlDao);
			cleanup(lConn);
		}

		return lStampaDocumentiMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'StampaDocumenti Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aStampaDocumenti
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException {
		Connection lConn = null;
		StampaDocumentiDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StampaDocumentiDAO(lConn);
			lStaDao.setDAOFromModel(aStampaDocumenti);
			lStaDao.selCondizioneUpdate(aStampaDocumenti.getIdStampa());
			lStaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("StampaDocumentiController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aStampaDocumenti
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException {
		Connection lConn = null;
		StampaDocumentiDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StampaDocumentiDAO(lConn);
			lStaDao.selCondizioneUpdate(aStampaDocumenti.getIdStampa());
			lStaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"StampaDocumentiController.ExCancellaStampaDocumenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aStampaDocumenti
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		StampaDocumentiSqlDAO lStampaDocumentiSqlDao = null;

		try {
			lConn = getDBConnection();
			lStampaDocumentiSqlDao = new StampaDocumentiSqlDAO(lConn);
			lStampaDocumentiSqlDao.getCountStampaDocumenti(aStampaDocumenti);
			lStampaDocumentiSqlDao.start();
			lStampaDocumentiSqlDao.next();
			lCount = lStampaDocumentiSqlDao.getBigDecimal("HowManyRecords");
			lStampaDocumentiSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"StampaDocumentiController.ExGetCountStampaDocumenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStampaDocumentiSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aStampaDocumenti
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaStampaDocumentiPaged(StampaDocumentiModel aStampaDocumenti, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lStampaDocumenti = new Vector();
		StampaDocumentiSqlDAO lStampaDocumentiSqlDao = null;

		try {
			lConn = getDBConnection();
			lStampaDocumentiSqlDao = new StampaDocumentiSqlDAO(lConn);
			lStampaDocumentiSqlDao.ricercaStampaDocumentiPaged(aStampaDocumenti, aPage);
			lStampaDocumenti = new Vector(lStampaDocumentiSqlDao.getModels());

			try {
				// Provo a svegliare il listener sulla coda di stampa
				SIAPReceiver.getInstance().testStampa();
			} catch (Exception e) {
				// Non rilancio l'eccezione se openjms è momentaneamente giù in quanto
				// devo visualizzare comunque l'esito della ricerca
			}

		} catch (DAOException daoEx) {
			throw new F3BException(
					"StampaDocumentiController.ExRicercaStampaDocumentiPaged: Non posso leggere : " + daoEx);
		} catch (Exception daoEx) {
			throw new F3BException(
					"StampaDocumentiController.ExRicercaStampaDocumentiPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStampaDocumentiSqlDao);
			cleanup(lConn);
		}
		return lStampaDocumenti;
	}

	/*********************************************************************
	 * Seleziona un singolo documento rtf sul DB e lo restituisce come ByteArrayOutputStream
	 * 
	 * @param aProvvedimento
	 * @return Array con il Documento recuperato dal DB
	 * @throws F3BException
	 *********************************************************************/
	public ByteArrayOutputStream ExGetDocumento(StampaDocumentiModel aEvento) throws F3BException {

		Connection lConn = null;
		StampaDocumentiDAO lStaDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
//		ByteArrayInputStream lByteArrayIn = null;
		try {
			lConn = getDBConnection();
			lStaDao = new StampaDocumentiDAO(lConn);

			lStaDao.setIdStampa(aEvento.getIdStampa());
			lStaDao.selByKey();

			lStaDao.start(1);

			if (lStaDao.next())
				lByteArrayOut = lStaDao.getDocBlob();

			lStaDao.stop();

			if (lByteArrayOut == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");

			if (lByteArrayOut.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Documento Associato");

		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	@Override
	public ByteArrayOutputStream ExPreStampaStatisticheFC(
			StatisticheFogliComplementariContainerModel container) throws F3BException {
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		UtenteModel aUtente = container.getUtenteConnesso();
		RicercaFogliCompModel aFiltroRicerca = container.getFiltro();

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = (XModel) CreateRoot(aUtente.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aFiltroRicerca));
			Vector<StatisticheFogliComplementariModel> fogli = container.getElenco();

			for (StatisticheFogliComplementariModel foglio : fogli) {
				TreeModel lTreeFogli = new TreeModel(foglio);
				lTreeRoot.add(lTreeFogli);
			}
		} catch (Exception lEx) {
			throw new SIEPException("StampaController.ExPreStampaStatisticheFC : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());
		String lIdTemplate = "SIEP_ST_001"; // Elenco Fogli Complementari Redatti

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);
		return lByteArrayOut;
	}

	/**
	 * Creazione della Root del Documento in da stampare
	 * 
	 * @param aCodiceUfficio
	 * @param aConn
	 * @return lXMod
	 * @throws F3BException
	 */
	private XModel CreateRoot(String aCodiceUfficio, Connection aConn) throws F3BException {
		UfficioSqlDAO lUDao = null;
		UfficioModel lUffMod = null;
		UfficioModel lUffCAP = null;
		XModel lXMod = null;

		try {
			lUDao = new UfficioSqlDAO(aConn);

			lUDao.selUfficioByCod(aCodiceUfficio);
			lUffMod = (UfficioModel) lUDao.getModelByKey();

			if (lUffMod == null)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Ufficio inesistente");

			// Viene istanziato l' XLM
			lXMod = new XModel();

			lXMod.setTipoUfficio(lUffMod.getCodTipoUfficio().toUpperCase());
			lXMod.setTipoUfficioT1(lUffMod.getDescrTipoUfficio().toUpperCase());
			lXMod.setUfficio(lUffMod.getDescrComune().toUpperCase());
			lXMod.setIndirizzo(lUffMod.getIndirizzo());
			lXMod.setCap(lUffMod.getCap());
			lXMod.setFax(lUffMod.getFax());
			lXMod.setTelefono(lUffMod.getTelefono());
			lXMod.setEMail(lUffMod.getEMail());
			lXMod.setDataElaborazione(DateUtils.getSysDate());

			if (lUffMod.getCodDistretto() != null) {
				lUDao.selUfficioByCod(lUffMod.getCodDistretto());
				lUffCAP = (UfficioModel) lUDao.getModelByKey();
			}
			if (lUffCAP.getDescrComune() != null)
				lXMod.setUfficioCAP(lUffCAP.getDescrComune().toUpperCase());

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIEPException("StampaSigeController.CreateRoot : " + daoEx);
		} catch (SIEPException se) {
			throw se;
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIEPException("StampaSigeController.CreateRoot : " + lEx);
		} finally {
			cleanup(lUDao);
		}

		return lXMod;
	}

}