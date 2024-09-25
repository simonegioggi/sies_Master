package siap.sico.decodifiche.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.decodifiche.dao.DecodificheDAO;
import siap.sico.decodifiche.dao.DecodificheSqlDAO;
import siap.sico.decodifiche.model.DecodificheModel;
// STUB 11/11/2003 Integrazione Dettaglio Oggetti.
import siap.sico.decodifiche.model.OggettiModel;

/**
 * Title: DecodificheController
 * Description: classe per la gestione delle decodifiche
 * Copyright: Copyright (c) 2002
 * Company: EII
 *
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecodificheController extends SiapController implements IDecodifiche {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private static final String NET = "Nessun Elemento trovato";
	private static final String DAOEXC = "DAOException: ";
	private static final String OGGETTO_SOSPENSIONI = "OGGETTO_SOSPENSIONI";
	private static final String EXC = "Exception: ";
	private static final String SQLEXC = "SQLException: ";
	private static final String COD_OGGETTO = "COD_OGGETTO";
	private static final String DESC_OGGETTO = "DESC_OGGETTO";
	private static final String COD_CONTENUTO = "COD_CONTENUTO";
	private static final String DESC_CONTENUTO = "DESC_CONTENUTO";
	private static final String COD_DETTAGLIO = "COD_DETTAGLIO";
	private static final String DESC_DETTAGLIO = "DESC_DETTAGLIO";
	private static final String ABBR_OGGETTO = "ABBR_OGGETTO";
	private static final String MOTIVO_INAMMISSIBILITA = "MOTIVO_INAMMISSIBILITA";
	private static final String NUM_MOT = "N.ro Motivi : ";
	private static final String MSG_INI = "L'operazione non puo' continuare perche' l'oggetto selezionato (cod: ";
	private static final String MSG_FIN = " ) non prevede esiti !";
	private static final String MSG_MOT = "N.ro Motivi dopo il filtraggio : ";

	public Collection ExRicercaDecodifiche(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("DecodificheController.ExRicercaDecodifiche: Connessione dal pool : "
			// + lConn);
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodifiche: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public DecodificheModel ExRicercaDecodificheByAbbByHigh(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;

		DecodificheDAO lDecDao = null;
		DecodificheModel lDecMod = new DecodificheModel();

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecMod = (DecodificheModel) lDecDao.getModelByKey();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheByAbbByHigh: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecMod;
	}

	public Collection ExRicercaDecodificheRwLowValue(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioneContestoRwLowValue(aModel);
			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheRwLowValue: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public DecodificheModel ExRicercaDecodificheProvvAnnMan(String aCodice) throws F3BException {

		Connection lConn = null;
		DecodificheModel lDecodifiche = new DecodificheModel();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheSqlDAO(lConn);
			lDecDao.ricercaProvvAnnMan(aCodice);
			// lDecDao.start();
			lDecodifiche = ((DecodificheModel) lDecDao.getModelByKey());
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheProvvAnnMan : " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	/**
	 * Ricerca Decodifiche con descrizione in Rv_Abbreviation
	 *
	 * @param aModel
	 * @return lDecodifiche
	 * @throws F3BException
	 */
	public Collection ExRicercaDecodificheRvAbbreviation(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("DecodificheController.ExRicercaDecodificheRvAbbreviation: Connessione dal
			// pool : "
			// + lConn);
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecDao.start();

			while (lDecDao.next()) {
				DecodificheModel lDec = new DecodificheModel();

				lDec.setCode(lDecDao.getCodice());
				lDec.setDescription(lDecDao.getFiltro());

				lDecodifiche.add(lDec);
			}

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheRvAbbreviation : " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaDecodificheOrdinatePerCodice(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecDao.setOrdinamentoPerCodice();
			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheOrdinatePerCodice: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaDecodificheOrdinatePerDescrizione(DecodificheModel aModel)
			throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecDao.setOrdinamentoPerDescrizione();
			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheOrdinatePerDescrizione: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaDecodificheOrdinatePerCodiceAlternativo(DecodificheModel aModel)
			throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecDao.setOrdinamentoPerCodiceAlternativo();
			lDecDao.setOrdinamentoPerDescrizione();

			lDecDao.start();
			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheOrdinatePerCodiceAlternativo: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaDecodificheFiltroNull(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setCondizioneContestoFiltroNull(aModel.getContesto());
			lDecDao.setOrdinamentoPerDescrizione();

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheFiltroNull: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaDecodificheFiltroNullOrRvAbbreviation(DecodificheModel aModel)
			throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setCondizioneContestoFiltroNullOrRvAbbreviation(aModel.getContesto(), aModel.getFiltro());
			lDecDao.setOrdinamentoPerDescrizione();

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, NET);

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheFiltroNullOrRvAbbreviation: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public DecodificheModel ExRicercaDecodificheByHighValue(String aValue) throws F3BException {

		Connection lConn = null;
		DecodificheModel lDecodifiche = new DecodificheModel();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setCondizioneByHighValue(aValue);

			lDecodifiche = (DecodificheModel) lDecDao.getModelByKey();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheByHighValue: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaEsitiByOggetto(String lCodOggetto) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheSqlDAO(lConn);

			lDecDao.ricercaEsitiByOggetto(lCodOggetto);

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, MSG_INI + lCodOggetto + MSG_FIN);

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaEsitiByOggetto: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	public Collection ExRicercaEsitiCompatibiliByEsitoOggetto(String lCodOggetto, String lCodEsito)
			throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheSqlDAO(lConn);

			lDecDao.ricercaEsitiCompatibiliByEsitoOggetto(lCodOggetto, lCodEsito);

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, MSG_INI + lCodOggetto + MSG_FIN);

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaEsitiCompatibiliByEsitoOggetto: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	/**
	 * Ricerca il corrispondente Codice Provvedimento per il Codice Esito Tenore dato
	 *
	 * @param lCodOggetto
	 * @return lDecodifica.getCodiceAlternativo()
	 * @throws F3BException
	 */
	public String ExRicercaCodEsitiProvByCodTenore(String lCodOggetto) throws F3BException {

		Connection lConn = null;
		DecodificheModel lDecodifica = null;
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheSqlDAO(lConn);
			lDecDao.ricercaCodEsitoByEsitoTenore(lCodOggetto);
			// lDecDao.start();
			// if (lDecDao.next())
			lDecodifica = (DecodificheModel) lDecDao.getModelByKey();
			// lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaCodEsitiProvByCodTenore: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifica.getCodiceAlternativo();
	}

	/**
	 * Lista Oggetti legati al Contenuto (passato come parametro).
	 * <p>
	 *
	 * @param aContenuto
	 * @param aCodTipoUfficio
	 * @return Vettore di Oggetti
	 * @throws F3BException
	 */
	public Collection ExListaOggetti(String aContenuto, String aCodTipoUfficio) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		// OggettiModel lOggMod = new OggettiModel();
		Collection lOggetti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggetti(aContenuto, aCodTipoUfficio);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				String lCodOggetto = lDecSqlDao.getString(COD_OGGETTO);
				String lDescOggetto = lDecSqlDao.getString(DESC_OGGETTO);
				String lCodContenuto = lDecSqlDao.getString(COD_CONTENUTO);
				String lDescContenuto = lDecSqlDao.getString(DESC_CONTENUTO);
				// STUB 10/11/2003 Inserimento Dettaglio Oggetto.
				String lCodDettaglio = lDecSqlDao.getString(COD_DETTAGLIO);
				String lDescDettaglio = lDecSqlDao.getString(DESC_DETTAGLIO);
				// STUB 13/09/2004 Inserimento Abbr. Oggetto.
				String lAbbrOggetto = lDecSqlDao.getString(ABBR_OGGETTO);

				// MERGE v10: aggiunto filtro solo per contenuto 'C046' --> passano solo gli oggetti 1215 e
				// 1216
				if (!"C046".equals(lCodContenuto) || ("C046".equals(lCodContenuto)
						&& ("1215".equals(lCodOggetto) || "1216".equals(lCodOggetto))))
					lOggetti.add(new OggettiModel(lCodOggetto, lDescOggetto, lCodContenuto, lDescContenuto,
							lCodDettaglio, lDescDettaglio, lAbbrOggetto));
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaOggetti: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lOggetti;
	}

	/**
	 * Lista Contenuti per Tipo Ufficio (passato come parametro).
	 * <p>
	 *
	 * @param aCodTipoUfficio
	 *            Codice del tipo Ufficio.
	 * @return Stringa di Contenuti.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExListaContenuti(String aCodTipoUfficio) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaContenuti(aCodTipoUfficio);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				// 28/04/2006 Aggiunta del Suffisso UDS/TDS
				// lContenuti.add( (DecodificheModel) lDecSqlDao.getModel());
				DecodificheModel lContenuto = (DecodificheModel) lDecSqlDao.getModel();
				if (lContenuto.getDescription().compareTo("-") != 0) {
					if (lContenuto.getCode().indexOf("C") == 0 && "".equals(aCodTipoUfficio.trim()))
						lContenuto.setDescription(lContenuto.getDescription() + " -  ( TDS )");
					if (lContenuto.getCode().indexOf("U") == 0 && "".equals(aCodTipoUfficio.trim()))
						lContenuto.setDescription(lContenuto.getDescription() + " -  ( UDS )");
				}
				if (lContenuto.getCode().indexOf("C") == 0 || lContenuto.getCode().indexOf("U") == 0
						|| lContenuto.getCode().indexOf("-") == 0) {
					// MEV10-s3: per i minori elimino un elemento dalla lista vettoriale
					if ("TDSM".equals(aCodTipoUfficio) || "UDSM".equals(aCodTipoUfficio)) {
						// MERGE v10 COLLAUDO: per i minori continuo nello scorrimento della lista per i
						// seguenti codici
						// MEV63: il contenuto UO43 DEVE ESSERE VISIBILE AGLI UFFICI UDSM
						if ("C015".equals(lContenuto.getCode()) || "C016".equals(lContenuto.getCode())
								|| "U045".equals(lContenuto.getCode()) || "U051".equals(lContenuto.getCode())
								|| "U052".equals(lContenuto.getCode()) || "U054".equals(lContenuto.getCode())
								|| "U066".equals(lContenuto.getCode()) || "U067".equals(lContenuto.getCode())
								|| "U093".equals(lContenuto.getCode()))
							continue;
						// MEV_9: aggiunto contenuto solo x TDS
						if ("TDSM".equals(aCodTipoUfficio) && "C050".equals(lContenuto.getCode()))
							continue;
					}
					// MERGE v10 COLLAUDO: per i maggiori continuo nello scorrimento della lista per i
					// seguenti codici
					if ("TDS".equals(aCodTipoUfficio) || "UDS".equals(aCodTipoUfficio)) {
						if ("C047".equals(lContenuto.getCode()) || "C048".equals(lContenuto.getCode())
								|| "U101".equals(lContenuto.getCode()) || "U102".equals(lContenuto.getCode())
								|| "U110".equals(lContenuto.getCode()) || "U112".equals(lContenuto.getCode())
								|| "U113".equals(lContenuto.getCode()) || "U114".equals(lContenuto.getCode())
								||
								// MEV_66: aggiunti 4 contenuti solo x minori
								"U120".equals(lContenuto.getCode()) || "U121".equals(lContenuto.getCode())
								|| "U122".equals(lContenuto.getCode()) || "U123".equals(lContenuto.getCode()))
							continue;
						// MEV_9: aggiunto contenuto solo x TDSM
						if ("TDS".equals(aCodTipoUfficio) && "C051".equals(lContenuto.getCode()))
							continue;
					}
					lContenuti.add(lContenuto);
				}
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("DecodificheController.ExListaContenuti: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoOS(String aOSLibAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoOS(aOSLibAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoOS: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoOSLiberazioneAnticipataMA(String aOSLibAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoOSLiberazioneAnticipataMA(aOSLibAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoOSLiberazioneAnticipataMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	// MEV_9-SIEP: cambiata firma del metodo per distinguere PM da PMM
	public Collection ExListaMotivoProvvMA(String aMisAlt, String aCodTipoUfficio) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvMA(aMisAlt, aCodTipoUfficio);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvSospProvvMA(String aMisAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvSospProvvMA(aMisAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvSospProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvSospProvvAD(String aMisAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvSospProvvArrestiDom(aMisAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvSospProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvRipristinoAD(String aMisAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvRipristinoArrestiDom(aMisAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvRipristinoMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvRevocaAD(String aMisAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvRevocaArrestiDom(aMisAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvRevocaMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoRipristinoMA(String aMisAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvRipristinoMA(aMisAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoRipristinoMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoDicEffMA(String aMisAlt) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvDicEffMA(aMisAlt);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoDicEffMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoRevocaProvvMA(String aMisAlt) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoRevocaMA(aMisAlt);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoRevocaProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	// 02/12/2010 Inizio :Aggiunto Daniela
	public Collection ExListaMotivoCessazioneProvvMA(String aMisAlt) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoCessazioneMA(aMisAlt);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoCessazioneProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	// 02/12/2010 Fine

	public Collection ExListaMotivoProvvDetDomSpeciale(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvDetDomSpeciale(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoRevocaProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvAmmProvvisoria(String aTipo) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvAmmProvvisoria(aTipo);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}
			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvAmmProvvisoria: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}
		return lContenuti;
	}

	public Collection ExListaMotivoProvvMADetDomTemp(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvMADetDomTemp(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());

				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvMADetDomTemp: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvMAReLibCond(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvMAReLibCond(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvMAReLibCond: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvMACOLibCond(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvMACOLibCond(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvMACOLibCond: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvUltPeriodoMA(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvUltPeriodoMA(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvUltPeriodoMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoMAPreEff(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoMAPreEff(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());

				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoMAPreEff: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvProrogaUltPeriodo(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvProrogaUltPeriodo(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvProrogaUltPeriodo: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvDetDomSpecialeAmmAff(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvAmmAff(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvProrogaUltPeriodo: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvRipristinoDetDomSpeciale(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvRipristinoDetDomSpec(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException(
					"DecodificheController.ExListaMotivoProvvRipristinoDetDomSpeciale: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista dei Motivi Provvedimento per il Differimneto
	 */
	public Collection ExListaMotivoProvvSospDifferimento() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvSospDifferimento();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvSospDifferimento: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista dei Motivi Provvedimento per il Differimento Provvisorio
	 */
	public Collection ExListaMotivoProvvSospDifferimentoProvv() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvSospDifferimentoProvv();
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvSospDifferimentoProvv: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista dei Motivi Provvedimento per il Differimento Definitivo
	 */
	public Collection ExListaMotivoProvvSospDifferimentoDef() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvSospDifferimentoDef();
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvSospDifferimentoDef: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista dei Motivi della Revoca Differimento
	 */
	public Collection ExListaMotivoProvvRevocaDifferimento() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvRevocaDifferimento();
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvRevocaDifferimento: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista dei Motivi della Rigetto Differimento
	 */
	public Collection ExListaMotivoProvvRigettoDifferimento() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvRigettoDifferimento();
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvRevocaDifferimento: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Lista Tipi Decret0.
	 * <p>
	 *
	 * @return Collection di model Decodifiche.
	 * @throws F3BException
	 */
	public Collection ExListaTipoDecreto() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lTipi = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaTipoDecreto();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				lTipi.add(lDecSqlDao.getModel());
			}
			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaTipiDecreto: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lTipi;
	}

	/**
	 * Elenco dei tipi uffici cumulo, ordinati per la Descrizione ( RV_MEANING ) e filtrati per i valori di
	 * RV_HIGH_VALUE pari a ( T,S e C ).
	 * <p>
	 *
	 * @return Collection di model Decodifiche.
	 * @throws F3BException
	 */
	public Collection ExListaTipiUfficioCumuloRifSiep() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lTipiUfficioCumulo = new ArrayList();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaTipoUfficioCumuloRifSiep();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecModel = new DecodificheModel();
				lDecModel.setCode(lDecSqlDao.getModelRvLowValue());
				lDecModel.setDescription(lDecSqlDao.getModelRvMeaning());
				lTipiUfficioCumulo.add(lDecModel);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("DecodificheController.ExListaTipiUfficioCumuloRifSiep : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("DecodificheController.ExListaTipiUfficioCumuloRifSiep : " + ex);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lTipiUfficioCumulo;
	}

	/**
	 * Elenco dei tipi uffici cumulo, ordinati per la Descrizione ( RV_MEANING ) e filtrati per i valori di
	 * RV_HIGH_VALUE pari a ( T,S e C ) per RV_ABBREVIATION pari a V
	 * <p>
	 *
	 * @return Collection di model Decodifiche.
	 * @throws F3BException
	 */
	public Collection ExListaTipiUfficioCumuloUfficioLoginRifSiep() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lTipiUfficioCumulo = new ArrayList();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaTipoUfficioCumuloUfficioLoginRifSiep();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecModel = new DecodificheModel();
				lDecModel.setCode(lDecSqlDao.getModelRvLowValue());
				lDecModel.setDescription(lDecSqlDao.getModelRvMeaning());
				lTipiUfficioCumulo.add(lDecModel);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"DecodificheController.ExListaTipiUfficioCumuloUfficioLoginRifSiep : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException(
					"DecodificheController.ExListaTipiUfficioCumuloUfficioLoginRifSiep : " + ex);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lTipiUfficioCumulo;
	}

	/**
	 * Elenco dei tipi uffici cumulo, ordinati per la Descrizione ( RV_MEANING ) e filtrati per i valori di
	 * RV_HIGH_VALUE pari a ( T,S,C e D ).
	 * <p>
	 *
	 * @return Collection di model Decodifiche.
	 * @throws F3BException
	 */
	public Collection ExListaTipiUfficioCumuloRifMSic() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lTipiUfficioCumuloMSic = new ArrayList();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaTipoUfficioCumuloRifMSic();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecModel = new DecodificheModel();
				lDecModel.setCode(lDecSqlDao.getModelRvLowValue());
				lDecModel.setDescription(lDecSqlDao.getModelRvMeaning());
				lTipiUfficioCumuloMSic.add(lDecModel);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("DecodificheController.ExListaTipiUfficioCumuloRifMSic : " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("DecodificheController.ExListaTipiUfficioCumuloRifMSic : " + ex);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lTipiUfficioCumuloMSic;
	}

	/**
	 * Lista Oggetti ordinati per Contenuto (Prima quelli del Contenuto passato come parametro).
	 * <p>
	 *
	 * @param aContenuto
	 * @param aCodTipoUfficio
	 * @return Vettore di Oggetti
	 * @throws F3BException
	 */
	public Collection ExListaOggettiCompleta(String aContenuto, String aCodTipoUfficio) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		// OggettiModel lOggMod = new OggettiModel();
		Collection lOggetti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggetti(aContenuto, aCodTipoUfficio);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				String lCodOggetto = lDecSqlDao.getString(COD_OGGETTO);
				String lDescOggetto = lDecSqlDao.getString(DESC_OGGETTO);
				String lCodContenuto = lDecSqlDao.getString(COD_CONTENUTO);
				String lDescContenuto = lDecSqlDao.getString(DESC_CONTENUTO);
				String lCodDettaglio = lDecSqlDao.getString(COD_DETTAGLIO);
				String lDescDettaglio = lDecSqlDao.getString(DESC_DETTAGLIO);
				String lAbbrOggetto = lDecSqlDao.getString(ABBR_OGGETTO);

				lOggetti.add(new OggettiModel(lCodOggetto, lDescOggetto, lCodContenuto, lDescContenuto,
						lCodDettaglio, lDescDettaglio, lAbbrOggetto));
			}

			lDecSqlDao.stop();

			// Inserimento di tutti gli oggetti di comntenuto diverso dal parametro.
			lDecSqlDao.listaOggettiDiversi(aContenuto, aCodTipoUfficio);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				String lCodOggetto = lDecSqlDao.getString(COD_OGGETTO);
				String lDescOggetto = lDecSqlDao.getString(DESC_OGGETTO);
				String lCodContenuto = lDecSqlDao.getString(COD_CONTENUTO);
				String lDescContenuto = lDecSqlDao.getString(DESC_CONTENUTO);
				String lCodDettaglio = lDecSqlDao.getString(COD_DETTAGLIO);
				String lDescDettaglio = lDecSqlDao.getString(DESC_DETTAGLIO);
				String lAbbrOggetto = lDecSqlDao.getString("ABBR_DETTAGLIO");

				lOggetti.add(new OggettiModel(lCodOggetto, lDescOggetto, lCodContenuto, lDescContenuto,
						lCodDettaglio, lDescDettaglio, lAbbrOggetto));
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaOggetti: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lOggetti;
	}

	public Collection ExListaAutoritaSospensione(Collection aTipoRegistroOrdinanza) throws F3BException {

		Connection lConn = null;

		DecodificheDAO lDecDao = null;
		Collection lListaAutoritaSospensione = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecDao.setCondizioni(new DecodificheModel("", "", "TIPO_UFFICIO_SOSP",
						lDecMod.getCodiceAlternativo(), "", "", "", "", ""));

				ArrayList lLista = new ArrayList(lDecDao.getModels());

				lListaAutoritaSospensione.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaAutoritaSospensione: " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lListaAutoritaSospensione;
	}

	public Collection ExListaOggettiSospensione(Collection aTipoRegistroOrdinanza) throws F3BException {

		Connection lConn = null;

		DecodificheDAO lDecDao = null;
		Collection lListaOggettiSospensione = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecDao.setCondizioni(new DecodificheModel("", "", OGGETTO_SOSPENSIONI, "",
						lDecMod.getCodiceAlternativo(), "", "", "", ""));

				ArrayList lLista = new ArrayList(lDecDao.getModels());

				if (lDecMod.getCodiceAlternativo().equals("0001")) {
					lLista.remove(
							new DecodificheModel("0005", "", OGGETTO_SOSPENSIONI, "", "", "", "", "", "")); // RV_ABBREVIATION
																											// =
																											// 'C025'
					lLista.remove(
							new DecodificheModel("0004", "", OGGETTO_SOSPENSIONI, "", "", "", "", "", "")); // RV_ABBREVIATION
																											// =
																											// 'C020'
					lLista.remove(
							new DecodificheModel("0023", "", OGGETTO_SOSPENSIONI, "", "", "", "", "", "")); // RV_ABBREVIATION
																											// =
																											// 'U028'
					lLista.remove(
							new DecodificheModel("0022", "", OGGETTO_SOSPENSIONI, "", "", "", "", "", "")); // RV_ABBREVIATION
																											// =
																											// 'U003'
					lLista.remove(
							new DecodificheModel("0001", "", OGGETTO_SOSPENSIONI, "", "", "", "", "", "")); // RV_ABBREVIATION
																											// =
																											// 'C005'
					lLista.remove(
							new DecodificheModel("0002", "", OGGETTO_SOSPENSIONI, "", "", "", "", "", "")); // RV_ABBREVIATION
																											// =
																											// 'C014'
				}

				lListaOggettiSospensione.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaOggettiSospensione: " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lListaOggettiSospensione;
	}

	// MEV_9-SIEP: cambiata firma del metodo per distinguere PM da PMM
	public Collection ExListaOggettiSospensioneDecisioneSor(String aCodTipoUfficio) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();
		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaOggettiSospensioneDecisioneSor(aCodTipoUfficio);
			lDecSqlDao.start();
			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}
			lDecSqlDao.stop();
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(SQLEXC + sqe);
			throw new F3BException("DecodificheController.ExListaOggettiSospensioneDecisioneSor: " + sqe);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}
		return lContenuti;
	}

	public Collection ExListaOggettiSospensioneDifferimento() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggettoSospDifferimento();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();

		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(SQLEXC + sqe);
			throw new F3BException("DecodificheController.ExListaOggettiSospensioneDifferimento: " + sqe);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista degli oggetti dei Differimenti Provvisori
	 *
	 * @throws F3BException
	 */
	public Collection ExListaOggettiSospensioneDifferimentoProvv() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggettoSospDifferimentoProvv();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(SQLEXC + sqe);
			throw new F3BException(
					"DecodificheController.ExListaOggettiSospensioneDifferimentoProvv: " + sqe);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista degli oggetti del Differimento Definitivo
	 *
	 * @throws F3BException
	 */
	public Collection ExListaOggettiSospensioneDifferimentoDef() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggettoSospDifferimentoDef();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(SQLEXC + sqe);
			throw new F3BException("DecodificheController.ExListaOggettiSospensioneDifferimentoDef: " + sqe);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce la lista degli oggetti della Revoca del Differimenti
	 *
	 * @throws F3BException
	 */
	public Collection ExListaOggettiRevocaDifferimento() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggettoRevocaDifferimento();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(SQLEXC + sqe);
			throw new F3BException("DecodificheController.ExListaOggettiRevocaDifferimento: " + sqe);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaOggettiRevoca(Collection aTipoRegistroOrdinanza) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lListaOggettiRevoca = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecSqlDao.listaOggettiRevoca(lDecMod.getCodiceAlternativo());

				ArrayList lLista = new ArrayList(lDecSqlDao.getModels());

				lListaOggettiRevoca.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaOggettiRevoca: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lListaOggettiRevoca;
	}

	public Collection ExListaMotiviProvvedimentoSospensione(Collection aTipoRegistroOrdinanza)
			throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lListaMotiviProvvedimentoSospensione = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecSqlDao.listaMotiviProvvedimentiSospensione(lDecMod.getCodiceAlternativo());

				ArrayList lLista = new ArrayList(lDecSqlDao.getModels());

				lListaMotiviProvvedimentoSospensione.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotiviProvvedimentoSospensione: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lListaMotiviProvvedimentoSospensione;
	}

	public Collection ExListaMotiviProvvedimentoRevoca(Collection aTipoRegistroOrdinanza)
			throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lListaMotiviProvvedimentoRevoca = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecSqlDao.listaMotiviProvvedimentiRevoca(lDecMod.getCodiceAlternativo());

				ArrayList lLista = new ArrayList(lDecSqlDao.getModels());

				lListaMotiviProvvedimentoRevoca.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotiviProvvedimentoRevoca: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lListaMotiviProvvedimentoRevoca;
	}

	public Collection ExListaEsitiTenoreSospensione(Collection aTipoRegistroOrdinanza) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lListaEsitiTenoreSospensione = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecSqlDao.listaEsitiTenoreSospensione(lDecMod.getCodiceAlternativo());

				ArrayList lLista = new ArrayList(lDecSqlDao.getModels());

				lListaEsitiTenoreSospensione.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaEsitiTenoreSospensione: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lListaEsitiTenoreSospensione;
	}

	public Collection ExListaEsitiTenoreRevoca(Collection aTipoRegistroOrdinanza) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;
		Collection lListaEsitiTenoreRevoca = new ArrayList();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			Iterator lIter = aTipoRegistroOrdinanza.iterator();

			while (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();

				lDecSqlDao.listaEsitiTenoreRevoca(lDecMod.getCodiceAlternativo());

				ArrayList lLista = new ArrayList(lDecSqlDao.getModels());

				lListaEsitiTenoreRevoca.add(lLista);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaEsitiTenoreRevoca: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lListaEsitiTenoreRevoca;
	}

	public Collection ExListaAutoritaSospTDSUDS() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaAutoritaSospTDSUDS();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvHighValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaAutoritaSospTDSUDS: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Recupera le descrizioni degli oggetti previsti della Sorveglianza (MDS,TDS) introdotti a seguito delle
	 * modifiche del DL 146/2013 all'articolo 51bis del O.P.
	 *
	 * @param aTipo
	 * @return
	 * @throws F3BException
	 */
	public Collection ExListaMotivoProvvedimentoProsecMA51Bis(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvedimentoProsecMA51Bis(aTipo);

			lDecSqlDao.start();
			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());

				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(SQLEXC, sqe);
			throw new F3BException("DecodificheController.ExListaMotivoProvvedimentoProsecMA51Bis: " + sqe);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvedimentoProsecProvvMA(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvedimentoProsecProvvMA(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());

				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvedimentoProsecProvvMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvedimentoEstDefMA(String aTipo) throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvedimentoEstDefMA(aTipo);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());

				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvedimentoEstDefMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	public Collection ExListaMotivoProvvedimentoEspulsione(String aTipo) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.listaMotivoProvvedimentoEspulsione(aTipo);
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvedimentoEspulsione: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}
		return lContenuti;
	}

	public Collection ExListaMotivoProvvedimentoRigettoMA() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvedimentoRigettoMA();
			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());

				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExListaMotivoProvvedimentoRigettoMA: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * La funzione ritorna l'elenco dei Motivi di Inammissibilita' dalla CG_REF_CODES. Il criterio per la
	 * costruzione della lista e' la seguente: Vengono selezionati i records con dominio
	 * MOTIVO_INAMMISSIBILITA per i quali il campo RV_HIGH_VALUE risulti valorizzato ed il campo
	 * RV_ABBREVIATION sia o nullo oppure valorizzato con il codice aCodTipoUff, passato come parametro.
	 *
	 * @param aTipoUff
	 * @return Vector
	 * @throws Exception
	 */
	public Collection ExListaMotiviInammissibilita(String aTipoUff) throws Exception {

		DecodificheModel lDecod = new DecodificheModel();
		lDecod.setContesto(MOTIVO_INAMMISSIBILITA);
		Set<String> motivazioniMinorenni = new HashSet<>();
		boolean flagMinorenni = this.isUfficioMinorenni(aTipoUff);
		if (flagMinorenni) {
			motivazioniMinorenni.add("01");
			motivazioniMinorenni.add("02");
			motivazioniMinorenni.add("03");
			motivazioniMinorenni.add("04");
			motivazioniMinorenni.add("06");
			motivazioniMinorenni.add("07");
			motivazioniMinorenni.add("08");
			motivazioniMinorenni.add("09");
			motivazioniMinorenni.add("10");
			motivazioniMinorenni.add("11");
			motivazioniMinorenni.add("12");
			motivazioniMinorenni.add("13");
			motivazioniMinorenni.add("19");
			motivazioniMinorenni.add("27");
			motivazioniMinorenni.add("33");
			motivazioniMinorenni.add("35");
		}

		Vector lVect = new Vector(ExRicercaDecodificheOrdinatePerCodiceAlternativo(lDecod));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(NUM_MOT + lVect.size());
		if (!lVect.isEmpty()) {
			Iterator lMotivCorr = lVect.iterator();
			while (lMotivCorr.hasNext()) {
				lDecod = (DecodificheModel) lMotivCorr.next();
				if (lDecod.getCode().startsWith("C") || lDecod.getCodiceAlternativo() == null
						|| lDecod.getCodiceAlternativo().trim().length() < 1
						|| (lDecod.getFiltro() != null && lDecod.getFiltro().trim().length() > 0
								&& lDecod.getFiltro().compareToIgnoreCase(aTipoUff) != 0)) {
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Rimosso : " + lDecod.getCode());
					lMotivCorr.remove();
					continue;
				}

				if (flagMinorenni && !motivazioniMinorenni.contains(lDecod.getCode())) {
						lMotivCorr.remove();
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(MSG_MOT + lVect.size());
		}
		return lVect;
	}

	/**
	 * La funzione ritorna l'elenco dei Motivi di Inammissibilita' dalla CG_REF_CODES. Il criterio per la
	 * costruzione della lista e' la seguente: Vengono selezionati i records con dominio
	 * MOTIVO_INAMMISSIBILITA+aTipoUff per i quali il campo RV_HIGH_VALUE risulti valorizzato ed il campo
	 * RV_ABBREVIATION sia o nullo oppure valorizzato con il codice aCodTipoUff, passato come parametro.
	 *
	 * @param aTipoUff
	 * @return Vector
	 * @throws Exception
	 */
	public Collection ExListaMotiviInammissibilitaxSottoSistema(String aTipoUff, String aSottoSistema)
			throws Exception {

		DecodificheModel lDecod = new DecodificheModel();
		lDecod.setContesto("MOTIVO_INAMMISSIBILITA_" + aSottoSistema);
		Vector lVect = new Vector(ExRicercaDecodificheOrdinatePerCodiceAlternativo(lDecod));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(NUM_MOT + lVect.size());
		return lVect;
	}

	/**
	 * La funzione ritorna l'elenco dei Motivi di Inammissibilita' dalla CG_REF_CODES. Il criterio per la
	 * costruzione della lista e' la seguente: Vengono selezionati i records con dominio
	 * MOTIVO_INAMMISSIBILITA per i quali il campo RV_HIGH_VALUE risulti valorizzato ed il campo
	 * RV_ABBREVIATION sia valorizzato a CPP.
	 *
	 * @param aTipoUff
	 * @return Vector
	 * @throws Exception
	 */

	public Collection ExListaMotiviInammissibilitaCPP(String aTipoUff) throws Exception {

		DecodificheModel lDecod = new DecodificheModel();
		lDecod.setContesto(MOTIVO_INAMMISSIBILITA);
		Vector lVect = new Vector(ExRicercaDecodificheOrdinatePerCodiceAlternativo(lDecod));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(NUM_MOT + lVect.size());
		if (!lVect.isEmpty()) {
			Iterator lMotivCorr = lVect.iterator();
			while (lMotivCorr.hasNext()) {
				lDecod = (DecodificheModel) lMotivCorr.next();
				if (lDecod.getCodiceAlternativo() != null
						&& (lDecod.getFiltro() != null && lDecod.getFiltro().compareToIgnoreCase("CPP") == 0))
					siesLogger.debug("lDecod.getFiltro = CPP");
				else {
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Rimosso : " + lDecod.getCode());
					lMotivCorr.remove();
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(MSG_MOT + lVect.size());
		}
		return lVect;
	}

	/**
	 * La funzione ritorna l'elenco dei Motivi di Inammissibilita' dalla CG_REF_CODES. Il criterio per la
	 * costruzione della lista e' la seguente: Vengono selezionati i records con dominio
	 * MOTIVO_INAMMISSIBILITA per i quali il campo RV_HIGH_VALUE risulti valorizzato ed il campo
	 * RV_ABBREVIATION sia valorizzato a RD.
	 *
	 * @param aTipoUff
	 * @return Vector
	 * @throws Exception
	 */

	public Collection ExListaMotiviInammissibilitaRD(String aTipoUff) throws Exception {

		DecodificheModel lDecod = new DecodificheModel();
		lDecod.setContesto(MOTIVO_INAMMISSIBILITA);
		Vector lVect = new Vector(ExRicercaDecodificheOrdinatePerCodiceAlternativo(lDecod));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(NUM_MOT + lVect.size());
		if (!lVect.isEmpty()) {
			Iterator lMotivCorr = lVect.iterator();
			while (lMotivCorr.hasNext()) {
				lDecod = (DecodificheModel) lMotivCorr.next();

				if (lDecod.getCodiceAlternativo() != null && (lDecod.getFiltro() != null
						&& lDecod.getFiltro().compareToIgnoreCase("RD") == 0)) {
					// Conversione del carattere € per jsp
					lDecod.setDescription(lDecod.getDescription().replace("€", "&#8364;"));
				} else {
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Rimosso : " + lDecod.getCode());
					lMotivCorr.remove();
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(MSG_MOT + lVect.size());
		}
		return lVect;
	}

	/**
	 * Recupero la lista delle tipologie di Rigetto del Differimento ESITO_TENORE - C014. Attenzione!!! nel
	 * campo Code viene caricato il valore dell'RV_ABBREVIATION invece del RV_LOW_VALUE in quanto sono questi
	 * i codici di interesse (anche per la costruzione delle Option)
	 * <p>
	 *
	 * @return Insieme di dati.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExRicercaTipologiaRigettoDiff() throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheSqlDAO(lConn);

			lDecDao.listaTipologiaRigettoDifferimento();

			lDecDao.start();

			// while (lDecDao.next())
			// lDecodifiche.add( (DecodificheModel) lDecDao.getModel());

			// Carico nel code il valore dell'RV_ABBREVIATION in quanto sono questi
			// i codici che interessano
			while (lDecDao.next()) {
				DecodificheModel lDecMod = (DecodificheModel) lDecDao.getModel();
				// lDecMod.getCodiceAlternativo()
				lDecMod.setCode(lDecMod.getCodiceAlternativo());
				lDecodifiche.add(lDecMod);
			}

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaTipologiaRigettoDiff: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	/**
	 * Recupero la lista delle Attivita' relative ad un Incarico SIEPE.
	 *
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */

	public Collection ExRicercaAttivitaByIncarico(String aCodIncarico) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheSqlDAO(lConn);
			lDecDao.ricercaAttivitaByIncarico(aCodIncarico);
			lDecodifiche = new Vector(lDecDao.getModels());
		} catch (Exception eEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(EXC + eEx);
			throw new F3BException(
					this.getClass().getPackage().getName() + ".ExRicercaAttivitaByIncarico: " + eEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lDecodifiche;
	}

	/**
	 * Metodo generico che setta una lista di decode model che come codici invece diprendere il low_value
	 * prende l'high value. L'ordinamento viene fatto pero' per il low_value
	 *
	 * @param aDomain
	 * @return Collectio di DecodeModel
	 */
	public Collection ExRicercaAndSetCodHighValue(DecodificheModel aModel) throws F3BException {

		Connection lConn = null;
		DecodificheDAO lDecDao = null;
		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setCondizioni(aModel);
			lDecDao.setOrdinamentoPerCodice();

			lDecDao.start();

			while (lDecDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecDao.getCodiceAlternativo());
				lDecMod.setDescription(lDecDao.getDescrizione());
				lContenuti.add(lDecMod);
			}

			lDecDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExRicercaAndSetCodHighValue: " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	/**
	 * Restituisce l'elenco dei tipi di Ufficio SIGE.
	 *
	 * @return Collection
	 * @throws F3BException
	 */

	public Collection ExListaTipiUfficioSige() throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setRicercaTipoUfficioSIGE();

			lDecodifiche = new ArrayList(lDecDao.getModels());
		} catch (Exception eEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(EXC + eEx);
			throw new F3BException(
					this.getClass().getPackage().getName() + ".ExListaTipiUfficioSige: " + eEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lDecodifiche;
	}

	public Collection ExListaTipiUfficioSigeAccorpato() throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setRicercaTipoUfficioSIGEAccorpato();

			lDecodifiche = new ArrayList(lDecDao.getModels());
		} catch (Exception eEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(EXC + eEx);
			throw new F3BException(
					this.getClass().getPackage().getName() + ".ExListaTipiUfficioSigeAccorpato: " + eEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lDecodifiche;
	}

	/**
	 * Lista Contenuti SIGE.
	 * <p>
	 *
	 * @return Vettore di Oggetti
	 * @throws F3BException
	 */
	public Collection ExListaContenutiSige() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lOggetti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaContenutiSige();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getString(COD_CONTENUTO));
				lDecMod.setDescription(lDecSqlDao.getString(DESC_CONTENUTO));
				lOggetti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("DecodificheController.ExListaContenutiSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("DecodificheController.ExListaContenutiSige: " + e);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lOggetti;
	}

	/**
	 * Lista Oggetti SIGE per Contenuto.
	 *
	 * @param codContenuto
	 *            codice del contenuto selezionato
	 * @return Vettore di Oggetti
	 * @throws F3BException
	 */
	public Collection ExListaOggettiSigePerContenuto(String codContenuto) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lOggetti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggettiSigePerContenuto(codContenuto);

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				OggettiModel lOggetto = new OggettiModel();

				lOggetto.setCodOggetto(lDecSqlDao.getString(COD_OGGETTO));
				lOggetto.setDescOggetto(lDecSqlDao.getString(DESC_OGGETTO));
				lOggetto.setCodContenuto(lDecSqlDao.getString(COD_CONTENUTO));
				lOggetto.setDescContenuto(lDecSqlDao.getString(DESC_CONTENUTO));
				lOggetto.setAbbrOggetto(lDecSqlDao.getString(ABBR_OGGETTO));

				lOggetti.add(lOggetto);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("DecodificheController.ExListaOggettiSigePerContenuto: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("DecodificheController.ExListaOggettiSigePerContenuto: " + e);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lOggetti;
	}

	/**
	 * Recupera il campo RV_MEANING nella tabella CG_REF_CODES corrispondente al codOggettoSige passato in
	 * ingresso
	 *
	 * @param codOggettoSige
	 * @return descrizione oggetto sige
	 * @throws F3BException
	 */
	public String ExRicercaDescrByCodOggettoSige(String codOggettoSige) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecDao = null;
		String descOggettoSige = "";

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheSqlDAO(lConn);
			lDecDao.ricercaDescrByCodOggettoSige(codOggettoSige);
			lDecDao.start();

			if (lDecDao.next())
				descOggettoSige = lDecDao.getModelRvMeaning();

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDescrByCodOggettoSige: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return descOggettoSige;
	}

	/**
	 * Lista Oggetti-Contenuto SIGE.
	 * <p>
	 *
	 * @return Vettore di Oggetti
	 * @throws F3BException
	 */
	public Collection ExListaOggettiSige() throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lOggetti = new Vector();

		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaOggettiSige();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				OggettiModel lOggetto = new OggettiModel();

				lOggetto.setCodOggetto(lDecSqlDao.getString(COD_OGGETTO));
				lOggetto.setDescOggetto(lDecSqlDao.getString(DESC_OGGETTO));
				lOggetto.setCodContenuto(lDecSqlDao.getString(COD_CONTENUTO));
				lOggetto.setDescContenuto(lDecSqlDao.getString(DESC_CONTENUTO));
				lOggetto.setAbbrOggetto(lDecSqlDao.getString(ABBR_OGGETTO));
				// STUB: da eliminare insieme alla outer join
				if (lOggetto.getDescContenuto() == null)
					lOggetto.setDescContenuto(lOggetto.getCodContenuto());

				lOggetti.add(lOggetto);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("DecodificheController.ExListaOggettiSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("DecodificheController.ExListaOggettiSige: " + e);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lOggetti;
	}

	/**
	 * Ricerca degli esiti associati ad uno specifico oggetto SIGE.
	 *
	 * @param lCodOggetto
	 * @return
	 * @throws F3BException
	 */

	public Collection ExRicercaEsitiByOggettoSige(String lCodOggetto) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheSqlDAO(lConn);

			lDecDao.ricercaEsitiByOggettoSige(lCodOggetto);

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, MSG_INI + lCodOggetto + MSG_FIN);

			lDecDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaEsitiByOggettoSige: " + daoex);
		} catch (Exception sqex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaEsitiByOggettoSige: " + sqex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	/**
	 * Ricerca dei Dati del Provvedimento SIGE associati ad uno specifico oggetto SIGE.
	 *
	 * @param lCodOggetto
	 * @return
	 * @throws F3BException
	 */
	public Collection ExRicercaDatiProvvSigeByOggetto(String lCodOggetto) throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheSqlDAO(lConn);

			lDecDao.ricercaDatiProvvSigeByOggetto(lCodOggetto);

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			lDecDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDatiProvvSigeByOggetto: " + daoex);
		} catch (Exception sqex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDatiProvvSigeByOggetto: " + sqex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	@Override
	public Collection<DecodificheModel> ExRicercaDecodificheTipoAutorita() throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheDAO(lConn);

			lDecDao.setRicercaTipoAutorita();

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			lDecDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheTipoAutorita: " + daoex);
		} catch (Exception sqex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDecodificheTipoAutorita: " + sqex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	private boolean isUfficioMinorenni(String tipoUfficio) {

		boolean ret = true;
		// prende dalla sessione il codice dell'ufficio dell'utente connesso
		Set<String> elenco = new HashSet<>();
		elenco.add("PMM");
		elenco.add("DIBM");
		elenco.add("GIPM");
		elenco.add("GUPM");
		elenco.add("CAPSM");
		elenco.add("TDSM");
		elenco.add("UDSM");

		if (!elenco.contains(tipoUfficio)) {
			ret = false;
		}
		return ret;
	}

	@Override
	public Collection<DecodificheModel> ExRicercaDecodificheTipoMisureMinorenni() throws F3BException {

		Connection lConn = null;

		DecodificheSqlDAO lDecSqlDao = null;

		Collection lContenuti = new Vector();

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.ricercaDecodificheTipoMisureMinorenni();

			lDecSqlDao.start();

			// MEV10-s3: aggiunta rigavuota in testa all'elenco
			DecodificheModel lDecMod = new DecodificheModel();
			lDecMod.setCode("-");
			lDecMod.setDescription("-");
			lContenuti.add(lDecMod);

			while (lDecSqlDao.next()) {
				lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				// modifica 24-02-05 dario
				// lDecMod.setDescription(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setFiltro(lDecSqlDao.getModelRvAbbreviation());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ExRicercaDecodificheTipoMisureMinorenni: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lContenuti;
	}

	// MEV10-s3: aggiunte collection per gestire invio mail segnalazione
	public Collection ExTitoloPersona(DecodificheModel aModel) throws F3BException {

		// esecuzione del metodo con caricamento combo
		return caricaCollection(aModel, "ExTitoloPersona");
	}

	public Collection ExFunzionalita(DecodificheModel aModel) throws F3BException {

		// esecuzione del metodo con caricamento combo
		Collection lDecodifiche = caricaCollection(aModel, "ExFunzionalita");
		aModel.setCodiceAlternativo("TUTTI");
		lDecodifiche.addAll(caricaCollection(aModel, "ExFunzionalita"));
		ArrayList al = new ArrayList(lDecodifiche);
		Collections.sort(al, new CodeComparator());
		lDecodifiche.clear();
		lDecodifiche.addAll(al);
		return lDecodifiche;
	}

	public Collection ExAzione(DecodificheModel aModel) throws F3BException {

		// esecuzione del metodo con caricamento combo
		return caricaCollection(aModel, "ExAzione");
	}

	public Collection ExTipoSegnalazione(DecodificheModel aModel) throws F3BException {

		// esecuzione del metodo con caricamento combo
		return caricaCollection(aModel, "ExTipoSegnalazione");
	}

	public Collection ExGravitaSegnalazione(DecodificheModel aModel) throws F3BException {

		// esecuzione del metodo con caricamento combo
		return caricaCollection(aModel, "ExGravitaSegnalazione");
	}

	/**
	 * Metodo per il caricamento dei valori richiesti nella combo
	 *
	 * @param aModel
	 * @param name
	 * @return Collection
	 * @throws F3BException
	 */
	private Collection caricaCollection(DecodificheModel aModel, String name) throws F3BException {

		Connection lConn = null;
		DecodificheDAO lDecDao = null;
		Collection lContenuti = new Vector();
		DecodificheModel lDecMod = new DecodificheModel();

		if (!"TUTTI".equals(aModel.getCodiceAlternativo())) {
			lDecMod.setCode("-");
			lDecMod.setDescription("-");
			lContenuti.add(lDecMod);
		}

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheDAO(lConn);
			lDecDao.setCondizioni(aModel);
			lDecDao.setOrdinamentoPerDescrizione();
			lDecDao.start();
			while (lDecDao.next()) {
				lDecMod = new DecodificheModel();
				// lDecMod.setCode(lDecDao.getCodice());
				lDecMod.setCode(lDecDao.getDescrizione());
				lDecMod.setDescription(lDecDao.getDescrizione());
				lContenuti.add(lDecMod);
			}
			lDecDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(DAOEXC + daoEx);
			throw new F3BException(getClass().getName() + "." + name + ": " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lContenuti;
	}

	/**
	 * Inner class responsabile dell'ordinamento di una collection
	 *
	 * @author sgioggi
	 */
	public class CodeComparator implements Comparator<DecodificheModel> {

		@Override
		public int compare(DecodificheModel p1, DecodificheModel p2) {

			// valore di ritorno - confronto
			return p1.getCode().compareTo(p2.getCode());
		}
	}

	/**
	 * RV_ABBREVIATION = 01
	 */
	public Collection<DecodificheModel> ExRicercaDecodificheTipoMSMinorenniByNatura(String codice)
			throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lContenuti = new Vector();
		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.ricercaDecodificheTipoMSMinorenniByNatura(codice);
			lDecSqlDao.start();
			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lContenuti.add(lDecMod);
			}
			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException(
					"DecodificheController.ExRicercaDecodificheTipoMSMinorenniByNatura: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lContenuti;
	}
	// FINE MEV10-s3

	/**
	 * intervento post collaudo 11.2
	 *
	 * @param listaNotInValue
	 * @return
	 * @throws F3BException
	 */
	public Collection<DecodificheModel> ricercaAllTipoAutoritaNotIn(String[] listaNotInValue)
			throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecSqlDao = null;
		Collection lAut = new Vector();
		try {
			lConn = getDBConnection();
			lDecSqlDao = new DecodificheSqlDAO(lConn);
			lDecSqlDao.ricercaAllTipoAutoritaNotIn(listaNotInValue);
			lDecSqlDao.start();
			while (lDecSqlDao.next()) {
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lAut.add(lDecMod);
			}
			lDecSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.debug(DAOEXC + daoEx);
			throw new F3BException("DecodificheController.ricercaAllTipoAutoritaNotIn: " + daoEx);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}
		// valore di ritorno
		return lAut;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * siap.sico.decodifiche.controller.IDecodifiche#ExRicercaEsitiCompatibiliByEsitoOggettoU023(java.lang.
	 * String, java.lang.String)
	 *
	 * 28/08/2018 : intervento post-collaudo
	 */
	public Collection ExRicercaEsitiCompatibiliByEsitoOggettoU023(String lCodOggetto, String lCodEsito)
			throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecDao = null;

		try {
			lConn = getDBConnection();

			lDecDao = new DecodificheSqlDAO(lConn);

			lDecDao.ricercaEsitiCompatibiliByEsitoOggettoU023(lCodOggetto, lCodEsito);

			lDecDao.start();

			while (lDecDao.next())
				lDecodifiche.add(lDecDao.getModel());

			if (lDecodifiche.isEmpty())
				throw new SICOException(F3BException.USER_MESSAGE, MSG_INI + lCodOggetto + MSG_FIN);

			lDecDao.stop();
		} catch (DAOException daoex) {
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaEsitiCompatibiliByEsitoOggettoU023: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}

	/**
	 * Recupera il campo RV_MEANING nella tabella CG_REF_CODES corrispondente al codContenutoSige passato in
	 * ingresso
	 *
	 * @param codContenutoSige
	 * @return descrizione contenuto sige
	 * @throws F3BException
	 */
	public String ExRicercaDescrByCodContenutoSige(String codOggettoSige) throws F3BException {

		Connection lConn = null;
		DecodificheSqlDAO lDecDao = null;
		String descOggettoSige = "";

		try {
			lConn = getDBConnection();
			lDecDao = new DecodificheSqlDAO(lConn);
			lDecDao.ExRicercaDescrByCodContenutoSige(codOggettoSige);
			lDecDao.start();

			if (lDecDao.next())
				descOggettoSige = lDecDao.getModelRvMeaning();

			lDecDao.stop();
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(DAOEXC + daoex);
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExRicercaDescrByCodOggettoSige: " + daoex);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}

		return descOggettoSige;
	}

	/**
	 * MEV_9-SIEP
	 */
	public Collection ExListaMotivoProvvSosp678() throws F3BException {

		Connection lConn = null;
		Collection lDecodifiche = new Vector();
		DecodificheSqlDAO lDecSqlDao = null;

		try {
			lConn = getDBConnection();

			lDecSqlDao = new DecodificheSqlDAO(lConn);

			lDecSqlDao.listaMotivoProvvSosp678();

			lDecSqlDao.start();

			while (lDecSqlDao.next()) {
				//lDecodifiche.add(lDecSqlDao.getModel());				
				DecodificheModel lDecMod = new DecodificheModel();
				lDecMod.setCode(lDecSqlDao.getModelRvLowValue());
				lDecMod.setDescription(lDecSqlDao.getModelRvMeaning());
				lDecodifiche.add(lDecMod);
			}

			lDecSqlDao.stop();
		} catch (DAOException daoex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExListaMotivoProvvSosp678: " + daoex);
		} catch (Exception sqex) {
			throw new SICOException(F3BException.USER_MESSAGE,
					"DecodificheController.ExListaMotivoProvvSosp678: " + sqex);
		} finally {
			cleanup(lDecSqlDao);
			cleanup(lConn);
		}

		return lDecodifiche;
	}
}