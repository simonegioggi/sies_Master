package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.jms.ICostantiJMS;
import siap.jms.controller.SiapPresaInCaricoJMSController;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.avvocato.dao.AvvocatoDAO;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.calcolopena.model.SemestreDL92Model;
import siap.siep.calcolopenadl92.dao.CalcoloPenaDL92DAO;
import siap.siep.calcolopenadl92.dao.CalcoloPenaDL92SqlDAO;
import siap.siep.calcolopenadl92.dao.SemestreDL92DAO;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.dao.FascicoloStoreProcedurePulisciDAO;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.istanza.dao.IstanzaDAO;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.DatiCumuloPerTrasferimentoModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.controller.IMisuraCautelareCumulo;
import siap.siep.modulocumulo.controller.IMisuraSicurezzaCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.IPenaComplessivaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.modulocumulo.controller.ISoggettoCumulato;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.RichiestePmInCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.dao.NuovaIstanzaDAO;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapresunta.controller.IPenaPresunta;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.reato.controller.IReato;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.util.SIEPLookupRemote;

/**
 * PresaInCaricoJMSController - Classe che prende in carico i dati pervenuti da bdi remote
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PresaInCaricoJMSController extends SiapPresaInCaricoJMSController implements IPresaInCaricoJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * INserisce l'Istanza Trasmessa 2010-04-30 - Metodo obsoleto, questo verrà rimpiazzato con :
	 * ExInserisciNuovaIstanzaTrasmessa
	 *
	 * @param aMessaggioModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExInserisciIstanzaTrasmessa(MessaggioModel aMessaggioModel) throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		IstanzaDAO lIstDao = null;
		MessaggioModel lMessReturn = null;

		ParserMessage lPars;

		try {
			lMessReturn = new MessaggioModel(aMessaggioModel);
			// lMessReturn = aMessaggioModel;

			if (aMessaggioModel.getTreeModel() != null)
				lPars = new ParserMessage(aMessaggioModel.getTreeModel());
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			if (!aMessaggioModel.getCodBdiDestinataria().equals(aMessaggioModel.getCodBdiMittente())) {

				lMessReturn = ExInserisciFascicoloSiep(aMessaggioModel);

				lConn = getDBTransaction();

				// BigDecimal lChiave = lPars.getFascicolo().getIdFascicoloSiep();
				// BigDecimal lChiaveEve;
				try {
					lEveDao = new EventoDAO(lConn);
					lEveDao.setDAOFromModel(lPars.getEvento().getEvento());
					lEveDao.setWithoutSequence(true);
					lEveDao.insert();
					// lChiaveEve = lPars.getEvento().getEvento().getIdEvento();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						aMessaggioModel.setCodEsito("00001");
						// lChiaveEve = lPars.getEvento().getEvento().getIdEvento();
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Soggetto! ");
				}

				try {
					lIstDao = new IstanzaDAO(lConn);
					lIstDao.setDAOFromModel(lPars.getIstanza());
					lIstDao.setWithoutSequence(true);
					lIstDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						aMessaggioModel.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Istanza ");
				}

				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + " %M ExInserisciIstanzaTrasmessa", ex);
			throw new F3BException(
					"PresaInCaricoJMSController.ExInserisciIstanzaTrasmessa: Non posso inserire: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + "  %M  ExInserisciIstanzaTrasmessa", sqe);
			throw new F3BException(
					"PresaInCaricoJMSController.ExInserisciIstanzaTrasmessa: Non posso inserire : " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + " %M  ExInserisciIstanzaTrasmessa", sqe);
			throw new F3BException(
					"PresaInCaricoJMSController.ExInserisciIstanzaTrasmessa: Non posso inserire : " + sqe);
		} finally {
			cleanup(lEveDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lMessReturn;
	}

	/**
	 * Inserisce l'Istanza Trasmessa ( Nuova Versione per tabella NuovaIstanza ) Questo metodo sostituisce
	 * quello precedente, ovvero quello afferente all'istanza.
	 *
	 * @param aMessaggioModel
	 * @return
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	public MessaggioModel ExInserisciNuovaIstanzaTrasmessa(MessaggioModel aMessaggioModel)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		NuovaIstanzaDAO lNIstDao = null;
		MessaggioModel lMessReturn = null;

		ParserMessage lPars;

		try {
			lMessReturn = new MessaggioModel(aMessaggioModel);

			if (aMessaggioModel.getTreeModel() != null)
				lPars = new ParserMessage(aMessaggioModel.getTreeModel());
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			if (!aMessaggioModel.getCodBdiDestinataria().equals(aMessaggioModel.getCodBdiMittente())) {
				lMessReturn = ExInserisciFascicoloSiep(aMessaggioModel);

				lConn = getDBTransaction();

				// Evento
				try {
					lEveDao = new EventoDAO(lConn);
					lEveDao.setDAOFromModel(lPars.getEvento().getEvento());
					lEveDao.setWithoutSequence(true);
					lEveDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						aMessaggioModel.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire il Soggetto! ");
				}

				// NuovaIstanza
				try {
					if (lPars.getNuovaIstanza() != null) {
						siesLogger.debug("lPars.getNuovaIstanza() is not null");
						lNIstDao = new NuovaIstanzaDAO(lConn);
						lNIstDao.setDAOFromModel(lPars.getNuovaIstanza());
						lNIstDao.setWithoutSequence(true);
						lNIstDao.insert();
					}
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED)
						aMessaggioModel.setCodEsito("00001");
					else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Istanza ");
				}
				commit(lConn);
			}
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + " %M ExInserisciNuovaIstanzaTrasmessa", daoex);
			throw new F3BException("PresaInCaricoJMSController.ExInserisciNuovaIstanzaTrasmessa : "
					+ "Non è possibile inserire i dati: " + daoex);
		} catch (SQLException sqex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + "  %M  ExInserisciNuovaIstanzaTrasmessa", sqex);
			throw new F3BException("PresaInCaricoJMSController.ExInserisciNuovaIstanzaTrasmessa : "
					+ "Non è possibile inserire i dati: " + sqex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + " %M  ExInserisciNuovaIstanzaTrasmessa", ex);
			throw new F3BException("PresaInCaricoJMSController.ExInserisciNuovaIstanzaTrasmessa : "
					+ "Non è possibile inserire i dati: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lNIstDao);
			cleanup(lConn);
		}
		return lMessReturn;
	}

	/**
	 * Inserisce il Provvedimento SIEP Trasmesso solo se di Altra BDI
	 *
	 * @param aMessaggioModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExInserisciProvvedimentoTrasmesso(MessaggioModel aMessaggioModel)
			throws F3BException {

		Connection lConn = null;

		EventoDAO lEveDao = null;
		MessaggioModel lMessReturn = null;

		ParserMessage lPars;

		try {
			lMessReturn = new MessaggioModel(aMessaggioModel);

			if (aMessaggioModel.getTreeModel() != null)
				lPars = new ParserMessage(aMessaggioModel.getTreeModel());
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			if (!aMessaggioModel.getCodBdiDestinataria().equals(aMessaggioModel.getCodBdiMittente())) {

				lMessReturn = ExInserisciFascicoloSiep(aMessaggioModel);

				lConn = getDBTransaction();

				try {
					lEveDao = new EventoDAO(lConn);
					lEveDao.setDAOFromModel(lPars.getEvento().getEvento());
					lEveDao.setWithoutSequence(true);
					lEveDao.insert();
				} catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						aMessaggioModel.setCodEsito("00001");
					} else
						throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l' Evento! ");
				}

				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + " %M ExInserisciIstanzaTrasmessa", ex);
			throw new F3BException("PresaInCaricoJMSController.ExInserisciProvvedimentoTrasmesso: " + ex);
		} catch (SQLException sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + "  %M  ExInserisciIstanzaTrasmessa", sqe);
			throw new F3BException("PresaInCaricoJMSController.ExInserisciProvvedimentoTrasmesso: " + sqe);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName() + " %M  ExInserisciProvvedimentoTrasmesso", sqe);
			throw new F3BException("PresaInCaricoJMSController.ExInserisciProvvedimentoTrasmesso: " + sqe);
		} finally {
			cleanup(lEveDao);

			cleanup(lConn);
		}

		return lMessReturn;
	}

	/**
	 * Metodo di inserimento dei dati del fascicolo SIEP nel caso di trasmissione
	 *
	 * @param aMessaggioModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExInserisciFascicoloSiep(MessaggioModel aMessaggioModel) throws F3BException {

		Connection lConn = null;
		FascicoloStoreProcedurePulisciDAO lProc = null;
		String lRapporto = "";

		ParserMessage lPars;
		try {
			if (aMessaggioModel.getTreeModel() != null)
				lPars = new ParserMessage(aMessaggioModel.getTreeModel());
			else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Messaggio Contenuto incorretto!");

			lConn = getDBTransaction();

			// ========================================================================
			// Modifica d.f. del DIC/2013. I fascicoli possono essere anche inoltrati
			// per cui la BDI/Ufficio mittente non è detto che siano i 'titolari' del
			// fascicolo trasmesso. Per testare se il fascicolo è della BDI di arrivo
			// va verificata la chiave ufficio del fascicolo
			String lChiaveUffFasc = lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getChiaveUfficio();
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficioAtti = lUff.getUfficioByKey(lChiaveUffFasc);

			boolean lStessaBDI = false;
			if (lUfficioAtti.getCodDistretto().equalsIgnoreCase(aMessaggioModel.getCodBdiDestinataria())) {
				lStessaBDI = true;
			} else {
				lStessaBDI = false;
			}
			// =================

			// se il fascicolo è di un'altra BDI si cancella!
			// if (!aMessaggioModel.getCodBdiDestinataria().equals(aMessaggioModel.getCodBdiMittente()))
			if (!lStessaBDI) {
				// Cancello tutte le informazioni legate ad un eventuale vecchio fascicolo
				lProc = new FascicoloStoreProcedurePulisciDAO(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Partita Store Procerdure PULISCI ALTRE BDI per FASCICOLO SIEP= "
						+ lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getIdFascicoloSiep());
				lProc.setIdFascicolo(
						lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getIdFascicoloSiep());
				lProc.execute();

				if (!lProc.getReturn().equals("0000")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("ERRORE DURANTE LA STORE PROCEDURE..."+lProc.getReturn());
					throw new DAOException("Errore durante la chiamata alla Store Procedure");
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Cancella PULISCI ALTRE BDI per FASCICOLO SIEP IdFascicolo = "
						+ lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getIdFascicoloSiep());
			}

			ISoggetto lSogContrl = SICOLookupRemote.getSoggettoRemote();
			String lCodEsito = lSogContrl.ExInserisciSoggettoWithoutSequence(
					lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getSoggetto(), lConn);
			// Devo fare update sul soggetto????
			lRapporto = buildRapporto("Inserimento ", " del Soggetto", lCodEsito);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Inserito Soggetto");
			// aMessaggioModel.setCodEsito(lCodEsito);
			// Devo fare update sulla sentenza????
			ISentenza lSenCntrl = SIEPLookupRemote.getSentenzaRemote();
			lCodEsito = lSenCntrl.ExInserisciSentenzaWithoutSequence(
					lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getSentenza(), lConn);
			lRapporto += buildRapporto("Inserimento ", "della Sentenza ", lCodEsito);

			IFascicoloSiep lFasCntrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lCodEsito = lFasCntrl.ExInserisciFascicoloWithoutSequence(
					lPars.getDettaglioFascicoloSiep().getFascicoloSiep(), lConn);
			lRapporto += buildRapporto("Inserimento ", "del Fascicolo ", lCodEsito);

			// STUB 31/03/2005 Inserimento Avvocati.
			if (lPars.getDettaglioFascicoloSiep().getAvvocati() != null)
				lRapporto += this.inserisciAvvocato(lPars.getDettaglioFascicoloSiep().getAvvocati(), lConn);

			// STUB 01/04/2005 Inserimento AvvocatiFascicoloSiep.
			if (lPars.getDettaglioFascicoloSiep().getAvvocatiSIEP() != null)
				lRapporto += this.inserisciAvvocatoSIEP(lPars.getDettaglioFascicoloSiep().getAvvocatiSIEP(),
						lConn);

			// MEV-2026_1 - Si inseriscono se presenti i dati dello Storico calcolo pena DL92
			if (lPars.getDettaglioFascicoloSiep().getStoricoCalcoliPenaDL92DB() != null) {
                lRapporto += this.inserisciStoricoCalcPenaDL92(lPars.getDettaglioFascicoloSiep().getStoricoCalcoliPenaDL92DB(),
                        lConn);			    
			}
			// MEV-2026_1 - FINE
			
			// 22/01/2008 Gestione INSERT degli EVENTI con ripetizione del ciclo: Risolto così il problema
			// delle integrità referenziali (EVE_ID_EVENTO e EVE_ID_EVENTO_REVOCA)
			// 22/01/2008 Gestione INSERT degli EVENTI con azzeramento campi (PEN_RES_ID_PENA_RESIDUA e
			// ANN_ID_ANNOTAZIONE_MANUALE)
			// e successivo UPDATE, dopo l'immissione di PENA_RESIDUA e ANNOTAZIONE_MANUALE.
			ArrayList lEventi = null;
			if (lPars.getDettaglioFascicoloSiep().getEventi() != null)
				lEventi = new ArrayList(lPars.getDettaglioFascicoloSiep().getEventi());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>>>>>>>>>>>>> INSERIMENTO EVENTI inizio");

			Vector lEventiInseriti = new Vector();
			// Inserimento EventoNotificaModel.
			if (lEventi != null) {
				int NroTentativi = 0;
				String[] lEsitoEventi = new String[2];
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(">>>>>>>>>>>>>>> INSERIMENTO EVENTI size() = " + lEventi.size());

				// 19/01/2011 E' stato ripristinato il ciclo FOR partendo dal primo elemento del Vettore di
				// Eventi;
				// Ad ogni remove (poiché gli elementi della lista vengono shiftati), occorre anche
				// decrementare l'indice i.
				while (!lEventi.isEmpty() && NroTentativi < 50) {
					for (int i = 0; i < lEventi.size(); i++) {
						EventoNotificaModel lEve = (EventoNotificaModel) lEventi.get(i);
						if (lEve != null) {
							lEsitoEventi = inserisciEventoNotificaAvvocatoSiep(lEve, lConn);
							// lRapporto += this.inserisciEventoNotificaAvvocatoSiep(lEve, lConn);
							lRapporto += lEsitoEventi[1];
							if (lEsitoEventi[0].compareTo("00000") == 0) {
								// 21/01/2008 Elimina l'Evento se inserito.
								lEventi.remove(i);
								i--; // 19/01/2011
								lEventiInseriti.add(lEve.getEvento().getIdEvento());
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info(
										">>>>> Inserito Evento con ID = " + lEve.getEvento().getIdEvento());
							}
						}
					}

					NroTentativi++;

				}
				if (lEventi.isEmpty())
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(">>>>> Fine Inserimento Eventi del Fascicolo ");
				else
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(
							">>>>> Attenzione!!!!! Inserimento Eventi del Fascicolo non completato: mancano "
									+ lEventi.size() + " eventi. ");
			}

			// STUB 02/01/2008 Inserimento Residenze + Residenze Fascicolo SIEP.
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListResidenzaFasSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListResidenzaFasSiep().size() > 0) {
				IResidenza lResCntrl = SICOLookupRemote.getResidenzaRemote();
				lCodEsito = lResCntrl.ExInserisciResidenzeWithoutSequence(new ArrayList(lPars
						.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListResidenzaFasSiep()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Residenze ", lCodEsito);
			}

			// inizioMEV_67 (SALVO L'INFORMAZIONE DI ALTRA_CAUSA)
			// Ticket#20211019014 - in alcuni casi arrivano fascicoli con Posizione giuridica che punta (FK
			// ALT_CAU_ID_ALTRA_CAUSA)
			// ALTRA_CAUSA ma il record ALTRA_CAUSA non viene trasferito perchè non recuprato dal metodo che
			// carica il dettaglio fascicolo SIEP
			// Quindi si testa se è stato trasferito il record AC e se l'id della PG coincide. In caso
			// negativo si
			// ripulisce il puntamento da PG
			// Recupero idAltraCauso
			BigDecimal idAltraCausa = null; // Ticket#20211019014
			if (lPars.getDettaglioFascicoloSiep().getAltraCausa() != null) {
				idAltraCausa = lPars.getDettaglioFascicoloSiep().getAltraCausa().getIdAltraCausa(); // Ticket#20211019014
				IAltraCausa altrCausCntrl = SIEPLookupRemote.getAltraCausa();
				lCodEsito = altrCausCntrl.ExInserisciAltraCausaWithoutSequence(
						lPars.getDettaglioFascicoloSiep().getAltraCausa(), lConn);
				lRapporto += buildRapporto("Inserimento ", "Altra Causa della Posizione Giuridica ",
						lCodEsito);
			}
			// fine MEV_67

			if (lPars.getDettaglioFascicoloSiep().getPosizioneGiuridica() != null) {
				// Ticket#20211019014 -
				BigDecimal idAltraCausaPG = lPars.getDettaglioFascicoloSiep().getPosizioneGiuridica()
						.getAltCauIdAltraCausa();
				if (idAltraCausaPG != null
						&& (idAltraCausa == null || idAltraCausaPG.compareTo(idAltraCausa) != 0)) {
					lPars.getDettaglioFascicoloSiep().getPosizioneGiuridica().setAltCauIdAltraCausa(null);
				}
				// Ticket#20211019014 - FINE
				IPosizioneGiuridica lPosGiuCntrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
				lCodEsito = lPosGiuCntrl.ExInserisciPosizioneGiuridicaWithoutSequence(
						lPars.getDettaglioFascicoloSiep().getPosizioneGiuridica(), lConn);
				lRapporto += buildRapporto("Inserimento ", "della Posizione Giuridica ", lCodEsito);
			}

			if (lPars.getDettaglioFascicoloSiep().getLuogoDetenzione() != null) {
				ILuogoDetenzione lLuoCntrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
				lCodEsito = lLuoCntrl.ExInserisciLuogoDetenzioneWithoutSequence(
						lPars.getDettaglioFascicoloSiep().getLuogoDetenzione(), lConn);
				lRapporto += buildRapporto("Inserimento ", "del Luogo Detenzione ", lCodEsito);
			}

			if (lPars.getDettaglioFascicoloSiep().getReatiCircostanze() != null) {
				IReato lReaCntrl = SIEPLookupRemote.getReatoRemote();
				lCodEsito = lReaCntrl.ExInserisciReatiWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getReatiCircostanze()), lConn);
				lRapporto += buildRapporto("Inserimento ", "dei Reati ", lCodEsito);
			}

			// 19/12/2007 Circostanze aggiunte x presa in carico Fascicolo SIEP
			if (lPars.getDettaglioFascicoloSiep().getCircostanze() != null
					&& lPars.getDettaglioFascicoloSiep().getCircostanze().size() > 0) {
				ICircostanza lCirCntrl = SIEPLookupRemote.getCircostanzaRemote();
				lCodEsito = lCirCntrl.ExInserisciCircostanzaWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getCircostanze()), lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Circostanze ", lCodEsito);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO CIRCOSTANZE --------------------------------------|||||||||||||||||||||| ");

			// 21/01/2008 Annotazione Manuale x presa in carico Fascicolo SIEP
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListAnnotazioneManuale() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(
						">>>>> DENTRO IF ANNOTAZIONI        --------------------------|||||||||||||||||||||| ");
				IAnnotazioneManuale lAnnCntrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lCodEsito = lAnnCntrl
						.ExInserisciAnnManualiWithoutSequence(new ArrayList(lPars.getDettaglioFascicoloSiep()
								.getDatiSiepPerTrasferimento().getListAnnotazioneManuale()), lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Annotazioni Manuali ", lCodEsito);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.info(">>>>> DOPO la CIRCOSTANZA     --------------------------|||||||||||||||||||||| ");
			// 21/01/2008 Pena Residua x presa in carico Fascicolo SIEP
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListPenaResidua() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(
						">>>>> DENTROla PENA RESIDUA     --------------------------|||||||||||||||||||||| ");
				IPenaResidua lPenResCntrl = SIEPLookupRemote.getPenaResiduaRemote();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("=========> PENA RESIDUA SIZE----->" + lPars.getDettaglioFascicoloSiep()
						.getDatiSiepPerTrasferimento().getListPenaResidua().size());
				lCodEsito = lPenResCntrl.ExInserisciPeneResidueWithoutSequence(new ArrayList(
						lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListPenaResidua()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Pene Residue ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.info(">>>>> DOPO la PENA RESIDUA     --------------------------|||||||||||||||||||||| ");

			// 22/01/2008 Fungibilità x presa in carico Fascicolo SIEP
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListFungibilita() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(
						">>>>>DENTRO FUNGIBILITA     --------------------------|||||||||||||||||||||| ");
				IFungibilita lFunCntrl = SIEPLookupRemote.getFungibilitaRemote();
				lCodEsito = lFunCntrl.ExInserisciFungibilitaWithoutSequence(new ArrayList(
						lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListFungibilita()),
						new ArrayList(lEventiInseriti), lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Fungibilità ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>>>DOPO FUNGIBILITA     --------------------------|||||||||||||||||||||| ");

			if (lPars.getDettaglioFascicoloSiep().getPenaComplessivaSanzioneSostitutiva() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info(
						">>>>>DENTRO PENACOMPLESSIVA     --------------------------|||||||||||||||||||||| ");
				IPenaComplessiva lPenCompCntrl = SIEPLookupRemote.getPenaComplessivaRemote();
				lCodEsito = lPenCompCntrl.ExInserisciPenaComplessivaWithoutSequence(
						lPars.getDettaglioFascicoloSiep().getPenaComplessivaSanzioneSostitutiva(), lConn);
				lRapporto += buildRapporto("Inserimento ", "della Pena Complessiva ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.info(">>>>>DOPO  PENACOMPLESSIVA     --------------------------|||||||||||||||||||||| ");

			if (lPars.getDettaglioFascicoloSiep().getPenaPresunta() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.info(">>>>>DENTRO PENAPRESUNTA   --------------------------|||||||||||||||||||||| ");
				IPenaPresunta lPenPresCntrl = SIEPLookupRemote.getPenaPresuntaRemote();
				lCodEsito = lPenPresCntrl.ExInserisciPenaPresuntaWithoutSequence(
						lPars.getDettaglioFascicoloSiep().getPenaPresunta(), lConn);
				lRapporto += buildRapporto("Inserimento ", "della Pena Presunta ", lCodEsito);
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger
					.info(">>>>> DOPO la PENA PRESUNTA   --------------------------|||||||||||||||||||||| ");
			// Operazione Duplicata - Fabio 07/04/2009
			/*
			 * if (lPars.getDettaglioFascicoloSiep().getPenaResidua() != null) { IPenaResidua lPenResCntrl =
			 * SIEPLookupRemote.getPenaResiduaRemote(); lCodEsito =
			 * lPenResCntrl.ExInserisciPenaResiduaWithoutSequence(lPars.getDettaglioFascicoloSiep().
			 * getPenaResidua(), lConn); lRapporto += buildRapporto("Inserimento ", "della Pena Residua ",
			 * lCodEsito); }
			 */

			if (lPars.getDettaglioFascicoloSiep().getStatoProcedimento() != null
					&& lPars.getDettaglioFascicoloSiep().getStatoProcedimento().size() > 0) {
				IStatoProcedimento lStatoCntrl = SIEPLookupRemote.getStatoProcedimentoRemote();
				lCodEsito = lStatoCntrl.ExInserisciStatoProcedimentoWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getStatoProcedimento()), lConn);
				lRapporto += buildRapporto("Inserimento ", "dello Stato procedimento ", lCodEsito);
			}

			if (lPars.getDettaglioFascicoloSiep().getMisureCautelari() != null
					&& lPars.getDettaglioFascicoloSiep().getMisureCautelari().size() > 0) {
				IMisuraCautelare lMisCntrl = SIEPLookupRemote.getMisuraCautelareRemote();
				lCodEsito = lMisCntrl.ExInserisciMisuraCautelareWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getMisureCautelari()), lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Misure Cautelari ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO la MISURE CAUTELARI   --------------------------|||||||||||||||||||||| ");

			if (lPars.getDettaglioFascicoloSiep().getPeneAccessorie() != null
					&& lPars.getDettaglioFascicoloSiep().getPeneAccessorie().size() > 0) {
				IPenaAccessoria lPenAccCntrl = SIEPLookupRemote.getPenaAccessoriaRemote();
				lCodEsito = lPenAccCntrl.ExInserisciPenaAccessoriaWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getPeneAccessorie()), lConn);
				lRapporto += buildRapporto("Inserimento ", "della Pena Accessoria ", lCodEsito);
			}

			if (lPars.getDettaglioFascicoloSiep().getBenefici() != null
					&& lPars.getDettaglioFascicoloSiep().getBenefici().size() > 0) {
				IBeneficio lBenCntrl = SIEPLookupRemote.getBeneficioRemote();
				lCodEsito = lBenCntrl.ExInserisciBeneficioWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getBenefici()), lConn);
				lRapporto += buildRapporto("Inserimento ", "dei Benefici ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>>> DOPO i BENEFICI     --------------------------|||||||||||||||||||||| ");

			if (lPars.getDettaglioFascicoloSiep().getMisureSicurezza() != null
					&& lPars.getDettaglioFascicoloSiep().getMisureSicurezza().size() > 0) {
				IMisuraSicurezza lMisSicCntrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				lCodEsito = lMisSicCntrl.ExInserisciMisuraSicurezzaWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getMisureSicurezza()), lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Misure di Sicurezza ", lCodEsito);
			}

			// new d.f. 29/01/2015 iscrivo anche eventuali collegamenti tra fascicoli
			// di esecuzione misure di sicurezza
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListFascMsToFascSiep() != null) {

				IMisuraSicurezza lMisSicCntrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				lCodEsito = lMisSicCntrl.ExInserisciFascMStoFascSIEPWithoutSequence(new ArrayList(lPars
						.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListFascMsToFascSiep()),
						lConn);
				lRapporto += buildRapporto("Inserimento ",
						"dei collegamenti fascicoli di esecuzione Misure di Sicurezza ", lCodEsito);
			}

			// STUB 28/11/2007 Inserimento Dati di CUMULO, PENA_CUMULO e ULTERIORE_SANZIONE_CUMULO.
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null) {
				if (lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListCumulo() != null
						&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListCumulo()
								.size() > 0) {
					ICumulo lCumCntrl = SIEPLookupRemote.getCumuloRemote();
					lCodEsito = lCumCntrl.ExInserisciCumuloWithoutSequence(new ArrayList(
							lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListCumulo()),
							lConn);
					lRapporto += buildRapporto("Inserimento ", "dei Cumulo ", lCodEsito);
				}
				if (lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
						.getListPenaCumulo() != null
						&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListPenaCumulo()
								.size() > 0) {
					IPenaCumulo lPenCumCntrl = SIEPLookupRemote.getPenaCumuloRemote();
					lCodEsito = lPenCumCntrl.ExInserisciPenaCumuloWithoutSequence(new ArrayList(lPars
							.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListPenaCumulo()),
							lConn);
					lRapporto += buildRapporto("Inserimento ", "delle PenaCumulo ", lCodEsito);
				}

				if (lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
						.getListUltSanCumulo() != null
						&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getListUltSanCumulo().size() > 0) {
					IUlterioreSanzioneCumulo lUltSanCumCntrl = SIEPLookupRemote
							.getUlterioreSanzioneCumuloRemote();
					lCodEsito = lUltSanCumCntrl.ExInserisciUlterioreSanzioneCumuloWithoutSequence(
							new ArrayList(lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
									.getListUltSanCumulo()),
							lConn);
					lRapporto += buildRapporto("Inserimento ", "delle UlterioreSanzioneCumulo ", lCodEsito);
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>>> DOPO il CUMULO     --------------------------|||||||||||||||||||||| ");

			// STUB 13/12/2007 Inserimento Dati di LICENZA_LIBANTICIPATA,
			// MISURA_ALTERNATIVA,RESIDENZA_FASCICOLO_SIEP, MAGISTRATO, MAGISTRATO_COMPETENTE.
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListLicLibAnticipata() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListLicLibAnticipata().size() > 0) {
				ILicenzaPeriodiLibAnticipata lLicLibCntrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				lCodEsito = lLicLibCntrl.ExInserisciLicenzePeriodiLibAnticipataWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getListLicLibAnticipata()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Licenza Lib. Anticipata ", lCodEsito);
			}

			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListMisuraAlternativa() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListMisuraAlternativa().size() > 0) {
				IMisuraAlternativa lMisAltCntrl = SICOLookupRemote.getMisuraAlternativaRemote();
				// old codice pre merge
				// lCodEsito = lMisAltCntrl.ExInserisciMisuraAlternativaWithoutSequence(new
				// ArrayList(lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListMisuraAlternativa()),
				// lConn);
				// lRapporto += buildRapporto("Inserimento ", "delle Misure Alternative ", lCodEsito);
				Vector<MisuraAlternativaModel> lListaMisureAlt = new Vector<MisuraAlternativaModel>(
						lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getListMisuraAlternativa());
				for (MisuraAlternativaModel lMisAltModel : lListaMisureAlt) {
					lCodEsito = lMisAltCntrl.ExInserisciMisuraAlternativaWithoutSequence(lMisAltModel, lConn);
					lRapporto += buildRapporto("Inserimento ", "delle Misure Alternative ", lCodEsito);
				}
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO le MISURE ALTERNATIVE     --------------------------|||||||||||||||||||||| ");

			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getMagistratoCompetente() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getMagistratoCompetente().getMagistrato() != null) {
				IMagistrato lMagCntrl = SICOLookupRemote.getMagistratoRemote();
				lCodEsito = lMagCntrl.ExInserisciMagistratoWithoutSequence(lPars.getDettaglioFascicoloSiep()
						.getDatiSiepPerTrasferimento().getMagistratoCompetente().getMagistrato(), lConn);
				lRapporto += buildRapporto("Inserimento ", "del Magistrato ", lCodEsito);
			}

			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getMagistratoCompetente() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getMagistratoCompetente().getMagistratoCompetente() != null) {
				IMagistratoCompetente lMagCompCntrl = SICOLookupRemote.getMagistratoCompetenteRemote();
				lCodEsito = lMagCompCntrl.ExInserisciMagistratoCompetenteWithoutSequence(
						lPars.getDettaglioFascicoloSiep().getFascicoloSiep().getIdFascicoloSiep(),
						lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getMagistratoCompetente(),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "del Magistrato Competente ", lCodEsito);
			}

			// 26/03/2008 Sanzione Sost. Residua x presa in carico Fascicolo SIEP
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListSanzioneSostResidua() != null) {
				ISanzioneSostitutiva lSSResiduaCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
				lCodEsito = lSSResiduaCtrl.ExInserisciSanzioniSostResidueWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getListSanzioneSostResidua()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Sanzioni Sost. Residue ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO le SANZ SOST RESIDUE    --------------------------|||||||||||||||||||||| ");

			// 22/01/2008 Aggiornamento degli EVENTO con PEN_RES_ID_PENA_RESIDUA e ANN_ID_ANNOTAZIONE_MANUALE.
			if (lEventi != null) {
				for (int i = 0; i < lEventi.size(); i++) {
					EventoNotificaModel lEve = (EventoNotificaModel) lEventi.get(i);
					if (lEve.getEvento().getAnnIdAnnotazioneManuale() != null
							|| lEve.getEvento().getPenIdPenaResidua() != null) {
						lRapporto += aggiornaEvento(lEve, lConn);
					}
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO il CICLO FOR EVENTI     --------------------------|||||||||||||||||||||| ");

			// 16/03/2009 Inserimento Dati di Conversione Pene Pecuniarie RICHIESTA_CONVERSIONE.
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListRichiesteConversioniPP() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListRichiesteConversioniPP().size() > 0) {
				IRichiestaConversione RicConCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
				lCodEsito = RicConCtrl.ExInserisciRichiesteConversioniWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getListRichiesteConversioniPP()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Richieste Conversioni ", lCodEsito);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO le RICHIESTE CONVERSIONI     --------------------------|||||||||||||||||||||| ");

			// 30/08/2010 Inserimento Dati di NUOVA_ISTANZA.
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListNuovaIstanza() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListNuovaIstanza()
							.size() > 0) {
				INuovaIstanza NuoIstCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
				lCodEsito = NuoIstCtrl.ExInserisciNuovaIstanzaWithoutSequence(new ArrayList(lPars
						.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListNuovaIstanza()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", "delle Nuove Istanze ", lCodEsito);
			}

			// 01/09/2010 Inserimento Dati Altri Gradi di Giudizio.
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListAltriGradiGiudizio() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListAltriGradiGiudizio().size() > 0) {
				IAltriGradiGiudizio AltGraGiuCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();
				lCodEsito = AltGraGiuCtrl.ExInserisciAltriGradiGiudizioWithoutSequence(
						new ArrayList(lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
								.getListAltriGradiGiudizio()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", " Altri Gradi di Giudizio ", lCodEsito);
			}

			// 30/09/2014 Inserimento Sentenze Riunite
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListSentenzeRiunite() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListSentenzeRiunite().size() > 0) {
				ISentenzaRiunita lSentRiuniteCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
				lCodEsito = lSentRiuniteCtrl.ExInserisciSentenzaRiunitaWithoutSequence(new ArrayList(lPars
						.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListSentenzeRiunite()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", " Sentenze Riunite ", lCodEsito);
			}

			// MEV26 CUMULO Inserimento record COMPETENZA collegati ai provvedimeti (EVNTI)
			// di trasmissione per competenza
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListCompetenze() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListCompetenze()
							.size() > 0) {
				ICompetenza lCompetenzaCtrl = SIEPLookupRemote.getCompetenzaRemote();

				lCodEsito = lCompetenzaCtrl.ExInserisciCompetenzeWithoutSequence(new ArrayList(
						lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListCompetenze()),
						lConn);
				lRapporto += buildRapporto("Inserimento ", " Competenza ", lCodEsito);
			}

			// ==========================================
			// 07/2017 Inserimento Sospensioni (MEV 42 - aggiunte le sospensioni che non venivano trasmesse)
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListSospensioni() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListSospensioni()
							.size() > 0) {
				ISospensione lSosCtrl = SIEPLookupRemote.getSospensioneRemote();

				List<SospensioneModel> lListaSospensioni = lPars.getDettaglioFascicoloSiep()
						.getDatiSiepPerTrasferimento().getListSospensioni();
				siesLogger.debug("Procedo all'acquisizione delle SOSPENSIONI...");
				for (SospensioneModel lSospensioneModel : lListaSospensioni) {
					lCodEsito = lSosCtrl.ExInserisciSospensioneWithoutSequence(lSospensioneModel, lConn);
					lRapporto += buildRapporto("Inserimento ", " Sospensioni ", lCodEsito);
				}
			}

			// ===============================================
			// 07/2017 Inserimento Decreto Ordinanza SIEP (MEV 42 - aggiunte le DecOrd che non venivano
			// trasmessi)
			if (lPars.getDettaglioFascicoloSiep() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento()
							.getListDecretiOrd() != null
					&& lPars.getDettaglioFascicoloSiep().getDatiSiepPerTrasferimento().getListDecretiOrd()
							.size() > 0) {
				IDecretoOrdinanzaSiep lDecOrdCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
				List<DecretoOrdinanzaSiepModel> lListaDecretiOrdSiep = lPars.getDettaglioFascicoloSiep()
						.getDatiSiepPerTrasferimento().getListDecretiOrd();
				siesLogger.debug("Procedo all'acquisizione di DECRETO_ORDINANZA_SIEP...");
				for (DecretoOrdinanzaSiepModel lDecretoOrdSiepModel : lListaDecretiOrdSiep) {
					lCodEsito = lDecOrdCtrl.ExInserisciDecretoOrdinanzaWithoutSequence(lDecretoOrdSiepModel,
							lConn);
					lRapporto += buildRapporto("Inserimento ", " Decreti Ordinanze SIEP ", lCodEsito);
				}
			}

			// --------------------------------------------------------------------------------------------
			// MEV 26 CUMULO Step2 - Inserimento Istruttorie
			// --------------------------------------------------------------------------------------------

			siesLogger.info("===========================================");
			siesLogger.info(">>>>> Inserimento Nuove Istruttorie Cumulo ");
			siesLogger.info("===========================================");
			Vector<IstruttoriaCumuloModel> lVecIstruttorie = new Vector();
			DatiCumuloPerTrasferimentoModel lModelDatiCum = new DatiCumuloPerTrasferimentoModel();
			Vector<TitoloCumulatoModel> lTotaleTitoli = new Vector();

			RichiesteInviateCumModel lRicInvMod = null;
			Vector<RichiestePmInCumuloModel> lTotaleRicPm = new Vector<>();

			// Recupero le Istruttorie_Cumulo
			if (lPars.getDatiCumuloPerTrasferimento() != null) {
				lModelDatiCum = lPars.getDatiCumuloPerTrasferimento();
				if (lModelDatiCum.getListIstruttoriaCumulo() != null
						&& lModelDatiCum.getListIstruttoriaCumulo().size() > 0) {
					lVecIstruttorie = new Vector<>(lModelDatiCum.getListIstruttoriaCumulo());
				}
			}

			// Inserisco le eventuali Istruttorie
			if (lVecIstruttorie != null && lVecIstruttorie.size() > 0) {
				IIstruttoriaCumulo lIstruCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

				for (IstruttoriaCumuloModel lIstruttoriaCumModel : lVecIstruttorie) {
					lCodEsito = lIstruCtrl.ExInserisciIstruttoriaCumuloWithoutSequence(lIstruttoriaCumModel,
							lConn);
					lRapporto += buildRapporto("Inserimento ", " Istruttoria_Cumulo ", lCodEsito);
				}
			}

			siesLogger.info(
					">>>>> DOPO il CICLO INSERT ISTRUTTORIA_CUMULO     --------------------------|||||||||||||||||||||| ");

			// ---------------------------------------------------------------------
			// Insert di tutti gli oggetti Collegati a ISTRUTTORIA_CUMULO
			if (lVecIstruttorie != null && lVecIstruttorie.size() > 0) {
				IDatiFinaliCumulo lDatiFinCtrl = SIEPLookupRemote.getDatiFinaliCumuloRemote();
				IComputiCumulo lCompCtrl = SIEPLookupRemote.getComputiCumuloRemote();
				ITitoloCumulato lTitoCtrl = SIEPLookupRemote.getTitoloCumulatoRemote();
				IRichiestePmInCumulo lRichCtrl = SIEPLookupRemote.getRichiestePmInCumuloRemote();

				Iterator ItxI = lVecIstruttorie.iterator();
				while (ItxI.hasNext()) {
					IstruttoriaCumuloModel lIstru = (IstruttoriaCumuloModel) ItxI.next();

					// Dati_Finali_Cumulo
					if (lIstru.getDatiFinaliCumulo() != null) {
						lCodEsito = lDatiFinCtrl.ExInserisciDatiFinaliCumuloWithoutSequence(
								lIstru.getDatiFinaliCumulo(), lConn);
						lRapporto += buildRapporto("Inserimento ", " Dati_Finali_Cumulo ", lCodEsito);
					}

					// Posizione_Giuridica_Cumulo
					if (lIstru.getPosizioneGiuridicaCumulo() != null) {
						lCodEsito = lDatiFinCtrl.ExInserisciPosizioneGiuridicaCumuloWithoutSequence(
								lIstru.getPosizioneGiuridicaCumulo(), lConn);
						lRapporto += buildRapporto("Inserimento ", " Posizione_Giuridica_Cumulo ", lCodEsito);
					}

					// Pena_Rideterminata_Cumulo
					if (lIstru.getPenaRideterminataCumulo() != null
							&& lIstru.getPenaRideterminataCumulo().size() > 0) {
						Vector<PenaRideterminataCumuloModel> VecPeneRide = new Vector(
								lIstru.getPenaRideterminataCumulo());

						for (PenaRideterminataCumuloModel lPenRidetCumModel : VecPeneRide) {
							lCodEsito = lDatiFinCtrl.ExInserisciPeneRideterminateCumuloWithoutSequence(
									lPenRidetCumModel, lConn);
							lRapporto += buildRapporto("Inserimento ", " Pena_Rideterminata_Cumulo ",
									lCodEsito);
						}
					}

					// Ulteriori Sanzioni Cumulo
					if (lIstru != null && lIstru.getDatiFinaliUlterioriSanzioni() != null
							&& lIstru.getDatiFinaliUlterioriSanzioni().size() > 0) {
						Vector<DatiFinaliUlterioriSanzioniModel> VecUlterioriSan = new Vector(
								lIstru.getDatiFinaliUlterioriSanzioni());

						for (DatiFinaliUlterioriSanzioniModel lDatFinUltSanzModel : VecUlterioriSan) {
							lCodEsito = lDatiFinCtrl.ExInserisciUlerioriSanzioniCumuloWithoutSequence(
									lDatFinUltSanzModel, lConn);
							lRapporto += buildRapporto("Inserimento ", " Ulteriori Sanzioni Cumulo ",
									lCodEsito);
						}
					}

					// Titolo_Cumulato
					if (lIstru != null && lIstru.getTitoliCumulati() != null
							&& lIstru.getTitoliCumulati().size() > 0) {
						Vector<TitoloCumulatoModel> VecTitoCum = new Vector(lIstru.getTitoliCumulati());

						for (TitoloCumulatoModel lTitoloCumModel : VecTitoCum) {
							lCodEsito = lTitoCtrl.ExInserisciTitoliCumulatiWithoutSequence(lTitoloCumModel,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Titolo_Cumulato ", lCodEsito);
						}

						// ottengo tutti i titoli di tutte le istruttorie
						lTotaleTitoli.addAll(VecTitoCum);
					}
					siesLogger.info(
							">>>>> DOPO INSERT TITOLO_CUMULATO     --------------------------|||||||||||||||||||||| ");

					// siesLogger.debug("Caricamento COMPUTI_CUMULO NON collegati a stato esecuzione");
					// if(lIstru!=null && lIstru.getComputiCumulo()!=null &&
					// lIstru.getComputiCumulo().size()>0 )
					// {
					// List <ComputiCumuloModel> listaCompCum = lIstru.getComputiCumulo();
					//
					// Vector <ComputiCumuloModel> VecCompCum = new Vector <ComputiCumuloModel>();
					// for (ComputiCumuloModel lComputo: listaCompCum) {
					// if (lComputo.getStatIdStatoEsecTitCum()==null)
					// VecCompCum.add (lComputo);
					// }
					//
					// lCodEsito = lCompCtrl.ExInserisciComputiCumuloWithoutSequence(VecCompCum , lConn);
					// lRapporto += buildRapporto("Inserimento ", " Computi_Cumulo ", lCodEsito);
					// }

					// Richieste_Inviate_Cum
					if (lIstru != null && lIstru.getRichiesteInviate() != null
							&& lIstru.getRichiesteInviate().size() > 0) {
						Vector<RichiesteInviateCumModel> VecRichInvCum = new Vector<>(
								lIstru.getRichiesteInviate());
						lCodEsito = lRichCtrl.ExInserisciRichiesteInviateCumWithoutSequence(VecRichInvCum,
								lConn);
						lRapporto += buildRapporto("Inserimento ",
								" Richieste_Inviate_Cum/Richieste_Pm_In_Cumulo (Inviate) ", lCodEsito);

						// preparazione Lista con le Richieste_PM (legate alle Richieste Inviate)
						// per il successivo inserimento delle tabelle di relazione RICHPM_xxxx
						Iterator ItxR = VecRichInvCum.iterator();
						while (ItxR.hasNext()) {
							lRicInvMod = (RichiesteInviateCumModel) ItxR.next();
							if (lRicInvMod != null && lRicInvMod.getListaRichiestePMinCumulo() != null
									&& lRicInvMod.getListaRichiestePMinCumulo().size() > 0) {
								lTotaleRicPm.addAll(lRicInvMod.getListaRichiestePMinCumulo());
							}
						}
					}

					// Richieste_Pm_In_Cumulo ( ancora da inviare, NON legate alle Richieste_Inviate )
					if (lIstru != null && lIstru.getListaRichiestePmInCumulo() != null
							&& lIstru.getListaRichiestePmInCumulo().size() > 0) {
						Vector<RichiestePmInCumuloModel> VecRichPmCum = new Vector<>(
								lIstru.getListaRichiestePmInCumulo());
						lCodEsito = lRichCtrl.ExInserisciRichiestePmInCumuloWithoutSequence(VecRichPmCum,
								lConn);
						lRapporto += buildRapporto("Inserimento ", " Richieste_Pm_In_Cumulo ", lCodEsito);

						// preparazione Lista con le Richieste_PM (ancora da inviare, NON legate alle
						// Richieste_Inviate )
						lTotaleRicPm.addAll(VecRichPmCum);
					}
					siesLogger.info(
							">>>>> DOPO INSERT RICHIESTE_PM (cumulo)    --------------------------|||||||||||||||||||||| ");

				} // Chiude while(istruttorie has next)

				siesLogger.info(
						">>>>> Terminate Insert dei dati Collegati ad ISTRUTTORIA_CUMULO   --------------------------|||||||||||||||||||||| ");

				// ---------------------------------------------------------------------
				// Insert di tutti gli oggetti Collegati a TITOLO_CUMULATO
				if (lTotaleTitoli != null && lTotaleTitoli.size() > 0) {
					ISoggettoCumulato lSogCtrl = SIEPLookupRemote.getSoggettoCumuloRemote();
					IPenaComplessivaCumulo lPenaCompCtrl = SIEPLookupRemote.getPenaComplessivaCumuloRemote();
					IReatoCumulo lReatoCumCtrl = SIEPLookupRemote.getReatoCumuloRemote();
					ICircostanzaCumulo lCirCumCtrl = SIEPLookupRemote.getCircostanzaCumuloRemote();
					IMisuraSicurezzaCumulo lMisSicCtrl = SIEPLookupRemote.getMisuraSicurezzaCumuloRemote();
					IMisuraCautelareCumulo lMisCauCtrl = SIEPLookupRemote.getMisuraCautelareCumuloRemote();
					IPenaAccessoriaCumulo lpenaAccCumCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
					IBeneficioCumulo lBeneCumCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
					IStatoEsecTitoloCumulato lStEsecCumCtrl = SIEPLookupRemote
							.getStatoEsecTitoloCumulatoRemote();
					// INotificaCumulo lNotCumCtrl = SIEPLookupRemote.getNotificaCumuloRemote();

					Iterator ItxT = lTotaleTitoli.iterator();
					while (ItxT.hasNext()) {
						TitoloCumulatoModel ltitoloMod = (TitoloCumulatoModel) ItxT.next();

						// Soggetto_Cumulato
						if (ltitoloMod != null && ltitoloMod.getSoggettoCumulato() != null) {
							lCodEsito = lSogCtrl.ExInserisciSoggetto_CumulatoWithoutSequence(
									ltitoloMod.getSoggettoCumulato(), lConn);
							lRapporto += buildRapporto("Inserimento ", " Soggetto_Cumulato ", lCodEsito);
						}

						// Procedimento_Cumulato
						if (ltitoloMod != null && ltitoloMod.getProcedimentoCumulato() != null) {
							lCodEsito = lTitoCtrl.ExInserisciProcedimentoCumulatoWithoutSequence(
									ltitoloMod.getProcedimentoCumulato(), lConn);
							lRapporto += buildRapporto("Inserimento ", " Procedimento_Cumulato ", lCodEsito);
						}
						siesLogger.info(
								">>>>> DOPO INSERT DATI SOGGETTO e PROCEDIMENTO_CUMULATO     --------------------------|||||||||||||||||||||| ");
						//

						// Pena_Complessiva_Cumulo + Sanzione Sostitutiva_Cumulo + Continuazioni
						if (ltitoloMod != null && ltitoloMod.getPenaComplessivaCumulo() != null) {
							lCodEsito = lPenaCompCtrl
									.ExInserisciPenaComplessivaSanzioneSostContinuazioniCumuloWithoutSequence(
											ltitoloMod.getPenaComplessivaCumulo(), lConn);
							lRapporto += buildRapporto("Inserimento ",
									" Pena_Complessiva_Cumulo/Sanzione Sost/Continuazioni ", lCodEsito);
						}
						siesLogger.info(
								">>>>> DOPO INSERT DATI PENA COMPLESSIVA, SANZIONE SOSTITUTIVA e CONTINUAZIONI CUMULO     ----------------||||||| ");
						//

						// Reato_Cumulo
						if (ltitoloMod != null && ltitoloMod.getReatiCumulo() != null
								&& ltitoloMod.getReatiCumulo().size() > 0) {
							Vector<ReatoCumuloModel> VecReato = new Vector(ltitoloMod.getReatiCumulo());

							lCodEsito = lReatoCumCtrl.ExInserisciReatiCumulatiWithoutSequence(VecReato,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Reato_Cumulo ", lCodEsito);

						}

						// Circostanza_Cumulo
						if (ltitoloMod != null && ltitoloMod.getCircostanzeCumulo() != null
								&& ltitoloMod.getCircostanzeCumulo().size() > 0) {
							Vector<CircostanzaCumuloModel> VecCirco = new Vector(
									ltitoloMod.getCircostanzeCumulo());

							lCodEsito = lCirCumCtrl.ExInserisciCircostanzeCumulateWithoutSequence(VecCirco,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Circostanza_Cumulo ", lCodEsito);

						}

						// Misura_Sicurezza_Cumulo
						if (ltitoloMod != null && ltitoloMod.getMisureSicurezzaCumulo() != null
								&& ltitoloMod.getMisureSicurezzaCumulo().size() > 0) {
							Vector<MisuraSicurezzaCumuloModel> VecMisSic = new Vector(
									ltitoloMod.getMisureSicurezzaCumulo());

							lCodEsito = lMisSicCtrl.ExInserisciMisuraSicurezzaCumuloWithoutSequence(VecMisSic,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Misura_Sicurezza_Cumulo ",
									lCodEsito);

						}

						// Misura_Cautelare_Cumulo
						if (ltitoloMod != null && ltitoloMod.getMisureCautelariCumulo() != null
								&& ltitoloMod.getMisureCautelariCumulo().size() > 0) {
							Vector<MisuraCautelareCumuloModel> VecMisCau = new Vector(
									ltitoloMod.getMisureCautelariCumulo());

							lCodEsito = lMisCauCtrl.ExInserisciMisuraCautelareCumuloWithoutSequence(VecMisCau,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Misura_Cautelare_Cumulo ",
									lCodEsito);

						}

						// Pena_Accessoria_Cumulo
						if (ltitoloMod != null && ltitoloMod.getPeneAccessorieCumulo() != null
								&& ltitoloMod.getPeneAccessorieCumulo().size() > 0) {
							Vector<PenaAccessoriaCumuloModel> VecPeneAccCau = new Vector(
									ltitoloMod.getPeneAccessorieCumulo());

							lCodEsito = lpenaAccCumCtrl
									.ExInserisciPeneAccessorieCumuloWithoutSequence(VecPeneAccCau, lConn);
							lRapporto += buildRapporto("Inserimento ", " Pena_Accessoria_Cumulo ", lCodEsito);

						}

						// Benefici_Cumulo
						if (ltitoloMod != null && ltitoloMod.getBeneficiCumulo() != null
								&& ltitoloMod.getBeneficiCumulo().size() > 0) {
							Vector<BeneficioCumuloModel> VecBenefici = new Vector(
									ltitoloMod.getBeneficiCumulo());

							lCodEsito = lBeneCumCtrl.ExInserisciBeneficiCumuloWithoutSequence(VecBenefici,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Benefici_Cumulo ", lCodEsito);

						}

						// Stato Esecuzione Titolo Cumulato e relative Notifiche_Cumulo
						Vector<StatoEsecTitoloCumulatoModel> VecStatoEsecCum = null;
						if (ltitoloMod != null && ltitoloMod.getStatoEsecuzioneTitoloCumulato() != null
								&& ltitoloMod.getStatoEsecuzioneTitoloCumulato().size() > 0) {
							VecStatoEsecCum = new Vector(ltitoloMod.getStatoEsecuzioneTitoloCumulato());

							lCodEsito = lStEsecCumCtrl.ExInserisciStatoEsecTitoloCumulatoFullWithoutSequence(
									VecStatoEsecCum, lConn);
							lRapporto += buildRapporto("Inserimento ",
									" Stato_Esecuzione_Titolo_Cumulato e Notifiche_Cumulo ", lCodEsito);

						}

						// Lib_Anticipata_Cumulo e Periodo_Lib_Ant_Cumulo
						StatoEsecTitoloCumulatoModel lStEsecTiCumModel = null;
						Vector<LibAnticipataCumuloModel> VecLibAbtCum = null;

						if (VecStatoEsecCum != null && VecStatoEsecCum.size() > 0) {
							Iterator ItxST = VecStatoEsecCum.iterator();
							while (ItxST.hasNext()) {
								lStEsecTiCumModel = (StatoEsecTitoloCumulatoModel) ItxST.next();
								if (lStEsecTiCumModel != null
										&& lStEsecTiCumModel.getListaLiberazioniAnticipate() != null) {
									VecLibAbtCum = new Vector<>(
											lStEsecTiCumModel.getListaLiberazioniAnticipate());

									lCodEsito = lStEsecCumCtrl
											.ExInserisciLiberazioneAnticipataCumuloFullWithoutSequence(
													VecLibAbtCum, lConn);
									lRapporto += buildRapporto("Inserimento ",
											" Lib_Anticipata_Cumulo e Periodo_Lib_Ant_Cumulo ", lCodEsito);
								}
							}
						}
						// FIXME Caricare qui i LIB_ANTICIPATA_CUMULO collegati alle richieste
						// Verificare NOTIFICA_CUMULO Inserita con stato esecuzione
					} // Chiude // Chiude while(Titoli has next)
					siesLogger.info(
							">>>>> Terminate Insert dei dati Collegati a TITOLO_CUMULATO   ------------------|||||||||||||| ");
				} // Chiude if(lTotaleTitoli!=null && lTotaleTitoli.size()>0)

				siesLogger.debug("Caricamento COMPUTI_CUMULO");
				if (lVecIstruttorie != null && lVecIstruttorie.size() > 0) {
					for (IstruttoriaCumuloModel lIstru : lVecIstruttorie) {
						if (lIstru.getComputiCumulo() != null && lIstru.getComputiCumulo().size() > 0) {
							Vector<ComputiCumuloModel> VecCompCum = new Vector<>(lIstru.getComputiCumulo());

							lCodEsito = lCompCtrl.ExInserisciComputiCumuloWithoutSequence(VecCompCum, lConn);
							lRapporto += buildRapporto("Inserimento ", " Computi_Cumulo ", lCodEsito);
						}
					}
				}

				// =======================================================================
				// Inserimento Tabelle di Relazione con RICHIESTE_PM_IN_CUMULO
				// =======================================================================
				if (lTotaleRicPm != null && lTotaleRicPm.size() > 0) {
					siesLogger.info("--XX-- TOTALE RICHIESTE_PM (Richieste Inviate E non iviate) = "
							+ lTotaleRicPm.size());
					Iterator ItxRpm1 = lTotaleRicPm.iterator();
					while (ItxRpm1.hasNext()) {
						RichiestePmInCumuloModel lRichieste = (RichiestePmInCumuloModel) ItxRpm1.next();
						if (lRichieste != null && lRichieste.getIdRichiestePmInCumulo() != null) {
							lCodEsito = lRichCtrl.ExinserisciTabellediRelazioneWithoutSequence(lRichieste,
									lConn);
							lRapporto += buildRapporto("Inserimento ", " Tabelle di Relazione ", lCodEsito);
						}
					}
				}
				siesLogger.info(
						">>>>> DOPO INSERT Tabelle di Relazione con RICHIESTE_PM_IN_CUMULO   ------------||||||||||| ");
			} // Chiude if(lVecIstruttorie!=null && lVecIstruttorie.size()>0)

			siesLogger.debug("lRapporto = " + lRapporto);
			aMessaggioModel.setRapportoEsito(lRapporto);
			lConn.commit();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					">>>>> DOPO il MESSAGGIO di FINE RAPPORTO     --------------------------|||||||||||||||||||||| ");
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "-" + ex.getStackTrace()[0].getMethodName(), ex);
			// inizio MEV_67 (mando a video un messaggio di errore più parlante)
			String messaggioErrore = ex.getMessage();
			// throw new F3BException(F3BException.USER_MESSAGE, "Errore durante il caricamento dei dati!" +
			// lRapporto);
			throw new F3BException(F3BException.USER_MESSAGE, messaggioErrore + lRapporto);
			// fine MEV_67
		} catch (SQLException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "-" + ex.getStackTrace()[0].getMethodName(), ex);
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore durante il caricamento dei dati!" + lRapporto);
		} catch (F3BException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "-" + ex.getStackTrace()[0].getMethodName(), ex);

			// inizio MEV_67 (mando a video un messaggio di errore più parlante)
			String messaggioErrore = ex.getMessage();
			// throw new F3BException(F3BException.USER_MESSAGE, "Errore durante il caricamento dei dati!" +
			// lRapporto);
			lRapporto += buildRapporto("ERRORE GENERICO", messaggioErrore, ICostantiJMS.ERRORE_CARICAMENTO);
			throw new F3BException(F3BException.USER_MESSAGE, lRapporto);
			// fine MEV_67
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(getClass().getName() + "-" + ex.getStackTrace()[0].getMethodName(), ex);
			rollback(lConn);
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore durante il caricamento dei dati!" + lRapporto);
		} finally {
			cleanup(lProc);
			cleanup(lConn);
		}
		return aMessaggioModel;
	}

	// STUB 01/04/2005 INSERIMENTO AVVOCATI
	protected String inserisciAvvocato(List lAvvocati, Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		AvvocatoDAO lAvvDao = null;
		// AvvocatoModel lAvv = null;
		String lRapporto = new String("");
		for (int i = 0; i < lAvvocati.size(); i++) {
			try {
				Object lObj = lAvvocati.get(i);
				if (lObj instanceof AvvocatoModel) {
					AvvocatoModel aAvvocato = (AvvocatoModel) lAvvocati.get(i);
					lAvvDao = new AvvocatoDAO(lConn);
					lAvvDao.setDAOFromModel(aAvvocato);
					lAvvDao.setWithoutSequence(true);
					lAvvDao.insert();
					lAvvDao.stop();
					lCodEsito = "00000";
				}
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					lCodEsito = "00001";
					lRapporto += buildRapporto("Inserimento ", "degli Avvocati ", lCodEsito);
				} else
					throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire l'Avvocato! ");
			} finally {
				cleanup(lAvvDao);
			}
		}

		return lRapporto;
	}

	protected String inserisciAvvocatoSIEP(List lAvvocatiSIEP, Connection lConn) throws F3BException {

		// String lCodEsito = "00000";
		AvvocatoFascicoloSiepDAO lAvvFasSieDao = null;
		// AvvocatoFascicoloSiepModel lAvvFasSie = null;
		String lRapporto = new String("");
		for (int i = 0; i < lAvvocatiSIEP.size(); i++) {
			try {
				Object lObj = lAvvocatiSIEP.get(i);
				if (lObj instanceof AvvocatoFascicoloSiepModel) {
					AvvocatoFascicoloSiepModel aAvvocatoSIEP = (AvvocatoFascicoloSiepModel) lAvvocatiSIEP
							.get(i);
					lAvvFasSieDao = new AvvocatoFascicoloSiepDAO(lConn);
					lAvvFasSieDao.setDAOFromModel(aAvvocatoSIEP);
					lAvvFasSieDao.setWithoutSequence(true);
					lAvvFasSieDao.insert();
					lAvvFasSieDao.stop();
					// lCodEsito = "00000";
				}
			} catch (DAOException ex) {
				if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
					// lCodEsito = "00001";
					lRapporto += buildRapporto("Inserimento ", "dell'Avvocato Fascicolo SIEP ",
							CHIAVE_DUPLICATA);
				} else
					throw new F3BException(F3BException.USER_MESSAGE,
							"Impossibile inserire l'Avvocato fascicolo SIEP! ");
			} finally {
				cleanup(lAvvFasSieDao);
			}
		}
		return lRapporto;
	}

	// 22/01/2008 Aggiornamento EVENTO
	protected String aggiornaEvento(EventoNotificaModel lEvento, Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		EventoDAO lEveDao = null;
		String lRapporto = new String("");
		try {
			EventoModel lEveMod = lEvento.getEvento();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForValoriIntegritaRef(lEveMod);
			lEveDao.update();
			lEveDao.stop();
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				lCodEsito = "00001";
				lRapporto += buildRapporto("Aggiornamento collegamenti ",
						"dell'Evento " + lEvento.getEvento().getIdEvento(), lCodEsito);
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile aggiornare l'Evento! ");
		} finally {
			cleanup(lEveDao);
		}
		lRapporto += buildRapporto("Aggiornamento ", "dell'Evento " + lEvento.getEvento().getIdEvento(),
				lCodEsito);
		return lRapporto;
	}
	

	/**
	 * Inserisce i dati sulle tabelle:
	 *  - CALCOLO_PENA_DL92
	 *    - SEMESTRI_LA_DL92
	 *    
	 * @param lStorico
	 * @param lConn
	 * @return
	 * @throws F3BException
	 * @since MEV-2026_1 
	 */
    protected String inserisciStoricoCalcPenaDL92 (List <CalcoloPenaDL92ModelDB> lStorico, Connection lConn) throws F3BException {
        siesLogger.debug("Sono in inserisciStoricoCalcPenaDL92");
        
        String lCodEsito = "00000";
        CalcoloPenaDL92DAO lCalcoloDao = null;
        SemestreDL92DAO lSemestreDao = null;

        String lRapporto = new String("");
        

        
        for (CalcoloPenaDL92ModelDB lCalcolo : lStorico) {
            try {
                lCalcoloDao = new CalcoloPenaDL92DAO (lConn);
                lSemestreDao = new SemestreDL92DAO (lConn);
                
                siesLogger.debug("Insert idCalc = "+lCalcolo.getIdCalcoloPenaDL92());
                
                lCalcoloDao.setDAOFromModel(lCalcolo);
                lCalcoloDao.setWithoutSequence(true);
                lCalcoloDao.insert();
                lCalcoloDao.stop();
                lCodEsito = "00000";
                
                
                Vector <SemestreDL92Model> lListaSemestri =  lCalcolo.getListaSemetri();
                if (lListaSemestri!=null) {
                    for (SemestreDL92Model lSemestre : lListaSemestri) {
                        siesLogger.debug("Insert idSemetre = "+lSemestre.getIdSemestriLaDl92());
                        lSemestreDao.setDAOFromModel(lSemestre);
                        lSemestreDao.setWithoutSequence(true);
                        lSemestreDao.insert();
                        lSemestreDao.stop();
                        lCodEsito = "00000"; 
                    }
                } 
            } catch (DAOException ex) {
                if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
                    lCodEsito = "00001";
                    lRapporto += buildRapporto("Inserimento ", " storico pene Virtuali ", lCodEsito);
                } else
                    throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la pena virtuale! ");
            } finally {
                cleanup(lCalcoloDao);                
                cleanup(lSemestreDao);
            }
        }

        return lRapporto;
    }	
}