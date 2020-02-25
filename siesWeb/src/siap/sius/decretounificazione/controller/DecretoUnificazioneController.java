package siap.sius.decretounificazione.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepSqlDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: DecretoUnificazioneController
 * </p>
 * <p>
 * Description: Classe Controller per DecretoUnificazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecretoUnificazioneController extends SiapController implements IDecretoUnificazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Verifica della Unificabilità dei due Procedimenti Individuati.;
	 * 
	 * @param aAnnoDaUnif
	 * @param aNumeroDaUnif
	 * @param aAnnoUnificante
	 * @param aNumeroUnificante
	 * @param aUfficioUtenteConnesso
	 * @return aResponse
	 * @throws F3BException
	 */
	public boolean ExVerificaDecretoUnificazione(String aAnnoDaUnif, String aNumeroDaUnif,
			String aAnnoUnificante, String aNumeroUnificante, String aUfficioUtenteConnesso)
			throws F3BException {

		Connection lConn = null;
		FascicoloGPSqlDAO lFasDao = null;

		boolean aResponse = true;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloGPSqlDAO(lConn);

			// Ricerca per Progressivo/Anno/codUfficio del fascicolo da Unificare.
			if (!lFasDao.existFasSiusUfficio(new BigDecimal(aAnnoDaUnif), new BigDecimal(aNumeroDaUnif),
					aUfficioUtenteConnesso)) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento da Unificare non esistente in archivio");
			}

			// Ricerca per Progressivo/Anno/codUfficio del fascicolo Unificante.
			if (!lFasDao.existFasSiusUfficio(new BigDecimal(aAnnoUnificante), new BigDecimal(
					aNumeroUnificante), aUfficioUtenteConnesso)) {
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento Unificante non esistente in archivio");
			}

			// I 2 fascicoli esistono; occorre verificare se sono compatibili per l'unificazione
			// Verifica Fascicolo da Unificare.
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasDaUnif = new FascicoloGPModel();
			lFasDaUnif = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(new BigDecimal(aAnnoDaUnif),
					new BigDecimal(aNumeroDaUnif), aUfficioUtenteConnesso);

			if (lFasDaUnif.getFascicoloSiusModel().getCodStatoFascicolo().equals("05"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Il Procedimento da Unificare è gia unificato ");

			if (!lFasDaUnif.getFascicoloSiusModel().getCodStatoFascicolo().equals("02"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Il Procedimento da Unificare è gia definito :  Unificazione Impossibile");

			// Verifica Fascicolo Unificante.
			FascicoloGPModel lFasUnificante = new FascicoloGPModel();
			lFasUnificante = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(new BigDecimal(aAnnoUnificante),
					new BigDecimal(aNumeroUnificante), aUfficioUtenteConnesso);

			if (lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("05"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Il Procedimento Unificante è gia unificato");

			// 26/03/2007 Se Rinviato a nuovo ruolo, può essere unificato.
			if (!lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("02")
					&& !lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("10"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Il Procedimento Unificante è gia archiviato :  Unificazione Impossibile");

		}

		catch (SIUSException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("DecretoUnificazioneController.ExVerificaDecretoUnificazione: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("DecretoUnificazioneController.ExVerificaDecretoUnificazione: " + e);

		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return aResponse;
	}

	/**
	 * Verifica del Procedimento da Unificare.
	 * <p>
	 * 
	 * @param aAnnoDaUnif
	 * @param aNumeroDaUnif
	 * @param aUfficioUtenteConnesso
	 * @return aFasDaUnif
	 * @throws F3BException
	 */
	public FascicoloGPModel ExVerificaFascicoloDaUnificare(String aAnnoDaUnif, String aNumeroDaUnif,
			String aUfficioUtenteConnesso) throws F3BException {
		Connection lConn = null;
		FascicoloGPSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;

		FascicoloGPModel lFasDaUnif = new FascicoloGPModel();
		MagistratoRelatoreModel lMagRel = new MagistratoRelatoreModel();

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloGPSqlDAO(lConn);

			// Ricerca per Progressivo/Anno/codUfficio del fascicolo da Unificare.
			if (!lFasDao.existFasSiusUfficio(new BigDecimal(aAnnoDaUnif), new BigDecimal(aNumeroDaUnif),
					aUfficioUtenteConnesso))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento da Unificare non esistente in archivio");

			// Verifica Fascicolo da Unificare.
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasDaUnif = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(new BigDecimal(aAnnoDaUnif),
					new BigDecimal(aNumeroDaUnif), aUfficioUtenteConnesso);

			if (lFasDaUnif.getFascicoloSiusModel().getCodStatoFascicolo().equals("05"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento da Unificare gia unificato ");

			// 26/03/2007 Se Rinviato a nuovo ruolo, può essere unificato.
			// 23/09/2009 Se Sospeso per rimessione atti, può essere unificato.
			if (!lFasDaUnif.getFascicoloSiusModel().getCodStatoFascicolo().equals("02")
					&& !lFasDaUnif.getFascicoloSiusModel().getCodStatoFascicolo().equals("10")
					&& !lFasDaUnif.getFascicoloSiusModel().getCodStatoFascicolo().equals("13"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento da Unificare archiviato :  Unificazione Impossibile");

			// 14.04.2011 Verifica presenza Magistrato Relatore in Fascicolo da Unificare
			IMagistratoRelatore lCtrlM = SIUSLookupRemote.getMagistratoRelatoreRemote();
			lMagRel = lCtrlM.ExRicercaEstesaMagRelByFascicolo(lFasDaUnif.getGeneraleProcedimentoModel()
					.getFasSiuIdFascicoloSius());

			// Modifica del 21/11/2016 MEV_32
			// [EC] 15022018 AMOMALIA ME32 SEGNALATA DA GASBARRI IN TEST PRECOLLAUDO
		    //controllo la presenza dei magistati per entrambi i fascicoli 			
			if (lMagRel != null && lMagRel.getMagistrato() != null) {
				lFasDaUnif.setMagistratoRelatore(lMagRel);				
			}		

			// STUB 17/09/2004.
			if (lFasDaUnif.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento da Unificare di Esecuzione Misure Alternative :  Unificazione Impossibile");
		}

		catch (SIUSException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("DecretoUnificazioneController.ExVerificaFascicoloDaUnificare: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("DecretoUnificazioneController.ExVerificaFascicoloDaUnificare: " + e);

		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lConn);
		}
		return lFasDaUnif;
	}

	/**
	 * Verifica del Procedimento Unificante.
	 * <p>
	 * 
	 * @param aAnnoUnificante
	 * @param aNumeroUnificante
	 * @param aUfficioUtenteConnesso
	 * @return aFasUnificante
	 * @throws F3BException
	 */
	public FascicoloGPModel ExVerificaFascicoloUnificante(String aAnnoUnificante, String aNumeroUnificante,
			String aUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;
		FascicoloGPSqlDAO lFasDao = null;
		SoggettoSqlDAO lSoggDao = null;

		FascicoloGPModel lFasUnificante = new FascicoloGPModel();
		MagistratoRelatoreModel lMagRel = new MagistratoRelatoreModel();

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloGPSqlDAO(lConn);

			// Ricerca per Progressivo/Anno/codUfficio del fascicolo Unificante.
			if (!lFasDao.existFasSiusUfficio(new BigDecimal(aAnnoUnificante), new BigDecimal(
					aNumeroUnificante), aUfficioUtenteConnesso))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento Unificante non esistente in archivio");

			// Verifica Fascicolo Unificante.
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasUnificante = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(new BigDecimal(aAnnoUnificante),
					new BigDecimal(aNumeroUnificante), aUfficioUtenteConnesso);

			if (lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("05"))
				throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento Unificante gia unificato ");

			// 26/03/2007 Se Rinviato a nuovo ruolo, può essere unificato.
			// 23/09/2009 Se Sospeso per rimessione atti, può essere unificato.
			if (!lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("02")
					&& !lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("10")
					&& !lFasUnificante.getFascicoloSiusModel().getCodStatoFascicolo().equals("13"))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento Unificante archiviato :  Unificazione Impossibile");

			// STUB 17/09/2004.
			if (lFasUnificante.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U004") == 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Procedimento Unificante di Esecuzione Misure Alternative :  Unificazione Impossibile");

			// Modifica del 21/11/2016 MEV_32
			IMagistratoRelatore lCtrlM = SIUSLookupRemote.getMagistratoRelatoreRemote();
			lMagRel = lCtrlM.ExRicercaEstesaMagRelByFascicolo(lFasUnificante.getFascicoloSiusModel()
					.getIdFascicoloSius());
			
			// [EC] 15022018 AMOMALIA ME32 SEGNALATA DA GASBARRI IN TEST PRECOLLAUDO
		    //controllo la presenza dei magistati per entrambi i fascicoli 			
			if (lMagRel != null && lMagRel.getMagistrato() != null) {
				lFasUnificante.setMagistratoRelatore(lMagRel);				
			} 
			
		} catch (SIUSException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("DecretoUnificazioneController.ExVerificaFascicoloUnificante: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException("DecretoUnificazioneController.ExVerificaFascicoloUnificante: " + e);

		} finally {
			cleanup(lFasDao);
			cleanup(lSoggDao);
			cleanup(lConn);
		}
		return lFasUnificante;
	}

	/**
	 * Esecuzione stampa Decreto di Unificazione
	 * <p>
	 * 
	 * @param aEvento
	 * @param lUfficio
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public EventoNotificaModel ExStampaDecretoUnificazione(EventoModel aEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException {
		ByteArrayOutputStream lByteArrayOut = null;
		EventoNotificaModel lEveNotifica = null;

		// Preleva i dati per la generazione.
		TreeModel lTree = this.prelevaDati(aEvento, lUfficio);

		// Invoca il Report generator.
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		// String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEveNot.getNomeTemplate());

		// Preleva dalla tabella Template il nome del template RTF.
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		TemplateModel lTemplateMod = lCtrlEve.ExRicercaTemplateByCodMotivo(aEvento.getCodMotivo());
		String lNomeTemplate = lTemplateMod.getPathRicerca() + lTemplateMod.getNomeTemplate();

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("######## NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug( ">>>>>> Generato il Documento ." );

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("EVENTO >>> "+aEvento.toString() );
		// Si imposta il ByteArrayInput ovverro il doc generato nell'evento
		// precisamente nel attributo DocBlobIn.
		aEvento.setDocBlobIn(lByteArrayInput);

		// Inserisce il documento generato nel model di ritorno
		// In esso inserisce il Nome del template di ritorno
		// e il documento generato.
		lEveNotifica = new EventoNotificaModel();
		lEveNotifica.setNomeTemplate(lTemplateMod.getNomeTemplate());
		lEveNotifica.getEvento().setDocBlobOut(lByteArrayOut);

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			// Preleva connessione dal Db
			lConn = getDBConnection();

			// Prepara un EventoDAO
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento);

			// Seleziona le condizioni di Update
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.update();

			commit(lConn);
		} catch (SIUSException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new F3BException(
					"DecretoUnificazioneController.ExStampaDecretoUnificazione: Non posso inserire il documento nell'evento : "
							+ daoex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new F3BException(
					"DecretoUnificazioneController.ExStampaDecretoUnificazione: Non posso inserire il documento nell'evento : "
							+ e);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return lEveNotifica;
	}

	/**
	 * Inserimento del Decreto di Unificazione : è stata già Verificata la Unificabilità dei due Procedimenti
	 * Individuati.; Vengono replicati i Tenori del FasDaUnif in FasUnificante; Il FasDaUnif viene aggiornato
	 * come Unificato; Si effettua l'inserimento dell'EVENTO relativo.
	 * <p>
	 * 
	 * @param aAnnoDaUnif
	 * @param aNumeroDaUnif
	 * @param aAnnoUnificante
	 * @param aNumeroUnificante
	 * @param aUtenteConnesso
	 * @param aUfficioUtenteConnesso
	 * @param aLuogoUfficioUtenteConnesso
	 * @param aDataUnificazione
	 * @return EventoModel
	 * @throws F3BException
	 */

	public EventoModel ExInserisciDecretoUnificazione(String aAnnoDaUnif, String aNumeroDaUnif,
			String aAnnoUnificante, String aNumeroUnificante, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso, Date aDataUnificazione)
			throws F3BException {
		Connection lConn = null;

		FascicoloSiusDAO lFasDao = null;
		TenoreDAO lTenDao = null;
		EventoDAO lEveDao = null;
		String lCodMagistrato = null;
		RichiestaConversioneDAO lRCDao = null;
		RichiestaConversioneSqlDAO lRCSqlDao = null;

		FascicoloGPModel lFasUnificante = new FascicoloGPModel();
		FascicoloGPModel lFasDaUnif = new FascicoloGPModel();
		EventoModel lEveMod = new EventoModel();

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSiusDAO(lConn);
			lTenDao = new TenoreDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Lettura del PROCEDIMENTO Unificante.
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasUnificante = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(new BigDecimal(aAnnoUnificante),
					new BigDecimal(aNumeroUnificante), aUfficioUtenteConnesso);

			// Lettura del PROCEDIMENTO da Unificare.
			lFasDaUnif = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(new BigDecimal(aAnnoDaUnif),
					new BigDecimal(aNumeroDaUnif), aUfficioUtenteConnesso);

			// Aggiornamento del FASCICOLO SIUS del PROCEDIMENTO da Unificare.
			lFasDao.setDAOFromModel(lFasDaUnif.getFascicoloSiusModel());
			lFasDao.setCodStatoFascicolo("05");
			lFasDao.setFasSiuIdFascicoloSius(lFasUnificante.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.setDataDefinizione(aDataUnificazione);
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasDaUnif.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.update();
			lFasDao.stop();

			// STUB 29/04/2004 Aggiornamento del FASCICOLO SIUS Unificante.
			lFasDao.setDAOFromModel(lFasUnificante.getFascicoloSiusModel());
			lFasDao.setNumeroFascicoliUnificati(lFasUnificante.getFascicoloSiusModel()
					.getNumeroFascicoliUnificati().add(new BigDecimal(1)));
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFasUnificante.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.update();
			lFasDao.stop();

			// Accodamento dei TENORE da Unificare in lFasUnificante.
			if (lFasDaUnif.getTenori() != null) {
				// Conta del N.ro dei TENORE del PROCEDIMENTO Unificante.
				int lTenoriUnificante = lFasUnificante.getTenori().length;

				// STUB 01/02/2005 Codici Oggetti gia presenti nel fascicolo unificante.
				String lCodOggettiPresenti = "";
				for (int k = 0; k < lTenoriUnificante; k++)
					lCodOggettiPresenti += lFasUnificante.getTenori()[k].getCodOggettoTenore();

				// Impostazione del ciclo di scrittura dei TENORE di lFasDaUnif;
				// La riscrittura consente di accodare al PROCEDIMENTO Unificante i TENORE del PROCEDIMENTO da
				// Unificare.
				TenoreModel lTenore = new TenoreModel();
				for (int j = 0; j < lFasDaUnif.getTenori().length; j++) {
					lTenore = lFasDaUnif.getTenori()[j];

					// STUB 01/02/2005 non si aggiunge il Codice Oggetto gia presente.
					if (lCodOggettiPresenti.indexOf(lTenore.getCodOggettoTenore()) < 0) {
						lTenore.setProgrTenore(new BigDecimal((double) (lTenoriUnificante + j + 1)));
						lTenore.setNote("UNIFICATO"); // STUB 01/02/2005
						lTenore.setGenPridGeneraleProcedimento(lFasUnificante.getGeneraleProcedimentoModel()
								.getIdGeneraleProcedimento());

						// Setto il DAO dal Model ed inserisco il Tenore
						lTenDao.setDAOFromModel(lTenore);
						lTenDao.insert();
					}
				}
			}
			// Ricerca del Magistrato Relatore
			IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
			MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(lFasDaUnif
					.getFascicoloSiusModel().getIdFascicoloSius());
			if (lMagRel != null && lMagRel.getMagistrato() != null)
				// Prelevare il codice Magistrato_Relatore
				lCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

			// Inserimento dell'EVENTO.
			lEveMod.setCodTipoEvento("01");
			lEveMod.setCodTipoProvvedimento("02");
			lEveMod.setCodMagistrato(lCodMagistrato);
			lEveMod.setDataEmissione(aDataUnificazione);
			lEveMod.setCodMotivo("0600");
			lEveMod.setFasSiuIdFascicoloSius(lFasDaUnif.getFascicoloSiusModel().getIdFascicoloSius());
			lEveMod.setFasSieIdFascicoloSiep(lFasDaUnif.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
			lEveMod.setCodUfficioEmittente(aUfficioUtenteConnesso);
			lEveMod.setDataInserimento(DateUtils.getSysDate());
			lEveMod.setCodUfficioInserimento(aUfficioUtenteConnesso);
			lEveMod.setCodOperatoreInserimento(aUtenteConnesso);

			lEveMod.setCodLuogoEmittente(aLuogoUfficioUtenteConnesso);
			lEveMod.setCodEsito("0600");
			lEveMod.setCodLuogoDestinatario("-");
			lEveMod.setCodTipoUfficioDestinatario("-");

			lEveDao.setDAOFromModel(lEveMod);
			lEveMod.setIdEvento(lEveDao.insert());

			// TODO carmela (verificare)
			// MEV "Revisione Misure di Sicurezza SIUS" modifica del 02/10/2013
			// In fase di Unificazione di due procedimenti SIUS, riportare tra gli "Altri Titoli Esecutivi"
			// nel procedimento unificante il/i procedimento/i assegnato/i al procedimento SIUS unificato

			// Titolo Esecutivo Principale
			BigDecimal idFasSiep = lFasDaUnif.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
			if (idFasSiep != null) {
				IFascicoloSiep lFasSiep = SIEPLookupRemote.getFascicoloSiepRemote();
				FascicoloSiepModel fascSiepMod = lFasSiep.ExRicercaFascicoloByKey(idFasSiep);
				if (fascSiepMod != null) {

					RiferimentoFascicoloSiepModel rifFasModel = new RiferimentoFascicoloSiepModel();

					rifFasModel.setFasSiuIdFascicoloSius(lFasUnificante.getFascicoloSiusModel()
							.getIdFascicoloSius());
					rifFasModel.setFasSieIdFascicoloSiep(lFasDaUnif.getFascicoloSiusModel()
							.getFasSieIdFascicoloSiep());
					rifFasModel.setAnnoFascicoloSiep(fascSiepMod.getChiaveAnno());
					rifFasModel.setProgrFascicoloSiep(fascSiepMod.getChiaveProgr());
					rifFasModel.setCodUffFascicoloSiep(fascSiepMod.getChiaveUfficio());
					rifFasModel.setCodTipoProvvedimento(fascSiepMod.getSentenza().getCodTipoProvvedimento());
					rifFasModel.setCodTipoAutoritaEmittente(fascSiepMod.getSentenza()
							.getCodTipoAutoritaEmittente());
					rifFasModel.setCodLuogoEmittente(fascSiepMod.getSentenza().getCodLuogoEmittente());
					rifFasModel.setAnnoProvvedimento(fascSiepMod.getSentenza().getAnnoSentenza());
					rifFasModel.setNumeroProvvedimento(fascSiepMod.getSentenza().getNumeroSentenza());
					rifFasModel.setDataProvvedimento(fascSiepMod.getSentenza().getDataProvvedimento());
					rifFasModel.setDataIrrevocabilita(fascSiepMod.getDataIrrevocabilita());
					rifFasModel.setCodOperatoreInserimento(aUtenteConnesso);
					rifFasModel.setCodUfficioInserimento(aUfficioUtenteConnesso);
					rifFasModel.setDataInserimento(DateUtils.getSysDate());
					rifFasModel.setFlagFasSiusUnif("S");

					IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
					lCtrlRFS.ExInserisciRiferimentoFascicoloSiep(rifFasModel);

				}

			}

			// Altri Titoli Esecutivi
			// Lettura del/i procedimento/i SIEP legati al procedimento SIUS unificato
			IRiferimentoFascicoloSiep lRifFasCtrl = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
			Vector lRiferimentiSiep = new Vector();
			lRiferimentiSiep = lRifFasCtrl.ExRicercaRiferimentoFascicoloSiepByIdFasSius(lFasDaUnif
					.getFascicoloSiusModel().getIdFascicoloSius());
			if (lRiferimentiSiep.size() > 0) {

				Iterator iter = lRiferimentiSiep.iterator();
				while (iter.hasNext()) {

					RiferimentoFascicoloSiepModel rifFasModel = (RiferimentoFascicoloSiepModel) iter.next();

					rifFasModel.setFasSiuIdFascicoloSius(lFasUnificante.getFascicoloSiusModel()
							.getIdFascicoloSius());
					rifFasModel.setCodOperatoreInserimento(aUtenteConnesso);
					rifFasModel.setDataInserimento(DateUtils.getSysDate());
					rifFasModel.setCodUfficioInserimento(aUfficioUtenteConnesso);
					rifFasModel.setFlagFasSiusUnif("S");

					// Si associano il/i Riferimento/i FascicoloSiep del Procedimento unificato
					// al Procedimento Unificante.
					IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
					lCtrlRFS.ExInserisciRiferimentoFascicoloSiep(rifFasModel);

				}

			}

			// 23/03/2009 Eventuali Inserimenti di RICHIESTA_CONVERSIONE.
			if (lFasDaUnif.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE) == 0) {
				lRCDao = new RichiestaConversioneDAO(lConn);
				lRCSqlDao = new RichiestaConversioneSqlDAO(lConn);
				RichiestaConversioneModel lRCModel = new RichiestaConversioneModel();
				lRCModel.setFasSiuIdFascicoloSius(lFasDaUnif.getFascicoloSiusModel().getIdFascicoloSius());
				lRCSqlDao.ricercaRichiestaConversione(lRCModel);
				lRCSqlDao.ricercaRichiestaConversioneByIdFasSIUS(lFasDaUnif.getFascicoloSiusModel()
						.getIdFascicoloSius());
				lRCSqlDao.start();
				while (lRCSqlDao.next()) {
					RichiestaConversioneModel lRicConv = new RichiestaConversioneModel(
							(RichiestaConversioneModel) lRCSqlDao.getModel());
					lRicConv.setFasSiuIdFascicoloSius(lFasUnificante.getFascicoloSiusModel()
							.getIdFascicoloSius());
					lRicConv.setDataInserimento(DateUtils.getSysDate());
					lRicConv.setCodUfficioInserimento(aUfficioUtenteConnesso);
					lRicConv.setCodOperatoreInserimento(aUtenteConnesso);
					lRicConv.setDataAggiornamento(null);
					lRicConv.setCodUfficioAggiornamento("");
					lRicConv.setCodOperatoreAggiornamento("");
					lRicConv.setEveIdEvento(lEveMod.getIdEvento());
					lRCDao.setDAOFromModel(lRicConv);
					lRCDao.insert();
				}
			}

			// COMMIT
			commit(lConn);

		} catch (SIUSException se) {
			rollback(lConn);
			throw se;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + ex);
			throw new F3BException("DecretoUnificazioneController.exInserisciDecretoUnificazione: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw new F3BException("DecretoUnificazioneController.exInserisciDecretoUnificazione: " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lRCDao);
			cleanup(lRCSqlDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Esegue la cancellazione di un Decreto di unificazione.
	 * <p>
	 * 
	 * @param aKeyEvento
	 *            : chiave del record
	 * @throws F3BException
	 */
	public void ExCancellaDecretoUnificazione(BigDecimal aKeyEvento, String aUfficioUtenteConnesso,
			String aUtenteConnesso, String aLuogoUfficioUtenteConnesso) throws F3BException {

		Connection lConn = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		FascicoloSiusDAO lFasDao = null;
		TenoreDAO lTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		EventoDAO lEveDao = null;
		RichiestaConversioneDAO lRCDao = null;

		// Preleva l'eventoModel da cancellare.
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel aEvento = lEveCtrl.ExRicercaEventoByKey(aKeyEvento);
		if (aEvento == null)
			throw new F3BException("Attenzione! Decreto Unificazione non trovato!");

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSiusDAO(lConn);
			lTenDao = new TenoreDAO(lConn);
			lTenSqlDao = new TenoreSqlDAO(lConn);
			lEveDao = new EventoDAO(lConn);

			// Lettura del Procedimento SIUS Unificato.
			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByKey(aEvento.getFasSiuIdFascicoloSius());
			FascicoloGPModel lFGPUnificato = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

			// Caricamento dei Tenore del Procedimento Unificato.
			lTenSqlDao.ricercaTenoreByGeneraleProc(lFGPUnificato.getGeneraleProcedimentoModel()
					.getIdGeneraleProcedimento());

			Vector lVectTenori = new Vector(lTenSqlDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFGPUnificato.setTenori(lTenoriModel);
			}
			lTenSqlDao.stop();

			// Lettura del Procedimento SIUS Unificante.
			lFasGPSqlDao.ricercaFascicoloByKey(lFGPUnificato.getFascicoloSiusModel()
					.getFasSiuIdFascicoloSius());
			FascicoloGPModel lFGPUnificante = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

			// Caricamento dei Tenore del Procedimento Unificante.
			lTenSqlDao.ricercaTenoreByGeneraleProc(lFGPUnificante.getGeneraleProcedimentoModel()
					.getIdGeneraleProcedimento());

			lVectTenori = new Vector(lTenSqlDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
				lFGPUnificante.setTenori(lTenoriModel);
			}
			lTenSqlDao.stop();

			// Aggiornamento del FASCICOLO SIUS del PROCEDIMENTO da DIS-Unificare.
			// STUB 10/05/2004 lFasDao.setDAOFromModel(lFGPUnificato.getFascicoloSiusModel());
			lFasDao.setCodStatoFascicolo("02");
			BigDecimal lBigDec = null;
			lFasDao.setFasSiuIdFascicoloSius(lBigDec);
			Date lDate = null;
			lFasDao.setDataDefinizione(lDate);
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFGPUnificato.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.update();
			lFasDao.stop();

			// Aggiornamento del FASCICOLO SIUS Unificante.
			// STUB 10/05/2004 lFasDao.setDAOFromModel(lFGPUnificante.getFascicoloSiusModel());
			// STUB 10/05/2004 Se il Numero di Fas. Unificati no è > 0 non esegue la "SUBTRACT".
			if (lFGPUnificante.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0)
				lFasDao.setNumeroFascicoliUnificati(lFGPUnificante.getFascicoloSiusModel()
						.getNumeroFascicoliUnificati().subtract(new BigDecimal(1)));
			lFasDao.setDataAggiornamento(DateUtils.getSysDate());
			lFasDao.setCodUfficioAggiornamento(aUfficioUtenteConnesso);
			lFasDao.setCodOperatoreAggiornamento(aUtenteConnesso);
			lFasDao.setCondizioneUpdate(lFGPUnificante.getFascicoloSiusModel().getIdFascicoloSius());
			lFasDao.update();
			lFasDao.stop();

			// Scodamento dei TENORE da lFGPUnificante e riaccodamento a lFGPUnificato.
			if (lFGPUnificante.getTenori() != null) {
				// Conta del N.ro dei TENORE del PROCEDIMENTO Unificante e Unificato.
				int lTenoriUnificante = lFGPUnificante.getTenori().length;
				int lTenoriUnificato = lFGPUnificato.getTenori().length;

				// Impostazione del ciclo di cancellazione dei TENORE di lFGPUnificante;
				// La Cancellazione consente di escludere i TENORE precedentemente inseriti, dal PROCEDIMENTO
				// Unificante.
				TenoreModel lTenoreDiUnificante = new TenoreModel();
				TenoreModel lTenoreDiUnificato = new TenoreModel();
				for (int j = 0; j < lTenoriUnificante; j++) {
					lTenoreDiUnificante = lFGPUnificante.getTenori()[j];
					for (int jj = 0; jj < lTenoriUnificato; jj++) {
						lTenoreDiUnificato = lFGPUnificato.getTenori()[jj];

						// STUB 01/02/2005 non si elimina il Tenore dell'unificante.
						if (lTenoreDiUnificante.getCodOggettoTenore().equals(
								lTenoreDiUnificato.getCodOggettoTenore())
								&& lTenoreDiUnificante.getCodDettaglioOggetto().equals(
										lTenoreDiUnificato.getCodDettaglioOggetto())
								&& lTenoreDiUnificante.getNote() != null
								&& lTenoreDiUnificante.getNote().compareTo("UNIFICATO") == 0) {
							// Setto il DAO dal Model e cancello il Tenore.
							lTenDao.setCondizioneUpdate(lTenoreDiUnificante.getIdTenore());
							lTenDao.delete();
							break;
						}
					}
				}
			}
			// 23/03/2009 Eventuali Cancellazioni di RICHIESTA_CONVERSIONE.
			if (lFGPUnificato.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE) == 0) {
				lRCDao = new RichiestaConversioneDAO(lConn);
				lRCDao.selCondizioneByIdFasSiusIdEvento(lFGPUnificante.getFascicoloSiusModel()
						.getIdFascicoloSius(), aKeyEvento);
				lRCDao.delete();
			}

			// TODO carmela (verificare)
			// Modifica del 11/10/2013 Mev "Revisione Misure di Sicurezza SIUS"
			// Bisogna Eliminare i Procedimenti SIEP del procedimento unificante,
			// che gli erano stati associati in fase di unificazione ed erano legati
			// al procedimento unificato.
			IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
			BigDecimal idFascicoloUnificante = lFGPUnificante.getFascicoloSiusModel().getIdFascicoloSius();
			lCtrlRFS.ExCancellaRiferimentoFascicoloSiepByIdFasSius(idFascicoloUnificante);

			// cancellazione Evento collegato.
			lEveDao.selCondizioneUpdate(aKeyEvento);
			lEveDao.delete();

			// COMMIT
			commit(lConn);
		} catch (F3BException Fe) {
			rollback(lConn);
			throw Fe;
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIUSException("DepositoDecretoController.ExCancellaDecretoUnificazione: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + e);
			throw new SIUSException("DepositoDecretoController.ExCancellaDecretoUnificazione: " + e);
		} finally {
			cleanup(lFasGPSqlDao);
			cleanup(lFasDao);
			cleanup(lTenDao);
			cleanup(lEveDao);
			cleanup(lTenSqlDao);
			cleanup(lRCDao);

			cleanup(lConn);
		}
	}

	/**
	 * Preleva dati dai dao e li organizza gerarchicamente.
	 * 
	 * @param aEvento
	 * @param lUfficio
	 * @return lTreeRoot
	 *         <p>
	 * @throws F3BException
	 */
	private TreeModel prelevaDati(EventoModel aEvento, UfficioModel lUfficio) throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		FascicoloGPSqlDAO lFasGPSqlDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		FascicoloSiepSqlDAO lFasSiepSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosGiuSqlDao = null;
		LuogoDetenzioneSqlDAO lDetenzioneSqlDao = null;
		CampoNotaSqlDAO lCampoNotaSqlDao = null;
		SentenzaSqlDAO lSentenzaSqlDao = null;

		Connection lConn = null;

		try {
			// Get Connection.
			lConn = getDBConnection();

			// Procedimento SIUS Unificato.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load Fascicolo Generale Procedimento. ") ;
			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByKey(aEvento.getFasSiuIdFascicoloSius());
			FascicoloGPModel lFGPUnificato = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("###### Dati prelevati da lFGPUnificato " + lFGPUnificato );

			// Procedimento SIUS Unificante.
			lFasGPSqlDao.ricercaFascicoloByKey(lFGPUnificato.getFascicoloSiusModel()
					.getFasSiuIdFascicoloSius());
			FascicoloGPModel lFGPUnificante = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("###### Dati prelevati da lFGPUnificante " + lFGPUnificante );

			// Estrazione del FascicoloSius Unificato.
			FascicoloSiusModel lFasUnificato = lFGPUnificato.getFascicoloSiusModel();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("###### Dati prelevati da lFasUnificato " + lFasUnificato );

			// Estrazione del FascicoloSius Unificante.
			FascicoloSiusModel lFasUnificante = lFGPUnificante.getFascicoloSiusModel();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("###### Dati prelevati da lFasUnificante " + lFasUnificante );

			// Estrazione del Generale procedimento model Unificante.
			// GeneraleProcedimentoModel lGPModel = lFGPUnificante.getGeneraleProcedimentoModel();
			// Estrazione del Generale procedimento model Unificato 16/01/2004.
			GeneraleProcedimentoModel lGPModelUnificato = lFGPUnificato.getGeneraleProcedimentoModel();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("###### Dati prelevati da lGPModelUnificato Unificato " + lGPModelUnificato );

			// Si prelevano i Tenori.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load Tenori. ") ;
			lTenSqlDao = new TenoreSqlDAO(lConn);
			lTenSqlDao.ricercaTenoriByGeneraleProcOrderByPeso(lGPModelUnificato.getIdGeneraleProcedimento());
			// Vector lTenori = new Vector(lTenSqlDao.getModels());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load Tenori. ") ;

			// Si preleva l'eventuale Magistrato Relatore (tramite Tenore.cod_magistrato sulla tabella
			// magistrato).
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load Magistrato Relatore. ") ;

			// Soggetto
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load Soggetto. ") ;
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasUnificante.getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load Soggetto. ") ;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("##### Dati prelevati da lSogModel " + lSogModel );

			// Residenza
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load Residenza. ") ;
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzaByFascicoloSius(lFasUnificante.getIdFascicoloSius());
			ResidenzaModel lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load Residenza. ") ;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("##### Dati prelevati da lResModel " + lResMod );

			// Fascicolo Siep.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load FascicoloSiep. ") ;
			FascicoloSiepModel lFasSiepModel = null;
			// Controlla se l'id nel model sius di siep è valorizzato.
			if (lFasUnificante.getFasSieIdFascicoloSiep() != null) {
				lFasSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
				lFasSiepSqlDao.ricercaFascicoloByKey(lFasUnificante.getFasSieIdFascicoloSiep());
				lFasSiepModel = (FascicoloSiepModel) lFasSiepSqlDao.getModelByKey();
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load FascicoloSiep. ") ;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("##### Dati prelevati da lFasSiepModel " + lFasSiepModel );

			// Sentenza
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load Sentenza. ") ;
			SentenzaModel lSentenzaModel = null;
			// Controllo se esiste un fascicolo SIEP.
			if (lFasSiepModel != null) {
				lSentenzaSqlDao = new SentenzaSqlDAO(lConn);
				lSentenzaSqlDao.ricercaSentenzaBykey(lFasSiepModel.getSenIdSentenza());
				lSentenzaModel = (SentenzaModel) lSentenzaSqlDao.getModelByKey();
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load Sentenza. ") ;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("##### Dati prelevati da lSentenzaModel " + lSentenzaModel );

			// Posizione Giuridica.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load PosizioneGiuridica. ") ;
			PosizioneGiuridicaModel lPosGiuModel = null;
			if (lFasSiepModel != null) {
				lPosGiuSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
				lPosGiuSqlDao.ricercaPosizioneGiuridicaByIdFascicolo(lFasUnificante
						.getFasSieIdFascicoloSiep());
				lPosGiuModel = (PosizioneGiuridicaModel) lPosGiuSqlDao.getModelByKey();
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load PosizioneGiuridica. ") ;
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("##### Dati prelevati da lPosGiuModel " + lPosGiuModel );

			// Luogo detenzione
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Inizio Sezione - Load LuogoDetenzione. ") ;
			lDetenzioneSqlDao = new LuogoDetenzioneSqlDAO(lConn);
			lDetenzioneSqlDao.ricercaLuogoDetenzioneCorrenteByFascicoloSius(lFasUnificante
					.getIdFascicoloSius());
			LuogoDetenzioneModel lDetenzioneModel = (LuogoDetenzioneModel) lDetenzioneSqlDao.getModelByKey();
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>>>>> Fine Sezione - Load LuogoDetenzione. ") ;

			// Imposta la data di emissione nel model prelevata dal Dbase
			// nel model di passaggio dati EveNot.
			// Questa operazione consente al metodo createRoot di valorizzare
			// nel XModel la data di emissione prelevata dal DB.
			aEvento.setDataEmissione(aEvento.getDataEmissione());

			// Costruzione albero dei model per la generazione del XML.
			// Creazione radice.
			lTreeRoot = new TreeModel(createRoot(aEvento, lUfficio));

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Prepara i TreeModel ");

			TreeModel lTreeEvento = new TreeModel(aEvento);
			// 16/01/2004 Inversione dati fra F. Unificante e Unificato.
			// TreeModel lTreeFasSiusModel = new TreeModel( lFasUnificante );
			// TreeModel lTreeFasSiusUnificato = new TreeModel( lFasUnificato );
			TreeModel lTreeFasSiusModel = new TreeModel(lFasUnificato);
			TreeModel lTreeFasSiusUnificante = new TreeModel(lFasUnificante);

			TreeModel lTreeSogModel = new TreeModel(lSogModel);
			TreeModel lTreeFasSiepModel = new TreeModel(lFasSiepModel);

			// Residenza sotto il soggetto.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug( ">>> Inserisce la Residenza sotto il Soggetto.");
			if (lResMod != null)
				lTreeSogModel.add(new TreeModel(lResMod));

			// Inserisce il GPmodel unificato sotto il fascicolo unificato.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Inserisce il GPmodel sotto il fascicolo unificato.");
			if (lGPModelUnificato != null)
				lTreeFasSiusModel.add(new TreeModel(lGPModelUnificato));

			// Inserisce il luogo di detenzione sotto il fascicolo SIUS.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Inserisce il luogo di detenzione model sotto il fascicolo Sius.");
			if (lDetenzioneModel != null)
				lTreeFasSiusUnificante.add(new TreeModel(lDetenzioneModel));

			// Inserisce la posizione giuridica sotto il fascicolo SIEP.
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Inserisce la posizione giuridica sotto il fascicolo Siep.");
			if (lPosGiuModel != null)
				lTreeFasSiepModel.add(new TreeModel(lPosGiuModel));

			// Inserisce la sentenza sotto il fascicolo Siep
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(">>>> Inserisce la Sentenza sotto il fascicolo Siep.");
			if (lSentenzaModel != null)
				lTreeFasSiepModel.add(new TreeModel(lSentenzaModel));

			// Si prelevano dei CampiNote
			lCampoNotaSqlDao = new CampoNotaSqlDAO(lConn);
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aEvento.getIdEvento());
			Vector lCampiNote = new Vector(lCampoNotaSqlDao.getModels());

			Iterator lItx = lCampiNote.iterator();
			// Aggiunge alla gerarchia sotto Evento le note.
			while (lItx.hasNext())
				lTreeEvento.add(new TreeModel((CampoNotaModel) lItx.next()));

			// Colpo Finale :))
			lTreeRoot.add(lTreeFasSiusModel);
			lTreeRoot.add(lTreeFasSiusUnificante);
			lTreeRoot.add(lTreeSogModel);
			lTreeRoot.add(lTreeEvento);
			lTreeRoot.add(lTreeFasSiepModel);
			// Riferimento Fascicolo SIEP.
			lTreeRoot = prelevaDatiRifasiep(lTreeRoot, lFGPUnificante.getFascicoloSiusModel()
					.getIdFascicoloSius(), lConn); // STUB 14/10/2004

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("DecretoUnificazioneController.prelevaDati: " + daoEx);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + Ex);
			throw new F3BException("DecretoUnificazioneController.prelevaDati: " + Ex);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lFasGPSqlDao);
			cleanup(lPosGiuSqlDao);
			cleanup(lResSqlDao);
			cleanup(lFasSiepSqlDao);
			cleanup(lSentenzaSqlDao);
			cleanup(lCampoNotaSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lDetenzioneSqlDao);
			cleanup(lSentenzaSqlDao);
			cleanup(lConn);
		}

		return lTreeRoot;
	}

	/**
	 * Creazione della radice dell'albero.
	 * <p>
	 * 
	 * @param aEvento
	 * @param lUfficio
	 * @return il model di radice.
	 */
	private XModel createRoot(EventoModel aEvento, UfficioModel lUfficio) {
		XModel lXMod = new XModel();

		lXMod.setTipoUfficioT1(aEvento.getDescrUfficioEmittente().toUpperCase());
		lXMod.setUfficio(aEvento.getDescrLuogoEmittente().toUpperCase());
		lXMod.setIndirizzo(lUfficio.getIndirizzo());
		lXMod.setCap(lUfficio.getCap());
		lXMod.setFax(lUfficio.getFax());
		lXMod.setTelefono(lUfficio.getTelefono());
		lXMod.setDataElaborazione(DateUtils.getSysDate());

		return lXMod;
	}

	/**
	 * STUB 11/10/2004 Si prelevano i dati di Riferimento Fascicolo Siep.
	 * <p>
	 * 
	 * @param aIdFascicoloSIUS
	 *            id del FascicoloSIUS.
	 * @param aConn
	 *            connessione al dbase.
	 * @return lTreeRFSMod dati dei Riferimento Fascicolo SIEP.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiRifasiep(TreeModel aTreeModel, BigDecimal aIdFascicoloSIUS, Connection aConn)
			throws F3BException {
		if (aTreeModel != null) {
			RiferimentoFascicoloSiepModel lRFSMod = new RiferimentoFascicoloSiepModel();
			lRFSMod.setFasSiuIdFascicoloSius(aIdFascicoloSIUS);
			RiferimentoFascicoloSiepSqlDAO lRifSqlDao = null;

			try {
				if (aIdFascicoloSIUS != null) {
					// Caricamento Riferimenti Fascicolo SIEP.
					lRifSqlDao = new RiferimentoFascicoloSiepSqlDAO(aConn);
					lRifSqlDao.ricercaRiferimentoFascicoloSiep(lRFSMod);
					Vector lRiFaSiep = new Vector(lRifSqlDao.getModels());

					if (lRiFaSiep.size() != 0) {
						Iterator lItx = lRiFaSiep.iterator();
						while (lItx.hasNext()) {
							RiferimentoFascicoloSiepModel lModel = ((RiferimentoFascicoloSiepModel) lItx
									.next());
							aTreeModel.add(new TreeModel(lModel));
						}
					}
				}
			}

			catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("DecretoUnificazioneController.prelevaDatiRifasiep: " + daoEx);
			} catch (Exception e) {
				e.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + e);
				throw new F3BException(
						"DecretoUnificazioneController.prelevaDatiRifasiep: Eccezione Generica: " + e);
			} finally {
				cleanup(lRifSqlDao);
			}
		} else
			throw new F3BException(
					"DecretoUnificazioneController.prelevaDatiRifasiep: TreeModel Fascicolo SIUS null !");
		return aTreeModel;
	}

}