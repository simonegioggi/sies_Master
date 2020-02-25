package siap.bdmc.sbpren.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.bdmc.sbperipren.dao.SbPeriprenDAO;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.bdmc.sbpren.action.ICostantiSbPren;
import siap.bdmc.sbpren.dao.SbPrenDAO;
import siap.bdmc.sbpren.model.EsitoImportModel;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbpren.model.ProvvedimentoSiepModel;
import siap.bdmc.sbviewcapoimpu.dao.SbViewCapoimpuDAO;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.bdmc.sbviewprocpena.dao.SbViewProcpenaDAO;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.bdmc.sbviewreat.dao.SbViewReatDAO;
import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.CalendarUtil;
import siap.siep.SIEPException;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.dao.MisuraCautelareDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.dao.MisuraCautelareBdmcDAO;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
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
 * Title: ImportaDatiInBDMCController
 * </p>
 * <p>
 * Description: Classe di Utilità per l'import in Siep dei dati BDMC
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ImportaDatiInBDMCController extends SiapController implements IImportaDati {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private siap.bdmc.sbpren.model.EsitoImportModel mEsito = null;
	private String mUtente = "";
	private String mUfficio = "";
	private Date mData = null;

	public siap.bdmc.sbpren.model.EsitoImportModel getEsito() {
		return mEsito;
	}

	/**
	 * Importa Provvedimento BDMC
	 * 
	 * @param aBDMCProvvedimento
	 * @return ProvvedimentoModel
	 * @throws F3BException
	 */
	public BigDecimal ExImportaProvvedimentoBDMC(siap.bdmc.sbpren.model.EsitoImportModel aEsito)
			throws F3BException {
		Connection lConn = null;

		BigDecimal lKeyFascicolo = null;

		SoggettoDAO lSoggDAO = null;
		SentenzaDAO lSentDAO = null;
		SentenzaModel lSentenza = null;
		SoggettoModel lSoggetto = null;

		ProvvedimentoModelBDMC lBDMCProvvedimento = aEsito.getProvvedimento();
		ProvvedimentoSiepModel lProvv = new ProvvedimentoSiepModel();
		FascicoloSiepModel lFascicolo = lBDMCProvvedimento.getFascicoloSiep();
		boolean isInserisciSentenzaNuova = true;
		boolean isInserisciSoggettoNuovo = true;
		try {
			mEsito = new EsitoImportModel(aEsito);
			mUtente = lBDMCProvvedimento.getUtente();
			mUfficio = lBDMCProvvedimento.getUfficio();
			mData = lBDMCProvvedimento.getDataInserimento();
			Vector lProcPena = lBDMCProvvedimento.getSbViewProcpena();
			SbViewProcpenaModel lModPena = (SbViewProcpenaModel) lProcPena.get(0);
			// Se esiste la sentenza (recuperata precedentemente dalla sessione) non si inserisce quella di
			// BDMC
			if (lBDMCProvvedimento.getSentenza() != null) {
				lSentenza = lBDMCProvvedimento.getSentenza();

				isInserisciSentenzaNuova = false;
			} else {
				lSentenza = lModPena.toSentenza();
			}
			// modifica in analogia della variazione in SentenzaModel - Romaggioli 29/07/2009
			// lSentenza.setDataArrivoAtto(lBDMCProvvedimento.getDataArrivoAtto());

			lSentenza.setDataInserimento(mData);
			lSentenza.setCodUfficioInserimento(mUfficio);
			lSentenza.setCodOperatoreInserimento(mUtente);

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.debug("SENTENZA da inserire = " + lSentenza);
			if (lBDMCProvvedimento.getIdSoggettoOmonimo() != null)
				isInserisciSoggettoNuovo = false;

			lConn = getDBTransaction();
			BigDecimal lIdSoggetto = null;
			try {
				if (isInserisciSoggettoNuovo) {

					lSoggetto = lBDMCProvvedimento.getSoggetto();
					lSoggetto.setFlagPresenzaFascicolo("S");
					lSoggetto.setSesso(lSoggetto.getSesso().trim());
					if (lSoggetto.getCodStatoNascita() != null
							&& lSoggetto.getCodStatoNascita().compareTo("100") == 0)
						lSoggetto.setCodStatoNascita("039");
					if (lSoggetto.getCodStatoNascita() == null
							|| lSoggetto.getCodStatoNascita().length() == 0
							|| lSoggetto.getCodStatoNascita().compareTo("000") == 0)
						lSoggetto.setCodStatoNascita("-");
					// ----Inserimento Soggetto------
					lSoggDAO = new SoggettoDAO(lConn);
					lSoggDAO.setDAOFromModel(lSoggetto);
					lIdSoggetto = lSoggDAO.insert();
					mEsito.setEsitoSoggetto(ICostantiSbPren.ESITO_POSITIVO);
					lSoggetto.setIdSoggetto(lIdSoggetto);
					lProvv.setSoggetto(lSoggetto);
				} else {
					lIdSoggetto = lBDMCProvvedimento.getIdSoggettoOmonimo();
					mEsito.setEsitoSoggetto(ICostantiSbPren.ESITO_DATO_PRESENTE);
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
					mEsito.setEsitoSentenza(ICostantiSbPren.ESITO_POSITIVO);
					lSentenza.setIdSentenza(lIdSentenza);
					lProvv.setSentenza(lSentenza);
				} else {
					lIdSentenza = lSentenza.getIdSentenza();
					mEsito.setEsitoSentenza(ICostantiSbPren.ESITO_DATO_PRESENTE);
					lProvv.setSentenza(lSentenza);
				}
			} catch (Exception ex) {
				mEsito.setEsitoSentenza(ex.getMessage());
			}

			// ---------- Inserimento Fascicolo Siep -----------
			lFascicolo.setSenIdSentenza(lIdSentenza);
			lFascicolo.setSogIdSoggetto(lIdSoggetto);

			lFascicolo = inserisciFascicoloSiep(lConn, lFascicolo);
			lProvv.setFascicoloSiep(lFascicolo);

			lKeyFascicolo = lFascicolo.getIdFascicoloSiep();

			// --- Inserimento entità collegate al fascicolo

			// ===========================================================================
			// Inserimento pena complessiva
			// ===========================================================================
			if (lBDMCProvvedimento.getSbViewProcpena() != null
					&& lBDMCProvvedimento.getSbViewProcpena().size() != 0)
				inserisciPenaComplessiva(lConn, lBDMCProvvedimento.getSbViewProcpena(), lKeyFascicolo);

			if (lBDMCProvvedimento.isSbViewReatCheck()) { // verifica se ci sono da inserire i reati
				Vector lReati = inserisciReati(lConn, lBDMCProvvedimento.getSbViewCapoImpu(), lKeyFascicolo);
				lProvv.setReati(lReati);
			}
			if (lBDMCProvvedimento.isSbCircostanzeCheck()) {
				inserisciCircostanze(lConn, lBDMCProvvedimento.getSbViewProcpena(), lKeyFascicolo);
			}

			Vector lMisCautBdmc = inserisciPeriPren(lConn, lBDMCProvvedimento.getSbPeriPren(),
					lBDMCProvvedimento.getSbViewProcpena(), lKeyFascicolo, lIdSoggetto,
					lFascicolo.getChiaveAnno(), lFascicolo.getChiaveProgr(), lFascicolo.getChiaveUfficio());
			if (lMisCautBdmc != null && lMisCautBdmc.size() != 0)
				lProvv.setMisCautBdmc(lMisCautBdmc);

			mEsito.setProvvedimentoSiep(lProvv);

			// if (deleteDatiBDMC(aEsito.getProvvedimento().getSbPren().getIdPren(), lConn))
			commit(lConn);
			/*
			 * else { //la cancellazione dei dati BDMC è andata male rollback(lConn); // [FT] - 03/08/2016 -
			 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			 * siesLogger.info("Si è verificato un errore durante la cancellazione dei dati BDMC!");
			 * mEsito.setEsitoCancellazioneBDMC
			 * ("Si è verificato un errore durante la cancellazione dei dati BDMC!"); // throw new
			 * F3BException("Si è verificato un errore durante la cancellazione dei dati BDMC"); }
			 */
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe, sqe);
			mEsito.setEsitoFascicolo(sqe.getMessage());
			// throw new F3BException("ImportaDatiInBDMCController.ExImportaProvvedimentoBDMC:" + sqe);
		} finally {
			cleanup(lSoggDAO);
			cleanup(lSentDAO);

			cleanup(lConn);
		}
		return lKeyFascicolo;
	}

	/**
	 * Integrazione di un fascicolo esistente con i dati selezionati da BDMC
	 * 
	 * @param aBDMCProvvedimento
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExIntegraFascicoloSiep(EsitoImportModel aEsito) throws F3BException {
		Connection lConn = null;
		ProvvedimentoModelBDMC lBDMCProvvedimento = aEsito.getProvvedimento();

		BigDecimal lKeyFascicolo = lBDMCProvvedimento.getFascicoloSiep().getIdFascicoloSiep();
		// BigDecimal lIdSoggetto = lBDMCProvvedimento.getSoggetto().getIdSoggetto();
		ProvvedimentoSiepModel lProvv = new ProvvedimentoSiepModel();
		lProvv.setSentenza(lBDMCProvvedimento.getSentenza());

		lProvv.setFascicoloSiep(lBDMCProvvedimento.getFascicoloSiep());
		lProvv.setSoggetto(lBDMCProvvedimento.getSoggetto());
		try {
			lConn = getDBTransaction();

			mEsito = new EsitoImportModel(aEsito);

			mEsito.setEsitoSentenza(ICostantiSbPren.ESITO_POSITIVO);
			mEsito.setEsitoSoggetto(ICostantiSbPren.ESITO_POSITIVO);
			mEsito.setEsitoFascicolo(ICostantiSbPren.ESITO_POSITIVO);

			// --- Inserimento entità collegate al fascicolo
			if (lBDMCProvvedimento.isSbViewReatCheck()) { // Ci sono da inserire i reati
				Vector lReati = inserisciReati(lConn, lBDMCProvvedimento.getSbViewCapoImpu(), lKeyFascicolo);
				lProvv.setReati(lReati);
			}
			if (lBDMCProvvedimento.isSbCircostanzeCheck()) { // Ci sono da inserire circostanze
				Vector lCirc = inserisciCircostanze(lConn, lBDMCProvvedimento.getSbViewProcpena(),
						lKeyFascicolo);
				lProvv.setCircostanze(lCirc);
			}
			mEsito.setProvvedimentoSiep(lProvv);
			if (deleteDatiBDMC(aEsito.getProvvedimento().getSbPren().getIdPren(), lConn))
				commit(lConn);
			else
				throw new F3BException("Si è verificato un errore durante la cancellazione dei dati BDMC");
		} catch (F3BException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("F3BException: " + sqe, sqe);
			// throw new F3BException("ImportaDatiInBDMCController.ExImportaProvvedimentoBDMC:" + sqe);
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
			// BigDecimal chiaveProgrManuale = null;
			// int annoCorrente = 0;

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

			mEsito.setEsitoFascicolo(ICostantiSbPren.ESITO_POSITIVO);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			if (ex.getMessage().indexOf("FAS_SIE_SEN_SOG_FK_I") != -1)
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Impossibile inserire il fascicolo! Esiste un fascicolo per la  ed il soggetto selezionato");

			if (ex.getMessage().indexOf("FAS_ANN_UFF_PRO_FK_I") != -1)
				throw new SIEPException(F3BException.USER_MESSAGE,
						"Impossibile inserire il fascicolo! Esiste un fascicolo per lo stesso numero SIEP");

			throw new SIEPException("ImportaDatiInBDMCController.inserisciFascicoloSiep: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			throw new F3BException("ImportaDatiInBDMCController.inserisciFascicoloSiep: " + sqe);
		} finally {
			cleanup(lFasDaoSql);
			cleanup(lFasDao);
			cleanup(lStatoProcDao);
		}

		return aFascicoloSiep;
	}

	/**
	 * Inserimento dei periodi prenotati
	 * 
	 * @param lConn
	 * @param aResidenza
	 * @return
	 * @throws F3BException
	 */
	private Vector inserisciPeriPren(Connection lConn, Vector aPeriPren, Vector aSbProcPena,
			BigDecimal lKeyFascicolo, BigDecimal lSogIdSoggetto, BigDecimal annoSiep, BigDecimal numeroSiep,
			String ufficioSiep) throws F3BException {
		SbPeriprenModel lPeriPren = null;
		MisuraCautelareBdmcDAO lMisCautBdmcDao = null;
		MisuraCautelareBdmcModel lMisCautBdmcMod = null;
		MisuraCautelareModel lMisCautelareMod = null;
		MisuraCautelareDAO lMisCautelareDao = null;
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		Vector MisCaut = new Vector();
		if (aPeriPren.size() != 0) {
			SbViewProcpenaModel ProcPena = (SbViewProcpenaModel) aSbProcPena.get(0);
			try {
				Iterator lItx = aPeriPren.iterator();
				while (lItx.hasNext()) {
					lPeriPren = (SbPeriprenModel) lItx.next();
					// inserimento misura Cautelare
					lMisCautelareMod = new MisuraCautelareModel();
					lMisCautelareMod.setAltroLuogoDetenzione(ProcPena.getDescLuog());
					lMisCautelareMod.setCodLuogoUfficioRifer("-");
					// lMisCautelareMod.setCodMotivoNonComputabile(aValore);
					// lMisCautelareMod.setCodOperatoreAggiornamento(aValore);
					lMisCautelareMod.setCodOperatoreInserimento(mUtente);
					lMisCautelareMod.setCodTipoMisura("-");

					if (lPeriPren.getDescPeri() != null && lPeriPren.getDescPeri().indexOf("IN CARCERE") > 0)
						lMisCautelareMod.setCodTipoMisura("CA");
					if (lPeriPren.getDescPeri() != null && lPeriPren.getDescPeri().indexOf("DOMICILIARI") > 0)
						lMisCautelareMod.setCodTipoMisura("AD");

					// Calcolo numero giorni mesi anni della custodia

					CalendarModel lCalPenaEspiata = new CalendarModel();
					CalendarUtil lCalUtil = new CalendarUtil();
					lCalPenaEspiata.setDataInizio(lPeriPren.getDataDaPeri());
					lCalPenaEspiata.setDataFine(lPeriPren.getDataAPeri());
					lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, false);
					lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);

					lMisCautelareMod.setNumAnni(new BigDecimal(lCalPenaEspiata.getNumAnni()));
					lMisCautelareMod.setNumGiorni(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
					lMisCautelareMod.setNumMesi(new BigDecimal(lCalPenaEspiata.getNumMesi()));

					// lMisCautelareMod.setCodTipoMisura(ProcPena.getCodiMisuCust());
					lMisCautelareMod.setCodTipoUfficioRifer("-");
					// lMisCautelareMod.setCodUfficioAggiornamento(aValore);
					lMisCautelareMod.setCodUfficioInserimento(mUfficio);
					// lMisCautelareMod.setDataAggiornamento(aValore);
					lMisCautelareMod.setDataFine(lPeriPren.getDataAPeri());
					// lMisCautelareMod.setDataFungibilita(aValore);
					lMisCautelareMod.setDataInizio(lPeriPren.getDataDaPeri());
					lMisCautelareMod.setDataInserimento(mData);
					// lMisCautelareMod.setDescrLuogoUfficioRifer(aValore);
					lMisCautelareMod.setCodMotivoNonComputabile("-");
					lMisCautelareMod.setDescrTipoMisura(ProcPena.getDescriMisuCust());
					// lMisCautelareMod.setDescrTipoUfficioRifer(aValore);
					// lMisCautelareMod.setDescrUfficioAggiornamento(aValore);
					// lMisCautelareMod.setDescrUfficioInserimento(aValore);
					// lMisCautelareMod.setEveIdEvento(aValore);
					lMisCautelareMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
					if (lPeriPren.getCodStatPrenPeri().compareTo("0") == 0)
						lMisCautelareMod.setFlagComputabile("S");
					else
						lMisCautelareMod.setFlagComputabile("N");
					lMisCautelareMod.setIstDetIdIstitutoDetenzione(ProcPena.getCodiIstiPena());
					// lMisCautelareMod.setIstitutoDetenzione(aValore);
					// lMisCautelareMod.setMessage(aValue);
					// lMisCautelareMod.setNote(aValore);
					// lMisCautelareMod.setNumAnni(ProcPena.get);
					// lMisCautelareMod.setNumGiorni(aValore);
					// lMisCautelareMod.setNumMesi(aValore);
					// lMisCautelareMod.setNumRifer(aValore);
					lMisCautelareDao = new MisuraCautelareDAO(lConn);
					lMisCautelareDao.setDAOFromModel(lMisCautelareMod);
					BigDecimal lIdMisCau = lMisCautelareDao.insert();
					lMisCautelareDao.stop();
					// fine Inserimento

					lMisCautBdmcMod = new MisuraCautelareBdmcModel();

					lMisCautBdmcMod.setNumAnni(new BigDecimal(lCalPenaEspiata.getNumAnni()));
					lMisCautBdmcMod.setNumGiorni(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
					lMisCautBdmcMod.setNumMesi(new BigDecimal(lCalPenaEspiata.getNumMesi()));

					lMisCautBdmcMod.setProgPeriPres(lPeriPren.getProgPeriPres());
					lMisCautBdmcMod.setIdMisuraCautelare(lIdMisCau);
					lMisCautBdmcMod.setAltroLuogoDetenzione(ProcPena.getDescLuog());
					lMisCautBdmcMod.setIstDetIdIstitutoDetenzione(ProcPena.getCodiIstiPena());
					lMisCautBdmcMod.setAnnoFascSiep(lPeriPren.getAnnoFascSiep());

					if (lPeriPren.getNumeFascSiep() != null) {
						lMisCautBdmcMod.setAnnoFascSiep(lPeriPren.getAnnoFascSiep());
						lMisCautBdmcMod.setNumeFascSiep(lPeriPren.getNumeFascSiep());

					} else {

						lMisCautBdmcMod.setNumeFascSiep(numeroSiep);
						lMisCautBdmcMod.setAnnoFascSiep(annoSiep);

					}
					lMisCautBdmcMod.setAnnoFascBdmc(lPeriPren.getAnnoFascBdmc());
					lMisCautBdmcMod.setNumeFascBdmc(lPeriPren.getNumeFascBdmc());
					lMisCautBdmcMod.setCodUfficioBdmc(lPeriPren.getCodiSedeInst());
					lMisCautBdmcMod.setCodTipoMisura(lMisCautelareMod.getCodTipoMisura());
					lMisCautBdmcMod.setDataFine(lPeriPren.getDataFinePeri());
					lMisCautBdmcMod.setDataInizio(lPeriPren.getDataInizPeri());
					lMisCautBdmcMod.setIdPren(lPeriPren.getIdPren());
					lMisCautBdmcMod.setFlagStato("I");
					lMisCautBdmcMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
					lMisCautBdmcMod.setSogIdSoggetto(lSogIdSoggetto);
					lMisCautBdmcMod.setCodOperatoreInserimento(mUtente);
					lMisCautBdmcMod.setDataInserimento(mData);
					lMisCautBdmcMod.setCodUfficioInserimento(mUfficio);
					lMisCautBdmcMod.setFlagComputabile(lPeriPren.getCodStatPrenPeri());
					lMisCautBdmcMod.setFlagCaricamento("BDMC");
					lMisCautBdmcMod.setStatoTrasmissioneIsc("N");
					lMisCautBdmcMod.setDataFineUsata(lPeriPren.getDataAPeri());
					lMisCautBdmcMod.setDataInizioUsata(lPeriPren.getDataDaPeri());
					lMisCautBdmcDao = new MisuraCautelareBdmcDAO(lConn);
					lMisCautBdmcDao.setDAOFromModel(lMisCautBdmcMod);
					BigDecimal lIdRes = lMisCautBdmcDao.insert();
					lMisCautBdmcDao.stop();

					lMisCautBdmcMod.setIdMisuraCautelare(lIdRes);

					MisCaut.add(lMisCautBdmcMod);
					lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
				}
			} catch (DAOException ex) {
				rollback(lConn);
				ex.printStackTrace();
				lEsiti.add(ex.getMessage());
				throw new SIEPException("ImportaDatiInBDMCController.inserisciPeriPren: " + ex);
			} catch (Exception sqe) {
				rollback(lConn);
				sqe.printStackTrace();
				lEsiti.add(sqe.getMessage());
				throw new F3BException("ImportaDatiInBDMCController.inserisciPeriPren: " + sqe);
			} finally {
				cleanup(lMisCautBdmcDao);
				mEsito.setEsitoPeriPren(lEsiti);
			}
		}
		return MisCaut;
	}

	/**
	 * inserimento dei Reati
	 * 
	 * @param lConn
	 * @param aReati
	 * @param lKeyFascicolo
	 * @throws F3BException
	 */
	private Vector inserisciReati(Connection lConn, Vector aCapoImpu, BigDecimal lKeyFascicolo)
			throws F3BException {
		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSiepDao = null;
		// ReatoCircostanzaModel lReaPrincipale = null;
		Vector lReati = new Vector();
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		try {
			if (aCapoImpu != null && aCapoImpu.size() > 0) {
				// Ci sono dei reati da inserire
				// COntrollo che non ci siano gia' dei reati nel fascicolo da importare
				// Vector lReatiSiep = new Vector();

				lReaSiepDao = new ReatoSqlDAO(lConn);
				int lProgrReatoEsistente = lReaSiepDao.getMaxProgrReato(lKeyFascicolo);
				int lContatoreProgressivo = 0;

				if (lProgrReatoEsistente != 0)
					lContatoreProgressivo = lProgrReatoEsistente;

				Iterator lItx = aCapoImpu.iterator();

				while (lItx.hasNext()) {

					SbViewCapoimpuModel lBDMCCapoimpu = (SbViewCapoimpuModel) lItx.next();
					lReaDao = new ReatoDAO(lConn);
					int lContatoreProgrCircostanze = 0;
					lContatoreProgressivo++;

					// preleva e inserisce i REATI LEGATI A CIASCUN CAPO IMPUTAZIONE
					Vector VSbViewReat = lBDMCCapoimpu.getSbViewReat();
					Iterator lreat = VSbViewReat.iterator();
					String codFonteGiuridica = new String();
					while (lreat.hasNext()) {
						SbViewReatModel lSbViewReat = (SbViewReatModel) lreat.next();
						ReatoModel lReaMod = new ReatoModel();
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");

						lReaMod.setNumero(lSbViewReat.getNumeArtiFont());
						codFonteGiuridica = "-";
						if (lSbViewReat.getCodiFontGiur() != null
								&& lSbViewReat.getCodiFontGiur().length() != 0) {

							if (lSbViewReat.getCodiFontGiur().compareTo("CC") == 0) {
								codFonteGiuridica = "11";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("CP") == 0) {
								codFonteGiuridica = "01";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("LG") == 0) {
								codFonteGiuridica = "05";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("TU") == 0) {
								codFonteGiuridica = "27";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("CM") == 0) {
								codFonteGiuridica = "08";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("CN") == 0) {
								codFonteGiuridica = "07";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("CO") == 0) {
								codFonteGiuridica = "09";
							} else if (lSbViewReat.getCodiFontGiur().compareTo("RD") == 0) {
								codFonteGiuridica = "06";
							}

						}

						lReaMod.setCodFonte(codFonteGiuridica);
						lReaMod.setLettera(lSbViewReat.getLettArtiFont());
						// ??lSbViewReat.getArtiQualFont()
						if (lSbViewReat.getAnnoFontGiur() != null
								&& lSbViewReat.getAnnoFontGiur().intValue() > 0)
							lReaMod.setAnnoFonte(lSbViewReat.getAnnoFontGiur());
						lReaMod.setArticolo(lSbViewReat.getArtiFontGiur().toString());
						if (lSbViewReat.getNumeFontGiur() != null
								&& lSbViewReat.getNumeFontGiur().intValue() > 0)
							lReaMod.setNumeroFonte(lSbViewReat.getNumeFontGiur().toString());
						lReaMod.setComma(lSbViewReat.getCommiArtiFont());
						if (lSbViewReat.getCommiArtiFont() != null
								&& lSbViewReat.getCommiArtiFont().length() > 5)
							lReaMod.setComma(lSbViewReat.getCommiArtiFont().substring(1, 5));
						lReaMod.setDescrFonte(lSbViewReat.getDescriFontGiur());
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");

						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyCirc = lReaDao.insert();
						lReaDao.stop();
						lReaMod.setIdReato(lKeyCirc);
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReati.add(lReaMod);
					}

					// Inserimento circostanze presenti nel CAPO IMPUTAZIONE
					if (lBDMCCapoimpu.getFlagArti0056().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setCodFonte("-");
						lReaMod.setArticolo("56");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0061().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setCodFonte("-");
						lReaMod.setArticolo("61");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						if ((lBDMCCapoimpu.getArti0061Comm() != null)
								&& (lBDMCCapoimpu.getArti0061Comm().length() > 0)) {
							lReaMod.setComma(lBDMCCapoimpu.getArti0061Comm());

						}
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0081().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setCodFonte("-");
						lReaMod.setArticolo("81");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						if ((lBDMCCapoimpu.getArti0081Comm() != null)
								&& (lBDMCCapoimpu.getArti0081Comm().length() > 0)) {
							lReaMod.setComma(lBDMCCapoimpu.getArti0081Comm());

						}
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0112().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setCodFonte("-");
						lReaMod.setArticolo("112");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						if ((lBDMCCapoimpu.getArti0112Commi() != null)
								&& (lBDMCCapoimpu.getArti0112Commi().length() > 0)) {
							lReaMod.setComma(lBDMCCapoimpu.getArti0112Commi());

						}
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArt0110().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setArticolo("110");
						lReaMod.setCodFonte("-");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0113().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setArticolo("113");
						lReaMod.setCodFonte("-");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0114().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setArticolo("114");
						lReaMod.setCodFonte("-");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0116().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setArticolo("116");
						lReaMod.setCodFonte("-");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}
					if (lBDMCCapoimpu.getFlagArti0117().equals("1")) {
						ReatoModel lReaMod = new ReatoModel();
						// campi con -
						lReaMod.setCodTipoReato("-");
						lReaMod.setCodPeriodoConsumazione("-");
						lReaMod.setCodSottonumerazione("-");
						lReaMod.setCodTipoPenaDetentiva("-");
						lReaMod.setCodTipoSanzione("-");
						lReaMod.setArticolo("117");
						lReaMod.setCodFonte("-");
						lReaMod.setProgrReato(new BigDecimal(lContatoreProgressivo));
						lContatoreProgrCircostanze++;
						lReaMod.setProgrCircostanza(new BigDecimal(lContatoreProgrCircostanze));
						lReaMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
						lReaMod.setCodOperatoreInserimento(mUtente);
						lReaMod.setDataInserimento(mData);
						lReaMod.setCodUfficioInserimento(mUfficio);
						lReaDao.setDAOFromModel(lReaMod);
						BigDecimal lKeyReato = lReaDao.insert();
						lReaDao.stop();
						lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
						lReaMod.setIdReato(lKeyReato);
						lReati.add(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug(lReaMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("-------------------------------------------------------------");
					}

				}
			}
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			lEsiti.add(ex.getMessage());
			throw new SIEPException("ImportaDatiInBDMCController.inserisciReati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			lEsiti.add(sqe.getMessage());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("ImportaDatiInBDMCController.inserisciReati: ", sqe);
			throw new F3BException("ImportaDatiInBDMCController.inserisciReati: " + sqe);
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
	private Vector inserisciCircostanze(Connection lConn, Vector aSbProcPena, BigDecimal lKeyFascicolo)
			throws F3BException {
		CircostanzaDAO lCirDao = null;
		CircostanzaModel lCirMod = new CircostanzaModel();
		Vector lCircostanze = new Vector();
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> Sto inserendo le circostanze! <<<");
			lCirDao = new CircostanzaDAO(lConn);

			SbViewProcpenaModel circBDMC = (SbViewProcpenaModel) aSbProcPena.get(0);
			/*
			 * Iterator lItx = aCircostanze.iterator();
			 * 
			 * while (lItx.hasNext()) { BDMCCircostanzaModel lBDMCCirc = (BDMCCircostanzaModel) lItx.next();
			 * 
			 * lCirMod = lBDMCCirc.toCircostanza(); lCirMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
			 * lCirMod.setCodOperatoreInserimento(mUtente); lCirMod.setDataInserimento(mData);
			 * lCirMod.setCodUfficioInserimento(mUfficio);
			 * 
			 * lCirDao.setDAOFromModel(lCirMod); BigDecimal lKey = null; lKey = lCirDao.insert();
			 * lCirMod.setIdCircostanza(lKey); lCirDao.stop(); lCircostanze.add(lCirMod);
			 * 
			 * lEsiti.add(ICostantiSbPren.ESITO_POSITIVO); }
			 */

			if (circBDMC.getFlagArti0089() != null && circBDMC.getFlagArti0089().length() != 0
					&& circBDMC.getFlagArti0089().equals("1")) {
				lCirMod = new CircostanzaModel();
				lCirMod.setArticolo("89");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");

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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0090() != null && circBDMC.getFlagArti0090().length() != 0
					&& circBDMC.getFlagArti0090().equals("1")) {
				lCirMod.setArticolo("90");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0091() != null && circBDMC.getFlagArti0091().length() != 0
					&& circBDMC.getFlagArti0091().equals("1")) {
				lCirMod.setArticolo("91");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0092() != null && circBDMC.getFlagArti0092().length() != 0
					&& circBDMC.getFlagArti0092().equals("1")) {
				lCirMod.setArticolo("92");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0093() != null && circBDMC.getFlagArti0093().length() != 0
					&& circBDMC.getFlagArti0093().equals("1")) {
				lCirMod.setArticolo("93");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0094() != null && circBDMC.getFlagArti0094().length() != 0
					&& circBDMC.getFlagArti0094().equals("1")) {
				lCirMod.setArticolo("94");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0095() != null && circBDMC.getFlagArti0095().length() != 0
					&& circBDMC.getFlagArti0095().equals("1")) {
				lCirMod.setArticolo("95");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0096() != null && circBDMC.getFlagArti0096().length() != 0
					&& circBDMC.getFlagArti0096().equals("1")) {
				lCirMod.setArticolo("96");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0097() != null && circBDMC.getFlagArti0097().length() != 0
					&& circBDMC.getFlagArti0097().equals("1")) {
				lCirMod.setArticolo("97");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0098() != null && circBDMC.getFlagArti0098().length() != 0
					&& circBDMC.getFlagArti0098().equals("1")) {
				lCirMod.setArticolo("98");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti0099() != null && circBDMC.getFlagArti0099().length() != 0
					&& circBDMC.getFlagArti0099().equals("1")) {
				lCirMod.setArticolo("99");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArti62() != null && circBDMC.getFlagArti62().length() != 0
					&& circBDMC.getFlagArti62().equals("1")) {
				if ((circBDMC.getArti0062Comm() != null) && (circBDMC.getArti0062Comm().length() != 0))
					lCirMod.setComma(circBDMC.getArti0062Comm());
				lCirMod.setArticolo("62");
				// Campi default
				lCirMod.setCodFonte("01");
				lCirMod.setCodSottonumerazione("-");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			}
			if (circBDMC.getFlagArt62bi() != null && circBDMC.getFlagArt62bi().length() != 0
					&& circBDMC.getFlagArt62bi().equals("1")) {
				lCirMod.setArticolo("62BI");
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
				lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);

			}

			/*
			 * lCirMod.setFasSieIdFascicoloSiep(lKeyFascicolo); lCirMod.setCodOperatoreInserimento(mUtente);
			 * lCirMod.setDataInserimento(mData); lCirMod.setCodUfficioInserimento(mUfficio);
			 * 
			 * lCirDao.setDAOFromModel(lCirMod); BigDecimal lKey = null; lKey = lCirDao.insert();
			 * lCirMod.setIdCircostanza(lKey); lCirDao.stop(); lCircostanze.add(lCirMod);
			 * 
			 * lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);
			 */

		} catch (DAOException ex) {
			rollback(lConn);
			lEsiti.add(ex.getMessage());
			throw new F3BException("ImportaDatiInBDMCController.inserisciCircostanze: Non posso inserire: "
					+ ex);
		} catch (Exception sqe) {
			rollback(lConn);
			lEsiti.add(sqe.getMessage());
			throw new F3BException("ImportaDatiInBDMCController.inserisciCircostanze: Non posso inserire : "
					+ sqe);
		} finally {
			cleanup(lCirDao);
			mEsito.setEsitoCircostanze(lEsiti);
		}
		return lCircostanze;
	}

	/**
	 * Inserimento della Pena complessiva
	 * 
	 * @param lConn
	 * @param aPenaComplessiva
	 * @param lKeyFascicolo
	 * @throws F3BException
	 */
	private boolean inserisciPenaComplessiva(Connection lConn, Vector aSbProcPena, BigDecimal lKeyFascicolo)
			throws F3BException {
		PenaComplessivaDAO lPenDao = null;
		PenaComplessivaModel lPenMod = new PenaComplessivaModel();
		boolean flagPenaPresente = false;
		;
		//
		// Esito degli inserimenti
		Vector lEsiti = new Vector();
		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(">>> Sto inserendo la pena complessiva! <<<");
			lPenDao = new PenaComplessivaDAO(lConn);
			SbViewProcpenaModel penaBDMC = (SbViewProcpenaModel) aSbProcPena.get(0);

			// ======================================================================
			// Controllo se è presente la pena da appello
			// Controllo se è Arresto o reclusione
			// Carico i campi della tabella pena_complessiva
			// ======================================================================
			if (penaBDMC.getFlagReclArreAppe() != null && penaBDMC.getFlagReclArreAppe().compareTo("R") == 0) {
				if (penaBDMC.getMesiPenaAppe() != null && penaBDMC.getGiorPenaAppe() != null
						&& penaBDMC.getAnniPenaAppe() != null) {
					flagPenaPresente = true;
					lPenMod.setNumMesiReclusione(penaBDMC.getMesiPenaAppe());
					lPenMod.setNumGiorniReclusione(penaBDMC.getGiorPenaAppe());
					lPenMod.setNumAnniReclusione(penaBDMC.getAnniPenaAppe());
				}
			}
			if (penaBDMC.getFlagReclArreAppe() != null && penaBDMC.getFlagReclArreAppe().compareTo("A") == 0) {
				if (penaBDMC.getMesiPenaAppe() != null && penaBDMC.getGiorPenaAppe() != null
						&& penaBDMC.getAnniPenaAppe() != null) {
					flagPenaPresente = true;
					lPenMod.setNumAnniArresto(penaBDMC.getAnniPenaAppe());
					lPenMod.setNumMesiArresto(penaBDMC.getMesiPenaAppe());
					lPenMod.setNumGiorniArresto(penaBDMC.getGiorPenaAppe());
				}
			}

			// ======================================================================
			// Controllo se è presente la pena da dibattimento
			// Controllo se è Arresto o reclusione
			// Carico i campi della tabella pena_complessiva
			// ======================================================================
			if (penaBDMC.getFlagReclArreDiba() != null && penaBDMC.getFlagReclArreDiba().compareTo("R") == 0) {
				if (penaBDMC.getMesiPenaDiba() != null && penaBDMC.getGiorPenaDiba() != null
						&& penaBDMC.getAnniPenaDiba() != null) {
					flagPenaPresente = true;
					lPenMod.setNumMesiReclusione(penaBDMC.getMesiPenaDiba());
					lPenMod.setNumGiorniReclusione(penaBDMC.getGiorPenaDiba());
					lPenMod.setNumAnniReclusione(penaBDMC.getAnniPenaDiba());
				}
			}
			if (penaBDMC.getFlagReclArreDiba() != null && penaBDMC.getFlagReclArreDiba().compareTo("A") == 0) {
				if (penaBDMC.getMesiPenaDiba() != null && penaBDMC.getGiorPenaDiba() != null
						&& penaBDMC.getAnniPenaDiba() != null) {
					flagPenaPresente = true;
					lPenMod.setNumAnniArresto(penaBDMC.getAnniPenaDiba());
					lPenMod.setNumMesiArresto(penaBDMC.getMesiPenaDiba());
					lPenMod.setNumGiorniArresto(penaBDMC.getGiorPenaDiba());
				}
			}

			// ======================================================================
			// Controllo se è presente la pena da Gigu
			// Controllo se è Arresto o reclusione
			// Carico i campi della tabella pena_complessiva
			// ======================================================================
			if (penaBDMC.getFlagReclArreGigu() != null && penaBDMC.getFlagReclArreGigu().compareTo("R") == 0) {
				if (penaBDMC.getMesiPenaGigu() != null && penaBDMC.getGiorPenaGigu() != null
						&& penaBDMC.getAnniPenaGigu() != null) {
					flagPenaPresente = true;
					lPenMod.setNumMesiReclusione(penaBDMC.getMesiPenaGigu());
					lPenMod.setNumGiorniReclusione(penaBDMC.getGiorPenaGigu());
					lPenMod.setNumAnniReclusione(penaBDMC.getAnniPenaGigu());
				}
			}
			if (penaBDMC.getFlagReclArreGigu() != null && penaBDMC.getFlagReclArreGigu().compareTo("A") == 0) {
				if (penaBDMC.getMesiPenaGigu() != null && penaBDMC.getGiorPenaGigu() != null
						&& penaBDMC.getAnniPenaGigu() != null) {
					flagPenaPresente = true;
					lPenMod.setNumAnniArresto(penaBDMC.getAnniPenaGigu());
					lPenMod.setNumMesiArresto(penaBDMC.getMesiPenaGigu());
					lPenMod.setNumGiorniArresto(penaBDMC.getGiorPenaGigu());
				}
			}

			// ======================================================================
			// Se è stata caricata una pena complessiva durante la prenotazine su Bdmc
			// la inserisco nel Db di SIES
			// ======================================================================
			if (flagPenaPresente) {
				// List lList = new ArrayList();
				lPenMod.setFasSieIdFascicoloSiep(lKeyFascicolo);
				lPenMod.setCodTipoPenaDetentiva("-");
				lPenMod.setCodTipoRito("-");
				lPenMod.setCodOperatoreInserimento(mUtente);
				lPenMod.setDataInserimento(mData);
				lPenMod.setCodUfficioInserimento(mUfficio);
				lPenDao.setDAOFromModel(lPenMod);
				// BigDecimal lKeyPenaComplessiva = null;
				/* lKeyPenaComplessiva = */lPenDao.insert();
				// lCtrl.ExInserisciPenaCompSanzioneSostContinuazioni(lPenMod, null, lList);
			}
			lEsiti.add(ICostantiSbPren.ESITO_POSITIVO);

		} catch (DAOException ex) {
			rollback(lConn);
			lEsiti.add(ex.getMessage());
			ex.printStackTrace();
			throw new F3BException(
					"ImportaDatiInBDMCController.inserisciPenaComplessiva: Non posso inserire : " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			lEsiti.add(sqe.getMessage());
			throw new F3BException(
					"ImportaDatiInBDMCController.inserisciPenaComplessiva: Non posso inserire : " + sqe);
		} finally {
			cleanup(lPenDao);
			// mEsito.setEsitoCircostanze(lEsiti);
		}
		return true;
	}

	/**
	 * Fujnzione di eliminazione dei dati BDMC appena importati con successo in SIEP
	 * 
	 * @param idFile
	 * @param lConn
	 * @return vero se sono stai cancellati tutti correttamente - false altrimenti
	 * @throws F3BException
	 */
	private boolean deleteDatiBDMC(BigDecimal aIdPren, Connection lConn) throws F3BException {
		SbPeriprenDAO lPeriprenDAO = null;
		SbPrenDAO lPrenDAO = null;
		SbViewCapoimpuDAO lViewCapoimpuDAO = null;
		SbViewProcpenaDAO lViewProcpenaDAO = null;
		SbViewReatDAO lViewReatDAO = null;

		try {
			lPrenDAO = new SbPrenDAO(lConn);
			lPrenDAO.selCondizioneDelete(aIdPren);
			lPrenDAO.delete();

			lPeriprenDAO = new SbPeriprenDAO(lConn);
			lPeriprenDAO.selCondizioneDelete(aIdPren);
			lPeriprenDAO.delete();

			lViewCapoimpuDAO = new SbViewCapoimpuDAO(lConn);
			lViewCapoimpuDAO.selCondizioneDelete(aIdPren);
			lViewCapoimpuDAO.delete();

			lViewProcpenaDAO = new SbViewProcpenaDAO(lConn);
			lViewProcpenaDAO.selCondizioneDelete(aIdPren);
			lViewProcpenaDAO.delete();

			lViewReatDAO = new SbViewReatDAO(lConn);
			lViewReatDAO.selCondizioneDelete(aIdPren);
			lViewReatDAO.delete();

			return true;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Errore durante la cancellazione dei dati BDMC", ex);
			return false;
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Errore durante la cancellazione dei dati BDMC", sqe);
			return false;
		} finally {

			cleanup(lPeriprenDAO);
			cleanup(lPrenDAO);
			cleanup(lViewCapoimpuDAO);
			cleanup(lViewProcpenaDAO);
			cleanup(lViewReatDAO);
		}
	}

}