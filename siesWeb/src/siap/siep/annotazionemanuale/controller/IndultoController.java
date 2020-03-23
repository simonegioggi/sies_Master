package siap.siep.annotazionemanuale.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

/**
 *
 * <p>
 * Title: IndultoController
 * </p>
 * <p>
 * Description: Controller per le funzioni dell'Indulto
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class IndultoController extends GenericController implements IIndulto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ExValidaProvvedimentoIndulto
	 *
	 * @param aEvento
	 * @param aPenRes
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExValidaProvvedimentoIndulto(EventoModel aEvento, PenaResiduaModel aPenRes)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		AnnotazioneManualeDAO lAnnManualeDAO = null;
		AnnotazioneManualeSqlDAO lAnnManuSqlDAO = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			aEvento.setFlagDocumentoRegistrato("S");
			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();
			lEveDao.stop();

			// Ricerco l'evento Ordinanza per il provvedimento indulto
			lEveSqlDao = new EventoSqlDAO(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Ricerca dell'evento 03 Ordinanza");

			EventoModel EveOrd = new EventoModel();
			EveOrd.setCodTipoEvento("01");
			EveOrd.setCodTipoProvvedimento("03");
			// Paolo Cherubini 24/05/2011
			// 0284 Applicazione Amnistia / Indulto
			// 0285 Applicazione Depenalizzazione
			// 0286 Applicazione incostituzionalita'
			// al già presente 0284 aggiungo il 0286 perchè ho effettuato una richiesta del GE di
			// incostituzionalità
			// poi ho scaricato la decisione del GE e quando vado a validare il Provvedimento del PM devo
			// anche
			// validare la relativa ordinanza collegato. Ho quindi modificato in eventosqlDao il metodo per
			// fargli accettare
			// all'interno del codmotivo più valori
			// 20/10/2011 aggiunto 0285 Applicazione Depenalizzazione.
			EveOrd.setCodMotivo("0284,0285,0286");
			// EveOrd.setCodMotivo("0284");
			EveOrd.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			lEveSqlDao.ricercaEventoNonRegistrato(EveOrd);
			EventoModel EveOrdRic = (EventoModel) lEveSqlDao.getModelByKey();
			if (EveOrdRic != null) { // Validazione dell'ordinanza
										// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
										// siesLogger al posto di mLog
				siesLogger.debug("EVENTO 03" + EveOrdRic);
				EveOrdRic.setFlagDocumentoRegistrato("S");
				EveOrdRic.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				EveOrdRic.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				EveOrdRic.setDataAggiornamento(DateUtils.getSysDate());

				lEveDao.setDAOFromModelForUpdate(EveOrdRic);
				lEveDao.update();
				lEveDao.stop();
			}

			// VALIDA la PENA RESIDUA
			// dalla versione 3.1upd02 è possibile validare gli scarichi del GE anche
			// se non è stato effettuato il calcolo della pena. In questo caso aPenRes = null
			// e va duplicata ed agganciata al provvedimento l'ultima pena a sistema.
			if (aPenRes != null) {
				// Aggiorno la pena
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setCondizioneUpdate(aPenRes.getIdPenaResidua());
				lPenResDao.setFlagValidato("S");
				lPenResDao.update();
				lPenResDao.stop();
			} else {
				// Recupero l'ultima pena residua VALIDATA e la duplico agganciandola al
				// provvedimento di scarico
				// since 3.1upd02
				PenaResiduaModel lUltimaPena = null;
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(
						aEvento.getFasSieIdFascicoloSiep());
				lUltimaPena = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

				// aggiorno i campi
				lUltimaPena.setIdPenaResidua(null);
				lUltimaPena.setEveIdEvento(aEvento.getIdEvento());
				lUltimaPena.setMisAltIdMisuraAlternativa(null);

				lUltimaPena.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
				lUltimaPena.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
				lUltimaPena.setDataInserimento(aEvento.getDataAggiornamento());

				lUltimaPena.setCodOperatoreAggiornamento(null);
				lUltimaPena.setCodUfficioAggiornamento(null);
				lUltimaPena.setDataAggiornamento(null);

				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setDAOFromModel(lUltimaPena);
				lPenResDao.insert();
				lPenResDao.stop();
			}

			// STATO_PROCEDIMENTO
			// cancellazione
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aEvento.getFasSieIdFascicoloSiep());
			lStatoDao.delete();
			// inserimento
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			// 20/11/2011 Gestione diversificata del codice Stato Procedimento.
			// 0284 Applicazione Amnistia / Indulto
			// 0285 Applicazione Depenalizzazione
			// 0286 Applicazione incostituzionalita'

			// AMBROS a8-rr-222

			if (aEvento.getCodMotivo().equals("0284"))
				if (aEvento.getCodEsito().equals("R") || aEvento.getCodEsito().equals("I")
						|| aEvento.getCodEsito().equals("U"))
					lStatoProcMod.setCodStatoProcedimento("0254");
				else
					lStatoProcMod.setCodStatoProcedimento("0213");
			// END AMBROS

			if (aEvento.getCodMotivo().equals("0285"))
				lStatoProcMod.setCodStatoProcedimento("0215");
			if (aEvento.getCodMotivo().equals("0286"))
				lStatoProcMod.setCodStatoProcedimento("0217");

			lStatoProcMod.setData(aEvento.getDataEmissione());
			lStatoProcMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lStatoProcMod.setEveIdEvento(null);

			lStatoDao.setDAOFromModel(lStatoProcMod);
			lStatoDao.insert();

			// Validazione dell'Annotazione Manuale
			lAnnManualeDAO = new AnnotazioneManualeDAO(lConn);
			lAnnManuSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
			// Ricerca dell'annotazione Manuale
			lAnnManuSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());
			AnnotazioneManualeModel lAnnManMod = (AnnotazioneManualeModel) lAnnManuSqlDAO.getModelByKey();

			if (lAnnManMod != null && lAnnManMod.getIdAnnotazioneManuale() != null && aPenRes != null
					&& aPenRes.getIdPenaResidua() != null) {
				lAnnManMod.setFlagValidato("S");
				lAnnManMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				lAnnManMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lAnnManMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lAnnManMod.setPenResIdPenaResidua(aPenRes.getIdPenaResidua());
				lAnnManualeDAO.setDAOFromModelForUpdate(lAnnManMod);
				lAnnManualeDAO.update();
				lAnnManualeDAO.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("IndultoController.ExValidaProvvedimentoIndulto : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("IndultoController.ExValidaProvvedimentoIndulto : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lStatoDao);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lAnnManualeDAO);
			cleanup(lAnnManuSqlDAO);

			cleanup(lConn);
		}

		return null;
	}

}