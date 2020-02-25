package siap.sico.stampa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.dao.MisuraAlternativaPerStatoEsecuzioneSqlDAO;
import siap.sico.misuraalternativa.dao.MisuraAlternativaSqlDAO;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaPrecedenteModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.penaresidua.model.PenaResiduaMisuraAlternativaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.penaresidua.util.PenaResiduaUtil;
import siap.siep.refertoscarcerazione.dao.RefertoScarcerazioneSqlDAO;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import siap.sius.tenore.dao.TenoreEventoSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 *
 * <p>
 * Title: StampaMAUtils
 * </p>
 * <p>
 * Description: Classe di utilità per la stampa delle Misure Alternative
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * not attributable 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaMAUtils extends SIAPStampaController {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private RefertoScarcerazioneModel mRefScarcMod; // lo cerco una volta sola e la setta la seconda

	public StampaMAUtils() {
		mRefScarcMod = null;
	}

	/**
	 * Viene passata la prima misura alternativa trovata per l'evento corrente
	 * 
	 * @param lMisAltMod
	 * @return
	 */
	public TreeModel getMATree(PenaResiduaModel lPenResMod, EventoNotificaModel aEveModel, Connection lConn,
			BigDecimal aIdFascicoloSiep, UtenteModel aUtenteModel) throws F3BException {
		MisuraAlternativaSqlDAO lMisAltDAO = null;
		EventoSqlDAO lEveDAO = null;
		TreeModel lTreeMisAlte = null;
		RefertoScarcerazioneSqlDAO lRefScarcDao = null;
		// Cerco la Misura Alternativa legata all'evento corrente
		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("getMATree ------------>> ");

			lMisAltDAO = new MisuraAlternativaSqlDAO(lConn);

			// Modifiche per stampa trasmissione atti per decreto Ex art 51Bis (146/2013)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("aEveModel.getEvento(): " + aEveModel.getEvento());

			if (aEveModel != null
					&& aEveModel.getEvento() != null
					&& aEveModel.getEvento().getCodMotivo() != null
					&& (aEveModel.getEvento().getCodMotivo().equals("5440") || aEveModel.getEvento()
							.getCodMotivo().equals("5441"))) {
				lMisAltDAO.ricercaMisuraAlternativaCorrenteByIdFascicolo(aIdFascicoloSiep);
			} else
				lMisAltDAO.ricercaMisuraAlternativaCorrenteByIdEvento(aEveModel.getEvento().getEveIdEvento());

			MisuraAlternativaModel lMisAltMod = (MisuraAlternativaModel) lMisAltDAO.getModelByKey();

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// siesLogger.debug("lMisAltMod = "+lMisAltMod);

			if (lMisAltMod != null) {
				lMisAltDAO.stop();
				lMisAltMod.calcolaStringaReclusione();
				lMisAltMod.calcolaStringaRevocaArresto();
				lMisAltMod.calcolaStringaRevocaReclusione();
				lMisAltMod.calcolaStringaMisura();
				lMisAltMod.setMisuraAlternativaCorrente("S");
				lTreeMisAlte = new TreeModel(lMisAltMod);

				if (lMisAltMod.getChiaveUfficioFascicoloSius() != null
						&& lMisAltMod.getChiaveUfficioFascicoloSius().length() > 0
						&& !lMisAltMod.getChiaveUfficioFascicoloSius().equals("-")) {
					UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(lMisAltMod
							.getChiaveUfficioFascicoloSius());

					// TODO completare e testare
					// C.S. 08/10/2015 richiesta di Michele
					// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso
					// il Tribunale per minorenni" in "Magistrato di Sorveglianza per i minorenni"
					if (aUtenteModel != null
							&& aUtenteModel.getUfficioUtente() != null
							&& aUtenteModel.getUfficioUtente().getCodTipoUfficio() != null
							&& !aUtenteModel.getUfficioUtente().getCodTipoUfficio().equals("")
							&& (aUtenteModel.getUfficioUtente().getCodTipoUfficio().equals("PM")
									|| aUtenteModel.getUfficioUtente().getCodTipoUfficio().equals("PMM") || aUtenteModel
									.getUfficioUtente().getCodTipoUfficio().equals("PGCAP"))) {
						if (lUff.getCodTipoUfficio() != null && !lUff.getCodTipoUfficio().equals("")
								&& lUff.getCodTipoUfficio().equals("UDSM")) {
							lMisAltMod
									.setDescrUfficioSorveglianza("Magistrato di Sorveglianza per i Minorenni"
											+ " " + lUff.getDescrComune());
						} else {
							lMisAltMod.setDescrUfficioSorveglianza(lUff.getDescrTipoUfficio() + " "
									+ lUff.getDescrComune());
						}
					} else {
						lMisAltMod.setDescrUfficioSorveglianza(lUff.getDescrTipoUfficio() + " "
								+ lUff.getDescrComune());
					}
					lTreeMisAlte.add(new TreeModel(lUff));
				}

				if (lMisAltMod.getChiaveUfficioFascicoloSiusMaAt() != null
						&& lMisAltMod.getChiaveUfficioFascicoloSiusMaAt().length() > 0
						&& !lMisAltMod.getChiaveUfficioFascicoloSiusMaAt().equals("-")) {
					try {
						UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(lMisAltMod
								.getChiaveUfficioFascicoloSiusMaAt());
						lMisAltMod.setDescrChiaveUfficioFascicoloSiusMaAt(lUff.getDescrTipoUfficio() + " "
								+ lUff.getDescrComune());
					} catch (Exception e) {
					}
				}

				// per calcolare il quantum tra il fine pena e il fine misura
				if (lMisAltMod.getDataFineMisura() != null && lPenResMod != null
						&& lPenResMod.getDataFine() != null && !lPenResMod.isErgastolo()) {
					Date lNuovoInizio = DateUtils.getDayAfter(lMisAltMod.getDataFineMisura());

					PenaResiduaModel lPenModel = PenaResiduaUtil.calcolaPenaNuovaDataInizio(lNuovoInizio,
							lPenResMod, false);
					PenaResiduaMisuraAlternativaModel lRimanentePena = new PenaResiduaMisuraAlternativaModel();

					lRimanentePena.setNumAnniArresto(lPenModel.getNumAnniArresto());
					lRimanentePena.setNumMesiArresto(lPenModel.getNumMesiArresto());
					lRimanentePena.setNumGiorniArresto(lPenModel.getNumGiorniArresto());
					lRimanentePena.setNumAnniReclusione(lPenModel.getNumAnniReclusione());
					lRimanentePena.setNumMesiReclusione(lPenModel.getNumMesiReclusione());
					lRimanentePena.setNumGiorniReclusione(lPenModel.getNumGiorniReclusione());
					lRimanentePena.calcolaStringaReclusione();
					lRimanentePena.calcolaStringaArresto();

					TreeModel lTreeRimanente = new TreeModel(lRimanentePena);
					lTreeMisAlte.add(lTreeRimanente);

				}

				// Caso del Rigetto Differimento devo recuperare anche il tenore
				if (aEveModel.getEvento().getCodMotivo() != null
						&& aEveModel.getEvento().getCodMotivo().equals("0354")) {
					TenoreEventoSqlDAO lTenDAo = new TenoreEventoSqlDAO(lConn);
					lTenDAo.ricercaTenorebyIdEvento(aEveModel.getEvento().getEveIdEvento());
					TenoreModel lTen = (TenoreModel) lTenDAo.getModelByKey();
					if (lTen != null)
						lTreeMisAlte.add(new TreeModel(lTen));
				}

				// 51 bis
				if (aEveModel.getEvento().getCodMotivo() != null
						&& (aEveModel.getEvento().getCodMotivo().equals("5450") // Affidamento TDS 51bis su
																				// reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5451") // Affidamento TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5452") // Affidamento TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5430") // Affidamento MDS
																						// 51bis
								|| aEveModel.getEvento().getCodMotivo().equals("5431") // Affidamento MDS
																						// 51bis
								|| aEveModel.getEvento().getCodMotivo().equals("5432") // Affidamento MDS
																						// 51bis
								//
								|| aEveModel.getEvento().getCodMotivo().equals("5453") // Detenzione Dom TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5454") // Detenzione Dom TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5455") // Detenzione Dom TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5456") // Detenzione Dom TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5433") // Detenzione Dom MDS
																						// 51bis
								|| aEveModel.getEvento().getCodMotivo().equals("5434") // Detenzione Dom MDS
																						// 51bis
								|| aEveModel.getEvento().getCodMotivo().equals("5435") // Detenzione Dom MDS
																						// 51bis
								|| aEveModel.getEvento().getCodMotivo().equals("5436") // Detenzione Dom MDS
																						// 51bis
								//
								|| aEveModel.getEvento().getCodMotivo().equals("5459") // Detenzione Dom a
																						// Termine TDS 51bis
																						// su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5439") // Detenzione Dom a
																						// Termine MDS 51bis
								//
								|| aEveModel.getEvento().getCodMotivo().equals("5457") // Semilibertà TDS
																						// 51bis su reclamo PM
								|| aEveModel.getEvento().getCodMotivo().equals("5437") // Semilibertà MDS
																						// 51bis
								//
								|| aEveModel.getEvento().getCodMotivo().equals("5442") // Indultino MDS 51bis
								//
								// || aEveModel.getEvento().getCodMotivo().equals("0363") // Esecuzione Presso
								// Domicilio
								|| aEveModel.getEvento().getCodMotivo().equals("5458") // Esecuzione Presso
																						// Domicilio TDS su
																						// reclamo PM
						|| aEveModel.getEvento().getCodMotivo().equals("5438") // Esecuzione Presso Domicilio
																				// MDS 51bis
						)) {
					lMisAltMod.setIs51Bis("S");
				} else
					lMisAltMod.setIs51Bis("N");

				// In questo caso testo l'esistenza di altre Misure Alternative a cui
				// il template puo' fare riferimento
				// Questa ricerca serve solo per un template. /siep/ma/SIEP_MA_DETERM_PROROGA.rtf
				// ricerca detenzione domiciliare a termine
				if (aEveModel.getEvento().getTemIdTemplate() != null
						&& aEveModel.getEvento().getTemIdTemplate().indexOf("SIEP_MA_DETERM_PROROGA") > 0) {
					// MisuraAlternativaModel lMisuraAlternativa = new MisuraAlternativaModel();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info("ENTRO PAOLO getMATree ------------>> ");
					/*
					 * paolo cherubini 15/06/2010 modifico il caricamento della misura alternativa precedente.
					 * Carico tutte le Misure Alternative (sono ordinate per data Desc) le scorro e salvo la
					 * Detenzione Domiciliare a rottura di codice, ossia quando trovo una Misura che non è
					 * Detenzione Domiciliare (DD) esco dal ciclo inserisco l'ultima trovata nel treemodel (in
					 * realtà è la prima DD delle ultime consecutive)
					 */

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info(
							"getFasSieIdFascicoloSiep " + aEveModel.getEvento().getFasSieIdFascicoloSiep());

					lMisAltDAO.ricercaMisuraAlternativaByIdFascicolo(aEveModel.getEvento()
							.getFasSieIdFascicoloSiep());
					// lMisAltDAO.ricercaMADetenzioneDomATermineByByIdFascicolo(aEveModel.getEvento().getFasSieIdFascicoloSiep());
					// lMisuraAlternativa = (MisuraAlternativaModel) lMisAltDAO.getModelByKey();

					/*
					 * setto il MisuraAlternativaCorrente a 'T' per indicare che si tratta dell'ultima
					 * concessione di detenzione domiciliare a termine
					 */
					/*
					 * ATTENZIONE : se tra 2 concessioni di detenzione domiciliare a termine si trova una
					 * qualsiasi altra misura alternativa si dovrà prendere nella proroga l'ultima concessione
					 * di detenzione domiciliare a termine
					 */

					/*
					 * paolo cherubini 15/06/2010 ariattenzione, non mi sembra proprio cosi devo caricare la
					 * prima delle ultime detenzioni domiciliari leggi sopra
					 */
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info("vMisAlt ");
					Vector vMisAlt = new Vector(lMisAltDAO.getModels());
					Iterator lItx = null;
					if (vMisAlt != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.info("vMisAlt size " + vMisAlt.size());
						lItx = vMisAlt.iterator();
						MisuraAlternativaPrecedenteModel lDetDomPrec = null;
						while (lItx.hasNext()) {
							MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel(
									(MisuraAlternativaModel) lItx.next());
							if (lMisAlt.getCodNaturaDecisione().equals("DD")) {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info("lMisuraAlternativa = " + lMisAlt);
								lMisAlt.setMisuraAlternativaCorrente("T");
								lDetDomPrec = new MisuraAlternativaPrecedenteModel(lMisAlt);
							} else {
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.info("break ");
								break;
							}
						}
						if (lDetDomPrec != null) {
							lTreeMisAlte.add(new TreeModel(lDetDomPrec));
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.info("lDetDomPrec = " + lDetDomPrec);
							return lTreeMisAlte;
						}
					}

					lMisAltDAO.stop();

					/*
					 * paolo cherubini 15/06/2010 ariattenzione, commento questo pezzo x quanto detto sopra if
					 * (lMisuraAlternativa != null && lMisuraAlternativa.getIdMisuraAlternativa() != null) {
					 * lMisuraAlternativa.setMisuraAlternativaCorrente("T");
					 * 
					 * MisuraAlternativaPrecedenteModel lDetDomPrec = new
					 * MisuraAlternativaPrecedenteModel(lMisuraAlternativa); //Inserisco tutto sotto la misura
					 * alternativa lTreeMisAlte.add(new TreeModel(lDetDomPrec));
					 * 
					 * return lTreeMisAlte; }
					 */

				} // La Misura Alternativa Esiste
			} else { // La Misura Alternativa non Esiste
						// cerco il referto di scarcerazione
				lRefScarcDao = new RefertoScarcerazioneSqlDAO(lConn);
				lRefScarcDao.ricercaRefertoScarcerazioneByEveIdEvento(aEveModel.getEvento().getEveIdEvento());
				mRefScarcMod = (RefertoScarcerazioneModel) lRefScarcDao.getModelByKey();
				// Cerco il Referto ma non l'inserisco nell'albero di stampa dato che va al di sotto
				// del fascicolo.

				if (mRefScarcMod != null) {
					// Esite il Referto di scarcerazione cerco anche la MA legata all'evento provvedimento
					// dato che non viene legata direttamente all'evento del referto di scarcerazione

					// Ricerca MA per id evento
					lMisAltDAO = new MisuraAlternativaSqlDAO(lConn);
					lMisAltDAO.ricercaMisuraAlternativaByIdEvento(aEveModel.getEvento().getIdEvento());
					MisuraAlternativaModel lMisMod = (MisuraAlternativaModel) lMisAltDAO.getModelByKey();
					if (lMisMod != null) {
						// if (lTreeMisAlte != null)
						// lTreeMisAlte.add(new TreeModel(lMisMod));
						// else
						lTreeMisAlte = new TreeModel(lMisMod);
					}
				}
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Eccezione: ", ex);
			throw new F3BException("Errore durante getMATree " + ex);
		} finally {
			cleanup(lMisAltDAO);
			cleanup(lEveDAO);
			cleanup(lRefScarcDao);
		}

		return lTreeMisAlte;

		// ---Questo va fatto fuori lTreePenResMod.add(lTreeMisAlte);
	}

	/**
	 * 04/06/2010 Viene passato PenaResiduaModel e IdFascicoloSIEP.
	 * 
	 * @param lMisAltMod
	 * @return
	 */
	public TreeModel getMATree(PenaResiduaModel lPenResMod, BigDecimal aIdFascicoloSiep, Connection lConn)
			throws F3BException {
		MisuraAlternativaSqlDAO lMisAltDAO = null;
		EventoSqlDAO lEveDAO = null;
		TreeModel lTreeMisAlte = null;
		// Cerco le occorrenze di Misura Alternativa legate al Fascicolo SIEP
		try {
			lMisAltDAO = new MisuraAlternativaSqlDAO(lConn);
			lMisAltDAO.ricercaMisuraAlternativaByIdFascicolo(aIdFascicoloSiep);
			Vector lMisure = new Vector(lMisAltDAO.getModels());
			Iterator itx = lMisure.iterator();

			while (itx.hasNext()) {
				MisuraAlternativaModel lMisura = (MisuraAlternativaModel) itx.next();

				if (lMisura != null) {
					lMisAltDAO.stop();
					lMisura.calcolaStringaReclusione();
					lMisura.calcolaStringaRevocaArresto();
					lMisura.calcolaStringaRevocaReclusione();
					lMisura.calcolaStringaMisura();
					lMisura.setMisuraAlternativaCorrente("S");
					lTreeMisAlte = new TreeModel(lMisura);

					if (lMisura.getChiaveUfficioFascicoloSius() != null
							&& lMisura.getChiaveUfficioFascicoloSius().length() > 0
							&& !lMisura.getChiaveUfficioFascicoloSius().equals("-")) {
						UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(lMisura
								.getChiaveUfficioFascicoloSius());
						lMisura.setDescrUfficioSorveglianza(lUff.getDescrTipoUfficio() + " "
								+ lUff.getDescrComune());
						lTreeMisAlte.add(new TreeModel(lUff));
					}

					if (lMisura.getChiaveUfficioFascicoloSiusMaAt() != null
							&& !lMisura.getChiaveUfficioFascicoloSiusMaAt().equals("")
							&& lMisura.getChiaveUfficioFascicoloSiusMaAt().length() > 0
							&& !lMisura.getChiaveUfficioFascicoloSiusMaAt().equals("-")) {
						try {
							UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(lMisura
									.getChiaveUfficioFascicoloSiusMaAt());
							lMisura.setDescrChiaveUfficioFascicoloSiusMaAt(lUff.getDescrTipoUfficio() + " "
									+ lUff.getDescrComune());
						} catch (Exception e) {
						}
					}

					// per calcolare il quantum tra il fine pena e il fine misura
					if (lMisura.getDataFineMisura() != null
							&& (lPenResMod != null && lPenResMod.getDataFine() != null)) {
						Date lNuovoInizio = DateUtils.getDayAfter(lMisura.getDataFineMisura());

						PenaResiduaModel lPenModel = PenaResiduaUtil.calcolaPenaNuovaDataInizio(lNuovoInizio,
								lPenResMod, false);
						PenaResiduaMisuraAlternativaModel lRimanentePena = new PenaResiduaMisuraAlternativaModel();

						lRimanentePena.setNumAnniArresto(lPenModel.getNumAnniArresto());
						lRimanentePena.setNumMesiArresto(lPenModel.getNumMesiArresto());
						lRimanentePena.setNumGiorniArresto(lPenModel.getNumGiorniArresto());
						lRimanentePena.setNumAnniReclusione(lPenModel.getNumAnniReclusione());
						lRimanentePena.setNumMesiReclusione(lPenModel.getNumMesiReclusione());
						lRimanentePena.setNumGiorniReclusione(lPenModel.getNumGiorniReclusione());
						lRimanentePena.calcolaStringaReclusione();
						lRimanentePena.calcolaStringaArresto();

						TreeModel lTreeRimanente = new TreeModel(lRimanentePena);
						lTreeMisAlte.add(lTreeRimanente);
					}

					// Caso del Rigetto Differimento devo recuperare anche il tenore
					lEveDAO = new EventoSqlDAO(lConn);
					lEveDAO.ricercaEventoByKey(lMisura.getEveIdEvento());
					EventoModel aEveModel = (EventoModel) lEveDAO.getModelByKey();

					if ("0354".equals(aEveModel.getCodMotivo())) {
						TenoreEventoSqlDAO lTenDAO = new TenoreEventoSqlDAO(lConn);
						lTenDAO.ricercaTenorebyIdEvento(aEveModel.getEveIdEvento());
						TenoreModel lTen = (TenoreModel) lTenDAO.getModelByKey();
						if (lTen != null)
							lTreeMisAlte.add(new TreeModel(lTen));
					}

					// In questo caso testo l'esistenza di altre Misure Alternative a cui
					// il template puo' fare riferimento
					// Questa ricerca serve solo per un template. /siep/ma/SIEP_MA_DETERM_PROROGA.rtf
					// ricerca detenzione domiciliare a termine
					if (aEveModel.getTemIdTemplate() != null
							&& aEveModel.getTemIdTemplate().indexOf("SIEP_MA_DETERM_PROROGA") > 0) {
						MisuraAlternativaModel lMisuraAlternativa = new MisuraAlternativaModel();
						lMisAltDAO.ricercaMADetenzioneDomATermineByByIdFascicolo(aEveModel
								.getFasSieIdFascicoloSiep());
						lMisuraAlternativa = (MisuraAlternativaModel) lMisAltDAO.getModelByKey();
						lMisAltDAO.stop();

						/*
						 * setto il MisuraAlternativaCorrente a 'T' per indicare che si tratta dell'ultima
						 * concessione di detenzione domiciliare a termine
						 */
						/*
						 * ATTENZIONE : se tra 2 concessioni di detenzione domiciliare a termine si trova una
						 * qualsiasi altra misura alternativa si dovrà prendere nella proroga l'ultima
						 * concessione di detenzione domiciliare a termine
						 */
						if (lMisuraAlternativa != null && lMisuraAlternativa.getIdMisuraAlternativa() != null) {
							lMisuraAlternativa.setMisuraAlternativaCorrente("T");

							MisuraAlternativaPrecedenteModel lDetDomPrec = new MisuraAlternativaPrecedenteModel(
									lMisuraAlternativa);
							// Inserisco tutto sotto la misura alternativa
							lTreeMisAlte.add(new TreeModel(lDetDomPrec));

							return lTreeMisAlte;
						}
					} // La Misura Alternativa Esiste
				}
			}
		} catch (Exception ex) {
			throw new F3BException("Errore durante getMATree" + ex);
		} finally {
			cleanup(lMisAltDAO);
			cleanup(lEveDAO);
		}

		return lTreeMisAlte;

	}

	/**
	 * getRefertoScarcerazioneTree
	 * 
	 * @param lPenResMod
	 * @param aEveModel
	 * @param lConn
	 * @return
	 * @throws F3BException
	 * @throws Exception
	 */
	public TreeModel getRefertoScarcerazioneTree(PenaResiduaModel lPenResMod, EventoNotificaModel aEveModel,
			Connection lConn) throws F3BException, Exception {
		TreeModel lTreeRefScarc = null;
		if (mRefScarcMod != null) { // Prendo il Referto Scarcerazione trovato prima
			lTreeRefScarc = new TreeModel(mRefScarcMod);
		}
		return lTreeRefScarc;
	}

	/**
	 * setMAPrecedente - Cerca la MA precedente Sospensione 51 Ter
	 * 
	 * @param aEveModel
	 * @param lConn
	 * @return
	 * @throws Exception
	 */
	public TreeModel setMAPrecedente(TreeModel lPenaTree, BigDecimal aKeyFascicolo, String aNomeTemplate,
			Connection lConn) throws Exception {
		MisuraAlternativaPrecedenteModel lMisuraPrecedente = null;
		MisuraAlternativaPerStatoEsecuzioneSqlDAO lMisSospSqlDAO = null;
		TreeModel lTreePrec = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("MAAAA  *** Nome Template " + aNomeTemplate);
		try {
			// INserire i criteri per cui selezionare la MA precedente
			// INDU_RIPR
			// PERDEF
			// SIEP_MA_RIPR_AFFI
			// SIEP_MA_VARIAZIONE_DECSCA 29/09/2010
			// SIEP_MA_ESECDOM 26/11/2010

			// AMBROSINO 29/09/2010 : Per Stampa di "Variazione Data Misura" (Eventi 5414 e 5415)
			// è stato aggiunto il codice '2005' nella query

			if (aNomeTemplate != null
					&& ((aNomeTemplate.indexOf("SIEP_MA_PERDEF") > 0)
							|| (aNomeTemplate.indexOf("SIEP_MA_INDU_RIPR") > 0)
							|| (aNomeTemplate.indexOf("SIEP_MA_RIPR_AFFI") > 0)
							|| (aNomeTemplate.indexOf("SIEP_MA_VARIAZIONE_DECSCA") > 0) || (aNomeTemplate
							.indexOf("SIEP_MA_ESECDOM") > 0))) {
				lMisSospSqlDAO = new MisuraAlternativaPerStatoEsecuzioneSqlDAO(lConn);

				// ==================================================================
				// add MEV 29 - nel caso di Ripristino Affidamento si ricerca il
				// provvedimento di Sospensione per CODICE, recuperando i codici
				// dal Decodifiche
				String[] lCodici = null;
				if (aNomeTemplate.indexOf("SIEP_MA_RIPR_AFFI") > 0) {
					Collection lCodiciSospensione = DecodificheManager.getInstance()
							.getMotivoProvvedimentoSospProvvMAffP();

					lCodici = new String[lCodiciSospensione.size()];
					Iterator lIter = lCodiciSospensione.iterator();
					int i = 0;
					while (lIter.hasNext()) {
						DecodificheModel lDecodeModel = (DecodificheModel) lIter.next();
						lCodici[i] = lDecodeModel.getCode();
						i++;
					}
				}

				lMisSospSqlDAO.ricercaMisuraAlternativaPrecedenteSosp(aKeyFascicolo, lCodici);
				MisuraAlternativaModel lMisu = new MisuraAlternativaModel();
				lMisu = (MisuraAlternativaModel) lMisSospSqlDAO.getModelByKey();
				if (lMisu != null) {
					lMisuraPrecedente = new MisuraAlternativaPrecedenteModel(lMisu);
					lTreePrec = new TreeModel(lMisuraPrecedente);
				}
			}
		} catch (Exception ex) {
			throw new F3BException("Errore durante getMATree" + ex);
		} finally {
			cleanup(lMisSospSqlDAO);
		}
		return lTreePrec;
	}
}