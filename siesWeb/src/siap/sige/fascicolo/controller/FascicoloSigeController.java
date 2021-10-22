package siap.sige.fascicolo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSigeDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaFascicoloSigeModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.util.report.ReportGenerator;
//import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.sentenza.dao.SentenzaDAO;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
//import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.dao.FasSigeDetenzioneDAO;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.dao.FascicoloSigeSqlDAO;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.dao.MagistratoAssegnatarioDAO;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.dao.ProvvedimentoSigeDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiesta.dao.RichiestaSigeDAO;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.richiestaatti.dao.ParereSqlDAO;
import siap.sige.richiestaatti.model.ParereModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.dao.FasSigeSentenzaDAO;
import siap.sige.sentenza.dao.FasSigeSentenzaSqlDAO;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeSqlDAO;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: FascicoloSigeController
 * </p>
 * <p>
 * Description: Classe Controller per FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoloSigeController extends SiapController implements IFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca paginata di "Richieste Parere su Fascicolo Sige"
	 *
	 * @param aFascicoloSige
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di ParereModel
	 * @throws F3BException
	 */
	public Vector<ParereModel> ExRicercaPareriPaginata(ParereModel aParereIn, int aPageNum)
			throws F3BException {

		Connection lConn = null;
		Vector<ParereModel> lElencoPareri = new Vector<>();
		ParereSqlDAO lParDao = null;

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();

		try {
			lConn = getDBConnection();

			lParDao = new ParereSqlDAO(lConn);
			lParDao.ricerca(aParereIn);

			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPageNum > 0)
				lParDao.startPage(aPageNum);
			else
				lParDao.start();

			while (lParDao.next()) {
				ParereModel lParere = (ParereModel) lParDao.getModel();
				BigDecimal idParere = lParere.getIdEvento();
				EventoNotificaModel lEve = lCtrl.ExRicercaEventoNotificaByKey(idParere);
				String destinatario = lEve.getNotifiche()[0].getUfficio().getDescrTipoUfficio();
				lParere.setDescrUfficioDestinatario(destinatario);
				lElencoPareri.add(lParere);
			}
			lParDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaPareriPaginata: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
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
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lParDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/*
	 * La funzione effettua l'inserimento della Richiesta associata al Fascicolo
	 */
	private RichiestaSigeModel InserisciRichiestaSige(RichiestaSigeModel aRichiestaSige, Connection aConn)
			throws Exception {

		RichiestaSigeDAO lRicDao = null;
		RichiestaSigeModel lRicMod = null;
		BigDecimal lKey = null;

		try {
			lRicMod = new RichiestaSigeModel(aRichiestaSige);
			lRicDao = new RichiestaSigeDAO(aConn);
			lRicDao.setDAOFromModel(aRichiestaSige);

			lKey = lRicDao.insert();
			lRicMod.setIdRichiestaSige(lKey);
		} catch (DAOException ex) {
			throw new F3BException("FascicoloSigeController.InserisciRichiestaSige DAOException: " + ex);
		} catch (Exception e) {
			throw new F3BException("FascicoloSigeController.InserisciRichiestaSige Exception -> " + e);
		} finally {
			cleanup(lRicDao);
		}
		return lRicMod;
	}

	/**
	 * La funzione attiva il processo di "Assegnazione" della Sentenza e dei Reati legati al Fascicolo SIEP al
	 * Fascicolo SIGE. La funzione istanzia un SentenzaSigeModel valorizzandolo con i dati caratteristici
	 * della relazione Fascicolo_SIGE - Sentenza e passa questo alla funzione
	 * IFasSigeSentenza.ExAssegnaSentenzaReatiFasSiep che provvederà alla ricerca di Sentenza e Reati ed
	 * all'inserimento dei dati nelle tabelle di relazione adibite alla memorizzazione dell'associazione
	 * fascicolo SIGE - Sentenza - Reati.
	 *
	 * @param aRichiestaSige
	 * @param aIdFasSige
	 * @param aConn
	 * @throws Exception
	 */
	private void InserisciSentenzaSiep(RichiestaSigeModel aRichiestaSige, BigDecimal aIdFasSige,
			Connection aConn) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di mLog
		siesLogger.debug("DAOException  InserisciSentenzaSiep: inizio ");
		// Interfaccia del Controller Sentenza Sige
		IFasSigeSentenza lCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();

		SentenzaSigeModel lFasSigeSen = new SentenzaSigeModel();

		// Valorizzazione model da passare alla funzione di Assegnazione
		// Sentenza

		lFasSigeSen.setFasIdFascicoloSige(aIdFasSige);
		// La sentenza SIEP viene sempre definita "di competenza"
		lFasSigeSen.setFlagCompetenza("S");
		lFasSigeSen.setCodUfficioInserimento(aRichiestaSige.getCodUfficioInserimento());
		lFasSigeSen.setCodOperatoreInserimento(aRichiestaSige.getCodOperatoreInserimento());
		lFasSigeSen.setDataInserimento(aRichiestaSige.getDataInserimento());
		lFasSigeSen.setCodUfficioAggiornamento(lFasSigeSen.getCodUfficioInserimento());
		lFasSigeSen.setCodOperatoreAggiornamento(lFasSigeSen.getCodOperatoreInserimento());
		lFasSigeSen.setDataAggiornamento(lFasSigeSen.getDataInserimento());
		lFasSigeSen.setFasSieIdFascicoloSiep(aRichiestaSige.getFasSieIdFascicoloSiep());

		// Viene chiamata la funzione di Assegnazione
		lCtrl.ExAssegnaSentenzaReatiFasSiep(lFasSigeSen, aConn);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di mLog
		siesLogger.debug("DAOException  InserisciSentenzaSiep: fine ");
	}

	/*
	 * La funzione effettua l'inserimento del Magistrato Assegnatario associato al Fascicolo
	 */
	private void InserisciMagistratoAssegnatario(MagistratoAssegnatarioModel aMagAss, Connection aConn)
			throws Exception {

		MagistratoAssegnatarioDAO lMagDao = null;

		try {
			lMagDao = new MagistratoAssegnatarioDAO(aConn);
			lMagDao.setDAOFromModelForInserimento(aMagAss);
			lMagDao.insert();
		} catch (DAOException ex) {
			throw new F3BException(
					"FascicoloSigeController.InserisciMagistratoAssegnatario DAOException: " + ex);
		} catch (Exception e) {
			throw new F3BException(
					"FascicoloSigeController.InserisciMagistratoAssegnatario Exception -> " + e);
		} finally {
			cleanup(lMagDao);
		}
	}

	/**
	 * La funzione effettua l'inserimento nella tabella FAS_SIGE_SETENZIONE del riferimento al
	 * LUOGO_DETENZIONE o ALTRA_CAUSA legato al Fascicolo SIEP
	 */
	private void InserisciLuogoDetenzioneSIEP(FasSigeDetenzioneModel aDetenzione, Connection aConn)
			throws Exception {

		FasSigeDetenzioneDAO lDetDao = null;
		try {
			lDetDao = new FasSigeDetenzioneDAO(aConn);
			lDetDao.setDAOFromModel(aDetenzione);
			lDetDao.insert();
		} catch (DAOException ex) {
			throw new F3BException(
					"FascicoloSigeController.InserisciLuogoDetenzioneSIEP DAOException: " + ex);
		} catch (Exception e) {
			throw new F3BException("FascicoloSigeController.InserisciLuogoDetenzioneSIEP Exception -> " + e);
		} finally {
			cleanup(lDetDao);
		}
	}

	/**
	 * Funzione di inserimento di un nuovo FascicoloSige e Richiesta associata.
	 */
	public FascicoloSigeModel ExInserisciFascicoloSige(FascicoloSigeModel aFascicoloSige,
			RichiestaSigeModel aRichiestaSige, MagistratoAssegnatarioModel aMagistrato,
			FasSigeDetenzioneModel aDetenzione) throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		FascicoloSigeModel lFasMod = null;
		RichiestaSigeModel lRichiestaSige = null;
		BigDecimal lKey = null;

		try {
			lConn = getDBConnection();
			lFasMod = new FascicoloSigeModel(aFascicoloSige);
			if (aRichiestaSige != null) {
				lRichiestaSige = InserisciRichiestaSige(aRichiestaSige, lConn);
				lFasMod.setRicIdRichiestaSige(lRichiestaSige.getIdRichiestaSige());
				// decodifica solo X debug Da togliere !!
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Inserita Richiesta_Sige ->" + lRichiestaSige.decodifica().toString());
			}

			// Si apre la connessione al SqlDAO per calcolare il Progressivo
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);

			// Si ricava la CHIAVE PROGR del Fascicolo SIGE da inserire
			BigDecimal lChiaveProgr = lFasSqlDao.getProgressivoFascicoloSige(aFascicoloSige);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.debug("Chiave Progr FASCICOLO_SIGE ->" + lChiaveProgr);
			lFasMod.setChiaveProgr(new BigDecimal(lChiaveProgr.intValue() + 1));

			// paolo cherubini per supersoggetto 30/07/2009
			// se il soggetto e' collegato ad un fascicolo che esso sia SIEP SIUS o SIGE
			// inserisce un nuovo soggetto duplicando i suoi dati
			lFasMod = SuperSoggetto(lFasMod, lConn);
			// fine paolo

			// Inserimento Fascicolo
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModel(lFasMod);
			lKey = lFasDao.insert();
			lFasMod.setIdFascicoloSige(lKey);

			// Eventuale Inserimento Magistrato Assegnatario
			if (aMagistrato != null) {
				aMagistrato.setFasSigeIdFascicoloSige(lKey);
				InserisciMagistratoAssegnatario(aMagistrato, lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Inserito Magistrato Assegnatario ->" + aMagistrato.toString());
			}

			// Copia di eventuali Riferimenti a Sentenza e Reati legati a
			// Fascicolo SIEP
			if (lRichiestaSige != null && lRichiestaSige.getFasSieIdFascicoloSiep() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Associazione a Sentenza e reati SIEP");
				InserisciSentenzaSiep(lRichiestaSige, lFasMod.getIdFascicoloSige(), lConn);
				this.InserisciResidenzeSiep(lFasMod.getIdFascicoloSige(),
						lRichiestaSige.getFasSieIdFascicoloSiep(), lConn);
			}
			// @emma 19072018 intervento post COLLAUDO 11.2
			// CASO IN CUI IL PROCEDIMENTO SIGE NON E' LEGATO AD UN PROCEDIMENTO SIEP
			if (lRichiestaSige != null && lRichiestaSige.getFasSieIdFascicoloSiep() == null
					&& lFasMod.getIdFascicoloSigeOrigine() != null) {
				// IN QUESTO CASO DEVO RECUPERARE LA SENTENZA INSERITA SU PROCEDIMENTO SIGE ORIGINARIO
				// E SETTARLA ANCHE SU QUESTO NUOVO FASCICOLO SIGE CHE STO CREANDO
				inserisciSentenzaNataInSige(lRichiestaSige, lFasMod.getIdFascicoloSige(),
						lFasMod.getIdFascicoloSigeOrigine(), lConn);
			}

			// Inserimento di Riferimento a Luogo Detenzione o Altra Causa SIEP
			if (aDetenzione != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Associazione a Luogo Detenzione (Altra Causa) SIEP");
				aDetenzione.setFasIdFasSige(lFasMod.getIdFascicoloSige());
				InserisciLuogoDetenzioneSIEP(aDetenzione, lConn);
			}

			// Iscrizione Procedimento Collegato
			// In fase di iscrizione del procedimento secondario, bisogna
			// aggiornare il fascicolo di provenienza (origine/primario),
			// ossia il procedimento sul quale è stato inserito il ricorso,
			// impostare lo Stato a "Annulla con Rinvio" se il CodTenoreDecisione = "02"
			// oppure "Ricorso convertito in opposizione" se il CodTenoreDecisione = "12"
			// e la Data Definizione uguale alla Data Decisione del Tenore.
			if (lFasMod.getIdFascicoloSigeOrigine() != null
					&& !"".equals(lFasMod.getIdFascicoloSigeOrigine().toString())) {
				lFasDao = new FascicoloSigeDAO(lConn);

				if (lFasMod.getCodTenoreDecisione() != null && lFasMod.getCodTenoreDecisione().equals("02")) {
					lFasDao.setCodStatoFascicolo("15"); // Annulla con Rinvio
				} else if (lFasMod.getCodTenoreDecisione() != null
						&& lFasMod.getCodTenoreDecisione().equals("12")) {
					lFasDao.setCodStatoFascicolo("16"); // Ricorso convertito in opposizione (Fissa Udienza)
				}

				lFasDao.setDataDefinizione(aFascicoloSige.getDataDefinizioneOrigine());
				lFasDao.setCodOperatoreAggiornamento(aFascicoloSige.getCodOperatoreAggiornamento());
				lFasDao.setCodUfficioAggiornamento(aFascicoloSige.getCodUfficioAggiornamento());
				lFasDao.setDataAggiornamento(aFascicoloSige.getDataAggiornamento());

				// Set del DAO e aggiornamento del FascicoloSige Primario/Origine.
				lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSigeOrigine());
				lFasDao.update();
				lFasDao.stop();
			}

			commit(lConn);
			// decodifica solo X debug Da togliere !!
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.debug("Inserito Fascicolo_Sige ->" + lFasMod.decodifica().toString());
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("FascicoloSigeController.ExInserisci DAOException: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSigeController.ExInserisciFascicoloSige Exception : " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasSqlDao);
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	/**
	 * Funzione di inserimento manuale di un nuovo FascicoloSige e Richiesta associata.
	 *
	 */

	public FascicoloSigeModel ExInserisciFascicoloSigeManuale(FascicoloSigeModel aFascicoloSige,
			RichiestaSigeModel aRichiestaSige, MagistratoAssegnatarioModel aMagistrato,
			FasSigeDetenzioneModel aDetenzione) throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		FascicoloSigeModel lFasMod = null;
		RichiestaSigeModel lRichiestaSige = null;
		BigDecimal lKey = null;

		try {
			lConn = getDBConnection();
			lFasMod = new FascicoloSigeModel(aFascicoloSige);
			if (aRichiestaSige != null) {
				lRichiestaSige = InserisciRichiestaSige(aRichiestaSige, lConn);
				lFasMod.setRicIdRichiestaSige(lRichiestaSige.getIdRichiestaSige());
				// decodifica solo X debug Da togliere !!
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Inserita Richiesta_Sige ->" + lRichiestaSige.decodifica().toString());
			}

			// Controllo di esistenza di un Fascicolo SIGE con stessi
			// CHIAVE_ANNO e CHIAVE_PROGR:
			String response = existAnnoProgrSige(lFasMod, lConn);
			if (response != "")
				throw new SIGEException(F3BException.USER_MESSAGE,
						"Attenzione: Esiste gia il procedimento " + response + " per questo ufficio!");

			// Si apre la connessione al SqlDAO per calcolare il Progressivo
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);

			BigDecimal lBigDec = lFasSqlDao.getProgressivoFascicoloSige(aFascicoloSige);

			// paolo cherubini per supersoggetto 08/09/2010
			// se il soggetto e' collegato ad un fascicolo che esso sia SIEP SIUS o SIGE
			// inserisce un nuovo soggetto duplicando i suoi dati
			lFasMod = SuperSoggetto(lFasMod, lConn);
			// fine paolo

			// Nell'ambito dell'anno corrente, non si possono inserire
			// manualmente Fascicoli con progressivo
			// maggiore del Max esistente.
			if ((lBigDec != null) && (lBigDec.intValue() < (aFascicoloSige.getChiaveProgr().intValue()))
					&& (aFascicoloSige.getChiaveAnno().toString()
							.equals(DateUtils.getYearToString(DateUtils.getSysDate()))))
				throw new SIGEException(F3BException.USER_MESSAGE,
						"Attenzione: Il progressivo SIGE non può essere maggiore di " + lBigDec.toString());

			// Inserimento Fascicolo
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModel(lFasMod);
			lKey = lFasDao.insert();
			lFasMod.setIdFascicoloSige(lKey);

			// Eventuale Inserimento Magistrato Assegnatario
			if (aMagistrato != null) {
				aMagistrato.setFasSigeIdFascicoloSige(lKey);
				InserisciMagistratoAssegnatario(aMagistrato, lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Inserito Magistrato Assegnatario ->" + aMagistrato.toString());
			}

			// Copia di eventuali Riferimenti a Sentenza e Reati legati a
			// Fascicolo SIEP
			if (lRichiestaSige != null && lRichiestaSige.getFasSieIdFascicoloSiep() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Associazione a Sentenza e reati SIEP");
				InserisciSentenzaSiep(lRichiestaSige, lFasMod.getIdFascicoloSige(), lConn);
			}

			// Inserimento di Riferimento a Luogo Detenzione o Altra Causa SIEP
			if (aDetenzione != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Associazione a Luogo Detenzione (Altra Causa) SIEP");
				aDetenzione.setFasIdFasSige(lFasMod.getIdFascicoloSige());
				InserisciLuogoDetenzioneSIEP(aDetenzione, lConn);
			}
			commit(lConn);
			// decodifica solo X debug Da togliere !!
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.debug("Inserito Fascicolo_Sige ->" + lFasMod.decodifica().toString());
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("FascicoloSigeController.ExInserisci DAOException: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSigeController.ExInserisciFascicoloSige Exception : " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	/**
	 * Ricerca Fascicolo SIGE
	 *
	 * @param aFascicoloSige
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSige(FascicoloSigeModel aFascicoloSige) throws F3BException {

		Connection lConn = null;
		Vector lFascicoloSigi = new Vector();
		FascicoloSigeDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizione(aFascicoloSige);
			lFascicoloSigi = new Vector(lFasDao.getModels());
			/*
			 * if ( lFascicoloSigi.size() == 0 ) { throw new F3BException(F3BException
			 * .USER_MESSAGE,"Nessun Elemento trovato"); }
			 */
		} catch (DAOException daoEx) {
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSige -> " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoloSigi;
	}

	/**
	 * La funzione effettua la ricerca del Fascicolo SIGE e degli altri dati ad esso collegati: Soggetto,
	 * Richiesta, Fascicolo Siep.
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByKey(BigDecimal aKey) throws F3BException {

		return ExRicercaEstesaFascicoloSigeByKey(aKey, false);
	}

	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByKey(BigDecimal aKey, boolean complete)
			throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		FascicoloSigeEstesoModel lFascicoloEsteso = null;
		try {

			// Si ricava la connessione
			lConn = getDBConnection();

			// Ricerca del Fascicolo SIGE

			lFasDao = new FascicoloSigeSqlDAO(lConn);
			lFasDao.ricercaFascicoloSigeByKey(aKey);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

			if (lFasMod == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non trovato il Fascicolo con ID -> " + aKey);
			if (lFasMod.getSogIdSoggetto() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non esiste il Soggetto per il Fascicolo con ID -> " + aKey);
			if (lFasMod.getRicIdRichiestaSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non esiste la Richiesta per il Fascicolo con ID -> " + aKey);

			lFascicoloEsteso = ricercaRicSogFasSiep(lFasMod, lConn);

			// Ricerca Residenza corrente
			ResidenzaAssociataModel lResAss = ExRicercaResidenzaFascicoloSigeCorrente(
					lFasMod.getIdFascicoloSige());
			if (lResAss != null && lResAss.getResidenza() != null)
				lFascicoloEsteso.setResidenza(lResAss.getResidenza());

			// Ricerca Domicilio Corrente
			lResAss = ExRicercaDomicilioFascicoloSigeCorrente(lFasMod.getIdFascicoloSige());
			if (lResAss != null && lResAss.getResidenza() != null)
				lFascicoloEsteso.setDomicilio(lResAss.getResidenza());

			// Ricerca Magistrato assegnatario
			IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
			lFascicoloEsteso.setMagAssegnatario(
					lMagCtrl.ExRicercaEstesaMagAssCorrenteXFascicolo(lFasMod.getIdFascicoloSige()));

			// Ricerca Luogo di Detenzione corrente
			IFasSigeDetenzione lDetCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
			lFascicoloEsteso
					.setDetenzione(lDetCtrl.ExRicercaUltimaDetenzioneFascicolo(lFasMod.getIdFascicoloSige()));

			// Ricerca Eventuale Udienza corrente
			IUdienzaProcedimentoSige lUdiCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
			lFascicoloEsteso.setUdienzaProcedimento(
					lUdiCtrl.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
							lFasMod.getIdFascicoloSige(), "'F','S'"));

			if (complete) {
				lFascicoloEsteso.setUdienzaProcedimento(
						lUdiCtrl.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
								lFasMod.getIdFascicoloSige(), "'F','S'"));
			} else {
				lFascicoloEsteso.setUdienzaProcedimento(
						lUdiCtrl.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
								lFasMod.getIdFascicoloSige(), "'F','S'", false));
			}

			EventoModel eventoAttiArchivio = this.ExRicercaDataInvioAtti(lFasMod.getIdFascicoloSige());
			if (eventoAttiArchivio != null)
				lFascicoloEsteso.setDataInvioAttiInArchivio(eventoAttiArchivio.getDataInvioAtti());

		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSige: Err  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SQLException: " + e);
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSige: Err  " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoloEsteso;
	}

	/**
	 * Ricerca Richiesta, Soggetto ed eventuale Fascicolo SIEP collegati al Fascicolo SIGE passato come
	 * argomento. Tutti i dati vengono restituiti aggregati assieme al Fascicolo SIGE nel
	 * FascicoloSigeEstesoModel.
	 *
	 * @param aFasMod
	 * @param aConn
	 * @param majorOffice
	 * @return FascicoloSigeEstesoModel
	 * @throws Exception
	 */
	private FascicoloSigeEstesoModel ricercaRicSogFasSiep(FascicoloSigeModel aFasMod, Connection aConn)
			throws Exception {

		FascicoloSigeEstesoModel lFascicoloEsteso = null;
		RichiestaSigeDAO lRicDao = null;
		RichiestaSigeModel lRicMod;
		SoggettoModel lSogMod = null;
		FascicoloSiepModel lFasSIEP = null;
		lSogMod = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		try {
			// Ricerca dela Richiesta
			lRicDao = new RichiestaSigeDAO(aConn);
			lRicDao.setIdRichiestaSige(aFasMod.getRicIdRichiestaSige());
			lRicDao.selByKey();
			lRicMod = (RichiestaSigeModel) lRicDao.getModelByKey();

			// Ricerca del Soggetto
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			lSogMod = lSogCtrl.ExRicercaSoggettoByKey(aFasMod.getSogIdSoggetto());

			if (lRicMod != null) {
				if (lRicMod.getFasSieIdFascicoloSiep() != null) {
					// Ricerca del Fascicolo SIEP
					IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();
					lFasSIEP = lCtrlSIEP.ExRicercaFascicoloByKeyNoError(lRicMod.getFasSieIdFascicoloSiep());
				}

				// trascodifica dei codici
				lRicMod.decodifica();
			}

			// trascodifica dei codici
			aFasMod.decodifica();

			// Tutti i model ricavati si aggregano nell'unico FascicoloSigeEsteso
			lFascicoloEsteso = new FascicoloSigeEstesoModel(aFasMod, lSogMod, lRicMod, lFasSIEP);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lRicDao);
		}
		return lFascicoloEsteso;
	}

	/**
	 * Ricerca del Fascicolo Sige dalla chiave formata da : Anno, Progr, Ufficio.
	 *
	 * @param aFascicolo
	 * @return FascicoloSigeEstesoModel
	 * @throws F3BException
	 */
	public FascicoloSigeEstesoModel ExRicercaFascicoloSigeByAnnoNumCodUfficio(FascicoloSigeModel aFascicolo)
			throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		FascicoloSigeEstesoModel lFascicoloEsteso = null;
		try {
			// Si ricava la connessione
			lConn = getDBConnection();

			// Ricerca del Fascicolo SIGE
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizione(aFascicolo);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

			if (lFasMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non trovato!");
			if (lFasMod.getSogIdSoggetto() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						" Non esiste il Soggetto per il Fascicolo con ID -> " + lFasMod.getIdFascicoloSige());
			if (lFasMod.getRicIdRichiestaSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						" Non esiste la Richiesta per il Fascicolo con ID -> "
								+ lFasMod.getIdFascicoloSige());
			lFascicoloEsteso = ricercaRicSogFasSiep(lFasMod, lConn);
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByAnnoNumCodUfficio: Err  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByAnnoNumCodUfficio: Err  " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoloEsteso;
	}

	/**
	 * Ricerca Estesa del Fascicolo Sige dalla chiave formata da : Anno, Progr, Ufficio.
	 *
	 * @param aFascicolo
	 * @return FascicoloSigeEstesoModel
	 * @throws F3BException
	 */
	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio(
			FascicoloSigeModel aFascicolo) throws F3BException {

		return ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio(aFascicolo, false);
	}

	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio(
			FascicoloSigeModel aFascicolo, boolean complete) throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		FascicoloSigeEstesoModel lFascicoloEsteso = null;
		try {

			// Si ricava la connessione
			lConn = getDBConnection();

			// Ricerca del Fascicolo SIGE
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizione(aFascicolo);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

			if (lFasMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non trovato !");
			if (lFasMod.getSogIdSoggetto() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						" Non esiste il Soggetto per il Fascicolo con ID -> " + lFasMod.getIdFascicoloSige());
			if (lFasMod.getRicIdRichiestaSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						" Non esiste la Richiesta per il Fascicolo con ID -> "
								+ lFasMod.getIdFascicoloSige());

			lFascicoloEsteso = ricercaRicSogFasSiep(lFasMod, lConn);

			// Ricerca Residenza corrente
			ResidenzaAssociataModel lResAss = ExRicercaResidenzaFascicoloSigeCorrente(
					lFasMod.getIdFascicoloSige());
			if (lResAss != null && lResAss.getResidenza() != null)
				lFascicoloEsteso.setResidenza(lResAss.getResidenza());

			// Ricerca Domicilio Corrente
			lResAss = ExRicercaDomicilioFascicoloSigeCorrente(lFasMod.getIdFascicoloSige());
			if (lResAss != null && lResAss.getResidenza() != null)
				lFascicoloEsteso.setDomicilio(lResAss.getResidenza());

			// Ricerca Magistrato assegnatario
			IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
			lFascicoloEsteso.setMagAssegnatario(
					lMagCtrl.ExRicercaEstesaMagAssCorrenteXFascicolo(lFasMod.getIdFascicoloSige()));

			// Ricerca Luogo di Detenzione corrente
			IFasSigeDetenzione lDetCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
			lFascicoloEsteso
					.setDetenzione(lDetCtrl.ExRicercaUltimaDetenzioneFascicolo(lFasMod.getIdFascicoloSige()));

			// Ricerca Eventuale Udienza corrente
			IUdienzaProcedimentoSige lUdiCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
			if (complete) {
				lFascicoloEsteso.setUdienzaProcedimento(
						lUdiCtrl.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
								lFasMod.getIdFascicoloSige(), "'F','S'"));
			} else {
				lFascicoloEsteso.setUdienzaProcedimento(
						lUdiCtrl.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
								lFasMod.getIdFascicoloSige(), "'F','S'", false));
			}

		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio: Err  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio: Err  " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoloEsteso;
	}

	public FascicoloSigeModel ExRicercaFascicoloSigeByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeSqlDAO(lConn);
			lFasDao.ricercaFascicoloSigeByKey(aKey);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByKey: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("SQLException: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByKey: Non posso leggere  : " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	/**
	 * Modifica Fascicolo
	 *
	 * @param aFascicoloSige
	 * @param aRichiesta
	 * @throws F3BException
	 */

	public void ExModificaFascicoloSige(FascicoloSigeModel aFascicoloSige, RichiestaSigeModel aRichiesta)
			throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		RichiestaSigeDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			// Aggiornamento Fascicolo
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModelForUpdate(aFascicoloSige);
			lFasDao.update();

			// Aggiornamento Richiesta
			lRicDao = new RichiestaSigeDAO(lConn);
			lRicDao.setDAOFromModelForUpdate(aRichiesta);
			lRicDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FascicoloSigeController.ExModificaFascicoloSige: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("FascicoloSigeController.ExModificaFascicoloSige-> " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Modifica Fascicolo
	 *
	 * @param aFascicoloSige
	 * @param aRichiesta
	 * @throws F3BException
	 */
	public void ExModificaFascicoloSige(FascicoloSigeModel aFascicoloSige) throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		RichiestaSigeDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			// Aggiornamento Fascicolo
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModelForUpdate(aFascicoloSige);
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FascicoloSigeController.ExModificaFascicoloSige: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("FascicoloSigeController.ExModificaFascicoloSige-> " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaFascicoloSige(FascicoloSigeModel aFascicoloSige) throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige());
			lFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExCancellaFascicoloSige: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca paginata Fascicolo Sige (Estesa)
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param aFascicoloSige
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSigeModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSigeEstesaPagina(RicercaFascicoloSigeModel aRicFascModel, int aPage,
			String majorOffice) throws F3BException {

		Connection lConn = null;
		Vector<FascicoloSigeEstesoModel> lFascicoli = new Vector<>();

		FascicoloSigeSqlDAO lFasSigeSqlDao = null;

		try {
			lConn = getDBConnection();

			lFasSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasSigeSqlDao.ricercaFascicoloSigeEstesa(aRicFascModel, majorOffice);
			} else {
				lFasSigeSqlDao.ricercaFascicoloSigeEstesa(aRicFascModel, "");
			}

			lFasSigeSqlDao.startPage(aPage);

			FascicoloSigeEstesoModel lFascicoloEsteso = null;
			while (lFasSigeSqlDao.next()) {
				lFascicoloEsteso = (FascicoloSigeEstesoModel) lFasSigeSqlDao.getModelEsteso();
				lFascicoli.add(lFascicoloEsteso);
			}

			lFasSigeSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaFascicoloSigeEstesaPagina: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSigeSqlDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca paginata Fascicolo Sige per Estremi
	 *
	 * @param aFascicoloSige
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSigeModel
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSigeByEstremiPagina(RicercaFascicoloSigeModel aRicFascModel, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSigeSqlDAO lFasSigeSqlDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiDao = null;
		UdienzaProcedimentoSigeModel lUdiMod = null;
		try {
			lConn = getDBConnection();

			lFasSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSigeSqlDao.ricercaFascicoloSigePerEstremi(aRicFascModel);

			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0
			if (aPage > 0)
				lFasSigeSqlDao.startPage(aPage);
			else
				lFasSigeSqlDao.start();

			FascicoloSigeEstesoModel lFascicoloEsteso = null;
			while (lFasSigeSqlDao.next()) {
				lFascicoloEsteso = (FascicoloSigeEstesoModel) lFasSigeSqlDao.getModelEsteso();

				lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdiDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
						lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige(), "'F','S'");
				lUdiDao.start();
				if (lUdiDao.next()) {
					lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelConDataUdienza();
					lFascicoloEsteso.setUdienzaProcedimento(lUdiMod);
				}
				lUdiDao.stop();

				lFascicoli.add(lFascicoloEsteso);
			}

			lFasSigeSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaFascicoloSigeByEstremiPagina: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSigeSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUdiDao);

			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una RicercaFascicoloSigeEstesa
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param aFascicoloSige
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoloSigeEstesa(RicercaFascicoloSigeModel aRicFascModel,
			String majorOffice) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasSigeSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice)) {
				lFasSigeSqlDao.ricercaFascicoloSigeEstesa(aRicFascModel, majorOffice);
			} else {
				lFasSigeSqlDao.ricercaFascicoloSigeEstesa(aRicFascModel, "");
			}
			lCont = lFasSigeSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExGetNumRicercaFascicoloSigeEstesa: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSigeSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ritorna n.ro di record risultato di una RicercaFascicoloSigeByEstremi
	 *
	 * @param aFascicoloSige
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoloSigeByEstremi(RicercaFascicoloSigeModel aRicFascModel)
			throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasSigeSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSigeSqlDao.ricercaFascicoloSigePerEstremi(aRicFascModel);
			lCont = lFasSigeSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExGetNumRicercaFascicoloSigeByEstremi: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSigeSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * La funzione effettua la ricerca di TUTTE le Sentenze di pertinenza al Procedimento SIGE. Per il momento
	 * questa funzione restituisce la Sentenza legata ad eventuale Fascicolo SIEP di competenza. Quando sarà
	 * creata la struttura dati che lega direttamente Sentenze ad un Fascicolo SIGE, questa funzione dovrà
	 * restituire anche quelle sentenze.
	 *
	 * @param FascicoloSigeEstesoModel
	 *            aFascicolo
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenze(FascicoloSigeEstesoModel aFascicolo) throws F3BException {

		Vector lSentenze = new Vector();

		try {
			// Se il Fascicolo e' associato ad un Fascicolo SIEP di competenza si preleva la Sentenza
			if (aFascicolo.getFascicoloSiep() != null
					&& aFascicolo.getFascicoloSiep().getSentenza() != null) {
				lSentenze.add(aFascicolo.getFascicoloSiep().getSentenza());
			}
		} catch (Exception e) {
			throw new F3BException("FascicoloSigeController.ExRicercaSentenze: " + e);
		} finally {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("finally di ExRicercaSentenze: " + getClass().getName());
		}
		return lSentenze;
	}

	/**
	 * Ricerca Fascicoli Sige Paginata per Soggetto, e filtri aggiuntivi.
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param aSogModel
	 * @param lCodUfficioUtenteConnesso
	 * @param lCodDistretto
	 * @param aPageNum
	 * @return Vettore di FascicoloSigeEsteso
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistretto, int aPageNum, String majorOffice)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();

		FascicoloSigeSqlDAO lFasSigeDao = null;
		FascicoloSigeEstesoModel lFascicolo = null;

		// paolo cherubini per supersoggetto 30/07/2009
		SoggettoSqlDAO lSogSqlDao = null;
		// fine

		try {
			lConn = getDBConnection();

			lFasSigeDao = new FascicoloSigeSqlDAO(lConn);
			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice))
				lFasSigeDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodDistretto,
						majorOffice);
			else
				lFasSigeDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodDistretto,
						"");

			// paolo cherubini per supersoggetto 30/07/2009
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			// fine

			lFasSigeDao.startPage(aPageNum);
			while (lFasSigeDao.next()) {
				lFascicolo = ((FascicoloSigeEstesoModel) lFasSigeDao.getModelEstesoRicSoggettiConProc());
				aSogModel = lFascicolo.getSoggetto();

				// paolo cherubini per supersoggetto 30/07/2009
				lSogSqlDao.ricercaSuperSoggetto("ufficio", lCodUfficioUtenteConnesso, aSogModel,
						"FASCICOLO_SIGE", lCodDistretto);
				aSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
				lFascicolo.getSoggetto().setIdSoggetto(aSogModel.getIdSoggetto());
				lFascicolo.getFascicoloSige().setSogIdSoggetto(aSogModel.getIdSoggetto());
				// fine

				lFascicoli.add(lFascicolo);
			}

			lFasSigeDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoliBySoggettoPagina: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoliBySoggettoPagina: " + e);
		} finally {
			cleanup(lFasSigeDao);
			// paolo cherubini per supersoggetto 30/07/2009
			cleanup(lSogSqlDao);
			// fine
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaFascicoliBySoggetto
	 *
	 * MEV_57: aggiunto parametro di passaggio
	 *
	 * @param aSogModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaFascicoliBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistretto, String majorOffice) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);
			// MEV_57: aggiunto parametro di passaggio
			if (StringUtils.checkValidValue(majorOffice))
				lFasSqlDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodDistretto,
						majorOffice);
			else
				lFasSqlDao.ricercaFascicoliBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodDistretto,
						"");
			lCont = lFasSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExGetNumRicercaFascicoliBySoggetto: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Fascicoli Sige di un Soggetto (Nel model aSogModel è valorizzato l'ID) in base ai parametri di
	 * ricerca selezionati.
	 * <p>
	 *
	 * @param aSogModel
	 * @param strCodUfficioUtenteConnesso
	 * @param lCodDistretto
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaFascSigeDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		SoggettoSqlDAO lSogDao = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);

			lFasSqlDao.ricercaFascicoliDelSoggetto(aSogModel, strCodUfficioUtenteConnesso, lCodDistretto);
			lFasSqlDao.start();

			FascicoloSigeEstesoModel lFascicolo = null;
			while (lFasSqlDao.next()) {
				// lFascicolo =
				// ((FascicoloSigeEstesoModel)lFasSqlDao.getModelEstesoRicSoggettiConProc()
				// ) ;
				lFascicolo = ((FascicoloSigeEstesoModel) lFasSqlDao.getModelEsteso());
				lFascicoli.add(lFascicolo);
			}

			lFasSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSigeController.ExRicercaFascSigeDelSoggetto: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSigeController.ExRicercaFascSigeDelSoggetto: " + e);
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Esecuzione stampa copertina del fascicolo.
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

		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		// Riempie l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO, ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
				ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
				ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
				ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
				ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };
		int aTipoStampa = ICostantiStampaSige.STAMPA_PROCEDIMENTO;
		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, lTipoUfficio,
				null);

		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());

		String lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_ST_001");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * Esecuzione stampa Assegnazione Avvocato al fascicolo.
	 * <p>
	 *
	 * @param aIdFascicolo
	 * @param lTipoUfficio
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaAvvocato(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		// Riempie l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO, ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
				ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
				ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
				ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
				ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };
		int aTipoStampa = ICostantiStampaSige.STAMPA_AVVOCATO;
		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, lTipoUfficio,
				null);
		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_ST_002");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		return lByteArrayOut;
	}

	/**
	 * Esecuzione stampa Assegnazione Avvocato al fascicolo.
	 * <p>
	 *
	 * @param aIdFascicolo
	 * @param lTipoUfficio
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaFoglioComplementare(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel, String templateName) throws F3BException {

		ByteArrayOutputStream lByteArrayOut = null;
		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		// Riempie l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSige.TREE_SOGGETTO, ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO,
				ICostantiStampaSige.TREE_FASCICOLOSIEP, ICostantiStampaSige.TREE_SENTENZA,
				ICostantiStampaSige.TREE_AVVOCATO, ICostantiStampaSige.TREE_LUOGODET,
				ICostantiStampaSige.TREE_MAGISTRATO, ICostantiStampaSige.TREEs_PROVVEDIMENTI,
				ICostantiStampaSige.TREE_TIT_ESE_REF, ICostantiStampaSige.TREE_UDIENZA };
		int aTipoStampa = ICostantiStampaSige.STAMPA_AVVOCATO;
		TreeModel lTree = lCtrlSta.ExPrelevaDatiStampa(aIdFascicolo, aTipoDati, aTipoStampa, lTipoUfficio,
				null);
		// ReportGenerator lReport = new ReportGenerator();
		ReportGenerator lReport = new ReportGenerator(aUtenteModel.getUfficioUtente().getCodUfficio());
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(templateName);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di LogF3B.getLogger()
		siesLogger.warn("NOME TEMPLATE >>>" + lNomeTemplate);
		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
		return lByteArrayOut;
	}

	public ResidenzaAssociataModel ExRicercaResidenzaFascicoloSigeCorrente(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();

		ResidenzaSqlDAO lResDao = null;

		try {
			lConn = getDBConnection();

			lResDao = new ResidenzaSqlDAO(lConn);

			// Residenza Corrente
			lResDao.ricercaResidenzaByFascicoloSige(aIdFascicolo);
			lResAss.setResidenza((ResidenzaModel) lResDao.getModelByKey());
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaResidenzaFascicoloSigeCorrente: " + dex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}
		return lResAss;
	}

	public ResidenzaAssociataModel ExRicercaDomicilioFascicoloSigeCorrente(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;

		ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();

		ResidenzaSqlDAO lResDao = null;

		try {
			lConn = getDBConnection();

			lResDao = new ResidenzaSqlDAO(lConn);

			// Domicilio Corrente
			lResDao.ricercaDomicilioByFascicoloSige(aIdFascicolo);
			lResAss.setResidenza((ResidenzaModel) lResDao.getModelByKey());
		} catch (DAOException dex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + dex);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaDomicilioFascicoloSigeCorrente: " + dex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResDao);
			cleanup(lConn);
		}
		return lResAss;
	}

	/**
	 * <p>
	 * Description: : la funzione inserisce Una occorrenza di ResidenzaAssociata nella tabella
	 * RESIDENZA_FASCICOLO_SIGE
	 *
	 * @param aResidenza
	 *            (ResidenzaAssociataModel)
	 * @return aResidenza (ResidenzaAssociataModel)
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExInserisciResidenzaFascicoloSige(ResidenzaAssociataModel aResidenza)
			throws F3BException {

		Connection lConn = null;

		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSigeDAO lResFascDao = null;

		ResidenzaModel lResidenza = aResidenza.getResidenza();
		ResidenzaFascicoloSigeModel lResidenzaFascicolo = aResidenza.getResidenzaFascicoloSige();

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

			if (lResidenza.getIdResidenza().compareTo(new BigDecimal(0)) != 0) {
				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModel(lResidenza);
				lResDao.setCondizioneUpdate(lResidenza.getIdResidenza());
				lResDao.update();
			}

			// ** Associa la Residenza al Fascicolo
			// ** storicizzando quella eventualmente presente
			// 1- Storicizza l'ultima occorrenza eventualmente presente
			BigDecimal lIdResFasCorrente = this
					.getIdResidenzaFascicoloSigeCorrente(lResidenzaFascicolo.getFasSigeIdFascicoloSige());

			lResFascDao = new ResidenzaFascicoloSigeDAO(lConn);
			if (lIdResFasCorrente != null) {
				lResFascDao.setDataFineValidita(lResidenzaFascicolo.getDataInizioValidita());

				lResFascDao.setCondizioneResFascCorrente(lResidenzaFascicolo.getFasSigeIdFascicoloSige(),
						lResidenza.getCodTipoResidenza().toString());
				lResFascDao.update();
				lResFascDao.stop();
			}

			// 2 - Inserisce la nuova occorrenza
			lResFascDao.setDAOFromModel(lResidenzaFascicolo);
			lResFascDao.insert();

			commit(lConn);
		} catch (DAOException dex) {
			rollback(lConn);
			dex.printStackTrace();
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExInserisciResidenzaFascicoloSige : " + dex);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExInserisciResidenzaFascicoloSige : " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lResFascDao);

			cleanup(lConn);
		}
		return aResidenza;
	}

	private BigDecimal getIdResidenzaFascicoloSigeCorrente(BigDecimal aIdFascicoloSige) throws F3BException {

		Connection lConn = null;

		BigDecimal lId = null;

		ResidenzaFascicoloSigeDAO lSqlDao = null;

		try {
			lConn = getDBConnection();

			lSqlDao = new ResidenzaFascicoloSigeDAO(lConn);
			lSqlDao.setCondizioneResFascCorrente(aIdFascicoloSige, "R");
			ResidenzaFascicoloSigeModel lResFas = (ResidenzaFascicoloSigeModel) lSqlDao.getModelByKey();

			if (lResFas != null) {
				lId = lResFas.getResIdResidenza();
			}
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.getIdResidenzaFascicoloSigeCorrente: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lId;
	}

	/**
	 * <p>
	 * Description: : la funzione modifica la Residenza riferita al Fascicolo SIGE in Oggetto.
	 *
	 * @param aResidenza
	 *            (ResidenzaAssociataModel)
	 * @return aResidenza (ResidenzaAssociataModel)
	 * @throws F3BException
	 */
	public ResidenzaAssociataModel ExModificaResidenzaFascicoloSige(ResidenzaAssociataModel aResidenza)
			throws F3BException {

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di LogF3B.getLogger()
		// siesLogger.debug("ExModificaResidenzaFascicoloSige : inizio");
		Connection lConn = null;

		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSigeDAO lResFascDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		if (aResidenza == null || aResidenza.getResidenza() == null
				|| aResidenza.getResidenzaFascicoloSige() == null)
			throw new SIGEException(F3BException.USER_MESSAGE, "Errore nei dati ");

		ResidenzaModel lResidenza = aResidenza.getResidenza();
		ResidenzaFascicoloSigeModel lResidenzaFascicolo = aResidenza.getResidenzaFascicoloSige();

		BigDecimal lIdResidenzaDaCambiare = lResidenzaFascicolo.getResIdResidenza();

		if (lIdResidenzaDaCambiare == null)
			throw new SIGEException(F3BException.USER_MESSAGE, "Errore nei dati: ID Residenza assente ");

		// Valorizzazione dei campi di aggiornamento
		lResidenza.setCodOperatoreAggiornamento(lResidenza.getCodOperatoreInserimento());
		lResidenza.setCodUfficioAggiornamento(lResidenza.getCodUfficioInserimento());
		lResidenza.setDataAggiornamento(lResidenza.getDataInserimento());
		try {
			lConn = getDBTransaction();

			// Si Controlla se la Residenza da modificare è collegata a più
			// Fascicoli
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			int lNumRes = lResSqlDao.getNumOccorrenzeResSIGE(lIdResidenzaDaCambiare);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
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

				// Si effettua l'update della Relazione Residenza_Fascicolo per
				// la residenza corrente
				lResFascDao = new ResidenzaFascicoloSigeDAO(lConn);
				lResFascDao.setResIdResidenza(lResidenzaFascicolo.getResIdResidenza());
				lResFascDao.setCondizioneDelete(lIdResidenzaDaCambiare,
						lResidenzaFascicolo.getFasSigeIdFascicoloSige());
				lResFascDao.update();
				lResFascDao.stop();
			} else if (lNumRes == 1) {
				// Si oggiorna il record Residenza
				lResDao = new ResidenzaDAO(lConn);
				lResDao.setDAOFromModelForUpdate(lResidenza);
				lResDao.update();
				lResDao.stop();
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore nei dati: nessuna occorrenza residenza ");

			commit(lConn);

			// Aggiornamento dati di uscita
			aResidenza.setResidenza(lResidenza);
			aResidenza.setResidenzaFascicoloSige(lResidenzaFascicolo);

		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExModificaResidenzaFascicoloSige : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExModificaResidenzaFascicoloSige : " + ex);
		} finally {
			cleanup(lResDao);
			cleanup(lResFascDao);
			cleanup(lResSqlDao);
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("ExModificaResidenzaFascicoloSige : fine");

		return aResidenza;
	}

	/**
	 * Ritorna il vettore di ResidenzaAssociataModel riferite al Fascicolo SIGE in esame
	 *
	 * @param aKeyFascicolo
	 * @param aTipoResidenza
	 * @return Vector di ResidenzaAssociataModel
	 * @throws F3BException
	 */
	public Vector ExRicercaResidenzaByProcedimentoSige(BigDecimal aKeyFascicolo, char aTipoResidenza)
			throws F3BException {

		Connection lConn = null;
		Vector lResidenze = new Vector();
		ResidenzaSqlDAO lDao = null;

		try {
			lConn = getDBConnection();
			lDao = new ResidenzaSqlDAO(lConn);
			lDao.ricercaResidenzaByProcedimentoSige(aKeyFascicolo, aTipoResidenza);
			lDao.start();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSigeModel lResFasMod = null;
			while (lDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();

				lResMod = (ResidenzaModel) lDao.getModel();
				lResFasMod = lDao.getModelResidenzaFascicoloSige();
				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSige(lResFasMod);
				lResidenze.add(lResAssMod);
			}
			lDao.stop();
			// if ( lResidenze.size() == 0 )
			// {
			// throw new SIGEException(F3BException.USER_MESSAGE,
			// "Nessun Elemento trovato");
			// }
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaResidenzaByProcedimentoSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lResidenze;
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di una residenza nella tabella
	 * RESIDENZA_FASCICOLO_SIGE
	 *
	 * @param IdResidenza
	 *            : identificatore RESIDENZA, IdFascicolo identificatore del fascicolo
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaResidenzaProcedimentoSige(BigDecimal IdResidenza, BigDecimal IdFascicolo)
			throws F3BException {

		Connection lConn = null;
		ResidenzaFascicoloSigeDAO lResFasSigeDao = null;

		try {
			lConn = getDBConnection();
			lResFasSigeDao = new ResidenzaFascicoloSigeDAO(lConn);
			lResFasSigeDao.setCondizioneDelete(IdResidenza, IdFascicolo);
			lResFasSigeDao.delete();
			lResFasSigeDao.stop();
			lResFasSigeDao.setDataFineValidita(null);
			lResFasSigeDao.setCondizioneUpdate(IdFascicolo, 'R');
			lResFasSigeDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIGEException(F3BException.USER_MESSAGE,
						"Residenza collegata ad altri dati. Impossibile effettuare la Cancellazione!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExCancellaResidenzaProcedimentoSige: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResFasSigeDao);
			cleanup(lConn);
		}
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un domicilio nella tabella
	 * RESIDENZA_FASCICOLO_SIGE
	 *
	 * @param IdResidenza
	 *            : identificatore RESIDENZA, IdFascicolo identificatore del fascicolo
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaDomicilioProcedimentoSige(BigDecimal IdResidenza, BigDecimal IdFascicolo)
			throws F3BException {

		Connection lConn = null;
		ResidenzaFascicoloSigeDAO lResFasSigeDao = null;

		try {
			lConn = getDBConnection();
			lResFasSigeDao = new ResidenzaFascicoloSigeDAO(lConn);
			lResFasSigeDao.setCondizioneDelete(IdResidenza, IdFascicolo);
			lResFasSigeDao.delete();
			lResFasSigeDao.stop();
			lResFasSigeDao.setDataFineValidita(null);
			lResFasSigeDao.setCondizioneUpdate(IdFascicolo, 'D');
			lResFasSigeDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED) {
				throw new SIGEException(F3BException.USER_MESSAGE,
						"Domicilio collegato ad altri dati. Impossibile effettuare la Cancellazione!");
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIGEException(F3BException.USER_MESSAGE,
						"FascicoloSigeController.ExCancellaDOmicilioProcedimentoSige: " + daoEx);
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lResFasSigeDao);
			cleanup(lConn);
		}
	}

	/**
	 * Funzione di ricerca di tutti i Fascicoli SIGE legati ad una specifica Sentenza.
	 *
	 * @param aIdSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSigeBySentenza(BigDecimal aIdSentenza) throws F3BException {

		Connection lConn = null;
		Vector lFascicoliSige = null;
		FascicoloSigeDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizioneByIdSentenza(aIdSentenza);
			lFascicoliSige = new Vector(lFasDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSigeBySentenza: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSigeBySentenza -> " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoliSige;
	}

	/**
	 * Funzione di ricerca di tutti i Fascicoli SIGE legati ad un Fascicolo SIEP.
	 *
	 * @param aIdFascicoloSiep
	 * @param strCodUfficioUtenteConnesso
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoloSigeByFasSiep(BigDecimal aIdFasSiep, String strCodUfficioUtenteConnesso)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoliSige = null;
		FascicoloSigeDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizioneByIdFasSiepCodUfficio(aIdFasSiep, strCodUfficioUtenteConnesso);
			lFascicoliSige = new Vector(lFasDao.getModels());

		} catch (DAOException daoEx) {
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSigeByFasSiep: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("FascicoloSigeController.ExRicercaFascicoloSigeByFasSiep -> " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFascicoliSige;
	}

	/**
	 * Metodo di ricerca di un FASCICOLO_SIGE con stessi: CHIAVE_ANNO, CHIAVE_PROGR, CHIAVE_UFFICIO.
	 * <p>
	 *
	 * @param aFSigeModel
	 * @param lConn
	 * @throws F3BException
	 */
	private String existAnnoProgrSige(FascicoloSigeModel aFSigeModel, Connection lConn) throws F3BException {

		String response = "";
		FascicoloSigeSqlDAO lFSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
		try {
			response = lFSigeSqlDao.ExistAnnoProgrSige(aFSigeModel.getChiaveAnno(),
					aFSigeModel.getChiaveProgr(), aFSigeModel.getChiaveUfficio());
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.existAnnoProgrSige: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFSigeSqlDao);
		}
		return response;
	}

	/**
	 * La funzione effettua l'aggiornamento del Fascicolo SIGE valorizzando i campi relativi alla definizione
	 * manuale (COD_TIPO_DEFINIZIONE, DATA_DEFINIZIONE, DESCR_DEFINIZIONE) e quello relativo allo stato
	 * (COD_STATO).
	 *
	 * @param aFSigeModel
	 * @throws F3BException
	 */
	/*
	 * public void ExDefinizioneManualeFascicoloSige(FascicoloSigeModel aFSigeModel) throws F3BException {
	 * Connection lConn = null;
	 *
	 * FascicoloSigeDAO lFasDao = null;
	 *
	 *
	 * try {
	 *
	 * lConn = getDBConnection(); // Update del Fascicolo Sige lFasDao = new FascicoloSigeDAO(lConn);
	 * lFasDao.setCodOperatoreAggiornamento(aFSigeModel. getCodOperatoreAggiornamento());
	 * lFasDao.setCodUfficioAggiornamento(aFSigeModel .getCodUfficioAggiornamento());
	 * lFasDao.setDataAggiornamento(aFSigeModel.getDataAggiornamento());
	 * lFasDao.setCodStatoFascicolo(aFSigeModel.getCodStatoFascicolo()); lFasDao.setCodTipoDefinizione(
	 * aFSigeModel.getCodTipoDefinizione() ); lFasDao.setDataDefinizione(aFSigeModel.getDataDefinizione());
	 * lFasDao.setDescrDefinizione( aFSigeModel.getDescrDefinizione());
	 * lFasDao.setCondizioneUpdate(aFSigeModel.getIdFascicoloSige());
	 *
	 * lFasDao.update(); lFasDao.stop();
	 *
	 * commit(lConn); } catch (DAOException dEx) { rollback(lConn); throw new
	 * SIGEException(SIGEException.USER_MESSAGE,
	 * "FascicoloSigeController.ExDefinizioneManualeFascicoloSige : " + dEx); } catch (Exception ex) {
	 * rollback(lConn); throw new SIGEException(SIGEException
	 * .USER_MESSAGE,"FascicoloSigeController.ExDefinizioneManualeFascicoloSige : " + ex); } finally {
	 * cleanup(lFasDao); cleanup(lConn); } }
	 */

	public void ExDefinizioneManualeFascicoloSige(ProvvedimentoSigeModel aProvvedimentoSige,
			EventoModel aEvento, FascicoloSigeModel aFascicoloSige) throws F3BException {

		Connection lConn = null;

		FascicoloSigeDAO lFasDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;

		try {

			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lFasDao = new FascicoloSigeDAO(lConn);

			// Si Setta il progressivo...
			lEveSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDao.getProgressivo(aEvento);
			aEvento.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// Inserimento Evento.
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lIdEvento = lEveDao.insert();
			lEveDao.stop();
			aEvento.setIdEvento(lIdEvento);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ExDefinizioneManualeFascicoloSige : Inserito Evento -> " + lIdEvento);

			aProvvedimentoSige.setIdEventoGenerato(lIdEvento);

			// Inserimento Provvedimento.
			lProvDao.setDAOFromModel(aProvvedimentoSige);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();

			aProvvedimentoSige.setIdProvvedimentoSige(lIdProvvedimento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(
					"ExDefinizioneManualeFascicoloSige : Inserito Provvedimento -> " + lIdProvvedimento);

			// Update del Fascicolo Sige
			lFasDao.setCodOperatoreAggiornamento(aFascicoloSige.getCodOperatoreAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFascicoloSige.getCodUfficioAggiornamento());
			lFasDao.setDataAggiornamento(aFascicoloSige.getDataAggiornamento());
			lFasDao.setCodTipoDefinizione(aFascicoloSige.getCodTipoDefinizione());
			lFasDao.setDataDefinizione(aFascicoloSige.getDataDefinizione());
			lFasDao.setDescrDefinizione(aFascicoloSige.getDescrDefinizione());
			lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige());

			lFasDao.update();
			lFasDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ExDefinizioneManualeFascicoloSige : Modificato Fascicolo Sige -> "
					+ aFascicoloSige.getIdFascicoloSige());

			commit(lConn);
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExDefinizioneManualeFascicoloSige : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExDefinizioneManualeFascicoloSige : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
	}

	public void ExModificaDefinizioneManualeFascicoloSige(ProvvedimentoSigeModel aProvvedimentoSige,
			EventoModel aEvento, FascicoloSigeModel aFascicoloSige) throws F3BException {

		Connection lConn = null;

		FascicoloSigeDAO lFasDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;

		try {

			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lFasDao = new FascicoloSigeDAO(lConn);

			// Modifica Evento.
			lEveDao.setDAOFromModelForUpdate(aEvento);
			lEveDao.update();

			// Modifica Provvedimento.
			lProvDao.setDAOFromModelForUpdate(aProvvedimentoSige);
			lProvDao.update();

			// Update del Fascicolo Sige
			lFasDao.setCodOperatoreAggiornamento(aFascicoloSige.getCodOperatoreAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFascicoloSige.getCodUfficioAggiornamento());
			lFasDao.setDataAggiornamento(aFascicoloSige.getDataAggiornamento());
			lFasDao.setCodTipoDefinizione(aFascicoloSige.getCodTipoDefinizione());
			lFasDao.setDataDefinizione(aFascicoloSige.getDataDefinizione());
			lFasDao.setDescrDefinizione(aFascicoloSige.getDescrDefinizione());
			lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExModificaDefinizioneManualeFascicoloSige : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExModificaDefinizioneManualeFascicoloSige : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lProvDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	public void ExAnnullaDefinizioneManualeFascicoloSige(ProvvedimentoSigeModel aProvvedimentoSige,
			EventoModel aEvento, FascicoloSigeModel aFascicoloSige) throws F3BException {

		Connection lConn = null;

		FascicoloSigeDAO lFasDao = null;
		ProvvedimentoSigeDAO lProvDao = null;
		EventoDAO lEveDao = null;
		CampoNotaDAO lCampoNotaDao = null;

		try {

			lConn = getDBConnection();
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lEveDao = new EventoDAO(lConn);
			lFasDao = new FascicoloSigeDAO(lConn);

			// Update del Fascicolo Sige
			lFasDao.setCodOperatoreAggiornamento(aFascicoloSige.getCodOperatoreAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFascicoloSige.getCodUfficioAggiornamento());
			lFasDao.setDataAggiornamento(aFascicoloSige.getDataAggiornamento());
			lFasDao.setCodTipoDefinizione(aFascicoloSige.getCodTipoDefinizione());
			lFasDao.setDataDefinizione(aFascicoloSige.getDataDefinizione());
			lFasDao.setDescrDefinizione(aFascicoloSige.getDescrDefinizione());
			lFasDao.setCondizioneUpdate(aFascicoloSige.getIdFascicoloSige());
			lFasDao.update();

			// cancellazione del PROVVEDIMENTO_SIGE
			lProvDao.selCondizioneByKey(aProvvedimentoSige.getIdProvvedimentoSige());
			lProvDao.delete();

			// MERGE v10: cancellazione preventiva dei Campi Note collegati
			lCampoNotaDao = new CampoNotaDAO(lConn);
			lCampoNotaDao.setCondizioneEvento(aEvento.getIdEvento());
			lCampoNotaDao.delete();

			// Cancellazione dell'EVENTO
			lEveDao.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDao.delete();
			lEveDao.stop();

			commit(lConn);
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExAnnullaDefinizioneManualeFascicoloSige : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExAnnullaDefinizioneManualeFascicoloSige : " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lProvDao);
			cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo di inserimento soggetto paolo cherubini x supersoggetto Se il soggetto è gia collegato ad un
	 * fascicolo SIEP, SIUS o SIGE viene duplicato
	 * <p>
	 *
	 * @param aFSigeModel
	 * @param lConn
	 * @throws F3BException
	 */
	private FascicoloSigeModel SuperSoggetto(FascicoloSigeModel aFSigeModel, Connection lConn)
			throws F3BException {

		SoggettoSqlDAO lSogSqlDao = null;
		SoggettoDAO lSogDao = null;
		SoggettoModel lSogMod = null;
		try {
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.getCountFascicoliPerSoggetto(aFSigeModel.getSogIdSoggetto());
			lSogSqlDao.start();
			lSogSqlDao.next();
			BigDecimal lCount = lSogSqlDao.getBigDecimal("HowManyRecords");
			lSogSqlDao.stop();
			if (lCount.intValue() > 0) {
				lSogDao = new SoggettoDAO(lConn);
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(aFSigeModel.getSogIdSoggetto());
				lSogMod = (SoggettoModel) lSogSqlDao.getModelByKey();
				lSogMod.setCodOperatoreInserimento(aFSigeModel.getCodOperatoreInserimento());
				lSogMod.setCodUfficioInserimento(aFSigeModel.getCodUfficioInserimento());
				lSogMod.setDataInserimento(DateUtils.getSysDate());
				lSogMod.setCodOperatoreAggiornamento(null);
				lSogMod.setCodUfficioAggiornamento(null);
				lSogMod.setDataAggiornamento(null);
				lSogMod.setIdSoggetto(null);
				lSogDao.setDAOFromModel(lSogMod);
				BigDecimal lSequence = lSogDao.insert();
				aFSigeModel.setSogIdSoggetto(lSequence);
			}
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.SuperSoggetto: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lSogDao);
			cleanup(lSogSqlDao);
		}
		return aFSigeModel;

	}

	/**
	 * Ricerca Fascicoli SIGE afferenti a un Fascicolo SIEP da individuare attraverso i parametri di ricerca
	 * (Progressivo, Anno, Ufficio ...)
	 *
	 * @param aFascicoloSiep
	 * @param aCodUfficioSige
	 * @return Vettore di FascicoloSigeEstesoModel
	 * @throws F3BException
	 */
	// 20181031: aggiunto parametro di passaggio
	public Vector ExRicercaFasSigeByDatiFasSiep(FascicoloSiepModel aFascicoloSiep, String aCodUfficioSige,
			String majorOffice) throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		FascicoloSiepSqlDAO lFasDao = null;
		// SoggettoSqlDAO lSoggDao = null;
		// SentenzaSqlDAO lSentDao = null;
		// StatoProcedimentoSqlDAO lStaDao = null;
		FascicoloSigeSqlDAO lFasSigeDao = null;

		try {
			lConn = getDBConnection();

			lFasDao = new FascicoloSiepSqlDAO(lConn);
			// lSoggDao = new SoggettoSqlDAO(lConn);
			// lSentDao = new SentenzaSqlDAO(lConn);
			// lStaDao = new StatoProcedimentoSqlDAO(lConn);

			lFasDao.ricercaFascicoloByProgrAnnoDescrComune(aFascicoloSiep, majorOffice);
			Vector lFascicoliSiep = new Vector(lFasDao.getModels());
			if (lFascicoliSiep.size() == 0)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento Trovato");
			if (lFascicoliSiep.size() > 1)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Fascicolo SIEP non individuato correttamente!");

			FascicoloSiepModel lFascicolo = (FascicoloSiepModel) lFascicoliSiep.firstElement();

			// SoggettoModel lSoggMod = null;
			// SentenzaModel lSentMod = null;

			lFasSigeDao = new FascicoloSigeSqlDAO(lConn);
			lFasSigeDao.ricercaFasSigePerIdFasSiep(lFascicolo.getIdFascicoloSiep(), aCodUfficioSige);

			lFasSigeDao.start();

			FascicoloSigeEstesoModel lFasEsteso = null;
			while (lFasSigeDao.next()) {
				lFasEsteso = (FascicoloSigeEstesoModel) lFasSigeDao.getModelEsteso();
				lFasEsteso.setFascicoloSiep(lFascicolo);
				lFascicoli.add(lFasEsteso);
			}

			lFasSigeDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE,
						"Nessun Procedimento SIGE collegato al procedimento SIEP indicato!");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaFasSigeByDatiFasSiep: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lFasSigeDao);

			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli Sige che riferiscono a una Sentenza (Nel model aSenModel è valorizzato l'ID). Il
	 * parametro aSenModel.CodUfficioInserimento, se valorizzato, consente l'individuazione del Codice Ufficio
	 * SIGE a cui va ristretta la ricerca Fascicoli SIGE.
	 * <p>
	 *
	 * @param aSenModel
	 * @param strCodUfficioUtenteConnesso
	 * @param lCodDistretto
	 * @param majorOffice
	 * @return Vector
	 * @throws F3BException
	 */
	// 20181031: aggiunto parametro di passaggio
	public Vector ExRicercaFasSigePerLaSentenza(SentenzaModel aSenModel, String majorOffice)
			throws F3BException {

		Connection lConn = null;

		Vector lFascicoli = new Vector();

		SentenzaSqlDAO lSenSqlDao = null;
		FasSigeSentenzaSqlDAO lFSSSqlDao = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		RichiestaSigeDAO lRicDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiDao = null;
		FascicoloSigeModel lFasMod;
		FascicoloSiepModel lFasSIEP = null;

		try {
			lConn = getDBConnection();

			lFSSSqlDao = new FasSigeSentenzaSqlDAO(lConn);
			// 20181031: aggiunto parametro di passaggio
			lFSSSqlDao.ricercaFasSIGEPerIdSentenza(aSenModel, majorOffice);
			lFSSSqlDao.start();

			while (lFSSSqlDao.next()) {
				FascicoloSigeEstesoModel lFasEsteso = new FascicoloSigeEstesoModel();
				FascicoloSigeModel lFascicolo = (FascicoloSigeModel) lFSSSqlDao.getFasSigePerIdSentenza();

				// Ricerca del Fascicolo SIGE
				lFasSqlDao = new FascicoloSigeSqlDAO(lConn);
				lFasSqlDao.ricercaFascicoloSigeByKey(lFascicolo.getIdFascicoloSige());
				lFasMod = (FascicoloSigeModel) lFasSqlDao.getModelByKey();

				if (lFasMod == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Non trovato il Fascicolo con ID -> " + lFascicolo.getIdFascicoloSige());
				if (lFasMod.getSogIdSoggetto() == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Non esiste il Soggetto per il Fascicolo con ID -> "
									+ lFascicolo.getIdFascicoloSige());
				if (lFasMod.getRicIdRichiestaSige() == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Non esiste la Richiesta per il Fascicolo con ID -> "
									+ lFascicolo.getIdFascicoloSige());

				// Ricerca dela Soggetto
				lSogSqlDao = new SoggettoSqlDAO(lConn);
				lSogSqlDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				SoggettoModel lSogMod = (SoggettoModel) lSogSqlDao.getModelByKey();

				// Ricerca della Richiesta
				lRicDao = new RichiestaSigeDAO(lConn);
				lRicDao.setIdRichiestaSige(lFasMod.getRicIdRichiestaSige());
				lRicDao.selByKey();
				RichiestaSigeModel lRicMod = (RichiestaSigeModel) lRicDao.getModelByKey();

				if (lRicMod != null) {
					if (lRicMod.getFasSieIdFascicoloSiep() != null) {
						// Ricerca del Fascicolo SIEP
						IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();
						lFasSIEP = lCtrlSIEP
								.ExRicercaFascicoloByKeyNoError(lRicMod.getFasSieIdFascicoloSiep());
					} else {
						// Ricerca Sentenza
						lFasSIEP = new FascicoloSiepModel();
						lSenSqlDao = new SentenzaSqlDAO(lConn);
						lSenSqlDao.ricercaSentenzaBykey(aSenModel.getIdSentenza());
						SentenzaModel lSenMod = (SentenzaModel) lSenSqlDao.getModelByKey();
						if (lSenMod == null)
							throw new F3BException(F3BException.USER_MESSAGE,
									"Sentenza associata non trovata");
						lFasSIEP.setSentenza(lSenMod);
					}

					// trascodifica dei codici
					lRicMod.decodifica();
				}

				// trascodifica dei codici
				lFasMod.decodifica();

				// Tutti i model ricavati si aggregano nell'unico
				// FascicoloSigeEsteso
				lFasEsteso = new FascicoloSigeEstesoModel(lFasMod, lSogMod, lRicMod, lFasSIEP);

				// Ricerca dell' Udienza.
				lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdiDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
						lFascicolo.getIdFascicoloSige(), "'F','S'");
				UdienzaProcedimentoSigeModel lUdiMod = null;
				lUdiDao.start();
				if (lUdiDao.next()) {
					lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelConDataUdienza();
					lFasEsteso.setUdienzaProcedimento(lUdiMod);
				}

				lFascicoli.add(lFasEsteso);
			}

			lFasSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FascicoloSigeController.ExRicercaFasSigePerLaSentenza: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException("FascicoloSigeController.ExRicercaFasSigePerLaSentenza: " + e);
		} finally {
			cleanup(lFSSSqlDao);
			cleanup(lFasSqlDao);
			cleanup(lSogSqlDao);
			cleanup(lSenSqlDao);
			cleanup(lRicDao);
			cleanup(lUdiDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli Unificati.
	 * <p>
	 *
	 * @param FascicoloSigeModel
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector<FascicoloSigeModel> ExRicercaElencoFascicoliUnificati(FascicoloSigeModel aModel)
			throws F3BException {

		Connection lConn = null;

		Vector<FascicoloSigeModel> lFascicoli = new Vector<>();
		FascicoloSigeSqlDAO lFasSqlDao = null;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.

			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);

			lFasSqlDao.ricercaElencoFascicoliUnificati(aModel);
			lFasSqlDao.start();

			FascicoloSigeModel lFascicolo = null;
			while (lFasSqlDao.next()) {
				lFascicolo = (FascicoloSigeModel) lFasSqlDao.getModel();
				lFascicoli.add(lFascicolo);
			}

			lFasSqlDao.stop();

			if (lFascicoli.isEmpty())
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.error("Errore nella Ricerca dei Fascicoli Unificati: Non Trovati ! ");
			// throw new
			// SIUSException(F3BException.USER_MESSAGE,"Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaElencoFascicoliUnificati: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}

		return lFascicoli;
	}

	/**
	 * Ricerca del Fascicolo Sige dalla chiave formata da : Anno, Progr, Ufficio.
	 *
	 * @param aFascicolo
	 * @return FascicoloSigeModel
	 * @throws F3BException
	 */
	public FascicoloSigeModel ExRicercaFascicoloSigeByAnnoProgrCodUfficio(FascicoloSigeModel aFascicolo)
			throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		try {

			// Si ricava la connessione
			lConn = getDBConnection();

			// Ricerca del Fascicolo SIGE
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setCondizione(aFascicolo);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

			if (lFasMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non trovato !");

		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByAnnoProgrCodUfficio: Err  " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByAnnoProgrCodUfficio: Err  " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public EventoModel ExUpdateValidaDefinizioneProcedimento(EventoModel aEvento,
			FascicoloSigeModel aFascicolo) throws F3BException {

		Connection lConn = null;
		EventoModel lEveMod = new EventoModel(aEvento);
		EventoDAO lEveDaoBlob = null;
		FascicoloSigeDAO lFasDao = null;

		try {
			lConn = getDBTransaction();

			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);
			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			lFasDao = new FascicoloSigeDAO(lConn);
			// Aggiornamento Fascicolo
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModelForUpdateCodStato(aFascicolo);
			lFasDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.info("DAOException: " + daoEx);
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException(
					"FascicoloSigeController.ExUpdateValidaDefinizioneProcedimento : " + daoEx);
		} catch (Exception ex) {
			siesLogger.info("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("FascicoloSigeController.ExUpdateValidaDefinizioneProcedimento : " + ex);
		} finally {
			cleanup(lConn);
			cleanup(lEveDaoBlob);
			cleanup(lFasDao);
		}

		return lEveMod;
	}

	/**
	 * Funzione di inserimento di un nuovo FascicoloSige e Richiesta associata per Soggetto IGNOTO.
	 */
	public FascicoloSigeModel ExInserisciFascicoloSigeSoggettoIgnoto(FascicoloSigeModel aFascicoloSige,
			RichiestaSigeModel aRichiestaSige, MagistratoAssegnatarioModel aMagistrato,
			FasSigeDetenzioneModel aDetenzione) throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;
		FascicoloSigeSqlDAO lFasSqlDao = null;
		FascicoloSigeModel lFasMod = null;
		RichiestaSigeModel lRichiestaSige = null;
		BigDecimal lKey = null;

		try {
			lConn = getDBConnection();
			lFasMod = new FascicoloSigeModel(aFascicoloSige);
			if (aRichiestaSige != null) {
				lRichiestaSige = InserisciRichiestaSige(aRichiestaSige, lConn);
				lFasMod.setRicIdRichiestaSige(lRichiestaSige.getIdRichiestaSige());
			}

			// Si apre la connessione al SqlDAO per calcolare il Progressivo
			lFasSqlDao = new FascicoloSigeSqlDAO(lConn);

			// Si ricava la CHIAVE PROGR del Fascicolo SIGE da inserire
			BigDecimal lChiaveProgr = lFasSqlDao.getProgressivoFascicoloSige(aFascicoloSige);
			lFasMod.setChiaveProgr(new BigDecimal(lChiaveProgr.intValue() + 1));

			// Inserimento Fascicolo
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModel(lFasMod);
			lKey = lFasDao.insert();
			lFasMod.setIdFascicoloSige(lKey);

			// Eventuale Inserimento Magistrato Assegnatario
			if (aMagistrato != null) {
				aMagistrato.setFasSigeIdFascicoloSige(lKey);
				InserisciMagistratoAssegnatario(aMagistrato, lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Inserito Magistrato Assegnatario ->" + aMagistrato.toString());
			}

			// Copia di eventuali Riferimenti a Sentenza e Reati legati a
			// Fascicolo SIEP
			if (lRichiestaSige != null && lRichiestaSige.getFasSieIdFascicoloSiep() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Associazione a Sentenza e reati SIEP");
				InserisciSentenzaSiep(lRichiestaSige, lFasMod.getIdFascicoloSige(), lConn);
			}

			// Inserimento di Riferimento a Luogo Detenzione o Altra Causa SIEP
			if (aDetenzione != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
				// istanza siesLogger al posto di mLog
				siesLogger.debug("Associazione a Luogo Detenzione (Altra Causa) SIEP");
				aDetenzione.setFasIdFasSige(lFasMod.getIdFascicoloSige());
				InserisciLuogoDetenzioneSIEP(aDetenzione, lConn);
			}
			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"FascicoloSigeController.ExInserisciFascicoloSigeSoggettoIgnoto DAOException: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExInserisciFascicoloSigeSoggettoIgnoto Exception : " + e);
		} finally {
			cleanup(lFasDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasSqlDao);
			cleanup(lConn);
		}

		return lFasMod;
	}

	@Override
	public Vector<FascicoloSigeEstesoModel> ExRicercaStatisticaFascicoloSigeByEstremi(
			RicercaFascicoloSigeModel aRicFascModel) throws F3BException {

		Connection lConn = null;
		Vector<FascicoloSigeEstesoModel> lFascicoli = new Vector<>();

		FascicoloSigeSqlDAO lFasSigeSqlDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiDao = null;
		UdienzaProcedimentoSigeModel lUdiMod = null;
		try {
			lConn = getDBConnection();

			lFasSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
			// lFasSigeSqlDao.ricercaFascicoloSigePerEstremi(aRicFascModel);
			lFasSigeSqlDao.ricercaStatisticaFascicoloSigePerEstremi(aRicFascModel);

			// Viene fatta la ricerca non paginata se aPageNum ha un valore <= 0

			lFasSigeSqlDao.start();

			// Ticket#20210928015 - L'aggregazione di tenori stesso fascicolo viene fatta da
			// codice in quanto non effettuabile dalla qeury tramite la LISTAGG
			Vector <FascicoloSigeEstesoModel> lFascicoliAppoggio = new Vector<>();
			while (lFasSigeSqlDao.next()) {
				FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel) lFasSigeSqlDao.getModelEsteso();
				lFascicoliAppoggio.add(lFascicoloEsteso);
			}
			lFasSigeSqlDao.stop();
			
			// Scorro il vettore per eliminare i doppioni ed aggregare le decrizioni
			//siesLogger.debug("Record estratti dalla query: "+lFascicoliAppoggio.size());
			FascicoloSigeEstesoModel lastFascicoloEstaso = null;
			String listaOggetti = "";
			//siesLogger.debug("Inizio ciclo per eliminare aggregare i tenori.. ");
			for (int i=0; i< lFascicoliAppoggio.size(); i++) {
				FascicoloSigeEstesoModel lFascicoloEsteso = lFascicoliAppoggio.elementAt (i);
				if (i==0) {
					//siesLogger.debug("Primo record ");
					if (lFascicoloEsteso.getFascicoloSige().getDescOggetto()!=null)
						listaOggetti = lFascicoloEsteso.getFascicoloSige().getDescOggetto()+";";
					
					lFascicoloEsteso.getFascicoloSige().setDescOggetto(listaOggetti);
				}
				else if (lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige().compareTo(lastFascicoloEstaso.getFascicoloSige().getIdFascicoloSige())==0) {
					// Stesso fascicolo, concateno gli oggetti
					if (lFascicoloEsteso.getFascicoloSige().getDescOggetto()!=null)
						listaOggetti = listaOggetti + "\n"+ lFascicoloEsteso.getFascicoloSige().getDescOggetto()+";";
					
					lFascicoloEsteso.getFascicoloSige().setDescOggetto(listaOggetti);
				}
				else {
					// Ho cambiato fascicolo, aggiungo precedente model al vettore di output
					lFascicoli.add(lastFascicoloEstaso);
					
					if (lFascicoloEsteso.getFascicoloSige().getDescOggetto()!=null)
						listaOggetti = lFascicoloEsteso.getFascicoloSige().getDescOggetto()+";";					

					lFascicoloEsteso.getFascicoloSige().setDescOggetto(listaOggetti);					
				}
				lastFascicoloEstaso = lFascicoloEsteso;
			}
			
			// Aggiungo l'ultimo Fascicolo trattato
			lFascicoli.add(lastFascicoloEstaso);
			
			// A questo punto ho il vettore dei fascicoli trovati privi di doppioni e con gli oggetti 
			// concatenati
			for (int i=0; i< lFascicoli.size(); i++) {
				FascicoloSigeEstesoModel lFascicoloEsteso = lFascicoli.elementAt (i);
				
				lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdiDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
						lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige(), "'F','S'");
				lUdiDao.start();
				if (lUdiDao.next()) {
					lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelConDataUdienza();
					lFascicoloEsteso.setUdienzaProcedimento(lUdiMod);
				}
				lUdiDao.stop();				
			}
			
			
/*			VECCHIO CODICE COMENTATO E SOSTITUITO
			FascicoloSigeEstesoModel lFascicoloEsteso = null;
			while (lFasSigeSqlDao.next()) {
				lFascicoloEsteso = (FascicoloSigeEstesoModel) lFasSigeSqlDao.getModelEsteso();

				lUdiDao = new UdienzaProcedimentoSigeSqlDAO(lConn);
				lUdiDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
						lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige(), "'F','S'");
				lUdiDao.start();
				if (lUdiDao.next()) {
					lUdiMod = (UdienzaProcedimentoSigeModel) lUdiDao.getModelConDataUdienza();
					lFascicoloEsteso.setUdienzaProcedimento(lUdiMod);
				}
				lUdiDao.stop();

				lFascicoli.add(lFascicoloEsteso);
			}

			lFasSigeSqlDao.stop();
			*/
			// Ticket#20210928015 - FINE
			if (lFascicoli.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaFascicoloSigeByEstremiPagina: " + daoEx);
		} catch (Exception e) {
			e.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasSigeSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUdiDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	@Override
	public EventoModel ExInserisciDataInvioAtti(ProvvedimentoSigeModel lProMod, EventoModel aEventoModel)
			throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ProvvedimentoSigeDAO lProvDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);

			// Si Setta il progressivo...
			lEveSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEveSqlDao.getProgressivo(aEventoModel);
			aEventoModel.setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// Inserimento Evento.
			lEveDao.setDAOFromModel(aEventoModel);
			BigDecimal lIdEvento = lEveDao.insert();
			lEveDao.stop();
			aEventoModel.setIdEvento(lIdEvento);

			lProMod.setIdEventoGenerato(lIdEvento);

			// Inserimento Provvedimento.
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lProvDao.setDAOFromModel(lProMod);
			BigDecimal lIdProvvedimento = lProvDao.insert();
			lProvDao.stop();

			lProMod.setIdProvvedimentoSige(lIdProvvedimento);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ExInserisciDataInvioAtti : Inserito Evento -> " + lIdEvento);
			commit(lConn);

			aEventoModel = this.ExRicercaDataInvioAtti(lIdEvento);
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExInserisciDataInvioAtti : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExInserisciDataInvioAtti : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lProvDao);
			cleanup(lConn);
		}

		return aEventoModel;
	}

	@Override
	public EventoModel ExModificaDataInvioAtti(EventoModel aEventoModel) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataInvioAtti(aEventoModel.getDataInvioAtti());
			lEveDao.setCodTipologiaInvioAtti(aEventoModel.getCodTipologiaInvioAtti());
			lEveDao.setDescrizioneTipologiaInvioAtti(aEventoModel.getDescrizioneInvioAtti());
			lEveDao.selCondizione(aEventoModel);
			lEveDao.update();
			lEveDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ExInserisciDataInvioAtti : Modificato Evento -> " + aEventoModel.getIdEvento());
			commit(lConn);
			aEventoModel = this.ExRicercaDataInvioAtti(aEventoModel.getIdEvento());
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExModificaDataInvioAtti : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExModificaDataInvioAtti : " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}

		return aEventoModel;
	}

	public EventoModel ExRicercaDataInvioAtti(BigDecimal idFascicolo) throws F3BException {

		EventoSqlDAO lEveSqlDao = null;
		Connection lConn = null;
		EventoModel evento = null;
		try {
			lConn = getDBConnection();

			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaDataInvioAtti(idFascicolo);
			lEveSqlDao.start();

			if (lEveSqlDao.next())
				evento = (EventoModel) lEveSqlDao.getModel();

			lEveSqlDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ExRicercaDataInvioAtti ");
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaDataInvioAtti : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExRicercaDataInvioAtti : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		return evento;
	}

	public void ExCancellaDataInvioAttiInArchivio(BigDecimal idEvento) throws F3BException {

		Connection lConn = null;
		EventoDAO lEveDao = null;
		ProvvedimentoSigeDAO lProvDao = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);

			// Cancellazione Provvedimento.
			lProvDao = new ProvvedimentoSigeDAO(lConn);
			lProvDao.selCondizioneByIdEvento(idEvento);
			lProvDao.delete();
			lProvDao.stop();

			// Cancellazione Evento.
			lEveDao.selCondizioneUpdate(idEvento);
			lEveDao.delete();
			lEveDao.stop();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("ExInserisciDataInvioAtti : Cancellato Evento -> " + idEvento);
			commit(lConn);
		} catch (DAOException dEx) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExInserisciDataInvioAtti : " + dEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"FascicoloSigeController.ExInserisciDataInvioAtti : " + ex);
		} finally {
			cleanup(lEveDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lProvDao);
			cleanup(lConn);
		}
	}

	private void InserisciResidenzeSiep(BigDecimal idFascicoloSige, BigDecimal idFascicoloSiep,
			Connection conn) throws F3BException {

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		ResidenzaFascicoloSiepDAO resSiep = null;
		ResidenzaFascicoloSigeDAO resSige = null;

		try {
			resSiep = new ResidenzaFascicoloSiepDAO(conn);
			resSige = new ResidenzaFascicoloSigeDAO(conn);

			resSiep.setCondizioneIdFascicoloSiep(idFascicoloSiep);
			resSiep.setFasSieIdFascicoloSiep(idFascicoloSiep);
			Vector<ResidenzaFascicoloSiepModel> residenzeSiep = new Vector<ResidenzaFascicoloSiepModel>(
					resSiep.getModels());
			for (ResidenzaFascicoloSiepModel residenzaSiep : residenzeSiep) {
				ResidenzaFascicoloSigeModel residenzaSige = new ResidenzaFascicoloSigeModel();
				residenzaSige.setFasSigeIdFascicoloSige(idFascicoloSige);
				residenzaSige.setDataInizioValidita(new Date());
				residenzaSige.setResIdResidenza(residenzaSiep.getResIdResidenza());
				resSige.setDAOFromModel(residenzaSige);
				resSige.insert();
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(resSiep);
			cleanup(resSige);
		}
	}

	/**
	 * Se il procedimento ha un opposizione al provvedimento definitorio con esito decisione "Accoglie (Fissa
	 * l'udienza)" dopo aver inserito una nuova udienza, viene eliminata la data del provvedimento
	 * definitorio.
	 *
	 * @param aFascicolo
	 * @param lConn
	 *
	 * @throws F3BException
	 */
	public void ExUpdateDataDefinizione(FascicoloSigeModel aFascicolo, Connection lConn) throws F3BException {

		FascicoloSigeDAO lFasDao = null;
		try {

			// Aggiornamento Data Definizione uguale a null
			lFasDao = new FascicoloSigeDAO(lConn);
			lFasDao.setDAOFromModelForUpdateDataDefinizione(aFascicolo);
			lFasDao.update();
			lFasDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.info("DAOException: " + daoEx);
			daoEx.printStackTrace();
			throw new F3BException("FascicoloSigeController.ExUpdateDataDefinizione : " + daoEx);
		} catch (Exception ex) {
			siesLogger.info("Exception: " + ex);
			ex.printStackTrace();
			throw new F3BException("FascicoloSigeController.ExUpdateDataDefinizione : " + ex);
		} finally {
			cleanup(lFasDao);
		}

	}

	/**
	 * Modifica l'ID_FASCICOLO_SIGE_ORIGINE del Fascicolo Sige
	 *
	 * @param aFascicoloSigeModel
	 * @return aFascicoloSigeModel
	 * @throws F3BException
	 */
	public FascicoloSigeModel ExModificaIdFascicoloSigeOrigine(FascicoloSigeModel aFascicoloSigeModel)
			throws F3BException {

		Connection lConn = null;
		FascicoloSigeDAO lFasDao = null;

		try {
			lConn = getDBTransaction();
			lFasDao = new FascicoloSigeDAO(lConn);

			// Set del DAO e aggiornamento del FascicoloSige.
			lFasDao.setDAOFromModelForAggiornaIdFascicoloOrigine(aFascicoloSigeModel);
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
					"FascicoloSigeController.ExModificaIdFascicoloSigeOrigine: " + ex);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return aFascicoloSigeModel;
	}

	public FascicoloSigeModel ExRicercaFascicoloCollegato(BigDecimal idFascicolo) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeSqlDAO(lConn);
			lFasDao.ricercaFascicoloCollegato(idFascicolo);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByKey: Non posso leggere : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaFascicoloSigeByKey: Non posso leggere  : " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public Vector ExRicercaSentenzeAssegnateFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		FascicoloSigeSqlDAO lFasDao = null;
		FascicoloSigeModel lFasMod;
		Vector lSentenze = new Vector();
		SentenzaSqlDAO lSenDao = null;
		SentenzaModel lSen = null;
		EventoSqlDAO lEveSqlDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FascicoloSigeSqlDAO(lConn);
			lFasDao.ricercaFascicoloSigeByKey(aKey);
			lFasMod = (FascicoloSigeModel) lFasDao.getModelByKey();

			// Ricerco il Provvedimento di cumulo sulla tabella Evento,
			// associato al Fascicolo Sige
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(lFasMod.getIdEventoProvvCumulo());
			EventoModel lEveCumuloModel = (EventoModel) lEveSqlDao.getModelByKey();

			// Ricerca Sentenza a partire dal suo ID
			if (lFasMod.getSenIdSentenzaCumulo() != null) {
				lSenDao = new SentenzaSqlDAO(lConn);
				lSenDao.ricercaSentenzaBykey(lFasMod.getSenIdSentenzaCumulo());
				lSen = new SentenzaModel((SentenzaModel) lSenDao.getModelByKey());

				if (lEveCumuloModel != null) {
					// sul dettaglio del Fascicolo Sige devo visualizzare la data
					// di emissione del provvedimento di cumulo, legato alla sentenza,
					// e non la data della sentenza
					if (lEveCumuloModel.getDataEmissione() != null) {
						lSen.setDataProvvedimento(lEveCumuloModel.getDataEmissione());
					}
					// sul dettaglio del Fascicolo Sige devo visualizzare la descrizione
					// del provvedimento di cumulo
					if (lEveCumuloModel.getDescrTipoProvvedimento() != null
							&& lEveCumuloModel.getDescrMotivo() != null) {
						lSen.setDescrProvvCumulo(lEveCumuloModel.getDescrTipoProvvedimento() + " "
								+ lEveCumuloModel.getDescrMotivo());
					}
				}
				lSentenze.add(lSen);
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaSentenzeAssegnateFascicolo: Non posso leggere : "
							+ daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaSentenzeAssegnateFascicolo: Non posso leggere  : " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lSenDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEveSqlDao);
			cleanup(lConn);
		}
		return lSentenze;
	}

	/**
	 * // intervento post COLLAUDO 11.2
	 *
	 * @param aRichiestaSige
	 * @param aIdFasSige
	 * @param aIdFasSigeOrigine
	 * @param aConn
	 * @throws Exception
	 */
	private void inserisciSentenzaNataInSige(RichiestaSigeModel aRichiestaSige, BigDecimal aIdFasSige,
			BigDecimal aIdFasSigeOrigine, Connection aConn) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
		// siesLogger al posto di mLog
		siesLogger.debug("DAOException  InserisciSentenzaSiep: inizio ");
		// Interfaccia del Controller Sentenza Sige
		// IFasSigeSentenza lCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		SentenzaSigeModel lFasSigeSen = new SentenzaSigeModel();

		// Valorizzazione model da passare alla funzione di Assegnazione
		// Sentenza

		lFasSigeSen.setFasIdFascicoloSige(aIdFasSige);
		// La sentenza SIEP viene sempre definita "di competenza"
		lFasSigeSen.setFlagCompetenza("S");
		lFasSigeSen.setCodUfficioInserimento(aRichiestaSige.getCodUfficioInserimento());
		lFasSigeSen.setCodOperatoreInserimento(aRichiestaSige.getCodOperatoreInserimento());
		lFasSigeSen.setDataInserimento(aRichiestaSige.getDataInserimento());
		lFasSigeSen.setCodUfficioAggiornamento(lFasSigeSen.getCodUfficioInserimento());
		lFasSigeSen.setCodOperatoreAggiornamento(lFasSigeSen.getCodOperatoreInserimento());
		lFasSigeSen.setDataAggiornamento(lFasSigeSen.getDataInserimento());
		lFasSigeSen.setFasSieIdFascicoloSiep(aRichiestaSige.getFasSieIdFascicoloSiep());

		// devo recuperare la sentenza legata al fascicolo origine

		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(aIdFasSigeOrigine);
		if (lSentenze != null && lSentenze.size() >= 0) {
			SentenzaSigeModel sigeSentenza = (SentenzaSigeModel) lSentenze.get(0);

			lFasSigeSen.setIdSentenza(sigeSentenza.getIdSentenza());
			lFasSigeSen.setDataIrrevocabilita(sigeSentenza.getDataIrrevocabilita());
		}

		// Data di irrevocabilita' è quella dal Fascicolo SIEP
		lFasSigeSen = assegnaSentenzaFascicoloSige(lFasSigeSen, aConn);

		siesLogger.debug("DAOException  InserisciSentenzaSiep: fine ");
	}

	/**
	 * Funzione che implementa l'inserimento nella tabella FAS_SIGE_SENTENZA del record che esprime
	 * l'associazione tra Fascicolo SIGE e Sentenza. Questa funzione utilizza una connessione passata come
	 * parametro per poter operare in una transazione unica gestita dalla funzione chiamante. aperta e chiusa
	 *
	 * @param aFasSigeSentenza
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	private SentenzaSigeModel assegnaSentenzaFascicoloSige(SentenzaSigeModel aFasSigeSentenza,
			Connection lConn) throws F3BException {

		FasSigeSentenzaDAO lFasDao = null;
		SentenzaSigeModel lFasMod = null;

		try {
			lFasMod = new SentenzaSigeModel(aFasSigeSentenza);
			lFasDao = new FasSigeSentenzaDAO(lConn);
			if (lFasMod.getFlagCompetenza() != null && lFasMod.getFlagCompetenza().equalsIgnoreCase("S")) {
				// Annullamento della competenza di un altro titolo già in tabella
				lFasDao.annullaCompetenzaXFascicolo(lFasMod);
				lFasDao.stop();
			}

			lFasDao.setDAOFromModelForInsert(lFasMod);
			BigDecimal lKey = null;
			lKey = lFasDao.insert();
			lFasMod.setIdFasSigeSentenza(lKey);
		} catch (DAOException ex) {
			if (ex.INTEGRITY_CONSTRAINT_VIOLATED || ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore: La Sentenza risulta già assegnata al Procedimento !");
			else
				throw new F3BException("FasSigeSentenzaController.ExAssegnaSentenzaFascicoloSige:  " + ex);
		} catch (Exception e) {
			throw new F3BException("FasSigeSentenzaController.ExAssegnaSentenzaFascicoloSige : " + e);
		} finally {
			cleanup(lFasDao);
		}
		return lFasMod;
	}

	/**
	 * MEV_65: aggiunti metodi per gestire nuove funzionalita'
	 */
	public Vector<FascicoloSigeEstesoModel> ExRicercaSoggettiSigePerPosizioneGiuridica(
			RicercaFascicoloSigeModel rfsm, int pagine) throws F3BException {

		Connection c = null;
		Vector soggetti = new Vector();
		FascicoloSigeSqlDAO fssdao = null;

		try {
			c = getDBConnection();
			fssdao = new FascicoloSigeSqlDAO(c);
			fssdao.ricercaSoggettiSigePerPosizioneGiuridica(rfsm, pagine);
			// Viene fatta la ricerca paginata
			FascicoloSigeEstesoModel fsem = null;
			if (pagine != 0)
				fssdao.startPage(pagine);
			else
				fssdao.start();
			while (fssdao.next()) {
				fsem = ((FascicoloSigeEstesoModel) fssdao.getModelRicercaSoggettiSigePerPosizioneGiuridica());
				soggetti.add(fsem);
			}
			fssdao.stop();
			if (soggetti.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Soggetto trovato");
		} catch (F3BException f3bEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("F3BException: " + f3bEx);
			throw f3bEx;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaSoggettiSigePerPosizioneGiuridica: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaSoggettiSigePerPosizioneGiuridica: " + e);
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}

		// valore di ritorno
		return soggetti;
	}

	public BigDecimal ExGetNumRicercaSoggettiSigePerPosizioneGiuridica(RicercaFascicoloSigeModel rfsm,
			int pagine) throws F3BException {

		Connection c = null;
		FascicoloSigeSqlDAO fssdao = null;
		BigDecimal bd = new BigDecimal(0);

		try {
			c = getDBConnection();
			fssdao = new FascicoloSigeSqlDAO(c);
			fssdao.ricercaSoggettiSigePerPosizioneGiuridica(rfsm, pagine);
			bd = fssdao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExGetNumRicercaSoggettiSigePerPosizioneGiuridica: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}

		// valore di ritorno
		return bd;
	}

	public Vector<FascicoloSigeEstesoModel> ExRicercaProcedimentiSigeConRicorsoOpposizione(
			RicercaFascicoloSigeModel rfsm, int pagine) throws F3BException {

		Connection c = null;
		Vector procedimenti = new Vector();
		FascicoloSigeSqlDAO fssdao = null;

		try {
			c = getDBConnection();
			fssdao = new FascicoloSigeSqlDAO(c);
			fssdao.ricercaProcedimentiSigeConRicorsoOpposizione(rfsm, pagine);
			// Viene fatta la ricerca paginata
			FascicoloSigeEstesoModel fsem = null;
			if (pagine != 0)
				fssdao.startPage(pagine);
			else
				fssdao.start();
			while (fssdao.next()) {
				fsem = (fssdao.getModelRicercaProcedimentiSigeConRicorsoOpposizione());
				procedimenti.add(fsem);
			}
			fssdao.stop();
			if (procedimenti.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Procedimento trovato");
		} catch (F3BException f3bEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("F3BException: " + f3bEx);
			throw f3bEx;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaProcedimentiSigeConRicorsoOpposizione: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"FascicoloSigeController.ExRicercaProcedimentiSigeConRicorsoOpposizione: " + e);
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}

		// valore di ritorno
		return procedimenti;
	}

	public BigDecimal ExGetNumRicercaProcedimentiSigeConRicorsoOpposizione(RicercaFascicoloSigeModel rfsm,
			int pagine) throws F3BException {

		Connection c = null;
		FascicoloSigeSqlDAO fssdao = null;
		BigDecimal bd = new BigDecimal(0);

		try {
			c = getDBConnection();
			fssdao = new FascicoloSigeSqlDAO(c);
			fssdao.ricercaProcedimentiSigeConRicorsoOpposizione(rfsm, pagine);
			bd = fssdao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(F3BException.USER_MESSAGE,
					"FascicoloSigeController.ExGetNumRicercaProcedimentiSigeConRicorsoOpposizione: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(fssdao);
			cleanup(c);
		}

		// valore di ritorno
		return bd;
	}
	// FINE MEV_65

}