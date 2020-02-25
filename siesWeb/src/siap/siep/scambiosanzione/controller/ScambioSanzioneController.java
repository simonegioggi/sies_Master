package siap.siep.scambiosanzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneRichiestaConvSqlDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ScambioSanzioneController
 * </p>
 * <p>
 * Description: Classe Controller per ScambioSanzione
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
public class ScambioSanzioneController extends SiapController implements IScambioSanzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua la ricerca dei dati ScambioSanzione
	 * 
	 * @param aScambioSanzione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaScambioSanzione(ScambioSanzioneModel aScambioSanzione) throws F3BException {
		Connection lConn = null;
		Vector lScambioSanzioni = new Vector();
		ScambioSanzioneDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScambioSanzioneDAO(lConn);
			lScaDao.setCondizioni(aScambioSanzione);
			lScaDao.setOrderBy();
			lScaDao.start();
			while (lScaDao.next()) {
				lScambioSanzioni.add((ScambioSanzioneModel) lScaDao.getModel());
			}
			lScaDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("ScambioSanzioneController.ExRicercaScambioSanzione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}

		return lScambioSanzioni;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ScambioSanzioneModel ExRicercaScambioSanzioneByEveIdEvento(BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;
		ScambioSanzioneModel lScambioSanzioneMod = new ScambioSanzioneModel();
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneSqlDao.ricercaScambioSanzioneByEveIdEvento(aIdEvento);
			lScambioSanzioneMod = (ScambioSanzioneModel) lScambioSanzioneSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneByEveIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lConn);
		}

		return lScambioSanzioneMod;
	}

	/**********************************************************************************
	 * Effettua la ricerca per chiave IdEvento senza controllare il flag di validazione
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 **********************************************************************************/
	public ScambioSanzioneModel ExRicercaScambioSanzioneByEveIdEventoNoControlValid(BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;
		ScambioSanzioneModel lScambioSanzioneMod = new ScambioSanzioneModel();
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneSqlDao.ricercaScambioSanzioneByEveIdEventoNoCtrlnValid(aIdEvento);
			lScambioSanzioneMod = (ScambioSanzioneModel) lScambioSanzioneSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneByEveIdEventoNoControlValid: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lConn);
		}

		return lScambioSanzioneMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ScambioSanzioneModel ExRicercaScambioSanzioneById(BigDecimal aIdScambioSanzione)
			throws F3BException {
		Connection lConn = null;
		ScambioSanzioneModel lScambioSanzioneMod = new ScambioSanzioneModel();
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneSqlDao.ricercaScambioSanzioneByKey(aIdScambioSanzione);
			lScambioSanzioneMod = (ScambioSanzioneModel) lScambioSanzioneSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lConn);
		}

		return lScambioSanzioneMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave (usata per conversione pene pec)
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public ScambioSanzioneModel ExRicercaScambioSanzioneByIdXRichConv(BigDecimal aIdScambioSanzione)
			throws F3BException {
		Connection lConn = null;
		ScambioSanzioneModel lScambioSanzioneMod = new ScambioSanzioneModel();
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneSqlDao.ricercaScambioSanzioneByKeyXRichConv(aIdScambioSanzione);
			lScambioSanzioneMod = (ScambioSanzioneModel) lScambioSanzioneSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneByIdRichConv: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lConn);
		}

		return lScambioSanzioneMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati ScambioSanzione e RichiestaConversione
	 * 
	 * @param aScambioSanzione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaScambioSanzioneRichConv(String TipoDec, BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		Vector lScambioSanzioni = new Vector();
		ScambioSanzioneRichiestaConvSqlDAO lScambioSanzioneRichiestaConvSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneRichiestaConvSqlDao = new ScambioSanzioneRichiestaConvSqlDAO(lConn);
			lScambioSanzioneRichiestaConvSqlDao.ricercaScambioSanzioneRichConv(TipoDec, aIdFascicolo);
			lScambioSanzioni = new Vector(lScambioSanzioneRichiestaConvSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneRichConv: Non posso leggere : "
							+ daoEx);
		}

		finally {
			cleanup(lScambioSanzioneRichiestaConvSqlDao);
			cleanup(lConn);
		}
		return lScambioSanzioni;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati ScambioSanzione e RichiestaConversione
	 * 
	 * @param aTipoProv
	 *            Array di Codici Tipo Provvedimento
	 * @param aIdFascicolo
	 *            Id Fascicolo SIEP
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector[] ExRicercaScambioSanzioneRichConv(String[] aTipoProv, BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		Vector lScambioSanzioni = new Vector();
		Vector lCodTipoProvv = new Vector();
		Vector[] lScaSanTipPro = new Vector[2];
		ScambioSanzioneRichiestaConvSqlDAO lScambioSanzioneRichiestaConvSqlDao = null;

		try {
			lConn = getDBConnection();
			for (int i = 0; i < aTipoProv.length; i++) {
				lScambioSanzioneRichiestaConvSqlDao = new ScambioSanzioneRichiestaConvSqlDAO(lConn);
				lScambioSanzioneRichiestaConvSqlDao
						.ricercaScambioSanzioneRichConv(aTipoProv[i], aIdFascicolo);
				Vector lScambio = new Vector(lScambioSanzioneRichiestaConvSqlDao.getModels());
				lScambioSanzioni.addAll(lScambio);
				for (int j = 0; j < lScambio.size(); j++) {
					lCodTipoProvv.add(aTipoProv[i]);
				}
			}

		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneRichConv: Non posso leggere : "
							+ daoEx);
		}

		finally {
			cleanup(lScambioSanzioneRichiestaConvSqlDao);
			cleanup(lConn);
		}
		lScaSanTipPro[0] = lScambioSanzioni;
		lScaSanTipPro[1] = lCodTipoProvv;

		return lScaSanTipPro;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati ScambioSanzione e RichiestaConversione 09/03/2015 Si imposta nella query
	 * il criterio di accoppiamento tra il MOTIVO_PROVVEDIMENTO e ESITO_PROVVEDIMENTO, individuando delle
	 * occorrenze con ESITO_PROVVEDIMENTO specifico
	 * 
	 * @param aTipoProv
	 *            Array di Codici Tipo Provvedimento
	 * @param aIdFascicolo
	 *            Id Fascicolo SIEP
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector[] ExRicercaScambioSanzioneRichConvEPS(String[] aTipoProv, BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		Vector lScambioSanzioni = new Vector();
		Vector lCodTipoProvv = new Vector();
		Vector[] lScaSanTipPro = new Vector[2];
		ScambioSanzioneRichiestaConvSqlDAO lScambioSanzioneRichiestaConvSqlDao = null;

		try {
			lConn = getDBConnection();
			for (int i = 0; i < aTipoProv.length; i++) {
				lScambioSanzioneRichiestaConvSqlDao = new ScambioSanzioneRichiestaConvSqlDAO(lConn);
				lScambioSanzioneRichiestaConvSqlDao.ricercaScambioSanzioneRichConvEPS(aTipoProv[i],
						aIdFascicolo);
				Vector lScambio = new Vector(lScambioSanzioneRichiestaConvSqlDao.getModels());
				lScambioSanzioni.addAll(lScambio);
				for (int j = 0; j < lScambio.size(); j++) {
					lCodTipoProvv.add(aTipoProv[i]);
				}
			}

		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneRichConv: Non posso leggere : "
							+ daoEx);
		}

		finally {
			cleanup(lScambioSanzioneRichiestaConvSqlDao);
			cleanup(lConn);
		}
		lScaSanTipPro[0] = lScambioSanzioni;
		lScaSanTipPro[1] = lCodTipoProvv;

		return lScaSanTipPro;
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aScambioSanzione
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountScambioSanzione(ScambioSanzioneModel aScambioSanzione) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneSqlDao.getCountScambioSanzione(aScambioSanzione);
			lScambioSanzioneSqlDao.start();
			lScambioSanzioneSqlDao.next();
			lCount = lScambioSanzioneSqlDao.getBigDecimal("HowManyRecords");
			lScambioSanzioneSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExGetCountScambioSanzione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aScambioSanzione
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaScambioSanzionePaged(ScambioSanzioneModel aScambioSanzione, int aPage)
			throws F3BException {
		Connection lConn = null;
		Vector lScambioSanzioni = new Vector();
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneSqlDao.ricercaScambioSanzionePaged(aScambioSanzione, aPage);
			lScambioSanzioni = new Vector(lScambioSanzioneSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzionePaged: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lConn);
		}
		return lScambioSanzioni;
	}

	/*****************************************************************************
	 * Dato il numero di un fascicolo SIEP, effettua la ricerca dei dati ScambioSanzione ad esso collegato
	 * 
	 * @param aFascicolo
	 *            BigDecimal
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public List ExRicercaScambioSanzioneByIdFascicoloSiepNaturaTipo(BigDecimal aFascicolo,
			String[] aTipoDecisone, String[] aNaturaSanzione, String[] aTipoSanzione)

	throws F3BException {
		Connection lConn = null;
		List lScambioSanzioni = new ArrayList();
		ScambioSanzioneSqlDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScambioSanzioneSqlDAO(lConn);
			lScaDao.ricercaByIdFascicoloNaturaTipo(aFascicolo, aTipoDecisone, aNaturaSanzione, aTipoSanzione);

			lScambioSanzioni = new ArrayList(lScaDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException(
					"ScambioSanzioneController.ExRicercaScambioSanzioneByIdFascicoloSiepNaturaTipo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}

		return lScambioSanzioni;
	}

	/*****************************************************************************
	 * Dato uno ScambioSanzioneModel, aggiorna lo Stato del procedimento del fascicolo secondo quanto indicato
	 * per lo
	 * 
	 * @param aFascicolo
	 *            BigDecimal
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/

	public EventoModel ExInserisciEventoAggiornaStatoProcedimento(EventoModel aEveMod,
			PosizioneGiuridicaModel aPos, PenaResiduaModel aPenaResidua, StatoProcedimentoModel aStatoProcMod)
			throws F3BException {

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		PenaResiduaDAO lPenaResDao = null;
		PosizioneGiuridicaDAO lPosDao = null;

		Connection lConn = null;

		EventoModel lEveRet = new EventoModel(aEveMod);
		try {
			lConn = getDBTransaction();

			lStatoDao = new StatoProcedimentoDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lPenaResDao = new PenaResiduaDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			// evento
			BigDecimal lProgr = lSqlDAO.getProgressivo(aEveMod);
			aEveMod.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			lEveDao.setDAOFromModel(aEveMod);

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveRet.setIdEvento(lKeyEvento);

			// Inserisce Pena Residua
			PenaResiduaModel lPenMod = aPenaResidua;
			if (lPenMod != null) {

				lPenMod.setIdPenaResidua(null);
				lPenMod.setCodOperatoreAggiornamento(null);
				lPenMod.setDataAggiornamento(null);
				lPenMod.setCodUfficioAggiornamento(null);

				lPenMod.setFlagValidato("S");
				lPenMod.setEveIdEvento(lKeyEvento);
				lPenMod.setCodOperatoreInserimento(aEveMod.getCodOperatoreInserimento());
				lPenMod.setDataInserimento(DateUtils.getSysDate());
				lPenMod.setCodUfficioInserimento(aEveMod.getCodUfficioInserimento());

				lPenaResDao.setDAOFromModel(lPenMod);
				lPenaResDao.insert();
				lPenaResDao.stop();
			}

			// chiudo ed inserisco posizione giuridica
			lPosDao.setCodUfficioAggiornamento(aEveMod.getCodUfficioInserimento());
			lPosDao.setCodOperatoreAggiornamento(aEveMod.getCodOperatoreInserimento());
			lPosDao.setDataAggiornamento(DateUtils.getSysDate());
			lPosDao.setDataFine(DateUtils.getSysDate());

			lPosDao.setCondizioneUpdate(aPos.getIdPosizioneGiuridica());
			lPosDao.update();
			lPosDao.stop();

			aPos.setIdPosizioneGiuridica(null);
			aPos.setCodOperatoreInserimento(aEveMod.getCodOperatoreInserimento());
			aPos.setDataInserimento(DateUtils.getSysDate());
			aPos.setCodUfficioInserimento(aEveMod.getCodUfficioInserimento());
			aPos.setIdEventoRiferimento(lKeyEvento);
			aPos.setDataInizio(DateUtils.getSysDate());

			lPosDao.setDAOFromModel(aPos);
			lPosDao.insert();
			lPosDao.stop();

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aStatoProcMod.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// - Inserisce
			aStatoProcMod.setEveIdEvento(lKeyEvento);
			lStatoDao.setDAOFromModel(aStatoProcMod);
			lStatoDao.insert();
			lStatoDao.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(daoEx.getMessage(), daoEx);

			throw new F3BException("ScambioSanzioneController.ExInserisciEventoAggiornaStatoProcedimento : "
					+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(ex.getMessage(), ex);

			throw new F3BException("ScambioSanzioneController.ExInserisciEventoAggiornaStatoProcedimento : "
					+ ex);
		} finally {
			cleanup(lStatoDao);
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lPosDao);

			cleanup(lConn);
		}
		return lEveRet;
	}

	public EventoModel ExInserisciRevocaConversione(EventoNotificaModel aEveUffMod,
			DepositoOrdinanzaPcModel lDepOrdMod, DepositoDecretoModel lDepDecMod, TenoreModel lTenMod,
			ScambioSanzioneModel lScSanzioneMod, LuogoDetenzioneModel aLuogoDetenzione,
			AltraCausaModel aAltraCausa, EventoModel aEveMod, PosizioneGiuridicaModel aPos,
			FascicoloSiepModel aFascicoloModel, PenaResiduaModel aPenaResidua,
			StatoProcedimentoModel aStatoProcMod, String aPosizione, AnnotazioneManualeModel aAnnMod)
			throws F3BException {

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		PenaResiduaDAO lPenaResDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		AnnotazioneManualeDAO AnnDao = null;
		AltraCausaDAO lAltraCausaDao = null;
		AltraCausaSqlDAO lAltraCausaSqlDao = null;
		FascicoloSiepDAO lFasDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;

		ScambioSanzioneDAO lScaSanDao = null;
		TenoreDAO lTenDAO = null;
		DepositoOrdinanzaPcDAO lDepOrdDAO = null;
		DepositoDecretoDAO lDepDecDAO = null;

		Connection lConn = null;

		EventoModel lEveRet = new EventoModel(aEveMod);
		try {
			lConn = getDBTransaction();

			lStatoDao = new StatoProcedimentoDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lSqlDAO = new EventoSqlDAO(lConn);
			lPenaResDao = new PenaResiduaDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			AnnDao = new AnnotazioneManualeDAO(lConn);

			lScaSanDao = new ScambioSanzioneDAO(lConn);
			lTenDAO = new TenoreDAO(lConn);
			lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
			lDepDecDAO = new DepositoDecretoDAO(lConn);

			// inserimento evento ORDINANZA/DECRETO
			if (aEveUffMod != null && aEveUffMod.getEvento() != null) {

				lEveDao.setDAOFromModel(aEveUffMod.getEvento());
				BigDecimal lKeyEventoUFF = null;
				lKeyEventoUFF = lEveDao.insert();
				lEveDao.stop();

				// insert sanzione sostitutiva
				lScSanzioneMod.setEveIdEvento(lKeyEventoUFF);
				lScaSanDao.setDAOFromModel(lScSanzioneMod);
				lScaSanDao.insert();

				if ("03".equals(aEveUffMod.getEvento().getCodTipoProvvedimento())) {
					// insert Deposito Ordinanza
					lDepOrdMod.setIdEventoGenerato(lKeyEventoUFF);
					lDepOrdDAO.setDAOFromModel(lDepOrdMod);
					BigDecimal lKeyDepOrd = lDepOrdDAO.insert();
					lTenDAO.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);
				} else // altrimenti è 02
				{
					// insert Deposito Decreto
					lDepDecDAO.setIdEventoGenerato(lKeyEventoUFF);
					lDepDecDAO.setDAOFromModel(lDepDecMod);
					BigDecimal lKeyDepDec = lDepDecDAO.insert();
					lTenDAO.setDepDecIdDepositoDecreto(lKeyDepDec);
				}

				// insert tenore
				lTenDAO.setDAOFromModel(lTenMod);
				lTenDAO.insert();

			}

			// evento provvedimento
			aEveMod.setEveIdEvento(lScSanzioneMod.getEveIdEvento());

			lEveDao.setDAOFromModel(aEveMod);

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDao.insert();
			lEveRet.setIdEvento(lKeyEvento);

			// Inserisce Pena Residua
			PenaResiduaModel lPenMod = aPenaResidua;
			if (lPenMod != null) {
				lPenMod.setIdPenaResidua(null);
				lPenMod.setCodOperatoreAggiornamento(null);
				lPenMod.setDataAggiornamento(null);
				lPenMod.setCodUfficioAggiornamento(null);

				lPenMod.setFlagValidato("S");
				lPenMod.setEveIdEvento(lKeyEvento);
				lPenMod.setCodOperatoreInserimento(aEveMod.getCodOperatoreInserimento());
				lPenMod.setDataInserimento(DateUtils.getSysDate());
				lPenMod.setCodUfficioInserimento(aEveMod.getCodUfficioInserimento());

				lPenaResDao.setDAOFromModel(lPenMod);
				lPenaResDao.insert();
				lPenaResDao.stop();
			}

			// chiudo ed inserisco posizione giuridica
			lPosDao.setCodUfficioAggiornamento(aEveMod.getCodUfficioInserimento());
			lPosDao.setCodOperatoreAggiornamento(aEveMod.getCodOperatoreInserimento());
			lPosDao.setDataAggiornamento(DateUtils.getSysDate());
			lPosDao.setDataFine(DateUtils.getSysDate());

			lPosDao.setCondizioneUpdate(aPos.getIdPosizioneGiuridica());
			lPosDao.update();
			lPosDao.stop();

			lPosDao.setCodOperatoreInserimento(aEveMod.getCodOperatoreInserimento());
			lPosDao.setDataInserimento(DateUtils.getSysDate());
			lPosDao.setCodUfficioInserimento(aEveMod.getCodUfficioInserimento());
			lPosDao.setIdEventoRiferimento(lKeyEvento);
			lPosDao.setDataInizio(DateUtils.getSysDate());
			lPosDao.setCodPosizioneGiuridica(aPosizione);
			lPosDao.setCodPosizioneProcessuale("-");
			lPosDao.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());

			BigDecimal lKeyPosGiu = lPosDao.insert();
			lPosDao.stop();

			// Modifica Fascicolo Associato alla Posizione Giuridica
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setFlagAltraCausa(aFascicoloModel.getFlagAltraCausa());
			lFasDao.setCodTipoPosLibero(aFascicoloModel.getCodTipoPosLibero());
			lFasDao.selCondizioneUpdate(aFascicoloModel.getIdFascicoloSiep());
			lFasDao.update();

			// Luogo Detenzione
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);

			lLuoDetSqlDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aFascicoloModel.getIdFascicoloSiep());
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();

			if (lLuogoDetenzione != null) {
				lLuoDetDao.setIdLuogoDetenzione(lLuogoDetenzione.getIdLuogoDetenzione());

				if (lLuogoDetenzione.getDataFineDetenzione() == null)
					lLuoDetDao.setDataFineDetenzione(new Date());
				lLuoDetDao.selByKey();
				lLuoDetDao.update();
				lLuoDetDao.stop();
			}

			aLuogoDetenzione.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
			lLuoDetDao.setDAOFromModel(aLuogoDetenzione);
			lLuoDetDao.insert();

			// Altra Causa
			lAltraCausaSqlDao = new AltraCausaSqlDAO(lConn);
			lAltraCausaSqlDao.ricercaAltraCausaByIdFascicolo(aFascicoloModel.getIdFascicoloSiep());
			AltraCausaModel lAltraCausa = (AltraCausaModel) lAltraCausaSqlDao.getModelByKey();

			lAltraCausaDao = new AltraCausaDAO(lConn);
			if (aFascicoloModel.getFlagAltraCausa() != null
					&& aFascicoloModel.getFlagAltraCausa().equals("S")) {
				if (lAltraCausa == null) {
					lAltraCausaDao.setDAOFromModel(aAltraCausa);
					lAltraCausaDao.insert();
				} else {
					aAltraCausa.setIdAltraCausa(lAltraCausa.getIdAltraCausa());
					lAltraCausaDao.setDAOFromModelForUpdate(aAltraCausa);
					lAltraCausaDao.update();
				}
			} else {
				if (lAltraCausa != null) {
					lAltraCausaDao.setIdAltraCausa(lAltraCausa.getIdAltraCausa());
					lAltraCausaDao.selByKey();
					lAltraCausaDao.delete();
				}
			}

			// inserisco l'annotazione manuale
			aAnnMod.setEveIdEvento(lKeyEvento);
			aAnnMod.setFasSieIdFascicoloSiep(aEveMod.getFasSieIdFascicoloSiep());

			AnnDao.setDAOFromModel(aAnnMod);
			AnnDao.insert();
			AnnDao.stop();

			/*
			 * 04/08/2015 Non va inserito adesso il nuovo Stato Procedimento, ma al momento dell' inserimento
			 * del fascicolo di classe I // - Cancella eventuali record prima di inserire un nuovo
			 * STATO_PROCEDIMENTO
			 * lStatoDao.setCondizioneByIdFascicolo(aStatoProcMod.getFasSieIdFascicoloSiep());
			 * lStatoDao.delete();
			 * 
			 * // - Inserisce aStatoProcMod.setEveIdEvento(lKeyEvento);
			 * lStatoDao.setDAOFromModel(aStatoProcMod); lStatoDao.insert(); lStatoDao.stop();
			 */
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(daoEx.getMessage(), daoEx);

			throw new F3BException("ScambioSanzioneController.ExInserisciRevocaConversione : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(ex.getMessage(), ex);

			throw new F3BException("ScambioSanzioneController.ExInserisciRevocaConversione : " + ex);
		} finally {
			cleanup(lStatoDao);
			cleanup(lEveDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);
			cleanup(lPosDao);
			cleanup(AnnDao);
			cleanup(lAltraCausaDao);
			cleanup(lAltraCausaSqlDao);
			cleanup(lFasDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);

			cleanup(lScaSanDao);
			cleanup(lTenDAO);
			cleanup(lDepOrdDAO);
			cleanup(lDepDecDAO);

			cleanup(lConn);
		}
		return lEveRet;
	}

}