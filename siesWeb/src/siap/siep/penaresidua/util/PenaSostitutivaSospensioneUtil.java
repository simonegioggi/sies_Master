package siap.siep.penaresidua.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.util.DateUtils;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;

/**
 * Aggiornamento SIEP della pena residua a seguito di sospensione/ripresa dell'esecuzione di una pena
 * sostitutiva inserita in SIUS (Riforma Cartabia). La sospensione e' marcata con
 * PENA_RESIDUA.FLAG_PENA_SOSPESA = 'S'. Le operazioni usano la connessione (transazione) del chiamante.
 *
 * @author SG
 * @since 20261124-MEV_2026-124_SG
 */
public class PenaSostitutivaSospensioneUtil {

	public static final String SOSPESA = "S";

	/**
	 * Marca la pena residua corrente come sospesa, con il quantum residuo al momento della sospensione e
	 * senza date di inizio e fine pena sostitutiva.
	 *
	 * @return true se e' stata inserita una nuova pena residua
	 */
	public static boolean sospendi(Connection aConn, BigDecimal aIdFascicoloSiep, BigDecimal aIdEvento,
			BigDecimal aResiduaAnni, BigDecimal aResiduaMesi, BigDecimal aResiduaGiorni,
			String aCodOperatore, String aCodUfficio) throws DAOException {

		PenaResiduaModel lCorrente = getPenaCorrente(aConn, aIdFascicoloSiep);
		if (lCorrente == null || SOSPESA.equals(lCorrente.getFlagPenaSospesa())) {
			return false;
		}
		SanzioneSostResiduaModel lSsr = getSanzioneSostResidua(aConn, lCorrente.getIdPenaResidua());

		BigDecimal lIdNuova = inserisciPena(aConn, lCorrente, SOSPESA, null, null, aIdEvento, aCodOperatore,
				aCodUfficio);

		if (lSsr != null) {
			if (aResiduaAnni != null || aResiduaMesi != null || aResiduaGiorni != null) {
				lSsr.setNumAnni(aResiduaAnni);
				lSsr.setNumMesi(aResiduaMesi);
				lSsr.setNumGiorni(aResiduaGiorni);
			}
			inserisciSsr(aConn, lSsr, lIdNuova, null, null, null, aIdEvento, aCodOperatore, aCodUfficio);
		}
		return true;
	}

	/**
	 * Toglie lo stato di sospensione e ripresenta le date di inizio e fine pena sostitutiva.
	 *
	 * @return true se e' stata inserita una nuova pena residua
	 */
	public static boolean riprendi(Connection aConn, BigDecimal aIdFascicoloSiep, BigDecimal aIdEvento,
			Date aDataInizio, Date aDataFine, String aCodOperatore, String aCodUfficio)
			throws DAOException {

		PenaResiduaModel lCorrente = getPenaCorrente(aConn, aIdFascicoloSiep);
		if (lCorrente == null || !SOSPESA.equals(lCorrente.getFlagPenaSospesa())) {
			return false;
		}
		SanzioneSostResiduaModel lSsr = getSanzioneSostResidua(aConn, lCorrente.getIdPenaResidua());

		BigDecimal lIdNuova = inserisciPena(aConn, lCorrente, null, aDataInizio, aDataFine, aIdEvento,
				aCodOperatore, aCodUfficio);

		if (lSsr != null) {
			inserisciSsr(aConn, lSsr, lIdNuova, aDataInizio, aDataFine, aDataFine, aIdEvento, aCodOperatore,
					aCodUfficio);
		}
		return true;
	}

	private static PenaResiduaModel getPenaCorrente(Connection aConn, BigDecimal aIdFascicoloSiep)
			throws DAOException {
		PenaResiduaSqlDAO lDao = new PenaResiduaSqlDAO(aConn);
		try {
			lDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aIdFascicoloSiep);
			return (PenaResiduaModel) lDao.getModelByKey();
		} finally {
			lDao.stop();
		}
	}

	private static SanzioneSostResiduaModel getSanzioneSostResidua(Connection aConn, BigDecimal aIdPena)
			throws DAOException {
		SanzioneSostResiduaSqlDAO lDao = new SanzioneSostResiduaSqlDAO(aConn);
		try {
			lDao.ricercaSanzioneSostResiduaByIdPenRes(aIdPena);
			return (SanzioneSostResiduaModel) lDao.getModelByKey();
		} finally {
			lDao.stop();
		}
	}

	private static BigDecimal inserisciPena(Connection aConn, PenaResiduaModel aPena, String aFlagSospesa,
			Date aDataInizio, Date aDataFine, BigDecimal aIdEvento, String aCodOperatore, String aCodUfficio)
			throws DAOException {
		aPena.setIdPenaResidua(null);
		aPena.setFlagValidato("S");
		aPena.setFlagPenaSospesa(aFlagSospesa);
		aPena.setDataInizio(aDataInizio);
		aPena.setDataFine(aDataFine);
		aPena.setEveIdEvento(aIdEvento);
		aPena.setCodOperatoreAggiornamento(null);
		aPena.setDataAggiornamento(null);
		aPena.setCodUfficioAggiornamento(null);
		aPena.setCodOperatoreInserimento(aCodOperatore);
		aPena.setCodUfficioInserimento(aCodUfficio);
		aPena.setDataInserimento(DateUtils.getSysDate());

		PenaResiduaDAO lDao = new PenaResiduaDAO(aConn);
		try {
			lDao.setDAOFromModel(aPena);
			return lDao.insert();
		} finally {
			lDao.stop();
		}
	}

	private static void inserisciSsr(Connection aConn, SanzioneSostResiduaModel aSsr, BigDecimal aIdPena,
			Date aDataInizio, Date aDataFinePresunta, Date aDataFine, BigDecimal aIdEvento,
			String aCodOperatore, String aCodUfficio) throws DAOException {
		aSsr.setIdSanzioneSostResidua(null);
		aSsr.setPenResIdPenaResidua(aIdPena);
		aSsr.setEveIdEvento(aIdEvento);
		aSsr.setDataInizio(aDataInizio);
		aSsr.setDataFinePresunta(aDataFinePresunta);
		aSsr.setDataFine(aDataFine);
		aSsr.setCodOperatoreAggiornamento(null);
		aSsr.setDataAggiornamento(null);
		aSsr.setCodUfficioAggiornamento(null);
		aSsr.setCodOperatoreInserimento(aCodOperatore);
		aSsr.setCodUfficioInserimento(aCodUfficio);
		aSsr.setDataInserimento(DateUtils.getSysDate());

		SanzioneSostResiduaDAO lDao = new SanzioneSostResiduaDAO(aConn);
		try {
			lDao.setDAOFromModel(aSsr);
			lDao.insert();
		} finally {
			lDao.stop();
		}
	}
}
