package siap.sico.libertaanticipata.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoLicenzePeriodiModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.permesso.dao.EventoPermessoLicenzaDAO;
import siap.sius.permesso.dao.EventoPermessoLicenzaSqlDAO;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * LicenzaPeriodiLibAnticipataController - Classe Controller per l'accesso a LICENZA_LIBAANTICIPATA e
 * PERIODO_LIBANTICIPATA.
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LicenzaPeriodiLibAnticipataController extends SiapController
		implements ILicenzaPeriodiLibAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public LicenzaPeriodiLibAnticipataModel[] ExInserisciLicenzeLibanticipata(
			LicenzaPeriodiLibAnticipataModel[] aLicenze, Connection aConn) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" CONTROLLER exins - Ord, LA -  ExInserisciLicenzeLibanticipata - inizio ");
		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibAnticipataModel lLicMod = null;
		BigDecimal lKeyLic = null;

		PeriodoLibanticipataDAO lPerDao = null;
		PeriodoLibAnticipataModel lPerMod = null;
		BigDecimal lKeyPer = null;
		PeriodoLibAnticipataModel[] lPeriodi = null;

		if (aLicenze == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Licenza da Inserire");

		try {
			lLicDao = new LicenzaLibanticipataDAO(aConn);
			lPerDao = new PeriodoLibanticipataDAO(aConn);

			for (int i = 0; i < aLicenze.length; i++) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" CONTROLLER exins - Ord, LA - ExInserisciLicenzeLibanticipata - ciclo for
				// i = "+i);
				// Inserimento Licenza
				lLicMod = new LicenzaLibAnticipataModel(aLicenze[i].getLicenza());
				lLicDao.setDAOFromModel(lLicMod);
				lKeyLic = lLicDao.insert();
				lLicMod.setIdLicenzaLibanticipata(lKeyLic);
				aLicenze[i].setLicenza(lLicMod);
				lLicDao.stop();

				lPeriodi = aLicenze[i].getPeriodi();
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLic);
						lPerDao.setDAOFromModel(lPerMod);
						lKeyPer = lPerDao.insert();
						lPerDao.stop();
						lPerMod.setIdPeriodoLibanticipata(lKeyPer);
						aLicenze[i].getPeriodi()[j] = lPerMod;
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: mancano periodi");
				}
			}
		} finally {// sca
			cleanup(lLicDao);
			cleanup(lPerDao);
		}

		return aLicenze;
	}

	public LicenzaPeriodiLibAnticipataModel[] ExInserisciLicenzeLibanticipata(
			LicenzaPeriodiLibAnticipataModel[] aLicenze, BigDecimal aIdEvento) throws Exception {

		Connection lConn = null;

		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		DepositoOrdinanzaPcDAO lDepDao = null;

		LicenzaPeriodiLibAnticipataModel[] lLicenze;

		try {
			lConn = getDBTransaction();

			// Aggiornamento LICENZA_LIBANTICIPATA di ID_EVENTO
			for (int i = 0; i < aLicenze.length; i++) {
				LicenzaLibAnticipataModel lLibAnt = aLicenze[i].getLicenza();
				lLibAnt.setEveIdEvento(aIdEvento);
			}

			lLicenze = ExInserisciLicenzeLibanticipata(aLicenze, lConn);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: " + sqe);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: " + ex);
		} finally {
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lLicenze;
	}

	// NUOVA ORDINANZA L.A. - Decreto 2013/146
	public void ExInserisciNewLicenzeLibanticipata(LicenzaPeriodiLibAnticipataModel[] aLicenze,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_spe, LicenzaPeriodiLibAnticipataModel[] aLicenze_int,
			LicenzaLibAnticipataModel aLicenzaC, LicenzaLibAnticipataModel aLicenzaC_SPE,
			LicenzaLibAnticipataModel aLicenzaC_INT, BigDecimal aIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" CONTROLLER - Ord, LA -  ExInserisciNewLicenzeLibanticipata - inizio ");
		Connection lConn = null;

		try {
			lConn = getDBTransaction();

			// Aggiornamento LICENZA_LIBANTICIPATA di ID_EVENTO
			if (aLicenze != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" CONTROLLER - Ord, LA - ExInserisciNewLicenzeLibanticipata - lLicenze !=
				// null ");
				for (int i = 0; i < aLicenze.length; i++) {
					LicenzaLibAnticipataModel lLibAnt = aLicenze[i].getLicenza();
					lLibAnt.setEveIdEvento(aIdEvento);
				}
				/* lLicenze = */ExInserisciLicenzeLibanticipata(aLicenze, lConn);
			}

			// Aggiornamento LICENZA_LIBANTICIPATA di ID_EVENTO
			if (aLicenze_spe != null) {
				for (int i = 0; i < aLicenze_spe.length; i++) {
					LicenzaLibAnticipataModel lLibAnt = aLicenze_spe[i].getLicenza();
					lLibAnt.setEveIdEvento(aIdEvento);
				}
				/* lLicenze = */ExInserisciLicenzeLibanticipata(aLicenze_spe, lConn);
			}

			// Aggiornamento LICENZA_LIBANTICIPATA di ID_EVENTO
			if (aLicenze_int != null) {
				for (int i = 0; i < aLicenze_int.length; i++) {
					LicenzaLibAnticipataModel lLibAnt = aLicenze_int[i].getLicenza();
					lLibAnt.setEveIdEvento(aIdEvento);
				}
				/* lLicenze = */ExInserisciLicenzeLibanticipata(aLicenze_int, lConn);
			}

			// Aggiornamento LICENZA_LIBANTICIPATA di ID_EVENTO
			if (aLicenzaC != null) {
				aLicenzaC.setEveIdEvento(aIdEvento);
				/* licMod = */ExInserisciLicenzaLibanticipata(aLicenzaC, lConn);
			}

			if (aLicenzaC_SPE != null) {
				aLicenzaC_SPE.setEveIdEvento(aIdEvento);
				/* licMod = */ExInserisciLicenzaLibanticipata(aLicenzaC_SPE, lConn);
			}

			if (aLicenzaC_INT != null) {
				aLicenzaC_INT.setEveIdEvento(aIdEvento);
				/* licMod = */ExInserisciLicenzaLibanticipata(aLicenzaC_INT, lConn);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciNewLicenzeLibanticipata: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciNewLicenzeLibanticipata: " + sqe);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciNewLicenzeLibanticipata: " + ex);
		} finally {
			cleanup(lConn);
		}
	}

	public EventoModel ExInserisciLicenzeLibanticipataSIEP(OrdinanzaEventoTenoriModel aOrdEveTenMod,
			LicenzaPeriodiLibAnticipataModel[] aLicenze, LicenzaPeriodiLibAnticipataModel[] aLicenze_spe,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_int) throws Exception {

		Connection lConn = null;

		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		DepositoOrdinanzaPcDAO lDepDao = null;
		// LicenzaPeriodiLibAnticipataModel[] lLicenze;
		EventoModel lEvento = new EventoModel();

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					" ExInserisciLicenzeLibanticipataSIEP - Inserimento EVENTO, DEPOSITO_ORD_PC, TENORE");

			lConn = getDBTransaction();

			// Inserimento EVENTO
			lEveDao = new EventoDAO(lConn);
			lEvento = aOrdEveTenMod.getEvento();

			// Setto l'anno e il progressivo...
			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDAO.getProgressivo(lEvento);
			lEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
			lEveDao.setDAOFromModel(lEvento);
			BigDecimal lKeyEvento = lEveDao.insert();
			lEvento.setIdEvento(lKeyEvento);
			lEveDao.stop();

			// Inserimento DEPOSITO_ORDINANZA_PC
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);
			DepositoOrdinanzaPcModel lDepOrd = aOrdEveTenMod.getOrdinanza();

			lDepOrd.setIdEventoGenerato(lKeyEvento);

			lDepDao.setDAOFromModel(lDepOrd);
			BigDecimal lKeyDepOrd = lDepDao.insert();
			lDepOrd.setIdDepositoOrdinanzaPc(lKeyDepOrd);
			lDepDao.stop();

			// Inserimento TENORE
			lTenDao = new TenoreDAO(lConn);
			// TenoreModel[] lTenori = null;
			int num = aOrdEveTenMod.getTenori().length;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ExInserisciLicenzeLibanticipataSIEP - Tenori da INSERIRE = " + num);
			// lTenori = new TenoreModel[num];

			BigDecimal lKeyTenore;

			for (int i = 0; i < num; i++) {
				TenoreModel lTenMod = aOrdEveTenMod.getTenori()[i];

				lTenMod.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);
				lTenDao.setDAOFromModel(lTenMod);
				lKeyTenore = lTenDao.insert();
				lTenMod.setIdTenore(lKeyTenore);
				lTenDao.stop();
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" ExInserisciLicenzeLibanticipataSIEP - Inserito TENORE = "+lTenMod);
			}
			//
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ExInserisciLicenzeLibanticipataSIEP - Ins LIBERAZIONE_ANTICIPATA e PERIODI ");

			// Inserisce LIBERAZIONE_ANTICIPATA E relativi PERIODI
			if (aLicenze != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("- L.A. NORM - ExInserisciLicenzeLibanticipataSIEP - aLicenze != null = "
				// + aLicenze);
				for (int i = 0; i < aLicenze.length; i++) {
					LicenzaLibAnticipataModel lLibAnt = aLicenze[i].getLicenza();
					lLibAnt.setEveIdEvento(lKeyEvento);
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze, lConn);
			}
			//
			// Inserisce LIBERAZIONE_ANTICIPATA_SPECIALE E relativi PERIODI
			if (aLicenze_spe != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("-L.A. SPEC - ExInserisciLicenzeLibanticipataSIEP - aLicenze_spe != null =
				// "
				// + aLicenze_spe);
				for (int i = 0; i < aLicenze_spe.length; i++) {
					LicenzaLibAnticipataModel lLibAnt = aLicenze_spe[i].getLicenza();
					lLibAnt.setEveIdEvento(lKeyEvento);
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze_spe, lConn);
			}
			//
			// Inserisce LIBERAZIONE_ANTICIPATA_INTEGRAZIONEE E relativi PERIODI
			if (aLicenze_int != null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("-L.A. INTEG - ExInserisciLicenzeLibanticipataSIEP - aLicenze_int != null
				// = "
				// + aLicenze_int);
				for (int i = 0; i < aLicenze_int.length; i++) {
					LicenzaLibAnticipataModel lLibAnt = aLicenze_int[i].getLicenza();
					lLibAnt.setEveIdEvento(lKeyEvento);
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze_int, lConn);
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipataSIEP: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipataSIEP: " + sqe);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipataSIEP: " + ex);
		} finally {
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lEvento;
	} // Chiude ExInserisciLicenzeLibanticipataSIEP(...)
		// END Decreto 2013/146

	public LicenzaPeriodiLibAnticipataModel[] ExInserisciLicenzeLibanticipata(
			LicenzaPeriodiLibAnticipataModel[] aLicenze, OrdinanzaEventoTenoriModel aOrdEveTenMod)
			throws Exception {

		Connection lConn = null;

		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		DepositoOrdinanzaPcDAO lDepDao = null;

		LicenzaPeriodiLibAnticipataModel[] lLicenze;

		try {
			lConn = getDBTransaction();

			// Inserimento TENORE
			lTenDao = new TenoreDAO(lConn);
			TenoreModel lTenMod = aOrdEveTenMod.getTenori()[0];
			lTenDao.setDAOFromModel(lTenMod);
			BigDecimal lKeyTenore = lTenDao.insert();
			lTenMod.setIdTenore(lKeyTenore);
			lTenDao.stop();

			// Inserimento EVENTO
			lEveDao = new EventoDAO(lConn);
			EventoModel lEvento = aOrdEveTenMod.getEvento();

			lEvento.setTenIdTenore(lKeyTenore);

			// Setto l'anno e il progressivo...
			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDAO.getProgressivo(lEvento);
			lEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
			lEveDao.setDAOFromModel(lEvento);
			BigDecimal lKeyEvento = lEveDao.insert();
			lEvento.setIdEvento(lKeyEvento);
			lEveDao.stop();

			// Inserimento DEPOSITO_ORDINANZA_PC
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);
			DepositoOrdinanzaPcModel lDepOrd = aOrdEveTenMod.getOrdinanza();

			lDepOrd.setIdEventoGenerato(lKeyEvento);

			lDepDao.setDAOFromModel(lDepOrd);
			BigDecimal lKeyDepOrd = lDepDao.insert();
			lDepOrd.setIdDepositoOrdinanzaPc(lKeyDepOrd);
			lDepDao.stop();

			// Aggiornamento LICENZA_LIBANTICIPATA di ID_EVENTO
			for (int i = 0; i < aLicenze.length; i++) {
				LicenzaLibAnticipataModel lLibAnt = aLicenze[i].getLicenza();
				lLibAnt.setEveIdEvento(lKeyEvento);
			}

			// Aggiornamento TENORE di ID_DEPOSITO_ORDINANZA_PC
			lTenDao.setIdTenore(lKeyTenore);
			lTenDao.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);
			lTenDao.selByKey();
			lTenDao.update();
			lTenDao.stop();

			lLicenze = ExInserisciLicenzeLibanticipata(aLicenze, lConn);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: " + sqe);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzeLibanticipata: " + ex);
		} finally {
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lLicenze;
	}

	public Vector ExRicercaLicenzeLibanticipataByEve(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;

		Vector lLicenze = new Vector();
		ArrayList lPeriodi = null;
		Vector lLicenzePeriodi = new Vector();

		LicenzaLibanticipataSqlDAO lLicDao = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;

		Iterator lItx = null;
		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataByEve(aIdEvento);
			lLicenze = new Vector(lLicDao.getModels());
			if (lLicenze.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessuna Licenza LA trovata a sistema");
			} else {
				lItx = lLicenze.iterator();
				while (lItx.hasNext()) {
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					aModel.setLicenza((LicenzaLibAnticipataModel) lItx.next());
					// 10-03-2014 Nuova Ordinanza L.A : In caso di Concessione Periodo Unico (senza date
					// "Dal.. Al.. con solamente numero gg" )
					// POSSO AVERE UNA LIBERAZIONE ANTICIPATA SENZA CORRISPONDENTE PERIODO, e la contrassegno
					// con 'LSU', 'LAU', 'LIU'

					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("--> LicenzaPeriodoLibanticipataController - Prima di
					// GetDescStatopermesso");
					if (("LAU").equals(aModel.getLicenza().getDescrStatoPermesso())
							|| ("LSU").equals(aModel.getLicenza().getDescrStatoPermesso())
							|| ("LIU").equals(aModel.getLicenza().getDescrStatoPermesso())) {
						// Non sono presenti PERIODI
						lLicenzePeriodi.add(aModel);

					} else {
						// Ricerca dei Periodi relativi alla licenza
						lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
						lPerDao.ricercaPeriodoLibanticipataByLic(
								aModel.getLicenza().getIdLicenzaLibanticipata());
						lPeriodi = new ArrayList(lPerDao.getModels());
						aModel.setPeriodi((PeriodoLibAnticipataModel[]) lPeriodi
								.toArray(new PeriodoLibAnticipataModel[1]));
						lLicenzePeriodi.add(aModel);
						cleanup(lPerDao);
					}
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzeLibanticipataByEve: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lLicenzePeriodi;
	}

	public Vector ExRicercaLicenzeByEve(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;

		Vector lLicenze = new Vector();

		LicenzaLibanticipataSqlDAO lLicDao = null;

		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataByEve(aIdEvento);
			lLicenze = new Vector(lLicDao.getModels());
			if (lLicenze.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessun Elemento Licenza Anticipata trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzeByEve: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}

		return lLicenze;
	}

	// 20/05/2014 Nuova L.A. DL 146/2013
	public Vector ExRicercaLicenzeByEve(BigDecimal aIdEvento, String aTipoLA) throws F3BException {

		Connection lConn = null;

		Vector lLicenze = new Vector();

		LicenzaLibanticipataSqlDAO lLicDao = null;

		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataByEveTipoLA(aIdEvento, aTipoLA);
			lLicenze = new Vector(lLicDao.getModels());
			// if (lLicenze.size() == 0)
			// throw new F3BException(F3BException.USER_MESSAGE,
			// "Nessun Elemento Licenza Anticipata trovato");
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzeByEve + topoLA: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}

		return lLicenze;
	}
	// End DL 146/2013

	public LicenzaLibAnticipataModel ExInserisciLicenzaLibanticipata(
			LicenzaLibAnticipataModel aLicenzaLibanticipata) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataDAO lLicDao = null;

		LicenzaLibAnticipataModel lLicMod = null;

		try {
			lConn = getDBConnection();
			lLicMod = new LicenzaLibAnticipataModel(aLicenzaLibanticipata);
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lLicDao.setDAOFromModel(aLicenzaLibanticipata);
			BigDecimal lKey = null;
			lKey = lLicDao.insert();
			commit(lConn);
			lLicMod.setIdLicenzaLibanticipata(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}

		return lLicMod;
	}

	// 10-03-2014 Nuova Ordinanza L.A. - Decreto legge 146/2013
	public LicenzaLibAnticipataModel ExInserisciLicenzaLibanticipata(
			LicenzaLibAnticipataModel aLicenzaLibanticipata, Connection alconn) throws F3BException {

		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibAnticipataModel lLicMod = null;
		try {
			lLicMod = new LicenzaLibAnticipataModel(aLicenzaLibanticipata);
			lLicDao = new LicenzaLibanticipataDAO(alconn);
			lLicDao.setDAOFromModel(aLicenzaLibanticipata);
			BigDecimal lKey = null;
			lKey = lLicDao.insert();

			lLicMod.setIdLicenzaLibanticipata(lKey);
		} catch (DAOException ex) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController Connection.ExInserisci: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lLicDao);
		}

		return lLicMod;
	}

	public LicenzaLibAnticipataModel ExRicercaLicenzaLibanticipataUltimaByIDFascicoloSIEP(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicDao = null;

		LicenzaLibAnticipataModel lLicMod;

		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataUltimaByIDFascicoloSIEP(aKey);
			lLicMod = (LicenzaLibAnticipataModel) lLicDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzaLibanticipata: " + daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}

		return lLicMod;
	}

	public LicenzaLibAnticipataModel ExModificaLicenzaLibanticipata(
			LicenzaLibAnticipataModel aLicenzaLibanticipata) throws F3BException {

		Connection lConn = null;
		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibAnticipataModel lLicMod = new LicenzaLibAnticipataModel(aLicenzaLibanticipata);

		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lLicDao.setDAOFromModelForUpdate(aLicenzaLibanticipata);
			lLicDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExModificaLicenzaLibanticipata: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExModificaLicenzaLibanticipata : " + ex);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}
		return lLicMod;
	}

	/*****************************************************************************
	 *
	 ************************************************************************** */
	public LicenzaLibAnticipataModel ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(BigDecimal aKey,
			String aFlagElaborato) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicDao = null;

		LicenzaLibAnticipataModel lLicMod;

		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aKey, aFlagElaborato);
			lLicMod = (LicenzaLibAnticipataModel) lLicDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}

		return lLicMod;
	}

	public LicenzaLibAnticipataModel ExRicercaLicenzaLibanticipataByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicDao = null;

		LicenzaLibAnticipataModel lLicMod;

		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataByKey(aKey);
			lLicMod = (LicenzaLibAnticipataModel) lLicDao.getModelByKey();

			lLicDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzaLibanticipataByKey: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}

		return lLicMod;
	}

	public List ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicDao = null;
		EventoDAO lEveDao = null;

		List lLicenze = null;

		try {
			lConn = getDBConnection();

			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataConcesseDepositate(aIdFascicolo, aFlagElaborato);
			lLicenze = new ArrayList(lLicDao.getModels());

			// Questa parte serve a valorizzare un attributo sul model della LA
			// per memorizzare la presenza eventuale di un documento validato
			// (utile per le "comunicazioni")
			if (!lLicenze.isEmpty()) {
				lEveDao = new EventoDAO(lConn);

				Iterator iter = lLicenze.iterator();
				while (iter.hasNext()) {
					LicenzaLibAnticipataModel item = (LicenzaLibAnticipataModel) iter.next();

					lEveDao.selCondizioneEveIdEvento(item.getEveIdEvento());
					lEveDao.start();
					if (lEveDao.next()) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("item : " + item.isConProvvedimentoValidato());
						EventoModel lEveMod = new EventoModel();
						lEveMod.setFlagDocumentoRegistrato(lEveDao.getFlagDocumentoRegistrato());

						if (lEveMod != null && "S".equals(lEveMod.getFlagDocumentoRegistrato())) {
							item.setConProvvedimentoValidato(true);
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug("item : " + item.isConProvvedimentoValidato());
						}
					}
					lEveDao.stop();

				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP: "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lLicenze;
	}

	public List ExRicercaLicenzaScomputiConcessiDepositatiByIdFascicoloSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicDao = null;
		EventoDAO lEveDao = null;

		List lLicenze = null;

		try {
			lConn = getDBConnection();

			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaScomputiConcessiDepositati(aIdFascicolo, aFlagElaborato);
			lLicenze = new ArrayList(lLicDao.getModels());

			// Questa parte serve a valorizzare un attributo sul model della LA
			// per memorizzare la presenza eventuale di un documento validato
			// (utile per le "comunicazioni")
			if (!lLicenze.isEmpty()) {
				lEveDao = new EventoDAO(lConn);

				Iterator iter = lLicenze.iterator();
				while (iter.hasNext()) {
					LicenzaLibAnticipataModel item = (LicenzaLibAnticipataModel) iter.next();

					lEveDao.selCondizioneEveIdEvento(item.getEveIdEvento());
					lEveDao.start();
					if (lEveDao.next()) {
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.debug("item : " + item.isConProvvedimentoValidato());
						EventoModel lEveMod = new EventoModel();
						lEveMod.setFlagDocumentoRegistrato(lEveDao.getFlagDocumentoRegistrato());

						if (lEveMod != null && "S".equals(lEveMod.getFlagDocumentoRegistrato())) {
							item.setConProvvedimentoValidato(true);
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug("item : " + item.isConProvvedimentoValidato());
						}
					}
					lEveDao.stop();

					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("LA : " + item);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP: "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lLicenze;
	}

	public Vector ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(BigDecimal aKey,
			BigDecimal aKeyEvento) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicDao = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;

		List lLicenze = null;
		Vector lLicenzePeriodi = new Vector();

		try {
			lConn = getDBConnection();

			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(aKey, aKeyEvento);
			lLicenze = new ArrayList(lLicDao.getModels());

			if (lLicenze != null && !lLicenze.isEmpty()) {
				Iterator lIterLicenze = lLicenze.iterator();

				while (lIterLicenze.hasNext()) {
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					aModel.setLicenza((LicenzaLibAnticipataModel) lIterLicenze.next());

					// Ricerca dei Periodi relativi alla licenza
					lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
					lPerDao.ricercaPeriodoLibanticipataByLic(aModel.getLicenza().getIdLicenzaLibanticipata());

					ArrayList lPeriodi = new ArrayList(lPerDao.getModels());
					aModel.setPeriodi(
							(PeriodoLibAnticipataModel[]) lPeriodi.toArray(new PeriodoLibAnticipataModel[1]));

					lLicenzePeriodi.add(aModel);
					cleanup(lPerDao);
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lLicenzePeriodi;
	}

	/**
	 * Aggiorna tutte le LA associate al Fascicolo con FLAG_ELABORATO = aFlagElaboratoVecchio modifcandolo in
	 * aFlagElaboratoNuovo
	 *
	 * @param aKeyFascicolo
	 * @param aFlagElaboratoVecchio
	 * @param aFlagElaboratoNuovo
	 * @throws F3BException
	 */
	public void ExModificaFlagElaboratoLicenzaLibanticipataByIdFascicoloSiep(BigDecimal aKeyFascicolo,
			String aFlagElaboratoVecchio, String aFlagElaboratoNuovo) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		try {
			lConn = getDBConnection();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicSqlDao.updateFlagElaboratoByIdFascicolo(aKeyFascicolo, aFlagElaboratoVecchio,
					aFlagElaboratoNuovo);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExModificaFlagElaboratoLicenzaLibanticipataByIdFascicoloSiep : "
							+ ex);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lConn);
		}
	}

	public int ExTotalePeriodiConcessiNonElaboratiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		List lLicenze = new ArrayList();

		int lTotGiorni = 0;

		try {
			lConn = getDBConnection();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			lLicSqlDao.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aIdFascicoloSiep, "N");

			lLicenze = new ArrayList(lLicSqlDao.getModels());

			LicenzaLibAnticipataModel lLicModel;
			if (lLicenze != null && !lLicenze.isEmpty()) {
				Iterator lIter = lLicenze.iterator();

				while (lIter.hasNext()) {
					lLicModel = (LicenzaLibAnticipataModel) lIter.next();

					lTotGiorni += lLicModel.getNumeroGiorni().intValue();
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExTotalePeriodiConcessiByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lConn);
		}

		return lTotGiorni;
	}

	public int ExTotalePeriodiConcessiElaboratiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		List lLicenze = new ArrayList();

		int lTotGiorni = 0;

		try {
			lConn = getDBConnection();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			// Per tenere conto della forzatura della LA è stato aggiunto il flag 'F'
			lLicSqlDao.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aIdFascicoloSiep, "SF");

			lLicenze = new ArrayList(lLicSqlDao.getModels());

			LicenzaLibAnticipataModel lLicModel;
			if (lLicenze != null && !lLicenze.isEmpty()) {
				Iterator lIter = lLicenze.iterator();

				while (lIter.hasNext()) {
					lLicModel = (LicenzaLibAnticipataModel) lIter.next();

					lTotGiorni += lLicModel.getNumeroGiorni().intValue();

					if ("F".equals(lLicModel.getFlagElaborato()))
						break;
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExTotalePeriodiConcessiByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lConn);
		}

		return lTotGiorni;
	}

	public int ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		List lLicenze = new ArrayList();

		int lTotGiorni = 0;

		try {
			lConn = getDBConnection();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			lLicSqlDao.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aIdFascicoloSiep, "E");

			lLicenze = new ArrayList(lLicSqlDao.getModels());

			LicenzaLibAnticipataModel lLicModel;
			if (lLicenze != null && !lLicenze.isEmpty()) {
				Iterator lIter = lLicenze.iterator();

				while (lIter.hasNext()) {
					lLicModel = (LicenzaLibAnticipataModel) lIter.next();

					lTotGiorni += lLicModel.getNumeroGiorni().intValue();
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lConn);
		}

		return lTotGiorni;
	}

	public int ExTotalePeriodiConcessiElaboratiByEveIdEvento(BigDecimal aEveIdEvento) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		List lLicenze = new ArrayList();

		int lTotGiorni = 0;

		try {
			lConn = getDBConnection();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			lLicSqlDao.ricercaLicenzaLibanticipataConcessaByEve(aEveIdEvento);

			lLicenze = new ArrayList(lLicSqlDao.getModels());

			LicenzaLibAnticipataModel lLicModel;
			if (lLicenze != null && !lLicenze.isEmpty()) {
				Iterator lIter = lLicenze.iterator();

				while (lIter.hasNext()) {
					lLicModel = (LicenzaLibAnticipataModel) lIter.next();

					lTotGiorni += lLicModel.getNumeroGiorni().intValue();
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lConn);
		}

		return lTotGiorni;
	}

	/*****************************************************************************
	 * Restituisce il totale giorni di licenze di Liberazione Anticipata Concessi e con flag elaborato (null,
	 * E o N) (non computati in una pena residua validata)
	 *
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public int ExTotalePeriodiConcessiNonValidatiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;

		List lLicenze = new ArrayList();

		int lTotGiorni = 0;

		try {
			lConn = getDBConnection();

			lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			lLicSqlDao.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP(aIdFascicoloSiep, "NE");

			lLicenze = new ArrayList(lLicSqlDao.getModels());

			LicenzaLibAnticipataModel lLicModel;
			if (lLicenze != null && !lLicenze.isEmpty()) {
				Iterator lIter = lLicenze.iterator();

				while (lIter.hasNext()) {
					lLicModel = (LicenzaLibAnticipataModel) lIter.next();

					lTotGiorni += lLicModel.getNumeroGiorni().intValue();
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lConn);
		}

		return lTotGiorni;
	}

	public EventoModel ExUpdateValidaComunicazioneLA(EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSql = null;
		NomeProvvedimentoDAO lNomProvDao = null;
		DepositoOrdinanzaPcDAO lDepDAO = null;
		DepositoOrdinanzaPcSqlDAO lDepSql = null;

		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			lEveSql = new EventoSqlDAO(lConn);
			// * Cerca L'EVENTO *
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
			}
			lEveDao.stop();

			// ************************* EVENTO ORDINANZA ****************************
			// Cerca l'evento legato alla Liberazione Anticipata se presente
			EventoModel lEveOrdinanza = null;

			if (lEveApp.getEveIdEvento() != null) {
				lEveSql.ricercaEventoByKey(lEveApp.getEveIdEvento());
				lEveOrdinanza = (EventoModel) lEveSql.getModelByKey();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lEveOrdinanza legato alla LA: " + lEveOrdinanza);

			if (lEveOrdinanza != null) {
				lEveDao.setIdEvento(lEveOrdinanza.getIdEvento());
				lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				lDepSql = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepSql.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveOrdinanza.getIdEvento());
				DepositoOrdinanzaPcModel lDepMod = (DepositoOrdinanzaPcModel) lDepSql.getModelByKey();

				if (lDepMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("lDepMod legato all'Ordinanza: " + lEveOrdinanza);

					lDepDAO = new DepositoOrdinanzaPcDAO(lConn);

					lDepDAO.setIdDepositoOrdinanzaPc(lDepMod.getIdDepositoOrdinanzaPc());
					lDepDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lDepDAO.setFlagElaborato("S");
					lDepDAO.selByKey();
					lDepDAO.update();
					lDepDAO.stop();
				}
			}
			// ************************ FINE EVENTO ORDINANZA ************************

			// ************************ NOME PROVVEDIMENTO ***************************
			NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			lNomProvDao = new NomeProvvedimentoDAO(lConn);

			lNomProvMod.setCodNomeProvvedimento("NP209");

			lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			lNomProvDao.setDAOFromModel(lNomProvMod);
			lNomProvDao.insert();

			// ************************ FINE NOME PROVVEDIMENTO **********************

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaComunicazioneLA : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaComunicazioneLA : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSql);
			cleanup(lDepDAO);
			cleanup(lDepSql);
			cleanup(lNomProvDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	public void ExCancellaFungibilitaLicenzeLibanticipata(PenaResiduaModel aPenaResidua,
			BigDecimal aIdFungibilita) throws F3BException {

		Connection lConn = null;

		FungibilitaDAO lFunDao = null;
		PenaResiduaDAO lPenDao = null;

		try {
			lConn = getDBTransaction();

			lFunDao = new FungibilitaDAO(lConn);
			lFunDao.setCondizioneUpdate(aIdFungibilita);
			lFunDao.delete();

			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDataFine(aPenaResidua.getDataFine());
			lPenDao.setCondizioneUpdate(aPenaResidua.getIdPenaResidua());
			lPenDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExCancellaFungibilitaLicenzeLibanticipata : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExCancellaFungibilitaLicenzeLibanticipata : "
							+ ex);
		} finally {
			cleanup(lFunDao);
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaLicenzeLibanticipataByEve(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataDAO lLicDao = null;

		try {
			lConn = getDBTransaction();

			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lLicDao.setCondizioneIdEvento(aIdEvento);
			lLicDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExCancellaLicenzeLibanticipataByEve : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExCancellaLicenzeLibanticipataByEve : " + ex);
		} finally {
			cleanup(lLicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua l'inserimento di un Elenco LICENZA_LIB_ANTICIPATA e relativi PERIODI <br>
	 * 23/06/2008 Aggiunta del vettore EventoPermessoLicenza
	 *
	 * @param aLicenze
	 *            - Elenco LicenzePeriodiLibAnticipata
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciLicenzePeriodiLibAnticipataWithoutSequence(ArrayList aLicenzePeriodi,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		LicenzaPeriodiLibAnticipataModel lLicPerMod = null;
		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibAnticipataModel lLicMod = null;

		PeriodoLibanticipataDAO lPerDao = null;
		PeriodoLibAnticipataModel lPerMod = null;
		PeriodoLibAnticipataModel[] lPeriodi = null;

		// 23/06/2008 Aggiunta di EventoPermessoLicenza.
		EventoPermessoLicenzaDAO lEPLDao = null;
		EventoPermessoLicenzaModel lEPLMod = null;
		EventoPermessoLicenzaModel[] lEPL = null;

		try {
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lPerDao = new PeriodoLibanticipataDAO(lConn);
			lEPLDao = new EventoPermessoLicenzaDAO(lConn); // 23/06/2008

			if (aLicenzePeriodi != null && aLicenzePeriodi.size() > 0) {
				for (int i = 0; i < aLicenzePeriodi.size(); i++) {
					lLicPerMod = (LicenzaPeriodiLibAnticipataModel) aLicenzePeriodi.get(i);
					lLicMod = lLicPerMod.getLicenza();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.info("Licenza Lib. Anticipata da inserire = " + lLicMod);
					if (lLicMod != null && lLicMod.getIdLicenzaLibanticipata() != null) {
						lLicDao.setDAOFromModel(lLicMod);
						lLicDao.setWithoutSequence(true);
						lLicDao.insert();
						lLicDao.stop();
					}

					if (!Utils.isNullObj(lLicPerMod.getPeriodi())) {
						lPeriodi = lLicPerMod.getPeriodi();
						// Inserimento Periodi
						for (int j = 0; j < lPeriodi.length; j++) {
							if (!Utils.isNullObj(lPeriodi[j])) {
								lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
								lPerDao.setDAOFromModel(lPerMod);
								lPerDao.setWithoutSequence(true);
								lPerDao.insert();
								lPerDao.stop();
							}
						}
					} else
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info(
								"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzePeriodiLibAnticipata: mancano periodi");

					// 23/06/2008 Inserimento EventoPermessoLicenza
					if (!Utils.isNullObj(lLicPerMod.getEventiPermLic())) {
						lEPL = lLicPerMod.getEventiPermLic();
						for (int j = 0; j < lEPL.length; j++) {
							if (!Utils.isNullObj(lEPL[j])) {
								lEPLMod = new EventoPermessoLicenzaModel(lEPL[j]);
								lEPLDao.setDAOFromModel(lEPLMod);
								lEPLDao.setWithoutSequence(true);
								lEPLDao.insert();
								lEPLDao.stop();
							}
						}
					} else
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info(
								"LicenzaPeriodiLibAnticipataController.ExInserisciLicenzePeriodiLibAnticipata: mancano EventoPermessoLicenza");
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("LicenzaPeriodiLibAnticipata gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(
						F3BException.USER_MESSAGE + " Impossibile inserire la LicenzaPeriodiLibAnticipata! ");
			}
		} finally {
			cleanup(lLicDao);
			cleanup(lPerDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEPLDao);
		}
		return lCodEsito;
	}

	/**
	 * 18/12/2007 Effettua la ricerca dell'Elenco di LICENZA_LIB_ANTICIPATA e relativi PERIODI riferiti al
	 * Fascicolo SIEP <br>
	 * 26/06/2008 Aggiunto il vettore degli EventoPermessoLicenza come attributo delle
	 * LicenzaPeriodiLibAnticipataModel.
	 *
	 * @param aIdFascicolo
	 *            - Id Fascicolo SIEP di riferimento
	 * @return Vector - Vettore di LicenzaPeriodiLibAnticipataModel
	 */
	public Vector ExRicercaLicenzeLibanticipataByIdFascicoloSIEP(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		Vector lLicenze = new Vector();
		ArrayList lPeriodi = null;
		Vector lLicenzePeriodi = new Vector();

		ArrayList lEventiPL = null; // 26/06/2008
		Vector lEventiPermLic = new Vector(); // 26/06/2008

		LicenzaLibanticipataSqlDAO lLicDao = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;
		EventoPermessoLicenzaSqlDAO lEvePerLicSqlDao = null; // 23/06/2008

		Iterator lItx = null;
		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicDao.ricercaLicenzaLibanticipataByIdFascicoloSiep(aIdFascicolo);
			lLicenze = new Vector(lLicDao.getModels());
			if (lLicenze.size() == 0)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("Nessun Elemento Licenza trovato");
			else {
				lItx = lLicenze.iterator();
				while (lItx.hasNext()) {
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					aModel.setLicenza((LicenzaLibAnticipataModel) lItx.next());

					// Ricerca dei Periodi relativi alla licenza
					lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
					lPerDao.ricercaPeriodoLibanticipataByLic(aModel.getLicenza().getIdLicenzaLibanticipata());
					lPeriodi = new ArrayList(lPerDao.getModels());
					aModel.setPeriodi(
							(PeriodoLibAnticipataModel[]) lPeriodi.toArray(new PeriodoLibAnticipataModel[1]));
					lLicenzePeriodi.add(aModel);
					cleanup(lPerDao);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("Licenza - > " + aModel);

					// 23/06/2008 Ricerca degli eventi permessi licenze.
					lEvePerLicSqlDao = new EventoPermessoLicenzaSqlDAO(lConn);
					lEvePerLicSqlDao.ricercaEventoPermessoLicenzaByKeyLicLib(
							aModel.getLicenza().getIdLicenzaLibanticipata());
					lEventiPL = new ArrayList(lEvePerLicSqlDao.getModels());
					aModel.setEventiPermLic((EventoPermessoLicenzaModel[]) lEventiPL
							.toArray(new EventoPermessoLicenzaModel[1]));
					lEventiPermLic.add(aModel);
					cleanup(lEvePerLicSqlDao);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("EventoPermessoLicenza - > " + aModel);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzeLibanticipataByEve: " + daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lPerDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEvePerLicSqlDao);
			cleanup(lConn);
		}
		return lLicenzePeriodi;
	}

	public BigDecimal ExInserisciRidimLibanticipata(EventoModel aEvento, CampoNotaModel aCampoNote,
			LicenzaPeriodiLibAnticipataModel aLicenzaLA, LicenzaPeriodiLibAnticipataModel aLicenzaLASPE,
			LicenzaPeriodiLibAnticipataModel aLicenzaLAINT) throws Exception {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		CampoNotaDAO lCampoNoteDAO = null;

		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibAnticipataModel lLicMod = null;
		BigDecimal lKeyLic = null;

		LicenzaLibAnticipataModel lLicModSPE = null;
		BigDecimal lKeyLicLS = null;

		LicenzaLibAnticipataModel lLicModINT = null;
		BigDecimal lKeyLicLI = null;

		PeriodoLibanticipataDAO lPerDao = null;
		PeriodoLibAnticipataModel lPerMod = null;
		BigDecimal lKeyPer = null;
		PeriodoLibAnticipataModel[] lPeriodi = null;

		if (!(aLicenzaLA != null && aLicenzaLA.getLicenza() != null)
				&& !(aLicenzaLASPE != null && aLicenzaLASPE.getLicenza() != null)
				&& !(aLicenzaLAINT != null && aLicenzaLAINT.getLicenza() != null)) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Licenza da Inserire");
		}

		EventoModel lEvento = null;
		try {
			lConn = getDBTransaction();

			// Inserimento EVENTO
			lEveDao = new EventoDAO(lConn);
			lEvento = new EventoModel(aEvento);
			lEveDao.setDAOFromModel(lEvento);
			BigDecimal lKeyEvento = lEveDao.insert();
			lEvento.setIdEvento(lKeyEvento);
			lEveDao.stop();

			// Inserisco il campo note
			if (aCampoNote != null && aCampoNote.getDescr() != null && !aCampoNote.getDescr().equals("")) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Inserisco il campo note....");
				aCampoNote.setEveIdEvento(lKeyEvento);

				lCampoNoteDAO = new CampoNotaDAO(lConn);
				lCampoNoteDAO.setDAOFromModel(aCampoNote);
				lCampoNoteDAO.insert();
				lCampoNoteDAO.stop();
			}

			// Inserisco le LA
			if (aLicenzaLA != null && aLicenzaLA.getLicenza() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lPerDao = new PeriodoLibanticipataDAO(lConn);

				lLicMod = new LicenzaLibAnticipataModel(aLicenzaLA.getLicenza());
				lLicMod.setEveIdEvento(lKeyEvento);
				lLicDao.setDAOFromModel(lLicMod);
				lKeyLic = lLicDao.insert();
				lLicMod.setIdLicenzaLibanticipata(lKeyLic);
				aLicenzaLA.setLicenza(lLicMod);
				lLicDao.stop();

				lPeriodi = aLicenzaLA.getPeriodi();
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("-------->>>>>>> L.A. periodi size = "+ lPeriodi.length );
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLic);
						lPerDao.setDAOFromModel(lPerMod);
						lKeyPer = lPerDao.insert();
						lPerDao.stop();
						lPerMod.setIdPeriodoLibanticipata(lKeyPer);
						aLicenzaLA.getPeriodi()[j] = lPerMod;
					}
				}
			}

			// Inserisco la L.A. SPECIALE
			if (aLicenzaLASPE != null && aLicenzaLASPE.getLicenza() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lPerDao = new PeriodoLibanticipataDAO(lConn);

				lLicModSPE = new LicenzaLibAnticipataModel(aLicenzaLASPE.getLicenza());
				lLicModSPE.setEveIdEvento(lKeyEvento);
				lLicDao.setDAOFromModel(lLicModSPE);
				lKeyLicLS = lLicDao.insert();
				lLicDao.stop();

				lPeriodi = aLicenzaLASPE.getPeriodi();
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLicLS);
						lPerDao.setDAOFromModel(lPerMod);
						lPerDao.insert();
						lPerDao.stop();
					}
				}

			}

			// Inserisco la L.A. INTEGRAZIONE
			if (aLicenzaLAINT != null && aLicenzaLAINT.getLicenza() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lPerDao = new PeriodoLibanticipataDAO(lConn);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Lic LAINT - gg = " + aLicenzaLAINT.getLicenza().getNumeroGiorni());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Lic LAINT - tipo = " + aLicenzaLAINT.getLicenza().getDescrStatoPermesso());

				lLicModINT = new LicenzaLibAnticipataModel(aLicenzaLAINT.getLicenza());
				lLicModINT.setEveIdEvento(lKeyEvento);
				lLicDao.setDAOFromModel(lLicModINT);
				lKeyLicLI = lLicDao.insert();
				// lLicMod.setIdLicenzaLibanticipata(lKeyLic);
				// aLicenzaLA.setLicenza(lLicMod);
				lLicDao.stop();

				lPeriodi = aLicenzaLAINT.getPeriodi();
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLicLI);
						lPerDao.setDAOFromModel(lPerMod);
						lPerDao.insert();
						lPerDao.stop();
					}
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciRidimLibanticipata: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciRidimLibanticipata: " + ex);
		} finally {
			cleanup(lEveDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lCampoNoteDAO);
			cleanup(lLicDao);
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lEvento.getIdEvento();
	}

	/* Febbraio 2011 per lettura provv. Sorveglianza nel ridimensionamento LA */
	public Vector ExRicercaLAPeriodiConcessiDepositatiByIdFascSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException {

		Connection lConn = null;

		Vector lLicenze = new Vector();
		ArrayList lPeriodi = null;
		Vector lLicenzePeriodi = new Vector();

		LicenzaLibanticipataSqlDAO lLicDao = null;
		PeriodoLibanticipataSqlDAO lPerDao = null;

		Iterator lItx = null;
		try {
			lConn = getDBConnection();
			lLicDao = new LicenzaLibanticipataSqlDAO(lConn);
			// lLicDao.ricercaLicenzaLibanticipataConcesseDepositate(aIdFascicolo, aFlagElaborato);
			lLicDao.ricercaRidimLicenzaLibanticipataConcesseDepositate(aIdFascicolo, aFlagElaborato);
			lLicenze = new Vector(lLicDao.getModels());

			if (lLicenze.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessun Periodo Concesso Depositato LA trovata a sistema");
			} else {
				lItx = lLicenze.iterator();
				while (lItx.hasNext()) {
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					aModel.setLicenza((LicenzaLibAnticipataModel) lItx.next());

					// Ricerca dei Periodi relativi alla licenza
					lPerDao = new PeriodoLibanticipataSqlDAO(lConn);

					// lPerDao.ricercaPeriodoLibanticipataByLic(aModel.getLicenza().getIdLicenzaLibanticipata());
					lPerDao.ricercaPeriodoLibanticipataOrdByLic(
							aModel.getLicenza().getIdLicenzaLibanticipata());

					lPeriodi = new ArrayList(lPerDao.getModels());
					aModel.setPeriodi(
							(PeriodoLibAnticipataModel[]) lPeriodi.toArray(new PeriodoLibAnticipataModel[1]));
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug(" primo periodo - ini/fin =
					// "+aModel.getPeriodi()[0].getDataInizio()+"/"+aModel.getPeriodi()[0].getDataFine());
					lLicenzePeriodi.add(aModel);
					cleanup(lPerDao);
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLicenzeLibanticipataByEve: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicDao);
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lLicenzePeriodi;
	}

	/**
	 *
	 * @param aIdFascicolo
	 *            idFascicolo SIEP
	 * @param aFlagElaborato
	 *            (null,S,N)
	 * @return Vector <LicenzaPeriodiLibAnticipataModel>
	 */
	public Vector ExRicercaLAPeriodiRevocatiDepositatiByIdFascSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException {

		Connection lConn = null;

		Vector lLicenze = new Vector();
		ArrayList lPeriodi = null;
		Vector lLicenzePeriodi = new Vector();

		LicenzaLibanticipataSqlDAO lLicLibSqlDao = null;
		PeriodoLibanticipataSqlDAO lPerLibSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();

			lLicLibSqlDao = new LicenzaLibanticipataSqlDAO(lConn);

			lLicLibSqlDao.ricercaLicenzaLibanticipataRevocateDepositate(aIdFascicolo, aFlagElaborato);
			lLicenze = new Vector(lLicLibSqlDao.getModels());

			if (lLicenze.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessuna Revoca LA trovata a sistema");
			} else {
				Iterator lItx = null;
				lItx = lLicenze.iterator();

				// Per ogni licenza recupero i periodi e la aggiungo alla lista
				while (lItx.hasNext()) {
					LicenzaPeriodiLibAnticipataModel aModel = new LicenzaPeriodiLibAnticipataModel();
					aModel.setLicenza((LicenzaLibAnticipataModel) lItx.next());

					// Ricerca dei Periodi relativi alla licenza
					lPerLibSqlDao = new PeriodoLibanticipataSqlDAO(lConn);

					lPerLibSqlDao.ricercaPeriodoLibanticipataOrdByLic(
							aModel.getLicenza().getIdLicenzaLibanticipata());

					lPeriodi = new ArrayList(lPerLibSqlDao.getModels());

					// MERGE v10 COLLAUDO: eseguo controllo preventivo
					if (!lPeriodi.isEmpty())
						aModel.setPeriodi((PeriodoLibAnticipataModel[]) lPeriodi
								.toArray(new PeriodoLibAnticipataModel[1]));
					else {
						PeriodoLibAnticipataModel[] plam = new PeriodoLibAnticipataModel[0];
						aModel.setPeriodi(plam);
					}

					cleanup(lPerLibSqlDao);

					// Carica i dati dell'evento (Decreto/Ordinanza)
					lEveSqlDao = new EventoSqlDAO(lConn);
					EventoModel lEveMod = null;
					lEveSqlDao.ricercaEventoByKey(aModel.getLicenza().getEveIdEvento());
					lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
					aModel.setEvento(lEveMod);
					cleanup(lEveSqlDao);

					// Aggiungo al model
					lLicenzePeriodi.add(aModel);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException:", daoEx);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaLAPeriodiRevocatiDepositatiByIdFascSIEP: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLicLibSqlDao);
			cleanup(lPerLibSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}

		return lLicenzePeriodi;
	}

	/**************************************************************************************/
	/* Ricerca dei periodi associati ad una licenza */
	/**************************************************************************************/
	public ArrayList ExRicercaPeriodiByIdLA(BigDecimal aIdLA) throws F3BException {

		Connection lConn = null;

		// PeriodoLibAnticipataModel lPeriodi = null;
		// Vector lLicenzePeriodi = new Vector();
		ArrayList lPeriodi = null;

		PeriodoLibanticipataSqlDAO lPerDao = null;

		try {
			lConn = getDBConnection();

			// Ricerca dei Periodi relativi alla licenza
			lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
			lPerDao.ricercaPeriodoLibanticipataByLic(aIdLA);
			// lPeriodi = (PeriodoLibAnticipataModel)lPerDao.getModels();
			// lLicenzePeriodi.add(lPeriodi);

			lPeriodi = new ArrayList(lPerDao.getModels());

			cleanup(lPerDao);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaPeriodiByIdLA: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPerDao);
			cleanup(lConn);
		}

		return lPeriodi;
	}

	/**
	 * Metodo per l'inserimento del ridimensionamento/revoca LA. Inserisce: - Evento Sorveglianza - Evento
	 * SIEP - Licenza Lib Anticipata e periodi
	 *
	 * @param aEvento
	 * @param aEventoAltroUff
	 * @param aCampoNote
	 * @param aLicenzaLA
	 *            - LA Ordinaria + Periodi
	 * @param aLicenzaLASPE
	 *            - LA Speciae + Periodi
	 * @param aLicenzaLAINT
	 *            - LA Integrazione + Periodi
	 * @return BigDecimal - idEvento SIEP inserito
	 */
	public BigDecimal ExInserisciRidimLibanticipataSorv(EventoModel aEvento, EventoModel aEventoAltroUff,
			CampoNotaModel aCampoNote, LicenzaPeriodiLibAnticipataModel aLicenzaLA,
			LicenzaPeriodiLibAnticipataModel aLicenzaLASPE, LicenzaPeriodiLibAnticipataModel aLicenzaLAINT)
			throws Exception {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		CampoNotaDAO lCampoNoteDAO = null;

		LicenzaLibanticipataDAO lLicDao = null;

		LicenzaLibAnticipataModel lLicModLA = null;
		LicenzaLibAnticipataModel lLicModSPE = null;
		LicenzaLibAnticipataModel lLicModINT = null;

		BigDecimal lKeyLicLA = null;
		BigDecimal lKeyLicLS = null;
		BigDecimal lKeyLicLI = null;

		PeriodoLibanticipataDAO lPerDao = null;
		PeriodoLibAnticipataModel lPerMod = null;
		BigDecimal lKeyPer = null;
		PeriodoLibAnticipataModel[] lPeriodi = null;

		if (!(aLicenzaLA != null && aLicenzaLA.getLicenza() != null)
				&& !(aLicenzaLASPE != null && aLicenzaLASPE.getLicenza() != null)
				&& !(aLicenzaLAINT != null && aLicenzaLAINT.getLicenza() != null)) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Licenza da Inserire");
		}

		EventoModel lEvento = null;

		try {
			lConn = getDBTransaction();

			// Inserimento EVENTO Sorveglianza
			lEveDao = new EventoDAO(lConn);
			EventoModel lEventoAltroUff = new EventoModel(aEventoAltroUff);
			lEveDao.setDAOFromModel(lEventoAltroUff);
			BigDecimal lKeyEvento = lEveDao.insert();
			lEventoAltroUff.setIdEvento(lKeyEvento);
			lEveDao.stop();

			// Inserimento EVENTO SIEP
			lEveDao = new EventoDAO(lConn);
			lEvento = new EventoModel(aEvento);
			lEveDao.setDAOFromModel(lEvento);
			lKeyEvento = lEveDao.insert();
			lEvento.setIdEvento(lKeyEvento);
			lEveDao.stop();

			// Inserisco il campo note
			if (aCampoNote != null && aCampoNote.getDescr() != null && !aCampoNote.getDescr().equals("")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco il campo note....");
				aCampoNote.setEveIdEvento(lKeyEvento);

				lCampoNoteDAO = new CampoNotaDAO(lConn);
				lCampoNoteDAO.setDAOFromModel(aCampoNote);
				lCampoNoteDAO.insert();
				lCampoNoteDAO.stop();
			}

			// 20/05/2014 Nuova L.A. - DL 146/2013 - Gestione di L.A., L.A. SPECIALE, L.A. INTEGRAZIONE

			// Inserisco la L.A.
			if (aLicenzaLA != null && aLicenzaLA.getLicenza() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lPerDao = new PeriodoLibanticipataDAO(lConn);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Lic LA - gg = " + aLicenzaLA.getLicenza().getNumeroGiorni());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Lic LA - tipo = " + aLicenzaLA.getLicenza().getDescrStatoPermesso());

				lLicModLA = new LicenzaLibAnticipataModel(aLicenzaLA.getLicenza());
				lLicModLA.setEveIdEvento(lKeyEvento);
				lLicDao.setDAOFromModel(lLicModLA);
				lKeyLicLA = lLicDao.insert();
				lLicModLA.setIdLicenzaLibanticipata(lKeyLicLA);
				aLicenzaLA.setLicenza(lLicModLA);
				lLicDao.stop();

				lPeriodi = aLicenzaLA.getPeriodi();
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLicLA);
						lPerDao.setDAOFromModel(lPerMod);
						lKeyPer = lPerDao.insert();
						lPerDao.stop();
						lPerMod.setIdPeriodoLibanticipata(lKeyPer);
						aLicenzaLA.getPeriodi()[j] = lPerMod;
					}
				}
			}

			// Inserisco la L.A. SPECIALE
			if (aLicenzaLASPE != null && aLicenzaLASPE.getLicenza() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lPerDao = new PeriodoLibanticipataDAO(lConn);

				lLicModSPE = new LicenzaLibAnticipataModel(aLicenzaLASPE.getLicenza());
				lLicModSPE.setEveIdEvento(lKeyEvento);
				lLicDao.setDAOFromModel(lLicModSPE);
				lKeyLicLS = lLicDao.insert();
				// lLicMod.setIdLicenzaLibanticipata(lKeyLic);
				// aLicenzaLA.setLicenza(lLicMod);
				lLicDao.stop();

				lPeriodi = aLicenzaLASPE.getPeriodi();
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLicLS);
						lPerDao.setDAOFromModel(lPerMod);
						lPerDao.insert();
						// lKeyPer = lPerDao.insert();
						lPerDao.stop();
						// lPerMod.setIdPeriodoLibanticipata(lKeyPer);
						// aLicenzaLA.getPeriodi()[j] = lPerMod;
					}
				}
			}

			// Inserisco la L.A. INTEGRAZIONE
			if (aLicenzaLAINT != null && aLicenzaLAINT.getLicenza() != null) {
				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lPerDao = new PeriodoLibanticipataDAO(lConn);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Lic LAINT - gg = " + aLicenzaLAINT.getLicenza().getNumeroGiorni());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" Lic LAINT - tipo = " + aLicenzaLAINT.getLicenza().getDescrStatoPermesso());

				lLicModINT = new LicenzaLibAnticipataModel(aLicenzaLAINT.getLicenza());
				lLicModINT.setEveIdEvento(lKeyEvento);
				lLicDao.setDAOFromModel(lLicModINT);
				lKeyLicLI = lLicDao.insert();
				// lLicMod.setIdLicenzaLibanticipata(lKeyLic);
				// aLicenzaLA.setLicenza(lLicMod);
				lLicDao.stop();

				lPeriodi = aLicenzaLAINT.getPeriodi();
				if (lPeriodi != null) {
					// Inserimento Periodi
					for (int j = 0; j < lPeriodi.length; j++) {
						lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
						lPerMod.setLicIdLicenzaLibanticipata(lKeyLicLI);
						lPerDao.setDAOFromModel(lPerMod);
						lPerDao.insert();
						// lKeyPer = lPerDao.insert();
						lPerDao.stop();
						// lPerMod.setIdPeriodoLibanticipata(lKeyPer);
						// aLicenzaLA.getPeriodi()[j] = lPerMod;
					}
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciRidimLibanticipata: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciRidimLibanticipata: " + ex);
		} finally {
			cleanup(lEveDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lCampoNoteDAO);
			cleanup(lLicDao);
			cleanup(lPerDao);
			cleanup(lConn);
		}
		return lEvento.getIdEvento();
	}

	public EventoModel ExInserisciRimediRisarcitoriSIEP(EventoModel aEvento,
			Vector<LicenzaPeriodiLibAnticipataModel> aLicenzeEPeriodi, DepositoDecretoModel aDepDecMod,
			DepositoOrdinanzaPcModel aDepOrdMod, TenoreModel aTenoreModel) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" ExInserisciRimediRisarcitoriSIEP - INIZIO");

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		TenoreDAO lTenDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		DepositoDecretoDAO lDepDecrDao = null;
		LicenzaLibanticipataDAO lLicDao = null;
		PeriodoLibanticipataDAO lPeriodoDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento EVENTO..");

			lConn = getDBTransaction(); // getDBConnection()

			// ====================
			// Inserimento EVENTO
			// ====================
			lEveDao = new EventoDAO(lConn);

			// Setto il PROGR_PROTOCOLLO
			lEveSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDAO.getProgressivo(aEvento);
			aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lKeyEvento = lEveDao.insert();
			aEvento.setIdEvento(lKeyEvento);
			lEveDao.stop();

			// ====================================
			// Inserimento DEPOSITO_DECRETO
			// ====================================
			if (aDepDecMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserimento DEPOSITO_DECRETO..");
				lDepDecrDao = new DepositoDecretoDAO(lConn);

				aDepDecMod.setIdEventoGenerato(lKeyEvento);

				lDepDecrDao.setDAOFromModel(aDepDecMod);
				BigDecimal lKeyDepDec = lDepDecrDao.insert();
				aDepDecMod.setIdDepositoDecreto(lKeyDepDec);
				lDepDecrDao.stop();
			}

			// ====================================
			// Inserimento DEPOSITO_ORDINANZA_PC
			// ====================================
			if (aDepOrdMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserimento DEPOSITO_ORDINANZA_PC..");
				lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);

				aDepOrdMod.setIdEventoGenerato(lKeyEvento);

				lDepOrdDao.setDAOFromModel(aDepOrdMod);
				BigDecimal lKeyDepOrd = lDepOrdDao.insert();
				aDepOrdMod.setIdDepositoOrdinanzaPc(lKeyDepOrd);
				lDepOrdDao.stop();
			}

			// ====================================
			// Inserimento TENORE
			// ====================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserimento TENORE...");
			if (aDepDecMod != null) {
				aTenoreModel.setDepDecIdDepositoDecreto(aDepDecMod.getIdDepositoDecreto());
			} else if (aDepOrdMod != null) {
				aTenoreModel.setDepOpidDepositoOrdinanzaPc(aDepOrdMod.getIdDepositoOrdinanzaPc());
			}

			lTenDao = new TenoreDAO(lConn);
			lTenDao.setDAOFromModel(aTenoreModel);

			BigDecimal lKeyTenore;
			lKeyTenore = lTenDao.insert();
			aTenoreModel.setIdTenore(lKeyTenore);
			lTenDao.stop();

			// =======================================================
			// Inserisce LICENZA_LIBANTICIPATA E relativi PERIODI
			// =======================================================
			// Vector <LicenzaPeriodiLibAnticipataModel> aLicenzeEPeriodi
			for (int i = 0; i < aLicenzeEPeriodi.size(); i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserimento LICENZA_LIBANTICIPATA...");
				// Inserisco licenza
				LicenzaLibAnticipataModel lLibAntModel = aLicenzeEPeriodi.elementAt(i).getLicenza();

				lLibAntModel.setEveIdEvento(lKeyEvento);

				lLicDao = new LicenzaLibanticipataDAO(lConn);
				lLicDao.setDAOFromModel(lLibAntModel);

				BigDecimal lIdLicenza = lLicDao.insert();
				lLibAntModel.setIdLicenzaLibanticipata(lIdLicenza);

				// Inserisco i periodi
				PeriodoLibAnticipataModel[] lArrayPeriodi = aLicenzeEPeriodi.elementAt(i).getPeriodi();
				for (int j = 0; j < lArrayPeriodi.length; j++) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserimento PERIODO_LIBANTICIPATA...");
					PeriodoLibAnticipataModel lPeriodoModel = lArrayPeriodi[j];

					lPeriodoModel.setLicIdLicenzaLibanticipata(lLibAntModel.getIdLicenzaLibanticipata());

					lPeriodoDao = new PeriodoLibanticipataDAO(lConn);
					lPeriodoDao.setDAOFromModel(lPeriodoModel);
					BigDecimal lIdPeriodo = lPeriodoDao.insert();
					lPeriodoModel.setIdPeriodoLibanticipata(lIdPeriodo);
					lPeriodoDao.stop();
				}
			}

			// rollback (lConn);
			commit(lConn);
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Eccezione in fase di inserimento RimediRisarcitori", ex);
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciRimediRisarcitoriSIEP: " + ex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Eccezione in fase di inserimento RimediRisarcitori", ex);
			rollback(lConn);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExInserisciRimediRisarcitoriSIEP: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lDepDecrDao);
			cleanup(lDepOrdDao);
			cleanup(lTenDao);
			cleanup(lLicDao);
			cleanup(lPeriodoDao);
			cleanup(lConn);
		}

		return aEvento;
	}

	/**
	 * Deve ricercare gli eventi di tipo 2790 e le relative licenze associate
	 *
	 * @param aIdFascicolo
	 * @param aFlagElaborato
	 * @return
	 * @throws F3BException
	 */
	public Vector<EventoLicenzePeriodiModel> ExRicercaRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(
			BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;

		// Evento per la ricerca
		EventoModel lEventoRicerca = new EventoModel();
		lEventoRicerca.setFasSieIdFascicoloSiep(aIdFascicolo);
		// lEventoRicerca.setFlagDocumentoRegistrato("S");
		lEventoRicerca.setCodTipoEvento("01");

		String[] lCodTipoProvvedimento = { "02", "03" };
		String[] lCodMotivo = { "2790" };

		Vector<EventoLicenzePeriodiModel> lListaEventiRet = new Vector<>();

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);

			lEveSqlDao.ricercaEventoPerMotivoPerProvv(lCodMotivo, lCodTipoProvvedimento, lEventoRicerca);

			List<EventoModel> lListaEventi = new ArrayList(lEveSqlDao.getModels());
			lEveSqlDao.stop();

			Iterator iter = lListaEventi.iterator();
			while (iter.hasNext()) {
				EventoModel lEventoModel = (EventoModel) iter.next();
				if (lEventoModel.getDataTrasmissioneAtti() != null) {

					EventoLicenzePeriodiModel lEveLicPerModel = new EventoLicenzePeriodiModel();

					// Aggiungo l'evento
					lEveLicPerModel.setEvento(lEventoModel);

					// recupero le LA collegate all'evento
					lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
					lLicSqlDao.ricercaLicenzaLibanticipataByEve(lEventoModel.getIdEvento());
					Vector lLicenze = new Vector(lLicSqlDao.getModels());
					lLicSqlDao.stop();

					lEveLicPerModel.setListaLicenze(lLicenze);

					// Vedo se c'è un evento di Esecuzione NON ANNULLATO collegato
					// lEveDao = new EventoDAO(lConn);
					// lEveDao.selCondizioneEveIdEvento(lEventoModel.getIdEvento());
					// lEveDao.start();

					lEveSqlDao.ricercaEventoByEveIdEvento(lEventoModel.getIdEvento());
					lEveSqlDao.start();

					if (lEveSqlDao.next()) {
						EventoModel lEveEsecuzione = (EventoModel) lEveSqlDao.getModel();
						lEveLicPerModel.setEventoCollegato(lEveEsecuzione);
					}
					lEveSqlDao.stop();

					lListaEventiRet.add(lEveLicPerModel);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lListaEventiRet;
	}

	/**
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaComunicazioneRimediRisarcitori(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		Connection lConnBlob = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoDAO lDepDecDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// Recupero alcuni dei dati dell'evento da validare
			// Data emissione e EveIdEvento ovvero evento di sorveglianza
			lEveDao = new EventoDAO(lConn);

			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
			}
			lEveDao.stop();

			// ======================================================================
			// Recupera l'evento della Sorvegliazna Dereto/Ordinanza per validarlo
			// se non validato ovvero inserito SIEP
			// ======================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			EventoModel lEveSorveglianza = null;

			if (lEveApp.getEveIdEvento() != null) {
				lEveSqlDao.ricercaEventoByKey(lEveApp.getEveIdEvento());
				lEveSorveglianza = (EventoModel) lEveSqlDao.getModelByKey();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lEveSorveglianza legato alla LA: " + lEveSorveglianza.getIdEvento());

			if (lEveSorveglianza != null) {
				// Aggiorno l'evento della Sorveglianza
				lEveDao.setIdEvento(lEveSorveglianza.getIdEvento());

				lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// Deposito Ordinanza PC
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveSorveglianza.getIdEvento());
				DepositoOrdinanzaPcModel lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao
						.getModelByKey();

				if (lDepOrdMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(
							"lDepOrdMod legato all'Ordinanza: " + lDepOrdMod.getIdDepositoOrdinanzaPc());

					lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);

					lDepOrdDao.setIdDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());

					lDepOrdDao.setFlagElaborato("S");

					lDepOrdDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepOrdDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepOrdDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					lDepOrdDao.selByKey();
					lDepOrdDao.update();
					lDepOrdDao.stop();
				}

				// Deposito Decreto
				lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
				lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(lEveSorveglianza.getIdEvento());
				DepositoDecretoModel lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

				if (lDepDecMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("lDepDecMod legato al Decreto: " + lDepDecMod.getIdDepositoDecreto());

					lDepDecDao = new DepositoDecretoDAO(lConn);

					lDepDecDao.setIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());

					lDepDecDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepDecDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepDecDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					lDepDecDao.selByKey();
					lDepDecDao.update();
					lDepDecDao.stop();
				}
			}
			// ************************ FINE EVENTO ORDINANZA ************************

			// ************************ NOME PROVVEDIMENTO ***************************
			// NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			// lNomProvDao = new NomeProvvedimentoDAO(lConn);
			//
			// lNomProvMod.setCodNomeProvvedimento("NP209");
			//
			// lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			// lNomProvDao.setDAOFromModel(lNomProvMod);
			// lNomProvDao.insert();

			// ************************ FINE NOME PROVVEDIMENTO **********************

			// FIXME DL92 - Comunicazone - Verificare se Aggiornare lo stato procedimento

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException:", daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaComunicazioneRimediRisarcitori : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception:", ex);
			rollback(lConn);
			rollback(lConnBlob);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaComunicazioneRimediRisarcitori : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lDepOrdDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecDao);
			cleanup(lDepDecSqlDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOSRimediRisarcitori(EventoModel aEvento) throws F3BException {

		Connection lConn = null;
		Connection lConnBlob = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoDAO lDepDecDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		// ==============
		LicenzaLibanticipataSqlDAO lLicLibSqlDao = null;
		LicenzaLibanticipataDAO lLicLibDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		MisuraAlternativaDAO lMisDAO = null;
		MisuraAlternativaSqlDAO lMisSqlDAO = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		FungibilitaDAO lFunDao = null;
		FungibilitaSqlDAO lFunSqlDao = null;

		try {
			lConn = getDBTransaction();

			// Recupero tutti dei dati dell'evento da validare
			lEveDao = new EventoDAO(lConn);

			EventoModel lEveApp = new EventoModel();

			lEveDao.selCondizione(aEvento);
			lEveApp = (EventoModel) lEveDao.getModelByKey();
			lEveDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lEveApp = " + lEveApp);

			// ======================================================================
			// Recupera l'evento della Sorvegliazna Decreto/Ordinanza per validarlo
			// se non validato ovvero inserito SIEP
			// ======================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			EventoModel lEveSorveglianza = null;

			if (lEveApp.getEveIdEvento() != null) {
				lEveSqlDao.ricercaEventoByKey(lEveApp.getEveIdEvento());
				lEveSorveglianza = (EventoModel) lEveSqlDao.getModelByKey();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lEveSorveglianza legato alla LA: " + lEveSorveglianza.getIdEvento());

			if (lEveSorveglianza != null) {
				// Aggiorno l'evento della Sorveglianza
				lEveDao.setIdEvento(lEveSorveglianza.getIdEvento());

				lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// Deposito Ordinanza PC
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveSorveglianza.getIdEvento());
				DepositoOrdinanzaPcModel lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao
						.getModelByKey();

				if (lDepOrdMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(
							"lDepOrdMod legato all'Ordinanza: " + lDepOrdMod.getIdDepositoOrdinanzaPc());

					lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);

					lDepOrdDao.setIdDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());

					lDepOrdDao.setFlagElaborato("S");

					lDepOrdDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepOrdDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepOrdDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					lDepOrdDao.selByKey();
					lDepOrdDao.update();
					lDepOrdDao.stop();
				}

				// Deposito Decreto
				lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
				lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(lEveSorveglianza.getIdEvento());
				DepositoDecretoModel lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

				if (lDepDecMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("lDepDecMod legato al Decreto: " + lDepDecMod.getIdDepositoDecreto());

					lDepDecDao = new DepositoDecretoDAO(lConn);

					lDepDecDao.setIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());

					lDepDecDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepDecDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepDecDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					lDepDecDao.selByKey();
					lDepDecDao.update();
					lDepDecDao.stop();
				}
			}
			// ************************ FINE EVENTO ORDINANZA ************************

			// ====================================================================
			// Aggiorno LICENZA_LIBANTICIPATA
			// ====================================================================
			lLicLibSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
			lLicLibSqlDao.ricercaLicenzaLibanticipataByEve(lEveApp.getEveIdEvento());
			Vector lLibVect = new Vector(lLicLibSqlDao.getModels());

			for (int i = 0; i < lLibVect.size(); i++) {
				LicenzaLibAnticipataModel lLicLibMod = (LicenzaLibAnticipataModel) lLibVect.get(i);

				if ("RD".equals(lLicLibMod.getCodTipoLicenza()) && "C".equals(lLicLibMod.getFlagConcesso())) {
					// Record dei GG di riduzione concessi
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Aggiornamento FLAG_ELABORATO su RD");

					lLicLibDao = new LicenzaLibanticipataDAO(lConn);

					lLicLibDao.setFlagElaborato("S");

					lLicLibDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lLicLibDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lLicLibDao.setDataAggiornamento(aEvento.getDataAggiornamento());

					lLicLibDao.setIdLicenzaLibanticipata(lLicLibMod.getIdLicenzaLibanticipata());
					lLicLibDao.selByKey();
					lLicLibDao.update();
					lLicLibDao.stop();
				}
			}

			// ====================================================================
			// Aggiorno Pena residua
			// ====================================================================
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Validazione pena residua");
				lPenResDao = new PenaResiduaDAO(lConn);

				lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());

				lPenResDao.setFlagValidato("S");

				lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPenResDao.setDataAggiornamento(aEvento.getDataAggiornamento());

				lPenResDao.selByKey();
				lPenResDao.update();
				lPenResDao.stop();
			}

			// ========================================================================
			// Validazione fungibilità (se presente)
			// ========================================================================
			lFunDao = new FungibilitaDAO(lConn);
			lFunSqlDao = new FungibilitaSqlDAO(lConn);

			lFunSqlDao.ricercaFungibilitaByKeyEvento(lEveApp.getIdEvento());

			FungibilitaModel lFunMod = (FungibilitaModel) lFunSqlDao.getModelByKey();
			if (lFunMod != null) {
				lFunDao.setFlagValidato("S");

				lFunDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lFunDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lFunDao.setDataAggiornamento(aEvento.getDataAggiornamento());

				lFunDao.setCondizioneUpdate(lFunMod.getIdFungibilita());
				lFunDao.update();
				lFunDao.stop();
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fungibilità non trovata");
			}

			// ======================================================================
			// Aggiorna STATO_PROCEDIMENTO
			// 0449 - Emesso Ordine di Scarcerazione per Concessione Risarcimento Danni D.L. 92/2014 il
			// 0010 - Pena in Esecuzione Fino al
			// ======================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornamento stato procedimento");
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(lEveApp.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// Inserisci STATO Del PROCEDIMENTO
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setFasSieIdFascicoloSiep(lEveApp.getFasSieIdFascicoloSiep());

			lStatoProcMod.setData(lEveApp.getDataEmissione());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			// 0449: Emesso Ordine di Scarcerazione per Concessione Risarcimento Danni D.L. 92/2014 il
			lStatoProcMod.setCodStatoProcedimento("0449");
			// inizio ticket 20190805017 - Mancata attribuzione dei giorni di detrazione rimedi risarcitori
			// nel caso di Ordine di Scarcerazione per RECLAMO Concessione Risarcimento Danni D.L. 92/2014
			// devo inserire un apposito stato del procedimento, OSSIA 0499
			if (lEveApp != null
					&& (lEveApp.getCodMotivo().equals("9154") || lEveApp.getCodMotivo().equals("9254"))) {
				lStatoProcMod.setCodStatoProcedimento("0499");
			}
			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();
			// 20260422 [SG]: aggiunto controllo preventivo
			if (lPenResMod != null && lPenResMod.getDataFine() != null)
				// 0010: Pena in Esecuzione Fino al
				lStatoProcMod.setData(lPenResMod.getDataFine());
			lStatoProcMod.setCodStatoProcedimento("0010");
			lStatoProcMod.setProgressivo(new BigDecimal(2));
			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			// ======================================================================
			// AGGIORNA MISURA ALTERNATIVA (data fine misura)
			// ======================================================================
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lEveApp.getFasSieIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosMod.isMisAlt()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiornamento fine misura...");

				lMisDAO = new MisuraAlternativaDAO(lConn);
				lMisSqlDAO = new MisuraAlternativaSqlDAO(lConn);
				lMisSqlDAO.ricercaMisuraAlternativaCorrenteByIdFascicolo(lEveApp.getFasSieIdFascicoloSiep());
				MisuraAlternativaModel lMisModel = (MisuraAlternativaModel) lMisSqlDAO.getModelByKey();

				if (lMisModel != null) {
					// Se la DATA_FINE_MISURA è successiva alla
					// DATA_FINE_PENA aggiorna la
					if (lMisModel.getDataFineMisura() != null && lPenResMod != null
							&& lPenResMod.getDataFine() != null
							&& lMisModel.getDataFineMisura().after(lPenResMod.getDataFine())) {
						lMisModel.setDataFineMisura(lPenResMod.getDataFine());

						lMisModel.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lMisModel.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
						lMisModel.setDataInserimento(aEvento.getDataAggiornamento());

						lMisDAO.setDAOFromModelForUpdate(lMisModel);
						lMisDAO.update();
						lMisDAO.stop();

						// inserisco duplico occorrenza MA agganciandola all'ordine di scarcerazione
						// FIXME DL92 Validazione OS duplica MA
						// lMisModel.setEveIdEvento(aEvento.getIdEvento());
						// lMisDAO.setDAOFromModel(lMisModel);
						// lMisDAO.insert();
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Misura non trovata");
				}
			}

			// ======================================================================
			//
			// ======================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inizio Aggiornamento scadenzari");
			lScaDao = new ScadenzarioDAO(lConn);
			lScaSqlDao = new ScadenzarioSqlDAO(lConn);

			// ======================================================================
			// Ricerca scadenzario Fine Pena (02) e lo aggiorna. Se non presente lo inserisce
			Vector<String> lScadenzari = new Vector<>();
			lScadenzari.add("02"); // 02 Scadenzario fine pena

			if (lPosMod.isMisAlt()) {
				lScadenzari.add("13"); // 13 Scadenzario fine Misura
			}

			for (int i = 0; i < lScadenzari.size(); i++) {
				String lTipoScadenzario = lScadenzari.elementAt(i);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Scadenzario = " + lTipoScadenzario);

				lScaSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo(lTipoScadenzario,
						lEveApp.getFasSieIdFascicoloSiep());
				ScadenzarioModel lScadModel = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScadModel != null) {
					// aggiorna scadenzario
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Aggiorno");

					lScaDao.setDataFineScadenza(lPenResMod.getDataFine());
					lScaDao.setDataInizioScadenza(lPenResMod.getDataInizio());

					lScaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDao.setDataAggiornamento(aEvento.getDataAggiornamento());

					lScaDao.setCondizioneUpdate(lScadModel.getIdScadenzario());
					lScaDao.update();
					lScaDao.stop();
				} else {
					// inserisce scadenzario
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco");

					ScadenzarioModel lScaMod = new ScadenzarioModel();

					lScaMod.setCodTipoScadenzario(lTipoScadenzario);

					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaMod.setDataFineScadenza(lPenResMod.getDataFine());

					lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setDataInserimento(aEvento.getDataAggiornamento());

					lScaMod.setFasSieIdFascicoloSiep(lEveApp.getFasSieIdFascicoloSiep());

					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();
					lScaDao.stop();
				}
			}

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConnBlob);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException:", sqe);

			rollback(lConn);
			rollback(lConnBlob);

			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaOSRimediRisarcitori : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception:", ex);

			rollback(lConn);
			rollback(lConnBlob);

			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaOSRimediRisarcitori : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lDepOrdDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecDao);
			cleanup(lDepDecSqlDao);
			cleanup(lEveDaoBlob);
			cleanup(lLicLibSqlDao);
			cleanup(lLicLibDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lStatoDao);
			cleanup(lPosSqlDao);
			cleanup(lMisDAO);
			cleanup(lMisSqlDAO);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lFunDao);
			cleanup(lFunSqlDao);

			cleanup(lConn);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * Deve ricercare gli eventi di tipo 9027 e le relative licenze associate
	 *
	 * @param aIdFascicolo
	 * @param aFlagElaborato
	 * @return
	 * @throws F3BException
	 */
	public Vector<EventoLicenzePeriodiModel> ExRicercaReclamoRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(
			BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;

		LicenzaLibanticipataSqlDAO lLicSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;

		// Evento per la ricerca
		EventoModel lEventoRicerca = new EventoModel();
		lEventoRicerca.setFasSieIdFascicoloSiep(aIdFascicolo);
		// lEventoRicerca.setFlagDocumentoRegistrato("S");
		lEventoRicerca.setCodTipoEvento("01");

		String[] lCodTipoProvvedimento = { "02", "03" };
		String[] lCodMotivo = { "9027" };

		Vector<EventoLicenzePeriodiModel> lListaEventiRet = new Vector<>();

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);

			lEveSqlDao.ricercaEventoPerMotivoPerProvv(lCodMotivo, lCodTipoProvvedimento, lEventoRicerca);

			List<EventoModel> lListaEventi = new ArrayList(lEveSqlDao.getModels());
			lEveSqlDao.stop();

			Iterator iter = lListaEventi.iterator();
			while (iter.hasNext()) {
				EventoModel lEventoModel = (EventoModel) iter.next();
				if (lEventoModel.getDataTrasmissioneAtti() != null) {

					EventoLicenzePeriodiModel lEveLicPerModel = new EventoLicenzePeriodiModel();

					// Aggiungo l'evento
					lEveLicPerModel.setEvento(lEventoModel);

					// recupero le LA collegate all'evento
					lLicSqlDao = new LicenzaLibanticipataSqlDAO(lConn);
					lLicSqlDao.ricercaLicenzaLibanticipataByEve(lEventoModel.getIdEvento());
					Vector lLicenze = new Vector(lLicSqlDao.getModels());
					lLicSqlDao.stop();

					lEveLicPerModel.setListaLicenze(lLicenze);

					// Vedo se c'è un evento di Esecuzione NON ANNULLATO collegato
					// lEveDao = new EventoDAO(lConn);
					// lEveDao.selCondizioneEveIdEvento(lEventoModel.getIdEvento());
					// lEveDao.start();

					lEveSqlDao.ricercaEventoByEveIdEvento(lEventoModel.getIdEvento());
					lEveSqlDao.start();

					if (lEveSqlDao.next()) {
						EventoModel lEveEsecuzione = (EventoModel) lEveSqlDao.getModel();
						lEveLicPerModel.setEventoCollegato(lEveEsecuzione);
					}
					lEveSqlDao.stop();

					lListaEventiRet.add(lEveLicPerModel);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExRicercaReclamoRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP: "
							+ daoEx);
		} finally {
			cleanup(lLicSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lListaEventiRet;
	}

	/**
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaComunicazioneReclamoRimediRisarcitori(EventoModel aEvento)
			throws F3BException {

		Connection lConn = null;
		Connection lConnBlob = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoDAO lDepDecDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		try {
			lConn = getDBTransaction();

			// Recupero alcuni dei dati dell'evento da validare
			// Data emissione e EveIdEvento ovvero evento di sorveglianza
			lEveDao = new EventoDAO(lConn);

			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				lEveApp.setEveIdEvento(lEveDao.getEveIdEvento());
			}
			lEveDao.stop();

			// ======================================================================
			// Recupera l'evento della Sorvegliazna Dereto/Ordinanza per validarlo
			// se non validato ovvero inserito SIEP
			// ======================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			EventoModel lEveSorveglianza = null;

			if (lEveApp.getEveIdEvento() != null) {
				lEveSqlDao.ricercaEventoByKey(lEveApp.getEveIdEvento());
				lEveSorveglianza = (EventoModel) lEveSqlDao.getModelByKey();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lEveSorveglianza legato alla LA: " + lEveSorveglianza.getIdEvento());

			if (lEveSorveglianza != null) {
				// Aggiorno l'evento della Sorveglianza
				lEveDao.setIdEvento(lEveSorveglianza.getIdEvento());

				lEveDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selByKey();
				lEveDao.update();
				lEveDao.stop();

				// Deposito Ordinanza PC
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveSorveglianza.getIdEvento());
				DepositoOrdinanzaPcModel lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao
						.getModelByKey();

				if (lDepOrdMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(
							"lDepOrdMod legato all'Ordinanza: " + lDepOrdMod.getIdDepositoOrdinanzaPc());

					lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);

					lDepOrdDao.setIdDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());

					lDepOrdDao.setFlagElaborato("S");

					lDepOrdDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepOrdDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepOrdDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					lDepOrdDao.selByKey();
					lDepOrdDao.update();
					lDepOrdDao.stop();
				}

				// Deposito Decreto
				lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
				lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(lEveSorveglianza.getIdEvento());
				DepositoDecretoModel lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

				if (lDepDecMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("lDepDecMod legato al Decreto: " + lDepDecMod.getIdDepositoDecreto());

					lDepDecDao = new DepositoDecretoDAO(lConn);

					lDepDecDao.setIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());

					lDepDecDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lDepDecDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lDepDecDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					lDepDecDao.selByKey();
					lDepDecDao.update();
					lDepDecDao.stop();
				}
			}
			// ************************ FINE EVENTO ORDINANZA ************************

			// ************************ NOME PROVVEDIMENTO ***************************
			// NomeProvvedimentoModel lNomProvMod = new NomeProvvedimentoModel();
			// lNomProvDao = new NomeProvvedimentoDAO(lConn);
			//
			// lNomProvMod.setCodNomeProvvedimento("NP209");
			//
			// lNomProvMod.setEveIdEvento(aEvento.getIdEvento());
			// lNomProvDao.setDAOFromModel(lNomProvMod);
			// lNomProvDao.insert();

			// ************************ FINE NOME PROVVEDIMENTO **********************

			// FIXME DL92 - Comunicazone - Verificare se Aggiornare lo stato procedimento

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException:", daoEx);
			rollback(lConn);
			rollback(lConnBlob);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaComunicazioneReclamoRimediRisarcitori : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception:", ex);
			rollback(lConn);
			rollback(lConnBlob);
			throw new F3BException(
					"LicenzaPeriodiLibAnticipataController.ExUpdateValidaComunicazioneReclamoRimediRisarcitori : "
							+ ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lDepOrdDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecDao);
			cleanup(lDepDecSqlDao);
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lConnBlob);
		}

		return lEveMod;
	}

}