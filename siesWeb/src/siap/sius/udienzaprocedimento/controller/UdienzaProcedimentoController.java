package siap.sius.udienzaprocedimento.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoDepositoModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.XModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.notifica.dao.NotificaDAO;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
//Set di import per DepositoDecreto.
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
//Set di import per DepositoOrdinanzaPc
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.controller.ITenore;
// Set di import per il Tenore
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.dao.ProcedimentixUdienzaSqlDAO;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoDAO;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoSqlDAO;
import siap.sius.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoUdiModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Classe Controller per gestire Udienza Procedimento
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UdienzaProcedimentoController extends SiapController implements IUdienzaProcedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public UdienzaProcedimentoModel ExInserisciUdienzaProcedimento(
			UdienzaProcedimentoModel aUdienzaProcedimento) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoDAO lUdiDao = null;
		UdienzaProcedimentoModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiMod = new UdienzaProcedimentoModel(aUdienzaProcedimento);
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			lUdiDao.setDAOFromModel(aUdienzaProcedimento);
			BigDecimal lKey = null;
			lKey = lUdiDao.insert();
			commit(lConn);
			lUdiMod.setIdUdienzaProcedimento(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("UdienzaProcedimentoController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	public Vector ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza, String aOrderBy) throws F3BException {

		Connection lConn = null;
		Vector lProcedimenti = new Vector();
		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentixUdienzaByUdienza(aIdUdienza, aOrderBy);

			// lProcedimenti = new Vector(lUdiDao.getModels());

			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getModelEvento();
				lProcedimenti.add(lProcedimentoUdienza);
			}
			lUdiDao.stop();

			if (lProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	/**
	 * OverLoading del metodo ExRicercaProcedimentixUdienza con l'aggiunta di parametri discriminanti il tipo
	 * Procedimento.
	 *
	 * @param aIdUdienza
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 */
	public Vector ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc) throws F3BException {

		Connection lConn = null;
		Vector lProcedimenti = new Vector();
		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentixUdienzaByUdienza(aIdUdienza, aOrderBy, aStatoProcedimento, aTipoProc);

			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getModelEvento();
				lProcedimenti.add(lProcedimentoUdienza);
			}
			lUdiDao.stop();

			if (lProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixUdienza: " + daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lProcedimenti;
	}

	/**
	 * Metodo ExRicercaProcedimentixUdienza con l'aggiunta di parametri discriminanti il tipo Procedimento.
	 *
	 * @param aDataUdienza
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 */
	public Vector ExRicercaProcedimentixDataUdienza(Date aDataUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UdienzaProcedimentoController.ExRicercaProcedimentixDataUdienza() : inizio");

		Connection lConn = null;
		Vector lProcedimenti = new Vector();
		ProcedimentixUdienzaSqlDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new ProcedimentixUdienzaSqlDAO(lConn);
			lUdiDao.ricercaProcedimentixUdienzaByDataUdienza(aDataUdienza, aOrderBy, aStatoProcedimento,
					aTipoProc, aCodUfficioConnesso);

			lUdiDao.start();
			ProcedimentixUdienzaModel lProcedimentoUdienza = null;

			while (lUdiDao.next()) {
				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lUdiDao.getModelEvento();
				lProcedimenti.add(lProcedimentoUdienza);
			}

			lUdiDao.stop();

			if (lProcedimenti.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato!");

		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaProcedimentixDataUdienza: " + daoEx);
		} catch (F3BException f3bex) {
			throw f3bex;
		} catch (Exception ex) {
			throw new F3BException("UdienzaProcedimentoController.ExRicercaProcedimentixDataUdienza: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UdienzaProcedimentoController.ExRicercaProcedimentixDataUdienza() : fine");

		return lProcedimenti;
	}

	public Vector ExRicercaUdienzaProcedimento(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException {

		Connection lConn = null;
		Vector lUdienzaProcedimenti = new Vector();
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimento(aUdienzaProcedimento);
			lUdienzaProcedimenti = new Vector(lUdiDao.getModels());
			if (lUdienzaProcedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaUdienzaProcedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienzaProcedimenti;
	}

	/**
	 * Ricerca in udienza_procedimento per id_generale_procedimento
	 *
	 * @param aKey
	 *            idGeneraleProcedimento
	 * @return Vector di UdienzaProcedimenti
	 * @throws F3BException
	 */
	public Vector ExRicercaUdienzaProcedimentoByGeneraleProcedimento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lUdienzaProcedimenti = new Vector();
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimentoByGeneraleProcedimento(aKey);
			lUdienzaProcedimenti = new Vector(lUdiDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ricercaUdienzaProcedimentoByGeneraleProcedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdienzaProcedimenti;
	}

	/**
	 * Ricerca in udienza_procedimento e udienza per id_generale_procedimento
	 *
	 * @param aKey
	 *            idGeneraleProcedimento
	 * @return Vector di UdienzaProcedimenti
	 * @throws F3BException
	 */
	public Vector ExRicercaUdienzaProcedimentoUdiByGeneraleProcedimento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lUdiProUdiVector = new Vector();
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimentoUdienzaByGenProc(aKey);

			lUdiDao.start();
			UdienzaProcedimentoUdiModel lUdiProUdi = null;
			while (lUdiDao.next()) {
				lUdiProUdi = (UdienzaProcedimentoUdiModel) lUdiDao.getModelConUdienza();
				lUdiProUdiVector.add(lUdiProUdi);
			}
			lUdiDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaUdienzaProcedimentoUdiByGeneraleProcedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiProUdiVector;
	}

	/**
	 * Ricerca in udienza_procedimento per id_generale_procedimento il record con il valore flag_rinviata
	 * contenuto nella stringa passata
	 *
	 * @param aKey
	 *            idGeneraleProcedimento
	 * @param aFilter
	 *            Stringa composta dai valori da ricercare per Flag_Rinviata es: "'F','S'"
	 * @return UdienzaProcedimentoModel
	 * @throws F3BException
	 */
	public UdienzaProcedimentoModel ExRicercaUdienzaProcedimentoByGenProFlagRinviata(BigDecimal aKey,
			String aFilter) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		UdienzaProcedimentoModel lUdiMod;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimentoByGenProAndFlagRinviata(aKey, aFilter);
			lUdiMod = (UdienzaProcedimentoModel) lUdiDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaUdienzaProcedimentoByGenProFlagRinviata: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	public UdienzaProcedimentoModel ExRicercaUdienzaProcedimentoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		UdienzaProcedimentoModel lUdiMod;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimentoByKey(aKey);
			lUdiMod = (UdienzaProcedimentoModel) lUdiDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaUdienzaProcedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	public UdienzaProcedimentoModel ExRicercaUdienzaProcedimentoByEve(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		UdienzaProcedimentoModel lUdiMod = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiDao.ricercaUdienzaProcedimentoByEve(aKey);
			lUdiMod = (UdienzaProcedimentoModel) lUdiDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExRicercaUdienzaProcedimentoByEve: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	public UdienzaProcedimentoModel ExModificaUdienzaProcedimento(
			UdienzaProcedimentoModel aUdienzaProcedimento) throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoDAO lUdiDao = null;
		UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel(aUdienzaProcedimento);

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			lUdiDao.setDAOFromModelForUpdate(aUdienzaProcedimento);
			lUdiDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("UdienzaProcedimentoController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lUdiMod;
	}

	public void ExCancellaUdienzaProcedimento(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			lUdiDao.setCondizioneUpdate(aUdienzaProcedimento.getIdUdienzaProcedimento());
			lUdiDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UdienzaProcedimentoController.ExCancellaUdienzaProcedimento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
	}

	public void ExAggiornaFissazioneUdienza(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoDAO lUdiDao = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			lUdiDao.setDAOFromModelForUpdateFissazione(aUdienzaProcedimento);
			lUdiDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);

			throw new F3BException(
					"UdienzaProcedimentoController.ExAggiornaFissazioneUdienza: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("UdienzaProcedimentoController.ExAggiornaFissazioneUdienza: " + e);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
	}

	/**
	 * Esecuzione stampa dei procedimenti fissati per udienza.
	 *
	 * @param aModel
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProcedimentixUdienza(BigDecimal aIdUdienza, String aCodMagistrato,
			BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento, String lOrderBy,
			UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
			throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		// TreeModel lTree = this.prelevaDati( aIdUdienza, aCodMagistrato, aIdEsperto, aStampa, lOrderBy);

		TreeModel lTree = this.prelevaDati(aIdUdienza, aCodMagistrato, aIdEsperto, aStampa, lOrderBy,
				aStatoProcedimento, aTipoProc, aCodUfficioConnesso);
		// ReportGenerator lReport = new ReportGenerator();

		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lNomeTemplate = null;
		// if (aIdMagistrato == null ){
		lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdDocumento);
		// }else{
		// lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_RU_002");
		// }
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		// lReport.parseTreeXML(lTree);
		return lByteArrayOut;
	}

	public ByteArrayOutputStream ExStampaProcedimentixDataUdienza(Date aDataUdienza, String aCodMagistrato,
			BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento, String lOrderBy,
			UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UdienzaProcedimentoController.ExStampaProcedimentixDataUdienza() : inizio");

		TreeModel lTree = this.prelevaDati(aDataUdienza, aCodMagistrato, aIdEsperto, aStampa, lOrderBy,
				aStatoProcedimento, aTipoProc, aCodUfficioConnesso);

		String lNomeTemplate = null;
		lNomeTemplate = TemplateManager.getInstance().getTemplateName(aIdDocumento);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		ByteArrayOutputStream lByteArrayOut = null;

		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UdienzaProcedimentoController.ExStampaProcedimentixDataUdienza() : fine");

		return lByteArrayOut;
	}

	/**
	 * Metdodo che si occupa di prelevare i dati necessari alla stampa e quindi alla generazione della
	 * struttura dati XML. Come si noterà il metodo prende un parametro di tipo Object poichè è stato
	 * generalizzato per gestire il recupero dati sia per IDUdienza (BigDecimal) che per DataUdienza (Date).
	 */
	private TreeModel prelevaDati(Object aValue, String aCodMagistrato, BigDecimal aIdEsperto,
			XModel aStampaMod, String lOrderBy, String aStatoProcedimento, String tipoProc,
			String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UdienzaProcedimentoController.prelevaDati() : inizio");

		TreeModel lTreeGenProc = null;
		TreeModel lTreeFasSIUS = null;
		TreeModel lTreeRoot; // radice dell'albero generale del documento
		TreeModel lRootUdienza; // radice del sottoalbero a partire dal nodo Udienza
		// TreeModel lTreeTenore = null;
		// TreeModel lTreeOrdinanza = null;
		String lCodMagistrato = null;
		Vector lTenori = new Vector();

		UdienzaModel lUdienza = null;
		EventoModel lEvento;
		RiferimentoFascicoloSiusModel lRifaSius;
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();

		Vector lProcedimentiPerUdienza = new Vector();

		// Si individua il tipo di parametro passato ( BigDecimal o Date )
		if (aValue instanceof BigDecimal) {
			BigDecimal lIdUdienza = (BigDecimal) aValue;
			lProcedimentiPerUdienza = ExRicercaProcedimentixUdienza(lIdUdienza, lOrderBy, aStatoProcedimento,
					tipoProc);
			// Recupera i dati dell'udienza.
			IUdienza lCtrlUd = SIUSLookupRemote.getUdienzaRemote();
			lUdienza = lCtrlUd.ExRicercaUdienzaByKey(lIdUdienza);
		} else if (aValue instanceof Date) {
			Date lDataUdienza = (Date) aValue;
			lProcedimentiPerUdienza = ExRicercaProcedimentixDataUdienza(lDataUdienza, lOrderBy,
					aStatoProcedimento, tipoProc, aCodUfficioConnesso);
			lUdienza = new UdienzaModel();
			lUdienza.setDataUdienza(lDataUdienza);
		}

		// Ricerca Udienza
		Iterator itx = lProcedimentiPerUdienza.iterator();

		// Intestazione del documento
		lTreeRoot = new TreeModel(aStampaMod);
		lRootUdienza = new TreeModel(lUdienza);

		lCodMagistrato = (aCodMagistrato != null && aCodMagistrato.trim().length() > 1) ? aCodMagistrato
				: null;
		int i = 0;

		while (itx.hasNext()) {
			ProcedimentixUdienzaModel lProcedimento = (ProcedimentixUdienzaModel) itx.next();

			if ((lCodMagistrato == null && aIdEsperto == null)
					|| (lCodMagistrato != null && lProcedimento.getCodMagistrato() != null
							&& lProcedimento.getCodMagistrato().compareTo(lCodMagistrato) == 0)
					|| (aIdEsperto != null && lProcedimento.getIdEsperto() != null
							&& aIdEsperto.compareTo(lProcedimento.getIdEsperto()) == 0)) {

				i++; // Contatore per progressivo fascicolo

				// Preleva i dati del fascicolo
				IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMod = lCtrl.ExRicercaFascicoloByKey(lProcedimento.getIdFasSIUS());
				lTreeFasSIUS = new TreeModel(lFasGPMod.getFascicoloSiusModel());

				// Setta il progressivo del fascicolo
				lRifaSius = new RiferimentoFascicoloSiusModel();
				lRifaSius.setProgrFascicoloSius(new BigDecimal(i));
				lTreeFasSIUS.add(new TreeModel(lRifaSius));

				// Preleva dati Generale Procedimento
				// lTreeFasSIUS.add(new TreeModel(lFasGPMod.getGeneraleProcedimentoModel() )) ;

				lTreeGenProc = new TreeModel(lFasGPMod.getGeneraleProcedimentoModel());

				// TenoreSqlDAO lTenDao = null;
				// Vector lTenoriMod = new Vector();

				ITenore lCtrlTenGP = SIUSLookupRemote.getTenoreRemote();
				lTenori = lCtrlTenGP.ExRicercaTenoreByGenProc(
						lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				if (lTenori.size() != 0) {
					Iterator lItxTen = lTenori.iterator();
					while (lItxTen.hasNext())
						lTreeGenProc.add(new TreeModel((TenoreModel) lItxTen.next()));
				}

				lTreeFasSIUS.add(lTreeGenProc);
				// Preleva altri dati del fascicolo
				IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
				// Riempi l'Array contenente le tipologie di dati da prelevare
				int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_AVVOCATO,
						ICostantiStampaSius.TREE_LUOGODET };
				// Crea il TreeModel con i dati che occorrono
				lTreeFasSIUS = lCtrlSta.ExAggiungiDatiStampa(lProcedimento.getIdFasSIUS(), aTipoDati,
						lTreeFasSIUS);

				// Aggiunge i dati UdienzaProcedimento
				lTreeFasSIUS.add(new TreeModel(lProcedimento));

				// Preleva i dati dei fascicoli unificati
				Vector lVectFas = null;
				if (lFasGPMod.getFascicoloSiusModel() != null
						&& lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
						&& lFasGPMod.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
					FascicoloSiusModel lFasRicModel = new FascicoloSiusModel();
					lFasRicModel.setFasSiuIdFascicoloSius(lProcedimento.getIdFasSIUS());
					lVectFas = lCtrl.ExRicercaElencoFascicoliUnificati(lFasRicModel);
					Iterator itx4 = lVectFas.iterator();
					while (itx4.hasNext()) {
						FascicoloSiusModel lFasUnificati = (FascicoloSiusModel) itx4.next();
						lTreeFasSIUS.add(new TreeModel(lFasUnificati));
					}
				}

				// Preleva dati ultimo Evento
				if (lFasGPMod.getFascicoloSiusModel() != null) {
					IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
					lEvento = lCtrlEve.ExRicercaUltimoByFasSius(
							lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString());
					if (lEvento != null) {
						lTreeFasSIUS.add(new TreeModel(lEvento));
						// Tenori Ordinanza
						if (lEvento.getCodTipoProvvedimento() != null
								&& lEvento.getCodTipoProvvedimento().equals("02")) {
							// Lettura del Deposito Ordinanza.
							IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
							DepositoOrdinanzaPcModel llDepMod = lCtrlOrd
									.ExRicercaDepositoOrdinanzaPcByEvento(lEvento.getIdEvento());

							if (llDepMod != null && llDepMod.getIdDepositoOrdinanzaPc() != null) {
								ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
								lTenori = lCtrlTen
										.ExRicercaTenoreByOrdinanza(llDepMod.getIdDepositoOrdinanzaPc());
								if (lTenori.size() != 0) {
									Iterator lItxTen = lTenori.iterator();
									while (lItxTen.hasNext()) {
										lTreeFasSIUS.add(new TreeModel((TenoreModel) lItxTen.next()));
									}
								}
							}
						} // endif

						// Tenori Decreto
						if (lEvento.getCodTipoProvvedimento() != null
								&& lEvento.getCodTipoProvvedimento().equals("03")) {
							// Lettura del Deposito Ordinanza.
							IDepositoDecreto lCtrlDec = SIUSLookupRemote.getDepositoDecretoRemote();
							DepositoDecretoModel llDepModDec = lCtrlDec
									.ExRicercaDepositoDecretoByIdEvento(lEvento.getIdEvento());
							if (llDepModDec != null && llDepModDec.getIdDepositoDecreto() != null) {
								ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
								lTenori = lCtrlTen
										.ExRicercaTenoreByDecreto(llDepModDec.getIdDepositoDecreto());
								if (lTenori.size() != 0) {
									Iterator lItxTen = lTenori.iterator();
									while (lItxTen.hasNext()) {
										lTreeFasSIUS.add(new TreeModel((TenoreModel) lItxTen.next()));
									}
								}
							}
						} // endif

					} // end if evento

				}
				lRootUdienza.add(lTreeFasSIUS);
				lTreeRoot.add(lRootUdienza);
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UdienzaProcedimentoController.prelevaDati() : fine");

		return lTreeRoot;
	}

	/**
	 * Inserisci Pre FIssazione Udienza
	 *
	 * @param aNuovaUdienzaProc
	 *            : Udienza_Procedimento da inserire;
	 * @param aGenProc
	 *            : Generale_procedimento da aggiornare;
	 * @param aVecchiaUdienzaProc
	 *            : eventuale Udienza_Procedimento da addiornare;
	 * @return aNuovaUdienzaProc: Udienza_Procedimento inserito.
	 * @throws F3BException
	 */
	public UdienzaProcedimentoModel ExInserisciPreFissazioneUdienza(
			UdienzaProcedimentoModel aNuovaUdienzaProc, GeneraleProcedimentoModel aGenProc,
			UdienzaProcedimentoModel aVecchiaUdienzaProc) throws F3BException {

		Connection lConn = null;

		UdienzaProcedimentoDAO lUdiDao = null;
		GeneraleProcedimentoDAO lGenDAO = null;
		FascicoloSiusDAO lFasSiuDAO = null;

		try {
			// Prende una connessione in transazione.
			lConn = getDBTransaction();

			// Inserimento di un nuovo record in UDIENZA_PROCEDIMENTO.
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			lUdiDao.setDAOFromModel(aNuovaUdienzaProc);
			BigDecimal lKey = null;
			lKey = lUdiDao.insert();
			lUdiDao.stop();
			aNuovaUdienzaProc.setIdUdienzaProcedimento(lKey);

			// Eventuale aggiornamento del vecchio record in UDIENZA_PROCEDIMENTO
			if (aVecchiaUdienzaProc != null && aVecchiaUdienzaProc.getIdUdienzaProcedimento() != null) {
				lUdiDao.setDAOFromModelForUpdateFissazione(aVecchiaUdienzaProc);
				lUdiDao.update();
				lUdiDao.stop();
			}

			// Aggiornamento di GENERALE_PROCEDIMENTO
			lGenDAO = new GeneraleProcedimentoDAO(lConn);
			lGenDAO.setDataCameraConsiglio(aGenProc.getDataCameraConsiglio());
			lGenDAO.setCodUfficioAggiornamento(aGenProc.getCodUfficioAggiornamento());
			lGenDAO.setCodOperatoreAggiornamento(aGenProc.getCodOperatoreAggiornamento());
			lGenDAO.setDataAggiornamento(aGenProc.getDataAggiornamento());
			lGenDAO.setUdiIdUdienza(aNuovaUdienzaProc.getUdiIdUdienza());
			lGenDAO.setCondizioneUpdate(aGenProc.getIdGeneraleProcedimento());
			lGenDAO.update();
			lGenDAO.stop();

			// Aggiornamento COD_STATO_FASCICOLO.
			lFasSiuDAO = new FascicoloSiusDAO(lConn);
			// @since MEV_2019-09: aggiunto metodo
			String codStatoFascicolo = analsiStatoFascicolo(aGenProc.getFasSiuIdFascicoloSius());
			lFasSiuDAO.setCodStatoFascicolo(codStatoFascicolo);
			lFasSiuDAO.setDataAggiornamento(aGenProc.getDataAggiornamento());
			lFasSiuDAO.setCodOperatoreAggiornamento(aGenProc.getCodOperatoreAggiornamento());
			lFasSiuDAO.setCodUfficioAggiornamento(aGenProc.getCodUfficioAggiornamento());
			lFasSiuDAO.setCondizioneUpdate(aGenProc.getFasSiuIdFascicoloSius());
			lFasSiuDAO.update();
			lFasSiuDAO.stop();

			// FINE
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new SIUSException(
					"UdienzaProcedimentoController.ExInserisciPreFissazioneUdienza: " + daoEx);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new SIUSException("" + sqe);
		} finally {
			cleanup(lGenDAO);
			cleanup(lUdiDao);
			cleanup(lFasSiuDAO);
			cleanup(lConn);
		}
		return aNuovaUdienzaProc;
	}

	/**
	 * aggiunto metodo per impostazione stato fascicolo quando si prefissa un'udienza
	 * 
	 * @author sgioggi
	 * @since MEV_2019-09
	 * 
	 * @param 	idFascicoloSius
	 * @return 	String
	 * @throws 	F3BException
	 */
	private String analsiStatoFascicolo(BigDecimal idFascicoloSius) throws F3BException {

		String ret = ICostantiFascicoloSius.COD_ISCRITTO;
		// Ricerco evento del fascicolo:
		IEvento ie = SICOLookupRemote.getEventoRemote();
		Vector<?> v = ie.ExRicercaEventoByFascicoloSius(idFascicoloSius, null);
		// STATO_FASCICOLO 22 Emesso Decreto Designazione
		// STATO_FASCICOLO 24 Emessa Ordinanza Applicazione Provvisoria
		// ESITO_PROVVEDIMENTO 0270 Applica provvisoriamente
		// ESITO_PROVVEDIMENTO 0271 Conferma Decisione del Magistrato Relatore
		// ESITO_PROVVEDIMENTO 0610 Designa Magistrato art. 678 1-ter
		for (int i = 0; i < v.size(); i++) {
			EventoDepositoModel edm = (EventoDepositoModel) v.elementAt(i);
			if ("S".equals(edm.getFlagDocumentoRegistrato()) && edm.getNumAllValidati() > 0) {
				if ("0610".equals(edm.getCodEsito()))
					ret = ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE;
				else if ("0270".equals(edm.getCodEsito()) || "0271".equals(edm.getCodEsito()))
					// MEV_2024-092: non più utilizzato; al suo posto 07 = COD_EMESSO_PROVVEDIMENTO
					// ESITO_PROVVEDIMENTO	0271	CO		Conferma Decisione del Magistrato Relatore
					// ESITO_PROVVEDIMENTO	0270	AP		Applica ex art. 678 comma 1 ter cpp
					// ret = ICostantiFascicoloSius.COD_EMESSA_ORDINANZA_APPLICAZIONE_PROVVISORIA;
					ret = ICostantiFascicoloSius.COD_EMESSO_PROVVEDIMENTO;
				break;
			}
		}

		// valore di ritorno
		return ret;
	}

	/**
	 * Effettua l'annullamento di una udienza già fissata.
	 *
	 * @param UdienzaProcedimentoModel
	 * @throws F3BException
	 */
	public void ExCancellaFissazioneUdienza(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException {

		Connection lConn = null;
		NotificaDAO lNotDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		GeneraleProcedimentoDAO lGenDAO = null;
		DepositoDecretoModel lDepDec = null;
		UdienzaProcedimentoDAO lUdiDao = null;
		UdienzaProcedimentoSqlDAO lUdiSqlDao = null; // 15/10/2009
		IDepositoDecreto lDecrCtrl = null;
		IDepositoOrdinanzaPc lOrdCtrl = null;
		DepositoOrdinanzaPcModel lOrdinanza = null;
		EventoSqlDAO lEventoSqlDao = null; // 15/10/2009

		BigDecimal lIdEvento = null;
		BigDecimal lIdGenPro = null;

		lIdEvento = aUdienzaProcedimento.getEveIdEvento();
		if (lIdEvento == null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Evento non valorizzato nell' UDIENZA_PROCEDIMENTO");

		lIdGenPro = aUdienzaProcedimento.getGenPridGeneraleProcedimento();
		if (lIdGenPro == null)
			throw new F3BException("ID Generale Procedimento non valorizzato nell' UDIENZA_PROCEDIMENTO");

		if (lIdEvento != null) {
			// ricerca del record DEPOSITO_DECRETO da cancellare
			lDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepDec = lDecrCtrl.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
			if (lDepDec == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("record DepositoDecreto inesistente");

			// ricerca del record DEPOSITO_ORDINANZA da cancellare
			lOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lOrdinanza = lOrdCtrl.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("record DepositoOrdinanza inesistente");
		}

		try {
			lConn = getDBConnection();

			if (lIdEvento != null) {
				// Cancellazione Notifiche
				lNotDao = new NotificaDAO(lConn);
				lNotDao.setCondizioneEvento(lIdEvento);
				lNotDao.delete();
				lNotDao.stop();

				// Cancellazione Campo Note
				lCampoNotaDao = new CampoNotaDAO(lConn);
				lCampoNotaDao.setCondizioneEvento(lIdEvento);
				lCampoNotaDao.delete();
				lCampoNotaDao.stop();
			}

			/*
			 * STUB 15/10/2009 Rivista la fase di cancellazione fissazione Udienza: previsto il ripristino
			 * della precedente. // Update di GENERALE_PROCEDIMENTO lGenDAO = new
			 * GeneraleProcedimentoDAO(lConn); lGenDAO.setDataCameraConsiglio(null);
			 * lGenDAO.setUdiIdUdienza(null);
			 * lGenDAO.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento());
			 * lGenDAO.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento());
			 * lGenDAO.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento());
			 * lGenDAO.setCondizioneUpdate(lIdGenPro); lGenDAO.update(); lGenDAO.stop();
			 */
			// Update UDIENZA_PROCEDIMENTO
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			aUdienzaProcedimento.seEveIdEvento(null);
			aUdienzaProcedimento.setFlagRinviata("A");
			lUdiDao.setDAOFromModelForUpdate(aUdienzaProcedimento);
			lUdiDao.update();
			lUdiDao.stop();

			// STUB 15/10/2009 Lettura dell'ultimo Record (eventuale).
			lUdiSqlDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiSqlDao.ricercaUdienzaProcedimentoUdienzaByGenProByFlagRinviata(lIdGenPro, "'R','M'");
			lUdiSqlDao.start();

			UdienzaProcedimentoUdiModel lUdiProUdi = new UdienzaProcedimentoUdiModel();

			if (lUdiSqlDao.next())
				lUdiProUdi = (UdienzaProcedimentoUdiModel) lUdiSqlDao.getModelConUdienza();

			lUdiSqlDao.stop();

			// STUB 15/10/2009 Update di GENERALE_PROCEDIMENTO.
			lGenDAO = new GeneraleProcedimentoDAO(lConn);
			lGenDAO.setDataCameraConsiglio(lUdiProUdi.getDataUdienza());

			if (lUdiProUdi.getUdienzaProcedimento() == null)
				lGenDAO.setUdiIdUdienza(null);
			else
				lGenDAO.setUdiIdUdienza(lUdiProUdi.getUdienzaProcedimento().getUdiIdUdienza());

			lGenDAO.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento());
			lGenDAO.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento());
			lGenDAO.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento());
			lGenDAO.setCondizioneUpdate(lIdGenPro);
			lGenDAO.update();
			lGenDAO.stop();

			if (lUdiProUdi.getUdienzaProcedimento() != null) {
				EventoModel lEvento = null;
				if (lUdiProUdi.getUdienzaProcedimento().getEveIdEvento() != null) {
					// STUB 15/10/2009 Nel ripristinare la precedente occorrenza di UDIENZA_PROCEDIMENTO
					// occorre leggere l'evento collegato;
					// In caso di COD_TIPO_PROVVEDIMENTO = "02" bisogna ripristinare il FlagRinviata = "F".
					lEventoSqlDao = new EventoSqlDAO(lConn);
					// Cerca l'Evento by key
					lEventoSqlDao.ricercaEventoByKey(lUdiProUdi.getUdienzaProcedimento().getEveIdEvento());
					lEvento = (EventoModel) lEventoSqlDao.getModelByKey();
				}

				// STUB 15/10/2009 Update UDIENZA_PROCEDIMENTO.
				lUdiDao = new UdienzaProcedimentoDAO(lConn);
				if (lEvento != null) {
					if (lEvento.getCodTipoProvvedimento().compareTo("03") == 0
							|| lEvento.getCodTipoProvvedimento().compareTo("50") == 0)
						lUdiDao.setFlagRinviata("S");
					else
						lUdiDao.setFlagRinviata("F");
				} else
					lUdiDao.setFlagRinviata("P");

				lUdiDao.setUdiIdUdienzaRinvio(null);
				lUdiDao.setCondizioneUpdate(lUdiProUdi.getUdienzaProcedimento().getIdUdienzaProcedimento());
				lUdiDao.update();
				lUdiDao.stop();
			}
			// Stub 15/10/2009 Fine intervento

			if (lDepDec != null) {
				// Cancellazione Decreto ed Evento
				lDepDec.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento()); // 30/01/2008
				lDepDec.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento()); // 30/01/2008
				lDepDec.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento()); // 30/01/2008
				lDecrCtrl.ExCancellaDepositoDecreto(lDepDec, lConn);
			}

			if (lOrdinanza != null) {
				// Cancellazione Ordinanza ed Evento
				lOrdinanza.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento()); // 30/01/2008
				lOrdinanza.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento()); // 30/01/2008
				lOrdinanza.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento()); // 30/01/2008
				lOrdCtrl.ExCancellaDepositoOrdinanza(lOrdinanza, lConn);
			}

			// FINE
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Fascicoli gia' fissati per l'udienza. Impossibile effettuare la Cancellazione!");

			throw new F3BException("UdienzaController.ExCancellaFissazioneUdienza:  " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("UdienzaController.ExCancellaFissazioneUdienza: " + e);
		} finally {
			if (lIdEvento != null) {
				cleanup(lCampoNotaDao);
				cleanup(lNotDao);
			}
			cleanup(lGenDAO);
			cleanup(lUdiDao);
			cleanup(lUdiSqlDao);
			cleanup(lEventoSqlDao);

			cleanup(lConn);
		}

		return;
	}

	/**
	 * Effettua l'annullamento di una udienza già rinviata.
	 *
	 * @param aUdienzaProcedimento
	 * @param aIdFasSius
	 *            Id del fascicolo SIUS di rferimento
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaRinvioUdienza(UdienzaProcedimentoModel aUdienzaProcedimento, BigDecimal aIdFasSius)
			throws F3BException {

		Connection lConn = null;
		NotificaDAO lNotDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		GeneraleProcedimentoDAO lGenDAO = null;
		DepositoDecretoModel lDepDec = null;
		UdienzaProcedimentoDAO lUdiDao = null;
		UdienzaProcedimentoSqlDAO lUdiSqlDao = null;
		IDepositoDecreto lDecrCtrl = null;
		IDepositoOrdinanzaPc lOrdCtrl = null;
		DepositoOrdinanzaPcModel lOrdinanza = null;
		FascicoloSiusDAO lFasSiusDao = null;
		EventoDAO lEventoDao = null;
		EventoSqlDAO lEventoSqlDao = null;

		BigDecimal lIdEvento = null;
		BigDecimal lIdGenPro = null;

		lIdEvento = aUdienzaProcedimento.getEveIdEvento();

		if (lIdEvento == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"ID Evento non valorizzato nell' UDIENZA_PROCEDIMENTO");

		lIdGenPro = aUdienzaProcedimento.getGenPridGeneraleProcedimento();

		if (lIdGenPro == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"ID Generale Procedimento non valorizzato nell' UDIENZA_PROCEDIMENTO");

		if (lIdEvento != null) {
			// Ricerca del record DEPOSITO_DECRETO da cancellare
			lDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepDec = lDecrCtrl.ExRicercaDepositoDecretoByIdEvento(lIdEvento);

			if (lDepDec == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Record DepositoDecreto inesistente.");

			// Ricerca del record DEPOSITO_ORDINANZA da cancellare
			lOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lOrdinanza = lOrdCtrl.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);

			if (lOrdinanza == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Record DepositoOrdinanza inesistente.");
		}

		try {
			lConn = getDBConnection();

			lEventoSqlDao = new EventoSqlDAO(lConn);
			if (lEventoSqlDao.getNumEventiPerCheckCancRinvioUdienza(aIdFasSius, lIdEvento) != 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Non è possibile cancellare rinvio, esistono altri rinvii successivi a questo.");
			lEventoSqlDao.stop();

			if (lIdEvento != null) {
				// Cancellazione Notifiche.
				lNotDao = new NotificaDAO(lConn);
				lNotDao.setCondizioneEvento(lIdEvento);
				lNotDao.delete();
				lNotDao.stop();

				// Cancellazione Campo Note.
				lCampoNotaDao = new CampoNotaDAO(lConn);
				lCampoNotaDao.setCondizioneEvento(lIdEvento);
				lCampoNotaDao.delete();
				lCampoNotaDao.stop();
			}

			// Annullamento UDIENZA_PROCEDIMENTO.
			lUdiDao = new UdienzaProcedimentoDAO(lConn);
			aUdienzaProcedimento.seEveIdEvento(null);
			aUdienzaProcedimento.setFlagRinviata("A");
			lUdiDao.setDAOFromModelForUpdate(aUdienzaProcedimento);
			lUdiDao.update();
			lUdiDao.stop();

			// Lettura dell'ultimo Record.
			lUdiSqlDao = new UdienzaProcedimentoSqlDAO(lConn);
			lUdiSqlDao.ricercaUdienzaProcedimentoUdienzaByGenProByFlagRinviata(lIdGenPro, "'R','M'");
			lUdiSqlDao.start();

			UdienzaProcedimentoUdiModel lUdiProUdi = new UdienzaProcedimentoUdiModel();

			if (lUdiSqlDao.next())
				lUdiProUdi = (UdienzaProcedimentoUdiModel) lUdiSqlDao.getModelConUdienza();

			lUdiSqlDao.stop();
			// *_*//

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>> lUdiProUdi : " + lUdiProUdi );

			// Update di GENERALE_PROCEDIMENTO.
			lGenDAO = new GeneraleProcedimentoDAO(lConn);
			lGenDAO.setDataCameraConsiglio(lUdiProUdi.getDataUdienza());

			if (lUdiProUdi.getUdienzaProcedimento() == null)
				lGenDAO.setUdiIdUdienza(null);
			else
				lGenDAO.setUdiIdUdienza(lUdiProUdi.getUdienzaProcedimento().getUdiIdUdienza());

			lGenDAO.setCodUfficioAggiornamento(aUdienzaProcedimento.getCodUfficioAggiornamento());
			lGenDAO.setCodOperatoreAggiornamento(aUdienzaProcedimento.getCodOperatoreAggiornamento());
			lGenDAO.setDataAggiornamento(aUdienzaProcedimento.getDataAggiornamento());
			lGenDAO.setCondizioneUpdate(lIdGenPro);
			lGenDAO.update();
			lGenDAO.stop();
			// *_*//

			if (lUdiProUdi.getUdienzaProcedimento() != null) {
				// 30/09/2009 Nel ripristinare la precedente occorrenza di UDIENZA_PROCEDIMENTO occorre
				// leggere l'evento
				// collegato; in caso di COD_TIPO_PROVVEDIMENTO = "03" o "50" bisogna ripristinare il
				// FlagRinviata = "S".
				lEventoSqlDao = new EventoSqlDAO(lConn);
				EventoModel lEvento = null;
				// Cerca l'Evento by key
				lEventoSqlDao.ricercaEventoByKey(lUdiProUdi.getUdienzaProcedimento().getEveIdEvento());
				lEvento = (EventoModel) lEventoSqlDao.getModelByKey();

				// Update UDIENZA_PROCEDIMENTO.
				lUdiDao = new UdienzaProcedimentoDAO(lConn);
				if (lEvento.getCodTipoProvvedimento().compareTo("03") == 0
						|| lEvento.getCodTipoProvvedimento().compareTo("50") == 0)
					lUdiDao.setFlagRinviata("S");
				else
					lUdiDao.setFlagRinviata("F");
				lUdiDao.setUdiIdUdienzaRinvio(null);
				lUdiDao.setCondizioneUpdate(lUdiProUdi.getUdienzaProcedimento().getIdUdienzaProcedimento());
				lUdiDao.update();
				lUdiDao.stop();
			}
			// *_*//

			// Cancellazione Decreto ed Evento.
			if (lDepDec != null)
				lDecrCtrl.ExCancellaDepositoDecreto(lDepDec, lConn);

			// Cancellazione Ordinanza ed Evento.
			if (lOrdinanza != null)
				lOrdCtrl.ExCancellaDepositoOrdinanza(lOrdinanza, lConn);

			// Cancellazione solo evento
			if (lDepDec == null && lOrdinanza == null && lIdEvento != null) {
				// Istanzia EventoDAO
				lEventoDao = new EventoDAO(lConn);
				// cancellazione Evento collegato
				lEventoDao.selCondizioneUpdate(lIdEvento);
				lEventoDao.delete();
				lEventoDao.stop();
			}

			// Update dello stato del fascicolo SIUS se trattasi di nuovo ruolo
			// il fascicolo viene reimpostato con codice 02 ( iscritto ).
			if (lUdiProUdi.getUdienzaProcedimento() == null || (lUdiProUdi.getUdienzaProcedimento() != null
					&& lUdiProUdi.getUdienzaProcedimento().getFlagRinviata().equalsIgnoreCase("M"))) {
				lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setCodStatoFascicolo("02"); // Iscritto
				lFasSiusDao.setCondizioneUpdate(aIdFasSius);
				lFasSiusDao.update();
				lFasSiusDao.stop();
			}

			commit(lConn); // Fine
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Fascicoli gia' fissati per l'udienza. Impossibile effettuare la Cancellazione!");

			throw new SIUSException("UdienzaProcedimentoController.ExCancellaRinvioUdienza:  " + daoEx);
		} catch (F3BException fex) {
			rollback(lConn);
			throw fex;
		} catch (Exception e) {
			rollback(lConn);
			throw new SIUSException("UdienzaProcedimentoController.ExCancellaRinvioUdienza: " + e);
		} finally {
			if (lIdEvento != null) {
				cleanup(lCampoNotaDao);
				cleanup(lNotDao);
			}
			cleanup(lEventoSqlDao);
			cleanup(lGenDAO);
			cleanup(lUdiDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUdiSqlDao);
			cleanup(lFasSiusDao);
			cleanup(lEventoDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Udienze - Magistrati Relatori - Procedimenti per data minima, data massima dell'Udienza.
	 *
	 * @param aUdienza
	 * @return
	 * @throws F3BException
	 */
	public Collection ExRicercaUdienzeMagistratiProcedimentiByDate(UdienzaModel aUdienza)
			throws F3BException {

		Connection lConn = null;
		UdienzaProcedimentoSqlDAO lUdiDao = null;
		Collection lListaRisultato = null;

		try {
			lConn = getDBConnection();
			lUdiDao = new UdienzaProcedimentoSqlDAO(lConn);
			lListaRisultato = lUdiDao.ricercaUdienzeMagistratiProcedimenti(aUdienza);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella lettura : " + e);
		} finally {
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lListaRisultato;
	}

}