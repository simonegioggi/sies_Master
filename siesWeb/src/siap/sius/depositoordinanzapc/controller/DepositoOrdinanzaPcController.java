package siap.sius.depositoordinanzapc.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.decodifiche.dao.DecodificheDAO;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoesecuzione.action.ICostantiStatoEsecuzione; // 06/04/2011
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.depositoordinanzapc.util.GestioneFlussoOrdinanza;
import siap.sius.depositosentenza.dao.DepositoSentenzaDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoDAO;
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaDAO;
import siap.sius.esecuzionemisurasicurezza.dao.EsecuzioneMisuraSicurezzaSqlDAO;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaSqlDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoDAO;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.misurasicurezza.dao.PeriodoAltraMisuraDAO;
import siap.sius.misurasicurezza.dao.PeriodoAltraMisuraSqlDAO;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.penapecuniaria.model.RichiesteConversioniPerOrdinanzaModel;
import siap.sius.prescrizione.dao.PrescrizioneDAO;
import siap.sius.prescrizione.dao.PrescrizioneSqlDAO;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneDAO;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneSqlDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * DepositoOrdinanzaPcController - Classe Controller per DepositoOrdinanzaPc
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DepositoOrdinanzaPcController extends SiapController implements IDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue l'inserimento dell'Evento, DepositoOrdinanza e tenori.
	 *
	 * @param aModel
	 * @return lModel
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public OrdinanzaEventoTenoriModel ExInserisciEventoDepositoOrdinanzaPc(OrdinanzaEventoTenoriModel aModel)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		EventoSqlDAO lSqlDAO = null;
		DepositoOrdinanzaPcDAO lDepDao = null;

		DepositoOrdinanzaPcModel lDepMod = null;
		OrdinanzaEventoTenoriModel lModel = new OrdinanzaEventoTenoriModel();

		// Da oggi e' possibile inserire + ordinanze nella stessa data. Luigi 17-5-2004
		/*
		 * if( ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(aModel.getOrdinanza().
		 * getGenPridGeneraleProcedimento (),aModel.getEvento().getDataEmissione())) { throw new
		 * SIUSException(SIUSException.USER_MESSAGE,
		 * "Operazione non consentita. Per il procedimento indicato e' gia' stata emessa una ordinanza nella stessa data."
		 * ); }
		 */
		try {
			// Insert DepositoOrdinanzaPc
			lConn = getDBTransaction();
			lDepMod = new DepositoOrdinanzaPcModel(aModel.getOrdinanza());
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);

			lDepDao.setDAOFromModel(aModel.getOrdinanza());
			BigDecimal lKeyDepOrd = lDepDao.insert();
			lDepDao.stop();

			lTenDao = new TenoreDAO(lConn);

			for (int i = 0; i < aModel.getTenori().length; i++) {
				// Imposta l'id del deposito ordinanza appena inserito
				// nel tenore.
				aModel.getTenori()[i].setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);
				// lTenDao.setDAOFromModelForUpdate(aModel.getTenori()[i]);
				lTenDao.setDAOFromModelForUpdateDepOrd(aModel.getTenori()[i]);
				lTenDao.update();
				lTenDao.stop();
			}

			// Esegue la ricerca dei tenori sortati per peso esito tenore.
			lTenSqlDao = new TenoreSqlDAO(lConn);
			// Ricerca tenori x Id Deposito Ordinanza Luigi 5-12-2003

			lTenSqlDao.ricercaTenoriByOrdinanzaOrderByPeso(lKeyDepOrd);

			// lTenSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(
			// aModel.getOrdinanza().getGenPridGeneraleProcedimento());
			Vector lTenori = new Vector(lTenSqlDao.getModels());
			if (lTenori.size() == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Tenori assenti");

			TenoreModel[] lTenoriModel = (TenoreModel[]) lTenori.toArray(new TenoreModel[0]);

			if (aModel.getEvento() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Evento null in aModel");

			// Se il campo template dell'evento e' null( Da implementare quando e' pronto )
			// Effettua la ricerca dei tenori sortati per il peso.
			// Vuol dire che si decide per la generazione automantica del template.

			if (aModel.getEvento().getTemIdTemplate() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Chiama la logica Decisionale per il template  ...");
				// Chiama la logica Decisionale per il template da associare.
				GestioneFlussoOrdinanza lGFO = new GestioneFlussoOrdinanza(lTenoriModel);
				lGFO.start();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Template Associato : " + lGFO.getTemplate());

				// Inserisce qui nell'evento l'id del template, prima dell'inserimento.
				aModel.getEvento().setTemIdTemplate(lGFO.getTemplate());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di inserimento per l'Evento");
			// Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller

			lEveDao = new EventoDAO(lConn);
			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(aModel.getEvento());
			aModel.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
			// Inposta il primo cod esito tenore significativo.
			aModel.getEvento().setCodMotivo(lTenoriModel[0].getCodOggettoTenore());
			aModel.getEvento().setCodEsito(lTenoriModel[0].getCodEsitoTenore());
			lEveDao.setDAOFromModel(aModel.getEvento());
			BigDecimal lKeyEvento = lEveDao.insert();
			aModel.getEvento().setIdEvento(lKeyEvento); // Imposta l'id Evento nel model.

			// Setto il Campo Id_Evento_Generato ed aggiorno DepositoOrdinanzaPC.
			lDepDao.setIdEventoGenerato(lKeyEvento);
			lDepDao.setCondizioneUpdate(lKeyDepOrd);
			lDepDao.update();

			commit(lConn);

			// Riempie il model di ritorno
			lDepMod.setIdDepositoOrdinanzaPc(lKeyDepOrd);
			lModel.setEvento(aModel.getEvento());
			lModel.getEvento().setIdEvento(lKeyEvento);
			lModel.setOrdinanza(lDepMod);
			lModel.setTenori(lTenoriModel);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciEventoDepositoOrdinanzaPc: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciEventoDepositoOrdinanzaPc: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lSqlDAO);
			cleanup(lEveDao);
			cleanup(lTenDao);
			cleanup(lTenSqlDao);
			cleanup(lConn);
		}
		return lModel;
	}

	/**
	 * Metodo che preleva il deposito ordinanza + tenori per id GenProc.
	 *
	 * @param aKey
	 *            generale procedimento id
	 * @return model OrdinanzaEventoTenoriModel
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	/*
	 * public OrdinanzaEventoTenoriModel ExRicercaDepositoOrdinanzaPcTenoriByGenProc(BigDecimal aKey) throws
	 * F3BException { // Preleva il DepositoOrdinaza per id generale procedimento OrdinanzaEventoTenoriModel
	 * lOrdEveTenMod = new OrdinanzaEventoTenoriModel();
	 * lOrdEveTenMod.setOrdinanza(ExRicercaDepositoOrdinanzaPcByGenProc( aKey ) );
	 *
	 * // Preleva i tenori per id Procedimento. ITenore lTenoreCtrl = SIUSLookupRemote.getTenoreRemote();
	 * Vector lTenori = lTenoreCtrl.ExRicercaTenoreByGenProcOrderByPeso( aKey );
	 *
	 * if( lTenori != null ) lOrdEveTenMod.setTenori( (TenoreModel[])lTenori.toArray(new TenoreModel[0]));
	 *
	 * return lOrdEveTenMod;
	 *
	 * }
	 */

	public DepositoOrdinanzaPcModel ExInserisciDepositoOrdinanzaPc(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod = null;

		try {
			lConn = getDBConnection();
			lDepMod = new DepositoOrdinanzaPcModel(aDepositoOrdinanzaPc);
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);
			lDepDao.setDAOFromModel(aDepositoOrdinanzaPc);
			BigDecimal lKey = null;
			lKey = lDepDao.insert();
			commit(lConn);
			lDepMod.setIdDepositoOrdinanzaPc(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("DepositoOrdinanzaPcController.ExInserisciDepositoOrdinanzaPc: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Esegue l'inserimento del Emissione Ordinanza. Description: Funzione per l'inserimento dell'Emissione di
	 * un'ordinanza generico. Le tabelle coinvolte sono: DEPOSITO_ORDINANZAPC : viene inserito il nuovo record
	 * decreto; EVENTO : viene inserito un nuovo record; TENORE : vengono chiusi i tenori attivi (data_fine)
	 * ed inseriti i nuovi tenori; GENERALE_PROCEDIMENTO : update del contenuto del procemimento.
	 *
	 * @param OrdinanzaEventoTenoriGProcModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanza(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori) throws F3BException {

		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lModRet = null;

		Connection lConn = null;
		/*
		 * Il controllo non c'e' piu' ! Luigi 27-5-2004 if(
		 * ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(aGProcOrdEveTenori
		 * .getGeneraleProcedimento().getIdGeneraleProcedimento(),
		 * aGProcOrdEveTenori.getEvento().getDataEmissione())) { throw new
		 * SIUSException(SIUSException.USER_MESSAGE,
		 * "Operazione non consentita. Per il procedimento indicato e' gia' stata emessa una ordinanza nella stessa data."
		 * ); }
		 */
		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			daoEx.printStackTrace();
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanza: Non posso leggere : " + daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlEx);
			sqlEx.printStackTrace();
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanza: Non posso leggere  : " + sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			e.printStackTrace();
			throw new SIUSException("DepositoOrdinanzaPcController.ExInserisciOrdinanza:" + e);
		} finally {
			cleanup(lConn);
		}

		return lModRet;
	}

	/**
	 * Esegue l'inserimento del Emissione Ordinanza e l'aggiornamento del Fascicolo SIUS origine collegato al
	 * Fascicolo SIUS. Description: Funzione per l'inserimento dell'Emissione di un'ordinanza generico. Le
	 * tabelle coinvolte sono: DEPOSITO_ORDINANZAPC : viene inserito il nuovo record decreto; EVENTO : viene
	 * inserito un nuovo record; TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi
	 * tenori; GENERALE_PROCEDIMENTO : update del contenuto del procemimento. FASCICOLO_SIUS : update del ID
	 * FASCICOLO ORIGINE.
	 *
	 * @param OrdinanzaEventoTenoriGProcModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanza(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, BigDecimal IdFascicoloOrigine)
			throws F3BException {

		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lModRet = null;
		FascicoloSiusDAO lFasSiusDao = null;

		Connection lConn = null;

		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			// Aggiorno il fascicolo
			lFasSiusDao = new FascicoloSiusDAO(lConn);
			lFasSiusDao.setCondizioneUpdate(aGProcOrdEveTenori.getEvento().getFasSiuIdFascicoloSius());
			lFasSiusDao.setIdFascicoloSiusOrigine(IdFascicoloOrigine);
			lFasSiusDao.update();
			lFasSiusDao.stop();
			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			e.printStackTrace();
			throw new SIUSException("DepositoOrdinanzaPcController.ExInserisciOrdinanza:" + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasSiusDao);
			cleanup(lConn);
		}

		return lModRet;
	}

	/**
	 * Esegue l'inserimento del Ordinanza di Liberazione Anticipata.
	 */
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaLibAnt(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, LicenzaPeriodiLibAnticipataModel[] aLicenze)
			throws F3BException {

		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lModRet = null;

		Connection lConn = null;
		// Controllo eliminato. Luigi 18-5-2004
		/*
		 * if( ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(aGProcOrdEveTenori.getGeneraleProcedimento().
		 * getIdGeneraleProcedimento(), aGProcOrdEveTenori.getEvento().getDataEmissione())) { throw new
		 * SIUSException(SIUSException.USER_MESSAGE,
		 * "Operazione non consentita. Per il procedimento indicato e' gia' stata emessa una ordinanza nella stessa data."
		 * ); }
		 */
		// E' possibbile inserire un'ordinanza senza licenze: Luigi 19-7-2005
		/*
		 * if (aLicenze == null) throw new
		 * SIUSException(SIUSException.USER_MESSAGE,"Errore nei dati: Nessuna Licenza da Inserire");
		 */
		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			if (aLicenze != null) {
				// Valorizzazione della Foreign Key : IdEvento.
				for (int i = 0; i < aLicenze.length; i++) {

					aLicenze[i].getLicenza().setEveIdEvento(lModRet.getEvento().getIdEvento());
					// Luigi 30-6-2005
					// Si aggiungono altri dati perche' servono alla Procura
					aLicenze[i].getLicenza()
							.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
					aLicenze[i].getLicenza()
							.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
					aLicenze[i].getLicenza().setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
					aLicenze[i].getLicenza()
							.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze, lConn);
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaLibAnt: Non posso leggere : " + daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaLibAnt: Non posso leggere  : "
							+ sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException("DepositoOrdinanzaPcController.ExInserisciOrdinanzaLibAnt:" + e);
		} finally {
			cleanup(lConn);
		}

		return lModRet;
	}

	// 10-03-2014 Nuova Ordinanza L.A. - Decreto legge 146/2013
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaLibAnt(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, LicenzaPeriodiLibAnticipataModel[] aLicenze,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_spe, LicenzaPeriodiLibAnticipataModel[] aLicenze_int,
			LicenzaLibAnticipataModel aLicenzaC, LicenzaLibAnticipataModel aLicenzaC_SPE,
			LicenzaLibAnticipataModel aLicenzaC_INT) throws F3BException {

		OrdinanzaEventoTenoriGProcModel lModRet = null;
		Connection lConn = null;

		try {
			lConn = getDBTransaction();

			// Inserisce Evento e DepositoOrdinanzaPC (principalmente)
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			// Inserisce LIBERAZIONE_ANTICIPATA E relativi PERIODI
			if (aLicenze != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("-----------------> CLOD - DepositoOrdinanzaController - aLicenze != null"
						+ aLicenze);
				for (int i = 0; i < aLicenze.length; i++) {
					aLicenze[i].getLicenza().setEveIdEvento(lModRet.getEvento().getIdEvento());
					aLicenze[i].getLicenza()
							.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
					aLicenze[i].getLicenza()
							.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
					aLicenze[i].getLicenza().setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
					aLicenze[i].getLicenza()
							.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze, lConn);
			}

			// Inserisce LIBERAZIONE_ANTICIPATA SPECIALE E relativi PERIODI
			if (aLicenze_spe != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("-----------------> CLOD - DepositoOrdinanzaController - aLicenze_spe != null"
								+ aLicenze_spe);
				for (int i = 0; i < aLicenze_spe.length; i++) {
					aLicenze_spe[i].getLicenza().setEveIdEvento(lModRet.getEvento().getIdEvento());
					aLicenze_spe[i].getLicenza()
							.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
					aLicenze_spe[i].getLicenza()
							.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
					aLicenze_spe[i].getLicenza()
							.setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
					aLicenze_spe[i].getLicenza()
							.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze_spe, lConn);
			}

			// Inserisce LIBERAZIONE_ANTICIPATA INTEGRAZIONE E relativi PERIODI
			if (aLicenze_int != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("-----------------> CLOD - DepositoOrdinanzaController - aLicenze_int != null"
								+ aLicenze_int);
				for (int i = 0; i < aLicenze_int.length; i++) {
					aLicenze_int[i].getLicenza().setEveIdEvento(lModRet.getEvento().getIdEvento());
					aLicenze_int[i].getLicenza()
							.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
					aLicenze_int[i].getLicenza()
							.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
					aLicenze_int[i].getLicenza()
							.setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
					aLicenze_int[i].getLicenza()
							.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());
				}

				ILicenzaPeriodiLibAnticipata LicenzaPeriodiLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaPeriodiLibAntCtrl.ExInserisciLicenzeLibanticipata(aLicenze_int, lConn);
			}

			// Inserisce LIBERAZIONE_ANTICIPATA (Periodo Unico)
			if (aLicenzaC != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"-----------------> CLOD - DepositoOrdinanzaController - aLicenzaC != null no semestri"
								+ aLicenzaC);
				aLicenzaC.setEveIdEvento(lModRet.getEvento().getIdEvento());
				aLicenzaC.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
				aLicenzaC.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
				aLicenzaC.setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
				aLicenzaC.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());

				ILicenzaPeriodiLibAnticipata LicenzaLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaLibAntCtrl.ExInserisciLicenzaLibanticipata(aLicenzaC, lConn);
			}

			// Inserisce LIBERAZIONE_ANTICIPATA SPECIALE (Periodo Unico)
			if (aLicenzaC_SPE != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"-----------------> CLOD - DepositoOrdinanzaController - aLicenzaC_SPE != null no semestri"
								+ aLicenzaC_SPE);
				aLicenzaC_SPE.setEveIdEvento(lModRet.getEvento().getIdEvento());
				aLicenzaC_SPE.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
				aLicenzaC_SPE.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
				aLicenzaC_SPE.setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
				aLicenzaC_SPE.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());

				ILicenzaPeriodiLibAnticipata LicenzaLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaLibAntCtrl.ExInserisciLicenzaLibanticipata(aLicenzaC_SPE, lConn);
			}

			// Inserisce LIBERAZIONE_ANTICIPATA INTEGRAZIONE (Periodo Unico)
			if (aLicenzaC_INT != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"-----------------> CLOD - DepositoOrdinanzaController - aLicenzaC_INT != null no semestri"
								+ aLicenzaC_INT);
				aLicenzaC_INT.setEveIdEvento(lModRet.getEvento().getIdEvento());
				aLicenzaC_INT.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
				aLicenzaC_INT.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
				aLicenzaC_INT.setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
				aLicenzaC_INT.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());

				ILicenzaPeriodiLibAnticipata LicenzaLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaLibAntCtrl.ExInserisciLicenzaLibanticipata(aLicenzaC_INT, lConn);
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.Riscritto.ExInserisciOrdinanzaLibAnt: Non posso leggere : "
							+ daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.Riscritto.ExInserisciOrdinanzaLibAnt: Non posso leggere  : "
							+ sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException("DepositoOrdinanzaPcController.ExInserisciOrdinanzaLibAnt:" + e);
		} finally {
			cleanup(lConn);
		}

		return lModRet;
	}

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanza(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, Connection aConn) throws Exception {

		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lGProcOrdEveTenori = new OrdinanzaEventoTenoriGProcModel(
				aGProcOrdEveTenori);

		GeneraleProcedimentoDAO lGenProcDao = null;
		TenoreDAO lTenoreDao = null;
		TenoreSqlDAO lTenoreSqlDao = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		EventoDAO lEventoDao = null;
		EventoSqlDAO lSqlDAO = null;

		try {
			lGenProcDao = new GeneraleProcedimentoDAO(aConn);
			lTenoreDao = new TenoreDAO(aConn);
			lTenoreSqlDao = new TenoreSqlDAO(aConn);
			lDepOrdDao = new DepositoOrdinanzaPcDAO(aConn);
			lEventoDao = new EventoDAO(aConn);

			// Update GeneraleProcedimento.
			lGenProcDao.setCodOggettoProcedimento(
					lGProcOrdEveTenori.getGeneraleProcedimento().getCodOggettoProcedimento());
			lGenProcDao.setDataAggiornamento(
					lGProcOrdEveTenori.getGeneraleProcedimento().getDataAggiornamento());
			lGenProcDao.setCodUfficioAggiornamento(
					lGProcOrdEveTenori.getGeneraleProcedimento().getCodUfficioAggiornamento());
			lGenProcDao.setCodOperatoreAggiornamento(
					lGProcOrdEveTenori.getGeneraleProcedimento().getCodOperatoreAggiornamento());
			lGenProcDao.setCondizioneUpdate(
					lGProcOrdEveTenori.getGeneraleProcedimento().getIdGeneraleProcedimento());
			lGenProcDao.update();

			// Insert DepositoOrdinanza.
			lDepOrdDao.setDAOFromModel(lGProcOrdEveTenori.getOrdinanza());
			BigDecimal lIdDepOrd = lDepOrdDao.insert();
			lDepOrdDao.stop();
			lGProcOrdEveTenori.getOrdinanza().setIdDepositoOrdinanzaPc(lIdDepOrd);

			// -- Parte Gestione Tenori --//
			BigDecimal lIdGenProc = aGProcOrdEveTenori.getGeneraleProcedimento().getIdGeneraleProcedimento();

			/*
			 * ISSUE MEV : aggiunta gestione per ORDINANZA di conferma decisione MAGISTRATO RELATORE 
			 * Numero MEV : 9 
			 * Autore : sgioggi 
			 * Data : 17 gen 2023 
			 * Branch : MEV_2019-09
			 */
			if (!"CM".equals(lGProcOrdEveTenori.getOrdinanza().getCodTipoOrdinanza())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fase di chiusura per il Tenore");
				TenoreModel lTenore = new TenoreModel();
				// Valorizzazione dei campi da aggiornare + update
				lTenore.setCodOperatoreAggiornamento(
						lGProcOrdEveTenori.getGeneraleProcedimento().getCodOperatoreAggiornamento());
				lTenore.setCodUfficioAggiornamento(
						lGProcOrdEveTenori.getGeneraleProcedimento().getCodUfficioAggiornamento());
				lTenore.setDataAggiornamento(
						lGProcOrdEveTenori.getGeneraleProcedimento().getDataAggiornamento());
				lTenore.setDataFine(lGProcOrdEveTenori.getGeneraleProcedimento().getDataAggiornamento());
				lTenore.setGenPridGeneraleProcedimento(lIdGenProc);
				lTenoreDao.setDAOFromModelForUpdateDataFine(lTenore);
				lTenoreDao.update();
				lTenoreDao.stop();
			}
			// ***** FINE INTERVENTO MEV_2019-09 *****//

			// Insert dei tenori.
			TenoreModel[] lTenori = lGProcOrdEveTenori.getTenori();
			int lCount = lTenori.length;
			for (int x = 0; x < lCount; x++) {
				lTenori[x].setGenPridGeneraleProcedimento(lIdGenProc);
				lTenori[x].setDepOpidDepositoOrdinanzaPc(lIdDepOrd);
				lTenori[x].setData(lGProcOrdEveTenori.getEvento().getDataEmissione());
				lTenoreDao.setDAOFromModel(lTenori[x]);
				lTenori[x].setIdTenore(lTenoreDao.insert());
				lTenoreDao.stop();
			}
			lGProcOrdEveTenori.setTenori(lTenori);

			// Select dati dal tenore + significativo.
			lTenoreSqlDao.ricercaTenoriByOrdinanzaOrderByPeso(lIdDepOrd);
			TenoreModel lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();
			if (lTenoreMod == null)
				throw new SIUSException(
						"DepositoOrdinanzaPcController.ExInserisciOrdinanza: tenori assenti ");

			EventoModel lEventoModel = new EventoModel(lGProcOrdEveTenori.getEvento());

			// Insert Evento
			// Imposta COD_MOTIVO e IdTenore, nel model.
			lEventoModel.setCodMotivo(lTenoreMod.getCodOggettoTenore());
			lEventoModel.setTenIdTenore(lTenoreMod.getIdTenore());
			if (lEventoModel.getCodEsito() != null && lEventoModel.getCodEsito().trim().length() > 0)
				lEventoModel.setCodEsito(lEventoModel.getCodEsito());
			else
				lEventoModel.setCodEsito(lTenoreMod.getCodEsitoTenore());

			// Setto l'anno e il progressivo...
			lSqlDAO = new EventoSqlDAO(aConn);
			BigDecimal lProgr = lSqlDAO.getProgressivo(lEventoModel);
			lEventoModel.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));
			// 06/04/2011 Modifica x Visibilita' Stato di Esecuzione.
			lEventoModel.setFasSieIdFascicoloSiep(null);

			lEventoDao.setDAOFromModel(lEventoModel);
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEventoModel.setIdEvento(lIdEvento);
			lGProcOrdEveTenori.setEvento(lEventoModel);

			// Nel caso di Revoca si effettua l'update dell'Evento Revocato
			if (lEventoModel.getEveIdEvento() != null) {
				lEventoDao.setDataAggiornamento(lEventoModel.getDataInserimento());
				lEventoDao.setCodUfficioAggiornamento(lEventoModel.getCodUfficioInserimento());
				lEventoDao.setCodOperatoreAggiornamento(lEventoModel.getCodOperatoreInserimento());
				lEventoDao.setEveIdEventoRevoca(lEventoModel.getIdEvento());
				lEventoDao.selCondizioneUpdate(lEventoModel.getEveIdEvento());
				lEventoDao.update();
				lEventoDao.stop();
			}

			// Effettua update del campo evento_generato
			lDepOrdDao.setCondizioneUpdate(lIdDepOrd);
			lDepOrdDao.setIdEventoGenerato(lIdEvento);
			lDepOrdDao.update();
			lGProcOrdEveTenori.getOrdinanza().setIdEventoGenerato(lIdEvento);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException("DepositoOrdinanzaPcController.ExInserisciOrdinanza : " + e);
		} finally {
			cleanup(lGenProcDao);
			cleanup(lTenoreDao);
			cleanup(lTenoreSqlDao);
			cleanup(lDepOrdDao);
			cleanup(lEventoDao);
			cleanup(lSqlDAO);
		}

		return lGProcOrdEveTenori;
	}

	public Vector ExRicercaDepositoOrdinanzaPc(DepositoOrdinanzaPcModel aDepositoOrdinanzaPc)
			throws F3BException {

		Connection lConn = null;
		Vector lDepositoOrdinanzaPi = new Vector();
		DepositoOrdinanzaPcSqlDAO lDepDao = null;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaDepositoOrdinanzaPc(aDepositoOrdinanzaPc);
			lDepositoOrdinanzaPi = new Vector(lDepDao.getModels());
			if (lDepositoOrdinanzaPi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepositoOrdinanzaPi;
	}

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaDepositoOrdinanzaPcByKey(aKey);
			lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	public DepositoOrdinanzaPcModel ExRicercaOrdinanzaRimessioneAttiPcByKeyPerUpdate(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaOrdinanzaRimessioneAttiByKeyPerUpdate(aKey);
			lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	public DepositoOrdinanzaPcModel ExModificaDepositoOrdinanzaPc(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel(aDepositoOrdinanzaPc);

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);
			lDepDao.setDAOFromModelForUpdate(aDepositoOrdinanzaPc);
			lDepDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("DepositoOrdinanzaPcController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}
	
	/**
	 * Aggiorna il Deposito e anche il record MA per consentire a SIEP di vedere l'ordinanza
	 * MEV_2019-09 02.2024
	 */
	public DepositoOrdinanzaPcModel ExAggiornaDataEsecutivitaDepositoOrdinanzaPc(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel(aDepositoOrdinanzaPc);

		MisuraAlternativaDAO lMisAltDao = null;
		
		try {
			lConn = getDBConnection();
			
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);
			lDepDao.setDAOFromModelForUpdate(aDepositoOrdinanzaPc);
			lDepDao.update();

			lMisAltDao = new MisuraAlternativaDAO(lConn);
			if (aDepositoOrdinanzaPc.getIdEventoGenerato()!=null) {
				lMisAltDao.setDataEsecutivita(aDepositoOrdinanzaPc.getDataEsecutivita());
				
				lMisAltDao.setCondizioneByIdEvento(aDepositoOrdinanzaPc.getIdEventoGenerato());
				
				lMisAltDao.update();
			}
			
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("DepositoOrdinanzaPcController.ExAggiornaDataEsecutivitaDepositoOrdinanzaPc: Non posso inserire: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lMisAltDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	public void ExCancellaDepositoOrdinanza(DepositoOrdinanzaPcModel aDepOrd) throws F3BException {

		Connection lConn = null;

		try {
			lConn = getDBConnection();
			ExCancellaDepositoOrdinanza(aDepOrd, lConn);
			commit(lConn);
		} catch (F3BException FEx) {
			rollback(lConn);
			throw FEx;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaPcController.ExCancellaDepositoOrdinanza: " + e);
		} finally {
			cleanup(lConn);
		}
	}

	public void ExCancellaDepositoOrdinanza(DepositoOrdinanzaPcModel aDepOrd, Connection aConn)
			throws F3BException {

		DepositoOrdinanzaPcDAO lDepDao = null;
		PrescrizioneDAO lPreDao = null;
		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		PeriodoAltraSanzioneDAO lPASDao = null;
		PeriodoAltraSanzioneSqlDAO lPASSqlDao = null;
		PeriodoAltraMisuraDAO lPAMDao = null;
		PeriodoAltraMisuraSqlDAO lPAMSqlDao = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lESSSqlDao = null;
		EsecuzioneSanzioneSostitutivaDAO lESSDao = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEMSSqlDao = null;
		EsecuzioneMisuraSicurezzaDAO lESMDao = null;
		RichiestaConversioneDAO lRCDao = null;
		MisuraSicurezzaDAO lMisSicDao = null;
		MisuraAlternativaDAO lMisAltDao = null;

		BigDecimal lIdDepOrd = aDepOrd.getIdDepositoOrdinanzaPc();
		try {
			// cancellazione Prescrizioni collegate
			lPreDao = new PrescrizioneDAO(aConn);
			/*
			 * Le prescrizioni sono collegate all'evento e non piu' al deposito ordinanza. Luigi 12-12-2003
			 */
			lPreDao.setCondizioneByDepOrdPC(aDepOrd.getIdEventoGenerato());
			lPreDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellate prescrizioni collegate a DepOrdinanzaPC " + lIdDepOrd);

			// TENORI COLLEGATI
			lTenDao = new TenoreDAO(aConn);
			lTenDao.setDAOForDeleteDepOrd(aDepOrd);
			/*
			 * ISSUE MEV : cancello tenore se cancello ordinanza di conferma decisione magistrato relatore
			 * Numero MEV : 9 
			 * Autore : sgioggi 
			 * Data : 18 gen 2023 
			 * Branch : MEV_2019-09
			 */
			if (!"CM".equals(aDepOrd.getCodTipoOrdinanza())) {
				// update Tenori collegati
				lTenDao.update();
			} else {
				// delete Tenori collegati
				lTenDao.delete();
			}
			// ***** FINE INTERVENTO MEV_2019-09 *****//

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Aggiornati tenori collegati a DepOrdinanzaPC " + lIdDepOrd);

			// cancellazione Deposito Ordinanza
			lDepDao = new DepositoOrdinanzaPcDAO(aConn);
			lDepDao.setCondizioneUpdate(lIdDepOrd);
			lDepDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato DepOrdinanzaPC " + lIdDepOrd);

			// --------------------------------------------------------------
			// Modifica l'Esecuzione Sanzione Sostituiva se necessario
			// e cancella il Periodo Altra Sanzione eventualmente collegato
			// --------------------------------------------------------------

			// ---- Ricerca in Periodo Altra Sanzione ----
			lPASSqlDao = new PeriodoAltraSanzioneSqlDAO(aConn);
			lPASSqlDao.ricercaSanzioneSostitutivaByIdEvento(aDepOrd.getIdEventoGenerato());
			PeriodoAltraSanzioneModel lPASMod = (PeriodoAltraSanzioneModel) lPASSqlDao.getModelByKey();

			if (lPASMod != null && lPASMod.getFasSiuIdFascicoloSius() != null
					&& aDepOrd.getFlagRecuperoSS().equals("S")) {
				// ---- Ricerca in Esecuzione Sanzione Sostitutiva con l'ID del Fascicolo SIUS (PADRE) trovato
				// ----
				lESSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(aConn);
				lESSSqlDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
						lPASMod.getFasSiuIdFascicoloSius());
				EsecuzioneSanzioneSostitutivaModel lESSMod = (EsecuzioneSanzioneSostitutivaModel) lESSSqlDao
						.getModelByKey();

				if (lESSMod != null && lESSMod.getDataTermineAttuale() != null) {
					// Sottrae gli eventuali giorni a DATA_TERMINE_ATTUALE di Esecuzione Sanzione Sostitutiva
					Date lDataTermineAttuale = DateUtils.moveDateTo(lESSMod.getDataTermineAttuale(),
							Calendar.DAY_OF_MONTH, -(aDepOrd.getGiorniRecuperoSS().intValue()));
					lESSDao = new EsecuzioneSanzioneSostitutivaDAO(aConn);

					lESSDao.setDataTermineAttuale(lDataTermineAttuale);

					lESSDao.setCodUfficioAggiornamento(aDepOrd.getCodUfficioAggiornamento());
					lESSDao.setCodOperatoreAggiornamento(aDepOrd.getCodOperatoreAggiornamento());
					lESSDao.setDataAggiornamento(aDepOrd.getDataAggiornamento());

					lESSDao.setCondizioneUpdate(lESSMod.getIdEsecuzioneSanzioneSost());
					lESSDao.update();
					lESSDao.stop();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Modificata Esecuzione Sanzione Sostitutiva collegata a DepOrdinanzaPC"
									+ lIdDepOrd);
				}
			}

			// cancellazione eventuale Periodo Altra Sanzione collegata
			lPASDao = new PeriodoAltraSanzioneDAO(aConn);
			// Il Periodo Altra Sanzione e' collegato all'evento
			lPASDao.setCondizioneByEveIdEvento(aDepOrd.getIdEventoGenerato());
			lPASDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellata Periodo Altra Sanzione collegata a DepOrdinanzaPC" + lIdDepOrd);

			// 06-03-2009 Modifica di eventuali Richieste Conversioni Pene Pecuniarie (Con Azzeramento dati di
			// Ordinanza).
			// previa lettura dell'Evento (prima di cancellarlo) per puntare all'IdFascicoloSius.
			if (aDepOrd.getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE) == 0) {
				lEveDao = new EventoDAO(aConn);
				lEveDao.setIdEvento(aDepOrd.getIdEventoGenerato());
				lEveDao.selByKey();
				lEveDao.start();
				EventoModel lEventoSave = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();
				lRCDao = new RichiestaConversioneDAO(aConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setFasSiuIdFascicoloSius(lEventoSave.getFasSiuIdFascicoloSius());
				lRCModel.setCodOperatoreAggiornamento(aDepOrd.getCodOperatoreAggiornamento());
				lRCModel.setCodUfficioAggiornamento(aDepOrd.getCodUfficioAggiornamento());
				lRCModel.setDataAggiornamento(aDepOrd.getDataAggiornamento());
				// 14/08/2015 lRCModel.setEveIdEvento(null);
				lRCDao.setDAOFromModelForCancOrdinanzaCPP(lRCModel);
				lRCDao.selCondizioneByIdFasSius(lEventoSave.getFasSiuIdFascicoloSius());
				lRCDao.update();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Aggiornate le Richieste Conv. Pene Pec. collegata a DepOrdinanzaPC"
						+ lIdDepOrd);
			}

			// Per le Ordinanze di Applicazione Misure Sicurezza, se l'Ordinanza stessa ha trasformato la
			// misura
			// occorre cancellare la misura generata dall' Ordinanza

			// 13/02/2015 La Misura di Sicurezza va cancellata se CodTipoOrdinanza rientra in uno specifico
			// gruppo di valori.
			// if (aDepOrd.getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA)==0)
			// MERGE v10 COLLAUDO: aggiunto caso "GE"
			// MEV_39: aggiunto codice AP - appello contro provvedimento su MS
			List<String> codiciDaCancellare = Arrays.asList("MS", "40", "TM", "GE", "AP");

			if (codiciDaCancellare.contains(aDepOrd.getCodTipoOrdinanza())) {
				lMisSicDao = new MisuraSicurezzaDAO(aConn);
				lMisSicDao.setCondizioneByDepOrdPC(aDepOrd.getIdEventoGenerato());
				lMisSicDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Cancellate misure sicurezza collegate a DepOrdinanzaPC " + lIdDepOrd);
			}

			lMisAltDao = new MisuraAlternativaDAO(aConn);
			lMisAltDao.setCondizioneByIdEvento(aDepOrd.getIdEventoGenerato());
			lMisAltDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellate misure alternative collegate a DepOrdinanzaPC " + lIdDepOrd);

			// Per le Ordinanze di Esecuzione Misure Sicurezza, se l'Ordinanza stessa ha trasformato la misura
			// occorre cancellare la misura generata dall' Ordinanza
			if (aDepOrd.getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
					|| aDepOrd.getCodTipoOrdinanza()
							.compareTo(ICostantiDepositoOrdinanzaPc.ORD_INOSSERVANZA_OBBLIGHI_MS) == 0) {
				lESMDao = new EsecuzioneMisuraSicurezzaDAO(aConn);
				lESMDao.setCondizioneDeleteByIdOrdinanza(lIdDepOrd);
				lESMDao.delete();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						">>>> Cancellate eventuali esecuzioni misure sicurezza collegate a DepOrdinanzaPC "
								+ lIdDepOrd);
			}

			// --------------------------------------------------------------
			// Modifica l'Esecuzione Misura Sicurezza se necessario
			// e cancella il Periodo Altra Misura eventualmente collegato
			// --------------------------------------------------------------

			// ---- Ricerca in Periodo Altra Misura ----
			lPAMSqlDao = new PeriodoAltraMisuraSqlDAO(aConn);
			lPAMSqlDao.ricercaMisuraSicurezzaByIdEvento(aDepOrd.getIdEventoGenerato());
			PeriodoAltraMisuraModel lPAMMod = (PeriodoAltraMisuraModel) lPAMSqlDao.getModelByKey();

			if (lPAMMod != null && lPAMMod.getFasSiuIdFascicoloSius() != null
			// Al momento il Flag Recupero
					&& aDepOrd.getFlagRecuperoSS() != null && aDepOrd.getFlagRecuperoSS().equals("S")) {
				// non e' utilizzato per:
				// ---- Ricerca in Esecuzione Misura Sicurezza con l'ID del Fascicolo SIUS (PADRE) trovato
				// ---- // le Misure di Sicurezza
				lEMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(aConn); // (24/5/2011)
				lEMSSqlDao.ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(lPAMMod.getFasSiuIdFascicoloSius());
				EsecuzioneMisuraSicurezzaModel lEMSMod = (EsecuzioneMisuraSicurezzaModel) lEMSSqlDao
						.getModelByKey();

				if (lEMSMod != null && lEMSMod.getDataTermineAttuale() != null) {
					// Sottrae gli eventuali giorni a DATA_TERMINE_ATTUALE di Esecuzione Misura Sicurezza
					Date lDataTermineAttuale = DateUtils.moveDateTo(lEMSMod.getDataTermineAttuale(),
							Calendar.DAY_OF_MONTH, -(aDepOrd.getGiorniRecuperoSS().intValue()));
					lESMDao = new EsecuzioneMisuraSicurezzaDAO(aConn);

					lESMDao.setDataTermineAttuale(lDataTermineAttuale);

					lESMDao.setCodUfficioAggiornamento(aDepOrd.getCodUfficioAggiornamento());
					lESMDao.setCodOperatoreAggiornamento(aDepOrd.getCodOperatoreAggiornamento());
					lESMDao.setDataAggiornamento(aDepOrd.getDataAggiornamento());

					lESMDao.setCondizioneUpdate(lEMSMod.getIdEsecuzioneMisuraSicurezza());
					lESMDao.update();
					lESMDao.stop();
				}
			}

			// cancellazione eventuale Periodo Altra Misura collegata
			lPAMDao = new PeriodoAltraMisuraDAO(aConn);
			// Il Periodo Altra Misura e' collegato all'evento
			lPAMDao.setCondizioneByEveIdEvento(aDepOrd.getIdEventoGenerato());
			lPAMDao.delete();

			// Istanzia EventoDAO
			lEveDao = new EventoDAO(aConn);

			// Cancellazione dei riferimenti in tabella Evento al record da cancellato
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aDepOrd.getCodOperatoreAggiornamento());
			lEvento.setCodUfficioAggiornamento(aDepOrd.getCodUfficioAggiornamento());
			lEvento.setDataAggiornamento(aDepOrd.getDataAggiornamento());
			lEvento.setIdEvento(aDepOrd.getIdEventoGenerato());

			// Cancellazione ai riferimenti tramite EVE_ID_EVENTO E EVE_ID_EVENTO_REVOCA
			lEveDao.updateDAOFromModelForResetRifEve(lEvento);

			// cancellazione Evento collegato
			lEveDao.selCondizioneUpdate(aDepOrd.getIdEventoGenerato());
			lEveDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato evento collegato a DepOrdinanzaPC " + lIdDepOrd);
		} catch (DAOException daoEx) {
			throw new F3BException("DepositoOrdinanzaPcController.ExCancellaDepositoOrdinanza: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("DepositoOrdinanzaPcController.ExCancellaDepositoOrdinanza: " + e);
		} finally {
			cleanup(lDepDao);
			cleanup(lPreDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lPASDao);
			cleanup(lPASSqlDao);
			cleanup(lPAMDao);
			cleanup(lPAMSqlDao);
			cleanup(lESSSqlDao);
			cleanup(lESSDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEMSSqlDao);
			cleanup(lESMDao);
			cleanup(lRCDao);
			cleanup(lMisSicDao);
			cleanup(lMisAltDao);
		}
	}

	public void ExCancellaRimessioneAtti(DepositoOrdinanzaPcModel aDepOrd) throws F3BException {

		Connection lConn = null;

		try {
			lConn = getDBConnection();
			ExCancellaRimessioneAtti(aDepOrd, lConn);
			commit(lConn);
		} catch (F3BException FEx) {
			rollback(lConn);
			throw FEx;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaPcController.ExCancellaDepositoOrdinanza: " + e);
		} finally {
			cleanup(lConn);
		}
	}

	public void ExCancellaRimessioneAtti(DepositoOrdinanzaPcModel aDepOrd, Connection aConn)
			throws F3BException {

		DepositoOrdinanzaPcDAO lDepDao = null;
		TenoreDAO lTenDao = null;
		NotificaDAO lNotDao = null;
		EventoDAO lEveDao = null;

		BigDecimal lIdDepOrd = aDepOrd.getIdDepositoOrdinanzaPc();
		BigDecimal lIdEvento = aDepOrd.getIdEventoGenerato();
		try {

			// update Tenori collegati
			lTenDao = new TenoreDAO(aConn);
			lTenDao.setDAOForDeleteDepOrd(aDepOrd);
			lTenDao.update();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Aggiornati tenori collegati a DepOrdinanzaPC " + lIdDepOrd);

			// cancellazione Deposito Ordinanza
			lDepDao = new DepositoOrdinanzaPcDAO(aConn);
			lDepDao.setCondizioneUpdate(lIdDepOrd);
			lDepDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato DepOrdinanzaPC " + lIdDepOrd);

			// Cancellazione Notifiche
			lNotDao = new NotificaDAO(aConn);
			lNotDao.setCondizioneEvento(lIdEvento);
			lNotDao.delete();
			lNotDao.stop();

			// Istanzia EventoDAO
			lEveDao = new EventoDAO(aConn);

			// Cancellazione dei riferimenti in tabella Evento al record da cancellato
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aDepOrd.getCodOperatoreAggiornamento());
			lEvento.setCodUfficioAggiornamento(aDepOrd.getCodUfficioAggiornamento());
			lEvento.setDataAggiornamento(aDepOrd.getDataAggiornamento());
			lEvento.setIdEvento(aDepOrd.getIdEventoGenerato());

			// Cancellazione ai riferimenti tramite EVE_ID_EVENTO E EVE_ID_EVENTO_REVOCA
			lEveDao.updateDAOFromModelForResetRifEve(lEvento);

			// cancellazione Evento collegato
			lEveDao.selCondizioneUpdate(aDepOrd.getIdEventoGenerato());
			lEveDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellato evento collegato a DepOrdinanzaPC " + lIdDepOrd);
		} catch (DAOException daoEx) {
			throw new F3BException("DepositoOrdinanzaPcController.ExCancellaDepositoOrdinanza: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("DepositoOrdinanzaPcController.ExCancellaDepositoOrdinanza: " + e);
		} finally {
			cleanup(lDepDao);
			cleanup(lTenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lNotDao);
			cleanup(lEveDao);
		}
	}

	/**
	 * Inserisci Emissione e Ordiannza
	 *
	 * @param lGPModel
	 * @return lGPModel
	 * @throws F3BException
	 */
	public GPTenoreModel ExInserisciEmissioneOrdinanza(GPTenoreModel lGPModel) throws F3BException {

		GeneraleProcedimentoDAO lGenDAO = null;
		TenoreDAO lTenDAO = null;
		Connection lConn = null;
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() Luigi 17-3-2004 siesLogger.debug("lettura preventiva per id gen proc ->"+
		 * lGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento()); DepositoOrdinanzaPcModel
		 * lDepOrd = ExRicercaDepositoOrdinanzaPcByGenProc
		 * (lGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento()); if (lDepOrd != null) throw
		 * new SIUSException(SIUSException.USER_MESSAGE,
		 * "L'Emissione di Deposito Ordinanza e' gia' presente");
		 */
		try {
			lConn = getDBTransaction();
			lGenDAO = new GeneraleProcedimentoDAO(lConn);
			lGenDAO.setDAOFromModelForUpdate(lGPModel.getGeneraleProcedimentoModel());
			lGenDAO.update();

			lTenDAO = new TenoreDAO(lConn);
			/*
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("Fase di delete per il Tenore");
			 *
			 * lTenDAO.setCondizioneDelete(lGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento()
			 * ) ; lTenDAO.delete();
			 */
			// I Tenori non vengono piu' cancellati ma chiusi ! Luigi 10-12-2003
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di chiusura per il Tenore");
			TenoreModel lTenore = new TenoreModel();
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setCodOperatoreAggiornamento(
					lGPModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					lGPModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataAggiornamento(lGPModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setDataFine(lGPModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					lGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenDAO.setDAOFromModelForUpdateDataFine(lTenore);
			lTenDAO.update();
			lTenDAO.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di inserimento per il Tenore");
			// Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller

			// int lIndex = lGPModel.getTenori().length;

			for (int lIndex = 0; lIndex < lGPModel.getTenori().length; lIndex++) {
				lTenDAO.setDAOFromModel(lGPModel.getTenori()[lIndex]);
				BigDecimal lKey = lTenDAO.insert();
				lTenDAO.stop();
				lGPModel.getTenori()[lIndex].setIdTenore(lKey);
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("DepositoOrdinanzaPcController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lGenDAO);
			cleanup(lTenDAO);

			cleanup(lConn);
		}
		return lGPModel;
	}

	/**
	 * Stamapa l'Ordinanaza
	 *
	 * @param aEvento
	 * @param aFasc
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumento(FascicoloGPModel aFasc, EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// Generazione documento di stampa 23-2-2005
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			lByteArrayOut = lCtrlSta.ExPreStampaDocumentoOrdinanza(aFasc, aEvento, aUtenteModel);
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

			// EventoNotificaModel lEveMod =
			// this.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());
			/*
			 * aEvento.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());
			 * TreeModel lTree = this.prelevaDatiOrdinanza(aEvento,aFasc); ReportGenerator lReport = new //
			 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() ReportGenerator(); siesLogger.debug("Chiave = " +
			 * aEvento.getNomeTemplate()); String lNomeTemplate =
			 * TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate()); // [FT] - 03/08/2016
			 * - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate); lByteArrayOut = (ByteArrayOutputStream)
			 * lReport.generateDocument(lTree, lNomeTemplate);
			 */

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampaDocumento: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampaDocumento: " + sqe);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Esecuzione stampa Emissione Ordinanza
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampEmissioneOrdinanza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.
		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Generazione documento di stampa
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();

			// Da Implementare
			lByteArrayOut = lCtrlSta.ExPreStampaEmissioneOrdinanza(lEvento, aCodUff, aUtenteModel);
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>>>> Generato il Documento .");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("EVENTO >>> " + lEvento.toString());
			// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
			// precisamente nel attributo DocBlobIn.
			lEvento.setDocBlobIn(lByteArrayInput);

			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(lEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(lEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoex);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampEmissioneOrdinanza: " + daoex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Stampa i modelli pe l'Ordinanza
	 *
	 * @param aEvento
	 * @param aFasc
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoModello(FascicoloGPModel aFasc, EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			aEvento.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			// Generazione documento di stampa 23-2-2005
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			lByteArrayOut = lCtrlSta.ExPreStampaDocumentoOrdinanza(aFasc, aEvento, aUtenteModel);
			// ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampaDocumentoModello: " + sqe);
		}
		return lByteArrayOut;
	}

	/**
	 * Esegue la ricerca del deposito ordinanza per l'id di generale procedimento.
	 *
	 * @param aGenProcKey
	 *            id generale procedimento.
	 * @return dati dell'ordinanza.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByGenProc(BigDecimal aGenProcKey)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaDepositoOrdinanzaPcByGenProcedimento(aGenProcKey);
			lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPcByGenProc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Esegue la ricerca del deposito ordinanza per l'id di generale procedimento e per tipo ordinanza.
	 *
	 * @param aGenProcKey
	 *            id generale procedimento,
	 * @param aCodTipoOrd
	 *            codice tipo Ordinanza.
	 * @return dati dell'ordinanza.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(BigDecimal aGenProcKey,
			String aCodTipoOrd) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaDepositoOrdinanzaPcByGenProcedimento(aGenProcKey, aCodTipoOrd);
			lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Inserisce la data di deposito dell'ordinanza, aggiorna l'evento e inserisce una notifica per ogni
	 * destinatario.
	 *
	 * @param aFasGPMod
	 *            dati del fasciclo GP Model.
	 * @param aDepositoOrdinanzaPc
	 *            dati di deposito ordinanza.
	 * @param aEveNot
	 *            dati di eventonotifica
	 * @return OrdinanzaEventoTenoriModel i dati dell'ordinanza con evento e tenori.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DocumentoAllegatoModel ExInserisciDataDepositoOrdinanza(FascicoloGPModel aFasGPMod,
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc, EventoNotificaModel aEveNot, String[] lCheck,
			ScadenzarioSiusModel lScadenzarioSiusModPrincipal, ScadenzarioSiusModel lScadenzarioSiusModSecond)
			throws F3BException {

		Connection lConn = null;

		AutoritaEsternaDAO lAutDao = null;
		DepositoOrdinanzaPcDAO lDepDao = null;
		DepositoOrdinanzaPcSqlDAO lDepDaoSql = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDaoSql = null;
		FascicoloGPSqlDAO lFasDao = null;
		MisuraAlternativaSqlDAO lMASqlDao = null;
		MisuraAlternativaDAO lMADao = null;
		CSSASqlDAO lCSSADao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;
		FascicoloSiusDAO lFasSiusDao = null;
		ScadenzarioSiusDAO lScadSiusDao = null;
		ScambioSanzioneDAO lScambioSanzioneDao = null;
		ScambioSanzioneSqlDAO lScambioSanzioneSqlDao = null;
		RichiestaConversioneDAO lRicConvDao = null; // 09/03/2008
		DecodificheDAO lDecDao = null; // 08/04/2011

		DocumentoAllegatoModel lDocAMod = null;
		DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel(aDepositoOrdinanzaPc);

		try {
			// Connessione
			lConn = getDBTransaction();

			// Viene istanziato il DAO per l'update di DEPOSITO_ORDINANZA_PC
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);

			if (lDepMod.getNumS3() == null) {
				// e' il primo inserimento
				// Si valorizza l'Anno corrente perche' il progressivo e' calcolato per anno
				lDepMod.setAnnoS3(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));

				// Trova il valore da assegnare al progressivo NUM_S3.
				lDepDaoSql = new DepositoOrdinanzaPcSqlDAO(lConn);
				lDepDaoSql.getProgressivoS3(lDepMod);
				lDepDaoSql.start();

				BigDecimal lBigDec = new BigDecimal(0);
				if (lDepDaoSql.next() && (lDepDaoSql.getBigDecimal("aMAX") != null))
					lBigDec = lDepDaoSql.getBigDecimal("aMAX");
				lDepDaoSql.stop();

				if (lBigDec == null)
					lBigDec = new BigDecimal(0);

				// Il campo Num_S3 viene valorizzato con l'ultimo valore presente + 1
				// Setto il NumS3 del Model di DepositoOrdinanzaPC con il MAX + 1
				lDepMod.setNumS3(new BigDecimal(lBigDec.intValue() + 1));

				// Update di ANNO e PROGR viene eseguito solo la prima volta
				lDepDao.setAnnoS3(lDepMod.getAnnoS3());
				lDepDao.setNumS3(lDepMod.getNumS3());
			}

			// Attraverso il DAO si realizza l'UPDATE

			// Effettuo l'inserimento data deposito in DepositoOrdinanzaModel; carico i dati da aggiornare.
			lDepDao.setCodUfficioAggiornamento(lDepMod.getCodUfficioAggiornamento());
			lDepDao.setCodOperatoreAggiornamento(lDepMod.getCodOperatoreAggiornamento());
			lDepDao.setDataAggiornamento(lDepMod.getDataAggiornamento());
			lDepDao.setDataDeposito(lDepMod.getDataDeposito());
			lDepDao.setCondizioneUpdate(lDepMod.getIdDepositoOrdinanzaPc());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per DepositoOrdinanzaPc (Data Deposito)");
			lDepDao.update();

			// Aggiornamento di eventuali Licenze collegate all'Ordinanza Luigi 20-09-2006
			if (lDepMod.getIdEventoGenerato() != null) {
				LicenzaLibanticipataDAO lLicLibDAO = new LicenzaLibanticipataDAO(lConn);
				// 29/06/2011 Risolto errore indotto dalla Modifica x Visibilita' Stato di Esecuzione:
				// Update LicenzaLibAnticipata va aggiornata per FAS_SIE_ID_FASCICOLO_SIEP.
				// lLicLibDAO.setDAOFromOrdinanzaForUpdate(lDepMod);
				lLicLibDAO.setDAOFromOrdinanzaForUpdate(lDepMod,
						aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

				lLicLibDAO.update();
			} else
				throw (new SIUSException(F3BException.EX_NOT_FOUND,
						"Manca ID Evento Generato nell'Ordinanza"));

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());

			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP

			// 06/04/2011 Modifica x Visibilita' Stato di Esecuzione.
			// lEveMod.setFlagVideoSiep("S");
			// lEveMod.setFlagStampaSiep("S");
			lEveMod.setFlagVideoSiep("");
			lEveMod.setFlagStampaSiep("");
			DecodificheModel lDecodifiche = new DecodificheModel();
			if (aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lDecDao = new DecodificheDAO(lConn);
				lDecDao.setCondizioneContestoRwLowValue("OGGETTO_PROCEDIMENTO",
						aFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
				lDecodifiche = (DecodificheModel) lDecDao.getModelByKey();
				if (lDecodifiche.getCodiceAlt2() != null && lDecodifiche.getCodiceAlt2()
						.compareTo(ICostantiStatoEsecuzione.STATO_ESECUZIONE_RISTRETTO) == 0) {
					lEveMod.setFlagVideoSiep("S");
					lEveMod.setFlagStampaSiep("S");
					lEveMod.setFasSieIdFascicoloSiep(
							aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				} else if (lDecodifiche.getCodiceAlt2() != null && lDecodifiche.getCodiceAlt2()
						.compareTo(ICostantiStatoEsecuzione.STATO_ESECUZIONE_ESTESO) == 0) {
					lEveMod.setFlagVideoSiep("N");
					lEveMod.setFlagStampaSiep("N");
					lEveMod.setFasSieIdFascicoloSiep(
							aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				}
			}

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "02");
			lDocAllDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellati doc allegati collegati a Evento " + lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(lDepMod.getDataDeposito());
			lDocAMod.setCodTipoDocumento("02");
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lDepMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lDepMod.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_OR_500");
			// lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Cancellazione Notifiche ");
			lNotDao = new NotificaDAO(lConn);
			if (lCheck != null) {
				for (int z = 0; z < lCheck.length; z++) {
					lNotDao.start();
					lNotDao.setCondizioneUpdate(new BigDecimal(lCheck[z].toUpperCase()));
					lNotDao.delete();
					lNotDao.stop();
				}
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Cancellazione Notifiche. ");

			// STUB 03/11/2003 Cancellazione delle misure alternative preesistenti per l'evento selezionato.
			// Misura Alternativa gia' inserita non viene cancellata. Luigi 6-3-2009
			/*
			 * lMADao = new MisuraAlternativaDAO(lConn); lMADao.start(); // [FT] - 03/08/2016 - MAC_LOG -
			 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug(">>>>>>> Evento per cancellazione misure alternative = "
			 * +lEveMod.getIdEvento()) ; lMADao.setCondizioneByIdEvento(lEveMod.getIdEvento()); // [FT] -
			 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() lMADao.delete();
			 * siesLogger.debug(">>>>>>> Fine Cancellazione Misure Alternative. ") ;
			 */

			// Insert delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");

				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					// STUB 24/10/2003 lEveNot.getNotifiche()[count].setEveIdEvento(lEveMod.getIdEvento());
					lNotDao.setDAOFromModel(lEveNot.getNotifiche()[count]);

					lNotDao.insert();
					lNotDao.stop();

					count++;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserite Notifiche per Evento" + lEveMod.getIdEvento());
			}

			// Stub 22/09/2003. Se il procedimento fa riferimento a un fascicolo SIEP,
			// e l'evento trattato e' relativo alla concessione/rigetto/revoca di Misura Alternativa,
			// occorre inserire un record nella tabella MISURA_ALTERNATIVA.
			lFasDao = new FascicoloGPSqlDAO(lConn);
			lMASqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMADao = new MisuraAlternativaDAO(lConn);
			lCSSADao = new CSSASqlDAO(lConn);

			boolean lExistFasSiep = lFasDao
					.existFasSiep(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

			// MEV_62 [EC] 15/05/2018 - INIZIO
			boolean lGiaEsisteMA = lMADao.esisteMisuraAlternativaPerEvento(lEveMod.getIdEvento());
			if (lGiaEsisteMA) {
				BigDecimal anno = lDepMod.getAnnoS3();
				BigDecimal numero = lDepMod.getNumS3();
				if (anno != null && numero != null) {
					lMADao.setAnnoRegistro(anno);
					lMADao.setNumeroRegistro(numero);
					lMADao.update();
				}
			}
			// MEV_62 [EC] 15/05/2018 - FINE

			if (lExistFasSiep && !(lMADao.esisteMisuraAlternativaPerEvento(lEveMod.getIdEvento()))
					&& lMASqlDao.eventoCoRiRe(lEveMod.getIdEvento())) {
				MisuraAlternativaModel lMAModel = new MisuraAlternativaModel();
				lMAModel.setCodTipoDecisione("03");
				lMAModel.setCodNaturaDecisione(lMASqlDao.ricecaNaturaDecisione(lEveMod.getIdEvento()));
				// STUB 04/10/2004
				if (lEveMod.getCodEsito().compareTo("0002") == 0)
					lMAModel.setCodTipoMisura("9000");
				else if (lEveMod.getCodEsito().compareTo("0003") == 0)
					lMAModel.setCodTipoMisura("9001");
				else
					lMAModel.setCodTipoMisura(lEveMod.getCodMotivo());
				lMAModel.setDataDecisione(lDepMod.getDataCameraConsiglio());
				lMAModel.setCodMagistrato(lDepMod.getCodMagistrato());
				lMAModel.setCodUfficioSorveglianza(lDepMod.getCodUfficioMagistratoComp());
				lMAModel.setCssIdCssa(lDepMod.getIdCssaComp());
				lMAModel.setDescrLuogoProva(lDepMod.getLuogoSvolgimentoProva());
				lMAModel.setDataInizioMisura(null); // STUB 24/09/2003 Da Gestire con la fase di
													// sottoscrizione degli obblighi.
				lMAModel.setChiaveAnnoFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveAnno());
				lMAModel.setChiaveUfficioFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveUfficio());
				lMAModel.setChiaveProgrFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveProgr());
				lMAModel.setAnnoRegistro(lDepMod.getAnnoS3());
				lMAModel.setNumeroRegistro(lDepMod.getNumS3());
				lMAModel.setFasSieIdFascicoloSiep(lEveMod.getFasSieIdFascicoloSiep());
				lMAModel.setEveIdEvento(lEveMod.getIdEvento());
				// 18/12/2003 Valorizzazione Campo CodTipoUfficioScarcerazione.
				lMAModel.setCodTipoUfficioScarcerazione("SORV");

				lMAModel.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
				lMAModel.setCodOperatoreInserimento(lDepMod.getCodOperatoreAggiornamento());
				lMAModel.setDataInserimento(lDepMod.getDataAggiornamento());

				// Correzione del 10-05-2006 Luigi
				lMAModel.setDataInizioRevoca(lDepMod.getDataDecorrenza());
				// Valorizzazione dei campi condizionata dal Codice Natura Decisione
				if (lMAModel.getCodNaturaDecisione() != null) {
					String lCodNaturaDecisione = lMAModel.getCodNaturaDecisione();
					if (lCodNaturaDecisione.equalsIgnoreCase("CO")) {
						lMAModel.setNumAnniMisura(lDepMod.getNumAnniDetenzioneDom());
						lMAModel.setNumMesiMisura(lDepMod.getNumMesiDetenzioneDom());
						lMAModel.setNumGiorniMisura(lDepMod.getNumGiorniDetenzioneDom());
					} else if (lCodNaturaDecisione.equalsIgnoreCase("RE")) {
						lMAModel.setNumAnniRevocaReclusione(lDepMod.getNumAnniDetenzioneDom());
						lMAModel.setNumMesiRevocaReclusione(lDepMod.getNumMesiDetenzioneDom());
						lMAModel.setNumGiorniRevocaReclusione(lDepMod.getNumGiorniDetenzioneDom());

						lMAModel.setNumAnniRevocaArresto(lDepMod.getNumAnniArrestoRev());
						lMAModel.setNumMesiRevocaArresto(lDepMod.getNumMesiArrestoRev());
						lMAModel.setNumGiorniRevocaArresto(lDepMod.getNumGiorniArrestoRev());
					}
				}

				lMADao.setDAOFromModel(lMAModel);
				/* BigDecimal lKeyMA = */lMADao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserita Misura Alternativa per evento -> " + lEveMod.getIdEvento());
			}

			// 2008-01-09 - Gestione inserimento record nella tabella SCAMBIO_SANZIONE.
			// 2009-03-11 - Isolata la fase di gestione delle Sanzioni Sostitutive per inserimento record
			// nella tabella SCAMBIO_SANZIONE.
			if (lDepMod.getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.APPLICAZIONE_SANZIONI_SOSTITUTIVE) == 0
					|| lDepMod.getCodTipoOrdinanza()
							.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_SANZIONI_SOSTITUTIVE) == 0
					|| lDepMod.getCodTipoOrdinanza()
							.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_SANZIONI_SOSTITUTIVE) == 0
					||
					// 08/07/2015
					// lDepMod.getCodTipoOrdinanza().compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE)==0
					// || // 20/03/2015
					lDepMod.getCodTipoOrdinanza()
							.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_SANZIONE_SOSTITUTIVA) == 0) {
				lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
				lScambioSanzioneDao = new ScambioSanzioneDAO(lConn);

				if (lExistFasSiep
						&& !lScambioSanzioneDao.esisteScambioSanzionePerEvento(lEveMod.getIdEvento())
						&& lScambioSanzioneSqlDao.eventoSS(lEveMod.getIdEvento())) {
					ScambioSanzioneModel lScambioSanzioneModel = new ScambioSanzioneModel();
					lScambioSanzioneModel.setCodOperatoreInserimento(lDepMod.getCodOperatoreAggiornamento());
					lScambioSanzioneModel.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
					lScambioSanzioneModel.setDataInserimento(lDepMod.getDataAggiornamento());
					lScambioSanzioneModel.setCodTipoDecisione(lEveMod.getCodTipoProvvedimento());
					lScambioSanzioneModel.setCodNaturaSanzione(
							lScambioSanzioneSqlDao.getNaturaDecisioneSS(lEveMod.getIdEvento()));
					lScambioSanzioneModel.setCodTipoSanzione(lEveMod.getCodMotivo());
					lScambioSanzioneModel.setAnnoRegistro(lDepMod.getAnnoS3());
					lScambioSanzioneModel.setNumeroRegistro(lDepMod.getNumS3());
					lScambioSanzioneModel
							.setChiaveAnnoFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveAnno());
					lScambioSanzioneModel
							.setChiaveProgrFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveProgr());
					lScambioSanzioneModel
							.setCodUfficioSorveglianza(aFasGPMod.getFascicoloSiusModel().getChiaveUfficio());
					lScambioSanzioneModel.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
					lScambioSanzioneModel.setDataEmissione(lEveMod.getDataEmissione());
					lScambioSanzioneModel.setEveIdEvento(lEveMod.getIdEvento());
					lScambioSanzioneModel.setFasSieIdFascicoloSiep(lEveMod.getFasSieIdFascicoloSiep());
					// Conversione della sanzione sostitutiva paolo c. 3/3/2008
					lScambioSanzioneModel.setNumGiorniArresto(lDepMod.getNumGiorniArrestoRev());
					lScambioSanzioneModel.setNumMesiArresto(lDepMod.getNumMesiArrestoRev());
					lScambioSanzioneModel.setNumAnniArresto(lDepMod.getNumAnniArrestoRev());
					lScambioSanzioneModel.setNumGiorniReclusione(lDepMod.getNumGiorniDetenzioneDom());
					lScambioSanzioneModel.setNumMesiReclusione(lDepMod.getNumMesiDetenzioneDom());
					lScambioSanzioneModel.setNumAnniReclusione(lDepMod.getNumAnniDetenzioneDom());

					lScambioSanzioneDao.setDAOFromModel(lScambioSanzioneModel);
					lScambioSanzioneDao.insert();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Eseguito Inserimento ScambioSanzione : " + lScambioSanzioneModel + " <<<<");
				}
			}

			// STUB 15-02-2006 Lo stato del fascicolo cambia se il Deposito Ordinanza non riguarda il Rinvio
			// Udienza.
			if (lEveMod.getCodEsito().compareTo("0603") != 0) {
				lFasSiusDao = new FascicoloSiusDAO(lConn);
				lFasSiusDao.setDAOFromModelForUpdate(aFasGPMod.getFascicoloSiusModel());
				// INIZIO: MEV_2019-09 (D.lgs. 123/2018)
				if (lEveMod.getCodEsito().compareTo("0605") != 0) {
					// INIZIO: MEV_2019-09 (D.lgs. 123/2018)
					if (lEveMod.getCodEsito().compareTo("0270") == 0) {
						// MEV_2024-092: non più utilizzato; al suo posto 07 = COD_EMESSO_PROVVEDIMENTO
						// ICostantiFascicoloSius.COD_EMESSA_ORDINANZA_APPLICAZIONE_PROVVISORIA
						lFasSiusDao.setCodStatoFascicolo("07");
					} // FINE: MEV_2019-09
					else
						lFasSiusDao.setCodStatoFascicolo("07");
				} else if (aFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("07") != 0)
					lFasSiusDao.setCodStatoFascicolo("13");
				lFasSiusDao.update();
				lFasSiusDao.stop();
			}

			// 09-03-2009 In caso di Ordinanza di Conversione P.P.:
			// a) Si aggiorna RICHIESTA_CONVERSIONE inserendo la Data Deposito.
			// b) Si Inserisce opportunamente una occorrenza di SCAMBIO_SANZIONE.
			if (lDepMod.getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE) == 0
					// 30/09/2015
					|| lDepMod.getCodTipoOrdinanza().compareTo(
							ICostantiDepositoOrdinanzaPc.DICHIARAZIONE_ESTINZIONE_LIB_CONTROLLATA) == 0) {
				lRicConvDao = new RichiestaConversioneDAO(lConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setFasSiuIdFascicoloSius(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lRCModel.setCodOperatoreAggiornamento(lDepMod.getCodOperatoreAggiornamento());
				lRCModel.setCodUfficioAggiornamento(lDepMod.getCodUfficioAggiornamento());
				lRCModel.setDataAggiornamento(lDepMod.getDataAggiornamento());
				lRCModel.setDataDeposito(lDepMod.getDataDeposito());
				lRicConvDao.setDAOFromModelForDepOrdinanzaCPP(lRCModel);
				lRicConvDao.selCondizioneByIdFasSius(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lRicConvDao.update();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Aggiornate le Richieste Conv. Pene Pec. x  Deposito Ordinanza"
						+ lDepMod.getIdDepositoOrdinanzaPc());

				// Inserimento Scambio Sanzione.
				lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
				lScambioSanzioneDao = new ScambioSanzioneDAO(lConn);

				if (lExistFasSiep
						&& (!lScambioSanzioneDao.esisteScambioSanzionePerEvento(lEveMod.getIdEvento()))
						&& lScambioSanzioneSqlDao.eventoSS(lEveMod.getIdEvento())) {
					ScambioSanzioneModel aScaSanMod = new ScambioSanzioneModel();
					aScaSanMod.setEveIdEvento(lEveMod.getIdEvento());
					aScaSanMod.setCodTipoDecisione(lEveMod.getCodTipoProvvedimento());
					aScaSanMod.setCodTipoSanzione(lEveMod.getCodMotivo());
					aScaSanMod.setCodNaturaSanzione(lEveMod.getCodEsito());
					aScaSanMod.setAnnoRegistro(lDepMod.getAnnoS3());
					aScaSanMod.setNumeroRegistro(lDepMod.getNumS3());
					aScaSanMod.setChiaveAnnoFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveAnno());
					aScaSanMod
							.setChiaveProgrFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveProgr());
					aScaSanMod
							.setCodUfficioSorveglianza(aFasGPMod.getFascicoloSiusModel().getChiaveUfficio());
					aScaSanMod.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
					aScaSanMod.setCodOperatoreInserimento(lDepMod.getCodOperatoreAggiornamento());
					aScaSanMod.setCodUfficioInserimento(lDepMod.getCodUfficioAggiornamento());
					aScaSanMod.setDataInserimento(lDepMod.getDataAggiornamento());
					aScaSanMod.setDataEmissione(lEveMod.getDataEmissione());

					aScaSanMod.setFasSieIdFascicoloSiep(lEveMod.getFasSieIdFascicoloSiep());

					lScambioSanzioneDao.setDAOFromModel(aScaSanMod);
					lScambioSanzioneDao.insert();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							">>>> Inserita Scambio Sanzione x  Deposito Ordinanza Conv. Pene Pecuniarie "
									+ lDepMod.getIdDepositoOrdinanzaPc());
				}
			}

			// Aggiorna/Inserisce Scadenzario
			// ---- Scadenzario principale
			if (lScadenzarioSiusModPrincipal != null) {
				lScadSiusDao = new ScadenzarioSiusDAO(lConn);
				lScadSiusDao.setDAOFromModelForUpdate(lScadenzarioSiusModPrincipal);
				lScadSiusDao.update();
				lScadSiusDao.stop();
			}
			// ---- Scadenzario secondario
			if (lScadenzarioSiusModSecond != null) {
				lScadSiusDao = new ScadenzarioSiusDAO(lConn);
				// Se trova l'ID viene effettuato l'update
				if (lScadenzarioSiusModSecond.getIdScadenzarioSius() != null) {
					lScadSiusDao.setDAOFromModelForUpdate(lScadenzarioSiusModSecond);
					lScadSiusDao.update();
					lScadSiusDao.stop();
				} else // se non trova l'ID inserisce lo scadenzario
				{
					lScadSiusDao.setDAOFromModel(lScadenzarioSiusModSecond);
					lScadSiusDao.insert();
					lScadSiusDao.stop();
				}
			}
			// FINE -- Aggiorna/Inserisce Scadenzario

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciDataDepositoOrdinanza: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new SIUSException("DepositoOrdinanzaPcController.ExInserisciDataDepositoOrdinanza: " + ex);
		} finally {
			cleanup(lAutDao);
			cleanup(lDepDao);
			cleanup(lDepDaoSql);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAvvDaoSql);
			cleanup(lFasDao);
			cleanup(lMASqlDao);
			cleanup(lMADao);
			cleanup(lCSSADao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);
			cleanup(lFasSiusDao);
			cleanup(lScadSiusDao);
			cleanup(lScambioSanzioneDao);
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lRicConvDao);
			cleanup(lDecDao); // 06/11/2011

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/**
	 * Verifica l'esistenza di un deposito decreto per l'id di generale procedimento.
	 *
	 * @param aKey
	 *            id di generale procedimento.
	 * @return esito della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(BigDecimal aKey, Date aData)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);

			int lNum = lDepOrdSqlDao.getNumDepoOrdinanzaByGenProcDataEmissione(aKey, aData);
			if (lNum > 0)
				lEsiste = true;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc - Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * La funzione verifica l'esistenza di almeno un'ordinanza emesso per uno specifico Generale Procedimento
	 * individuato dal suo id e che sia di un tipo decreto non compreso tra quelli nella lista definita nella
	 * funzione stessa.
	 *
	 * @param aKey
	 *            : id di generale procedimento,
	 * @param aTipiDaEscludere
	 *            : elenco dei tipi di decreto da non comprendere nella ricerca,
	 * @return esito della verifica : true, false.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean ExEsisteDepositoOrdinanzaByGenProcEccettoTipi(BigDecimal aKey, String[] aTipiDaEscludere)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();
			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			int lNum = lDepOrdSqlDao.getNumDepoOrdinanzaByGenProcEccettoTipi(aKey, aTipiDaEscludere);
			if (lNum > 0)
				lEsiste = true;
		} catch (Exception sqe) {
			throw new SIUSException(
					"DepositoDecretoController.ExEsisteDepositoDecretoByGenProcDataEmissione: " + sqe);
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Esegue la ricerca di una Ordinanza aggregando dati : Evento, Prescrizioni e Tenori, per la chiave
	 * idEvento.
	 *
	 * @param aIdEvento
	 *            id evento per ricerca ordinanza.
	 * @return ritorna i dati di ricerca come aggregato di model.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public OrdinanzaEventoTenoriPrescrizioniModel ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(
			BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;

		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		PrescrizioneSqlDAO lPreSqlDao = null;

		OrdinanzaEventoTenoriPrescrizioniModel lOrdEveTenPreMod = new OrdinanzaEventoTenoriPrescrizioniModel();

		try {
			lConn = getDBConnection();

			// Preleva L'EVENTO
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);

			lOrdEveTenPreMod.setEvento((EventoModel) lEveSqlDao.getModelByKey());

			// Dati ordinanza.
			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();

			/*
			 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato 
			 * Numero MEV : SIES v10 
			 * Autore : gioggi 
			 * Data : 27/gen/2016 
			 * Branch : MEV_SIES v10
			 */
			// lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato( aIdEvento );
			lDepOrdSqlDao.ricercaDepositoOrdinanzaCssaUssmPcByIdEveGenerato(aIdEvento);
			// ***** FINE INTERVENTO MEV_SIES v10 *****//

			lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();
			// Controllo di esistenza Ordinanza.
			if (lDepOrdMod == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Nessuna ordinanza per l'evento selezionato.");

			lDepOrdMod.setDescrUfficioMagistratoComp(
					RicercaComunebyUfficio(lDepOrdMod.getCodUfficioMagistratoComp()));
			lDepOrdMod.setDescrUffTdsConcessoRiduzione(
					RicercaComunebyUfficio(lDepOrdMod.getCodUffTdsConcessoRiduzione()));

			lOrdEveTenPreMod.setOrdinanza(lDepOrdMod);

			// Dati Prescrizioni.
			lPreSqlDao = new PrescrizioneSqlDAO(lConn);
			/*
			 * Le prescrizioni sono collegate all'evento e non piu' al deposito ordinanza. Luigi 12-12-2003
			 */
			lPreSqlDao.ricercaPrescrizioneByIdEve(aIdEvento);
			Vector lPrescrizioni = new Vector(lPreSqlDao.getModels());
			lOrdEveTenPreMod
					.setPrescrizioni((PrescrizioneModel[]) lPrescrizioni.toArray(new PrescrizioneModel[0]));

			// Preleva i Tenori.
			lTenSqlDao = new TenoreSqlDAO(lConn);
			// Ricerca Tenori x ID DepositoOrdinanza Luigi 5-12-2003
			lTenSqlDao.ricercaTenoriByOrdinanzaOrderByPeso(lDepOrdMod.getIdDepositoOrdinanzaPc());
			Vector lTenori = new Vector(lTenSqlDao.getModels());
			lOrdEveTenPreMod.setTenori((TenoreModel[]) lTenori.toArray(new TenoreModel[0]));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento - Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepOrdSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lPreSqlDao);
			cleanup(lConn);
		}

		return lOrdEveTenPreMod;
	}

	/**
	 * STUB: 20031014 - Recupero dei destinatari con impipamento dei dati nel formato TIPO DESTINATARIO | SEDE
	 * | COD_UFFICIO .
	 *
	 * @param aIdDepositoOrdinanzaPc
	 * @return lStampa
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public String getDestinatariDeposito(BigDecimal aIdDepositoOrdinanzaPc) throws F3BException {

		Connection lConn = null;

		String lSedi = new String("");
		DepositoOrdinanzaPcSqlDAO lDepDao = null;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaDepositoOrdinanzaPcByKey(aIdDepositoOrdinanzaPc);

			DepositoOrdinanzaPcModel lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();

			// In caso di Ordinanza con Data Deposito valorizzata, e' possibile caricare i destinatari.
			if (lDepMod.getDataDeposito() != null) {
				// Preleva i destinatari dell'ordinanza.
				INotifica lNotCtrl = SIEPLookupRemote.getNotificaRemote();
				/* Vector lNotifica = */lNotCtrl
						.ExRicercaEstesaNotificaByKeyEvento(lDepMod.getIdEventoGenerato());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lSedi;
	}

	/**
	 * Stamapa il documento allegato all'Ordinanaza
	 *
	 * @param aDocAllegato
	 * @param aFasc
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaDocumentoAllegato(BigDecimal aIdFascicoloSius,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel) throws F3BException {

		Connection lConn = null;
		DocumentoAllegatoDAO lDADao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// Generazione documento di stampa
			IStampaSius lCtrlSt = SIUSLookupRemote.getStampaRemote();
			lByteArrayOut = lCtrlSt.ExPreStampaAllegato(aIdFascicoloSius, aDAMod, aCodUff, aUtenteModel);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DOCUMENTO_ALLEGATO >>> " + aDAMod.toString());

			aDAMod.setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lDADao = new DocumentoAllegatoDAO(lConn);
			lDADao.setDAOFromModelForUpdateBlob(aDAMod);

			lDADao.setCondizioneUpdate(aDAMod.getIdDocumentoAllegato());
			lDADao.update();
			commit(lConn);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampaDocumentoAllegato: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampaDocumentoAllegato: " + sqe);
		} finally {
			cleanup(lDADao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Esecuzione stampa Foglio Complementare
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaFoglioComp(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;

		Connection lConn = null;
		DocumentoAllegatoDAO lDADao = null;
		DocumentoAllegatoSqlDAO lDASqlDao = null;

		try {
			// Generazione documento di stampa
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			lByteArrayOut = lCtrlSta.ExPreStampaEmissioneOrdinanza(lEvento, aCodUff, aUtenteModel);
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Lettura del Documento Allegato.
			DocumentoAllegatoModel lDAModel = new DocumentoAllegatoModel();

			// Settaggio delle condizioni di ricerca
			lDAModel.setEveIdEvento(lEvento.getIdEvento());
			lDAModel.setCodTipoDocumento("06");
			lDASqlDao = new DocumentoAllegatoSqlDAO(lConn);
			lDASqlDao.ricercaDocumentoAllegato(lDAModel);
			Vector lDocAll = new Vector(lDASqlDao.getModels());
			if (lDocAll.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Documento Allegato Mancante");

			// Si presuppone che il Documento Allegato sia unico.
			lDAModel = (DocumentoAllegatoModel) lDocAll.get(0);

			// Aggiornamento Documento Allegato.
			lDAModel.setDocBlobIn(lByteArrayInput);
			lDAModel.setDataAggiornamento(DateUtils.getSysDate());
			lDAModel.setCodUfficioAggiornamento(aCodUff);
			lDAModel.setCodOperatoreAggiornamento(aUtenteModel.getUserId());
			lDAModel.setFlagDocumentoRegistrato("N");

			lDADao = new DocumentoAllegatoDAO(lConn);
			lDADao.setDAOFromModelForUpdateBlob(lDAModel);

			// Selezione delle condizioni di Update.
			lDADao.setCondizioneUpdate(lDAModel.getIdDocumentoAllegato());
			lDADao.update();

			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoex);
			throw new F3BException("DepositoOrdinanzaPcController.ExStampaFoglioComp: " + daoex);
		} finally {
			cleanup(lDASqlDao);
			cleanup(lDADao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Esegue la ricerca del deposito ordinanza per l'id di Evento.
	 *
	 * @param aEveKey
	 *            id Evento.
	 * @return dati dell'ordinanza.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByEvento(BigDecimal aEveKey)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);

			/*
			 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
			 * Numero MEV : SIES v10 
			 * Autore : gioggi 
			 * Data : 27/gen/2016 
			 * Branch : MEV_SIES v10
			 */
			// lDepDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aEveKey);
			lDepDao.ricercaDepositoOrdinanzaCssaUssmPcByIdEveGenerato(aEveKey);
			// ***** FINE INTERVENTO MEV_SIES v10 *****//

			lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();
			if (lDepMod != null)
				lDepMod = RicercaUffici(lDepMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPcByGenProc: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	// 01/2014 AMBROS MIS SIC
	/**
	 * Query usata nella Gestione MISURE SICUREZZA dalla parte SIEP. Esegue la ricerca del deposito ordinanza
	 * di Esito sulla Richiesta di Accertamento Pericolosita' Sociale; L'interrogazione cerca nel
	 * FASCICOLO_SIUS.FAS_SIE_ID_FASCICOLO_SIEP il procedimento SIEP Corrente e nel
	 * EVENTO.FAS_SIE_ID_FASCICOLO_SIEP il procedimento SIEP Corrente
	 *
	 * @param aFascSiepKey
	 *            id FASCICLO_SIEP
	 * @return dati dell'ordinanza.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep(BigDecimal aFascSiepKey)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		Vector lDepVec = new Vector();

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.ricercaDepositoOrdinanzaPcByFascicoloSiepIdEveGenerato(aFascSiepKey);

			lDepDao.start();
			OrdinanzaEventoTenoriFascicoloSiusModel lDepMod = null;
			while (lDepDao.next()) {
				lDepMod = (OrdinanzaEventoTenoriFascicoloSiusModel) lDepDao.getModelEsitoMisSic();
				lDepVec.add(lDepMod);
			}
			lDepDao.stop();

			// if(lDepVec.size()==0)
			// {
			// throw new
			// F3BException(F3BException.USER_MESSAGE,"Nessuna Ordinanza con esito per questo procedimento
			// SIEP");
			// }
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lDepVec;
	} // CHIUDE ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep

	// 06/2014 MIS SIC
	/**
	 * Query usata nella Gestione MISURE SICUREZZA dalla parte SIEP. Esegue la ricerca dei provvedimenti di
	 * Archiviazione lato SIUS elencati nella popup di elenco
	 *
	 * @param aFascSiepKey
	 *            id FASCICLO_SIEP
	 * @return dati dell'Evento
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep(BigDecimal aFascSiepKey)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		Vector lArcVec = new Vector();
		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.RicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep(aFascSiepKey);

			lDepDao.start();
			OrdinanzaEventoTenoriFascicoloSiusModel lDepMod = null;
			while (lDepDao.next()) {
				lDepMod = (OrdinanzaEventoTenoriFascicoloSiusModel) lDepDao.getModelEsitoArchMisSic();
				lArcVec.add(lDepMod);
			}
			lDepDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lArcVec;
	} // CHIUDE ExRicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep

	/**
	 * Inserisce la data di deposito del decreto, aggiorna l'evento e inserisce una notifica per ogni
	 * destinatario.
	 *
	 * @param aFasGPMod
	 * @param aDepositoDecreto
	 * @param aEveNot
	 * @return DepositoDecretoEventoModel
	 * @throws F3BException
	 */
	public DocumentoAllegatoModel ExModificaDataDepositoOrdinanza(FascicoloGPModel aFasGPMod,
			DepositoOrdinanzaPcModel aDepositoOrdinanza, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		NotificaDAO lNotDaoCanc = null;
		AutoritaEsternaDAO lAutDao = null;
		DepositoOrdinanzaPcDAO lDecDao = null;
		DepositoOrdinanzaPcSqlDAO lDecDaoSql = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDaoSql = null;
		FascicoloGPSqlDAO lFasDao = null;
		MisuraAlternativaSqlDAO lMASqlDao = null;
		MisuraAlternativaDAO lMADao = null;
		CSSASqlDAO lCSSADao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;

		DocumentoAllegatoModel lDocAMod = null;
		DepositoOrdinanzaPcModel lDecMod = new DepositoOrdinanzaPcModel(aDepositoOrdinanza);

		try {
			lConn = getDBTransaction();

			// Setto il DAO dal Model di DepositoOrdinanza per l'Update
			lDecDao = new DepositoOrdinanzaPcDAO(lConn);

			// Effettuo l'inserimento data deposito in DepositoOrdinanzaModel; carico i dati da aggiornare.
			lDecDao.setCodUfficioAggiornamento(lDecMod.getCodUfficioAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(lDecMod.getCodOperatoreAggiornamento());
			lDecDao.setDataAggiornamento(lDecMod.getDataAggiornamento());
			lDecDao.setDataDeposito(lDecMod.getDataDeposito());
			lDecDao.setCondizioneUpdate(lDecMod.getIdDepositoOrdinanzaPc());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per DepositoOrdinanza (Data Ordinanza)");
			lDecDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());

			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagStampaSiep("S");

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdate(lEveMod);
			lEveDao.update();

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "02");
			lDocAllDao.delete();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>> Cancellati doc allegati collegati a Evento " + lEveMod.getIdEvento());

			// Setto il NumeroProgressivo del Model di DocumentoAllegato con il MAX + 1 (per Uff. Inserimento
			// ed IdEvento).
			lDocAMod = new DocumentoAllegatoModel();
			lDocAMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAllSqlDao = new DocumentoAllegatoSqlDAO(lConn);
			BigDecimal lBigDecAll = lDocAllSqlDao.getProgressivo(lDocAMod);

			lDocAMod.setNumeroProgressivo(new BigDecimal(lBigDecAll.intValue() + 1));
			lDocAMod.setEveIdEvento(lEveMod.getIdEvento());
			lDocAMod.setDataEmissione(lDecMod.getDataDeposito());
			lDocAMod.setCodTipoDocumento("02");
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lDecMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lDecMod.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_OR_500");
			// lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setDAOFromModel(lDocAMod);
			lDocAMod.setIdDocumentoAllegato(lDocAllDao.insert());

			// Update delle Notifiche.
			lAutDao = new AutoritaEsternaDAO(lConn);

			BigDecimal lKeyAutorita = null;
			int count = 0;
			if (lEveNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti " + lEveNot.getNotifiche().length + " notifiche");
				while (count < lEveNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + lEveNot.getNotifiche()[count]);

					if (lEveNot.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(lEveNot.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(lEveNot.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							lEveNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}
					lNotDao = new NotificaDAO(lConn);
					lNotDao.setDAOFromModelForUpdate(lEveNot.getNotifiche()[count]);
					lNotDao.update();

					count++;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Modificate Notifiche per Evento" + lEveMod.getIdEvento());

				// Cancellazione delle notifiche preesistenti per le notifiche selezionate.
				lNotDaoCanc = new NotificaDAO(lConn);
				if (lCheck != null) {
					for (int z = 0; z < lCheck.length; z++) {
						lNotDaoCanc.start();
						lNotDaoCanc.setCondizioneUpdate(new BigDecimal(lCheck[z].toUpperCase()));
						lNotDaoCanc.delete();
						lNotDaoCanc.stop();
					}
				}
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(">>>>>>> Fine Cancellazione Notifiche. ");
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaController.ExModificaDataDepositoOrdinanza: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaController.ExModificaDataDepositoOrdinanza: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoCanc);
			cleanup(lAutDao);
			cleanup(lDecDao);
			cleanup(lDecDaoSql);
			cleanup(lAvvDaoSql);
			cleanup(lFasDao);
			cleanup(lMASqlDao);
			cleanup(lMADao);
			cleanup(lCSSADao);
			cleanup(lDocAllDao);
			cleanup(lDocAllSqlDao);

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/*
	 * Funzione di utility. Ricava la Descrizione del comune dal codice dell'ufficio.
	 */
	private String RicercaComunebyUfficio(String aCodUfficio) throws F3BException {

		String lDescComune = null; // Stringa restituita
		if (aCodUfficio != null && aCodUfficio.compareTo("-") != 0) {
			IUfficio lUff = null; // Interfaccia al Controller Ufficio
			lUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficio = null;

			// Preleva Ufficio Competente
			lUfficio = lUff.getUfficioByKey(aCodUfficio);
			lDescComune = lUfficio.getDescrComune();
		}
		return lDescComune;
	}

	private DepositoOrdinanzaPcModel RicercaUffici(DepositoOrdinanzaPcModel aDepOrdinanza)
			throws F3BException {

		aDepOrdinanza.setDescrUfficioMagistratoComp(
				RicercaComunebyUfficio(aDepOrdinanza.getCodUfficioMagistratoComp()));
		aDepOrdinanza.setDescrUffTdsConcessoRiduzione(
				RicercaComunebyUfficio(aDepOrdinanza.getCodUffTdsConcessoRiduzione()));
		aDepOrdinanza
				.setDescrUfficioInserimento(RicercaComunebyUfficio(aDepOrdinanza.getCodUfficioInserimento()));
		aDepOrdinanza.setDescrUfficioAggiornamento(
				RicercaComunebyUfficio(aDepOrdinanza.getCodUfficioAggiornamento()));
		return aDepOrdinanza;
	}

	public void ExAggiornaTenoriEvento(TenoreModel[] aTenori, EventoModel aEvento, BigDecimal aIdOrdinanza,
			BigDecimal aIdDecreto) throws F3BException {

		Connection lConn = null;

		try {
			// Tenore significativo
			TenoreModel lTenoreMax = null;

			// la connessione al DB
			lConn = getDBTransaction();

			// Aggiornamento dell' Esito dei Tenori
			aggiornaEsitoTenori(aTenori, lConn);

			// Selezione del tenore di peso maggiore per l'Ordinanza o per il Decreto
			if (aIdOrdinanza != null)
				lTenoreMax = ricercaTenoreSignificativoxOrdinanza(aIdOrdinanza, lConn);
			else if (aIdDecreto != null)
				lTenoreMax = ricercaTenoreSignificativoxDecreto(aIdDecreto, lConn);
			else
				throw new F3BException(F3BException.NULL_OBJECT_ERROR, "Manca ID Decreto");

			// Aggiornamento dell'Evento
			aggiornaEvento(aEvento, lTenoreMax, lConn);

			// Il decreto memorizza anche la data di emissione
			if (aIdDecreto != null)
				aggiornaDataEmissioneDecreto(aEvento, aIdDecreto, lConn);
			// 13/05/2008 MAC Planning 29119481 - Anche l'ordinanza memorizza la data di emissione
			else if (aIdOrdinanza != null)
				aggiornaDataEmissioneOrdinanza(aEvento, aIdOrdinanza, lConn);

			// 23/10/2008 MAC Planning 31493477 - Per le E.M.A anche la tabella MISURA_ALTERNATIVA va
			// aggiornata.
			aggiornaMisuraAlternativa(aEvento, lTenoreMax, lConn);

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaPcController.ExAggiornaTenoriEvento: " + e);
		} finally {
			cleanup(lConn);
		}
		return;
	}

	// 10102014 - DL 92 2014 Violazione CEDU -
	public void ExAggiornaTenoriEventoPeriodiLA(TenoreModel[] aTenori, EventoModel aEvento,
			DepositoOrdinanzaPcModel OrdinanzaMod, DepositoDecretoModel DecretoMod,
			LicenzaPeriodiLibAnticipataModel[] aLicenze) throws F3BException {

		LicenzaLibanticipataDAO lCancLicDao = null;
		// LicenzaPeriodiLibAnticipataModel[] lLicenze;

		LicenzaLibanticipataDAO lLicDao = null;
		LicenzaLibAnticipataModel lLicMod = null;
		BigDecimal lKeyLic = null;

		PeriodoLibanticipataDAO lPerDao = null;
		PeriodoLibAnticipataModel lPerMod = null;
		BigDecimal lKeyPer = null;
		PeriodoLibAnticipataModel[] lPeriodi = null;

		Connection lConn = null;
		try {
			// Tenore significativo
			TenoreModel lTenoreMax = null;
			// la connessione al DB
			lConn = getDBTransaction();

			// Aggiornamento dell' Esito dei Tenori
			aggiornaEsitoTenori(aTenori, lConn);

			// Selezione del tenore di peso maggiore per l'Ordinanza o per il Decreto
			if (OrdinanzaMod != null && OrdinanzaMod.getIdDepositoOrdinanzaPc() != null)
				lTenoreMax = ricercaTenoreSignificativoxOrdinanza(OrdinanzaMod.getIdDepositoOrdinanzaPc(),
						lConn);
			else if (DecretoMod != null && DecretoMod.getIdDepositoDecreto() != null)
				lTenoreMax = ricercaTenoreSignificativoxDecreto(DecretoMod.getIdDepositoDecreto(), lConn);
			else
				throw new F3BException(F3BException.NULL_OBJECT_ERROR, "Manca ID Ordinanza/ Decreto");

			// Aggiornamento dell'Evento
			aggiornaEvento(aEvento, lTenoreMax, lConn);

			// Aggiornamento Ordinanza anche la data di emissione/Camera di consiglio
			if (OrdinanzaMod != null && OrdinanzaMod.getIdDepositoOrdinanzaPc() != null) {
				OrdinanzaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				OrdinanzaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				OrdinanzaMod.setDataAggiornamento(aEvento.getDataAggiornamento());
				OrdinanzaMod.setDataCameraConsiglio(aEvento.getDataEmissione());
				aggiornaOrdinanza(aEvento, OrdinanzaMod, lConn);
			}

			// Aggiornamento Decreto anche la data di emissione
			if (DecretoMod != null && DecretoMod.getIdDepositoDecreto() != null) {
				DecretoMod.setDataEmissione(aEvento.getDataEmissione());
				aggiornaDecreto(aEvento, DecretoMod, lConn);
			}

			// L.A. e Periodi L.A. : Cancellazione
			lCancLicDao = new LicenzaLibanticipataDAO(lConn);
			lCancLicDao.setCondizioneIdEvento(aEvento.getIdEvento());
			lCancLicDao.delete();

			// L.A. e Periodi L.A. : Inserimento dato modificato
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lPerDao = new PeriodoLibanticipataDAO(lConn);

			if (aLicenze != null) {
				for (int i = 0; i < aLicenze.length; i++) {
					// Inserimento Licenza
					lLicMod = new LicenzaLibAnticipataModel(aLicenze[i].getLicenza());
					lLicMod.setEveIdEvento(aEvento.getIdEvento());
					lLicDao.setDAOFromModel(lLicMod);
					lKeyLic = lLicDao.insert();
					lLicMod.setIdLicenzaLibanticipata(lKeyLic);
					lLicDao.stop();

					lPeriodi = aLicenze[i].getPeriodi();
					if (lPeriodi != null) {
						// Inserimento Periodi
						for (int j = 0; j < lPeriodi.length; j++) {
							lPerMod = new PeriodoLibAnticipataModel(lPeriodi[j]);
							lPerMod.setLicIdLicenzaLibanticipata(lKeyLic);
							lPerDao.setDAOFromModel(lPerMod);
							lKeyPer = lPerDao.insert();
							lPerDao.stop();
							lPerMod.setIdPeriodoLibanticipata(lKeyPer);
						}
					} else
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug(
								"DepositoOrdinanzaPcController.ExAggiornaTenoriEventoPeriodiLA: Inserimento PeriodiLicenzeLibanticipata: mancano periodi");
				} // Chiude ciclo for (int i = 0;
			} // Chiude if(aLicenze != null)

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaPcController.ExAggiornaTenoriEventoPeriodiLA: " + e);
		} finally {
			cleanup(lConn);
			cleanup(lCancLicDao);
			cleanup(lLicDao);
			cleanup(lPerDao);
		}
		return;
	}

	private void aggiornaEsitoTenori(TenoreModel[] aTenori, Connection aConn) throws F3BException {

		TenoreDAO lTenDAO = null;
		try {
			// Viene istanziato il DAO alla tabella TENORE
			lTenDAO = new TenoreDAO(aConn);

			// Aggiornamento dell' Esito dei Tenori
			int lNumTenori = aTenori.length;
			for (int i = 0; i < lNumTenori; i++) {
				lTenDAO.setCodOperatoreAggiornamento(aTenori[i].getCodOperatoreAggiornamento());
				lTenDAO.setCodUfficioAggiornamento(aTenori[i].getCodUfficioAggiornamento());
				lTenDAO.setDataAggiornamento(aTenori[i].getDataAggiornamento());
				lTenDAO.setData(aTenori[i].getData());
				lTenDAO.setCodEsitoTenore(aTenori[i].getCodEsitoTenore());
				lTenDAO.setCondizioneUpdate(aTenori[i].getIdTenore());
				lTenDAO.update();
				lTenDAO.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiornato tenore -> " + i);
			}
		} catch (Exception e) {
			throw new F3BException("Errore nell'aggiornamento dell'esito: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenDAO);
		}
	}

	private void aggiornaEvento(EventoModel aEvento, TenoreModel aTenoreSignificativo, Connection aConn)
			throws F3BException {

		EventoDAO lEventoDao = null;
		try {
			// Viene istanziato il DAO alla tabella EVENTO
			lEventoDao = new EventoDAO(aConn);

			// Aggiornamento dell'Evento
			lEventoDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEventoDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEventoDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lEventoDao.setDataEmissione(aEvento.getDataEmissione());
			// L'Evento eredita il codice Motivo e l'Esito del tenore piu' significativo
			lEventoDao.setCodMotivo(aTenoreSignificativo.getCodOggettoTenore());
			lEventoDao.setTenIdTenore(aTenoreSignificativo.getIdTenore());
			lEventoDao.setCodEsito(aTenoreSignificativo.getCodEsitoTenore());
			lEventoDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEventoDao.update();
			lEventoDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornato l'Evento");
		} catch (Exception e) {
			throw new F3BException("Errore nell'aggiornamento dell'evento: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEventoDao);
		}
	}

	/*
	 * 23/10/2008 MAC Planning 31493477 - Per le E.M.A anche la tabella MISURA_ALTERNATIVA va aggiornata. Il
	 * metodo consente l'aggiornamento della MISURA_ALTERNATIVA. La lettura dell'EVENTO e' effettuata solo se
	 * esite la MISURA_ALTERNATIVA che lo referenzia.
	 */
	private void aggiornaMisuraAlternativa(EventoModel aEvento, TenoreModel aTenoreSignificativo,
			Connection aConn) throws F3BException {

		MisuraAlternativaDAO lMisuraDao = null;
		MisuraAlternativaSqlDAO lMASqlDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			// Solo con il buon esito della ricerca di MISURA_ALTERNATIVA per ID_EVENTO,
			// si opera l'aggiornamento della stessa.
			lMASqlDao = new MisuraAlternativaSqlDAO(aConn);
			lMASqlDao.ricercaMisuraAlternativaByIdEvento(aEvento.getIdEvento());
			// 17/04/2009 Risolto errore mancata modifica della MISURA_ALTERNATIVA in caso di modifica esito
			// nel deposito Ordinanza/Decreto.
			// if (lMASqlDao.next() )
			MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMASqlDao.getModelByKey();
			if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
				lMisuraDao = new MisuraAlternativaDAO(aConn);
				// Occorre l'evento.Viene istanziato il SqlDAO alla tabella MISURA_ALTERNATIVA
				lEveSqlDao = new EventoSqlDAO(aConn);
				lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
				aEvento = (EventoModel) lEveSqlDao.getModelByKey();

				// Aggiornamento della Misura Alternativa
				lMisuraDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lMisuraDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lMisuraDao.setDataAggiornamento(aEvento.getDataAggiornamento());
				lMisuraDao.setDataDecisione(aEvento.getDataEmissione());

				// La Misura Alternativa eredita il codice Motivo e l'Esito del tenore piu' significativo
				// e il sqlDAO(per la ricerca Natura Decisione).
				lMisuraDao.setCodNaturaDecisione(lMASqlDao.ricecaNaturaDecisione(aEvento.getIdEvento()));
				if (aEvento.getCodEsito().compareTo("0002") == 0)
					lMisuraDao.setCodTipoMisura("9000");
				else if (aEvento.getCodEsito().compareTo("0003") == 0)
					lMisuraDao.setCodTipoMisura("9001");
				else
					lMisuraDao.setCodTipoMisura(aEvento.getCodMotivo());

				lMisuraDao.setCodNaturaDecisione(lMASqlDao.ricecaNaturaDecisione(aEvento.getIdEvento()));

				lMisuraDao.setCondizioneByIdEvento(aEvento.getIdEvento());
				lMisuraDao.update();
				lMisuraDao.stop();
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiornata la Misura Alternativa");
		} catch (Exception e) {
			throw new F3BException("Errore nell'aggiornamento della Misura Alternativa: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lMisuraDao);
			cleanup(lMASqlDao);
			cleanup(lEveSqlDao);
		}
	}

	private void aggiornaDataEmissioneDecreto(EventoModel aEvento, BigDecimal aIdDepDecreto, Connection aConn)
			throws F3BException {

		DepositoDecretoDAO lDecDao = null;

		try {
			lDecDao = new DepositoDecretoDAO(aConn);
			lDecDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lDecDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lDecDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lDecDao.setDataEmissione(aEvento.getDataEmissione());
			lDecDao.setCondizioneUpdate(aIdDepDecreto);
			lDecDao.update();
			lDecDao.stop();
		} catch (Exception e) {
			throw new F3BException("Errore nell'aggiornamento data emissione nel decreto: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDecDao);
		}
	}

	// 13/05/2008 MAC Planning 29119481 - Anche la modifica dell'ordinanza memorizza la data di emissione.
	private void aggiornaDataEmissioneOrdinanza(EventoModel aEvento, BigDecimal aIdDepOrdinanza,
			Connection aConn) throws F3BException {

		DepositoOrdinanzaPcDAO lDepOrdDao = null;

		try {
			lDepOrdDao = new DepositoOrdinanzaPcDAO(aConn);
			lDepOrdDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lDepOrdDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lDepOrdDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lDepOrdDao.setDataCameraConsiglio(aEvento.getDataEmissione());
			lDepOrdDao.setCondizioneUpdate(aIdDepOrdinanza);
			lDepOrdDao.update();
			lDepOrdDao.stop();
		} catch (Exception e) {
			throw new F3BException("Errore nell'aggiornamento data emissione nell ordinanza: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDepOrdDao);
		}
	}

	// 10102014 - DL 92 2014 Violazione Cedu -->
	private void aggiornaOrdinanza(EventoModel aEvento, DepositoOrdinanzaPcModel ModOrd, Connection aConn)
			throws F3BException {

		DepositoOrdinanzaPcDAO lDepOrdDao = null;

		try {
			lDepOrdDao = new DepositoOrdinanzaPcDAO(aConn);

			lDepOrdDao.setDAOFromModelForUpdate(ModOrd);
			// lDepOrdDao.setDataCameraConsiglio(aEvento.getDataEmissione());
			lDepOrdDao.update();
			lDepOrdDao.stop();
		} catch (Exception e) {
			throw new F3BException(
					"DepositoOrdinanzaPcController.aggiornaOrdinanza: Errore nell'aggiornamento dell ordinanza: "
							+ e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDepOrdDao);
		}
	}

	private void aggiornaDecreto(EventoModel aEvento, DepositoDecretoModel aDepDecreto, Connection aConn)
			throws F3BException {

		DepositoDecretoDAO lDecDao = null;

		try {
			lDecDao = new DepositoDecretoDAO(aConn);
			lDecDao.setDAOFromModelForUpdate(aDepDecreto);
			lDecDao.update();
			lDecDao.stop();
		} catch (Exception e) {
			throw new F3BException(
					"DepositoOrdinanzaPcController.aggiornaDecreto: Errore nell'aggiornamento del decreto: "
							+ e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lDecDao);
		}
	}
	// End DL 92 2014 <--

	private TenoreModel ricercaTenoreSignificativoxOrdinanza(BigDecimal aIdProvvedimento, Connection aConn)
			throws Exception {

		TenoreSqlDAO lTenoreSqlDao = null;
		TenoreModel lTenoreMod = null;
		try {
			lTenoreSqlDao = new TenoreSqlDAO(aConn);
			// Select dati dal tenore + significativo.
			lTenoreSqlDao.ricercaTenoriByOrdinanzaOrderByPeso(aIdProvvedimento);
			lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();

			if (lTenoreMod == null)
				throw new F3BException(F3BException.EX_NOT_FOUND,
						"errore nella lettura del tenore significativo per l'Ordinanza");
		} catch (Exception e) {
			throw new F3BException("Errore in ricercaTenoreSignificativoxOrdinanza: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenoreSqlDao);
		}
		return lTenoreMod;
	}

	private TenoreModel ricercaTenoreSignificativoxDecreto(BigDecimal aIdDecreto, Connection aConn)
			throws Exception {

		TenoreSqlDAO lTenoreSqlDao = null;
		TenoreModel lTenoreMod = null;

		try {
			lTenoreSqlDao = new TenoreSqlDAO(aConn);
			lTenoreSqlDao.ricercaTenoriByDecretoOrderByPeso(aIdDecreto);
			lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();
			if (lTenoreMod == null)
				throw new F3BException(F3BException.EX_NOT_FOUND,
						"errore nella lettura del tenore per il decreto");
		} catch (Exception e) {
			throw new F3BException("Errore in ricercaTenoreSignificativoxDecreto: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenoreSqlDao);
		}
		return lTenoreMod;
	}

	/**
	 * Ricerca DepositoOrdinanzaPc da ANNO, NUM e cod Ufficio.
	 *
	 * @param aDepositoOrdinanzaPc
	 * @return
	 * @throws F3BException
	 */

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByAnnoNumUfficio(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcDAO lDepDao = null;
		DepositoOrdinanzaPcModel lDepMod;

		try {
			// Si effettua una ricerca nella Tabella DEPOSITOORDINANZAPC
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcDAO(lConn);
			lDepDao.setCondizione(aDepositoOrdinanzaPc);
			lDepMod = (DepositoOrdinanzaPcModel) lDepDao.getModelByKey();
			if (lDepMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Ordinanza non trovata");
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("Errore nella ricerca dell'Ordinanza: " + e);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lDepMod;
	}

	/**
	 * Esegue l'inserimento dell'Emissione Ordinanza, del Periodo Altra Sanzione e modifica l'Esecuzione
	 * Sanzione Sostitutiva . Description: Funzione per l'inserimento dell'Emissione di un'ordinanza generico,
	 * di Periodo Altra Sanzione e Esecuzione Sanzione Sostitutiva Le tabelle coinvolte sono:
	 * DEPOSITO_ORDINANZAPC : viene inserito il nuovo record decreto; EVENTO : viene inserito un nuovo record;
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori; GENERALE_PROCEDIMENTO :
	 * update del contenuto del procedimento. PERIODO_ALTRA_SANZIONE: viene inserito un nuovo record.
	 * ESECUZIONE_SANZIONE_SOST: update della data_termine_attuale e dei dati di aggiornamento
	 *
	 * @param OrdinanzaEventoTenoriGProcModel
	 *            , aPeriodoAltraSanzioneModel, aEsecuzioneSanzioneSostitutivaModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaPeriodoAltraSanzioneModificaESS(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori,
			PeriodoAltraSanzioneModel aPeriodoAltraSanzioneModel,
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutivaModel) throws F3BException {

		Connection lConn = null;
		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lModRet = null;
		// Dao per inserimento
		PeriodoAltraSanzioneDAO lPasDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEssDAo = null;

		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			if (aPeriodoAltraSanzioneModel != null) {
				// Inserimento Periodo Altra Sanzione
				aPeriodoAltraSanzioneModel.setEveIdEvento(lModRet.getEvento().getIdEvento());
				lPasDao = new PeriodoAltraSanzioneDAO(lConn);
				lPasDao.setDAOFromModel(aPeriodoAltraSanzioneModel);
				lPasDao.insert();
			}

			if (aEsecuzioneSanzioneSostitutivaModel != null) {
				// Modifica Esecuzione Sanzione Sostitutiva
				lEssDAo = new EsecuzioneSanzioneSostitutivaDAO(lConn);
				lEssDAo.setDAOFromModelForUpdate(aEsecuzioneSanzioneSostitutivaModel);
				lEssDAo.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaPeriodoAltraSanzioneModificaESS: "
							+ daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaPeriodoAltraSanzioneModificaESS: "
							+ sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaPeriodoAltraSanzioneModificaESS: "
							+ e);
		} finally {
			cleanup(lEssDAo);
			cleanup(lPasDao);
			cleanup(lConn);
		}
		return lModRet;
	}

	/**
	 * Esegue l'inserimento dell'Emissione Ordinanza, di SCAMBIO_SANZIONE e modifica le RICHIESTA_CONVERSIONE
	 * collegate al Procedimento. Description: Funzione per l'inserimento dell'Emissione di un'ordinanza
	 * generica, di SCAMBIO_SANZIONE e modifica di RICHIESTA_CONVERSIONE Le tabelle coinvolte sono:
	 * DEPOSITO_ORDINANZAPC : viene inserito il nuovo record decreto; EVENTO : viene inserito un nuovo record;
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori; GENERALE_PROCEDIMENTO :
	 * update del contenuto del procedimento. SCAMBIO_SANZIONE: viene inserito un nuovo record.
	 * RICHIESTA_CONVERSIONE: update dei dati immessi in fase di emissione ordinanza
	 *
	 * @param OrdinanzaEventoTenoriGProcModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaConversioneRateizzazionePP(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori,
			RichiesteConversioniPerOrdinanzaModel aRicConvMod) throws F3BException {

		Connection lConn = null;
		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lModRet = null;

		// Dao per inserimento
		// ScambioSanzioneDAO lScaSanDAO = null;
		RichiestaConversioneDAO lRicConvDAO = null;

		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			// Ciclo di Aggiornamento delle RICHIESTA_CONVERSIONE.
			for (int j = 0; j < aRicConvMod.getIdRichiestaConversione().length; j++) {
				lRicConvDAO = new RichiestaConversioneDAO(lConn);
				RichiestaConversioneModel lRicConv = new RichiestaConversioneModel();

				lRicConv.setIdRichiestaConversione(
						new BigDecimal(aRicConvMod.getIdRichiestaConversione()[j]));
				lRicConv.setCodTipoSanzione(aRicConvMod.getCodTipoSanzione()[j]);

				if (aRicConvMod.getCodTipoRichiesta()[j].trim()
						.compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_CONVERSIONE) == 0) {
					if ((aRicConvMod.getNumGiorniDurataEsito()[j]).length() != 0)
						lRicConv.setDurataEsitoGiorni(
								new BigDecimal(aRicConvMod.getNumGiorniDurataEsito()[j]));
					if ((aRicConvMod.getNumMesiDurataEsito()[j]).length() != 0)
						lRicConv.setDurataEsitoMesi(new BigDecimal(aRicConvMod.getNumMesiDurataEsito()[j]));
					if ((aRicConvMod.getNumAnniDurataEsito()[j]).length() != 0)
						lRicConv.setDurataEsitoAnni(new BigDecimal(aRicConvMod.getNumAnniDurataEsito()[j]));
				}
				if (aRicConvMod.getCodTipoRichiesta()[j].trim()
						.compareTo(ICostantiDepositoOrdinanzaPc.TIPO_CONV_RATEIZZAZIONE) == 0) {
					if ((aRicConvMod.getNumeroRate()[j]).length() != 0)
						lRicConv.setNumeroRate(new BigDecimal(aRicConvMod.getNumeroRate()[j]));
					if ((aRicConvMod.getValoreRata()[j]) != null)
						lRicConv.setValoreRata(aRicConvMod.getValoreRata()[j]);
					if ((aRicConvMod.getValoreUltimaRata()[j]) != null)
						lRicConv.setValoreUltimaRata(aRicConvMod.getValoreUltimaRata()[j]);
					if ((aRicConvMod.getDataInizioPagamento()) != null)
						lRicConv.setDataInizioPagamento(aRicConvMod.getDataInizioPagamento());
					if ((aRicConvMod.getNumGiorniInizioPagamento()) != null)
						lRicConv.setNumeroGiorniInizioPagamento(aRicConvMod.getNumGiorniInizioPagamento());
				}
				// 14/08/2015 lRicConv.setEveIdEvento(lModRet.getEvento().getIdEvento());
				lRicConv.setCodOperatoreAggiornamento(lModRet.getEvento().getCodOperatoreInserimento());
				lRicConv.setCodUfficioAggiornamento(lModRet.getEvento().getCodUfficioInserimento());
				lRicConv.setDataAggiornamento(lModRet.getEvento().getDataInserimento());

				lRicConvDAO.setDAOFromModelForUpdate(lRicConv);
				lRicConvDAO.selCondizioneUpdate(lRicConv.getIdRichiestaConversione());
				lRicConvDAO.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaConversioneRateizzazionePP : "
							+ daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaConversioneRateizzazionePP : "
							+ sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaConversioneRateizzazionePP : " + e);
		} finally {
			// cleanup(lScaSanDAO);
			cleanup(lRicConvDAO);
			cleanup(lConn);
		}
		return lModRet;
	}

	/**
	 * Esegue l'inserimento dell'Emissione Ordinanza, del Periodo Altra Misura e modifica l'Esecuzione Misura
	 * Sicurezza . Description: Funzione per l'inserimento dell'Emissione di un'ordinanza generico, di Periodo
	 * Altra Misura e Esecuzione Misura Sicurezza Le tabelle coinvolte sono: DEPOSITO_ORDINANZAPC : viene
	 * inserito il nuovo record decreto; EVENTO : viene inserito un nuovo record; TENORE : vengono chiusi i
	 * tenori attivi (data_fine) ed inseriti i nuovi tenori; GENERALE_PROCEDIMENTO : update del contenuto del
	 * procedimento. PERIODO_ALTRA_MISURA: viene inserito un nuovo record. ESECUZIONE_MISURA_SICUREZZA: update
	 * della data_termine_attuale e dei dati di aggiornamento
	 *
	 * @param OrdinanzaEventoTenoriGProcModel
	 *            , aPeriodoAltraMisuraModel, aEsecuzioneMisuraSicurezzaModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaPeriodoAltraMisuraModificaEMS(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori,
			PeriodoAltraMisuraModel aPeriodoAltraMisuraModel,
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezzaModel) throws F3BException {

		Connection lConn = null;
		// model di ritorno
		OrdinanzaEventoTenoriGProcModel lModRet = null;
		// Dao per inserimento
		PeriodoAltraMisuraDAO lPamDao = null;
		EsecuzioneMisuraSicurezzaDAO lEmsDAo = null;

		try {
			lConn = getDBTransaction();
			lModRet = ExInserisciOrdinanza(aGProcOrdEveTenori, lConn);

			if (aPeriodoAltraMisuraModel != null) {
				// Inserimento Periodo Altra Misura
				aPeriodoAltraMisuraModel.setEveIdEvento(lModRet.getEvento().getIdEvento());
				lPamDao = new PeriodoAltraMisuraDAO(lConn);
				lPamDao.setDAOFromModel(aPeriodoAltraMisuraModel);
				lPamDao.insert();
			}

			if (aEsecuzioneMisuraSicurezzaModel != null) {
				// Modifica Esecuzione Misura Sicurezza
				lEmsDAo = new EsecuzioneMisuraSicurezzaDAO(lConn);
				lEmsDAo.setDAOFromModelForUpdate(aEsecuzioneMisuraSicurezzaModel);
				lEmsDAo.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaPeriodoAltraMisuraModificaEMS: "
							+ daoEx);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SQLException: " + sqlEx);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaPeriodoAltraMisuraModificaEMS: "
							+ sqlEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(
					"DepositoOrdinanzaPcController.ExInserisciOrdinanzaPeriodoAltraMisuraModificaEMS: " + e);
		} finally {
			cleanup(lEmsDAo);
			cleanup(lPamDao);
			cleanup(lConn);
		}
		return lModRet;
	}

	/**
	 * Esegue modifica del magistrato per ordinanza.
	 */
	public DepositoOrdinanzaPcModel ExModificaMagistratoOrdinanza(DepositoOrdinanzaPcModel aDepOrdPcMod)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcDAO lDepOrdPcDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdPcSqlDao = null;
		EventoDAO lEventoDao = null;
		TenoreDAO lTenoreDao = null;

		TenoreModel lTenoreMod = null;

		try {
			lConn = getDBConnection();
			// modifica l'evento
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setIdEvento(aDepOrdPcMod.getIdEventoGenerato());
			lEventoDao.setCodMagistrato(aDepOrdPcMod.getCodMagistrato());
			lEventoDao.setDataAggiornamento(new Date());
			lEventoDao.setCodUfficioAggiornamento(aDepOrdPcMod.getCodUfficioAggiornamento());
			lEventoDao.setCodOperatoreAggiornamento(aDepOrdPcMod.getCodOperatoreAggiornamento());
			lEventoDao.selByKey();
			lEventoDao.update();

			// legge l'id del deposito ordinaza per l'id evento genrato.
			lDepOrdPcSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepOrdPcSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(aDepOrdPcMod.getIdEventoGenerato());
			aDepOrdPcMod.setIdDepositoOrdinanzaPc(
					((DepositoOrdinanzaPcModel) lDepOrdPcSqlDao.getModelByKey()).getIdDepositoOrdinanzaPc());

			// modifica magistrato all'ordinanza.
			lDepOrdPcDao = new DepositoOrdinanzaPcDAO(lConn);
			lDepOrdPcDao.setIdEventoGenerato(aDepOrdPcMod.getIdEventoGenerato());
			lDepOrdPcDao.setCodMagistrato(aDepOrdPcMod.getCodMagistrato());
			lDepOrdPcDao.setDataAggiornamento(new Date());
			lDepOrdPcDao.setCodUfficioAggiornamento(aDepOrdPcMod.getCodUfficioAggiornamento());
			lDepOrdPcDao.setCodOperatoreAggiornamento(aDepOrdPcMod.getCodOperatoreAggiornamento());
			lDepOrdPcDao.setCondizioneUpdate(aDepOrdPcMod.getIdDepositoOrdinanzaPc());
			lDepOrdPcDao.update();

			// modifica dei tenori afferenti.
			lTenoreMod = new TenoreModel();
			lTenoreMod.setDepOpidDepositoOrdinanzaPc(aDepOrdPcMod.getIdDepositoOrdinanzaPc());
			lTenoreMod.setCodMagistrato(aDepOrdPcMod.getCodMagistrato());
			lTenoreMod.setCodOperatoreAggiornamento(aDepOrdPcMod.getCodUfficioAggiornamento());
			lTenoreMod.setDataAggiornamento(aDepOrdPcMod.getDataAggiornamento());
			lTenoreDao = new TenoreDAO(lConn);
			lTenoreDao.setDAOFromModelForUpdateMagistratoByOrdinanza(lTenoreMod);
			lTenoreDao.update();

			// commit
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoex);
			throw new SIUSException("DepositoOrdinanzaPcController.ExModificaMagistratoOrdinanza: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + ex);
			throw new SIUSException("DepositoOrdinanzaPcController.ExModificaMagistratoOrdinanza: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lDepOrdPcDao);
			cleanup(lDepOrdPcSqlDao);
			cleanup(lTenoreDao);
			cleanup(lConn);
		}
		return aDepOrdPcMod;
	}

	/**
	 * 13-11-2014 Misre Sicurezza Fuori Sentenza Query usata nella Gestione MISURE SICUREZZA dalla parte SIEP.
	 * Esegue la CONTA dei provvedimenti SIUS ordinanza per DATA; 
	 * MEV_39: aggiunto parametro di passaggio
	 *
	 * @param aData_inizio
	 * @param aData_fine
	 * @param aElaborati
	 * @param codUfficio
	 * @return BigDecimal
	 * @throws F3BException
	 */
	public BigDecimal ExCountProvvedimentiSoggettoPerMisuraFuoriSentenza(Date aData_inizio, Date aData_fine,
			Boolean aElaborati, String codUfficio) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		BigDecimal HowManyRecords = null;
		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.CountProvvFascicoloSiusTenoreSoggetto(aData_inizio, aData_fine, aElaborati, codUfficio);
			lDepDao.start();
			lDepDao.next();
			HowManyRecords = lDepDao.getBigDecimal("HowManyRecords");
			lDepDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExCountProvvedimentiSoggettoPerMisuraFuoriSentenza: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return HowManyRecords;
	} // CHIUDE ExCountProvvedimentiSoggettoPerMisuraFuoriSentenza

	/**
	 * 13-11-2014 Misre Sicurezza Fuori Sentenza Query usata nella Gestione MISURE SICUREZZA dalla parte SIEP.
	 * Esegue la RICERCA dei provvedimenti SIUS ordinanza per DATA;
	 * MEV_39: aggiunto parametro di passaggio
	 *
	 * @param aData_inizio
	 * @param aData_fine
	 * @param aElaborati
	 * @param codUfficio
	 * @param aPage
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaProvvedimentiSoggettoPerMisuraFuoriSentenza(Date aData_inizio, Date aData_fine,
			Boolean aElaborati, String codUfficio, int aPage) throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		OrdinanzaEventoTenoriFascicoloSiusModel lDepModel = null;
		Vector lProvv = new Vector();

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.RicercaProvvFascicoloSiusTenoreSoggetto(aData_inizio, aData_fine, aElaborati, codUfficio,
					aPage);
			lDepDao.start();

			while (lDepDao.next()) {
				lDepModel = (OrdinanzaEventoTenoriFascicoloSiusModel) lDepDao.getModelMisFuoriSentenza();
				lProvv.add(lDepModel);
			}
			lDepDao.stop();
			if (lProvv.isEmpty())
				throw new SIEPException(F3BException.USER_MESSAGE, "Nessun Provvedimento trovato ");

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx, daoEx);
			daoEx.printStackTrace();
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaProvvedimentiSoggettoPerMisuraFuoriSentenza: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lProvv;
	} // CHIUDE ExRicercaProvvedimentiSoggettoPerMisuraFuoriSentenza

	public void ExAggiornaTenoriEventoDOS(TenoreModel[] aTenori, EventoModel aEvento, BigDecimal aIdOrdinanza,
			BigDecimal aIdDecreto, BigDecimal aIdSentenza) throws F3BException {

		Connection lConn = null;

		try {
			// Tenore significativo
			TenoreModel lTenoreMax = null;

			// la connessione al DB
			lConn = getDBTransaction();

			// Aggiornamento dell' Esito dei Tenori
			aggiornaEsitoTenori(aTenori, lConn);

			// Selezione del tenore di peso maggiore per l'Ordinanza, il Decreto o la Sentenza
			if (aIdOrdinanza != null)
				lTenoreMax = ricercaTenoreSignificativoxOrdinanza(aIdOrdinanza, lConn);
			else if (aIdDecreto != null)
				lTenoreMax = ricercaTenoreSignificativoxDecreto(aIdDecreto, lConn);
			else if (aIdSentenza != null)
				lTenoreMax = ricercaTenoreSignificativoxSentenza(aIdSentenza, lConn);
			else
				throw new F3BException(F3BException.NULL_OBJECT_ERROR, "Manca ID Decreto/Ordinanza/Sentenza");

			// Aggiornamento dell'Evento
			aggiornaEvento(aEvento, lTenoreMax, lConn);

			// Il decreto memorizza anche la data di emissione
			if (aIdDecreto != null)
				aggiornaDataEmissioneDecreto(aEvento, aIdDecreto, lConn);
			// 13/05/2008 MAC Planning 29119481 - Anche l'ordinanza memorizza la data di emissione
			else if (aIdOrdinanza != null)
				aggiornaDataEmissioneOrdinanza(aEvento, aIdOrdinanza, lConn);
			else if (aIdSentenza != null)
				aggiornaDataEmissioneSentenza(aEvento, aIdSentenza, lConn);

			// 23/10/2008 MAC Planning 31493477 - Per le E.M.A anche la tabella MISURA_ALTERNATIVA va
			// aggiornata.
			aggiornaMisuraAlternativa(aEvento, lTenoreMax, lConn);

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("DepositoOrdinanzaPcController.ExAggiornaTenoriEventoDOS: " + e);
		} finally {
			cleanup(lConn);
		}
		return;
	}

	private TenoreModel ricercaTenoreSignificativoxSentenza(BigDecimal aIdProvvedimento, Connection aConn)
			throws Exception {

		TenoreSqlDAO lTenoreSqlDao = null;
		TenoreModel lTenoreMod = null;

		try {
			lTenoreSqlDao = new TenoreSqlDAO(aConn);

			// Select dati dal tenore + significativo.
			lTenoreSqlDao.ricercaTenoriBySentenzaOrderByPeso(aIdProvvedimento);
			lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();

			if (lTenoreMod == null)
				throw new F3BException(F3BException.EX_NOT_FOUND,
						"errore nella lettura del tenore significativo per la Sentenza");
		} catch (Exception e) {
			throw new F3BException("Errore in ricercaTenoreSignificativoxSentenza: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTenoreSqlDao);
		}

		return lTenoreMod;
	}

	private void aggiornaDataEmissioneSentenza(EventoModel aEvento, BigDecimal aIdDepSentenza,
			Connection aConn) throws F3BException {

		DepositoSentenzaDAO lSenDao = null;

		try {
			lSenDao = new DepositoSentenzaDAO(aConn);
			lSenDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lSenDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lSenDao.setDataAggiornamento(aEvento.getDataAggiornamento());
			lSenDao.setDataEmissione(aEvento.getDataEmissione());
			lSenDao.setCondizioneUpdate(aIdDepSentenza);
			lSenDao.update();
			lSenDao.stop();
		} catch (Exception e) {
			throw new F3BException("Errore nell'aggiornamento data emissione nella sentenza: " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSenDao);
		}
	}

	/*
	 * ISSUE MEV : aggiunti metodi di ricerca provvedimenti differimento SIUS 
	 * Numero MEV : 39 
	 * Autore : Gioggi
	 * Data : 24/feb/2017 
	 * Branch : MEV_39
	 */
	public Vector ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(BigDecimal idFascicoloSiep)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		Vector lDiffVec = new Vector();
		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.RicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(idFascicoloSiep);
			lDepDao.start();
			OrdinanzaEventoTenoriFascicoloSiusModel lDepMod = null;
			while (lDepDao.next()) {
				lDepMod = (OrdinanzaEventoTenoriFascicoloSiusModel) lDepDao.getModelEsitoDiffMisSic();
				lDiffVec.add(lDepMod);
			}
			lDepDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep: Non posso leggere: "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		// valore di ritorno
		return lDiffVec;
	}

	public OrdinanzaEventoTenoriFascicoloSiusModel ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(
			BigDecimal idFascicoloSiep, BigDecimal idFascSius, BigDecimal idEveFascSius, BigDecimal idEvento)
			throws F3BException {

		Connection lConn = null;
		DepositoOrdinanzaPcSqlDAO lDepDao = null;
		OrdinanzaEventoTenoriFascicoloSiusModel lDepMod = null;
		try {
			lConn = getDBConnection();
			lDepDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepDao.RicercaEventoProvvDiffSIUSByFascSiepEFascSius(idFascicoloSiep, idFascSius, idEveFascSius,
					idEvento);
			lDepDao.start();
			if (lDepDao.next())
				lDepMod = (OrdinanzaEventoTenoriFascicoloSiusModel) lDepDao.getModelEsitoDiffMisSic();
			lDepDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoOrdinanzaPcController.ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius: Non posso leggere: "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		// valore di ritorno
		return lDepMod;
	}
	// ***** FINE INTERVENTO MEV_39 *****//

}