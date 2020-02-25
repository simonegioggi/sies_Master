package siap.siep.penapecuniaria.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.dao.MagistratoCompetenteDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.note.dao.NoteDAO;
import siap.sico.note.model.NoteModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiepDAO;
import siap.sico.residenza.model.ResidenzaFascicoloSiepModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.stampa.controller.StampaCPController;
import siap.sico.template.controller.TemplateManager;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.dao.AnnotazioneManualeDAO;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.dao.AutoritaEsternaDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.dao.AvvocatoFascicoloSiepDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.beneficio.dao.BeneficioDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.dao.RiaperturaFascicoloSiepDAO;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.parametro.dao.ParametroSqlDAO;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.dao.PenaComplessivaDAO;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penapecuniaria.dao.RichiestaConversioneDAO;
import siap.siep.penapecuniaria.dao.RichiestaConversioneSqlDAO;
import siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostitutivaDAO;
import siap.siep.scambiosanzione.dao.ScambioSanzioneDAO;
import siap.siep.scambiosanzione.model.ScambioSanzioneModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoDAO;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.dao.DepositoDecretoDAO;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.dao.DepositoOrdinanzaPcDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.generaleprocedimento.dao.GeneraleProcedimentoSqlDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.scadenzario.action.ICostantiScadenzarioSius;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: RichiestaConversioneController
 * </p>
 * <p>
 * Description: Classe Controller per RichiestaConversione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class RichiestaConversioneController extends SiapController implements IRichiestaConversione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua l'inserimento di un RichiestaConversione a partire dai dati contenuti nel Model
	 * 
	 * @param aRichiestaConversione
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExInserisciRichiestaConversione(
			RichiestaConversioneModel aRichiestaConversione) throws F3BException {
		Connection lConn = null;

		RichiestaConversioneDAO lRicDao = null;
		RichiestaConversioneModel lRicMod = null;

		try {
			lConn = getDBConnection();

			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaConversione);

			BigDecimal lSequence = lRicDao.insert();

			// Se iscrive SIUS viene inserito lo Scadenzario.
			inserimentoScadenzario(aRichiestaConversione, lConn);

			commit(lConn);

			lRicMod = new RichiestaConversioneModel(aRichiestaConversione);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestaConversione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaConversioneController.ExInserisciRichiestaConversione: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException("RichiestaConversioneController.ExInserisciRichiestaConversione: " + e);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}

		return lRicMod;
	}

	/**
	 * Effettua l'inserimento di un RichiestaConversione a partire dai dati contenuti nel Model inserisce
	 * anche l'evento a cui la richiesta viene collegata
	 * 
	 * @param aRichiestaConversione
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExInserisciRichiestaConversione(
			RichiestaConversioneModel aRichiestaConversione, EventoModel aEvento,
			DettaglioFascicoloModel aDettaglioFascicolo) throws F3BException {
		Connection lConn = null;

		RichiestaConversioneDAO lRicDao = null;
		RichiestaConversioneModel lRicMod = null;

		EventoDAO lEveDao = null;

		PenaComplessivaDAO lPenComDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		// AnnotazioneManualeDAO lAnnDao = null;

		try {
			lConn = getDBConnection();

			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lIdEvento = lEveDao.insert();

			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaConversione);
			lRicDao.setEveIdEvento(lIdEvento);
			BigDecimal lSequence = lRicDao.insert();

			// Se iscrive SIUS viene inserito lo Scadenzario.
			inserimentoScadenzario(aRichiestaConversione, lConn);

			// 05/02/2015 Abolizione aggiornamento della pena residua.
			/*
			 * // Paolo Cherubini 01/06/2011 devo detrarre gli importi inseriti per la richiesta dalla pena
			 * residua //=============================================== //Pena Residua
			 * //===============================================
			 * 
			 * lPenResDao = new PenaResiduaDAO(lConn); lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			 * PenaResiduaModel lPenResMod = new PenaResiduaModel();
			 * 
			 * BigDecimal lkeyFasI =aDettaglioFascicolo.getFascicoloSiep().getIdFascicoloSiep();
			 * lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lkeyFasI); lPenResMod =
			 * (PenaResiduaModel) lPenResSqlDao.getModelByKey(); BigDecimal lkeyPenI = null; int aAmmenda = 0;
			 * int aMulta = 0;
			 * 
			 * if (lPenResMod != null && lPenResMod.getEveIdEvento() != null) { // esiste la pena residua
			 * detraggo gli importi convertiti e scrivo un nuovo record pena residua if
			 * (lPenResMod.getImportoAmmenda() != null) aAmmenda = lPenResMod.getImportoAmmenda().intValue();
			 * if (aRichiestaConversione.getImportoAmmenda() != null) aAmmenda = aAmmenda -
			 * aRichiestaConversione.getImportoAmmenda().intValue();
			 * 
			 * if (lPenResMod.getImportoMulta() != null) aMulta = lPenResMod.getImportoMulta().intValue(); if
			 * (aRichiestaConversione.getImportoMulta() != null) aMulta = aMulta -
			 * aRichiestaConversione.getImportoMulta().intValue();
			 * 
			 * lPenResMod.setImportoAmmenda(new BigDecimal(aAmmenda)); lPenResMod.setImportoMulta(new
			 * BigDecimal(aMulta)); lPenResMod.setEveIdEvento(lEveClassI); lPenResMod.setFlagValidato("S");
			 * lPenResMod.setFasSieIdFascicoloSiep(lkeyFasI);
			 * lPenResMod.setDataInserimento(DateUtils.getSysDate()); lPenResDao.setDAOFromModel(lPenResMod);
			 * lkeyPenI =lPenResDao.insert();
			 * 
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > pena residua esistente1");
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > pena residua scritta = "+lkeyPenI);
			 * //LogF3B.getLogger(
			 * ).debug("Paolo ---- > scritto pena residua per classe I = "+lPenResDao.getFasSieIdFascicoloSiep
			 * ());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lPenResDao.getImportoAmmenda());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > scritto aMulta = "+lPenResDao.getImportoMulta());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > legata ad evento = "+lPenResDao.getEveIdEvento());
			 * //lPenResDao.stop();
			 * 
			 * }else{ // non esiste la pena residua detraggo gli importi convertiti dalla pena complessiva //
			 * e scrivo un nuovo record pena residua if
			 * (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva
			 * ().getPenaComplessiva().getImportoAmmenda()!= null) aAmmenda =
			 * aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva
			 * ().getPenaComplessiva().getImportoAmmenda().intValue(); if
			 * (aRichiestaConversione.getImportoAmmenda()!= null) aAmmenda = aAmmenda -
			 * aRichiestaConversione.getImportoAmmenda().intValue();
			 * 
			 * if
			 * (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getImportoMulta
			 * ()!= null) aMulta =
			 * aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva
			 * ().getImportoMulta().intValue(); if (aRichiestaConversione.getImportoMulta() != null) aMulta =
			 * aMulta - aRichiestaConversione.getImportoMulta().intValue();
			 * 
			 * lPenResMod.setImportoAmmenda(new BigDecimal(aAmmenda)); lPenResMod.setImportoMulta(new
			 * BigDecimal(aMulta)); lPenResMod.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			 * lPenResMod.setEveIdEvento(lEveClassI); lPenResMod.setFlagValidato("S");
			 * lPenResMod.setCodOperatoreAggiornamento(aEvento.getCodOperatoreInserimento());
			 * lPenResMod.setCodUfficioAggiornamento(aEvento.getCodUfficioInserimento());
			 * lPenResMod.setDataAggiornamento(DateUtils.getSysDate());
			 * lPenResDao.setDAOFromModel(lPenResMod); lkeyPenI =lPenResDao.insert();
			 * 
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > pena residua non esistente");
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > pena residua scritta = "+lkeyPenI);
			 * //LogF3B.getLogger(
			 * ).debug("Paolo ---- > scritto pena residua per classe I = "+lPenResDao.getFasSieIdFascicoloSiep
			 * ());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lPenResDao.getImportoAmmenda());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > scritto aMulta = "+lPenResDao.getImportoMulta());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //siesLogger.debug("Paolo ---- > legata ad evento = "+lPenResDao.getEveIdEvento());
			 * //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * //lPenResDao.stop(); } siesLogger.debug("AnnotazioneManualeModel"); // scrivo una
			 * annotazione manuale con gli importi da detrarre AnnotazioneManualeModel lAnnMod = new
			 * AnnotazioneManualeModel(); lAnnMod.setCodTipoAnnotazione("014"); // altro
			 * lAnnMod.setFlagPiuMeno("-"); lAnnMod.setFlagConforme("-"); lAnnMod.setFlagValidato("S");
			 * lAnnMod.setCodFonte("-"); lAnnMod.setCodSottonumerazione("-");
			 * lAnnMod.setCodCausaleComputo("-"); lAnnMod.setCodDpr("-"); lAnnMod.setFlagAppProvvisoria("-");
			 * lAnnMod.setMotivazioni("conversione pena pecuniaria"); lAnnMod.setNumAnniReclusione(new
			 * BigDecimal(0)); lAnnMod.setNumMesiReclusione(new BigDecimal(0));
			 * lAnnMod.setNumGiorniReclusione(new BigDecimal(0)); lAnnMod.setNumAnniArresto(new
			 * BigDecimal(0)); lAnnMod.setNumMesiArresto(new BigDecimal(0)); lAnnMod.setNumGiorniArresto(new
			 * BigDecimal(0));
			 * 
			 * lAnnMod.setImportoAmmenda(aRichiestaConversione.getImportoAmmenda());
			 * lAnnMod.setImportoMulta(aRichiestaConversione.getImportoMulta());
			 * lAnnMod.setEveIdEvento(lEveClassI); lAnnMod.setFasSieIdFascicoloSiep(lkeyFasI);
			 * lAnnMod.setPenResIdPenaResidua(lkeyPenI);
			 * 
			 * lAnnMod.setCodOperatoreInserimento (aEvento.getCodOperatoreInserimento());
			 * lAnnMod.setCodUfficioInserimento (aEvento.getCodUfficioInserimento());
			 * lAnnMod.setDataInserimento (DateUtils.getSysDate());
			 * 
			 * lAnnDao = new AnnotazioneManualeDAO(lConn); lAnnDao.setDAOFromModel(lAnnMod); lAnnDao.insert();
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto annotazione per classe I = "+lAnnMod.
			 * getFasSieIdFascicoloSiep());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lAnnMod.getImportoAmmenda());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aMulta = "+lAnnMod.getImportoMulta());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > legata ad evento = "+lAnnMod.getEveIdEvento());
			 * //lAnnDao.stop();
			 */// 05/02/2015 Fine abolizione aggiornamento della pena residua.

			// 05/02/2015 Eventuale aggiornamento del fascicolo di classe VII nel rif.
			// FAS_SIE_IDFASCICOLOSIEP.
			int lFascProg = aDettaglioFascicolo.getFascicoloSiep().getChiaveProgr().intValue();
			if (lFascProg > 70000 && lFascProg < 80000
					&& aDettaglioFascicolo.getFascicoloSiep().getFasSieIdFascicoloSiep() != null) {
				FascicoloSiepDAO lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.selCondizioneUpdate(aDettaglioFascicolo.getFascicoloSiep().getIdFascicoloSiep());
				lFascDao.setFasSieIdFascicoloSiep(aDettaglioFascicolo.getFascicoloSiep()
						.getFasSieIdFascicoloSiep());
				lFascDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreInserimento());
				lFascDao.setCodUfficioAggiornamento(aEvento.getCodUfficioInserimento());
				lFascDao.setDataAggiornamento(DateUtils.getSysDate());

				lFascDao.update();
				lFascDao.stop();
				cleanup(lFascDao);
			}

			// 20/02/2015 Inserimento della pena complessiva per il fascicolo di classe VII.
			if (lFascProg > 70000 && lFascProg < 80000) {
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenComDao = new PenaComplessivaDAO(lConn);
				PenaResiduaModel lPenResMod = new PenaResiduaModel();
				PenaComplessivaModel lPenComMod = new PenaComplessivaModel();

				BigDecimal lkeyFasI = aDettaglioFascicolo.getFascicoloSiep().getFasSieIdFascicoloSiep();
				int aAmmenda = 0;
				int aMulta = 0;
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lkeyFasI);
				if (lPenResSqlDao.next()) {
					lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

					if (lPenResMod != null && lPenResMod.getEveIdEvento() != null) {
						// esiste la pena residua del Fascicolo di Classe I, la imposto come Pena Complessiva
						// del classe VII.
						if (lPenResMod.getImportoAmmenda() != null)
							aAmmenda = lPenResMod.getImportoAmmenda().intValue();

						if (lPenResMod.getImportoMulta() != null)
							aMulta = lPenResMod.getImportoMulta().intValue();
					}
				}
				lPenComMod.setImportoAmmenda(new BigDecimal(aAmmenda));
				lPenComMod.setImportoMulta(new BigDecimal(aMulta));
				lPenComMod.setFasSieIdFascicoloSiep(aDettaglioFascicolo.getFascicoloSiep()
						.getIdFascicoloSiep());
				lPenComMod.setDataInserimento(DateUtils.getSysDate());
				lPenComMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
				lPenComMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
				lPenComDao.setDAOFromModel(lPenComMod);
				lPenComDao.insert();

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(" ---- > pena complessiva scritta = "+lkeyFasI);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(" ---- > scritto pena complessiva per classe VII = "+lPenComDao.getFasSieIdFascicoloSiep());
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(" ---- > scritto aAmmenda = "+lPenComDao.getImportoAmmenda());
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(" ---- > scritto aMulta = "+lPenComDao.getImportoMulta());
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				// siesLogger.debug(" ---- > legata ad evento = "+lPenComDao.getEveIdEvento());
				// lPenComDao.stop();

				// 23/02/2015 Inserimento della pena residua con importi a zero per il Fascicolo di classe
				// VII.
				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
				lPenResMod = new PenaResiduaModel();

				lkeyFasI = aDettaglioFascicolo.getFascicoloSiep().getIdFascicoloSiep();
				lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lkeyFasI);
				lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();

				if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
					lPenResDao.setIdPenaResidua(lPenResMod.getIdPenaResidua());
					lPenResDao.setEveIdEvento(aEvento.getIdEvento());
					lPenResDao.setFlagValidato("S");

					lPenResDao.setCodOperatoreAggiornamento(aEvento.getCodOperatoreInserimento());
					lPenResDao.setCodUfficioAggiornamento(aEvento.getCodUfficioInserimento());
					lPenResDao.setDataAggiornamento(DateUtils.getSysDate());

					lPenResDao.selByKey();
					lPenResDao.update();
					lPenResDao.stop();
				} else {
					lPenResDao.setFlagValidato("S");
					lPenResDao.setEveIdEvento(aEvento.getIdEvento());

					lPenResDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
					lPenResDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
					lPenResDao.setDataInserimento(DateUtils.getSysDate());

					lPenResDao.insert();
					lPenResDao.stop();
				}
			}
			commit(lConn);

			lRicMod = new RichiestaConversioneModel(aRichiestaConversione);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			lRicMod.setIdRichiestaConversione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaConversioneController.ExInserisciRichiestaConversione: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException("RichiestaConversioneController.ExInserisciRichiestaConversione: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lRicDao);

			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lPenComDao);
			// cleanup(lAnnDao);

			cleanup(lConn);
		}

		return lRicMod;
	}

	/**
	 * Effettua la ricerca dei dati RichiestaConversione
	 * 
	 * @param aRichiestaConversione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		Vector lRichiestaConversioni = new Vector();
		RichiestaConversioneDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.setCondizioni(aRichiestaConversione);
			lRicDao.setOrderBy();
			lRicDao.start();
			while (lRicDao.next()) {
				lRichiestaConversioni.add((RichiestaConversioneModel) lRicDao.getModel());
			}
			lRicDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiestaConversione: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
		return lRichiestaConversioni;
	}

	/**
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExRicercaRichiestaConversioneById(BigDecimal aIdRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		RichiestaConversioneModel lRichiestaConversioneMod = new RichiestaConversioneModel();
		RichiestaConversioneSqlDAO lRichiestaConversioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaConversioneSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRichiestaConversioneSqlDao.ricercaRichiestaConversioneByKey(aIdRichiestaConversione);
			lRichiestaConversioneMod = (RichiestaConversioneModel) lRichiestaConversioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiestaConversioneById:  "
					+ daoEx);
		} finally {
			cleanup(lRichiestaConversioneSqlDao);
			cleanup(lConn);
		}

		return lRichiestaConversioneMod;
	}

	/**
	 * Effettua la ricerca per chiave evitando il filtro sull'evento
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdSenzaEvento(
			BigDecimal aIdRichiestaConversione) throws F3BException {
		Connection lConn = null;
		RichiestaConversioneModel lRichiestaConversioneMod = new RichiestaConversioneModel();
		RichiestaConversioneSqlDAO lRichiestaConversioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaConversioneSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRichiestaConversioneSqlDao.ricercaRichiestaConversioneByKeySenzaEvento(aIdRichiestaConversione);
			lRichiestaConversioneMod = (RichiestaConversioneModel) lRichiestaConversioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiestaConversioneByIdSenzaEvento:  " + daoEx);
		} finally {
			cleanup(lRichiestaConversioneSqlDao);
			cleanup(lConn);
		}

		return lRichiestaConversioneMod;
	}

	/**
	 * 29/09/2015 Ricerca l'Evento di Provv. Richiesta Conversione della Sorveglianza con StringArray di
	 * inclusione per COD_MOTIVO e COD_TIPO_PROVVEDIMENTO ed esclusione per COD_ESITO. La condizione sullo
	 * stato di validazione viene specificata nel model
	 * 
	 * @param aModel
	 * @param aTipoProv
	 * @param aCodMotivo
	 * @param aCodEsito
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoSorvDiRichiestaConversione(String aIdRicConv, EventoModel aModel,
			String[] aTipoProv, String[] aCodMotivo, String[] aCodEsito) throws F3BException {
		Connection lConn = null;

		EventoSqlDAO lDao = null;
		EventoModel lEvento = null;
		BigDecimal lIdEvento = null;
		try {
			lConn = getDBConnection();
			lDao = new EventoSqlDAO(lConn);
			lDao.ricercaEventoValidoByRicConvFasSiusCodProvvCodMotivo(aIdRicConv, aModel, aTipoProv,
					aCodMotivo, aCodEsito);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("Eseguita la query");
			lDao.start();
			if (lDao.next())
				lIdEvento = lDao.getBigDecimal("ID_EVENTO");
			lDao.stop();

			if (lIdEvento != null) {
				lDao = new EventoSqlDAO(lConn);
				lDao.ricercaEventoByKey(lIdEvento);
				lEvento = new EventoModel((EventoModel) lDao.getModelByKey());
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("Eseguita la getModel");
		} catch (Exception e) {
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaEventoSorvDiRichiestaConversione: " + e);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}

		return lEvento;
	}

	/**
	 * Metodo che modifica i dati della RichiestaConversione. Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aRichiestaConversione
	 *            Model con i nuovi valori
	 * @throws F3BException
	 */
	public void ExModificaRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		RichiestaConversioneDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaConversione);
			lRicDao.selCondizioneUpdate(aRichiestaConversione.getIdRichiestaConversione());
			lRicDao.update();

			inserimentoScadenzario(aRichiestaConversione, lConn);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaConversioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Metodo che modifica (se non valorizzata) la DataDeposito della RichiestaConversione. La
	 * RichiestaConversione viene individuata attraverso la relazione col Fascicolo SIEP.
	 * 
	 * @param aIdFascicoloSIEP
	 *            Model con i nuovi valori
	 * @throws F3BException
	 */
	public void ExModificaDataDepositoRichiestaConversione(BigDecimal aIdRichiestaConversione,
			Date lDataEmissione) throws F3BException {
		Connection lConn = null;
		RichiestaConversioneDAO lRicDao = null;

		try {
			lConn = getDBConnection();
			RichiestaConversioneModel RCMod = this.ExRicercaRichiestaConversioneById(aIdRichiestaConversione);
			if (RCMod.getDataDeposito() == null) {
				lRicDao = new RichiestaConversioneDAO(lConn);
				lRicDao.setDAOFromModel(RCMod);
				lRicDao.setDataDeposito(lDataEmissione);
				lRicDao.selCondizioneUpdate(aIdRichiestaConversione);
				lRicDao.update();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RichiestaConversioneController.ExModificaDataDepositoRichiestaConversione: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	public void ExModificaRichiestaConversione(RichiestaConversioneModel aRicConvMod,
			FascicoloSiepModel aFasSiepMod) throws F3BException {
		Connection lConn = null;
		GeneraleProcedimentoModel lGenMod = null;
		RichiestaConversioneDAO lRicDao = null;
//		FascicoloSiusDAO lFasSiusDao = null;
		GeneraleProcedimentoSqlDAO lGenDao = null;
		NoteDAO lNoteDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.setDAOFromModel(aRicConvMod);
			lRicDao.selCondizioneUpdate(aRicConvMod.getIdRichiestaConversione());
			lRicDao.update();

			inserimentoScadenzario(aRicConvMod, lConn);

			// 07/09/2015 Inserimento Nota di Modifica Richiesta Conversione per Fascicoli SIUS con
			// COD_OGGETTO_PROCEDIMENTO = U070,
			// Previa Lettura Generale_Procedimento.
			lGenDao = new GeneraleProcedimentoSqlDAO(lConn);
			lGenDao.ricercaGeneraleProcedimentoByIdFas(aRicConvMod.getFasSiuIdFascicoloSius());
			lGenMod = (GeneraleProcedimentoModel) lGenDao.getModelByKey();
			if (lGenMod.getCodOggettoProcedimento() != null
					&& lGenMod.getCodOggettoProcedimento().compareTo("U070") == 0) {
				NoteModel lNoteMod = new NoteModel();
//				BigDecimal lKeyRes = null;

				lNoteMod.setData(DateUtils.getSysDate());
				lNoteMod.setDescrizione("MODIFICATA LA RICHIESTA CONVERSIONE per il fascicolo SIEP "
						+ aFasSiepMod.getChiaveAnno() + "/" + aFasSiepMod.getChiaveProgr() + " "
						+ aRicConvMod.getDescrUfficioInserimento()
						+ " Successivamente all’iscrizione del procedimento SIUS");
				lNoteMod.setCodOperatoreInserimento(aRicConvMod.getCodOperatoreInserimento());
				lNoteMod.setDataInserimento(aRicConvMod.getDataInserimento());
				lNoteMod.setCodUfficioInserimento(aRicConvMod.getCodUfficioInserimento());
				lNoteMod.setFasSiuIdFascicoloSius(aRicConvMod.getFasSiuIdFascicoloSius());
				lNoteMod.setFasSieIdFascicoloSiep(aRicConvMod.getFasSieIdFascicoloSiep());

				lNoteDao = new NoteDAO(lConn);
				lNoteDao.setDAOFromModel(lNoteMod);
				/*lKeyRes = */lNoteDao.insert();
			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RichiestaConversioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRicDao);
			cleanup(lNoteDao);
			cleanup(lGenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la cancellazione del record
	 * 
	 * @param aRichiestaConversione
	 * @throws F3BException
	 */
	public void ExCancellaRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		RichiestaConversioneDAO lRicDao = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.selCondizioneUpdate(aRichiestaConversione.getIdRichiestaConversione());
			lRicDao.delete();

			// Se esiste, si cancella l'occorrenza dallo scadenzario.
			if (aRichiestaConversione.getFasSiuIdFascicoloSius() != null) {
				lScaDao = new ScadenzarioSiusDAO(lConn);
				lScaDao.setCondizioniByIdFascicoloTipo(aRichiestaConversione.getFasSiuIdFascicoloSius(),
						ICostantiScadenzarioSius.TIPO_SCA_RICHIESTA_CONVERSIONE_PP);
				lScaDao.delete();
			}

			commit(lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException("RichiestaConversioneController.ExCancellaRichiestaConversione: " + daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lConn);
		}
	}

	/**
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aRichiestaConversione
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountRichiestaConversione(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		RichiestaConversioneSqlDAO lRichiestaConversioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaConversioneSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRichiestaConversioneSqlDao.getCountRichiestaConversione(aRichiestaConversione);
			lRichiestaConversioneSqlDao.start();
			lRichiestaConversioneSqlDao.next();
			lCount = lRichiestaConversioneSqlDao.getBigDecimal("HowManyRecords");
			lRichiestaConversioneSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExGetCountRichiestaConversione: " + daoEx);
		} finally {
			cleanup(lRichiestaConversioneSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/**
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aRichiestaConversione
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiestaConversionePaged(RichiestaConversioneModel aRichiestaConversione,
			int aPage) throws F3BException {
		Connection lConn = null;
		Vector lRichiestaConversioni = new Vector();
		RichiestaConversioneSqlDAO lRichiestaConversioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaConversioneSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRichiestaConversioneSqlDao.ricercaRichiestaConversionePaged(aRichiestaConversione, aPage);
			lRichiestaConversioni = new Vector(lRichiestaConversioneSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiestaConversionePaged: "
					+ daoEx);
		} finally {
			cleanup(lRichiestaConversioneSqlDao);
			cleanup(lConn);
		}
		return lRichiestaConversioni;
	}

	/**
	 * controller utilizzato per l'inserimento di un fascicolo di Conversione delle pene pecuniare (CLasse
	 * VII) a partire da un Fascicolo pena detentiva (CLasse I) duplica il soggetto per non incorrere
	 * nell'errore sulla chiave univoca fascicolo-soggetto-sentenza e comunque in linea con le nuove
	 * disposizione del SuperSoggetto. inserisce evento di conversione sul Fascicolo pena detentiva (CLasse I)
	 * inserisce fascicolo di Conversione delle pene pecuniare (CLasse VII) inserisce rischiesta di
	 * conversione inserisce evento di conversione anche sul Fascicolo di classe VII riporta oltre i dati del
	 * fascicolo la posizione giuridica e gli avvocati
	 * 
	 * @param: aSoggetto Model con i dati da inserire aEvento Model con i dati da inserire aFascicoloSiep
	 *         Model con i dati da inserire aRichiestaConversione Model con i dati da inserire
	 * @return il model aRichiestaConversione con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExInserisciRichiestaConversionedaClasseI(SoggettoModel aSoggetto,
			EventoModel aEvento, FascicoloSiepModel aFascicoloSiep,
			DettaglioFascicoloModel aDettaglioFascicolo, RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		RichiestaConversioneModel lRicMod = null;

		SoggettoDAO lSogDao = null;
		EventoDAO lEveDao = null;
		FascicoloSiepDAO lFascDao = null;
		FascicoloSiepSqlDAO lFascDaoSql = null;
		RichiestaConversioneDAO lRicDao = null;
		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		MagistratoCompetenteDAO lMagComDao = null;
		ReatoDAO lReaDao = null;
		CircostanzaDAO lCirDao = null;
		PenaComplessivaDAO lPenDao = null;
		SanzioneSostitutivaDAO lSanDao = null;

		// Paolo Cherubini 27/04/2011
		AvvocatoFascicoloSiepDAO lAvvFasDao = null;
		PenaResiduaDAO lPenResDao = null;
		PenaResiduaSqlDAO lPenResSqlDao = null;
		AnnotazioneManualeDAO lAnnDao = null;

		try {
			lConn = getDBTransaction();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Paolo ---- > ExInserisciRichiestaConversionedaClasseI");
			BigDecimal lkeyFasI = aDettaglioFascicolo.getFascicoloSiep().getIdFascicoloSiep(); // salvo id
																								// procedimento
																								// classe I

			// **************************************************//
			// CLASSE I //
			// **************************************************//

			// --------------------------------------------------//
			// PENA RESIDUA //
			// --------------------------------------------------//

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Paolo ---- > procedimento classe I = " + lkeyFasI);

			// inserisco l'evento di conversione nel fascicolo di classe I
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lEveClassI = lEveDao.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Paolo ---- > scritto evento per classe I = " + lEveClassI);

			/*
			 * 23/02/2015 sulla pena del classe I non deve più aggiornare la pena residua, né scrivere una
			 * annotazione manuale con gli importi da detrarre.
			 * 
			 * // Paolo Cherubini 03/05/2011 // quando inserisco una richiesta conversione devo detrarre gli
			 * importi indicati, dalla pena residua // o dalla pena in sentenza se questa non esiste. // per
			 * cui scrivo un record pena residua con la differenza // e una annotazione manuale con gli
			 * importi da detrarre
			 * 
			 * //=============================================== //Pena Residua
			 * //=============================================== lPenResDao = new PenaResiduaDAO(lConn);
			 * lPenResSqlDao = new PenaResiduaSqlDAO(lConn); PenaResiduaModel lPenResMod = new
			 * PenaResiduaModel();
			 * 
			 * lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lkeyFasI); lPenResMod =
			 * (PenaResiduaModel) lPenResSqlDao.getModelByKey(); BigDecimal lkeyPenI = null; int aAmmenda = 0;
			 * int aMulta = 0;
			 * 
			 * if (lPenResMod != null && lPenResMod.getEveIdEvento() != null) { // esiste la pena residua
			 * detraggo gli importi convertiti e scrivo un nuovo record pena residua if
			 * (lPenResMod.getImportoAmmenda() != null) aAmmenda = lPenResMod.getImportoAmmenda().intValue();
			 * if (aRichiestaConversione.getImportoAmmenda() != null) aAmmenda = aAmmenda -
			 * aRichiestaConversione.getImportoAmmenda().intValue();
			 * 
			 * if (lPenResMod.getImportoMulta() != null) aMulta = lPenResMod.getImportoMulta().intValue(); if
			 * (aRichiestaConversione.getImportoMulta() != null) aMulta = aMulta -
			 * aRichiestaConversione.getImportoMulta().intValue();
			 * 
			 * lPenResMod.setImportoAmmenda(new BigDecimal(aAmmenda)); lPenResMod.setImportoMulta(new
			 * BigDecimal(aMulta)); lPenResMod.setEveIdEvento(lEveClassI); lPenResMod.setFlagValidato("S");
			 * lPenResMod.setFasSieIdFascicoloSiep(lkeyFasI);
			 * lPenResMod.setDataInserimento(DateUtils.getSysDate()); lPenResDao.setDAOFromModel(lPenResMod);
			 * lkeyPenI =lPenResDao.insert();
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > pena residua esistente1");
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > pena residua scritta = "+lkeyPenI); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger
			 * .debug("Paolo ---- > scritto pena residua per classe I = "+lPenResDao.getFasSieIdFascicoloSiep
			 * ()); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lPenResDao.getImportoAmmenda()); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aMulta = "+lPenResDao.getImportoMulta()); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > legata ad evento = "+lPenResDao.getEveIdEvento()); //
			 * lPenResDao.stop();
			 * 
			 * }else{ // non esiste la pena residua detraggo gli importi convertiti dalla pena complessiva //
			 * e scrivo un nuovo record pena residua if
			 * (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva
			 * ().getPenaComplessiva().getImportoAmmenda()!= null) aAmmenda =
			 * aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva
			 * ().getPenaComplessiva().getImportoAmmenda().intValue(); if
			 * (aRichiestaConversione.getImportoAmmenda()!= null) aAmmenda = aAmmenda -
			 * aRichiestaConversione.getImportoAmmenda().intValue();
			 * 
			 * if
			 * (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getImportoMulta
			 * ()!= null) aMulta =
			 * aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva
			 * ().getImportoMulta().intValue(); if (aRichiestaConversione.getImportoMulta() != null) aMulta =
			 * aMulta - aRichiestaConversione.getImportoMulta().intValue();
			 * 
			 * lPenResMod.setImportoAmmenda(new BigDecimal(aAmmenda)); lPenResMod.setImportoMulta(new
			 * BigDecimal(aMulta)); lPenResMod.setIdPenaResidua(lPenResMod.getIdPenaResidua());
			 * lPenResMod.setEveIdEvento(lEveClassI); lPenResMod.setFlagValidato("S");
			 * lPenResMod.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreInserimento());
			 * lPenResMod.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioInserimento());
			 * lPenResMod.setDataAggiornamento(DateUtils.getSysDate());
			 * lPenResDao.setDAOFromModel(lPenResMod); lkeyPenI =lPenResDao.insert();
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > pena residua non esistente");
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > pena residua scritta = "+lkeyPenI); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger
			 * .debug("Paolo ---- > scritto pena residua per classe I = "+lPenResDao.getFasSieIdFascicoloSiep
			 * ()); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lPenResDao.getImportoAmmenda()); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aMulta = "+lPenResDao.getImportoMulta()); //
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > legata ad evento = "+lPenResDao.getEveIdEvento()); //
			 * lPenResDao.stop(); }
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("AnnotazioneManualeModel"); // scrivo una annotazione manuale con gli
			 * importi da detrarre AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();
			 * lAnnMod.setCodTipoAnnotazione("014"); // altro lAnnMod.setFlagPiuMeno("-");
			 * lAnnMod.setFlagConforme("-"); lAnnMod.setFlagValidato("S"); lAnnMod.setCodFonte("-");
			 * lAnnMod.setCodSottonumerazione("-"); lAnnMod.setCodCausaleComputo("-"); lAnnMod.setCodDpr("-");
			 * lAnnMod.setFlagAppProvvisoria("-"); lAnnMod.setMotivazioni("conversione pena pecuniaria");
			 * lAnnMod.setNumAnniReclusione(new BigDecimal(0)); lAnnMod.setNumMesiReclusione(new
			 * BigDecimal(0)); lAnnMod.setNumGiorniReclusione(new BigDecimal(0));
			 * lAnnMod.setNumAnniArresto(new BigDecimal(0)); lAnnMod.setNumMesiArresto(new BigDecimal(0));
			 * lAnnMod.setNumGiorniArresto(new BigDecimal(0));
			 * 
			 * lAnnMod.setImportoAmmenda(aRichiestaConversione.getImportoAmmenda());
			 * lAnnMod.setImportoMulta(aRichiestaConversione.getImportoMulta());
			 * lAnnMod.setEveIdEvento(lEveClassI); lAnnMod.setFasSieIdFascicoloSiep(lkeyFasI);
			 * lAnnMod.setPenResIdPenaResidua(lkeyPenI);
			 * 
			 * lAnnMod.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			 * lAnnMod.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			 * lAnnMod.setDataInserimento (DateUtils.getSysDate());
			 * 
			 * lAnnDao = new AnnotazioneManualeDAO(lConn); lAnnDao.setDAOFromModel(lAnnMod); lAnnDao.insert();
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto annotazione per classe I = "+lAnnMod.
			 * getFasSieIdFascicoloSiep());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lAnnMod.getImportoAmmenda());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aMulta = "+lAnnMod.getImportoMulta());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > legata ad evento = "+lAnnMod.getEveIdEvento());
			 * //lAnnDao.stop();
			 */

			// **************************************************//
			// CLASSE VII //
			// **************************************************//

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("CLASSE VII	");

			// --------------------------------------------------//
			// SOGGETTO //
			// --------------------------------------------------//
			// stiamo creando un procedimento di classe VII partendo da procedimento di classe I
			// per prima cosa duplico il soggetto legato al procedimento di classe classe I che poi vado a
			// collegare
			// al procedimento di classe VII
			lSogDao = new SoggettoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggetto);
			BigDecimal lkeySoggVII = lSogDao.insert();
			aSoggetto.setIdSoggetto(lkeySoggVII);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("SOGGETTO inserito	");

			// --------------------------------------------------//
			// PROCEDIMENTO SIEP //
			// --------------------------------------------------//
			// creo il nuovo fascicolo classe VII collegandolo al nuovo soggetto
			// Cerco il Progressivo rispettivamente al tipo progressivo impostato
			// tipo fascicolo = 7 fascicolo di conversione
			lFascDao = new FascicoloSiepDAO(lConn);
			lFascDaoSql = new FascicoloSiepSqlDAO(lConn);
			int nTipo = 7;
			aFascicoloSiep.setTipoProgressivo(nTipo);
			lFascDaoSql.getProgressivoFascicoloSiep(aFascicoloSiep);
			lFascDaoSql.start();
			int lMaxProgr = 0;
			if (lFascDaoSql.next() && (lFascDaoSql.getInt("aMAX") > 0))
				lMaxProgr = lFascDaoSql.getInt("aMAX");
			lFascDaoSql.stop();

			// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
			int lTipoProgr = aFascicoloSiep.getTipoProgressivo();
			if (lMaxProgr == 0) {
				if (lTipoProgr == 1)
					aFascicoloSiep.setChiaveProgr(new BigDecimal(1));
				else
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
			} else {
				aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
			}

			// inserisco il nuovo fascicolo di classe VII
			aFascicoloSiep.setSogIdSoggetto(lkeySoggVII); // collego il procedimento al soggetto duplicato
			aFascicoloSiep.setFasSieIdFascicoloSiep(lkeyFasI); // collego il procedimento al fasdcicoli di
																// Classe I.
			lFascDao.setDAOFromModel(aFascicoloSiep);
			BigDecimal lkeyFasVII = lFascDao.insert();
			aFascicoloSiep.setIdFascicoloSiep(lkeyFasVII);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("fascicolo di classe VII inserito	");

			// --------------------------------------------------//
			// PENA COMPLESSIVA //
			// --------------------------------------------------//
			// Paolo Cherubini 03/05/2011
			// devo prendere e riportare la quota parte della penapecuniaria della pena complessiva del
			// procedimento
			// di classe I al proc. di classe VII
			// quindi inserisco un record pena complessiva con questi importi.
			// quando inserisco una richiesta conversione devo detrarre gli importi indicati
			// per cui scrivo un record pena residua con la differenza per entrambi i procedimenti
			// e una annotazione manuale con gli importi da detrarre per entrambi i procedimenti
			// questi record per la classe VII vengono scritti in fondo dopo l'evento

			// 23/02/2015 Inserimento della pena complessiva per il fascicolo di classe VII coi dati dalla
			// pena residua del fascicolo di classe I.
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("PENA COMPLESSIVA");

			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResSqlDao.ricercaPenaResiduaCorrenteByFascicoloSiep(lkeyFasI);
			PenaResiduaModel lPenResMod = (PenaResiduaModel) lPenResSqlDao.getModelByKey();
			int aAmmenda = 0;
			int aMulta = 0;

			if (lPenResMod != null) {
				// esiste la pena residua del Fascicolo di Classe I, la imposto come Pena Complessiva del
				// classe VII.
				if (lPenResMod.getImportoAmmenda() != null)
					aAmmenda = lPenResMod.getImportoAmmenda().intValue();

				if (lPenResMod.getImportoMulta() != null)
					aMulta = lPenResMod.getImportoMulta().intValue();
			}

			PenaComplessivaModel lPenMod = new PenaComplessivaModel();
			lPenDao = new PenaComplessivaDAO(lConn);

			lPenMod.setCodTipoPenaDetentiva("-");
			lPenMod.setCodTipoRito("-");
			lPenMod.setFlagPenaInContinuazione("N");

			lPenMod.setImportoMulta(new BigDecimal(aMulta));
			lPenMod.setImportoAmmenda(new BigDecimal(aAmmenda));
			lPenMod.setFasSieIdFascicoloSiep(lkeyFasVII);
			lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lPenMod.setDataInserimento(DateUtils.getSysDate());
			lPenDao.setDAOFromModel(lPenMod);
			lPenDao.insert();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(" ---- > Letta pena residua Classe I ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger
					.debug(" ---- > scritto pena complessiva per classe VII = "
							+ lPenMod.getFasSieIdFascicoloSiep());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(" ---- > scritto aAmmenda = " + lPenMod.getImportoAmmenda());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(" ---- > scritto aMulta = " + lPenMod.getImportoMulta());

			// Paolo Cherubini 04/05/2011 su indicazione di Michele Testa porto sempre
			// e comunque tutta la pena erogata in sentenza. commentato la parte sopra

			/*
			 * 23/02/2015 Non va replicata la pena complessiva del fascicolo di classe I in quella del
			 * fascicolo di classe VII. PenaComplessivaModel lPenMod = new PenaComplessivaModel(); lPenDao =
			 * new PenaComplessivaDAO(lConn); if
			 * (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva()!= null){
			 * 
			 * lPenMod.setCodTipoPenaDetentiva("-"); lPenMod.setCodTipoRito("-");
			 * lPenMod.setFlagPenaInContinuazione("N");
			 * 
			 * lPenMod.setImportoMulta(aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().
			 * getPenaComplessiva().getImportoMulta());
			 * lPenMod.setImportoAmmenda(aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva
			 * ().getPenaComplessiva().getImportoAmmenda()); lPenMod.setFasSieIdFascicoloSiep(lkeyFasVII);
			 * lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			 * lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			 * lPenMod.setDataInserimento(DateUtils.getSysDate());
			 * 
			 * lPenDao.setDAOFromModel(lPenMod );
			 * 
			 * lPenDao.insert();
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto pena complessiva per classe VII = "+lPenMod.
			 * getFasSieIdFascicoloSiep());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lPenMod.getImportoAmmenda());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aMulta = "+lPenMod.getImportoMulta()); }
			 */// 23/02/2015

			/*
			 * if (!Multa.equals("")) { if (!Multa_dec.equals("")) { lAnnMod.setImportoMulta(new
			 * BigDecimal(Multa + "." + Multa_dec)); } else lAnnMod.setImportoMulta(new BigDecimal(Multa)); }
			 * else if (!Multa_dec.equals("")) lAnnMod.setImportoMulta(new BigDecimal("0." + Multa_dec));
			 */

			/*
			 * if (!Ammenda.equals("")) { if (!Ammenda_dec.equals("")) { lAnnMod.setImportoAmmenda(new
			 * BigDecimal(Ammenda + "." + Ammenda_dec)); } else lAnnMod.setImportoAmmenda(new
			 * BigDecimal(Ammenda)); } else if (!Ammenda_dec.equals("")) lAnnMod.setImportoAmmenda(new
			 * BigDecimal("0." + Ammenda_dec));
			 */

			// stato_procedimento prendo l'ultimo statoprocedimento della lista
			// (size-1)
			StatoProcedimentoDAO lStaDao = null;
			lStaDao = new StatoProcedimentoDAO(lConn);
			// StatoProcedimentoModel lStaMod =
			// (StatoProcedimentoModel)aDettaglioFascicolo.getStatoProcedimento().get(aDettaglioFascicolo.getStatoProcedimento().size()-1);
			StatoProcedimentoModel lStaMod = new StatoProcedimentoModel();
			lStaMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lStaMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lStaMod.setDataInserimento(DateUtils.getSysDate());
			lStaMod.setEveIdEvento(lEveClassI);

			lStaMod.setFasSieIdFascicoloSiep(lkeyFasVII);
			lStaMod.setProgressivo(new BigDecimal(1));
			lStaMod.setCodStatoProcedimento("0109"); // validato
			lStaMod.setData(null);
			lStaDao.setDAOFromModel(lStaMod);
			lStaDao.insert();

			// residenza se sono vuoti???
			if (aDettaglioFascicolo.getResidenza() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getResidenza();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				BigDecimal lkeyResVII = lResDao.insert();

				// collego la residenza al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(lkeyFasVII);
				lResFasMod.setResIdResidenza(lkeyResVII);
				lResFasDao.setDAOFromModel(lResFasMod);
				lResFasDao.insert();
			}

			// duplico domicilio collegandolo al nuovo soggetto
			if (aDettaglioFascicolo.getDomicilio() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getDomicilio();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				BigDecimal lkeyDomVII = lResDao.insert();

				// collego il domicilio al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(lkeyFasVII);
				lResFasMod.setResIdResidenza(lkeyDomVII);
				lResFasDao.setDAOFromModel(lResFasMod);
				lResFasDao.insert();
			}

			// duplico posizione giuridica collegandolo al nuovo fascicolo
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			if (aDettaglioFascicolo.getPosizioneGiuridica() != null) {
				PosizioneGiuridicaModel lPosMod = aDettaglioFascicolo.getPosizioneGiuridica();
				lPosMod.setFasSieIdFascicoloSiep(lkeyFasVII);
				lPosMod.setIdEventoRiferimento(null); // 24/07/2015
				lPosDao.setDAOFromModel(lPosMod);
			} else {
				// creo posizione giuridica a libero
				lPosDao = new PosizioneGiuridicaDAO(lConn);
				PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
				lPosMod.setCodPosizioneGiuridica("07");
				lPosMod.setCodPosizioneProcessuale("-");
				lPosMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
				lPosMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
				lPosMod.setDataInserimento(DateUtils.getSysDate());
				lPosMod.setFasSieIdFascicoloSiep(lkeyFasVII);
				lPosDao.setDAOFromModel(lPosMod);
			}
			lPosDao.insert();

			// duplico magistrato collegandolo al nuovo fascicolo
			if (aDettaglioFascicolo.getMagistratoCompetente() != null) {
				if (aDettaglioFascicolo.getMagistratoCompetente().getMagistratoCompetente() != null) {
					lMagComDao = new MagistratoCompetenteDAO(lConn);

					MagistratoCompetenteMagistratoModel lMagistrato = aDettaglioFascicolo
							.getMagistratoCompetente();
					MagistratoCompetenteModel lMagMod = lMagistrato.getMagistratoCompetente();

					lMagMod = lMagistrato.getMagistratoCompetente();
					lMagMod.setMagCodMagistrato(lMagistrato.getMagistrato().getCodMagistrato());
					lMagMod.setFasSieIdFascicoloSiep(lkeyFasVII);
					lMagComDao.setDAOFromModel(lMagMod);
					lMagComDao.insert();
				}
			}

			// Insert Reato e le circostanze sempre e solo da tabella REATO
			if (aDettaglioFascicolo.getReatiCircostanze() != null) {
				// prendo la lista dei reati e la metto nel model comune
				lReaDao = new ReatoDAO(lConn);
				List lLisReaCir = aDettaglioFascicolo.getReatiCircostanze();
				ReatoModel lReaMod = new ReatoModel();

				// ciclo su questa lista
				for (int i = 0; i <= lLisReaCir.size() - 1; i++) {
					// prendo il reato per scriverlo
					lReaMod = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getReato();
					// scrivo reato
					lReaMod.setFasSieIdFascicoloSiep(lkeyFasVII);
					lReaDao.setDAOFromModel(lReaMod);
					lReaDao.insert();

					// prendo la lista delle circostanze affogate nella tabella reati
					ReatoModel[] lLisCir = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getCircostanze();
					// Ciclo sulle n° circostanze afffogate nella tabella REATI
					for (int j = 0; j <= lLisCir.length - 1; j++) {
						// prendo la circostanza per scriverla
						lReaMod = lLisCir[j];
						// scrivo la circostanza
						lReaMod.setFasSieIdFascicoloSiep(lkeyFasVII);
						lReaDao.setDAOFromModel(lReaMod);
						lReaDao.insert();
					}
				}
			}

			// circostanze
			if (aDettaglioFascicolo.getCircostanze() != null) {
				CircostanzaModel lCirMod = new CircostanzaModel();
				List lLisCir = aDettaglioFascicolo.getCircostanze();
				lCirDao = new CircostanzaDAO(lConn);
				for (int i = 0; i <= lLisCir.size() - 1; i++) {
					lCirMod = (CircostanzaModel) lLisCir.get(i);
					lCirMod.setFasSieIdFascicoloSiep(lkeyFasVII);
					lCirDao.setDAOFromModel(lCirMod);
					lCirDao.insert();
				}
			}

			// Paolo Cherubini 26/04/2011 aggiungo anche copia avvocati
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("AVVOCATI PER FASCICOLO: " + lkeyFasVII);
			if (aDettaglioFascicolo.getAvvocatiSIEP() != null) {
				AvvocatoFascicoloSiepModel lAvvFasMod = new AvvocatoFascicoloSiepModel();
				List lLisAvv = aDettaglioFascicolo.getAvvocatiSIEP();
				lAvvFasDao = new AvvocatoFascicoloSiepDAO(lConn);
				for (int i = 0; i <= lLisAvv.size() - 1; i++) {
					lAvvFasMod = (AvvocatoFascicoloSiepModel) lLisAvv.get(i);
					lAvvFasMod.setFasSieIdFascicoloSiep(lkeyFasVII);
					lAvvFasDao.setDAOFromModel(lAvvFasMod);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					// siesLogger.debug("lAvvFasMod: "+lAvvFasMod);
					lAvvFasDao.insert();
				}
			}

			// Paolo Cherubini 22/04/2011 inserisco l'evento anche per la classe VII
			// inserisco l'evento di conversione nel fascicolo di classe VII
			lEveDao.stop();
			aEvento.setFasSieIdFascicoloSiep(lkeyFasVII);
			lEveDao.setDAOFromModel(aEvento);
			BigDecimal lkeyEveVII = lEveDao.insert();

			/*
			 * 23/02/2015 Gestione decurtazione quantum Pena Residua e inserimento Annotazione Manuale
			 * aboliti.
			 * 
			 * // Paolo Cherubini 03/05/2011 // quando inserisco una richiesta conversione devo detrarre gli
			 * importi indicati
			 * 
			 * // pena residua classe VII = pena residua classe I // annotazione manuale classe VII = pena
			 * complessiva - pena residua
			 * 
			 * aAmmenda = 0; aMulta = 0;
			 * 
			 * lPenResMod.setNumAnniReclusione(new BigDecimal(0)); lPenResMod.setNumMesiReclusione(new
			 * BigDecimal(0)); lPenResMod.setNumGiorniReclusione(new BigDecimal(0));
			 * lPenResMod.setNumAnniArresto(new BigDecimal(0)); lPenResMod.setNumMesiArresto(new
			 * BigDecimal(0)); lPenResMod.setNumGiorniArresto(new BigDecimal(0));
			 * 
			 * lPenResMod.setEveIdEvento(lkeyEveVII); lPenResMod.setFasSieIdFascicoloSiep(lkeyFasVII);
			 * 
			 * lPenResDao.setDAOFromModel(lPenResMod); BigDecimal lkeyPenVII =lPenResDao.insert();
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto pena residua per classe VII = "+lPenResMod.
			 * getFasSieIdFascicoloSiep());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto pena residua per evento VII = "
			 * +lPenResMod.getEveIdEvento());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aAmmenda = "+lPenResMod.getImportoAmmenda());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto aMulta = "+lPenResMod.getImportoMulta());
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("AnnotazioneManualeModel");
			 * 
			 * lAnnMod = new AnnotazioneManualeModel(); lAnnDao = new AnnotazioneManualeDAO(lConn);
			 * 
			 * lAnnMod.setCodTipoAnnotazione("014"); // altro lAnnMod.setFlagPiuMeno("-");
			 * lAnnMod.setFlagConforme("-"); lAnnMod.setFlagValidato("S"); lAnnMod.setCodFonte("-");
			 * lAnnMod.setCodSottonumerazione("-"); lAnnMod.setCodCausaleComputo("-"); lAnnMod.setCodDpr("-");
			 * lAnnMod.setFlagAppProvvisoria("-"); lAnnMod.setMotivazioni("conversione pena pecuniaria");
			 * lAnnMod.setNumAnniReclusione(new BigDecimal(0)); lAnnMod.setNumMesiReclusione(new
			 * BigDecimal(0)); lAnnMod.setNumGiorniReclusione(new BigDecimal(0));
			 * lAnnMod.setNumAnniArresto(new BigDecimal(0)); lAnnMod.setNumMesiArresto(new BigDecimal(0));
			 * lAnnMod.setNumGiorniArresto(new BigDecimal(0));
			 * 
			 * if (lPenMod.getImportoAmmenda() != null) aAmmenda = lPenMod.getImportoAmmenda().intValue(); if
			 * (lPenResMod.getImportoAmmenda() != null) aAmmenda = aAmmenda -
			 * lPenResMod.getImportoAmmenda().intValue();
			 * 
			 * if (lPenMod.getImportoMulta() != null) aMulta = lPenMod.getImportoMulta().intValue(); if
			 * (lPenResMod.getImportoMulta() != null) aMulta = aMulta -
			 * lPenResMod.getImportoMulta().intValue();
			 * 
			 * lAnnMod.setImportoAmmenda(new BigDecimal(aAmmenda)); lAnnMod.setImportoMulta(new
			 * BigDecimal(aMulta)); lAnnMod.setEveIdEvento(lkeyEveVII);
			 * lAnnMod.setFasSieIdFascicoloSiep(lkeyFasVII); lAnnMod.setPenResIdPenaResidua(lkeyPenVII);
			 * 
			 * lAnnMod.setCodOperatoreInserimento (aFascicoloSiep.getCodOperatoreInserimento());
			 * lAnnMod.setCodUfficioInserimento (aFascicoloSiep.getCodUfficioInserimento());
			 * lAnnMod.setDataInserimento (DateUtils.getSysDate());
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto annotazioni per classe VII = "+lAnnMod.
			 * getFasSieIdFascicoloSiep());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto annotazioni per evento VII = "
			 * +lAnnMod.getEveIdEvento());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto ammenda = "+lAnnMod.getImportoAmmenda());
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * siesLogger.debug("Paolo ---- > scritto multaaa = "+lAnnMod.getImportoMulta());
			 * lAnnDao.setDAOFromModel(lAnnMod); lAnnDao.insert(); //lAnnDao.stop();
			 */

			// 08/07/2015 Inserimento della pena residua con importi a zero per il Fascicolo di classe VII.
			lPenResDao = new PenaResiduaDAO(lConn);
			lPenResSqlDao = new PenaResiduaSqlDAO(lConn);
			lPenResMod = new PenaResiduaModel();

			lPenResDao.setDAOFromModel(lPenResMod);
			lPenResDao.setFlagValidato("S");
			lPenResDao.setEveIdEvento(aEvento.getIdEvento());
			lPenResDao.setFasSieIdFascicoloSiep(lkeyFasVII);

			lPenResDao.setCodOperatoreInserimento(aEvento.getCodOperatoreAggiornamento());
			lPenResDao.setCodUfficioInserimento(aEvento.getCodUfficioAggiornamento());
			lPenResDao.setDataInserimento(DateUtils.getSysDate());

			lPenResDao.insert();
			lPenResDao.stop();

			// inserisco la richiesta di conversione
			aRichiestaConversione.setFasSieIdFascicoloSiep(lkeyFasVII);
			aRichiestaConversione.setEveIdEvento(lkeyEveVII);
			lRicDao = new RichiestaConversioneDAO(lConn);
			lRicDao.setDAOFromModel(aRichiestaConversione);
			BigDecimal lkeyConVII = lRicDao.insert();
			aRichiestaConversione.setIdRichiestaConversione(lkeyConVII);

			commit(lConn);

			lRicMod = new RichiestaConversioneModel(aRichiestaConversione);
			lRicMod.setMessage("Inserimento avvenuto correttamente!");
			// lRicMod.setIdRichiestaConversione(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RichiestaConversioneController.ExInserisciRichiestaConversionedaClasseI: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lEveDao);
			cleanup(lFascDao);
			cleanup(lFascDaoSql);
			cleanup(lRicDao);
			cleanup(lResDao);
			cleanup(lResFasDao);
			cleanup(lPosDao);
			cleanup(lMagComDao);
			cleanup(lReaDao);
			cleanup(lCirDao);
			cleanup(lPenDao);
			cleanup(lSanDao);

			cleanup(lPenResDao);
			cleanup(lPenResSqlDao);
			cleanup(lAvvFasDao);
			cleanup(lAnnDao);

			cleanup(lConn);
		}
		return lRicMod;
	}

	/**
	 * controller utilizzato per la stampa
	 * 
	 * @param: aEventoNotificaModel Model con i dati da inserire aUtente Model con i dati da inserire
	 * @return lByteArrayOut
	 * @throws F3BException
	 */
	public ByteArrayOutputStream exStampaCP(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;

		ByteArrayOutputStream lByteArrayOut = null;

		try {
			IEvento lEveCntrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lEveCntrl.ExRicercaEventoNotificaByKey(aEvento.getEvento()
					.getIdEvento());
			lEveMod.getEvento().setDescrUfficioEmittente(aEvento.getEvento().getDescrUfficioEmittente());
			String lNomeTemplate = TemplateManager.getInstance().getTemplateName(aEvento.getNomeTemplate());
			// QUI setto l'id del template con il nemo vero e proprio
			lEveMod.getEvento().setTemIdTemplate(lNomeTemplate);
			StampaCPController lStampaCP = new StampaCPController();
			TreeModel lTree = lStampaCP.prelevaDatiRichiestaConversione(lEveMod, aUtente);

			ReportGenerator lReport = new ReportGenerator();
			lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTree, lNomeTemplate);
			ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(lByteArrayOut.toByteArray());

			aEvento.getEvento().setDocBlobIn(lByteArrayInput);
			lConn = getDBConnection();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModelForUpdateBlob(aEvento.getEvento());

			lEveDao.selCondizioneUpdate(aEvento.getEvento().getIdEvento());
			lEveDao.update();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".exStampaCP:  " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(this.getClass().getName() + ".exStampaCP: " + ex);
		} finally {
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lByteArrayOut;
	}

	/**
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdEvento(BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;
		RichiestaConversioneModel lRichiestaConversioneMod = new RichiestaConversioneModel();
		RichiestaConversioneSqlDAO lRichiestaConversioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaConversioneSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRichiestaConversioneSqlDao.ricercaRichiestaConversioneByEvento(aIdEvento);
			lRichiestaConversioneMod = (RichiestaConversioneModel) lRichiestaConversioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiestaConversioneByEvento: "
					+ daoEx);
		} finally {
			cleanup(lRichiestaConversioneSqlDao);
			cleanup(lConn);
		}

		return lRichiestaConversioneMod;
	}

	/**
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdFascicoloSiep(
			BigDecimal aIdFascicoloSiep) throws F3BException {
		Connection lConn = null;
		RichiestaConversioneModel lRichiestaConversioneMod = new RichiestaConversioneModel();
		RichiestaConversioneSqlDAO lRichiestaConversioneSqlDao = null;

		try {
			lConn = getDBConnection();
			lRichiestaConversioneSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRichiestaConversioneSqlDao.ricercaRichiestaConversioneByIdFascicoloSiep(aIdFascicoloSiep);
			lRichiestaConversioneMod = (RichiestaConversioneModel) lRichiestaConversioneSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiestaConversioneByIdFascicoloSiep: " + daoEx);
		} finally {
			cleanup(lRichiestaConversioneSqlDao);
			cleanup(lConn);
		}

		return lRichiestaConversioneMod;
	}

	/**
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 */
	public RichiestaConversioneModel ExRicercaRichiestaConversioneByIdFascicoloSiepClasseI(
			BigDecimal aIdFascicoloSiep) throws F3BException {
		Connection lConn = null;
		RichiestaConversioneModel lRicConMod = new RichiestaConversioneModel();
		RichiestaConversioneSqlDAO lRicConSqlDao = null;
		FascicoloSiepSqlDAO lFascDao = null;
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		try {
			lConn = getDBConnection();

			lFascDao = new FascicoloSiepSqlDAO(lConn);
			lFasMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
			lFascDao.ricercaFascicolo(lFasMod);
			Vector lFascicoli = new Vector(lFascDao.getModels());
			if (lFascicoli.size() > 0) {
				lFasMod = (FascicoloSiepModel) lFascicoli.get(0);
				lRicConSqlDao = new RichiestaConversioneSqlDAO(lConn);
				lRicConSqlDao.ricercaRichiestaConversioneByIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
				lRicConMod = (RichiestaConversioneModel) lRicConSqlDao.getModelByKey();
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiestaConversioneByIdFascicoloSiep: " + daoEx);
		} finally {
			cleanup(lRicConSqlDao);
			cleanup(lFascDao);
			cleanup(lConn);
		}

		return lRicConMod;
	}

	/**
	 * Effettua la ricerca dei dati RichiestaConversione usando il RichiestaConversioneSqlDAO. Creato per
	 * puntare alle Richieste riferite a un fascicolo SIUS.
	 * 
	 * @param aRichiestaConversione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiesteConversionePenePecuniarie(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		Vector lRichiestaConversioni = new Vector();
		RichiestaConversioneSqlDAO lRicSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRicSqlDao.ricercaRichiestaConversione(aRichiestaConversione);
			lRichiestaConversioni = new Vector(lRicSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiesteConversionePenePecuniarie: " + daoEx);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + Ex);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiesteConversionePenePecuniarie: " + Ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}
		return lRichiestaConversioni;
	}

	/**
	 * Effettua la ricerca dei dati RichiestaConversione Valide
	 * 
	 * @param aRichiestaConversione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiesteConversioniValide(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		Vector lRichiestaConversioni = new Vector();
		RichiestaConversioneDAO lRicDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		try {
			lConn = getDBConnection();
			lRicDao = new RichiestaConversioneDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lRicDao.setCondizioni(aRichiestaConversione);
			lRicDao.setOrderBy();
			lRicDao.start();
			while (lRicDao.next()) {
				RichiestaConversioneModel lRicMod = (RichiestaConversioneModel) lRicDao.getModel();
				lEveSqlDAO.ricercaEventoByKey(lRicMod.getEveIdEvento());
				EventoModel lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();
				if (lEveMod != null && lEveMod.getFlagDocumentoRegistrato() != null) {
					if (lEveMod.getFlagDocumentoRegistrato().compareTo("S") == 0) {
						lRichiestaConversioni.add((RichiestaConversioneModel) lRicDao.getModel());
					}
				}
			}
			lRicDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiesteConversioniValide: "
					+ daoEx);
		} finally {
			cleanup(lRicDao);
			cleanup(lEveSqlDAO);
			cleanup(lConn);
		}
		return lRichiestaConversioni;
	}

	public Vector ExRicercaRichiesteConversionePenePecuniarieByIdFascicoloSius(BigDecimal aIdFascicoloSius)
			throws F3BException {
		Connection lConn = null;
		Vector lRichiestaConversioni = new Vector();
		RichiestaConversioneSqlDAO lRicSqlDao = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lRicSqlDao.ricercaRichiestaConversioneByIdFasSIUS(aIdFascicoloSius);
			lRichiestaConversioni = new Vector(lRicSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiesteConversionePenePecuniarie: " + daoEx);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + Ex);
			throw new F3BException(
					"RichiestaConversioneController.ExRicercaRichiesteConversionePenePecuniarie: " + Ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lConn);
		}
		return lRichiestaConversioni;
	}

	/**
	 * Effettua la ricerca dei dati di RichiestaConversione usando il RichiestaConversioneSqlDAO. Creato per
	 * puntare alle Richieste riferite a un fascicolo SIUS, carica il model esteso
	 * RichiestaConversioneEstesaModel.
	 * 
	 * @param aRichiestaConversione
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 */
	public Vector ExRicercaRichiestaConversioneEstesa(RichiestaConversioneModel aRichiestaConversione)
			throws F3BException {
		Connection lConn = null;
		Vector lRichiesteConversioni = new Vector();
		RichiestaConversioneSqlDAO lRicSqlDao = null;
		FascicoloSiepSqlDAO lFasSieSqlDao = null;
		EventoSqlDAO lEveSqlDAO = null;

		try {
			lConn = getDBConnection();
			lRicSqlDao = new RichiestaConversioneSqlDAO(lConn);
			lEveSqlDAO = new EventoSqlDAO(lConn);

			// lRicSqlDao.ricercaRichiestaConversione(aRichiestaConversione);
			lRicSqlDao.ricercaRichiestaConversioneByIdFasSIUS(aRichiestaConversione
					.getFasSiuIdFascicoloSius());
			lRicSqlDao.start();

			while (lRicSqlDao.next()) {
				RichiestaConversioneModel lRicConv = new RichiestaConversioneModel(
						(RichiestaConversioneModel) lRicSqlDao.getModel());

				// Controllo che la Richiesta non sia associata ad un evento non validato/annullato
				lEveSqlDAO.ricercaEventoByKey(lRicConv.getEveIdEvento());
				EventoModel lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();

				if (lEveMod != null && lEveMod.getFlagDocumentoRegistrato() != null
						&& lEveMod.getFlagDocumentoRegistrato().compareTo("A") == 0) {
					// Salto il record conversione in quanto associato ad un evento (richiesta) annullato
					continue;
				}

				RichiestaConversioneEstesaModel lRicConvEstesa = new RichiestaConversioneEstesaModel();
				lRicConvEstesa.setRichiestaConversione(lRicConv);

				if (lRicConv.getFasSieIdFascicoloSiep() != null) {
					lFasSieSqlDao = new FascicoloSiepSqlDAO(lConn);
					lFasSieSqlDao.ricercaFascicoloByKey(lRicConv.getFasSieIdFascicoloSiep());
					FascicoloSiepModel lFascicolo = new FascicoloSiepModel(
							(FascicoloSiepModel) lFasSieSqlDao.getModelByKey());
					if (lFascicolo != null)
						lRicConvEstesa.setFasSiep(lFascicolo);
				}
				lRichiesteConversioni.add(lRicConvEstesa);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiestaConversioneEstesa: "
					+ daoEx);
		} catch (Exception Ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + Ex);
			throw new F3BException("RichiestaConversioneController.ExRicercaRichiestaConversioneEstesa: "
					+ Ex);
		} finally {
			cleanup(lRicSqlDao);
			cleanup(lFasSieSqlDao);
			cleanup(lEveSqlDAO);
			cleanup(lConn);
		}
		return lRichiesteConversioni;
	}

	public void inserimentoScadenzario(RichiestaConversioneModel aRichiestaConversione, Connection lConn)
			throws F3BException {
		// Se iscrive SIUS viene inserito lo Scadenzario (Previa lettura del
		// Parametro).
		// Condizioni aggiuntive almeno uno dei tre seguenti parametri deve
		// essere valorizzato:
		// DATA_IRREVOCABILITA, IMPORTO_MULTA, IMPORTO_AMMENDA.
		if (aRichiestaConversione.getFasSiuIdFascicoloSius() != null
				&& (aRichiestaConversione.getDataIrrevocabilita() != null
						|| aRichiestaConversione.getImportoAmmenda() != null || aRichiestaConversione
						.getImportoMulta() != null)) {
			ParametroSqlDAO lParDao = null;
			ScadenzarioSiusDAO lScaSiusDao = null;

			try {
				// Lettura Tabella PARAMETRO x Richiesta Conversione PP (Multa o
				// Ammenda).
				ParametroModel lParMod = new ParametroModel();
				if (Utils.isNullObj(aRichiestaConversione.getDataPrescrizioneAmmenda())
						&& Utils.isNullObj(aRichiestaConversione.getDataIrrevocabilita())
						&& Utils.isNullObj(aRichiestaConversione.getDataPrescrizioneMulta())) {
					lParMod.setGiorni(new BigDecimal(0));
					lParMod.setMesi(new BigDecimal(0));
					lParMod.setAnni(new BigDecimal(0));
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug(" Nessun importo multa/ammenda selezionato ");
				} else {
					if (!Utils.isNullObj(aRichiestaConversione.getImportoAmmenda()))
						lParMod.setNomeParametro("RICHIESTA_CONVERSIONE_PP_AMMENDA");
					if (!Utils.isNullObj(aRichiestaConversione.getImportoMulta()))
						lParMod.setNomeParametro("RICHIESTA_CONVERSIONE_PP_MULTA");

					Vector lVectPar = null;

					lParDao = new ParametroSqlDAO(lConn);
					lParDao.ricercaParametroScadenzario(lParMod.getNomeParametro(),
							lParMod.getCodUfficioValidita());
					lVectPar = new Vector(lParDao.getModels());

					// Segnalazione della mancanza del Parametro.
					if (lVectPar.size() < 1)
						throw new SIUSException(F3BException.USER_MESSAGE,
								"Errore: Parametro di Scadenzario mancante! ");

					lParMod = (ParametroModel) lVectPar.get(0);

					// Se esiste, si cancella l'occorrenza dallo scadenzario.
					lScaSiusDao = new ScadenzarioSiusDAO(lConn);
					lScaSiusDao.setCondizioniByIdFascicoloTipo(
							aRichiestaConversione.getFasSiuIdFascicoloSius(),
							ICostantiScadenzarioSius.TIPO_SCA_RICHIESTA_CONVERSIONE_PP);
					lScaSiusDao.delete();
					lScaSiusDao.stop();

					// Inserimento Scadenzario.
					lScaSiusDao = new ScadenzarioSiusDAO(lConn);
					ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
					lScaMod.setCodTipoScadenzario(ICostantiScadenzarioSius.TIPO_SCA_RICHIESTA_CONVERSIONE_PP); // Prescrizione
																												// conversione
																												// pena
																												// pecuniaria
					lScaMod.setFasSiuIdFascicoloSius(aRichiestaConversione.getFasSiuIdFascicoloSius());

					if (!Utils.isNullObj(aRichiestaConversione.getDataIrrevocabilita()))
						lScaMod.setDataInizioScadenza(aRichiestaConversione.getDataIrrevocabilita());
					if (!Utils.isNullObj(aRichiestaConversione.getDataPrescrizioneMulta()))
						lScaMod.setDataInizioScadenza(aRichiestaConversione.getDataPrescrizioneMulta());
					if (!Utils.isNullObj(aRichiestaConversione.getDataPrescrizioneAmmenda()))
						lScaMod.setDataInizioScadenza(aRichiestaConversione.getDataPrescrizioneAmmenda());
					else
						// MEV_39: MODIFICATA DATA
						lScaMod.setDataInizioScadenza(DateUtils.getSysDateAsDate("dd/MM/yyyy"));

					// Impostazione della Data Fine Scadenza in base al periodo
					// di PARAMETRO o alla imprescrittibilità.
					if (aRichiestaConversione.getFlagImprescrittibileMulta() == "S")
						lScaMod.setDataFineScadenza(DateUtils.getDate("01-01-9999", "dd-MM-yyyy"));
					else if (aRichiestaConversione.getFlagImprescrittibileAmmenda() == "S")
						lScaMod.setDataFineScadenza(DateUtils.getDate("01-01-9999", "dd-MM-yyyy"));
					else {
						lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(),
								java.util.Calendar.DAY_OF_MONTH, lParMod.getGiorni().intValue()));
						lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(),
								java.util.Calendar.MONTH, lParMod.getMesi().intValue()));
						lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lScaMod.getDataInizioScadenza(),
								java.util.Calendar.YEAR, lParMod.getAnni().intValue()));
					}

					// Valorizzazione dell'ID_EVENTO, solo se è stato caricato
					// l'evento in precedenza.
					if (aRichiestaConversione.getEveIdEvento() != null)
						lScaMod.setEveIdEvento(aRichiestaConversione.getEveIdEvento());

					lScaMod.setCodOperatoreInserimento(aRichiestaConversione.getCodOperatoreInserimento());
					lScaMod.setCodUfficioInserimento(aRichiestaConversione.getCodUfficioInserimento());
					lScaMod.setDataInserimento(aRichiestaConversione.getDataInserimento());

					lScaSiusDao.setDAOFromModel(lScaMod);
					lScaSiusDao.insert();
					lScaSiusDao.stop();

					commit(lConn);
				}
			} catch (DAOException ex) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.error("DAOException: " + ex);
				throw new F3BException("RichiestaConversioneController.inserimentoScadenzario: " + ex);
			} catch (Exception e) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.error("Exception: " + e);
				throw new F3BException("RichiestaConversioneController.inserimentoScadenzario:  " + e);
			} finally {
				cleanup(lParDao);
				cleanup(lScaSiusDao);
			}
		}

	}

	public RichiestaConversioneModel ExInserisciRichiestaEvento(
			RichiestaConversioneModel aRichiestaConversione, EventoNotificaModel aEvento,
			PenaResiduaModel aPenaResidua, String StatoPro) throws F3BException {
		Connection lConn = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lSqlDAO = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;
		PenaResiduaDAO lPenDao = null;
		RichiestaConversioneDAO lRicDao = null;
		StatoProcedimentoDAO lStatoDao = null;
		// CampoNotaDAO lCampoNotaDao = null;

		EventoNotificaModel lEveRet = new EventoNotificaModel(aEvento);

		try {
			lConn = getDBTransaction();
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDAOFromModel(aEvento.getEvento());
			BigDecimal lKeyEvento = lEveDao.insert();

			lAutDao = new AutoritaEsternaDAO(lConn);
			lNotDao = new NotificaDAO(lConn);

			lEveRet.getEvento().setIdEvento(lKeyEvento);

			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEvento != null && aEvento.getNotifiche() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Presenti " + aEvento.getNotifiche().length + " notifiche");

				while (count < aEvento.getNotifiche().length) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Notifica[" + count + "] = " + aEvento.getNotifiche()[count]);

					if (aEvento.getNotifiche()[count] != null) {

						if (aEvento.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(aEvento.getNotifiche()[count].getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(aEvento.getNotifiche()[count].getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
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

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.debug("Inserito evento" + lKeyEvento);
					}
					count++;
				}
			}

			lPenDao = new PenaResiduaDAO(lConn);
			// Inserisce Pena Residua
			PenaResiduaModel lPenMod = aPenaResidua;
			if (lPenMod != null) {
				lPenMod.setIdPenaResidua(null);
				lPenMod.setCodOperatoreAggiornamento(null);
				lPenMod.setDataAggiornamento(null);
				lPenMod.setCodUfficioAggiornamento(null);

				lPenMod.setFlagValidato("S");
				lPenMod.setEveIdEvento(lKeyEvento);
				lPenMod.setCodOperatoreInserimento(aEvento.getEvento().getCodOperatoreInserimento());
				lPenMod.setDataInserimento(DateUtils.getSysDate());
				lPenMod.setCodUfficioInserimento(aEvento.getEvento().getCodUfficioInserimento());

				lPenDao.setDAOFromModel(lPenMod);
				lPenDao.insert();
				lPenDao.stop();
			}

			lRicDao = new RichiestaConversioneDAO(lConn);
			aRichiestaConversione.setEveIdEvento(lKeyEvento);
			lRicDao.setDAOFromModel(aRichiestaConversione);
			BigDecimal lSequence = lRicDao.insert();
			aRichiestaConversione.setIdRichiestaConversione(lSequence);

			if (StatoPro != null) {
				StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();
				lStatoDao = new StatoProcedimentoDAO(lConn);
				// - Cancella eventuali record prima di inserire un nuovo
				// STATO_PROCEDIMENTO
				lStatoDao.setCondizioneByIdFascicolo(lEveRet.getEvento().getFasSieIdFascicoloSiep());
				lStatoDao.delete();

				lStatoProcMod.setCodStatoProcedimento(StatoPro);
				lStatoProcMod.setFasSieIdFascicoloSiep(lEveRet.getEvento().getFasSieIdFascicoloSiep());
				lStatoProcMod.setCodOperatoreInserimento(lEveRet.getEvento().getCodOperatoreAggiornamento());
				lStatoProcMod.setDataInserimento(lEveRet.getEvento().getDataAggiornamento());
				lStatoProcMod.setCodUfficioInserimento(lEveRet.getEvento().getCodUfficioAggiornamento());
				lStatoProcMod.setProgressivo(new BigDecimal(1));
				lStatoProcMod.setData(lEveRet.getEvento().getDataEmissione());
				lStatoDao.setDAOFromModel(lStatoProcMod);
				lStatoDao.insert();
				lStatoDao.stop();
			}
			/*
			 * // Inserimento delle eventuali note aggiuntive. lCampoNotaDao = new CampoNotaDAO(lConn); if
			 * (// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			 * (aEvento.getCampoNote() != null) { siesLogger.debug("Inserimento Eventuali Note
			 * Aggiuntive Numero note Aggiuntive : " + aEvento.getCampoNote().length); count = 0; while (count
			 * < aEvento.getCampoNote().length) { aEvento.getCampoNote()[count].setEveIdEvento(lKeyEvento);
			 * aEvento.getCampoNote()[count].setProgressivo(new BigDecimal((double) count + 1));
			 * lCampoNotaDao.setDAOFromModel(aEvento.getCampoNote()[count]); lCampoNotaDao.insert();
			 * lCampoNotaDao.stop();
			 * 
			 * count++; } }
			 */

			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + daoEx);
		} catch (Exception ex) {
			throw new F3BException("EventoController.ExInserisciEventoNotifica: " + ex);
		} finally {
			// cleanup(lCampoNotaDao);
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);
			cleanup(lSqlDAO);
			cleanup(lPenDao);
			cleanup(lRicDao);
			cleanup(lStatoDao);

			cleanup(lConn);
		}

		return aRichiestaConversione;
	}

	/**
	 * Inserisci i records di Richiesta Conversione JMS senza assegnare la sequence
	 * 
	 * @param aRichiesteConversione
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciRichiesteConversioniWithoutSequence(ArrayList aRichiesteConversione,
			Connection lConn) throws F3BException {
		String lCodEsito = "00000";
		RichiestaConversioneDAO lRicConDao = null;
		RichiestaConversioneModel lRicConMod = null;

		try {
			lRicConDao = new RichiestaConversioneDAO(lConn);

			if (aRichiesteConversione != null && aRichiesteConversione.size() > 0) {
				for (int i = 0; i < aRichiesteConversione.size(); i++) {
					lRicConMod = (RichiestaConversioneModel) aRichiesteConversione.get(i);
					if (lRicConMod != null) {
						if (lRicConMod.getIdRichiestaConversione() != null) {
							lRicConDao.setDAOFromModel(lRicConMod);
							lRicConDao.setWithoutSequence(true);
							lRicConDao.insert();
							lRicConDao.stop();
						}
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Richiesta Conversione gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la Richiesta Conversione! ");
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new F3BException(
					"SanzioneSostitutivaController.ExInserisciRichiesteConversioneWithoutSequence: " + e);
		} finally {
			cleanup(lRicConDao);
		}
		return lCodEsito;
	}

	/**
	 * Effettua l'inserimento della dell'evento SIEP di 'Annotazione' decisione richiesta conversione della
	 * sorveglianza
	 * 
	 * @param aEventoSIEP
	 * @param aEventoSIUS
	 * @param aRichiestaConversioneModel
	 * @param aScambioSanzioneModel
	 * @param
	 * 
	 * @return
	 */
	public EventoNotificaModel ExInserisciDecisioneSorveglianza(EventoNotificaModel aEventoNotSIEP,
			EventoModel aEventoSIUS, RichiestaConversioneModel aRichiestaConversioneModel,
			ScambioSanzioneModel aScambioSanzioneModel, TenoreModel aTenoreModel,
			DepositoDecretoModel aDepoDecModel, DepositoOrdinanzaPcModel aDepOrdPcModel,
			PenaResiduaModel aPenaResidua) throws F3BException {

		Connection lConn = null;

		EventoNotificaModel lEveNotRet = new EventoNotificaModel(aEventoNotSIEP);

		EventoDAO lEveDao = null;
		NotificaDAO lNotDao = null;
		AutoritaEsternaDAO lAutDao = null;

		ScambioSanzioneDAO lScambioDao = null;
		RichiestaConversioneDAO lRichiestaDao = null;
		DepositoDecretoDAO lDepDAO = null;
		DepositoOrdinanzaPcDAO lDepOrdDAO = null;
		TenoreDAO lTenDAO = null;
		PenaResiduaDAO lPenDao = null;

		try {
			lConn = getDBTransaction();

			lEveDao = new EventoDAO(lConn);
			// Se presente inserisco l'evento SIUS
			BigDecimal lKeyEventoSius = null;
			if (aEventoSIUS != null) {
				// Inserisco l'evento SIUS
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Inserisco Evento SIUS");

				lEveDao.setDAOFromModel(aEventoSIUS);
				lKeyEventoSius = lEveDao.insert();
				lEveDao.stop();

				if (aDepoDecModel != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Inserisco DEPOSITO_DECRETO");
					lDepDAO = new DepositoDecretoDAO(lConn);
					DepositoDecretoModel lDepDecretoMod = new DepositoDecretoModel(aDepoDecModel);
					lDepDecretoMod.setIdEventoGenerato(lKeyEventoSius);
					lDepDAO.setDAOFromModel(lDepDecretoMod);
					BigDecimal lKeyDepDec = lDepDAO.insert();

					// insert tenore
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Inserisco TENORE");
					lTenDAO = new TenoreDAO(lConn);
					aTenoreModel.setDepDecIdDepositoDecreto(lKeyDepDec);

					lTenDAO.setDAOFromModel(aTenoreModel);
					lTenDAO.insert();
				} else if (aDepOrdPcModel != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Inserisco DEPOSITO_ORDINANZA_PC");
					lDepOrdDAO = new DepositoOrdinanzaPcDAO(lConn);
					DepositoOrdinanzaPcModel lDepPCMod = new DepositoOrdinanzaPcModel(aDepOrdPcModel);
					lDepPCMod.setIdEventoGenerato(lKeyEventoSius);
					lDepOrdDAO.setDAOFromModel(lDepPCMod);
					BigDecimal lKeyDepOrd = lDepOrdDAO.insert();

					// Insert tenore
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Inserisco TENORE");
					lTenDAO = new TenoreDAO(lConn);
					aTenoreModel.setDepOpidDepositoOrdinanzaPc(lKeyDepOrd);

					lTenDAO.setDAOFromModel(aTenoreModel);
					lTenDAO.insert();
				}

				// Inserisco scambio Sanzione legandola all'evento SIUS
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("Inserisco SCAMBIO_SANZIONE");

				aScambioSanzioneModel.setEveIdEvento(lKeyEventoSius);
				lScambioDao = new ScambioSanzioneDAO(lConn);
				lScambioDao.setDAOFromModel(aScambioSanzioneModel);
				lScambioDao.insert();

				// Aggiorno la tabella RICHIESTA_CONVERSIONE nei campi che vengono
				// di norma valorizzati da SIUS
				if (aRichiestaConversioneModel != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Aggiorno RICHIESTA_CONVERSIONE");

					lRichiestaDao = new RichiestaConversioneDAO(lConn);

					// La richiesta conversione va agganciata all'evento SIUS
					// 27/07/2015 Non va aggiornato l'EVE_ID_EVENTO di RICHIESTA_CONVERSIONE
					// lRichiestaDao.setEveIdEvento(lKeyEventoSius);

					lRichiestaDao.setDurataEsitoAnni(aRichiestaConversioneModel.getDurataEsitoAnni());
					lRichiestaDao.setDurataEsitoMesi(aRichiestaConversioneModel.getDurataEsitoMesi());
					lRichiestaDao.setDurataEsitoGiorni(aRichiestaConversioneModel.getDurataEsitoGiorni());

					lRichiestaDao.setCodTipoSanzione(aRichiestaConversioneModel.getCodTipoSanzione());

					lRichiestaDao.setNumeroRate(aRichiestaConversioneModel.getNumeroRate());
					lRichiestaDao.setValoreRata(aRichiestaConversioneModel.getValoreRata());
					lRichiestaDao.setValoreUltimaRata(aRichiestaConversioneModel.getValoreUltimaRata());
					lRichiestaDao.setDataInizioPagamento(aRichiestaConversioneModel.getDataInizioPagamento());
					lRichiestaDao.setNumGGInizioPagamento(aRichiestaConversioneModel
							.getNumeroGiorniInizioPagamento());

					// Update in chiave
					lRichiestaDao.selCondizioneUpdate(aRichiestaConversioneModel.getIdRichiestaConversione());

					lRichiestaDao.update();
				}
			}

			// Inserisco l'evento
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Inserisco Evento SIEP");
			if (lKeyEventoSius != null) {
				// Lego l'evento SIEP all'evento SIUS
				aEventoNotSIEP.getEvento().setEveIdEvento(lKeyEventoSius);
			}

			lEveDao.setDAOFromModel(aEventoNotSIEP.getEvento());
			BigDecimal lKeyEvento = lEveDao.insert();
			lEveNotRet.getEvento().setIdEvento(lKeyEvento);

			// Inserisco la pena residua
			lPenDao = new PenaResiduaDAO(lConn);
			// Inserisce Pena Residua
			PenaResiduaModel lPenMod = aPenaResidua;
			if (lPenMod != null) {
				lPenMod.setIdPenaResidua(null);
				lPenMod.setCodOperatoreAggiornamento(null);
				lPenMod.setDataAggiornamento(null);
				lPenMod.setCodUfficioAggiornamento(null);

				lPenMod.setFlagValidato("S");
				lPenMod.setEveIdEvento(lKeyEvento);

				lPenMod.setCodOperatoreInserimento(aEventoNotSIEP.getEvento().getCodOperatoreInserimento());
				lPenMod.setCodUfficioInserimento(aEventoNotSIEP.getEvento().getCodUfficioInserimento());
				lPenMod.setDataInserimento(aEventoNotSIEP.getEvento().getDataInserimento());

				lPenDao.setDAOFromModel(lPenMod);
				lPenDao.insert();
				lPenDao.stop();
			}

			// Inserisco i destinatari
			lNotDao = new NotificaDAO(lConn);
			lAutDao = new AutoritaEsternaDAO(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Inserisco I detinatari");
			BigDecimal lKeyAutorita = null;
			int count = 0;

			if (aEventoNotSIEP != null && aEventoNotSIEP.getNotifiche() != null) {

				while (count < aEventoNotSIEP.getNotifiche().length) {
					if (aEventoNotSIEP.getNotifiche()[count] != null) {
						if (aEventoNotSIEP.getNotifiche()[count].getAutoritaEsterna() != null) {
							lAutDao.setRicercaByAutSede(aEventoNotSIEP.getNotifiche()[count]
									.getAutoritaEsterna());
							AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
							lAutMod = (AutoritaEsternaModel) lAutDao.getModelByKey();

							if (lAutMod == null) {
								lAutDao.setDAOFromModel(aEventoNotSIEP.getNotifiche()[count]
										.getAutoritaEsterna());
								lKeyAutorita = lAutDao.insert();
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
								siesLogger.debug("Inserita AUTORITA con ID = " + lKeyAutorita);
								aEventoNotSIEP.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							} else {
								lKeyAutorita = lAutMod.getIdAutoritaEsterna();
								aEventoNotSIEP.getNotifiche()[count].setAutEstIdAutoritaEsterna(lKeyAutorita);
							}
						}

						aEventoNotSIEP.getNotifiche()[count].setEveIdEvento(lKeyEvento);

						// Inserisco la notifica
						lNotDao.setDAOFromModel(aEventoNotSIEP.getNotifiche()[count]);
						lNotDao.insert();
						lNotDao.stop();

					}
					count++;
				}
			}

			// rollback(lConn);
			commit(lConn);

		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: ", ex);
			rollback(lConn);
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire l'Annotazione Conversione! ");
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: ", e);
			rollback(lConn);
			throw new F3BException("SanzioneSostitutivaController.ExInserisciDecisioneSorveglianza: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lNotDao);
			cleanup(lAutDao);

			cleanup(lDepDAO);
			cleanup(lDepOrdDAO);
			cleanup(lTenDAO);

			cleanup(lPenDao);

			cleanup(lScambioDao);
			cleanup(lRichiestaDao);

			cleanup(lConn);
		}

		return lEveNotRet;

	}

	/**
	 * Metodo utilizzato per l'inserimento di un fascicolo di pena detentiva (CLasse I) a partire da un
	 * Fascicolo Pena ecuniaria (CLasse VII). Duplica il soggetto per non incorrere nell'errore sulla chiave
	 * univoca fascicolo-soggetto-sentenza e comunque in linea con le nuove disposizioni del SuperSoggetto.
	 * Inserisce evento di Annotazione Revoca Conversione Sanzione Sostitutiva x Pene Pecuniarie sul Fascicolo
	 * di classe VII Archivia il Fascicolo di classe VII Inserisce il nuovo fascicolo di pena detentiva
	 * (Classe I)
	 * 
	 * Duplica le PeneAccessorie e i Benefici ( dal procedimento Classe VII al Classe I)
	 * 
	 * @param: aSoggetto Model con i dati da inserire aEvento Model con i dati da inserire aFascicoloSiep
	 *         Model con i dati da inserire
	 * @return il model aFascicolo con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 */
	public FascicoloSiepModel ExInserisciFascicolodaClasseVII(SoggettoModel aSoggetto, EventoModel aEvento,
			FascicoloSiepModel aFascicoloSiep, DettaglioFascicoloModel aDettaglioFascicolo,
			FascicoloSiepModel aFascicoloClasseVIISiep, AnnotazioneManualeModel AnnMan,
			StatoProcedimentoModel aStatoProcMod) throws F3BException {
		Connection lConn = null;

		SoggettoDAO lSogDao = null;
		EventoDAO lEveDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		EventoDAO lEveDaoRevo = null;
		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasDaoSql = null;
		ResidenzaDAO lResDao = null;
		ResidenzaFascicoloSiepDAO lResFasDao = null;
		PosizioneGiuridicaDAO lPosDao = null;
		MagistratoCompetenteDAO lMagComDao = null;
		ReatoDAO lReaDao = null;
		CircostanzaDAO lCirDao = null;
		PenaComplessivaDAO lPenDao = null;
//		PenaResiduaDAO lPenResDao = null;

		try {
			lConn = getDBTransaction();
			// duplico il soggetto
			lSogDao = new SoggettoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggetto);
			BigDecimal lSequence = lSogDao.insert();
			aSoggetto.setIdSoggetto(lSequence);

			// ========================================================================
			// preparo il nuovo fascicolo classe I collegandolo al nuovo soggetto
			// ========================================================================
			aFascicoloSiep.setSogIdSoggetto(lSequence);

			lFasDao = new FascicoloSiepDAO(lConn);
			// Cerco il Progressivo rispettivamente al tipo progressivo impostato
			lFasDaoSql = new FascicoloSiepSqlDAO(lConn);
			int nTipo = 1;
			aFascicoloSiep.setTipoProgressivo(nTipo);
			aFascicoloSiep.setCodStatoFascicolo("02"); // Iscritto
			lFasDaoSql.getProgressivoFascicoloSiep(aFascicoloSiep);
			lFasDaoSql.start();
			int lMaxProgr = 0;

			if (lFasDaoSql.next() && (lFasDaoSql.getInt("aMAX") > 0))
				lMaxProgr = lFasDaoSql.getInt("aMAX");
			lFasDaoSql.stop();

			// Setto la ChiaveProgressivo del Model con il MAX + 1 a seconda del tipo...
			int lTipoProgr = aFascicoloSiep.getTipoProgressivo();
			if (lMaxProgr == 0) {
				if (lTipoProgr == 1)
					aFascicoloSiep.setChiaveProgr(new BigDecimal(1));
				else
					aFascicoloSiep.setChiaveProgr(new BigDecimal(lTipoProgr * 10000 + 1));
			} else {
				aFascicoloSiep.setChiaveProgr(new BigDecimal(lMaxProgr + 1));
			}

			// Inserimento del nuovo fascicolo di classe I
			lFasDao.setDAOFromModel(aFascicoloSiep);
			lSequence = lFasDao.insert();
			aFascicoloSiep.setIdFascicoloSiep(lSequence);

			// Inserimento della Pena Complessiva (dal classe VII)
			lPenDao = new PenaComplessivaDAO(lConn);
			if (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva() != null) {
				if (aDettaglioFascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() != null) {
					PenaComplessivaModel lPenMod = aDettaglioFascicolo
							.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();
					lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lPenMod.setDataInserimento(DateUtils.getSysDate());
					// 31/07/2015 Valorizzazione dei quantum della FORM di inserimento
					lPenMod.setNumAnniReclusione(aDettaglioFascicolo.getPenaResidua().getNumAnniReclusione());
					lPenMod.setNumMesiReclusione(aDettaglioFascicolo.getPenaResidua().getNumMesiReclusione());
					lPenMod.setNumGiorniReclusione(aDettaglioFascicolo.getPenaResidua()
							.getNumGiorniReclusione());
					lPenMod.setNumAnniArresto(aDettaglioFascicolo.getPenaResidua().getNumAnniArresto());
					lPenMod.setNumMesiArresto(aDettaglioFascicolo.getPenaResidua().getNumMesiArresto());
					lPenMod.setNumGiorniArresto(aDettaglioFascicolo.getPenaResidua().getNumGiorniArresto());
					lPenMod.setImportoMulta(new BigDecimal("0"));
					lPenMod.setImportoAmmenda(new BigDecimal("0"));
					lPenMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lPenMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lPenMod.setDataInserimento(DateUtils.getSysDate());
					lPenMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lPenDao.setDAOFromModel(lPenMod);
					lSequence = lPenDao.insert();
				}
			}

			/*
			 * 31/07/2015 non va inserita la Pena Residua. Inserimento della Pena Residua (dal classe VII, con
			 * adeguamento dei quantum dalla FORM di inserimento ) lPenResDao = new PenaResiduaDAO(lConn); if
			 * (aDettaglioFascicolo.getPenaResidua()!= null){ PenaResiduaModel lPenResMod =
			 * aDettaglioFascicolo.getPenaResidua();
			 * lPenResMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			 * lPenResMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			 * lPenResMod.setDataInserimento(DateUtils.getSysDate());
			 * lPenResMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			 * lPenResDao.setDAOFromModel(lPenResMod ); lSequence = lPenResDao.insert(); } else { // In
			 * assenza della PenaResidua sul classe VII, istanzio un nuovo PenaResiduaModel e lo carico dei
			 * quantum impostati in aDettaglioFascicolo) PenaResiduaModel lPenResMod = new PenaResiduaModel();
			 * lPenResMod.setNumAnniReclusione (aDettaglioFascicolo.getPenaResidua().getNumAnniReclusione());
			 * lPenResMod.setNumMesiReclusione (aDettaglioFascicolo.getPenaResidua().getNumMesiReclusione());
			 * lPenResMod.setNumGiorniReclusione
			 * (aDettaglioFascicolo.getPenaResidua().getNumGiorniReclusione()); lPenResMod.setNumAnniArresto
			 * (aDettaglioFascicolo.getPenaResidua().getNumAnniArresto()); lPenResMod.setNumMesiArresto
			 * (aDettaglioFascicolo.getPenaResidua().getNumMesiArresto()); lPenResMod.setNumGiorniArresto
			 * (aDettaglioFascicolo.getPenaResidua().getNumGiorniArresto());
			 * lPenResMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			 * lPenResMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			 * lPenResMod.setDataInserimento(DateUtils.getSysDate());
			 * lPenResMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			 * lPenResDao.setDAOFromModel(lPenResMod ); lSequence = lPenResDao.insert(); }
			 */

			// Stato_procedimento prendo l'ultimo statoprocedimento della lista (size-1)
			StatoProcedimentoDAO lStaDao = null;
			lStaDao = new StatoProcedimentoDAO(lConn);
			StatoProcedimentoModel lStaMod = (StatoProcedimentoModel) aDettaglioFascicolo
					.getStatoProcedimento().get(aDettaglioFascicolo.getStatoProcedimento().size() - 1);
			lStaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lStaMod.setProgressivo(new BigDecimal(1));
			lStaMod.setCodStatoProcedimento("0108"); // iscritto
			 // 19/02/2016 lStaMod.setData(null);
      		lStaMod.setData(DateUtils.getSysDate());	// 19/02/2016
			lStaDao.setDAOFromModel(lStaMod);
			lStaDao.insert();

			// Residenza.
			if (aDettaglioFascicolo.getResidenza() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getResidenza();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				lSequence = lResDao.insert();

				// Si collega la residenza al nuovo fascicolo.
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lSequence);
				lResFasDao.setDAOFromModel(lResFasMod);
				lSequence = lResFasDao.insert();
			}

			// Si duplica il domicilio collegandolo al nuovo soggetto.
			if (aDettaglioFascicolo.getDomicilio() != null) {
				lResDao = new ResidenzaDAO(lConn);
				ResidenzaModel lResMod = aDettaglioFascicolo.getDomicilio();
				lResMod.setSogIdSoggetto(aFascicoloSiep.getSogIdSoggetto());
				lResDao.setDAOFromModel(lResMod);
				lSequence = lResDao.insert();

				// Si collega il domicilio al nuovo fascicolo
				lResFasDao = new ResidenzaFascicoloSiepDAO(lConn);
				ResidenzaFascicoloSiepModel lResFasMod = new ResidenzaFascicoloSiepModel();
				lResFasMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lResFasMod.setResIdResidenza(lSequence);
				lResFasDao.setDAOFromModel(lResFasMod);
				lSequence = lResFasDao.insert();
			}

			// Si duplica la posizione giuridica del classe VII collegandola al nuovo fascicolo
			if (aDettaglioFascicolo.getPosizioneGiuridica() != null) {
				lPosDao = new PosizioneGiuridicaDAO(lConn);
				PosizioneGiuridicaModel lPosMod = aDettaglioFascicolo.getPosizioneGiuridica();
				lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lPosDao.setDAOFromModel(lPosMod);
				lSequence = lPosDao.insert();
			}

			/*
			 * // Si crea la posizione giuridica a libero lPosDao = new PosizioneGiuridicaDAO(lConn);
			 * PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
			 * lPosMod.setCodPosizioneGiuridica("07"); lPosMod.setCodPosizioneProcessuale("-");
			 * lPosMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			 * lPosMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			 * lPosMod.setDataInserimento(DateUtils.getSysDate());
			 * lPosMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			 * lPosDao.setDAOFromModel(lPosMod ); lSequence = lPosDao.insert();
			 */

			// Si duplica il magistrato collegandolo al nuovo fascicolo
			if (aDettaglioFascicolo.getMagistratoCompetente() != null) {
				if (aDettaglioFascicolo.getMagistratoCompetente().getMagistratoCompetente() != null) {
					lMagComDao = new MagistratoCompetenteDAO(lConn);

					MagistratoCompetenteMagistratoModel lMagistrato = aDettaglioFascicolo
							.getMagistratoCompetente();
					MagistratoCompetenteModel lMagMod = lMagistrato.getMagistratoCompetente();

					lMagMod = lMagistrato.getMagistratoCompetente();
					lMagMod.setMagCodMagistrato(lMagistrato.getMagistrato().getCodMagistrato());
					lMagMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lMagComDao.setDAOFromModel(lMagMod);
					lMagComDao.insert();
				}
			}

			// Insert Reato e le Circostanze sempre e solo da tabella REATO
			if (aDettaglioFascicolo.getReatiCircostanze() != null) {
				// prendo la lista dei reati e la metto nel model comune
				lReaDao = new ReatoDAO(lConn);
				List lLisReaCir = aDettaglioFascicolo.getReatiCircostanze();
				ReatoModel lReaMod = new ReatoModel();

				// Si cicla su questa lista
				for (int i = 0; i <= lLisReaCir.size() - 1; i++) {
					// Si scrive il reato.
					lReaMod = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getReato();
					lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lReaDao.setDAOFromModel(lReaMod);
					lSequence = lReaDao.insert();

					// Si prende la lista delle eventuali circostanze incapsulate nella tabella reati.
					ReatoModel[] lLisCir = ((ReatoCircostanzaModel) lLisReaCir.get(i)).getCircostanze();
					// Si ciclano le n° circostanze incluse nella tabella REATI
					for (int j = 0; j <= lLisCir.length - 1; j++) {
						// Si scrive la circostanza.
						lReaMod = lLisCir[j];
						// scrivo la circostanza
						lReaMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
						lReaDao.setDAOFromModel(lReaMod);
						lSequence = lReaDao.insert();
					}
				}
			}

			// Circostanze.
			if (aDettaglioFascicolo.getCircostanze() != null) {
				CircostanzaModel lCirMod = new CircostanzaModel();
				List lLisCir = aDettaglioFascicolo.getCircostanze();
				lCirDao = new CircostanzaDAO(lConn);
				for (int i = 0; i <= lLisCir.size() - 1; i++) {
					lCirMod = (CircostanzaModel) lLisCir.get(i);
					lCirMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lCirDao.setDAOFromModel(lCirMod);
					lSequence = lCirDao.insert();
				}
			}

			// Duplicazione PeneAccessorie e Benefici
			int Kcont = 0;
			BigDecimal lSequenceBen = null;

			BigDecimal[] lidBeneficio;
			lidBeneficio = new BigDecimal[10];

			BigDecimal[] lidBeneficioNEW;
			lidBeneficioNEW = new BigDecimal[10];

			if (aDettaglioFascicolo.getPeneAccessorie() != null) {
				List listaPeneAcc = aDettaglioFascicolo.getPeneAccessorie();

				for (int i = 0; i <= listaPeneAcc.size() - 1; i++) {
					PenaAccessoriaDAO lPenAccDao = null;
					PenaAccessoriaModel lPenAccMod = new PenaAccessoriaModel(
							(PenaAccessoriaModel) listaPeneAcc.get(i));

					lPenAccMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lPenAccMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lPenAccMod.setDataInserimento(aFascicoloSiep.getDataInserimento());
					lPenAccMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lPenAccMod.setCodOperatoreAggiornamento(null);
					lPenAccMod.setCodUfficioAggiornamento(null);
					lPenAccMod.setDataAggiornamento(null);

					// Controllo che P.Acc. non sia legata ad un Beneficio : Se si, bisogna scrivere prima il
					// Beneficio
					if (lPenAccMod.getBenIdBeneficio() != null) {
						List listaBenefici = aDettaglioFascicolo.getBenefici();

						// Si cerca il Beneficio a cui è legata la Pena Acc.
						for (int j = 0; j <= listaBenefici.size() - 1; j++) {
							BeneficioDAO lBeneDao = null;
							BeneficioModel lBeneMod = new BeneficioModel(
									(BeneficioModel) listaBenefici.get(j));
							if (lBeneMod.getIdBeneficio().equals(lPenAccMod.getBenIdBeneficio())) {
								// controllo che il Beneficio non sia stato già scritto
								String ScrivoSiNo = "SI";
								for (int ic = 0; ic <= lidBeneficio.length - 1; ic++) {
									if (lBeneMod.getIdBeneficio().equals(lidBeneficio[ic])) {
										ScrivoSiNo = "NO";
										lSequenceBen = lidBeneficioNEW[ic];
									}
								}

								if (ScrivoSiNo.equals("SI")) {
									lBeneMod.setCodOperatoreInserimento(aFascicoloSiep
											.getCodOperatoreInserimento());
									lBeneMod.setCodUfficioInserimento(aFascicoloSiep
											.getCodUfficioInserimento());
									lBeneMod.setDataInserimento(DateUtils.getSysDate());
									lBeneMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
									lBeneMod.setCodOperatoreAggiornamento(null);
									lBeneMod.setCodUfficioAggiornamento(null);
									lBeneMod.setDataAggiornamento(null);

									lBeneDao = new BeneficioDAO(lConn);
									lBeneDao.setDAOFromModel(lBeneMod);
									lSequence = lBeneDao.insert();
									lBeneDao.stop();
									// Si annota id del beneficio di classe VII e l'id appena scritto del
									// Beneficio in classe I
									lidBeneficio[Kcont] = lBeneMod.getIdBeneficio();
									lidBeneficioNEW[Kcont] = lSequence;
									Kcont++;
									// scrivo PenaAccess legata al Beneficio
									lPenAccMod.setBenIdBeneficio(lSequence);
									lPenAccDao = new PenaAccessoriaDAO(lConn);
									lPenAccDao.setDAOFromModel(lPenAccMod);
									lSequence = lPenAccDao.insert();
									lPenAccDao.stop();
								} else {
									// Si scrive solo la Pena Acc. legata ad un Beneficio già scritto in
									// precedenza.
									lPenAccMod.setBenIdBeneficio(lSequenceBen);
									lPenAccDao = new PenaAccessoriaDAO(lConn);
									lPenAccDao.setDAOFromModel(lPenAccMod);
									lSequence = lPenAccDao.insert();
									lPenAccDao.stop();
								}
							}
							cleanup(lBeneDao);
						} // End ciclo for
					} // End if(lPenAccMod.getBenIdBeneficio() != null)
					else {
						lPenAccDao = new PenaAccessoriaDAO(lConn);
						lPenAccDao.setDAOFromModel(lPenAccMod);
						lSequence = lPenAccDao.insert();
						lPenAccDao.stop();
					}

					cleanup(lPenAccDao);
				} // End ciclo for (int i=0
			} // End if (aDettaglioFascicolo.getPeneAccessorie() != null)

			// --> BENEFICI -- (solo quelli che non erano legati alle PeneAccessorie)

			if (aDettaglioFascicolo.getBenefici() != null) {
				List listaBenefici = aDettaglioFascicolo.getBenefici();
				for (int i = 0; i <= listaBenefici.size() - 1; i++) {
					BeneficioDAO lBeneDao = null;
					BeneficioModel lBeneMod = new BeneficioModel((BeneficioModel) listaBenefici.get(i));
					lBeneMod.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
					lBeneMod.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
					lBeneMod.setDataInserimento(DateUtils.getSysDate());
					lBeneMod.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
					lBeneMod.setCodOperatoreAggiornamento(null);
					lBeneMod.setCodUfficioAggiornamento(null);
					lBeneMod.setDataAggiornamento(null);

					String ScrivoSiNo = "SI";
					for (int ic = 0; ic <= lidBeneficio.length - 1; ic++) {
						// Controllo che non è un Beneficio legato alle Pene Pec. e che quindi ho già scritto
						if (lBeneMod.getIdBeneficio().equals(lidBeneficio[ic]))
							ScrivoSiNo = "NO";
					}

					if (ScrivoSiNo.equals("SI")) {
						lBeneDao = new BeneficioDAO(lConn);
						lBeneDao.setDAOFromModel(lBeneMod);
						lSequence = lBeneDao.insert();
						lBeneDao.stop();
					}

					cleanup(lBeneDao);

				} // End ciclo for

			} // End if

			// Fine duplicazione PeneAccessorie e Benefici da Classe VII a classe I

			// =============================================================================
			// Validazione dell'Evento di Conversione/Revoca per il fascicolo di classe VII
			// =============================================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataAggiornamento(aFascicoloSiep.getDataAggiornamento());
			lEveDao.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreAggiornamento());
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selCondizioneUpdate(aEvento.getEveIdEvento());
			lEveDao.update();
			lEveDao.stop();

			// 04/08/2015 Inserimento stato procedimento del classe VII
			// - Cancella eventuali record prima di inserire un nuovo STATO_PROCEDIMENTO
			lStaDao = new StatoProcedimentoDAO(lConn);
			lStaDao.setCondizioneByIdFascicolo(aStatoProcMod.getFasSieIdFascicoloSiep());
			lStaDao.delete();

			// - Inserisce
			aStatoProcMod.setEveIdEvento(aEvento.getEveIdEvento());
			lStaDao.setDAOFromModel(aStatoProcMod);
			lStaDao.insert();
			lStaDao.stop();

			// ====================================================================
			// Validazione dell'Evento di Ufficio di Sorveglianza iscritto da SIEP
			// ====================================================================
			// Lettura dell'evento di Conversione/Revoca per puntare all'evento di Ufficio di Sorveglianza
			// iscritto da SIEP tramite EVE_ID_EVENTO.
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aEvento.getEveIdEvento());
			EventoModel lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();
			if (lEveMod != null) {
				lEveDao = new EventoDAO(lConn);
				lEveDao.setDataAggiornamento(aFascicoloSiep.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioAggiornamento());
				lEveDao.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreAggiornamento());
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selCondizioneUpdate(lEveMod.getEveIdEvento());
				lEveDao.update();
				lEveDao.stop();
			}

			// ========================================================================
			// Archivio il vecchio fascicolo di classe VII
			// ========================================================================
			FascicoloSiepDAO lFascDao = new FascicoloSiepDAO(lConn);
			lFascDao.selCondizioneUpdate(aFascicoloClasseVIISiep.getIdFascicoloSiep());
			lFascDao.setCodStatoFascicolo("01"); // 01 - Archiviato/Definito
			lFascDao.setDataArchiviazione(DateUtils.getSysDate());
			lFascDao.setCodMotivoArchiviazione("10"); // 10 - Provvedimento altra Autorità
			lFascDao.setFlagValidato("S");
			lFascDao.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
			lFascDao.setCodOperatoreAggiornamento(aFascicoloSiep.getCodOperatoreAggiornamento());
			lFascDao.setCodUfficioAggiornamento(aFascicoloSiep.getCodUfficioAggiornamento());
			lFascDao.setDataAggiornamento(aFascicoloSiep.getDataAggiornamento());

			lFascDao.update();
			lFascDao.stop();

			// /=========================================================================================================================================
			// / Si Inserisce l'evento di Revoca/Conversione Sanzione Sostitutiva in pena detentiva (Art. 66
			// L. 689/81 per Pene Pecuniarie) in classe VII
			// /=========================================================================================================================================
			// /lEveDao=new EventoDAO(lConn);
			// /lEveDao.setDAOFromModel(aEvento);
			// /BigDecimal lSequenceEveVII=lEveDao.insert();

			// /====================================================================================================
			// / Si inseriscono i dati della sentenza di revoca nella tab ANNOTAZIONE_MANUALE classe VII se
			// non sono
			// / stati trovati nel distretto ma inseriti a mano dall'utente
			// /====================================================================================================
			// /if (!AnnMan.equals(null)){
			// / // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// / siesLogger.debug("ANNOTAZIONE per i dati della sentenza->" + AnnMan);
			// / AnnotazioneManualeDAO lAnnotazioneManualeDao = new AnnotazioneManualeDAO(lConn);
			// / AnnMan.setEveIdEvento(aEvento.getEveIdEvento());
			// / lAnnotazioneManualeDao.setDAOFromModel(AnnMan);
			// / BigDecimal lSeqAnn=lAnnotazioneManualeDao.insert();
			// / lEveDao=new EventoDAO(lConn);
			// / lEveDao.setAnnIdAnnotazioneManuale(lSeqAnn);
			// / lEveDao.selCondizioneUpdate(aEvento.getEveIdEvento());
			// / lEveDao.update();
			// /}

			// ================================================================
			// Se è REVOCA BENEFICIO, Si duplica anche in Classe I
			// EVENTO e ANNOTAZIONE_MANUALE
			// ================================================================

			BigDecimal lSequenceEveI = null;
			if (aEvento.getCodTipoEvento().equals("01") && aEvento.getCodTipoProvvedimento().equals("25")
					&& (aEvento.getCodMotivo().equals("0261") || aEvento.getCodMotivo().equals("0262"))) // 10/04/2015
																											// Provvedimenti
																											// Conversione/Revoca
																											// Sanzione
																											// Sostitutiva
																											// in
																											// pena
																											// detentiva
																											// (Art.
																											// 66
																											// L.
																											// 689/81
																											// per
																											// Pene
																											// Pecuniarie)
			{
				lEveDaoRevo = new EventoDAO(lConn);
				lEveDaoRevo.setDAOFromModel(aEvento);

				lEveDaoRevo.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());
				lSequenceEveI = lEveDaoRevo.insert();
			}

			// ========================================================================
			// Annotazione_Manuale
			// ========================================================================

			AnnotazioneManualeDAO lAnnotazManClasseIDao = null;

			if (!AnnMan.equals(null) && lSequenceEveI != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug("ANNOTAZIONE per i dati della sentenza in Classe I -> ");
				lAnnotazManClasseIDao = new AnnotazioneManualeDAO(lConn);

				AnnMan.setEveIdEvento(lSequenceEveI);
				AnnMan.setFasSieIdFascicoloSiep(aFascicoloSiep.getIdFascicoloSiep());

				lAnnotazManClasseIDao.setDAOFromModel(AnnMan);
				BigDecimal lSeqAnnClI = lAnnotazManClasseIDao.insert();

				// Lego EVENTO Claase I all'ANNOTAZIONE_MANUALE
				lEveDaoRevo = new EventoDAO(lConn);
				lEveDaoRevo.setAnnIdAnnotazioneManuale(lSeqAnnClI);
				lEveDaoRevo.selCondizioneUpdate(lSequenceEveI);
				lEveDaoRevo.update();

				cleanup(lAnnotazManClasseIDao);
				cleanup(lEveDaoRevo);
			}

			// ======================================================================
			// Si Inserisce lo Stato del procedimento per il fascicolo di classe VII
			// ======================================================================
			StatoProcedimentoDAO lStatoProcDao = null;
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setCondizioneByIdFascicolo(aFascicoloClasseVIISiep.getIdFascicoloSiep());
			lStatoProcDao.delete();
			lStatoProcDao.stop();

			StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			lStat.setFasSieIdFascicoloSiep(aFascicoloClasseVIISiep.getIdFascicoloSiep());
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setDataInserimento(DateUtils.getSysDate());
			lStat.setCodUfficioInserimento(aFascicoloSiep.getCodUfficioInserimento());
			lStat.setCodOperatoreInserimento(aFascicoloSiep.getCodOperatoreInserimento());
			lStat.setData(DateUtils.getSysDate());	// 19/02/2016
			// 31/05/2016 Impostazione corretta di Stato Procedimento relativo all'Evento di
			// Revoca/Conversione
			// lStat.setCodStatoProcedimento("0349"); //Definito - Archiviazione per Revoca Beneficio ex artt
			// 168 c.p.-674 c.p.p.
			// Lettura dell'Evento di Revoca/Conversione
			EventoSqlDAO lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aEvento.getEveIdEvento());
			EventoModel lEveModRC = (EventoModel) lEveSqlDao.getModelByKey();
			lEveSqlDao.stop();
			if (lEveModRC.getCodMotivo().equals("1011"))
				lStat.setCodStatoProcedimento("0262");
			else
				lStat.setCodStatoProcedimento("0263");
			// 31/05/2016 fine
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"RichiestaConversioneController.ExInserisciFascicolodaClasseVII: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lEveDao);
			cleanup(lFasDao);
			cleanup(lFasDaoSql);
			cleanup(lResDao);
			cleanup(lResFasDao);
			cleanup(lPosDao);
			cleanup(lMagComDao);
			cleanup(lReaDao);
			cleanup(lCirDao);
			cleanup(lPenDao);
			cleanup(lEveDaoRevo);
			cleanup(lEveSqlDAO);

			cleanup(lConn);

		}
		return aFascicoloSiep;
	}

	/**
	 * Archiviazione del fascicolo SIEP di classe VII con validazione dei provvedimenti: - Annotazione
	 * Archiviazione per Conversione Sanzione Sostitutiva in pena detentiva (Art. 66 L. 689/81 per Pene
	 * Pecuniarie) - Sanzioni Sostitutive Delle Pene Detentive Brevi (Art. 66-108 L. 689/1981) -->
	 * (Provvedimento della sorveglianza iscritto da SIEP) Eventuale riapertura da Archiviazione del fascicolo
	 * SIEP di classe I se lFlagPenaScaduta = "N".
	 * 
	 * @param FascicoloSiepModel
	 *            aFasClasseVII, BigDecimal aIdEvento, String lFlagPenaScaduta.
	 * @return aFasModel
	 * @throws F3BException
	 * @author Luigi
	 */
	public FascicoloSiepModel ExArchiviazioneClasseVII(FascicoloSiepModel aFasClasseVII,
			BigDecimal aIdEvento, String lFlagPenaScaduta) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info(
				">>>>>>>>>>>>>>>>>>>>>>>>>  FascicoloSiepModel = " + aFasClasseVII.toString()
						+ " <<<<<<<<<<<<<<<<<<<<<<<<<<");
		Connection lConn = null;

		FascicoloSiepDAO lFasDao = null;
		FascicoloSiepSqlDAO lFasSqlDao = null;
		StatoProcedimentoDAO lStatoProcDao = null;
		EventoSqlDAO lEveSqlDAO = null;
		EventoDAO lEveDao = null;
		RiaperturaFascicoloSiepDAO lRiapFasSie = null;
		PenaResiduaDAO lPenResDao = null;

		try {
			lConn = getDBTransaction();

			// =============================================================================
			// Validazione dell'Evento di Conversione/Revoca per il fascicolo di classe VII
			// =============================================================================
			lEveDao = new EventoDAO(lConn);
			lEveDao.setDataAggiornamento(aFasClasseVII.getDataAggiornamento());
			lEveDao.setCodUfficioAggiornamento(aFasClasseVII.getCodUfficioAggiornamento());
			lEveDao.setCodOperatoreAggiornamento(aFasClasseVII.getCodOperatoreAggiornamento());
			lEveDao.setFlagDocumentoRegistrato("S");
			lEveDao.selCondizioneUpdate(aIdEvento);
			lEveDao.update();
			// Lettura dell'Evento
			EventoSqlDAO lEveSqlDao = new EventoSqlDAO(lConn);
			lEveSqlDao.ricercaEventoByKey(aIdEvento);
			EventoModel lEveModCR = (EventoModel) lEveDao.getModelByKey();
			lEveSqlDao.stop();

			// ====================================================================
			// Validazione dell'Evento di Ufficio di Sorveglianza iscritto da SIEP
			// ====================================================================
			// Lettura dell'evento di Conversione/Revoca per puntare all'evento di Ufficio di Sorveglianza
			// iscritto da SIEP tramite EVE_ID_EVENTO.
			lEveSqlDAO = new EventoSqlDAO(lConn);
			lEveSqlDAO.ricercaEventoByKey(aIdEvento);
			EventoModel lEveMod = (EventoModel) lEveSqlDAO.getModelByKey();
			if (lEveMod != null) {
				lEveDao = new EventoDAO(lConn);
				lEveDao.setDataAggiornamento(aFasClasseVII.getDataAggiornamento());
				lEveDao.setCodUfficioAggiornamento(aFasClasseVII.getCodUfficioAggiornamento());
				lEveDao.setCodOperatoreAggiornamento(aFasClasseVII.getCodOperatoreAggiornamento());
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.selCondizioneUpdate(lEveMod.getEveIdEvento());
				lEveDao.update();
				lEveDao.stop();
			}

			// ====================================================================
			// Archiviazione FASCICOLO
			// ====================================================================
			lFasDao = new FascicoloSiepDAO(lConn);
			lFasDao.setCodOperatoreAggiornamento(aFasClasseVII.getCodOperatoreAggiornamento());
			lFasDao.setDataAggiornamento(aFasClasseVII.getDataAggiornamento());
			lFasDao.setCodUfficioAggiornamento(aFasClasseVII.getCodUfficioAggiornamento());
			lFasDao.setDataArchiviazione(aFasClasseVII.getDataAggiornamento());
			lFasDao.setCodStatoFascicolo("01"); // 01 - Archiviato/Definito
			lFasDao.setCodMotivoArchiviazione("10"); // 10 - Provvedimento altra Autorità

			lFasDao.selCondizioneUpdate(aFasClasseVII.getIdFascicoloSiep());
			lFasDao.update();
			lFasDao.stop();

			// ======================================================================
			// Si Inserisce lo Stato del procedimento per il fascicolo di classe VII
			// ======================================================================
			lStatoProcDao = null;
			lStatoProcDao = new StatoProcedimentoDAO(lConn);
			lStatoProcDao.setCondizioneByIdFascicolo(aFasClasseVII.getIdFascicoloSiep());
			lStatoProcDao.delete();
			lStatoProcDao.stop();

			StatoProcedimentoModel lStat = new StatoProcedimentoModel();
			lStat.setFasSieIdFascicoloSiep(aFasClasseVII.getIdFascicoloSiep());
			lStat.setProgressivo(new BigDecimal(1));
			lStat.setDataInserimento(DateUtils.getSysDate());
			lStat.setData(DateUtils.getSysDate());
			lStat.setCodUfficioInserimento(aFasClasseVII.getCodUfficioAggiornamento());
			lStat.setCodOperatoreInserimento(aFasClasseVII.getCodOperatoreAggiornamento());
			// 31/05/2016 Impostazione corretta di Stato Procedimento relativo all'Evento di
			// Revoca/Conversione
			// lStat.setCodStatoProcedimento("0349"); //Definito - Archiviazione per Revoca Beneficio ex artt
			// 168 c.p.-674 c.p.p.
			if (lEveModCR.getCodMotivo().equals("1011"))
				lStat.setCodStatoProcedimento("0262");
			else
				lStat.setCodStatoProcedimento("0263");
			// 31/05/2016 fine
			lStatoProcDao.setDAOFromModel(lStat);
			lStatoProcDao.insert();

			// 04/08/2015 Eventuale riapertura del fascicolo di classe I
			if (aFasClasseVII.getFasSieIdFascicoloSiep() != null) {
				// Legge il fascicolo e testa se COD_STATO_FASCICOLO = 01 (Archiviato) prima di Riaprire.
				lFasSqlDao = new FascicoloSiepSqlDAO(lConn);
				lFasSqlDao.ricercaFascicoloByKey(aFasClasseVII.getFasSieIdFascicoloSiep());
				FascicoloSiepModel lFascicolo = (FascicoloSiepModel) lFasSqlDao.getModelByKey();
				lFasSqlDao.stop();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.info("CodStatoFascicolo = " + lFascicolo.getCodStatoFascicolo());

				if (lFascicolo.getCodStatoFascicolo().compareTo("01") == 0) {

					lFasDao = new FascicoloSiepDAO(lConn);
					lFasDao.setCodOperatoreAggiornamento(aFasClasseVII.getCodOperatoreAggiornamento());
					lFasDao.setDataAggiornamento(aFasClasseVII.getDataAggiornamento());
					lFasDao.setCodUfficioAggiornamento(aFasClasseVII.getCodUfficioAggiornamento());
					lFasDao.setDataUltimaRiapertura(aFasClasseVII.getDataAggiornamento());
					lFasDao.setCodMotivoRiapertura("1017");
					lFasDao.setDataArchiviazione(null);
					lFasDao.setCodStatoFascicolo("03"); // 03 - Validato
					lFasDao.setCodMotivoArchiviazione("-");

					lFasDao.setIdFascicoloSiep(aFasClasseVII.getFasSieIdFascicoloSiep());
					lFasDao.selByKey();
					lFasDao.update();
					lFasDao.stop();

					// Inserimento della occorrenza di RIAPERTURA_FASCICOLO_SIEP.
					lRiapFasSie = new RiaperturaFascicoloSiepDAO(lConn);
					lRiapFasSie.setCodMotivo("1017");
					lRiapFasSie.setDataRiapertura(aFasClasseVII.getDataAggiornamento());
					lRiapFasSie.setDataInserimento(aFasClasseVII.getDataAggiornamento());
					lRiapFasSie.setCodOperatoreInserimento(aFasClasseVII.getCodOperatoreAggiornamento());
					lRiapFasSie.setCodUfficioInserimento(aFasClasseVII.getCodUfficioAggiornamento());
					lRiapFasSie.setFasSieIdFascicoloSiep(aFasClasseVII.getFasSieIdFascicoloSiep());
					lRiapFasSie.insert();

					// Per il fascicolo di classe I, si opera l'Inserimento di un evento (gia validato) di
					// Annotazione Riapertura procedimento
					// a seguito Revoca/Conversione Sanzione Sostitutiva in pena detentiva (tipo 01 25 1017).
					lEveDao = new EventoDAO(lConn);
					lEveDao.setDAOFromModel(lEveModCR); // Impostazione dall'evento di Revoca Conversione.
					lEveDao.setCodTipoEvento("01"); // Provvedimento
					lEveDao.setCodTipoProvvedimento("25"); // Annotazione
					lEveDao.setCodMotivo("1017"); // Riapertura procedimento a seguito ...
					lEveDao.setFlagDocumentoRegistrato("S");
					lEveDao.setFlagVideoSiep("S");
					lEveDao.setFlagStampaSiep("S");
					lEveDao.setFasSieIdFascicoloSiep(aFasClasseVII.getFasSieIdFascicoloSiep());
					lEveDao.setEveIdEvento(null);
					lEveDao.setDataInserimento(aFasClasseVII.getDataAggiornamento());
					lEveDao.setCodUfficioInserimento(aFasClasseVII.getCodUfficioAggiornamento());
					lEveDao.setCodOperatoreInserimento(aFasClasseVII.getCodOperatoreAggiornamento());
					lEveDao.setDataAggiornamento(null);
					lEveDao.setCodUfficioAggiornamento(null);
					lEveDao.setCodOperatoreAggiornamento(null);
					// 30/07/2015 Impostazione Data Emissione senza le ore.
					// lEveDao.setDataEmissione(aFasClasseVII.getDataAggiornamento());
					String lDataEmissione = DateUtils.getDateToString(aFasClasseVII.getDataAggiornamento(),
							"dd/MM/yyyy");
					lEveDao.setDataEmissione(DateUtils.getDate(lDataEmissione, "dd-MM-yyyy"));
					lEveDao.setCodEsito("-");
					lEveDao.setCodMagistrato("-");
					lEveDao.setCodLuogoDestinatario("-");
					lEveDao.setCodTipoUfficioDestinatario("-");

//					BigDecimal lKeyEvento = null;
					/*lKeyEvento = */lEveDao.insert();
					lEveDao.stop();
				}
			}

			// ====================================================================
			// Gestione caso Archiviazione con flagPenaScaduta = N
			// ====================================================================
			if (lFlagPenaScaduta.compareTo("S") != 0) {

			} else {
				// ====================================================================
				// Gestione caso Archiviazione con flagPenaScaduta = S
				// ====================================================================
				// Per il fascicolo di classe I, si opera l'Inserimento di un evento (gia validato) per
				// Provvedimento di
				// Pena Residua Manuale Per Revoca/Conversione Sanzione Sostitutiva in Pena Detentiva (tipo 01
				// 04 1018).
				lEveDao = new EventoDAO(lConn);
				lEveDao.setDAOFromModel(lEveModCR); // Impostazione dall'evento di Revoca Conversione.
				lEveDao.setCodTipoEvento("01"); // Provvedimento
				lEveDao.setCodTipoProvvedimento("04"); // Provvedimento
				lEveDao.setCodMotivo("1018"); // Pena Residua Manuale Per Revoca/Conversione Sanzione
												// Sostitutiva in Pena Detentiva ...
				lEveDao.setFlagDocumentoRegistrato("S");
				lEveDao.setFlagVideoSiep("S");
				lEveDao.setFlagStampaSiep("S");
				lEveDao.setFasSieIdFascicoloSiep(aFasClasseVII.getFasSieIdFascicoloSiep());
				lEveDao.setEveIdEvento(null);
				lEveDao.setDataInserimento(aFasClasseVII.getDataAggiornamento());
				lEveDao.setCodUfficioInserimento(aFasClasseVII.getCodUfficioAggiornamento());
				lEveDao.setCodOperatoreInserimento(aFasClasseVII.getCodOperatoreAggiornamento());
				lEveDao.setDataAggiornamento(null);
				lEveDao.setCodUfficioAggiornamento(null);
				lEveDao.setCodOperatoreAggiornamento(null);
				// 30/07/2015 Impostazione Data Emissione senza le ore.
				// lEveDao.setDataEmissione(aFasClasseVII.getDataAggiornamento());
				String lDataEmissione = DateUtils.getDateToString(aFasClasseVII.getDataAggiornamento(),
						"dd/MM/yyyy");
				lEveDao.setDataEmissione(DateUtils.getDate(lDataEmissione, "dd-MM-yyyy"));
				lEveDao.setCodEsito("-");
				lEveDao.setCodMagistrato("-");
				lEveDao.setCodLuogoDestinatario("-");
				lEveDao.setCodTipoUfficioDestinatario("-");

				BigDecimal lKeyEvento = null;
				lKeyEvento = lEveDao.insert();
				lEveDao.stop();

				// Si opera l'Inserimento di una pena residua con tutti i quantum a zero, e con EVE_ID_EVENTO
				// pari all'ID dell'EVENTO appena inserito.
				// Si Azzerano i quantum di reclusione, Arresto, e date di espiazione.
				PenaResiduaModel lPenModIns = new PenaResiduaModel();

				lPenModIns.setNumAnniReclusione(new BigDecimal(0));
				lPenModIns.setNumMesiReclusione(new BigDecimal(0));
				lPenModIns.setNumGiorniReclusione(new BigDecimal(0));

				lPenModIns.setNumAnniArresto(new BigDecimal(0));
				lPenModIns.setNumMesiArresto(new BigDecimal(0));
				lPenModIns.setNumGiorniArresto(new BigDecimal(0));

				lPenModIns.setDataInizio(null);
				lPenModIns.setDataFineReclusione(null);
				lPenModIns.setDataInizioArresto(null);
				lPenModIns.setDataFinePresunta(null);
				lPenModIns.setDataFine(null);

				lPenModIns.setFlagValidato("S");

				lPenModIns.setIdPenaResidua(null);
				lPenModIns.setEveIdEvento(lKeyEvento);
				lPenModIns.setFasSieIdFascicoloSiep(aFasClasseVII.getFasSieIdFascicoloSiep());

				lPenModIns.setCodOperatoreInserimento(aFasClasseVII.getCodOperatoreAggiornamento());
				lPenModIns.setCodUfficioInserimento(aFasClasseVII.getCodUfficioAggiornamento());
				lPenModIns.setDataInserimento(aFasClasseVII.getDataAggiornamento());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.debug(" Inserimento della PenaResidua con Quantum a zero " + lPenModIns);

				lPenResDao = new PenaResiduaDAO(lConn);
				lPenResDao.setDAOFromModel(lPenModIns);
				/*BigDecimal lIdPen = */lPenResDao.insert();
				lPenResDao.stop();

			}
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new SIEPException("RichiestaConversioneController.ExArchiviazioneClasseVII: " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + e);
			throw new SIEPException("RichiestaConversioneController.ExArchiviazioneClasseVII: " + e);
		} finally {
			cleanup(lEveDao);
			cleanup(lEveSqlDAO);
			cleanup(lFasDao);
			cleanup(lStatoProcDao);
			cleanup(lRiapFasSie);
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return aFasClasseVII;
	}

}