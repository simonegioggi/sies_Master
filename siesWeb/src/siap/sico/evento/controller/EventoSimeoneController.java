package siap.sico.evento.controller;

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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSimeoneSqlDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoFascicoloModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepPerEventoSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.cumulo.dao.CumuloDAO;
import siap.siep.cumulo.dao.CumuloSqlDAO;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacumulo.dao.PenaCumuloSqlDAO;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.riepilogoprovvedimento.dao.RiepilogoProvvedimentoSqlDAO;
import siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: Evento Simeone Controller
 * </p>
 * <p>
 * Description: Classe Controller per Evento
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
public class EventoSimeoneController extends SiapController implements IEventoSimeone {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce Evento Notifica e Autorita Esterne associate
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByIdFascicoloDescrMotivo(BigDecimal aKeyFasc,
			String aMotivo) throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEve = null;

		EventoSqlDAO lEveDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		CSSASqlDAO lCssaDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvSiusDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		MagistratoSqlDAO lMagDAO = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lMagDAO = new MagistratoSqlDAO(lConn);

			lEveDao.ricercaEventoByIdFascicoloDescrMotivo(aKeyFasc, aMotivo);
			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve != null && lEve.getEvento() != null) {
				if (lEve.getEvento().getCodMagistrato() != null) {
					lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
					MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
					lEve.setMagistrato(lMag);
				}

				lUffDao = new UfficioSqlDAO(lConn);
				lCssaDao = new CSSASqlDAO(lConn);
				lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
				lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

				lNotEveDao.ricercaNotificaByEvento(lEve.getEvento().getIdEvento());
				Vector lNotifiche = new Vector(lNotEveDao.getModels());
				lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

				Vector lAvvocati = new Vector();
				Vector lAvvocatiSius = new Vector();

				// Verfica ed inserisce le Autorita Esterne e gli uffici e gli Avvocati
				int count = 0;
				while (count < lEve.getNotifiche().length) {
					// Autorita Esterne
					if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
						lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
								lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
						AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao
								.getModelByKey();

						// Inserisce l'occorenza nel model delle notifiche.
						lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
						lAutoritaSqlDao.stop();
					}

					// Autorita Esterne Delegate
					if (lEve.getNotifiche()[count].getAutEstIdAutoritaEstDeleg() != null) {
						lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
								lEve.getNotifiche()[count].getAutEstIdAutoritaEstDeleg());
						AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao
								.getModelByKey();

						// Inserisce l'occorenza nel model delle notifiche.
						lEve.getNotifiche()[count].setAutoritaEsternaDelegata(lAutorita);
						lAutoritaSqlDao.stop();
					}

					if (lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
							&& !lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
						lIstDao.ricercaIstitutoDetenzioneByKey(
								lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
						IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();

						// Inserisce l'occorenza nel model delle notifiche.
						lEve.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
						lIstDao.stop();
					}
					// fine modifica relativa al tipo istituto

					// Preleva gli uffici
					if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
						lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
						UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
						// Inserisce l'occorrenza nel model delle notifiche.
						lEve.getNotifiche()[count].setUfficio(lUffMod);
						lUffDao.stop();
					}

					// Preleva il Cssa
					if (lEve.getNotifiche()[count].getCssIdCssa() != null) {
						lCssaDao.selModelCssabyKey(lEve.getNotifiche()[count].getCssIdCssa());
						CSSAModel lCssaMod = (CSSAModel) lCssaDao.getModelByKey();
						// Inserisce l'occorrenza nel model delle notifiche.
						lEve.getNotifiche()[count].setCSSA(lCssaMod);
						lCssaDao.stop();
					}

					// Preleva gli avvocati
					if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
						lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);
						lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
								lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
						AvvocatoSiepModel lAvvSiep = (AvvocatoSiepModel) lAvvDao.getModelByKey();
						lAvvocati.add(lAvvSiep);

						// Aggiunge l'AvvocatoSiepModel al model di Notifica
						lEve.getNotifiche()[count].setAvvSiep(lAvvSiep);

					}
					// Preleva gli avvocati SIUS
					if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius() != null) {
						lAvvSiusDao = new AvvocatoFascicoloSiusSqlDAO(lConn);

						lAvvSiusDao.ricercaAvvocatoByKeyAvvocatoFasSius(
								lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius());
						AvvocatoSiusModel lAvvSius = (AvvocatoSiusModel) lAvvSiusDao.getModelByKey();
						lAvvocatiSius.add(lAvvSius);

						// Aggiunge l'AvvocatoSiusModel al model di Notifica
						lEve.getNotifiche()[count].setAvvSius(lAvvSius);
					}

					count++;
				}

				lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

				if (lAvvocati.size() > 0)
					lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));
				if (lAvvocatiSius.size() > 0)
					lEve.setAvvocatiSius(
							(AvvocatoSiusModel[]) lAvvocatiSius.toArray(new AvvocatoSiusModel[0]));
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lAvvSiusDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);
			cleanup(lCssaDao);
			cleanup(lIstDao);

			cleanup(lConn);
		}

		return lEve;
	}

	/**
	 * Ricerca evento dal fascicolo e dal tipo evento, tipo provvedimento, motivo
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aMotivo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo(BigDecimal aFascKey,
			String aTipoEvento, String aTipoProv, String aMotivo) throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lEveDao = null;
		// EventoNotificaModel lEve = null;
		EventoModel lEvento = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSimeoneSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvCodMotivo(aFascKey, aTipoEvento, aTipoProv, aMotivo);

			lEveDao.start();
			if (lEveDao.next())
				lEvento = (EventoModel) lEveDao.getModelEvento();
			lEveDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	// 27/03/2019 MEV70
	/**
	 * Ricerca eventi dal fascicolo e dal tipo evento, tipo provvedimento, motivo
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aMotivo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventiByFascicoloSiepTipEventoTipProvCodMotivo(BigDecimal aFascKey,
			String aTipoEvento, String aTipoProv, String aMotivo) throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lEveDao = null;
		// EventoNotificaModel lEve = null;
		// EventoModel lEvento = null;
		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSimeoneSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvCodMotivo(aFascKey, aTipoEvento, aTipoProv, aMotivo);
			lEveDao.start();

			while (lEveDao.next()) {
				EventoModel lEveMod = null;

				lEveMod = (EventoModel) lEveDao.getModel();
				// **** get Blob ****
				lEveMod.setDocBlobOut(lEveDao.getBlob("DOC_BLOB"));
				lEventi.add(lEveMod);
			}

			// if (lEventi.size() == 0)
			// throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoSimeoneController.ExRicercaEventiByFascicoloSiepTipEventoTipProvCodMotivo: "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca evento dal fascicolo e dal tipo evento, tipo provvedimento, motivo
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aMotivo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoByFascicoloSiepTipProv(BigDecimal aFascKey, String[] aTipoProv)
			throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lEveDao = null;
		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSimeoneSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipProvDesc(aFascKey, aTipoProv);
			lEveDao.start();
			while (lEveDao.next()) {
				lEventi.add(lEveDao.getModelEvento());
			}
			lEveDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepTipProv: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	/**
	 * Ricerca Notifica per irreperibilità
	 *
	 * @param aKeyFasc
	 * @param aMotivo
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExRicercaEventoNotificaByIdFascicoloCodiceMotivo(BigDecimal aKeyFasc,
			String[] aMotivo) throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEve = null;

		EventoSqlDAO lEveDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		CSSASqlDAO lCssaDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvSiusDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		MagistratoSqlDAO lMagDAO = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lMagDAO = new MagistratoSqlDAO(lConn);

			lEveDao.ricercaEventoByIdFascicoloCodiceMotivo(aKeyFasc, aMotivo);
			lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());

			if (lEve != null && lEve.getEvento() != null) {
				if (lEve.getEvento().getCodMagistrato() != null) {
					lMagDAO.ricercaMagistratoByCod(lEve.getEvento().getCodMagistrato());
					MagistratoModel lMag = (MagistratoModel) lMagDAO.getModelByKey();
					lEve.setMagistrato(lMag);
				}

				lUffDao = new UfficioSqlDAO(lConn);
				lCssaDao = new CSSASqlDAO(lConn);
				lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
				lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);

				lNotEveDao.ricercaNotificaByEvento(lEve.getEvento().getIdEvento());
				Vector lNotifiche = new Vector(lNotEveDao.getModels());
				lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

				Vector lAvvocati = new Vector();
				Vector lAvvocatiSius = new Vector();

				// Verfica ed inserisce le Autorita Esterne e gli uffici e gli Avvocati
				int count = 0;
				while (count < lEve.getNotifiche().length) {
					// Autorita Esterne
					if (lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
						lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
								lEve.getNotifiche()[count].getAutEstIdAutoritaEsterna());
						AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao
								.getModelByKey();

						// Inserisce l'occorenza nel model delle notifiche.
						lEve.getNotifiche()[count].setAutoritaEsterna(lAutorita);
						lAutoritaSqlDao.stop();
					}

					if (lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
							&& !lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
						lIstDao.ricercaIstitutoDetenzioneByKey(
								lEve.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
						IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();

						// Inserisce l'occorenza nel model delle notifiche.
						lEve.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
						lIstDao.stop();
					}
					// fine modifica relativa al tipo istituto

					// Preleva gli uffici
					if (lEve.getNotifiche()[count].getUffCodUfficio() != null) {
						lUffDao.selUfficioByCod(lEve.getNotifiche()[count].getUffCodUfficio());
						UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
						// Inserisce l'occorrenza nel model delle notifiche.
						lEve.getNotifiche()[count].setUfficio(lUffMod);
						lUffDao.stop();
					}

					// Preleva il Cssa
					if (lEve.getNotifiche()[count].getCssIdCssa() != null) {
						lCssaDao.selModelCssabyKey(lEve.getNotifiche()[count].getCssIdCssa());
						CSSAModel lCssaMod = (CSSAModel) lCssaDao.getModelByKey();
						// Inserisce l'occorrenza nel model delle notifiche.
						lEve.getNotifiche()[count].setCSSA(lCssaMod);
						lCssaDao.stop();
					}

					// Preleva gli avvocati
					if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep() != null) {
						lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(lConn);
						lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(
								lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSiep());
						AvvocatoSiepModel lAvvSiep = (AvvocatoSiepModel) lAvvDao.getModelByKey();
						lAvvocati.add(lAvvSiep);

						// Aggiunge l'AvvocatoSiepModel al model di Notifica
						lEve.getNotifiche()[count].setAvvSiep(lAvvSiep);

					}
					// Preleva gli avvocati SIUS
					if (lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius() != null) {
						lAvvSiusDao = new AvvocatoFascicoloSiusSqlDAO(lConn);

						lAvvSiusDao.ricercaAvvocatoByKeyAvvocatoFasSius(
								lEve.getNotifiche()[count].getAvvIdAvvocatoFascicoloSius());
						AvvocatoSiusModel lAvvSius = (AvvocatoSiusModel) lAvvSiusDao.getModelByKey();
						lAvvocatiSius.add(lAvvSius);

						// Aggiunge l'AvvocatoSiusModel al model di Notifica
						lEve.getNotifiche()[count].setAvvSius(lAvvSius);
					}
					count++;

				}

				lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

				if (lAvvocati.size() > 0)
					lEve.setAvvocati((AvvocatoSiepModel[]) lAvvocati.toArray(new AvvocatoSiepModel[0]));
				if (lAvvocatiSius.size() > 0)
					lEve.setAvvocatiSius(
							(AvvocatoSiusModel[]) lAvvocatiSius.toArray(new AvvocatoSiusModel[0]));
			}
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lUffDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lAvvDao);
			cleanup(lAvvSiusDao);
			cleanup(lMagDAO);
			cleanup(lNotEveDao);
			cleanup(lCssaDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lEve;
	}

	/**
	 * - Inserisce o aggiorna il Provvedimento di Cumulo. - Aggancia il record CUMULO al Provvedimento -
	 * Inserisce le Notifiche - Aggancia le LA al Provvedimento
	 *
	 * @param aFascicolo
	 * @param aEvento
	 *            - Provvedimento di Cumulo e relative Notifiche
	 * @param aCumulo
	 *            - Cumulo Model
	 */
	public EventoNotificaModel ExInseriscioModificaEventoNotificaCumulo(FascicoloSiepModel aFascicolo,
			EventoNotificaModel aEvento, CumuloModel aCumulo) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CumuloDAO lCumDao = null;
		CumuloSqlDAO lCumSql = null;
		LicenzaLibanticipataDAO lLibDAO = null;
		LicenzaLibanticipataSqlDAO lLibSqlDAO = null;
		PenaCumuloSqlDAO lPenSqlDao = null;

		EventoModel lEveMod = new EventoModel();
		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lCumSql = new CumuloSqlDAO(lConn);
			lCumDao = new CumuloDAO(lConn);

			// ========================================================================
			// Ricerca l'ultimo Provvedimento del tipo che devo Inserire/Aggiornare
			//
			// ========================================================================
			String lMotivo[] = { aEvento.getEvento().getCodMotivo() };
			lSqlDAO.ricercaEventoPerMotivo(lMotivo, aEvento.getEvento());
			lEveMod = (EventoModel) lSqlDAO.getModelByKey();

			BigDecimal lKeyEvento = null;

			// Se l'ultimo provvedimento è NON VALIDATO, lo aggiorno
			if (lEveMod != null && (lEveMod.getFlagDocumentoRegistrato() == null
					|| lEveMod.getFlagDocumentoRegistrato().equals("N"))) {
				lKeyEvento = lEveMod.getIdEvento();
				lEveRet.getEvento().setIdEvento(lKeyEvento);

				lEveMod.setFlagDocumentoRegistrato(null);
				lEveDao.setDAOFromModelForUpdate(lEveMod);

				lEveDao.setDataEmissione(aEvento.getEvento().getDataEmissione());
				lEveDao.setDataTrasmissioneAtti(aEvento.getEvento().getDataTrasmissioneAtti());
				lEveDao.setCodUfficioEmittente(aEvento.getEvento().getCodUfficioEmittente());
				lEveDao.setCodLuogoEmittente(aEvento.getEvento().getCodLuogoEmittente());
				lEveDao.setCodMagistrato(aEvento.getEvento().getCodMagistrato());
				lEveDao.setDataAggiornamento(DateUtils.getSysDate());
				lEveDao.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreInserimento());
				lEveDao.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioInserimento());
				lEveDao.update();
				lEveDao.stop();

				// cancello le notifiche associate all'evento
				lNotDao.setCondizioneEvento(lKeyEvento);
				lNotDao.delete();
				lNotDao.stop();
			} else // Se non presente lo inserisco
			{
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());
				lEveDao.setDataInserimento(DateUtils.getSysDate());
				lKeyEvento = lEveDao.insert();
				lEveDao.stop();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
			}

			// ========================================================================
			// CUMULO - Ricerco l'ultimo cumulo non validato.
			// Aggiorno il tipo stampa (tipo provvedimento) e lo aggancio al
			// provvedimento di cumulo.
			//
			// ========================================================================
			lCumSql.ricercaFascicoliCumulobyIdFascicoloSiepFlagValidato(aCumulo.getFasSieIdFascicoloSiep());
			CumuloModel lCumMod = new CumuloModel();
			lCumMod = (CumuloModel) lCumSql.getModelByKey();

			if (lCumMod != null && lCumMod.getIdCumulo() != null) {
				lCumDao.setFlagTipoStampa(aCumulo.getFlagTipoStampa());
				lCumDao.setCondizioneUpdate(lCumMod.getIdCumulo());
				lCumDao.setEveIdEvento(lKeyEvento);

				/*
				 * modifica 12-06-06 -- Dario -- Viviana -- serve per gestire la cancellazione e
				 * l'annullamento, se PrimoCumulo è uguale a 'P' vuol dire che posso togliere dal fascicolo la
				 * 'S' del flagCumulante perchè esiste solo questo di cumulo su quel fascicolo, mentre se
				 * PrimoCumulo è uguale ad 'A'non posso togliere dal fascicolo il flagCumulante ad 'S' perchè
				 * ci sono altri cumuli su quel fascicolo!!!
				 */
				if ("S".equals(aFascicolo.getFlagCumulante()))
					lCumDao.setPrimoCumulo("A");
				else
					lCumDao.setPrimoCumulo("P");

				lCumDao.setDataAggiornamento(DateUtils.getSysDate());
				lCumDao.setCodOperatoreAggiornamento(aEvento.getEvento().getCodOperatoreInserimento());
				lCumDao.setCodUfficioAggiornamento(aEvento.getEvento().getCodUfficioInserimento());

				lCumDao.update();
				lCumDao.stop();
			}

			// ========================================================================
			// Inserisci le notifiche
			// ========================================================================
			BigDecimal lKeyAutorita = null;
			int count = 0;
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
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ========================================================================
			// Aggiorna liberazione anticipata legandola all'evento inserito
			// Recupera l'ultimo record con flag a N
			// n.b. se non sono state inserite LA con il cumulo, recupera le ultime
			// non validate????
			// n.b. vanno recuperate le SOLE LA iscritte in cumulo in annotazione dati
			// finali le quali non sono collegate ad alcun evento, al più all'evento
			// di cumulo
			//
			// ========================================================================
			/*
			 * lLibSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);
			 * lLibSqlDAO.ricercaLicenzaLibanticipataConcesseByIDFascicoloSIEP
			 * (aFascicolo.getIdFascicoloSiep(), "N");
			 *
			 * lLicMod = (LicenzaLibAnticipataModel) lLibSqlDAO.getModelByKey(); if ( lLicMod != null ) {
			 * lLibDAO = new LicenzaLibanticipataDAO(lConn); lLicMod.setEveIdEvento(lKeyEvento);
			 * lLibDAO.setDAOFromModelForUpdate(lLicMod); lLibDAO.update(); lLibDAO.stop(); }
			 */

			// ========================================================================
			// Nuova versione!! Il record LICENZA_LIBANTICIPATA viene iscritto
			// contestualmente al provvedimento ed ad esso collegato. I GG vengono
			// recuperati dal record PENA_CUMULO
			// ========================================================================
			if (lCumMod != null && lCumMod.getIdCumulo() != null) {
				PenaCumuloModel lPenCumMod = null;
				lPenSqlDao = new PenaCumuloSqlDAO(lConn);
				lPenSqlDao.ricercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
				lPenCumMod = (PenaCumuloModel) lPenSqlDao.getModelByKey();

				if (lPenCumMod.getNumGiorniLibAnticipataLA() != null
						&& lPenCumMod.getNumGiorniLibAnticipataLA().compareTo(new BigDecimal(0)) != 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco LA e le aggancio al provv di cumulo");
					LicenzaLibAnticipataModel libAntMod = new LicenzaLibAnticipataModel();

					libAntMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					libAntMod.setEveIdEvento(lKeyEvento);

					libAntMod.setFlagConcesso("C");
					libAntMod.setFlagElaborato("N");
					libAntMod.setCodTipoLicenza("LA");

					libAntMod.setNumeroGiorni(lPenCumMod.getNumGiorniLibAnticipataLA()); // Nuova L.A.
					libAntMod.setDescrStatoPermesso("LA"); // Nuova L.A.

					libAntMod.setCodUfficioInserimento(lPenCumMod.getCodUfficioInserimento());
					libAntMod.setCodOperatoreInserimento(lPenCumMod.getCodOperatoreInserimento());
					libAntMod.setDataInserimento(lPenCumMod.getDataInserimento());

					lLibDAO = new LicenzaLibanticipataDAO(lConn);
					lLibDAO.setDAOFromModel(libAntMod);
					lLibDAO.insert();
					lLibDAO.stop();
				}

				if (lPenCumMod.getNumGiorniLibAnticipataSPE() != null
						&& lPenCumMod.getNumGiorniLibAnticipataSPE().compareTo(new BigDecimal(0)) != 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco LA SPECIALE e le aggancio al provv di cumulo");
					LicenzaLibAnticipataModel libAntMod = new LicenzaLibAnticipataModel();

					libAntMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					libAntMod.setEveIdEvento(lKeyEvento);

					libAntMod.setFlagConcesso("C");
					libAntMod.setFlagElaborato("N");
					libAntMod.setCodTipoLicenza("LA");

					libAntMod.setNumeroGiorni(lPenCumMod.getNumGiorniLibAnticipataSPE()); // Nuova L.A.
					libAntMod.setDescrStatoPermesso("LS"); // Nuova L.A.

					libAntMod.setCodUfficioInserimento(lPenCumMod.getCodUfficioInserimento());
					libAntMod.setCodOperatoreInserimento(lPenCumMod.getCodOperatoreInserimento());
					libAntMod.setDataInserimento(lPenCumMod.getDataInserimento());

					lLibDAO = new LicenzaLibanticipataDAO(lConn);
					lLibDAO.setDAOFromModel(libAntMod);
					lLibDAO.insert();
					lLibDAO.stop();
				}

				if (lPenCumMod.getNumGiorniLibAnticipataINT() != null
						&& lPenCumMod.getNumGiorniLibAnticipataINT().compareTo(new BigDecimal(0)) != 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco LA INTEGRAZIONE e le aggancio al provv di cumulo");
					LicenzaLibAnticipataModel libAntMod = new LicenzaLibAnticipataModel();

					libAntMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					libAntMod.setEveIdEvento(lKeyEvento);

					libAntMod.setFlagConcesso("C");
					libAntMod.setFlagElaborato("N");
					libAntMod.setCodTipoLicenza("LA");

					libAntMod.setNumeroGiorni(lPenCumMod.getNumGiorniLibAnticipataINT()); // Nuova L.A.
					libAntMod.setDescrStatoPermesso("LI"); // Nuova L.A.

					libAntMod.setCodUfficioInserimento(lPenCumMod.getCodUfficioInserimento());
					libAntMod.setCodOperatoreInserimento(lPenCumMod.getCodOperatoreInserimento());
					libAntMod.setDataInserimento(lPenCumMod.getDataInserimento());

					lLibDAO = new LicenzaLibanticipataDAO(lConn);
					lLibDAO.setDAOFromModel(libAntMod);
					lLibDAO.insert();
					lLibDAO.stop();
				}

				// ====================================================================
				// DL 92/2014
				// ====================================================================
				if (lPenCumMod.getNumGiorniRiduzionePena() != null
						&& lPenCumMod.getNumGiorniRiduzionePena().intValue() != 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco Riduzione DL 92 e le aggancio al provv di cumulo");
					LicenzaLibAnticipataModel libAntMod = new LicenzaLibAnticipataModel();

					libAntMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
					libAntMod.setEveIdEvento(lKeyEvento);

					libAntMod.setCodTipoLicenza("RD");
					libAntMod.setFlagConcesso("C");
					libAntMod.setFlagElaborato("N");

					libAntMod.setNumeroGiorni(lPenCumMod.getNumGiorniRiduzionePena());

					libAntMod.setCodUfficioInserimento(lPenCumMod.getCodUfficioInserimento());
					libAntMod.setCodOperatoreInserimento(lPenCumMod.getCodOperatoreInserimento());
					libAntMod.setDataInserimento(lPenCumMod.getDataInserimento());

					lLibDAO = new LicenzaLibanticipataDAO(lConn);
					lLibDAO.setDAOFromModel(libAntMod);
					lLibDAO.insert();
					lLibDAO.stop();
				}

			} // Chiude if (lCumMod != null && lCumMod.getIdCumulo() != null)

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", daoEx);

			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEventoNotificaCumulo: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: ", ex);

			rollback(lConn);
			throw new F3BException("EventoController.ExInserisciEventoNotificaCumulo: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lCumSql);
			cleanup(lCumDao);
			cleanup(lLibDAO);
			cleanup(lLibSqlDAO);
			cleanup(lPenSqlDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * Ricerca Evento By FascicoloSiep TipEvento TipProv Per Evento Da Annullare Cancellare
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aOrdinamento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
			BigDecimal aFascKey, String[] aTipoEvento, String[] aTipoProv, String aOrdinamento)
			throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		// EventoNotificaModel lEve = null;
		EventoModel lEvento = new EventoModel();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvSiepDescPerEventoDaAnnullareCancellare(aFascKey,
					aTipoEvento, aTipoProv, aOrdinamento);
			lEvento = (EventoModel) lEveDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	// 26/03/2019 MEV70 - Esclusione degli Eventi di Comunicazione Cumulo (Cod-Motivo = 0670) dall'Elenco
	// Provvedimenti PM.
	/**
	 * Ricerca Evento By FascicoloSiep TipEvento TipProv Per Evento Da Annullare Cancellare
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aCodMotivo
	 * @param aOrdinamento
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
			BigDecimal aFascKey, String[] aTipoEvento, String[] aTipoProv, String[] aCodMotivo,
			String aOrdinamento) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		// EventoNotificaModel lEve = null;
		EventoModel lEvento = new EventoModel();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvSiepDescPerEventoDaAnnullareCancellare(aFascKey,
					aTipoEvento, aTipoProv, aCodMotivo, aOrdinamento);
			lEvento = (EventoModel) lEveDao.getModelByKey();

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEvento;
	}

	/*
	 * Commentato perchè non usato. Luigi 6-2-2006 public Vector
	 * ExRicercaEventoByFascicoloSiepTipEventoNOTTipProv(BigDecimal aFascKey, String[] aTipoEvento, String[]
	 * aTipoProv) throws F3BException { Connection lConn = null; EventoSqlDAO lEveDao = null;
	 * EventoNotificaModel lEve = null; Vector lEventi = null; try { lConn = getDBConnection(); lEveDao = new
	 * EventoSqlDAO(lConn); lEveDao.ricercaEventoByFascicoloTipEveTipProvSiepAsc(aFascKey, aTipoEvento,
	 * aTipoProv); lEventi = new Vector(lEveDao.getModels()); if (lEventi.size() == 0) throw new
	 * F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato"); } catch (DAOException daoEx) { //
	 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere : " + daoEx);
	 * } catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lEveDao); cleanup(lConn); } return lEventi; }
	 */

	// per la paginazione
	/**
	 * Ricerca Evento By FascicoloSiep TipEvento NOT TipProvPaged
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aPage
	 * @param aOrdinamento
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			// String aCodUfficioUtenteConnesso,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv, int aPage,
			String aOrdinamento) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDao = null;

		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvSiepAsc(aFascKey,
					// aCodUfficioUtenteConnesso,
					aUfficioUtenteConnesso, aTipoEvento, aTipoProv, aPage, aOrdinamento);

			lEveDao.start();

			while (lEveDao.next()) {
				EventoModel lEveMod = null;

				// Nel caso non sia stato passato l'Id del Fascicolo
				// significa che si vuole tutti i provvedimenti
				// NON VALIDATI
				if (aFascKey != null) {
					lEveMod = (EventoModel) lEveDao.getModel();
				} else {
					lEveMod = (EventoModel) lEveDao.getModelEventoFascicolo();
				}

				// **** get Blob ****
				lEveMod.setDocBlobOut(lEveDao.getBlob("DOC_BLOB"));

				lEventi.add(lEveMod);
			}

			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged: " + daoEx);
		} finally {
			cleanup(lEveDao);

			cleanup(lConn);
		}

		return lEventi;
	}

	/**
	 * Get Count Evento By FascicoloSiep TipEvento NOT TipProvPaged
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			String aCodUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);

		Connection lConn = null;

		EventoSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new EventoSqlDAO(lConn);
			lSqlDao.getCountEventoByFascicoloSiepTipEventoNOTTipProv(aFascKey, aCodUfficioUtenteConnesso,
					aTipoEvento, aTipoProv);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoSimeoneController.ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged: "
							+ daoEx);
		} finally {
			cleanup(lSqlDao);

			cleanup(lConn);
		}

		return lCount;
	}

	// 26/03/2019 MEV70 - Esclusione degli Eventi di Comunicazione Cumulo (Cod-Motivo = 0670) dall'Elenco
	// Provvedimenti PM.
	/**
	 * Ricerca Evento By FascicoloSiep TipEvento NOT TipProvPaged
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @param aPage
	 * @param aOrdinamento
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			// String aCodUfficioUtenteConnesso,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv,
			String[] aCodMotivo, int aPage, String aOrdinamento) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveDao = null;

		Vector lEventi = new Vector();

		try {
			lConn = getDBConnection();
			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvSiepAsc(aFascKey, aUfficioUtenteConnesso,
					aTipoEvento, aTipoProv, aCodMotivo, aPage, aOrdinamento);

			lEveDao.start();

			while (lEveDao.next()) {
				EventoModel lEveMod = null;

				// Nel caso non sia stato passato l'Id del Fascicolo
				// significa che si vuole tutti i provvedimenti
				// NON VALIDATI
				if (aFascKey != null) {
					lEveMod = (EventoModel) lEveDao.getModel();
				} else {
					lEveMod = (EventoModel) lEveDao.getModelEventoFascicolo();
				}

				// **** get Blob ****
				lEveMod.setDocBlobOut(lEveDao.getBlob("DOC_BLOB"));

				lEventi.add(lEveMod);
			}

			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;
	}

	// 26/03/2019 MEV70 - Esclusione degli Eventi di Comunicazione Cumulo (Cod-Motivo = 0670) dall'Elenco
	// Provvedimenti PM.
	/**
	 * Get Count Evento By FascicoloSiep TipEvento NOT TipProvPaged
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @return
	 * @throws F3BException
	 */
	// Ticket#202101270113 - si adeguano le condizione della count alle condizioni della select
	//                       impostando il filtro sull'ufficio + accorpati
	public BigDecimal ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			// String aCodUfficioUtenteConnesso
			UfficioModel aUfficioUtenteConnesso
			, String[] aTipoEvento, String[] aTipoProv, String[] aCodMotivo)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);

		Connection lConn = null;

		EventoSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new EventoSqlDAO(lConn);
			lSqlDao.getCountEventoByFascicoloSiepTipEventoNOTTipProv (aFascKey, aUfficioUtenteConnesso, // aCodUfficioUtenteConnesso,
					aTipoEvento, aTipoProv, aCodMotivo);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"EventoSimeoneController.ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged: "
							+ daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Ricerca Per le ordianza e decreti della sorveglianza lato SIEP
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaOrdinanzeDecretiSiep(BigDecimal aFascKey, String[] aTipoEvento, String[] aTipoProv)
			throws F3BException {

		Connection lConn = null;

		EventoSimeoneSqlDAO lEveDao = null;
		LicenzaLibanticipataSqlDAO lLibSql = null;
		// PeriodoLibanticipataSqlDAO lPerDao = null; // 04/02/2008
		TenoreSqlDAO lTenSql = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSql = null;
		DepositoDecretoSqlDAO lDepDecSql = null;
		MisuraAlternativaSqlDAO lMisSql = null;

		MisuraAlternativaAggregatoModel lMisAggregato = null;
		// LicenzaLibAnticipataModel lLibAntic = null;
		DepositoOrdinanzaPcModel lDepOrdPCMod = null;
		DepositoDecretoModel lDepDecMod = null;

		Vector lAggregato = new Vector();
		Vector lEventi = null;
		Vector lTenori = null;
		// Vector lLicenzePeriodi = new Vector(); // 04/02/2008
		try {
			lConn = getDBConnection();
			lEveDao = new EventoSimeoneSqlDAO(lConn);
			lLibSql = new LicenzaLibanticipataSqlDAO(lConn);
			lDepOrdSql = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDecSql = new DepositoDecretoSqlDAO(lConn);
			lTenSql = new TenoreSqlDAO(lConn);
			lMisSql = new MisuraAlternativaSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloTipEveTipProvAsc(aFascKey, aTipoEvento, aTipoProv);
			lEventi = new Vector(lEveDao.getModels());

			if (lEventi != null) {

				Iterator iter = lEventi.iterator();
				while (iter.hasNext()) {
					lMisAggregato = new MisuraAlternativaAggregatoModel();
					EventoModel lEveMod = (EventoModel) iter.next();

					// cerco la misura alternativa legata all'evento
					lMisSql.ricercaMisuraAlternativaByIdEvento(lEveMod.getIdEvento());
					MisuraAlternativaModel lModMis = (MisuraAlternativaModel) lMisSql.getModelByKey();
					if (lModMis != null && lModMis.getIdMisuraAlternativa() != null)
						lMisAggregato.setMisuraAlternativa(lModMis);

					// cerco la liberazione anticipata
					// 17/06/2008 Ordinamento Occorrenze di Licenza Liberazione Anticipata per FLAG_CONCESSO.
					// lLibSql.ricercaLicenzaLibanticipataByEve(lEveMod.getIdEvento());
					lLibSql.ricercaLicenzaLibanticipataByEveOrderByFlagConc(lEveMod.getIdEvento());
					// 04/02/2008 Gestione di più periodi di Licenza Liberazione Anticipata
					// lLibAntic = (LicenzaLibAnticipataModel) lLibSql.getModelByKey();
					Vector lLicenze = new Vector();
					lLicenze = new Vector(lLibSql.getModels());
					Iterator lItx = null;
					BigDecimal aTotGiorniLicenza = new BigDecimal(0);
					if (lLicenze.size() > 0) {
						LicenzaLibAnticipataModel lLibAntic = null;
						lLibAntic = (LicenzaLibAnticipataModel) lLicenze.elementAt(0);
						lItx = lLicenze.iterator();
						while (lItx.hasNext()) {
							LicenzaLibAnticipataModel aLicenza = new LicenzaLibAnticipataModel();
							aLicenza = (LicenzaLibAnticipataModel) lItx.next();
							// 15/05/2008 Soluzione segnalazione a8-rr-107 Conteggio N.ro Giorni Liberazione
							// Anticipata solo se FlagConcesso = C.
							// if (aLicenza.getNumeroGiorni() != null )
							if (aLicenza.getNumeroGiorni() != null
									&& aLicenza.getFlagConcesso().trim().compareTo("C") == 0)
								aTotGiorniLicenza = aTotGiorniLicenza.add(aLicenza.getNumeroGiorni());
						}
						lLibAntic.setNumeroGiorni(aTotGiorniLicenza);

						lEveMod.setLicenzaLibAnticipata(lLibAntic);
					}

					// setto l'evento
					lMisAggregato.getEventoNotifica().setEvento(lEveMod);

					// se il tipo provvedimento è 02 cerco il decreto altrimenti è 03 e cerco il
					// l'ordinanza e il relativo tenore!!
					if (lEveMod != null && lEveMod.getCodTipoProvvedimento() != null) {
						if (lEveMod.getCodTipoProvvedimento().equals("02")) {
							lDepDecSql.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(
									lEveMod.getIdEvento());
							lDepDecSql.start();
							if (lDepDecSql.next()) {
								lDepDecMod = (DepositoDecretoModel) lDepDecSql.getModelNoDescTipoDecreto();
							}

							lDepDecSql.stop();
							if (lDepDecMod != null) {
								lMisAggregato.setDepositoDecreto(lDepDecMod);
								// tenore
								lTenSql.ricercaTenoriByDecretoOrderByPesoNoGenProc(
										lDepDecMod.getIdDepositoDecreto());
								lTenori = new Vector(lTenSql.getModels());
								// il prino elemeto viene rimosso perchè non deve essere visualizzato nella
								// lista
								if (lTenori.size() > 1)
									lTenori.remove(0);
								lMisAggregato.setTenori((TenoreModel[]) lTenori.toArray(new TenoreModel[0]));

							}
						} else if (lEveMod.getCodTipoProvvedimento().equals("03")) {
							lDepOrdSql.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveMod.getIdEvento());
							lDepOrdPCMod = (DepositoOrdinanzaPcModel) lDepOrdSql.getModelByKey();
							if (lDepOrdPCMod != null) {
								lMisAggregato.setDepositoOrdinanzaPc(lDepOrdPCMod);
								// tenore
								lTenSql.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(
										lDepOrdPCMod.getIdDepositoOrdinanzaPc());
								lTenori = new Vector(lTenSql.getModels());
								// il prino elemeto viene rimosso perchè non deve essere visualizzato nella
								// lista
								if (lTenori.size() > 1)
									lTenori.remove(0);
								lMisAggregato.setTenori((TenoreModel[]) lTenori.toArray(new TenoreModel[0]));
							}
						}
					}

					// riempio il vettore
					lAggregato.add(lMisAggregato);
				}

			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaOrdinanzeDecretiSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lLibSql);
			cleanup(lDepOrdSql);
			cleanup(lDepDecSql);
			cleanup(lTenSql);
			cleanup(lMisSql);

			cleanup(lConn);
		}
		return lAggregato;
	}

	/*
	 * Commentato perchè non usato. Luigi 6-2-06 public Vector
	 * ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvNONAnnullati(BigDecimal aFascKey, String[]
	 * aTipoEvento, String[] aTipoProv) throws F3BException { Connection lConn = null; EventoSqlDAO lEveDao =
	 * null; EventoNotificaModel lEve = null; Vector lEventi = null; try { lConn = getDBConnection(); lEveDao
	 * = new EventoSqlDAO(lConn);
	 * lEveDao.ricercaEventoByFascicoloSiepTipEventoNOTTipProvNONAnnullati(aFascKey, aTipoEvento, aTipoProv);
	 * lEventi = new Vector(lEveDao.getModels()); if (lEventi.size() == 0) throw new
	 * F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato"); } catch (DAOException daoEx) { //
	 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
	 * siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere : " + daoEx);
	 * } catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("EventoController.ExRicercaEventoNotificaByFascicoloSiep: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lEveDao); cleanup(lConn); } return lEventi; }
	 */

	/**
	 * metodo per fascicoli migrati da RES
	 *
	 * @param aFascKey
	 * @param aTipoEvento
	 * @param aTipoProv
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoNotificaByFascicoloSiepTipEventoNOTTipProvNONAnnullati(BigDecimal aFascKey,
			String[] aTipoEvento, String[] aTipoProv) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		CampoNotaSqlDAO lCampoDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;
		CSSASqlDAO lCssaDao = null;
		UfficioSqlDAO lUffDao = null;
		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		RiepilogoProvvedimentoSqlDAO lRiepDao = null;

		EventoModel lEve = null;
		EventoNotificaModel lEveNot = null;
		RiepilogoProvvedimentoModel lRiep = new RiepilogoProvvedimentoModel();
		Vector lEventi = null;
		Vector lEventiNotifica = null;

		try {
			lConn = getDBConnection();

			lUffDao = new UfficioSqlDAO(lConn);
			lCssaDao = new CSSASqlDAO(lConn);
			lAutoritaSqlDao = new AutoritaEsternaSqlDAO(lConn);
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lNotEveDao = new NotificaEventoSqlDAO(lConn);

			lEveDao = new EventoSqlDAO(lConn);

			lEveDao.ricercaEventoByFascicoloSiepTipEventoNOTTipProvNONAnnullati(aFascKey, aTipoEvento,
					aTipoProv);

			lEventi = new Vector(lEveDao.getModels());

			if (lEventi.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

			lEventiNotifica = new Vector();
			lCampoDao = new CampoNotaSqlDAO(lConn);
			lRiepDao = new RiepilogoProvvedimentoSqlDAO(lConn);
			Iterator it = lEventi.iterator();
			while (it.hasNext()) {
				lEve = (EventoModel) it.next();
				lEveNot = new EventoNotificaModel(lEve);
				// Campi note
				lCampoDao.ricercaCampoNotaByKeyEvento(lEve.getIdEvento());
				Vector lCampiNote = new Vector(lCampoDao.getModels());
				lEveNot.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
				if (lEve.getCodTipoEvento().equals("01") && lEve.getCodTipoProvvedimento().equals("04")
						&& lEve.getCodMotivo().equals("7777")) {
					lRiepDao.ricercaRiepilogoProvvedimentoByKeyEvento(lEve.getIdEvento());
					lRiep = ((RiepilogoProvvedimentoModel) lRiepDao.getModelByKey());

				}
				lEveNot.setRiepilogoProvvedimento(lRiep);

				lNotEveDao.ricercaNotificaByEvento(lEve.getIdEvento());
				Vector lNotifiche = new Vector(lNotEveDao.getModels());
				lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

				// Verfica ed inserisce le Autorita Esterne e gli uffici
				int count = 0;
				while (count < lEveNot.getNotifiche().length) {
					// Autorita Esterne
					if (lEveNot.getNotifiche()[count].getAutEstIdAutoritaEsterna() != null) {
						lAutoritaSqlDao.ricercaAutoritaEsternaByKey(
								lEveNot.getNotifiche()[count].getAutEstIdAutoritaEsterna());
						AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao
								.getModelByKey();
						// Inserisce l'occorenza nel model delle notifiche.
						lEveNot.getNotifiche()[count].setAutoritaEsterna(lAutorita);
						lAutoritaSqlDao.stop();
					}
					// modifica relativa al tipo istituto
					if (lEveNot.getNotifiche()[count].getIstDetIdIstitutoDetenzione() != null
							&& !lEveNot.getNotifiche()[count].getIstDetIdIstitutoDetenzione().equals("")) {
						lIstDao.ricercaIstitutoDetenzioneByKey(
								lEveNot.getNotifiche()[count].getIstDetIdIstitutoDetenzione());
						IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						// Inserisce l'occorenza nel model delle notifiche.
						lEveNot.getNotifiche()[count].setIstitutoDetenzione(lIstituto);
						lIstDao.stop();
					}
					// fine modifica relativa al tipo istituto

					// Preleva gli uffici
					if (lEveNot.getNotifiche()[count].getUffCodUfficio() != null) {
						lUffDao.selUfficioByCod(lEveNot.getNotifiche()[count].getUffCodUfficio());
						UfficioModel lUffMod = (UfficioModel) lUffDao.getModelByKey();
						// Inserisce l'occorrenza nel model delle notifiche.
						lEveNot.getNotifiche()[count].setUfficio(lUffMod);
						lUffDao.stop();
					}

					// Preleva il Cssa
					if (lEveNot.getNotifiche()[count].getCssIdCssa() != null) {
						lCssaDao.selModelCssabyKey(lEveNot.getNotifiche()[count].getCssIdCssa());
						CSSAModel lCssaMod = (CSSAModel) lCssaDao.getModelByKey();
						// Inserisce l'occorrenza nel model delle notifiche.
						lEveNot.getNotifiche()[count].setCSSA(lCssaMod);
						lCssaDao.stop();
					}

					count++;

				}
				lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

				lEventiNotifica.add(lEveNot);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoSimeoneController.ExRicercaEventoNotificaByFascicoloSiepTipEventoNOTTipProvNONAnnullati: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lCampoDao);
			cleanup(lRiepDao);
			cleanup(lUffDao);
			cleanup(lCssaDao);
			cleanup(lAutoritaSqlDao);
			cleanup(lIstDao);
			cleanup(lNotEveDao);

			cleanup(lConn);
		}
		return lEventiNotifica;
	}

	/**
	 * ExRicercaEventoByEveIdEvento
	 *
	 * @param aEventoKey
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByEveIdEvento(BigDecimal aEventoKey) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBConnection();

			// AMBROSINO 30/06/2011 --> nella ricerca 'lEveDao.ricercaEventoByEveIdEvento(aEventoKey)' viene
			// aggiunto
			// un ORDER BY data-emissione DESC;
			// è chiamata anche da ExAggiornaEventoInserisciCampoNota (MisuraAlternativaIndultinoController)
			// e da ExRicercaEventoNotificaByEveIdEvento(EventoControllr)

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaEventoByEveIdEvento(aEventoKey);
			lEveMod = (EventoModel) lEveDao.getModelByKey();

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Ricerca Evento By EveIdEvento TipoProv CodMotivo
	 *
	 * @param aEventoKey
	 * @param aTipoEvento
	 * @param aTipoProvv
	 * @param aMotivo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByEveIdEventoTipoProvCodMotivo(BigDecimal aEventoKey,
			String aTipoEvento, String aTipoProvv, String aMotivo) throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lEveDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSimeoneSqlDAO(lConn);
			lEveDao.ricercaEventoByEveIdEventoTipEveTipProvCodMotivo(aEventoKey, aTipoEvento, aTipoProvv,
					aMotivo);
			lEveDao.start();

			if (lEveDao.next())
				lEveMod = (EventoModel) lEveDao.getModelEvento();
			lEveDao.stop();

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByEveIdEventoTipoProvCodMotivo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Ricerca Evento By FascicoloSiep Desc
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByFascicoloSiepDesc(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lEveDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSimeoneSqlDAO(lConn);
			lEveDao.ricercaEventoByFascicoloSiepDesc(aKey);
			lEveDao.start();

			if (lEveDao.next())
				lEveMod = (EventoModel) lEveDao.getModelEvento();
			lEveDao.stop();

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepDesc: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * ricerca usata nel ActionSiap nel metodo isEventoValidato
	 *
	 * @param aKey
	 * @param aCodUfficio
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoByFascicoloSiepDescUfficioConnesso(BigDecimal aKey, String aCodUfficio)
			throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lEveDao = null;
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSimeoneSqlDAO(lConn);
			lEveDao.ricercaEventoByFascicoloSiepDescUfficioConnesso(aKey, aCodUfficio);
			lEveDao.start();

			if (lEveDao.next())
				lEveMod = (EventoModel) lEveDao.getModelEvento();
			lEveDao.stop();
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoByFascicoloSiepDescUfficioConnesso: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Ricerca Ultimo Evento By IdFascicolo
	 *
	 * @param aKeyFasc
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaUltimoEventoByIdFascicolo(BigDecimal aKeyFasc) throws F3BException {

		Connection lConn = null;
		EventoSqlDAO lEveDao = null;
		Vector lEve = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSqlDAO(lConn);
			lEveDao.ricercaUltimoEventoByIdFascicolo(aKeyFasc);
			lEve = new Vector(lEveDao.getModels());

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoNotificaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEve;
	}

	/**
	 * Ricerca Evento Stato Esecuzione By Fascicolo Siep Paged
	 *
	 * @param aIdFascicolo
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoStatoEsecuzioneByFascicoloSiepPaged(BigDecimal aIdFascicolo, int aPage)
			throws F3BException {

		Connection lConn = null;
		EventoSimeoneSqlDAO lDao = null;
		TenoreSqlDAO lTenSql = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSql = null;
		DepositoDecretoSqlDAO lDepDecSql = null;
		MisuraAlternativaSqlDAO lMisSql = null;

		MisuraAlternativaAggregatoModel lMisAggregato = null;
		// LicenzaLibAnticipataModel lLibAntic = null;
		DepositoOrdinanzaPcModel lDepOrdPCMod = null;
		DepositoDecretoModel lDepDecMod = null;
		MisuraAlternativaModel lMisMod;

		Vector lAggregato = new Vector();
		Vector lTenori = null;

		try {
			lConn = getDBConnection();
			lDao = new EventoSimeoneSqlDAO(lConn);
			lDepOrdSql = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDecSql = new DepositoDecretoSqlDAO(lConn);
			lTenSql = new TenoreSqlDAO(lConn);
			lMisSql = new MisuraAlternativaSqlDAO(lConn);

			lDao.ricercaOrdineEsecuzioneSoloEventiVisualizzazioneByIdFascicoloPaged(aIdFascicolo, aPage);

			lDao.start();
			while (lDao.next()) {
				lMisAggregato = new MisuraAlternativaAggregatoModel();
				EventoModel lEveMod = (EventoModel) lDao.getModelEvento();
				// **** get Blob ****
				lEveMod.setDocBlobOut(lDao.getBlob());

				lMisAggregato.getEventoNotifica().setEvento(lEveMod);

				// se il tipo provvedimento è 02 cerco il decreto altrimenti è 03 e cerco il
				// l'ordinanza e il relativo tenore!!
				if (lEveMod != null && lEveMod.getCodTipoProvvedimento() != null) {
					if (lEveMod.getCodTipoProvvedimento().equals("02")) {
						lDepDecSql.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(
								lEveMod.getIdEvento());
						lDepDecSql.start();
						if (lDepDecSql.next()) {
							lDepDecMod = (DepositoDecretoModel) lDepDecSql.getModelNoDescTipoDecreto();
						}
						lDepDecSql.stop();
						if (lDepDecMod != null) {
							lMisAggregato.setDepositoDecreto(lDepDecMod);
							// tenore
							lTenSql.ricercaTenoriByDecretoOrderByPesoNoGenProc(
									lDepDecMod.getIdDepositoDecreto());
							lTenori = new Vector(lTenSql.getModels());
							lMisAggregato.setTenori((TenoreModel[]) lTenori.toArray(new TenoreModel[0]));

						}
					} else if (lEveMod.getCodTipoProvvedimento().equals("03")) {
						lDepOrdSql.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveMod.getIdEvento());
						lDepOrdPCMod = (DepositoOrdinanzaPcModel) lDepOrdSql.getModelByKey();
						if (lDepOrdPCMod != null) {
							lMisAggregato.setDepositoOrdinanzaPc(lDepOrdPCMod);
							// tenore
							lTenSql.ricercaTenoriByOrdinanzaOrderByPesoNoGenProc(
									lDepOrdPCMod.getIdDepositoOrdinanzaPc());
							lTenori = new Vector(lTenSql.getModels());
							lMisAggregato.setTenori((TenoreModel[]) lTenori.toArray(new TenoreModel[0]));
						}
					}
				}
				// ricerca misura alternativa
				lMisSql.ricercaMisuraAlternativaByIdEvento(lEveMod.getIdEvento());
				lMisMod = (MisuraAlternativaModel) lMisSql.getModelByKey();
				lMisAggregato.setMisuraAlternativa(lMisMod);

				lAggregato.add(lMisAggregato);
			}

			lDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"EventoController.ExRicercaEventoStatoEsecuzioneByFascicoloSiep: " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("EventoController.ExRicercaEventoStatoEsecuzioneByFascicoloSiep: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + ex);
			throw new F3BException("EventoController.ExRicercaEventoStatoEsecuzioneByFascicoloSiep: " + ex);
		} finally {
			cleanup(lDao);
			cleanup(lDepOrdSql);
			cleanup(lDepDecSql);
			cleanup(lTenSql);
			cleanup(lMisSql);

			cleanup(lConn);
		}

		return lAggregato;
	}

	/**
	 * Get Count Eventi Paged
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountEventiPaged(BigDecimal aKeyFascicolo) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		EventoSimeoneSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new EventoSimeoneSqlDAO(lConn);
			lSqlDao.getCountSoloEventiVisualizzazione(aKeyFascicolo);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoSimeoneController.ExGetCountEventiPaged: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Get Count Eventi Non Validati Paged
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountEventiNonValidatiPaged(EventoModel aModel) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		EventoSimeoneSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new EventoSimeoneSqlDAO(lConn);
			lSqlDao.getCountEventiNonValidati(aModel);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("EventoSimeoneController.ExGetCountEventiNonValidatiPaged: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Ricerca Provvedimenti On View Paged
	 *
	 * @param aModel
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaProvvedimentiOnViewPaged(EventoModel aModel, int aPage) throws F3BException {

		Connection lConn = null;
		// Vector lFascicoli = new Vector();
		EventoSimeoneSqlDAO lEveDao = null;
		Vector lVectAggr = new Vector();
		FascicoloSiepModel lFascicolo = null;
		EventoFascicoloModel lEveFascicoloModel = new EventoFascicoloModel();

		EventoModel lEveMod = null;
		BigDecimal cont = new BigDecimal(0);
		List eventi = new ArrayList();
		EventoFascicoloModel lEveFascAppoggio = null;
		// int conta = 0;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoSimeoneSqlDAO(lConn);
			lEveDao.ricercaProvvedimentiNonValidatiPaged(aModel, aPage);
			lEveDao.start();

			while (lEveDao.next()) {
				lFascicolo = (FascicoloSiepModel) lEveDao.getModelViewFascicolo();

				lEveMod = (EventoModel) lEveDao.getModelEventoProvvedimentiNonValidati();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(
						"---FASCICOLO--->" + lFascicolo.getIdFascicoloSiep() + "    EVENTO--->" + lEveMod);

				// eventi.add(lEveMod);
				if (cont.compareTo(new BigDecimal(0)) == 0) // prima Volta
				{

					eventi.add(lEveMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("---PRIMA VOLTA agginto-evento------------>" + lEveMod.getCodMotivo());
					lEveFascicoloModel.setFascicoloSiep(lFascicolo);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("---fascicolo------------>" + lEveFascicoloModel.getFascicoloSiep());

					lEveFascicoloModel.setEventi(eventi);
					lEveFascAppoggio = lEveFascicoloModel;
					cont = lFascicolo.getIdFascicoloSiep();
				} else {
					if (lEveFascAppoggio.getFascicoloSiep().getIdFascicoloSiep()
							.compareTo(lFascicolo.getIdFascicoloSiep()) == 0) { // stesso fascicolo
																				// [FT] - 03/08/2016 - MAC_LOG
																				// - Utilizzo la variabile di
																				// istanza siesLogger al posto
																				// di mLog
						siesLogger.debug("---STESSO FASCICOLO----------->");
						eventi.add(lEveMod);

						for (int i = 0; i < eventi.size(); i++) {
							EventoModel lEveVerifica = (EventoModel) eventi.get(i);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug(
									"---STESSO FASCICOLO----Eventi------->" + lEveVerifica.getCodMotivo());

						}

						cont = lFascicolo.getIdFascicoloSiep();

					} else // altro fascicolo
					{
						lVectAggr.add(lEveFascAppoggio);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("---ALTRO FASCICOLO------------>");
						lEveFascicoloModel = new EventoFascicoloModel();
						lEveFascAppoggio = new EventoFascicoloModel();

						eventi = new Vector();

						lEveFascicoloModel.setFascicoloSiep(lFascicolo);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(
								"---fascicolo-altro----------->" + lEveFascicoloModel.getFascicoloSiep());

						eventi.add(lEveMod);
						for (int i = 0; i < eventi.size(); i++) {
							EventoModel lEveVerifica = (EventoModel) eventi.get(i);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di mLog
							siesLogger.debug(
									"---ALTRO FASCICOLO----Eventi------->" + lEveVerifica.getCodMotivo());

						}

						lEveFascicoloModel.setEventi(eventi);

						lEveFascAppoggio = lEveFascicoloModel;
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("---eventi.SIZE() ALTRO FASC ****------------>"
								+ lEveFascAppoggio.getEventi().size());

						// lVectAggr.add(lEveFascAppoggio);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("---aggregato----------->" + lVectAggr.size());

						cont = lFascicolo.getIdFascicoloSiep();

					}
				}
				// conta++;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("---FASCICOLO- confronto-->"
						+ lEveFascAppoggio.getFascicoloSiep().getIdFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger
						.debug("---eventi.SIZE()  FASC ------------>" + lEveFascAppoggio.getEventi().size());

			}
			if (lEveFascAppoggio != null)
				lVectAggr.add(lEveFascAppoggio);

			if (lVectAggr.size() == 0 && lEveFascAppoggio != null) {
				lVectAggr.add(lEveFascAppoggio);

			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("fine metodo----------lVectAggr---------------°°°°°°°°" + lVectAggr.size());
			/*
			 * for (int i = 0; i < lVectAggr.size(); i++) { EventoFascicoloModel lEveVerifica =
			 * (EventoFascicoloModel) lVectAggr.get(i); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile
			 * di istanza siesLogger al posto di mLog siesLogger.error("---AGGREGATO FASCICOLO------>" +
			 * lEveVerifica.getFascicoloSiep()+"NUM EVENTI"+lEveVerifica.getEventi().size()); Vector eventiver
			 * = new Vector( lEveVerifica.getEventi()); for (int Y = 0; Y < lVectAggr.size(); Y++) {
			 * EventoModel lEveVer = (EventoModel) eventiver.get(Y); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo
			 * la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.error("---AGGREGATO EVENTI------>" + lEveVer.getCodMotivo()); } }
			 */
			// lEveDao.stop();
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiepController.ExRicercaFascicoloSiep: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lVectAggr;
	}

	/**
	 * Ricerca Provvedimenti Per Omesse Notifiche On View Paged
	 *
	 * @param aModel
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public List ExRicercaProvvedimentiPerOmesseNotificheOnViewPaged(EventoModel aModel,
			String aCodUfficioUtenteConnesso, int aPage) throws F3BException {

		Connection lConn = null;

		EventoSimeoneSqlDAO lEveFasDao = null;
		EventoSimeoneSqlDAO lEveDao = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		List lFascicoliEventi = new ArrayList();

		try {
			lConn = getDBConnection();

			lEveFasDao = new EventoSimeoneSqlDAO(lConn);
			lEveFasDao.ricercaProvvedimentiOmesseNotifiche(aModel, aCodUfficioUtenteConnesso);
			lEveFasDao.startPage(aPage);

			lEveDao = new EventoSimeoneSqlDAO(lConn);

			while (lEveFasDao.next()) {
				FascicoloSiepModel lFasMod = (FascicoloSiepModel) lEveFasDao.getModelEventoSimeoneFascicolo();

				if (lFasMod != null && lFasMod.getIdFascicoloSiep() != null) {
					EventoFascicoloModel lEveFasNuovoMod = new EventoFascicoloModel();

					lEveFasNuovoMod.setFascicoloSiep(lFasMod);

					lEveDao.ricercaEventoSimeoneByIdFascicolo(lFasMod.getIdFascicoloSiep());

					List lListEventiSimeone = new ArrayList();
					lNotEveDao = new NotificaEventoSqlDAO(lConn);

					lEveDao.start();
					while (lEveDao.next()) {
						EventoNotificaModel lEveNotSimeone = (EventoNotificaModel) lEveDao
								.getModelEventoSimeoneON();

						if (lEveNotSimeone != null && lEveNotSimeone.getEvento() != null) {
							lNotEveDao.ricercaNotificaByEvento(lEveNotSimeone.getEvento().getIdEvento());
							List lNotifiche = (List) lNotEveDao.getModels();
							lEveNotSimeone
									.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
						}

						lListEventiSimeone.add(lEveNotSimeone);
					}
					lEveDao.stop();

					lEveFasNuovoMod.setEventi(lListEventiSimeone);

					lFascicoliEventi.add(lEveFasNuovoMod);
				}
			}
			lEveFasDao.stop();

			if (lFascicoliEventi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new SIEPException(F3BException.USER_MESSAGE,
					"EventoSimeoneController.ExRicercaProvvedimentiPerOmesseNotificheOnViewPaged: " + daoEx);
		} finally {
			cleanup(lNotEveDao);
			cleanup(lEveDao);
			cleanup(lEveFasDao);

			cleanup(lConn);
		}

		return lFascicoliEventi;
	}

	public BigDecimal ExGetCountProvvedimentiPerOmesseNotifiche(EventoModel aEvento,
			String aCodUfficioUtenteConnesso) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);

		Connection lConn = null;

		EventoSimeoneSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSimeoneSqlDAO(lConn);
			lEveSqlDao.getCountProvvedimentiOmesseNotifiche(aEvento, aCodUfficioUtenteConnesso);

			lEveSqlDao.start();
			lEveSqlDao.next();
			lCount = lEveSqlDao.getBigDecimal("HowManyRecords");
			lEveSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"EventoSimeoneController.ExGetCountProvvedimentiPerOmesseNotifiche: " + daoEx);
		} finally {
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}

		return lCount;
	}

}