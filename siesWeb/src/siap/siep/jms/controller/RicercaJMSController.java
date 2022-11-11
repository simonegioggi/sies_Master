package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.controller.SiapController;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.messaggio.model.RootJMSModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoPerTrasferimentoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.dao.MagistratoSqlDAO;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.note.dao.NoteSqlDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.DatiCumuloPerTrasferimentoModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.scambiosanzione.dao.ScambioSanzioneSqlDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.avvocato.dao.AvvocatoFascicoloSiusSqlDAO;
import siap.sius.depositodecreto.dao.DepositoDecretoSqlDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcSqlDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.documentoallegato.dao.DocumentoAllegatoSqlDAO;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaSqlDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.esperto.dao.EspertoSqlDAO;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.dao.FascicoloGPSqlDAO;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloGPTPModel;
import siap.sius.magistratorelatore.dao.MagistratoRelatoreSqlDAO;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.sanzionesostitutiva.dao.PeriodoAltraSanzioneSqlDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.tenore.model.TenoreProvvedimentoModel;

/**
 * <p>
 * Title: RicercaJMSController
 * </p>
 * <p>
 * Description: Controller che genera i meggi model impostati per le richieste di ricerca Asincrona
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RicercaJMSController extends SiapController implements IRicercaJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Costruisce il MEssaggio per la richiesta di un messaggio
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExSpedisciRichiestaRicerca(GenericModel aModel) throws F3BException {

		TreeModel lTreeRoot = null;
		if (aModel instanceof FascicoloSiepModel) {
			lTreeRoot = new TreeModel(createRoot(1));
			TreeModel lTreeFasMod = new TreeModel(aModel);
			lTreeRoot.add(lTreeFasMod);
		}

		MessaggioModel lMessage = new MessaggioModel();
		lMessage.setTreeModel(lTreeRoot);

		return lMessage;
	}

	/**
	 * Ricerca il fascicolo Siep e prepara il messaggio di risposta
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaFascicoloSiep(FascicoloSiepModel aModel) throws F3BException {

		// Ricerco Fascicolo Sogetto e sentenza
		IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFascModel = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aModel);

		TreeModel lTreeRoot = null;
		MessaggioModel lMessage = new MessaggioModel();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("Fascicolo trovato = " + lFascModel);

		if (lFascModel != null) {
			lTreeRoot = new TreeModel(createRoot(1));
			TreeModel lTreeFasMod = new TreeModel(lFascModel);
			lTreeRoot.add(lTreeFasMod);
			lMessage.setCodEsito("10000");
		} else {
			lTreeRoot = new TreeModel(createRoot(0));
			lMessage.setCodEsito("10001");
		}

		lMessage.setTreeModel(lTreeRoot);
		return lMessage;
	}

	/**
	 * Ricerca tutto l'intero fascicolo Siep e prepara il messaggio di risposta o di invio il metodo è infatti
	 * utilizzato sia in caso di trasferimento atti per competenza che nel caso di richiesta.
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaFascicoloSiepPerTrasferimento(FascicoloSiepModel aModel)
			throws F3BException {

		Connection lConn = null;
		TreeModel lTreeRoot = null;
		MessaggioModel lMessage = new MessaggioModel();

		try {
			siesLogger.debug(" SONO NEL METODO  ExRicercaFascicoloSiepPerTrasferimento");

			// Ricerco Fascicolo Soggetto e sentenza
			IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFascModel = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aModel);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Fascicolo trovato = " + lFascModel);

			// 27-05-2009 Il trasferimento del fascicolo ad altra BDI va consentito solo se è validato.
			if (lFascModel != null && lFascModel.getFlagValidato() != null
					&& lFascModel.getFlagValidato().equalsIgnoreCase("S")) {
				DettaglioFascicoloModel lDettFascicolo = null;
				try {
					// ====================================================================
					// Effettua un primo caricamento dei dati utilizzando lo stesso
					// metodo utilizzato per recuperare i dati da presentare sul Dettaglio
					// del fascicolo.
					// Tali dati sono incompleti per il tresferimento e parte dei dati
					// recuperati da tale metodo verranno sovrascritti con quelli
					// più completi
					// ====================================================================
					lDettFascicolo = lFasc.ExDettaglioFascicoloSiep(lFascModel.getIdFascicoloSiep());
					siesLogger.debug(">>>> VALORE DEL CAMPO NOTE 0" + lDettFascicolo.getNoteFascicolo());
					// 10/12/2007 Solo x Trasferimento si aggiungono i dati afferenti al FASCICOLO_SIEP.
					lDettFascicolo = lFasc.ExAltriDatiFascicoloSiep(lDettFascicolo,
							lFascModel.getIdFascicoloSiep());
					siesLogger.debug(">>>> VALORE DEL CAMPO NOTE 1" + lDettFascicolo.getNoteFascicolo());

				} catch (F3BException ex) {
					if (ex.getErrorCode() == F3BException.EX_NOT_FOUND) {
					} else
						throw ex;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Ricerco Eventi per fascicolo SIEP.");

				IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
				Vector lEventi = ricercaEventoNotificaByFascicoloSiepPerTrasferimento(
						lFascModel.getIdFascicoloSiep());

				Vector lEventiNot = null;

				if (lEventi != null && lEventi.size() > 0) {
					lConn = getDBConnection();
					lEventiNot = new Vector();
					Iterator lEveItx = lEventi.iterator();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("INIZIO CICLO WHILE - Numero Eventi = " + lEventi.size());
					while (lEveItx.hasNext()) {
						EventoModel lEveTemp = (EventoModel) lEveItx.next();
						EventoNotificaModel lEveNotModel = lEveCtrl
								.ExRicercaEventoNotificaByKey(lEveTemp.getIdEvento(), lConn);
						// lEveNotModel.getEvento().setDocBlobOut(lEveTemp.getDocBlobOut());Blob a null
						if (lEveTemp.getDocBlobOut() != null)
							lEveNotModel.getEvento()
									.setDocPerTrasferimento(lEveTemp.getDocBlobOut().toByteArray());

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ELABORAZIONE Evento per IdEvento = " + lEveTemp.getIdEvento());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("             e per IdFascicoloSius = "
								+ lEveTemp.getFasSiuIdFascicoloSius());
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.info("Settato BBBLOBBBB = " +
						// lEveNotModel.getEvento().getDocBlobOut().size());

						FascicoloGPTPModel lFasGPTPModel = new FascicoloGPTPModel();
						if (lEveTemp.getFasSiuIdFascicoloSius() != null) {
							// 21/02/2008 Lettura di tutti i dati del Fascicolo SIUS.
							lFasGPTPModel = ricercaDatiFascicoloSius(lFasGPTPModel, lEveTemp, lConn);
						}
						// 09/01/2008 Un Evento non linkato con FAS_SIU_ID_FASCICOLO_SIUS può afferire
						// a provvedimenti inseriti da SIEP (o DEPOSITO_ORDINANZA_SIEP, o
						// DEPOSITO_ORDINANZA_PC, oppure DEPOSITO_DECRETO.)
						// Si integra il FascicoloGPTPModel con i dati di un Provvedimento SIEP.
						else if (lEveTemp.getCodTipoProvvedimento().equals("02"))
							lFasGPTPModel = ricercaProvvedimentoPerEvento(lEveTemp.getIdEvento(),
									lFasGPTPModel, lConn);

						// 09/01/2008 Un Evento può essere collegato a un DOCUMENTO_ALLEGATO.
						// Si integra il FascicoloGPTPModel con i dati del DOCUMENTO_ALLEGATO usando il link
						// EVE_ID_EVENTO.
						lFasGPTPModel = ricercaAllegatoPerEvento(lEveTemp.getIdEvento(), lFasGPTPModel,
								lConn);

						lEveNotModel.setFascicoloGPTP(lFasGPTPModel);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn(
								"ATTENZIONE FascicoloGPTP.getFascicoloSiusModel() caricato in lEveNotModel = "
										+ lFasGPTPModel.getFascicoloSiusModel());
						lEventiNot.add(lEveNotModel);

					}
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("\n\nRicerco Eventi per fascicolo SIEP.\n\n" + lEventiNot.firstElement());

					lDettFascicolo.setEventi(lEventiNot);
				}

				lTreeRoot = new TreeModel(createRoot(2));
				siesLogger.debug(">>>> VALORE DEL CAMPO NOTE 2" + lDettFascicolo.getNoteFascicolo());

				TreeModel lTreeFasMod = new TreeModel(lDettFascicolo);
				lTreeRoot.add(lTreeFasMod);

				// MEV 26 CUMULO Step2 (già MEV 42)
				// -------------------------------------------------------------------------------------------------
				DatiCumuloPerTrasferimentoModel StrutturaCumuloPerTrasferimento = new DatiCumuloPerTrasferimentoModel();
				IstruttoriaCumuloModel IstruttoriaCumulo = null;
				Vector<IstruttoriaCumuloModel> lVecIstru = new Vector();

				TreeModel lTreeStrutturaCumulo = new TreeModel(StrutturaCumuloPerTrasferimento);

				// --------------------------------------------------------------------------------
				// lVec = Insieme dei Codici Motivo_Provvedimento che riguardano il Cumulo NEW
				IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
				DecodificheModel lModel = new DecodificheModel();
				lModel.setContesto("MOTIVO_PROVVEDIMENTO");
				lModel.setCodiceAlternativo("CUMULO_NEW");
				Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));
				// -------------------------------------------------------------------------------

				Boolean Trovato = false;
				if (lEventi != null && lEventi.size() > 0) {
					Iterator lEveItx1 = lEventi.iterator();
					siesLogger.info(
							"INIZIO CICLO WHILE per Eventi_Provvedimento_Cumulo; Numero Eventi Totali = "
									+ lEventi.size());
					while (lEveItx1.hasNext()) {
						Trovato = false;
						EventoModel lEveXCum = (EventoModel) lEveItx1.next();
						Trovato = CercaCodice(lEveXCum, lVec);
						// Per Ogni Evento valido cerco la relativa ISTRUTTORIA_CUMULO
						if (Trovato) {
							siesLogger.info("Evento_Provvedimento_Cumulo Valido - IdEvento = "
									+ lEveXCum.getIdEvento());
							IIstruttoriaCumulo lCtristr = SIEPLookupRemote.getIstruttoriaCumuloRemote();
							IstruttoriaCumulo = lCtristr.ExCercaIsruttoriaPerAltriDatiCumulo(
									lFascModel.getIdFascicoloSiep(), lEveXCum.getIdEvento(), lConn);

							if (IstruttoriaCumulo != null
									&& IstruttoriaCumulo.getIdIstruttoriaCumulo() != null)
								lVecIstru.add(IstruttoriaCumulo);

						}

					}

					// Tutte le Istruttorie legate al Fascicolo sono dentro 'StrutturaCumuloPerTrasferimento'
					if (lVecIstru.size() > 0)
						StrutturaCumuloPerTrasferimento.setListIstruttoriaCumulo(lVecIstru);

				}

				// Viene Aggiunto un SECONDO NODO al TreeRoot per la scrittura del BLOB del MESSAGGIO
				if (StrutturaCumuloPerTrasferimento.getListIstruttoriaCumulo() != null
						&& StrutturaCumuloPerTrasferimento.getListIstruttoriaCumulo().size() > 0) {
					lTreeRoot.add(lTreeStrutturaCumulo);
				}
				// END MEV 26 CUMULO Step2
				// --------------------------------------------------------------------------------------------------------------------------

				lMessage.setCodEsito("10000");
			} else {
				lTreeRoot = new TreeModel(createRoot(0));
				lMessage.setCodEsito("10001");
			}

			// Scrittura del BLOB
			lMessage.setTreeModel(lTreeRoot);
		} catch (F3BException ex) {
			siesLogger.error("F3BException", ex);
			ex.printStackTrace();
		} finally {
			cleanup(lConn);
		}

		return lMessage;
	}

	/**
	 * Crea il MessaggioModel per la richiesta trasmissione atti per competenza. Inserisce nel Blob
	 * (TreeModel) solo i dati essenziali del fascicolo cumulante e dell'ultimo record competenza contenente i
	 * dati della Richiesta.
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaFascicoloSiepPerRichiestaTrasferimento(FascicoloSiepModel aModel)
			throws F3BException {

		Connection lConn = null;
		TreeModel lTreeRoot = null;
		MessaggioModel lMessage = new MessaggioModel();

		try {
			// Ricerco Fascicolo Soggetto e sentenza
			IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFascModel = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aModel);

			//
			DettaglioFascicoloModel lDettFascicolo = new DettaglioFascicoloModel();
			lDettFascicolo.setFascicoloSiep(lFascModel);

			// Ricerco e carico SOLO l'ultima Competenza
			DatiSiepPerTrasferimentoModel lDatiPerTrasf = new DatiSiepPerTrasferimentoModel();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Aggiungo i record COMPETENZA per trasmissione atti cumulo");
			ICompetenza lCtrlCompetenza = SIEPLookupRemote.getCompetenzaRemote();
			Vector lVectCompetenze = lCtrlCompetenza
					.ExRicercaCompetenzaByIdFascicoloSiep(lFascModel.getIdFascicoloSiep());

			lDatiPerTrasf.setListCompetenze(lVectCompetenze);

			lDettFascicolo.setDatiSiepPerTrasferimento(lDatiPerTrasf);

			// Creo la root
			RootJMSModel aRootModel = new RootJMSModel();

			aRootModel.setCodTipoMessaggio("01");
			aRootModel.setCodTipoOperazione("00075");
			aRootModel.setDescrTipoMessaggio("RICHIESTA");
			aRootModel.setDescrTipoOperazione("RICERCA TRASMISSIONE ATTI");
			aRootModel.setEsito("-");

			// Creo il tree model
			lTreeRoot = new TreeModel(aRootModel);
			TreeModel lTreeFasMod = new TreeModel(lDettFascicolo);
			lTreeRoot.add(lTreeFasMod);

			lMessage.setTreeModel(lTreeRoot);
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("RicercaJMSController:", ex);
			ex.printStackTrace();
		} finally {
			cleanup(lConn);
		}

		return lMessage;
	}

	/*
	 * // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo
	 * su ufficio minorenne o meno
	 *
	 * @see
	 * siap.siep.jms.controller.IRicercaJMS#ExRicercaFascicoloSiepPerTrasferimento(siap.siep.fascicolo.model.
	 * FascicoloSiepModel, boolean)
	 */
	public MessaggioModel ExRicercaFascicoloSiepPerTrasferimento(FascicoloSiepModel aModel, String checkMajor)
			throws F3BException {

		Connection lConn = null;
		TreeModel lTreeRoot = null;
		MessaggioModel lMessage = new MessaggioModel();

		try {
			// Ricerco Fascicolo Soggetto e sentenza
			IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFascModel = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aModel,
					checkMajor);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Fascicolo trovato = " + lFascModel);

			// 27-05-2009 Il trasferimento del fascicolo ad altra BDI va consentito solo se è validato.
			if (lFascModel != null && lFascModel.getFlagValidato() != null
					&& lFascModel.getFlagValidato().equalsIgnoreCase("S")) {
				DettaglioFascicoloModel lDettFascicolo = null;
				try {
					// ====================================================================
					// Effettua un primo caricamento dei dati utilizzando lo stesso
					// metodo utilizzato per recuperare i dati da presentare sul Dettaglio
					// del fascicolo.
					// Tali dati sono incompleti per il tresferimento e parte dei dati
					// recuperati da tale metodo verranno sovrascritti con quelli
					// più completi
					// ====================================================================
					lDettFascicolo = lFasc.ExDettaglioFascicoloSiep(lFascModel.getIdFascicoloSiep());

					// 10/12/2007 Solo x Trasferimento si aggiungono i dati afferenti al FASCICOLO_SIEP.
					lDettFascicolo = lFasc.ExAltriDatiFascicoloSiep(lDettFascicolo,
							lFascModel.getIdFascicoloSiep());

				} catch (F3BException ex) {
					if (ex.getErrorCode() == F3BException.EX_NOT_FOUND) {
					} else
						throw ex;
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Ricerco Eventi per fascicolo SIEP.");

				IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
				Vector lEventi = ricercaEventoNotificaByFascicoloSiepPerTrasferimento(
						lFascModel.getIdFascicoloSiep());

				Vector lEventiNot = null;

				if (lEventi != null && lEventi.size() > 0) {
					lConn = getDBConnection();
					lEventiNot = new Vector();
					Iterator lEveItx = lEventi.iterator();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("INIZIO CICLO WHILE - Numero Eventi = " + lEventi.size());
					while (lEveItx.hasNext()) {
						EventoModel lEveTemp = (EventoModel) lEveItx.next();
						EventoNotificaModel lEveNotModel = lEveCtrl
								.ExRicercaEventoNotificaByKey(lEveTemp.getIdEvento(), lConn);
						// lEveNotModel.getEvento().setDocBlobOut(lEveTemp.getDocBlobOut());Blob a null
						if (lEveTemp.getDocBlobOut() != null)
							lEveNotModel.getEvento()
									.setDocPerTrasferimento(lEveTemp.getDocBlobOut().toByteArray());

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ELABORAZIONE Evento per IdEvento = " + lEveTemp.getIdEvento());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("             e per IdFascicoloSius = "
								+ lEveTemp.getFasSiuIdFascicoloSius());
						// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						// posto di LogF3B.getLogger()
						// siesLogger.info("Settato BBBLOBBBB = " +
						// lEveNotModel.getEvento().getDocBlobOut().size());

						FascicoloGPTPModel lFasGPTPModel = new FascicoloGPTPModel();
						if (lEveTemp.getFasSiuIdFascicoloSius() != null) {
							// 21/02/2008 Lettura di tutti i dati del Fascicolo SIUS.
							lFasGPTPModel = ricercaDatiFascicoloSius(lFasGPTPModel, lEveTemp, lConn);
						}
						// 09/01/2008 Un Evento non linkato con FAS_SIU_ID_FASCICOLO_SIUS può afferire
						// a provvedimenti inseriti da SIEP (o DEPOSITO_ORDINANZA_SIEP, o
						// DEPOSITO_ORDINANZA_PC, oppure DEPOSITO_DECRETO.)
						// Si integra il FascicoloGPTPModel con i dati di un Provvedimento SIEP.
						else if (lEveTemp.getCodTipoProvvedimento().equals("02"))
							lFasGPTPModel = ricercaProvvedimentoPerEvento(lEveTemp.getIdEvento(),
									lFasGPTPModel, lConn);

						// 09/01/2008 Un Evento può essere collegato a un DOCUMENTO_ALLEGATO.
						// Si integra il FascicoloGPTPModel con i dati del DOCUMENTO_ALLEGATO usando il link
						// EVE_ID_EVENTO.
						lFasGPTPModel = ricercaAllegatoPerEvento(lEveTemp.getIdEvento(), lFasGPTPModel,
								lConn);

						lEveNotModel.setFascicoloGPTP(lFasGPTPModel);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn(
								"ATTENZIONE FascicoloGPTP.getFascicoloSiusModel() caricato in lEveNotModel = "
										+ lFasGPTPModel.getFascicoloSiusModel());
						lEventiNot.add(lEveNotModel);

					}
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger
							.debug("\n\nRicerco Eventi per fascicolo SIEP.\n\n" + lEventiNot.firstElement());

					lDettFascicolo.setEventi(lEventiNot);
				}

				lTreeRoot = new TreeModel(createRoot(2));
				TreeModel lTreeFasMod = new TreeModel(lDettFascicolo);
				lTreeRoot.add(lTreeFasMod);
				
				// Ticket#20210702015 - In qeuesta rierca non venivano aricati i dati del CUMULO
				// aggiunto come nel metodo:
				// public MessaggioModel ExRicercaFascicoloSiepPerTrasferimento(FascicoloSiepModel aModel)
				// MEV 26 CUMULO Step2 (già MEV 42)
				// -------------------------------------------------------------------------------------------------
				DatiCumuloPerTrasferimentoModel StrutturaCumuloPerTrasferimento = new DatiCumuloPerTrasferimentoModel();
				IstruttoriaCumuloModel IstruttoriaCumulo = null;
				Vector<IstruttoriaCumuloModel> lVecIstru = new Vector();

				TreeModel lTreeStrutturaCumulo = new TreeModel(StrutturaCumuloPerTrasferimento);

				// --------------------------------------------------------------------------------
				// lVec = Insieme dei Codici Motivo_Provvedimento che riguardano il Cumulo NEW
				IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
				DecodificheModel lModel = new DecodificheModel();
				lModel.setContesto("MOTIVO_PROVVEDIMENTO");
				lModel.setCodiceAlternativo("CUMULO_NEW");
				Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));
				// -------------------------------------------------------------------------------

				Boolean Trovato = false;
				if (lEventi != null && lEventi.size() > 0) {
					Iterator lEveItx1 = lEventi.iterator();
					siesLogger.info(
							"INIZIO CICLO WHILE per Eventi_Provvedimento_Cumulo; Numero Eventi Totali = "
									+ lEventi.size());
					while (lEveItx1.hasNext()) {
						Trovato = false;
						EventoModel lEveXCum = (EventoModel) lEveItx1.next();
						Trovato = CercaCodice(lEveXCum, lVec);
						// Per Ogni Evento valido cerco la relativa ISTRUTTORIA_CUMULO
						if (Trovato) {
							siesLogger.info("Evento_Provvedimento_Cumulo Valido - IdEvento = "
									+ lEveXCum.getIdEvento());
							IIstruttoriaCumulo lCtristr = SIEPLookupRemote.getIstruttoriaCumuloRemote();
							IstruttoriaCumulo = lCtristr.ExCercaIsruttoriaPerAltriDatiCumulo(
									lFascModel.getIdFascicoloSiep(), lEveXCum.getIdEvento(), lConn);

							if (IstruttoriaCumulo != null
									&& IstruttoriaCumulo.getIdIstruttoriaCumulo() != null)
								lVecIstru.add(IstruttoriaCumulo);

						}

					}

					// Tutte le Istruttorie legate al Fascicolo sono dentro 'StrutturaCumuloPerTrasferimento'
					if (lVecIstru.size() > 0)
						StrutturaCumuloPerTrasferimento.setListIstruttoriaCumulo(lVecIstru);

				}

				// Viene Aggiunto un SECONDO NODO al TreeRoot per la scrittura del BLOB del MESSAGGIO
				if (StrutturaCumuloPerTrasferimento.getListIstruttoriaCumulo() != null
						&& StrutturaCumuloPerTrasferimento.getListIstruttoriaCumulo().size() > 0) {
					lTreeRoot.add(lTreeStrutturaCumulo);
				}
				// Ticket#20210702015 - FINE
				// --------------------------------------------------------------------------------------------------------------------------
				
				
				
				lMessage.setCodEsito("10000");
			} else {
				lTreeRoot = new TreeModel(createRoot(0));
				lMessage.setCodEsito("10001");
			}

			lMessage.setTreeModel(lTreeRoot);
		} catch (F3BException ex) {
			ex.printStackTrace();
		} finally {
			cleanup(lConn);
		}

		return lMessage;
	}

	/**
	 * Crea la root del tree model
	 *
	 * @param caseSwitch
	 * @return
	 */
	private RootJMSModel createRoot(int caseSwitch) {

		RootJMSModel aModel = new RootJMSModel();

		switch (caseSwitch) {
		case 0: // Elemento NON TROVATO
			aModel.setCodTipoMessaggio("04");
			aModel.setCodTipoOperazione("00030");
			aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
			aModel.setDescrTipoOperazione("RICERCA FASCICOLO");
			aModel.setEsito("NON TROVATO");
			break;
		case 1: // Fascicolo
			aModel.setCodTipoMessaggio("04");
			aModel.setCodTipoOperazione("00030");
			aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
			aModel.setDescrTipoOperazione("RICERCA FASCICOLO");
			aModel.setEsito("TROVATO");
			break;
		case 2: // Fascicolo COmpleto
			aModel.setCodTipoMessaggio("04");
			aModel.setCodTipoOperazione("00050");
			aModel.setDescrTipoMessaggio("RICHIESTA RICERCA");
			aModel.setDescrTipoOperazione("RICERCA FASCICOLO COMPLETA");
			aModel.setEsito("TROVATO");
			break;
		}
		return aModel;
	}

	/**
	 * ricerca Evento Notifica By Fascicolo Siep Per Trasferimento
	 *
	 * @param lKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ricercaEventoNotificaByFascicoloSiepPerTrasferimento(BigDecimal lKeyFascicolo)
			throws F3BException {

		Connection lConn = null;
		EventoPerTrasferimentoSqlDAO lEveDao = null;
		// EventoNotificaModel lEve = null;
		Vector lEventi = null;

		try {
			lConn = getDBConnection();
			lEveDao = new EventoPerTrasferimentoSqlDAO(lConn);
			lEveDao.ricercaEventiByFascicoloSiep(lKeyFascicolo);
			lEventi = new Vector(lEveDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RicercaJMSController.ricercaEventoNotificaByFascicoloSiepPerTrasferimento: " + daoEx);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lEventi;

	}

	/**
	 * 09/01/2008 Ricerca dei Provvedimenti di Sorveglianza iscritti da SIEP per Trasferimento ad altra BDI.
	 * Viene utilizzato ed adeguato allo scopo il FascicoloGPTPModel, che conterrà anche
	 * DECRETO_ORDINANZA_SIEP
	 *
	 * @param aIdEvento
	 *            (BigDecimal)
	 * @param lFasGPTPModel
	 *            (FascicoloGPTPModel): model da caricare
	 * @param lConn
	 *            (Connection)
	 * @return FascicoloGPTPModel - Caricato solo per quanto riguarda la property TenoreProvvedimentoModel[]
	 *         mTenori; - Con i dati di:DEPOSITO_DECRETO, DEPOSITO_ORDINANZA_PC, DECRETO_ORDINANZA_SIEP
	 * @throws F3BException
	 */
	private FascicoloGPTPModel ricercaProvvedimentoPerEvento(BigDecimal aIdEvento,
			FascicoloGPTPModel lFasGPTPModel, Connection lConn) throws F3BException {

		DepositoDecretoSqlDAO lDepDecSqlDAO = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDAO = null;
		DecretoOrdinanzaSiepSqlDAO lDecOrdSiepSqlDAO = null;
		TenoreSqlDAO lTenSqlDao = null;

		TenoreProvvedimentoModel[] lTenoreProvvedimento = new TenoreProvvedimentoModel[1];
		lTenoreProvvedimento[0] = new TenoreProvvedimentoModel();
		Object genericObject = null;
		try {
			// Si tenta di individuare l'eventuale DECRETO.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA DepositoDecreto per IdEvento = " + aIdEvento);
			lDepDecSqlDAO = new DepositoDecretoSqlDAO(lConn);
			lDepDecSqlDAO.ricercaDepositoDecretoByIdEveGeneratoNoDescTipoDecreto(aIdEvento);

			lDepDecSqlDAO.start();
			if (lDepDecSqlDAO.next()) {
				DepositoDecretoModel lDepDecModel = null;
				genericObject = lDepDecSqlDAO.getModelNoDescTipoDecreto();
				if (genericObject != null) {
					lDepDecModel = (DepositoDecretoModel) genericObject;
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.warn("INDIVIDUATO DepositoDecreto per IdEvento ");
					lTenoreProvvedimento[0].setDecreto(lDepDecModel);

					// Occorre recuperare il TENORE legato al Decreto.
					if (lDepDecModel.getIdDepositoDecreto() != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn(
								"FASE di RICERCA Tenore per Decreto " + lDepDecModel.getIdDepositoDecreto());
						lTenSqlDao = new TenoreSqlDAO(lConn);
						lTenSqlDao.ricercaTenoreByDecreto(lDepDecModel.getIdDepositoDecreto());
						if (lTenSqlDao.getModelByKey() != null)
							lTenoreProvvedimento[0].setTenore((TenoreModel) lTenSqlDao.getModelByKey());
					}
				}
			}
			lDepDecSqlDAO.stop();

			// Si tenta di individuare l'eventuale DEPOSITO_ORDINANZA_PC.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA DepositoOrdinanzaPc per IdEvento = " + aIdEvento);
			lDepOrdSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn);
			lDepOrdSqlDAO.ricercaDepositoOrdinanzaPcByIdEveGenerato(aIdEvento);
			DepositoOrdinanzaPcModel lDepOrdModel = null;
			genericObject = lDepOrdSqlDAO.getModelByKey();
			if (genericObject != null) {
				lDepOrdModel = (DepositoOrdinanzaPcModel) genericObject;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("INDIVIDUATO DepositoOrdinanzaPc per IdEvento ");
				lTenoreProvvedimento[0].setOrdinanza(lDepOrdModel);

				// Occorre recuperare il TENORE legato all'Ordinanza.
				if (lDepOrdModel.getIdDepositoOrdinanzaPc() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.warn("FASE di RICERCA Tenore per Ordinanza "
							+ lDepOrdModel.getIdDepositoOrdinanzaPc());
					lTenSqlDao = new TenoreSqlDAO(lConn);
					lTenSqlDao.ricercaTenoreByOrdinanza(lDepOrdModel.getIdDepositoOrdinanzaPc());
					if (lTenSqlDao.getModelByKey() != null)
						lTenoreProvvedimento[0].setTenore((TenoreModel) lTenSqlDao.getModelByKey());
				}
			}

			// Si tenta di individuare l'eventuale DECRETO_ORDINANZA_SIEP.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA DecretoOrdinanzaSiep per IdEvento = " + aIdEvento);
			lDecOrdSiepSqlDAO = new DecretoOrdinanzaSiepSqlDAO(lConn);
			lDecOrdSiepSqlDAO.ricercaDecretoOrdinanzaSiepByIdEvento(aIdEvento);
			genericObject = lDecOrdSiepSqlDAO.getModelByKey();
			DecretoOrdinanzaSiepModel lDecOrdSiepModel = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA DecretoOrdinanzaSiep prima di getModelByKey ");
			if (genericObject != null) {
				lDecOrdSiepModel = (DecretoOrdinanzaSiepModel) genericObject;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("INDIVIDUATO DecretoOrdinanzaSiep per IdEvento ");
				lTenoreProvvedimento[0].setDecOrdSiep(lDecOrdSiepModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RicercaJMSController.ricercaProvvedimentoPerEvento:" + daoEx);
		} finally {
			cleanup(lDepDecSqlDAO);
			cleanup(lDepOrdSqlDAO);
			cleanup(lDecOrdSiepSqlDAO);
			cleanup(lTenSqlDao);
		}
		lFasGPTPModel.setTenori(lTenoreProvvedimento);

		return lFasGPTPModel;
	}

	/**
	 * 09/01/2008 Ricerca del DOCUMENTO_ALLEGATO collegato ad un EVENTO. Viene utilizzato il
	 * FascicoloGPTPModel, che lo conterrà.
	 *
	 * @param aIdEvento
	 * @param lFasGPTPModel
	 * @return FascicoloGPTPModel
	 * @throws F3BException
	 */
	private FascicoloGPTPModel ricercaAllegatoPerEvento(BigDecimal aIdEvento,
			FascicoloGPTPModel lFasGPTPModel, Connection lConn) throws F3BException {

		DocumentoAllegatoSqlDAO lDocAllSqlDAO = null;
		Object genericObject = null;
		try {
			// Si tenta di individuare l'eventuale DOCUMENTO_ALLEGATO.
			lDocAllSqlDAO = new DocumentoAllegatoSqlDAO(lConn);
			lDocAllSqlDAO.ricercaDocumentoAllegatoByIdEvento(aIdEvento);
			List allegati = new ArrayList();
			DocumentoAllegatoModel lAllegatoModel = null;
			genericObject = lDocAllSqlDAO.getModelByKey();
			if (genericObject != null) {
				lAllegatoModel = (DocumentoAllegatoModel) genericObject;
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("INDIVIDUATO DOCUMENTO_ALLEGATO per IdEvento = " + aIdEvento);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("                               con IdDocumentoAllegato = "
						+ lAllegatoModel.getIdDocumentoAllegato());
				if (lAllegatoModel.getIdDocumentoAllegato() != null
						&& lAllegatoModel.getDocBlobOut() != null) {
					lAllegatoModel.setDocPerTrasferimento(lAllegatoModel.getDocBlobOut().toByteArray());
				}
				allegati.add(lAllegatoModel);
				lFasGPTPModel.setAllegati(allegati);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RicercaJMSController.ricercaAllegatoPerEvento:" + daoEx);
		} finally {
			cleanup(lDocAllSqlDAO);
		}

		return lFasGPTPModel;
	}

	/**
	 * 21/02/2008 Ricerca di tutti i dati del Fascicolo SIUS per il Trasferimento ad altra BDI. Viene
	 * introdotto il nuovo DatiSiusPerTrasferimentoModel ed adeguato allo scopo il FascicoloGPTPModel, che lo
	 * conterrà
	 *
	 * I dati caricati nel model sono: FascicoloGPTPModel - FascicoloSiusModel - GeneraleProcedimentoModel -
	 * TenoreProvvedimentoModel[] - [gli allegati non vengono caricati] - DatiSiusPerTrasferimentoModel - List
	 * <ResidenzaAssociataModel> - LuogoDetenzioneModel - MagistratoRelatoreModel - List <AvvocatoSiusModel> -
	 * List <NoteModel> - EsecuzioneSanzioneSostitutivaModel - List <PeriodoAltraSanzioneModel> -
	 * ScambioSanzioneModel - List <>RCPP non caicate
	 *
	 *
	 * @param lFasGPTPModel
	 * @param lConn
	 * @return ricercaAltriDatiFascicoloSius
	 * @throws F3BException
	 */
	private FascicoloGPTPModel ricercaDatiFascicoloSius(FascicoloGPTPModel lFasGPTPModel,
			EventoModel lEveTemp, Connection lConn) throws F3BException {

		DepositoDecretoSqlDAO lDepDecSqlDAO = null;
		DepositoOrdinanzaPcSqlDAO lDepOrdSqlDAO = null;
		TenoreSqlDAO lTenSqlDao = null;
		FascicoloGPSqlDAO lFasGPSqlDao = null;
		ResidenzaSqlDAO lResSqlDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		MagistratoRelatoreSqlDAO lMagRelSqlDao = null;
		MagistratoSqlDAO lMagDao = null;
		EspertoSqlDAO lEspDao = null;
		AvvocatoFascicoloSiusSqlDAO lAvvSqlDao = null;
		NoteSqlDAO lNoteSqlDao = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lESSSqlDao = null;
		PeriodoAltraSanzioneSqlDAO lPASSqlDao = null;
		ScambioSanzioneSqlDAO lSSSqlDao = null;
		SoggettoSqlDAO lSogSqlDao = null; // 21/06/2010

		try {
			// ========================================================================
			// Carica FASCIOLO_SIUS e GENERALE_PROCEDIMENTO
			// ========================================================================
			lFasGPSqlDao = new FascicoloGPSqlDAO(lConn);
			lFasGPSqlDao.ricercaFascicoloByKey(lEveTemp.getFasSiuIdFascicoloSius());
			FascicoloGPModel lFasGPModel = (FascicoloGPModel) lFasGPSqlDao.getModelByKey();

			// 21/06/2010 Caricamento Soggetto SIUS.
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(lFasGPModel.getFascicoloSiusModel().getSogIdSoggetto());
			SoggettoModel lSogModel = (SoggettoModel) lSogSqlDao.getModelByKey();
			lFasGPModel.getFascicoloSiusModel().setSoggetto(lSogModel);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>>>>>>>>>>>>> ricercaDatiFascicoloSius gpmodel sogg =" + lSogModel);

			lTenSqlDao = new TenoreSqlDAO(lConn);
			lTenSqlDao.ricercaTenoreByGeneraleProc(
					lFasGPModel.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			Vector lVectTenori = new Vector(lTenSqlDao.getModels());
			if (lVectTenori != null) {
				TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);

				lFasGPModel.setTenori(lTenoriModel);
			}

			// ========================================================================
			//
			// ========================================================================
			lFasGPTPModel.setFascicoloSiusModel(lFasGPModel.getFascicoloSiusModel());
			lFasGPTPModel.setGeneraleProcedimentoModel(lFasGPModel.getGeneraleProcedimentoModel());

			TenoreProvvedimentoModel[] lTenoriProModel = new TenoreProvvedimentoModel[lVectTenori.size()];
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>>>>>>>>>>>>> ricercaDatiFascicoloSius gptpmodel sogg ="
					+ lFasGPTPModel.getFascicoloSiusModel().getSoggetto());
			for (int j = 0; j < lVectTenori.size(); j++) {
				TenoreProvvedimentoModel lTenProModel = new TenoreProvvedimentoModel();
				lTenProModel.setTenore(lFasGPModel.getTenori()[j]);
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.info("ELABORAZIONE TenoreProvvedimentoModel["+j+"] IdTenore =
				//// "+lTenProModel.getTenore().getIdTenore());
				if (lTenProModel.getTenore().getDepDecIdDepositoDecreto() != null) {
					//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					//// LogF3B.getLogger()
					// siesLogger.info("ELABORAZIONE ricercaDepositoDecretoByKey["+j+"] IdDepositoDecreto =
					//// "+lTenProModel.getTenore().getDepDecIdDepositoDecreto() );
					lDepDecSqlDAO = new DepositoDecretoSqlDAO(lConn);
					lDepDecSqlDAO.ricercaDepositoDecretoByKey(
							lTenProModel.getTenore().getDepDecIdDepositoDecreto());
					lTenProModel.setDecreto((DepositoDecretoModel) lDepDecSqlDAO.getModelByKey());
					lDepDecSqlDAO.stop();
				}
				if (lTenProModel.getTenore().getDepOpidDepositoOrdinanzaPc() != null) {
					//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					//// LogF3B.getLogger()
					// siesLogger.info("ELABORAZIONE ricercaDepositoOrdinanzaPcByKey["+j+"]
					//// IdDepositoOrdinanzaPC = "+lTenProModel.getTenore().getDepOpidDepositoOrdinanzaPc() );
					lDepOrdSqlDAO = new DepositoOrdinanzaPcSqlDAO(lConn);
					lDepOrdSqlDAO.ricercaDepositoOrdinanzaPcByKey(
							lTenProModel.getTenore().getDepOpidDepositoOrdinanzaPc());
					lTenProModel.setOrdinanza((DepositoOrdinanzaPcModel) lDepOrdSqlDAO.getModelByKey());
				}
				// Si carica l'Array di aggregati dei tenori.
				lTenoriProModel[j] = lTenProModel;
			}
			lFasGPTPModel.setTenori(lTenoriProModel);

			// Caricamento dati Residenze e Domicili del FASCICOLO SIUS x Trasferimento.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA RESIDENZA FASCICOLO SIUS ");
			lResSqlDao = new ResidenzaSqlDAO(lConn);
			lResSqlDao.ricercaResidenzeDomiciliByFascicoloSius(
					lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius());
			lResSqlDao.start();

			Vector lResidenze = new Vector();

			ResidenzaAssociataModel lResAssMod = null;
			ResidenzaModel lResMod = null;
			ResidenzaFascicoloSiusModel lResFasSius = null;
			while (lResSqlDao.next()) {
				lResAssMod = new ResidenzaAssociataModel();

				lResMod = (ResidenzaModel) lResSqlDao.getModel();
				lResFasSius = lResSqlDao.getModelResidenzaFascicoloSius();

				lResAssMod.setResidenza(lResMod);
				lResAssMod.setResidenzaFascicoloSius(lResFasSius);
				lResidenze.add(lResAssMod);
			}
			lResSqlDao.stop();
			lFasGPTPModel.getDatiSiusPerTrasferimento().setListResidenzaFasSius(lResidenze);

			// Caricamento del LuogoDetenzione.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA LUOGO DETENZIONE ");
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDetSqlDao.ricercaLuogoDetenzioneCorrenteByFascicoloSius(
					lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius());
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();

			if (lLuogoDetenzione != null)
				lFasGPTPModel.getDatiSiusPerTrasferimento().setLuogoDetenzione(lLuogoDetenzione);

			// Caricamento del Magistrato Relatore.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA MAGISTRATO RELATORE ");
			lMagRelSqlDao = new MagistratoRelatoreSqlDAO(lConn);
			lMagRelSqlDao.ricercaMagistratoRelatoreCorrenteByFascicolo(
					lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius());
			MagistratoRelatoreModel lMagRelMod = (MagistratoRelatoreModel) lMagRelSqlDao.getModelByKey();

			if (lMagRelMod != null && lMagRelMod.getMagCodMagistrato() != null) {
				// Magistrato
				if (lMagRelMod.getMagCodMagistrato() != null) {
					MagistratoModel lMagMod;
					lMagDao = new MagistratoSqlDAO(lConn);
					lMagDao.ricercaMagistratoByCod(lMagRelMod.getMagCodMagistrato());
					lMagMod = (MagistratoModel) lMagDao.getModelByKey();
					lMagRelMod.setMagistrato(lMagMod);
				}
				// Esperto
				if (lMagRelMod.getEspIdEsperto() != null) {
					EspertoModel lEspMod;
					lEspDao = new EspertoSqlDAO(lConn);
					lEspDao.ricercaEspertoByKey(lMagRelMod.getEspIdEsperto());
					lEspMod = (EspertoModel) lEspDao.getModelByKey();
					lMagRelMod.setEsperto(lEspMod);
				}
				lFasGPTPModel.getDatiSiusPerTrasferimento().setMagistratoRelatore(lMagRelMod);
			}

			// Caricamento degli AVVOCATI Fascicolo Sius.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA AVVOCATI SIUS");
			Vector lAvvocati = new Vector();
			lAvvSqlDao = new AvvocatoFascicoloSiusSqlDAO(lConn);
			lAvvSqlDao.ricercaAvvocatiByFascicolo(lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius());

			lAvvSqlDao.start();
			while (lAvvSqlDao.next()) {
				lAvvocati.add(lAvvSqlDao.getModel());
			}
			lAvvSqlDao.stop();

			if (lAvvocati.size() > 0)
				lFasGPTPModel.getDatiSiusPerTrasferimento().setListAvvocatiFasSius(lAvvocati);

			// Ricerca delle Note per ID Fascicolo SIUS.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA NOTE FASCICOLO SIUS");

			Vector lNote = new Vector();
			lNoteSqlDao = new NoteSqlDAO(lConn);

			lNoteSqlDao
					.ricercaNoteByIdFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius());
			lNote = new Vector(lNoteSqlDao.getModels());

			if (lNote.size() > 0)
				lFasGPTPModel.getDatiSiusPerTrasferimento().setNote(lNote);

			// 25/02/2008 Ricerca della ESS per ID Fascicolo SIUS.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA Esecuzione Sanzione Sostitutiva");
			if (lFasGPTPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo("U019") == 0) {
				lESSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
				lESSSqlDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
						lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius());
				EsecuzioneSanzioneSostitutivaModel lESSMod = (EsecuzioneSanzioneSostitutivaModel) lESSSqlDao
						.getModelByKey();
				lFasGPTPModel.getDatiSiusPerTrasferimento().setESS(lESSMod);
			}

			// 25/02/2008 Ricerca dei Periodi Altra Sanzione per ID Fascicolo SIUS.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA Periodi Altra Sanzione");
			if (lFasGPTPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo("U019") == 0) {
				Vector lPAS = new Vector();
				lPASSqlDao = new PeriodoAltraSanzioneSqlDAO(lConn);
				lPASSqlDao.ricercaSanzioneSostitutivaByIdFascicolo(
						lFasGPTPModel.getFascicoloSiusModel().getIdFascicoloSius(), "DESC");
				lPAS = new Vector(lPASSqlDao.getModels());
				if (lPAS.size() > 0)
					lFasGPTPModel.getDatiSiusPerTrasferimento().setPAS(lPAS);
			}
			// 25/02/2008 Ricerca di SCAMBIO_SANZIONE per Fascicolo SIUS.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("FASE di RICERCA Scambio Sanzione");
			if (lFasGPTPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
					.compareTo("U017") == 0) {
				lSSSqlDao = new ScambioSanzioneSqlDAO(lConn);
				ScambioSanzioneModel lSSMod = new ScambioSanzioneModel();
				lSSMod.setChiaveAnnoFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getChiaveAnno());
				lSSMod.setChiaveProgrFascicoloSius(lFasGPTPModel.getFascicoloSiusModel().getChiaveProgr());
				lSSMod.setCodUfficioSorveglianza(lFasGPTPModel.getFascicoloSiusModel().getChiaveUfficio());
				lSSSqlDao.ricercaScambioSanzione(lSSMod);
				lSSMod = (ScambioSanzioneModel) lSSSqlDao.getModelByKey();
				lFasGPTPModel.getDatiSiusPerTrasferimento().setSS(lSSMod);
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RicercaJMSController.ricercaDatiFascicoloSius:" + daoEx);
		} finally {
			cleanup(lDepDecSqlDAO);
			cleanup(lDepOrdSqlDAO);
			cleanup(lFasGPSqlDao);
			cleanup(lTenSqlDao);
			cleanup(lResSqlDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lMagRelSqlDao);
			cleanup(lMagDao);
			cleanup(lEspDao);
			cleanup(lAvvSqlDao);
			cleanup(lNoteSqlDao);
			cleanup(lESSSqlDao);
			cleanup(lPASSqlDao);
			cleanup(lSSSqlDao);
			cleanup(lSogSqlDao); // 21/06/2010
		}

		return lFasGPTPModel;
	}

	/**
	 * Ricerca il fascicolo Siep e prepara il messaggio di risposta
	 *
	 * // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: creo nuovo metodo passando anche il controllo
	 * su ufficio minorenne o meno
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public MessaggioModel ExRicercaFascicoloSiep(FascicoloSiepModel aModel, String checkMajor)
			throws F3BException {

		// Ricerco Fascicolo Sogetto e sentenza
		IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFascModel = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(aModel, checkMajor);

		TreeModel lTreeRoot = null;
		MessaggioModel lMessage = new MessaggioModel();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("Fascicolo trovato = " + lFascModel);

		if (lFascModel != null) {
			lTreeRoot = new TreeModel(createRoot(1));
			TreeModel lTreeFasMod = new TreeModel(lFascModel);
			lTreeRoot.add(lTreeFasMod);
			lMessage.setCodEsito("10000");
		} else {
			lTreeRoot = new TreeModel(createRoot(0));
			lMessage.setCodEsito("10001");
		}

		lMessage.setTreeModel(lTreeRoot);
		return lMessage;
	}

	private Boolean CercaCodice(EventoModel aEve, Vector lVec) throws F3BException {

		Boolean trovato = false;

		DecodificheModel lModelVec = null;
		Iterator Ite1 = lVec.iterator();
		while (Ite1.hasNext()) {
			lModelVec = (DecodificheModel) Ite1.next();
			if (lModelVec.getCode() != null) {
				if (lModelVec.getCode().equals(aEve.getCodMotivo())) {
					trovato = true;
				}
			}
		}

		return trovato;
	}

}