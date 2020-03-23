package siap.siep.ripristino.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepDAO;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

/**
 * <p>
 * Title: RipristinoController
 * </p>
 * <p>
 * Description: Classe Controller per Ripristino Esecuzione
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
public class RipristinoController extends SiapController implements IRipristino {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EventoModel ExUpdateValidaProvvedimentoRipristino(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {

		Connection lConn = null;
		Connection lConnBlob = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PenaResiduaDAO lPenResDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScaSqlDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecOrdSqlDao = null;
		DecretoOrdinanzaSiepDAO lDecOrdDao = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			// EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao = new PenaResiduaDAO(lConn);

			// AGGIORNA PENA RESIDUA
			lPenResSqlDao.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
			PenaResiduaModel lPenResMod0272 = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			if (lPenResMod0272 != null) {
				lPenResDao.setFlagValidato("S");
				lPenResDao.setCondizioneUpdate(lPenResMod0272.getIdPenaResidua());
				lPenResDao.update();
				lPenResDao.stop();
			}

			// AGGIORNA DECRETO_ORDINANZA
			lDecOrdSqlDao = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecOrdSqlDao.ricercaDecretoOrdinanzaSiepByIdEvento(aEvento.getIdEvento());
			DecretoOrdinanzaSiepModel lDecOrdMod = (DecretoOrdinanzaSiepModel) lDecOrdSqlDao.getModelByKey();

			if (lDecOrdMod != null) {
				lDecOrdDao = new DecretoOrdinanzaSiepDAO(lConn);
				lDecOrdDao.setFlagElaborato("S");
				lDecOrdDao.setCondizioneUpdate(lDecOrdMod.getIdDecretoOrdinanzaSiep());
				lDecOrdDao.update();
				lDecOrdDao.stop();
			}

			// NOME_PROVVEDIMENTO
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP113");
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// STATO_PROCEDIMENTO
			// cancellazione
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// inserimento
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setCodStatoProcedimento("0092");
			lStatoProcMod.setData(aEvento.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();

			// Cerca POSIZIONE_GIURIDICA corrente
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			/* PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) */lPosSqlDao.getModelByKey();

			// String lCodPosizione = lPosMod.getCodPosizioneGiuridica();

			// Cerca l'ultima PENA_RESIDUA per fascicolo
			PenaResiduaModel lPenResMod = new PenaResiduaModel();

			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			/*
			 * // SCADENZARIO boolean lFlagLibero = false; if (lCodPosizione != null &&
			 * (lCodPosizione.equals("07") || lCodPosizione.equals("10"))) // LIBERO { lFlagLibero = true; }
			 *
			 * boolean lFlagAltraCausa = false; if (aFascicolo.getFlagAltraCausa() != null &&
			 * aFascicolo.getFlagAltraCausa().equals("S")) // ALTRA CAUSA { lFlagAltraCausa = true; } // Se
			 * non è libero oppure è libero ma detenuto per altra causa // modifica/inserisce lo scadenzario
			 * fine pena if (!lFlagLibero || (lFlagLibero && lFlagAltraCausa)) {
			 */
			// Cerca l'ultimo scadenzario fine pena per quel fascicolo
			// assume che se ce n'è, è uno
			lScaDao = new ScadenzarioDAO(lConn);
			ScadenzarioModel lScaMod = new ScadenzarioModel();

			lScaMod.setCodTipoScadenzario("02"); // FINE PENA
			lScaMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			if (lPenResMod != null) {
				if (lPenResMod.getDataInizio() != null)
					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
				if (lPenResMod.getDataFine() != null)
					lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
			}
			lScaMod.setFlagVisto("N");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Residua Scadenzario : " + lPenResMod);

			if (lScaMod.getDataInizioScadenza() != null && lScaMod.getDataFineScadenza() != null) {
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				lScaSqlDao.ricercaScadenzarioVerbaleArresto(lScaMod);

				ScadenzarioModel lScaPresente = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				// Se non c'e' lo inserisce
				if (lScaPresente == null) {
					lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataInserimento(aEvento.getDataAggiornamento());

					lScaDao.setDAOFromModel(lScaMod);
					lScaDao.insert();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserito scadenzario : " + lScaMod);
				} else // Se c'e' lo modifica
				{
					lScaMod.setIdScadenzario(lScaPresente.getIdScadenzario());

					lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());

					lScaDao.setDAOFromModelForUpdate(lScaMod);
					lScaDao.update();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Modificato scadenzario : " + lScaMod);
				}
			}
			// }

			commit(lConn);

			// ------- EVENTO--------
			lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.setFlagDocumentoRegistrato("S");
			lEveDaoBlob.setFlagVideoSiep("S");
			lEveDaoBlob.setFlagStampaSiep("S");
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			commit(lConnBlob);
		} catch (DAOException daoEx) {
			rollback(lConn);
			rollback(lConnBlob);
			daoEx.printStackTrace();
			throw new F3BException("RipristinoController.ExUpdateValidaProvvedimentoRipristino : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			rollback(lConnBlob);
			ex.printStackTrace();
			throw new F3BException("RipristinoController.ExUpdateValidaProvvedimentoRipristino : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lStatoDao);
			cleanup(lPosSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenResDao);
			cleanup(lScaDao);
			cleanup(lScaSqlDao);
			cleanup(lDecOrdSqlDao);
			cleanup(lDecOrdDao);
			cleanup(lEveDaoBlob);

			cleanup(lConn);
			cleanup(lConnBlob);
		}

		return aEvento;
	}

}