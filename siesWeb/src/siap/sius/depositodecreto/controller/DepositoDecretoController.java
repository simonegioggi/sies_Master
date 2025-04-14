package siap.sius.depositodecreto.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.decodifiche.dao.DecodificheDAO;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoesecuzione.action.ICostantiStatoEsecuzione; // 11/04/2011
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.avvocatura.dao.AvvisiAvvocatoDAO;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel;
import siap.sius.depositodecreto.model.DepositoDecretoEventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
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
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoDAO;
import siap.sius.motivazionedecreto.dao.MotivazioneDecretoSqlDAO;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.prescrizione.dao.PrescrizioneDAO;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneDAO;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneSqlDAO;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * DepositoDecretoController - Classe Controller per DepositoDecreto
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DepositoDecretoController extends SiapController implements IDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua l'inserimento di un Deposito Decreto.
	 *
	 * @param aDepositoDecreto
	 *            Model con i dati da inserire.
	 * @return il model con i dati inseriti in più l'id del record.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoModel ExInserisciDepositoDecreto(DepositoDecretoModel aDepositoDecreto)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoDAO lDepDao = null;
		DepositoDecretoModel lDepMod = null;

		try {
			lConn = getDBConnection();

			lDepMod = new DepositoDecretoModel(aDepositoDecreto);
			lDepDao = new DepositoDecretoDAO(lConn);
			lDepDao.setDAOFromModel(aDepositoDecreto);
			BigDecimal lKey = null;
			lKey = lDepDao.insert();

			commit(lConn);
			lDepMod.setIdDepositoDecreto(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new SIUSException("DepositoDecretoController.ExInserisciDepositoDecreto: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Effettua la ricerca di depositi decreti.
	 *
	 * @param aDepositoDecreto
	 *            model deposito decreto con i dati utili per la ricerca.
	 * @return l'insieme dei dati trovati.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	/*
	 * public Vector ExRicercaDepositoDecreto (DepositoDecretoModel aDepositoDecreto ) throws F3BException {
	 * Connection lConn = null; Vector lDepositoDecreti = new Vector(); DepositoDecretoSqlDAO lDepDao = null;
	 *
	 * try { lConn = getDBConnection();
	 *
	 * lDepDao = new DepositoDecretoSqlDAO(lConn); lDepDao.ricercaDepositoDecreto(aDepositoDecreto);
	 * lDepositoDecreti = new Vector(lDepDao.getModels());
	 *
	 * if ( lDepositoDecreti.size() == 0 ) throw new
	 * SIUSException(SIUSException.USER_MESSAGE,"Nessun Elemento trovato");
	 *
	 * } catch (DAOException daoEx) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di LogF3B.getLogger() siesLogger.debug("DAOException: " + daoEx); throw new
	 * SIUSException("DepositoDecretoController.ExRicercaDepositoDecreto: Non posso leggere : " + daoEx); }
	 * catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	 * al posto di LogF3B.getLogger() siesLogger.debug("SQLException: " + sqe); throw new
	 * SIUSException("DepositoDecretoController.ExRicercaDepositoDecreto: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lDepDao); cleanup(lConn); }
	 *
	 * return lDepositoDecreti; }
	 */

	/**
	 * Effettua la ricerca di un singolo record di deposito decreto.
	 *
	 * @param aKey
	 *            l'id del deposito decreto da ricercare.
	 * @return il model con i dati del record ricercato.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoModel ExRicercaDepositoDecretoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		DepositoDecretoModel lDepMod;

		try {
			lConn = getDBConnection();

			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.ricercaDepositoDecretoByKey(aKey);
			lDepMod = (DepositoDecretoModel) lDepDao.getModelByKey();

			// Cerca eventuali Ufficio competente e Procura Esecuzione
			if (lDepMod != null)
				lDepMod = RicercaUffici(lDepMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ExRicercaDepositoDecretoByKey: " + daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Ricerca deposito decreto attraverso l'id generato.
	 *
	 * @param aIdEvento
	 *            l'id dell'evento.
	 * @return dati del deposito decreto.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoModel ExRicercaDepositoDecretoByIdEvento(BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		DepositoDecretoModel lDepMod;

		try {
			lConn = getDBConnection();

			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
			lDepMod = (DepositoDecretoModel) lDepDao.getModelByKey();

			// Cerca descrizione Uffici.
			if (lDepMod != null)
				lDepMod = RicercaUffici(lDepMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ExRicercaDepositoDecretoByIdEvento: " + daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

	/**
	 * Ricerca Deposito Decreto Inammissibilità e Motivazioni attraverso l'id generato.
	 *
	 * @param aIdEvento
	 *            l'id dell'evento.
	 * @return dati del deposito decreto e motivazioni.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoEventoMotivazioniModel ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(
			BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		DepositoDecretoSqlDAO lDepDao = null;
		MotivazioneDecretoSqlDAO lMotSqlDao = null;

		DepositoDecretoEventoMotivazioniModel lDepDecrMotMod = new DepositoDecretoEventoMotivazioniModel();

		try {
			lConn = getDBConnection();
			// Preleva il valore del flag di stato del documento registrato.
			lEveSqlDao = new EventoSqlDAO(lConn);
			EventoModel lEvento = null;
			// lEvento.setFlagDocumentoRegistrato( lEveSqlDao.getFlagDocumentoRegistrato( aIdEvento ) );

			// Cerca l'Evento by key
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			lEvento = (EventoModel) lEveSqlDao.getModelByKey();
			lDepDecrMotMod.setEvento(lEvento);

			// Deposito Decreto.
			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
			lDepDecrMotMod.setDepositoDecreto((DepositoDecretoModel) lDepDao.getModelByKey());
			// Verifica se esiste una occorenza del dposito decreto.
			// altrimenti rilancia l'errore di eccezione.
			if (lDepDecrMotMod.getDepositoDecreto() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Nessun decreto per l'evento selezionato");
			else
				// Cerca eventuali Ufficio competente e Procura Esecuzione
				lDepDecrMotMod.setDepositoDecreto(RicercaUffici(lDepDecrMotMod.getDepositoDecreto()));

			// Preleva le motivazioni decreto.
			BigDecimal lIdDepDecr = lDepDecrMotMod.getDepositoDecreto().getIdDepositoDecreto();
			lMotSqlDao = new MotivazioneDecretoSqlDAO(lConn);
			// Prende le motivazioni per il deposito decreto.
			lMotSqlDao.ricercaMotivazioneDecretoInammissibilitaByDepDec(lIdDepDecr);
			Vector lMotivazioni = new Vector(lMotSqlDao.getModels());
			lDepDecrMotMod.setMotivazioniDecreto(
					(MotivazioneDecretoModel[]) lMotivazioni.toArray(new MotivazioneDecretoModel[0]));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento: "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lMotSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}

		return lDepDecrMotMod;
	}

	/**
	 * Ricerca Deposito Decreto Incompetenza e Motivazioni attraverso l'id generato, inoltre preleva dalla
	 * tabella evento il flag di stato del documento registrato.
	 *
	 * @param aIdEvento
	 *            l'id dell'evento.
	 * @return dati del deposito decreto e motivazioni.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoEventoMotivazioniModel ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(
			BigDecimal aIdEvento) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		EventoSqlDAO lEveSqlDao = null;
		MotivazioneDecretoSqlDAO lMotSqlDao = null;

		DepositoDecretoEventoMotivazioniModel lDepDecrMotMod = new DepositoDecretoEventoMotivazioniModel();

		try {
			lConn = getDBConnection();

			// Preleva il valore del flag di stato del documento registrato.
			lEveSqlDao = new EventoSqlDAO(lConn);
			EventoModel lEvento = new EventoModel();
			lEvento.setFlagDocumentoRegistrato(lEveSqlDao.getFlagDocumentoRegistrato(aIdEvento));
			lDepDecrMotMod.setEvento(lEvento);

			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.ricercaDepositoDecretoByIdEveGenerato(aIdEvento);
			lDepDecrMotMod.setDepositoDecreto((DepositoDecretoModel) lDepDao.getModelByKey());
			// Verifica se esiste una occorenza del dposito decreto.
			// altrimenti rilancia l'errore di eccezione.
			if (lDepDecrMotMod.getDepositoDecreto() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Nessun decreto per l'evento selezionato");
			else
				// Cerca eventuali Ufficio competente e Procura Esecuzione
				lDepDecrMotMod.setDepositoDecreto(RicercaUffici(lDepDecrMotMod.getDepositoDecreto()));

			// Preleva le motivazioni decreto.
			BigDecimal lIdDepDecr = lDepDecrMotMod.getDepositoDecreto().getIdDepositoDecreto();
			lMotSqlDao = new MotivazioneDecretoSqlDAO(lConn);
			// Prende le motivazioni per il deposito decreto.

			lMotSqlDao.ricercaMotivazioneDecretoIncompetenzaByDepDec(lIdDepDecr);
			Vector lMotivazioni = new Vector(lMotSqlDao.getModels());

			lDepDecrMotMod.setMotivazioniDecreto(
					(MotivazioneDecretoModel[]) lMotivazioni.toArray(new MotivazioneDecretoModel[0]));
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento: "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lMotSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}

		return lDepDecrMotMod;
	}

	/**
	 * Ricerca Deposito Decreto, Evento, Fascicolo SIUS a partire dall'ID Soggetto.
	 * Nota: del Fascicolo vengono valorizzati solo ID, Anno e Progressivo e non gli altri campi.
	 *
	 * @param aIdSoggetto
	 *            l'id del soggetto.
	 * @return dati del deposito decreto e motivazioni.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExRicercaDecretoFascicoloByIdSoggetto(BigDecimal aIdSoggetto, String aTipoDecreto,
			String aTipoLicenza, String aCodUff) throws F3BException {

		Vector lVect = null;
		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		SoggettoSqlDAO lSogDao = null;
		try {
			lConn = getDBConnection();

			// Paolo Cherubini 02/05/2011 Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aIdSoggetto);
			SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lVect = lDepDao.RicercaDecretoLicenzaBySuperSoggetto(aSogModel, aTipoDecreto, aTipoLicenza,
					aCodUff);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException", daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExRicercaDecretoFascicoloByIdSoggetto: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new SIUSException("DepositoDecretoController.ExRicercaDecretoFascicoloByIdSoggetto: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lVect;
	}

	/**
	 * Ricerca Deposito Decreto, Fascicolo SIUS a partire dall'ID Evento Generato.
	 * Nota: del Fascicolo vengono valorizzati solo ID, Anno e Progressivo e non gli altri campi.
	 *
	 * @param aIdSoggetto
	 *            l'id del soggetto.
	 * @return dati del deposito decreto e motivazioni.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoFascicoloModel ExRicercaDecretoFascicoloByIdEvento(BigDecimal aIdEventoGenerato)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		DepositoDecretoFascicoloModel lModel = null;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lModel = lDepDao.RicercaDecretoFascicoloByIdEvento(aIdEventoGenerato);
		} catch (DAOException daoEx) {
			throw new SIUSException("DepositoDecretoController.ExRicercaDecretoFascicoloByKey: " + daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lModel;

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
	public boolean ExVerificaEsistenzaDepositoDecretoByIdGenProc(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			lDepSqlDao = new DepositoDecretoSqlDAO(lConn);
			lDepSqlDao.ricercaEsistenzaDepositoDecretoByIdGenProc(aKey);
			lDepSqlDao.start();
			lDepSqlDao.next();
			int lNum = lDepSqlDao.getInt("NUM_REC");

			if (lNum > 0)
				lEsiste = true;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExVerificaEsistenzaDepositoDecretoByIdGenProc: " + daoEx);
		} finally {
			cleanup(lDepSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Verifica l'esistenza di un deposito decreto per l'id di generale procedimento e di un tipo specificato.
	 *
	 * @param aKey
	 *            id di generale procedimento,
	 * @param aTipoDecreto
	 *            : cod_tipo_decreto.
	 * @return esito della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(BigDecimal aKey,
			String aCodTipoDecreto) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			lDepSqlDao = new DepositoDecretoSqlDAO(lConn);
			lDepSqlDao.ricercaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(aKey, aCodTipoDecreto);
			lDepSqlDao.start();
			lDepSqlDao.next();
			int lNum = lDepSqlDao.getInt("NUM_REC");

			if (lNum > 0)
				lEsiste = true;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec: "
							+ daoEx);
		} finally {
			cleanup(lDepSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Verifica l'esistenza di un deposito decreto per l'id di generale procedimento e di un tipo specificato.
	 *
	 * @param aKey
	 *            id di generale procedimento,
	 * @param aTipoDecreto
	 *            : cod_tipo_decreto.
	 * @return esito della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(BigDecimal aKey,
			String[] aCodTipiDecreto) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			lDepSqlDao = new DepositoDecretoSqlDAO(lConn);
			lDepSqlDao.ricercaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(aKey, aCodTipiDecreto);
			lDepSqlDao.start();
			lDepSqlDao.next();
			int lNum = lDepSqlDao.getInt("NUM_REC");

			if (lNum > 0)
				lEsiste = true;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec: "
							+ daoEx);
		} finally {
			cleanup(lDepSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Verifica l'esistenza di un deposito decreto per l'id di generale procedimento e per cod_esito
	 * nell'evento collegato.
	 *
	 * @param aKey
	 *            : id di generale procedimento,
	 * @param aCod
	 *            : cod_esito dell'evento.
	 * @return esito della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean ExEsisteDepositoDecretoByGenProcCodEsito(BigDecimal aKey, String aCod)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();
			lDepSqlDao = new DepositoDecretoSqlDAO(lConn);
			int lNum = lDepSqlDao.getNumDepDecretoByGenProcCodEsito(aKey, aCod);
			if (lNum > 0)
				lEsiste = true;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExEsisteDepositoDecretoByGenProcCodEsito: " + daoEx);
		} finally {
			cleanup(lDepSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Verifica l'esistenza di un deposito decreto per l'id di generale procedimento e per Data Emissione
	 * nell'evento collegato.
	 *
	 * @param aKey
	 *            : id di generale procedimento,
	 * @param aData
	 *            : data_emissione dell'evento.
	 * @return esito della verifica.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public boolean ExEsisteDepositoDecretoByGenProcDataEmissione(BigDecimal aKey, Date aData)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();
			lDepSqlDao = new DepositoDecretoSqlDAO(lConn);
			int lNum = lDepSqlDao.getNumDepDecretoByGenProcDataEmissione(aKey, aData);
			if (lNum > 0)
				lEsiste = true;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException(
					"DepositoDecretoController.ExEsisteDepositoDecretoByGenProcDataEmissione: " + daoEx);
		} finally {
			cleanup(lDepSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * La funzione verifica l'esistenza di almeno un decreto emesso per uno specifico Generale Procedimento
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
	public boolean ExEsisteDepositoDecretoByGenProcEccettoTipi(BigDecimal aKey, String[] aTipiDaEscludere)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepSqlDao = null;
		boolean lEsiste = false;

		try {
			lConn = getDBConnection();
			lDepSqlDao = new DepositoDecretoSqlDAO(lConn);
			int lNum = lDepSqlDao.getNumDepDecretoByGenProcEccettoTipi(aKey, aTipiDaEscludere);
			if (lNum > 0)
				lEsiste = true;
		} catch (Exception sqe) {
			throw new SIUSException(
					"DepositoDecretoController.ExEsisteDepositoDecretoByGenProcDataEmissione: " + sqe);
		} finally {
			cleanup(lDepSqlDao);
			cleanup(lConn);
		}
		return lEsiste;
	}

	/**
	 * Esegue la modifica di un determinato record di deposito decreto.
	 *
	 * @param aDepositoDecreto
	 *            contine i dati con cui modificare il record già esistente.
	 * @return ritorna il model con i dati appena modificati.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public DepositoDecretoModel ExModificaDepositoDecreto(DepositoDecretoModel aDepositoDecreto)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoDAO lDepDao = null;
		DepositoDecretoModel lDepMod = new DepositoDecretoModel(aDepositoDecreto);

		try {
			lConn = getDBConnection();

			lDepDao = new DepositoDecretoDAO(lConn);
			lDepDao.setDAOFromModelForUpdate(aDepositoDecreto);
			lDepDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new SIUSException("DepositoDecretoController.ExModificaDepositoDecreto: " + ex);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lDepMod;
	}

	/**
	 * Esegue la cancellazione di un record del deposito decreto.
	 *
	 * @param aKeyDepDec
	 *            : chiave del record
	 * @throws F3BException
	 */
	public void ExCancellaDepositoDecreto(DepositoDecretoModel aDepDec) throws F3BException {

		Connection lConn = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " : ExCancellaDepositoDecreto: inizio ");

		try {
			lConn = getDBConnection();
			ExCancellaDepositoDecreto(aDepDec, lConn);
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ExCancellaDepositoDecreto: " + daoEx);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqe);
			throw new SIUSException("DepositoDecretoController.ExCancellaDepositoDecreto: " + sqe);
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			throw new SIUSException("DepositoDecretoController.ExCancellaDepositoDecreto: " + e);
		} finally {
			cleanup(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("" + getClass().getName() + " : ExCancellaDepositoDecreto: fine ");
		}
	}

	/**
	 * Esegue la cancellazione di un record del deposito decreto.
	 *
	 * @param aDepDec
	 *            : model DepositoDecreto in cui siano valorizzati almeno i campi IDDepositoDecreto e
	 *            IDEvento_generato.
	 * @throws Exception
	 */

	public void ExCancellaDepositoDecreto(DepositoDecretoModel aDepDec, Connection aConn) throws Exception {

		MotivazioneDecretoDAO lMotDao = null;
		DepositoDecretoDAO lDepDao = null;
		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		PrescrizioneDAO lPreDao = null;
		PeriodoAltraSanzioneDAO lPASDao = null;
		PeriodoAltraSanzioneSqlDAO lPASSqlDao = null;
		PeriodoAltraMisuraDAO lPAMDao = null;
		PeriodoAltraMisuraSqlDAO lPAMSqlDao = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lESSSqlDao = null;
		EsecuzioneSanzioneSostitutivaDAO lESSDao = null;
		EsecuzioneMisuraSicurezzaSqlDAO lEMSSqlDao = null;
		EsecuzioneMisuraSicurezzaDAO lEMSDao = null;
		RichiestaConversioneDAO lRCDao = null; // 25/11/2015
		MisuraAlternativaDAO lMisAltDao = null;

		if (aDepDec == null)
			throw new SIUSException("DepositoDecreto nullo");

		BigDecimal lIdDecreto = aDepDec.getIdDepositoDecreto();
		if (lIdDecreto == null)
			throw new SIUSException("ID Decreto nullo");

		BigDecimal lIdEvento = aDepDec.getIdEventoGenerato();
		if (lIdEvento == null)
			throw new SIUSException("ID Evento nullo");

		try {
			// cancellazione Motivazioni collegate
			lMotDao = new MotivazioneDecretoDAO(aConn);
			lMotDao.setCondizioneByDepDecreto(lIdDecreto);
			lMotDao.delete();

			// update Tenori collegati
			lTenDao = new TenoreDAO(aConn);
			lTenDao.setDAOForDeleteDepDec(aDepDec);
			lTenDao.update();

			// cancellazione Deposito Decreto
			lDepDao = new DepositoDecretoDAO(aConn);
			lDepDao.setCondizioneUpdate(lIdDecreto);
			lDepDao.delete();

			// cancellazione eventuali Prescrizioni collegate
			lPreDao = new PrescrizioneDAO(aConn);
			// Le prescrizioni sono collegate all'evento
			lPreDao.setCondizioneByDepOrdPC(lIdEvento);
			lPreDao.delete();

			// MEV63
			lMisAltDao = new MisuraAlternativaDAO(aConn);
			lMisAltDao.setCondizioneByIdEvento(lIdEvento);
			lMisAltDao.delete();
			siesLogger.debug(">>>> Cancellate misure alternative collegate aL depositoDecreto " + lIdEvento);

			// --------------------------------------------------------------
			// Modifica l'Esecuzione Sanzione Sostituiva se necessario
			// e cancella il Periodo Altra Sanzione eventualmente collegato
			// --------------------------------------------------------------

			// ---- Ricerca in Periodo Altra Sanzione ----
			lPASSqlDao = new PeriodoAltraSanzioneSqlDAO(aConn);
			lPASSqlDao.ricercaSanzioneSostitutivaByIdEvento(lIdEvento);
			PeriodoAltraSanzioneModel lPASMod = (PeriodoAltraSanzioneModel) lPASSqlDao.getModelByKey();

			if (lPASMod != null && lPASMod.getFasSiuIdFascicoloSius() != null
					&& "S".equals(aDepDec.getFlagRecuperoSS())) {
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
							Calendar.DAY_OF_MONTH, -(aDepDec.getGiorniRecuperoSS().intValue()));
					lESSDao = new EsecuzioneSanzioneSostitutivaDAO(aConn);

					lESSDao.setDataTermineAttuale(lDataTermineAttuale);

					lESSDao.setCodUfficioAggiornamento(aDepDec.getCodUfficioAggiornamento());
					lESSDao.setCodOperatoreAggiornamento(aDepDec.getCodOperatoreAggiornamento());
					lESSDao.setDataAggiornamento(aDepDec.getDataAggiornamento());

					lESSDao.setCondizioneUpdate(lESSMod.getIdEsecuzioneSanzioneSost());
					lESSDao.update();
					lESSDao.stop();
				}
			}

			// cancellazione eventuale Periodo Altra Sanzione collegata
			lPASDao = new PeriodoAltraSanzioneDAO(aConn);
			// Il Periodo Altra Sanzione è collegato all'evento
			lPASDao.setCondizioneByEveIdEvento(lIdEvento);
			lPASDao.delete();

			// 25-11-2014 Modifica di eventuali Richieste Conversioni Pene Pecuniarie (Con Azzeramento dati di
			// Decreto NDP/NLP o Incompetenza).
			// previa lettura dell'Evento (prima di cancellarlo) per puntare all'IdFascicoloSius.
			if (aDepDec.getCodTipoDecreto().compareTo(ICostantiDepositoDecreto.NDP_NLP) == 0
					|| aDepDec.getCodTipoDecreto().compareTo(ICostantiDepositoDecreto.INCOMPETENZA) == 0) {
				lEveDao = new EventoDAO(aConn);
				lEveDao.setIdEvento(aDepDec.getIdEventoGenerato());
				lEveDao.selByKey();
				lEveDao.start();
				EventoModel lEventoSave = (EventoModel) lEveDao.getModelByKey();
				lEveDao.stop();
				lRCDao = new RichiestaConversioneDAO(aConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setFasSiuIdFascicoloSius(lEventoSave.getFasSiuIdFascicoloSius());
				lRCModel.setCodOperatoreAggiornamento(aDepDec.getCodOperatoreAggiornamento());
				lRCModel.setCodUfficioAggiornamento(aDepDec.getCodUfficioAggiornamento());
				lRCModel.setDataAggiornamento(aDepDec.getDataAggiornamento());
				// 14/08/2015 lRCModel.setEveIdEvento(null);
				lRCDao.setDAOFromModelForCancOrdinanzaCPP(lRCModel);
				lRCDao.selCondizioneByIdFasSius(lEventoSave.getFasSiuIdFascicoloSius());
				lRCDao.update();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Aggiornate le Richieste Conv. Pene Pec. collegata a DepositoDecreto"
						+ aDepDec.getIdDepositoDecreto());
			}

			// --------------------------------------------------------------
			// Modifica l'Esecuzione Misura Sicurezza se necessario
			// e cancella il Periodo Altra Misura eventualmente collegato
			// --------------------------------------------------------------

			// ---- Ricerca in Periodo Altra Misura ----
			lPAMSqlDao = new PeriodoAltraMisuraSqlDAO(aConn);
			lPAMSqlDao.ricercaMisuraSicurezzaByIdEvento(lIdEvento);
			PeriodoAltraMisuraModel lPAMMod = (PeriodoAltraMisuraModel) lPAMSqlDao.getModelByKey();

			if (lPAMMod != null && lPAMMod.getFasSiuIdFascicoloSius() != null
					&& aDepDec.getFlagRecuperoSS() != null && aDepDec.getFlagRecuperoSS().equals("S")) // Al
																										// momento
																										// il
																										// Flag
																										// Recupero
			{ // non è utilizzato per
				// ---- Ricerca in Esecuzione Misura Sicurezza con l'ID del Fascicolo SIUS (PADRE) trovato
				// ---- // le Misure di Sicurezza
				lEMSSqlDao = new EsecuzioneMisuraSicurezzaSqlDAO(aConn); // (24/5/2011)
				lEMSSqlDao.ricercaEsecuzioneMisuraSicurezzaByIdFascicolo(lPAMMod.getFasSiuIdFascicoloSius());
				EsecuzioneMisuraSicurezzaModel lEMSMod = (EsecuzioneMisuraSicurezzaModel) lEMSSqlDao
						.getModelByKey();

				if (lEMSMod != null && lEMSMod.getDataTermineAttuale() != null) {
					// Sottrae gli eventuali giorni a DATA_TERMINE_ATTUALE di Esecuzione Misura Sicurezza
					Date lDataTermineAttuale = DateUtils.moveDateTo(lEMSMod.getDataTermineAttuale(),
							Calendar.DAY_OF_MONTH, -(aDepDec.getGiorniRecuperoSS().intValue()));
					lEMSDao = new EsecuzioneMisuraSicurezzaDAO(aConn);

					lEMSDao.setDataTermineAttuale(lDataTermineAttuale);

					lEMSDao.setCodUfficioAggiornamento(aDepDec.getCodUfficioAggiornamento());
					lEMSDao.setCodOperatoreAggiornamento(aDepDec.getCodOperatoreAggiornamento());
					lEMSDao.setDataAggiornamento(aDepDec.getDataAggiornamento());

					lEMSDao.setCondizioneUpdate(lEMSMod.getIdEsecuzioneMisuraSicurezza());
					lEMSDao.update();
					lEMSDao.stop();
				}
			}

			// cancellazione eventuale Periodo Altra Misura collegata
			lPAMDao = new PeriodoAltraMisuraDAO(aConn);
			// Il Periodo Altra Misura è collegato all'evento
			lPAMDao.setCondizioneByEveIdEvento(lIdEvento);
			lPAMDao.delete();

			// Istanzia EventoDAO
			lEveDao = new EventoDAO(aConn);

			// Cancellazione dei riferimenti in tabella Evento al record da cancellato
			EventoModel lEvento = new EventoModel();
			lEvento.setCodOperatoreAggiornamento(aDepDec.getCodOperatoreAggiornamento());
			lEvento.setCodUfficioAggiornamento(aDepDec.getCodUfficioAggiornamento());
			lEvento.setDataAggiornamento(aDepDec.getDataAggiornamento());
			lEvento.setIdEvento(lIdEvento);
			// Cancellazione ai riferimenti tramite EVE_ID_EVENTO E EVE_ID_EVENTO_REVOCA
			lEveDao.updateDAOFromModelForResetRifEve(lEvento);

			// cancellazione Evento collegato al Decreto
			lEveDao.selCondizioneUpdate(lIdEvento);
			lEveDao.delete();
			lEveDao.stop();
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lMotDao);
			cleanup(lDepDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lPreDao);
			cleanup(lPASDao);
			cleanup(lPASSqlDao);
			cleanup(lPAMDao);
			cleanup(lPAMSqlDao);
			cleanup(lESSSqlDao);
			cleanup(lESSDao);
			cleanup(lEMSSqlDao);
			cleanup(lEMSDao);
			cleanup(lRCDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lMisAltDao);
		}
		return;
	}

	/**
	 * Esegue l'inserimento del Emissione Decreto Incompetenza.
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	/*
	 * public DepositoDecretoEventoModel ExInserisciDecretoIncompetenza( GPTenoreModel aGpTenoreModel,
	 * DepositoDecretoEventoModel aDepDecrEveModel ) throws F3BException { Connection lConn = null;
	 *
	 * GeneraleProcedimentoDAO lGenProcDao = null; TenoreDAO lTenoreDao = null; TenoreSqlDAO lTenoreSqlDao =
	 * null; DepositoDecretoDAO lDepDecrDao = null; EventoDAO lEventoDao = null;
	 *
	 * // STUB 20030902 - Rivedere di usare gli oggetti passati come paramteri al metodo. GPTenoreModel
	 * lGPTenoreModel = new GPTenoreModel( aGpTenoreModel ); DepositoDecretoModel lDepDecrModel = new
	 * DepositoDecretoModel( aDepDecrEveModel.getDepositoDecreto() ); EventoModel lEventoModel = new
	 * EventoModel( aDepDecrEveModel.getEvento() ); DepositoDecretoEventoModel lModelRet = null;
	 *
	 * try { lConn = getDBTransaction();
	 *
	 * lGenProcDao = new GeneraleProcedimentoDAO( lConn ); lTenoreDao = new TenoreDAO( lConn ); lTenoreSqlDao
	 * = new TenoreSqlDAO( lConn ); lDepDecrDao = new DepositoDecretoDAO( lConn ); lEventoDao = new EventoDAO(
	 * lConn );
	 *
	 * // Update GeneraleProcedimento. lGenProcDao.setCodOggettoProcedimento(
	 * lGPTenoreModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento() );
	 * lGenProcDao.setDataAggiornamento( lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento()
	 * ); lGenProcDao.setCodUfficioAggiornamento(
	 * lGPTenoreModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento() );
	 * lGenProcDao.setCodOperatoreAggiornamento(
	 * lGPTenoreModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento() );
	 * lGenProcDao.setCondizioneUpdate(
	 * lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() ); lGenProcDao.update();
	 *
	 * // Insert DepositoDecreto. lDepDecrDao.setDAOFromModel( lDepDecrModel ); BigDecimal lIdDepDecr =
	 * lDepDecrDao.insert(); lDepDecrDao.stop();
	 *
	 * // Parte Gestione Tenori. // Delete dei tenori. BigDecimal lIdGenProc =
	 * lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
	 */
	/*
	 * // I Tenori non vengono più cancellati ma chiusi ! Luigi 10-12-2003 // Quindi questa parte non è più
	 * necessaria. lTenoreDao.setCondizioneDelete( lIdGenProc ); lTenoreDao.delete(); lTenoreDao.stop();
	 */
	/*
	 * // I Tenori non vengono più cancellati ma chiusi ! Luigi 10-12-2003 // [FT] - 03/08/2016 - MAC_LOG -
	 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug("Fase di chiusura per il Tenore"); TenoreModel lTenore = new TenoreModel(); //
	 * Valorizzazione dei campi da aggiornare + update
	 * lTenore.setCodOperatoreAggiornamento(lGPTenoreModel.getGeneraleProcedimentoModel
	 * ().getCodOperatoreAggiornamento());
	 * lTenore.setCodUfficioAggiornamento(lGPTenoreModel.getGeneraleProcedimentoModel
	 * ().getCodUfficioAggiornamento());
	 * lTenore.setDataAggiornamento(lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
	 * lTenore.setDataFine(lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
	 * lTenore.setGenPridGeneraleProcedimento
	 * (lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
	 * lTenore.setCodUfficioInserimento(lDepDecrModel.getCodUfficioInserimento());
	 * lTenore.setDescrUfficioInserimento(lDepDecrModel.getDescrUfficioInserimento());
	 * lTenoreDao.setDAOFromModelForUpdateDataFine(lTenore); lTenoreDao.update(); lTenoreDao.stop();
	 *
	 * // Insert dei tenori. TenoreModel[] lTenori = lGPTenoreModel.getTenori(); int lCount = lTenori.length;
	 * for( int x=0; x<lCount; x++ ) { lTenori[x].setGenPridGeneraleProcedimento( lIdGenProc );
	 * lTenori[x].setDepDecIdDepositoDecreto( lIdDepDecr );
	 *
	 * lTenori[x].setCodUfficioInserimento(lDepDecrModel.getCodUfficioInserimento());
	 * lTenori[x].setDescrUfficioInserimento(lDepDecrModel.getDescrUfficioInserimento());
	 *
	 * lTenoreDao.setDAOFromModel( lTenori[x] ); lTenoreDao.insert(); lTenoreDao.stop(); }
	 *
	 * // Select dati dal tenore. //lTenoreSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(lIdGenProc);
	 * lTenoreSqlDao.ricercaTenoriByDecretoOrderByPeso(lIdDepDecr); TenoreModel lTenoreMod = new TenoreModel(
	 * (TenoreModel)lTenoreSqlDao.getModelByKey() );
	 *
	 * // Insert Evento // Imposta COD_MOTIVO e IdTenore, nel model. lEventoModel.setCodMotivo(
	 * lTenoreMod.getCodOggettoTenore() ); lEventoModel.setTenIdTenore(lTenoreMod.getIdTenore() );
	 * lEventoDao.setDAOFromModel( lEventoModel ); BigDecimal lIdEvento = lEventoDao.insert();
	 *
	 * // Effettua update del campo evento_generato lDepDecrDao.setCondizioneUpdate( lIdDepDecr );
	 * lDepDecrDao.setIdEventoGenerato(lIdEvento); lDepDecrDao.update();
	 *
	 * // Prepara il model di ritorno. // STUB : 20030902 - Bisognerebbe aggiungere anche il GPTenoriModel //
	 * per aggiornamento dati in sessione. lModelRet = new DepositoDecretoEventoModel();
	 * lModelRet.getEvento().setIdEvento( lIdEvento ); lModelRet.getDepositoDecreto().setIdDepositoDecreto(
	 * lIdDepDecr );
	 *
	 * commit(lConn); } catch(DAOException daoEx) { rollback(lConn); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo
	 * la variabile di istanza siesLogger al posto di LogF3B.getLogger() siesLogger.debug("DAOException: " +
	 * daoEx); throw new SIUSException("DepositoDecretoController.ExInserisciDecretoIncompetenza: " + daoEx);
	 * } catch(SQLException sqlEx) { rollback(lConn); // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile
	 * di istanza siesLogger al posto di LogF3B.getLogger() siesLogger.debug("SQLException: " + sqlEx); throw
	 * new SIUSException("DepositoDecretoController.ExInserisciDecretoIncompetenza: " + sqlEx); } finally {
	 * cleanup(lGenProcDao); cleanup(lTenoreDao); cleanup(lTenoreSqlDao); cleanup(lDepDecrDao);
	 * cleanup(lEventoDao); cleanup(lConn); }
	 *
	 * return lModelRet; }
	 */
	/**
	 * Esegue l'inserimento del Emissione Decreto Incompetenza.
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public DepositoDecretoEventoModel ExInserisciDecretoIrreperibilità(GPTenoreModel aGpTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel) throws F3BException {

		Connection lConn = null;

		GeneraleProcedimentoDAO lGenProcDao = null;
		TenoreDAO lTenoreDao = null;
		TenoreSqlDAO lTenoreSqlDao = null;
		DepositoDecretoDAO lDepDecrDao = null;
		EventoDAO lEventoDao = null;

		// STUB 20030902 - Rivedere di usare gli oggetti passati come paramteri al metodo.
		GPTenoreModel lGPTenoreModel = new GPTenoreModel(aGpTenoreModel);
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel(aDepDecrEveModel.getDepositoDecreto());
		EventoModel lEventoModel = new EventoModel(aDepDecrEveModel.getEvento());
		DepositoDecretoEventoModel lModelRet = null;

		try {
			lConn = getDBTransaction();

			lGenProcDao = new GeneraleProcedimentoDAO(lConn);
			lTenoreDao = new TenoreDAO(lConn);
			lTenoreSqlDao = new TenoreSqlDAO(lConn);
			lDepDecrDao = new DepositoDecretoDAO(lConn);
			lEventoDao = new EventoDAO(lConn);

			// Update GeneraleProcedimento.
			lGenProcDao.setCodOggettoProcedimento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
			lGenProcDao.setDataAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lGenProcDao.setCodUfficioAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lGenProcDao.setCodOperatoreAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lGenProcDao.setCondizioneUpdate(
					lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lGenProcDao.update();

			// Insert DepositoDecreto.
			lDepDecrDao.setDAOFromModel(lDepDecrModel);
			BigDecimal lIdDepDecr = lDepDecrDao.insert();
			lDepDecrDao.stop();

			// Parte Gestione Tenori.
			BigDecimal lIdGenProc = lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fase di chiusura per il Tenore");
			TenoreModel lTenore = new TenoreModel();
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setCodOperatoreAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setDataFine(lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenore.setCodUfficioInserimento(lDepDecrModel.getCodUfficioInserimento());
			lTenore.setDescrUfficioInserimento(lDepDecrModel.getDescrUfficioInserimento());
			lTenoreDao.setDAOFromModelForUpdateDataFine(lTenore);
			lTenoreDao.update();
			lTenoreDao.stop();

			// Insert dei tenori.
			TenoreModel[] lTenori = lGPTenoreModel.getTenori();
			int lCount = lTenori.length;
			for (int x = 0; x < lCount; x++) {
				lTenori[x].setGenPridGeneraleProcedimento(lIdGenProc);
				lTenori[x].setDepDecIdDepositoDecreto(lIdDepDecr);
				lTenori[x].setCodUfficioInserimento(lDepDecrModel.getCodUfficioInserimento());
				lTenori[x].setDescrUfficioInserimento(lDepDecrModel.getDescrUfficioInserimento());
				lTenoreDao.setDAOFromModel(lTenori[x]);
				lTenoreDao.insert();
				lTenoreDao.stop();
			}

			// Select dati dal tenore.
			// lTenoreSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(lIdGenProc);
			lTenoreSqlDao.ricercaTenoriByDecretoOrderByPeso(lIdDepDecr);
			TenoreModel lTenoreMod = new TenoreModel((TenoreModel) lTenoreSqlDao.getModelByKey());

			// Insert Evento
			// Imposta COD_MOTIVO e IdTenore, nel model.

			lEventoModel.setCodMotivo(lTenoreMod.getCodOggettoTenore());
			lEventoModel.setTenIdTenore(lTenoreMod.getIdTenore());
			lEventoDao.setDAOFromModel(lEventoModel);
			BigDecimal lIdEvento = lEventoDao.insert();

			// Effettua update del campo evento_generato
			/* genny 20/02/2004 */
			lDepDecrDao.setCondizioneUpdate(lIdDepDecr);
			lDepDecrDao.setIdEventoGenerato(lIdEvento);
			lDepDecrDao.update();

			// Prepara il model di ritorno.
			// STUB : 20030902 - Bisognerebbe aggiungere anche il GPTenoriModel
			// per aggiornamento dati in sessione.

			lModelRet = new DepositoDecretoEventoModel();
			lModelRet.getEvento().setIdEvento(lIdEvento);
			lModelRet.getDepositoDecreto().setIdDepositoDecreto(lIdDepDecr);

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ExInserisciDecretoIrreperibilità: " + daoEx);
		} finally {
			cleanup(lGenProcDao);
			cleanup(lTenoreDao);
			cleanup(lTenoreSqlDao);
			cleanup(lDepDecrDao);
			cleanup(lEventoDao);
			cleanup(lConn);
		}

		return lModelRet;
	}

	/**
	 * Esegue l'inserimento del Emissione Decreto.
	 * Description: Funzione per l'inserimento dell'Emissione di un decreto generico.
	 * Le tabelle coinvolte sono:
	 * DEPOSITO_DECRETO : viene inserito il nuovo record decreto;
	 * EVENTO : viene inserito un nuovo record;
	 * TENORE : vengono chiusi i tenori attivi (data_fine) ed inseriti i nuovi tenori;
	 * GENERALE_PROCEDIMENTO : update del contenuto del procemimento.
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @param aCodEsitoEvento
	 *            : se null viene utilizzato il codice del tenore più significativo.
	 * @throws F3BException
	 * @return lModelRet
	 */
	public DepositoDecretoEventoModel ExInserisciDecreto(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, String aCodEsitoEvento) throws F3BException {

		Connection lConn = null;
		DepositoDecretoEventoModel lModelRet = null;

		try {
			lConn = getDBTransaction();
			lModelRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel, aCodEsitoEvento, lConn);
			commit(lConn);
		} catch (F3BException e) {
			rollback(lConn);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lModelRet;
	}

	/**
	 * Metodo che esegue l'inseriemento di un decreto, tale metodo chiama il metodo private ExInserisciDecreto
	 * appartente a questa classe
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @return Model popolato DepositoDecretoEventoModel.
	 * @throws F3BException
	 */
	public DepositoDecretoEventoModel ExInserisciDecreto(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel) throws F3BException {

		Connection lConn = null;
		DepositoDecretoEventoModel lModelRet = null;

		try {
			lConn = getDBTransaction();
			lModelRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel,
					aDepDecrEveModel.getEvento().getCodEsito(), lConn);
			commit(lConn);
		} catch (F3BException e) {
			rollback(lConn);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lModelRet;
	}

	public DepositoDecretoEventoModel ExInserisciDecretoRevocaPermesso(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, LicenzaLibAnticipataModel aLicenzaRevocata)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoEventoModel lModelRet = null;
		LicenzaLibanticipataDAO lLicDao = null;
		controlloDatiRevoca(aDepDecrEveModel);

		try {
			lConn = getDBTransaction();
			// Inserimento Tenori, Decreto, Evento
			lModelRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel, null, lConn);
			// Inserimento Licenza di Revoca
			aLicenzaRevocata.setEveIdEvento(lModelRet.getEvento().getIdEvento());
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lLicDao.setDAOFromModel(aLicenzaRevocata);
			lLicDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"DepositoDecretoController.ExInserisciDecretoRevocaPermesso: Non posso inserire: " + ex);
		} catch (F3BException e) {
			rollback(lConn);
			throw e;
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lLicDao);
			cleanup(lConn);
		}
		return lModelRet;
	}

	/**
	 * 30/08/2007 Inserimento Decreto Licenza o Permesso Transazionale.
	 * Description: Funzione per l'inserimento del decreto di Concessione Licenza/Permesso.
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @param aLicenzaPermessoModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public DepositoDecretoEventoModel ExInserisciDecretoLicenzaoPermesso(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, LicenzaLibAnticipataModel aLicenzaPermessoModel)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoEventoModel lModelRet = null;
		LicenzaLibanticipataDAO lLicDao = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Tenori, Decreto, Evento
			lModelRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel, null, lConn);

			// Inserimento Licenza o Permesso
			aLicenzaPermessoModel.setEveIdEvento(lModelRet.getEvento().getIdEvento());
			lLicDao = new LicenzaLibanticipataDAO(lConn);
			lLicDao.setDAOFromModel(aLicenzaPermessoModel);
			lLicDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("DepositoDecretoController.ExInserisciDecretoLicenzaoPermesso: " + ex);
		} catch (F3BException e) {
			rollback(lConn);
			throw e;
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lLicDao);
			cleanup(lConn);
		}
		return lModelRet;
	}

	/**
	 * Description: Funzione per l'inserimento del decreto di Procedimento di Sospensione e Periodo Altra
	 * Sanzione.
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @param aPeriodoAltraSanzioneModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public DepositoDecretoEventoModel ExInserisciDecretoPeriodoAltraSanzione(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, PeriodoAltraSanzioneModel aPeriodoAltraSanzioneModel,
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutivaModel) throws F3BException {

		Connection lConn = null;
		DepositoDecretoEventoModel lModelRet = null;
		PeriodoAltraSanzioneDAO lPasDao = null;
		EsecuzioneSanzioneSostitutivaDAO lEssDAo = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Tenori, Decreto, Evento
			lModelRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel, null, lConn);

			if (aPeriodoAltraSanzioneModel != null) {
				// Inserimento Periodo Altra Sanzione
				aPeriodoAltraSanzioneModel.setEveIdEvento(lModelRet.getEvento().getIdEvento());
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
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("DepositoDecretoController.ExInserisciDecretoPeriodoAltraSanzione: " + ex);
		} catch (F3BException e) {
			rollback(lConn);
			throw e;
		} finally {
			cleanup(lEssDAo);
			cleanup(lPasDao);
			cleanup(lConn);
		}
		return lModelRet;
	}

	// Funzione per il controllo della congruenza dei dati del decreto di revoca/scomputo
	// con quelli del decreto da revocare.
	private void controlloDatiRevoca(DepositoDecretoEventoModel aDepDecrEveModel) throws F3BException {

		if (aDepDecrEveModel != null && aDepDecrEveModel.getEvento() != null
				&& aDepDecrEveModel.getDepositoDecreto() != null) {
			DepositoDecretoFascicoloModel lDepDecMod = ExRicercaDecretoFascicoloByIdEvento(
					aDepDecrEveModel.getEvento().getEveIdEvento());

			if (lDepDecMod != null && lDepDecMod.getDepositoDecreto() != null) {
				// La data di emissione del decreto di revoca deve essere successiva
				// di quella del decreto da revocare.
				Date lData1 = aDepDecrEveModel.getDepositoDecreto().getDataEmissione();
				Date lData2 = lDepDecMod.getDepositoDecreto().getDataEmissione();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data emissione decreto di revoca -> " + lData1);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Data emissione decreto da revocare  -> " + lData2);

				if (lData1.compareTo(lData2) < 0)
					throw new F3BException(F3BException.USER_MESSAGE,
							"La data di emissione del decreto di revoca non può precedere quella del decreto da revocare!");

				// STUB: bisogna inserire qui anche il confronto tra giorni di licenze
				// e giorni revocati/scomputati
			}
		}
	}

	private DepositoDecretoEventoModel ExInserisciDecreto(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, String aCodEsitoEvento, Connection aConn)
			throws F3BException {

		GeneraleProcedimentoDAO lGenProcDao = null;
		TenoreDAO lTenoreDao = null;
		TenoreSqlDAO lTenoreSqlDao = null;
		DepositoDecretoDAO lDepDecrDao = null;
		EventoDAO lEventoDao = null;

		// STUB 20030902 - Rivedere di usare gli oggetti passati come paramteri al metodo.
		GPTenoreModel lGPTenoreModel = new GPTenoreModel(aGPTenoreModel);
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel(aDepDecrEveModel.getDepositoDecreto());
		EventoModel lEventoModel = new EventoModel(aDepDecrEveModel.getEvento());
		DepositoDecretoEventoModel lModelRet = null;

		try {

			lGenProcDao = new GeneraleProcedimentoDAO(aConn);
			lTenoreDao = new TenoreDAO(aConn);
			lTenoreSqlDao = new TenoreSqlDAO(aConn);
			lDepDecrDao = new DepositoDecretoDAO(aConn);
			lEventoDao = new EventoDAO(aConn);

			// Update GeneraleProcedimento.
			lGenProcDao.setCodOggettoProcedimento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
			lGenProcDao.setDataAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lGenProcDao.setCodUfficioAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lGenProcDao.setCodOperatoreAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lGenProcDao.setCondizioneUpdate(
					lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lGenProcDao.update();

			// Insert DepositoDecreto.
			lDepDecrDao.setDAOFromModel(lDepDecrModel);
			BigDecimal lIdDepDecr = lDepDecrDao.insert();
			lDepDecrDao.stop();
			lDepDecrModel.setIdDepositoDecreto(lIdDepDecr);

			// Parte Gestione Tenori.
			BigDecimal lIdGenProc = lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

			// I Tenori non vengono più cancellati ma chiusi ! Luigi 10-12-2003
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fase di chiusura per il Tenore");
			TenoreModel lTenore = new TenoreModel();
			// Valorizzazione dei campi da aggiornare + update
			lTenore.setCodOperatoreAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodOperatoreAggiornamento());
			lTenore.setCodUfficioAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getCodUfficioAggiornamento());
			lTenore.setDataAggiornamento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setDataFine(lGPTenoreModel.getGeneraleProcedimentoModel().getDataAggiornamento());
			lTenore.setGenPridGeneraleProcedimento(
					lGPTenoreModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenoreDao.setDAOFromModelForUpdateDataFine(lTenore);
			lTenoreDao.update();
			lTenoreDao.stop();

			// Insert dei tenori.
			TenoreModel[] lTenori = lGPTenoreModel.getTenori();
			int lCount = lTenori.length;
			for (int x = 0; x < lCount; x++) {
				lTenori[x].setGenPridGeneraleProcedimento(lIdGenProc);
				lTenori[x].setDepDecIdDepositoDecreto(lIdDepDecr);

				// Imposta la data con quella dell'emissione contenuta
				// nell'evento.
				lTenori[x].setData(lEventoModel.getDataEmissione());

				lTenoreDao.setDAOFromModel(lTenori[x]);
				lTenoreDao.insert();
				lTenoreDao.stop();
			}

			// Select dati dal tenore.
			// lTenoreSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(lIdGenProc);
			lTenoreSqlDao.ricercaTenoriByDecretoOrderByPeso(lIdDepDecr);

			TenoreModel lTenoreMod = (TenoreModel) lTenoreSqlDao.getModelByKey();
			if (lTenoreMod == null)
				throw new SIUSException("DepositoDecretoController.ExInserisciDecreto: tenori assenti ");

			// Imposta COD_MOTIVO e IdTenore, nel model dell'Evento
			lEventoModel.setCodMotivo(lTenoreMod.getCodOggettoTenore());
			lEventoModel.setTenIdTenore(lTenoreMod.getIdTenore());
			if (aCodEsitoEvento != null)
				lEventoModel.setCodEsito(aCodEsitoEvento);
			else
				lEventoModel.setCodEsito(lTenoreMod.getCodEsitoTenore());

			// 11/04/2011 Modifica x Visibilità Stato di Esecuzione.
			lEventoModel.setFasSieIdFascicoloSiep(null);

			// Inserimento dell'Evento
			lEventoDao.setDAOFromModel(lEventoModel);
			BigDecimal lIdEvento = lEventoDao.insert();
			lEventoDao.stop();
			lEventoModel.setIdEvento(lIdEvento);

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

			// Effettua update del campo evento_generato nel Decreto appena inserito
			lDepDecrDao.setCondizioneUpdate(lIdDepDecr);
			lDepDecrDao.setIdEventoGenerato(lIdEvento);
			lDepDecrDao.update();
			lDepDecrModel.setIdEventoGenerato(lIdEvento);

			// Prepara il model di ritorno.
			// STUB : 20030902 - Bisognerebbe aggiungere anche il GPTenoriModel
			// per aggiornamento dati in sessione.
			lModelRet = new DepositoDecretoEventoModel();
			// lModelRet.getEvento().setIdEvento( lIdEvento );
			lModelRet.setEvento(lEventoModel);
			lModelRet.setDepositoDecreto(lDepDecrModel);
			// lModelRet.getDepositoDecreto().setIdDepositoDecreto( lIdDepDecr );
			// commit(lConn);
		} catch (DAOException daoEx) {
			// rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ExInserisciDecreto: " + daoEx);
		} catch (Exception e) {
			// rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIUSException("DepositoDecretoController.ExInserisciDecreto:" + e);
		} finally {
			cleanup(lGenProcDao);
			cleanup(lTenoreDao);
			cleanup(lTenoreSqlDao);
			cleanup(lDepDecrDao);
			cleanup(lEventoDao);
			// cleanup(lConn);
		}
		return lModelRet;
	}

	/**
	 * Esegue la stampa decreto.
	 *
	 * @param aEvento
	 *            id evento
	 * @param aFasc
	 *            fascicolo GP Model
	 * @return lByteArrayOut documento generato.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayOutputStream ExStampaDecreto(FascicoloGPModel aFasc, EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			// EventoNotificaModel lEveMod =
			// this.ExRicercaEventoNotificaByKey(aEvento.getEvento().getIdEvento());
			aEvento.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());

			TreeModel lTree = this.prelevaDatiDecreto(aEvento, aFasc);

			// ReportGenerator lReport = new ReportGenerator();
			ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Chiave = " + aEvento.getNomeTemplate());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("NOME TEMPLATE >>>" + lNomeTemplate);
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("EVENTO >>> " + aEvento.getEvento().toString());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);

			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (F3BException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("F3BException: " + ex);
			throw ex;
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new SIUSException("DepositoDecretoController.ExStampaDecreto: " + daoex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Preleva i dati del decreto.
	 *
	 * @param aEvento
	 *            Evento model.
	 * @param lFasModel
	 *            fascicolo sius model.
	 * @return Gerarchia di model.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	private TreeModel prelevaDatiDecreto(EventoNotificaModel aEvento, FascicoloGPModel lFasModel)
			throws F3BException {

		TreeModel lTree = null;
		DepositoDecretoSqlDAO lDecrDao = null;
		MotivazioneDecretoSqlDAO lMotDecrDao = null;
		TenoreSqlDAO lTenDao = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;

		Connection lConn = null;
		BigDecimal lKeyFascicolo = new BigDecimal(0);

		try {
			// Preleva i dati di SIEP.
			IStampa lStampa = SICOLookupRemote.getStampaRemote();

			// STUB 20030926 : Patch Temporanea, quando non esiste un
			// fascicolo SIEP per un fascicolo SIUS evita di tirare giù tutti dati di
			// SIEP. per tanto crea solo la documentRoot.
			if (aEvento.getEvento().getFasSieIdFascicoloSiep() != null)
				lTree = lStampa.prelevaDatiEventoSiep(aEvento);
			else
				lTree = new TreeModel(this.createRoot(aEvento));

			lKeyFascicolo = aEvento.getEvento().getFasSiuIdFascicoloSius();

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("***** Presi Eventi *****");
			TreeModel lTreeFasMod = new TreeModel(lFasModel.getFascicoloSiusModel());
			TreeModel lTreeGenMod = new TreeModel(lFasModel.getGeneraleProcedimentoModel());

			lConn = getDBConnection(); // Preleva Connessione dal DB.

			// Soggetto per SIUS.
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasModel.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			TreeModel lTreeSogMod = new TreeModel(lSogModel);

			lTreeFasMod.add(lTreeSogMod); // STUB 18/10/2004

			// Residenza per SIUS.
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzaByFascicoloSius(lKeyFascicolo);
			ResidenzaModel lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();

			if (lResMod != null) {
				TreeModel lTreeResMod = new TreeModel(lResMod);
				lTreeSogMod.add(lTreeResMod);
			}

			// Deposito Decreto.
			lDecrDao = new DepositoDecretoSqlDAO(lConn);
			lDecrDao.ricercaDepositoDecretoByIdEveGenerato(aEvento.getEvento().getIdEvento());
			// lDecrDao.ricercaDepositoDecretoByIdGenProc(lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("######Preleva dati Dep Decreto : " );
			DepositoDecretoModel lDecrMod = (DepositoDecretoModel) lDecrDao.getModelByKey();

			// Cerca eventuali Ufficio competente e Procura Esecuzione
			if (lDecrMod != null) {
			}

			// Preleva Motivi decreto per Inammissibilità.
			if (lDecrMod != null) {
				TreeModel lTreeDecrMod = new TreeModel(lDecrMod);
				lDecrMod = RicercaUffici(lDecrMod);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("cerco l'ufficio");
				if (lDecrMod.getUfficioCompetente() != null) {
					TreeModel lTreeUffComp = new TreeModel(lDecrMod.getUfficioCompetente());
					lTreeDecrMod.add(lTreeUffComp);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("ho trovato l'ufficio");
				}

				// Motivazione decreto.
				lMotDecrDao = new MotivazioneDecretoSqlDAO(lConn);
				lMotDecrDao.ricercaMotivazioneDecretoInammissibilitaByDepDec(lDecrMod.getIdDepositoDecreto());
				Vector lMotivazioni = new Vector(lMotDecrDao.getModels());
				Iterator lItx = lMotivazioni.iterator();
				TreeModel lTreeMotDecrMod = null;

				while (lItx.hasNext()) {
					lTreeMotDecrMod = new TreeModel((MotivazioneDecretoModel) lItx.next());
					lTreeDecrMod.add(lTreeMotDecrMod);
				}
				lTreeFasMod.add(lTreeDecrMod);
			}

			// Luogo detenzione.
			lLuoDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDao.ricercaLuogoDetenzioneCorrenteByFascicoloSius(
					lFasModel.getFascicoloSiusModel().getIdFascicoloSius());
			LuogoDetenzioneModel lLuoMod = (LuogoDetenzioneModel) lLuoDao.getModelByKey();
			lTreeFasMod.add(new TreeModel(lLuoMod));

			// Inseriti avvocati e magistrato legati al fascicolo
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			// Riempi l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSius.TREE_AVVOCATO, ICostantiStampaSius.TREE_MAGISTRATO,
					ICostantiStampaSius.TREE_TIT_ESE_REF, // STUB 19/01/2005
					ICostantiStampaSius.TREE_RIF_FAS_SIEP }; // STUB 14/10/2004
			// Crea il TreeModel con i dati che occorrono
			lTreeFasMod = lCtrlSta.ExAggiungiDatiStampa(
					lFasModel.getFascicoloSiusModel().getIdFascicoloSius(), aTipoDati, lTreeFasMod);

			// Tenori sortati per peso.
			lTenDao = new TenoreSqlDAO(lConn);
			// lTenDao.ricercaTenoriByGeneraleProcOrderByPeso(
			// lFasModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() );
			lTenDao.ricercaTenoriByDecretoOrderByPesoNoFine(lDecrMod.getIdDepositoDecreto());

			Vector lTenori = new Vector(lTenDao.getModels());
			Iterator lItxTen = lTenori.iterator();
			TreeModel lTreeTenMod = null;

			while (lItxTen.hasNext()) {
				lTreeTenMod = new TreeModel((TenoreModel) lItxTen.next());
				lTreeFasMod.add(lTreeTenMod);
			}
			lTree.add(lTreeSogMod);
			lTree.add(lTreeFasMod);
			lTree.add(lTreeGenMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("DepositoDecretoController.prelevaDatiDecreto: " + daoEx);
		} finally {
			cleanup(lDecrDao);
			cleanup(lMotDecrDao);
			cleanup(lTenDao);
			cleanup(lLuoDao);
			cleanup(lSogSqlDao);
			cleanup(lResSqlDao);
			cleanup(lConn);
		}
		return lTree;
	}

	public DepositoDecretoModel ExRicercaDepositoDecretoByGenProc(BigDecimal aGenProcKey, String aCodTipoDec)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		DepositoDecretoModel lDepMod;

		try {
			lConn = getDBConnection();
			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.ricercaDepositoDecretoByIdGenProc(aGenProcKey, aCodTipoDec);
			lDepMod = (DepositoDecretoModel) lDepDao.getModelByKey();

			// Cerca eventuali Ufficio competente e Procura Esecuzione
			if (lDepMod != null)
				lDepMod = RicercaUffici(lDepMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("DepositoDecretoController.ExRicercaDepositoDecretoByGenProc: " + daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}
		return lDepMod;
	}

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
	public DocumentoAllegatoModel ExInserisciDataDepositoDecreto(FascicoloGPModel aFasGPMod,
			DepositoDecretoModel aDepositoDecreto, EventoNotificaModel aEveNot, String[] lCheck,
			ScadenzarioSiusModel lScadenzarioSiusModPrincipal, ScadenzarioSiusModel lScadenzarioSiusModSecond)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		DepositoDecretoDAO lDecDao = null;
		DepositoDecretoSqlDAO lDecDaoSql = null;
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
		DecodificheDAO lDecodDao = null; // 08/04/2011
		LicenzaLibanticipataDAO lLicLibDAO = null;
		RichiestaConversioneDAO lRicConvDao = null; // 25/11/2015

		DocumentoAllegatoModel lDocAMod = null;
		DepositoDecretoModel lDecMod = new DepositoDecretoModel(aDepositoDecreto);

		// DepositoDecretoEventoModel lDecEveModel = new DepositoDecretoEventoModel();
		try {
			// Update di DepositoDecreto.
			lConn = getDBTransaction();
			lDecDao = new DepositoDecretoDAO(lConn);

			// Il campo Num_S72 viene valorizzato con l'ultimo valore presente + 1
			// Trovo il valore da assegnare al progressivo NUM_S72.
			if (lDecMod.getNumS72() == null) {
				// Si valorizza l'Anno Corrente perchè il progressivo è riferito all'anno
				lDecMod.setAnnoS72(new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));

				lDecDaoSql = new DepositoDecretoSqlDAO(lConn);
				lDecDaoSql.getProgressivoS72(lDecMod);

				lDecDaoSql.start();

				BigDecimal lBigDec = new BigDecimal(0);
				if (lDecDaoSql.next() && (lDecDaoSql.getBigDecimal("aMAX") != null))
					lBigDec = lDecDaoSql.getBigDecimal("aMAX");
				lDecDaoSql.stop();

				if (lBigDec == null)
					lBigDec = new BigDecimal(0);

				// Setto il NumS72 del Model di DepositoDecreto con il MAX + 1
				lDecMod.setNumS72(new BigDecimal(lBigDec.intValue() + 1));

				// Aggiornamento ANNO e PROGR solo al primo inserimento
				lDecDao.setAnnoS72(lDecMod.getAnnoS72());
				lDecDao.setNumS72(lDecMod.getNumS72());

			}
			// Setto il DAO dal Model di DepositoDecreto per l'Update

			// Effettuo l'inserimento data deposito in DepositoDecretoModel; carico i dati da aggiornare.
			lDecDao.setCodUfficioAggiornamento(lDecMod.getCodUfficioAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(lDecMod.getCodOperatoreAggiornamento());
			lDecDao.setDataAggiornamento(lDecMod.getDataAggiornamento());
			lDecDao.setDataDeposito(lDecMod.getDataDeposito());
			lDecDao.setCondizioneUpdate(lDecMod.getIdDepositoDecreto());

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Fase di aggiornamento per DepositoDecreto (Data Deposito)");
			lDecDao.update();

			// Update di Evento.
			EventoNotificaModel lEveNot = new EventoNotificaModel(aEveNot);
			EventoModel lEveMod = new EventoModel(lEveNot.getEvento());
			// STUB: Se poi bisogna trasferire l'evento tocca settare i flag x SIEP

			// 11/04/2011 Modifica x Visibilità Stato di Esecuzione.
			// lEveMod.setFlagVideoSiep("S");
			// lEveMod.setFlagStampaSiep("S");
			lEveMod.setFlagVideoSiep("");
			lEveMod.setFlagStampaSiep("");
			DecodificheModel lDecodifiche = new DecodificheModel();
			if (aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				lDecodDao = new DecodificheDAO(lConn);
				lDecodDao.setCondizioneContestoRwLowValue("OGGETTO_PROCEDIMENTO",
						aFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento());
				lDecodifiche = (DecodificheModel) lDecodDao.getModelByKey();
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

			// 25/07/2014 DL 146/2013 - Revoca L.A. e DL 92 2014 - Violazione CEDU
			if (lDecMod.getCodTipoDecreto().compareTo("43") == 0
					|| lDecMod.getCodTipoDecreto().compareTo("VC") == 0) {
				lLicLibDAO = new LicenzaLibanticipataDAO(lConn);
				// Update LicenzaLibAnticipata va aggiornata per FAS_SIE_ID_FASCICOLO_SIEP.
				lLicLibDAO.setDAOFromDecretoForUpdate(lDecMod,
						aFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
				lLicLibDAO.update();
			}
			// End DL 146/2013

			// Fase di Insert del Documento Allegato.

			// Occorre cancellare eventuali DocumentiAllegati preesistenti
			lDocAllDao = new DocumentoAllegatoDAO(lConn);
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "03");
			lDocAllDao.delete();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Cancellati doc allegati collegati a Evento "+lEveMod.getIdEvento());

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
			lDocAMod.setCodTipoDocumento("03"); // Codifica di COD_TIPO_DOCUMENTO_ALLEGATO = Deposito Decreto
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lDecMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lDecMod.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_DE_500");
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
			}

			// Stub 14/03/2005. Se il procedimento fa riferimento a un fascicolo SIEP,
			// e l'evento trattato è relativo alla concessione/rigetto/revoca di Misura Alternativa,
			// occorre inserire un record nella tabella MISURA_ALTERNATIVA.
			lFasDao = new FascicoloGPSqlDAO(lConn);
			lMASqlDao = new MisuraAlternativaSqlDAO(lConn);
			lMADao = new MisuraAlternativaDAO(lConn);
			lCSSADao = new CSSASqlDAO(lConn);

			boolean lExistFasSiep = lFasDao
					.existFasSiep(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			// boolean lGiaEsisteMA = lMADao.esisteMisuraAlternativaPerEvento(lEveMod.getIdEvento());

			if (lExistFasSiep && (lEveMod.getCodEsito() != null &&
			// STUB 24-02-2006 La Misura Alternativa si iscrive se il Deposito non riguarda la
			// Fissazione Udienza.
					(lEveMod.getCodEsito().compareTo("0601") != 0
							&& lEveMod.getCodEsito().compareTo("0602") != 0))
					&& !(lMADao.esisteMisuraAlternativaPerEvento(lEveMod.getIdEvento()))
					&& lMASqlDao.eventoCoRiRe(lEveMod.getIdEvento())) {
				MisuraAlternativaModel lMAModel = new MisuraAlternativaModel();
				lMAModel.setCodTipoDecisione("02");
				lMAModel.setCodNaturaDecisione(lMASqlDao.ricecaNaturaDecisione(lEveMod.getIdEvento()));
				if (lEveMod.getCodEsito().compareTo("0002") == 0)
					lMAModel.setCodTipoMisura("9000");
				else if (lEveMod.getCodEsito().compareTo("0003") == 0)
					lMAModel.setCodTipoMisura("9001");
				else
					lMAModel.setCodTipoMisura(lEveMod.getCodMotivo());
				lMAModel.setDataDecisione(lDecMod.getDataEmissione());
				lMAModel.setCodMagistrato(lDecMod.getCodMagistrato());
				lMAModel.setCodUfficioSorveglianza(lDecMod.getCodUfficioCompetente());
				lMAModel.setDescrLuogoProva(lDecMod.getLuogoSvolgimentoProva());
				// Da Gestire con la fase di sottoscrizione degli obblighi
				lMAModel.setDataInizioMisura(null);
				lMAModel.setChiaveAnnoFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveAnno());
				lMAModel.setChiaveUfficioFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveUfficio());
				lMAModel.setChiaveProgrFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveProgr());
				lMAModel.setAnnoRegistro(lDecMod.getAnnoS72());
				lMAModel.setNumeroRegistro(lDecMod.getNumS72());
				lMAModel.setFasSieIdFascicoloSiep(lEveMod.getFasSieIdFascicoloSiep());
				lMAModel.setEveIdEvento(lEveMod.getIdEvento());
				lMAModel.setCodTipoUfficioScarcerazione("SORV");
				lMAModel.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
				lMAModel.setCodOperatoreInserimento(lDecMod.getCodOperatoreAggiornamento());
				lMAModel.setDataInserimento(lDecMod.getDataAggiornamento());
				lMADao.setDAOFromModel(lMAModel);

				/* BigDecimal lKeyMA = */lMADao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserita Misura Alternativa per evento -> " + lEveMod.getIdEvento());
			}

			// 25-11-2014 In caso di Deposito Decreto di Conversione P.P.:
			// a) Si Inserisce opportunamente una occorrenza di SCAMBIO_SANZIONE.
			// b) Si aggiorna RICHIESTA_CONVERSIONE inserendo la Data Deposito.

			// Inserimento Scambio Sanzione.
			lScambioSanzioneSqlDao = new ScambioSanzioneSqlDAO(lConn);
			lScambioSanzioneDao = new ScambioSanzioneDAO(lConn);

			if (lExistFasSiep && (!lScambioSanzioneDao.esisteScambioSanzionePerEvento(lEveMod.getIdEvento()))
					&& lScambioSanzioneSqlDao.eventoSS(lEveMod.getIdEvento())) {
				ScambioSanzioneModel aScaSanMod = new ScambioSanzioneModel();
				aScaSanMod.setEveIdEvento(lEveMod.getIdEvento());
				aScaSanMod.setCodTipoDecisione(lEveMod.getCodTipoProvvedimento());
				aScaSanMod.setCodTipoSanzione(lEveMod.getCodMotivo());
				aScaSanMod.setCodNaturaSanzione(lEveMod.getCodEsito());
				aScaSanMod.setAnnoRegistro(lDecMod.getAnnoS72());
				aScaSanMod.setNumeroRegistro(lDecMod.getNumS72());
				aScaSanMod.setChiaveAnnoFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveAnno());
				aScaSanMod.setChiaveProgrFascicoloSius(aFasGPMod.getFascicoloSiusModel().getChiaveProgr());
				aScaSanMod.setCodUfficioSorveglianza(aFasGPMod.getFascicoloSiusModel().getChiaveUfficio());
				aScaSanMod.setCodUfficioEmittente(lEveMod.getCodUfficioEmittente());
				aScaSanMod.setCodOperatoreInserimento(lDecMod.getCodOperatoreAggiornamento());
				aScaSanMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
				aScaSanMod.setDataInserimento(lDecMod.getDataAggiornamento());
				aScaSanMod.setDataEmissione(lEveMod.getDataEmissione());

				aScaSanMod.setFasSieIdFascicoloSiep(lEveMod.getFasSieIdFascicoloSiep());

				lScambioSanzioneDao.setDAOFromModel(aScaSanMod);
				lScambioSanzioneDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Inserita Scambio Sanzione x  Deposito Decreto Conv. Pene Pecuniarie "
						+ lDecMod.getIdDepositoDecreto());

				lRicConvDao = new RichiestaConversioneDAO(lConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setFasSiuIdFascicoloSius(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lRCModel.setCodOperatoreAggiornamento(lDecMod.getCodOperatoreAggiornamento());
				lRCModel.setCodUfficioAggiornamento(lDecMod.getCodUfficioAggiornamento());
				lRCModel.setDataAggiornamento(lDecMod.getDataAggiornamento());
				lRCModel.setDataDeposito(lDecMod.getDataDeposito());
				lRCModel.setEveIdEvento(lEveMod.getIdEvento()); // 26/11/2014 La Richiesta Conversione va
																// collegata all'Evento del Decreto SIUS.
				lRicConvDao.setDAOFromModelForDepDecretoCPP(lRCModel);
				lRicConvDao.selCondizioneByIdFasSius(aFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lRicConvDao.update();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>> Aggiornate le Richieste Conv. Pene Pec. x  Deposito Decreto "
						+ lDecMod.getIdDepositoDecreto());
			}

			// Aggiorno il fascicolo con stato_fascicolo = 07
			lFasSiusDao = new FascicoloSiusDAO(lConn);
			lFasSiusDao.setDAOFromModelForUpdate(aFasGPMod.getFascicoloSiusModel());
			// STUB 02-02-2006 Lo stato del fascicolo cambia se il Deposito non riguarda la Fissazione
			// Udienza.
			if (lEveMod.getCodEsito().compareTo("0601") != 0
					&& lEveMod.getCodEsito().compareTo("0602") != 0) {
				lFasSiusDao.setCodStatoFascicolo("07");
				lFasSiusDao.update();
				lFasSiusDao.stop();
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
		}

		catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("DepositoDecertoController.ExInserisciDataDepositoDecreto: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("DepositoDecretoController.ExInserisciDataDepositoDecreto: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
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
			cleanup(lFasSiusDao); // sca
			cleanup(lScadSiusDao); // sca
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lScambioSanzioneDao);
			cleanup(lScambioSanzioneSqlDao);
			cleanup(lDecodDao); // 11/04/2011
			cleanup(lLicLibDAO);
			cleanup(lRicConvDao);

			cleanup(lConn);
		}
		// Restituito il model del documento allegato inserito.
		return lDocAMod;
	}

	/**
	 * STUB: 20030926 - Temporanea per problema riferito al fascicolo siep inestitente a fronte di un fasciclo
	 * SIUS.
	 * Crea la root del Documento
	 *
	 * @param aEveModel
	 * @return lStampa
	 */
	private XModel createRoot(EventoNotificaModel aEveModel) {

		XModel lStampa = new XModel();
		String descrTipoUff = aEveModel.getEvento().getDescrUfficioEmittente().toUpperCase();
		lStampa.setUfficio(aEveModel.getEvento().getDescrLuogoEmittente().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" UFFICIO >>>  " + descrTipoUff);

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
			// STUB 01/02/2005 Patch x Valorizzare TipoUfficioT1.
			else
				lStampa.setTipoUfficioT1(descrTipoUff);
		}
		return lStampa;
	}

	/**
	 * STUB: 20031014 - Recupero dei destinatari con impipamento dei dati nel formato TIPO DESTINATARIO | SEDE
	 * | COD_UFFICIO .
	 *
	 * @param aIdDepositoDecreto
	 * @return lStampa
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public String getDestinatariDeposito(BigDecimal aIdDepositoDecreto) throws F3BException {

		Connection lConn = null;

		String lSedi = new String("");
		DepositoDecretoSqlDAO lDecDao = null;
		try {
			lConn = getDBConnection();
			lDecDao = new DepositoDecretoSqlDAO(lConn);
			lDecDao.ricercaDepositoDecretoByKey(aIdDepositoDecreto);

			DepositoDecretoModel lDecMod = (DepositoDecretoModel) lDecDao.getModelByKey();

			// In caso di Decreto con Data Deposito valorizzata, è possibile caricare i destinatari.
			if (lDecMod.getDataDeposito() != null) {
				// Preleva i destinatari del decreto.
				INotifica lNotCtrl = SIEPLookupRemote.getNotificaRemote();
				/* Vector lNotifica = */lNotCtrl
						.ExRicercaEstesaNotificaByKeyEvento(lDecMod.getIdEventoGenerato());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("DepositoDecretoController.getDestinatariDeposito: " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lSedi;
	}

	/**
	 * Stamapa il documento allegato al Decreto
	 *
	 * @param aIdFascicoloSius
	 * @param aDAMod
	 * @param aCodUff
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
			siesLogger.debug("SQLException: " + sqe);
			throw new F3BException("DepositoDecretoController.ExStampaDocumentoAllegato: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("DepositoDecretoController.ExStampaDocumentoAllegato: " + sqe);
		} finally {
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
	public DepositoDecretoModel ExRicercaDepositoDecretoByEvento(BigDecimal aEveKey) throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDecDao = null;
		DepositoDecretoModel lDecMod;

		try {
			lConn = getDBConnection();
			lDecDao = new DepositoDecretoSqlDAO(lConn);
			lDecDao.ricercaDepositoDecretoByIdEveGenerato(aEveKey);
			lDecMod = (DepositoDecretoModel) lDecDao.getModelByKey();

			// Cerca eventuali Ufficio competente e Procura Esecuzione
			if (lDecMod != null)
				lDecMod = RicercaUffici(lDecMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("DepositoDecretoController.ExRicercaDepositoDecretoByEvento : " + daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lDecMod;
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
	public DepositoDecretoModel ExRicercaDepositoDecretoByEveIdEventoNoDescTipoDecreto(BigDecimal aEveKey)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDecDao = null;
		DepositoDecretoModel lDecMod;

		try {
			lConn = getDBConnection();
			lDecDao = new DepositoDecretoSqlDAO(lConn);

			lDecDao.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(aEveKey);
			lDecDao.start();
			lDecDao.next();
			lDecMod = (DepositoDecretoModel) lDecDao.getModelNoDescTipoDecreto();
			lDecDao.stop();

			// Cerca eventuali Ufficio competente e Procura Esecuzione
			if (lDecMod != null)
				lDecMod = RicercaUffici(lDecMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoDecretoController.ExRicercaDepositoDecretoByEveIdEventoNoDescTipoDecreto : "
							+ daoEx);
		} finally {
			cleanup(lDecDao);
			cleanup(lConn);
		}
		return lDecMod;
	}

	private DepositoDecretoModel RicercaUffici(DepositoDecretoModel aDepDecreto) throws F3BException {

		IUfficio lUff = null; // Interfaccia al Controller Ufficio
		String lCodUffComp = aDepDecreto.getCodUfficioCompetente();
		String lCodProcEsec = aDepDecreto.getCodProcuraEsecuzione();
		String lCodUff = null;
		lUff = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficio = null;

		// Preleva Ufficio Competente
		if (lCodUffComp != null && lCodUffComp.compareTo("-") != 0) {
			lUfficio = lUff.getUfficioByKey(lCodUffComp);
			if (lUfficio != null) {
				aDepDecreto.setUfficioCompetente(lUfficio);
				// aDepDecreto.setDescrUfficioCompetente(lUfficio.getDescrTipoUfficio() + " di " +
				// lUfficio.getDescrComune());
				aDepDecreto.setDescrUfficioCompetente(lUfficio.getDescrComune());
			}
		}
		// Preleva Procura Esecuzione
		if (lCodProcEsec != null && lCodProcEsec.compareTo("-") != 0) {
			lUfficio = lUff.getUfficioByKey(lCodProcEsec);
			if (lUfficio != null) {
				aDepDecreto.setProcuraEsecuzione(lUfficio);
				// aDepDecreto.setDescrProcuraEsecuzione(lUfficio.getDescrTipoUfficio() + " di " +
				// lUfficio.getDescrComune());
				aDepDecreto.setDescrProcuraEsecuzione(lUfficio.getDescrComune());
			}
		}
		// Tribunale di Sorveglianza Competente
		lCodUff = aDepDecreto.getCodTdsComp();
		if (lCodUff != null && lCodUff.compareTo("-") != 0) {
			lUfficio = lUff.getUfficioByKey(lCodUff);
			if (lUfficio != null)
				// aDepDecreto.setDescrTdsComp(lUfficio.getDescrTipoUfficio() + " di " +
				// lUfficio.getDescrComune());
				aDepDecreto.setDescrTdsComp(lUfficio.getDescrComune());
		}
		// Sede di Emissione Procedimento Revocato
		lCodUff = aDepDecreto.getCodProcuraRevocato();
		if (lCodUff != null && lCodUff.compareTo("-") != 0) {
			lUfficio = lUff.getUfficioByKey(lCodUff);
			if (lUfficio != null)
				aDepDecreto.setDescrProcuraRevocato(
						lUfficio.getDescrTipoUfficio() + " di " + lUfficio.getDescrComune());
		}
		// Ufficio di Inserimento
		lCodUff = aDepDecreto.getCodUfficioInserimento();
		if (lCodUff != null && lCodUff.compareTo("-") != 0) {
			lUfficio = lUff.getUfficioByKey(lCodUff);
			if (lUfficio != null)
				aDepDecreto.setDescrUfficioInserimento(
						lUfficio.getDescrTipoUfficio() + " di " + lUfficio.getDescrComune());
		}
		// Ufficio di Aggiornamento
		lCodUff = aDepDecreto.getCodUfficioAggiornamento();
		if (lCodUff != null && lCodUff.compareTo("-") != 0) {
			lUfficio = lUff.getUfficioByKey(lCodUff);
			if (lUfficio != null)
				aDepDecreto.setDescrUfficioAggiornamento(
						lUfficio.getDescrTipoUfficio() + " di " + lUfficio.getDescrComune());
		}

		return aDepDecreto;
	}

	/*
	 * Funzione di prova. Effettua degli inserimenti nella tabella DEPOSITO_DECRETO, quindi genera una
	 * Exception non trattata nella funzione stessa. Viene effettuato il rollback() solo in concomitanza
	 * dell'occorrenza delle Excrepion di tipo: DAOException e SQLException.
	 */
	public void ProvaInsertSenzaCatch() throws F3BException {

		Connection lConn = null;
		BigDecimal lIdDepDecr = null;
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel();
		DepositoDecretoDAO lDepDecrDao = null;

		try {
			// Apertura connessione
			lConn = getDBTransaction();
			lDepDecrDao = new DepositoDecretoDAO(lConn);

			// Inserimento di 2 record nella DEPOSITO DECRETO
			int i = 1;
			for (; i < 3; i++) {
				// valorizzazione di un campo per individuare il record
				lDepDecrModel.setLuogoSvolgimentoProva("record con catch nro. " + i);

				// Insert DepositoDecreto.
				lDepDecrDao.setDAOFromModel(lDepDecrModel);
				lIdDepDecr = lDepDecrDao.insert();
				lDepDecrDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserito record con ID: " + lIdDepDecr);
			}

			// ......
			// Succede qualcosa per cui parte una eccezione
			if (i == 3)
				throw new SIUSException("Qualcosa è andato storto!");

			// Nuovo inserimento
			lDepDecrModel.setLuogoSvolgimentoProva("record senza catch nro. " + i);

			// Insert DepositoDecreto.
			lDepDecrDao.setDAOFromModel(lDepDecrModel);
			lIdDepDecr = lDepDecrDao.insert();
			lDepDecrDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito record con ID: " + lIdDepDecr);
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ProvaInsertSenzaCatch: " + daoEx);
		} finally {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DepositoDecretoController.ProvaInsertSenzaCatch: finally");
			cleanup(lDepDecrDao);
			cleanup(lConn);
		}
		return;
	}

	/*
	 * Funzione di prova. Effettua degli inserimenti nella tabella DEPOSITO_DECRETO, quindi genera una
	 * Exception. A differenza della f.ne precedente qui vengono trattate tutte le possibili Exceptionnon
	 * effettuando il rollback() sull'occorrenza di ognuna di esse.
	 */

	public void ProvaInsertConCatch() throws F3BException {

		Connection lConn = null;
		BigDecimal lIdDepDecr = null;
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel();
		DepositoDecretoDAO lDepDecrDao = null;

		try {
			// Apertura connessione
			lConn = getDBTransaction();
			lDepDecrDao = new DepositoDecretoDAO(lConn);

			// Inserimento di 2 record nella DEPOSITO DECRETO
			int i = 1;
			for (; i < 3; i++) {
				// valorizzazione di un campo per individuare il record
				lDepDecrModel.setLuogoSvolgimentoProva("record senza catch nro. " + i);

				// Insert DepositoDecreto.
				lDepDecrDao.setDAOFromModel(lDepDecrModel);
				lIdDepDecr = lDepDecrDao.insert();
				lDepDecrDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserito record con ID: " + lIdDepDecr);
			}

			// ......
			// Succede qualcosa per cui parte una eccezione
			if (i == 3)
				throw new SIUSException("Qualcosa è andato storto!");

			// Nuovo inserimento
			lDepDecrModel.setLuogoSvolgimentoProva("record senza catch nro. " + i);

			// Insert DepositoDecreto.
			lDepDecrDao.setDAOFromModel(lDepDecrModel);
			lIdDepDecr = lDepDecrDao.insert();
			lDepDecrDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserito record con ID: " + lIdDepDecr);
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ProvaInsertConCatch: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new SIUSException("DepositoDecretoController.ProvaInsertConCatch:" + e);
		} finally {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DepositoDecretoController.ProvaInsertConCatch: finally");
			cleanup(lDepDecrDao);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Esecuzione stampa Emissione Decreto
	 *
	 * @param aModel
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampEmissioneDecreto(EventoModel lEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		// EventoNotificaModel lEveNotifica = null;

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		lByteArrayOut = lCtrlSta.ExPreStampaEmissioneDecreto(lEvento, lUfficio.getCodUfficio(), aUtenteModel);

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

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
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
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException("UdienzaController.ExStampEmissioneDecreto: " + daoex);
		} finally {
			cleanup(lEveDao);
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

		// Generazione documento di stampa
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();

		lByteArrayOut = lCtrlSta.ExPreStampaEmissioneDecreto(lEvento, aCodUff, aUtenteModel);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		Connection lConn = null;
		DocumentoAllegatoDAO lDADao = null;
		DocumentoAllegatoSqlDAO lDASqlDao = null;

		try {
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
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException("DepositoDecretoController.ExStampaFoglioComp: " + daoex);
		} finally {
			cleanup(lDASqlDao);
			cleanup(lDADao);
			cleanup(lConn);
		}

		return lByteArrayOut;
	}

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
	public DocumentoAllegatoModel ExModificaDataDepositoDecreto(FascicoloGPModel aFasGPMod,
			DepositoDecretoModel aDepositoDecreto, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		NotificaDAO lNotDaoCanc = null;
		AutoritaEsternaDAO lAutDao = null;
		DepositoDecretoDAO lDecDao = null;
		DepositoDecretoSqlDAO lDecDaoSql = null;
		AvvocatoFascicoloSiusSqlDAO lAvvDaoSql = null;
		FascicoloGPSqlDAO lFasDao = null;
		MisuraAlternativaSqlDAO lMASqlDao = null;
		MisuraAlternativaDAO lMADao = null;
		CSSASqlDAO lCSSADao = null;
		DocumentoAllegatoDAO lDocAllDao = null;
		DocumentoAllegatoSqlDAO lDocAllSqlDao = null;

		DocumentoAllegatoModel lDocAMod = null;
		DepositoDecretoModel lDecMod = new DepositoDecretoModel(aDepositoDecreto);

		try {
			lConn = getDBTransaction();

			// Setto il DAO dal Model di DepositoDecreto per l'Update
			lDecDao = new DepositoDecretoDAO(lConn);

			// Effettuo l'inserimento data deposito in DepositoDecretoModel; carico i dati da aggiornare.
			lDecDao.setCodUfficioAggiornamento(lDecMod.getCodUfficioAggiornamento());
			lDecDao.setCodOperatoreAggiornamento(lDecMod.getCodOperatoreAggiornamento());
			lDecDao.setDataAggiornamento(lDecMod.getDataAggiornamento());
			lDecDao.setDataDeposito(lDecMod.getDataDeposito());
			lDecDao.setAnnoS72(lDecMod.getAnnoS72());
			lDecDao.setNumS72(lDecMod.getNumS72());
			lDecDao.setCondizioneUpdate(lDecMod.getIdDepositoDecreto());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Fase di aggiornamento per DepositoDecreto (Data Deposito)");
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
			lDocAllDao.setCondizioneDelete(lEveMod.getIdEvento(), "03");
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
			lDocAMod.setCodTipoDocumento("03"); // Codifica di COD_TIPO_DOCUMENTO_ALLEGATO = Deposito Decreto
			lDocAMod.setFlagDocumentoRegistrato("N");
			// lDocAMod.setDocBlobIn();
			lDocAMod.setCodUfficioInserimento(lDecMod.getCodUfficioAggiornamento());
			lDocAMod.setCodOperatoreInserimento(lDecMod.getCodOperatoreAggiornamento());
			lDocAMod.setDataInserimento(lDecMod.getDataAggiornamento());
			lDocAMod.setTemIdTemplate("SIUS_DE_500");
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
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(">>>>>>> Cancellazione Notifiche ");
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
			siesLogger.debug("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("DepositoDecertoController.ExModificaDataDepositoDecreto: " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("DepositoDecretoController.ExModificaDataDepositoDecreto: " + ex);
		} finally {
			cleanup(lDecDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lNotDaoCanc);
			cleanup(lAutDao);
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

	/**
	 * Ricerca DepositoDecretoModel da ANNO, NUM e cod Ufficio.
	 *
	 * @param aDepositoDecreto
	 * @return
	 * @throws F3BException
	 */

	public DepositoDecretoModel ExRicercaDepositoDecretoByAnnoNumUfficio(
			DepositoDecretoModel aDepositoDecreto) throws F3BException {

		Connection lConn = null;
		DepositoDecretoDAO lDepDao = null;
		DepositoDecretoModel lDepMod;
		try {
			// Si effettua una ricerca nella Tabella DEPOSITO_DECRETO
			lConn = getDBConnection();
			lDepDao = new DepositoDecretoDAO(lConn);
			lDepDao.setCondizione(aDepositoDecreto);
			lDepMod = (DepositoDecretoModel) lDepDao.getModelByKey();
			if (lDepMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Decreto non trovato");
			cleanup(lDepDao);
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("Errore nella ricerca del Decreto: " + e);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		return lDepMod;
	}

	/**
	 * Description: Funzione per l'inserimento del decreto di Procedimento di Sospensione e Periodo Altra
	 * Misura
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @param aPeriodoAltraMisuraModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public DepositoDecretoEventoModel ExInserisciDecretoPeriodoAltraMisura(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, PeriodoAltraMisuraModel aPeriodoAltraMisuraModel,
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezzaModel) throws F3BException {

		Connection lConn = null;
		DepositoDecretoEventoModel lModelRet = null;
		PeriodoAltraMisuraDAO lPamDao = null;
		EsecuzioneMisuraSicurezzaDAO lEmsDAo = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Tenori, Decreto, Evento
			lModelRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel, null, lConn);

			if (aPeriodoAltraMisuraModel != null) {
				// Inserimento Periodo Altra Misura
				aPeriodoAltraMisuraModel.setEveIdEvento(lModelRet.getEvento().getIdEvento());
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
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("DepositoDecretoController.ExInserisciDecretoPeriodoAltraMisura: " + ex);
		} catch (F3BException e) {
			rollback(lConn);
			throw e;
		} finally {
			cleanup(lEmsDAo);
			cleanup(lPamDao);
			cleanup(lConn);
		}
		return lModelRet;
	}

	/**
	 * Esegue modifica del magistrato per ordinanza.
	 */
	public DepositoDecretoModel ExModificaMagistratoDecreto(DepositoDecretoModel aDepDecrMod)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoDAO lDepDecrDao = null;
		DepositoDecretoSqlDAO lDepDecrSqlDao = null;
		EventoDAO lEventoDao = null;
		TenoreDAO lTenoreDao = null;

		TenoreModel lTenoreMod = null;

		// modifica il codMagistrato.
		try {
			lConn = getDBConnection();
			// modifica l'evento
			lEventoDao = new EventoDAO(lConn);
			lEventoDao.setIdEvento(aDepDecrMod.getIdEventoGenerato());
			lEventoDao.setCodMagistrato(aDepDecrMod.getCodMagistrato());
			lEventoDao.setDataAggiornamento(new Date());
			lEventoDao.setCodUfficioAggiornamento(aDepDecrMod.getCodUfficioAggiornamento());
			lEventoDao.setCodOperatoreAggiornamento(aDepDecrMod.getCodOperatoreAggiornamento());
			lEventoDao.selByKey();
			lEventoDao.update();

			// legge l'id del deposito decreto per l'id evento genrato.
			lDepDecrSqlDao = new DepositoDecretoSqlDAO(lConn);
			lDepDecrSqlDao.ricercaDepositoDecretoByIdEveGenerato(aDepDecrMod.getIdEventoGenerato());
			aDepDecrMod.setIdDepositoDecreto(
					((DepositoDecretoModel) lDepDecrSqlDao.getModelByKey()).getIdDepositoDecreto());

			// modifica magistrato al decreto.
			lDepDecrDao = new DepositoDecretoDAO(lConn);
			lDepDecrDao.setIdEventoGenerato(aDepDecrMod.getIdEventoGenerato());
			lDepDecrDao.setCodMagistrato(aDepDecrMod.getCodMagistrato());
			lDepDecrDao.setDataAggiornamento(new Date());
			lDepDecrDao.setCodUfficioAggiornamento(aDepDecrMod.getCodUfficioAggiornamento());
			lDepDecrDao.setCodOperatoreAggiornamento(aDepDecrMod.getCodOperatoreAggiornamento());
			lDepDecrDao.setCondizioneUpdate(aDepDecrMod.getIdDepositoDecreto());
			lDepDecrDao.update();

			// modifica dei tenori afferenti.
			lTenoreMod = new TenoreModel();
			lTenoreMod.setDepDecIdDepositoDecreto(aDepDecrMod.getIdDepositoDecreto());
			lTenoreMod.setCodMagistrato(aDepDecrMod.getCodMagistrato());
			lTenoreMod.setCodOperatoreAggiornamento(aDepDecrMod.getCodUfficioAggiornamento());
			lTenoreMod.setDataAggiornamento(aDepDecrMod.getDataAggiornamento());
			lTenoreDao = new TenoreDAO(lConn);
			lTenoreDao.setDAOFromModelForUpdateMagistratoByDecreto(lTenoreMod);
			lTenoreDao.update();

			// commit
			commit(lConn);
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new SIUSException("DepositoDecretoController.ExModificaMagistratoDecreto: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new SIUSException("DepositoDecretoController.ExModificaMagistratoDecreto: " + ex);
		} finally {
			cleanup(lEventoDao);
			cleanup(lDepDecrDao);
			cleanup(lDepDecrSqlDao);
			cleanup(lTenoreDao);
			cleanup(lConn);
		}
		return aDepDecrMod;
	}

	/**
	 * 07/2014 Inserimento Decreto Revoca liberazione Anticipata.
	 * Description: Funzione per l'inserimento del decreto di Revoca liberazione Anticipata ,
	 * Inserimento Decreto Violazione art 3 CEDU,
	 *
	 * @param aGPTenoreModel
	 * @param aDepDecrEveModel
	 * @param aLicenzaPermessoModel
	 * @throws F3BException
	 * @return lModelRet
	 */
	public DepositoDecretoEventoModel ExInserisciDecretoRevocaLiberazAnticipata(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, LicenzaPeriodiLibAnticipataModel[] aLicenze,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_spe, LicenzaPeriodiLibAnticipataModel[] aLicenze_int,
			LicenzaLibAnticipataModel aLicenzaC, LicenzaLibAnticipataModel aLicenzaC_SPE,
			LicenzaLibAnticipataModel aLicenzaC_INT,
			// MEV_AVVOCATURA - aggiunto parametro
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException, Exception {

		Connection lConn = null;
		DepositoDecretoEventoModel lModRet = null;
		// MEV_AVVOCATURA - aggiunta variabile
		AvvisiAvvocatoDAO lAvvisiAvvocatoDao = null;

		try {
			lConn = getDBTransaction();

			// Inserimento Tenori, Decreto, Evento e Update Generale_Procedimento
			lModRet = ExInserisciDecreto(aGPTenoreModel, aDepDecrEveModel, null, lConn);

			// Inserisce LIBERAZIONE_ANTICIPATA E relativi PERIODI
			if (aLicenze != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("--> - DepositoDecretoController - aLicenze != null" + aLicenze);
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
				siesLogger.debug("--> - DepositoDecretoController - aLicenze_spe != null" + aLicenze_spe);
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
				siesLogger.debug("---> - DepositoDecretoController - aLicenze_int != null" + aLicenze_int);
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
				siesLogger
						.debug("---> DepositoDecretoController - aLicenzaC != null no semestri" + aLicenzaC);
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
						"--> DepositoDecretoController - aLicenzaC_SPE != null no semestri" + aLicenzaC_SPE);
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
						"---> DepositoDecretoController - aLicenzaC_INT != null no semestri" + aLicenzaC_INT);
				aLicenzaC_INT.setEveIdEvento(lModRet.getEvento().getIdEvento());
				aLicenzaC_INT.setFasSieIdFascicoloSiep(lModRet.getEvento().getFasSieIdFascicoloSiep());
				aLicenzaC_INT.setDataEmissioneOrdinanza(lModRet.getEvento().getDataEmissione());
				aLicenzaC_INT.setCodLuogoEmittente(lModRet.getEvento().getCodLuogoEmittente());
				aLicenzaC_INT.setCodUfficioEmittente(lModRet.getEvento().getCodUfficioEmittente());

				ILicenzaPeriodiLibAnticipata LicenzaLibAntCtrl = SICOLookupRemote
						.getLicenzaPeriodiLibAntRemote();
				LicenzaLibAntCtrl.ExInserisciLicenzaLibanticipata(aLicenzaC_INT, lConn);
			}

			// inserisco gli avvisi sulla tabella AVVISI_AVVOCATO
			if (lAvvvisiAvvocato != null && lAvvvisiAvvocato.size() > 0) {
				lAvvisiAvvocatoDao = new AvvisiAvvocatoDAO(lConn);
				for (AvvisiAvvocatoModel avvisoAvvocato : lAvvvisiAvvocato) {
					// avvisoAvvocato.setIdProvvedimento(lModRet.getDepositoDecreto().getIdDepositoDecreto());
					// setto idEvento (emma 22/08/2016)
					avvisoAvvocato.setIdEvento(lModRet.getDepositoDecreto().getIdEventoGenerato());
					lAvvisiAvvocatoDao.setDAOFromModel(avvisoAvvocato);
					lAvvisiAvvocatoDao.insert();
					lAvvisiAvvocatoDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"DepositoDecretoController.ExInserisciDecretoRevocaLiberazAnticipata: " + ex);
		} catch (SQLException sqlEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqlEx);
			throw new SIUSException(
					"DepositoDecretoController.ExInserisciDecretoRevocaLiberazAnticipata: Non posso leggere  : "
							+ sqlEx);
		} catch (F3BException e) {
			rollback(lConn);
			// throw e;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: ", e);
			throw new SIUSException(
					"DepositoDecretoController.ExInserisciDecretoRevocaLiberazAnticipata:" + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lAvvisiAvvocatoDao);
			cleanup(lConn);
		}
		return lModRet;
	}

	public Vector ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(BigDecimal idFascicoloSiep)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		Vector lDiffVec = new Vector();
		try {
			lConn = getDBConnection();
			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.RicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(idFascicoloSiep);
			lDepDao.start();
			DecretoEventoTenoriFascicoloSiusModel lDepMod = null;
			while (lDepDao.next()) {
				lDepMod = (DecretoEventoTenoriFascicoloSiusModel) lDepDao.getModelEsitoDiffMisSic();
				lDiffVec.add(lDepMod);
			}
			lDepDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
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

	public DecretoEventoTenoriFascicoloSiusModel ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(
			BigDecimal idFascicoloSiep, BigDecimal idFascSius, BigDecimal idEveFascSius, BigDecimal idEvento)
			throws F3BException {

		Connection lConn = null;
		DepositoDecretoSqlDAO lDepDao = null;
		DecretoEventoTenoriFascicoloSiusModel lDepMod = null;
		try {
			lConn = getDBConnection();
			lDepDao = new DepositoDecretoSqlDAO(lConn);
			lDepDao.RicercaEventoProvvDiffSIUSByFascSiepEFascSius(idFascicoloSiep, idFascSius, idEveFascSius,
					idEvento);
			lDepDao.start();
			if (lDepDao.next())
				lDepMod = (DecretoEventoTenoriFascicoloSiusModel) lDepDao.getModelEsitoDiffMisSic();
			lDepDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException(
					"DepositoDecretoController.ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius: Non posso leggere: "
							+ daoEx);
		} finally {
			cleanup(lDepDao);
			cleanup(lConn);
		}

		// valore di ritorno
		return lDepMod;
	}

}