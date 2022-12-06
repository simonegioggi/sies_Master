package siap.sius.fascicolo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.XModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiusDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.SIEPException;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoSqlDAO;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.esecuzionemisuraalternativa.dao.EsecuzioneMisuraAlternativaDAO;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaDAO;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.dao.FascicoloSiusSoggettoSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusCertBlobModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreDAO;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreSqlDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.produzioneatti.dao.ParereSqlDAO;
import siap.sius.produzioneatti.model.ParereModel;
import siap.sius.rifasius.dao.RiferimentoFascicoloSiusDAO;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienzaprocedimento.dao.UdienzaProcedimentoSqlDAO;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * Title: FascicoloSiusController
 * Description: Classe Controller per FascicoloSius
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloSiusController extends SiapController implements IFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// AVVOCATURA: aggiunto logger
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Inserisce il Fascicolo Sius
	 *
	 * @param aFascicoloGPModel
	 * @return aFascicoloGPModel;
	 * @throws F3BException
	 */
	public FascicoloGPModel ExInserisciFascicoloSius(FascicoloGPModel aFascicoloGPModel) throws F3BException {

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

			lFasDaoSql.getProgressivoFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel());

			lFasDaoSql.start();
			BigDecimal lBigDec = new BigDecimal(0);
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

			// 21/07/2004 Modificata la valorizzazione del Progressivo GP, impostato al valore del Fascicolo
			// SIUS inserito.
			aFascicoloGPModel.getGeneraleProcedimentoModel()
					.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
			aFascicoloGPModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(lChiave);

			// Setto il DAO dal Model ed inserisco il Generale Procedimento
			lGenProDao.setDAOFromModel(aFascicoloGPModel.getGeneraleProcedimentoModel());
			BigDecimal lChiaveGP = lGenProDao.insert();

			// Valirizzazione della chiave del GP nel model di ritorno.
			aFascicoloGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lChiaveGP);

			// Fase di inserimento per il Tenore.
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
				aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(lChiaveGP);
				lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
				lChiave = lTenDao.insert();
				aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
			}
			// STUB 22/03/2004 Inserimento del RIFERIMENTO_FASCICOLO_SIUS.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null
					&& (!stressoDistretto(aFascicoloGPModel.getFascicoloSiusModel(), lConn)))

				insRiferimentoSius(aFascicoloGPModel, lConn);

			// 18/12/2003 Inserimento del Magistrato Relatore Cod_Magistrato Appoggiato sull'Autorita'
			// Delegata.
			if (!aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata().startsWith("-")) {
				MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
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
				// 20200125 [SG]: 1. errore su magistrato (intercettare l’errore)!!!
				// try {
				lMagRelDao.insert();
				// } catch (DAOException daoE) {
				// if (daoE.INTEGRITY_CONSTRAINT_VIOLATED)
				// throw new SIUSException(F3BException.USER_MESSAGE,
				// "Attenzione: Magistrato non trovato!");
				// }
			}

			// 12/01/2004 Si Effettua la modifica del luogo detenzione o l'inserimento di luogo detenzione x
			// altra causa.
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
				// STUB 16/03/2004 Aggiornamento/Inserimento del luogo Detenzione.
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

				// STUB 16/03/2004 Se il Luogo detenzione non ha il campo FasSiuIdFascicoloSius impostato, lo
				// aggiorno;
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

			// Validazione Residenza SIUS Padre per Procedimento collegato
			if (aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaResidenzaByFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSiusOrigine());
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
					}
				}
				cleanup(lResSqlDao);

				// Validazione Domicilio SIUS Padre per Procedimento collegato
				lResSqlDao = new ResidenzaSqlDAO(lConn);
				lResSqlDao.ricercaDomicilioByFascicoloSius(
						aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSiusOrigine());
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
					}
				}
				cleanup(lResSqlDao);
			}

			// 15/01/2004 Validazione Residenza SIEP.
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

				// 15/01/2004 Validazione Domicilio SIEP.
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

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExInserisciFascicoloSius: " + ex);
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
	 * Inserisce il Fascicolo Sius partendo da un Fascicolo SIUS.
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 * @param aIdEventoInviato
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExInserisciFascicoloDaSius(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato) throws F3BException {

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
		// Paolo x SuperSoggetto
		SoggettoDAO lSogDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SoggettoModel lSoggMod = null;
		ResidenzaDAO lResDao = null;
		// fine Paolo x SuperSoggetto

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDaoSql = new FascicoloSiusSqlDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lGenProDaoSql = new GeneraleProcedimentoSqlDAO(lConn);
			lTenDao = new TenoreDAO(lConn);
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lAltCauSqlDao = new AltraCausaSqlDAO(lConn);

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

			BigDecimal lChiave = lFasDao.insert();
			aFascicoloGPModel.getFascicoloSiusModel().setIdFascicoloSius(lChiave);

			// 21/07/2004 Modificata la valorizzazione del Progressivo GP, impostato al valore del Fascicolo
			// SIUS inserito.
			aFascicoloGPModel.getGeneraleProcedimentoModel()
					.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
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
			}

			// STUB 22/03/2004 Inserimento del RIFERIMENTO_FASCICOLO_SIUS.
			if (aFascicoloGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null)
				insRiferimentoSius(aFascicoloGPModel, lConn);

			// STUB 23/02/2004 Leggo l'evento Inviato per aggiornarlo.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Id Evento Inviato = " + aIdEventoInviato);
			lEveSqlDao.ricercaEventoByKeyForTrasmAtti(new BigDecimal(aIdEventoInviato));
			EventoModel aEvento = (EventoModel) lEveSqlDao.getModelByKey();
			if (aEvento == null)
				throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Evento non trovato");

			aEvento.setDataRicezioneAtti(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
			aEvento.setFasSiuIdFascicoloSiusDest(
					aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());

			// Setto il DAO dal Model ed aggiorno l'Evento.
			lEveDao.setDAOFromModel(aEvento);
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();

			// 18/12/2003 Inserimento del Magistrato Relatore Cod_Magistrato Appoggiato sull'Autorita'
			// Delegata
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

			// 12/01/2004 Si Effettua la modifica del luogo detenzione o l'inserimento di luogo detenzione x
			// altra causa.
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
				// STUB 16/03/2004 Aggiornamento/Inserimento del luogo Detenzione.
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

				// STUB 16/03/2004 Se il Luogo detenzione non ha il campo FasSiuIdFascicoloSius impostato, lo
				// aggiorno;
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

			// 15/01/2004 Validazione Residenza SIEP.
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

				// 15/01/2004 Validazione Domicilio SIEP.
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

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExInserisciFascicoloDaSius: " + ex);
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
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSogDao);
			cleanup(lSoggDao);
			cleanup(lResDao);

			cleanup(lConn);
		}

		return aFascicoloGPModel;
	}

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

			// 05/11/2003 Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
			Vector lVectTenori = new Vector(lTenDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFascicolo.setTenori(lTenoriModel);
			}
			// 05/11/2003 Fine Rework FascicoloGPModel.

			// 02/10/2003 Lettura del Magistrato Relatore.
			lMagRelSqlDao.ricercaMagistratoRelatoreCorrenteByFascicolo(
					lFascicolo.getFascicoloSiusModel().getIdFascicoloSius());
			MagistratoRelatoreModel lMagRelMod = new MagistratoRelatoreModel();
			lMagRelMod = (MagistratoRelatoreModel) lMagRelSqlDao.getModelByKey();

			// 18/12/2003 Caricamento del Magistrato Relatore Cod_Magistrato sull'Autorità Delegata del
			// Generale Procedimento.
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
					"FascicoloSiusController.ExRicercaFascicoloByKey: " + dex);
		} catch (SQLException sqlex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloByKey: " + sqlex);
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
	 * 20131206 - creazione nuovo metodo di ricerca non paginata, per stampa folglio xls. Ricerca Fascicolo
	 * Sius per Estremi
	 * <p>
	 *
	 * @param aFascModel
	 * @param sTipoAtto
	 * @param aCancAssFascSius
	 * @param aFiltroCollaboratore
	 * @return
	 * @throws F3BException
	 */
	public Collection<FascicoloGPModel> ExRicercaFascicoloSiusByEstremi(FascicoloSiusModel aFascModel,
			String sTipoAtto, CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore)
			throws F3BException {

		Connection lConn = null;
		Collection<FascicoloGPModel> lFascicoli = new ArrayList<>();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		TenoreSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloSiusProcedimentoPerEstremi(aFascModel, sTipoAtto, aCancAssFascSius,
					aFiltroCollaboratore, true);
			lFasSoggDao.start();
			FascicoloGPModel lFascicolo = null;
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelPerEstremi();
				lFascicoli.add(lFascicolo);
			}
			// lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloSiusByEstremi: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * 20131206 - creazione nuovo metodo di ricerca non paginata, per stampa folglio xls. Ricerca Fascicolo
	 * Sius per Estremi
	 *
	 * @param aFascModel
	 * @param sTipoAtto
	 * @param aCancAssFascSius
	 * @param aFiltroCollaboratore
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExReportFascicoloSiusByEstremiXLS(FascicoloSiusModel aFasModel,
			String aTipoAtto, CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore,
			UfficioModel aUfficio, HashMap<String, Object> aParams) throws F3BException {

		// Invocazione metodo estrazione dati.
		Collection<FascicoloGPModel> lElenco = ExRicercaFascicoloSiusByEstremi(aFasModel, aTipoAtto,
				aCancAssFascSius, aFiltroCollaboratore);

		// Preparazione foglio excel.
		HSSFWorkbook lWb = new HSSFWorkbook();
		HSSFSheet lSheet;
		HSSFCellStyle lCellStyleNull;
		HSSFCellStyle lCellStyleCenter;
		HSSFRow lRow;
		Iterator<FascicoloGPModel> lItx;
		int lContatore = 0;
		int lRowCounter = 0;
		String lDatePattern = "dd/MM/yyyy";

		// creazione foglio
		lSheet = lWb.createSheet("Elenco Procedimenti per Estremi Atti");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficio, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		// 20131209 - Magistrato ( prelevare dai dati in sessione )
		if (aParams.get("descMagistrato") != null) {
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Magistrato : " + aParams.get("descMagistrato"),
					lCellStyleNull);
			lRowCounter++;
		}

		// Tipo atto
		if (aParams.get("descrContenuto") != null) {
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Tipo Atto : " + aParams.get("descrContenuto"),
					lCellStyleNull);
			lRowCounter++;
		}

		// Data iscrizione inizio e fine.
		if (aParams.get("dataIscrizioneInizio") != null) {
			lRow = lSheet.createRow(lRowCounter);
			String lTemp = "Data Iscrizione : dal " + aParams.get("dataIscrizioneInizio");
			if (aParams.get("dataIscrizioneFine") != null)
				lTemp += " al " + aParams.get("dataIscrizioneFine");
			HSSFUtils.getInstance().setCell(lRow, 0, lTemp, lCellStyleNull);
			lRowCounter++;
		}

		// Data Atto inizio e fine.
		if (aParams.get("dataAttoInizio") != null) {
			lRow = lSheet.createRow(lRowCounter);
			String lTemp = "Data Atto : dal " + aParams.get("dataAttoInizio");
			if (aParams.get("dataAttoFine") != null)
				lTemp += " al " + aParams.get("dataAttoFine");
			HSSFUtils.getInstance().setCell(lRow, 0, lTemp, lCellStyleNull);
			lRowCounter++;
		}

		// Descrizione Ufficio
		if (aParams.get("descrUfficio") != null) {
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Ufficio : " + aParams.get("descrUfficio"),
					lCellStyleNull);
			lRowCounter++;
		}

		// Data Atto inizio e fine.
		if (aParams.get("dataFinePendenza") != null) {
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, (short) 0,
					"Procedimenti pendenti al : " + aParams.get("dataFinePendenza"), lCellStyleNull);
			lRowCounter++;
		}

		// Data Atto inizio e fine.
		if (aParams.get("dataArrivoInizio") != null) {
			lRow = lSheet.createRow(lRowCounter);
			String lTemp = "Procedimenti con data arrivo in cancelleria : dal "
					+ aParams.get("dataArrivoInizio");
			if (aParams.get("dataArrivoFine") != null)
				lTemp += " al " + aParams.get("dataArrivoFine");
			HSSFUtils.getInstance().setCell(lRow, (short) 0, lTemp, lCellStyleNull);
			lRowCounter++;
		}

		// Data definizione iniziale e finale.
		if (aParams.get("dataDefinizioneIniziale") != null) {
			lRow = lSheet.createRow(lRowCounter);
			String lTemp = "Procedimenti Definiti : dal " + aParams.get("dataDefinizioneIniziale");
			if (aParams.get("dataDefinizioneFinale") != null)
				lTemp += " al " + aParams.get("dataDefinizioneFinale");
			HSSFUtils.getInstance().setCell(lRow, (short) 0, lTemp, lCellStyleNull);
			lRowCounter++;
		}

		// ==========================================================================
		lRow = lSheet.createRow(lRowCounter);

		HSSFUtils.getInstance().setCell(lRow, (short) 0, "", lCellStyleNull);
		lRowCounter += 2;

		// impostazione della larghezza
		// delle colonne
		lSheet.setColumnWidth(0, (short) (10 * 256)); // Prog
		lSheet.setColumnWidth(1, (short) (15 * 256)); // Procedimento SIUS
		lSheet.setColumnWidth(2, (short) (35 * 256)); // Generalità Soggetto
		lSheet.setColumnWidth(3, (short) (15 * 256)); // Data Udienza
		lSheet.setColumnWidth(4, (short) (15 * 256)); // Data Arrivo Cancelleria
		lSheet.setColumnWidth(5, (short) (15 * 256)); // Data Iscrizione.
		lSheet.setColumnWidth(6, (short) (15 * 256)); // Data Definizione.
		lSheet.setColumnWidth(7, (short) (55 * 256)); // Contenuto Atto

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = HSSFUtils.getInstance().getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		// Intestazione colonne
		HSSFUtils.getInstance().setCell(lRow, (short) 0, "Progr.", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 1, "Procedimento SIUS", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 2, "Generalità Soggetto", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 3, "Data Udienza", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 4, "Data Arrivo Cancelleria", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 5, "Data Iscrizione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 6, "Data Definizione", lCellStyleCenter);
		HSSFUtils.getInstance().setCell(lRow, (short) 7, "Contenuto", lCellStyleCenter);

		lItx = lElenco.iterator();
		// inizio ciclo di scrittura dei dati

		while (lItx.hasNext()) {
			lContatore++;
			FascicoloGPModel lModel = lItx.next();
			SoggettoModel lSoggettoModel = lModel.getFascicoloSiusModel().getSoggetto();
			lRow = lSheet.createRow(lRowCounter++);

			HSSFUtils.getInstance().setCell(lRow, (short) 0, "" + lContatore, lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 1, lModel.getFascicoloSiusModel().getChiaveAnno()
					+ "/" + lModel.getFascicoloSiusModel().getChiaveProgr(), lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 2,
					lSoggettoModel.getCognome() + " " + lSoggettoModel.getNome(), lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 3,
					StringUtils.cStrForJS(DateUtils.getDateToString(
							lModel.getGeneraleProcedimentoModel().getDataCameraConsiglio(), lDatePattern)),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 4,
					StringUtils.cStrForJS(DateUtils.getDateToString(
							lModel.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), lDatePattern)),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 5,
					StringUtils.cStrForJS(DateUtils.getDateToString(
							lModel.getFascicoloSiusModel().getDataIscrizione(), lDatePattern)),
					lCellStyleCenter);

			// 20131208 - Riuso della logica di associazione della di DataDefinizione che per determinate
			// condizione
			// si recupera nella DataDefinizioneFinale.
			// ( logica ereditata da funzione già realizzata per la pagina di elenco ).
			String lDataDefinizione = null;
			if (lModel.getFascicoloSiusModel().getDataDefinizione() != null)
				lDataDefinizione = DateUtils
						.getDateToString(lModel.getFascicoloSiusModel().getDataDefinizione(), lDatePattern);
			else
				lDataDefinizione = DateUtils.getDateToString(
						lModel.getFascicoloSiusModel().getDataDefinizioneFinale(), lDatePattern);

			HSSFUtils.getInstance().setCell(lRow, (short) 6, StringUtils.cStrForJS(lDataDefinizione),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 7,
					lModel.getGeneraleProcedimentoModel().getDescrOggettoProcedimento(), lCellStyleCenter);

		}

		// Generazione foglio excel e ineristo by referece nell'oggetto BAOS
		ByteArrayOutputStream lBAOS = new ByteArrayOutputStream();
		try {
			lWb.write(lBAOS);
		} catch (IOException e) {
			e.printStackTrace();
			throw new F3BException(e);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("######### creaFoglioElencoProc STOP ########");
		return lBAOS;
	}

	/**
	 * Ricerca paginata Fascicolo Sius per Estremi.
	 * <p>
	 *
	 * @param aFascicoloSius
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiusByEstremiPagina(FascicoloSiusModel aFascModel, String sTipoAtto,
			CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		TenoreSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloSiusProcedimentoPerEstremi(aFascModel, sTipoAtto, aCancAssFascSius,
					aFiltroCollaboratore, false);

			lFasSoggDao.startPage(aPage);

			FascicoloGPModel lFascicolo = null;

			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelPerEstremi();
				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloSiusByEstremiPagina: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una RicercaFascicoloSiusByEstremi
	 *
	 * @param aFascicoloSius
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoloSiusByEstremi(FascicoloSiusModel aFascModel, String sTipoAtto,
			CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore) throws F3BException {

		Connection lConn = null;
		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloSiusProcedimentoPerEstremi(aFascModel, sTipoAtto, aCancAssFascSius,
					aFiltroCollaboratore, false);
			lCont = lFasSoggDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExGetNumRicercaFascicoloSiusByEstremi: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ritorna il vettore di ResidenzaAssociataModel riferite al Fascicolo in esame
	 *
	 * @param aKeyFascicolo
	 * @param aTipoResidenza
	 * @return Vector di ResidenzaAssociataModel
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenzaByProcedimentoSius(BigDecimal aKeyFascicolo, char aTipoResidenza)
			throws F3BException {

		Connection lConn = null;

		Vector lResidenze = new Vector();

		ResidenzaSqlDAO lDao = null;

		try {
			lConn = getDBConnection();

			lDao = new ResidenzaSqlDAO(lConn);

			lDao.ricercaResidenzaByProcedimentoSius(aKeyFascicolo, aTipoResidenza);

			lDao.start();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiusModel lResFasMod = null;
			while (lDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();
				lResMod = (ResidenzaModel) lDao.getModel();
				lResFasMod = lDao.getModelResidenzaFascicoloSius();
				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSius(lResFasMod);
				lResidenze.add(lResAssMod);
			}
			lDao.stop();
			if (lResidenze.size() == 0) {
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaResidenzaByProcedimentoSius: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lResidenze;
	}

	/**
	 * Ricerca Fascicolo Sius per Soggetto, Anno e Ufficio
	 *
	 * @param aFascicoloSius
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiusBySoggetto(SoggettoModel aSogModel) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		TenoreSqlDAO lTenDao = null;
		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 21/05/2003 popolo di tutti i dati il model del soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloSiusSoggetto(aSogModel);
			lFasSoggDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelPerEstremi();

				// Enzo - 21/05/2003 ricarico il model del soggetto nel fascicolo Sius.
				lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Enzo - 21/05/2003 Inserisco la parte di Tenore
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// 05/11/2003 Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloSiusBySoggetto: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lTenDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicolo Sius per Soggetto
	 *
	 * @param aFascicoloSius
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiusBySoggettoForStorico(SoggettoModel aSogModel) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();
		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 21/05/2003 popolo di tutti i dati il model del soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoloSiusSoggettoForStorico(aSogModel);
			lFasSoggDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelForStorico();
				lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);
				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloSiusBySoggettoForStorico: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca il Fascicolo SIUS in Banca Dati per Anno/Progressivo/Cod Ufficio con Controlli
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param ufficioUtenteConnesso
	 * @param aControllo
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso) throws F3BException {

		return ExRicercaFascicoloByAnnoProgrCodUfficio(aChiaveAnno, aChiaveProgr, ufficioUtenteConnesso,
				false);
	}

	/**
	 * Ricerca il Fascicolo SIUS in Banca Dati per Anno/Progressivo/Cod Ufficio senza Controlli.
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param ufficioUtenteConnesso
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficio(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso) throws F3BException {

		return ExRicercaFascicoloByAnnoProgrCodUfficio(aChiaveAnno, aChiaveProgr, ufficioUtenteConnesso,
				true);
	}

	/**
	 * Ricerca il Fascicolo SIUS in Banca Dati per Anno/Progressivo/codUfficio
	 *
	 * @param aModel
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficioFast(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso, Connection lConn) throws F3BException {

		FascicoloGPModel lFascicolo = null;
		FascicoloGPSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		TenoreSqlDAO lTenDao = null;

		try {
			lFascDao = new FascicoloGPSqlDAO(lConn);

			// Ricerca del fascicolo per Progressivo/Anno
			if (!lFascDao.existFasSiusUfficio(aChiaveAnno, aChiaveProgr, ufficioUtenteConnesso)) {
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Procedimento " + aChiaveAnno + "/" + aChiaveProgr + " inesistente");
			}

			lFascDao.ricercaFascicoloByAnnoProgrUfficio(aChiaveAnno, aChiaveProgr, ufficioUtenteConnesso);
			lFascicolo = (FascicoloGPModel) lFascDao.getModelByKey();
			if (lFascicolo == null)
				throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Fascicolo SIUS non trovato");
			// Udienza_Procedimento
			if (lFascicolo.getGeneraleProcedimentoModel() != null
					&& lFascicolo.getGeneraleProcedimentoModel().getUdiIdUdienza() != null)
				lFascicolo.setUdiPro(cercaUdiProcAttiva(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), lConn));

		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloByAnnoProgrCodUfficioFast: " + dex);
		} catch (SQLException sqlex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloByAnnoProgrCodUfficioFast: " + sqlex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lTenDao);
		}
		return lFascicolo;
	}

	/**
	 * Ricerca Fascicolo Sius paginata
	 *
	 * @param aFascicoloSius
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiusPagina(FascicoloGPModel aProgSiusModel, int aPageNum)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiusSoggettoSqlDAO lFasProgDao = null;

		try {
			lConn = getDBConnection();

			lFasProgDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasProgDao.ricercaFascicoloSiusPerNumeroSius(aProgSiusModel);
			lFasProgDao.startPage(aPageNum);

			FascicoloGPModel lFascicolo = null;
			while (lFasProgDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasProgDao.getFascicoloSiusPerNumeroSius();
				lFascicoli.add(lFascicolo);
			}
			lFasProgDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloSiusPagina: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasProgDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicolo Sius paginata
	 *
	 * @param aFascicoloSius
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSiusPaginaMinori(FascicoloGPModel aProgSiusModel, int aPageNum,
			String majorOffice) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloSiusSoggettoSqlDAO lFasProgDao = null;

		try {
			lConn = getDBConnection();

			lFasProgDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			// lFasProgDao.ricercaFascicoloSiusPerNumeroSius(aProgSiusModel);
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasProgDao.ricercaFascicoloSiusPerNumeroSius(aProgSiusModel, majorOffice);
			} else {
				lFasProgDao.ricercaFascicoloSiusPerNumeroSius(aProgSiusModel);
			}

			lFasProgDao.startPage(aPageNum);

			FascicoloGPModel lFascicolo = null;
			while (lFasProgDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasProgDao.getFascicoloSiusPerNumeroSius();
				lFascicoli.add(lFascicolo);
			}
			lFasProgDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloSiusPagina: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasProgDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaFascicoloSiusPerNumeroSius
	 *
	 * @param aFascicoloSius
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoloSius(FascicoloGPModel aProgSiusModel) throws F3BException {

		Connection lConn = null;
		FascicoloSiusSoggettoSqlDAO lFasProgDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();

			lFasProgDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasProgDao.ricercaFascicoloSiusPerNumeroSius(aProgSiusModel);
			lCont = lFasProgDao.getNumRowsSelected();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExGetNumRicercaFascicoloSius: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasProgDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Modifica il Fascicolo Sius Aggregato
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExModificaFascicoloSius(FascicoloGPModel aFascicoloGPModel) throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		GeneraleProcedimentoDAO lGenProDao = null;
		GeneraleProcedimentoSqlDAO lGenProSqlDao = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenDaoSql = null;
		Vector lTenoriGenProc = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lGenProDao = new GeneraleProcedimentoDAO(lConn);
			lTenDao = new TenoreDAO(lConn);

			boolean insertEMA = false, deleteEMA = false;
			boolean insertESS = false, deleteESS = false;
			boolean insertEMS = false, deleteEMS = false;

			// 04/01/2005 Si legge il G.P. che si vuole cambiare, per determinare se occorre inserire o
			// eliminare l'EMA(ESS).
			lGenProSqlDao = new GeneraleProcedimentoSqlDAO(lConn);
			lGenProSqlDao.ricercaGeneraleProcedimentoByKey(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			GeneraleProcedimentoModel lGenMod = (GeneraleProcedimentoModel) lGenProSqlDao.getModelByKey();
			// 04/01/2005 se cambia il Contenuto potrebbe essere necessario inserire o cancellare l'EMA(ESS).
			if (lGenMod.getCodOggettoProcedimento().compareTo(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()) != 0) {
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U004") == 0)
					insertEMA = true;
				if (lGenMod.getCodOggettoProcedimento().compareTo("U004") == 0)
					deleteEMA = true;
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U019") == 0)
					insertESS = true;
				if (lGenMod.getCodOggettoProcedimento().compareTo("U019") == 0)
					deleteESS = true;
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U024") == 0)
					insertEMS = true;
				if (lGenMod.getCodOggettoProcedimento().compareTo("U024") == 0)
					deleteEMS = true;
			}

			// Set del DAO e aggiornamento del FascicoloSius.
			lFasDao.setDAOFromModelForUpdate(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDao.update();

			/*
			 * ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato come in inserimento
			 * FascicoloSiusUDSController.ExInserisciFascicoloSiusUDS Numero MAC : 20200610014 Autore : Gioggi
			 * Data : 11 giu 2020 Branch : MAC_20200610014
			 */
			if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento().equals("U004")
					|| aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.equals("U019")
					|| aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.equals("U024")) {
				// Caso di Inserimento ESECUZIONE_MISURA_ALTERNATIVA - U004
				// Caso di Inserimento ESECUZIONE_SANZIONE_SOSTITUTIVA - U019
				// Caso di Inserimento ESECUZIONE_MISURA_SICUREZZA - U024
				// ATTENZIONE! Occorre Updatare il Generale Procedimento appena inserito nei campi ANNO_S1 &
				// PROGR_S1 poichè in essi hanno "viaggiato" Anno e Numero Ordinanza!
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
				aFascicoloGPModel.getGeneraleProcedimentoModel()
						.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
			}
			// ***** FINE INTERVENTO MAC_20200610014 *****//

			// Set del DAO e aggiornamento del GeneraleProcedimento.
			// STUB 14/12/2004 Aggiornamento parziale del Generale Procedimento.
			// lGenProDao.setDAOFromModelForUpdate(aFascicoloGPModel.getGeneraleProcedimentoModel());
			lGenProDao.setDAOFromModelForUpdateParziale(aFascicoloGPModel.getGeneraleProcedimentoModel());
			lGenProDao.update();

			// I Tenori non vengono più cancellati ma chiusi ! Luigi 10-12-2003
			TenoreModel lTenore = new TenoreModel();
			TenoreModel lTenoreGenProc = new TenoreModel();

			// Michele: effettuo la ricerca di tutti i tenori attivi per il generale procedimento
			lTenDaoSql = new TenoreSqlDAO(lConn);
			lTenDaoSql.ricercaTenoreByGeneraleProc(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenoriGenProc = new Vector(lTenDaoSql.getModels());
			Iterator itx = lTenoriGenProc.iterator();
			Vector elencoTenoriAttiviGenProc = new Vector();
			Vector elencoNuoviTenori = new Vector();
			// String condElencoTenoriAttivi = "";
			String condElencoNuoviTenori = "";

			// Caricamento del Vettore dei Tenori già Attivi nel Generale Procedimento
			while (itx.hasNext()) {
				lTenoreGenProc = (TenoreModel) itx.next();
				if (lTenoreGenProc.getCodOggettoTenore() != null) {
					if (!elencoTenoriAttiviGenProc.contains(lTenoreGenProc.getCodOggettoTenore())) {
						// if (!elencoTenoriAttiviGenProc.isEmpty()) {
						// condElencoTenoriAttivi += ", ";
						// }
						elencoTenoriAttiviGenProc.addElement(lTenoreGenProc.getCodOggettoTenore());
						// Filtro per il DAO sui Tenori già Attivi
						// condElencoTenoriAttivi += "'" + lTenoreGenProc.getCodOggettoTenore() + "'";
					}
				}
			}

			// Caricamento del Vettore dei Tenori presenti nel Generale Procedimento Modificato
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				if (aFascicoloGPModel.getTenori()[i].getCodOggettoTenore() != null) {
					if (!elencoNuoviTenori.contains(aFascicoloGPModel.getTenori()[i].getCodOggettoTenore())) {
						if (!elencoNuoviTenori.isEmpty()) {
							condElencoNuoviTenori += ", ";
						}
						elencoNuoviTenori.addElement(aFascicoloGPModel.getTenori()[i].getCodOggettoTenore());
						// Filtro per il DAO sui Nuovi Tenori
						condElencoNuoviTenori += "'" + aFascicoloGPModel.getTenori()[i].getCodOggettoTenore()
								+ "'";
					}
				}
			}

			// Aggiornamento della data fine per i tenori non più presenti
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setCodOperatoreAggiornamento(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataAggiornamento(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setDataFine(aFascicoloGPModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			if (!elencoNuoviTenori.isEmpty())
				lTenDao.setDAOFromModelForUpdateDataFine(lTenore, condElencoNuoviTenori);
			else
				lTenDao.setDAOFromModelForUpdateDataFine(lTenore);
			lTenDao.update();
			lTenDao.stop();

			// Fase di inserimento per il Tenore (Solo se non era già presente)
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Inserimento dei nuovi tenori non già presenti per quel generale procedimento
				if (!elencoTenoriAttiviGenProc
						.contains(aFascicoloGPModel.getTenori()[i].getCodOggettoTenore())) {
					// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
					aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					aFascicoloGPModel.getTenori()[i].setProgrTenore(new BigDecimal((double) (i + 1)));
					lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
					BigDecimal lChiave = lTenDao.insert();
					lTenDao.stop();
					aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
				}
				// Per i tenori già presenti vanno aggiornati gli altri campi eventualmente modificati
				else {
					// MEV_39: gestione differente per codTenore = "2422"
					TenoreModel tm = aFascicoloGPModel.getTenori()[i];
					if ("2422".equals(tm.getCodOggettoTenore())) {
						tm.setCodOperatoreAggiornamento(aFascicoloGPModel.getGeneraleProcedimentoModel()
								.getCodOperatoreAggiornamento());
						tm.setCodUfficioAggiornamento(aFascicoloGPModel.getGeneraleProcedimentoModel()
								.getCodUfficioAggiornamento());
						tm.setDataAggiornamento(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getDataAggiornamento());
						tm.setGenPridGeneraleProcedimento(
								aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					}
					lTenDao.setDAOFromModelForUpdateSenzaDataFine(tm);
					lTenDao.update();
					lTenDao.stop();
				}
			}

			// 04/01/2005 inserimento / cancellazione dell'EMA e Scadenzario.
			if (insertEMA)
				insEMAeScadenzario(aFascicoloGPModel, insertEMA, lConn);
			else if (deleteEMA)
				delEMAeScadenzario(aFascicoloGPModel, lConn);
			// 22/06/2009 inserimento / cancellazione dell'ESS e Scadenzario.
			if (insertESS)
				insESSeScadenzario(aFascicoloGPModel, insertESS, lConn);
			else if (deleteESS)
				delESSeScadenzario(aFascicoloGPModel, lConn);

			if (insertEMS)
				insEMSeScadenzario(aFascicoloGPModel, insertEMS, lConn);
			else if (deleteEMS)
				delEMSeScadenzario(aFascicoloGPModel, lConn);

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExModificaFascicoloSius: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lGenProDao);
			cleanup(lTenDao);
			cleanup(lTenDaoSql);
			cleanup(lGenProSqlDao);
			cleanup(lConn);
		}
		aFascicoloGPModel = ExRicercaFascicoloByKey(
				aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
		return aFascicoloGPModel;
	}

	/**
	 * Modifica il Fascicolo Sius
	 *
	 * @param aFascicoloSiusModel
	 * @return FascicoloSiusModel
	 * @throws F3BException
	 */
	public FascicoloSiusModel ExModificaFascicoloSius(FascicoloSiusModel aFascicoloSiusModel)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			// Set del DAO e aggiornamento del FascicoloSius
			lFasDao.setDAOFromModelForUpdate(aFascicoloSiusModel);
			lFasDao.update();
			lFasDao.stop();

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExModificaFascicoloSius: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return aFascicoloSiusModel;
	}

	/**
	 * Modifica l' ID_FASCICOLO_SIUS_ORIGINE del Fascicolo Sius
	 * <p>
	 *
	 * @param aFascicoloSiusModel
	 * @return FascicoloSiusModel
	 * @throws F3BException
	 */
	public FascicoloSiusModel ExModificaIdFascicoloSiusOrigine(FascicoloSiusModel aFascicoloSiusModel)
			throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);

			// Set del DAO e aggiornamento del FascicoloSius.
			lFasDao.setDAOFromModelForAggiornaIdFascicoloOrigine(aFascicoloSiusModel);
			lFasDao.update();
			lFasDao.stop();

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExModificaIdFascicoloSiusOrigine: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return aFascicoloSiusModel;
	}

	/**
	 * Esecuzione stampa del fascicolo.
	 * <p>
	 *
	 * @param aIdFascicolo
	 * @param lTipoUfficio
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProcedimento(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		// Riempi l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO,
				// ICostantiStampaSius.TREE_FASCICOLOSIEP, STUB 13/01/2005
				ICostantiStampaSius.TREE_FASCICOLOSIEP_ALL, // STUB 13/01/2005
				ICostantiStampaSius.TREE_SENTENZA, ICostantiStampaSius.TREE_AVVOCATO,
				ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_MAGISTRATO,
				ICostantiStampaSius.TREEs_RICHIESTE_ISTRUTTORIE, ICostantiStampaSius.TREEs_PROVVEDIMENTI,
				ICostantiStampaSius.TREEs_PROVVEDIMENTI_ALTRI, ICostantiStampaSius.TREE_TIT_ESE_REF, // Enzo
																										// 21/01/2005
				ICostantiStampaSius.TREE_RIF_FAS_SIEP, // Enzo 21/01/2005
				ICostantiStampaSius.TREE_ESECUZIONEMISURAALTERNATIVA, // Enzo 13/01/2006
				ICostantiStampaSius.TREE_UDIENZA };
		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, lTipoUfficio);

		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_ST_001");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * 02/02/2005 Esecuzione stampa dei procedimenti Del Soggetto.
	 * <p>
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProcedimentiDelSoggetto(BigDecimal aIdSoggetto, String aIdDocumento,
			XModel aStampa, String aCodUff, UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();

		// Ricerca Soggetto.
		ISoggetto lCtrlSog = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggetto = lCtrlSog.ExRicercaSoggettoByKey(aIdSoggetto);

		// Ricerca Procedimenti del Soggetto.
		Vector lProcedimentiDelSoggetto = ExRicercaFascicoloSiusBySoggetto(lSoggetto);

		lByteArrayOut = lCtrlSta.ExPreStampaProcedimentiDelSoggetto(lSoggetto, lProcedimentiDelSoggetto,
				aIdDocumento, aStampa, aCodUff, aUtenteModel);

		return lByteArrayOut;
	}

	/**
	 * Ricerca Fascicolo Sius by Generale Procedimento
	 *
	 * @param aIdGenProc
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExRicercaFascicoloByGenProc(BigDecimal aIdGenProc) throws F3BException {

		Connection lConn = null;
		FascicoloGPModel lFascicolo = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;

		try {
			lConn = getDBConnection();

			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByGenProc(aIdGenProc);
			lFasGPSqlDao.start();
			lFascicolo = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

			// popolo il model del soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lFascicolo.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			// ricarico il model del soggetto nel fascicolo Sius.
			lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

			// Inserisco la parte di Tenore
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByGeneraleProc(
					lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

			// Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
			Vector lVectTenori = new Vector(lTenDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFascicolo.setTenori(lTenoriModel);
			}

			lFasGPSqlDao.stop();

			if (lFascicolo != null && lFascicolo.getGeneraleProcedimentoModel() != null
					&& lFascicolo.getGeneraleProcedimentoModel().getUdiIdUdienza() != null)
				lFascicolo.setUdiPro(cercaUdiProcAttiva(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), lConn));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloByGenProc : " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloByGenProc : " + sqe);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lTenDao);// sca
			cleanup(lSogDao); // sca
			cleanup(lFasSoggDao); // sca
			cleanup(lConn);
		}
		return lFascicolo;
	}

	/**
	 * Ricerca Fascicoli Per Id Origine = aIdFascicoloOrigine
	 * <p>
	 *
	 * @param aIdFascicoloOrigine
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliXIdOrigine(BigDecimal aIdFascicoloOrigine) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		FascicoloGPSqlDAO lFasGPSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			if (aIdFascicoloOrigine != null) {
				lFasGPSqlDao.ricercaFascicoloXIdOrigine(aIdFascicoloOrigine);
				// MERGE v10 COLLAUDO: lo start & stop avviene nel metodo "getModels()"
				// altrimenti esegue 2 volte la stessa query
				// lFasGPSqlDao.start();
				lFascicoli = new Vector(lFasGPSqlDao.getModels());
				// lFasGPSqlDao.stop();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliXIdOrigine : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliXIdOrigine : " + e.getMessage());
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli Sius per Soggetto e Codice Oggetto.
	 * <p>
	 *
	 * @param aIdSoggetto
	 * @return Vector di FascicoloGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliByIdSoggettoCodOggetto(BigDecimal aIdSoggetto, String aCodOggetto)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByIdSoggettoCodOggetto(aIdSoggetto, aCodOggetto);
			lFascicoli = new Vector(lFasGPSqlDao.getModels());
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliByIdSoggettoCodOggetto: " + dex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Totale fascicoli: " + lFascicoli.size());
		return lFascicoli;
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di una residenza nella tabella
	 * RESIDENZA_FASCICOLO_SIUS
	 *
	 * @param IdResidenza
	 *            : identificatore RESIDENZA, IdFascicolo identificatore del fascicolo
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaResidenzaProcedimentoSius(BigDecimal IdResidenza, BigDecimal IdFascicolo)
			throws F3BException {

		Connection lConn = null;
		ResidenzaFascicoloSiusDAO lResFasSiusDao = null;

		try {
			lConn = getDBConnection();
			lResFasSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
			lResFasSiusDao.setCondizioneDelete(IdResidenza, IdFascicolo);
			lResFasSiusDao.delete();
			lResFasSiusDao.stop();
			lResFasSiusDao.setDataFineValidita(null);
			lResFasSiusDao.setCondizioneUpdate(IdFascicolo, 'R');
			lResFasSiusDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Residenza collegata ad altri dati. Impossibile effettuare la Cancellazione!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"UdienzaController.ExCancellaResidenzaProcedimentoSius: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResFasSiusDao);
			cleanup(lConn);
		}
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un domicilio nella tabella
	 * RESIDENZA_FASCICOLO_SIUS
	 *
	 * @param IdResidenza
	 *            : identificatore RESIDENZA, IdFascicolo identificatore del fascicolo
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaDomicilioProcedimentoSius(BigDecimal IdResidenza, BigDecimal IdFascicolo)
			throws F3BException {

		Connection lConn = null;
		ResidenzaFascicoloSiusDAO lResFasSiusDao = null;

		try {
			lConn = getDBConnection();
			lResFasSiusDao = new ResidenzaFascicoloSiusDAO(lConn);
			lResFasSiusDao.setCondizioneDelete(IdResidenza, IdFascicolo);
			lResFasSiusDao.delete();
			lResFasSiusDao.stop();
			lResFasSiusDao.setDataFineValidita(null);
			lResFasSiusDao.setCondizioneUpdate(IdFascicolo, 'D');
			lResFasSiusDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Domicilio collegato ad altri dati. Impossibile effettuare la Cancellazione!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExCancellaDOmicilioProcedimentoSius: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResFasSiusDao);
			cleanup(lConn);
		}
	}

	/**
	 * <p>
	 * Description: : la funzione inserisce Una occorrenza di ResidenzaAssociata nella tabella
	 * RESIDENZA_FASCICOLO_SIUS
	 *
	 * @param aResidenza
	 *            (ResidenzaAssociataModel)
	 * @return aResidenza (ResidenzaAssociataModel)
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExInserisciResidenzaFascicoloSius(ResidenzaAssociataModel aResidenza)
			throws F3BException {

		Connection lConn = null;

		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiusDAO lResFascDao = null;

		ResidenzaModel lResidenza = aResidenza.getResidenza();
		ResidenzaFascicoloSiusModel lResidenzaFascicolo = aResidenza.getResidenzaFascicoloSius();

		try {
			lConn = getDBTransaction();

			// Inserisce una nuova Residenza per un dato Soggetto
			if (lResidenza.getIdResidenza().compareTo(new BigDecimal(0)) == 0) {
				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModel(lResidenza);

				BigDecimal lSequence = lResDao.insert();
				lResidenza.setIdResidenza(lSequence);
				lResidenzaFascicolo.setResIdResidenza(lSequence);
			}

			// ** Associa la Residenza al Fascicolo
			// ** storicizzando quella eventualmente presente
			// 1- Storicizza l'ultima occorrenza eventualmente presente
			BigDecimal lIdResFasCorrente = this
					.getIdResidenzaFascicoloSiusCorrente(lResidenzaFascicolo.getFasSiuIdFascicoloSius());

			lResFascDao = new ResidenzaFascicoloSiusDAO(lConn);
			if (lIdResFasCorrente != null) {
				lResFascDao.setDataFineValidita(lResidenzaFascicolo.getDataInizioValidita());

				lResFascDao.setCondizioneResFascCorrente(lResidenzaFascicolo.getFasSiuIdFascicoloSius(),
						lResidenza.getCodTipoResidenza().toString());
				lResFascDao.update();
				lResFascDao.stop();
			}

			// 2 - Inserisce la nuova occorrenza
			lResFascDao.setDAOFromModel(lResidenzaFascicolo);
			lResFascDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExInserisciResidenzaFascicoloSius : " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExInserisciResidenzaFascicoloSius : " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lResFascDao);

			cleanup(lConn);
		}

		return aResidenza;
	}

	/**
	 * <p>
	 * Description: : la funzione modifica la Residenza riferita al Fascicolo SIUS in Oggetto.
	 *
	 * @param aResidenza
	 *            (ResidenzaAssociataModel)
	 * @return aResidenza (ResidenzaAssociataModel)
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExModificaResidenzaFascicoloSius(ResidenzaAssociataModel aResidenza)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExModificaResidenzaFascicoloSius : inizio");
		Connection lConn = null;

		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiusDAO lResFascDao = null;
		ResidenzaSqlDAO lResSqlDAO = null;
		if (aResidenza == null || aResidenza.getResidenza() == null
				|| aResidenza.getResidenzaFascicoloSius() == null)
			throw new SIUSException(F3BException.USER_MESSAGE, "Errore nei dati ");

		ResidenzaModel lResidenza = aResidenza.getResidenza();
		ResidenzaFascicoloSiusModel lResidenzaFascicolo = aResidenza.getResidenzaFascicoloSius();

		// boolean lPiuFascicoli = true;
		BigDecimal lIdResidenzaDaCambiare = lResidenzaFascicolo.getResIdResidenza();

		if (lIdResidenzaDaCambiare == null)
			throw new SIUSException(F3BException.USER_MESSAGE, "Errore nei dati: ID Residenza assente ");

		// Valorizzazione dei campi di aggiornamento
		lResidenza.setCodOperatoreAggiornamento(lResidenza.getCodOperatoreInserimento());
		lResidenza.setCodUfficioAggiornamento(lResidenza.getCodUfficioInserimento());
		lResidenza.setDataAggiornamento(lResidenza.getDataInserimento());
		try {
			lConn = getDBTransaction();

			// Si Controlla se la Residenza da modificare è collegata a più Fascicoli
			lResSqlDAO = new ResidenzaSqlDAO(lConn);
			int lNumRes = lResSqlDAO.getNumOccorrenze(lIdResidenzaDaCambiare);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Numero Occorrenze : " + lNumRes);

			// La Residenza è collegata a più fascicoli
			if (lNumRes > 1) {

				// Si inserisce un nuovo record Residenza
				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModel(lResidenza);
				BigDecimal lSequence = lResDao.insert();
				lResDao.stop();

				// Aggiornamento dati
				lResidenza.setIdResidenza(lSequence);
				lResidenzaFascicolo.setResIdResidenza(lSequence);

				// Si effettua l'update della Relazione Residenza_Fascicolo per la residenza corrente
				lResFascDao = new ResidenzaFascicoloSiusDAO(lConn);
				lResFascDao.setResIdResidenza(lResidenzaFascicolo.getResIdResidenza());
				lResFascDao.setCondizioneDelete(lIdResidenzaDaCambiare,
						lResidenzaFascicolo.getFasSiuIdFascicoloSius());
				lResFascDao.update();
				lResFascDao.stop();
			} else if (lNumRes == 1) {
				// Si oggiorna il record Residenza
				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModelForUpdate(lResidenza);
				lResDao.update();
				lResDao.stop();
			} else
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Errore nei dati: nessuna occorrenza residenza ");

			commit(lConn);

			// Aggiornamento dati di uscita
			aResidenza.setResidenza(lResidenza);
			aResidenza.setResidenzaFascicoloSius(lResidenzaFascicolo);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExModificaResidenzaFascicoloSius : " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lResFascDao);
			cleanup(lResSqlDAO);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ExModificaResidenzaFascicoloSius : fine");

		return aResidenza;
	}

	public ResidenzaAssociataModel ExRicercaResidenzaFascicoloSiusCorrente(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();

		ResidenzaSqlDAO lResDao = null;

		try {
			lConn = getDBConnection();

			lResDao = new ResidenzaSqlDAO(lConn);

			// Residenza Corrente
			// lResDao.ricercaResidenzaByFascicolo(aIdFascicolo);
			lResDao.ricercaResidenzaByFascicoloSius(aIdFascicolo);
			lResAss.setResidenza((ResidenzaModel) lResDao.getModelByKey());
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaResidenzaFascicoloSiusCorrente: " + dex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResDao);

			cleanup(lConn);
		}

		return lResAss;
	}

	/**
	 * Ricerca Fascicoli Sius Paginata per Soggetto, e filtri aggiuntivi.
	 * <p>
	 *
	 * @param aSogModel
	 * @param strCodUffOTrib
	 * @param lIncludeDistretto
	 * @param lIncludeArchiviati
	 * @param lCodContenuto
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeArchiviati, String lCodContenuto, Date dataDalInCanc, Date dataAlInCanc,
			int aPageNum, String majorOffice) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloGPModel lFascicolo = null;

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodUffOTrib,
					lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc,
					majorOffice);
			// lFasSoggDao.start();
			lFasSoggDao.startPage(aPageNum);
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelUnico();

				// Si popola di tutti i dati il model del soggetto
				lSogDao = new SoggettoSqlDAO(lConn);

				// paolo cherubini per supersoggetto 28/07/2009
				// cambio la select
				// Si popola di tutti i dati il model del soggetto
				// lSogDao.ricercaSoggettoByKey (lFascicolo.getFascicoloSiusModel().getSogIdSoggetto());
				lSogDao.ricercaSuperSoggetto("ufficio", lCodUfficioUtenteConnesso,
						lFascicolo.getFascicoloSiusModel().getSoggetto(), "FASCICOLO_SIUS", lCodDistretto,
						majorOffice, false, "");
				// fine

				aSogModel = (SoggettoModel) lSogDao.getModelByKey();

				// Si ricarica il model del soggetto nel fascicolo Sius.
				lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// paolo cherubini per supersoggetto 28/07/2009
				// aggiorno sogidsoggetto del fascicolo
				if (aSogModel != null) {
					lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(aSogModel.getIdSoggetto());
				} else {
					lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(null);
				}

				// Si Caricano i dati del tenore nell'array di Tenori di FascicoloGPModel.
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliBySoggettoPagina: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lTenDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaFascicoliBySoggetto
	 *
	 * @param aFascicoloSius
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoliBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeArchiviati, String lCodContenuto, Date dataDalInCanc, Date dataAlInCanc)
			throws F3BException {

		Connection lConn = null;
		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodUffOTrib,
					lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc, "");
			lCont = lFasSoggDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExGetNumRicercaFascicoliBySoggetto: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaFascicoliPerDataFinePena
	 *
	 * @param aFascicoloSius
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoliPerDataFinePena(String lUfficioUtenteConnesso,
			Date dataDalIscrizione, Date dataAlIscrizione, Date dataDalFinePena, Date dataAlFinePena,
			String lIncludeArchiviati, String lCodPosGiuridica, String lCodContenuto) throws F3BException {

		Connection lConn = null;
		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoliPerDataFinePena(lUfficioUtenteConnesso, dataDalIscrizione,
					dataAlIscrizione, dataDalFinePena, dataAlFinePena, lIncludeArchiviati, lCodPosGiuridica,
					lCodContenuto);
			lCont = lFasSoggDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExGetNumRicercaFascicoliPerDataFinePena: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Fascicoli di Un Soggetto (Nel model aSogModel è valorizzato l'ID) in base ai parametridi
	 * ricerca selezionati.
	 * <p>
	 *
	 * @param aSogModel
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodUffOTrib
	 * @param strCodUffOTrib
	 * @param lCodDistretto
	 * @param lIncludeArchiviati
	 * @param lCodContenuto
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;
		// boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);

			lFasSoggDao.ricercaFascicoliDelSoggetto(aSogModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
					lCodDistretto, lIncludeArchiviati, lCodContenuto, dataDalInCanc, dataAlInCanc);
			lFasSoggDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lFasSoggDao.next()) {
				// STUB 11/03/2005 lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModel();
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelForProvv();

				// Si reimposta il model del soggetto nel Fascicolo Sius.
				lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// 05/11/2003 Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliDelSoggetto: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lSogDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli per fascicoli SIEP
	 * <p>
	 *
	 * @param Id_FascicoloSIEP
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliPerNumeroSIEP(BigDecimal lId_FascicoloSiep) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);

			lFasSoggDao.ricercaFascicoliPerNumeroSIEP(lId_FascicoloSiep);
			lFasSoggDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusPerNumeroSIEP();
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProcOrderByPeso(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					// il primo elemeto viene rimosso perchè non deve essere visualizzato nella lista
					if (lVectTenori.size() > 1)
						lVectTenori.remove(0);

					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lFascicoli.add(lFascicolo);
			}

			lFasSoggDao.stop();

			// modifica del 21-09-04 spostato nell'azione ActRicercaProcedimentiPerNumeroSiep -- Dario
			// if (lFascicoli.isEmpty())
			// throw new SIUSException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliPerNumeroSIEP: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lSogDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli Sius Paginata per Fine Pena, e filtri aggiuntivi.
	 * <p>
	 *
	 * @param lUfficioUtenteConnesso
	 * @param dataDalIscrizione
	 * @param dataAlIscrizione
	 * @param dataDalFinePena
	 * @param dataAlFinePena
	 * @param lIncludeArchiviati
	 * @param lCodPosGiuridica
	 * @param lCodContenuto
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliPerDataFinePena(String lUfficioUtenteConnesso, Date dataDalIscrizione,
			Date dataAlIscrizione, Date dataDalFinePena, Date dataAlFinePena, String lIncludeArchiviati,
			String lCodPosGiuridica, String lCodContenuto, int aPageNum) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloGPModel lFascicolo = null;

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoliPerDataFinePena(lUfficioUtenteConnesso, dataDalIscrizione,
					dataAlIscrizione, dataDalFinePena, dataAlFinePena, lIncludeArchiviati, lCodPosGiuridica,
					lCodContenuto);
			lFasSoggDao.startPage(aPageNum);
			while (lFasSoggDao.next()) {
				lFascicolo = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelPerDataFinePena();
				lFascicoli.add(lFascicolo);
			}
			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Nessun Procedimento individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliPerDataFinePena: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lTenDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli Sius Fine Pena e filtri aggiuntivi per Stampa Excel.
	 * <p>
	 *
	 * @param lUfficioUtenteConnesso
	 * @param dataDalIscrizione
	 * @param dataAlIscrizione
	 * @param dataDalFinePena
	 * @param dataAlFinePena
	 * @param lIncludeArchiviati
	 * @param lCodPosGiuridica
	 * @param lCodContenuto
	 * @return File Excel risultante
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExReportFascicoliPerDataFinePenaExcel(UfficioModel aUfficioUtenteConnesso,
			Date aDataDalIscrizione, Date aDataAlIscrizione, Date aDataDalFinePena, Date aDataAlFinePena,
			String aIncludeArchiviati, String aCodPosGiuridica, String aCodContenuto,
			String aDescrPosGiuridica, String aDescrContenuto) throws F3BException {

		final int colonnaProgressivo = 0;
		final int colonnaProcedimentoSIUS = 1;
		final int colonnaSoggetto = 2;
		final int colonnaDataIscrizione = 3;
		final int colonnaDataFinePena = 4;
		final int colonnaPosizioneGuiridica = 5;
		final int colonnaContenuto = 6;

		ByteArrayOutputStream lResult = null;
		Connection lConn = null;
		Vector<FascicoloGPModel> lFascicoli = new Vector<>();

		FascicoloSiusSoggettoSqlDAO lFasSoggDao = null;
		TenoreSqlDAO lTenoreDao = null;
		SoggettoSqlDAO lSoggettoDao = null;
		FascicoloGPModel lFascicoloGPModel = null;
		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFRow lRow = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		// HSSFCell lCell = null;
		int lRowCounter = 0;
		Iterator<FascicoloGPModel> lIterator = null;
		int lProgrssivo = 0;
		String lText = null;

		try {
			lConn = getDBConnection();

			lFasSoggDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggDao.ricercaFascicoliPerDataFinePena(aUfficioUtenteConnesso.getCodUfficio(),
					aDataDalIscrizione, aDataAlIscrizione, aDataDalFinePena, aDataAlFinePena,
					aIncludeArchiviati, aCodPosGiuridica, aCodContenuto);
			lFasSoggDao.start();
			while (lFasSoggDao.next()) {
				lFascicoloGPModel = (FascicoloGPModel) lFasSoggDao.getFascicoloSiusGPModelPerDataFinePena();
				lFascicoli.add(lFascicoloGPModel);
			}
			lFasSoggDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Nessun Procedimento individuato con i criteri di ricerca selezionati! ");

			lWb = new HSSFWorkbook();
			lSheet = lWb.createSheet("ElencoProvvedimenti");
			lCellStyleNull = lWb.createCellStyle();

			// Intestazione
			lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficioUtenteConnesso,
					lCellStyleNull);

			lRowCounter += 2;

			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Elenco Procedimenti per Formazione ruolo di udienza",
					lCellStyleNull);
			lRowCounter += 2;

			// Data iscrizione iniziale e finale.
			if (aDataDalIscrizione != null || aDataAlIscrizione != null) {
				lRow = lSheet.createRow(lRowCounter);
				lText = "Visualizza i procedimenti Iscritti ";
				if (aDataDalIscrizione != null) {
					lText += " dal " + DateUtils.getDateToString(aDataDalIscrizione, "dd/MM/yyyy");
				}
				if (aDataAlIscrizione != null) {
					lText += " al " + DateUtils.getDateToString(aDataAlIscrizione, "dd/MM/yyyy");
				}
				HSSFUtils.getInstance().setCell(lRow, 0, lText, lCellStyleNull);
				lRowCounter++;
			}
			// Data fine pena iniziale e finale.
			if (aDataDalFinePena != null || aDataAlFinePena != null) {
				lRow = lSheet.createRow(lRowCounter);
				lText = "Visualizza i procedimenti con Data Fine Pena ";
				if (aDataDalFinePena != null) {
					lText += " dal " + DateUtils.getDateToString(aDataDalFinePena, "dd/MM/yyyy");
				}
				if (aDataAlFinePena != null) {
					lText += " al " + DateUtils.getDateToString(aDataAlFinePena, "dd/MM/yyyy");
				}
				HSSFUtils.getInstance().setCell(lRow, 0, lText, lCellStyleNull);
				lRowCounter++;
			}

			// Visualizza solo i procedimenti con Pos. Giuridica
			if (aDescrPosGiuridica != null) {
				if (aDescrPosGiuridica.compareTo("-") != 0) {
					lRow = lSheet.createRow(lRowCounter);
					lText = "Visualizza solo i procedimenti con Posizione Giuridica " + aDescrPosGiuridica;
					HSSFUtils.getInstance().setCell(lRow, 0, lText, lCellStyleNull);
					lRowCounter++;
				}
			}

			// Visualizza solo i procedimenti relativi a
			if (aDescrContenuto != null) {
				if (aDescrContenuto.compareTo("-") != 0) {
					lRow = lSheet.createRow(lRowCounter);
					lText = "Visualizza solo i procedimenti relativi a " + aDescrContenuto;
					HSSFUtils.getInstance().setCell(lRow, 0, lText, lCellStyleNull);
					lRowCounter++;
				}
			}

			lRowCounter += 2;

			// impostazione della larghezza
			// delle colonne
			lSheet.setColumnWidth(colonnaProgressivo, (10 * 256)); // Prog
			lSheet.setColumnWidth(colonnaProcedimentoSIUS, (15 * 256)); // Procedimento SIUS
			lSheet.setColumnWidth(colonnaSoggetto, (35 * 256)); // Generalità Soggetto
			lSheet.setColumnWidth(colonnaDataIscrizione, (15 * 256)); // Data Iscrizione
			lSheet.setColumnWidth(colonnaDataFinePena, (15 * 256)); // Data Fine Pena
			lSheet.setColumnWidth(colonnaPosizioneGuiridica, (15 * 256)); // Posizione Giuridica
			lSheet.setColumnWidth(colonnaContenuto, (55 * 256)); // Contenuto Atto

			lCellStyleCenter = HSSFUtils.getInstance().getBordo4Lati(lWb);
			lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			lCellStyleCenter.setWrapText(true);

			lRow = lSheet.createRow(lRowCounter++);

			// Intestazione colonne
			HSSFUtils.getInstance().setCell(lRow, colonnaProgressivo, "Progr.", lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, colonnaProcedimentoSIUS, "Procedimento SIUS",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, colonnaSoggetto, "Generalità Soggetto", lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, colonnaDataIscrizione, "Data Iscrizione", lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, colonnaDataFinePena, "Data Fine Pena", lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, colonnaPosizioneGuiridica, "Posizione Giuridica",
					lCellStyleCenter);
			HSSFUtils.getInstance().setCell(lRow, colonnaContenuto, "Contenuto", lCellStyleCenter);

			lIterator = lFascicoli.iterator();
			while (lIterator.hasNext()) {
				lFascicoloGPModel = lIterator.next();
				lRow = lSheet.createRow(lRowCounter++);

				// Progressivo
				HSSFUtils.getInstance().setCell(lRow, colonnaProgressivo, ++lProgrssivo + "",
						lCellStyleCenter);

				// Procedimento SIUS
				HSSFUtils.getInstance().setCell(lRow, colonnaProcedimentoSIUS,
						lFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno() + "/"
								+ lFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr(),
						lCellStyleCenter);

				// Soggeto
				HSSFUtils.getInstance().setCell(lRow, colonnaSoggetto,
						lFascicoloGPModel.getFascicoloSiusModel().getSoggetto().getCognome() + " "
								+ lFascicoloGPModel.getFascicoloSiusModel().getSoggetto().getNome(),
						lCellStyleCenter);

				// Data Iscrizione
				HSSFUtils.getInstance().setCell(lRow, colonnaDataIscrizione,
						StringUtils.toStringJSP(DateUtils.getDateToString(
								lFascicoloGPModel.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy"),
								"-"),
						lCellStyleCenter);

				// Data Fine Pena
				HSSFUtils.getInstance().setCell(lRow, colonnaDataFinePena,
						StringUtils.toStringJSP(DateUtils.getDateToString(
								lFascicoloGPModel.getGeneraleProcedimentoModel().getDataFinePena(),
								"dd/MM/yyyy"), "-"),
						lCellStyleCenter);

				// Posizione Giuridica
				HSSFUtils.getInstance().setCell(lRow, colonnaPosizioneGuiridica,
						lFascicoloGPModel.getGeneraleProcedimentoModel().getDescrPosGiuridica(),
						lCellStyleCenter);

				// Contenuto
				HSSFUtils.getInstance().setCell(lRow, colonnaContenuto,
						lFascicoloGPModel.getGeneraleProcedimentoModel().getDescrOggettoProcedimento(),
						lCellStyleCenter);
			}

			lResult = new ByteArrayOutputStream();

			try {
				lWb.write(lResult);
			} catch (IOException e) {
				e.printStackTrace();
				throw new F3BException(e);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliPerDataFinePena: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSoggDao);
			cleanup(lTenoreDao);
			cleanup(lSoggettoDao);
			cleanup(lConn);
		}

		return lResult;
	}

	/**
	 * Inserisce il Fascicolo Sius con CHIAVE_ANNO e CHIAVE_PROGR inseriti manualmente.
	 * <p>
	 *
	 * @param aFascicoloGPModel
	 *            ;
	 * @return aFascicoloGPModel;
	 * @throws F3BException
	 */
	public FascicoloGPModel ExInserisciFascicoloSiusManuale(FascicoloGPModel aFascicoloGPModel)
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

			lFasDaoSql.getProgressivoFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel());
			lFasDaoSql.start();

			BigDecimal lBigDec = new BigDecimal(0);
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

			// 21/07/2004 Modificata la valorizzazione del Progressivo GP, impostato al valore del Fascicolo
			// SIUS inserito.
			aFascicoloGPModel.getGeneraleProcedimentoModel()
					.setProgrS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveProgr());
			// 26/01/2007 Aggiunta la valorizzazione dell'AnnoS1, impostato al valore della chiave Anno del
			// Fascicolo SIUS inserito.
			aFascicoloGPModel.getGeneraleProcedimentoModel()
					.setAnnoS1(aFascicoloGPModel.getFascicoloSiusModel().getChiaveAnno());
			aFascicoloGPModel.getGeneraleProcedimentoModel().setFasSiuIdFascicoloSius(lChiave);

			// Setto il DAO dal Model ed inserisco il Generale Procedimento
			lGenProDao.setDAOFromModel(aFascicoloGPModel.getGeneraleProcedimentoModel());
			BigDecimal lChiaveGP = lGenProDao.insert();

			aFascicoloGPModel.getGeneraleProcedimentoModel().setIdGeneraleProcedimento(lChiaveGP);

			// Fase di inserimento per il Tenore.
			for (int i = 0; i < aFascicoloGPModel.getTenori().length; i++) {
				// Imposta i campi GenPridGeneraleProcedimento & ProgrTenore di Tenore.
				aFascicoloGPModel.getTenori()[i].setGenPridGeneraleProcedimento(lChiaveGP);
				lTenDao.setDAOFromModel(aFascicoloGPModel.getTenori()[i]);
				lChiave = lTenDao.insert();
				aFascicoloGPModel.getTenori()[i].setIdTenore(lChiave);
			}
			// Inserimento del Magistrato Relatore Cod_Magistrato Appoggiato sull'Autorità Delegata.
			if (!aFascicoloGPModel.getGeneraleProcedimentoModel().getCodAutoritaDelegata().startsWith("-")) {
				MagistratoRelatoreModel lMagistrato = new MagistratoRelatoreModel();
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

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExInserisciFascicoloSiusManuale: " + ex);
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
	 * Ricerca Fascicoli Unificati.
	 * <p>
	 *
	 * @param idFasSius
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaElencoFascicoliUnificati(FascicoloSiusModel aModel) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();
		FascicoloSiusSqlDAO lFasSqlDao = null;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.

			lFasSqlDao = new FascicoloSiusSqlDAO(lConn);

			lFasSqlDao.ricercaElencoFascicoliUnificati(aModel);
			lFasSqlDao.start();

			FascicoloSiusModel lFascicolo = null;
			while (lFasSqlDao.next()) {
				lFascicolo = (FascicoloSiusModel) lFasSqlDao.getModel();
				lFascicoli.add(lFascicolo);
			}

			lFasSqlDao.stop();

			if (lFascicoli.isEmpty())
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Errore nella Ricerca dei Fascicoli Unificati: Non Trovati ! ");
			// throw new SIUSException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaElencoFascicoliUnificati: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	// -----------------
	// METODI PRIVATE
	// -----------------
	/**
	 * Ricerca il Fascicolo SIUS in Banca Dati per Anno/Progressivo/Cod Ufficio
	 *
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param ufficioUtenteConnesso
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	private FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficio(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso, boolean aControllo) throws F3BException {

		Connection lConn = null;

		FascicoloGPModel lFascicolo = null;
		// TenoreModel lTenMod = null;
		SoggettoModel lSoggMod = null;

		FascicoloGPSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		TenoreSqlDAO lTenDao = null;

		try {
			lConn = getDBConnection();

			lFascDao = new FascicoloGPSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);

			// Ricerca del fascicolo per Progressivo/Anno
			if (!lFascDao.existFasSiusUfficio(aChiaveAnno, aChiaveProgr, ufficioUtenteConnesso)) {
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Procedimento " + aChiaveAnno + "/" + aChiaveProgr + " inesistente");
			}

			lFascDao.ricercaFascicoloByAnnoProgrUfficio(aChiaveAnno, aChiaveProgr, ufficioUtenteConnesso);
			lFascicolo = (FascicoloGPModel) lFascDao.getModelByKey();
			if (lFascicolo == null)
				throw new SIUSException(F3BException.USER_MESSAGE, "Errore: Fascicolo SIUS non trovato");

			// 07/06/2004 Parametrizzata l'esecuzione dei controlli.
			if (aControllo) {
				if (lFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().equals("01"))
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Operazione non consentita, Procedimento " + aChiaveAnno + "/" + aChiaveProgr
									+ " già definito");

				// 07/06/2004 Filtrata la ricerca puntuale per fascicoli Unificati.
				if (lFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().equals("05"))
					throw new SIUSException(F3BException.USER_MESSAGE,
							"Operazione non consentita, Procedimento " + aChiaveAnno + "/" + aChiaveProgr
									+ " unificato");
			}

			lSoggDao.ricercaSoggettoByKey(lFascicolo.getFascicoloSiusModel().getSogIdSoggetto());
			lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			lFascicolo.getFascicoloSiusModel().setSoggetto(lSoggMod);

			// Enzo - 21/05/2003 Inserisco la parte di Tenore
			lTenDao = new TenoreSqlDAO(lConn);
			lTenDao.ricercaTenoreByGeneraleProc(
					lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

			// 05/11/2003 Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
			Vector lVectTenori = new Vector(lTenDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFascicolo.setTenori(lTenoriModel);
			}
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
					"FascicoloSiusController.ExRicercaFascicoloByAnnoProgrCodUfficio: " + dex);
		} catch (SQLException sqlex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoloByAnnoProgrCodUfficio: " + sqlex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lFascicolo;
	}

	/**
	 * Metodo di scrittura di RIFERIMENTO_FASCICOLO_SIUS a partire da aFasGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void insRiferimentoSius(FascicoloGPModel aFasGPModel, Connection lConn) throws F3BException {

		RiferimentoFascicoloSiusDAO lRFSDao = null;
		if (!aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep().equals(null)) {
			try {
				lRFSDao = new RiferimentoFascicoloSiusDAO(lConn);
				lRFSDao.setAnnoFascicoloSius(aFasGPModel.getFascicoloSiusModel().getChiaveAnno());
				lRFSDao.setProgrFascicoloSius(aFasGPModel.getFascicoloSiusModel().getChiaveProgr());
				lRFSDao.setCodUffFascicoloSius(aFasGPModel.getFascicoloSiusModel().getChiaveUfficio());
				lRFSDao.setDataRicezione(aFasGPModel.getFascicoloSiusModel().getDataInserimento());
				lRFSDao.setCodOggettoProcedimento(
						aFasGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
				lRFSDao.setFasSieIdFascicoloSiep(
						aFasGPModel.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				/* BigDecimal lChiaveRFS = */lRFSDao.insert();
			} catch (DAOException daoEx) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIUSException(F3BException.USER_MESSAGE,
						"FascicoloSiusController.insRiferimentoSius: " + daoEx);
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

	private BigDecimal getIdResidenzaFascicoloSiusCorrente(BigDecimal aIdFascicoloSius) throws F3BException {

		Connection lConn = null;

		BigDecimal lId = null;

		ResidenzaFascicoloSiusDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new ResidenzaFascicoloSiusDAO(lConn);
			lSqlDao.setCondizioneResFascCorrente(aIdFascicoloSius, "R");
			ResidenzaFascicoloSiusModel lResFas = (ResidenzaFascicoloSiusModel) lSqlDao.getModelByKey();

			if (lResFas != null) {
				lId = lResFas.getResIdResidenza();
			}
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.getIdResidenzaFascicoloSiusCorrente: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lId;
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
					"FascicoloSiusController.existAttoSius: " + ex);
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
			// STUB 31/03/2004 Se insertEMA=true, va inserita una ESECUZIONE_MISURA_ALTERNATIVA in assenza di
			// ORDINANZA.
			// Altrimenti viene effettuato l'inserimento previsto in presenza dell'ordinanza.
			EsecuzioneMisuraAlternativaModel lMisAltModel = new EsecuzioneMisuraAlternativaModel();
			if (insertEMA) {
				// Inserimento dell' ESECUZIONE_MISURA_ALTERNATIVA.
				lEsMisAltDao = new EsecuzioneMisuraAlternativaDAO(lConn);
				lMisAltModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lMisAltModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
				lMisAltModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMisAltModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				// ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato campo di audit
				// lMisAltModel.setDataInserimento(
				// aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMisAltModel.setDataInserimento(DateUtils.getSysDate());

				lMisAltModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lMisAltModel.setDataOrdinanza(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getDataRichiesta());
				lMisAltModel.setCodAutoritaEmittOrd(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				// STUB 30/09/2004 Valorizzazione dei dati del Mittente dell'atto.
				lMisAltModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lMisAltModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// STUB 30/09/2004 CodTipoMisura valorizzato con il primo CodOggettoTenore.
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lMisAltModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lMisAltModel.setCodTipoMisura("-");

				// STUB 30/09/2004 Valorizzazione del codice Ufficio mittente, se presente.
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lMisAltModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				lMisAltModel.setDepOpidDepositoOrdinanzaPc(null);

				lEsMisAltDao.setDAOFromModel(lMisAltModel);
				/* BigDecimal lChiave = */lEsMisAltDao.insert();
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
				} else // STUB 03/03/2006 Lettura dell'Eventuale Decreto collegato.
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
					// STUB 03/03/2006 Dati prelevati dall'Ordinanza o dal Decreto collegato.
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
					// ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato campo di audit
					// lMisAltModel.setDataInserimento(
					// aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
					lMisAltModel.setDataInserimento(DateUtils.getSysDate());
					lMisAltModel.setGenPridGeneraleProcedimento(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lMisAltModel.setDataOrdinanza(aEvento.getDataEmissione());
					lMisAltModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());
					lMisAltModel.setCodTipoAutoritaEmittOrd("-");
					lMisAltModel.setCodLuogoAutoritaEmittOrd("-");
					lMisAltModel.setCodTipoMisura(aEvento.getCodMotivo());

					lEsMisAltDao.setDAOFromModel(lMisAltModel);
					/* BigDecimal lChiave = */lEsMisAltDao.insert();
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

			// 15/03/2004 Segnalazione della mancanza del Parametro.
			if (lVectPar.size() < 1)
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Errore: Parametro di Scadenzario mancante! ");

			lParMod = (ParametroModel) lVectPar.get(0);

			// Inserimento scadenzario.
			lScaDao = new ScadenzarioSiusDAO(lConn);
			ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
			lScaMod.setCodTipoScadenzario("70"); // ESECUZIONE MISURA ALTERNATIVA.
			lScaMod.setFasSiuIdFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			// MEV_39: MODIFICATA DATA
			lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));

			// 02/08/2004 Valorizzazione dell'ID_EVENTO, solo se è stato caricato l'evento in precedenza.
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
					"FascicoloSiusController.insEMAeScadenzario: " + daoEx);
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
	 * Metodo di cancellazione di ESECUZIONE_MISURA_ALTERNATIVA e SCADENZARIO_SIUS. a partire da
	 * aFascicoloGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void delEMAeScadenzario(FascicoloGPModel aFascicoloGPModel, Connection lConn)
			throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		EsecuzioneMisuraAlternativaDAO lEsMisAltDao = null;
		ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Cancellazione dell' ESECUZIONE_MISURA_ALTERNATIVA.
			lEsMisAltDao = new EsecuzioneMisuraAlternativaDAO(lConn);
			lEsMisAltDao.setCondizioneDeleteByGP(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lEsMisAltDao.delete();

			// Cancellazione dello Scadenzario.
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setCondizioneDeleteByTipo(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius(),
					"70");
			lScaDao.delete();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.delEMAeScadenzario: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lEsMisAltDao);
			cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Metodo di scrittura di ESECUZIONE_SANZIONE_SOSTITUTIVA e SCADENZARIO_SIUS. a partire da
	 * aFascicoloGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void insESSeScadenzario(FascicoloGPModel aFascicoloGPModel, boolean insertESS, Connection lConn)
			throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEsSanzSostDao = null;
		// ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// STUB 31/03/2004 Se insertESS=true, va inserita una ESECUZIONE_SANZIONE_SOSTITUTIVA in assenza
			// di ORDINANZA.
			// Altrimenti viene effettuato l'inserimento previsto in presenza dell'ordinanza.
			EsecuzioneSanzioneSostitutivaModel lSanzSostModel = new EsecuzioneSanzioneSostitutivaModel();
			if (insertESS) {
				// Inserimento dell' ESECUZIONE_SANZIONE_SOSTITUTIVA.
				lEsSanzSostDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
				lSanzSostModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lSanzSostModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
				lSanzSostModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lSanzSostModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				// ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato campo di audit
				// lSanzSostModel.setDataInserimento(
				// aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lSanzSostModel.setDataInserimento(DateUtils.getSysDate());
				lSanzSostModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lSanzSostModel.setDataOrdinanza(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getDataRichiesta());
				lSanzSostModel.setCodAutoritaEmittOrd(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				// STUB 30/09/2004 Valorizzazione dei dati del Mittente dell'atto.
				lSanzSostModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lSanzSostModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// STUB 30/09/2004 CodTipoMisura valorizzato con il primo CodOggettoTenore.
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lSanzSostModel.setCodTipoSanzione(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lSanzSostModel.setCodTipoSanzione("-");

				// STUB 30/09/2004 Valorizzazione del codice Ufficio mittente, se presente.
				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lSanzSostModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				lSanzSostModel.setDepOpidDepositoOrdinanzaPc(null);

				lEsSanzSostDao.setDAOFromModel(lSanzSostModel);
				/* BigDecimal lChiave = */lEsSanzSostDao.insert();
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
				} else // STUB 03/03/2006 Lettura dell'Eventuale Decreto collegato.
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
					// Inserimento dell' ESECUZIONE_SANZIONE_SOSTITUTIVA.
					// STUB 21/06/2009 Dati prelevati dall'Ordinanza o dal Decreto collegato.
					if (lDepOrdMod != null) {
						lEsSanzSostDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
						lSanzSostModel.setAnnoS07(lDepOrdMod.getAnnoS3());
						lSanzSostModel.setProgrS07(lDepOrdMod.getNumS3());
						lSanzSostModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
						if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
							lSanzSostModel.setLuogoEsecuzioneSanzione(lDepOrdMod.getLuogoSvolgimentoProva());
					}
					if (lDepDecMod != null) {
						lEsSanzSostDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
						lSanzSostModel.setAnnoS07(lDepDecMod.getAnnoS72());
						lSanzSostModel.setProgrS07(lDepDecMod.getNumS72());
						lSanzSostModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
						if (lDepDecMod.getLuogoSvolgimentoProva() != null)
							lSanzSostModel.setLuogoEsecuzioneSanzione(lDepDecMod.getLuogoSvolgimentoProva());
					}

					lSanzSostModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lSanzSostModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					// ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato campo di audit
					// lSanzSostModel.setDataInserimento(
					// aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
					lSanzSostModel.setDataInserimento(DateUtils.getSysDate());
					lSanzSostModel.setGenPridGeneraleProcedimento(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lSanzSostModel.setDataOrdinanza(aEvento.getDataEmissione());
					lSanzSostModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());
					lSanzSostModel.setCodTipoAutoritaEmittOrd("-");
					lSanzSostModel.setCodLuogoAutoritaEmittOrd("-");
					lSanzSostModel.setCodTipoSanzione(aEvento.getCodMotivo());

					lEsSanzSostDao.setDAOFromModel(lSanzSostModel);
					/* BigDecimal lChiave = */lEsSanzSostDao.insert();
				}
			}
			/***
			 * NESSUNA MODIFICA ALLO SCADENZARIO AL MOMENTO // Lettura Tabella PARAMETRO x Sanzione
			 * Sostitiutiva. ParametroModel lParMod = new ParametroModel();
			 *
			 * lParMod.setNomeParametro("Termine Sanzione Sostitutiva");
			 * lParMod.setCodUfficioValidita(lSanzSostModel.getCodUfficioInserimento());
			 *
			 * Vector lVectPar = null;
			 *
			 * lParDao = new ParametroSqlDAO(lConn);
			 * lParDao.ricercaParametroScadenzario(lParMod.getNomeParametro(),
			 * lParMod.getCodUfficioValidita()); lVectPar = new Vector(lParDao.getModels());
			 *
			 * // 15/03/2004 Segnalazione della mancanza del Parametro. if (lVectPar.size() < 1) throw new
			 * SIUSException(F3BException.USER_MESSAGE, "Errore: Parametro di Scadenzario mancante! ");
			 *
			 * lParMod = (ParametroModel) lVectPar.get(0);
			 *
			 * // Inserimento scadenzario. lScaDao = new ScadenzarioSiusDAO(lConn); ScadenzarioSiusModel
			 * lScaMod = new ScadenzarioSiusModel(); lScaMod.setCodTipoScadenzario("80"); //ESECUZIONE
			 * SANZIONE SOSTITUTIVA.
			 * lScaMod.setFasSiuIdFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel()
			 * .getIdFascicoloSius()); lScaMod.setDataInizioScadenza(DateUtils.getSysDate());
			 *
			 * //02/08/2004 Valorizzazione dell'ID_EVENTO, solo se è stato caricato l'evento in precedenza. if
			 * (lEveSqlDao != null) { EventoModel aEvento = (EventoModel) lEveSqlDao.getModelByKey(); if
			 * (aEvento != null && aEvento.getIdEvento() != null)
			 * lScaMod.setEveIdEvento(aEvento.getIdEvento()); }
			 *
			 * // Impostazione della Data Fine Scadenza in base al periodo di PARAMETRO.
			 * lScaMod.setDataFineScadenza
			 * (DateUtils.moveDateTo(DateUtils.getSysDate(),java.util.Calendar.DAY_OF_MONTH,
			 * lParMod.getGiorni().intValue()));
			 * lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
			 * java.util.Calendar.MONTH, lParMod.getMesi().intValue()));
			 * lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
			 * java.util.Calendar.YEAR, lParMod.getAnni().intValue()));
			 *
			 * lScaMod.setCodOperatoreInserimento(aFascicoloGPModel.getFascicoloSiusModel().
			 * getCodOperatoreInserimento());
			 * lScaMod.setCodUfficioInserimento(aFascicoloGPModel.getFascicoloSiusModel
			 * ().getCodUfficioInserimento());
			 * lScaMod.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
			 *
			 * lScaDao.setDAOFromModel(lScaMod); lScaDao.insert(); lScaDao.stop(); NESSUNA MODIFICA ALLO
			 * SCADENZARIO AL MOMENTO
			 ***/
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.insEMAeScadenzario: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lEsSanzSostDao);
			// cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Metodo di cancellazione di ESECUZIONE_SANZIONE_SOSTITUTIVA e SCADENZARIO_SIUS. a partire da
	 * aFascicoloGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void delESSeScadenzario(FascicoloGPModel aFascicoloGPModel, Connection lConn)
			throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEsSanzSostDao = null;
		// ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Cancellazione dell' ESECUZIONE_SANZIONE_SOSTITUTIVA.
			lEsSanzSostDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEsSanzSostDao.setCondizioneDeleteByGP(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lEsSanzSostDao.delete();

			// Cancellazione dello Scadenzario.
			// lScaDao = new ScadenzarioSiusDAO(lConn);
			// lScaDao.setCondizioneDeleteByTipo(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius(),
			// "80" );
			// lScaDao.delete();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.delEMAeScadenzario: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lEsSanzSostDao);
			// cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Metodo di scrittura di ESECUZIONE_MISURA_SICUREZZA e SCADENZARIO_SIUS. a partire da aFascicoloGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void insEMSeScadenzario(FascicoloGPModel aFascicoloGPModel, boolean insertEMS, Connection lConn)
			throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		EsecuzioneMisuraSicurezzaDAO lEsMisSicDao = null;
		// ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// 02/05/2011 Se insertEMS=true, va inserita una ESECUZIONE_MISURA_SICUREZZA in assenza di
			// ORDINANZA.
			// Altrimenti viene effettuato l'inserimento previsto in presenza dell'ordinanza.
			EsecuzioneMisuraSicurezzaModel lMisSicModel = new EsecuzioneMisuraSicurezzaModel();
			if (insertEMS) {
				// Inserimento dell' ESECUZIONE_MISURA_SICUREZZA.
				lEsMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
				lMisSicModel.setAnnoS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getAnnoS1());
				lMisSicModel.setProgrS07(aFascicoloGPModel.getGeneraleProcedimentoModel().getProgrS1());
				lMisSicModel.setCodOperatoreInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
				lMisSicModel.setCodUfficioInserimento(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				// ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato campo di audit
				// lMisSicModel.setDataInserimento(
				// aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
				lMisSicModel.setDataInserimento(DateUtils.getSysDate());
				lMisSicModel.setGenPridGeneraleProcedimento(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
				lMisSicModel.setDataOrdinanza(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getDataRichiesta());
				lMisSicModel.setCodAutoritaEmittOrd(
						aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
				lMisSicModel.setCodTipoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodTipoMittenteAtto());
				lMisSicModel.setCodLuogoAutoritaEmittOrd(
						aFascicoloGPModel.getGeneraleProcedimentoModel().getCodSedeMittente());
				// 02/05/2011 CodTipoMisura valorizzato con il primo CodOggettoTenore.
				if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
						&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
					lMisSicModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
				else
					lMisSicModel.setCodTipoMisura("-");

				if (aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().length() > 1)
					lMisSicModel.setCodAutoritaEmittOrd(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getCodUfficioMittente().trim());

				lMisSicModel.setDepOpidDepositoOrdinanzaPc(null);

				lEsMisSicDao.setDAOFromModel(lMisSicModel);
				/* BigDecimal lChiave = */lEsMisSicDao.insert();
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
				} else // STUB 03/03/2006 Lettura dell'Eventuale Decreto collegato.
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
					// Inserimento dell' ESECUZIONE_MISURA_SICUREZZA.
					// STUB 21/06/2009 Dati prelevati dall'Ordinanza o dal Decreto collegato.
					if (lDepOrdMod != null) {
						lEsMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
						lMisSicModel.setAnnoS07(lDepOrdMod.getAnnoS3());
						lMisSicModel.setProgrS07(lDepOrdMod.getNumS3());
						lMisSicModel.setDepOpidDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());
						if (lDepOrdMod.getLuogoSvolgimentoProva() != null)
							lMisSicModel.setLuogoEsecuzioneMisura(lDepOrdMod.getLuogoSvolgimentoProva());
					}
					if (lDepDecMod != null) {
						lEsMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
						lMisSicModel.setAnnoS07(lDepDecMod.getAnnoS72());
						lMisSicModel.setProgrS07(lDepDecMod.getNumS72());
						lMisSicModel.setDepDecIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());
						if (lDepDecMod.getLuogoSvolgimentoProva() != null)
							lMisSicModel.setLuogoEsecuzioneMisura(lDepDecMod.getLuogoSvolgimentoProva());
					}

					lMisSicModel.setCodOperatoreInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodOperatoreInserimento());
					lMisSicModel.setCodUfficioInserimento(
							aFascicoloGPModel.getFascicoloSiusModel().getCodUfficioInserimento());
					// ISSUE MAC : Ticket#20200610014 — Anomalia SIES: modificato campo di audit
					// lMisSicModel.setDataInserimento(
					// aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
					lMisSicModel.setDataInserimento(DateUtils.getSysDate());
					lMisSicModel.setGenPridGeneraleProcedimento(
							aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
					lMisSicModel.setDataOrdinanza(aEvento.getDataEmissione());
					lMisSicModel.setCodAutoritaEmittOrd(aEvento.getCodUfficioEmittente());
					lMisSicModel.setCodTipoAutoritaEmittOrd("-");
					lMisSicModel.setCodLuogoAutoritaEmittOrd("-");
					// 02/05/2011 CodTipoMisura sempre valorizzato con il primo CodOggettoTenore.
					if (aFascicoloGPModel.getTenori().length > 0 && aFascicoloGPModel.getTenori()[0] != null
							&& aFascicoloGPModel.getTenori()[0].getCodOggettoTenore() != null)
						lMisSicModel.setCodTipoMisura(aFascicoloGPModel.getTenori()[0].getCodOggettoTenore());
					else
						lMisSicModel.setCodTipoMisura("-");

					// lMisSicModel.setCodTipoMisura(aEvento.getCodMotivo());

					lEsMisSicDao.setDAOFromModel(lMisSicModel);
					/* BigDecimal lChiave = */lEsMisSicDao.insert();
				}
			}
			/***
			 * NESSUNA MODIFICA ALLO SCADENZARIO AL MOMENTO // Lettura Tabella PARAMETRO x Misura Sicurezza.
			 * ParametroModel lParMod = new ParametroModel();
			 *
			 * lParMod.setNomeParametro("Termine Misura Sicurezza");
			 * lParMod.setCodUfficioValidita(lMisSicModel.getCodUfficioInserimento());
			 *
			 * Vector lVectPar = null;
			 *
			 * lParDao = new ParametroSqlDAO(lConn);
			 * lParDao.ricercaParametroScadenzario(lParMod.getNomeParametro(),
			 * lParMod.getCodUfficioValidita()); lVectPar = new Vector(lParDao.getModels());
			 *
			 * // 15/03/2004 Segnalazione della mancanza del Parametro. if (lVectPar.size() < 1) throw new
			 * SIUSException(F3BException.USER_MESSAGE, "Errore: Parametro di Scadenzario mancante! ");
			 *
			 * lParMod = (ParametroModel) lVectPar.get(0);
			 *
			 * // Inserimento scadenzario. lScaDao = new ScadenzarioSiusDAO(lConn); ScadenzarioSiusModel
			 * lScaMod = new ScadenzarioSiusModel(); lScaMod.setCodTipoScadenzario("83"); //ESECUZIONE MISURA
			 * SICUREZZA.
			 * lScaMod.setFasSiuIdFascicoloSius(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius
			 * ()); lScaMod.setDataInizioScadenza(DateUtils.getSysDate());
			 *
			 * //02/08/2004 Valorizzazione dell'ID_EVENTO, solo se è stato caricato l'evento in precedenza. if
			 * (lEveSqlDao != null) { EventoModel aEvento = (EventoModel) lEveSqlDao.getModelByKey(); if
			 * (aEvento != null && aEvento.getIdEvento() != null)
			 * lScaMod.setEveIdEvento(aEvento.getIdEvento()); }
			 *
			 * // Impostazione della Data Fine Scadenza in base al periodo di PARAMETRO.
			 * lScaMod.setDataFineScadenza
			 * (DateUtils.moveDateTo(DateUtils.getSysDate(),java.util.Calendar.DAY_OF_MONTH,
			 * lParMod.getGiorni().intValue()));
			 * lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
			 * java.util.Calendar.MONTH, lParMod.getMesi().intValue()));
			 * lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataFineScadenza(),
			 * java.util.Calendar.YEAR, lParMod.getAnni().intValue()));
			 *
			 * lScaMod.setCodOperatoreInserimento(aFascicoloGPModel.getFascicoloSiusModel().
			 * getCodOperatoreInserimento());
			 * lScaMod.setCodUfficioInserimento(aFascicoloGPModel.getFascicoloSiusModel
			 * ().getCodUfficioInserimento());
			 * lScaMod.setDataInserimento(aFascicoloGPModel.getFascicoloSiusModel().getDataInserimento());
			 *
			 * lScaDao.setDAOFromModel(lScaMod); lScaDao.insert(); lScaDao.stop(); NESSUNA MODIFICA ALLO
			 * SCADENZARIO AL MOMENTO
			 ***/
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.insEMSeScadenzario: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lEsMisSicDao);
			// cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Metodo di cancellazione di ESECUZIONE_MISURA_SICUREZZA e SCADENZARIO_SIUS. a partire da
	 * aFascicoloGPModel.
	 * <p>
	 *
	 * @param aFasGPModel
	 * @param lConn
	 * @throws F3BException
	 */
	private void delEMSeScadenzario(FascicoloGPModel aFascicoloGPModel, Connection lConn)
			throws F3BException {

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		EsecuzioneMisuraSicurezzaDAO lEsMisSicDao = null;
		// ScadenzarioSiusDAO lScaDao = null;
		ParametroSqlDAO lParDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Cancellazione dell' ESECUZIONE_MISURA_SICUREZZA.
			lEsMisSicDao = new EsecuzioneMisuraSicurezzaDAO(lConn);
			lEsMisSicDao.setCondizioneDeleteByGP(
					aFascicoloGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lEsMisSicDao.delete();

			// Cancellazione dello Scadenzario.
			// lScaDao = new ScadenzarioSiusDAO(lConn);
			// lScaDao.setCondizioneDeleteByTipo(aFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius(),
			// "83" );
			// lScaDao.delete();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.delEMSeScadenzario: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lEsMisSicDao);
			// cleanup(lScaDao);
			cleanup(lParDao);
			cleanup(lEveSqlDao);
		}
	}

	/**
	 * Verifica se 2 Codici Ufficio appartengono allo stesso distretto.
	 *
	 * @param aFascicoloSius
	 * @param aConn
	 * @return aCond
	 * @throws F3BException
	 */
	private boolean stressoDistretto(FascicoloSiusModel aFascicoloSius, Connection aConn)
			throws F3BException {

		UfficioSqlDAO lUDao = null;
		boolean aCond = false;

		try {
			lUDao = new UfficioSqlDAO(aConn);
			lUDao.stessoDistretto(aFascicoloSius.getChiaveUfficio(),
					aFascicoloSius.getFasSieIdFascicoloSiep());
			lUDao.start();

			if (lUDao.next()) {
				aCond = true;
			}
			lUDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.stessoDistretto: " + daoEx);
		} finally {
			cleanup(lUDao);
		}
		return aCond;
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
					"FascicoloSiusController.ExInserisciDefinizioneFascicoloSius : " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExInserisciDefinizioneFascicoloSius : " + ex);
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
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
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
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaPareriPaginata: " + daoEx);
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

	/**
	 * Ricerca il Fascicolo SIUS di collegamento in Banca Dati per primary key; Non si propaga errore di
	 * eccezione in caso di mancato recupero del Fascicolo
	 * <p>
	 *
	 * @param aIdFascicoloSius
	 * @return FascicoloGPModel
	 * @throws F3BException
	 */
	public FascicoloGPModel ExRicercaFascicoloCollegato(BigDecimal aIdFascicoloSius) throws F3BException {

		Connection lConn = null;

		FascicoloGPModel lFascicolo = null;
		FascicoloGPSqlDAO lFascDao = null;

		try {
			lConn = getDBConnection();
			lFascDao = new FascicoloGPSqlDAO(lConn);

			lFascDao.ricercaFascicoloByKey(aIdFascicoloSius);
			lFascicolo = (FascicoloGPModel) lFascDao.getModelByKey();
			if (lFascicolo == null)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info(F3BException.USER_MESSAGE + " Fascicolo SIUS non trovato");
		}

		catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
		} finally {
			cleanup(lFascDao);
			cleanup(lConn);
		}
		return lFascicolo;
	}

	/**
	 * Ricerca l'elenco dei fascicoli correntemente assegnati a un magistrato su un particolare ufficio in
	 * base allo stato del fascicolo
	 *
	 * @param aCodMagistrato
	 *            - Codice CSM del magistrato
	 * @param aCodUfficio
	 *            - Codice ufficio di appartenenza del Procedimento
	 * @param aStato
	 *            - Array di COD_STATO_FASCICOLO
	 * @return
	 */
	public Vector ExRicercaFascicoliByMagistratoSorvAssegnatario(String aCodMagistrato, String aCodUfficio,
			String[] aStato) throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSiusSqlDAO lFasSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasSqlDao = new FascicoloSiusSqlDAO(lConn);
			lFasSqlDao.ricercaFascicoloSiusByMagistratoSorvAssegnatario(aCodMagistrato, aCodUfficio, aStato);
			lFascicoli = new Vector(lFasSqlDao.getModels());
			// lFasSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaFascicoliByMagistratoSorvAssegnatario: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Prova a recuperare per il fascicolo SIUS indicato, la data emissione del record DOCUMENTO_ALLEGATO
	 * collegato all'ultimo evento (id_evento max) collegato al fascicolo. Tale data può essere utilizzata
	 * come data definizione del procedimento se assente sul FASCICOLO_SIUS
	 *
	 * @param aIdFascicoloSius
	 *            - Id Fascicolo SIUS
	 * @param -
	 *            Eventuale connessione da utilizzare
	 * @return - DOCUMENTO_ALLEGATO.DATA_EMISSIONE o null se non presente
	 * @throws Exception
	 */
	public Date ExGetDataDefinizineFinale(BigDecimal aIdFascicoloSius, Connection aConn) throws Exception {

		Connection lConn = null;

		FascicoloSiusSoggettoSqlDAO lFasSoggSqlDao = null;

		Date lDataDefinizioneFinale = null;

		boolean lIsConnInInput = false;

		if (aConn != null)
			lIsConnInInput = true;

		try {
			if (lIsConnInInput)
				lConn = aConn;
			else
				lConn = getDBConnection();

			lFasSoggSqlDao = new FascicoloSiusSoggettoSqlDAO(lConn);
			lFasSoggSqlDao.ricercaDataDefinizioneFinaleByIdFasc(aIdFascicoloSius);
			lFasSoggSqlDao.start();

			if (lFasSoggSqlDao.next())
				lDataDefinizioneFinale = lFasSoggSqlDao.getDate("DATA_DEFINIZIONE_EVENTO");

			lFasSoggSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExGetDataDefinizineFinale: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasSoggSqlDao);

			if (!lIsConnInInput)
				cleanup(lConn);
		}
		return lDataDefinizioneFinale;
	}

	public ByteArrayOutputStream ExGetCertificatoPenale(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FascicoloSiusSqlDAO lFasDao = null;
		ByteArrayOutputStream lByteArrayOut = null;
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiusSqlDAO(lConn);

			lFasDao.getCertificatoPenaleByIdFascicolo(aKey);
			lFasDao.start();

			if (lFasDao.next()) {
				lByteArrayOut = lFasDao.getBlob("CERTIFICATO_PENALE");
				lFasDao.stop();
			}

			if ((lByteArrayOut == null) || (lByteArrayOut.size() == 0))
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Certificato Giudiziale Associato");
		} catch (F3BException eF3b) {
			throw eF3b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	public void ExInsertCertificatoPenale(FascicoloSiusCertBlobModel fascicolo) throws F3BException {

		Connection lConn = null;
		FascicoloSiusDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiusDAO(lConn);
			lFasDao.setDAOFromModelForUpdateBlob(fascicolo);
			lFasDao.setCondizioneUpdate(fascicolo.getFascicoloSius().getIdFascicoloSius());
			lFasDao.update();
			commit(lConn);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("FascicoloSiusController.ExInsertCertificatoPenale: " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	public BigDecimal ExGetLengthCertPenaleByIdFascicolo(BigDecimal aIdFascicoloSius) throws F3BException {

		Connection lConn = null;

		FascicoloSiusSqlDAO lFasDao = null;
		BigDecimal lengthCertPenale = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiusSqlDAO(lConn);

			lFasDao.getLengthCertPenaleByIdFascicolo(aIdFascicoloSius);
			lFasDao.start();

			if (lFasDao.next()) {
				lengthCertPenale = lFasDao.getBigDecimal("LEN_BLOB_CERT_PENALE");
			}

			lFasDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ExRicercaLengthCertPenaleByIdFascicolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lengthCertPenale;
	}

	/**
	 * MEV10-s3: aggiunto metodo per gestire passaggio alla maggiore età del soggetto
	 *
	 * @param lFasGPMod
	 * @throws F3BException
	 */
	public void ExModificaVisibilitaMinoreFascicoloSius(FascicoloGPModel lFasGPMod) throws F3BException {

		Connection lConn = null;
		FascicoloSiusDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiusDAO(lConn);

			lFasDao.setVisibilitaMinorenne(lFasGPMod.getFascicoloSiusModel().getVisibilitaMinorenne());
			lFasDao.setCodOperatoreAggiornamento(
					lFasGPMod.getFascicoloSiusModel().getCodOperatoreAggiornamento());
			lFasDao.setDataAggiornamento(lFasGPMod.getFascicoloSiusModel().getDataAggiornamento());
			lFasDao.setCodUfficioAggiornamento(
					lFasGPMod.getFascicoloSiusModel().getCodUfficioAggiornamento());
			lFasDao.setCondizioneUpdate(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("FascicoloSiepController.ExModificaNoteFascicoloSiep: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * AVVOCATURA: aggiunto metodo di ricerca
	 */
	public Vector ricercaSoggettiConProcedimenti(SoggettoModel sm, String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio) throws F3BException {

		// info per il log
		avvocaturaLogger.debug(
				"Starting Point della classe: FascicoloSiusController, metodo: ricercaSoggettiConProcedimenti");

		Connection connection = null;
		Vector soggettiConProcedimenti = new Vector();
		FascicoloSiusSoggettoSqlDAO fsssd = null;
		SoggettoSqlDAO ssd = null;
		FascicoloGPModel fgpm = null;

		try {
			connection = getDBConnection();
			fsssd = new FascicoloSiusSoggettoSqlDAO(connection);
			fsssd.ricercaSoggettiConProcedimenti(sm, codDistretto, codFiscaleAvvocato, codTipoUfficio);
			fsssd.start();
			while (fsssd.next()) {
				fgpm = (FascicoloGPModel) fsssd.getFascicoloSiusGPModelUnico();
				// inizializzo l'oggetto di tipo "SoggettoSqlDAO"
				ssd = new SoggettoSqlDAO(connection);
				// effettuo la ricerca del soggetto
				// ssd.ricercaSuperSoggettoConProcedimenti(fgpm.getFascicoloSiusModel().getSoggetto(),
				// codDistretto, codFiscaleAvvocato, codTipoUfficio);
				ssd.ricercaSoggettoByKey(fgpm.getFascicoloSiusModel().getSogIdSoggetto());
				// popolo di tutti i dati il model del soggetto
				sm = (SoggettoModel) ssd.getModelByKey();
				// ricarico il model del soggetto nel fascicolo Sius
				fgpm.getFascicoloSiusModel().setSoggetto(sm);
				// aggiorno SogIdSoggetto del fascicolo
				if (sm != null)
					fgpm.getFascicoloSiusModel().setSogIdSoggetto(sm.getIdSoggetto());
				else
					fgpm.getFascicoloSiusModel().setSogIdSoggetto(null);
				// aggiungo alla lista
				soggettiConProcedimenti.add(fgpm);
			}
			fsssd.stop();
		} catch (DAOException daoEx) {
			rollback(connection);
			avvocaturaLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.ricercaSoggettiConProcedimenti: " + daoEx);
		} catch (Exception e) {
			rollback(connection);
			avvocaturaLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			// stop degli oggetti di tipo "GenericDAO"
			cleanup(fsssd);
			cleanup(ssd);
			cleanup(connection);
		}
		// valore di ritorno
		return soggettiConProcedimenti;
	}

	/**
	 * AVVOCATURA: aggiunto metodo di ricerca
	 */
	public Vector elencoProcedimentiDelSoggetto(BigDecimal idSoggetto, String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio) throws F3BException {

		// info per il log
		avvocaturaLogger.debug(
				"Starting Point della classe: FascicoloSiusController, metodo: elencoProcedimentiDelSoggetto");

		Connection connection = null;
		Vector procedimentiDelSoggetto = new Vector();
		FascicoloSiusSoggettoSqlDAO fsssd = null;
		// SoggettoSqlDAO ssd = null;

		try {
			connection = getDBConnection();
			// inizializzo l'oggetto di tipo "SoggettoSqlDAO"
			// ssd = new SoggettoSqlDAO(connection);
			// effettuo la ricerca del soggetto
			// ssd.ricercaSoggettoByKey(idSoggetto);
			// popolo di tutti i dati il model del soggetto
			// SoggettoModel sm = (SoggettoModel) ssd.getModelByKey();
			fsssd = new FascicoloSiusSoggettoSqlDAO(connection);
			fsssd.elencoProcedimentiDelSoggetto(idSoggetto, codDistretto, codFiscaleAvvocato, codTipoUfficio);
			fsssd.start();
			FascicoloGPModel fgpm = null;
			while (fsssd.next()) {
				fgpm = (FascicoloGPModel) fsssd.getFascicoloSiusGPModelForProvv();
				// reimposto il model del soggetto nel Fascicolo Sius
				// fgpm.getFascicoloSiusModel().setSoggetto(sm);
				// aggiungo alla lista
				procedimentiDelSoggetto.add(fgpm);
			}
			fsssd.stop();
		} catch (DAOException daoEx) {
			rollback(connection);
			avvocaturaLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.elencoProcedimentiDelSoggetto: " + daoEx);
		} catch (Exception e) {
			rollback(connection);
			avvocaturaLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			// stop degli oggetti di tipo "GenericDAO"
			cleanup(fsssd);
			// cleanup(ssd);
			cleanup(connection);
		}

		// valore di ritorno
		return procedimentiDelSoggetto;
	}

	/**
	 * AVVOCATURA: aggiunto metodo di richiesta stampa
	 */
	public ByteArrayOutputStream richiestaStampa(BigDecimal idFascicoloSius, String codDistretto,
			String codiceFiscaleAvvocato, String codTipoUfficio, String codUfficio) throws F3BException {

		// info per il log
		avvocaturaLogger
				.debug("Starting Point della classe: FascicoloSiusController, metodo: richiestaStampa");

		// instanzio un oggetto di tipo "ByteArrayOutputStream"
		ByteArrayOutputStream baos = null;
		IStampaSius iss = SIUSLookupRemote.getStampaRemote();

		// riempio l'Array contenente le tipologie di dati da prelevare
		int[] tree = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_FASCICOLOSIEP_ALL,
				ICostantiStampaSius.TREE_SENTENZA, ICostantiStampaSius.TREE_AVVOCATO,
				ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_MAGISTRATO,
				ICostantiStampaSius.TREEs_RICHIESTE_ISTRUTTORIE, ICostantiStampaSius.TREEs_PROVVEDIMENTI,
				ICostantiStampaSius.TREEs_PROVVEDIMENTI_ALTRI, ICostantiStampaSius.TREE_TIT_ESE_REF,
				ICostantiStampaSius.TREE_RIF_FAS_SIEP, ICostantiStampaSius.TREE_ESECUZIONEMISURAALTERNATIVA,
				ICostantiStampaSius.TREE_UDIENZA };

		// prelevo i dati da stampare
		TreeModel tm = iss.ExPrelevaDatiStampa(idFascicoloSius, tree, codUfficio);

		// intervento per MEV 64- AVVOCATURA (anche in stampa devono apparire solo le ordinanze/decreti
		// depositati)
		EventoModel lEvento = new EventoModel();
		EventoModel lEventoDep = null;
		lEvento.setCodTipoEvento("01");
		// lEvento.setCodTipoProvvedimento(getRequestStringParameter(this.CAMPO_TIPO_PROVVEDIMENTO));
		lEvento.setFlagDocumentoRegistrato("S");
		lEvento.setFasSiuIdFascicoloSius(idFascicoloSius);
		// Ricerca
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		Vector lProvValidati = mCtrl.ExRicercaProvvedimentiConDataDeposito(lEvento);
		// Vector lProvDepositati = new Vector();
		// L'elenco dei Provvedimenti Validati viene analizzato per
		// restituire solo quelli Depositati e non già Revocati
		Iterator lItx = lProvValidati.iterator();
		while (lItx.hasNext()) {
			lEventoDep = (EventoModel) lItx.next();
			tm.add(new TreeModel(lEventoDep));
		}

		// instanzio ed inizializzo un oggetto di tipo "ReportGenerator"
		ReportGenerator report = new ReportGenerator(codUfficio);

		// recupero il nome del template di stampa
		// intervento per MEV 64- AVVOCATURA (introduco un nuovo template SIUS_ST_001_AVV)
		String nomeTemplate = TemplateManager.getInstance().getTemplateName("SIUS_ST_001_AVV");
		// nomeTemplate = nomeTemplate.replace("/", "\\").replace("\\var\\SIES", "C:");
		baos = (ByteArrayOutputStream) report.generateDocument(tm, nomeTemplate);

		// valore di ritorno
		return baos;
	}

	/**
	 * AVVOCATURA: aggiunto metodo di ricerca
	 */
	public String ricercaCodUfficioAppartenenza(BigDecimal idFascicoloSius, String codiceFiscaleAvvocato,
			String codDistretto, String codTipoUfficio) throws F3BException {

		// info per il log
		avvocaturaLogger.debug(
				"Starting Point della classe: FascicoloSiusController, metodo: ricercaCodUfficioAppartenenza");

		// instanzio oggetti di tipo "Connection" e "AvvocatoSqlDAO" e "AvvocatoModel"
		Connection connection = null;
		AvvocatoSqlDAO asd = null;
		AvvocatoModel am = null;

		try {
			// inizializzo l'oggetto di tipo "Connection"
			connection = getDBConnection();
			// inizializzo l'oggetto di tipo "AvvocatoSqlDAO"
			asd = new AvvocatoSqlDAO(connection);
			// eseguo query di ricerca
			asd.ricercaCodUfficioAppartenenza(idFascicoloSius, codDistretto, codiceFiscaleAvvocato,
					codTipoUfficio);
			asd.start();
			if (asd.next())
				// valorizzo il model
				am = (AvvocatoModel) asd.getModel();
			else
				// inizializzo l'oggetto di tipo "AvvocatoModel"
				am = new AvvocatoModel();
			asd.stop();
		} catch (DAOException ex) {
			// info per il log
			avvocaturaLogger.error("DAOException: " + ex);
			// lancio nuova eccezione
			throw new SIEPException("FascicoloSiusController.ricercaCodUfficioAppartenenza: " + ex);
		} finally {
			// stop degli oggetti di tipo "GenericDAO"
			cleanup(asd);
			cleanup(connection);
		}

		// valore di ritorno
		return am.getCodUffAppartenenza();
	}

	/*
	 * ISSUE MEV : aggiunto aggiornamento stato fascicolo per decreto di tipo DM 
	 * Numero MEV : 9 
	 * Autore : Gioggi 
	 * Data : 19 nov 2020 
	 * Branch : MEV_9
	 */
	public void aggiornaStatoFascicoloSius(FascicoloSiusModel fsm) throws F3BException {

		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			if (ICostantiFascicoloSius.COD_EMESSO_DECRETO_DESIGNAZIONE.equals(fsm.getCodStatoFascicolo())
					|| ICostantiFascicoloSius.COD_ISCRITTO.equals(fsm.getCodStatoFascicolo())) {
				FascicoloGPModel fgpm = ExRicercaFascicoloByKey(fsm.getIdFascicoloSius());
				FascicoloSiusModel fsmOld = fgpm.getFascicoloSiusModel();
				// Set del DAO e aggiornamento del FascicoloSius
				lFasDao.setDAOFromModel(fsmOld);
				lFasDao.setDataAggiornamento(fsm.getDataAggiornamento());
				lFasDao.setCodOperatoreAggiornamento(fsm.getCodOperatoreAggiornamento());
				lFasDao.setCodUfficioAggiornamento(fsm.getCodUfficioAggiornamento());
				lFasDao.setCodStatoFascicolo(fsm.getCodStatoFascicolo());
				lFasDao.setCondizioneUpdateStatoFascicolo(fsmOld.getIdFascicoloSius(),
						fsmOld.getCodStatoFascicolo());
			}
			lFasDao.update();
			lFasDao.stop();

			// COMMIT
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"FascicoloSiusController.aggiornaStatoFascicoloSius: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}
	// ***** FINE INTERVENTO MEV_9 *****//

}