package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.dao.IstanzaSqlDAO;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.nuovaistanza.dao.NuovaIstanzaSqlDAO;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloGPTPModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.tenore.model.TenoreProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title:TrasmissioneJMScontroller
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
public class TrasmissioneJMSController extends SiapController implements ITrasmissioneJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Preleva i dati dal DB per Nuova Istanza.
	 * <p>
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public MessaggioModel getMessageForNuovaIstanza(BigDecimal aKeyEvento, BigDecimal aKeyFascicolo)
			throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		EventoSqlDAO lEveDao = null;
		NotificaSqlDAO lNotDao = null;

		// IstanzaSqlDAO lIstDao = null;
		NuovaIstanzaSqlDAO lIstDao = null;

		FascicoloSiepModel lFascicolo = null;
		SoggettoModel lSoggMod = null;
		SentenzaModel lSentMod = null;

		MessaggioModel lMessage = null;

		// Lettura dell'Evento.
		EventoModel lEvento = ricercaEvento(aKeyEvento);
		EventoNotificaModel lEve = new EventoNotificaModel(lEvento);

		try {
			lConn = getDBConnection();
			/*
			 * Sostituita lEveDao = new EventoSqlDAO(lConn); //lEveDao.ricercaEventoByKey(aKeyEvento);
			 * //ricercaEventoIstanzaByKey lEveDao.ricercaEventoIstanzaByKey(aKeyEvento); EventoNotificaModel
			 * lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());
			 */
			lIstDao = new NuovaIstanzaSqlDAO(lConn);
			// lIstDao.ricercaByKeyEvento(aKeyEvento);
			lIstDao.ricercaNuovaIstanzaByEveIdEvento(aKeyEvento);
			// IstanzaModel lIst = new IstanzaModel((IstanzaModel) lIstDao.getModelByKey());

			NuovaIstanzaModel lIst = new NuovaIstanzaModel((NuovaIstanzaModel) lIstDao.getModelByKey());

			lTreeRoot = new TreeModel(createRoot(lEve.getEvento()));
			lTreeRoot.add(new TreeModel(lIst));

			// Si recupera tutta la storia del fascicolo...
			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lFascDao.ricercaFascicoloByKey(aKeyFascicolo);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			// Cerca il soggetto associato al fascicolo.
			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			lFascicolo.setSoggetto(lSoggMod);

			// Cerca la sentenza associata al fascicolo.
			lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
			lSentMod = (SentenzaModel) lSentDao.getModelByKey();

			lFascicolo.setSentenza(lSentMod);

			// ---BUG TreeModel lTreeEveMod = new TreeModel(lEve.getEvento());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("SPEDISCO EVENTO = " + lEve);

			TreeModel lTreeEveMod = new TreeModel(lEve);

			TreeModel lTreeFasMod = new TreeModel(lFascicolo);
			TreeModel lTreeSogMod = new TreeModel(lFascicolo.getSoggetto());
			TreeModel lTreeSenMod = new TreeModel(lFascicolo.getSentenza());

			lTreeRoot.add(lTreeEveMod); // Evento Model
			lTreeRoot.add(lTreeFasMod); // Fascicolo Model
			lTreeRoot.add(lTreeSogMod); // Soggetto Model
			lTreeRoot.add(lTreeSenMod); // Sentenza Model

			lTreeRoot.add(ricercaFascicoloSiepCompleto(aKeyFascicolo));

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("fine");

		} catch (DAOException daoEx) {
			throw new F3BException(
					"TrasmissioneJMSController.getMessageForNuovaIstanza: Non posso leggere : " + daoEx);
		}
		/*
		 * catch (SQLException sqe) { throw new
		 * F3BException("TrasmissioneJMSController.getMessageForNuovaIstanza: Non posso leggere  : " + sqe); }
		 */
		catch (Exception ex) {
			throw new F3BException(
					"TrasmissioneJMSController.getMessageForNuovaIstanza : Non posso leggere : " + ex);
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lMessage;
	}

	/**
	 * Preleva i dati dal DB per le stampe di IStruttoria
	 * 
	 * @param aEveModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForIstanza(BigDecimal aKeyEvento, BigDecimal aKeyFascicolo)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		EventoSqlDAO lEveDao = null;
		NotificaSqlDAO lNotDao = null;
		IstanzaSqlDAO lIstDao = null;

		FascicoloSiepModel lFascicolo = null;
		SoggettoModel lSoggMod = null;
		SentenzaModel lSentMod = null;

		MessaggioModel lMessage = null;

		// Lettura dell'Evento Luigi 22-3-05
		EventoModel lEvento = ricercaEvento(aKeyEvento);
		EventoNotificaModel lEve = new EventoNotificaModel(lEvento);

		try {
			lConn = getDBConnection();
			/*
			 * Sostituita lEveDao = new EventoSqlDAO(lConn); //lEveDao.ricercaEventoByKey(aKeyEvento);
			 * //ricercaEventoIstanzaByKey lEveDao.ricercaEventoIstanzaByKey(aKeyEvento); EventoNotificaModel
			 * lEve = new EventoNotificaModel((EventoModel) lEveDao.getModelByKey());
			 */
			lIstDao = new IstanzaSqlDAO(lConn);
			lIstDao.ricercaByKeyEvento(aKeyEvento);
			IstanzaModel lIst = new IstanzaModel((IstanzaModel) lIstDao.getModelByKey());

			lTreeRoot = new TreeModel(createRoot(lEve.getEvento()));

			lTreeRoot.add(new TreeModel(lIst));

			// DEvo prendere tutta la storia del fascicolo...
			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lFascDao.ricercaFascicoloByKey(aKeyFascicolo);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			// Cerca il soggetto associato al fascicolo
			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			lFascicolo.setSoggetto(lSoggMod);

			// Cerca la sentenza associata al fascicolo
			lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
			lSentMod = (SentenzaModel) lSentDao.getModelByKey();

			lFascicolo.setSentenza(lSentMod);

			// ---BUG TreeModel lTreeEveMod = new TreeModel(lEve.getEvento());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("SPEDISCO EVENTO = " + lEve);
			TreeModel lTreeEveMod = new TreeModel(lEve);

			TreeModel lTreeFasMod = new TreeModel(lFascicolo);
			TreeModel lTreeSogMod = new TreeModel(lFascicolo.getSoggetto());
			TreeModel lTreeSenMod = new TreeModel(lFascicolo.getSentenza());

			lTreeRoot.add(lTreeEveMod);
			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(lTreeSenMod);

			lTreeRoot.add(ricercaFascicoloSiepCompleto(aKeyFascicolo));

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);

		} catch (DAOException daoEx) {
			throw new F3BException("TrasmissioneJMSController.getMessageForIstanza: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lMessage;
	}

	/**
	 * Preleva i dati dal DB per trasmettere i dati di provv. - Legge Simeone.
	 * 
	 * @param aKeyEvento
	 * @param aKeyFascicolo
	 * @return lMessage
	 * @throws F3BException
	 */
	public MessaggioModel getMessageForProvvedimento(BigDecimal aKeyEvento, BigDecimal aKeyFascicolo)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;

		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		EventoSqlDAO lEveDao = null;
		NotificaSqlDAO lNotDao = null;
		FascicoloSiepModel lFascicolo = null;
		SoggettoModel lSoggMod = null;
		SentenzaModel lSentMod = null;

		MessaggioModel lMessage = null;

		// Lettura dell'Evento Luigi 22-3-05
		EventoModel lEvento = ricercaEvento(aKeyEvento);
		EventoNotificaModel lEve = new EventoNotificaModel(lEvento);

		try {

			lConn = getDBConnection();
			/*
			 * Sostituita Luigi 22-3-05 lEveDao = new EventoSqlDAO(lConn);
			 * lEveDao.ricercaEventoByKey(aKeyEvento); EventoNotificaModel lEve = new
			 * EventoNotificaModel((EventoModel) lEveDao.getModelByKey());
			 */

			// /lIstDao = new IstanzaSqlDAO(lConn);
			// /lIstDao.ricercaByKeyEvento(aKeyEvento);
			// /IstanzaModel lIst = new IstanzaModel((IstanzaModel) lIstDao.getModelByKey());

			lTreeRoot = new TreeModel(createRoot(lEve.getEvento()));

			// /lTreeRoot.add(new TreeModel(lIst));

			// Devo prendere tutta la storia del fascicolo...
			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lFascDao.ricercaFascicoloByKey(aKeyFascicolo);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			// Cerca il soggetto associato al fascicolo
			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			lFascicolo.setSoggetto(lSoggMod);

			// Cerca la sentenza associata al fascicolo
			lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
			lSentMod = (SentenzaModel) lSentDao.getModelByKey();

			lFascicolo.setSentenza(lSentMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("SPEDISCO EVENTO = " + lEve);
			TreeModel lTreeEveMod = new TreeModel(lEve);

			TreeModel lTreeFasMod = new TreeModel(lFascicolo);
			TreeModel lTreeSogMod = new TreeModel(lFascicolo.getSoggetto());
			TreeModel lTreeSenMod = new TreeModel(lFascicolo.getSentenza());

			lTreeRoot.add(lTreeEveMod);
			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(lTreeSenMod);
			lTreeRoot.add(ricercaFascicoloSiepCompleto(aKeyFascicolo));

			lMessage = new MessaggioModel();
			lMessage.setTreeModel(lTreeRoot);

			// Richiamo del ReportGenerator per debug
			// ReportGenerator lRep = new ReportGenerator();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info( "Treemodel nel messaggio " + lRep.debugTreeXML(lTreeRoot));
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.info(getClass().getPackage().getName() + ".getMessageForProvvedimento: fine");

		} catch (DAOException daoEx) {
			throw new F3BException(
					"TrasmissioneJMSController.getMessageForProvvedimento: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lEveDao);
			cleanup(lNotDao);

			cleanup(lConn);
		}

		return lMessage;
	}

	/**
	 * Crea la root del Documento
	 * 
	 * @param aEveModel
	 * @return
	 */
	private XModel createRoot(EventoModel lEve) {
		XModel lStampa = new XModel();

		String descrTipoUff = lEve.getDescrUfficioEmittente();

		lStampa.setUfficio(lEve.getDescrLuogoEmittente().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff.toUpperCase());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug(" UFFICIO >>>  " + descrTipoUff);

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
		}
		return lStampa;
	}

	/**
	 * Ricerca del fascicolo Siep
	 * 
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	private TreeModel ricercaFascicoloSiepCompleto(BigDecimal aKeyFascicolo) throws F3BException {
		IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();

		TreeModel lTreeRoot = null;
//		MessaggioModel lMessage = new MessaggioModel();
		DepositoDecretoSqlDAO lDepDecSqlDAO = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDAO = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		SoggettoSqlDAO lSogSqlDao = null; // 21/06/2010
		Connection lConn = null;

		try {
			if (aKeyFascicolo != null) {
				DettaglioFascicoloModel lDettFascicolo = null;
				try {
					lDettFascicolo = lFasc.ExDettaglioFascicoloSiep(aKeyFascicolo);
				} catch (F3BException ex) {
					if (ex.getErrorCode() == F3BException.EX_NOT_FOUND) {
					} else
						throw ex;
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ricerco Eventi per fascicolo SIEP.");

				IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();

				RicercaJMSController lContrl = new RicercaJMSController();
				Vector lEventi = lContrl.ricercaEventoNotificaByFascicoloSiepPerTrasferimento(aKeyFascicolo);

				Vector lEventiNot = new Vector();

				Iterator lEveItx = lEventi.iterator();

				lConn = getDBConnection();

				while (lEveItx.hasNext()) {
					EventoModel lEveTemp = (EventoModel) lEveItx.next();
					EventoNotificaModel lEveNotModel = lEveCtrl.ExRicercaEventoNotificaByKey(lEveTemp
							.getIdEvento());
					// Se c'e' il BLOB ... Luigi 22-3-05
					if (lEveTemp.getDocBlobOut() != null)
						lEveNotModel.getEvento().setDocPerTrasferimento(
								lEveTemp.getDocBlobOut().toByteArray());

					if (lEveTemp.getFasSiuIdFascicoloSius() != null) {
						FascicoloGPTPModel lFasGPTPModel = new FascicoloGPTPModel();

						// Sostituito il FascicoloSiusModel con l'aggregato FascicoloGPModel.
						/*
						 * FascicoloSiusDAO lFasSiusDao = new FascicoloSiusDAO(lConn);
						 * lFasSiusDao.setIdFascicoloSius(lEveTemp.getFasSiuIdFascicoloSius());
						 * lFasSiusDao.setCondizioneUpdate(lEveTemp.getFasSiuIdFascicoloSius());
						 * FascicoloSiusModel lFas = (FascicoloSiusModel) lFasSiusDao.getModelByKey();
						 * lEveNotModel.setFascicoloSiusModel(lFas);
						 */
						lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
						lFasGPSqlDao.ricercaFascicoloByKey(lEveTemp.getFasSiuIdFascicoloSius());
						FascicoloGPModel lFasGPModel = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

						// 21/06/2010 Caricamento Soggetto SIUS.
						lSogSqlDao = new SoggettoSqlDAO(lConn);
						lSogSqlDao.ricercaSoggettoByKey(lFasGPModel.getFascicoloSiusModel()
								.getSogIdSoggetto());
						SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
						lFasGPModel.getFascicoloSiusModel().setSoggetto(lSogModel);

						lTenSqlDao = new TenoreSqlDAO(lConn);
						lTenSqlDao.ricercaTenoreByGeneraleProc(lFasGPModel.getGeneraleProcedimentoModel()
								.getIdGeneraleProcedimento());
						Vector lVectTenori = new Vector(lTenSqlDao.getModels());
						if (lVectTenori != null) {
							TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori
									.toArray(new TenoreModel[0]);
							lFasGPModel.setTenori(lTenoriModel);
						}
						// 26/04/2005 Sostituito il FascicoloGPModel con FascicoloGPTPModel;
						// lEveNotModel.setFascicoloGP(lFasGPModel);
						lFasGPTPModel.setFascicoloSiusModel(lFasGPModel.getFascicoloSiusModel());
						lFasGPTPModel
								.setGeneraleProcedimentoModel(lFasGPModel.getGeneraleProcedimentoModel());
						TenoreProvvedimentoModel[] lTenoriProModel = new TenoreProvvedimentoModel[lVectTenori
								.size()];
						for (int j = 0; j < lVectTenori.size() - 1; j++) {
							TenoreProvvedimentoModel lTenProModel = new TenoreProvvedimentoModel();
							lTenProModel.setTenore(lFasGPModel.getTenori()[j]);
							if (lTenProModel.getTenore().getDepDecIdDepositoDecreto() != null) {
								lDepDecSqlDAO = new DepositoDecretoSqlDAO(lConn);
								lDepDecSqlDAO.ricercaDepositoDecretoByKey(lTenProModel.getTenore()
										.getDepDecIdDepositoDecreto());
								lTenProModel.setDecreto((DepositoDecretoModel) lDepDecSqlDAO.getModelByKey());
								lDepDecSqlDAO.stop();
							}
							if (lTenProModel.getTenore().getDepOpidDepositoOrdinanzaPc() != null) {
								lDepOrdSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn);
								lDepOrdSqlDAO.ricercaDepositoOrdinanzaPcByKey(lTenProModel.getTenore()
										.getDepOpidDepositoOrdinanzaPc());
								lTenProModel.setOrdinanza((DepositoOrdinanzaPcModel) lDepOrdSqlDAO
										.getModelByKey());
							}
							// Si carica l'Array di aggregati dei tenori.
							lTenoriProModel[j] = lTenProModel;
						}
						lFasGPTPModel.setTenori(lTenoriProModel);

						lEveNotModel.setFascicoloGPTP(lFasGPTPModel);
					}
					lEventiNot.add(lEveNotModel);
				}
				lDettFascicolo.setEventi(lEventiNot);
				lTreeRoot = new TreeModel(lDettFascicolo);
			}
		} catch (DAOException ex) {
			ex.printStackTrace();
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lDepDecSqlDAO);
			cleanup(lDepOrdSqlDAO);
			cleanup(lSogSqlDao); // 21/06/2010
			cleanup(lConn);
		}

		return lTreeRoot;
	}

	/**
	 * La funzione ricerca l'Evento da trasmettere. Inserisce l'eventuale BLOB nel formato richiesto per la
	 * trasmissione.
	 * 
	 * @param aKeyEvento
	 * @return EventoModel
	 * @throws F3BException
	 */
	private EventoModel ricercaEvento(BigDecimal aKeyEvento) throws F3BException {
		// Lettura dell'Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lCtrl.ExRicercaEventoByKey(aKeyEvento);
		if (lEvento == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Evento mancante!");
		// Lettura del BLOB e suo inserimento nel formato utile al trasferimento (byte[])
		lEvento.setDocPerTrasferimento(lCtrl.ExGetDocPerTrasferimento(lEvento.getIdEvento()));
		return lEvento;
	}

}