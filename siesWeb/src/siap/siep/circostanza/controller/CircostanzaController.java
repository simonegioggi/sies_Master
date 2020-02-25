package siap.siep.circostanza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.dao.CircostanzaSqlDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.circostanza.util.CircostanzaUtil;
import siap.sige.circostanza.dao.CircostanzaSenSigeDAO;
import siap.sige.circostanza.model.CircostanzaSigeModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CircostanzaController
 * </p>
 * <p>
 * Description: Classe Controller per Circostanza
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
public class CircostanzaController extends SiapController implements ICircostanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public CircostanzaModel ExInserisciCircostanza(CircostanzaModel aCircostanza) throws F3BException {
		Connection lConn = null;
		CircostanzaDAO lCirDao = null;
		CircostanzaModel lCirMod = null;

		try {
			lConn = getDBConnection();

			lCirMod = new CircostanzaModel(aCircostanza);
			lCirDao = new CircostanzaDAO(lConn);
			lCirDao.setDAOFromModel(aCircostanza);
			BigDecimal lKey = null;
			lKey = lCirDao.insert();
			commit(lConn);

			lCirMod.setIdCircostanza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CircostanzaController.ExInserisciCircostanza: " + ex);
		} finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}

		return lCirMod;
	}

	public Vector ExRicercaCircostanza(CircostanzaModel aCircostanza) throws F3BException {
		Connection lConn = null;
		Vector lCircostanzi = new Vector();

		CircostanzaSqlDAO lCirDao = null;
		try {
			lConn = getDBConnection();

			lCirDao = new CircostanzaSqlDAO(lConn);

			lCirDao.ricercaCircostanza(aCircostanza);

			lCircostanzi = new Vector(lCirDao.getModels());

			// FEDE
			// Serena rework generale - manipola il vettore di circostanze mettendo come ultimo elemento la
			// circostanza avente Art 442 e CodFonte 25
			// lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lCircostanzi);

			if (lCircostanzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanza: Non posso leggere : " + daoEx);
		} catch (F3BException sqe) {
			throw sqe;
		} catch (Exception e) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanza: Non posso leggere  : " + e);
		}

		finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}
		return lCircostanzi;
	}

	public Vector ExRicercaCircostanzaNoErr(CircostanzaModel aCircostanza) throws F3BException {
		Connection lConn = null;
		Vector lCircostanzi = new Vector();
		CircostanzaSqlDAO lCirDao = null;
		Vector lNewVectCirc = null;
		try {
			lConn = getDBConnection();

			lCirDao = new CircostanzaSqlDAO(lConn);

			lCirDao.ricercaCircostanza(aCircostanza);

			lCircostanzi = new Vector(lCirDao.getModels());
			// Serena rework generale - manipola il vettore di circostanze mettendo come ultimo elemento la
			// circostanza avente Art 442 e CodFonte 25

			lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lCircostanzi);

		} catch (DAOException daoEx) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanza: Non posso leggere : " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanza: Non posso leggere  : " + sqe);
		} catch (Exception e) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanza: Non posso leggere  : " + e);
		}

		finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}

		return lNewVectCirc;
	}

	public CircostanzaModel ExRicercaCircostanzaByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		CircostanzaSqlDAO lCirDao = null;
		CircostanzaModel lCirMod;

		try {
			lConn = getDBConnection();

			lCirDao = new CircostanzaSqlDAO(lConn);

			lCirDao.ricercaCircostanzaByKey(aKey);

			lCirMod = (CircostanzaModel) lCirDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaByKey: " + daoEx);
		} finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}

		return lCirMod;
	}

	public Vector ExRicercaCircostanzaByIdFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		Vector lCircostanze = new Vector();
		Vector lNewVectCirc = null;

		CircostanzaSqlDAO lCirDao = null;

		try {
			lConn = getDBConnection();
			lCirDao = new CircostanzaSqlDAO(lConn);
			lCirDao.ricercaCircostanzeByIdFascicolo(aKey);
			lCircostanze = new Vector(lCirDao.getModels());
			// Serena rework generale - manipola il vettore di circostanze mettendo come ultimo elemento la
			// circostanza avente Art 442 e CodFonte 25

			lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lCircostanze);

		} catch (DAOException daoEx) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaByIdFascicolo: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaByIdFascicolo: " + sqe);
		} catch (Exception e) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaByIdFascicolo: " + e);
		}

		finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}

		return lNewVectCirc;
	}

	public Vector ExRicercaCircostanzaDescByIdFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		Vector lCircostanze = new Vector();

		CircostanzaSqlDAO lCirDao = null;
		Vector lNewVectCirc = null;
		try {
			lConn = getDBConnection();
			lCirDao = new CircostanzaSqlDAO(lConn);
			lCirDao.ricercaCircostanzeDescByIdFascicolo(aKey);
			lCircostanze = new Vector(lCirDao.getModels());
			// Serena rework generale - manipola il vettore di circostanze mettendo come ultimo elemento la
			// circostanza avente Art 442 e CodFonte 25
			lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lCircostanze);

		} catch (DAOException daoEx) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaDescByIdFascicolo: " + daoEx);
		} catch (SQLException sqe) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaDescByIdFascicolo: " + sqe);
		} catch (Exception e) {
			throw new F3BException("CircostanzaController.ExRicercaCircostanzaDescByIdFascicolo: " + e);
		}

		finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}

		return lNewVectCirc;
	}

	/*
	 * public CircostanzaModel ExModificaCircostanza (CircostanzaModel aCircostanza ) throws F3BException {
	 * Connection lConn = null; CircostanzaDAO lCirDao = null; CircostanzaModel lCirMod = new
	 * CircostanzaModel(aCircostanza);
	 * 
	 * try { lConn = getDBConnection(); lCirDao = new CircostanzaDAO(lConn);
	 * lCirDao.setDAOFromModelForUpdate(aCircostanza ); lCirDao.update(); commit(lConn); } catch (DAOException
	 * ex) { rollback(lConn); throw new F3BException("CircostanzaController.ExModifica: Non posso inserire: "
	 * + ex); } catch (SQLException sqe) { rollback(lConn); throw new
	 * F3BException("CircostanzaController.ExModificaCircostanza: Non posso inserire il soggetti : " + sqe); }
	 * finally { cleanup(lCirDao); cleanup(lConn); }
	 * 
	 * return lCirMod; }
	 */
	// public CircostanzaModel ExModificaCircostanza (CircostanzaModel aCircostanza )
	public CircostanzaModel ExModificaCircostanza(CircostanzaModel aCircostanza, boolean flagAgg,
			String flagGiudizio, String flagSentenza, String codBil, String noteBil,
			BigDecimal idFascicoloSiep, BigDecimal aIdFascicoloSentenzaSige) throws F3BException {
		Connection lConn = null;
		CircostanzaDAO lCirDao = null;
		CircostanzaSqlDAO lCirSqlDao = null;
		CircostanzaModel lCirMod = new CircostanzaModel(aCircostanza);

		try {
			lConn = getDBTransaction();
			lCirDao = new CircostanzaDAO(lConn);
			lCirDao.setDAOFromModelForUpdate(aCircostanza);

			lCirDao.update();
			lCirDao.stop();

			// procedo con l'eventuale aggiornamento dei campi comuni
			// su tutte le circostanze del fascicolo
			if (flagAgg) {
				// ***********************************************************************************
				// Federica - a9-rr-078
				/*
				 * boolean flagCircBil = false; lCirSqlDao = new CircostanzaSqlDAO(lConn);
				 * lCirSqlDao.ricercaCircostanzeByIdFascicoloByArtByCodFonte(idFascicolo, "442", "25");
				 * CircostanzaModel lCirMod2 = new CircostanzaModel(); lCirMod2 =
				 * (CircostanzaModel)lCirSqlDao.getModelByKey(); if(lCirMod2!=null)flagCircBil = true; //se il
				 * flagGiudizio è "S" e non ci sono circostanze art 442cpp la inserisco
				 * if(flagGiudizio.equalsIgnoreCase("S")&& flagCircBil==false){ CircostanzaModel lCirMod3 =
				 * new CircostanzaModel(); lCirMod3.setArticolo("442"); lCirMod3.setCodFonte("25");
				 * lCirMod3.setCodOperatoreInserimento(aCircostanza.getCodOperatoreInserimento());
				 * lCirMod3.setCodUfficioInserimento(aCircostanza.getCodUfficioInserimento());
				 * lCirMod3.setDataInserimento(DateUtils.getSysDate());
				 * lCirMod3.setFasSieIdFascicoloSiep(aCircostanza.getFasSieIdFascicoloSiep());
				 * lCirMod3.setCodBilanciamentoCircostanze("-"); lCirMod3.setCodSottonumerazione("-");
				 * //*************************************** //Federica - a9-rr-078 //aggiunto campo
				 * Comma-Qualificante lCirMod3.setCommaQualificante("-");
				 * //***************************************
				 * 
				 * lCirDao.setDAOFromModel(lCirMod3); lCirDao.insert(); lCirDao.stop(); } //se il flagGiudizio
				 * è "N" e ci sono circostanze art 442cpp le elimino else
				 * if(flagGiudizio.equalsIgnoreCase("N")&& flagCircBil){
				 * lCirDao.setCondizioneCircBilanciamento(aCircostanza.getFasSieIdFascicoloSiep());
				 * lCirDao.delete(); lCirDao.stop(); }
				 * 
				 * //aggiornamento campi comuni // lCirDao.setFlagGiudizioAbbreviato(flagGiudizio); //
				 * lCirDao.setFlagSentenzaApplicazPena(flagSentenza); // fine modifica
				 */

				lCirDao.setCodBilanciamentoCircostanze(codBil);
				lCirDao.setNoteBilanciamento(noteBil);
				if (aIdFascicoloSentenzaSige != null)
					lCirDao.setCondizioneUpdateFascicoloSige(aIdFascicoloSentenzaSige);
				else
					lCirDao.setCondizioneUpdateFascicolo(idFascicoloSiep);
				lCirDao.update();
				lCirDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CircostanzaController.ExModificaCircostanza: Non posso inserire: " + ex);
		} finally {
			cleanup(lCirDao);
			cleanup(lCirSqlDao);
			cleanup(lConn);
		}

		return lCirMod;
	}

	public void ExCancellaCircostanza(CircostanzaModel aCircostanza) throws F3BException {
		Connection lConn = null;
		CircostanzaDAO lCirDao = null;
		CircostanzaSqlDAO lCirSqlDao = null;

		try {
			lConn = getDBConnection();

			lCirDao = new CircostanzaDAO(lConn);
			lCirSqlDao = new CircostanzaSqlDAO(lConn);

			lCirSqlDao.ricercaCircostanzaByKey(aCircostanza.getIdCircostanza());
			CircostanzaModel aCirc = (CircostanzaModel) lCirSqlDao.getModelByKey();

			lCirDao.setCondizioneUpdate(aCirc.getIdCircostanza());
			lCirDao.setDAOFromModel(aCirc);

			// **************************************************************
			// Federica - a9-rr-078
			// String flagGiudizio= "N";
			String flagGiudizio = aCirc.getFlagGiudizioAbbreviato();
			// **************************************************************

			String flagSentenza = aCirc.getFlagSentenzaApplicazPena();
			String codBil = aCirc.getCodBilanciamentoCircostanze();
			String noteBil = aCirc.getNoteBilanciamento();
			BigDecimal idFascicolo = aCirc.getFasSieIdFascicoloSiep();

			boolean aggiornamento = false;
			if (aCirc.getArticolo().equals("442") && aCirc.getCodFonte().equals("25"))
				flagGiudizio = "N";
			aggiornamento = true;

			// **************************************************************
			// Federica - a9-rr-078
			if (aCirc.getArticolo().equals("444") && aCirc.getCodFonte().equals("25"))
				flagSentenza = "N";
			aggiornamento = true;
			// **************************************************************

			// Circostanza SIGE
			if (aCircostanza instanceof siap.sige.circostanza.model.CircostanzaSigeModel) {
				// Cancellazione record di relazione in CIRCOSTANZA_SENTENZA_SIGE
				CircostanzaSenSigeDAO lCircostanzaSigeDAO = new CircostanzaSenSigeDAO(lConn);
				lCircostanzaSigeDAO.setCondizioneDeleteCircostanza(aCircostanza.getIdCircostanza());
				lCircostanzaSigeDAO.delete();
				lCircostanzaSigeDAO.stop();
				cleanup(lCircostanzaSigeDAO);
			}

			// Cancellazione CIRCOSTANZA
			lCirDao.delete();
			lCirDao.stop();

			// se la circostanza che viene eliminata è art. 442 del Cpp
			// cambio il flagGiudizio su tutte le restanti circostanze del fascicolo
			if (aggiornamento) {
				lCirDao.setFlagGiudizioAbbreviato(flagGiudizio);
				lCirDao.setFlagSentenzaApplicazPena(flagSentenza);
				lCirDao.setCodBilanciamentoCircostanze(codBil);
				lCirDao.setNoteBilanciamento(noteBil);
				lCirDao.setCondizioneUpdateFascicolo(idFascicolo);
				lCirDao.update();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("CircostanzaController.ExCancellaCircostanza: " + daoEx);
		} finally {
			cleanup(lCirDao);
			cleanup(lCirSqlDao);
			cleanup(lConn);
		}
	}

	/**
	 * Il metodo inserisce una circostanza nella tabella "CIRCOSTANZE" Vengono passati i paramentri per
	 * effettuare l'aggiornamento dei campi comuni a tutte le circostanze del fascicolo
	 * 
	 * Implementato per la a7-rr-165 (Inserimento Info Accessorie per Bilanciamento Circostanze) Nel caso di
	 * Circostanze SIGE vengono anche inseriti i record nella tabella di relazione CIRCOSTANZA_SENTENZA_SIGE.
	 * 
	 * @param Vector
	 *            aCircostanze
	 * @param boolean aggiornamento
	 * @param boolean flagGiudizio
	 * @param boolean flagSentenza
	 * @param String
	 *            codBil
	 * @param String
	 *            noteBil
	 * @param BigDecimal
	 *            idFascicoloSiep
	 * @param BigDecimal
	 *            aIdFascicoloSentenzaSige
	 * 
	 */
	public void ExInserisciCircostanze(Vector aCircostanze, boolean aggiornamento, String flagGiudizio,
			String flagSentenza, String codBil, String noteBil, BigDecimal aIdFascicoloSiep,
			BigDecimal aIdFascicoloSentenzaSige) throws F3BException {
		Connection lConn = null;

		CircostanzaDAO lCirDao = null;
		CircostanzaModel lCirMod = null;
		CircostanzaSenSigeDAO lCirSigeDao = null;

		// Flag inserimento di Circostanze per Sentenza SIGE
		boolean lCirSige = false;
		if (aIdFascicoloSiep == null && aIdFascicoloSentenzaSige != null)
			lCirSige = true;

		try {
			lConn = getDBTransaction();

			lCirDao = new CircostanzaDAO(lConn);
			if (lCirSige)
				lCirSigeDao = new CircostanzaSenSigeDAO(lConn);

			for (int i = 0; i < aCircostanze.size(); i++) {
				lCirMod = new CircostanzaModel((CircostanzaModel) aCircostanze.get(i));
				lCirDao.setDAOFromModel(lCirMod);
				BigDecimal lKey = null;
				lKey = lCirDao.insert();
				lCirDao.stop();

				// Inserimento record di relazione in CIRCOSTANZA_SENTENZA_SIGE
				if (lCirSige) {
					CircostanzaSigeModel lCircostanzaSige = new CircostanzaSigeModel(lKey,
							aIdFascicoloSentenzaSige);
					lCirSigeDao.setDAOFromModel(lCircostanzaSige);
					lCirSigeDao.insert();
					lCirSigeDao.stop();
				}

			}
			// procedo con l'eventuale aggiornamento dei campi comuni
			if (aggiornamento) {
				lCirDao.setFlagGiudizioAbbreviato(flagGiudizio);
				lCirDao.setFlagSentenzaApplicazPena(flagSentenza);
				lCirDao.setCodBilanciamentoCircostanze(codBil);
				lCirDao.setNoteBilanciamento(noteBil);
				if (lCirSige)
					lCirDao.setCondizioneUpdateFascicoloSige(aIdFascicoloSentenzaSige);
				else
					lCirDao.setCondizioneUpdateFascicolo(aIdFascicoloSiep);
				lCirDao.update();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("CircostanzaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua l'inserimento di un Elenco di CIRCOSTANZA <br>
	 *
	 * @param aCircostanze
	 *            - Elenco Circostanze
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciCircostanzaWithoutSequence(ArrayList aCircostanze, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		CircostanzaDAO lCirDao = null;
		CircostanzaModel lCirMod = null;
		try {
			lCirDao = new CircostanzaDAO(lConn);

			if (aCircostanze != null && aCircostanze.size() > 0) {
				for (int i = 0; i < aCircostanze.size(); i++) {
					lCirMod = (CircostanzaModel) aCircostanze.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info(" Misura Alternativa da inserire = " + lCirMod.toString());
					if (lCirMod != null && lCirMod.getIdCircostanza() != null) {
						lCirDao.setDAOFromModel(lCirMod);
						lCirDao.setWithoutSequence(true);
						lCirDao.insert();
						lCirDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Circostanza! ");
			}
		} finally {
			cleanup(lCirDao);
		}
		return lCodEsito;
	}

}