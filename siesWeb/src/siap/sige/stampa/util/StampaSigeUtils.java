package siap.sige.stampa.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.stampa.controller.SIAPStampaController;
import siap.sige.SIGEException;
import siap.sige.avvocato.dao.AvvocatoFascicoloSigeSqlDAO;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: StampaSigeUtils
 * </p>
 * <p>
 * Description: Classe di utilita' per il package FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaSigeUtils extends SIAPStampaController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue la ricerca degli Avvocati x Fascicolo SIGE.
	 * <p>
	 * 
	 * @param aKey
	 *            chiave id del Fascicolo SIGE.
	 * @return l'insieme degli Avvocati.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector getAvvocatiSige(BigDecimal aIdFascicoloSIGE, Connection aConn) throws F3BException {
		AvvocatoFascicoloSigeSqlDAO lAvvSqlDao = null;
		Vector lAvvocati = new Vector();
		try {
			lAvvSqlDao = new AvvocatoFascicoloSigeSqlDAO(aConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(aIdFascicoloSIGE);

			while (lAvvSqlDao.next()) {
				AvvocatoSigeModel lAvvSige = (AvvocatoSigeModel) lAvvSqlDao.getModel();
				lAvvocati.add(lAvvSige);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo getAvvocatiSige : " + lAvvocati);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeUtils.getAvvocatiSige : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIGEException("StampaSigeUtils.getAvvocatiSige : " + lEx);
		} finally {
			cleanup(lAvvSqlDao);
		}
		return lAvvocati;
	}

	/**
	 * Esegue il prelievo del TreeModel degli Avvocati Sige.
	 * <p>
	 * 
	 * @param aIdFascicoloSIGE.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati degli Avvocati Sige come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel prelevaDatiAvvocatiSige(TreeModel lTreeFasSIGE, BigDecimal aIdFascicoloSIGE,
			Connection aConn) throws F3BException {
		TreeModel lTreeAvvSige = null;
		AvvocatoFascicoloSigeSqlDAO lAvvSqlDao = null;

		try {
			lAvvSqlDao = new AvvocatoFascicoloSigeSqlDAO(aConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(aIdFascicoloSIGE);

			lAvvSqlDao.start();
			while (lAvvSqlDao.next()) {
				AvvocatoSigeModel lAvvSige = (AvvocatoSigeModel) lAvvSqlDao.getModel();
				AvvocatoModel lAvv = lAvvSige.getAvvocato();
				AvvocatoFascicoloSigeModel lAvvSigMod = lAvvSige.getAvvocatoFascicoloSigeModel();

				lTreeAvvSige = new TreeModel(lAvv);
				lTreeAvvSige.add(new TreeModel(lAvvSigMod));
				lTreeFasSIGE.add(lTreeAvvSige);
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeUtils.prelevaDatiAvvocatiSige : " + daoEx);
		} catch (Exception lEx) {
			lEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIGEException("StampaSigeUtils.prelevaDatiAvvocatiSige : " + lEx);
		} finally {
			cleanup(lAvvSqlDao);
		}
		return lTreeFasSIGE;
	}

}