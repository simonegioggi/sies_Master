package siap.siep.nuovaistanza.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.avvocato.dao.AvvocatoSqlDAO;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.nuovaistanza.dao.NuovaIstanzaDAO;
import siap.siep.nuovaistanza.dao.NuovaIstanzaSqlDAO;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: NuovaIstanzaController
 * </p>
 * <p>
 * Description: Classe Controller per NuovaIstanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @version 5.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NuovaIstanzaController extends GenericController implements INuovaIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public NuovaIstanzaModel ExInserisciNuovaIstanza(EventoModel aEveMod, NuovaIstanzaModel aNuovaIstanza,
			SentenzaModel aSenMod, SoggettoModel aSogMod, FascicoloSiepModel aFascMod) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;
		EventoDAO lEveDao = null;
		SentenzaDAO lSenDao = null;
		SoggettoDAO lSogDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		try {
			lConn = getDBTransaction();
			lNuoDao = new NuovaIstanzaDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lSenDao = new SentenzaDAO(lConn);
			lSogDao = new SoggettoDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			// Sentenza
			BigDecimal lKeySentenza = null;
			if (aSenMod != null) {
				lSenDao.setDAOFromModel(aSenMod);
				lKeySentenza = lSenDao.insert();
				lSenDao.stop();
			}

			// Soggetto
			BigDecimal lKeySoggetto = null;
			if (aSogMod != null) {
				lSogDao.setDAOFromModel(aSogMod);
				lKeySoggetto = lSogDao.insert();
				lSogDao.stop();
			}

			// Fascicolo
			BigDecimal lKeyFascicolo = null;
			if (aFascMod != null) {
				// Cerco il Progressivo rispettivamente al tipo progressivo impostato
				// solo se la classe è impostata
				lFasDaoSql.getProgressivoFascicoloSiep(aFascMod);
				lFasDaoSql.start();
				int lMaxProgr = 0;

				if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
					lMaxProgr = lFasDaoSql.getInt("aMAX");

				lFasDaoSql.stop();

				// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
				int lTipoProgr = aFascMod.getTipoProgressivo();
				if (lMaxProgr == 0) {
					aFascMod.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
				} else {
					aFascMod.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
				}

				if (lKeySoggetto != null)
					aFascMod.setSogIdSoggetto(lKeySoggetto);

				if (lKeySentenza != null)
					aFascMod.setSenIdSentenza(lKeySentenza);

				lFasDao.setDAOFromModel(aFascMod);
				lKeyFascicolo = lFasDao.insert();
				lFasDao.stop();
			}

			// Evento
			if (lKeyFascicolo != null)
				aEveMod.setFasSieIdFascicoloSiep(lKeyFascicolo);

			lEveDao.setDAOFromModel(aEveMod);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// Istanza
			aNuovaIstanza.setEveIdEvento(lKeyEvento);
			aNuovaIstanza.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());

			lNuoDao.setDAOFromModel(aNuovaIstanza);
			BigDecimal lKeyIstanza = null;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(aNuovaIstanza.toString());
			lKeyIstanza = lNuoDao.insert();
			lNuoDao.stop();

			aNuovaIstanza.setIdNuovaIstanza(lKeyIstanza);

			// STATO_PROCEDIMENTO
			StatoProcedimentoModel lProcModel = new StatoProcedimentoModel();
			lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiepDesc(aEveMod.getFasSieIdFascicoloSiep());
			lProcModel = (StatoProcedimentoModel) lStatoSqlDao.getModelByKey();
			BigDecimal codStatoMax = null;

			// ========================================================================
			// Se l'ultimo stato procedimento è:
			// 0011 - Emesso Ordine di Esecuzione con Contestuale Sospensione il
			// oppure
			// 0013 - Atti Trasmessi al TDS il
			// Vado in append aggiungendo 0012 - Presentata Istanza il, altrimenti
			// cancello tutto e inserisco solo 0012
			// ========================================================================
			if (lProcModel != null && lProcModel.getCodStatoProcedimento() != null
					&& (lProcModel.getCodStatoProcedimento().equals("0011") // Emesso Ordine di Esecuzione con
																			// Contestuale Sospensione il
					|| lProcModel.getCodStatoProcedimento().equals("0013") // Atti Trasmessi al TDS il
					)) {
				codStatoMax = lStatoSqlDao.getProgressivo(aEveMod.getFasSieIdFascicoloSiep());
				codStatoMax = new BigDecimal(codStatoMax.intValue() + 1);
			} else {
				lStatoDao.setCondizioneByIdFascicolo(aEveMod.getFasSieIdFascicoloSiep());
				lStatoDao.delete();
				lStatoDao.stop();
				codStatoMax = new BigDecimal(1);
			}

			StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();

			// Presentata istanza
			lStatoUno.setProgressivo(codStatoMax);

			lStatoUno.setCodStatoProcedimento("0012"); // Presentata Istanza il
			lStatoUno.setData(DateUtils.getSysDate());
			lStatoUno.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());
			lStatoUno.setCodOperatoreInserimento(aNuovaIstanza.getCodOperatoreInserimento());
			lStatoUno.setDataInserimento(aNuovaIstanza.getDataInserimento());
			lStatoUno.setCodUfficioInserimento(aNuovaIstanza.getCodUfficioInserimento());

			lStatoDao.setDAOFromModel(lStatoUno);
			lStatoDao.insert();
			lStatoDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExInserisci: Non posso inserire: " + ex);
		} finally {

			cleanup(lEveDao);
			cleanup(lNuoDao);
			cleanup(lSenDao);
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);
			cleanup(lConn);

		}

		return aNuovaIstanza;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati NuovaIstanza
	 * 
	 * @param aNuovaIstanza
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<NuovaIstanzaModel> ExRicercaNuovaIstanza(NuovaIstanzaModel aNuovaIstanza)
			throws F3BException {
		Connection lConn = null;
		Vector<NuovaIstanzaModel> lNuovaIstanzi = new Vector<NuovaIstanzaModel>();
		NuovaIstanzaDAO lNuoDao = null;

		try {
			lConn = getDBConnection();
			lNuoDao = new NuovaIstanzaDAO(lConn);
			lNuoDao.setCondizioni(aNuovaIstanza);
			lNuoDao.setOrderBy();
			lNuoDao.start();
			while (lNuoDao.next()) {
				lNuovaIstanzi.add((NuovaIstanzaModel) lNuoDao.getModel());
			}
			lNuoDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("NuovaIstanzaController.ExRicercaNuovaIstanza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuoDao);
			cleanup(lConn);
		}

		return lNuovaIstanzi;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati NuovaIstanza per id Fascicolo
	 * 
	 * @param aIdFascicolo
	 *            id fascicolo SIEP utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato
	 *            nel model verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Collection<NuovaIstanzaModel> ExRicercaNuovaIstanzaByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		Collection<NuovaIstanzaModel> lNuovaIstanzi = new Vector<NuovaIstanzaModel>();
		NuovaIstanzaSqlDAO lNuoDao = null;
		NuovaIstanzaModel lModel;
		AvvocatoModel lAvvocato;

		try {
			lConn = getDBConnection();
			lNuoDao = new NuovaIstanzaSqlDAO(lConn);

			lNuoDao.ricercaNuovaIstanzaByIdFascicolo(aIdFascicolo);
			lNuoDao.start();

			while (lNuoDao.next()) {
				lModel = (NuovaIstanzaModel) lNuoDao.getModel();
				if (lModel != null && lModel.getAvvIdAvvocato() != null) {
					lAvvocato = this.getAvvocatoByIdFascicolo(lModel.getAvvIdAvvocato(), lConn);
					lModel.setAvvocato(lAvvocato);
				}
				// 15/03/2010 Lettura Avvocato Presentante
				if (lModel.getAvvIdAvvocatoPresentante() != null) {
					lAvvocato = this.getAvvocatoByIdFascicolo(lModel.getAvvIdAvvocatoPresentante(), lConn);
					lModel.setAvvocatoPresentante(lAvvocato);
				}
				lNuovaIstanzi.add(lModel);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("SELEZIONATO SIZE ALLA FINE" + lNuovaIstanzi.size());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("", daoEx);
			throw new F3BException("ExRicercaNuovaIstanzaByIdFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNuoDao);
			cleanup(lConn);
		}

		return lNuovaIstanzi;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati NuovaIstanza per id Fascicolo. La NuovaIstanza non deve essere stata
	 * annullata
	 * 
	 * @param aIdFascicolo
	 *            id fascicolo SIEP utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato
	 *            nel model verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Collection<NuovaIstanzaModel> ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		Collection<NuovaIstanzaModel> lNuovaIstanzi = new Vector<NuovaIstanzaModel>();
		NuovaIstanzaSqlDAO lNuoDao = null;
		NuovaIstanzaModel lModel;
		AvvocatoModel lAvvocato;

		try {
			lConn = getDBConnection();
			lNuoDao = new NuovaIstanzaSqlDAO(lConn);

			lNuoDao.ricercaNuovaIstanzaNonAnnullataByIdFascicolo(aIdFascicolo);
			lNuoDao.start();

			while (lNuoDao.next()) {
				lModel = (NuovaIstanzaModel) lNuoDao.getModel();
				if (lModel != null && lModel.getAvvIdAvvocato() != null) {
					lAvvocato = this.getAvvocatoByIdFascicolo(lModel.getAvvIdAvvocato(), lConn);
					lModel.setAvvocato(lAvvocato);
				}
				lNuovaIstanzi.add(lModel);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("SELEZIONATO SIZE ALLA FINE" + lNuovaIstanzi.size());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("", daoEx);
			throw new F3BException("ExRicercaNuovaIstanzaNonAnnullataByIdFascicolo: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuoDao);
			cleanup(lConn);
		}

		return lNuovaIstanzi;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public NuovaIstanzaModel ExRicercaNuovaIstanzaById(BigDecimal aIdNuovaIstanza) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaModel lNuovaIstanzaMod = new NuovaIstanzaModel();
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;
		// AvvocatoSqlDAO lAvvSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.ricercaNuovaIstanzaByKey(aIdNuovaIstanza);
			lNuovaIstanzaMod = (NuovaIstanzaModel) lNuovaIstanzaSqlDao.getModelByKey();

			// 15/03/2010 Lettura Avvocato
			if (lNuovaIstanzaMod.getAvvIdAvvocato() != null)
				lNuovaIstanzaMod.setAvvocatoPresentante(this.getAvvocatoByIdFascicolo(
						lNuovaIstanzaMod.getAvvIdAvvocatoPresentante(), lConn));
			// 15/03/2010 Lettura Avvocato Presentante
			if (lNuovaIstanzaMod.getAvvIdAvvocatoPresentante() != null)
				lNuovaIstanzaMod.setAvvocatoPresentante(this.getAvvocatoByIdFascicolo(
						lNuovaIstanzaMod.getAvvIdAvvocatoPresentante(), lConn));

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExRicercaNuovaIstanzaById: Non posso leggere: "
					+ daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}

		return lNuovaIstanzaMod;
	}

	/**
	 * ExRicercaNuovaIstanzaByEveIdEvento
	 * 
	 * @param aEveIdEvento
	 * @return NuovaIstanzaModel
	 * @throws F3BException
	 */

	public NuovaIstanzaModel ExRicercaNuovaIstanzaByEveIdEvento(BigDecimal aEveIdEvento) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaModel lNuovaIstanzaMod = new NuovaIstanzaModel();
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.ricercaNuovaIstanzaByEveIdEvento(aEveIdEvento);
			lNuovaIstanzaMod = (NuovaIstanzaModel) lNuovaIstanzaSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"NuovaIstanzaController.ExRicercaNuovaIstanzaByEveIdEvento: Non posso leggere: " + daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}

		return lNuovaIstanzaMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'NuovaIstanza Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aNuovaIstanza
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaNuovaIstanza(NuovaIstanzaModel aNuovaIstanza) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;

		try {
			lConn = getDBConnection();
			lNuoDao = new NuovaIstanzaDAO(lConn);
			lNuoDao.setDAOFromModel(aNuovaIstanza);
			lNuoDao.selCondizioneUpdate(aNuovaIstanza.getIdNuovaIstanza());
			lNuoDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("NuovaIstanzaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lNuoDao);
			cleanup(lConn);
		}
	}

	/**
	 * annullamento Istanza
	 * 
	 * @param aIstMod
	 * @param aCampoNota
	 * @return
	 * @throws F3BException
	 */
	public void ExAnnulamentoIstanza(NuovaIstanzaModel aIstMod, CampoNotaModel aCampoNota)
			throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		NuovaIstanzaDAO lIstaDao = null;

		EventoModel EveMod = null;
		// NuovaIstanzaModel lIstMod = new NuovaIstanzaModel(aIstMod);

		try {
			lConn = getDBTransaction();

			// RICERCA L'EVENTO DA ANNULLARE assocciato all'istanza
			lEveDao = new EventoDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			if (aIstMod != null && aIstMod.getEveIdEvento() != null) {
				lEveSqlDAO.ricercaEventoByKey(aIstMod.getEveIdEvento());
				EveMod = (EventoModel) lEveSqlDAO.getModelByKey();

				lEveDao.setIdEvento(EveMod.getIdEvento());
				lEveDao.setFlagDocumentoRegistrato("A");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
			}

			// Inserisco il CampoNote
			lCampoNotaDao = new CampoNotaDAO(lConn);
			// BigDecimal lKey = null;
			if (!aCampoNota.getDescr().equals("")) {
				aCampoNota.setProgressivo(new BigDecimal(1));
				aCampoNota.setEveIdEvento(EveMod.getIdEvento());
				lCampoNotaDao.setDAOFromModel(aCampoNota);
				/* lKey = */lCampoNotaDao.insert();
				lCampoNotaDao.stop();
			}

			// ANNULLO L'ISTANZA
			lIstaDao = new NuovaIstanzaDAO(lConn);

			lIstaDao.setIdNuovaIstanza(aIstMod.getIdNuovaIstanza());
			lIstaDao.setCodStatoIstanza(aIstMod.getCodStatoIstanza());
			lIstaDao.selByKey();
			lIstaDao.update();
			lIstaDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAnnulamentoIstanza: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAnnulamentoIstanza: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lIstaDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);

			cleanup(lConn);
		}

	}

	public void ExAnnullamentoInoltroIstanza(BigDecimal aIdEventoNuovaIstanza, BigDecimal aIdIstanza,
			CampoNotaModel aCampoNota, String TipoOp) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		NuovaIstanzaDAO lIstaDao = null;

		EventoModel EveMod = null;
		// NuovaIstanzaModel lIstMod = new NuovaIstanzaModel();

		try {
			lConn = getDBTransaction();

			if (TipoOp.equals("A")) {
				// RICERCA L'EVENTO di inoltro DA ANNULLARE associato all'istanza
				lEveDao = new EventoDAO(lConn);
				lEveSqlDAO = new EventoSqlDAO(lConn);

				lEveSqlDAO.ricercaEventoByKey(aIdEventoNuovaIstanza);
				EveMod = (EventoModel) lEveSqlDAO.getModelByKey();

				lEveDao.setIdEvento(EveMod.getIdEvento());
				lEveDao.setFlagDocumentoRegistrato("A");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Update su tabella Evento");

				// Inserisco il CampoNote
				/*
				 * lCampoNotaDao = new CampoNotaDAO(lConn); BigDecimal lKey = null; if
				 * (!aCampoNota.getDescr().equals("")) { aCampoNota.setProgressivo(new BigDecimal(1));
				 * aCampoNota.setEveIdEvento(EveMod.getIdEvento()); lCampoNotaDao.setDAOFromModel(aCampoNota);
				 * lKey = lCampoNotaDao.insert(); lCampoNotaDao.stop(); }
				 */

			} else {
				lEveDao = new EventoDAO(lConn);
				lEveDao.selCondizioneUpdate(aIdEventoNuovaIstanza);
				lEveDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Delete su tabella Evento");

			}
			// Cancello la data inoltro !!!
			lIstaDao = new NuovaIstanzaDAO(lConn);
			lIstaDao.setIdNuovaIstanza(aIdIstanza);
			lIstaDao.setDataInoltroPM(null);
			lIstaDao.selByKey();
			lIstaDao.update();
			lIstaDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Update su tabella Istanza");
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAnnullamentoInoltroIstanza: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAnnullamentoInoltroIstanza: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lIstaDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);

			cleanup(lConn);
		}

	}

	public void ExAnnullamentoDisposizioneIstanza(BigDecimal aIdEventoNuovaIstanza, BigDecimal aIdIstanza,
			CampoNotaModel aCampoNota, String TipoOp) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		CampoNotaDAO lCampoNotaDao = null;
		NuovaIstanzaSqlDAO lIstaDao = null;

		EventoModel EveMod = null;
		// NuovaIstanzaModel lIstMod = new NuovaIstanzaModel();

		try {
			lConn = getDBTransaction();

			if (TipoOp.equals("A")) {
				// RICERCA L'EVENTO di inoltro DA ANNULLARE associato all'istanza
				lEveDao = new EventoDAO(lConn);
				lEveSqlDAO = new EventoSqlDAO(lConn);

				lEveSqlDAO.ricercaEventoByKey(aIdEventoNuovaIstanza);
				EveMod = (EventoModel) lEveSqlDAO.getModelByKey();

				lEveDao.setIdEvento(EveMod.getIdEvento());
				lEveDao.setFlagDocumentoRegistrato("A");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Update su tabella Evento");

				// Inserisco il CampoNote

				lCampoNotaDao = new CampoNotaDAO(lConn);
				// BigDecimal lKey = null;
				if (!aCampoNota.getDescr().equals("")) {
					aCampoNota.setProgressivo(new BigDecimal(1));
					aCampoNota.setEveIdEvento(EveMod.getIdEvento());
					lCampoNotaDao.setDAOFromModel(aCampoNota);
					/* lKey = */lCampoNotaDao.insert();
					lCampoNotaDao.stop();
				}

			} else {
				lIstaDao = new NuovaIstanzaSqlDAO(lConn);
				lIstaDao.cancellaNoteDisposizioneNuovaIstanza(aIdEventoNuovaIstanza);
				lIstaDao.start();
				lIstaDao.stop();
				lIstaDao.cancellaNotificheDisposizioneNuovaIstanza(aIdEventoNuovaIstanza);
				lIstaDao.start();
				lIstaDao.stop();

				lEveDao = new EventoDAO(lConn);
				lEveDao.selCondizioneUpdate(aIdEventoNuovaIstanza);
				lEveDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Delete su tabella Evento");

			}
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAnnullamentoInoltroIstanza: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAnnullamentoInoltroIstanza: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lIstaDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);

			cleanup(lConn);
		}

	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aNuovaIstanza
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountNuovaIstanza(NuovaIstanzaModel aNuovaIstanza) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.getCountNuovaIstanza(aNuovaIstanza);
			lNuovaIstanzaSqlDao.start();
			lNuovaIstanzaSqlDao.next();
			lCount = lNuovaIstanzaSqlDao.getBigDecimal("HowManyRecords");
			lNuovaIstanzaSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExGetCountNuovaIstanza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aNuovaIstanza
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountNuovaIstanzaByAnnoProgr(NuovaIstanzaModel aNuovaIstanza, int annoIni,
			int progrIni, int annoFine, int progrFine) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.getCountNuoveIstanzeByAnnoProgr(aNuovaIstanza, annoIni, progrIni, annoFine,
					progrFine);
			lNuovaIstanzaSqlDao.start();
			lNuovaIstanzaSqlDao.next();
			lCount = lNuovaIstanzaSqlDao.getBigDecimal("HowManyRecords");
			lNuovaIstanzaSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExGetCountNuovaIstanza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aNuovaIstanza
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountNuovaIstanzaBySoggetto(NuovaIstanzaModel aNuovaIstanza, SoggettoModel aSogMod)
			throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.getCountNuoveIstanzeBySoggetto(aNuovaIstanza, aSogMod);
			lNuovaIstanzaSqlDao.start();
			lNuovaIstanzaSqlDao.next();
			lCount = lNuovaIstanzaSqlDao.getBigDecimal("HowManyRecords");
			lNuovaIstanzaSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExGetCountNuovaIstanza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aNuovaIstanza
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaNuovaIstanzaPaged(NuovaIstanzaModel aNuovaIstanza, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lNuovaIstanzi = new Vector();
		// Vector lNuoIst = new Vector();
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.ricercaNuovaIstanzaPaged(aNuovaIstanza, aPage);
			lNuovaIstanzi = new Vector(lNuovaIstanzaSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExRicercaNuovaIstanzaPaged: " + daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}
		return lNuovaIstanzi;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aNuovaIstanza
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaNuoveIstanzeByAnnoProgrPaged(NuovaIstanzaModel aNuovaIstanza, int annoIni,
			int progrIni, int annoFine, int progrFine, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lNuovaIstanzi = new Vector();
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.ricercaNuoveIstanzeByAnnoProgrPaged(aNuovaIstanza, annoIni, progrIni,
					annoFine, progrFine, aPage);
			lNuovaIstanzi = new Vector(lNuovaIstanzaSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExRicercaNuovaIstanzaPaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}
		return lNuovaIstanzi;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aNuovaIstanza
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaNuoveIstanzeBySoggettoPaged(NuovaIstanzaModel aNuovaIstanza,
			SoggettoModel aSogMod, int aPage) throws F3BException {
		Connection lConn = null;
		Vector lNuovaIstanzi = new Vector();
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;

		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.ricercaNuoveIstanzeBySoggettoPaged(aNuovaIstanza, aSogMod, aPage);
			lNuovaIstanzi = new Vector(lNuovaIstanzaSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExRicercaNuovaIstanzaPaged: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}
		return lNuovaIstanzi;
	}

	/**
	 * metodo di utilita' per il recupero dell'avocato associato al fascicolo
	 * 
	 * @param lKeyFascicolo
	 * @return
	 */

	private AvvocatoModel getAvvocatoByIdFascicolo(BigDecimal lKeyAvvocato, Connection lConn)
			throws F3BException {
		AvvocatoSqlDAO lAvvDao = null;
		AvvocatoModel lAvv = null;
		try {
			lAvvDao = new AvvocatoSqlDAO(lConn);

			lAvvDao.ricercaAvvocatobyKey(lKeyAvvocato);

			lAvv = (AvvocatoModel) lAvvDao.getModelByKey();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("getAvvocatoByIdFascicolo: Non posso leggere : " + ex);

		} finally {
			cleanup(lAvvDao);
		}

		return lAvv;
	}

	/**
	 * Stampa trasferimento Nuova Istanza
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	// 20180110: [SG] aggiunto parametro di passaggio x gestione NOTIFICHE: prendo solo l'ultima
	public ByteArrayOutputStream ExStampaTrasmissioneNuovaIstanza(EventoNotificaModel aEvento,
			UtenteModel aUtente, String tipologia) throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			IEvento lEvCrtl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEventoModel = lEvCrtl.ExRicercaEventoNotificaByKey(aEvento.getEvento()
					.getIdEvento());

			// 20180110: [SG] gestione NOTIFICHE: prendo solo l'ultima
			if ("TI".equals(tipologia)) {
				NotificaModel[] nm = lEventoModel.getNotifiche();
				if (nm != null && nm.length > 1) {
					Vector v = new Vector();
					int length = nm.length;
					v.add(nm[length - 1]);
					lEventoModel.setNotifiche((NotificaModel[]) v.toArray(new NotificaModel[0]));
				}
			}

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiIstruttoria(lEventoModel, aUtente);

			ReportGenerator lReport = new ReportGenerator();
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("getFlagDocumentoRegistrato >>> "
					+ lEventoModel.getEvento().getFlagDocumentoRegistrato());

			// aEvento.getEvento().setFlagDocumentoRegistrato("N");
			aEvento.getEvento().setFlagDocumentoRegistrato(
					lEventoModel.getEvento().getFlagDocumentoRegistrato());
			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExStampaTrasmissioneNuovaIstanza: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Stampa trasferimento Nuova Istanza
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaRicevutaNuovaIstanza(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			IEvento lEvCrtl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEventoModel = lEvCrtl.ExRicercaEventoNotificaByKey(aEvento.getEvento()
					.getIdEvento());

			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiIstruttoria(lEventoModel, aUtente);

			ReportGenerator lReport = new ReportGenerator();
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

			aEvento.getEvento().setFlagDocumentoRegistrato("S");
			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("NuovaIstanzaController.ExStampaRicevutaNuovaIstanza: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Inserisci i records di Nuova Istanza senza assegnare la sequence
	 * 
	 * @param aNuovaIstanza
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciNuovaIstanzaWithoutSequence(ArrayList aNuovaIstanza, Connection lConn)
			throws F3BException {
		String lCodEsito = "00000";
		NuovaIstanzaDAO lNuoIstDao = null;
		NuovaIstanzaModel lRicConMod = null;

		try {
			lNuoIstDao = new NuovaIstanzaDAO(lConn);

			if (aNuovaIstanza != null && aNuovaIstanza.size() > 0) {
				for (int i = 0; i < aNuovaIstanza.size(); i++) {
					lRicConMod = (NuovaIstanzaModel) aNuovaIstanza.get(i);
					if (lRicConMod != null) {
						if (lRicConMod.getIdNuovaIstanza() != null) {
							lNuoIstDao.setDAOFromModel(lRicConMod);
							lNuoIstDao.setWithoutSequence(true);
							lNuoIstDao.insert();
							lNuoIstDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Nuova Istanza gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Nuova Istanza! ");
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("SanzioneSostitutivaController.ExInserisciNuovaIstanzaWithoutSequence: "
					+ e);
		} finally {
			cleanup(lNuoIstDao);
		}
		return lCodEsito;
	}

	public NuovaIstanzaModel ExInoltraNuovaIstanza(EventoModel aEveMod, NuovaIstanzaModel aNuovaIstanza)
			throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;
		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			lEveDao.setDAOFromModel(aEveMod);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// Istanza
			// nel campo eveIdEvento dell'istanza sostituisco l'idevento della creazione del record
			aNuovaIstanza.setEveIdEvento(lKeyEvento);

			lNuoDao = new NuovaIstanzaDAO(lConn);

			lNuoDao.setDataAggiornamento(aNuovaIstanza.getDataAggiornamento());
			lNuoDao.setCodOperatoreAggiornamento(aNuovaIstanza.getCodOperatoreAggiornamento());
			lNuoDao.setCodUfficioAggiornamento(aNuovaIstanza.getCodUfficioAggiornamento());
			lNuoDao.setDataInoltroPM(aNuovaIstanza.getDataInoltroPM());
			lNuoDao.selCondizioneUpdate(aNuovaIstanza.getIdNuovaIstanza());

			// NO non devo cambiare l'idevento dell'istanza ma scrivere nell'eve_idevento
			// dell'EVENTO stesso!!
			// lNuoDao.setEveIdEvento(lKeyEvento);

			lNuoDao.update();
			lNuoDao.stop();

			// STATO_PROCEDIMENTO verificare se va gestito!!!
			/*
			 * StatoProcedimentoModel lProcModel = new StatoProcedimentoModel();
			 * lStatoSqlDao.ricercaStatoProcedimentoByFascicoloSiepDesc(aEveMod.getFasSieIdFascicoloSiep());
			 * lProcModel = (StatoProcedimentoModel)lStatoSqlDao.getModelByKey(); BigDecimal codStatoMax =
			 * null;
			 * 
			 * //======================================================================== // Se l'ultimo stato
			 * procedimento è: // 0011 - Emesso Ordine di Esecuzione con Contestuale Sospensione il // oppure
			 * // 0013 - Atti Trasmessi al TDS il // Vado in append aggiungendo 0012 - Presentata Istanza il,
			 * altrimenti // cancello tutto e inserisco solo 0012
			 * //======================================================================== if( lProcModel !=
			 * null && lProcModel.getCodStatoProcedimento() != null && (
			 * lProcModel.getCodStatoProcedimento().equals("0011") // Emesso Ordine di Esecuzione con
			 * Contestuale Sospensione il || lProcModel.getCodStatoProcedimento().equals("0013") // Atti
			 * Trasmessi al TDS il ) ) { codStatoMax =
			 * lStatoSqlDao.getProgressivo(aEveMod.getFasSieIdFascicoloSiep()); codStatoMax = new
			 * BigDecimal(codStatoMax.intValue() + 1); } else {
			 * lStatoDao.setCondizioneByIdFascicolo(aEveMod.getFasSieIdFascicoloSiep()); lStatoDao.delete();
			 * lStatoDao.stop(); codStatoMax = new BigDecimal(1); }
			 * 
			 * StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();
			 * 
			 * //Presentata istanza lStatoUno.setProgressivo(codStatoMax);
			 * 
			 * lStatoUno.setCodStatoProcedimento("0012"); // Presentata Istanza il
			 * lStatoUno.setData(DateUtils.getSysDate());
			 * lStatoUno.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());
			 * lStatoUno.setCodOperatoreInserimento(aNuovaIstanza.getCodOperatoreInserimento());
			 * lStatoUno.setDataInserimento(aNuovaIstanza.getDataInserimento());
			 * lStatoUno.setCodUfficioInserimento(aNuovaIstanza.getCodUfficioInserimento());
			 * 
			 * lStatoDao.setDAOFromModel(lStatoUno); lStatoDao.insert(); lStatoDao.stop();
			 */
			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExInserisci: Non posso inserire: " + ex);
		} finally {

			cleanup(lEveDao);
			cleanup(lNuoDao);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);
			cleanup(lConn);

		}

		return aNuovaIstanza;
	}

	public EventoModel ExUpdateValidaInoltroNuovaIstanza(EventoModel aEvento) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;

		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveModOE = null;

		try {
			lConn = getDBTransaction();

			// ===================================================
			// Recupera l'evento da validare
			// ===================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero l'evento da validare ");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();
			lEveModOE = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveMod istanza = " + lEveModOE);
			commit(lConn);

			// ====================
			// Update del Blob
			// ====================
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException("NuovaIstanzaController.ExUpdateValidaInoltroNuovaIstanza : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("NuovaIstanzaController.ExUpdateValidaInoltroNuovaIstanza : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveModOE;
	}

	public EventoNotificaModel ExDisposizioneNuovaIstanza(NuovaIstanzaModel aNuovaIstanza,
			EventoNotificaModel lEve) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;

		EventoNotificaModel lRetModel = new EventoNotificaModel();

		try {
			lConn = getDBConnection();
			lNuoDao = new NuovaIstanzaDAO(lConn);

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lRetModel = lCtrl.ExInserisciEventoNotifica(lEve, lConn);

			// lNuoDao.setEveIdEvento(lRetModel.getEvento().getIdEvento());
			lNuoDao.setDataAggiornamento(aNuovaIstanza.getDataAggiornamento());
			lNuoDao.setCodOperatoreAggiornamento(aNuovaIstanza.getCodOperatoreAggiornamento());
			lNuoDao.setCodUfficioAggiornamento(aNuovaIstanza.getCodUfficioAggiornamento());
			lNuoDao.setCodStatoIstanza(aNuovaIstanza.getCodStatoIstanza());
			lNuoDao.selCondizioneUpdate(aNuovaIstanza.getIdNuovaIstanza());
			lNuoDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("NuovaIstanzaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lNuoDao);
			cleanup(lConn);
		}
		return lRetModel;
	}

	/*****************************************************************************
	 * Funzione di ricerca flag di validazione
	 * 
	 * @param aIdEventoNuovaIstanza
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @throws F3BException
	 ****************************************************************************/
	public String ExRicercaFlagValNuovaIstanzaPaged(BigDecimal aIdEventoNuovaIstanza, int aPage)
			throws F3BException {
		Connection lConn = null;
		String lNuovaIstanzi = null;
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;
		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			lNuovaIstanzaSqlDao.ricercaFlagValNuovaIstanzaPaged(aIdEventoNuovaIstanza, aPage);
			lNuovaIstanzaSqlDao.start();
			lNuovaIstanzaSqlDao.next();
			lNuovaIstanzi = lNuovaIstanzaSqlDao.getString("VAL_FLAG");
			lNuovaIstanzaSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"NuovaIstanzaController.ExRicercaFlagValNuovaIstanzaPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}
		return lNuovaIstanzi;
	}

	// Conversione RI in Fascicolo SIEP
	public FascicoloSiepModel ExConvertiNuovaIstanza(SoggettoModel aSogMod, FascicoloSiepModel aFascMod)
			throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;
		SoggettoDAO lSogDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;

		try {
			lConn = getDBTransaction();
			lNuoDao = new NuovaIstanzaDAO(lConn);
			lSogDao = new SoggettoDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);

			// Soggetto
			BigDecimal lKeySoggetto = null;
			if (aSogMod != null) {
				lSogDao.setDAOFromModel(aSogMod);
				lKeySoggetto = lSogDao.insert();
				lSogDao.stop();
			}

			// Fascicolo
			BigDecimal lKeyFascicolo = null;
			if (aFascMod != null) {
				lFasDaoSql.getProgressivoFascicoloSiep(aFascMod);
				lFasDaoSql.start();
				int lMaxProgr = 0;

				if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
					lMaxProgr = lFasDaoSql.getInt("aMAX");

				lFasDaoSql.stop();

				// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
				int lTipoProgr = aFascMod.getTipoProgressivo();
				if (lMaxProgr == 0) {
					aFascMod.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
				} else {
					aFascMod.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
				}

				// Assegna al nuovo fascicolo l'id del nuovo soggetto
				aFascMod.setSogIdSoggetto(lKeySoggetto);

				lFasDao.setDAOFromModel(aFascMod);
				lKeyFascicolo = lFasDao.insert();
				lFasDao.stop();

				aFascMod.setIdFascicoloSiep(lKeyFascicolo);
			}

			// Istanza
			/*
			 * aNuovaIstanza.setEveIdEvento(lKeyEvento);
			 * aNuovaIstanza.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());
			 * lNuoDao.setDAOFromModel(aNuovaIstanza ); BigDecimal lKeyIstanza = null; // [FT] - 03/08/2016 -
			 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.info(aNuovaIstanza.toString()); lKeyIstanza = lNuoDao.insert(); lNuoDao.stop();
			 * aNuovaIstanza.setIdNuovaIstanza(lKeyIstanza);
			 */

			// Aggiorna il Registro Istanza con stato = "01" archiviato/definito
			FascicoloSiepDAO lRIDao = new FascicoloSiepDAO(lConn);

			// lRIDao.setCodMotivoArchiviazione(lMotivoArchiviazione);
			lRIDao.setCodStatoFascicolo("01");
			lRIDao.setDataArchiviazione(DateUtils.getSysDate());

			lRIDao.setDataAggiornamento(DateUtils.getSysDate());
			lRIDao.setCodUfficioAggiornamento(aFascMod.getCodUfficioInserimento());
			lRIDao.setCodOperatoreAggiornamento(aFascMod.getCodOperatoreInserimento());

			lRIDao.selCondizioneUpdate(aFascMod.getFasSieIdFascicoloSiep());
			lRIDao.update();
			lRIDao.stop();

			// STATO_PROCEDIMENTO
			BigDecimal codStatoMax = new BigDecimal(1);
			StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();
			lStatoUno.setProgressivo(codStatoMax);
			lStatoUno.setCodStatoProcedimento("0108"); // Iscritto
			lStatoUno.setData(DateUtils.getSysDate());
			lStatoUno.setFasSieIdFascicoloSiep(lKeyFascicolo);
			lStatoUno.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
			lStatoUno.setDataInserimento(aFascMod.getDataInserimento());
			lStatoUno.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());

			lStatoDao.setDAOFromModel(lStatoUno);
			lStatoDao.insert();
			lStatoDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExInserisci: Non posso inserire: " + ex);
		} finally {

			cleanup(lNuoDao);
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);
			cleanup(lConn);

		}

		return aFascMod;
	}

	// Nuova Conversione RI in Fascicolo SIEP
	public FascicoloSiepModel ExConvertiNuovaIstanza(SoggettoModel aSogMod, FascicoloSiepModel aFascMod,
			FascicoloSiepModel aRIMod, EventoModel aEveMod, NuovaIstanzaModel aNuoMod) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;
		SoggettoDAO lSogDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		StatoProcedimentoDAO lStatoDao = null;
		StatoProcedimentoDAO lStatoNIDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;
		FascicoloSiepDAO lRIDao = null;
		EventoDAO lEveDao = null;

		try {
			lConn = getDBTransaction();
			lNuoDao = new NuovaIstanzaDAO(lConn);
			lSogDao = new SoggettoDAO(lConn);
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoNIDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);
			lRIDao = new FascicoloSiepDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Soggetto
			BigDecimal lKeySoggetto = null;
			if (aSogMod != null) {
				lSogDao.setDAOFromModel(aSogMod);
				lKeySoggetto = lSogDao.insert();
				lSogDao.stop();
			}

			// Fascicolo
			BigDecimal lKeyFascicolo = null;
			if (aFascMod != null) {
				lFasDaoSql.getProgressivoFascicoloSiep(aFascMod);
				lFasDaoSql.start();
				int lMaxProgr = 0;

				if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
					lMaxProgr = lFasDaoSql.getInt("aMAX");

				lFasDaoSql.stop();

				// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
				int lTipoProgr = aFascMod.getTipoProgressivo();
				if (lMaxProgr == 0) {
					aFascMod.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
				} else {
					aFascMod.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
				}

				// Assegna al nuovo fascicolo l'id del nuovo soggetto
				aFascMod.setSogIdSoggetto(lKeySoggetto);

				lFasDao.setDAOFromModel(aFascMod);
				lKeyFascicolo = lFasDao.insert();
				lFasDao.stop();

				aFascMod.setIdFascicoloSiep(lKeyFascicolo);
			}

			// Evento
			if (lKeyFascicolo != null)
				aEveMod.setFasSieIdFascicoloSiep(lKeyFascicolo);

			lEveDao.setDAOFromModel(aEveMod);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// Sdoppiamento di Istanza ed Evento
			aNuoMod.setEveIdEvento(lKeyEvento);
			aNuoMod.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());
			aNuoMod.setDataInoltroPM(null); // 09/03/2011
			lNuoDao.setDAOFromModel(aNuoMod);
			BigDecimal lKeyIstanza = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(aNuoMod.toString());
			lKeyIstanza = lNuoDao.insert();
			lNuoDao.stop();
			aNuoMod.setIdNuovaIstanza(lKeyIstanza);
			// ///////////////////////////////////////////

			// Aggiorna il Registro Istanza con stato = "01" archiviato/definito
			// lRIDao.setCodMotivoArchiviazione(lMotivoArchiviazione);
			lRIDao.setCodStatoFascicolo("01");
			lRIDao.setDataArchiviazione(DateUtils.getSysDate());

			lRIDao.setDataAggiornamento(DateUtils.getSysDate());
			lRIDao.setCodUfficioAggiornamento(aFascMod.getCodUfficioInserimento());
			lRIDao.setCodOperatoreAggiornamento(aFascMod.getCodOperatoreInserimento());
			lRIDao.setFasSieIdFascicoloSiep(lKeyFascicolo);

			lRIDao.selCondizioneUpdate(aRIMod.getIdFascicoloSiep());
			lRIDao.update();
			lRIDao.stop();

			// STATO_PROCEDIMENTO NUOVO FASICOLO SIEP
			BigDecimal codStatoMax = new BigDecimal(1);
			StatoProcedimentoModel lStatoUno = new StatoProcedimentoModel();
			lStatoUno.setProgressivo(codStatoMax);
			lStatoUno.setCodStatoProcedimento("0108"); // Iscritto
			lStatoUno.setData(DateUtils.getSysDate());
			lStatoUno.setFasSieIdFascicoloSiep(lKeyFascicolo);
			lStatoUno.setCodOperatoreInserimento(aFascMod.getCodOperatoreInserimento());
			lStatoUno.setDataInserimento(aFascMod.getDataInserimento());
			lStatoUno.setCodUfficioInserimento(aFascMod.getCodUfficioInserimento());

			lStatoDao.setDAOFromModel(lStatoUno);
			lStatoDao.insert();
			lStatoDao.stop();

			// STATO_PROCEDIMENTO ISTANZA CONVERTITA
			lStatoNIDao.setCodStatoProcedimento("0076"); // Archiviato
			lStatoNIDao.setData(DateUtils.getSysDate());

			lStatoNIDao.setCondizioneByIdFascicolo(aRIMod.getIdFascicoloSiep());

			lStatoNIDao.update();
			lStatoNIDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExConvertiNuovaIstanza: Non posso convertire: "
					+ ex);
		} finally {

			cleanup(lNuoDao);
			cleanup(lSogDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lStatoDao);
			cleanup(lStatoSqlDao);
			cleanup(lRIDao);
			cleanup(lStatoNIDao);
			cleanup(lEveDao);
			cleanup(lConn);

		}

		return aFascMod;
	}

	// Associazione RI a Fascicolo SIEP
	public FascicoloSiepModel ExAssociaNuovaIstanza(BigDecimal aIdFascSiep, FascicoloSiepModel aRIMod,
			EventoModel aEveMod, NuovaIstanzaModel aNuoMod) throws F3BException {
		Connection lConn = null;
		StatoProcedimentoDAO lStatoNIDao = null;
		FascicoloSiepDAO lRIDao = null;
		EventoDAO lEveDao = null;
		NuovaIstanzaDAO lNuoDao = null;

		try {
			lConn = getDBTransaction();
			lStatoNIDao = new StatoProcedimentoDAO(lConn);
			lRIDao = new FascicoloSiepDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lNuoDao = new NuovaIstanzaDAO(lConn);

			// Aggiorna il Registro Istanza con stato = "01" archiviato/definito
			lRIDao.setCodStatoFascicolo("01");
			lRIDao.setDataArchiviazione(DateUtils.getSysDate());

			lRIDao.setDataAggiornamento(DateUtils.getSysDate());
			lRIDao.setCodUfficioAggiornamento(aRIMod.getCodUfficioAggiornamento());
			lRIDao.setCodOperatoreAggiornamento(aRIMod.getCodOperatoreAggiornamento());
			lRIDao.setFasSieIdFascicoloSiep(aRIMod.getFasSieIdFascicoloSiep());

			lRIDao.selCondizioneUpdate(aRIMod.getIdFascicoloSiep());
			lRIDao.update();
			lRIDao.stop();

			// Evento
			aEveMod.setFasSieIdFascicoloSiep(aRIMod.getFasSieIdFascicoloSiep());
			lEveDao.setDAOFromModel(aEveMod);
			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// Sdoppiamento di Istanza ed Evento
			aNuoMod.setEveIdEvento(lKeyEvento);
			aNuoMod.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());
			aNuoMod.setDataInoltroPM(null); // 09/03/2011

			lNuoDao.setDAOFromModel(aNuoMod);
			BigDecimal lKeyIstanza = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(aNuoMod.toString());
			lKeyIstanza = lNuoDao.insert();
			lNuoDao.stop();
			aNuoMod.setIdNuovaIstanza(lKeyIstanza);

			// STATO_PROCEDIMENTO ISTANZA ASSOCIATA
			lStatoNIDao.setCodStatoProcedimento("0076"); // Archiviato
			lStatoNIDao.setData(DateUtils.getSysDate());

			lStatoNIDao.setCondizioneByIdFascicolo(aRIMod.getIdFascicoloSiep());

			lStatoNIDao.update();
			lStatoNIDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NuovaIstanzaController.ExAssociaNuovaIstanza: Non posso associare: " + ex);
		} finally {
			cleanup(lRIDao);
			cleanup(lStatoNIDao);
			cleanup(lConn);
		}

		return aRIMod;
	}

	// Annullamento dell'Associazione RI a Fascicolo SIEP
	public FascicoloSiepModel ExAnnullaAssociaNuovaIstanza(FascicoloSiepModel aRIMod) throws F3BException {
		Connection lConn = null;
		StatoProcedimentoDAO lStatoNIDao = null;
		StatoProcedimentoSqlDAO lStatoSqlDao = null;
		FascicoloSiepDAO lRIDao = null;

		try {
			lConn = getDBTransaction();
			lStatoNIDao = new StatoProcedimentoDAO(lConn);
			lStatoSqlDao = new StatoProcedimentoSqlDAO(lConn);
			lRIDao = new FascicoloSiepDAO(lConn);

			// Aggiorna il Registro Istanza con stato = "02" iscritto
			lRIDao.setCodStatoFascicolo("02");
			lRIDao.setDataArchiviazione(null);

			lRIDao.setDataAggiornamento(DateUtils.getSysDate());
			lRIDao.setCodUfficioAggiornamento(aRIMod.getCodUfficioAggiornamento());
			lRIDao.setCodOperatoreAggiornamento(aRIMod.getCodOperatoreAggiornamento());
			lRIDao.setFasSieIdFascicoloSiep(null);

			lRIDao.selCondizioneUpdate(aRIMod.getIdFascicoloSiep());
			lRIDao.update();
			lRIDao.stop();

			// STATO_PROCEDIMENTO ISTANZA ASSOCIATA
			lStatoNIDao.setCodStatoProcedimento("0012"); // Presentata istanza
			lStatoNIDao.setData(DateUtils.getSysDate());

			lStatoNIDao.setCondizioneByIdFascicolo(aRIMod.getIdFascicoloSiep());

			lStatoNIDao.update();
			lStatoNIDao.stop();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"NuovaIstanzaController.ExAnnullaAssociaNuovaIstanza: Non posso annullare l'associazione: "
							+ ex);
		} finally {
			cleanup(lStatoSqlDao);
			cleanup(lRIDao);
			cleanup(lStatoNIDao);
			cleanup(lConn);
		}

		return aRIMod;
	}

	/*****************************************************************************
	 * Metodo che modifica lo Stato della NuovaIstanza
	 * 
	 * @param aNuovaIstanza
	 *            Model
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaStatoNuovaIstanza(NuovaIstanzaModel aNuovaIstanza) throws F3BException {
		Connection lConn = null;
		NuovaIstanzaDAO lNuoDao = null;

		try {
			lConn = getDBConnection();
			lNuoDao = new NuovaIstanzaDAO(lConn);
			lNuoDao.setDataAggiornamento(aNuovaIstanza.getDataAggiornamento());
			lNuoDao.setCodOperatoreAggiornamento(aNuovaIstanza.getCodOperatoreAggiornamento());
			lNuoDao.setCodUfficioAggiornamento(aNuovaIstanza.getCodUfficioAggiornamento());
			lNuoDao.setCodStatoIstanza(aNuovaIstanza.getCodStatoIstanza());

			lNuoDao.setCodLuogoDestinatario(aNuovaIstanza.getCodLuogoDestinatario());
			lNuoDao.setCodUfficioDestinatario(aNuovaIstanza.getCodUfficioDestinatario());
			lNuoDao.setCodTipoUfficioDestinatario(aNuovaIstanza.getCodTipoUfficioDestinatario());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Nuova Istanza da MODIFICARE=" + lNuoDao);

			lNuoDao.selCondizioneUpdate(aNuovaIstanza.getIdNuovaIstanza());
			lNuoDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"NuovaIstanzaController.ExModificaStatoNuovaIstanza: Non posso modificare: " + ex);
		} finally {
			cleanup(lNuoDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * ANNA Funzione di ricerca flag di validazione Inoltro
	 * 
	 * @param aIdEventoNuovaIstanza
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @throws F3BException
	 ****************************************************************************/
	public String ExRicercaFlagValNuovaIstanza(BigDecimal aIdEventoNuovaIstanza, String Tipo)
			throws F3BException {
		Connection lConn = null;
		String lNuovaIstanzi = "";
		NuovaIstanzaSqlDAO lNuovaIstanzaSqlDao = null;
		try {
			lConn = getDBConnection();
			lNuovaIstanzaSqlDao = new NuovaIstanzaSqlDAO(lConn);
			if (Tipo.equals("I"))
				lNuovaIstanzaSqlDao.ricercaFlagValInoltroNuovaIstanza(aIdEventoNuovaIstanza);
			else
				lNuovaIstanzaSqlDao.ricercaFlagValDisposizioneNuovaIstanza(aIdEventoNuovaIstanza);
			lNuovaIstanzaSqlDao.start();
			// 31/05/2011 occorre prevedere il RS vuoto.
			// NUOVA INFRASTRUTTURA: Appunto lo potevi prevedere genio!!!
			if (lNuovaIstanzaSqlDao.next())
				// ;
				lNuovaIstanzi = lNuovaIstanzaSqlDao.getString("VAL_FLAG") + "*"
						+ lNuovaIstanzaSqlDao.getString("EVE_ID");
			lNuovaIstanzaSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx.getErrorCode());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx.getMessage());
			// Paolo Cherubini 07/02/2012 x b2/rr/006
			// aggiungo controllo per Exhausted Resultset se la lingua fosse inglese come Reggio Calabria
			if (!daoEx.getMessage().contains("Resultset esaurito")
					&& !daoEx.getMessage().contains("Exhausted Resultset"))
				throw new F3BException(
						"NuovaIstanzaController.ExRicercaFlagValNuovaIstanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lNuovaIstanzaSqlDao);
			cleanup(lConn);
		}
		return lNuovaIstanzi;
	}

}