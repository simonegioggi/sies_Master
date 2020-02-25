package siap.siep.rinnovo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteMagistratoSqlDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.stampa.controller.SIAPStampaController;
import siap.sico.stampa.controller.StampaUtils;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rinnovo.dao.RinnovoDAO;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: RinnovoController
 * </p>
 * <p>
 * Description: Classe Controller per Rinnovo
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
public class RinnovoStampaController extends SIAPStampaController implements IRinnovoStampa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ByteArrayOutputStream ExStampaDocumento(RinnovoModel aRinnovo, UtenteModel aUtenteModel)
			throws F3BException {
		Connection lConn = null;
		RinnovoDAO lRinDao = null;

		ByteArrayOutputStream lByteArrayOut = null;
		try {
			TreeModel lTree = prelevaDatiRinnovo(aRinnovo, aUtenteModel);

			ReportGenerator lReport = new ReportGenerator();
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aRinnovo.getTemIdTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aRinnovo.setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lRinDao = new RinnovoDAO(lConn);
			lRinDao.setDAOFromModelForUpdateBlob(aRinnovo);
			lRinDao.setTemIdTemplate(aRinnovo.getTemIdTemplate());
			lRinDao.setCondizioneUpdate(aRinnovo.getIdRinnovo());
			lRinDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("EventoController.ExRicercaTemplateByCodMotivo: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lRinDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/*-------------------------------------Preleva i campi per la stampa-------------------------------------------*/

	private TreeModel prelevaDatiRinnovo(RinnovoModel aRinModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();
		NotificaSqlDAO lNotDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		EventoSqlDAO lEveSqlDao = null;
		VerbaleSqlDAO lVerDao = null;
		AutoritaEsternaSqlDAO lAutDao = null;
		MagistratoSqlDAO lMadDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;

		Connection lConn = null;

		try {
			lConn = getDBConnection();

			TreeModel lTreeRineMod = new TreeModel(aRinModel);
			EventoNotificaModel lNotEveMod = new EventoNotificaModel();

			// Notifiche
			lNotDao = new NotificaSqlDAO(lConn);
			lNotDao.ricercaNotificaByKey(aRinModel.getNotIdNotifica());
			NotificaModel lNotifica = new NotificaModel();
			lNotifica = (NotificaModel) lNotDao.getModelByKey();
			TreeModel lTreeNot = new TreeModel(lNotifica);

			// Autorità esterna
			lAutDao = new AutoritaEsternaSqlDAO(lConn);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			if (lNotifica != null && lNotifica.getAutEstIdAutoritaEsterna() != null) {
				lAutDao.ricercaAutoritaEsternaByKey(lNotifica.getAutEstIdAutoritaEsterna());
				lAut = (AutoritaEsternaModel) lAutDao.getModelByKey();
				lTreeNot.add(new TreeModel(lAut));
			}

			lTreeRineMod.add(lTreeNot);

			// Evento
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(lNotifica.getEveIdEvento());
			EventoModel lEveMod = new EventoModel();
			lEveMod = (EventoModel) lEveSqlDao.getModelByKey();
			lNotEveMod.setEvento(lEveMod);
			TreeModel lTreeEveMod = new TreeModel(lEveMod);

			// Paolo Cherubini 02/02/2012 su segnalazione di Pina Marchese registrata come b2/rr/003
			// VVR Comunicazione rinnovo.
			// Nel fascicolo è stato assegnato il nuovo magistrato (Vittorio Corsi), la comunicazione aggancia
			// sempre il vecchio magistrato. Questo in tutti i fascicoli che si trovano con un magistrato
			// cambiato.
			// PC x tale modifico cambio la query prendo il magistrato dal fascicolo e non dall'evento
			// nota1 salgo le seguenti 4 righe mi servono per il codice magistrato
			// Fascicolo
			BigDecimal lKeyFascicolo = lEveMod.getFasSieIdFascicoloSiep();
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

			// magistrato
			/*
			 * lMadDao = new MagistratoSqlDAO(lConn); MagistratoModel lMag = new MagistratoModel();
			 * if(lEveMod.getCodMagistrato() != null) {
			 * lMadDao.ricercaMagistratoByCod(lEveMod.getCodMagistrato()); lMag = (MagistratoModel)
			 * lMadDao.getModelByKey(); lTreeEveMod.add(new TreeModel(lMag)); }
			 */

			// Magistrato competente
			MagistratoModel lMag = null;
			MagistratoCompetenteMagistratoSqlDAO lMagSql = null;
			lMagSql = new MagistratoCompetenteMagistratoSqlDAO(lConn);
			lMagSql.ricercaMagistratoCompetenteByFascicolo(lFasModel.getIdFascicoloSiep());
			MagistratoCompetenteMagistratoModel lMagModel = (MagistratoCompetenteMagistratoModel) lMagSql
					.getModelByKey();
			if (lMagModel != null)
				lMag = new MagistratoModel(lMagModel.getMagistrato());
			if (lMag != null)
				lTreeEveMod.add(new TreeModel(lMag));
			// fine Paolo Cherubini 02/02/2012

			// Verbale
			lVerDao = new VerbaleSqlDAO(lConn);
			lVerDao.ricercaVerbaleByKey(aRinModel.getVerIdVerbale());
			VerbaleModel lVerMod = new VerbaleModel();
			lVerMod = (VerbaleModel) lVerDao.getModelByKey();
			TreeModel lTreeVerMod = new TreeModel(lVerMod);
			lTreeRineMod.add(lTreeVerMod);

			lTreeRoot = new TreeModel(createRoot(lNotEveMod, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			// Fascicolo le 4 righe relative al Dao sono state spostate sopra leggi nota1
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			if (lFasModel != null
					&& lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res") || lFasModel
							.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr()2:4 " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(StampaUtils
							.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn, null);
			TreeModel lTreeFasMod = new TreeModel(lFasModel);

			// Avvocati
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
			Vector lAvvocati = new Vector(lAvvDao.getModels());

			if (lAvvocati != null) {
				Iterator lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItx.next();
					lTreeFasMod.add(new TreeModel(lAvv.getAvvocato()));
				}
			}

			lTreeRoot.add(lTreeRineMod);
			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(this.getTreeSentenza(lFasModel, lConn));
			lTreeRoot.add(lTreeEveMod);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaController.prelevaDatiRinnovo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lNotDao);
			cleanup(lEveSqlDao);
			cleanup(lVerDao);
			cleanup(lAutDao);
			cleanup(lMadDao);
			cleanup(lAvvDao);

			cleanup(lConn);
		}
		return lTreeRoot;
	}
	/*-----------------------------------Fine Preleva i campi per la stampa-----------------------------------------*/

}