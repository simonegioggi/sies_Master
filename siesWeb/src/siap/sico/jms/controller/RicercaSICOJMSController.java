package siap.sico.jms.controller;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.jms.jmscode.dao.JmsCodeSqlDAO;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.messaggio.model.RootJMSModel;
import siap.jms.util.ParserMessage;
import siap.sico.SICOException;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: RicercaSICOJMSController
 * </p>
 * <p>
 * Description: Classe che ricerca gli elementi di Sico ricercati tramite JMS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RicercaSICOJMSController extends SiapController implements IRicercaSICOJMS, ICostantiSicoJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Spedisci la Richiesta della ricerca
	 * 
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExSpedisciRichiestaRicerca(GenericModel aModel) throws F3BException {
		TreeModel lTreeRoot = null;
		if (aModel instanceof SoggettoModel) {
			lTreeRoot = new TreeModel(createRoot(1));
			TreeModel lTreeFasMod = new TreeModel(aModel);
			lTreeRoot.add(lTreeFasMod);
		}
		MessaggioModel lMessage = new MessaggioModel();
		lMessage.setTreeModel(lTreeRoot);

		return lMessage;
	}

	/**
	 * Ricerca Soggetto
	 * 
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaSoggetto(SoggettoModel aModel, String CodBDIMittente) throws F3BException {
		return ExRicercaSoggetto(aModel, CodBDIMittente, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see siap.sico.jms.controller.IRicercaSICOJMS#ExRicercaSoggetto(siap.sico.soggetto.model.SoggettoModel,
	 * java.lang.String, boolean)
	 */
	public MessaggioModel ExRicercaSoggetto(SoggettoModel aModel, String CodBDIMittente, boolean checkMin_Maj)
			throws F3BException {
		Connection lConn = null;
		Vector lSoggetti = null;
		MessaggioModel lMessage = new MessaggioModel();
		SoggettoModel lSogMod = new SoggettoModel();
		Vector VectDettFascMod = null;
		SoggettoSqlDAO lSogSqlDao = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("RicercaSICOJMSController.ExRicercaSoggetto: entrata nel metodo ");

		try {
			lConn = getDBConnection();

			lSogSqlDao = new SoggettoSqlDAO(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Soggetto model --->" + aModel);

			lSogSqlDao.ricercaSoggettoAltreBDI(aModel, checkMin_Maj);

			lSoggetti = new Vector(lSogSqlDao.getModels());

			TreeModel lTreeRoot = null;

			if (lSoggetti.isEmpty()) {
				lTreeRoot = new TreeModel(createRoot(0));
				lMessage.setCodEsito(NON_TROVATO);
			} else // I Soggetti sono stati trovati
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Soggetto trovato = " + lSoggetti.firstElement());
				lTreeRoot = new TreeModel(createRoot(1));
				Iterator lItxSogg = lSoggetti.iterator();
				TreeModel lTreeSogMod = null;

				boolean trovatoFascicoloValidato = false;
				while (lItxSogg.hasNext()) {

					lSogMod = (SoggettoModel) lItxSogg.next();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("chiama select del fascicolo NELL METODO RICERCA SOGGETTO------->");

					VectDettFascMod = ExRicercaFascicoloSiepSoggettoPerTrasferimento(lSogMod, CodBDIMittente);

					if (VectDettFascMod != null && !(VectDettFascMod.isEmpty())) {
						lSogMod.setDettaglioFascicoli(VectDettFascMod);
						lSogMod.setFlagPresenzaFascicolo("S");
						lTreeSogMod = new TreeModel(lSogMod);
						lTreeRoot.add(lTreeSogMod);
						lMessage.setCodEsito(TROVATO);
						trovatoFascicoloValidato = true;
					}
				}

				if (!trovatoFascicoloValidato) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("JMS - Trovato fascicolo NON validato");
					lTreeRoot = new TreeModel(createRoot(0));
					lMessage.setCodEsito(NON_TROVATO);
				}
			}

			lMessage.setTreeModel(lTreeRoot);
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			throw new SICOException(SICOException.USER_MESSAGE,
					"RicercaSICOJMSController.ExRicercaSoggetto: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			throw new SICOException(SICOException.USER_MESSAGE,
					"RicercaSICOJMSController.ExRicercaSoggetto: " + sqe);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}
		return lMessage;
	}

	/**
	 * Crea la Root della Ricerca Soggetto
	 * 
	 * @param caseSwitch
	 * @return
	 */
	private RootJMSModel createRoot(int caseSwitch) {
		RootJMSModel aModel = new RootJMSModel();

		switch (caseSwitch) {
		case 0: // Elemento NON TROVATO
			aModel.setCodTipoMessaggio(ESITO_RICERCA);
			aModel.setCodTipoOperazione(RICERCA_SOGGETTO);
			aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
			aModel.setDescrTipoOperazione("RICERCA SOGGETTO");
			aModel.setEsito(NON_TROVATO);
			break;

		case 1: // Soggetto
			aModel.setCodTipoMessaggio(ESITO_RICERCA);
			aModel.setCodTipoOperazione(ESITO_RICERCA_SOGGETTO);
			aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
			aModel.setDescrTipoOperazione("RICERCA SOGGETTO");
			aModel.setEsito(TROVATO);
			break;
		case 2: // Soggetto Completo
			aModel.setCodTipoMessaggio(ESITO_RICERCA);
			aModel.setCodTipoOperazione("00110");
			aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
			aModel.setDescrTipoOperazione("RICERCA SOGGETTO COMPLETA");
			aModel.setEsito(TROVATO);
			break;

		}
		return aModel;
	}

	/**
	 * ExRicercaFascicoloSiepSoggettoPerTrasferimento
	 * 
	 * @param aModel
	 * @return Vettore di fascicolo Siep associati al soggetto
	 * @throws F3BException
	 */
	private Vector ExRicercaFascicoloSiepSoggettoPerTrasferimento(SoggettoModel aModel, String CodBDIMittente)
			throws F3BException {
		// 07/02/2008 Rework con chiamata al metodo ExRicercaFascicoloSiepPerTrasferimento.
		Connection lConn = null;
		FascicoloSiepSqlDAO lFasSql = null;
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		FascicoloSiepModel lFascModel = new FascicoloSiepModel();

		Vector lFascicoli = null;
		Vector lDettagliFascicoli = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SOGGETTOMODEL----->" + aModel);
			lConn = getDBConnection();
			lFasSql = new FascicoloSiepSqlDAO(lConn);
			lFasMod.setSogIdSoggetto(aModel.getIdSoggetto());

			lFasSql.ricercaFascicoloBySoggettoValidato(aModel.getIdSoggetto());
			lFascicoli = new Vector(lFasSql.getModels());

			lDettagliFascicoli = new Vector();

			if (lFascicoli.size() > 0) {
				DettaglioFascicoloModel lDettFascicolo = new DettaglioFascicoloModel();
				Iterator lItxFasc = lFascicoli.iterator();
				while (lItxFasc.hasNext()) {
					lFascModel = (FascicoloSiepModel) lItxFasc.next();

					// 03/02/2009 Solo se la BDI mittente non coincide con quella destinataria, posso
					// aggiungere il fascicolo.
					IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
					UfficioModel lUffFascicolo = new UfficioModel();
					lUffFascicolo = lCtrlUff.getUfficioByKey(lFascModel.getChiaveUfficio());
					if (lUffFascicolo.getCodDistretto().trim().compareTo(CodBDIMittente) != 0) {
						IRicercaJMS lRicCtrl = SIEPLookupRemote.getRicercaJMS();
						MessaggioModel lMessage = lRicCtrl.ExRicercaFascicoloSiepPerTrasferimento(lFascModel);

						// Cerco il nodo DettaglioFascicoloSIEP
						if (lMessage != null && lMessage.getTreeModel() != null) {
							ParserMessage lPars = new ParserMessage(lMessage.getTreeModel());
							lDettFascicolo = lPars.getDettaglioFascicoloSiep();
							lDettagliFascicoli.add(lDettFascicolo);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SICOException(SICOException.USER_MESSAGE,
					"RicercaSICOJMSController.ExRicercaFascicoloSiepSoggettoPerTrasferimento: " + e);
		} finally {
			cleanup(lFasSql);

			cleanup(lConn);
		}
		return lDettagliFascicoli;
	}

	/**
	 * ExRicercaAllBDI
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAllBDI() throws F3BException {
		Connection lConn = null;
		Vector lJmsCodi = new Vector();
		JmsCodeSqlDAO lJmsDao = null;

		try {
			lConn = getDBConnection();
			lJmsDao = new JmsCodeSqlDAO(lConn);
			lJmsDao.ricercaAllBDI();
			lJmsCodi = new Vector(lJmsDao.getModels());
			if (lJmsCodi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RicercaSICOJMSController.ExRicercaAllBDI: " + daoEx);
		} finally {
			cleanup(lJmsDao);
			cleanup(lConn);
		}
		return lJmsCodi;
	}

}