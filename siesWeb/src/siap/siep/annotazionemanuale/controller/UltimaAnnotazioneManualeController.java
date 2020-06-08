package siap.siep.annotazionemanuale.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.model.EventoModel;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.dao.UltimaAnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.UltimaAnnotazioneModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UltimaAnnotazioneManualeController extends SiapController implements IUltimaAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector ExRicercaUltimeAnnotazioniManuali(BigDecimal aKeyFascicolo, EventoModel aEventoCorrente)
			throws F3BException {

		Vector lUltimeAnnotazioni = null;

		UltimaAnnotazioneManualeSqlDAO lUltSqlDAO = null;
		AnnotazioneManualeSqlDAO lAnnManSqlDAO = null;

		UltimaAnnotazioneModel lUltModel = null;
		Vector lVectAnnMod = null;

		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lUltSqlDAO = new UltimaAnnotazioneManualeSqlDAO(lConn);
			lAnnManSqlDAO = new AnnotazioneManualeSqlDAO(lConn);

			if (aEventoCorrente != null && aEventoCorrente.getAnnIdAnnotazioneManuale() != null) {
				lUltModel = new UltimaAnnotazioneModel(aEventoCorrente);

				lAnnManSqlDAO.ricercaAnnotazioneManualeByKey(aEventoCorrente.getAnnIdAnnotazioneManuale());
				lVectAnnMod = new Vector(lAnnManSqlDAO.getModels());
				lUltModel.setVectAnnotazioneManuale(lVectAnnMod);
				lAnnManSqlDAO.stop();
			}

			lUltimeAnnotazioni = new Vector();

			if (lUltModel != null) {
				lUltimeAnnotazioni.addElement(lUltModel);
			}

			lUltModel = new UltimaAnnotazioneModel();

			lUltSqlDAO.ricercaEventoOrdinanza(aKeyFascicolo);
			lUltModel = (UltimaAnnotazioneModel) lUltSqlDAO.getModelByKey();
			lUltSqlDAO.stop();

			if (lUltModel != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ricercaAnnotazioneManualeByIdEvento");

				lAnnManSqlDAO.ricercaAnnotazioneManualeByIdEvento(lUltModel.getIdEvento());
				lVectAnnMod = new Vector(lAnnManSqlDAO.getModels());
				lUltModel.setVectAnnotazioneManuale(lVectAnnMod);
				lAnnManSqlDAO.stop();

				lUltimeAnnotazioni.addElement(lUltModel);
			}

			lUltModel = new UltimaAnnotazioneModel();

			lUltSqlDAO.ricercaEventoProvvedimento(aKeyFascicolo);
			lUltModel = (UltimaAnnotazioneModel) lUltSqlDAO.getModelByKey();

			if (lUltModel != null) {
				lAnnManSqlDAO.ricercaAnnotazioneManualeByIdEvento(lUltModel.getIdEvento());
				lVectAnnMod = new Vector(lAnnManSqlDAO.getModels());
				lUltModel.setVectAnnotazioneManuale(lVectAnnMod);
				lAnnManSqlDAO.stop();

				lUltimeAnnotazioni.addElement(lUltModel);
			}
		} catch (DAOException ex) {
			throw new F3BException(
					"UltimaAnnotazioneManualeController.ExRicercaUltimeAnnotazioniManuali: " + ex);
		} finally {
			cleanup(lUltSqlDAO);
			cleanup(lAnnManSqlDAO);

			cleanup(lConn);
		}

		return lUltimeAnnotazioni;
	}

}