package siap.siepe.fascicolo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.jms.messaggio.dao.MessaggioDAO;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siepe.SIEPEException;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.dao.AttivitaSqlDAO;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.fascicolo.dao.FascicoloSiepeDAO;
import siap.siepe.fascicolo.dao.FascicoloSiepeSoggSqlDAO;
import siap.siepe.fascicolo.dao.FascicoloSiepeSqlDAO;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
//import siap.siepe.fascicolo.model.FascicoloSoggAttModel;
import siap.siepe.fascicolo.model.FascicoloSiepeRicercaModel;
import siap.siepe.fascicolo.model.FascicoloSoggAttModel;
//import siap.sius.util.SIUSLookupRemote;
import siap.siepe.util.SIEPELookupRemote;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: FascicoloSiepeController
 * </p>
 * <p>
 * Description: Classe Controller per FascicoloSiepe
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
public class FascicoloSiepeController extends SiapController implements IFascicoloSiepe {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// //static Logger mLog = LogF3B.getLogger();

	public FascicoloSiepeModel ExInserisciFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe,
			MessaggioModel aMessaggio, AttivitaModel[] aListaAttivita) throws F3BException {

		Connection lConn = null;
		FascicoloSiepeModel lFasMod = null;
		FascicoloSiepeSqlDAO lFasDAO = null;
		try {
			lConn = getDBConnection();

			// Presa In Carico
			PresaInCarico(aFascicoloSiepe, aMessaggio, lConn);

			// Si ricava la CHIAVE PROGR da inserire nel Fascicolo SIEPE da inserire
			lFasDAO = new FascicoloSiepeSqlDAO(lConn);
			BigDecimal lChiaveProgr = lFasDAO.getProgressivoFascicoloSiepe(aFascicoloSiepe);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Chiave Progr->" + lChiaveProgr);
			aFascicoloSiepe.setChiaveProgr(new BigDecimal(lChiaveProgr.intValue() + 1));
			// Inserimento Fascicolo SIEPE
			lFasMod = ExInserisciFascicoloSiepe(aFascicoloSiepe, lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Fascicolo:" + aFascicoloSiepe);

			// Si istanzia il Controller dell'Attività
			IAttivita lAttCtrl = SIEPELookupRemote.getAttivitaRemote();
			// Si valorizza nelle Attività il riferimento al Fascicolo SIEPE
			for (int i = 0; i < aListaAttivita.length; i++)
				aListaAttivita[i].setFasSieIdFasSiepe(lFasMod.getIdFascicoloSiepe());
			// Inserimento Attività
			lAttCtrl.ExInserisciAttivita(aListaAttivita, lConn);

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSiepeController.ExInserisciFascicoloSiepe: " + e);
		} finally {
			cleanup(lFasDAO);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public FascicoloSiepeModel ExInserisciFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe,
			Connection aConn) throws Exception {

		FascicoloSiepeDAO lFasDao = null;
		FascicoloSiepeModel lFasMod = null;

		try {
			lFasMod = new FascicoloSiepeModel(aFascicoloSiepe);
			lFasDao = new FascicoloSiepeDAO(aConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("ExInserisciFascicoloSiepe : inizio");
			// Inserimento fel Fascicolo SIEPE
			lFasDao.setDAOFromModel(aFascicoloSiepe);
			BigDecimal lKey = null;
			lKey = lFasDao.insert();
			lFasMod.setIdFascicoloSiepe(lKey);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSiepeController.ExInserisciFascicoloSiepe: " + e);
		} finally {
			cleanup(lFasDao);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("ExInserisciFascicoloSiepe : fine");

		return lFasMod;
	}

	/**
	 * La funzione attiva la Presa In Carico richiamando la funzione specifica in un altro controller.
	 *
	 * @param aFascicoloSiepe
	 * @param aMessaggio
	 * @return
	 * @throws Exception
	 */
	private MessaggioModel PresaInCarico(FascicoloSiepeModel aFascicoloSiepe, MessaggioModel aMessaggio,
			Connection aConn) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("PresaInCarico : inizio");

		AggiornaFascicoloSiepe(aMessaggio, aFascicoloSiepe);

		// Presa in carico
		MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();

		// La Misura Alternativa serve a trasferire dei dati alla funzione di presa in carico
		lMisAlt.setCodOperatoreAggiornamento(aFascicoloSiepe.getCodOperatoreInserimento());
		lMisAlt.setCodUfficioAggiornamento(aFascicoloSiepe.getCodUfficioInserimento());
		lMisAlt.setDataAggiornamento(aFascicoloSiepe.getDataInserimento());

		// 13/12/2007 Cambiato il riferimento da SIUS.PresaInCaricoController a SICO.PresaInCaricoController.
		// IPresaInCarico lPres = SIUSLookupRemote.getPresaInCaricoSIEPE();
		IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
		if (aMessaggio.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_ORDINANZA)) {
			// Richiamo della funzione di Presa In Carico Ordinanza
			/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoOrdinanza(aMessaggio, lMisAlt, aConn);
		} else if (aMessaggio.getCodTipoOperazione().equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_DECRETO)) {
			// Richiamo della funzione di Presa In Carico Decreto
			/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoDecreto(aMessaggio, lMisAlt, aConn);
		} else if (aMessaggio.getCodTipoOperazione()
				.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO)) {
			// Richiamo della funzione di Presa In Carico Provvedimento
			/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoProvvedimento(aMessaggio, aConn);
		} else if (aMessaggio.getCodTipoOperazione()
				.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE)) {
			// Richiamo della funzione di Presa In Carico Richiesta Relazione
			/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoRichiestaRelazione(aMessaggio, aConn);
		}
		// 26/06/2007
		else if (aMessaggio.getCodTipoOperazione()
				.equalsIgnoreCase(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE)) {
			// Richiamo della funzione di Presa In Carico Richiesta UEPE
			/* MessaggioModel lMessReturn = */lPres.ExPresaInCaricoRichiestaSiepe(aMessaggio, aConn);
		} else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Presa in Carico di questo tipo di messaggio non disponibile.");

		// Flag sul messaggio ricevuto di Presa in Carico (S)
		MessaggioDAO lMesDao = new MessaggioDAO(aConn);
		lMesDao.setDataEsito(aFascicoloSiepe.getDataInserimento());
		lMesDao.setFlagVisto("S");
		lMesDao.setCondizioneUpdate(aMessaggio.getIdMessaggio());
		lMesDao.update();
		cleanup(lMesDao);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("PresaInCarico : fine");

		return aMessaggio;
	}

	/**
	 * La funzione analizza il messaggio da prendere in carico attraverso il ParseMessage per ricavare i
	 * riferimenti da aggiornare nel Fascicolo SIEPE model.
	 *
	 * @param aPars
	 * @param aFasSiepe
	 */
	private void AggiornaFascicoloSiepe(MessaggioModel aMessaggio, FascicoloSiepeModel aFasSiepe)
			throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("AggiornaFascicoloSiepe : inizio");

		ParserMessage lPars;

		if (aMessaggio.getTreeModel() != null) {
			lPars = new ParserMessage(aMessaggio.getTreeModel());

			// 26-06-2006 Viene valorizzato il riferimento al Soggetto.
			// L'IdSoggetto si recupera dalla root del treeModel nel caso trasmette un ufficio della
			// sorveglianza o della procura.
			// L'IdSoggetto si recupera dal FascicoloSiepeEstesoModel quando l'ufficio che ha trasmesso è un
			// UEPE.
			if (lPars.getSoggetto() != null) {
				aFasSiepe.setSogIdSoggetto(lPars.getSoggetto().getIdSoggetto());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID Soggetto ->" + lPars.getSoggetto().getIdSoggetto());
			} else if (lPars.getFascicoloSiepeEsteso() != null
					&& lPars.getFascicoloSiepeEsteso().getSoggetto() != null) {
				aFasSiepe.setSogIdSoggetto(lPars.getFascicoloSiepeEsteso().getSoggetto().getIdSoggetto());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(
						"ID Soggetto ->" + lPars.getFascicoloSiepeEsteso().getSoggetto().getIdSoggetto());
				// 28-06-2006 Riportati Tutti i puntamenti dall'altro UEPE.
				aFasSiepe
						.setEveIdEvento(lPars.getFascicoloSiepeEsteso().getFascicoloSiepe().getEveIdEvento());
				aFasSiepe.setFasSiuIdFascicoloSius(
						lPars.getFascicoloSiepeEsteso().getFascicoloSiepe().getFasSiuIdFascicoloSius());
				aFasSiepe.setFasSieIdFascicoloSiep(
						lPars.getFascicoloSiepeEsteso().getFascicoloSiepe().getFasSieIdFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID Evento ->"
						+ lPars.getFascicoloSiepeEsteso().getFascicoloSiepe().getFasSieIdFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID Evento ->"
						+ lPars.getFascicoloSiepeEsteso().getFascicoloSiepe().getFasSiuIdFascicoloSius());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID Evento ->"
						+ lPars.getFascicoloSiepeEsteso().getFascicoloSiepe().getEveIdEvento());
			}

			// Viene valorizzato il riferimento al Fascicolo SIEP
			if (lPars.getFascicolo() != null) {
				aFasSiepe.setFasSieIdFascicoloSiep(lPars.getFascicolo().getIdFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID FascicoloSiep ->" + lPars.getFascicolo().getIdFascicoloSiep());
			}

			// Viene valorizzato il riferimento al Fascicolo SIUS
			if (lPars.getFascicoloGPSius() != null
					&& lPars.getFascicoloGPSius().getFascicoloSiusModel() != null
					&& lPars.getFascicoloGPSius().getFascicoloSiusModel().getIdFascicoloSius() != null) {
				aFasSiepe.setFasSiuIdFascicoloSius(
						lPars.getFascicoloGPSius().getFascicoloSiusModel().getIdFascicoloSius());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID FascicoloSius ->"
						+ lPars.getFascicoloGPSius().getFascicoloSiusModel().getIdFascicoloSius());

				// Se il Soggetto è stato passato con il Fascicolo SIUS se ne ricava l'ID per aggiornare il
				// Fascicolo SIEPE
				if (lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto() != null && lPars
						.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto().getIdSoggetto() != null) {
					aFasSiepe.setSogIdSoggetto(
							lPars.getFascicoloGPSius().getFascicoloSiusModel().getSoggetto().getIdSoggetto());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("ID Soggetto ->" + lPars.getFascicoloGPSius().getFascicoloSiusModel()
							.getSoggetto().getIdSoggetto());

				}
			}

			// Viene valorizzato il riferimento all'Evento
			if (lPars.getEvento() != null && lPars.getEvento().getEvento() != null) {
				aFasSiepe.setEveIdEvento(lPars.getEvento().getEvento().getIdEvento());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("ID Evento ->" + lPars.getEvento().getEvento().getIdEvento());
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("AggiornaFascicoloSiepe : fine");

	}

	public Vector ExRicercaFascicoloSiepe(FascicoloSiepeRicercaModel aFascicoloSiepeSogg)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoloSiepe = new Vector();
		FascicoloSiepeSoggSqlDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepeSoggSqlDAO(lConn);
			lFasDao.ricercaFascicoloSiepeSoggAtt(aFascicoloSiepeSogg);
			lFascicoloSiepe = new Vector(lFasDao.getModels());
			if (lFascicoloSiepe.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSiepeController.ExRicercaFascicoloSiepe: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoloSiepe;
	}

	public FascicoloSiepeModel ExRicercaFascicoloSiepeByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FascicoloSiepeSqlDAO lFasDao = null;
		FascicoloSiepeModel lFasMod;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepeSqlDAO(lConn);
			lFasDao.ricercaFascicoloSiepeByKey(aKey);
			lFasMod = (FascicoloSiepeModel) lFasDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSiepeController.ExRicercaFascicoloSiepeByKey: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public FascicoloSiepeModel ExRicercaFascicoloByAnnoProgrCodUfficio(FascicoloSiepeModel aFasSiepe)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepeSqlDAO lFasDao = null;
		FascicoloSiepeModel lFasMod;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepeSqlDAO(lConn);
			lFasDao.ricercaFascicoloByAnnoProgrCodUfficio(aFasSiepe);
			lFasMod = (FascicoloSiepeModel) lFasDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSiepeController.ExRicercaFascicoloSiepeByAnnoProgrCodUfficio: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	/**
	 * Metodo che esegue la modifica del fascicolo.
	 * <p>
	 *
	 * @param aFascicoloSiepe
	 *            FascicoloSiepeModel Dati da aggiornare
	 * @throws F3BException
	 *             propaga errore di ecczeione.
	 * @return FascicoloSiepeModel ritorna il model con i dati aggiornati.
	 */
	public FascicoloSiepeModel ExModificaFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepeDAO lFasDao = null;
		FascicoloSiepeModel lFasMod = new FascicoloSiepeModel(aFascicoloSiepe);

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepeDAO(lConn);
			lFasDao.setDAOFromModelForUpdate(aFascicoloSiepe);
			lFasDao.update();
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoex);
			throw new SIEPEException("FascicoloSiepeController.ExModificaFascicoloSiepe: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPEException("FascicoloSiepeController.ExModificaFascicoloSiepe : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public void ExCancellaFascicoloSiepe(FascicoloSiepeModel aFascicoloSiepe) throws F3BException {

		Connection lConn = null;
		FascicoloSiepeDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiepeDAO(lConn);
			lFasDao.setCondizioneUpdate(aFascicoloSiepe.getIdFascicoloSiepe());
			lFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSiepeController.ExCancellaFascicoloSiepe: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Fascicolo Siepe paginata.
	 * <p>
	 *
	 * @param aFascicoloSiepe
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiepeModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiepePaginata(FascicoloSiepeRicercaModel aSiepeRicercaModel, int aPageNum)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiepeSoggSqlDAO lFasSoggSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasSoggSqlDao = new FascicoloSiepeSoggSqlDAO(lConn);
			// lFasSoggSqlDao.ricercaFascicoloSiusPerNumeroSius(aProgSiusModel);
			lFasSoggSqlDao.ricercaFascicoloSiepeSoggAtt(aSiepeRicercaModel);
			lFasSoggSqlDao.startPage(aPageNum);

			FascicoloSoggAttModel lFascicolo = null;
			while (lFasSoggSqlDao.next()) {
				// lFascicolo = (FascicoloGPModel) lFasSqlDao.getFascicoloSiusPerNumeroSius();
				lFascicolo = (FascicoloSoggAttModel) lFasSoggSqlDao.getModel();
				lFascicoli.add(lFascicolo);
			}
			lFasSoggSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPEException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExRicercaFascicoloSiepePaginata: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggSqlDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaFascicoloSiepPaginata
	 *
	 * @param aFascicoloSiepe
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoloSiepe(FascicoloSiepeRicercaModel aFasSiepeRicModel)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiepeSoggSqlDAO lFasSoggSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			lFasSoggSqlDao = new FascicoloSiepeSoggSqlDAO(lConn);
			lFasSoggSqlDao.ricercaFascicoloSiepeSoggAtt(aFasSiepeRicModel);
			lCont = lFasSoggSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExGetNumRicercaFascicoloSiepe: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaFascSiepeBySoggetto
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param lIncludeArchiviati
	 * @param lCodIncarico
	 * @param dataDal
	 * @param dataAl
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoliBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lIncludeArchiviati, String lCodIncarico, Date dataDal,
			Date dataAl) throws F3BException {

		Connection lConn = null;
		FascicoloSiepeSoggSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepeSoggSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lIncludeArchiviati,
					lCodIncarico, dataDal, dataAl);
			lCont = lFasSoggDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExGetNumRicercaFascicoliBySoggetto: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Fascicoli Siepe Paginata per Soggetto, e filtri aggiuntivi.
	 * <p>
	 *
	 * @param aSogModel
	 * @param lIncludeArchiviati
	 * @param lCodIncarico
	 * @param dataDal
	 * @param dataAl
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiepeModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascSiepeBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lIncludeArchiviati, String lCodIncarico, Date dataDal,
			Date dataAl, int aPageNum) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiepeSoggSqlDAO lFasSoggDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloSoggAttModel lFascicolo = null;

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiepeSoggSqlDAO(lConn);
			lFasSoggDao.ricercaFascSiepeBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lIncludeArchiviati,
					lCodIncarico, dataDal, dataAl);

			lFasSoggDao.startPage(aPageNum);
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSoggAttModel) lFasSoggDao.getTotaleFascicoliSiepePerSoggetto();

				// Si popola di tutti i dati il model del soggetto
				lSogDao = new SoggettoSqlDAO(lConn);
				lSogDao.ricercaSoggettoByKey(lFascicolo.getFascicoloSiepeRicercaModel().getSogIdSoggetto());
				aSogModel = (SoggettoModel) lSogDao.getModelByKey();

				// Si ricarica il model del soggetto nel Model SIEPE Aggregato.
				lFascicolo.setSoggettoModel(aSogModel);

				// Si Caricano i dati delle Attività nel Model SIEPE Aggregato.
				// lAttDao = new AttivitaSqlDAO(lConn);
				// lAttDao.ricercaAttivitaByFascicolo(lFascicolo.getFascicoloSiepeRicercaModel().getIdFascicoloSiepe());

				// Vector lVectAttivita = new Vector(lAttDao.getModels());
				// if( lVectAttivita != null )
				// {
				// AttivitaModel[] lAttivitaModel = (AttivitaModel[])lVectAttivita.toArray( new
				// AttivitaModel[0] );
				// lFascicolo.setAttivita(lAttivitaModel);
				// }

				lFascicoli.add(lFascicolo);
			}
			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPEException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExRicercaFascSiepeBySoggettoPagina: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			// cleanup(lAttDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli di Un Soggetto (Nel model aSogModel è valorizzato l'ID) in base ai parametridi
	 * ricerca selezionati.
	 * <p>
	 *
	 * @param aSogModel
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodUffOTrib
	 * @param strCodUffOTrib
	 * @param lCodDistretto
	 * @param lIncludeArchiviati
	 * @param lCodContenuto
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascSiepeDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String lIncludeArchiviati, String lCodIncarico, Date dataDal, Date dataAl) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiepeSoggSqlDAO lFasSoggDao = null;
		// AttivitaModel lAttMod = null;
		// AttivitaSqlDAO lAttDao = null;
		SoggettoSqlDAO lSogDao = null;
		// boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lFasSoggDao = new FascicoloSiepeSoggSqlDAO(lConn);

			lFasSoggDao.ricercaFascSiepeDelSoggetto(aSogModel, strCodUfficioUtenteConnesso,
					lIncludeArchiviati, lCodIncarico, dataDal, dataAl);
			lFasSoggDao.start();

			FascicoloSoggAttModel lFascicolo = null;
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloSoggAttModel) lFasSoggDao.getModel();

				// Si reimposta il model del soggetto nel Fascicolo Siepe.
				lFascicolo.setSoggettoModel(aSogModel);

				// Si Inseriscono le Attivita
				// lAttDao = new AttivitaSqlDAO(lConn);
				// lAttDao.ricercaAttivitaByIdFascicolo(lFascicolo.getFascicoloSiepeModel().getIdFascicoloSiepe());

				// Si Caricano i dati delle attivita nell'array di Attività in FascicoloSoggAttModel.
				// Vector lVectAttivita = new Vector(lAttDao.getModels());
				// if( lVectAttivita != null )
				// {
				// AttivitaModel[] lAttivitaModel = (AttivitaModel[])lVectAttivita.toArray( new
				// AttivitaModel[0] );
				// lFascicolo.setAttivita(lAttivitaModel);
				// }

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIEPEException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExRicercaFascicoliDelSoggetto: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lSogDao);
			// cleanup(lAttDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli SIEPE per IdFascicoloSIUS.
	 * <p>
	 *
	 * @param aIdFasSius
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliSiepePerIdFasSius(BigDecimal aIdFasSius) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiepeSqlDAO lFasSqlDao = null;
		AttivitaSqlDAO lAttDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloGPSqlDAO lFascDao = null;
		TenoreSqlDAO lTenDao = null;
		FascicoloSiepSqlDAO lFasSiepSqlDao = null;
		SentenzaSqlDAO lSenSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasSqlDao = new FascicoloSiepeSqlDAO(lConn);

			lFasSqlDao.ricercaFascicoliSiepePerIdFasSius(aIdFasSius);
			lFasSqlDao.start();

			FascicoloSiepeModel lFascicolo = null;
			FascicoloGPModel lFasGP = null;
			SoggettoModel lSoggetto = null;
			FascicoloSiepModel lFasSiep = null;
			SentenzaModel lSentMod = null;
			EventoModel lEveMod = null;

			while (lFasSqlDao.next()) {
				FascicoloSiepeEstesoModel lFascicoloEsteso = new FascicoloSiepeEstesoModel();
				lFascicolo = (FascicoloSiepeModel) lFasSqlDao.getModel();

				// Si reimposta il model del FascicoloSiepe nel Fascicolo Siepe Esteso.
				lFascicoloEsteso.setFascicoloSiepe(lFascicolo);

				// Ricerca Attività collegate
				AttivitaModel lAttivitaRicerca = new AttivitaModel();
				lAttivitaRicerca.setFasSieIdFasSiepe(lFascicolo.getIdFascicoloSiepe());
				lAttDao = new AttivitaSqlDAO(lConn);
				lAttDao.ricercaAttivita(lAttivitaRicerca);
				Vector lElencoAttivita = new Vector(lAttDao.getModels());
				lFascicoloEsteso.setElencoAttivita(lElencoAttivita);

				// Ricerca Soggetto
				if (lFascicolo.getSogIdSoggetto() != null) {
					lSogSqlDao = new SoggettoSqlDAO(lConn);
					lSogSqlDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
					lSoggetto = (SoggettoModel) lSogSqlDao.getModelByKey();
					lFascicoloEsteso.setSoggetto(lSoggetto);
				}

				// Ricerca Fascicolo SIUS
				if (lFascicolo.getFasSiuIdFascicoloSius() != null) {
					lFascDao = new FascicoloGPSqlDAO(lConn);
					lTenDao = new TenoreSqlDAO(lConn);

					lFascDao.ricercaFascicoloByKey(lFascicolo.getFasSiuIdFascicoloSius());
					lFasGP = (FascicoloGPModel) lFascDao.getModelByKey();
					if (lFasGP == null)
						throw new SIEPEException(F3BException.USER_MESSAGE,
								"Errore: Fascicolo SIUS non trovato");

					// Si Caricano i records eventuali di Tenore
					lTenDao.ricercaTenoreByGeneraleProc(
							lFasGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

					// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
					Vector lVectTenori = new Vector(lTenDao.getModels());
					if (lVectTenori != null) {
						TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
						lFasGP.setTenori(lTenoriModel);
					}
					lFasGP.getFascicoloSiusModel().setSoggetto(lSoggetto);

					lFascicoloEsteso.setFascicoloSiusGP(lFasGP);
				}

				// Ricerca Fascicolo SIEP
				if (lFascicolo.getFasSieIdFascicoloSiep() != null) {
					lFasSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
					lSenSqlDao = new SentenzaSqlDAO(lConn);

					// Cerca il fascicolo SIEP by key fascicolo
					lFasSiepSqlDao.ricercaFascicoloByKey(lFascicolo.getFasSieIdFascicoloSiep());
					lFasSiep = (FascicoloSiepModel) lFasSiepSqlDao.getModelByKey();

					if (lFasSiep == null)
						throw new SIEPEException(F3BException.USER_MESSAGE, "Fascicolo Siep non trovato");

					lFasSiep.setSoggetto(lSoggetto);

					// Cerca la sentenza associata al fascicolo
					lSenSqlDao.ricercaSentenzaBykey(lFasSiep.getSenIdSentenza());
					lSentMod = (SentenzaModel) lSenSqlDao.getModelByKey();

					if (lSentMod == null)
						throw new SIEPEException(F3BException.USER_MESSAGE, "Sentenza associata non trovata");

					lFasSiep.setSentenza(lSentMod);

					lFascicoloEsteso.setFascicoloSiep(lFasSiep);
				}
				// Ricerca Evento
				if (lFascicoloEsteso.getFascicoloSiepe().getEveIdEvento() != null) {
					lEveSqlDao = new EventoSqlDAO(lConn);
					lEveSqlDao.ricercaEventoByKey(lFascicoloEsteso.getFascicoloSiepe().getEveIdEvento());
					lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

					lFascicoloEsteso.setEvento(lEveMod);
				}

				lFascicoli.addElement(lFascicoloEsteso);
			}

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExRicercaFascicoliSiepePerIdFasSius: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lSogSqlDao);
			cleanup(lAttDao);
			cleanup(lFascDao);
			cleanup(lTenDao);
			cleanup(lFasSiepSqlDao);
			cleanup(lSenSqlDao);
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli SIEPE per IdFasSiep.
	 * <p>
	 *
	 * @param aIdFasSiep
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliSiepePerIdFasSiep(BigDecimal aIdFasSiep) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiepeSqlDAO lFasSqlDao = null;
		// AttivitaSqlDAO lAttDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloGPSqlDAO lFascDao = null;
		TenoreSqlDAO lTenDao = null;
		FascicoloSiepSqlDAO lFasSiepSqlDao = null;
		SentenzaSqlDAO lSenSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasSqlDao = new FascicoloSiepeSqlDAO(lConn);

			lFasSqlDao.ricercaFascicoliSiepePerIdFasSiep(aIdFasSiep);
			lFasSqlDao.start();

			FascicoloSiepeModel lFascicolo = null;
			FascicoloGPModel lFasGP = null;
			SoggettoModel lSoggetto = null;
			FascicoloSiepModel lFasSiep = null;
			SentenzaModel lSentMod = null;
			EventoModel lEveMod = null;

			while (lFasSqlDao.next()) {
				FascicoloSiepeEstesoModel lFascicoloEsteso = new FascicoloSiepeEstesoModel();
				lFascicolo = (FascicoloSiepeModel) lFasSqlDao.getModel();

				// Si reimposta il model del FascicoloSiepe nel Fascicolo Siepe Esteso.
				lFascicoloEsteso.setFascicoloSiepe(lFascicolo);

				// Ricerca Attività collegate
				/*
				 * AttivitaModel lAttivitaRicerca = new AttivitaModel();
				 * lAttivitaRicerca.setFasSieIdFasSiepe(lFascicolo.getIdFascicoloSiepe()); lAttDao = new
				 * AttivitaSqlDAO(lConn); lAttDao.ricercaAttivita(lAttivitaRicerca); Vector lElencoAttivita =
				 * new Vector(lAttDao.getModels()); lFascicoloEsteso.setElencoAttivita(lElencoAttivita);
				 */

				// Ricerca Soggetto
				if (lFascicolo.getSogIdSoggetto() != null) {
					lSogSqlDao = new SoggettoSqlDAO(lConn);
					lSogSqlDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
					lSoggetto = (SoggettoModel) lSogSqlDao.getModelByKey();
					lFascicoloEsteso.setSoggetto(lSoggetto);
				}

				// Ricerca Fascicolo SIUS
				if (lFascicolo.getFasSiuIdFascicoloSius() != null) {
					lFascDao = new FascicoloGPSqlDAO(lConn);
					lTenDao = new TenoreSqlDAO(lConn);

					lFascDao.ricercaFascicoloByKey(lFascicolo.getFasSiuIdFascicoloSius());
					lFasGP = (FascicoloGPModel) lFascDao.getModelByKey();
					if (lFasGP == null)
						throw new SIEPEException(F3BException.USER_MESSAGE,
								"Errore: Fascicolo SIUS non trovato");

					// Si Caricano i records eventuali di Tenore
					lTenDao.ricercaTenoreByGeneraleProc(
							lFasGP.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

					// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
					Vector lVectTenori = new Vector(lTenDao.getModels());
					if (lVectTenori != null) {
						TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
						lFasGP.setTenori(lTenoriModel);
					}
					lFasGP.getFascicoloSiusModel().setSoggetto(lSoggetto);

					lFascicoloEsteso.setFascicoloSiusGP(lFasGP);
				}

				// Ricerca Fascicolo SIEP
				if (lFascicolo.getFasSieIdFascicoloSiep() != null) {
					lFasSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
					lSenSqlDao = new SentenzaSqlDAO(lConn);

					// Cerca il fascicolo SIEP by key fascicolo
					lFasSiepSqlDao.ricercaFascicoloByKey(lFascicolo.getFasSieIdFascicoloSiep());
					lFasSiep = (FascicoloSiepModel) lFasSiepSqlDao.getModelByKey();

					if (lFasSiep == null)
						throw new SIEPEException(F3BException.USER_MESSAGE, "Fascicolo Siep non trovato");

					lFasSiep.setSoggetto(lSoggetto);

					// Cerca la sentenza associata al fascicolo
					lSenSqlDao.ricercaSentenzaBykey(lFasSiep.getSenIdSentenza());
					lSentMod = (SentenzaModel) lSenSqlDao.getModelByKey();

					if (lSentMod == null)
						throw new SIEPEException(F3BException.USER_MESSAGE, "Sentenza associata non trovata");

					lFasSiep.setSentenza(lSentMod);

					lFascicoloEsteso.setFascicoloSiep(lFasSiep);
				}

				// Ricerca Evento
				if (lFascicoloEsteso.getFascicoloSiepe().getEveIdEvento() != null) {
					lEveSqlDao = new EventoSqlDAO(lConn);
					lEveSqlDao.ricercaEventoByKey(lFascicoloEsteso.getFascicoloSiepe().getEveIdEvento());
					lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

					lFascicoloEsteso.setEvento(lEveMod);
				}

				lFascicoli.addElement(lFascicoloEsteso);
			}
		} catch (DAOException daoEx) {
			// rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExRicercaFascicoliSiepePerIdFasSius: " + daoEx);
		} catch (Exception e) {
			// rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lSogSqlDao);
			// cleanup(lAttDao);
			cleanup(lFascDao);
			cleanup(lTenDao);
			cleanup(lFasSiepSqlDao);
			cleanup(lSenSqlDao);
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}

		return lFascicoli;
	}

	public void ExInserisciDefinizioneFascicoloSiepe(FascicoloSiepeModel aFasMod) throws F3BException {

		Connection lConn = null;

		FascicoloSiepeDAO lFasDao = null;
		FascicoloSiepeModel lFasc = aFasMod;

		if (lFasc == null || lFasc.getIdFascicoloSiepe() == null)
			throw new SIEPEException(F3BException.USER_MESSAGE, "Dati Fascicolo non definiti !");

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("####### Contenuto di FasSiepeModel : " + lFasc);

			lConn = getDBConnection();
			// Update del FASCICOLO_SIEPE
			lFasDao = new FascicoloSiepeDAO(lConn);
			lFasDao.setCodOperatoreAggiornamento(lFasc.getCodOperatoreAggiornamento());
			lFasDao.setCodUfficioAggiornamento(lFasc.getCodUfficioAggiornamento());
			lFasDao.setDataAggiornamento(lFasc.getDataAggiornamento());
			lFasDao.setCodStatoFascicolo(lFasc.getCodStatoFascicolo());
			lFasDao.setTipoDefinizione(lFasc.getTipoDefinizione());
			lFasDao.setDataDefinizione(lFasc.getDataDefinizione());
			lFasDao.setDescrDefinizione(lFasc.getDescrDefinizione());
			lFasDao.setCondizioneUpdate(lFasc.getIdFascicoloSiepe());

			lFasDao.update();
			lFasDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExInserisciDefinizioneFascicoloSius : " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIEPEException(F3BException.USER_MESSAGE,
					"FascicoloSiepeController.ExInserisciDefinizioneFascicoloSius : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

}