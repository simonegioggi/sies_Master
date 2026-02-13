package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.jms.messaggio.dao.MessaggioSqlDAO;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.annotazioneesitotrasmissione.dao.AnnotazioneEsitoTrasmissioneDAO;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.beneficio.dao.BeneficioSqlDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.dao.CircostanzaSqlDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.competenza.dao.CompetenzaDAO;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.continuazione.dao.ContinuazioneSqlDAO;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.dao.IstruttoriaCumuloDAO;
import siap.siep.istruttoriacumulo.dao.IstruttoriaCumuloSqlDAO;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misurasicurezza.dao.FascMsToFascSiepSqlDAO;
import siap.siep.misurasicurezza.dao.MisuraSicurezzaSqlDAO;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.modulocumulo.dao.BeneficioCumuloDAO;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ModuloCumuloSqlDao;
import siap.siep.modulocumulo.dao.NotificaCumuloDAO;
import siap.siep.modulocumulo.dao.NotificaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoDAO;
import siap.siep.modulocumulo.dao.SoggettoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.ModuloCumuloUtils;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.penaaccessoria.dao.PenaAccessoriaSqlDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaSqlDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel;
import siap.siep.sollecitoesitotrasmissione.dao.SollecitoEsitoTrasmissioneDAO;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.tipologiaorario.dao.TipologiaOrarioDAO;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ModuloCumuloController - Classe Controller contenente i metodi per l'estrazione dei dati analitici
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModuloCumuloController extends TitoloEsecutivoController implements IModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Estrae i dati di FascicoloSiep, Sentenza e Soggetto creando le entità TitoloCumulato,
	 * ProcedimentoCumulato e SoggettoCumulato e collegandole all'istruttoria indicata
	 *
	 * @param aIdIstruttoriaCumulo
	 *            - Id Istruttoria a cui collegare i dati
	 * @param aIdFascicoloSiep
	 *            - idFascicoloSiep da cui prelevare i dati
	 * @param aDBConnection
	 * @param aIdMessaggio
	 * @param aTipoIscrizione
	 * @throws F3BException
	 */
	public void ExInserisciTitoloInIstruttoria(BigDecimal aIdIstruttoriaCumulo, BigDecimal aIdFascicoloSiep,
			DatiOperazioneModel aDatoOpModel, Connection aDBConnection, BigDecimal aIdMessaggio,
			String aTipoIscrizione) throws F3BException {

		Connection lConn = null;

		FascicoloSiepSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		SentenzaSqlDAO lSentDao = null;
		TitoloCumulatoDAO lTitoloCumDao = null;
		ProcedimentoCumulatoDAO lProcCumDao = null;
		SoggettoCumulatoDAO lSoggCumDao = null;
		UfficioSqlDAO lUffSqlDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaCumuloDAO lPosCumDao = null;
		EventoSqlDAO lEveSqlDao = null;
		MisuraAlternativaSqlDAO lMisSqlDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		IstruttoriaCumuloSqlDAO lIstruttoraSqlDao = null;
		ContinuazioneCumuloSqlDAO lContinCunSqlDao = null;
		ContinuazioneCumuloDAO lContinuaDao = null;

		try {
			// FIXME Transazione: la connessione non può più arrivare in input. Ogni titolo va caricato
			// in autonomia e committato separatamente. L'errore in fase di caricamento di un titolo
			// NON può bloccare, ma va solo segnalata.

			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lSentDao = new SentenzaSqlDAO(lConn);

			// ===============================================================
			// Prelevo i dati del FASCICOLO_SIEP
			// ===============================================================
			lFascDao.ricercaFascicoloByKey(aIdFascicoloSiep);
			FascicoloSiepModel lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();
			String lFascicoloStr = lFascicolo.getChiaveAnno() + "/" + lFascicolo.getChiaveProgr() + " - "
					+ lFascicolo.getDescrTipoUfficio() + " di " + lFascicolo.getDescrComuneUfficio();

			ProcedimentoCumulatoModel lProcCumModel = new ProcedimentoCumulatoModel(lFascicolo);

			// ufficio
			lUffSqlDao = new UfficioSqlDAO(lConn);
			lUffSqlDao.ricercaUfficioByCod(lFascicolo.getChiaveUfficio());
			UfficioModel lUfficio = (UfficioModel) lUffSqlDao.getModelByKey();
			lProcCumModel.setCodTipoUfficioFasCumulato(lUfficio.getCodTipoUfficio());
			lProcCumModel.setCodLuogoUfficioFasCumulato(lUfficio.getCodComune());

			lProcCumModel.setFlagStato("E"); // Estratto
			lProcCumModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
			lProcCumModel.setDataInserimento(aDatoOpModel.getData());
			lProcCumModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());

			// lProcCumModel.setDataRichiestaFascicolo
			// lProcCumModel.setDataPervenimentoFascicolo

			// =============================================================
			// Prelevo e duplico i dati della SENTENZA in TITOLO_CUMULATO
			// =============================================================
			lSentDao.ricercaSentenzaBykey(lFascicolo.getSenIdSentenza());
			SentenzaModel lSentMod = (SentenzaModel) lSentDao.getModelByKey();

			//
			TitoloCumulatoModel lTitoloModel = new TitoloCumulatoModel(lSentMod);

			lTitoloModel.setDataIrrevocabilita(lFascicolo.getDataIrrevocabilita());
			if (lTitoloModel.getDataIrrevocabilita() == null
					&& ("03".equals(lTitoloModel.getCodTipoProvvedimento())
							|| "02".equals(lTitoloModel.getCodTipoProvvedimento()))
					&& (lFascicolo.getClasseProcedimento() == 4)) {
				// Se MS iscritta fuori sentenza, la data irrevocabilità non
				// è presente sul fascicolo. Prendo in considerazione la data
				// provvedimento necessario per l'ordinamento cronologico
				// dei titoli
				lTitoloModel.setDataIrrevocabilita(lSentMod.getDataProvvedimento());
			}

			lTitoloModel.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);
			lTitoloModel.setFlagStato("E"); // Estratto
			lTitoloModel.setFlagEscluso("N");

			lTitoloModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
			lTitoloModel.setDataInserimento(aDatoOpModel.getData());
			lTitoloModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
			// aTipoIscrizione: 00 = Titolo principale - 01 = Iscrizione da presa in carico - 02= Iscrizione
			// Manuale - 03 = da NSC - 04 = Iscrizione Proprio Ufficio
			lTitoloModel.setTipoIscrizione(aTipoIscrizione);

			if (aIdMessaggio != null)
				lTitoloModel.setMessIdMessaggio(aIdMessaggio);

			// ===============================================================
			// Prelevo e duplico i dati di SOGGETTO in SOGGETTO_CUMULATO
			// ===============================================================
			lSoggDao.ricercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
			SoggettoModel lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

			SoggettoCumulatoModel lSoggCumModel = new SoggettoCumulatoModel(lSoggMod);

			lSoggCumModel.setFlagStato("E"); // Estratto
			lSoggCumModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
			lSoggCumModel.setDataInserimento(aDatoOpModel.getData());
			lSoggCumModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());

			// Iscrivo il TITOLO_CUMULATO
			lTitoloCumDao = new TitoloCumulatoDAO(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Iscrivo il TITOLO_CUMULATO = " + lTitoloModel);
			lTitoloCumDao.setDAOFromModel(lTitoloModel);
			BigDecimal idTitoloCum = lTitoloCumDao.insert();
			lTitoloModel.setIdTitoloCumulato(idTitoloCum);
			lTitoloCumDao.stop();

			// Iscrivo il PROCEDIMENTO_CUMULATO
			lProcCumDao = new ProcedimentoCumulatoDAO(lConn);

			lProcCumModel.setTitIdTitoloCumulato(lTitoloModel.getIdTitoloCumulato());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Iscrivo il PROCEDIMENTO_CUMULATO = " + lProcCumModel);
			lProcCumDao.setDAOFromModel(lProcCumModel);
			BigDecimal lIdProcCum = lProcCumDao.insert();
			lProcCumModel.setIdProcedimentoCumulato(lIdProcCum);
			lProcCumDao.stop();

			// Iscrivo il SOGGETTO_CUMULATO
			lSoggCumDao = new SoggettoCumulatoDAO(lConn);

			lSoggCumModel.setTitIdTitoloCumulato(lTitoloModel.getIdTitoloCumulato());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Iscrivo il SOGGETTO_CUMULATO = " + lSoggCumModel);
			lSoggCumDao.setDAOFromModel(lSoggCumModel);
			BigDecimal lIdSoggetto = lSoggCumDao.insert();
			lSoggCumModel.setIdSoggettoCumulato(lIdSoggetto);
			lSoggCumDao.stop();

			// ===========================================================
			// Estraggo i dati analitici e li collego al TITOLO_CUMULATO
			// ===========================================================
			ExEstraiDatiAnalitici(aIdFascicoloSiep, lTitoloModel, lConn);

			// Verifico se esistono in istruttoria TITOLI che hanno un record CONTINUAZIONE_CUMULO
			// che potrebbe referenziare il titolo corrente. In questo caso aggiorno il puntamento
			// CONTINUAZIONE_CUMULO.TIT_ID_TITOLO_CUMULATO_CONT = id titolo corrente
			// asdsad
			siesLogger.debug("Ricerco se presenti continuazioni che puntano il titolo che sto caricando...");
			lContinCunSqlDao = new ContinuazioneCumuloSqlDAO(lConn);
			lContinCunSqlDao.ricercaContinuazioneByIdIstruttoria(aIdIstruttoriaCumulo);
			Vector<ContinuazioneCumuloModel> lListaContinuazioni = new Vector<ContinuazioneCumuloModel>(
					lContinCunSqlDao.getModels());
			siesLogger.debug("Continuazioni totali trovate: " + lListaContinuazioni.size());
			for (ContinuazioneCumuloModel lContinua : lListaContinuazioni) {
				if (lContinua.isStessoTitolo(lTitoloModel)) {
					siesLogger.debug("Stesso titolo: " + lTitoloModel.getIdTitoloCumulato());
					siesLogger.debug(
							"Aggiorno il record Continuazione con id: " + lContinua.getIdContinuazioneCum());
					lContinuaDao = new ContinuazioneCumuloDAO(lConn);
					lContinuaDao.setTitIdTitoloCumulatoCont(lTitoloModel.getIdTitoloCumulato());
					lContinuaDao.setCondizioneUpdate(lContinua.getIdContinuazioneCum());
					lContinuaDao.update();
					lContinuaDao.stop();
				}
			}
			lContinCunSqlDao.stop();

			// PATCH Collaudo. Committo subito l'iscrizione del fascicolo con almeno i
			// dati analitici in modo che se fallisce il caricamento dello
			// stato esecuzione e/o altri dati, almeno l'utente non deve iscrivere.
			//
			siesLogger.debug("Prima COMMIT Titolo e Dati analitici = " + lFascicoloStr);
			commit(lConn);

			// ===========================================================
			// Estraggo lo stato esecuzione e li collego al TITOLO_CUMULATO
			// ===========================================================
			if (ModuloCumuloUtils.isMev42Abilitata()) {
				siesLogger.debug("Caricamento Stato esecuzione ABILITATO: procedo...");
				ExEstraiStatoEsecuzione(aIdFascicoloSiep, lTitoloModel, lConn, aDatoOpModel);
			} else {
				siesLogger.debug("Caricamento Stato esecuzione DISABILITATO.");
			}

			// ===========================================================
			// Se la pena è in esecuzione (data Fine > data Odierna)
			// Carico anche l'espiazione ATTUALE
			// ===========================================================
			// Pena Residua
			// FIXME Transazione: mettere in try catch ESPIAZIONE ATTUALE
			try {
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aIdFascicoloSiep);
				PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

				if (lPenResMod != null)
					siesLogger.debug("Data Fine Pena = " + lPenResMod.getDataFine());

				lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
				lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicoloSiep);
				PosizioneGiuridicaModel lPosMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

				// Mac -28/02/2019 Se la PG non è gestita in DatiFinaliCumulo non va
				// caricata l'espiazione pregressa in quanto andrebbe in errore
				// la modifica dell'espiazione attuale e il caricamento nella PG finale
				boolean isPosGiuGestita = false;
				if (lPosMod != null && lPosMod.getCodPosizioneGiuridica() != null) {
					String lCodPGDaEstrarre = lPosMod.getCodPosizioneGiuridica();
					siesLogger.debug("Verifico se la PG [" + lCodPGDaEstrarre + "] è gestita...");

					if (!isPosGiuGestita) {
						Collection lPGLib = DecodificheManager.getInstance()
								.getPosizioniGiuridicheCumLibero();
						Iterator itx = lPGLib.iterator();
						while (itx.hasNext()) {
							DecodeModel decodeModel = (DecodeModel) itx.next();
							if (lCodPGDaEstrarre.equals(decodeModel.getCode())) {
								siesLogger.debug("Gestita in PG Lib! ");
								isPosGiuGestita = true;
							}
						}
					}

					if (!isPosGiuGestita) {
						Collection lPGEspIst = DecodificheManager.getInstance()
								.getPosizioniGiuridicheCumEspIst();
						Iterator itx = lPGEspIst.iterator();
						while (itx.hasNext()) {
							DecodeModel decodeModel = (DecodeModel) itx.next();
							if (lCodPGDaEstrarre.equals(decodeModel.getCode())) {
								siesLogger.debug("Gestita in PG EspIst! ");
								isPosGiuGestita = true;
							}
						}
					}

					if (!isPosGiuGestita) {
						Collection lPGEspAltro = DecodificheManager.getInstance()
								.getPosizioniGiuridicheCumEspAltro();
						Iterator itx = lPGEspAltro.iterator();
						while (itx.hasNext()) {
							DecodeModel decodeModel = (DecodeModel) itx.next();
							if (lCodPGDaEstrarre.equals(decodeModel.getCode())) {
								siesLogger.debug("Gestita in PG EspAltro! ");
								isPosGiuGestita = true;
							}
						}
					}
				}

				if (isPosGiuGestita) {
					siesLogger.debug("PG Gestita, procedo alla verifica dell'espiato! ");
					if ((lPenResMod != null && lPenResMod.getDataFine() != null
							&& !DateUtils.isGreater(DateUtils.getSysDate(), lPenResMod.getDataFine())
							// Controllo se decorrenza futura
							&& lPenResMod.getDataInizio() != null
							&& !DateUtils.isGreater(lPenResMod.getDataInizio(), DateUtils.getSysDate()))
							|| ("16".equals(lPosMod.getCodPosizioneGiuridica()) // diff
									|| "17".equals(lPosMod.getCodPosizioneGiuridica()) // diff prov
							)) {
						siesLogger.debug("Pena in esecuzione o differita recupero i dati");

						PosizioneGiuridicaCumuloModel lPosGiuCum = new PosizioneGiuridicaCumuloModel();

						lPosGiuCum.setCodPosizioneGiuridica(lPosMod.getCodPosizioneGiuridica());

						// FIX: è la data di inizio delle pena e non della PG.
						// lPosGiuCum.setDataInizio(lPosMod.getDataInizio());
						lPosGiuCum.setDataInizio(lPenResMod.getDataInizio());

						lPosGiuCum.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);
						lPosGiuCum.setTitIdTitoloCumulato(lTitoloModel.getIdTitoloCumulato());

						lPosGiuCum.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
						lPosGiuCum.setDataInserimento(aDatoOpModel.getData());
						lPosGiuCum.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());

						if (lPosMod.getLuogoProvaAffidamento() != null
								&& !"".equals(lPosMod.getLuogoProvaAffidamento()))
							lPosGiuCum.setAltroLuogo(lPosMod.getLuogoProvaAffidamento());
						else if (lPosMod.getLuogoLavoroSemiliberta() != null
								&& !"".equals(lPosMod.getLuogoLavoroSemiliberta()))
							lPosGiuCum.setAltroLuogo(lPosMod.getLuogoLavoroSemiliberta());

						// FIXME Provare a recuperare le ulteriori informazione previste per le
						// diverse PG
						// - Luogo detenzione
						// - Se differimento (16-17) Provare a recuperare gli estremi di provvedimenti di
						// concessione TDS/UDS
						// - Se misure - Estremi del provvedimento di concessione
						//
						// -- Se su PG è presente ID_EVENTO_RIFERIMENTO
						//
						List lPGSorv = Arrays.asList("12", "13", "14", "54", "29", "16", "17", "31", "32",
								"33");

						if (lPGSorv.contains(lPosMod.getCodPosizioneGiuridica())) {
							siesLogger.debug(
									"Soggetto 'in Misura' recupero se possibile gli estremi del provvedimento della SORV");
							if (lPosMod.getIdEventoRiferimento() != null) {
								lEveSqlDao = new EventoSqlDAO(lConn);
								lEveSqlDao.ricercaEventoByKey(lPosMod.getIdEventoRiferimento());

								EventoModel lEvePG = (EventoModel) lEveSqlDao.getModelByKey();
								lEveSqlDao.stop();

								if (lEvePG != null) {
									siesLogger.debug("Trovato Evento PG: " + lEvePG.getDescrMotivo());

									if (lEvePG.getEveIdEvento() != null) {
										lEveSqlDao.ricercaEventoByKey(lEvePG.getEveIdEvento());
										EventoModel lEveSorv = (EventoModel) lEveSqlDao.getModelByKey();
										lEveSqlDao.stop();

										if (lEveSorv != null) {
											siesLogger.debug("Trovato evento Sorveglianza");

											siesLogger.debug("Recupero Mis Alt");
											lMisSqlDao = new MisuraAlternativaSqlDAO(lConn);
											lMisSqlDao.ricercaMisuraAlternativaByIdEvento(
													lEveSorv.getIdEvento());
											MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisSqlDao
													.getModelByKey();

											if (lMisMod != null) {
												siesLogger.debug("Recupero dati da MA id: "
														+ lMisMod.getIdMisuraAlternativa());

												siesLogger.debug("Setto data inizio Misura "
														+ lMisMod.getDataInizioMisura());
												lPosGiuCum.setDataInizioMisura(lMisMod.getDataInizioMisura());

												lPosGiuCum.setCodTipoProvvedimento(
														lMisMod.getCodTipoDecisione());

												lPosGiuCum.setChiaveUffFasSius(
														lMisMod.getChiaveUfficioFascicoloSius());

												lPosGiuCum.setChiaveAnnoFasSius(
														lMisMod.getChiaveAnnoFascicoloSius());
												lPosGiuCum.setChiaveProgrFasSius(
														lMisMod.getChiaveProgrFascicoloSius());

												lPosGiuCum.setAnnoRegistro(lMisMod.getAnnoRegistro());
												lPosGiuCum.setNumeroRegistro(lMisMod.getNumeroRegistro());

												lPosGiuCum.setDataEmissioneProvv(lMisMod.getDataDecisione());

												if ("16".equals(lPosMod.getCodPosizioneGiuridica())
														|| "17".equals(lPosMod.getCodPosizioneGiuridica())) {
													lPosGiuCum.setNumAnniMisura(lMisMod.getNumAnniMisura());
													lPosGiuCum.setNumMesiMisura(lMisMod.getNumMesiMisura());
													lPosGiuCum
															.setNumGiorniMisura(lMisMod.getNumGiorniMisura());

													lPosGiuCum.setDataFineMisura(lMisMod.getDataFineMisura());

													lPosGiuCum.setFlagDecisioneTDS(
															lMisMod.getFlagDecisioneTribunale());
												}
											}
										}
									}
								}
							}
						} // end in misura

						siesLogger.debug("Recupero luogo detenzione se presente");
						lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
						lLuoDetSqlDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
						LuogoDetenzioneModel lLuogoDet = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();

						if (lLuogoDet != null && lLuogoDet.getIstDetIdIstitutoDetenzione() != null)
							lPosGiuCum
									.setIstDetIdIstitutoDetenzione(lLuogoDet.getIstDetIdIstitutoDetenzione());
						else if (lLuogoDet != null && lLuogoDet.getAltroLuogo() != null)
							lPosGiuCum.setAltroLuogo(lLuogoDet.getAltroLuogo());

						siesLogger.debug("Inserisco PG x Espiazione Attuale " + lPosGiuCum);
						lPosCumDao = new PosizioneGiuridicaCumuloDAO(lConn);
						lPosCumDao.setDAOFromModel(lPosGiuCum);
						lPosCumDao.insert();
					}
				} // lPosMod != null
				else {
					siesLogger.debug("PG Gestita NON GESTITA. Non estraggo l'espiato! ");
				}

				// ========================================================================
				// Espiazione pregressa nel caso di archiviazione
				// FIXME In test
				// ========================================================================
				try {
					siesLogger.debug("Test caricaEspiazionePregressa ");
					caricaEspiazionePregressa(aIdFascicoloSiep, lTitoloModel.getIdTitoloCumulato(),
							aIdIstruttoriaCumulo, aDatoOpModel, lConn);
				} catch (Exception e) {
				}

				siesLogger.debug("COMMIT Espiazione Attuale = " + lFascicoloStr);
				commit(lConn);
			} catch (Exception e) {
				siesLogger.error(
						"ROLLBACK. Errore in fase di caricamento dell'Espiazione attuale per il procedimento: "
								+ lFascicoloStr,
						e);
				rollback(lConn);
			}
			// ========================================================================
			//
			siesLogger.debug("Ricerca eventuale presenza procedimento di cumulo...");
			lIstruttoraSqlDao = new IstruttoriaCumuloSqlDAO(lConn);
			lIstruttoraSqlDao.ricercaIstruttoriaCumuloByIdFas(aIdFascicoloSiep);
			IstruttoriaCumuloModel lUltimaIstruttoria = (IstruttoriaCumuloModel) lIstruttoraSqlDao
					.getModelByKey();

			if (lUltimaIstruttoria != null) {
				siesLogger.debug("Presente provvedimento di cumulo (idIstruttoria = "
						+ lUltimaIstruttoria.getIdIstruttoriaCumulo()
						+ ", procedo all'estrazione dei titoli cumulati");
				EstraiDaPrecedenteCumulo(lUltimaIstruttoria, aIdIstruttoriaCumulo, lFascicolo, aDatoOpModel,
						lConn, aTipoIscrizione);
			} else {
				siesLogger.debug(
						"Nessun provvedimento di cumulo presente sul cumulato, estrazione dati terminata. ");
				// // MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
				// // TEST se non trovo una istruttoria chiusa valida Estraggo i dati dall'ultima istruttoria
				// annullata
				// // NON PIU implementata. Il trasferimento è esplicito solo dalla grgiglia delle istruttorie
				// aperte
				// lIstruttoraSqlDao.ricercaIstruttoriaCumuloAnnullataByIdFas(aIdFascicoloSiep);
				// lUltimaIstruttoria = (IstruttoriaCumuloModel) lIstruttoraSqlDao.getModelByKey();
				// if (lUltimaIstruttoria != null) {
				// siesLogger.debug("Trovata Istruttoria annullata la carico...");
				// EstraiDaPrecedenteCumulo(lUltimaIstruttoria, aIdIstruttoriaCumulo, lFascicolo,
				// aDatoOpModel,
				// lConn, aTipoIscrizione);
				// }
				// MEV_2025-48 – 2.14 FINE
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExInserisciTitoloCumulato: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExInserisciTitoloCumulato: Non posso inserire: " + ex);
		} finally {
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lSentDao);
			cleanup(lTitoloCumDao);
			cleanup(lProcCumDao);
			cleanup(lSoggCumDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUffSqlDao);
			cleanup(lPenResSqlDao);
			cleanup(lPosSqlDao);
			cleanup(lPosCumDao);
			cleanup(lEveSqlDao);
			cleanup(lMisSqlDao);
			cleanup(lLuoDetSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lIstruttoraSqlDao);
			cleanup(lContinCunSqlDao);
			cleanup(lContinuaDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	public void ExInserisciTitoliProprioUfficioInIstruttoria(BigDecimal aIdIstruttoriaCumulo,
			String[] lIdFascicoliMioUfficio, DatiOperazioneModel aDatoOpModel, Connection aDBConnection,
			BigDecimal aIdFasCumulante, String aTipoIscrizione) throws F3BException {

		Connection lConn = null;

		FascicoloSiepSqlDAO lFasSqlDao = null;
		FascicoloSiepModel lFasCumulanteModel = new FascicoloSiepModel();

		try {
			if (aDBConnection != null)
				lConn = aDBConnection;
			else
				lConn = getDBConnection();

			// Ricerca fascicolo Cumulante
			lFasSqlDao = new FascicoloSiepSqlDAO(lConn);

			lFasSqlDao.ricercaFascicoloByKey(aIdFasCumulante);
			lFasCumulanteModel = (FascicoloSiepModel) lFasSqlDao.getModelByKey();

			if (lIdFascicoliMioUfficio != null && lIdFascicoliMioUfficio.length > 0) {
				for (int i = 0; i < lIdFascicoliMioUfficio.length; i++) {
					// siesLogger.debug("--XX--lavoro il fascicolo ID N. >"+lIdFascicoliMioUfficio[i]+"<");
					ExInserisciTitoloInIstruttoria(aIdIstruttoriaCumulo,
							new BigDecimal(lIdFascicoliMioUfficio[i]), aDatoOpModel, lConn, null,
							aTipoIscrizione);
					ExInserisciEventoAnnotazioneEsitoTrasm(lFasCumulanteModel, aIdIstruttoriaCumulo,
							new BigDecimal(lIdFascicoliMioUfficio[i]), aDatoOpModel, lConn);
				}
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (Exception ex) {
			siesLogger.debug("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExInserisciTitoliProprioUfficioInIstruttoria: " + ex);
		} finally {
			cleanup(lFasSqlDao);

			if (aDBConnection == null)
				cleanup(lConn);
		}
	} // Chiude ExInserisciTitoliProprioUfficioInIstruttoria

	public void ExInserisciEventoAnnotazioneEsitoTrasm(FascicoloSiepModel lFasCumulante,
			BigDecimal aIdIstruttoriaCumulo, BigDecimal aIdFascicolo, DatiOperazioneModel aDatoOpModel,
			Connection aDBConnection) throws F3BException {

		siesLogger.debug("--XX-- Inizio ExInserisciEventoAnnotazioneEsitoTrasm - lavoro il fascicolo ID N. >"
				+ aIdFascicolo + "<");
		Connection lConn = null;
		EventoModel lEveMod = new EventoModel();
		AnnotazioneEsitoTrasmissioneModel lAnnEsiMod = new AnnotazioneEsitoTrasmissioneModel();
		ProcedimentoCumulatoModel lProcMod = new ProcedimentoCumulatoModel();

		BigDecimal lKeyEve = null;
		BigDecimal lKeyAnnEsi = null;

		EventoDAO lEveDao = null;
		AnnotazioneEsitoTrasmissioneDAO lAnnEsiDao = null;
		ProcedimentoCumulatoDAO lProcDao = null;
		ProcedimentoCumulatoSqlDAO lProcSqlDao = null;

		try {
			if (aDBConnection != null) {
				lConn = aDBConnection;
			} else {
				lConn = getDBConnection();
			}

			// Inserimento Evento: Iscrizine In Istruttoria Stesso Ufficio (Cod = 1040)
			Date dataOd = DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy");
			lEveDao = new EventoDAO(lConn);

			lEveMod.setCodMotivo("1040");
			lEveMod.setCodTipoEvento("01");
			lEveMod.setCodTipoProvvedimento("31");

			lEveMod.setCodUfficioEmittente(lFasCumulante.getChiaveUfficio());
			lEveMod.setCodLuogoEmittente(aDatoOpModel.getCodUfficio().substring(0, 6));
			lEveMod.setDataEmissione(dataOd);

			lEveMod.setCodEsito("-");
			lEveMod.setCodLuogoDestinatario("-");
			lEveMod.setCodTipoUfficioDestinatario("-");

			lEveMod.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
			lEveMod.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
			lEveMod.setDataInserimento(aDatoOpModel.getData());

			lEveMod.setFasSieIdFascicoloSiep(aIdFascicolo);
			lEveMod.setFlagStampaSiep("S");
			lEveMod.setFlagVideoSiep("S");
			lEveMod.setFlagDocumentoRegistrato("S");

			lEveDao.setDAOFromModel(lEveMod);
			lKeyEve = lEveDao.insert();
			siesLogger.debug("--XX--  >>>>>>>>>>>  Ho inserito Evento con Id = " + lKeyEve);

			// Inserimento Annotazione_Esito_Trasmissione: Iscrizine In Istruttoria Stesso Ufficio (Cod_Esito
			// = 1008)
			lAnnEsiDao = new AnnotazioneEsitoTrasmissioneDAO(lConn);

			lAnnEsiMod.setOggettoTrasmissione("00066");
			lAnnEsiMod.setCodEsito("01008");
			lAnnEsiMod.setCodUfficioEsito(aDatoOpModel.getCodUfficio());
			lAnnEsiMod.setDataEsito(dataOd);

			lAnnEsiMod.setChiaveAnno(lFasCumulante.getChiaveAnno());
			lAnnEsiMod.setChiaveProgr(lFasCumulante.getChiaveProgr());
			lAnnEsiMod.setChiaveUfficio(lFasCumulante.getChiaveUfficio());

			lAnnEsiMod.setEveIdEvento(lKeyEve);
			lAnnEsiMod.setFasSieIdFascicoloSiep(aIdFascicolo);

			lAnnEsiMod.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
			lAnnEsiMod.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
			lAnnEsiMod.setDataInserimento(aDatoOpModel.getData());

			lAnnEsiDao.setDAOFromModel(lAnnEsiMod);
			lKeyAnnEsi = lAnnEsiDao.insert();
			siesLogger.debug(
					"--XX--  >>>>>>>>>>>  Ho Inserito AnnotazioneEsitoTrasmissione con Id = " + lKeyAnnEsi);

			// Update Procedimento_Cumulato
			lProcDao = new ProcedimentoCumulatoDAO(lConn);
			lProcSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			lProcSqlDao.ricercaProcedimentoCumulatoByIdFascicoloSiepOrigEIstruttoria(aIdFascicolo,
					aIdIstruttoriaCumulo);
			lProcSqlDao.start();
			if (lProcSqlDao.next()) {
				lProcMod = (ProcedimentoCumulatoModel) lProcSqlDao.getModel();
				lProcMod.setEveIdEvento(lKeyEve);

				lProcDao.setDAOFromModelForUpdate(lProcMod);
				lProcDao.selCondizioneUpdate(lProcMod.getIdProcedimentoCumulato());
				lProcDao.update();
				lProcDao.stop();
			}
			lProcSqlDao.stop();

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (Exception ex) {
			siesLogger.debug("Exception: ", ex);
			throw new F3BException("ModuloCumuloController.ExInserisciEventoAnnotazioneEsitoTrasm: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lAnnEsiDao);
			cleanup(lProcDao);
			cleanup(lProcSqlDao);

			if (aDBConnection == null)
				cleanup(lConn);
		}
	} // Chiude ExInserisciEventoAnnotazioneEsitoTrasm

	/**
	 * Metodo che pilota l'estrazione dei dati analitici di un dato fascicolo agganciadoli al TITOLO_CUMULATO.
	 *
	 * @param aIdFascicoloCumulato
	 * @param aCumulo
	 * @param aDBConnection
	 * @throws F3BException
	 */
	public void ExEstraiDatiAnalitici(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitolo,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati della Pena Complessiva,
			// Sanzione Sostitutiva
			// e Sentenze Continuazione del fasciolo
			// ========================================================================
			ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ========================================================================
			// Estraggo i dati dei Reati del fasciolo
			// ========================================================================
			ExEstraiReatiPerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ========================================================================
			// Estraggo i dati dei Reati del fasciolo
			// ========================================================================
			ExEstraiCircostanzePerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ========================================================================
			// Estraggo i dati delle misure sicurezza del fasciolo
			// ========================================================================
			ExEstraiMisuraSicurezzaPerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ========================================================================
			// Estraggo i dati della Pena Accessoria del fasciolo
			// ========================================================================
			ExEstraiPenaAccessoriaPerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ========================================================================
			// Estraggo i dati dei Benefici Concessi del fasciolo
			// ========================================================================
			ExEstraiBeneficiConcessiPerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ===================================================================================
			// Estraggo i dati dei Benefici del fasciolo Revocati in altro Procedimento (REVOCHE)
			// ===================================================================================
			ExEstraiRevochePerCumulo(aIdFascicoloCumulato, aTitolo, lConn);

			// ========================================================================
			// Estraggo i dati delle Misure Cautelari del fasciolo
			// ========================================================================
			ExEstraiMisureCautelariPerCumulo(aIdFascicoloCumulato, aTitolo, lConn);
			// ========================================================================
			// Estraggo i dati del ....................... del fasciolo
			// ========================================================================
			// ExEstrai.............PerCumulo(aIdFascicoloCumulato, aCumulo, lConn);

			// ========================================================================
			// [INSERIRE QUI SOPRA LE CHIAMATE AGLI ALTRI METODI DI ESTRAZIONE]
			// ========================================================================

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (F3BException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore in fase di estrazioned dei dati analitici", ex);
			throw new F3BException("ModuloCumuloController.ExEstraiDatiAnalitici: Non posso inserire: " + ex);
		} finally {
			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}
	// ==================

	/**
	 * Metodo che estrae i dati dei Benefici Concessi del fascicolo da cumulare, e li carica nella tabella
	 * BENEFICIO_CUMULO, agganciandoli al record TITOLO_CUMULATO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiBeneficiConcessiPerCumulo(BigDecimal aIdFascicoloCumulato,
			TitoloCumulatoModel aTitoloModel, Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		BeneficioSqlDAO lBenSqlDao = null;
		BeneficioCumuloDAO lBenCumDao = null;
		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		PenaAccessoriaSqlDAO lpenAccSDao = null;
		PenaAccessoriaCumuloDAO lpenAccCumDao = null;
		PenaAccessoriaCumuloSqlDAO lPAcCumSqlDao = null;
		TipologiaOrarioDAO lTipOrDao = null;

		Vector lListaBenefici = new Vector();

		BeneficioModel lBenMod = null;
		BeneficioCumuloModel lBenCumMod = null;
		PenaAccessoriaModel lPenMod = null;
		PenaAccessoriaCumuloModel lPenCumMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati del Beneficio legati al fascicolo SIEP
			// ========================================================================
			lBenSqlDao = new BeneficioSqlDAO(lConn);

			lBenSqlDao.ricercaBeneficioByKeyFascicolo(aIdFascicoloCumulato);
			lListaBenefici = new Vector(lBenSqlDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("--XX-- ExEstraiBeneficiConcessiPerCumulo - Totale Benefici trovati = "
					+ lListaBenefici.size());
			if (lListaBenefici != null && lListaBenefici.size() > 0) {
				Iterator itxBn = lListaBenefici.iterator();
				while (itxBn.hasNext()) {
					lBenMod = (BeneficioModel) itxBn.next();

					// Prendo solo i CONCESSI (TipoBeneficio = "C")
					if (lBenMod != null && lBenMod.getIdBeneficio() != null
							&& lBenMod.getCodNaturaBeneficio().compareTo("C") == 0) {
						if (lBenMod.getCodTipoBeneficio().compareTo("02") == 0
								&& lBenMod.getBenIdBeneficio() != null) {
							// NON lo copio perchè trattasi di NON MENZIONE Collegata a SOSPENSIONE, e viene
							// copiato dopo
						} else {
							lBenCumMod = new BeneficioCumuloModel();
							lBenCumMod = PreparaBeneficio(lBenMod);

							lBenCumMod.setCodOperatoreInserimento(aTitoloModel.getCodOperatoreInserimento());
							lBenCumMod.setCodUfficioInserimento(aTitoloModel.getCodUfficioInserimento());
							lBenCumMod.setDataInserimento(aTitoloModel.getDataInserimento());
							lBenCumMod.setFlagStato("E");
							lBenCumMod.setIdBeneficioOrigine(lBenMod.getIdBeneficio());
							lBenCumMod.setTitIdTitoloCumulato(aTitoloModel.getIdTitoloCumulato());

							// Insert;
							lBenCumDao = new BeneficioCumuloDAO(lConn);
							lBenCumDao.setDAOFromModel(lBenCumMod);
							BigDecimal lKey_01 = lBenCumDao.insert();
							lBenCumDao.stop();

							//
							if (lBenMod.getCodTipoSospSubordinata().compareTo("08") == 0) {
								// Cerco Tipologia Orario
								ITipologiaOrario CtrlTip = SIEPLookupRemote.getTipologiaOrarioRemote();
								Vector lTipologia = CtrlTip
										.ExRicercaTipologiaOrarioByIdBeneficio(lBenMod.getIdBeneficio());
								if (lTipologia != null && lTipologia.size() > 0) {
									Iterator itx = lTipologia.iterator();
									while (itx.hasNext()) {
										TipologiaOrarioModel lTipolo = (TipologiaOrarioModel) itx.next();
										TipologiaOrarioModel NewTipolo = new TipologiaOrarioModel();

										NewTipolo.setAlleOre(lTipolo.getAlleOre());
										NewTipolo.setDalleOre(lTipolo.getDalleOre());
										NewTipolo.setCodNumGiorno(lTipolo.getCodNumGiorno());
										NewTipolo.setDatFinCumUltSanzioni(lTipolo.getDatFinCumUltSanzioni());
										NewTipolo.setDescrNumGiorno(lTipolo.getDescrNumGiorno());
										NewTipolo.setEnteIncaricato(lTipolo.getEnteIncaricato());

										NewTipolo.setCodOperatoreInserimento(
												lTipolo.getCodOperatoreInserimento());
										NewTipolo
												.setCodUfficioInserimento(lTipolo.getCodUfficioInserimento());
										NewTipolo.setDataInserimento(lTipolo.getDataInserimento());

										NewTipolo.setBenIdBeneficio(null);
										NewTipolo.setBenIdBeneficioCumulo(lKey_01);

										// Insert
										lTipOrDao = new TipologiaOrarioDAO(lConn);
										lTipOrDao.setDAOFromModel(NewTipolo);
										lTipOrDao.insert();
										lTipOrDao.stop();
									}
								}
							}

							// Amnistia/Indulto
							if (lBenMod.getCodTipoBeneficio().compareTo("03") == 0
									|| lBenMod.getCodTipoBeneficio().compareTo("04") == 0) {
								// Cerco eventuale pena Accessoria
								lpenAccSDao = new PenaAccessoriaSqlDAO(lConn);

								lpenAccSDao.ricercaPenaAccessoriaByBenIdBeneficio(lBenMod.getIdBeneficio());
								lPenMod = (PenaAccessoriaModel) lpenAccSDao.getModelByKey();
								if (lPenMod != null && lPenMod.getIdPenaAccessoria() != null) {
									// Cerco Pena_Acceessoria_Cumulo nata da Pena_Accessoria
									lPAcCumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
									lPAcCumSqlDao.ricercaPenaAccessoriaCumuloByKeyPAOrigine(
											lPenMod.getIdPenaAccessoria(),
											aTitoloModel.getIdTitoloCumulato());

									lPenCumMod = (PenaAccessoriaCumuloModel) lPAcCumSqlDao.getModelByKey();
									if (lPenCumMod != null
											&& lPenCumMod.getIdPenaAccessoriaCumulo() != null) {
										lPenCumMod.setBenIdBeneficioCumulo(lKey_01);
										lpenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);
										lpenAccCumDao.setDAOFromModelForUpdate(lPenCumMod);
										lpenAccCumDao
												.selCondizioneUpdate(lPenCumMod.getIdPenaAccessoriaCumulo());
										lpenAccCumDao.update();
										lpenAccCumDao.stop();
									}
								}
							}

							// Sospensione Condizionale
							if (lBenMod.getCodTipoBeneficio().compareTo("01") == 0) {
								// cerco un eventuale Beneficio Non_Menzione collegato
								lBenSqlDao = new BeneficioSqlDAO(lConn);
								lBenSqlDao.ricercaBeneficioByBenIdBeneficio(lBenMod.getIdBeneficio());
								BeneficioModel lBen = (BeneficioModel) lBenSqlDao.getModelByKey();
								if (lBen != null && lBen.getIdBeneficio() != null) {
									if (lBen.getCodTipoBeneficio().compareTo("02") == 0) {
										lBenCumMod = new BeneficioCumuloModel();
										lBenCumMod = PreparaBeneficio(lBen);

										lBenCumMod.setCodOperatoreInserimento(
												aTitoloModel.getCodOperatoreInserimento());
										lBenCumMod.setCodUfficioInserimento(
												aTitoloModel.getCodUfficioInserimento());
										lBenCumMod.setDataInserimento(aTitoloModel.getDataInserimento());
										lBenCumMod.setFlagStato("E");

										lBenCumMod.setIdBeneficioOrigine(lBen.getIdBeneficio());
										lBenCumMod.setBenIdBeneficioOrig(lBen.getBenIdBeneficio());
										lBenCumMod.setTitIdTitoloCumulato(aTitoloModel.getIdTitoloCumulato());
										lBenCumMod.setBenIdBeneficioCumulo(lKey_01);
										// Insert;
										lBenCumDao = new BeneficioCumuloDAO(lConn);
										lBenCumDao.setDAOFromModel(lBenCumMod);
										/* BigDecimal lKey_02 = */lBenCumDao.insert();
										lBenCumDao.stop();
									}
								}
							}

							// Si cerca di collegare la concessione SOSPENSIONE CONDIZIONALE
							// ad una eventuale revoca su uno dei titoli già a sistema
							if (lBenMod.getCodTipoBeneficio().compareTo("01") == 0) {
								// Cerca sui titoli in istruttoria se presente un record che revoca il
								// beneficio
								siesLogger.debug("Ricerco le revoche delle sospensive già in istruttoria");
								lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);

								lBenCumSqlDao.ricercaBeneficioCumuloByIdIstruttoria(
										aTitoloModel.getIstrIdIstruttoriaCumulo());
								Vector<BeneficioCumuloModel> lListaBeneficiIstr = new Vector<BeneficioCumuloModel>(
										lBenCumSqlDao.getModels());
								lBenCumSqlDao.stop();

								for (BeneficioCumuloModel lBenIstrRev : lListaBeneficiIstr) {
									if ("R".equals(lBenIstrRev.getCodNaturaBeneficio()) // R = Revoca
											&& "01".equals(lBenIstrRev.getCodTipoBeneficio()) // "01" =
																								// Sospensione
																								// condizionale
											&& !"C".equals(lBenIstrRev.getFlagStato()) // C = Cancellato
									) {
										siesLogger.debug("Trovata revoca " + lBenIstrRev);
										if (lBenIstrRev.isStessoTitolo(aTitoloModel)) {
											siesLogger.debug(
													"La revoca si riferisce proprio al beneficio che sto inserendo. Aggiorno i collegamenti.");

											// Aggiorno il record della revoca con l'id del titolo di
											// concessione che sto caricando
											lBenCumDao.setTitIdTitoloCumulatoCollegato(
													aTitoloModel.getIdTitoloCumulato());
											lBenCumDao
													.setCondizioneUpdate(lBenIstrRev.getIdBeneficioCumulo());
											lBenCumDao.update();
											lBenCumDao.stop();

											// Aggiorno il record corrente della concessione con l'id del
											// titolo di revoca
											lBenCumDao.setTitIdTitoloCumulatoCollegato(
													lBenIstrRev.getTitIdTitoloCumulato());
											lBenCumDao.setCondizioneUpdate(lKey_01);
											lBenCumDao.update();
											lBenCumDao.stop();
										}
									}
								}
							}
							// ================================================================

						}
					}
				}
			}
			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiBeneficiConcessiPerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiBeneficiConcessiPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lpenAccSDao);
			cleanup(lpenAccCumDao);
			cleanup(lBenSqlDao);
			cleanup(lBenCumDao);
			cleanup(lBenCumSqlDao);
			cleanup(lPAcCumSqlDao);
			cleanup(lTipOrDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	} // Chiude ExEstraiBeneficiConcessiPerCumulo()

	/**
	 * Metodo che estrae i dati dei Benefici (REVOCHE) del fascicolo da cumulare, Revocati in altri
	 * procedimenti, e li carica nella tabella BENEFICIO_CUMULO, agganciandoli al record TITOLO_CUMULATO
	 * dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiRevochePerCumulo(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitoloModel,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		BeneficioSqlDAO lBenSqlDao = null;
		BeneficioCumuloDAO lBenCumDao = null;
		TitoloCumulatoSqlDAO lTitSqlDao = null;
		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		BeneficioCumuloSqlDAO lConCumSqlDao = null;
		BeneficioCumuloDAO lConcessioneCumDao = null;

		Vector lListaBenefici = new Vector();

		BeneficioModel lBenMod = null;
		BeneficioCumuloModel lBenCumMod = null;
		// TitoloCumulatoModel lTitoloMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati del Beneficio legati al fascicolo SIEP
			// ========================================================================
			lBenSqlDao = new BeneficioSqlDAO(lConn);

			lBenSqlDao.ricercaBeneficioByKeyFascicolo(aIdFascicoloCumulato);
			lListaBenefici = new Vector(lBenSqlDao.getModels());

			if (lListaBenefici != null && lListaBenefici.size() > 0) {
				Iterator itxBn = lListaBenefici.iterator();
				while (itxBn.hasNext()) {
					lBenMod = (BeneficioModel) itxBn.next();

					// Prendo solo le REVOCHE (TipoBeneficio = "R")
					if (lBenMod != null && lBenMod.getIdBeneficio() != null
							&& lBenMod.getCodNaturaBeneficio().compareTo("R") == 0) {
						lBenCumMod = new BeneficioCumuloModel();
						lBenCumMod = PreparaBeneficio(lBenMod);

						lBenCumMod.setIdBeneficioOrigine(lBenMod.getIdBeneficio());

						// Se la Revoca_Origine è collegata ad un altro Beneficio tramite 'Ben_Id_Beneficio',
						// anche la Revoca_Cumulo avrà un collegamento ad un Beneficio tramite
						// 'Ben_Id_Beneficio_Cumulo'
						if (lBenMod.getBenIdBeneficio() != null) {
							lBenCumMod.setBenIdBeneficioOrig(lBenMod.getBenIdBeneficio());

							lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
							lBenCumSqlDao
									.ricercaBeneficioCumuloByKeyBeneficioOrig(lBenMod.getBenIdBeneficio());
							BeneficioCumuloModel lBeneficioCum = (BeneficioCumuloModel) lBenCumSqlDao
									.getModelByKey();

							if (lBeneficioCum != null && lBeneficioCum.getIdBeneficioCumulo() != null) {
								lBenCumMod.setBenIdBeneficioCumulo(lBeneficioCum.getIdBeneficioCumulo());
							}
							// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
							// cleanup(lBenCumSqlDao);
						}

						// Se la Revoca_Origine riporta il riferimento alla Sentenza di Concessione
						// Beneficio_Origine tramite 'Rif_Id_Provvedimento',
						// anche la Revoca_Cumulo avrà il riferimento al TITOLO_CUMULATO di concessione
						// Beneficio_Cumulo tramite 'Tit_Id_Titolo_Cumulo_Collegato'
						if (lBenMod.getRifIdProvvedimento() != null) {
							// Cerco titolo_Cumulato By Id_Sentenza_origine e Istruttiria;
							lTitSqlDao = new TitoloCumulatoSqlDAO(lConn);
							TitoloCumulatoModel lTitoloMod = new TitoloCumulatoModel();

							lTitoloMod.setIdSentenzaOrigine(lBenMod.getRifIdProvvedimento());
							lTitoloMod.setIstrIdIstruttoriaCumulo(aTitoloModel.getIstrIdIstruttoriaCumulo());

							lTitSqlDao.ricercaTitoloCumulato(lTitoloMod);
							lTitoloMod = (TitoloCumulatoModel) lTitSqlDao.getModelByKey();

							if (lTitoloMod != null && lTitoloMod.getIdTitoloCumulato() != null) {
								// nella REVOCA_CUMULO riporto il dato relativo AL TITOLO_CUMULATO che aveva
								// CONCESSIO il BENEFICIO
								lBenCumMod.setTitIdTitoloCumulatoCollegato(lTitoloMod.getIdTitoloCumulato());
							}

							// cerco Il beneficio_Cumulo_Concesso con Tit_id_Titolo_Cumulato appena trovato
							if (lTitoloMod != null && lTitoloMod.getIdTitoloCumulato() != null) {
								lConCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
								Vector lConcessi = new Vector();
								BeneficioCumuloModel lModel = new BeneficioCumuloModel();

								lModel.setCodTipoBeneficio(lBenMod.getCodTipoBeneficio());
								lModel.setCodNaturaBeneficio("C");
								lModel.setTitIdTitoloCumulato(lTitoloMod.getIdTitoloCumulato());

								if (lBenMod.getCodTipoBeneficio().compareTo("03") == 0)
									lModel.setCodDpr(lBenMod.getCodDpr());

								lConCumSqlDao.ricercaBeneficioCumulo(lModel);
								lConcessi = new Vector(lConCumSqlDao.getModels());

								// In realtà dovrei trovarne solo uno
								if (lConcessi != null && lConcessi.size() > 0) {
									// Nel Beneficio_Concesso riporto il dato relativo AL TITOLO_CUMULATO che
									// lo sta REVOCANDO
									lModel = (BeneficioCumuloModel) lConcessi.get(0);
									lModel.setTitIdTitoloCumulatoCollegato(
											aTitoloModel.getIdTitoloCumulato());

									lConcessioneCumDao = new BeneficioCumuloDAO(lConn);
									lConcessioneCumDao.setDAOFromModelForUpdate(lModel);
									lConcessioneCumDao.update();
									lConcessioneCumDao.stop();
								}
							}
						}

						// Finisco di inserire la Revoca_Cumulo
						lBenCumMod.setCodOperatoreInserimento(aTitoloModel.getCodOperatoreInserimento());
						lBenCumMod.setCodUfficioInserimento(aTitoloModel.getCodUfficioInserimento());
						lBenCumMod.setDataInserimento(aTitoloModel.getDataInserimento());
						lBenCumMod.setFlagStato("E");
						lBenCumMod.setTitIdTitoloCumulato(aTitoloModel.getIdTitoloCumulato());

						// Insert;
						lBenCumDao = new BeneficioCumuloDAO(lConn);
						lBenCumDao.setDAOFromModel(lBenCumMod);
						BigDecimal lKey_01 = lBenCumDao.insert();
						lBenCumMod.setIdBeneficioCumulo(lKey_01);
						lBenCumDao.stop();
					}
				} // Chiude Iterator
			}
			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiRevochePerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiRevochePerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lBenSqlDao);
			cleanup(lBenCumDao);
			cleanup(lTitSqlDao);
			cleanup(lConCumSqlDao);
			cleanup(lConcessioneCumDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lBenCumSqlDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	} // Chiude ExEstraiRevochePerCumulo()

	public BeneficioCumuloModel PreparaBeneficio(BeneficioModel lBenMod) throws F3BException {

		BeneficioCumuloModel lBenCumMod = new BeneficioCumuloModel();

		lBenCumMod.setCodTipoBeneficio(lBenMod.getCodTipoBeneficio());
		lBenCumMod.setCodSottotipoBeneficio(lBenMod.getCodSottotipoBeneficio());
		lBenCumMod.setCodNaturaBeneficio(lBenMod.getCodNaturaBeneficio());
		lBenCumMod.setCodDpr(lBenMod.getCodDpr());
		lBenCumMod.setCodTipoSospSubordinata(lBenMod.getCodTipoSospSubordinata());

		lBenCumMod.setDescrDpr(lBenMod.getDescrDpr());
		lBenCumMod.setDescrLuogoEmittente(lBenMod.getDescrLuogoAutoritaEmittente());
		lBenCumMod.setDescrNaturaBeneficio(lBenMod.getDescrNaturaBeneficio());
		lBenCumMod.setDescrSottotipoBeneficio(lBenMod.getDescrSottotipoBeneficio());
		lBenCumMod.setDescrTipoAutoEmittente(lBenMod.getDescrTipoAutoritaEmittente());
		lBenCumMod.setDescrTipoBeneficio(lBenMod.getDescrTipoBeneficio());
		lBenCumMod.setDescrTipoProvvedimento(lBenMod.getDescrTipoProvvedimento());
		lBenCumMod.setDescrTipoSospSubordinata(lBenMod.getDescrTipoSospSubordinata());

		lBenCumMod.setFlagFrequenzaSettimanale(lBenMod.getFlagFrequenzaSettimanale());
		lBenCumMod.setImportoAmmenda(lBenMod.getImportoAmmenda());
		lBenCumMod.setImportoMulta(lBenMod.getImportoMulta());
		lBenCumMod.setNote(lBenMod.getNote());

		lBenCumMod.setNumAnniAdempimento(lBenMod.getNumAnniAdempimento());
		lBenCumMod.setNumAnniArresto(lBenMod.getNumAnniArresto());
		lBenCumMod.setNumAnniReclusione(lBenMod.getNumAnniReclusione());
		lBenCumMod.setNumAnniSospensione(lBenMod.getNumAnniSospensione());
		lBenCumMod.setNumGiorniAdempimento(lBenMod.getNumGiorniAdempimento());
		lBenCumMod.setNumGiorniArresto(lBenMod.getNumGiorniArresto());
		lBenCumMod.setNumGiorniPrestazione(lBenMod.getNumGiorniPrestazione());
		lBenCumMod.setNumGiorniReclusione(lBenMod.getNumGiorniReclusione());
		lBenCumMod.setNumMesiAdempimento(lBenMod.getNumMesiAdempimento());
		lBenCumMod.setNumMesiArresto(lBenMod.getNumMesiArresto());
		lBenCumMod.setNumMesiPrestazione(lBenMod.getNumMesiPrestazione());
		lBenCumMod.setNumMesiReclusione(lBenMod.getNumMesiReclusione());
		lBenCumMod.setNumOreSettimanali(lBenMod.getNumOreSettimanali());

		lBenCumMod.setRifAnnoProvvedimento(lBenMod.getRifAnnoProvvedimento());
		lBenCumMod.setRifCodLuogoEmittente(lBenMod.getRifCodLuogoAutoEmittente());
		lBenCumMod.setRifCodTipoAutoEmittente(lBenMod.getRifCodTipoAutoEmittente());
		lBenCumMod.setRifCodTipoProvvedimento(lBenMod.getRifCodTipoProvvedimento());
		lBenCumMod.setRifDataIrrevocabilita(lBenMod.getRifDataIrrevocabilita());
		lBenCumMod.setRifDataProvvedimento(lBenMod.getRifDataProvvedimento());
		lBenCumMod.setRifIdProvvedimento(lBenMod.getRifIdProvvedimento());
		lBenCumMod.setRifNumeroProvvedimento(lBenMod.getRifNumeroProvvedimento());
		lBenCumMod.setRifNumSezioneAutoEmittente(lBenMod.getRifNumSezioneAutoEmittente());

		return lBenCumMod;
	}

	/**
	 * Metodo che estrae i dati delle Pena_Accessoria del fascicolo da cumulare e li carica nella tabella
	 * PENA_ACCESSORIA_CUMULO agganciandoli al record TITOLO_CUMULATO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiPenaAccessoriaPerCumulo(BigDecimal aIdFascicoloCumulato,
			TitoloCumulatoModel aTitoloModel, Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		PenaAccessoriaSqlDAO lpenAccSDao = null;
		PenaAccessoriaCumuloDAO lpenAccCumDao = null;

		Vector lListaPenaAc = new Vector();

		PenaAccessoriaModel lPenMod = null;
		PenaAccessoriaCumuloModel lPenCumMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati della Pena Accessoria del fasciolo
			// ========================================================================
			lpenAccSDao = new PenaAccessoriaSqlDAO(lConn);

			lpenAccSDao.ricercaPenaAccessoriaByFascicolo(aIdFascicoloCumulato);
			lListaPenaAc = new Vector(lpenAccSDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Pena Accesoria trovate = " + lListaPenaAc.size());
			for (int i = 0; i < lListaPenaAc.size(); i++) {
				lPenMod = (PenaAccessoriaModel) lListaPenaAc.elementAt(i);

				lPenCumMod = new PenaAccessoriaCumuloModel();

				lPenCumMod.setCodTipoPenaAccessoria(lPenMod.getCodTipoPenaAccessoria());
				lPenCumMod.setDescrAltrePA(lPenMod.getDescrAltrePA());
				lPenCumMod.setDurata(lPenMod.getDurata());
				lPenCumMod.setNumAnni(lPenMod.getNumAnni());
				lPenCumMod.setNumMesi(lPenMod.getNumMesi());
				lPenCumMod.setNumGiorni(lPenMod.getNumGiorni());
				lPenCumMod.setBenIdBeneficioOrig(lPenMod.getBenIdBeneficio());
				lPenCumMod.setNote(lPenMod.getNote());

				lPenCumMod.setFlagStato("E"); // E = Estratto
				lPenCumMod.setTitIdTitoloCumulato(aTitoloModel.getIdTitoloCumulato());
				lPenCumMod.setIdPenaAccessoriaOrigine(lPenMod.getIdPenaAccessoria());

				lPenCumMod.setCodOperatoreInserimento(aTitoloModel.getCodOperatoreInserimento());
				lPenCumMod.setDataInserimento(aTitoloModel.getDataInserimento());
				lPenCumMod.setCodUfficioInserimento(aTitoloModel.getCodUfficioInserimento());

				lPenCumMod.setFlagDatiFinali("S"); // per default le PA vengono iscritte in Dati Finali Cumulo
				// Inserisco
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug("Inserisco Pena Accessoria cumulo:");
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(lPenCumMod);

				lpenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);
				lpenAccCumDao.setDAOFromModel(lPenCumMod);
				lpenAccCumDao.insert();
				lpenAccCumDao.stop();
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiPenaAccessoriaPerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiPenaAccessoriaPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lpenAccSDao);
			cleanup(lpenAccCumDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	} // Chiude ExEstraiPenaAccessoriaPerCumulo()

	/**
	 *
	 * @param aIdFascicoloCumulato
	 * @param aTitoloModel
	 * @param aDBConnection
	 * @throws F3BException
	 */
	public void ExEstraiMisureCautelariPerCumulo(BigDecimal aIdFascicoloCumulato,
			TitoloCumulatoModel aTitoloModel, Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		MisuraCautelareDAO lMisCautelareDAO = null;
		MisuraCautelareCumuloDAO lMisCautelareCumuloDAO = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati della Misure Cautelari del fasciolo
			// ========================================================================
			lMisCautelareDAO = new MisuraCautelareDAO(lConn);

			lMisCautelareDAO.setCondizioneByFascicoloSiep(aIdFascicoloCumulato);
			lMisCautelareDAO.start();

			lMisCautelareCumuloDAO = new MisuraCautelareCumuloDAO(lConn);

			while (lMisCautelareDAO.next()) {
				MisuraCautelareModel lMisuraModel = (MisuraCautelareModel) lMisCautelareDAO.getModel();

				// Carico solo le computabili con esclusione di quelle in corso di espiazione
				if ("S".equals(lMisuraModel.getFlagComputabile()) && lMisuraModel.getDataFine() != null) {
					MisuraCautelareCumuloModel lMisuraCumuloModel = new MisuraCautelareCumuloModel(
							lMisuraModel);

					lMisuraCumuloModel.setFlagStato("E"); // E = Estratto
					lMisuraCumuloModel.setTitIdTitoloCumulato(aTitoloModel.getIdTitoloCumulato());
					lMisuraCumuloModel.setIdMisuraCautelareOrigine(lMisuraModel.getIdMisuraCautelare());

					lMisuraCumuloModel.setCodOperatoreInserimento(aTitoloModel.getCodOperatoreInserimento());
					lMisuraCumuloModel.setDataInserimento(aTitoloModel.getDataInserimento());
					lMisuraCumuloModel.setCodUfficioInserimento(aTitoloModel.getCodUfficioInserimento());

					// Inserisco
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Inserisco Misura Cautelare cumulo:");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(lMisuraCumuloModel);

					lMisCautelareCumuloDAO.setDAOFromModel(lMisuraCumuloModel);
					lMisCautelareCumuloDAO.insert();
					lMisCautelareCumuloDAO.stop();
				}
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiMisureCautelariCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiMisureCautelariCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisCautelareDAO);
			cleanup(lMisCautelareCumuloDAO);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	} // Chiude ExEstraiMisureCautelariCumulo()

	/**
	 * Metodo che estrae i dati delle Misure_sicurezza del fascicolo da cumulare e li carica nella tabella
	 * MISURA_SICUREZZA_CUMULO agganciandoli al record TITOLO_CUMULATO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiMisuraSicurezzaPerCumulo(BigDecimal aIdFascicoloCumulato,
			TitoloCumulatoModel aTitoloModel, Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		MisuraSicurezzaSqlDAO lMisSicSqlDao = null;
		MisuraSicurezzaCumuloDAO lMisCumDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisCumSqlDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisCumSqlDaoXX = null;
		FascMsToFascSiepSqlDAO lFascMsSqlDao = null;
		UfficioSqlDAO lUffSqldao = null;

		FascMsToFascSiepModel lFascMsModel = null;
		UfficioModel lUffMod = null;

		Vector lListaMisure = new Vector();

		MisuraSicurezzaCumuloModel lMisSicCumMod = null;
		MisuraSicurezzaModel lMisSicMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati delle misure sicurezza del fasciolo
			// ========================================================================
			lMisSicSqlDao = new MisuraSicurezzaSqlDAO(lConn);

			lMisSicSqlDao.ricercaMisuraSicurezzaByIdFascicolo(aIdFascicoloCumulato);
			lListaMisure = new Vector(lMisSicSqlDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Misure trovate = " + lListaMisure.size());
			for (int i = 0; i < lListaMisure.size(); i++) {
				lMisSicMod = (MisuraSicurezzaModel) lListaMisure.elementAt(i);
				lMisSicCumMod = new MisuraSicurezzaCumuloModel();

				String lNatura = lMisSicMod.getCodNatura();
				if (lMisSicMod.getCodNatura().equals("-")) {
					if (lMisSicMod.getCodTipo() != null && lMisSicMod.getCodTipo().compareTo("-") != 0) {
						lNatura = trattaDato(lMisSicMod.getCodTipo());
					}
				}

				lMisSicCumMod.setCodNatura(lNatura);

				lMisSicCumMod.setCodTipo(lMisSicMod.getCodTipo());
				lMisSicCumMod.setNumAnni(lMisSicMod.getNumAnni());
				lMisSicCumMod.setNumMesi(lMisSicMod.getNumMesi());
				lMisSicCumMod.setNumGiorni(lMisSicMod.getNumGiorni());
				lMisSicCumMod.setAnnoReg38(lMisSicMod.getAnnoReg38());
				lMisSicCumMod.setNumReg38(lMisSicMod.getNumReg38());

				lMisSicCumMod.setFlagStato("E"); // E = Estratto
				lMisSicCumMod.setTitIdTitoloCumulato(aTitoloModel.getIdTitoloCumulato());
				lMisSicCumMod.setIdMisuraSicurezzaOrigine(lMisSicMod.getIdMisuraSicurezza());

				// lMisSicCumMod.setNote (lMisSicMod.get???????);
				lMisSicCumMod.setFlagAnnullaMisura(lMisSicMod.getFlagAnnullaMisura());
				lMisSicCumMod.setDataFineValidita(lMisSicMod.getDataFineValidita());
				lMisSicCumMod.setIstDetIdIstitutoDetenzione(lMisSicMod.getIstDetIdIstitutoDetenzione());
				lMisSicCumMod.setLuogoEsecuzioneMisura(lMisSicMod.getLuogoEsecuzioneMisura());

				if (lMisSicMod.getMisIdMisuraSicurezza() != null) {
					lMisSicCumMod.setMisIdMisuraSicurezzaOrigine(lMisSicMod.getMisIdMisuraSicurezza());
				}

				lMisSicCumMod.setMisIdMisuraSicurezzaCumulo(null); // viene aggiornato più avanti

				lMisSicCumMod.setCodOperatoreInserimento(aTitoloModel.getCodOperatoreInserimento());
				lMisSicCumMod.setDataInserimento(aTitoloModel.getDataInserimento());
				lMisSicCumMod.setCodUfficioInserimento(aTitoloModel.getCodUfficioInserimento());

				// Se la Misura di Classe I è Iscritta ad un Classe IV vanno riempiti Ulteriori campi :
				// ANNO e NUMERO Fascicolo di classe IV; COD AUTORITA' e LUOGO AUTORITA' EMITTENTI del
				// Fascicolo di classe IV;
				lFascMsSqlDao = new FascMsToFascSiepSqlDAO(lConn);
				lFascMsSqlDao.ricercaByKeyFascEsecuzione(aIdFascicoloCumulato);
				lFascMsModel = (FascMsToFascSiepModel) lFascMsSqlDao.getModelByKey();
				if (lFascMsModel != null && lFascMsModel.getIdFascMsToFascSiep() != null) {
					lMisSicCumMod.setAnnoFascicoloSiepIV(lFascMsModel.getChiaveAnnoSiepCollegato());
					lMisSicCumMod.setNumeroFascicoloSiepIV(lFascMsModel.getChiaveProgrSiepCollegato());

					if (lFascMsModel.getChiaveUfficioSiepCollegato() != null) {
						lMisSicCumMod.setCodAutoritaEmittenteIV(lFascMsModel.getChiaveUfficioSiepCollegato());

						lUffSqldao = new UfficioSqlDAO(lConn);
						lUffSqldao.ricercaUfficioByCod(lFascMsModel.getChiaveUfficioSiepCollegato());
						lUffMod = (UfficioModel) lUffSqldao.getModelByKey();
						if (lUffMod != null && lUffMod.getCodUfficio() != null) {

							lMisSicCumMod.setCodLuogoEmittenteIV(lUffMod.getCodComune());
							lMisSicCumMod.setDescrAutoritaEmittenteIV(lUffMod.getDescrTipoUfficio());
							lMisSicCumMod.setDescrluogoEmittenteIV(lUffMod.getDescrComune());
						}
					}
				}

				if (lMisSicCumMod.getDataFineValidita() == null) {
					lMisSicCumMod.setFlagStatoMisura("V"); // per default è valida
				} else {
					// dovrebbe essere solo Classe IV per ora porto il trattino l'utente
					// dovrà indicare il vero stato (revocata/trasformata)
					lMisSicCumMod.setFlagStatoMisura("-");
				}

				lMisSicCumMod.setFlagDatiFinali("S"); // per defult tutte le Misure fanno parte del PROVV.
														// Dati_Finali_Cumulo

				// Inserisco
				lMisCumDao = new MisuraSicurezzaCumuloDAO(lConn);
				lMisCumDao.setDAOFromModel(lMisSicCumMod);
				lMisCumDao.insert();
				lMisCumDao.stop();
			}

			// Serve un secondo passaggio per valorizzare, se possibile e se esiste, l'attributo
			// 'MisIdMisuraSicurezzaCumulo'
			// con il corrispondente valore di 'IdMisuraSicurezzaCumulo'
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Secondo giro con le Misure trovate = ");
			for (int i = 0; i < lListaMisure.size(); i++) {
				lMisSicMod = (MisuraSicurezzaModel) lListaMisure.elementAt(i);

				if (lMisSicMod.getMisIdMisuraSicurezza() != null) {
					BigDecimal KeyMisCumulo = null;
					lMisCumSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
					lMisCumSqlDao.ricercaMisuraSicurezzaCumuloByIdMisuraOrigine(
							lMisSicMod.getMisIdMisuraSicurezza(), aTitoloModel.getIdTitoloCumulato());
					MisuraSicurezzaCumuloModel lMisCum = (MisuraSicurezzaCumuloModel) lMisCumSqlDao
							.getModelByKey();
					if (lMisCum != null && lMisCum.getIdMisuraSicurezzaCumulo() != null) {
						KeyMisCumulo = lMisCum.getIdMisuraSicurezzaCumulo();
					}

					lMisCumSqlDaoXX = new MisuraSicurezzaCumuloSqlDAO(lConn);
					lMisCumSqlDaoXX.ricercaMisuraSicurezzaCumuloByMisIdMisuraOrigine(
							lMisSicMod.getMisIdMisuraSicurezza(), aTitoloModel.getIdTitoloCumulato());
					MisuraSicurezzaCumuloModel lMisCumXX = (MisuraSicurezzaCumuloModel) lMisCumSqlDaoXX
							.getModelByKey();

					if (lMisCumXX != null && lMisCumXX.getIdMisuraSicurezzaCumulo() != null) {
						lMisCumXX.setMisIdMisuraSicurezzaCumulo(KeyMisCumulo);

						lMisCumDao = new MisuraSicurezzaCumuloDAO(lConn);
						lMisCumDao.setDAOFromModelForUpdate(lMisCumXX);
						lMisCumDao.selCondizioneUpdate(lMisCumXX.getIdMisuraSicurezzaCumulo());
						lMisCumDao.update();
						lMisCumDao.stop();
					}
				}
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiMisuraSicurezzaPerCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiMisuraSicurezzaPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisSicSqlDao);
			cleanup(lMisCumDao);
			cleanup(lMisCumSqlDao);
			cleanup(lMisCumSqlDaoXX);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFascMsSqlDao);
			cleanup(lUffSqldao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	public String trattaDato(String lTipo) throws F3BException {

		String lNat = "";

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lModel = new DecodificheModel();
		lModel.setContesto("TIPO_MISURA_SICUREZZA");
		Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));

		DecodificheModel lModelVec = null;
		Iterator Ite1 = lVec.iterator();
		while (Ite1.hasNext()) {
			lModelVec = (DecodificheModel) Ite1.next();
			if (lModelVec.getCode().compareTo(lTipo) == 0) {
				lNat = lModelVec.getFiltro();
			}
		}
		return lNat;
	}

	/**
	 * Metodo che estrae i dati dei Reati del fascicolo da cumulare e li carica nella tabella REATO_CUMULO
	 * agganciandoli al record CUMULO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiReatiPerCumulo(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitolo,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		ReatoSqlDAO lReaSqlDao = null;
		ReatoCumuloDAO lReaCumDao = null;

		Vector lReati = new Vector();

		ReatoCumuloModel lReaCumPrincipale = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input (Metodo ExEstraiReatiCumulo) ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione (Metodo ExEstraiReatiCumulo)");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati dei reati del fasciolo
			// ========================================================================
			lReaSqlDao = new ReatoSqlDAO(lConn);

			// lReaSqlDao.ricercaReatiByFascicoloSiep(aIdFascicoloCumulato);
			// Estrae solo i reati, non le Aggravanti/Attenuanti
			lReaSqlDao.ricercaReatiNoCircostanzaByFascicoloSiep(aIdFascicoloCumulato);

			lReati = new Vector(lReaSqlDao.getModels());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Reati trovati = " + lReati.size());

			Iterator iter = lReati.iterator();
			while (iter.hasNext()) {
				ReatoModel lReaPrincipale = (ReatoModel) iter.next();

				lReaCumPrincipale = new ReatoCumuloModel(lReaPrincipale);

				lReaCumPrincipale.setFlagStato("E"); // E = Estratto
				lReaCumPrincipale.setMotivoModificaNote("");
				lReaCumPrincipale.setTitIdTitoloCumulato(aTitolo.getIdTitoloCumulato());

				lReaCumPrincipale.setCodOperatoreInserimento(aTitolo.getCodOperatoreInserimento());
				lReaCumPrincipale.setDataInserimento(aTitolo.getDataInserimento());
				lReaCumPrincipale.setCodUfficioInserimento(aTitolo.getCodUfficioInserimento());

				// Inserisco
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco Reato cumulo:");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(lReaCumPrincipale);

				lReaCumDao = new ReatoCumuloDAO(lConn);
				lReaCumDao.setDAOFromModel(lReaCumPrincipale);
				lReaCumDao.insert();
				lReaCumDao.stop();
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiReatiPerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lReaSqlDao);
			cleanup(lReaCumDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	} // CHIUDE ExEstraiReatiPerCumulo()

	/**
	 * Metodo che estrae i dati delle Circostanze (Aggravnti/Attenuanti) del fascicolo da cumulare e li carica
	 * nella tabella CIRCOSTANZA_CUMULO agganciandoli al record CUMULO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiCircostanzePerCumulo(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitolo,
			Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		CircostanzaSqlDAO lCirSqlDao = null;
		CircostanzaCumuloDAO lCirCumDao = null;

		Vector lCirco = new Vector();

		CircostanzaCumuloModel lCirCumuloMod = null;

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input (Metodo ExEstraiCircostanzeCumulo) ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione (Metodo ExEstraiCircostanzeCumulo)");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati delle Circostanze del fasciolo
			// ========================================================================
			lCirSqlDao = new CircostanzaSqlDAO(lConn);
			lCirSqlDao.ricercaCircostanzeByIdFascicolo(aIdFascicoloCumulato);

			lCirco = new Vector(lCirSqlDao.getModels());

			siesLogger.debug("Circostanze trovate = " + lCirco.size());

			Iterator iter = lCirco.iterator();
			while (iter.hasNext()) {
				CircostanzaModel lCircostanza = (CircostanzaModel) iter.next();

				lCirCumuloMod = new CircostanzaCumuloModel(lCircostanza);

				lCirCumuloMod.setFlagStato("E"); // E = Estratto
				lCirCumuloMod.setMotivoModifica("");
				lCirCumuloMod.setTitIdTitoloCumulato(aTitolo.getIdTitoloCumulato());

				lCirCumuloMod.setCodOperatoreInserimento(aTitolo.getCodOperatoreInserimento());
				lCirCumuloMod.setDataInserimento(aTitolo.getDataInserimento());
				lCirCumuloMod.setCodUfficioInserimento(aTitolo.getCodUfficioInserimento());

				// Inserisco
				siesLogger.debug("Inserisco Circostanza cumulo:");
				siesLogger.debug(lCirCumuloMod);

				lCirCumDao = new CircostanzaCumuloDAO(lConn);
				lCirCumDao.setDAOFromModel(lCirCumuloMod);
				lCirCumDao.insert();
				lCirCumDao.stop();
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiCircostanzePerCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lCirSqlDao);
			cleanup(lCirCumDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	/**
	 * Metodo che estrae i dati della Pena-Complessiva, Sanzione Sostitutiva, Continuazione del fascicolo da
	 * cumulare e li carica nelle tabelle PENA_COMPLESSIVA_CUMULO, SANZIONE_SOST_CUM, CONTINUAZIONE_CUMULO
	 * agganciandoli al record TITOLO_CUMULATO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aTitoloCumulato
	 *            = model del TITOLO_CUMULATO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo(BigDecimal aIdFascicoloCumulato,
			TitoloCumulatoModel aTitoloCumulato, Connection aDBConnection) throws F3BException {

		Connection lConn = null;

		PenaComplessivaSqlDAO lPenSqlDao = null;
		SanzioneSostitutivaSqlDAO lSanSqlDao = null;
		ContinuazioneSqlDAO lContSqlDao = null;
		PenaComplessivaCumuloDAO lPenCumDao = null;
		SanzioneSostitutivaCumuloDAO lSanzCumDao = null;
		ContinuazioneCumuloDAO lContCumDao = null;
		ContinuazioneCumuloSqlDAO lContCumSqlDAo = null;
		TitoloCumulatoSqlDAO lTitCumSqlDao = null;

		PenaComplessivaModel lPenMod = null;
		SanzioneSostitutivaModel lSanMod = null;
		List lListContMod = null;

		PenaComplessivaCumuloModel lPenCumMod = null;
		SanzioneSostitutivaCumuloModel lSanCumMod = null;
		ContinuazioneCumuloModel lContCumMod = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Utilizzo connessione in input (Metodo ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo) ");
				lConn = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Apro nuova connessione (Metodo ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo)");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Estraggo i dati della Pena Complessiva del fasciolo Cumulato
			// - - -> MODULO PENA_COMPLESSIVA / PENA_COMPLESSIVA_CUMULO
			// ========================================================================
			lPenSqlDao = new PenaComplessivaSqlDAO(lConn);
			lPenSqlDao.ricercaPenaComplessivaByIdFascicolo(aIdFascicoloCumulato);

			lPenSqlDao.start();
			if (lPenSqlDao.next()) {
				lPenMod = (PenaComplessivaModel) lPenSqlDao.getModel();
			}
			lPenSqlDao.stop();

			if (lPenMod != null) {
				lPenCumMod = new PenaComplessivaCumuloModel(lPenMod);

				lPenCumMod.setTitIdTitoloCumulato(aTitoloCumulato.getIdTitoloCumulato());

				lPenCumMod.setFlagStato("E"); // E = Estratto
				lPenCumMod.setMotivoModifica(null);

				lPenCumMod.setCodOperatoreInserimento(aTitoloCumulato.getCodOperatoreInserimento());
				lPenCumMod.setDataInserimento(aTitoloCumulato.getDataInserimento());
				lPenCumMod.setCodUfficioInserimento(aTitoloCumulato.getCodUfficioInserimento());

				// ======================================================================
				// Estraggo i dati della Sanzione Sostitutiva del fasciolo Cumulato
				// - - -> MODULO SANZIONE_SOSTITUTIVA / SANZIONE_SOST_CUM
				// ======================================================================

				lSanSqlDao = new SanzioneSostitutivaSqlDAO(lConn);
				lSanSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

				lSanSqlDao.start();
				if (lSanSqlDao.next()) {
					lSanMod = (SanzioneSostitutivaModel) lSanSqlDao.getModel();
				}
				lSanSqlDao.stop();

				if (lSanMod != null) {
					lSanCumMod = new SanzioneSostitutivaCumuloModel(lSanMod);

					lSanCumMod.setFlagStato("E"); // E = Estratto
					lSanCumMod.setMotivoModifica(null);

					lSanCumMod.setTitIdTitoloCumulato(aTitoloCumulato.getIdTitoloCumulato());

					lSanCumMod.setCodOperatoreInserimento(aTitoloCumulato.getCodOperatoreInserimento());
					lSanCumMod.setDataInserimento(aTitoloCumulato.getDataInserimento());
					lSanCumMod.setCodUfficioInserimento(aTitoloCumulato.getCodUfficioInserimento());
				}

				// ========================================================================
				// Estraggo i dati della Sentenze in Continuazione del fasciolo Cumulato
				// - - -> MODULO CONTINUAZIONE / CONTINUAZIONE_CUMULO
				// ========================================================================

				lContSqlDao = new ContinuazioneSqlDAO(lConn);
				lContSqlDao.ricercaContinuazioneByIdPenaComplessiva(lPenMod.getIdPenaComplessiva());

				lListContMod = new ArrayList(lContSqlDao.getModels());

				Vector VContCum = new Vector();

				Iterator iter = lListContMod.iterator();

				// Recupero i titoli in istruttoria per verificare se uno di essi coincide
				// con la continuazione
				lTitCumSqlDao = new TitoloCumulatoSqlDAO(lConn);
				Vector<TitoloCumulatoModel> lListaTitoli = null;
				if (iter.hasNext()) {
					// Recupero i titoli solo se esiste almeno una continuazione
					lTitCumSqlDao
							.ricercaTitoloCumulatoByIstruttoria(aTitoloCumulato.getIstrIdIstruttoriaCumulo());
					lTitCumSqlDao.start();
					lListaTitoli = new Vector(lTitCumSqlDao.getModels());
					lTitCumSqlDao.stop();
				}

				while (iter.hasNext()) {
					ContinuazioneModel lContMod = (ContinuazioneModel) iter.next();

					lContCumMod = new ContinuazioneCumuloModel(lContMod);

					lContCumMod.setFlagStato("E"); // E = Estratto
					lContCumMod.setMotivoModifica(null);

					lContCumMod.setTitIdTitoloCumulato(aTitoloCumulato.getIdTitoloCumulato());

					lContCumMod.setCodOperatoreInserimento(aTitoloCumulato.getCodOperatoreInserimento());
					lContCumMod.setDataInserimento(aTitoloCumulato.getDataInserimento());
					lContCumMod.setCodUfficioInserimento(aTitoloCumulato.getCodUfficioInserimento());

					// Verifico se la Continuazione Coincide con uno dei titoli già in istruttoria
					Iterator<TitoloCumulatoModel> lTitoliIter = lListaTitoli.iterator();
					while (lTitoliIter.hasNext()) {
						TitoloCumulatoModel lTitolo = lTitoliIter.next();
						if (lContCumMod.isStessoTitolo(lTitolo)) {
							lContCumMod.setTitIdTitoloCumulatoCont(lTitolo.getIdTitoloCumulato());
							break;
						}
					}

					VContCum.add(lContCumMod);
				}

				// ======================================================================
				// Inserimento Pena Complessiva/Sanzione Sostitutiva/Sentenze in Continuazione
				// ======================================================================
				lPenCumDao = new PenaComplessivaCumuloDAO(lConn);
				lPenCumDao.setDAOFromModel(lPenCumMod);

				BigDecimal lKeyPenaComplessiva = null;
				lKeyPenaComplessiva = lPenCumDao.insert();
				lPenCumMod.setIdPenaComplessivaCum(lKeyPenaComplessiva);

				// Inserimento Sanzione Sostitutiva
				if (lSanCumMod != null) {
					lSanzCumDao = new SanzioneSostitutivaCumuloDAO(lConn);

					lSanCumMod.setPcIdPenaComplessivaCum(lKeyPenaComplessiva);

					lSanzCumDao.setDAOFromModel(lSanCumMod);
					BigDecimal lKeySanzioneSostitutiva = null;
					lKeySanzioneSostitutiva = lSanzCumDao.insert();
					lSanCumMod.setIdSanzioneSostitutivaCum(lKeySanzioneSostitutiva);
				}

				// Inserimento Continuazioni
				if (VContCum.size() > 0) {
					lContCumDao = new ContinuazioneCumuloDAO(lConn);
					lContCumSqlDAo = new ContinuazioneCumuloSqlDAO(lConn);
					Iterator lIter = VContCum.iterator();
					while (lIter.hasNext()) {
						ContinuazioneCumuloModel lItemConCum = (ContinuazioneCumuloModel) lIter.next();

						// Gestione Progressivo
						BigDecimal lProgr = lContCumSqlDAo.getProgressivoContinuazione(lKeyPenaComplessiva);
						lItemConCum.setProgrContinuazione(new BigDecimal(lProgr.intValue() + 1));

						lItemConCum.setPcIdPenaComplessivaCum(lKeyPenaComplessiva);

						lContCumDao.setDAOFromModel(lItemConCum);
						lContCumDao.insert();
						lContCumDao.stop();
					}
				}

				if (aDBConnection == null) {
					commit(lConn);
				}
			}

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo: Non posso inserire: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lPenSqlDao);
			cleanup(lSanSqlDao);
			cleanup(lContSqlDao);
			cleanup(lPenCumDao);
			cleanup(lSanzCumDao);
			cleanup(lContCumDao);
			cleanup(lContCumSqlDAo);
			cleanup(lTitCumSqlDao);

			if (aDBConnection == null)
				cleanup(lConn);
		}
	} // Chiude metodo (void) ExEstraiPenaComplessivaSanzSostContinuazionePerCumulo

	/**
	 * Metod di validazione della Richiesta trasmissione atti
	 *
	 * @param aEvento
	 *            = evento da validate @return @throws
	 */
	public EventoModel ExUpdateValidaRichiestaTrasmissioneAtti(EventoModel aEvento) throws F3BException {

		Connection lConn = null;

		EventoModel lEveMod = new EventoModel(aEvento);

		EventoSqlDAO lEveSqlDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		EventoDAO lEveDaoBlob = null;

		try {
			lConn = getDBTransaction();

			// ========================================================================
			// Recupero l'EVENTO completo, quello in input contiene solo i dati da
			// aggiornare
			// ========================================================================
			lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getIdEvento());
			// EventoModel lEveModel = (EventoModel) lEveSqlDao.getModelByKey();

			// ========================================================================
			// Aggiorna lo stato del PROCEDIMENTO cancellando i record precedenti
			// e inserendo il nuovo stato
			// n.b. data stato = data emissione provvedimento (decreto)
			// ========================================================================
			// StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
			//
			// lStatoProcMod.setProgressivo(new BigDecimal(1));
			// lStatoProcMod.setCodStatoProcedimento("0437"); // Da stabilire new
			//
			// lStatoProcMod.setData(lEveModel.getDataEmissione());
			//
			// lStatoProcMod.setFasSieIdFascicoloSiep(aFascicolo.getIdFascicoloSiep());
			//
			// lStatoProcMod.setCodOperatoreInserimento (aEvento.getCodOperatoreAggiornamento());
			// lStatoProcMod.setDataInserimento (aEvento.getDataAggiornamento());
			// lStatoProcMod.setCodUfficioInserimento (aEvento.getCodUfficioAggiornamento());
			//
			//
			// lStatoProcDao = new StatoProcedimentoDAO(lConn);
			//
			// // - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			// lStatoProcDao.setCondizioneByIdFascicolo(aFascicolo.getIdFascicoloSiep());
			// lStatoProcDao.delete();
			//
			// // - Inserisce
			// lStatoProcDao.setDAOFromModel(lStatoProcMod);
			// lStatoProcDao.insert();
			// lStatoProcDao.stop();

			// ========================================================================
			// Aggiorno lo NOME_PROCEDIMENTO [eventualmente]
			// ========================================================================

			// ========================================================================
			// Aggiorno il blob sull'evento
			// ========================================================================
			lEveDaoBlob = new EventoDAO(lConn);
			lEveDaoBlob.setDAOFromModelForUpdateBlob(aEvento);

			lEveDaoBlob.selCondizioneUpdate(aEvento.getIdEvento());
			lEveDaoBlob.update();
			lEveDaoBlob.stop();

			commit(lConn);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("ModuloCumuloController.ExUpdateValidaRichiestaTrasmissioneAtti : " + ex);
		} finally {
			cleanup(lEveSqlDao);
			cleanup(lStatoProcDao);
			cleanup(lEveDaoBlob);
			cleanup(lConn);
		}

		return lEveMod;
	}

	public Vector<MessaggioModel> ExRicercaMessaggi(String aDeliveryMode, String aCodTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante, int aPage)
			throws F3BException {

		Connection lConn = null;

		MessaggioSqlDAO lMessaggioSqlDAO = null;

		Vector<MessaggioModel> lLista = new Vector<>();

		try {
			lConn = getDBConnection();

			lMessaggioSqlDAO = new MessaggioSqlDAO(lConn);

			lMessaggioSqlDAO.ricercaMessaggioRicercaConFiltri(aDeliveryMode, aCodTipoMessaggio,
					aListaTipoOperazione, aListaEsiti, aFlagVisto, aChiaveAnnoSiep, aChiaveProgrSiep,
					aChiaveUfficioSiep, aCodUfficioMitt, null // Utente Mitt
					, aCodUfficioDest // Ufficio Dest
					, aDataTrasmissioneDal, aDataTrasmissioneAl, aCognome, aNome, null // aIdMessSollecitato
					, aChiaveAnnoFasCumulante, aChiaveProgrFasCumulante, aChiaveUfficioFasCumulante, aPage);

			lLista = new Vector<MessaggioModel>(lMessaggioSqlDAO.getModels());

			lMessaggioSqlDAO.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ModuloCumuloController.ExRicercaMessaggi: ", daoEx);
			throw new F3BException("ModuloCumuloController.ExRicercaMessaggi: " + daoEx);
		} finally {
			cleanup(lMessaggioSqlDAO);

			cleanup(lConn);
		}

		return lLista;
	}

	public Vector<MessaggioModel> ExRicercaMessaggi(String aDeliveryMode, Vector<String> aListaTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante, int aPage)
			throws F3BException {

		Connection lConn = null;

		MessaggioSqlDAO lMessaggioSqlDAO = null;

		Vector<MessaggioModel> lLista = new Vector<>();

		try {
			lConn = getDBConnection();

			lMessaggioSqlDAO = new MessaggioSqlDAO(lConn);

			lMessaggioSqlDAO.ricercaMessaggioRicercaConFiltri(aDeliveryMode, aListaTipoMessaggio,
					aListaTipoOperazione, aListaEsiti, aFlagVisto, aChiaveAnnoSiep, aChiaveProgrSiep,
					aChiaveUfficioSiep, aCodUfficioMitt, null // Utente Mitt
					, aCodUfficioDest // Ufficio Dest
					, aDataTrasmissioneDal, aDataTrasmissioneAl, aCognome, aNome, null // aIdMessSollecitato
					, aChiaveAnnoFasCumulante, aChiaveProgrFasCumulante, aChiaveUfficioFasCumulante, aPage);

			lLista = new Vector<MessaggioModel>(lMessaggioSqlDAO.getModels());

			lMessaggioSqlDAO.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("ModuloCumuloController.ExRicercaMessaggi: ", daoEx);
			throw new F3BException("ModuloCumuloController.ExRicercaMessaggi: " + daoEx);
		} finally {
			cleanup(lMessaggioSqlDAO);
			cleanup(lConn);
		}

		return lLista;
	}

	public BigDecimal ExCountRicercaMessaggi(String aDeliveryMode, String aCodTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante) throws Exception {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesSqlDao = null;

		try {
			lConn = getDBConnection();

			lMesSqlDao = new MessaggioSqlDAO(lConn);

			lMesSqlDao.getCountRicercaMessaggioRicercaConFiltri(aDeliveryMode, aCodTipoMessaggio,
					aListaTipoOperazione, aListaEsiti, aFlagVisto, aChiaveAnnoSiep, aChiaveProgrSiep,
					aChiaveUfficioSiep, aCodUfficioMitt, null // Utente Mitt
					, aCodUfficioDest, aDataTrasmissioneDal, aDataTrasmissioneAl, aCognome, aNome);

			lMesSqlDao.start();
			lMesSqlDao.next();
			lCount = lMesSqlDao.getBigDecimal("HowManyRecords");
			lMesSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ModuloCumuloController.ExCountRicercaMessaggi: " + daoEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return lCount;
	}

	public BigDecimal ExCountRicercaMessaggi(String aDeliveryMode, Vector<String> aListaTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante) throws Exception {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MessaggioSqlDAO lMesSqlDao = null;

		try {
			lConn = getDBConnection();

			lMesSqlDao = new MessaggioSqlDAO(lConn);

			lMesSqlDao.getCountRicercaMessaggioRicercaConFiltri(aDeliveryMode, aListaTipoMessaggio,
					aListaTipoOperazione, aListaEsiti, aFlagVisto, aChiaveAnnoSiep, aChiaveProgrSiep,
					aChiaveUfficioSiep, aCodUfficioMitt, null // Utente Mitt
					, aCodUfficioDest, aDataTrasmissioneDal, aDataTrasmissioneAl, aCognome, aNome);

			lMesSqlDao.start();
			lMesSqlDao.next();
			lCount = lMesSqlDao.getBigDecimal("HowManyRecords");
			lMesSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ModuloCumuloController.ExCountRicercaMessaggi: " + daoEx);
		} finally {
			cleanup(lMesSqlDao);
			cleanup(lConn);
		}

		siesLogger.info("[JMS]: fine");
		return lCount;
	}

	public Vector<SentenzaSoggettoFascicoloModel> ExRicercaProcedimentiPerTitoloSoggetto(
			SentenzaModel aSentenzaModel, SoggettoModel aSoggettoModel,
			FascicoloSiepModel aFascicoloSiepModel, int aPage) throws Exception {

		Connection lConn = null;

		siesLogger.debug("-ModuloCumuloController --> ExRicercaProcedimentiPerTitoloSoggetto");

		Vector<SentenzaSoggettoFascicoloModel> lListaProcedimenti = new Vector<>();
		ModuloCumuloSqlDao lModuloCumuloSqlDao = null;
		SentenzaSoggettoFascicoloModel FasciSentMod = new SentenzaSoggettoFascicoloModel();

		try {
			lConn = getDBConnection();
			lModuloCumuloSqlDao = new ModuloCumuloSqlDao(lConn);
			// Dati Obbligatori in aSentenzaModel: Tipo e Data Provvedimento, Tipo e Sede Autorità Emittente;
			// Gli altri eventuali dati in aSoggettoModel e aFascicoloSiepModel sono Facoltativi
			lModuloCumuloSqlDao.RicercaProcedimentiPerTitoloSoggetto(aSentenzaModel, aSoggettoModel,
					aFascicoloSiepModel, aPage);

			lModuloCumuloSqlDao.start();
			while (lModuloCumuloSqlDao.next()) {
				FasciSentMod = (SentenzaSoggettoFascicoloModel) lModuloCumuloSqlDao
						.getModelTitoliProcSoggetto();
				siesLogger.debug("--XX-- Trovato sentenzaFasciMod = " + FasciSentMod);
				lListaProcedimenti.add(FasciSentMod);
			}

			lModuloCumuloSqlDao.stop();

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ModuloCumuloController.ExRicercaProcedimentiPerTitoloSoggetto: " + daoEx);
		} finally {
			cleanup(lModuloCumuloSqlDao);
			cleanup(lConn);
		}

		// siesLogger.debug("Size() = "+lListaProcedimenti.size());
		return lListaProcedimenti;
	}

	public BigDecimal ExCountProcedimentiPerTitoloSoggetto(SentenzaModel aSentenzaModel,
			SoggettoModel aSoggettoModel, FascicoloSiepModel aFascicoloSiepModel) throws Exception {

		Connection lConn = null;

		siesLogger.debug("ExCountProcedimentiPerTitoloSoggetto");

		BigDecimal lContaRecord = new BigDecimal(0);
		ModuloCumuloSqlDao lModuloCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lModuloCumuloSqlDao = new ModuloCumuloSqlDao(lConn);
			lModuloCumuloSqlDao.CountProcedimentiPerTitoloSoggetto(aSentenzaModel, aSoggettoModel,
					aFascicoloSiepModel);
			lModuloCumuloSqlDao.start();
			lModuloCumuloSqlDao.next();
			lContaRecord = lModuloCumuloSqlDao.getBigDecimal("HowManyRecords");
			lModuloCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ModuloCumuloController.ExCountProcedimentiPerTitoloSoggetto: " + daoEx);
		} finally {
			cleanup(lModuloCumuloSqlDao);
			cleanup(lConn);
		}

		return lContaRecord;
	}

	public BigDecimal ExInserisciSollecitoRichiestaAtti(EventoNotificaModel aEventoNot,
			SollecitoEsitoTrasmissioneModel aSollecitoModel, CompetenzaModel aCompetenza)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" --XXX-- ExInserisciSollecitoRichiestaAtti - Inizio inserimento sollecito...");

		Connection lConn = null;

		EventoSqlDAO lEventoSqlDao = null;
		EventoDAO lEveDao = null;
		CampoNotaDAO lCampoNotaDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		SollecitoEsitoTrasmissioneDAO lSollecitoDAO = null;
		CompetenzaDAO lCompDAO = null;

		BigDecimal lKeyEvento = null;
		try {
			lConn = getDBConnection();

			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);

			// Setto l'anno e il progressivo...
			lEventoSqlDao = new EventoSqlDAO(lConn);
			BigDecimal lProgr = lEventoSqlDao.getProgressivo(aEventoNot.getEvento());
			aEventoNot.getEvento().setProgrProtocollo(new BigDecimal(lProgr.intValue() + 1));

			// =============================================
			// Insert dell'evento
			// =============================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEventoNot.getEvento());
			lKeyEvento = lEveDao.insert();
			lEveDao.stop();

			// =============================================
			// Insert delle notifiche
			// =============================================
			if (aEventoNot.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" --XXX-- Presenti " + aEventoNot.getNotifiche().length + " notifiche");
				int count = 0;
				while (count < aEventoNot.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEventoNot.getNotifiche()[count]);

					if (aEventoNot.getNotifiche()[count] != null) {

						if (aEventoNot.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(
									aEventoNot.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();
							BigDecimal lKeyAutorita = null;
							if (lAutMod == null) {
								lAutDao.setDAOFromModel(
										aEventoNot.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
								aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEventoNot.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEventoNot.getNotifiche()[count].setEveIdEvento(lKeyEvento);

						lNotDao.setDAOFromModel(aEventoNot.getNotifiche()[count]);
						lNotDao.insert();
						lNotDao.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Inserito evento" + lKeyEvento);
					}
					count++;
				}
			}

			// =============================================
			// Insert Note
			// =============================================
			if (aEventoNot.getCampoNote() != null) {
				int count = 0;
				lCampoNotaDao = new CampoNotaDAO(lConn);
				while (count < aEventoNot.getCampoNote().length) {
					aEventoNot.getCampoNote()[count].setEveIdEvento(lKeyEvento);
					aEventoNot.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
					lCampoNotaDao.setDAOFromModel(aEventoNot.getCampoNote()[count]);
					lCampoNotaDao.insert();
					lCampoNotaDao.stop();

					count++;
				}
			}

			// =============================================
			// Insert del Sollecito
			// =============================================
			aSollecitoModel.setEveIdEvento(lKeyEvento);

			lSollecitoDAO = new SollecitoEsitoTrasmissioneDAO(lConn);
			lSollecitoDAO.setDAOFromModel(aSollecitoModel);
			BigDecimal lKeySollecito = lSollecitoDAO.insert();
			lSollecitoDAO.stop();
			aSollecitoModel.setIdSollecito(lKeySollecito);

			// =============================================
			// Insert Competenza
			// =============================================
			aCompetenza.setEveIdEvento(lKeyEvento);

			lCompDAO = new CompetenzaDAO(lConn);
			lCompDAO.setDAOFromModel(aCompetenza);
			lCompDAO.insert();
			lCompDAO.stop();

			// rollback(lConn);
			commit(lConn);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("ModuloCumuloController.ExInserisciSollecitoRichiestaAtti: ", e);
			throw new F3BException("ModuloCumuloController.ExInserisciSollecitoRichiestaAtti: " + e);
		} finally {
			cleanup(lEventoSqlDao);
			cleanup(lEveDao);
			cleanup(lCampoNotaDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSollecitoDAO);
			cleanup(lCompDAO);

			cleanup(lConn);
		}
		return lKeyEvento;
	} // Chiude ExInserisciSollecitoRichiestaAtti

	/**
	 * Recupera i provvedimenti dello stato esecuzione del fascicolo che si sta iscrivendo in istruttoria e
	 * ribalta i provvedimenti. n.b. Il metodo committa/rollbacca un provvedimento alla volta in quanto data
	 * l'etereogeneità dei provvedimenti è possibile che qualche dato non sia caricabile. L'errore su un
	 * provvedimento NON deve bloccare l'iscrizione in istruttoria dell'intero procedimento.
	 *
	 * Il metodo lavora sulla connessione in input ma committa singolarmente quindi il chiamante deve essere
	 * cosciente della commit.
	 *
	 * @param aIdFascicoloCumulato
	 * @param aTitolo
	 * @param aDBConnection
	 * @param aDatiOper
	 * @throws F3BException
	 */
	private void ExEstraiStatoEsecuzione(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitolo,
			Connection aDBConnection, DatiOperazioneModel aDatiOper) throws F3BException {

		Connection lConn = null;

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			siesLogger.debug("Caricamento stato esecuzione");
			siesLogger.debug("aIdFascicoloCumulato = " + aIdFascicoloCumulato);
			siesLogger.debug("aTitolo.getIdTitoloCumulato() = " + aTitolo.getIdTitoloCumulato());
			siesLogger
					.debug("aTitolo.getIstrIdIstruttoriaCumulo() = " + aTitolo.getIstrIdIstruttoriaCumulo());

			siesLogger.debug("Recupero gli eventi del fascicolo...");
			ITitoloCumulato lTitoloCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
			Vector<MisuraAlternativaAggregatoModel> lListaEventi = null;
			lListaEventi = lTitoloCtrl
					.ExRicercaEventiPerStatoEsecuzioneByFascicoloSiepPaged(aIdFascicoloCumulato, 0);
			siesLogger.debug("Eventi recuperati: " + lListaEventi.size());

			siesLogger.debug("Carico gli idEvento...");
			Vector<BigDecimal> lListaIdEventiDaInserire = new Vector<>();
			for (MisuraAlternativaAggregatoModel lAggregatoModel : lListaEventi) {
				EventoModel lEvento = lAggregatoModel.getEventoNotifica().getEvento();
				lListaIdEventiDaInserire.add(lEvento.getIdEvento());
			}

			Vector<StatoEsecTitoloCumulatoModel> lListaDaRimuovere = new Vector<>();

			siesLogger.debug("Procedo al caricamento dello stato esecuzione");
			IStatoEsecTitoloCumulato lCtrlStatEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			lCtrlStatEsec.ExAggiornaStatoEsecTitoloCumulatoByIdTitolo(lListaIdEventiDaInserire,
					lListaDaRimuovere, aTitolo.getIdTitoloCumulato(), aTitolo.getIstrIdIstruttoriaCumulo(),
					lConn, aDatiOper, true);

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (F3BException ex) {
			rollback(lConn);
			siesLogger.error("Errore in fase di estrazione dello stato di esecuzione", ex);
			throw new F3BException(
					"ModuloCumuloController.ExEstraiStatoEsecuzione: Non posso inserire: " + ex);
		} finally {
			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	/**
	 *
	 * @param aUltimaIstruttoria
	 *            - Istruttoria da cui estrarre i dati
	 * @param aIdIstruttoriaCumulo
	 *            - Istruttoria a cui agganciare i dati in copia
	 * @param aFascicoloSiepCumulato
	 *            - fascicolo che si sta cumulando
	 * @param aDatoOpModel
	 * @param aDBConnection
	 * @param aTipoIscrizione
	 *            - ??? Per ora non utilizzata
	 * @throws F3BException
	 */
	/*
	 * MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata il metodo diventa public per essere richiamato
	 * anche da IStruttoriaCumuloController
	 */
	// private void EstraiDaPrecedenteCumulo(IstruttoriaCumuloModel aUltimaIstruttoria,
	public void EstraiDaPrecedenteCumulo(IstruttoriaCumuloModel aUltimaIstruttoria,
			BigDecimal aIdIstruttoriaCumulo, FascicoloSiepModel aFascicoloSiepCumulato,
			DatiOperazioneModel aDatoOpModel, Connection aDBConnection, String aTipoIscrizione)
			throws F3BException {

		Connection lConn = null;

		TitoloCumulatoSqlDAO lTitoloCumSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcedimentoSqlDao = null;
		ContinuazioneCumuloDAO lContCumDao = null;
		BeneficioCumuloDAO lBenCumDao = null;

		// Ticket#202601140182 - SIEP : Doppio titolo Esecutivo
		FascicoloSiepDAO lFascSiepDao = null;
		IstruttoriaCumuloDAO lIstrDAO = null;
		// Ticket#202601140182 - FINE

		// Le tabelle del cumulo si referenziano tra di loro per cui in fase di
		// copia dei record di un titolo vanno ricreati i collegamneti anche sui
		// nuovi record inseriti in copia (FK logiche e/o fisiche)
		// Poichè tale collegamento NON è sempre possibile effettuarlo in fase di
		// primo inserimento, il record da puntare non è stato ancora inserito in copia,
		// si salvano nelle HashTable i collegamenti originari e/o la mappatura
		// del vecchio id con l'id di copia, in modo da procedere a tale aggiornamento
		// in un secondo momento.
		//
		//
		// Mappa gli idTitoloCumulato Origine con quelli della copia Hashtable: <ID Origine, ID Copia>
		// La tabella TITOLO_CUMULATO è puntata: da tutte le ulteriori tabelle da inserire per lo stesso
		// titolo
		// ma anche da CONTINUAZIONE_CUMULO di altri titoli
		Hashtable<BigDecimal, BigDecimal> lIdTitOrigNewMap = new Hashtable<>();

		// lIdPenaCompOrigNewMap = tiene traccia degli idPenaComplessiva Origine e quelli nuovi necessari
		// per aggiornare il campo CONTINUAZIONE_CUMULO.PC_ID_PENA_COMPLESSIVA_CUM (serve a continuazione)
		// Hashtable <BigDecimal, BigDecimal> lIdPenaCompOrigNewMap = new Hashtable <BigDecimal,
		// BigDecimal>();

		// lIdContNewIdPenCompOldMap = tiene traccia degli id record CONTINUAZIONE_CUMULO nuovi e del
		// idPenCum che puntava originariamente per poter aggiornare il campo
		// CONTINUAZIONE_CUMULO.PC_ID_PENA_COMPLESSIVA_CUM
		// Hashtable <BigDecimal, BigDecimal> lIdContNewIdPenCompOldMap = new Hashtable <BigDecimal,
		// BigDecimal>();

		// lIdContNewIdTitContOldMap = tiene traccia degli id record CONTINUAZIONE_CUMULO nuovi e del
		// idTitCont che puntava originariamente per poter aggiornare il campo
		// CONTINUAZIONE_CUMULO.TIT_ID_TITOLO_CUMULATO_CONT
		Hashtable<BigDecimal, BigDecimal> lIdContNewIdTitContOldMap = new Hashtable<>();

		// Mappa gli id dei record PENA_ACCESSORIA_CUMULO inseriti con gli id Beneficio cumulo Old da
		// aggiornare
		// Hashtable <BigDecimal, BigDecimal> lIdPenAccNewIdBenCumOldMap = new Hashtable <BigDecimal,
		// BigDecimal>();

		// Mappa gli id dei record BENEFICIO_CUMULO originari con quelli inserti in copia
		// La tabella BENEFICIO_CUMULO è Referenziata da: BENEFICIO_CUMULO
		Hashtable<BigDecimal, BigDecimal> lIdBenCumOrigNewMap = new Hashtable<>();
		Hashtable<BigDecimal, BigDecimal> lIdBenCumNewBenIdBeneOldMap = new Hashtable<>();
		Hashtable<BigDecimal, BigDecimal> lIdBenCumNewTitIdTitCollOldMap = new Hashtable<>();

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// Ticket#202601140182 - SIEP : Doppio titolo Esecutivo
			// recupero dati del cumulante principale
			siesLogger.debug("Istruttoria Principale: " + aIdIstruttoriaCumulo);
			lIstrDAO = new IstruttoriaCumuloDAO(lConn);
			lIstrDAO.selCondizioneUpdate(aIdIstruttoriaCumulo);
			IstruttoriaCumuloModel lIstruttoria = (IstruttoriaCumuloModel) lIstrDAO.getModelByKey();

			siesLogger.debug(
					"Ricerca il fascicolo cumulante principale: " + lIstruttoria.getFasSieIdFascicoloSiep());
			lFascSiepDao = new FascicoloSiepDAO(lConn);
			lFascSiepDao.selCondizioneUpdate(lIstruttoria.getFasSieIdFascicoloSiep());
			FascicoloSiepModel lFasCumulantePrincipale = (FascicoloSiepModel) lFascSiepDao.getModelByKey();
			siesLogger.debug("lFasCumulantePrincipale = " + lFasCumulantePrincipale);
			// Ticket#202601140182 - FINE

			lTitoloCumSqlDao = new TitoloCumulatoSqlDAO(lConn);

			lTitoloCumSqlDao.ricercaTitoloCumulatoByIstruttoria(aUltimaIstruttoria.getIdIstruttoriaCumulo());
			Vector<TitoloCumulatoModel> lListaTitoli = new Vector<TitoloCumulatoModel>(
					lTitoloCumSqlDao.getModels());

			siesLogger.debug("Titoli trovati = " + lListaTitoli.size());

			for (int i = 0; i < lListaTitoli.size(); i++) {
				TitoloCumulatoModel lTitolo = lListaTitoli.elementAt(i);
				siesLogger.debug("lTitolo = " + lTitolo);

				lProcedimentoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

				siesLogger.debug("Recupero il procedimento cumulato se presente...");
				ProcedimentoCumulatoModel lProcModel = null;
				lProcedimentoSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
				lProcedimentoSqlDao.start();
				if (lProcedimentoSqlDao.next())
					lProcModel = (ProcedimentoCumulatoModel) lProcedimentoSqlDao.getModel();

				siesLogger.debug("lProcModel = " + lProcModel);
				siesLogger.debug("aFascicoloSiepCumulato = " + aFascicoloSiepCumulato);

				boolean lStessoTitolo = false;
				if (lProcModel != null) {
					// Ticket#202601140182 - SIEP : Doppio titolo Esecutivo
					// Attenzione che se l'istruttoria non è del fascicolo cumulante (secono cumulo)
					// ma viene da un cumulo presente su un cumulato è possibile che il cumulante corrente
					// fosse in istruttoria
					// if (aFascicoloSiepCumulato.getChiaveAnno()
					// .compareTo(lProcModel.getChiaveAnnoFasCumulato()) == 0
					// && aFascicoloSiepCumulato.getChiaveProgr()
					// .compareTo(lProcModel.getChiaveProgrFasCumulato()) == 0
					// && aFascicoloSiepCumulato.getChiaveUfficio()
					// .equals(lProcModel.getCodUfficioFasCumulato())) {
					// siesLogger.debug("Stesso titolo del cumulante lo salto");
					// lStessoTitolo = true;
					// }

					if (lFasCumulantePrincipale.getChiaveAnno()
							.compareTo(lProcModel.getChiaveAnnoFasCumulato()) == 0
							&& lFasCumulantePrincipale.getChiaveProgr()
									.compareTo(lProcModel.getChiaveProgrFasCumulato()) == 0
							&& lFasCumulantePrincipale.getChiaveUfficio()
									.equals(lProcModel.getCodUfficioFasCumulato())) {
						siesLogger.debug("Stesso titolo del cumulante principale lo salto");
						lStessoTitolo = true;
					}
					// Ticket#202601140182 - FINE
				} else {
					siesLogger.debug("Procedimento cumulato assente");
				}

				if (!lStessoTitolo) {
					siesLogger.debug("Procedo all'acquisizione");
					CopiaDatiTitolo(lTitolo, aIdIstruttoriaCumulo, aDatoOpModel, lIdTitOrigNewMap,
							lIdContNewIdTitContOldMap, lIdBenCumOrigNewMap, lIdBenCumNewBenIdBeneOldMap,
							lIdBenCumNewTitIdTitCollOldMap, lConn);
				}
			}

			// ========================================================================
			// Al termine della copia di tutti i titoli devo aggiornare i riferimenti
			// che riguardano i dati relativi a titoli differenti: dato del titolo A
			// che punta un dato del titolo B. I riferimenti interni al singolo titolo
			// sono già stati risolti dal metodo CopiaDatiTitolo, mentre i riferimenti
			// esterni sono stati memorizzati nelle HashTable per essere aggiornati al
			// termine dell'inserimento di TUTTI i titoli.
			// ========================================================================
			// Aggiorno sulla tabella CONTINUAZIONE_CUMULO i riferimenti a TIT_ID_TITOLO_CUMULATO_CONT
			// ovvero il ref al titolo che è in continuazione (altro titolo)
			if (!lIdContNewIdTitContOldMap.isEmpty()) {
				lContCumDao = new ContinuazioneCumuloDAO(lConn);

				siesLogger.debug(
						"Aggiorno sulla tabella CONTINUAZIONE_CUMULO i puntamenti a TIT_ID_TITOLO_CUMULATO_CONT...");
				Enumeration<BigDecimal> keys = lIdContNewIdTitContOldMap.keys();
				while (keys.hasMoreElements()) {
					BigDecimal idConttoUpdate = keys.nextElement();
					BigDecimal idTitCumOld = lIdContNewIdTitContOldMap.get(idConttoUpdate);
					BigDecimal idTitCumNew = lIdTitOrigNewMap.get(idTitCumOld);

					siesLogger.debug("Record aggiornato: idConttoUpdate = " + idConttoUpdate
							+ ", TitIdTitCumCont = " + idTitCumNew);
					lContCumDao.setTitIdTitoloCumulatoCont(idTitCumNew);
					lContCumDao.setCondizioneUpdate(idConttoUpdate);
					lContCumDao.update();
					lContCumDao.stop();
				}
			}

			// Aggiorno sulla tabella BENEFICIO_CUMULO i riferimenti a TIT_ID_TITOLO_CUMULO_COLLEGATO
			// ovvero al titolo su cui si revocano i benefici o che hanno revocato i benefici
			if (!lIdBenCumNewTitIdTitCollOldMap.isEmpty()) {
				lBenCumDao = new BeneficioCumuloDAO(lConn);

				siesLogger.debug(
						"Aggiorno sulla tabella BENEFICIO_CUMULO i puntamenti a TIT_ID_TITOLO_CUMULO_COLLEGATO...");
				Enumeration<BigDecimal> keys = lIdBenCumNewTitIdTitCollOldMap.keys();
				while (keys.hasMoreElements()) {
					BigDecimal idBenCumToUpdate = keys.nextElement();
					BigDecimal idTitCumOld = lIdBenCumNewTitIdTitCollOldMap.get(idBenCumToUpdate);
					BigDecimal idTitCumNew = lIdTitOrigNewMap.get(idTitCumOld);

					siesLogger.debug("Record aggiornato: idBenCumToUpdate = " + idBenCumToUpdate
							+ ", TitIdTitCumColl = " + idTitCumNew);
					lBenCumDao.setTitIdTitoloCumulatoCollegato(idTitCumNew);
					lBenCumDao.setCondizioneUpdate(idBenCumToUpdate);
					lBenCumDao.update();
					lBenCumDao.stop();
				}
			}

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.estraiDaPrecedenteCumulo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.estraiDaPrecedenteCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lTitoloCumSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lProcedimentoSqlDao);
			cleanup(lContCumDao);
			cleanup(lBenCumDao);

			// Ticket#202601140182 - SIEP : Doppio titolo Esecutivo
			cleanup(lFascSiepDao);
			cleanup(lIstrDAO);
			// Ticket#202601140182 - FINE

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	/**
	 * Copia tutti i dati di un titoloCumulato agganciandoli all'struttoria
	 *
	 *
	 * @param aTitolo
	 * @param aIdIstruttoriaCumulo
	 *            - Nuova Istruttoria cumulo a cui agganciare i dati
	 * @param aDatoOpModel
	 * @param aDBConnection
	 * @param
	 */
	private void CopiaDatiTitolo(TitoloCumulatoModel aTitoloOrigine, BigDecimal aIdIstruttoriaCumulo,
			DatiOperazioneModel aDatoOpModel, Hashtable<BigDecimal, BigDecimal> aIdTitOrigNewMap,
			Hashtable<BigDecimal, BigDecimal> aIdContNewIdTitContOldMap,
			Hashtable<BigDecimal, BigDecimal> aIdBenCumOrigNewMap,
			Hashtable<BigDecimal, BigDecimal> aIdBenCumNewBenIdBeneOldMap,
			Hashtable<BigDecimal, BigDecimal> aIdBenCumNewTitIdTitCollOldMap, Connection aDBConnection)
			throws F3BException {

		Connection lConn = null;

		TitoloCumulatoDAO lTitoloCumDao = null;
		ProcedimentoCumulatoSqlDAO lProcedimentoSqlDao = null;
		ProcedimentoCumulatoDAO lProcedimentoDao = null;
		SoggettoCumulatoSqlDAO lSoggettoCumulatoSqlDao = null;
		SoggettoCumulatoDAO lSoggettoCumulatoDao = null;
		ReatoCumuloSqlDAO lReatoCumSqlDao = null;
		ReatoCumuloDAO lReatoCumDao = null;
		CircostanzaCumuloSqlDAO lCircostCumSqlDao = null;
		CircostanzaCumuloDAO lCircostCumDao = null;
		PenaComplessivaCumuloSqlDAO lPenaCompSqlDao = null;
		PenaComplessivaCumuloDAO lPenaCompDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSanSostCumSqlDao = null;
		SanzioneSostitutivaCumuloDAO lSanSostCumDao = null;
		ContinuazioneCumuloSqlDAO lContinuaCumSqlDao = null;
		ContinuazioneCumuloDAO lContinuaCumDao = null;
		PenaAccessoriaCumuloSqlDAO lPenAccCumSqlDao = null;
		PenaAccessoriaCumuloDAO lPenAccCumDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSicCumSqlDao = null;
		MisuraSicurezzaCumuloDAO lMisSicCumDao = null;
		MisuraCautelareCumuloSqlDAO lMisCautCumSqlDao = null;
		MisuraCautelareCumuloDAO lMisCautCumDao = null;
		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		BeneficioCumuloDAO lBenCumDao = null;
		// - STATO_ESEC_TITOLO_CUMULATO
		// - NOTIFICA_CUMULO
		// - COMPUTI_CUMULO
		// - LIB_ANTICIPATA_CUMULO
		// - PERIODO_LIB_ANT_CUMULO
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecSqlDao = null;
		StatoEsecTitoloCumulatoDAO lStatoEsecDao = null;
		NotificaCumuloSqlDAO lNotificaSqlDao = null;
		NotificaCumuloDAO lNotificaDao = null;
		ComputiCumuloSqlDAO lComputiCumSqlDao = null;
		ComputiCumuloDAO lComputiCumDao = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		LibAnticipataCumuloDAO lLibAntCumDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoLibAntCumSqlDao = null;
		PeriodoLibAntCumuloDAO lPeriodoLibAntCumDao = null;

		Hashtable<BigDecimal, BigDecimal> lIdReatoOrigIdReatoNewMap = new Hashtable<>();
		Hashtable<BigDecimal, BigDecimal> lIdPenaCompOrigNewMap = new Hashtable<>();
		Hashtable<BigDecimal, BigDecimal> lIdPenAccNewIdBenCumOldMap = new Hashtable<>();
		// Hashtable<BigDecimal, BigDecimal> lIdLibAntOrigIdLibAntNewMap = new Hashtable<BigDecimal,
		// BigDecimal>();

		String lTipoIscrizione = "02"; // FIXME per ora manuale 02
		String lFlagStato = "E"; // FIXME Estratto

		try {
			if (aDBConnection != null) {
				siesLogger.debug("Utilizzo connessione in input ");
				lConn = aDBConnection;
			} else {
				siesLogger.debug("Apro nuova connessione");
				lConn = getDBConnection();
			}

			// ========================================================================
			// Inserimento Titolo_cumulato
			// ========================================================================
			BigDecimal lIdTitoloIns = null;
			siesLogger.debug("Titolo Origine = " + aTitoloOrigine);
			TitoloCumulatoModel lTitoloModelNew = new TitoloCumulatoModel(aTitoloOrigine);

			lTitoloModelNew.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);
			lTitoloModelNew.setTipoIscrizione(lTipoIscrizione);

			// FIXME da stabilire cosa scrivere in questo campo
			lTitoloModelNew.setMessIdMessaggio(null);

			lTitoloModelNew.setFlagStato(lFlagStato);
			lTitoloModelNew.setMotivoModifica(null);

			lTitoloModelNew.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
			lTitoloModelNew.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
			lTitoloModelNew.setDataInserimento(aDatoOpModel.getData());

			lTitoloModelNew.setCodOperatoreAggiornamento(null);
			lTitoloModelNew.setCodUfficioAggiornamento(null);
			lTitoloModelNew.setDataAggiornamento(null);

			siesLogger.debug("Titolo Da Inserire = " + lTitoloModelNew);

			lTitoloCumDao = new TitoloCumulatoDAO(lConn);
			lTitoloCumDao.setDAOFromModel(lTitoloModelNew);
			lIdTitoloIns = lTitoloCumDao.insert();

			aIdTitOrigNewMap.put(aTitoloOrigine.getIdTitoloCumulato(), lIdTitoloIns);

			// ========================================================================
			// Inserimento Procedimento_Cumulato
			// ========================================================================
			siesLogger.debug("Recupero il Procedimento Cumulato");

			lProcedimentoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			ProcedimentoCumulatoModel lProcModel = null;
			lProcedimentoSqlDao.ricercaProcedimentoCumulatoByIdTitolo(aTitoloOrigine.getIdTitoloCumulato());
			lProcModel = (ProcedimentoCumulatoModel) lProcedimentoSqlDao.getModelByKey();

			if (lProcModel != null) {
				siesLogger.debug("ProcedimentoCumulato presente...");
				lProcModel.setTitIdTitoloCumulato(lIdTitoloIns);

				lProcModel.setEveIdEvento(null);// ripulisco il collegamento con l'evento di trasmissione

				lProcModel.setFlagStato(lFlagStato);
				lProcModel.setMotivoModifica(null);

				lProcModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
				lProcModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
				lProcModel.setDataInserimento(aDatoOpModel.getData());

				lProcModel.setCodOperatoreAggiornamento(null);
				lProcModel.setCodUfficioAggiornamento(null);
				lProcModel.setDataAggiornamento(null);

				siesLogger.debug("Inserisco il ProcedimentoCumulato = " + lProcModel);
				lProcedimentoDao = new ProcedimentoCumulatoDAO(lConn);
				lProcedimentoDao.setDAOFromModel(lProcModel);
				lProcedimentoDao.insert();
			}

			// ========================================================================
			// Inserimento Soggetto_cumulato
			// ========================================================================
			siesLogger.debug("Recupero il Soggetto Cumulato");
			lSoggettoCumulatoSqlDao = new SoggettoCumulatoSqlDAO(lConn);

			lSoggettoCumulatoSqlDao.ricercaSoggettoCumulatoByIdTitolo(aTitoloOrigine.getIdTitoloCumulato());

			SoggettoCumulatoModel lSoggCumModel = (SoggettoCumulatoModel) lSoggettoCumulatoSqlDao
					.getModelByKey();

			if (lSoggCumModel != null) {
				siesLogger.debug("Soggetto Cumulato presente...");

				lSoggCumModel.setTitIdTitoloCumulato(lIdTitoloIns);

				lSoggCumModel.setFlagStato(lFlagStato);
				lSoggCumModel.setMotivoModifica(null);

				lSoggCumModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
				lSoggCumModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
				lSoggCumModel.setDataInserimento(aDatoOpModel.getData());

				lSoggCumModel.setCodOperatoreAggiornamento(null);
				lSoggCumModel.setCodUfficioAggiornamento(null);
				lSoggCumModel.setDataAggiornamento(null);

				siesLogger.debug("Inserisco il Soggetto Cumulato = " + lSoggCumModel);
				lSoggettoCumulatoDao = new SoggettoCumulatoDAO(lConn);
				lSoggettoCumulatoDao.setDAOFromModel(lSoggCumModel);
				lSoggettoCumulatoDao.insert();
			} else
				siesLogger.debug("Soggetto Cumulato non presente");

			// ========================================================================
			// Inserimento ReatoCumulo
			// ========================================================================
			siesLogger.debug("Recupero i Reato Cumulo");
			lReatoCumSqlDao = new ReatoCumuloSqlDAO(lConn);
			lReatoCumSqlDao.ricercaReatiCumuloByIdTitolo(aTitoloOrigine.getIdTitoloCumulato());

			Vector<ReatoCumuloModel> lListaReati = new Vector<ReatoCumuloModel>(lReatoCumSqlDao.getModels());

			siesLogger.debug("Reati trovati: " + (lListaReati != null ? lListaReati.size() : "0"));

			if (lListaReati != null && lListaReati.size() > 0) {
				lReatoCumDao = new ReatoCumuloDAO(lConn);

				for (int i = 0; i < lListaReati.size(); i++) {
					ReatoCumuloModel lReatoCum = lListaReati.elementAt(i);

					BigDecimal lIdReatoOrig = lReatoCum.getIdReatoCum();

					lReatoCum.setTitIdTitoloCumulato(lIdTitoloIns);
					// ID_CONTINUAZIONE_REATO_CUM n.b. viene copiato tal quale. NON è un id a una chiave
					// primaria (sequence)

					lReatoCum.setFlagStato(lFlagStato);
					lReatoCum.setMotivoModificaNote(null);

					lReatoCum.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lReatoCum.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lReatoCum.setDataInserimento(aDatoOpModel.getData());

					lReatoCum.setCodOperatoreAggiornamento(null);
					lReatoCum.setCodUfficioAggiornamento(null);
					lReatoCum.setDataAggiornamento(null);

					siesLogger.debug("Inserisco il Reato = " + lReatoCum);

					lReatoCumDao.setDAOFromModel(lReatoCum);
					BigDecimal lIdReatoNew = lReatoCumDao.insert();
					lReatoCumDao.stop();

					// Serve per i computi
					lIdReatoOrigIdReatoNewMap.put(lIdReatoOrig, lIdReatoNew);
				}
			}

			// ========================================================================
			// Inserimento CircostanzaCumulo
			// - Punta solo il Titolo
			// ========================================================================
			siesLogger.debug("Recupero le Circostanze");
			lCircostCumSqlDao = new CircostanzaCumuloSqlDAO(lConn);
			lCircostCumSqlDao.ricercaCircostanzeCumuloByTitolo(aTitoloOrigine.getIdTitoloCumulato());

			Vector<CircostanzaCumuloModel> lListaCircostanze = new Vector<CircostanzaCumuloModel>(
					lCircostCumSqlDao.getModels());

			siesLogger.debug(
					"Circostanze trovate: " + (lListaCircostanze != null ? lListaCircostanze.size() : "0"));

			if (lListaCircostanze != null && lListaCircostanze.size() > 0) {
				lCircostCumDao = new CircostanzaCumuloDAO(lConn);

				for (int i = 0; i < lListaCircostanze.size(); i++) {
					CircostanzaCumuloModel lCircModel = lListaCircostanze.elementAt(i);

					lCircModel.setTitIdTitoloCumulato(lIdTitoloIns);

					lCircModel.setFlagStato(lFlagStato);
					lCircModel.setMotivoModifica(null);

					lCircModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lCircModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lCircModel.setDataInserimento(aDatoOpModel.getData());

					lCircModel.setCodOperatoreAggiornamento(null);
					lCircModel.setCodUfficioAggiornamento(null);
					lCircModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco Circostanza = " + lCircModel);

					lCircostCumDao.setDAOFromModel(lCircModel);
					lCircostCumDao.insert();
					lCircostCumDao.stop();
				}
			}

			// ========================================================================
			// Inserimento Pena Complessiva Cumulo
			// - è puntata da: CONTINUAZIONE_CUMULO, SANZIONE_SOST_CUM
			// ========================================================================
			siesLogger.debug("Recupero Pena Complessiva Cumulo");
			lPenaCompSqlDao = new PenaComplessivaCumuloSqlDAO(lConn);

			lPenaCompSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(aTitoloOrigine.getIdTitoloCumulato());

			PenaComplessivaCumuloModel lPenaCompMod = (PenaComplessivaCumuloModel) lPenaCompSqlDao
					.getModelByKey();

			if (lPenaCompMod != null) {
				lPenaCompDao = new PenaComplessivaCumuloDAO(lConn);

				BigDecimal lIdPenCumOrig = lPenaCompMod.getIdPenaComplessivaCum();

				lPenaCompMod.setTitIdTitoloCumulato(lIdTitoloIns);

				lPenaCompMod.setFlagStato(lFlagStato);
				lPenaCompMod.setMotivoModifica(null);

				lPenaCompMod.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
				lPenaCompMod.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
				lPenaCompMod.setDataInserimento(aDatoOpModel.getData());

				lPenaCompMod.setCodOperatoreAggiornamento(null);
				lPenaCompMod.setCodUfficioAggiornamento(null);
				lPenaCompMod.setDataAggiornamento(null);

				siesLogger.debug("Inserisco Pena Complessiva Cumulo = " + lPenaCompMod);

				lPenaCompDao.setDAOFromModel(lPenaCompMod);
				BigDecimal lIdPenConNew = lPenaCompDao.insert();
				lPenaCompDao.stop();

				// Salvo la mappatura dell'id di copia con quello originario
				lIdPenaCompOrigNewMap.put(lIdPenCumOrig, lIdPenConNew);
			}

			// ========================================================================
			// Inserimento Sanzione Sostitutiva Cumulo
			// - Punta anche PENA_COMPLESSIVA_CUMULO stesso titolo
			// n.b tramite FK not null
			// ========================================================================
			siesLogger.debug("Recupero Sanzione Sostitutiva Cumulo");
			lSanSostCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);

			lSanSostCumSqlDao.ricercaSanzioneSostitutivaCumByTitoloCum(aTitoloOrigine.getIdTitoloCumulato());

			Vector<SanzioneSostitutivaCumuloModel> lListaSanzSost = null;

			lListaSanzSost = new Vector<SanzioneSostitutivaCumuloModel>(lSanSostCumSqlDao.getModels());

			siesLogger.debug("Sanzione Sostitutiva Cumulo trovate: "
					+ (lListaSanzSost != null ? lListaSanzSost.size() : "0"));

			if (lListaSanzSost != null && lListaSanzSost.size() > 0) {
				lSanSostCumDao = new SanzioneSostitutivaCumuloDAO(lConn);

				for (int i = 0; i < lListaSanzSost.size(); i++) {
					SanzioneSostitutivaCumuloModel lSanSostModel = lListaSanzSost.elementAt(i);

					lSanSostModel.setTitIdTitoloCumulato(lIdTitoloIns);

					// Collego subito la SANZIONE_SOST_CUM alla PENA_COMPLESSIVA_CUMULO
					// essendo stesso titolo e la FK not null
					BigDecimal lPcIdPenaComplessivaNew = lIdPenaCompOrigNewMap
							.get(lSanSostModel.getPcIdPenaComplessivaCum());
					lSanSostModel.setPcIdPenaComplessivaCum(lPcIdPenaComplessivaNew); // !!!NOT NULL!!!

					lSanSostModel.setFlagStato(lFlagStato);
					lSanSostModel.setMotivoModifica(null);

					lSanSostModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lSanSostModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lSanSostModel.setDataInserimento(aDatoOpModel.getData());

					lSanSostModel.setCodOperatoreAggiornamento(null);
					lSanSostModel.setCodUfficioAggiornamento(null);
					lSanSostModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco Sanzione Sostitutiva = " + lSanSostModel);

					lSanSostCumDao.setDAOFromModel(lSanSostModel);
					lSanSostCumDao.insert();
					lSanSostCumDao.stop();
				}
			}

			// ========================================================================
			// Inserimento Continuazione Cumulo
			// - punta PENA_COMPLESSIVA_CUMULO (stesso titolo not null)
			// - punta TITOLO_CUMULATO (altro titolo, tittolo con cui è in continuazione)
			// ========================================================================
			siesLogger.debug("Recupero Continuazione Cumulo");

			lContinuaCumSqlDao = new ContinuazioneCumuloSqlDAO(lConn);
			lContinuaCumSqlDao.ricercaContinuazioneByIdTitolo(aTitoloOrigine.getIdTitoloCumulato());

			Vector<ContinuazioneCumuloModel> lListaContinuazioni = null;
			lListaContinuazioni = new Vector<ContinuazioneCumuloModel>(lContinuaCumSqlDao.getModels());

			siesLogger.debug("Continuazioni trovate: "
					+ (lListaContinuazioni != null ? lListaContinuazioni.size() : "0"));

			if (lListaContinuazioni != null && lListaContinuazioni.size() > 0) {
				lContinuaCumDao = new ContinuazioneCumuloDAO(lConn);

				for (int i = 0; i < lListaContinuazioni.size(); i++) {
					ContinuazioneCumuloModel lContModel = lListaContinuazioni.elementAt(i);

					lContModel.setTitIdTitoloCumulato(lIdTitoloIns);

					// Collego subito la CONTINUAZIONE_CUMULO alla PENA_COMPLESSIVA_CUMULO
					// essendo stesso titolo e la FK not null
					BigDecimal lPcIdPenaComplessivaNew = lIdPenaCompOrigNewMap
							.get(lContModel.getPcIdPenaComplessivaCum());
					lContModel.setPcIdPenaComplessivaCum(lPcIdPenaComplessivaNew); // !!!NOT NULL!!

					// TIT_ID_TITOLO_CUMULATO_CONT
					BigDecimal lTitIdTitoloCumulatoCont = lContModel.getTitIdTitoloCumulatoCont();
					lContModel.setTitIdTitoloCumulatoCont(null);

					lContModel.setFlagStato(lFlagStato);
					lContModel.setMotivoModifica(null);

					lContModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lContModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lContModel.setDataInserimento(aDatoOpModel.getData());

					lContModel.setCodOperatoreAggiornamento(null);
					lContModel.setCodUfficioAggiornamento(null);
					lContModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco Continuazione = " + lContModel);

					lContinuaCumDao.setDAOFromModel(lContModel);
					BigDecimal lIdContCumNew = lContinuaCumDao.insert();
					lContinuaCumDao.stop();

					// Salvo la mappatura del nuovi IdCont cone idPC e id TitCont originari
					// aIdContNewIdPenCompOldMap.put(lIdContCumNew, lPcIdPenaComplessivaCum);
					if (lTitIdTitoloCumulatoCont != null)
						aIdContNewIdTitContOldMap.put(lIdContCumNew, lTitIdTitoloCumulatoCont);
				}
			}

			// ========================================================================
			// Inserimento Pena Accessoria Cumulo
			// - Punta eventualmente BENEFICIO_CUMULO ovvero l'amnistia/indulto che hanno
			// recovato la PA
			// ========================================================================
			siesLogger.debug("Recupero Pena Accessoria Cumulo");

			lPenAccCumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);

			lPenAccCumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCum(aTitoloOrigine.getIdTitoloCumulato());

			Vector<PenaAccessoriaCumuloModel> lListaPeneAcc = null;
			lListaPeneAcc = new Vector<PenaAccessoriaCumuloModel>(lPenAccCumSqlDao.getModels());

			siesLogger.debug(
					"Pene Accessorie trovate: " + (lListaPeneAcc != null ? lListaPeneAcc.size() : "0"));

			if (lListaPeneAcc != null && lListaPeneAcc.size() > 0) {
				lPenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);

				for (int i = 0; i < lListaPeneAcc.size(); i++) {
					PenaAccessoriaCumuloModel lPenaAccModel = lListaPeneAcc.elementAt(i);

					lPenaAccModel.setTitIdTitoloCumulato(lIdTitoloIns);

					// BEN_ID_BENEFICIO_CUMULO Punta il record BENEFICIO_CUMULO (indulto/amnistia) che ha
					// Annullato la PA (n.b. stesso titolo)
					// FLAG_DATI_FINALI
					BigDecimal lBenIdBeneficioCumulo = lPenaAccModel.getBenIdBeneficioCumulo();
					lPenaAccModel.setBenIdBeneficioCumulo(null);

					lPenaAccModel.setFlagDatiFinali("S"); // Per default le PA sono caricati in Dati
															// Finali Cumulo

					lPenaAccModel.setFlagStato(lFlagStato);
					lPenaAccModel.setMotivoModifica(null);

					lPenaAccModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lPenaAccModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lPenaAccModel.setDataInserimento(aDatoOpModel.getData());

					lPenaAccModel.setCodOperatoreAggiornamento(null);
					lPenaAccModel.setCodUfficioAggiornamento(null);
					lPenaAccModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco Pena Accessoria = " + lPenaAccModel);

					lPenAccCumDao.setDAOFromModel(lPenaAccModel);
					BigDecimal lIdPenAccNew = lPenAccCumDao.insert();
					lPenAccCumDao.stop();

					// Salvo i reference
					if (lBenIdBeneficioCumulo != null)
						lIdPenAccNewIdBenCumOldMap.put(lIdPenAccNew, lBenIdBeneficioCumulo);
				}
			}

			// ========================================================================
			// Inserimento Misura Sicurezza Cumulo
			// - nessun reference se non al TITOLO_CUMULATO
			// ========================================================================
			siesLogger.debug("Recupero le Misure Sicurezza Cumulo");

			lMisSicCumSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);

			lMisSicCumSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCum(aTitoloOrigine.getIdTitoloCumulato());

			Vector<MisuraSicurezzaCumuloModel> lListaMisureSicurezza = null;
			lListaMisureSicurezza = new Vector<MisuraSicurezzaCumuloModel>(lMisSicCumSqlDao.getModels());

			siesLogger.debug("Misure Sicurezza trovate: "
					+ (lListaMisureSicurezza != null ? lListaMisureSicurezza.size() : "0"));

			if (lListaMisureSicurezza != null && lListaMisureSicurezza.size() > 0) {
				lMisSicCumDao = new MisuraSicurezzaCumuloDAO(lConn);

				for (int i = 0; i < lListaMisureSicurezza.size(); i++) {
					MisuraSicurezzaCumuloModel lMisSicCumModel = lListaMisureSicurezza.elementAt(i);

					lMisSicCumModel.setTitIdTitoloCumulato(lIdTitoloIns);

					// da capire chi valorizza il campo MIS_ID_MISURA_SICUREZZA_CUMULO
					// n.b. non valorizzato da codice. Probabilmente un refuso
					lMisSicCumModel.setMisIdMisuraSicurezzaCumulo(null);

					// FLAG_CREA_PROCEDIMENTO - Non gestito qui

					lMisSicCumModel.setFlagDatiFinali("S"); // Per default le MS sono caricati in Dati
															// Finali Cumulo

					lMisSicCumModel.setFlagStato(lFlagStato);
					lMisSicCumModel.setMotivoModifica(null);

					lMisSicCumModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lMisSicCumModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lMisSicCumModel.setDataInserimento(aDatoOpModel.getData());

					lMisSicCumModel.setCodOperatoreAggiornamento(null);
					lMisSicCumModel.setCodUfficioAggiornamento(null);
					lMisSicCumModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco la Misura di Sicurezza = " + lMisSicCumModel);

					lMisSicCumDao.setDAOFromModel(lMisSicCumModel);
					lMisSicCumDao.insert();
					lMisSicCumDao.stop();
				}
			}

			// ========================================================================
			// Inserimento Misura Cautelare Cumulo
			// - nessun reference se non al TITOLO_CUMULATO
			// ========================================================================
			siesLogger.debug("Recupero le Misure Cautelari Cumulo");

			lMisCautCumSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);

			lMisCautCumSqlDao.ricercaMisuraCautelareCumuloByIdTitolo(aTitoloOrigine.getIdTitoloCumulato());

			Vector<MisuraCautelareCumuloModel> lListaMisureCautCum = null;
			lListaMisureCautCum = new Vector<MisuraCautelareCumuloModel>(lMisCautCumSqlDao.getModels());

			siesLogger.debug("Misure Cautelari trovate: "
					+ (lListaMisureCautCum != null ? lListaMisureCautCum.size() : "0"));

			if (lListaMisureCautCum != null && lListaMisureCautCum.size() > 0) {
				lMisCautCumDao = new MisuraCautelareCumuloDAO(lConn);

				for (int i = 0; i < lListaMisureCautCum.size(); i++) {
					MisuraCautelareCumuloModel lMisCautCumModel = lListaMisureCautCum.elementAt(i);

					lMisCautCumModel.setTitIdTitoloCumulato(lIdTitoloIns);

					lMisCautCumModel.setFlagStato(lFlagStato);
					lMisCautCumModel.setMotivoModifica(null);

					lMisCautCumModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lMisCautCumModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lMisCautCumModel.setDataInserimento(aDatoOpModel.getData());

					lMisCautCumModel.setCodOperatoreAggiornamento(null);
					lMisCautCumModel.setCodUfficioAggiornamento(null);
					lMisCautCumModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco la Misura di Cautelare = " + lMisCautCumModel);

					lMisCautCumDao.setDAOFromModel(lMisCautCumModel);
					lMisCautCumDao.insert();
					lMisCautCumDao.stop();
				}
			}

			// ========================================================================
			// Inserimento Beneficio Cumulo
			// - puntata da PENA_ACCESSORIA_CUMULO
			// - punta a sua volta BENEFICIO_CUMULO. Nel caso di Non Menzione, punta
			// il record relativo alla sospensione condizionale (stesso Titolo)
			// - Punta TITOLO_CUMULATO altro titolo. Se trattasi di una REVOCA, viene
			// puntato il TITOLO su cui è presente il beneficio revocato.
			// Se trattasi di una CONCESSIONE ma revocata, punto il titolo che la ha
			// revocata
			// ========================================================================
			siesLogger.debug("Recupero i Benefici Cumulo");

			lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);

			lBenCumSqlDao.ricercaBeneficioCumuloByTitoloCum(aTitoloOrigine.getIdTitoloCumulato(), null, null);

			Vector<BeneficioCumuloModel> lListaBenefici = null;
			lListaBenefici = new Vector<BeneficioCumuloModel>(lBenCumSqlDao.getModels());

			siesLogger.debug(
					"Benefici Cumulo trovati: " + (lListaBenefici != null ? lListaBenefici.size() : "0"));

			if (lListaBenefici != null && lListaBenefici.size() > 0) {
				lBenCumDao = new BeneficioCumuloDAO(lConn);

				for (int i = 0; i < lListaBenefici.size(); i++) {
					BeneficioCumuloModel lBenCumModel = lListaBenefici.elementAt(i);

					lBenCumModel.setTitIdTitoloCumulato(lIdTitoloIns);

					BigDecimal lIdBenCumOrig = lBenCumModel.getIdBeneficioCumulo();
					BigDecimal lBenIdBeneficioCumulo = lBenCumModel.getBenIdBeneficioCumulo();
					BigDecimal lTitIdTitoloCumulatoCollegato = lBenCumModel.getTitIdTitoloCumulatoCollegato();

					lBenCumModel.setBenIdBeneficioCumulo(null);
					lBenCumModel.setTitIdTitoloCumulatoCollegato(null);
					// BEN_ID_BENEFICIO_CUMULO
					// TIT_ID_TITOLO_CUMULO_COLLEGATO (altro titolo)

					lBenCumModel.setFlagStato(lFlagStato);
					lBenCumModel.setMotivoModifica(null);

					lBenCumModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lBenCumModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
					lBenCumModel.setDataInserimento(aDatoOpModel.getData());

					lBenCumModel.setCodOperatoreAggiornamento(null);
					lBenCumModel.setCodUfficioAggiornamento(null);
					lBenCumModel.setDataAggiornamento(null);

					siesLogger.debug("Inserisco BeneficioCumulo = " + lBenCumModel);

					lBenCumDao.setDAOFromModel(lBenCumModel);
					BigDecimal lIdBenNew = lBenCumDao.insert();
					lBenCumDao.stop();

					// Salvo i reference
					aIdBenCumOrigNewMap.put(lIdBenCumOrig, lIdBenNew);

					if (lBenIdBeneficioCumulo != null)
						aIdBenCumNewBenIdBeneOldMap.put(lIdBenNew, lBenIdBeneficioCumulo);
					if (lTitIdTitoloCumulatoCollegato != null)
						aIdBenCumNewTitIdTitCollOldMap.put(lIdBenNew, lTitIdTitoloCumulatoCollegato);
				}
			}

			// n.b. potrei già in questa fase aggiornare i link dato che gli unici
			// legami cross TITOLI sono tra le tabelle:
			// - CONTINUAZIONE_CUMULO >>>> TITOLO_CUMULATO (altro titolo)
			// - BENEFICIO_CUMULO >>>> TITOLO_CUMULATO (altro titolo)

			// ========================================================================
			// Aggiorno su Pena Accessoria Cumulo il ref a Beneficio cumulo (stesso titolo)
			// che ha condonato la PA.
			if (!lIdPenAccNewIdBenCumOldMap.isEmpty()) {
				siesLogger.debug(
						"Aggiorno sulla tabella PENA_ACCESSORIA_CUMULO i puntamenti a BENEFICIO_CUMULO...");

				// FIXME testare
				if (lPenAccCumDao == null)
					lPenAccCumDao = new PenaAccessoriaCumuloDAO(lConn);
				Enumeration<BigDecimal> keys = lIdPenAccNewIdBenCumOldMap.keys();
				while (keys.hasMoreElements()) {
					BigDecimal idPAtoUpdate = keys.nextElement();
					BigDecimal idBenCumOld = lIdPenAccNewIdBenCumOldMap.get(idPAtoUpdate);
					BigDecimal idBenCumNew = aIdBenCumOrigNewMap.get(idBenCumOld);

					siesLogger.debug("Record aggiornato: idPAtoUpdate = " + idPAtoUpdate
							+ ", BenIdBeneficio = " + idBenCumNew);
					lPenAccCumDao.setBenIdBeneficioCumulo(idBenCumNew);
					lPenAccCumDao.selCondizioneUpdate(idPAtoUpdate);
					lPenAccCumDao.update();
					lPenAccCumDao.stop();
				}
			}

			// ========================================================================
			// Aggiorno su Beneficio Cumulo il ref a Beneficio cumulo (stesso titolo)
			// ovvero la non menzione che punta la sospensione condizionale.
			if (!aIdBenCumNewBenIdBeneOldMap.isEmpty()) {
				siesLogger.debug(
						"Aggiorno sulla tabella BENEFICIO_CUMULO i puntamenti a BENEFICIO_CUMULO (non menzione to sosp. cond)....");
				Enumeration<BigDecimal> keys = aIdBenCumNewBenIdBeneOldMap.keys();

				if (lBenCumDao == null)
					lBenCumDao = new BeneficioCumuloDAO(lConn);

				while (keys.hasMoreElements()) {
					BigDecimal idBenCumToUpdate = keys.nextElement();
					BigDecimal idBenCumOld = aIdBenCumNewBenIdBeneOldMap.get(idBenCumToUpdate);
					BigDecimal idBenCumNew = aIdBenCumOrigNewMap.get(idBenCumOld);

					siesLogger.debug("Record aggiornato: idBenCumToUpdate = " + idBenCumToUpdate
							+ ", BenIdBeneficio = " + idBenCumNew);
					lBenCumDao.setBenIdBeneficioCumulo(idBenCumNew);
					lBenCumDao.setCondizioneUpdate(idBenCumToUpdate);
					lBenCumDao.update();
					lBenCumDao.stop();
				}
			}

			// ========================================================================
			// Aggiungere lo stato esecuzione
			// - STATO_ESEC_TITOLO_CUMULATO
			// - NOTIFICA_CUMULO
			// - COMPUTI_CUMULO
			// - LIB_ANTICIPATA_CUMULO
			// - PERIODO_LIB_ANT_CUMULO
			// ========================================================================
			if (ModuloCumuloUtils.isMev42Abilitata()) {
				// Se la MEV42 non è abilitata non estraggo lo statao esecuzione di un cumulo
				// precedente. Lo stato esecuzuione è disabilitato sul distretto
				siesLogger.debug("Procedo a copiare lo stato esecuzione...");
				lStatoEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
				lStatoEsecSqlDao
						.ricercaStatoEsecTitoloCumulatoByIdTitolo(aTitoloOrigine.getIdTitoloCumulato(), null);

				Vector<StatoEsecTitoloCumulatoModel> lListaStatoEsec = null;

				lListaStatoEsec = new Vector<StatoEsecTitoloCumulatoModel>(lStatoEsecSqlDao.getModels());

				siesLogger.debug("Provvedimenti Trovati trovati: "
						+ (lListaStatoEsec != null ? lListaStatoEsec.size() : "0"));

				if (lListaStatoEsec != null && lListaStatoEsec.size() > 0) {
					lStatoEsecDao = new StatoEsecTitoloCumulatoDAO(lConn);

					lNotificaSqlDao = new NotificaCumuloSqlDAO(lConn);
					lNotificaDao = new NotificaCumuloDAO(lConn);
					lComputiCumSqlDao = new ComputiCumuloSqlDAO(lConn);
					lComputiCumDao = new ComputiCumuloDAO(lConn);
					lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
					lLibAntCumDao = new LibAnticipataCumuloDAO(lConn);
					lPeriodoLibAntCumSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);
					lPeriodoLibAntCumDao = new PeriodoLibAntCumuloDAO(lConn);

					for (int i = 0; i < lListaStatoEsec.size(); i++) {
						StatoEsecTitoloCumulatoModel lStatoEsecModel = lListaStatoEsec.elementAt(i);

						BigDecimal lIdStatoEsecOrig = lStatoEsecModel.getIdStatoEsecTitoloCumulato();

						lStatoEsecModel.setTitIdTitoloCumulato(lIdTitoloIns);
						lStatoEsecModel.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

						// FIXME ID_EVENTO_ORIGINE, EVE_ID_EVENTO_ORIGINE

						lStatoEsecModel.setFlagStato(lFlagStato);
						lStatoEsecModel.setMotivoModifica(null);

						lStatoEsecModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
						lStatoEsecModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
						lStatoEsecModel.setDataInserimento(aDatoOpModel.getData());

						lStatoEsecModel.setCodOperatoreAggiornamento(null);
						lStatoEsecModel.setCodUfficioAggiornamento(null);
						lStatoEsecModel.setDataAggiornamento(null);

						siesLogger.debug("Inserisco StatoEsecuzione = " + lStatoEsecModel.getCodTipoEvento()
								+ " - " + lStatoEsecModel.getCodTipoProvvedimento() + " - "
								+ lStatoEsecModel.getCodMotivo());

						lStatoEsecDao.setDAOFromModel(lStatoEsecModel);
						BigDecimal lIdStatoNew = lStatoEsecDao.insert();
						lStatoEsecDao.stop();

						// ==================================================================
						// Inserisco le Notifiche (Se presenti)
						// ==================================================================
						siesLogger.debug("Inserisco le Notifiche ");

						lNotificaSqlDao.ricercaNotificheCumuloByIdStatoEsec(lIdStatoEsecOrig);
						Vector<NotificaCumuloModel> lListaNotifiche = null;
						lListaNotifiche = new Vector<NotificaCumuloModel>(lNotificaSqlDao.getModels());

						siesLogger.debug("Notifiche trovate: "
								+ (lListaNotifiche != null ? lListaNotifiche.size() : "0"));

						if (lListaNotifiche != null && lListaNotifiche.size() > 0) {

							for (NotificaCumuloModel lNotificaModel : lListaNotifiche) {
								lNotificaModel.setStatIdStatoEsecTitCum(lIdStatoNew);

								// FIXME NotificaCumulo completare ref esterni
								// lNotificaModel.setAutEstIdAutoritaEsterna(aValore)
								// lNotificaModel.setSogIdSoggetto(aValore)
								// lNotificaModel.setAvvIdAvvocatoFascicoloSiep(aValore)

								// lNotificaModel.setCssIdCssa(aValore) OK
								// lNotificaModel.setUffCodUfficio(aValore) OK
								// lNotificaModel.setIstDetIdIstitutoDetenzione(aValore) OK

								lNotificaModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
								lNotificaModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
								lNotificaModel.setDataInserimento(aDatoOpModel.getData());

								lNotificaModel.setCodOperatoreAggiornamento(null);
								lNotificaModel.setCodUfficioAggiornamento(null);
								lNotificaModel.setDataAggiornamento(null);

								siesLogger.debug("Inserisco Notifica = " + lNotificaModel);
								lNotificaDao.setDAOFromModel(lNotificaModel);
								lNotificaDao.insert();
								lNotificaDao.stop();
							}
						} // end if notifiche

						// ==================================================================
						// Inserimento computi cumulo
						// ==================================================================
						siesLogger.debug("Recupero i Cumputi");
						lComputiCumSqlDao.ricercaComputiCumuloByIdStatoEsec(lIdStatoEsecOrig);
						Vector<ComputiCumuloModel> lListaComputi = null;
						lListaComputi = new Vector<ComputiCumuloModel>(lComputiCumSqlDao.getModels());

						siesLogger.debug("Computi Cumulo trovati: "
								+ (lListaComputi != null ? lListaComputi.size() : "0"));

						if (lListaComputi != null && lListaComputi.size() > 0) {

							for (ComputiCumuloModel lComputiModel : lListaComputi) {

								lComputiModel.setStatIdStatoEsecTitCum(lIdStatoNew);

								// Campi reference da gestire
								// TIT_ID_TITOLO_CUMULATO ok
								// DAT_ID_DATI_FINALI_CUMULO ok
								// ISTR_ID_ISTRUTTORIA_CUMULO ok
								// IST_DET_ID_ISTITUTO_DETENZIONE OK
								// REA_ID_REATO_CUM OK
								// n.b. punta i reati in caso di
								// Amnistia/Indulto/Incostituzionalità/Depenalizzazione
								if (lComputiModel.getReaIdReatoCum() != null) {
									BigDecimal lIdReatoNew = lIdReatoOrigIdReatoNewMap
											.get(lComputiModel.getReaIdReatoCum());
									if (lIdReatoNew != null)
										lComputiModel.setReaIdReatoCum(lIdReatoNew);
									else
										lComputiModel.setReaIdReatoCum(null);
								}

								lComputiModel.setTitIdTitoloCumulato(lIdTitoloIns);
								lComputiModel.setIstrIdIstruttoriaCumulo(aIdIstruttoriaCumulo);

								lComputiModel.setDatIdDatiFinaliCumulo(null);

								lComputiModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
								lComputiModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
								lComputiModel.setDataInserimento(aDatoOpModel.getData());

								lComputiModel.setCodOperatoreAggiornamento(null);
								lComputiModel.setCodUfficioAggiornamento(null);
								lComputiModel.setDataAggiornamento(null);

								siesLogger.debug("Inserisco Computo = " + lComputiModel);
								lComputiCumDao.setDAOFromModel(lComputiModel);
								lComputiCumDao.insert();
								lComputiCumDao.stop();
							}
						}

						// ==================================================================
						// Inserisco Liberazione Anticipata
						// ==================================================================
						lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(lIdStatoEsecOrig);
						Vector<LibAnticipataCumuloModel> lListaLibAntCum = null;

						lListaLibAntCum = new Vector<LibAnticipataCumuloModel>(lLibAntCumSqlDao.getModels());

						siesLogger.debug("Lib Ant trovate: "
								+ (lListaLibAntCum != null ? lListaLibAntCum.size() : "0"));

						if (lListaLibAntCum != null && lListaLibAntCum.size() > 0) {
							for (LibAnticipataCumuloModel lLibAntModel : lListaLibAntCum) {

								BigDecimal lIdLibAntOrig = lLibAntModel.getIdLibAnticipataCumulo();

								lLibAntModel.setStatIdStatoEsecTitoloCum(lIdStatoNew);

								// Gestione Reference
								// TIT_ID_TITOLO_CUMULATO
								lLibAntModel.setTitIdTitoloCumulato(lIdTitoloIns);

								lLibAntModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
								lLibAntModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
								lLibAntModel.setDataInserimento(aDatoOpModel.getData());

								lLibAntModel.setCodOperatoreAggiornamento(null);
								lLibAntModel.setCodUfficioAggiornamento(null);
								lLibAntModel.setDataAggiornamento(null);

								siesLogger.debug("Inserisco LibAntCumulo = " + lLibAntModel);
								lLibAntCumDao.setDAOFromModel(lLibAntModel);
								BigDecimal lIdLibAntNew = lLibAntCumDao.insert();
								lLibAntCumDao.stop();

								// ==============================================================
								// Inserisco Periodi Liberazione Anticipata
								// ==============================================================
								siesLogger.debug("Ricerco i Periodi...");
								lPeriodoLibAntCumSqlDao
										.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(lIdLibAntOrig);
								Vector<PeriodoLibAntCumuloModel> lListaPeriodi = null;

								lListaPeriodi = new Vector<PeriodoLibAntCumuloModel>(
										lPeriodoLibAntCumSqlDao.getModels());
								siesLogger.debug("Periodi trovati: "
										+ (lListaPeriodi != null ? lListaPeriodi.size() : "0"));

								if (lListaPeriodi != null && lListaPeriodi.size() > 0) {
									for (PeriodoLibAntCumuloModel lPeriodoModel : lListaPeriodi) {

										lPeriodoModel.setLibIdLibAnticipataCumulo(lIdLibAntNew);

										lPeriodoModel
												.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
										lPeriodoModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());
										lPeriodoModel.setDataInserimento(aDatoOpModel.getData());

										lPeriodoModel.setCodOperatoreAggiornamento(null);
										lPeriodoModel.setCodUfficioAggiornamento(null);
										lPeriodoModel.setDataAggiornamento(null);

										siesLogger.debug("Inserisco PeriodiLibAntCumulo = " + lPeriodoModel);
										lPeriodoLibAntCumDao.setDAOFromModel(lPeriodoModel);
										lPeriodoLibAntCumDao.insert();
										lPeriodoLibAntCumDao.stop();
									}
								}

							}
						} // End if (lListaLibAntCum!=null
					} // END for (int i = 0; i<lListaStatoEsec.size(); i++ )
				} // END if (lListaStatoEsec!=null
			} // END blocco di gestione dello stato di secuzione

			if (aDBConnection == null) {
				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("ModuloCumuloController.CopiaDatiTitolo: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: ", ex);
			throw new F3BException("ModuloCumuloController.CopiaDatiTitolo: Non posso inserire: " + ex);
		} finally {
			cleanup(lTitoloCumDao);
			cleanup(lProcedimentoSqlDao);
			cleanup(lProcedimentoDao);
			cleanup(lSoggettoCumulatoSqlDao);
			cleanup(lSoggettoCumulatoDao);
			cleanup(lReatoCumSqlDao);
			cleanup(lReatoCumDao);
			cleanup(lCircostCumSqlDao);
			cleanup(lCircostCumDao);
			cleanup(lPenaCompSqlDao);
			cleanup(lPenaCompDao);
			cleanup(lSanSostCumSqlDao);
			cleanup(lSanSostCumDao);
			cleanup(lContinuaCumSqlDao);
			cleanup(lContinuaCumDao);
			cleanup(lPenAccCumSqlDao);
			cleanup(lPenAccCumDao);
			cleanup(lMisSicCumSqlDao);
			cleanup(lMisSicCumDao);
			cleanup(lMisCautCumSqlDao);
			cleanup(lMisCautCumDao);
			cleanup(lBenCumSqlDao);
			cleanup(lBenCumDao);
			cleanup(lStatoEsecSqlDao);
			cleanup(lStatoEsecDao);
			cleanup(lNotificaSqlDao);
			cleanup(lNotificaDao);
			cleanup(lComputiCumSqlDao);
			cleanup(lComputiCumDao);
			cleanup(lLibAntCumSqlDao);
			cleanup(lLibAntCumDao);
			cleanup(lPeriodoLibAntCumSqlDao);
			cleanup(lPeriodoLibAntCumDao);

			if (aDBConnection == null) {
				cleanup(lConn);
			}
		}
	}

	/**
	 * Prova a caricare l'espiazione pregressa se l'ultima PR validata ha data fine < data caricamento in
	 * istruttoria. In questo caso si suppone la pena espiata, almeno i quantum dell'ultima PR, e li si
	 * caricano come un provvedimento fittizio di tipo espiazione pregressa. Vedi Attività del PM - Espiazione
	 * - Espiazione Pregressa
	 *
	 * @param aIdFascicoloSiep
	 * @param aIdTitolo
	 * @param aIdIstruttoria
	 * @param aDatoOpModel
	 * @param aConn
	 * @param aConn
	 */
	private void caricaEspiazionePregressa(BigDecimal aIdFascicoloSiep, BigDecimal aIdTitolo,
			BigDecimal aIdIstruttoria, DatiOperazioneModel aDatoOpModel, Connection aConn)
			throws F3BException {

		PenaResiduaSqlDAO lPenResSqlDao = null;
		StatoEsecTitoloCumulatoDAO lStatoEsecDAO = null;
		ComputiCumuloDAO lComputiDao = null;

		try {
			lPenResSqlDao = new PenaResiduaSqlDAO(aConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aIdFascicoloSiep);
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			lPenResSqlDao.stop();

			if (lPenResMod != null)
				siesLogger.debug("caricaEspiazionePregressa - Data Fine Pena = " + lPenResMod.getDataFine());

			// ========================================================================
			// Se il fine pena è < della data di caricamento si suppone la PR
			// interamente espiata. Quindi i quantum di pena presenti sull'ultima
			// PR vanno considerati come ESPIATO.
			// Potrebbero essere assenti se l'archiviazione ha azzerato i quantum,
			// in questo caso devo recuperare i quantum dall'ultimo record PR che ha
			// quantum valorizzati.
			// ========================================================================
			if (lPenResMod != null && lPenResMod.getDataFine() != null
					&& DateUtils.isGreater(DateUtils.getSysDate(), lPenResMod.getDataFine())) {
				siesLogger.debug("Data fine ultima pena trascorsa. Provo a caricare i quantum...");
				if (lPenResMod.isQuantumReclusioneZero() && lPenResMod.isQuantumArrestoZero()) {
					siesLogger.debug("Ultima pena con quantum nulli. Cerco sulla pena precedente");

					/*
					 * lPenResSqlDao.ricercaPenaResiduaByIdFascicoloDataDesc (aIdFascicoloSiep); Vector
					 * <PenaResiduaModel> lListaPR = new Vector (lPenResSqlDao.getModels());
					 *
					 * siesLogger.debug("Trovate "+lListaPR.size()+" Pene residue"); for (PenaResiduaModel
					 * lPR:lListaPR) { siesLogger.debug("idPr = "+lPR.getIdPenaResidua()); if (
					 * lPR.getIdPenaResidua().compareTo (lPenResMod.getIdPenaResidua())!=0 &&
					 * (!lPenResMod.isQuantumReclusioneZero() || !lPenResMod.isQuantumArrestoZero())) {
					 * siesLogger.debug("Trovati i quantum "); } }
					 */

				} else {
					siesLogger.debug("Ultima pena con quantum valorizzati");
				}

				//
				if (!lPenResMod.isQuantumReclusioneZero() || !lPenResMod.isQuantumArrestoZero()) {
					siesLogger.debug(
							"Recuperati ultimi quantum di pena espiati li carico come Espiazione pregressa");
					StatoEsecTitoloCumulatoModel lStatoEsecMod = new StatoEsecTitoloCumulatoModel();

					lStatoEsecMod.setCodTipoEvento("01");
					lStatoEsecMod.setCodTipoProvvedimento("25");
					lStatoEsecMod.setCodMotivo("0675"); // Espiazione pregressa

					lStatoEsecMod.setDataEmissione(null);
					lStatoEsecMod.setCodEsito("-");
					lStatoEsecMod.setCodEsitoTenore("-");
					lStatoEsecMod.setAnnoProcedimento(null);
					lStatoEsecMod.setProgrProcedimento(null);
					lStatoEsecMod.setAnnoProvvedimento(null);
					lStatoEsecMod.setProgrProvvedimento(null);

					lStatoEsecMod.setTitIdTitoloCumulato(aIdTitolo);
					lStatoEsecMod.setIstrIdIstruttoriaCumulo(aIdIstruttoria);
					lStatoEsecMod.setFlagStato("E");
					lStatoEsecMod.setMotivoModifica(null);

					lStatoEsecMod.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lStatoEsecMod.setDataInserimento(aDatoOpModel.getData());
					lStatoEsecMod.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());

					// Inserisco
					lStatoEsecDAO = new StatoEsecTitoloCumulatoDAO(aConn);
					lStatoEsecDAO.setDAOFromModel(lStatoEsecMod);
					BigDecimal lIdStatEsec = lStatoEsecDAO.insert();
					lStatoEsecDAO.stop();

					// ====================================================================
					// Carico i computi
					// ====================================================================
					ComputiCumuloModel lComputiModel = new ComputiCumuloModel();

					lComputiModel.setCodTipoAnnotazione("019"); // 019-Espiazione Pregressa
					lComputiModel.setCodCausaleComputo("-");
					lComputiModel.setFlagPiuMeno("-"); // Sempre meno

					lComputiModel.setDataReclusioneDa(lPenResMod.getDataInizio());
					lComputiModel.setDataReclusioneA(lPenResMod.getDataFine());

					// FIXME e l'arresto? dove lo carico??? Sommo?
					// lComputiModel.setNumAnniReclusione (lPenResMod.getNumAnniReclusione());
					// lComputiModel.setNumMesiReclusione (lPenResMod.getNumMesiReclusione());
					// lComputiModel.setNumGiorniReclusione (lPenResMod.getNumGiorniReclusione());

					// ====================================================================
					// FIX richesta da MT il 30/05/2019
					// Non vanno presi i quantum ma calcolati tra data inizio e data fine
					// Se presenti LA infatti verrebbero computate 2 volte in cumulo
					siesLogger.debug("Calcolo l'espiato effettivo: dataInizio-DataFine " + lPenResMod);
					siesLogger.debug("DataInizio = "
							+ DateUtils.getDateToString(lPenResMod.getDataInizio(), "dd/MM/yyyy"));
					siesLogger.debug("DataFine = "
							+ DateUtils.getDateToString(lPenResMod.getDataFine(), "dd/MM/yyyy"));

					Date data_inizio = lPenResMod.getDataInizio();
					Date data_fine = lPenResMod.getDataFine();

					CalendarModel lCalPenaEspiata = new CalendarModel();
					CalendarUtil lCalUtil = new CalendarUtil();

					lCalPenaEspiata.setDataInizio(data_inizio);
					lCalPenaEspiata.setDataFine(data_fine);

					lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, false);

					// Normalizzo i quantum
					lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);

					lComputiModel.setNumAnniReclusione(new BigDecimal(lCalPenaEspiata.getNumAnni()));
					lComputiModel.setNumMesiReclusione(new BigDecimal(lCalPenaEspiata.getNumMesi()));
					lComputiModel.setNumGiorniReclusione(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
					siesLogger.debug("Fine Calcolo l'espiato effettiva:" + lCalPenaEspiata);
					// ====================================================================

					lComputiModel.setNote("Dato estratto da ultima pena in espiazione sul Titolo Importato");

					lComputiModel.setFlagStato("E");
					lComputiModel.setMotivoModifica(null);

					lComputiModel.setStatIdStatoEsecTitCum(lIdStatEsec);
					lComputiModel.setTitIdTitoloCumulato(aIdTitolo);
					lComputiModel.setIstrIdIstruttoriaCumulo(aIdIstruttoria);

					lComputiModel.setCodOperatoreInserimento(aDatoOpModel.getCodOperatore());
					lComputiModel.setDataInserimento(aDatoOpModel.getData());
					lComputiModel.setCodUfficioInserimento(aDatoOpModel.getCodUfficio());

					// Inserisco
					lComputiDao = new ComputiCumuloDAO(aConn);
					lComputiDao.setDAOFromModel(lComputiModel);
					lComputiDao.insert();
					lComputiDao.stop();
				} else {
					siesLogger.warn(
							"Impossibile recuperare l'espiato. La PR corrente non ha quantum e nemmeno quella precedente.");
				}
			}
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.caricaEspizionePregressa: Non posso inserire: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.caricaEspizionePregressa: Non posso inserire: " + ex);
		} finally {
			cleanup(lPenResSqlDao);
			cleanup(lStatoEsecDAO);
			cleanup(lComputiDao);
		}
	}

	/**
	 * MEV 16 CUMULO: aggiunto metodo
	 */
	public void ExInserisciTitoloInIstruttoria(BigDecimal idIstruttoriaCumulo, DatiNscToSiesModel dntsm,
			DatiOperazioneModel dom, String tipoIscrizione, Connection aDBConnection) throws Exception {

		Connection c = null;

		SoggettoSqlDAO sosDAO = null;
		SentenzaSqlDAO sesDAO = null;
		TitoloCumulatoDAO tcDAO = null;
		// ProcedimentoCumulatoDAO pcDAO = null;
		SoggettoCumulatoDAO scDAO = null;
		// IstruttoriaCumuloSqlDAO icsDAO = null;
		ContinuazioneCumuloSqlDAO ccsDAO = null;
		ContinuazioneCumuloDAO ccDAO = null;

		try {
			if (aDBConnection != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Utilizzo connessione in input ");
				c = aDBConnection;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Apro nuova connessione");
				c = getDBConnection();
			}

			sosDAO = new SoggettoSqlDAO(c);
			sesDAO = new SentenzaSqlDAO(c);

			// ProcedimentoCumulatoModel pcm = new ProcedimentoCumulatoModel();
			// pcm.setCodTipoUfficioFasCumulato(dom.getCodUfficio());
			// pcm.setCodLuogoUfficioFasCumulato("");
			// pcm.setFlagStato("E"); // Estratto
			// pcm.setCodOperatoreInserimento(dom.getCodOperatore());
			// pcm.setDataInserimento(dom.getData());
			// pcm.setCodUfficioInserimento(dom.getCodUfficio());

			// =============================================================
			// Prelevo e duplico i dati della SENTENZA in TITOLO_CUMULATO
			// =============================================================
			sesDAO.ricercaSentenzaBykey(dntsm.getSentenzaModel().getIdSentenza());
			SentenzaModel sem = (SentenzaModel) sesDAO.getModelByKey();

			TitoloCumulatoModel tcm = new TitoloCumulatoModel();
			if (sem != null)
				tcm = new TitoloCumulatoModel(sem);
			else
				tcm = new TitoloCumulatoModel(dntsm.getSentenzaModel());
			tcm.setDataIrrevocabilita(dntsm.getFascicoloSiepModel() != null
					? dntsm.getFascicoloSiepModel().getDataIrrevocabilita()
					: dntsm.getSentenzaModel().getDataProvvedimento());
			tcm.setIstrIdIstruttoriaCumulo(idIstruttoriaCumulo);
			tcm.setFlagStato("E"); // Estratto
			tcm.setFlagEscluso("N");
			tcm.setCodOperatoreInserimento(dom.getCodOperatore());
			tcm.setDataInserimento(dom.getData());
			tcm.setCodUfficioInserimento(dom.getCodUfficio());
			// aTipoIscrizione: 00 = Titolo principale - 01 = Iscrizione da presa in carico - 02= Iscrizione
			// Manuale - 03 = da NSC - 04 = Iscrizione Proprio Ufficio
			tcm.setTipoIscrizione(tipoIscrizione);

			// ===============================================================
			// Prelevo e duplico i dati di SOGGETTO in SOGGETTO_CUMULATO
			// ===============================================================
			sosDAO.ricercaSoggettoByKey(dntsm.getSoggettoModel().getIdSoggetto());
			SoggettoModel som = (SoggettoModel) sosDAO.getModelByKey();

			SoggettoCumulatoModel scm = new SoggettoCumulatoModel();
			if (som != null)
				scm = new SoggettoCumulatoModel(som);
			else
				scm = new SoggettoCumulatoModel(dntsm.getSoggettoModel());
			scm.setFlagStato("E"); // Estratto
			scm.setCodOperatoreInserimento(dom.getCodOperatore());
			scm.setDataInserimento(dom.getData());
			scm.setCodUfficioInserimento(dom.getCodUfficio());

			// Iscrivo il TITOLO_CUMULATO
			tcDAO = new TitoloCumulatoDAO(c);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Iscrivo il TITOLO_CUMULATO = " + tcm);
			tcDAO.setDAOFromModel(tcm);
			BigDecimal idTitoloCum = tcDAO.insert();
			tcm.setIdTitoloCumulato(idTitoloCum);
			tcDAO.stop();

			// Iscrivo il PROCEDIMENTO_CUMULATO
			// pcDAO = new ProcedimentoCumulatoDAO(lConn);
			// pcm.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// // LogF3B.getLogger()
			// siesLogger.debug("Iscrivo il PROCEDIMENTO_CUMULATO = " + pcm);
			// pcDAO.setDAOFromModel(pcm);
			// BigDecimal lIdProcCum = pcDAO.insert();
			// pcm.setIdProcedimentoCumulato(lIdProcCum);
			// pcDAO.stop();

			// Iscrivo il SOGGETTO_CUMULATO
			scDAO = new SoggettoCumulatoDAO(c);
			scm.setTitIdTitoloCumulato(tcm.getIdTitoloCumulato());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Iscrivo il SOGGETTO_CUMULATO = " + scm);
			scDAO.setDAOFromModel(scm);
			BigDecimal lIdSoggetto = scDAO.insert();
			scm.setIdSoggettoCumulato(lIdSoggetto);
			scDAO.stop();

			// ===========================================================
			// Estraggo i dati analitici e li collego al TITOLO_CUMULATO
			// ===========================================================
			ExEstraiDatiAnalitici(dntsm, tcm, c);

			// Verifico se esistono in istruttoria TITOLI che hanno un record CONTINUAZIONE_CUMULO
			// che potrebbe referenziare il titolo corrente. In questo caso aggiorno il puntamento
			// CONTINUAZIONE_CUMULO.TIT_ID_TITOLO_CUMULATO_CONT = id titolo corrente
			// asdsad
			siesLogger.debug("Ricerco se presenti continuazioni che puntano il titolo che sto caricando...");
			ccsDAO = new ContinuazioneCumuloSqlDAO(c);
			ccsDAO.ricercaContinuazioneByIdIstruttoria(idIstruttoriaCumulo);
			Vector<ContinuazioneCumuloModel> lListaContinuazioni = new Vector<ContinuazioneCumuloModel>(
					ccsDAO.getModels());
			siesLogger.debug("Continuazioni totali trovate: " + lListaContinuazioni.size());
			for (ContinuazioneCumuloModel lContinua : lListaContinuazioni) {
				if (lContinua.isStessoTitolo(tcm)) {
					siesLogger.debug("Stesso titolo: " + tcm.getIdTitoloCumulato());
					siesLogger.debug(
							"Aggiorno il record Continuazione con id: " + lContinua.getIdContinuazioneCum());
					ccDAO = new ContinuazioneCumuloDAO(c);
					ccDAO.setTitIdTitoloCumulatoCont(tcm.getIdTitoloCumulato());
					ccDAO.setCondizioneUpdate(lContinua.getIdContinuazioneCum());
					ccDAO.update();
					ccDAO.stop();
				}
			}
			ccsDAO.stop();
			commit(c);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Ricerca eventuale presenza procedimento di cumulo...");
			// icsDAO = new IstruttoriaCumuloSqlDAO(lConn);
			// icsDAO.ricercaIstruttoriaCumuloByKey(idIstruttoriaCumulo);
			// IstruttoriaCumuloModel lUltimaIstruttoria = (IstruttoriaCumuloModel) icsDAO
			// .getModelByKey();
			// if (lUltimaIstruttoria != null) {
			// siesLogger.debug("Presente provvedimento di cumulo (idIstruttoria = "
			// + lUltimaIstruttoria.getIdIstruttoriaCumulo()
			// + ", procedo all'estrazione dei titoli cumulati");
			// EstraiDaPrecedenteCumulo(lUltimaIstruttoria, idIstruttoriaCumulo, null, dom, lConn,
			// tipoIscrizione);
			// } else {
			// siesLogger.debug(
			// "Nessun provvedimento di cumulo presente sul cumulato, estrazione dati terminata.");
			// }
			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExInserisciTitoloCumulato: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(c);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"ModuloCumuloController.ExInserisciTitoloCumulato: Non posso inserire: " + ex);
		} finally {
			cleanup(sosDAO);
			cleanup(sesDAO);
			cleanup(scDAO);
			cleanup(tcDAO);
			cleanup(ccsDAO);
			cleanup(ccDAO);

			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			if (aDBConnection == null) {
				cleanup(c);
			}
		}
	}

	/**
	 * Si aggiunge metodo per recuperare i titoli con Continuazioni non agganciate correttamente Per verifica
	 * mancato aggancio con il titolo in continuazione
	 *
	 * @since MEV_2025-48 - 2.12 Alert su continuazione
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliConContinuazioniSganciate(
			BigDecimal aIdIstruttoriaCumulo) throws F3BException {

		Connection lConn = null;

		ContinuazioneCumuloSqlDAO lContCumSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;

		Vector<ContinuazioneCumuloModel> lListaContinuazioni = new Vector<>();
		Vector<TitoloCumulatoModel> lListaTitoli = new Vector<>();

		try {
			lConn = getDBConnection();

			siesLogger.debug("Ricerco se presenti continuazioni in istruttoria");

			lContCumSqlDao = new ContinuazioneCumuloSqlDAO(lConn);
			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			lContCumSqlDao.ricercaContinuazioneByIdIstruttoria(aIdIstruttoriaCumulo);

			lListaContinuazioni = new Vector<ContinuazioneCumuloModel>(lContCumSqlDao.getModels());

			// n.b. sullo stesso titolo potrebbero esserci più continuazioni
			// dovendo aggregare per titolo...
			for (int i = 0; i < lListaContinuazioni.size(); i++) {
				ContinuazioneCumuloModel lContModel = lListaContinuazioni.elementAt(i);
				if ("R".equals(lContModel.getCodTipoContinuazione())
						&& lContModel.getTitIdTitoloCumulatoCont() == null) {
					// Se il titolo non è in lista lo ricerco
					TitoloCumulatoModel lTitolo = null;
					for (int j = 0; j < lListaTitoli.size(); j++) {
						if (lListaTitoli.elementAt(j).getIdTitoloCumulato()
								.compareTo(lContModel.getTitIdTitoloCumulato()) == 0)
							lTitolo = lListaTitoli.elementAt(j);
					}

					if (lTitolo == null) {
						// Recupera il titolo
						lTitoloSqlDao.ricercaTitoloCumulatoByKey(lContModel.getTitIdTitoloCumulato());
						lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModelByKey();

						// Recupera il procedimento
						lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
						ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
								.getModelByKey();
						lTitolo.setProcedimentoCumulato(lProcModel);

						// record fittizio per aggiungere la lista delle continuazioni
						PenaComplessivaCumuloModel lPenaComp = new PenaComplessivaCumuloModel();

						lTitolo.setPenaComplessivaCumulo(lPenaComp);

						Vector vect = new Vector();
						vect.add(lContModel);
						lPenaComp.setContinuazioniCumulo(vect);

						lListaTitoli.add(lTitolo);
					} else {
						Vector vect = (Vector) lTitolo.getPenaComplessivaCumulo().getContinuazioniCumulo();
						vect.add(lContModel);
					}
				}
			}
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("ModuloCumuloController.ExRicercaTitoliConContinuazioniSganciate: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException("ModuloCumuloController.ExRicercaTitoliConContinuazioniSganciate: " + ex);
		} finally {
			cleanup(lContCumSqlDao);
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);

			cleanup(lConn);
		}

		return lListaTitoli;
	}

	/**
	 * Si aggiunge metodo per recuperare i titoli con Revoche Benefici (sosp cond e indulto) non agganciate
	 * correttamente
	 *
	 * @since MEV_2025-48 - 2.12 Alert su continuazione
	 */
	public Vector<TitoloCumulatoModel> ExRicercaTitoliConRevBenSganciati(BigDecimal aIdIstruttoriaCumulo)
			throws F3BException {

		Connection lConn = null;

		BeneficioCumuloSqlDAO lBenCumSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcCumSqlDao = null;

		Vector<TitoloCumulatoModel> lListaTitoli = new Vector<>();

		try {
			lConn = getDBConnection();

			siesLogger.debug("Ricerco se presenti revoche benefici in istruttoria");

			lBenCumSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lProcCumSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);

			lBenCumSqlDao.ricercaBeneficioCumuloByIdIstruttoria(aIdIstruttoriaCumulo);

			Vector<BeneficioCumuloModel> lListaBenefici = new Vector<BeneficioCumuloModel>(
					lBenCumSqlDao.getModels());

			// n.b. prendo solo quelli con con natura R e di tipo 01/03 che non hanno il puntamento
			// al titolo
			for (int i = 0; i < lListaBenefici.size(); i++) {
				BeneficioCumuloModel lBenModel = lListaBenefici.elementAt(i);

				if ("R".equals(lBenModel.getCodNaturaBeneficio())
						&& ("01".equals(lBenModel.getCodTipoBeneficio())
								|| "03".equals(lBenModel.getCodTipoBeneficio()))
						&& lBenModel.getTitIdTitoloCumulatoCollegato() == null) {
					siesLogger.debug("Trovata revoca non collegata: " + lBenModel.getIdBeneficioCumulo()
							+ " - " + lBenModel.getDescrNaturaBeneficio() + " - "
							+ lBenModel.getDescrTipoBeneficio());

					// Se il titolo non è in lista lo ricerco
					TitoloCumulatoModel lTitolo = null;
					for (int j = 0; j < lListaTitoli.size(); j++) {
						if (lListaTitoli.elementAt(j).getIdTitoloCumulato()
								.compareTo(lBenModel.getTitIdTitoloCumulato()) == 0)
							lTitolo = lListaTitoli.elementAt(j);
					}

					if (lTitolo == null) {
						// Recupera il titolo
						lTitoloSqlDao.ricercaTitoloCumulatoByKey(lBenModel.getTitIdTitoloCumulato());
						lTitolo = (TitoloCumulatoModel) lTitoloSqlDao.getModelByKey();

						// Recupera il procedimento
						lProcCumSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitolo.getIdTitoloCumulato());
						ProcedimentoCumulatoModel lProcModel = (ProcedimentoCumulatoModel) lProcCumSqlDao
								.getModelByKey();
						lTitolo.setProcedimentoCumulato(lProcModel);

						Vector vect = new Vector();
						vect.add(lBenModel);

						lTitolo.setBeneficiCumulo(vect);

						lListaTitoli.add(lTitolo);
					} else {
						lTitolo.getBeneficiCumulo().add(lBenModel);
					}
				}
			}
		} catch (DAOException ex) {
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("ModuloCumuloController.ExRicercaTitoliConRevBenSganciati: " + ex);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException("ModuloCumuloController.ExRicercaTitoliConRevBenSganciati: " + ex);
		} finally {
			cleanup(lBenCumSqlDao);
			cleanup(lTitoloSqlDao);
			cleanup(lProcCumSqlDao);

			cleanup(lConn);
		}

		return lListaTitoli;
	}

} // Chiude CLASSE