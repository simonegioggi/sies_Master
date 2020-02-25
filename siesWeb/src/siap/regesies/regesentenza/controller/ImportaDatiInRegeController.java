package siap.regesies.regesentenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.regesies.regeavvocato.dao.RegeAvvocatoDAO;
import siap.regesies.regeavvocato.model.RegeAvvocatoModel;
import siap.regesies.regecircostanza.dao.RegeCircostanzaDAO;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import siap.regesies.regenotiziareato.dao.RegeNotiziaReatoDAO;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import siap.regesies.regereato.dao.RegeReatoDAO;
import siap.regesies.regereato.model.RegeReatoCircostanzaModel;
import siap.regesies.regeresidenza.dao.RegeResidenzaDAO;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import siap.regesies.regesentenza.action.ICostantiRegeSentenza;
import siap.regesies.regesentenza.dao.RegeSentenzaDAO;
import siap.regesies.regesentenza.model.EsitoImportModel;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesentenza.model.ProvvedimentoSiepModel;
import siap.regesies.regesoggetto.dao.RegeSoggettoDAO;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.SIEPException;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notefascicolo.dao.NoteFascicoloDAO;
import siap.siep.notefascicolo.model.NoteFascicoloModel;
import siap.siep.notiziareato.dao.NotiziaReatoDAO;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ImportaDatiInRegeController
 * </p>
 * <p>
 * Description: Classe di Utilità per l'import in Siep dei dati Rege
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImportaDatiInRegeController extends SiapController implements IImportaDati {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private EsitoImportModel mEsito = null;
	private String mUtente = "";
	private String mUfficio = "";
	private Date mData = null;

	public EsitoImportModel getEsito() {
		return mEsito;
	}

	/**
	 * Importa Provvedimento Rege
	 * 
	 * @param aRegeProvvedimento
	 * @return ProvvedimentoModel
	 * @throws F3BException
	 */
	public BigDecimal ExImportaProvvedimentoRege(EsitoImportModel aEsito) throws F3BException {
		Connection lConn = null;

		BigDecimal lKeyFascicolo = null;

		SoggettoDAO lSoggDAO = null;
		SentenzaDAO lSentDAO = null;
		SentenzaModel lSentenza = null;
		SoggettoModel lSoggetto = null;

		ProvvedimentoModel lRegeProvvedimento = aEsito.getProvvedimento();
		ProvvedimentoSiepModel lProvv = new ProvvedimentoSiepModel();

		FascicoloSiepModel lFascicolo = lRegeProvvedimento.getFascicoloSiep();
		boolean isInserisciSentenzaNuova = true;
		boolean isInserisciSoggettoNuovo = true;
		try {
			mEsito = new EsitoImportModel(aEsito);
			mUtente = lRegeProvvedimento.getUtente();
			mUfficio = lRegeProvvedimento.getUfficio();
			mData = lRegeProvvedimento.getDataInserimento();

			// Se esiste la sentenza non si inserisce da Rege
			if (lRegeProvvedimento.getSentenza() != null) {
				lSentenza = lRegeProvvedimento.getSentenza();
				isInserisciSentenzaNuova = false;
			} else
				lSentenza = lRegeProvvedimento.getRegeSentenza().toSentenza();

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("SENTENZA da inserire = " + lSentenza);
			if (lRegeProvvedimento.getIdSoggettoOmonimo() != null)
				isInserisciSoggettoNuovo = false;

			lConn = getDBTransaction();
			BigDecimal lIdSoggetto = null;
			try {
				if (isInserisciSoggettoNuovo) {
					lSoggetto = lRegeProvvedimento.getRegeSoggetto().toSoggettoModel();
					lSoggetto.setFlagPresenzaFascicolo("S");
					// ----Inserimento Soggetto------
					lSoggDAO = new SoggettoDAO(lConn);
					lSoggDAO.setDAOFromModel(lSoggetto);
					lIdSoggetto = lSoggDAO.insert();
					mEsito.setEsitoSoggetto(ICostantiRegeSentenza.ESITO_POSITIVO);
					lSoggetto.setIdSoggetto(lIdSoggetto);
					lProvv.setSoggetto(lSoggetto);
				} else {
					lIdSoggetto = lRegeProvvedimento.getIdSoggettoOmonimo();
					mEsito.setEsitoSoggetto(ICostantiRegeSentenza.ESITO_DATO_PRESENTE);
				}
			} catch (Exception ex) {
				mEsito.setEsitoSoggetto(ex.getMessage());
			}
			BigDecimal lIdSentenza = null;
			// Inserimento Sentenza -- se già c'e non deve essere chiamato questo metodo
			try {
				if (isInserisciSentenzaNuova) {
					lSentDAO = new SentenzaDAO(lConn);
					lSentDAO.setDAOFromModel(lSentenza);
					lIdSentenza = lSentDAO.insert();
					mEsito.setEsitoSentenza(ICostantiRegeSentenza.ESITO_POSITIVO);
					lSentenza.setIdSentenza(lIdSentenza);
					lProvv.setSentenza(lSentenza);
				} else {
					lIdSentenza = lSentenza.getIdSentenza();
					mEsito.setEsitoSentenza(ICostantiRegeSentenza.ESITO_DATO_PRESENTE);
					lProvv.setSentenza(lSentenza);
				}
			} catch (Exception ex) {
				mEsito.setEsitoSentenza(ex.getMessage());
			}

			// ---------- Inserimento Fascicolo Siep -----------
			lFascicolo.setSenIdSentenza(lIdSentenza);
			lFascicolo.setSogIdSoggetto(lIdSoggetto);

			lFascicolo = this.inserisciFascicoloSiep(lConn, lFascicolo);
			lProvv.setFascicoloSiep(lFascicolo);

			lKeyFascicolo = lFascicolo.getIdFascicoloSiep();

			// --- Inserimento entità collegate al fascicolo
			if (lRegeProvvedimento.isResidenzeCheck()) { // Ci sono da inserire le Residenze
				Vector lResidenze = this.inserisciResidenza(lConn, lRegeProvvedimento.getResidenze(),
						lKeyFascicolo, lIdSoggetto);
				lProvv.setResidenze(lResidenze);
			}
			if (lRegeProvvedimento.isReatoCheck()) { // Ci sono da inserire i reati
				Vector lReati = this.inserisciReati(lConn, lRegeProvvedimento.getReati(), lKeyFascicolo);
				lProvv.setReati(lReati);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(lRegeProvvedimento);
			if (lRegeProvvedimento.isCircostanzaCheck()) { // Ci sono da inserire circostanze
				Vector lCirc = this.inserisciCircostanze(lConn, lRegeProvvedimento.getCircostanze(),
						lKeyFascicolo);
				lProvv.setCircostanze(lCirc);
			}
			if (lRegeProvvedimento.isNotiziaReatoCheck()) { // Ci sono da inserire le Notizie di reato
				Vector lNotizie = this.inserisciNotizieReato(lConn, lRegeProvvedimento.getNotizieDiReato(),
						lKeyFascicolo);
				lProvv.setNotizieDiReato(lNotizie);
			}
			if (lRegeProvvedimento.isDispositivoCheck() || lRegeProvvedimento.isDifensoriCheck()) { // C'e' da
																									// inserire
																									// il
																									// dispositivo
																									// o i
																									// difensori
				NoteFascicoloModel lDispositivo = this.inserisciDispositivoDifensori(lConn,
						lRegeProvvedimento, lKeyFascicolo);
				lProvv.setNoteFascicolo(lDispositivo);
			}

			mEsito.setProvvedimentoSiep(lProvv);

			if (deleteDatiRege(aEsito.getProvvedimento().getRegeSentenza().getIdFile(), lConn))
				commit(lConn);
			else { // la cancellazione dei dati rege è andata male
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Si è verificato un errore durante la cancellazione dei dati Rege!");
				mEsito.setEsitoCancellazioneRege("Si è verificato un errore durante la cancellazione dei dati Rege!");
				// throw new F3BException("Si è verificato un errore durante la cancellazione dei dati Rege");
			}
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe, sqe);
			mEsito.setEsitoFascicolo(sqe.getMessage());
			// throw new F3BException("ImportaDatiInRegeController.ExImportaProvvedimentoRege:" + sqe);
		} finally {
			cleanup(lSoggDAO);
			cleanup(lSentDAO);

			cleanup(lConn);
		}
		return lKeyFascicolo;
	}

	/**
	 * Integrazione di un fascicolo esistente con i dati selezionati da Rege
	 * 
	 * @param aRegeProvvedimento
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExIntegraFascicoloSiep(EsitoImportModel aEsito) throws F3BException {
		Connection lConn = null;
		ProvvedimentoModel lRegeProvvedimento = aEsito.getProvvedimento();

		BigDecimal lKeyFascicolo = lRegeProvvedimento.getFascicoloSiep().getIdFascicoloSiep();
		BigDecimal lIdSoggetto = lRegeProvvedimento.getSoggetto().getIdSoggetto();
		ProvvedimentoSiepModel lProvv = new ProvvedimentoSiepModel();

		lProvv.setSentenza(lRegeProvvedimento.getSentenza());

		lProvv.setFascicoloSiep(lRegeProvvedimento.getFascicoloSiep());
		lProvv.setSoggetto(lRegeProvvedimento.getSoggetto());
		try {
			lConn = getDBTransaction();

			mEsito = new EsitoImportModel(aEsito);

			mEsito.setEsitoSentenza(ICostantiRegeSentenza.ESITO_POSITIVO);
			mEsito.setEsitoSoggetto(ICostantiRegeSentenza.ESITO_POSITIVO);
			mEsito.setEsitoFascicolo(ICostantiRegeSentenza.ESITO_POSITIVO);

			// --- Inserimento entità collegate al fascicolo
			if (lRegeProvvedimento.isResidenzeCheck()) { // Ci sono da inserire le Residenze
				Vector lResidenze = this.inserisciResidenza(lConn, lRegeProvvedimento.getResidenze(),
						lKeyFascicolo, lIdSoggetto);
				lProvv.setResidenze(lResidenze);
			}
			if (lRegeProvvedimento.isReatoCheck()) { // Ci sono da inserire i reati
				Vector lReati = this.inserisciReati(lConn, lRegeProvvedimento.getReati(), lKeyFascicolo);
				lProvv.setReati(lReati);
			}
			if (lRegeProvvedimento.isCircostanzaCheck()) { // Ci sono da inserire circostanze
				Vector lCirc = this.inserisciCircostanze(lConn, lRegeProvvedimento.getCircostanze(),
						lKeyFascicolo);
				lProvv.setCircostanze(lCirc);
			}
			if (lRegeProvvedimento.isNotiziaReatoCheck()) { // Ci sono da inserire le Notizie di reato
				Vector lNotizie = this.inserisciNotizieReato(lConn, lRegeProvvedimento.getNotizieDiReato(),
						lKeyFascicolo);
				lProvv.setNotizieDiReato(lNotizie);
			}
			if (lRegeProvvedimento.isDispositivoCheck() || lRegeProvvedimento.isDifensoriCheck()) { // C'e' da
																									// inserire
																									// il
																									// dispositivo
																									// o i
																									// difensori
				NoteFascicoloModel lDispositivo = this.inserisciDispositivoDifensori(lConn,
						lRegeProvvedimento, lKeyFascicolo);
				lProvv.setNoteFascicolo(lDispositivo);
			}
			mEsito.setProvvedimentoSiep(lProvv);
			if (deleteDatiRege(aEsito.getProvvedimento().getRegeSentenza().getIdFile(), lConn))
				commit(lConn);
			else
				throw new F3BException("Si è verificato un errore durante la cancellazione dei dati Rege");
		} catch (F3BException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("F3BException: " + sqe, sqe);
			// throw new F3BException("ImportaDatiInRegeController.ExImportaProvvedimentoRege:" + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe, sqe);
			mEsito.setEsitoFascicolo(sqe.getMessage());
		} finally {
			cleanup(lConn);
		}
		return lKeyFascicolo;
	}

	/**
	 * Inserimento del Fascicolo Siep
	 * 
	 * @param lConn
	 * @param aFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	private FascicoloSiepModel inserisciFascicoloSiep(Connection lConn, FascicoloSiepModel aFascicoloSiep)
			throws F3BException {

		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		try {
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
//			BigDecimal chiaveProgrManuale = null;
//			int annoCorrente = 0;

			// Cerco il Progressivo rispettivamente al tipo progressivo impostato
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
			} else
				aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));

			lFasDao.setDAOFromModel(aFascicoloSiep);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(aFascicoloSiep.toString());
			BigDecimal lChiave = lFasDao.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("**** Key Fascicolo = " + lChiave);

			aFascicoloSiep.setIdFascicoloSiep(lChiave);

			StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			lStat.setFasSieIdFascicoloSiep(lChiave);
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setDataInserimento(mData);
			lStat.setCodUfficioInserimento(mUfficio);
			lStat.setCodOperatoreInserimento(mUtente);
			lStat.setCodStatoProcedimento("0108"); // Iscritto
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();

			mEsito.setEsitoFascicolo(ICostantiRegeSentenza.ESITO_POSITIVO);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			if (ex.getMessage().indexOf("FAS_SIE_SEN_SOG_FK_I") != -1)
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Impossibile inserire il fascicolo! Esiste un fascicolo per la sentenza ed il soggetto selezionato");

			if (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") != -1)
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Impossibile inserire il fascicolo! Esiste un fascicolo per lo stesso numero SIEP");

			throw new SIEPException("ImportaDatiInRegeController.inserisciFascicoloSiep: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			throw new F3BException("ImportaDatiInRegeController.inserisciFascicoloSiep: " + sqe);
		} finally {
			cleanup(lFasDaoSql);
			cleanup(lFasDao);
			cleanup(lStatoProcDao);
		}

		return aFascicoloSiep;
	}

	/**
	 * Inserimento delle residenza legarta al Provveidmento Rege
	 * 
	 * @param lConn
	 * @param aResidenza
	 * @return
	 * @throws F3BException
	 */
	private Vector inserisciResidenza(Connection lConn, Vector aResidenze, BigDecimal lKeyFascicolo,
			BigDecimal lSogIdSoggetto) throws F3BException {
		ResidenzaDAO lResDao = null;
		ResidenzaModel lResidenza = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		Vector lResidenze = new Vector();

		try {
			Iterator lItx = aResidenze.iterator();
			while (lItx.hasNext()) {
				RegeResidenzaModel lRegeRes = (RegeResidenzaModel) lItx.next();

				lResidenza = lRegeRes.toResidenza();
				lResidenza.setSogIdSoggetto(lSogIdSoggetto);

				lResidenza.setCodOperatoreInserimento(mUtente);
				lResidenza.setDataInserimento(mData);
				lResidenza.setCodUfficioInserimento(mUfficio);

				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModel(lResidenza);
				BigDecimal lIdRes = lResDao.insert();
				lResDao.stop();

				lResidenza.setIdResidenza(lIdRes);

				lResidenze.add(lResidenza);

				if (lResidenza != null) {
					ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel(new Date(),
							null, lIdRes, lKeyFascicolo);

					lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
					lResFasDao.setDAOFromModel(lResFasMod);
					lResFasDao.insert();
					lResFasDao.stop();
				}
				lEsiti.add(ICostantiRegeSentenza.ESITO_POSITIVO);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			lEsiti.add(ex.getMessage());
			throw new SIEPException("ImportaDatiInRegeController.inserisciResidenza: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			lEsiti.add(sqe.getMessage());
			throw new F3BException("ImportaDatiInRegeController.inserisciResidenza: " + sqe);
		} finally {
			cleanup(lResDao);
			cleanup(lResFasDao);
			mEsito.setEsitoResidenze(lEsiti);
		}

		return lResidenze;
	}

	/**
	 * inserimento dei Reati
	 * 
	 * @param lConn
	 * @param aReati
	 * @param lKeyFascicolo
	 * @throws F3BException
	 */
	private Vector inserisciReati(Connection lConn, Vector aReati, BigDecimal lKeyFascicolo)
			throws F3BException {
		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSiepDao = null;
		ReatoCircostanzaModel lReaPrincipale = null;
		Vector lReati = new Vector();
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		try {
			if (aReati != null && aReati.size() > 0) {
				// Ci sono dei reati da inserire
				// COntrollo che non ci siano gia' dei reati nel fascicolo da importare
//				Vector lReatiSiep = new Vector();

				lReaSiepDao = new ReatoSqlDAO(lConn);
				int lProgrReatoEsistente = lReaSiepDao.getMaxProgrReato(lKeyFascicolo);
				int lContatoreProgressivo = 0;

				if (lProgrReatoEsistente != 0)
					lContatoreProgressivo = lProgrReatoEsistente + 1;

				// Inserisco i Reati
				Iterator lItx = aReati.iterator();

				while (lItx.hasNext()) {

					RegeReatoCircostanzaModel lRegeReatoCirc = (RegeReatoCircostanzaModel) lItx.next();

					lReaPrincipale = new ReatoCircostanzaModel(
							(ReatoCircostanzaModel) lRegeReatoCirc.toReatoCircostanze());
					if (lReaPrincipale != null && lReaPrincipale.getReato() != null) {
						lReaPrincipale.getReato().setFasSieIdFascicoloSiep(lKeyFascicolo);
						// Inserimento primo Reato
						lReaDao = new ReatoDAO(lConn);
						ReatoModel lPrincipale = lReaPrincipale.getReato();
						lPrincipale.setCodOperatoreInserimento(mUtente);
						lPrincipale.setDataInserimento(mData);
						lPrincipale.setCodUfficioInserimento(mUfficio);
						if (lContatoreProgressivo != 0) {
							lPrincipale.setProgrReato(new BigDecimal(lContatoreProgressivo));
							lContatoreProgressivo++;
						}

						lReaDao.setDAOFromModel(lPrincipale);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lReaPrincipale.getReato().setIdReato(lKeyReato);
						lEsiti.add(ICostantiRegeSentenza.ESITO_POSITIVO);

						// Inserimento successivi
						ReatoModel lReaMod = null;
//						BigDecimal lProgrCircostanza = null;

						Vector lCircostanze = null;
						// Circostanze
						if (lReaPrincipale.getCircostanze() != null) {
							for (int i = 0; i < lReaPrincipale.getCircostanze().length; i++) {
								if (lReaPrincipale.getCircostanze()[i] != null) {
									lCircostanze = new Vector();
									lReaMod = new ReatoModel();

									lReaMod = (ReatoModel) lReaPrincipale.getCircostanze()[i];
									lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
									lReaMod.setCodOperatoreInserimento(mUtente);
									lReaMod.setDataInserimento(mData);
									lReaMod.setCodUfficioInserimento(mUfficio);

									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di mLog
									siesLogger
											.debug("-------------------------------------------------------------");
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di mLog
									siesLogger.debug(lReaMod);
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di mLog
									siesLogger
											.debug("-------------------------------------------------------------");

									// Gestione Progressivo Circostanza
									lReaDao.setDAOFromModel(lReaMod);
									BigDecimal lKeyCirc = lReaDao.insert();
									lReaDao.stop();
									lReaMod.setIdReato(lKeyCirc);
									lCircostanze.add(lReaMod);

									lEsiti.add(ICostantiRegeSentenza.ESITO_POSITIVO);
								}
							}
						}
						lReati.add(lReaPrincipale);
					}
				}
			}
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			lEsiti.add(ex.getMessage());
			throw new SIEPException("ImportaDatiInRegeController.inserisciReati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			lEsiti.add(sqe.getMessage());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ImportaDatiInRegeController.inserisciReati: ", sqe);
			throw new F3BException("ImportaDatiInRegeController.inserisciReati: " + sqe);
		} finally {
			cleanup(lReaDao);
			mEsito.setEsitoReati(lEsiti);
		}
		return lReati;
	}

	/**
	 * Inserimento delle Circostanze
	 * 
	 * @param lConn
	 * @param aCircostanze
	 * @param lKeyFascicolo
	 * @throws F3BException
	 */
	private Vector inserisciCircostanze(Connection lConn, Vector aCircostanze, BigDecimal lKeyFascicolo)
			throws F3BException {
		CircostanzaDAO lCirDao = null;
		CircostanzaModel lCirMod = null;
		Vector lCircostanze = new Vector();
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> Sto inserendo le circostanze! <<<");
			lCirDao = new CircostanzaDAO(lConn);

			Iterator lItx = aCircostanze.iterator();

			while (lItx.hasNext()) {
				RegeCircostanzaModel lRegeCirc = (RegeCircostanzaModel) lItx.next();

				lCirMod = lRegeCirc.toCircostanza();
				lCirMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
				lCirMod.setCodOperatoreInserimento(mUtente);
				lCirMod.setDataInserimento(mData);
				lCirMod.setCodUfficioInserimento(mUfficio);

				lCirDao.setDAOFromModel(lCirMod);
				BigDecimal lKey = null;
				lKey = lCirDao.insert();
				lCirMod.setIdCircostanza(lKey);
				lCirDao.stop();
				lCircostanze.add(lCirMod);

				lEsiti.add(ICostantiRegeSentenza.ESITO_POSITIVO);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			lEsiti.add(ex.getMessage());
			throw new F3BException("ImportaDatiInRegeController.inserisciCircostanze: Non posso inserire: "
					+ ex);
		} catch (Exception sqe) {
			rollback(lConn);
			lEsiti.add(sqe.getMessage());
			throw new F3BException("ImportaDatiInRegeController.inserisciCircostanze: Non posso inserire : "
					+ sqe);
		} finally {
			cleanup(lCirDao);
			mEsito.setEsitoCircostanze(lEsiti);
		}
		return lCircostanze;
	}

	/**
	 * Inserimento delle Notizia Reato
	 * 
	 * @param lConn
	 * @param aNotiziaReato
	 * @param lKeyFascicolo
	 * @throws F3BException
	 */
	private Vector inserisciNotizieReato(Connection lConn, Vector aNotiziaReato, BigDecimal lKeyFascicolo)
			throws F3BException {
		NotiziaReatoDAO lNotDao = null;
		NotiziaReatoModel lNotMod = null;
		Vector lNotizie = new Vector();
		// Esito degli inserimenti
		Vector lEsiti = new Vector();

		try {

			lNotDao = new NotiziaReatoDAO(lConn);
			Iterator lItx = aNotiziaReato.iterator();

			while (lItx.hasNext()) {
				RegeNotiziaReatoModel lRegeNot = (RegeNotiziaReatoModel) lItx.next();

				lNotMod = lRegeNot.toNotiziaReato();
				lNotMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
				lNotMod.setCodOperatoreInserimento(mUtente);
				lNotMod.setDataInserimento(mData);
				lNotMod.setCodUfficioInserimento(mUfficio);

				lNotDao.setDAOFromModel(lNotMod);
				BigDecimal lKey = null;
				lKey = lNotDao.insert();
				lNotDao.stop();
				lNotMod.setIdNotiziaReato(lKey);
				lEsiti.add(ICostantiRegeSentenza.ESITO_POSITIVO);
				lNotizie.add(lNotMod);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			lEsiti.add(ex.getMessage());
			throw new F3BException("ImportaDatiInRegeController.inserisciNotiziaReato: Non posso inserire: "
					+ ex);
		} finally {
			cleanup(lNotDao);
			mEsito.setEsitoNotizieDiReato(lEsiti);
		}
		return lNotizie;
	}

	/**
	 *
	 * @param lConn
	 * @param aNotiziaReato
	 * @param lKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	private NoteFascicoloModel inserisciDispositivoDifensori(Connection lConn, ProvvedimentoModel aProvv,
			BigDecimal lKeyFascicolo) throws F3BException {
		NoteFascicoloDAO lNotDao = null;
		NoteFascicoloModel lNotMod = null;
		String lEsitoDispositivo = "";
		String lEsitoAvvocati = "";
		String lTag = "<font class=\"campo\">";
		String lCloseTag = "</font>";

		try {
			lNotMod = new NoteFascicoloModel();
			lNotDao = new NoteFascicoloDAO(lConn);

			if (aProvv.isDispositivoCheck()) {
				lNotMod.setNotaDispositivo(aProvv.getRegeSentenza().getNotaDispositivo());
				lEsitoDispositivo = ICostantiRegeSentenza.ESITO_POSITIVO;
			}

			if (aProvv.isDifensoriCheck()) { // Componet una stringa con gli avvocati associati
				String lAvvocati = "";
				Iterator lItx = aProvv.getDifensori().iterator();
				int lCont = 0;
				while (lItx.hasNext()) {
					if (lCont == 1)
						lAvvocati += "<br>";
					RegeAvvocatoModel lAvvMod = (RegeAvvocatoModel) lItx.next();

					lAvvocati += "Avvocato " + lTag + lAvvMod.getDescrTipoAvvocato() + " "
							+ lAvvMod.getCognome() + " " + lAvvMod.getNome() + lCloseTag;
					if (lAvvMod.getForo() != null && lAvvMod.getForo().length() > 1)
						lAvvocati += " del foro di " + lTag + lAvvMod.getForo() + lCloseTag;

					lCont++;
					// vado a capo
				}
				lNotMod.setNotaAvvocati(lAvvocati);
				lEsitoAvvocati = ICostantiRegeSentenza.ESITO_POSITIVO;
			}

			lNotDao = new NoteFascicoloDAO(lConn);

			lNotMod.setFasIdFascicoloSiep(lKeyFascicolo);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("MODEL DISPOSITIVO NOTE" + lNotMod);
			lNotDao.setDAOFromModel(lNotMod);
			lNotDao.insert();
		} catch (DAOException ex) {
			if (ex.getMessage().startsWith("ORA-00001")) {
				lEsitoDispositivo = "Impossibile inserire - Dispositivo già presente!";
				lEsitoAvvocati = "Impossibile inserire - Nota Fascicolo già presente!";
			} else {
				lEsitoDispositivo = ex.getMessage();
				lEsitoAvvocati = ex.getMessage();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("ImportaDatiInRegeController.inserisciDispositivoDifensori= ", ex);
		} finally {
			cleanup(lNotDao);
			mEsito.setEsitoDispositivo(lEsitoDispositivo);
			mEsito.setEsitoDifensori(lEsitoAvvocati);
		}
		return lNotMod;
	}

	/**
	 * Fujnzione di eliminazione dei dati rege appena importati con successo in SIEP
	 * 
	 * @param idFile
	 * @param lConn
	 * @return vero se sono stai cancellati tutti correttamente - false altrimenti
	 * @throws F3BException
	 */
	private boolean deleteDatiRege(String idFile, Connection lConn) throws F3BException {
		RegeSentenzaDAO lSenDao = null;
		RegeSoggettoDAO lSoggDao = null;
		RegeCircostanzaDAO lCircDao = null;
		RegeReatoDAO lReaDao = null;
		RegeNotiziaReatoDAO lNotReaDao = null;
		RegeAvvocatoDAO lAvvDao = null;
		RegeResidenzaDAO lResDao = null;

		try {
			lSenDao = new RegeSentenzaDAO(lConn);
			lSenDao.setCondizioneDelete(idFile);
			lSenDao.delete();

			lSoggDao = new RegeSoggettoDAO(lConn);
			lSoggDao.setCondizioneDelete(idFile);
			lSoggDao.delete();

			lCircDao = new RegeCircostanzaDAO(lConn);
			lCircDao.setCondizioneDelete(idFile);
			lCircDao.delete();

			lReaDao = new RegeReatoDAO(lConn);
			lReaDao.setCondizioneDelete(idFile);
			lReaDao.delete();

			lNotReaDao = new RegeNotiziaReatoDAO(lConn);
			lNotReaDao.setCondizioneDelete(idFile);
			lNotReaDao.delete();

			lAvvDao = new RegeAvvocatoDAO(lConn);
			lAvvDao.setCondizioneDelete(idFile);
			lAvvDao.delete();

			lResDao = new RegeResidenzaDAO(lConn);
			lResDao.setCondizioneDelete(idFile);
			lResDao.delete();

			return true;

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Errore durante la cancellazione dei dati Rege", ex);
			return false;

		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Errore durante la cancellazione dei dati Rege", sqe);
			return false;
		} finally {
			cleanup(lSenDao);
			cleanup(lSoggDao);
			cleanup(lCircDao);
			cleanup(lReaDao);
			cleanup(lNotReaDao);
			cleanup(lAvvDao);
			cleanup(lResDao);
		}
	}

}