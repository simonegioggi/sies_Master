package siap.sige.udienza.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.aula.dao.AulaSqlDAO;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.dao.CollegioMagistratoSqlDAO;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.dao.ProvvedimentoSigeSqlDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.dao.TenoreSentenzaReatoDAO;
import siap.sige.tenore.dao.TenoreSigeDAO;
import siap.sige.tenore.dao.TenoreSigeSqlDAO;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.dao.UdienzaSigeRuoloSqlDAO;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeDAO;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: UdienzaSigeRuoloController
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
public class UdienzaSigeRuoloController extends UdienzaSigeController implements IUdienzaSigeRuolo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Funzione di ricerca delle udienze Sige. Si pone un limite al numero di occorrenze da cercare,
	 * attraverso un parametro.
	 * <p>
	 * 
	 * @param aUdienza
	 *            istanza della classe UdienzaSigeModel
	 * @param aNumOccorrenze
	 *            numero di occorrenze da visualizzare.
	 * @return l'insieme delle istanze UdienzaSigeModel.
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaUdienza(UdienzaSigeModel aUdienzaSige, int aNumOccorrenze) throws F3BException {
		Connection lConn = null;
		Vector lUdienzeSige = new Vector();
		UdienzaSigeSqlDAO lUdiSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdiSqlDao = new UdienzaSigeSqlDAO(lConn);
			lUdiSqlDao.ricercaUdienzaSige(aUdienzaSige);
			lUdienzeSige = new Vector(lUdiSqlDao.getModels(aNumOccorrenze));

			if (lUdienzeSige.size() == 0)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("UdienzaSigeRuoloController.ExRicercaUdienza: " + daoEx);
		} finally {
			cleanup(lUdiSqlDao);
			cleanup(lConn);
		}
		return lUdienzeSige;
	}

	/**
	 * Funzione di ricerca delle udienze Sige in base ai parametri in UdienzaSigeModel. l'estrazione tiene
	 * conto del Tipo Ufficio per impostare il filtro "Tipo Rito" (Monocratico / Collegiale)
	 * <p>
	 * 
	 * @param aUdienza
	 *            istanza della classe UdienzaSigeModel
	 * @return l'insieme delle istanze UdienzaSigeModel.
	 * @throws F3BException
	 *             propagazione dell'errore di eccezione.
	 */
	public Vector ExRicercaUdienzaSigePerRuolo(UdienzaSigeModel aUdienzaSige, String aTipoRito)
			throws F3BException {
		Connection lConn = null;
		Vector<UdienzaSigeModel> lUdienzeSige = new Vector<UdienzaSigeModel>();
		UdienzaSigeRuoloSqlDAO lUdiRuoSqlDao = null;
		CollegioMagistratoSqlDAO lColMagDao = null;

		// CollegioMagistratoModel magModel = new CollegioMagistratoModel();

		try {
			lConn = getDBConnection();
			lUdiRuoSqlDao = new UdienzaSigeRuoloSqlDAO(lConn);
			// intervento 11.2.1
			if ("C".equals(aTipoRito)) {
				lUdiRuoSqlDao.ricercaUdienzaSige(aUdienzaSige, aTipoRito);
			} else {
				lUdiRuoSqlDao.ricercaUdienzaSigePerRuolo(aUdienzaSige, aTipoRito);
			}

			lUdiRuoSqlDao.start();

			while (lUdiRuoSqlDao.next()) {
				UdienzaSigeModel lUdienzaSige = null;
				if ("M".equals(aTipoRito)) {
					lUdienzaSige = (UdienzaSigeModel) lUdiRuoSqlDao.getNewExtendModel();
				} else
					lUdienzaSige = (UdienzaSigeModel) lUdiRuoSqlDao.getModelCollegiali();

				if (lUdienzaSige.getColIdCollegio() != null) {
					lColMagDao = new CollegioMagistratoSqlDAO(lConn);

					lColMagDao.ricercaMagistratoByIdCollegioCodUff(lUdienzaSige.getColIdCollegio(),
							aUdienzaSige.getCodUfficioAppartenenza());
					Collection<CollegioMagistratoModel> lColl = new ArrayList<CollegioMagistratoModel>();
					lColl = lColMagDao.getModels();

					if (lUdienzaSige.getCollegio() == null)
						lUdienzaSige.setCollegio(new CollegioModel());

					lUdienzaSige.getCollegio().setCollegioMagistrati(
							(CollegioMagistratoModel[]) lColl.toArray(new CollegioMagistratoModel[0]));
				}

				AulaUdienzaModel aulaModel = new AulaUdienzaModel();
				if (lUdienzaSige != null && lUdienzaSige.getCodIdAulaUdienza() != null) {
					AulaSqlDAO aulaDao = new AulaSqlDAO(lConn);
					aulaDao.ricercaAulaByKey(lUdienzaSige.getCodIdAulaUdienza(),
							lUdienzaSige.getCodIdSezioneUdienza());
					aulaModel = (AulaUdienzaModel) aulaDao.getModelByKey();

				}

				lUdienzaSige.setAulaUdienzaModel(aulaModel);
				lUdienzeSige.add(lUdienzaSige);
			}

			lUdiRuoSqlDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Vector size : " + lUdienzeSige.size());

			// lUdienzeSige = new Vector(lUdiRuoSqlDao.getModels());

			// if ( lUdienzeSige.size() == 0 )
			// throw new SIGEException(SIGEException.USER_MESSAGE,"Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("UdienzaSigeRuoloController.ExRicercaUdienza: " + daoEx);
		} finally {
			cleanup(lUdiRuoSqlDao);
			cleanup(lConn);
		}
		return lUdienzeSige;
	}

	private Vector aggiornaListaTenori(Vector<TenoreSigeModel> aListaTenori,
			ProvvedimentoSigeModel aProvvedimento) {

		// 20190508 [SG]: imposto IdRichiestaSige sempre sui nuovi tenori
		// BigDecimal bd = null;
		// for (TenoreSigeModel tsm : aListaTenori) {
		// if (tsm.getRicSigIdRichiestaSige() != null) {
		// bd = tsm.getRicSigIdRichiestaSige();
		// break;
		// }
		// }
		if (aListaTenori != null) {
			Iterator itx = aListaTenori.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				lTenore.setCodOperatoreInserimento(aProvvedimento.getCodOperatoreInserimento());
				lTenore.setCodUfficioInserimento(aProvvedimento.getCodUfficioInserimento());
				lTenore.setDataInserimento(aProvvedimento.getDataInserimento());
				lTenore.setData(aProvvedimento.getDataInserimento());
				lTenore.setFasIdFascicoloSige(aProvvedimento.getFasIdFascicoloSige());
				lTenore.setProvIdProvvedimentoSige(aProvvedimento.getIdProvvedimentoSige());
				lTenore.setRicSigIdRichiestaSige(null);
				// 20190508 [SG]: cambiata impostazione proprietà
				// lTenore.setRicSigIdRichiestaSige(null);
				// if (lTenore.getRicSigIdRichiestaSige() == null)
				// lTenore.setProvIdProvvedimentoSige(aProvvedimento.getIdProvvedimentoSige());
				// lTenore.setCodOperatoreAggiornamento(null);
				// lTenore.setCodUfficioAggiornamento(null);
				// lTenore.setDataAggiornamento(null);
				// if (bd != null && lTenore.getRicSigIdRichiestaSige() == null)
				// lTenore.setRicSigIdRichiestaSige(bd);
			}
		} // endif

		return aListaTenori;

	}

	/**
	 * Inserisci Fissazione Udienza
	 * <p>
	 * 
	 * @param aNuovaUdienzaProc
	 * @param aFasSige
	 * @param aEve
	 * @param lProvvedimento
	 * @param aVecchiaUdienzaProc
	 * @param lTenori
	 * @return
	 * @throws F3BException
	 */
	public UdienzaProcedimentoSigeModel ExInserisciFissazioneUdienza(
			UdienzaProcedimentoSigeModel aNuovaUdienzaProc, FascicoloSigeModel aFasSige,
			EventoNotificaModel aEve, ProvvedimentoSigeModel lProvvedimento, Vector lTenori,
			UdienzaProcedimentoSigeModel aVecchiaUdienzaProc) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		TenoreSigeDAO lTenDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		TenoreSigeSqlDAO lTenoreSqlDao = null;
		FascicoloSigeDAO lFasSigeDAO = null;
		UdienzaProcedimentoSigeDAO lUdiDao = null;

		if (aFasSige == null || aFasSige.getIdFascicoloSige() == null)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Impossibile aggiornare il Fascicolo SIGE");

		try {
			// Prende una connessione in transazione.
			lConn = getDBTransaction();

			// Chiamata al Controller per inserimento EVENTO NOTIFICA
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEve = lCtrlEve.ExInserisciEventoNotifica(aEve, lConn);

			// Inserimento di un nuovo record in UDIENZA_PROCEDIMENTO.
			lUdiDao = new UdienzaProcedimentoSigeDAO(lConn);
			aNuovaUdienzaProc.seEveIdEvento(lEve.getEvento().getIdEvento());
			lUdiDao.setDAOFromModel(aNuovaUdienzaProc);
			BigDecimal lKey = lUdiDao.insert();
			lUdiDao.stop();
			aNuovaUdienzaProc.setIdUdienzaProcedimentoSige(lKey);

			// Eventuale aggiornamento del vecchio record in UDIENZA_PROCEDIMENTO
			if (aVecchiaUdienzaProc != null && aVecchiaUdienzaProc.getIdUdienzaProcedimentoSige() != null) {
				lUdiDao.setDAOFromModelForUpdateFissazione(aVecchiaUdienzaProc);
				lUdiDao.update();
				lUdiDao.stop();
			}

			// Aggiornamento di FASCICOLO_SIGE (DATA_UDIENZA (impostata nella action) e COD_STATO_FASCICOLO).
			lFasSigeDAO = new FascicoloSigeDAO(lConn);
			lFasSigeDAO.setDAOFromModel(aFasSige);
			// MEV 15 S4 - Modifica del 14/01/2016
			// se il procedimento ha un opposizione al provvedimento definitorio
			// con esito decisione "Accoglie (Fissa l'udienza)", lo stato del procedimento
			// non deve essere aggiornato (14 = Opposizione - Accoglie (fissa l'udienza))
			// idem se il procedimento ha un ricorso con esito decisione "Ricorso convertito
			// in opposizione (Fissa Udienza)", lo stato del procedimento
			// non deve essere aggiornato (16 = Ricorso convertito in opposizione (Fissa Udienza))
			if (aFasSige.getCodStatoFascicolo() != null && !aFasSige.getCodStatoFascicolo().equals("14")
					&& !aFasSige.getCodStatoFascicolo().equals("16")) {
				// Modifica del 27/02/2017
				// Richiesta fatta da Nunzia
				// Se sul fascicolo è stata fissata un'udienza lo stato del fascicolo
				// deve essere settato a 20 ---> "Decreto Fissazione Udienza"
				// lFasSigeDAO.setCodStatoFascicolo("02");
				lFasSigeDAO.setCodStatoFascicolo("20");
			}

			lFasSigeDAO.setCodUfficioAggiornamento(aFasSige.getCodUfficioAggiornamento());
			lFasSigeDAO.setCodOperatoreAggiornamento(aFasSige.getCodOperatoreAggiornamento());
			lFasSigeDAO.setDataAggiornamento(aFasSige.getDataAggiornamento());
			lFasSigeDAO.setCodTipoGiudizio(aFasSige.getCodTipoGiudizio());
			lFasSigeDAO.setCondizioneUpdate(aFasSige.getIdFascicoloSige());
			lFasSigeDAO.update();
			lFasSigeDAO.stop();

			// MEV 15 S4 - Modifica del 15/01/2016
			// se il procedimento ha un opposizione al provvedimento definitorio
			// con esito decisione "Accoglie (Fissa l'udienza)" (COD_STATO_FASCICOLO = 14),
			// bisogna annullare la data del provvedimento definitorio.
			// idem se il procedimento ha un ricorso con esito decisione "Ricorso convertito
			// in opposizione (Fissa Udienza)" (COD_STATO_FASCICOLO = 16)
			if (aFasSige.getCodStatoFascicolo() != null && (aFasSige.getCodStatoFascicolo().equals("14")
					|| aFasSige.getCodStatoFascicolo().equals("16"))) {
				Date lDate = null;
				aFasSige.setDataDefinizione(lDate);
				// Chiamata al Controller
				IFascicoloSige lCtrlFasSige = SIGELookupRemote.getFascicoloSigeRemote();
				lCtrlFasSige.ExUpdateDataDefinizione(aFasSige, lConn);
			}

			// Inserimento Provvedimento SIGE con l'IdEventoGenerato poco prima.
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lProvvedimento.setIdEventoGenerato(lEve.getEvento().getIdEvento());
			lProvDao.setDAOFromModel(lProvvedimento);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();
			lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);

			// lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciProvvedimento : Inserito Provvedimento -> " + lIdProvvedimento);

			// Valorizzazione campi Tenori.
			lTenori = aggiornaListaTenori(lTenori, lProvvedimento);

			// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExInserisciFissazioneUdienza : Fase di chiusura per il Tenore");

			ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
			lCtrlTen.ExInserisciOggetti(lTenori, aFasSige.getIdFascicoloSige(), lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIGEException("UdienzaSigeRuoloController.ExInserisciFissazioneUdienza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIGEException("UdienzaSigeRuoloController.ExInserisciFissazioneUdienza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUdiDao);
			cleanup(lProvDao);
			cleanup(lTenDao);
			cleanup(lTenoreSqlDao);
			cleanup(lFasSigeDAO);
			cleanup(lConn);
		}

		return aNuovaUdienzaProc;
	}

	private void ExCancellaNotifica(Connection lConn, NotificaModel aNotifica) throws F3BException {
		NotificaDAO lNotDao = null;

		try {
			lNotDao = new NotificaDAO(lConn);
			lNotDao.setCondizioneUpdate(aNotifica.getIdNotifica());
			lNotDao.delete();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("UdienzaSigeRuoloController.ExCancellaNotifica:  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("UdienzaSigeRuoloController.ExCancellaNotifica:  " + e);
		} finally {
			cleanup(lNotDao);
		}
	}

	private void ExRipulisciNotificheByKeyEvento(Connection lConn, BigDecimal aKey) throws F3BException {

		Vector<NotificaModel> lNotifici = new Vector<NotificaModel>();
		NotificaSqlDAO lNotDao = null;

		try {
			lNotDao = new NotificaSqlDAO(lConn);
			// lNotDao.ricercaNotificaByEvento(aKey);
			lNotDao.ricercaNotificheByEventoNotParti(aKey);
			lNotifici = new Vector(lNotDao.getModels());

			for (NotificaModel nm : lNotifici) {
				ExCancellaNotifica(lConn, nm);
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("UdienzaSigeRuoloController.ExRipulisciNotificheByKeyEvento: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("UdienzaSigeRuoloController.ExRipulisciNotificheByKeyEvento: " + e);
		} finally {
			cleanup(lNotDao);
		}

	}

	private void ExInserisciNotificheByKeyEvento(Connection lConn, BigDecimal lKeyEvento,
			NotificaModel[] updNotifiche) throws F3BException, DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Presenti " + updNotifiche.length + " notifiche");

		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		try {
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);

			int count = 0;
			BigDecimal lKeyAutorita = null;
			while (count < updNotifiche.length) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Notifica[" + count + "] = " + updNotifiche[count]);

				NotificaModel nm = updNotifiche[count];
				if (nm != null) {

					if (nm.getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(nm.getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(nm.getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							nm.setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							nm.setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					nm.setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(nm);
					lNotDao.insert();
					lNotDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserito evento" + lKeyEvento);
				}
				count++;
			}

		} catch (DAOException daoEx) {
			throw new F3BException("UdienzaSigeRuoloController.ExInserisciNotificheByKeyEvento: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("UdienzaSigeRuoloController.ExInserisciNotificheByKeyEvento: " + ex);
		} finally {
			cleanup(lAutDao);
			cleanup(lNotDao);
		}

	}

	/**
	 * Modifica Fissazione Udienza
	 * <p>
	 * 
	 * @param aNuovaUdienzaProc
	 * @param aFasSige
	 * @param aEve
	 * @param lProvvedimento
	 * @param aVecchiaUdienzaProc
	 * @param lTenori
	 * @return
	 * @throws F3BException
	 */
	public UdienzaProcedimentoSigeModel ExModificaFissazioneUdienza(
			UdienzaProcedimentoSigeModel aNuovaUdienzaProc, FascicoloSigeModel aFasSige,
			EventoNotificaModel aEve, ProvvedimentoSigeModel lProvvedimento, Vector lTenori,
			UdienzaProcedimentoSigeModel aVecchiaUdienzaProc) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		TenoreSigeDAO lTenDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		TenoreSigeSqlDAO lTenoreSqlDao = null;
		FascicoloSigeDAO lFasSigeDAO = null;
		UdienzaProcedimentoSigeDAO lUdiDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;

		if (aFasSige == null || aFasSige.getIdFascicoloSige() == null)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Impossibile aggiornare il Fascicolo SIGE");

		try {
			// Prende una connessione in transazione.
			lConn = getDBTransaction();

			// Chiamata al Controller per aggiornare EVENTO
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			// EventoNotificaModel lEve = lCtrlEve.ExInserisciEventoNotifica(aEve, lConn);
			EventoModel updEvento = aEve.getEvento();
			updEvento.setIdEvento(aVecchiaUdienzaProc.getEveIdEvento());
			lCtrlEve.ExModificaEvento(updEvento);

			// Chiamata al Controller per aggiornare NOTIFICHE
			ExRipulisciNotificheByKeyEvento(lConn, updEvento.getIdEvento());
			NotificaModel[] updNotifiche = aEve.getNotifiche();
			ExInserisciNotificheByKeyEvento(lConn, updEvento.getIdEvento(), updNotifiche);

			// Aggiorna record in UDIENZA_PROCEDIMENTO_SIGE.
			lUdiDao = new UdienzaProcedimentoSigeDAO(lConn);
			aNuovaUdienzaProc
					.setIdUdienzaProcedimentoSige(aVecchiaUdienzaProc.getIdUdienzaProcedimentoSige());
			aNuovaUdienzaProc.seEveIdEvento(updEvento.getIdEvento());
			aNuovaUdienzaProc
					.setCodOperatoreAggiornamento(aVecchiaUdienzaProc.getCodOperatoreAggiornamento());
			aNuovaUdienzaProc.setCodUfficioAggiornamento(aVecchiaUdienzaProc.getCodUfficioAggiornamento());
			aNuovaUdienzaProc.setDataAggiornamento(aVecchiaUdienzaProc.getDataAggiornamento());
			// lUdiDao.setDAOFromModel(aNuovaUdienzaProc);
			// BigDecimal lKey = lUdiDao.insert();
			// lUdiDao.stop();
			// aNuovaUdienzaProc.setIdUdienzaProcedimentoSige(lKey);

			// Eventuale aggiornamento del vecchio record in UDIENZA_PROCEDIMENTO
			// if (aVecchiaUdienzaProc != null && aVecchiaUdienzaProc.getIdUdienzaProcedimentoSige() != null)
			// {
			// aVecchiaUdienzaProc.seEveIdEvento(lEve.getEvento().getIdEvento());
			lUdiDao.setDAOFromModelForUpdate(aNuovaUdienzaProc);
			lUdiDao.update();
			lUdiDao.stop();
			// }

			// Aggiornamento di FASCICOLO_SIGE (DATA_UDIENZA (impostata nella action) e COD_STATO_FASCICOLO).
			lFasSigeDAO = new FascicoloSigeDAO(lConn);
			lFasSigeDAO.setDAOFromModel(aFasSige);
			// Modifica del 14/11/2016
			// Quando lo stato del fascicolo è 14 (Opposizione - Accoglie (fissa l'udienza)) oppure
			// 16 (Ricorso convertito in opposizione (Fissa Udienza)), una qualsiasi modifica fatta
			// sull'udienza non deve modificarne lo stato
			if (aFasSige != null && aFasSige.getCodStatoFascicolo() != null
					&& (aFasSige.getCodStatoFascicolo().equals("14")
							|| aFasSige.getCodStatoFascicolo().equals("16"))) {
				// Non aggiorno lo stato
			} else {
				// Modifica del 27/02/2017
				// Richiesta fatta da Nunzia
				// Se sul fascicolo è stata fissata un'udienza lo stato del fascicolo
				// deve essere settato a 20 ---> "Decreto Fissazione Udienza"
				// lFasSigeDAO.setCodStatoFascicolo("02");
				lFasSigeDAO.setCodStatoFascicolo("20");
			}
			lFasSigeDAO.setCodUfficioAggiornamento(aFasSige.getCodUfficioAggiornamento());
			lFasSigeDAO.setCodOperatoreAggiornamento(aFasSige.getCodOperatoreAggiornamento());
			lFasSigeDAO.setDataAggiornamento(aFasSige.getDataAggiornamento());
			lFasSigeDAO.setCodTipoGiudizio(aFasSige.getCodTipoGiudizio());
			lFasSigeDAO.setCondizioneUpdate(aFasSige.getIdFascicoloSige());
			lFasSigeDAO.update();
			lFasSigeDAO.stop();

			// Mev 15: se la data emissione è valorizzata allora carico il provvedimento e i tenori
			if (lProvvedimento.getDataEmissione() != null) {

				ProvvedimentoSigeModel aProvModel = new ProvvedimentoSigeModel();
				aProvModel.setFasIdFascicoloSige(aFasSige.getIdFascicoloSige());
				aProvModel.setIdEventoGenerato(updEvento.getIdEvento());

				Vector<ProvvedimentoSigeModel> lProvvedimentiSige = new Vector();

				lProvSqlDao = new ProvvedimentoSigeSqlDAO(lConn);
				lProvSqlDao.ricercaProvvedimentoPerEvento(aProvModel);
				lProvSqlDao.start();
				while (lProvSqlDao.next()) {
					ProvvedimentoSigeModel lProvvModel = new ProvvedimentoSigeModel();
					lProvvModel = (ProvvedimentoSigeModel) lProvSqlDao.getModel();
					lProvvModel.decodifica();
					lProvvedimentiSige.add(lProvvModel);
				}

				lProvDao = new ProvvedimentoSigeDAO(lConn);
				if (lProvvedimentiSige.size() == 0) {
					// Inserimento Provvedimento SIGE con l'IdEventoGenerato poco prima.
					lProvvedimento.setIdEventoGenerato(updEvento.getIdEvento());
					lProvDao.setDAOFromModel(lProvvedimento);
					BigDecimal lIdProvvedimento = lProvDao.insert();
					lProvDao.stop();
					lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);

					// lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"ExInserisciProvvedimento : Inserito Provvedimento -> " + lIdProvvedimento);

					// Valorizzazione campi Tenori.
					lTenori = aggiornaListaTenori(lTenori, lProvvedimento);

					// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ExModificaFissazioneUdienza : Fase di chiusura per il Tenore");

					ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
					lCtrlTen.ExInserisciOggetti(lTenori, aFasSige.getIdFascicoloSige(), lConn);
				} else {
					// Aggiorno il Provvedimento SIGE con la nuova data emissione
					ProvvedimentoSigeModel psmUpd = lProvvedimentiSige.get(0);
					psmUpd.setDataEmissione(lProvvedimento.getDataEmissione());
					psmUpd.setLuogoSvolgimento(lProvvedimento.getLuogoSvolgimento());
					psmUpd.setDataAggiornamento(lProvvedimento.getDataInserimento());
					psmUpd.setCodOperatoreAggiornamento(lProvvedimento.getCodOperatoreInserimento());
					psmUpd.setCodUfficioAggiornamento(lProvvedimento.getCodUfficioInserimento());
					lProvDao.setDAOFromModelForUpdate(psmUpd);
					lProvDao.update();

					// 20190516 [SG]: cambiata gestione cancello e inserisco
					// Valorizzazione campi Tenori.
					lTenori = aggiornaListaTenori(lTenori, psmUpd);
					// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ExModificaFissazioneUdienza : Fase di chiusura per il Tenore");
					ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
					lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);
					lTenDao = new TenoreSigeDAO(lConn);
					// for (int i = 0; i < lTenori.size(); i++) {
					// Cancellazione dei record nella tabella TENORE_SENTENZA_REATO in relazione
					// con il tenore da cancellare
					// lTenSenReaDAO
					// .selCondizioneDelete(((TenoreSigeModel) lTenori.get(i)).getIdTenoreSige());
					lTenSenReaDAO.selCondizioneDeleteProvvedimento(psmUpd.getIdProvvedimentoSige());
					lTenSenReaDAO.delete();
					lTenSenReaDAO.stop();
					// }
					// Cancellazione del Tenore nella tabella TENORE_SIGE
					lTenDao.selCondizioneIdProvvedimento(psmUpd.getIdProvvedimentoSige());
					lTenDao.delete();
					lTenDao.stop();

					lCtrlTen.ExInserisciOggetti(lTenori, aFasSige.getIdFascicoloSige(), lConn);
				}

			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIGEException("UdienzaSigeRuoloController.ExModificaFissazioneUdienza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIGEException("UdienzaSigeRuoloController.ExModificaFissazioneUdienza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lUdiDao);
			cleanup(lProvSqlDao);
			cleanup(lProvDao);
			cleanup(lTenDao);
			cleanup(lTenoreSqlDao);
			cleanup(lFasSigeDAO);
			cleanup(lConn);
		}

		return aNuovaUdienzaProc;
	}

	/**
	 * Esecuzione stampa Fissazione Udienza
	 * <p>
	 * 
	 * @param lEvento
	 * @param lUfficio
	 * @param aUtenteModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaFissazioneUdienza(BigDecimal aIdFascicolo, EventoModel lEvento,
			String aCodUff, UtenteModel aUtenteModel) throws F3BException {
		ByteArrayOutputStream lByteArrayOut = null;
		EventoDAO lEveDao = null;
		Connection lConn = null;

		try {
			// Generazione documento di stampa
			IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();

			// Riempie l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO,
					ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO, ICostantiStampaSige.TREE_PROVVEDIMENTO,
					ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
					ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
					ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
					ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA,
					ICostantiStampaSige.TREE_PARTICIVILI };
			int aTipoStampa = ICostantiStampaSige.STAMPA_UDIENZA;

			TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, aCodUff,
					lEvento.getIdEvento());

			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lEvento.getTemIdTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug( ">>>>>> Generato il Documento ." );
			lEvento.setDocBlobIn(lByteArrayInput);

			// Inserisce il documento generato nel model di ritorno.
			// In esso inserisce il Nome del template di ritorno e il documento generato.

			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException(
					"UdienzaSigeRuoloController.ExStampaFissazioneUdienza: Non posso inserire il documento nell'evento : "
							+ daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + ex);
			throw new F3BException(
					"UdienzaSigeRuoloController.ExStampaFissazioneUdienza: Non posso inserire il documento nell'evento : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	@Override
	public void ExInserisciFissazioneUdienza(FascicoloSigeModel aFasSige, EventoNotificaModel aEve,
			ProvvedimentoSigeModel lProvvedimento, Vector lTenori) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		TenoreSigeDAO lTenDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		TenoreSigeSqlDAO lTenoreSqlDao = null;
		FascicoloSigeDAO lFasSigeDAO = null;

		if (aFasSige == null || aFasSige.getIdFascicoloSige() == null)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Impossibile aggiornare il Fascicolo SIGE");

		try {
			// Prende una connessione in transazione.
			lConn = getDBTransaction();

			// Chiamata al Controller per inserimento EVENTO NOTIFICA
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEve = lCtrlEve.ExInserisciEventoNotifica(aEve, lConn);

			// Aggiornamento di FASCICOLO_SIGE (DATA_UDIENZA (impostata nella action) e COD_STATO_FASCICOLO).
			lFasSigeDAO = new FascicoloSigeDAO(lConn);
			lFasSigeDAO.setDAOFromModel(aFasSige);
			lFasSigeDAO.setCodStatoFascicolo("02");
			lFasSigeDAO.setCodUfficioAggiornamento(aFasSige.getCodUfficioAggiornamento());
			lFasSigeDAO.setCodOperatoreAggiornamento(aFasSige.getCodOperatoreAggiornamento());
			lFasSigeDAO.setDataAggiornamento(aFasSige.getDataAggiornamento());
			lFasSigeDAO.setCodTipoGiudizio(aFasSige.getCodTipoGiudizio());
			lFasSigeDAO.setCondizioneUpdate(aFasSige.getIdFascicoloSige());
			lFasSigeDAO.update();
			lFasSigeDAO.stop();

			// Mev 15: se la data emissione è valorizzata allora carico il provvedimento e i tenori
			if (lProvvedimento.getDataEmissione() != null) {
				// Inserimento Provvedimento SIGE con l'IdEventoGenerato poco prima.
				lProvDao = new ProvvedimentoSigeDAO(lConn);
				lProvvedimento.setIdEventoGenerato(lEve.getEvento().getIdEvento());
				lProvDao.setDAOFromModel(lProvvedimento);
				BigDecimal lIdProvvedimento = lProvDao.insert();
				lProvDao.stop();
				lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);

				// lProvvedimento.setIdProvvedimentoSige(lIdProvvedimento);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ExInserisciProvvedimento : Inserito Provvedimento -> " + lIdProvvedimento);

				// Valorizzazione campi Tenori.
				lTenori = aggiornaListaTenori(lTenori, lProvvedimento);

				// Aggiornamento Tenori demandata alla funzione TenoreSigeController.ExInserisciOggetti.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ExInserisciFissazioneUdienza : Fase di chiusura per il Tenore");

				ITenoreSige lCtrlTen = SIGELookupRemote.getTenoreSigeRemote();
				lCtrlTen.ExInserisciOggetti(lTenori, aFasSige.getIdFascicoloSige(), lConn);
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);

			// Controlla l'esistenza di un procedimento per l'udienza richiesta.
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Esiste già un procedimento per l'udienza richiesta!");

			throw new SIGEException("UdienzaSigeRuoloController.ExInserisciFissazioneUdienza: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIGEException("UdienzaSigeRuoloController.ExInserisciFissazioneUdienza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lProvDao);
			cleanup(lTenDao);
			cleanup(lTenoreSqlDao);
			cleanup(lFasSigeDAO);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * 20170913: [SG] aggiunta query
	 */
	public Vector<Object> cercaCollegi(Date dataUdienza, String codUfficioAppartenenza, String codMagis,
			BigDecimal idSezione, String tipoRito, String idCollegio) throws F3BException {

		Connection lConn = null;
		Vector collegi = new Vector();
		UdienzaSigeSqlDAO lUdiSqlDao = null;

		try {
			lConn = getDBConnection();
			lUdiSqlDao = new UdienzaSigeSqlDAO(lConn);
			BigDecimal bd = Utils.isNullObj(idSezione) ? new BigDecimal(0) : idSezione;
			lUdiSqlDao.cercaCollegi(dataUdienza, codUfficioAppartenenza, codMagis, bd, tipoRito, idCollegio);
			collegi = new Vector(lUdiSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.debug("UdienzaSigeRuoloController.cercaCollegi: " + daoEx);
			throw new SIGEException("UdienzaSigeRuoloController.cercaCollegi: " + daoEx);
		} finally {
			cleanup(lUdiSqlDao);
			cleanup(lConn);
		}
		return collegi;
	}

}