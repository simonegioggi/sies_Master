package siap.siep.posizione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.dao.MisuraCautelareDAO;
import siap.siep.misuracautelare.dao.MisuraCautelareSqlDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.posizione.dao.PosizioneGiuridicaDAO;
import siap.siep.posizione.dao.PosizioneGiuridicaSqlDAO;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PosizioneGiuridicaController
 * </p>
 * <p>
 * Description: Classe Controller per la Posizione Giuridica
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PosizioneGiuridicaController extends SiapController implements IPosizioneGiuridica {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce la Posizione Giuridica
	 * 
	 * @param aPosizioneGiuridica
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExInserisciPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaModel lPosMod = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setDAOFromModel(aPosizioneGiuridica);

			BigDecimal lKey = null;
			lKey = lPosDao.insert();

			commit(lConn);

			lPosMod = new PosizioneGiuridicaModel();
			lPosMod.setIdPosizioneGiuridica(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PosizioneGiuridicaController.ExInserisciPosizioneGiuridica: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * ExInserisciPosizioneGiuridicaVerbaleSotto - Inserisce la posizione giudica o esegue l'update
	 * 
	 * @param aPosMod
	 * @param aKeyFasc
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExInserisciPosizioneGiuridicaVerbaleSotto(PosizioneGiuridicaModel aPosMod,
			BigDecimal aKeyFasc) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaDAO lPosDao = null;
		PosizioneGiuridicaSqlDAO lPosSqlDao = null;
		PosizioneGiuridicaModel lPosMod = null;

		try {
			lConn = getDBTransaction();

			lPosSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			lPosMod = new PosizioneGiuridicaModel(aPosMod);

			BigDecimal lIdFasc = lPosMod.getFasSieIdFascicoloSiep();
			lPosSqlDao.ricercaPosGiuCorrenteByIdFascicolo(lIdFasc);

			PosizioneGiuridicaModel lPosizMod = null;
			lPosizMod = (PosizioneGiuridicaModel) lPosSqlDao.getModelByKey();

			if (lPosizMod != null) {
				// update
				lPosDao.setIdPosizioneGiuridica(lPosizMod.getIdPosizioneGiuridica());
				lPosDao.setCodOperatoreAggiornamento(lPosMod.getCodOperatoreInserimento());
				lPosDao.setCodUfficioAggiornamento(lPosMod.getCodUfficioInserimento());
				lPosDao.setDataAggiornamento(lPosMod.getDataInserimento());
				lPosDao.setDataFine(lPosMod.getDataInizio());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();
			}
			// insert

			lPosDao.setDAOFromModel(aPosMod);
//			BigDecimal lKeyPosGiu = null;
			/*lKeyPosGiu = */lPosDao.insert();
			lPosDao.stop();

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PosizioneGiuridicaController.ExInserisciPosizioneGiuridica: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lPosSqlDao);
			cleanup(lConn);

		}

		return lPosMod;
	}

	/**
	 * ExInserisciPosizioneGiuridicaModificaFascicoloSiepAssociato - inserisce o modifica la posizione
	 * giuridica
	 * 
	 * @param aPosizioneGiuridica
	 * @param aFascicoloModel
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExInserisciPosizioneGiuridicaModificaFascicoloSiepAssociato(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel)
			throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaDAO lPosDao = null;
		FascicoloSiepDAO lFasDao = null;

		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosizioneGiuridica);

		try {
			lConn = getDBTransaction();

			// Inserimento la Posizione Giuridica (storicizzando quella eventualmente presente)

			// 1- Storicizza l'ultima occorrenza eventualmente presente
			BigDecimal lIdPosGiuCorrente = this.getIdPosizioneGiuridicaCorrente(aFascicoloModel
					.getIdFascicoloSiep());

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			if (lIdPosGiuCorrente != null) {
				lPosDao.setIdPosizioneGiuridica(lIdPosGiuCorrente);
				// Se non presente la data inizio della nuova
				// viene chiusa la vecchia con la data di sistema
				if (lPosMod.getDataInizio() == null)
					lPosDao.setDataFine(new Date());
				else
					// altrimenti viene chiusa con la data inserita
					lPosDao.setDataFine(lPosMod.getDataInizio());

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();
			}

			// 2 - Inserisce la nuova occorrenza
			lPosDao.setDAOFromModel(lPosMod);
			BigDecimal lKeyPosGiu = null;
			lKeyPosGiu = lPosDao.insert();

			// Modifica Fascicolo Associato alla Posizione Giuridica
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setFlagAltraCausa(aFascicoloModel.getFlagAltraCausa());

			lFasDao.setCodTipoPosLibero(aFascicoloModel.getCodTipoPosLibero());

			lFasDao.selCondizioneUpdate(aFascicoloModel.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConn);

			lPosMod.setIdPosizioneGiuridica(lKeyPosGiu);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExInserisciPosizioneGiuridicaModificaFascicoloSiepAssociato: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExInserisciPosizioneGiuridicaModificaFascicoloSiepAssociato: "
							+ ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * inserisce la posiizone giuridica ed il luogo di detenzione
	 * 
	 * @param aPosizioneGiuridica
	 * @param aFascicoloModel
	 * @param aLuogoDetenzione
	 * @param aAltraCausa
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa) throws F3BException {
		return ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(aPosizioneGiuridica,
				aFascicoloModel, aLuogoDetenzione, aAltraCausa, null);
	}

	public PosizioneGiuridicaModel ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa,
			MisuraCautelareModel aMisuraCautelare) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaDAO lPosDao = null;
		FascicoloSiepDAO lFasDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;
		AltraCausaDAO lAltraCausaDao = null;
		AltraCausaSqlDAO lAltraCausaSqlDao = null;
		MisuraCautelareDAO lMisCauDao = null;
		MisuraCautelareSqlDAO lMisCauSqlDao = null;
		MisuraCautelareDAO lMisCauBisDao = null;

		if (aMisuraCautelare != null)
			aPosizioneGiuridica.setLuogoEspiazione(aMisuraCautelare.getAltroLuogoDetenzione());
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosizioneGiuridica);

		try {
			lConn = getDBTransaction();

			// Inserimento la Posizione Giuridica (storicizzando quella eventualmente presente)

			// 1- Storicizza l'ultima occorrenza eventualmente presente
			BigDecimal lIdPosGiuCorrente = this.getIdPosizioneGiuridicaCorrente(aFascicoloModel
					.getIdFascicoloSiep());

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			// Se presente una posizione giuridica..
			if (lIdPosGiuCorrente != null) {
				lPosDao.setIdPosizioneGiuridica(lIdPosGiuCorrente);
				// Se non presente la data inizio della nuova
				// viene chiusa la vecchia con la data di sistema
				if (lPosMod.getDataInizio() == null)
					lPosDao.setDataFine(new Date());
				else
					// altrimenti viene chiusa con la data inserita
					lPosDao.setDataFine(lPosMod.getDataInizio());

				// if (aFascicoloModel.getFlagAltraCausa().equals("N")){
				// lPosDao.setAltCauIdAltraCausa(null);
				// }

				lPosDao.selByKey();
				lPosDao.update();
				lPosDao.stop();
			}

			// 4- Inserisce o Modifica o Cancella Altra Causa
			// Gestione Altra Causa deve essere sempre inserita o Cancellata giugno 2015
			// 4.1- Cerca Altra Causa se presente
			lAltraCausaSqlDao = new AltraCausaSqlDAO(lConn);
			lMisCauSqlDao = new MisuraCautelareSqlDAO(lConn);
			AltraCausaModel lAltraCausa = null;
			// lAltraCausaSqlDao.ricercaAltraCausaByIdFascicolo(aFascicoloModel.getIdFascicoloSiep());
			// lAltraCausa = (AltraCausaModel) lAltraCausaSqlDao.getModelByKey();

			// lMisCauSqlDao.ricercaMisuraCautelareByFascicolo(aFascicoloModel.getIdFascicoloSiep());
			MisuraCautelareModel lMisuraCautelare = null;
			if (lIdPosGiuCorrente != null) {
				lMisCauSqlDao.ricercaMisuraCautelareByPosizioneGiuridica(lIdPosGiuCorrente);
				lMisuraCautelare = (MisuraCautelareModel) lMisCauSqlDao.getModelByKey();
			}

			lAltraCausaDao = new AltraCausaDAO(lConn);
			lMisCauDao = new MisuraCautelareDAO(lConn);

			BigDecimal lKeyAltra = null;
			if (aFascicoloModel.getFlagAltraCausa() != null
					&& aFascicoloModel.getFlagAltraCausa().equals("S")) {
				// Gestione Altra Causa deve essere sempre inserita
				if (lAltraCausa == null) { // Inserisce Altra Causa
					if (aAltraCausa != null && aAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("74")
							|| aAltraCausa.getCodTipoPosGiuridica().equalsIgnoreCase("75")) {
						aAltraCausa.setDataDecorrenza(DateUtils.getSysDate());
					}
					lAltraCausaDao.setDAOFromModel(aAltraCausa);
					// BigDecimal lKeyAltra = lAltraCausaDao.insert();
					lKeyAltra = lAltraCausaDao.insert();
				}
				// else // Modifica Altra Causa
				// {
				// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// //siesLogger.error("\nModifica Altra Causa\n");
				//
				// aAltraCausa.setIdAltraCausa(lAltraCausa.getIdAltraCausa());
				//
				// lAltraCausaDao.setDAOFromModelForUpdate(aAltraCausa);
				// lAltraCausaDao.update();
				// }

				// // Gestione Misura Cautelare
				// if (lMisuraCautelare == null) // Inserisce Misura Cautelare
				// {
				// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// //siesLogger.error("\nInserisci Misura Cautelare\n");
				//
				// // verifico che la maschera contenga la misura cautelare
				// if (aMisuraCautelare!=null){
				// // allora inserisco
				// lMisCauDao.setDAOFromModel(aMisuraCautelare);
				// BigDecimal lKeyMisura = lMisCauDao.insert();
				// } else {
				// // altrimenti lascio vuoto
				// }
				//
				// }
				// else // Modifica Misura Cautelare
				// {
				// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// //siesLogger.error("\nModifica Misura Cautelare\n");
				//
				// // verifico che la maschera contenga la misura cautelare
				// if (aMisuraCautelare!=null){
				// // allora aggiorno
				// aMisuraCautelare.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
				// lMisCauDao.setDAOFromModelForUpdate(aMisuraCautelare);
				// lMisCauDao.update();
				// }
				// //TODO verificare
				// /*else {
				// // altrimenti elimino
				// lMisCauDao.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
				// lMisCauDao.selByKey();
				// lMisCauDao.delete();
				// }*/
				//
				// }
			} else {
				// Gestione Altra Causa
//				if (lAltraCausa != null) { // Cancella
//					// metto a null il campo che lega la posizione giuridica ad altra causa
//					lPosDao.setCondizioneUpdateCampoLegatoAltraCausa(lAltraCausa.getIdAltraCausa());
//					lPosDao.setAltCauIdAltraCausa(null);
//					lPosDao.update();
//					lPosDao.stop();
//					// elimino altra causa
//					lAltraCausaDao.setIdAltraCausa(lAltraCausa.getIdAltraCausa());
//					lAltraCausaDao.selByKey();
//					lAltraCausaDao.delete();
//				}

				// TODO verificare
				// Modifica 27/04/2015
				// Non bisogna eliminate le Misure Cautelari legate al Fascicolo

				// Gestione Misura Cautelare
				// if (lMisuraCautelare != null) // Cancella
				// {
				// lMisCauDao.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
				// lMisCauDao.selByKey();
				// lMisCauDao.delete();
				// }
			}

			// 2 - Inserisce la nuova occorrenza
			lPosMod.setAltCauIdAltraCausa(lKeyAltra);
			// se la Data Decorrenza(data inizio sul DB) è non valorizzata viene impostata alla data di
			// sistema
			if (lPosMod != null && lPosMod.getDataInizio() == null)
				lPosDao.setDataInizio(new Date());
			lPosDao.setDAOFromModel(lPosMod);
			BigDecimal lKeyPosGiu = null;
			lKeyPosGiu = lPosDao.insert();

			// String lTipoMisura = "AD"; // ARRESTI DOMICILIARI
			String lTipoMisura = "";
			if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("01")) { // IN CUSTODIA CAUTELARE PER
																				// QUESTA CAUSA IN REGIME DI
																				// DETENZIONE
				lTipoMisura = "CA"; // CARCERE
			} else if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("02")) {
				lTipoMisura = "AD"; // Custodia cautelare in Arresti domiciliari
			} else if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("70")) {
				lTipoMisura = "CM"; // Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr
									// 309/90
			} else if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("71")) {
				lTipoMisura = "CB"; // Custodia Cautelare in Regime di Permanenza in Casa
			} else if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("72")) {
				lTipoMisura = "CC"; // Custodia Cautelare in Collocamento in Comunita'
			} else if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("73")) {
				lTipoMisura = "CD"; // Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria
			}

			if (aMisuraCautelare != null && !lTipoMisura.equalsIgnoreCase("")) {
				aMisuraCautelare.setCodTipoMisura(lTipoMisura);
			}
			// Gestione Misura Cautelare
			if (lMisuraCautelare == null) // Inserisce Misura Cautelare
			{
				// verifico che la maschera contenga la misura cautelare
				if (aMisuraCautelare != null) {
					// allora inserisco
					aMisuraCautelare.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					// aMisuraCautelare.setCodTipoMisura(lTipoMisura);
					aMisuraCautelare.setDataInizio(aPosizioneGiuridica.getDataInizio());
					aMisuraCautelare.setFlagComputabile("S");
					lMisCauDao.setDAOFromModel(aMisuraCautelare);
					/*BigDecimal lKeyMisura = */lMisCauDao.insert();
				} else {
					// altrimenti lascio vuoto
				}
			} else // Modifica Misura Cautelare
			{
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// siesLogger.error("\nModifica Misura Cautelare\n");

				// verifico che la maschera contenga la misura cautelare
				if (aMisuraCautelare != null) {
					// allora aggiorno
					aMisuraCautelare.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
					aMisuraCautelare.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					// aMisuraCautelare.setCodTipoMisura(lTipoMisura);
					aMisuraCautelare.setDataInizio(aPosizioneGiuridica.getDataInizio());
					aMisuraCautelare.setFlagComputabile("S");
					lMisCauDao.setDAOFromModelForUpdate(aMisuraCautelare);
					lMisCauDao.update();
				} else {
					// cancella misura cautelare idposgiu corrente
					lMisCauDao.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
					lMisCauDao.selByKey();
					lMisCauDao.delete();
				}

			}

			// Modifica Fascicolo Associato alla Posizione Giuridica
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setFlagAltraCausa(aFascicoloModel.getFlagAltraCausa());
			lFasDao.setCodTipoPosLibero(aFascicoloModel.getCodTipoPosLibero());
			lFasDao.selCondizioneUpdate(aFascicoloModel.getIdFascicoloSiep());
			lFasDao.update();

			// 3 - Inserisce Luogo Detenzione (storicizzando quella eventualmente presente)
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);

			// 3.1- Storicizza l'ultima occorrenza eventualmente presente
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);

			lLuoDetSqlDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aFascicoloModel.getIdFascicoloSiep());
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();

			if (lLuogoDetenzione != null) {
				lLuoDetDao.setIdLuogoDetenzione(lLuogoDetenzione.getIdLuogoDetenzione());

				if (lLuogoDetenzione.getDataFineDetenzione() == null)
					lLuoDetDao.setDataFineDetenzione(new Date());
				lLuoDetDao.selByKey();
				lLuoDetDao.update();
				lLuoDetDao.stop();
			}

			// 3.2- Inserisce il luogo detenzione
			aLuogoDetenzione.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
			lLuoDetDao.setDAOFromModel(aLuogoDetenzione);
			lLuoDetDao.insert();

			// // 4- Inserisce o Modifica o Cancella Altra Causa
			//
			// // 4.1- Cerca Altra Causa se presente
			// lAltraCausaSqlDao = new AltraCausaSqlDAO(lConn);
			// lMisCauSqlDao = new MisuraCautelareSqlDAO(lConn);
			//
			// lAltraCausaSqlDao.ricercaAltraCausaByIdFascicolo(aFascicoloModel.getIdFascicoloSiep());
			// AltraCausaModel lAltraCausa = (AltraCausaModel) lAltraCausaSqlDao.getModelByKey();
			//
			// lMisCauSqlDao.ricercaMisuraCautelareByFascicolo(aFascicoloModel.getIdFascicoloSiep());
			// MisuraCautelareModel lMisuraCautelare = (MisuraCautelareModel) lMisCauSqlDao.getModelByKey();
			//
			// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// //siesLogger.error("####### ALtra Causa DB : "+lAltraCausa);
			//
			// lAltraCausaDao = new AltraCausaDAO(lConn);
			// lMisCauDao = new MisuraCautelareDAO(lConn);
			// if (aFascicoloModel.getFlagAltraCausa().equals("S"))
			// {
			//
			// // Gestione Altra Causa
			// if (lAltraCausa == null) // Inserisce Altra Causa
			// {
			// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// //siesLogger.error("\nInserisci Altra Causa\n");
			//
			// lAltraCausaDao.setDAOFromModel(aAltraCausa);
			// BigDecimal lKeyAltra = lAltraCausaDao.insert();
			// }
			// else // Modifica Altra Causa
			// {
			// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// //siesLogger.error("\nModifica Altra Causa\n");
			//
			// aAltraCausa.setIdAltraCausa(lAltraCausa.getIdAltraCausa());
			//
			// lAltraCausaDao.setDAOFromModelForUpdate(aAltraCausa);
			// lAltraCausaDao.update();
			// }
			//
			// // Gestione Misura Cautelare
			// if (lMisuraCautelare == null) // Inserisce Misura Cautelare
			// {
			// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// //siesLogger.error("\nInserisci Misura Cautelare\n");
			//
			// // verifico che la maschera contenga la misura cautelare
			// if (aMisuraCautelare!=null){
			// // allora inserisco
			// lMisCauDao.setDAOFromModel(aMisuraCautelare);
			// BigDecimal lKeyMisura = lMisCauDao.insert();
			// } else {
			// // altrimenti lascio vuoto
			// }
			//
			// }
			// else // Modifica Misura Cautelare
			// {
			// //// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// //siesLogger.error("\nModifica Misura Cautelare\n");
			//
			// // verifico che la maschera contenga la misura cautelare
			// if (aMisuraCautelare!=null){
			// // allora aggiorno
			// aMisuraCautelare.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
			// lMisCauDao.setDAOFromModelForUpdate(aMisuraCautelare);
			// lMisCauDao.update();
			// } else {
			// // altrimenti elimino
			// lMisCauDao.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
			// lMisCauDao.selByKey();
			// lMisCauDao.delete();
			// }
			//
			// }
			//
			// }
			// else
			// {
			//
			// // Gestione Altra Causa
			// if (lAltraCausa != null) // Cancella
			// {
			// lAltraCausaDao.setIdAltraCausa(lAltraCausa.getIdAltraCausa());
			// lAltraCausaDao.selByKey();
			// lAltraCausaDao.delete();
			// }
			//
			// // Gestione Misura Cautelare
			// if (lMisuraCautelare != null) // Cancella
			// {
			// lMisCauDao.setIdMisuraCautelare(lMisuraCautelare.getIdMisuraCautelare());
			// lMisCauDao.selByKey();
			// lMisCauDao.delete();
			// }
			// }

			// 5- Inserimento Misure Cautelari
			// se la Posizione Giuridica corrisponde ai codici :
			// 01 - IN CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI DETENZIONE,
			// 02 - IN CUSTODIA CAUTELARE PER QUESTA CAUSA IN REGIME DI ARRESTI DOMICILIARI
			// 70 - Custodia Cautelare in Regime di Arresti Domiciliare ex art 89 dpr 309/90
			// 71 - Custodia Cautelare in Regime di Permanenza in Casa
			// 72 - Custodia Cautelare in Collocamento in Comunita
			// 73 - Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria

			// if( aPosizioneGiuridica.isPrimaPosizione() &&
			// (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("01") ||
			// aPosizioneGiuridica.getCodPosizioneGiuridica().equals("02"))
			if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("01")
					|| aPosizioneGiuridica.getCodPosizioneGiuridica().equals("73")) {
				MisuraCautelareModel lMisMod = new MisuraCautelareModel();

				lTipoMisura = "AD"; // ARRESTI DOMICILIARI
				if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("01")) { // IN CUSTODIA CAUTELARE
																					// PER QUESTA CAUSA IN
																					// REGIME DI DETENZIONE
					lTipoMisura = "CA"; // CARCERE
				} else if (aPosizioneGiuridica.getCodPosizioneGiuridica().equals("73")) {
					lTipoMisura = "CD"; // Custodia Cautelare in Misura di Sicurezza Applicata in via
										// Provvisoria
				}

				lMisMod.setCodTipoMisura(lTipoMisura);

				// La Data Decorrenza Posizione è obbligatoria in questi casi (01 e 02)
				lMisMod.setDataInizio(aPosizioneGiuridica.getDataInizio());
				// modifica relativa al tipo istituto
				lMisMod.setIstDetIdIstitutoDetenzione(aLuogoDetenzione.getIstDetIdIstitutoDetenzione());
				lMisMod.setAltroLuogoDetenzione(aLuogoDetenzione.getAltroLuogo());
				// lMisMod.setCodTipoIstitutoDetenzione(aLuogoDetenzione.getCodTipoIstituto());
				// lMisMod.setAltroLuogoDetenzione(aLuogoDetenzione.getIndirizzo());
				// lMisMod.setCodLuogoDetenzione(aLuogoDetenzione.getCodLuogo());

				lMisMod.setCodTipoUfficioRifer("-");
				lMisMod.setCodLuogoUfficioRifer("-");
				lMisMod.setCodMotivoNonComputabile("-");
				lMisMod.setFlagComputabile("S");

				lMisMod.setFasSieIdFascicoloSiep(aPosizioneGiuridica.getFasSieIdFascicoloSiep());
				lMisMod.setPosGiuIdPosizioneGiuridica(aPosizioneGiuridica.getIdPosizioneGiuridica());
				lMisMod.setCodOperatoreInserimento(aPosizioneGiuridica.getCodOperatoreInserimento());
				lMisMod.setCodUfficioInserimento(aPosizioneGiuridica.getCodUfficioInserimento());
				lMisMod.setDataInserimento(aPosizioneGiuridica.getDataInserimento());
				if (aPosizioneGiuridica.getAutoritaCompetente() != null
						&& !aPosizioneGiuridica.getAutoritaCompetente().equalsIgnoreCase(""))
					lMisMod.setAutoritaCompetente(aPosizioneGiuridica.getAutoritaCompetente());
				else
					lMisMod.setAutoritaCompetente("-");

				lMisCauBisDao = new MisuraCautelareDAO(lConn);

				// 1- Cerca un eventuale misura alterativa in corso non computabile
				lMisCauBisDao.setCondizioneInCorsoComputabile(aFascicoloModel.getIdFascicoloSiep());

				MisuraCautelareModel lMisuraPresente = null;
				lMisCauBisDao.start();
				if (lMisCauBisDao.next()) {
					lMisuraPresente = (MisuraCautelareModel) lMisCauBisDao.getModel();
				}
				lMisCauBisDao.stop();

				// 2- Se presente la aggiorna altrimenti la inserisce
				if (lMisuraPresente != null) {
					lMisMod.setIdMisuraCautelare(lMisuraPresente.getIdMisuraCautelare());
					lMisMod.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					lMisCauBisDao.setDAOFromModelForUpdate(lMisMod);
					lMisCauBisDao.update();
					lMisCauBisDao.stop();
				} else {
					lMisMod.setPosGiuIdPosizioneGiuridica(lKeyPosGiu);
					lMisCauBisDao.setDAOFromModel(lMisMod);
					/*BigDecimal lKeyMisura = */lMisCauBisDao.insert();
					lMisCauBisDao.stop();
				}
			}

			commit(lConn);

			lPosMod.setIdPosizioneGiuridica(lKeyPosGiu);
		} catch (DAOException ex) {
			rollback(lConn);
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);

			ex.printStackTrace();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExInserisciPosizioneLuogoDetenzioneAltraCausaModificaFascicolo: "
							+ ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lFasDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lAltraCausaDao);
			cleanup(lAltraCausaSqlDao);
			cleanup(lMisCauDao);
			cleanup(lMisCauSqlDao);
			cleanup(lMisCauBisDao);

			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * Ricerca le posizioni giuridiche
	 * 
	 * @param aPosizioneGiuridica
	 * @return Vector
	 * @throws F3BException
	 */
	public Vector ExRicercaPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException {
		Connection lConn = null;
		Vector lPosizioneGiuridici = new Vector();
		PosizioneGiuridicaSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridica(aPosizioneGiuridica);
			lPosizioneGiuridici = new Vector(lPosDao.getModels());
			if (lPosizioneGiuridici.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExRicercaPosizioneGiuridica: " + daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosizioneGiuridici;
	}

	/**
	 * Ricerca la posizione giuridica dalla chiave
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		PosizioneGiuridicaModel lPosMod;

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);

			lPosDao.ricercaPosizioneGiuridicaByKey(aKey);

			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaByKey: " + daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * Ricerca posizione giuridica e luogo detenzione dalla chiave
	 * 
	 * @param aKey
	 *            - id posizione giuridica
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneModel ExRicercaPosizioneGiuridicaLuogoDetenzioneByKey(
			BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;

		PosizioneGiuridicaModel lPosMod;
//		LuogoDetenzioneModel lLuoDet;

		PosizioneGiuridicaLuogoDetenzioneModel lPosGiuLuoDetMod = new PosizioneGiuridicaLuogoDetenzioneModel();

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaByKey(aKey);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lPosGiuLuoDetMod.setPosizioneGiuridica(lPosMod);
			lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
			// lLuoDetDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(lPosMod.getFasSieIdFascicoloSiep());
			lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();
			lPosGiuLuoDetMod.setLuogoDetenzione(lLuogoDetenzione);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneByKey: " + daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lConn);
		}

		return lPosGiuLuoDetMod;
	}

	/**
	 * Ricerca posizione giuridica, istituto detenzione, altra causa per l'id fascicolo siep
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstDetMod = null;

		PosizioneGiuridicaModel lPosMod = null;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// ** Posizione Giuridica **
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lPosDao.ricercaPosGiuCorrenteByIdFascicoloDataFineNull(aIdFascicolo);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			// ** Luogo Detenzione **
			lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDetDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aIdFascicolo);
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

			// modifica relativa al tipo istituto
			/**************************************** Istituto Detenzione **************************************/
			if (lLuogoDetenzione != null) {
				lIstDetMod = new IstitutoDetenzioneModel();
				lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione.getIstDetIdIstitutoDetenzione());
				lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
			}
			/**************************************** Fine Istituto Detenzione *************************************/
			lMod.setLuogoDetenzione(lLuogoDetenzione);

			// ** Altra Causa **
			lAltCauDao = new AltraCausaSqlDAO(lConn);
			lAltCauDao.ricercaAltraCausaByIdFascicolo(aIdFascicolo);
			AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

			// modifica relativa al tipo istituto
			/**************************************** Istituto Detenzione **************************************/
			if (lAltraCausa != null) {
				lIstDetMod = new IstitutoDetenzioneModel();
				lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
				lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lAltraCausa.setIstitutoDetenzione(lIstDetMod);
			}
			/**************************************** Fine Istituto Detenzione *************************************/

			lMod.setAltraCausa(lAltraCausa);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			// modifica relativa al tipo istituto
			cleanup(lIstDao);

			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Ricerca posizione giuridica corrente, istituto detenzione, altra causa per l'id fascicolo siep
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaMisuraCautelareCorrentiByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;

		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstDetMod = null;

		FascicoloSiepDAO lFascDao = null;

		PosizioneGiuridicaModel lPosMod = null;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// ** Posizione Giuridica **
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lPosDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			if (lPosMod != null) {

				lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.setIdFascicoloSiep(aIdFascicolo);
				lFascDao.selCondizioneUpdate(aIdFascicolo);
				lFascDao.start();
				lFascDao.next();
				String lAltraCausaFlag = lFascDao.getFlagAltraCausa();
				lFascDao.stop();

				if (lAltraCausaFlag != null && lAltraCausaFlag.equals("S")) {

					lAltCauDao = new AltraCausaSqlDAO(lConn);
					// lAltCauDao.ricercaAltraCausaByIdFascicolo(aIdFascicolo);
					if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null)
						lAltCauDao.ricercaAltraCausaByKey(lPosMod.getAltCauIdAltraCausa());
					else
						lAltCauDao.ricercaAltraCausaByIdFascicolo(aIdFascicolo);
					AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

					// modifica relativa al tipo istituto
					/**************************************** Istituto Detenzione **************************************/
					if (lAltraCausa != null && lAltraCausa.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetMod = new IstitutoDetenzioneModel();
						lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
						lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						lAltraCausa.setIstitutoDetenzione(lIstDetMod);
					}
					/**************************************** Fine Istituto Detenzione *************************************/

					lMod.setAltraCausa(lAltraCausa);

					lMisCauDao = new MisuraCautelareSqlDAO(lConn);
					MisuraCautelareModel lMisCau = null;
					if (lPosMod != null) {
						lMisCauDao.ricercaMisuraCautelareByPosizioneGiuridica(lPosMod
								.getIdPosizioneGiuridica());
						lMisCau = (MisuraCautelareModel) lMisCauDao.getModelByKey();
					}

					lMod.setMisuraCautelare(lMisCau);

				} else {
					// ** Luogo Detenzione **
					lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
					lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
					// lLuoDetDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aIdFascicolo);
					LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

					// modifica relativa al tipo istituto
					/**************************************** Istituto Detenzione **************************************/
					if (lLuogoDetenzione != null && lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetMod = new IstitutoDetenzioneModel();
						lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione
								.getIstDetIdIstitutoDetenzione());
						lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
					}
					/**************************************** Fine Istituto Detenzione *************************************/
					lMod.setLuogoDetenzione(lLuogoDetenzione);
				}
				// ** Altra Causa **
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			cleanup(lMisCauDao);

			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lFascDao);

			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Ricerca posizione giuridica corrente, istituto detenzione, altra causa per l'id fascicolo siep
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;

		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstDetMod = null;

		FascicoloSiepDAO lFascDao = null;

		PosizioneGiuridicaModel lPosMod = null;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// ** Posizione Giuridica **
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lPosDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			if (lPosMod != null) {

				lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.setIdFascicoloSiep(aIdFascicolo);
				lFascDao.selCondizioneUpdate(aIdFascicolo);
				lFascDao.start();
				lFascDao.next();
				String lAltraCausaFlag = lFascDao.getFlagAltraCausa();
				lFascDao.stop();

				if (lAltraCausaFlag != null && lAltraCausaFlag.equals("S")) {

					lAltCauDao = new AltraCausaSqlDAO(lConn);
					// lAltCauDao.ricercaAltraCausaByIdFascicolo(aIdFascicolo);

					// cerco ALTRA_CAUSA legata alla mia posizione giuriddica
					lAltCauDao.ricercaAltraCausaByKey(lPosMod.getAltCauIdAltraCausa());
					AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

					// modifica relativa al tipo istituto
					/**************************************** Istituto Detenzione **************************************/
					if (lAltraCausa != null && lAltraCausa.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetMod = new IstitutoDetenzioneModel();
						lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
						lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						lAltraCausa.setIstitutoDetenzione(lIstDetMod);
					}
					/**************************************** Fine Istituto Detenzione *************************************/

					lMod.setAltraCausa(lAltraCausa);

					lMisCauDao = new MisuraCautelareSqlDAO(lConn);
					lMisCauDao.ricercaMisuraCautelareByFascicolo(aIdFascicolo);
					MisuraCautelareModel lMisCau = (MisuraCautelareModel) lMisCauDao.getModelByKey();

					lMod.setMisuraCautelare(lMisCau);

				} else {
					// ** Luogo Detenzione **
					lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
					lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
					// lLuoDetDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aIdFascicolo);
					LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

					// modifica relativa al tipo istituto
					/**************************************** Istituto Detenzione **************************************/
					if (lLuogoDetenzione != null && lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetMod = new IstitutoDetenzioneModel();
						lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione
								.getIstDetIdIstitutoDetenzione());
						lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
					}
					/**************************************** Fine Istituto Detenzione *************************************/
					lMod.setLuogoDetenzione(lLuogoDetenzione);
				}
				// ** Altra Causa **
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			cleanup(lMisCauDao);

			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lFascDao);

			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Ricerca posizione giuridica, istituto detenzione, altra causa per l'id posizione giuridica
	 * 
	 * @param aIdPosizione
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey(
			BigDecimal aIdPosizione) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		MisuraCautelareSqlDAO lMisCauDao = null;

		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;

		PosizioneGiuridicaModel lPosMod;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneModel lIstDetMod;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);

			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaByKey(aIdPosizione);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
			lAltCauDao = new AltraCausaSqlDAO(lConn);
			if (lPosMod != null) {
				lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(aIdPosizione);
				LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

				// modifica relativa al tipo istituto
				if (lLuogoDetenzione != null) {
					lIstDetMod = new IstitutoDetenzioneModel();
					lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione.getIstDetIdIstitutoDetenzione());
					lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
				}
				// fine modifica relativa al tipo istituto

				lMod.setLuogoDetenzione(lLuogoDetenzione);

				// Associata alla Posizione Giuridica ( e al Fascicolo Siep ) è associato
				// una sola occorrenza della tabella ALTRA_CAUSA

				// lAltCauDao.ricercaAltraCausaByIdFascicolo(lPosMod.getFasSieIdFascicoloSiep());
				if (lPosMod != null && lPosMod.getAltCauIdAltraCausa() != null)
					lAltCauDao.ricercaAltraCausaByKey(lPosMod.getAltCauIdAltraCausa());
				else
					lAltCauDao.ricercaAltraCausaByIdFascicolo(lPosMod.getFasSieIdFascicoloSiep());
				AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();
				// modifica relativa al tipo istituto
				/**************************************** Istituto Detenzione **************************************/
				if (lAltraCausa != null) {
					lIstDetMod = new IstitutoDetenzioneModel();
					lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
					lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lAltraCausa.setIstitutoDetenzione(lIstDetMod);
				}
				/**************************************** Fine Istituto Detenzione *************************************/

				lMod.setAltraCausa(lAltraCausa);

				lMisCauDao = new MisuraCautelareSqlDAO(lConn);
				// lMisCauDao.ricercaMisuraCautelareByFascicolo(lPosMod.getFasSieIdFascicoloSiep());
				lMisCauDao.ricercaMisuraCautelareByPosizioneGiuridica(aIdPosizione);
				MisuraCautelareModel lMisCau = (MisuraCautelareModel) lMisCauDao.getModelByKey();

				lMod.setMisuraCautelare(lMisCau);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByKey: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			cleanup(lMisCauDao);

			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Ricerca posizione giuridica, istituto detenzione, altra causa per l'id evento
	 * 
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdEvento(
			BigDecimal aIdEvento) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;

		PosizioneGiuridicaModel lPosMod;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneModel lIstDetMod;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);

			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaByIdEvento(aIdEvento);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			if (lPosMod != null) {
				lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
				lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

				// modifica relativa al tipo istituto
				if (lLuogoDetenzione != null) {
					lIstDetMod = new IstitutoDetenzioneModel();
					lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione.getIstDetIdIstitutoDetenzione());
					lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
				}
				// fine modifica relativa al tipo istituto

				lMod.setLuogoDetenzione(lLuogoDetenzione);

				// Associata alla Posizione Giuridica ( e al Fascicolo Siep ) è associato
				// una sola occorrenza della tabella ALTRA_CAUSA
				lAltCauDao = new AltraCausaSqlDAO(lConn);
				// lAltCauDao.ricercaAltraCausaByIdFascicolo(lPosMod.getFasSieIdFascicoloSiep());

				// cerco ALTRA_CAUSA legata alla mia posizione giuriddica
				lAltCauDao.ricercaAltraCausaByKey(lPosMod.getAltCauIdAltraCausa());

				AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();
				// modifica relativa al tipo istituto

				/**************************************** Istituto Detenzione **************************************/
				if (lAltraCausa != null) {
					lIstDetMod = new IstitutoDetenzioneModel();
					lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
					lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
					lAltraCausa.setIstitutoDetenzione(lIstDetMod);
				}
				/**************************************** Fine Istituto Detenzione *************************************/

				lMod.setAltraCausa(lAltraCausa);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdEvento: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Ricerca la posizione giuridica tramite la chiave del fascicolo
	 * 
	 * @param aKey
	 * @return List
	 * @throws F3BException
	 */
	public List ExRicercaPosizioneGiuridicaByIdFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		List lPosizioni = new ArrayList();

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);

			lPosDao.ricercaPosizioneGiuridicaByIdFascicolo(aKey);

			lPosizioni = new Vector(lPosDao.getModels());

			if (lPosizioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaByIdFascicolo: "
					+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosizioni;
	}

	/**
	 * Ricerca la posizione giuridica tramite l'Id Evento
	 * 
	 * @param aIdEvento
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaByIdEvento(BigDecimal aIdEvento)
			throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		PosizioneGiuridicaModel lPosModel = null;
		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaByIdEvento(aIdEvento);
			lPosModel = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaByIdEvento: "
					+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosModel;
	}

	/**
	 * ricerca posizione giuridica precedente
	 * 
	 * @param aKey
	 *            Id Fascicolo
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		Vector lPosizioni = new Vector();
		PosizioneGiuridicaModel lPoModelPrec = new PosizioneGiuridicaModel();

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaByIdFascicoloDesc(aKey);
			lPosizioni = new Vector(lPosDao.getModels());
			if (lPosizioni.size() > 1) {
				lPoModelPrec = (PosizioneGiuridicaModel) lPosizioni.get(1);
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPoModelPrec;
	}

	/**
	 * Ricerca Posizione Giuridica By Id Fascicolo senza rilanciare errore di non trovato
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public List ExRicercaPosizioneGiuridicaByIdFascicoloNoError(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		List lPosizioni = new ArrayList();

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaByIdFascicolo(aKey);
			lPosizioni = new Vector(lPosDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaByIdFascicolo: "
					+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosizioni;
	}

	/**
	 * Modifica posizioni Giuridiche
	 * 
	 * @param aPosizioneGiuridica
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExModificaPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica)
			throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaDAO lPosDao = null;

		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosizioneGiuridica);

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setDAOFromModelForUpdate(lPosMod);
			lPosDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PosizioneGiuridicaController.ExModificaPosizioneGiuridica: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * Modifica posizioni Giuridiche
	 * 
	 * @param aPosizioneGiuridica
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExModificaPosizioneGiuridicaIdPosGiu(
			PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaDAO lPosDao = null;

		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel(aPosizioneGiuridica);

		try {
			lConn = getDBConnection();

			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setDAOFromModelForUpdate(lPosMod);
			lPosDao.setCondizioneUpdate(aPosizioneGiuridica.getIdPosizioneGiuridica());
			lPosDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PosizioneGiuridicaController.ExModificaPosizioneGiuridica: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * Modifica Posizione Giuridica Modifica Fascicolo Siep Associato
	 * 
	 * @param aPosizioneGiuridica
	 * @param aFascicoloModel
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociato(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel)
			throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaModel lPosMod = aPosizioneGiuridica;

		PosizioneGiuridicaDAO lPosDao = null;
		FascicoloSiepDAO lFasDao = null;

		try {
			lConn = getDBTransaction();

			// Modifica la posizione giuridica
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setDAOFromModelForUpdate(lPosMod);
			lPosDao.update();

			// Modifica Fascicolo Associato alla Posizione Giuridica
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setFlagAltraCausa(aFascicoloModel.getFlagAltraCausa());

			lFasDao.selCondizioneUpdate(aFascicoloModel.getIdFascicoloSiep());
			lFasDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociato: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociato: "
							+ ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lFasDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * Modifica Posizione Giuridica Modifica FascicoloSiep Associato e LuogoD etenzione
	 * 
	 * @param aPosizioneGiuridica
	 * @param aFascicoloModel
	 * @param aLuogoDetenzione
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociatoLuogoDetenzione(
			PosizioneGiuridicaModel aPosizioneGiuridica, FascicoloSiepModel aFascicoloModel,
			LuogoDetenzioneModel aLuogoDetenzione) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaModel lPosMod = aPosizioneGiuridica;

		PosizioneGiuridicaDAO lPosDao = null;
		FascicoloSiepDAO lFasDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		LuogoDetenzioneSqlDAO lLuoDetSqlDao = null;

		try {
			lConn = getDBTransaction();

			// Modifica la posizione giuridica
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setDAOFromModelForUpdate(lPosMod);
			lPosDao.update();

			// Modifica Fascicolo Associato alla Posizione Giuridica
			lFasDao = new FascicoloSiepDAO(lConn);

			lFasDao.setFlagAltraCausa(aFascicoloModel.getFlagAltraCausa());
			lFasDao.selCondizioneUpdate(aFascicoloModel.getIdFascicoloSiep());
			lFasDao.update();

			// 3 - Inserisce Luogo Detenzione (storicizzando quella eventualmente presente)
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);

			// 3.1- Storicizza l'ultima occorrenza eventualmente presente
			lLuoDetSqlDao = new LuogoDetenzioneSqlDAO(lConn);

			lLuoDetSqlDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aFascicoloModel.getIdFascicoloSiep());
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetSqlDao.getModelByKey();

			if (lLuogoDetenzione != null) {
				lLuoDetDao.setIdLuogoDetenzione(lLuogoDetenzione.getIdLuogoDetenzione());

				if (lLuogoDetenzione.getDataFineDetenzione() == null)
					lLuoDetDao.setDataFineDetenzione(new Date());
				lLuoDetDao.selByKey();
				lLuoDetDao.update();
				lLuoDetDao.stop();
			}

			// 3.2- Inserisce il luogo detenzione
			lLuoDetDao.setDAOFromModel(aLuogoDetenzione);
			lLuoDetDao.insert();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociatoLuogoDetenzione: "
							+ ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaController.ExModificaPosizioneGiuridicaModificaFascicoloSiepAssociatoLuogoDetenzione: "
							+ ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lFasDao);
			cleanup(lLuoDetDao);
			cleanup(lLuoDetSqlDao);
			cleanup(lConn);
		}

		return lPosMod;
	}

	/**
	 * Ricerca la Posizione Giuridica corrente
	 * 
	 * @param aPosizioneGiuridica
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaCorrente(
			PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaModel lModel = new PosizioneGiuridicaModel();
		PosizioneGiuridicaSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosizioneGiuridicaCorrente(aPosizioneGiuridica);
			lPosDao.start();
			if (lPosDao.next()) {
				lModel = (PosizioneGiuridicaModel) lPosDao.getModel();
			}

			lPosDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaCorrente: "
					+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lModel;
	}

	/**
	 * Ricerca Posizione Giuridica Corrente By Id Fascicolo
	 * 
	 * @param aIdFascicolo
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaModel lModel = null;
		PosizioneGiuridicaSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			lPosDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);

			lPosDao.start();
			if (lPosDao.next()) {
				lModel = (PosizioneGiuridicaModel) lPosDao.getModel();
			}
			lPosDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lModel;
	}

	/**
	 * Ricerca Posizione Giuridica Corrente By Id Fascicolo con data fine = null
	 * 
	 * @param aIdFascicolo
	 * @return PosizioneGiuridicaModel
	 * @throws F3BException
	 */
	public PosizioneGiuridicaModel ExRicercaPosizioneGiuridicaCorrenteByIdFascicoloDataFineNull(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaModel lModel = null;
		PosizioneGiuridicaSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			// lPosDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);
			lPosDao.ricercaPosGiuCorrenteByIdFascicoloDataFineNull(aIdFascicolo);

			lPosDao.start();
			if (lPosDao.next()) {
				lModel = (PosizioneGiuridicaModel) lPosDao.getModel();
			}
			lPosDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lModel;
	}

	/**
	 * Cancella la posizione giuridica
	 * 
	 * @param aPosizioneGiuridica
	 * @throws F3BException
	 */
	public void ExCancellaPosizioneGiuridica(PosizioneGiuridicaModel aPosizioneGiuridica) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneGiuridicaDAO(lConn);
			lPosDao.setCondizioneUpdate(aPosizioneGiuridica.getIdPosizioneGiuridica());
			lPosDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PosizioneGiuridicaController.ExCancellaPosizioneGiuridica: " + daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
	}

	/**
	 * get Id Posizione Giuridica Corrente
	 * 
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	private BigDecimal getIdPosizioneGiuridicaCorrente(BigDecimal aIdFascicoloSiep) throws F3BException {
		Connection lConn = null;
		BigDecimal lId = null;
		PosizioneGiuridicaSqlDAO lSqlDao = null;

		try {
			lConn = getDBConnection();
			lSqlDao = new PosizioneGiuridicaSqlDAO(lConn);
			lSqlDao.ricercaMaxIdPosizioneGiuridicaByIdFascicoloSiep(aIdFascicoloSiep);
			lSqlDao.start();
			if (lSqlDao.next()) {
				lId = lSqlDao.getIdPosizioneGiuridica();
			}
			lSqlDao.stop();
		} catch (DAOException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PosizioneGiuridicaController.getIdPosizioneGiuridicaCorrente: " + ex);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}

		return lId;
	}

	/**
	 * Ricerca Posizione Giuridica Luogo Detenzione Altra Causa Correnti By Id Fascicolo Sius
	 * 
	 * @param aIdFascicolo
	 * @param aIdFascicoloSius
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicoloSius(
			BigDecimal aIdFascicolo, BigDecimal aIdFascicoloSius) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstDetMod = null;

		PosizioneGiuridicaModel lPosMod = null;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// ** Posizione Giuridica **
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lPosDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			// ** Luogo Detenzione **
			lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDetDao.ricercaLuogoDetenzioneCorrenteByFascicoloSius(aIdFascicoloSius);
			LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

			/**************************************** Istituto Detenzione **************************************/
			if (lLuogoDetenzione != null) {
				lIstDetMod = new IstitutoDetenzioneModel();
				lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione.getIstDetIdIstitutoDetenzione());
				lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
			}
			/**************************************** Fine Istituto Detenzione *************************************/
			lMod.setLuogoDetenzione(lLuogoDetenzione);

			// ** Altra Causa **
			lAltCauDao = new AltraCausaSqlDAO(lConn);
			lAltCauDao.ricercaAltraCausaByIdFascicolo(aIdFascicolo);
			AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

			/**************************************** Istituto Detenzione **************************************/
			if (lAltraCausa != null) {
				lIstDetMod = new IstitutoDetenzioneModel();
				lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
				lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lAltraCausa.setIstitutoDetenzione(lIstDetMod);
			}
			/**************************************** Fine Istituto Detenzione *************************************/

			lMod.setAltraCausa(lAltraCausa);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}

		return lMod;
	}

	/**
	 * Inserisci Posizione Giuridica WithoutSequence
	 * 
	 * @param lPos
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciPosizioneGiuridicaWithoutSequence(PosizioneGiuridicaModel lPos, Connection lConn)
			throws F3BException {
		PosizioneGiuridicaDAO lPosDao = null;
		String lCodEsito = "00000";

		try {
			lPosDao = new PosizioneGiuridicaDAO(lConn);

			lPosDao.setDAOFromModel(lPos);
			lPosDao.setWithoutSequence(true);
			lPosDao.insert();
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Posizione Giuridica gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire la Posizione Giuridica! ");
			}
		} finally {
			cleanup(lPosDao);
		}

		return lCodEsito;
	}

	/**
	 * Ricerca posizione giuridica corrente, istituto detenzione, altra causa per l'id fascicolo siep
	 * Modificata dall'originale per la gestione del Luogo Detenzione che ora viene recuperato anche per
	 * detenuto Altra Causa
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo2(
			BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		PosizioneGiuridicaSqlDAO lPosDao = null;
		LuogoDetenzioneSqlDAO lLuoDetDao = null;
		AltraCausaSqlDAO lAltCauDao = null;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstDetMod = null;

		FascicoloSiepDAO lFascDao = null;

		PosizioneGiuridicaModel lPosMod = null;
//		LuogoDetenzioneModel lLuoDet;
//		AltraCausaModel lAltCau;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();

		try {
			lConn = getDBConnection();

			// ** Posizione Giuridica **
			lPosDao = new PosizioneGiuridicaSqlDAO(lConn);
			// modifica relativa al tipo istituto
			lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
			lPosDao.ricercaPosGiuCorrenteByIdFascicolo(aIdFascicolo);
			lPosMod = (PosizioneGiuridicaModel) lPosDao.getModelByKey();
			lMod.setPosizioneGiuridica(lPosMod);

			if (lPosMod != null) {

				lFascDao = new FascicoloSiepDAO(lConn);
				lFascDao.setIdFascicoloSiep(aIdFascicolo);
				lFascDao.selCondizioneUpdate(aIdFascicolo);
				lFascDao.start();
				lFascDao.next();
				String lAltraCausaFlag = lFascDao.getFlagAltraCausa();
				lFascDao.stop();

				// ** Luogo Detenzione **
				lLuoDetDao = new LuogoDetenzioneSqlDAO(lConn);
				lLuoDetDao.ricercaLuogoDetenzioneByIdPosizione(lPosMod.getIdPosizioneGiuridica());
				// lLuoDetDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aIdFascicolo);
				LuogoDetenzioneModel lLuogoDetenzione = (LuogoDetenzioneModel) lLuoDetDao.getModelByKey();

				if (lAltraCausaFlag != null && lAltraCausaFlag.equals("S")) {

					lAltCauDao = new AltraCausaSqlDAO(lConn);
					lAltCauDao.ricercaAltraCausaByIdFascicolo(aIdFascicolo);
					AltraCausaModel lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

					// modifica relativa al tipo istituto
					/**************************************** Istituto Detenzione **************************************/
					if (lAltraCausa != null && lAltraCausa.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetMod = new IstitutoDetenzioneModel();
						lIstDao.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
						lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						lAltraCausa.setIstitutoDetenzione(lIstDetMod);
					}
					/**************************************** Fine Istituto Detenzione *************************************/

					lMod.setAltraCausa(lAltraCausa);

				} else {

					// modifica relativa al tipo istituto
					/**************************************** Istituto Detenzione **************************************/
					if (lLuogoDetenzione != null && lLuogoDetenzione.getIstDetIdIstitutoDetenzione() != null) {
						lIstDetMod = new IstitutoDetenzioneModel();
						lIstDao.ricercaIstitutoDetenzioneByKey(lLuogoDetenzione
								.getIstDetIdIstitutoDetenzione());
						lIstDetMod = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
						lLuogoDetenzione.setIstitutoDetenzione(lIstDetMod);
					}
					/**************************************** Fine Istituto Detenzione *************************************/

				}
				// ** Altra Causa **

				lMod.setLuogoDetenzione(lLuogoDetenzione);

			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo: "
							+ daoEx);
		} finally {
			cleanup(lPosDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			// modifica relativa al tipo istituto
			cleanup(lIstDao);
			cleanup(lFascDao);

			cleanup(lConn);
		}

		return lMod;
	}

}