package siap.sius.trasmissioneatti.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoNotificaModel;
//import siap.sico.passaggioevento.dao.PassaggioEventoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;

/**
 * <p>
 * Title: TrasmissioneAttiController
 * </p>
 * <p>
 * Description: Classe Controller per TrasmissioneAtti
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
public class TrasmissioneAttiController extends SiapController implements ITrasmissioneAtti {

	/**
	 * Trasmissione dell'atto: Aggiornamento del fascicolo SIUS, del GENERALE PROCEDIMENTO; Inserimento
	 * dell'EVENTO .
	 *
	 * @param aFascicoloGPModel
	 * @param aEventoNotificaModel
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExTrasmettiAtto(FascicoloGPModel aFascicoloGPModel,
			EventoNotificaModel aEventoNotificaModel) throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NotificaDAO lNotDao = null;
		NotificaSqlDAO lNotSqlDao = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lNotSqlDao = new NotificaSqlDAO(lConn);
			// lPasEveDao = new PassaggioEventoDAO(lConn);

			// Aggiornamento del FASCICOLO SIUS.
			// Setto il DAO dal Model ed aggiorno il FascicoloSius.
			lFasDao.setDAOFromModel(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDao.setCondizioneUpdate(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.update();

			// Aggiornamento del GENERALE PROCEDIMENTO.
			// Setto il DAO dal Model ed aggiorno il Generale Procedimento.
			lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
			lGenProDao.update();

			// Inserimento dell'EVENTO E della NOTIFICA.
			// Setto il DAO dal Model ed inserisco l'Evento.
			lEveDao.setDAOFromModel(aEventoNotificaModel.getEvento());
			BigDecimal lIdEvento = lEveDao.insert();
			aEventoNotificaModel.getEvento().setIdEvento(lIdEvento);

			// Setto il DAO dal Model ed inserisco la Notifica.
			lNotDao.setDAOFromModel(aEventoNotificaModel.getNotifiche()[0]);
			// L'EveIdEvento viene caricato della chiave ottenuta dalla insert di Evento.
			lNotDao.setEveIdEvento(lIdEvento);
			BigDecimal lChiave = lNotDao.insert();
			// Dopo l'inserimento di notifica, carico anche il model dei 2 ID mancanti.
			(aEventoNotificaModel.getNotifiche())[0].setEveIdEvento(lIdEvento);
			(aEventoNotificaModel.getNotifiche())[0].setIdNotifica(lChiave);

			// Setto il DAO dal Model ed inserisco il PassaggioEvento
			// aEventoNotificaModel.getPassaggioEvento().setEveIdEvento(lIdEvento);
			// lPasEveDao.setDAOFromModel(aEventoNotificaModel.getPassaggioEvento());

			// L'EveIdEvento viene caricato a seguito della insert di Evento.
			// lChiave = lPasEveDao.insert();
			// (aEventoNotificaModel.getPassaggioEvento()).setIdPassaggioEvento(lChiave);

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("TtasmissioneAttiController.ExTrasmettiAtto: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lGenProDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNotDao);
			cleanup(lNotSqlDao);
			cleanup(lConn);
		}

		return aEventoNotificaModel;
	}

}