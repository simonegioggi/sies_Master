package siap.sige.stampa.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.opensaml.common.SAMLRuntimeException;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.sico.camponota.dao.CampoNotaSqlDAO;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.cssa.dao.CSSASqlDAO;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.SIAPStampaController;
import siap.sico.stampa.controller.StampaMAUtils;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.report.ReportGenerator;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeSqlDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaSqlDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepPerEventoSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.notiziareato.dao.NotiziaReatoSqlDAO;
import siap.siep.notiziareato.model.NotiziaReatoModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.avvocato.dao.AvvocatoFascicoloSigeSqlDAO;
import siap.sige.avvocato.dao.AvvocatoSqlDAO;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.collegio.dao.CollegioSqlDAO;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegioesperto.dao.CollegioEspertoSqlDAO;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiomagistrato.dao.CollegioMagistratoSqlDAO;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.datiprovsige.dao.DatiProvvedimentoSigeSqlDAO;
import siap.sige.datiprovsige.model.DatiProvvedimentoSigeModel;
import siap.sige.detenzione.dao.FasSigeDetenzioneDAO;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.dao.FascicoloSigeSqlDAO;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.impugnazione.dao.ImpugnazioneSigeSqlDAO;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.magistratoassegnatario.dao.MagistratoAssegnatarioSqlDAO;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.motivazioneprovvedimento.dao.MotivazioneProvvedimentoSigeSqlDAO;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.dao.ProvvedimentoSigeSqlDAO;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiesta.dao.RichiestaSigeDAO;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.richiestaatti.model.FiltroPareriModel;
import siap.sige.richiestaatti.model.ParereModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.dao.FasSigeSentenzaDAO;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.stampa.action.ICostantiStampaSige;
import siap.sige.stampa.util.StampaSigeUtils;
import siap.sige.statistiche.model.EveFasGepSogProvModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.dao.UdienzaSigeSqlDAO;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.dao.PartiUdienzaSqlDAO;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.NotificaParteCivileModel;
import siap.sige.udienzaparti.model.NotificaParteOffesaModel;
import siap.sige.udienzaparti.model.ParteCivileUdienzaModel;
import siap.sige.udienzaparti.model.ParteOffesaUdienzaModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.udienzaprocedimento.dao.ProcedimentixUdienzaSqlDAO;
import siap.sige.udienzaprocedimento.dao.UdienzaProcedimentoSigeSqlDAO;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: StampaSigeController
 * </p>
 * <p>
 * Description: Classe Controller per Stampe SIGE.
 * </p>
 * Classe centralizzata, pubblica una serie di metodi che gestiscono il prelievo dei dati per la generazione
 * base delle pagine XML per la gestione delle stampe in ambito SIGE.
 * </p>
 * <p>
 * Title: StampaSigeController.java
 * </p>
 * <p>
 * Description:
 * <p>
 * Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author sc
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaSigeController extends SIAPStampaController implements IStampaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Metodi pubblici, per entry point richiesta stampe per funzionalità.

	public TreeModel ExPrelevaDatiStampaProcedimentixUdienza(BigDecimal aIdUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String aOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("inizio");

		Connection lConn = null;
		TreeModel lTreeDati = null;

		try {
			lConn = getDBConnection();
			lTreeDati = new TreeModel(CreateRoot(aCodUfficioConnesso, lConn));

			lTreeDati = prelevaDatiProcedimentixUdienza(aIdUdienza, aIdFascicolo, aCodMagistrato, aIdEsperto,
					aStampa, aOrderBy, aStatoProcedimento, aTipoProc, aCodUfficioConnesso, lConn);

		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("fine");
		return lTreeDati;
	}

	public TreeModel ExPrelevaDatiStampaProcedimentixDataUdienza(Date aDataUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String aOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("inizio");

		Connection lConn = null;
		TreeModel lTreeDati = null;

		try {
			lConn = getDBConnection();
			lTreeDati = new TreeModel(CreateRoot(aCodUfficioConnesso, lConn));

			lTreeDati = prelevaDatiProcedimentixUdienza(aDataUdienza, aIdFascicolo, aCodMagistrato,
					aIdEsperto, aStampa, aOrderBy, aStatoProcedimento, aTipoProc, aCodUfficioConnesso, lConn);

		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("fine");
		return lTreeDati;
	}

	/**
	 * Classe Public per il prelievo dati del Fascicolo al fine di creare un report.
	 *
	 * @param aIdFasSige
	 *            l'id del Fascicolo SIGE.
	 * @param aTipoDati
	 *            Quali dati recuperare.
	 * @param aCodiceUfficio
	 *            codice ufficio utente connesso.
	 * @return dati in formato TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdFasSige, int[] aTipoDati, int aTipoStampa,
			String aCodiceUfficio, BigDecimal idEvento) throws F3BException {

		Connection lConn = null;
		TreeModel lTreeDati = null;

		try {
			lConn = getDBConnection();
			lTreeDati = new TreeModel(CreateRoot(aCodiceUfficio, lConn));
			lTreeDati = prelevaDati(idEvento, aIdFasSige, aTipoDati, lTreeDati, aTipoStampa, aCodiceUfficio,
					lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lTreeDati;
	}

	public ByteArrayOutputStream ExPreStampaPareri(ParereModel aParere, Vector aListaRichieste,
			UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione del documento
			XModel lBase = CreateRoot(aParere.getCodUfficioEmittente(), lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			FiltroPareriModel lXParere = new FiltroPareriModel(lBase, aParere.getDataEmissione(),
					aParere.getDataEmissione2(), aParere.getDescrOggettoProcedimento(),
					aParere.getDescrMotivo());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root Xparere " + lXParere);

			// lTreeRoot = new TreeModel(lXParere);
			// STUB: devo fare così perche pare che il parsing del TreeModel non ispeziona gli ancestor. Da
			// approfondire ! Luigi
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(lXParere));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("data di elaborazione " + lXParere.getDataElaborazione());

			// Iterazione della lista di Richiesta Parere
			Iterator lItx = aListaRichieste.iterator();
			ParereModel lParMod = null;
			while (lItx.hasNext()) {
				lParMod = new ParereModel((ParereModel) lItx.next());
				TreeModel lTreeParere = new TreeModel(lParMod);
				lTreeRoot.add(lTreeParere);
			}
		} catch (Exception lEx) {
			throw new SIGEException("StampaController.ExPreStampaPareri : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// generazione del documento con nome template fisso
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_RP_005");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * Classe Public per il prelievo dati SIGE a partire dall' IdEvento, al fine di creare un report.
	 *
	 * @param aIdEvento
	 *            Id dell'Evento. ( Nel caso di Impugnazione IdEvento è l'id dell'impugnazione )
	 * @param aIdFasSige
	 *            l'id del Fascicolo SIGE.
	 * @param aTipoDati
	 *            Quali dati recuperare.
	 * @param aCodiceUfficio
	 *            codice ufficio utente connesso.
	 * @return dati in formato TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdEvento, BigDecimal aIdFasSige, int[] aTipoDati,
			int aTipoStampa, String aCodiceUfficio) throws F3BException {

		Connection lConn = null;
		TreeModel lTreeDati = null;

		try {
			lConn = getDBConnection();
			lTreeDati = new TreeModel(CreateRoot(aCodiceUfficio, lConn));
			lTreeDati = prelevaDati(aIdEvento, aIdFasSige, aTipoDati, lTreeDati, aTipoStampa, aCodiceUfficio,
					lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lTreeDati;
	}

	/**
	 * Classe Public per il prelievo dati SIGE a partire dall' IdEvento per un decreto di unificazione, al
	 * fine di creare un report.
	 *
	 * @param aIdEvento
	 *            Id dell'Evento.
	 * @param aIdFasSige
	 *            l'id del Fascicolo SIGE.
	 * @param aTipoDati
	 *            Quali dati recuperare.
	 * @param aCodiceUfficio
	 *            codice ufficio utente connesso.
	 * @return dati in formato TreeModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdEvento, BigDecimal aIdFasSige,
			BigDecimal aIdFasSigeUnificante, int[] aTipoDati, int aTipoStampa, String aCodiceUfficio)
			throws F3BException {

		Connection lConn = null;
		TreeModel lTreeDati = null;

		try {
			lConn = getDBConnection();
			lTreeDati = new TreeModel(CreateRoot(aCodiceUfficio, lConn));
			lTreeDati = prelevaDati(aIdEvento, aIdFasSige, aIdFasSigeUnificante, aTipoDati, lTreeDati,
					aTipoStampa, aCodiceUfficio, lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}
		return lTreeDati;
	}

	/**
	 * Esegue il prelievo dati del Soggetto.
	 *
	 * @param aIdSoggetto
	 *            l'id del soggetto, prelevato dal fascicolo SIGE.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del soggetto come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiSoggetto(BigDecimal aIdSoggetto, BigDecimal lIdFasSige, Connection aConn)
			throws F3BException {

		TreeModel lTreeSoggetto = null;
		TreeModel lTreeResidenza = null;
		TreeModel lTreeDomicilio = null;
		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lSogSqlDao = new SoggettoSqlDAO(aConn);
			lSogSqlDao.ricercaSoggettoByKey(aIdSoggetto);
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lTreeSoggetto = new TreeModel(lSogModel);
			lTreeResidenza = prelevaDatiResidenzaDomicilio(lIdFasSige, 'R', aConn);
			lTreeDomicilio = prelevaDatiResidenzaDomicilio(lIdFasSige, 'D', aConn);

			// Mette Residenza e domicilio in soggetto
			lTreeSoggetto.add(lTreeResidenza);
			lTreeSoggetto.add(lTreeDomicilio);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiSoggetto : " + lSogModel);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiSoggetto : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiSoggetto : " + sqlEx);
		} finally {
			cleanup(lSogSqlDao);
		}

		return lTreeSoggetto;
	}

	/**
	 * Esegue il prelievo dati Residenza e Domicilio del Soggetto.
	 *
	 * @param aIdFasSige
	 *            l'id del fascicolo Sige.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della residenza del soggetto di un fascicolo Sige come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiResidenzaDomicilio(BigDecimal aIdFasSige, char lTipo, Connection aConn)
			throws F3BException {

		TreeModel lTreeResidenza = null; // new TreeModel();
		ResidenzaSqlDAO lResSqlDao = null;

		try {
			lResSqlDao = new ResidenzaSqlDAO(aConn);
			ResidenzaModel lResMod = new ResidenzaModel();
			lResSqlDao.ricercaResidenzaUltimaByProcedimentoSige(aIdFasSige, lTipo);
			lResMod = (ResidenzaModel) lResSqlDao.getModelByKey();
			if (lResMod != null) {
				lTreeResidenza = new TreeModel(lResMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("##### Dati prelevati nel metodo prelevaDatiResidenzaDomicilio : " + lResMod);
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Nessun dato prelevato nel metodo prelevaDatiResidenzaDomicilio ");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiResidenzaDomicilio : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiResidenzaDomicilio : " + sqlEx);
		} finally {
			cleanup(lResSqlDao);
		}
		return lTreeResidenza;
	}

	/**
	 * Esegue il prelievo dati del Magistrato Assegnatario.
	 *
	 * @param aIdFasSige
	 *            l'id del Fascicolo SIGE.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del Magistrato come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiMagistratoAssegnatario(BigDecimal aIdFasSige, String codUffUtenteConnesso,
			Connection aConn) throws F3BException {

		TreeModel lTreeMagistrato = null; // new TreeModel();

		MagistratoAssegnatarioSqlDAO lMagAssSqlDao = null;
		MagistratoAssegnatarioModel lMagAssMod = null;

		MagistratoSqlDAO lMagDao = null;
		MagistratoModel lMagMod = null;

		try {
			// Magistrato Assegnatario
			lMagAssSqlDao = new MagistratoAssegnatarioSqlDAO(aConn);
			lMagAssSqlDao.ricercaMagistratoAssegnatarioByFascicolo(aIdFasSige, codUffUtenteConnesso);
			lMagAssMod = (MagistratoAssegnatarioModel) lMagAssSqlDao.getModelByKey();

			if (lMagAssMod != null && lMagAssMod.getMagCodMagistrato() != null) {
				lMagDao = new MagistratoSqlDAO(aConn);
				lMagDao.ricercaMagistratoByCod(lMagAssMod.getMagCodMagistrato());
				lMagMod = (MagistratoModel) lMagDao.getModelByKey();
			}

			lTreeMagistrato = new TreeModel(lMagAssMod);

			if (lMagMod != null) // Magistrato
				lTreeMagistrato.add(new TreeModel(lMagMod));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"##### Dati prelevati nel metodo prelevaDatiMagistratoAssegnatario : " + lMagAssMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiMagistratoAssegnatario : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiMagistratoAssegnatario : " + sqlEx);
		} finally {
			cleanup(lMagAssSqlDao);
			cleanup(lMagDao);
		}
		return lTreeMagistrato;
	}

	/**
	 * Creazione della Root del Documento in da stampare
	 *
	 * @param aCodiceUfficio
	 * @param aConn
	 * @return lXMod
	 * @throws F3BException
	 */
	private XModel CreateRoot(String aCodiceUfficio, Connection aConn) throws F3BException {

		UfficioSqlDAO lUDao = null;
		UfficioModel lUffMod = null;
		UfficioModel lUffCAP = null;
		XModel lXMod = null;

		try {
			lUDao = new UfficioSqlDAO(aConn);

			lUDao.selUfficioByCod(aCodiceUfficio);
			lUffMod = (UfficioModel) lUDao.getModelByKey();

			if (lUffMod == null)
				throw new SIGEException(SIGEException.USER_MESSAGE, "Ufficio inesistente");

			// Viene istanziato l' XLM
			lXMod = new XModel();

			lXMod.setTipoUfficio(lUffMod.getCodTipoUfficio().toUpperCase());
			lXMod.setTipoUfficioT1(lUffMod.getDescrTipoUfficio().toUpperCase());
			lXMod.setUfficio(lUffMod.getDescrComune().toUpperCase());
			lXMod.setIndirizzo(lUffMod.getIndirizzo());
			lXMod.setCap(lUffMod.getCap());
			lXMod.setFax(lUffMod.getFax());
			lXMod.setTelefono(lUffMod.getTelefono());
			lXMod.setEMail(lUffMod.getEMail());
			lXMod.setDataElaborazione(DateUtils.getSysDate());

			if (lUffMod.getCodDistretto() != null) {
				lUDao.selUfficioByCod(lUffMod.getCodDistretto());
				lUffCAP = (UfficioModel) lUDao.getModelByKey();
			}
			if (lUffCAP.getDescrComune() != null)
				lXMod.setUfficioCAP(lUffCAP.getDescrComune().toUpperCase());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.CreateRoot : " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIGEException("StampaSigeController.CreateRoot : " + lEx);
		} finally {
			cleanup(lUDao);
		}

		return lXMod;
	}

	/**
	 * Genera il TreeModel per la stampa del Luogo detenzione Corrente
	 *
	 * @param aKey
	 * @return dati del soggetto come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiLuogoDetenzioneSige(BigDecimal aIdFasSige, Connection aConn)
			throws F3BException {

		TreeModel lTreeLuogoDetenzione = null;

		FasSigeDetenzioneDAO lFasDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		IstitutoDetenzioneSqlDAO lIstDetDao = null;

		FasSigeDetenzioneModel lDetenzioneSige = null;
		LuogoDetenzioneModel lLuoDetenzione = null;
		IstitutoDetenzioneModel lIstDetenzione = null;

		try {
			lFasDetDao = new FasSigeDetenzioneDAO(aConn);
			lFasDetDao.setCondizioneUltimoLuogoByFascicolo(aIdFasSige);
			lDetenzioneSige = (FasSigeDetenzioneModel) lFasDetDao.getModelByKey();

			if (lDetenzioneSige != null) {
				if (lDetenzioneSige.getLdIdLuogoDetenzione() != null) {
					// Ricerca Luogo Detenzione
					ILuogoDetenzione lLdCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
					lDetenzioneSige.setLuogoDetenzione(
							lLdCtrl.ExRicercaLuogoDetenzioneByKey(lDetenzioneSige.getLdIdLuogoDetenzione()));
				} else if (lDetenzioneSige.getAcIdAltraCausa() != null) {
					// Ricerca Altra Causa
					IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
					lDetenzioneSige.setAltraCausa(
							lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lDetenzioneSige.getAcIdAltraCausa()));
				} else
					throw new SIGEException(SIGEException.USER_MESSAGE,
							"Errore nella lettura del Luogo di Detenzione SIGE");

				lLuoDetDao = new LuogoDetenzioneSqlDAO(aConn);
				lLuoDetDao.ricercaLuogoDetenzioneByKey(lDetenzioneSige.getLdIdLuogoDetenzione());
				lLuoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();
				if (lLuoDetenzione != null) {
					if (lLuoDetenzione.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetDao = new IstitutoDetenzioneSqlDAO(aConn);
						lIstDetDao.ricercaIstitutoDetenzioneByKey(
								lLuoDetenzione.getIstDetIdIstitutoDetenzione());
						lIstDetenzione = (IstitutoDetenzioneModel) lIstDetDao.getModelByKey();
						lLuoDetenzione.setIstitutoDetenzione(lIstDetenzione);
						lLuoDetenzione.setDescrTipoIstituto(
								lLuoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto() + " di "
										+ lLuoDetenzione.getIstitutoDetenzione().getDescrComune() + " - "
										+ lLuoDetenzione.getIstitutoDetenzione().getIndirizzo());
						lLuoDetenzione.setDescrLuogo(lLuoDetenzione.getIstitutoDetenzione().getDescrComune());
					} else {
						lLuoDetenzione.setDescrTipoIstituto(lLuoDetenzione.getAltroLuogo());
					}
					lTreeLuogoDetenzione = new TreeModel(lLuoDetenzione);
					lTreeLuogoDetenzione.add(new TreeModel(lIstDetenzione));
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"##### Dati prelevati nel metodo prelevaDatiLuogoDetenzioneSige : " + lLuoDetenzione);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiLuogoDetenzioneSige : " + daoEx);
		} catch (SIGEException se) {
			throw se;
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiLuogoDetenzioneSige : " + sqlEx);
		} finally {
			cleanup(lFasDetDao);
			cleanup(lLuoDetDao);
			cleanup(lIstDetDao);
		}
		return lTreeLuogoDetenzione;
	}

	/**
	 * Esegue il prelievo dati del FascicoloSIEP.
	 *
	 * @param lKeyFascicolo
	 *            Chiave del Fascicolo SIEP.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati dell FascicoloSIEP come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiFascicoloSiep(BigDecimal lKeyFascicolo, BigDecimal aIdFasSige,
			Connection aConn) throws F3BException {

		TreeModel lTreeFasMod = new TreeModel();

		SoggettoModel lSoggetto = null;

		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		MisuraCautelareSqlDAO lMisDao = null;
		ReatoSqlDAO lReaDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		PenaComplessivaSqlDAO lPenDao = null;
		PenaAccessoriaSqlDAO lPenAccDao = null;
		PosizioneGiuridicaSqlDAO lPosGiuDao = null;
		BeneficioSqlDAO lBenDao = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		MisuraSicurezzaSqlDAO lMisSicDao = null;
		PenaResiduaSqlDAO lPenaResDao = null;
		EventoSqlDAO lEveSqlDao = null;
		ProvvedimentoSigeSqlDAO lProvSqlDao = null;

		StampaMAUtils lStampa = new StampaMAUtils(); // 03/06/2010

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Preleva dati fascicolo SIEP " + lKeyFascicolo);
		if (lKeyFascicolo != null) {
			try {
				// Fascicolo
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### FASCICOLO SIEP");
				lFasDao = new FascicoloSiepSqlDAO(aConn);
				lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
				FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

				// Cerca il soggetto associato al fascicolo
				lSogSqlDao = new SoggettoSqlDAO(aConn);
				lSogSqlDao.ricercaSoggettoByKey(lFasModel.getSogIdSoggetto());

				lSoggetto = new SoggettoModel((SoggettoModel) lSogSqlDao.getModelByKey());
				TreeModel lTreeSoggettoSiep = null;
				// if (lSoggetto == null) {
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// // LogF3B.getLogger()
				// siesLogger.debug("##### SOGGETTO NON TROVATO");
				// } else {
				lFasModel.setSoggetto(lSoggetto);
				lTreeSoggettoSiep = new TreeModel(lSoggetto);
				// }

				// Crea TreeModel Fascicolo Siep
				lTreeFasMod = new TreeModel(lFasModel);

				// Pena Residua
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Pena Residua");
				lPenaResDao = new PenaResiduaSqlDAO(aConn);
				// lPenaResDao.ricercaPenaResiduaForStampa(lKeyFascicolo);
				// ** Per la stampa viene considerata la Pena Residua corrente
				// ** associata al Fascicolo Siep
				lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lKeyFascicolo);

				PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenaResDao.getModelByKey();
				// Stringa Arresto - REclusione
				if (lPenResMod != null) {
					lPenResMod.calcolaStringaReclusione();
					lPenResMod.calcolaStringaArresto();
					// 03/06/2010 Aggiunta Pena Residua.
					TreeModel lTreePenRes = new TreeModel(lPenResMod);
					lTreeFasMod.add(lTreePenRes);
				}

				// 03/06/2010 Aggiunta Misura Alternativa.
				// Lettura della Misura Alternativa.
				TreeModel lMATree = lStampa.getMATree(lPenResMod, lKeyFascicolo, aConn);
				if (lMATree != null)
					lTreeFasMod.add(lMATree);

				lTreeFasMod.add(lTreeSoggettoSiep);

				// Aggiunta sentenza per RichiestaAtti.
				TreeModel lTreeSentenza = prelevaDatiSentenza(lFasModel.getIdFascicoloSiep(), aConn);
				lTreeFasMod.add(lTreeSentenza);

				// Avvocati
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("##### Avvocati");
				lAvvDao = new AvvocatoSiepxStampaSqlDAO(aConn);
				lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
				Vector lAvvocati = new Vector(lAvvDao.getModels());

				// Add Avvocati per Fascicolo
				if (lAvvocati != null) {
					Iterator lItx = lAvvocati.iterator();
					while (lItx.hasNext()) {
						AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
						TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
						lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
						lTreeFasMod.add(lTreeAvvMod);
					}
				}

				// Aggiunge nodi di evento e notifica
				Vector lEventoMod = new Vector();
				EventoSqlDAO lEveDao = null;
				lEveDao = new EventoSqlDAO(aConn);
				lEveDao.ricercaEventoByIdFascicoloSiep(lKeyFascicolo);

				lEventoMod = new Vector(lEveDao.getModels());
				if (lEventoMod.size() != 0) {
					Iterator lItx = lEventoMod.iterator();
					while (lItx.hasNext()) {
						lTreeFasMod.add(new TreeModel((EventoModel) lItx.next()));
					}
				}

				// Metodo per aggiungere tutte le entità del Fascicolo SIEP Luigi 28-08-2007
				appendTableToFascicoloSiep(aConn, lFasModel.getIdFascicoloSiep(), lTreeFasMod, null);
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaSigeController.prelevaDatiFascicoloSiep: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException(
						"StampaSigeController.prelevaDatiFascicoloSiep: Eccezione Generica: " + sqe);
			} finally {
				cleanup(lFasDao);
				cleanup(lSogSqlDao);
				cleanup(lReaDao);
				cleanup(lAltCauDao);
				cleanup(lAvvDao);
				cleanup(lBenDao);
				cleanup(lMisDao);
				cleanup(lPenDao);
				cleanup(lLuoDao);
				cleanup(lPosGiuDao);
				cleanup(lPenAccDao);
				cleanup(lMisSicDao);
				cleanup(lPenaResDao);
				cleanup(lEveSqlDao);
				cleanup(lProvSqlDao);
			}
		}
		return lTreeFasMod;
	}

	/**
	 * Esegue il prelievo dati della Sentenza.
	 *
	 * @param aIdFascicoloSIEP
	 *            id del FascicoloSIEP.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati della sentenza come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiSentenza(BigDecimal aIdFascicoloSIEP, Connection aConn) throws F3BException {

		TreeModel lTreeSenMod = new TreeModel();

		FascicoloSiepSqlDAO lFasDao = null;
		SentenzaSqlDAO lSenDao = null;

		if (aIdFascicoloSIEP != null) {
			try {
				// Fascicolo
				lFasDao = new FascicoloSiepSqlDAO(aConn);
				lFasDao.ricercaFascicoloByKey(aIdFascicoloSIEP);
				FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

				// Sentenza
				lSenDao = new SentenzaSqlDAO(aConn);
				lSenDao.ricercaSentenzaBykey(lFasModel.getSenIdSentenza());
				SentenzaModel lSenModel = (SentenzaModel) lSenDao.getModelByKey();

				lTreeSenMod = new TreeModel(lSenModel);
			} catch (DAOException daoEx) {
				daoEx.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("DAOException: " + daoEx);
				throw new F3BException("StampaSigeController.prelevaDatiSentenza: " + daoEx);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Exception: " + sqe);
				throw new F3BException(
						"StampaSigeController.prelevaDatiSentenza: Eccezione Generica: " + sqe);
			} finally {
				cleanup(lSenDao);
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lFasDao);
			}
		}
		return lTreeSenMod;
	}

	/**
	 * Genera il TreeModel per la stampa del Provvedimento Definitorio
	 *
	 * @param aIdFasSige
	 * @return dati del Provvedimento come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiProvvedimentoDefinitorio(BigDecimal aIdFasSige, Connection aConn)
			throws F3BException {

		TreeModel lTreeProvvedimento = null;
		TreeModel lTreeEvento = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		MotivazioneProvvedimentoSigeSqlDAO lMPSDao = null;
		UfficioSqlDAO lUDao = null;

		Vector lMotivazioni = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ProvvedimentoDefinitorio : inizio");
			// Lettura del Provvedimento completo di Evento e Notifiche.
			lProvSqlDao = new ProvvedimentoSigeSqlDAO(aConn);
			lProvSqlDao.ricercaProvvedimentoDefinitorioByIdFasSige(aIdFasSige);

			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				ProvvedimentoSigeModel lProSige = (ProvvedimentoSigeModel) lProvSqlDao.getModel();

				lProSige.decodifica();
				// 21/05/2010 Lettura Eventuale Ufficio Destinatario.
				if (lProSige.getCodUfficioDestinatario() != null) {
					lUDao = new UfficioSqlDAO(aConn);
					lUDao.getDescTipoUffByCodUfficio(lProSige.getCodUfficioDestinatario());
					lUDao.start();
					while (lUDao.next())
						lProSige.setDescrUfficioDestinatario(lUDao.getString("DESC_UFFICIO"));
					lUDao.stop();
				}
				lTreeProvvedimento = new TreeModel(lProSige);

				lEveSqlDao = new EventoSqlDAO(aConn);
				lEveSqlDao.ricercaEventoByKey(lProSige.getIdEventoGenerato());
				EventoModel lEvento = new EventoModel();
				lEvento = (EventoModel) lEveSqlDao.getModelByKey();
				lTreeEvento = prelevaDatiEvento(lEvento, aConn);
				lTreeProvvedimento.add(lTreeEvento);

				// Lettura composizione Collegio.
				if (lProSige.getColIdCollegio() != null)
					lTreeProvvedimento.add(prelevaDatiCollegioById(lProSige.getColIdCollegio(), aConn));

				// Lettura dei "Tenore Sige Esteso" via Controller.
				ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setFasIdFascicoloSige(aIdFasSige);
				Vector lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);

				/*
				 * 01/12/2009 correzione caricamento tenori. // Si itera sull'elenco dei Tenori per caricare
				 * il TreeModel con Tenore, Sentenza e Reato. Iterator itx = lTenoriEstesi.iterator();
				 * TreeModel lTreeTenore = null; while ( itx.hasNext()) { TenoreSigeEstesoModel lTenEsteso =
				 * (TenoreSigeEstesoModel)itx.next(); lTreeTenore = new TreeModel
				 * (lTenEsteso.getTenoreSige().decodifica() ); lTreeTenore.add(new
				 * TreeModel(lTenEsteso.getSentenza() )); lTreeTenore.add(new TreeModel(lTenEsteso.getReato()
				 * )); if (lTreeTenore != null ) lTreeProvvedimento.add(lTreeTenore ); }
				 */
				// Costruzione e caricamento della struttura dei Tenori nel Provvedimento.
				lTreeProvvedimento = buildTreeTenoriEstesiConDatiProvvedimento(lTreeProvvedimento,
						lTenoriEstesi, aConn);

				// 27-05-2009 Lettura Eventuali Motivi Provvedimento Sige.
				lMPSDao = new MotivazioneProvvedimentoSigeSqlDAO(aConn);
				lMPSDao.ricercaMotivazioneDecretoInammissibilitaByIdProvSige(
						lProSige.getIdProvvedimentoSige());
				lMotivazioni = new Vector(lMPSDao.getModels());
				if (lMotivazioni != null) {
					Iterator itx2 = lMotivazioni.iterator();
					while (itx2.hasNext()) {
						MotivazioneProvvedimentoSigeModel lMotProSige = (MotivazioneProvvedimentoSigeModel) itx2
								.next();
						lTreeProvvedimento.add(new TreeModel(lMotProSige));
					}
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ProvvedimentoDefinitorio : fine");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiProvvedimentoDefinitorio : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiProvvedimentoDefinitorio : " + sqlEx);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lMPSDao);
			cleanup(lUDao);
		}
		return lTreeProvvedimento;
	}

	/**
	 * Genera il TreeModel per la stampa del Provvedimento a partere dall' IdEvento.
	 *
	 * @param aIdEvento
	 * @return dati del Provvedimento come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiProvvedimentoByIdEvento(BigDecimal aIdEvento, BigDecimal aIdFasSige,
			Connection aConn) throws F3BException {

		TreeModel lTreeProvvedimento = null;
		TreeModel lTreeEvento = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		UfficioSqlDAO lUDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ProvvedimentoByIdEvento : inizio");

			// Lettura del Provvedimento completo di Evento e Notifiche.
			ProvvedimentoSigeModel aProvModel = new ProvvedimentoSigeModel();
			aProvModel.setIdEventoGenerato(aIdEvento);
			aProvModel.setFasIdFascicoloSige(aIdFasSige);
			lProvSqlDao = new ProvvedimentoSigeSqlDAO(aConn);
			lProvSqlDao.ricercaProvvedimentoPerEvento(aProvModel);

			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				ProvvedimentoSigeModel lProSige = (ProvvedimentoSigeModel) lProvSqlDao.getModel();

				lProSige.decodifica();
				// 21/05/2010 Lettura Eventuale Ufficio Destinatario.
				if (lProSige.getCodUfficioDestinatario() != null) {
					lUDao = new UfficioSqlDAO(aConn);
					lUDao.getDescTipoUffByCodUfficio(lProSige.getCodUfficioDestinatario());
					lUDao.start();
					while (lUDao.next())
						lProSige.setDescrUfficioDestinatario(lUDao.getString("DESC_UFFICIO"));
					lUDao.stop();
				}

				lTreeProvvedimento = new TreeModel(lProSige);

				lEveSqlDao = new EventoSqlDAO(aConn);
				lEveSqlDao.ricercaEventoByKey(aIdEvento);
				EventoModel lEvento = new EventoModel();
				lEvento = (EventoModel) lEveSqlDao.getModelByKey();
				lTreeEvento = prelevaDatiEvento(lEvento, aConn);
				lTreeProvvedimento.add(lTreeEvento);

				// Lettura composizione Collegio.
				if (lProSige.getColIdCollegio() != null)
					lTreeProvvedimento.add(prelevaDatiCollegioById(lProSige.getColIdCollegio(), aConn));

				// Lettura dei "Tenore Sige Esteso" via Controller Solo se non si tratta di una istruttoria.
				if (lProSige.getCodTipoProvvedimento().compareTo("52") != 0) {
					ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
					TenoreSigeModel aTenore = new TenoreSigeModel();
					aTenore.setFasIdFascicoloSige(aIdFasSige);
					Vector lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);

					/*
					 * 01/12/2009 correzione caricamento tenori. // Si itera sull'elenco dei Tenori per
					 * caricare il TreeModel con Tenore, Sentenza e Reato. Iterator itx =
					 * lTenoriEstesi.iterator(); TreeModel lTreeTenore = null; while ( itx.hasNext()) {
					 * TenoreSigeEstesoModel lTenEsteso = (TenoreSigeEstesoModel)itx.next(); lTreeTenore = new
					 * TreeModel (lTenEsteso.getTenoreSige().decodifica() ); lTreeTenore.add(new
					 * TreeModel(lTenEsteso.getSentenza() )); lTreeTenore.add(new
					 * TreeModel(lTenEsteso.getReato() )); if (lTreeTenore != null )
					 * lTreeProvvedimento.add(lTreeTenore ); }
					 */
					// Costruzione e caricamento della struttura dei Tenori nel Provvedimento.
					lTreeProvvedimento = buildTreeTenoriEstesi(lTreeProvvedimento, lTenoriEstesi);

				}
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Istruttoria non trovata");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ProvvedimentoByIdEvento : fine");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiProvvedimentoByIdEvento : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiProvvedimentoByIdEvento : " + sqlEx);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lUDao);
		}
		return lTreeProvvedimento;
	}

	/**
	 * Genera il TreeModel per la stampa del Provvedimento a partere dall' IdImpugnazione.
	 *
	 * @param aIdImpugnazione
	 * @return dati del Provvedimento come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiProvvedimentoByIdImpugnazione(BigDecimal aIdImpugnazione,
			BigDecimal aIdFasSige, Connection aConn) throws F3BException {

		TreeModel lTreeProvvedimento = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		ImpugnazioneSigeSqlDAO lImpSqlDao = null;
		UfficioSqlDAO lUDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ProvvedimentoByIdImpugnazione : inizio");

			// Lettura del Provvedimento
			lProvSqlDao = new ProvvedimentoSigeSqlDAO(aConn);
			lImpSqlDao = new ImpugnazioneSigeSqlDAO(aConn);
			BigDecimal lIdProvvSige = lImpSqlDao.getIdProvvedimentoSige(aIdImpugnazione);
			lProvSqlDao.ricercaProvvedimentoSigeByKey(lIdProvvSige);

			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				ProvvedimentoSigeModel lProSige = (ProvvedimentoSigeModel) lProvSqlDao.getModel();

				lProSige.decodifica();
				// 21/05/2010 Lettura Eventuale Ufficio Destinatario.
				if (lProSige.getCodUfficioDestinatario() != null) {
					lUDao = new UfficioSqlDAO(aConn);
					lUDao.getDescTipoUffByCodUfficio(lProSige.getCodUfficioDestinatario());
					lUDao.start();
					while (lUDao.next())
						lProSige.setDescrUfficioDestinatario(lUDao.getString("DESC_UFFICIO"));
					lUDao.stop();
				}

				lTreeProvvedimento = new TreeModel(lProSige);

				// Lettura composizione Collegio.
				if (lProSige.getColIdCollegio() != null)
					lTreeProvvedimento.add(prelevaDatiCollegioById(lProSige.getColIdCollegio(), aConn));

				// Lettura dei "Tenore Sige Esteso" via Controller Solo se non si tratta di una istruttoria.
				if (lProSige.getCodTipoProvvedimento().compareTo("52") != 0) {
					ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
					TenoreSigeModel aTenore = new TenoreSigeModel();
					aTenore.setFasIdFascicoloSige(aIdFasSige);
					Vector lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);

					/*
					 * 01/12/2009 correzione caricamento tenori. // Si itera sull'elenco dei Tenori per
					 * caricare il TreeModel con Tenore, Sentenza e Reato. Iterator itx =
					 * lTenoriEstesi.iterator(); TreeModel lTreeTenore = null; while ( itx.hasNext()) {
					 * TenoreSigeEstesoModel lTenEsteso = (TenoreSigeEstesoModel)itx.next(); lTreeTenore = new
					 * TreeModel (lTenEsteso.getTenoreSige().decodifica() ); lTreeTenore.add(new
					 * TreeModel(lTenEsteso.getSentenza() )); lTreeTenore.add(new
					 * TreeModel(lTenEsteso.getReato() )); if (lTreeTenore != null )
					 * lTreeProvvedimento.add(lTreeTenore ); }
					 */
					// Costruzione e caricamento della struttura dei Tenori nel Provvedimento.
					lTreeProvvedimento = buildTreeTenoriEstesi(lTreeProvvedimento, lTenoriEstesi);

				}
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Istruttoria non trovata");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ProvvedimentoByIdImpugnazione : fine");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException(
					"StampaSigeController.prelevaDatiProvvedimentoByIdImpugnazione : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException(
					"StampaSigeController.prelevaDatiProvvedimentoByIdImpugnazione : " + sqlEx);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lImpSqlDao);
			cleanup(lUDao);
		}
		return lTreeProvvedimento;
	}

	/**
	 * Genera il TreeModel per la stampa dell' Impugnazione a partire dall' IdImpugnazione.
	 *
	 * @param aIdImpugnazione
	 * @return dati dell' Impugnazione come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiImpugnazioneByKey(BigDecimal aIdImpugnazione, BigDecimal aIdFasSige,
			Connection aConn) throws F3BException {

		TreeModel lTreeImpugnazione = null;

		ImpugnazioneSigeSqlDAO lImpSqlDao = null;
		UfficioSqlDAO lUDao = null;
		NotificaSqlDAO notificaSqlDao = null;
		AutoritaEsternaSqlDAO autoritaSqlDao = null;
		UfficioSqlDAO ufficioSqlDao = null;
		// MAC 2017/04/21
		AvvocatoSqlDAO avvocatoSqlDao = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ImpugnazioneByKey : inizio");

			// Lettura dell' Impugnazione
			// ImpugnazioneSigeModel aImpModel = new ImpugnazioneSigeModel();
			lImpSqlDao = new ImpugnazioneSigeSqlDAO(aConn);
			lImpSqlDao.ricercaImpugnazioneByKey(aIdImpugnazione);

			lImpSqlDao.start();

			if (lImpSqlDao.next()) {
				ImpugnazioneSigeModel lImpSige = (ImpugnazioneSigeModel) lImpSqlDao.getModel();

				// lImpSige.decodifica(); // ? necessaria ?
				// 21/05/2010 Lettura Eventuale Ufficio Destinatario. (o autorità ?)
				if (lImpSige.getCodAutoritaDestinataria() != null) {
					lUDao = new UfficioSqlDAO(aConn);
					lUDao.getDescTipoUffByCodUfficio(lImpSige.getCodAutoritaDestinataria());
					lUDao.start();
					while (lUDao.next())
						lImpSige.setDescrAutoritaDestinataria(lUDao.getString("DESC_UFFICIO"));
					lUDao.stop();
				}

				IProvvedimentoSige iProvvSigeCtrl = SIGELookupRemote.getProvvedimentoRemote();

				lTreeImpugnazione = new TreeModel(lImpSige);

				if (lImpSige != null) {
					ProvvedimentoSigeEventoModel prvovvEvento = iProvvSigeCtrl
							.ExRicercaProvvedimentoById(lImpSige.getProvvIdProvvedimentoSige());
					lImpSige.setProvvedimentoSige(prvovvEvento);
				}

				if (lImpSige != null && lImpSige.getIdProvvedimentoGenerato() != null) {
					ProvvedimentoSigeEventoModel prvovvEventoGenerato = iProvvSigeCtrl
							.ExRicercaProvvedimentoById(lImpSige.getIdProvvedimentoGenerato());
					lImpSige.setProvvedimentoSigeGenerato(prvovvEventoGenerato);
					notificaSqlDao = new NotificaSqlDAO(aConn);
					notificaSqlDao.ricercaNotificaByEvento(lImpSige.getProvvedimentoSigeGenerato()
							.getEventoNotifica().getEvento().getIdEvento());
					Vector<NotificaModel> notifiche = new Vector<NotificaModel>(notificaSqlDao.getModels());
					autoritaSqlDao = new AutoritaEsternaSqlDAO(aConn);
					for (NotificaModel notifica : notifiche) {
						TreeModel lTreeAutEst = null;
						TreeModel lTreeUfficio = null;
						TreeModel lTreeAvvocato = null;
						if (notifica.getAutEstIdAutoritaEsterna() != null) {
							autoritaSqlDao.ricercaAutoritaEsternaByKey(notifica.getAutEstIdAutoritaEsterna());
							autoritaSqlDao.start();

							if (autoritaSqlDao.next())
								// MAC 2017/04/21
								// notifica.setAutoritaEsterna((AutoritaEsternaModel)autoritaSqlDao.getModel());
								lTreeAutEst = new TreeModel(autoritaSqlDao.getModel());

							autoritaSqlDao.stop();
						}

						if (notifica.getUffCodUfficio() != null) {
							ufficioSqlDao = new UfficioSqlDAO(aConn);
							ufficioSqlDao.ricercaUfficioByCod(notifica.getUffCodUfficio());
							ufficioSqlDao.start();

							if (ufficioSqlDao.next())
								// MAC 2017/04/21
								// notifica.setUfficio((UfficioModel)ufficioSqlDao.getModel());
								lTreeUfficio = new TreeModel(ufficioSqlDao.getModel());

							ufficioSqlDao.stop();
						}

						// MAC 2017/04/21 Inizio
						// Se è valorizzato il campo AVV_ID_AVVOCATO_FASCICOLO_SIGE e il campo
						// AUT_EST_ID_AUTORITA_ESTERNA
						// è null, nel form di inserimento delle notifiche è stato selezionato SNT (Sistema
						// Notifica Telematico)
						if (notifica.getAvvIdAvvocatoFascicoloSige() != null
								&& notifica.getAutEstIdAutoritaEsterna() == null) {
							avvocatoSqlDao = new AvvocatoSqlDAO(aConn);
							avvocatoSqlDao.ricercaAvvocatobyKey(notifica.getAvvIdAvvocatoFascicoloSige());
							avvocatoSqlDao.start();

							if (avvocatoSqlDao.next())
								// notifica.setAutoritaEsterna((AutoritaEsternaModel)autoritaSqlDao.getModel());
								lTreeAvvocato = new TreeModel(avvocatoSqlDao.getModel());
						}

						TreeModel lTreeNotifica = new TreeModel(notifica);

						lTreeNotifica.add(lTreeAutEst);
						lTreeNotifica.add(lTreeUfficio);
						lTreeNotifica.add(lTreeAvvocato);
						// MAC 2017/04/21 Fine
						lTreeImpugnazione.add(lTreeNotifica);
					}
				}
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impugnazione non trovata");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati ImpugnazioneByKey : fine");
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiImpugnazioneByKey : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiImpugnazioneByKey : " + sqlEx);
		} finally {
			cleanup(lImpSqlDao);
			cleanup(ufficioSqlDao);
			cleanup(notificaSqlDao);
			cleanup(autoritaSqlDao);
			cleanup(lUDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(avvocatoSqlDao);

		}
		return lTreeImpugnazione;
	}

	/**
	 *
	 * @param aKey
	 * @param aConn
	 * @return
	 * @throws F3BException
	 */
	private UdienzaSigeModel prelevaDatiUdienza(BigDecimal aKey, Connection aConn) throws F3BException {

		UdienzaSigeModel lUdienzaSigeMod = new UdienzaSigeModel();
		UdienzaSigeSqlDAO lUdienzaSigeSqlDao = null;

		try {

			lUdienzaSigeSqlDao = new UdienzaSigeSqlDAO(aConn);
			lUdienzaSigeSqlDao.ricercaUdienzaSigeByKey(aKey);
			lUdienzaSigeMod = (UdienzaSigeModel) lUdienzaSigeSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiUdienza : " + daoEx);
		} catch (Exception ex) {
			siesLogger.debug("Exception: " + ex);
			throw new SIGEException("StampaSigeController.prelevaDatiUdienza : " + ex);
		} finally {
			cleanup(lUdienzaSigeSqlDao);
		}
		return lUdienzaSigeMod;

	}

	private TreeModel prelevaDatiCollegioById(BigDecimal aKey, Connection aConn) throws F3BException {

		CollegioSqlDAO lColDao = null;
		CollegioMagistratoSqlDAO lColMagDao = null;
		CollegioEspertoSqlDAO lColEspDao = null;

		CollegioModel lColMod;

		TreeModel lTreeCollegio = null;

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati di composizione Collegio : inizio");

			lColDao = new CollegioSqlDAO(aConn);
			lColDao.ricercaCollegioByKey(aKey);
			lColMod = (CollegioModel) lColDao.getModelByKey();

			lTreeCollegio = new TreeModel(lColMod);

			// Recupero dei dati afferenti al magistrato.
			lColMagDao = new CollegioMagistratoSqlDAO(aConn);
			lColMagDao.ricercaMagistratoByIdCollegioCodUff(aKey, lColMod.getCodUfficioAppartenenza());
			Collection lColl = new ArrayList();
			lColl = lColMagDao.getModels();

			Iterator itx = lColl.iterator();
			while (itx.hasNext()) {
				CollegioMagistratoModel lColMag = (CollegioMagistratoModel) itx.next();
				TreeModel lTreeColMag = new TreeModel(lColMag);
				// Necessario eseguire una nuova istanza di : siap.sico.magistrato.model.MagistratoModel,
				// per copia dati della istanza : siap.sige.magistrato.model.MagistratoModel
				// poiché il parser non è in grado di gestire model ereditati.
				// Infatti, lColMag.getMagistrato() ritorna istanza :
				// siap.sige.magistrato.model.MagistratoModel
				// che eredita : siap.sico.magistrato.model.MagistratoModel
				lTreeColMag.add(new TreeModel(new MagistratoModel(lColMag.getMagistrato())));
				lTreeCollegio.add(lTreeColMag);
			}

			// Recupero dei dati afferenti al Giudice Popolare.
			// lColGiuPopDao = new CollegioGiudicePopolareSqlDAO(lConn);
			// lColGiuPopDao.ricercaGiudicePopolareByIdCollegio(aKey);
			// lColl = new ArrayList(); lColl = lColGiuPopDao.getModels();

			// Recupero dei dati afferenti al Giudice Popolare.
			lColEspDao = new CollegioEspertoSqlDAO(aConn);
			lColEspDao.ricercaEspertoByIdCollegio(aKey);
			lColl = new ArrayList();
			lColl = lColEspDao.getModels();

			itx = lColl.iterator();
			while (itx.hasNext()) {
				CollegioEspertoModel lColEsp = (CollegioEspertoModel) itx.next();
				lTreeCollegio.add(new TreeModel(lColEsp.getEsperto()));
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Preleva dati di composizione Collegio : fine");
		} catch (DAOException daoex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoex);
			throw new SIGEException("StampaSigeController.prelevaDatiCollegioById : " + daoex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + ex);
			throw new SIGEException("StampaSigeController.prelevaDatiCollegioById : " + ex);
		} finally {
			cleanup(lColDao);
			cleanup(lColMagDao);
			cleanup(lColEspDao);
		}

		return lTreeCollegio;
	}

	private TreeModel prelevaDatiEvento(EventoModel aEvento, Connection aConn) throws F3BException {

		TreeModel lTreeEvento = null;
		NotificaSqlDAO lNotDao = null;

		try {
			lTreeEvento = new TreeModel(aEvento);

			Vector lNotifiche = null;

			// Caricamento notifiche.
			lNotDao = new NotificaSqlDAO(aConn);
			lNotDao.ricercaNotificaByEvento(aEvento.getIdEvento());
			lNotifiche = new Vector(lNotDao.getModels());

			// Inserimento di eventuali destinatari: Autorita Esterna,Ufficio, Avvocato Siep, Avvocato Sige,
			// CSSA.
			lNotifiche = getDestinatari(lNotifiche, aConn);

			lTreeEvento = prelevaDatiNotificheDestinatari(lNotifiche, lTreeEvento, aConn);
			lTreeEvento = prelevaDatiCampoNota(lTreeEvento, aEvento.getIdEvento(), aConn);
			// lTreeEvento = prelevaDatiMotivazioniDecreto(lTreeEvento,aEvento.getIdEvento(), aConn);

			// 28/05/2010 Agginte le Annotazioni Manuali.
			lTreeEvento = prelevaDatiAnnotazioneManuale(lTreeEvento, aEvento, aConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiEvento : " + daoEx);
		} catch (SQLException sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiEvento : " + sqlEx);
		} catch (Exception Ex) {
			throw new SIGEException("StampaSigeController.prelevaDatiEvento : " + Ex);
		} finally {
			cleanup(lNotDao);
		}

		return lTreeEvento;
	}

	/**
	 * Esegue il prelievo dati del Fascicolo Sige Esteso.
	 *
	 * @param aIdFascicoloSIGE
	 *            .
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati del Fascicolo Sige Esteso.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private FascicoloSigeEstesoModel prelevaDatiFasSigeEsteso(BigDecimal aIdFascicoloSIGE, Connection aConn)
			throws F3BException {

		FascicoloSigeSqlDAO lFasSigeDao = null;
		RichiestaSigeDAO lRicSigeDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiSigeSqlDao = null;

		FascicoloSigeModel lFasSigeMod;
		RichiestaSigeModel lRicSigeMod;
		UdienzaProcedimentoSigeModel lUdiSigeMod;
		FascicoloSigeEstesoModel lFascicoloEsteso = new FascicoloSigeEstesoModel();

		try {
			// Ricerca del Fascicolo SIGE
			lFasSigeDao = new FascicoloSigeSqlDAO(aConn);
			lFasSigeDao.ricercaFascicoloSigeByKey(aIdFascicoloSIGE);
			lFasSigeMod = (FascicoloSigeModel) lFasSigeDao.getModelByKey();

			if (lFasSigeMod == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non trovato il Fascicolo con ID -> " + aIdFascicoloSIGE);
			if (lFasSigeMod.getSogIdSoggetto() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non esiste il Soggetto per il Fascicolo con ID -> " + aIdFascicoloSIGE);
			if (lFasSigeMod.getRicIdRichiestaSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non esiste la Richiesta per il Fascicolo con ID -> " + aIdFascicoloSIGE);

			lFascicoloEsteso.setFascicoloSige(lFasSigeMod);

			// Ricerca della Richiesta
			lRicSigeDao = new RichiestaSigeDAO(aConn);
			lRicSigeDao.setIdRichiestaSige(lFasSigeMod.getRicIdRichiestaSige());
			lRicSigeDao.selByKey();
			lRicSigeMod = (RichiestaSigeModel) lRicSigeDao.getModelByKey();

			if (lRicSigeMod == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non trovata la Richiesta SIGE con ID -> " + lFasSigeMod.getRicIdRichiestaSige());

			lFascicoloEsteso.setRichiestaSige(lRicSigeMod);

			// Ricerca dell' UdienzaProcedimento
			lUdiSigeSqlDao = new UdienzaProcedimentoSigeSqlDAO(aConn);
			lUdiSigeSqlDao.ricercaUdienzaProcedimentoUdienzaByFascicoloByFlagRinviata(
					lFasSigeMod.getIdFascicoloSige(), "'F','S'");
			lUdiSigeSqlDao.start();
			if (lUdiSigeSqlDao.next()) {
				lUdiSigeMod = (UdienzaProcedimentoSigeModel) lUdiSigeSqlDao.getModelConDataUdienza();
				lFascicoloEsteso.setUdienzaProcedimento(lUdiSigeMod);
			}
			lUdiSigeSqlDao.stop();
		} catch (F3BException fE) {
			throw fE;
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaSigeController.prelevaDatiFasSigeEsteso: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + e);
			throw new F3BException("StampaSigeController.prelevaDatiFasSigeEsteso: " + e);
		} finally {
			cleanup(lFasSigeDao);
			cleanup(lRicSigeDao);
			cleanup(lUdiSigeSqlDao);
		}
		return lFascicoloEsteso;
	}

	private TreeModel prelevaDati(BigDecimal aIdEvento, BigDecimal aIdFasSige, int[] aTipoDati,
			TreeModel lTreeDati, int aTipoStampa, String codUffUtenteConnesso, Connection lConn)
			throws F3BException {

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeFasSIGE = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeMagAssegnatario = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeProvvedimento = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeRicSIGE = null; // 02/12/2009
		TreeModel lTreeImpugnazione = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("PrelevaDati : inizio");

		try {
			FascicoloSigeEstesoModel lFascicoloSigeEsteso = prelevaDatiFasSigeEsteso(aIdFasSige, lConn);

			for (int i = 0; i < aTipoDati.length; i++) {
				// creazione delle varie foglie componenti del documento TreeModel
				switch (aTipoDati[i]) {
				case ICostantiStampaSige.TREE_SOGGETTO:
					lTreeSoggetto = prelevaDatiSoggetto(
							lFascicoloSigeEsteso.getFascicoloSige().getSogIdSoggetto(),
							lFascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige(), lConn);
					lTreeDati.add(lTreeSoggetto);
					break;

				case ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO:
					FascicoloSigeModel aFasMod = lFascicoloSigeEsteso.getFascicoloSige();
					aFasMod.decodifica();
					lTreeFasSIGE = new TreeModel(aFasMod);
					RichiestaSigeModel aRicMod = lFascicoloSigeEsteso.getRichiestaSige();
					aRicMod.decodifica();
					// 02/12/2009 cambio struttura tree di Richiesta.
					// lTreeFasSIGE.add(new TreeModel(aRicMod) );
					lTreeRicSIGE = new TreeModel(aRicMod);

					/*
					 * 02/12/2009 correzione caricamento struttura tenori. Vector lTenori = getTenoriSige(
					 * lFascicoloSigeEsteso.getRichiestaSige().getIdRichiestaSige() , lConn); if
					 * (lTenori.size() != 0) { Iterator lItxTen = lTenori.iterator(); while( lItxTen.hasNext()
					 * ) lTreeFasSIGE.add(new TreeModel( (TenoreSigeModel)lItxTen.next())); }
					 */

					// 02/12/2009 Nuovo caricamento dei "Tenore Sige Esteso" via Controller.
					ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
					Vector<TenoreSigeEstesoModel> lTenoriEstesiDiRichiesta = lCtrl
							.ExRicercaTenoreEstesoByRichiesta(
									lFascicoloSigeEsteso.getRichiestaSige().getIdRichiestaSige(), lConn);
					// 20170912: [SG] commentato "cleanTenori" poiche' non stampa tutte le sentenze legate
					// all'oggetto in essere (pero' ci sono anche altre 3 occorrenze di questo tipo) in:
					// prelevaDati + prelevaDatiFissazioneUdienza + prelevaDatiOrdinanzaRinvioUdienza
					// lTenoriEstesiDiRichiesta = cleanTenori(lTenoriEstesiDiRichiesta);

					if (lTenoriEstesiDiRichiesta.size() != 0)
						lTreeRicSIGE = buildTreeTenoriEstesi(lTreeRicSIGE, lTenoriEstesiDiRichiesta);

					lTreeFasSIGE.add(lTreeRicSIGE);

					lTreeMagAssegnatario = prelevaDatiMagistratoAssegnatario(aIdFasSige, codUffUtenteConnesso,
							lConn);
					lTreeFasSIGE.add(lTreeMagAssegnatario);

					lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSige(aIdFasSige, lConn);
					lTreeFasSIGE.add(lTreeLuogoDetenzione);

					StampaSigeUtils lStaSigeUtils = new StampaSigeUtils();
					lTreeFasSIGE = lStaSigeUtils.prelevaDatiAvvocatiSige(lTreeFasSIGE, aIdFasSige, lConn);

					// Sentenze SIGE
					lTreeFasSIGE = prelevaDatiSentenzeSige(lTreeFasSIGE, aIdFasSige, lConn);

					// 20170920: [SG] gestione fissazione udienza senza date, tolto secondo controllo
					// && lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null
					if (lFascicoloSigeEsteso.getUdienzaProcedimento() != null) {
						if (lFascicoloSigeEsteso.getUdienzaProcedimento().getFlagRinviata()
								.equalsIgnoreCase("F")) {
							lTreeUdienza = prelevaDatiFissazioneUdienza(aIdFasSige,
									lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige(),
									lConn, aIdEvento);
						} else if (lFascicoloSigeEsteso.getUdienzaProcedimento().getFlagRinviata()
								.equalsIgnoreCase("S")) {
							lTreeUdienza = prelevaDatiOrdinanzaRinvioUdienza(aIdFasSige,
									lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige(),
									lConn);
						}

						if (lTreeUdienza != null)
							lTreeFasSIGE.add(lTreeUdienza);
						else if (aTipoStampa == ICostantiStampaSige.STAMPA_UDIENZA)
							throw new SIGEException(SIGEException.USER_MESSAGE,
									"Non trovata l'Udienza SIGE con ID -> " + lFascicoloSigeEsteso
											.getUdienzaProcedimento().getUdiIdUdienzaSige());
					}

					// Si distingue il caso del prelevamento dati pilotato dall'Evento (Istruttorie, ...)
					// da quello dei provvedimenti definitori (Unico provvedimento per l'IdFascicoloSige).
					if (aTipoStampa != ICostantiStampaSige.STAMPA_IMPUGNAZIONE) {
						if (aIdEvento == null)
							lTreeProvvedimento = prelevaDatiProvvedimentoDefinitorio(aIdFasSige, lConn);
						else
							lTreeProvvedimento = prelevaDatiProvvedimentoByIdEvento(aIdEvento, aIdFasSige,
									lConn);
					} else {
						// Nel caso di stampa Impugnazione l' ID è quello dell'impugnazione e non dell'evento
						lTreeImpugnazione = prelevaDatiImpugnazioneByKey(aIdEvento, aIdFasSige, lConn);
						lTreeProvvedimento = prelevaDatiProvvedimentoByIdImpugnazione(aIdEvento, aIdFasSige,
								lConn);
						// 20170921: [SG] anticipato questo controllo
						if ((lTreeImpugnazione != null) && (lTreeProvvedimento != null))
							lTreeProvvedimento.add(lTreeImpugnazione);
					}

					if (lTreeProvvedimento != null)
						lTreeFasSIGE.add(lTreeProvvedimento);

					if ((lTreeImpugnazione != null) && (lTreeProvvedimento != null))
						// lTreeFasSIGE.add (lTreeImpugnazione);
						lTreeProvvedimento.add(lTreeImpugnazione);

					// 04-06-2009 Notizia Reato.
					Vector<NotiziaReatoModel> lNotizieReato = getNotiziaReato(aIdFasSige, lConn);
					if (lNotizieReato.size() != 0) {
						Iterator<NotiziaReatoModel> lItxNR = lNotizieReato.iterator();
						while (lItxNR.hasNext())
							lTreeFasSIGE.add(new TreeModel(lItxNR.next()));
					}

					lTreeDati.add(lTreeFasSIGE);
					break;

				/*
				 * case ICostantiStampaSige.TREE_UDIENZA: lTreeUdienza =
				 * prelevaDatiFissazioneUdienza(aIdFasSige,
				 * lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige(), lConn); if
				 * (lTreeUdienza != null) lTreeDati.add(lTreeUdienza); else if (aTipoStampa ==
				 * ICostantiStampaSige.STAMPA_UDIENZA) throw new SIGEException(SIGEException.USER_MESSAGE,
				 * "Non trovata l'Udienza SIGE con ID -> " +
				 * lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige()); break;
				 */
				case ICostantiStampaSige.TREE_FASCICOLOSIEP:
					lTreeFasSIEP = prelevaDatiFascicoloSiep(
							lFascicoloSigeEsteso.getRichiestaSige().getFasSieIdFascicoloSiep(), aIdFasSige,
							lConn);
					lTreeDati.add(lTreeFasSIEP);
					break;
				}
				aTipoDati[i] = 0;
			}
		} catch (SIGEException se) {
			throw se;
		} catch (F3BException Fe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("StampaSigeController.prelevaDati: " + Fe);
			throw Fe;
		} catch (Exception e) {
			throw new F3BException("StampaSigeController.prelevaDati: " + e);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("PrelevaDati : fine");
		return lTreeDati;
	}

	private TreeModel prelevaDati(BigDecimal aIdEvento, BigDecimal aIdFasSige,
			BigDecimal aIdFasSigeUnificante, int[] aTipoDati, TreeModel lTreeDati, int aTipoStampa,
			String codUffUtenteConnesso, Connection lConn) throws F3BException {

		// Oggetti TreeModel componenti del documento di stampa
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeFasSIGE = null;
		TreeModel lTreeFasSIGEUnificante = null;
		TreeModel lTreeFasSIEP = null;
		TreeModel lTreeMagAssegnatario = null;
		TreeModel lTreeLuogoDetenzione = null;
		TreeModel lTreeProvvedimento = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeRicSIGE = null; // 02/12/2009
		TreeModel lTreeImpugnazione = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("PrelevaDati : inizio");

		try {
			FascicoloSigeEstesoModel lFascicoloSigeEsteso = prelevaDatiFasSigeEsteso(aIdFasSige, lConn);
			FascicoloSigeEstesoModel lFascicoloSigeEstesoUnificante = prelevaDatiFasSigeEsteso(
					aIdFasSigeUnificante, lConn);

			for (int i = 0; i < aTipoDati.length; i++) {
				// creazione delle varie foglie componenti del documento TreeModel
				switch (aTipoDati[i]) {
				case ICostantiStampaSige.TREE_SOGGETTO:
					lTreeSoggetto = prelevaDatiSoggetto(
							lFascicoloSigeEstesoUnificante.getFascicoloSige().getSogIdSoggetto(),
							lFascicoloSigeEstesoUnificante.getFascicoloSige().getIdFascicoloSige(), lConn);
					lTreeDati.add(lTreeSoggetto);
					break;

				case ICostantiStampaSige.TREE_FASCICOLOSIGEESTESO:
					FascicoloSigeModel aFasMod = lFascicoloSigeEsteso.getFascicoloSige();
					FascicoloSigeModel aFasUnificanteMod = lFascicoloSigeEstesoUnificante.getFascicoloSige();
					aFasMod.decodifica();
					lTreeFasSIGE = new TreeModel(aFasMod);
					aFasUnificanteMod.decodifica();
					lTreeFasSIGEUnificante = new TreeModel(aFasUnificanteMod);

					RichiestaSigeModel aRicMod = lFascicoloSigeEsteso.getRichiestaSige();
					aRicMod.decodifica();
					// 02/12/2009 cambio struttura tree di Richiesta.
					// lTreeFasSIGE.add(new TreeModel(aRicMod) );
					lTreeRicSIGE = new TreeModel(aRicMod);

					/*
					 * 02/12/2009 correzione caricamento struttura tenori. Vector lTenori = getTenoriSige(
					 * lFascicoloSigeEsteso.getRichiestaSige().getIdRichiestaSige() , lConn); if
					 * (lTenori.size() != 0) { Iterator lItxTen = lTenori.iterator(); while( lItxTen.hasNext()
					 * ) lTreeFasSIGE.add(new TreeModel( (TenoreSigeModel)lItxTen.next())); }
					 */

					// 02/12/2009 Nuovo caricamento dei "Tenore Sige Esteso" via Controller.
					ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
					Vector<TenoreSigeEstesoModel> lTenoriEstesiDiRichiesta = lCtrl
							.ExRicercaTenoreEstesoByRichiesta(
									lFascicoloSigeEsteso.getRichiestaSige().getIdRichiestaSige(), lConn);
					lTenoriEstesiDiRichiesta = cleanTenori(lTenoriEstesiDiRichiesta);
					if (lTenoriEstesiDiRichiesta.size() != 0)
						lTreeRicSIGE = buildTreeTenoriEstesi(lTreeRicSIGE, lTenoriEstesiDiRichiesta);
					lTreeFasSIGE.add(lTreeRicSIGE);

					lTreeMagAssegnatario = prelevaDatiMagistratoAssegnatario(aIdFasSige, codUffUtenteConnesso,
							lConn);
					lTreeFasSIGE.add(lTreeMagAssegnatario);

					lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSige(aIdFasSige, lConn);
					lTreeFasSIGE.add(lTreeLuogoDetenzione);

					StampaSigeUtils lStaSigeUtils = new StampaSigeUtils();
					lTreeFasSIGE = lStaSigeUtils.prelevaDatiAvvocatiSige(lTreeFasSIGE, aIdFasSige, lConn);

					// Sentenze SIGE
					lTreeFasSIGE = prelevaDatiSentenzeSige(lTreeFasSIGE, aIdFasSige, lConn);

					if (lFascicoloSigeEsteso.getUdienzaProcedimento() != null
							&& lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {

						if (lFascicoloSigeEsteso.getUdienzaProcedimento().getFlagRinviata()
								.equalsIgnoreCase("F")) {
							lTreeUdienza = prelevaDatiFissazioneUdienza(aIdFasSige,
									lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige(),
									lConn, aIdEvento);
						} else if (lFascicoloSigeEsteso.getUdienzaProcedimento().getFlagRinviata()
								.equalsIgnoreCase("S")) {
							lTreeUdienza = prelevaDatiOrdinanzaRinvioUdienza(aIdFasSige,
									lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige(),
									lConn);
						}

						if (lTreeUdienza != null)
							lTreeFasSIGE.add(lTreeUdienza);
						else if (aTipoStampa == ICostantiStampaSige.STAMPA_UDIENZA)
							throw new SIGEException(SIGEException.USER_MESSAGE,
									"Non trovata l'Udienza SIGE con ID -> " + lFascicoloSigeEsteso
											.getUdienzaProcedimento().getUdiIdUdienzaSige());
					}

					// Si distingue il caso del prelevamento dati pilotato dall'Evento (Istruttorie, ...)
					// da quello dei provvedimenti definitori (Unico provvedimento per l'IdFascicoloSige).
					if (aTipoStampa != ICostantiStampaSige.STAMPA_IMPUGNAZIONE) {
						if (aIdEvento == null)
							lTreeProvvedimento = prelevaDatiProvvedimentoDefinitorio(aIdFasSige, lConn);
						else
							lTreeProvvedimento = prelevaDatiProvvedimentoByIdEvento(aIdEvento, aIdFasSige,
									lConn);
					} else {
						// Nel caso di stampa Impugnazione l' ID è quello dell'impugnazione e non dell'evento
						lTreeImpugnazione = prelevaDatiImpugnazioneByKey(aIdEvento, aIdFasSige, lConn);
						lTreeProvvedimento = prelevaDatiProvvedimentoByIdImpugnazione(aIdEvento, aIdFasSige,
								lConn);
					}

					if (lTreeProvvedimento != null)
						lTreeFasSIGE.add(lTreeProvvedimento);

					if ((lTreeImpugnazione != null) && (lTreeProvvedimento != null))
						// lTreeFasSIGE.add (lTreeImpugnazione);
						lTreeProvvedimento.add(lTreeImpugnazione);

					// 04-06-2009 Notizia Reato.
					Vector lNotizieReato = getNotiziaReato(aIdFasSige, lConn);
					if (lNotizieReato.size() != 0) {
						Iterator lItxNR = lNotizieReato.iterator();
						while (lItxNR.hasNext())
							lTreeFasSIGE.add(new TreeModel((NotiziaReatoModel) lItxNR.next()));
					}

					lTreeDati.add(lTreeFasSIGE);
					lTreeDati.add(lTreeFasSIGEUnificante);
					break;

				/*
				 * case ICostantiStampaSige.TREE_UDIENZA: lTreeUdienza =
				 * prelevaDatiFissazioneUdienza(aIdFasSige,
				 * lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige(), lConn); if
				 * (lTreeUdienza != null) lTreeDati.add(lTreeUdienza); else if (aTipoStampa ==
				 * ICostantiStampaSige.STAMPA_UDIENZA) throw new SIGEException(SIGEException.USER_MESSAGE,
				 * "Non trovata l'Udienza SIGE con ID -> " +
				 * lFascicoloSigeEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige()); break;
				 */
				case ICostantiStampaSige.TREE_FASCICOLOSIEP:
					lTreeFasSIEP = prelevaDatiFascicoloSiep(
							lFascicoloSigeEsteso.getRichiestaSige().getFasSieIdFascicoloSiep(), aIdFasSige,
							lConn);
					lTreeDati.add(lTreeFasSIEP);
					break;

				}
				aTipoDati[i] = 0;
			}
		} catch (SIGEException se) {
			throw se;
		} catch (F3BException Fe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("StampaSigeController.prelevaDati: " + Fe);
			throw Fe;
		} catch (Exception e) {
			throw new F3BException("StampaSigeController.prelevaDati: " + e);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("PrelevaDati : fine");
		return lTreeDati;
	}

	private Vector<NotificaModel> getDestinatari(Vector<NotificaModel> aNotifiche, Connection aConn)
			throws F3BException {

		Vector<NotificaModel> retNotifiche = new Vector<>();

		AutoritaEsternaSqlDAO lAutoritaSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;
		CSSASqlDAO lCSSASqlDao = null;
		AvvocatoFascicoloSiepPerEventoSqlDAO lAvvDao = null;
		AvvocatoFascicoloSigeSqlDAO lAvvSigeDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		siap.sige.avvocato.dao.AvvocatoSqlDAO avvocatoSqlDao = null;
		PartiUdienzaSqlDAO lPartiDao = null;

		try {
			for (NotificaModel lNotifica : aNotifiche) {
				// Autorita Esterne
				if (lNotifica.getAutEstIdAutoritaEsterna() != null) {
					lAutoritaSqlDao = new AutoritaEsternaSqlDAO(aConn);
					lAutoritaSqlDao.ricercaAutoritaEsternaByKey(lNotifica.getAutEstIdAutoritaEsterna());
					AutoritaEsternaModel lAutorita = (AutoritaEsternaModel) lAutoritaSqlDao.getModelByKey();
					// Inserisce l'occorenza nel model delle notifiche.
					lNotifica.setAutoritaEsterna(lAutorita);
				}

				// Modifica del 27/10/2015 MEV_15_S3
				// in presenza di una parte, si recuperano i dati della stessa
				// compresa la residenza, per leggere il valore del campo FLAG_DOMICILIO_DIFENSORE
				if (lNotifica.getIdParteUdienza() != null) {
					lPartiDao = new PartiUdienzaSqlDAO(aConn);
					lPartiDao.ricercaParteUdienzaByKey(lNotifica.getIdParteUdienza());
					AnagraficaPartiUdienzaModel lAnagParteMod = new AnagraficaPartiUdienzaModel(
							(AnagraficaPartiUdienzaModel) lPartiDao.getModelByKey());

					// Residenza/Domicilio
					ResidenzaSqlDAO lResidenzaDao = null;
					lResidenzaDao = new ResidenzaSqlDAO(aConn);
					lResidenzaDao.ricercaDomicilioCorrenteByIdParteUdienza(lAnagParteMod.getIdSoggetto());
					lAnagParteMod.setResidenza((ResidenzaModel) lResidenzaDao.getModelByKey());

					String lTempString = "";
					if (lAnagParteMod.getResidenza() != null) {
						if (lAnagParteMod.getResidenza().getDescrComune() != null
								&& lAnagParteMod.getResidenza().getDescrComune().compareTo("-") != 0)
							lTempString += lAnagParteMod.getResidenza().getDescrComune() + " ";
						if (lAnagParteMod.getResidenza().getCodProvincia() != null
								&& lAnagParteMod.getResidenza().getCodProvincia().compareTo("-") != 0)
							lTempString += "(Prov. " + lAnagParteMod.getResidenza().getCodProvincia() + ") ";

						if (lAnagParteMod.getResidenza().getCodStato().compareTo("039") != 0) { // straniero
							if (lAnagParteMod.getResidenza().getDescComuneEstero() != null
									&& lAnagParteMod.getResidenza().getDescComuneEstero() != "")
								lTempString += lAnagParteMod.getResidenza().getDescComuneEstero() + " ";

							lTempString += lAnagParteMod.getResidenza().getDescrStato() + " ";
						}
						if (lAnagParteMod.getResidenza().getIndirizzo() != null
								&& lAnagParteMod.getResidenza().getIndirizzo() != "")
							lTempString += lAnagParteMod.getResidenza().getIndirizzo() + " ";

						lAnagParteMod.setIndirizzoResidenza(lTempString);
					}

					if (lAnagParteMod.getResidenza() != null) {
						lNotifica.setFlagDomicilioDifensore(
								lAnagParteMod.getResidenza().getFlgDomicilioDifensore());
					}
					lNotifica.setPartiUdienza(lAnagParteMod);

				}

				// Preleva gli uffici
				if (lNotifica.getUffCodUfficio() != null
						&& lNotifica.getUffCodUfficio().compareTo("-") != 0) {
					lUffSqlDao = new UfficioSqlDAO(aConn);
					lUffSqlDao.selUfficioByCod(lNotifica.getUffCodUfficio());
					UfficioModel lUffMod = (UfficioModel) lUffSqlDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lNotifica.setUfficio(lUffMod);
				}
				// Preleva CSSA
				if (lNotifica.getCssIdCssa() != null) {
					lCSSASqlDao = new CSSASqlDAO(aConn);
					lCSSASqlDao.selCSSAByKey(lNotifica.getCssIdCssa());
					CSSAModel lCSSAMod = (CSSAModel) lCSSASqlDao.getModelByKey();
					// Inserisce l'occorrenza nel model delle notifiche.
					lNotifica.setCSSA(lCSSAMod);
				}

				// Preleva gli avvocati SIEP
				if (lNotifica.getAvvIdAvvocatoFascicoloSiep() != null) {
					lAvvDao = new AvvocatoFascicoloSiepPerEventoSqlDAO(aConn);

					lAvvDao.ricercaAvvocatoByKeyAvvocatoFasSiep(lNotifica.getAvvIdAvvocatoFascicoloSiep());
					lNotifica.setAvvSiep((AvvocatoSiepModel) lAvvDao.getModelByKey());
				}

				// Preleva gli avvocati SIGE
				if (lNotifica.getAvvIdAvvocatoFascicoloSige() != null) {
					lAvvSigeDao = new AvvocatoFascicoloSigeSqlDAO(aConn);
					lAvvSigeDao
							.ricercaAvvocatoByKeyAvvocatoFasSige(lNotifica.getAvvIdAvvocatoFascicoloSige());
					AvvocatoSigeModel avvocato = (AvvocatoSigeModel) lAvvSigeDao.getModelByKey();
					lNotifica.setAvvSige(avvocato);
					if (avvocato == null) {
						avvocatoSqlDao = new siap.sige.avvocato.dao.AvvocatoSqlDAO(aConn);
						avvocatoSqlDao.ricercaAvvocatobyKey(lNotifica.getAvvIdAvvocatoFascicoloSige());
						avvocatoSqlDao.start();
						if (avvocatoSqlDao.next())
							lNotifica.setAvvocato(
									(siap.sico.avvocato.model.AvvocatoModel) avvocatoSqlDao.getModel());

						avvocatoSqlDao.stop();
					}

					// Modifica del 27/10/2015 MEV_15_S3
					// in presenda di una parte, si recuperano i dati della stessa
					// compresa la residenza, per leggere il valore del campo FLAG_DOMICILIO_DIFENSORE
					if (lNotifica.getIdParteUdienza() != null) {
						AnagraficaPartiUdienzaModel lAnagParteMod = new AnagraficaPartiUdienzaModel();
						lPartiDao = new PartiUdienzaSqlDAO(aConn);
						lPartiDao.ricercaParteUdienzaByKey(lNotifica.getIdParteUdienza());
						lAnagParteMod = new AnagraficaPartiUdienzaModel(
								(AnagraficaPartiUdienzaModel) lPartiDao.getModelByKey());

						// Residenza/Domicilio
						ResidenzaSqlDAO lResidenzaDao = null;
						lResidenzaDao = new ResidenzaSqlDAO(aConn);
						lResidenzaDao.ricercaDomicilioCorrenteByIdParteUdienza(lAnagParteMod.getIdSoggetto());
						lAnagParteMod.setResidenza((ResidenzaModel) lResidenzaDao.getModelByKey());

						if (lAnagParteMod.getResidenza() != null) {
							lNotifica.setFlagDomicilioDifensore(
									lAnagParteMod.getResidenza().getFlgDomicilioDifensore());
						}
					}
				}

				// tipo istituto
				if (lNotifica.getIstDetIdIstitutoDetenzione() != null
						&& !lNotifica.getIstDetIdIstitutoDetenzione().equals("")) {
					lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
					lIstDao.ricercaIstitutoDetenzioneByKey(lNotifica.getIstDetIdIstitutoDetenzione());
					IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lNotifica.setIstitutoDetenzione(lIstituto);
					lIstDao.stop();
				}

				retNotifiche.add(lNotifica);
			} // endwhile
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new F3BException("StampaSigeController.getDestinatari: " + daoEx);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException("StampaSigeController.getDestinatari: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lAutoritaSqlDao);
			cleanup(lUffSqlDao);
			cleanup(lCSSASqlDao);
			cleanup(lAvvDao);
			cleanup(lAvvSigeDao);
			cleanup(avvocatoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lIstDao);
			cleanup(lPartiDao);
		}

		return retNotifiche;
	}

	private TreeModel prelevaDatiNotificheDestinatari(Vector<NotificaModel> lNotifiche, TreeModel aTree,
			Connection aConn) throws F3BException {

		TreeModel lTreeNotMod = null;
		TreeModel lTreeAutMod = null;
		TreeModel lTreeUffMod = null;
		TreeModel lTreeCSSAMod = null;
		TreeModel lTreeAvvSiepMod = null;
		TreeModel lTreeAvvSigeMod = null;
		TreeModel lTreeIstitutoDetenzione = null;
		TreeModel lTreePartiUdienzaMod = null;

		try {
			// Creazione treemodel degli eventuali destinatari: Autorita Esterna,Ufficio, Avvocato Siep,
			// Avvocato Sige, CSSA.

			for (NotificaModel lNotMod : lNotifiche) {

				lTreeNotMod = new TreeModel(lNotMod);

				// Tutti gli attributi di NotificaModel con dignità di model vengono esplosi in XML.

				// Autorità Esterna.
				if (lNotMod.getAutoritaEsterna() != null) {
					lTreeAutMod = new TreeModel(lNotMod.getAutoritaEsterna());
					lTreeNotMod.add(lTreeAutMod);
				}

				// Ufficio.
				if (lNotMod.getUfficio() != null) {
					lTreeUffMod = new TreeModel(lNotMod.getUfficio());
					lTreeNotMod.add(lTreeUffMod);
				}

				// CSSA.
				if (lNotMod.getCSSA() != null) {
					lTreeCSSAMod = new TreeModel(lNotMod.getCSSA());
					lTreeNotMod.add(lTreeCSSAMod);
				}

				// Avvocato SIEP.
				if (lNotMod.getAvvSiep() != null) {
					lTreeAvvSiepMod = new TreeModel(lNotMod.getAvvSiep().getAvvocato());
					lTreeNotMod.add(lTreeAvvSiepMod);
				}

				// Avvocato SIGE.
				if (lNotMod.getAvvocato() != null) {
					lTreeAvvSigeMod = new TreeModel(lNotMod.getAvvocato());
					lTreeNotMod.add(lTreeAvvSigeMod);
				}

				if (lNotMod.getAvvSige() != null) {
					lTreeAvvSigeMod = new TreeModel(lNotMod.getAvvSige().getAvvocato());
					lTreeNotMod.add(lTreeAvvSigeMod);
				}

				// Luogo Detenzione.
				if (lNotMod.getIstDetIdIstitutoDetenzione() != null) {
					lTreeIstitutoDetenzione = new TreeModel(lNotMod.getIstitutoDetenzione());
					lTreeNotMod.add(lTreeIstitutoDetenzione);
				}

				// Parti Udienza.
				if (lNotMod.getPartiUdienza() != null) {
					lTreePartiUdienzaMod = new TreeModel(lNotMod.getPartiUdienza());
					lTreeNotMod.add(lTreePartiUdienzaMod);
				}

				aTree.add(lTreeNotMod);
			} // endwhile
		} catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqe);
			throw new F3BException(
					"StampaSigeController.prelevaDatiNotificheDestinatari: Eccezione Generica: " + sqe);
		}

		return aTree;
	}

	/*
	 * La funzione estrae dalla tabella CAMPO_NOTA i record collegati all'Evento e li aggiunge al TreeModel
	 * passato.
	 */
	private TreeModel prelevaDatiCampoNota(TreeModel aTree, BigDecimal aEventoKey, Connection aConn)
			throws Exception {

		// CampoNote
		CampoNotaSqlDAO lCampoNotaSqlDao = null;
		Vector lCampiNote = null;
		CampoNotaModel lCampoNota = null;
		TreeModel LTreeNota = null;

		try {
			// Ricerca Campi note collegate all'evento
			lCampoNotaSqlDao = new CampoNotaSqlDAO(aConn);
			lCampoNotaSqlDao.ricercaCampoNotaByKeyEvento(aEventoKey);
			lCampiNote = new Vector(lCampoNotaSqlDao.getModels());
			Iterator lItx = lCampiNote.iterator();
			while (lItx.hasNext()) {
				lCampoNota = (CampoNotaModel) lItx.next();
				LTreeNota = new TreeModel(lCampoNota);
				aTree.add(LTreeNota);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lCampoNotaSqlDao);
		}
		return aTree;
	}

	/*
	 * 27/05/2010 La funzione estrae dalla tabella ANNOTAZIONE_MANUALE i record collegati all'Evento e li
	 * aggiunge al TreeModel passato.
	 */
	private TreeModel prelevaDatiAnnotazioneManuale(TreeModel aTree, EventoModel aEvento, Connection aConn)
			throws Exception {

		// AnnotazioneManuale
		AnnotazioneManualeSqlDAO lAnnManSqlDao = null;
		Vector lVectAnnMan = null;
		AnnotazioneManualeModel lAnnManuale = null;
		TreeModel LTreeAnnotazioni = null;

		try {
			// Ricerca Annotazioni Manuali collegate all'evento
			lAnnManSqlDao = new AnnotazioneManualeSqlDAO(aConn);
			lAnnManSqlDao.ricercaAnnotazioneManualeByIdEvento(aEvento.getIdEvento());
			lVectAnnMan = new Vector(lAnnManSqlDao.getModels());
			Iterator lItx = lVectAnnMan.iterator();
			while (lItx.hasNext()) {
				lAnnManuale = (AnnotazioneManualeModel) lItx.next();
				// Controlla che l'evento legato all'annotazione sia validato e sia del tipo
				// 0287 (Determinazione Pena - ex art. 671 c.p.p. e 174 c.p.)
				// 0290 (Applicazione Benefici - ex art. 174 c.p. e 672 c.p.p. in maschera 'Applicazione
				// benefici : Indulto')
				if (aEvento != null && "S".equals(aEvento.getFlagDocumentoRegistrato())
						&& aEvento.getCodTipoProvvedimento() != null && aEvento.getCodMotivo() != null) {
					String lCodMotivo = aEvento.getCodMotivo();
					if (("26".equals(aEvento.getCodTipoProvvedimento()) && "0290".equals(lCodMotivo))
							|| ("26".equals(aEvento.getCodTipoProvvedimento())
									&& "0287".equals(lCodMotivo))) {
						// Formatta per il template la stringa reclusione e arresto
						lAnnManuale.calcolaStringaReclusione();
						lAnnManuale.calcolaStringaArresto();
					}
				}
				LTreeAnnotazioni = new TreeModel(lAnnManuale);
				aTree.add(LTreeAnnotazioni);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cleanup(lAnnManSqlDao);
		}
		return aTree;
	}

	/**
	 * Genera il TreeModel per la stampa dell'Udienza.
	 *
	 * @param aKey
	 * @return dati del soggetto come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiFissazioneUdienza(BigDecimal aIdFasSige, BigDecimal aIdUdienza,
			Connection aConn, BigDecimal idEvento) throws F3BException {

		TreeModel lTreeFissazioneUdienza = null;
		TreeModel lTreeUdienzaProcedimento = null;
		TreeModel lTreeProvvedimento = null;
		TreeModel lTreeEvento = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Udienza Sige");

			// 20170921: [SG] gestione fissazione udienza senza date
			UdienzaSigeModel lUdiMod = new UdienzaSigeModel();
			if (aIdUdienza != null) {
				// Lettura dell'Udienza SIGE.
				lUdiSigeSqlDao = new UdienzaSigeSqlDAO(aConn);
				lUdiSigeSqlDao.ricercaUdienzaSigeByKey(aIdUdienza);
				lUdiMod = (UdienzaSigeModel) lUdiSigeSqlDao.getModelByKey();
			}

			// Lettura dell'Udienza_Procedimento SIGE.
			lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(aConn);
			UdienzaProcedimentoSigeModel lUdiProSige = new UdienzaProcedimentoSigeModel();
			lUdiProSige.setFasIdFascicoloSige(aIdFasSige);
			lUdiProSige.setUdiIdUdienzaSige(aIdUdienza);
			lUdiProSige.setFlagRinviata("F");
			lUdiProSqlDao.ricercaUdienzaProcedimentoSige(lUdiProSige);
			lUdiProSqlDao.ricercaUdienzaProcedimentoByEve(idEvento);
			lUdiProSige = (UdienzaProcedimentoSigeModel) lUdiProSqlDao.getModelByKey();

			if (lUdiProSige == null) {
				lUdiProSige = new UdienzaProcedimentoSigeModel();
				lUdiProSige.setFasIdFascicoloSige(aIdFasSige);
				lUdiProSige.setUdiIdUdienzaSige(aIdUdienza);
				lUdiProSige.setFlagRinviata("F");

				lUdiProSqlDao.ricercaUdienzaProcedimentoSige(lUdiProSige);
				lUdiProSige = (UdienzaProcedimentoSigeModel) lUdiProSqlDao.getModelByKey();
			}

			lTreeUdienzaProcedimento = new TreeModel(lUdiProSige);
			lTreeFissazioneUdienza = new TreeModel(lUdiMod);
			lTreeFissazioneUdienza.add(lTreeUdienzaProcedimento);

			// 20170921: [SG] aggiunto controllo preventivo
			if (lUdiMod != null && lUdiMod.getCodIdAulaUdienza() != null) {
				IAula aulaCtrl = SIGELookupRemote.getAulaRemote();
				AulaUdienzaModel aula = aulaCtrl.ExRicercaAulaByIdAula(lUdiMod.getCodIdAulaUdienza());
				lUdiMod.setAulaUdienzaModel(aula);
			}

			// Lettura composizione collegio che aggiunge all'udienza
			if (lUdiMod.getColIdCollegio() != null)
				lTreeFissazioneUdienza.add(prelevaDatiCollegioById(lUdiMod.getColIdCollegio(), aConn));

			// Lettura del Provvedimento completo di Evento e Notifiche.
			ProvvedimentoSigeModel lProSige = new ProvvedimentoSigeModel();
			lProSige.setFasIdFascicoloSige(aIdFasSige);
			lProSige.setIdEventoGenerato(lUdiProSige.getEveIdEvento());

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(aConn);
			lProvSqlDao.ricercaProvvedimentoPerEvento(lProSige);

			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				lProSige = (ProvvedimentoSigeModel) lProvSqlDao.getModel();

				lTreeProvvedimento = new TreeModel(lProSige);

				lEveSqlDao = new EventoSqlDAO(aConn);
				lEveSqlDao.ricercaEventoByKey(lProSige.getIdEventoGenerato());
				EventoModel lEvento = new EventoModel();
				lEvento = (EventoModel) lEveSqlDao.getModelByKey();
				lTreeEvento = prelevaDatiEvento(lEvento, aConn);
				lTreeProvvedimento.add(lTreeEvento);

				// Lettura dei "Tenore Sige Esteso" via Controller.
				ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setFasIdFascicoloSige(aIdFasSige);
				Vector<TenoreSigeEstesoModel> lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);
				lTenoriEstesi = cleanTenori(lTenoriEstesi);
				// Costruzione e caricamento della struttura dei Tenori nel Provvedimento.
				lTreeProvvedimento = buildTreeTenoriEstesi(lTreeProvvedimento, lTenoriEstesi);
				List<ParteCivileUdienzaModel> partiCivili = prelevaPartiCivili(lUdiProSige);
				lTreeProvvedimento = buildTreePartiCivili(partiCivili, lTreeProvvedimento);

				List<ParteOffesaUdienzaModel> partiOffese = prelevaPartiOffese(lUdiProSige);
				lTreeProvvedimento = buildTreePartiOffese(partiOffese, lTreeProvvedimento);

			}

			lTreeFissazioneUdienza.add(lTreeProvvedimento);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiFissazioneUdienza : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			sqlEx.printStackTrace();
			throw new SIGEException("StampaSigeController.prelevaDatiFissazioneUdienza : " + sqlEx);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lUdiSigeSqlDao);
			cleanup(lUdiProSqlDao);
		}
		return lTreeFissazioneUdienza;
	}

	private TreeModel buildTreePartiCivili(List<ParteCivileUdienzaModel> partiCivili,
			TreeModel provvedimento) {

		for (ParteCivileUdienzaModel parteCivile : partiCivili) {
			TreeModel parteCivileModel = new TreeModel(parteCivile);
			List<PartiUdienzaDifensoreModel> difensori = parteCivile.getDifensori();
			for (PartiUdienzaDifensoreModel difensore : difensori) {
				parteCivileModel.add(new TreeModel(difensore));
			}

			List<NotificaParteCivileModel> notificheParticiCivili = prelevaNotifichePartiCivili(parteCivile);
			for (NotificaParteCivileModel notifica : notificheParticiCivili) {
				parteCivileModel.add(new TreeModel(notifica));
			}

			provvedimento.add(parteCivileModel);

		}
		return provvedimento;
	}

	private TreeModel buildTreePartiOffese(List<ParteOffesaUdienzaModel> partiOffese,
			TreeModel provvedimento) {

		for (ParteOffesaUdienzaModel parteOffesa : partiOffese) {
			TreeModel parteOffesaModel = new TreeModel(parteOffesa);
			List<PartiUdienzaDifensoreModel> difensori = parteOffesa.getDifensori();
			for (PartiUdienzaDifensoreModel difensore : difensori) {
				parteOffesaModel.add(new TreeModel(difensore));
			}

			List<NotificaParteOffesaModel> notifichePartiOffese = prelevaNotifichePartiOffese(parteOffesa);
			for (NotificaParteOffesaModel notificaParteOffesa : notifichePartiOffese) {
				parteOffesaModel.add(new TreeModel(notificaParteOffesa));
			}

			provvedimento.add(parteOffesaModel);
		}
		return provvedimento;
	}

	/**
	 * Genera il TreeModel per la stampa dell'Udienza.
	 *
	 * @param aKey
	 * @return dati del soggetto come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiOrdinanzaRinvioUdienza(BigDecimal aIdFasSige, BigDecimal aIdUdienza,
			Connection aConn) throws F3BException {

		TreeModel lTreeFissazioneUdienza = null;
		TreeModel lTreeUdienzaProcedimento = null;
		TreeModel lTreeProvvedimento = null;
		TreeModel lTreeEvento = null;

		ProvvedimentoSigeSqlDAO lProvSqlDao = null;
		EventoSqlDAO lEveSqlDao = null;
		UdienzaSigeSqlDAO lUdiSigeSqlDao = null;
		UdienzaProcedimentoSigeSqlDAO lUdiProSqlDao = null;

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Udienza Sige");

			// Lettura dell'Udienza SIGE.
			lUdiSigeSqlDao = new UdienzaSigeSqlDAO(aConn);
			lUdiSigeSqlDao.ricercaUdienzaSigeByKey(aIdUdienza);
			UdienzaSigeModel lUdiMod = (UdienzaSigeModel) lUdiSigeSqlDao.getModelByKey();

			// TreeModel Fissazione Udienza.
			lTreeFissazioneUdienza = new TreeModel(lUdiMod);

			// Lettura dell'Udienza_Procedimento SIGE.
			lUdiProSqlDao = new UdienzaProcedimentoSigeSqlDAO(aConn);
			UdienzaProcedimentoSigeModel lUdiProSige = new UdienzaProcedimentoSigeModel();
			lUdiProSige.setFasIdFascicoloSige(aIdFasSige);
			lUdiProSige.setUdiIdUdienzaSige(aIdUdienza);
			lUdiProSige.setFlagRinviata("S");
			lUdiProSqlDao.ricercaUdienzaProcedimentoSige(lUdiProSige);
			lUdiProSige = (UdienzaProcedimentoSigeModel) lUdiProSqlDao.getModelByKey();

			lTreeUdienzaProcedimento = new TreeModel(lUdiProSige);
			lTreeFissazioneUdienza.add(lTreeUdienzaProcedimento);

			// Lettura composizione collegio che aggiunge all'udienza
			if (lUdiMod.getColIdCollegio() != null)
				lTreeFissazioneUdienza.add(prelevaDatiCollegioById(lUdiMod.getColIdCollegio(), aConn));

			// Lettura del Provvedimento completo di Evento e Notifiche.
			ProvvedimentoSigeModel lProSige = new ProvvedimentoSigeModel();
			lProSige.setFasIdFascicoloSige(aIdFasSige);
			lProSige.setIdEventoGenerato(lUdiProSige.getEveIdEvento());

			lProvSqlDao = new ProvvedimentoSigeSqlDAO(aConn);
			lProvSqlDao.ricercaProvvedimentoPerEvento(lProSige);

			lProvSqlDao.start();
			if (lProvSqlDao.next()) {
				lProSige = (ProvvedimentoSigeModel) lProvSqlDao.getModel();

				lTreeProvvedimento = new TreeModel(lProSige);

				lEveSqlDao = new EventoSqlDAO(aConn);
				lEveSqlDao.ricercaEventoByKey(lProSige.getIdEventoGenerato());
				EventoModel lEvento = new EventoModel();
				lEvento = (EventoModel) lEveSqlDao.getModelByKey();
				lTreeEvento = prelevaDatiEvento(lEvento, aConn);
				lTreeProvvedimento.add(lTreeEvento);

				// Lettura dei "Tenore Sige Esteso" via Controller.
				ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setFasIdFascicoloSige(aIdFasSige);
				Vector<TenoreSigeEstesoModel> lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);
				lTenoriEstesi = cleanTenori(lTenoriEstesi);
				// Costruzione e caricamento della struttura dei Tenori nel Provvedimento.
				lTreeProvvedimento = buildTreeTenoriEstesi(lTreeProvvedimento, lTenoriEstesi);

			}
			lTreeFissazioneUdienza.add(lTreeProvvedimento);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiFissazioneUdienza : " + daoEx);
		} catch (Exception sqlEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + sqlEx);
			throw new SIGEException("StampaSigeController.prelevaDatiFissazioneUdienza : " + sqlEx);
		} finally {
			cleanup(lProvSqlDao);
			cleanup(lEveSqlDao);
			cleanup(lUdiSigeSqlDao);
			cleanup(lUdiProSqlDao);
		}
		return lTreeFissazioneUdienza;
	}

	/**
	 * Esegue il prelievo dati dei Tenori Sige a partire dal vettore di Tenori Estesi.
	 *
	 * @param aTenoriEstesi
	 *            del Fascicolo SIGE.
	 * @return TreeModel dei Tenori Estesi.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel buildTreeTenoriEstesi(TreeModel lTreeProvvedimento,
			Vector<TenoreSigeEstesoModel> lTenoriEstesi) throws F3BException {

		// Si itera sull'elenco dei Tenori per caricare il TreeModel con Tenore, Sentenza e Reato.
		TenoreSigeEstesoModel lTenEstesoPrec = new TenoreSigeEstesoModel();

		TreeModel lTreeTenoreEsteso = null;
		TreeModel lTreeSentenzaReati = null; // 21/05/2010
		// 30/11/2018 intervento per anomalia segnalata da Nunzia (email del 28/11/2018 - copertina con più
		// titoli duplicati.rtf)
		Vector<BigDecimal> sentenze = null;
		int counter = 0;
		for (TenoreSigeEstesoModel lTenEsteso : lTenoriEstesi) {
			// Per gli Oggetti (Tenori) successivi al primo.
			if (lTenEstesoPrec != null && lTenEstesoPrec.getTenoreSige() != null) {
				// A rottura di Tenore, aggiungo la struttura dati costruita.
				if (lTenEsteso.getTenoreSige().getIdTenoreSige()
						.compareTo(lTenEstesoPrec.getTenoreSige().getIdTenoreSige()) != 0) {
					if (lTreeTenoreEsteso != null) {
						// 21/05/2010 Indentatura di Sentenza-Reato-SentenzaSige in Tenore
						if (lTreeSentenzaReati != null)
							lTreeTenoreEsteso.add(lTreeSentenzaReati);
						lTreeProvvedimento.add(lTreeTenoreEsteso);
						lTreeTenoreEsteso = null;
						lTreeSentenzaReati = null;
					}
					lTreeTenoreEsteso = new TreeModel(lTenEsteso.getTenoreSige().decodifica());
					// 21/05/2010 Sentenza Tenore indentata.
					// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getSentenza() ));
					// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getReato() ));
					// 30/11/2018 intervento per anomalia segnalata da Nunzia (email del 28/11/2018 -
					// copertina con più titoli duplicati.rtf)
					// controllo oltre il fatto che la sentenza già esista anche che sia l'ultimo elemento
					// della lista
					boolean isUltimoElemento = (lTenoriEstesi.size() - 1) == counter;
					if (!sentenze.contains(lTenEsteso.getSentenza().getIdSentenza()) || isUltimoElemento) {
						lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
						sentenze.add(lTenEsteso.getSentenza().getIdSentenza());
						lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
						if (lTenEsteso.getSentenzaSige() != null)
							lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenzaSige()));
					}

					// Modifica del 23/11/2016 MEV_15_S4
					// Vengono aggiunte le Sentenze legate al Tenore Sige,
					// tranne la sentenza principale aggiunta in fase di iscrizione del fascicolo Sige
					// già recuperata in precedenza e presente sulla tabella FAS_SIGE_SENTENZA
					// Nota: la Data Irrevocabilità è valorizzata solo per questa sentenza
					if (lTenEsteso.getSentenzaSigeVector().size() > 0) {
						Iterator its = lTenEsteso.getSentenzaSigeVector().iterator();
						while (its.hasNext()) {
							SentenzaModel lSent = (SentenzaModel) its.next();
							sentenze.add(lSent.getIdSentenza());
							// lTreeTenoreEsteso.add(new TreeModel(lSent));
							// Recupero la sentenza SIGE completa per prendere la data di irevocabilità
							// (Ticket#20191029016 + Ticket#20191031016 della stampa copertina)
							TreeModel sentenza = new TreeModel(lSent);
							IFasSigeSentenza ifss = SIGELookupRemote.getFasSigeSentenzaRemote();
							SentenzaSigeModel ssm = new SentenzaSigeModel();
							ssm.setFasIdFascicoloSige(lTenEsteso.getFasIdFascicoloSige());
							ssm.setIdSentenza(lSent.getIdSentenza());
							Vector v = ifss.ExRicercaFasSigeSentenza(ssm);
							if (v != null && v.size() > 0) {
								SentenzaSigeModel sentenzaCompleta = (SentenzaSigeModel) v.get(0);
								TreeModel sentenzaSige = new TreeModel(sentenzaCompleta);
								sentenza.add(sentenzaSige);
							}
							lTreeTenoreEsteso.add(sentenza);
						}
					}
				} else {
					// A rottura di Sentenza, si aggiunge solo Sentenza e Reato.
					// 30/11/2018 intervento per anomalia segnalata da Nunzia (email del 28/11/2018 -
					// copertina con più titoli duplicati.rtf)
					if (lTenEsteso.getTenoreSige().getIdSentenza()
							.compareTo(lTenEstesoPrec.getTenoreSige().getIdSentenza()) != 0
							&& !sentenze.contains(lTenEstesoPrec.getTenoreSige().getIdSentenza())) {
						// 21/05/2010 Sentenza Tenore indentata.
						// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getSentenza() ));
						// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getReato() ));
						if (lTreeSentenzaReati != null) {
							// lTreeTenoreEsteso.add(lTreeSentenzaReati);
							// 30/11/2018 intervento per anomalia segnalata da Nunzia (email del 28/11/2018 -
							// copertina con più titoli duplicati.rtf)
							if (!sentenze.contains(lTenEsteso.getSentenza().getIdSentenza())) {
								lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
								sentenze.add(lTenEsteso.getSentenza().getIdSentenza());
							}

							// Modifica del 23/11/2016 MEV_15_S4
							// Vengono aggiunte le Sentenze legate al Tenore Sige,
							// tranne la sentenza principale aggiunta in fase di iscrizione del fascicolo Sige
							// gia' recuperata in precedenza e presente sulla tabella FAS_SIGE_SENTENZA
							// Nota: la Data Irrevocabilità è valorizzata solo per questa sentenza
							if (lTenEsteso.getSentenzaSigeVector().size() > 0) {
								Iterator its = lTenEsteso.getSentenzaSigeVector().iterator();
								while (its.hasNext()) {
									SentenzaModel lSent = (SentenzaModel) its.next();
									sentenze.add(lSent.getIdSentenza());
									// lTreeTenoreEsteso.add(new TreeModel(lSent));
									// Recupero la sentenza SIGE completa per prendere la data di
									// irevocabilità
									// (Ticket#20191029016 + Ticket#20191031016 della stampa copertina)
									TreeModel sentenza = new TreeModel(lSent);
									IFasSigeSentenza ifss = SIGELookupRemote.getFasSigeSentenzaRemote();
									SentenzaSigeModel ssm = new SentenzaSigeModel();
									ssm.setFasIdFascicoloSige(lTenEsteso.getFasIdFascicoloSige());
									ssm.setIdSentenza(lSent.getIdSentenza());
									Vector v = ifss.ExRicercaFasSigeSentenza(ssm);
									if (v != null && v.size() > 0) {
										SentenzaSigeModel sentenzaCompleta = (SentenzaSigeModel) v.get(0);
										TreeModel sentenzaSige = new TreeModel(sentenzaCompleta);
										sentenza.add(sentenzaSige);
									}
									lTreeTenoreEsteso.add(sentenza);
								}
							}
						} else {
							// 30/11/2018 intervento per anomalia segnalata da Nunzia (email del 28/11/2018 -
							// copertina con più titoli duplicati.rtf)
							if (!sentenze.contains(lTenEsteso.getSentenza().getIdSentenza())) {
								lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
								sentenze.add(lTenEsteso.getSentenza().getIdSentenza());
								// Modifica del 23/11/2016 MEV_15_S4
								// Vengono aggiunte le Sentenze legate al Tenore Sige,
								// tranne la sentenza principale aggiunta in fase di iscrizione del fascicolo
								// Sige già recuperata in precedenza e presente sulla tabella
								// FAS_SIGE_SENTENZA
								// Nota: la Data Irrevocabilità è valorizzata solo per questa sentenza
								if (lTenEsteso.getSentenzaSigeVector().size() > 0) {
									Iterator its = lTenEsteso.getSentenzaSigeVector().iterator();
									while (its.hasNext()) {
										SentenzaModel lSent = (SentenzaModel) its.next();
										sentenze.add(lSent.getIdSentenza());
										// lTreeTenoreEsteso.add(new TreeModel(lSent));
										// Recupero la sentenza SIGE completa per prendere la data di
										// irevocabilità
										// (Ticket#20191029016 + Ticket#20191031016 della stampa copertina)
										TreeModel sentenza = new TreeModel(lSent);
										IFasSigeSentenza ifss = SIGELookupRemote.getFasSigeSentenzaRemote();
										SentenzaSigeModel ssm = new SentenzaSigeModel();
										ssm.setFasIdFascicoloSige(lTenEsteso.getFasIdFascicoloSige());
										ssm.setIdSentenza(lSent.getIdSentenza());
										Vector v = ifss.ExRicercaFasSigeSentenza(ssm);
										if (v != null && v.size() > 0) {
											SentenzaSigeModel sentenzaCompleta = (SentenzaSigeModel) v.get(0);
											TreeModel sentenzaSige = new TreeModel(sentenzaCompleta);
											sentenza.add(sentenzaSige);
										}
										lTreeTenoreEsteso.add(sentenza);
									}
								}

								lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
								if (lTenEsteso.getSentenzaSige() != null)
									lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenzaSige()));
							}
						}
					} else {
						// A rottura di Reato, si aggiunge solo Reato.
						// 22/05/2019: AGGIUNGO LA NEW PER ERRORE DI NULLPOINTER
						lTreeSentenzaReati = new TreeModel(lTenEsteso.getReato());
						lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
					}
				}
			} else {
				// Sono al primo elemento di Tenore Esteso.
				lTreeTenoreEsteso = new TreeModel(lTenEsteso.getTenoreSige().decodifica());
				// 21/05/2010 Sentenza Tenore indentata.
				// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getSentenza() ));
				// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getReato() ));
				lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
				// 30/11/2018 aggiungo blocco su richiesta Nunzia (email del 28/11/2018 - copertina con più
				// titoli duplicati.rtf)
				sentenze = new Vector();
				sentenze.add(lTenEsteso.getSentenza().getIdSentenza());
				if (lTenEsteso.getReato() != null)
					lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
				if (lTenEsteso.getSentenzaSige() != null)
					lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenzaSige()));

				// Modifica del 23/11/2016 MEV_15_S4
				// Vengono aggiunte le Sentenze legate al Tenore Sige,
				// tranne la sentenza principale aggiunta in fase di iscrizione del fascicolo Sige
				// già recuperata in precedenza e presente sulla tabella FAS_SIGE_SENTENZA
				// Nota: la Data Irrevocabilità è valorizzata solo per questa sentenza
				if (lTenEsteso.getSentenzaSigeVector().size() > 0) {
					Iterator its = lTenEsteso.getSentenzaSigeVector().iterator();
					while (its.hasNext()) {
						SentenzaModel lSent = (SentenzaModel) its.next();
						sentenze.add(lSent.getIdSentenza());
						// Recupero la sentenza SIGE completa per prendere la data di irevocabilità
						// (Ticket#20191029016 + Ticket#20191031016 della stampa copertina)
						TreeModel sentenza = new TreeModel(lSent);
						IFasSigeSentenza ifss = SIGELookupRemote.getFasSigeSentenzaRemote();
						SentenzaSigeModel ssm = new SentenzaSigeModel();
						ssm.setFasIdFascicoloSige(lTenEsteso.getFasIdFascicoloSige());
						ssm.setIdSentenza(lSent.getIdSentenza());
						Vector v = ifss.ExRicercaFasSigeSentenza(ssm);
						if (v != null && v.size() > 0) {
							SentenzaSigeModel sentenzaCompleta = (SentenzaSigeModel) v.get(0);
							TreeModel sentenzaSige = new TreeModel(sentenzaCompleta);
							sentenza.add(sentenzaSige);
						}

						lTreeTenoreEsteso.add(sentenza);
					}
				}
			}
			// Impostazione del TenoreEsteso appena elaborato come "Precedente".
			lTenEstesoPrec = lTenEsteso;
			counter++;
		}
		// Finito il ciclo While, Carico la struttura del TreeModel.
		// 21/05/2010 Si indenta Sentenza-Reato-SentenzaSige in Tenore
		if (lTreeSentenzaReati != null)
			lTreeTenoreEsteso.add(lTreeSentenzaReati);
		lTreeProvvedimento.add(lTreeTenoreEsteso);

		return lTreeProvvedimento;
	}

	/**
	 * 15/12/2009 Alimenta il TreeModel di Provvedimento con i dati di Tenori Sige a partire dal vettore di
	 * Tenori Estesi. Legge e carica anche i DATI PROVVEDIMENTO per ogni singolo Tenore.
	 *
	 * @param aTenoriEstesi
	 *            del Fascicolo SIGE.
	 * @return TreeModel dei Tenori Estesi.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel buildTreeTenoriEstesiConDatiProvvedimento(TreeModel lTreeProvvedimento,
			Vector lTenoriEstesi, Connection lConn) throws F3BException {

		// Si itera sull'elenco dei Tenori per caricare il TreeModel con Tenore, Sentenza e Reato.
		// DatiProvvedimentoSigeSqlDAO lDatDao = null;
		TenoreSigeEstesoModel lTenEstesoPrec = new TenoreSigeEstesoModel();
		TenoreSigeEstesoModel lTenEsteso = new TenoreSigeEstesoModel();
		Iterator itx = lTenoriEstesi.iterator();
		TreeModel lTreeTenoreEsteso = null;
		TreeModel lTreeSentenzaReati = null; // 21/05/2010
		while (itx.hasNext()) {
			lTenEsteso = (TenoreSigeEstesoModel) itx.next();
			// Per gli Oggetti (Tenori) successivi al primo.
			if (lTenEstesoPrec != null && lTenEstesoPrec.getTenoreSige() != null) {
				// A rottura di Tenore, aggiungo la struttura dati costruita.
				if (lTenEsteso.getTenoreSige().getIdTenoreSige()
						.compareTo(lTenEstesoPrec.getTenoreSige().getIdTenoreSige()) != 0) {
					if (lTreeTenoreEsteso != null) {
						// 21/05/2010 Indentatura di Sentenza-Reato-SentenzaSige in Tenore
						if (lTreeSentenzaReati != null)
							lTreeTenoreEsteso.add(lTreeSentenzaReati);

						// 21/05/2010 Lettura Eventuali Dati Provvedimento Sige.
						if (lTenEstesoPrec.getTenoreSige().getProvIdProvvedimentoSige() != null) {
							lTreeTenoreEsteso = getDatiProvvedimentoSige(lTreeTenoreEsteso,
									lTenEstesoPrec.getTenoreSige().getIdTenoreSige(), lConn);
						}
						// @emma 23072018 intervento post COLLAUDO 11.2 (aggiungo all'albero solo se il codice
						// oggetto è diverso)
						if (!lTenEsteso.getTenoreSige().getCodOggettoSige()
								.equals(lTenEstesoPrec.getTenoreSige().getCodOggettoSige())) {
							lTreeProvvedimento.add(lTreeTenoreEsteso);
						}
						lTreeTenoreEsteso = null;
						lTreeSentenzaReati = null;
					}
					lTreeTenoreEsteso = new TreeModel(lTenEsteso.getTenoreSige().decodifica());
					// 21/05/2010 Sentenza Tenore indentata.
					// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getSentenza() ));
					// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getReato() ));
					lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
					lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
					if (lTenEsteso.getSentenzaSige() != null)
						lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenzaSige()));
				} else {
					// A rottura di Sentenza, si aggiunge solo Sentenza e Reato.
					if (lTenEsteso.getTenoreSige().getIdSentenza()
							.compareTo(lTenEstesoPrec.getTenoreSige().getIdSentenza()) != 0) {
						// 21/05/2010 Sentenza Tenore indentata.
						// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getSentenza() ));
						// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getReato() ));
						if (lTreeSentenzaReati != null) {
							lTreeTenoreEsteso.add(lTreeSentenzaReati);
							// lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenza() ));
							lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
						} else
							lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());

						lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
						if (lTenEsteso.getSentenzaSige() != null)
							lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenzaSige()));
					} else {
						// A rottura di Reato, si aggiunge solo Reato.
						lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
					}
				}
			} else {
				// Sono al primo elemento di Tenore Esteso.
				lTreeTenoreEsteso = new TreeModel(lTenEsteso.getTenoreSige().decodifica());
				// 21/05/2010 Sentenza Tenore indentata.
				// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getSentenza() ));
				// lTreeTenoreEsteso.add(new TreeModel(lTenEsteso.getReato() ));
				lTreeSentenzaReati = new TreeModel(lTenEsteso.getSentenza());
				if (lTenEsteso.getReato() != null)
					lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getReato()));
				if (lTenEsteso.getSentenzaSige() != null)
					lTreeSentenzaReati.add(new TreeModel(lTenEsteso.getSentenzaSige()));
			}
			// Impostazione del TenoreEsteso appena elaborato come "Precedente".
			lTenEstesoPrec = lTenEsteso;
		}
		// Finito il ciclo While, Carico la struttura del TreeModel.
		// 21/05/2010 Si indenta Sentenza-Reato-SentenzaSige in Tenore
		if (lTreeSentenzaReati != null)
			lTreeTenoreEsteso.add(lTreeSentenzaReati);

		// 24/05/2010 Lettura Eventuali Dati Provvedimento Sige Ultimo Tenore.
		if (lTenEsteso.getTenoreSige().getProvIdProvvedimentoSige() != null)
			lTreeTenoreEsteso = getDatiProvvedimentoSige(lTreeTenoreEsteso,
					lTenEsteso.getTenoreSige().getIdTenoreSige(), lConn);

		lTreeProvvedimento.add(lTreeTenoreEsteso);

		return lTreeProvvedimento;
	}

	/**
	 * Esegue il prelievo dati delle Sentenze Sige + FascicoloSIEP.
	 *
	 * @param aIdFasSige
	 *            Chiave del Fascicolo SIGE.
	 * @param aConn
	 *            connessione al dbase.
	 * @return dati delle Sentenze afferenti al Fascicolo SIGE e relativo FascicoloSIEP come TreeModel.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel prelevaDatiSentenzeSige(TreeModel lTreeFasSige, BigDecimal aIdFasSige, Connection aConn)
			throws F3BException {

		SentenzaSqlDAO lSenDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloSiepSqlDAO lFasDao = null;
		MisuraCautelareSqlDAO lMisDao = null;
		ReatoSqlDAO lReaDao = null;
		MisuraSicurezzaSqlDAO lMisSicDao = null;
		PenaResiduaSqlDAO lPenaResDao = null;
		FasSigeSentenzaDAO lFasSenDao = null;

		SentenzaModel lSentenza = null;
		SentenzaSigeModel lFasSigeSentenza = new SentenzaSigeModel();
		SoggettoModel lSoggetto = null;

		TreeModel lTreeSentenzaSige = new TreeModel();
		Vector lFasSigeSentenze = new Vector(); // Vettore delle FAS_SIGE_SENTENZA

		lFasSigeSentenza.setFasIdFascicoloSige(aIdFasSige);
		// Ricerca dei riferimenti a Sentenze nella tabella di relazione FAS_SIGE_SENTENZA
		lFasSigeSentenza.setFasIdFascicoloSige(aIdFasSige);

		try {
			lSenDao = new SentenzaSqlDAO(aConn);
			lFasSenDao = new FasSigeSentenzaDAO(aConn);
			lFasSenDao.setCondizione(lFasSigeSentenza);
			lFasSigeSentenze = new Vector(lFasSenDao.getModels());

			if (lFasSigeSentenze != null) {
				// Si itera sull'elenco dei FasSigeSentenzaModel per trovare le Sentenze
				Iterator itx = lFasSigeSentenze.iterator();
				while (itx.hasNext()) {
					// Ricerca Sentenza a partire da ID_SENTENZA
					lFasSigeSentenza = (SentenzaSigeModel) itx.next();
					lSenDao.ricercaSentenzaBykey(lFasSigeSentenza.getIdSentenza());
					lSentenza = new SentenzaModel((SentenzaModel) lSenDao.getModelByKey());

					// Ricerca del Fascicolo SIEP
					TreeModel lTreeFasSiep = null;
					if (lFasSigeSentenza.getFasSieIdFascicoloSiep() != null) {
						lFasDao = new FascicoloSiepSqlDAO(aConn);
						lFasDao.ricercaFascicoloByKey(lFasSigeSentenza.getFasSieIdFascicoloSiep());
						FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();

						// Cerca il soggetto associato al fascicolo
						lSogSqlDao = new SoggettoSqlDAO(aConn);
						lSogSqlDao.ricercaSoggettoByKey(lFasModel.getSogIdSoggetto());
						lSoggetto = new SoggettoModel((SoggettoModel) lSogSqlDao.getModelByKey());

						TreeModel lTreeSoggettoSiep = null;
						// if (lSoggetto == null) {
						// } else {
						lFasModel.setSoggetto(lSoggetto);
						lTreeSoggettoSiep = new TreeModel(lSoggetto);
						// }

						// Pena Residua
						lPenaResDao = new PenaResiduaSqlDAO(aConn);
						lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(
								lFasSigeSentenza.getFasSieIdFascicoloSiep());
						PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenaResDao.getModelByKey();

						// Stringa Arresto - Reclusione
						if (lPenResMod != null) {
							lPenResMod.calcolaStringaReclusione();
							lPenResMod.calcolaStringaArresto();
						}

						// Crea TreeModel Fascicolo Siep
						lTreeFasSiep = new TreeModel(lFasModel);
						lTreeFasSiep.add(lTreeSoggettoSiep);
					}
					// Costruzione del TreeModel x SentenzaSigeModel
					lTreeSentenzaSige = new TreeModel(lFasSigeSentenza);
					lTreeSentenzaSige.add(new TreeModel(lSentenza));
					if (lTreeFasSiep != null)
						lTreeSentenzaSige.add(lTreeFasSiep);

					lTreeFasSige.add(lTreeSentenzaSige);
				}
			}
		} catch (Exception e) {
			throw new F3BException("StampaSigeController.prelevaDatiSentenzeSige Exception: " + e);
		} finally {
			cleanup(lSenDao);
			cleanup(lFasDao);
			cleanup(lSogSqlDao);
			cleanup(lReaDao);
			cleanup(lMisDao);
			cleanup(lMisSicDao);
			cleanup(lPenaResDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasSenDao);
		}
		return lTreeFasSige;
	}

	/**
	 * Metodo che preleva i dati dei procedimenti per udienza.
	 *
	 * @param aValue
	 * @param aCodMagistrato
	 * @param aIdEsperto
	 * @param aStampaMod
	 * @param aOrderBy
	 * @param aStatoProcedimento
	 * @param aTipoProc
	 * @param aCodUfficioConnesso
	 * @param aConn
	 * @return
	 * @throws F3BException
	 */
	private TreeModel prelevaDatiProcedimentixUdienza(Object aValue, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampaMod, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso, Connection aConn)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("inizio");

		TreeModel lTreeRoot = null;
		TreeModel lTreeUdienza = null;
		TreeModel lTreeFasSIGE = null;
		TreeModel lTreeMagistrato = null;
		TreeModel lTreeSoggetto = null;
		TreeModel lTreeAvvocato = null;
		TreeModel lTreeProcUdienza = null;
		TreeModel lTreeLuogoDetenzione = null;

		Collection<GenericModel> lColl = null;

		lTreeRoot = new TreeModel(aStampaMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("Step[0] - executed!");

		ProcedimentixUdienzaSqlDAO lProxUdiDao = null;
		BigDecimal lIdUdienza = null;
		Date lDataUdienza = null;
		String listaIdUdienze = null;

		try {
			lProxUdiDao = new ProcedimentixUdienzaSqlDAO(aConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Step[1-0] - executed!");

			// Verifica che tipo di oggetto trattasi ed in virtù di esso
			// si invoca il metodo appropriato.
			if (aValue instanceof BigDecimal) { // bigdecimal
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[1-1] - executed!");
				lIdUdienza = (BigDecimal) aValue;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[1-2] - executed!");
				if (aIdFascicolo != null && aCodMagistrato == null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("Step[1-2-1] - executed!------ aIdFascicolo : " + aIdFascicolo);
					lProxUdiDao.ricercaProcedimentixUdienzaByUdienzaIdFascicolo(lIdUdienza, aIdFascicolo,
							aOrderBy, aStatoProcedimento, aTipoProc);
				} else {
					lProxUdiDao.ricercaProcedimentixUdienzaByUdienza(lIdUdienza, aCodMagistrato, aOrderBy,
							aStatoProcedimento, aTipoProc);
				}

			} else if (aValue instanceof Date) { // date
				lDataUdienza = (Date) aValue;
				lProxUdiDao.ricercaProcedimentixUdienzaByDataUdienza(lDataUdienza, aCodMagistrato, // Aggiunto...
						aOrderBy, aStatoProcedimento, aTipoProc, aCodUfficioConnesso);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[2] - executed!");
			} else if (aValue instanceof String) { // String
				// introdotto per 11.2.2
				// IL PARAMETRO STRING IN ARRIVO PUò CONTENTERE PIù ID-UDIENZE SEPARATE DA PUNTO E VIRGOLA
				listaIdUdienze = (String) aValue;
				if (!listaIdUdienze.equals("")) {
					String[] udi = listaIdUdienze.split(";");

					for (int i = 0; i < udi.length; i++) {
						String idUdiSingola = udi[i].trim();

						lProxUdiDao.ricercaProcedimentixUdienzaByUdienzaIdFascicolo(
								new BigDecimal(idUdiSingola), aIdFascicolo, aOrderBy, aStatoProcedimento,
								aTipoProc);

						if (lColl == null)
							lColl = lProxUdiDao.getModels();
						else
							lColl.addAll(lProxUdiDao.getModels());

					}

				}

			}

			// lProxUdiDao.start();

			ProcedimentixUdienzaModel lProcedimentoUdienza = null;
			if (aValue instanceof BigDecimal || aValue instanceof Date) {
				lColl = lProxUdiDao.getModels();
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Step[333 ] - size : " + lColl.size());

			Iterator lItxProcUdienza = lColl.iterator();
			Vector<ProcedimentixUdienzaModel> procUdi = new Vector<>();

			int i = 0; // Contatore del progressivo fascicolo

			// Iterazione su occorrenze provvedimento.
			while (lItxProcUdienza.hasNext()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[3xn--] - i=" + i);
				// lProcedimentoUdienza = (ProcedimentixUdienzaModel) lProxUdiDao.getModelEvento();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[3xn] - executed!");
				// lProcedimentixUdienza.add(lProcedimentoUdienza);

				lProcedimentoUdienza = (ProcedimentixUdienzaModel) lItxProcUdienza.next();
				procUdi.addElement(lProcedimentoUdienza);
				i++;

				// ProcedimentoxUdienza
				lTreeProcUdienza = new TreeModel(lProcedimentoUdienza);
				// Tenori
				for (int x = 0; x < lProcedimentoUdienza.getTenori().length; x++) {
					lTreeProcUdienza.add(new TreeModel(lProcedimentoUdienza.getTenori()[x]));
				}

				// intevento per SIES 11.2.1
				// Preleva dati dell'udienza
				if (lIdUdienza != null) {
					UdienzaSigeModel udienzaSige = prelevaDatiUdienza(lIdUdienza, aConn);
					if (lProcedimentoUdienza != null && i > 0) {
						ProcedimentixUdienzaModel model = procUdi.get(0);
						udienzaSige.setDescrGiudice(
								model.getCognomeMagistrato() + " " + model.getNomeMagistrato());
						if (model.getDescrProcuratore() != null)
							udienzaSige.setDescrProcuratore(model.getDescrProcuratore());
						if (model.getDescrIdAssistente() != null)
							udienzaSige.setDescrIdAssistente(model.getDescrIdAssistente());
					}
					lTreeUdienza = new TreeModel(udienzaSige);
					if (udienzaSige.getColIdCollegio() != null)
						lTreeUdienza.add(prelevaDatiCollegioById(udienzaSige.getColIdCollegio(), aConn));

				}
				if (lDataUdienza != null) {
					UdienzaSigeModel lUdi = new UdienzaSigeModel();
					// intevento per SIES 11.2.1
					// Preleva dati della prima udienza (i dati di interesse sono uguali per tutti i
					// procedimenti)
					if (lProcedimentoUdienza != null && i > 0) {
						ProcedimentixUdienzaModel model = procUdi.get(0);
						lUdi.setDescrGiudice(model.getCognomeMagistrato() + " " + model.getNomeMagistrato());
						if (model.getDescrProcuratore() != null)
							lUdi.setDescrProcuratore(model.getDescrProcuratore());
						if (model.getDescrIdAssistente() != null)
							lUdi.setDescrIdAssistente(model.getDescrIdAssistente());
					}
					lUdi.setDataUdienza(lDataUdienza);
					lTreeUdienza = new TreeModel(lUdi);
				}

				// Preleva fascisoloSIGE
				lTreeFasSIGE = new TreeModel(lProcedimentoUdienza.getFascicoloSige());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[4xn] - executed!");
				// Preleva i dati del soggetto
				lTreeSoggetto = new TreeModel(lProcedimentoUdienza.getSoggetto());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[5xn] - executed!");

				// Preleva dati del magistrato assegnatario o non ?
				lTreeMagistrato = new TreeModel(lProcedimentoUdienza.getMagistrato());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[6xn] - executed!");

				// 20180109 [EC] commento perchè gli avvocati possono essere più di uno (vedi modifica sotto
				// riportata)
				// Preleva dati dell'avvocato difensore
				// lTreeAvvocato = new TreeModel(lProcedimentoUdienza.getAvvocato());

				// 20180109 [EC] recupero gli avvocati difensori legati ad un fascicolo sige
				// metodo introdotto per problematica inviata tramite email Maffucci/Alfieri del 02/01/2018
				// (errore prototipo PALERMO)
				for (int x = 0; x < lProcedimentoUdienza.getAvvocati().length; x++)
					lTreeProcUdienza.add(new TreeModel(lProcedimentoUdienza.getAvvocati()[x]));

				// Preleva Luogo di detenzione
				lTreeLuogoDetenzione = prelevaDatiLuogoDetenzioneSige(lProcedimentoUdienza.getIdFasSIGE(),
						aConn);

				/*
				 * StampaSigeUtils lStaSigeUtils = new StampaSigeUtils(); lTreeFasSIGE =
				 * lStaSigeUtils.prelevaDatiAvvocatiSige(lTreeFasSIGE, aIdFasSige, lConn);
				 */
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[7xn] - executed!");

				// Composizione dell'albertaura del TreeModel
				lTreeFasSIGE.add(lTreeSoggetto);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[7xn] - executed!");
				lTreeFasSIGE.add(lTreeLuogoDetenzione);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[7bxn] - executed!");
				lTreeFasSIGE.add(lTreeMagistrato);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[8xn] - executed!");
				lTreeFasSIGE.add(lTreeAvvocato);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[9xn] - executed!");
				lTreeFasSIGE.add(lTreeProcUdienza);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[10xn] - executed!");
				lTreeUdienza.add(lTreeFasSIGE);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[11xn] - executed!");
				lTreeRoot.add(lTreeUdienza);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Step[12xn] - executed!");
			}

			lProxUdiDao.stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Step[13] - executed!");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.prelevaDatiProcedimentixUdienza : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIGEException("StampaSigeController.prelevaDatiProcedimentixUdienza : " + lEx);
		} finally {
			cleanup(lProxUdiDao);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("fine");

		return lTreeRoot;
	}

	/**
	 * Esegue la ricerca di NOTIZIA_REATO per Fascicolo SIGE.
	 *
	 * @param aKey
	 *            chiave id del Fascicolo SIGE.
	 * @return il vettore delle NOTIZIA_REATO.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private Vector getNotiziaReato(BigDecimal aIdFascicoloSige, Connection aConn) throws F3BException {

		NotiziaReatoSqlDAO lNRSqlDao = null;
		Vector lNotizieReato = new Vector();

		try {
			lNRSqlDao = new NotiziaReatoSqlDAO(aConn);
			lNRSqlDao.ricercaNotiziaReatoByIdFascicoloSigeOrder(aIdFascicoloSige);
			lNotizieReato = new Vector(lNRSqlDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("##### Dati prelevati nel metodo getNotiziaReato : " + lNotizieReato);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("DAOException: " + daoEx);
			throw new SIGEException("StampaSigeController.getNotiziaReato : " + daoEx);
		} catch (Exception lEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + lEx);
			throw new SIGEException("StampaSigeController.getNotiziaReato : " + lEx);
		} finally {
			cleanup(lNRSqlDao);
		}
		return lNotizieReato;
	}

	/**
	 * 24/05/2010 Esegue la ricerca di DATI_PROVVEDIMENTO_SIGE.
	 *
	 * @param aIdTenoreSige
	 *            chiave id del Tenore SIGE.
	 * @return il treeModel completo di DATI_PROVVEDIMENTO_SIGE.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	private TreeModel getDatiProvvedimentoSige(TreeModel lTreeTenore, BigDecimal aIdTenoreSige,
			Connection aConn) throws F3BException {

		DatiProvvedimentoSigeSqlDAO lDatDao = null;

		try {
			lDatDao = new DatiProvvedimentoSigeSqlDAO(aConn);
			lDatDao.ricercaDatiProvvedimentoSigeByIdTenore(aIdTenoreSige);
			Vector lDatiProvvedimentoSige = new Vector(lDatDao.getModels());

			if (lDatiProvvedimentoSige != null) {
				Iterator itx = lDatiProvvedimentoSige.iterator();
				while (itx.hasNext()) {
					DatiProvvedimentoSigeModel lMotProSige = (DatiProvvedimentoSigeModel) itx.next();
					lTreeTenore.add(new TreeModel(lMotProSige));
				}
			}
		} catch (DAOException daoEx) {
			throw new F3BException("StampaSigeController.getDatiProvvedimentoSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("StampaSigeController.getDatiProvvedimentoSige: " + e);
		} finally {
			cleanup(lDatDao);
		}
		return lTreeTenore;
	}

	/**
	 * Genera il ByteArrayOutputStream per la stampa dell'Elenco di Procedimenti SIGE per estremi Foglio
	 * Complementare.
	 *
	 * @param aFiltroRicerca
	 *            (RicercaFogliCompModel)
	 * @param aElenco
	 *            (Vector di EveFasGepSogProvModel),
	 * @param lUtenteModel
	 *            (Model di UtenteModel).
	 * @return ByteArrayOutputStream.
	 * @throws SIGEException
	 *             propaga l'errore di eccezione.
	 */
	public ByteArrayOutputStream ExPreStampaProcSigeXProv(RicercaFogliCompModel aFiltroRicerca,
			Vector aElenco, UtenteModel aUtente) throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		String lIdTemplate;

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(aUtente.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aFiltroRicerca));

			// Iterazione della lista di Notifiche
			Iterator lItx = aElenco.iterator();
			EveFasGepSogProvModel lProcCorr = null;

			while (lItx.hasNext()) {
				lProcCorr = (EveFasGepSogProvModel) lItx.next();

				TreeModel lTreeProcedimento = new TreeModel(lProcCorr);

				// ramo relativo all'evento
				if (lProcCorr.getEvento() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getEvento()));
				// ramo relativo al Fascicolo SIGE
				if (lProcCorr.getFascicoloSige() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getFascicoloSige()));
				// Ramo Provvedimento Sige
				if (lProcCorr.getProvvedimentoSige() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getProvvedimentoSige()));
				// DocumentoAllegato (Foglio Complementare)
				if (lProcCorr.getDocumentoAllegato() != null)
					lTreeProcedimento.add(new TreeModel(lProcCorr.getDocumentoAllegato()));

				// Viene aggiunto il ramo relativo al Soggetto
				if (lProcCorr.getSoggetto() != null)
					if (lProcCorr.getFascicoloSige() != null)
						lTreeProcedimento.add(prelevaDatiSoggetto(lProcCorr.getSoggetto().getIdSoggetto(),
								lProcCorr.getFascicoloSige().getIdFascicoloSige(), lConn));
					else
						lTreeProcedimento.add(new TreeModel(lProcCorr.getSoggetto()));

				lTreeRoot.add(lTreeProcedimento);
			}
		} catch (Exception lEx) {
			throw new SIGEException("StampaController.ExPreStampaProcSigeXProv : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());

		lIdTemplate = "";

		if (aFiltroRicerca.isRicercaXOrdinanza())
			lIdTemplate = "SIGE_ST_003"; // Elenco Ordinanze prive di Foglio Complementare
		else if (aFiltroRicerca.isRicercaXFoglioComplementare())
			lIdTemplate = "SIGE_ST_004"; // Elenco Fogli Complementari Redatti

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

		return lByteArrayOut;
	}

	@Override
	public ByteArrayOutputStream ExPreStampaStatisticheFC(
			StatisticheFogliComplementariContainerModel container) throws F3BException {

		Connection lConn = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream lByteArrayOut = null;

		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		UtenteModel aUtente = container.getUtenteConnesso();
		RicercaFogliCompModel aFiltroRicerca = container.getFiltro();

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(aUtente.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(aUtente));
			lTreeRoot.add(new TreeModel(aFiltroRicerca));
			Vector<StatisticheFogliComplementariModel> fogli = container.getElenco();

			for (StatisticheFogliComplementariModel foglio : fogli) {
				TreeModel lTreeFogli = new TreeModel(foglio);
				lTreeRoot.add(lTreeFogli);
			}
		} catch (Exception lEx) {
			throw new SIGEException("StampaController.ExPreStampaStatisticheFC : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(aUtente.getUfficioUtente().getCodUfficio());
		String lIdTemplate = "SIGE_ST_005"; // Elenco Fogli Complementari Redatti

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);
		return lByteArrayOut;
	}

	@Override
	public ByteArrayOutputStream ExStampaAttiInArchivio(EventoModel ev, FascicoloSigeEstesoModel fascicolo,
			UtenteModel utenteConnesso) throws F3BException {

		Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;
		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		FascicoloSigeModel fascicoloSige = fascicolo.getFascicoloSige();
		FasSigeDetenzioneModel lDetenzione = fascicolo.getDetenzione();
		SoggettoModel soggetto = fascicolo.getSoggetto();
		FascicoloSiepModel fascicoloSiep = fascicolo.getFascicoloSiep();

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(utenteConnesso.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(utenteConnesso));
			lTreeRoot.add(new TreeModel(soggetto));
			lTreeRoot.add(new TreeModel(fascicoloSige));
			lTreeRoot.add(new TreeModel(lDetenzione));
			lTreeRoot.add(new TreeModel(ev));
			lTreeRoot.add(new TreeModel(fascicoloSiep));
		} catch (Exception lEx) {
			throw new SIGEException("StampaController.ExStampaAttiInArchivio : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(utenteConnesso.getUfficioUtente().getCodUfficio());
		String lIdTemplate = "SIGE_AR_001";

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);
		return lByteArrayOut;
	}

	@Override
	public ByteArrayOutputStream ExStampaSollecito(EventoNotificaModel ev, FascicoloSigeEstesoModel fascicolo,
			UtenteModel utenteConnesso) throws F3BException {

		Connection lConn = null;
		ByteArrayOutputStream lByteArrayOut = null;
		TreeModel lTreeRoot = null; // radice dell'albero generale del documento
		FascicoloSigeModel fascicoloSige = fascicolo.getFascicoloSige();
		FasSigeDetenzioneModel lDetenzione = fascicolo.getDetenzione();
		SoggettoModel soggetto = fascicolo.getSoggetto();
		FascicoloSiepModel fascicoloSiep = fascicolo.getFascicoloSiep();

		try {
			lConn = getDBConnection(); // connessione al Db

			// Intestazione dell'Ufficio documento
			XModel lBase = CreateRoot(utenteConnesso.getUfficioUtente().getCodUfficio(), lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + lBase);

			// Intestazione del documento XML
			lTreeRoot = new TreeModel(lBase);
			lTreeRoot.add(new TreeModel(utenteConnesso));
			lTreeRoot.add(new TreeModel(soggetto));
			lTreeRoot.add(new TreeModel(fascicoloSige));
			lTreeRoot.add(new TreeModel(lDetenzione));
			lTreeRoot.add(new TreeModel(ev));
			lTreeRoot.add(new TreeModel(fascicoloSiep));
		} catch (Exception lEx) {
			throw new SIGEException("StampaController.ExStampaAttiInArchivio : " + lEx);
		} finally {
			cleanup(lConn);
		}

		// Viene istanziato il Report Generator
		ReportGenerator lReport = new ReportGenerator(utenteConnesso.getUfficioUtente().getCodUfficio());
		String lIdTemplate = "SIGE_IS_021"; // Solleciti

		// Si ricava il Nome del template
		String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lIdTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);
		return lByteArrayOut;
	}

	private Vector<TenoreSigeEstesoModel> cleanTenori(Vector<TenoreSigeEstesoModel> toClean) {

		Vector<TenoreSigeEstesoModel> cleaned = new Vector<>();
		HashMap<String, TenoreSigeEstesoModel> hashTenori = new HashMap<>();
		for (TenoreSigeEstesoModel tenore : toClean) {
			String codOggetto = tenore.getTenoreSige().getCodOggettoSige();
			if (hashTenori.get(codOggetto) == null) {
				cleaned.add(tenore);
				hashTenori.put(codOggetto, tenore);
			}
		}
		return cleaned;
	}

	private List<ParteCivileUdienzaModel> prelevaPartiCivili(UdienzaProcedimentoSigeModel lUdiProSige)
			throws F3BException {

		IPartiUdienza ctrParti = SIGELookupRemote.getPartiUdienzaRemote();
		return ctrParti.ExRicercaPartiCiviliUdienzaByIdUdienza(lUdiProSige.getIdUdienzaProcedimentoSige());
	}

	private List<ParteOffesaUdienzaModel> prelevaPartiOffese(UdienzaProcedimentoSigeModel lUdiProSige)
			throws F3BException {

		IPartiUdienza ctrParti = SIGELookupRemote.getPartiUdienzaRemote();
		return ctrParti.ExRicercaPartiOffesaUdienzaByIdUdienza(lUdiProSige.getIdUdienzaProcedimentoSige());
	}

	private List<NotificaParteCivileModel> prelevaNotifichePartiCivili(ParteCivileUdienzaModel parteCivile) {

		List<NotificaParteCivileModel> notifiche = new ArrayList<>();
		// List<PartiUdienzaDifensoreModel> difensori = parteCivile.getDifensori();

		if (!"S".equalsIgnoreCase(parteCivile.getResidenza().getFlgDomicilioDifensore())) {
			NotificaParteCivileModel notificaSoggetto = new NotificaParteCivileModel();
			notificaSoggetto.setAvvocato(false);
			notificaSoggetto.setNominativo(parteCivile.getNome() + " " + parteCivile.getCognome());
			notificaSoggetto.setIndirizzoNotifica(parteCivile.getResidenza().toStringaResidenza());
			notifiche.add(notificaSoggetto);
		}

		// for (PartiUdienzaDifensoreModel difensore : difensori) {
		// NotificaParteCivileModel notificaAvvocato= new NotificaParteCivileModel ();
		// notificaAvvocato.setAvvocato(true);
		// String
		// indirizzoNotifica=difensore.getAvvocato().getDescComuneResidenza()+"
		// "+difensore.getAvvocato().getIndirizzo();
		// notificaAvvocato.setIndirizzoNotifica(indirizzoNotifica);
		// notificaAvvocato.setNominativo(difensore.getNomeCognomeAvvocato());
		// notifiche.add(notificaAvvocato);
		// }
		return notifiche;
	}

	private List<NotificaParteOffesaModel> prelevaNotifichePartiOffese(ParteOffesaUdienzaModel parteOffesa) {

		List<NotificaParteOffesaModel> notifiche = new ArrayList<>();
		// List<PartiUdienzaDifensoreModel> difensori = parteOffesa.getDifensori();

		if (!"S".equalsIgnoreCase(parteOffesa.getResidenza().getFlgDomicilioDifensore())) {
			NotificaParteOffesaModel notificaSoggetto = new NotificaParteOffesaModel();
			notificaSoggetto.setAvvocato(false);
			notificaSoggetto.setNominativo(parteOffesa.getNome() + " " + parteOffesa.getCognome());
			notificaSoggetto.setIndirizzoNotifica(parteOffesa.getResidenza().toStringaResidenza());
			notifiche.add(notificaSoggetto);
		}

		// for (PartiUdienzaDifensoreModel difensore : difensori) {
		// NotificaParteOffesaModel notificaAvvocato= new NotificaParteOffesaModel ();
		// notificaAvvocato.setAvvocato(true);
		// NotificaModel notifica=difensore.getNotifica();
		// AutoritaEsternaModel autorita= notifica.getAutoritaEsterna();
		// notificaAvvocato.setNominativo(difensore.getNomeCognomeAvvocato());
		// String
		// indirizzoNotifica=difensore.getAvvocato().getDescComuneResidenza()+"
		// "+difensore.getAvvocato().getIndirizzo();
		// notificaAvvocato.setIndirizzoNotifica(indirizzoNotifica);
		// notifiche.add(notificaAvvocato);
		// }
		return notifiche;
	}

	/**
	 * MEV_65: aggiunto metodo di stampa
	 */
	public ByteArrayOutputStream ExStampaProcedimentiSigeConRicorsoOpposizione(
			Vector<FascicoloSigeEstesoModel> fascicoli, RicercaFascicoloSigeModel rfsm, UtenteModel um)
			throws F3BException {

		// Connessione al DB per il prelievo dei dati.
		Connection c = null;

		// ArrayOutput restituito dalla funzione
		ByteArrayOutputStream baos = null;

		// radice dell'albero generale del documento
		TreeModel tm = null;

		try {
			// connessione al Db
			c = getDBConnection();

			// Intestazione dell'Ufficio documento
			XModel xm = CreateRoot(um.getUfficioUtente().getCodUfficio(), c);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("root base " + xm);

			// Intestazione del documento XML
			tm = new TreeModel(xm);
			tm.add(new TreeModel(um));
			tm.add(new TreeModel(rfsm));

			// Iterazione della lista di Notifiche
			Iterator f = fascicoli.iterator();
			FascicoloSigeEstesoModel fsem = null;

			while (f.hasNext()) {
				fsem = (FascicoloSigeEstesoModel) f.next();
				TreeModel tmIter = new TreeModel(fsem);

				// ramo relativo all'evento
				if (fsem.getProvvedimentoEventoSige() != null
						&& fsem.getProvvedimentoEventoSige().getEventoNotifica() != null
						&& fsem.getProvvedimentoEventoSige().getEventoNotifica().getEvento() != null)
					tmIter.add(
							new TreeModel(fsem.getProvvedimentoEventoSige().getEventoNotifica().getEvento()));
				// ramo relativo al tenore
				if (fsem.getProvvedimentoEventoSige() != null
						&& fsem.getProvvedimentoEventoSige().getTenoriEstesi() != null
						&& !fsem.getProvvedimentoEventoSige().getTenoriEstesi().isEmpty()) {
					TenoreSigeEstesoModel tsem = (TenoreSigeEstesoModel) fsem.getProvvedimentoEventoSige()
							.getTenoriEstesi().get(0);
					TenoreSigeModel tsm = new TenoreSigeModel();
					tsm.setDescrOggettoSige(tsem.getDescrOggettoSige());
					tsm.setDescrEsitoSige(tsem.getDescrEsitoSige());
					tmIter.add(new TreeModel(tsm));
				}
				// ramo relativo al Fascicolo SIGE
				if (fsem.getFascicoloSige() != null)
					tmIter.add(new TreeModel(fsem.getFascicoloSige()));
				// Impugnazione
				if (fsem.getImpugnazioneSige() != null)
					tmIter.add(new TreeModel(fsem.getImpugnazioneSige()));

				// Viene aggiunto il ramo relativo al Soggetto
				if (fsem.getSoggetto() != null)
					if (fsem.getFascicoloSige() != null)
						tmIter.add(prelevaDatiSoggetto(fsem.getSoggetto().getIdSoggetto(),
								fsem.getFascicoloSige().getIdFascicoloSige(), c));
					else
						tmIter.add(new TreeModel(fsem.getSoggetto()));

				tm.add(tmIter);
			}
		} catch (Exception lEx) {
			throw new SAMLRuntimeException(
					"StampaController.ExStampaProcedimentiSigeConRicorsoOpposizione : " + lEx);
		} finally {
			cleanup(c);
		}

		// Viene istanziato il Report Generator
		ReportGenerator rg = new ReportGenerator(um.getUfficioUtente().getCodUfficio());

		// Si ricava il Nome del template
		String nomeTemplate = TemplateManager.getInstance().getTemplateName("SIGE_ST_007");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("NOME TEMPLATE >>>" + nomeTemplate);

		baos = (ByteArrayOutputStream) rg.generateDocument(tm, nomeTemplate);

		return baos;
	}

	/*
	 * (non-Javadoc) metodo introdotto per 11.2.1
	 *
	 * @see siap.sige.stampa.controller.IStampaSige#ExPrelevaDatiStampaProcedimentixUdienza(java.lang.String,
	 * java.math.BigDecimal, java.lang.String, java.math.BigDecimal, siap.sico.evento.model.XModel,
	 * java.lang.String, java.lang.String, siap.sico.utente.model.UtenteModel, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public TreeModel ExPrelevaDatiStampaProcedimentixUdienza(String idUdienze, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String aOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("inizio");

		Connection lConn = null;
		TreeModel lTreeDati = null;

		try {
			lConn = getDBConnection();
			lTreeDati = new TreeModel(CreateRoot(aCodUfficioConnesso, lConn));

			lTreeDati = prelevaDatiProcedimentixUdienza(idUdienze, aIdFascicolo, aCodMagistrato, aIdEsperto,
					aStampa, aOrderBy, aStatoProcedimento, aTipoProc, aCodUfficioConnesso, lConn);
		} catch (F3BException e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception: " + e);
			throw e;
		} finally {
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("fine");
		return lTreeDati;
	}

}