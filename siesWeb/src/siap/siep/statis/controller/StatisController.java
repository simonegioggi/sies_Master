package siap.siep.statis.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.dao.EventoSimeoneSqlDAO;
import siap.sico.evento.model.EventoFascicoloStatoModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.scadenzario.dao.ScadenzarioSoggettoSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.scadenzario.util.ScadenzarioUtils;
import siap.siep.statis.action.ICostantiStatis;
import siap.siep.statis.dao.IspAttivitaMagistratiSqlDAO;
import siap.siep.statis.dao.IspProvvedimentiDAO;
import siap.siep.statis.dao.IspProvvedimentiSqlDAO;
import siap.siep.statis.dao.IspTempiEmissioneSqlDAO;
import siap.siep.statis.dao.IspTempiIscrizioneDAO;
import siap.siep.statis.dao.IspTempiRicezioneDAO;
import siap.siep.statis.dao.IspTempiSqlDAO;
import siap.siep.statis.dao.StatRisSiesDAO;
import siap.siep.statis.dao.StatisStoreProcedureDAO;
import siap.siep.statis.dao.StatoFascicoloResDAO;
import siap.siep.statis.model.IspAttivitaMagistratiModel;
import siap.siep.statis.model.IspProvvedimentiModel;
import siap.siep.statis.model.IspTempiEmissioneModel;
import siap.siep.statis.model.IspTempiIscrizioneModel;
import siap.siep.statis.model.IspTempiModel;
import siap.siep.statis.model.IspTempiRicezioneModel;
import siap.siep.statis.model.StatRisSiesModel;
import siap.siep.statis.model.StatoFascicoloResModel;
import siap.siep.statistiche.model.RicercaFogliCompModel;
import siap.siep.statistiche.model.RiepilogoStatisticheFogliComplementari;
import siap.siep.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.impugnazione.dao.ImpugnazioneDAO;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.statistiche.action.ICostantiStatistiche;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.dao.IspMotivoOggettoDAO;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.IspConteggioOggettiModel;
import siap.sius.statistiche.model.IspConteggioRelatoriModel;
import siap.sius.statistiche.model.IspConteggioTempiModel;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import siap.sius.statistiche.model.IspMotivoOggettoModel;
import siap.sius.statistiche.model.IspProcIntervalliModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: StatisController
 * </p>
 * <p>
 * Description: Controller per le statistiche
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatisController extends GenericController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public Vector ExRicercaStatoFascicoloRes(StatoFascicoloResModel aStatoFascicoloRes) throws F3BException {

		Connection lConn = null;
		Vector lStatoFascicoloRei = new Vector();
		StatoFascicoloResDAO lStaDao = null;

		try {
			lConn = getDBConnection();
			lStaDao = new StatoFascicoloResDAO(lConn);
			// lStaDao.selCondizione(aStatoFascicoloRes);
			// lStaDao.setOrdinamento("COD_STATO_FASCICOLO");
			lStaDao.setOrdinamento("ORDINAMENTO");

			lStaDao.start();

			while (lStaDao.next()) {
				StatoFascicoloResModel lStatoModel = (StatoFascicoloResModel) lStaDao.getModel();
				if (!ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lStatoModel.getTipoCampo())
						&& !ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lStatoModel.getTipoCampo())) {
					lStatoFascicoloRei.add(lStatoModel);
				}
			}

			if (lStatoFascicoloRei.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExRicercaStatoFascicoloRes: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
		return lStatoFascicoloRei;
	}

	/**
	 * Chiama la stored procedure ISPETTORATO.stat_provvedimenti
	 *
	 * @param ufficio
	 *            Codice ufficio
	 * @param dataVerifica
	 *            Data in cui e' lanciata la procedura
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExStatProvvedimentiStoredProcedure(String ufficio, String uffAccorpa1, String uffAccorpa2,
			String uffAccorpa3, String dataVerifica) throws F3BException {

		Connection lConn = null;
		StatisStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisStoreProcedureDAO(lConn);
			lSpDao.setStatProvvedimentiStoreProcedure();
			lSpDao.setCodUfficioInserimento(ufficio);
			lSpDao.setDataVerifica(dataVerifica);
			// NGG
			lSpDao.setCodUfficioAccorpato_1(uffAccorpa1);
			lSpDao.setCodUfficioAccorpato_2(uffAccorpa2);
			lSpDao.setCodUfficioAccorpato_3(uffAccorpa3);
			//
			lSpDao.execute();

			lConn.commit();
		} catch (DAOException daoEx) {
			throw new F3BException("StatisController.ExStatProvvedimentiStoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("StatisController.ExStatProvvedimentiStoredProcedure: " + sqe);
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	/**
	 * Chiama la stored procedure ISPETTORATO.AttivitaMagistrati
	 *
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param ufficio
	 *            Codice ufficio
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExAttivitaMagistratiStoredProcedure(String dataIni, String dataFin, String ufficio)
			// String ufficio, String ufficioaccorpato1, String ufficioaccorpato2, String ufficioaccorpato3 )
			throws F3BException {

		Connection lConn = null;

		StatisStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisStoreProcedureDAO(lConn);

			lSpDao.setAttivitaMagistratiStoreProcedure();
			lSpDao.setDataInizio(dataIni);
			lSpDao.setDataFine(dataFin);
			lSpDao.setCodUfficioInserimento(ufficio);
			// NGG
			/*
			 * lSpDao.setCodUfficioAccorpato_1(ufficioaccorpato1);
			 * lSpDao.setCodUfficioAccorpato_2(ufficioaccorpato2);
			 * lSpDao.setCodUfficioAccorpato_3(ufficioaccorpato3);
			 */
			//
			lSpDao.execute();

			lConn.commit();
		} catch (DAOException daoEx) {
			throw new F3BException("StatisController.ExAttivitaMagistratiStoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("StatisController.ExAttivitaMagistratiStoredProcedure: " + sqe);
		} finally {
			cleanup(lSpDao);

			cleanup(lConn);
		}
	}

	/**
	 * Chiama la stored procedure ISPETTORATO.tempi_iscrizione
	 *
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param ufficio
	 *            Codice ufficio
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExTempiIscrizioneStoredProcedure(String dataIni, String dataFin, String ufficio)
			// , String ufficioaccorpato1, String ufficioaccorpato2, String ufficioaccorpato3)
			throws F3BException {

		Connection lConn = null;

		StatisStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisStoreProcedureDAO(lConn);

			lSpDao.setTempiIscrizioneStoreProcedure();
			lSpDao.setDataInizio(dataIni);
			lSpDao.setDataFine(dataFin);
			lSpDao.setCodUfficioInserimento(ufficio);
			// NGG
			// lSpDao.setCodUfficioAccorpato_1(ufficioaccorpato1);
			// lSpDao.setCodUfficioAccorpato_2(ufficioaccorpato2);
			// lSpDao.setCodUfficioAccorpato_3(ufficioaccorpato3);
			//
			lSpDao.execute();

			lConn.commit();
		} catch (DAOException daoEx) {
			throw new F3BException("StatisController.ExTempiIscrizioneStoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("StatisController.ExTempiIscrizioneStoredProcedure: " + sqe);
		} finally {
			cleanup(lSpDao);

			cleanup(lConn);
		}
	}

	/**
	 * Chiama la stored procedure ISPETTORATO.tempi_emissione
	 *
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param ufficio
	 *            Codice ufficio
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExTempiEmissioneStoredProcedure(String dataIni, String dataFin, String ufficio,
			String ufficioaccorpato1, String ufficioaccorpato2, String ufficioaccorpato3)
			throws F3BException {

		Connection lConn = null;

		StatisStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			// lSpDao = new StatProvvedimentiStoreProcedureDAO(lConn);
			lSpDao = new StatisStoreProcedureDAO(lConn);
			lSpDao.setTempiEmissioneStoreProcedure();
			lSpDao.setDataInizio(dataIni);
			lSpDao.setDataFine(dataFin);
			lSpDao.setCodUfficioInserimento(ufficio);
			// NGG
			lSpDao.setCodUfficioAccorpato_1(ufficioaccorpato1);
			lSpDao.setCodUfficioAccorpato_2(ufficioaccorpato2);
			lSpDao.setCodUfficioAccorpato_3(ufficioaccorpato3);

			lSpDao.execute();

			lConn.commit();
		} catch (DAOException daoEx) {
			throw new F3BException("StatisController.ExTempiEmissioneStoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("StatisController.ExTempiEmissioneStoredProcedure: " + sqe);
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaIspProvvedimenti(IspProvvedimentiModel aIspProvvedimenti) throws F3BException {

		Connection lConn = null;

		Vector lIspProvvedimenti = new Vector();
		IspProvvedimentiDAO lIspDao = null;

		try {
			lConn = getDBConnection();

			lIspDao = new IspProvvedimentiDAO(lConn);

			// lIspDao.selCondizione(aIspProvvedimenti);
			lIspDao.start();
			while (lIspDao.next()) {
				lIspProvvedimenti.add(lIspDao.getModel());
			}
			if (lIspProvvedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExRicercaIspProvvedimenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lIspProvvedimenti;
	}

	public Vector ExGetCountRiepilogoIspProvvedimenti(IspProvvedimentiModel aIspProvvedimenti)
			throws F3BException {

		Connection lConn = null;
		Vector lIspProvvedimenti = new Vector();
		IspProvvedimentiSqlDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspProvvedimentiSqlDAO(lConn);

			lIspDao.getCountRiepilogoIspProvvedimenti(aIspProvvedimenti);

			lIspDao.start();
			while (lIspDao.next()) {
				lIspProvvedimenti.add(lIspDao.getModelCountRiepilogo());
			}
			if (lIspProvvedimenti.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExGetCountRiepilogoIspProvvedimenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lIspProvvedimenti;
	}

	/**
	 *
	 * @param aCodici
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaDettaglioProcedimenti(String[] aCodici) throws F3BException {

		Connection lConn = null;
		Vector lIspProvvedimenti = new Vector();
		IspProvvedimentiSqlDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspProvvedimentiSqlDAO(lConn);

			lIspDao.RicercaDettaglioProcedimenti(aCodici);

			lIspDao.start();
			while (lIspDao.next()) {
				lIspProvvedimenti.add(lIspDao.getModel());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExRicercaDettaglioProcedimenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}

		return lIspProvvedimenti;
	}

	/**
	 *
	 * @param aStatVect
	 * @throws F3BException
	 */
	public void ExInserisciStatRisSies(Vector aStatVect) throws F3BException {

		Connection lConn = null;
		StatRisSiesDAO lStaDao = null;

		try {
			lConn = getDBConnection();

			Iterator itx = aStatVect.iterator();

			while (itx.hasNext()) {
				StatRisSiesModel lMod = (StatRisSiesModel) itx.next();

				// StatRisSiesModel aStatRisSies
				lStaDao = new StatRisSiesDAO(lConn);
				lStaDao.setDAOFromModel(lMod);
				lStaDao.insert();

			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("StatisController.ExInserisciStatRisSies: Non posso inserire: " + ex);
		} finally {
			cleanup(lStaDao);
			cleanup(lConn);
		}
	}

	/**
	 * Estrae i dati aggregati per anno e tipologia per creare il foglio di 'Riepoligo'
	 *
	 * @param aAnnoIni
	 * @param aAnnoFin
	 * @return Vector<IspAttivitaMagistratiModel>
	 * @throws F3BException
	 */
	public Vector<IspAttivitaMagistratiModel> ExRiepilogoGeneraleAttivita(int aAnnoIni, int aAnnoFin)
			throws F3BException {

		Connection lConn = null;
		Vector<IspAttivitaMagistratiModel> lVect = new Vector<>();
		IspAttivitaMagistratiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
				try {
					lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);
					lIspDao.ricercaRiepilogoGeneraleAttivita(aAnno);
					lIspDao.start();
					while (lIspDao.next()) {
						lVect.add(
								(IspAttivitaMagistratiModel) lIspDao.getModelAttivita("RIEPILOGO GENERALE"));
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisController.ExRiepilogoGeneraleAttivita: Non posso leggere : " + daoEx);
				} finally {
					cleanup(lIspDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	}

	/**
	 * Estrae i dati aggregati per anno e tipologia, per il magistrato passato in input. I risultati vengono
	 * aggiunti in append al vettore passato in input
	 *
	 * @param aAnnoIni
	 * @param aAnnoFin
	 * @param modMag
	 * @param lVect
	 *            <IspAttivitaMagistratiModel> Vettore al quale aggiungere i dati estratti
	 * @return
	 * @throws F3BException
	 */
	public Vector ExAttivitaMagistrato(int aAnnoIni, int aAnnoFin, MagistratoModel modMag, Vector lVect)
			throws F3BException {

		Connection lConn = null;

		IspAttivitaMagistratiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
				try {
					lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);
					lIspDao.ricercaAttivitaMagistrato(aAnno, modMag.getCodMagistrato());
					lIspDao.start();
					while (lIspDao.next()) {
						lVect.add(lIspDao.getModelAttivitaCodMag(modMag.getCognome() + " " + modMag.getNome(),
								modMag.getCodMagistrato()));
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisController.ExAttivitaMagistrato: Non posso leggere : " + daoEx);
				} finally {
					cleanup(lIspDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	}

	/**
	 * Carica i dati aggregati per anno da visualizzare sul foglio Riepilogo
	 *
	 * @param aAnnoIni
	 * @param aAnnoFin
	 * @param tipo
	 *            : Tipo di distinta
	 * @return Verctor<IspTempiModel>
	 * @throws F3BException
	 */
	public Vector<IspTempiModel> ExRicercaRiepilogoGeneraleTempi(int aAnnoIni, int aAnnoFin, int tipo)
			throws F3BException {

		Connection lConn = null;

		Vector<IspTempiModel> lVect = new Vector<>();
		IspTempiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
				try {
					lIspDao = new IspTempiSqlDAO(lConn);
					if (tipo == 1)
						lIspDao.ricercaRiepilogoGeneraleTempiRicezioneIscrizione(aAnno);
					else if (tipo == 2)
						lIspDao.ricercaRiepilogoGeneraleTempiGiudicatoRicezione(aAnno);
					else if (tipo == 3)
						lIspDao.ricercaRiepilogoGeneraleTempiGiudicatoIscrizione(aAnno);
					else if (tipo == 4)
						lIspDao.ricercaRiepilogoGeneraleTempiIscrizioneEmissione(aAnno);
					lIspDao.start();
					while (lIspDao.next()) {
						lVect.add((IspTempiModel) lIspDao.getModel());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisController.ExRicercaRiepilogoGeneraleTempi: Non posso leggere : " + daoEx);
				} finally {
					cleanup(lIspDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	}

	/**
	 * Estrae dalla tabella STATIS.ISP_TEMPI_ISCRIZIONE i dati imponendo le condizioni su
	 * TEMPO_GIUDICATO_RICEZIONE o TEMPO_RICEZIONE_ISCRIZIONE in finzione del "tipo" scelto.
	 *
	 * @param tipo
	 * @param intervallo
	 * @return Vector<IspTempiIscrizioneModel>
	 * @throws F3BException
	 */
	public Vector<IspTempiIscrizioneModel> ExRicercaDettaglioTempiIscrizione(int tipo, int intervallo)
			throws F3BException {

		Connection lConn = null;
		Vector<IspTempiIscrizioneModel> lIspVect = new Vector<>();
		IspTempiIscrizioneDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspTempiIscrizioneDAO(lConn);

			if (tipo == 1)
				lIspDao.selCondizioneRicezioneIscrizione(intervallo);
			else if (tipo == 3)
				lIspDao.selCondizioneGiudicatoIscrizione(intervallo);
			else
				throw new F3BException(
						"StatisController.ExRicercaDettaglioTempiIscrizione: tipo non corretto");

			lIspDao.setOrdinamento();

			lIspDao.start();
			while (lIspDao.next()) {
				lIspVect.add((IspTempiIscrizioneModel) lIspDao.getModel());
			}
			if (lIspVect.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExRicercaDettaglioTempiIscrizione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lIspVect;
	}

	/**
	 * Estrae dalla tabella STATIS.ISP_TEMPI_RICEZIONE i record che hanno un TEMPO_GIUDICATO_RICEZIONE che
	 * ricade nell'intervallo indicato
	 *
	 * @param intervallo
	 *            (id dell'intervallo di tempo)
	 * @return Vector<IspTempiRicezioneModel>
	 * @throws F3BException
	 */
	public Vector<IspTempiRicezioneModel> ExRicercaDettaglioTempiRicezione(int intervallo)
			throws F3BException {

		Connection lConn = null;
		Vector<IspTempiRicezioneModel> lIspVect = new Vector<>();
		IspTempiRicezioneDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspTempiRicezioneDAO(lConn);
			lIspDao.selCondizione(intervallo);
			lIspDao.setOrdinamento();

			lIspDao.start();
			while (lIspDao.next()) {
				lIspVect.add((IspTempiRicezioneModel) lIspDao.getModel());
			}

			if (lIspVect.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExRicercaDettaglioTempiRicezione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lIspVect;
	}

	public Vector ExRicercaDettaglioTempiEmissione(int intervallo, String codMag) throws F3BException {

		Connection lConn = null;
		Vector lVect = new Vector();
		IspTempiEmissioneSqlDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspTempiEmissioneSqlDAO(lConn);

			lIspDao.ricercaDettaglioTempiEmissione(intervallo, codMag);

			lIspDao.start();
			while (lIspDao.next()) {
				lVect.add(lIspDao.getModel());
			}
			if (lVect.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisController.ExRicercaDettaglioTempiEmissione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}
		return lVect;
	}

	/**
	 * Metodo che crea il foglio 'RIEPILOGO' del Riepilogo Ispettivo SIEP
	 *
	 * @param aIspModVect
	 *            <IspProvvedimentiModel>
	 * @param wb
	 * @param uffUteConnesso
	 * @param UffScelto
	 * @throws F3BException
	 */
	public void ExCreateRiepilogoIspProvvedimenti(Vector aIspModVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String UffScelto) throws F3BException {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();

		// Stile della cella con bordi
		HSSFCellStyle cs = getBordo4Lati(wb);

		// Stile della cella con bordi e grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(fontBold);

		// Titolo 1
		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

		csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
		csTitolo1.setFont(fontBold);

		// Titolo 2
		HSSFCellStyle csTitolo2 = getBordo4Lati(wb);

		csTitolo2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
		csTitolo2.setFont(fontBold);

		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

		// primo foglio
		HSSFSheet sheet = wb.createSheet("Riepilogo");
		// larghezza colonne
		sheet.setColumnWidth(0, (110 * 256));
		sheet.setColumnWidth(1, (20 * 256));

		// Intestazione del foglio excel
		int nRow = 0;
		if (UffScelto.equals("-"))
			nRow = setIntestazione(sheet, uffUteConnesso, csNull);
		else
			nRow = setIntestazione(sheet, uffUteConnesso, csNull, UffScelto);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0,
				"PROSPETTO RIEPILOGATIVO DELLE ESECUZIONI PENDENTI ED IN CORSO DISTINTE SECONDO LO STATO DELLA PROCEDURA",
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "", cs);
		setCell(row, 1, "TOTALE PROCEDURE", cs);

		Iterator itx = aIspModVect.iterator();

		nRow++;
		int rifRow = nRow;

		Integer lContaArchiviati = 0;
		while (itx.hasNext()) {
			IspProvvedimentiModel lMod = (IspProvvedimentiModel) itx.next();

			if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())
					|| ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
				// Se non si vogliono visualizzare i 'Titoli' sul RIEPILOGO
				// continue;
			}

			if (lMod.getCodStatoFascicoloRes().intValue() == Integer
					.parseInt(ICostantiStatis.COD_ARCHIVIAZIONI)) {
				// 131 - Archiviazione vengono spostate in coda al foglio nella sezione
				// dei totali 21/07/2014 MEV 19
				lContaArchiviati = lMod.getConta();
			} else if (lMod.getCodStatoFascicoloRes().intValue() == Integer
					.parseInt(ICostantiStatis.COD_TITOLO1_ARCHIVIAZIONI)) {
				// e' il titolo ARCHIVIAZIONI lo salto
			} else {
				row = sheet.createRow(nRow);

				if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())) {
					setCell(row, 0, lMod.getDescrStatoFascicoloRes(), csTitolo1);
					setCell(row, 1, "", cs);
					sheet.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1));
				} else if (ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
					setCell(row, 0, lMod.getDescrStatoFascicoloRes(), csTitolo2);
					setCell(row, 1, "", cs);
					sheet.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1));
				} else {
					setCell(row, 0, lMod.getDescrStatoFascicoloRes(), cs);
					setCell(row, 1, lMod.getConta().doubleValue(), cs);
				}
				nRow++;
			}
		}

		row = sheet.createRow(nRow);
		setCell(row, 0, "", cs);
		setCell(row, 1, "", cs);

		// ===============================
		// TOTALE PROCEDIMENTI IN CORSO
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "TOTALE PROCEDIMENTI IN CORSO", csBold);
		setFormulaCell(row, 1, "SUM(B" + (rifRow + 1) + ":B" + (nRow - 1) + ")", csBold);

		// TOTALE PROCEDIMENTI DEFINITI (SU TUTTA LA BASE DATI)
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "TOTALE PROCEDIMENTI DEFINITI (SU TUTTA LA BASE DATI)", csBold);
		setCell(row, 1, lContaArchiviati, csBold);

		// TOTALE GENERALE
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "TOTALE GENERALE", csBold);
		setFormulaCell(row, 1, "SUM(B" + (nRow - 1) + ":B" + (nRow) + ")", csBold);
		// ==============

		// nRow++;
		// row = sheet.createRow(nRow);
		// setCell(row, 0, "TOTALE GENERALE", csBold);
		// setFormulaCell(row, 1, "SUM(B" + (rifRow + 1) + ":B" + (nRow - 1) + ")", csBold);
	}

	/**
	 * Crea il foglio excel per il 'DETTAGLIO' del riepilogo ispettivo
	 *
	 * @param aVect
	 *            Vettore con i dati dei tempi
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param nomeFoglio
	 *            Nome del foglio (sheet)
	 * @throws F3BException
	 *             propaga l'eccezione
	 */
	public void ExCreateDettagliProcedimenti(Vector aIspModVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String nomeFoglio, String UffScelto) throws F3BException {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();

		// Stile della cella con bordi
		// HSSFCellStyle cs = getBordo4Lati(wb);

		HSSFCellStyle csBold = wb.createCellStyle();
		// Create a new font and alter it.
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(fontBold);

		// Stile della cella con bordi e
		// allineamento a destra
		HSSFCellStyle csR = getBordo4Lati(wb);
		csR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// stile per celle col bordo con carattere grassetto
		// ALLINEATO A DESTRA
		HSSFCellStyle csBoldRight = getBordo4Lati(wb);
		csBoldRight.setFont(fontBold);
		csBoldRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// Stile Titolo 1
		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

		csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
		csTitolo1.setFont(fontBold);

		// Stile Titolo 2
		HSSFCellStyle csTitolo2 = getBordo4Lati(wb);

		csTitolo2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo2.setFillForegroundColor(HSSFColor.YELLOW.index);
		csTitolo2.setFont(fontBold);

		// Utilizzo colore non standard (204,255,204)
		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 204, (byte) 255, (byte) 204);

		HSSFSheet sheet = wb.createSheet(nomeFoglio);

		// int nRow = 0;

		int nRow = 0;
		if (UffScelto.equals("-"))
			nRow = setIntestazione(sheet, uffUteConnesso, csNull);
		else
			nRow = setIntestazione(sheet, uffUteConnesso, csNull, UffScelto);

		nRow++;
		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "ELENCO   NUMERICO   DELLE   PROCEDURE  ESECUTIVE  PENDENTI  ALLA  DATA  DEL",
				csNull);

		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		setCell(row, 0, DateUtils.getSysDate("dd-MM-yyyy")
				+ ",   DISTINTE   PER   ANNO   E  SECONDO  LO  STATO  DI  ESECUZIONE", csNull);

		String oldUfficio = "";
		boolean firstUfficio = true;

		int nCell = 0;
		Integer oldCodStato = new Integer(0);
		Integer oldAnno = new Integer(0);
		boolean firstCod = true;
		boolean firstAnno = true;
		int totGenNum = 0;
		int totAnnoNum = 0;
		// String totAnno = "";
		String formula = "";
		int maxCellxRow = 8;

		// boolean aa = true;
		boolean lLastRigaTitolo1 = false; // indica se l'ultima riga inserita era un Titolo1
		boolean lLastRigaTitolo2 = false; // indica se l'ultima riga inserita era un Titolo1

		nRow++;

		Iterator itx = aIspModVect.iterator();
		while (itx.hasNext()) {

			IspProvvedimentiModel lMod = (IspProvvedimentiModel) itx.next();

			// if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo()))
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Titolo 1: "+lMod.getDescrStatoFascicoloRes());
			// else if (ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo()))
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Titolo 2: "+lMod.getDescrStatoFascicoloRes());

			if (!oldCodStato.equals(lMod.getCodStatoFascicoloRes())) {

				// if (!firstCod)
				if (!firstCod && !lLastRigaTitolo1 && !lLastRigaTitolo2) {
					// ====================================================================
					// Cambio Stato_Fascicolo
					// - Va scritta la riga con il 'TOTALE ANNO'
					// - Va scritta una riga vuota
					// - Va scritta la riga con il 'TOTALE GENERALE'
					// ====================================================================
					if (nCell > 0)
						nRow++;

					// formula = totAnno + ":I" + (nRow) + ")";
					// formula = totAnno + ":I" + (nRow) + ";\"<>\")";
					formula = totAnnoNum + "";
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldRight, csNull, formula);

					totGenNum += totAnnoNum;

					nRow++;
					nRow++;

					// formula = totGen.substring(0, totGen.length() - 1) + ")";
					formula = totGenNum + "";
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE GENERALE", csBoldRight, csNull, formula);

					totGenNum = 0;
					nRow++;
				}

				if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())
						|| ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
					// Il record corrente contine il TITOLO (1 o 2) da visualizare
					nRow++;
					if (!lLastRigaTitolo1)
						nRow++;

					HSSFCellStyle csRiga = null;
					if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())) {
						csRiga = csTitolo1;
						lLastRigaTitolo1 = true;
						lLastRigaTitolo2 = false;
					} else {
						csRiga = csTitolo2;
						lLastRigaTitolo1 = false;
						lLastRigaTitolo2 = true;
					}

					row = sheet.createRow(nRow);

					setCell(row, 0, lMod.getDescrStatoFascicoloRes(), csRiga);

					for (int i = 1; i <= maxCellxRow; i++)
						setCell(row, i, "", csRiga);

					sheet.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, maxCellxRow));

					// setCell(row, (maxCellxRow+1), "x", csTitolo1); // debug
				} else {
					lLastRigaTitolo1 = false;
					lLastRigaTitolo2 = false;

					nRow++;
					nRow++;
					row = sheet.createRow(nRow);
					setCell(row, 0, lMod.getDescrStatoFascicoloRes(), csBold);
				} // end if T1,T2

				oldAnno = new Integer(0);

				// nRow++;

				if (!ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())
						&& !ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
					firstCod = false;
				}
				firstAnno = true;
			}

			oldCodStato = lMod.getCodStatoFascicoloRes();

			if (ICostantiStatis.TIPO_CAMPO_TITOLO1.equals(lMod.getTipoCampo())
					|| ICostantiStatis.TIPO_CAMPO_TITOLO2.equals(lMod.getTipoCampo())) {
				// Nel caso in cui sto visualizzando un titolo (T1,T2) non procedo
				// con il test su cambio anno o cambio ufficio. Il test e' inutile e
				// andrebbe in errore non essendo definiti sul record del titolo ne
				// l'anno ne l'ufficio
				continue;
			}

			// ====================================================================
			// Se nuovo anno (o prima riga del nuovo Stato Fascicolo)
			// ANNO YYYY - Descrizione Ufficio
			if (!oldAnno.equals(lMod.getChiaveAnno())) {
				if (!firstAnno) {
					if (nCell > 0)
						nRow++;

					formula = totAnnoNum + "";
					setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldRight, csNull, formula);

					totGenNum += totAnnoNum;
				}

				firstAnno = false;

				nRow++;
				nRow++;

				row = sheet.createRow(nRow);
				// -- NGG
				if (UffScelto.equals("-"))
					setCell(row, 0, "ANNO " + lMod.getChiaveAnno().toString() + " - "
							+ lMod.getDescUfficioInserimento(), csNull);
				else
					setCell(row, 0, "ANNO " + lMod.getChiaveAnno().toString(), csNull);

				firstUfficio = true;
				// --

				nCell = 0;
				nRow++;
				totAnnoNum = 0;
			} else
				firstUfficio = false;

			oldAnno = lMod.getChiaveAnno();

			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			// NGG

			if (!oldUfficio.equals(lMod.getCodUfficioInserimento())) {
				if (!firstUfficio) {
					nRow++;
					nRow++;
					row = sheet.createRow(nRow);
					if (UffScelto.equals("-"))
						setCell(row, 0, "ANNO " + lMod.getChiaveAnno().toString() + " - "
								+ lMod.getDescUfficioInserimento(), csNull);
					else
						setCell(row, 0, "ANNO " + lMod.getChiaveAnno().toString(), csNull);
					nCell = 0;
					nRow++;

					row = sheet.getRow(nRow);
					if (row == null)
						row = sheet.createRow(nRow);

					// if(lMod.getChiaveProgr() > 999999999)
					if (lMod.getChiaveProgrOrig() != null)
						setCell(row, nCell, lMod.getChiaveProgrOrig().toString(), csR);
					else
						setCell(row, nCell, lMod.getChiaveProgr().toString(), csR);

					oldUfficio = lMod.getCodUfficioInserimento();
				} else {
					firstUfficio = false;
				}
			}

			// if(lMod.getChiaveProgr().intValue() > 999999999)
			// =======================================
			// Scrivo il Numero Fascicolo
			// =======================================
			if (lMod.getChiaveProgrOrig() != null)
				setCell(row, nCell, lMod.getChiaveProgrOrig().toString(), csR);
			else
				setCell(row, nCell, lMod.getChiaveProgr().toString(), csR);

			oldUfficio = lMod.getCodUfficioInserimento();

			// END NGG

			totAnnoNum++;

			if (nCell == maxCellxRow) {
				nCell = 0;
				nRow++;
			} else {
				nCell++;
			}

		} // end while

		nRow++;
		formula = totAnnoNum + "";
		setRowTotaliProvvedimenti(sheet, nRow, "TOTALE ANNO", csBoldRight, csNull, formula);

		totGenNum += totAnnoNum;

		nRow++;
		nRow++;
		// formula = totGen.substring(0, totGen.length() - 1) + ")";
		formula = totGenNum + "";
		setRowTotaliProvvedimenti(sheet, nRow, "TOTALE GENERALE", csBoldRight, csNull, formula);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Fine ExCreateDettagliProcedimenti");
	}

	/**
	 * Crea il foglio excel per l'attivita' dei magistrati
	 *
	 * @param aVect
	 *            Vettore <IspAttivitaMagistratiModel> con i dati dell'attivita' dei magistrati
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param String
	 *            descIntesta
	 * @param Vector
	 *            VTotaliMotivi - Totali aggregati per codice motivo x il Riepilogo Generale
	 * @param Vector
	 *            VTotaliMotiviMag - Totali aggregati per codice motivo e Magistrato x il Dettaglio
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateAttivitaMagistrati(Vector aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, String descIntesta, Vector VTotaliMotivi, Vector VTotaliMotiviMag)
			throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);

		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// primo foglio -----------------------------------------RIEPILOGO
		HSSFSheet sheet = wb.createSheet("Riepilogo");
		sheet.setColumnWidth(0, (70 * 256));

		// Colore per TIPOLOGIA
		HSSFCellStyle style = wb.createCellStyle();
		style = getBordo4Lati(wb);
		// style.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
		HSSFFont fontGR = wb.createFont();
		fontGR.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontGR.setColor(HSSFColor.BLUE.index);
		style.setFont(fontGR);

		// Colore per TOTALI
		HSSFCellStyle stylered = wb.createCellStyle();
		stylered = getBordo4Lati(wb);

		HSSFFont fontR = wb.createFont();
		fontR.setColor(HSSFColor.RED.index);
		stylered.setFont(fontR);

		// Intestazione del foglio excel
		int nRow = setIntestazione(sheet, uffUteConnesso, csNull, descIntesta);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica lavoro magistrati del " + DateUtils.getSysDate("dd/MM/yyyy"), csNull);

		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Periodo dal " + dataIni + " al " + dataFin, csNull);

		Iterator itx = aVect.iterator();

		int annoOld = 0;
		String magOld = "";
		int nColAnno = 0;
		int nRowAnno = nRow;
		int firstMag = 0;
		int[] RigheBuone; // = new int[43];
		int colonne = 0;
		int k = 0;

		int numRighe = 0;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero il numero di record della tabella ISP_TIPOLOGIA_ATTIVITA...");
		numRighe = getNumTipologieAttivita();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero di record della tabella ISP_TIPOLOGIA_ATTIVITA = " + numRighe);

		RigheBuone = new int[numRighe];

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {

			IspAttivitaMagistratiModel lMod = (IspAttivitaMagistratiModel) itx.next();
			// test per cambio magistrato
			if (!(magOld.equals(lMod.getDescrMagistrato()))) {

				// se primo magistrato non entro (il primo e' il Riepilogo)
				if (firstMag > 0) {

					nColAnno++;

					// Richiamo metodo per la scrittura delle formule contenenti
					// le somme parziali e generali

					nRow = setTotaliAttivitaMagistrati(sheet, csBold, nRow, nRowAnno, nColAnno, RigheBuone,
							style, stylered);
					k = 0;
					// se finito riepilogo cambio foglio per il dettaglio
					if (firstMag == 1) {

						// ------------ //secondo foglio //-------------------------------------------------
						sheet = wb.createSheet("Dettaglio");
						sheet.setColumnWidth(0, (70 * 256));

						// Intestazione del foglio excel
						nRow = setIntestazione(sheet, uffUteConnesso, csNull, descIntesta);

						nRow++;
						nRow++;

						// row = sheet.createRow(nRow);
						row = sheet.getRow(nRow);
						if (row == null)
							row = sheet.createRow(nRow);

						setCell(row, 0,
								"Statistica lavoro magistrati del " + DateUtils.getSysDate("dd/MM/yyyy"),
								csNull);

						nRow++;

						row = sheet.getRow(nRow);
						if (row == null)
							row = sheet.createRow(nRow);

						setCell(row, 0, "Periodo dal " + dataIni + " al " + dataFin, csNull);
					}
				}
				firstMag++;

				nRow++;
				nRow++;
				nRow++;

				nRowAnno = nRow;
				nColAnno = 0;
				annoOld = 0;

				row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);

				setCell(row, nColAnno, lMod.getDescrMagistrato(), csBold);

			}
			magOld = lMod.getDescrMagistrato();

			// test per cambio anno
			if (annoOld != lMod.getAnno().intValue()) {

				nColAnno++;
				nRow = nRowAnno;

				// scrittura anno
				row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);

				setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBold);
			}
			annoOld = lMod.getAnno().intValue();

			nRow++;

			// scrittura tipologia e valore
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, lMod.getDescrTipologia(), style);
			setCell(row, nColAnno, lMod.getConta().doubleValue(), style);

			// serve per segnare le righebuone ai fini del conteggio finale
			colonne = nColAnno;
			if (colonne == 1) {
				RigheBuone[k] = nRow;
				if (k < RigheBuone.length) {
					k++;
				}
			}

			// NGG _- Statistiche SIEP -
			// -------------------------------> Inserimento RIGHE COD_MOTIVO

			// if(lMod.getDescrMagistrato().equals("RIEPILOGO GENERALE"))
			// {
			Vector lMotivi = ExCercaMotivi(lMod.getTipologia());

			nRow++;
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, "DI CUI", cs);

			Iterator itx1 = lMotivi.iterator();
			while (itx1.hasNext()) {
				IspAttivitaMagistratiModel lAct = (IspAttivitaMagistratiModel) itx1.next();

				nRow++;
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);

				// setCell(row, 0, " Cod. "+lAct.getCodMotivo()+" - "+lAct.getDescrMotivo(), cs);
				setCell(row, 0, "   " + lAct.getDescrMotivo(), cs);
				// -------->
				int totamoti = 0;
				if (lMod.getDescrMagistrato().equals("RIEPILOGO GENERALE")) {
					Iterator itx2 = VTotaliMotivi.iterator();

					while (itx2.hasNext()) {
						IspAttivitaMagistratiModel TotMotMod1 = (IspAttivitaMagistratiModel) itx2.next();
						if (TotMotMod1.getAnno().equals(lMod.getAnno().intValue())
								&& TotMotMod1.getTipologia().equals(lMod.getTipologia())
								&& TotMotMod1.getCodMotivo().equals(lAct.getCodMotivo())) {
							totamoti = TotMotMod1.getConta();
						}
					}
				}
				// ------>
				else {
					Iterator itx2 = VTotaliMotiviMag.iterator();
					while (itx2.hasNext()) {
						IspAttivitaMagistratiModel TotMotMod2 = (IspAttivitaMagistratiModel) itx2.next();

						if (lMod.getDescrMagistrato().equals("MAGISTRATO NULLO")) {
							if (TotMotMod2.getAnno().equals(lMod.getAnno().intValue())
									&& TotMotMod2.getTipologia().equals(lMod.getTipologia())
									&& TotMotMod2.getCodMotivo().equals(lAct.getCodMotivo())
									&& TotMotMod2.getCodMagistrato() == null) {
								totamoti = TotMotMod2.getConta();
							}
						} else {
							if (TotMotMod2.getAnno().equals(lMod.getAnno().intValue())
									&& TotMotMod2.getTipologia().equals(lMod.getTipologia())
									&& TotMotMod2.getCodMotivo().equals(lAct.getCodMotivo())) {
								if (TotMotMod2.getCodMagistrato() != null) {
									if (TotMotMod2.getCodMagistrato().equals(lMod.getCodMagistrato())) {
										totamoti = TotMotMod2.getConta();
									}
								}
							}
						}
					}
				}
				setCell(row, nColAnno, totamoti, cs);
			}
			// }
			// NGG - END
		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti
		// le somme parziali e generali
		nColAnno++;

		nRow = setTotaliAttivitaMagistrati(sheet, csBold, nRow, nRowAnno, nColAnno, RigheBuone, style,
				stylered);
	}

	/**
	 * Crea il foglio excel per il Riepilogo dei tempi iscrizione
	 *
	 * @param aVect
	 *            Vettore di Vettori con i dati dei tempi per le tre tipologie di Distinta
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateRiepilogoTempi(Vector<Vector> aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, String DescIntesta) throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// primo foglio
		HSSFSheet sheet = wb.createSheet("Riepilogo");
		sheet.setColumnWidth(0, (40 * 256));
		// sheet.setColumnWidth(1, (20 * 256));

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica relativa al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		for (int tipo = 1; tipo < 4; tipo++) {

			// Tipologia tempi
			String tipologia = "";
			if (tipo == 1)
				tipologia = "TEMPI TRA RICEZIONE ESTRATTO ED ISCRIZIONE PROCEDIMENTI";
			else if (tipo == 2)
				tipologia = "TEMPI TRA PASSAGGIO IN GIUDICATO E RICEZIONE ESTRATTO";
			else if (tipo == 3)
				tipologia = "TEMPI TRA PASSAGGIO IN GIUDICATO ED ISCRIZIONE PROCEDIMENTI";

			// creazione riga con altezza per wraptext
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			// row.setHeight(100);
			csBold.setWrapText(true);
			setCell(row, 0, tipologia, csBold);
			// csBold.setWrapText( false );

			// setCell(row, 1, "ANNI", csBold);

			// nRow++;

			Vector lVect = aVect.get(tipo - 1);
			Iterator itx = lVect.iterator();

			int nRowAnno = nRow;
			int annoOld = 0;
			int nColAnno = 0;
			String formula = "";

			// inizio ciclo di scrittura dei dati
			while (itx.hasNext()) {

				IspTempiModel lMod = (IspTempiModel) itx.next();

				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {

					nColAnno++;
					nRow = nRowAnno;

					// scrittura anno

					// row = sheet.createRow(nRowAnno);
					row = sheet.getRow(nRowAnno);
					if (row == null)
						row = sheet.createRow(nRowAnno);

					setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
				}
				annoOld = lMod.getAnno().intValue();

				nRow++;

				// scrittura tipologia e valore
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "ENTRO 5 GIORNI", cs);
				setCell(row, nColAnno, lMod.getEntro5().doubleValue(), cs);

				nRow++;

				// scrittura tipologia e valore
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "ENTRO 20 GIORNI", cs);
				setCell(row, nColAnno, lMod.getEntro20().doubleValue(), cs);

				nRow++;

				// scrittura tipologia e valore
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "ENTRO 30 GIORNI", cs);
				setCell(row, nColAnno, lMod.getEntro30().doubleValue(), cs);

				nRow++;

				// scrittura tipologia e valore
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "ENTRO 60 GIORNI", cs);
				setCell(row, nColAnno, lMod.getEntro60().doubleValue(), cs);

				nRow++;

				// scrittura tipologia e valore
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "ENTRO 90 GIORNI", cs);
				setCell(row, nColAnno, lMod.getEntro90().doubleValue(), cs);

				nRow++;

				// scrittura tipologia e valore
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "OLTRE 90 GIORNI", cs);
				setCell(row, nColAnno, lMod.getOltre90().doubleValue(), cs);

				nRow++;

				// scrittura totale anno
				// row = sheet.createRow(nRow);
				row = sheet.getRow(nRow);
				if (row == null)
					row = sheet.createRow(nRow);
				setCell(row, 0, "TOTALE", csBoldCenter);
				formula = this.getStringaSomma(nRowAnno + 1, nColAnno, nRow - 1, nColAnno);
				setFormulaCell(row, nColAnno, formula, csBold);
			}

			// fuori ciclo
			// Richiamo metodo per la scrittura delle formule contenenti
			// le somme parziali e generali
			nColAnno++;
			nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);

			nRow++;
			nRow++;
			nRow++;
		}
	}

	/**
	 * Crea il foglio excel per il riepilogo dei tempi emissione
	 *
	 * @param aVect
	 *            Vettore con i dati dei tempi
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateRiepilogoTempiEmissione(Vector aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, String DescUffIntesta) throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// primo foglio
		HSSFSheet sheet = wb.createSheet("Riepilogo");
		sheet.setColumnWidth(0, (40 * 256));
		// sheet.setColumnWidth(1, (20 * 256));

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescUffIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica relativa al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// Tipologia tempi
		String tipologia = "";
		tipologia = "TEMPI TRA ISCRIZIONE FASCICOLO ED EMISSIONE ORDINE DI ESECUZIONE E SOSPENSIONE";

		// creazione riga con altezza per wraptext
		row = sheet.createRow(nRow);
		// row.setHeight(100);
		csBold.setWrapText(true);
		setCell(row, 0, tipologia, csBold);
		// csBold.setWrapText( false );

		// setCell(row, 1, "ANNI", csBold);

		// nRow++;

		Iterator itx = aVect.iterator();

		int nRowAnno = nRow;
		int annoOld = 0;
		int nColAnno = 0;
		String formula = "";

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {

			IspTempiModel lMod = (IspTempiModel) itx.next();

			// test per cambio anno
			if (annoOld != lMod.getAnno().intValue()) {

				nColAnno++;
				nRow = nRowAnno;

				// scrittura anno
				// row = sheet.createRow(nRowAnno);
				row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);

				setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			}
			annoOld = lMod.getAnno().intValue();

			nRow++;

			// scrittura tipologia e valore
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ENTRO 5 GIORNI", cs);
			setCell(row, nColAnno, lMod.getEntro5().doubleValue(), cs);

			nRow++;

			// scrittura tipologia e valore
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ENTRO 20 GIORNI", cs);
			setCell(row, nColAnno, lMod.getEntro20().doubleValue(), cs);

			nRow++;

			// scrittura tipologia e valore
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ENTRO 30 GIORNI", cs);
			setCell(row, nColAnno, lMod.getEntro30().doubleValue(), cs);

			nRow++;

			// scrittura tipologia e valore
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ENTRO 60 GIORNI", cs);
			setCell(row, nColAnno, lMod.getEntro60().doubleValue(), cs);

			nRow++;

			// scrittura tipologia e valore
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ENTRO 90 GIORNI", cs);
			setCell(row, nColAnno, lMod.getEntro90().doubleValue(), cs);

			nRow++;

			// scrittura tipologia e valore
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "OLTRE 90 GIORNI", cs);
			setCell(row, nColAnno, lMod.getOltre90().doubleValue(), cs);

			nRow++;

			// scrittura totale anno
			// row = sheet.createRow(nRow);
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE", csBoldCenter);
			formula = this.getStringaSomma(nRowAnno + 1, nColAnno, nRow - 1, nColAnno);
			setFormulaCell(row, nColAnno, formula, csBold);
		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti
		// le somme parziali e generali
		nColAnno++;
		nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);
	}

	/**
	 * Crea il foglio excel per il 'Dettaglio' dei tempi iscrizione/ricezione
	 *
	 * @param aVect
	 *            Vettore con i dati dei tempi
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param tipo
	 *            Tipologia tempi
	 * @param intervallo
	 *            intervallo temporale
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateDettaglioTempi(Vector aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, int tipo, int intervallo, String Accorpato, String DescIntesta)
			throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// foglio dettaglio
		HSSFSheet sheet = wb.createSheet("Dettaglio");

		// settaggio della larghezza
		// delle colonne
		sheet.setColumnWidth(0, (18 * 256));
		sheet.setColumnWidth(1, (18 * 256));
		sheet.setColumnWidth(2, (18 * 256));
		sheet.setColumnWidth(3, (10 * 256));
		sheet.setColumnWidth(4, (30 * 256));
		sheet.setColumnWidth(5, (20 * 256));
		sheet.setColumnWidth(6, (20 * 256));

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativo al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// Tipologia tempi
		String tipologia = "";
		String data1 = "";
		String data2 = "";

		if (tipo == 1) {
			tipologia = "RICEZIONE ED ISCRIZIONE";
			data1 = "RICEZIONE";
			data2 = "ISCRIZIONE";
		} else if (tipo == 2) {

			tipologia = "PASSAGGIO IN GIUDICATO E RICEZIONE";
			data1 = "GIUDICATO";
			data2 = "RICEZIONE";

		} else if (tipo == 3) {

			tipologia = "PASSAGGIO IN GIUDICATO ED ISCRIZIONE";
			data1 = "GIUDICATO";
			data2 = "ISCRIZIONE";
		}

		// intervallo temporale
		String tipoIntervallo = "";
		if (intervallo == 0)
			tipoIntervallo = "OLTRE 90";
		else
			tipoIntervallo = "ENTRO " + intervallo;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Distinta dei fascicoli con intervallo tra le date di " + tipologia, csNull);

		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "compreso nel periodo " + tipoIntervallo + " GIORNI", csNull);

		Iterator itx = aVect.iterator();

		String annoOld = "";
		String anno = "";

		IspTempiIscrizioneModel lMod = null;
		IspTempiRicezioneModel lMod2 = null;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {

			// controllo il tipo per recuperare
			// il model appropriato
			if (tipo == 2) {
				lMod2 = (IspTempiRicezioneModel) itx.next();
				anno = DateUtils.getYearToString(lMod2.getDataArrivoAtto());
			} else {
				lMod = (IspTempiIscrizioneModel) itx.next();
				anno = DateUtils.getYearToString(lMod.getDataIscrizione());
			}

			// test per cambio anno
			if (!(annoOld.equals(anno))) {

				nRow++;
				nRow++;
				nRow++;

				row = sheet.createRow(nRow);
				setCell(row, 0, "ANNO " + data2 + " " + anno, csNull);

				nRow++;

				row = sheet.createRow(nRow);
				setCell(row, 0, "N. FASCICOLO", csBoldCenter);
				setCell(row, 1, "DATA " + data1, csBoldCenter);
				setCell(row, 2, "DATA " + data2, csBoldCenter);
				setCell(row, 3, "GIORNI", csBoldCenter);
				setCell(row, 4, "AUTORITA' EMITTENTE", csBoldCenter);
				setCell(row, 5, "LUOGO EMITTENTE", csBoldCenter);
				setCell(row, 6, "SEZIONE AUTORITA'", csBoldCenter);
			}
			annoOld = anno;

			nRow++;

			row = sheet.createRow(nRow);

			if (tipo == 2) {
				// if(lMod2.getChiaveProgr().intValue() > 999999999)
				// {
				if (!Accorpato.equals("NO"))
					setCell(row, 0, " " + lMod2.getChiaveProgrOrig().toString() + "/"
							+ lMod2.getChiaveAnno().toString(), cs);
				else
					// setCell(row, 0, " " + lMod2.getChiaveProgrOrig().toString() + "/" +
					// lMod2.getChiaveAnno().toString() + " - "+ lMod2.getDescUfficioInserimento(), cs);
					// }
					// else
					setCell(row, 0,
							" " + lMod2.getChiaveProgr().toString() + "/" + lMod2.getChiaveAnno().toString(),
							cs);

				setCell(row, 1, " " + DateUtils.getDateToString(lMod2.getDataIrrevocabilita(), "dd-MM-yyyy"),
						cs);
				setCell(row, 2, " " + DateUtils.getDateToString(lMod2.getDataArrivoAtto(), "dd-MM-yyyy"), cs);
				if (lMod2.getTempoGiudicatoRicezione() != null) {
					setCell(row, 3, lMod2.getTempoGiudicatoRicezione().doubleValue(), cs);
				} else {
					setCell(row, 3, "n.d.", cs);
				}
				setCell(row, 4, lMod2.getDescTipoAutoritaEmittente(), cs);
				setCell(row, 5, lMod2.getDescLuogoEmittente(), cs);
				setCell(row, 6, lMod2.getDescSezioneAutorita(), cs);

			} else {
				// if(lMod.getChiaveProgr().intValue() > 999999999)
				// {
				if (!Accorpato.equals("NO"))
					setCell(row, 0, " " + lMod.getChiaveProgrOrig().toString() + "/"
							+ lMod.getChiaveAnno().toString(), cs);
				else
					// setCell(row, 0, " " + lMod.getChiaveProgrOrig().toString() + "/" +
					// lMod.getChiaveAnno().toString() + " - "+ lMod.getDescUfficioInserimento(), cs);
					// }
					// else
					setCell(row, 0,
							" " + lMod.getChiaveProgr().toString() + "/" + lMod.getChiaveAnno().toString(),
							cs);

				if (tipo == 3) {

					setCell(row, 1,
							" " + DateUtils.getDateToString(lMod.getDataIrrevocabilita(), "dd-MM-yyyy"), cs);
					setCell(row, 2, " " + DateUtils.getDateToString(lMod.getDataIscrizione(), "dd-MM-yyyy"),
							cs);
					if (lMod.getTempoGiudicatoIscrizione() != null) {
						setCell(row, 3, lMod.getTempoGiudicatoIscrizione().doubleValue(), cs);
					} else {
						setCell(row, 3, "n.d.", cs);
					}
				} else if (tipo == 1) {

					setCell(row, 1, " " + DateUtils.getDateToString(lMod.getDataArrivoAtto(), "dd-MM-yyyy"),
							cs);
					setCell(row, 2, " " + DateUtils.getDateToString(lMod.getDataIscrizione(), "dd-MM-yyyy"),
							cs);
					setCell(row, 3, lMod.getTempoRicezioneIscrizione().doubleValue(), cs);
				}

				setCell(row, 4, lMod.getDescTipoAutoritaEmittente(), cs);
				setCell(row, 5, lMod.getDescLuogoEmittente(), cs);
				setCell(row, 6, lMod.getDescSezioneAutorita(), cs);
			}
		}
	}

	/**
	 * Crea il foglio excel per il dettaglio dei tempi emissione
	 *
	 * @param aVect
	 *            Vettore con i dati dei tempi
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param intervallo
	 *            intervallo temporale
	 * @param codMag
	 *            codice magistrato ("0" = tutti)
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateDettaglioTempiEmissione(Vector aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, int intervallo, String codMag, String Accorpato1,
			String DescIntestazione) throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		// HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato e testo verticale
		HSSFCellStyle csVert = getBordo4Lati(wb);
		csVert.setFont(font);
		csVert.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csVert.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csVert.setRotation((short) 90);

		// foglio dettaglio
		HSSFSheet sheet = wb.createSheet("Dettaglio");

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntestazione);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativo al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// controllo se dettaglio di
		// tutti i magistrati
		int colShift = 0;
		if (codMag.equals("0")) {
			colShift = 1; // Se la ricerca e' per tutti i magistrati Inserisco anche la colonna magistrato
		} else {

			IspTempiEmissioneModel lMod = (IspTempiEmissioneModel) aVect.get(0);
			row = sheet.createRow(nRow);
			setCell(row, 0, "Magistrato delegato: " + lMod.getDescrMagistrato(), csNull);

			nRow++;
			nRow++;
		}

		// intervallo temporale
		String tipoIntervallo = "";
		if (intervallo == 0)
			tipoIntervallo = "dopo 90";
		else
			tipoIntervallo = "entro " + intervallo;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco delle esecuzioni iniziate " + tipoIntervallo
				+ " giorni dall'iscrizione del fascicolo", csNull);

		nRow++;

		// ============================================
		// settaggio della larghezza delle colonne
		// ============================================
		sheet.setColumnWidth(0, (10 * 256)); // Numero d'ordine
		sheet.setColumnWidth(1, (10 * 256)); // Numero Fascicolo

		if (codMag.equals("0"))
			sheet.setColumnWidth(2, (15 * 256)); // Magistrato Delegato

		sheet.setColumnWidth((2 + colShift), (10 * 256)); // Data passaggio in giudicato
		sheet.setColumnWidth((3 + colShift), (10 * 256)); // Data arrivo estratto esecutivo
		sheet.setColumnWidth((4 + colShift), (10 * 256)); // Ritardo
		sheet.setColumnWidth((5 + colShift), (10 * 256)); // Data iscrizione fascicolo
		sheet.setColumnWidth((6 + colShift), (10 * 256)); // Tempi di Registrazione
		sheet.setColumnWidth((7 + colShift), (10 * 256)); // Data primo atto P.M.M.
		sheet.setColumnWidth((8 + colShift), (20 * 256)); // Natura dell'atto

		// sheet.setColumnWidth((9+colShift), (10 * 256)); // Data pervenimento ultimo atto o
		// provvedimento Giudice
		sheet.setColumnWidth((9 + colShift), (10 * 256)); // Data ordine esecuzione
		sheet.setColumnWidth((10 + colShift), (15 * 256)); // Indicazione atto o provvedimento
		sheet.setColumnWidth((11 + colShift), (10 * 256)); // Giorni di ritardo dall'arrivo
															// dell'ultimo atto o
															// provvedimento

		sheet.setColumnWidth((12 + colShift), (10 * 256)); // Data inizio istruttoria
		sheet.setColumnWidth((13 + colShift), (10 * 256)); // Data fine istruttoria
		sheet.setColumnWidth((14 + colShift), (10 * 256)); // Tempi Istruttoria
		sheet.setColumnWidth((15 + colShift), (10 * 256)); // Inattivita' dal
		sheet.setColumnWidth((16 + colShift), (10 * 256)); // Inattivita' al
		sheet.setColumnWidth((17 + colShift), (10 * 256)); // Totale Giorni Inattivita'
		sheet.setColumnWidth((18 + colShift), (10 * 256)); // Giorni da neutralizzare

		// ==================================================
		// Crea la Riga con le intestazioni della tabella
		// ==================================================
		row = sheet.createRow(nRow);
		setCell(row, 0, "Numero d'ordine", csVert);
		setCell(row, 1, "Numero fascicolo", csVert);

		if (codMag.equals("0"))
			setCell(row, 2, "Magistrato delegato", csVert);

		setCell(row, (2 + colShift), "Data passaggio in giudicato", csVert); // new
		setCell(row, (3 + colShift), "Data arrivo estratto esecutivo", csVert); // new
		setCell(row, (4 + colShift), "Ritardo", csVert); // new
		setCell(row, (5 + colShift), "Data iscrizione fascicolo", csVert);
		setCell(row, (6 + colShift), "Tempi di Registrazione", csVert); // new

		setCell(row, (7 + colShift), "Data primo atto P.M.M.", csVert);
		setCell(row, (8 + colShift), "Natura dell'atto", csBoldCenter);
		// setCell(row, (9+colShift), "Data pervenimento ultimo atto o provvedimento Giudice",
		// csVert);
		setCell(row, (9 + colShift), "Data ordine esecuzione", csVert);
		setCell(row, (10 + colShift), "Indicazione atto o provvedimento", csVert);
		setCell(row, (11 + colShift), "Giorni di ritardo dall'arrivo dell'ultimo atto o provvedimento",
				csVert);

		setCell(row, (12 + colShift), "Data inizio istruttoria", csVert);
		setCell(row, (13 + colShift), "Data fine istruttoria", csVert);
		setCell(row, (14 + colShift), "Tempi Istruttoria", csVert);
		setCell(row, (15 + colShift), "Inattivita' dal", csVert);
		setCell(row, (16 + colShift), "Inattivita' al", csVert);
		setCell(row, (17 + colShift), "Totale Giorni Inattivita'", csVert);
		setCell(row, (18 + colShift), "Giorni da neutralizzare", csVert);

		nRow++;

		// ====================================================
		// Inizio ciclo di scrittura delle righe con i dati
		// ====================================================
		Iterator itx = aVect.iterator();
		int cont = 1;
		while (itx.hasNext()) {
			IspTempiEmissioneModel lMod = (IspTempiEmissioneModel) itx.next();

			row = sheet.createRow(nRow);
			setCell(row, 0, cont, csCenter);
			// if(lMod.getChiaveProgr().intValue() > 999999999)
			if (lMod.getChiaveProgrOrig() != null) {
				if (Accorpato1.equals("-"))
					setCell(row, 1, " " + lMod.getChiaveProgrOrig().toString() + "/"
							+ lMod.getChiaveAnno().toString(), csCenter);
				else
					setCell(row, 1, " " + lMod.getChiaveProgrOrig().toString() + "/"
							+ lMod.getChiaveAnno().toString() + " - " + lMod.getDescUfficioInserimento(),
							csCenter);
			} else
				setCell(row, 1, lMod.getChiaveProgr() + "/" + lMod.getChiaveAnno(), csCenter);
			if (codMag.equals("0"))
				setCell(row, 2, lMod.getDescrMagistrato(), csCenter);

			setCell(row, (2 + colShift),
					DateUtils.getDateToString(lMod.getDataPassatoGiudicato(), "dd/MM/yyyy"), csCenter);
			setCell(row, (3 + colShift), DateUtils.getDateToString(lMod.getDataArrivoAtto(), "dd/MM/yyyy"),
					csCenter);

			if (lMod.getTempiGiudicatoArrivo() != null) {
				setCell(row, (4 + colShift), lMod.getTempiGiudicatoArrivo().doubleValue(), csBoldCenter);
			} else {
				setCell(row, (4 + colShift), "n.d.", csBoldCenter);
			}
			setCell(row, (5 + colShift),
					DateUtils.getDateToString(lMod.getDataIscrizioneFascicolo(), "dd/MM/yyyy"), csCenter);

			if (lMod.getTempiArrivoIscrizione() != null) {
				setCell(row, (6 + colShift), lMod.getTempiArrivoIscrizione().doubleValue(), csBoldCenter);
			} else {
				setCell(row, (6 + colShift), "n.d.", csBoldCenter);
			}

			setCell(row, (7 + colShift), DateUtils.getDateToString(lMod.getDataPrimoAtto(), "dd/MM/yyyy"),
					csCenter);
			setCell(row, (8 + colShift), lMod.getDescrizioneMotivoPrimoAtto(), csCenter);
			setCell(row, (9 + colShift),
					DateUtils.getDateToString(lMod.getDataOrdineEsecuzione(), "dd/MM/yyyy"), csCenter);
			setCell(row, (10 + colShift), lMod.getDescrizioneMotivoEsecuzione(), csCenter);

			if (lMod.getTempoIscrizioneEmissione() != null) {
				setCell(row, (11 + colShift), lMod.getTempoIscrizioneEmissione().doubleValue(), csBoldCenter);
			} else {
				setCell(row, (11 + colShift), "n.d.", csBoldCenter);
			}

			setCell(row, (12 + colShift),
					DateUtils.getDateToString(lMod.getDataInizioIstruttoria(), "dd/MM/yyyy"), csCenter);
			setCell(row, (13 + colShift),
					DateUtils.getDateToString(lMod.getDataFineIstruttoria(), "dd/MM/yyyy"), csCenter);

			if (lMod.getTempiIstruttoria() != null) {
				setCell(row, (14 + colShift), lMod.getTempiIstruttoria().doubleValue(), csBoldCenter);
			} else {
				setCell(row, (14 + colShift), "n.d.", csBoldCenter);
			}

			setCell(row, (15 + colShift),
					DateUtils.getDateToString(lMod.getDataInizioInattivita(), "dd/MM/yyyy"), csCenter);
			setCell(row, (16 + colShift),
					DateUtils.getDateToString(lMod.getDataFineInattivita(), "dd/MM/yyyy"), csCenter);

			if (lMod.getTempiInattivita() != null) {
				setCell(row, (17 + colShift), lMod.getTempiInattivita().doubleValue(), csBoldCenter);
			} else {
				setCell(row, (17 + colShift), "n.d.", csBoldCenter);
			}

			if (lMod.getGiorniDaNeutralizzare() != null) {
				setCell(row, (18 + colShift), lMod.getGiorniDaNeutralizzare().doubleValue(), csBoldCenter);
			} else {
				setCell(row, (18 + colShift), "n.d.", csBoldCenter);
			}
			cont++;
			nRow++;
		}
	}

	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return cs;
	}

	private void setRowTotaliProvvedimenti(HSSFSheet sheet, int nRow, String tipoTot,
			HSSFCellStyle csBoldRight, HSSFCellStyle csNull, String formula) {

		HSSFRow row = sheet.createRow(nRow);

		setCell(row, 6, tipoTot, csNull);

		// Eliminata formula per i totali perche'
		// si sono dovuti gestire i progr fascicolo come stringhe
		// ed a causa delle limitazioni della libreria POI
		// nella gestione dei formati e delle formule

		setCell(row, 8, formula, csBoldRight);

	}

	private String getStringaSomma(int nRow1, int nCol1, int nRow2, int nCol2) {

		CellReference cellRef1 = new CellReference(nRow1, nCol1);
		CellReference cellRef2 = new CellReference(nRow2, nCol2);

		String formula = "SUM(" + cellRef1.formatAsString() + ":" + cellRef2.formatAsString() + ")";

		return formula;
	}

	// NGG - Nuova somma Totale Tipologie
	private String getStringaSommaTipo(int nRow1, int nCol1, int nRow2, int nCol2, int[] RigheBuone) {

		CellReference cellRef1 = new CellReference(nRow1, nCol1);
		String formula = "SUM(" + cellRef1.formatAsString();

		for (int k = 1; k < RigheBuone.length; k++) {
			cellRef1 = new CellReference(RigheBuone[k], nCol1);
			formula += "+" + cellRef1.formatAsString();
		}

		formula += ")";

		return formula;
	}

	private int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull) {

		int nRow = 0;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		// Create a cell and put a value in it.
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, 0, value, csNull);

		nRow++;
		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		// Create a cell and put a value in it.

		// [SG] 11/11/2021 se è null scrivo ""
		value = ("Tel. " + StringUtils.toStringJSP(uffUteConnesso.getTelefono()) + " - Fax "
				+ StringUtils.toStringJSP(uffUteConnesso.getFax()));
		setCell(row, 0, value, csNull);

		return nRow;
	}

	// Duplico setIntestazione per NGG Statistiche SIEP
	private int setIntestazione(HSSFSheet sheet, UfficioModel uffUteConnesso, HSSFCellStyle csNull,
			String DescUffIntesta) {

		int nRow = 0;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		// Create a cell and put a value in it.
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ DescUffIntesta.toUpperCase());
		setCell(row, 0, value, csNull);

		nRow++;
		// Create a row and put some cells in it. Rows are 0 based.
		row = sheet.createRow(nRow);
		// Create a cell and put a value in it.

		// [SG] 11/11/2021 se è null scrivo ""
		value = ("Tel. " + StringUtils.toStringJSP(uffUteConnesso.getTelefono()) + " - Fax "
				+ StringUtils.toStringJSP(uffUteConnesso.getFax()));
		setCell(row, 0, value, csNull);

		return nRow;
	}

	/**
	 * Scrive l'ultima colonna dei TOTALI inserendo le formule (SOMMA). Viene invocata per ogni 'Magistrato',
	 * quindi una volta sul foglio di Riepilogo Generale ed enne volte sul foglio di 'Dettaglio' dove sono
	 * presenti piu' magistrati Prendi in input il punto di partenza da cui iniziare a scrivere ovvero: riga =
	 * nRowAnno colonna = nColAnno
	 *
	 * @param sheet
	 * @param csBold
	 * @param nRow
	 *            - ultima riga scritta, quindi
	 * @param nRowAnno
	 *            - Riga in cui e' riportata la cella con al descrizione ANNO Sul 'Riepilogo Generale' e' in
	 *            genere la riga 8 ma sul 'Dettaglio' e' la riga in cui inizia ogni magistrato
	 * @param nColAnno
	 *            - numero della colonna in cui vanno scritti i totali
	 * @param RigheBuone
	 *            - Array dove sono riportate le posizioni delle righe con la Descr Tipologia
	 * @param style
	 * @param stylered
	 * @return
	 */
	private int setTotaliAttivitaMagistrati(HSSFSheet sheet, HSSFCellStyle csBold, int nRow, int nRowAnno,
			int nColAnno, int[] RigheBuone, HSSFCellStyle style, HSSFCellStyle stylered) {

		HSSFRow row = null;
		// HSSFCell cell = null;

		String somma = "";
		int forRow = 0;
		// ---> NGG --> Scrivo 'TOTALE' su ultima riga/colonna 0

		int RRiga = 0;
		RRiga = nRow;

		int coltot = 0;
		coltot = nColAnno;
		coltot = coltot - 1;

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		setCell(row, 0, "TOTALE", csBold);

		nRow = RRiga;

		// ---> END NGG

		// Ciclo per scrivere il totale per tipologia per tutti gli anni
		// tranne le righe dove c'e' "DI CUI"
		for (forRow = nRowAnno; forRow < nRow + 1; forRow++) {

			// row = sheet.createRow(forRow);
			row = sheet.getRow(forRow);
			if (row == null)
				row = sheet.createRow(forRow);

			String Campo = row.getCell(0).toString();

			/*
			 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato Numero MEV : SIES
			 * v10 Autore : gioggi Data : 03/feb/2016 Branch : MEV_SIES v10
			 */
			// if (!Campo.equals("DI CUI")) {
			// cell = row.createCell(nColAnno);
			// cell.setCellStyle(style);
			// }
			// ***** FINE INTERVENTO MEV_SIES v10 *****//

			if (forRow == nRowAnno) {
				setCell(row, nColAnno, "TOTALE", csBold);
			} else {
				// NGG

				if (!Campo.equals("DI CUI")) {
					somma = getStringaSomma(forRow, 1, forRow, nColAnno - 1);
					boolean rigaTipologia = false;
					for (int k = 0; k < RigheBuone.length; k++) {
						if (forRow == RigheBuone[k]) {
							// la riga corrente e' la riga del totale Tipologia
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug(" Campo = "+Campo);
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug(" forRow = "+forRow);
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug(" nColAnno = "+nColAnno);
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug(" RigheBuone[k] = "+ RigheBuone[k]);
							rigaTipologia = true;
							// setFormulaCell(row, nColAnno, somma, style);
							break;
						} else {
							// setFormulaCell(row, nColAnno, somma, stylered);
						}
					}

					if (rigaTipologia)
						setFormulaCell(row, nColAnno, somma, style); // blu
					else
						setFormulaCell(row, nColAnno, somma, stylered); // rosso

				}
			}
		}

		nRow++;

		// Totale generale
		// row = sheet.createRow(nRow);
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		// setCell(row, (nColAnno - 1), "TOTALE", csBold);

		for (int J = 1; J < (nColAnno); J++) {
			somma = getStringaSommaTipo(nRowAnno + 1, J, forRow - 1, J, RigheBuone);
			setFormulaCell(row, J, somma, style);
		}

		somma = getStringaSommaTipo(nRowAnno + 1, nColAnno, forRow - 1, nColAnno, RigheBuone);
		setFormulaCell(row, nColAnno, somma, style);

		return nRow;
	}

	private int setTotaliTempi(HSSFSheet sheet, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter, int nRow,
			int nRowAnno, int nColAnno) {

		HSSFRow row = null;
		HSSFCell cell = null;

		String somma = "";

		int forRow = 0;

		// Ciclo per scrivere il totale per tipologia per tutti gli anni
		for (forRow = nRowAnno; forRow < nRow + 1; forRow++) {

			// row = sheet.createRow(forRow);
			row = sheet.getRow(forRow);
			if (row == null)
				row = sheet.createRow(forRow);

			cell = row.createCell(nColAnno);
			cell.setCellStyle(csBold);

			if (forRow == nRowAnno) {
				setCell(row, nColAnno, "TOTALE", csBoldCenter);
			} else {

				somma = getStringaSomma(forRow, 1, forRow, nColAnno - 1);
				setFormulaCell(row, nColAnno, somma, csBold);
			}
		}

		return nRow;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCell setCellRichText(HSSFRow row, int nCol, HSSFRichTextString value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, double value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCell setFormulaCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		// cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(value);
		cell.setCellStyle(cs);

		return cell;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, String value) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);

		return cell;
	}

	// STATISTICHE SIUS
	/**
	 * Crea il foglio excel per il dettaglio dei tempi emissione
	 *
	 * @param aVect
	 *            Vettore con i dati dei conteggi per Oggetti
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @param tipo_estrazione
	 *            tipo di estrazione dati
	 * @param aRelatore
	 *            )
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateDettaglioOggettiSIUS(Vector aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			Date dataIni, Date dataFin, int tipo_estrazione,
			// MagistratoModel aMagistrato,
			String aRelatore, Vector aElencoProc, Vector aElencoProcMag, Vector aElencoFascPen,
			Vector aElencoOggettiPen, Vector aElencoFascUnificati, Vector aElencoOggettiCanc,
			Vector aElencoFascNoRelatore, Vector aStatisticheRelatori, String aDescOggetto,
			CancelleriaAssegnatariaModel aCancAss, String aFiltroCollab, String aFiltroPosGiurid)
			throws F3BException {

		int numCol = 0;

		boolean isAggregato = false;
		if (tipo_estrazione == 1)
			isAggregato = true;

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		// HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato e testo verticale
		HSSFCellStyle csVert = getBordo4Lati(wb);
		csVert.setFont(font);
		csVert.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csVert.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csVert.setRotation((short) 90);

		// foglio dettaglio
		String lNomeFoglio = "Dettaglio";
		if (isAggregato)
			lNomeFoglio = "Aggregato";

		HSSFSheet sheet = wb.createSheet(lNomeFoglio);

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (20 * 256));  // CONTENUTO
		// Nel caso di Aggregato non c'e' la colonna OGGETTO
		if (!isAggregato)
			sheet.setColumnWidth(numCol++, (30 * 256)); // OGGETTO
		sheet.setColumnWidth(numCol++, (10 * 256)); // Pendenti Inizio Periodo
		sheet.setColumnWidth(numCol++, (10 * 256)); // Sopravvenuti
		sheet.setColumnWidth(numCol++, (10 * 256));   // Accolti		
		// MEV9 - Si aggiunge Accolti Provvisoriamente
		sheet.setColumnWidth(numCol++, (10 * 256));   // Accolti Provvisoriamente
		// MEV9 - FINE		
		sheet.setColumnWidth(numCol++, (10 * 256));   // Rigettati
		sheet.setColumnWidth(numCol++, (10 * 256));   // Inammissibilita'
		sheet.setColumnWidth(numCol++, (10 * 256));   // NLP/NDP
		sheet.setColumnWidth(numCol++, (10 * 256));   // Incompetenza
		sheet.setColumnWidth(numCol++, (10 * 256)); // Iscritti per errore
		sheet.setColumnWidth(numCol++, (10 * 256)); // Unificati
		sheet.setColumnWidth(numCol++, (10 * 256)); // Cancellati
		sheet.setColumnWidth(numCol++, (10 * 256)); // Altro
		sheet.setColumnWidth(numCol++, (10 * 256)); // Pendenti Fine Periodo 

		// N.ro riga corrente
		int nRow = 0;
		// N.ro riga inizio dati
		int nRowIni;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		// setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") +
		// " relativo al periodo dal " + DateUtils.getDateToString(dataIni,"dd/MM/yyyy") + " al " +
		// DateUtils.getDateToString(dataFin,"dd/MM/yyyy") , csNull);
		// creazione riga con altezza per wraptext
		row = sheet.createRow(nRow);
		// row.setHeight(100);
		csBold.setWrapText(true);

		String lTitolo = "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ DateUtils.getDateToString(dataIni, "dd/MM/yyyy") + " al "
				+ DateUtils.getDateToString(dataFin, "dd/MM/yyyy");
		if (aFiltroCollab != null) {
			if (aFiltroCollab.equalsIgnoreCase("SI"))
				lTitolo += " ,Procedimenti collegati a Collaboratore di Giustizia";
			else if (aFiltroCollab.equalsIgnoreCase("NO"))
				lTitolo += " ,Procedimenti non collegati a Collaboratore di Giustizia";
		}
		if (aCancAss != null) {
			lTitolo += ", alla Cancelleria " + aCancAss.getDescCancelleriaAssegnataria();
		}
		if (aFiltroPosGiurid != null && aFiltroPosGiurid.compareTo("tutti") != 0) {
			lTitolo += ", per la posizione giuridica: " + aFiltroPosGiurid;
		}

		// Titolo ridotto (senza magistrato)
		String lTitolo1 = lTitolo;
		/*
		 * if (aMagistrato != null) { lTitolo += " ed agli Atti riferiti al Magistrato: "+
		 * aMagistrato.getCognome() + " " + aMagistrato.getNome(); }
		 */
		if (aRelatore != null) {
			lTitolo += " ed agli Atti riferiti al Magistrato: " + aRelatore;
		}
		setCell(row, 0, lTitolo, csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "CONTENUTO", csCenter);
		// Nel caso di Aggregato non c'e' la colonna OGGETTO
		if (!isAggregato)
			setCell(row, numCol++, "OGGETTO", csCenter);

		setCell(row, numCol++, "Pendenti Inizio Periodo", csCenter);
		setCell(row, numCol++, "Sopravvenuti", csCenter);
		setCell(row, numCol++, "Accolti", csCenter);
		// MEV9 - Si aggiunge Accolti Provvisoriamente
		setCell(row, numCol++, "Accolti Provvisoriamente", csCenter);
		// MEV9 - FINE
		setCell(row, numCol++, "Rigettati", csCenter);
		setCell(row, numCol++, "Inammissibilita'", csCenter);
		setCell(row, numCol++, "NLP/NDP", csCenter);
		setCell(row, numCol++, "Incompetenza", csCenter);
		setCell(row, numCol++, "Iscritti per errore", csCenter);
		setCell(row, numCol++, "Unificati", csCenter);
		setCell(row, numCol++, "Cancellati", csCenter);
		setCell(row, numCol++, "Altro", csCenter);
		setCell(row, numCol++, "Pendenti Fine Periodo", csCenter);

		nRow++;
		// Viene memorizzato il n.ro di riga dove iniziano i dati
		nRowIni = nRow;
		if (tipo_estrazione == 1)
			nRow = scriviAggregato(aVect, sheet, nRow, csCenter);
		else {
			// Creazione primo foglio Dettaglio.
			Iterator itx = aVect.iterator();
			// inizio ciclo di scrittura dei dati
			while (itx.hasNext()) {
				numCol = 0;

				IspConteggioOggettiModel lMod = (IspConteggioOggettiModel) itx.next();

				row = sheet.createRow(nRow++);
				setCell(row, numCol++, lMod.getDescContenutoStatis(), csCenter);
				setCell(row, numCol++, lMod.getDescOggetto(), csCenter);
				setCell(row, numCol++, lMod.getNumPendentiInizio().doubleValue(), csCenter);
				setCell(row, numCol++, lMod.getNumSopravvenuti().doubleValue(), csCenter);
				setCell(row, numCol++, lMod.getNumDefEsito1().doubleValue(), csCenter); // Accolti
				// MEV9 - Si aggiunge Accolti Provvisoriamente
				setCell(row, numCol++, lMod.getNumAccoltiProvv().doubleValue(), csCenter); // Accolti
				// MEV9 - FINE
				setCell(row, numCol++, lMod.getNumDefEsito2().doubleValue(), csCenter); // Rigettati
				setCell(row, numCol++, lMod.getNumDefEsito3().doubleValue(), csCenter); // Inammissibilita'
				setCell(row, numCol++, lMod.getNumDefEsito4().doubleValue(), csCenter); // NLP/NDP
				setCell(row, numCol++, lMod.getNumDefEsito5().doubleValue(), csCenter); // Incompetenza
				setCell(row, numCol++, lMod.getNumDefIscErr().doubleValue(), csCenter); // Iscritti per errore
				setCell(row, numCol++, lMod.getNumUnificati().doubleValue(), csCenter); // Unificati
				setCell(row, numCol++, lMod.getNumCancellati().doubleValue(), csCenter); // Cancellati
				setCell(row, numCol++, lMod.getNumDefEsito6().doubleValue(), csCenter); // Altro
				setCell(row, numCol++, lMod.getNumPendentiFine().doubleValue(), csCenter);
			}
		}
		// Riga TOTALI
		int nRowFine = nRow - 1;
		numCol = 0;
		String formula = "";
		// Nel caso di Aggregato non c'e' la colonna OGGETTO
		if (!isAggregato)
			numCol++;
		nRow++;
		row = sheet.createRow(nRow++);
		setCell(row, numCol++, "TOTALI", csCenter);
		// 12 totali su 12 colonne
		// MEV9 - Si aggiunge Accolti Provvisoriamente le colonne diventano 13
		//for (int i = 0; i < 12; i++) {
		for (int i = 0; i < 13; i++) {
			formula = getStringaSomma(nRowIni, numCol, nRowFine, numCol);
			setFormulaCell(row, numCol++, formula, csCenter);
		}

		// Creazione secondo foglio
		if (aElencoProc != null && aDescOggetto != null) {
			creaFoglioDettaglioOggetti(wb, uffUteConnesso, lTitolo, aElencoProc, dataIni, aDescOggetto);
		}

		// Creazione secondo foglio nel caso di un Magistrato e nessun oggetto scelto (2/3/2010)
		// if (aElencoProcMag != null && (aDescOggetto == null || aDescOggetto.length() ==0) && aRelatore !=
		// null)
		if (aElencoProcMag != null && aRelatore != null) {
			creaFoglioDettaglioOggettiDelMagistrato(wb, uffUteConnesso, lTitolo, aElencoProcMag, dataIni);
		}

		// Creazione foglio Totali per Magistrato e foglio Privi di Magistrato
		if (aStatisticheRelatori != null) {
			creaFoglioTotaliPerRelatore(wb, uffUteConnesso, lTitolo, aStatisticheRelatori, dataIni);
		}

		// Creazione foglio Elenco Fascicoli Pendenti
		if (aElencoFascPen != null) {
			creaFoglioFascicoliPendenti(wb, uffUteConnesso, lTitolo, aElencoFascPen, dataFin);
			creaFoglioContenutiPendenti(wb, uffUteConnesso, lTitolo, aElencoFascPen, dataFin); // #: ...
			creaFoglioOggettiPendenti(wb, uffUteConnesso, lTitolo, aElencoOggettiPen, dataFin); // #: ...
		}

		// Creazione foglio Elenco Fascicoli Unificati
		if (aElencoFascUnificati != null) {
			creaFoglioFascicoliUnificati(wb, uffUteConnesso, lTitolo, aElencoFascUnificati, dataFin);
		}

		// Creazione foglio Elenco Oggetti Cancellati
		if (aElencoOggettiCanc != null) {
			creaFoglioOggettiCancellati(wb, uffUteConnesso, lTitolo, aElencoOggettiCanc, dataFin);
		}

		// Creazione foglio Elenco Fascicoli Privi Di Magistrato Assegnato
		// aElencoFascNoRelatore
		if (aElencoFascNoRelatore != null) {
			creaFoglioFascicoliPriviDiRelatore(wb, uffUteConnesso, lTitolo1, aElencoFascNoRelatore, dataFin);
		}
	}

	private int scriviAggregato(Vector aVect, HSSFSheet aSheet, int aNumRowRow, HSSFCellStyle aCsCenter)
			throws F3BException {

		int numCol;
		HSSFRow row;
		int lSizeVect = aVect.size();
		if (lSizeVect > 0) {
			Iterator itx = aVect.iterator();

			// Prima lettura fuori ciclo
			IspConteggioOggettiModel lMod = (IspConteggioOggettiModel) itx.next();

			// inizio ciclo di scrittura dei dati
			while (lMod != null) {
				IspConteggioOggettiModel lModNext = null;
				// Lettura del prossimo
				if (itx.hasNext())
					lModNext = (IspConteggioOggettiModel) itx.next();

				// Confronto del nuovo record letto con l'ultimo

				// MAC 2017/04/11
				// aggiunta condizione "lMod.getDescContenutoStatis() != null"
				// per risolvere errore NullPointerException segnalato da Michele
				if (lModNext != null && lMod.getDescContenutoStatis() != null
						&& lMod.getDescContenutoStatis().equalsIgnoreCase(lModNext.getDescContenutoStatis()))
					lMod = lMod.add(lModNext);
				else {
					// Scrittura riga
					numCol = 0;

					row = aSheet.createRow(aNumRowRow++);
					setCell(row, numCol++, lMod.getDescContenutoStatis(), aCsCenter);
					setCell(row, numCol++, lMod.getNumPendentiInizio().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumSopravvenuti().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumDefEsito1().doubleValue(), aCsCenter);
					// MEV9 - Accolti provvisoriamente
					setCell(row, numCol++, lMod.getNumAccoltiProvv().doubleValue(), aCsCenter);
					// MEV9 - FINE
					setCell(row, numCol++, lMod.getNumDefEsito2().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumDefEsito3().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumDefEsito4().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumDefEsito5().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumDefIscErr().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumUnificati().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumCancellati().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumDefEsito6().doubleValue(), aCsCenter);
					setCell(row, numCol++, lMod.getNumPendentiFine().doubleValue(), aCsCenter);

					// Si aggiorna il model per il prossimo ciclo
					lMod = lModNext;
				}
			}
		}
		return aNumRowRow;
	}

	/**
	 * La funzione prepara la pagina xls di ulteriore dettaglio sulla stastistica dei procedimenti per oggetto
	 * ed in un periodo di riferimento. Il report preparato contiene l'elenco dei Procedimenti SIUS relativi
	 * ad un oggetto specifico pendenti o sopravvenuti nel periodo di riferimento. L'elenco dei procedimenti
	 * nella lista e' quello passato attraverso il parametro aElencoProc. Per ogni elemento nella lista viene
	 * riportato il suo stato di pendente, sopravvenuto, definito.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataIni
	 * @param aDescOggetto
	 */
	private void creaFoglioDettaglioOggetti(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, Date adataIni, String aDescOggetto) {

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ElencoProc.PerOggetto");
		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle con carattere grassetto
		// HSSFCellStyle csBold = csNull;
		// Create a new font and alter it.
		// HSSFFont font = wb.createFont();
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// csBold.setFont(font);

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco procedimenti relativi ad oggetto: " + aDescOggetto, csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Procedimento", csCenter);
		setCell(row, numCol++, "Data di Iscrizione", csCenter);
		setCell(row, numCol++, "Data di Definizione", csCenter);
		setCell(row, numCol++, "Data di Deposito", csCenter);
		setCell(row, numCol++, "Provvedimento", csCenter);
		setCell(row, numCol++, "Pendente Inizio Periodo", csCenter);
		setCell(row, numCol++, "Sopravvenuto", csCenter);
		setCell(row, numCol++, "Definito", csCenter);
		setCell(row, numCol++, "Pendente Fine Periodo", csCenter);

		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			String lDataIscrizione = "-";
			String lDataDeposito = "-";
			String lDataDefinizione = "-";

			if (lProcEstrModel.getFasSiuDataIscrizione() != null)
				lDataIscrizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataIscrizione(),
						"dd/MM/yyyy");
			if (lProcEstrModel.getDataDeposito() != null)
				lDataDeposito = DateUtils.getDateToString(lProcEstrModel.getDataDeposito(), "dd/MM/yyyy");
			if (lProcEstrModel.getFasSiuDataDefinizione() != null)
				lDataDefinizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataDefinizione(),
						"dd/MM/yyyy");

			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
					+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csCenter);
			setCell(row, numCol++, lDataIscrizione, csCenter);
			setCell(row, numCol++, lDataDefinizione, csCenter);
			setCell(row, numCol++, lDataDeposito, csCenter);

			// Provvedimento: Decreto / Ordinanza
			if (lProcEstrModel.getDepDecIdDepositoDecreto() != null)
				setCell(row, numCol++, "Decreto", csCenter);
			else if (lProcEstrModel.getDepOpidDepositoOrdinanzaPc() != null)
				setCell(row, numCol++, "Ordinanza", csCenter);
			else
				setCell(row, numCol++, "-", csCenter);

			// Determinazione se Pendente inizio periodo o Sopravvenuto
			if (lProcEstrModel.getTenDataIns() == null) {
				setCell(row, numCol++, "-", csCenter);
				setCell(row, numCol++, "-", csCenter);
			} else if (lProcEstrModel.getTenDataIns().before(adataIni)) {
				setCell(row, numCol++, "si", csCenter);
				setCell(row, numCol++, "no", csCenter);
			} else {
				setCell(row, numCol++, "no", csCenter);
				setCell(row, numCol++, "si", csCenter);
			}

			// Determinazione se Definito o "Pendente fine periodo"
			if (lProcEstrModel.getDefinito() == null) {
				setCell(row, numCol++, "-", csCenter);
				setCell(row, numCol++, "-", csCenter);
			} else if (lProcEstrModel.getDefinito().equalsIgnoreCase("S")) {
				setCell(row, numCol++, "si" + " : " + lProcEstrModel.getDescrEsitoStatistica(), csCenter);
				setCell(row, numCol++, "no", csCenter);
			} else {
				setCell(row, numCol++, "no", csCenter);
				setCell(row, numCol++, "si", csCenter);
			}
		}
	}

	/**
	 * La funzione prepara la pagina xls di ulteriore dettaglio sulla stastistica dei procedimenti del
	 * Magistrato ordinati per oggetto ed in un periodo di riferimento. Il report preparato contiene l'elenco
	 * dei Procedimenti SIUS di un Magistrato ordinati per oggetto specifico pendenti o sopravvenuti nel
	 * periodo di riferimento. L'elenco dei procedimenti nella lista e' quello passato attraverso il parametro
	 * aElencoProc. Per ogni elemento nella lista viene riportato il suo stato di pendente, sopravvenuto,
	 * definito.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataIni
	 */
	private void creaFoglioDettaglioOggettiDelMagistrato(HSSFWorkbook wb, UfficioModel aUuffUteConnesso,
			String aTitolo, Vector aElencoProc, Date adataIni) {

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ElencoProcedimentiMagistrato");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco procedimenti relativi a tutti gli oggetti ", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (30 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Oggetto", csCenter);
		setCell(row, numCol++, "Procedimento", csCenter);
		setCell(row, numCol++, "Data di Iscrizione", csCenter);
		setCell(row, numCol++, "Data di Definizione", csCenter);
		setCell(row, numCol++, "Data di Deposito", csCenter);
		setCell(row, numCol++, "Provvedimento", csCenter);
		setCell(row, numCol++, "Pendente Inizio Periodo", csCenter);
		setCell(row, numCol++, "Sopravvenuto", csCenter);
		setCell(row, numCol++, "Definito", csCenter);
		setCell(row, numCol++, "Pendente Fine Periodo", csCenter);

		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			String lDataIscrizione = "-";
			String lDataDeposito = "-";
			String lDataDefinizione = "-";
			String lDescrOggetto = "-";

			if (lProcEstrModel.getDescrOggettoTenore() != null)
				lDescrOggetto = lProcEstrModel.getDescrOggettoTenore();
			if (lProcEstrModel.getFasSiuDataIscrizione() != null)
				lDataIscrizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataIscrizione(),
						"dd/MM/yyyy");
			if (lProcEstrModel.getDataDeposito() != null)
				lDataDeposito = DateUtils.getDateToString(lProcEstrModel.getDataDeposito(), "dd/MM/yyyy");
			if (lProcEstrModel.getFasSiuDataDefinizione() != null)
				lDataDefinizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataDefinizione(),
						"dd/MM/yyyy");

			// String lOggettoEstratto = lProcEstrModel.getDescrOggettoTenore();

			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lDescrOggetto, csCenter);
			setCell(row, numCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
					+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csCenter);
			setCell(row, numCol++, lDataIscrizione, csCenter);
			setCell(row, numCol++, lDataDefinizione, csCenter);
			setCell(row, numCol++, lDataDeposito, csCenter);

			// Provvedimento: Decreto / Ordinanza
			if (lProcEstrModel.getDepDecIdDepositoDecreto() != null)
				setCell(row, numCol++, "Decreto", csCenter);
			else if (lProcEstrModel.getDepOpidDepositoOrdinanzaPc() != null)
				setCell(row, numCol++, "Ordinanza", csCenter);
			else
				setCell(row, numCol++, "-", csCenter);

			// Determinazione se Pendente inizio periodo o Sopravvenuto
			if (lProcEstrModel.getTenDataIns() == null) {
				setCell(row, numCol++, "-", csCenter);
				setCell(row, numCol++, "-", csCenter);
			} else if (lProcEstrModel.getTenDataIns().before(adataIni)) {
				setCell(row, numCol++, "si", csCenter);
				setCell(row, numCol++, "no", csCenter);
			} else {
				setCell(row, numCol++, "no", csCenter);
				setCell(row, numCol++, "si", csCenter);
			}

			// Determinazione se Definito o "Pendente fine periodo"
			if (lProcEstrModel.getDefinito() == null) {
				setCell(row, numCol++, "-", csCenter);
				setCell(row, numCol++, "-", csCenter);
			} else if (lProcEstrModel.getDefinito().equalsIgnoreCase("S")) {
				setCell(row, numCol++, "si" + " : " + lProcEstrModel.getDescrEsitoStatistica(), csCenter);
				setCell(row, numCol++, "no", csCenter);
			} else {
				setCell(row, numCol++, "no", csCenter);
				setCell(row, numCol++, "si", csCenter);
			}
		}
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fascicoli Privi di Relatore alla fine periodo.
	 * L'elenco dei procedimenti nella lista e' quello passato attraverso il parametro aElencoProcNoRel. Per
	 * ogni elemento nella lista viene riportato il suo stato di pendente, sopravvenuto, definito.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataIni
	 * @param aDescOggetto
	 */
	private void creaFoglioFascicoliPriviDiRelatore(HSSFWorkbook wb, UfficioModel aUuffUteConnesso,
			String aTitolo, Vector aElencoProcNoRel, Date adataFine) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliPriviDiRelatore : inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vector size : " + aElencoProcNoRel.size());

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("Elenco oggetti privi Magistrato");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco oggetti privi di Magistrato al : "
				+ DateUtils.getDateToString(adataFine, "dd/MM/yyyy"), csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (30 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Procedimento", csCenter);
		setCell(row, numCol++, "Data di Iscrizione", csCenter);
		setCell(row, numCol++, "Data di Definizione", csCenter);
		setCell(row, numCol++, "Data di Deposito", csCenter);
		setCell(row, numCol++, "Provvedimento", csCenter);
		setCell(row, numCol++, "Pendente Inizio Periodo", csCenter);
		setCell(row, numCol++, "Sopravvenuto", csCenter);
		setCell(row, numCol++, "Definito", csCenter);
		setCell(row, numCol++, "Pendente Fine Periodo", csCenter);
		setCell(row, numCol++, "Oggetto", csCenter);

		Iterator itx = aElencoProcNoRel.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			String lDataIscrizione = "-";
			String lDataDeposito = "-";
			String lDataDefinizione = "-";

			if (lProcEstrModel.getFasSiuDataIscrizione() != null)
				lDataIscrizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataIscrizione(),
						"dd/MM/yyyy");
			if (lProcEstrModel.getDataDeposito() != null)
				lDataDeposito = DateUtils.getDateToString(lProcEstrModel.getDataDeposito(), "dd/MM/yyyy");
			if (lProcEstrModel.getFasSiuDataDefinizione() != null)
				lDataDefinizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataDefinizione(),
						"dd/MM/yyyy");

			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
					+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csCenter);
			setCell(row, numCol++, lDataIscrizione, csCenter);
			setCell(row, numCol++, lDataDefinizione, csCenter);
			setCell(row, numCol++, lDataDeposito, csCenter);

			// Provvedimento: Decreto / Ordinanza
			if (lProcEstrModel.getDepDecIdDepositoDecreto() != null)
				setCell(row, numCol++, "Decreto", csCenter);
			else if (lProcEstrModel.getDepOpidDepositoOrdinanzaPc() != null)
				setCell(row, numCol++, "Ordinanza", csCenter);
			else
				setCell(row, numCol++, "-", csCenter);

			// Determinazione se Pendente inizio periodo o Sopravvenuto
			if (lProcEstrModel.getTenDataIns() == null) {
				setCell(row, numCol++, "-", csCenter);
				setCell(row, numCol++, "-", csCenter);
			} else if (lProcEstrModel.getTenDataIns().before(adataFine)) {
				setCell(row, numCol++, "si", csCenter);
				setCell(row, numCol++, "no", csCenter);
			} else {
				setCell(row, numCol++, "no", csCenter);
				setCell(row, numCol++, "si", csCenter);
			}

			// Determinazione se Definito o "Pendente fine periodo"
			if (lProcEstrModel.getDefinito() == null) {
				setCell(row, numCol++, "-", csCenter);
				setCell(row, numCol++, "-", csCenter);
			} else if (lProcEstrModel.getDefinito().equalsIgnoreCase("S")) {
				setCell(row, numCol++, "si" + " : " + lProcEstrModel.getDescrEsitoStatistica(), csCenter);
				setCell(row, numCol++, "no", csCenter);
			} else {
				setCell(row, numCol++, "no", csCenter);
				setCell(row, numCol++, "si", csCenter);
			}

			setCell(row, numCol++,
					DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),
							lProcEstrModel.getCodOggettoTenore()),
					csCenter);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliPriviDiRelatore : fine");
	}

	/**
	 * La funzione prepara la pagina xls di quadro riassuntivo sulla stastistica dei procedimenti totali per
	 * magistrato ed in un periodo di riferimento. L'elenco dei procedimenti nella lista e' quello passato
	 * attraverso il parametro aTotaliRelatore.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aTotaliRelatore
	 * @param adataIni
	 * @param aDescOggetto
	 */
	private void creaFoglioTotaliPerRelatore(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aTotaliRelatore, Date adataIni) throws F3BException {

		int numCol = 0;

		HSSFSheet sheet = wb.createSheet("Totali per magistrato");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;
		int nRowIni;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		nRow++;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (40 * 256)); // 
		sheet.setColumnWidth(numCol++, (10 * 256)); // 
		sheet.setColumnWidth(numCol++, (10 * 256)); // 
		sheet.setColumnWidth(numCol++, (10 * 256)); // 
		sheet.setColumnWidth(numCol++, (10 * 256)); // Accolti Provvisoriamente - MEV 9
		sheet.setColumnWidth(numCol++, (10 * 256)); // 
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Magistrato", csCenter);
		setCell(row, numCol++, "Pendenti Inizio Periodo", csCenter);
		setCell(row, numCol++, "Sopravvenuti", csCenter);
		setCell(row, numCol++, "Accolti", csCenter);
		setCell(row, numCol++, "Accolti Provvisoriamente", csCenter); // MEV 9
		setCell(row, numCol++, "Rigettati", csCenter);
		setCell(row, numCol++, "Inammissibilita'", csCenter);
		setCell(row, numCol++, "NLP/NDP", csCenter);
		setCell(row, numCol++, "Incompetenza", csCenter);
		setCell(row, numCol++, "Iscritti per errore", csCenter);
		setCell(row, numCol++, "Unificati", csCenter);
		setCell(row, numCol++, "Cancellati", csCenter);
		setCell(row, numCol++, "Altro", csCenter);
		setCell(row, numCol++, "Pendenti Fine Periodo", csCenter);

		Iterator itx = aTotaliRelatore.iterator();
		// Iterator itx = aVect.iterator();
		// inizio ciclo di scrittura dei dati
		nRowIni = nRow;
		String codRelatore = null;
		String nomeRelatore = null;
		IStatisticheSius mRel = null;

		MagistratoModel lMagistrato = null;
		EspertoModel lEsperto = null;

		mRel = SIUSLookupRemote.getStatisticheSiusRemote();

		while (itx.hasNext()) {
			numCol = 0;

			IspConteggioRelatoriModel lMod = (IspConteggioRelatoriModel) itx.next();
			codRelatore = lMod.getCodRelatore();

			if (codRelatore.compareTo("0") != 0) {
				if (codRelatore.length() < 9) {
					// Ricerca del Magistrato
					lMagistrato = mRel.ExRicercaMagistratoByCod(codRelatore);
					nomeRelatore = lMagistrato.getCognome() + " " + lMagistrato.getNome();
				} else {
					if (codRelatore.compareTo("privi di magistrato") == 0) {
						// Conteggio dei Procedimenti ancora privi di magistrato
						nomeRelatore = "Procedimenti privi di magistrato";
					} else {
						// Ricerca dell' Esperto
						lEsperto = mRel.ExRicercaEspertoByCod(codRelatore);
						nomeRelatore = lEsperto.getCognome() + " " + lEsperto.getNome() + " (ESPERTO)";
					}
				}

			}

			row = sheet.createRow(nRow++);

			setCell(row, numCol++, nomeRelatore, csNull);
			setCell(row, numCol++, lMod.getNumPendentiInizio().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumSopravvenuti().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumDefEsito1().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumAppProvv().doubleValue(), csCenter); // MEV 9			
			setCell(row, numCol++, lMod.getNumDefEsito2().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumDefEsito3().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumDefEsito4().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumDefEsito5().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumDefIscErr().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumUnificati().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumCancellati().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumDefEsito6().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumPendentiFine().doubleValue(), csCenter);
		}

		// Riga TOTALI
		int nRowFine = nRow - 1;
		numCol = 0;
		String formula = "";
		// Nel caso di Aggregato non c'e' la colonna OGGETTO

		nRow++;
		row = sheet.createRow(nRow++);
		setCell(row, numCol++, "TOTALI", csCenter);
		// MEV9 Sono diventati 13
		// 12 totali su 12 colonne
		//for (int i = 0; i < 12; i++) {
		for (int i = 0; i < 13; i++) {	
			formula = getStringaSomma(nRowIni, numCol, nRowFine, numCol);
			setFormulaCell(row, numCol++, formula, csCenter);
		}
	}

	/**
	 * Crea il foglio excel per il dettaglio dei tempi emissione
	 *
	 * @param aVect
	 *            Vettore con i dati dei conteggi estratti dalla tabella ISP_CONTEGGIO_TEMPI
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo temporale
	 * @param dataFin
	 *            Fine intervallo temporale
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreaReportTempiSIUS(HSSFWorkbook wb, UfficioModel uffUteConnesso, Date dataIni,
			Date dataFin, String aRelatore, Vector aElencoProc, String aDescOggetto,
			CancelleriaAssegnatariaModel aCancAss, String aFiltroCollab, Vector... aVectors)
			throws F3BException {

		int numCol = 0;

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		// HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato e testo verticale
		HSSFCellStyle csVert = getBordo4Lati(wb);
		csVert.setFont(font);
		csVert.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csVert.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csVert.setRotation((short) 90);

		// foglio dettaglio
		String lNomeFoglio = "Dettaglio";

		HSSFSheet sheet = wb.createSheet(lNomeFoglio);

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (30 * 256));
		sheet.setColumnWidth(numCol++, (30 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// N.ro riga corrente
		int nRow = 0;
		// N.ro riga inizio dati
		// int nRowIni;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		// creazione riga con altezza per wraptext
		row = sheet.createRow(nRow);
		// row.setHeight(100);
		csBold.setWrapText(true);
		String lTitolo = "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ DateUtils.getDateToString(dataIni, "dd/MM/yyyy") + " al "
				+ DateUtils.getDateToString(dataFin, "dd/MM/yyyy");

		if (aFiltroCollab != null) {
			if (aFiltroCollab.equalsIgnoreCase("SI"))
				lTitolo += " ,Procedimenti collegati a Collaboratore di Giustizia";
			else if (aFiltroCollab.equalsIgnoreCase("NO"))
				lTitolo += " ,Procedimenti non collegati a Collaboratore di Giustizia";
		}

		if (aCancAss != null) {
			lTitolo += ", alla Cancelleria " + aCancAss.getDescCancelleriaAssegnataria();
		}

		if (aRelatore != null) {
			lTitolo += " ed agli Atti riferiti al Magistrato: " + aRelatore;
		}

		setCell(row, 0, lTitolo, csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "CONTENUTO", csCenter);
		setCell(row, numCol++, "OGGETTO", csCenter);
		setCell(row, numCol++, "Intervallo temporale", csCenter);
		setCell(row, numCol++, "Entro i 10 gg", csCenter);
		setCell(row, numCol++, "Da 11 a 30 gg", csCenter);
		setCell(row, numCol++, "Da 31 a 45 gg", csCenter);
		setCell(row, numCol++, "Da 46 a 60 gg", csCenter);
		setCell(row, numCol++, "Da 61 a 120 gg", csCenter);
		setCell(row, numCol++, "Oltre i 120 gg", csCenter);
		setCell(row, numCol++, "Durata media", csCenter);
		setCell(row, numCol++, "Totale procedimenti", csCenter);

		nRow++;
		// Viene memorizzato il n.ro di riga dovo iniziano i dati
		// nRowIni = nRow;
		// 20131124 - Iterazione sull'insieme di IspConteggioTempiModel.
		Iterator itx = aVectors[0].iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;
			IspConteggioTempiModel lMod = (IspConteggioTempiModel) itx.next();

			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lMod.getDescContenutoStatis(), csCenter);
			setCell(row, numCol++, lMod.getDescOggetto(), csCenter);
			setCell(row, numCol++, lMod.getDescIntervallo(), csCenter);
			setCell(row, numCol++, lMod.getNumTempo1().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumTempo2().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumTempo3().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumTempo4().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumTempo5().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumTempo6().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getDurataMedia().doubleValue(), csCenter);
			setCell(row, numCol++, lMod.getNumTotale().doubleValue(), csCenter);
		}

		/*
		 * // Riga TOTALI int nRowFine = nRow -1; // Si inizia dalla 3za colonna numCol = 2; String formula =
		 * ""; nRow++; row = sheet.createRow(nRow++); setCell(row, numCol++, "TOTALI", csCenter); // 8 totali
		 * su 8 colonne for (int i = 0; i <8; i++) { if( i == 6) // Si salta la colonna "Durata Media"
		 * numCol++; else { formula = getStringaSomma(nRowIni, numCol, nRowFine, numCol); setFormulaCell(row,
		 * numCol++, formula, csCenter); } }
		 */

		// Creazione secondo foglio. 20131123 - terzo foglio
		if (aElencoProc != null && aDescOggetto != null) {
			creaFoglioDettaglioTempi(wb, uffUteConnesso, lTitolo, aElencoProc, aDescOggetto);

		}
		// 20131123 - creazione secondo (o terzo) foglio
		// se esiste un secondo elemento, invoca il metodo preposto
		if (aVectors.length > 1)
			if (aVectors[1] != null)
				this.creaFoglioDettaglioGiorniIntercorsi(wb, uffUteConnesso, lTitolo, aVectors[1],
						aDescOggetto);
	}

	/**
	 * La funzione prepara la pagina xls di ulteriore dettaglio sulla stastistica relativa alla
	 * "Movimentazione di procedimenti SIUS" Il report preparato contiene l'elenco dei Procedimenti SIUS
	 * relativi ad un oggetto specifico definiti nel periodo di riferimento. L'elenco dei procedimenti nella
	 * lista e' quello passato attraverso il parametro aElencoProc. Per ogni elemento nella lista vengono
	 * riportati i suoi tempi caratteristici di movimentazione.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param aDescOggetto
	 */
	private void creaFoglioDettaglioTempi(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, String aDescOggetto) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## creaFoglioDettaglioTempi - START ####### ");

		int numCol = 0;
		IspProcIntervalliModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ElencoProc.PerOggetto");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco procedimenti relativi ad oggetto: " + aDescOggetto, csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Procedimento", csCenter);
		setCell(row, numCol++, "Data di Ricezione", csCenter);
		setCell(row, numCol++, "Data Fissazione Udienza 1", csCenter);
		setCell(row, numCol++, "Data Fissazione Udienza 2", csCenter);
		setCell(row, numCol++, "Data di Decisione", csCenter);
		setCell(row, numCol++, "Data di Deposito", csCenter);
		setCell(row, numCol++, "Ricezione a Fissazione 1 (gg)", csCenter);
		setCell(row, numCol++, "Fissazione 2 a Deposito (gg)", csCenter);
		setCell(row, numCol++, "Ricezione a Deposito (gg)", csCenter);
		setCell(row, numCol++, "Decisione a Deposito (gg)", csCenter);

		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			// Inizializzazione Date
			lProcEstrModel = (IspProcIntervalliModel) itx.next();
			String lDataRicezione = "-";
			String lDataFissazione1 = "-";
			String lDataFissazione2 = "-";
			String lDataDeposito = "-";
			String lDataDecisione = "-";

			// Formattazione Date
			if (lProcEstrModel.getDataRicezione() != null)
				lDataRicezione = DateUtils.getDateToString(lProcEstrModel.getDataRicezione(), "dd/MM/yyyy");
			if (lProcEstrModel.getDataDeposito() != null)
				lDataDeposito = DateUtils.getDateToString(lProcEstrModel.getDataDeposito(), "dd/MM/yyyy");
			if (lProcEstrModel.getDataDecisione() != null)
				lDataDecisione = DateUtils.getDateToString(lProcEstrModel.getDataDecisione(), "dd/MM/yyyy");
			if (lProcEstrModel.getDataPrimaUdienza() != null)
				lDataFissazione1 = DateUtils.getDateToString(lProcEstrModel.getDataPrimaUdienza(),
						"dd/MM/yyyy");
			if (lProcEstrModel.getDataUltimaUdienza() != null)
				lDataFissazione2 = DateUtils.getDateToString(lProcEstrModel.getDataUltimaUdienza(),
						"dd/MM/yyyy");

			// Lettura tempi
			String lTempoRicFiss1 = (lProcEstrModel.getTepoRicezioneFissazione1() != null)
					? lProcEstrModel.getTepoRicezioneFissazione1().toString()
					: "-";
			String lTempoRicDep = (lProcEstrModel.getTempoRicezioneDeposito() != null)
					? lProcEstrModel.getTempoRicezioneDeposito().toString()
					: "-";
			String lTempoFiss2Dep = (lProcEstrModel.getTempoFissazione2Deposito() != null)
					? lProcEstrModel.getTempoFissazione2Deposito().toString()
					: "-";
			String lTempoDecDep = (lProcEstrModel.getTempoDecisioneDeposito() != null)
					? lProcEstrModel.getTempoDecisioneDeposito().toString()
					: "-";

			// Scrittura riga del report
			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
					+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csCenter);
			setCell(row, numCol++, lDataRicezione, csCenter);
			setCell(row, numCol++, lDataFissazione1, csCenter);
			setCell(row, numCol++, lDataFissazione2, csCenter);
			setCell(row, numCol++, lDataDecisione, csCenter);
			setCell(row, numCol++, lDataDeposito, csCenter);
			setCell(row, numCol++, lTempoRicFiss1, csCenter);
			setCell(row, numCol++, lTempoFiss2Dep, csCenter);
			setCell(row, numCol++, lTempoRicDep, csCenter);
			setCell(row, numCol++, lTempoDecDep, csCenter);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######## creaFoglioDettaglioTempi - STOP ####### ");
	}

	/**
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param aDescOggetto
	 */
	private void creaFoglioDettaglioGiorniIntercorsi(HSSFWorkbook wb, UfficioModel aUuffUteConnesso,
			String aTitolo, Vector<IspProcIntervalliModel> aElencoProc, String aDescOggetto) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### creaFoglioDettaglioGiorniIntercorsi START ########");

		int numCol = 0;
		IspProcIntervalliModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ElencoProc.GG.Intercorsi");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);
		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco procedimenti ordinati per oggetto e n.ro giorni trascorsi tra data ricezione e data deposito.",
				csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (15 * 256));
		sheet.setColumnWidth(numCol++, (25 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (30 * 256));
		sheet.setColumnWidth(numCol++, (30 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Procedimento", csCenter);
		setCell(row, numCol++, "Oggetto", csCenter);
		setCell(row, numCol++, "Ricezione a Deposito (gg)", csCenter);
		setCell(row, numCol++, "Decisione a Deposito (gg)", csCenter);
		setCell(row, numCol++, "Ricezione a Fissazione 1 (gg)", csCenter);
		setCell(row, numCol++, "Fissazione 2 a Deposito (gg)", csCenter);
		setCell(row, numCol++, "Data di Ricezione", csCenter);
		setCell(row, numCol++, "(tbv) Data Prima Udienza ", csCenter);
		setCell(row, numCol++, "(tbv) Data Ultima Udienza ", csCenter);
		setCell(row, numCol++, "Data di Decisione", csCenter);
		setCell(row, numCol++, "Data di Deposito", csCenter);
		setCell(row, numCol++, "Magistrato", csCenter);
		setCell(row, numCol++, "Esito", csCenter);

		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			// Inizializzazione Date
			lProcEstrModel = (IspProcIntervalliModel) itx.next();
			String lDataRicezione = "-";
			String lDataPrimaUdienza = "-";
			String lDataUltimaUdienza = "-";
			String lDataDeposito = "-";
			String lDataDecisione = "-";
			String lOggetto = "-";
			String lTempoRicDep = "-";
			String lTempoRicFiss1 = "-";
			String lTempoDecDep = "-";
			String lTempoFiss2Dep = "-";
			String lCodMagistrato = "-";
			String lEsito = "-";

			// Formattazione Date
			if (lProcEstrModel.getDataRicezione() != null)
				lDataRicezione = DateUtils.getDateToString(lProcEstrModel.getDataRicezione(), "dd/MM/yyyy");
			if (lProcEstrModel.getDataDeposito() != null)
				lDataDeposito = DateUtils.getDateToString(lProcEstrModel.getDataDeposito(), "dd/MM/yyyy");
			if (lProcEstrModel.getDataDecisione() != null)
				lDataDecisione = DateUtils.getDateToString(lProcEstrModel.getDataDecisione(), "dd/MM/yyyy");
			if (lProcEstrModel.getDataPrimaUdienza() != null)
				lDataPrimaUdienza = DateUtils.getDateToString(lProcEstrModel.getDataPrimaUdienza(),
						"dd/MM/yyyy");
			if (lProcEstrModel.getDataUltimaUdienza() != null)
				lDataUltimaUdienza = DateUtils.getDateToString(lProcEstrModel.getDataUltimaUdienza(),
						"dd/MM/yyyy");

			// Oggetto
			lOggetto = (lProcEstrModel.getDescrOggettoTenore() != null)
					? lProcEstrModel.getDescrOggettoTenore().toString()
					: "-";

			// Lettura tempi
			lTempoRicDep = (lProcEstrModel.getTempoRicezioneDeposito() != null)
					? lProcEstrModel.getTempoRicezioneDeposito().toString()
					: "-";
			lTempoDecDep = (lProcEstrModel.getTempoDecisioneDeposito() != null)
					? lProcEstrModel.getTempoDecisioneDeposito().toString()
					: "-";
			lTempoRicFiss1 = (lProcEstrModel.getTepoRicezioneFissazione1() != null)
					? lProcEstrModel.getTepoRicezioneFissazione1().toString()
					: "-";
			lTempoFiss2Dep = (lProcEstrModel.getTempoFissazione2Deposito() != null)
					? lProcEstrModel.getTempoFissazione2Deposito().toString()
					: "-";

			// Magistrato
			lCodMagistrato = (lProcEstrModel.getDescrMagistrato() != null)
					? lProcEstrModel.getDescrMagistrato()
					: "-";
			// COLLAUDO 11.3 (TERZA SESSIONE): INTEGRO LA STRING 'ESPERTO' SE TRATTASI DI UN MAGISTRATO
			// ESPERTO
			if (lCodMagistrato != null && lProcEstrModel.getCodMagistrato().contains("ESPERTO")) {
				lCodMagistrato = lCodMagistrato + " (ESPERTO) ";
			}

			// Esito
			lEsito = (lProcEstrModel.getDescrEsitoTenore() != null)
					? lProcEstrModel.getDescrEsitoTenore().toString()
					: "-";

			// Scrittura riga del report
			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
					+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csCenter);
			setCell(row, numCol++, lOggetto, csCenter);
			setCell(row, numCol++, lTempoRicDep, csCenter);
			setCell(row, numCol++, lTempoDecDep, csCenter);
			setCell(row, numCol++, lTempoRicFiss1, csCenter);
			setCell(row, numCol++, lTempoFiss2Dep, csCenter);
			setCell(row, numCol++, lDataRicezione, csCenter);
			setCell(row, numCol++, lDataPrimaUdienza, csCenter);
			setCell(row, numCol++, lDataUltimaUdienza, csCenter);
			setCell(row, numCol++, lDataDecisione, csCenter);
			setCell(row, numCol++, lDataDeposito, csCenter);
			setCell(row, numCol++, lCodMagistrato, csCenter);
			setCell(row, numCol++, lEsito, csCenter);

		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######### creaFoglioDettaglioGiorniIntercorsi STOP ########");
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei provvedimenti cercati L'elenco dei provvedimenti
	 * nella lista e' quello passato attraverso il parametro aElencoProc.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param aDescOggetto
	 */
	// public void creaFoglioElencoProvvedimenti(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String
	// aTitolo, Vector aElencoProvv) throws F3BException
	public void creaFoglioElencoProvvedimenti(HSSFWorkbook wb, String aModalitaRicerca,
			UfficioModel aUuffUteConnesso, String aCriterio1, String aCriterio2, String aCriterio3,
			String aCriterio4, Vector aElencoProvv) throws F3BException {

		int numCol = 0;
		EveFasGepSogProvModel lProvvedimento = null;

		HSSFSheet sheet = wb.createSheet("ElencoProvvedimenti");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		// HSSFRow row = sheet.createRow(nRow);
		// setCell(row, 0, aTitolo , csNull);
		// nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio1, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio2, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio3, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio4, csNull);
		nRow++;
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco provvedimenti Depositati ", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (40 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Progr.", csCenter);
		setCell(row, numCol++, "Anno/Progr. Provvedimento", csCenter);
		setCell(row, numCol++, "Procedimento SIUS", csCenter);
		setCell(row, numCol++, "Generalita' Soggetto", csCenter);
		setCell(row, numCol++, "Tipo Atto", csCenter);
		setCell(row, numCol++, "Contenuto Atto", csCenter);
		setCell(row, numCol++, "Esito Provvedimento", csCenter);
		setCell(row, numCol++, "Data Emissione", csCenter);
		setCell(row, numCol++, "Data Deposito", csCenter);
		setCell(row, numCol++, "Strumenti Controllo", csCenter);

		Iterator itx = aElencoProvv.iterator();
		int progr = 0;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			// Inizializzazione Date
			lProvvedimento = (EveFasGepSogProvModel) itx.next();
			String AnnoProgr = new String();
			String dataDeposito = new String();
			String GeneralitaSoggetto = new String();
			// String DataContAtto = new String();
			String lTipoProvvedimento = new String();
			String luogoNascita = new String();

			// Scrittura riga del report

			row = sheet.createRow(nRow++);

			if (lProvvedimento.getDepositoOrdinanzaPc() != null) {
				lTipoProvvedimento = "Ordinanza";
				// MEV10-s3: cambiato il codice
				if (Utils.isPresent(lProvvedimento.getEvento().getCodTipoProvvedimento())
						&& "01".equals(lProvvedimento.getEvento().getCodTipoProvvedimento()))
					lTipoProvvedimento = "Sentenza";
				AnnoProgr = lProvvedimento.getDepositoOrdinanzaPc().getAnnoS3() + "/"
						+ lProvvedimento.getDepositoOrdinanzaPc().getNumS3();
				if (lProvvedimento.getEvento().getFlagDocumentoRegistrato().compareTo("A") == 0)
					AnnoProgr += "\nANNULLATA";
				dataDeposito = DateUtils.getDateToString(
						lProvvedimento.getDepositoOrdinanzaPc().getDataDeposito(), "dd/MM/yyyy");
			}

			if (lProvvedimento.getDepositoSentenza() != null) {
				lTipoProvvedimento = "Sentenza";
				AnnoProgr = lProvvedimento.getDepositoSentenza().getAnnoSentenza() + "/"
						+ lProvvedimento.getDepositoSentenza().getNumSentenza();
				if (lProvvedimento.getEvento().getFlagDocumentoRegistrato().compareTo("A") == 0)
					AnnoProgr += "\nANNULLATO";
				dataDeposito = DateUtils.getDateToString(
						lProvvedimento.getDepositoSentenza().getDataDeposito(), "dd/MM/yyyy");
			}

			if (lProvvedimento.getDepositoDecreto() != null) {
				lTipoProvvedimento = "Decreto";
				AnnoProgr = lProvvedimento.getDepositoDecreto().getAnnoS72() + "/"
						+ lProvvedimento.getDepositoDecreto().getNumS72();
				if (lProvvedimento.getEvento().getFlagDocumentoRegistrato().compareTo("A") == 0)
					AnnoProgr += "\nANNULLATO";
				dataDeposito = DateUtils
						.getDateToString(lProvvedimento.getDepositoDecreto().getDataDeposito(), "dd/MM/yyyy");
			}

			BigDecimal lIdSoggetto = lProvvedimento.getSoggetto().getIdSoggetto();
			SoggettoModel lSoggetto = new SoggettoModel();

			try {
				ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
				lSoggetto = lCtrlSoggetto.ExRicercaSoggettoByKey(lIdSoggetto);
			} catch (Exception e) {
				throw new F3BException("creaFoglioElencoProvvedimenti: ricerca Soggetto " + e);
			}

			if (lSoggetto.getCodStatoNascita().compareTo("039") == 0)
				luogoNascita = lSoggetto.getDescrComuneNascita() + "(" + lSoggetto.getCodProvinciaNascita()
						+ ")";
			else
				luogoNascita = lSoggetto.getDescComuneNascitaEstero() + "(" + lSoggetto.getDescrStatoNascita()
						+ ")";

			GeneralitaSoggetto = lSoggetto.getNome() + " " + lSoggetto.getCognome() + "\n"
					+ DateUtils.getDateToString(lSoggetto.getDataNascita(), "dd/MM/yyyy") + " "
					+ luogoNascita;

			// DataContAtto = DateUtils.getDateToString(lProvvedimento.getEvento().getDataEmissione(),
			// "dd/MM/yyyy")
			// + " "
			// + lTipoProvvedimento
			// + "\n"
			// + lProvvedimento.getEvento().getDescrMotivo()
			// + "\n"
			// + lProvvedimento.getEvento().getDescrEsito();
			setCell(row, numCol++, new Integer(++progr).toString(), csCenter);
			setCell(row, numCol++, AnnoProgr, csCenter);
			setCell(row, numCol++, lProvvedimento.getFascicoloSius().getChiaveAnno() + "/"
					+ lProvvedimento.getFascicoloSius().getChiaveProgr(), csCenter);
			setCell(row, numCol++, GeneralitaSoggetto, csCenter);
			setCell(row, numCol++, lTipoProvvedimento, csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrMotivo(), csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrEsito(), csCenter);
			setCell(row, numCol++,
					DateUtils.getDateToString(lProvvedimento.getEvento().getDataEmissione(), "dd/MM/yyyy"),
					csCenter);
			setCell(row, numCol++, dataDeposito, csCenter);

			if (lProvvedimento.getDepositoOrdinanzaPc() != null) {
				setCell(row, numCol++,
						lProvvedimento.getDepositoOrdinanzaPc().getCodTipoControlloEsecuzione(), csCenter);
			}
			if (lProvvedimento.getDepositoSentenza() != null) {
				setCell(row, numCol++, lProvvedimento.getDepositoSentenza().getCodTipoSentenza(), csCenter);
			}
			if (lProvvedimento.getDepositoDecreto() != null) {
				setCell(row, numCol++, lProvvedimento.getDepositoDecreto().getCodTipoControlloEsecuzione(),
						csCenter);
			}
		}
	}

	public List ExRicercaScadenzarioSimeone(ScadenzarioModel aScadenzario) throws F3BException {

		Connection lConn = null;

		List lScadenzari = new ArrayList();

		ScadenzarioSoggettoSqlDAO lScaSogDao = null;

		try {
			lConn = getDBConnection();

			lScaSogDao = new ScadenzarioSoggettoSqlDAO(lConn);

			lScaSogDao.ricercaScadenzarioSimeoneCompleta(aScadenzario);

			lScaSogDao.start();

			while (lScaSogDao.next()) {
				lScadenzari.add(lScaSogDao.getModelSimeone());
			}

			lScaSogDao.stop();

			if (lScadenzari.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);

			throw new F3BException("StatisController.ExRicercaScadenzarioSimeone: " + daoEx);
		} finally {
			cleanup(lScaSogDao);

			cleanup(lConn);
		}

		return lScadenzari;
	}

	public void ExCreateReportScadenzarioSimeone(ScadenzarioModel aScaMod, UfficioModel aUffMod,
			HSSFWorkbook wb) throws F3BException {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();

		// Stile della cella con bordi
		// HSSFCellStyle cs = getBordo4Lati(wb);

		HSSFCellStyle csBold = wb.createCellStyle();
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// Stile della cella con bordi e
		// allineamento a destra
		HSSFCellStyle csR = getBordo4Lati(wb);
		csR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// stile per celle col bordo con carattere grassetto
		// ALLINEATO A DESTRA
		HSSFCellStyle csBoldRight = getBordo4Lati(wb);
		csBoldRight.setFont(font);
		csBoldRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFSheet sheet = wb.createSheet("Elenco");

		// int nRow = 0;

		// Intestazione Ufficio
		int nRow = setIntestazione(sheet, aUffMod, csNull);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aScaMod.getTitoloReport(), csNull);

		nRow++; // Eventualmente in questa riga Dettagliare i tipo di ricerca
		nRow++;
		nRow++;

		// Intestazione Elenco
		int numCol = 0;

		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (15 * 256));
		if ("S".equals(aScaMod.getCodStatoNotifica())) {
			sheet.setColumnWidth(numCol++, (15 * 256));
			sheet.setColumnWidth(numCol++, (15 * 256));
			sheet.setColumnWidth(numCol++, (25 * 256));
		} else {
			sheet.setColumnWidth(numCol++, (15 * 256));
			sheet.setColumnWidth(numCol++, (25 * 256));
		}

		row = sheet.createRow(nRow);

		numCol = 0;

		setCell(row, numCol++, "N° SIEP", csBoldCenter);
		setCell(row, numCol++, "Cognome", csBoldCenter);
		setCell(row, numCol++, "Nome", csBoldCenter);
		setCell(row, numCol++, "Luogo Nascita", csBoldCenter);
		setCell(row, numCol++, "Data Nascita", csBoldCenter);
		if ("S".equals(aScaMod.getCodStatoNotifica())) {
			setCell(row, numCol++, "Data Notifica", csBoldCenter);
			setCell(row, numCol++, "Data Scadenza", csBoldCenter);
			setCell(row, numCol++, "N° Giorni Residui", csBoldCenter);
		} else {
			setCell(row, numCol++, "Data Emissione Decreto", csBoldCenter);
			setCell(row, numCol++, "Stato delle notifiche", csBoldCenter);
		}

		nRow++;

		List lElenco = ExRicercaScadenzarioSimeone(aScaMod);

		Iterator itx = lElenco.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			ScadenzarioModel lSca = (ScadenzarioModel) itx.next();
			FascicoloSiepModel lFas = lSca.getFascicoloModel();
			SoggettoModel lSog = lFas.getSoggetto();

			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lFas.getChiaveAnno() + "/" + lFas.getChiaveProgr(), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(lSog.getCognome()), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(lSog.getNome()), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(lSog.getDescrComuneNascita()), csCenter);
			setCell(row, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(), "dd/MM/yyyy")),
					csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(
					DateUtils.getDateToString(lSca.getDataInizioScadenza(), "dd/MM/yyyy")), csCenter);

			if ("S".equals(aScaMod.getCodStatoNotifica())) {
				setCell(row, numCol++,
						StringUtils.toStringJSP(
								DateUtils.getDateToString(lSca.getDataFineScadenza(), "dd/MM/yyyy")),
						csCenter);
				setCell(row, numCol++,
						ScadenzarioUtils.getDifferenza(lSca.getDataFineScadenza(), DateUtils.getSysDate()),
						csCenter);
			} else {
				setCell(row, numCol++, StringUtils.toStringJSP(lSca.getDescrStatoNotifica()), csCenter);
			}
		}
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fascicoli Pendenti alla fine periodo. Nell'elenco
	 * ogni fascicolo viene individuato da ANNO/Progr; nell'elenco i fascicoli vengono raggrupati per anno e
	 * per ogni anno si riporta anche il numero totale di Fascicoli.
	 *
	 * @param wb
	 *            HSSFWorkbook
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataFine
	 */
	private void creaFoglioFascicoliPendenti(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, Date adataFine) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliPendenti : inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vector size : " + aElencoProc.size());

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ElencoPendenti");
		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle con carattere grassetto
		/*
		 * HSSFCellStyle csBold = wb.createCellStyle(); // Create a new font and alter it. HSSFFont font =
		 * wb.createFont(); font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); csBold.setFont(font);
		 */
		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Procedimenti SIUS pendenti al : "
				+ DateUtils.getDateToString(adataFine, "dd/MM/yyyy"), csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		for (int i = 0; i < 10; i++)
			sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// N.ro colonna corrente
		int nnumCol = 0;
		// Anno corrente
		String lAnno = "0000";
		// Numero di Fascicoli per anno
		int lNumFas = 0;
		// Anno e Progressivo dell'ultimo elemento conteggiato
		String lAnnoLast = "0000";
		String lProgrLast = "0000";
		// Elemento corrente nella lista
		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			if (lProcEstrModel.getFasSiuChiaveAnno() != null
					&& lProcEstrModel.getFasSiuChiaveProgr() != null) {
				if (lProcEstrModel.getFasSiuChiaveAnno().toString().compareTo(lAnno) != 0) {
					if (lNumFas > 0) {
						// Stampa Totale numero fascicoli per anno
						row = sheet.createRow(nRow++);
						setCell(row, 8, "Totale Anno " + lAnno + " = " + lNumFas, csNull);
					}
					row = sheet.createRow(nRow++);
					// Cambio Anno
					lAnno = lProcEstrModel.getFasSiuChiaveAnno().toString();
					// Scrittura intestazione Anno
					row = sheet.createRow(nRow++);
					setCell(row, 5, "Anno   " + lAnno, csNull);
					// Salto a capo
					row = sheet.createRow(nRow++);
					nnumCol = 0;
					// Inizializzazione contatore
					lNumFas = 0;
				}
				if (nnumCol == 10) {
					// Salto riga
					nnumCol = 0;
					row = sheet.createRow(nRow++);
				}
				// Verifica che si tratti di un fascicolo non gia' conteggiato
				if (lProcEstrModel.getFasSiuChiaveAnno().toString().compareTo(lAnnoLast) != 0
						|| lProcEstrModel.getFasSiuChiaveProgr().toString().compareTo(lProgrLast) != 0) {
					lAnnoLast = lProcEstrModel.getFasSiuChiaveAnno().toString();
					lProgrLast = lProcEstrModel.getFasSiuChiaveProgr().toString();
					setCell(row, nnumCol++, lAnnoLast + "/" + lProgrLast, csNull);
					lNumFas++;
				}

				// setCell(row, nnumCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/" +
				// lProcEstrModel.getFasSiuChiaveProgr().toString(), csNull);
				// lNumFas++;
			}
		}
		if (lNumFas > 0) {
			// Stampa Totale numero fascicoli per anno
			row = sheet.createRow(nRow++);
			setCell(row, 8, "Totale Anno " + lAnno + " = " + lNumFas, csNull);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliPendenti : fine");
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fascicoli Pendenti alla fine periodo. Nell'elenco
	 * ogni fascicolo viene individuato da ANNO/Progr; nell'elenco i fascicoli vengono raggrupati per
	 * Contenuto e per ogni Contenuto si riporta anche il numero totale di Fascicoli.
	 *
	 * @param wb
	 *            HSSFWorkbook
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataFine
	 */
	private void creaFoglioContenutiPendenti(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, Date adataFine) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioContenutiPendenti : inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vector size : " + aElencoProc.size());

		// Riordina Vettore per Contenuto
		Vector aElencoCont = new Vector();
		Vector aElencoProcByCont = new Vector();
		IspEstrazioneOggettiModel lProcEstrCont1Model = null;
		Iterator itxProc = aElencoProc.iterator();

		// Variabili per la gestione degli Oggetti senza riscontro
		Vector<String> lCodiciConRiscontri = new Vector<>();
		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		// Ticket#202008250110 - SIES - chiusura connessione a DB
		Connection lConn = null;
		IspMotivoOggettoDAO lIspOggettiSenzaRiscontriDAO = null;
		IspMotivoOggettoModel lOggettoSenzaRiscontroModel = null;
		Vector lOggettiSenzaRiscontri = new Vector();

		String lContenuto = "0000";
		while (itxProc.hasNext()) {
			lProcEstrCont1Model = (IspEstrazioneOggettiModel) itxProc.next();
			if (lProcEstrCont1Model.getCodOggettoProcedimento() != null) {
				if (!aElencoCont.contains(lProcEstrCont1Model.getCodOggettoProcedimento())) {
					aElencoCont.addElement(lProcEstrCont1Model.getCodOggettoProcedimento());
				}
			}
		}

		Iterator itxCont = aElencoCont.iterator();

		while (itxCont.hasNext()) {
			lContenuto = itxCont.next().toString();
			Iterator itx2Proc = aElencoProc.iterator();

			while (itx2Proc.hasNext()) {
				lProcEstrCont1Model = (IspEstrazioneOggettiModel) itx2Proc.next();
				if (lProcEstrCont1Model.getCodOggettoProcedimento().compareTo(lContenuto) == 0) {
					if (!aElencoProcByCont.contains(lProcEstrCont1Model)) {
						aElencoProcByCont.addElement(lProcEstrCont1Model);
					}
				}
			}
		}

		// Fine Riordino

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ContenutiPendenti");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Procedimenti SIUS pendenti al  "
				+ DateUtils.getDateToString(adataFine, "dd/MM/yyyy") + " aggregati per Contenuto", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		for (int i = 0; i < 10; i++)
			sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// N.ro colonna corrente
		int nnumCol = 0;
		// Anno corrente
		String lAnno = "0000";
		String lProgr = "00000";
		String lCodContenuto = "0000";
		String lDescrContenuto = "0000";
		// Numero di Fascicoli per contenuto
		int lNumFas = 0;
		// Elemento corrente nella lista
		Iterator itx = aElencoProcByCont.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			if (lProcEstrModel.getFasSiuChiaveAnno() != null
					&& lProcEstrModel.getFasSiuChiaveProgr() != null) {
				if (lProcEstrModel.getCodOggettoProcedimento().compareTo(lCodContenuto) != 0) {
					lDescrContenuto = DecodificheUtils.getDescbyCode(
							DecodificheManager.getInstance().getOggettoProcedimento(), lCodContenuto);
					if (lNumFas > 0) {
						// Stampa Totale numero fascicoli per Contenuto
						row = sheet.createRow(nRow++);
						setCell(row, 8, "Totale Contenuto = " + lNumFas, csNull);
					}
					row = sheet.createRow(nRow++);
					// Cambio Contenuto
					lCodContenuto = lProcEstrModel.getCodOggettoProcedimento();
					lCodiciConRiscontri.add(lCodContenuto);
					lDescrContenuto = DecodificheUtils.getDescbyCode(
							DecodificheManager.getInstance().getOggettoProcedimento(), lCodContenuto);
					// Scrittura intestazione Oggetto
					row = sheet.createRow(nRow++);
					setCell(row, 0, lDescrContenuto, csNull);
					// Salto a capo
					row = sheet.createRow(nRow++);
					nnumCol = 0;
					// Inizializzazione contatore
					lNumFas = 0;
				}
				if (nnumCol == 10) {
					// Salto riga
					nnumCol = 0;
					row = sheet.createRow(nRow++);
				}
				// Verifica che si tratti di un fascicolo non gia' conteggiato
				if (lProcEstrModel.getFasSiuChiaveAnno().toString().compareTo(lAnno) != 0
						|| lProcEstrModel.getFasSiuChiaveProgr().toString().compareTo(lProgr) != 0) {
					lAnno = lProcEstrModel.getFasSiuChiaveAnno().toString();
					lProgr = lProcEstrModel.getFasSiuChiaveProgr().toString();
					setCell(row, nnumCol++, lAnno + "/" + lProgr, csNull);
					lNumFas++;
				}
			}
		}
		if (lNumFas > 0) {
			// Stampa Totale numero fascicoli per Contenuto
			row = sheet.createRow(nRow++);
			setCell(row, 8, "Totale Contenuto = " + lNumFas, csNull);
		}

		try {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			// Ticket#202008250110 - SIES - chiusura connessione a DB
			lConn = getDBConnection();
			// lIspOggettiSenzaRiscontriDAO = new IspMotivoOggettoDAO(getDBConnection());
			lIspOggettiSenzaRiscontriDAO = new IspMotivoOggettoDAO(lConn);
			lIspOggettiSenzaRiscontriDAO.setCondizioneEsclusioneCodiciOggetto(lCodiciConRiscontri,
					aUuffUteConnesso.getCodUfficio());
			lIspOggettiSenzaRiscontriDAO.setOrdine("DESC_OGGETTO");
			lOggettiSenzaRiscontri.addAll(lIspOggettiSenzaRiscontriDAO.getModels());

			String lDescrizioneIniziale = new String();

			// il SORT del DAO !DEVE ESSERE! basato su DESC_OGGETTO. In questo modo la funzione 'DISCTINCT'
			// puo' essere effettuata
			// da codice utilizzando la variabile di appoggio lDescrizioneIniziale

			Iterator iter = lOggettiSenzaRiscontri.iterator();
			while (iter.hasNext()) {
				lOggettoSenzaRiscontroModel = (IspMotivoOggettoModel) iter.next();
				if (lOggettoSenzaRiscontroModel != null) {
					if (lDescrizioneIniziale.compareTo(lOggettoSenzaRiscontroModel.getDescOggetto()) != 0) {
						lDescrizioneIniziale = lOggettoSenzaRiscontroModel.getDescOggetto();
						row = sheet.createRow(++nRow);
						setCell(row, 0, lDescrizioneIniziale, csNull);
						row = sheet.createRow(++nRow);
						setCell(row, 8, "Totale Oggetto = 0", csNull);
						nRow++;
					}
				}
			}
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("StatisController.creaFoglioOggettiPendenti: " + ex);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("F3BException: " + ex);
			throw new F3BException("StatisController.creaFoglioOggettiPendenti: " + ex);
		}
		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		// Ticket#202008250110 - SIES - chiusura connessione a DB
		finally {
			cleanup(lIspOggettiSenzaRiscontriDAO);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioContenutiPendenti : fine");
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fascicoli Pendenti alla fine periodo. Nell'elenco
	 * ogni fascicolo viene individuato da ANNO/Progr; nell'elenco i fascicoli vengono raggrupati per anno e
	 * per ogni anno si riporta anche il numero fi Fascicoli.
	 *
	 * @param wb
	 *            HSSFWorkbook
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataFine
	 */
	private void creaFoglioOggettiPendenti(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, Date adataFine) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioOggettiPendenti : inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vector size : " + aElencoProc.size());

		// Variabili per la gestione degli Oggetti senza riscontro
		Vector<String> lCodiciConRiscontri = new Vector<>();
		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		// Ticket#202008250110 - SIES - chiusura connessione a DB
		Connection lConn = null;
		IspMotivoOggettoDAO lIspOggettiSenzaRiscontriDAO = null;
		IspMotivoOggettoModel lOggettoSenzaRiscontroModel = null;
		Vector lOggettiSenzaRiscontri = new Vector();

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("OggettiPendenti");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Procedimenti SIUS pendenti al  "
				+ DateUtils.getDateToString(adataFine, "dd/MM/yyyy") + " aggregati per Oggetto", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		for (int i = 0; i < 10; i++)
			sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// N.ro colonna corrente
		int nnumCol = 0;
		// Anno corrente
		// String lAnno = "0000";
		String lCodOggetto = "0000";
		String lDescrOggetto = "0000";
		// Numero di Fascicoli per anno
		int lNumFas = 0;
		// Elemento corrente nella lista
		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			if (lProcEstrModel.getFasSiuChiaveAnno() != null
					&& lProcEstrModel.getFasSiuChiaveProgr() != null) {
				if (lProcEstrModel.getCodOggettoTenore().compareTo(lCodOggetto) != 0) {
					lDescrOggetto = DecodificheUtils.getDescbyCode(
							DecodificheManager.getInstance().getMotivoProvvedimento(), lCodOggetto);
					if (lNumFas > 0) {
						// Stampa Totale numero fascicoli per anno
						row = sheet.createRow(nRow++);
						setCell(row, 8, "Totale Oggetto = " + lNumFas, csNull);
					}
					row = sheet.createRow(nRow++);
					// Cambio Anno
					lCodOggetto = lProcEstrModel.getCodOggettoTenore().toString();
					lCodiciConRiscontri.add(lCodOggetto);
					lDescrOggetto = DecodificheUtils.getDescbyCode(
							DecodificheManager.getInstance().getMotivoProvvedimento(), lCodOggetto);
					// Scrittura intestazione Oggetto
					row = sheet.createRow(nRow++);
					setCell(row, 0, lDescrOggetto, csNull);
					// Salto a capo
					row = sheet.createRow(nRow++);
					nnumCol = 0;
					// Inizializzazione contatore
					lNumFas = 0;
				}
				if (nnumCol == 10) {
					// Salto riga
					nnumCol = 0;
					row = sheet.createRow(nRow++);
				}
				setCell(row, nnumCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
						+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csNull);
				lNumFas++;
			}
		}
		if (lNumFas > 0) {
			// Stampa Totale numero fascicoli per oggetto
			row = sheet.createRow(nRow++);
			setCell(row, 8, "Totale Oggetto = " + lNumFas, csNull);
		}

		try {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			// Ticket#202008250110 - SIES - chiusura connessione a DB
			lConn = getDBConnection();
			// lIspOggettiSenzaRiscontriDAO = new IspMotivoOggettoDAO(getDBConnection());
			lIspOggettiSenzaRiscontriDAO = new IspMotivoOggettoDAO(lConn);
			lIspOggettiSenzaRiscontriDAO.setCondizioneEsclusioneCodiciMotivo(lCodiciConRiscontri,
					aUuffUteConnesso.getCodUfficio());
			lIspOggettiSenzaRiscontriDAO.setOrdine("DESC_MOTIVO");
			lOggettiSenzaRiscontri.addAll(lIspOggettiSenzaRiscontriDAO.getModels());

			Iterator iter = lOggettiSenzaRiscontri.iterator();
			while (iter.hasNext()) {
				lOggettoSenzaRiscontroModel = (IspMotivoOggettoModel) iter.next();
				if (lOggettoSenzaRiscontroModel != null) {
					row = sheet.createRow(++nRow);
					setCell(row, 0, lOggettoSenzaRiscontroModel.getDescMotivo(), csNull);
					row = sheet.createRow(++nRow);
					setCell(row, 8, "Totale Oggetto = 0", csNull);
					nRow++;
				}
			}
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("StatisController.creaFoglioOggettiPendenti: " + ex);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("F3BException: " + ex);
			throw new F3BException("StatisController.creaFoglioOggettiPendenti: " + ex);
		}
		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		// Ticket#202008250110 - SIES - chiusura connessione a DB
		finally {
			cleanup(lIspOggettiSenzaRiscontriDAO);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliPendentiPerOggetto : fine");
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco degli Oggetti Cancellati nel periodo.
	 *
	 * @param wb
	 *            HSSFWorkbook
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataFine
	 */
	private void creaFoglioOggettiCancellati(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, Date adataFine) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioOggettiCancellati : inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vector size : " + aElencoProc.size());

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("OggettiCancellati");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Procedimenti SIUS cancellati nel periodo aggregati per Oggetto", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		for (int i = 0; i < 10; i++)
			sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// N.ro colonna corrente
		int nnumCol = 0;
		// Anno corrente
		// String lAnno = "0000";
		String lCodOggetto = "0000";
		String lDescrOggetto = "0000";
		// Numero di Fascicoli per anno
		int lNumFas = 0;
		// Elemento corrente nella lista
		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			if (lProcEstrModel.getFasSiuChiaveAnno() != null
					&& lProcEstrModel.getFasSiuChiaveProgr() != null) {
				if (lProcEstrModel.getCodOggettoTenore().compareTo(lCodOggetto) != 0) {
					lDescrOggetto = DecodificheUtils.getDescbyCode(
							DecodificheManager.getInstance().getMotivoProvvedimento(), lCodOggetto);
					if (lNumFas > 0) {
						// Stampa Totale numero fascicoli per anno
						row = sheet.createRow(nRow++);
						setCell(row, 8, "Totale Oggetto = " + lNumFas, csNull);
					}
					row = sheet.createRow(nRow++);
					// Cambio Anno
					lCodOggetto = lProcEstrModel.getCodOggettoTenore().toString();
					lDescrOggetto = DecodificheUtils.getDescbyCode(
							DecodificheManager.getInstance().getMotivoProvvedimento(), lCodOggetto);
					// Scrittura intestazione Oggetto
					row = sheet.createRow(nRow++);
					setCell(row, 0, lDescrOggetto, csNull);
					// Salto a capo
					row = sheet.createRow(nRow++);
					nnumCol = 0;
					// Inizializzazione contatore
					lNumFas = 0;
				}
				// if (nnumCol == 10)
				// {
				// Salto riga
				nnumCol = 0;
				row = sheet.createRow(nRow++);
				// }
				setCell(row, nnumCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
						+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csNull);
				lNumFas++;
			}
		}
		if (lNumFas > 0) {
			// Stampa Totale numero fascicoli per oggetto
			row = sheet.createRow(nRow++);
			setCell(row, 8, "Totale Oggetto = " + lNumFas, csNull);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioOggettiCancellati : fine");
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fascicoli Unificati. Nell'elenco ogni fascicolo
	 * viene individuato da ANNO/Progr; nell'elenco i fascicoli vengono raggrupati per anno e per ogni anno si
	 * riporta anche il numero di Fascicoli.
	 *
	 * @param wb
	 *            HSSFWorkbook
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataFine
	 */
	private void creaFoglioFascicoliUnificati(HSSFWorkbook wb, UfficioModel aUuffUteConnesso, String aTitolo,
			Vector aElencoProc, Date adataFine) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliUnificati : inizio");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Vector size : " + aElencoProc.size());

		int numCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		HSSFSheet sheet = wb.createSheet("ElencoUnificati");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aTitolo, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		// setCell(row, 0, "Elenco Procedimenti SIUS unificati al : " +
		// DateUtils.getDateToString(adataFine,"dd/MM/yyyy"), csNull);
		setCell(row, 0, "Elenco Procedimenti SIUS unificati", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		for (int i = 0; i < 10; i++)
			sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// N.ro colonna corrente
		int nnumCol = 0;
		// Anno corrente
		String lAnno = "0000";
		String lProgr = "0000";
		// Numero di Fascicoli per anno
		int lNumFas = 0;
		// Elemento corrente nella lista
		Iterator itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			lProcEstrModel = (IspEstrazioneOggettiModel) itx.next();
			if (lProcEstrModel.getFasSiuChiaveAnno() != null
					&& lProcEstrModel.getFasSiuChiaveProgr() != null) {
				if (lProcEstrModel.getFasSiuChiaveAnno().toString().compareTo(lAnno) != 0) {
					if (lNumFas > 0) {
						// Stampa Totale numero fascicoli per anno
						row = sheet.createRow(nRow++);
						setCell(row, 8, "Totale Anno " + lAnno + " = " + lNumFas, csNull);
					}
					row = sheet.createRow(nRow++);
					// Cambio Anno
					lAnno = lProcEstrModel.getFasSiuChiaveAnno().toString();
					// Scrittura intestazione Anno
					row = sheet.createRow(nRow++);
					setCell(row, 5, "Anno   " + lAnno, csNull);
					// Salto a capo
					row = sheet.createRow(nRow++);
					nnumCol = 0;
					// Inizializzazione contatore
					lNumFas = 0;
				}
				if (nnumCol == 10) {
					// Salto riga
					nnumCol = 0;
					row = sheet.createRow(nRow++);
				}
				// Verifica che si tratti di un fascicolo non gia' conteggiato
				// if ( lProcEstrModel.getFasSiuChiaveAnno().toString().compareTo(lAnno) != 0 ||
				// lProcEstrModel.getFasSiuChiaveProgr().toString().compareTo(lProgr) != 0 )
				if (lNumFas == 0 || lProcEstrModel.getFasSiuChiaveProgr().toString().compareTo(lProgr) != 0) {
					lAnno = lProcEstrModel.getFasSiuChiaveAnno().toString();
					lProgr = lProcEstrModel.getFasSiuChiaveProgr().toString();
					setCell(row, nnumCol++, lAnno + "/" + lProgr, csNull);
					lNumFas++;
				}
				// setCell(row, nnumCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/" +
				// lProcEstrModel.getFasSiuChiaveProgr().toString(), csNull);
				// lNumFas++;
			}
		}
		if (lNumFas > 0) {
			// Stampa Totale numero fascicoli per anno
			row = sheet.createRow(nRow++);
			setCell(row, 8, "Totale Anno " + lAnno + " = " + lNumFas, csNull);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".creaFoglioFascicoliUnificati : fine");
	}

	// AMBROS L78/2013
	public void ExCreateReportTrasmessiL78del2013(ScadenzarioModel aScaMod, UfficioModel aUffMod,
			HSSFWorkbook wb, Boolean attivi, Boolean noattivi, String uffutecoll) throws F3BException {

		// Stile della cella vuoto
		HSSFCellStyle csNull = wb.createCellStyle();

		// Stile della cella con bordi
		// HSSFCellStyle cs = getBordo4Lati(wb);

		HSSFCellStyle csBold = wb.createCellStyle();
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// Stile della cella con bordi e
		// allineamento a destra
		HSSFCellStyle csR = getBordo4Lati(wb);
		csR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		// stile per celle col bordo con carattere grassetto
		// ALLINEATO A DESTRA
		HSSFCellStyle csBoldRight = getBordo4Lati(wb);
		csBoldRight.setFont(font);
		csBoldRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFSheet sheet = wb.createSheet("Elenco");

		// int nRow = 0;

		// Intestazione Ufficio
		int nRow = setIntestazione(sheet, aUffMod, csNull);

		nRow++;
		nRow++;

		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(nRow);

		// setCell(row, 0, aScaMod.getTitoloReport(), csNull);
		setCell(row, 0, "Procedimeti trasmessi - Decreto Legge 78/2013 ", csNull);

		nRow++; // Eventualmente in questa riga Dettagliare i tipo di ricerca
		nRow++;
		nRow++;

		// Intestazione Elenco
		int numCol = 0;

		sheet.setColumnWidth(numCol++, (15 * 256)); // n.siep
		sheet.setColumnWidth(numCol++, (20 * 256)); // cogno
		sheet.setColumnWidth(numCol++, (20 * 256)); // nome
		sheet.setColumnWidth(numCol++, (25 * 256)); // luogo
		sheet.setColumnWidth(numCol++, (15 * 256)); // data

		sheet.setColumnWidth(numCol++, (20 * 256)); // provv
		sheet.setColumnWidth(numCol++, (50 * 256)); // motivo

		sheet.setColumnWidth(numCol++, (15 * 256)); // data emissione
		sheet.setColumnWidth(numCol++, (50 * 256)); // stato procedimento

		row = sheet.createRow(nRow);

		numCol = 0;

		setCell(row, numCol++, "N° SIEP", csBoldCenter);
		setCell(row, numCol++, "Cognome", csBoldCenter);
		setCell(row, numCol++, "Nome", csBoldCenter);
		setCell(row, numCol++, "Luogo Nascita", csBoldCenter);
		setCell(row, numCol++, "Data Nascita", csBoldCenter);

		setCell(row, numCol++, "provvedimento", csBoldCenter);
		setCell(row, numCol++, "Motivo Provvedimento", csBoldCenter);

		setCell(row, numCol++, "Data Emissione Decreto", csBoldCenter);
		setCell(row, numCol++, "Stato Procedimento", csBoldCenter);

		nRow++;

		List lElenco = ExRicercaTrasmessiL78del2013(aScaMod, uffutecoll, attivi, noattivi);

		Iterator itx = lElenco.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			EventoFascicoloStatoModel lEveFas = (EventoFascicoloStatoModel) itx.next();
			FascicoloSiepModel lFas = lEveFas.getFascicoloSiep();
			SoggettoModel lSog = lFas.getSoggetto();
			EventoModel mEvento = lEveFas.getEvento();
			StatoProcedimentoModel mStatoProc = lEveFas.getStatoProcedimento();

			row = sheet.createRow(nRow++);
			setCell(row, numCol++, lFas.getChiaveAnno() + "/" + lFas.getChiaveProgr(), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(lSog.getCognome()), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(lSog.getNome()), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(lSog.getDescrComuneNascita()), csCenter);
			setCell(row, numCol++,
					StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(), "dd/MM/yyyy")),
					csCenter);

			setCell(row, numCol++, StringUtils.toStringJSP(mEvento.getDescrProvvedimento()), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(mEvento.getDescrMotivo()), csCenter);
			setCell(row, numCol++, StringUtils.toStringJSP(
					DateUtils.getDateToString(mEvento.getDataEmissione(), "dd/MM/yyyy")), csCenter);

			setCell(row, numCol++, StringUtils.toStringJSP(mStatoProc.getDescrStatoProcedimento()), csCenter);
		}
	} // chiude ExCreateReportTrasmessiL78del2013

	public List ExRicercaTrasmessiL78del2013(ScadenzarioModel aScadenzario, String uffutecol, Boolean attivi,
			Boolean noattivi) throws F3BException {

		Connection lConn = null;

		List lTrasmessi = new ArrayList();
		EventoSimeoneSqlDAO lEveFasDao = null;

		try {
			lConn = getDBConnection();

			lEveFasDao = new EventoSimeoneSqlDAO(lConn);
			lEveFasDao.ricercatrasmessiL78del2013Xreport(aScadenzario, uffutecol, attivi, noattivi);
			lEveFasDao.start();

			while (lEveFasDao.next()) {
				lTrasmessi.add(lEveFasDao.getModelL78del2013());
			}

			lEveFasDao.stop();

			if (lTrasmessi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StatisController.ExRicercaTrasmessiL78del2013: " + daoEx);
		} finally {
			cleanup(lEveFasDao);
			cleanup(lConn);
		}

		return lTrasmessi;
	} // chiude ExRicercaTrasmessiL78del2013

	public Vector<IspAttivitaMagistratiModel> ExCercaMotivi(String Tipologia) throws F3BException {

		Connection lConn = null;
		Vector<IspAttivitaMagistratiModel> lVect = new Vector<>();
		IspAttivitaMagistratiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);
			// lIspDao.ricercaRiepilogoGeneraleAttivita(aAnno);
			lIspDao.ricercaMotivoPerAttivita(Tipologia);
			lIspDao.start();
			while (lIspDao.next()) {
				lVect.add((IspAttivitaMagistratiModel) lIspDao.getModelMotivo());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StatisController.ExCercaMotivi: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lConn);
			cleanup(lIspDao);
		}

		return lVect;
	}

	// NGG
	public Vector<IspAttivitaMagistratiModel> ExTotaleMotiviAnno(int annoini, int annofin)
			throws F3BException {

		Connection lConn = null;
		Vector<IspAttivitaMagistratiModel> lVect = new Vector<>();
		IspAttivitaMagistratiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			for (int aAnno = annoini; aAnno < annofin + 1; aAnno++) {
				try {
					lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);
					lIspDao.ricercaTotaleMotivoPerAnno(aAnno);
					lIspDao.start();
					while (lIspDao.next()) {
						lVect.add((IspAttivitaMagistratiModel) lIspDao.getTotaliPerAnno());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisController.ExTotaleMotiviAnno: Non posso leggere : " + daoEx);
				} finally {
					cleanup(lIspDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	}

	public Vector<IspAttivitaMagistratiModel> ExTotaleMotiviMagAnno(int annoini, int annofin)
			throws F3BException {

		Connection lConn = null;
		Vector<IspAttivitaMagistratiModel> lVect = new Vector<>();
		IspAttivitaMagistratiSqlDAO lIspDao = null;
		try {
			lConn = getDBConnection();
			for (int aAnno = annoini; aAnno < annofin + 1; aAnno++) {
				try {
					lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);
					lIspDao.ricercaTotaleMotivoPerMagistratoAnno(aAnno);
					lIspDao.start();
					while (lIspDao.next()) {
						lVect.add((IspAttivitaMagistratiModel) lIspDao.getTotaliPerMagAnno());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisController.ExTotaleMotiviMagAnno: Non posso leggere : " + daoEx);
				} finally {
					cleanup(lIspDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	}

	public Vector<IspAttivitaMagistratiModel> ExElencoMotiviMagAnno(String CodMag) throws F3BException {

		Connection lConn = null;
		Vector<IspAttivitaMagistratiModel> lVect = new Vector<>();
		IspAttivitaMagistratiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);
			lIspDao.RicercaElencoMotiviPerMagistratoAnno(CodMag);
			lIspDao.start();
			while (lIspDao.next()) {
				lVect.add((IspAttivitaMagistratiModel) lIspDao.getElencoMotiviPerMagAnno());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StatisController.ExElencoMotiviMagAnno: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lConn);
			cleanup(lIspDao);
		}

		return lVect;
	}

	/**
	 * Metodo che crea il foglio 'Elenco Procedimenti'
	 *
	 * NGG Step2 (foglio Elenco Procedimenti Magistrati)
	 *
	 * @param wb
	 * @param uffUteConnesso
	 * @param dataIni
	 * @param dataFin
	 * @param descIntesta
	 * @param VElencoMotivi
	 *            <IspAttivitaMagistratiModel>
	 * @throws F3BException
	 */
	public void ExCreateAttivitaMagistratiElenco(HSSFWorkbook wb, UfficioModel uffUteConnesso, String dataIni,
			String dataFin, String descIntesta, Vector VElencoMotivi, MagistratoModel aMagistratoMod)
			throws F3BException {

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		// HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// csBoldCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csBoldCenter.setWrapText(true);

		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 198, (byte) 224, (byte) 180);

		// Stile righe Intestazioni
		HSSFFont fontGR = wb.createFont();
		fontGR.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontGR.setColor(HSSFColor.BLUE.index);

		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);

		csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.GREEN.index);
		csTitolo1.setFont(fontGR);

		// stile per celle col bordo con carattere grassetto centrato e testo verticale
		/*
		 * HSSFCellStyle csVert = getBordo4Lati(wb); csVert.setFont(font);
		 * csVert.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 * csVert.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); csVert.setRotation((short) 90);
		 */

		// Stile righe Intestazioni
		// palette.setColorAtIndex(HSSFColor.DARK_TEAL.index, (byte) 237, (byte) 125, (byte) 49);
		HSSFFont fontGruppo = wb.createFont();
		fontGruppo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// HSSFColor color = palette.findSimilarColor(237, 125, 49);
		// fontGruppo.setColor(color.getIndex());
		fontGruppo.setColor(HSSFColor.BLUE.index);

		// foglio Elenco
		HSSFSheet sheet = wb.createSheet("Elenco Procedimenti");

		sheet.getPrintSetup().setLandscape(true);
		sheet.setMargin(Sheet.RightMargin, 0.25);
		sheet.setMargin(Sheet.LeftMargin, 0.25);
		sheet.setMargin(Sheet.BottomMargin, 0.63); // 1,6 cm
		sheet.setMargin(Sheet.TopMargin, 0.75);

		// Colore per TOTALI
		HSSFCellStyle style = wb.createCellStyle();
		style = getBordo4Lati(wb);
		HSSFFont fontR = wb.createFont();
		// style.setFillForegroundColor(HSSFColor.LIME.index);
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		fontR.setColor(HSSFColor.RED.index);
		style.setFont(fontR);

		// Intestazione del foglio excel
		int nRow = setIntestazione(sheet, uffUteConnesso, csNull, descIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativo al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// IspAttivitaMagistratiModel lMod1 = (IspAttivitaMagistratiModel)VElencoMotivi.get(0);
		// row = sheet.createRow(nRow);
		// setCell(row, 0, "Magistrato delegato: " + lMod1.getDescrMagistrato(), csNull);

		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Magistrato delegato: " + aMagistratoMod.getNome() + " " + aMagistratoMod.getCognome(),
				csNull);

		nRow++;
		nRow++;
		nRow++;

		// ============================================
		// settaggio della larghezza delle colonne
		// ============================================
		// sheet.setColumnWidth((short) 0, (short) (10 * 256)); // Numero d'ordine
		// sheet.setColumnWidth((short) 1, (short) (15 * 256)); // Numero Fascicolo
		/*
		 * ISSUE MEV : i valori 13, 52 presi da MEV 29-36 (ex 15, 50) Numero MEV : SIES v10 Autore : gioggi
		 * Data : 28/gen/2016 Branch : MEV_SIES v10
		 */
		// sheet.setColumnWidth((short) 2, (short) (13 * 256)); // Data passaggio in giudicato
		// sheet.setColumnWidth((short) 3, (short) (52 * 256)); // Natura dell'atto
		// ***** FINE INTERVENTO MEV_SIES v10 *****//
		// sheet.setColumnWidth((short) 4, (short) (52 * 256)); // Descrizione Provvedimento di Origine
		//
		sheet.setColumnWidth(0, (9 * 256)); // Numero d'ordine
		sheet.setColumnWidth(1, (14 * 256)); // Numero Fascicolo
		/*
		 * ISSUE MEV : i valori 13, 52 presi da MEV 29-36 (ex 15, 50) Numero MEV : SIES v10 Autore : gioggi
		 * Data : 28/gen/2016 Branch : MEV_SIES v10
		 */
		sheet.setColumnWidth(2, (10 * 256)); // Data passaggio in giudicato
		sheet.setColumnWidth(3, (50 * 256)); // Natura dell'atto
		// ***** FINE INTERVENTO MEV_SIES v10 *****//
		sheet.setColumnWidth(4, (50 * 256)); // Descrizione Provvedimento di Origine

		// ==================================================
		// Crea la Riga con le intestazioni della tabella
		// ==================================================
		row = sheet.createRow(nRow);
		setCell(row, 0, "Numero d'ordine", csBoldCenter);
		setCell(row, 1, "Numero Procedimento", csBoldCenter);
		setCell(row, 2, "Data Emissione", csBoldCenter); // new
		setCell(row, 3, "Descrizione tipo Provvedimento", csBoldCenter);
		//
		setCell(row, 4, "Descrizione Motivo Provvedimento (Cod_Motivo)", csBoldCenter);

		nRow++;

		Iterator itx = VElencoMotivi.iterator();
		int cont = 1;
		// inizio ciclo di scrittura dei dati
		// String lLastTipologia = "";
		while (itx.hasNext()) {
			IspAttivitaMagistratiModel lMod = (IspAttivitaMagistratiModel) itx.next();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(lMod.getTipologia() + " - " + lMod.getDescrTipologia());

			// siesLogger.debug(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Descr motivo Orig =
			// "+lMod.getDescrMotivoOrig());

			// =================================
			// Inserisco riga con intestazione
			// =================================
			// if (!lLastTipologia.equals(lMod.getTipologia())){
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Cambio tipologia inserisco riga di descrizione: "+lMod.getTipologia()+" -
			// "+lMod.getDescrTipologia());
			//
			// row = sheet.createRow(nRow);
			// setCell(row, 0, lMod.getDescrTipologia(), csTitolo1);
			// setCell(row, 1, "", cs);
			// setCell(row, 2, "", cs);
			// setCell(row, 3, "", cs);
			// sheet.addMergedRegion(new CellRangeAddress(nRow,nRow,0,3));
			// nRow++;
			// }

			// =================================
			// Riga con il provvedimento
			// =================================
			row = sheet.createRow(nRow);
			setCell(row, 0, cont, csCenter);

			// if(lMod.getChiaveProg().intValue() > 999999999)
			if (lMod.getChiaveProgrOrig() != null)
				setCell(row, 1, " " + lMod.getChiaveProgrOrig().toString() + "/"
						+ lMod.getChiaveAnno().toString() + " - " + lMod.getDescUfficioInserimento(),
						csCenter);
			else
				setCell(row, 1, lMod.getChiaveProgr() + "/" + lMod.getChiaveAnno(), csCenter);

			setCell(row, 2, DateUtils.getDateToString(lMod.getDataEmissione(), "dd/MM/yyyy"), csCenter);

			// Test MEV29 - Stile per create formattazioni differenti all'interno della
			// stessa cella. Es parte del testo in BOLD o Rosso per essere evidenziata
			//
			HSSFRichTextString lRichString = new HSSFRichTextString(
					lMod.getDescrTipologia() + " \n " + lMod.getDescrMotivo());
			lRichString.applyFont(0, lMod.getDescrTipologia().length(), fontGruppo);
			// lRichString.applyFont(lMod.getDescrTipologia().length()+1, lRichString.length(), fontGruppo);

			// setCell(row, 3, lMod.getDescrTipologia()+" - "+lMod.getDescrMotivo(), csCenter);
			setCellRichText(row, 3, lRichString, csCenter);

			// MEV_70 : descrizione del Codice_Motivo originale
			setCell(row, (short) 4, lMod.getDescrMotivoOrig(), csCenter);

			cont++;

			// lLastTipologia = lMod.getTipologia();

			nRow++;
		}
	}

	// 20131206
	// Generazione del file excel contenente ricorsi / impugnazioni
	// Utilizza il vettore aElencoProvv che contiene i dati gia' filtrati
	public void creaFoglioElencoRicorsiImpugnazioni(HSSFWorkbook wb, UfficioModel aUffUtenteConnessoModel,
			String aCriterio1, String aCriterio2, String aCriterio3, Vector aElencoProvv)
			throws F3BException {

		int numCol = 0;
		EveFasGepSogProvModel lProvvedimentoModel = null;
		ImpugnazioneDAO lImpugnazioneDao = null;
		ImpugnazioneModel lImpugnazioneModel = null;
		HSSFSheet sheet = wb.createSheet("Elenco Provvedimenti");
		HSSFCellStyle csNull = wb.createCellStyle();
		String lDataDeposito = new String();
		String lGeneralitaSoggetto = new String();
		// String lTipoProvvedimento = new String();
		String lLuogoNascita = new String();
		String lStatoEsecuzione = new String();
		String lStato = new String();
		BigDecimal lIdSoggetto;
		SoggettoModel lSoggettoModel = null;
		HSSFRow lRowExcel;
		BigDecimal lID;
		Iterator lIterator;
		int lProgressivo;
		DocumentoAllegatoDAO lDocumentoAllegatoDao = null;
		Collection lDocumentoAllegatoModels;
		DocumentoAllegatoModel lDocumentoAllegatoModel;
		SoggettoDAO lSoggettoDao = null;
		Collection lSoggettoModels;
		Connection lDBConnection = null;

		int lRow = 0;

		// Intestazione del foglio excel
		lRow = setIntestazione(sheet, aUffUtenteConnessoModel, csNull);

		lRow += 2;
		lRowExcel = sheet.createRow(lRow);
		setCell(lRowExcel, 0, "Elenco Ricorsi/Impugnazioni", csNull);
		lRow += 2;

		lRowExcel = sheet.createRow(lRow);
		setCell(lRowExcel, 0, aCriterio1, csNull);
		lRow++;
		lRowExcel = sheet.createRow(lRow);
		setCell(lRowExcel, 0, aCriterio2, csNull);
		lRow++;
		lRowExcel = sheet.createRow(lRow);
		setCell(lRowExcel, 0, aCriterio3, csNull);
		lRow++;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (10 * 256)); // Progr.
		sheet.setColumnWidth(numCol++, (20 * 256)); // N.ro Ricorso Impugnazione
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Inserimento
		sheet.setColumnWidth(numCol++, (10 * 256)); // Procedimento SIUS
		sheet.setColumnWidth(numCol++, (40 * 256)); // Generalita' Soggetto
		sheet.setColumnWidth(numCol++, (10 * 256)); // Tipo Provv.
		sheet.setColumnWidth(numCol++, (20 * 256)); // Contenuto Atto
		sheet.setColumnWidth(numCol++, (20 * 256)); // Esito Provvedimento
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Emissione
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Deposito
		sheet.setColumnWidth(numCol++, (10 * 256)); // Tipo Impugnazione
		sheet.setColumnWidth(numCol++, (20 * 256)); // Presentato da
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Imp.
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data arr. Cancelleria
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Trasmissione
		sheet.setColumnWidth(numCol++, (20 * 256)); // Aut. Destinataria
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Decisione
		sheet.setColumnWidth(numCol++, (20 * 256)); // Tenore Decisione
		sheet.setColumnWidth(numCol++, (10 * 256)); // Data Rest. Atti
		sheet.setColumnWidth(numCol++, (15 * 256)); // Stato Esecuzione
		sheet.setColumnWidth(numCol++, (15 * 256)); // Stato

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		lRowExcel = sheet.createRow(lRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(lRowExcel, numCol++, "Progr.", csCenter);
		setCell(lRowExcel, numCol++, "N.ro Ricorso Impugnazione", csCenter);
		setCell(lRowExcel, numCol++, "Data Inserimento", csCenter);
		setCell(lRowExcel, numCol++, "Procedimento SIUS", csCenter);
		setCell(lRowExcel, numCol++, "Generalita' Soggetto", csCenter);
		setCell(lRowExcel, numCol++, "Tipo Provv.", csCenter);
		setCell(lRowExcel, numCol++, "Contenuto Atto", csCenter);
		setCell(lRowExcel, numCol++, "Esito Provvedimento", csCenter);
		setCell(lRowExcel, numCol++, "Data Emissione", csCenter);
		setCell(lRowExcel, numCol++, "Data Deposito", csCenter);
		setCell(lRowExcel, numCol++, "Tipo Impugnazione", csCenter);
		setCell(lRowExcel, numCol++, "Presentato da", csCenter);
		setCell(lRowExcel, numCol++, "Data Imp.", csCenter);
		setCell(lRowExcel, numCol++, "Data arr. Cancelleria", csCenter);
		setCell(lRowExcel, numCol++, "Data Trasmissione", csCenter);
		setCell(lRowExcel, numCol++, "Aut. Destinataria", csCenter);
		setCell(lRowExcel, numCol++, "Data Decisione", csCenter);
		setCell(lRowExcel, numCol++, "Tenore Decisione", csCenter);
		setCell(lRowExcel, numCol++, "Data Rest. Atti", csCenter);
		setCell(lRowExcel, numCol++, "Stato Esecuzione", csCenter);
		setCell(lRowExcel, numCol++, "Stato", csCenter);

		try {
			lDBConnection = getDBConnection();

			lImpugnazioneDao = new ImpugnazioneDAO(lDBConnection);

			lIterator = aElencoProvv.iterator();
			lProgressivo = 0;

			// inizio ciclo di scrittura dei dati
			while (lIterator.hasNext()) {
				numCol = 0;

				lProvvedimentoModel = (EveFasGepSogProvModel) lIterator.next();

				// I dati dell-Impugnazione vengono prelevati dal model popolato con
				// i dati provenienti dallo specifico DAO filtrato su ID_IMPUGNAZIONE
				// il metodo setCondizioneUpdate viene utilizzato IMPROPRIAMENTE per poter
				// impostare un filtro sul DAO.
				lID = lProvvedimentoModel.getImpugnazione().getIdImpugnazione();
				lImpugnazioneDao.setCondizioneUpdate(lID);
				lImpugnazioneModel = null;
				try {
					lImpugnazioneModel = (ImpugnazioneModel) lImpugnazioneDao.getModelByKey();
				} catch (DAOException e) {
					throw new F3BException("creaFoglioElencoRicorsiImpugnazioni: ricerca Impugnazione " + e);
				}

				if (lImpugnazioneModel != null) {
					lRowExcel = sheet.createRow(lRow++);

					// I dati del Soggetto vengno prelevati dal model popolato con i dati
					// provenenti dallo specifico DAO filtrato su ID_SOGGETTO
					// si usa IMPROPRIAMENTE il metod selCondizioneUpdate per impostare la condizione
					// di filtro
					lDataDeposito = "";
					lGeneralitaSoggetto = "";
					// lTipoProvvedimento = "";
					lLuogoNascita = "";
					lIdSoggetto = lProvvedimentoModel.getSoggetto().getIdSoggetto();
					lSoggettoModel = null;
					try {
						lSoggettoDao = new SoggettoDAO(lDBConnection);
						lSoggettoDao.selCondizioneUpdate(lIdSoggetto);
						lSoggettoModels = lSoggettoDao.getModels();
						if (lSoggettoModels != null) {
							if (lSoggettoModels.size() > 0) {
								lSoggettoModel = (SoggettoModel) lSoggettoModels.iterator().next();
							}
						}
					} catch (Exception e) {
						throw new F3BException("creaFoglioElencoRicorsiImpugnazioni: ricerca Soggetto " + e);
					}

					if (lSoggettoModel != null) {
						// se il Soggetto e' italiano si riporta la provincia di nascita, altrimenti lo stato
						// di
						// nascita
						if (lSoggettoModel.getCodStatoNascita().compareTo("039") == 0) {
							lLuogoNascita = lSoggettoModel.getDescrComuneNascita() + "("
									+ lSoggettoModel.getCodProvinciaNascita() + ")";
						} else {
							lLuogoNascita = lSoggettoModel.getDescComuneNascitaEstero() + "("
									+ lSoggettoModel.getDescrStatoNascita() + ")";
						}

						lGeneralitaSoggetto = lSoggettoModel.getNome() + " " + lSoggettoModel.getCognome()
								+ "\n"
								+ DateUtils.getDateToString(lSoggettoModel.getDataNascita(), "dd/MM/yyyy")
								+ " " + lLuogoNascita;
					}

					// Progr. +
					setCell(lRowExcel, numCol++, StringUtils.intZerotoString(++lProgressivo), csCenter);

					// N.ro Ricorso Impugnazione +
					setCell(lRowExcel, numCol++,
							lImpugnazioneModel.getAnnoS7() + "/" + lImpugnazioneModel.getProgrS7(), csCenter);

					// Data Inserimento +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DateUtils
									.getDateToString(lImpugnazioneModel.getDataInserimento(), "dd/MM/yyyy")),
							csCenter);

					// Procedimento SIUS +
					setCell(lRowExcel, numCol++, lProvvedimentoModel.getFascicoloSius().getChiaveAnno() + "/"
							+ lProvvedimentoModel.getFascicoloSius().getChiaveProgr(), csCenter);

					// Generalita' Soggetto +
					setCell(lRowExcel, numCol++, lGeneralitaSoggetto, csCenter);

					// Tipo Provv. +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DecodificheUtils.getDescbyCode(
									DecodificheManager.getInstance().getTipoProvvedimenti(),
									lProvvedimentoModel.getEvento().getCodTipoProvvedimento())),
							csCenter);

					// Contenuto Atto +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(lProvvedimentoModel.getEvento().getDescrMotivo()),
							csCenter);

					// Esito Provvedimento +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(lProvvedimentoModel.getEvento().getDescrEsito()), csCenter);

					// Data Emissione +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DateUtils.getDateToString(
									lProvvedimentoModel.getEvento().getDataEmissione(), "dd/MM/yyyy")),
							csCenter);

					// Data Deposito
					// la data deposito viene prelevata dal campo DATA_EMISSIONE del documento allegato
					// E' necessario popolare il model del DocumentoAllegato partendo da ID_EVENTO
					lDataDeposito = "";
					lID = lProvvedimentoModel.getEvento().getIdEvento();
					lDocumentoAllegatoDao = new DocumentoAllegatoDAO(lDBConnection);
					lDocumentoAllegatoDao.setCondizioneByEve(lID);
					try {
						lDocumentoAllegatoModels = lDocumentoAllegatoDao.getModels();
						if (lDocumentoAllegatoModels != null) {
							if (lDocumentoAllegatoModels.size() > 0) {
								lDocumentoAllegatoModel = (DocumentoAllegatoModel) lDocumentoAllegatoModels
										.iterator().next();
								lDataDeposito = DateUtils.getDateToString(
										lDocumentoAllegatoModel.getDataEmissione(), "dd/MM/yyyy");
							}
						}
					} catch (DAOException e) {
						throw new F3BException(
								"creaFoglioElencoRicorsiImpugnazioni: ricerca Documento Allegato " + e);
					}
					setCell(lRowExcel, numCol++, StringUtils.cStrForJS(lDataDeposito), csCenter);

					// Tipo Impugnazione +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DecodificheUtils.getDescbyCode(
									DecodificheManager.getInstance().getTipoRicorso(),
									lImpugnazioneModel.getCodTipoImpugnazione())),
							csCenter);

					// Presentato da +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DecodificheUtils.getDescbyCode(
									DecodificheManager.getInstance().getSoggettoImpugnante(),
									lImpugnazioneModel.getSoggettoImpugnante())),
							csCenter);

					// Data Imp. +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DateUtils
									.getDateToString(lImpugnazioneModel.getDataRicorso(), "dd/MM/yyyy")),
							csCenter);

					// Data arr. Cancelleria +
					setCell(lRowExcel, numCol++, StringUtils.cStrForJS(DateUtils
							.getDateToString(lImpugnazioneModel.getDataArrivoCancelleria(), "dd/MM/yyyy")),
							csCenter);

					// Data Trasmissione +
					setCell(lRowExcel, numCol++, StringUtils.cStrForJS(DateUtils
							.getDateToString(lImpugnazioneModel.getDataTrasmissioneAtti(), "dd/MM/yyyy")),
							csCenter);

					// Aut. Destinataria +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DecodificheUtils.getDescbyCode(
									DecodificheManager.getInstance().getTipoUfficio(),
									lImpugnazioneModel.getCodAutoritaDestinataria())),
							csCenter);

					// Data Decisione +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DateUtils
									.getDateToString(lImpugnazioneModel.getDataDecisione(), "dd/MM/yyyy")),
							csCenter);

					// Tenore Decisione +
					setCell(lRowExcel, numCol++,
							StringUtils.cStrForJS(DecodificheUtils.getDescbyCode(
									DecodificheManager.getInstance().getTenoreDecisioneRicorso(),
									lImpugnazioneModel.getCodTenoreDecisione())),
							csCenter);

					// Data Rest. Atti +
					setCell(lRowExcel, numCol++, StringUtils.cStrForJS(DateUtils
							.getDateToString(lImpugnazioneModel.getDataRestituzioneAtti(), "dd/MM/yyyy")),
							csCenter);

					// Stato Esecuzione +
					lStatoEsecuzione = "";
					if (lImpugnazioneModel.getFlagSospEsec() != null) {
						if (lImpugnazioneModel.getFlagSospEsec().compareTo("S") == 0) {
							lStatoEsecuzione = "Esecuzione sospesa";
						}
					}
					setCell(lRowExcel, numCol++, lStatoEsecuzione, csCenter);

					// Stato +
					lStato = "";
					if (lImpugnazioneModel.getFlagAnnullamento() != null) {
						if (lImpugnazioneModel.getFlagAnnullamento().compareTo("S") == 0) {
							lStato = "ANNULLATO";
						}
					}
					setCell(lRowExcel, numCol++, lStato, csCenter);
				}
			}
		} catch (Exception e) {
			siesLogger.error(e.getMessage());
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSoggettoDao);
			cleanup(lDocumentoAllegatoDao);
			cleanup(lImpugnazioneDao);
			cleanup(lDBConnection);
		}
	}

	public void creaFoglioElencoProc(HSSFWorkbook aWb, UfficioModel aUfficioUtenteConnesso, String aTitolo,
			Vector<EveFasGepSogProvModel> aElencoProc) {

		HSSFSheet lSheet;
		HSSFCellStyle lCellStyleNull;
		HSSFCellStyle lCellStyleCenter;
		HSSFRow lRow;
		Iterator lItx;
		int lContatore = 0;
		int lRowCounter = 0;
		EveFasGepSogProvModel lModel;
		String lPatternData = "dd/MM/yyyy";
		SoggettoModel lSoggettoModel = null;
		DepositoDecretoModel lDepositoDecretoModel = null;
		String lTemp = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### creaFoglioElencoProc START ########");

		lSheet = aWb.createSheet("Elenco Provvedimenti");
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter++;
		lRowCounter++;

		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, aTitolo, lCellStyleNull);
		lRowCounter++;
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, "Elenco provvedimenti Depositati", lCellStyleNull);
		lRowCounter += 2;

		// settaggio della larghezza
		// delle colonne
		lSheet.setColumnWidth(0, (10 * 256)); // Prog
		lSheet.setColumnWidth(1, (25 * 256)); // N.ro Ricosto/Impugnazione
		lSheet.setColumnWidth(2, (15 * 256)); // Data Impungazione
		lSheet.setColumnWidth(3, (15 * 256)); // Procedimento SIUS
		lSheet.setColumnWidth(4, (30 * 256)); // Generalita' Soggetto
		lSheet.setColumnWidth(5, (10 * 256)); // Tipo Provv.
		lSheet.setColumnWidth(6, (10 * 256)); // Contenuto Atto
		lSheet.setColumnWidth(7, (10 * 256)); // Esito Provvedimento
		lSheet.setColumnWidth(8, (10 * 256)); // Data Emissione
		lSheet.setColumnWidth(9, (10 * 256)); // Data Deposito
		lSheet.setColumnWidth(10, (10 * 256)); // Tipo Impugnazione
		lSheet.setColumnWidth(11, (10 * 256)); // Presentato da
		lSheet.setColumnWidth(11, (10 * 256)); // Data Imp.
		lSheet.setColumnWidth(11, (10 * 256)); // Data arr. Cancelleria

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(aWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		// Intestazione colonne
		setCell(lRow, 0, "Prog", lCellStyleCenter);
		setCell(lRow, 1, "N.ro Ricosto/Impugnazione", lCellStyleCenter);
		setCell(lRow, 2, "Data Impungazione", lCellStyleCenter);
		setCell(lRow, 3, "Procedimento SIUS", lCellStyleCenter);
		setCell(lRow, 4, "Generalita' Soggetto", lCellStyleCenter);
		setCell(lRow, 5, "Tipo Provv.", lCellStyleCenter);
		setCell(lRow, 6, "Contenuto Atto", lCellStyleCenter);
		setCell(lRow, 7, "Esito Provvedimento", lCellStyleCenter);
		setCell(lRow, 8, "Data Emissione", lCellStyleCenter);
		setCell(lRow, 9, "Data Deposito", lCellStyleCenter);
		setCell(lRow, 10, "Tipo Impugnazione", lCellStyleCenter);
		setCell(lRow, 11, "Presentato da", lCellStyleCenter);
		setCell(lRow, 11, "Data Imp.", lCellStyleCenter);
		setCell(lRow, 11, "Data arr. Cancelleria", lCellStyleCenter);

		lItx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (lItx.hasNext()) {

			lContatore++;

			lModel = (EveFasGepSogProvModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);
			setCell(lRow, 0, "" + lContatore, lCellStyleCenter);
			setCell(lRow, 1, "?", lCellStyleCenter);
			setCell(lRow, 2, "?", lCellStyleCenter);
			setCell(lRow, 3, "?", lCellStyleCenter);

			lSoggettoModel = lModel.getSoggetto();
			if (lSoggettoModel != null) {
				lTemp = "";
				lTemp += lSoggettoModel.getCognome() != null ? lSoggettoModel.getCognome() + " " : "";
				lTemp += lSoggettoModel.getNome() != null ? lSoggettoModel.getNome() + " " : "";
				lTemp += lSoggettoModel.getDataNascita() != null
						? DateUtils.getDateToString(lSoggettoModel.getDataNascita(), lPatternData) + " "
						: "";
				lTemp += lSoggettoModel.getDescrComuneNascita() != null
						? lSoggettoModel.getDescrComuneNascita() + " "
						: "";
				lTemp += lSoggettoModel.getDescrProvinciaNascita() != null
						? lSoggettoModel.getDescrProvinciaNascita() + " "
						: "";
			} else {
				lTemp = "-";
			}
			setCell(lRow, 4, lTemp, lCellStyleCenter);

			setCell(lRow, 5, "", lCellStyleCenter);
			setCell(lRow, 6, "", lCellStyleCenter);
			setCell(lRow, 7, "", lCellStyleCenter);

			lDepositoDecretoModel = lModel.getDepositoDecreto();
			if (lDepositoDecretoModel != null) {
				lTemp = lDepositoDecretoModel.getDataEmissione() == null ? "-"
						: DateUtils.getDateToString(lDepositoDecretoModel.getDataEmissione(), lPatternData);
			} else {
				lTemp = "-";
			}
			setCell(lRow, 8, lTemp, lCellStyleCenter);

			if (lDepositoDecretoModel != null) {
				lTemp = lDepositoDecretoModel.getDataDeposito() == null ? "-"
						: DateUtils.getDateToString(lDepositoDecretoModel.getDataDeposito(), lPatternData);
			} else {
				lTemp = "-";
			}
			setCell(lRow, 9, lTemp, lCellStyleCenter);

			setCell(lRow, 10, "Tipo Impugnazione", lCellStyleCenter);
			setCell(lRow, 11, "Presentato da", lCellStyleCenter);
			setCell(lRow, 11, "Data Imp.", lCellStyleCenter);
			setCell(lRow, 11, "Data arr. Cancelleria", lCellStyleCenter);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("######### creaFoglioElencoProc STOP ########");
	}

	public HSSFWorkbook creaFoglioFoglioComplementare(UfficioModel aUfficioUtenteConnesso,
			RicercaOrdinanzaModel aRicerca) throws F3BException {

		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		EveFasGepSogProvModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Vector lElenco = null;
		int lContatore = 0;

		lWb = new HSSFWorkbook();

		// creazione primo foglio
		lSheet = lWb.createSheet("Elenco Fogli Complementari");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		// Stampa filtri di ricerca
		if (aRicerca != null) {

			if (aRicerca.getDataEmissioneIniziale() != null && aRicerca.getDataEmissioneFinale() != null) {
				lBuffer = "Data di emissione tra " + StringUtils.toStringJSP(
						DateUtils.getDateToString(aRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy"), "-")
						+ " e "
						+ StringUtils.toStringJSP(
								DateUtils.getDateToString(aRicerca.getDataEmissioneFinale(), "dd-MM-yyyy"),
								"-");

				lRow = lSheet.createRow(lRowCounter);
				HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicerca.getAnnoIniziale() != null && aRicerca.getNumIniziale() != null
					&& aRicerca.getAnnoFinale() != null && aRicerca.getNumFinale() != null) {

				lBuffer = "Dal N. " + StringUtils.toStringJSP(aRicerca.getAnnoIniziale(), "-") + "/"
						+ StringUtils.toStringJSP(aRicerca.getNumIniziale(), "-") + " al "
						+ StringUtils.toStringJSP(aRicerca.getAnnoFinale(), "-") + "/"
						+ StringUtils.toStringJSP(aRicerca.getNumFinale(), "-");

				lRow = lSheet.createRow(lRowCounter);
				HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicerca.getStatoValidazione() == null) {
				lBuffer = "Tutti";
			} else if (aRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
				lBuffer = "Annullato";
			} else if (aRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI)) {
				lBuffer = "Non Annullato";
			} else {
				lBuffer = "Tutti";
			}

			lBuffer = "Stato: " + lBuffer;

			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 10 * 256); // Prog
		lSheet.setColumnWidth(1, 15 * 256); // Anno Progressivo Foglio Complementare
		lSheet.setColumnWidth(2, 15 * 256); // Data Compilazione
		lSheet.setColumnWidth(3, 15 * 256); // Procedimento SIUS
		lSheet.setColumnWidth(4, 40 * 256); // Generalita' Soggetto
		lSheet.setColumnWidth(5, 15 * 256); // Tipo Atto
		lSheet.setColumnWidth(6, 20 * 256); // Contenuto Atto
		lSheet.setColumnWidth(7, 15 * 256); // Esito Provvedimento
		lSheet.setColumnWidth(8, 15 * 256); // Data Emissione
		lSheet.setColumnWidth(9, 15 * 256); // Stato

		// Intestazione colonne
		HSSFUtils.getInstance().setCell(lRow, 0, "Prog.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 1, "Anno/Prog. F.C.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 2, "Data Compilazione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 3, "Procedimento SIUS", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 4, "Generalita' Soggetto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 5, "Tipo Atto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 6, "Contenuto Atto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 7, "Esito Provvedimento", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 8, "Data Emissione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 9, "Stato", lCellStyleCenter);

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcSiusXProvvedimentiPaginata(aRicerca, -1);

		lContatore = 1;
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogProvModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			HSSFUtils.getInstance().setCell(lRow, 0, "" + lContatore++, lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 1,
					(lModel.getDocumentoAllegato() != null
							&& lModel.getDocumentoAllegato().getAnnoFoglioComplementare() != null
							&& lModel.getDocumentoAllegato().getProgrFoglioComplementare() != null)
									? lModel.getDocumentoAllegato().getAnnoFoglioComplementare() + "/"
											+ lModel.getDocumentoAllegato().getProgrFoglioComplementare()
									: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 2,
					lModel.getGeneraleProcedimento() != null
							? StringUtils.cStrForJS(DateUtils.getDateToString(
									lModel.getGeneraleProcedimento().getDataCameraConsiglio(), lPatternData))
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 3,
					(lModel.getFascicoloSius() != null && lModel.getFascicoloSius().getChiaveAnno() != null
							&& lModel.getFascicoloSius().getChiaveProgr() != null)
									? lModel.getFascicoloSius().getChiaveAnno() + "/"
											+ lModel.getFascicoloSius().getChiaveProgr()
									: "-",
					lCellStyleCenter);
			if (lModel.getSoggetto() != null) {
				lBuffer = StringUtils.cStrForJS(lModel.getSoggetto().getCognome()) + " "
						+ StringUtils.cStrForJS(lModel.getSoggetto().getNome()) + "\n";
				lBuffer += StringUtils.cStrForJS(
						DateUtils.getDateToString(lModel.getSoggetto().getDataNascita(), lPatternData));
				lBuffer += " ";
				if (lModel.getSoggetto().getCodStatoNascita() != null) {
					if (lModel.getSoggetto().getCodStatoNascita().compareTo("039") == 0) {
						lBuffer += StringUtils.cStrForJS(lModel.getSoggetto().getDescrComuneNascita()) + " ("
								+ StringUtils.cStrForJS(lModel.getSoggetto().getCodProvinciaNascita()) + ")";
					} else {
						lBuffer += StringUtils.cStrForJS(lModel.getSoggetto().getDescComuneNascitaEstero());
						lBuffer += " (" + StringUtils.cStrForJS(lModel.getSoggetto().getDescrStatoNascita())
								+ ")";
					}
				}
			} else {
				lBuffer = "";
			}
			HSSFUtils.getInstance().setCell(lRow, 4, lBuffer, lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, 5,
					(lModel.getEvento() != null && lModel.getEvento().getDescrTipoProvvedimento() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrTipoProvvedimento())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 6,
					(lModel.getEvento() != null && lModel.getEvento().getDescrMotivo() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrMotivo())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 7,
					(lModel.getEvento() != null && lModel.getEvento().getDescrEsito() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrEsito())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 8,
					(lModel.getDocumentoAllegato() != null
							&& lModel.getDocumentoAllegato().getDataEmissione() != null)
									? DateUtils.getDateToString(
											lModel.getDocumentoAllegato().getDataEmissione(), lPatternData)
									: "-",
					lCellStyleCenter);
			if (lModel.getDocumentoAllegato() != null
					&& lModel.getDocumentoAllegato().getFlagDocumentoRegistrato() != null) {
				if (lModel.getDocumentoAllegato().getFlagDocumentoRegistrato()
						.compareTo(ICostantiStatistiche.ANNULLATI) == 0) {
					HSSFUtils.getInstance().setCell(lRow, 9, "ANNULLATO", lCellStyleCenter);
				} else {
					HSSFUtils.getInstance().setCell(lRow, 9, "", lCellStyleCenter);
				}
			} else {
				HSSFUtils.getInstance().setCell(lRow, 9, "-", lCellStyleCenter);
			}
		}

		return lWb;
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	private int getNumTipologieAttivita() throws F3BException {

		int numRecord = 0;

		Connection lConn = null;

		IspAttivitaMagistratiSqlDAO lIspDao = null;

		try {
			lConn = getDBConnection();
			lIspDao = new IspAttivitaMagistratiSqlDAO(lConn);

			lIspDao.getNumTipologieAttivita();
			lIspDao.start();
			lIspDao.next();
			numRecord = lIspDao.getInt("contaTipologie");
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: ", sqe);
			throw new F3BException("StatisController.getNumTipologieAttivita: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lIspDao);

			cleanup(lConn);
		}
		return numRecord;
	}

	/**
	 *
	 * @param aCodiciSelezionati
	 * @param aTipoTitolo
	 * @return
	 * @throws F3BException
	 */
	public Vector<String> getTitoliPerCodici(String[] aCodiciSelezionati, String aTipoTitolo)
			throws F3BException {

		Vector<String> lElencoTitoli = new Vector<>();
		Connection lConn = null;
		IspProvvedimentiSqlDAO lIspSqlDao = null;

		try {
			lConn = getDBConnection();
			lIspSqlDao = new IspProvvedimentiSqlDAO(lConn);
			lIspSqlDao.getTitoliPerCodiciPerTipoTitolo(aCodiciSelezionati, aTipoTitolo);
			lIspSqlDao.start();
			while (lIspSqlDao.next()) {
				lElencoTitoli.add(lIspSqlDao.getString(aTipoTitolo));
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StatisController.getTitoliPerCodici: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspSqlDao);
			cleanup(lConn);
		}

		return lElencoTitoli;
	}

	public HSSFWorkbook creaFoglioFoglioComplementare(UfficioModel aUfficioUtenteConnesso,
			RicercaProvvedimentoModel aRicerca) throws F3BException {

		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator lItx = null;
		int lRowCounter = 0;
		EveFasGepSogProvModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Vector lElenco = null;
		int lContatore = 0;

		lWb = new HSSFWorkbook();

		// creazione primo foglio
		lSheet = lWb.createSheet("Elenco Fogli Complementari");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		// Stampa filtri di ricerca
		if (aRicerca != null) {

			if (aRicerca.getDataEmissioneIniziale() != null && aRicerca.getDataEmissioneFinale() != null) {
				lBuffer = "Data di emissione tra " + StringUtils.toStringJSP(
						DateUtils.getDateToString(aRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy"), "-")
						+ " e "
						+ StringUtils.toStringJSP(
								DateUtils.getDateToString(aRicerca.getDataEmissioneFinale(), "dd-MM-yyyy"),
								"-");

				lRow = lSheet.createRow(lRowCounter);
				HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicerca.getAnnoIniziale() != null && aRicerca.getNumIniziale() != null
					&& aRicerca.getAnnoFinale() != null && aRicerca.getNumFinale() != null) {

				lBuffer = "Dal N. " + StringUtils.toStringJSP(aRicerca.getAnnoIniziale(), "-") + "/"
						+ StringUtils.toStringJSP(aRicerca.getNumIniziale(), "-") + " al "
						+ StringUtils.toStringJSP(aRicerca.getAnnoFinale(), "-") + "/"
						+ StringUtils.toStringJSP(aRicerca.getNumFinale(), "-");

				lRow = lSheet.createRow(lRowCounter);
				HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
				lRowCounter++;
			}

			if (aRicerca.getStatoValidazione() == null) {
				lBuffer = "Tutti";
			} else if (aRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI)) {
				lBuffer = "Annullato";
			} else if (aRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI)) {
				lBuffer = "Non Annullato";
			} else {
				lBuffer = "Tutti";
			}

			lBuffer = "Stato: " + lBuffer;

			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, lBuffer, lCellStyleNull);
			lRowCounter++;
		}

		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 10 * 256); // Prog
		lSheet.setColumnWidth(1, 15 * 256); // Anno Progressivo Foglio Complementare
		lSheet.setColumnWidth(2, 15 * 256); // Data Compilazione
		lSheet.setColumnWidth(3, 15 * 256); // Procedimento SIUS
		lSheet.setColumnWidth(4, 40 * 256); // Generalita' Soggetto
		lSheet.setColumnWidth(5, 15 * 256); // Tipo Atto
		lSheet.setColumnWidth(6, 20 * 256); // Contenuto Atto
		lSheet.setColumnWidth(7, 15 * 256); // Esito Provvedimento
		lSheet.setColumnWidth(8, 15 * 256); // Data Emissione
		lSheet.setColumnWidth(9, 15 * 256); // Stato

		// Intestazione colonne
		HSSFUtils.getInstance().setCell(lRow, 0, "Prog.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 1, "Anno/Prog. F.C.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 2, "Data Compilazione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 3, "Procedimento SIUS", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 4, "Generalita' Soggetto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 5, "Tipo Atto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 6, "Contenuto Atto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 7, "Esito Provvedimento", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 8, "Data Emissione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, 9, "Stato", lCellStyleCenter);

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaProcSiusXProvvedimentiPaginata(aRicerca, -1);

		lContatore = 1;
		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = (EveFasGepSogProvModel) lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			HSSFUtils.getInstance().setCell(lRow, 0, "" + lContatore++, lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 1,
					(lModel.getDocumentoAllegato() != null
							&& lModel.getDocumentoAllegato().getAnnoFoglioComplementare() != null
							&& lModel.getDocumentoAllegato().getProgrFoglioComplementare() != null)
									? lModel.getDocumentoAllegato().getAnnoFoglioComplementare() + "/"
											+ lModel.getDocumentoAllegato().getProgrFoglioComplementare()
									: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 2,
					lModel.getGeneraleProcedimento() != null
							? StringUtils.cStrForJS(DateUtils.getDateToString(
									lModel.getGeneraleProcedimento().getDataCameraConsiglio(), lPatternData))
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 3,
					(lModel.getFascicoloSius() != null && lModel.getFascicoloSius().getChiaveAnno() != null
							&& lModel.getFascicoloSius().getChiaveProgr() != null)
									? lModel.getFascicoloSius().getChiaveAnno() + "/"
											+ lModel.getFascicoloSius().getChiaveProgr()
									: "-",
					lCellStyleCenter);
			if (lModel.getSoggetto() != null) {
				lBuffer = StringUtils.cStrForJS(lModel.getSoggetto().getCognome()) + " "
						+ StringUtils.cStrForJS(lModel.getSoggetto().getNome()) + "\n";
				lBuffer += StringUtils.cStrForJS(
						DateUtils.getDateToString(lModel.getSoggetto().getDataNascita(), lPatternData));
				lBuffer += " ";
				if (lModel.getSoggetto().getCodStatoNascita() != null) {
					if (lModel.getSoggetto().getCodStatoNascita().compareTo("039") == 0) {
						lBuffer += StringUtils.cStrForJS(lModel.getSoggetto().getDescrComuneNascita()) + " ("
								+ StringUtils.cStrForJS(lModel.getSoggetto().getCodProvinciaNascita()) + ")";
					} else {
						lBuffer += StringUtils.cStrForJS(lModel.getSoggetto().getDescComuneNascitaEstero());
						lBuffer += " (" + StringUtils.cStrForJS(lModel.getSoggetto().getDescrStatoNascita())
								+ ")";
					}
				}
			} else {
				lBuffer = "";
			}
			HSSFUtils.getInstance().setCell(lRow, 4, lBuffer, lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, 5,
					(lModel.getEvento() != null && lModel.getEvento().getDescrTipoProvvedimento() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrTipoProvvedimento())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 6,
					(lModel.getEvento() != null && lModel.getEvento().getDescrMotivo() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrMotivo())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 7,
					(lModel.getEvento() != null && lModel.getEvento().getDescrEsito() != null)
							? StringUtils.cStrForJS(lModel.getEvento().getDescrEsito())
							: "-",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, 8,
					(lModel.getDocumentoAllegato() != null
							&& lModel.getDocumentoAllegato().getDataEmissione() != null)
									? DateUtils.getDateToString(
											lModel.getDocumentoAllegato().getDataEmissione(), lPatternData)
									: "-",
					lCellStyleCenter);
			if (lModel.getDocumentoAllegato() != null
					&& lModel.getDocumentoAllegato().getFlagDocumentoRegistrato() != null) {
				if (lModel.getDocumentoAllegato().getFlagDocumentoRegistrato()
						.compareTo(ICostantiStatistiche.ANNULLATI) == 0) {
					HSSFUtils.getInstance().setCell(lRow, 9, "ANNULLATO", lCellStyleCenter);
				} else {
					HSSFUtils.getInstance().setCell(lRow, 9, "", lCellStyleCenter);
				}
			} else {
				HSSFUtils.getInstance().setCell(lRow, 9, "-", lCellStyleCenter);
			}
		}

		return lWb;
	}

	public HSSFWorkbook getReportStatisticheFogliComplementari(
			StatisticheFogliComplementariContainerModel container) {

		HSSFWorkbook wb = new HSSFWorkbook();
		Vector<StatisticheFogliComplementariModel> provvedimentiConFCTrasmessi = container
				.getProvvedimentiConFc();
		Vector<StatisticheFogliComplementariModel> iscrittiManualmente = container.getFcIscrittiManualmente();
		Vector<StatisticheFogliComplementariModel> fcNonTrasmessi = container.getFcNonTrasmessi();
		Vector<StatisticheFogliComplementariModel> fcTrasmessiConErrore = container.getFcTrasmessiConErrore();
		Vector<StatisticheFogliComplementariModel> fcAnnullati = container.getFcAnnullati();
		// Vector<StatisticheFogliComplementariModel> provvedimentiPriviFC =
		// container.getProvvedimentiPriviFc();

		int row = 0;

		HSSFSheet riepilogoSheet = wb.createSheet("Riepilogo");
		row = this.writeIntestazioneRiepilogo(container, riepilogoSheet, wb);
		this.elaboraRiepilogo(riepilogoSheet, row, container, wb);

		if (provvedimentiConFCTrasmessi != null) {
			HSSFSheet sheetProvvedimentiConFc = wb.createSheet("FC Trasmessi");
			row = writeIntestazioneStatisticheFC(container, sheetProvvedimentiConFc, wb);
			this.elaboraSheetStatisticheFC(provvedimentiConFCTrasmessi, sheetProvvedimentiConFc, row);
		}

		if (iscrittiManualmente != null) {
			HSSFSheet sheetIscrittiManualmente = wb
					.createSheet("FC Iscritti Manualmente o con altre opzioni");
			row = writeIntestazioneStatisticheFC(container, sheetIscrittiManualmente, wb);
			this.elaboraSheetStatisticheFC(iscrittiManualmente, sheetIscrittiManualmente, row);
		}

		if (fcNonTrasmessi != null) {
			HSSFSheet sheetProvvedimentiConFc = wb
					.createSheet("Provvedimenti con FC compilati ma non trasmessi");
			row = writeIntestazioneStatisticheFC(container, sheetProvvedimentiConFc, wb);
			this.elaboraSheetStatisticheFC(fcNonTrasmessi, sheetProvvedimentiConFc, row);
		}

		if (fcTrasmessiConErrore != null) {
			HSSFSheet sheetProvvedimentiConFc = wb.createSheet("FC Trasmessi con Errore");
			row = writeIntestazioneStatisticheFC(container, sheetProvvedimentiConFc, wb);
			this.elaboraSheetStatisticheFC(fcTrasmessiConErrore, sheetProvvedimentiConFc, row);
		}

		if (fcAnnullati != null) {
			HSSFSheet sheetAnnullati = wb.createSheet("FC Annullati");
			row = writeIntestazioneStatisticheFC(container, sheetAnnullati, wb);
			this.elaboraSheetStatisticheFC(fcAnnullati, sheetAnnullati, row);
		}

		/*
		 * if (provvedimentiPriviFC != null) { HSSFSheet sheetProvvedimentiPriviFc =
		 * wb.createSheet("Provvedimenti privi di FC"); row = writeIntestazioneStatisticheFC(container,
		 * sheetProvvedimentiPriviFc, wb); this.elaboraSheetStatisticheFC(provvedimentiPriviFC,
		 * sheetProvvedimentiPriviFc, row); }
		 */

		/*
		 * if (provvedimentiConFC != null && provvedimentiConFC.size() > 0) { HSSFSheet
		 * sheetProvvedimentiConFc = wb.createSheet("Provvedimenti Con FC"); row =
		 * writeIntestazioneStatisticheFC(container, sheetProvvedimentiConFc, wb);
		 * this.elaboraSheetStatisticheFC(provvedimentiConFC, sheetProvvedimentiConFc, row); }
		 */

		// valore di ritorno
		return wb;
	}

	private int writeIntestazioneRiepilogo(StatisticheFogliComplementariContainerModel container,
			HSSFSheet sheet, HSSFWorkbook wb) {

		int nRow = 0;
		HSSFRow row = sheet.createRow(nRow);
		HSSFCellStyle boldStyle = this.getBoldStyle(wb);

		UfficioModel uffUteConnesso = container.getUffUteConnesso();
		RicercaFogliCompModel filtro = container.getFiltro();
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, 0, "Ufficio", boldStyle);
		setCell(row, 1, value);

		row = sheet.createRow(++nRow);
		setCell(row, 0, "", boldStyle);

		row = sheet.createRow(++nRow);
		setCell(row, 0, "Elaborato il: ", boldStyle);
		Date oggi = new Date();

		setCell(row, 1, DateUtils.getDateToString(oggi, "dd-MM-yyyy"));

		String lCriterio1 = "";
		String lCriterio2 = "";

		if (filtro.getAnnoIniziale() != null) {
			if (filtro.getAnnoFinale() == null) {
				lCriterio1 = "Anno: " + filtro.getAnnoIniziale();
			}

			if (filtro.getAnnoFinale() != null) {
				lCriterio1 = "Dall'Anno: " + filtro.getAnnoIniziale() + " all'anno " + filtro.getAnnoFinale();
			}
		}

		if (filtro.getDataEmissioneIniziale() != null) {
			if (filtro.getDataEmissioneFinale() == null) {
				lCriterio2 = "Data Compilazione: "
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy");
			}

			if (filtro.getDataEmissioneFinale() != null) {
				lCriterio2 = "Data Compilazione dal"
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy") + " al "
						+ DateUtils.getDateToString(filtro.getDataEmissioneFinale(), "dd-MM-yyyy");
			}
		}

		row = sheet.createRow(++nRow);
		setCell(row, 0, "Criteri di Ricerca selezionati:", boldStyle);
		setCell(row, 1, lCriterio1);
		setCell(row, 2, lCriterio2);

		row = sheet.createRow(++nRow);
		row = sheet.createRow(++nRow);
		row = sheet.createRow(++nRow);

		Vector<String> testataRiepilogo = container.getTestataRiepilogo();
		Iterator<String> it = testataRiepilogo.iterator();
		int cellIndex = 0;
		while (it.hasNext()) {
			setCell(row, cellIndex, it.next(), boldStyle);
			cellIndex++;
		}

		setCell(row, cellIndex, "Totale", boldStyle);

		row = sheet.createRow(++nRow);
		setCell(row, 0, "", boldStyle);

		return nRow;
	}

	private int elaboraRiepilogo(HSSFSheet riepilogoSheet, int nRow,
			StatisticheFogliComplementariContainerModel container, HSSFWorkbook wb) {

		Vector<RiepilogoStatisticheFogliComplementari> provvedimentiConFC = container
				.getRiepilogoProvvedimentiConFC();
		Vector<RiepilogoStatisticheFogliComplementari> fcIscrittiManualmente = container
				.getRiepilogFCIscrittiManualmente();
		Vector<RiepilogoStatisticheFogliComplementari> fcNonTrasmessi = container
				.getRiepilogoFCNonTrasmessi();
		Vector<RiepilogoStatisticheFogliComplementari> fcTrasmessiConErrore = container
				.getRiepilogoFCTrasmessiConErrore();
		Vector<RiepilogoStatisticheFogliComplementari> fcAnnullati = container.getRiepilogoAnnullati();
		// Vector<RiepilogoStatisticheFogliComplementari> provvedimentiPriviFC = container
		// .getRiepilogoProvvedimentiPriviFC();

		Vector<StatisticheFogliComplementariModel> provvConFC = container.getProvvedimentiConFc();
		Vector<StatisticheFogliComplementariModel> iscrittiManualmente = container.getFcIscrittiManualmente();
		Vector<StatisticheFogliComplementariModel> nonTrasmessi = container.getFcNonTrasmessi();
		Vector<StatisticheFogliComplementariModel> trasmessiConErrore = container.getFcTrasmessiConErrore();
		Vector<StatisticheFogliComplementariModel> annullati = container.getFcAnnullati();
		// Vector<StatisticheFogliComplementariModel> provvPriviFC = container.getProvvedimentiPriviFc();

		HSSFCellStyle boldStyle = this.getBoldStyle(wb);

		if (provvedimentiConFC.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = provvedimentiConFC.iterator();
			int totale = 0;
			int cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), boldStyle);
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		} else if (provvConFC != null) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			int cellIndex = 0;
			setCell(row, cellIndex, "Fogli Complementari Trasmessi", boldStyle);
			setCell(row, ++cellIndex, "0");
			setCell(row, ++cellIndex, "0");
		}

		if (fcIscrittiManualmente.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = fcIscrittiManualmente.iterator();
			int totale = 0;
			int cellIndex = 0;

			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), boldStyle);
			cellIndex += 1;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				// BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex += 1;
				setCell(row, cellIndex, riepilogo.getConteggio().toString());
				totale += riepilogo.getConteggio().intValue();
			}
			cellIndex += 1;
			setCell(row, cellIndex, String.valueOf(totale));
		} else if (iscrittiManualmente != null) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			int cellIndex = 0;
			setCell(row, cellIndex, "FC Iscritti Manualmente o con altre opzioni", boldStyle);
			setCell(row, ++cellIndex, "0");
			setCell(row, ++cellIndex, "0");
		}

		if (fcNonTrasmessi.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = fcNonTrasmessi.iterator();
			int totale = 0;
			int cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), boldStyle);
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		} else if (nonTrasmessi != null) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			int cellIndex = 0;
			setCell(row, cellIndex, "Provvedimenti con FC compilati ma non Trasmessi", boldStyle);
			setCell(row, ++cellIndex, "0");
			setCell(row, ++cellIndex, "0");
		}

		if (fcTrasmessiConErrore.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = fcTrasmessiConErrore.iterator();
			int totale = 0;
			int cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), boldStyle);
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		} else if (trasmessiConErrore != null) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			int cellIndex = 0;
			setCell(row, cellIndex, "Fogli Complementari Trasmessi con Errore", boldStyle);
			setCell(row, ++cellIndex, "0");
			setCell(row, ++cellIndex, "0");
		}

		if (fcAnnullati.size() > 0) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			Iterator<RiepilogoStatisticheFogliComplementari> it = fcAnnullati.iterator();
			int totale = 0;
			int cellIndex = 0;
			RiepilogoStatisticheFogliComplementari riepilogo = it.next();
			setCell(row, cellIndex, riepilogo.getDescrizione(), boldStyle);
			cellIndex++;
			setCell(row, cellIndex, riepilogo.getConteggio().toString());
			totale += riepilogo.getConteggio().intValue();

			while (it.hasNext()) {
				riepilogo = it.next();
				BigDecimal totAnno = riepilogo.getConteggio();
				cellIndex++;
				setCell(row, cellIndex, totAnno.toString());
				totale += totAnno.intValue();
			}

			setCell(row, ++cellIndex, String.valueOf(totale));
		} else if (annullati != null) {
			HSSFRow row = riepilogoSheet.createRow(++nRow);
			int cellIndex = 0;
			setCell(row, cellIndex, "Fogli Complementari Annullati", boldStyle);
			setCell(row, ++cellIndex, "0");
			setCell(row, ++cellIndex, "0");
		}

		/*
		 * if (provvedimentiPriviFC.size() > 0) { HSSFRow row = riepilogoSheet.createRow(++nRow);
		 * Iterator<RiepilogoStatisticheFogliComplementari> it = provvedimentiPriviFC.iterator(); int totale =
		 * 0; int cellIndex = 0; RiepilogoStatisticheFogliComplementari riepilogo = it.next(); setCell(row,
		 * cellIndex, riepilogo.getDescrizione(), boldStyle); cellIndex++; setCell(row, cellIndex,
		 * riepilogo.getConteggio().toString()); totale += riepilogo.getConteggio().intValue();
		 *
		 * while (it.hasNext()) { riepilogo = it.next(); BigDecimal totAnno = riepilogo.getConteggio();
		 * cellIndex++; setCell(row, cellIndex, totAnno.toString()); totale += totAnno.intValue(); }
		 *
		 * setCell(row, ++cellIndex, String.valueOf(totale)); } else if (provvPriviFC != null) { HSSFRow row =
		 * riepilogoSheet.createRow(++nRow); int cellIndex = 0; setCell(row, cellIndex,
		 * "Provvedimenti Privi di Fogli Complementari", boldStyle); setCell(row, ++cellIndex, "0");
		 * setCell(row, ++cellIndex, "0"); }
		 */

		// valore di ritorno
		return nRow;
	}

	private int writeIntestazioneStatisticheFC(StatisticheFogliComplementariContainerModel container,
			HSSFSheet sheet, HSSFWorkbook wb) {

		int nRow = 0;
		HSSFRow row = sheet.createRow(nRow);
		HSSFCellStyle boldStyle = this.getBoldStyle(wb);
		UfficioModel uffUteConnesso = container.getUffUteConnesso();
		RicercaFogliCompModel filtro = container.getFiltro();
		String value = (uffUteConnesso.getDescrTipoUfficio().toUpperCase() + " DI "
				+ uffUteConnesso.getDescrComune().toUpperCase());
		setCell(row, 0, "Ufficio", boldStyle);
		setCell(row, 1, value);

		row = sheet.createRow(++nRow);
		setCell(row, 0, "", boldStyle);

		row = sheet.createRow(++nRow);
		setCell(row, 0, "Elaborato il: ", boldStyle);
		Date oggi = new Date();

		setCell(row, 1, DateUtils.getDateToString(oggi, "dd-MM-yyyy"));

		String lCriterio1 = "";
		String lCriterio2 = "";

		if (filtro.getAnnoIniziale() != null) {
			if (filtro.getAnnoFinale() == null) {
				lCriterio1 = "Anno: " + filtro.getAnnoIniziale();
			}

			if (filtro.getAnnoFinale() != null) {
				lCriterio1 = "Dall'Anno: " + filtro.getAnnoIniziale() + " all'anno " + filtro.getAnnoFinale();
			}
		}

		if (filtro.getDataEmissioneIniziale() != null) {
			if (filtro.getDataEmissioneFinale() == null) {
				lCriterio2 = "Data Compilazione: "
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy");
			}

			if (filtro.getDataEmissioneFinale() != null) {
				lCriterio2 = "Data Compilazione dal"
						+ DateUtils.getDateToString(filtro.getDataEmissioneIniziale(), "dd-MM-yyyy") + " al "
						+ DateUtils.getDateToString(filtro.getDataEmissioneFinale(), "dd-MM-yyyy");
			}
		}

		row = sheet.createRow(++nRow);
		setCell(row, 0, "Criteri di Ricerca selezionati:", boldStyle);
		setCell(row, 1, lCriterio1);
		setCell(row, 2, lCriterio2);

		row = sheet.createRow(++nRow);
		row = sheet.createRow(++nRow);
		setCell(row, 0, "Numero SIGE", boldStyle);
		setCell(row, 1, "Data Provvedimento", boldStyle);
		setCell(row, 2, "Provvedimento", boldStyle);
		setCell(row, 3, "Data Foglio Complementare", boldStyle);
		setCell(row, 4, "Esito", boldStyle);

		return nRow;
	}

	private int elaboraSheetStatisticheFC(Vector<StatisticheFogliComplementariModel> dati, HSSFSheet sheet,
			int nRow) {

		Iterator<StatisticheFogliComplementariModel> it = dati.iterator();
		while (it.hasNext()) {
			StatisticheFogliComplementariModel model = it.next();
			HSSFRow row = sheet.createRow(++nRow);

			setCell(row, 0, model.getDescrFascicolo());
			setCell(row, 1, DateUtils.getDateToString(model.getDataProvvedimento(), "dd-MM-yyyy"));
			setCell(row, 2, model.getDescrProvvedimento());
			setCell(row, 3, model.getDataFoglioComplementare());
			setCell(row, 4, model.getDescrEsito());
		}
		return nRow;
	}

	private HSSFCellStyle getBoldStyle(HSSFWorkbook wb) {

		HSSFCellStyle boldStyle = wb.createCellStyle();
		HSSFFont fontBold = wb.createFont();

		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldStyle.setFont(fontBold);
		return boldStyle;
	}

	/**
	 * La funzione prepara la pagina xls con l'elenco dei Fogli Complementari ricercati L'elenco dei FC nella
	 * lista e' quello passato attraverso il parametro aElencoProvv.
	 *
	 * @param wb
	 * @param aUuffUteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param aDescOggetto
	 */
	public void creaFoglioElencoFogliComplementari(HSSFWorkbook wb, UfficioModel aUuffUteConnesso,
			String aCriterio1, String aCriterio2, String aCriterio3, Vector aElencoProvv)
			throws F3BException {

		int numCol = 0;
		EveFasGepSogProvModel lProvvedimento = null;

		HSSFSheet sheet = wb.createSheet("ElencoFogliComplementari");
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, aUuffUteConnesso, csNull);

		nRow++;
		nRow++;
		// HSSFRow row = sheet.createRow(nRow);
		// setCell(row, 0, aTitolo , csNull);
		// nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio1, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio2, csNull);
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, aCriterio3, csNull);
		nRow++;
		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Fogli Complementari ", csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		numCol = 0;
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));
		sheet.setColumnWidth(numCol++, (40 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (20 * 256));
		sheet.setColumnWidth(numCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenter.setWrapText(true);

		row = sheet.createRow(nRow++);

		// Intestazione colonne
		numCol = 0;
		setCell(row, numCol++, "Progr.", csCenter);
		setCell(row, numCol++, "Numero Foglio Complementare", csCenter);
		setCell(row, numCol++, "Numero SIUS", csCenter);
		setCell(row, numCol++, "Cognome Nome", csCenter);
		setCell(row, numCol++, "Data Emissione", csCenter);
		setCell(row, numCol++, "Data Emissione", csCenter);
		setCell(row, numCol++, "Oggetto", csCenter);
		setCell(row, numCol++, "Esito", csCenter);
		setCell(row, numCol++, "Stato", csCenter);

		Iterator itx = aElencoProvv.iterator();
		int progr = 0;

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			numCol = 0;

			// Inizializzazione Date
			lProvvedimento = (EveFasGepSogProvModel) itx.next();

			String numFC = new String();
			// String dataDeposito = new String();
			String generalitaSoggetto = new String();
			String stato = new String();

			String flagRegistrato = lProvvedimento.getEvento().getFlagDocumentoRegistrato();
			if (flagRegistrato != null && flagRegistrato.equals("A")) {
				stato = "ANNULLATO";
			} else {
				stato = "";
			}

			// Scrittura riga del report
			row = sheet.createRow(nRow++);

			numFC = lProvvedimento.getDocumentoAllegato().getAnnoFoglioComplementare() + "/"
					+ lProvvedimento.getDocumentoAllegato().getProgrFoglioComplementare();

			// // Ordinanza
			// if (lProvvedimento.getDepositoOrdinanzaPc() != null) {
			// dataDeposito = DateUtils.getDateToString(lProvvedimento.getDepositoOrdinanzaPc()
			// .getDataDeposito(), "dd/MM/yyyy");
			// }
			//
			// // Decreto
			// if (lProvvedimento.getDepositoDecreto() != null) {
			// dataDeposito = DateUtils.getDateToString(lProvvedimento.getDepositoDecreto()
			// .getDataDeposito(), "dd/MM/yyyy");
			// }

			BigDecimal lIdSoggetto = lProvvedimento.getSoggetto().getIdSoggetto();
			SoggettoModel lSoggetto = new SoggettoModel();

			try {
				ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();
				lSoggetto = lCtrlSoggetto.ExRicercaSoggettoByKey(lIdSoggetto);
			} catch (Exception e) {
				throw new F3BException("creaFoglioElencoFogliComplementari: ricerca Soggetto " + e);
			}

			generalitaSoggetto = lSoggetto.getCognome() + " " + lSoggetto.getNome();

			setCell(row, numCol++, new Integer(++progr).toString(), csCenter);
			setCell(row, numCol++, numFC, csCenter);
			setCell(row, numCol++, lProvvedimento.getFascicoloSius().getChiaveAnno() + "/"
					+ lProvvedimento.getFascicoloSius().getChiaveProgr(), csCenter);
			setCell(row, numCol++, generalitaSoggetto, csCenter);
			setCell(row, numCol++,
					DateUtils.getDateToString(lProvvedimento.getEvento().getDataEmissione(), "dd/MM/yyyy"),
					csCenter);
			setCell(row, numCol++, DateUtils.getDateToString(
					lProvvedimento.getDocumentoAllegato().getDataEmissione(), "dd/MM/yyyy"), csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrMotivo(), csCenter);
			setCell(row, numCol++, lProvvedimento.getEvento().getDescrEsito(), csCenter);
			setCell(row, numCol++, stato, csCenter);
		}
	}

} // Chiude classe StatisController