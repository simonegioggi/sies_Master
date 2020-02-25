package siap.sico.stampa.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.avvocato.dao.AvvocatoSiepxStampaSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.decretoordinanza.dao.DecretoOrdinanzaSiepSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.dao.PenaPrecedenteSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaSqlDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.sospensione.dao.SospensioneSqlDAO;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.statoesecuzione.controller.StatoEsecuzioneController;
import siap.util.SIESSwitch;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class StampaCPController extends StampaController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Preleva i dati dal DB per le stampe delle Sanzioni Sostitutive
	 * 
	 * @param aEveModel
	 * @return il treeModel riempito con tutti i dati selezionati
	 * @throws F3BException
	 ****************************************************************************/
	public TreeModel prelevaDatiRichiestaConversione(EventoNotificaModel aEveModel, UtenteModel aUtenteModel)
			throws F3BException {
		TreeModel lTreeRoot = new TreeModel();

		Connection lConn = null;
		StampaEventoUtils lStampaEvento = new StampaEventoUtils();

		FascicoloSiepSqlDAO lFasDao = null;
		EventoSqlDAO lEventoSqlDAO = null;
		AvvocatoSiepxStampaSqlDAO lAvvDao = null;
		PenaPrecedenteSqlDAO lPenPrecDao = null;
		SanzioneSostitutivaSqlDAO lSanDao = null;
		DecretoOrdinanzaSiepSqlDAO lDecSqlDao = null;
		ScadenzarioSqlDAO lScadenzarioSqlDao = null;
		PenaResiduaSqlDAO lPenaResDao = null;
		SospensioneSqlDAO lSospSql = null;
		SospensioneModel lSosp = null;
		SanzioneSostResiduaSqlDAO lSSSqlDAO = null;
		// RichiestaConversioneDAO lRicDao = null;
		RichiestaConversioneSqlDAO lRicSqlDao = null;

		BigDecimal lKeyFascicolo = aEveModel.getEvento().getFasSieIdFascicoloSiep();

		try {
			lConn = getDBConnection();

			// Fascicolo
			lFasDao = new FascicoloSiepSqlDAO(lConn);
			lFasDao.ricercaFascicoloByKey(lKeyFascicolo);
			FascicoloSiepModel lFasModel = (FascicoloSiepModel) lFasDao.getModelByKey();
			// MEV a7-rr-311 riportare anche il vecchio codice RES
			if (lFasModel != null && lFasModel.getCodUfficioInserimento() != null
					&& (lFasModel.getCodOperatoreInserimento().startsWith("res")
							|| lFasModel.getCodOperatoreInserimento().startsWith("RES"))) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("lFasModel.getChiaveProgr()2: " + lFasModel.getChiaveProgr());
				if (lFasModel.getChiaveProgr().intValue() > 1000000)
					lFasModel.setCodiceRES(
							StampaUtils.getCodiceOrigine(lFasModel.getChiaveProgr().toString()));
			}

			// Pena Residua
			lPenaResDao = new PenaResiduaSqlDAO(lConn);
			lPenaResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lKeyFascicolo);
			PenaResiduaModel lPenresMod = (PenaResiduaModel) lPenaResDao.getModelByKey();

			SanzioneSostResiduaModel lSSResiduaModel = null;

			if (lPenresMod != null) {
				// sanzione sostitutiva residua -- dario -- 26-03-2008
				lSSSqlDAO = new SanzioneSostResiduaSqlDAO(lConn);
				lSSSqlDAO.ricercaUltimaSanzioneSostResiduaByIdFasc(lKeyFascicolo, "N");
				lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO.getModelByKey();
				lSSSqlDAO.stop();

				if (lSSResiduaModel == null) { // Probabilmente mi trovo sul primo calcolo per cui la SSR non
												// è validata
					lSSSqlDAO.ricercaUltimaSanzioneSostResiduaByIdFasc(lKeyFascicolo, "S");
					lSSResiduaModel = (SanzioneSostResiduaModel) lSSSqlDAO.getModelByKey();
					lSSSqlDAO.stop();
				}

				lPenresMod.calcolaStringaReclusione();
				lPenresMod.calcolaStringaArresto();
				lPenresMod.calcolaStringaIsolamento();
				if (lPenresMod.getDataFine() != null
						&& lPenresMod.getDataFine().compareTo(DateUtils.getSysDate()) <= 0
						// GDV 23/10/2006 a6-rr-328
						&& lPenresMod.getFlagErgastolo() != null && !lPenresMod.getFlagErgastolo().equals("S")
						&& !lPenresMod.getFlagErgastolo().equals("D")) {
					lPenresMod.setImmediataScarcerazione("S");
				} else {
					if (lPenresMod != null)
						lPenresMod.setImmediataScarcerazione("N");
				}
			}
			lPenaResDao.stop();

			// Sospensione
			lSospSql = new SospensioneSqlDAO(lConn);
			if (lPenresMod != null) {
				lSospSql.ricercaSospensioneByIdPenaResidua(lPenresMod.getIdPenaResidua());
				lSosp = (SospensioneModel) lSospSql.getModelByKey();
			}

			// Creazione del Tree Fascicolo Model
			TreeModel lTreeFasMod = new TreeModel(lFasModel);

			// Aggiunta del nodo SanzioneSostResidua
			if (lSSResiduaModel != null) {
				lSSResiduaModel.calcolaStringaSanzione();
				TreeModel lTreeSSResiduaMod = new TreeModel(lSSResiduaModel);
				lTreeFasMod.add(lTreeSSResiduaMod);
			}

			this.appendTableToFascicoloSiep(lConn, lKeyFascicolo, lTreeFasMod, lFasModel.getFlagAltraCausa());

			TreeModel lTreeEveMod = new TreeModel(aEveModel.getEvento());

			// Aggiunge nodi di evento e notifica
			lTreeRoot = new TreeModel(createRoot(aEveModel, aUtenteModel));
			lTreeRoot.add(new TreeModel(aUtenteModel));

			aEveModel.getEvento().setEventoCorrente("S");
			aEveModel.setEvento(this.mEventoUtils.setEventoDiStampa(aEveModel.getEvento(), lConn));

			// Avvocati
			lAvvDao = new AvvocatoSiepxStampaSqlDAO(lConn);
			lAvvDao.ricercaAvvocatiByFascicolo(lKeyFascicolo);
			Vector lAvvocati = new Vector(lAvvDao.getModels());

			this.mEventoUtils.appendNotifiche(lTreeEveMod, aEveModel, lAvvocati, aUtenteModel);

			TreeModel lVerbale = lStampaEvento.getVerbaleTree(aEveModel, lConn, "05");
			if (lVerbale != null) {
				lTreeEveMod.add(lVerbale);
			}

			lTreeRoot.add(lTreeEveMod);

			// Magistrato
			if (aEveModel.getMagistrato() != null)
				lTreeEveMod.add(new TreeModel(aEveModel.getMagistrato()));

			// Nodi livello 1 Fascicolo - Soggetto - Sentenza
			TreeModel lTreeSogMod = getTreeSoggetto(lFasModel.getSogIdSoggetto(), lKeyFascicolo, lConn, null);
			// TreeModel lTreeSenMod = new TreeModel(this.getTreeSentenza(lFasModel.getSenIdSentenza(),
			// lConn));

			if (lPenresMod != null) // Pena Residua
			{
				TreeModel lTreelPenresMod = new TreeModel(lPenresMod);
				if (lSosp != null) {
					TreeModel lTreeSosp = new TreeModel(lSosp);
					lTreelPenresMod.add(lTreeSosp);

				}
				lTreeFasMod.add(lTreelPenresMod);
			}

			Iterator lItx = null;

			// Add Avvocati per Fascicolo
			if (lAvvocati != null) {
				lItx = lAvvocati.iterator();
				while (lItx.hasNext()) {
					AvvocatoSiepModel lAvvModel = (AvvocatoSiepModel) lItx.next();
					TreeModel lTreeAvvMod = new TreeModel(lAvvModel.getAvvocato());
					lTreeFasMod.add(lTreeAvvMod);
					lTreeAvvMod.add(new TreeModel(lAvvModel.getAvvocatoFascicoloSiepModel()));
				}
			}

			// Scadenzario
			lScadenzarioSqlDao = new ScadenzarioSqlDAO(lConn);
			lScadenzarioSqlDao.ricercaScadenzarioByIdFascicolo(lKeyFascicolo);

			ScadenzarioModel lScadenzario = (ScadenzarioModel) lScadenzarioSqlDao.getModelByKey();

			if (lScadenzario != null) {
				lTreeFasMod.add(new TreeModel(lScadenzario));
			}

			lTreeRoot.add(lTreeFasMod);
			// Richiesta Conversione PenaPec

			// commento non serve più Vector lRichiestaConversioni = new Vector();
			RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
			lRicMod.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());

			// modifiche paolo cherubini 24/01/2011 per portare in stampa le varie descrizioni
			lRicSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRicSqlDao.setCondizioni(lRicMod);
			lRicSqlDao.ricercaRichiestaConversioneByIdFasSIEP(lFasModel.getIdFascicoloSiep());
			lRicMod = (RichiestaConversioneModel) lRicSqlDao.getModelByKey();

			/*
			 * lRicDao = new RichiestaConversioneDAO(lConn); lRicDao.setCondizioni(lRicMod);
			 * lRicDao.setOrderBy(); lRicDao.start(); while ( lRicDao.next() ) { lRichiestaConversioni.add(
			 * (RichiestaConversioneModel)lRicDao.getModel() ); } lRicDao.stop(); if
			 * (lRichiestaConversioni.size() > 0) { lRicMod=
			 * (RichiestaConversioneModel)lRichiestaConversioni.get(0); fine modifiche paolo cherubini
			 */

			TreeModel lTreeRichCon = new TreeModel(lRicMod);
			lTreeRoot.add(lTreeRichCon);
			// commento la parentesi per le suddette modifiche }

			// // cumulo
			if (lFasModel.getFlagCumulante() != null && lFasModel.getFlagCumulante().equals("S")) {
				TreeModel lPenCumModTree = getPenaCumulo(lKeyFascicolo, lConn);
				lTreeRoot.add(lPenCumModTree);
			}

			// ========= FABIO 14-05-2008 - Modifica per correzioni su stampe Sanzioni Sostitutive
			// (Rtitrasmissione Atti)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("====> TRASMISSIONE CP - Prima di appendere lo Stato di Esecuzione = = = "
					+ SIESSwitch.isReworkStatoEsecuzioneOn());
			if (SIESSwitch.isReworkStatoEsecuzioneOn()) {
				// Se sono nel rework dello stato esecuzione chiamo il nuovo
				// controller che gestisce lo stato esec.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Prima di appendere lo Stato di Esecuzione");
				StatoEsecuzioneController lStat = new StatoEsecuzioneController();
				lStat.appendStatoEsecuzione("FULL", lTreeRoot, lConn, lFasModel.getIdFascicoloSiep());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Ho appeso lo Stato di Esecuzione");
			} else
				this.mEventoUtils.appendStatoEsecuzione("FULL", lTreeRoot, lConn,
						lFasModel.getIdFascicoloSiep());
			// =========

			lTreeRoot.add(lTreeSogMod);
			lTreeRoot.add(getTreeSentenza(lFasModel, lConn));
		} catch (DAOException daoEx) {
			daoEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx, daoEx);
			throw new F3BException(this.getClass().getName() + ".prelevaDatiRichiestaConversione: " + daoEx);
		}

		catch (Exception sqe) {
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe, sqe);
			throw new F3BException(this.getClass().getName() + ".prelevaDatiRichiestaConversione: " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lAvvDao);
			cleanup(lEventoSqlDAO);
			cleanup(lSanDao);
			cleanup(lPenPrecDao);
			cleanup(lDecSqlDao);
			cleanup(lPenaResDao);
			cleanup(lSospSql);
			cleanup(lSSSqlDAO);
			// cleanup(lRicDao);
			cleanup(lRicSqlDao);

			cleanup(lConn);
		}

		return lTreeRoot;
	}

}