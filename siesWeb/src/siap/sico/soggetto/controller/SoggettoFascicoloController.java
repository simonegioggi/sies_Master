package siap.sico.soggetto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: SoggettoFascicoloController
 * </p>
 * <p>
 * Description: Classe controller del Soggettocon Fascicoli
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SoggettoFascicoloController extends SiapController implements ISoggettoFascicolo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Ambrosino 03/2010
	/**
	 * Ricerca di Soggetti all'interno del distretto Con TipoUffucio e Numero fascicolo nella lista
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aCodDistretto,
			int aPage) throws F3BException {

		return ExRicercaSoggettoPerDistrettoProgFasc(aSoggetto, aCodDistretto, aPage, "");
	}

	public Vector ExRicercaSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aCodDistretto,
			int aPage, String majorOffice) throws F3BException {

		Connection lConn = null;
		Vector lSoggetti = new Vector();

		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloSiepSqlDAO lFasDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			SoggettoModel lSogMod = new SoggettoModel();

			FascicoloSiepModel lFas = new FascicoloSiepModel();
			lSogSqlDao.ricercaSoggettoPerDistretto(aSoggetto, aCodDistretto, aPage, majorOffice);

			lSogSqlDao.start();
			while (lSogSqlDao.next()) {
				FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
				lSogMod = (SoggettoModel) lSogSqlDao.getModel();
				lFascicolo.setSoggetto(lSogMod);

				// se il parametro aPage=0 Questa ricerca serve solo per il conteggio
				// totale dei soggetti
				// e non serve che vada avanti col numero fascicolo assosciato

				if (aPage > 0) {
					lFas.setSogIdSoggetto(lSogMod.getIdSoggetto());
					FascicoloSiepModel lFascicoloSiep = null;

					// Ricerca su FascicoloSiep
					lFasDao.ricercaFascicoloProgFasc(lFas, "FASCICOLO_SIEP");
					lFasDao.start();
					if (lFasDao.next()) {
						lFascicoloSiep = (FascicoloSiepModel) lFasDao.getModelsPerRicUff();
						lFasDao.stop();
					} else {
						// Ricerca su FascicoloSius
						lFasDao.ricercaFascicoloProgFasc(lFas, "FASCICOLO_SIUS");
						lFasDao.start();
						if (lFasDao.next()) {
							lFascicoloSiep = (FascicoloSiepModel) lFasDao.getModelsPerRicUff();
							lFasDao.stop();
						} else {
							// Ricerca su FascicoloSige
							lFasDao.ricercaFascicoloProgFasc(lFas, "FASCICOLO_SIGE");
							lFasDao.start();
							if (lFasDao.next()) {
								lFascicoloSiep = (FascicoloSiepModel) lFasDao.getModelsPerRicUff();
								lFasDao.stop();
							}
						}
					}

					if (lFascicoloSiep != null) {
						lFascicolo.setDescrComuneUfficio(lFascicoloSiep.getDescrComuneUfficio());
						lFascicolo.setDescrTipoUfficio(lFascicoloSiep.getDescrTipoUfficio());
						lFascicolo.setChiaveAnno(lFascicoloSiep.getChiaveAnno());
						lFascicolo.setChiaveProgr(lFascicoloSiep.getChiaveProgr());
					}

				} // chiude if if (aPage > 0 )

				lSoggetti.add(lFascicolo);

			} // chiude while (lSogSqlDao.next())

			lSogSqlDao.stop();

			if (lSoggetti.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoFascicoloController.ExRicercaSoggettoPerDistrettoProgFasc : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lFasDao);

			cleanup(lConn);
		}

		return lSoggetti;
	} // chiude ExRicercaSoggettoPerDistrettoProgFasc

	/**
	 * Conta i Soggetti all'interno del distretto
	 *
	 * @param aSoggetto
	 *            SoggettoModel, aCodDistretto CodiceDistretto, aPage (numero di pagina = 0 per contare)
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public BigDecimal ExCountSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aCodDistretto,
			int aPage) throws F3BException {

		return ExCountSoggettoPerDistrettoProgFasc(aSoggetto, aCodDistretto, aPage, "");
	}

	public BigDecimal ExCountSoggettoPerDistrettoProgFasc(SoggettoModel aSoggetto, String aCodDistretto,
			int aPage, String majorOffice) throws F3BException {

		Connection lConn = null;
		SoggettoSqlDAO lSogSqlDao = null;
		BigDecimal HowManyRecords = null;

		try {
			lConn = getDBConnection();

			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoPerDistretto(aSoggetto, aCodDistretto, aPage, majorOffice);
			lSogSqlDao.start();
			lSogSqlDao.next();
			HowManyRecords = lSogSqlDao.getBigDecimal("HowManyRecords");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoFascicoloController.ExCountSoggettoPerDistrettoProgFasc : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}
		return HowManyRecords;
	} // chiude ExCountSoggettoPerDistrettoProgFasc

} // chiude classe