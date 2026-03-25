package siap.siep.istruttoriacumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.StampaEventoUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.alias.dao.AliasSqlDAO;
import siap.siep.alias.model.AliasModel;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.istruttoriacumulo.model.CalcoloPenaRidetermCumModel;
import siap.siep.istruttoriacumulo.model.DatiPrincipaliBeneficioCumuloModel;
import siap.siep.istruttoriacumulo.model.DatiPrincipaliTitoloCumulatoModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.istruttoriacumulo.model.ResiduoPenaEspiandaTitoloCumuloModel;
import siap.siep.istruttoriacumulo.model.RiepilogoPenaComplessivaCumuloModel;
import siap.siep.istruttoriacumulo.model.RiepilogoPresoffertoCumuloModel;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.controller.ReatoContinuazioneCumuloController;
import siap.siep.modulocumulo.dao.BeneficioCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ComputiCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ContinuazioneCumuloSqlDAO;
import siap.siep.modulocumulo.dao.LibAnticipataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloSqlDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.NotificaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaAccessoriaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaComplessivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PenaRideterminataCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PeriodoLibAntCumuloSqlDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ProvvedimentoGeSorvCumSqlDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.dao.RichPMStatoEsecCumSqlDAO;
import siap.siep.modulocumulo.dao.RichPMTitoloCumSqlDAO;
import siap.siep.modulocumulo.dao.RichiesteInviateCumSqlDAO;
import siap.siep.modulocumulo.dao.RichiestePmInCumuloSqlDAO;
import siap.siep.modulocumulo.dao.SanzioneSostitutivaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.StatoEsecTitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.modulocumulo.model.ContinuazioneReatiCumuloModel;
import siap.siep.modulocumulo.model.DatePeriodiLACumModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.RichPMStatoEsecCumModel;
import siap.siep.modulocumulo.model.RichPMTitoloCumModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.CalcoloPenaCumuloModel;
import siap.siep.modulocumulo.util.NotaDiTrasmissioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: StampaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per StampaCumuloController
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaCumuloController extends SiapController implements IStampaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Misure Cautelari Cumulo - Conterrà il totale Misure di tutti i titoli
	protected Vector<MisuraCautelareCumuloModel> VecMisCau = new Vector<>();

	// Benefici Cumulo - Conterrà il totale Benefici di tutti i titoli
	protected Vector<BeneficioCumuloModel> VecBenefici = new Vector<>();

	// pena_Complessiva Cumulo - Conterrà il totale PenaComplessiva di tutti i titoli
	protected Vector<PenaComplessivaCumuloModel> VecPenaCompl = new Vector<>();

	// Computi Cumulo - Conterrà il totale ComputiCumulo di tutti i titoli
	protected Vector<ComputiCumuloModel> VecComputi = new Vector<>();

	// cg_Ref_Codes : Domain = MOTIVO_PROVVEDIMENTO()
	protected Vector lVecDeco = new Vector();

	protected CalcoloPenaCumuloModel aCalcoloPenaModel = new CalcoloPenaCumuloModel();


	/**
	*
	*/
	public TreeModel prelevaDatiIstruttoriaCumulo(FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector<TitoloCumulatoModel> aListaTitoli, Date lDataEmissione) throws F3BException {
		siesLogger.debug("prelevaDatiIstruttoriaCumulo INIZIO");

		Connection lConn = null;
		TreeModel lTreeRoot = new TreeModel();
		SoggettoSqlDAO lSogDao = null;

		RichiestePmInCumuloSqlDAO RichiestePmSqlDao = null;
		RichPMTitoloCumSqlDAO lRichPMTitSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloCumsqlDao = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSSCumSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvSqlDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		RichPMStatoEsecCumSqlDAO lRichPmStEsecSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStEsecCumSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoCumSqlDAO = null;
		ComputiCumuloSqlDAO lCompCumSqlDao = null;

		RichiesteInviateCumSqlDAO lRicInvSqlDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;

		MisuraSicurezzaCumuloSqlDAO lMisSicSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoSqlDao = null;

		PenaRideterminataCumuloSqlDAO lPenaRidSqlDao = null;

		try {
			lConn = getDBConnection();

			// vettore con i codici DPR per i calcoli dei quantum Indulto nel Riepilogo
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("DPR");
			/* Vector lVec = new Vector( */lDecodifiche.ExRicercaDecodifiche(lModel)/* ) */;

			// vettore con i codici di MOTIVO_PROVVEDIMENTO
			DecodificheModel lDecoModel = new DecodificheModel();
			lDecoModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lVecDeco = new Vector(lDecodifiche.ExRicercaDecodifiche(lDecoModel));

			// Root X
			lTreeRoot = new TreeModel(createRootX(lUfficioMod, aUtenteMod));

			// Utente
			lTreeRoot.add(new TreeModel(aUtenteMod));

			// Fascicolo Siep
			lTreeRoot.add(new TreeModel(lFascicoloModel));

			// Soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lFascicoloModel.getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogDao.getModelByKey();

			// ========================================================================
			// Patch STEP 1 per mancato caricamento dati residenza
			// old
			// if(lSogModel!=null && lSogModel.getIdSoggetto()!=null)
			// lTreeRoot.add( new TreeModel(lSogModel));
			// new
			TreeModel lTreeSogMod = getTreeSoggetto(lFascicoloModel.getSogIdSoggetto(), lFascicoloModel,
					lConn);
			lTreeRoot.add(lTreeSogMod);
			// End patch
			// ========================================================================

			// Istruttoria
			TreeModel lTreeIstruMod = new TreeModel(aIstruttoriaCumulo);

			// Titoli_Cumulati
			Iterator lItx = null;
			if (aListaTitoli != null) {
				siesLogger.debug("aListaTitoli.size() = " + aListaTitoli.size());
				lItx = aListaTitoli.iterator();
				while (lItx.hasNext()) {
					TitoloCumulatoModel lTitoCumMod = (TitoloCumulatoModel) lItx.next();
					TreeModel lTreeTitoloMod = new TreeModel(lTitoCumMod);

					// Soggetto
					SoggettoCumulatoModel lSoggCum = lTitoCumMod.getSoggettoCumulato();
					siesLogger.debug("lSoggCum = " + lSoggCum);
					if (lSoggCum != null) {
						lTreeTitoloMod.add(new TreeModel(lSoggCum));

						// Il soggetto ha anagrafica differente rispetto al soggetto del cumulante
						if (!lSoggCum.isStessoSoggetto(lSogModel))
							lSoggCum.calcolaStringaSoggetto();
					}

					// Procedimento cumulato
					ProcedimentoCumulatoModel lProcModel = lTitoCumMod.getProcedimentoCumulato();
					siesLogger.debug("Procedimento cumulato = " + lProcModel);
					if (lProcModel != null) {
						lProcModel.calcolaStringaProcedimento();
						lTreeTitoloMod.add(new TreeModel(lProcModel));
					}

					if (lTitoCumMod.getNotaTrasmissione() != null) {
						Iterator Itx = lTitoCumMod.getNotaTrasmissione().iterator();
						while (Itx.hasNext()) {
							// NotaDiTrasmissioneModel lNotaTrasm = lTitoCumMod.getNotaTrasmissione();
							NotaDiTrasmissioneModel lNotaTrasm = (NotaDiTrasmissioneModel) Itx.next();

							TreeModel lNoteTrasmTree = new TreeModel(lNotaTrasm);

							lTreeTitoloMod.add(lNoteTrasmTree);
							lNoteTrasmTree.add(new TreeModel(lNotaTrasm.getUfficioNotaTrasmissione()));
						}

					}

					// Dati Analitici legati al Titolo;
					appendDatiAnalitici(lConn, lTitoCumMod, lTreeTitoloMod, 1);

					// =============================================================================================
					// Dati Stato_Esecuzione_Titolo_Cumulato
					appendStatoEsecuzioneTitoloCum(lConn, aIstruttoriaCumulo.getIdIstruttoriaCumulo(),
							lTitoCumMod.getIdTitoloCumulato(), lTreeTitoloMod, 1);
					// =============================================================

					lTreeIstruMod.add(lTreeTitoloMod);

				}

				siesLogger.debug(
						"---XXXZ--- Ho Inserito tutti i Titoli, Stato Esecuzione e dati analitici collegati alla Istruttoria ... ");

				// =======================================================================================
				// Aggiungo le Richieste del PM al GE e all SORV (sempre legati all'istruttoria)
				siesLogger.debug("---XXXZ--- Inserimento  Richieste del PM al GE e all  SORV ... ");
				// =======================================================================================
				// INIZIO
				RichiestePmSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
				lTitoloCumsqlDao = new TitoloCumulatoSqlDAO(lConn);
				lRichPMTitSqlDao = new RichPMTitoloCumSqlDAO(lConn);
				lReaSqlDao = new ReatoCumuloSqlDAO(lConn);
				lSSCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
				lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
				lProvvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
				lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
				lRichPmStEsecSqlDao = new RichPMStatoEsecCumSqlDAO(lConn);
				lStEsecCumSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
				lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
				lStatoEsecTitoCumSqlDAO = new StatoEsecTitoloCumulatoSqlDAO(lConn);
				lCompCumSqlDao = new ComputiCumuloSqlDAO(lConn);

				lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
				lPeriodoSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

				Vector<RichiestePmInCumuloModel> VecRichieste = new Vector();
				RichiestePmInCumuloModel lRichPMCumMod = null;
				RichiestePmInCumuloModel lRichPM = null;
				RichPMTitoloCumModel lRichTitCumMod = null;
				TitoloCumulatoModel lTitoMod = null;
				SanzioneSostitutivaCumuloModel lSSCumMod = null;
				PenaAccessoriaCumuloModel lPACumMod = null;

				// Ricerca della RICHIESTA
				RichiestePmSqlDao.ricercaRichiestePmInCumuloByIdIstruttoria(
						aIstruttoriaCumulo.getIdIstruttoriaCumulo());
				RichiestePmSqlDao.start();
				while (RichiestePmSqlDao.next()) {
					lRichPM = (RichiestePmInCumuloModel) RichiestePmSqlDao.getModel();
					siesLogger.debug("lRichPM.getCodTipoAnnotazione() = id: "
							+ lRichPM.getIdRichiestePmInCumulo() + "-" + lRichPM.getCodTipoAnnotazione());

					// Ricerca di una Eventuale Ordinanza (Decisione di GE/SORV)
					ProvvedimentoGeSorvCumModel lProvvMod = null;
					lProvvSqlDao
							.ricercaProvvedimentoGeSorvCumByIdRichiesta(lRichPM.getIdRichiestePmInCumulo());
					lProvvMod = (ProvvedimentoGeSorvCumModel) lProvvSqlDao.getModelByKey();
					if (lProvvMod != null && lProvvMod.getIdProvvedimentoGeSorvCum() != null) {
						siesLogger.debug(
								"Trovato scarico del GE  = " + lProvvMod.getIdProvvedimentoGeSorvCum());

						// ======================================================================================
						lRichPM.setDecisioneGeSorvCum(lProvvMod);
						// ======================================================================================

						// ORDINANZA di APPLICAZIONE BENEFICIO Concesso sul Titolo
						if (lRichPM.getCodTipoAnnotazione().equals("002")
								|| lRichPM.getCodTipoAnnotazione().equals("003")) {
							// Amnistia o indulto
							siesLogger.debug("AggiungiAlCalcoloPena Concesso");
							this.AggiungiAlCalcoloPena(lRichPM, "C", lProvvMod, null); // C = Concesso
						}

						// ORDINANZA di REVOCA BENEFICIO Concesso sul Titolo
						if (lRichPM.getCodTipoAnnotazione().equals("021")
								&& lRichPM.getFlagPiuMenoR() != null) // Indulto/Amnistia
						{
							BeneficioCumuloModel lBeneCumMod = null;
							// Beneficio Cumulo (Servono i dati del Beneficio da revocare )
							lBenSqlDao.ricercaBeneficioCumuloByIdTitoloCumRichGE(
									lRichPM.getTitIdTitoloCumulato(), lRichPM.getIdRichiestePmInCumulo());
							lBenSqlDao.start();
							while (lBenSqlDao.next()) {
								lBeneCumMod = (BeneficioCumuloModel) lBenSqlDao.getModel();
								siesLogger.debug("AggiungiAlCalcoloPena Revoca");
								this.AggiungiAlCalcoloPena(lRichPM, "R", lProvvMod, lBeneCumMod); // R =
																									// Revoca
							}
						}

						// =========================================================================
						// MEV_70 : aggiungiamo comnunque le richieste: ANCHE quelle con la decisione

						VecRichieste.add(lRichPM);
						// =========================================================================

					} else {
						siesLogger.debug(
								"--XX-- Stampa certificato - Richiesta senza scarico la aggiungo al VecRichieste");
						VecRichieste.add(lRichPM);
					}
				}

				// Esaminiamo solo le RICHIESTE PRIVE di DECISIONI
				// ***** ATTENZIONE : non è più così: da Aprile 2019, Interventi per MEV_70 --> ESAMINIAMO
				// TUTTE LE RICHIESTE
				siesLogger.debug("--XX-- Totale Richiesta EX-SENZA Decisione = " + VecRichieste.size());
				if (VecRichieste != null && VecRichieste.size() > 0) {
					// siesLogger.debug("--XX-- RichiestePM in Id_Istruttoria = "+VecRichieste.size());
					Iterator lItxR = VecRichieste.iterator();
					while (lItxR.hasNext()) {
						lRichPMCumMod = (RichiestePmInCumuloModel) lItxR.next();

						if (lRichPMCumMod.getCodMotivo() != null) {
							// Cerco le Descrizioni di COD_MOTIVO di Richieste_PM_In_Cumulo.
							String lDescrMot = "";
							String lDescrArt = "";

							DecodificheModel lModelDecodifiche = null;
							Iterator Ite1 = lVecDeco.iterator();

							while (Ite1.hasNext()) {
								lModelDecodifiche = (DecodificheModel) Ite1.next();
								if (lModelDecodifiche.getCode().equals(lRichPMCumMod.getCodMotivo())) {
									lDescrMot = lModelDecodifiche.getDescription();

									if (lModelDecodifiche.getFiltro() != null
											&& !"".equals(lModelDecodifiche.getFiltro())) {
										lDescrArt = lModelDecodifiche.getFiltro();
									}
								}
							}

							if (!lDescrMot.equals("")) {
								lRichPMCumMod.setDescrMotivo(lDescrMot);
							}
							if (!lDescrArt.equals("")) {
								lRichPMCumMod.setDescrArticolo(lDescrArt);
							}
						}

						if (!lRichPMCumMod.isQuantumReclusioneZero())
							lRichPMCumMod.calcolaStringaReclusioneinRichiestePm();
						if (!lRichPMCumMod.isQuantumArrestoZero())
							lRichPMCumMod.calcolaStringaArrestoinRichiestePm();

						TreeModel lTreeRichiestaPMMod = new TreeModel(lRichPMCumMod);

						// ===========================================================================
						// MEV_70 : aggiungiamo comnunque le richieste: ANCHE le RICHIESTE CON LE DECISIONI
						// creo il TreeChild del PROVVEDIMENTO_GE
						ProvvedimentoGeSorvCumModel lProvvGESORV = null;
						if (lRichPMCumMod != null && lRichPMCumMod.getDecisioneGeSorvCum() != null
								&& lRichPMCumMod.getDecisioneGeSorvCum()
										.getIdProvvedimentoGeSorvCum() != null) {

							lProvvGESORV = lRichPMCumMod.getDecisioneGeSorvCum();

							TreeModel lTreeProvvGESORV = new TreeModel(lProvvGESORV);
							lTreeRichiestaPMMod.add(lTreeProvvGESORV);
						}
						// =========================================================================

						// Spostare qui il caricamento nei benefici
						// Eventuale Richiesta di APPLICAZIONE BENEFICIO Concesso sul Titolo
						// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
						// ====== ALL'ELENCO DEI BENEFICI CHE CONCORRONO AL CALCOLOPENA ======
						if ((lRichPMCumMod.getCodTipoAnnotazione().equals("002") // Indulto
								|| lRichPMCumMod.getCodTipoAnnotazione().equals("003") // Amnistia
						) && ("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) // A = Anticipazione
						{
							this.AggiungiAlCalcoloPena(lRichPMCumMod, "C", null, null); // C = Concesso
						}

						// ==============================================================================================================
						// RICHIESTE INVIATE
						// ==============================================================================================================
						if (lRichPMCumMod.getRicIdRichiesteInviateCum() != null) {
							RichiesteInviateCumModel lRicInvMod = null;
							lRicInvSqlDao.ricercaRichiesteInviateCumByKey(
									lRichPMCumMod.getRicIdRichiesteInviateCum());
							lRicInvMod = (RichiesteInviateCumModel) lRicInvSqlDao.getModelByKey();

							if (lRicInvMod != null && lRicInvMod.getIdRichiesteInviateCum() != null) {
								TreeModel lTreeRichiestaINV = new TreeModel(lRicInvMod);
								lTreeRichiestaPMMod.add(lTreeRichiestaINV);
							}

						}
						// =============================================================================================================
						String lTutto = "";
						// Rich_PM_Titolo_Cum: Ricerca dei TITOLI legati alla Richiesta
						lRichPMTitSqlDao
								.ricercaRichPmTitoloCumByRichIdRich(lRichPMCumMod.getIdRichiestePmInCumulo());
						lRichPMTitSqlDao.start();
						while (lRichPMTitSqlDao.next()) {
							lTutto = "";
							lRichTitCumMod = (RichPMTitoloCumModel) lRichPMTitSqlDao.getModel();
							// ===
							if (lRichPMCumMod.getFlagInteroCumulo() != null
									&& "S".equals(lRichPMCumMod.getFlagInteroCumulo())) {
								lTutto = lRichPMCumMod.getFlagInteroCumulo();
							}
							// ===
							lTitoloCumsqlDao
									.ricercaTitoloCumulatoByKey(lRichTitCumMod.getTitIdTitoloCumulato());
							lTitoMod = (TitoloCumulatoModel) lTitoloCumsqlDao.getModelByKey();

							// Preparazione dei campi Principali del TITOLO_CUMULATO che Interessano la
							// RICHIESTA
							// in esame a questa parte di Stampa
							TreeModel lTreeDatiPrincCum = null;
							if (lTitoMod != null && lTitoMod.getIdTitoloCumulato() != null) {
								DatiPrincipaliTitoloCumulatoModel lDatiPrincMod = new DatiPrincipaliTitoloCumulatoModel(
										lTitoMod);
								lDatiPrincMod.setEstremiProvvedimento(
										lDatiPrincMod.FormaEstremiProvvedimentoperProspetto());
								if (!lTutto.equals("")) {
									lDatiPrincMod.setFlagTutto(lTutto);
								}

								// ai dati principali del titolo aggiungo la PENA_COMPLESSIVA del singolo
								// titolo
								PenaComplessivaCumuloModel lPenaCum = cercaPenaComplessivaCum(
										lTitoMod.getIdTitoloCumulato(), lConn);
								if (lPenaCum != null && lPenaCum.getIdPenaComplessivaCum() != null) {
									lPenaCum.calcolaStringaReclusioneCum();
									lPenaCum.calcolaStringaArrestoCum();
									lPenaCum.calcolaStringaIsolamentoCum();

									lDatiPrincMod.setReclusionePenaComp(lPenaCum.getStringaReclusione());
									lDatiPrincMod.setArrestoPenaComp(lPenaCum.getStringaArresto());
									lDatiPrincMod
											.setIsolamentoPenaComp(lPenaCum.getStringaIsolamentoDiurno());
									lDatiPrincMod.setAmmendaPenaComp(lPenaCum.getImportoAmmenda());
									lDatiPrincMod.setMultaPenaComp(lPenaCum.getImportoMulta());
								}

								// ======= >>>>>>>>>>>>>>>>> MEV_70 - Aggiungo sempre la SanzioneSostitutiva,
								// in caso esista, legata alla PENA COMPLESSIVA
								// Sanzione Sostitutiva Cumulo
								SanzioneSostitutivaCumuloModel lSSCumMod1 = new SanzioneSostitutivaCumuloModel();

								//==========================================================================================
								// Ticket#20200715012 - la pena complessiva potrebbe non essere presente.
								BigDecimal idPenaCompCumulo = null;
								if (lPenaCum!=null) 
									idPenaCompCumulo = lPenaCum.getIdPenaComplessivaCum();
									
								lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(
										idPenaCompCumulo,
										lRichTitCumMod.getTitIdTitoloCumulato());
								
//								lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(
//										lPenaCum.getIdPenaComplessivaCum(),
//										lRichTitCumMod.getTitIdTitoloCumulato());								
								// END Ticket#20200715012
								//==========================================================================================
								
								
								
								lSSCumSqlDao.start();
								while (lSSCumSqlDao.next()) {

									lSSCumMod1 = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
									if (lSSCumMod1 != null
											&& lSSCumMod1.getIdSanzioneSostitutivaCum() != null) {

										lSSCumMod1.calcolaStringaSanzionePerStampaProspetto();
										lDatiPrincMod.setStringaSanzioneSostitutiva(
												lSSCumMod1.getStringaSanzione());

										lSSCumMod1.calcolaPeriodoSanzione();
										lDatiPrincMod.setPeriodoSanzioneSostitutiva(
												lSSCumMod1.getPeriodoSanzione());

									}

								}
								// Ticket#202603160115 - stop() per chiudere subito il cursore
								lSSCumSqlDao.stop();
								// Ticket#202603160115 - FINE

								lTreeDatiPrincCum = new TreeModel(lDatiPrincMod);
								// ==== MEV_70 - Fine Aggiunta SanzioneSostitutiva <<<<<<<<<<<<<<<<<<<<<<
								// ======================================

								// Eventuale Richiesta di REVOCA BENEFICIO
								if (lRichPMCumMod.getCodTipoAnnotazione().equals("021")) {
									BeneficioCumuloModel lBeneCumMod = null;
									// Beneficio Cumulo (Servono i dati del Beneficio da revocare )
									lBenSqlDao.ricercaBeneficioCumuloByIdTitoloCumRichGE(
											lRichTitCumMod.getTitIdTitoloCumulato(),
											lRichTitCumMod.getRicIdRichiestePmInCumulo());

									// ===========================================================================================
									// REVOCA BENEFICIO Concesso sul Titolo (in sentenza): Ciclo su Elenco
									// Benefici
									lBenSqlDao.start();
									while (lBenSqlDao.next()) {
										lBeneCumMod = (BeneficioCumuloModel) lBenSqlDao.getModel();
										lBeneCumMod.calcolaStringaArrestoCumulo();
										lBeneCumMod.calcolaStringaReclusioneCumulo();

										DatiPrincipaliBeneficioCumuloModel lDatiPrincBenef = new DatiPrincipaliBeneficioCumuloModel(
												lBeneCumMod);

										// Cerco il titolo di Riferimento della revoca Beneficio
										lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(
												lRichPMCumMod.getTitIdTitoloCumulatoRef());

										// MEV_70 : nel caso REVOCA SOSPENSIONE EX ART.165, potrebbe NON
										// esistere il titolo di RIFERIMENTO
										lTitoloCumsqlDao.start();
										if (lTitoloCumsqlDao.next()) {
											TitoloCumulatoModel lTitoRef = (TitoloCumulatoModel) lTitoloCumsqlDao
													.getModelByKey();

											DatiPrincipaliTitoloCumulatoModel lDatiPrincRef = new DatiPrincipaliTitoloCumulatoModel(
													lTitoRef);
											lDatiPrincRef.setEstremiProvvedimento(
													lDatiPrincRef.FormaEstremiProvvedimentoperProspetto());

											lDatiPrincBenef.setStringaTitoloRef(
													lDatiPrincRef.getEstremiProvvedimento());
										}

										lTreeDatiPrincCum.add(new TreeModel(lDatiPrincBenef));

										// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
										// ====== ALL'ELENCO DEI BENEFICI CHE CONCORRONO AL CALCOLOPENA ======
										// A = Anticipazione
										if (("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) {
											// R = Revoca
											this.AggiungiAlCalcoloPena(lRichPMCumMod, "R", null, lBeneCumMod);
										}

									} // Chiude Ciclo Benefici
									// Ticket#202603160115 - stop() per chiudere subito il cursore
									lBenSqlDao.stop();
									// Ticket#202603160115 - FINE
									
									// =========================================================================================
									// REVOCA BENEFICIO Concesso con Ordinanza (dati su StatoEsecTitoloCum e
									// ComputiCumulo)
									StatoEsecTitoloCumulatoModel lStatoEseMod = null;
									ComputiCumuloModel lCompMod = null;

									lStatoEsecTitoCumSqlDAO.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
											lRichTitCumMod.getTitIdTitoloCumulato(),
											lRichTitCumMod.getRicIdRichiestePmInCumulo());
									lStatoEsecTitoCumSqlDAO.start();
									if (lStatoEsecTitoCumSqlDAO.next()) { // ci può essere un solo
																			// StatoEsecuzione (per titolo e
																			// Richiesta), di questo tipo di
																			// Revoca Beneficio;
										lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoCumSqlDAO
												.getModel();

										lCompCumSqlDao.ricercaComputiCumuloByIdStatoEsec(
												lStatoEseMod.getIdStatoEsecTitoloCumulato());
										lCompCumSqlDao.start();
										if (lCompCumSqlDao.next()) {
											lCompMod = (ComputiCumuloModel) lCompCumSqlDao.getModel();

											lCompMod.calcolaStringaAmmenda();
											lCompMod.calcolaStringaReclusione();

											// ===================================================================================
											// Creo un Oggetto DatiPrincipaliBeneficioCumuloModel con i dati
											// di COMPUTIMODEL;
											// Durante la stampa del prospetto sarà trattato come fosse un
											// BENEFICIO
											DatiPrincipaliBeneficioCumuloModel lDatiPrincBenefdaStatEsec = new DatiPrincipaliBeneficioCumuloModel(
													lCompMod);

											if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
												lDatiPrincBenefdaStatEsec.setCodTipoBeneficio("03");
												lDatiPrincBenefdaStatEsec.setDescrTipoBeneficio("Indulto");
											} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
												lDatiPrincBenefdaStatEsec.setCodTipoBeneficio("04");
												lDatiPrincBenefdaStatEsec.setDescrTipoBeneficio("Amnistia");
											}

											// ====================================================================================================================
											// inserisco nel Model di Stampa (nella parte
											// DatiPrincipaliTitoloCumulato)
											// gli estremi dell'Ordinanza di Concessione Beneficio
											DatiPrincipaliTitoloCumulatoModel lDatiPrincOrd = new DatiPrincipaliTitoloCumulatoModel(
													lRichTitCumMod.getTitIdTitoloCumulato(),
													lStatoEseMod.getCodTipoProvvedimento(),
													lStatoEseMod.getDescrTipoProvvedimento(),
													lStatoEseMod.getDataEmissione(), lCompMod.getAnnoProvv(),
													// Ticket#20210824013 - il ProgrProvv potrebbe essere null
													//lCompMod.getProgrProvv().toString(),
													lCompMod.getProgrProvv()!=null ? lCompMod.getProgrProvv().toString() : null,
													// Ticket#20210824013 - FINE
													lStatoEseMod.getCodUfficioEmittente(),
													lStatoEseMod.getDescrUfficioEmittente(),
													lStatoEseMod.getCodLuogoEmittente(),
													lStatoEseMod.getDescrLuogoEmittente(),
													lStatoEseMod.getSezioneAltro(),
													// lCompMod.getSezioneProvv(),
													lStatoEseMod.getNote());

											lDatiPrincMod.setEstremiOrdinanza(
													lDatiPrincOrd.FormaEstremiProvvedimentoperProspetto());
											// =================================================================================================================

											// Cerco il titolo di Riferimento della revoca Beneficio
											lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(
													lRichPMCumMod.getTitIdTitoloCumulatoRef());
											TitoloCumulatoModel lTitoRef = (TitoloCumulatoModel) lTitoloCumsqlDao
													.getModelByKey();

											DatiPrincipaliTitoloCumulatoModel lDatiPrincRef = new DatiPrincipaliTitoloCumulatoModel(
													lTitoRef);

											lDatiPrincRef.setEstremiProvvedimento(
													lDatiPrincRef.FormaEstremiProvvedimentoperProspetto());
											lDatiPrincBenefdaStatEsec.setStringaTitoloRef(
													lDatiPrincRef.getEstremiProvvedimento());
											lTreeDatiPrincCum.add(new TreeModel(lDatiPrincBenefdaStatEsec));

											// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA
											// ======
											// ====== ALL'ELENCO DEI BENEFICI/COMPUTI CHE CONCORRONO AL
											// CALCOLOPENA ======
											if (("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) // A =
																										// Anticipazione
											{ // R = Revoca
												// Costruisco un BeneficioCumuloModel con solo 4 campi che
												// servono ne Metodo
												BeneficioCumuloModel lBeneCumPerCalcPenaMod = new BeneficioCumuloModel();

												if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
													lBeneCumPerCalcPenaMod.setCodTipoBeneficio("03");
													lBeneCumPerCalcPenaMod.setDescrTipoBeneficio("INDULTO");
												} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
													lBeneCumPerCalcPenaMod.setCodTipoBeneficio("04");
													lBeneCumPerCalcPenaMod.setDescrTipoBeneficio("AMNISTIA");
												}

												lBeneCumPerCalcPenaMod.setCodDpr(lCompMod.getCodDpr());
												lBeneCumPerCalcPenaMod.setDescrDpr(lCompMod.getDescDpr());

												this.AggiungiAlCalcoloPena(lRichPMCumMod, "R", null,
														lBeneCumPerCalcPenaMod);
											}

										} // Chiude Computi
										// Ticket#202603160115 - stop() per chiudere subito il cursore
										lCompCumSqlDao.stop();
										// Ticket#202603160115 - FINE
									} // Chiude Ciclo su StatoEsecuzione
									// Ticket#202603160115 - stop() per chiudere subito il cursore
									lStatoEsecTitoCumSqlDAO.stop();
									// Ticket#202603160115 - FINE
								} // Chiude if CodAnnotazione = 021

								// Libeazioni Anticipate (Eventuale Richiesta alla SORV. di Revoca LIBERAZIONE
								// ANTICIPATA Concesso sul Titolo)
								if (lRichPMCumMod.getCodTipoAnnotazione().equals("020")) {
									RichPMStatoEsecCumModel lRicStaEseMod = null;
									StatoEsecTitoloCumulatoModel lStatoEseMod = null;
									LibAnticipataCumuloModel lLibAntCumMod = null;
									TreeModel lTreeDatePeriodiLAMod = null;
									TreeModel lTreeDatiPrincStatoEsecCum = null;

									PeriodoLibAntCumuloModel lPeriodo = null;

									String lStringaPeriodo = null;

									lRichPmStEsecSqlDao.ricercaRichPmStatoEsecCumByRichIdRich(
											lRichTitCumMod.getRicIdRichiestePmInCumulo());
									lRichPmStEsecSqlDao.start();
									while (lRichPmStEsecSqlDao.next()) {
										lRicStaEseMod = (RichPMStatoEsecCumModel) lRichPmStEsecSqlDao
												.getModel();

										lStEsecCumSqlDao.ricercaStatoEsecTitoloCumulatoByKey(
												lRicStaEseMod.getStatIdStatoEsecCumulo());
										lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStEsecCumSqlDao
												.getModelByKey();
										// ===
										if (lStatoEseMod != null
												&& lStatoEseMod.getIdStatoEsecTitoloCumulato() != null) {

											// ======== Creo il TreeModel di STATO_ESEC_TITOLO_CUM
											lTreeDatiPrincStatoEsecCum = new TreeModel(lStatoEseMod);

											lStringaPeriodo = "";

											lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(
													lStatoEseMod.getIdStatoEsecTitoloCumulato());
											lLibAntCumSqlDao.start();
											while (lLibAntCumSqlDao.next()) {
												lLibAntCumMod = (LibAnticipataCumuloModel) lLibAntCumSqlDao
														.getModel();

												if ("C".equals(lLibAntCumMod.getFlagConcesso())) {
													if (lStringaPeriodo.length() > 0)
														lStringaPeriodo += ", ";

													lStringaPeriodo += "giorni "
															+ lLibAntCumMod.getNumeroGiorni().toString();

													if ("LS".equals(lLibAntCumMod.getTipoLa())) {
														lStringaPeriodo += " di Liberazione Anticipata Speciale";
													} else if ("LI".equals(lLibAntCumMod.getTipoLa())) {
														lStringaPeriodo += " di Integrazione Liberazione Anticipata";
													}
												}

												// ======= Periodi: Dal giorno x Al giorno y
												// =============================
												lPeriodoSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
														lLibAntCumMod.getIdLibAnticipataCumulo());
												lPeriodoSqlDao.start();
												while (lPeriodoSqlDao.next()) {
													lPeriodo = (PeriodoLibAntCumuloModel) lPeriodoSqlDao
															.getModel();
													DatePeriodiLACumModel lDateLA = new DatePeriodiLACumModel(
															lPeriodo);
													lDateLA.setStringaPeriodoDalAl(
															lDateLA.FormaStringaDatePeriodo());
													lTreeDatePeriodiLAMod = new TreeModel(lDateLA);

													lTreeDatiPrincStatoEsecCum.add(lTreeDatePeriodiLAMod);

												}
												// Ticket#202603160115 - stop() per chiudere subito il cursore
												lPeriodoSqlDao.stop();
												// Ticket#202603160115 - FINE
												// ======= Fine DAL AL
												// ===========================================

											}
											// Ticket#202603160115 - stop() per chiudere subito il cursore
											lLibAntCumSqlDao.stop();
											// Ticket#202603160115 - FINE
											lStatoEseMod.setStringaPeriodoLA(lStringaPeriodo);
										}
										// ====
										// lTreeDatiPrincCum.add(new TreeModel(lStatoEseMod));
										lTreeDatiPrincCum.add(lTreeDatiPrincStatoEsecCum);
									}
									// Ticket#202603160115 - stop() per chiudere subito il cursore
									lRichPmStEsecSqlDao.stop();
									// Ticket#202603160115 - FINE

								} // CHIUDE if (lRichPMCumMod.getCodTipoAnnotazione().equals("020")) "Revoca
									// Libeazioni Anticipate"

								// ========================= Richieste alla SORV
								// ====================================
								// MEV_70 : Richiesta di Revoca Misura Alternativa (cod_tipo_annotazione =
								// 031)
								// ===================================================================================

								if (lRichPMCumMod.getCodTipoAnnotazione().equals("031")) {
									// REVOCA MISURA ALTERNATIVA Concessa con Ordinanza (dati su
									// StatoEsecTitoloCum e ComputiCumulo)
									StatoEsecTitoloCumulatoModel lStatoEseMod = null;
									ComputiCumuloModel lCompMod = null;

									lStatoEsecTitoCumSqlDAO.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
											lRichTitCumMod.getTitIdTitoloCumulato(),
											lRichTitCumMod.getRicIdRichiestePmInCumulo());
									lStatoEsecTitoCumSqlDAO.start();
									if (lStatoEsecTitoCumSqlDAO.next()) { // ci dovrebbe essere un solo
																			// StatoEsecuzione (per titolo e
																			// Richiesta);
										lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoCumSqlDAO
												.getModel();

										lCompCumSqlDao.ricercaComputiCumuloByIdStatoEsec(
												lStatoEseMod.getIdStatoEsecTitoloCumulato());
										lCompCumSqlDao.start();
										if (lCompCumSqlDao.next()) {
											lCompMod = (ComputiCumuloModel) lCompCumSqlDao.getModel();

											lCompMod.calcolaStringaAmmenda();
											lCompMod.calcolaStringaReclusione();

											// ====================================================================================================================
											// inserisco nel Model di Stampa (nella parte
											// DatiPrincipaliTitoloCumulato)
											// gli estremi dell'Ordinanza di Concessione Beneficio
											DatiPrincipaliTitoloCumulatoModel lDatiPrincOrd = new DatiPrincipaliTitoloCumulatoModel(
													lRichTitCumMod.getTitIdTitoloCumulato(),
													lStatoEseMod.getCodTipoProvvedimento(),
													lStatoEseMod.getDescrTipoProvvedimento(),
													lStatoEseMod.getDataEmissione(), lCompMod.getAnnoProvv(),
													// Ticket#20210824013 - il ProgrProvv potrebbe essere null
													//lCompMod.getProgrProvv().toString(),
													lCompMod.getProgrProvv()!=null ? lCompMod.getProgrProvv().toString() : null,
													// Ticket#20210824013 - FINE													
													lStatoEseMod.getCodUfficioEmittente(),
													lStatoEseMod.getDescrUfficioEmittente(),
													lStatoEseMod.getCodLuogoEmittente(),
													lStatoEseMod.getDescrLuogoEmittente(),
													lStatoEseMod.getSezioneAltro(),

													// Solo per REVOCA MA - -Luogo Esec Misura nelle Note
													// lStatoEseMod.getNote()
													lCompMod.getLuogoEsecMisura());

											lDatiPrincMod.setNote(lCompMod.getLuogoEsecMisura());
											lDatiPrincMod.setEstremiOrdinanza(
													lDatiPrincOrd.FormaEstremiProvvedimentoperProspetto());
											// =================================================================================================================

											lTreeDatiPrincCum.add(new TreeModel(lStatoEseMod));

											// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA
											// ======
											// ====== ALL'ELENCO DEI BENEFICI/COMPUTI CHE CONCORRONO AL
											// CALCOLOPENA ======

										} // Chiude Computi

									} // Chiude Ciclo su StatoEsecuzione

								} // Chiude if CodAnnotazione = 031

								// ========================= Richieste alla SORV
								// ====================================
								// MEV_70 : Richiesta di Unificazione Misure di Sicurezza
								// (cod_tipo_annotazione = 022)
								// Aggiungo MISURE_SICUREZZA legate a Richiesta_PM e Titolo_Cumulato
								// ===================================================================================

								if (lRichPMCumMod.getCodTipoAnnotazione().equals("022")
										// MEV70 aggiungo Applicazione Benefici 002 e 003
										|| lRichPMCumMod.getCodTipoAnnotazione().equals("002")
										|| lRichPMCumMod.getCodTipoAnnotazione().equals("003")) {
									// Misure Sicurezza
									lMisSicSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
									lMisSicSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCumRichGE(
											lTitoMod.getIdTitoloCumulato(),
											lRichPMCumMod.getIdRichiestePmInCumulo());

									Vector lListaMisure = new Vector(lMisSicSqlDao.getModels());

									if (lListaMisure != null && lListaMisure.size() > 0) {
										Iterator lItxMS = lListaMisure.iterator();
										while (lItxMS.hasNext()) {
											MisuraSicurezzaCumuloModel lMis = (MisuraSicurezzaCumuloModel) lItxMS
													.next();
											if (lMis != null && lMis.getIdMisuraSicurezzaCumulo() != null) {
												if (lMis.getFlagStato().compareTo("C") != 0) {
													TreeModel lTreeMisSicMod = new TreeModel(lMis);

													lTreeDatiPrincCum.add(lTreeMisSicMod);
												}
											}
										}
									}

								}
								// ============================================================

								lTreeRichiestaPMMod.add(lTreeDatiPrincCum);

							} // Chiude if(lTitoMod!=null && lTitoMod.getIdTitoloCumulato()!=null)

							// Reati Cumulo ( interessano solo una Eventuale Richiesta di Revoca Pena
							// Principale
							// da inserire nella Stampa Prospetto)
							if (lRichPMCumMod.getCodTipoAnnotazione().equals("004")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("013")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("017")
									// MEV70 aggiungo Applicazione Benefici 002 e 003
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("002")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("003")) {

								lReaSqlDao.ricercaReatiCumNoCircostanzaByIdTitoloRichGE(
										lRichTitCumMod.getTitIdTitoloCumulato(),
										lRichTitCumMod.getRicIdRichiestePmInCumulo());
								Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(
										lReaSqlDao.getModels());

								Iterator lItxRR = lReati.iterator();
								while (lItxRR.hasNext()) {
									ReatoCircostanzaCumuloModel ReaModel = new ReatoCircostanzaCumuloModel();
									ReaModel.setReatoCum((ReatoCumuloModel) lItxRR.next());
									TreeModel lTreeReatoMod = new TreeModel(ReaModel.getReatoCum());

									lReaSqlDao.ricercaCircostanzeReatoCumByReatoTitoloCum(
											ReaModel.getReatoCum().getProgrReato(),
											lRichTitCumMod.getTitIdTitoloCumulato());
									List lCircostanze = new ArrayList(lReaSqlDao.getModels());
									ReaModel.setCircostanzeCum((ReatoCumuloModel[]) lCircostanze
											.toArray(new ReatoCumuloModel[0]));

									int count = 0; // Circostanze
									while (count < ReaModel.getCircostanzeCum().length) {
										TreeModel lTreeCirc = new TreeModel(
												ReaModel.getCircostanzeCum()[count]);
										lTreeReatoMod.add(lTreeCirc);
										count++;
									}

									lTreeDatiPrincCum.add(lTreeReatoMod);
								}

							}

							// Sanzione Sostitutiva Cumulo (Eventuale Richiesta di Revoca della Stessa da
							// inserire
							// nella Stampa Prospetto)
							lSSCumSqlDao.ricercaSanzioneSostitutivaCumByTitoloCumRichGE(
									lRichTitCumMod.getTitIdTitoloCumulato(),
									lRichTitCumMod.getRicIdRichiestePmInCumulo());
							lSSCumSqlDao.start();
							while (lSSCumSqlDao.next()) {
								lSSCumMod = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
								lSSCumMod.calcolaStringaSanzionePerStampaProspetto();

								SanzioneSostitutivaCumuloModel SSCumModMini = new SanzioneSostitutivaCumuloModel();
								SSCumModMini.setCodTipoSanzione(lSSCumMod.getCodTipoSanzione());
								SSCumModMini.setDescrTipoSanzione(lSSCumMod.getDescrTipoSanzione());
								SSCumModMini.setFlagPenaNetta(lSSCumMod.getFlagPenaNetta());
								SSCumModMini.setStringaSanzione(lSSCumMod.getStringaSanzione());

								lTreeDatiPrincCum.add(new TreeModel(SSCumModMini));
							}

							// Pena Accessoria Cumulo (Eventuale Richiesta di Sostituzione della Stessa da
							// inserire nella Stampa Prospetto)
							lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCumRichGE(
									lRichTitCumMod.getTitIdTitoloCumulato(),
									lRichTitCumMod.getRicIdRichiestePmInCumulo());
							lPACumSqlDao.start();
							while (lPACumSqlDao.next()) {
								lPACumMod = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();
								lPACumMod.calcolaStringaDurataPACum();

								PenaAccessoriaCumuloModel lPaCumModMini = new PenaAccessoriaCumuloModel();
								lPaCumModMini.setCodTipoPenaAccessoria(lPACumMod.getCodTipoPenaAccessoria());
								lPaCumModMini
										.setDescrTipoPenaAccessoria(lPACumMod.getDescrTipoPenaAccessoria());
								lPaCumModMini.setDescrAltrePA(lPACumMod.getDescrAltrePA());
								lPaCumModMini.setDurata(lPACumMod.getDurata());
								lPaCumModMini.setDescrDurata(lPACumMod.getDescrDurata());
								lPaCumModMini.setNote(lPACumMod.getNote());
								lPaCumModMini.setStringaDurata(lPACumMod.getStringaDurata());
								// mettiamo i seguenti a 'null' per evitare che escano nel file Xml
								lPaCumModMini.setCodUfficioInserimento(null);
								lPaCumModMini.setCodUfficioAggiornamento(null);

								lTreeDatiPrincCum.add(new TreeModel(lPaCumModMini));
							}

						} // chiude while (lRichPMTitSqlDao.next()) { Elenco Titoli Cimilati della Ricjìhiesta
						// Ticket#202603160115 - stop() per chiudere subito il cursore
						lRichPMTitSqlDao.stop();
						// Ticket#202603160115 - FINE
						lTreeIstruMod.add(lTreeRichiestaPMMod);

					} // Chiude while (lItxR.hasNext())

				} // Chiude if(VecRichieste!=null && VecRichieste.size() > 0)

				// FINE parte Richieste PM
				// ========================================

				// =================================================
				// Start Richieste Inviate
				// siesLogger.debug("--XX-- START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Richiesta Inviata ");
				lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
				lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

				RichiesteInviateCumModel lRichInvMod = new RichiesteInviateCumModel();
				lRicInvSqlDao.ricercaRichiesteInviateCumByIdIstruttoria(
						aIstruttoriaCumulo.getIdIstruttoriaCumulo());
				Vector<RichiesteInviateCumModel> lVecInv = new Vector<RichiesteInviateCumModel>(
						lRicInvSqlDao.getModels());

				if (lVecInv != null && lVecInv.size() > 0) {
					Iterator itxRI = lVecInv.iterator();
					while (itxRI.hasNext()) {
						lRichInvMod = (RichiesteInviateCumModel) itxRI.next();
						// siesLogger.debug("--XX-- Richiesta Inviata =
						// "+lRichInvMod.getIdRichiesteInviateCum());

						// Ricerca RICHIESTE_PM_IN_CUMULO legate alla richiesta inviata
						if (lRichInvMod != null && lRichInvMod.getIdRichiesteInviateCum() != null) {
							lRicPMSqlDao.ricercaRichiestePmInCumuloByIdRichInviateCum(
									lRichInvMod.getIdRichiesteInviateCum());
							Vector<RichiestePmInCumuloModel> listaRichieste = new Vector<RichiestePmInCumuloModel>(
									lRicPMSqlDao.getModels());
							if (listaRichieste != null && listaRichieste.size() > 0) {
								lRichInvMod.setListaRichiestePMinCumulo(listaRichieste);
							}
						}

						// PRELEVA DATI RICHIESTE INVIATE
						TreeModel lTreeRichiestaINV = new TreeModel();
						lTreeRichiestaINV = prelevaDatiRichiestaInviataCumulo(lConn, lTreeRichiestaINV,
								aUtenteMod, lUfficioMod, aIstruttoriaCumulo, lRichInvMod);
						lTreeIstruMod.add(lTreeRichiestaINV);
					}
				}
				// End Richieste Inviate
				// =====================================================
				/*
				 * //
				 * =================================================================================----------
				 * -- // Produzione dei RIEPILOGHI Totali dell'istruttoria e dei dati Analitici //
				 * =================================================================================----------
				 * --
				 *
				 * // ------------- RIEPILOGO dei periodi PRESOFFERTO (Misure Cautelari) nell'ambito di una //
				 * Istruttoria_Cumulo ----------------- if (VecMisCau != null && VecMisCau.size() > 0) { //
				 * siesLogger.debug("---XXXZ--- Totali Misure Cautelari  = "+VecMisCau.size()); CalendarModel
				 * lTotPre = new CalendarModel();
				 *
				 * CalendarUtil lCalUtil = new CalendarUtil();
				 *
				 * Iterator ItM = VecMisCau.iterator(); while (ItM.hasNext()) { MisuraCautelareCumuloModel
				 * lMisMod = (MisuraCautelareCumuloModel) ItM.next(); if (lMisMod != null &&
				 * lMisMod.getIdMisuraCautelareCumulo() != null) { CalendarModel lCalMod = new
				 * CalendarModel();
				 *
				 * lCalMod.setNumAnni(lMisMod.getNumAnni()); lCalMod.setNumMesi(lMisMod.getNumMesi());
				 * lCalMod.setNumGiorni(lMisMod.getNumGiorni());
				 *
				 * lTotPre = lCalUtil.sommaGiornieValute(lTotPre, lCalMod); } }
				 *
				 * RiepilogoPresoffertoCumuloModel lRiepilogo = new RiepilogoPresoffertoCumuloModel();
				 *
				 * lRiepilogo.setTotAnniPresofferto(new BigDecimal(lTotPre.getNumAnni()));
				 * lRiepilogo.setTotMesiPresofferto(new BigDecimal(lTotPre.getNumMesi()));
				 * lRiepilogo.setTotGiorniPresofferto(new BigDecimal(lTotPre.getNumGiorni()));
				 *
				 * // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX TreeModel
				 * lTreeRiepilogo = new TreeModel(lRiepilogo);
				 *
				 * lTreeIstruMod.add(lTreeRiepilogo); //
				 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
				 *
				 * } // Chiude VecMisCau
				 *
				 * // ------------ RIEPILOGO dei BENEFICI nell'ambito di una Istruttoria_Cumulo //
				 * ------------------ if (VecBenefici != null && VecBenefici.size() > 0) { //
				 * siesLogger.debug("---XXXZ--- Totali Benefici  = "+VecBenefici.size()); Iterator ItB =
				 * VecBenefici.iterator(); while (ItB.hasNext()) { // scarto le revoche, i Benefici Revocati e
				 * gli Indulti sono trattati a parte BeneficioCumuloModel lBenMod = (BeneficioCumuloModel)
				 * ItB.next(); if (lBenMod != null && lBenMod.getIdBeneficioCumulo() != null) { if
				 * (lBenMod.getCodTipoBeneficio().compareTo("03") == 0) // Indulto { // Viene Trattato più
				 * avanti } else if (lBenMod.getCodNaturaBeneficio().compareTo("C") == 0 &&
				 * lBenMod.getTitIdTitoloCumulatoCollegato() != null) // Beneficio // revocato { ItB.remove();
				 * } else if (lBenMod.getCodNaturaBeneficio().compareTo("R") == 0) // Revoca { ItB.remove(); }
				 * else { // Scrivo il Riepilogo delle SOSPENSIONI, NON_MENZIONI, e poi le Rimuovo dal //
				 * vector lBenMod.calcolaStringaReclusioneCumulo(); lBenMod.calcolaStringaArrestoCumulo();
				 *
				 * TreeModel lTreeBenMod = new TreeModel(lBenMod); lTreeIstruMod.add(lTreeBenMod);
				 *
				 * ItB.remove(); }
				 *
				 * } }
				 *
				 * // Nel 'VecBenefici' rimangono ora solamente Gli INDULTI e le REVOCHE INDULTO //
				 * siesLogger.debug("---XXXZ--- Dopo Remove, Totali Benefici Rimasti size = "+VecBenefici.size
				 * ());
				 *
				 * } // Chiude if(VecBenefici > 0)
				 *
				 * if (VecBenefici != null && VecBenefici.size() > 0) { DecodificheModel lModelVec = null;
				 * Iterator Ite1 = lVec.iterator(); // lVec contiene tutti i DPR while (Ite1.hasNext()) {
				 * lModelVec = (DecodificheModel) Ite1.next(); if (lModelVec.getCode() != null &&
				 * !lModelVec.getCode().equals("-")) { this.RiepilogoGeneraleIndulto(lTreeIstruMod,
				 * VecBenefici, lModelVec.getCode()); } } }
				 */
				// ------------ RIEPILOGO della PENA_COMPLESSIVA nell'ambito di una Istruttoria_Cumulo
				// ------------------
				if (VecPenaCompl != null && VecPenaCompl.size() > 0) {
					// siesLogger.debug("---XXXZ--- Totali PenaComplessiva = "+VecPenaCompl.size());
					RiepilogoPenaComplessivaCumuloModel lRiepPCModel = new RiepilogoPenaComplessivaCumuloModel();
					CalendarModel lTotaleArresti = new CalendarModel();
					CalendarModel lTotaleReclusioni = new CalendarModel();

					lRiepPCModel.setTotalePenaComplessiva(VecPenaCompl);

					// Trova se c'è lergastolo tra tutte le pene complessive dell'istruttoria
					lRiepPCModel.SeErgastolo();

					// Calcolo dei Totali ARRESTO e Amm, RECLUSIONE e Multa di Tutte le PeneComplessive
					// dell'Istruttoria
					lTotaleArresti = lRiepPCModel.getTotaleArrestiPenaCumulo();
					lTotaleReclusioni = lRiepPCModel.getTotaleReclusioniPenaCumulo();

					// riempio il model di stampa
					lRiepPCModel.setNumAnniReclusione(new BigDecimal(lTotaleReclusioni.getNumAnni()));
					lRiepPCModel.setNumMesiReclusione(new BigDecimal(lTotaleReclusioni.getNumMesi()));
					lRiepPCModel.setNumGiorniReclusione(new BigDecimal(lTotaleReclusioni.getNumGiorni()));
					lRiepPCModel.setImportoMulta(new BigDecimal(lTotaleReclusioni.getImportoMulta()));

					lRiepPCModel.setNumAnniArresto(new BigDecimal(lTotaleArresti.getNumAnni()));
					lRiepPCModel.setNumMesiArresto(new BigDecimal(lTotaleArresti.getNumMesi()));
					lRiepPCModel.setNumGiorniArresto(new BigDecimal(lTotaleArresti.getNumGiorni()));
					lRiepPCModel.setImportoAmmenda(new BigDecimal(lTotaleArresti.getImportoAmmenda()));

					lRiepPCModel.calcolaStringaArrestoCum();
					lRiepPCModel.calcolaStringaReclusioneCum();

					TreeModel lTreeRiepilogoPenCompl = new TreeModel(lRiepPCModel);
					lTreeIstruMod.add(lTreeRiepilogoPenCompl);
				}

				// --------------------------------------------

			} // Chiude if((aListaTitoli != null)

			// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
			// siesLogger.debug("add di TUTTO Il NODO ISTRUTTORIA .... ");
			// lTreeRoot.add(lTreeIstruMod);
			// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

			// ==================================================================================================================
			// MEV_70
			// viene aggiunta la parte del calcolo pena Complessiva LORDA, e
			// la parte del calcolo pena al NETTO dei presofferti e simili

			// TEST PER COMPUTO DELLE RICHIESTE
			siesLogger.debug(
					"--YY-- Parte del PROVVEDIMENTO - Chiamata al nuovo metodo AggiungiRichiesteDelPm");
			this.AggiungiRichiesteDelPm(aIstruttoriaCumulo.getIdIstruttoriaCumulo(), aCalcoloPenaModel,
					lConn);

			// ========================================================================
			// CALCOLO PENA COMPLESSIVA
			// ========================================================================
			// FIXME GESTIRE ERGASTOLO (?)
			// DEBUG

			// MEV_70
			// PenaRideterminataCumuloModel : Riportiamo il dato preso dal DB e lo inseriamo nel TreeModel di
			// stampa con il nome : CalcoloPenaRidetermCumModel
			// più avanti riportiamo il dato ricalcolato durante la stampa e sarà inserito con i nomi :
			// lPenaTotLorda e lPenaTotNetta
			lPenaRidSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
			lPenaRidSqlDao.ricercaPenaRideterminataCumulByIdIstruttoria(
					aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lPenaRidSqlDao.start();

			/*
			 * while (lPenaRidSqlDao.next()) { PenaRideterminataCumuloModel lPenaRidMod =
			 * (PenaRideterminataCumuloModel) lPenaRidSqlDao.getModel();
			 *
			 * CalcoloPenaRidetermCumModel lCalcoloPenaRid = new CalcoloPenaRidetermCumModel(lPenaRidMod);
			 * lCalcoloPenaRid.calcolaStringheXStampa();
			 *
			 * lTreeIstruMod.add(new TreeModel(lCalcoloPenaRid)); } lPenaRidSqlDao.stop();
			 */
			// Ripristino vecchio codice

			// 17/04/2020 Ticket#20200220018 - Valorizzazione di lCodTipoPenaDetentiva "03" o "04" Ergastolo e
			// relativi quantum
			String lCodTipoPenaDetentiva = "-";
			BigDecimal lNumAnniIsolamentoDiurno = null;
			BigDecimal lNumMesiIsolamentoDiurno = null;
			BigDecimal lNumGiorniIsolamentoDiurno = null;

			while (lPenaRidSqlDao.next()) {
				PenaRideterminataCumuloModel lPenaRidMod = (PenaRideterminataCumuloModel) lPenaRidSqlDao
						.getModel();

				// 17/04/2020 Ticket#20200220018 - Valorizzazione di lCodTipoPenaDetentiva "03" o "04"
				// Ergastolo e relativi quantum
				if (lPenaRidMod.getCodTipoPenaDetentiva() != null
						&& lPenaRidMod.getCodTipoPenaDetentiva().compareTo(lCodTipoPenaDetentiva) > 0)
					lCodTipoPenaDetentiva = lPenaRidMod.getCodTipoPenaDetentiva();
				if ("04".equals(lCodTipoPenaDetentiva)) {
					lNumAnniIsolamentoDiurno = lPenaRidMod.getNumAnniIsolamentoDiurno();
					lNumMesiIsolamentoDiurno = lPenaRidMod.getNumMesiIsolamentoDiurno();
					lNumGiorniIsolamentoDiurno = lPenaRidMod.getNumGiorniIsolamentoDiurno();
				}

				siesLogger.debug(" lPenaRidMod.getFlagPenaResiduaCumulo() = "
						+ lPenaRidMod.getFlagPenaResiduaCumulo());
				siesLogger.debug(" lPenaRidMod.getDataFine()  = " + lPenaRidMod.getDataFine());

				if ("S".equals(lPenaRidMod.getFlagPenaResiduaCumulo())) {
					// MEV_70 del 30/05/2019 - Solo se vengo da Stampa Provvedimento lDataEmissione != null
					if (lDataEmissione != null) {

						try {
							// Provo a calcolare i quantum di pena da espiare alla data di emissione
							// del provvedimento
							if ("S".equals(lPenaRidMod.getFlagPenaResiduaCumulo())) {
								if (lPenaRidMod.getDataFine() != null // Pena in decorrenza
										&& !"03".equals(lPenaRidMod.getCodTipoPenaDetentiva()) // non
																								// ergastolo
										&& !"04".equals(lPenaRidMod.getCodTipoPenaDetentiva()) // non
																								// ergastolo
								) { // Pena in decorrenza calcolo i quantum residui alla data di emissione

									// PenaResiduaModel lPenaDaEspiareAdOggi =
									// this.calcolaResiduoPenaAdOggi(lPenaTotNetta,
									// aEventoNotModel.getEvento().getDataEmissione());
									PenaResiduaModel lPenaDaEspiareAdOggi = this
											.calcolaResiduoPenaAdOggi(lPenaRidMod, lDataEmissione);

									siesLogger.debug("lPenaDaEspiareAdOggi = " + lPenaDaEspiareAdOggi);

									lPenaDaEspiareAdOggi.calcolaStringaReclusione();
									lPenaDaEspiareAdOggi.calcolaStringaArresto();

									String lStrResiduoAdOggi = "";

									siesLogger.debug("lPenaDaEspiareAdOggi.getStringaReclusione() = "
											+ lPenaDaEspiareAdOggi.getStringaReclusione());
									if (lPenaDaEspiareAdOggi.getStringaReclusione() != null) {
										lStrResiduoAdOggi += "" + lPenaDaEspiareAdOggi.getStringaReclusione();
									}

									lPenaRidMod.setStringaPenaResiduaAdOggi(lStrResiduoAdOggi);

								}
							}
						} catch (Exception e) {
							// do nothing
						}
					}
				}

				CalcoloPenaRidetermCumModel lCalcoloPenaRid = new CalcoloPenaRidetermCumModel(lPenaRidMod);
				siesLogger
						.debug("---YYY---1 CodTipoPenaDetentiva = " + lPenaRidMod.getCodTipoPenaDetentiva());
				// 17/04/2020 Ticket#20200220018 - Elaborazione in calcolaStringheXStampa() di
				// lCodTipoPenaDetentiva "03" o "04" Ergastolo e relativi quantum
				if ("03".equals(lCodTipoPenaDetentiva))
					lCalcoloPenaRid.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);
				if ("04".equals(lCodTipoPenaDetentiva)) {
					lCalcoloPenaRid.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);
					lCalcoloPenaRid.setNumAnniIsolamentoDiurno(lNumAnniIsolamentoDiurno);
					lCalcoloPenaRid.setNumMesiIsolamentoDiurno(lNumMesiIsolamentoDiurno);
					lCalcoloPenaRid.setNumGiorniIsolamentoDiurno(lNumGiorniIsolamentoDiurno);
				}
				lCalcoloPenaRid.calcolaStringheXStampa();

				// Aggiungi il meto od se S

				lTreeIstruMod.add(new TreeModel(lCalcoloPenaRid));
			}
			lPenaRidSqlDao.stop();
			// FIne ripristino codice

			TreeModel lTreeCalcoloPena = new TreeModel(aCalcoloPenaModel);
			// lTreeIstruMod.add(lTreeCalcoloPena);
			// DEBUG

			// Pena Principale Lorda
			PenaRideterminataCumuloModel lPenaTotLorda = aCalcoloPenaModel.getPenaPrincipaleTotLorda();

			// 19/04/2020 Ticket#20200220018 - Elaborazione in calcolaStringheXStampa() di
			// lCodTipoPenaDetentiva "03" o "04" Ergastolo e relativi quantum
			lPenaTotLorda.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);
			lPenaTotLorda.setNumGiorniIsolamentoDiurno(lNumGiorniIsolamentoDiurno);
			lPenaTotLorda.setNumMesiIsolamentoDiurno(lNumMesiIsolamentoDiurno);
			lPenaTotLorda.setNumAnniIsolamentoDiurno(lNumAnniIsolamentoDiurno);

			lPenaTotLorda.calcolaStringheXStampa();
			siesLogger.debug("--YY-- Parte del PROVVEDIMENTO -  lPenaTotLorda = " + lPenaTotLorda);
			lTreeIstruMod.add(new TreeModel(lPenaTotLorda));
			// lTreeCalcoloPena.add(new TreeModel(lPenaTotLorda));

			// Misure cautelari Totali
			MisuraCautelareCumuloModel lMCTotali = aCalcoloPenaModel.getMisureCautelariTotali();
			siesLogger.debug("--YY-- Parte del PROVVEDIMENTO -  Misure Cautelari lMCTotali = " + lMCTotali);

			// Computi Cumulo Totali (ESPIAZIONE PREGRESSA)
			ComputiCumuloModel lCMPTotali = aCalcoloPenaModel.getComputiTotali();
			siesLogger.debug("--YY-- Parte del PROVVEDIMENTO -  Computi lCMPTotali = " + lCMPTotali);

			// =============================================================================================
			// 09-12-2017 : Al Totale del PRESOFFERTO, viene aggiunto anche il totale ESPIAZIONE PREGRESSA
			// PROVVEDIMENTI con codice = 01 04 0900/0901/0902/0903/0937/0267/0270/0675
			// =============================================================================================
			RiepilogoPresoffertoCumuloModel lRiepilogo = new RiepilogoPresoffertoCumuloModel();
			CalendarUtil lCalUtil = new CalendarUtil();
			CalendarModel lCaleMod = new CalendarModel();
			CalendarModel lCaleCMP = new CalendarModel();

			// Sommo COMP: tot
			lCaleCMP.setNumAnni(lCMPTotali.getNumAnniReclusione());
			lCaleCMP.setNumMesi(lCMPTotali.getNumMesiReclusione());
			lCaleCMP.setNumGiorni(lCMPTotali.getNumGiorniReclusione());

			// Somma M.C. tot
			lCaleMod.setNumAnni(lMCTotali.getNumAnni());
			lCaleMod.setNumMesi(lMCTotali.getNumMesi());
			lCaleMod.setNumGiorni(lMCTotali.getNumGiorni());

			// Somma M.C. + COMP
			lCaleMod = lCalUtil.sommaGiornieValute(lCaleMod, lCaleCMP);

			lRiepilogo.setTotAnniPresofferto(
					lCaleMod.getNumAnni() != 0 ? new BigDecimal(lCaleMod.getNumAnni()) : null);
			lRiepilogo.setTotMesiPresofferto(
					lCaleMod.getNumMesi() != 0 ? new BigDecimal(lCaleMod.getNumMesi()) : null);
			lRiepilogo.setTotGiorniPresofferto(
					lCaleMod.getNumGiorni() != 0 ? new BigDecimal(lCaleMod.getNumGiorni()) : null);

			lRiepilogo.calcolaStringaPresofferto();

			TreeModel lTreeRiepilogo = new TreeModel(lRiepilogo);

			lTreeIstruMod.add(lTreeRiepilogo);
			lTreeIstruMod.add(new TreeModel(lMCTotali));

			// Sanzioni Sostitutive Totali x Ora solo Lorde
			aCalcoloPenaModel.calcolaTotaliSanzioniSost();
			if (aCalcoloPenaModel.getTotSemidetenzione() != null)
				lTreeCalcoloPena.add(new TreeModel(aCalcoloPenaModel.getTotSemidetenzione()));

			if (aCalcoloPenaModel.getTotLibertaControllata() != null)
				lTreeCalcoloPena.add(new TreeModel(aCalcoloPenaModel.getTotLibertaControllata()));

			// ===============================================================
			// Nuovo CALCOLO TOTALE
			// ===============================================================
			// ===========
			IIstruttoriaCumulo CtrlI = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			// CalcoloPenaCumuloModel CCalcoloPenaModel = new CalcoloPenaCumuloModel();
			aCalcoloPenaModel = CtrlI.ExCalcolaPenaCumuloByIstruttoria(
					aIstruttoriaCumulo.getIdIstruttoriaCumulo(), null, false);
			// ==============

			// Pena Principale Netta
			PenaRideterminataCumuloModel lPenaTotNetta = aCalcoloPenaModel.getPenaPrincipaleTotNetta();
			lPenaTotNetta.calcolaStringheXStampa();

			/*
			 * // MEV_70 del 30/05/2019 - Solo se vengo da Stampa Provvedimento lDataEmissione != null if
			 * (lDataEmissione != null) { try { // Provo a calcolare i quantum di pena da espiare alla data di
			 * emissione // del provvedimento if ("S".equals(lPenaTotNetta.getFlagPenaResiduaCumulo())) { if
			 * (lPenaTotNetta.getDataFine() != null // Pena in decorrenza &&
			 * !"03".equals(lPenaTotNetta.getCodTipoPenaDetentiva()) // non // ergastolo &&
			 * !"04".equals(lPenaTotNetta.getCodTipoPenaDetentiva()) // non // ergastolo ) { // Pena in
			 * decorrenza calcolo i quantum residui alla data di emissione
			 *
			 * // PenaResiduaModel lPenaDaEspiareAdOggi = // this.calcolaResiduoPenaAdOggi(lPenaTotNetta, //
			 * aEventoNotModel.getEvento().getDataEmissione()); PenaResiduaModel lPenaDaEspiareAdOggi = this
			 * .calcolaResiduoPenaAdOggi(lPenaTotNetta, lDataEmissione);
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
			 * lPenaTotNetta.setStringaPenaResiduaAdOggi(lStrResiduoAdOggi);
			 *
			 * } } } catch (Exception e) { // do nothing }
			 *
			 * }
			 */
			// ======
			siesLogger.debug("--YY-- Parte del PROVVEDIMENTO -  lPenaTotNetta = " + lPenaTotNetta);

			lTreeIstruMod.add(new TreeModel(lPenaTotNetta));

			Vector<BeneficioCumuloModel> lListaBenAggregati = aCalcoloPenaModel.getBeneficiAggregati();
			for (int i = 0; i < lListaBenAggregati.size(); i++) {
				BeneficioCumuloModel lBen = lListaBenAggregati.elementAt(i);

				lBen.calcolaStringaReclusioneCumulo();
				lBen.calcolaStringaArrestoCumulo();

				lTreeIstruMod.add(new TreeModel(lBen));
			}

			// ==================================================================================================================
			// MEV_70
			siesLogger.debug("add di TUTTO Il NODO ISTRUTTORIA al root .... ");
			lTreeRoot.add(lTreeIstruMod);
			//

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaCumuloController.prelevaDatiIstruttoriaCumulo: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("StampaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("StampaCumuloController.prelevaDatiIstruttoriaCumulo: " + e);
		} finally {
			cleanup(lSogDao);

			cleanup(RichiestePmSqlDao);
			cleanup(lTitoloCumsqlDao);
			cleanup(lRichPMTitSqlDao);
			cleanup(lReaSqlDao);
			cleanup(lSSCumSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lProvvSqlDao);
			cleanup(lBenSqlDao);
			cleanup(lRichPmStEsecSqlDao);
			cleanup(lStEsecCumSqlDao);
			cleanup(lLibAntCumSqlDao);
			cleanup(lStatoEsecTitoCumSqlDAO);
			cleanup(lCompCumSqlDao);

			cleanup(lMisSicSqlDao);
			cleanup(lPeriodoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lRicInvSqlDao);
			cleanup(lRicPMSqlDao);
			cleanup(lPenaRidSqlDao);

			cleanup(lConn);
		}

		return lTreeRoot;

	} // Chiude prelevaDatiIstruttoriaCumulo()

	/**
	*
	*/
	public TreeModel prelevaDatiIstruttoriaPerPropostaCumulo(FascicoloSiepModel lFascicoloModel,
			UtenteModel aUtenteMod, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector<TitoloCumulatoModel> aListaTitoli, String lFunzione) throws F3BException {
		Connection lConn = null;

		SoggettoSqlDAO lSogDao = null;
		RichiestePmInCumuloSqlDAO RichiestePmSqlDao = null;
		RichPMTitoloCumSqlDAO lRichPMTitSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloCumsqlDao = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSSCumSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvSqlDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		RichPMStatoEsecCumSqlDAO lRichPmStEsecSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStEsecCumSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;

		AvvocatoSiepxStampaSqlDAO lAvvSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoCumSqlDAO = null;
		ComputiCumuloSqlDAO lCompCumSqlDao = null;
		RichiesteInviateCumSqlDAO lRicInvSqlDao = null;

		MisuraSicurezzaCumuloSqlDAO lMisSicSqlDao = null;
		RichiestePmInCumuloSqlDAO lRicPMSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoSqlDao = null;
		PenaRideterminataCumuloSqlDAO lPenaRidSqlDao = null;

		TreeModel lTreeRoot = new TreeModel();

		try {
			siesLogger.debug("--XXXX-- INIZIO prelevaDatiIstruttoriaPerPropostaCumulo - Istr = "
					+ aIstruttoriaCumulo.getAnnoProtocollo() + " / " + aIstruttoriaCumulo.getNumProtocollo());
			lConn = getDBConnection();

			// vettore con i codici di MOTIVO_PROVVEDIMENTO
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			DecodificheModel lDecoModel = new DecodificheModel();
			lDecoModel.setContesto("MOTIVO_PROVVEDIMENTO");
			lVecDeco = new Vector(lDecodifiche.ExRicercaDecodifiche(lDecoModel));

			// Root X
			lTreeRoot = new TreeModel(createRootX(lUfficioMod, aUtenteMod));

			// Utente
			lTreeRoot.add(new TreeModel(aUtenteMod));

			// Fascicolo Siep
			TreeModel lTreeFascicolo = new TreeModel(lFascicoloModel);
			lTreeRoot.add(lTreeFascicolo);

			// Soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lFascicoloModel.getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogDao.getModelByKey();

			// Patch STEP1 Recupero anche residenza alias ecc
			TreeModel lTreeSogMod = getTreeSoggetto(lFascicoloModel.getSogIdSoggetto(), lFascicoloModel,
					lConn);
			lTreeRoot.add(lTreeSogMod);

			// ======================
			// Aggiungo La sentenza
			// ======================
			SentenzaModel lSentenza = lFascicoloModel.getSentenza();
			lTreeRoot.add(new TreeModel(lSentenza));

			// =====================================
			// Aggiungo gli avvocati del Fascicolo
			// =====================================
			// Avvocati
			lAvvSqlDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(lFascicoloModel.getIdFascicoloSiep());
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

			PosizioneGiuridicaCumuloModel lPosGiuMod = null;
			PosizioneGiuridicaCumuloSqlDAO lPosGiuSqlDao = null;

			// Istruttoria
			TreeModel lTreeIstruMod = new TreeModel(aIstruttoriaCumulo);

			// Titoli_Cumulati
			Iterator lItx = null;
			if (aListaTitoli != null) {
				lItx = aListaTitoli.iterator();
				while (lItx.hasNext()) {
					TitoloCumulatoModel lTitoCumMod = (TitoloCumulatoModel) lItx.next();
					TreeModel lTreeTitoloMod = new TreeModel(lTitoCumMod);

					SoggettoCumulatoModel lSoggCum = lTitoCumMod.getSoggettoCumulato();
					siesLogger.debug("lSoggCum = " + lSoggCum);
					if (lSoggCum != null) {
						lTreeTitoloMod.add(new TreeModel(lSoggCum));

						// Il soggetto ha anagrafica differente rispetto al soggetto del cumulante
						if (!lSoggCum.isStessoSoggetto(lSogModel))
							lSoggCum.calcolaStringaSoggetto();
					}

					// Procedimento cumulato
					ProcedimentoCumulatoModel lProcModel = lTitoCumMod.getProcedimentoCumulato();
					siesLogger.debug("Procedimento cumulato = " + lProcModel);
					if (lProcModel != null) {
						lProcModel.calcolaStringaProcedimento();
						lTreeTitoloMod.add(new TreeModel(lProcModel));
					}

					// ESPIAZIONE ATTUALE- Posizione Giuridica Cumulo (titolo con pena in corso di espiazione)
					lPosGiuSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
					lPosGiuSqlDao.ricercaPosizioneGiuridicaCumuloByIdTitoloCumulato(
							lTitoCumMod.getIdTitoloCumulato());
					lPosGiuMod = (PosizioneGiuridicaCumuloModel) lPosGiuSqlDao.getModelByKey();
					if (lPosGiuMod != null && lPosGiuMod.getIdPosizioneGiuridicaCum() != null) {
						siesLogger.debug(
								"Posizione Giuridica per stabilire Espiazione pena Attuale = " + lPosGiuMod);
						lTreeTitoloMod.add(new TreeModel(lPosGiuMod));
					}

					// =============================================================================================
					// Dati Analitici legati al Titolo
					appendDatiAnalitici(lConn, lTitoCumMod, lTreeTitoloMod, 2);

					// =============================================================================================
					// Dati Stato_Esecuzione_Titolo_Cumulato
					appendStatoEsecuzioneTitoloCum(lConn, aIstruttoriaCumulo.getIdIstruttoriaCumulo(),
							lTitoCumMod.getIdTitoloCumulato(), lTreeTitoloMod, 2);

					lTreeIstruMod.add(lTreeTitoloMod);

				}

				siesLogger.debug(
						"---XXXZ--- Ho Inserito tutti i Titoli e dati analitici e StatoEsecTitoloCum collegati alla Istruttoria ... ");
			}

			// ========================================================================
			// GESTIONE RICHIESTE_PM_IN_CUMULO : Richiesta al G.E e alla SORV.
			// ========================================================================
			RichiestePmSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
			lTitoloCumsqlDao = new TitoloCumulatoSqlDAO(lConn);
			lRichPMTitSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lReaSqlDao = new ReatoCumuloSqlDAO(lConn);
			lSSCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
			lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lProvvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lRichPmStEsecSqlDao = new RichPMStatoEsecCumSqlDAO(lConn);
			lStEsecCumSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lStatoEsecTitoCumSqlDAO = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lCompCumSqlDao = new ComputiCumuloSqlDAO(lConn);

			lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
			lPeriodoSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			Vector<RichiestePmInCumuloModel> VecRichieste = new Vector();
			RichiestePmInCumuloModel lRichPMCumMod = null;
			RichiestePmInCumuloModel lRichPM = null;
			RichPMTitoloCumModel lRichTitCumMod = null;
			TitoloCumulatoModel lTitoMod = null;
			SanzioneSostitutivaCumuloModel lSSCumMod = null;
			PenaAccessoriaCumuloModel lPACumMod = null;

			// Ricerca della RICHIESTA
			RichiestePmSqlDao
					.ricercaRichiestePmInCumuloByIdIstruttoria(aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			RichiestePmSqlDao.start();
			while (RichiestePmSqlDao.next()) {
				lRichPM = (RichiestePmInCumuloModel) RichiestePmSqlDao.getModel();
				siesLogger.debug("lRichPM.getCodTipoAnnotazione() = id: " + lRichPM.getIdRichiestePmInCumulo()
						+ "-" + lRichPM.getCodTipoAnnotazione());

				// Ricerca di una Eventuale Ordinanza (Decisione di GE/SORV)
				ProvvedimentoGeSorvCumModel lProvvMod = null;
				lProvvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(lRichPM.getIdRichiestePmInCumulo());
				lProvvMod = (ProvvedimentoGeSorvCumModel) lProvvSqlDao.getModelByKey();
				if (lProvvMod != null && lProvvMod.getIdProvvedimentoGeSorvCum() != null) {

					// ======================================================================================
					lRichPM.setDecisioneGeSorvCum(lProvvMod);
					// ======================================================================================

					siesLogger.debug("Trovato scarico del GE  = " + lProvvMod.getIdProvvedimentoGeSorvCum());
					// ORDINANZA di APPLICAZIONE BENEFICIO Concesso sul Titolo
					if (lRichPM.getCodTipoAnnotazione().equals("002")
							|| lRichPM.getCodTipoAnnotazione().equals("003")) {
						// Amnistia o indulto
						siesLogger.debug("AggiungiAlCalcoloPena Concesso");
						this.AggiungiAlCalcoloPena(lRichPM, "C", lProvvMod, null); // C = Concesso
					}

					// ORDINANZA di REVOCA BENEFICIO Concesso sul Titolo
					if (lRichPM.getCodTipoAnnotazione().equals("021") && lRichPM.getFlagPiuMenoR() != null) // Indulto/Amnistia
					{
						BeneficioCumuloModel lBeneCumMod = null;
						// Beneficio Cumulo (Servono i dati del Beneficio da revocare )
						lBenSqlDao.ricercaBeneficioCumuloByIdTitoloCumRichGE(lRichPM.getTitIdTitoloCumulato(),
								lRichPM.getIdRichiestePmInCumulo());
						lBenSqlDao.start();
						while (lBenSqlDao.next()) {
							lBeneCumMod = (BeneficioCumuloModel) lBenSqlDao.getModel();
							siesLogger.debug("AggiungiAlCalcoloPena Revoca");
							this.AggiungiAlCalcoloPena(lRichPM, "R", lProvvMod, lBeneCumMod); // R = Revoca
						}
					}

					// =========================================================================
					// MEV_70 : aggiungiamo comnunque le richieste: ANCHE le RICHIESTE CON DECISIONI

					VecRichieste.add(lRichPM);
					// =========================================================================

				} else {
					siesLogger.debug("Richiesta senza scarico la aggiungo al VecRichieste");
					VecRichieste.add(lRichPM);
				}
			}

			// Esaminiamo solo le RICHIESTE PRIVE di DECISIONI
			// ***** ATTENZIONE : non è più così dal Aprile 2019 - Interventi per MEV_70 --> ESAMINIAMO TUTTE
			// LE RICHIESTE
			siesLogger.debug("--XX-- Totale Richiesta EX-SENZA Decisione = " + VecRichieste.size());
			if (VecRichieste != null && VecRichieste.size() > 0) {
				// siesLogger.debug("--XX-- RichiestePM in Id_Istruttoria = "+VecRichieste.size());
				Iterator lItxR = VecRichieste.iterator();
				while (lItxR.hasNext()) {
					lRichPMCumMod = (RichiestePmInCumuloModel) lItxR.next();

					if (lRichPMCumMod.getCodMotivo() != null) {
						// Cerco le Descrizioni di COD_MOTIVO di Richieste_PM_In_Cumulo.
						String lDescrMot = "";
						String lDescrArt = "";

						DecodificheModel lModelDecodifiche = null;
						Iterator Ite1 = lVecDeco.iterator();

						while (Ite1.hasNext()) {
							lModelDecodifiche = (DecodificheModel) Ite1.next();
							if (lModelDecodifiche.getCode().equals(lRichPMCumMod.getCodMotivo())) {
								lDescrMot = lModelDecodifiche.getDescription();

								if (lModelDecodifiche.getFiltro() != null
										&& !"".equals(lModelDecodifiche.getFiltro())) {
									lDescrArt = lModelDecodifiche.getFiltro();
								}
							}
						}

						if (!lDescrMot.equals("")) {
							lRichPMCumMod.setDescrMotivo(lDescrMot);
						}
						if (!lDescrArt.equals("")) {
							lRichPMCumMod.setDescrArticolo(lDescrArt);
						}
					}

					if (!lRichPMCumMod.isQuantumReclusioneZero())
						lRichPMCumMod.calcolaStringaReclusioneinRichiestePm();
					if (!lRichPMCumMod.isQuantumArrestoZero())
						lRichPMCumMod.calcolaStringaArrestoinRichiestePm();

					TreeModel lTreeRichiestaPMMod = new TreeModel(lRichPMCumMod);

					// ===========================================================================
					// MEV_70 : aggiungiamo comnunque le richieste: ANCHE le RICHIESTE CON DECISIONI
					// creo il TreeChild del PROVVEDIMENTO_E
					ProvvedimentoGeSorvCumModel lProvvGESORV = null;
					if (lRichPMCumMod != null && lRichPMCumMod.getDecisioneGeSorvCum() != null
							&& lRichPMCumMod.getDecisioneGeSorvCum().getIdProvvedimentoGeSorvCum() != null) {

						lProvvGESORV = lRichPMCumMod.getDecisioneGeSorvCum();

						TreeModel lTreeProvvGESORV = new TreeModel(lProvvGESORV);
						lTreeRichiestaPMMod.add(lTreeProvvGESORV);
					}
					// =========================================================================

					// Spostare qui il caricamento nei benefici
					// Eventuale Richiesta di APPLICAZIONE BENEFICIO Concesso sul Titolo
					// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
					// ====== ALL'ELENCO DEI BENEFICI CHE CONCORRONO AL CALCOLOPENA ======
					if ((lRichPMCumMod.getCodTipoAnnotazione().equals("002") // Indulto
							|| lRichPMCumMod.getCodTipoAnnotazione().equals("003") // Amnistia
					) && ("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) // A = Anticipazione
					{
						this.AggiungiAlCalcoloPena(lRichPMCumMod, "C", null, null); // C = Concesso
					}

					// ==============================================================================================================
					// RICHIESTE INVIATE
					// ==============================================================================================================
					if (lRichPMCumMod.getRicIdRichiesteInviateCum() != null) {
						RichiesteInviateCumModel lRicInvMod = null;
						lRicInvSqlDao
								.ricercaRichiesteInviateCumByKey(lRichPMCumMod.getRicIdRichiesteInviateCum());
						lRicInvMod = (RichiesteInviateCumModel) lRicInvSqlDao.getModelByKey();

						if (lRicInvMod != null && lRicInvMod.getIdRichiesteInviateCum() != null) {
							TreeModel lTreeRichiestaINV = new TreeModel(lRicInvMod);
							lTreeRichiestaPMMod.add(lTreeRichiestaINV);
						}

					}
					// =============================================================================================================
					String lTutto = "";
					// Rich_PM_Titolo_Cum: Ricerca dei TITOLI legati alla Richiesta
					lRichPMTitSqlDao
							.ricercaRichPmTitoloCumByRichIdRich(lRichPMCumMod.getIdRichiestePmInCumulo());
					lRichPMTitSqlDao.start();
					while (lRichPMTitSqlDao.next()) {
						lTutto = "";
						lRichTitCumMod = (RichPMTitoloCumModel) lRichPMTitSqlDao.getModel();
						// ===
						if (lRichTitCumMod.getFlagInteroCumulo() != null
								&& "S".equals(lRichTitCumMod.getFlagInteroCumulo())) {
							lTutto = lRichTitCumMod.getFlagInteroCumulo();
						}
						// ===
						lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(lRichTitCumMod.getTitIdTitoloCumulato());
						lTitoMod = (TitoloCumulatoModel) lTitoloCumsqlDao.getModelByKey();

						// Preparazione dei campi Principali del TITOLO_CUMULATO che Interessano la RICHIESTA
						// in esame a questa parte di Stampa
						TreeModel lTreeDatiPrincCum = null;
						if (lTitoMod != null && lTitoMod.getIdTitoloCumulato() != null) {
							DatiPrincipaliTitoloCumulatoModel lDatiPrincMod = new DatiPrincipaliTitoloCumulatoModel(
									lTitoMod);
							lDatiPrincMod.setEstremiProvvedimento(
									lDatiPrincMod.FormaEstremiProvvedimentoperProspetto());
							if (!lTutto.equals("")) {
								lDatiPrincMod.setFlagTutto(lTutto);
							}

							// ai dati principali del titolo aggiungo la PENA_COMPLESSIVA del singolo titolo
							PenaComplessivaCumuloModel lPenaCum = cercaPenaComplessivaCum(
									lTitoMod.getIdTitoloCumulato(), lConn);
							if (lPenaCum != null && lPenaCum.getIdPenaComplessivaCum() != null) {
								lPenaCum.calcolaStringaReclusioneCum();
								lPenaCum.calcolaStringaArrestoCum();
								lPenaCum.calcolaStringaIsolamentoCum();

								lDatiPrincMod.setReclusionePenaComp(lPenaCum.getStringaReclusione());
								lDatiPrincMod.setArrestoPenaComp(lPenaCum.getStringaArresto());
								lDatiPrincMod.setIsolamentoPenaComp(lPenaCum.getStringaIsolamentoDiurno());
								lDatiPrincMod.setAmmendaPenaComp(lPenaCum.getImportoAmmenda());
								lDatiPrincMod.setMultaPenaComp(lPenaCum.getImportoMulta());
							}

							// siesLogger.debug("--XX--jjjjjjjxxxxxxxxx >>>>>>>>>>>>>>>>>>>>>>>>Prina di
							// Ricerca Sanzione Sost Cum Dao");
							// ======= >>>>>>>>>>>>>>>>> MEV_70 - Aggiungo sempre la SanzioneSostitutiva, in
							// caso esista, legata alla PENA COMPLESSIVA
							// Sanzione Sostitutiva Cumulo							
							SanzioneSostitutivaCumuloModel lSSCumMod1 = new SanzioneSostitutivaCumuloModel();
							
							//==========================================================================================
							// Ticket#20200715012 - la pena complessiva potrebbe non essere presente.
							BigDecimal idPenaCompCumulo = null;
							if (lPenaCum!=null) 
								idPenaCompCumulo = lPenaCum.getIdPenaComplessivaCum();
								
							lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(
									idPenaCompCumulo,
									lRichTitCumMod.getTitIdTitoloCumulato());
							
//							lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(
//									lPenaCum.getIdPenaComplessivaCum(),
//									lRichTitCumMod.getTitIdTitoloCumulato());	
							
							// END Ticket#20200715012
							//==========================================================================================
							
							lSSCumSqlDao.start();
							while (lSSCumSqlDao.next()) {

								lSSCumMod1 = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
								if (lSSCumMod1 != null && lSSCumMod1.getIdSanzioneSostitutivaCum() != null) {

									lSSCumMod1.calcolaStringaSanzionePerStampaProspetto();
									lDatiPrincMod
											.setStringaSanzioneSostitutiva(lSSCumMod1.getStringaSanzione());

									lSSCumMod1.calcolaPeriodoSanzione();
									lDatiPrincMod
											.setPeriodoSanzioneSostitutiva(lSSCumMod1.getPeriodoSanzione());

								}

							}

							lTreeDatiPrincCum = new TreeModel(lDatiPrincMod);
							// ==== MEV_70 - Fine Aggiunta SanzioneSostitutiva <<<<<<<<<<<<<<<<<<<<<<
							// ======================================

							// Eventuale Richiesta di REVOCA BENEFICIO
							if (lRichPMCumMod.getCodTipoAnnotazione().equals("021")) {
								BeneficioCumuloModel lBeneCumMod = null;
								// Beneficio Cumulo (Servono i dati del Beneficio da revocare )
								lBenSqlDao.ricercaBeneficioCumuloByIdTitoloCumRichGE(
										lRichTitCumMod.getTitIdTitoloCumulato(),
										lRichTitCumMod.getRicIdRichiestePmInCumulo());

								// ===========================================================================================
								// REVOCA BENEFICIO Concesso sul Titolo (in sentenza): Ciclo su Elenco
								// Benefici
								lBenSqlDao.start();
								while (lBenSqlDao.next()) {
									lBeneCumMod = (BeneficioCumuloModel) lBenSqlDao.getModel();
									lBeneCumMod.calcolaStringaArrestoCumulo();
									lBeneCumMod.calcolaStringaReclusioneCumulo();

									DatiPrincipaliBeneficioCumuloModel lDatiPrincBenef = new DatiPrincipaliBeneficioCumuloModel(
											lBeneCumMod);

									// Cerco il titolo di Riferimento della revoca Beneficio
									lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(
											lRichPMCumMod.getTitIdTitoloCumulatoRef());

									// MEV_70 : nel caso REVOCA SOSPENSIONE EX ART.165, potrebbe NON esistere
									// il titolo di RIFERIMENTO
									lTitoloCumsqlDao.start();
									if (lTitoloCumsqlDao.next()) {

										TitoloCumulatoModel lTitoRef = (TitoloCumulatoModel) lTitoloCumsqlDao
												.getModelByKey();
										DatiPrincipaliTitoloCumulatoModel lDatiPrincRef = new DatiPrincipaliTitoloCumulatoModel(
												lTitoRef);
										lDatiPrincRef.setEstremiProvvedimento(
												lDatiPrincRef.FormaEstremiProvvedimentoperProspetto());

										lDatiPrincBenef
												.setStringaTitoloRef(lDatiPrincRef.getEstremiProvvedimento());
									}

									lTreeDatiPrincCum.add(new TreeModel(lDatiPrincBenef));

									// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
									// ====== ALL'ELENCO DEI BENEFICI CHE CONCORRONO AL CALCOLOPENA ======
									// A = Anticipazione
									if (("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) {
										// R = Revoca
										this.AggiungiAlCalcoloPena(lRichPMCumMod, "R", null, lBeneCumMod);
									}

								} // Chiude Ciclo Benefici

								// =========================================================================================
								// REVOCA BENEFICIO Concesso con Ordinanza (dati su StatoEsecTitoloCum e
								// ComputiCumulo)
								StatoEsecTitoloCumulatoModel lStatoEseMod = null;
								ComputiCumuloModel lCompMod = null;

								lStatoEsecTitoCumSqlDAO.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
										lRichTitCumMod.getTitIdTitoloCumulato(),
										lRichTitCumMod.getRicIdRichiestePmInCumulo());
								lStatoEsecTitoCumSqlDAO.start();
								if (lStatoEsecTitoCumSqlDAO.next()) { // ci può essere un solo StatoEsecuzione
																		// (per titolo e Richiesta), di questo
																		// tipo di Revoca Beneficio;
									lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoCumSqlDAO
											.getModel();

									lCompCumSqlDao.ricercaComputiCumuloByIdStatoEsec(
											lStatoEseMod.getIdStatoEsecTitoloCumulato());
									lCompCumSqlDao.start();
									if (lCompCumSqlDao.next()) {
										lCompMod = (ComputiCumuloModel) lCompCumSqlDao.getModel();

										lCompMod.calcolaStringaAmmenda();
										lCompMod.calcolaStringaReclusione();

										// ===================================================================================
										// Creo un Oggetto DatiPrincipaliBeneficioCumuloModel con i dati di
										// COMPUTIMODEL;
										// Durante la stampa del prospetto sarà trattato come fosse un
										// BENEFICIO
										DatiPrincipaliBeneficioCumuloModel lDatiPrincBenefdaStatEsec = new DatiPrincipaliBeneficioCumuloModel(
												lCompMod);

										if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
											lDatiPrincBenefdaStatEsec.setCodTipoBeneficio("03");
											lDatiPrincBenefdaStatEsec.setDescrTipoBeneficio("Indulto");
										} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
											lDatiPrincBenefdaStatEsec.setCodTipoBeneficio("04");
											lDatiPrincBenefdaStatEsec.setDescrTipoBeneficio("Amnistia");
										}

										// ====================================================================================================================
										// inserisco nel Model di Stampa (nella parte
										// DatiPrincipaliTitoloCumulato)
										// gli estremi dell'Ordinanza di Concessione Beneficio
										DatiPrincipaliTitoloCumulatoModel lDatiPrincOrd = new DatiPrincipaliTitoloCumulatoModel(
												lRichTitCumMod.getTitIdTitoloCumulato(),
												lStatoEseMod.getCodTipoProvvedimento(),
												lStatoEseMod.getDescrTipoProvvedimento(),
												lStatoEseMod.getDataEmissione(), lCompMod.getAnnoProvv(),
												// Ticket#20210824013 - il ProgrProvv potrebbe essere null
												//lCompMod.getProgrProvv().toString(),
												lCompMod.getProgrProvv()!=null ? lCompMod.getProgrProvv().toString() : null,
												// Ticket#20210824013 - FINE												
												lStatoEseMod.getCodUfficioEmittente(),
												lStatoEseMod.getDescrUfficioEmittente(),
												lStatoEseMod.getCodLuogoEmittente(),
												lStatoEseMod.getDescrLuogoEmittente(),
												lStatoEseMod.getSezioneAltro(),

												lStatoEseMod.getNote());

										lDatiPrincMod.setEstremiOrdinanza(
												lDatiPrincOrd.FormaEstremiProvvedimentoperProspetto());
										// =================================================================================================================

										// Cerco il titolo di Riferimento della revoca Beneficio
										lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(
												lRichPMCumMod.getTitIdTitoloCumulatoRef());
										TitoloCumulatoModel lTitoRef = (TitoloCumulatoModel) lTitoloCumsqlDao
												.getModelByKey();

										DatiPrincipaliTitoloCumulatoModel lDatiPrincRef = new DatiPrincipaliTitoloCumulatoModel(
												lTitoRef);

										lDatiPrincRef.setEstremiProvvedimento(
												lDatiPrincRef.FormaEstremiProvvedimentoperProspetto());
										lDatiPrincBenefdaStatEsec
												.setStringaTitoloRef(lDatiPrincRef.getEstremiProvvedimento());
										lTreeDatiPrincCum.add(new TreeModel(lDatiPrincBenefdaStatEsec));

										// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
										// ====== ALL'ELENCO DEI BENEFICI/COMPUTI CHE CONCORRONO AL
										// CALCOLOPENA ======
										if (("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) // A =
																									// Anticipazione
										{ // R = Revoca
											// Costruisco un BeneficioCumuloModel con solo 4 campi che servono
											// ne Metodo
											BeneficioCumuloModel lBeneCumPerCalcPenaMod = new BeneficioCumuloModel();

											if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
												lBeneCumPerCalcPenaMod.setCodTipoBeneficio("03");
												lBeneCumPerCalcPenaMod.setDescrTipoBeneficio("INDULTO");
											} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
												lBeneCumPerCalcPenaMod.setCodTipoBeneficio("04");
												lBeneCumPerCalcPenaMod.setDescrTipoBeneficio("AMNISTIA");
											}

											lBeneCumPerCalcPenaMod.setCodDpr(lCompMod.getCodDpr());
											lBeneCumPerCalcPenaMod.setDescrDpr(lCompMod.getDescDpr());

											this.AggiungiAlCalcoloPena(lRichPMCumMod, "R", null,
													lBeneCumPerCalcPenaMod);
										}

									} // Chiude Computi

								} // Chiude Ciclo su StatoEsecuzione

							} // Chiude if CodAnnotazione = 021

							// Libeazioni Anticipate (Eventuale Richiesta alla SORV. di Revoca LIBERAZIONE
							// ANTICIPATA Concesso sul Titolo)
							if (lRichPMCumMod.getCodTipoAnnotazione().equals("020")) {
								RichPMStatoEsecCumModel lRicStaEseMod = null;
								StatoEsecTitoloCumulatoModel lStatoEseMod = null;
								LibAnticipataCumuloModel lLibAntCumMod = null;

								TreeModel lTreeDatePeriodiLAMod = null;
								TreeModel lTreeDatiPrincStatoEsecCum = null;
								PeriodoLibAntCumuloModel lPeriodo = null;

								String lStringaPeriodo = null;

								lRichPmStEsecSqlDao.ricercaRichPmStatoEsecCumByRichIdRich(
										lRichTitCumMod.getRicIdRichiestePmInCumulo());
								lRichPmStEsecSqlDao.start();
								while (lRichPmStEsecSqlDao.next()) {
									lRicStaEseMod = (RichPMStatoEsecCumModel) lRichPmStEsecSqlDao.getModel();

									lStEsecCumSqlDao.ricercaStatoEsecTitoloCumulatoByKey(
											lRicStaEseMod.getStatIdStatoEsecCumulo());
									lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStEsecCumSqlDao
											.getModelByKey();
									// ===
									if (lStatoEseMod != null
											&& lStatoEseMod.getIdStatoEsecTitoloCumulato() != null) {

										// ======== Creo il TreeModel di STATO_ESEC_TITOLO_CUM
										lTreeDatiPrincStatoEsecCum = new TreeModel(lStatoEseMod);
										//
										lStringaPeriodo = "";

										lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(
												lStatoEseMod.getIdStatoEsecTitoloCumulato());
										lLibAntCumSqlDao.start();
										while (lLibAntCumSqlDao.next()) {
											lLibAntCumMod = (LibAnticipataCumuloModel) lLibAntCumSqlDao
													.getModel();

											if ("C".equals(lLibAntCumMod.getFlagConcesso())) {
												if (lStringaPeriodo.length() > 0)
													lStringaPeriodo += ", ";

												lStringaPeriodo += "giorni "
														+ lLibAntCumMod.getNumeroGiorni().toString();

												if ("LS".equals(lLibAntCumMod.getTipoLa())) {
													lStringaPeriodo += " di Liberazione Anticipata Speciale";
												} else if ("LI".equals(lLibAntCumMod.getTipoLa())) {
													lStringaPeriodo += " di Integrazione Liberazione Anticipata";
												}
											}

											// ======= Periodi: Dal giorno x Al giorno y
											// =============================
											lPeriodoSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
													lLibAntCumMod.getIdLibAnticipataCumulo());
											lPeriodoSqlDao.start();
											while (lPeriodoSqlDao.next()) {
												lPeriodo = (PeriodoLibAntCumuloModel) lPeriodoSqlDao
														.getModel();
												DatePeriodiLACumModel lDateLA = new DatePeriodiLACumModel(
														lPeriodo);
												lDateLA.setStringaPeriodoDalAl(
														lDateLA.FormaStringaDatePeriodo());
												lTreeDatePeriodiLAMod = new TreeModel(lDateLA);

												lTreeDatiPrincStatoEsecCum.add(lTreeDatePeriodiLAMod);

											}

											// ======= Fine DAL AL ===========================================

										}

										lStatoEseMod.setStringaPeriodoLA(lStringaPeriodo);
									}
									// ==
									// lTreeDatiPrincCum.add(new TreeModel(lStatoEseMod));
									lTreeDatiPrincCum.add(lTreeDatiPrincStatoEsecCum);
								}

							}

							// ========================= Richieste alla SORV
							// ====================================
							// MEV_70 : Richiesta di Revoca Misura Alternativa (cod_tipo_annotazione = 031)
							// ===================================================================================

							if (lRichPMCumMod.getCodTipoAnnotazione().equals("031")) {
								// REVOCA MISURA ALTERNATIVA Concessa con Ordinanza (dati su
								// StatoEsecTitoloCum e ComputiCumulo)
								StatoEsecTitoloCumulatoModel lStatoEseMod = null;
								ComputiCumuloModel lCompMod = null;

								lStatoEsecTitoCumSqlDAO.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
										lRichTitCumMod.getTitIdTitoloCumulato(),
										lRichTitCumMod.getRicIdRichiestePmInCumulo());
								lStatoEsecTitoCumSqlDAO.start();
								if (lStatoEsecTitoCumSqlDAO.next()) { // ci dovrebbe essere un solo
																		// StatoEsecuzione (per titolo e
																		// Richiesta);
									lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoCumSqlDAO
											.getModel();

									lCompCumSqlDao.ricercaComputiCumuloByIdStatoEsec(
											lStatoEseMod.getIdStatoEsecTitoloCumulato());
									lCompCumSqlDao.start();
									if (lCompCumSqlDao.next()) {
										lCompMod = (ComputiCumuloModel) lCompCumSqlDao.getModel();
										lCompMod.calcolaStringaAmmenda();
										lCompMod.calcolaStringaReclusione();

										// ====================================================================================================================
										// inserisco nel Model di Stampa (nella parte
										// DatiPrincipaliTitoloCumulato)
										// gli estremi dell'Ordinanza di Concessione Beneficio
										DatiPrincipaliTitoloCumulatoModel lDatiPrincOrd = new DatiPrincipaliTitoloCumulatoModel(
												lRichTitCumMod.getTitIdTitoloCumulato(),
												lStatoEseMod.getCodTipoProvvedimento(),
												lStatoEseMod.getDescrTipoProvvedimento(),
												lStatoEseMod.getDataEmissione(), lCompMod.getAnnoProvv(),
												// Ticket#20210824013 - il ProgrProvv potrebbe essere null
												//lCompMod.getProgrProvv().toString(),
												lCompMod.getProgrProvv()!=null ? lCompMod.getProgrProvv().toString() : null,
												// Ticket#20210824013 - FINE												
												lStatoEseMod.getCodUfficioEmittente(),
												lStatoEseMod.getDescrUfficioEmittente(),
												lStatoEseMod.getCodLuogoEmittente(),
												lStatoEseMod.getDescrLuogoEmittente(),
												lStatoEseMod.getSezioneAltro(),

												// Solo per REVOCA MA - -Luogo Esec Misura nelle Note
												// lStatoEseMod.getNote()
												lCompMod.getLuogoEsecMisura());

										lDatiPrincMod.setNote(lCompMod.getLuogoEsecMisura());
										lDatiPrincMod.setEstremiOrdinanza(
												lDatiPrincOrd.FormaEstremiProvvedimentoperProspetto());
										// =================================================================================================================

										lTreeDatiPrincCum.add(new TreeModel(lStatoEseMod));

										// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
										// ====== ALL'ELENCO DEI BENEFICI/COMPUTI CHE CONCORRONO AL
										// CALCOLOPENA ======

									} // Chiude Computi

								} // Chiude Ciclo su StatoEsecuzione

							} // Chiude if CodAnnotazione = 031

							// ========================= Richieste alla SORV
							// ====================================
							// MEV_70 : Richiesta di Unificazione Misure di Sicurezza (cod_tipo_annotazione =
							// 022)
							// Aggiungo MISURE_SICUREZZA legate a Richiesta_PM e Titolo_Cumulato
							// ============================================================
							if (lRichPMCumMod.getCodTipoAnnotazione().equals("022")
									// MEV70 aggiungo Applicazione Benefici 002 e 003
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("002")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("003")) {
								// Misure Sicurezza
								lMisSicSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
								lMisSicSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCumRichGE(
										lTitoMod.getIdTitoloCumulato(),
										lRichPMCumMod.getIdRichiestePmInCumulo());

								Vector lListaMisure = new Vector(lMisSicSqlDao.getModels());

								if (lListaMisure != null && lListaMisure.size() > 0) {
									Iterator lItxMS = lListaMisure.iterator();
									while (lItxMS.hasNext()) {
										MisuraSicurezzaCumuloModel lMis = (MisuraSicurezzaCumuloModel) lItxMS
												.next();
										if (lMis != null && lMis.getIdMisuraSicurezzaCumulo() != null) {
											if (lMis.getFlagStato().compareTo("C") != 0) {
												TreeModel lTreeMisSicMod = new TreeModel(lMis);

												lTreeDatiPrincCum.add(lTreeMisSicMod);
											}
										}
									}
								}

							}
							// ============================================================

							lTreeRichiestaPMMod.add(lTreeDatiPrincCum);

						} // Chiude if(lTitoMod!=null && lTitoMod.getIdTitoloCumulato()!=null)

						// Reati Cumulo ( interessano solo una Eventuale Richiesta di Revoca Pena Principale
						// da inserire nella Stampa Prospetto)
						if (lRichPMCumMod.getCodTipoAnnotazione().equals("004")
								|| lRichPMCumMod.getCodTipoAnnotazione().equals("013")
								|| lRichPMCumMod.getCodTipoAnnotazione().equals("017")
								// MEV70 aggiungo Applicazione Benefici 002 e 003
								|| lRichPMCumMod.getCodTipoAnnotazione().equals("002")
								|| lRichPMCumMod.getCodTipoAnnotazione().equals("003")) {
							lReaSqlDao.ricercaReatiCumNoCircostanzaByIdTitoloRichGE(
									lRichTitCumMod.getTitIdTitoloCumulato(),
									lRichTitCumMod.getRicIdRichiestePmInCumulo());
							Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(
									lReaSqlDao.getModels());

							Iterator lItxRR = lReati.iterator();
							while (lItxRR.hasNext()) {
								ReatoCircostanzaCumuloModel ReaModel = new ReatoCircostanzaCumuloModel();
								ReaModel.setReatoCum((ReatoCumuloModel) lItxRR.next());
								TreeModel lTreeReatoMod = new TreeModel(ReaModel.getReatoCum());

								lReaSqlDao.ricercaCircostanzeReatoCumByReatoTitoloCum(
										ReaModel.getReatoCum().getProgrReato(),
										lRichTitCumMod.getTitIdTitoloCumulato());
								List lCircostanze = new ArrayList(lReaSqlDao.getModels());
								ReaModel.setCircostanzeCum(
										(ReatoCumuloModel[]) lCircostanze.toArray(new ReatoCumuloModel[0]));

								int count = 0; // Circostanze
								while (count < ReaModel.getCircostanzeCum().length) {
									TreeModel lTreeCirc = new TreeModel(ReaModel.getCircostanzeCum()[count]);
									lTreeReatoMod.add(lTreeCirc);
									count++;
								}

								lTreeDatiPrincCum.add(lTreeReatoMod);
							}

						}

						// Sanzione Sostitutiva Cumulo (Eventuale Richiesta di Revoca della Stessa da inserire
						// nella Stampa Prospetto)
						lSSCumSqlDao.ricercaSanzioneSostitutivaCumByTitoloCumRichGE(
								lRichTitCumMod.getTitIdTitoloCumulato(),
								lRichTitCumMod.getRicIdRichiestePmInCumulo());
						lSSCumSqlDao.start();
						while (lSSCumSqlDao.next()) {
							lSSCumMod = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
							lSSCumMod.calcolaStringaSanzionePerStampaProspetto();

							SanzioneSostitutivaCumuloModel SSCumModMini = new SanzioneSostitutivaCumuloModel();
							SSCumModMini.setCodTipoSanzione(lSSCumMod.getCodTipoSanzione());
							SSCumModMini.setDescrTipoSanzione(lSSCumMod.getDescrTipoSanzione());
							SSCumModMini.setFlagPenaNetta(lSSCumMod.getFlagPenaNetta());
							SSCumModMini.setStringaSanzione(lSSCumMod.getStringaSanzione());

							lTreeDatiPrincCum.add(new TreeModel(SSCumModMini));
						}

						// Pena Accessoria Cumulo (Eventuale Richiesta di Sostituzione della Stessa da
						// inserire nella Stampa Prospetto)
						lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCumRichGE(
								lRichTitCumMod.getTitIdTitoloCumulato(),
								lRichTitCumMod.getRicIdRichiestePmInCumulo());
						lPACumSqlDao.start();
						while (lPACumSqlDao.next()) {
							lPACumMod = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();
							lPACumMod.calcolaStringaDurataPACum();

							PenaAccessoriaCumuloModel lPaCumModMini = new PenaAccessoriaCumuloModel();
							lPaCumModMini.setCodTipoPenaAccessoria(lPACumMod.getCodTipoPenaAccessoria());
							lPaCumModMini.setDescrTipoPenaAccessoria(lPACumMod.getDescrTipoPenaAccessoria());
							lPaCumModMini.setDescrAltrePA(lPACumMod.getDescrAltrePA());
							lPaCumModMini.setDurata(lPACumMod.getDurata());
							lPaCumModMini.setDescrDurata(lPACumMod.getDescrDurata());
							lPaCumModMini.setNote(lPACumMod.getNote());
							lPaCumModMini.setStringaDurata(lPACumMod.getStringaDurata());
							// mettiamo i seguenti a 'null' per evitare che escano nel file Xml
							lPaCumModMini.setCodUfficioInserimento(null);
							lPaCumModMini.setCodUfficioAggiornamento(null);

							lTreeDatiPrincCum.add(new TreeModel(lPaCumModMini));
						}
					}

					lTreeIstruMod.add(lTreeRichiestaPMMod);

				} // Chiude while (lItxR.hasNext())

			} // Chiude if(VecRichieste!=null && VecRichieste.size() > 0)

			// FINE parte Richieste PM
			// ========================================

			// =================================================
			// Start Richieste Inviate
			// siesLogger.debug("--XX-- START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Richiesta Inviata ");

			if (!lFunzione.equals("Richiesta")) {
				lRicInvSqlDao = new RichiesteInviateCumSqlDAO(lConn);
				lRicPMSqlDao = new RichiestePmInCumuloSqlDAO(lConn);

				RichiesteInviateCumModel lRichInvMod = new RichiesteInviateCumModel();
				lRicInvSqlDao.ricercaRichiesteInviateCumByIdIstruttoria(
						aIstruttoriaCumulo.getIdIstruttoriaCumulo());
				Vector<RichiesteInviateCumModel> lVecInv = new Vector<RichiesteInviateCumModel>(
						lRicInvSqlDao.getModels());

				if (lVecInv != null && lVecInv.size() > 0) {
					Iterator itxRI = lVecInv.iterator();
					while (itxRI.hasNext()) {
						lRichInvMod = (RichiesteInviateCumModel) itxRI.next();
						// siesLogger.debug("--XX-- Richiesta Inviata =
						// "+lRichInvMod.getIdRichiesteInviateCum());

						// Ricerca RICHIESTE_PM_IN_CUMULO legate alla richiesta inviata
						if (lRichInvMod != null && lRichInvMod.getIdRichiesteInviateCum() != null) {
							lRicPMSqlDao.ricercaRichiestePmInCumuloByIdRichInviateCum(
									lRichInvMod.getIdRichiesteInviateCum());
							Vector<RichiestePmInCumuloModel> listaRichieste = new Vector<RichiestePmInCumuloModel>(
									lRicPMSqlDao.getModels());
							if (listaRichieste != null && listaRichieste.size() > 0) {
								lRichInvMod.setListaRichiestePMinCumulo(listaRichieste);
							}
						}

						// PRELEVA DATI RICHIESTE INVIATE
						TreeModel lTreeRichiestaINV = new TreeModel();
						lTreeRichiestaINV = prelevaDatiRichiestaInviataCumulo(lConn, lTreeRichiestaINV,
								aUtenteMod, lUfficioMod, aIstruttoriaCumulo, lRichInvMod);
						lTreeIstruMod.add(lTreeRichiestaINV);
					}
				}
			}
			// End Richieste Inviate
			// =====================================================

			// TEST PER COMPUTO DELLE RICHIESTE
			siesLogger.debug("Chiamata al nuovo metodo AggiungiRichiesteDelPm");
			this.AggiungiRichiesteDelPm(aIstruttoriaCumulo.getIdIstruttoriaCumulo(), aCalcoloPenaModel,
					lConn);

			// ========================================================================
			// CALCOLO PENA COMPLESSIVA
			// ========================================================================
			// FIXME GESTIRE ERGASTOLO (?)
			// DEBUG

			// MEV_70
			// PenaRideterminataCumuloModel : Riportiamo il dato preso dal DB e lo inseriamo nel TreeModel di
			// stampa con il nome : CalcoloPenaRidetermCumModel
			// più avanti riportiamo il dato ricalcolato durante la stampa e sarà inserito con i nomi :
			// lPenaTotLorda e lPenaTotNetta
			lPenaRidSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
			lPenaRidSqlDao.ricercaPenaRideterminataCumulByIdIstruttoria(
					aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lPenaRidSqlDao.start();
			while (lPenaRidSqlDao.next()) {
				PenaRideterminataCumuloModel lPenaRidMod = (PenaRideterminataCumuloModel) lPenaRidSqlDao
						.getModel();
				siesLogger
						.debug("---YYY---2 CodTipoPenaDetentiva = " + lPenaRidMod.getCodTipoPenaDetentiva());

				CalcoloPenaRidetermCumModel lCalcoloPenaRid = new CalcoloPenaRidetermCumModel(lPenaRidMod);
				lCalcoloPenaRid.calcolaStringheXStampa();

				lTreeIstruMod.add(new TreeModel(lCalcoloPenaRid));
			}
			lPenaRidSqlDao.stop();

			TreeModel lTreeCalcoloPena = new TreeModel(aCalcoloPenaModel);
			// lTreeIstruMod.add(lTreeCalcoloPena);
			// DEBUG

			// Pena Principale Lorda
			PenaRideterminataCumuloModel lPenaTotLorda = aCalcoloPenaModel.getPenaPrincipaleTotLorda();
			lPenaTotLorda.calcolaStringheXStampa();
			siesLogger.debug("lPenaTotLorda = " + lPenaTotLorda);
			lTreeIstruMod.add(new TreeModel(lPenaTotLorda));
			// lTreeCalcoloPena.add(new TreeModel(lPenaTotLorda));

			// Misure cautelari Totali
			MisuraCautelareCumuloModel lMCTotali = aCalcoloPenaModel.getMisureCautelariTotali();
			siesLogger.debug("Misure Cautelari lMCTotali = " + lMCTotali);

			// Computi Cumulo Totali (ESPIAZIONE PREGRESSA)
			ComputiCumuloModel lCMPTotali = aCalcoloPenaModel.getComputiTotali();
			siesLogger.debug("Computi lCMPTotali = " + lCMPTotali);

			// =============================================================================================
			// 09-12-2017 : Al Totale del PRESOFFERTO, viene aggiunto anche il totale ESPIAZIONE PREGRESSA
			// PROVVEDIMENTI con codice = 01 04 0900/0901/0902/0903/0937/0267/0270/0675
			// =============================================================================================
			RiepilogoPresoffertoCumuloModel lRiepilogo = new RiepilogoPresoffertoCumuloModel();
			CalendarUtil lCalUtil = new CalendarUtil();
			CalendarModel lCaleMod = new CalendarModel();
			CalendarModel lCaleCMP = new CalendarModel();

			// Sommo COMP: tot
			lCaleCMP.setNumAnni(lCMPTotali.getNumAnniReclusione());
			lCaleCMP.setNumMesi(lCMPTotali.getNumMesiReclusione());
			lCaleCMP.setNumGiorni(lCMPTotali.getNumGiorniReclusione());

			// Somma M.C. tot
			lCaleMod.setNumAnni(lMCTotali.getNumAnni());
			lCaleMod.setNumMesi(lMCTotali.getNumMesi());
			lCaleMod.setNumGiorni(lMCTotali.getNumGiorni());

			// Somma M.C. + COMP
			lCaleMod = lCalUtil.sommaGiornieValute(lCaleMod, lCaleCMP);

			lRiepilogo.setTotAnniPresofferto(
					lCaleMod.getNumAnni() != 0 ? new BigDecimal(lCaleMod.getNumAnni()) : null);
			lRiepilogo.setTotMesiPresofferto(
					lCaleMod.getNumMesi() != 0 ? new BigDecimal(lCaleMod.getNumMesi()) : null);
			lRiepilogo.setTotGiorniPresofferto(
					lCaleMod.getNumGiorni() != 0 ? new BigDecimal(lCaleMod.getNumGiorni()) : null);

			lRiepilogo.calcolaStringaPresofferto();

			TreeModel lTreeRiepilogo = new TreeModel(lRiepilogo);

			lTreeIstruMod.add(lTreeRiepilogo);
			lTreeIstruMod.add(new TreeModel(lMCTotali));

			// Sanzioni Sostitutive Totali x Ora solo Lorde
			aCalcoloPenaModel.calcolaTotaliSanzioniSost();
			if (aCalcoloPenaModel.getTotSemidetenzione() != null)
				lTreeCalcoloPena.add(new TreeModel(aCalcoloPenaModel.getTotSemidetenzione()));

			if (aCalcoloPenaModel.getTotLibertaControllata() != null)
				lTreeCalcoloPena.add(new TreeModel(aCalcoloPenaModel.getTotLibertaControllata()));

			// ===============================================================
			// Nuovo CALCOLO TOTALE
			// ===============================================================
			// ===========
			IIstruttoriaCumulo CtrlI = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			// CalcoloPenaCumuloModel CCalcoloPenaModel = new CalcoloPenaCumuloModel();
			aCalcoloPenaModel = CtrlI.ExCalcolaPenaCumuloByIstruttoria(
					aIstruttoriaCumulo.getIdIstruttoriaCumulo(), null, false);
			// ==============

			// Pena Principale Netta
			PenaRideterminataCumuloModel lPenaTotNetta = aCalcoloPenaModel.getPenaPrincipaleTotNetta();
			lPenaTotNetta.calcolaStringheXStampa();
			siesLogger.debug("lPenaTotNetta = " + lPenaTotNetta);

			lTreeIstruMod.add(new TreeModel(lPenaTotNetta));

			Vector<BeneficioCumuloModel> lListaBenAggregati = aCalcoloPenaModel.getBeneficiAggregati();
			for (int i = 0; i < lListaBenAggregati.size(); i++) {
				BeneficioCumuloModel lBen = lListaBenAggregati.elementAt(i);

				lBen.calcolaStringaReclusioneCumulo();
				lBen.calcolaStringaArrestoCumulo();

				lTreeIstruMod.add(new TreeModel(lBen));
			}
			
			// MEV_2025-48 - ALTRO – Aggiunta Ramo <CalcoloPenaCumulo>
			IDatiFinaliCumulo lDatFinCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
			TreeModel lTreeCalcoloPenaNew = null; 
			lTreeCalcoloPenaNew = lDatFinCtrl.getTreeModelCalcoloPenaCumulo(aIstruttoriaCumulo.getIdIstruttoriaCumulo());
			lTreeIstruMod.add(lTreeCalcoloPenaNew);
			// MEV_2025-48 - ALTRO – Aggiunta Ramo <CalcoloPenaCumulo> - FINE

			siesLogger.debug("add di TUTTO Il NODO ISTRUTTORIA al root .... ");
			lTreeRoot.add(lTreeIstruMod);

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"StampaCumuloController.prelevaDatiIstruttoriaPerPropostaCumulo: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("StampaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("StampaCumuloController.prelevaDatiIstruttoriaPerPropostaCumulo: " + e);
		} finally {
			cleanup(lSogDao);
			cleanup(lAvvSqlDao);

			cleanup(RichiestePmSqlDao);
			cleanup(lRichPMTitSqlDao);
			cleanup(lTitoloCumsqlDao);
			cleanup(lReaSqlDao);
			cleanup(lSSCumSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lProvvSqlDao);
			cleanup(lBenSqlDao);
			cleanup(lRichPmStEsecSqlDao);
			cleanup(lStEsecCumSqlDao);
			cleanup(lLibAntCumSqlDao);

			cleanup(lStatoEsecTitoCumSqlDAO);
			cleanup(lCompCumSqlDao);

			cleanup(lRicInvSqlDao);
			cleanup(lMisSicSqlDao);
			cleanup(lPeriodoSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lRicPMSqlDao);
			cleanup(lPenaRidSqlDao);

			cleanup(lConn);

		}

		return lTreeRoot;

	} // Chiude prelevaDatiIstruttoriaPerPropostaCumulo()

	public TreeModel prelevaDatiRichiestaInviataCumulo(Connection aConn, TreeModel lTreeMod,
			UtenteModel aUtenteMod, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			RichiesteInviateCumModel lRichMod) throws F3BException {
		Connection lConn = null;

		// se il parametro aConn = null provengo da Stampa Richiesta Inviata e ritorna un TreeModel completo
		// dalla root
		// se il parametro lConn != null provengo da Stampa Provvediemto di Cumulo e ritorn un TreeModel di
		// tipo RichiestaInviataCum

		if (aConn != null)
			lConn = aConn;

		TreeModel lTreeRoot = null;
		TreeModel lTreeRichiestaINV = null;

		if (aConn == null) {
			lTreeRoot = lTreeMod;
		}

		RichiestePmInCumuloSqlDAO RichiestePmSqlDao = null;
		RichPMTitoloCumSqlDAO lRichPMTitSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloCumsqlDao = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSSCumSqlDao = null;
		PenaAccessoriaCumuloSqlDAO lPACumSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvSqlDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		RichPMStatoEsecCumSqlDAO lRichPmStEsecSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStEsecCumSqlDao = null;
		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		StatoEsecTitoloCumulatoSqlDAO lStatoEsecTitoCumSqlDAO = null;
		ComputiCumuloSqlDAO lCompCumSqlDao = null;

		MisuraSicurezzaCumuloSqlDAO lMisSicSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriodoSqlDao = null;

		try {
			if (lConn == null)
				lConn = getDBConnection();

			RichiestePmSqlDao = new RichiestePmInCumuloSqlDAO(lConn);
			lTitoloCumsqlDao = new TitoloCumulatoSqlDAO(lConn);
			lRichPMTitSqlDao = new RichPMTitoloCumSqlDAO(lConn);
			lReaSqlDao = new ReatoCumuloSqlDAO(lConn);
			lSSCumSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
			lPACumSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			lProvvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(lConn);
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			lRichPmStEsecSqlDao = new RichPMStatoEsecCumSqlDAO(lConn);
			lStEsecCumSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lStatoEsecTitoCumSqlDAO = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lCompCumSqlDao = new ComputiCumuloSqlDAO(lConn);

			lMisSicSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lPeriodoSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			RichiestePmInCumuloModel lRichPMCumMod = null;
			// RichiestePmInCumuloModel lRichPM = null;
			RichPMTitoloCumModel lRichTitCumMod = null;
			TitoloCumulatoModel lTitoMod = null;
			SanzioneSostitutivaCumuloModel lSSCumMod = null;
			PenaAccessoriaCumuloModel lPACumMod = null;

			// ==============================================================================================================
			// RICHIESTE INVIATE
			// ==============================================================================================================

			if (lRichMod != null && lRichMod.getIdRichiesteInviateCum() != null) {
				lTreeRichiestaINV = new TreeModel(lRichMod);

				if (lRichMod.getListaRichiestePMinCumulo() != null
						&& lRichMod.getListaRichiestePMinCumulo().size() > 0) {

					// siesLogger.debug("--XX-- RichiestePM in Id_Istruttoria = "+VecRichieste.size());
					Iterator lItxR = lRichMod.getListaRichiestePMinCumulo().iterator();
					while (lItxR.hasNext()) {
						lRichPMCumMod = (RichiestePmInCumuloModel) lItxR.next();

						if (lRichPMCumMod.getCodMotivo() != null) {
							// Cerco le Descrizioni di COD_MOTIVO di Richieste_PM_In_Cumulo.
							String lDescrMot = "";
							String lDescrArt = "";

							DecodificheModel lModelDecodifiche = null;
							Iterator Ite1 = lVecDeco.iterator();

							while (Ite1.hasNext()) {
								lModelDecodifiche = (DecodificheModel) Ite1.next();
								if (lModelDecodifiche.getCode().equals(lRichPMCumMod.getCodMotivo())) {
									lDescrMot = lModelDecodifiche.getDescription();

									if (lModelDecodifiche.getFiltro() != null
											&& !"".equals(lModelDecodifiche.getFiltro())) {
										lDescrArt = lModelDecodifiche.getFiltro();
									}
								}
							}

							if (!lDescrMot.equals("")) {
								lRichPMCumMod.setDescrMotivo(lDescrMot);
							}
							if (!lDescrArt.equals("")) {
								lRichPMCumMod.setDescrArticolo(lDescrArt);
							}
						}

						if (!lRichPMCumMod.isQuantumReclusioneZero())
							lRichPMCumMod.calcolaStringaReclusioneinRichiestePm();
						if (!lRichPMCumMod.isQuantumArrestoZero())
							lRichPMCumMod.calcolaStringaArrestoinRichiestePm();

						TreeModel lTreeRichiestaPMMod = new TreeModel(lRichPMCumMod);

						// MEV_2025-48 - Si aggiunge la decisione se presente
						lProvvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(lRichPMCumMod.getIdRichiestePmInCumulo());
					    ProvvedimentoGeSorvCumModel lDecisione = null;
					    lDecisione = (ProvvedimentoGeSorvCumModel) lProvvSqlDao.getModelByKey();
						if (lDecisione!=null) {
						    lRichPMCumMod.setIsPresenzaDecisione(true);
						    lTreeRichiestaPMMod.add(new TreeModel(lDecisione));
						}
						else 
						    lRichPMCumMod.setIsPresenzaDecisione(false);
						// MEV_2025-48 - FINE
						
						// Spostare qui il caricamento nei benefici
						// Eventuale Richiesta di APPLICAZIONE BENEFICIO Concesso sul Titolo
						// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
						// ====== ALL'ELENCO DEI BENEFICI CHE CONCORRONO AL CALCOLOPENA ======
						if ((lRichPMCumMod.getCodTipoAnnotazione().equals("002") // Indulto
								|| lRichPMCumMod.getCodTipoAnnotazione().equals("003") // Amnistia
						) && ("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) // A = Anticipazione
						{
							this.AggiungiAlCalcoloPena(lRichPMCumMod, "C", null, null); // C = Concesso
						}

						// >>>>>>>>> Rich_PM_Titolo_Cum: Ricerca dei TITOLI legati alla Richiesta
						// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
						String lTutto = "";
						lRichPMTitSqlDao
								.ricercaRichPmTitoloCumByRichIdRich(lRichPMCumMod.getIdRichiestePmInCumulo());
						lRichPMTitSqlDao.start();
						while (lRichPMTitSqlDao.next()) {
							lTutto = "";
							lRichTitCumMod = (RichPMTitoloCumModel) lRichPMTitSqlDao.getModel();
							// ===
							if (lRichTitCumMod.getFlagInteroCumulo() != null
									&& "S".equals(lRichTitCumMod.getFlagInteroCumulo())) {
								lTutto = lRichTitCumMod.getFlagInteroCumulo();
							}
							// ===
							lTitoloCumsqlDao
									.ricercaTitoloCumulatoByKey(lRichTitCumMod.getTitIdTitoloCumulato());
							lTitoMod = (TitoloCumulatoModel) lTitoloCumsqlDao.getModelByKey();

							// Preparazione dei campi Principali del TITOLO_CUMULATO che Interessano la
							// RICHIESTA
							// in esame a questa parte di Stampa
							TreeModel lTreeDatiPrincCum = null;
							if (lTitoMod != null && lTitoMod.getIdTitoloCumulato() != null) {
								DatiPrincipaliTitoloCumulatoModel lDatiPrincMod = new DatiPrincipaliTitoloCumulatoModel(
										lTitoMod);
								lDatiPrincMod.setEstremiProvvedimento(
										lDatiPrincMod.FormaEstremiProvvedimentoperProspetto());
								if (!lTutto.equals("")) {
									lDatiPrincMod.setFlagTutto(lTutto);
								}

								// ai dati principali del titolo aggiungo la PENA_COMPLESSIVA del singolo
								// titolo
								PenaComplessivaCumuloModel lPenaCum = cercaPenaComplessivaCum(
										lTitoMod.getIdTitoloCumulato(), lConn);
								if (lPenaCum != null && lPenaCum.getIdPenaComplessivaCum() != null) {
									lPenaCum.calcolaStringaReclusioneCum();
									lPenaCum.calcolaStringaArrestoCum();
									lPenaCum.calcolaStringaIsolamentoCum();

									lDatiPrincMod.setReclusionePenaComp(lPenaCum.getStringaReclusione());
									lDatiPrincMod.setArrestoPenaComp(lPenaCum.getStringaArresto());
									lDatiPrincMod
											.setIsolamentoPenaComp(lPenaCum.getStringaIsolamentoDiurno());
									lDatiPrincMod.setAmmendaPenaComp(lPenaCum.getImportoAmmenda());
									lDatiPrincMod.setMultaPenaComp(lPenaCum.getImportoMulta());
								}

								// siesLogger.debug("--XX--jjjjjjjxxxxxxxxx >>>>>>>>>>>>>>>>>>>>>>>>Prina di
								// Ricerca Sanzione Sost Cum Dao");
								// ======= >>>>>>>>>>>>>>>>> MEV_70 - Aggiungo sempre la SanzioneSostitutiva,
								// in caso esista, legata alla PENA COMPLESSIVA
								// Sanzione Sostitutiva Cumulo
								SanzioneSostitutivaCumuloModel lSSCumMod1 = new SanzioneSostitutivaCumuloModel();
								
								//==========================================================================================
								// Ticket#20200715012 - la pena complessiva potrebbe non essere presente.
								BigDecimal idPenaCompCumulo = null;
								if (lPenaCum!=null) 
									idPenaCompCumulo = lPenaCum.getIdPenaComplessivaCum();
									
								lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(
										idPenaCompCumulo,
										lRichTitCumMod.getTitIdTitoloCumulato());
								
								
//								lSSCumSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(
//										lPenaCum.getIdPenaComplessivaCum(),
//										lRichTitCumMod.getTitIdTitoloCumulato());								
								// END Ticket#20200715012
								//==========================================================================================
				
								lSSCumSqlDao.start();
								while (lSSCumSqlDao.next()) {

									lSSCumMod1 = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
									if (lSSCumMod1 != null
											&& lSSCumMod1.getIdSanzioneSostitutivaCum() != null) {

										lSSCumMod1.calcolaStringaSanzionePerStampaProspetto();
										lDatiPrincMod.setStringaSanzioneSostitutiva(
												lSSCumMod1.getStringaSanzione());

										lSSCumMod1.calcolaPeriodoSanzione();
										lDatiPrincMod.setPeriodoSanzioneSostitutiva(
												lSSCumMod1.getPeriodoSanzione());

									}

								}

								lTreeDatiPrincCum = new TreeModel(lDatiPrincMod);
								// ==== MEV_70 - Fine Aggiunta SanzioneSostitutiva <<<<<<<<<<<<<<<<<<<<<<
								// ======================================

								// Eventuale Richiesta di REVOCA BENEFICIO
								if (lRichPMCumMod.getCodTipoAnnotazione().equals("021")) {
									BeneficioCumuloModel lBeneCumMod = null;
									// Beneficio Cumulo (Servono i dati del Beneficio da revocare )
									lBenSqlDao.ricercaBeneficioCumuloByIdTitoloCumRichGE(
											lRichTitCumMod.getTitIdTitoloCumulato(),
											lRichTitCumMod.getRicIdRichiestePmInCumulo());

									// ===========================================================================================
									// REVOCA BENEFICIO Concesso sul Titolo (in sentenza): Ciclo su Elenco
									// Benefici
									lBenSqlDao.start();
									while (lBenSqlDao.next()) {
										lBeneCumMod = (BeneficioCumuloModel) lBenSqlDao.getModel();
										lBeneCumMod.calcolaStringaArrestoCumulo();
										lBeneCumMod.calcolaStringaReclusioneCumulo();

										DatiPrincipaliBeneficioCumuloModel lDatiPrincBenef = new DatiPrincipaliBeneficioCumuloModel(
												lBeneCumMod);

										// Cerco il titolo di Riferimento della revoca Beneficio
										lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(
												lRichPMCumMod.getTitIdTitoloCumulatoRef());

										// MEV_70 : nel caso REVOCA SOSPENSIONE EX ART.165, potrebbe NON
										// esistere il titolo di RIFERIMENTO
										lTitoloCumsqlDao.start();
										if (lTitoloCumsqlDao.next()) {
											TitoloCumulatoModel lTitoRef = (TitoloCumulatoModel) lTitoloCumsqlDao
													.getModelByKey();

											DatiPrincipaliTitoloCumulatoModel lDatiPrincRef = new DatiPrincipaliTitoloCumulatoModel(
													lTitoRef);
											lDatiPrincRef.setEstremiProvvedimento(
													lDatiPrincRef.FormaEstremiProvvedimentoperProspetto());

											lDatiPrincBenef.setStringaTitoloRef(
													lDatiPrincRef.getEstremiProvvedimento());
										}

										lTreeDatiPrincCum.add(new TreeModel(lDatiPrincBenef));

										// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA ======
										// ====== ALL'ELENCO DEI BENEFICI CHE CONCORRONO AL CALCOLOPENA ======
										// A = Anticipazione
										if (("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) {
											// R = Revoca
											this.AggiungiAlCalcoloPena(lRichPMCumMod, "R", null, lBeneCumMod);
										}

									} // Chiude Ciclo Benefici

									// =========================================================================================
									// REVOCA BENEFICIO Concesso con Ordinanza (dati su StatoEsecTitoloCum e
									// ComputiCumulo)
									StatoEsecTitoloCumulatoModel lStatoEseMod = null;
									ComputiCumuloModel lCompMod = null;

									lStatoEsecTitoCumSqlDAO.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
											lRichTitCumMod.getTitIdTitoloCumulato(),
											lRichTitCumMod.getRicIdRichiestePmInCumulo());
									lStatoEsecTitoCumSqlDAO.start();
									if (lStatoEsecTitoCumSqlDAO.next()) { // ci può essere un solo
																			// StatoEsecuzione (per titolo e
																			// Richiesta), di questo tipo di
																			// Revoca Beneficio;
										lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoCumSqlDAO
												.getModel();

										lCompCumSqlDao.ricercaComputiCumuloByIdStatoEsec(
												lStatoEseMod.getIdStatoEsecTitoloCumulato());
										lCompCumSqlDao.start();
										if (lCompCumSqlDao.next()) {
											lCompMod = (ComputiCumuloModel) lCompCumSqlDao.getModel();

											lCompMod.calcolaStringaAmmenda();
											lCompMod.calcolaStringaReclusione();

											// ===================================================================================
											// Creo un Oggetto DatiPrincipaliBeneficioCumuloModel con i dati
											// di COMPUTIMODEL;
											// Durante la stampa del prospetto sarà trattato come fosse un
											// BENEFICIO
											DatiPrincipaliBeneficioCumuloModel lDatiPrincBenefdaStatEsec = new DatiPrincipaliBeneficioCumuloModel(
													lCompMod);

											if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
												lDatiPrincBenefdaStatEsec.setCodTipoBeneficio("03");
												lDatiPrincBenefdaStatEsec.setDescrTipoBeneficio("Indulto");
											} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
												lDatiPrincBenefdaStatEsec.setCodTipoBeneficio("04");
												lDatiPrincBenefdaStatEsec.setDescrTipoBeneficio("Amnistia");
											}

											// ====================================================================================================================
											// inserisco nel Model di Stampa (nella parte
											// DatiPrincipaliTitoloCumulato)
											// gli estremi dell'Ordinanza di Concessione Beneficio
											DatiPrincipaliTitoloCumulatoModel lDatiPrincOrd = new DatiPrincipaliTitoloCumulatoModel(
													lRichTitCumMod.getTitIdTitoloCumulato(),
													lStatoEseMod.getCodTipoProvvedimento(),
													lStatoEseMod.getDescrTipoProvvedimento(),
													lStatoEseMod.getDataEmissione(), lCompMod.getAnnoProvv(),
													// Ticket#20210824013 - il ProgrProvv potrebbe essere null
													//lCompMod.getProgrProvv().toString(),
													lCompMod.getProgrProvv()!=null ? lCompMod.getProgrProvv().toString() : null,
													// Ticket#20210824013 - FINE													
													lStatoEseMod.getCodUfficioEmittente(),
													lStatoEseMod.getDescrUfficioEmittente(),
													lStatoEseMod.getCodLuogoEmittente(),
													lStatoEseMod.getDescrLuogoEmittente(),
													lStatoEseMod.getSezioneAltro(),
													// lCompMod.getSezioneProvv(),
													lStatoEseMod.getNote());

											lDatiPrincMod.setEstremiOrdinanza(
													lDatiPrincOrd.FormaEstremiProvvedimentoperProspetto());
											// =================================================================================================================

											// Cerco il titolo di Riferimento della revoca Beneficio
											lTitoloCumsqlDao.ricercaTitoloCumulatoByKey(
													lRichPMCumMod.getTitIdTitoloCumulatoRef());
											TitoloCumulatoModel lTitoRef = (TitoloCumulatoModel) lTitoloCumsqlDao
													.getModelByKey();

											DatiPrincipaliTitoloCumulatoModel lDatiPrincRef = new DatiPrincipaliTitoloCumulatoModel(
													lTitoRef);

											lDatiPrincRef.setEstremiProvvedimento(
													lDatiPrincRef.FormaEstremiProvvedimentoperProspetto());
											lDatiPrincBenefdaStatEsec.setStringaTitoloRef(
													lDatiPrincRef.getEstremiProvvedimento());
											lTreeDatiPrincCum.add(new TreeModel(lDatiPrincBenefdaStatEsec));

											// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA
											// ======
											// ====== ALL'ELENCO DEI BENEFICI/COMPUTI CHE CONCORRONO AL
											// CALCOLOPENA ======
											if (("A").equals(lRichPMCumMod.getFlagAppProvvisoria())) // A =
																										// Anticipazione
											{ // R = Revoca
												// Costruisco un BeneficioCumuloModel con solo 4 campi che
												// servono ne Metodo
												BeneficioCumuloModel lBeneCumPerCalcPenaMod = new BeneficioCumuloModel();

												if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
													lBeneCumPerCalcPenaMod.setCodTipoBeneficio("03");
													lBeneCumPerCalcPenaMod.setDescrTipoBeneficio("INDULTO");
												} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
													lBeneCumPerCalcPenaMod.setCodTipoBeneficio("04");
													lBeneCumPerCalcPenaMod.setDescrTipoBeneficio("AMNISTIA");
												}

												lBeneCumPerCalcPenaMod.setCodDpr(lCompMod.getCodDpr());
												lBeneCumPerCalcPenaMod.setDescrDpr(lCompMod.getDescDpr());

												this.AggiungiAlCalcoloPena(lRichPMCumMod, "R", null,
														lBeneCumPerCalcPenaMod);
											}

										} // Chiude Computi

									} // Chiude Ciclo su StatoEsecuzione

								} // Chiude if CodAnnotazione = 021

								// Libeazioni Anticipate (Eventuale Richiesta alla SORV. di Revoca LIBERAZIONE
								// ANTICIPATA Concesso sul Titolo)
								if (lRichPMCumMod.getCodTipoAnnotazione().equals("020")) {
									RichPMStatoEsecCumModel lRicStaEseMod = null;
									StatoEsecTitoloCumulatoModel lStatoEseMod = null;
									LibAnticipataCumuloModel lLibAntCumMod = null;

									TreeModel lTreeDatePeriodiLAMod = null;
									TreeModel lTreeDatiPrincStatoEsecCum = null;
									PeriodoLibAntCumuloModel lPeriodo = null;

									String lStringaPeriodo = null;

									lRichPmStEsecSqlDao.ricercaRichPmStatoEsecCumByRichIdRich(
											lRichTitCumMod.getRicIdRichiestePmInCumulo());
									lRichPmStEsecSqlDao.start();
									while (lRichPmStEsecSqlDao.next()) {
										lRicStaEseMod = (RichPMStatoEsecCumModel) lRichPmStEsecSqlDao
												.getModel();

										lStEsecCumSqlDao.ricercaStatoEsecTitoloCumulatoByKey(
												lRicStaEseMod.getStatIdStatoEsecCumulo());
										lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStEsecCumSqlDao
												.getModelByKey();
										// ==
										if (lStatoEseMod != null
												&& lStatoEseMod.getIdStatoEsecTitoloCumulato() != null) {

											// ======== Creo il TreeModel di STATO_ESEC_TITOLO_CUM
											lTreeDatiPrincStatoEsecCum = new TreeModel(lStatoEseMod);

											lStringaPeriodo = "";

											lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(
													lStatoEseMod.getIdStatoEsecTitoloCumulato());
											lLibAntCumSqlDao.start();
											while (lLibAntCumSqlDao.next()) {
												lLibAntCumMod = (LibAnticipataCumuloModel) lLibAntCumSqlDao
														.getModel();

												if ("C".equals(lLibAntCumMod.getFlagConcesso())) {
													if (lStringaPeriodo.length() > 0)
														lStringaPeriodo += ", ";

													lStringaPeriodo += "giorni "
															+ lLibAntCumMod.getNumeroGiorni().toString();

													if ("LS".equals(lLibAntCumMod.getTipoLa())) {
														lStringaPeriodo += " di Liberazione Anticipata Speciale";
													} else if ("LI".equals(lLibAntCumMod.getTipoLa())) {
														lStringaPeriodo += " di Integrazione Liberazione Anticipata";
													}
												}

												// ======= Periodi: Dal giorno x Al giorno y
												// =============================
												lPeriodoSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
														lLibAntCumMod.getIdLibAnticipataCumulo());
												lPeriodoSqlDao.start();
												while (lPeriodoSqlDao.next()) {
													lPeriodo = (PeriodoLibAntCumuloModel) lPeriodoSqlDao
															.getModel();
													DatePeriodiLACumModel lDateLA = new DatePeriodiLACumModel(
															lPeriodo);
													lDateLA.setStringaPeriodoDalAl(
															lDateLA.FormaStringaDatePeriodo());
													lTreeDatePeriodiLAMod = new TreeModel(lDateLA);

													lTreeDatiPrincStatoEsecCum.add(lTreeDatePeriodiLAMod);

												}

												// ======= Fine DAL AL
												// ===========================================

											}

											lStatoEseMod.setStringaPeriodoLA(lStringaPeriodo);
										}
										//
										// lTreeDatiPrincCum.add(new TreeModel(lStatoEseMod));
										lTreeDatiPrincCum.add(lTreeDatiPrincStatoEsecCum);
									}

								}

								// ========================= Richieste alla SORV
								// ====================================
								// MEV_70 : Richiesta di Revoca Misura Alternativa (cod_tipo_annotazione =
								// 031)
								// ===================================================================================
								if (lRichPMCumMod.getCodTipoAnnotazione().equals("031")) {
									// REVOCA MISURA ALTERNATIVA Concessa con Ordinanza (dati su
									// StatoEsecTitoloCum e ComputiCumulo)
									StatoEsecTitoloCumulatoModel lStatoEseMod = null;
									ComputiCumuloModel lCompMod = null;

									lStatoEsecTitoCumSqlDAO.ricercaStatoEsecTitoloCumulatoByIdTitoloCumRichGE(
											lRichTitCumMod.getTitIdTitoloCumulato(),
											lRichTitCumMod.getRicIdRichiestePmInCumulo());
									lStatoEsecTitoCumSqlDAO.start();
									if (lStatoEsecTitoCumSqlDAO.next()) { // ci dovrebbe essere un solo
																			// StatoEsecuzione (per titolo e
																			// Richiesta);
										lStatoEseMod = (StatoEsecTitoloCumulatoModel) lStatoEsecTitoCumSqlDAO
												.getModel();

										lCompCumSqlDao.ricercaComputiCumuloByIdStatoEsec(
												lStatoEseMod.getIdStatoEsecTitoloCumulato());
										lCompCumSqlDao.start();
										if (lCompCumSqlDao.next()) {
											lCompMod = (ComputiCumuloModel) lCompCumSqlDao.getModel();
											lCompMod.calcolaStringaAmmenda();
											lCompMod.calcolaStringaReclusione();

											// ====================================================================================================================
											// inserisco nel Model di Stampa (nella parte
											// DatiPrincipaliTitoloCumulato)
											// gli estremi dell'Ordinanza di Concessione Beneficio
											DatiPrincipaliTitoloCumulatoModel lDatiPrincOrd = new DatiPrincipaliTitoloCumulatoModel(
													lRichTitCumMod.getTitIdTitoloCumulato(),
													lStatoEseMod.getCodTipoProvvedimento(),
													lStatoEseMod.getDescrTipoProvvedimento(),
													lStatoEseMod.getDataEmissione(), lCompMod.getAnnoProvv(),
													// Ticket#20210824013 - il ProgrProvv potrebbe essere null
													//lCompMod.getProgrProvv().toString(),
													lCompMod.getProgrProvv()!=null ? lCompMod.getProgrProvv().toString() : null,
													// Ticket#20210824013 - FINE													
													lStatoEseMod.getCodUfficioEmittente(),
													lStatoEseMod.getDescrUfficioEmittente(),
													lStatoEseMod.getCodLuogoEmittente(),
													lStatoEseMod.getDescrLuogoEmittente(),
													lStatoEseMod.getSezioneAltro(),

													// Solo per REVOCA MA - -Luogo Esec Misura nelle Note
													// lStatoEseMod.getNote()
													lCompMod.getLuogoEsecMisura());

											lDatiPrincMod.setNote(lCompMod.getLuogoEsecMisura());
											lDatiPrincMod.setEstremiOrdinanza(
													lDatiPrincOrd.FormaEstremiProvvedimentoperProspetto());
											// =================================================================================================================

											lTreeDatiPrincCum.add(new TreeModel(lStatoEseMod));

											// ====== SOLO SE CON ANTICIPAZIONE DEGLI EFFETTI VA AGGIUNTA
											// ======
											// ====== ALL'ELENCO DEI BENEFICI/COMPUTI CHE CONCORRONO AL
											// CALCOLOPENA ======

										} // Chiude Computi

									} // Chiude Ciclo su StatoEsecuzione

								} // Chiude if CodAnnotazione = 031

								// ========================= Richieste alla SORV
								// ====================================
								// MEV_70 : Richiesta di Unificazione Misure di Sicurezza
								// (cod_tipo_annotazione = 022)
								// Aggiungo MISURE_SICUREZZA legate a Richiesta_PM e Titolo_Cumulato
								// ============================================================
								if (lRichPMCumMod.getCodTipoAnnotazione().equals("022")
										// MEV70 aggiungo Applicazione Benefici 002 e 003
										|| lRichPMCumMod.getCodTipoAnnotazione().equals("002")
										|| lRichPMCumMod.getCodTipoAnnotazione().equals("003")) {
									// Misure Sicurezza
									lMisSicSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
									lMisSicSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCumRichGE(
											lTitoMod.getIdTitoloCumulato(),
											lRichPMCumMod.getIdRichiestePmInCumulo());

									Vector lListaMisure = new Vector(lMisSicSqlDao.getModels());

									if (lListaMisure != null && lListaMisure.size() > 0) {
										Iterator lItxMS = lListaMisure.iterator();
										while (lItxMS.hasNext()) {
											MisuraSicurezzaCumuloModel lMis = (MisuraSicurezzaCumuloModel) lItxMS
													.next();
											if (lMis != null && lMis.getIdMisuraSicurezzaCumulo() != null) {
												if (lMis.getFlagStato().compareTo("C") != 0) {
													TreeModel lTreeMisSicMod = new TreeModel(lMis);

													lTreeDatiPrincCum.add(lTreeMisSicMod);
												}
											}
										}
									}

								}
								// ============================================================

								lTreeRichiestaPMMod.add(lTreeDatiPrincCum);

							} // Chiude if(lTitoMod!=null && lTitoMod.getIdTitoloCumulato()!=null)

							// Reati Cumulo ( interessano solo una Eventuale Richiesta di Revoca Pena
							// Principale
							// da inserire nella Stampa Prospetto)
							if (lRichPMCumMod.getCodTipoAnnotazione().equals("004")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("013")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("017")
									// MEV70 aggiungo Applicazione Benefici 002 e 003
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("002")
									|| lRichPMCumMod.getCodTipoAnnotazione().equals("003")) {

								lReaSqlDao.ricercaReatiCumNoCircostanzaByIdTitoloRichGE(
										lRichTitCumMod.getTitIdTitoloCumulato(),
										lRichTitCumMod.getRicIdRichiestePmInCumulo());
								Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(
										lReaSqlDao.getModels());

								Iterator lItxRR = lReati.iterator();
								while (lItxRR.hasNext()) {
									ReatoCircostanzaCumuloModel ReaModel = new ReatoCircostanzaCumuloModel();
									ReaModel.setReatoCum((ReatoCumuloModel) lItxRR.next());
									TreeModel lTreeReatoMod = new TreeModel(ReaModel.getReatoCum());

									lReaSqlDao.ricercaCircostanzeReatoCumByReatoTitoloCum(
											ReaModel.getReatoCum().getProgrReato(),
											lRichTitCumMod.getTitIdTitoloCumulato());
									List lCircostanze = new ArrayList(lReaSqlDao.getModels());
									ReaModel.setCircostanzeCum((ReatoCumuloModel[]) lCircostanze
											.toArray(new ReatoCumuloModel[0]));

									int count = 0; // Circostanze
									while (count < ReaModel.getCircostanzeCum().length) {
										TreeModel lTreeCirc = new TreeModel(
												ReaModel.getCircostanzeCum()[count]);
										lTreeReatoMod.add(lTreeCirc);
										count++;
									}

									lTreeDatiPrincCum.add(lTreeReatoMod);
								}

							}

							// Sanzione Sostitutiva Cumulo (Eventuale Richiesta di Revoca della Stessa da
							// inserire
							// nella Stampa Prospetto)
							lSSCumSqlDao.ricercaSanzioneSostitutivaCumByTitoloCumRichGE(
									lRichTitCumMod.getTitIdTitoloCumulato(),
									lRichTitCumMod.getRicIdRichiestePmInCumulo());
							lSSCumSqlDao.start();
							while (lSSCumSqlDao.next()) {
								lSSCumMod = (SanzioneSostitutivaCumuloModel) lSSCumSqlDao.getModel();
								lSSCumMod.calcolaStringaSanzionePerStampaProspetto();

								SanzioneSostitutivaCumuloModel SSCumModMini = new SanzioneSostitutivaCumuloModel();
								SSCumModMini.setCodTipoSanzione(lSSCumMod.getCodTipoSanzione());
								SSCumModMini.setDescrTipoSanzione(lSSCumMod.getDescrTipoSanzione());
								SSCumModMini.setFlagPenaNetta(lSSCumMod.getFlagPenaNetta());
								SSCumModMini.setStringaSanzione(lSSCumMod.getStringaSanzione());

								lTreeDatiPrincCum.add(new TreeModel(SSCumModMini));
							}

							// Pena Accessoria Cumulo (Eventuale Richiesta di Sostituzione della Stessa da
							// inserire nella Stampa Prospetto)
							lPACumSqlDao.ricercaPenaAccessoriaCumuloByTitoloCumRichGE(
									lRichTitCumMod.getTitIdTitoloCumulato(),
									lRichTitCumMod.getRicIdRichiestePmInCumulo());
							lPACumSqlDao.start();
							while (lPACumSqlDao.next()) {
								lPACumMod = (PenaAccessoriaCumuloModel) lPACumSqlDao.getModel();
								lPACumMod.calcolaStringaDurataPACum();

								PenaAccessoriaCumuloModel lPaCumModMini = new PenaAccessoriaCumuloModel();
								lPaCumModMini.setCodTipoPenaAccessoria(lPACumMod.getCodTipoPenaAccessoria());
								lPaCumModMini
										.setDescrTipoPenaAccessoria(lPACumMod.getDescrTipoPenaAccessoria());
								lPaCumModMini.setDescrAltrePA(lPACumMod.getDescrAltrePA());
								lPaCumModMini.setDurata(lPACumMod.getDurata());
								lPaCumModMini.setDescrDurata(lPACumMod.getDescrDurata());
								lPaCumModMini.setNote(lPACumMod.getNote());
								lPaCumModMini.setStringaDurata(lPACumMod.getStringaDurata());
								// mettiamo i seguenti a 'null' per evitare che escano nel file Xml
								lPaCumModMini.setCodUfficioInserimento(null);
								lPaCumModMini.setCodUfficioAggiornamento(null);

								lTreeDatiPrincCum.add(new TreeModel(lPaCumModMini));
							}
						}

						lTreeRichiestaINV.add(lTreeRichiestaPMMod);

					} // Chiude while (lItxR.hasNext())

				}

				if (aConn == null) {
					lTreeRoot.add(lTreeRichiestaINV);
				}
			}

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaCumuloController.prelevaDatiRichiestaInviataCumulo: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("StampaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("StampaCumuloController.prelevaDatiRichiestaInviataCumulo: " + e);
		} finally {

			cleanup(RichiestePmSqlDao);
			cleanup(lRichPMTitSqlDao);
			cleanup(lTitoloCumsqlDao);
			cleanup(lReaSqlDao);
			cleanup(lSSCumSqlDao);
			cleanup(lPACumSqlDao);
			cleanup(lProvvSqlDao);
			cleanup(lBenSqlDao);
			cleanup(lRichPmStEsecSqlDao);
			cleanup(lStEsecCumSqlDao);
			cleanup(lLibAntCumSqlDao);

			cleanup(lStatoEsecTitoCumSqlDAO);
			cleanup(lCompCumSqlDao);
			cleanup(lMisSicSqlDao);
			cleanup(lPeriodoSqlDao);

			if (aConn == null) {
				cleanup(lConn);
			}

		}

		if (aConn == null) {
			return lTreeRoot;
		} else {
			return lTreeRichiestaINV;
		}

	} // Chiude prelevaDatiRichiestaInviataCumulo()

	/**
	 * Aggiunge un Oggetto all'Elenco dei BENEFICI che concorrono al Calcolo Pena Nota che di fatto tale
	 * metodo serve per aggiungere una decisione del GE o una richiesta con anticipazione, priva di decisione,
	 * di applicazione/revoca indulto al model del calcolo pena spacciandola per un beneficio
	 * concesso/Revocato in sentenza.
	 *
	 * @param lRichPMCumMod
	 * @param lNatura
	 * @param lProvvMod
	 * @param lBeneficio
	 * @throws F3BException
	 */
	private void AggiungiAlCalcoloPena(RichiestePmInCumuloModel lRichPMCumMod, String lNatura,
			ProvvedimentoGeSorvCumModel lProvvMod, BeneficioCumuloModel lBeneficio) throws F3BException {

		// Per ora non aggiungo nulla Funzione sostituita con apposito add
		// delle richiesta al model di calcolo: AggiungiRichiesteDelPm
		// if (1 == 1)
		// BeneficioCumuloModel lBenCum = new BeneficioCumuloModel();
		// if (lProvvMod != null) {
		// lBenCum = new BeneficioCumuloModel(lProvvMod);
		//
		// lBenCum.setCodDpr(lRichPMCumMod.getCodDpr());
		// lBenCum.setDescrDpr(lRichPMCumMod.getDescrDpr());
		// } else {
		// lBenCum = new BeneficioCumuloModel(lRichPMCumMod);
		// }
		//
		// // è brutto ma serve solo per avere un Id Valorizzato
		// lBenCum.setIdBeneficioCumulo(lRichPMCumMod.getIdRichiestePmInCumulo());
		// // ============================================================
		//
		// lBenCum.setCodNaturaBeneficio(lNatura);
		// if (lRichPMCumMod.getCodTipoAnnotazione().equals("021")) { // Revoca Beneficio
		// lBenCum.setCodTipoBeneficio(lBeneficio.getCodTipoBeneficio());
		// // lBenCum.setDescrTipoBeneficio(lBeneficio.getCodTipoBeneficio());
		// lBenCum.setDescrTipoBeneficio(lBeneficio.getDescrTipoBeneficio());
		//
		// // Nelle Revoche NON è riportato il DPR dell'Indulto
		// lBenCum.setCodDpr(lBeneficio.getCodDpr());
		// lBenCum.setDescrDpr(lBeneficio.getDescrDpr());
		//
		// } else if (lRichPMCumMod.getCodTipoAnnotazione().equals("002")) // Applicaz Indulto
		// {
		// lBenCum.setCodTipoBeneficio("03");
		// lBenCum.setDescrTipoBeneficio("INDULTO");
		// } else if (lRichPMCumMod.getCodTipoAnnotazione().equals("003")) // applicaz Amnistia
		// {
		// lBenCum.setCodTipoBeneficio("04");
		// lBenCum.setDescrTipoBeneficio("AMNISTIA");
		// }
		//
		// // siesLogger.debug("addBeneficio = "+lBenCum);
		// aCalcoloPenaModel.addBeneficio(lBenCum);
		// // siesLogger.debug("--XXYYZZ-- aCalcoloPenaModel size =
		// // "+aCalcoloPenaModel.getListaBenefici().size());

	}

	// =================================================================================

	// serve per avere nelle stampe la PenaComplessiva data in sentenza (quella del TitoloCumulato) prima di
	// Eventuali Revoche
	private PenaComplessivaCumuloModel cercaPenaComplessivaCum(BigDecimal aKeyTitolo, Connection aConn)
			throws F3BException {
		PenaComplessivaCumuloModel lPcCum = null;
		PenaComplessivaCumuloSqlDAO lPcCumSqlDao = null;

		try {
			lPcCumSqlDao = new PenaComplessivaCumuloSqlDAO(aConn);
			lPcCumSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(aKeyTitolo);
			lPcCum = (PenaComplessivaCumuloModel) lPcCumSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaCumuloController.cercaPenaComplessivaCum: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("StampaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("StampaCumuloController.cercaPenaComplessivaCum: " + e);
		} finally {
			cleanup(lPcCumSqlDao);
		}

		return lPcCum;
	}

	public TreeModel prelevaDatiComunicazioniCumulo(FascicoloSiepModel lFascicoloModel,
			UtenteModel aUtenteMod, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector<TitoloCumulatoModel> aListaTitoli, EventoNotificaModel aEventoNotModel,
			String lDestinatario) throws F3BException {
		siesLogger.debug("prelevaDatiComunicazioniCumulo INIZIO");

		Connection lConn = null;
		TreeModel lTreeRoot = new TreeModel();
		SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// vettore con i codici DPR per i calcoli dei quantum Indulto nel Riepilogo
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			DecodificheModel lModel = new DecodificheModel();
			lModel.setContesto("DPR");
			Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));
			//
			// Root X
			lTreeRoot = new TreeModel(createRootX(lUfficioMod, aUtenteMod));

			// Utente
			lTreeRoot.add(new TreeModel(aUtenteMod));

			// Fascicolo Siep
			lTreeRoot.add(new TreeModel(lFascicoloModel));

			// Soggetto
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(lFascicoloModel.getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogDao.getModelByKey();

			if (lSogModel != null && lSogModel.getIdSoggetto() != null)
				lTreeRoot.add(new TreeModel(lSogModel));

			// Istruttoria
			TreeModel lTreeIstruMod = new TreeModel(aIstruttoriaCumulo);

			// Titoli_Cumulati
			Iterator lItx = null;
			if (aListaTitoli != null) {
				siesLogger.debug("aListaTitoli.size() = " + aListaTitoli.size());
				lItx = aListaTitoli.iterator();
				while (lItx.hasNext()) {
					TitoloCumulatoModel lTitoCumMod = (TitoloCumulatoModel) lItx.next();
					// Se lDestinatario = "Procure" viene eseguito un ciclo di controllo del campo
					// "CurIdCuratore" per la valorizzazione del campo "FlagEscluso" per il vettore dei Titoli
					// nell'XML,
					// per pilotare la stampa; Se lDestinatario è numerico, corrisponde all'unico
					// IdTitoloCumulato interessato.
					if (lDestinatario.compareTo("Procure") == 0) {
						// Per ogni notifica, se è stato selezionato l'ufficio come destinatario di
						// comunicazione, per convenzione
						// in fase di inserimento comunicazione è stato valorizzato il campo CurIdCuratore con
						// il valore dell' IdTitoloCumulato.
						// In tal caso si imposta il FlagEscluso="N" (altrimenti posto a "S") per una gestione
						// mirata delle stampe di Comunicazione Cumulo.
						lTitoCumMod.setFlagEscluso("S");
						NotificaModel[] lNotifiche = aEventoNotModel.getNotifiche();
						for (int j = 0; j < lNotifiche.length; j++) {
							NotificaModel lNot = lNotifiche[j];
							if (lNot.getCurIdCuratore() != null
									&& lNot.getCurIdCuratore().equals(lTitoCumMod.getIdTitoloCumulato())) {
								lTitoCumMod.setFlagEscluso("N");
								break;
							}
						}
					} else {
						// Se lDestinatario <> "Procure" allora può essere un IdTitoloCumulato (preceduto da
						// 'T')
						if (lDestinatario.startsWith("T")) {
							BigDecimal lIdTitCum = new BigDecimal(lDestinatario.substring(1));
							lTitoCumMod.setFlagEscluso("S");
							if (lIdTitCum.equals(lTitoCumMod.getIdTitoloCumulato()))
								lTitoCumMod.setFlagEscluso("N");
						}
					}

					TreeModel lTreeTitoloMod = new TreeModel(lTitoCumMod);

					// Soggetto
					SoggettoCumulatoModel lSoggCum = lTitoCumMod.getSoggettoCumulato();
					siesLogger.debug("lSoggCum = " + lSoggCum);
					if (lSoggCum != null) {
						lTreeTitoloMod.add(new TreeModel(lSoggCum));

						// Il soggetto ha anagrafica differente rispetto al soggetto del cumulante
						if (!lSoggCum.isStessoSoggetto(lSogModel))
							lSoggCum.calcolaStringaSoggetto();
					}

					// Procedimento cumulato
					ProcedimentoCumulatoModel lProcModel = lTitoCumMod.getProcedimentoCumulato();
					siesLogger.debug("Procedimento cumulato = " + lProcModel);
					if (lProcModel != null) {
						lProcModel.calcolaStringaProcedimento();
						lTreeTitoloMod.add(new TreeModel(lProcModel));
					}

					if (lTitoCumMod.getNotaTrasmissione() != null) {
						Iterator Itx = lTitoCumMod.getNotaTrasmissione().iterator();
						while (Itx.hasNext()) {
							// NotaDiTrasmissioneModel lNotaTrasm = lTitoCumMod.getNotaTrasmissione();
							NotaDiTrasmissioneModel lNotaTrasm = (NotaDiTrasmissioneModel) Itx.next();

							TreeModel lNoteTrasmTree = new TreeModel(lNotaTrasm);

							lTreeTitoloMod.add(lNoteTrasmTree);
							lNoteTrasmTree.add(new TreeModel(lNotaTrasm.getUfficioNotaTrasmissione()));
						}

					}

					// Dati Analitici legati al Titolo;
					appendDatiAnalitici(lConn, lTitoCumMod, lTreeTitoloMod, 1);

					lTreeIstruMod.add(lTreeTitoloMod);

				}

				siesLogger.debug(
						"---XXXZ--- Ho Inserito tutti i Titoli e dati analitici collegati alla Istruttoria ... ");

				// =================================================================================------------
				// Produzione dei RIEPILOGHI Totali dell'istruttoria e dei dati Analitici
				// =================================================================================------------

				// ------------- RIEPILOGO dei periodi PRESOFFERTO (Misure Cautelari) nell'ambito di una
				// Istruttoria_Cumulo -----------------
				if (VecMisCau != null && VecMisCau.size() > 0) {
					// siesLogger.debug("---XXXZ--- Totali Misure Cautelari = "+VecMisCau.size());
					CalendarModel lTotPre = new CalendarModel();

					CalendarUtil lCalUtil = new CalendarUtil();

					Iterator ItM = VecMisCau.iterator();
					while (ItM.hasNext()) {
						MisuraCautelareCumuloModel lMisMod = (MisuraCautelareCumuloModel) ItM.next();
						if (lMisMod != null && lMisMod.getIdMisuraCautelareCumulo() != null) {
							CalendarModel lCalMod = new CalendarModel();

							lCalMod.setNumAnni(lMisMod.getNumAnni());
							lCalMod.setNumMesi(lMisMod.getNumMesi());
							lCalMod.setNumGiorni(lMisMod.getNumGiorni());

							lTotPre = lCalUtil.sommaGiornieValute(lTotPre, lCalMod);
						}
					}

					RiepilogoPresoffertoCumuloModel lRiepilogo = new RiepilogoPresoffertoCumuloModel();

					lRiepilogo.setTotAnniPresofferto(new BigDecimal(lTotPre.getNumAnni()));
					lRiepilogo.setTotMesiPresofferto(new BigDecimal(lTotPre.getNumMesi()));
					lRiepilogo.setTotGiorniPresofferto(new BigDecimal(lTotPre.getNumGiorni()));

					TreeModel lTreeRiepilogo = new TreeModel(lRiepilogo);

					lTreeIstruMod.add(lTreeRiepilogo);

				} // Chiude VecMisCau

				// ------------ RIEPILOGO dei BENEFICI nell'ambito di una Istruttoria_Cumulo
				// ------------------
				if (VecBenefici != null && VecBenefici.size() > 0) {
					// siesLogger.debug("---XXXZ--- Totali Benefici = "+VecBenefici.size());
					Iterator ItB = VecBenefici.iterator();
					while (ItB.hasNext()) {
						// scarto le revoche, i Benefici Revocati e gli Indulti sono trattati a parte
						BeneficioCumuloModel lBenMod = (BeneficioCumuloModel) ItB.next();
						if (lBenMod != null && lBenMod.getIdBeneficioCumulo() != null) {
							if (lBenMod.getCodTipoBeneficio().compareTo("03") == 0) // Indulto
							{
								// Viene Trattato più avanti
							} else if (lBenMod.getCodNaturaBeneficio().compareTo("C") == 0
									&& lBenMod.getTitIdTitoloCumulatoCollegato() != null) // Beneficio
																							// revocato
							{
								ItB.remove();
							} else if (lBenMod.getCodNaturaBeneficio().compareTo("R") == 0) // Revoca
							{
								ItB.remove();
							} else {
								// Scrivo il Riepilogo delle SOSPENSIONI, NON_MENZIONI, e poi le Rimuovo dal
								// vector
								lBenMod.calcolaStringaReclusioneCumulo();
								lBenMod.calcolaStringaArrestoCumulo();

								TreeModel lTreeBenMod = new TreeModel(lBenMod);
								lTreeIstruMod.add(lTreeBenMod);

								ItB.remove();
							}

						}
					}

					// Nel 'VecBenefici' rimangono ora solamente Gli INDULTI e le REVOCHE INDULTO
					// siesLogger.debug("---XXXZ--- Dopo Remove, Totali Benefici Rimasti size =
					// "+VecBenefici.size());

				} // Chiude if(VecBenefici > 0)

				if (VecBenefici != null && VecBenefici.size() > 0) {
					DecodificheModel lModelVec = null;
					Iterator Ite1 = lVec.iterator(); // lVec contiene tutti i DPR
					while (Ite1.hasNext()) {
						lModelVec = (DecodificheModel) Ite1.next();
						if (lModelVec.getCode() != null && !lModelVec.getCode().equals("-")) {
							this.RiepilogoGeneraleIndulto(lTreeIstruMod, VecBenefici, lModelVec.getCode());
						}
					}
				}

				// ------------ RIEPILOGO della PENA_COMPLESSIVA nell'ambito di una Istruttoria_Cumulo
				// ------------------
				if (VecPenaCompl != null && VecPenaCompl.size() > 0) {
					// siesLogger.debug("---XXXZ--- Totali PenaComplessiva = "+VecPenaCompl.size());
					RiepilogoPenaComplessivaCumuloModel lRiepPCModel = new RiepilogoPenaComplessivaCumuloModel();
					CalendarModel lTotaleArresti = new CalendarModel();
					CalendarModel lTotaleReclusioni = new CalendarModel();

					lRiepPCModel.setTotalePenaComplessiva(VecPenaCompl);

					// Trova se c'è lergastolo tra tutte le pene complessive dell'istruttoria
					lRiepPCModel.SeErgastolo();

					// Calcolo dei Totali ARRESTO e Amm, RECLUSIONE e Multa di Tutte le PeneComplessive
					// dell'Istruttoria
					lTotaleArresti = lRiepPCModel.getTotaleArrestiPenaCumulo();
					lTotaleReclusioni = lRiepPCModel.getTotaleReclusioniPenaCumulo();

					// riempio il model di stampa
					lRiepPCModel.setNumAnniReclusione(new BigDecimal(lTotaleReclusioni.getNumAnni()));
					lRiepPCModel.setNumMesiReclusione(new BigDecimal(lTotaleReclusioni.getNumMesi()));
					lRiepPCModel.setNumGiorniReclusione(new BigDecimal(lTotaleReclusioni.getNumGiorni()));
					lRiepPCModel.setImportoMulta(new BigDecimal(lTotaleReclusioni.getImportoMulta()));

					lRiepPCModel.setNumAnniArresto(new BigDecimal(lTotaleArresti.getNumAnni()));
					lRiepPCModel.setNumMesiArresto(new BigDecimal(lTotaleArresti.getNumMesi()));
					lRiepPCModel.setNumGiorniArresto(new BigDecimal(lTotaleArresti.getNumGiorni()));
					lRiepPCModel.setImportoAmmenda(new BigDecimal(lTotaleArresti.getImportoAmmenda()));

					lRiepPCModel.calcolaStringaArrestoCum();
					lRiepPCModel.calcolaStringaReclusioneCum();

					TreeModel lTreeRiepilogoPenCompl = new TreeModel(lRiepPCModel);
					lTreeIstruMod.add(lTreeRiepilogoPenCompl);
				}

				// --------------------------------------------

			} // Chiude if((aListaTitoli != null)

			siesLogger.debug("add di TUTTO Il NODO ISTRUTTORIA .... ");
			lTreeRoot.add(lTreeIstruMod);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaCumuloController.prelevaDatiComunicazioniCumulo: " + daoEx);
		} catch (Exception e) {
			siesLogger.error("StampaCumuloController - -------> Exception: " + e, e);
			throw new F3BException("StampaCumuloController.prelevaDatiComunicazioniCumulo: " + e);
		} finally {
			cleanup(lSogDao);

			cleanup(lConn);
		}

		return lTreeRoot;

	} // Chiude prelevaDatiComunicazioniCumulo()

	/**
	 * @param lTreeIstruMod
	 * @param VecBenefici
	 * @param CodDpr
	 * @throws F3BException
	 */
	public void RiepilogoGeneraleIndulto(TreeModel lTreeIstruMod, Vector<BeneficioCumuloModel> VecBenefici,
			String CodDpr) throws F3BException {

		String Dprsi = "";
		Vector<BeneficioCumuloModel> lInduC = new Vector<>();
		Vector<BeneficioCumuloModel> lInduR = new Vector<>();

		CalendarModel lArrConcessi = new CalendarModel();
		CalendarModel lArrRevocati = new CalendarModel();

		CalendarModel lReclConcessi = new CalendarModel();
		CalendarModel lReclRevocati = new CalendarModel();

		BeneficioCumuloModel lBenMod = null;
		BeneficioCumuloModel lIndulto = null;

		// cerco gli indulti per codice DPR
		Iterator ItBB = VecBenefici.iterator();
		while (ItBB.hasNext()) {
			lBenMod = (BeneficioCumuloModel) ItBB.next();
			if (lBenMod != null && lBenMod.getIdBeneficioCumulo() != null
					&& lBenMod.getCodDpr().compareTo(CodDpr) == 0) {
				lIndulto = new BeneficioCumuloModel(lBenMod);
				// siesLogger.debug("---XXXZ--- Indulto preso in esame = "+lIndulto);

				Dprsi = "SI";
				if (lIndulto.getCodNaturaBeneficio().equals("C")) // Indulto Concesso
					lInduC.add(lIndulto);
				else if (lIndulto.getCodNaturaBeneficio().equals("R")) // Revoca Indulto
					lInduR.add(lIndulto);
			}
		}

		if (Dprsi.compareTo("SI") == 0) {
			CalendarUtil lCalUtilArr = new CalendarUtil();
			CalendarModel lTotArr = new CalendarModel();

			CalendarUtil lCalUtilRecl = new CalendarUtil();
			CalendarModel lTotRecl = new CalendarModel();

			// calcolo il totale Concesso
			if (lInduC != null && lInduC.size() > 0) {
				lIndulto.setTotBeneficiCumulo(lInduC);

				lArrConcessi = lIndulto.getBeneficiArrestiCumulo("C");
				lReclConcessi = lIndulto.getBeneficiReclusioneCumulo("C");
			}

			// calcolo il totale Revocato
			if (lInduR != null && lInduR.size() > 0) {
				lIndulto.setTotBeneficiCumulo(lInduR);

				lArrRevocati = lIndulto.getBeneficiArrestiCumulo("R");
				lReclRevocati = lIndulto.getBeneficiReclusioneCumulo("R");

				// il RIEPILOGO INDULTI è la DIFFERENZA tra CONCESSI e REVOCATI
				// Arresto e Ammenda
				lTotArr = lCalUtilArr.sottraiGiorniValuteNew(lArrConcessi, lArrRevocati);
				// Reclusione e Multa
				lTotRecl = lCalUtilRecl.sottraiGiorniValuteNew(lReclConcessi, lReclRevocati);
			} else {
				// SE non Esistono Quantum di Revoca, il RIEPILOGO INDULTI corrisponde ai CONCESSI
				// Arresto e Ammenda
				lTotArr = new CalendarModel(lArrConcessi);
				// Reclusione e Multa
				lTotRecl = new CalendarModel(lReclConcessi);
			}

			// Preparo il Model di Stampa
			lIndulto.setCodNaturaBeneficio("C");
			lIndulto.setDescrNaturaBeneficio("Concesso");

			lIndulto.setNumAnniReclusione(new BigDecimal(lTotRecl.getNumAnni()));
			lIndulto.setNumMesiReclusione(new BigDecimal(lTotRecl.getNumMesi()));
			lIndulto.setNumGiorniReclusione(new BigDecimal(lTotRecl.getNumGiorni()));
			lIndulto.setImportoMulta(new BigDecimal(lTotRecl.getImportoMulta()));

			lIndulto.setNumAnniArresto(new BigDecimal(lTotArr.getNumAnni()));
			lIndulto.setNumMesiArresto(new BigDecimal(lTotArr.getNumMesi()));
			lIndulto.setNumGiorniArresto(new BigDecimal(lTotArr.getNumGiorni()));
			lIndulto.setImportoAmmenda(new BigDecimal(lTotArr.getImportoAmmenda()));

			lIndulto.calcolaStringaArrestoCumulo();
			lIndulto.calcolaStringaReclusioneCumulo();

			TreeModel lTreeRiepIndu = new TreeModel(lIndulto);
			lTreeIstruMod.add(lTreeRiepIndu);

		}

	} // Chiude preparaIndulti()

	/**
	 * Crea la root del Documento Prospetto
	 *
	 * @param aUtenteMod
	 * @return XModel
	 */
	public XModel createRootX(UfficioModel lUfficioMod, UtenteModel aUtenteModel) throws F3BException {
		XModel lStampa = new XModel();

		String descrTipoUff = lUfficioMod.getDescrTipoUfficio().toUpperCase();
		lStampa.setUfficio(lUfficioMod.getDescrComune().toUpperCase());
		lStampa.setTipoUfficio(descrTipoUff.toUpperCase());
		lStampa.setDataElaborazione(DateUtils.getSysDate());

		if (aUtenteModel != null && aUtenteModel.getUfficioUtente() != null) {
			UfficioModel lUffMod = aUtenteModel.getUfficioUtente();

			lStampa.setCap(lUffMod.getCap());
			lStampa.setFax(lUffMod.getFax());
			lStampa.setIndirizzo(lUffMod.getIndirizzo());
			lStampa.setTelefono(lUffMod.getTelefono());
			lStampa.setEMail(lUffMod.getEMail());
		}

		if (descrTipoUff != null) {
			if (descrTipoUff.indexOf("PRESSO") > 1) {
				lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
				lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
			}
		}

		if (descrTipoUff.indexOf("GENERALE") > 0) {// GDV modifica
													// lStampa.setFirmatario("Il Sostituto Procuratore
													// Generale");
			lStampa.setFirmatario("Il Procuratore Generale");
		} else {
			lStampa.setFirmatario("Il Pubblico Ministero");
		}

		return lStampa;

	} // Chiude createRootX()

	/**
	 * Metodo per aggiungere i dati Analitici legati al Titolo Cumulato
	 *
	 * @param lConn
	 * @param lKeyTitolo
	 * @param lTreeTitoloMod
	 * @param TipoProspetto
	 *            : 1 = Prospetto Titoli Coinvolti; 2 = Prospetto Proposta
	 * @throws F3BException
	 */
	protected void appendDatiAnalitici(Connection lConn, TitoloCumulatoModel aTitolo,
			TreeModel lTreeTitoloMod, int TipoProspetto) throws F3BException {

		PenaComplessivaCumuloSqlDAO lPenSqlDao = null;
		SanzioneSostitutivaCumuloSqlDAO lSanSqlDao = null;
		ContinuazioneCumuloSqlDAO lContSqlDAO = null;
		PenaAccessoriaCumuloSqlDAO lPenAccSqlDao = null;
		MisuraSicurezzaCumuloSqlDAO lMisSicSqlDao = null;
		MisuraCautelareCumuloSqlDAO lMisCauSqlDao = null;
		BeneficioCumuloSqlDAO lBenSqlDao = null;
		PenaRideterminataCumuloSqlDAO lPenaRidSqlDao = null; // 20/04/2020

		SanzioneSostitutivaCumuloModel lSanSostMod = null;

		Vector<ContinuazioneCumuloModel> lContinuazioni = null;
		Vector<ReatoCircostanzaCumuloModel> lReatiCum = null;
		Vector<CircostanzaCumuloModel> lCircoCum = null;

		try {
			siesLogger.debug("--XX-- AppendDatiAnalitici ");

			// 20/04/2020 Ticket#20200220018 - Valorizzazione degli eventuali quantum di isolamento diurno -
			// ergastolo
			lPenaRidSqlDao = new PenaRideterminataCumuloSqlDAO(lConn);
			lPenaRidSqlDao.ricercaPenaRideterminataCumulByIdIstruttoria(aTitolo.getIstrIdIstruttoriaCumulo());
			lPenaRidSqlDao.start();

			String lCodTipoPenaDetentiva = "-";
			BigDecimal lNumAnniIsolamentoDiurno = null;
			BigDecimal lNumMesiIsolamentoDiurno = null;
			BigDecimal lNumGiorniIsolamentoDiurno = null;

			while (lPenaRidSqlDao.next()) {
				PenaRideterminataCumuloModel lPenaRidMod = (PenaRideterminataCumuloModel) lPenaRidSqlDao
						.getModel();

				if (lPenaRidMod.getCodTipoPenaDetentiva() != null
						&& lPenaRidMod.getCodTipoPenaDetentiva().compareTo(lCodTipoPenaDetentiva) > 0)
					lCodTipoPenaDetentiva = lPenaRidMod.getCodTipoPenaDetentiva();
				if ("04".equals(lCodTipoPenaDetentiva)) {
					lNumAnniIsolamentoDiurno = lPenaRidMod.getNumAnniIsolamentoDiurno();
					lNumMesiIsolamentoDiurno = lPenaRidMod.getNumMesiIsolamentoDiurno();
					lNumGiorniIsolamentoDiurno = lPenaRidMod.getNumGiorniIsolamentoDiurno();
				}
			}

			IstruttoriaCumuloController lIstrCtrl = new IstruttoriaCumuloController();
			BeneficioCumuloModel lBeneficioSosp = lIstrCtrl.isPenaSospesa(aTitolo.getIdTitoloCumulato(),
					lConn);
			boolean isPenaSospesa = false;
			boolean isPenaInteramenteSospesa = false;
			boolean isPenaDetentivaSospesa = false;
			boolean isPenaInContinuazione = false;

			if (lBeneficioSosp != null && !lBeneficioSosp.getIsRevocato()) {
				siesLogger.debug("Presente Sospensione condizionale non revocata");

				isPenaSospesa = true;

				if ("02".equals(lBeneficioSosp.getCodSottotipoBeneficio()))
					isPenaDetentivaSospesa = true;
				else
					isPenaInteramenteSospesa = true;
			}

			isPenaInContinuazione = lIstrCtrl.isPenaInContinuazione(aTitolo.getIdTitoloCumulato(), lConn);
			siesLogger.debug("isPenaInContinuazione = " + isPenaInContinuazione);

			// ========================================================================
			// Pena Complessiva Cumulo
			// ========================================================================
			lPenSqlDao = new PenaComplessivaCumuloSqlDAO(lConn);
			lPenSqlDao.ricercaPenaComplessivaCumuloByIdTitolo(aTitolo.getIdTitoloCumulato());
			PenaComplessivaCumuloModel lPenaComplMod = (PenaComplessivaCumuloModel) lPenSqlDao
					.getModelByKey();
			if (lPenaComplMod != null && lPenaComplMod.getIdPenaComplessivaCum() != null) {
				if (lPenaComplMod.getFlagStato().compareTo("C") != 0) {
					// Aggiungo la Pena_Complessiva al Vettore 'VecPenaCompl' per i Riepiloghi finali

					// Test per pena sospesa
					if (isPenaDetentivaSospesa) {
						// Procedo ad azzerare i quantum, lascio solo la pecuniaria

						PenaComplessivaCumuloModel lPCperCalcolo = new PenaComplessivaCumuloModel(
								lPenaComplMod);
						// Reclusione
						lPCperCalcolo.setNumGiorniReclusione(null);
						lPCperCalcolo.setNumMesiReclusione(null);
						lPCperCalcolo.setNumAnniReclusione(null);
						// Arresto
						lPCperCalcolo.setNumGiorniArresto(null);
						lPCperCalcolo.setNumMesiArresto(null);
						lPCperCalcolo.setNumAnniArresto(null);

						aCalcoloPenaModel.addPenaComplessiva(lPCperCalcolo);
					} else if (isPenaInteramenteSospesa) {
						// non aggiungo la pena al model
					} else if (isPenaInContinuazione) {
						// La pena è in continuazione. Esiste un altro titolo la cui pena
						// è dichiarata in continuazione con la pena corrente e il tipo
						// di continuazione è R (Pena Complessiva ritenuta la continuazione)
						// quindi la pena su tale titolo già ingloba la pena del titolo corrente
						// Non aggiungo la pena al model altrimenti verrebbe conteggiata 2 volte
						siesLogger.debug("NON carico la pena = " + lPenaComplMod);
					} else {
						aCalcoloPenaModel.addPenaComplessiva(lPenaComplMod);
					}

					VecPenaCompl.add(lPenaComplMod);

					// aCalcoloPenaModel.addPenaComplessiva(lPenaComplMod);

					// Stringa Arresto - Reclusione
					lPenaComplMod.calcolaStringaReclusioneCum();
					lPenaComplMod.calcolaStringaArrestoCum();
					lPenaComplMod.calcolaStringaIsolamentoCum();

					TreeModel lTreePenaComplMod = new TreeModel(lPenaComplMod);

					// Sanzione Sostitutiva Cumulo
					lSanSqlDao = new SanzioneSostitutivaCumuloSqlDAO(lConn);
					lSanSqlDao.ricercaSanzioneSostitutivaByIdPenaComplessivaCum(
							lPenaComplMod.getIdPenaComplessivaCum());
					lSanSostMod = (SanzioneSostitutivaCumuloModel) lSanSqlDao.getModelByKey();

					if (lSanSostMod != null && lSanSostMod.getIdSanzioneSostitutivaCum() != null) {
						if (lSanSostMod.getFlagStato().compareTo("C") != 0) {

							aCalcoloPenaModel.addSanzioneSost(lSanSostMod);
							lIstrCtrl.isSSRevocata(aTitolo.getIstrIdIstruttoriaCumulo(), lSanSostMod, lConn);

							// lSanSostMod.calcolaStringaSanzione();
							lSanSostMod.calcolaStringaSanzionePerStampaProspetto();
							lSanSostMod.calcolaPeriodoSanzione();

							lTreePenaComplMod.add(new TreeModel(lSanSostMod));
						}
					}

					// Continuazioni Cumulo
					lContSqlDAO = new ContinuazioneCumuloSqlDAO(lConn);
					lContSqlDAO.ricercaContinuazioneByIdPenaComplessivaCum(
							lPenaComplMod.getIdPenaComplessivaCum());
					lContinuazioni = new Vector<ContinuazioneCumuloModel>(lContSqlDAO.getModels());
					if (lContinuazioni != null && lContinuazioni.size() > 0) {
						Iterator lItxCont = lContinuazioni.iterator();
						while (lItxCont.hasNext()) {
							ContinuazioneCumuloModel lContMod = (ContinuazioneCumuloModel) lItxCont.next();
							if (lContMod != null && lContMod.getIdContinuazioneCum() != null)
								if (lContMod.getFlagStato().compareTo("C") != 0)
									lTreePenaComplMod.add(new TreeModel(lContMod));
						}

					}

					// il TreeModel della penaComplessiva, si porta dietro anche SanzioneSostitutiva e
					// Continuazioni
					lTreeTitoloMod.add(lTreePenaComplMod);
				}
			}

			// ========================================================================
			// Cerco i Reati (Norma principale + Norme circostanze)
			// ========================================================================
			IReatoCumulo lReaCtr = SIEPLookupRemote.getReatoCumuloRemote();
			lReatiCum = lReaCtr.ExRicercaReatoCircostanzaCumByTitoloCum(aTitolo.getIdTitoloCumulato());

			// Reati Cumulo (Norma principale + Norme circostanze)

			if (lReatiCum != null && lReatiCum.size() > 0) {
				Vector lReatiPerCont = new Vector();
				Iterator lItxRea = lReatiCum.iterator();
				while (lItxRea.hasNext()) {

					ReatoCircostanzaCumuloModel lReatoCircoModel = new ReatoCircostanzaCumuloModel(
							(ReatoCircostanzaCumuloModel) lItxRea.next());
					TreeModel lTreeReatoMod = new TreeModel(lReatoCircoModel.getReatoCum());

					// Creo Vettore dei Reati per le Continuazioni
					ReatoCumuloModel lReatoCum = lReatoCircoModel.getReatoCum();
					if (lReatoCum != null) {
						lReatoCum.calcolaPenaReatoCum();
						lReatoCum.calcolaStringaPenaPecuniariaCum();
					}

					lReatiPerCont.add(lReatoCum);

					int count = 0; // Circostanze
					while (count < lReatoCircoModel.getCircostanzeCum().length) {
						TreeModel lTreeCirc = new TreeModel(lReatoCircoModel.getCircostanzeCum()[count]);
						lTreeReatoMod.add(lTreeCirc);
						count++;
					}

					lTreeTitoloMod.add(lTreeReatoMod);

				}

				// Continuazione Reati Cumulo
				ReatoContinuazioneCumuloController lRCtrl = new ReatoContinuazioneCumuloController();
				Hashtable lTable = lRCtrl.getTableContinuazioni(lReatiPerCont);

				Vector lContinuazioniReati = lRCtrl.getContinuazioniReati(lTable);
				if (lContinuazioniReati != null && lContinuazioniReati.size() > 0) {
					Iterator lContItx = lContinuazioniReati.iterator();
					while (lContItx.hasNext()) {
						ContinuazioneReatiCumuloModel lContReaCum = (ContinuazioneReatiCumuloModel) lContItx
								.next();
						lTreeTitoloMod.add(new TreeModel(lContReaCum));
					}
				}
			}

			// ------------------------------------------------------------------------------
			// Le Circostanze Aggravanti (tabella Circostanze_Cumulo) sono previste in STEP2
			// --------------------------------------------------------------------------------
			// MEV 26 Cumulo Step 2
			ICircostanzaCumulo lCirCtr = SIEPLookupRemote.getCircostanzaCumuloRemote();
			lCircoCum = lCirCtr.ExRicercaCircostanzaCumulobyTitolo(aTitolo.getIdTitoloCumulato());

			if (lCircoCum != null && lCircoCum.size() > 0) {
				Iterator lItxC = lCircoCum.iterator();
				while (lItxC.hasNext()) {
					// Add Circostanza_Cumulo
					CircostanzaCumuloModel lCircoCumModel = new CircostanzaCumuloModel(
							(CircostanzaCumuloModel) lItxC.next());
					TreeModel lTreeCircMod = new TreeModel(lCircoCumModel);
					lTreeTitoloMod.add(lTreeCircMod);
				}

			}

			// END MEV 26
			// ------------------ FINE REATI

			// Pena Accessoria
			lPenAccSqlDao = new PenaAccessoriaCumuloSqlDAO(lConn);
			PenaAccessoriaCumuloModel lPAccCumMod = new PenaAccessoriaCumuloModel();
			lPAccCumMod.setTitIdTitoloCumulato(aTitolo.getIdTitoloCumulato());
			lPenAccSqlDao.ricercaPenaAccessoriaCumulo(lPAccCumMod);
			Vector lPeneAccessorie = new Vector(lPenAccSqlDao.getModels());

			if (lPeneAccessorie != null && lPeneAccessorie.size() > 0) {
				Iterator lItxPA = lPeneAccessorie.iterator();
				while (lItxPA.hasNext()) {
					PenaAccessoriaCumuloModel lPen = (PenaAccessoriaCumuloModel) lItxPA.next();
					if (lPen != null && lPen.getIdPenaAccessoriaCumulo() != null) {
						if (lPen.getFlagStato().compareTo("C") != 0) {
							if (isPenaSospesa) {
								// Per non farla uscire nella sezione OSSERVA essendo sospensa
								lPen.setFlagDatiFinali("N");
							}
							TreeModel lTreePenAccMod = new TreeModel(lPen);
							lTreeTitoloMod.add(lTreePenAccMod);
						}
					}
				}
			}

			// Misure Sicurezza
			lMisSicSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			MisuraSicurezzaCumuloModel lMisSicMod = new MisuraSicurezzaCumuloModel();
			lMisSicMod.setTitIdTitoloCumulato(aTitolo.getIdTitoloCumulato());
			lMisSicSqlDao.ricercaMisuraSicurezzaCumulo(lMisSicMod);
			Vector lListaMisure = new Vector(lMisSicSqlDao.getModels());

			if (lListaMisure != null && lListaMisure.size() > 0) {
				Iterator lItxMS = lListaMisure.iterator();
				while (lItxMS.hasNext()) {
					MisuraSicurezzaCumuloModel lMis = (MisuraSicurezzaCumuloModel) lItxMS.next();
					if (lMis != null && lMis.getIdMisuraSicurezzaCumulo() != null) {
						if (lMis.getFlagStato().compareTo("C") != 0) {
							TreeModel lTreeMisSicMod = new TreeModel(lMis);
							lTreeTitoloMod.add(lTreeMisSicMod);
						}
					}
				}
			}

			// ========================================================================
			// Misure Cautelari
			// ========================================================================
			lMisCauSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);
			lMisCauSqlDao.ricercaMisuraCautelareCumuloByIdTitolo(aTitolo.getIdTitoloCumulato());
			Vector listaMisureCau = new Vector(lMisCauSqlDao.getModels());

			if (listaMisureCau != null && listaMisureCau.size() > 0) {
				Iterator ItxMC = listaMisureCau.iterator();
				while (ItxMC.hasNext()) {
					MisuraCautelareCumuloModel lMisCauMod = (MisuraCautelareCumuloModel) ItxMC.next();

					if (lMisCauMod != null && lMisCauMod.getIdMisuraCautelareCumulo() != null) {
						if (lMisCauMod.getFlagStato().compareTo("C") != 0) {

							if (isPenaSospesa) {
								lMisCauMod.setIsPenaSospesa("S");
							} else
								lMisCauMod.setIsPenaSospesa("N");

							TreeModel lTreeMisCauMod = new TreeModel(lMisCauMod);
							lTreeTitoloMod.add(lTreeMisCauMod);

							if (lSanSostMod != null && !lSanSostMod.getIsRevocata())
								lMisCauMod.setIsPenaSospesa("S"); // Serve a non farlo uscire sulla stampa

							if (!isPenaSospesa && (lSanSostMod == null || lSanSostMod.getIsRevocata())) {
								// In caso di pena sospesa non calcolo il presofferto
								aCalcoloPenaModel.addMisuraCautelare(lMisCauMod);
							}
							// Aggiungo tutti i PRESOFFERTI al Vettore 'VecMisCau' per i Riepiloghi finali
							VecMisCau.add(lMisCauMod);
						}
					}
				}
			}

			siesLogger.debug("--XX Benefici in sentenza ");
			// ========================================================================
			// Benefici Cumulo (tutti, sia CONCESSI che REVOCATI)
			// ========================================================================
			lBenSqlDao = new BeneficioCumuloSqlDAO(lConn);
			BeneficioCumuloModel lBenCumMod = new BeneficioCumuloModel();
			lBenCumMod.setTitIdTitoloCumulato(aTitolo.getIdTitoloCumulato());
			lBenSqlDao.ricercaBeneficioCumulo(lBenCumMod);
			Vector lBenefici = new Vector(lBenSqlDao.getModels());

			// MEV_70 : sul ciclo dei titoli per fare uscire BENEFICIO REVOCATO anche sotto la NON MENZIONE
			Boolean bRevoca = false; // <===

			if (lBenefici != null && lBenefici.size() > 0) {
				Iterator lItxB = lBenefici.iterator();
				while (lItxB.hasNext()) {
					BeneficioCumuloModel lBen = (BeneficioCumuloModel) lItxB.next();

					if (lBen != null && lBen.getIdBeneficioCumulo() != null) {

						if (lBen.getFlagStato().compareTo("C") != 0) {

							if (bRevoca && lBen.getCodTipoBeneficio().compareTo("02") == 0) { // <===
								lBen.setStringaRevoca("Beneficio Revocato"); // <===
							} // <===

							siesLogger.debug("BenInSentenza id = " + lBen.getIdBeneficioCumulo());
							if (lBeneficioSosp != null) {
								siesLogger.debug(
										"lBeneficioSosp id = " + lBeneficioSosp.getIdBeneficioCumulo());
								if (lBeneficioSosp.getIdBeneficioCumulo()
										.compareTo(lBen.getIdBeneficioCumulo()) == 0
										&& lBeneficioSosp.getIsRevocato()) {
									lBen.setStringaRevoca(lBeneficioSosp.getStringaRevoca());
									bRevoca = true; // <===
								} else if (!isPenaSospesa) {
									aCalcoloPenaModel.addBeneficio(lBen);
								}
							} else {
								if (lSanSostMod == null || lSanSostMod.getIsRevocata())
									aCalcoloPenaModel.addBeneficio(lBen);
								// lMisCauMod.setIsPenaSospesa("N");
							}

							lBen.calcolaStringaReclusioneCumulo();
							lBen.calcolaStringaArrestoCumulo();

							String StringaSentenzaRevoca = "";
							if (lBen.getCodNaturaBeneficio().equals("C")
									&& lBen.getTitIdTitoloCumulatoCollegato() != null) {
								StringaSentenzaRevoca = this.DatiSentenzaRevoca(lBen);
							}

							if (StringaSentenzaRevoca.compareTo("") != 0)
								lBen.setStringaRevoca(StringaSentenzaRevoca);

							TreeModel lTreeBenMod = new TreeModel(lBen);
							lTreeTitoloMod.add(lTreeBenMod);

							// Aggiungo tutti i BENEFICI al Vettore 'VecBenefici' per i Riepiloghi finali
							VecBenefici.add(lBen);
						}

					}

				}
			}

			// --------------------------------------------------------------------------------------
			// SOLO PER Stampa Prospetto Titoli: CALCOLO del Totale Pena Residua Espianda del SINGLO TITOLO
			// CUMULATO
			// ---------------------------------------------------------------------------------------

			// MEV_70 : cambiamo la parte di codice sottostante con il Nuovo Metodo preso dal CalcoloPena:
			// per bypassere il codice sotto, forzo TipoProspetto == 2
			TipoProspetto = 2;
			ResiduoPenaEspiandaTitoloCumuloModel lPenaResCum = new ResiduoPenaEspiandaTitoloCumuloModel();

			if (TipoProspetto == 1) {
				// Se Esiste SANZIONE_SOSTITUTIVA, La Pena Residua Espianda è la SANZIONE_SOSTITUTIVA!!!!
				// ( questa regola è stata ereditata dal precedente Prospetto del cumulo)

				// ResiduoPenaEspiandaTitoloCumuloModel lPenaResCum = new
				// ResiduoPenaEspiandaTitoloCumuloModel();

				if (lSanSostMod != null && lSanSostMod.getIdSanzioneSostitutivaCum() != null) {
					lSanSostMod.calcolaStringaSanzione();
					lSanSostMod.calcolaPeriodoSanzione();

					// ======== >> Riempio il Model di stampa lPenaResCum di tipo
					// ResiduoPenaEspiandaTitoloCumuloModel
					lPenaResCum = new ResiduoPenaEspiandaTitoloCumuloModel(lSanSostMod);

				} else {
					// Riempio il Model con la pena_Complessiva (Recl e mul)
					CalendarModel TotalePenaRecl = new CalendarModel();
					CalendarModel TotalePenaArr = new CalendarModel();
					if (lPenaComplMod != null) {
						if (lPenaComplMod.getImportoMulta() != null)
							TotalePenaRecl.setImportoMulta(lPenaComplMod.getImportoMulta().doubleValue());
						else
							TotalePenaRecl.setImportoMulta(0);

						if (lPenaComplMod.getNumAnniReclusione() != null)
							TotalePenaRecl.setNumAnni(lPenaComplMod.getNumAnniReclusione());
						else
							TotalePenaRecl.setNumAnni(new BigDecimal(0));

						if (lPenaComplMod.getNumMesiReclusione() != null)
							TotalePenaRecl.setNumMesi(lPenaComplMod.getNumMesiReclusione());
						else
							TotalePenaRecl.setNumMesi(new BigDecimal(0));

						if (lPenaComplMod.getNumGiorniReclusione() != null)
							TotalePenaRecl.setNumGiorni(lPenaComplMod.getNumGiorniReclusione());
						else
							TotalePenaRecl.setNumGiorni(new BigDecimal(0));

						// Riempio il Model con la pena_Complessiva (Arrest e Amm)

						if (lPenaComplMod.getImportoAmmenda() != null)
							TotalePenaArr.setImportoAmmenda(lPenaComplMod.getImportoAmmenda().doubleValue());
						else
							TotalePenaArr.setImportoAmmenda(0);

						if (lPenaComplMod.getNumAnniArresto() != null)
							TotalePenaArr.setNumAnni(lPenaComplMod.getNumAnniArresto());
						else
							TotalePenaArr.setNumAnni(new BigDecimal(0));

						if (lPenaComplMod.getNumMesiArresto() != null)
							TotalePenaArr.setNumMesi(lPenaComplMod.getNumMesiArresto());
						else
							TotalePenaArr.setNumMesi(new BigDecimal(0));

						if (lPenaComplMod.getNumGiorniArresto() != null)
							TotalePenaArr.setNumGiorni(lPenaComplMod.getNumGiorniArresto());
						else
							TotalePenaArr.setNumGiorni(new BigDecimal(0));
					} else {
						siesLogger.debug("--XX-- Pena Complessiva assente per il titolo con id = "
								+ aTitolo.getIdTitoloCumulato());
					}
					siesLogger.debug("--XX-- TotalePenaRecl = " + TotalePenaRecl);
					siesLogger.debug("--XX-- TotalePenaArr = " + TotalePenaArr);

					// Scorro il Vettore dei Benefici...
					siesLogger.debug("--XX-- Scorro il Vettore dei Benefici...");
					if (lBenefici != null && lBenefici.size() > 0) {
						Iterator lItxB = lBenefici.iterator();
						while (lItxB.hasNext()) {
							// ...Rimuovo dal Vector le Revoche, i Benefici Revocatii, e i Benefici che non
							// hanno i Quantum di PENA
							BeneficioCumuloModel lBen = (BeneficioCumuloModel) lItxB.next();
							if (lBen != null && lBen.getIdBeneficioCumulo() != null) {
								if (lBen.getCodNaturaBeneficio().compareTo("C") != 0
										|| lBen.getCodTipoBeneficio().compareTo("03") != 0
										|| lBen.getTitIdTitoloCumulatoCollegato() != null) {
									lItxB.remove();
								}
							}
						}
					}

					CalendarUtil lCalUtil = new CalendarUtil();
					CalendarModel ResiduoArr = null;
					CalendarModel ResiduoRecl = null;

					// Se nel Vector 'lBenefici' ci sono Benefici da sottrarre alla Pena .....
					if (lBenefici != null && lBenefici.size() > 0) {
						// calcolo il totale Benefici Concesso
						BeneficioCumuloModel lMod = new BeneficioCumuloModel();
						CalendarModel ArrIndultati = new CalendarModel();
						CalendarModel ReclIndultati = new CalendarModel();

						lMod.setTotBeneficiCumulo(lBenefici);

						ArrIndultati = lMod.getBeneficiArrestiCumulo("C");
						ReclIndultati = lMod.getBeneficiReclusioneCumulo("C");

						lCalUtil = new CalendarUtil();
						ResiduoArr = new CalendarModel();
						ResiduoRecl = new CalendarModel();

						// Pena Residua = Pena Complessiva - Benefici (Reclusione Multa)
						ResiduoRecl = lCalUtil.sottraiGiorniValuteNew(TotalePenaRecl, ReclIndultati);

						// Pena Residua = Pena Complessiva - Benefici (Arresto Ammenda)
						ResiduoArr = lCalUtil.sottraiGiorniValuteNew(TotalePenaArr, ArrIndultati);

					} else {
						// Se NON CI SONO BENEFICI da Sottrarre, Pena Residua = Pena Complessiva
						siesLogger.debug(" --XX--  NON CI SONO BENEFICI...");
						ResiduoArr = new CalendarModel(TotalePenaArr);
						ResiduoRecl = new CalendarModel(TotalePenaRecl);
					}

					// Residuo pena DOPO eventuale sottrazione degli INDULTI
					siesLogger.debug(
							"---XXXZ--- PenaComplessiva Recl. Mul. - (Meno) Benefici = Pena Residua  = "
									+ ResiduoRecl); // OK
					siesLogger.debug(
							"---XXXZ--- PenaComplessiva Arr. Amm.  - (Meno) Benefici = Pena Residua  = "
									+ ResiduoArr);

					// PRESOFFERTO
					CalendarModel lTotPresoff = new CalendarModel();
					if (listaMisureCau != null && listaMisureCau.size() > 0) {
						siesLogger.debug(
								"---XXXZ--- Totali Misure Cautelari, size  = " + listaMisureCau.size());
						lCalUtil = new CalendarUtil();

						// Preparo il Totale con tutto il presofferto e lo metto in 'lTotPresoff'
						Iterator ItM = listaMisureCau.iterator();
						while (ItM.hasNext()) {
							MisuraCautelareCumuloModel lMisMod = (MisuraCautelareCumuloModel) ItM.next();
							if (lMisMod != null && lMisMod.getIdMisuraCautelareCumulo() != null) {
								CalendarModel lCalMod = new CalendarModel();

								lCalMod.setNumAnni(lMisMod.getNumAnni());
								lCalMod.setNumMesi(lMisMod.getNumMesi());
								lCalMod.setNumGiorni(lMisMod.getNumGiorni());

								lTotPresoff = lCalUtil.sommaGiornieValute(lTotPresoff, lCalMod);
							}
						}
					}
					siesLogger.debug("lTotPresoff = " + lTotPresoff);

					// Per avere il RESIDUO PENA ESPIANDA :
					// Se esiste il Presofferto, lo Sottraggo al Residuo Reclusione;
					// Se avanza ancora qualcosa di Presofferto, lo sottraggo al Residuo Arresto.
					//
					// Se NON esiste Residuo Reclusione, il Presofferto viene sottratto direttamente al
					// residuo Arresto;
					//
					if (listaMisureCau != null && listaMisureCau.size() > 0) {
						if (!lCalUtil.isZero(ResiduoRecl)) // controlla che Residuo Recl esiste
						{

							siesLogger.debug("Sottraggo il prodsofferto dalla Reclusione");
							ResiduoRecl = lCalUtil.sottraiGiorniValuteNew(ResiduoRecl, lTotPresoff);

							if (!lCalUtil.isPositiveTime(ResiduoRecl)) // Controlla se la differenza è
																		// Positiva o negativa
							{
								siesLogger.debug("Reclusione negativa: sotraggo da arresto. " + ResiduoRecl);
								siesLogger.debug("ResiduoArr: " + ResiduoArr);
								ResiduoRecl = lCalUtil.abs(ResiduoRecl); // tolgo il segno meno

								if (!lCalUtil.isZero(ResiduoArr)) // controlla che Residuo Arr esiste
								{
									// Se avanza ancora presofferto lo sottraggo al Residuo Arr
									ResiduoArr = lCalUtil.sottraiGiorniValuteNew(ResiduoArr, ResiduoRecl);
								}
							}
						} else if (!lCalUtil.isZero(ResiduoArr)) {
							siesLogger
									.debug("Residuo reclusione nullo. Sotraggo il presofferto dagli arresti");
							// Se Non c'è Residuo Recl, il Presofferto viene sottratto al Residuo Arr
							ResiduoArr = lCalUtil.sottraiGiorniValuteNew(ResiduoArr, lTotPresoff);
						}
					}

					siesLogger.debug("Dopo scomputo presofferto");
					siesLogger.debug("ResiduoRecl = " + ResiduoRecl);
					siesLogger.debug("ResiduoArr  = " + ResiduoArr);

					// ======== >> Riempio il Model di stampa lPenaResCum di tipo
					// ResiduoPenaEspiandaTitoloCumuloModel
					lPenaResCum = new ResiduoPenaEspiandaTitoloCumuloModel();

					if (lPenaComplMod != null) {
						lPenaResCum.setCodTipoPenaDetentiva(lPenaComplMod.getCodTipoPenaDetentiva());
						lPenaResCum.setDescrTipoPenaDetentiva(lPenaComplMod.getDescrTipoPenaDetentiva());
					}

					// 20/04/2020 Ticket#20200220018 - Valorizzazione di DescrTipoPenaDetentiva in base agli
					// eventuali quantum di isolamento diurno - ergastolo
					if ("04".equals(lPenaResCum.getCodTipoPenaDetentiva())
							&& (lNumAnniIsolamentoDiurno != null || lNumMesiIsolamentoDiurno != null
									|| lNumGiorniIsolamentoDiurno != null))
						lPenaResCum.setDescrTipoPenaDetentiva("Ergastolo con Isolamento Diurno per Anni "
								+ lNumAnniIsolamentoDiurno + " Mesi " + lNumMesiIsolamentoDiurno + " Giorni "
								+ lNumGiorniIsolamentoDiurno);

					if (!lCalUtil.isZero(ResiduoRecl)) {
						lPenaResCum.setNumAnniReclusione(new BigDecimal(ResiduoRecl.getNumAnni()));
						lPenaResCum.setNumMesiReclusione(new BigDecimal(ResiduoRecl.getNumMesi()));
						lPenaResCum.setNumGiorniReclusione(new BigDecimal(ResiduoRecl.getNumGiorni()));
					}

					if (new BigDecimal(ResiduoRecl.getImportoMulta()) != null)
						lPenaResCum.setImportoMulta(new BigDecimal(ResiduoRecl.getImportoMulta()));
					// else
					// lPenaResCum.setImportoMulta(new BigDecimal(0));

					if (!lCalUtil.isZero(ResiduoArr)) {
						lPenaResCum.setNumAnniArresto(new BigDecimal(ResiduoArr.getNumAnni()));
						lPenaResCum.setNumMesiArresto(new BigDecimal(ResiduoArr.getNumMesi()));
						lPenaResCum.setNumGiorniArresto(new BigDecimal(ResiduoArr.getNumGiorni()));
					}

					if (new BigDecimal(ResiduoArr.getImportoAmmenda()) != null)
						lPenaResCum.setImportoAmmenda(new BigDecimal(ResiduoArr.getImportoAmmenda()));
					// else
					// lPenaResCum.setImportoAmmenda(new BigDecimal(0));

					lPenaResCum.calcolaStringaArrestoCum();
					lPenaResCum.calcolaStringaReclusioneCum();

				} // chiude la else di if(lSanSostMod!=null &&
					// lSanSostMod.getIdSanzioneSostitutivaCum()!=null)

				TreeModel lTreePenaResEspianda = new TreeModel(lPenaResCum);
				lTreeTitoloMod.add(lTreePenaResEspianda);

			} // Chiude if(TipoProspetto == 1)
			else {
				siesLogger.debug(
						" --XX-- >>>>>>>>>>>   XXXXXX    NUOVO PEZZO di codice XXXXXX  <<<<<<<<<<<<<<< ");
				IIstruttoriaCumulo CtrlI = SIEPLookupRemote.getIstruttoriaCumuloRemote();
				CalcoloPenaCumuloModel bCalcoloPenaModel = new CalcoloPenaCumuloModel();
				bCalcoloPenaModel = CtrlI.ExCalcolaPenaCumuloByIstruttoria(
						aTitolo.getIstrIdIstruttoriaCumulo(), aTitolo.getIdTitoloCumulato(), false);

				PenaRideterminataCumuloModel lTotaleDaScontare = bCalcoloPenaModel
						.getPenaPrincipaleTotNetta();

				if (lSanSostMod != null && lSanSostMod.getIdSanzioneSostitutivaCum() != null) {
					lSanSostMod.calcolaStringaSanzione();
					lSanSostMod.calcolaPeriodoSanzione();

					// ======== >> Riempio il Model di stampa lPenaResCum di tipo
					// ResiduoPenaEspiandaTitoloCumuloModel
					lPenaResCum = new ResiduoPenaEspiandaTitoloCumuloModel(lSanSostMod);

				} else {

					// ======== >> Riempio il Model di stampa lPenaResCum di tipo
					// ResiduoPenaEspiandaTitoloCumuloModel
					lPenaResCum = new ResiduoPenaEspiandaTitoloCumuloModel();

					if (lPenaComplMod != null) {
						lPenaResCum.setCodTipoPenaDetentiva(lPenaComplMod.getCodTipoPenaDetentiva());
						lPenaResCum.setDescrTipoPenaDetentiva(lPenaComplMod.getDescrTipoPenaDetentiva());
					}

					// 20/04/2020 Ticket#20200220018 - Valorizzazione di DescrTipoPenaDetentiva in base agli
					// eventuali quantum di isolamento diurno - ergastolo
					if ("04".equals(lPenaResCum.getCodTipoPenaDetentiva())
							&& (lNumAnniIsolamentoDiurno != null || lNumMesiIsolamentoDiurno != null
									|| lNumGiorniIsolamentoDiurno != null))
						lPenaResCum.setDescrTipoPenaDetentiva("Ergastolo con Isolamento Diurno per Anni "
								+ lNumAnniIsolamentoDiurno + " Mesi " + lNumMesiIsolamentoDiurno + " Giorni "
								+ lNumGiorniIsolamentoDiurno);

					if (lTotaleDaScontare.getNumAnniReclusione() != null
							&& lTotaleDaScontare.getNumAnniReclusione().intValue() != 0)
						lPenaResCum.setNumAnniReclusione(lTotaleDaScontare.getNumAnniReclusione());

					if (lTotaleDaScontare.getNumMesiReclusione() != null
							&& lTotaleDaScontare.getNumMesiReclusione().intValue() != 0)
						lPenaResCum.setNumMesiReclusione(lTotaleDaScontare.getNumMesiReclusione());

					if (lTotaleDaScontare.getNumGiorniReclusione() != null
							&& lTotaleDaScontare.getNumGiorniReclusione().intValue() != 0)
						lPenaResCum.setNumGiorniReclusione(lTotaleDaScontare.getNumGiorniReclusione());
					//
					if (lTotaleDaScontare.getImportoMulta() != null
							&& lTotaleDaScontare.getImportoMulta().intValue() > 0)
						lPenaResCum.setImportoMulta(lTotaleDaScontare.getImportoMulta());
					//
					if (lTotaleDaScontare.getNumAnniArresto() != null
							&& lTotaleDaScontare.getNumAnniArresto().intValue() != 0)
						lPenaResCum.setNumAnniArresto(lTotaleDaScontare.getNumAnniArresto());

					if (lTotaleDaScontare.getNumMesiArresto() != null
							&& lTotaleDaScontare.getNumMesiArresto().intValue() != 0)
						lPenaResCum.setNumMesiArresto(lTotaleDaScontare.getNumMesiArresto());

					if (lTotaleDaScontare.getNumGiorniArresto() != null
							&& lTotaleDaScontare.getNumGiorniArresto().intValue() != 0)
						lPenaResCum.setNumGiorniArresto(lTotaleDaScontare.getNumGiorniArresto());
					//
					if (lTotaleDaScontare.getImportoAmmenda() != null
							&& lTotaleDaScontare.getImportoAmmenda().intValue() > 0)
						lPenaResCum.setImportoAmmenda(lTotaleDaScontare.getImportoAmmenda());

					lPenaResCum.calcolaStringaArrestoCum();
					lPenaResCum.calcolaStringaReclusioneCum();
				}

				siesLogger.debug(" --XX-- >>>>>>>>>>>   XXXXXX   lPenaResCum NUOVO XXXXXX  <<<<<<<<<<<<<<< ");
				TreeModel lTreePenaResEspianda = new TreeModel(lPenaResCum);
				lTreeTitoloMod.add(lTreePenaResEspianda);

			}
			// ----------------------------------------------------------------------------------------
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaController.appendDatiAnalitici " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: " + e, e);
			throw new F3BException("StampaController.appendDatiAnalitici: Eccezione Generica: " + e);
		} finally {
			cleanup(lPenSqlDao);
			cleanup(lSanSqlDao);
			cleanup(lContSqlDAO);
			cleanup(lPenAccSqlDao);
			cleanup(lMisSicSqlDao);
			cleanup(lMisCauSqlDao);
			cleanup(lBenSqlDao);
			cleanup(lPenaRidSqlDao);
		}

	} // Chiude appendDatiAnalitici()

	// =================

	/**
	 * Metodo per aggiungere Lo Stato Esecuzione (Con tutti i PROVVEDIMENTI) legati al Titolo Cumulato
	 *
	 * @param lConn
	 * @param lKeyTitolo
	 * @param lKeyIstruttoria
	 * @param lTreeTitoloMod
	 * @param TipoProspetto
	 *            : 1 = Prospetto Titoli Coinvolti; 2 = Prospetto Proposta
	 * @throws F3BException
	 */
	protected void appendStatoEsecuzioneTitoloCum(Connection lConn, BigDecimal lKeyIstru,
			BigDecimal lKeyTitolo, TreeModel lTreeTitoloMod, int TipoProspetto) throws F3BException {

		siesLogger.debug("--XX-- START appendStatoEsecuzioneTitoloCum - Id_Titolo_Cumulato = " + lKeyTitolo);

		StatoEsecTitoloCumulatoSqlDAO lStEsecSqlDao = null;
		ComputiCumuloSqlDAO lCompSqlDao = null;
		NotificaCumuloSqlDAO lNotCumSqlDao = null;
		IstitutoDetenzioneSqlDAO lIstSqlDao = null;

		LibAnticipataCumuloSqlDAO lLibAntCumSqlDao = null;
		PeriodoLibAntCumuloSqlDAO lPeriCumSqlDao = null;

		Vector<StatoEsecTitoloCumulatoModel> lVecStatoEsec = null;
		Vector<ComputiCumuloModel> lVecCompCum = null;
		Vector<NotificaCumuloModel> lVecNotifiche = null;
		try {
			lStEsecSqlDao = new StatoEsecTitoloCumulatoSqlDAO(lConn);
			lCompSqlDao = new ComputiCumuloSqlDAO(lConn);
			lNotCumSqlDao = new NotificaCumuloSqlDAO(lConn);
			lIstSqlDao = new IstitutoDetenzioneSqlDAO(lConn);

			lLibAntCumSqlDao = new LibAnticipataCumuloSqlDAO(lConn);
			lPeriCumSqlDao = new PeriodoLibAntCumuloSqlDAO(lConn);

			// Stato_Esecuzione_Titolo_Cumulato
			StatoEsecTitoloCumulatoModel lStEsecCumMod = null;
			ComputiCumuloModel lCompMod = null;
			NotificaCumuloModel lNotMod = null;
			IstitutoDetenzioneModel lIstMod = null;

			lStEsecSqlDao.ricercaStatoEsecTitoloCumulatoByIdTitolo(lKeyTitolo, null);
			lVecStatoEsec = new Vector<StatoEsecTitoloCumulatoModel>(lStEsecSqlDao.getModels());
			if (lVecStatoEsec != null && lVecStatoEsec.size() > 0) {
				// siesLogger.debug("--XX-- TRovato almeno 1 Stato_Esecuzione ");
				Iterator lItxSta = lVecStatoEsec.iterator();
				while (lItxSta.hasNext()) {
					lStEsecCumMod = (StatoEsecTitoloCumulatoModel) lItxSta.next();
					if (lStEsecCumMod != null && lStEsecCumMod.getIdStatoEsecTitoloCumulato() != null) {
						TreeModel lTreeStatoEsecCum = new TreeModel(lStEsecCumMod);

						// Computi_Cumulo ==================
						lCompSqlDao.ricercaComputiCumuloByIdStatoEsec(
								lStEsecCumMod.getIdStatoEsecTitoloCumulato());
						lVecCompCum = new Vector<ComputiCumuloModel>(lCompSqlDao.getModels());
						if (lVecCompCum != null && lVecCompCum.size() > 0) {
							Iterator lItxCmp = lVecCompCum.iterator();
							while (lItxCmp.hasNext()) {
								lCompMod = (ComputiCumuloModel) lItxCmp.next();
								if (lCompMod != null && lCompMod.getIdComputiCumulo() != null) {
									if (!lCompMod.isQuantumReclusioneZero())
										lCompMod.calcolaStringaReclusione();
									if (!lCompMod.isQuantumArrestoZero())
										lCompMod.calcolaStringaArresto();

									TreeModel lTreeCompMod = new TreeModel(lCompMod);

									// ESPIAZIONE PREGRESSA- Eventuale Istituto di Detenzione
									if (lCompMod.getIstDetIdIstitutoDetenzione() != null) {
										lIstSqlDao.ricercaIstitutoDetenzioneByKey(
												lCompMod.getIstDetIdIstitutoDetenzione());
										lIstMod = (IstitutoDetenzioneModel) lIstSqlDao.getModelByKey();
										if (lIstMod != null && lIstMod.getIdIstitutoDetenzione() != null) {
											lCompMod.setIstitutoDetenzione(lIstMod);
											// siesLogger.debug("--XX-- trovato Istituto Deyenzionei =
											// "+lCompMod.getIstitutoDetenzione());

											lTreeCompMod.add(new TreeModel(lIstMod));
										}
									}

									lTreeStatoEsecCum.add(lTreeCompMod);

									// =======================================================================================
									// Nella stampa Prospetto: Preparazione dei Totali Periodi Espiati di
									// Tutta l'Istruttoria
									// =======================================================================================
									if (lStEsecCumMod.getCodMotivo().compareTo("0900") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0901") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0902") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0903") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0937") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0267") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0270") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0675") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0121") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0212") == 0
											|| lStEsecCumMod.getCodMotivo().compareTo("0213") == 0) {
										aCalcoloPenaModel.addComputi(lCompMod);

										// Aggiungo tutti gli ESPIATI al Vettore 'VecComputi' per i Riepiloghi
										// finali
										VecComputi.add(lCompMod);
									}

									// =======================================================================================
									// Nella stampa Prospetto i Quantum di questi computi vanno sommati ai
									// BENEFICI
									// =======================================================================================
									if (lStEsecCumMod.getCodMotivo().compareTo("0284") == 0) {
										BeneficioCumuloModel lBenCum = new BeneficioCumuloModel(lCompMod);

										// è brutto ma serve solo per avere un Id Valorizzato
										lBenCum.setIdBeneficioCumulo(lCompMod.getIdComputiCumulo());
										// ============================================================

										// Ticket#20200525014 — emissione cumulo pene: gestito nullpointer
										// il campo "FlagPiuMeno" in banca dati è nullable
										if ("-".equals(lCompMod.getFlagPiuMeno()))
											lBenCum.setCodNaturaBeneficio("C");
										else if ("+".equals(lCompMod.getFlagPiuMeno()))
											lBenCum.setCodNaturaBeneficio("R");

										if ("002".equals(lCompMod.getCodTipoAnnotazione())) {
											lBenCum.setCodTipoBeneficio("03");
											lBenCum.setDescrTipoBeneficio("INDULTO");
										} else if ("003".equals(lCompMod.getCodTipoAnnotazione())) {
											lBenCum.setCodTipoBeneficio("04");
											lBenCum.setDescrTipoBeneficio("AMNISTIA");
										}
										// FINE Ticket#20200525014

										// Aggiungo il Model alla Lista dei Benefici per il calcoloPena
										aCalcoloPenaModel.addBeneficio(lBenCum);
									}
								}
							}
						}

						// Notifica_Cumulo
						lNotCumSqlDao.ricercaNotificheCumuloByIdStatoEsec(
								lStEsecCumMod.getIdStatoEsecTitoloCumulato());
						lVecNotifiche = new Vector<NotificaCumuloModel>(lNotCumSqlDao.getModels());
						if (lVecNotifiche != null && lVecNotifiche.size() > 0) {
							Iterator lItxNot = lVecNotifiche.iterator();
							while (lItxNot.hasNext()) {
								lNotMod = (NotificaCumuloModel) lItxNot.next();
								if (lNotMod != null && lNotMod.getIdNotificaCumulo() != null) {

									TreeModel lTreeNotMod = new TreeModel(lNotMod);

									lTreeStatoEsecCum.add(lTreeNotMod);
								}
							}
						}

						// =========== LIBERAZIONE ANTICIPATA LA, LS, LI, Revoche, reclami e scomputi
						// ============================
						String lStringaPeriodo = "";
						if (lStEsecCumMod.getCodMotivo().compareTo("0028") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("0039") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("0076") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("2130") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("2131") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("2132") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("2250") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("2790") == 0
								|| lStEsecCumMod.getCodMotivo().compareTo("9027") == 0) {

							lLibAntCumSqlDao.ricercaLibAnticipataCumuloByIdStatoEsec(
									lStEsecCumMod.getIdStatoEsecTitoloCumulato());
							lLibAntCumSqlDao.start();
							while (lLibAntCumSqlDao.next()) {
								LibAnticipataCumuloModel lLibAntCumMod = (LibAnticipataCumuloModel) lLibAntCumSqlDao
										.getModel();

								// if ("C".equals(lLibAntCumMod.getFlagConcesso())) {
								if (!"SL".equals(lLibAntCumMod.getCodTipoLicenza())
										&& lLibAntCumMod.getNumeroGiorni() != null) {

									if (lStringaPeriodo.length() > 0)
										lStringaPeriodo += ", ";

									lStringaPeriodo += "giorni " + lLibAntCumMod.getNumeroGiorni().toString();

									if ("LA".equals(lLibAntCumMod.getTipoLa())) {
										lStringaPeriodo += " di Liberazione Anticipata";
									} else if ("LS".equals(lLibAntCumMod.getTipoLa())) {
										lStringaPeriodo += " di Liberazione Anticipata Speciale";
									} else if ("LI".equals(lLibAntCumMod.getTipoLa())) {
										lStringaPeriodo += " di Integrazione Liberazione Anticipata";
									}
								}

								TreeModel lTreeLibAntCumMod = new TreeModel(lLibAntCumMod);

								// ======= Periodi: Dal giorno x Al giorno y =============================
								lPeriCumSqlDao.ricercaPeriodoLibAntCumuloByLibIdLibAntCum(
										lLibAntCumMod.getIdLibAnticipataCumulo());
								lPeriCumSqlDao.start();
								while (lPeriCumSqlDao.next()) {
									PeriodoLibAntCumuloModel lPeriodo = (PeriodoLibAntCumuloModel) lPeriCumSqlDao
											.getModel();
									if (lPeriodo != null && lPeriodo.getIdPeriodoLibAntCumulo() != null) {
										DatePeriodiLACumModel lDateLA = new DatePeriodiLACumModel(lPeriodo);

										lDateLA.setStringaPeriodoDalAl(lDateLA.FormaStringaDatePeriodo());

										TreeModel lTreeDatePeriodiLAMod = new TreeModel(lDateLA);

										lTreeLibAntCumMod.add(lTreeDatePeriodiLAMod);
									}

								}
								// Ticket#202603160115 - stop() per chiudere subito il cursore
								lPeriCumSqlDao.stop();
								// Ticket#202603160115 - FINE
								lTreeStatoEsecCum.add(lTreeLibAntCumMod);

								// ======= Fine DAL AL ===========================================

							} // Chiude while (lLibAntCumSqlDao.next())
							// Ticket#202603160115 - stop() per chiudere subito il cursore
							lLibAntCumSqlDao.stop();
							// Ticket#202603160115 - FINE
						}

						lTreeTitoloMod.add(lTreeStatoEsecCum);

					} // Chiude(lStEsecCumMod != null)

				} // Chiude ciclo while (lItxSta.hasNext()) (Ciclo StatoEsecTitoloCumulato
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaController.appendStatoEsecuzioneTitoloCum " + daoEx);
		} catch (Exception e) {
			siesLogger.error("Exception: " + e, e);
			throw new F3BException(
					"StampaController.appendStatoEsecuzioneTitoloCum: Eccezione Generica: " + e);
		} finally {
			cleanup(lStEsecSqlDao);
			cleanup(lCompSqlDao);
			cleanup(lNotCumSqlDao);
			cleanup(lIstSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lLibAntCumSqlDao);
			cleanup(lPeriCumSqlDao);
		}

	} // CHIUDE appendStatoEsecuzioneTitoloCum()

	/**
	 *
	 * @param lBen
	 * @return
	 * @throws F3BException
	 */
	private String DatiSentenzaRevoca(BeneficioCumuloModel lBen) throws F3BException {
		String StringaDati = "";
		String StringQuant = "";
		String StringaRevo = "";

		TitoloCumulatoModel lModel = null;
		BeneficioCumuloModel lModBen = null;
		Vector listaBen = new Vector();

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		lModel = lCtrlT.ExRicercaTitoloCumulatoById(lBen.getTitIdTitoloCumulatoCollegato());

		if (lModel != null && lModel.getIdTitoloCumulato() != null) {
			StringaDati += lModel.getDescrTipoProvvedimento();

			if (lModel.getDataProvvedimento() != null)
				StringaDati += " del "
						+ DateUtils.getDateToString(lModel.getDataProvvedimento(), "dd-MM-yyyy");

			if (lModel.getCodTipoAutoritaEmittente() != null && lModel.getCodLuogoEmittente() != null)
				StringaDati += " " + lModel.getDescrTipoAutoritaEmittente() + " di "
						+ lModel.getDescrLuogoEmittente();

		}

		if (lBen.getCodTipoBeneficio().equals("03")) {
			IBeneficioCumulo lCtrlB = SIEPLookupRemote.getBeneficioCumuloRemote();
			BeneficioCumuloModel beneficio = new BeneficioCumuloModel();

			beneficio.setTitIdTitoloCumulato(lBen.getTitIdTitoloCumulatoCollegato());
			beneficio.setCodTipoBeneficio(lBen.getCodTipoBeneficio());
			beneficio.setCodDpr(lBen.getCodDpr());
			beneficio.setCodNaturaBeneficio("R");

			listaBen = lCtrlB.ExRicercaBeneficioCumulo(beneficio);
			if (listaBen != null && listaBen.size() > 0) {
				// Sicuramente verrà trovato solo 1 Elemento che soddisfi i crteri di Ricerca
				Iterator It = listaBen.iterator();
				while (It.hasNext()) {
					lModBen = (BeneficioCumuloModel) It.next();
					StringQuant += this.Datitotquant(lModBen);
				}
			}

		}

		StringaRevo += "Beneficio Revocato ";
		if (StringQuant.compareTo("") != 0)
			StringaRevo += "nella misura di: " + StringQuant;

		if (StringaDati.compareTo("") != 0)
			StringaRevo += " " + StringaDati;

		return StringaRevo;
	}

	/**
	 *
	 * @param beneficio
	 * @return
	 * @throws F3BException
	 */
	private String Datitotquant(BeneficioCumuloModel beneficio) throws F3BException {
		String StringQuant = "";

		// Preparazione Quantum Reclusione
		String lReclusione = "";
		if (beneficio.getNumAnniReclusione() != null && beneficio.getNumAnniReclusione().intValue() > 0)
			lReclusione += "Anni " + beneficio.getNumAnniReclusione().toString() + " ";
		if (beneficio.getNumMesiReclusione() != null && beneficio.getNumMesiReclusione().intValue() > 0)
			lReclusione += "Mesi " + beneficio.getNumMesiReclusione().toString() + " ";
		if (beneficio.getNumGiorniReclusione() != null && beneficio.getNumGiorniReclusione().intValue() > 0)
			lReclusione += "Giorni " + beneficio.getNumGiorniReclusione().toString() + " ";

		if (lReclusione.compareTo("") != 0)
			StringQuant += "Reclusione " + lReclusione;

		// Preparazione Importo Multa
		String multa = new String("");
		if (beneficio.getImportoMulta() == null
				|| beneficio.getImportoMulta().compareTo(new BigDecimal(0)) == 0)
			multa = " - ";
		else
			multa = " " + StringUtils.toEuroFormat(beneficio.getImportoMulta());

		if (multa.compareTo(" - ") != 0)
			StringQuant += " Multa Euro " + multa;

		// Preparazione Quantum Arresto
		String lArresto = "";
		if (beneficio.getNumAnniArresto() != null && beneficio.getNumAnniArresto().intValue() > 0)
			lArresto += "Anni " + beneficio.getNumAnniArresto().toString() + " ";
		if (beneficio.getNumMesiArresto() != null && beneficio.getNumMesiArresto().intValue() > 0)
			lArresto += "Mesi " + beneficio.getNumMesiArresto().toString() + " ";
		if (beneficio.getNumGiorniArresto() != null && beneficio.getNumGiorniArresto().intValue() > 0)
			lArresto += "Giorni " + beneficio.getNumGiorniArresto().toString() + " ";

		if (lArresto.compareTo("") != 0)
			StringQuant += "Arresto " + lArresto;

		// Preparazione Importo Ammenda
		String ammenda = new String("");
		if (beneficio.getImportoAmmenda() == null
				|| beneficio.getImportoAmmenda().compareTo(new BigDecimal(0)) == 0)
			ammenda = " - ";
		else
			ammenda = " " + StringUtils.toEuroFormat(beneficio.getImportoAmmenda());

		if (ammenda.compareTo(" - ") != 0)
			StringQuant += " Ammenda Euro " + ammenda;

		return StringQuant;
	}

	/**
	 *
	 * @param lKeySoggetto
	 * @param lKeyFascicolo
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	protected TreeModel getTreeSoggetto(BigDecimal lKeySoggetto, FascicoloSiepModel aFascicoloSiep,
			Connection lConn) throws F3BException {
		SoggettoSqlDAO lSogSqlDao = null;
		ResidenzaSqlDAO lResDqlDao = null;
		AliasSqlDAO lAliasSqlDAO = null;
		SentenzaSqlDAO lSentSqlDao = null;
		Iterator lItx = null;

		try {
			// Soggetto
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lKeySoggetto);
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();

			// ========================================================================
			// Patch STEP 1: Aggiunta gestione Minorenne
			lSentSqlDao = new SentenzaSqlDAO(lConn);
			lSentSqlDao.ricercaSentenzaBykey(aFascicoloSiep.getSenIdSentenza());
			SentenzaModel lSentenza = (SentenzaModel) lSentSqlDao.getModelByKey();

			Vector lListaReati = new Vector();

			StampaEventoUtils lEventoUtils = new StampaEventoUtils();
			// String minorMagg = lEventoUtils.checkMinorMagg (lReati, lSogModel, lFasModel, lSentenza);
			String minorMagg = lEventoUtils.checkMinorMagg(lListaReati, lSogModel, aFascicoloSiep, lSentenza);
			siesLogger.debug("minorMagg = " + minorMagg);
			lSogModel.setStatoMinorMagg(minorMagg);
			// END PATCH
			// ========================================================================

			// Alias
			lAliasSqlDAO = new AliasSqlDAO(lConn);
			lAliasSqlDAO.ricercaAliasByIdSoggetto(lKeySoggetto);
			Vector lAlias = new Vector(lAliasSqlDAO.getModels());

			// Residenza
			lResDqlDao = new ResidenzaSqlDAO(lConn);
			lResDqlDao.ricercaResidenzaByFascicoloXStampa(aFascicoloSiep.getIdFascicoloSiep());
			Vector lResidenze = new Vector(lResDqlDao.getModels());

			TreeModel lTreeSogMod = new TreeModel(lSogModel);

			// Aggiungere Residenze e Domicili
			if (lResidenze != null) {
				lItx = lResidenze.iterator();
				while (lItx.hasNext()) {
					ResidenzaModel lResModel = new ResidenzaModel((ResidenzaModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lResModel));
				}
			}

			// Aggiungere Alias
			if (lAlias != null) {
				lItx = lAlias.iterator();
				while (lItx.hasNext()) {
					AliasModel lAliasModel = new AliasModel((AliasModel) lItx.next());
					lTreeSogMod.add(new TreeModel(lAliasModel));
				}
			}

			return lTreeSogMod;
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("StampaCumuloController.getTreeSoggetto: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			siesLogger.error("Exception: " + ex);
			throw new F3BException("StampaCumuloController.getTreeSoggetto: " + ex);
		} finally {
			cleanup(lAliasSqlDAO);
			cleanup(lSogSqlDao);
			cleanup(lResDqlDao);
			cleanup(lSentSqlDao);
		}
	}

	/**
	 *
	 * @param aIdIstruttoriaCumulo
	 * @param aCalcoloPenaModel
	 * @param aConn
	 * @throws Exception
	 */
	private void AggiungiRichiesteDelPm(BigDecimal aIdIstruttoriaCumulo,
			CalcoloPenaCumuloModel aCalcoloPenaModel, Connection aConn) throws Exception {
		siesLogger.debug("================================ ");
		siesLogger.debug("Recupero le richieste al GE/SORV ");
		siesLogger.debug("================================ ");

		RichiestePmInCumuloSqlDAO lRichPmInCumuloSqlDao = null;
		ProvvedimentoGeSorvCumSqlDAO lProvvGeSorvSqlDao = null;

		try {
			lRichPmInCumuloSqlDao = new RichiestePmInCumuloSqlDAO(aConn);
			lRichPmInCumuloSqlDao.ricercaRichiestePmInCumuloByIdIstruttoria(aIdIstruttoriaCumulo);

			Vector<RichiestePmInCumuloModel> lListaRichieste = new Vector<RichiestePmInCumuloModel>(
					lRichPmInCumuloSqlDao.getModels());

			lProvvGeSorvSqlDao = new ProvvedimentoGeSorvCumSqlDAO(aConn);
			for (int i = 0; i < lListaRichieste.size(); i++) {
				RichiestePmInCumuloModel lRichiestaModel = lListaRichieste.elementAt(i);

				// Rimuovo le richieste che non concorrono al calcolo
				if (1 == 2 || (!"002".equals(lRichiestaModel.getCodTipoAnnotazione()) // Indulto
						&& !"003".equals(lRichiestaModel.getCodTipoAnnotazione()) // Amnistia
						&& !"004".equals(lRichiestaModel.getCodTipoAnnotazione()) // Depenalizzazione
						&& !"021".equals(lRichiestaModel.getCodTipoAnnotazione()) // Revoca Beneficio
						&& !"013".equals(lRichiestaModel.getCodTipoAnnotazione()) // Incostituzionalità
						&& !"017".equals(lRichiestaModel.getCodTipoAnnotazione()) // Illecito amministrativo

						&& !"020".equals(lRichiestaModel.getCodTipoAnnotazione()) // Revoca LA
				)) {
					siesLogger.debug("Rimuovo la richiesta: [" + lRichiestaModel.getCodTipoAnnotazione() + ","
							+ lRichiestaModel.getFlagAppProvvisoria() + "] " + "[id: "
							+ lRichiestaModel.getIdRichiestePmInCumulo() + "], "
							+ lRichiestaModel.getDescrTipoAnnotazione() + ". ");
					lListaRichieste.remove(i);
					i--;
				} else {
					// Verifico se presente decisione
					siesLogger.debug("Richiesta " + lRichiestaModel.getIdRichiestePmInCumulo() + "-"
							+ lRichiestaModel.getCodTipoAnnotazione() + "-"
							+ lRichiestaModel.getDescrTipoAnnotazione()
							+ ": Verifico se presente la decisione");
					lProvvGeSorvSqlDao.ricercaProvvedimentoGeSorvCumByIdRichiesta(
							lRichiestaModel.getIdRichiestePmInCumulo());
					ProvvedimentoGeSorvCumModel lDecisioneModel = (ProvvedimentoGeSorvCumModel) lProvvGeSorvSqlDao
							.getModelByKey();
					if (lDecisioneModel == null && !"A".equals(lRichiestaModel.getFlagAppProvvisoria())) {
						siesLogger.debug("Richiesta " + lRichiestaModel.getDescrTipoAnnotazione()
								+ " senza Anticipazione e Decisione assente: la rimuovo");
						lListaRichieste.remove(i);
						i--;
					} else if (lDecisioneModel != null) {
						siesLogger.debug("Richiesta con decisione: aggiungo la decisione "
								+ lDecisioneModel.getIdProvvedimentoGeSorvCum() + " alla richiesta "
								+ lRichiestaModel.getIdRichiestePmInCumulo());
						lRichiestaModel.setDecisioneGeSorvCum(lDecisioneModel);
					} else {
						siesLogger.debug("Richiesta senza decisione ma con Anticipazione. La lascio.");
					}
				}
			}

			// SOLO PER LOGGATURE
			siesLogger.debug("Lista richieste dopo rimozione " + lListaRichieste.size());
			for (RichiestePmInCumuloModel lRichiestaModel : lListaRichieste) {
				String lLogRichDec = "";
				lLogRichDec += lRichiestaModel.getIdRichiestePmInCumulo();
				lLogRichDec += " - " + lRichiestaModel.getCodTipoAnnotazione();
				lLogRichDec += " - " + lRichiestaModel.getDescrTipoAnnotazione();
				lLogRichDec += " - " + lRichiestaModel.getFlagAppProvvisoria();

				ProvvedimentoGeSorvCumModel lDecisioneModel = lRichiestaModel.getDecisioneGeSorvCum();
				if (lDecisioneModel == null)
					lLogRichDec += " - DECISIONE: ASSENTE";
				else
					lLogRichDec += " - DECISIONE: " + lDecisioneModel.getIdProvvedimentoGeSorvCum();

				siesLogger.debug(lLogRichDec);
			}

			// Aggiungo la lista delle richieste
			siesLogger.debug("Aggiungo la lista delle richieste");
			aCalcoloPenaModel.setListaRichiestePM(lListaRichieste);

			siesLogger.debug("FINE CARICAMENTO RICHIESTE");
			siesLogger.debug("" + aCalcoloPenaModel.toString());

		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException("StampaCumuloController.AggiungiRichiesteDelPm: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			siesLogger.error("Exception: ", ex);
			throw new F3BException("StampaCumuloController.AggiungiRichiesteDelPm: " + ex);
		} finally {
			cleanup(lRichPmInCumuloSqlDao);
			cleanup(lProvvGeSorvSqlDao);

		}

	}

	// MEV_70 ===========================================================
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
	// ==================================================================

} // Chiude StampaCumuloController()