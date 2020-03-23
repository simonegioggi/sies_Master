package siap.siep.statis.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;

import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.statis.dao.DettaglioArchiviazioniCPPSqlDAO;
import siap.siep.statis.dao.DettaglioIscrizioniAttivitaCPPSqlDAO;
import siap.siep.statis.dao.DettaglioTempiIscrizioneProcedimentiCPPSqlDAO;
import siap.siep.statis.dao.IspProvvedimentiSqlDAO;
import siap.siep.statis.dao.IspTempiSqlDAO;
import siap.siep.statis.dao.RiepilogoIscrizioniAttivitaCPPSqlDAO;
import siap.siep.statis.dao.RiepilogoPendentiDefinitiCPPSqlDAO;
import siap.siep.statis.dao.StatisStoreProcedureDAO;
import siap.siep.statis.model.DettaglioArchiviazioniCPPModel;
import siap.siep.statis.model.DettaglioIscrizioniAttivitaCPPModel;
import siap.siep.statis.model.DettaglioTempiIscrizioneProcedimentiCPPModel;
import siap.siep.statis.model.IspProvvedimentiModel;
import siap.siep.statis.model.IspTempiModel;
import siap.siep.statis.model.RiepilogoIscrizioniAttivitaCPPModel;
import siap.siep.statis.model.RiepilogoPendentiDefinitiCPPModel;

/**
 *
 * <p>
 * Title: StatisControllerCPP
 * </p>
 * <p>
 * Description: Controller per le statistiche sulla Classe VII
 * </p>
 * <p>
 * Conversione Pene Pecuniarie
 * </p>
 * <p>
 * Company: Intersistemi Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatisControllerCPP extends GenericController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * per Statistica RIEPILOGO ISCRIZIONI E ATTIVITA' CPP Cerca i dati aggregati per anno già selezionati
	 * dalla Strore-Procedure, per visualizzazione TOTALI ISCRIZIONI CPP sul foglio xls Riepilogo
	 */
	public Vector<RiepilogoIscrizioniAttivitaCPPModel> ExRicercaRiepilogoGeneraleIscrizioniAttivita(
			int aAnnoIni, int aAnnoFin, String aggmmIni, String aggmmFin) throws F3BException {

		Connection lConn = null;
		Vector<RiepilogoIscrizioniAttivitaCPPModel> lVect = new Vector<>();
		RiepilogoIscrizioniAttivitaCPPSqlDAO lRiepDao = null;

		try {
			lConn = getDBConnection();

			for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
				try {
					lRiepDao = new RiepilogoIscrizioniAttivitaCPPSqlDAO(lConn);
					lRiepDao.ricercaRiepilogoGeneraleIscrizioniAttivita(aAnno, aAnnoIni, aAnnoFin, aggmmIni,
							aggmmFin);

					lRiepDao.start();

					while (lRiepDao.next()) {
						lVect.add((RiepilogoIscrizioniAttivitaCPPModel) lRiepDao.getModelIscrizioni());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisControllerCPP.ExRicercaRiepilogoGeneraleIscrizioniAttivita: Non posso leggere : "
									+ daoEx);
				} finally {
					cleanup(lRiepDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	} // Chiude ExRicercaRiepilogoGeneraleIscrizioniAttivita

	/**
	 * Cerca i dati sui fascicoli di classe VII aggregati per anno (già selezionati dalla Strore-Procedure),
	 * per visualizzazione DETTAGLIO ATTIVITA CPP sul foglio xls di Riepilogo
	 */

	public Vector<DettaglioIscrizioniAttivitaCPPModel> ExRicercaDettagliAttivitaCPP(int aAnnoIni,
			int aAnnoFin, String aggmmIni, String aggmmFin) throws F3BException {

		Connection lConn = null;
		Vector<DettaglioIscrizioniAttivitaCPPModel> lVect = new Vector<>();
		DettaglioIscrizioniAttivitaCPPSqlDAO lDettDao = null;

		try {
			lConn = getDBConnection();
			lDettDao = new DettaglioIscrizioniAttivitaCPPSqlDAO(lConn);
			// Ciclo su TipoDettalio (0 = Iscritti, 1 = Errori, 2 = In atteesa Esecuzione etc...)
			for (int aTipo = 0; aTipo < 6; aTipo++) {
				// Ciclo su Anni (da Anno Inizio a Anno Fine)
				for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
					try {
						lDettDao.ricercaDettagliIscrizioniCPP(aAnno, aTipo, aAnnoIni, aAnnoFin, aggmmIni,
								aggmmFin);
						lDettDao.start();

						while (lDettDao.next()) {
							lVect.add((DettaglioIscrizioniAttivitaCPPModel) lDettDao.getModelDettaglioCPP());
						}
					} catch (DAOException daoEx) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.error("DAOException: " + daoEx);
						throw new F3BException(
								"StatisControllerCPP.ExRicercaDettagliAttivitaCPP: Non posso leggere : "
										+ daoEx);
					}
				}
			}
		} finally {
			cleanup(lConn);
			cleanup(lDettDao);
		}

		return lVect;
	} // Chiude ExRicercaDettagliAttivitaCPP

	public Vector<RiepilogoIscrizioniAttivitaCPPModel> ExRicercaRiepilogoGeneraleIscrizioniAttivitaPerMese(
			int aAnno, String aggmmIni, String aggmmFin, String aTipoMese, String aQuale_Trim_Sem)
			throws F3BException {

		Connection lConn = null;
		Vector<RiepilogoIscrizioniAttivitaCPPModel> lVect = new Vector<>();
		RiepilogoIscrizioniAttivitaCPPSqlDAO lRiepDao = null;

		try {
			lConn = getDBConnection();

			// Esplodiamo la statistica solo per un Semestre o un Trimestre, senza dividerlo ulteriormente per
			// mese:
			try {
				lRiepDao = new RiepilogoIscrizioniAttivitaCPPSqlDAO(lConn);
				lRiepDao.ricercaRiepilogoGeneraleIscrizioniAttivitaPerMese(aAnno, aggmmIni, aggmmFin,
						aTipoMese, aQuale_Trim_Sem);

				lRiepDao.start();

				while (lRiepDao.next()) {
					lVect.add((RiepilogoIscrizioniAttivitaCPPModel) lRiepDao.getModelIscrizioniPerMese());
				}
			} catch (DAOException daoEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("DAOException: " + daoEx);
				throw new F3BException(
						"StatisControllerCPP.ExRicercaRiepilogoGeneraleIscrizioniAttivitaPerMese: Non posso leggere : "
								+ daoEx);
			} finally {
				cleanup(lRiepDao);
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	} // Chiude ExRicercaRiepilogoGeneraleIscrizioniAttivitaPerMese

	public Vector<DettaglioIscrizioniAttivitaCPPModel> ExRicercaDettagliAttivitaCPPPerMese(int aAnno,
			String aggmmIni, String aggmmFin, String aTipoMese, String aQuale_Trim_Sem) throws F3BException {

		Connection lConn = null;
		Vector<DettaglioIscrizioniAttivitaCPPModel> lVect = new Vector<>();
		DettaglioIscrizioniAttivitaCPPSqlDAO lDettDao = null;

		// Esplodiamo la statistica solo per un Semestre o un Trimestre, senza dividerlo ulteriormente per
		// mese:
		try {
			lConn = getDBConnection();
			lDettDao = new DettaglioIscrizioniAttivitaCPPSqlDAO(lConn);
			// Ciclo su TipoDettalio (0 = Iscritti, 1 = Errori, 2 = In atteesa Esecuzione etc...)
			for (int aTipo = 0; aTipo < 6; aTipo++) {
				try {
					lDettDao.ricercaDettagliIscrizioniCPPPerMese(aAnno, aTipo, aggmmIni, aggmmFin, aTipoMese,
							aQuale_Trim_Sem);
					lDettDao.start();

					while (lDettDao.next()) {
						lVect.add(
								(DettaglioIscrizioniAttivitaCPPModel) lDettDao.getModelDettaglioCPPPerMese());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisControllerCPP.ExRicercaDettagliAttivitaCPPPerMese: Non posso leggere : "
									+ daoEx);
				}
			}
		} finally {
			cleanup(lConn);
			cleanup(lDettDao);
		}

		return lVect;
	} // Chiude ExRicercaDettagliAttivitaCPPPerMese

	//
	// ======================================
	/**
	 * Per Statistica TEMPI ISCRIZIONE PROCEDIMENTI CPP ExRicercaRiepilogoTempiIscrizioniCPP: Cerca i dati sui
	 * fascicoli di classe VII aggregati per anno (già selezionati dalla Strore-Procedure), per
	 * visualizzazione TEMPI ISCRIZIONE PROCEDIMENTI CPP sul foglio xls di Riepilogo
	 */
	public Vector<IspTempiModel> ExRicercaRiepilogoTempiIscrizioniCPP(int aAnnoIni, int aAnnoFin,
			String aggmmIni, String aggmmFin) throws F3BException {

		Connection lConn = null;
		Vector<IspTempiModel> lVect = new Vector<>();
		IspTempiSqlDAO lTempSqlDao = null;

		try {
			lConn = getDBConnection();
			lTempSqlDao = new IspTempiSqlDAO(lConn);

			// Ciclo sul Tipo Distinta (stato del procedimento: es. tra Data Arrivo e data decisione, tra data
			// decisione e data Validazione etc..)
			for (int aTipo = 1; aTipo < 5; aTipo++) {
				// Ciclo su Anni (da Anno Inizio a Anno Fine)
				for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
					try {
						lTempSqlDao.RicercaRiepilogoTempiIscrizioniCPP(aAnno, aTipo, aggmmIni, aggmmFin,
								aAnnoIni, aAnnoFin);
						lTempSqlDao.start();

						while (lTempSqlDao.next()) {
							lVect.add((IspTempiModel) lTempSqlDao.getRiepilogoCPPModel());
						}
					} catch (DAOException daoEx) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.error("DAOException: " + daoEx);
						throw new F3BException(
								"StatisControllerCPP.ExRicercaRiepilogoTempiIscrizioniCPP: Non posso leggere : "
										+ daoEx);
					}
				}
			}
		} finally {
			cleanup(lConn);
			cleanup(lTempSqlDao);
		}

		return lVect;
	} // Chiude ExRicercaRiepilogoTempiIscrizioniCPP

	public Vector<IspTempiModel> ExRicercaRiepilogoTempiIscrizioniCPPPerMese(int aAnno, String aggmmIni,
			String aggmmFin, String aTipoMese, String aQuale_Trim_Sem) throws F3BException {

		Connection lConn = null;
		Vector<IspTempiModel> lVect = new Vector<>();
		IspTempiSqlDAO lTempSqlDao = null;

		try {
			lConn = getDBConnection();
			lTempSqlDao = new IspTempiSqlDAO(lConn);

			// Ciclo sul Tipo Distinta (stato del procedimento: es. tra Data Arrivo e data decisione, tra data
			// decisione e data Validazione etc..)
			for (int aTipo = 1; aTipo < 5; aTipo++) {
				try {
					lTempSqlDao.RicercaRiepilogoTempiIscrizioniCPPPerMese(aAnno, aTipo, aggmmIni, aggmmFin,
							aTipoMese, aQuale_Trim_Sem);
					lTempSqlDao.start();
					while (lTempSqlDao.next()) {
						lVect.add((IspTempiModel) lTempSqlDao.getRiepilogoPerMeseCPPModel());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisControllerCPP.ExRicercaRiepilogoTempiIscrizioniCPPPerMese: Non posso leggere : "
									+ daoEx);
				} finally {
					cleanup(lTempSqlDao);
				}
			} // Chiude ciclo FOR su Tipo_Distinta
		} finally {
			cleanup(lConn);
		}

		return lVect;
	} // Chiude ExRicercaRiepilogoTempiIscrizioniCPPPerMese

	public Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> ExRicercaDettaglioTempiIscrizioneCPP(
			int aAnnoIni, int aAnnoFin, String aggmmIni, String aggmmFin) throws F3BException {

		Connection lConn = null;
		Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> lVect = new Vector<>();
		DettaglioTempiIscrizioneProcedimentiCPPSqlDAO lTempSqlDao = null;

		try {
			lConn = getDBConnection();
			lTempSqlDao = new DettaglioTempiIscrizioneProcedimentiCPPSqlDAO(lConn);

			// Ciclo sul Tipo Distinta (stato del procedimento: es. tra Data Arrivo e data decisione, tra data
			// decisione e data Validazione etc..)
			for (int aTipo = 1; aTipo < 5; aTipo++) {
				// for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) // Ciclo su Anni (da Anno Inizio
				// a Anno Fine)
				try {
					int aAnno = 0;
					lTempSqlDao.RicercaDettaglioTempiIscrizioneCPP(aAnno, aTipo, aAnnoIni, aAnnoFin, aggmmIni,
							aggmmFin);
					lTempSqlDao.start();

					while (lTempSqlDao.next()) {
						lVect.add((DettaglioTempiIscrizioneProcedimentiCPPModel) lTempSqlDao
								.getModelDettaglioTempiIscrCPP());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisControllerCPP.ExRicercaDettaglioTempiIscrizioneCPP: Non posso leggere : "
									+ daoEx);
				}
			}
		} finally {
			cleanup(lConn);
			cleanup(lTempSqlDao);
		}

		return lVect;
	} // Chiude ExRicercaDettaglioTempiIscrizioneCPP

	public Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> ExRicercaDettaglioTempiIscrizioneCPPPerMese(
			int aAnno, String aggmmIni, String aggmmFin, String aTipoMese, String aQuale_Trim_Sem)
			throws F3BException {

		Connection lConn = null;
		Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> lVect = new Vector<>();
		DettaglioTempiIscrizioneProcedimentiCPPSqlDAO lTempSqlDao = null;

		try {
			lConn = getDBConnection();
			lTempSqlDao = new DettaglioTempiIscrizioneProcedimentiCPPSqlDAO(lConn);

			// Ciclo sul Tipo Distinta (stato del procedimento: es. tra Data Arrivo e data decisione, tra data
			// decisione e data Validazione etc..)
			for (int aTipo = 1; aTipo < 5; aTipo++) {
				try {
					lTempSqlDao.RicercaDettaglioTempiIscrizioneCPP_PerMese(aAnno, aTipo, aggmmIni, aggmmFin,
							aTipoMese, aQuale_Trim_Sem);
					lTempSqlDao.start();

					while (lTempSqlDao.next()) {
						lVect.add((DettaglioTempiIscrizioneProcedimentiCPPModel) lTempSqlDao
								.getModelDettaglioTempiIscrCPP_PerMese());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisControllerCPP.ExRicercaDettaglioTempiIscrizioneCPPPerMese: Non posso leggere : "
									+ daoEx);
				}
			}
		} finally {
			cleanup(lConn);
			cleanup(lTempSqlDao);
		}

		return lVect;
	} // Chiude ExRicercaDettaglioTempiIscrizioneCPPPerMese()

	// ======================================
	/**
	 * per Statistica RIEPILO PROCEDIMENTO PENDENTI CPP ExRicercaRiepilogoDefinitiCPP: Cerca i dati sui
	 * fascicoli Definiti di classe VII aggregati per anno (già selezionati dalla Strore-Procedure), per
	 * visualizzazione sul foglio xls di Riepilogo Definiti
	 */
	public Vector<RiepilogoPendentiDefinitiCPPModel> ExRicercaRiepilogoDefinitiCPP(int aAnnoIni, int aAnnoFin,
			String aggmmIni, String aggmmFin) throws F3BException {

		Connection lConn = null;
		Vector<RiepilogoPendentiDefinitiCPPModel> lVect = new Vector<>();
		RiepilogoPendentiDefinitiCPPSqlDAO lRiepDao = null;

		try {
			lConn = getDBConnection();

			for (int aAnno = aAnnoIni; aAnno < aAnnoFin + 1; aAnno++) {
				try {
					lRiepDao = new RiepilogoPendentiDefinitiCPPSqlDAO(lConn);
					lRiepDao.ricercaRiepilogoDefinitiCPP(aAnno, aAnnoIni, aAnnoFin, aggmmIni, aggmmFin);

					lRiepDao.start();

					while (lRiepDao.next()) {
						lVect.add((RiepilogoPendentiDefinitiCPPModel) lRiepDao.getModelPendDefCPP());
					}
				} catch (DAOException daoEx) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.error("DAOException: " + daoEx);
					throw new F3BException(
							"StatisControllerCPP.ExRicercaRiepilogoDefinitiCPP: Non posso leggere : "
									+ daoEx);
				} finally {
					cleanup(lRiepDao);
				}
			}
		} finally {
			cleanup(lConn);
		}

		return lVect;
	} // Chiude ExRicercaRiepilogoDefinitiCPP()

	public Vector ExRicercaDettaglioProcedimenti_CPP(String[] aCodici) throws F3BException {

		Connection lConn = null;
		Vector lIspProvvedimenti = new Vector();
		IspProvvedimentiSqlDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspProvvedimentiSqlDAO(lConn);

			lIspDao.RicercaDettaglioProcedimenti_CPP(aCodici);

			lIspDao.start();
			while (lIspDao.next()) {
				lIspProvvedimenti.add(lIspDao.getModel());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisControllerCPP.ExRicercaDettaglioProcedimenti_CPP: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}

		return lIspProvvedimenti;
	}

	public Vector<String> getTitoliPerCodici_CPP(String[] aCodiciSelezionati, String aTipoTitolo)
			throws F3BException {

		Vector<String> lElencoTitoli = new Vector<>();

		Connection lConn = null;
		IspProvvedimentiSqlDAO lIspSqlDao = null;

		try {
			lConn = getDBConnection();

			lIspSqlDao = new IspProvvedimentiSqlDAO(lConn);

			lIspSqlDao.getTitoliPerCodiciPerTipoTitolo_CPP(aCodiciSelezionati, aTipoTitolo);

			lIspSqlDao.start();

			while (lIspSqlDao.next()) {
				lElencoTitoli.add(lIspSqlDao.getString(aTipoTitolo));
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StatisControllerCPP.getTitoliPerCodici: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lIspSqlDao);
			cleanup(lConn);
		}

		return lElencoTitoli;
	}

	public Vector ExGetCountRiepilogoIspProvvedimenti_CPP(IspProvvedimentiModel aIspProvvedimenti)
			throws F3BException {

		Connection lConn = null;
		Vector lIspProvvedimenti = new Vector();
		IspProvvedimentiSqlDAO lIspDao = null;

		try {

			lConn = getDBConnection();
			lIspDao = new IspProvvedimentiSqlDAO(lConn);

			lIspDao.getCountRiepilogoIspProvvedimenti_CPP(aIspProvvedimenti);

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
					"StatisControllerCPP.ExGetCountRiepilogoIspProvvedimenti_CPP: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lIspDao);
			cleanup(lConn);
		}

		return lIspProvvedimenti;
	}

	// 07-06-2016 - Riciclo dopo primo collaudo V.10
	// public Vector ExRicercaDettaglioArchiviazioni_CPP(String[] aCodici) throws F3BException
	public Vector ExRicercaDettaglioArchiviazioni_CPP(String[] aCodici, String dataIni, String dataFin)
			throws F3BException { // 07-06-2016 - END Riciclo

		Connection lConn = null;
		Vector lDetArchiviazioni = new Vector();
		DettaglioArchiviazioniCPPSqlDAO lArcDao = null;

		try {
			lConn = getDBConnection();
			lArcDao = new DettaglioArchiviazioniCPPSqlDAO(lConn);

			// 07-06-2016 - Riciclo dopo primo collaudo V.10
			// lArcDao.RicercaDettaglioArchiviazioni_CPP(aCodici);
			lArcDao.RicercaDettaglioArchiviazioni_CPP(aCodici, dataIni, dataFin);
			// 07-06-2016 - Riciclo dopo primo collaudo V.10

			lArcDao.start();
			while (lArcDao.next()) {
				lDetArchiviazioni.add(lArcDao.getModel());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"StatisControllerCPP.ExRicercaDettaglioArchiviazioni_CPP: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lArcDao);
			cleanup(lConn);
		}

		return lDetArchiviazioni;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// ------------------------------------------------------------------------------------------------------------------
	//
	// METODI PER LA SCRITTURA E PRODUZIONE DEGLI OUTPUT SU FOGLI .EXCEL
	//
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Crea il foglio excel per il Riepilogo dei Totali delle Attività/Iscrizioni dei Procedimenti Classe VII
	 * (Conversione Pene Pecuniarie)
	 *
	 * @param aVect
	 *            Vettore con i dati dei Totali raggruppati per Anno
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo
	 * @param dataFin
	 *            Fine intervallo
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateRiepilogoIscrizioni_Attivita(Vector<RiepilogoIscrizioniAttivitaCPPModel> aVect,
			HSSFWorkbook wb, UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// primo foglio; RIEPILOGO_ISCRIZIONI
		HSSFSheet sheet = wb.createSheet("Riepilogo Iscrizioni");
		sheet.setColumnWidth(0, (40 * 256));

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Iscrizioni relativo al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// Tipologia tempi
		String tipologia = "";
		tipologia = "RIEPILOGO ISCRIZIONE CONVERSIONI PENE PECUNIARIE";

		// creazione riga con altezza per wraptext
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		csBold.setWrapText(true);
		setCell(row, 0, tipologia, csBold);

		// Vector lVect = (Vector) aVect.get(tipo - 1);
		Iterator itx = aVect.iterator();

		int nRowAnno = nRow;
		int annoOld = 0;
		short nColAnno = 0;
		String formula = "";

		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			RiepilogoIscrizioniAttivitaCPPModel lMod = (RiepilogoIscrizioniAttivitaCPPModel) itx.next();

			// test per cambio anno
			if (annoOld != lMod.getAnno().intValue()) {
				nColAnno++;
				nRow = nRowAnno;

				// scrittura anno

				row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);

				setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			}

			annoOld = lMod.getAnno().intValue();
			nRow++;

			// scrittura tipologia e valore
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Iscritti", cs);
			setCell(row, nColAnno, lMod.getIscritti().doubleValue(), cs);

			nRow++;

			// scrittura tipologia e valore
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Iscritti per Errore", cs);
			setCell(row, nColAnno, lMod.getIscrittiErrore().doubleValue(), cs);

			nRow++;

			// scrittura totale anno
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

		// Secondo foglio; RIEPILOGO_ATTIVITA
		sheet = wb.createSheet("Riepilogo Attività");
		sheet.setColumnWidth(0, (60 * 256));

		nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Attività relativo al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		tipologia = "";
		tipologia = "RIEPILOGO ATTIVITA' CONVERSIONE PENE PECUNIARIE";

		// creazione riga con altezza per wraptext
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		csBold.setWrapText(true);
		setCell(row, 0, tipologia, csBold);

		// Vector lVect = (Vector) aVect.get(tipo - 1);
		Iterator itx1 = aVect.iterator();

		nRowAnno = nRow;
		annoOld = 0;
		nColAnno = 0;
		formula = "";

		// inizio ciclo di scrittura dei dati Attività
		while (itx1.hasNext()) {
			RiepilogoIscrizioniAttivitaCPPModel lMod = (RiepilogoIscrizioniAttivitaCPPModel) itx1.next();

			// test per cambio anno
			if (annoOld != lMod.getAnno().intValue()) {
				nColAnno++;
				nRow = nRowAnno;

				row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);

				setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			}

			annoOld = lMod.getAnno().intValue();
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Emessa Ordinanza di Conversione in attesa esecuzione", cs);
			setCell(row, nColAnno, lMod.getInAttesaEsecuzione().doubleValue(), cs);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Provvedimenti inoltrati all’Ufficio di Sorveglianza in attesa di risposta", cs);
			setCell(row, nColAnno, lMod.getInoltroUDSinAttesadiRisposta().doubleValue(), cs);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Attesa inoltro all'Ufficio di Sorveglianza", cs);
			setCell(row, nColAnno, lMod.getAttesaInoltroUDS().doubleValue(), cs);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Procedimenti privi di Attività ", cs);
			setCell(row, nColAnno, lMod.getSenzaClasseI().doubleValue(), cs);

			nRow++;

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
	} // CHIUDE ExCreateRiepilogoIscrizioni_Attivita()

	/**
	 * Crea il foglio excel per l'elenco Dettagliato delle varie Tipologie di Iscrizioni/Attività dei
	 * Procedimenti di Classe VII (Conversione Pene Pecuniarie)
	 *
	 * @param aVect
	 *            Vettore con i Dettagli per le tre tipologie di Procedimenti Classe VII
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
	public void ExCreateElencoDettaglioIscrizioni_Attivita(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect,
			HSSFWorkbook wb, UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFSheet sheet;

		// primo foglio; DETTAGLIO_ISCRIZIONI
		sheet = wb.createSheet("Elenco Iscritti");
		sheet = settaLarghezzaColumnElenco(sheet);

		// int nRow = 0;
		/* nRow = */CreaElencoIscrizioni(aVect, sheet, csNull, cs, csBold, csBoldCenter, font, uffUteConnesso,
				DescIntesta, dataIni, dataFin);

		// Secondo foglio; DETTAGLIO_ISCRITTI_PER_ERRORE
		sheet = wb.createSheet("Elenco Iscritti per Errore");
		sheet = settaLarghezzaColumnElenco(sheet);

		// nRow = 0;
		/* nRow = */CreaElencoIscrizioniErrore(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Terzo foglio; DETTAGLIO Fascicoli con Provvedimento di Conversione in ATTESA_ESECUZIONE
		sheet = wb.createSheet("Elenco In Attesa di Esecuzione");
		sheet = settaLarghezzaColumnElenco(sheet);

		// nRow = 0;
		/* nRow = */CreaElencoAttesaEsecuzione(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Quarto foglio; DETTAGLIO Fascicoli Inoltrati a UDS in ATTESA_DI_RISPOSTA
		sheet = wb.createSheet("Elenco In Attesa di Risposta");
		sheet = settaLarghezzaColumnElenco(sheet);

		// nRow = 0;
		/* nRow = */CreaElencoAttesaRisposta(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Quinto foglio; DETTAGLIO Fascicoli in ATTESA_DI_INOLTRO a UDS
		sheet = wb.createSheet("Elenco In Attesa Inoltro a UDS");
		sheet = settaLarghezzaColumnElenco(sheet);

		// nRow = 0;
		/* nRow = */CreaElencoAttesaInoltro(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Sesto foglio; DETTAGLIO Fascicoli PRIVI di ATTIVITA' e Iscritti senza Classe I
		sheet = wb.createSheet("Elenco Privi di Attività");
		sheet = settaLarghezzaColumnElenco(sheet);

		// nRow = 0;
		/* nRow = */CreaElencoSenzaAttivita(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);
	} // CHIUDE ExCreateElencoDettaglioIscrizioni_Attivita()

	private int CreaElencoIscrizioni(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFSheet sheet,
			HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta, String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Fascicoli Iscritti relativo al periodo dal " + dataIni + " al " + dataFin,
				csNull);

		int annoOld = 0;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Iscritti") == 0) {
				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {
					nRow++;
					nRow++;
					nRow++;

					// scrittura anno
					row = sheet.createRow(nRow);
					setCell(row, 0, "ANNO " + lMod.getAnno(), csNull);

					nRow++;
					nRow++;
					row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaElencoIscrizioni()

	private int CreaElencoIscrizioniErrore(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFSheet sheet,
			HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta, String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli Iscritti per Errore relativo al periodo dal " + dataIni + " al " + dataFin,
				csNull);

		int annoOld = 0;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Errori") == 0) {
				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {
					nRow++;
					nRow++;
					nRow++;

					// scrittura anno
					row = sheet.createRow(nRow);
					setCell(row, 0, "ANNO " + lMod.getAnno(), csNull);

					nRow++;
					nRow++;
					row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaElencoIscrizioniErrore()

	private int CreaElencoAttesaEsecuzione(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFSheet sheet,
			HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta, String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli con Emessa Ordinaza di Conversione in Attesa di Esecuzione, relativo al periodo dal "
						+ dataIni + " al " + dataFin,
				csNull);

		int annoOld = 0;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Attesa_Esecuzione") == 0) {
				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {
					nRow++;
					nRow++;
					nRow++;

					// scrittura anno
					row = sheet.createRow(nRow);
					setCell(row, 0, "ANNO " + lMod.getAnno(), csNull);

					nRow++;
					nRow++;
					row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaElencoAttesaEsecuzione()

	private int CreaElencoAttesaRisposta(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFSheet sheet,
			HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta, String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli inoltrati all'Ufficio di Sorveglianza in Attesa di Risposta, relativo al periodo dal "
						+ dataIni + " al " + dataFin,
				csNull);

		int annoOld = 0;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Attesa_Risposta") == 0) {
				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {
					nRow++;
					nRow++;
					nRow++;

					// scrittura anno
					row = sheet.createRow(nRow);
					setCell(row, 0, "ANNO " + lMod.getAnno(), csNull);

					nRow++;
					nRow++;
					row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaElencoAttesaRisposta()

	private int CreaElencoAttesaInoltro(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFSheet sheet,
			HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta, String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli in attesa di inoltro all'Ufficio di Sorveglianza, relativo al periodo dal "
						+ dataIni + " al " + dataFin,
				csNull);

		int annoOld = 0;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Attesa_Inoltro") == 0) {
				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {
					nRow++;
					nRow++;
					nRow++;

					// scrittura anno
					row = sheet.createRow(nRow);
					setCell(row, 0, "ANNO " + lMod.getAnno(), csNull);

					nRow++;
					nRow++;
					row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaElencoAttesaInoltro()

	private int CreaElencoSenzaAttivita(Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFSheet sheet,
			HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta, String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Fascicoli senza Attività in essere, relativo al periodo dal " + dataIni
				+ " al " + dataFin, csNull);

		int annoOld = 0;

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Senza_Attivita") == 0) {
				// test per cambio anno
				if (annoOld != lMod.getAnno().intValue()) {
					nRow++;
					nRow++;
					nRow++;

					// scrittura anno
					row = sheet.createRow(nRow);
					setCell(row, 0, "ANNO " + lMod.getAnno(), csNull);

					nRow++;
					nRow++;
					row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaElencoSenzaAttivita()

	private HSSFSheet settaLarghezzaColumnElenco(HSSFSheet sheet) {

		sheet.setColumnWidth(0, (16 * 256)); // Anno/Numero Proc
		sheet.setColumnWidth(1, (23 * 256)); // Data comunicazione impossibilita esazione
		sheet.setColumnWidth(2, (27 * 256)); // Data arrivo in cancelleria
		sheet.setColumnWidth(3, (18 * 256)); // Data Iscrizione Fascicolo
		sheet.setColumnWidth(4, (27 * 256)); // Data Trasmissione a UDS
		sheet.setColumnWidth(5, (25 * 256)); // Data altro provvedimento Pubblico Ministero
		sheet.setColumnWidth(6, (20 * 256)); // Data Decisione UDS
		sheet.setColumnWidth(7, (40 * 256)); // Cognome / Nome Soggetto

		return sheet;
	}

	private HSSFRow valorizzaTestataElenco(HSSFSheet sheet, int riga, HSSFCellStyle csBoldCenter) {

		HSSFRow row = sheet.getRow(riga);
		if (row == null)
			row = sheet.createRow(riga);

		setCell(row, 0, "N. FASCICOLO", csBoldCenter);
		setCell(row, 1, "DATA IMPOS. ESAZIONE", csBoldCenter);
		setCell(row, 2, "DATA ARRIVO CANCELLERIA", csBoldCenter);
		setCell(row, 3, "DATA ISCRIZIONE", csBoldCenter);
		setCell(row, 4, "DATA TRASMISSIONE A UDS", csBoldCenter);
		setCell(row, 5, "DATA ALTRO PROVV. PM", csBoldCenter);
		setCell(row, 6, "DATA DECISIONE UDS", csBoldCenter);
		setCell(row, 7, "COGNOME / NOME ", csBoldCenter);

		return row;
	}

	private HSSFRow valorizzaRigaElenco(HSSFSheet sheet, int riga, HSSFCellStyle cs,
			DettaglioIscrizioniAttivitaCPPModel lMod) {

		HSSFRow row = sheet.getRow(riga);

		if (row == null)
			row = sheet.createRow(riga);

		setCell(row, 0, " " + lMod.getChiaveAnno().toString() + "/" + lMod.getChiaveProgr().toString(), cs);
		setCell(row, 1,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataImpEsazione(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 2,
				" " + StringUtils.toStringJSP(
						DateUtils.getDateToString(lMod.getDataArrivoAttoinCancelleria(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 3,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataIscrizione(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 4,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataTrasmissione(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 5,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataUltEventoPM(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 6,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataEventoSorv(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 7, " " + lMod.getCognome() + "  " + lMod.getNome(), cs);

		return row;
	}
	// ///////////////

	/**
	 * Crea il foglio excel per il Riepilogo dei Totali delle Attività/Iscrizioni dei Procedimenti Classe VII
	 * (Conversione Pene Pecuniarie)
	 *
	 * @param aVect
	 *            Vettore con i dati dei Totali raggruppati per Semestre o Trimestre
	 * @param wb
	 *            Workbook
	 * @param uffUteConnesso
	 *            Model dell'ufficio dell'utente connesso
	 * @param dataIni
	 *            Inizio intervallo
	 * @param dataFin
	 *            Fine intervallo
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExCreateRiepilogoIscrizioni_Attivita_PerMese(
			Vector<RiepilogoIscrizioniAttivitaCPPModel> aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, String DescIntesta, String aTipoMese, String aQuale_Trim_Sem)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csNullBold = wb.createCellStyle();
		csNullBold.setFont(font);

		// primo foglio; RIEPILOGO_ISCRIZIONI
		HSSFSheet sheet = wb.createSheet("Riepilogo Iscrizioni");
		sheet.setColumnWidth(0, (40 * 256));
		sheet.setColumnWidth(1, (30 * 256));

		int nRow = 0;
		String lAnnoStat = dataIni.substring(6);

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Iscrizioni relativo al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNullBold);

		nRow++;
		nRow++;

		// creazione riga con altezza per wraptext
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		csBold.setWrapText(true);
		setCell(row, 0, "RIEPILOGO ISCRIZIONE CONVERSIONI PENE PECUNIARIE", csBold);
		csBoldCenter.setWrapText(true);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csBoldCenter);

		String formula = "";
		int nRow_Int = nRow;

		Iterator itx = aVect.iterator(); // In Realtà c'è solo 1 Elemento nel vettore (il Trimestre o
											// semestre)
		while (itx.hasNext()) {
			RiepilogoIscrizioniAttivitaCPPModel lMod = (RiepilogoIscrizioniAttivitaCPPModel) itx.next();

			nRow++;

			// scrittura tipologia e valore
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, "Iscritti", cs);
			setCell(row, 1, lMod.getIscritti().doubleValue(), csCenter);

			nRow++;

			// scrittura tipologia e valore
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, "Iscritti per Errore", cs);
			setCell(row, 1, lMod.getIscrittiErrore().doubleValue(), csCenter);

			nRow++;

			// scrittura totale anno
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, "TOTALE", csBoldCenter);
			formula = this.getStringaSomma(nRow_Int + 1, 1, nRow - 1, 1);

			setFormulaCell(row, 1, formula, csBoldCenter);
		}

		nRow++;

		// Secondo foglio; RIEPILOGO_ATTIVITA
		sheet = wb.createSheet("Riepilogo Attività");
		sheet.setColumnWidth(0, (60 * 256));
		sheet.setColumnWidth(1, (30 * 256));

		nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Attività relativo al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNullBold);

		nRow++;
		nRow++;

		// creazione riga con altezza per wraptext
		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		csBold.setWrapText(true);
		setCell(row, 0, "RIEPILOGO ATTIVITA' CONVERSIONE PENE PECUNIARIE", csBold);
		csBoldCenter.setWrapText(true);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csBoldCenter);

		formula = "";
		nRow_Int = nRow;

		Iterator itx1 = aVect.iterator(); // In Realtà c'è solo 1 Elemento nel vettore (il Trimestre o
											// semestre)
		while (itx1.hasNext()) {
			RiepilogoIscrizioniAttivitaCPPModel lMod = (RiepilogoIscrizioniAttivitaCPPModel) itx1.next();

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, "Emessa Ordinanza di Conversione in attesa esecuzione", cs);
			setCell(row, 1, lMod.getInAttesaEsecuzione().doubleValue(), csCenter);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Provvedimenti inoltrati all’Ufficio di Sorveglianza in attesa di risposta", cs);
			setCell(row, 1, lMod.getInoltroUDSinAttesadiRisposta().doubleValue(), csCenter);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Attesa inoltro all'Ufficio di Sorveglianza", cs);
			setCell(row, 1, lMod.getAttesaInoltroUDS().doubleValue(), csCenter);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Procedimenti privi di Attività ", cs);
			setCell(row, 1, lMod.getSenzaClasseI().doubleValue(), csCenter);

			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, "TOTALE", csBoldCenter);

			formula = this.getStringaSomma(nRow_Int + 1, 1, nRow - 1, 1);
			setFormulaCell(row, 1, formula, csBoldCenter);
		}

		nRow++;
	} // CHIUDE ExCreateRiepilogoIscrizioni_Attivita_PerMese()

	public void ExCreateElencoDettaglioIscrizioni_Attivita_PerMese(
			Vector<DettaglioIscrizioniAttivitaCPPModel> aVect, HSSFWorkbook wb, UfficioModel uffUteConnesso,
			String dataIni, String dataFin, String DescIntesta, String aTipoMese, String aQuale_Trim_Sem)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFSheet sheet;

		String lAnnoStat = dataIni.substring(6);

		// primo foglio; DETTAGLIO_ISCRIZIONI
		sheet = wb.createSheet("Elenco Iscritti");
		sheet = settaLarghezzaColumnElenco(sheet);
		//
		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Fascicoli Iscritti relativo al periodo dal " + dataIni + " al " + dataFin,
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csNull);

		nRow++;
		nRow++;
		row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx.next();

			if (lMod.getTipodettaglio().compareTo("Iscritti") == 0) {
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}

		}

		nRow++;

		// Secondo foglio; DETTAGLIO_ISCRITTI_PER_ERRORE
		sheet = wb.createSheet("Elenco Iscritti per Errore");
		sheet = settaLarghezzaColumnElenco(sheet);

		nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli Iscritti per Errore, relativo al periodo dal " + dataIni + " al " + dataFin,
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csNull);

		nRow++;
		nRow++;
		row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);

		Iterator itx1 = aVect.iterator();
		while (itx1.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx1.next();

			if (lMod.getTipodettaglio().compareTo("Errori") == 0) {
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}

		}

		nRow++;

		// Terzo foglio; DETTAGLIO Fascicoli con Provvedimento di Conversione in ATTESA_ESECUZIONE
		sheet = wb.createSheet("Elenco In Attesa di Esecuzione");
		sheet = settaLarghezzaColumnElenco(sheet);

		nRow = 0;
		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli con Emessa Ordinaza di Conversione in Attesa di Esecuzione, relativo al periodo dal "
						+ dataIni + " al " + dataFin,
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csNull);

		nRow++;
		nRow++;
		row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);

		Iterator itx2 = aVect.iterator();
		while (itx2.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx2.next();

			if (lMod.getTipodettaglio().compareTo("Attesa_Esecuzione") == 0) {
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}

		}

		nRow++;

		// Quarto foglio; DETTAGLIO Fascicoli Inoltrati a UDS in ATTESA_DI_RISPOSTA
		sheet = wb.createSheet("Elenco In Attesa di Risposta");
		sheet = settaLarghezzaColumnElenco(sheet);

		nRow = 0;
		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli inoltrati all'Ufficio di Sorveglianza in Attesa di Risposta, relativo al periodo dal "
						+ dataIni + " al " + dataFin,
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csNull);

		nRow++;
		nRow++;
		row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);

		Iterator itx3 = aVect.iterator();
		while (itx3.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx3.next();

			if (lMod.getTipodettaglio().compareTo("Attesa_Risposta") == 0) {
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}

		}

		nRow++;

		// Quinto foglio; DETTAGLIO Fascicoli in ATTESA_DI_INOLTRO a UDS
		sheet = wb.createSheet("Elenco In Attesa Inoltro a UDS");
		sheet = settaLarghezzaColumnElenco(sheet);

		nRow = 0;
		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Elenco Fascicoli in attesa di inoltro all'Ufficio di Sorveglianza, relativo al periodo dal "
						+ dataIni + " al " + dataFin,
				csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csNull);

		nRow++;
		nRow++;
		row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);

		Iterator itx4 = aVect.iterator();
		while (itx4.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx4.next();

			if (lMod.getTipodettaglio().compareTo("Attesa_Inoltro") == 0) {
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}

		}

		nRow++;

		// Sesto foglio; DETTAGLIO Fascicoli PRIVI di ATTIVITA' e Iscritti senza Classe I
		sheet = wb.createSheet("Elenco Privi di Attività");
		sheet = settaLarghezzaColumnElenco(sheet);

		nRow = 0;
		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "Elenco Fascicoli privi di Attività in essere, relativo al periodo dal " + dataIni
				+ " al " + dataFin, csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, aQuale_Trim_Sem + " " + aTipoMese.toUpperCase(), csNull);

		nRow++;
		nRow++;
		row = valorizzaTestataElenco(sheet, nRow, csBoldCenter);

		Iterator itx5 = aVect.iterator();
		while (itx5.hasNext()) {
			DettaglioIscrizioniAttivitaCPPModel lMod = (DettaglioIscrizioniAttivitaCPPModel) itx5.next();

			if (lMod.getTipodettaglio().compareTo("Senza_Attivita") == 0) {
				nRow++;
				row = valorizzaRigaElenco(sheet, nRow, cs, lMod);
			}
		}

		nRow++;
	} // CHIUDE ExCreateElencoDettaglioIscrizioni_AttivitaPerMese()

	/**
	 * Crea il foglio excel per il Riepilogo delle varie Tipologie di situazione del Fascicolo; Statistica per
	 * Procedimenti di Classe VII (Conversione Pene Pecuniarie)
	 */
	public void ECreateXLSRiepilogoTempiIscrizioniCPP(Vector<IspTempiModel> aVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// PRIMO FOGLIO: RIEPILOGO
		HSSFSheet sheet = wb.createSheet("Riepilogo ");
		sheet.setColumnWidth(0, (50 * 256));

		int nRow = 0;
		// int Riga = 0;
		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica relativa al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Tempi Iscrizioni Procedimenti di Classe VII", csNull);

		nRow++;
		nRow++;

		int nRowAnno = nRow;
		short nColAnno = 0;

		for (int TypeD = 1; TypeD < 5; TypeD++) {
			nRow = ScriviRighe_Riepilogo_Tempi(aVect, nRow, nRowAnno, nColAnno, sheet, cs, csBoldCenter,
					csBold, TypeD);
			nRow++;
			nRow++;
			nRow++;
			nRowAnno = nRow;
		}
	} // CHIUDE ECreateXLSRiepilogoTempiIscrizioniCPP()

	private int ScriviRighe_Riepilogo_Tempi(Vector<IspTempiModel> aVect, int Riga, int nRowAnno,
			short nColAnno, HSSFSheet sheet, HSSFCellStyle cs, HSSFCellStyle csBoldCenter,
			HSSFCellStyle csBold, int TipoD) {

		int nRow = Riga;

		String Tipologia = "";
		String TipoDett = "";
		if (TipoD == 1) {
			Tipologia = "TEMPI TRA COMUNICAZIONE IMPOSSIBILITA' ESAZIONE E ARRIVO IN CANCELLERIA";
			TipoDett = "DISTINTA_1";
		} else if (TipoD == 2) {
			Tipologia = "TEMPI TRA ARRIVO IN CANCELLERIA E ISCRIZIONE";
			TipoDett = "DISTINTA_2";
		} else if (TipoD == 3) {
			Tipologia = "TEMPI TRA ISCRIZIONE E TRASMISSIONE A UFFICIO SORVEGLIANZA";
			TipoDett = "DISTINTA_3";
		} else if (TipoD == 4) {
			Tipologia = "TEMPI TRA TRASMISSIONE A UFFICIO SORVEGLIANZA E DECISIONE UFFICIO SORVEGLIANZA";
			TipoDett = "DISTINTA_4";
		}

		int annoOld = 0;
		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			IspTempiModel lMod = (IspTempiModel) itx.next();
			// if(lMod.getTipo().compareTo(TipoDett) == 0)
			if (TipoDett.equals(lMod.getTipo())) {
				if (annoOld != lMod.getAnno().intValue()) {
					nColAnno++;
					nRow = nRowAnno;

					HSSFRow row = sheet.getRow(nRowAnno);
					if (row == null)
						row = sheet.createRow(nRowAnno);
					//
					csBold.setWrapText(true);
					setCell(row, 0, Tipologia, csBold);
					setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
				}

				annoOld = lMod.getAnno().intValue();
				nRow++;

				nRow = ScriviRigheTempi(lMod, nRow, nRowAnno, nColAnno, sheet, cs, csBoldCenter, csBold);

			}

		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti
		// le somme parziali e generali
		nColAnno++;
		nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);

		return nRow;
	}

	public void ECreateXLSRiepilogoTempiIscrizioniCPP_PerMese(Vector<IspTempiModel> aVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta,
			String TipoRicerca, String PeriodoRic) throws F3BException {

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

		HSSFCellStyle csNullBold = wb.createCellStyle();
		csNullBold.setFont(font);

		// PRIMO FOGLIO: RIEPILOGO
		HSSFSheet sheet = wb.createSheet("Riepilogo ");
		sheet.setColumnWidth(0, (50 * 256));
		sheet.setColumnWidth(1, (30 * 256));

		int nRow = 0;
		// int Riga = 0;
		String lAnnoStat = dataIni.substring(6);

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;
		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica relativa al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Tempi Iscrizioni Procedimenti di Classe VII", csNull);

		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNullBold);

		nRow++;
		nRow++;

		int nRowAnno = nRow;
		short nColAnno = 0;

		for (int TypeD = 1; TypeD < 5; TypeD++) {
			nRow = ScriviRighe_Riepilogo_Tempi_PerMese(aVect, nRow, nRowAnno, nColAnno, sheet, cs,
					csBoldCenter, csBold, TypeD, TipoRicerca, PeriodoRic);
			nRow++;
			nRow++;
			nRow++;
			nRowAnno = nRow;
		}
	} // CHIUDE ECreateXLSRiepilogoTempiIscrizioniCPP_PerMese()

	private int ScriviRighe_Riepilogo_Tempi_PerMese(Vector<IspTempiModel> aVect, int Riga, int nRowAnno,
			short nColAnno, HSSFSheet sheet, HSSFCellStyle cs, HSSFCellStyle csBoldCenter,
			HSSFCellStyle csBold, int TipoD, String TipoRicerca, String PeriodoRic) {

		int nRow = Riga;

		String Tipologia = "";
		String TipoDett = "";
		if (TipoD == 1) {
			Tipologia = "TEMPI TRA COMUNICAZIONE IMPOSSIBILITA' ESAZIONE E ARRIVO IN CANCELLERIA";
			TipoDett = "DISTINTA_1";
		} else if (TipoD == 2) {
			Tipologia = "TEMPI TRA ARRIVO IN CANCELLERIA E ISCRIZIONE";
			TipoDett = "DISTINTA_2";
		} else if (TipoD == 3) {
			Tipologia = "TEMPI TRA ISCRIZIONE E TRASMISSIONE A UFFICIO SORVEGLIANZA";
			TipoDett = "DISTINTA_3";
		} else if (TipoD == 4) {
			Tipologia = "TEMPI TRA TRASMISSIONE A UFFICIO SORVEGLIANZA E DECISIONE UFFICIO SORVEGLIANZA";
			TipoDett = "DISTINTA_4";
		}

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			IspTempiModel lMod = (IspTempiModel) itx.next();
			if (TipoDett.equals(lMod.getTipo())) {
				nColAnno++;
				nRow = nRowAnno;

				HSSFRow row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);
				//
				csBold.setWrapText(true);
				setCell(row, 0, Tipologia, csBold);
				// setCell(row, nColAnno, lMod.getPeriodo(), csBoldCenter);

				csBoldCenter.setWrapText(true);
				setCell(row, 1, PeriodoRic + " " + TipoRicerca.toUpperCase(), csBoldCenter);

				nRow++;

				nRow = ScriviRigheTempi(lMod, nRow, nRowAnno, nColAnno, sheet, cs, csBoldCenter, csBold);

			}

		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti
		// le somme parziali e generali
		nColAnno++;
		nRow = setTotaliTempi(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno);

		return nRow;
	}

	// -----------------------------------------
	private int ScriviRigheTempi(IspTempiModel lMod, int Riga, int nRowAnno, short nColAnno, HSSFSheet sheet,
			HSSFCellStyle cs, HSSFCellStyle csBoldCenter, HSSFCellStyle csBold) {

		int nRow = Riga;
		String formula = "";

		HSSFRow row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "ENTRO 5 GIORNI", cs);
		setCell(row, nColAnno, lMod.getEntro5().doubleValue(), cs);

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "ENTRO 20 GIORNI", cs);
		setCell(row, nColAnno, lMod.getEntro20().doubleValue(), cs);

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "ENTRO 30 GIORNI", cs);
		setCell(row, nColAnno, lMod.getEntro30().doubleValue(), cs);

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "ENTRO 60 GIORNI", cs);
		setCell(row, nColAnno, lMod.getEntro60().doubleValue(), cs);

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "ENTRO 90 GIORNI", cs);
		setCell(row, nColAnno, lMod.getEntro90().doubleValue(), cs);

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "OLTRE 90 GIORNI", cs);
		setCell(row, nColAnno, lMod.getOltre90().doubleValue(), cs);

		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);
		setCell(row, 0, "TOTALE", csBoldCenter);
		formula = this.getStringaSomma(nRowAnno + 1, nColAnno, nRow - 1, nColAnno);
		setFormulaCell(row, nColAnno, formula, csBold);

		return nRow;
	} // Chiude ScriviRigheTempi()

	/**
	 * Crea il foglio excel per l'elenco Dettagliato dei Procedimenti di Classe VII (Conversione Pene
	 * Pecuniarie) Ordinati per Tipo di Distinata e Numeroproc. N.B.Tipo Distinta = tipo di intervallo di
	 * tempo tra 2 differenti stati del procedimento (ES. tra data di Arrivo e data Iscrizione, tra Data
	 * iscrizione e Data Trasmissione, etc...)
	 *
	 * @param aVect
	 *            Vettore con i Dettagli per le tre tipologie di Procedimenti Classe VII
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
	public void ExCreateXLSDettaglioTempiIscrizioniCPP(
			Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFSheet sheet;

		// primo foglio; DETTAGLIO TEMPI TRA COMUNICAZIONE IMPOSSIBILITA' ESAZIONE E ARRIVO IN CANCELLERIA
		sheet = wb.createSheet("Elenco Tempi Imposs. Esazione");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// int nRow = 0;
		/* nRow = */CreaDettaglioTempi_D1(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Secondo foglio; DETTAGLIO TEMPI TRA ARRIVO IN CANCELLERIA E ISCRIZIONE
		sheet = wb.createSheet("Elenco per Tempi Iscrizione");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// nRow = 0;
		/* nRow = */CreaDettaglioTempi_D2(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Terzo foglio; DETTAGLIO TEMPI TRA ISCRIZIONE E TRASMISSIONE A UFFICIO SORVEGLIANZA
		sheet = wb.createSheet("Elenco per Tempi Trasmissione");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// nRow = 0;
		/* nRow = */CreaDettaglioTempi_D3(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);

		// Quarto foglio; DETTAGLIO TEMPI TRA TRASMISSIONE A UFFICIO SORVEGLIANZA E DECISIONE UFFICIO
		// SORVEGLIANZA
		sheet = wb.createSheet("Elenco per Tempi Decisione UDS");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// nRow = 0;
		/* nRow = */CreaDettaglioTempi_D4(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin);
	} // CHIUDE ExCreateXLSDettaglioTempiIscrizioniCPP()

	public void ExCreateXLSDettaglioTempiIscrizioniCPP_PerMese(
			Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta,
			String TipoRicerca, String PeriodoRic) throws F3BException {

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

		HSSFSheet sheet;

		// primo foglio; DETTAGLIO TEMPI TRA COMUNICAZIONE IMPOSSIBILITA' ESAZIONE E ARRIVO IN CANCELLERIA
		sheet = wb.createSheet("Elenco Tempi Imposs. Esazione");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// int nRow = 0;
		/* nRow = */CreaDettaglioTempi_D1_Mese(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin, TipoRicerca, PeriodoRic);

		// Secondo foglio; DETTAGLIO TEMPI TRA ARRIVO IN CANCELLERIA E ISCRIZIONE
		sheet = wb.createSheet("Elenco per Tempi Iscrizione");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// nRow = 0;
		/* nRow = */CreaDettaglioTempi_D2_Mese(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin, TipoRicerca, PeriodoRic);

		// Terzo foglio; DETTAGLIO TEMPI TRA ISCRIZIONE E TRASMISSIONE A UFFICIO SORVEGLIANZA
		sheet = wb.createSheet("Elenco per Tempi Trasmissione");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// nRow = 0;
		/* nRow = */CreaDettaglioTempi_D3_Mese(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin, TipoRicerca, PeriodoRic);

		// Quarto foglio; DETTAGLIO TEMPI TRA TRASMISSIONE A UFFICIO SORVEGLIANZA E DECISIONE UFFICIO
		// SORVEGLIANZA
		sheet = wb.createSheet("Elenco per Tempi Decisione UDS");
		sheet = settaLarghezzaColumn_DettaglioTempiCPP(sheet);

		// nRow = 0;
		/* nRow = */CreaDettaglioTempi_D4_Mese(aVect, sheet, csNull, cs, csBold, csBoldCenter, font,
				uffUteConnesso, DescIntesta, dataIni, dataFin, TipoRicerca, PeriodoRic);
	} // CHIUDE ExCreateXLSDettaglioTempiIscrizioniCPP_PerMese()

	private HSSFSheet settaLarghezzaColumn_DettaglioTempiCPP(HSSFSheet sheet) {

		sheet.setColumnWidth(0, (16 * 256)); // Anno/Numero Proc
		sheet.setColumnWidth(1, (23 * 256)); // Data comunicazione impossibilita esazione
		sheet.setColumnWidth(2, (10 * 256)); // Tempi
		sheet.setColumnWidth(3, (16 * 256)); // Descrizione tempo
		sheet.setColumnWidth(4, (27 * 256)); // Data arrivo in cancelleria
		sheet.setColumnWidth(5, (18 * 256)); // Data Iscrizione Fascicolo
		sheet.setColumnWidth(6, (27 * 256)); // Data Trasmissione a UDS
		sheet.setColumnWidth(7, (25 * 256)); // Data altro provvedimento Pubblico Ministero
		sheet.setColumnWidth(8, (20 * 256)); // Data Decisione UDS
		sheet.setColumnWidth(9, (40 * 256)); // Cognome / Nome Soggetto
		sheet.setColumnWidth(10, (10 * 256)); // Cod magistrato
		sheet.setColumnWidth(11, (40 * 256)); // Cognome / Nome Magistrato

		return sheet;
	}

	private HSSFRow valorizzaTestata_DettaglioTempiCPP(HSSFSheet sheet, int riga,
			HSSFCellStyle csBoldCenter) {

		HSSFRow row = sheet.getRow(riga);
		if (row == null)
			row = sheet.createRow(riga);

		setCell(row, 0, "N. FASCICOLO", csBoldCenter);
		setCell(row, 1, "DATA IMPOS. ESAZIONE", csBoldCenter);
		setCell(row, 2, "TEMPI", csBoldCenter);
		setCell(row, 3, "DESCRIZIONE", csBoldCenter);
		setCell(row, 4, "DATA ARRIVO CANCELLERIA", csBoldCenter);
		setCell(row, 5, "DATA ISCRIZIONE", csBoldCenter);
		setCell(row, 6, "DATA TRASMISSIONE A UDS", csBoldCenter);
		setCell(row, 7, "DATA ALTRO PROVV. PM", csBoldCenter);
		setCell(row, 8, "DATA DECISIONE UDS", csBoldCenter);
		setCell(row, 9, "COGNOME / NOME SOGGETTO", csBoldCenter);
		setCell(row, 10, "COD. MAG.", csBoldCenter);
		setCell(row, 11, "COGNOME / NOME  MAGISTRATO", csBoldCenter);

		return row;
	}

	private HSSFRow valorizzaRiga_DettaglioTempiCPP(HSSFSheet sheet, int riga, HSSFCellStyle cs,
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod, String Descrizione, String Tempo) {

		HSSFRow row = sheet.getRow(riga);

		if (row == null)
			row = sheet.createRow(riga);

		setCell(row, 0, " " + lMod.getChiaveAnno().toString() + "/" + lMod.getChiaveProgr().toString(), cs);
		setCell(row, 1,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataImpEsazione(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 2, " " + Tempo, cs);
		setCell(row, 3, " " + Descrizione, cs);
		setCell(row, 4,
				" " + StringUtils.toStringJSP(
						DateUtils.getDateToString(lMod.getDataArrivoAttoinCancelleria(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 5,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataIscrizione(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 6,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataTrasmissione(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 7,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataUltEventoPM(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 8,
				" " + StringUtils
						.toStringJSP(DateUtils.getDateToString(lMod.getDataEventoSorv(), "dd-MM-yyyy"), ""),
				cs);
		setCell(row, 9, " " + lMod.getCognome() + "  " + lMod.getNome(), cs);
		setCell(row, 10, " " + lMod.getCodiceMag(), cs);
		setCell(row, 11, " " + lMod.getCognomeMag() + "  " + lMod.getNomeMag(), cs);

		return row;
	}

	private int CreaDettaglioTempi_D1(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 1 : Intervalla Tra data impossibilità esazione e data arrivo in cancellaria
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di IMPOSSIBILITA' ESAZIONE e ARRIVO IN CANCELLERIA",
				csNull);

		nRow++;
		nRow++;
		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_1") == 0) {
				nRow++;

				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Arrivo_Comu_ImpEsa() != null) {
					if (lMod.getTempi_Arrivo_Comu_ImpEsa() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 5
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 20
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 30
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 60
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Arrivo_Comu_ImpEsa().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}

		}

		nRow++;

		return nRow;
	} // CHIUDE CreaDettaglioTempi_D1()

	private int CreaDettaglioTempi_D2(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 2 : Intervalla Tra data arrivo in cancellaria e data Iscrizione
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di ARRIVO IN CANCELLERIA e ISCRIZIONE",
				csNull);

		nRow++;
		nRow++;
		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_2") == 0) {
				nRow++;

				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Arrivo_Iscrizione() != null) {
					if (lMod.getTempi_Arrivo_Iscrizione() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 5 && lMod.getTempi_Arrivo_Iscrizione() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 20 && lMod.getTempi_Arrivo_Iscrizione() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 30 && lMod.getTempi_Arrivo_Iscrizione() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 60 && lMod.getTempi_Arrivo_Iscrizione() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Arrivo_Iscrizione().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaDettaglioTempi_D2()

	private int CreaDettaglioTempi_D3(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 3 : Intervalla Tra data Iscrizione e data Trasmissione a UDS
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di ISCRIZIONE FASCICOLO e TRASMISSIONE a UFFICIO di SORVEGLIANZA",
				csNull);

		nRow++;
		nRow++;
		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_3") == 0) {
				nRow++;

				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Iscrizione_Trasmissione() != null) {
					if (lMod.getTempi_Iscrizione_Trasmissione() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 5
							&& lMod.getTempi_Iscrizione_Trasmissione() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 20
							&& lMod.getTempi_Iscrizione_Trasmissione() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 30
							&& lMod.getTempi_Iscrizione_Trasmissione() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 60
							&& lMod.getTempi_Iscrizione_Trasmissione() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Iscrizione_Trasmissione().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaDettaglioTempi_D3()

	private int CreaDettaglioTempi_D4(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin) {

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 4 : Intervalla Tra data Trasmissione a UDS e Data Decisione UDS
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di TRASMISSIONE A UFFICIO SORVEGLIANZA E DECISIONE UFFICIO SORVEGLIANZA",
				csNull);

		nRow++;
		nRow++;
		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_4") == 0) {
				nRow++;

				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Trasmissione_Provv_Sorv() != null) {
					if (lMod.getTempi_Trasmissione_Provv_Sorv() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 5
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 20
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 30
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 60
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Trasmissione_Provv_Sorv().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaDettaglioTempi_D4()

	private int CreaDettaglioTempi_D1_Mese(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin, String TipoRicerca, String PeriodoRic) {

		int nRow = 0;
		String lAnnoStat = dataIni.substring(6);

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 1 : Intervalla Tra data impossibilità esazione e data arrivo in cancellaria
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di IMPOSSIBILITA' ESAZIONE e ARRIVO IN CANCELLERIA",
				csNull);

		nRow++;
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, PeriodoRic + " " + TipoRicerca.toUpperCase(), csNull);

		nRow++;
		nRow++;

		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_1") == 0) {
				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Arrivo_Comu_ImpEsa() != null) {
					if (lMod.getTempi_Arrivo_Comu_ImpEsa() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 5
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 20
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 30
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 60
							&& lMod.getTempi_Arrivo_Comu_ImpEsa() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Arrivo_Comu_ImpEsa() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Arrivo_Comu_ImpEsa().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				nRow++;
				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;
		return nRow;
	} // CHIUDE CreaDettaglioTempi_D1_Mese()

	private int CreaDettaglioTempi_D2_Mese(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin, String TipoRicerca, String PeriodoRic) {

		int nRow = 0;
		String lAnnoStat = dataIni.substring(6);

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 2 : Intervalla Tra data arrivo in cancellaria e data Iscrizione
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di ARRIVO IN CANCELLERIA e ISCRIZIONE",
				csNull);

		nRow++;
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, PeriodoRic + " " + TipoRicerca.toUpperCase(), csNull);

		nRow++;
		nRow++;

		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_2") == 0) {
				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Arrivo_Iscrizione() != null) {
					if (lMod.getTempi_Arrivo_Iscrizione() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 5 && lMod.getTempi_Arrivo_Iscrizione() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 20 && lMod.getTempi_Arrivo_Iscrizione() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 30 && lMod.getTempi_Arrivo_Iscrizione() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 60 && lMod.getTempi_Arrivo_Iscrizione() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Arrivo_Iscrizione() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Arrivo_Iscrizione().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				nRow++;
				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;
		return nRow;
	} // CHIUDE CreaDettaglioTempi_D2_Mese()

	private int CreaDettaglioTempi_D3_Mese(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin, String TipoRicerca, String PeriodoRic) {

		int nRow = 0;
		String lAnnoStat = dataIni.substring(6);

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 3 : Intervalla Tra data Iscrizione e data Trasmissione a UDS
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di ISCRIZIONE FASCICOLO e TRASMISSIONE a UFFICIO di SORVEGLIANZA",
				csNull);

		nRow++;
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, PeriodoRic + " " + TipoRicerca.toUpperCase(), csNull);

		nRow++;
		nRow++;

		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_3") == 0) {
				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Iscrizione_Trasmissione() != null) {
					if (lMod.getTempi_Iscrizione_Trasmissione() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 5
							&& lMod.getTempi_Iscrizione_Trasmissione() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 20
							&& lMod.getTempi_Iscrizione_Trasmissione() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 30
							&& lMod.getTempi_Iscrizione_Trasmissione() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 60
							&& lMod.getTempi_Iscrizione_Trasmissione() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Iscrizione_Trasmissione() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Iscrizione_Trasmissione().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				nRow++;
				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;

		return nRow;
	} // CHIUDE CreaDettaglioTempi_D3_Mese()

	private int CreaDettaglioTempi_D4_Mese(Vector<DettaglioTempiIscrizioneProcedimentiCPPModel> aVect,
			HSSFSheet sheet, HSSFCellStyle csNull, HSSFCellStyle cs, HSSFCellStyle csBold,
			HSSFCellStyle csBoldCenter, HSSFFont font, UfficioModel uffUteConnesso, String DescIntesta,
			String dataIni, String dataFin, String TipoRicerca, String PeriodoRic) {

		int nRow = 0;
		String lAnnoStat = dataIni.substring(6);

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Statistica del " + DateUtils.getSysDate("dd/MM/yyyy") + " relativa al periodo dal "
				+ dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		// DISTINTA 4 : Intervalla Tra data Trasmissione a UDS e Data Decisione UDS
		row = sheet.createRow(nRow);
		setCell(row, 0,
				"Distinta dei fascicoli con intervallo tra le date di TRASMISSIONE A UFFICIO SORVEGLIANZA E DECISIONE UFFICIO SORVEGLIANZA",
				csNull);

		nRow++;
		nRow++;
		nRow++;

		row = sheet.createRow(nRow);
		setCell(row, 0, "ANNO " + lAnnoStat, csNull);
		setCell(row, 1, PeriodoRic + " " + TipoRicerca.toUpperCase(), csNull);

		nRow++;
		nRow++;

		row = valorizzaTestata_DettaglioTempiCPP(sheet, nRow, csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioTempiIscrizioneProcedimentiCPPModel lMod = (DettaglioTempiIscrizioneProcedimentiCPPModel) itx
					.next();

			if (lMod.getTipodettaglio().compareTo("DISTINTA_4") == 0) {
				String Descrizio = "";
				String Tempo = "";
				if (lMod.getTempi_Trasmissione_Provv_Sorv() != null) {
					if (lMod.getTempi_Trasmissione_Provv_Sorv() < 6)
						Descrizio = "ENTRO 5 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 5
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 21)
						Descrizio = "ENTRO 20 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 20
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 31)
						Descrizio = "ENTRO 30 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 30
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 61)
						Descrizio = "ENTRO 60 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 60
							&& lMod.getTempi_Trasmissione_Provv_Sorv() < 91)
						Descrizio = "ENTRO 90 GIORNI";
					else if (lMod.getTempi_Trasmissione_Provv_Sorv() > 90)
						Descrizio = "OLTRE 90 GIORNI";

					Tempo = lMod.getTempi_Trasmissione_Provv_Sorv().toString();
				} else {
					Tempo = "-";
					Descrizio = "-";
				}

				nRow++;
				row = valorizzaRiga_DettaglioTempiCPP(sheet, nRow, cs, lMod, Descrizio, Tempo);
			}
		}

		nRow++;
		return nRow;
	} // CHIUDE CreaDettaglioTempi_D4_Mese()

	/**
	 * Crea il foglio excel per l'elenco Dettagliato delle varie Tipologie di ARCHIVIAZIONE dei Procedimenti
	 * di Classe VII (Conversione Pene Pecuniarie)
	 *
	 * @param aVect
	 *            Vettore con i Dettagli per le tre tipologie di Procedimenti Classe VII
	 */

	// 07-06-2016 - Riciclo dopo primo collaudo V.10
	// public void ExCreateFoglioDettaglioArchiviazioni_CPP(Vector aVect, HSSFWorkbook wb, UfficioModel
	// uffUteConnesso, String DescIntesta) throws F3BException
	public void ExCreateFoglioDettaglioArchiviazioni_CPP(Vector aVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta)
			throws F3BException { // 07-06-2016 - END Riciclo

		HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo con la propietà Testo a Capo
		HSSFCellStyle csWrap = getBordo4Lati(wb);
		csWrap.setWrapText(true);

		// stile per celle col bordo con carattere grassetto
		HSSFCellStyle csBold = getBordo4Lati(wb);
		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		csBold.setFont(font);

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFSheet sheet;

		// primo foglio; DETTAGLIO_ISCRIZIONI
		sheet = wb.createSheet("Elenco Archiviazioni");

		sheet.setColumnWidth(0, (16 * 256)); // Anno/Numero Proc
		sheet.setColumnWidth(1, (18 * 256)); // Data Iscrizione Fascicolo
		sheet.setColumnWidth(2, (20 * 256)); // Data Archiviazione
		sheet.setColumnWidth(3, (30 * 256)); // Tipo Sanzione
		sheet.setColumnWidth(4, (25 * 256)); // Data Inizio SS
		sheet.setColumnWidth(5, (27 * 256)); // Data Scadenza SS
		sheet.setColumnWidth(6, (65 * 256)); // Motivo Archiviazione
		sheet.setColumnWidth(7, (40 * 256)); // Cognome / Nome Soggetto

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);

		// 07-06-2016 - Riciclo dopo primo collaudo V.10
		// setCell(row, 0, "Elenco procedimenti archiviati al " + DateUtils.getSysDate("dd-MM-yyyy"),
		// csNull);
		setCell(row, 0, "Elenco procedimenti archiviati dal " + dataIni + " al " + dataFin, csNull);
		// 07-06-2016 - END Riciclo

		nRow++;
		nRow++;
		nRow++;

		// TESTATA ELENCO ARCHIVIATI
		row = sheet.createRow(nRow);

		setCell(row, 0, "N. FASCICOLO", csBoldCenter);
		setCell(row, 1, "DATA ISCRIZIONE", csBoldCenter);
		setCell(row, 2, "DATA ARCHIVIAZIONE", csBoldCenter);
		setCell(row, 3, "TIPO SANZIONE ", csBoldCenter);
		setCell(row, 4, "DATA INIZIO SANZ.SOS.", csBoldCenter);
		setCell(row, 5, "DATA SCADENZA SANZ.SOS.", csBoldCenter);
		setCell(row, 6, "MOTIVO ARCHIVIAZIONE", csBoldCenter);
		setCell(row, 7, "COGNOME / NOME ", csBoldCenter);

		Iterator itx = aVect.iterator();
		while (itx.hasNext()) {
			DettaglioArchiviazioniCPPModel lMod = (DettaglioArchiviazioniCPPModel) itx.next();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" --XX-- StatisControllerCPP - ExCreateFoglioDettaglioArchiviazioni_CPP lMod =
			// "+lMod);
			// test per cambio anno
			// if (annoOld != lMod.getAnno().intValue())
			// {
			// nRow++;
			// nRow++;
			// nRow++;

			// scrittura anno
			// row = sheet.createRow(nRow);
			// setCell(row, 0, "ANNO "+lMod.getAnno(), csNull);

			// nRow++;

			// }

			// annoOld = lMod.getAnno().intValue();
			nRow++;
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			setCell(row, 0, " " + lMod.getChiaveAnno().toString() + "/" + lMod.getChiaveProgr().toString(),
					cs);
			setCell(row, 1, " " + StringUtils
					.toStringJSP(DateUtils.getDateToString(lMod.getDataIscrizione(), "dd-MM-yyyy"), ""), cs);
			setCell(row, 2, " " + StringUtils
					.toStringJSP(DateUtils.getDateToString(lMod.getDataArchiviazione(), "dd-MM-yyyy"), ""),
					cs);
			setCell(row, 3, " " + StringUtils.toStringJSP(lMod.getDescTipoSanzione(), ""), cs);
			setCell(row, 4,
					" " + StringUtils
							.toStringJSP(DateUtils.getDateToString(lMod.getDataInizioSS(), "dd-MM-yyyy"), ""),
					cs);
			setCell(row, 5, " " + StringUtils
					.toStringJSP(DateUtils.getDateToString(lMod.getDataScadenzaSS(), "dd-MM-yyyy"), ""), cs);
			setCell(row, 6, " " + StringUtils.toStringJSP(lMod.getDescMotivoArch(), ""), csWrap);
			setCell(row, 7, " " + lMod.getCognome() + "  " + lMod.getNome(), cs);

		}

		nRow++;
	} // CHIUDE ExCreateFoglioDettaglioArchiviazioni_CPP()

	/**
	 * Statistica: RIEPILOGO PROCEDIMENTI PENEDENTI (Classe VII - Conversione Pene Pecuniarie) Crea il foglio
	 * excel per il Riepilogo Definiti aggregati per Anno dei Procedimenti Classe VII (Conversione Pene
	 * Pecuniarie)
	 */
	public void ExCreateRiepilogoDefinitiCPP(Vector<RiepilogoPendentiDefinitiCPPModel> aVect, HSSFWorkbook wb,
			UfficioModel uffUteConnesso, String dataIni, String dataFin, String DescIntesta)
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

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(font);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csNullCenter = wb.createCellStyle();
		csNullCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		HSSFCellStyle csNullCenterBold = wb.createCellStyle();
		csNullCenterBold.setFont(font);
		csNullCenterBold.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// primo foglio; RIEPILOGO_ISCRIZIONI
		HSSFSheet sheet = wb.createSheet("Riepilogo Definiti");
		sheet.setColumnWidth(0, (60 * 256));

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(sheet, uffUteConnesso, csNull, DescIntesta);

		nRow++;
		nRow++;

		HSSFRow row = sheet.createRow(nRow);
		setCell(row, 0, "Riepilogo Definiti relativo al periodo dal " + dataIni + " al " + dataFin, csNull);

		nRow++;
		nRow++;

		row = sheet.getRow(nRow);
		if (row == null)
			row = sheet.createRow(nRow);

		csBold.setWrapText(true);
		setCell(row, 0, "PROCEDIMENTI DEFINITI CON PROVVEDIMENTO DEL GIUDICE DELL’ESECUZIONE", csBold);

		Iterator itx = aVect.iterator();

		int nRowAnno = nRow; // nRowAnno = 6
		int annoOld = 0;
		short nColAnno = 0;
		String formula = "";

		int nRowAnno_LC = 0;
		int nRowAnno_LS = 0;
		int nRowAnno_NLP = 0;
		int nRowAnno_ADEF = 0;

		String formula_LC = "";
		String formula_LS = "";

		int RigaTot_LC = 0;
		int RigaTot_LS = 0;
		// int RigaTot_NLP = 0;

		while (itx.hasNext()) {
			RiepilogoPendentiDefinitiCPPModel lMod = (RiepilogoPendentiDefinitiCPPModel) itx.next();

			// test per cambio anno
			if (annoOld != lMod.getAnno().intValue()) {
				nColAnno++;
				nRow = nRowAnno;

				row = sheet.getRow(nRowAnno);
				if (row == null)
					row = sheet.createRow(nRowAnno);

				setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			}

			annoOld = lMod.getAnno().intValue();
			nRow++;

			// INIZIO scrittura tipologia DEFINITI
			// DEFINITI per Provv. GE
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Applicazione Indulto", cs);
			setCell(row, nColAnno, lMod.getDefGEIndulto().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Applicazione Amnistia", cs);
			setCell(row, nColAnno, lMod.getDefGEAmnistia().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Estinzione pena per Morte del Reo", cs);
			setCell(row, nColAnno, lMod.getDefGEMorteReo().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Estinzione pena per Decorso del Tempo", cs);
			setCell(row, nColAnno, lMod.getDefGE_EstinzioneperDecorsoTempo().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Estinzione pena per intervenuta depenalizzazione", cs);
			setCell(row, nColAnno, lMod.getDefGEDepenalizzazione().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Altro", cs);
			setCell(row, nColAnno, lMod.getDefGEAltro().doubleValue(), cs);
			nRow++;

			// scrittura totale anno per: DEFINITI GE
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE", csBoldCenter);
			formula = this.getStringaSomma(nRowAnno + 1, nColAnno, nRow - 1, nColAnno);
			setFormulaCell(row, nColAnno, formula, csBold);

			nRow++;
			nRow++;

			// DEFINITI con PENA PEC. convertita in LIB. CONTR.
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);

			csBold.setWrapText(true);
			setCell(row, 0, "DEFINIZIONI PROCEDIMENTI CON PENA PECUNIARIA CONVERTITA IN SANZIONE SOSTITUTIVA",
					csBold);
			// setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ESPIAZIONE DI PENA IN LIBERTA' CONTROLLATA", csNullCenter);
			nRow++;

			nRowAnno_LC = nRow;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Declaratoria estinzione Libertà Controllata", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_Est_libCon().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Revoca sanzione sostitutiva e conversione in Pena Detentiva", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_RevocaLC_inPenaDet().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Conversione sanzione sostitutiva in Pena Detentiva", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_ConvLC_inPenaDet().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Altro", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_AltroLC().doubleValue(), cs);
			nRow++;

			// Totale LIBERTA CONTROLLARA
			RigaTot_LC = nRow;
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE ", csBoldCenter);
			formula_LC = this.getStringaSomma(nRowAnno_LC, nColAnno, nRow - 1, nColAnno);
			setFormulaCell(row, nColAnno, formula_LC, csBold);
			nRow++;
			nRow++;

			// DEFINITI con PENA PEC. convertita in LAV. SOST.
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "ESPIAZIONE DI PENA IN LAVORO SOSTITUTIVO", csNullCenter);
			nRow++;

			nRowAnno_LS = nRow;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Declaratoria estinzione Lavoro Sostitutivo", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_Est_LavSos().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Revoca sanzione sostitutiva e conversione in Pena Detentiva", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_RevocaLS_inPenaDet().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Conversione sanzione sostitutiva in Pena Detentiva", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_ConvLS_inPenaDet().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Altro", cs);
			setCell(row, nColAnno, lMod.getDefSanSos_AltroLS().doubleValue(), cs);
			nRow++;

			// Totale LAVORO SOSTITUTIVO
			RigaTot_LS = nRow;
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE ", csBoldCenter);
			formula_LS = this.getStringaSomma(nRowAnno_LS, nColAnno, nRow - 1, nColAnno);
			setFormulaCell(row, nColAnno, formula_LS, csBold);
			nRow++;

			// Totale LAVORO SOSTITUTIVO + LIBERTA CONTROLLATA
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE (Convertiti in Sanzione Sostitutiva)", csBoldCenter);

			CellReference cellRef1 = new CellReference(RigaTot_LC, nColAnno);
			CellReference cellRef2 = new CellReference(RigaTot_LS, nColAnno);
			formula = "SUM(" + cellRef1.formatAsString() + "," + cellRef2.formatAsString() + ")";

			setFormulaCell(row, nColAnno, formula, csBold);
			nRow++;
			nRow++;

			// DEFINITI con Provv di UDS con NLP
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			csBold.setWrapText(true);
			setCell(row, 0, "DEFINITI DA UFFICIO DI SORVEGLIANZA CON PROVVEDIMENTO DI NON LUOGO A PROVVEDERE",
					csBold);
			// setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			nRow++;

			nRowAnno_NLP = nRow;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per Avvenuto Pagamento", cs);
			setCell(row, nColAnno, lMod.getDefNLP_Pagamento().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per Morte del reo", cs);
			setCell(row, nColAnno, lMod.getDefNLP_MorteReo().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per irreperibilità", cs);
			setCell(row, nColAnno, lMod.getDefNLP_Irreperibilita().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per accertata solvibilità", cs);
			setCell(row, nColAnno, lMod.getDefNLP_Solvibilita().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per Intervenuta Prescrizione", cs);
			setCell(row, nColAnno, lMod.getDefNLP_Prescrizione().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per Assorbimenro in Cumulo", cs);
			setCell(row, nColAnno, lMod.getDefNLP_AssorbimentoCumulo().doubleValue(), cs);
			nRow++;

			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "N.L.P. per Altro", cs);
			setCell(row, nColAnno, lMod.getDefNLP_Altro().doubleValue(), cs);
			nRow++;

			// Totale Definiti per N.L.P.
			// RigaTot_NLP = nRow;
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE ", csBoldCenter);
			formula = this.getStringaSomma(nRowAnno_NLP, nColAnno, nRow - 1, nColAnno);
			setFormulaCell(row, nColAnno, formula, csBold);
			nRow++;
			nRow++;

			// DEFINITI PER ALTRE DEFINIZIONI
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			csBold.setWrapText(true);
			setCell(row, 0, "DEFINITI PER ALTRE DEFINIZIONI", csBold);
			// setCell(row, nColAnno, lMod.getAnno().doubleValue(), csBoldCenter);
			nRow++; // 41

			nRowAnno_ADEF = nRow;
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "Totale Altre definizioni", cs);
			setCell(row, nColAnno, lMod.getAltreDefinizioni().doubleValue(), cs);
			nRow++; // 42

			nRow++;
			// Totali GENERALI per Ogni ANNO
			row = sheet.getRow(nRow);
			if (row == null)
				row = sheet.createRow(nRow);
			setCell(row, 0, "TOTALE GENERALE", csBoldCenter);

			CellReference cellRefA = new CellReference(nRowAnno_LC - 4, nColAnno);
			CellReference cellRefB = new CellReference(nRowAnno_NLP - 3, nColAnno);
			CellReference cellRefC = new CellReference(nRowAnno_ADEF - 3, nColAnno);
			CellReference cellRefD = new CellReference(nRowAnno_ADEF, nColAnno);
			formula = "SUM(" + cellRefA.formatAsString() + "," + cellRefB.formatAsString() + ","
					+ cellRefC.formatAsString() + "," + cellRefD.formatAsString() + ")";

			setFormulaCell(row, nColAnno, formula, csBoldCenter);
			nRow++;

		}

		// fuori ciclo
		// Richiamo metodo per la scrittura delle formule contenenti
		// le somme parziali e generali
		nRow++; // 45
		nColAnno++;

		int RigaAnnoLC = nRowAnno_LC - 2;
		int RigaAnnoLS = nRowAnno_LS - 1;
		int RigaAnnoNLP = nRowAnno_NLP - 1;
		int RigaAnnoADEF = nRowAnno_ADEF - 1;

		nRow = setTotaliTempi_Per_Def(sheet, csBold, csBoldCenter, nRow, nRowAnno, nColAnno, RigaAnnoLC,
				RigaAnnoLS, RigaAnnoNLP, RigaAnnoADEF);
		nRow++;
	} // CHIUDI ExCreateRiepilogoDefinitiCPP()

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		return cs;
	}

	private HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

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

		value = ("Tel. " + uffUteConnesso.getTelefono() + " - Fax " + uffUteConnesso.getFax());
		setCell(row, 0, value, csNull);

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
				setCell(row, nColAnno, "TOTALE T", csBoldCenter);
			} else {

				somma = getStringaSomma(forRow, 1, forRow, nColAnno - 1);
				setFormulaCell(row, nColAnno, somma, csBold);
			}
		}

		return nRow;
	}

	private int setTotaliTempi_Per_Def(HSSFSheet sheet, HSSFCellStyle csBold, HSSFCellStyle csBoldCenter,
			int nRow, int nRowAnno, int nColAnno, int RigaAnnoLC, int RigaAnnoLS, int RigaAnnoNLP,
			int RigaAnnoADEF) {

		HSSFRow row = null;
		HSSFCell cell = null;

		String somma = "";

		int forRow = 0;

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--XX-- setTotaliTempi_Per_Def - nRow = "+nRow+" - nRowAnno = "+nRowAnno+" -
		// nColAnno = "+nColAnno);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--XX-- setTotaliTempi_Per_Def - RigaAnnoLC = "+RigaAnnoLC+" - RigaAnnoLS =
		// "+RigaAnnoLS);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--XX-- setTotaliTempi_Per_Def - RigaAnnoNLP = "+RigaAnnoNLP+" - RigaAnnoADEF =
		// "+RigaAnnoADEF);

		// Ciclo per scrivere il totale per tipologia per tutti gli anni
		for (forRow = nRowAnno; forRow < nRow - 1; forRow++) {
			row = sheet.getRow(forRow);
			if (row == null)
				row = sheet.createRow(forRow);

			// if (forRow == nRowAnno || forRow == RigaAnnoLC ||
			// forRow == RigaAnnoNLP || forRow == RigaAnnoADEF)
			if (forRow == nRowAnno) {
				cell = row.createCell(nColAnno);
				cell.setCellStyle(csBold);
				setCell(row, nColAnno, "TOTALE", csBoldCenter);
			} else if (forRow == RigaAnnoLC || forRow == RigaAnnoLC - 1 || forRow == RigaAnnoLC + 1
					|| forRow == RigaAnnoLS || forRow == RigaAnnoLS - 1 || forRow == RigaAnnoNLP
					|| forRow == RigaAnnoNLP - 1 || forRow == RigaAnnoADEF || forRow == RigaAnnoADEF - 1
					|| forRow == RigaAnnoADEF + 2) {
				// NON deve fare nulla, RIGA VUOTA
			} else {
				cell = row.createCell(nColAnno);
				cell.setCellStyle(csBold);

				somma = getStringaSomma(forRow, 1, forRow, nColAnno - 1);
				setFormulaCell(row, nColAnno, somma, csBoldCenter);
			}
		}

		return nRow;
	}

	private String getStringaSomma(int nRow1, int nCol1, int nRow2, int nCol2) {

		CellReference cellRef1 = new CellReference(nRow1, nCol1);
		CellReference cellRef2 = new CellReference(nRow2, nCol2);

		String formula = "SUM(" + cellRef1.formatAsString() + ":" + cellRef2.formatAsString() + ")";

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("--XX-- StatisControllerCPP.getStringaSomma fine : formula : "+ formula );

		return formula;
	}

	// -----------------------------------------------------------------------------------
	// ---> LANCIO DELLE STORE_PROCEDURE per la produzione delle Tabelle Statistiche

	/**
	 * Chiama la stored procedure ISPETTORATO_CPP.riepilogo_iscrizioni_CPP
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
	public void ExRiepilogoIscrizioniCPPStoredProcedure(String dataIni, String dataFin, String ufficio)
			// , String ufficioaccorpato1, String ufficioaccorpato2, String ufficioaccorpato3)
			throws F3BException {

		Connection lConn = null;

		StatisStoreProcedureDAO lSpDao = null;

		try {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("--XX-- StatisControllerCPP.ExRiepilogoIscrizioniCPPStoredProcedure - STORED
			// PROCEDURE - cod Uff = "+ufficio);
			lConn = getDBConnection();

			lSpDao = new StatisStoreProcedureDAO(lConn);

			lSpDao.setRiepilogoIscrizioniCPPStoreProcedure();
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
			throw new F3BException("StatisControllerCPP.ExRiepilogoIscrizioniCPPStoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("StatisControllerCPP.ExRiepilogoIscrizioniCPPStoredProcedure: " + sqe);
		} finally {
			cleanup(lSpDao);

			cleanup(lConn);
		}
	} // CHIUDE ExRiepilogoIscrizioniCPPStoredProcedure

	/**
	 * Chiama la stored procedure ISPETTORATO_CPP.stat_provvedimenti_CPP
	 *
	 * @param ufficio
	 *            Codice ufficio
	 * @param dataVerifica
	 *            Data in cui è lanciata la procedura
	 * @throws F3BException
	 *             propaga l'eccezione.
	 */
	public void ExStatProvvedimenti_CPP_StoredProcedure(String ufficio, String uffAccorpa1,
			String uffAccorpa2, String uffAccorpa3, String dataVerifica) throws F3BException {

		Connection lConn = null;
		StatisStoreProcedureDAO lSpDao = null;

		try {
			lConn = getDBConnection();

			lSpDao = new StatisStoreProcedureDAO(lConn);
			// lSpDao.setStatProvvedimentiStoreProcedure();
			lSpDao.setStatProvvedimenti_CPP_StoreProcedure();
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
			throw new F3BException("StatisControllerCPP.ExStatProvvedimenti_CPP_StoredProcedure: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("StatisControllerCPP.ExStatProvvedimenti_CPP_StoredProcedure: " + sqe);
		} finally {
			cleanup(lSpDao);
			cleanup(lConn);
		}
	} // CHIUDE ExStatProvvedimenti_CPP_StoredProcedure()

} // Chiude classe StatisController