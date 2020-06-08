package siap.sius.fascicolo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiusDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.esecuzionemisuraalternativa.dao.EsecuzioneMisuraAlternativaDAO;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaDAO;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaSqlDAO;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreDAO;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreSqlDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.produzioneatti.dao.ParereSqlDAO;
import siap.sius.produzioneatti.model.ParereModel;
import siap.sius.rifasius.dao.RiferimentoFascicoloSiusDAO;
import siap.sius.rifasius.model.RiferimentoFascicoloSiusModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoSqlDAO;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;

/**
 * <p>
 * Title: FascicoloSiusUDSController
 * </p>
 * <p>
 * Description: Classe Controller per FascicoloSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloSiusUDSController extends SiapController implements IFascicoloSiusUDS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca il Fascicolo SIUS in Banca Dati per primary key
	 * <p>
	 *
	 * @param aModel
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExRicercaFascicoloByKey(BigDecimal aIdFascicoloSius) throws F3BException {

		Connection lConn = null;

		FascicoloGPModel lFascicolo = null;
		SoggettoModel lSoggMod = null;

		FascicoloGPSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		TenoreSqlDAO lTenDao = null;
		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;

		try {
			lConn = getDBConnection();

			lFascDao = new FascicoloGPSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lTenDao = new TenoreSqlDAO(lConn);
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);

			lFascDao.ricercaFascicoloByKey(aIdFascicoloSius);
			lFascicolo = (FascicoloGPModel) lFascDao.getModelByKey();
			if (lFascicolo == null)
				throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Fascicolo SIUS non trovato");

			lSoggDao.ricercaSoggettoByKey(lFascicolo.getFascicoloSiusModel().getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggMod);

			// Carico i records eventuali di Tenore
			lTenDao.ricercaTenoreByGeneraleProc(
					lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

			// Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
			Vector lVectTenori = new Vector(lTenDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFascicolo.setTenori(lTenoriModel);
			}

			// Lettura del Magistrato Relatore.
			lMagRelSqlDao.ricercaMagistratoRelatoreCorrenteByFascicolo(
					lFascicolo.getFascicoloSiusModel().getIdFascicoloSius());
			MagistratoRelatoreModel lMagRelMod = new MagistratoRelatoreModel();
			lMagRelMod = (MagistratoRelatoreModel) lMagRelSqlDao.getModelByKey();

			// Caricamento del Magistrato Relatore Cod_Magistrato sull'Autorità Delegata del Generale
			// Procedimento.
			if (lMagRelMod != null)
				lFascicolo.getGeneraleProcedimentoModel()
						.setCodAutoritaDelegata(lMagRelMod.getMagCodMagistrato());

			// Udienza_Procedimento
			if (lFascicolo != null && lFascicolo.getGeneraleProcedimentoModel() != null
					&& lFascicolo.getGeneraleProcedimentoModel().getUdiIdUdienza() != null)
				lFascicolo.setUdiPro(cercaUdiProcAttiva(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), lConn));

		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExRicercaFascicoloByKey: " + dex);
		} catch (SQLException sqlex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExRicercaFascicoloByKey: " + sqlex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lTenDao);
			cleanup(lMagRelSqlDao);
			cleanup(lConn);
		}
		return lFascicolo;
	}

	/**
	 * Inserisce il Fascicolo Sius per l'UDS
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 *            ;
	 * @return aFascicoloGPModel;
	 * @throws F3BException
	 */
	public FascicoloGPModel ExInserisciFascicoloSiusUDS(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato, int aDurataEsitoAnni, int aDurataEsitoMesi, int aDurataEsitoGiorni)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		FascicoloSiusSqlDAO lFasDaoSql = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		GeneraleProcedimentoSqlDAO lGenProDaoSql = null;
		TenoreDAO lTenDao = null;
		MagistratoRelatoreDAO lMagRelDao = null;
		AltraCausaSqlDAO lAltCauSqlDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSiusDAO lResFSiusDao = null;
		// paolo cherubini per supersoggetto 20/07/2009
		SoggettoSqlDAO lSogSqlDao = null;
		SoggettoDAO lSogDao = null;
		SoggettoModel lSoggMod = null;
		ResidenzaDAO lResDao = null;
		// fine paolo

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDaoSql = new FascicoloSiusSqlDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lGenProDaoSql = new GeneraleProcedimentoSqlDAO(lConn);
			lTenDao = new TenoreDAO(lConn);

			// Controllo di esistenza di un Atto con stessi:
			// COD_TIPO_ATTO, DATA_RICHIESTA, COD_TIPO_MITTENTE_ATTO, COD_SEDE_MITTENTE,
			// COD_OGGETTO_PROCEDIMENTO, DATA_ARRIVO_CANCELLERIA.
			// / commentato controllo di esistenza atto simile.
			// / String response = existAttoSius(aFascicoloGPModel.getGeneraleProcedimentoModel(),
			// aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto(),
			// aFascicoloGPModel.getFascicoloSiusModel().getChiaveUfficio(), lConn );
			// / if (response != "")
			// / throw new SIUSException(F3BException.USER_MESSAGE,
			// "Attenzione: Esiste in archivio il procedimento "+response+" con gli stessi estremi atto!");

			lFasDaoSql.getProgressivoFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDaoSql.start();

			BigDecimal lBigDec = null;
			if (lFasDaoSql.next())
				lBigDec = lFasDaoSql.getBigDecimal("aMAX");
			lFasDaoSql.stop();

			if (lBigDec == null)
				lBigDec = new BigDecimal(0);

			// paolo cherubini per supersoggetto 20/07/2009
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.getCountFascicoliPerSoggetto(
					aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
			lSogSqlDao.start();
			lSogSqlDao.next();
			BigDecimal lCount = lSogSqlDao.getBigDecimal("HowManyRecords");
			lSogSqlDao.stop();
			if (lCount.intValue() > 0) {
				// Paolo x SuperSoggetto
				lSogDao = new SoggettoDAO(lConn);
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSogSqlDao.getModelByKey();
				lSoggMod.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lSoggMod.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lSoggMod.setDataInserimento(DateUtils.getSysDate());
				lSoggMod.setCodOperatoreAggiornamento(null);
				lSoggMod.setCodUfficioAggiornamento(null);
				lSoggMod.setDataAggiornamento(null);
				lSoggMod.setIdSoggetto(null);
				lSogDao.setDAOFromModel(lSoggMod);
				BigDecimal lSequence = lSogDao.insert();
				cleanup(lSogDao);
				cleanup(lSogSqlDao);
				aFascicoloGPModel.getFascicoloSiusModel().setSogIdSoggetto(lSequence);
				// fine Paolo x SuperSoggetto
			}
			// fine paolo
			// Setto la ChiaveProgressivo del Model di Fascicolo SIUS con il MAX + 1
			aFascicoloGPModel.getFascicoloSiusModel().setChiaveProgr(new BigDecimal(lBigDec.intValue() + 1));

			// Setto il DAO dal Model ed inserisco il FascicoloSius
			lFasDao.setDAOFromModel(aFascicoloGPModel.getFascicoloSiusModel());
			BigDecimal lChiave = lFasDao.insert();
			aFascicoloGPModel.getFascicoloSiusModel().setIdFascicoloSius(lChiave);

			// Si Utilizza il flag insertEMA_ESS per inserire una Esecuzione M.A. (o S.S.) da soggetto, senza
			// disporre dell'Ordinanza / Evento
			// Il flag viene impostato nel caso in cui sto iscrivendo da soggetto un procedimento di E.M.A. (o
			// di E.S.S.) e sono valorizzati Anno/Progressivo dell'Ordinanza.
			boolean insertEMA_ESS = false;

			// Setto il DAO per GeneraleProcedimento.
			// Se il fascicolo è legato a un procedimento di esecuzione M.A. il progressivo ProgrS1 è già
			// valorizzato;
			// altrimenti viene calcolato in base all'anno/progr/ufficio/registro.
			// Stesso Meccanismo valido per l'esecuzione Sanzioni Sostitutive.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1() == null) {
				// STUB 11/02/2004 ProgrS1 e AnnoS1 di GP vengono altrimenti impostati con i valori di
				// FascicoloSius inserito.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
			}
			// Il flag viene impostato nel caso in cui sto iscrivendo da soggetto un procedimento di E.M.A. e
			// sono valorizzati Anno/Progressivo dell'Ordinanza.
			else {
				insertEMA_ESS = true;
			}
			// Setto l'IdFascicoloSius del Model di Generale Procedimento con il MAX + 1
			aFascicoloGPModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(lChiave);

			// Setto il DAO dal Model ed inserisco il Generale Procedimento
			lGenProDao.setDAOFromModel(aFascicoloGPModel.getGeneraleProcedimentoModel());
			BigDecimal lChiaveGP = lGenProDao.insert();

			aFascicoloGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lChiaveGP);

			// Fase di inserimento per i Tenori
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
				aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(lChiaveGP);
				lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
				lChiave = lTenDao.insert();
				aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
			}

			// Inserimento del RIFERIMENTO_FASCICOLO_SIUS.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				insRiferimentoSius(aFascicoloGPModel, lConn);

			// Aggiornamento eventuale della RICHIESTA CONVERSIONE // 19/02/2009.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE))
				this.updRichiestaConversione(aFascicoloGPModel, lConn);

			// Inserimento del Magistrato Relatore.
			// Caricamento del Magistrato Relatore sull'Autorità Delegata del Generale Procedimento.
			if (!aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata().startsWith("-")) {
				MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
				lMagRelDao = new MagistratoRelatoreDAO(lConn);

				// Dati nuovo Magistrato.
				// lMagistrato.setMagCodMagistrato(aFascicoloGPModel.getTenoreModel().getCodMagistrato());
				lMagistrato.setMagCodMagistrato(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
				lMagistrato.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lMagistrato.setDataInizio(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodRuoloMagistrato("01");
				lMagistrato
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMagistrato.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());

				// Si Esegue l'inserimento del Nuovo Magistrato.
				lMagRelDao.setDAOFromModel(lMagistrato);
				lMagRelDao.insert();
			}

			// Si Effettua la modifica del luogo detenzione o l'inserimento di luogo detenzione x altra causa.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa().length() > 1) {
				// Inserimento del luogo Detenzione a partire da altra causa.
				AltraCausaModel lAltCauModel = new AltraCausaModel();

				lAltCauSqlDao = new AltraCausaSqlDAO(lConn);
				lAltCauSqlDao.ricercaAltraCausaByKey(
						new BigDecimal(aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa()));
				lAltCauModel = (AltraCausaModel) lAltCauSqlDao.getModelByKey();

				if (lAltCauModel == null)
					throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Altra Causa Non trovata");

				// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetDao = new LuogoDetenzioneDAO(lConn);
				lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
				lLuoDetModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lLuoDetModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lLuoDetModel.setDataInizioDetenzione(DateUtils.getSysDate());
				// lLuoDetModel.setDataFineDetenzione(aFascicoloGPModel.getGeneraleProcedimentoModel().getDataFinePena());
				lLuoDetModel.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lLuoDetModel.setAltroLuogo(lAltCauModel.getAltroLuogo());
				lLuoDetModel.setIstDetIdIstitutoDetenzione(lAltCauModel.getIstDetIdIstitutoDetenzione());
				lLuoDetModel.setNote("DETENUTO ALTRA CAUSA");

				lLuoDetDao.setDAOFromModel(lLuoDetModel);
				lChiave = lLuoDetDao.insert();
			} else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione().length() > 1) {
				// Aggiornamento/Inserimento del luogo Detenzione.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
				lLuoDetSqlDao.ricercaLuogoDetenzioneByKey(new BigDecimal(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione()));
				lLuoDetSqlDao.start();
				if (lLuoDetSqlDao.next())
					lLuoDetModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModel();
				else
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Errore: Luogo Detenzione non trovato");

				lLuoDetSqlDao.stop();

				// Se il Luogo detenzione non ha il campo FasSiuIdFascicoloSius impostato, lo aggiorno;
				// se è già stato assegnato ad un Fascicolo Sius, ne inserisco un altro.
				if (lLuoDetModel.getFasSiuIdFascicoloSius() == null) {
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataAggiornamento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);
					lLuoDetDao.setIdLuogoDetenzione(lLuoDetModel.getIdLuogoDetenzione());
					lLuoDetDao.setDAOFromModelForUpdate(lLuoDetModel);
					lLuoDetDao.update();
					lLuoDetDao.stop();
				} else {
					// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lLuoDetModel.setDataAggiornamento(null);
					lLuoDetModel.setCodUfficioAggiornamento("");
					lLuoDetModel.setCodOperatoreAggiornamento("");

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);

					lLuoDetDao.setDAOFromModel(lLuoDetModel);
					lChiave = lLuoDetDao.insert();
					lLuoDetDao.stop();
				}
			}

			// Validazione Residenza SIEP.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaResidenzaByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiepModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiepModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);

				// Validazione Domicilio SIEP.
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaDomicilioByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiepModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiepModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.start();
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						lResFSiusDao.stop();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);
			}
			// In caso di Concessione Misure Alternative (U004), vengono inserite le occorrenze di
			// ESECUZIONE_MISURA_ALTERNATIVA e SCADENZARIO_SIUS.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U004")) {
				// sostituita tutta la parte di Ins. di EMA e Scadenzario con la chiamata a un metodo private.
				if (insertEMA_ESS == true)
					insEMAeScadenzario(aFascicoloGPModel, insertEMA_ESS, lConn);

				// Caso di Inserimento ESECUZIONE_MISURA_ALTERNATIVA - ISCRIZIONE.
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// In caso di Applicazione Sanzioni Sostitutive (U019), vengono inserite le occorrenze di
			// ESECUZIONE_SANZIONE_SOSTITUTIVA.
			else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.equals("U019")) {
				if (insertEMA_ESS == true)
					// insertESS(aFascicoloGPModel, aIdEventoInviato, lConn );
					insertESS(aFascicoloGPModel, aIdEventoInviato, lConn, aDurataEsitoAnni, aDurataEsitoMesi,
							aDurataEsitoGiorni);

				// Update del Generale Procedimento appena inserito nei campi ANNO_S1 & PROGR_S1 poichè in
				// essi hanno "viaggiato" Anno e Numero Ordinanza.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// In caso di Applicazione Misure Sicurezz (U024), vengono inserite le occorrenze di
			// ESECUZIONE_MISURA_SICUREZZA.
			else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.equals("U024")) {
				String oggEsecDaAMS = "";
				if (insertEMA_ESS == true)
					oggEsecDaAMS = insertEMS(aFascicoloGPModel, aIdEventoInviato, lConn, aDurataEsitoAnni,
							aDurataEsitoMesi, aDurataEsitoGiorni);
				// Update del Generale Procedimento appena inserito nei campi ANNO_S1 & PROGR_S1 poichè in
				// essi hanno "viaggiato" Anno e Numero Ordinanza.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				if (oggEsecDaAMS != "") {
					// L'oggetto viene forzato alla misura applicata o ereditata da trasf ems
					// Prima però devo considerare che i tenori sono già inseriti
					// Quindi il primo tenore lo aggiorno alla misura applicata o ereditata da trasf ems
					// mentre gli altri vengono cancellati. (ho la possibilità in modifica di aggiungerne)
					for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
						// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
						if (i == 0) {
							lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
							aFascicoloGPModel.getTenori()[i].setCodOggettoTenore(oggEsecDaAMS);
							lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
							lTenDao.update();
						} else { // Tutti gli altri oggetti vengono eliminati
							lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
							lTenDao.delete();
						}
					}
					/** Anche in caso di nessun tenore inserito, andrebbe forzato un tenore alla misura **/
					if (aFascicoloGPModel.getTenori().length == 0) {
						TenoreModel lTenoriMisura[] = new TenoreModel[1];
						TenoreModel lTenoreMisura = new TenoreModel();
						lTenoreMisura.setCodOggettoTenore(oggEsecDaAMS);
						lTenoreMisura.setGenPridGeneraleProcedimento(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
						// lTenoreMisura[0].setDescrOggettoTenore( "-");
						lTenoreMisura.setCodUfficioInserimento(
								aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
						lTenoreMisura.setCodOperatoreInserimento(
								aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
						lTenoreMisura.setDataInserimento(DateUtils.getSysDate());
						lTenoreMisura.setCodMagistrato(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
						lTenoreMisura.setProgrTenore(new BigDecimal((double) (1)));
						lTenoreMisura.setCodEsitoTenore("-");

						lTenoriMisura[0] = lTenoreMisura;
						aFascicoloGPModel.setTenori(lTenoriMisura);

						lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[0]);
						lTenDao.insert();
					}
					/******************************************************************************************/

				}
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExInserisciFascicoloSiusUDS: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lGenProDao);
			cleanup(lGenProDaoSql);
			cleanup(lTenDao);
			cleanup(lMagRelDao);
			cleanup(lAltCauSqlDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lResSqlDao);
			cleanup(lResFSiusDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSogSqlDao);
			cleanup(lSogDao);
			cleanup(lResDao);

			cleanup(lConn);
		}

		return aFascicoloGPModel;
	}

	/**
	 * Inserisce il Fascicolo Sius per l'UDS
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 *            ;
	 * @return aFascicoloGPModel;
	 * @throws F3BException
	 */
	// public FascicoloGPModel ExInserisciFascicoloSiusUDS (FascicoloGPModel aFascicoloGPModel, String
	// aIdEventoInviato, BigDecimal aIdPadreEsec)
	public FascicoloGPModel ExInserisciFascicoloSiusUDS(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato, BigDecimal aIdPadreEsec, int aDurataEsitoAnni, int aDurataEsitoMesi,
			int aDurataEsitoGiorni) throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		FascicoloSiusSqlDAO lFasDaoSql = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		GeneraleProcedimentoSqlDAO lGenProDaoSql = null;
		TenoreDAO lTenDao = null;
		MagistratoRelatoreDAO lMagRelDao = null;
		AltraCausaSqlDAO lAltCauSqlDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSiusDAO lResFSiusDao = null;
		// paolo cherubini per supersoggetto 20/07/2009
		SoggettoSqlDAO lSogSqlDao = null;
		SoggettoDAO lSogDao = null;
		SoggettoModel lSoggMod = null;
		ResidenzaDAO lResDao = null;
		// fine paolo

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDaoSql = new FascicoloSiusSqlDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lGenProDaoSql = new GeneraleProcedimentoSqlDAO(lConn);
			lTenDao = new TenoreDAO(lConn);

			// Controllo di esistenza di un Atto con stessi:
			// COD_TIPO_ATTO, DATA_RICHIESTA, COD_TIPO_MITTENTE_ATTO, COD_SEDE_MITTENTE,
			// COD_OGGETTO_PROCEDIMENTO, DATA_ARRIVO_CANCELLERIA.
			// / commentato controllo di esistenza atto simile.
			// / String response = existAttoSius(aFascicoloGPModel.getGeneraleProcedimentoModel(),
			// aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto(),
			// aFascicoloGPModel.getFascicoloSiusModel().getChiaveUfficio(), lConn );
			// / if (response != "")
			// / throw new SIUSException(F3BException.USER_MESSAGE,
			// "Attenzione: Esiste in archivio il procedimento "+response+" con gli stessi estremi atto!");

			lFasDaoSql.getProgressivoFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDaoSql.start();

			BigDecimal lBigDec = null;
			if (lFasDaoSql.next())
				lBigDec = lFasDaoSql.getBigDecimal("aMAX");
			lFasDaoSql.stop();

			if (lBigDec == null)
				lBigDec = new BigDecimal(0);

			// paolo cherubini per supersoggetto 20/07/2009
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.getCountFascicoliPerSoggetto(
					aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
			lSogSqlDao.start();
			lSogSqlDao.next();
			BigDecimal lCount = lSogSqlDao.getBigDecimal("HowManyRecords");
			lSogSqlDao.stop();
			if (lCount.intValue() > 0) {
				// Paolo x SuperSoggetto
				lSogDao = new SoggettoDAO(lConn);
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSogSqlDao.getModelByKey();
				lSoggMod.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lSoggMod.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lSoggMod.setDataInserimento(DateUtils.getSysDate());
				lSoggMod.setCodOperatoreAggiornamento(null);
				lSoggMod.setCodUfficioAggiornamento(null);
				lSoggMod.setDataAggiornamento(null);
				lSoggMod.setIdSoggetto(null);
				lSogDao.setDAOFromModel(lSoggMod);
				BigDecimal lSequence = lSogDao.insert();
				cleanup(lSogDao);
				cleanup(lSogSqlDao);
				aFascicoloGPModel.getFascicoloSiusModel().setSogIdSoggetto(lSequence);
				// fine Paolo x SuperSoggetto
			}
			// fine paolo

			// Setto la ChiaveProgressivo del Model di Fascicolo SIUS con il MAX + 1
			aFascicoloGPModel.getFascicoloSiusModel().setChiaveProgr(new BigDecimal(lBigDec.intValue() + 1));

			// Setto il DAO dal Model ed inserisco il FascicoloSius
			lFasDao.setDAOFromModel(aFascicoloGPModel.getFascicoloSiusModel());
			BigDecimal lChiave = lFasDao.insert();
			aFascicoloGPModel.getFascicoloSiusModel().setIdFascicoloSius(lChiave);

			// Si Utilizza il flag insertEMA_ESS per inserire una Esecuzione M.A. (o S.S.) da soggetto, senza
			// disporre dell'Ordinanza / Evento
			// Il flag viene impostato nel caso in cui sto iscrivendo da soggetto un procedimento di E.M.A. (o
			// di E.S.S.) e sono valorizzati Anno/Progressivo dell'Ordinanza.
			boolean insertEMA_ESS = false;

			// Setto il DAO per GeneraleProcedimento.
			// Se il fascicolo è legato a un procedimento di esecuzione MA, SS o MS il progressivo ProgrS1 è
			// già valorizzato;
			// altrimenti viene calcolato in base all'anno/progr/ufficio/registro.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1() == null) {
				// STUB 11/02/2004 ProgrS1 e AnnoS1 di GP vengono altrimenti impostati con i valori di
				// FascicoloSius inserito.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
			}
			// Il flag viene impostato nel caso in cui sto iscrivendo da soggetto un procedimento di EMA, ESS
			// o EMS e sono valorizzati Anno/Progressivo dell'Ordinanza.
			else {
				insertEMA_ESS = true;
			}
			// Setto l'IdFascicoloSius del Model di Generale Procedimento con il MAX + 1
			aFascicoloGPModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(lChiave);

			// Setto il DAO dal Model ed inserisco il Generale Procedimento
			lGenProDao.setDAOFromModel(aFascicoloGPModel.getGeneraleProcedimentoModel());
			BigDecimal lChiaveGP = lGenProDao.insert();

			aFascicoloGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lChiaveGP);

			// Fase di inserimento per i Tenori
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
				aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(lChiaveGP);
				lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
				lChiave = lTenDao.insert();
				aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
			}

			// Inserimento del RIFERIMENTO_FASCICOLO_SIUS.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				insRiferimentoSius(aFascicoloGPModel, lConn);

			// Aggiornamento eventuale della RICHIESTA CONVERSIONE // 19/02/2009.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE))
				this.updRichiestaConversione(aFascicoloGPModel, lConn);

			// Inserimento del Magistrato Relatore.
			// Caricamento del Magistrato Relatore sull'Autorità Delegata del Generale Procedimento.
			if (!aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata().startsWith("-")) {
				MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
				lMagRelDao = new MagistratoRelatoreDAO(lConn);

				// Dati nuovo Magistrato.
				// lMagistrato.setMagCodMagistrato(aFascicoloGPModel.getTenoreModel().getCodMagistrato());
				lMagistrato.setMagCodMagistrato(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
				lMagistrato.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lMagistrato.setDataInizio(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodRuoloMagistrato("01");
				lMagistrato
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMagistrato.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());

				// Si Esegue l'inserimento del Nuovo Magistrato.
				lMagRelDao.setDAOFromModel(lMagistrato);
				lMagRelDao.insert();
			}

			// Si Effettua la modifica del luogo detenzione o l'inserimento di luogo detenzione x altra causa.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa().length() > 1) {
				// Inserimento del luogo Detenzione a partire da altra causa.
				AltraCausaModel lAltCauModel = new AltraCausaModel();

				lAltCauSqlDao = new AltraCausaSqlDAO(lConn);
				lAltCauSqlDao.ricercaAltraCausaByKey(
						new BigDecimal(aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa()));
				lAltCauModel = (AltraCausaModel) lAltCauSqlDao.getModelByKey();

				if (lAltCauModel == null)
					throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Altra Causa Non trovata");

				// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetDao = new LuogoDetenzioneDAO(lConn);
				lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
				lLuoDetModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lLuoDetModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lLuoDetModel.setDataInizioDetenzione(DateUtils.getSysDate());
				// lLuoDetModel.setDataFineDetenzione(aFascicoloGPModel.getGeneraleProcedimentoModel().getDataFinePena());
				lLuoDetModel.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lLuoDetModel.setAltroLuogo(lAltCauModel.getAltroLuogo());
				lLuoDetModel.setIstDetIdIstitutoDetenzione(lAltCauModel.getIstDetIdIstitutoDetenzione());
				lLuoDetModel.setNote("DETENUTO ALTRA CAUSA");

				lLuoDetDao.setDAOFromModel(lLuoDetModel);
				lChiave = lLuoDetDao.insert();
			} else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione().length() > 1) {
				// Aggiornamento/Inserimento del luogo Detenzione.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
				lLuoDetSqlDao.ricercaLuogoDetenzioneByKey(new BigDecimal(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione()));
				lLuoDetSqlDao.start();
				if (lLuoDetSqlDao.next())
					lLuoDetModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModel();
				else
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Errore: Luogo Detenzione non trovato");

				lLuoDetSqlDao.stop();

				// Se il Luogo detenzione non ha il campo FasSiuIdFascicoloSius impostato, lo aggiorno;
				// se è già stato assegnato ad un Fascicolo Sius, ne inserisco un altro.
				if (lLuoDetModel.getFasSiuIdFascicoloSius() == null) {
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataAggiornamento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);
					lLuoDetDao.setIdLuogoDetenzione(lLuoDetModel.getIdLuogoDetenzione());
					lLuoDetDao.setDAOFromModelForUpdate(lLuoDetModel);
					lLuoDetDao.update();
					lLuoDetDao.stop();
				} else {
					// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lLuoDetModel.setDataAggiornamento(null);
					lLuoDetModel.setCodUfficioAggiornamento("");
					lLuoDetModel.setCodOperatoreAggiornamento("");

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);

					lLuoDetDao.setDAOFromModel(lLuoDetModel);
					lChiave = lLuoDetDao.insert();
					lLuoDetDao.stop();
				}
			}

			// Validazione Residenza SIUS Padre per EMA-ESS-EMS
			boolean attivaResidenzaSiep = true;
			boolean attivaDomicilioSiep = true;
			if (aIdPadreEsec != null) {
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaResidenzaByFascicoloSius(aIdPadreEsec);
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiusModel lResFSiusModel = new ResidenzaFascicoloSiusModel();
					lResFSiusModel = lResSqlDao.getModelResidenzaFascicoloSius();
					if (lResFSiusModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiusModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiusModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.setDataInizioValidita(lResFSiusModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiusModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiusModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						cleanup(lResFSiusDao);
						attivaResidenzaSiep = false;
					}
				}
				cleanup(lResSqlDao);

				// Validazione Domicilio SIUS Padre per EMA-ESS
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaDomicilioByFascicoloSius(aIdPadreEsec);
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiusModel lResFSiusModel = new ResidenzaFascicoloSiusModel();
					lResFSiusModel = lResSqlDao.getModelResidenzaFascicoloSius();
					if (lResFSiusModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiusModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiusModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.start();
						lResFSiusDao.setDataInizioValidita(lResFSiusModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiusModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiusModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						lResFSiusDao.stop();
						cleanup(lResFSiusDao);
						attivaDomicilioSiep = false;
					}
				}
				cleanup(lResSqlDao);
			}

			// Validazione Residenza SIEP.
			if ((aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
					&& (attivaResidenzaSiep || attivaDomicilioSiep)) {
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaResidenzaByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						if (attivaResidenzaSiep)
							lResFSiusDao.insert();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);

				// Validazione Domicilio SIEP.
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaDomicilioByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.start();
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						if (attivaDomicilioSiep)
							lResFSiusDao.insert();

						lResFSiusDao.stop();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);
			}

			// In caso di Concessione Misure Alternative (U004), vengono inserite le occorrenze di
			// ESECUZIONE_MISURA_ALTERNATIVA e SCADENZARIO_SIUS.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U004")) {
				// sostituita tutta la parte di Ins. di EMA e Scadenzario con la chiamata a un metodo private.
				if (insertEMA_ESS == true)
					insEMAeScadenzario(aFascicoloGPModel, insertEMA_ESS, lConn);

				// Caso di Inserimento ESECUZIONE_MISURA_ALTERNATIVA - ISCRIZIONE.
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// In caso di Applicazione Sanzioni Sostitutive (U019), vengono inserite le occorrenze di
			// ESECUZIONE_SANZIONE_SOSTITUTIVA.
			else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.equals("U019")) {
				if (insertEMA_ESS == true)
					// insertESS(aFascicoloGPModel, aIdEventoInviato, lConn );
					insertESS(aFascicoloGPModel, aIdEventoInviato, lConn, aDurataEsitoAnni, aDurataEsitoMesi,
							aDurataEsitoGiorni);
				// Update del Generale Procedimento appena inserito nei campi ANNO_S1 & PROGR_S1 poichè in
				// essi hanno "viaggiato" Anno e Numero Ordinanza.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// In caso di Applicazione Misure Sicurezza (U024), vengono inserite le occorrenze di
			// ESECUZIONE_MISURA_SICUREZZA.
			else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.equals("U024")) {
				String oggEsecDaAMS = "";
				if (insertEMA_ESS == true)
					oggEsecDaAMS = insertEMS(aFascicoloGPModel, aIdEventoInviato, lConn, aDurataEsitoAnni,
							aDurataEsitoMesi, aDurataEsitoGiorni);

				// Update del Generale Procedimento appena inserito nei campi ANNO_S1 & PROGR_S1 poichè in
				// essi hanno "viaggiato" Anno e Numero Ordinanza.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				if (oggEsecDaAMS != "") {
					// L'oggetto viene forzato alla misura applicata o ereditata da trasf ems
					// Prima però devo considerare che i tenori sono già inseriti
					// Quindi il primo tenore lo aggiorno alla misura applicata o ereditata da trasf ems
					// mentre gli altri vengono cancellati. (ho la possibilità in modifica di aggiungerne)

					for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
						// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
						if (i == 0) {
							lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
							aFascicoloGPModel.getTenori()[i].setCodOggettoTenore(oggEsecDaAMS);
							lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
							lTenDao.update();
						} else { // Tutti gli altri oggetti vengono eliminati
							lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
							lTenDao.delete();
						}
					}
					/** Anche in caso di nessun tenore inserito, andrebbe forzato un tenore alla misura **/
					if (aFascicoloGPModel.getTenori().length == 0) {
						TenoreModel lTenoriMisura[] = new TenoreModel[1];
						TenoreModel lTenoreMisura = new TenoreModel();
						lTenoreMisura.setCodOggettoTenore(oggEsecDaAMS);
						lTenoreMisura.setGenPridGeneraleProcedimento(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
						// lTenoreMisura[0].setDescrOggettoTenore( "-");
						lTenoreMisura.setCodUfficioInserimento(
								aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
						lTenoreMisura.setCodOperatoreInserimento(
								aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
						lTenoreMisura.setDataInserimento(DateUtils.getSysDate());
						lTenoreMisura.setCodMagistrato(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
						lTenoreMisura.setProgrTenore(new BigDecimal((double) (1)));
						lTenoreMisura.setCodEsitoTenore("-");

						lTenoriMisura[0] = lTenoreMisura;
						aFascicoloGPModel.setTenori(lTenoriMisura);

						lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[0]);
						lTenDao.insert();
					}
					/******************************************************************************************/
				}
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExInserisciFascicoloSiusUDS: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lGenProDao);
			cleanup(lGenProDaoSql);
			cleanup(lTenDao);
			cleanup(lMagRelDao);
			cleanup(lAltCauSqlDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lResSqlDao);
			cleanup(lResFSiusDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSogSqlDao);
			cleanup(lSogDao);
			cleanup(lResDao);

			cleanup(lConn);
		}

		return aFascicoloGPModel;
	}

	/**
	 * Verifica l'esistenza del Procedimento di Esecuzione per Anno/Progressivo/Ufficio/Codice Contenuto/Tipo
	 * Registro/Soggetto
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aCodContenuto
	 * @param aTipoRegistro
	 * @param ufficioUtenteConnesso
	 * @param idSoggetto
	 * @return boolean
	 * @throws F3BException
	 */
	public boolean ExistProcedimentoEsecuzione(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aCodContenuto, String aTipoRegistro, String ufficioUtenteConnesso, BigDecimal idSoggetto)
			throws F3BException {

		Connection lConn = null;

		GeneraleProcedimentoSqlDAO lGPSqlDao = null;
		FascicoloGPSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		String lTipoProcedimento = "";
		try {
			lConn = getDBConnection();

			lGPSqlDao = new GeneraleProcedimentoSqlDAO(lConn);

			// Ricerca del generale procedimento per Progressivo/Anno/Ufficio/Contenuto/Registro.
			if (!lGPSqlDao.ExistProcedimentoEsecuzione(aChiaveAnno, aChiaveProgr, aCodContenuto,
					aTipoRegistro, idSoggetto, ufficioUtenteConnesso)) {
				if (aTipoRegistro.compareTo("S12") == 0)
					lTipoProcedimento = "della Sanzione Sostitutiva";
				if (aTipoRegistro.compareTo("S22") == 0)
					lTipoProcedimento = "della Misura Alternativa";
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Procedimento di Esecuzione " + lTipoProcedimento + " " + aChiaveAnno + "/"
								+ aChiaveProgr + " inesistente o non riferito al soggetto in esame ");
			}

		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExistProcedimentoEsecuzione: " + dex);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lGPSqlDao);

			cleanup(lConn);
		}

		return true;
	}

	/**
	 * Inserisce il Fascicolo Sius per l'ufficio, partendo da un Fascicolo SIUS in ricezione.
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 * @param aIdEventoInviato
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExInserisciFascicoloDaSiusUDS(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato, int aDurataEsitoAnni, int aDurataEsitoMesi, int aDurataEsitoGiorni)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		FascicoloSiusSqlDAO lFasDaoSql = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		GeneraleProcedimentoSqlDAO lGenProDaoSql = null;
		TenoreDAO lTenDao = null;
		EventoSqlDAO lEveSqlDao = null;
		EventoDAO lEveDao = null;
		MagistratoRelatoreDAO lMagRelDao = null;
		AltraCausaSqlDAO lAltCauSqlDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSiusDAO lResFSiusDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EsecuzioneMisuraAlternativaDAO lEsMisAltDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEsSanSosDao = null;
		EsecuzioneMisuraSicurezzaDAO lEsMisSicDao = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseMisSicSqlDao = null;
		// Paolo x SuperSoggetto
		SoggettoDAO lSogDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SoggettoModel lSoggMod = null;
		ResidenzaDAO lResDao = null;
		// fine Paolo x SuperSoggetto

		String codOggettoEsecuzioneAMS = "";

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDaoSql = new FascicoloSiusSqlDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lGenProDaoSql = new GeneraleProcedimentoSqlDAO(lConn);
			lTenDao = new TenoreDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Controllo di esistenza di un Atto con stessi:
			// COD_TIPO_ATTO, DATA_RICHIESTA, COD_TIPO_MITTENTE_ATTO, COD_SEDE_MITTENTE,
			// COD_OGGETTO_PROCEDIMENTO, DATA_ARRIVO_CANCELLERIA.
			// / commentato controllo di esistenza atto simile.
			// / String response = existAttoSius(aFascicoloGPModel.getGeneraleProcedimentoModel(),
			// aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto(),
			// aFascicoloGPModel.getFascicoloSiusModel().getChiaveUfficio(), lConn );
			// / if (response != "")
			// / throw new SIUSException(F3BException.USER_MESSAGE,
			// "Attenzione: Esiste in archivio il procedimento "+response+" con gli stessi estremi atto!");

			// Leggo l'evento Inviato per aggiornarlo.
			lEveSqlDao.ricercaEventoByKeyForTrasmAtti(new BigDecimal(aIdEventoInviato));
			EventoModel aEvento = (EventoModel) lEveSqlDao.getModelByKey();
			if (aEvento == null)
				throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Evento non trovato");

			lFasDaoSql.getProgressivoFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDaoSql.start();

			BigDecimal lBigDec = null;
			if (lFasDaoSql.next())
				lBigDec = lFasDaoSql.getBigDecimal("aMAX");
			lFasDaoSql.stop();

			if (lBigDec == null)
				lBigDec = new BigDecimal(0);

			// Paolo x SuperSoggetto
			lSogDao = new SoggettoDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSoggDao.ricercaSoggettoByKey(aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();
			lSoggMod.setCodOperatoreInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
			lSoggMod.setCodUfficioInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
			lSoggMod.setDataInserimento(DateUtils.getSysDate());
			lSoggMod.setCodOperatoreAggiornamento(null);
			lSoggMod.setCodUfficioAggiornamento(null);
			lSoggMod.setDataAggiornamento(null);
			lSoggMod.setIdSoggetto(null);
			lSogDao.setDAOFromModel(lSoggMod);
			BigDecimal lSequence = lSogDao.insert();
			cleanup(lSogDao);
			cleanup(lSoggDao);
			aFascicoloGPModel.getFascicoloSiusModel().setSogIdSoggetto(lSequence);
			// fine Paolo x SuperSoggetto

			// Setto la ChiaveProgressivo del Model di Fascicolo SIUS con il MAX + 1
			aFascicoloGPModel.getFascicoloSiusModel().setChiaveProgr(new BigDecimal(lBigDec.intValue() + 1));

			// Setto il DAO dal Model ed inserisco il FascicoloSius.
			lFasDao.setDAOFromModel(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDao.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
			BigDecimal lChiave = lFasDao.insert();
			aFascicoloGPModel.getFascicoloSiusModel().setIdFascicoloSius(lChiave);

			// STUB 25/02/2004 Si Aggiorna l'evento precedentemente letto.
			aEvento.setDataRicezioneAtti(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
			aEvento.setFasSiuIdFascicoloSiusDest(
					aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());

			// Si Setta il DAO dal Model ed aggiorno l'Evento.
			lEveDao.setDAOFromModel(aEvento);
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();

			// Setto il DAO per GeneraleProcedimento.
			// Se il fascicolo è legato a un procedimento di esecuzione. il progressivo ProgrS1 è già
			// valorizzato;
			// altrimenti viene calcolato in base all'anno/progr/ufficio/registro.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1() == null) {
				// ProgrS1 e AnnoS1 di GP vengono altrimenti impostati con i valori di FascicoloSius inserito.
				// Commentato il contenuto precedente che calcola in base all'anno/progr/ufficio/registro.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
			}
			// Setto l'IdFascicoloSius del Model di Generale Procedimento con il MAX + 1
			aFascicoloGPModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(lChiave);

			// Setto il DAO dal Model ed inserisco il Generale Procedimento
			lGenProDao.setDAOFromModel(aFascicoloGPModel.getGeneraleProcedimentoModel());
			BigDecimal lChiaveGP = lGenProDao.insert();

			aFascicoloGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lChiaveGP);

			// Fase di inserimento per il Tenore.
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
				aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(lChiaveGP);
				aFascicoloGPModel.getTenori()[i].setProgrTenore(new BigDecimal((double) (i + 1)));
				lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
				lChiave = lTenDao.insert();
				// Att! Impostazione ID tenore aggiunta il 05/05/2011
				// per garantire la possibilità di eventuali cancellazioni per i fascicoli EMS da presa in
				// carico
				// Verificare eventuali effetti collaterali
				aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
			}

			// Inserimento del RIFERIMENTO_FASCICOLO_SIUS.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				insRiferimentoSius(aFascicoloGPModel, lConn);

			// Aggiornamento eventuale della RICHIESTA CONVERSIONE // 19/02/2009.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE))
				this.updRichiestaConversione(aFascicoloGPModel, lConn);

			// Inserimento del Magistrato Relatore appoggiato sull'Autorità Delegata del Generale
			// Procedimento.
			if (!aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata().startsWith("-")) {
				MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
				lMagRelDao = null;
				lMagRelDao = new MagistratoRelatoreDAO(lConn);

				// Dati nuovo Magistrato.
				lMagistrato.setMagCodMagistrato(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
				lMagistrato.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lMagistrato.setDataInizio(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodRuoloMagistrato("01");
				lMagistrato
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMagistrato.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());

				// Si Esegue l'inserimento del Nuovo Magistrato.
				lMagRelDao.setDAOFromModel(lMagistrato);
				lMagRelDao.insert();
			}

			// Si Effettua la modifica del luogo detenzione o l'inserimento di luogo detenzione x altra causa.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa().length() > 1) {
				// Inserimento del luogo Detenzione a partire da altra causa.
				AltraCausaModel lAltCauModel = new AltraCausaModel();

				lAltCauSqlDao = new AltraCausaSqlDAO(lConn);
				lAltCauSqlDao.ricercaAltraCausaByKey(
						new BigDecimal(aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa()));
				lAltCauModel = (AltraCausaModel) lAltCauSqlDao.getModelByKey();

				if (lAltCauModel == null)
					throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Altra Causa Non trovata");

				// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetDao = new LuogoDetenzioneDAO(lConn);
				lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
				lLuoDetModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lLuoDetModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lLuoDetModel.setDataInizioDetenzione(DateUtils.getSysDate());
				lLuoDetModel.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lLuoDetModel.setAltroLuogo(lAltCauModel.getAltroLuogo());
				lLuoDetModel.setIstDetIdIstitutoDetenzione(lAltCauModel.getIstDetIdIstitutoDetenzione());
				lLuoDetModel.setNote("DETENUTO ALTRA CAUSA");

				lLuoDetDao.setDAOFromModel(lLuoDetModel);
				lChiave = lLuoDetDao.insert();
			} else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione().length() > 1) {
				// Aggiornamento/Inserimento del luogo Detenzione.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
				lLuoDetSqlDao.ricercaLuogoDetenzioneByKey(new BigDecimal(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione()));
				lLuoDetSqlDao.start();
				if (lLuoDetSqlDao.next())
					lLuoDetModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModel();
				else
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Errore: Luogo Detenzione non trovato");

				lLuoDetSqlDao.stop();

				// Se il Luogo detenzione non ha il campo FasSiuIdFascicoloSius impostato, lo aggiorno;
				// se è già stato assegnato ad un Fascicolo Sius, ne inserisco un altro.
				if (lLuoDetModel.getFasSiuIdFascicoloSius() == null) {
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataAggiornamento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);
					lLuoDetDao.setIdLuogoDetenzione(lLuoDetModel.getIdLuogoDetenzione());
					lLuoDetDao.setDAOFromModelForUpdate(lLuoDetModel);
					lLuoDetDao.update();
					lLuoDetDao.stop();
				} else {
					// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lLuoDetModel.setDataAggiornamento(null);
					lLuoDetModel.setCodUfficioAggiornamento("");
					lLuoDetModel.setCodOperatoreAggiornamento("");

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);

					lLuoDetDao.setDAOFromModel(lLuoDetModel);
					lChiave = lLuoDetDao.insert();
					lLuoDetDao.stop();
				}
			}

			// Validazione Residenza SIEP.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaResidenzaByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiepModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiepModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);

				// Validazione Domicilio SIEP.
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaDomicilioByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiepModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiepModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.start();
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						lResFSiusDao.stop();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);
			}

			// In caso di Concessione Misure Alternative (U004), vengono inserite le occorrenze di
			// ESECUZIONE_MISURA_ALTERNATIVA e SCADENZARIO_SIUS.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U004")) {
				// Lettura dell'Ordinanza collegata.
				DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aEvento.getIdEvento());
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

				// Lettura dell'Eventuale Decreto collegato.
				DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
				lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
				lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(aEvento.getIdEvento());
				lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

				// Inserimento dell' ESECUZIONE_MISURA_ALTERNATIVA.
				lEsMisAltDao = new EsecuzioneMisuraAlternativaDAO(lConn);
				EsecuzioneMisuraAlternativaModel lMisAltModel = new EsecuzioneMisuraAlternativaModel();

				// Gestione della possibilità dell'EMA per Decreto
				if (lDepOrdMod != null) {
					lMisAltModel.setAnnoS07(lDepOrdMod.getAnnoS3());
					lMisAltModel.setProgrS07(lDepOrdMod.getNumS3());
					lMisAltModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
					if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
						lMisAltModel.setLuogoEsecuzioneMisura(lDepOrdMod.getLuogoSvolgimentoProva());
				} else if (lDepDecMod != null) {
					lMisAltModel.setAnnoS07(lDepDecMod.getAnnoS72());
					lMisAltModel.setProgrS07(lDepDecMod.getNumS72());
					lMisAltModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
					if (lDepDecMod.getLuogoSvolgimentoProva() != null)
						lMisAltModel.setLuogoEsecuzioneMisura(lDepDecMod.getLuogoSvolgimentoProva());
				}

				lMisAltModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMisAltModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lMisAltModel
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMisAltModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lMisAltModel.setDataOrdinanza(aEvento.getDataEmissione());
				lMisAltModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());

				// Valorizzazione dei dati del Mittente dell'atto.
				lMisAltModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lMisAltModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// CodTipoMisura valorizzato con il primo CodOggettoTenore.
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lMisAltModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lMisAltModel.setCodTipoMisura("-");
				// Valorizzazione del codice Ufficio mittente, se presente.
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lMisAltModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				lMisAltModel.setCodTipoMisura(aEvento.getCodMotivo());

				lEsMisAltDao.setDAOFromModel(lMisAltModel);
				lChiave = lEsMisAltDao.insert();

				// Lettura Tabella PARAMETRO x Misura Alternativa.
				ParametroModel lParMod = new ParametroModel();

				lParMod.setNomeParametro("TERMINE SOTTOSCRIZIONE VERBALE M.A.");
				lParMod.setCodUfficioValidita(lMisAltModel.getCodUfficioInserimento());

				Vector lVectPar = null;

				lParDao = new ParametroSqlDAO(lConn);
				lParDao.ricercaParametroScadenzario(lParMod.getNomeParametro(),
						lParMod.getCodUfficioValidita());
				lVectPar = new Vector(lParDao.getModels());

				if (lVectPar.size() < 1)
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Errore: Parametro di Scadenzario mancante! ");

				lParMod = (ParametroModel) lVectPar.get(0);

				// Caso di Inserimento ESECUZIONE_MISURA_ALTERNATIVA - ISCRIZIONE.
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.equals("U004")) {
					aFascicoloGPModel.getGeneraleProcedimentoModel()
							.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
					aFascicoloGPModel.getGeneraleProcedimentoModel()
							.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
					// Set del DAO e aggiornamento del GeneraleProcedimento.
					lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
					lGenProDao.setCondizioneUpdate(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lGenProDao.update();
				}

				// Inserimento scadenzario.
				lScaDao = new ScadenzarioSiusDAO(lConn);
				ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
				lScaMod.setCodTipoScadenzario("70"); // ESECUZIONE MISURA ALTERNATIVA.
				lScaMod.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());

				// Valorizzazione dell'ID_EVENTO, solo se è stato caricato l'evento in precedenza.
				if (aEvento != null && aEvento.getIdEvento() != null)
					lScaMod.setEveIdEvento(aEvento.getIdEvento());

				// MEV_39: MODIFICATA DATA
				lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));

				// Impostazione della Data Fine Scadenza in base al periodo di PARAMETRO.
				lScaMod.setDataFineScadenza(DateUtils.moveDateTo(DateUtils.getSysDate(),
						java.util.Calendar.DAY_OF_MONTH, lParMod.getGiorni().intValue()));
				lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
						java.util.Calendar.MONTH, lParMod.getMesi().intValue()));
				lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
						java.util.Calendar.YEAR, lParMod.getAnni().intValue()));

				lScaMod.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lScaMod.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lScaMod.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());

				lScaDao.setDAOFromModel(lScaMod);
				lScaDao.insert();
				lScaDao.stop();
			}
			// 28/08/2007 In caso di Concessione Sanzioni Sostitutive (U019), viene inserita l'occorrenza di
			// ESECUZIONE_SANZIONE_SOSTITUTIVA.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U019")) {
				// Lettura dell'Ordinanza collegata.
				DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aEvento.getIdEvento());
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

				// Lettura dell'Eventuale Decreto collegato.
				DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
				lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
				lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(aEvento.getIdEvento());
				lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

				// Inserimento dell' ESECUZIONE_SANZIONE_SOSTITUTIVA.
				lEsSanSosDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
				EsecuzioneSanzioneSostitutivaModel lSanSosModel = new EsecuzioneSanzioneSostitutivaModel();
				// Gestione della possibilità dell'ESS per Decreto
				if (lDepOrdMod != null) {
					lSanSosModel.setAnnoS07(lDepOrdMod.getAnnoS3());
					lSanSosModel.setProgrS07(lDepOrdMod.getNumS3());
					lSanSosModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
					if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
						lSanSosModel.setLuogoEsecuzioneSanzione(lDepOrdMod.getLuogoSvolgimentoProva());
					if (lDepOrdMod.getNumGiorniDetenzioneDom() != null)
						lSanSosModel.setNumGiorniSanzione(lDepOrdMod.getNumGiorniDetenzioneDom());
					else if (aDurataEsitoGiorni > 0)
						lSanSosModel.setNumGiorniSanzione(new BigDecimal(aDurataEsitoGiorni));
					if (lDepOrdMod.getNumMesiDetenzioneDom() != null)
						lSanSosModel.setNumMesiSanzione(lDepOrdMod.getNumMesiDetenzioneDom());
					else if (aDurataEsitoMesi > 0)
						lSanSosModel.setNumMesiSanzione(new BigDecimal(aDurataEsitoMesi));
					if (lDepOrdMod.getNumAnniDetenzioneDom() != null)
						lSanSosModel.setNumAnniSanzione(lDepOrdMod.getNumAnniDetenzioneDom());
					else if (aDurataEsitoAnni > 0)
						lSanSosModel.setNumAnniSanzione(new BigDecimal(aDurataEsitoAnni));
				} else if (lDepDecMod != null) {
					lSanSosModel.setAnnoS07(lDepDecMod.getAnnoS72());
					lSanSosModel.setProgrS07(lDepDecMod.getNumS72());
					lSanSosModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
					if (lDepDecMod.getLuogoSvolgimentoProva() != null)
						lSanSosModel.setLuogoEsecuzioneSanzione(lDepDecMod.getLuogoSvolgimentoProva());
				}

				lSanSosModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lSanSosModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lSanSosModel
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lSanSosModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lSanSosModel.setDataOrdinanza(aEvento.getDataEmissione());
				lSanSosModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());

				// Valorizzazione dei dati del Mittente dell'atto.
				lSanSosModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lSanSosModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// CodTipoSanzione valorizzato con il primo CodOggettoTenore.
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lSanSosModel.setCodTipoSanzione(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lSanSosModel.setCodTipoSanzione("-");
				// Valorizzazione del codice Ufficio mittente, se presente.
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lSanSosModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				// 19/03/2015 lSanSosModel.setCodTipoSanzione(aEvento.getCodMotivo());

				lEsSanSosDao.setDAOFromModel(lSanSosModel);
				lChiave = lEsSanSosDao.insert();

				// Caso di Inserimento ESECUZIONE_SANZIONE_SOSTITUTIVA - ISCRIZIONE.
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.equals("U019")) {
					aFascicoloGPModel.getGeneraleProcedimentoModel()
							.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
					aFascicoloGPModel.getGeneraleProcedimentoModel()
							.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
					// Set del DAO e aggiornamento del GeneraleProcedimento.
					lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
					lGenProDao.setCondizioneUpdate(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lGenProDao.update();
				}
			}

			// 02/05/2011 In caso di Applicazione Misure Sicurezza (U024), viene inserita l'occorrenza di
			// ESECUZIONE_MISURA_SICUREZZA.

			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U024")) {
				// Lettura dell'Ordinanza collegata.
				DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aEvento.getIdEvento());
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

				// Lettura dell'Eventuale Decreto collegato.
				DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
				lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
				lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(aEvento.getIdEvento());
				lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

				// INIZIOO 17/01/2020 ---->>> aggiungo per anomalia in fase di inserimento del contenuto U024
				if (lDepOrdMod == null) {
					DepositoOrdinanzaPcModel lDepOrdAMSMod = new DepositoOrdinanzaPcModel();
					if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente() != null)
						lDepOrdAMSMod.setCodUfficioInserimento(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente());
					lDepOrdAMSMod.setAnnoS3(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
					lDepOrdAMSMod.setNumS3(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
					lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByS3(lDepOrdAMSMod);
					lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();
				}
				// FINE 17/01/2020 ---->>> aggiungo per anomalia in fase di inserimento del contenuto U024

				// Inserimento dell' ESECUZIONE_MISURA_SICUREZZA.
				lEsMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
				EsecuzioneMisuraSicurezzaModel lMisSicModel = new EsecuzioneMisuraSicurezzaModel();
				// Gestione della possibilità dell'EMS per Decreto
				if (lDepOrdMod != null) {
					lMisSicModel.setAnnoS07(lDepOrdMod.getAnnoS3());
					lMisSicModel.setProgrS07(lDepOrdMod.getNumS3());
					lMisSicModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
					if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
						lMisSicModel.setLuogoEsecuzioneMisura(lDepOrdMod.getLuogoSvolgimentoProva());
					if (lDepOrdMod.getNumGiorniDetenzioneDom() != null)
						lMisSicModel.setNumGiorniMisura(lDepOrdMod.getNumGiorniDetenzioneDom());
					else if (aDurataEsitoGiorni > 0)
						lMisSicModel.setNumGiorniMisura(new BigDecimal(aDurataEsitoGiorni));
					if (lDepOrdMod.getNumMesiDetenzioneDom() != null)
						lMisSicModel.setNumMesiMisura(lDepOrdMod.getNumMesiDetenzioneDom());
					else if (aDurataEsitoMesi > 0)
						lMisSicModel.setNumMesiMisura(new BigDecimal(aDurataEsitoMesi));
					if (lDepOrdMod.getNumAnniDetenzioneDom() != null)
						lMisSicModel.setNumAnniMisura(lDepOrdMod.getNumAnniDetenzioneDom());
					else if (aDurataEsitoAnni > 0)
						lMisSicModel.setNumAnniMisura(new BigDecimal(aDurataEsitoAnni));

					// L'Ordinanza di partenza può essere di AMS o di EMS-TM
					// Distinguiamo per ordinanza di partenza:
					// Se AMS (MS) -> eredito i quantum dal fascicolo
					// Se EMS (TM) -> eredito i quantum dall'ems collegata all'ordinanza

					boolean isEMSTM = false;
					if ((lDepOrdMod.getCodTipoOrdinanza() != null)
							&& (lDepOrdMod.getCodTipoOrdinanza().equals("TM"))) {
						isEMSTM = true;
					}

					if (!isEMSTM) {

						if (aEvento.getFasSiuIdFascicoloSius() != null) { // Cerco le Misure Applicate

							MisuraSicurezzaSqlDAO lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);
							MisuraSicurezzaModel lMisSicAMS = new MisuraSicurezzaModel();
							lMisSicAMS.setFasSiuIdFascicoloSius(aEvento.getFasSiuIdFascicoloSius());
							lMisSicSqlDao.ricercaMisuraSicurezza(lMisSicAMS);
							// Occore verificare la validità della misura.
							// In attesa delle gestione della data validità si fa il controllo sull'evento
							Collection listaMisureAMS = lMisSicSqlDao.getModels();
							Iterator itAMS = listaMisureAMS.iterator();
							int numMisureAMS = 0;
							while (itAMS.hasNext()) {
								lMisSicAMS = (MisuraSicurezzaModel) itAMS.next();
								if (lMisSicAMS.getEveIdEvento() != null) { // Se c'è l' EVE_ID_EVENTO è una
																			// trasformata, quindi sicuramente
																			// unica
									numMisureAMS = 1;
									break;
								} else
									numMisureAMS += 1; // Se non c'è EVE_ID_EVENTO potrebbe essere non valida,
														// o potrebbero
							}
							if (numMisureAMS == 1) { // Solo nel caso di misura unica eredito i quantum
								if (lMisSicAMS.getNumAnni() != null)
									lMisSicModel.setNumAnniMisura(lMisSicAMS.getNumAnni());
								if (lMisSicAMS.getNumMesi() != null)
									lMisSicModel.setNumMesiMisura(lMisSicAMS.getNumMesi());
								if (lMisSicAMS.getNumGiorni() != null)
									lMisSicModel.setNumGiorniMisura(lMisSicAMS.getNumGiorni());
								// Nel caso di misura unica, l'oggetto di esecuzione è forzato dal tipo misura
								codOggettoEsecuzioneAMS = lMisSicAMS.getCodOggettoEsecuzione();

							}

						}
					} // Chiusura id !isEMSTM
					else { // per il Riesame e la trasformazione misura TM i quantum sono nell'esecuzione
							// legata all'ordinanza
						lEseMisSicSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
						lEseMisSicSqlDao.ricercaEsecuzioneMisuraSicurezzaByIdOrdinanza(
								lDepOrdMod.getIdDepositoOrdinanzaPc());

						// INZIO RISOLUZIONE Ticket#20200109018 — PRESA IN CARICO ATTI PERVENUTI +
						// Ticket#20200124013 — errore iscrizione procedimento
						EsecuzioneMisuraSicurezzaModel lMisSicModelRice = (EsecuzioneMisuraSicurezzaModel) lEseMisSicSqlDao
								.getModelByKey();
						if (lMisSicModelRice != null)
							lMisSicModel = lMisSicModelRice;
						// FINE RISOLUZIONE Ticket#20200109018 — PRESA IN CARICO ATTI PERVENUTI +
						// Ticket#20200124013 — errore iscrizione procedimento

						lMisSicModel.setDepOpidDepositoOrdinanzaPc(null); // Questa misura non è legata
																			// all'ordinanza. Verificare
																			// effetti
						lMisSicModel.setAnnoS07(lDepOrdMod.getAnnoS3()); // Per la misura legata ad ordinanza
																			// EMS-TM
						lMisSicModel.setProgrS07(lDepOrdMod.getNumS3()); // vanno reimpostati AnnoS07 e
																			// ProgrS07
						// In analogia a quanto fatto per AMS
						if (lMisSicModel != null && lMisSicModel.getCodTipoMisura() != null)
							codOggettoEsecuzioneAMS = lMisSicModel.getCodTipoMisura();
					}

				} else if (lDepDecMod != null) {
					lMisSicModel.setAnnoS07(lDepDecMod.getAnnoS72());
					lMisSicModel.setProgrS07(lDepDecMod.getNumS72());
					lMisSicModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
					if (lDepDecMod.getLuogoSvolgimentoProva() != null)
						lMisSicModel.setLuogoEsecuzioneMisura(lDepDecMod.getLuogoSvolgimentoProva());
					// INIZIOO 17/01/2020 ---->>> aggiungo per anomalia in fase di inserimento del contenuto
					// U024
				} else if (lDepOrdMod == null) {
					if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
						lMisSicModel.setCodAutoritaEmittOrd(aFascicoloGPModel.getGeneraleProcedimentoModel()
								.getCodUfficioMittente().trim());
					lMisSicModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
					lMisSicModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
				}
				// FINEE 17/01/2020 ---->>> aggiungo per anomalia in fase di inserimento del contenuto U024

				lMisSicModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMisSicModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lMisSicModel
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMisSicModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lMisSicModel.setDataOrdinanza(aEvento.getDataEmissione());
				lMisSicModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());

				// Valorizzazione dei dati del Mittente dell'atto.
				lMisSicModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lMisSicModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// La misura è sempre l'oggetto del fascicolo di esecuzione
				// 02/05/2011
				if (codOggettoEsecuzioneAMS == "") {
					if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
							&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
						lMisSicModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
					else
						lMisSicModel.setCodTipoMisura("-");
				} else
					lMisSicModel.setCodTipoMisura(codOggettoEsecuzioneAMS); // L'oggetto sarà forzato alla
																			// misura applicata
				// Valorizzazione del codice Ufficio mittente, se presente.
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lMisSicModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				// lMisSicModel.setCodTipoMisura(aEvento.getCodMotivo()); // Il motivo evento è l'oggetto
				// dell'ord di aplicazione!

				lEsMisSicDao.setDAOFromModel(lMisSicModel);
				lChiave = lEsMisSicDao.insert();

				// Caso di Inserimento ESECUZIONE_MISURA_SICUREZZA - ISCRIZIONE.
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.equals("U024")) {
					aFascicoloGPModel.getGeneraleProcedimentoModel()
							.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
					aFascicoloGPModel.getGeneraleProcedimentoModel()
							.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
					if (codOggettoEsecuzioneAMS != "") {
						// L'oggetto viene forzato alla misura applicata.
						// Prima però devo considerare che i tenori sono già inseriti
						// Quindi il primo tenore lo aggiorno alla misura applicata.
						// mentre gli altri vengono cancellati. (ho la possibilità in modifica di aggiungerne)
						for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
							// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
							if (i == 0) {
								lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
								aFascicoloGPModel.getTenori()[i].setCodOggettoTenore(codOggettoEsecuzioneAMS);
								lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
								lTenDao.update();
							} else { // Tutti gli altri oggetti vengono eliminati
								lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
								lTenDao.delete();
							}
						}

					}
					// Set del DAO e aggiornamento del GeneraleProcedimento.
					lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
					lGenProDao.setCondizioneUpdate(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lGenProDao.update();
				}
			}

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExInserisciFascicoloSius: " + ex);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lGenProDao);
			cleanup(lGenProDaoSql);
			cleanup(lTenDao);
			cleanup(lEveSqlDao);
			cleanup(lEveDao);
			cleanup(lMagRelDao);
			cleanup(lAltCauSqlDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lResSqlDao);
			cleanup(lResFSiusDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lEsMisAltDao);
			cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEsSanSosDao); // 19/08/2008
			cleanup(lEsMisSicDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEseMisSicSqlDao);
			cleanup(lSogDao);
			cleanup(lSoggDao);
			cleanup(lResDao);

			cleanup(lConn);
		}
		return aFascicoloGPModel;
	}

	/**
	 * Inserisce il Fascicolo Sius per l'UDS
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 *            ;
	 * @return aFascicoloGPModel;
	 * @throws F3BException
	 */

	public FascicoloGPModel ExInserisciFascicoloSiusUDSManuale(FascicoloGPModel aFascicoloGPModel)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		FascicoloSiusSqlDAO lFasDaoSql = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		GeneraleProcedimentoSqlDAO lGenProDaoSql = null;
		TenoreDAO lTenDao = null;
		MagistratoRelatoreDAO lMagRelDao = null;
		AltraCausaSqlDAO lAltCauSqlDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSiusDAO lResFSiusDao = null;
		// paolo cherubini per supersoggetto 20/07/2009
		SoggettoSqlDAO lSogSqlDao = null;
		SoggettoDAO lSogDao = null;
		SoggettoModel lSoggMod = null;
		ResidenzaDAO lResDao = null;
		// fine paolo

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDaoSql = new FascicoloSiusSqlDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lGenProDaoSql = new GeneraleProcedimentoSqlDAO(lConn);
			lTenDao = new TenoreDAO(lConn);

			// Controllo di esistenza di un Fascicolo con stessi CHIAVE_ANNO e CHIAVE_PROGR:
			String response = existAnnoProgrSius(aFascicoloGPModel.getFascicoloSiusModel(), lConn);
			if (response != "")
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Attenzione: Esiste gia il procedimento " + response + " per questo ufficio!");

			// Controllo di esistenza di un Atto con stessi:
			// COD_TIPO_ATTO, DATA_RICHIESTA, COD_TIPO_MITTENTE_ATTO, COD_SEDE_MITTENTE,
			// COD_OGGETTO_PROCEDIMENTO, DATA_ARRIVO_CANCELLERIA.
			// /response = existAttoSius(aFascicoloGPModel.getGeneraleProcedimentoModel(),
			// aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto(),
			// aFascicoloGPModel.getFascicoloSiusModel().getChiaveUfficio(), lConn );
			// /if (response != "")
			// / throw new SIUSException(F3BException.USER_MESSAGE,
			// "Attenzione: Esiste in archivio il procedimento "+response+" con gli stessi estremi atto!");

			lFasDaoSql.getProgressivoFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDaoSql.start();

			BigDecimal lBigDec = null;
			// if (lFasDaoSql.next() && (lFasDaoSql.getBigDecimal("aMAX") != null) )
			if (lFasDaoSql.next())
				lBigDec = lFasDaoSql.getBigDecimal("aMAX");
			lFasDaoSql.stop();

			// Nell'ambito dell'anno corrente, non si possono inserire manualmente Fascicoli con progressivo
			// maggiore del Max esistente.
			if ((lBigDec != null)
					&& (lBigDec.intValue() < (aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr()
							.intValue()))
					&& (aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno().toString()
							.equals(DateUtils.getYearToString(DateUtils.getSysDate()))))
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Attenzione: Il progressivo SIUS non può essere maggiore di " + lBigDec.toString());

			// paolo cherubini per supersoggetto 20/07/2009
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.getCountFascicoliPerSoggetto(
					aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
			lSogSqlDao.start();
			lSogSqlDao.next();
			BigDecimal lCount = lSogSqlDao.getBigDecimal("HowManyRecords");
			lSogSqlDao.stop();
			if (lCount.intValue() > 0) {
				// Paolo x SuperSoggetto
				lSogDao = new SoggettoDAO(lConn);
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(aFascicoloGPModel.getFascicoloSiusModel().getSogIdSoggetto());
				lSoggMod = (SoggettoModel) lSogSqlDao.getModelByKey();
				lSoggMod.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lSoggMod.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lSoggMod.setDataInserimento(DateUtils.getSysDate());
				lSoggMod.setCodOperatoreAggiornamento(null);
				lSoggMod.setCodUfficioAggiornamento(null);
				lSoggMod.setDataAggiornamento(null);
				lSoggMod.setIdSoggetto(null);
				lSogDao.setDAOFromModel(lSoggMod);
				BigDecimal lSequence = lSogDao.insert();
				cleanup(lSogDao);
				cleanup(lSogSqlDao);
				aFascicoloGPModel.getFascicoloSiusModel().setSogIdSoggetto(lSequence);
				// fine Paolo x SuperSoggetto
			}
			// fine paolo

			// Setto il DAO dal Model ed inserisco il FascicoloSius
			lFasDao.setDAOFromModel(aFascicoloGPModel.getFascicoloSiusModel());
			BigDecimal lChiave = lFasDao.insert();
			aFascicoloGPModel.getFascicoloSiusModel().setIdFascicoloSius(lChiave);

			// Si Utilizza il flag insertEMA_ESS per inserire una Esecuzione M.A. da soggetto, senza disporre
			// dell'Ordinanza / Evento
			// Il flag viene impostato nel caso in cui sto iscrivendo da soggetto un procedimento di E.M.A. e
			// sono valorizzati Anno/Progressivo dell'Ordinanza.
			boolean insertEMA_ESS = false;

			// Setto il DAO per GeneraleProcedimento.
			// Se il fascicolo è legato a un procedimento di esecuzione M.A. il progressivo ProgrS1 è già
			// valorizzato;
			// altrimenti viene calcolato in base all'anno/progr/ufficio/registro.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1() == null) {
				// STUB 11/02/2004 ProgrS1 e AnnoS1 di GP vengono altrimenti impostati con i valori di
				// FascicoloSius inserito.
				// STUB 11/02/2004 Commentato il contenuto precedente che calcola in base
				// all'anno/progr/ufficio/registro.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				/*
				 * lGenProDaoSql.getProgrS1TipoReg(aFascicoloGPModel.getGeneraleProcedimentoModel());
				 * lGenProDaoSql.start();
				 *
				 * //Imposto la sequence per GeneraleProcedimento lBigDec = null; if (lGenProDaoSql.next() )
				 * lBigDec = lGenProDaoSql.getBigDecimal("aMAX"); lGenProDaoSql.stop();
				 *
				 * if (lBigDec == null) lBigDec = new BigDecimal(0);
				 *
				 * //Setto il ProgressivoS1 del Model di Generale Procedimento con il MAX + 1
				 * aFascicoloGPModel.getGeneraleProcedimentoModel().setProgrS1(new
				 * BigDecimal(lBigDec.intValue() + 1) );
				 */
				// Setto l'IdFascicoloSius del Model di Generale Procedimento con il MAX + 1
			}
			// Il flag viene impostato nel caso in cui sto iscrivendo da soggetto un procedimento di E.M.A. e
			// sono valorizzati Anno/Progressivo dell'Ordinanza.
			else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn(">>>>>>>>>>>>> 31/03/2004 insertEMA_ESS = true ");
				insertEMA_ESS = true;
			}
			// Setto l'IdFascicoloSius del Model di Generale Procedimento con il MAX + 1
			aFascicoloGPModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(lChiave);

			// Setto il DAO dal Model ed inserisco il Generale Procedimento
			lGenProDao.setDAOFromModel(aFascicoloGPModel.getGeneraleProcedimentoModel());
			BigDecimal lChiaveGP = lGenProDao.insert();

			aFascicoloGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lChiaveGP);

			// Fase di inserimento per i Tenori
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
				aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(lChiaveGP);
				lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
				lChiave = lTenDao.insert();
				aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
			}

			// Inserimento del RIFERIMENTO_FASCICOLO_SIUS.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				insRiferimentoSius(aFascicoloGPModel, lConn);

			// Aggiornamento eventuale della RICHIESTA CONVERSIONE // 19/02/2009.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE))
				this.updRichiestaConversione(aFascicoloGPModel, lConn);

			// Inserimento del Magistrato Relatore.
			// 18/12/2003 Caricamento del Magistrato Relatore sull'Autorità Delegata del Generale
			// Procedimento.
			if (!aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata().startsWith("-")) {
				MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
				lMagRelDao = new MagistratoRelatoreDAO(lConn);

				// Dati nuovo Magistrato.
				// lMagistrato.setMagCodMagistrato(aFascicoloGPModel.getTenoreModel().getCodMagistrato());
				lMagistrato.setMagCodMagistrato(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
				lMagistrato.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lMagistrato.setDataInizio(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodRuoloMagistrato("01");
				lMagistrato
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMagistrato.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMagistrato.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());

				// Si Esegue l'inserimento del Nuovo Magistrato.
				lMagRelDao.setDAOFromModel(lMagistrato);
				lMagRelDao.insert();
			}

			// Si Effettua la modifica del luogo detenzione o l'inserimento di luogo detenzione x altra causa.
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa().length() > 1) {
				// Inserimento del luogo Detenzione a partire da altra causa.
				AltraCausaModel lAltCauModel = new AltraCausaModel();

				lAltCauSqlDao = new AltraCausaSqlDAO(lConn);
				lAltCauSqlDao.ricercaAltraCausaByKey(
						new BigDecimal(aFascicoloGPModel.getGeneraleProcedimentoModel().getIdAltraCausa()));
				lAltCauModel = (AltraCausaModel) lAltCauSqlDao.getModelByKey();

				if (lAltCauModel == null)
					throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Altra Causa Non trovata");

				// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetDao = new LuogoDetenzioneDAO(lConn);
				lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
				lLuoDetModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lLuoDetModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lLuoDetModel.setDataInizioDetenzione(DateUtils.getSysDate());
				// lLuoDetModel.setDataFineDetenzione(aFascicoloGPModel.getGeneraleProcedimentoModel().getDataFinePena());
				lLuoDetModel.setFasSiuIdFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
				lLuoDetModel.setAltroLuogo(lAltCauModel.getAltroLuogo());
				lLuoDetModel.setIstDetIdIstitutoDetenzione(lAltCauModel.getIstDetIdIstitutoDetenzione());
				lLuoDetModel.setNote("DETENUTO ALTRA CAUSA");

				lLuoDetDao.setDAOFromModel(lLuoDetModel);
				lChiave = lLuoDetDao.insert();
			} else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione() != null
					&& aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione().length() > 1) {
				// Aggiornamento/Inserimento del luogo Detenzione.
				LuogoDetenzioneModel lLuoDetModel = new LuogoDetenzioneModel();
				lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
				lLuoDetSqlDao.ricercaLuogoDetenzioneByKey(new BigDecimal(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdLuogoDetenzione()));
				lLuoDetSqlDao.start();
				if (lLuoDetSqlDao.next())
					lLuoDetModel = (LuogoDetenzioneModel) lLuoDetSqlDao.getModel();
				else
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Errore: Luogo Detenzione non trovato");

				lLuoDetSqlDao.stop();

				// Se il Luogo detenzione non ha il campo FasSiuIdFascicoloSius impostato, lo aggiorno;
				// se è già stato assegnato ad un Fascicolo Sius, ne inserisco un altro.
				if (lLuoDetModel.getFasSiuIdFascicoloSius() == null) {
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataAggiornamento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreAggiornamento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					// lLuoDetModel.setDataFineDetenzione(aFascicoloGPModel.getGeneraleProcedimentoModel().getDataFinePena());

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);
					lLuoDetDao.setIdLuogoDetenzione(lLuoDetModel.getIdLuogoDetenzione());
					lLuoDetDao.setDAOFromModelForUpdate(lLuoDetModel);
					lLuoDetDao.update();
					lLuoDetDao.stop();
				} else {
					// Set dei campi del nuovo record Luogo Detenzione e Inserimento.
					lLuoDetModel.setFasSiuIdFascicoloSius(
							aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lLuoDetModel.setDataInserimento(DateUtils.getSysDate());
					lLuoDetModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lLuoDetModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lLuoDetModel.setDataAggiornamento(null);
					lLuoDetModel.setCodUfficioAggiornamento("");
					lLuoDetModel.setCodOperatoreAggiornamento("");

					lLuoDetDao = new LuogoDetenzioneDAO(lConn);

					lLuoDetDao.setDAOFromModel(lLuoDetModel);
					lChiave = lLuoDetDao.insert();
					lLuoDetDao.stop();
				}
			}

			// Validazione Residenza SIEP.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaResidenzaByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {
						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiepModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiepModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);

				// Validazione Domicilio SIEP.
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaDomicilioByFascicolo(
						aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lResSqlDao.start();
				if (lResSqlDao.next()) {
					ResidenzaFascicoloSiepModel lResFSiepModel = new ResidenzaFascicoloSiepModel();
					lResFSiepModel = lResSqlDao.getModelResidenzaFascicoloSiep();
					if (lResFSiepModel != null) {

						// Paolo x SuperSoggetto
						ResidenzaModel lResMod = new ResidenzaModel();
						lResDao = new ResidenzaDAO(lConn);
						lResMod.setIdResidenza(lResFSiepModel.getResIdResidenza());
						lResSqlDao.ricercaResidenza(lResMod);
						lResMod = (ResidenzaModel) (lResSqlDao.getModelByKey());
						lResDao.setDAOFromModel(lResMod);
						BigDecimal lKey = lResDao.insert();
						lResFSiepModel.setResIdResidenza(lKey);
						cleanup(lResDao);
						// fine Paolo x SuperSoggetto

						lResFSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
						lResFSiusDao.start();
						lResFSiusDao.setDataInizioValidita(lResFSiepModel.getDataInizioValidita());
						lResFSiusDao.setDataFineValidita(lResFSiepModel.getDataFineValidita());
						lResFSiusDao.setResIdResidenza(lResFSiepModel.getResIdResidenza());
						lResFSiusDao.setFasSiuIdFascicoloSius(
								aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lResFSiusDao.insert();
						lResFSiusDao.stop();
						cleanup(lResFSiusDao);
					}
				}
				cleanup(lResSqlDao);
			}

			// In caso di Concessione Misure Alternative (U004), vengono inserite le occorrenze di
			// ESECUZIONE_MISURA_ALTERNATIVA e SCADENZARIO_SIUS.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.warn(">>>>>>>>>>>>> 31/03/2004 inserimento di ESECUZIONE_MISURA_ALTERNATIVA e
			// SCADENZARIO_SIUS. ");
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U004")) {
				// Sostituita tutta la parte di Ins. di EMA e Scadenzario con la chiamata a un metodo private.
				if (insertEMA_ESS == true)
					insEMAeScadenzario(aFascicoloGPModel, insertEMA_ESS, lConn);

				// Caso di Inserimento ESECUZIONE_MISURA_ALTERNATIVA - ISCRIZIONE.
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// In caso di Applicazione Sanzioni Sostitutive (U019), viene inserita l'ccorrenza di
			// ESECUZIONE_SANZIONE_SOSTITUTIVA.
			else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.equals("U019")) {
				if (insertEMA_ESS == true)
					// insertESS(aFascicoloGPModel, null, lConn );
					insertESS(aFascicoloGPModel, null, lConn, 0, 0, 0);

				// Update del Generale Procedimento appena inserito nei campi ANNO_S1 & PROGR_S1 poichè in
				// essi hanno "viaggiato" Anno e Numero Ordinanza.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// In caso di Applicazione Misure Sicurezza (U024), viene inserita l'ccorrenza di
			// ESECUZIONE_MISURA_SICUREZZA.
			else if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.equals("U024")) {
				String oggEsecDaAMS = "";
				if (insertEMA_ESS == true)
					oggEsecDaAMS = insertEMS(aFascicoloGPModel, null, lConn, 0, 0, 0);
				// Update del Generale Procedimento appena inserito nei campi ANNO_S1 & PROGR_S1 poichè in
				// essi hanno "viaggiato" Anno e Numero Ordinanza.
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
				if (oggEsecDaAMS != "") {
					// L'oggetto viene forzato alla misura applicata o ereditata da trasf ems
					// Prima però devo considerare che i tenori sono già inseriti
					// Quindi il primo tenore lo aggiorno alla misura applicata o ereditata da trasf ems
					// mentre gli altri vengono cancellati. (ho la possibilità in modifica di aggiungerne)
					for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
						// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
						if (i == 0) {
							lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
							aFascicoloGPModel.getTenori()[i].setCodOggettoTenore(oggEsecDaAMS);
							lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
							lTenDao.update();
						} else { // Tutti gli altri oggetti vengono eliminati
							lTenDao.setCondizioneUpdate(aFascicoloGPModel.getTenori()[i].getIdTenore());
							lTenDao.delete();
						}
					}
					/** Anche in caso di nessun tenore inserito, andrebbe forzato un tenore alla misura **/
					if (aFascicoloGPModel.getTenori().length == 0) {
						TenoreModel lTenoriMisura[] = new TenoreModel[1];
						TenoreModel lTenoreMisura = new TenoreModel();
						lTenoreMisura.setCodOggettoTenore(oggEsecDaAMS);
						lTenoreMisura.setGenPridGeneraleProcedimento(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
						// lTenoreMisura[0].setDescrOggettoTenore( "-");
						lTenoreMisura.setCodUfficioInserimento(
								aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
						lTenoreMisura.setCodOperatoreInserimento(
								aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
						lTenoreMisura.setDataInserimento(DateUtils.getSysDate());
						lTenoreMisura.setCodMagistrato(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata());
						lTenoreMisura.setProgrTenore(new BigDecimal((double) (1)));
						lTenoreMisura.setCodEsitoTenore("-");

						lTenoriMisura[0] = lTenoreMisura;
						aFascicoloGPModel.setTenori(lTenoriMisura);

						lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[0]);
						lTenDao.insert();
					}
					/******************************************************************************************/

				}
				// Set del DAO e aggiornamento del GeneraleProcedimento.
				lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
				lGenProDao.setCondizioneUpdate(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lGenProDao.update();
			}
			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExInserisciFascicoloSiusUDS: " + ex);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lGenProDao);
			cleanup(lGenProDaoSql);
			cleanup(lTenDao);
			cleanup(lMagRelDao);
			cleanup(lAltCauSqlDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lResSqlDao);
			cleanup(lResFSiusDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSogSqlDao);
			cleanup(lSogDao);
			cleanup(lResDao);

			cleanup(lConn);
		}
		return aFascicoloGPModel;
	}

	// -----------------
	// METODI PRIVATE
	// -----------------
	private void insRiferimentoSius(FascicoloGPModel aFasGPModel, Connection lConn) throws F3BException {

		RiferimentoFascicoloSiusDAO lRFSDao = null;
		if (!aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep().equals(null)) {
			try {
				RiferimentoFascicoloSiusModel lRFSModel = new RiferimentoFascicoloSiusModel();
				lRFSDao = new RiferimentoFascicoloSiusDAO(lConn);
				lRFSDao.setAnnoFascicoloSius(aFasGPModel.getFascicoloSiusModel().getChiaveAnno());
				lRFSDao.setProgrFascicoloSius(aFasGPModel.getFascicoloSiusModel().getChiaveProgr());
				lRFSDao.setCodUffFascicoloSius(aFasGPModel.getFascicoloSiusModel().getChiaveUfficio());
				lRFSDao.setDataRicezione(aFasGPModel.getFascicoloSiusModel().getDataInserimento());
				lRFSDao.setCodOggettoProcedimento(
						aFasGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
				lRFSDao.setFasSieIdFascicoloSiep(
						aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				BigDecimal lChiaveRFS = lRFSDao.insert();
				lRFSModel.setIdRiferimentoFascicoloSius(lChiaveRFS);
			} catch (DAOException daoEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIUSException(F3BException.USER_MESSAGE,
						"FascicoloSiusUDSController.insRiferimentoSius: " + daoEx);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Exception: " + e);
				throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
			} finally {
				cleanup(lRFSDao);
			}
		}
	}

	/**
	 * Metodo di ricerca di un FASCICOLO_SIUS con stessi: CHIAVE_ANNO, CHIAVE_PROGR, CHIAVE_UFFICIO.
	 * <p>
	 *
	 * @param aFSModel
	 * @param lConn
	 * @throws F3BException
	 */
	private String existAnnoProgrSius(FascicoloSiusModel aFSModel, Connection lConn) throws F3BException {

		String response = "";
		FascicoloSiusSqlDAO lFSSqlDao = new FascicoloSiusSqlDAO(lConn);
		try {
			response = lFSSqlDao.ExistAnnoProgrSius(aFSModel.getChiaveAnno(), aFSModel.getChiaveProgr(),
					aFSModel.getChiaveUfficio());
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.existAttoSius: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFSSqlDao);
		}
		return response;
	}

	/**
	 * Metodo di scrittura di ESECUZIONE_MISURA_ALTERNATIVA e SCADENZARIO_SIUS. a partire da
	 * aFascicoloGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void insEMAeScadenzario(FascicoloGPModel aFascicoloGPModel, boolean insertEMA, Connection lConn)
			throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EsecuzioneMisuraAlternativaDAO lEsMisAltDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Se insertEMA=true, va inserita una ESECUZIONE_MISURA_ALTERNATIVA in assenza di ORDINANZA.
			// Altrimenti viene effettuato l'inserimento previsto in presenza dell'ordinanza.
			EsecuzioneMisuraAlternativaModel lMisAltModel = new EsecuzioneMisuraAlternativaModel();
			if (insertEMA == true) {
				// Inserimento dell' ESECUZIONE_MISURA_ALTERNATIVA.
				lEsMisAltDao = new EsecuzioneMisuraAlternativaDAO(lConn);
				lMisAltModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lMisAltModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
				lMisAltModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMisAltModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lMisAltModel
						.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMisAltModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lMisAltModel.setDataOrdinanza(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getDataRichiesta());
				lMisAltModel.setCodAutoritaEmittOrd(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				// Valorizzazione dei dati del Mittente dell'atto.
				lMisAltModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lMisAltModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// CodTipoMisura valorizzato con il primo CodOggettoTenore.
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lMisAltModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lMisAltModel.setCodTipoMisura("-");

				// Valorizzazione del codice Ufficio mittente, se presente.
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lMisAltModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				lMisAltModel.setDepOpidDepositoOrdinanzaPc(null);

				lEsMisAltDao.setDAOFromModel(lMisAltModel);
				BigDecimal lChiave = lEsMisAltDao.insert();
				lMisAltModel.setIdEsecuzioneMisuraAlternati(lChiave);
			} else {
				// Lettura dell'Ordinanza collegata.
				DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
				DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
				EventoModel aEvento = new EventoModel();
				lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByGenProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

				if (lDepOrdMod != null && (lDepOrdMod.getIdEventoGenerato() != null)) {
					// Lettura dell'evento collegato.
					lEveSqlDao = new EventoSqlDAO(lConn);

					lEveSqlDao.ricercaEventoByKeyForTrasmAtti(lDepOrdMod.getIdEventoGenerato());
					aEvento = (EventoModel) lEveSqlDao.getModelByKey();
					if (aEvento == null)
						throw new SIUSException(F3BException.USER_MESSAGE,
								"Errore: Evento dell'Ordinanza non trovato");
				} else // Lettura dell'Eventuale Decreto collegato.
				{
					lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
					lDepDecSqlDao.ricercaDepositoDecretoByIdGenProc(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

					if (lDepDecMod != null && (lDepDecMod.getIdEventoGenerato() != null)) {
						// Lettura dell'evento collegato.
						lEveSqlDao = new EventoSqlDAO(lConn);

						lEveSqlDao.ricercaEventoByKeyForTrasmAtti(lDepDecMod.getIdEventoGenerato());
						aEvento = (EventoModel) lEveSqlDao.getModelByKey();
						if (aEvento == null)
							throw new SIUSException(F3BException.USER_MESSAGE,
									"Errore: Evento del Decreto non trovato");
					}
				}
				if (lDepOrdMod != null || lDepDecMod != null) {
					// Inserimento dell' ESECUZIONE_MISURA_ALTERNATIVA.
					// Dati prelevati dall'Ordinanza o dal Decreto collegato.
					if (lDepOrdMod != null) {
						lEsMisAltDao = new EsecuzioneMisuraAlternativaDAO(lConn);
						lMisAltModel.setAnnoS07(lDepOrdMod.getAnnoS3());
						lMisAltModel.setProgrS07(lDepOrdMod.getNumS3());
						lMisAltModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
						if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
							lMisAltModel.setLuogoEsecuzioneMisura(lDepOrdMod.getLuogoSvolgimentoProva());
					}
					if (lDepDecMod != null) {
						lEsMisAltDao = new EsecuzioneMisuraAlternativaDAO(lConn);
						lMisAltModel.setAnnoS07(lDepDecMod.getAnnoS72());
						lMisAltModel.setProgrS07(lDepDecMod.getNumS72());
						lMisAltModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
						if (lDepDecMod.getLuogoSvolgimentoProva() != null)
							lMisAltModel.setLuogoEsecuzioneMisura(lDepDecMod.getLuogoSvolgimentoProva());
					}

					lMisAltModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lMisAltModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lMisAltModel.setDataInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
					lMisAltModel.setGenPridGeneraleProcedimento(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lMisAltModel.setDataOrdinanza(aEvento.getDataEmissione());
					lMisAltModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());
					lMisAltModel.setCodTipoAutoritaEmittOrd("-");
					lMisAltModel.setCodLuogoAutoritaEmittOrd("-");
					lMisAltModel.setCodTipoMisura(aEvento.getCodMotivo());

					lEsMisAltDao.setDAOFromModel(lMisAltModel);
					BigDecimal lChiave = lEsMisAltDao.insert();
					lMisAltModel.setIdEsecuzioneMisuraAlternati(lChiave);
				}
			}

			// Lettura Tabella PARAMETRO x Misura Alternativa.
			ParametroModel lParMod = new ParametroModel();

			lParMod.setNomeParametro("TERMINE SOTTOSCRIZIONE VERBALE M.A.");
			lParMod.setCodUfficioValidita(lMisAltModel.getCodUfficioInserimento());

			Vector lVectPar = null;

			lParDao = new ParametroSqlDAO(lConn);
			lParDao.ricercaParametroScadenzario(lParMod.getNomeParametro(), lParMod.getCodUfficioValidita());
			lVectPar = new Vector(lParDao.getModels());

			// Segnalazione della mancanza del Parametro.
			if (lVectPar.size() < 1)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore: Parametro di Scadenzario mancante! ");

			lParMod = (ParametroModel) lVectPar.get(0);

			// Inserimento scadenzario.
			lScaDao = new ScadenzarioSiusDAO(lConn);
			ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
			lScaMod.setCodTipoScadenzario("70"); // ESECUZIONE MISURA ALTERNATIVA.
			lScaMod.setFasSiuIdFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));

			// Valorizzazione dell'ID_EVENTO, solo se è stato caricato l'evento in precedenza.
			if (lEveSqlDao != null) {
				EventoModel aEvento = (EventoModel) lEveSqlDao.getModelByKey();
				if (aEvento != null && aEvento.getIdEvento() != null)
					lScaMod.setEveIdEvento(aEvento.getIdEvento());
			}

			// Impostazione della Data Fine Scadenza in base al periodo di PARAMETRO.
			lScaMod.setDataFineScadenza(DateUtils.moveDateTo(DateUtils.getSysDate(),
					java.util.Calendar.DAY_OF_MONTH, lParMod.getGiorni().intValue()));
			lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
					java.util.Calendar.MONTH, lParMod.getMesi().intValue()));
			lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
					java.util.Calendar.YEAR, lParMod.getAnni().intValue()));

			lScaMod.setCodOperatoreInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
			lScaMod.setCodUfficioInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
			lScaMod.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());

			lScaDao.setDAOFromModel(lScaMod);
			lScaDao.insert();
			lScaDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.insEMAeScadenzario: " + daoEx);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lEsMisAltDao);
			cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Metodo di scrittura di ESECUZIONE_SANZIONE_SOSTITUTIVA a partire da aFascicoloGPModel (STUB 08/05/2008
	 * RIVISTO). (STUB 19/08/2008 RIVISTO).
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 * @param aIdEventoInviato
	 * @param lConn
	 * @throws F3BException
	 */
	private void insertESS(FascicoloGPModel aFascicoloGPModel, String aIdEventoInviato, Connection lConn,
			int aDurataEsitoAnni, int aDurataEsitoMesi, int aDurataEsitoGiorni) throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEsSanSosDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Se insertESS=true, va inserita una ESECUZIONE_SANZIONE_SOSTITUTIVA in assenza di ORDINANZA.
			// Altrimenti viene effettuato l'inserimento previsto in presenza dell'ordinanza.
			EsecuzioneSanzioneSostitutivaModel lSanSosModel = new EsecuzioneSanzioneSostitutivaModel();
			// Lettura dell'Ordinanza collegata.
			DepositoOrdinanzaPcModel lDepOrdMod = null;
			DepositoDecretoModel lDepDecMod = null;
			EventoModel aEvento = null;
			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			// 18/08/2008 In Caso di aIdEventoGenerato = null si inserisce l'ESS senza dati di provvedimento
			// origine.
			if (aIdEventoInviato != null) {
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(new BigDecimal(aIdEventoInviato));
				// lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByGenProcedimento(aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

				if (lDepOrdMod != null && (lDepOrdMod.getIdEventoGenerato() != null)) {
					// Lettura dell'evento collegato.
					lEveSqlDao = new EventoSqlDAO(lConn);

					lEveSqlDao.ricercaEventoByKeyForTrasmAtti(lDepOrdMod.getIdEventoGenerato());
					aEvento = (EventoModel) lEveSqlDao.getModelByKey();
					if (aEvento == null)
						throw new F3BException(F3BException.USER_MESSAGE,
								"Errore: Evento dell'Ordinanza non trovato");
				} else // Lettura dell'Eventuale Decreto collegato.
				{
					lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
					// 18/08/2008
					// lDepDecSqlDao.ricercaDepositoDecretoByIdGenProc(aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(new BigDecimal(aIdEventoInviato));
					lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

					if (lDepDecMod != null && (lDepDecMod.getIdEventoGenerato() != null)) {
						// Lettura dell'evento collegato.
						lEveSqlDao = new EventoSqlDAO(lConn);

						lEveSqlDao.ricercaEventoByKeyForTrasmAtti(lDepDecMod.getIdEventoGenerato());
						aEvento = (EventoModel) lEveSqlDao.getModelByKey();
						if (aEvento == null)
							throw new F3BException(F3BException.USER_MESSAGE,
									"Errore: Evento del Decreto non trovato");
					}
				}
			}
			// Inserimento dell' ESECUZIONE_SANZIONE_SOSTITUTIVA.
			// Dati prelevati dall'Ordinanza o dal Decreto collegato.
			if (lDepOrdMod != null) {
				lSanSosModel.setAnnoS07(lDepOrdMod.getAnnoS3());
				lSanSosModel.setProgrS07(lDepOrdMod.getNumS3());
				lSanSosModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
				if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
					lSanSosModel.setLuogoEsecuzioneSanzione(lDepOrdMod.getLuogoSvolgimentoProva());
				if (lDepOrdMod.getNumGiorniDetenzioneDom() != null)
					lSanSosModel.setNumGiorniSanzione(lDepOrdMod.getNumGiorniDetenzioneDom());
				if (lDepOrdMod.getNumMesiDetenzioneDom() != null)
					lSanSosModel.setNumMesiSanzione(lDepOrdMod.getNumMesiDetenzioneDom());
				if (lDepOrdMod.getNumAnniDetenzioneDom() != null)
					lSanSosModel.setNumAnniSanzione(lDepOrdMod.getNumAnniDetenzioneDom());
			}
			if (lDepDecMod != null && lDepDecMod.getIdDepositoDecreto() != null) {
				lSanSosModel.setAnnoS07(lDepDecMod.getAnnoS72());
				lSanSosModel.setProgrS07(lDepDecMod.getNumS72());
				lSanSosModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
				if (lDepDecMod.getLuogoSvolgimentoProva() != null)
					lSanSosModel.setLuogoEsecuzioneSanzione(lDepDecMod.getLuogoSvolgimentoProva());
			}

			if (aEvento != null && aEvento.getIdEvento() != null) {
				lSanSosModel.setDataOrdinanza(aEvento.getDataEmissione());
				lSanSosModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());
				lSanSosModel.setCodTipoSanzione(aEvento.getCodMotivo());
			} else {
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lSanSosModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());
				lSanSosModel.setCodTipoSanzione("-");
				lSanSosModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lSanSosModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());

			}
			lSanSosModel.setCodOperatoreInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
			lSanSosModel.setCodUfficioInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
			lSanSosModel.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
			lSanSosModel.setGenPridGeneraleProcedimento(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lSanSosModel.setCodTipoAutoritaEmittOrd("-");
			lSanSosModel.setCodLuogoAutoritaEmittOrd("-");

			// Solo se aIdEventoInviato è null (proc. collegato) leggo gli esiti singolarmente
			// if ((aIdEventoInviato == null) && ((aDurataEsitoAnni >0) || (aDurataEsitoMesi > 0) ||
			// (aDurataEsitoGiorni > 0))) {
			if ((aDurataEsitoAnni > 0) || (aDurataEsitoMesi > 0) || (aDurataEsitoGiorni > 0)) {

				lSanSosModel.setNumAnniSanzione((new BigDecimal(aDurataEsitoAnni)));
				lSanSosModel.setNumMesiSanzione((new BigDecimal(aDurataEsitoMesi)));
				lSanSosModel.setNumGiorniSanzione((new BigDecimal(aDurataEsitoGiorni)));
			}
			lEsSanSosDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEsSanSosDao.setDAOFromModel(lSanSosModel);

			lEsSanSosDao.setDAOFromModel(lSanSosModel);
			BigDecimal lChiave = lEsSanSosDao.insert();
			lSanSosModel.setIdEsecuzioneSanzioneSost(lChiave);

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.insertESS: " + daoEx);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lEsSanSosDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Metodo di scrittura di ESECUZIONE_MISURA_SICUREZZA a partire da aFascicoloGPModel
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 * @param aIdEventoInviato
	 * @param lConn
	 * @throws F3BException
	 */
	private String insertEMS(FascicoloGPModel aFascicoloGPModel, String aIdEventoInviato, Connection lConn,
			int aDurataEsitoAnni, int aDurataEsitoMesi, int aDurataEsitoGiorni) throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EsecuzioneMisuraSicurezzaDAO lEsMisSicDao = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEseMisSicSqlDao = null;
		MisuraSicurezzaSqlDAO lMisSicSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		String codOggettoEsecuzioneAMS = "";

		try {
			// Se insertEMS=true, va inserita una ESECUZIONE_MISURA_SICUREZZA in assenza di ORDINANZA.
			// Altrimenti viene effettuato l'inserimento previsto in presenza dell'ordinanza.
			EsecuzioneMisuraSicurezzaModel lMisSicModel = new EsecuzioneMisuraSicurezzaModel();
			// Lettura dell'Ordinanza collegata.
			DepositoOrdinanzaPcModel lDepOrdMod = null;
			EventoModel aEvento = null;
			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			// 22/04/2008 In Caso di aIdEventoGenerato = null si inserisce l'EMS senza dati di provvedimento
			// origine.
			if (aIdEventoInviato != null) {
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(new BigDecimal(aIdEventoInviato));
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

				if (lDepOrdMod != null && (lDepOrdMod.getIdEventoGenerato() != null)) {
					// Lettura dell'evento collegato.
					lEveSqlDao = new EventoSqlDAO(lConn);

					lEveSqlDao.ricercaEventoByKeyForTrasmAtti(lDepOrdMod.getIdEventoGenerato());
					aEvento = (EventoModel) lEveSqlDao.getModelByKey();
					if (aEvento == null)
						throw new F3BException(F3BException.USER_MESSAGE,
								"Errore: Evento dell'Ordinanza non trovato");
				}
			}
			// Inserimento dell' ESECUZIONE_MISURA_SICUREZZA.
			// Dati prelevati dall'Ordinanza o dal Decreto collegato.
			// Anzitutto, se non sono presenti i dati dell'ordinanza cerco di recuperarli da S1 dove stanno
			// viaggiando
			// Ovviamente in quel caso, cioè input in maschera l'ufficio è quello corrente

			if (lDepOrdMod == null) {
				DepositoOrdinanzaPcModel lDepOrdAMSMod = new DepositoOrdinanzaPcModel();
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente() != null)
					lDepOrdAMSMod.setCodUfficioInserimento(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente());
				lDepOrdAMSMod.setAnnoS3(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lDepOrdAMSMod.setNumS3(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
				lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByS3(lDepOrdAMSMod);
				lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();
			}

			// L'ordinanza potrebbe essere di AMS (tipo_ordinanza MS) oppure di EMS Riesame (tipo_ordinanza
			// TM)
			if (lDepOrdMod != null) {
				lMisSicModel.setAnnoS07(lDepOrdMod.getAnnoS3());
				lMisSicModel.setProgrS07(lDepOrdMod.getNumS3());
				lMisSicModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
				if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
					lMisSicModel.setLuogoEsecuzioneMisura(lDepOrdMod.getLuogoSvolgimentoProva());

				// 04/05/2011
				// Avendo un'ordinanza AMS (MS) associata, prelevo i quantum dalla misura del fascicolo AMS
				// con il seguente criterio:
				// fascicolo AMS con 1 sola misura -> eredito i quantum dalla misura
				// fascicolo AMS con più misure -> non eredito quantum
				if (lDepOrdMod.getIdEventoGenerato() != null) {
					// Lettura dell'evento collegato all'ordinanza AMS
					EventoSqlDAO lEveOrdAMSSqlDao = new EventoSqlDAO(lConn);
					// FascicoloGPSqlDAO lFasAMSSqlDao = new FascicoloGPSqlDAO(lConn);

					lEveOrdAMSSqlDao.ricercaEventoByKey(lDepOrdMod.getIdEventoGenerato());
					EventoModel aEventoOrdAMS = (EventoModel) lEveOrdAMSSqlDao.getModelByKey();
					if (aEventoOrdAMS == null)
						throw new F3BException(F3BException.USER_MESSAGE,
								"Errore: Evento dell'Ordinanza AMS o EMS-TM non trovato");

					// Distinguiamo per ordinanza di partenza:
					// Se AMS (MS) -> eredito i quantum dal fascicolo
					// Se EMS (TM) -> eredito i quantum dall'ems collegata all'ordinanza

					boolean isEMSTM = false;
					if ((lDepOrdMod.getCodTipoOrdinanza() != null)
							&& (lDepOrdMod.getCodTipoOrdinanza().equals("TM"))) {
						isEMSTM = true;
					}

					if (!isEMSTM) {
						if (aEventoOrdAMS.getFasSiuIdFascicoloSius() != null) {
							lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);
							MisuraSicurezzaModel lMisSicAMS = new MisuraSicurezzaModel();
							lMisSicAMS.setFasSiuIdFascicoloSius(aEventoOrdAMS.getFasSiuIdFascicoloSius());
							lMisSicSqlDao.ricercaMisuraSicurezza(lMisSicAMS);
							// Occore verificare la validità della misura.
							// In attesa delle gestione della data validità si fa il controllo sull'evento
							Collection listaMisureAMS = lMisSicSqlDao.getModels();
							Iterator itAMS = listaMisureAMS.iterator();
							int numMisureAMS = 0;
							while (itAMS.hasNext()) {
								lMisSicAMS = (MisuraSicurezzaModel) itAMS.next();
								if (lMisSicAMS.getEveIdEvento() != null) { // Se c'è l' EVE_ID_EVENTO è una
																			// trasformata, quindi sicuramente
																			// unica
									numMisureAMS = 1;
									break;
								} else
									numMisureAMS += 1; // Se non c'è EVE_ID_EVENTO potrebbe essere non valida,
														// o potrebbero
														// essere più di una
							}
							if (numMisureAMS == 1) { // Solo nel caso di misura unica eredito i quantum
								if (lMisSicAMS.getNumAnni() != null)
									lMisSicModel.setNumAnniMisura(lMisSicAMS.getNumAnni());
								if (lMisSicAMS.getNumMesi() != null)
									lMisSicModel.setNumMesiMisura(lMisSicAMS.getNumMesi());
								if (lMisSicAMS.getNumGiorni() != null)
									lMisSicModel.setNumGiorniMisura(lMisSicAMS.getNumGiorni());
								// Nel caso di misura unica, l'oggetto di esecuzione è forzato dal tipo misura
								codOggettoEsecuzioneAMS = lMisSicAMS.getCodOggettoEsecuzione();
							}
						}
					} // Chiusura id !isEMSTM
					else { // per il Riesame e la trasformazione misura TM i quantum sono nell'esecuzione
							// legata all'ordinanza
						lEseMisSicSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(lConn);
						lEseMisSicSqlDao.ricercaEsecuzioneMisuraSicurezzaByIdOrdinanza(
								lDepOrdMod.getIdDepositoOrdinanzaPc());

						// INZIO RISOLUZIONE Ticket#20200109018 — PRESA IN CARICO ATTI PERVENUTI +
						// Ticket#20200124013 — errore iscrizione procedimento
						EsecuzioneMisuraSicurezzaModel lMisSicModelRice = (EsecuzioneMisuraSicurezzaModel) lEseMisSicSqlDao
								.getModelByKey();
						if (lMisSicModelRice != null)
							lMisSicModel = lMisSicModelRice;
						// FINE RISOLUZIONE Ticket#20200109018 — PRESA IN CARICO ATTI PERVENUTI
						// Ticket#20200124013 — errore iscrizione procedimento

						lMisSicModel.setDepOpidDepositoOrdinanzaPc(null); // Questa misura non è legata
																			// all'ordinanza. Verificare
																			// effetti
						lMisSicModel.setAnnoS07(lDepOrdMod.getAnnoS3()); // Per la misura legata ad ordinanza
																			// EMS-TM
						lMisSicModel.setProgrS07(lDepOrdMod.getNumS3()); // vanno reimpostati AnnoS07 e
																			// ProgrS07
						// In analogia a quanto fatto per AMS
						if (lMisSicModel != null && lMisSicModel.getCodTipoMisura() != null)
							codOggettoEsecuzioneAMS = lMisSicModel.getCodTipoMisura();
					}

				}

				// 04/05/2011 Fine modifica

			}

			if (aEvento != null && aEvento.getIdEvento() != null) {
				lMisSicModel.setDataOrdinanza(aEvento.getDataEmissione());
				lMisSicModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());
				// lMisSicModel.setCodTipoMisura(aEvento.getCodMotivo());
			} else {
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lMisSicModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());
				// lMisSicModel.setCodTipoMisura("-");
				lMisSicModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lMisSicModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());

			}
			lMisSicModel.setCodOperatoreInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
			lMisSicModel.setCodUfficioInserimento(
					aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
			lMisSicModel.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
			lMisSicModel.setGenPridGeneraleProcedimento(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lMisSicModel.setCodTipoAutoritaEmittOrd("-");
			lMisSicModel.setCodLuogoAutoritaEmittOrd("-");

			// La misura se non fosse presente (caso previsto ma al momento impossibile) viene impostata
			// all'oggetto del fascicolo di esecuzione
			// 02/05/2011
			if (codOggettoEsecuzioneAMS == "") {
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lMisSicModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lMisSicModel.setCodTipoMisura("-");
			} else
				lMisSicModel.setCodTipoMisura(codOggettoEsecuzioneAMS); // L'oggetto sarà poi forzato alla
																		// misura applicata
			//

			// Solo se aIdEventoInviato è null (proc. collegato) leggo gli esiti singolarmente
			// if ((aIdEventoInviato == null) && ((aDurataEsitoAnni >0) || (aDurataEsitoMesi > 0) ||
			// (aDurataEsitoGiorni > 0))) {
			if ((aDurataEsitoAnni > 0) || (aDurataEsitoMesi > 0) || (aDurataEsitoGiorni > 0)) {

				lMisSicModel.setNumAnniMisura((new BigDecimal(aDurataEsitoAnni)));
				lMisSicModel.setNumMesiMisura((new BigDecimal(aDurataEsitoMesi)));
				lMisSicModel.setNumGiorniMisura((new BigDecimal(aDurataEsitoGiorni)));
			}
			lEsMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEsMisSicDao.setDAOFromModel(lMisSicModel);

			BigDecimal lChiave = lEsMisSicDao.insert();
			lEsMisSicDao.setIdEsecuzioneMisuraSicurezza(lChiave);

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.insertEMS: " + daoEx);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lMisSicSqlDao);
			cleanup(lEsMisSicDao);
			cleanup(lEseMisSicSqlDao);
			cleanup(lEveSqlDao);
		}
		return codOggettoEsecuzioneAMS;
	}

	public void ExInserisciDefinizioneFascicoloSius(FascicoloGPModel aFasGPMod) throws F3BException {

		Connection lConn = null;
		FascicoloSiusDAO lFasDao = null;
		GeneraleProcedimentoDAO lGenProcDao = null;

		FascicoloSiusModel lFasc = aFasGPMod.getFascicoloSiusModel();
		GeneraleProcedimentoModel lGenProc = aFasGPMod.getGeneraleProcedimentoModel();
		if (lFasc == null || lFasc.getIdFascicoloSius() == null || lGenProc == null
				|| lGenProc.getIdGeneraleProcedimento() == null)
			throw new SIUSException(F3BException.USER_MESSAGE, "Dati Fascicolo non definiti !");

		try {
			lConn = getDBTransaction();
			// Update del FASCICOLO_SIUS
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDao.setCodOperatoreAggiornamento(lFasc.getCodOperatoreAggiornamento());
			lFasDao.setCodUfficioAggiornamento(lFasc.getCodUfficioAggiornamento());
			lFasDao.setDataAggiornamento(lFasc.getDataAggiornamento());
			lFasDao.setCodStatoFascicolo(lFasc.getCodStatoFascicolo());
			lFasDao.setDataDefinizione(lFasc.getDataDefinizione());
			lFasDao.setCondizioneUpdate(lFasc.getIdFascicoloSius());
			lFasDao.update();
			lFasDao.stop();

			// Update del GENERALE_PROCEDIMENTO
			lGenProcDao = new GeneraleProcedimentoDAO(lConn);
			lGenProcDao.setCodOperatoreAggiornamento(lGenProc.getCodOperatoreAggiornamento());
			lGenProcDao.setCodUfficioAggiornamento(lGenProc.getCodUfficioAggiornamento());
			lGenProcDao.setDataAggiornamento(lGenProc.getDataAggiornamento());
			lGenProcDao.setTipoDefinizione(lGenProc.getTipoDefinizione());
			lGenProcDao.setDescrDefinizione(lGenProc.getDescrDefinizione());
			lGenProcDao.setDataDefinizione(lGenProc.getDataDefinizione());
			lGenProcDao.setCondizioneUpdate(lGenProc.getIdGeneraleProcedimento());
			lGenProcDao.update();
			lGenProcDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExInserisciDefinizioneFascicoloSius : " + ex);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExInserisciDefinizioneFascicoloSius : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lGenProcDao);
			cleanup(lConn);
		}
		return;
	}

	// Ricerca della relazione Udiena-Procedimento Fissata o Prefissata
	private UdienzaProcedimentoModel cercaUdiProcAttiva(BigDecimal aKey, Connection aConn) throws Exception {

		UdienzaProcedimentoSqlDAO lUdiDao = null;
		UdienzaProcedimentoModel lUdiMod = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		try {
			lUdiDao = new UdienzaProcedimentoSqlDAO(aConn);
			lUdiDao.ricercaUdienzaProcedimentoByGenProAndFlagRinviata(aKey, "'F','P'");
			lUdiMod = (UdienzaProcedimentoModel) lUdiDao.getModelByKey();
		} catch (Exception e) {
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lUdiDao);
		}
		return lUdiMod;
	}

	/**
	 * Ricerca paginata di "Richieste Parere su Fascicolo Sius"
	 *
	 * @param aFascicoloSius
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di ParereModel
	 * @throws F3BException
	 */
	public Vector ExRicercaPareriPaginata(ParereModel aParereIn, int aPageNum) throws F3BException {

		Connection lConn = null;
		Vector lElencoPareri = new Vector();
		ParereSqlDAO lParDao = null;

		try {
			lConn = getDBConnection();

			lParDao = new ParereSqlDAO(lConn);
			lParDao.ricerca(aParereIn);

			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPageNum > 0)
				lParDao.startPage(aPageNum);
			else
				lParDao.start();

			ParereModel lParere = null;
			while (lParDao.next()) {
				lParere = (ParereModel) lParDao.getModel();
				lElencoPareri.add(lParere);
			}
			lParDao.stop();

			if (lElencoPareri.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusUDSController.ExRicercaPareriPaginata: " + daoEx);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}

		return lElencoPareri;
	}

	/**
	 * Ritorna n.ro di record risultato della ExRicercaPareriPagina
	 *
	 * @param aParereIn
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaPareri(ParereModel aParereIn) throws F3BException {

		Connection lConn = null;
		ParereSqlDAO lParDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lParDao = new ParereSqlDAO(lConn);
			lParDao.ricerca(aParereIn);
			lCont = lParDao.getNumRowsSelected();
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}
		return lCont;
	}

	private void updRichiestaConversione(FascicoloGPModel aFasGPModel, Connection lConn) throws F3BException {

		RichiestaConversioneSqlDAO lRCSqlDAO = null;
		RichiestaConversioneDAO lRCDAO = null;
		Vector lRicConversioni = new Vector();

		if (!aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep().equals(null)) {
			try {
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setFasSieIdFascicoloSiep(
						aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lRCSqlDAO = new RichiestaConversioneSqlDAO(lConn);
				lRCSqlDAO.ricercaRichiestaConversioneByIdFasSIEP(
						aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lRicConversioni = new Vector(lRCSqlDAO.getModels());

				// 30/10/2009 Si gestisce anche il caso di assenza di Richiesta Conversione.
				if (!lRicConversioni.isEmpty()) {
					RichiestaConversioneModel lRC1Model = (RichiestaConversioneModel) lRicConversioni
							.firstElement();

					if (lRC1Model.getFasSiuIdFascicoloSius() != null)
						throw new F3BException(F3BException.USER_MESSAGE,
								"Attenzione! Per il Titolo Esecutivo selezionato risulta già iscritto un Fascicolo di Conversione Pena Pecuniaria");
					else {
						lRCDAO = new RichiestaConversioneDAO(lConn);
						lRCDAO.setFasSiuIdFascicoloSius(
								aFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());
						lRCDAO.setCodOperatoreAggiornamento(
								aFasGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
						lRCDAO.setCodUfficioAggiornamento(
								aFasGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
						lRCDAO.setDataAggiornamento(aFasGPModel.getFascicoloSiusModel().getDataInserimento());
						lRCDAO.setCondizioni(lRCModel);
						lRCDAO.update();
					}
					// inserimento scadenzario.
					lRC1Model.setFasSiuIdFascicoloSius(
							aFasGPModel.getFascicoloSiusModel().getIdFascicoloSius());
					lRC1Model.setCodOperatoreAggiornamento(
							aFasGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lRC1Model.setCodUfficioAggiornamento(
							aFasGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					lRC1Model.setDataAggiornamento(aFasGPModel.getFascicoloSiusModel().getDataInserimento());
					IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
					lCtrlRC.inserimentoScadenzario(lRC1Model, lConn);
				}
			} catch (DAOException daoEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIUSException(F3BException.USER_MESSAGE,
						"FascicoloSiusUDSController.updRichiestaConversione: " + daoEx);
			} catch (F3BException fe) {
				rollback(lConn);
				throw fe;
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Exception: " + e);
				throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
			} finally {
				cleanup(lRCDAO);
				cleanup(lRCSqlDAO);
			}
		}
	}

}