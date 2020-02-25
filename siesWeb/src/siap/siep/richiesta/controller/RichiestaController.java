package siap.siep.richiesta.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.jms.messaggio.dao.MessaggioDAO;
import siap.jms.messaggio.dao.MessaggioSqlDAO;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.CalendarUtil;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.archiviazione.dao.ArchiviazioneSqlDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.competenza.dao.CompetenzaDAO;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.dao.FungibilitaDAO;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.nomeprovvedimento.dao.NomeProvvedimentoDAO;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.dao.SospensioneDAO;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: RichiestaController
 * </p>
 * <p>
 * Description: Controller della richiesta
 * </p>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RichiestaController extends SiapController implements IRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Stampa la richiesta
	 * 
	 * @param aTreeModel
	 * @param aTypeReport
	 * @return
	 * @throws F3BException
	 */
	public OutputStream ExStampaRichiesta(TreeModel aTreeModel, int aTypeReport) throws F3BException {

		// String lOutput;
		// ByteArrayOutputStream lOut;
		try {
			// Creare l'XML
			/*
			 * //Creo il TreeMOdel non gerarchico uno per Soggetto,Fascicolo,Sentenza ModelTreeInputSource lIs
			 * = new ModelTreeInputSource(aTreeModel); ParserModelTree lParser = new ParserModelTree();
			 * XmlDocument lDocXML = lParser.parse(lIs); CharArrayWriter lWri = new CharArrayWriter();
			 * 
			 * lDocXML.write(System.out); lDocXML.write(lWri);
			 * 
			 * //ByteArrayInputStream lXML = new ByteArrayInputStream(lXMLData.getBytes());
			 * ByteArrayInputStream lXML = new ByteArrayInputStream(lWri.toString().getBytes());
			 * 
			 * //Settare il template rtf e il doc rtf di output FileInputStream lRTF = new
			 * FileInputStream("C://template//IS02.rtf"); //ByteArrayInputStream lXML = new
			 * ByteArrayInputStream(lXMLData.getBytes()); lOut = new ByteArrayOutputStream();
			 * 
			 * ReportGenerator lReport = new ReportGenerator(lXML, lRTF, lOut);
			 * lReport.setReport(aTypeReport);
			 * 
			 * lReport.process(); lOutput = lOut.toString();
			 * 
			 * //Chiudo le risorse. lRTF.close(); lXML.close();
			 */
		} catch (Throwable t) {
			t.printStackTrace();
			throw new F3BException(F3BException.USER_MESSAGE, "Errore nella generazione del report");
		}

		return null;
	}

	/**
	 * Valida la Richiesta Acc Reato
	 * 
	 * @param aEventoRid
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaRichiestaAccReato(EventoModel aEventoRid, EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDAO = null;
		AnnotazioneManualeDAO lAnnDAO = null;
		AnnotazioneManualeSqlDAO lAnnSqlDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		PenaResiduaDAO lPenResDAO = null;
		PenaResiduaSqlDAO lPenResSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		FungibilitaDAO lFungDao = null;
		FungibilitaSqlDAO lFungSqlDao = null;

		/*
		 * Connection lConnBlob = null;
		 */
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		AnnotazioneManualeModel lAnnMod = null;
		PenaResiduaModel lPenResMod = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(" ");

			// Ricerco l'ultima pena in assoluto
			lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDAO.ricercaPenaResiduaByIdFascicoloDataDesc(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

			// valido annotazione legata all'evento 0122
			lAnnDAO = new AnnotazioneManualeDAO(lConn);
			lAnnMod = new AnnotazioneManualeModel();

			lAnnSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
			String lAttProv = null;

			if (aEventoRid != null) {
				lAnnSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEventoRid.getIdEvento());

				Vector lAnnVec = new Vector(lAnnSqlDAO.getModels());
				for (int y = 0; y < lAnnVec.size(); y++) {
					lAnnMod = (AnnotazioneManualeModel) lAnnVec.get(y);
					if (lAnnMod.getFlagAppProvvisoria().equals("A"))
						lAttProv = lAnnMod.getFlagAppProvvisoria();

					// Se esiste non validata lega l'annotazione
					// all'EVENTO effettivo di cui l'utente ha fatto la stampa
					// e di cui vede il dettaglio a video
					// if ( lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato())) )
					if (aEventoRid.getAnnIdAnnotazioneManuale() == null) {
						lAnnMod.setEveIdEvento(aEvento.getIdEvento());
					}

					lAnnMod.setFlagValidato("S");

					lAnnMod.setDataAggiornamento(aEventoRid.getDataAggiornamento());
					lAnnMod.setCodOperatoreAggiornamento(aEventoRid.getCodOperatoreAggiornamento());
					lAnnMod.setCodUfficioAggiornamento(aEventoRid.getCodUfficioAggiornamento());

					lAnnDAO.setDAOFromModelForUpdate(lAnnMod);
					lAnnDAO.update();
					lAnnDAO.stop();
				}
			}

			if (aEventoRid != null) {
				// Ricerco la fungibilita' legata all'evento di appoggio
				// che deve essere cancellato
				lFungSqlDao = new FungibilitaSqlDAO(lConn);
				lFungSqlDao.ricercaFungibilitaByKeyEvento(aEventoRid.getIdEvento());
				FungibilitaModel lFunMod = (FungibilitaModel) lFungSqlDao.getModelByKey();

				// Se almeno una presente viene legata allo stesso evento
				// a cui si lega la pena residua
				if (lFunMod != null) {
					lFungDao = new FungibilitaDAO(lConn);

					lFungDao.setEveIdEvento(aEvento.getIdEvento());
					lFungDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lFungDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lFungDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					// Al momento potrebbe esserci piu' di un record di Fungibilita'
					// per lo stesso evento,
					// quindi la condizione di update viene posta sul campo EVE_ID_EVENTO
					lFungDao.setCondizioneUpdateEveIdEvento(aEventoRid.getIdEvento());
					lFungDao.update();
					lFungDao.stop();
				}

				// ========================================================================
				// Cancello l'evento di appoggio collegato (della richiesta)
				// ========================================================================
				lEveDao.setIdEvento(aEventoRid.getIdEvento());
				lEveDao.setDAOFromModelForUpdate(aEventoRid);
				lEveDao.delete();
				lEveDao.stop();
			}

			// ========================================================================
			// Valido pena residua se flag app_provvisoria delle annotazioni manuali e' = A
			// e aggancia tale pena all'EVENTO effettivo di cui l'utente ha fatto la stampa
			// e di cui vede il dettaglio a video
			// ========================================================================
			if (lAttProv != null && lAttProv.equals("A")) {
				// Se esiste non validata aggiorna la pena
				if (lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato()))) {
					lPenResMod.setFlagValidato("S");
					lPenResMod.setEveIdEvento(aEvento.getIdEvento());

					lPenResDAO = new PenaResiduaDAO(lConn);

					lPenResDAO.setDAOFromModelForUpdate(lPenResMod);
					lPenResDAO.update();
					lPenResDAO.stop();
				}
			}

			// ** STATO_PROCEDIMENTO **/

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoDao = new StatoProcedimentoDAO(lConn);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			lStatoProcMod.setCodStatoProcedimento("0127");

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoDao.setDAOFromModel(lStatoProcMod);

			lStatoDao.insert();
			lStatoDao.stop();

			// inserisco nome provvedimento

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP083");
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
				aEvento.setPenIdPenaResidua(lPenResMod.getIdPenaResidua());
			}

			// lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdate(aEvento);
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaAccReato : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lAnnDAO);
			cleanup(lAnnSqlDAO);
			cleanup(lPenResSqlDAO);
			cleanup(lPenResDAO);
			cleanup(lStatoDao);
			cleanup(lPosSqlDAO);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lFungDao);
			cleanup(lFungSqlDao);

			// cleanup(lConnBlob);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Valida la richiesta con Codice. - Rideterminazione pena Amnistia/Indulto (revoca e concessione) -
	 * Depenalizzazione - Incostituzionalita' - Anche le richieste di quantificazione pena su singolo reato
	 * (art 671) - Restituzione Ordine di esecuzione - Ordine di scarcerazione provvisorio
	 * 
	 * @param aEventoRid
	 *            evento collegato all'evento da validare
	 * @param aEvento
	 *            evento da validare
	 * @param lCodiceNomeProvv
	 * @param aFascicolo
	 * @return
	 */
	public EventoModel ExUpdateValidaRichiesteConCodice(EventoModel aEventoRid, EventoModel aEvento,
			String lCodiceNomeProvv, FascicoloSiepModel aFascicolo, BigDecimal aGiorniLA) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("####### inizio ######## ");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aEventoRid: " + aEventoRid);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("aEvento: " + aEvento);
		// ==========================================================================
		// L'evento da validare (aEvento) e' collegato a una annotazione, a sua
		// volta legata ad altro evento (aEventoRid)
		// aEventoRid se <> null e' necessariamente l'evento fittizio da eliminare
		// vuol dire che ci si trova ancora nella situazione di una annotazione
		// per la quale non e' stato ancora emesso un vero evento
		// ==========================================================================
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		AnnotazioneManualeDAO lAnnDAO = null;
		PenaResiduaDAO lPenResDAO = null;
		PenaResiduaSqlDAO lPenResSqlDAO = null;
		AnnotazioneManualeSqlDAO lAnnSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		FungibilitaDAO lFungDao = null;
		FungibilitaSqlDAO lFungSqlDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		AnnotazioneManualeModel lAnnMod = null;
		PenaResiduaModel lPenResMod = null;

		SospensioneSqlDAO lSospSqlDao = null;
		SospensioneDAO lSospDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero alcuni dati dell'evento da validare
			// - data emissione
			// - data inserimento
			// ========================================================================
			lEveDao = new EventoDAO(lConn);
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setDataInserimento(lEveDao.getDataInserimento());
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}

			// Ricerco l'ultima pena in assoluto
			lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDAO.ricercaPenaResiduaByIdFascicoloDataDesc(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

			lAnnDAO = new AnnotazioneManualeDAO(lConn);
			lAnnMod = new AnnotazioneManualeModel();

			lAnnSqlDAO = new AnnotazioneManualeSqlDAO(lConn);

			// ========================================================================
			// Se ancora presente evento fittizio, Ricerco le annotazioni manuali, le
			// aggancio all'evento corrente e le Valido
			// ========================================================================
			String lAttProv = null;
			if (aEventoRid != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("Presente evento fittizio, Ricerco le annotazioni manuali, le aggancio all'evento corrente e le Valido");
				lAnnSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEventoRid.getIdEvento());

				Vector lAnnVec = new Vector(lAnnSqlDAO.getModels());

				lAnnSqlDAO.stop();

				if (lAnnVec != null) {
					for (int y = 0; y < lAnnVec.size(); y++) {
						lAnnMod = (AnnotazioneManualeModel) lAnnVec.get(y);
						if (lAnnMod.getFlagAppProvvisoria().equals("A"))
							lAttProv = lAnnMod.getFlagAppProvvisoria();

						// Se esiste non validata lega l'annotazione
						// all'EVENTO effettivo di cui l'utente ha fatto la stampa
						// e di cui vede il dettaglio a video
						// if ( lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato())) )
						if (aEventoRid.getAnnIdAnnotazioneManuale() == null) {
							lAnnMod.setEveIdEvento(aEvento.getIdEvento());
						}

						lAnnMod.setFlagValidato("S");

						// Modifico la data di inserimento, deve essere la stessa dell'evento
						// che valido altrimenti le annotazioni potrebbero non venir computate
						// correttamente
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lEveApp1 = " + lEveApp);

						lAnnMod.setDataAggiornamento(aEvento.getDataAggiornamento());
						lAnnMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lAnnMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

						lAnnDAO.setDataInserimento(lEveApp.getDataInserimento());
						lAnnDAO.setDAOFromModelForUpdate(lAnnMod);
						lAnnDAO.update();
						lAnnDAO.stop();
					}
				}
			}

			// ========================================================================
			// Se presente fungibilita' o PR agganciati all'evento fittizio li aggancio
			// all'evento corrente
			// ========================================================================
			if (aEventoRid != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presente evento fittizio, Ricerco le Fungibilita' e PR");
				// Ricerco la fungibilita' legata all'evento di appoggio
				// che deve essere cancellato
				lFungSqlDao = new FungibilitaSqlDAO(lConn);
				lFungSqlDao.ricercaFungibilitaByKeyEvento(aEventoRid.getIdEvento());
				FungibilitaModel lFunMod = (FungibilitaModel) lFungSqlDao.getModelByKey();

				// Se almeno una presente viene legata allo stesso evento
				// a cui si lega la pena residua
				if (lFunMod != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Fungibilita' Presente...");
					lFungDao = new FungibilitaDAO(lConn);

					lFungDao.setEveIdEvento(aEvento.getIdEvento());

					lFungDao.setDataInserimento(lEveApp.getDataInserimento());

					lFungDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lFungDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lFungDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					// Al momento potrebbe esserci piu' di un record di Fungibilita'
					// per lo stesso evento,
					// quindi la condizione di update viene posta sul campo EVE_ID_EVENTO
					lFungDao.setCondizioneUpdateEveIdEvento(aEventoRid.getIdEvento());
					lFungDao.update();
					lFungDao.stop();
				}

				// ========================================================================
				// Verifico se e' stato effettuato un calcolo della pena sulla richiesta
				// in questo caso devo sganciare il record PENA_RESIDUA e agganciarlo
				// all'evento corrente
				// ========================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Verifico se presenti PR su evento fittizio");
				lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDAO.ricercaPenaResiduaByKeyEvento(aEventoRid.getIdEvento());
				PenaResiduaModel lPenResModApp = null;
				lPenResModApp = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

				if (lPenResModApp == null || lPenResModApp.getIdPenaResidua() == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Nessuna pena residua su evento fittizio");
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Pena Residua su evento fittizio AGGIORNO "
							+ lPenResModApp.getIdPenaResidua());
					lPenResModApp.setFlagValidato("S");
					lPenResModApp.setEveIdEvento(aEvento.getIdEvento());

					lPenResDAO = new PenaResiduaDAO(lConn);
					// Aggiorno anche la data inserimento. Devo forzarla perche' il metodo
					// setDAOFromModelForUpdate non la aggiorna
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lEveApp.getDataInserimento() = " + lEveApp.getDataInserimento());

					lPenResDAO.setDataInserimento(lEveApp.getDataInserimento());

					lPenResDAO.setDAOFromModelForUpdate(lPenResModApp);

					lPenResDAO.update();
					lPenResDAO.stop();
				}

				// ======================================================================
				// Cancello l'evento di appoggio collegato (della richiesta)
				// ======================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Cancello l'evento di appoggio...");

				lEveDao.setIdEvento(aEventoRid.getIdEvento());
				lEveDao.setDAOFromModelForUpdate(aEventoRid);
				lEveDao.delete();
				lEveDao.stop();
			}

			// ========================================================================
			// Valido pena residua se flag app_provvisoria delle annotazioni manuali e' = A
			// e aggancia tale pena all'EVENTO effettivo di cui l'utente ha fatto la stampa
			// e di cui vede il dettaglio a video
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("lAttProv DOPO" + lAttProv);

			// if (lAttProv != null && lAttProv.equals("A"))
			// {
			// // Se esiste NON VALIDATA aggiorna la pena agganciandola all'evento corrente
			// if ( lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato())) )
			// {
			// lPenResMod.setFlagValidato("S");
			// lPenResMod.setEveIdEvento(aEvento.getIdEvento());
			//
			// lPenResDAO = new PenaResiduaDAO(lConn);
			// lPenResDAO.setDAOFromModelForUpdate(lPenResMod);
			// lPenResDAO.update();
			// lPenResDAO.stop();
			// }
			// }

			// ========================================================================
			// Nuova gestione azzeramento pena e cambiamento posizione giuridica su
			// scarcerazione provvisoria
			// ========================================================================
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("aEvento = "+aEvento);
			BigDecimal lIdPen = null;
			if ((aEvento.getCodTipoEvento().equals("01") && aEvento.getCodTipoProvvedimento().equals("09") // Ordine
																											// di
																											// scarcerazione
					&& aEvento.getCodMotivo().equals("0367") // Provvisorio per concessione Indulto
			&& aEvento.getAnnIdAnnotazioneManuale() != null // Dopo inserimento quantum
					)
					|| (aEvento.getCodTipoEvento().equals("01")
							&& aEvento.getCodTipoProvvedimento().equals("26") // Richiesta
					&& aEvento.getCodMotivo().equals("0290") // Applicazione Benefici - ex art. 174 c.p. e 672
																// c.p.p.
					)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Verifico lo stato della Pena Residua, trascorsa o ancora in decorrenza");
				boolean lIsPenaTerminata = false;
				boolean lDuplicaEAzzeraPena = false;
				boolean lAzzeraPena = false;

				// Recupero la PR puntata dall'evento
				PenaResiduaModel lPenResModApp = null;
				// lPenResSqlDAO.ricercaPenaResiduaByKey(aEvento.getPenIdPenaResidua());
				lPenResSqlDAO.ricercaPenaResiduaByKeyEvento(aEvento.getIdEvento());
				lPenResModApp = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();
				lPenResSqlDAO.stop();

				// Nessuna pena punta l'evento corrente, provo a recuperarla dall'evento
				// puntato dall'annotazione
				if (lPenResModApp == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Ricerco pena su evento puntato da annotazione");
					lAnnSqlDAO.ricercaAnnotazioneManualeByKey(aEvento.getAnnIdAnnotazioneManuale());
					Vector lAnnVec = new Vector(lAnnSqlDAO.getModels());
					lAnnSqlDAO.stop();

					lAnnMod = null;
					if (!lAnnVec.isEmpty()) {
						lAnnMod = (AnnotazioneManualeModel) lAnnVec.elementAt(0);
						lPenResSqlDAO.ricercaPenaResiduaByKeyEvento(lAnnMod.getEveIdEvento());
						lPenResModApp = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();
						lPenResSqlDAO.stop();
					} else {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Nessuna annotazione!");
					}

				}

				if (lPenResModApp != null && lPenResModApp.getIdPenaResidua() != null
						&& lPenResModApp.getDataInizio() != null && lPenResModApp.getDataFine() != null) {
					// Se esiste le pena associata all'evento corrente mi trovo
					// necessariamente dopo
					Date lOggi = DateUtils.getSysDate();

					String lGiorno = DateUtils.getDateToString(lOggi, "dd");
					String lMese = DateUtils.getDateToString(lOggi, "MM");
					String lAnno = DateUtils.getDateToString(lOggi, "yyyy");
					lOggi = DateUtils.getDate(lAnno, lMese, lGiorno);

					if (!DateUtils.isGreater(lPenResModApp.getDataFine(), lOggi)) {
						lIsPenaTerminata = true;

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Pena terminata!!! " + lPenResModApp.getIdPenaResidua());

						if (aEvento.getCodMotivo().equals("0367") && aEventoRid == null) {
							// E' gia' presente la RICH, devo duplicare la pena, azzerare i
							// quantum e agganciarla all'evento corrente
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Duplico pena su 0367 presente richiesta ");
							lDuplicaEAzzeraPena = true;
						} else if (aEvento.getCodMotivo().equals("0367") && aEventoRid != null) {
							// Esiste l'evento fittizio, quindi non e' ancora presente la RICH
							// devo semplicemente azzerare i quantum
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Azzero pena su 0367 assente RICH ");
							lAzzeraPena = true;
						} else if (aEvento.getCodMotivo().equals("0290") && aEventoRid != null) {
							// RICH, DEVE esistere un OSP altrimenti inserito prima della
							// richiesta per poter interrompere la pena
							// Se esste OSP collegato alla stessa pena non devo fare nulla
							// e' l'OSP che eventualmente ha azzerato la pena
							// Verifico se presente OSP senza AM inserito prima della RICH.
							// In questo caso il condannato e' stato gia' scarcerato
							EventoModel lEveModRic = new EventoModel();
							lEveModRic.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
							lEveModRic.setCodTipoEvento("01");
							lEveModRic.setCodTipoProvvedimento("09");
							lEveModRic.setCodMotivo("0367");
							lEveModRic.setFlagDocumentoRegistrato("S");

							lEveSqlDao = new EventoSqlDAO(lConn);
							lEveSqlDao.ricercaEvento(lEveModRic);
							lEveModRic = (EventoModel) lEveSqlDao.getModelByKey();
							lEveSqlDao.stop();
							if (lEveModRic != null && lEveModRic.getIdEvento() != null) { // Presente OSP devo
																							// azzerare i
																							// quantum di pena
								lAzzeraPena = true;
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("RICH con precedente OSP, azzero i quantum");
							} else {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("RICH senza precedente OSP, non faccio nulla");
							}
						} else if (aEvento.getCodMotivo().equals("0290") && aEventoRid == null) {
							// RICH - Manca evento fittizio non faccio nulla
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("RICH senza evento fittizio, non faccio nulla");
						}
					}
				}

				if (lIsPenaTerminata && (lAzzeraPena || lDuplicaEAzzeraPena)) { // OSP
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("Interruzione per OSP, inserisco Sospensione e aggiorno PR azzerando i quantum ");

					PenaResiduaModel lPenModIns = new PenaResiduaModel(lPenResModApp);
					;

					// Azzero i quantum di reclusione
					lPenModIns.setNumAnniReclusione(new BigDecimal(0));
					lPenModIns.setNumMesiReclusione(new BigDecimal(0));
					lPenModIns.setNumGiorniReclusione(new BigDecimal(0));

					// Azzero i quantum di Arresti
					lPenModIns.setNumAnniArresto(new BigDecimal(0));
					lPenModIns.setNumMesiArresto(new BigDecimal(0));
					lPenModIns.setNumGiorniArresto(new BigDecimal(0));

					// Azzero le date di espiazione
					lPenModIns.setDataInizio(null);
					lPenModIns.setDataFineReclusione(null);
					lPenModIns.setDataInizioArresto(null);
					lPenModIns.setDataFinePresunta(null);
					lPenModIns.setDataFine(null);

					if (lDuplicaEAzzeraPena) {
						lPenModIns.setIdPenaResidua(null);
						lPenModIns.setEveIdEvento(aEvento.getIdEvento());

						lPenModIns.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
						lPenModIns.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
						lPenModIns.setDataInserimento(aEvento.getDataInserimento());

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserisco la PR azzerandola " + lPenModIns);

						lPenResDAO = new PenaResiduaDAO(lConn);
						lPenResDAO.setDAOFromModel(lPenModIns);
						lIdPen = lPenResDAO.insert();
						lPenResDAO.stop();

						lPenModIns.setIdPenaResidua(lIdPen);
					} else if (lAzzeraPena) {
						// Aggiorno la pena residua
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Aggiorno PR azzerandola " + lPenModIns);
						lPenResDAO = new PenaResiduaDAO(lConn);
						lPenResDAO.setDAOFromModelForUpdate(lPenModIns);
						lPenResDAO.update();
						lPenResDAO.stop();
					}

					// ====================================================================
					// Preparo il record sospensione
					// ====================================================================
					SospensioneModel lSospMod = new SospensioneModel();

					lSospMod.setFasSieIdFascicoloSiep(lPenResModApp.getFasSieIdFascicoloSiep());
					lSospMod.setPenResIdPenaResidua(lPenModIns.getIdPenaResidua());

					CalendarModel lRec = lPenResModApp.getQuantumReclusione();
					CalendarModel lArr = lPenResModApp.getQuantumArresto();
					CalendarUtil lCalUtil = new CalendarUtil();
					CalendarModel lPenaEspiata = lCalUtil.sommaGiorni(lRec, lArr);

					// Inizio interruzione = data scarcerazione = data fine pena (!!!DA VERIFICARE
					lSospMod.setDataInizio(lPenResModApp.getDataFine());

					// Pena espiata
					lSospMod.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiata.getNumAnni()));
					lSospMod.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiata.getNumMesi()));
					lSospMod.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiata.getNumGiorni()));
					lSospMod.setNumGiorniLibanticipata(aGiorniLA);

					// Reclusione
					lSospMod.setNumAnniPenaResiduaReclus(new BigDecimal(0));
					lSospMod.setNumMesiPenaResiduaReclus(new BigDecimal(0));
					lSospMod.setNumGiorniPenaResiduaReclus(new BigDecimal(0));
					lSospMod.setMultaResidua(lPenResModApp.getImportoMulta());

					// Arresti
					lSospMod.setNumAnniPenaResiduaArres(new BigDecimal(0));
					lSospMod.setNumMesiPenaResiduaArres(new BigDecimal(0));
					lSospMod.setNumGiorniPenaResiduaArres(new BigDecimal(0));
					lSospMod.setAmmendaResidua(lPenResModApp.getImportoAmmenda());

					lSospMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
					lSospMod.setDataInserimento(aEvento.getDataInserimento());
					lSospMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("SOSPENSIONE - " + lSospMod);

					// Inserisco sospensione
					lSospDao = new SospensioneDAO(lConn);
					lSospDao.setDAOFromModel(lSospMod);
//					BigDecimal lKey = null;
					/*lKey = */lSospDao.insert();

					// ====================================================================
					// Aggiorno la POSIZIONE GIURIDICA in Libero
					// ====================================================================
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Cambio la posizione giuridica in Libero in quanto scarcerato");
					// Aggiorno data fine posizione
					lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
					lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aEvento.getFasSieIdFascicoloSiep());
					PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

					lPosDao = new PosizioneGiuridicaDAO(lConn);
					lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());

					lPosDao.setDataFine(lSospMod.getDataInizio());

					lPosDao.selByKey();
					lPosDao.update();
					lPosDao.stop();

					// ========================================================================
					// Inserisco Nuova Posizione Giuridica (data_inizio = data differimento)
					// agganciandola all'evento che sto validando
					// ========================================================================
					PosizioneGiuridicaModel lPosizione = new PosizioneGiuridicaModel();

					lPosizione.setCodPosizioneGiuridica("10");
					lPosizione.setDataInizio(lSospMod.getDataInizio());

					lPosizione.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lPosizione.setDataInserimento(aEvento.getDataAggiornamento());
					lPosizione.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

					lPosizione.setCodPosizioneProcessuale("-");
					lPosizione.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
					lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());

					lPosDao.setDAOFromModel(lPosizione);
					lPosDao.insert();
					lPosDao.stop();

				}

			}

			// ============================================================================
			// AGGIORNO LO STATO DEL PROCEDIMENTO ed eventualmente aggiorno lo scadenzario
			// solo se annotazioni con anticipazione
			// ============================================================================
			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			if (aEvento.getCodMotivo().equals("0296") || aEvento.getCodMotivo().equals("0295")) {
				// 0295 - Restituzione Ordine di Esecuzione ex art. 673 c.p.p.
				// 0296 - Restituzione Ordine di Esecuzione ex art.672 c.p.p.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("aEvento.getCodMotivo().equals(0296) || aEvento.getCodMotivo().equals(0295)");

				lStatoProcMod.setCodStatoProcedimento("0126");
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("altri casi");

				//
				if (lAttProv != null && lAttProv.equals("A")) {
					ScadenzarioModel lScaMod = null;
					lScaSqlDAO = new ScadenzarioSqlDAO(lConn);
					lScaSqlDAO.ricercaScadenzarioFinePenaVaneRicerche(aFascicolo.getIdFascicoloSiep());
					lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

					if (lScaMod != null) {
						// aggiorna scadenzario
						lScaDAO = new ScadenzarioDAO(lConn);

						lScaDAO.setDataFineScadenza(lPenResMod.getDataFine());
						lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
						lScaMod.setFlagVisto("N");

						// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
						lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
						lScaDAO.update();
						lScaDAO.stop();
					}

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("altri casi 0125" + lStatoProcMod.getCodStatoProcedimento());
					lStatoProcMod.setCodStatoProcedimento("0125");
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("altri casi 0124" + lStatoProcMod.getCodStatoProcedimento());
					lStatoProcMod.setCodStatoProcedimento("0124");
				}
			}

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoDao.setDAOFromModel(lStatoProcMod);

			lStatoDao.insert();
			lStatoDao.stop();

			// ========================================
			// Aggiorna tabella nome_provvedimento
			// ========================================
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento(lCodiceNomeProvv);
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// =======================================
			// Aggiorno il BLOB
			// =======================================
			// lConnBlob = getDBConnection();
			// Collego l'evento all'ultima pena residua validata che puo' essere quella
			// collegata all'evento oppure l'ultima validata in assoluto.
			if (lIdPen != null) {
				aEvento.setPenIdPenaResidua(lIdPen);
			} else if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null
					&& "S".equals(lPenResMod.getFlagValidato())) { // n.b. l'aggancio viene effettuato solo se
																	// la pena e' validata
				aEvento.setPenIdPenaResidua(lPenResMod.getIdPenaResidua());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("[=======================================]");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("->Monitor : " + aEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("[=======================================]");

			// lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob = new EventoDAO(lConn);

			/*
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() if( aEvento.getDocBlobIn() != null )
			 * siesLogger.debug("#### ->Monitor size docblob : " + aEvento.getDocBlobIn().available());
			 */

			lEveDaoBlob.setDAOFromModelForUpdate(aEvento);
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			/*
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.debug("#### ->Monitor size docblob II : " +
			 * aEvento.getDocBlobIn().available()); lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			 * lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento()); lEveDaoBlob.update();
			 * lEveDaoBlob.stop();
			 */

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiesteConCodice : " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			// rollback(lConnBlob);
//			sqe.printStackTrace();
//			throw new F3BException("RichiestaController.ExUpdateValidaRichiesteConCodice : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiesteConCodice : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lAnnDAO);
			cleanup(lAnnSqlDAO);
			cleanup(lPenResSqlDAO);
			cleanup(lPenResDAO);
			cleanup(lStatoDao);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lNomProvvDAO);
			cleanup(lFungDao);
			cleanup(lFungSqlDao);

			cleanup(lSospSqlDao);
			cleanup(lSospDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);

			cleanup(lEveSqlDao);

			cleanup(lEveDaoBlob);
			// cleanup(lConnBlob);

			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("######## fine ########");

		return lEveMod;
	}

	/**
	 * Metodo Utilizzato per la Validazione Delle Comunicazioni emesse in caso di Richieste al GE o Decisione
	 * del GE riguardanti l'applicazione di Amnistia/Indulto, Depenalizzazione e Incostituzionalita' Vengono
	 * validati i seguenti dati: - Comunicazione - Ordinanza di concessione (solo decisione del GE) -
	 * Provvedimento di concessione (solo decisione del GE) - Annotazioni Manuali collegate alla Richiesta o
	 * al Provvedimento - Pena Residua (ultima inserita) Viene aggiornata la tabella Provvedimento
	 *
	 * @param aEventoRid
	 *            - Richiesta o Provvedimento associato alla Comunicazione
	 * @param aEvento
	 *            - Comunicazione
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaEmissioneComunicazioni(EventoModel aEventoRid, EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaDAO lPenResDAO = null;
		PenaResiduaSqlDAO lPenResSqlDAO = null;
		AnnotazioneManualeDAO lAnnDAO = null;
		AnnotazioneManualeSqlDAO lAnnSqlDAO = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		FungibilitaDAO lFungDao = null;
		FungibilitaSqlDAO lFungSqlDao = null;

//		Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		PenaResiduaModel lPenResMod = null;
		AnnotazioneManualeModel lAnnMod = null;

		try {
			lConn = getDBTransaction();

			EventoModel lEveApp = new EventoModel();

			// ========================================================================
			// Recupero l'evento Comunicazione (sto codice non serve a una mazza)
			// ========================================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
			}

			// ========================================================================
			// Cerca l'ultima Pena Residua trovata a sistema (validata o meno)
			// ========================================================================
			lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDAO.ricercaPenaResiduaByIdFascicoloDataDesc(aEvento.getFasSieIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

			// ========================================================================
			// Se Comunicazione Concessione Indulto o Amnistia, ricerca l'ordinanza
			// del GE e la valida (Decisione del GE)
			// ========================================================================
			if (aEvento.getCodMotivo().equals("0301") || aEvento.getCodMotivo().equals("0300")) {
				String[] motivi = { "0284", "0285", "0286" };

				EventoModel EveOrd = new EventoModel();
				EveOrd.setCodTipoEvento("01");
				EveOrd.setCodTipoProvvedimento("03");
				EveOrd.setCodMotivo(aEvento.getCodMotivo());
				EveOrd.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());

				lEveSqlDao = new EventoSqlDAO(lConn);
				lEveSqlDao.ricercaEventoPerMotivo(motivi, EveOrd);
				EventoModel EveOrdRic = (EventoModel) lEveSqlDao.getModelByKey();

				lEveDao.stop();

				// Valido l'Ordinanza
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selCondizioneUpdate(EveOrdRic.getIdEvento());
				lEveDao.update();
				lEveDao.stop();
			}

			// valido annotazione legata all'evento
			lAnnDAO = new AnnotazioneManualeDAO(lConn);
			lAnnMod = new AnnotazioneManualeModel();

			// ========================================================================
			// Recupero le annotazioni legate al provvedimento di concessione o alla
			// richiesta per validarle
			// ========================================================================

			if (aEventoRid != null) {
				lAnnSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
				lAnnSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEventoRid.getIdEvento());
//				String lAttProv = null;

				Vector lAnnVec = new Vector(lAnnSqlDAO.getModels());
				for (int y = 0; y < lAnnVec.size(); y++) {
					lAnnMod = (AnnotazioneManualeModel) lAnnVec.get(y);
					if (lAnnMod.getFlagAppProvvisoria().equals("A"))
//						lAttProv = lAnnMod.getFlagAppProvvisoria();

					// Se Decisioni del GE (vengo da 'Rideterminazione della Pena')
					if ("26".equals(aEventoRid.getCodTipoProvvedimento())) {
						// Se esiste non validata lega l'annotazione
						// all'EVENTO effettivo di cui l'utente ha fatto la stampa
						// e di cui vede il dettaglio a video
						// if ( lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato())) )
						if (aEventoRid.getAnnIdAnnotazioneManuale() == null) {
							lAnnMod.setEveIdEvento(aEvento.getIdEvento());
						}
					}

					lAnnMod.setFlagValidato("S");

					lAnnMod.setDataAggiornamento(aEventoRid.getDataAggiornamento());
					lAnnMod.setCodOperatoreAggiornamento(aEventoRid.getCodOperatoreAggiornamento());
					lAnnMod.setCodUfficioAggiornamento(aEventoRid.getCodUfficioAggiornamento());

					lAnnDAO.setDAOFromModelForUpdate(lAnnMod);
					lAnnDAO.update();
					lAnnDAO.stop();
				}
			}

			// ========================================================================
			// Effettua aggiornamento (VALIDAZIONE) della Richiesta o Provvedimento
			// di Concessione se Decisioni del GE o la cancellazione dell'evento
			// d'appoggio se Richieste al GE (da 'Rideterminazione della Pena')
			// ========================================================================
			if (aEventoRid != null) {
				if ("04".equals(aEventoRid.getCodTipoProvvedimento())) {
					aEventoRid.setFlagDocumentoRegistrato("S");

					lEveDao.setIdEvento(aEventoRid.getIdEvento());
					lEveDao.setDAOFromModelForUpdate(aEventoRid);
					lEveDao.update();
					lEveDao.stop();
				} else // CASO COD_TIPO_PROVVEDIMENTO = 26
				{
					// Ricerco la fungibilita' legata all'evento di appoggio
					// che deve essere cancellato
					lFungSqlDao = new FungibilitaSqlDAO(lConn);
					lFungSqlDao.ricercaFungibilitaByKeyEvento(aEventoRid.getIdEvento());
					FungibilitaModel lFunMod = (FungibilitaModel) lFungSqlDao.getModelByKey();

					// Se almeno una presente viene legata allo stesso evento
					// a cui si lega la pena residua
					if (lFunMod != null) {
						lFungDao = new FungibilitaDAO(lConn);

						lFungDao.setEveIdEvento(aEvento.getIdEvento());
						lFungDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lFungDao.setDataAggiornamento(aEvento.getDataAggiornamento());
						lFungDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

						// Al momento potrebbe esserci piu' di un record di Fungibilita'
						// per lo stesso evento,
						// quindi la condizione di update viene posta sul campo EVE_ID_EVENTO
						lFungDao.setCondizioneUpdateEveIdEvento(aEventoRid.getIdEvento());
						lFungDao.update();
						lFungDao.stop();
					}

					// ========================================================================
					// Cancello l'evento di appoggio collegato (della richiesta)
					// ========================================================================
					lEveDao.setIdEvento(aEventoRid.getIdEvento());
					lEveDao.setDAOFromModelForUpdate(aEventoRid);
					lEveDao.delete();
					lEveDao.stop();
				}
			}

			// ========================================================================
			// Validazione Ultima Pena Residua trovata a sistema
			// ========================================================================
			lPenResDAO = new PenaResiduaDAO(lConn);

			if (aEventoRid != null && "26".equals(aEventoRid.getCodTipoProvvedimento())) {
				// Se esiste non validata aggiorna la pena
				if (lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato()))) {
					lPenResMod.setFlagValidato("S");
					lPenResMod.setEveIdEvento(aEvento.getIdEvento());

					lPenResDAO.setDAOFromModelForUpdate(lPenResMod);
					lPenResDAO.update();
					lPenResDAO.stop();
				}
			} else {
				// ---rework INDULTO - Validazioni Comunicaizoni ---
				// Hanno richiesto di validare sempre la pena residua per cui
				// Commento l'if sull'annotazione manuale e faccio eseguire sempre
				// la validazione

				// valido pena residua se flag app_provvisoria delle annotazioni manuali e' = A
				// --REW-INDULTO if (lAttProv != null && lAttProv.equals("A"))
				// --REW-INDULTO {
				lPenResMod.setFlagValidato("S");
				// lPenResMod.setEveIdEvento(aEvento.getIdEvento());
				lPenResDAO.setDAOFromModelForUpdate(lPenResMod);
				lPenResDAO.update();
				lPenResDAO.stop();
				// --REW-INDULTO }
			}

			// inserisco nome provvedimento
			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);
			if (aEvento.getCodMotivo().equals("0301")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP094");
			}
			if (aEvento.getCodMotivo().equals("0300")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP093");
			}

			if (aEvento.getCodMotivo().equals("0298")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP091");
			}

			if (aEvento.getCodMotivo().equals("0299")) {
				lNomProvvDAO.setCodNomeProvvedimento("NP092");
			}

			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ======================================
			// Aggiornamento della Comunicazione
			// ======================================
			// 2010-10-20 : Rimozione doppia connection
			// lConnBlob = getDBConnection();

			lEveDaoBlob = new EventoDAO(lConn);
			// 2010-10-20 : Rimozione doppia connection
			// lEveDaoBlob = new EventoDAO(lConnBlob);

			if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
				aEvento.setPenIdPenaResidua(lPenResMod.getIdPenaResidua());
			}

			lEveDaoBlob.setDAOFromModelForUpdate(aEvento);
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// 2011-05-05 - Commentato per rimozione baco di svuotamento blob su segnalazione LECCE
			/*
			 * lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			 * lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento()); lEveDaoBlob.update();
			 * lEveDaoBlob.stop();
			 */
			// ---------------------

			// 2010-10-20 : Rimozione doppia connection
			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// 2010-10-20 : Rimozione doppia connection
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaEmissioneComunicazioni : " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			// 2010-10-20 : Rimozione doppia connection
//			// rollback(lConnBlob);
//			sqe.printStackTrace();
//			throw new F3BException("RichiestaController.ExUpdateValidaEmissioneComunicazioni : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// 2010-10-20 : Rimozione doppia connection
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaEmissioneComunicazioni : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lAnnDAO);
			cleanup(lAnnSqlDAO);
			cleanup(lPenResSqlDAO);
			cleanup(lPenResDAO);
			cleanup(lNomProvvDAO);
			cleanup(lFungDao);
			cleanup(lFungSqlDao);

			cleanup(lEveDaoBlob);
			// 2010-10-20 : Rimozione doppia connection
			// cleanup(lConnBlob);

			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * Inserisce/Aggiorna l'evento e le notifiche
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciOModificaNotifica(EventoNotificaModel aEvento) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		PenaResiduaDAO lPenaResDao = null;

		BigDecimal lKeyEvento = null;
		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);
			lCampoNotaDao = new CampoNotaDAO(lConn);

			// ========================================================================
			// Recupero l'ultimo evento dello stesso tipo di quello passato in input
			// non validato. Se non lo trovo lo aggiorno altrimenti lo inserisco.
			// ========================================================================
			lSqlDAO = new EventoSqlDAO(lConn);
			lSqlDAO.ricercaEventoNonRegistrato(aEvento.getEvento());
			EventoModel lEve = (EventoModel) lSqlDAO.getModelByKey();
			if (lEve != null) {
				lKeyEvento = lEve.getIdEvento();
				aEvento.getEvento().setIdEvento(lKeyEvento);

				lEveDao.setDAOFromModelForUpdate(aEvento.getEvento());
				lEveDao.update();
				lEveDao.stop();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
			} else {
				BigDecimal lProgr = lSqlDAO.getProgressivo(aEvento.getEvento());
				aEvento.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

				lEveDao.setDAOFromModel(aEvento.getEvento());

				lKeyEvento = lEveDao.insert();
				lEveRet.getEvento().setIdEvento(lKeyEvento);
			}
			// ************************************************************************

			// ========================================================================
			// Cancello tutte le notifiche precedentemente presenti sull'evento e le
			// reinserisco
			// ========================================================================
			lNotDao.setCondizioneEvento(lKeyEvento);
			lNotDao.delete();
			lNotDao.stop();

			BigDecimal lKeyAutorita = null;
			int count = 0;

			while (count < aEvento.getNotifiche().length) {
				if (aEvento.getNotifiche()[count] != null) {
					if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
						lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
						AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
						lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

						if (lAutMod == null) {
							lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
							lKeyAutorita = lAutDao.insert();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						} else {
							lKeyAutorita = lAutMod.getIdAutoritaEsterna();
							aEvento.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
						}
					}

					aEvento.getNotifiche()[count].setEveIdEvento(lKeyEvento);

					lNotDao.setDAOFromModel(aEvento.getNotifiche()[count]);
					lNotDao.insert();
					lNotDao.stop();
				}
				count++;
			}

			// ========================================================================
			// Inserimento delle eventuali note aggiuntive.
			// ========================================================================
			if (aEvento.getCampoNote() != null) {
				count = 0;
				while (count < aEvento.getCampoNote().length) {
					aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("RichiestaController.ExInserisciOModificaNotifica: " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			throw new F3BException("RichiestaController.ExInserisciOModificaNotifica: " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("RichiestaController.ExInserisciOModificaNotifica: " + ex);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenaResDao);

			cleanup(lConn);
		}

		return lEveRet;
	}

	/**
	 * <p>
	 * Valida la Richiesta al GE di <i>'quantificare gli aumenti per i singoli reati posti in continuazione
	 * con il reato principale'</i> nel caso di Depenalizzazione e Incostituzionalita' (artt 671 e 673 cpp).
	 * </p>
	 * Se ancora presente l'evento di appoggio (aEventoRid):<br>
	 * - sgancia le annotazioni da tale evento e le riaggancia all'evento da validare<br>
	 * - sgancia la fungibilita' da tale evento e la riaggancia all'evento da validare<br>
	 * - sgancia la pena residua da tale evento e la riaggancia all'evento da validare<br>
	 * - elimina l'evento di appoggio <br>
	 * 
	 * @param aEventoRid
	 *            - Evento di appoggio a cui e' collegata l'annotazione (se presente)
	 * @param aEvento
	 *            - Evento corrente da validare
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaRichDetPenAboReato(EventoModel aEventoRid, EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		AnnotazioneManualeDAO lAnnDAO = null;
		AnnotazioneManualeSqlDAO lAnnSqlDAO = null;
		PenaResiduaDAO lPenResDAO = null;
		PenaResiduaSqlDAO lPenResSqlDAO = null;
		StatoProcedimentoDAO lStatoDao = null;
		ScadenzarioSqlDAO lScaSqlDAO = null;
		ScadenzarioDAO lScaDAO = null;
		FungibilitaDAO lFungDao = null;
		FungibilitaSqlDAO lFungSqlDao = null;

		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		PenaResiduaModel lPenResMod = null;
		AnnotazioneManualeModel lAnnMod = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveDao = new EventoDAO(lConn);
//			EventoSqlDAO lEveSqlDAO = new EventoSqlDAO(lConn);
			EventoModel lEveApp = new EventoModel();

			lEveDao.setIdEvento(aEvento.getIdEvento());
			lEveDao.selByKey();

			lEveDao.start();
			if (lEveDao.next()) {
				lEveApp.setIdEvento(lEveDao.getIdEvento());
				lEveApp.setDataEmissione(lEveDao.getDataEmissione());
				// MEV37 Inizio - 01/03/2016
				lEveApp.setDataInserimento(lEveDao.getDataInserimento());
				// MEV37 Inizio - Fine
			}

			// Cerca Ultima penaResidua
			lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDAO.ricercaPenaResiduaByIdFascicoloDataDesc(aFascicolo.getIdFascicoloSiep());
			lPenResMod = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

			lAnnDAO = new AnnotazioneManualeDAO(lConn);
			lAnnMod = new AnnotazioneManualeModel();

			// ========================================================================
			// Ricerco le annotazioni manuali e le Valido
			// ========================================================================
			String lAttProv = null;
			if (aEventoRid != null) {
				lAnnSqlDAO = new AnnotazioneManualeSqlDAO(lConn);
				lAnnSqlDAO.ricercaAnnotazioneManualeByIdEvento(aEventoRid.getIdEvento());
				Vector lAnnVec = new Vector(lAnnSqlDAO.getModels());
				for (int y = 0; y < lAnnVec.size(); y++) {
					lAnnMod = (AnnotazioneManualeModel) lAnnVec.get(y);
					if (lAnnMod.getFlagAppProvvisoria().equals("A"))
						lAttProv = lAnnMod.getFlagAppProvvisoria();

					// Se esiste non validata lega l'annotazione
					// all'EVENTO effettivo di cui l'utente ha fatto la stampa
					// e di cui vede il dettaglio a video
					// if ( lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato())) )
					if (aEventoRid.getAnnIdAnnotazioneManuale() == null) {
						lAnnMod.setEveIdEvento(aEvento.getIdEvento());
					}

					lAnnMod.setFlagValidato("S");
					lAnnMod.setDataAggiornamento(aEventoRid.getDataAggiornamento());
					lAnnMod.setCodOperatoreAggiornamento(aEventoRid.getCodOperatoreAggiornamento());
					lAnnDAO.setDAOFromModelForUpdate(lAnnMod);
					lAnnDAO.update();
					lAnnDAO.stop();
				}
			}

			if (aEventoRid != null) {
				// Ricerco la fungibilita' legata all'evento di appoggio
				// che deve essere cancellato
				lFungSqlDao = new FungibilitaSqlDAO(lConn);
				lFungSqlDao.ricercaFungibilitaByKeyEvento(aEventoRid.getIdEvento());
				FungibilitaModel lFunMod = (FungibilitaModel) lFungSqlDao.getModelByKey();

				// Se almeno una presente viene legata allo stesso evento
				// a cui si lega la pena residua
				if (lFunMod != null) {
					lFungDao = new FungibilitaDAO(lConn);

					lFungDao.setEveIdEvento(aEvento.getIdEvento());
					lFungDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lFungDao.setDataAggiornamento(aEvento.getDataAggiornamento());
					lFungDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());

					// Al momento potrebbe esserci piu' di un record di Fungibilita'
					// per lo stesso evento,
					// quindi la condizione di update viene posta sul campo EVE_ID_EVENTO
					lFungDao.setCondizioneUpdateEveIdEvento(aEventoRid.getIdEvento());
					lFungDao.update();
					lFungDao.stop();
				}
				// AMBROS 08/11/2013 MEV8 -->

				// ========================================================================
				// Verifico se e' stato effettuato un calcolo della pena sulla richiesta
				// in questo caso devo sganciare il record PENA_RESIDUA e agganciarlo
				// all'evento corrente da validare
				// ========================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Verifico se presenti PR su evento fittizio");
				lPenResSqlDAO = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDAO.ricercaPenaResiduaByKeyEvento(aEventoRid.getIdEvento());
				PenaResiduaModel lPenResModApp = null;
				lPenResModApp = (PenaResiduaModel) lPenResSqlDAO.getModelByKey();

				if (lPenResModApp == null || lPenResModApp.getIdPenaResidua() == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Nessuna pena residua su evento fittizio");
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Pena Residua su evento fittizio AGGIORNO "
							+ lPenResModApp.getIdPenaResidua());
					lPenResModApp.setFlagValidato("S");
					lPenResModApp.setEveIdEvento(aEvento.getIdEvento());

					lPenResDAO = new PenaResiduaDAO(lConn);
					// Aggiorno anche la data inserimento. Devo forzarla perche' il metodo
					// setDAOFromModelForUpdate non la aggiorna
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lEveApp.getDataInserimento() = " + lEveApp.getDataInserimento());

					lPenResDAO.setDataInserimento(lEveApp.getDataInserimento());

					lPenResDAO.setDAOFromModelForUpdate(lPenResModApp);

					lPenResDAO.update();
					lPenResDAO.stop();
				}

				// --> END AMBROS

				// ========================================================================
				// Cancello l'evento di appoggio collegato (della richiesta)
				// ========================================================================
				lEveDao.setIdEvento(aEventoRid.getIdEvento());
				lEveDao.setDAOFromModelForUpdate(aEventoRid);
				lEveDao.delete();
				lEveDao.stop();
			}

			// valido pena residua se flag app_provvisoria delle annotazioni manuali e' = A
			if (lAttProv != null && lAttProv.equals("A")) {
				// Se esiste non validata aggiorna la pena
				if (lPenResMod != null && (!"S".equals(lPenResMod.getFlagValidato()))) {
					lPenResMod.setFlagValidato("S");
					lPenResMod.setEveIdEvento(aEvento.getIdEvento());

					lPenResDAO = new PenaResiduaDAO(lConn);
					lPenResDAO.setDAOFromModelForUpdate(lPenResMod);
					lPenResDAO.update();
					lPenResDAO.stop();
				}
			}

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			lStatoDao = new StatoProcedimentoDAO(lConn);
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			lStatoDao.delete();

			if (lAttProv != null && lAttProv.equals("A")) {
				lScaSqlDAO = new ScadenzarioSqlDAO(lConn);

				ScadenzarioModel lScaMod = null;

				lScaSqlDAO.ricercaScadenzarioFinePenaVaneRicerche(aFascicolo.getIdFascicoloSiep());
				lScaMod = (ScadenzarioModel) lScaSqlDAO.getModelByKey();

				if (lScaMod != null) {
					// aggiorna scadenzario
					lScaDAO = new ScadenzarioDAO(lConn);
					lScaDAO.setDataFineScadenza(lPenResMod.getDataFine());
					lScaDAO.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lScaDAO.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lScaDAO.setDataAggiornamento(aEvento.getDataAggiornamento());
					lScaMod.setFlagVisto("N");

					// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
					lScaDAO.setCondizioneUpdate(lScaMod.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				}

				lStatoProcMod.setCodStatoProcedimento("0125");
			} else {
				lStatoProcMod.setCodStatoProcedimento("0124");
			}

			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setData(lEveApp.getDataEmissione());
			lStatoDao.setDAOFromModel(lStatoProcMod);

			lStatoDao.insert();
			lStatoDao.stop();

			// inserisco nome provvedimento

			// Aggiorna tabella nome_provvedimento
			lNomProvvDAO = new NomeProvvedimentoDAO(lConn);

			lNomProvvDAO.setCodNomeProvvedimento("NP087");
			lNomProvvDAO.setEveIdEvento(aEvento.getIdEvento());

			lNomProvvDAO.insert();
			lNomProvvDAO.stop();

			// ------- EVENTO--------
			// lConnBlob = getDBConnection();

			if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
				aEvento.setPenIdPenaResidua(lPenResMod.getIdPenaResidua());
			}

			// lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdate(aEvento);
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// 2010-10-20 : Rimozione per retaggio della doppia connection.
			/*
			 * lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			 * lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento()); lEveDaoBlob.update();
			 * lEveDaoBlob.stop();
			 */
			// ---------------------

			// commit(lConnBlob);
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaAccReato : " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			// rollback(lConnBlob);
//			sqe.printStackTrace();
//			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaAccReato : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaAccReato : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lAnnDAO);
			cleanup(lAnnSqlDAO);
			cleanup(lPenResSqlDAO);
			cleanup(lPenResDAO);
			cleanup(lStatoDao);
			cleanup(lScaSqlDAO);
			cleanup(lScaDAO);
			cleanup(lNomProvvDAO);
			cleanup(lFungDao);
			cleanup(lFungSqlDao);

			cleanup(lEveDaoBlob);
			cleanup(lConn);
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	/**
	 * VAlida al richiesta Generica
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaRichiestaGenerica(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Aggiorna Inserisci PENA_RESIDUA
			InserimentoAggiornamentoPenRes(lConn, lEveModel, aFascicolo.getIdFascicoloSiep());

			// SETTA LO STATO PROCEDIMENTO
			if (lEveModel != null && lEveModel.getCodMotivo() != null) {
				String lMotivo = lEveModel.getCodMotivo();
				String lStatoProcMod = null;

				//16/05/2016 Modifica per integrazione MEV2
		        String lTipoUffDest = "";
		        if (lEveModel.getCodTipoUfficioDestinatario() != null)
		        	lTipoUffDest=lEveModel.getCodTipoUfficioDestinatario();
				//16/05/2016 Modifica per integrazione MEV2

				if (lMotivo.equals("0320")) {
					lStatoProcMod = "0141";
				} else if (lMotivo.equals("0321")) {
					lStatoProcMod = "0142";
				} else if (lMotivo.equals("0324")) {
					lStatoProcMod = "0143";
				} else if (lMotivo.equals("0325")) {
					lStatoProcMod = "0144";
				} else if (lMotivo.equals("0344")) {
					lStatoProcMod = "0145";
				} else if (lMotivo.equals("0328")) {
					lStatoProcMod = "0146";
				} else if (lMotivo.equals("0329")) {
					lStatoProcMod = "0147";
				} else if (lMotivo.equals("0330")) {
					lStatoProcMod = "0148";
				} else if (lMotivo.equals("0331")) {
					lStatoProcMod = "0149";
				} else if (lMotivo.equals("0346")) {
					lStatoProcMod = "0150";
				} else if (lMotivo.equals("0348")) {
					lStatoProcMod = "0151";
				} else if (lMotivo.equals("0349")) {
					lStatoProcMod = "0152";
				} else if (lMotivo.equals("0340")) {
					lStatoProcMod = "0139";
				} else if (lMotivo.equals("2110")) {
					// 16/05/2016 Modifica per integrazione MEV2
		        	if (lTipoUffDest.equals("UDS"))
		        		lStatoProcMod = "0510";	// Rich accertamento pericolosità sociale all'UDS
		        	else if (lTipoUffDest.equals("UDSM"))
		        		lStatoProcMod = "0552";	// Rich accertamento pericolosità sociale all'UDS Minori
		            // 16/05/2016 FINE Modifica per integrazione MEV2
				} else if (lMotivo.equals("2114")) {
					// 16/05/2016 Modifica per integrazione MEV2
		        	if (lTipoUffDest.equals("UDS"))
						lStatoProcMod = "0511"; // Rich accertamento pericolosità sociale e unificazione
												// Misure Sicurezza all'UDS
		        	else if (lTipoUffDest.equals("UDSM"))
						lStatoProcMod = "0553"; // Rich accertamento pericolosità sociale e unificazione
												// Misure Sicurezza all'UDS Minori
		            // 16/05/2016 FINE Modifica per integrazione MEV2
				} else
				// 06/04/2010 Revisione Codici Motivo per Pene Accessorie.
				// if (lMotivo.equals("5133")){
				if (lMotivo.equals("5403")) {
					lStatoProcMod = "0153";
				}
				if (lStatoProcMod != null) {
					BigDecimal lKeyEvento = lEveModel.getIdEvento();
					InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(),
							lEveModel, lStatoProcMod, lKeyEvento);
				}
			}

			// ------- EVENTO--------
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaGenerica : " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			sqe.printStackTrace();
//			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaGenerica : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaGenerica : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lConn);
		}

		return lEveMod;
	}

	/**
	 * VAlida al richiesta esito espulsione
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaRichiestaEsitoEspulsione(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;

		EventoModel lEveMod = new EventoModel(aEvento);
		// 2010-10-20 : Rimozione doppia connection.
		// Connection lConnBlob = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// EVENTO
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// POSIZIONE_GIURIDICA
			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			lPosSqlDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
			PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			lPosSqlDao.inserimentoAggiornamentoPosizioneGiuridica(lConn, lPosMod.getCodPosizioneGiuridica(),
					lPosMod, lEveModel.getDataEmissione(), lEveModel, aFascicolo.getIdFascicoloSiep(),
					aEvento.getIdEvento(), "S");

			/*
			 * lPosDao.setIdPosizioneGiuridica(lPosMod.getIdPosizioneGiuridica());
			 * lPosDao.setDataFine(lEveModel.getDataEmissione());
			 * 
			 * lPosDao.selByKey(); lPosDao.update(); lPosDao.stop();
			 * 
			 * //inserimento nuova occorrenza PosizioneGiuridicaModel lPosizione = new
			 * PosizioneGiuridicaModel();
			 * 
			 * lPosizione.setCodPosizioneGiuridica(lPosMod.getCodPosizioneGiuridica());
			 * lPosizione.setDataInizio(lEveModel.getDataEmissione());
			 * 
			 * lPosizione.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			 * lPosizione.setDataInserimento(aEvento.getDataAggiornamento());
			 * lPosizione.setCodPosizioneProcessuale("-");
			 * lPosizione.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			 * lPosizione.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			 * lPosizione.setIdEventoRiferimento(aEvento.getIdEvento());
			 * 
			 * lPosDao.setDAOFromModel(lPosizione); lPosDao.insert(); lPosDao.stop();
			 */

			// Aggiorna Inserisci PENA_RESIDUA
			InserimentoAggiornamentoPenRes(lConn, aEvento, aFascicolo.getIdFascicoloSiep());

			// ------- EVENTO--------
			// 2010-10-20 : Rimozione doppia connection.
			// lConnBlob = getDBConnection();
			// 2010-10-20 : Rimozione doppia connection.
			// lEveDaoBlob = new EventoDAO(lConnBlob);
			lEveDaoBlob = new EventoDAO(lConn);

			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);
			// 2010-10-20 : Rimozione doppia connection.
			// commit(lConnBlob);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			// 2010-10-20 : Rimozione doppia connection.
			// rollback(lConnBlob);

			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaEsitoEspulsione : " + daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			// 2010-10-20 : Rimozione doppia connection.
//			// rollback(lConnBlob);
//			sqe.printStackTrace();
//			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaEsitoEspulsione : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			// 2010-10-20 : Rimozione doppia connection.
			// rollback(lConnBlob);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExUpdateValidaRichiestaEsitoEspulsione : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lEveDaoBlob);

			cleanup(lConn);

			// 2010-10-20 : Rimozione doppia connection.
			// cleanup(lConnBlob);
		}

		return lEveMod;
	}

	// METODI PRIVATI
	/**
	 * Inserimento Aggiornamento Pena Residua
	 * 
	 * @param lConn
	 * @param lEveModel
	 * @param aKey
	 * @return
	 * @throws DAOException
	 * @throws F3BException
	 */
	private PenaResiduaModel InserimentoAggiornamentoPenRes(Connection lConn, EventoModel lEveModel,
			BigDecimal aKey) throws DAOException, F3BException {
		PenaResiduaDAO lPenResDao = new PenaResiduaDAO(lConn);
		PenaResiduaSqlDAO lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
		PenaResiduaModel lPenResMod = new PenaResiduaModel();

		try {
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			if (lPenResSqlDao != null) {
				lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
				if (lPenResMod != null) {
					if (lPenResMod.getEveIdEvento() == null) {
						lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
						lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
						lPenResDao.setFlagValidato("S");
						lPenResDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
						lPenResDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
						lPenResDao.setDataAggiornamento(DateUtils.getSysDate());
						lPenResDao.selByKey();
						lPenResDao.update();
						lPenResDao.stop();
					} else {
						lPenResDao.setDAOFromModel(lPenResMod);
						lPenResDao.setFlagValidato("S");
						lPenResDao.setEveIdEvento(lEveModel.getIdEvento());
						lPenResDao.setDataInserimento(DateUtils.getSysDate());
						lPenResDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
						lPenResDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
						lPenResDao.insert();
						lPenResDao.stop();
					}
				}
			}
		} finally {
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
		}
		return lPenResMod;
	}

	/**
	 * Inserimento Cancellazione Stato Procedimento
	 * 
	 * @param lConn
	 * @param aKey
	 * @param lEveModel
	 * @param lStatoProcMod
	 * @param aKeyEvento
	 * @throws DAOException
	 * @throws F3BException
	 */
	private void InserimentoCancellazioneStatoProcedimento(Connection lConn, BigDecimal aKey,
			EventoModel lEveModel, String lStatoProcMod, BigDecimal aKeyEvento) throws DAOException,
			F3BException {
		StatoProcedimentoDAO lStatoDao = new StatoProcedimentoDAO(lConn);
		try {
			// cancellazione
			lStatoDao.setCondizioneByIdFascicolo(aKey);
			lStatoDao.delete();
			lStatoDao.stop();
			// inserimento
			lStatoDao.setProgressivo(new BigDecimal(1));
			lStatoDao.setData(lEveModel.getDataEmissione());
			lStatoDao.setCodStatoProcedimento(lStatoProcMod);
			lStatoDao.setFasSieIdFascicoloSiep(aKey);
			lStatoDao.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
			lStatoDao.setDataInserimento(lEveModel.getDataAggiornamento());
			lStatoDao.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
			if (aKeyEvento != null)
				lStatoDao.setEveIdEvento(aKeyEvento);

			lStatoDao.insert();
		} finally {
			cleanup(lStatoDao);
		}
	}

	/*****************************************************************************
	 * Cancellazione Trasmissione per Competenza
	 * 
	 * @param EventoNotificaModel
	 *            aEveModel
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaTrasmissioneCompetenza(EventoNotificaModel lEveMod) throws F3BException {
		Connection lConn = null;

		EventoDAO lEventoDAO = null;
		CampoNotaDAO lCampoNotaDAO = null;
		CompetenzaDAO lCompDAO = null;
		NotificaDAO lNotDAO = null;

		try {
			lConn = getDBTransaction();
			BigDecimal IdEve = lEveMod.getEvento().getIdEvento();

			// Competenza
			lCompDAO = new CompetenzaDAO(lConn);
			lCompDAO.setCondizioneEvento(IdEve);
			lCompDAO.delete();
			lCompDAO.stop();

			// Campo Nota
			lCampoNotaDAO = new CampoNotaDAO(lConn);
			lCampoNotaDAO.setCondizioneEvento(IdEve);
			lCampoNotaDAO.delete();
			lCampoNotaDAO.stop();

			// Notifiche
			lNotDAO = new NotificaDAO(lConn);
			for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
				NotificaModel lNotMod = new NotificaModel();
				lNotMod = lEveMod.getNotifiche()[i];

				if (lNotMod != null) {
					lNotDAO.setCondizioneEvento(IdEve);
					lNotDAO.delete();
				}
			}
			lNotDAO.stop();

			// Evento
			lEventoDAO = new EventoDAO(lConn);
			lEventoDAO.selCondizioneUpdate(IdEve);
			lEventoDAO.delete();
			lEventoDAO.stop();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(
					"RichiestaController.ExCancellaTrasmissioneCompetenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lEventoDAO);
			cleanup(lCampoNotaDAO);
			cleanup(lNotDAO);
			cleanup(lCompDAO);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Aggiornamento dello stato del procedimento a seguito della presa in carico per competenza nella stessa
	 * BDI
	 * 
	 * 
	 * @throws F3BException
	 ****************************************************************************/

	public void ExUpdStatoProcPresaincaricoStessaBDI(FascicoloSiepModel aFascicolo, Date dataEmissione,
			Date dataInserimento, String codOperatore, String codUfficio) throws F3BException {
		Connection lConn = null;

		try {
			lConn = getDBTransaction();

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			lStatoProcMod.setCodStatoProcedimento("0347");
			lStatoProcMod.setData(dataEmissione);
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(codOperatore);
			lStatoProcMod.setDataInserimento(dataInserimento);
			lStatoProcMod.setCodUfficioInserimento(codUfficio);

			changeStatoProcedimento(lConn, lStatoProcMod);

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("RichiesatController.ExUpdStatoProcPresaincaricoStessaBDI : " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + sqe);
			rollback(lConn);
			sqe.printStackTrace();
			throw new F3BException("RichiesatController.ExUpdStatoProcPresaincaricoStessaBDI : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("RichiesatController.ExUpdStatoProcPresaincaricoStessaBDI : " + ex);
		} finally {
			cleanup(lConn);

		}
	}

	/*****************************************************************************
	 * Aggiornamento dello stato del procedimento a seguito alla restituzione degli atti
	 * 
	 * @throws F3BException
	 ****************************************************************************/

	public void ExUpdStatoProcRestituzioneAttiStessaBDI(FascicoloSiepModel aFascicolo, Date dataEmissione,
			Date dataInserimento, String codOperatore, String codUfficio) throws F3BException {
		Connection lConn = null;

		try {
			lConn = getDBTransaction();

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			// *************CODICE DA DEFINIRE ****************///
			lStatoProcMod.setCodStatoProcedimento("????");
			lStatoProcMod.setData(dataEmissione);
			lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			lStatoProcMod.setCodOperatoreInserimento(codOperatore);
			lStatoProcMod.setDataInserimento(dataInserimento);
			lStatoProcMod.setCodUfficioInserimento(codUfficio);

			changeStatoProcedimento(lConn, lStatoProcMod);

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("RichiesatController.ExUpdStatoProcPresaincaricoStessaBDI : " + daoEx);
		} catch (SQLException sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + sqe);
			rollback(lConn);
			sqe.printStackTrace();
			throw new F3BException("RichiesatController.ExUpdStatoProcPresaincaricoStessaBDI : " + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("RichiesatController.ExUpdStatoProcPresaincaricoStessaBDI : " + ex);
		} finally {
			cleanup(lConn);

		}
	}

	/**
	 * Metodo privato che modifica lo stato del procedimento
	 * 
	 * @param lConn
	 */
	private void changeStatoProcedimento(Connection lConn, StatoProcedimentoModel aStatoProcMod)
			throws Exception {
		StatoProcedimentoDAO lStatoDao = null;

		try {

			// cancellazione
			lStatoDao = new StatoProcedimentoDAO(lConn);
			lStatoDao.setCondizioneByIdFascicolo(aStatoProcMod.getFasSieIdFascicoloSiep());
			lStatoDao.delete();

			// inserimento
			lStatoDao.setDAOFromModel(aStatoProcMod);
			lStatoDao.insert();
		} finally {
			cleanup(lStatoDao);
		}
	}

	// 02/2014 MISURE SICUREZZA SIEP
	/**
	 * <p>
	 * Valida la Annotazione della SORVEGLIANZA
	 * </p>
	 * <p>
	 * su DECISIONE di Applicazione Misure Sicurezza
	 * </p>
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExValidaAnnotazioneDecisioneDellaSorveglianza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		FascicoloSiepDAO lFascDao = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoBlob = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoDAO lDepDecDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		MisuraSicurezzaSqlDAO lMisSqlDao = null;
		MisuraSicurezzaDAO lMisDAO = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Aggiorna Inserisci PENA_RESIDUA
			// InserimentoAggiornamentoPenRes(lConn, lEveModel, aFascicolo.getIdFascicoloSiep());

			String lMotivo = "";
			String lStatoProcMod = "";

			// SETTA LO STATO PROCEDIMENTO
			if (lEveModel != null && lEveModel.getIdEvento() != null && lEveModel.getCodMotivo() != null) {
				lMotivo = lEveModel.getCodMotivo();

				if (lMotivo.equals("1125") || lMotivo.equals("1135") || lMotivo.equals("1136")
						|| lMotivo.equals("1137") || lMotivo.equals("1138") || lMotivo.equals("1139")
						|| lMotivo.equals("1140") || lMotivo.equals("1141") || lMotivo.equals("1142")
						|| lMotivo.equals("1143") || lMotivo.equals("1144") || lMotivo.equals("1145")
						|| lMotivo.equals("1146") || lMotivo.equals("1147") || lMotivo.equals("1148")) {
					lStatoProcMod = "0512"; // annotazione Decisione Sorveglianza
				} else {
					lStatoProcMod = "0076"; // Archiziazione MIS.SIC.
				}

				if (lStatoProcMod.compareTo("") != 0) {
					BigDecimal lKeyEvento = lEveModel.getIdEvento();
					lEveModel.setDataAggiornamento(lEveMod.getDataAggiornamento());
					lEveModel.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
					lEveModel.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
					InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(),
							lEveModel, lStatoProcMod, lKeyEvento);
				}
			}

			// ----- FASCICOLO_SIEP-----------------
			// Aggiorna il fascicolo con stato = "01" Archiviato/definito SOLO per Archiviazione MIS. SIC. (da
			// togliere)

			if (lStatoProcMod.compareTo("0076") == 0) {
				lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.setCodStatoFascicolo("01");
				lFascDao.setDataArchiviazione(lEveModel.getDataEmissione());

				lFascDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lFascDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lFascDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());

				lFascDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
				lFascDao.update();
				lFascDao.stop();
			}

			// 18/11/2014 Si setta in DEPOSITO_ORDINANZA_PC il FLAG_ELABORATO = "S"

			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveModel.getEveIdEvento());
			DepositoOrdinanzaPcModel lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

			if (lDepOrdMod != null && lDepOrdMod.getIdDepositoOrdinanzaPc() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("lDepOrdMod legato all'Ordinanza: " + lDepOrdMod.getIdDepositoOrdinanzaPc());

				lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);
				lDepOrdDao.setIdDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());

				lDepOrdDao.setFlagElaborato("S");

				lDepOrdDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lDepOrdDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lDepOrdDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

				lDepOrdDao.selByKey();
				lDepOrdDao.update();
				lDepOrdDao.stop();
			}

			// 18/11/2014 Si setta in DEPOSITO_DECRETO il FLAG_ELABORATO = "S"

			lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
			lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(lEveModel.getEveIdEvento());
			DepositoDecretoModel lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

			if (lDepDecMod != null && lDepDecMod.getIdDepositoDecreto() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("lDepODecreto legato all'Ordinanza: " + lDepDecMod.getIdDepositoDecreto());

				lDepDecDao = new DepositoDecretoDAO(lConn);
				lDepDecDao.setIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());

				lDepDecDao.setFlagElaborato("S");

				lDepDecDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lDepDecDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lDepDecDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

				lDepDecDao.selByKey();
				lDepDecDao.update();
				lDepDecDao.stop();
			}

			// --------- MISURA SICUREZZA -----------
			//
			lMisSqlDao = new MisuraSicurezzaSqlDAO(lConn);
			lMisDAO = new MisuraSicurezzaDAO(lConn);

			MisuraSicurezzaModel lMisMod = null;
			MisuraSicurezzaModel lMisOldMod = null;

			Vector MisSicVec = null;
			Vector OldMisSicVec = null;

			// Nuova Misura
			lMisSqlDao.ricercaMisuraSicurezzaByEventoKey(aEvento.getIdEvento());
			MisSicVec = new Vector(lMisSqlDao.getModels());
			lMisSqlDao.stop();

			BigDecimal lNewMisKey = null;
			if (MisSicVec != null && MisSicVec.size() > 0) {
				Iterator Itx1 = MisSicVec.iterator();
				while (Itx1.hasNext()) {
					lMisMod = (MisuraSicurezzaModel) Itx1.next();
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.info("--XX-- Nuova Misura - " + lMisMod);
					if (lMisMod != null && lMisMod.getIdMisuraSicurezza() != null)
						lNewMisKey = lMisMod.getIdMisuraSicurezza();

					if (lNewMisKey != null) {
						// Vecchia Misura
						lMisSqlDao.ricercaMisuraSicurezzaByKeyMisuraCollegata(lNewMisKey);
						OldMisSicVec = new Vector(lMisSqlDao.getModels());
						lMisSqlDao.stop();

						if (OldMisSicVec != null && OldMisSicVec.size() > 0) {
							Iterator Itx2 = OldMisSicVec.iterator();
							while (Itx2.hasNext()) {
								lMisOldMod = (MisuraSicurezzaModel) Itx2.next();
								// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
								// siesLogger al posto di LogF3B.getLogger()
								// siesLogger.info("--XX-- Vecchia Misura - " + lMisOldMod);
								if (lMisOldMod != null && lMisOldMod.getIdMisuraSicurezza() != null) {
									lMisOldMod.setDataAggiornamento(lEveMod.getDataAggiornamento());
									lMisOldMod.setCodUfficioAggiornamento(lEveMod
											.getCodUfficioAggiornamento());
									lMisOldMod.setCodOperatoreAggiornamento(lEveMod
											.getCodOperatoreAggiornamento());
									lMisOldMod.setDataFineValidita(lEveModel.getDataEmissione());

									lMisDAO.setDAOFromModelForUpdate(lMisOldMod);
									lMisDAO.update();
									lMisDAO.stop();
								}
							}
						}
					}
				}
			}
			// ------- EVENTO--------

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExValidaAnnotazioneDecisioneDellaSorveglianza : "
					+ daoEx);
//		} catch (SQLException sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("SQLException: " + sqe);
//			rollback(lConn);
//			sqe.printStackTrace();
//			throw new F3BException("RichiestaController.ExValidaAnnotazioneDecisioneDellaSorveglianza : "
//					+ sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);
			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExValidaAnnotazioneDecisioneDellaSorveglianza : "
					+ ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lFascDao);
			cleanup(lDepOrdDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecDao);
			cleanup(lDepDecSqlDao);
			cleanup(lConn);

		}

		return lEveMod;

	} // Chiude ExValidaAnnotazioneDecisioneDellaSorveglianza()

	/**
	 * <p>
	 * Valida tutti i provvedimenti sull Applicazione Misure Sicurezza di Fase istruttoria:
	 * </p>
	 * <p>
	 * OE Internato, OE Liberazione, Comunicazione/Ordine di Consegna, Richiesta DAP
	 * </p>
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExValidaAnnotazioneOComunicazioneApplicazioneMS(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		NomeProvvedimentoDAO lNomProvvDAO = null;
		FascicoloSiepDAO lFascDao = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoBlob = null;
		DepositoOrdinanzaPcDAO lDepOrdDao = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDao = null;
		DepositoDecretoDAO lDepDecDao = null;
		DepositoDecretoSqlDAO lDepDecSqlDao = null;
		PosizioneGiuridicaSqlDAO pgsDAO = null;

		try {
			lConn = getDBTransaction();

			// ** Aggiorna EVENTO **
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();
		
			// Aggiorna Inserisci PENA_RESIDUA
			// InserimentoAggiornamentoPenRes(lConn, lEveModel, aFascicolo.getIdFascicoloSiep());
			
			// mwv 39: POSIZIONE_GIURIDICA per MOTIVO DEL PROVVEDIMENTO DI DIFFERIMENTO (1132) 
			//aggiornamento: Libero in Differimento misura di sicurezza quando la da il tribunale  (codice 89)
			// oppure in Libero in Differimento misura di sicurezza (Provvisorio) quando la da il magistrato (codice 90)
			if("1132".equals(lEveModel.getCodMotivo())){
				String posGiur = "89";
				pgsDAO = new PosizioneGiuridicaSqlDAO(lConn);
				pgsDAO.ricercaPosizioneGiuridicaByIdFascicoloDesc(aFascicolo.getIdFascicoloSiep());
				PosizioneGiuridicaModel pgm = (PosizioneGiuridicaModel) pgsDAO.getModelByKey();		
				
				// ricerco il fascicolo sius				
				if(lEveModel.getFasSiuIdFascicoloSius() != null){
					IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
					IFascicoloSius iFS= SIUSLookupRemote.getFascicoloSiusRemote();
					FascicoloGPModel fgpm = iFS.ExRicercaFascicoloByKey(lEveModel.getFasSiuIdFascicoloSius());
					
					String codTipoUff = fgpm.getFascicoloSiusModel().getCodTipoUfficio();
					
					posGiur = "UDS".equals(codTipoUff) || "UDSM".equals(codTipoUff)  ? "90" : "89";
				}
				pgsDAO.inserimentoAggiornamentoPosizioneGiuridica(lConn, posGiur, pgm, lEveMod.getDataEmissione(), lEveModel,
						aFascicolo.getIdFascicoloSiep(), lEveModel.getIdEvento(), "N");				
			}
			// mwv 39: POSIZIONE_GIURIDICA: fine
			
			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = "";
			if (lEveModel != null && lEveModel.getCodMotivo() != null) {
				if ("1126".equals(lEveModel.getCodMotivo())) // Ordine di Consegna forze di polizia
					lStatoProcMod = "0516";
				else if ("1131".equals(lEveModel.getCodMotivo())) // Comunicazione forze di polizia
					lStatoProcMod = "0513";
				else if ("1127".equals(lEveModel.getCodMotivo())) // Emissione Richiesta al DAP per
																	// Designazione Ist.
					lStatoProcMod = "";
				else if ("1128".equals(lEveModel.getCodMotivo())) // Emissione O.E. per Internamento
					lStatoProcMod = "0514";
				else if ("1129".equals(lEveModel.getCodMotivo())) // Emissione O.E. per Liberazione
					lStatoProcMod = "0515";
				// MEV_39: aggiunto OL per differimento MS
				else if ("1132".equals(lEveModel.getCodMotivo()))
					lStatoProcMod = "0517";
				// MEV_39: aggiunta restituzione ordine di consegna x esecuzione MS
				else if ("1149".equals(lEveModel.getCodMotivo()))
					lStatoProcMod = "0518";
				else
					lStatoProcMod = "0076"; // Archiziazione MIS.SIC.

				if (lStatoProcMod.compareTo("") != 0) {
					BigDecimal lKeyEvento = lEveModel.getIdEvento();
					InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(),
							lEveModel, lStatoProcMod, lKeyEvento);
				}
			}

			// ----- FASCICOLO_SIEP-----------------
			// Aggiorna il fascicolo con stato = "01" Archiviato/definito SOLO per Archiviazione MIS. SIC.

			if (lStatoProcMod.compareTo("0076") == 0) {
				lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.setCodStatoFascicolo("01");
				lFascDao.setDataArchiviazione(lEveModel.getDataEmissione());

				lFascDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lFascDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lFascDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());

				lFascDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
				lFascDao.update();
				lFascDao.stop();
			}

			// 18/11/2014 Si setta in DEPOSITO_ORDINANZA_PC il FLAG_ELABORATO = "S"
			lDepOrdSqlDao = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepOrdSqlDao.ricercaDepositoOrdinanzaPcByIdEveGenerato(lEveModel.getEveIdEvento());
			DepositoOrdinanzaPcModel lDepOrdMod = (DepositoOrdinanzaPcModel) lDepOrdSqlDao.getModelByKey();

			if (lDepOrdMod != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ValidaProvv - lDepOrdMod legato all'Ordinanza: "
								+ lDepOrdMod.getIdDepositoOrdinanzaPc());

				lDepOrdDao = new DepositoOrdinanzaPcDAO(lConn);
				lDepOrdDao.setIdDepositoOrdinanzaPc(lDepOrdMod.getIdDepositoOrdinanzaPc());

				lDepOrdDao.setFlagElaborato("S");

				lDepOrdDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lDepOrdDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lDepOrdDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

				lDepOrdDao.selByKey();
				lDepOrdDao.update();
				lDepOrdDao.stop();
			}

			// 20/02/2015 Si setta in DEPOSITO_DECRETO il FLAG_ELABORATO = "S"
			lDepDecSqlDao = new DepositoDecretoSqlDAO(lConn);
			lDepDecSqlDao.ricercaDepositoDecretoByIdEveGenerato(lEveModel.getEveIdEvento());
			DepositoDecretoModel lDepDecMod = (DepositoDecretoModel) lDepDecSqlDao.getModelByKey();

			if (lDepDecMod != null && lDepDecMod.getIdDepositoDecreto() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ValidaProvv - lDepODecreto legato all'Ordinanza: "
								+ lDepDecMod.getIdDepositoDecreto());

				lDepDecDao = new DepositoDecretoDAO(lConn);
				lDepDecDao.setIdDepositoDecreto(lDepDecMod.getIdDepositoDecreto());

				lDepDecDao.setFlagElaborato("S");

				lDepDecDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
				lDepDecDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lDepDecDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

				lDepDecDao.selByKey();
				lDepDecDao.update();
				lDepDecDao.stop();
			}

			// ------- EVENTO--------

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);

			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExValidaAnnotazioneOComunicazioneApplicazioneMS : "
					+ daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("RichiestaController.ExValidaAnnotazioneOComunicazioneApplicazioneMS : "
			// + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExValidaAnnotazioneOComunicazioneApplicazioneMS : "
					+ ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lNomProvvDAO);
			cleanup(lEveDaoBlob);
			cleanup(lFascDao);
			cleanup(lDepOrdDao);
			cleanup(lDepOrdSqlDao);
			cleanup(lDepDecSqlDao);
			cleanup(lDepDecDao);
			cleanup(lConn);
		}

		return lEveMod;
	}

	// 09-2015 MEV_2 (Misure Sicurezza) - STEP_2
	// Validazione Archiviazione per Provv Giudice/Cassazione (Mis Sic Provvisoria o disposta Fuori Sentenza)

	public EventoModel ExValidaArchiviazionePerProvvGiudiceEsecuzione(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		FascicoloSiepDAO lFascDao = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			String lStatoProcMod = "";
			if (lEveModel != null && lEveModel.getCodMotivo() != null) {
				lStatoProcMod = "0076"; // Archiziazione MIS.SIC.
				BigDecimal lKeyEvento = lEveModel.getIdEvento();
				InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(), lEveModel,
						lStatoProcMod, lKeyEvento);

			}

			// ----- FASCICOLO_SIEP-----------------
			// Aggiorna il fascicolo con stato = "01" Archiviato/definito
			if (lStatoProcMod.compareTo("0076") == 0) {
				lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.setCodStatoFascicolo("01");
				lFascDao.setDataArchiviazione(lEveModel.getDataEmissione());

				lFascDao.setDataAggiornamento(lEveMod.getDataAggiornamento());
				lFascDao.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());
				lFascDao.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());

				lFascDao.selCondizioneUpdate(aFascicolo.getIdFascicoloSiep());
				lFascDao.update();
				lFascDao.stop();
			}

			// ------- EVENTO--------

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();
			// ---------------------
			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);

			daoEx.printStackTrace();

			throw new F3BException("RichiestaController.ExValidaArchiviazionePerProvvGiudiceEsecuzione : "
					+ daoEx);
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// rollback(lConn);
			//
			// sqe.printStackTrace();
			//
			// throw new F3BException("RichiestaController.ExValidaArchiviazionePerProvvGiudiceEsecuzione : "
			// + sqe);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(lConn);

			ex.printStackTrace();

			throw new F3BException("RichiestaController.ExValidaArchiviazionePerProvvGiudiceEsecuzione : "
					+ ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lEveDaoBlob);
			cleanup(lFascDao);
			cleanup(lConn);
		}

		return lEveMod;

	} // Chiude ExValidaArchiviazionePerProvvGiudiceEsecuzione()


	/**
	 * Validazione del provvedimento di Trasmissione Atti per Competenza ai fini di Emissione Provvedimento di
	 * Cumulo
	 */
	public EventoModel ExValidaTrasmissioneAttiPerCompetenza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo, CompetenzaModel aCompetenza) throws F3BException {
      Connection lConn = null;

      EventoSqlDAO lEveSqlDao = null;
      EventoModel lEveMod = new EventoModel(aEvento);
      EventoDAO lEveDaoBlob = null;

      MessaggioModel lMessage = null;
      MessaggioDAO lMessDAO = null;
      MessaggioSqlDAO lMessSqlDAO = null;

		try {
        lConn = getDBTransaction();

  // ** Aggiorna EVENTO **
        lEveSqlDao = new EventoSqlDAO(lConn);
        lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
        EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

  // Aggiorna Inserisci PENA_RESIDUA
        InserimentoAggiornamentoPenRes(lConn, lEveModel, aFascicolo.getIdFascicoloSiep());

  // SETTA LO STATO PROCEDIMENTO
			if (lEveModel != null && lEveModel.getCodMotivo() != null) {
            String lMotivo = lEveModel.getCodMotivo();
            String lStatoProcMod = null;
            
				// String lTipoUffDest="";
				// if (lEveModel.getCodTipoUfficioDestinatario() != null)
				// lTipoUffDest = lEveModel.getCodTipoUfficioDestinatario();
  
				if (lMotivo.equals("0340")) {
              lStatoProcMod = "0139";
            }
				if (lMotivo.equals("5403")) {
              lStatoProcMod = "0153";         
            }
  
				if (lStatoProcMod != null) {
              BigDecimal lKeyEvento = lEveModel.getIdEvento();
					InserimentoCancellazioneStatoProcedimento(lConn, aFascicolo.getIdFascicoloSiep(),
							lEveModel, lStatoProcMod, lKeyEvento);
            }
        }
        
        // MESSAGGIO
			if (aCompetenza.getIdMessaggioRichiesta() != null) {
          lMessSqlDAO = new MessaggioSqlDAO(lConn);
          lMessSqlDAO.ricercaMessaggioPerJmsIdOnly(aCompetenza.getIdMessaggioRichiesta().toString());
          lMessage = (MessaggioModel)lMessSqlDAO.getModelByKey();
          
          lMessage.setFlagVisto("S");
          lMessage.setCodEsito("01006");
  	      lMessage.setDataEsito(DateUtils.getSysDate());
          
          lMessDAO = new MessaggioDAO(lConn);
          lMessDAO.setDAOFromModelForUpdate(lMessage);
          lMessDAO.update();
          lMessDAO.stop();
        }

  //------- EVENTO--------
        lEveDaoBlob = new EventoDAO(lConn);
        lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

        lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
        lEveDaoBlob.update();
        lEveDaoBlob.stop();
  //---------------------
        commit(lConn);

		} catch (DAOException daoEx) {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
        siesLogger.error("DAOException: ",daoEx);
        rollback(lConn);
        //daoEx.printStackTrace();

        throw new F3BException("RichiestaController.ExValidaTrasmissioneAttiPerCompetenza : " + daoEx);
		} catch (SQLException sqe) {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
        siesLogger.error("SQLException: ",sqe);
        rollback(lConn);
        //sqe.printStackTrace();

        throw new F3BException("RichiestaController.ExValidaTrasmissioneAttiPerCompetenza : " + sqe);
		} catch (Exception ex) {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
        siesLogger.error("Exception: ",ex);
        rollback(lConn);
        //ex.printStackTrace();

        throw new F3BException("RichiestaController.ExValidaTrasmissioneAttiPerCompetenza : " + ex);
		} finally {
        cleanup(lEveSqlDao);
        cleanup(lEveDaoBlob);
        cleanup(lConn);
      }

      return lEveMod;
  }  
      
 /**
  * Validazione del provvedimento di 'Comunicazione Rigetto Richiesta Atti per Competenza' 
  * ai fini di Emissione Provvedimento di Cumulo
  * 
  */
 public EventoModel ExValidaRigettoRichiestaAttiPerCompetenza(EventoModel aEvento, FascicoloSiepModel aFascicolo, CompetenzaModel aCompetenza) throws F3BException
{
	    Connection lConn = null;

	    EventoSqlDAO lEveSqlDao = null;
	    EventoModel lEveMod = new EventoModel(aEvento);
	    EventoDAO lEveDaoBlob = null;

	    MessaggioModel lMessage = null;
	    MessaggioDAO lMessDAO = null;
	    MessaggioSqlDAO lMessSqlDAO = null;

	    try
	    {
	      lConn = getDBTransaction();

	// ** Aggiorna EVENTO **
	      lEveSqlDao = new EventoSqlDAO(lConn);
	      lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
	      EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

	// Aggiorna Inserisci PENA_RESIDUA
	      InserimentoAggiornamentoPenRes(lConn, lEveModel, aFascicolo.getIdFascicoloSiep());

	      // MESSAGGIO di RICHIESTA, 
	      if (aCompetenza.getIdMessaggioRichiesta()!=null) 
	      {
   	      lMessSqlDAO = new MessaggioSqlDAO(lConn);
   	      lMessSqlDAO.ricercaMessaggioPerJmsIdOnly(aCompetenza.getIdMessaggioRichiesta().toString());
   	      lMessage = (MessaggioModel)lMessSqlDAO.getModelByKey();
   	      
   	      lMessage.setFlagVisto("S");		// S=Visto ed elaborato 
   	      lMessage.setCodEsito("01007");    // 01007 = Rigettato
   	      lMessage.setDataEsito(DateUtils.getSysDate());
   	      
   	      lMessage.setCodUfficioInoltro(null);
   	      lMessage.setCodBdiInoltro(null);
   	      lMessage.setCodUfficioReplyTo(null);
   	      lMessage.setCodBdiReplyTo(null);
   	      lMessage.setJmsCorrelationReplyTo(null);
   	      lMessage.setIdMessaggioSollecitato(null);
   	      
   	      lMessDAO = new MessaggioDAO(lConn);
   	      lMessDAO.setDAOFromModelForUpdate(lMessage);
   	      lMessDAO.update();
   	      lMessDAO.stop();
	      }

	//------- EVENTO--------
	      lEveDaoBlob = new EventoDAO(lConn);
	      lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

	      lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
	      lEveDaoBlob.update();
	      lEveDaoBlob.stop();
	//---------------------
	      commit(lConn);

	    }
	    catch (DAOException daoEx)
	    {
	      siesLogger.error("DAOException: ",daoEx);
	      rollback(lConn);
	      daoEx.printStackTrace();

	      throw new F3BException("RichiestaController.ExValidaRigettoRichiestaAttiPerCompetenza : " + daoEx);
	    }
	    catch (SQLException sqe)
	    {
	      siesLogger.error("SQLException: ",sqe);
	      rollback(lConn);
	      sqe.printStackTrace();

	      throw new F3BException("RichiestaController.ExValidaRigettoRichiestaAttiPerCompetenza : " + sqe);
	    }
	    catch (Exception ex)
	    {
	      siesLogger.error("Exception: ",ex);
	      rollback(lConn);
	      ex.printStackTrace();

	      throw new F3BException("RichiestaController.ExValidaRigettoRichiestaAttiPerCompetenza : " + ex);
	    }
	    finally
	    {
	      cleanup(lEveSqlDao);
	      cleanup(lEveDaoBlob);
	      cleanup(lConn);
	    }

	    return lEveMod;
	    
 }	// Chiude ExValidaRigettoRichiestaAttiPerCompetenza() 

	/**
	 * MEV_39: aggiunto metodo per la validazione di un evento di archiviazione provvedimento di cumulo
	 * 
	 * @param em
	 * @param fsm
	 * @throws F3BException
	 */
	public void ExValidaArchiviazionePerProvvCumulo(EventoModel em, FascicoloSiepModel fsm)
			throws F3BException {

		Connection c = null;

		EventoSqlDAO esDAO = null;
		FascicoloSiepDAO fsDAO = null;
		EventoDAO eDAO = null;
		PosizioneGiuridicaSqlDAO pgsDAO = null;
		ArchiviazioneSqlDAO asDAO = null;

		try {
			c = getDBTransaction();

			esDAO = new EventoSqlDAO(c);
			esDAO.ricercaEventoByKey(em.getIdEvento());
			EventoModel emNew = (EventoModel) esDAO.getModelByKey();

			// SETTA LO STATO PROCEDIMENTO
			String statoProcedimento = "";
			if (emNew != null && emNew.getCodMotivo() != null) {
				statoProcedimento = "0132"; // Assorbimento in cumulo
				BigDecimal idEvento = emNew.getIdEvento();
				InserimentoCancellazioneStatoProcedimento(c, fsm.getIdFascicoloSiep(), emNew,
						statoProcedimento, idEvento);
			}

			// ARCHIVIAZIONE
			asDAO = new ArchiviazioneSqlDAO(c);
			ArchiviazioneModel am = new ArchiviazioneModel();
			asDAO.ricercaArchiviazioneByIdEvento(em.getIdEvento());
			am = (ArchiviazioneModel) asDAO.getModelByKey();

			// POSIZIONE_GIURIDICA: aggiorno a LIBERO
			pgsDAO = new PosizioneGiuridicaSqlDAO(c);
			pgsDAO.ricercaPosizioneGiuridicaByIdFascicoloDesc(fsm.getIdFascicoloSiep());
			PosizioneGiuridicaModel pgm = (PosizioneGiuridicaModel) pgsDAO.getModelByKey();
			pgsDAO.inserimentoAggiornamentoPosizioneGiuridica(c, "10", pgm, am.getDataDefinizione(), emNew,
					fsm.getIdFascicoloSiep(), emNew.getIdEvento(), "N");

			// ----- FASCICOLO_SIEP-----------------
			// Aggiorna il fascicolo con stato = "01" Archiviato/definito
			if (statoProcedimento.compareTo("0132") == 0) {
				fsDAO = new FascicoloSiepDAO(c);
				fsDAO.setCodStatoFascicolo("01");
				fsDAO.setDataArchiviazione(emNew.getDataEmissione());
				fsDAO.setDataAggiornamento(em.getDataAggiornamento());
				fsDAO.setCodUfficioAggiornamento(em.getCodUfficioAggiornamento());
				fsDAO.setCodOperatoreAggiornamento(em.getCodOperatoreAggiornamento());
				fsDAO.selCondizioneUpdate(fsm.getIdFascicoloSiep());
				fsDAO.update();
				fsDAO.stop();
			}

			// ------- EVENTO--------
			eDAO = new EventoDAO(c);
			eDAO.setDAOFromModelForUpdateBlob(em);
			eDAO.selCondizioneUpdate(em.getIdEvento());
			eDAO.update();
			eDAO.stop();
			// ---------------------
			commit(c);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			rollback(c);
			daoEx.printStackTrace();
			throw new F3BException("RichiestaController.ExValidaArchiviazionePerProvvCumulo : " + daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex, ex);
			rollback(c);
			ex.printStackTrace();
			throw new F3BException("RichiestaController.ExValidaArchiviazionePerProvvCumulo : " + ex);
		} finally {
			cleanup(esDAO);
			cleanup(eDAO);
			cleanup(fsDAO);
			cleanup(pgsDAO);
			cleanup(asDAO);
			cleanup(c);
		}
	}

} // chiude CONTROLLER