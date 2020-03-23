package siap.siep.avvocato.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoDAO;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSqlDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.storicoavvocato.dao.StoricoAvvocatoDAO;
import siap.siep.storicoavvocato.dao.StoricoAvvocatoSqlDAO;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: AvvocatoController
 * </p>
 * <p>
 * Description: Classe Controller per Avvocato
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
public class AvvocatoController extends SiapController implements IAvvocato {

	public BigDecimal ExInserisciAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSiepModel aAvvFascMod)
			throws F3BException {

		Connection lConn = null;

		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiepDAO lAvvFascDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		BigDecimal lSequence = null;

		try {
			lConn = getDBTransaction();
			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);

			aAvvFascMod.setAvvIdAvvocato(aAvvocato.getIdAvvocato());

			lAvvFascDao.setDAOFromModel(aAvvFascMod);
			lSequence = lAvvFascDao.insert();
			aAvvFascMod.setIdAvvocatoFascicoloSiep(lSequence);

			// ricerco eventuale evento OE con sospensione (simeone) (OEDS in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvFascMod.getFasSieIdFascicoloSiep(), "LS");
			EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			boolean lIsRicalcoloScadenzario = false;
			// se cè il suddetto evento OEDS controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "assegna", aAvvFascMod, lEveMod, null);
			}

			// salvo l'evento OEDS per cercare il decreto di irreperibilità (OEDI in breve)
			EventoModel lSalvoEveModSimeone = new EventoModel();
			if (lEveMod != null) {
				lSalvoEveModSimeone = new EventoModel(lEveMod);
			}

			// ricerco eventuale evento decreto di irreperibilità (OEDI in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvFascMod.getFasSieIdFascicoloSiep(), "8BIS");
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			// se cè il suddetto evento OEDI controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "assegna", aAvvFascMod, lEveMod, null);
			}

			// se non cè il suddetto evento OEDI
			// riprendo l'evento salvato OEDS
			if (lEveMod == null && lSalvoEveModSimeone != null) {
				lEveMod = new EventoModel(lSalvoEveModSimeone);
			}

			List lNotificheSimeone = null;
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lNotEveDao = new NotificaEventoSqlDAO(lConn);
				lNotEveDao.ricercaNotificaByEvento(lEveMod.getIdEvento());
				lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
				lNotEveDao.stop();
			}

			// IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

			if (lNotificheSimeone != null && lIsRicalcoloScadenzario) {
				RicalcoloScadenzario(lConn, lNotificheSimeone, aAvvFascMod, lEveMod);
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExInserisciAvvocato: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExInserisciAvvocato: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lAvvFascDao);
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}

		return lSequence;
	}

	public BigDecimal ExInserisciAvvocato(EventoNotificaModel lEveNot, AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiepModel aAvvFascMod) throws F3BException {

		Connection lConn = null;

		AvvocatoDAO lAvvDao = null;
		AvvocatoFascicoloSiepDAO lAvvFascDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		BigDecimal lSequence = null;

		try {
			lConn = getDBTransaction();

			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);

			aAvvFascMod.setAvvIdAvvocato(aAvvocato.getIdAvvocato());

			lAvvFascDao.setDAOFromModel(aAvvFascMod);
			lSequence = lAvvFascDao.insert();
			aAvvFascMod.setIdAvvocatoFascicoloSiep(lSequence);
			// se inserisco un avvocato d'ufficio devo scrivere l'evento
			lEveNot = ExInserisciEventoNotifica(lSequence, lEveNot, lConn);

			// ricerco eventuale evento OE con sospensione (simeone) (OEDS in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvFascMod.getFasSieIdFascicoloSiep(), "LS");
			EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			boolean lIsRicalcoloScadenzario = false;
			// se cè il suddetto evento OEDS controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "assegna", aAvvFascMod, lEveMod, null);
			}

			// salvo l'evento OEDS per cercare il decreto di irreperibilità (OEDI in breve)
			EventoModel lSalvoEveModSimeone = new EventoModel();
			if (lEveMod != null) {
				lSalvoEveModSimeone = new EventoModel(lEveMod);
			}

			// ricerco eventuale evento decreto di irreperibilità (OEDI in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvFascMod.getFasSieIdFascicoloSiep(), "8BIS");
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			// se cè il suddetto evento OEDI controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "assegna", aAvvFascMod, lEveMod, null);
			}

			// se non cè il suddetto evento OEDI
			// riprendo l'evento salvato OEDS
			if (lEveMod == null && lSalvoEveModSimeone != null) {
				lEveMod = new EventoModel(lSalvoEveModSimeone);
			}

			List lNotificheSimeone = null;
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lNotEveDao = new NotificaEventoSqlDAO(lConn);
				lNotEveDao.ricercaNotificaByEvento(lEveMod.getIdEvento());
				lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
				lNotEveDao.stop();
			}

			// IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

			if (lNotificheSimeone != null && lIsRicalcoloScadenzario) {
				RicalcoloScadenzario(lConn, lNotificheSimeone, aAvvFascMod, lEveMod);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExInserisciAvvocato: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExInserisciAvvocato: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lAvvFascDao);
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}

		return lSequence;
	}

	/**
	*
	*/
	public AvvocatoModel ExInserisciAvvocato(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoModel lAvv = null;

		try {
			lConn = getDBConnection();

			lAvvDao = new AvvocatoDAO(lConn);

			lAvvDao.setDAOFromModel(aAvvocato);
			BigDecimal lSequence = lAvvDao.insert();

			commit(lConn);
			lAvv = new AvvocatoModel(aAvvocato);
			lAvv.setIdAvvocato(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".ExInserisciAvvocato: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvv;
	}

	/**
	*
	*/
	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSiepModel aAvvFascMod)
			throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocato(aAvvocato, aAvvFascMod);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaAvvocato: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoModel ExRicercaAvvocatoByKey(BigDecimal aIdAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		AvvocatoModel lAvvocato = new AvvocatoModel();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatobyKey(aIdAvvocato);

			lAvvocato = (AvvocatoModel) lAvvDao.getModelByKey();

			lAvvDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaAvvocatoByKey: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaAvvocatoByKey: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocato;
	}

	/*****************************************************************************
	 * Ricerca gli avvocati ATTUALMENTE assegnati al fascicolo in input la condizione è per id avvocato o nome
	 */
	public Vector ExRicercaAvvocatiAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiepModel aAvvFascMod) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoAttualeFascicolo(aAvvocato, aAvvFascMod);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Nessun avvocato associato al fascicolo.");
		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatiAttualiFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public List ExRicercaStoricoAvvocatiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiepModel aAvvFascMod) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepSqlDAO lAvvFasDao = null;
		List lAvvocati = new ArrayList();

		try {
			lConn = getDBConnection();

			lAvvFasDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvFasDao.ricercaStoricoAvvocatoFascicolo(aAvvFascMod);

			lAvvFasDao.start();

			while (lAvvFasDao.next()) {
				lAvvocati.add(lAvvFasDao.getModel());
			}

			lAvvFasDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Nessun avvocato associato al fascicolo.");
		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaStoricoAvvocatiFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvFasDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/*****************************************************************************
	 * Recupera l'elenco degli avvocati ATTUALMENTE assegnati a al fascicolo specificato in input
	 *
	 * @param aKey
	 *            - id del fascicolo
	 * @return vettore di AvvocatoModel
	 * @throws F3BException
	 *             - se avvocati non trovati o altro errore
	 */
	public Vector ExRicercaAvvocatiByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepSqlDAO lAvvDao = null;
		Vector lAvvocati = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(aKey);

			lAvvocati = new Vector(lAvvDao.getModels());

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Nessun avvocato associato al fascicolo.");

		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatiByFascicolo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/*****************************************************************************
	*
	************************************************************************** */
	public Vector ExRicercaAvvocatiByFascicoloNoError(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepSqlDAO lAvvDao = null;
		Vector lAvvocati = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(aKey);

			lAvvocati = new Vector(lAvvDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatiByFascicoloNoError: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/*****************************************************************************
	*
	************************************************************************** */
	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocato(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/**
	*
	*/
	public Vector ExRicercaAvvocatoPerUffApparteneza(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoPerUffApparteneza(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatoPerUffApparteneza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/*****************************************************************************
	*
	************************************************************************** */
	public Vector ExRicercaAvvocatoPaged(AvvocatoModel aAvvocato, int aPage) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoPaged(aAvvocato, aPage);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaAvvocatoPaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/**
	*
	*/
	public BigDecimal ExGetCountAvvocati(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;

		BigDecimal lCount = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.getCountAvvocati(aAvvocato);
			lAvvDao.start();
			lAvvDao.next();
			lCount = lAvvDao.getBigDecimal("HowManyRecords");
			lAvvDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(SIEPException.USER_MESSAGE, this.getClass().getName()
					+ ".ExGetCountAvvocati: Non posso leggere gli avvocati : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/*****************************************************************************
	*
	************************************************************************** */
	public Vector ExRicercaAvvocatoPerInserimento(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoPerInserimento(aAvvocato);
			lAvvDao.start();

			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}

			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");

		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatoPerInserimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	public AvvocatoSiepModel ExRicercaAvvocatoByKeyAvvocatoFasSiep(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepSqlDAO lAvvDao = null;
		AvvocatoSiepModel lAvvFasModel = new AvvocatoSiepModel();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(aKey);

			lAvvFasModel = (AvvocatoSiepModel) lAvvDao.getModelByKey();

		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatoByKeyAvvocatoFasSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvFasModel;
	}

	public AvvocatoModel ExModificaAvvocato(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoDAO(lConn);
			lAvvDao.setDAOFromModel(aAvvocato);
			lAvvDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					this.getClass().getName() + ".ExModificaAvvocato: Non posso inserire: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return aAvvocato;
	}

	/*****************************************************************************
	*
	************************************************************************** */
	public AvvocatoFascicoloSiepModel ExDeassegnaAvvocato(AvvocatoFascicoloSiepModel aAvvocato)
			throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepDAO lAvvFascDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		try {
			lConn = getDBTransaction();
			// Deassegna l'Avvocato
			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);

			lAvvFascDao.setDAOFromModelForUpdateAvvIdAvvocatoForDeassegnazione(aAvvocato);
			lAvvFascDao.update();

			// ricerco eventuale evento OE con sospensione (simeone) (OEDS in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvocato.getFasSieIdFascicoloSiep(), "LS");
			EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			boolean lIsRicalcoloScadenzario = false;
			// se cè il suddetto evento OEDS controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "revoca", aAvvocato, lEveMod, null);
			}

			// salvo l'evento OEDS per cercare il decreto di irreperibilità (OEDI in breve)
			EventoModel lSalvoEveModSimeone = new EventoModel();
			if (lEveMod != null) {
				lSalvoEveModSimeone = new EventoModel(lEveMod);
			}

			// ricerco eventuale evento decreto di irreperibilità (OEDI in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvocato.getFasSieIdFascicoloSiep(), "8BIS");
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			// se cè il suddetto evento OEDI controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "revoca", aAvvocato, lEveMod, null);
			}

			// se non cè il suddetto evento OEDI
			// riprendo l'evento salvato OEDS
			if (lEveMod == null && lSalvoEveModSimeone != null) {
				lEveMod = new EventoModel(lSalvoEveModSimeone);
			}

			List lNotificheSimeone = null;
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lNotEveDao = new NotificaEventoSqlDAO(lConn);
				lNotEveDao.ricercaNotificaByEvento(lEveMod.getIdEvento());
				lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
				lNotEveDao.stop();
			}

			// IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

			if (lNotificheSimeone != null && lIsRicalcoloScadenzario) {
				RicalcoloScadenzario(lConn, lNotificheSimeone, aAvvocato, lEveMod);
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExDeassegnaAvvocato: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExDeassegnaAvvocato: " + ex);
		} finally {
			cleanup(lAvvFascDao);
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);
			cleanup(lConn);
		}

		return aAvvocato;
	}

	public AvvocatoModel ExModificaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		StoricoAvvocatoDAO lStoricoAvvDAO = null;

		try {
			lConn = getDBTransaction();
			lAvvDao = new AvvocatoDAO(lConn);

			lAvvDao.setDAOFromModelForUpdate(aAvvocato);
			lAvvDao.update();

			// lStoricoModel = new StoricoAvvocatoModel();
			lStoricoAvvDAO = new StoricoAvvocatoDAO(lConn);

			lStoricoAvvDAO.setDAOFromModel(aStorico);
			/* BigDecimal lSequence = */lStoricoAvvDAO.insert();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					this.getClass().getName() + ".ExModificaStoricizzaAvvocato: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".ExModificaStoricizzaAvvocato: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lStoricoAvvDAO);
			cleanup(lConn);
		}

		return aAvvocato;
	}

	public AvvocatoModel ExCancellaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;
		AvvocatoSqlDAO lAvvDAO = null;
		StoricoAvvocatoDAO lStoricoAvvDAO = null;
		StoricoAvvocatoSqlDAO lStoricoSqlDAO = null;

		Vector lAvvocati = new Vector();
		AvvocatoModel lAvv = null;
		// Vector lstorici = null;

		try {
			lConn = getDBTransaction();

			lStoricoAvvDAO = new StoricoAvvocatoDAO(lConn);
			lAvvDAO = new AvvocatoSqlDAO(lConn);

			lAvv = new AvvocatoModel();
			lAvvDAO.ricercaAvvocato(aAvvocato);
			lAvvDAO.start();

			while (lAvvDAO.next()) {
				lAvvocati.add(lAvvDAO.getModel());
			}

			lAvvDAO.stop();
			lAvv = new AvvocatoModel();
			lAvv = (AvvocatoModel) lAvvocati.get(0);
			aStorico.setFlagCancellato("S");
			lStoricoAvvDAO.setDAOFromModel(aStorico);
			/* BigDecimal lSequence = */lStoricoAvvDAO.insert();

			lStoricoAvvDAO.stop();

			lStoricoSqlDAO = new StoricoAvvocatoSqlDAO(lConn);
			lStoricoSqlDAO.ricercaStoricoAvvocatoByIdAvvocato(lAvv.getIdAvvocato());
			/* lstorici = new Vector( */lStoricoSqlDAO.getModels()/* ) */;

			// delete avvocato
			lAvv.setFlagCancellato("S");

			lAvvDao = new AvvocatoDAO(lConn);
			lAvvDao.setDAOFromModelForUpdate(lAvv);
			lAvvDao.update();
			lAvvDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					this.getClass().getName() + ".ExCancellaStoricizzaAvvocato: Non posso inserire: " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lAvvDAO);
			cleanup(lStoricoAvvDAO);
			cleanup(lStoricoSqlDAO);

			cleanup(lConn);
		}

		return aAvvocato;
	}

	public void ExCancellaAvvocato(AvvocatoModel aAvvocato) throws F3BException {

		Connection lConn = null;
		AvvocatoDAO lAvvDao = null;

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoDAO(lConn);
			lAvvDao.selCondizioneUpdate(aAvvocato.getIdAvvocato());
			lAvvDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					this.getClass().getName() + ".ExCancellaAvvocato: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					this.getClass().getName() + ".ExCancellaAvvocato: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaAvvocatoFascicoloSiepbyKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepDAO lAvvFasDao = null;

		try {
			lConn = getDBConnection();
			lAvvFasDao = new AvvocatoFascicoloSiepDAO(lConn);
			lAvvFasDao.selCondizioneUpdate(aKey);
			lAvvFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName()
					+ ".ExCancellaAvvocatoFascicoloSiepbyKey: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName()
					+ ".ExCancellaAvvocatoFascicoloSiepbyKey: Non posso leggere  : " + ex);
		} finally {
			cleanup(lAvvFasDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera i dati di tutti i fascicoli su cui è attualmente impegnato dell'avvocato passato in input
	 *
	 * @param aKey
	 *            - id avvocato
	 * @return vector di AvvocatoSiepModel
	 * @throws F3BException
	 *             se avvocati non presenti o sql exception
	 */
	public Vector ExRicercaAvvocatoFascicoloSiepByKeyAvvocato(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepSqlDAO lAvvDao = null;
		Vector lAvvocati = new Vector();

		try {
			lConn = getDBConnection();

			lAvvDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoFascicoloSiepByKeyAvvocato(aKey);

			lAvvDao.start();
			while (lAvvDao.next()) {
				lAvvocati.add(lAvvDao.getModel());
			}
			lAvvDao.stop();

			if (lAvvocati.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Nessun avvocato associato al fascicolo.");
		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatoFascicoloSiepByKeyAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvocati;
	}

	/*****************************************************************************
	 * Recupera i dati dell'avvocato specificato sul fascicolo specificato
	 *
	 * @param aIdAvvocato
	 * @param aIdFascicolo
	 * @return AvvocatoSiepModel
	 * @throws F3BException
	 */
	public AvvocatoSiepModel ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(BigDecimal aIdAvvocato,
			BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;
		AvvocatoFascicoloSiepSqlDAO lAvvDao = null;
		AvvocatoSiepModel lAvvFasModel = new AvvocatoSiepModel();

		try {
			lConn = getDBConnection();

			lAvvDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAvvDao.ricercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(aIdAvvocato, aIdFascicolo);
			lAvvFasModel = (AvvocatoSiepModel) lAvvDao.getModelByKey();
			if (lAvvFasModel == null)
				throw new SIEPException(SIEPException.USER_MESSAGE, "L'avvocato non esiste.");
		} catch (DAOException daoEx) {
			throw new F3BException(this.getClass().getName()
					+ ".ExRicercaAvvocatoFascicoloSiepByKeyAvvocatoIdFascicolo: " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lAvvFasModel;
	}

	/*****************************************************************************
	 * Metodo che effettua la sostituzione di un avvocato su un fascicolo SIES
	 *
	 * @param aAvvUp
	 *            - vecchio avvocato da aggiornare (data fine validità)
	 * @param aAvvocatoIns
	 *            - nuovo avvocato da inserire
	 * @return model dell'avvocato inserito
	 * @throws F3BException
	 */
	public AvvocatoFascicoloSiepModel ExSostituzioneAvvocato(AvvocatoFascicoloSiepModel aAvvocatoUp,
			AvvocatoFascicoloSiepModel aAvvocatoIns) throws F3BException {

		Connection lConn = null;

		AvvocatoFascicoloSiepDAO lAvvFascDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		try {
			lConn = getDBTransaction();

			// Effettuo la deassegnazione dell'avvocato
			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);
			lAvvFascDao.setDAOFromModelForUpdateAvvIdAvvocatoForDeassegnazione(aAvvocatoUp);
			lAvvFascDao.update();
			lAvvFascDao.stop();

			// Effettuo l'inserimento del nuovo avvocato
			lAvvFascDao.setDAOFromModel(aAvvocatoIns);
			BigDecimal lSequence = lAvvFascDao.insert();
			lAvvFascDao.stop();
			aAvvocatoIns.setIdAvvocatoFascicoloSiep(lSequence);

			// ricerco eventuale evento OE con sospensione (simeone) (OEDS in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvocatoUp.getFasSieIdFascicoloSiep(), "LS");
			EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			boolean lIsRicalcoloScadenzario = false;
			// se cè il suddetto evento OEDS controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "sostituzione", aAvvocatoIns, lEveMod,
						aAvvocatoUp);
			}

			// salvo l'evento OEDS per cercare il decreto di irreperibilità (OEDI in breve)
			EventoModel lSalvoEveModSimeone = new EventoModel();
			if (lEveMod != null) {
				lSalvoEveModSimeone = new EventoModel(lEveMod);
			}

			// ricerco eventuale evento decreto di irreperibilità (OEDI in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvocatoUp.getFasSieIdFascicoloSiep(), "8BIS");
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			// se cè il suddetto evento OEDI controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "revoca", aAvvocatoIns, lEveMod, null);
			}

			// se non cè il suddetto evento OEDI
			// riprendo l'evento salvato OEDS
			if (lEveMod == null && lSalvoEveModSimeone != null) {
				lEveMod = new EventoModel(lSalvoEveModSimeone);
			}

			List lNotificheSimeone = null;
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lNotEveDao = new NotificaEventoSqlDAO(lConn);
				lNotEveDao.ricercaNotificaByEvento(lEveMod.getIdEvento());
				lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
				lNotEveDao.stop();
			}

			// IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

			if (lNotificheSimeone != null && lIsRicalcoloScadenzario) {
				RicalcoloScadenzario(lConn, lNotificheSimeone, aAvvocatoIns, lEveMod);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException(this.getClass().getName() + ".ExSostituzioneAvvocato: " + ex);
		} catch (Exception ex) {
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException(this.getClass().getName() + ".ExSostituzioneAvvocato: " + ex);
		} finally {
			cleanup(lAvvFascDao);
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}

		return aAvvocatoIns;
	}

	/*****************************************************************************
	 * Metodo che effettua la sostituzione di un avvocato su un fascicolo SIES
	 *
	 * @param aAvvUp
	 *            - vecchio avvocato da aggiornare (data fine validità)
	 * @param aAvvocatoIns
	 *            - nuovo avvocato da inserire
	 * @return model dell'avvocato inserito
	 * @throws F3BException
	 */
	public AvvocatoFascicoloSiepModel ExSostituzioneAvvocato(EventoNotificaModel lEveNot,
			AvvocatoFascicoloSiepModel aAvvocatoUp, AvvocatoFascicoloSiepModel aAvvocatoIns)
			throws F3BException {

		Connection lConn = null;

		AvvocatoFascicoloSiepDAO lAvvFascDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		try {
			lConn = getDBTransaction();

			// Effettuo la deassegnazione dell'avvocato
			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);
			lAvvFascDao.setDAOFromModelForUpdateAvvIdAvvocatoForDeassegnazione(aAvvocatoUp);
			lAvvFascDao.update();
			lAvvFascDao.stop();

			// Effettuo l'inserimento del nuovo avvocato

			lAvvFascDao.setDAOFromModel(aAvvocatoIns);
			BigDecimal lSequence = lAvvFascDao.insert();
			lAvvFascDao.stop();
			aAvvocatoIns.setIdAvvocatoFascicoloSiep(lSequence);

			// se inserisco un avvocato d'ufficio devo scrivere l'evento
			lEveNot = ExInserisciEventoNotifica(lSequence, lEveNot, lConn);

			lAvvFascDao = new AvvocatoFascicoloSiepDAO(lConn);
			lAvvFascDao.setEveIdEvento(lEveNot.getEvento().getIdEvento());
			lAvvFascDao.selCondizioneUpdateIdAvvFascSiep(lSequence);
			lAvvFascDao.update();
			lAvvFascDao.stop();

			// ricerco eventuale evento OE con sospensione (simeone) (OEDS in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvocatoUp.getFasSieIdFascicoloSiep(), "LS");
			EventoModel lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			boolean lIsRicalcoloScadenzario = false;
			// se cè il suddetto evento OEDS controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "sostituzione", aAvvocatoIns, lEveMod,
						aAvvocatoUp);
			}

			// salvo l'evento OEDS per cercare il decreto di irreperibilità (OEDI in breve)
			EventoModel lSalvoEveModSimeone = new EventoModel();
			if (lEveMod != null) {
				lSalvoEveModSimeone = new EventoModel(lEveMod);
			}

			// ricerco eventuale evento decreto di irreperibilità (OEDI in breve)
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByIdFascicoloDescrMotivo(aAvvocatoUp.getFasSieIdFascicoloSiep(), "8BIS");
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();

			// se cè il suddetto evento OEDI controllo e aggiorno le eventuali notifiche all'avvocato
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lIsRicalcoloScadenzario = RicalcoloNotifiche(lConn, "revoca", aAvvocatoIns, lEveMod, null);
			}

			// se non cè il suddetto evento OEDI
			// riprendo l'evento salvato OEDS
			if (lEveMod == null && lSalvoEveModSimeone != null) {
				lEveMod = new EventoModel(lSalvoEveModSimeone);
			}

			List lNotificheSimeone = null;
			if (lEveMod != null && lEveMod.getIdEvento() != null) {
				lNotEveDao = new NotificaEventoSqlDAO(lConn);
				lNotEveDao.ricercaNotificaByEvento(lEveMod.getIdEvento());
				lNotificheSimeone = new ArrayList(lNotEveDao.getModels());
				lNotEveDao.stop();
			}

			// IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

			if (lNotificheSimeone != null && lIsRicalcoloScadenzario) {
				RicalcoloScadenzario(lConn, lNotificheSimeone, aAvvocatoIns, lEveMod);
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExSostituzioneAvvocato: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException(this.getClass().getName() + ".ExSostituzioneAvvocato: " + ex);
		} finally {
			cleanup(lAvvFascDao);
			cleanup(lEveSqlDao);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}
		return aAvvocatoIns;
	}

	public EventoNotificaModel ExInserisciEventoNotifica(BigDecimal lSequence, EventoNotificaModel aEvento,
			Connection aConn) throws F3BException {

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {

			lEveDao = new EventoDAO(aConn);
			lAutDao = new AutoritaEsternaDAO(aConn);
			lNotDao = new NotificaDAO(aConn);
			lCampoNotaDao = new CampoNotaDAO(aConn);
			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(aConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
			aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEvento.getEvento());

			BigDecimal lKeyEvento = lEveDao.insert();
			lEveRet.getEvento().setIdEvento(lKeyEvento);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvento != null && aEvento.getNotifiche() != null) {

				while (count < aEvento.getNotifiche().length) {
					if (aEvento.getNotifiche()[count] != null) {

						if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);
						lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
						lNotDao.setAvvIdAvvocatoFascicoloSiep(lSequence);
						lNotDao.insert();
						lNotDao.stop();
					}
					count++;
				}
			}

			// Inserimento delle eventuali note aggiuntive.
			if (aEvento.getCampoNote() != null) {
				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
		}

		return lEveRet;
	}

	/**
	 * Stampa un documento di Avvocato
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoAvvocato(EventoModel aEvento,
			AvvocatoFascicoloSiepModel aAvvocatoSiep, String aNomeTemplate, UtenteModel aUtenteModel)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBTransaction();
			IStampa lStampa = SICOLookupRemote.getStampaRemote();
			TreeModel lTree = lStampa.prelevaDatiAvvocato(aAvvocatoSiep, aUtenteModel);
			ReportGenerator lReport = new ReportGenerator();
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			aEvento.setDocBlobIn(lByteArrayInput);

			// paolo aggiunta gestione evento
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento);
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					this.getClass().getName() + ".ExStampaDocumentoAvvocato: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/*****************************************************************************
	 * Recupera tutti i fori caricati per caricare la combo
	 *
	 */
	public Vector ExRicercaForiCaricati() throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lFori = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaForiCaricati();
			lAvvDao.start();

			while (lAvvDao.next()) {
				lFori.add(lAvvDao.getModelForo());
			}

			lAvvDao.stop();

			if (lFori.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Nessun Foro trovato, impossibile caricare gli avvocati");
		} catch (DAOException daoEx) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaForiCaricati: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lFori;
	}

	/*****************************************************************************
	 * Recupera tutti i fori caricati per caricare la combo
	 *
	 */
	public Vector ExRicercaForiDisponibili() throws F3BException {

		Connection lConn = null;
		AvvocatoSqlDAO lAvvDao = null;
		Vector lFori = new Vector();

		try {
			lConn = getDBConnection();
			lAvvDao = new AvvocatoSqlDAO(lConn);
			lAvvDao.ricercaForiDisponibili();
			lAvvDao.start();

			while (lAvvDao.next()) {
				lFori.add(lAvvDao.getModelForo());
			}

			lAvvDao.stop();

			if (lFori.size() == 0)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Nessun Foro trovato, impossibile caricare gli avvocati");
		} catch (DAOException daoEx) {
			throw new F3BException(
					this.getClass().getName() + ".ExRicercaForiDisponibili: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lAvvDao);
			cleanup(lConn);
		}

		return lFori;
	}

	private void RicalcoloScadenzario(Connection lConn, List lListNotifiche,
			AvvocatoFascicoloSiepModel aAvvocato, EventoModel lEveModLS) throws F3BException {

		ScadenzarioDAO lScaDao = null;
		try {

			IOrdineEsecuzione lCtrlOE = SIEPLookupRemote.getOrdineEsecuzioneRemote();

			// Calcola la Data di Avvenuta notifica massima su tutte le notifiche utili
			Date lDataAvvenutaNotifica = lCtrlOE
					.calcolaDataMaggiore((NotificaModel[]) lListNotifiche.toArray(new NotificaModel[0]));

			// Calcola periodo feriale opzionale sulla data scadenza
			Date lDataFineScadenza = null;
			if (lDataAvvenutaNotifica != null) {
				lDataFineScadenza = lCtrlOE.calcolaPeriodoFeriale(lConn, lDataAvvenutaNotifica,
						aAvvocato.getCodUfficioAggiornamento());
			}

			// Cerca l'eventuale ultimo Rinnovo legato alla notifica al condannato
			RinnovoModel lRinnovo = null;
			/*
			 * if( lIdNotificaCondannato != null ) { lRinSqlDao = new RinnovoSqlDAO(lConn);
			 *
			 * lRinSqlDao.ricercaRinnovoIdNotificaCodTipoRinnovo(lIdNotificaCondannato, null); lRinnovo =
			 * (RinnovoModel)lRinSqlDao.getModelByKey(); }
			 */

			// Controlla l'esistenza dello scadenzario
			lScaDao = new ScadenzarioDAO(lConn);
			lScaDao.setCondizioneByIdFascicoloSiepTipoScadenzario(aAvvocato.getFasSieIdFascicoloSiep(), "01");
			ScadenzarioModel lScaMod = (ScadenzarioModel) lScaDao.getModelByKey();
			lScaDao.stop();

			lScaDao.setCodTipoScadenzario("01");
			lScaDao.setDataInizioScadenza(lDataAvvenutaNotifica);
			lScaDao.setDataFineScadenza(lDataFineScadenza);
			lScaDao.setFlagVisto("N");

			// lScaDao.setCodOperatoreInserimento(aAvvocato.getCodOperatoreAggiornamento());
			// lScaDao.setCodUfficioInserimento(aAvvocato.getCodUfficioAggiornamento());
			// lScaDao.setDataInserimento(DateUtils.getSysDate());

			lScaDao.setFasSieIdFascicoloSiep(aAvvocato.getFasSieIdFascicoloSiep());
			// lScaDao.setNotIdNotifica(IdNotifiche[i].getIdNotifica());

			// Inserisce/Modifica lo scadenzario
			lScaDao.setCodStatoNotifica(
					lCtrlOE.calcolaCodStatoNotifica(lListNotifiche, lDataAvvenutaNotifica, lRinnovo));

			if (lScaMod == null && lDataAvvenutaNotifica != null) {
				// paolo 02/09/2010 non va inserito lo scadenzario poichè se è stato cancellato un motivo ci
				// sarà
				// e poi lo crea errato con i sotto riportati campi vuoti generando anche il problema con il
				// trigger
				// cancella CANCELLA_SCADENZARIO che sta sulla tabella EVENTO
				// lScaDao.setCodOperatoreInserimento(aAvvocato.getCodOperatoreAggiornamento());
				// lScaDao.setCodUfficioInserimento(aAvvocato.getCodUfficioAggiornamento());
				// lScaDao.setDataInserimento(DateUtils.getSysDate());
				// lScaDao.insert();
			} else if (lScaMod != null && lScaMod.getIdScadenzario() != null
					&& lDataAvvenutaNotifica != null) {
				lScaDao.setCodOperatoreAggiornamento(aAvvocato.getCodOperatoreAggiornamento());
				lScaDao.setCodUfficioAggiornamento(aAvvocato.getCodUfficioAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
			} else if (lScaMod != null && lScaMod.getIdScadenzario() != null
					&& lDataAvvenutaNotifica == null) {
				lScaDao.setCodOperatoreAggiornamento(aAvvocato.getCodOperatoreAggiornamento());
				lScaDao.setCodUfficioAggiornamento(aAvvocato.getCodUfficioAggiornamento());
				lScaDao.setDataAggiornamento(DateUtils.getSysDate());
				lScaDao.setDataInizioScadenza(lEveModLS.getDataEmissione());
				lScaDao.setCondizioneUpdate(lScaMod.getIdScadenzario());
				lScaDao.update();
			}
			lScaDao.stop();
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".RicalcoloScadenzario: " + ex);
		} finally {
			cleanup(lScaDao);
		}
	}

	private boolean RicalcoloNotifiche(Connection lConn, String lAzione,
			AvvocatoFascicoloSiepModel aAvvCorrente, EventoModel lEveMod,
			AvvocatoFascicoloSiepModel aAvvSostituito) throws F3BException {

		NotificaEventoSqlDAO lNotEveDao = null;
		NotificaDAO lNotDao = null;

		try {
			boolean lIsRicalcoloScadenzario = false;
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lNotEveDao.ricercaNotificaByEvento(lEveMod.getIdEvento());
			List lNotifiche = new ArrayList(lNotEveDao.getModels());
			if (!lNotifiche.isEmpty()) {
				int nNotifice = 0;
				for (Iterator lIter = lNotifiche.iterator(); lIter.hasNext();) {
					NotificaModel lNotMod = (NotificaModel) lIter.next();
					if (lNotMod != null && lNotMod.getIdNotifica() != null
							&& "N".equals(lNotMod.getCodTipoNotifica()))
						nNotifice++; // conto notifiche agli avvocati
				}

				for (Iterator lIter = lNotifiche.iterator(); lIter.hasNext();) {
					NotificaModel lNotMod = (NotificaModel) lIter.next();
					if (lNotMod != null && lNotMod.getIdNotifica() != null
							&& "N".equals(lNotMod.getCodTipoNotifica())) {
						if ("revoca".equals(lAzione) && aAvvCorrente.getIdAvvocatoFascicoloSiep()
								.equals(lNotMod.getAvvIdAvvocatoFascicoloSiep())) {
							// revoca o deassegna
							if (nNotifice == 2) {
								// se ci sono 2 notifiche
								if (lNotMod.getDataAvvenutaNotifica() == null) {
									// e quella relativa all'avvocato revocato è senza data
									// metto data ins, sbianco id e ricalcolo scadenzario
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									lNotDao.setDataAvvenutaNotifica(lNotMod.getDataInserimento());
									lNotDao.setAvvIdAvvocatoFascicoloSiep(null);
									lNotDao.update();
									lNotDao.stop();
									lIsRicalcoloScadenzario = true;
									break;
								} else {
									// e quella relativa all'avvocato revocato è con data
									// metto data ins, sbianco id e ricalcolo scadenzario
									// annullo con X
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									lNotDao.setDataAvvenutaNotifica(lNotMod.getDataInserimento());
									lNotDao.setAvvIdAvvocatoFascicoloSiep(null);
									lNotDao.setCodTipoNotifica("X");
									lNotDao.update();
									lNotDao.stop();
									lIsRicalcoloScadenzario = true;
									break;
								}
							} else {
								// se la notifica è una sola
								if ("02".equals(aAvvCorrente.getCodTipoAvvocato())) {
									// se l'avvocato è di fiducia e la data notifica è piena non faccio niente
									// se la data è vuota inserisco data fittizia sbianco idavvocato per
									// annullare
									// la notifica e ricalcolo

									if (lNotMod.getDataAvvenutaNotifica() == null) {
										lNotDao = new NotificaDAO(lConn);
										lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
										lNotDao.setDataAvvenutaNotifica(lNotMod.getDataInserimento());
										lNotDao.setAvvIdAvvocatoFascicoloSiep(null);
										lNotDao.update();
										lNotDao.stop();
										lIsRicalcoloScadenzario = true;
										break;
									}
								} else {
									// se l'avvocato non è di fiducia sbianco idavvocato e data
									// se la data era piena ricalcolo
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									if (lNotMod.getDataAvvenutaNotifica() != null)
										lIsRicalcoloScadenzario = true;
									lNotDao.setDataAvvenutaNotifica(null);
									lNotDao.setAvvIdAvvocatoFascicoloSiep(null);
									lNotDao.update();
									lNotDao.stop();
									break;
								}
							}
						} // fine caso revoca

						if ("assegna".equals(lAzione) && lNotMod.getAvvIdAvvocatoFascicoloSiep() == null) {
							// assegna e IDavvocato è vuoto
							if ("02".equals(aAvvCorrente.getCodTipoAvvocato())) {
								// se l'avvocato è di fiducia e la data notifica è vuota
								// metto una data fittizia tanto per annulare la notifica e ricalcolo
								// se data piena non faccio niente
								if (lNotMod.getDataAvvenutaNotifica() == null) {
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									lNotDao.setDataAvvenutaNotifica(lNotMod.getDataInserimento());
									lNotDao.update();
									lNotDao.stop();
									lIsRicalcoloScadenzario = true;
									break;
								}
							} else {
								// l'avvocato è d'ufficio o giudizio e
								// la data è vuota vuota lo riempio con il nuovo
								if (lNotMod.getDataAvvenutaNotifica() == null) {
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									lNotDao.setAvvIdAvvocatoFascicoloSiep(
											aAvvCorrente.getIdAvvocatoFascicoloSiep());
									lNotDao.update();
									lNotDao.stop();
									break;
								}
							}
						} // fine caso assegna

						if ("sostituzione".equals(lAzione) && aAvvSostituito.getIdAvvocatoFascicoloSiep()
								.equals(lNotMod.getAvvIdAvvocatoFascicoloSiep())) {
							// sostituzione
							if ("02".equals(aAvvCorrente.getCodTipoAvvocato())) {
								// se l'avvocato sostituente è di fiducia sbianco idAvvocato
								// e se data notifica vuota riempo con data fittizia (altrimenti niente)
								// tanto per annullare la notifica
								if (lNotMod.getDataAvvenutaNotifica() == null) {
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									lNotDao.setDataAvvenutaNotifica(lNotMod.getDataInserimento());
									lNotDao.setAvvIdAvvocatoFascicoloSiep(null);
									lNotDao.update();
									lNotDao.stop();
									lIsRicalcoloScadenzario = true;
									break;
								}
							} else {
								if ("02".equals(aAvvSostituito.getCodTipoAvvocato())
										&& lNotMod.getDataAvvenutaNotifica() != null) {
									// se l'avvocato sostitutito è di fiducia e la data notifica e piena
									// non faccio niente
								} else {
									// se l'avvocato sostituente è di giudizio o di ufficio cambio
									// idavvocato con il nuovo sbianco data notifica
									// se la data era piena ricalcolo
									lNotDao = new NotificaDAO(lConn);
									lNotDao.setCondizioneUpdate(lNotMod.getIdNotifica());
									if (lNotMod.getDataAvvenutaNotifica() != null)
										lIsRicalcoloScadenzario = true;
									lNotDao.setDataAvvenutaNotifica(null);
									lNotDao.setAvvIdAvvocatoFascicoloSiep(
											aAvvCorrente.getIdAvvocatoFascicoloSiep());
									lNotDao.update();
									lNotDao.stop();
									break;
								}
							} // fine caso sostituzione
						}
					}
				}
			}
			lNotEveDao.stop();

			return lIsRicalcoloScadenzario;
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".RicalcoloNotifiche: " + ex);
		} finally {
			cleanup(lNotEveDao);
			cleanup(lNotDao);
		}
	}

}