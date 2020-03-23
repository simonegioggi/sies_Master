package siap.sius.remissionedebito.controller;

/**
* <p>Title: RichiestaRemissioneController</p>
* <p>Description: Classe Controller per RichiestaRemissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
//import siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaDAO;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.remissionedebito.dao.RichiestaRemissioneDAO;
import siap.sius.remissionedebito.dao.RichiestaRemissioneSqlDAO;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RichiestaRemissioneController extends SiapController implements IRichiestaRemissione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua l'inserimento di un RichiestaRemissione a partire dai dati contenuti nel Model
	 *
	 * @param aRichiestaRemissione
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public RichiestaRemissioneModel ExInserisciRichiestaRemissione(
			RichiestaRemissioneModel aRichiestaRemissione) throws F3BException {

		Connection lConn = null;

		RichiestaRemissioneDAO lRicDao = null;
		RichiestaRemissioneModel lRicMod = null;

		try {
			lConn = getDBConnection();

			lRicDao = new RichiestaRemissioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaRemissione);

			BigDecimal lSequence = lRicDao.insert();

			commit(lConn);

			lRicMod = new RichiestaRemissioneModel(aRichiestaRemissione);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestaRemissione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaRemissioneController.ExInserisciRichiestaRemissione: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException("RichiestaRemissioneController.ExInserisciRichiestaRemissione: " + e);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicMod;
	}

	/**
	 * Effettua la ricerca dei dati RichiestaRemissione
	 *
	 * @param aRichiestaRemissione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		Vector lRichiestaRemissioni = new Vector();
		RichiestaRemissioneSqlDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaRemissioneSqlDAO(lConn);
			lRicDao.ricercaRichiestaRemissione(aRichiestaRemissione);
			lRicDao.start();
			while (lRicDao.next()) {
				lRichiestaRemissioni.add(lRicDao.getModel());
			}
			lRicDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaRemissioneController.ExRicercaRichiestaRemissione: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRichiestaRemissioni;
	}

	/**
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaRemissioneModel ExRicercaRichiestaRemissioneById(BigDecimal aIdRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		RichiestaRemissioneModel lRichiestaRemissioneMod = new RichiestaRemissioneModel();
		RichiestaRemissioneSqlDAO lRichiestaRemissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaRemissioneSqlDao = new RichiestaRemissioneSqlDAO(lConn);
			lRichiestaRemissioneSqlDao.ricercaRichiestaRemissioneByKey(aIdRichiestaRemissione);
			lRichiestaRemissioneMod = (RichiestaRemissioneModel) lRichiestaRemissioneSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaRemissioneController.ExRicercaRichiestaRemissioneById:  " + daoEx);
		} finally {
			cleanup(lRichiestaRemissioneSqlDao);
			cleanup(lConn);
		}

		return lRichiestaRemissioneMod;
	}

	/**
	 * Metodo che modifica i dati dell'RichiestaRemissione Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aRichiestaRemissione
	 *            Model con i nuovi valori
	 * @throws F3BException
	 */
	public void ExModificaRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		RichiestaRemissioneDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaRemissioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaRemissione);
			lRicDao.selCondizioneUpdate(aRichiestaRemissione.getIdRichiestaRemissione());
			lRicDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaRemissioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la cancellazione del record
	 *
	 * @param aRichiestaRemissione
	 * @throws F3BException
	 */
	public void ExCancellaRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		RichiestaRemissioneDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaRemissioneDAO(lConn);
			lRicDao.selCondizioneUpdate(aRichiestaRemissione.getIdRichiestaRemissione());
			lRicDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("RichiestaRemissioneController.ExCancellaRichiestaRemissione: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aRichiestaRemissione
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountRichiestaRemissione(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		RichiestaRemissioneSqlDAO lRichiestaRemissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaRemissioneSqlDao = new RichiestaRemissioneSqlDAO(lConn);
			lRichiestaRemissioneSqlDao.getCountRichiestaRemissione(aRichiestaRemissione);
			lRichiestaRemissioneSqlDao.start();
			lRichiestaRemissioneSqlDao.next();
			lCount = lRichiestaRemissioneSqlDao.getBigDecimal("HowManyRecords");
			lRichiestaRemissioneSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaRemissioneController.ExGetCountRichiestaRemissione: " + daoEx);
		} finally {
			cleanup(lRichiestaRemissioneSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/**
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aRichiestaRemissione
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiestaRemissionePaged(RichiestaRemissioneModel aRichiestaRemissione, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lRichiestaRemissioni = new Vector();
		RichiestaRemissioneSqlDAO lRichiestaRemissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaRemissioneSqlDao = new RichiestaRemissioneSqlDAO(lConn);
			lRichiestaRemissioneSqlDao.ricercaRichiestaRemissionePaged(aRichiestaRemissione, aPage);
			lRichiestaRemissioni = new Vector(lRichiestaRemissioneSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaRemissioneController.ExRicercaRichiestaRemissionePaged: " + daoEx);
		} finally {
			cleanup(lRichiestaRemissioneSqlDao);
			cleanup(lConn);
		}
		return lRichiestaRemissioni;
	}

	/**
	 * controller utilizzato per l'inserimento di un fascicolo di Remissione delle pene pecuniare (CLasse
	 * VIII) a partire da un Fascicolo pena detentiva (CLasse I) duplica il soggetto per non incorrere
	 * nell'errore sulla chiave univoca fascicolo-soggetto-sentenza e comunque in linea con le nuove
	 * disposizione del SuperSoggetto. inserisce evento di conversione sul Fascicolo pena detentiva (CLasse I)
	 * inserisce fascicolo di Remissione delle pene pecuniare (CLasse VIII) inserisce rischiesta di
	 * conversione
	 *
	 * @param: aSoggetto
	 *             Model con i dati da inserire aEvento Model con i dati da inserire aFascicoloSiep Model con
	 *             i dati da inserire aRichiestaRemissione Model con i dati da inserire
	 * @return il model aRichiestaRemissione con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public RichiestaRemissioneModel ExInserisciRichiestaRemissionedaClasseI(SoggettoModel aSoggetto,
			EventoModel aEvento, FascicoloSiepModel aFascicoloSiep,
			DettaglioFascicoloModel aDettaglioFascicolo, RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		RichiestaRemissioneModel lRicMod = null;

		SoggettoDAO lSogDao = null;
		EventoDAO lEveDao = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		RichiestaRemissioneDAO lRicDao = null;
		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		MagistratoCompetenteDAO lMagComDao = null;
		ReatoDAO lReaDao = null;
		CircostanzaDAO lCirDao = null;
		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanDao = null;

		try {
			lConn = getDBTransaction();
			// duplico il soggetto
			lSogDao = new SoggettoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggetto);
			BigDecimal lSequence = lSogDao.insert();
			aSoggetto.setIdSoggetto(lSequence);
			// preparo il nuovo fascicolo classe VIII collegandolo al nuovo soggetto
			// FascicoloSiepModel lFasMod = aDettaglioFascicolo.getFascicoloSiep();
			aFascicoloSiep.setSogIdSoggetto(lSequence);

			// inserisco l'evento di conversione nel fascicolo di classe I
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento);
			lSequence = lEveDao.insert();

			lFasDao = new FascicoloSiepDAO(lConn);
			// Cerco il Progressivo rispettivamente al tipo progressivo impostato
			// tipo fascicolo = 8 fascicolo di conversione
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			int nTipo = 7;
			aFascicoloSiep.setTipoProgressivo(nTipo);
			lFasDaoSql.getProgressivoFascicoloSiep(aFascicoloSiep);
			lFasDaoSql.start();
			int lMaxProgr = 0;

			if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
				lMaxProgr = lFasDaoSql.getInt("aMAX");
			lFasDaoSql.stop();

			// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
			int lTipoProgr = aFascicoloSiep.getTipoProgressivo();
			if (lMaxProgr == 0) {
				if (lTipoProgr == 1)
					aFascicoloSiep.setChiaveProgr(new BigDecimal(1));
				else
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
			} else {
				aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
			}

			// inserisco il nuovo fascicolo di classe VII
			lFasDao.setDAOFromModel(aFascicoloSiep);
			lSequence = lFasDao.insert();
			aFascicoloSiep.setIdFascicoloSiep(lSequence);

			// inserisco la richiesta di conversione
			aRichiestaRemissione.setFasSieIdFascicoloSiep(lSequence);
			lRicDao = new RichiestaRemissioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaRemissione);
			lSequence = lRicDao.insert();
			aRichiestaRemissione.setIdRichiestaRemissione(lSequence);

			/*
			 * // duplico pena complessiva collegandolo al nuovo fascicolo (errrato) // provo a crearla nuova
			 * if (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() != null){
			 * lPenDao = new PenaComplessivaDAO(lConn);
			 *
			 * PenaComplessivaModel lPenMod =
			 * aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();
			 *
			 * lPenMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			 * lPenDao.setDAOFromModel(lPenMod ); lSequence = lPenDao.insert();
			 *
			 * // duplico sanzione sostitutiva collegandolo al nuovo fascicolo if
			 * (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva() != null){
			 * lSanDao = new SanzioneSostitutivaDAO(lConn); SanzioneSostitutivaModel lSanMod =
			 * aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva();
			 * //lSanMod.setPenComIdPenaComplessiva(lPenMod.getIdPenaComplessiva());
			 * lSanDao.setDAOFromModel(lSanMod ); lSequence = lSanDao.insert(); } }
			 */
			lPenDao = new PenaComplessivaDAO(lConn);
			if (aDettaglioFascicolo.getPenaResidua() != null) {
				PenaComplessivaModel lPenMod = new PenaComplessivaModel();
				lPenMod.setCodTipoPenaDetentiva("-");
				lPenMod.setImportoAmmenda(aDettaglioFascicolo.getPenaResidua().getImportoAmmenda());
				lPenMod.setImportoMulta(aDettaglioFascicolo.getPenaResidua().getImportoMulta());
				lPenMod.setCodTipoRito("-");
				lPenMod.setFlagPenaInContinuazione("N");
				lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lPenMod.setDataInserimento(DateUtils.getSysDate());
				lPenMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lPenDao.setDAOFromModel(lPenMod);
				lSequence = lPenDao.insert();
			} else {
				if (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva()
						.getPenaComplessiva() != null) {
					PenaComplessivaModel lPenMod = new PenaComplessivaModel();
					lPenMod.setCodTipoPenaDetentiva("-");
					lPenMod.setImportoAmmenda(aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva()
							.getPenaComplessiva().getImportoAmmenda());
					lPenMod.setImportoMulta(aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva()
							.getPenaComplessiva().getImportoMulta());
					lPenMod.setCodTipoRito("-");
					lPenMod.setFlagPenaInContinuazione("N");
					lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lPenMod.setDataInserimento(DateUtils.getSysDate());
					lPenMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lPenDao.setDAOFromModel(lPenMod);
					lSequence = lPenDao.insert();
				}
			}

			// stato_procedimento prendo l'ultimo statoprocedimento della lista (size-1)
			StatoProcedimentoDAO lStaDao = null;
			lStaDao = new StatoProcedimentoDAO(lConn);
			StatoProcedimentoModel lStaMod = (StatoProcedimentoModel) aDettaglioFascicolo
					.getStatoProcedimento().get(aDettaglioFascicolo.getStatoProcedimento().size() - 1);
			lStaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lStaMod.setProgressivo(new BigDecimal(1));
			lStaMod.setCodStatoProcedimento("0109"); // validato
			lStaMod.setData(null);
			lStaDao.setDAOFromModel(lStaMod);
			lStaDao.insert();

			// residenza se sono vuoti???
			if (aDettaglioFascicolo.getResidenza() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getResidenza();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				lSequence = lResDao.insert();

				// collego la residenza al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lSequence);
				lResFasDao.setDAOFromModel(lResFasMod);
				lSequence = lResFasDao.insert();
			}

			// duplico domicilio collegandolo al nuovo soggetto
			if (aDettaglioFascicolo.getDomicilio() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getDomicilio();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				lSequence = lResDao.insert();

				// collego il domicilio al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lSequence);
				lResFasDao.setDAOFromModel(lResFasMod);
				lSequence = lResFasDao.insert();
			}
			/*
			 * // duplico posizione giuridica collegandolo al nuovo fascicolo if
			 * (aDettaglioFascicolo.getPosizioneGiuridica() != null){ lPosDao = new
			 * PosizioneGiuridicaDAO(lConn); PosizioneGiuridicaModel lPosMod =
			 * aDettaglioFascicolo.getPosizioneGiuridica();
			 * lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			 * lPosDao.setDAOFromModel(lPosMod ); lSequence = lPosDao.insert(); }
			 */
			// creo posizione giuridica a libero
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setCodPosizioneGiuridica("07");
			lPosMod.setCodPosizioneProcessuale("-");
			lPosMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lPosMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lPosMod.setDataInserimento(DateUtils.getSysDate());
			lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lPosDao.setDAOFromModel(lPosMod);
			lSequence = lPosDao.insert();

			// duplico magistrato collegandolo al nuovo fascicolo
			if (aDettaglioFascicolo.getMagistratoCompetente() != null) {
				if (aDettaglioFascicolo.getMagistratoCompetente().getMagistratoCompetente() != null) {
					lMagComDao = new MagistratoCompetenteDAO(lConn);

					MagistratoCompetenteMagistratoModel lMagistrato = aDettaglioFascicolo
							.getMagistratoCompetente();
					MagistratoCompetenteModel lMagMod = lMagistrato.getMagistratoCompetente();

					lMagMod = lMagistrato.getMagistratoCompetente();
					lMagMod.setMagCodMagistrato(lMagistrato.getMagistrato().getCodMagistrato());
					lMagMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lMagComDao.setDAOFromModel(lMagMod);
					lMagComDao.insert();
				}
			}

			// Insert Reato e le circostanze sempre e solo da tabella REATO
			if (aDettaglioFascicolo.getReatiCircostanze() != null) {
				// prendo la lista dei reati e la metto nel model comune
				lReaDao = new ReatoDAO(lConn);
				List lLisReaCir = aDettaglioFascicolo.getReatiCircostanze();
				ReatoModel lReaMod = new ReatoModel();

				// ciclo su questa lista
				for (int i = 0; i <= lLisReaCir.size() - 1; i++) {
					// prendo il reato per scriverlo
					lReaMod = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getReato();
					// scrivo reato
					lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lReaDao.setDAOFromModel(lReaMod);
					lSequence = lReaDao.insert();

					// prendo la lista delle circostanze affogate nella tabella reati
					ReatoModel[] lLisCir = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getCircostanze();
					// Ciclo sulle n° circostanze afffogate nella tabella REATI
					for (int j = 0; j <= lLisCir.length - 1; j++) {
						// prendo la circostanza per scriverla
						lReaMod = lLisCir[j];
						// scrivo la circostanza
						lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
						lReaDao.setDAOFromModel(lReaMod);
						lSequence = lReaDao.insert();
					}
				}
			}

			// circostanze
			if (aDettaglioFascicolo.getCircostanze() != null) {
				CircostanzaModel lCirMod = new CircostanzaModel();
				List lLisCir = aDettaglioFascicolo.getCircostanze();
				lCirDao = new CircostanzaDAO(lConn);
				for (int i = 0; i <= lLisCir.size() - 1; i++) {
					lCirMod = (CircostanzaModel) lLisCir.get(i);
					lCirMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lCirDao.setDAOFromModel(lCirMod);
					lSequence = lCirDao.insert();
				}
			}
			commit(lConn);

			lRicMod = new RichiestaRemissioneModel(aRichiestaRemissione);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			// lRicMod.setIdRichiestaRemissione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RichiestaRemissioneController.ExInserisciRichiestaRemissionedaClasseI: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lEveDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lRicDao);
			cleanup(lResDao);
			cleanup(lResFasDao);
			cleanup(lPosDao);
			cleanup(lMagComDao);
			cleanup(lReaDao);
			cleanup(lCirDao);
			cleanup(lPenDao);
			cleanup(lSanDao);
			cleanup(lConn);
		}
		return lRicMod;
	}

	/**
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaRemissioneModel ExRicercaRichiestaRemissioneByIdEvento(BigDecimal aIdEvento)
			throws F3BException {

		Connection lConn = null;
		RichiestaRemissioneModel lRichiestaRemissioneMod = new RichiestaRemissioneModel();
		RichiestaRemissioneSqlDAO lRichiestaRemissioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaRemissioneSqlDao = new RichiestaRemissioneSqlDAO(lConn);
			lRichiestaRemissioneSqlDao.ricercaRichiestaRemissioneByEvento(aIdEvento);
			lRichiestaRemissioneMod = (RichiestaRemissioneModel) lRichiestaRemissioneSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaRemissioneController.ExRicercaRichiestaRemissioneByEvento: " + daoEx);
		} finally {
			cleanup(lRichiestaRemissioneSqlDao);
			cleanup(lConn);
		}

		return lRichiestaRemissioneMod;
	}

	/**
	 * Effettua la ricerca dei dati RichiestaRemissione usando il RichiestaRemissioneSqlDAO. Creato per
	 * puntare alle Richieste riferite a un fascicolo SIUS.
	 *
	 * @param aRichiestaRemissione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiesteRemissioneDebito(RichiestaRemissioneModel aRichiestaRemissione)
			throws F3BException {

		Connection lConn = null;
		Vector lRichiestaRemissioni = new Vector();
		RichiestaRemissioneSqlDAO lRicSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestaRemissioneSqlDAO(lConn);
			lRicSqlDao.ricercaRichiestaRemissione(aRichiestaRemissione);
			lRichiestaRemissioni = new Vector(lRicSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaRemissioneController.ExRicercaRichiesteRemissioneDebito: " + daoEx);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + Ex);
			throw new F3BException("RichiestaRemissioneController.ExRicercaRichiesteRemissioneDebito: " + Ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}
		return lRichiestaRemissioni;
	}

	public RichiestaRemissioneModel ExInserisciRichiestaEvento(RichiestaRemissioneModel aRichiestaRemissione,
			EventoNotificaModel aEvento, PenaResiduaModel aPenaResidua, String StatoPro) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenDao = null;
		RichiestaRemissioneDAO lRicDao = null;
		StatoProcedimentoDAO lStatoDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento.getEvento());
			BigDecimal lKeyEvento = lEveDao.insert();

			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);

			lEveRet.getEvento().setIdEvento(lKeyEvento);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvento != null && aEvento.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");

				while (count < aEvento.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);

					if (aEvento.getNotifiche()[count] != null) {

						if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
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

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserito evento" + lKeyEvento);
					}
					count++;
				}
			}

			lPenDao = new PenaResiduaDAO(lConn);
			// Inserisce Pena Residua
			PenaResiduaModel lPenMod = aPenaResidua;
			if (lPenMod != null) {
				lPenMod.setIdPenaResidua(null);
				lPenMod.setCodOperatoreAggiornamento(null);
				lPenMod.setDataAggiornamento(null);
				lPenMod.setCodUfficioAggiornamento(null);

				lPenMod.setFlagValidato("S");
				lPenMod.setEveIdEvento(lKeyEvento);
				lPenMod.setCodOperatoreInserimento(aEvento.getEvento().getCodOperatoreInserimento());
				lPenMod.setDataInserimento(DateUtils.getSysDate());
				lPenMod.setCodUfficioInserimento(aEvento.getEvento().getCodUfficioInserimento());

				lPenDao.setDAOFromModel(lPenMod);
				lPenDao.insert();
				lPenDao.stop();
			}

			lRicDao = new RichiestaRemissioneDAO(lConn);
			aRichiestaRemissione.setEveIdEvento(lKeyEvento);
			lRicDao.setDAOFromModel(aRichiestaRemissione);
			BigDecimal lSequence = lRicDao.insert();
			aRichiestaRemissione.setIdRichiestaRemissione(lSequence);

			if (StatoPro != null) {
				StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
				lStatoDao = new StatoProcedimentoDAO(lConn);
				// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
				lStatoDao.setCondizioneByIdFascicolo(lEveRet.getEvento().getFasSieIdFascicoloSiep());
				lStatoDao.delete();

				lStatoProcMod.setCodStatoProcedimento(StatoPro);
				lStatoProcMod.setFasSieIdFascicoloSiep(lEveRet.getEvento().getFasSieIdFascicoloSiep());
				lStatoProcMod.setCodOperatoreInserimento(lEveRet.getEvento().getCodOperatoreAggiornamento());
				lStatoProcMod.setDataInserimento(lEveRet.getEvento().getDataAggiornamento());
				lStatoProcMod.setCodUfficioInserimento(lEveRet.getEvento().getCodUfficioAggiornamento());
				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoProcMod.setData(lEveRet.getEvento().getDataEmissione());
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();
			}
			/*
			 * // Inserimento delle eventuali note aggiuntive. lCampoNotaDao = new CampoNotaDAO(lConn); if
			 * (aEvento.getCampoNote() != null) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
			 * istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Inserimento Eventuali Note Aggiuntive Numero note Aggiuntive : " +
			 * aEvento.getCampoNote().length); count = 0; while (count < aEvento.getCampoNote().length) {
			 * aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
			 * aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
			 * lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]); lCampoNotaDao.insert();
			 * lCampoNotaDao.stop();
			 *
			 * count++; } }
			 */

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenDao);
			cleanup(lRicDao);
			cleanup(lStatoDao);

			cleanup(lConn);
		}

		return aRichiestaRemissione;
	}

	/**
	 * Inserisci i records di Richiesta Remissione JMS senza assegnare la sequence
	 *
	 * @param aRichiesteRemissione
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciRichiesteRemissioniWithoutSequence(ArrayList aRichiesteRemissione,
			Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		RichiestaRemissioneDAO lRicConDao = null;
		RichiestaRemissioneModel lRicConMod = null;

		try {
			lRicConDao = new RichiestaRemissioneDAO(lConn);

			if (aRichiesteRemissione != null && aRichiesteRemissione.size() > 0) {
				for (int i = 0; i < aRichiesteRemissione.size(); i++) {
					lRicConMod = (RichiestaRemissioneModel) aRichiesteRemissione.get(i);
					if (lRicConMod != null) {
						if (lRicConMod.getIdRichiestaRemissione() != null) {
							lRicConDao.setDAOFromModel(lRicConMod);
							lRicConDao.setWithoutSequence(true);
							lRicConDao.insert();
							lRicConDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Richiesta Remissione gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la Richiesta Remissione! ");
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"SanzioneSostitutivaController.ExInserisciRichiesteRemissioneWithoutSequence: " + e);
		} finally {
			cleanup(lRicConDao);
		}
		return lCodEsito;
	}

}