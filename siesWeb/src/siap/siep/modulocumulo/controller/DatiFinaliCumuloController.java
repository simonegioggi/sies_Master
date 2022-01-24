package siap.siep.modulocumulo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.jms.ICostantiJMS;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.dao.ComuneDAO;
import siap.sico.decodifiche.dao.DecodificheDAO;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.dao.EventoStoreProcedurePulisciDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.StampaEventoUtils;
import siap.sico.template.controller.TemplateManager;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepDAO;
import siap.siep.agdgfascicolosiep.dao.AgdgFascicoloSiepSqlDAO;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneDAO;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.dao.ArchiviazioneDAO;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepSqlDAO;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IStampaCumulo;
import siap.siep.istruttoriacumulo.dao.EsitoArchiviazioniCumuloDAO;
import siap.siep.istruttoriacumulo.dao.IstruttoriaCumuloDAO;
import siap.siep.istruttoriacumulo.dao.IstruttoriaCumuloSqlDAO;
import siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepDAO;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.modulocumulo.action.ICostantiDatiFinaliUlterioriSanzioni;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.dao.DatiFinaliCumuloDAO;
import siap.siep.modulocumulo.dao.DatiFinaliCumuloSqlDAO;
import siap.siep.modulocumulo.dao.DatiFinaliUlterioriSanzioniDAO;
import siap.siep.modulocumulo.dao.DatiFinaliUlterioriSanzioniSqlDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaRideterminataCumuloDAO;
import siap.siep.modulocumulo.dao.PenaRideterminataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.NotaDiTrasmissioneModel;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepDAO;
import siap.siep.sentenzariunita.dao.SentenzaRiunitaFascSiepSqlDAO;
import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.siep.tipologiaorario.dao.TipologiaOrarioSqlDAO;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.dao.RiferimentoFascicoloSiepDAO;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;

/**
 * <p>
 * Title: DatiFinaliCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per DatiFinaliCumulo
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DatiFinaliCumuloController extends SiapController implements IDatiFinaliCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Effettua l'inserimento di un DatiFinaliCumulo a partire dai dati contenuti nel Model
	 *
	 * @param aDatiFinaliCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	public DatiFinaliCumuloModel ExInserisciDatiFinaliCumulo(DatiFinaliCumuloModel aDatiFinaliCumulo)
			throws F3BException {

		Connection lConn = null;
		DatiFinaliCumuloDAO lDatDao = null;
		DatiFinaliCumuloModel lDatMod = null;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiFinaliCumuloDAO(lConn);
			lDatDao.setDAOFromModel(aDatiFinaliCumulo);
			BigDecimal lSequence = lDatDao.insert();
			commit(lConn);
			lDatMod = new DatiFinaliCumuloModel(aDatiFinaliCumulo);
			lDatMod.setMessage("Inserimento avvenuto correttamente!");
			lDatMod.setIdDatiFinaliCumulo(lSequence);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException("DatiFinaliCumuloController.ExInserisci: Non posso inserire: " + daoEx);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}

		return lDatMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati DatiFinaliCumulo
	 *
	 * @param aDatiFinaliCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaDatiFinaliCumulo(DatiFinaliCumuloModel aDatiFinaliCumulo) throws F3BException {

		Connection lConn = null;
		Vector lDatiFinaliCumuli = new Vector();
		DatiFinaliCumuloDAO lDatDao = null;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiFinaliCumuloDAO(lConn);
			lDatDao.setCondizioni(aDatiFinaliCumulo);
			lDatDao.setOrderBy();
			lDatDao.start();
			while (lDatDao.next()) {
				lDatiFinaliCumuli.add(lDatDao.getModel());
			}
			lDatDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExRicercaDatiFinaliCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}

		return lDatiFinaliCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public DatiFinaliCumuloModel ExRicercaDatiFinaliCumuloById(BigDecimal aIdDatiFinaliCumulo)
			throws F3BException {

		Connection lConn = null;
		DatiFinaliCumuloModel lDatiFinaliCumuloMod = new DatiFinaliCumuloModel();
		DatiFinaliCumuloSqlDAO lDatiFinaliCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lDatiFinaliCumuloSqlDao = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliCumuloSqlDao.ricercaDatiFinaliCumuloByKey(aIdDatiFinaliCumulo);
			lDatiFinaliCumuloMod = (DatiFinaliCumuloModel) lDatiFinaliCumuloSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExRicercaDatiFinaliCumuloById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDatiFinaliCumuloSqlDao);
			cleanup(lConn);
		}

		return lDatiFinaliCumuloMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'DatiFinaliCumulo Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aDatiFinaliCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaDatiFinaliCumulo(DatiFinaliCumuloModel aDatiFinaliCumulo) throws F3BException {

		Connection lConn = null;
		DatiFinaliCumuloDAO lDatDao = null;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiFinaliCumuloDAO(lConn);
			lDatDao.setDAOFromModelForUpdate(aDatiFinaliCumulo);
			lDatDao.selCondizioneUpdate(aDatiFinaliCumulo.getIdDatiFinaliCumulo());
			lDatDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExModificaDatiFinaliCumulo: Non posso inserire: " + daoEx);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aDatiFinaliCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaDatiFinaliCumulo(DatiFinaliCumuloModel aDatiFinaliCumulo) throws F3BException {

		Connection lConn = null;
		DatiFinaliCumuloDAO lDatDao = null;

		try {
			lConn = getDBConnection();
			lDatDao = new DatiFinaliCumuloDAO(lConn);
			lDatDao.selCondizioneUpdate(aDatiFinaliCumulo.getIdDatiFinaliCumulo());
			lDatDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			rollback(lConn);
			throw new F3BException(
					"DatiFinaliCumuloController.ExCancellaDatiFinaliCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDatDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aDatiFinaliCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountDatiFinaliCumulo(DatiFinaliCumuloModel aDatiFinaliCumulo)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		DatiFinaliCumuloSqlDAO lDatiFinaliCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lDatiFinaliCumuloSqlDao = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliCumuloSqlDao.getCountDatiFinaliCumulo(aDatiFinaliCumulo);
			lDatiFinaliCumuloSqlDao.start();
			lDatiFinaliCumuloSqlDao.next();
			lCount = lDatiFinaliCumuloSqlDao.getBigDecimal("HowManyRecords");
			lDatiFinaliCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExGetCountDatiFinaliCumulo: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lDatiFinaliCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/**
	*
	*/
	public DatiFinaliCumuloModel ExRicercaDatiFinaliCumuloByIdIstrutt(BigDecimal aIdIstruttoria)
			throws F3BException {

		Connection lConn = null;
		DatiFinaliCumuloModel lDatiFinaliCumuloMod = null;
		DatiFinaliCumuloSqlDAO lDatiFinaliCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lDatiFinaliCumuloSqlDao = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliCumuloSqlDao.ricercaDatiFinaliCumuloByIdIstruttoria(aIdIstruttoria);
			// MEV 16 CUMULO: refactor
			// lDatiFinaliCumuloSqlDao.start();
			// if (lDatiFinaliCumuloSqlDao.next())
			lDatiFinaliCumuloMod = (DatiFinaliCumuloModel) lDatiFinaliCumuloSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExRicercaDatiFinaliCumuloByIdIstrutt: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDatiFinaliCumuloSqlDao);
			cleanup(lConn);
		}

		return lDatiFinaliCumuloMod;
	}

	/**
	 * Metodo che recupera tutti i dati finali cumulo presenti in istruttoria
	 */
	public DatiFinaliCumuloAggregatoModel ExRicercaDatiFinaliAggregatiByIdIstruttoria(
			BigDecimal aIdIstruttoria) throws F3BException {

		Connection lConn = null;

		DatiFinaliCumuloAggregatoModel lDatiFinaliAggregatoMod = new DatiFinaliCumuloAggregatoModel();

		DatiFinaliCumuloModel lDatiFinaliCumuloMod = null;
		DatiFinaliCumuloSqlDAO lDatiFinaliCumuloSqlDao = null;

		PenaRideterminataCumuloSqlDAO lPenaRidetCumuloModSqlDao = null;

		DatiFinaliUlterioriSanzioniSqlDAO lUlterioriSanzioniSqlDao = null;
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;

		PosizioneGiuridicaCumuloSqlDAO lPosGiuCumuloSqlDao = null;

		PenaResiduaSqlDAO lPenaResiduaSqlDao = null;

		try {
			lConn = getDBConnection();

			// Recuperao dati finali cumulo + posizione giuridica
			lDatiFinaliCumuloSqlDao = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliCumuloSqlDao.ricercaDatiFinaliCumuloByIdIstruttoria(aIdIstruttoria);
			// MEV 16 CUMULO: refactor
			// lDatiFinaliCumuloSqlDao.start();
			// if (lDatiFinaliCumuloSqlDao.next())
			lDatiFinaliCumuloMod = (DatiFinaliCumuloModel) lDatiFinaliCumuloSqlDao.getModelByKey();
			lDatiFinaliAggregatoMod.setDatiFinaliCumulo(lDatiFinaliCumuloMod);

			if (lDatiFinaliCumuloMod != null && lDatiFinaliCumuloMod.getIdDatiFinaliCumulo() != null) {
				// Recupero Pene Rideterminate
				lPenaRidetCumuloModSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
				lPenaRidetCumuloModSqlDao.ricercaPenaRideterminataCumulByIdDatiFinali(
						lDatiFinaliCumuloMod.getIdDatiFinaliCumulo());
				// MEV 16 CUMULO: refactor
				// lPenaRidetCumuloModSqlDao.start();
				// if (lPenaRidetCumuloModSqlDao.next())
				PenaRideterminataCumuloModel lPenaRidetCumuloMod = (PenaRideterminataCumuloModel) lPenaRidetCumuloModSqlDao
						.getModelByKey();
				lDatiFinaliAggregatoMod.setPenaRideterminataCumulo(lPenaRidetCumuloMod);
				// lPenaRidetCumuloModSqlDao.stop();

				// Recupero Ulteriori Sanzioni
				lUlterioriSanzioniSqlDao = new DatiFinaliUlterioriSanzioniSqlDAO(lConn);
				lUlterioriSanzioniSqlDao.ricercaDatiFinaliUlterioriSanzioniByIdDatiFinali(
						lDatiFinaliCumuloMod.getIdDatiFinaliCumulo());
				Vector<DatiFinaliUlterioriSanzioniModel> lListaUlterioriSanzioni = new Vector<DatiFinaliUlterioriSanzioniModel>(
						lUlterioriSanzioniSqlDao.getModels());
				lDatiFinaliAggregatoMod.setListaDatiFinaliUlterioriSanzioni(lListaUlterioriSanzioni);

				if (lListaUlterioriSanzioni != null) {
					Iterator<DatiFinaliUlterioriSanzioniModel> lUlterioriSanzioniIter = lListaUlterioriSanzioni
							.iterator();
					while (lUlterioriSanzioniIter.hasNext()) {
						DatiFinaliUlterioriSanzioniModel lUltSanzModel = lUlterioriSanzioniIter.next();
						if (lUltSanzModel.getCodTipoUlterioreSanzione()
								.equals(ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS)) {
							lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
							lTipologiaOrarioSqlDao.ricercaTipologiaOrarioByIdUltSanz(
									lUltSanzModel.getIdDatiFinaliUlterioriSanz());

							Vector<TipologiaOrarioModel> lListaOrari = new Vector<TipologiaOrarioModel>(
									lTipologiaOrarioSqlDao.getModels());
							lUltSanzModel.setListaOrariLPU(lListaOrari);
						}
					}
				}

				// Recupero se presente la Posizione giuridica
				lPosGiuCumuloSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
				lPosGiuCumuloSqlDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria(aIdIstruttoria);
				// MEV 16 CUMULO: refactor
				// lPosGiuCumuloSqlDao.start();
				// if (lPosGiuCumuloSqlDao.next())
				PosizioneGiuridicaCumuloModel lPosGiuCumModel = (PosizioneGiuridicaCumuloModel) lPosGiuCumuloSqlDao
						.getModelByKey();
				lDatiFinaliAggregatoMod.setPosizioneGiuridicaCumulo(lPosGiuCumModel);

				// Recupero se presente la pena Residua
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ricerco la pena residua se presente");
				// lPenaResiduaSqlDao = new PenaResiduaSqlDAO (lConn);
				// lPenaResiduaSqlDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria (aIdIstruttoria);
				// PenaResiduaModel lPenaResiduaModel = (PenaResiduaModel) lPenaResiduaSqlDao.getModelByKey();
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("lPenaResiduaModel ="+lPenaResiduaModel);
				// lDatiFinaliAggregatoMod.setPenaResidua (lPenaResiduaModel);
				lPenaRidetCumuloModSqlDao
						.ricercaPenaResiduaCumulByIdDatiFinali(lDatiFinaliCumuloMod.getIdDatiFinaliCumulo());
				// MEV 16 CUMULO: refactor
				// lPenaRidetCumuloModSqlDao.start();
				// if (lPenaRidetCumuloModSqlDao.next())
				PenaRideterminataCumuloModel lPenaResiduaCumuloMod = (PenaRideterminataCumuloModel) lPenaRidetCumuloModSqlDao
						.getModelByKey();
				lDatiFinaliAggregatoMod.setPenaResiduaCumulo(lPenaResiduaCumuloMod);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExRicercaDatiFinaliAggregatiByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lDatiFinaliCumuloSqlDao);
			cleanup(lPenaRidetCumuloModSqlDao);
			cleanup(lUlterioriSanzioniSqlDao);
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lPosGiuCumuloSqlDao);
			cleanup(lPenaResiduaSqlDao);

			cleanup(lConn);
		}

		return lDatiFinaliAggregatoMod;
	}

	/**
	*
	*/
	public PenaRideterminataCumuloModel ExInserisciPenaRideterminataCumulo(
			PenaRideterminataCumuloModel aPenaRideterminataCumulo) throws F3BException {

		Connection lConn = null;
		PenaRideterminataCumuloDAO lPenDao = null;
		PenaRideterminataCumuloModel lPenMod = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaRideterminataCumuloDAO(lConn);
			lPenDao.setDAOFromModel(aPenaRideterminataCumulo);
			BigDecimal lSequence = lPenDao.insert();
			commit(lConn);
			lPenMod = new PenaRideterminataCumuloModel(aPenaRideterminataCumulo);
			lPenMod.setMessage("Inserimento avvenuto correttamente!");
			lPenMod.setIdPenaRideterminataCumulo(lSequence);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExInserisciPenaRideterminataCumulo: Non posso inserire: "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	/**
	*
	*/
	public void ExModificaPenaRideterminataCumulo(PenaRideterminataCumuloModel aPenaRideterminataCumulo)
			throws F3BException {

		Connection lConn = null;
		PenaRideterminataCumuloDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaRideterminataCumuloDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaRideterminataCumulo);
			lPenDao.selCondizioneUpdate(aPenaRideterminataCumulo.getIdPenaRideterminataCumulo());
			lPenDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExModificaPenaRideterminataCumulo: Non posso inserire: "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	*
	*/
	public void ExCancellaPenaRideterminataCumulo(PenaRideterminataCumuloModel aPenaRideterminataCumulo)
			throws F3BException {

		Connection lConn = null;
		PenaRideterminataCumuloDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaRideterminataCumuloDAO(lConn);
			lPenDao.selCondizioneUpdate(aPenaRideterminataCumulo.getIdPenaRideterminataCumulo());
			lPenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			rollback(lConn);
			throw new F3BException(
					"DatiFinaliCumuloController.ExCancellaPenaRideterminataCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo che effettua l'aggiornamento (Insert, Update, Delete) dei dati di Pena Rideterminata e Ulteriori
	 * Sanzioni
	 */
	public void ExCRUDPeneRideterminateUlterioriSanzioniCumulo(
			DatiFinaliCumuloAggregatoModel aDatiFInaliAggregato) throws F3BException {

		Connection lConn = null;

		PenaRideterminataCumuloDAO lPenDao = null;
		DatiFinaliUlterioriSanzioniDAO lUlterioriDao = null;
		TipologiaOrarioDAO lTipologiaOrarioDao = null;

		PenaRideterminataCumuloModel lPenaRideterminataCumulo = aDatiFInaliAggregato
				.getPenaRideterminataCumulo();
		Vector<DatiFinaliUlterioriSanzioniModel> lListaUlterioriSanzioni = aDatiFInaliAggregato
				.getListaDatiFinaliUlterioriSanzioni();

		PenaRideterminataCumuloSqlDAO lPenSqlDao = null;

		try {
			lConn = getDBConnection();

			// =====================
			// Pena Rideterminata
			// =====================
			if (lPenaRideterminataCumulo != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Gestione Pena Rideterminata");
				lPenDao = new PenaRideterminataCumuloDAO(lConn);

				if (lPenaRideterminataCumulo.getTipoOperazioneCRUD().equals("I")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Gestione Pena Rideterminata - Insert");
					lPenDao.setDAOFromModel(lPenaRideterminataCumulo);
					BigDecimal lSequence = lPenDao.insert();
					lPenaRideterminataCumulo.setIdPenaRideterminataCumulo(lSequence);
				} else if (lPenaRideterminataCumulo.getTipoOperazioneCRUD().equals("U")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Gestione Pena Rideterminata - Update - id = "
							+ lPenaRideterminataCumulo.getIdPenaRideterminataCumulo());
					lPenDao.setDAOFromModelForUpdate(lPenaRideterminataCumulo);
					lPenDao.selCondizioneUpdate(lPenaRideterminataCumulo.getIdPenaRideterminataCumulo());
					lPenDao.update();
				} else if (lPenaRideterminataCumulo.getTipoOperazioneCRUD().equals("D")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Gestione Pena Rideterminata - Delete - id = "
							+ lPenaRideterminataCumulo.getIdPenaRideterminataCumulo());
					lPenDao.selCondizioneUpdate(lPenaRideterminataCumulo.getIdPenaRideterminataCumulo());
					lPenDao.delete();
				}
				lPenDao.stop();

				// Verifico se aggiornare
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Verifico se aggiornare la pena: " + lPenaRideterminataCumulo);
				lPenSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
				lPenSqlDao.ricercaPenaResiduaCumulByIdDatiFinali(
						lPenaRideterminataCumulo.getDatIdDatiFinaliCumulo());
				PenaRideterminataCumuloModel lPenaResidua = (PenaRideterminataCumuloModel) lPenSqlDao
						.getModelByKey();
				if (lPenaResidua != null && lPenaResidua.getIdPenaRideterminataCumulo() != null
						&& "S".equals(lPenaRideterminataCumulo.getAggiornaFlagRicalcoloPR())) {
					lPenDao.setIsPenaDaRicalcolare("S");
					lPenDao.selCondizioneUpdate(lPenaResidua.getIdPenaRideterminataCumulo());
					lPenDao.update();
					lPenDao.stop();
				}
			}

			// =================================
			// Scorro le Ulteriori Sanzioni
			// =================================
			if (lListaUlterioriSanzioni != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Gestione Ulteriori Sanzioni");

				lUlterioriDao = new DatiFinaliUlterioriSanzioniDAO(lConn);
				lTipologiaOrarioDao = new TipologiaOrarioDAO(lConn);

				for (int i = 0; i < lListaUlterioriSanzioni.size(); i++) {
					DatiFinaliUlterioriSanzioniModel lUlterSanz = lListaUlterioriSanzioni.elementAt(i);
					boolean isLPU = false;

					if (lUlterSanz.getCodTipoUlterioreSanzione()
							.equals(ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS))
						isLPU = true;

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lUlterSanz = " + lUlterSanz);

					if (lUlterSanz.getTipoOperazioneCRUD().equals("I")) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Gestione Ulteriori Sanzioni - Insert "
								+ lUlterSanz.getCodTipoUlterioreSanzione());
						lUlterioriDao.setDAOFromModel(lUlterSanz);
						BigDecimal lSequence = lUlterioriDao.insert();
						lUlterSanz.setIdDatiFinaliUlterioriSanz(lSequence);

						if (isLPU && lUlterSanz.getListaOrariLPU() != null
								&& lUlterSanz.getListaOrariLPU().size() > 0) {
							Iterator<TipologiaOrarioModel> lOrariIter = lUlterSanz.getListaOrariLPU()
									.iterator();
							while (lOrariIter.hasNext()) {
								TipologiaOrarioModel lOrarioModel = lOrariIter.next();

								lOrarioModel.setDatFinCumUltSanzioni(lSequence);

								lTipologiaOrarioDao.setDAOFromModel(lOrarioModel);
								lTipologiaOrarioDao.insert();
								lTipologiaOrarioDao.stop();
							}
						}
					} else if (lUlterSanz.getTipoOperazioneCRUD().equals("U")) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Gestione Ulteriori Sanzioni - Update - "
								+ lUlterSanz.getCodTipoUlterioreSanzione() + " - id = "
								+ lUlterSanz.getIdDatiFinaliUlterioriSanz());
						lUlterioriDao.setDAOFromModelForUpdate(lUlterSanz);
						lUlterioriDao.selCondizioneUpdate(lUlterSanz.getIdDatiFinaliUlterioriSanz());
						lUlterioriDao.update();

						if (isLPU) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug("Tipologia Orario: vado in delete");
							lTipologiaOrarioDao.selCondizioneDeleteByIdDatFinUltSanz(
									lUlterSanz.getIdDatiFinaliUlterioriSanz());
							lTipologiaOrarioDao.delete();

							if (lUlterSanz.getListaOrariLPU() != null
									&& lUlterSanz.getListaOrariLPU().size() > 0) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Tipologia Orario: vado in insert");

								Iterator<TipologiaOrarioModel> lOrariIter = lUlterSanz.getListaOrariLPU()
										.iterator();
								while (lOrariIter.hasNext()) {
									TipologiaOrarioModel lOrarioModel = lOrariIter.next();

									lOrarioModel.setDatFinCumUltSanzioni(
											lUlterSanz.getIdDatiFinaliUlterioriSanz());

									lTipologiaOrarioDao.setDAOFromModel(lOrarioModel);
									lTipologiaOrarioDao.insert();
									lTipologiaOrarioDao.stop();
								}
							}
						}
					} else if (lUlterSanz.getTipoOperazioneCRUD().equals("D")) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Gestione Ulteriori Sanzioni - Delete - "
								+ lUlterSanz.getCodTipoUlterioreSanzione() + " - id = "
								+ lUlterSanz.getIdDatiFinaliUlterioriSanz());

						if (isLPU) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.debug(
									"Sono in cancellazione LPU: cancello prima le eventuali Tipologie Orari collegate ");
							lTipologiaOrarioDao.selCondizioneDeleteByIdDatFinUltSanz(
									lUlterSanz.getIdDatiFinaliUlterioriSanz());
							lTipologiaOrarioDao.delete();
						}

						lUlterioriDao.selCondizioneUpdate(lUlterSanz.getIdDatiFinaliUlterioriSanz());
						lUlterioriDao.delete();

					}
					lUlterioriDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx.getMessage(), daoEx);
			rollback(lConn);
			throw new F3BException(
					"DatiFinaliCumuloController.ExCRUDPeneRideterminateUlterioriSanzioniCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lPenSqlDao);
			cleanup(lUlterioriDao);
			cleanup(lTipologiaOrarioDao);

			cleanup(lConn);
		}
	}

	/**
	*
	*/
	public PosizioneGiuridicaCumuloModel ExInserisciPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException {

		Connection lConn = null;
		PosizioneGiuridicaCumuloDAO lPosDao = null;
		PosizioneGiuridicaCumuloModel lPosMod = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaCumuloDAO(lConn);
			lPosDao.setDAOFromModel(aPosizioneGiuridicaCumulo);
			BigDecimal lSequence = lPosDao.insert();
			commit(lConn);

			lPosMod = new PosizioneGiuridicaCumuloModel(aPosizioneGiuridicaCumulo);
			lPosMod.setMessage("Inserimento avvenuto correttamente!");
			lPosMod.setIdPosizioneGiuridicaCum(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("DatiFinaliCumuloController.ExInserisci: Non posso inserire: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			siesLogger.error("Exception: ", e);
			throw new F3BException("DatiFinaliCumuloController.ExInserisci: Non posso inserire: " + e);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	*
	*/
	public void ExModificaPosizioneGiuridicaCumulo(PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo)
			throws F3BException {

		Connection lConn = null;
		PosizioneGiuridicaCumuloDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaCumuloDAO(lConn);
			lPosDao.setDAOFromModelForUpdate(aPosizioneGiuridicaCumulo);

			lPosDao.selCondizioneUpdate(aPosizioneGiuridicaCumulo.getIdPosizioneGiuridicaCum());
			lPosDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.ExModificaPosizioneGiuridicaCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
	}

	/**
	 *
	 * @param aEveNotModel
	 * @param aCompetenzaModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel ExInserisciProvvedimentoCumulo(EventoNotificaModel aEveNotModel,
			BigDecimal aIdDatiFinali) throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEveNot = null;
		DatiFinaliCumuloDAO lDatiFinaliDAO = null;

		try {
			lConn = getDBConnection();

			// Inserimento Evento e Notifiche e CampoNote
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			lEveNot = lEveCtrl.ExInserisciEventoNotifica(aEveNotModel, lConn);

			// Aggiorno dati finali cumulo
			lDatiFinaliDAO = new DatiFinaliCumuloDAO(lConn);
			lDatiFinaliDAO.setEveIdEvento(lEveNot.getEvento().getIdEvento());
			lDatiFinaliDAO.selCondizioneUpdate(aIdDatiFinali);
			lDatiFinaliDAO.update();

			// n.b. Pena Residua e LA vengon ribaltate in fase di validazione.

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in fase di inserimento del Provvedimento di cumulo", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.ExInserisciProvvedimentoCumulo: Non posso inserire: " + ex);
		} catch (F3BException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in fase di inserimento del Provvedimento di cumulo", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.ExInserisciProvvedimentoCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lDatiFinaliDAO);
			cleanup(lConn);
		}

		return lEveNot;
	}

	/**
	*
	*/
	public EventoNotificaModel ExModificaProvvedimentoCumulo(EventoNotificaModel aEveNotModel,
			BigDecimal aIdDatiFinali) throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEveNot = null;
		EventoStoreProcedurePulisciDAO lEventoProc = null;
		DatiFinaliCumuloDAO lDatiFinaliDAO = null;

		try {
			lConn = getDBConnection();

			// Rimuovo l'evento e le grandezze collegate
			// n.b. la SP effettua la commit interna
			BigDecimal idEvento = aEveNotModel.getEvento().getIdEvento();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Cancello evento con id = " + idEvento);
			if (idEvento == null)
				throw new F3BException("Impossibile cancellare. Id evento assente");

			lEventoProc = new EventoStoreProcedurePulisciDAO(lConn);
			lEventoProc.setIdEvento(idEvento);
			lEventoProc.execute();

			// Inserimento Evento e Notifiche e CampoNote
			IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
			lEveNot = lEveCtrl.ExInserisciEventoNotifica(aEveNotModel, lConn);

			lDatiFinaliDAO = new DatiFinaliCumuloDAO(lConn);
			lDatiFinaliDAO.setEveIdEvento(lEveNot.getEvento().getIdEvento());
			lDatiFinaliDAO.selCondizioneUpdate(aIdDatiFinali);
			lDatiFinaliDAO.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: Errore in fase di inserimento del Provvedimento di cumulo", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.ExInserisciProvvedimentoCumulo: Non posso inserire: " + ex);
		} catch (F3BException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("F3BException: Errore in fase di inserimento del Provvedimento di cumulo", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.ExInserisciProvvedimentoCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lEventoProc);
			cleanup(lDatiFinaliDAO);

			cleanup(lConn);
		}

		return lEveNot;
	}

	/**
	 * Recupera gli estremi dei procedimenti di classe IV iscriti in istruttoria (aIdIstruttoria) appartenenti
	 * all'ufficio (aChiaveUfficio) e i procedimenti di classe IV su cui sono in esecuzione delle Misure di
	 * sicurezza.
	 */
	public Vector<ProcedimentoCumulatoModel> ExRicercaProcedimentiClasseIVPerRibaltamentoByIdIstru(
			BigDecimal aIdIstruttoria, String aChiaveUfficio) throws F3BException {

		Connection lConn = null;

		Vector<ProcedimentoCumulatoModel> lProcedimenti = new Vector<>();

		ProcedimentoCumulatoSqlDAO lProcedimentoSqlDao = null;

		try {
			lConn = getDBConnection();

			lProcedimentoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
			lProcedimentoSqlDao.ricercaProcedimentiClasseIVPerRibaltamentoByIdIstru(aIdIstruttoria,
					aChiaveUfficio);
			lProcedimentoSqlDao.start();

			while (lProcedimentoSqlDao.next()) {
				ProcedimentoCumulatoModel lProcedimento = (ProcedimentoCumulatoModel) lProcedimentoSqlDao
						.getModelPerRibaltamento();

				lProcedimenti.add(lProcedimento);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"DatiFinaliCumuloController.ExRicercaProcedimentiClasseIVPerRibaltamentoByIdIstru: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.ExRicercaProcedimentiClasseIVPerRibaltamentoByIdIstru: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lProcedimentoSqlDao);

			cleanup(lConn);
		}

		return lProcedimenti;
	}

	/**
	 * Validazion Del provvedimento di cumulo.
	 *
	 * - Aggiorna la Posizione giuridica - Ribata la Pena Residua da eseguire - Crea i Record Licenza
	 * LibAnticipata se presenti - Crea/aggiorna gli scadenzari - Aggiorna FLAG_CUMULANTE si FascicoloSiep -
	 * Creae se necesario il fascicolo di classe IV o Aggiorna quello esistente
	 *
	 */
	public void ExUpdateValidaProvvedimentoCumulo(EventoModel aEvento,
			Vector<TitoloCumulatoModel> alistaTitoli) throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		EventoDAO lEveDaoBlob = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		LicenzaLibanticipataDAO lLicenzaDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSicCumSqlDAO = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		ScadenzarioDAO lScaDao = null;
		ScadenzarioSqlDAO lScadeSqlDao = null;
		FascicoloSiepDAO lFascDao = null;
		FascicoloSiepSqlDAO lFascSqlDao = null;
		IstruttoriaCumuloDAO lIstruttoriaDao = null;
		DatiFinaliCumuloSqlDAO lDatiFinaliSqlDao = null;
		DatiFinaliCumuloDAO lDatiFinaliDao = null;
		PenaRideterminataCumuloSqlDAO lPenaRidetCumuloSqlDao = null;
		PosizioneGiuridicaCumuloSqlDAO lPosGiuCumuloSqlDao = null;
		ParametroSqlDAO lParSqlDao = null;
		EventoDAO lEveDao = null;
		ArchiviazioneDAO lArcDao = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnotaEsitoDao = null;
		PenaAccessoriaCumuloSqlDAO lPenAccCumSqlDao = null;
		PenaAccessoriaDAO lPenAccDao = null;
		
		AltraCausaDAO lAltraCausaDao = null;
		AltraCausaSqlDAO lAltraCausaSqlDao = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			lEveModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lEveModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lEveModel.setDataAggiornamento(aEvento.getDataAggiornamento());
			//
			// Cerco il FascicoloSiep per leggere FLAG_CUMULANTE
			lFascSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFascSqlDao.ricercaFascicoloByKey(lEveModel.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFascSqlDao.getModelByKey();

			lDatiFinaliSqlDao = new DatiFinaliCumuloSqlDAO(lConn);
			lDatiFinaliDao = new DatiFinaliCumuloDAO(lConn);

			lDatiFinaliSqlDao.ricercaDatiFinaliCumuloByIdIstruttoria(lEveModel.getIstruIdIstruttoriaCumulo());
			DatiFinaliCumuloModel lDatiFinaliModel = (DatiFinaliCumuloModel) lDatiFinaliSqlDao
					.getModelByKey();

			// se FLAG_CUMULANTE è = S, allora NON si tratta di PRIMO_CUMULO, ma di ALTRO_CUMULO
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorno Dati_Finali_Cumulo valorizzando Flag_Primo_Cumulo con P o A ");
			if ("S".equals(lFasModel.getFlagCumulante()))
				lDatiFinaliModel.setFlagPrimoCumulo("A");
			else
				lDatiFinaliModel.setFlagPrimoCumulo("P");

			lDatiFinaliDao.setDAOFromModelForUpdate(lDatiFinaliModel);
			lDatiFinaliDao.selCondizioneUpdate(lDatiFinaliModel.getIdDatiFinaliCumulo());
			lDatiFinaliDao.update();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorno lo stato procedimento");

			StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

			lStatoProcMod.setProgressivo(new BigDecimal(1));
			// if (atipologia.equals("0")) // Condannato in stato di liberta'
			// lStatoProcMod.setCodStatoProcedimento("0003"); // Eseguito Ordine di Esecuzione con Arresto il
			// else if (atipologia.equals("1")) // Condannato gia' detenuto
			// lStatoProcMod.setCodStatoProcedimento("0015"); // Eseguito Ordine di Esecuzione con Notifica il
			// else if (atipologia.equals("2")) // Condannato in misura alternativa
			// lStatoProcMod.setCodStatoProcedimento("0117"); // Emesso Provvedimento di Esecuzione Pene
			// Concorrenti in Misura Alternativa
			// else // stampa generica // 3 Generico
			lStatoProcMod.setCodStatoProcedimento("0136"); // Emesso provvedimento di pene concorrenti in data
			// FIXME da stabilire i codici corretti

			lStatoProcMod.setData(lEveModel.getDataEmissione());

			lStatoProcMod.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());

			lStatoProcMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lStatoProcMod.setDataInserimento(aEvento.getDataAggiornamento());
			lStatoProcMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

			lStatoProcDao = new StatoProcedimentoDAO(lConn);

			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStatoProcDao.setCondizioneByIdFascicolo(lEveModel.getFasSieIdFascicoloSiep());
			lStatoProcDao.delete();

			// - Inserisce
			lStatoProcDao.setDAOFromModel(lStatoProcMod);
			lStatoProcDao.insert();

			// ========================================================================
			// Aggiorno la Pena Residua
			// ========================================================================
			// Recupero Pene Rideterminate
			PenaRideterminataCumuloModel lPenaResiduaCumuloMod = null;

			lPenaRidetCumuloSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
			lPenaRidetCumuloSqlDao
					.ricercaPenaResiduaCumulByIdDatiFinali(lDatiFinaliModel.getIdDatiFinaliCumulo());
			lPenaResiduaCumuloMod = (PenaRideterminataCumuloModel) lPenaRidetCumuloSqlDao.getModelByKey();

			// Coverto la pena rideterminata in pena residua
			PenaResiduaModel lPenResMod = lPenaResiduaCumuloMod.getPenaResidua();

			if (lPenResMod.isErgastolo()) {
				// Azzero o dati della detentiva in caso di ergastolo
				// Reclusione/multa
				lPenResMod.setNumAnniReclusione(null);
				lPenResMod.setNumMesiReclusione(null);
				lPenResMod.setNumGiorniReclusione(null);
				lPenResMod.setImportoMulta(null);

				// Arresti/Ammenda
				lPenResMod.setNumAnniArresto(null);
				lPenResMod.setNumMesiArresto(null);
				lPenResMod.setNumGiorniArresto(null);
				lPenResMod.setImportoAmmenda(null);
			}

			lPenResMod.setEveIdEvento(lEveModel.getIdEvento());
			lPenResMod.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());
			lPenResMod.setFlagValidato("S");

			lPenResMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lPenResMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lPenResMod.setDataInserimento(aEvento.getDataAggiornamento());

			lPenResMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lPenResMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lPenResMod.setDataAggiornamento(aEvento.getDataAggiornamento());

			// Recupero l'ultima pena residua
			PenaResiduaModel lUltimaPenResMod = null;
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());
			lUltimaPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

			lPenResDao = new PenaResiduaDAO(lConn);
			if (lUltimaPenResMod != null && !"S".equals(lUltimaPenResMod.getFlagValidato())
					&& lUltimaPenResMod.getEveIdEvento() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Presente pena residua non validata non agganciata ad alcun evento. La cancello ");

				lPenResDao.setCondizioneUpdate(lUltimaPenResMod.getIdPenaResidua());
				lPenResDao.delete();
			}

			// Inserisco la pena residua
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco la pena residua rideterminata in cumulo");
			lPenResDao.setDAOFromModel(lPenResMod);
			lPenResDao.insert();

			// ========================================================================
			// Inserisco le Liberazioni Anticipate se presenti
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco le Liberazioni Anticipate se presenti");
			PenaRideterminataCumuloModel lPenaRideterminataCumuloMod = null;

			lPenaRidetCumuloSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
			lPenaRidetCumuloSqlDao
					.ricercaPenaRideterminataCumulByIdDatiFinali(lDatiFinaliModel.getIdDatiFinaliCumulo());

			lPenaRideterminataCumuloMod = (PenaRideterminataCumuloModel) lPenaRidetCumuloSqlDao
					.getModelByKey();

			lLicenzaDao = new LicenzaLibanticipataDAO(lConn);
			if (lPenaRideterminataCumuloMod.isLibAnt()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Presenti LA procedo all'inserimento");
				Vector<LicenzaLibAnticipataModel> lListaLA = lPenaRideterminataCumuloMod
						.getLiberazioniAnticipate();

				for (int i = 0; i < lListaLA.size(); i++) {
					LicenzaLibAnticipataModel lLicModel = lListaLA.elementAt(i);

					lLicModel.setEveIdEvento(aEvento.getIdEvento());
					lLicModel.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());

					if (lPenResMod.getDataInizio() != null && !lPenResMod.isErgastolo()) {
						lLicModel.setFlagElaborato("S");
					}

					lLicModel.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lLicModel.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lLicModel.setDataInserimento(aEvento.getDataAggiornamento());

					lLicModel.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
					lLicModel.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
					lLicModel.setDataAggiornamento(aEvento.getDataAggiornamento());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco LA = " + lLicModel);
					// lLicenzaDao.setDAOFromModelForUpdate(lLicModel);
					lLicenzaDao.setDAOFromModel(lLicModel);
					lLicenzaDao.insert();
					lLicenzaDao.stop();
				}
			}

			// ========================================================================
			// Aggiorno la posizione giuridica
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorno la posizione giuridica");
			lPosGiuCumuloSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			lPosGiuCumuloSqlDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria(
					lDatiFinaliModel.getIstrIdIstruttoriaCumulo());

			PosizioneGiuridicaCumuloModel lPosGiuCumModel = (PosizioneGiuridicaCumuloModel) lPosGiuCumuloSqlDao
					.getModelByKey();

			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lEveModel.getFasSieIdFascicoloSiep());

			PosizioneGiuridicaModel lPosGiuCorrente = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			// Aggiorno la Posizione Corrente
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			if (lPosGiuCorrente != null && lPosGiuCorrente.getDataFine() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Aggiorno la Posizione Corrente: Data fine");
				lPosDao.setDataFine(lEveModel.getDataEmissione());

				lPosDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
				lPosDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
				lPosDao.setDataAggiornamento(aEvento.getDataAggiornamento());

				lPosDao.setCondizioneUpdate(lPosGiuCorrente.getIdPosizioneGiuridica());
				lPosDao.update();
				lPosDao.stop();
			}

			// Inserisco la nuova posizione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Inserisco la nuova posizione");
			PosizioneGiuridicaModel lNewPosGiuModel = new PosizioneGiuridicaModel();

			lNewPosGiuModel.setCodPosizioneGiuridica(lPosGiuCumModel.getCodPosizioneGiuridica());
			lNewPosGiuModel.setDataInizio(lEveModel.getDataEmissione());

			lNewPosGiuModel.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());
			lNewPosGiuModel.setIdEventoRiferimento(aEvento.getIdEvento());

			lNewPosGiuModel.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
			lNewPosGiuModel.setDataInserimento(lEveModel.getDataAggiornamento());
			lNewPosGiuModel.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());

			lNewPosGiuModel.setCodPosizioneProcessuale("-");

			lPosDao.setDAOFromModel(lNewPosGiuModel);
			lPosDao.insert();
			lPosDao.stop();

			// [Ticket#20210430011] - se la PG è detenuto altra causa, aggiporno il flga altra causa
			// si FASCICOLO_SIEPe aggiungo il record ALTRA_CAUSA
			siesLogger.debug("Verifico se detenutao Altra Causa...");
		    String flagAltraCausa = "N";
		    Collection <DecodificheModel> lCollPosGiuAltra = DecodificheManager.getInstance()
		        .getPosizioneGiuridicaAltraCausa();
		    Iterator iteCollPosGiuAltra = lCollPosGiuAltra.iterator();

		    while (iteCollPosGiuAltra.hasNext()) {
		      DecodificheModel dMPosGiuAltra = (DecodificheModel) iteCollPosGiuAltra.next();
		      String code = dMPosGiuAltra.getCode();
		      if (code.equals(lNewPosGiuModel.getCodPosizioneGiuridica())) {
		        flagAltraCausa = "S";
		        break;
		      }
		    }
			
		    if ("S".equals(flagAltraCausa)) {
		    	siesLogger.debug("Detenuto Altra Causa inserisco il record AC");
		    	AltraCausaModel altraCausaModel = new AltraCausaModel();
		    	altraCausaModel.setFasSieIdFascicoloSiep (lFasModel.getIdFascicoloSiep());
		    	altraCausaModel.setCodTipoPosGiuridica   (lNewPosGiuModel.getCodPosizioneGiuridica());
		    	altraCausaModel.setDataDecorrenza        (lNewPosGiuModel.getDataInizio());
		    	altraCausaModel.setCodAutorita("-");
		    	altraCausaModel.setCodLuogo("-");
		    	
		    	altraCausaModel.setIstDetIdIstitutoDetenzione (lPosGiuCumModel.getIstDetIdIstitutoDetenzione());
		    	
		    	altraCausaModel.setCodOperatoreInserimento (lEveModel.getCodOperatoreAggiornamento());
		    	altraCausaModel.setDataInserimento         (lEveModel.getDataAggiornamento());
		    	altraCausaModel.setCodUfficioInserimento   (lEveModel.getCodUfficioAggiornamento());

		    	lAltraCausaDao = new AltraCausaDAO (lConn);
		    	lAltraCausaDao.setDAOFromModel(altraCausaModel);
		    	lAltraCausaDao.insert();
		    }
			// FINE [Ticket#20210430011]
			
			// ========================================================================
			// Aggiorna flag cumulante e flag cumulato sul Fascicolo_model
			// ========================================================================
			// TODO Aggiorna flag cumulante e flag cumulato sul Fascicolo_model
			// ========================================================================
			// Aggiorno lo FASCICOLO_SIEP.FLAG_CUMULANTE
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorno FASCICOLO_SIEP.FLAG_CUMULANTE = 'S'");
			lFascDao = new FascicoloSiepDAO(lConn);

			lFascDao.selCondizioneUpdate(lEveModel.getFasSieIdFascicoloSiep());
			
			lFascDao.setFlagCumulante("S");

			// [Ticket#20210430011] - aggiorno eventualmente il flagAltraCausa
			if ("S".equals(flagAltraCausa)) {
			  lFascDao.setFlagAltraCausa(flagAltraCausa);
			}
			// FINE [Ticket#20210430011] - aggiorno eventualmente il flagAltraCausa
			
			// INTERVENTO PER Ticket#20200220015 — Cumulo su procedimento archiviato
			// se sto validanto un cumulo e lo stato in cui si trova il fascicolo è ARCHIVIATO, questo va
			// settato a 03
			if (lFasModel != null && "01".equals(lFasModel.getCodStatoFascicolo()))
				lFascDao.setCodStatoFascicolo("03");

			lFascDao.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
			lFascDao.setCodOperatoreAggiornamento(lEveModel.getCodOperatoreAggiornamento());
			lFascDao.setDataAggiornamento(lEveModel.getDataAggiornamento());

			lFascDao.update();
			lFascDao.stop();

			// ========================================================================
			// Aggiorno lo scadenzario
			// 01 = Legge 165/98 (c.d. Simeone)
			// 02 = Fine pena
			// 03 = Vane Ricerche
			// 06 = Differimento Pena
			// 13 = Misure Alternative
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorno lo scadenzario");
			lScaDao = new ScadenzarioDAO(lConn);
			ScadenzarioModel lScaMod = null;

			// Date lFineScadenza = null;

			// lTipoScedenzario:
			// 01 = Simeone
			// 02 = Fine Pena
			// 03 = Vane Ricerche
			// 06 = Differimento Pena
			// 13 = Misura Alternativa
			String lTipoScedenzario = "01";

			// lPosGiuCumModel.getCodPosizioneGiuridica()
			if (lEveModel.getCodMotivo().equals("0635") // Ordine di esecuzione per la carcerazione ex Art 656
														// comma 5 cpp
					|| lEveModel.getCodMotivo().equals("0637") // Ordine di esecuzione per la carcerazione ex
																// Art 656 comma 5 cpp
					|| lEveModel.getCodMotivo().equals("0642") // Ordine di esecuzione per la carcerazione
																// contestuale sospensione ex Art 656 comma 5
																// cpp
					|| lEveModel.getCodMotivo().equals("0661") // Ordine di esecuzione per la carcerazione
																// contestuale sospensione ex Art 656 comma 5
																// cpp
					|| lEveModel.getCodMotivo().equals("0666") // Ordine di esecuzione per la carcerazione ex
																// Art 656 comma 5 cpp - Decreto
																// Irreperibilità
			) {
				lTipoScedenzario = "01"; // Simeone
			} else if (lEveModel.getCodMotivo().equals("0643") // Ordine di esecuzione Ex art 656 comma 1 cpp
																// (condannato detenuto)
					|| lEveModel.getCodMotivo().equals("0638") // Ordine di esecuzione per la carcerazione ex
																// Art 656 comma 9 let. c cpp
																// (Custodia Cautelare in carcere)
					|| lPenResMod.getDataFine() != null // MEV70 20/05/2019 Aggiornamento scadenzario se data
														// fine pena è valorizzata.
			// || lEveModel.getCodMotivo().equals("0640") //Ordine di esecuzione Ex art 656 comma 1 cpp
			// (Custodia Cautelare in carcere Altra Causa)
			) {
				lTipoScedenzario = "02"; // Fine Pena
			} else if (lEveModel.getCodMotivo().equals("0630") // Libero - Comma 1
					|| lEveModel.getCodMotivo().equals("0631") // Libero - Comma 7
					|| lEveModel.getCodMotivo().equals("0632") // Libero - Comma 8
					|| lEveModel.getCodMotivo().equals("0633") // Libero - Comma 9 let a cpp
					|| lEveModel.getCodMotivo().equals("0634") // Libero - Comma 9 let c cpp
					|| lEveModel.getCodMotivo().equals("0636") // Differimento scaduto
			) {
				lTipoScedenzario = "03"; // Vane Ricerche
			} else if (lEveModel.getCodMotivo().equals("0646") //
					|| lEveModel.getCodMotivo().equals("0647") //
					|| lEveModel.getCodMotivo().equals("0648") //
					|| lEveModel.getCodMotivo().equals("0649") //
					|| lEveModel.getCodMotivo().equals("0650") //
					|| lEveModel.getCodMotivo().equals("0651") //
			) {
				// Posizioni "In Misura" si chiede la prosecuzione o la cessazione
				// Da defnire
			} else {
				// PG: [02,70,71,72] - Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari
				// PG: [77,78,79,80,81] - Custodia Cautelare per Altra Causa - Regime di Arresti Domiciliari
				// PG: [62, 64, 65] - Sospensione cautelativa delle misure alternative 51 ter legge 354/75
				// PG: [04, 82, 83] - Espiazione Pena in Regime di Arresti Domiciliari
				// Vane ricerche???
			}

			// 23/12/2019 - Ticket 20191217019 - Limitazione accesso alla fase di gestione scadenzario ai soli
			// casi di presenza pena residua detentiva.
			// Reclusione
			if ((lPenResMod.getNumAnniReclusione() != null
					&& lPenResMod.getNumAnniReclusione().intValue() > 0)
					|| (lPenResMod.getNumMesiReclusione() != null
							&& lPenResMod.getNumMesiReclusione().intValue() > 0)
					|| (lPenResMod.getNumGiorniReclusione() != null
							&& lPenResMod.getNumGiorniReclusione().intValue() > 0)
					||
					// Arresti
					(lPenResMod.getNumAnniArresto() != null && lPenResMod.getNumAnniArresto().intValue() > 0)
					|| (lPenResMod.getNumMesiArresto() != null
							&& lPenResMod.getNumMesiArresto().intValue() > 0)
					|| (lPenResMod.getNumGiorniArresto() != null
							&& lPenResMod.getNumGiorniArresto().intValue() > 0)) {
				if (lTipoScedenzario.equals("03")) { // Vane Ricerche
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Soggetto Libero Attivo lo scadenzario Vane Ricerche");

					// Cerco se presente lo scadenzario specifico "VANE RICERCHE" dell'ufficio
					lParSqlDao = new ParametroSqlDAO(lConn);
					lParSqlDao.ricercaParametroScadenzario("VANE RICERCHE",
							aEvento.getCodUfficioAggiornamento());

					ParametroModel lParMod = (ParametroModel) lParSqlDao.getModelByKey();
					lParSqlDao.stop();

					if (lParMod == null) {
						// se non presente cerco quello generico
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di
						// LogF3B.getLogger()
						siesLogger.debug("Non presente scadenzario specifico, cerco quello generico");
						lParSqlDao.ricercaParametroScadenzario("VANE RICERCHE", null);
						lParMod = (ParametroModel) lParSqlDao.getModelByKey();
					}

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lParMod = " + lParMod);

					Date lInizioScadenza = lEveModel.getDataEmissione();
					Date lFineScadenza = lEveModel.getDataEmissione();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"lInizioScadenza = " + DateUtils.getDateToString(lInizioScadenza, "dd/MM/yyyy"));
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"lFineScadenza = " + DateUtils.getDateToString(lFineScadenza, "dd/MM/yyyy"));

					lFineScadenza = DateUtils.moveDateTo(lFineScadenza, java.util.Calendar.YEAR,
							lParMod.getAnni().intValue());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"lFineScadenza = " + DateUtils.getDateToString(lFineScadenza, "dd/MM/yyyy"));
					lFineScadenza = DateUtils.moveDateTo(lFineScadenza, java.util.Calendar.MONTH,
							lParMod.getMesi().intValue());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"lFineScadenza = " + DateUtils.getDateToString(lFineScadenza, "dd/MM/yyyy"));
					lFineScadenza = DateUtils.moveDateTo(lFineScadenza, java.util.Calendar.DAY_OF_MONTH,
							lParMod.getGiorni().intValue());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"lFineScadenza = " + DateUtils.getDateToString(lFineScadenza, "dd/MM/yyyy"));

					lScaMod = new ScadenzarioModel();
					lScaMod.setCodTipoScadenzario("03");
					lScaMod.setDataInizioScadenza(lInizioScadenza);
					lScaMod.setDataFineScadenza(lFineScadenza);

				} else if (lTipoScedenzario.equals("02")) { // FINE PENA
					lScaMod = new ScadenzarioModel();
					lScaMod.setCodTipoScadenzario("02");
					lScaMod.setDataInizioScadenza(lPenResMod.getDataInizio());
					lScaMod.setDataFineScadenza(lPenResMod.getDataFine());
				} else if (lTipoScedenzario.equals("06")) { // Differimento
					lScaMod = new ScadenzarioModel();
					lScaMod.setCodTipoScadenzario("06");
					lScaMod.setDataInizioScadenza(lPosGiuCumModel.getDataInizio());
					lScaMod.setDataFineScadenza(lPosGiuCumModel.getDataFineMisura());
				} else if (lTipoScedenzario.equals("01")) { // 17/04/2019 MEV70 Aggiunta Scadenzario Simeone
					lScaMod = new ScadenzarioModel();
					lScaMod.setCodTipoScadenzario("01");
					if (lPosGiuCumModel.getDataInizio() != null)
						lScaMod.setDataInizioScadenza(lPosGiuCumModel.getDataInizio());
					else
						lScaMod.setDataInizioScadenza(lEveModel.getDataEmissione());
					lScaMod.setDataFineScadenza(lPosGiuCumModel.getDataFineMisura());
				}

				if (lScaMod != null) {
					// Verifico se andare in Update
					lScadeSqlDao = new ScadenzarioSqlDAO(lConn);
					lScadeSqlDao.ricercaScadenzarioByTipoScadenzarioIdFascicolo(
							lScaMod.getCodTipoScadenzario(), lEveModel.getFasSieIdFascicoloSiep());
					ScadenzarioModel lScadModelOld = (ScadenzarioModel) lScadeSqlDao.getModelByKey();

					lScaMod.setFlagVisto("N");
					lScaMod.setEveIdEvento(lEveModel.getIdEvento());
					lScaMod.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());

					if (lScaMod.getCodTipoScadenzario().equals("03"))// Vane Ricerche
						lScaMod.setCodStatoNotifica("NP"); // Valore Iniziale Indica il VVR non ancora
															// pervenuto

					if (lScadModelOld == null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di
						// LogF3B.getLogger()
						siesLogger.debug("Lo scadenzario non esiste vado in insert ");
						lScaMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
						lScaMod.setDataInserimento(aEvento.getDataAggiornamento());

						lScaDao.setDAOFromModel(lScaMod);
						lScaDao.insert();
						lScaDao.stop();
					} else {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di
						// LogF3B.getLogger()
						siesLogger.debug("Lo scadenzario esiste, vado in update");

						lScaMod.setIdScadenzario(lScadModelOld.getIdScadenzario());

						lScaMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
						lScaMod.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
						lScaMod.setDataAggiornamento(aEvento.getDataAggiornamento());

						lScaDao.setDAOFromModelForUpdate(lScaMod);
						lScaDao.update();
						lScaDao.stop();
					}
				}
			} // Chiusura inserimento/aggiornamento Scadenzario

			// ========================================================================
			// Chiudo L'istruttoria
			// ========================================================================
			lIstruttoriaDao = new IstruttoriaCumuloDAO(lConn);

			// lIstruttoriaDao.setDataChiusura (lEveModel.getDataEmissione());
			lIstruttoriaDao.setDataChiusura(aEvento.getDataAggiornamento());
			lIstruttoriaDao.setFlagStato(ICostantiIstruttoriaCumulo.FLAG_STATO_CHIUSA);
			lIstruttoriaDao.setEveIdEventoProv(lEveModel.getIdEvento());

			lIstruttoriaDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreAggiornamento());
			lIstruttoriaDao.setCodUfficioAggiornamento(aEvento.getCodUfficioAggiornamento());
			lIstruttoriaDao.setDataAggiornamento(aEvento.getDataAggiornamento());

			// lIstruttoriaDao.setDAOFromModel(aIstruttoriaCumulo );
			lIstruttoriaDao.selCondizioneUpdate(lDatiFinaliModel.getIstrIdIstruttoriaCumulo());
			lIstruttoriaDao.update();

			// ========================================================================
			// Verifico se necessario creare/aggiornare il procedimento di classe IV di
			// esecuzione delle MS
			// ========================================================================
			if ("S".equals(lDatiFinaliModel.getFlagCreaFascicoloMs())) {
				lMisSicCumSqlDAO = new MisuraSicurezzaCumuloSqlDAO(lConn);
				Vector<MisuraSicurezzaCumuloModel> lListaMisure = new Vector<>();
				lMisSicCumSqlDAO.ricercaMisureSicurezzaCumuloByIdIstruttoria(
						lEveModel.getIstruIdIstruttoriaCumulo(), true);
				lMisSicCumSqlDAO.start();
				lListaMisure = new Vector<MisuraSicurezzaCumuloModel>(lMisSicCumSqlDAO.getModels());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lListaMisure.size() = " + lListaMisure.size());

				if (lDatiFinaliModel.getFasSieIdFascicoloSiepMs() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Devo aggiungere le MS a un procedimento già presente");
					aggiungiAProcedimentoDiClasseIV(lConn, lListaMisure, lEveModel, lDatiFinaliModel);
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Devo creare un nuovo procedimento di classe IV");
					iscriviProcedimentoDiClasseIV(lConn, lListaMisure, lEveModel, lNewPosGiuModel,
							lPenResMod);
				}
			}

			// ========================================================================
			// "MAC" - 04/10/2018 è stato richiesto di ribaltare le PA sul cumulante.
			// Prendo le PENA_ACCESSORIA_CUMULO.FLAG_DATI_FINALI = 'S' e le ribalto su
			// PENA_ACCESSORIA del cumulante se non già presenti
			// ========================================================================
			{
				Vector<PenaAccessoriaCumuloModel> lPeneAccessorieCumulo = new Vector<>();

				lPenAccCumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
				lPenAccCumSqlDao.ricercaPenaAccessoriaCumuloByIdIstruttoria(
						lDatiFinaliModel.getIstrIdIstruttoriaCumulo(), true);
				lPeneAccessorieCumulo = new Vector<PenaAccessoriaCumuloModel>(lPenAccCumSqlDao.getModels());
				lPenAccCumSqlDao.stop();

				lPenAccDao = new PenaAccessoriaDAO(lConn);

				// ======================================
				// Recupero lIdTitoloCumulante x escludere dal ribaltamento le PA del
				// cumulante già presenti
				BigDecimal lIdTitoloCumulante = null;
				Iterator itx = alistaTitoli.iterator();
				while (itx.hasNext()) {
					TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) itx.next();
					if (lTitolo.getProcedimentoCumulato() != null
							&& lTitolo.getProcedimentoCumulato().getIdFascicoloSiepOrigine() != null
							&& lTitolo.getProcedimentoCumulato().getIdFascicoloSiepOrigine()
									.compareTo(lFasModel.getIdFascicoloSiep()) == 0) {
						lIdTitoloCumulante = lTitolo.getIdTitoloCumulato();
						break;
					}
				}
				// ======================================

				Iterator<PenaAccessoriaCumuloModel> lIterPA = lPeneAccessorieCumulo.iterator();
				while (lIterPA.hasNext()) {
					PenaAccessoriaCumuloModel lPACumModel = lIterPA.next();

					// Verifico se inserire la PA. La salto se già presente sul cumulante
					boolean ribaltaPA = false;
					if ("C".equals(lPACumModel.getFlagStato())) { // Cancellata logicamente
						ribaltaPA = false;
					} else if (lPACumModel.getTitIdTitoloCumulato().compareTo(lIdTitoloCumulante) != 0) {
						siesLogger.debug("PA non del cumulante, la ribalto");
						ribaltaPA = true;
					} else {
						if (lPACumModel.getIdPenaAccessoriaOrigine() == null) {
							siesLogger.debug(
									"PA del cumulante, ma aggiunta in cumulo. La Ribalto: " + lPACumModel);
							ribaltaPA = true;
						} else {
							siesLogger.debug("PA del cumulante, originaria NON la ribalto: " + lPACumModel);
							ribaltaPA = false;
						}
					}

					if (ribaltaPA) {
						PenaAccessoriaModel lPAModel = new PenaAccessoriaModel();

						// lPAModel.setIdPenaAccessoria(BigDecimal aValore )
						lPAModel.setCodTipoPenaAccessoria(lPACumModel.getCodTipoPenaAccessoria());
						lPAModel.setDescrAltrePA(lPACumModel.getDescrAltrePA());

						lPAModel.setDurata(lPACumModel.getDurata());
						lPAModel.setNumAnni(lPACumModel.getNumAnni());
						lPAModel.setNumMesi(lPACumModel.getNumMesi());
						lPAModel.setNumGiorni(lPACumModel.getNumGiorni());

						// Verificare se inserire almeno a testo libero l'indicazione che
						// proviene dal cumulo
						// Disposta sul titolo xxxx assorbito con provvedimento di cumulo del
						// xx/xx/xxxx
						String lTestoNota = null;
						for (TitoloCumulatoModel lTitPA : alistaTitoli) {
							if (lTitPA.getIdTitoloCumulato()
									.compareTo(lPACumModel.getTitIdTitoloCumulato()) == 0) {
								lTestoNota = "Disposta su " + lTitPA.getDescrTipoProvvedimento();
								lTestoNota += " N. " + lTitPA.getAnnoSentenza() + "/"
										+ lTitPA.getNumeroSentenza();
								lTestoNota += " del " + DateUtils
										.getDateToString(lTitPA.getDataProvvedimento(), "dd/MM/yyyy");
								lTestoNota += " Emessa da " + lTitPA.getDescrTipoAutoritaEmittente() + " di "
										+ lTitPA.getDescrLuogoEmittente();
								lTestoNota += " assorbita con provvedimento di pene concorrenti emesso in data "
										+ DateUtils.getDateToString(lEveModel.getDataEmissione(),
												"dd/MM/yyyy");
								break;
							}
						}

						// lPAModel.setNote (lPACumModel.getNote());
						lPAModel.setNote(lTestoNota);

						lPAModel.setFasSieIdFascicoloSiep(lEveModel.getFasSieIdFascicoloSiep());
						lPAModel.setFlagDichiarazioneFalsita("N"); // Altrimenti va in null pointer il
																	// dettaglio
						lPAModel.setFlagRevocaCondono("N"); // Altrimenti va in null pointer il dettaglio

						lPAModel.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
						lPAModel.setDataInserimento(aEvento.getDataAggiornamento());
						lPAModel.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());

						/*
						 * String lStrPA = new String(); lStrPA = "lPAModel:\n" +
						 * "[ mCodTipoPenaAccessoria = "+lPAModel.getCodTipoPenaAccessoria()+" ]\n"+
						 * "[ mDescrAltrePA          = "+lPAModel.getDescrAltrePA()+" ]\n"+
						 * "[ mDurata                = "+lPAModel.getDurata()+" ]\n"+
						 * "[ mNumAnni               = "+lPAModel.getNumAnni()+" ]\n"+
						 * "[ mNumMesi               = "+lPAModel.getNumMesi()+" ]\n"+
						 * "[ mNumGiorni             = "+lPAModel.getNumGiorni()+" ]\n"+
						 * "[ mNote                  = "+lPAModel.getNote() +" ]\n"+
						 * "[ mFasSieIdFascicoloSiep = "+lPAModel.getFasSieIdFascicoloSiep()+" ]";
						 *
						 * siesLogger.debug("Ribalto la PA sul Cumulante = "+lStrPA);
						 */

						lPenAccDao.setDAOFromModel(lPAModel);
						lPenAccDao.insert();
					}

				}
				lPenAccDao.stop();
				// PenaAccessoriaDAO lPenAccDao = null;
			}

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiorno il blob sull'evento e valido");
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			// ===========================================================================================
			// La Commit sulla Validazione del Provvedimento di Cumulo viene fatta comunque in questo momento
			// ===========================================================================================

			commit(lConn);

			// ===================================================================
			// la Commit per le Archiviazioni viene fatta in un secondo momento
			// ===================================================================

			// ==================================================================
			// Vado in Modifica/Aggiornamento dei FASCICOLI_SIEP collegati ai titoli
			// assorbiti in cumulo: fascicoli cumulati. Le modifiche vengono effettuate
			// SOLO per i FASCIOLI stesso Ufficio e consistono in:
			// 1) registrazione dei dati del cumulante sulla tabella FASCICOLO_SIEP
			// 2) Aggiorno il codice ESITO sul record ANNOTAZIONE_ESITO_TRASMISSIONE
			// 3) Eventuale Archiviazione automaticamente (perdita di Competenza)
			// ==================================================================
			TitoloCumulatoModel lTitolo = null;
			EventoModel lEveArc = null;
			ArchiviazioneModel lArcMod = null;

			EsitoArchiviazioniCumuloModel lEsitoArcMod = null;
			EsitoArchiviazioniCumuloDAO lEsiArcDao = null;

			siesLogger.debug("Procedo all'aggiornamento dei Fascicoli Stesso Ufficio");
			Iterator itx = alistaTitoli.iterator();
			while (itx.hasNext()) {
				try {
					lTitolo = (TitoloCumulatoModel) itx.next();
					String lRapporto = "";
					lRapporto = "";
					lRapporto += "Titolo " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza()
							+ " ";
					siesLogger.debug("" + lRapporto);

					// Controllo che ci sia Procedimento_Cumulato e Fascicolo_Siep_Origine
					// 1) Registrare il cumulo sul FASCIOLO cumulato (FLAG_CUMULANTE, FLAG_CUMULATO,
					// COD_UFFICIO_UNIONE, DATA_UNIONE)
					// ANNO_FASCICOLO_UNIONE, NUM_FASCICOLO_UNIONE
					// 2) Se possibile si procede all'archiviazione
					// - Registrare esito archiviazione
					// 3) Registrazione ESITO su ANNOTAZIONE_ESITO_TRASMISSIONE
					if (lTitolo.getProcedimentoCumulato() != null
							&& lTitolo.getProcedimentoCumulato().getIdFascicoloSiepOrigine() != null
							// Escludo il cumulante
							&& lTitolo.getProcedimentoCumulato().getIdFascicoloSiepOrigine()
									.compareTo(lFasModel.getIdFascicoloSiep()) != 0
							// Solo Stesso ufficio
							&& lTitolo.getProcedimentoCumulato().getCodUfficioFasCumulato()
									.equals(lFasModel.getChiaveUfficio())) {
						// FASCICOLO SIEP CUMULATO
						FascicoloSiepModel lFascicolo = null;
						lFascSqlDao.ricercaFascicoloByKey(
								lTitolo.getProcedimentoCumulato().getIdFascicoloSiepOrigine());
						lFascicolo = (FascicoloSiepModel) lFascSqlDao.getModelByKey();

						if (lFascicolo != null && lFascicolo.getIdFascicoloSiep() != null) {
							// Registro i dati del cumulante s FASCIOLO SIEP
							// Aggiorno ANNOTAZIONE ESITO TRASMISSIONE
							// -- commit
							// Provo ad archiviare se previsto (rollback)
							// Registro esito archiviazione (commit)

							// Registro il fatto che il fascicolo è stato cumulato se non già registrato
							if (lFascicolo.getAnnoFascicoloUnione() == null
									|| "".equals(lFascicolo.getAnnoFascicoloUnione())) {
								siesLogger.debug("Aggiorno ANNO_FASCICOLO_UNIONE,ID_FASCICOLO_SIEP, ecc sul "
										+ lFascicolo.getChiaveAnno() + "/" + lFascicolo.getChiaveProgr());
								lFascicolo.setAnnoFascicoloUnione(lFasModel.getChiaveAnno().toString());
								lFascicolo.setNumFascicoloUnione(lFasModel.getChiaveProgr().toString());
								lFascicolo.setCodUfficioUnione(lFasModel.getChiaveUfficio());
								lFascicolo.setDataUnione(lEveModel.getDataEmissione());

								lFascicolo.setCodOperatoreAggiornamento(
										lEveModel.getCodOperatoreAggiornamento());
								lFascicolo.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
								lFascicolo.setDataAggiornamento(lEveModel.getDataAggiornamento());

								lFascDao.setDAOFromModel(lFascicolo);
								lFascDao.selCondizioneUpdate(lFascicolo.getIdFascicoloSiep());
								lFascDao.update();
								lFascDao.stop();
							} else {
								siesLogger.debug(
										"NON Aggiorno ANNO_FASCICOLO_UNIONE,ID_FASCICOLO_SIEP, ecc sul "
												+ lFascicolo.getChiaveAnno() + "/"
												+ lFascicolo.getChiaveProgr() + " DATI GIA' VALORIZZATI");
							}

							// Procedo ad archiviare il procedimentoDevo verificare se già

							// ================================================================
							// Aggiorno il codice ESITO sul
							// record ANNOTAZIONE_ESITO_TRASMISSIONE dell'evento fittizio
							// del cumulato in modo da far sparire la scritta sul dettaglio
							// fascicolo.
							// ================================================================
							if ("04".equals(lTitolo.getTipoIscrizione())
									&& lTitolo.getProcedimentoCumulato().getEveIdEvento() != null) {
								siesLogger.debug(
										"Aggiorno ANNOTAZIONE_ESITO_TRASMISSIONE su evento di trasmissione stesso Ufficio");

								lAnnotaEsitoDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);

								lAnnotaEsitoDao.setCodEsito(ICostantiJMS.ASSORBITO_IN_CUMULO);

								lAnnotaEsitoDao
										.setCodUfficioAggiornamento(lEveModel.getCodUfficioAggiornamento());
								lAnnotaEsitoDao.setCodOperatoreAggiornamento(
										lEveModel.getCodOperatoreAggiornamento());
								lAnnotaEsitoDao.setDataAggiornamento(lEveModel.getDataAggiornamento());

								lAnnotaEsitoDao.selCondizioneUpdateByEveIdEvento(
										lTitolo.getProcedimentoCumulato().getEveIdEvento());

								lAnnotaEsitoDao.update();
							}

							commit(lConn);

							// ================================================================
							// Provo ad Archiviare se previsto Inserisco l'evento di archiviazione
							// ================================================================
							boolean isDaArchiviare = true;
							// Escludo Fas classe IV su cui ho caricato le MS
							if (lDatiFinaliModel.getFasSieIdFascicoloSiepMs() != null
									&& lTitolo.getProcedimentoCumulato().getIdFascicoloSiepOrigine()
											.compareTo(lDatiFinaliModel.getFasSieIdFascicoloSiepMs()) == 0) {
								isDaArchiviare = false;
							}
							// isDaArchiviare = this.isDaArchiviare(lTitolo, lConn);

							// FIXME aggiungere ulteriori condizioni di non archiviabilità es classe III
							// non dovrebbe essere archiviato ulteriormente se già archiviato per perdita di
							// competenza

							siesLogger.debug("isDaArchiviare = " + isDaArchiviare);

							if (isDaArchiviare) {
								boolean isArchiviato = true;

								// 15/04/2019 MEV70 Non va inserito l'evento di archiviazione dei titoli di
								// classe III (La loro archiviazione viene gestita manualmente).
								if (lFascicolo.getChiaveProgr().intValue() < 30000
										|| lFascicolo.getChiaveProgr().intValue() >= 40000) {
									try {
										String lCodMotivo = "";

										if (lFascicolo.getCodStatoFascicolo().equals("01")) // 01 = Archiviato
											lCodMotivo = "0356"; // Cumulo stesso ufficio
										else
											lCodMotivo = "0019"; // archiviazione per assorbimento in cumulo
																	// stesso ufficio

										// EVENTO
										lEveDao = new EventoDAO(lConn);
										lEveArc = new EventoModel();

										lEveArc.setCodTipoEvento("01");
										lEveArc.setCodTipoProvvedimento("25");

										lEveArc.setCodMotivo(lCodMotivo);
										lEveArc.setDataEmissione(lEveModel.getDataEmissione());
										lEveArc.setDataTrasmissioneAtti(aEvento.getDataAggiornamento());
										lEveArc.setCodUfficioEmittente(lEveModel.getCodUfficioEmittente());
										lEveArc.setCodLuogoEmittente(lEveModel.getCodLuogoEmittente());
										lEveArc.setCodTipoUfficioDestinatario("-");
										lEveArc.setCodLuogoDestinatario("-");
										lEveArc.setCodUfficioDestinatario("-");
										// lEveArc.setCodMagistrato(this.calcolaMagistrato());
										lEveArc.setCodEsito("-");
										lEveArc.setFasSieIdFascicoloSiep(lTitolo.getProcedimentoCumulato()
												.getIdFascicoloSiepOrigine());
										lEveArc.setFlagVideoSiep("S");
										lEveArc.setFlagStampaSiep("S");
										lEveArc.setAnnoProtocollo(
												new BigDecimal(DateUtils.getSysDate("yyyy")));

										lEveArc.setCodOperatoreInserimento(
												lEveModel.getCodOperatoreAggiornamento());
										lEveArc.setCodUfficioInserimento(
												lEveModel.getCodUfficioAggiornamento());
										lEveArc.setDataInserimento(lEveModel.getDataAggiornamento());

										lEveDao.setDAOFromModel(lEveArc);
										BigDecimal lKeyEve = lEveDao.insert();
										lEveDao.stop();

										lEveArc.setIdEvento(lKeyEve);
										lEveArc.setFlagDocumentoRegistrato("S");

										// ARCHIVIAZIONE
										lArcDao = new ArchiviazioneDAO(lConn);
										lArcMod = new ArchiviazioneModel();

										lArcMod.setCodTipoProvvedimento("22");
										lArcMod.setCodOggettoDefinizione(lCodMotivo);
										lArcMod.setDataDefinizione(lEveModel.getDataAggiornamento());

										lArcMod.setCodOperatoreInserimento(
												lEveModel.getCodOperatoreAggiornamento());
										lArcMod.setCodUfficioInserimento(
												lEveModel.getCodUfficioAggiornamento());
										lArcMod.setDataInserimento(lEveModel.getDataAggiornamento());

										lArcMod.setFasSieIdFascicoloSiep(lTitolo.getProcedimentoCumulato()
												.getIdFascicoloSiepOrigine());
										lArcMod.setEveIdEvento(lKeyEve);

										lArcDao.setDAOFromModel(lArcMod);
										lArcDao.insert();
										lArcDao.stop();

										IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();

										if ("0019".equals(lCodMotivo)) {
											lCtrlArc.ExUpdateValidaArchiviazione(lEveArc, lFascicolo, lConn);
										} else if ("0356".equals(lCodMotivo)) {
											lCtrlArc.ExUpdateValidaAnnProvCumulo(lEveArc, lFascicolo, lConn);
										}
										isArchiviato = true;
									} catch (Exception ex) {
										isArchiviato = false;
										lRapporto = lRapporto
												+ " Errore durante l'Archiviazione/Validazione dei Cumulati - "
												+ ex + "";
										siesLogger.error("Exception: ", ex);
										siesLogger.debug(
												"Errore durante l'Archiviazione/Validazione dei Cumulati!"
														+ lRapporto);
										rollback(lConn);
										// ex.printStackTrace();
									} finally {

									}
								}

								// ==============================================================
								// Aggiorno Esito Archiviazione
								// ==============================================================
								siesLogger.debug("Aggiorno Esito Archiviazione");
								lEsiArcDao = new EsitoArchiviazioniCumuloDAO(lConn);
								lEsitoArcMod = new EsitoArchiviazioniCumuloModel();

								lEsitoArcMod.setChiaveAnnoFascSiep(
										lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato());
								lEsitoArcMod.setChiaveProgrFascSiep(
										lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato());
								lEsitoArcMod.setChiaveUfficio(
										lTitolo.getProcedimentoCumulato().getCodUfficioFasCumulato());

								// 15/04/2019 MEV70 Per i Classe III si inserisce un esito di archiviazione
								// 'E' = Escluso (La loro archiviazione viene gestita manualmente).
								if (lFascicolo.getChiaveProgr().intValue() >= 30000
										&& lFascicolo.getChiaveProgr().intValue() < 40000) {
									lEsitoArcMod.setFlagArchiviato("E");
									lRapporto += " " + "Escluso dall'Archiviazione";
								} else if (isArchiviato) {
									lEsitoArcMod.setFlagArchiviato("S");
									lRapporto += " " + "Archiviato Correttamente";
								} else
									lEsitoArcMod.setFlagArchiviato("N");

								lEsitoArcMod.setDescrizioneEsito(lRapporto);

								lEsitoArcMod.setIstrIdIstruttoriaCumulo(lTitolo.getIstrIdIstruttoriaCumulo());
								lEsitoArcMod.setFasIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());
								lEsitoArcMod.setEveIdEvento(aEvento.getIdEvento());

								lEsitoArcMod
										.setCodOperatoreInserimento(lEveModel.getCodOperatoreAggiornamento());
								lEsitoArcMod.setCodUfficioInserimento(lEveModel.getCodUfficioAggiornamento());
								lEsitoArcMod.setDataInserimento(lEveModel.getDataAggiornamento());

								lEsiArcDao.setDAOFromModel(lEsitoArcMod);
								lEsiArcDao.insert();

								lEsiArcDao.stop();

								commit(lConn);
							} // end if da archiviare
						} // end if fascicolo cumulato trovato
					} // end if procedimento stesso ufficio cumulato
				} catch (Exception ex) {
					// Probabilmente non si dovrebbe mai entrare qui
					siesLogger.error("Exception: ", ex);
					siesLogger.debug(
							"Errore durante l'elaborazione del titolo " + lTitolo.getIdTitoloCumulato());
					rollback(lConn);
				} finally {
					siesLogger.debug(
							"Errore durante l'elaborazione del titolo " + lTitolo.getIdTitoloCumulato());
				}
			} // Chiude ciclo while
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("DatiFinaliCumuloController.ExUpdateValidaProvvedimentoCumulo : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lStatoProcDao);
			cleanup(lEveDaoBlob);
			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lLicenzaDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lMisSicCumSqlDAO);
			cleanup(lPosSqlDao);
			cleanup(lPosDao);
			cleanup(lScaDao);
			cleanup(lScadeSqlDao);
			cleanup(lFascDao);
			cleanup(lFascSqlDao);
			cleanup(lIstruttoriaDao);
			cleanup(lDatiFinaliSqlDao);
			cleanup(lDatiFinaliDao);
			cleanup(lPenaRidetCumuloSqlDao);
			cleanup(lPosGiuCumuloSqlDao);
			cleanup(lParSqlDao);
			cleanup(lEveDao);
			cleanup(lArcDao);
			cleanup(lAnnotaEsitoDao);
			cleanup(lPenAccCumSqlDao);
			cleanup(lPenAccDao);

			cleanup(lConn);
		}
	} // CHIUDE ExUpdateValidaProvvedimentoCumulo()

	/**
	 * Metodo che genera il provvedimento di Cumulo
	 *
	 * @param EventoNotificaModel
	 * @param aEventoNotModel
	 * @param aFascicoloModel
	 * @param aUtenteMod
	 * @param aUfficioMod
	 *
	 */
	public ByteArrayOutputStream ExStampaProvvedimentoCumulo(EventoNotificaModel aEventoNotModel,
			FascicoloSiepModel aFascicoloModel, UtenteModel aUtenteMod, UfficioModel aUfficioMod)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		AvvocatoSiepxStampaSqlDAO lAvvSqlDao = null;
		IstruttoriaCumuloSqlDAO lIstrCumSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		SoggettoCumulatoSqlDAO lSoggCumSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		PenaRideterminataCumuloSqlDAO lPenaRidetCumuloModSqlDao = null;
		DatiFinaliUlterioriSanzioniSqlDAO lUlterioriSanzioniSqlDao = null;
		PosizioneGiuridicaCumuloSqlDAO lPosGiuCumSalDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSicCumSqlDAO = null;
		UfficioSqlDAO lUffSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstitutoSqlDao = null;
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;
		DecodificheDAO lDecDao = null;
		ComuneDAO lComDao = null;
		EventoSqlDAO lEveSqlDAO = null;

		Vector<UfficioModel> lListaUffEsecuzione = new Vector<>();
		Vector<UfficioModel> lListaUffCancelleria = new Vector<>();
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBTransaction();

			// Recupero i dati dell'istruttoria
			lIstrCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstrCumSqlDao
					.ricercaIstruttoriaCumuloByKey(aEventoNotModel.getEvento().getIstruIdIstruttoriaCumulo());

			IstruttoriaCumuloModel lIstruttoriaCumulo = (IstruttoriaCumuloModel) lIstrCumSqlDao
					.getModelByKey();

			// Verifico l'ordinamento Titoli
			String lOrdinamento = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;
			if (lIstruttoriaCumulo.getOrdinamentoTitoli() != null
					&& !lIstruttoriaCumulo.getOrdinamentoTitoli().equals("")) {
				lOrdinamento = lIstruttoriaCumulo.getOrdinamentoTitoli();
			}

			// Recupero i titoli Ordinati opportunamente
			Vector<TitoloCumulatoModel> lListaTitoli = new Vector<>();

			lSoggCumSqlDao = new SoggettoCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo(), lOrdinamento);

			lTitoloSqlDao.start();

			lUffSqlDao = new UfficioSqlDAO(lConn);
			lDecDao = new DecodificheDAO(lConn);
			lComDao = new ComuneDAO(lConn);

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();
				Vector<NotaDiTrasmissioneModel> lListaNoteTrasmissione = new Vector<>();

				if (!"S".equals(lTitolo.getFlagEscluso())) {
					// Recupero anche il SoggettoCumulato
					lSoggCumSqlDao.ricercaSoggettoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					SoggettoCumulatoModel lSoggModel = (SoggettoCumulatoModel) lSoggCumSqlDao.getModelByKey();
					lTitolo.setSoggettoCumulato(lSoggModel);
					lSoggCumSqlDao.stop();

					// Recupero il procedimento Cumulato
					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcCumModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();

					// Recupero i dati dell'ufficio origine se accorpato
					if (lProcCumModel != null && "S".equals(lProcCumModel.getFlagAccorpato())) {
						siesLogger.debug("Recupero i dati dell'ufficio origine");
						lUffSqlDao.selUfficioByCod(lProcCumModel.getChiaveUfficioOrigine());
						UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();
						lProcCumModel.setUfficioOrigine(lUffModel);
					}

					lTitolo.setProcedimentoCumulato(lProcCumModel);
					lProcCumSqlDao.stop();

					lListaTitoli.add(lTitolo);

					// Recupero i dati per le note di trasmissione : Ufficio di esecuzione
					NotaDiTrasmissioneModel lNoteTrasmEsecXTitolo = new NotaDiTrasmissioneModel();
					String lCod = "";

					siesLogger.debug("NotaDiTrasmissioneModel per Ufficio ESECUZIONE");
					if (lProcCumModel != null) {
						lUffSqlDao.selUfficioByCod(lProcCumModel.getCodUfficioFasCumulato());
						UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();

//						siesLogger.debug(lProcCumModel.getChiaveAnnoFasCumulato()+"/"+lProcCumModel.getChiaveProgrFasCumulato()+" di "+lUffModel.getCodTipoUfficio()+" di "+lUffModel.getDescrComune());
						lListaUffEsecuzione.add(lUffModel);
						lCod = lUffModel.getCodUfficio();

						lNoteTrasmEsecXTitolo.setUfficioNotaTrasmissione(lUffModel);
						lNoteTrasmEsecXTitolo.setFlagTipoUfficio("E");

						// Se lo stesso Ufficio Esecuzione è presente + volte nell'ambito del Titolo_Cumulato,
						// nella stampa sarà visualizzato una sola volta
						Iterator ItE = lListaUffEsecuzione.iterator();
						int lVisibile = 0;
						while (ItE.hasNext()) {
							UfficioModel aModel = (UfficioModel) ItE.next();
							if (aModel.getCodUfficio().compareTo(lCod) == 0) {
								lVisibile++;
							}
						}

//						siesLogger.debug("lVisibile = "+lVisibile);
						if (lVisibile > 1)
							lNoteTrasmEsecXTitolo.setFlagVisualizzaUfficio("N");
						else
							lNoteTrasmEsecXTitolo.setFlagVisualizzaUfficio("S");
						//
//						siesLogger.debug("Flag Visualizza = "+lNoteTrasmEsecXTitolo.getFlagVisualizzaUfficio());
						// Ticket#20220124013: si esclude il PM del cumulante dalle note di trasmissione per 
						// l'esecuzione
						//aFascicoloModel
						if (   lProcCumModel.getChiaveAnnoFasCumulato().compareTo(aFascicoloModel.getChiaveAnno())==0
							&& lProcCumModel.getChiaveProgrFasCumulato().compareTo(aFascicoloModel.getChiaveProgr())==0
							&& lProcCumModel.getCodUfficioFasCumulato().equals(aFascicoloModel.getChiaveUfficio())
							)
						{
							lNoteTrasmEsecXTitolo.setFlagVisualizzaUfficio("N");
						}
						// Ticket#20220124013 - FINE 
						lListaNoteTrasmissione.add(lNoteTrasmEsecXTitolo);
						// siesLogger.debug("NotaDiTrasmissioneModel per Ufficio ESECUZIONE scritta =
						// "+lNoteTrasmEsecXTitolo);

					}

					// Recupero i dati per le note di trasmissione: Ufficio di cancelleria
					// NB - se Accorpato va mandata alla cancelleria dell'accorpante
					NotaDiTrasmissioneModel lNoteTrasmCancXTitolo = new NotaDiTrasmissioneModel();
					lCod = "";

					siesLogger.debug("NotaDiTrasmissioneModel per Ufficio di CANCELLERIA -TipoUfficio = "
							+ lTitolo.getCodTipoAutoritaEmittente());

					lUffSqlDao.selUfficioByCodTipoUffCodComune(lTitolo.getCodTipoAutoritaEmittente(),
							lTitolo.getCodLuogoEmittente());
					UfficioModel lUffCancModel = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					// verifico se l'Ufficio Esiste
					if (lUffCancModel != null && lUffCancModel.getCodUfficio() != null) {
						// verifico se è stato accorpato in questo caso recupero i dati dell'accorpante
						lUffSqlDao.getUfficioAccorpanteByCodUffAccorpato(lUffCancModel.getCodUfficio());
						lUffSqlDao.start();

						lNoteTrasmCancXTitolo.setFlagTipoUfficio("C");
						if (lUffSqlDao.next()) {
							UfficioModel lUffAccorpante = (UfficioModel) lUffSqlDao.getModel();
							lNoteTrasmCancXTitolo.setUfficioNotaTrasmissione(lUffAccorpante);
							siesLogger.debug(
									"NotaDiTrasmissioneModel per Ufficio di CANCELLERIA: caso di Accorpato");
							lListaUffCancelleria.add(lUffAccorpante);
							lCod = lUffAccorpante.getCodUfficio();
						} else {
							lNoteTrasmCancXTitolo.setUfficioNotaTrasmissione(lUffCancModel);

							lListaUffCancelleria.add(lUffCancModel);
							lCod = lUffCancModel.getCodUfficio();
						}
					} else {
						siesLogger.debug(
								"NotaDiTrasmissioneModel per Ufficio di CANCELLERIA: caso Ufficio Inesistente");

						// Se l'Ufficio è inesistente, dentro NotaDiTrasmissioneModel riportato direttamente i
						// valori di
						// TitoloCumulato.TipoAutoritaEmittente e TitoloCumulato.CodLuogouEmittente.
						DecodificheModel lModel = new DecodificheModel();
						lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
						lModel.setCode(lTitolo.getCodTipoAutoritaEmittente());

						DecodificheModel lModelRet = null;
						UfficioModel lUffCancInesistModel = new UfficioModel();

						// Cerco di trovare la descrizione del Tipo Ufficio Emittente
						lDecDao.setCondizioni(lModel);
						lModelRet = (DecodificheModel) lDecDao.getModelByKey();
						lDecDao.stop();

						lUffCancInesistModel.setCodTipoUfficio(lTitolo.getCodTipoAutoritaEmittente());

						if (lModelRet.getDescription() != null
								&& lModelRet.getDescription().compareTo("-") != 0)
							lUffCancInesistModel.setDescrTipoUfficio(lModelRet.getDescription());

						// Cerco di trovare la descrizione del Comune
						ComuneModel lComMod = null;

						lComDao.setCodComune(lTitolo.getCodLuogoEmittente());
						lComDao.selByKey();
						lComMod = (ComuneModel) lComDao.getModelByKey();
						lComDao.stop();

						lUffCancInesistModel.setCodComune(lTitolo.getCodLuogoEmittente());

						if (lComMod != null && lComMod.getDescrizione() != null)
							lUffCancInesistModel.setDescrComune(lComMod.getDescrizione());

						// Riempio NotadiTrasmissione
						lNoteTrasmCancXTitolo.setFlagTipoUfficio("C");
						lNoteTrasmCancXTitolo.setUfficioNotaTrasmissione(lUffCancInesistModel);
						lNoteTrasmCancXTitolo.setFlagVisualizzaUfficio("S");

						// lListaUffCancelleria.add(lUffCancInesistModel);
						// lCod = lUffCancInesistModel.getCodUfficio();

					}

					// Se lo stesso Ufficio di Cancellaria è presente + volte nell'ambito del Titolo_Cumulato,
					// nella stampa sarà visualizzato una sola volta
					Iterator ItC = lListaUffCancelleria.iterator();
					int lVisibile = 0;
					while (ItC.hasNext()) {
						UfficioModel aModel = (UfficioModel) ItC.next();
						if (aModel.getCodUfficio().compareTo(lCod) == 0) {
							lVisibile++;
						}
					}

					if (lVisibile > 1)
						lNoteTrasmCancXTitolo.setFlagVisualizzaUfficio("N");
					else
						lNoteTrasmCancXTitolo.setFlagVisualizzaUfficio("S");
					//

					lListaNoteTrasmissione.add(lNoteTrasmCancXTitolo);
					// siesLogger.debug("NotaDiTrasmissioneModel per Ufficio di CANCELLERIA scritto 1 =
					// "+lNoteTrasmCancXTitolo);
				}

				lTitolo.setNotaTrasmissione(lListaNoteTrasmissione);
				siesLogger.debug(
						"ADD di Vector lListaNoteTrasmissione - size = " + lListaNoteTrasmissione.size());
			}
			lTitoloSqlDao.stop();

			// ========================================================================
			// Costruzione dell'xml
			// ========================================================================
			IStampaCumulo lStampaC = SIEPLookupRemote.getStampaCumuloRemote();
			TreeModel lTreeRoot = lStampaC.prelevaDatiIstruttoriaCumulo(aFascicoloModel, aUtenteMod,
					aUfficioMod, lIstruttoriaCumulo, lListaTitoli,
					aEventoNotModel.getEvento().getDataEmissione());

			// ======================
			// Aggiungo La sentenza
			// ======================
			SentenzaModel lSentenza = aFascicoloModel.getSentenza();
			lTreeRoot.add(new TreeModel(lSentenza));

			// =====================================
			// Aggiungo gli avvocati del Fascicolo
			// =====================================
			TreeModel lTreeFascicolo = lTreeRoot.findTreeModel(lTreeRoot, new FascicoloSiepModel());

			// TreePath lTreePath = new TreePath(new Object[] {"tmp", "foo", "bar"}).

			// Avvocati
			lAvvSqlDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(lIstruttoriaCumulo.getFasSieIdFascicoloSiep());
			lAvvSqlDao.start();
			Vector<AvvocatoSiepModel> lAvvocati = new Vector<AvvocatoSiepModel>(lAvvSqlDao.getModels());

			Iterator<AvvocatoSiepModel> lIterAvv = lAvvocati.iterator();
			while (lIterAvv.hasNext()) {
				AvvocatoSiepModel lAvvModel = lIterAvv.next();

				TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
				lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));

				lTreeFascicolo.add(lTreeAvvMod);

			}
			lAvvSqlDao.stop();

			// Aggiungo l'evento
			TreeModel lTreeEveMod = new TreeModel(aEventoNotModel.getEvento());
			StampaEventoUtils mEventoUtils = new StampaEventoUtils();
			mEventoUtils.appendNotifiche(lTreeEveMod, aEventoNotModel, lAvvocati, aUtenteMod);

			// Descrizione Evento Per intestazione
			String lDescrComma = "";
			String lCodMotivo = aEventoNotModel.getEvento().getCodMotivo();
			if (lCodMotivo.equals("0630") || lCodMotivo.equals("0636") || lCodMotivo.equals("0639"))
				lDescrComma = "ARTT. 663 656 COMMA 1 C.P.P.";
			else if (lCodMotivo.equals("0631"))
				lDescrComma = "ARTT. 663 656 COMMA 7 C.P.P.";
			else if (lCodMotivo.equals("0632"))
				lDescrComma = "ARTT. 663 656 COMMA 8 C.P.P.";
			else if (lCodMotivo.equals("0633"))
				lDescrComma = "ARTT. 663 656 COMMA 9 let. a C.P.P.";
			else if (lCodMotivo.equals("0634"))
				lDescrComma = "ARTT. 663 656 COMMA 9 let. b C.P.P.";

			aEventoNotModel.getEvento().setDescrProvvedimento(lDescrComma);

			// Magistrato
			if (aEventoNotModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEventoNotModel.getMagistrato()));

			lTreeRoot.add(lTreeEveMod);
			// siesLogger.debug("lTreeIstruttoria = "+lTreeIstruttoria);
			// IstruttoriaCumuloModel lIstrCumTree = (IstruttoriaCumuloModel) lTreeIstruttoria.getModel();
			// siesLogger.debug("lIstrCumTree = "+ lIstrCumTree);

			// ============================
			// Aggiungo i dati finali
			// ============================
			TreeModel lTreeIstruttoria = lTreeRoot.findTreeModel(lTreeRoot, new IstruttoriaCumuloModel());

			// Posizione Giuridica
			lPosGiuCumSalDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			lPosGiuCumSalDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo());
			PosizioneGiuridicaCumuloModel lPGCumMod = (PosizioneGiuridicaCumuloModel) lPosGiuCumSalDao
					.getModelByKey();

			TreeModel lTreePG = new TreeModel(lPGCumMod);

			if (lPGCumMod.getChiaveUffFasSius() != null) {
				siesLogger.debug("Aggiungo l'ufficio Sorv");
				lUffSqlDao = new UfficioSqlDAO(lConn);
				lUffSqlDao.selUfficioByCod(lPGCumMod.getChiaveUffFasSius());
				UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();

				siesLogger.debug("lUffModel = " + lUffModel);
				lTreePG.add(new TreeModel(lUffModel));
			}

			if (lPGCumMod.getIstDetIdIstitutoDetenzione() != null) {
				siesLogger.debug("Recupero i dati dell'istituto");
				lIstitutoSqlDao = new IstitutoDetenzioneSqlDAO(lConn);
				lIstitutoSqlDao.ricercaIstitutoDetenzioneByKey(lPGCumMod.getIstDetIdIstitutoDetenzione());
				IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstitutoSqlDao.getModelByKey();

				String lDescIst = lIstituto.getDescrTipoIstituto() + " di " + lIstituto.getDescrizione()
						+ ", " + lIstituto.getIndirizzo();

				lPGCumMod.setDescrIstituto(lDescIst);

			}

			lTreeIstruttoria.add(lTreePG);

			// MEV_70 ==========================================================
			// le pene rideterminate sono estratte e riportatre in stampa in
			// StampaCumiloController.prelevaDatiIstruttoriaCumulo().
			// sono calcolate con il calcoloPenaCumulo usato da diego nella popup in linea
			/*
			 * // Recupero Pene Rideterminate lPenaRidetCumuloModSqlDao = new
			 * PenaRideterminataCumuloSqlDAO(lConn);
			 * lPenaRidetCumuloModSqlDao.ricercaPenaRideterminataCumulByIdIstruttoria(lIstruttoriaCumulo
			 * .getIdIstruttoriaCumulo()); lPenaRidetCumuloModSqlDao.start();
			 *
			 * while (lPenaRidetCumuloModSqlDao.next()) { PenaRideterminataCumuloModel lPenaRidetCumuloMod =
			 * (PenaRideterminataCumuloModel) lPenaRidetCumuloModSqlDao .getModel();
			 *
			 * lPenaRidetCumuloMod.calcolaStringheXStampa();
			 *
			 * try { // Provo a calcolare i quantum di pena da espiare alla data di emissione // del
			 * provvedimento if ("S".equals(lPenaRidetCumuloMod.getFlagPenaResiduaCumulo())) { if
			 * (lPenaRidetCumuloMod.getDataFine() != null // Pena in decorrenza &&
			 * !"03".equals(lPenaRidetCumuloMod.getCodTipoPenaDetentiva()) // non // ergastolo &&
			 * !"04".equals(lPenaRidetCumuloMod.getCodTipoPenaDetentiva()) // non // ergastolo ) { // Pena in
			 * decorrenza calcolo i quantum residui alla data di emissione
			 *
			 * PenaResiduaModel lPenaDaEspiareAdOggi = this.calcolaResiduoPenaAdOggi( lPenaRidetCumuloMod,
			 * aEventoNotModel.getEvento().getDataEmissione());
			 *
			 * siesLogger.debug("lPenaDaEspiareAdOggi = " + lPenaDaEspiareAdOggi);
			 *
			 * lPenaDaEspiareAdOggi.calcolaStringaReclusione(); lPenaDaEspiareAdOggi.calcolaStringaArresto();
			 *
			 * String lStrResiduoAdOggi = "";
			 *
			 * if (lPenaDaEspiareAdOggi.getStringaReclusione() != null) { lStrResiduoAdOggi += "" +
			 * lPenaDaEspiareAdOggi.getStringaReclusione(); }
			 *
			 * lPenaRidetCumuloMod.setStringaPenaResiduaAdOggi(lStrResiduoAdOggi);
			 *
			 * } } } catch (Exception e) { // do nothing }
			 *
			 * TreeModel lTreePRC = new TreeModel(lPenaRidetCumuloMod); lTreeIstruttoria.add(lTreePRC); }
			 *
			 * lPenaRidetCumuloModSqlDao.stop();
			 */
			// Recupero Ulteriori Sanzioni
			lUlterioriSanzioniSqlDao = new DatiFinaliUlterioriSanzioniSqlDAO(lConn);
			lUlterioriSanzioniSqlDao.ricercaDatiFinaliUlterioriSanzioniByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo());

			Vector<DatiFinaliUlterioriSanzioniModel> lListaUlterioriSanzioni = new Vector<DatiFinaliUlterioriSanzioniModel>(
					lUlterioriSanzioniSqlDao.getModels());

			if (lListaUlterioriSanzioni != null) {
				Iterator<DatiFinaliUlterioriSanzioniModel> lUlterioriSanzioniIter = lListaUlterioriSanzioni
						.iterator();
				while (lUlterioriSanzioniIter.hasNext()) {
					DatiFinaliUlterioriSanzioniModel lUltSanzModel = lUlterioriSanzioniIter.next();

					lUltSanzModel.calcolaStringheXStampa();

					TreeModel lTreeUltSanz = new TreeModel(lUltSanzModel);
					lTreeIstruttoria.add(lTreeUltSanz);

					if (lUltSanzModel.getCodTipoUlterioreSanzione()
							.equals(ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS)) {
						lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
						lTipologiaOrarioSqlDao.ricercaTipologiaOrarioByIdUltSanz(
								lUltSanzModel.getIdDatiFinaliUlterioriSanz());

						Vector<TipologiaOrarioModel> lListaOrari = new Vector<TipologiaOrarioModel>(
								lTipologiaOrarioSqlDao.getModels());
						lTipologiaOrarioSqlDao.stop();

						if (lListaOrari != null) {
							Iterator<TipologiaOrarioModel> lIterTipologia = lListaOrari.iterator();

							while (lIterTipologia.hasNext()) {
								TipologiaOrarioModel lTipol = lIterTipologia.next();

								lTipol.calcolaStringaDurata();

								lTreeUltSanz.add(new TreeModel(lTipol));
							}
						}
					}
				}
			}

			// ========================================================================
			// Richieste con anticipazione
			// ========================================================================
			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiSqlDao.ricercaComputiCumuloByIdIstruttoriaDatiFinali(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo(), lPGCumMod.getDatIdDatiFinaliCumulo());
			// lComputiSqlDao.ricercaComputiCumuloByIdIstruttoria
			// (lIstruttoriaCumulo.getIdIstruttoriaCumulo());

			Vector<ComputiCumuloModel> lComputiCumulo = new Vector<ComputiCumuloModel>(
					lComputiSqlDao.getModels());

			for (int i = 0; i < lComputiCumulo.size(); i++) {
				ComputiCumuloModel lComputo = lComputiCumulo.elementAt(i);
				lComputo.calcolaStringheXStampa();
				TreeModel lTreeComputo = new TreeModel(lComputo);
				lTreeIstruttoria.add(lTreeComputo);
			}

			// ========================================================================
			// Misure di sicurezza
			// ========================================================================
			lMisSicCumSqlDAO = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisSicCumSqlDAO.ricercaMisureSicurezzaCumuloByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo(), true);
			lMisSicCumSqlDAO.start();
			Vector<MisuraSicurezzaCumuloModel> lListaMisure = new Vector<MisuraSicurezzaCumuloModel>(
					lMisSicCumSqlDAO.getModels());

			siesLogger.debug("lListaMisure.size() = " + lListaMisure.size());
			Iterator<MisuraSicurezzaCumuloModel> lIterMisSic = lListaMisure.iterator();
			while (lIterMisSic.hasNext()) {
				MisuraSicurezzaCumuloModel lMisSicMod = lIterMisSic.next();
				// mev56 INIZIO ***********************
				// verifico se per il Fasciclo Siep, esiste un ordinanza/decreto
				// (COD_TIPO_PROVVEDIMENTO=02/03)
				// sulla tabella EVENTO emessa dal Magistrato di Sorveglianza
				// legato alla Misura di Sicurezza
				EventoModel lEventoMs = new EventoModel();
				lEventoMs.setFlagDocumentoRegistrato("S");
				// lEventoMs.setFasSieIdFascicoloSiep(lMis.getFasSieIdFascicoloSiep());
				lEventoMs.setFasSieIdFascicoloSiep(aFascicoloModel.getIdFascicoloSiep());
				lEventoMs.setCodTipoEvento("01");

				// Di seguito i MOTIVO_PROVVEDIMENTO legati ai seguenti contenuti:
				// OGGETTO_PROCEDIMENTO ('C029', 'C036')
				// OGGETTO_PROCEDIMENTO ('U023', 'U077', 'U082', 'U088', 'U089')
				String[] lCodMotivoProvvedimento = { "0258", "0259", "0260", "0428", "0429", "0430", "0431",
						"0432", "0433", "2110", "2111", "2112", "2113", "2114", "2550", "2551", "2552",
						"2553", "2554", "2555", "2660", "2670", "2404", "2405", "2406", "2407", "2409",
						"2408", "2422", "2416" };

				String[] lCodTipoProvvedimento = { "02", "03" };

				lEveSqlDAO = new EventoSqlDAO(lConn);
				lEveSqlDAO.ricercaEventoPerMotivoProvv(lCodMotivoProvvedimento, lCodTipoProvvedimento,
						lEventoMs);
				lEveSqlDAO.start();
				// MEV 16 CUMULO: aggiunto controllo di consistenza
				if (lEveSqlDAO.next()) {
					MisuraAlternativaAggregatoModel lAgg = lEveSqlDAO.getModelDecretoOrdinanzaUfficio();
					EventoModel lEveMS = null;
					if (lAgg != null && lAgg.getEventoNotifica() != null
							&& lAgg.getEventoNotifica().getEvento() != null)
						lEveMS = lAgg.getEventoNotifica().getEvento();
					if (lEveMS != null && lEveMS.getIdEvento() != null) {
						if (lEveMS.getCodTipoProvvedimento().equals("03")
								&& lAgg.getDepositoOrdinanzaPc() != null) {
							// NUMERO ORDINANZA
							lMisSicMod.setNumOrdDec(lAgg.getDepositoOrdinanzaPc().getNumS3().toString());
							// ANNO ORDINANZA
							lMisSicMod.setAnnoOrdDec(lAgg.getDepositoOrdinanzaPc().getAnnoS3().toString());
						} else if (lEveMS.getCodTipoProvvedimento().equals("02")
								&& lAgg.getDepositoDecreto() != null) {
							// NUMERO DECRETO
							lMisSicMod.setNumOrdDec(lAgg.getDepositoDecreto().getNumS72().toString());
							// ANNO DECRETO
							lMisSicMod.setAnnoOrdDec(lAgg.getDepositoDecreto().getAnnoS72().toString());
						}
						// LUOGO EMITTENTE
						lMisSicMod.setLuogoEmittente(lEveMS.getDescrLuogoEmittente());
						// DATA EMISSIONE
						lMisSicMod.setDataEmissione(lEveMS.getDataEmissione());
						// COD ESITO
						lMisSicMod.setCodEsito(lEveMS.getCodEsito());
						// DESC ESITO
						lMisSicMod.setDescEsitoTemplate(lEveMS.getDescEsitoTemplate());
					} else {
						// NON ESISTE DECISIONE DEL MAGISTRATO DI SORVEGLIANZA
					}
					// mev56 FINE ****************************
				}

				lTreeIstruttoria.add(new TreeModel(lMisSicMod));
			}

			// ========================================================================
			// Generazione del report
			// ========================================================================
			// Recupero il path completo del Template a partire dall'ID_TEMPLATE
			// String lNomeTemplate = TemplateManager.getInstance().getTemplateName
			// (aEventoNotModel.getNomeTemplate());
			String lNomeTemplate = TemplateManager.getInstance()
					.getTemplateName(aEventoNotModel.getEvento().getTemIdTemplate());
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aEventoNotModel.getEvento().setDocBlobIn(lByteArrayInput);
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEventoNotModel.getEvento());

			lEveDao.selCondizioneUpdate(aEventoNotModel.getEvento().getIdEvento());
			lEveDao.update();

			commit(lConn);

		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			siesLogger.error("DatiFinaliCumuloController - -------> Exception: " + e, e);
			throw new F3BException("DatiFinaliCumuloController.ExStampaProvvedimentoCumulo: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lAvvSqlDao);
			cleanup(lIstrCumSqlDao);
			cleanup(lTitoloSqlDao);
			cleanup(lSoggCumSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lPenaRidetCumuloModSqlDao);
			cleanup(lUlterioriSanzioniSqlDao);
			cleanup(lPosGiuCumSalDao);
			cleanup(lMisSicCumSqlDAO);
			cleanup(lUffSqlDao);
			cleanup(lIstitutoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lComputiSqlDao);
			cleanup(lDecDao);
			cleanup(lComDao);
			cleanup(lEveSqlDAO);

			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	/**
	 * Metodo che estrae i dati del provvedimento di Cumulo x la stampa delle comunicazioni alle Procure
	 * competenti
	 *
	 * @param EventoNotificaModel
	 * @param aEventoNotModel
	 * @param aFascicoloModel
	 * @param aUtenteMod
	 * @param aUfficioMod
	 *
	 */
	public ByteArrayOutputStream ExStampaComunicazioni(EventoNotificaModel aEventoNotModel,
			FascicoloSiepModel aFascicoloModel, UtenteModel aUtenteMod, UfficioModel aUfficioMod,
			String lDestinatario) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		NotificaSqlDAO lNotSqlDAO = null;
		AvvocatoSiepxStampaSqlDAO lAvvSqlDao = null;
		IstruttoriaCumuloSqlDAO lIstrCumSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		SoggettoCumulatoSqlDAO lSoggCumSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		PenaRideterminataCumuloSqlDAO lPenaRidetCumuloModSqlDao = null;
		DatiFinaliUlterioriSanzioniSqlDAO lUlterioriSanzioniSqlDao = null;
		PosizioneGiuridicaCumuloSqlDAO lPosGiuCumSalDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSicCumSqlDAO = null;
		UfficioSqlDAO lUffSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstitutoSqlDao = null;
		TipologiaOrarioSqlDAO lTipologiaOrarioSqlDao = null;
		ComputiCumuloSqlDAO lComputiSqlDao = null;
		DecodificheDAO lDecDao = null;
		ComuneDAO lComDao = null;

		Vector<UfficioModel> lListaUffEsecuzione = new Vector<>();
		Vector<UfficioModel> lListaUffCancelleria = new Vector<>();
		ByteArrayOutputStream lByteArrayOut = null;

		try {
			lConn = getDBTransaction();

			// Recupero i dati dell'istruttoria
			lIstrCumSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstrCumSqlDao
					.ricercaIstruttoriaCumuloByKey(aEventoNotModel.getEvento().getIstruIdIstruttoriaCumulo());

			IstruttoriaCumuloModel lIstruttoriaCumulo = (IstruttoriaCumuloModel) lIstrCumSqlDao
					.getModelByKey();

			// Verifico l'ordinamento Titoli
			String lOrdinamento = ICostantiIstruttoriaCumulo.ORDER_BY_DATA_IRREVOCABILITA_DESC;
			if (lIstruttoriaCumulo.getOrdinamentoTitoli() != null
					&& !lIstruttoriaCumulo.getOrdinamentoTitoli().equals("")) {
				lOrdinamento = lIstruttoriaCumulo.getOrdinamentoTitoli();
			}

			// Recupero i titoli Ordinati opportunamente
			Vector<TitoloCumulatoModel> lListaTitoli = new Vector<>();

			lSoggCumSqlDao = new SoggettoCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lTitoloSqlDao.ricercaTitoloCumulatoByIstruttoriaOrderBy(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo(), lOrdinamento);

			lTitoloSqlDao.start();

			lUffSqlDao = new UfficioSqlDAO(lConn);
			lDecDao = new DecodificheDAO(lConn);
			lComDao = new ComuneDAO(lConn);
			lNotSqlDAO = new NotificaSqlDAO(lConn);

			while (lTitoloSqlDao.next()) {
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModel();
				Vector<NotaDiTrasmissioneModel> lListaNoteTrasmissione = new Vector<>();

				if (!"S".equals(lTitolo.getFlagEscluso())) {
					// Recupero anche il SoggettoCumulato
					lSoggCumSqlDao.ricercaSoggettoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					SoggettoCumulatoModel lSoggModel = (SoggettoCumulatoModel) lSoggCumSqlDao.getModelByKey();
					lTitolo.setSoggettoCumulato(lSoggModel);
					lSoggCumSqlDao.stop();

					// Recupero il procedimento Cumulato
					lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
					ProcedimentoCumulatoModel lProcCumModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
							.getModelByKey();

					// Recupero i dati dell'ufficio origine se accorpato
					if (lProcCumModel != null && "S".equals(lProcCumModel.getFlagAccorpato())) {
						siesLogger.debug("Recupero i dati dell'ufficio origine");
						lUffSqlDao.selUfficioByCod(lProcCumModel.getChiaveUfficioOrigine());
						UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();
						lProcCumModel.setUfficioOrigine(lUffModel);
					}

					lTitolo.setProcedimentoCumulato(lProcCumModel);
					lProcCumSqlDao.stop();

					lListaTitoli.add(lTitolo);

					// Recupero i dati per le note di trasmissione : Ufficio di esecuzione
					NotaDiTrasmissioneModel lNoteTrasmEsecXTitolo = new NotaDiTrasmissioneModel();
					String lCod = "";

					siesLogger.debug("NotaDiTrasmissioneModel per Ufficio ESECUZIONE");
					if (lProcCumModel != null) {
						lUffSqlDao.selUfficioByCod(lProcCumModel.getCodUfficioFasCumulato());
						UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();
						lUffSqlDao.stop();

						lListaUffEsecuzione.add(lUffModel);
						lCod = lUffModel.getCodUfficio();

						lNoteTrasmEsecXTitolo.setUfficioNotaTrasmissione(lUffModel);
						lNoteTrasmEsecXTitolo.setFlagTipoUfficio("E");

						// Se lo stesso Ufficio Esecuzione è presente + volte nell'ambito del Titolo_Cumulato,
						// nella stampa sarà visualizzato una sola volta
						Iterator ItE = lListaUffEsecuzione.iterator();
						int lVisibile = 0;
						while (ItE.hasNext()) {
							UfficioModel aModel = (UfficioModel) ItE.next();
							if (aModel.getCodUfficio().compareTo(lCod) == 0) {
								lVisibile++;
							}
						}

						if (lVisibile > 1)
							lNoteTrasmEsecXTitolo.setFlagVisualizzaUfficio("N");
						else
							lNoteTrasmEsecXTitolo.setFlagVisualizzaUfficio("S");
						//

						lListaNoteTrasmissione.add(lNoteTrasmEsecXTitolo);
						// siesLogger.debug("NotaDiTrasmissioneModel per Ufficio ESECUZIONE scritta =
						// "+lNoteTrasmEsecXTitolo);

					}

					// Recupero i dati per le note di trasmissione: Ufficio di cancelleria
					// NB - se Accorpato va mandata alla cancelleria dell'accorpante
					NotaDiTrasmissioneModel lNoteTrasmCancXTitolo = new NotaDiTrasmissioneModel();
					lCod = "";

					siesLogger.debug("NotaDiTrasmissioneModel per Ufficio di CANCELLERIA -TipoUfficio = "
							+ lTitolo.getCodTipoAutoritaEmittente());

					lUffSqlDao.selUfficioByCodTipoUffCodComune(lTitolo.getCodTipoAutoritaEmittente(),
							lTitolo.getCodLuogoEmittente());
					UfficioModel lUffCancModel = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					// verifico se l'Ufficio Esiste
					if (lUffCancModel != null && lUffCancModel.getCodUfficio() != null) {
						// verifico se è stato accorpato in questo caso recupero i dati dell'accorpante
						lUffSqlDao.getUfficioAccorpanteByCodUffAccorpato(lUffCancModel.getCodUfficio());
						lUffSqlDao.start();

						lNoteTrasmCancXTitolo.setFlagTipoUfficio("C");
						if (lUffSqlDao.next()) {
							UfficioModel lUffAccorpante = (UfficioModel) lUffSqlDao.getModel();
							lNoteTrasmCancXTitolo.setUfficioNotaTrasmissione(lUffAccorpante);
							siesLogger.debug(
									"NotaDiTrasmissioneModel per Ufficio di CANCELLERIA: caso di Accorpato");
							lListaUffCancelleria.add(lUffAccorpante);
							lCod = lUffAccorpante.getCodUfficio();
						} else {
							lNoteTrasmCancXTitolo.setUfficioNotaTrasmissione(lUffCancModel);

							lListaUffCancelleria.add(lUffCancModel);
							lCod = lUffCancModel.getCodUfficio();
						}
					} else {
						siesLogger.debug(
								"NotaDiTrasmissioneModel per Ufficio di CANCELLERIA: caso Ufficio Inesistente");

						// Se l'Ufficio è inesistente, dentro NotaDiTrasmissioneModel riportato direttamente i
						// valori di
						// TitoloCumulato.TipoAutoritaEmittente e TitoloCumulato.CodLuogouEmittente.
						DecodificheModel lModel = new DecodificheModel();
						lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
						lModel.setCode(lTitolo.getCodTipoAutoritaEmittente());

						DecodificheModel lModelRet = null;
						UfficioModel lUffCancInesistModel = new UfficioModel();

						// Cerco di trovare la descrizione del Tipo Ufficio Emittente
						lDecDao.setCondizioni(lModel);
						lModelRet = (DecodificheModel) lDecDao.getModelByKey();
						lDecDao.stop();

						lUffCancInesistModel.setCodTipoUfficio(lTitolo.getCodTipoAutoritaEmittente());

						if (lModelRet.getDescription() != null
								&& lModelRet.getDescription().compareTo("-") != 0)
							lUffCancInesistModel.setDescrTipoUfficio(lModelRet.getDescription());

						// Cerco di trovare la descrizione del Comune
						ComuneModel lComMod = null;

						lComDao.setCodComune(lTitolo.getCodLuogoEmittente());
						lComDao.selByKey();
						lComMod = (ComuneModel) lComDao.getModelByKey();
						lComDao.stop();

						lUffCancInesistModel.setCodComune(lTitolo.getCodLuogoEmittente());

						if (lComMod != null && lComMod.getDescrizione() != null)
							lUffCancInesistModel.setDescrComune(lComMod.getDescrizione());

						// Riempio NotadiTrasmissione
						lNoteTrasmCancXTitolo.setFlagTipoUfficio("C");
						lNoteTrasmCancXTitolo.setUfficioNotaTrasmissione(lUffCancInesistModel);
						lNoteTrasmCancXTitolo.setFlagVisualizzaUfficio("S");

						// lListaUffCancelleria.add(lUffCancInesistModel);
						// lCod = lUffCancInesistModel.getCodUfficio();

					}

					// Se lo stesso Ufficio di Cancellaria è presente + volte nell'ambito del Titolo_Cumulato,
					// nella stampa sarà visualizzato una sola volta
					Iterator ItC = lListaUffCancelleria.iterator();
					int lVisibile = 0;
					while (ItC.hasNext()) {
						UfficioModel aModel = (UfficioModel) ItC.next();
						if (aModel.getCodUfficio().compareTo(lCod) == 0) {
							lVisibile++;
						}
					}

					if (lVisibile > 1)
						lNoteTrasmCancXTitolo.setFlagVisualizzaUfficio("N");
					else
						lNoteTrasmCancXTitolo.setFlagVisualizzaUfficio("S");
					//

					lListaNoteTrasmissione.add(lNoteTrasmCancXTitolo);
					// siesLogger.debug("NotaDiTrasmissioneModel per Ufficio di CANCELLERIA scritto 1 =
					// "+lNoteTrasmCancXTitolo);
				}

				lTitolo.setNotaTrasmissione(lListaNoteTrasmissione);
				siesLogger.debug(
						"ADD di Vector lListaNoteTrasmissione - size = " + lListaNoteTrasmissione.size());
			}
			lTitoloSqlDao.stop();

			// Carica i collegamenti a CurIdCuratore per le notifiche alle Procure.

			// Hashtable contiene per ogni CodProcura (chiave) la lista degli idTitolo
			// per i quali si inviano le notifiche
			Hashtable<String, Vector<BigDecimal>> lListaProcureTitoli = new Hashtable<>();

			if (lDestinatario.equals("Procure")) {
				lNotSqlDAO.ricercaNotificaByEvento(aEventoNotModel.getEvento().getIdEvento());
				Vector<NotificaModel> lNotifiche = new Vector<NotificaModel>(lNotSqlDAO.getModels());

				{
					// FIXME test per raggruppare le comunicazioni alla stessa procura se presenti più titoli
					// Se presenti più notifiche alla stessa procura ne devo inserire una
					// sola e devo aggiungere al NotificaModel un vettore di TitoliCumulatiModel
					Vector<NotificaModel> lNotificheAggregate = new Vector<>();
					Hashtable<String, NotificaModel> lNotificheProcura = new Hashtable<>();
					for (NotificaModel lNotifica : lNotifiche) {
						siesLogger.debug("lNotifica = " + lNotifica);
						// siesLogger.debug ("lNotifica.getUfficio() = "+lNotifica.getUfficio());
						// if ( "TDS".equals(lNotifica.getUfficio().getCodTipoUfficio()) ||
						// "UDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
						// || "TDSM".equals(lNotifica.getUfficio().getCodTipoUfficio()) ||
						// "UDSM".equals(lNotifica.getUfficio().getCodTipoUfficio())
						// )
						if (lNotifica.getCurIdCuratore() == null) { // Ufficio di sorveglianza lo aggiungo
																	// sempre
							lNotificheAggregate.add(lNotifica);
						} else {
							String lCodUfficioProc = lNotifica.getUffCodUfficio();
							if (lNotificheProcura.containsKey(lCodUfficioProc)) {
								// Notifica già presente per la procura, aggiungo solo l'id del titolo
								Vector<BigDecimal> lListaIdTitoli = lListaProcureTitoli.get(lCodUfficioProc);
								lListaIdTitoli.add(lNotifica.getCurIdCuratore());
							} else {
								// Aggiungo la notifica alla procura
								lNotificheProcura.put(lNotifica.getUffCodUfficio(), lNotifica);

								// Aggiungo il titolo alla notifica
								Vector<BigDecimal> lListaIdTitoli = new Vector<>();
								lListaIdTitoli.add(lNotifica.getCurIdCuratore());
								lListaProcureTitoli.put(lNotifica.getUffCodUfficio(), lListaIdTitoli);
							}
						}
					}
					//
					Enumeration lKeyProcure = lNotificheProcura.keys();
					while (lKeyProcure.hasMoreElements()) {
						String key = (String) lKeyProcure.nextElement();
						lNotificheAggregate.add(lNotificheProcura.get(key));
					}

					aEventoNotModel.setNotifiche(lNotificheAggregate.toArray(new NotificaModel[0]));
				}
				// aEventoNotModel.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
			}

			// ========================================================================
			// Costruzione dell'xml
			// ========================================================================
			IStampaCumulo lStampaC = SIEPLookupRemote.getStampaCumuloRemote();
			TreeModel lTreeRoot = lStampaC.prelevaDatiComunicazioniCumulo(aFascicoloModel, aUtenteMod,
					aUfficioMod, lIstruttoriaCumulo, lListaTitoli, aEventoNotModel, lDestinatario);

			// ======================
			// Aggiungo La sentenza
			// ======================
			SentenzaModel lSentenza = aFascicoloModel.getSentenza();
			lTreeRoot.add(new TreeModel(lSentenza));

			// =====================================
			// Aggiungo gli avvocati del Fascicolo
			// =====================================
			TreeModel lTreeFascicolo = lTreeRoot.findTreeModel(lTreeRoot, new FascicoloSiepModel());

			// TreePath lTreePath = new TreePath(new Object[] {"tmp", "foo", "bar"}).

			// Avvocati
			lAvvSqlDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(lIstruttoriaCumulo.getFasSieIdFascicoloSiep());
			lAvvSqlDao.start();
			Vector<AvvocatoSiepModel> lAvvocati = new Vector<AvvocatoSiepModel>(lAvvSqlDao.getModels());

			Iterator<AvvocatoSiepModel> lIterAvv = lAvvocati.iterator();
			while (lIterAvv.hasNext()) {
				AvvocatoSiepModel lAvvModel = lIterAvv.next();

				TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
				lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));

				lTreeFascicolo.add(lTreeAvvMod);

			}
			lAvvSqlDao.stop();

			// Per la "stampa Comunicazioni altre autorità" occorre valorizzare il "CodEsito" della Notifica
			// interessata a "S".
			if (lDestinatario.startsWith("N")) {
				NotificaModel[] lNotificheMod = aEventoNotModel.getNotifiche();
				for (int j = 0; j < lNotificheMod.length; j++) {
					NotificaModel lNot = lNotificheMod[j];
					if (lNot.getIdNotifica() != null
							&& lNot.getIdNotifica().equals(new BigDecimal(lDestinatario.substring(1)))) {
						aEventoNotModel.getNotifiche()[j].setCodEsito("S");
						break;
					}
				}
			}

			// Aggiungo l'evento
			TreeModel lTreeEveMod = new TreeModel(aEventoNotModel.getEvento());
			StampaEventoUtils mEventoUtils = new StampaEventoUtils();
			mEventoUtils.appendNotifiche(lTreeEveMod, aEventoNotModel, lAvvocati, aUtenteMod);

			// ========================================================================
			// Apro il lTreeEveMod <Evento>, per aggiungere alle notifiche i TreeModel
			// dei Titoli opportuni
			if (lDestinatario.equals("Procure")) {
				siesLogger.debug("Dopo append notifiche scorro TreeEvento");
				Enumeration lChildRoot = lTreeEveMod.children();
				while (lChildRoot.hasMoreElements()) {
					TreeModel lTNod = (TreeModel) lChildRoot.nextElement();
					// siesLogger.debug("Tipo di nodo = "+lTNod.getModel().getClass().getName());
					if (lTNod.getModel() instanceof NotificaModel) {

						NotificaModel lNotifica = (NotificaModel) lTNod.getModel();
						siesLogger.debug("Nodo di NotificaModel x Ufficio " + lNotifica.getUffCodUfficio());

						Vector<BigDecimal> lListaTitoliPerProcura = lListaProcureTitoli
								.get(lNotifica.getUffCodUfficio());
						if (lListaTitoliPerProcura != null) {
							for (BigDecimal lIdTitolo : lListaTitoliPerProcura) {
								siesLogger.debug("Titolo = " + lIdTitolo);

								for (TitoloCumulatoModel lTitolo : lListaTitoli) {
									siesLogger.debug("lTitolo = " + lTitolo);
									if (lTitolo.getIdTitoloCumulato().compareTo(lIdTitolo) == 0) {
										TreeModel lTreeTitolo = new TreeModel(lTitolo);
										if (lTitolo.getProcedimentoCumulato() != null) {
											lTreeTitolo.add(new TreeModel(lTitolo.getProcedimentoCumulato()));
										}
										lTNod.add(lTreeTitolo);
									}
								}
							}
						}
					}
				}
			}
			// ========================================================================

			// Descrizione Evento Per intestazione
			String lDescrComma = "";
			String lCodMotivo = aEventoNotModel.getEvento().getCodMotivo();
			if (lCodMotivo.equals("0630") || lCodMotivo.equals("0636") || lCodMotivo.equals("0639"))
				lDescrComma = "ARTT. 663 656 COMMA 1 C.P.P.";
			else if (lCodMotivo.equals("0631"))
				lDescrComma = "ARTT. 663 656 COMMA 7 C.P.P.";
			else if (lCodMotivo.equals("0632"))
				lDescrComma = "ARTT. 663 656 COMMA 8 C.P.P.";
			else if (lCodMotivo.equals("0633"))
				lDescrComma = "ARTT. 663 656 COMMA 9 let. a C.P.P.";
			else if (lCodMotivo.equals("0634"))
				lDescrComma = "ARTT. 663 656 COMMA 9 let. b C.P.P.";

			aEventoNotModel.getEvento().setDescrProvvedimento(lDescrComma);

			// Magistrato
			if (aEventoNotModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEventoNotModel.getMagistrato()));

			lTreeRoot.add(lTreeEveMod);
			// siesLogger.debug("lTreeIstruttoria = "+lTreeIstruttoria);
			// IstruttoriaCumuloModel lIstrCumTree = (IstruttoriaCumuloModel) lTreeIstruttoria.getModel();
			// siesLogger.debug("lIstrCumTree = "+ lIstrCumTree);

			// ============================
			// Aggiungo i dati finali
			// ============================
			TreeModel lTreeIstruttoria = lTreeRoot.findTreeModel(lTreeRoot, new IstruttoriaCumuloModel());

			// Posizione Giuridica
			lPosGiuCumSalDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			lPosGiuCumSalDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo());
			PosizioneGiuridicaCumuloModel lPGCumMod = (PosizioneGiuridicaCumuloModel) lPosGiuCumSalDao
					.getModelByKey();

			TreeModel lTreePG = new TreeModel(lPGCumMod);

			if (lPGCumMod.getChiaveUffFasSius() != null) {
				siesLogger.debug("Aggiungo l'ufficio Sorv");
				lUffSqlDao = new UfficioSqlDAO(lConn);
				lUffSqlDao.selUfficioByCod(lPGCumMod.getChiaveUffFasSius());
				UfficioModel lUffModel = (UfficioModel) lUffSqlDao.getModelByKey();

				siesLogger.debug("lUffModel = " + lUffModel);
				lTreePG.add(new TreeModel(lUffModel));
			}

			if (lPGCumMod.getIstDetIdIstitutoDetenzione() != null) {
				siesLogger.debug("Recupero i dati dell'istituto");
				lIstitutoSqlDao = new IstitutoDetenzioneSqlDAO(lConn);
				lIstitutoSqlDao.ricercaIstitutoDetenzioneByKey(lPGCumMod.getIstDetIdIstitutoDetenzione());
				IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel) lIstitutoSqlDao.getModelByKey();

				String lDescIst = lIstituto.getDescrTipoIstituto() + " di " + lIstituto.getDescrizione()
						+ ", " + lIstituto.getIndirizzo();

				lPGCumMod.setDescrIstituto(lDescIst);

			}

			lTreeIstruttoria.add(lTreePG);

			// Recupero Pene Rideterminate
			lPenaRidetCumuloModSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
			lPenaRidetCumuloModSqlDao.ricercaPenaRideterminataCumulByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lPenaRidetCumuloModSqlDao.start();

			while (lPenaRidetCumuloModSqlDao.next()) {
				PenaRideterminataCumuloModel lPenaRidetCumuloMod = (PenaRideterminataCumuloModel) lPenaRidetCumuloModSqlDao
						.getModel();

				lPenaRidetCumuloMod.calcolaStringheXStampa();

				try {
					// Provo a calcolare i quantum di pena da espiare alla data di emissione
					// del provvedimento
					if ("S".equals(lPenaRidetCumuloMod.getFlagPenaResiduaCumulo())) {
						if (lPenaRidetCumuloMod.getDataFine() != null // Pena in decorrenza
								&& !"03".equals(lPenaRidetCumuloMod.getCodTipoPenaDetentiva()) // non
																								// ergastolo
								&& !"04".equals(lPenaRidetCumuloMod.getCodTipoPenaDetentiva()) // non
																								// ergastolo
						) { // Pena in decorrenza calcolo i quantum residui alla data di emissione

							PenaResiduaModel lPenaDaEspiareAdOggi = this.calcolaResiduoPenaAdOggi(
									lPenaRidetCumuloMod, aEventoNotModel.getEvento().getDataEmissione());

							siesLogger.debug("lPenaDaEspiareAdOggi = " + lPenaDaEspiareAdOggi);

							lPenaDaEspiareAdOggi.calcolaStringaReclusione();
							lPenaDaEspiareAdOggi.calcolaStringaArresto();

							String lStrResiduoAdOggi = "";

							if (lPenaDaEspiareAdOggi.getStringaReclusione() != null) {
								lStrResiduoAdOggi += "" + lPenaDaEspiareAdOggi.getStringaReclusione();
							}

							lPenaRidetCumuloMod.setStringaPenaResiduaAdOggi(lStrResiduoAdOggi);

						}
					}
				} catch (Exception e) {
					// do nothing
				}

				TreeModel lTreePRC = new TreeModel(lPenaRidetCumuloMod);
				lTreeIstruttoria.add(lTreePRC);
			}
			lPenaRidetCumuloModSqlDao.stop();

			// Recupero Ulteriori Sanzioni
			lUlterioriSanzioniSqlDao = new DatiFinaliUlterioriSanzioniSqlDAO(lConn);
			lUlterioriSanzioniSqlDao.ricercaDatiFinaliUlterioriSanzioniByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo());

			Vector<DatiFinaliUlterioriSanzioniModel> lListaUlterioriSanzioni = new Vector<DatiFinaliUlterioriSanzioniModel>(
					lUlterioriSanzioniSqlDao.getModels());

			if (lListaUlterioriSanzioni != null) {
				Iterator<DatiFinaliUlterioriSanzioniModel> lUlterioriSanzioniIter = lListaUlterioriSanzioni
						.iterator();
				while (lUlterioriSanzioniIter.hasNext()) {
					DatiFinaliUlterioriSanzioniModel lUltSanzModel = lUlterioriSanzioniIter.next();

					lUltSanzModel.calcolaStringheXStampa();

					TreeModel lTreeUltSanz = new TreeModel(lUltSanzModel);
					lTreeIstruttoria.add(lTreeUltSanz);

					if (lUltSanzModel.getCodTipoUlterioreSanzione()
							.equals(ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS)) {
						lTipologiaOrarioSqlDao = new TipologiaOrarioSqlDAO(lConn);
						lTipologiaOrarioSqlDao.ricercaTipologiaOrarioByIdUltSanz(
								lUltSanzModel.getIdDatiFinaliUlterioriSanz());

						Vector<TipologiaOrarioModel> lListaOrari = new Vector<TipologiaOrarioModel>(
								lTipologiaOrarioSqlDao.getModels());
						lTipologiaOrarioSqlDao.stop();

						if (lListaOrari != null) {
							Iterator<TipologiaOrarioModel> lIterTipologia = lListaOrari.iterator();

							while (lIterTipologia.hasNext()) {
								TipologiaOrarioModel lTipol = lIterTipologia.next();

								lTipol.calcolaStringaDurata();

								lTreeUltSanz.add(new TreeModel(lTipol));
							}
						}
					}
				}
			}

			// ========================================================================
			// Richieste con anticipazione
			// ========================================================================
			lComputiSqlDao = new ComputiCumuloSqlDAO(lConn);
			lComputiSqlDao.ricercaComputiCumuloByIdIstruttoriaDatiFinali(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo(), lPGCumMod.getDatIdDatiFinaliCumulo());
			// lComputiSqlDao.ricercaComputiCumuloByIdIstruttoria
			// (lIstruttoriaCumulo.getIdIstruttoriaCumulo());

			Vector<ComputiCumuloModel> lComputiCumulo = new Vector<ComputiCumuloModel>(
					lComputiSqlDao.getModels());

			for (int i = 0; i < lComputiCumulo.size(); i++) {
				ComputiCumuloModel lComputo = lComputiCumulo.elementAt(i);
				lComputo.calcolaStringheXStampa();
				TreeModel lTreeComputo = new TreeModel(lComputo);
				lTreeIstruttoria.add(lTreeComputo);
			}

			// ========================================================================
			// Misure di sicurezza
			// ========================================================================
			lMisSicCumSqlDAO = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisSicCumSqlDAO.ricercaMisureSicurezzaCumuloByIdIstruttoria(
					lIstruttoriaCumulo.getIdIstruttoriaCumulo(), true);
			lMisSicCumSqlDAO.start();
			Vector<MisuraSicurezzaCumuloModel> lListaMisure = new Vector<MisuraSicurezzaCumuloModel>(
					lMisSicCumSqlDAO.getModels());

			siesLogger.debug("lListaMisure.size() = " + lListaMisure.size());
			Iterator<MisuraSicurezzaCumuloModel> lIterMisSic = lListaMisure.iterator();
			while (lIterMisSic.hasNext()) {
				MisuraSicurezzaCumuloModel lMisSicMod = lIterMisSic.next();

				lTreeIstruttoria.add(new TreeModel(lMisSicMod));
			}

			// ========================================================================
			// Generazione del report
			// ========================================================================
			// Recupero il path completo del Template a partire dall'ID_TEMPLATE
			// String lNomeTemplate = TemplateManager.getInstance().getTemplateName
			// (aEventoNotModel.getNomeTemplate());
			String lNomeTemplate = TemplateManager.getInstance()
					.getTemplateName(aEventoNotModel.getEvento().getTemIdTemplate());
			// siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot, lNomeTemplate);

			// Aggiorni evento e blob solo in caso di procure. X comunicazioni a
			// cancellerie ed altro non viene creato alcune evento ma ci si appoggia
			// sul provvedimento di cumulo che non va modificato
			if (lDestinatario.equals("Procure")) {
				ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

				aEventoNotModel.getEvento().setDocBlobIn(lByteArrayInput);
				lEveDao = new EventoDAO(lConn);
				lEveDao.setDAOFromModelForUpdateBlob(aEventoNotModel.getEvento());

				lEveDao.selCondizioneUpdate(aEventoNotModel.getEvento().getIdEvento());
				lEveDao.update();
			}

			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			e.printStackTrace();
			siesLogger.error("DatiFinaliCumuloController - -------> Exception: " + e, e);
			throw new F3BException("DatiFinaliCumuloController.ExStampaComunicazioni: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotSqlDAO);
			cleanup(lAvvSqlDao);
			cleanup(lIstrCumSqlDao);
			cleanup(lTitoloSqlDao);
			cleanup(lSoggCumSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lPenaRidetCumuloModSqlDao);
			cleanup(lUlterioriSanzioniSqlDao);
			cleanup(lPosGiuCumSalDao);
			cleanup(lMisSicCumSqlDAO);
			cleanup(lUffSqlDao);
			cleanup(lIstitutoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lTipologiaOrarioSqlDao);
			cleanup(lComputiSqlDao);
			cleanup(lDecDao);
			cleanup(lComDao);

			cleanup(lConn);
		}

		return lByteArrayOut;
	}

	private PenaResiduaModel calcolaResiduoPenaAdOggi(PenaRideterminataCumuloModel aPenaRidetCumulo,
			Date aOggi) throws Exception {

		PenaResiduaModel lPenaInEspiazione = aPenaRidetCumulo.getPenaResidua();

		// Calcolo il residuo pena se interrompessi Oggi
		CalcoloPenaModel lCalcPenaModel = new CalcoloPenaModel();
		lCalcPenaModel.calcolaPenaDaSospensione(lPenaInEspiazione, aOggi);

		PenaResiduaModel lPenaAncoraDaEspiareAdOggi = lCalcPenaModel.getPenaResiduaRicalcolata();
		// siesLogger.debug("lPenaDaEspiareAdOggi = "+lPenaAncoraDaEspiareAdOggi);
		// CalendarModel lPenaEspiata = lCalcPenaModel.getPenaEspiata();
		// siesLogger.debug("lPenaEspiata = "+lPenaEspiata);

		// Scarico Reclusione e Arresto su un unico tipo (Reclusione).
		// Non mi interessa se Reclusione e Arresto
		CalendarModel lCalRec = lPenaAncoraDaEspiareAdOggi.getQuantumReclusione();
		CalendarModel lCalArr = lPenaAncoraDaEspiareAdOggi.getQuantumArresto();

		CalendarUtil lCalendarUtil = new CalendarUtil();
		CalendarModel lCalApp = new CalendarModel();
		lCalApp = lCalendarUtil.sommaGiorni(lCalRec, lCalArr);
		// siesLogger.debug("lCalApp = "+lCalApp);
		lPenaAncoraDaEspiareAdOggi.setQuantumReclusione(lCalApp);
		lPenaAncoraDaEspiareAdOggi.setQuantumArresto(new CalendarModel()); // ZERO

		// ==========================================================================
		// Verifico se il quantum ricalcolato è tale da determinare una data fine
		// pena pari al giorno di calcolo. Per effetto degli errori di calcolo
		// i conti potrebbero non tornare per un giorno
		// ==========================================================================
		// Istanzio un secondo model di calcolo inizializzato con la residua calcolata
		// ad oggi
		CalcoloPenaModel lCalcoloModelApp = new CalcoloPenaModel();
		lCalcoloModelApp.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);
		lCalcoloModelApp.setPenaResiduaManuale(lPenaAncoraDaEspiareAdOggi);

		// ==========================================================================
		//
		// ==========================================================================
		// ==========================================================================
		// Entro nel ciclo di verifica:
		// aggiungo una richiesta al GE pari al quantum residuo calcolato ed
		// effettuo i calcoli del fine pena
		// ==========================================================================
		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalRecAncoraDaEspiareAl = new CalendarModel();
		CalendarModel lCalArrAncoraDaEspiareAl = new CalendarModel();

		// boolean uguale = false;
		boolean maggiore = false;
		boolean minore = false;

		// String lMessage = "";

		siesLogger.debug("Entro nel ciclo di verifica:");
		Date lDataScarcerazione = aPenaRidetCumulo.getDataFine();
		int lMaxLoopCount = 5;
		int lLoopCount = 0;

		while (true) {
			lLoopCount++;

			siesLogger.debug("lLoopCount = " + lLoopCount);

			if (lLoopCount > lMaxLoopCount) {
				siesLogger.debug("Esco per eccessive iterate ");
				// lMessage =
				// "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione
				// richiesta.";
				break;
			}

			Date lDataFinePena = null;
			PenaResiduaModel lPenaResidua = lCalcoloModelApp.getPenaDaEspiare(aOggi, null, "all");
			lDataFinePena = lPenaResidua.getDataFine();

			if (lDataFinePena == null) {
				siesLogger.debug("lDataFinePena null");
				// lDataFinePena = null se la pena è a 0 o in negativo
				if (CalendarUtil.getTotGiorni(lPenaResidua.getQuantumReclusione()) <= 0
						|| CalendarUtil.getTotGiorni(lPenaResidua.getQuantumArresto()) <= 0) {
					siesLogger.debug("Pena residua nulla o negativa esco");
					break;
				}
			} else if (lDataFinePena.compareTo(lDataScarcerazione) == 0) {
				// uguale = true;
				siesLogger.debug(
						"Data fine rideterminata coincidente con la data di scarcerazione. Quantum trovato!");
				break;
			} else if (lDataFinePena.before(lDataScarcerazione)) {
				siesLogger.debug(
						"Data fine rideterminata (" + DateUtils.getDateToString(lDataFinePena, "dd-MM-yyyy")
								+ ") minore della data di scarcerazione ("
								+ DateUtils.getDateToString(lDataScarcerazione, "dd-MM-yyyy")
								+ "). Tolgo un giorno al residuo.");
				if (maggiore) {
					// condizione per evitare loop
					siesLogger.debug("ERR. Esco per evitare loop");
					// lMessage =
					// "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione
					// richiesta.";
					break;
				}
				minore = true;

				// Fine pena Minore di quanto previsto. Devo aggiungere almeno un GG
				// alla pena resiuda per verificare se ottengo la stessa data fine pena
				PenaResiduaModel lPenaManuale = lCalcoloModelApp.getPenaResiduaManuale();

				lCalRecAncoraDaEspiareAl = lPenaManuale.getQuantumReclusione();
				lCalArrAncoraDaEspiareAl = lPenaManuale.getQuantumArresto();
				int totReclusione = CalendarUtil.getTotGiorni(lCalRecAncoraDaEspiareAl);
				int totArresto = CalendarUtil.getTotGiorni(lCalArrAncoraDaEspiareAl);
				if (totReclusione > 0) {
					totReclusione++;
					lCalRecAncoraDaEspiareAl.setNumGiorni(totReclusione);
					lCalRecAncoraDaEspiareAl.setNumMesi(0);
					lCalRecAncoraDaEspiareAl.setNumAnni(0);
					lCalRecAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalRecAncoraDaEspiareAl);
				} else if (totArresto > 0) {
					totArresto++;
					lCalArrAncoraDaEspiareAl.setNumGiorni(totArresto);
					lCalArrAncoraDaEspiareAl.setNumMesi(0);
					lCalArrAncoraDaEspiareAl.setNumAnni(0);
					lCalArrAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalArrAncoraDaEspiareAl);
				}
				lPenaManuale.setQuantumReclusione(lCalRecAncoraDaEspiareAl);
				lPenaManuale.setQuantumArresto(lCalArrAncoraDaEspiareAl);

				lCalcoloModelApp.setPenaResiduaManuale(lPenaManuale);
			} else if (lDataFinePena.after(lDataScarcerazione)) {
				if (minore) {
					// condizione per evitare loop
					siesLogger.debug("ERR. Esco per evitare loop");
					// lMessage =
					// "Attenzione! Impossibile determinare un Quantum tale da ottenere la Data Scarcerazione
					// richiesta.";
					break;
				}
				siesLogger.debug(
						"Data fine rideterminata (" + DateUtils.getDateToString(lDataFinePena, "dd-MM-yyyy")
								+ ") maggiore della data di scarcerazione ("
								+ DateUtils.getDateToString(lDataScarcerazione, "dd-MM-yyyy")
								+ "). Aggiungo un giorno al residuo.");
				maggiore = true;

				// Data fine calcolata superiore alla data fine pena prevista.
				// I quantum sono eccessivi provo a togliere un GG alla pena da residua ad Oggi
				PenaResiduaModel lPenaManuale = lCalcoloModelApp.getPenaResiduaManuale();

				lCalRecAncoraDaEspiareAl = lPenaManuale.getQuantumReclusione();
				lCalArrAncoraDaEspiareAl = lPenaManuale.getQuantumArresto();
				int totReclusione = CalendarUtil.getTotGiorni(lCalRecAncoraDaEspiareAl);
				int totArresto = CalendarUtil.getTotGiorni(lCalArrAncoraDaEspiareAl);
				if (totReclusione > 0) {
					totReclusione--;
					lCalRecAncoraDaEspiareAl.setNumGiorni(totReclusione);
					lCalRecAncoraDaEspiareAl.setNumMesi(0);
					lCalRecAncoraDaEspiareAl.setNumAnni(0);
					lCalRecAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalRecAncoraDaEspiareAl);
				} else if (totArresto > 0) {
					totArresto--;
					lCalArrAncoraDaEspiareAl.setNumGiorni(totArresto);
					lCalArrAncoraDaEspiareAl.setNumMesi(0);
					lCalArrAncoraDaEspiareAl.setNumAnni(0);
					lCalArrAncoraDaEspiareAl = lCalUtil.ricalcolaGAM(lCalArrAncoraDaEspiareAl);
				}
				lPenaManuale.setQuantumReclusione(lCalRecAncoraDaEspiareAl);
				lPenaManuale.setQuantumArresto(lCalArrAncoraDaEspiareAl);
			}
		}

		// Recupero il quantum della ottenuto dal ciclo di loop
		PenaResiduaModel lPenaManuale = lCalcoloModelApp.getPenaResiduaManuale();

		return lPenaManuale;
	}

	/**
	 *
	 * @param aDBConnection
	 * @param aListaMisure
	 * @param aIdFascicoloCumulante
	 * @throws F3BException
	 */
	private void iscriviProcedimentoDiClasseIV(Connection aDBConnection,
			Vector<MisuraSicurezzaCumuloModel> aListaMisure, EventoModel aEvento,
			PosizioneGiuridicaModel aPosMod, PenaResiduaModel aPenaResidua) throws F3BException {

		Connection lConn = null;

		SoggettoDAO lSoggDao = null;
		SoggettoSqlDAO lSoggSqlDao = null;
		ResidenzaDAO lResidenzaDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		ResidenzaFascicoloSiepDAO lResidenzaFascicoloSiepDAO = null;
		SentenzaRiunitaFascSiepSqlDAO lSentenzaRiunitaFascSiepSqlDao = null;
		SentenzaRiunitaFascSiepDAO lSentenzaRiunitaFascSiepDao = null;
		FascicoloSiepSqlDAO lFascSiepSqlDao = null;
		FascicoloSiepDAO lFascSiepDao = null;
		MisuraSicurezzaDAO lMisuraSicurezzaDao = null;
		FascMsToFascSiepDAO lFascMsToFascSiepDao = null;
		RiferimentoFascicoloSiepDAO lRifFasSiepDao = null;
		AvvocatoFascicoloSiepSqlDAO lAccFascSiepDao = null;
		AvvocatoFascicoloSiepDAO lAvvFasDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PenaResiduaDAO lPenaResiduaDao = null;
		AgdgFascicoloSiepSqlDAO lAgdgFascicoloSiepSqlDao = null;
		AgdgFascicoloSiepDAO lAgdgFascicoloSiepDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		TitoloCumulatoSqlDAO lTitCumSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// Recupero i dati del fascicolo cumulante da duplicare
			lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFascSiepSqlDao.ricercaFascicoloByKey(aEvento.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFascicoloCumulante = (FascicoloSiepModel) lFascSiepSqlDao.getModelByKey();

			// ========================================================================
			// ========================================================================
			// Inizializzo i dati del fascicolo di classe IV
			// ========================================================================
			FascicoloSiepModel lFasClasseIVMod = new FascicoloSiepModel();

			lFasClasseIVMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
			lFasClasseIVMod.setChiaveProgr(null); // Il progressivo del fascicolo (in base all'anno e
													// all'ufficio) viene calcolato dal controller
			lFasClasseIVMod.setChiaveUfficio(aEvento.getCodUfficioInserimento()); // Ufficio dell'operatore
																					// che inserisce

			lFasClasseIVMod.setCodStatoFascicolo("02"); // Stato fascicolo settato ad Iscritto
			lFasClasseIVMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
			lFasClasseIVMod.setCodTipoPosLibero("-"); // '-' per le join

			lFasClasseIVMod.setDataIscrizione(DateUtils.getSysDateAsDate("dd/MM/yyyy"));

			lFasClasseIVMod.setNote(null);

			lFasClasseIVMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
			lFasClasseIVMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della
													// posizione giuridica

			lFasClasseIVMod.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lFasClasseIVMod.setDataInserimento(aEvento.getDataAggiornamento());
			lFasClasseIVMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

			// lFasClasseIVMod.setSogIdSoggetto(null); VA duplicato
			lFasClasseIVMod.setSenIdSentenza(null);

			lFasClasseIVMod.setTipoProgressivo(4); // sempre 4

			// lFasClasseIVMod.setDataArrivoAtto ( );
			// lFasClasseIVMod.setDataIrrevocabilita ( );

			// ========================================================================
			// =====================================
			// Recupero CHIAVE_PROGR da Utilizzare
			// =====================================
			lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFascSiepSqlDao.getProgressivoFascicoloSiep(lFasClasseIVMod);
			lFascSiepSqlDao.start();
			int lMaxProgr = 0;
			if (lFascSiepSqlDao.next() && (lFascSiepSqlDao.getInt("aMAX") > 0))
				lMaxProgr = lFascSiepSqlDao.getInt("aMAX");
			lFascSiepSqlDao.stop();
			siesLogger.debug("lMaxProgr = " + lMaxProgr);

			// Assegnazione automatica
			siesLogger.debug("Assegnazione automatica recupero il progressivo... ");

			int lTipoProgr = lFasClasseIVMod.getTipoProgressivo();
			if (lMaxProgr == 0) {
				lFasClasseIVMod.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
			} else {
				lFasClasseIVMod.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
			}
			siesLogger.debug("Progressivo di inserimento = " + lFasClasseIVMod.getChiaveProgr());

			// ========================================================================
			// Duplicazione Soggetto - Sempre in copia, ogni fascicolo ha il SUO soggetto
			// ========================================================================
			siesLogger.debug("Duplicazione Soggetto...");
			lSoggSqlDao = new SoggettoSqlDAO(lConn);
			lSoggSqlDao.ricercaSoggettoByKey(lFascicoloCumulante.getSogIdSoggetto());

			SoggettoModel lSoggettoOrig = (SoggettoModel) lSoggSqlDao.getModelByKey();
			SoggettoModel lSoggetto = new SoggettoModel(lSoggettoOrig);

			lSoggetto.setDataInserimento(lFasClasseIVMod.getDataInserimento());
			lSoggetto.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
			lSoggetto.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
			lSoggetto.setDataAggiornamento(null);
			lSoggetto.setCodOperatoreAggiornamento(null);
			lSoggetto.setCodUfficioAggiornamento(null);

			lSoggetto.setKeySoggNsc(null);
			lSoggetto.setFlagPresenzaFascicolo(null);

			lSoggDao = new SoggettoDAO(lConn);
			lSoggDao.setDAOFromModel(lSoggetto);
			BigDecimal lkeySoggIV = lSoggDao.insert();

			lFasClasseIVMod.setSogIdSoggetto(lkeySoggIV);

			// ========================================================================
			// Aggancio la Sentenza del Cumulante.
			// ========================================================================
			lFasClasseIVMod.setSenIdSentenza(lFascicoloCumulante.getSenIdSentenza());

			// ================================
			// Inserimento Fascicolo SIEP
			// ================================
			siesLogger.debug("Inserimento Fascicolo di classe IV...");
			lFascSiepDao = new FascicoloSiepDAO(lConn);
			lFascSiepDao.setDAOFromModel(lFasClasseIVMod);

			BigDecimal lkeyFasIV = lFascSiepDao.insert();

			lFasClasseIVMod.setIdFascicoloSiep(lkeyFasIV);

			// Sentenze Riunite
			siesLogger.debug("Acquisizione Sentenze Riunite...");
			Vector<SentenzaRiunitaFascSiepModel> lListaSentenzeRiunite = new Vector<>();
			lSentenzaRiunitaFascSiepSqlDao = new SentenzaRiunitaFascSiepSqlDAO(lConn);

			lSentenzaRiunitaFascSiepSqlDao
					.ricercaSentenzaRiunitaFascSiepByIdFasciolo(lFascicoloCumulante.getIdFascicoloSiep());

			lListaSentenzeRiunite = new Vector<SentenzaRiunitaFascSiepModel>(
					lSentenzaRiunitaFascSiepSqlDao.getModels());
			siesLogger.debug("Sentenze Riunite trovate: " + lListaSentenzeRiunite.size());

			lSentenzaRiunitaFascSiepDao = new SentenzaRiunitaFascSiepDAO(lConn);

			Iterator lSentRiunIter = lListaSentenzeRiunite.iterator();
			while (lSentRiunIter.hasNext()) {
				SentenzaRiunitaFascSiepModel lSenRiuFasSiepModel = null;
				lSenRiuFasSiepModel = (SentenzaRiunitaFascSiepModel) lSentRiunIter.next();

				SentenzaRiunitaModel lSentRiunModel = lSenRiuFasSiepModel.getSentenzaRiunitaModel();
				// Il fascicolo origine punta una sentenza riunita, la faccio puntare anche
				// dal fascicolo di classe IV

				// Inserisco su SENTENZARIUNITA_FASC_SIEP
				siesLogger.debug("Scrivo SENTENZARIUNITA_FASC_SIEP");
				lSenRiuFasSiepModel = new SentenzaRiunitaFascSiepModel();
				lSenRiuFasSiepModel.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
				lSenRiuFasSiepModel.setSenRiuIdSentenzaRiunita(lSentRiunModel.getIdSentenzaRiunita());

				lSentenzaRiunitaFascSiepDao.setDAOFromModel(lSenRiuFasSiepModel);

				BigDecimal lId = lSentenzaRiunitaFascSiepDao.insert();

				siesLogger.debug("lId = " + lId);
				lSentenzaRiunitaFascSiepDao.stop();
			}

			// ========================================================================
			// Altri gradi di Giudizio
			// ========================================================================
			siesLogger.debug("Acquisizione Altri Gradi Di Giudizio...");
			lAgdgFascicoloSiepSqlDao = new AgdgFascicoloSiepSqlDAO(lConn);
			lAgdgFascicoloSiepSqlDao
					.ricercaAgdgFascicoloSiepByIdFasSiep(lFascicoloCumulante.getIdFascicoloSiep());
			Vector<AgdgFascicoloSiepModel> lAltriGradiGiudizio = new Vector(
					lAgdgFascicoloSiepSqlDao.getModels());

			siesLogger.debug("Altri Gradi Di Giudizio trovati: " + lAltriGradiGiudizio.size());

			lAgdgFascicoloSiepDao = new AgdgFascicoloSiepDAO(lConn);
			Iterator lIterAGDG = lAltriGradiGiudizio.iterator();
			while (lIterAGDG.hasNext()) {
				AgdgFascicoloSiepModel lAGDGFasSiepModel = (AgdgFascicoloSiepModel) lIterAGDG.next();

				AltriGradiGiudizioModel lAltriGradiModel = lAGDGFasSiepModel.getAltriGradiGiudizioModel();

				// Inserisco AGDG_FASCICOLO_SIEP
				siesLogger.debug("Sentenza non duplicata, scrivo solo AGDG_FASCICOLO_SIEP");
				lAGDGFasSiepModel = new AgdgFascicoloSiepModel();
				lAGDGFasSiepModel.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
				lAGDGFasSiepModel.setAgdgIdAltrigradigiudizio(lAltriGradiModel.getIdAltrigradigiudizio());

				lAgdgFascicoloSiepDao.setDAOFromModel(lAGDGFasSiepModel);
				BigDecimal lId = lAgdgFascicoloSiepDao.insert();
				lAgdgFascicoloSiepDao.stop();
				siesLogger.debug("lId = " + lId);
			}

			// ================================
			// Duplicazione Residenza
			// ================================
			lResSqlDao = new ResidenzaSqlDAO(lConn);

			// Residenza (al massimo 1)
			lResSqlDao.ricercaResidenzaByFascicolo(lFascicoloCumulante.getIdFascicoloSiep());
			ResidenzaModel lResidenza = (ResidenzaModel) lResSqlDao.getModelByKey();
			lResSqlDao.stop();

			if (lResidenza != null) {
				siesLogger.debug("Inserisco la residenza");
				lResidenzaDao = new ResidenzaDAO(lConn);

				ResidenzaModel lResMod = new ResidenzaModel(lResidenza);

				lResMod.setSogIdSoggetto(lFasClasseIVMod.getSogIdSoggetto());

				lResMod.setDataInserimento(lFasClasseIVMod.getDataInserimento());
				lResMod.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
				lResMod.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
				lResMod.setDataAggiornamento(null);
				lResMod.setCodOperatoreAggiornamento(null);
				lResMod.setCodUfficioAggiornamento(null);

				lResidenzaDao.setDAOFromModel(lResMod);
				BigDecimal lkeyResIV = lResidenzaDao.insert();
				lResidenzaDao.stop();

				// collego la residenza al nuovo fascicolo
				lResidenzaFascicoloSiepDAO = new ResidenzaFascicoloSiepDAO(lConn);

				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lkeyResIV);
				lResFasMod.setDataInizioValidita(lFasClasseIVMod.getDataInserimento());
				lResFasMod.setDataFineValidita(null);

				lResidenzaFascicoloSiepDAO.setDAOFromModel(lResFasMod);
				lResidenzaFascicoloSiepDAO.insert();
				lResidenzaFascicoloSiepDAO.stop();
			}

			// Domicilio (al massimo 1)
			lResSqlDao.ricercaDomicilioByFascicolo(lFascicoloCumulante.getIdFascicoloSiep());
			ResidenzaModel lDomicilio = (ResidenzaModel) lResSqlDao.getModelByKey();

			if (lDomicilio != null) {
				siesLogger.debug("Inserisco il Domicilio");
				lResidenzaDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = new ResidenzaModel(lDomicilio);

				lResMod.setSogIdSoggetto(lFasClasseIVMod.getSogIdSoggetto());

				lResMod.setDataInserimento(lFasClasseIVMod.getDataInserimento());
				lResMod.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
				lResMod.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
				lResMod.setDataAggiornamento(null);
				lResMod.setCodOperatoreAggiornamento(null);
				lResMod.setCodUfficioAggiornamento(null);

				lResidenzaDao.setDAOFromModel(lResMod);
				BigDecimal lkeyResIV = lResidenzaDao.insert();
				lResidenzaDao.stop();

				// collego la residenza al nuovo fascicolo
				lResidenzaFascicoloSiepDAO = new ResidenzaFascicoloSiepDAO(lConn);

				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lkeyResIV);
				lResFasMod.setDataInizioValidita(lFasClasseIVMod.getDataInserimento());
				lResFasMod.setDataFineValidita(null);

				lResidenzaFascicoloSiepDAO.setDAOFromModel(lResFasMod);
				lResidenzaFascicoloSiepDAO.insert();
				lResidenzaFascicoloSiepDAO.stop();
			}

			// ================================
			// Duplicazione Avvocati
			// ================================
			lAccFascSiepDao = new AvvocatoFascicoloSiepSqlDAO(lConn);
			lAccFascSiepDao.ricercaAvvocatiByFascicolo(lFascicoloCumulante.getIdFascicoloSiep());

			Vector<AvvocatoSiepModel> lAvvocati = new Vector<AvvocatoSiepModel>(lAccFascSiepDao.getModels());

			if (lAvvocati != null) {
				siesLogger.debug("Duplicazione Avvocati...");

				lAvvFasDao = new AvvocatoFascicoloSiepDAO(lConn);
				for (int i = 0; i < lAvvocati.size(); i++) {
					AvvocatoSiepModel lAvvSiep = lAvvocati.get(i);

					AvvocatoFascicoloSiepModel lAvvFasMod = lAvvSiep.getAvvocatoFascicoloSiepModel();

					siesLogger.debug("Duplico: " + lAvvFasMod);

					lAvvFasMod.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
					// lAvvFasMod.setDataInizioValidita (aValore); lascio quella del fascicolo di provenienza
					lAvvFasMod.setAvvIdAvvocatoFascicoloSost(null);
					lAvvFasMod.setEveIdEvento(null);

					lAvvFasMod.setDataInserimento(lFasClasseIVMod.getDataInserimento());
					lAvvFasMod.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
					lAvvFasMod.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
					lAvvFasMod.setDataAggiornamento(null);
					lAvvFasMod.setCodOperatoreAggiornamento(null);
					lAvvFasMod.setCodUfficioAggiornamento(null);

					lAvvFasDao.setDAOFromModel(lAvvFasMod);
					lAvvFasDao.insert();
				}
			}

			// ==================================
			// Duplicazione Posizione Giuridica
			// ==================================
			if (aPosMod != null) {
				siesLogger.debug("Duplicazione Posizione Giuridica...");

				PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosMod);

				lPosMod.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
				lPosMod.setIdEventoRiferimento(null);

				lPosMod.setDataInserimento(lFasClasseIVMod.getDataInserimento());
				lPosMod.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
				lPosMod.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
				lPosMod.setDataAggiornamento(null);
				lPosMod.setCodOperatoreAggiornamento(null);
				lPosMod.setCodUfficioAggiornamento(null);

				lPosDao = new PosizioneGiuridicaDAO(lConn);
				lPosDao.setDAOFromModel(lPosMod);
				BigDecimal idPos = lPosDao.insert();
				lPosMod.setIdPosizioneGiuridica(idPos);
			}

			// ==============================================
			// Duplicazione LUOGO DETENZIONE
			// ==============================================
			// FIXME da verificare

			// ==============================================
			// Duplicazione Pena Residua
			// ==============================================
			if (aPenaResidua != null) {
				lPenaResiduaDao = new PenaResiduaDAO(lConn);

				PenaResiduaModel lPenResModel = new PenaResiduaModel(aPenaResidua);

				lPenResModel.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
				lPenResModel.setEveIdEvento(null);
				lPenResModel.setMisAltIdMisuraAlternativa(null);

				lPenResModel.setDataInserimento(lFasClasseIVMod.getDataInserimento());
				lPenResModel.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
				lPenResModel.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
				lPenResModel.setDataAggiornamento(null);
				lPenResModel.setCodOperatoreAggiornamento(null);
				lPenResModel.setCodUfficioAggiornamento(null);

				lPenaResiduaDao.setDAOFromModel(lPenResModel);
				lPenaResiduaDao.insert();
			}

			// ========================================================================
			// Duplicazione Misure di Sicurezza
			// ========================================================================
			siesLogger.debug("Inserimento Misure di Sicurezza...");
			Date lDataIrrevocabilita = null;
			Date lDataArrivoAtto = null;
			// n.b. se esite MS sul cumulante, utilizzo i dati del cumulante

			boolean isMSCumulante = false;

			for (int i = 0; i < aListaMisure.size(); i++) {
				MisuraSicurezzaCumuloModel lMisuraCum = aListaMisure.get(i);
				siesLogger.debug("lMisuraCum = " + lMisuraCum);

				MisuraSicurezzaModel lMisuraNew = new MisuraSicurezzaModel();

				lMisuraNew.setCodNatura(lMisuraCum.getCodNatura());
				lMisuraNew.setCodTipo(lMisuraCum.getCodTipo());

				lMisuraNew.setNumAnni(lMisuraCum.getNumAnni());
				lMisuraNew.setNumMesi(lMisuraCum.getNumMesi());
				lMisuraNew.setNumGiorni(lMisuraCum.getNumGiorni());

				lMisuraNew.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep()); // Collegata al
																							// fascicolo di
																							// classe IV
				lMisuraNew.setEveIdEvento(null);
				lMisuraNew.setFasSiuIdFascicoloSius(null);

				lMisuraNew.setDataInserimento(lFasClasseIVMod.getDataInserimento());
				lMisuraNew.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
				lMisuraNew.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
				lMisuraNew.setDataAggiornamento(null);
				lMisuraNew.setCodOperatoreAggiornamento(null);
				lMisuraNew.setCodUfficioAggiornamento(null);

				lMisuraSicurezzaDao = new MisuraSicurezzaDAO(lConn);
				lMisuraSicurezzaDao.setDAOFromModel(lMisuraNew);
				BigDecimal lIdMisura = lMisuraSicurezzaDao.insert();
				lMisuraSicurezzaDao.stop();

				// Registro il collegamento al titolo esecutivo di origine
				siesLogger.debug("Registro il collegamento al titolo esecutivo di origine");

				// Recupero i dati del Titolo
				lTitCumSqlDao = new TitoloCumulatoSqlDAO(lConn);
				lTitCumSqlDao.ricercaTitoloCumulatoByKey(lMisuraCum.getTitIdTitoloCumulato());
				TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitCumSqlDao.getModelByKey();

				lDataIrrevocabilita = lTitolo.getDataIrrevocabilita();

				// Recupero i dati del Procedimento (se presente)
				lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lMisuraCum.getTitIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcCum = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();

				//
				RiferimentoFascicoloSiepModel lRifMod = new RiferimentoFascicoloSiepModel();
				if (lProcCum != null) {
					if (lProcCum.getChiaveAnnoFasCumulato()
							.compareTo(lFascicoloCumulante.getChiaveAnno()) == 0
							&& lProcCum.getChiaveProgrFasCumulato()
									.compareTo(lFascicoloCumulante.getChiaveProgr()) == 0
							&& lProcCum.getCodUfficioFasCumulato()
									.equals(lFascicoloCumulante.getChiaveUfficio())) {
						isMSCumulante = true;
						// TODO verificare se è il caso di Saltare (continue)
						// Ovvero se la MS è del cumulante va iscritta normalmente non collegandola ad altro
						// titolo
					}

					// Dati del Procedimento
					lRifMod.setAnnoFascicoloSiep(lProcCum.getChiaveAnnoFasCumulato());
					lRifMod.setProgrFascicoloSiep(lProcCum.getChiaveProgrFasCumulato());
					lRifMod.setCodUffFascicoloSiep(lProcCum.getCodUfficioFasCumulato());

					if (lProcCum.getIdFascicoloSiepOrigine() != null) {
						lRifMod.setFasSieIdFascicoloSiep(lProcCum.getIdFascicoloSiepOrigine());
						lRifMod.setFlagMS("N");
					} else
						lRifMod.setFlagMS("M"); //

					// Dati del Titolo
					lRifMod.setCodTipoProvvedimento(lTitolo.getCodTipoProvvedimento());
					lRifMod.setDataProvvedimento(lTitolo.getDataProvvedimento());
					lRifMod.setAnnoProvvedimento(lTitolo.getAnnoSentenza());
					lRifMod.setNumeroProvvedimento(lTitolo.getNumeroSentenza());

					lRifMod.setCodLuogoEmittente(lTitolo.getCodLuogoEmittente());
					lRifMod.setCodTipoAutoritaEmittente(lTitolo.getCodTipoAutoritaEmittente());
					lRifMod.setDataIrrevocabilita(lTitolo.getDataIrrevocabilita());

					lRifMod.setDataInserimento(lFasClasseIVMod.getDataInserimento());
					lRifMod.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
					lRifMod.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());

					// Inserisco
					siesLogger.debug("Inserisco RifFascSiep = " + lRifMod);
					lRifFasSiepDao = new RiferimentoFascicoloSiepDAO(lConn);
					lRifFasSiepDao.setDAOFromModel(lRifMod);
					BigDecimal lIdRif = lRifFasSiepDao.insert();

					// Aggiorna l'id sulla misura
					siesLogger.debug("Aggiorno idRifFasc (" + lIdRif + ") su MS " + lIdMisura);
					lMisuraSicurezzaDao.setFasSieIdFascicoloSiepRif(lIdRif);

					lMisuraSicurezzaDao.setCondizioneUpdate(lIdMisura);

					lMisuraSicurezzaDao.update();
					lMisuraSicurezzaDao.stop();
				} else {
					// Non ho i dati del Fasciolo non posso inserire collegamenti
					siesLogger
							.debug("Procedimento Cumulato non specificato. Non posso creare il collegamnto.");
				}
			}

			// Aggiorno la data arrivo atto e data irrevocabilità
			// isMSCumulante = true; //FIXME da correggere
			if (isMSCumulante) {
				lDataArrivoAtto = lFascicoloCumulante.getDataArrivoAtto();
				lDataIrrevocabilita = lFascicoloCumulante.getDataIrrevocabilita();
			}

			lFascSiepDao.stop();

			lFascSiepDao.setDataArrivoAtto(lDataArrivoAtto);
			lFascSiepDao.setDataIrrevocabilita(lDataIrrevocabilita);

			lFascSiepDao.selCondizioneUpdate(lFasClasseIVMod.getIdFascicoloSiep());

			lFascSiepDao.update();
			lFascSiepDao.stop();

			// =====================================================
			// Collego il fascicolo di Origine al Nuovo fascicolo
			// =====================================================
			siesLogger.debug("Collego il fascicolo di Origine al Nuovo fascicolo");

			FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();

			lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());

			lFascMsToFascSiepModel.setChiaveAnnoSiep(lFasClasseIVMod.getChiaveAnno());
			lFascMsToFascSiepModel.setChiaveProgrSiep(lFasClasseIVMod.getChiaveProgr());
			lFascMsToFascSiepModel.setChiaveUfficioSiep(lFasClasseIVMod.getChiaveUfficio());

			lFascMsToFascSiepModel
					.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_IN_ESECUZIONE_DI);

			// Eventuale data Ultimo Cumulo sul fascicolo di Origine
			lFascMsToFascSiepModel.setDataCumulo(aEvento.getDataEmissione());

			// Fascicolo collegato, quello da cui nasce il fascicolo di esecuzione MS
			lFascMsToFascSiepModel.setFasSieIdFascicoloCollegato(lFascicoloCumulante.getIdFascicoloSiep());
			lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(lFascicoloCumulante.getChiaveAnno());
			lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(lFascicoloCumulante.getChiaveProgr());
			lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(lFascicoloCumulante.getChiaveUfficio());

			// Lego l'iscrizione al messaggio di Richiesta se presente
			lFascMsToFascSiepModel.setMesIdMessaggio(null);

			lFascMsToFascSiepModel.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
			lFascMsToFascSiepModel.setDataInserimento(lFasClasseIVMod.getDataInserimento());
			lFascMsToFascSiepModel.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());

			lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
			lFascMsToFascSiepModel.setDataAggiornamento(null);
			lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

			lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
			lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
			lFascMsToFascSiepDao.insert();
			lFascMsToFascSiepDao.stop();

			// Se stesso ufficio inserisco anche il record per il fascicolo di origine
			lFascMsToFascSiepModel = new FascMsToFascSiepModel();

			// fascicolo di origine
			lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(lFascicoloCumulante.getIdFascicoloSiep());
			lFascMsToFascSiepModel.setChiaveAnnoSiep(lFascicoloCumulante.getChiaveAnno());
			lFascMsToFascSiepModel.setChiaveProgrSiep(lFascicoloCumulante.getChiaveProgr());
			lFascMsToFascSiepModel.setChiaveUfficioSiep(lFascicoloCumulante.getChiaveUfficio());

			// Tipo di relazine
			lFascMsToFascSiepModel
					.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);

			// fascicolo di esecuzione
			lFascMsToFascSiepModel.setFasSieIdFascicoloCollegato(lFasClasseIVMod.getIdFascicoloSiep());
			lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(lFasClasseIVMod.getChiaveAnno());
			lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(lFasClasseIVMod.getChiaveProgr());
			lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(lFasClasseIVMod.getChiaveUfficio());

			lFascMsToFascSiepModel.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());
			lFascMsToFascSiepModel.setDataInserimento(lFasClasseIVMod.getDataInserimento());
			lFascMsToFascSiepModel.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());

			lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
			lFascMsToFascSiepModel.setDataAggiornamento(null);
			lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

			lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
			lFascMsToFascSiepDao.insert();
			lFascMsToFascSiepDao.stop();

			// =========================
			// STATO PROCEDIMENTO
			// =========================
			StatoProcedimentoModel lStat = new StatoProcedimentoModel();

			lStat.setFasSieIdFascicoloSiep(lFasClasseIVMod.getIdFascicoloSiep());
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setCodStatoProcedimento("0108"); // Iscritto

			lStat.setDataInserimento(lFasClasseIVMod.getDataInserimento());
			lStat.setCodUfficioInserimento(lFasClasseIVMod.getCodUfficioInserimento());
			lStat.setCodOperatoreInserimento(lFasClasseIVMod.getCodOperatoreInserimento());

			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();

			// ========================================================================

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("Errore in fase di estrazioned dei dati analitici", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.iscriviProcedimentoDiClasseIV: Non posso inserire: " + ex);
		} catch (F3BException ex) {
			rollback(lConn);
			siesLogger.error("Errore in fase di estrazioned dei dati analitici", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.iscriviProcedimentoDiClasseIV: Non posso inserire: " + ex);
		} finally {
			if (aDBConnection == null)
				cleanup(lConn);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSoggDao);
			cleanup(lSoggSqlDao);
			cleanup(lResidenzaDao);
			cleanup(lResSqlDao);
			cleanup(lResidenzaFascicoloSiepDAO);
			cleanup(lSentenzaRiunitaFascSiepSqlDao);
			cleanup(lSentenzaRiunitaFascSiepDao);
			cleanup(lFascSiepSqlDao);
			cleanup(lFascSiepDao);
			cleanup(lMisuraSicurezzaDao);
			cleanup(lFascMsToFascSiepDao);
			cleanup(lRifFasSiepDao);
			cleanup(lAccFascSiepDao);
			cleanup(lAvvFasDao);
			cleanup(lPosDao);
			cleanup(lPenaResiduaDao);
			cleanup(lAgdgFascicoloSiepSqlDao);
			cleanup(lAgdgFascicoloSiepDao);
			cleanup(lStatoProcDao);
			cleanup(lTitCumSqlDao);
			cleanup(lProcCumSqlDao);
		}
	}

	/**
	 * Aggiunge, su un Classe IV esistente e indicato in fase istruttoria, le MS disposte in cumulo, e crea i
	 * collegamenti tra il cumulante e il classe IV
	 *
	 * @param aDBConnection
	 * @param aListaMisure
	 *            : MS da aggiungere al classe IV
	 * @param aEvento
	 * @param aDatiFinaliModel
	 *            - Preleva l'id del fasciciolo di classe IV
	 * @throws F3BException
	 */
	private void aggiungiAProcedimentoDiClasseIV(Connection aDBConnection,
			Vector<MisuraSicurezzaCumuloModel> aListaMisure, EventoModel aEvento,
			DatiFinaliCumuloModel aDatiFinaliModel) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaDAO lMisuraSicurezzaDao = null;
		FascMsToFascSiepDAO lFascMsToFascSiepDao = null;
		RiferimentoFascicoloSiepDAO lRifFasSiepDao = null;
		TitoloCumulatoSqlDAO lTitCumSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;
		FascicoloSiepSqlDAO lFascSiepSqlDao = null;
		UfficioSqlDAO lUffSqlDao = null;
		FascMsToFascSiepSqlDAO lFascMsToFascSiepSqlDao = null;

		try {
			if (aDBConnection != null) {
				// siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// Recupero il fascicolo classe IV a cui agganciare le Misure
			lFascSiepSqlDao = new FascicoloSiepSqlDAO(lConn);

			lFascSiepSqlDao.ricercaFascicoloByKey(aDatiFinaliModel.getFasSieIdFascicoloSiepMs());
			FascicoloSiepModel lFascModelDaAggiornare = (FascicoloSiepModel) lFascSiepSqlDao.getModelByKey();
			lFascSiepSqlDao.stop();

			// Recupero il fascicolo cumulante a cui collegare il classe IV
			lFascSiepSqlDao.ricercaFascicoloByKey(aEvento.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFascicoloCumulante = (FascicoloSiepModel) lFascSiepSqlDao.getModelByKey();
			lFascSiepSqlDao.stop();

			// ========================================================================
			// Aggiungo le Misure di Sicurezza
			// ========================================================================
			siesLogger.debug("Aggiungo le Misure di Sicurezza...");

			for (int i = 0; i < aListaMisure.size(); i++) {
				MisuraSicurezzaCumuloModel lMisuraCum = aListaMisure.get(i);
				siesLogger.debug("lMisuraCum = " + lMisuraCum);

				// Recupero i dati del Procedimento (se presente)
				lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
				lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lMisuraCum.getTitIdTitoloCumulato());
				ProcedimentoCumulatoModel lProcCum = (ProcedimentoCumulatoModel) lProcCumSqlDao
						.getModelByKey();

				// Devo escludere la MS se già appartenente al fascicolo a cui sto
				// aggiungendo le MS.
				// n.b il lProcCum è in genere in classe I mentre lFascModelDaAggiornare è
				// sicuramente un classe IV.
				// Devo verificare che il lFascModelDaAggiornare non coincida con lProcCum
				// o con il classe IV collegato al lProcCum
				boolean isStessoProcedimento = false;

				if (lProcCum != null
						&& lProcCum.getChiaveAnnoFasCumulato()
								.compareTo(lFascModelDaAggiornare.getChiaveAnno()) == 0
						&& lProcCum.getChiaveProgrFasCumulato()
								.compareTo(lFascModelDaAggiornare.getChiaveProgr()) == 0
						&& lProcCum.getCodUfficioFasCumulato()
								.equals(lFascModelDaAggiornare.getChiaveUfficio())) { // Caso particolare in
																						// cui ha
																						// caricato in cumulo
																						// il
																						// classe IV ed lo ho
																						// selezionato anche
																						// come
																						// fascicolo a cui
																						// aggiungere
																						// le MS
					isStessoProcedimento = true;
				}

				if (lMisuraCum.getAnnoFascicoloSiepIV() != null) { // La MS da caricare è già collegata a un
																	// classe IV, verifico che non
																	// coincida con il classe IV su cui la
																	// devo caricare a seguito del cumulo
					lUffSqlDao = new UfficioSqlDAO(lConn);

					lUffSqlDao.selUfficioByCod(lMisuraCum.getCodAutoritaEmittenteIV());
					UfficioModel lUffFascIV = (UfficioModel) lUffSqlDao.getModelByKey();
					lUffSqlDao.stop();

					if (lMisuraCum.getAnnoFascicoloSiepIV()
							.compareTo(lFascModelDaAggiornare.getChiaveAnno()) == 0
							&& lMisuraCum.getNumeroFascicoloSiepIV()
									.compareTo(lFascModelDaAggiornare.getChiaveProgr()) == 0
							&& lUffFascIV.getCodUfficio().equals(lFascModelDaAggiornare.getChiaveUfficio())) {
						isStessoProcedimento = true;
					}
				}

				if (!isStessoProcedimento) {
					siesLogger.debug("La MS non appartiene al fascicolo di destinazione, la carico...");
					MisuraSicurezzaModel lMisuraNew = new MisuraSicurezzaModel();

					lMisuraNew.setCodNatura(lMisuraCum.getCodNatura());
					lMisuraNew.setCodTipo(lMisuraCum.getCodTipo());

					lMisuraNew.setNumAnni(lMisuraCum.getNumAnni());
					lMisuraNew.setNumMesi(lMisuraCum.getNumMesi());
					lMisuraNew.setNumGiorni(lMisuraCum.getNumGiorni());

					// Collegata al fascicolo di classe IV
					lMisuraNew.setFasSieIdFascicoloSiep(lFascModelDaAggiornare.getIdFascicoloSiep());
					lMisuraNew.setEveIdEvento(null);
					lMisuraNew.setFasSiuIdFascicoloSius(null);

					lMisuraNew.setDataInserimento(aEvento.getDataInserimento());
					lMisuraNew.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
					lMisuraNew.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
					lMisuraNew.setDataAggiornamento(null);
					lMisuraNew.setCodOperatoreAggiornamento(null);
					lMisuraNew.setCodUfficioAggiornamento(null);

					lMisuraSicurezzaDao = new MisuraSicurezzaDAO(lConn);
					lMisuraSicurezzaDao.setDAOFromModel(lMisuraNew);
					BigDecimal lIdMisura = lMisuraSicurezzaDao.insert();
					lMisuraSicurezzaDao.stop();

					// Registro il collegamento al titolo esecutivo di origine
					if (lProcCum != null) {
						siesLogger.debug(
								"Ho lProcCum. Registro il collegamento al titolo esecutivo di origine");

						// Recupero i dati del Titolo
						lTitCumSqlDao = new TitoloCumulatoSqlDAO(lConn);
						lTitCumSqlDao.ricercaTitoloCumulatoByKey(lMisuraCum.getTitIdTitoloCumulato());
						TitoloCumulatoModel lTitolo = (TitoloCumulatoModel) lTitCumSqlDao.getModelByKey();

						RiferimentoFascicoloSiepModel lRifMod = new RiferimentoFascicoloSiepModel();

						// Dati del Procedimento
						lRifMod.setAnnoFascicoloSiep(lProcCum.getChiaveAnnoFasCumulato());
						lRifMod.setProgrFascicoloSiep(lProcCum.getChiaveProgrFasCumulato());
						lRifMod.setCodUffFascicoloSiep(lProcCum.getCodUfficioFasCumulato());

						if (lProcCum.getIdFascicoloSiepOrigine() != null) {
							lRifMod.setFasSieIdFascicoloSiep(lProcCum.getIdFascicoloSiepOrigine());
							lRifMod.setFlagMS("N");
						} else
							lRifMod.setFlagMS("M"); //

						// Dati del Titolo
						lRifMod.setCodTipoProvvedimento(lTitolo.getCodTipoProvvedimento());
						lRifMod.setDataProvvedimento(lTitolo.getDataProvvedimento());
						lRifMod.setAnnoProvvedimento(lTitolo.getAnnoSentenza());
						lRifMod.setNumeroProvvedimento(lTitolo.getNumeroSentenza());

						lRifMod.setCodLuogoEmittente(lTitolo.getCodLuogoEmittente());
						lRifMod.setCodTipoAutoritaEmittente(lTitolo.getCodTipoAutoritaEmittente());
						lRifMod.setDataIrrevocabilita(lTitolo.getDataIrrevocabilita());

						lRifMod.setDataInserimento(aEvento.getDataInserimento());
						lRifMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
						lRifMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

						// Inserisco
						siesLogger.debug("Inserisco RifFascSiep = " + lRifMod);
						lRifFasSiepDao = new RiferimentoFascicoloSiepDAO(lConn);
						lRifFasSiepDao.setDAOFromModel(lRifMod);
						BigDecimal lIdRif = lRifFasSiepDao.insert();

						// Aggiorna l'id sulla misura
						siesLogger.debug("Aggiorno idRifFasc (" + lIdRif + ") su MS " + lIdMisura);
						lMisuraSicurezzaDao.setFasSieIdFascicoloSiepRif(lIdRif);

						lMisuraSicurezzaDao.setCondizioneUpdate(lIdMisura);

						lMisuraSicurezzaDao.update();
						lMisuraSicurezzaDao.stop();
					} else {
						// Non ho i dati del Fasciolo non posso inserire collegamenti
						siesLogger.debug(
								"Procedimento Cumulato non specificato. Non posso creare il collegamnto.");
					}
				} else {
					siesLogger.debug("isStessoProcedimento = " + isStessoProcedimento);
				}

			}

			// ========================================================================
			// Collego il fascicolo Di classe IV come fascicolo di esecuzione del Cumulante
			// Ovvero delle misure disposte sul Cumulante
			// ========================================================================
			siesLogger.debug("Collego il fascicolo di Origine al Nuovo fascicolo");

			//
			// Verifico prima che il Classe IV selezionato non sia già collegato
			// al cumulante per es se sul cumulante era disposta la MS e il PM ha
			// subito generato il classe IV prima di effettuare il cumulo
			//
			// Verifico se il classe IV già punta il cumulante, in caso negativo inserisco
			// il collegamento, altrimenti aggiorno solo DATA_CUMULO
			siesLogger.debug("Verifico se il Classe IV già punta il Cumulante...");
			lFascMsToFascSiepSqlDao = new FascMsToFascSiepSqlDAO(lConn);
			lFascMsToFascSiepSqlDao.ricercaCollegamentiSiep(lFascModelDaAggiornare.getIdFascicoloSiep());

			Vector<FascMsToFascSiepModel> lLista = new Vector<FascMsToFascSiepModel>(
					lFascMsToFascSiepSqlDao.getModels());
			lFascMsToFascSiepSqlDao.stop();

			boolean isClasseIVGiaCollegato = false;
			BigDecimal idFasToMsModel = null;
			if (lLista != null && lLista.size() > 0) {
				for (int i = 0; i < lLista.size(); i++) {
					FascMsToFascSiepModel lFasToMsModel = lLista.elementAt(i);
					if (lFasToMsModel.getFasSieIdFascicoloCollegato()
							.compareTo(lFascicoloCumulante.getIdFascicoloSiep()) == 0) {
						siesLogger.debug("Il classe IV è già collegato al cumulante");
						idFasToMsModel = lFasToMsModel.getIdFascMsToFascSiep();
						isClasseIVGiaCollegato = true;
					}
				}
			}

			if (!isClasseIVGiaCollegato) {
				siesLogger.debug("Cumulante e Classe IV non ancora collegati, procedo al collegamento");
				FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();

				lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(lFascModelDaAggiornare.getIdFascicoloSiep());

				lFascMsToFascSiepModel.setChiaveAnnoSiep(lFascModelDaAggiornare.getChiaveAnno());
				lFascMsToFascSiepModel.setChiaveProgrSiep(lFascModelDaAggiornare.getChiaveProgr());
				lFascMsToFascSiepModel.setChiaveUfficioSiep(lFascModelDaAggiornare.getChiaveUfficio());

				lFascMsToFascSiepModel.setCodTipoRelazioneMS(
						ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_IN_ESECUZIONE_DI);

				// Eventuale data Ultimo Cumulo sul fascicolo di Origine
				lFascMsToFascSiepModel.setDataCumulo(aEvento.getDataEmissione());

				// Fascicolo collegato, quello da cui nasce il fascicolo di esecuzione MS
				lFascMsToFascSiepModel
						.setFasSieIdFascicoloCollegato(lFascicoloCumulante.getIdFascicoloSiep());
				lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(lFascicoloCumulante.getChiaveAnno());
				lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(lFascicoloCumulante.getChiaveProgr());
				lFascMsToFascSiepModel.setChiaveUfficioSiepCollegato(lFascicoloCumulante.getChiaveUfficio());

				// Lego l'iscrizione al messaggio di Richiesta se presente
				lFascMsToFascSiepModel.setMesIdMessaggio(null);

				lFascMsToFascSiepModel.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lFascMsToFascSiepModel.setDataInserimento(aEvento.getDataInserimento());
				lFascMsToFascSiepModel.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
				lFascMsToFascSiepModel.setDataAggiornamento(null);
				lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

				lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);
				lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
				lFascMsToFascSiepDao.insert();
				lFascMsToFascSiepDao.stop();
			} else {
				// dovrei aggiornare FASC_MS_TO_FASC_SIEP.DATA_CUMULO
				siesLogger.debug("Aggiorno data cumulo id=" + idFasToMsModel);
				lFascMsToFascSiepDao = new FascMsToFascSiepDAO(lConn);

				lFascMsToFascSiepDao.setDataCumulo(aEvento.getDataEmissione());

				lFascMsToFascSiepDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreInserimento());
				lFascMsToFascSiepDao.setDataAggiornamento(aEvento.getDataInserimento());
				lFascMsToFascSiepDao.setCodUfficioAggiornamento(aEvento.getCodUfficioInserimento());

				lFascMsToFascSiepDao.setCondizioneUpdate(idFasToMsModel);

				lFascMsToFascSiepDao.update();
				lFascMsToFascSiepDao.stop();
			}

			// ========================================================================
			// Verifico se il cumulante già punta il classe IV, in caso negativo inserisco
			// il collegamento
			// ========================================================================
			siesLogger.debug("Verifico il cumulante già punta il classe IV...");

			lFascMsToFascSiepSqlDao = new FascMsToFascSiepSqlDAO(lConn);
			lFascMsToFascSiepSqlDao.ricercaCollegamentiSiep(lFascicoloCumulante.getIdFascicoloSiep());

			lLista = new Vector<FascMsToFascSiepModel>(lFascMsToFascSiepSqlDao.getModels());
			boolean isCumulanteGiaCollegato = false;
			if (lLista != null && lLista.size() > 0) {
				for (int i = 0; i < lLista.size(); i++) {
					FascMsToFascSiepModel lFasToMsModel = lLista.elementAt(i);
					if (lFasToMsModel.getFasSieIdFascicoloCollegato()
							.compareTo(lFascModelDaAggiornare.getIdFascicoloSiep()) == 0) {
						siesLogger.debug("Il cumulante è già collegato al classe IV");
						isCumulanteGiaCollegato = true;
					}
				}
			}

			if (!isCumulanteGiaCollegato) {
				// ========================================================================
				// Collego il Cumulante al facicolo di classe IV
				// ========================================================================
				siesLogger.debug("Collego il Cumulante al facicolo di classe IV");
				FascMsToFascSiepModel lFascMsToFascSiepModel = new FascMsToFascSiepModel();

				// fascicolo di origine
				lFascMsToFascSiepModel.setFasSieIdFascicoloSiep(lFascicoloCumulante.getIdFascicoloSiep());
				lFascMsToFascSiepModel.setChiaveAnnoSiep(lFascicoloCumulante.getChiaveAnno());
				lFascMsToFascSiepModel.setChiaveProgrSiep(lFascicoloCumulante.getChiaveProgr());
				lFascMsToFascSiepModel.setChiaveUfficioSiep(lFascicoloCumulante.getChiaveUfficio());

				// Tipo di relazine
				lFascMsToFascSiepModel
						.setCodTipoRelazioneMS(ICostantiMisuraSicurezza.COD_TIPO_RELAZIONE_MS_ISCRITTO_AL);

				// fascicolo di esecuzione
				lFascMsToFascSiepModel
						.setFasSieIdFascicoloCollegato(lFascModelDaAggiornare.getIdFascicoloSiep());
				lFascMsToFascSiepModel.setChiaveAnnoSiepCollegato(lFascModelDaAggiornare.getChiaveAnno());
				lFascMsToFascSiepModel.setChiaveProgrSiepCollegato(lFascModelDaAggiornare.getChiaveProgr());
				lFascMsToFascSiepModel
						.setChiaveUfficioSiepCollegato(lFascModelDaAggiornare.getChiaveUfficio());

				lFascMsToFascSiepModel.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lFascMsToFascSiepModel.setDataInserimento(aEvento.getDataInserimento());
				lFascMsToFascSiepModel.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());

				lFascMsToFascSiepModel.setCodOperatoreAggiornamento(null);
				lFascMsToFascSiepModel.setDataAggiornamento(null);
				lFascMsToFascSiepModel.setCodUfficioAggiornamento(null);

				lFascMsToFascSiepDao.setDAOFromModel(lFascMsToFascSiepModel);
				lFascMsToFascSiepDao.insert();
				lFascMsToFascSiepDao.stop();
			}

			// ========================================================================
			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("Errore in fase di aggiornamento collegamento Cumulante-ClasseIV", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.aggiungiAProcedimentoDiClasseIV: Non posso inserire: " + ex);
		} catch (F3BException ex) {
			rollback(lConn);
			siesLogger.error("Errore in fase di aggiornamento collegamento Cumulante-ClasseIV", ex);
			throw new F3BException(
					"DatiFinaliCumuloController.aggiungiAProcedimentoDiClasseIV: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisuraSicurezzaDao);
			cleanup(lFascMsToFascSiepDao);
			cleanup(lFascMsToFascSiepSqlDao);
			cleanup(lRifFasSiepDao);
			cleanup(lTitCumSqlDao);
			cleanup(lProcCumSqlDao);
			cleanup(lFascSiepSqlDao);
			cleanup(lUffSqlDao);

			if (aDBConnection == null)
				cleanup(lConn);
		}
	}
	// MEV 26 Cumulo Step2

	// La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce, ma inserendo
	// il valore della Primary_Key è già preimpostato;
	// Questo metodo è usato nella funzione di presa in carico, per scaricare Tutti i dati del Fascicolo sulla
	// nuova Base dati.
	//
	public String ExInserisciDatiFinaliCumuloWithoutSequence(DatiFinaliCumuloModel aDatiFinaliCumulo,
			Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		DatiFinaliCumuloDAO lDatiFinCumDao = null;
		// DatiFinaliCumuloModel lDatiFin = null;

		try {
			lDatiFinCumDao = new DatiFinaliCumuloDAO(lConn);

			if (aDatiFinaliCumulo != null && aDatiFinaliCumulo.getIdDatiFinaliCumulo() != null) {
				lDatiFinCumDao.setDAOFromModel(aDatiFinaliCumulo);
				lDatiFinCumDao.setWithoutSequence(true);
				lDatiFinCumDao.insert();
				lDatiFinCumDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("DatiFinali_Cumulo gia' presente...>"
						+ aDatiFinaliCumulo.getIdDatiFinaliCumulo() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire DatiFinali_CUMULO! ");
			}
		} finally {
			cleanup(lDatiFinCumDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciDatiFinaliCumuloWithoutSequence()

	public String ExInserisciPosizioneGiuridicaCumuloWithoutSequence(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo, Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		PosizioneGiuridicaCumuloDAO lPosCumDao = null;
		// PosizioneGiuridicaCumuloModel lPosizione = null;

		try {
			lPosCumDao = new PosizioneGiuridicaCumuloDAO(lConn);

			if (aPosizioneGiuridicaCumulo != null
					&& aPosizioneGiuridicaCumulo.getIdPosizioneGiuridicaCum() != null) {
				lPosCumDao.setDAOFromModel(aPosizioneGiuridicaCumulo);
				lPosCumDao.setWithoutSequence(true);
				lPosCumDao.insert();
				lPosCumDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("Posizione Giuridica Cumulo gia' presente...>"
						+ aPosizioneGiuridicaCumulo.getIdPosizioneGiuridicaCum() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Posizione Giuridica Cumulo! ");
			}
		} finally {
			cleanup(lPosCumDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciPosizioneGiuridicaCumuloWithoutSequence()

	public String ExInserisciPeneRideterminateCumuloWithoutSequence(
			PenaRideterminataCumuloModel aPenaRidetCumModel, Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		PenaRideterminataCumuloDAO lPeneCumDao = null;

		try {
			lPeneCumDao = new PenaRideterminataCumuloDAO(lConn);

			if (aPenaRidetCumModel != null && aPenaRidetCumModel.getIdPenaRideterminataCumulo() != null) {
				lPeneCumDao.setDAOFromModel(aPenaRidetCumModel);
				lPeneCumDao.setWithoutSequence(true);
				lPeneCumDao.insert();
				lPeneCumDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("Pena Rideterminata Cumulo gia' presente...>"
						+ aPenaRidetCumModel.getIdPenaRideterminataCumulo() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Pena Rideterminata Cumulo! ");
			}
		} finally {
			cleanup(lPeneCumDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciPeneRideterminateCumuloWithoutSequence

	public String ExInserisciUlerioriSanzioniCumuloWithoutSequence(
			DatiFinaliUlterioriSanzioniModel aDatiFinaliUlterioriSanzioniModel, Connection lConn)
			throws F3BException {

		String EsitodiRitorno = "00000";
		DatiFinaliUlterioriSanzioniDAO lUlterioriDao = null;

		try {
			lUlterioriDao = new DatiFinaliUlterioriSanzioniDAO(lConn);

			if (aDatiFinaliUlterioriSanzioniModel != null
					&& aDatiFinaliUlterioriSanzioniModel.getIdDatiFinaliUlterioriSanz() != null) {
				lUlterioriDao.setDAOFromModel(aDatiFinaliUlterioriSanzioniModel);
				lUlterioriDao.setWithoutSequence(true);
				lUlterioriDao.insert();
				lUlterioriDao.stop();
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.warn("Dati Finali Ulteriori Sanzioni Cumulo gia' presente...>"
						+ aDatiFinaliUlterioriSanzioniModel.getIdDatiFinaliUlterioriSanz() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire Dati Finali Ulteriori Sanzioni Cumulo! ");
			}
		} finally {
			cleanup(lUlterioriDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciUlerioriSanzioniCumuloWithoutSequence

	/**
	 * Verifica se il fascicoli in input può essere o meno archiviato a seguito del cumulo. Le condizioni di
	 * archiviabilità sono: [da terminare]
	 *
	 * @param aIdFascicoloSIEP
	 * @return
	 * @deprecated da finire
	 */
	// private boolean isDaArchiviare (BigDecimal aIdFascicoloSIEP, Connection aConn) {
	// private boolean isDaArchiviare(TitoloCumulatoModel aTitoloCumulato,
	// DatiFinaliCumuloModel aDatiFinaliModel, Connection aConn) throws F3BException {
	// boolean isDaArchiviare = false;
	//
	// try {
	// if (aTitoloCumulato.getProcedimentoCumulato().getIdFascicoloSiepOrigine()
	// .compareTo(aDatiFinaliModel.getFasSieIdFascicoloSiepMs()) == 0)
	// isDaArchiviare = true;
	//
	// // } catch (DAOException daoEx) {
	// } catch (Exception ex) {
	//
	// } finally {
	// // cleanup(lUlterioriDao);
	// }
	//
	// return isDaArchiviare;
	// }

	/*
	 * ISSUE MAC : aggiunto metodo che aggiorna il flag altra causa (posizione giuridica) sul fascicolo
	 * Numero MAC : 20191128013 
	 * Autore : monica 
	 * Data : 19/dic/2019 
	 * Branch : 11.2.4
	 */
	/**
	 * Aggiorna il flag altra causa sul fascicolo
	 */
	public void ExUpdateFlagAltraCausaFascicolo(FascicoloSiepModel aFascicolo, String flagAltraCausa)
			throws F3BException {

		Connection lConn = null;

		EventoSqlDAO lEveSqlDao = null;
		FascicoloSiepDAO lFascDao = null;
		FascicoloSiepSqlDAO lFascSqlDao = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Aggiorna flag altra causa sul Fascicolo_model
			// ========================================================================
			// Aggiorno lo FASCICOLO_SIEP.FLAG_ALTRA_CAUSA
			// ========================================================================
			siesLogger.debug("Aggiorno FASCICOLO_SIEP.FLAG_ALTRA_CAUSA = " + flagAltraCausa);

			lFascSqlDao = new FascicoloSiepSqlDAO(lConn);
			lFascSqlDao.ricercaFascicoloByKey(aFascicolo.getIdFascicoloSiep());
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFascSqlDao.getModelByKey();
			lFascSqlDao.stop();

			lFascDao = new FascicoloSiepDAO(lConn);
			lFascDao.selCondizioneUpdate(lFasModel.getIdFascicoloSiep());
			// lFascDao.setDAOFromModel(lFasModel);
			lFascDao.setCodOperatoreAggiornamento(aFascicolo.getCodOperatoreAggiornamento());
			lFascDao.setCodUfficioAggiornamento(aFascicolo.getCodUfficioAggiornamento());
			lFascDao.setDataAggiornamento(aFascicolo.getDataAggiornamento());
			lFascDao.setFlagAltraCausa(flagAltraCausa);
			lFascDao.update();
			commit(lConn);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("DatiFinaliCumuloController.ExUpdateFlagAltraCausaFascicolo : " + ex);
		} finally {
			cleanup(lFascDao);
			cleanup(lFascSqlDao);
			cleanup(lEveSqlDao);

			cleanup(lConn);
		}
	} // CHIUDE ExUpdateFlagAltraCausaFascicolo()
	// ***** FINE INTERVENTO 20191128013 *****//

}